package io.graphenee.core.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode.Include;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = "user_session_detail")
public class GxUserSessionDetail implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    @Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer oid;
    
    private Boolean isSignedIn;
    private Timestamp signedinAt;
    private Timestamp lastSync;

    @ManyToOne
    @JoinColumn(name = "oid_user")
    private GxUserAccount user;

    @ManyToOne
    @JoinColumn(name = "oid_namespace")
    private GxNamespace namespace;

    public String getUserName() {
        return this.user != null ? this.user.getUsername() : "";
    }

    public String getNamespaceName() {
        return this.namespace != null ? this.namespace.getNamespace() : "";
    }
}
