package io.graphenee.vaadin.flow.domain;

import java.util.concurrent.atomic.AtomicInteger;

import io.graphenee.core.model.GxAuthenticatedUser;

public abstract class AbstractDashboardUser<T> implements GxAuthenticatedUser {

    private T user;
    private AtomicInteger notificationCount = new AtomicInteger();

    public AbstractDashboardUser(T user) {
        this.user = user;
    }

    public T getUser() {
        return user;
    }

    @Override
    public int getUnreadNotificationCount() {
        return notificationCount.get();
    }

    @Override
    public void setUnreadNotificationCount(int count) {
        notificationCount.set(count);
    }
}
