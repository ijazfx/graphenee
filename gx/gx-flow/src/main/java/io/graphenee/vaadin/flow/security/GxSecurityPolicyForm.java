package io.graphenee.vaadin.flow.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.flowingcode.vaadin.addons.twincolgrid.TwinColGrid;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouteData;

import io.graphenee.core.model.api.GxDataService;
import io.graphenee.core.model.bean.GxSecurityGroupBean;
import io.graphenee.core.model.bean.GxSecurityPolicyBean;
import io.graphenee.core.model.bean.GxSecurityPolicyDocumentBean;
import io.graphenee.core.model.bean.GxUserAccountBean;
import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.base.GxTabItem;
import io.graphenee.vaadin.flow.converter.BeanCollectionFaultToSetConverter;

@Component
@Scope("prototype")
public class GxSecurityPolicyForm extends GxAbstractEntityForm<GxSecurityPolicyBean> {
	private static final long serialVersionUID = 1L;

	private TextField securityPolicyName;
	private TextField securityPolicyDescription;
	private TextField priority;
	private Checkbox isActive;
	private ComboBox<GxSecurityPolicyDocumentBean> securityPolicyDocumentComboBox;
	private TextArea jsonDocumentTextArea;
	private TwinColGrid<GxSecurityGroupBean> securityGroupCollectionFault;
	private TwinColGrid<GxUserAccountBean> userAccountCollectionFault;
	Tab sourceTab;
	Tab resourcesTab;
	Tabs tabs;
	List<GxStatementBean> statementBeans = new ArrayList<>();
	List<RouteData> routes = new ArrayList<>();
	ComboBox<String> resources;

	Grid<GxStatementBean> resourceTable;

	GxSecurityPolicyDocumentBean selectedDocumentBean;

	@Autowired
	GxDataService gxDataService;

	public GxSecurityPolicyForm() {
		super(GxSecurityPolicyBean.class);
	}

