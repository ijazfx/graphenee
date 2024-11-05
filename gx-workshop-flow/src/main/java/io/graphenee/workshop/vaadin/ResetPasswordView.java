package io.graphenee.workshop.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

import io.graphenee.util.callback.TRErrorCallback;
import io.graphenee.util.callback.TRVoidCallback;
import io.graphenee.vaadin.flow.GxAbstractFlowSetup;
import io.graphenee.vaadin.flow.GxAbstractResetPasswordView;

@Route(value = "reset-password")
@Scope("prototype")
@PreserveOnRefresh
public class ResetPasswordView extends GxAbstractResetPasswordView {

    private static final long serialVersionUID = 1L;

    @Autowired
    GxAbstractFlowSetup flowSetup;

    // @Autowired
    // GxDataService dataService;
    //
    // @Autowired
    // GxPasswordPolicyDataService passwordService;

    @Override
    protected GxAbstractFlowSetup flowSetup() {
        return flowSetup;
    }

    @Override
    protected void sendSecurityPinToUser(String securityPin, String username, TRVoidCallback success,
            TRErrorCallback error) {
        System.out.println("Security Pin = " + securityPin);
        success.execute();
    }

    @Override
    protected void changePassword(String username, String password, TRVoidCallback success, TRErrorCallback error) {
        System.out.println("Password changed");
        // try {
        // passwordService.changePassword(dataService.systemNamespace(), username,
        // password);
        success.execute();
        // } catch (ChangePasswordFailedException e) {
        // error.execute(e);
        // }
    }

}
