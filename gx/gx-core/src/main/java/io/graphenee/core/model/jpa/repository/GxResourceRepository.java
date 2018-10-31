package io.graphenee.core.model.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.graphenee.core.model.entity.GxResource;

public interface GxResourceRepository extends JpaRepository<GxResource, Integer> {
	GxResource findByName(String name);
}
