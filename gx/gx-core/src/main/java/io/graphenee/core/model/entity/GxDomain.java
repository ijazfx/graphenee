package io.graphenee.core.model.entity;

import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;

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
@Table(name = "gx_domain")
public class GxDomain extends GxMappedSuperclass {

    public static final Pattern DNS_PATTERN = Pattern.compile("^([a-zA-Z0-9-]+\\.)*[a-zA-Z0-9-]+\\.[a-zA-Z]{2,}$");

    String dns;
    Boolean isActive = true;
    Boolean isVerified = false;
    String txtRecord = RandomStringUtils.secureStrong().nextAlphanumeric(32);

    @ManyToOne
    @JoinColumn(name = "oid_namespace")
    private GxNamespace namespace;

    @Override
    public String toString() {
        return dns;
    }

    public boolean isDnsValid() {
        return dns != null && DNS_PATTERN.matcher(dns).matches();
    }
    
    public static boolean isDnsValid(String dns) {
        return dns != null && DNS_PATTERN.matcher(dns).matches();
    }


}
