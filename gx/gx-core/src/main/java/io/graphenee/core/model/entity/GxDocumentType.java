package io.graphenee.core.model.entity;

import org.json.JSONObject;

import io.graphenee.core.model.GxMappedSuperclass;
import io.graphenee.core.model.jpa.converter.GxStringToJsonConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "gx_document_type")
public class GxDocumentType extends GxMappedSuperclass {

	String name;
	String note;

	@Convert(converter = GxStringToJsonConverter.class)
	JSONObject tags;

	@ManyToOne
	@JoinColumn(name = "oid_namespace")
	GxNamespace namespace;

}
