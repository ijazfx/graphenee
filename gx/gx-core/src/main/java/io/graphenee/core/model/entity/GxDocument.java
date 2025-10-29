package io.graphenee.core.model.entity;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.graphenee.common.GxAuthenticatedUser;
import io.graphenee.core.model.GxMappedSuperclass;
import io.graphenee.util.TRCalendarUtil;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "gx_document")
public class GxDocument extends GxMappedSuperclass implements GxDocumentExplorerItem {
	private static final long serialVersionUID = 1L;

	String name;

	String note;
	String mimeType;
	Long size;

	UUID documentId = UUID.randomUUID();

	Integer versionNo = 0;
	String path;

	Integer sortOrder = 0;
	Integer expiryReminderInDays = 30;
	Date issueDate;
	Date expiryDate;

	@ManyToOne
	@JoinColumn(name = "oid_document")
	GxDocument document;

	@ManyToOne
	@JoinColumn(name = "oid_folder")
	GxFolder folder;

	@ManyToOne
	@JoinColumn(name = "oid_namespace")
	GxNamespace namespace;

	@OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
	List<GxDocument> versions = new ArrayList<>();

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "gx_document_audit_log_join", joinColumns = @JoinColumn(name = "oid_document", referencedColumnName = "oid"), inverseJoinColumns = @JoinColumn(name = "oid_audit_log", referencedColumnName = "oid"))
	List<GxAuditLog> auditLogs = new ArrayList<>();

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "gx_document_tag_join", joinColumns = @JoinColumn(name = "oid_document", referencedColumnName = "oid"), inverseJoinColumns = @JoinColumn(name = "oid_tag", referencedColumnName = "oid"))
	Set<GxTag> tags = new HashSet<>();

	@JoinTable(name = "gx_security_group_document_join", joinColumns = @JoinColumn(name = "oid_document", referencedColumnName = "oid"), inverseJoinColumns = @JoinColumn(name = "oid_security_group", referencedColumnName = "oid"))
	@ManyToMany(cascade = CascadeType.ALL)
	Set<GxSecurityGroup> groups = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "gx_user_account_document_join", joinColumns = @JoinColumn(name = "oid_document", referencedColumnName = "oid"), inverseJoinColumns = @JoinColumn(name = "oid_user_account", referencedColumnName = "oid"))
	Set<GxUserAccount> users = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "oid_owner")
	GxUserAccount owner;

	public void audit(GxUserAccount user, String event) {
		GxAuditLog log = new GxAuditLog();
		log.setAuditDate(new Timestamp(System.currentTimeMillis()));
		log.setAuditEvent(event);
		log.setUserAccount(user);
		auditLogs.add(log);
	}

	@Override
	public Boolean isFile() {
		return true;
	}

	@Override
	public List<GxDocumentExplorerItem> getChildren() {
		return new ArrayList<>(getVersions());
	}

	@Override
	public Boolean hasChildren() {
		return !getVersions().isEmpty();
	}

	@Override
	public Integer getChildCount() {
		return hasChildren() ? getVersions().size() : 0;
	}

	@Override
	public Integer getVersion() {
		return versionNo;
	}

	@Override
	public String getExtension() {
		if (name != null) {
			String[] parts = name.trim().split("\\.");
			if (parts.length > 0) {
				return parts[parts.length - 1].toLowerCase();
			}
		}
		return null;
	}

	@Override
	public GxDocumentExplorerItem getParent() {
		GxFolder f = getFolder();
		if (f == null) {
			return getDocument().getFolder();
		}
		return f;
	}

	public Integer getExpiryReminderInDays() {
		return getParent() != null && getParent().getExpiryReminderInDays() != null
				? getParent().getExpiryReminderInDays()
				: expiryReminderInDays;
	}

	public Timestamp getReminderDate() {
		if (expiryDate != null) {
			return new Timestamp(TRCalendarUtil.minusDaysToDate(expiryDate, getExpiryReminderInDays()).getTime());
		}
		return null;
	}

	public String getTagsJoined() {
		return tags.stream().map(t -> t.getTag()).collect(Collectors.joining(", "));
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String getRelativePath() {
		GxFolder f = getFolder();
		if (getDocument() != null) {
			f = getDocument().getFolder();
		}
		if (f == null) {
			return getName();
		}
		return f.getRelativePath() + "/" + getName();
	}

	public void setName(String name) {
		this.name = name;
	}

	@Transient
	private Set<Principal> grants;

	public Set<Principal> getGrants() {
		if (grants == null) {
			grants = Stream.concat(groups.stream(), users.stream()).collect(Collectors.toSet());
		}
		return grants;
	}

	public boolean isGranted(GxAuthenticatedUser user) {
		if (user.canDoAction("all", "all"))
			return true;
		if (owner != null && owner.equals(user))
			return true;
		if (getGrants().contains(user))
			return true;
		if (!getGrants().isEmpty()) {
			return getGrants().stream().filter(i -> {
				if (i instanceof GxSecurityGroup) {
					return ((GxSecurityGroup) i).isMember(user);
				}
				return false;
			}).count() > 0;
		}
		return getFolder() != null && getFolder().isGranted(user);
	}

}
