package io.graphenee.core.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import io.graphenee.core.GxAuthenticationService;
import io.graphenee.core.model.entity.GxAuthentication;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.jpa.repository.GxAuthenticationRepository;

@DependsOn({ "flyway", "flywayInitializer" })
@Service
public class GxAuthenticationServiceImpl implements GxAuthenticationService {

    @Autowired
    private GxAuthenticationRepository repo;

    @Override
    public GxAuthentication save(GxAuthentication entity) {
        return repo.save(entity);
    }

    @Override
    public void delete(Collection<GxAuthentication> entities) {
        repo.deleteAllInBatch(entities);
    }

    @Override
    public GxAuthentication findOne(Integer oid) {
        return repo.findOne(oid);
    }

    @Override
    public List<GxAuthentication> findByNamespace(GxNamespace namespace) {
        return repo.findByNamespaceOrderByName(namespace);
    }

}
