package io.graphenee.core.model.entity;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import io.graphenee.common.GxAuthenticatedUser;

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

	LocalDate getIssueDate();

	LocalDate getExpiryDate();

	Integer getExpiryReminderInDays();

	default LocalDate getReminderDate() {
		if(getExpiryDate() != null && getExpiryReminderInDays() != null) {
			return getExpiryDate().minusDays(getExpiryReminderInDays());
		}
		return null;
	}

	void setIssueDate(LocalDate issueDate);

	void setExpiryDate(LocalDate expiryDate);

	void setExpiryReminderInDays(Integer expiryReminderInDays);

	GxNamespace getNamespace();

	void setNamespace(GxNamespace namespace);

	Set<Principal> getGrants();

	void setGrants(Set<Principal> grants);

	boolean isGranted(GxAuthenticatedUser user);

	GxUserAccount getOwner();

}
