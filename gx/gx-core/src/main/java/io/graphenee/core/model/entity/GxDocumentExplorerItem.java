package io.graphenee.core.model.entity;

import java.util.Date;
import java.util.List;

import io.graphenee.util.TRCalendarUtil;

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

	Integer getSortOrder();

	Date getIssueDate();

	Date getExpiryDate();

	Integer getExpiryReminderInDays();

	default Date getReminderDate() {
		if(getExpiryDate() != null && getExpiryReminderInDays() != null) {
			return TRCalendarUtil.minusDaysToDate(getExpiryDate(), getExpiryReminderInDays());
		}
		return null;
	}

	void setIssueDate(Date issueDate);

	void setExpiryDate(Date expiryDate);

	void setExpiryReminderInDays(Integer expiryReminderInDays);

}
