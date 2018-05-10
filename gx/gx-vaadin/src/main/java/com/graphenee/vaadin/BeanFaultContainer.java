package com.graphenee.vaadin;

import java.util.Collection;
import java.util.Iterator;

import com.graphenee.core.model.BeanFault;
import com.graphenee.core.util.KeyValueWrapper;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;

@SuppressWarnings("serial")
public class BeanFaultContainer<ID, BEANTYPE> extends BeanItemContainer<BeanFault<ID, BEANTYPE>> {

	private String idPropertyName;

	public BeanFaultContainer(String idPropertyName) {
		super(BeanFault.class);
		this.idPropertyName = idPropertyName;
	}

	public BeanFaultContainer(String idPropertyName, Collection<BEANTYPE> beanCollection) throws IllegalArgumentException {
		super(BeanFault.class);
		this.idPropertyName = idPropertyName;
		setBeans(beanCollection);
	}

	public void setBeans(Collection<BEANTYPE> beanCollection) {
		removeAllItems();
		if (beanCollection != null) {
			beanCollection.forEach(bean -> {
				ID oid = (ID) new KeyValueWrapper(bean).valueForKeyPath(idPropertyName);
				addBean(BeanFault.beanFault(oid, bean));
			});
		}
	}

	@Override
	protected boolean passesFilters(Object itemId) {
		BeanItem<BeanFault<ID, BEANTYPE>> item = getUnfilteredItem(itemId);
		if (getFilters().isEmpty() || item.getBean() == null) {
			return true;
		}
		final Iterator<Filter> i = getFilters().iterator();
		while (i.hasNext()) {
			final Filter f = i.next();
			if (f instanceof SimpleStringFilter) {
				SimpleStringFilter sf = (SimpleStringFilter) f;
				KeyValueWrapper kvw = new KeyValueWrapper(item.getBean().getBean());
				String value = kvw.stringForKeyPath(sf.getPropertyId().toString());
				if (value == null) {
					return false;
				}
				if (sf.isIgnoreCase()) {
					if (sf.isOnlyMatchPrefix()) {
						return value.toLowerCase().startsWith(sf.getFilterString().toLowerCase());
					}
					return value.toLowerCase().contains(sf.getFilterString().toLowerCase());
				} else {
					if (sf.isOnlyMatchPrefix()) {
						return value.startsWith(sf.getFilterString());
					}
					return value.contains(sf.getFilterString());
				}
			}
			if (!f.passesFilter(itemId, item)) {
				return false;
			}
		}
		return true;
	}

}
