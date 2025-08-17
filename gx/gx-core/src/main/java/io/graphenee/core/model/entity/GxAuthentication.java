package io.graphenee.core.model.entity;

import io.graphenee.core.enums.GxAuthTypeEnum;
import io.graphenee.core.enums.GxRequestTypeEnum;
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
@Table(name = "gx_authentication")
public class GxAuthentication extends GxMappedSuperclass {

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private GxAuthTypeEnum authType;

    private String username;
    private String password;
    private String bearerToken;
    private String authUrl;

    @Enumerated(EnumType.STRING)
    private GxRequestTypeEnum requestType;

    private String requestTemplate;

    @ManyToOne
    @JoinColumn(name = "oid_namespace")
    private GxNamespace namespace;

}
