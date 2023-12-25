package io.graphenee.vaadin.flow.base;

import java.util.Optional;

import org.json.JSONObject;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.data.binder.BeanPropertySet;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.binder.PropertySet;

@SuppressWarnings("serial")
public class GxPreferenceForm<T> extends GxAbstractEntityForm<JSONObject> {

	private HasComponents entityForm;
	private Grid<T> entityGrid;
	private Class<T> entityClass;

	public GxPreferenceForm() {
		super(JSONObject.class);
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
	protected void preBinding(JSONObject entity) {
		entityForm.removeAll();
		String key = entityClass.getSimpleName();
		PropertySet<T> propertySet = BeanPropertySet.get(entityClass);
		final JSONObject prefs;
		if (!getEntity().has(key)) {
			prefs = new JSONObject();
			getEntity().put(key, prefs);
		} else {
			prefs = getEntity().getJSONObject(key);
		}
		final JSONObject json;
		if (!prefs.has("props")) {
			json = new JSONObject();
			prefs.put("props", json);
		} else {
			json = prefs.getJSONObject("props");
		}
		H2 columns = new H2("Columns");
		expand(columns);
		entityForm.add(columns);
		entityGrid.getColumns().stream().filter(c -> c.getKey() != null).map(c -> c.getKey()).forEach(prop -> {
			final JSONObject propJson;
			if (!json.has(prop)) {
				propJson = new JSONObject();
				json.put(prop, propJson);
			} else {
				propJson = json.getJSONObject(prop);
			}
			Optional<PropertyDefinition<T, ?>> p = propertySet.getProperty(prop);
			if (p.isPresent()) {
				PropertyDefinition<T, ?> pd = p.get();
				Checkbox c = new Checkbox(pd.getCaption());
				try {
					c.setValue(propJson.getBoolean("show"));
				} catch (Exception ex) {
					propJson.put("show", true);
					c.setValue(true);
				}
				c.addValueChangeListener(vcl -> {
					propJson.put("show", vcl.getValue());
				});
				entityForm.add(c);
			}
		});
	}

	@Override
	protected String formTitle() {
		return "Preferences";
	}

}
