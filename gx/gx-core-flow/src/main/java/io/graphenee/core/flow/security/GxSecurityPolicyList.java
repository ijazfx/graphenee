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
import io.graphenee.core.model.entity.GxSecurityPolicy;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.GxAbstractEntityList;
import io.graphenee.vaadin.flow.component.GxFormLayout;

@SpringComponent
@Scope("prototype")
public class GxSecurityPolicyList extends GxAbstractEntityList<GxSecurityPolicy> {
    private static final long serialVersionUID = 1L;

    @Autowired
    GxAuditLogDataService auditService;

    @Autowired
    GxDataService dataService;

    @Autowired
    GxSecurityPolicyForm securityPolicyForm;

    private GxNamespace namespace = null;

    public GxSecurityPolicyList() {
        super(GxSecurityPolicy.class);
    }

    @Override
    protected Stream<GxSecurityPolicy> getData() {
        if (namespace == null)
            return dataService.findSecurityPolicy().stream();
        return dataService.findSecurityPolicyByNamespace(namespace).stream();
    }

    @Override
    protected String[] visibleProperties() {
        return new String[] { "securityPolicyName", "priority", "isActive" };
    }

    @Override
    protected GxAbstractEntityForm<GxSecurityPolicy> getEntityForm(GxSecurityPolicy entity) {
        return securityPolicyForm;
    }

    @Override
    protected void onSave(GxSecurityPolicy entity) {
        dataService.save(entity);
    }

    @Override
    protected void onDelete(Collection<GxSecurityPolicy> entities) {
        for (GxSecurityPolicy entity : entities) {
            dataService.delete(entity);
        }
    }

    protected void preEdit(GxSecurityPolicy entity) {
        if (entity.getOid() == null) {
            entity.setNamespace(namespace);
        }
    }

    @Override
    protected void decorateSearchForm(GxFormLayout searchForm, Binder<GxSecurityPolicy> searchBinder) {
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
        Boolean flag = loggedInUser().canDoAction("namespace-filter","view",true);
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
            Collection<GxSecurityPolicy> entities) {
        entities.forEach(e -> {
            auditService.log(user, remoteAddress, auditEvent, e.getSecurityPolicyName(), auditEntity, e.getOid());
        });
    }

}
