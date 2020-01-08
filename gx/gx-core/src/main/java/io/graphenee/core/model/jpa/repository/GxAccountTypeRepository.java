package io.graphenee.core.model.jpa.repository;

import java.util.List;

import io.graphenee.core.model.entity.GxAccountType;
import io.graphenee.core.model.jpa.GxJpaRepository;

public interface GxAccountTypeRepository extends GxJpaRepository<GxAccountType, Integer> {

	List<GxAccountType> findAllByOrderByTypeName();
}
