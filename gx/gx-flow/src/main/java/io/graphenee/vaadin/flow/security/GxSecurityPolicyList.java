package io.graphenee.vaadin.flow.security;

import java.util.Collection;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxSecurityPolicyBean;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxAbstractEntityList;

@Component
@Scope("prototype")
public class GxSecurityPolicyList extends GxAbstractEntityList<GxSecurityPolicyBean> {
    private static final long serialVersionUID = 1L;

    @Autowired
    GxDataService dataService;

    @Autowired
    GxSecurityPolicyForm securityPolicyForm;

    public GxSecurityPolicyList() {
        super(GxSecurityPolicyBean.class);
    }

    @Override
    protected int getTotalCount() {
        return dataService.findSecurityPolicy().size();
    }

    @Override
    protected Stream<GxSecurityPolicyBean> getData(int pageNumber, int pageSize) {
        return dataService.findSecurityPolicy().stream();
    }

    @Override
    protected String[] visibleProperties() {
        return new String[] { "securityPolicyName", "priority", "isActive" };
    }

    @Override
    protected GxAbstractEntityForm<GxSecurityPolicyBean> getEntityForm(GxSecurityPolicyBean entity) {
        return securityPolicyForm;
    }

    @Override
    protected void onSave(GxSecurityPolicyBean entity) {
        dataService.save(entity);
    }

    @Override
    protected void onDelete(Collection<GxSecurityPolicyBean> entities) {
        for (GxSecurityPolicyBean entity : entities) {
            dataService.delete(entity);
        }
    }

    protected void preEdit(GxSecurityPolicyBean entity) {
        if (entity.getOid() == null) {
            GxNamespaceBean namespaceBean = dataService.findSystemNamespace();
            entity.setNamespaceFault(new BeanFault<>(namespaceBean.getOid(), namespaceBean));
        }
    }

    @Override
    protected boolean shouldShowFormInDialog() {
        return true;
    }
    
}
