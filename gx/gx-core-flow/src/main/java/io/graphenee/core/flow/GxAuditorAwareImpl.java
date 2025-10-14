package io.graphenee.core.flow;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

import io.graphenee.vaadin.flow.utils.DashboardUtils;

public class GxAuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(DashboardUtils.getLoggedInUsername());
    }
    
}
