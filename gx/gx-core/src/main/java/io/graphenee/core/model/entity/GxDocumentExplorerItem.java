package io.graphenee.core.model.entity;

import java.util.List;

public interface GxDocumentExplorerItem {

	Boolean isFile();

	String getMimeType();

	String getExtension();

	String getName();

	void setName(String name);

	String getNote();

	void setNote(String note);

	Integer getVersion();

	Long getSize();

	Boolean hasChildren();

	Integer getChildCount();

	List<GxDocumentExplorerItem> getChildren();

	GxDocumentExplorerItem getParent();

}
