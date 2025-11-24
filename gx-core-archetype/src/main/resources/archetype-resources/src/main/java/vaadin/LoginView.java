package ${package}.vaadin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.login.AbstractLogin.LoginEvent;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

import io.graphenee.common.GxAuthenticatedUser;
import io.graphenee.common.exception.AuthenticationFailedException;
import io.graphenee.common.exception.PasswordChangeRequiredException;
import io.graphenee.core.GxDataService;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.vaadin.flow.GxAbstractFlowSetup;
import io.graphenee.vaadin.flow.GxAbstractLoginView;

@Route(value = "login")
@Scope("prototype")
@PreserveOnRefresh
public class LoginView extends GxAbstractLoginView {

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
    protected GxAuthenticatedUser onLogin(LoginEvent event)
            throws AuthenticationFailedException, PasswordChangeRequiredException {
        String username = event.getUsername();
        String password = event.getPassword();
        Optional<GxNamespace> namespace = dataService.findNamespaceByHost(host());
        GxUserAccount user = null;
        if (namespace.isPresent()) {
            user = dataService.findUserAccountByUsernamePasswordAndNamespace(username, password, namespace.get());
        } else {
            user = dataService.findUserAccountByUsernamePasswordAndNamespace(username, password, dataService.systemNamespace());
            if(user == null) {
                user = dataService.findUserAccountByUsernameAndPassword(username, password);
            }
        }
        if (user == null) {
            throw new AuthenticationFailedException("Invalid credentials, please try again.");
        }
        if (user.getIsLocked()) {
            throw new AuthenticationFailedException("The account is locked, please contact system administrator.");
        }
        if (user.getIsPasswordChangeRequired()) {
            throw new PasswordChangeRequiredException();
        }
        return user;
    }

}
