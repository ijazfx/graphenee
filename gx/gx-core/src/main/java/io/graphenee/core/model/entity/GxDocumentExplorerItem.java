package io.graphenee.core.model.entity;

import java.util.List;

public interface GxDocumentExplorerItem {

	Boolean isFile();

	String getMimeType();

	String getExtension();

	String getName();

	String getNote();

	Integer getVersion();

	Long getSize();

	Boolean hasChildren();
	
	Integer getChildCount();

	List<GxDocumentExplorerItem> getChildren();

	GxDocumentExplorerItem getParent();

}
