package io.graphenee.core.model.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.entity.GxUserAccount;
import jakarta.annotation.PostConstruct;

@Service
public class CoreEventBusListener {
	@Autowired
	EventBus coreEventBus;

	@Autowired
	GxDataService dataService;

	@PostConstruct
	void postConstruct() {
		coreEventBus.register(this);
	}

	@Subscribe
	public void onUserChanged(GxUserAccount user) {
		dataService.save(user);
	}
}
