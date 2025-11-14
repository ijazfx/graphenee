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
    public static final String SHARED_DOCUMENT_PATH = "/v1/shared-document";

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

    @RequestMapping(value = SHARED_DOCUMENT_PATH, method = { RequestMethod.HEAD, RequestMethod.GET })
    public ResponseEntity<Resource> downloadSharedAttachment(
            @RequestParam("documentId") String documentId,
            @RequestParam("shareKey") String shareKey) {

        log.info("Received request to download shared document with documentId: {}", documentId);

        // 1. Retrieve Document Metadata
        Optional<GxDocument> documentOptional = documentRepository.findTop1ByDocumentId(UUID.fromString(documentId));

        if (!documentOptional.isPresent()) {
            log.warn("Shared document not found for documentId: {}", documentId);
            return ResponseEntity.notFound().build();
        }

        GxDocument doc = documentOptional.get();

        // 2. Validate Share Key
        if (doc.getShareKey() == null || !doc.getShareKey().toString().equals(shareKey)) {
            log.warn("Invalid shareKey provided for documentId: {}. Access denied.", documentId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // 3. Resolve the file stream
            InputStream istream = attachmentStream(doc);

            // Use InputStreamResource to allow Spring to stream the file data
            InputStreamResource resource = new InputStreamResource(istream);

            // 4. Set HTTP Headers
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getName() + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, doc.getMimeType());

            // 5. Return ResponseEntity
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(doc.getSize())
                    .contentType(MediaType.parseMediaType(doc.getMimeType()))
                    .body(resource);

        } catch (Exception ex) {
            // Log the error
            log.error("Error while preparing file download for shared documentId: {}", documentId, ex);

            // Return 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private InputStream attachmentStream(GxDocument document) throws ResolveFailedException {
        String resourcePath = fileStorage.resourcePath("documents", document.getPath());
        InputStream istream = fileStorage.resolve(resourcePath);
        return istream;
    }

}
