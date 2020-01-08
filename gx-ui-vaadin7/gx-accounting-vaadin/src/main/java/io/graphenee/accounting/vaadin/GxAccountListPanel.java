package io.graphenee.accounting.vaadin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid.Column;

import io.graphenee.accounting.api.GxAccountingDataService;
import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxAccountBean;
import io.graphenee.core.model.bean.GxAccountTypeBean;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.vaadin.AbstractEntityListPanel;
import io.graphenee.vaadin.TRAbstractForm;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxAccountListPanel extends AbstractEntityListPanel<GxAccountBean> {

	private GxNamespaceBean namespaceBean;

	private ComboBox namespaceComboBox;

	@Autowired
	GxDataService dataService;

	@Autowired
	GxAccountingDataService accountingDataService;

	@Autowired
	GxAccountForm form;

	private GxAccountTypeBean accountType;

	public GxAccountListPanel() {
		super(GxAccountBean.class);
	}

	@Override
	protected boolean onSaveEntity(GxAccountBean entity) {
		accountingDataService.createOrUpdate(entity);
		return true;
	}

	@Override
	protected boolean onDeleteEntity(GxAccountBean entity) {
		accountingDataService.delete(entity);
		return true;
	}

	@Override
	protected String panelCaption() {
		return null;
	}

	@Override
	protected List<GxAccountBean> fetchEntities() {
		if (namespaceBean != null) {
			return accountingDataService.findAllAccountsByNamespaceAndAccountType(namespaceBean, accountType);
		}
		return accountingDataService.findAllAccounts();
	}

	@Override
	protected <F> List<GxAccountBean> fetchEntities(F filter) {
		if (filter instanceof GxNamespaceBean) {
			return accountingDataService.findAllAccountsByNamespaceAndAccountType((GxNamespaceBean) filter, accountType);
		}
		return super.fetchEntities(filter);
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "accountCode", "accountName", "parentAccount" };
	}

	@Override
	protected TRAbstractForm<GxAccountBean> editorForm() {
		return form;
	}

	@Override
	protected boolean shouldShowDeleteConfirmation() {
		return true;
	}

	@Override
	protected boolean isGridCellFilterEnabled() {
		return true;
	}

	@Override
	protected void preEdit(GxAccountBean item) {
		if (item.getOid() == null) {
			GxNamespaceBean selectedNamespaceBean = namespaceBean != null ? namespaceBean : (GxNamespaceBean) namespaceComboBox.getValue();
			if (selectedNamespaceBean != null) {
				item.setGxNamespaceBeanFault(BeanFault.beanFault(selectedNamespaceBean.getOid(), selectedNamespaceBean));
			}
			if (accountType != null) {
				item.setGxAccountTypeBeanFault(BeanFault.beanFault(accountType.getOid(), accountType));
				item.setAccountCode(accountType.getAccountNumberSequence());
			}
		}
	}

	public void initializeWithEntity(GxNamespaceBean namespaceBean, GxAccountTypeBean accountType) {
		this.namespaceBean = namespaceBean;
		this.accountType = accountType;
		namespaceComboBox.setVisible(namespaceBean == null);
	}

	@Override
	protected void addButtonsToToolbar(AbstractOrderedLayout toolbar) {
		super.addButtonsToToolbar(toolbar);

		namespaceComboBox = new ComboBox("Namespace");
		namespaceComboBox.setTextInputAllowed(false);
		namespaceComboBox.addItems(dataService.findNamespace());
		namespaceComboBox.addValueChangeListener(event -> {
			refresh(event.getProperty().getValue());
		});
		toolbar.addComponent(namespaceComboBox);
	}

	@Override
	protected void applyRendererForColumn(Column column) {
		if (column.getPropertyId().equals("accountCode")) {
			column.setMaximumWidth(150);
		}
		if (column.getPropertyId().equals("indentedTitle")) {
			column.setHeaderCaption("Account Name");
		}
		if (column.getPropertyId().equals("parentAccount")) {
			column.setMaximumWidth(250);
		} else {
			super.applyRendererForColumn(column);
		}
	}

}
