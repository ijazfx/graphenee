package io.graphenee.vaadin.flow.security;

import java.util.Collection;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.GxDataService;
import io.graphenee.core.model.entity.GxAccessKey;
import io.graphenee.core.model.entity.GxSecurityGroup;
import io.graphenee.core.model.entity.GxSecurityPolicy;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.GxAbstractEntityList;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxAccessKeyList extends GxAbstractEntityList<GxAccessKey> {

	@Autowired
	GxDataService dataService;

	@Autowired
	GxAccessKeyForm form;

	private GxUserAccount user;

	private GxSecurityGroup group;

	private GxSecurityPolicy policy;

	public GxAccessKeyList() {
		super(GxAccessKey.class);
	}

	@Override
	protected Stream<GxAccessKey> getData() {
		if (user != null)
			return dataService.findAccessKeyByUserAccount(user).stream();
		if (group != null)
			return dataService.findAccessKeyBySecurityGroup(group).stream();
		if (policy != null)
			return dataService.findAccessKeyBySecurityPolicy(policy).stream();

		return Stream.empty();
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "accessKey", "isActive" };
	}

	@Override
	protected GxAbstractEntityForm<GxAccessKey> getEntityForm(GxAccessKey entity) {
		return form;
	}

	@Override
	protected void preEdit(GxAccessKey entity) {
		entity.setUserAccount(user);
	}

	@Override
	protected void onSave(GxAccessKey entity) {
		dataService.save(entity);
	}

	@Override
	protected void onDelete(Collection<GxAccessKey> entities) {
		entities.forEach(entity -> {
			dataService.delete(entity);
		});
	}

	public void initializeWithUserAccount(GxUserAccount user) {
		this.user = user;
		refresh();
	}

	public void initializeWithSecurityGroup(GxSecurityGroup group) {
		this.group = group;
		refresh();
	}

	public void initializeWithSecurityPolicy(GxSecurityPolicy policy) {
		this.policy = policy;
		refresh();
	}

}
