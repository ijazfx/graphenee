package io.graphenee.core.model.api;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import io.graphenee.core.model.entity.GxEmailTemplate;

public interface GxEmailTemplateDataService {

	GxEmailTemplate save(GxEmailTemplate entity);

	void delete(Collection<GxEmailTemplate> entities);

	Integer countAll(GxEmailTemplate searchEntity);

	List<GxEmailTemplate> findAll(GxEmailTemplate searchEntity, Pageable pageable);

	List<GxEmailTemplate> findAll(Specification<GxEmailTemplate> specification);

	GxEmailTemplate findOne(Integer oid);

}
