package ${package}.vaadin;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

import io.graphenee.vaadin.flow.GxSecuredView;
import io.graphenee.vaadin.flow.GxVerticalLayoutView;

@GxSecuredView
@Route(value = "", layout = MainLayout.class)
@Scope("prototype")
@PreserveOnRefresh
public class HomeView extends GxVerticalLayoutView {

    private static final long serialVersionUID = 1L;

}
