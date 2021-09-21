package io.graphenee.vaadin.flow.security;

import java.util.ArrayList;
import java.util.List;

import com.flowingcode.vaadin.addons.twincolgrid.TwinColGrid;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxSecurityGroupBean;
import io.graphenee.core.model.bean.GxSecurityPolicyBean;
import io.graphenee.core.model.bean.GxSecurityPolicyDocumentBean;
import io.graphenee.core.model.bean.GxUserAccountBean;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxTabItem;
import io.graphenee.vaadin.flow.converter.BeanCollectionFaultToSetConverter;

@Component
@Scope("prototype")
public class GxSecurityPolicyForm extends GxAbstractEntityForm<GxSecurityPolicyBean> {
    private static final long serialVersionUID = 1L;

    private TextField securityPolicyName;
    private TextField securityPolicyDescription;
    private TextField priority;
    private Checkbox isActive;
    private ComboBox<GxSecurityPolicyDocumentBean> securityPolicyDocumentComboBox;
    private TextArea jsonDocumentTextArea;
    private TwinColGrid<GxSecurityGroupBean> securityGroupCollectionFault;
    private TwinColGrid<GxUserAccountBean> userAccountCollectionFault;

    GxSecurityPolicyDocumentBean selectedDocumentBean;

    @Autowired
    GxDataService gxDataService;

    public GxSecurityPolicyForm() {
        super(GxSecurityPolicyBean.class);
    }

