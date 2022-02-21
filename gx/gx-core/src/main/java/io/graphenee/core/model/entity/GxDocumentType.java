package io.graphenee.core.model.entity;

import java.io.Serializable;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.json.JSONObject;

import io.graphenee.core.model.jpa.converter.GxStringToJsonConverter;
import lombok.Data;

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

}
