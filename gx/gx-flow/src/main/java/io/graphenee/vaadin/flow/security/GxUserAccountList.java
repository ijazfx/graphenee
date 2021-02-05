package io.graphenee.vaadin.flow.security;

import java.util.Collection;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxUserAccountBean;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxAbstractEntityList;

@Component
@Scope("prototype")
public class GxUserAccountList extends GxAbstractEntityList<GxUserAccountBean> {

    private static final long serialVersionUID = 1L;

    @Autowired
    GxDataService dataService;

    @Autowired
    GxUserAccountForm entityForm;

    public GxUserAccountList() {
        super(GxUserAccountBean.class);
    }

    @Override
    protected String[] visibleProperties() {
        return new String[] { "firstName", "lastName", "username", "email", "isActive" };
    }

    @Override
    protected int getTotalCount() {
        return dataService.findUserAccount().size();
    }

    @Override
    protected Stream<GxUserAccountBean> getData(int pageNumber, int pageSize) {
        return dataService.findUserAccount().stream();
    }

    @Override
    protected GxAbstractEntityForm<GxUserAccountBean> getEntityForm(GxUserAccountBean entity) {
        return entityForm;
    }

    @Override
    public void onSave(GxUserAccountBean entity) {
        dataService.save(entity);
    }

    @Override
    protected void onDelete(Collection<GxUserAccountBean> entities) {
        for (GxUserAccountBean entity : entities) {
            dataService.delete(entity);
        }
    }

    @Override
    protected void preEdit(GxUserAccountBean entity) {
        if (entity.getOid() == null) {
            GxNamespaceBean namespaceBean = dataService.findSystemNamespace();
            entity.setNamespaceFault(new BeanFault<>(namespaceBean.getOid(), namespaceBean));
        }
    }

    @Override
    protected boolean shouldShowFormInDialog() {
        return false;
    }

}
