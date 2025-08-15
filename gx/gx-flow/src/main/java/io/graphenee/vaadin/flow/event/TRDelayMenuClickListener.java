package io.graphenee.vaadin.flow.event;

import java.util.concurrent.Executors;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu.GridContextMenuItemClickEvent;

public abstract class TRDelayMenuClickListener<T, C extends Component> implements ComponentEventListener<GridContextMenu.GridContextMenuItemClickEvent<T>> {

    private static final long serialVersionUID = 1L;

    private long sleepDuration = 1000L;
    volatile boolean isFired = false;

    @Override
    public void onComponentEvent(GridContextMenuItemClickEvent<T> event) {
        if (!isFired) {
            synchronized (this) {
                if (!isFired) {
                    isFired = true;
                    try {
                        onClick(event);
                    } finally {
                        Executors.newVirtualThreadPerTaskExecutor().execute(() -> {
                            try {
                                Thread.sleep(sleepDuration);
                            } catch (InterruptedException e) {
                            } finally {
                                isFired = false;
                            }
                        });
                    }
                }
            }
        }
    }

    public TRDelayMenuClickListener<T, C> withSleepDuration(long sleepDuration) {
        this.sleepDuration = sleepDuration;
        return this;
    }

    public abstract void onClick(GridContextMenuItemClickEvent<T> event);
}
