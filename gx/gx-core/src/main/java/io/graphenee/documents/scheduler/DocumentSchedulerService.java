package io.graphenee.documents.scheduler;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.graphenee.core.GxAuditLogDataService;
import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.core.model.jpa.repository.GxDocumentRepository;
import io.graphenee.core.model.jpa.repository.GxFolderRepository;
import io.graphenee.documents.GxDocumentExplorerService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@ConditionalOnProperty(name = "dms.scheduler.enabled", havingValue = "true", matchIfMissing = false)
public class DocumentSchedulerService {

    @Autowired
    private GxDocumentRepository documentRepository;

    @Autowired
    private GxFolderRepository folderRepository;

    @Autowired
    private GxDocumentExplorerService explorerService;

    @Autowired
    private GxAuditLogDataService auditLogDataService;

    @Scheduled(cron = "0 0 1 * * ?") // Runs every day at 1 AM
    @Transactional
    public void deleteArchivedDocumentsAndFolders() {
        log.info("Finding archived documents and folders older than 7 days...");
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        List<GxDocument> archivedDocuments = documentRepository.findAllByIsArchivedTrueAndUpdatedAtBefore(sevenDaysAgo);
        log.info("Found {} archived documents to be deleted.", archivedDocuments.size());
        // Deletion logic will be added here later.

        List<GxFolder> archivedFolders = folderRepository.findAllByIsArchivedTrueAndUpdatedAtBefore(sevenDaysAgo);
        log.info("Found {} archived folders to be deleted.", archivedFolders.size());
        // Deletion logic will be added here later.

        archivedDocuments.forEach(d -> {
            explorerService.deleteDocument(List.of(d));
            auditLogDataService.log("Scheduler-Service", "", "DELETED", " (7 days old) DELETED" + " : " + d.getName(),
                    "GxDocument",
                    d.getOid());
        });

        archivedFolders.forEach(f -> {
            explorerService.deleteFolder(List.of(f));
            auditLogDataService.log("Scheduler-Service", "", "DELETED", " (7 days old) DELETED" + " : " + f.getName(),
                    "GxFolder",
                    f.getOid());
        });

        log.info("Deleted archived documents and folders older than 7 days - Done");

    }

    @Scheduled(cron = "0 0 2 * * ?") // Runs every day at 2 AM
    @Transactional
    public void handleExpiredDocuments() {
        log.info("Finding expired documents...");
        Date now = new Date();
        List<GxDocument> expiredDocuments = documentRepository.findAllByExpiryDateBefore(now);
        log.info("Found {} expired documents.", expiredDocuments.size());
        // Logic to handle expired documents will be added here.

        expiredDocuments.forEach(d -> {
            explorerService.deleteDocument(List.of(d));
            auditLogDataService.log("Scheduler-Service", "", "DELETED", " (Expired) DELETED" + " : " + d.getName(),
                    "GxDocument",
                    d.getOid());
        });

        log.info("Deleted expired documents - Done");

    }

}