	@Override
	protected void decorateForm(HasComponents entityForm) {

		routes = RouteConfiguration.forSessionScope().getAvailableRoutes();
		List<String> paths = new ArrayList<>();
		paths.add("all");
		routes.forEach(r -> {
			if (!r.getTemplate().isEmpty()) {
				paths.add(r.getTemplate());
			}
		});
		sourceTab = new Tab("Source");
		resourcesTab = new Tab("Resources");
		tabs = new Tabs(sourceTab, resourcesTab);
		resourceTable = new Grid<>(GxStatementBean.class, false);
		resourceTable.setSelectionMode(SelectionMode.MULTI);

		MenuBar crudMenuBar = new MenuBar();
		crudMenuBar.addThemeVariants(MenuBarVariant.LUMO_ICON);

		MenuItem addMenuItem = crudMenuBar.addItem(VaadinIcon.PLUS.create());
		addMenuItem.addClickListener(cl -> {
			GxStatementBean bean = new GxStatementBean();
			bean.setPath("all");
			statementBeans.add(bean);
			resourceTable.setItems(statementBeans);
		});
		MenuItem deleteMenuItem = crudMenuBar.addItem(VaadinIcon.TRASH.create());
		deleteMenuItem.addClickListener(cl -> {
			statementBeans.removeAll(resourceTable.getSelectedItems());
			resourceTable.setItems(statementBeans);
		});

		tabs.setWidthFull();

		resourceTable.addSelectionListener(l -> {
			deleteMenuItem.setEnabled(l.getAllSelectedItems().size() > 0);
		});

		resourceTable.addColumn(new ComponentRenderer<>(f -> {
			resources = new ComboBox<>();
			resources.setAllowCustomValue(true);
			resources.setItems(paths);
			resources.setValue(f.getPath());
			resources.setWidth("160px");
			resources.addValueChangeListener(event -> {
				f.setPath(event.getValue());
			});
			resources.addCustomValueSetListener(cvl -> {
				paths.add(cvl.getDetail());
				resources.setItems(paths);
				resources.setValue(cvl.getDetail());
			});
			return resources;
		})).setHeader("Resource").setWidth("170px").setAutoWidth(false).setResizable(false).setFlexGrow(0);

		resourceTable.addColumn(new ComponentRenderer<>(f -> {
			Checkbox checkbox = new Checkbox();
			checkbox.setValue(f.isAll());
			checkbox.addValueChangeListener(event -> {
				if (event.getValue()) {
					f.setAll(true);
					f.setDelete(true);
					f.setEdit(true);
					f.setView(true);
					f.setExecute(true);
				} else {
					f.setAll(false);
					f.setDelete(false);
					f.setEdit(false);
					f.setView(false);
					f.setExecute(false);
				}
				resourceTable.getDataProvider().refreshAll();
			});
			return checkbox;
		})).setHeader("All").setWidth("100px").setAutoWidth(false).setResizable(false).setFlexGrow(0);

		resourceTable.addColumn(new ComponentRenderer<>(f -> {
			Checkbox checkbox = new Checkbox();
			checkbox.setValue(f.isView());
			checkbox.addValueChangeListener(event -> {
				if (event.getValue()) {
					if (f.isEdit() && f.isDelete() && f.isExecute()) {
						f.setAll(true);
					}
					f.setView(true);
				} else {
					if (f.isAll()) {
						f.setAll(false);
					}
					f.setView(false);
				}
				resourceTable.getDataProvider().refreshAll();
			});
			return checkbox;
		})).setHeader("View").setWidth("100px").setAutoWidth(false).setResizable(false).setFlexGrow(0);
		resourceTable.addColumn(new ComponentRenderer<>(f -> {
			Checkbox checkbox = new Checkbox();
			checkbox.setValue(f.isEdit());
			checkbox.addValueChangeListener(event -> {
				if (event.getValue()) {
					if (f.isView() && f.isDelete() && f.isExecute()) {
						f.setAll(true);
					}
					f.setEdit(true);
				} else {
					if (f.isAll()) {
						f.setAll(false);
					}
					f.setEdit(false);
				}
				resourceTable.getDataProvider().refreshAll();
			});
			return checkbox;
		})).setHeader("Edit").setWidth("100px").setAutoWidth(false).setResizable(false).setFlexGrow(0);
		resourceTable.addColumn(new ComponentRenderer<>(f -> {
			Checkbox checkbox = new Checkbox();
			checkbox.setValue(f.isDelete());
			checkbox.addValueChangeListener(event -> {
				if (event.getValue()) {
					if (f.isView() && f.isEdit() && f.isExecute()) {
						f.setAll(true);
					}
					f.setDelete(true);
				} else {
					if (f.isAll()) {
						f.setAll(false);
					}
					f.setDelete(false);
				}
				resourceTable.getDataProvider().refreshAll();
			});
			return checkbox;
		})).setHeader("Delete").setWidth("100px").setAutoWidth(false).setResizable(false).setFlexGrow(0);
		resourceTable.addColumn(new ComponentRenderer<>(f -> {
			Checkbox checkbox = new Checkbox();
			checkbox.setValue(f.isExecute());
			checkbox.addValueChangeListener(event -> {
				if (event.getValue()) {
					if (f.isView() && f.isEdit() && f.isDelete()) {
						f.setAll(true);
					}
					f.setExecute(true);
				} else {
					if (f.isAll()) {
						f.setAll(false);
					}
					f.setExecute(false);
				}
				resourceTable.getDataProvider().refreshAll();
			});
			return checkbox;
		})).setHeader("Execute").setWidth("100px").setAutoWidth(false).setResizable(false).setFlexGrow(0);

		securityPolicyName = new TextField("Policy Name");
		securityPolicyDescription = new TextField("Policy Description");
		priority = new TextField("Priority");
		isActive = new Checkbox("Is Active?");

		Button createButton = new Button("Create");
		createButton.addClickListener(event -> {
			selectedDocumentBean = new GxSecurityPolicyDocumentBean();
			selectedDocumentBean.setIsDefault(false);
			// selectedDocumentBean.setDocumentJson("");
			getEntity().getSecurityPolicyDocumentCollectionFault().add(selectedDocumentBean);
			securityPolicyDocumentComboBox.setItems(getEntity().getSecurityPolicyDocumentCollectionFault().getBeans());
			securityPolicyDocumentComboBox.setValue(selectedDocumentBean);
		});

		Button cloneButton = new Button("Clone");
		cloneButton.addClickListener(event -> {
			if (selectedDocumentBean.getOid() != null) {
				GxSecurityPolicyDocumentBean cloned = new GxSecurityPolicyDocumentBean();
				cloned.setIsDefault(false);
				cloned.setDocumentJson(selectedDocumentBean.getDocumentJson());
				selectedDocumentBean = cloned;
				getEntity().getSecurityPolicyDocumentCollectionFault().add(selectedDocumentBean);
				securityPolicyDocumentComboBox.setValue(selectedDocumentBean);
			}
		});

		Button deleteButton = new Button("Delete");
		deleteButton.addClickListener(event -> {
			if (selectedDocumentBean != null) {
				getEntity().getSecurityPolicyDocumentCollectionFault().remove(selectedDocumentBean);
				securityPolicyDocumentComboBox
						.setItems(getEntity().getSecurityPolicyDocumentCollectionFault().getBeans());
				securityPolicyDocumentComboBox.clear();
				jsonDocumentTextArea.clear();
			}
		});

		Button makeDefaultButton = new Button("Make Default");
		makeDefaultButton.addClickListener(event -> {
			if (selectedDocumentBean != null) {
				List<GxSecurityPolicyDocumentBean> documents = new ArrayList<>(
						getEntity().getSecurityPolicyDocumentCollectionFault().getBeans());
				documents.forEach(bean -> {
					bean.setIsDefault(false);
					getEntity().getSecurityPolicyDocumentCollectionFault().update(bean);
				});
				selectedDocumentBean.setIsDefault(true);
				getEntity().getSecurityPolicyDocumentCollectionFault().update(selectedDocumentBean);
				makeDefaultButton.setEnabled(false);
			}
		});

		securityPolicyDocumentComboBox = new ComboBox<GxSecurityPolicyDocumentBean>();

		createButton.setEnabled(true);
		cloneButton.setEnabled(false);
		makeDefaultButton.setEnabled(false);
		deleteButton.setEnabled(false);

		securityPolicyDocumentComboBox.addValueChangeListener(event -> {
			selectedDocumentBean = (GxSecurityPolicyDocumentBean) securityPolicyDocumentComboBox.getValue();
			if (selectedDocumentBean != null) {
				makeDefaultButton.setEnabled(!selectedDocumentBean.getIsDefault());
				deleteButton.setEnabled(true);
				if (selectedDocumentBean.getOid() != null) {
					cloneButton.setEnabled(true);
				} else {
					cloneButton.setEnabled(false);
				}
				jsonDocumentTextArea.setEnabled(true);
				jsonDocumentTextArea.setRequired(true);
				if (selectedDocumentBean.getDocumentJson() != null)
					jsonDocumentTextArea.setValue(selectedDocumentBean.getDocumentJson());
				jsonDocumentTextArea.focus();
			} else {
				makeDefaultButton.setEnabled(false);
				cloneButton.setEnabled(false);
				deleteButton.setEnabled(false);
				jsonDocumentTextArea.setEnabled(false);
			}
		});

		HorizontalLayout policyDocumentToolbar = new HorizontalLayout(securityPolicyDocumentComboBox, createButton,
				cloneButton, makeDefaultButton, deleteButton);
		jsonDocumentTextArea = new TextArea("Statements");
		jsonDocumentTextArea.getStyle().set("padding-top", "0px");
		jsonDocumentTextArea.setEnabled(true);
		jsonDocumentTextArea.setWidth("100%");
		jsonDocumentTextArea.setHeight("250px");
		jsonDocumentTextArea.addValueChangeListener(event -> {
			if (isEntityBound()) {
				if (selectedDocumentBean != null) {
					selectedDocumentBean.setDocumentJson(event.getValue());
					getEntity().getSecurityPolicyDocumentCollectionFault().update(selectedDocumentBean);
				}
			}
		});

		resourceTable.setVisible(false);
		crudMenuBar.setVisible(false);

		VerticalLayout policyDocumentLayout = new VerticalLayout(policyDocumentToolbar, tabs, jsonDocumentTextArea,
				crudMenuBar, resourceTable);
		policyDocumentLayout.setWidthFull();
		policyDocumentLayout.setPadding(false);
		setColspan(policyDocumentLayout, 2);
		tabs.addSelectedChangeListener(event -> {
			if (event.getSelectedTab().equals(sourceTab)) {
				jsonDocumentTextArea.clear();
				Map<String, List<GxStatementBean>> groupedBeans = statementBeans.stream()
						.collect(Collectors.groupingBy(sb -> sb.getPath()));
				List<String> keys = groupedBeans.keySet().stream().collect(Collectors.toList());
				statementBeans.clear();
				jsonDocumentTextArea.setValue(jsonDocumentTextArea.getValue().concat("revoke all on all\n"));
				for (String key : keys) {
					List<GxStatementBean> beans = new ArrayList<>();
					beans = groupedBeans.get(key);
					GxStatementBean gBean = new GxStatementBean();
					gBean.setPath(key);
					beans.forEach(b -> {
						gBean.setAll(gBean.isAll() ? true : b.isAll());
						gBean.setView(gBean.isView() ? true : b.isView());
						gBean.setEdit(gBean.isEdit() ? true : b.isEdit());
						gBean.setDelete(gBean.isDelete() ? true : b.isDelete());
						gBean.setExecute(gBean.isExecute() ? true : b.isExecute());
					});
					statementBeans.add(gBean);
					if (gBean.isAll()) {
						jsonDocumentTextArea.setValue(
								jsonDocumentTextArea.getValue().concat("grant all on " + gBean.getPath() + "\n"));
						continue;
					}
					if (gBean.isDelete()) {
						jsonDocumentTextArea.setValue(
								jsonDocumentTextArea.getValue().concat("grant delete on " + gBean.getPath() + "\n"));
					}
					if (gBean.isEdit()) {
						jsonDocumentTextArea.setValue(
								jsonDocumentTextArea.getValue().concat("grant edit on " + gBean.getPath() + "\n"));
					}
					if (gBean.isView()) {
						jsonDocumentTextArea.setValue(
								jsonDocumentTextArea.getValue().concat("grant view on " + gBean.getPath() + "\n"));
					}
					if (gBean.isExecute()) {
						jsonDocumentTextArea.setValue(
								jsonDocumentTextArea.getValue().concat("grant execute on " + gBean.getPath() + "\n"));
					}
				}
				jsonDocumentTextArea.setVisible(true);
				if (resourceTable.isVisible()) {
					resourceTable.setVisible(false);
					crudMenuBar.setVisible(false);
				}
			} else {

				if (!jsonDocumentTextArea.getValue().isEmpty()) {
					String[] statements = jsonDocumentTextArea.getValue().split("\n", 100);
					statementBeans.clear();
					resourceTable.getDataProvider().refreshAll();
					for (String st : statements) {
						Pattern pattern = Pattern.compile(
								"(?<permission>grant|revoke)\\s+(?<action>\\w+)\\s+on\\s+(?<resource>[\\w-]+)");

						Matcher matcher = pattern.matcher(st);
						if (matcher.find()) {
							if (!matcher.group("permission").equals("revoke")) {
								GxStatementBean gBean = new GxStatementBean();
								statementBeans.add(gBean.makeStatementBean(matcher));
							}
						}
					}
					List<GxStatementBean> mybeans = new ArrayList<>();
					Map<String, List<GxStatementBean>> groupedBeans = statementBeans.stream()
							.collect(Collectors.groupingBy(sb -> sb.getPath()));
					List<String> keys = groupedBeans.keySet().stream().collect(Collectors.toList());
					for (String key : keys) {
						List<GxStatementBean> beans = new ArrayList<>();
						beans = groupedBeans.get(key);
						GxStatementBean gBean = new GxStatementBean();
						gBean.setPath(key);
						beans.forEach(b -> {
							gBean.setAll(gBean.isAll() ? true : b.isAll());
							gBean.setView(gBean.isView() ? true : b.isView());
							gBean.setEdit(gBean.isEdit() ? true : b.isEdit());
							gBean.setDelete(gBean.isDelete() ? true : b.isDelete());
							gBean.setExecute(gBean.isExecute() ? true : b.isExecute());
						});
						mybeans.add(gBean);
					}
					statementBeans = mybeans;
					resourceTable.setItems(statementBeans);
				} else {
					statementBeans.clear();
					resourceTable.setItems(statementBeans);
					resourceTable.getDataProvider().refreshAll();
				}
				jsonDocumentTextArea.setVisible(false);
				resourceTable.setVisible(true);
				crudMenuBar.setVisible(true);
			}
		});
		entityForm.add(securityPolicyName, securityPolicyDescription, priority, isActive, policyDocumentLayout);

		securityGroupCollectionFault = new TwinColGrid<GxSecurityGroupBean>()
				.addFilterableColumn(GxSecurityGroupBean::getSecurityGroupName, "Group Name", "Group Name", true)
				.withLeftColumnCaption("Available").withRightColumnCaption("Selected");
		securityGroupCollectionFault.setSizeFull();

		userAccountCollectionFault = new TwinColGrid<GxUserAccountBean>()
				.addFilterableColumn(GxUserAccountBean::getUsername, "User Name", "User Name", true)
				.withLeftColumnCaption("Available").withRightColumnCaption("Selected");
		userAccountCollectionFault.setSizeFull();
	}

	@Override
	protected void preBinding(GxSecurityPolicyBean entity) {
		jsonDocumentTextArea.clear();
		securityGroupCollectionFault
				.setItems(gxDataService.findSecurityGroupByNamespaceActive(entity.getNamespaceFault().getBean()));
		userAccountCollectionFault
				.setItems(gxDataService.findUserAccountByNamespace(entity.getNamespaceFault().getBean()));
		securityPolicyDocumentComboBox.setItems(entity.getSecurityPolicyDocumentCollectionFault().getBeans());
		if (entity.getOid() != null && entity.getDefaultSecurityPolicyDocumentBean() != null) {
			securityPolicyDocumentComboBox.setValue(entity.getDefaultSecurityPolicyDocumentBean());
			jsonDocumentTextArea.setValue(entity.getDefaultSecurityPolicyDocumentBean().getDocumentJson());
		}
		tabs.setSelectedTab(sourceTab);
	}

	@Override
	protected void addTabsToForm(List<GxTabItem> tabItems) {
		tabItems.add(GxTabItem.create(1, "Users", userAccountCollectionFault));
		tabItems.add(GxTabItem.create(2, "Groups", securityGroupCollectionFault));
	}

	@Override
	protected String dialogWidth() {
		return "800px";
	}

}