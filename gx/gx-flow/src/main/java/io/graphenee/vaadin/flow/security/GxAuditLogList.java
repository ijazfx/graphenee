package io.graphenee.vaadin.flow.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import com.vaadin.flow.data.provider.QuerySortOrder;

import io.graphenee.core.model.api.GxAuditLogDataService;
import io.graphenee.core.model.entity.GxAuditLog;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxAbstractEntityLazyList;

@Component
@Scope("prototype")
public class GxAuditLogList extends GxAbstractEntityLazyList<GxAuditLog> {

	@Autowired
	GxAuditLogDataService dataService;

	public GxAuditLogList() {
		super(GxAuditLog.class);
	}

	private static final long serialVersionUID = 1L;

	@Override
	protected int getTotalCount(GxAuditLog searchEntity) {
		return dataService.count(searchEntity);
	}

	@Override
	protected Stream<GxAuditLog> getData(int pageNumber, int pageSize, GxAuditLog searchEntity, List<QuerySortOrder> sortOrders) {
		List<Order> orders = sortOrdersToSpringOrders(sortOrders);
		return dataService.fetch(pageNumber, pageSize, searchEntity, orders).stream();
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "timestamp", "username", "remoteAddress", "auditEvent", "detail", "auditEntity", "oidAuditEntity" };
	}

	@Override
	protected GxAbstractEntityForm<GxAuditLog> getEntityForm(GxAuditLog entity) {
		return null;
	}

	@Override
	protected void onSave(GxAuditLog entity) {
	}

	@Override
	protected void onDelete(Collection<GxAuditLog> entities) {
	}

}
