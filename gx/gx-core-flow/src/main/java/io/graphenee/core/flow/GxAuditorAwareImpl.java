package io.graphenee.core.flow;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

import io.graphenee.vaadin.flow.utils.DashboardUtils;

public class GxAuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        if(DashboardUtils.getLoggedInUser() != null) {
            return Optional.of(DashboardUtils.getLoggedInUsername());
        }
        return Optional.of("system");
    }
    
}
