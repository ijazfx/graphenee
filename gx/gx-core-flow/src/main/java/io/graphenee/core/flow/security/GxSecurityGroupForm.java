package io.graphenee.core.flow.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.flowingcode.vaadin.addons.twincolgrid.TwinColGrid;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.GxDataService;
import io.graphenee.core.model.entity.GxSecurityGroup;
import io.graphenee.core.model.entity.GxSecurityPolicy;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.GxTabItem;

@SpringComponent
@Scope("prototype")
public class GxSecurityGroupForm extends GxAbstractEntityForm<GxSecurityGroup> {
    private static final long serialVersionUID = 1L;

    private TextField securityGroupName;
    private TextField securityGroupDescription;
    private TextField priority;
    private Checkbox isActive;
    private TwinColGrid<GxSecurityPolicy> securityPolicies;
    private TwinColGrid<GxUserAccount> userAccounts;

    @Autowired
    GxDataService dataService;

    public GxSecurityGroupForm() {
        super(GxSecurityGroup.class);
    }

    @Override
    protected void decorateForm(HasComponents entityForm) {
        securityGroupName = new TextField("Group Name");
        securityGroupDescription = new TextField("Group Description");
        priority = new TextField("Priority");
        isActive = new Checkbox("Is Active?");
        securityPolicies = new TwinColGrid<GxSecurityPolicy>()
                .addFilterableColumn(GxSecurityPolicy::getSecurityPolicyName, "Policy Name", "Policy Name", true)
                .withAvailableGridCaption("Available").withSelectionGridCaption("Selected").withDragAndDropSupport();
        securityPolicies.setSizeFull();

        userAccounts = new TwinColGrid<GxUserAccount>()
                .addFilterableColumn(GxUserAccount::getUsername, "User Name", "User Name", true)
                .withAvailableGridCaption("Available")
                .withSelectionGridCaption("Selected").withDragAndDropSupport();
        userAccounts.setSizeFull();

        entityForm.add(securityGroupName, securityGroupDescription, priority, isActive);
    }

    @Override
    protected void bindFields(Binder<GxSecurityGroup> dataBinder) {
        dataBinder.forMemberField(priority).withConverter(new StringToIntegerConverter("value must be integer"));
    }

    @Override
    protected void addTabsToForm(List<GxTabItem> tabItems) {
        tabItems.add(GxTabItem.create(1, "Users", userAccounts));
        tabItems.add(GxTabItem.create(2, "Policies", securityPolicies));
    }

    @Override
    protected void preBinding(GxSecurityGroup entity) {
        securityPolicies.setItems(dataService.findSecurityPolicyByNamespaceActive(entity.getNamespace()));
        userAccounts.setItems(dataService.findUserAccountByNamespace(entity.getNamespace()));
    }

}
