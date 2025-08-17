package io.graphenee.core.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import io.graphenee.core.GxEntityCallbackService;
import io.graphenee.core.model.entity.GxEntityCallback;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.jpa.repository.GxEntityCallbackRepository;

@DependsOn({ "flyway", "flywayInitializer" })
@Service
public class GxEntityCallbackServiceImpl implements GxEntityCallbackService {

    @Autowired
    private GxEntityCallbackRepository repo;

    @Override
    public GxEntityCallback save(GxEntityCallback entity) {
        return repo.save(entity);
    }

    @Override
    public void delete(Collection<GxEntityCallback> entities) {
        repo.deleteAllInBatch(entities);
    }

    @Override
    public GxEntityCallback findOne(Integer oid) {
        return repo.findOne(oid);
    }

    @Override
    public List<GxEntityCallback> findByNamespace(GxNamespace namespace) {
        return repo.findByNamespaceOrderByName(namespace);
    }

}
