package io.graphenee.vaadin.flow.security;

import java.util.Collection;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.graphenee.core.GxAuditLogDataService;
import io.graphenee.core.GxDataService;
import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxSecurityGroup;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.GxAbstractEntityList;

@Component
@Scope("prototype")
public class GxSecurityGroupList extends GxAbstractEntityList<GxSecurityGroup> {
	private static final long serialVersionUID = 1L;

	@Autowired
	GxAuditLogDataService auditService;

	@Autowired
	GxDataService dataService;

	@Autowired
	GxSecurityGroupForm entityForm;

	private GxNamespace namespace;

	public GxSecurityGroupList() {
		super(GxSecurityGroup.class);
	}

	@Override
	protected Stream<GxSecurityGroup> getData() {
		if (namespace == null)
			return dataService.findSecurityGroup().stream();
		return dataService.findSecurityGroupByNamespace(namespace).stream();
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "securityGroupName", "priority", "isActive" };
	}

	@Override
	protected GxAbstractEntityForm<GxSecurityGroup> getEntityForm(GxSecurityGroup entity) {
		return entityForm;
	}

	@Override
	protected void onSave(GxSecurityGroup entity) {
		dataService.save(entity);
	}

	@Override
	protected void onDelete(Collection<GxSecurityGroup> entities) {
		for (GxSecurityGroup entity : entities) {
			dataService.delete(entity);
		}
	}

	@Override
	protected void preEdit(GxSecurityGroup entity) {
		if (entity.getOid() == null) {
			entity.setNamespace(namespace);
		}
	}

	public void initializeWithNamespace(GxNamespace namespace) {
		this.namespace = namespace != null ? namespace : dataService.systemNamespace();
		refresh();
	}

	@Override
	protected boolean isAuditLogEnabled() {
		return true;
	}

	@Override
	protected void auditLog(GxAuthenticatedUser user, String remoteAddress, String auditEvent, String auditEntity, Collection<GxSecurityGroup> entities) {
		entities.forEach(e -> {
			auditService.log(user, remoteAddress, auditEvent, e.getSecurityGroupName(), auditEntity, e.getOid());
		});
	}

}
