package io.graphenee.documents;

import java.util.List;

import io.graphenee.common.GxAuthenticatedUser;
import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.core.model.entity.GxDocumentExplorerItem;
import io.graphenee.core.model.entity.GxDocumentFilter;
import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxUserAccount;

public interface GxDocumentExplorerService {

	GxFolder findOrCreateNamespaceFolder(GxNamespace namespace);

	GxFolder findOrCreateFolder(GxNamespace namespace, String folderName);

	GxFolder findOrCreateFolder(GxFolder parentFolder, String folderName);

	List<GxFolder> findFolder(GxFolder parent, String... sortKey);

	List<GxDocument> findDocument(GxFolder parent, String... sortKey);

	GxDocument alreadyExistsDocument(GxFolder parent, GxUserAccount owner, String documentName);

	List<GxDocument> findDocumentVersion(GxDocument document, String... sortKey);

	List<GxDocumentExplorerItem> saveExplorerItem(GxDocumentExplorerItem parent, List<GxDocumentExplorerItem> item);

	List<GxFolder> saveFolder(GxFolder parent, List<GxFolder> folders);

	List<GxDocument> saveDocument(GxFolder parent, List<GxDocument> documents);

	GxDocument createDocumentVersion(GxDocument parent, GxDocument newDocument);

	void deleteExplorerItem(List<GxDocumentExplorerItem> items);

	void deleteExplorerItem(List<GxDocumentExplorerItem> items, GxUserAccount user);

	void archiveExplorerItem(List<GxDocumentExplorerItem> items, GxUserAccount user);

	void restoreExplorerItem(List<GxDocumentExplorerItem> items, GxUserAccount user);

	void archiveDocumentVersion(List<GxDocument> document, GxUserAccount user);

	void deleteFolder(List<GxFolder> folders);

	void deleteDocument(List<GxDocument> documents);

	Long countChildren(GxAuthenticatedUser user, GxDocumentExplorerItem parent, GxDocumentExplorerItem searchEntity,
			GxDocumentFilter filter);

	List<GxDocumentExplorerItem> findExplorerItem(GxAuthenticatedUser user, GxDocumentExplorerItem parent,
			GxDocumentExplorerItem searchEntity, GxDocumentFilter filter, String... sortKey);

	List<GxDocumentExplorerItem> findAll(GxDocumentExplorerItem item, GxFolder rootFolder, GxUserAccount user);

	List<GxDocumentExplorerItem> findFolderItems(GxAuthenticatedUser user, GxFolder parent);

	List<GxDocumentExplorerItem> findAllArchivedItems(GxAuthenticatedUser user, GxDocumentExplorerItem item,
			GxFolder topFolder, GxNamespace namespace);

	int countAllArchivedItems(GxAuthenticatedUser user, GxDocumentExplorerItem item, GxFolder topFolder,
			GxNamespace namespace);

	int countAll(GxDocumentExplorerItem item, GxFolder rootFolder, GxUserAccount user);

	void positionBefore(List<GxDocumentExplorerItem> items, GxDocumentExplorerItem targetItem);

	void positionAfter(List<GxDocumentExplorerItem> items, GxDocumentExplorerItem targetItem);

	void changeParent(List<GxDocumentExplorerItem> items, GxDocumentExplorerItem parent);

	void saveDocument(GxDocument document);

	void saveFolder(GxFolder folder);

}
