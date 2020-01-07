package io.graphenee.accounting.vaadin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.ComboBox;

import io.graphenee.accounting.api.GxAccountingDataService;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxTransactionBean;
import io.graphenee.vaadin.AbstractEntityListPanel;
import io.graphenee.vaadin.TRAbstractForm;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxTransactionListPanel extends AbstractEntityListPanel<GxTransactionBean> {

	private GxNamespaceBean namespaceBean;

	private ComboBox namespaceComboBox;

	@Autowired
	GxDataService dataService;

	@Autowired
	GxAccountingDataService accountingDataService;

	public GxTransactionListPanel() {
		super(GxTransactionBean.class);
	}

	@Override
	protected boolean onSaveEntity(GxTransactionBean entity) {
		return false;
	}

	@Override
	protected boolean onDeleteEntity(GxTransactionBean entity) {
		return false;
	}

	@Override
	protected String panelCaption() {
		return null;
	}

	@Override
	protected List<GxTransactionBean> fetchEntities() {
		if (namespaceBean != null)
			return accountingDataService.findAllTransactionsByNamespaceOrderByDateAsc(namespaceBean);
		return accountingDataService.findAllTransactionsOrderByDateAsc();
	}

	@Override
	protected <F> List<GxTransactionBean> fetchEntities(F filter) {
		if (filter instanceof GxNamespaceBean) {
			return accountingDataService.findAllTransactionsByNamespaceOrderByDateAsc((GxNamespaceBean) filter);
		}
		return super.fetchEntities(filter);
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "transactionDate", "accountName", "description", "debit", "credit" };
	}

	@Override
	protected TRAbstractForm<GxTransactionBean> editorForm() {
		return null;
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
	protected void addButtonsToSecondaryToolbar(AbstractOrderedLayout toolbar) {
		namespaceComboBox = new ComboBox("Namespace");
		namespaceComboBox.setTextInputAllowed(false);
		namespaceComboBox.addItems(dataService.findNamespace());
		namespaceComboBox.addValueChangeListener(event -> {
			refresh(event.getProperty().getValue());
		});
		toolbar.addComponent(namespaceComboBox);
	}

	public void initializeWithNamespace(GxNamespaceBean namespaceBean) {
		this.namespaceBean = namespaceBean;
		namespaceComboBox.setVisible(namespaceBean == null);
	}

	@Override
	protected void postBuild() {
		super.postBuild();
		setAddButtonVisibility(false);
		setEditButtonVisibility(false);
		setDeleteButtonVisibility(false);
		hideSecondaryToolbar();
	}

}
