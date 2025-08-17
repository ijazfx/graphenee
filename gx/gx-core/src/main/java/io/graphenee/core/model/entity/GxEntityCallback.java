package io.graphenee.core.model.entity;

import io.graphenee.core.enums.GxEntityEventEnum;
import io.graphenee.core.model.GxMappedSuperclass;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "gx_entity_callback")
public class GxEntityCallback extends GxMappedSuperclass {

    private String name;
    private String description;
    private String entityName;

    @Enumerated(EnumType.STRING)
    private GxEntityEventEnum eventType;

    private String attributeSet;
    private String callbackUrl;
    private String requestTemplate;

    @ManyToOne
    @JoinColumn(name = "oid_authentication")
    private GxAuthentication authentication;

    @ManyToOne
    @JoinColumn(name = "oid_namespace")
    private GxNamespace namespace;

}
