package io.graphenee.core.flow.domain;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.GxAuditLogDataService;
import io.graphenee.core.GxDataService;
import io.graphenee.core.model.entity.GxDomain;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.util.DnsUtils;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.GxAbstractEntityList;
import io.graphenee.vaadin.flow.component.GxCopyToClipboardWrapper;
import io.graphenee.vaadin.flow.component.GxFormLayout;
import io.graphenee.vaadin.flow.component.GxNotification;

@SpringComponent
@Scope("prototype")
public class GxDomainList extends GxAbstractEntityList<GxDomain> {
    private static final long serialVersionUID = 1L;

    @Autowired
    GxAuditLogDataService auditService;

    @Autowired
    GxDataService dataService;

    @Autowired
    GxDomainForm entityForm;

    private GxNamespace namespace = null;

    private Button verifyDomain;

    public GxDomainList() {
        super(GxDomain.class);
    }

    @Override
    protected Stream<GxDomain> getData() {
        if (namespace == null)
            return dataService.findAllDomains().stream();
        return dataService.findDomainsByNamespace(namespace).stream();
    }

    @Override
    protected String[] visibleProperties() {
        return new String[] { "dns", "isActive", "isVerified", "txtRecord" };
    }

    @Override
    protected GxAbstractEntityForm<GxDomain> getEntityForm(GxDomain entity) {
        return entityForm;
    }

    @Override
    protected void onSave(GxDomain entity) {
        dataService.save(entity);
    }

    @Override
    protected void onDelete(Collection<GxDomain> entities) {
        for (GxDomain entity : entities) {
            dataService.delete(entity);
        }
    }

    @Override
    protected void preEdit(GxDomain entity) {
        if (entity.getOid() == null) {
            entity.setNamespace(namespace);
        }
    }

    @Override
    protected Renderer<GxDomain> rendererForProperty(String propertyName,
            PropertyDefinition<GxDomain, ?> propertyDefinition) {
        if (propertyName.matches("txtRecord")) {
            return new ComponentRenderer<>(s -> {
                return new GxCopyToClipboardWrapper(new Span(s.getTxtRecord()));
            });
        }
        return super.rendererForProperty(propertyName, propertyDefinition);
    }

    @Override
    protected void decorateSearchForm(GxFormLayout searchForm, Binder<GxDomain> searchBinder) {
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
        // Making the namespace filter visible to Super Admin only
        Boolean flag = loggedInUser().canDoAction("namespaces", "view", null);
        namespaceComboBox.setVisible(flag);
    }

    public void initializeWithNamespace(GxNamespace namespace) {
        this.namespace = namespace;
        refresh();
    }

    @Override
    protected void decorateToolbarLayout(HorizontalLayout toolbarLayout) {
        verifyDomain = new Button("Verify Domain");
        verifyDomain.setEnabled(false);
        verifyDomain.addClickListener(cl -> {
            entityGrid().getSelectedItems().forEach(domain -> {
                Set<String> txtRecords = DnsUtils.dnsTxtRecord(domain.getDns());
                if (txtRecords.contains(domain.getTxtRecord())) {
                    domain.setIsVerified(true);
                    onSave(domain);
                    GxNotification.success(domain.getDns() + " has been verified.");
                } else {
                    domain.setIsVerified(false);
                    onSave(domain);
                    GxNotification.error(domain.getDns() + " could not be verified.");
                }
            });
            getUI().ifPresent(ui -> {
                ui.access(() -> refresh());
            });
        });
        toolbarLayout.add(verifyDomain);
    }

    @Override
    protected void onGridItemSelect(SelectionEvent<Grid<GxDomain>, GxDomain> event) {
        long verified = event.getAllSelectedItems().stream().filter(f -> f.getIsVerified()).count();
        long unverified = event.getAllSelectedItems().stream().filter(f -> !f.getIsVerified()).count();
        verifyDomain.setEnabled(unverified > 0 && verified == 0);
    }

}
