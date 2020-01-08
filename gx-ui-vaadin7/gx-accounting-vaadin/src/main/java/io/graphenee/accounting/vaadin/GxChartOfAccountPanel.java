package io.graphenee.accounting.vaadin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.accounting.api.GxAccountingDataService;
import io.graphenee.core.enums.AccountType;
import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxAccountTypeBean;
import io.graphenee.core.model.bean.GxNamespaceBean;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxChartOfAccountPanel extends MVerticalLayout {

	@Autowired
	GxDataService dataService;

	@Autowired
	GxAccountingDataService accountingDataService;

	@Autowired
	GxAccountListPanel assetAccountListPanel;

	@Autowired
	GxAccountListPanel liabilityAccountListPanel;

	@Autowired
	GxAccountListPanel equityAccountListPanel;

	@Autowired
	GxAccountListPanel incomeAccountListPanel;

	@Autowired
	GxAccountListPanel expenseAccountListPanel;

	private GxNamespaceBean namespaceBean;

	public GxChartOfAccountPanel() {
		withFullHeight();
		withFullWidth();
		setSpacing(false);
		setMargin(false);
	}

	private MVerticalLayout buildPanel() {
		MVerticalLayout accountTypeLayout = new MVerticalLayout().withSpacing(false).withMargin(false);
		accountTypeLayout.setSizeFull();
		TabSheet accountTypeTabSheet = new TabSheet();
		accountTypeTabSheet.setStyleName(ValoTheme.TABSHEET_COMPACT_TABBAR);
		accountTypeTabSheet.setSizeFull();

		List<GxAccountTypeBean> accountTypeList = accountingDataService.findAllAccountTypes();
		GxAccountTypeBean assetAccountType = accountTypeList.stream().filter(at -> at.getTypeCode().equals(AccountType.ASSET.typeCode())).findFirst().get();
		GxAccountTypeBean liabilityAccountType = accountTypeList.stream().filter(at -> at.getTypeCode().equals(AccountType.LIABILITY.typeCode())).findFirst().get();
		GxAccountTypeBean equityAccountType = accountTypeList.stream().filter(at -> at.getTypeCode().equals(AccountType.EQUITY.typeCode())).findFirst().get();
		GxAccountTypeBean incomeAccountType = accountTypeList.stream().filter(at -> at.getTypeCode().equals(AccountType.INCOME.typeCode())).findFirst().get();
		GxAccountTypeBean expenseAccountType = accountTypeList.stream().filter(at -> at.getTypeCode().equals(AccountType.EXPENSE.typeCode())).findFirst().get();

		accountTypeTabSheet.addTab(assetAccountListPanel.build(), AccountType.ASSET.typeName());
		assetAccountListPanel.initializeWithEntity(namespaceBean, assetAccountType);
		assetAccountListPanel.refresh();

		accountTypeTabSheet.addTab(liabilityAccountListPanel.build(), AccountType.LIABILITY.typeName());
		liabilityAccountListPanel.initializeWithEntity(namespaceBean, liabilityAccountType);

		accountTypeTabSheet.addTab(equityAccountListPanel.build(), AccountType.EQUITY.typeName());
		equityAccountListPanel.initializeWithEntity(namespaceBean, equityAccountType);

		accountTypeTabSheet.addTab(incomeAccountListPanel.build(), AccountType.INCOME.typeName());
		incomeAccountListPanel.initializeWithEntity(namespaceBean, incomeAccountType);

		accountTypeTabSheet.addTab(expenseAccountListPanel.build(), AccountType.EXPENSE.typeName());
		expenseAccountListPanel.initializeWithEntity(namespaceBean, expenseAccountType);

		accountTypeLayout.addComponent(accountTypeTabSheet);

		accountTypeTabSheet.addSelectedTabChangeListener(cl -> {
			Component component = cl.getTabSheet().getSelectedTab();
			if (component == assetAccountListPanel)
				assetAccountListPanel.refresh();
			if (component == liabilityAccountListPanel)
				liabilityAccountListPanel.refresh();
			if (component == equityAccountListPanel)
				equityAccountListPanel.refresh();
			if (component == incomeAccountListPanel)
				incomeAccountListPanel.refresh();
			if (component == expenseAccountListPanel)
				expenseAccountListPanel.refresh();
		});

		accountTypeTabSheet.setSelectedTab(0);

		return accountTypeLayout;
	}

	public void initializeWithNamespace(GxNamespaceBean namespaceBean) {
		this.namespaceBean = namespaceBean;
		addComponent(buildPanel());
	}

}
