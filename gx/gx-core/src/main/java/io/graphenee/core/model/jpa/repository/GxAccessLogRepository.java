package io.graphenee.core.model.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.graphenee.core.model.entity.GxAccessLog;

public interface GxAccessLogRepository extends JpaRepository<GxAccessLog, Integer> {

}
