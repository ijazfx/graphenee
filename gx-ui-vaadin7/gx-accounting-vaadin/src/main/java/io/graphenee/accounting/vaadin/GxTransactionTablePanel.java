package io.graphenee.accounting.vaadin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextArea;
import org.vaadin.viritin.fields.MTextField;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.ValoTheme;

import io.graphenee.accounting.api.GxAccountingDataService;
import io.graphenee.core.model.BeanFault;
import io.graphenee.core.model.bean.GxAccountBean;
import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxTransactionBean;
import io.graphenee.core.model.bean.GxVoucherBean;
import io.graphenee.vaadin.AbstractEntityTablePanel;
import io.graphenee.vaadin.TRAbstractForm;
import io.graphenee.vaadin.converter.BeanFaultToBeanConverter;

@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxTransactionTablePanel extends AbstractEntityTablePanel<GxTransactionBean> {

	@Autowired
	GxAccountingDataService accountingDataService;

	private GxVoucherBean voucherBean;

	private BeanItemContainer<GxAccountBean> accountBeanContainer = new BeanItemContainer<GxAccountBean>(GxAccountBean.class);

	private GxVoucherTableDelegate<GxVoucherBean> delegate = null;

	public GxTransactionTablePanel() {
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
		return new ArrayList<>(voucherBean.getGxTransactionBeanCollectionFault().getBeans());
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "gxAccountBeanFault", "description", "debit", "credit" };
	}

	@Override
	protected TRAbstractForm<GxTransactionBean> editorForm() {
		return null;
	}

	private String calculateTotalCredit() {
		return String.format("%.2f", voucherBean.getCreditTotal());
	}

	private String calculateTotalDebit() {
		return String.format("%.2f", voucherBean.getDebitTotal());
	}

	public void initializeWithEntity(GxVoucherBean bean) {
		entityTable().removeAllItems();
		this.voucherBean = bean;
		accountBeanContainer.removeAllItems();

		entityTable().setColumnFooter("debit", calculateTotalDebit());
		entityTable().setColumnFooter("credit", calculateTotalCredit());

		accountBeanContainer.addAll(accountingDataService.findAllAccountsByNamespace(voucherBean.getGxNamespaceBeanFault().getBean()));
	}

	@Override
	protected boolean isTableEditable() {
		return true;
	}

	@Override
	protected boolean isGridCellFilterEnabled() {
		return true;
	}

	@Override
	protected void postBuild() {
		setAddButtonVisibility(false);
		setEditButtonVisibility(false);
		setDeleteButtonVisibility(false);
		entityTable().setFooterVisible(true);
		entityTable().setColumnFooter("description", "Total");
	}

	@Override
	protected void addButtonsToToolbar(AbstractOrderedLayout toolbar) {
		MButton addTransaction = new MButton("Add Entry");
		addTransaction.addClickListener(clicked -> {
			GxTransactionBean transaction = new GxTransactionBean();
			transaction.setGxNamespaceBeanFault(
					new BeanFault<Integer, GxNamespaceBean>(voucherBean.getGxNamespaceBeanFault().getOid(), voucherBean.getGxNamespaceBeanFault().getBean()));
			voucherBean.getGxTransactionBeanCollectionFault().add(transaction);
			refresh();
		});

		toolbar.addComponentAsFirst(addTransaction);
	}

	@Override
	protected Field<?> propertyField(GxTransactionBean itemId, String propertyId) {
		if (propertyId.equals("gxAccountBeanFault")) {
			ComboBox cbx = new ComboBox();
			cbx.setWidth("200px");
			cbx.setRequired(true);
			cbx.setNullSelectionAllowed(false);
			cbx.setConverter(new BeanFaultToBeanConverter(GxAccountBean.class));
			cbx.setStyleName(ValoTheme.COMBOBOX_BORDERLESS);
			cbx.setContainerDataSource(accountBeanContainer);
			cbx.addValueChangeListener(listener -> {
				voucherBean.getGxTransactionBeanCollectionFault().update(itemId);
			});
			return cbx;
		}
		if (propertyId.matches("(description)")) {
			MTextArea textArea = new MTextArea().withWidth("350px");
			textArea.setRows(2);
			textArea.setRequired(true);
			textArea.addValueChangeListener(listener -> {
				voucherBean.getGxTransactionBeanCollectionFault().update(itemId);
			});
			return textArea;
		}
		if (propertyId.matches("(debit)")) {
			MTextField textField = new MTextField().withWidth("95px");
			textField.setHeight("40px");
			textField.setConverter(new StringToDoubleConverter());
			textField.addValueChangeListener(listener -> {
				voucherBean.getGxTransactionBeanCollectionFault().update(itemId);
				entityTable().setColumnFooter("debit", calculateTotalDebit());
				if (delegate != null) {
					delegate.onUpdate(voucherBean);
				}
			});
			return textField;
		}
		if (propertyId.matches("(credit)")) {
			MTextField textField = new MTextField().withWidth("95px");
			textField.setHeight("40px");
			textField.setConverter(new StringToDoubleConverter());
			textField.addValueChangeListener(listener -> {
				voucherBean.getGxTransactionBeanCollectionFault().update(itemId);
				entityTable().setColumnFooter("credit", calculateTotalCredit());
				if (delegate != null) {
					delegate.onUpdate(voucherBean);
				}
			});
			return textField;
		}

		return super.propertyField(itemId, propertyId);
	}

	@Override
	protected void applyRendererForColumn(TableColumn column) {
		String id = column.getPropertyId();
		if (id.equals("gxAccountBeanFault"))
			column.setHeader("Account");
		if (id.equals("debit") || id.equals("credit"))
			column.setAlignment(Align.RIGHT);
		super.applyRendererForColumn(column);
	}

	public static interface GxVoucherTableDelegate<GxVoucherBean> {
		default void onUpdate(GxVoucherBean entity) {

		}
	}

	public void setDelegate(GxVoucherTableDelegate<GxVoucherBean> delegate) {
		this.delegate = delegate;
	}

}
