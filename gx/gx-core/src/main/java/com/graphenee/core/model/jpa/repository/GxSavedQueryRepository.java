package com.graphenee.core.model.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.graphenee.core.model.entity.GxSavedQuery;

public interface GxSavedQueryRepository extends JpaRepository<GxSavedQuery, Integer> {

	GxSavedQuery findByQueryNameAndTargetUser(String queryName, String targetUser);

}
