package io.graphenee.core;

import java.util.Collection;
import java.util.List;

import io.graphenee.core.model.entity.GxEntityCallback;
import io.graphenee.core.model.entity.GxNamespace;

public interface GxEntityCallbackService {

    GxEntityCallback save(GxEntityCallback entity);
    void delete(Collection<GxEntityCallback> entities);
    GxEntityCallback findOne(Integer oid);
    List<GxEntityCallback> findByNamespace(GxNamespace namespace);

}
