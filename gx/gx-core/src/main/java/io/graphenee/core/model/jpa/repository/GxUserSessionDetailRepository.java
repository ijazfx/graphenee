package io.graphenee.core.model.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.core.model.entity.GxUserSessionDetail;
import io.graphenee.core.model.jpa.GxJpaRepository;


@Repository
public interface GxUserSessionDetailRepository extends GxJpaRepository<GxUserSessionDetail, Integer> {
    
    @Transactional
    @Modifying
    @Query(value = "update user_session_detail set is_signed_in = false, last_sync = NOW() where oid_user = ?1 and is_signed_in = true", nativeQuery = true)
    void updateExistingSignedInCheck(Integer id);

    List<GxUserSessionDetail> findByUserAndIsSignedInTrue(GxUserAccount user);

}
