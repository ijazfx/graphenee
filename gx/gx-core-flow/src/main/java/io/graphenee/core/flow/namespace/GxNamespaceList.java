package io.graphenee.core.flow.namespace;

import java.util.Collection;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.jpa.repository.GxNamespaceRepository;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.GxAbstractEntityList;

@SuppressWarnings("serial")
@Component
@Scope("prototype")
public class GxNamespaceList extends GxAbstractEntityList<GxNamespace> {

	@Autowired
	GxNamespaceRepository repo;

	@Autowired
	GxNamespaceForm form;

	public GxNamespaceList() {
		super(GxNamespace.class);
	}

	@Override
	protected Stream<GxNamespace> getData() {
		return repo.findAll(Sort.by("namespace")).stream();
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "namespace", "namespaceDescription", "isActive", "isProtected" };
	}

	@Override
	protected GxAbstractEntityForm<GxNamespace> getEntityForm(GxNamespace entity) {
		return form;
	}

	@Override
	protected void onSave(GxNamespace entity) {
		repo.save(entity);
	}

	@Override
	protected void onDelete(Collection<GxNamespace> entities) {
		if (entities.stream().filter(e -> e.getIsProtected()).count() == 0)
			repo.deleteAllInBatch(entities);
	}

	@Override
	protected boolean shouldShowExportDataMenu() {
		return false;
	}

}
