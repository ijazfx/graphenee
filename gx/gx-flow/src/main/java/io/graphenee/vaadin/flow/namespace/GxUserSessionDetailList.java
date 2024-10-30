package io.graphenee.vaadin.flow.namespace;

import java.util.Collection;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.hazelcast.core.IMap;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.api.GxUserSessionDetailDataService;
import io.graphenee.core.model.entity.GxUserSessionDetail;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxAbstractEntityList;

@SpringComponent
@Scope("prototype")
public class GxUserSessionDetailList extends GxAbstractEntityList<GxUserSessionDetail> {

    @Autowired
    public IMap<String, Boolean> sessionMap;

    public GxUserSessionDetailList() {
        super(GxUserSessionDetail.class);
    }

    @Autowired
    GxUserSessionDetailDataService dataService;

    @Override
    protected Stream<GxUserSessionDetail> getData() {
        return dataService.fetchAll().stream();
    }

    @Override
    protected String[] visibleProperties() {
        return new String[] { "namespaceName", "userName", "isSignedIn", "signedinAt", "lastSync"};
    }

    @Override
    protected GxAbstractEntityForm<GxUserSessionDetail> getEntityForm(GxUserSessionDetail entity) {
        return null;
    }

    @Override
    protected void onSave(GxUserSessionDetail entity) {
        // dataService.save(entity);
    }

    @Override
    protected void onDelete(Collection<GxUserSessionDetail> entities) {
        dataService.delete(entities);
    }

    @Override
    protected void decorateMenuBar(MenuBar menuBar) {
        menuBar.addItem("Show Map", l -> {
            sessionMap.forEach((k, v) -> System.out.println("Key: " + k + " V: " + v));
            System.out.println("-----------------------------");
        });
        menuBar.addItem("Add", l -> {
            Integer i = sessionMap.keySet().size() + 1;
            sessionMap.putIfAbsent(i.toString(), true);
            System.out.println("-----------------------------");
        });
    }

}
