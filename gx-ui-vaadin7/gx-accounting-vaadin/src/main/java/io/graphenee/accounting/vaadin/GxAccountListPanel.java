package io.graphenee.accounting.vaadin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.button.MButton;

import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;

import io.graphenee.accounting.api.GxAccountingDataService;
import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxAccountBean;
import io.graphenee.core.model.bean.GxAccountConfigurationBean;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.util.TRCalendarUtil;
import io.graphenee.vaadin.AbstractEntityListPanel;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.ui.GxNotification;

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

	private MButton closeYearButton;

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
		if (namespaceBean != null)
			return accountingDataService.findAllAccountsByNamespace(namespaceBean);
		return accountingDataService.findAllAccounts();
	}

	@Override
	protected <F> List<GxAccountBean> fetchEntities(F filter) {
		if (filter instanceof GxNamespaceBean) {
			return accountingDataService.findAllAccountsByNamespace((GxNamespaceBean) filter);
		}
		return super.fetchEntities(filter);
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "accountType", "accountCode", "accountName", "parentAccount" };
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
	protected void addButtonsToSecondaryToolbar(AbstractOrderedLayout toolbar) {
		namespaceComboBox = new ComboBox("Namespace");
		namespaceComboBox.setTextInputAllowed(false);
		namespaceComboBox.addItems(dataService.findNamespace());
		namespaceComboBox.addValueChangeListener(event -> {
			refresh(event.getProperty().getValue());
		});
		toolbar.addComponent(namespaceComboBox);
	}

	@Override
	protected void preEdit(GxAccountBean item) {
		if (item.getOid() == null) {
			GxNamespaceBean selectedNamespaceBean = namespaceBean != null ? namespaceBean : (GxNamespaceBean) namespaceComboBox.getValue();
			if (selectedNamespaceBean != null) {
				item.setGxNamespaceBeanFault(BeanFault.beanFault(selectedNamespaceBean.getOid(), selectedNamespaceBean));
			}
		}
	}

	public void initializeWithNamespace(GxNamespaceBean namespaceBean) {
		this.namespaceBean = namespaceBean;
		namespaceComboBox.setVisible(namespaceBean == null);
	}

	@Override
	protected void addButtonsToToolbar(AbstractOrderedLayout toolbar) {
		super.addButtonsToToolbar(toolbar);
		closeYearButton = new MButton("Close Year").withListener(event -> {
			GxAccountConfigurationBean configurationBean = accountingDataService.findAccountConfigurationByNamespace(namespaceBean);
			if (configurationBean != null) {
				if (TRCalendarUtil.getCurrentTimeStamp().after(configurationBean.getFiscalYearEnd())) {
					ConfirmDialog.show(UI.getCurrent(), "Are you sure to close a finalcial year?", e -> {
						if (e.isConfirmed()) {
							List<GxAccountBean> rows = entityGrid().getRows();
							if (!rows.isEmpty()) {
								accountingDataService.closeYear(rows, namespaceBean);
								refresh();
								entityGrid().deselectAll();
							}
						}
					});
				} else {
					GxNotification.tray("You cannot close year till " + TRCalendarUtil.getFormattedDate(configurationBean.getFiscalYearEnd()) + " .").show(Page.getCurrent());
				}
			} else {
				GxNotification.tray("Account configuration is not configure.").show(Page.getCurrent());
			}
		});

		toolbar.addComponent(closeYearButton);
	}

}
