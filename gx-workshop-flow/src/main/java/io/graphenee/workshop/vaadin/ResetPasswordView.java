package io.graphenee.workshop.vaadin;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.Route;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.util.callback.TRErrorCallback;
import io.graphenee.util.callback.TRVoidCallback;
import io.graphenee.vaadin.flow.base.GxAbstractFlowSetup;
import io.graphenee.vaadin.flow.base.GxAbstractResetPasswordView;

@Route(value = "reset-password")
public class ResetPasswordView extends GxAbstractResetPasswordView {

    private static final long serialVersionUID = 1L;

    @Autowired
    GxAbstractFlowSetup flowSetup;

    @Autowired
    GxDataService dataService;

    @Override
    protected GxAbstractFlowSetup flowSetup() {
        return flowSetup;
    }

    @Override
    protected void sendSecurityPinToUser(String securityPin, String username, TRVoidCallback success, TRErrorCallback error) {
        System.out.println("Security Pin = " + securityPin);
        success.execute();
    }

    @Override
    protected void changePassword(String username, String password, TRVoidCallback success, TRErrorCallback error) {
        System.out.println("Password changed");
        success.execute();
    }

}
