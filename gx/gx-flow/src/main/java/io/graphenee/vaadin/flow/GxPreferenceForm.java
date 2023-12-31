package io.graphenee.vaadin.flow;

import java.util.Optional;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.data.binder.BeanPropertySet;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.binder.PropertySet;

import io.graphenee.vaadin.flow.model.ColumnPreferences;
import io.graphenee.vaadin.flow.model.GridPreferences;
import io.graphenee.vaadin.flow.model.GxPreferences;

@SuppressWarnings("serial")
public class GxPreferenceForm<T> extends GxAbstractEntityForm<GxPreferences> {

	private HasComponents entityForm;
	private Grid<T> entityGrid;
	private Class<T> entityClass;

	public GxPreferenceForm(GxEventBus eventBus) {
		super(GxPreferences.class);
		setEventBus(eventBus);
	}

	@Override
	protected void decorateForm(HasComponents entityForm) {
		this.entityForm = entityForm;
	}

	public void initializeWith(Grid<T> entityGrid, Class<T> entityClass) {
		this.entityGrid = entityGrid;
		this.entityClass = entityClass;
	}

	@Override
	protected void preBinding(GxPreferences entity) {
		entityForm.removeAll();
		GridPreferences grid = entity.addGrid(entityClass.getSimpleName());
		PropertySet<T> propertySet = BeanPropertySet.get(entityClass);
		H2 columns = new H2("Columns");
		columns.setWidthFull();
		expand(columns);
		entityForm.add(columns);
		entityGrid.getColumns().stream().filter(c -> c.getKey() != null && !c.getKey().startsWith("__")).map(c -> c.getKey()).forEach(columnName -> {
			ColumnPreferences column = grid.addColumn(columnName);
			Optional<PropertyDefinition<T, ?>> p = propertySet.getProperty(columnName);
			if (p.isPresent()) {
				PropertyDefinition<T, ?> pd = p.get();
				Checkbox c = new Checkbox(pd.getCaption());
				try {
					c.setValue(column.getVisible());
				} catch (Exception ex) {
					column.setVisible(true);
					c.setValue(true);
				}
				c.addValueChangeListener(vcl -> {
					column.setVisible(vcl.getValue());
				});
				c.setWidthFull();
				entityForm.add(c);
			}
		});
	}

	@Override
	protected String formTitle() {
		return "Preferences";
	}

}
