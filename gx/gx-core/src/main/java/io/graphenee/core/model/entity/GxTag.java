package io.graphenee.core.model.entity;

import io.graphenee.core.model.GxMappedSuperclass;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "gx_tag")
public class GxTag extends GxMappedSuperclass {

    String tag;

    @ManyToOne
    @JoinColumn(name = "oid_namespace")
    private GxNamespace namespace;

    @Override
    public String toString() {
        return tag;
    }
}