    @Override
    protected void decorateForm(HasComponents entityForm) {
        securityPolicyName = new TextField("Policy Name");
        securityPolicyDescription = new TextField("Policy Description");
        priority = new TextField("Priority");
        isActive = new Checkbox("Is Active?");

        Button createButton = new Button("Create");
        createButton.addClickListener(event -> {
            selectedDocumentBean = new GxSecurityPolicyDocumentBean();
            selectedDocumentBean.setIsDefault(false);
            // selectedDocumentBean.setDocumentJson("");
            getEntity().getSecurityPolicyDocumentCollectionFault().add(selectedDocumentBean);
            securityPolicyDocumentComboBox.setItems(getEntity().getSecurityPolicyDocumentCollectionFault().getBeans());
            securityPolicyDocumentComboBox.setValue(selectedDocumentBean);
        });

        Button cloneButton = new Button("Clone");
        cloneButton.addClickListener(event -> {
            if (selectedDocumentBean.getOid() != null) {
                GxSecurityPolicyDocumentBean cloned = new GxSecurityPolicyDocumentBean();
                cloned.setIsDefault(false);
                cloned.setDocumentJson(selectedDocumentBean.getDocumentJson());
                selectedDocumentBean = cloned;
                getEntity().getSecurityPolicyDocumentCollectionFault().add(selectedDocumentBean);
                securityPolicyDocumentComboBox.setValue(selectedDocumentBean);
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.addClickListener(event -> {
            if (selectedDocumentBean != null) {
                getEntity().getSecurityPolicyDocumentCollectionFault().remove(selectedDocumentBean);
                securityPolicyDocumentComboBox.setItems(getEntity().getSecurityPolicyDocumentCollectionFault().getBeans());
                securityPolicyDocumentComboBox.clear();
                jsonDocumentTextArea.clear();
            }
        });

        Button makeDefaultButton = new Button("Make Default");
        makeDefaultButton.addClickListener(event -> {
            if (selectedDocumentBean != null) {
                List<GxSecurityPolicyDocumentBean> documents = new ArrayList<>(getEntity().getSecurityPolicyDocumentCollectionFault().getBeans());
                documents.forEach(bean -> {
                    bean.setIsDefault(false);
                    getEntity().getSecurityPolicyDocumentCollectionFault().update(bean);
                });
                selectedDocumentBean.setIsDefault(true);
                getEntity().getSecurityPolicyDocumentCollectionFault().update(selectedDocumentBean);
                makeDefaultButton.setEnabled(false);
            }
        });

        securityPolicyDocumentComboBox = new ComboBox<GxSecurityPolicyDocumentBean>();

        createButton.setEnabled(true);
        cloneButton.setEnabled(false);
        makeDefaultButton.setEnabled(false);
        deleteButton.setEnabled(false);

        securityPolicyDocumentComboBox.addValueChangeListener(event -> {
            selectedDocumentBean = (GxSecurityPolicyDocumentBean) securityPolicyDocumentComboBox.getValue();
            if (selectedDocumentBean != null) {
                makeDefaultButton.setEnabled(!selectedDocumentBean.getIsDefault());
                deleteButton.setEnabled(true);
                if (selectedDocumentBean.getOid() != null) {
                    cloneButton.setEnabled(true);
                } else {
                    cloneButton.setEnabled(false);
                }
                jsonDocumentTextArea.setEnabled(true);
                jsonDocumentTextArea.setRequired(true);
                if (selectedDocumentBean.getDocumentJson() != null)
                    jsonDocumentTextArea.setValue(selectedDocumentBean.getDocumentJson());
                jsonDocumentTextArea.focus();
            } else {
                makeDefaultButton.setEnabled(false);
                cloneButton.setEnabled(false);
                deleteButton.setEnabled(false);
                jsonDocumentTextArea.setEnabled(false);
            }
        });

        HorizontalLayout policyDocumentToolbar = new HorizontalLayout(securityPolicyDocumentComboBox, createButton, cloneButton, makeDefaultButton, deleteButton);
        jsonDocumentTextArea = new TextArea("Statements");
        jsonDocumentTextArea.getStyle().set("padding-top", "0px");
        jsonDocumentTextArea.setEnabled(true);
        jsonDocumentTextArea.setWidth("100%");
        jsonDocumentTextArea.setHeight("250px");
        jsonDocumentTextArea.addValueChangeListener(event -> {
            if (isEntityBound()) {
                if (selectedDocumentBean != null) {
                    selectedDocumentBean.setDocumentJson(event.getValue());
                    getEntity().getSecurityPolicyDocumentCollectionFault().update(selectedDocumentBean);
                }
            }
        });

        VerticalLayout policyDocumentLayout = new VerticalLayout(policyDocumentToolbar, jsonDocumentTextArea);
        policyDocumentLayout.setWidthFull();
        policyDocumentLayout.setPadding(false);
        setColspan(policyDocumentLayout, 2);
        entityForm.add(securityPolicyName, securityPolicyDescription, priority, isActive, policyDocumentLayout);

        securityGroupCollectionFault = new TwinColGrid<GxSecurityGroupBean>().addFilterableColumn(GxSecurityGroupBean::getSecurityGroupName, "Group Name", "Group Name", true)
                .withLeftColumnCaption("Available").withRightColumnCaption("Selected");
        securityGroupCollectionFault.setSizeFull();

        userAccountCollectionFault = new TwinColGrid<GxUserAccountBean>().addFilterableColumn(GxUserAccountBean::getUsername, "User Name", "User Name", true)
                .withLeftColumnCaption("Available").withRightColumnCaption("Selected");
        userAccountCollectionFault.setSizeFull();
    }

    @Override
    protected void bindFields(Binder<GxSecurityPolicyBean> dataBinder) {
        dataBinder.forMemberField(priority).withConverter(new StringToIntegerConverter("value must be integer"));
        dataBinder.forMemberField(securityGroupCollectionFault).withConverter(new BeanCollectionFaultToSetConverter<GxSecurityGroupBean>());
        dataBinder.forMemberField(userAccountCollectionFault).withConverter(new BeanCollectionFaultToSetConverter<GxUserAccountBean>());
        dataBinder.forMemberField(securityPolicyName).asRequired("Policy name is required.");
    }

    @Override
    protected void addTabsToForm(List<GxTabItem> tabItems) {
        tabItems.add(GxTabItem.create(1, "Users", userAccountCollectionFault));
        tabItems.add(GxTabItem.create(2, "Groups", securityGroupCollectionFault));
    }

    @Override
    protected void preBinding(GxSecurityPolicyBean entity) {
        jsonDocumentTextArea.clear();
        securityGroupCollectionFault.setItems(gxDataService.findSecurityGroupByNamespaceActive(entity.getNamespaceFault().getBean()));
        userAccountCollectionFault.setItems(gxDataService.findUserAccountByNamespace(entity.getNamespaceFault().getBean()));
        securityPolicyDocumentComboBox.setItems(entity.getSecurityPolicyDocumentCollectionFault().getBeans());
        if (entity.getOid() != null && entity.getDefaultSecurityPolicyDocumentBean() != null) {
            securityPolicyDocumentComboBox.setValue(entity.getDefaultSecurityPolicyDocumentBean());
            jsonDocumentTextArea.setValue(entity.getDefaultSecurityPolicyDocumentBean().getDocumentJson());
        }
    }

}