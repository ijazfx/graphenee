package io.graphenee.core.model.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxUserAccountBean;

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
