package io.graphenee.documents.controller;

import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.core.model.jpa.repository.GxDocumentRepository;
import io.graphenee.util.storage.FileStorage;
import io.graphenee.util.storage.ResolveFailedException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/gx/dms")
public class GxFileDownloadController {

    @Autowired
    FileStorage fileStorage;

    @Autowired
    GxDocumentRepository documentRepository;

    public static final String DOCUMENT_PATH = "/v1/document";

    /**
     * Endpoint to download an attachment.
     * Access to this method requires a valid JWT, which is checked by
     * GxJwtRequestFilter
     * and enforced by your Spring Security configuration.
     * The token's user identity is available via SecurityContextHolder.
     */
    
    @RequestMapping(value = DOCUMENT_PATH, method = { RequestMethod.HEAD, RequestMethod.GET })
    public ResponseEntity<Resource> downloadAttachment(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("documentId") String documentId) {

        // 1. Retrieve Document Metadata
        Optional<GxDocument> document = documentRepository.findTop1ByDocumentId(UUID.fromString(documentId));

        if (!document.isPresent()) {
            // Return 404 Not Found using ResponseEntity
            return ResponseEntity.notFound().build();
        }

        GxDocument doc = document.get();

        try {
            // 2. Resolve the file stream
            InputStream istream = attachmentStream(doc);
            
            // Use InputStreamResource to allow Spring to stream the file data
            InputStreamResource resource = new InputStreamResource(istream);

            // 3. Set HTTP Headers
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getName() + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, doc.getMimeType()); // Note: Content-Type can be set in the builder too.

            // 4. Return ResponseEntity
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(doc.getSize()) // Use Content-Length from the metadata
                    .contentType(MediaType.parseMediaType(doc.getMimeType()))
                    .body(resource);

        } catch (Exception ex) {
            // Log the error
            log.warn("Error while preparing file download for documentId: {}", documentId, ex);
            
            // Return 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    
    
    private InputStream attachmentStream(GxDocument document) throws ResolveFailedException {
        String resourcePath = fileStorage.resourcePath("documents", document.getPath());
        InputStream istream = fileStorage.resolve(resourcePath);
        return istream;
    }
    
     // @RequestMapping(value = DOWNLOAD_ATTACHMENT, method = { RequestMethod.HEAD, RequestMethod.GET })
    // public void downloadAttachment(

    //         @RequestHeader("Authorization") String authorizationHeader,
    //         @RequestParam("documentId") String documentId,
    //         HttpServletRequest request,
    //         HttpServletResponse response) throws Exception {

    //     Optional<GxDocument> document = documentRepository.findTop1ByDocumentId(UUID.fromString(documentId));

    //     if (!document.isPresent()) {

    //         response.sendError(HttpServletResponse.SC_NOT_FOUND);
    //         return;
    //     }

    //     downloadContent(document.get(), response);
    // }

    // private void downloadContent(GxDocument document, HttpServletResponse response)
    //         throws IOException {
    //     response.setStatus(200);
    //     response.setContentType(document.getMimeType());
    //     response.setContentLengthLong(document.getSize());
    //     response.setHeader("Content-Disposition", "attachment; filename=" + document.getName());
    //     try {
    //         InputStream istream = attachmentStream(document);
    //         StreamUtils.copy(istream, response.getOutputStream());
    //         istream.close();
    //     } catch (Exception ex) {
    //         log.warn(ex.getMessage());
    //         if (!response.isCommitted())
    //             response.sendError(500, ex.getMessage());
    //     }
    // }


}
