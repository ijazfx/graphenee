package io.graphenee.od;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.UrlEscapers;
import io.graphenee.util.storage.FileSharingFailedException;
import io.graphenee.util.storage.FileStorage;
import io.graphenee.util.storage.ResolveFailedException;
import io.graphenee.util.storage.SaveFailedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.DavServletResponse;
import org.apache.jackrabbit.webdav.MultiStatus;
import org.apache.jackrabbit.webdav.MultiStatusResponse;
import org.apache.jackrabbit.webdav.client.methods.HttpMkcol;
import org.apache.jackrabbit.webdav.client.methods.HttpPropfind;
import org.apache.jackrabbit.webdav.property.DavPropertyName;
import org.apache.jackrabbit.webdav.property.DavPropertyNameSet;
import org.apache.jackrabbit.webdav.property.DavPropertySet;
import org.apache.jackrabbit.webdav.security.SecurityConstants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import static org.apache.jackrabbit.webdav.DavConstants.DEPTH_0;

@Slf4j
public class OwnCloudFileStorage implements FileStorage {

    private HttpClient httpClient;
    private String rootFolder;
    private String rootFolderPath;

    public OwnCloudFileStorage(String rootFolder, String webDevBaseUrl, String username, String password) {

        this.rootFolder = webDevBaseUrl;
        this.rootFolderPath = rootFolder;
        String credentials = username + ":" + password;
        Collection<Header> defaultHeaders = List.of(
                new BasicHeader(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes()))
        );
        httpClient = HttpClientBuilder.create()
                .setDefaultHeaders(defaultHeaders)
                .setMaxConnPerRoute(20)
                .setMaxConnTotal(50)
                .build();
        if (createSingleDirectory(rootFolder)) {
            this.rootFolder = this.rootFolder + "/" + rootFolder;
        }

    }

    @Override
    public boolean exists(String fileName) {
        try {
            HttpPropfind httpPropfind = new HttpPropfind(rootFolder + "/" +
                    UrlEscapers.urlPathSegmentEscaper().escape(fileName).replace("%2F", "/"),
                    getPropertyNamesForFetch(),
                    DEPTH_0);
            HttpResponse response = httpClient.execute(httpPropfind);
            if (response.getStatusLine().getStatusCode() == DavServletResponse.SC_MULTI_STATUS) {
                MultiStatus multiStatus = httpPropfind.getResponseBodyAsMultiStatus(response);
                MultiStatusResponse[] responses = multiStatus.getResponses();
                DavPropertySet foundProperties = responses[0].getProperties(HttpStatus.SC_OK);
                if (foundProperties != null) {
                    return true;
                }
            }
        } catch (IOException | DavException e) {
            log.error("Error while searching file: {}", e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void createDirectory(final String directoryPath) {
        if (StringUtils.isBlank(directoryPath)) {
            return;
        }
        String[] folders = directoryPath.split("/");
        String fPath = folders[0];
        for (String folder : folders) {
            createSingleDirectory(fPath);
            fPath = fPath + "/" + folder;
        }
    }

    private boolean createSingleDirectory(final String directoryPath) {
        String completePath = this.rootFolder + "/" + directoryPath;
        HttpMkcol httpMkcol = new HttpMkcol(completePath);
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpMkcol);
            int statusCode = response.getStatusLine().getStatusCode();
            return switch (statusCode) {
                case 201 -> {
                    log.info("Folder Created: {}", completePath);
                    yield true;
                }
                case 405 -> {
                    log.info("Folder already exist: {}", completePath);
                    yield true;
                }
                case 409 -> {
                    log.info("Parent Folder already exist: {}", completePath);
                    yield false;
                }
                default -> {
                    log.info("Error while creating folder: {}", statusCode);
                    yield false;
                }
            };
        } catch (IOException e) {
            log.info("Error while creating folder: {}", e.getMessage());
        }
        return false;
    }

    @Override
    public Future<FileMetaData> save(String folder, String fileName, InputStream inputStream) throws SaveFailedException {
        return Executors.newVirtualThreadPerTaskExecutor().submit(() -> {
            this.createDirectory(folder);
            String resourcePath = this.resourcePath(folder, UrlEscapers.urlPathSegmentEscaper().escape(fileName));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            long checksum = 0;

            try (CheckedInputStream checkedInputStream = new CheckedInputStream(inputStream, new CRC32())) {
                while ((bytesRead = checkedInputStream.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
                checksum = checkedInputStream.getChecksum().getValue();
            }

            byte[] fileData = baos.toByteArray();
            long contentLength = fileData.length;
            try {
                HttpPut httpPut = new HttpPut(this.rootFolder + "/" + resourcePath);
                InputStreamEntity entity = new InputStreamEntity(new ByteArrayInputStream(fileData), contentLength);
                httpPut.setEntity(entity);
                HttpResponse response = httpClient.execute(httpPut);
                if (response.getStatusLine().getStatusCode() == 204 || response.getStatusLine().getStatusCode() == 201) {
                    long fileSize = fileData.length;
                    return new FileStorage.FileMetaData(resourcePath, fileName, Long.valueOf(fileSize).intValue(),
                            String.valueOf(checksum));
                }
                return null;
            } catch (Exception e) {
                throw new SaveFailedException(e);
            }
        });
    }

    @Override
    public InputStream resolve(String resourcePath) throws ResolveFailedException {
        if (this.exists(resourcePath)) {
            try {
                HttpGet httpGet = new HttpGet(this.rootFolder + "/" +
                        UrlEscapers.urlPathSegmentEscaper().escape(resourcePath).replace("%2F", "/"));

                // Execute the request and get the response.
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();

                // Check if the response entity is not null and the status is successful.
                if (entity != null && response.getStatusLine().getStatusCode() == 200) {
                    return entity.getContent();
                }
            } catch (IOException e) {
                throw new ResolveFailedException("Failed to resolve resource " + resourcePath, e);
            }
        } else {
            throw new ResolveFailedException("Failed to resolve resource " + resourcePath);
        }
        return null;
    }

    @Override
    public String share(String filePath) throws FileSharingFailedException {
        String fileSharingEndpoint = "/ocs/v2.php/apps/files_sharing/api/v1/shares?format=json";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String hostName = URIUtils.extractHost(new URI(rootFolder)).toURI();
            FileSharingRequest fileSharingRequest = FileSharingRequest.builder()
                    .shareType(3)
                    .path("/" + rootFolderPath + "/" + filePath)
                    .name("Grpahenee")
                    .expireDate("")
                    .permissions(1)
                    .password("")
                    .build();
            HttpPost httpPost = new HttpPost(hostName + fileSharingEndpoint);
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(fileSharingRequest)));
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                OCSResponse<Response<FleSharingResponse>> fileSharingResponse =
                        objectMapper.readValue(response.getEntity().getContent(), new TypeReference<OCSResponse<Response<FleSharingResponse>>>() {
                        });
                return fileSharingResponse.getOcs().getData().getUrl();
            } else {
                log.error("Creating file sharing link failed. Status code: {}", response.getStatusLine().getStatusCode());
                throw new FileSharingFailedException("Creating file sharing link failed. Status code: " + response.getStatusLine().getStatusCode());
            }
        } catch (URISyntaxException e) {
            log.error("OD Cloud Base URL not correct: " + e.getMessage());
            throw new FileSharingFailedException("OD Cloud Base URL not correct: " + e.getMessage(), e.getCause());
        } catch (UnsupportedEncodingException e) {
            log.error("Encoded Exception: " + e.getMessage());
            throw new FileSharingFailedException("Encoded Exception: " + e.getMessage(), e.getCause());
        } catch (JsonProcessingException e) {
            log.error("OD Cloud Response is not correct: " + e.getMessage());
            throw new FileSharingFailedException("OD Cloud Response is not correct: " + e.getMessage(), e.getCause());
        } catch (ClientProtocolException e) {
            log.error("Connection failed: " + e.getMessage());
            throw new FileSharingFailedException("Connection failed: " + e.getMessage(), e.getCause());
        } catch (IOException e) {
            log.error("IO Exception :" + e.getMessage());
            throw new FileSharingFailedException("IO Exception :" + e.getMessage(), e.getCause());
        }
    }

    private DavPropertyNameSet getPropertyNamesForFetch() {
        DavPropertyNameSet baseProperties = new DavPropertyNameSet();
        baseProperties.add(DavPropertyName.RESOURCETYPE);
        baseProperties.add(DavPropertyName.DISPLAYNAME);
        baseProperties.add(SecurityConstants.OWNER);
        baseProperties.add(SecurityConstants.CURRENT_USER_PRIVILEGE_SET);

        return baseProperties;
    }
}
