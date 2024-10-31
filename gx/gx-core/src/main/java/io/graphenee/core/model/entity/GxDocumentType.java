package io.graphenee.core.model.entity;

import java.io.Serializable;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.json.JSONObject;

import io.graphenee.core.model.GxMappedSuperclass;
import io.graphenee.core.model.jpa.converter.GxStringToJsonConverter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "gx_document_type")
public class GxDocumentType extends GxMappedSuperclass implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer oid;

    String name;
    String note;

    @Convert(converter = GxStringToJsonConverter.class)
    JSONObject tags;

    @ManyToOne
    @JoinColumn(name = "oid_namespace")
    GxNamespace namespace;

}
