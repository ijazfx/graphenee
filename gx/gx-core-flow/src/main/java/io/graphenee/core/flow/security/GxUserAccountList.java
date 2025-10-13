package io.graphenee.core.flow.security;

import java.util.Collection;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.common.GxAuthenticatedUser;
import io.graphenee.core.GxAuditLogDataService;
import io.graphenee.core.GxDataService;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.GxAbstractEntityList;
import io.graphenee.vaadin.flow.component.GxFormLayout;

@SpringComponent
@Scope("prototype")
public class GxUserAccountList extends GxAbstractEntityList<GxUserAccount> {

    private static final long serialVersionUID = 1L;

    @Autowired
    GxDataService dataService;

    @Autowired
    GxAuditLogDataService auditService;

    @Autowired
    GxUserAccountForm entityForm;

   private GxNamespace namespace = null;

    public GxUserAccountList() {
        super(GxUserAccount.class);
    }

    @Override
    protected String[] visibleProperties() {
        return new String[] { "firstName", "lastName", "username", "email", "isActive", "createdBy", "dateCreated", "modifiedBy", "dateModified" };
    }

    @Override
    protected Stream<GxUserAccount> getData() {
        if (namespace == null)
            return dataService.findUserAccount().stream();
        return dataService.findUserAccountByNamespace(namespace).stream();
    }

    @Override
    protected GxAbstractEntityForm<GxUserAccount> getEntityForm(GxUserAccount entity) {
        return entityForm;
    }

    @Override
    public void onSave(GxUserAccount entity) {
        dataService.save(entity);
    }

    @Override
    protected void onDelete(Collection<GxUserAccount> entities) {
        for (GxUserAccount entity : entities) {
            dataService.delete(entity);
        }
    }

    @Override
    protected void preEdit(GxUserAccount entity) {
        if (entity.getOid() == null) {
            entity.setNamespace(namespace);
        }
    }

    @Override
    protected void decorateSearchForm(GxFormLayout searchForm, Binder<GxUserAccount> searchBinder) {
        ComboBox<GxNamespace> namespaceComboBox = new ComboBox<>("Namespace");
        namespaceComboBox.setItemLabelGenerator(GxNamespace::getNamespace);
        namespaceComboBox.setClearButtonVisible(true);
        namespaceComboBox.setItems(dataService.findNamespace());
        namespaceComboBox.setValue(namespace);
        namespaceComboBox.addValueChangeListener(vcl -> {
            namespace = vcl.getValue();
            refresh();
        });
        searchForm.add(namespaceComboBox);
        //Making the namespace filter visible to Super Admin only
        Boolean flag = loggedInUser().canDoAction("namespace-filter","view", null);
        namespaceComboBox.setVisible(flag);
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
    protected void auditLog(GxAuthenticatedUser user, String remoteAddress, String auditEvent, String auditEntity,
            Collection<GxUserAccount> entities) {
        entities.forEach(e -> {
            auditService.log(user, remoteAddress, auditEvent, e.getUsername(), auditEntity, e.getOid());
        });
    }

}
