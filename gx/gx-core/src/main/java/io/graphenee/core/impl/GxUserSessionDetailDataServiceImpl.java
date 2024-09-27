package io.graphenee.core.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.graphenee.core.api.GxUserSessionDetailDataService;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.core.model.entity.GxUserSessionDetail;
import io.graphenee.core.model.jpa.repository.GxNamespaceRepository;
import io.graphenee.core.model.jpa.repository.GxUserAccountRepository;
import io.graphenee.core.model.jpa.repository.GxUserSessionDetailRepository;
import io.graphenee.util.TRCalendarUtil;

@Service
public class GxUserSessionDetailDataServiceImpl implements GxUserSessionDetailDataService {
    
    @Autowired
    GxUserSessionDetailRepository repository;

    @Autowired
    GxNamespaceRepository namespaceRepository;

    @Autowired
    GxUserAccountRepository userAccountRepository;

    @Override
    public List<GxUserSessionDetail> fetchAll() {
        return repository.findAll();
    }

    @Override
    public void save(GxUserSessionDetail gxUserSessionDetail, Integer oidNamespace, Integer userId) {
    }
    
    @Override
    public void delete(Collection<GxUserSessionDetail> gxUserSessionDetails) {
        repository.delete(gxUserSessionDetails);
    }
    
    @Override
    public void saveNewSessionForUser(Integer oidNamespace, Integer userId) {
        Optional<GxNamespace> namespace = namespaceRepository.findById(oidNamespace);
        Optional<GxUserAccount> user = userAccountRepository.findById(userId);
        if (namespace.isPresent() && user.isPresent()) {
            repository.updateExistingSignedInCheck(userId);

            // repository.findByUserAndIsSignedInTrue(user.get()).stream().map(usd -> {
            //     usd.setIsSignedIn(false);
            //     usd.setLastSync(TRCalendarUtil.getCurrentTimeStamp());
            //     return repository.save(usd);
            // });

            GxUserSessionDetail gxUserSessionDetail = new GxUserSessionDetail();
            gxUserSessionDetail.setNamespace(namespace.get());
            gxUserSessionDetail.setUser(user.get());
            gxUserSessionDetail.setIsSignedIn(true);
            gxUserSessionDetail.setSignedinAt(TRCalendarUtil.getCurrentTimeStamp());
            gxUserSessionDetail.setLastSync(TRCalendarUtil.getCurrentTimeStamp());
            repository.save(gxUserSessionDetail);
        }
    }

    public void closeAllExistingSessionsForUser(String username) {
        
    }

}
