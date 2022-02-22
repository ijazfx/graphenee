package io.graphenee.documents;

import java.util.List;

import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.core.model.entity.GxDocumentExplorerItem;
import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.util.storage.FileStorage;

public interface GxDocumentExplorerService {

	Long countFolder(GxNamespace namespace);

	Long countDocuments(GxNamespace namespace);

	List<GxFolder> findFolder(GxNamespace namespace, String... sortKey);

	List<GxFolder> findFolder(GxFolder parent, String... sortKey);

	List<GxDocument> findDocument(GxNamespace namespace, String... sortKey);

	List<GxDocument> findDocument(GxFolder parent, String... sortKey);

	List<GxDocument> findDocumentVersion(GxDocument document, String... sortKey);

	List<GxDocumentExplorerItem> saveExplorerItem(List<GxDocumentExplorerItem> item);

	List<GxFolder> saveFolder(List<GxFolder> folders);

	List<GxDocument> saveDocument(List<GxDocument> documents);

	void deleteExplorerItem(List<GxDocumentExplorerItem> items);

	void deleteFolder(List<GxFolder> folders);

	void deleteDocument(List<GxDocument> documents);

	GxDocument createVersion(GxDocument document, FileStorage storage);

	Long countChildren(GxNamespace namespace);

	Long countChildren(GxDocumentExplorerItem parent);

	List<GxDocumentExplorerItem> findExplorerItem(GxNamespace namespace, String... sortKey);

	List<GxDocumentExplorerItem> findExplorerItem(GxDocumentExplorerItem parent, String... sortKey);

}
