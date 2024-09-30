package io.graphenee.vaadin.flow.namespace;

import java.util.Collection;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.api.GxUserSessionDetailDataService;
import io.graphenee.core.model.entity.GxUserSessionDetail;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxAbstractEntityList;

@SpringComponent
@Scope("prototype")
public class GxUserSessionDetailList extends GxAbstractEntityList<GxUserSessionDetail> {

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

}
