package io.graphenee.core;

import java.util.Collection;
import java.util.List;

import io.graphenee.core.model.entity.GxAuthentication;
import io.graphenee.core.model.entity.GxNamespace;

public interface GxAuthenticationService {

    GxAuthentication save(GxAuthentication entity);

    void delete(Collection<GxAuthentication> entities);

    GxAuthentication findOne(Integer oid);

    List<GxAuthentication> findByNamespace(GxNamespace namespace);

}
