package io.graphenee.core.model.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.graphenee.core.model.api.GxEmailTemplateDataService;
import io.graphenee.core.model.entity.GxEmailTemplate;
import io.graphenee.core.model.jpa.repository.GxEmailTemplateRepository;
import io.graphenee.util.JpaSpecificationBuilder;

@Service
@Transactional
public class GxEmailTemplateDataServiceImpl implements GxEmailTemplateDataService {

	@Autowired
	private GxEmailTemplateRepository repository;

	@Override
	public GxEmailTemplate save(GxEmailTemplate entity) {
		return repository.save(entity);
	}

	@Override
	public void delete(Collection<GxEmailTemplate> entities) {
		repository.delete(entities);
	}

	@Override
	public Integer countAll(GxEmailTemplate searchEntity) {
		return (int) repository.count(makeEmailTemplateSpec(searchEntity));
	}

	@Override
	public List<GxEmailTemplate> findAll(GxEmailTemplate searchEntity, Pageable pageable) {
		return repository.findAll(makeEmailTemplateSpec(searchEntity), pageable).toList();
	}

	private Specification<GxEmailTemplate> makeEmailTemplateSpec(GxEmailTemplate searchEntity) {
		JpaSpecificationBuilder<GxEmailTemplate> sb = JpaSpecificationBuilder.get();
		if (searchEntity.getNamespace() != null) {
			sb.eq("namespace", searchEntity.getNamespace());
		}
		sb.eq("isActive", searchEntity.getIsActive());
		sb.like("templateCode", searchEntity.getTemplateCode());
		sb.like("templateName", searchEntity.getTemplateName());
		sb.like("subject", searchEntity.getSubject());
		return sb.build();
	}

	@Override
	public List<GxEmailTemplate> findAll(Specification<GxEmailTemplate> specification) {
		return repository.findAll(specification);
	}

	@Override
	public GxEmailTemplate findOne(Integer oid) {
		return repository.findOne(oid);
	}

}
