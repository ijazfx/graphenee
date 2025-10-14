package io.graphenee.core.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
@Table(name = "gx_document")
public class GxDocument extends GxMappedSuperclass implements GxDocumentExplorerItem {

	String name;

	String note;
	String mimeType;
	Long size;

	UUID documentId = UUID.randomUUID();

	Integer versionNo = 0;
	String path;

	Integer sortOrder = 0;
	Integer expiryReminderInDays = 30;
	LocalDate issueDate;
	LocalDate expiryDate;

	@Convert(converter = GxStringToJsonConverter.class)
	JSONObject tags;

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
	@JoinTable(name = "gx_file_tag_document_join", joinColumns = @JoinColumn(name = "oid_document", referencedColumnName = "oid"), inverseJoinColumns = @JoinColumn(name = "oid_tag", referencedColumnName = "oid"))
	Set<GxFileTag> fileTags = new HashSet<>();

	public void audit(GxUserAccount user, String event) {
		GxAuditLog log = new GxAuditLog();
		log.setAuditDate(LocalDateTime.now());
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

	public LocalDate getReminderDate() {
		if (expiryDate != null) {
			return expiryDate.minusDays(getExpiryReminderInDays());
		}
		return null;
	}

	public String getFileTagsJoined() {
		return fileTags.stream().map(t -> t.getTag()).collect(Collectors.joining(", "));
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

}
