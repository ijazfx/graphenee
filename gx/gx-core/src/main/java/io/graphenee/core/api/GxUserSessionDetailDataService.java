package io.graphenee.core.api;

import java.util.Collection;
import java.util.List;

import io.graphenee.core.model.entity.GxUserSessionDetail;

public interface GxUserSessionDetailDataService {

    List<GxUserSessionDetail> fetchAll();

    void save(GxUserSessionDetail gxUserSessionDetail, Integer oidNamespace, Integer userId);

    void saveNewSessionForUser(Integer oidNamespace, Integer userId);

    void delete(Collection<GxUserSessionDetail> gxUserSessionDetails);

}
