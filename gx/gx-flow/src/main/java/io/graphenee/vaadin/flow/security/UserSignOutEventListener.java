package io.graphenee.vaadin.flow.security;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinSession;

import io.graphenee.core.api.GxUserSessionDetailDataService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserSignOutEventListener implements ApplicationListener<UserSignOutEvent> {

    @Autowired
    GxUserSessionDetailDataService userSessionDetailDataService;

    @Autowired
    private Map<String, Boolean> sessionMap;

    @Override
    public void onApplicationEvent(UserSignOutEvent event) {
        try {
            // if (!sessionMap.keySet().isEmpty()) {
            Boolean userSignedIn = sessionMap
                    .containsKey(event.getIdentifer());
            if (userSignedIn == false) {
                UI.getCurrent().navigate("login");
                VaadinSession.getCurrent().close();
            }
            // }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static String getRemoteAddress() {
        return VaadinRequest.getCurrent().getRemoteAddr();
    }

}
