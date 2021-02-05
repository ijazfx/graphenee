package io.graphenee.vaadin.flow.security;

import java.util.List;

import com.flowingcode.vaadin.addons.twincolgrid.TwinColGrid;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxSecurityGroupBean;
import io.graphenee.core.model.bean.GxSecurityPolicyBean;
import io.graphenee.core.model.bean.GxUserAccountBean;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxTabItem;
import io.graphenee.vaadin.flow.converter.BeanCollectionFaultToSetConverter;

@Component
@Scope("prototype")
public class GxSecurityGroupForm extends GxAbstractEntityForm<GxSecurityGroupBean> {
    private static final long serialVersionUID = 1L;

    private TextField securityGroupName;
    private TextField securityGroupDescription;
    private TextField priority;
    private Checkbox isActive;
    private TwinColGrid<GxSecurityPolicyBean> securityPolicyCollectionFault;
    private TwinColGrid<GxUserAccountBean> userAccountCollectionFault;

    @Autowired
    GxDataService gxDataService;

    public GxSecurityGroupForm() {
        super(GxSecurityGroupBean.class);
    }

    @Override
    protected void decorateForm(HasComponents entityForm) {
        securityGroupName = new TextField("Group Name");
        securityGroupDescription = new TextField("Group Description");
        priority = new TextField("Priority");
        isActive = new Checkbox("Is Active?");
        securityPolicyCollectionFault = new TwinColGrid<GxSecurityPolicyBean>().addFilterableColumn(GxSecurityPolicyBean::getSecurityPolicyName, "Policy Name", "Policy Name", true)
                .withLeftColumnCaption("Available").withRightColumnCaption("Selected");
        securityPolicyCollectionFault.setSizeFull();
        userAccountCollectionFault = new TwinColGrid<GxUserAccountBean>().addFilterableColumn(GxUserAccountBean::getUsername, "User Name", "User Name", true)
                .withLeftColumnCaption("Available").withRightColumnCaption("Selected");
        userAccountCollectionFault.setSizeFull();

        entityForm.add(securityGroupName, securityGroupDescription, priority, isActive);
    }

    @Override
    protected void bindFields(Binder<GxSecurityGroupBean> dataBinder) {
        dataBinder.forMemberField(priority).withConverter(new StringToIntegerConverter("value must be integer"));
        dataBinder.forMemberField(securityPolicyCollectionFault).withConverter(new BeanCollectionFaultToSetConverter<GxSecurityPolicyBean>());
        dataBinder.forMemberField(userAccountCollectionFault).withConverter(new BeanCollectionFaultToSetConverter<GxUserAccountBean>());
    }

    @Override
    protected void addTabsToForm(List<GxTabItem> tabItems) {
        tabItems.add(GxTabItem.create(1, "Users", userAccountCollectionFault));
        tabItems.add(GxTabItem.create(2, "Policies", securityPolicyCollectionFault));
    }

    @Override
    protected void preBinding(GxSecurityGroupBean entity) {
        securityPolicyCollectionFault.setItems(gxDataService.findSecurityPolicyActive());
        userAccountCollectionFault.setItems(gxDataService.findUserAccountActive());
    }
}
