package io.graphenee.core.flow.email_template;

import java.util.Collection;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;

import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.GxEmailTemplateDataService;
import io.graphenee.core.model.entity.GxEmailTemplate;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.GxAbstractEntityLazyList;

@SpringComponent
@Scope("prototype")
public class GxEmailTemplateList extends GxAbstractEntityLazyList<GxEmailTemplate> {

	@Autowired
	private GxEmailTemplateDataService emailTemplateDataService;

	@Autowired
	private GxEmailTemplateForm form;

	public GxEmailTemplateList() {
		super(GxEmailTemplate.class);
	}

	@Override
	protected int getTotalCount(GxEmailTemplate searchEntity) {
		return emailTemplateDataService.countAll(searchEntity);
	}

	@Override
	protected Stream<GxEmailTemplate> getData(int pageNumber, int pageSize, GxEmailTemplate searchEntity) {
		PageRequest request = PageRequest.of(pageNumber, pageSize);
		return emailTemplateDataService.findAll(searchEntity, request).stream();
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "templateCode", "templateName", "subject" };
	}

	@Override
	protected GxAbstractEntityForm<GxEmailTemplate> getEntityForm(GxEmailTemplate entity) {
		return form;
	}

	@Override
	protected void decorateColumn(String propertyName, Column<GxEmailTemplate> column) {
		if (propertyName.equals("templateCode")) {
			column.setWidth("300px");
		}
		if (propertyName.equals("templateName")) {
			column.setWidth("300px");
		}
		if (propertyName.equals("subject")) {
			column.setWidth("400px");
		}
	}

	@Override
	protected void onSave(GxEmailTemplate entity) {
		emailTemplateDataService.save(entity);
	}

	@Override
	protected void onDelete(Collection<GxEmailTemplate> entities) {
		emailTemplateDataService.delete(entities);
	}

	@Override
	protected void preEdit(GxEmailTemplate entity) {
		if (entity.getOid() == null) {
			entity.setIsActive(true);
			entity.setNamespace(getSearchEntity().getNamespace());
		}
	}

	public void initializeWithNamespace(GxNamespace namespace) {
		getSearchEntity().setNamespace(namespace);
		refresh();
	}

}
