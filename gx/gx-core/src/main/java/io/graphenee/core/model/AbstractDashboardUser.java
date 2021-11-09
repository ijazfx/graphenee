package io.graphenee.core.model;

import java.util.concurrent.atomic.AtomicInteger;

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
