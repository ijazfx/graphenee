package io.graphenee.vaadin.flow.security;

import java.util.Collection;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxSecurityGroupBean;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxAbstractEntityList;

@Component
@Scope("prototype")
public class GxSecurityGroupList extends GxAbstractEntityList<GxSecurityGroupBean> {
    private static final long serialVersionUID = 1L;


    @Autowired
    GxDataService dataService;

    @Autowired
    GxSecurityGroupForm entityForm;

    public GxSecurityGroupList() {
        super(GxSecurityGroupBean.class);
    }

    @Override
    protected int getTotalCount() {
        return dataService.findSecurityGroup().size();
    }

    @Override
    protected Stream<GxSecurityGroupBean> getData(int pageNumber, int pageSize) {
        return dataService.findSecurityGroup().stream();
    }

    @Override
    protected String[] visibleProperties() {
        return new String[] { "securityGroupName", "priority", "isActive" };
    }

    @Override
    protected GxAbstractEntityForm<GxSecurityGroupBean> getEntityForm(GxSecurityGroupBean entity) {
        return entityForm;
    }

    @Override
    protected void onSave(GxSecurityGroupBean entity) {
        dataService.save(entity);
    }

    @Override
    protected void onDelete(Collection<GxSecurityGroupBean> entities) {
        for (GxSecurityGroupBean entity : entities) {
            dataService.delete(entity);
        }
    }

    @Override
    protected void preEdit(GxSecurityGroupBean entity) {
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
