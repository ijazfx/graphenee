package io.graphenee.documents;

import java.util.List;

import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.core.model.entity.GxDocumentExplorerItem;
import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.core.model.entity.GxNamespace;

public interface GxDocumentExplorerService {

	GxFolder findOrCreateNamespaceFolder(GxNamespace namespace);

	List<GxFolder> findFolder(GxFolder parent, String... sortKey);

	List<GxDocument> findDocument(GxFolder parent, String... sortKey);

	List<GxDocument> findDocumentVersion(GxDocument document, String... sortKey);

	List<GxDocumentExplorerItem> saveExplorerItem(GxDocumentExplorerItem parent, List<GxDocumentExplorerItem> item);

	List<GxFolder> saveFolder(GxFolder parent, List<GxFolder> folders);

	List<GxDocument> saveDocument(GxFolder parent, List<GxDocument> documents);

	GxDocument createDocumentVersion(GxDocument parent, GxDocument newDocument);

	void deleteExplorerItem(List<GxDocumentExplorerItem> items);

	void deleteFolder(List<GxFolder> folders);

	void deleteDocument(List<GxDocument> documents);

	Long countChildren(GxDocumentExplorerItem parent);

	List<GxDocumentExplorerItem> findExplorerItem(GxDocumentExplorerItem parent, String... sortKey);

}
