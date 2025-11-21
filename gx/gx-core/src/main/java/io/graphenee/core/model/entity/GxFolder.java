package io.graphenee.core.model.entity;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.annotation.LastModifiedDate;

import com.google.common.base.Strings;

import io.graphenee.common.GxAuthenticatedUser;
import io.graphenee.core.model.GxMappedSuperclass;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
@Table(name = "gx_folder")
public class GxFolder extends GxMappedSuperclass implements GxDocumentExplorerItem {
	private static final long serialVersionUID = 1L;

	String name;

	String note;
	UUID folderId = UUID.randomUUID();
	Boolean isArchived = false;

	Integer sortOrder = 0;

	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@ManyToOne
	@JoinColumn(name = "oid_folder")
	GxFolder folder;

	@ManyToOne
	@JoinColumn(name = "oid_namespace")
	GxNamespace namespace;

	@OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
	List<GxFolder> folders = new ArrayList<>();

	@OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
	List<GxDocument> documents = new ArrayList<>();

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "gx_folder_audit_log_join", joinColumns = @JoinColumn(name = "oid_folder", referencedColumnName = "oid"), inverseJoinColumns = @JoinColumn(name = "oid_audit_log", referencedColumnName = "oid"))
	List<GxAuditLog> auditLogs = new ArrayList<>();

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "gx_folder_tag_join", joinColumns = @JoinColumn(name = "oid_folder", referencedColumnName = "oid"), inverseJoinColumns = @JoinColumn(name = "oid_tag", referencedColumnName = "oid"))
	Set<GxTag> tags = new HashSet<>();

	@JoinTable(name = "gx_security_group_folder_join", joinColumns = @JoinColumn(name = "oid_folder", referencedColumnName = "oid"), inverseJoinColumns = @JoinColumn(name = "oid_security_group", referencedColumnName = "oid"))
	@ManyToMany(cascade = CascadeType.ALL)
	Set<GxSecurityGroup> groups = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "gx_user_account_folder_join", joinColumns = @JoinColumn(name = "oid_folder", referencedColumnName = "oid"), inverseJoinColumns = @JoinColumn(name = "oid_user_account", referencedColumnName = "oid"))
	Set<GxUserAccount> users = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "oid_owner")
	GxUserAccount owner;

	public void audit(GxUserAccount user, String event) {
		GxAuditLog log = new GxAuditLog();
		log.setAuditDate(new Timestamp(System.currentTimeMillis()));
		log.setAuditEvent(event);
		log.setDetail(event + " : " + name);
		log.setAuditEntity(this.getClass().getSimpleName());
		log.setOidAuditEntity(this.getOid());
		log.setUserAccount(user);
		auditLogs.add(log);
	}

	@Override
	public Boolean isFile() {
		return false;
	}

	@Override
	public String getMimeType() {
		return null;
	}

	@Override
	public Long getSize() {
		return 0L;
	}

	@Override
	public List<GxDocumentExplorerItem> getChildren() {
		List<GxDocumentExplorerItem> items = new ArrayList<>(getFolders());
		items.addAll(getDocuments());
		return items;
	}

	@Override
	public Boolean hasChildren() {
		return !getFolders().isEmpty() || !getDocuments().isEmpty();
	}

	@Override
	public Integer getChildCount() {
		return hasChildren() ? getFolders().size() + getDocuments().size() : 0;
	}

	@Override
	public Integer getVersion() {
		return null;
	}

	@Override
	public String getExtension() {
		return null;
	}

	@Override
	public GxDocumentExplorerItem getParent() {
		return getFolder();
	}

	@Override
	public String getRelativePath() {
		if (getFolder() == null) {
			return getGenericName();
		}
		return getFolder().getRelativePath() + "/" + getGenericName();
	}

	public String getGenericName() {
		if (getName().matches("io.graphenee.system")) {
			return "home";
		}
		return getName();
	}

	public GxDocument addDocument(GxDocument document) {
		if (!documents.contains(document)) {
			documents.add(document);
			document.setFolder(this);
		}
		return document;
	}

	@Override
	public Timestamp getIssueDate() {
		return null;
	}

	@Override
	public Date getExpiryDate() {
		return null;
	}

	@Override
	public Integer getExpiryReminderInDays() {
		return null;
	}

	@Override
	public Date getReminderDate() {
		return null;
	}

	@Override
	public void setIssueDate(Date issueDate) {
	}

	@Override
	public void setExpiryDate(Date expiryDate) {
	}

	@Override
	public void setExpiryReminderInDays(Integer expiryReminderInDays) {
	}

	public String getTagsJoined() {
		return tags.stream().map(t -> t.getTag()).collect(Collectors.joining(", "));
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String getPath() {
		return null;
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

	@Override
	public String getUniqueId() {
		return folderId.toString();
	}

	@Override
	public String getOwnerName() {
		if (owner == null) {
			return null;
		}
		if (!Strings.isNullOrEmpty(owner.getName())) {
			return owner.getName();
		}
		return null;
	}

}
