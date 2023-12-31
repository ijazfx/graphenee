package io.graphenee.core.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;

import io.graphenee.core.model.GxMappedSuperclass;
import io.graphenee.core.model.jpa.converter.GxStringToJsonConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "gx_folder")
public class GxFolder extends GxMappedSuperclass implements Serializable, GxDocumentExplorerItem {
	private static final long serialVersionUID = 1L;

	String name;

	String note;
	UUID folderId = UUID.randomUUID();

	@Convert(converter = GxStringToJsonConverter.class)
	JSONObject tags;

	Integer sortOrder = 0;

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

	public void audit(GxUserAccount user, String event) {
		GxAuditLog log = new GxAuditLog();
		log.setAuditDate(new Timestamp(System.currentTimeMillis()));
		log.setAuditEvent(event);
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
	public Timestamp getExpiryDate() {
		return null;
	}

	@Override
	public Integer getExpiryReminderInDays() {
		return null;
	}

	@Override
	public Timestamp getReminderDate() {
		return null;
	}

	@Override
	public void setIssueDate(Timestamp issueDate) {
	}

	@Override
	public void setExpiryDate(Timestamp expiryDate) {
	}

	@Override
	public void setExpiryReminderInDays(Integer expiryReminderInDays) {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
