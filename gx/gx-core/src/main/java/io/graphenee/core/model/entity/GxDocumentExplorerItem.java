package io.graphenee.core.model.entity;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import io.graphenee.common.GxAuthenticatedUser;
import io.graphenee.util.TRCalendarUtil;

public interface GxDocumentExplorerItem {

	Boolean isFile();

	String getMimeType();

	Integer getOid();

	String getExtension();

	String getName();

	String getPath();

	String getRelativePath();

	Set<GxTag> getTags();

	String getTagsJoined();

	void setTags(Set<GxTag> tags);

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
		if (getExpiryDate() != null && getExpiryReminderInDays() != null) {
			return TRCalendarUtil.minusDaysToDate(getExpiryDate(), getExpiryReminderInDays());
		}
		return null;
	}

	void setIssueDate(Date issueDate);

	void setExpiryDate(Date expiryDate);

	void setExpiryReminderInDays(Integer expiryReminderInDays);

	GxNamespace getNamespace();

	void setNamespace(GxNamespace namespace);

	Set<Principal> getGrants();

	void setGrants(Set<Principal> grants);

	boolean isGranted(GxAuthenticatedUser user);

}
