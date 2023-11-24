package io.graphenee.core.model.entity;

import java.io.Serializable;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import org.json.JSONObject;

import io.graphenee.core.model.jpa.converter.GxStringToJsonConverter;
import lombok.Data;
import lombok.EqualsAndHashCode.Include;

@Data
@Entity
public class GxDocumentType implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer oid;

	String name;
	String note;

	@Convert(converter = GxStringToJsonConverter.class)
	JSONObject tags;

	@Include
	@ManyToOne
	@JoinColumn(name = "oid_namespace")
	GxNamespace namespace;

}
