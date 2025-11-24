package io.graphenee.core.model.entity;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import io.graphenee.common.GxAuthenticatedUser;
import io.graphenee.util.TRCalendarUtil;

public interface GxDocumentExplorerItem {

	Boolean isFile();
	
	Boolean getIsArchived();

	Boolean getIsReadOnly();
	
	void setIsReadOnly(Boolean isReadOnly);
	
	void setIsArchived(Boolean isArchived);

	String getMimeType();

	Integer getOid();

	String getExtension();

	String getName();

	String getPath();

	String getUniqueId();

	String getRelativePath();

	Set<GxTag> getTags();

	String getTagsJoined();

	String getOwnerName();

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

	LocalDateTime getUpdatedAt();

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

	GxUserAccount getOwner();

}
