package io.graphenee.core.model.entity;

import java.io.Serializable;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
