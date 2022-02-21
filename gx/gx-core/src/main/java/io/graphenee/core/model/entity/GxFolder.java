package io.graphenee.core.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.json.JSONObject;

import io.graphenee.core.model.jpa.converter.GxStringToJsonConverter;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class GxFolder implements Serializable, GxDocumentExplorerItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer oid;

	UUID folderId = UUID.randomUUID();

	@Include
	String name;
	String note;

	@Convert(converter = GxStringToJsonConverter.class)
	JSONObject tags;

	@Include
	@ManyToOne
	@JoinColumn(name = "oid_folder")
	GxFolder folder;

	@Include
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
		log.setGxUserAccount(user);
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

}
