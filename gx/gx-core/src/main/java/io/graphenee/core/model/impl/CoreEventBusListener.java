package io.graphenee.core.model.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxUserAccountBean;
import jakarta.annotation.PostConstruct;

@Service
public class CoreEventBusListener {
    @Autowired
    EventBus coreEventBus;

    @Autowired
    GxDataService gxDataService;

    @PostConstruct
    void postConstruct() {
        coreEventBus.register(this);
    }

    @Subscribe
    public void onUserChanged(GxUserAccountBean user) {
        gxDataService.save(user);
    }
}
