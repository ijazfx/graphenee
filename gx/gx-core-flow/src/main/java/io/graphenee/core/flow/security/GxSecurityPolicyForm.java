package io.graphenee.core.flow.security;

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
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouteData;

import io.graphenee.core.GxDataService;
import io.graphenee.core.model.entity.GxSecurityGroup;
import io.graphenee.core.model.entity.GxSecurityPolicy;
import io.graphenee.core.model.entity.GxSecurityPolicyDocument;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.GxTabItem;

@Component
@Scope("prototype")
public class GxSecurityPolicyForm extends GxAbstractEntityForm<GxSecurityPolicy> {
	private static final long serialVersionUID = 1L;

	private TextField securityPolicyName;
	private TextField securityPolicyDescription;
	private TextField priority;
	private Checkbox isActive;
	private ComboBox<GxSecurityPolicyDocument> securityPolicyDocumentComboBox;
	private TextArea jsonDocumentTextArea;
	private TwinColGrid<GxSecurityGroup> securityGroups;
	private TwinColGrid<GxUserAccount> userAccounts;
	Tab sourceTab;
	Tab resourcesTab;
	Tabs tabs;
	List<GxStatementBean> statementBeans = new ArrayList<>();
	List<RouteData> routes = new ArrayList<>();
	ComboBox<String> resources;

	Grid<GxStatementBean> resourceTable;

	GxSecurityPolicyDocument selectedDocument;

	@Autowired
	GxDataService dataService;

	public GxSecurityPolicyForm() {
		super(GxSecurityPolicy.class);
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
			selectedDocument = new GxSecurityPolicyDocument();
			selectedDocument.setIsDefault(false);
			// selectedDocumentBean.setDocumentJson("");
			getEntity().addSecurityPolicyDocument(selectedDocument);
			securityPolicyDocumentComboBox.setItems(getEntity().getSecurityPolicyDocuments());
			securityPolicyDocumentComboBox.setValue(selectedDocument);
		});

		Button cloneButton = new Button("Clone");
		cloneButton.addClickListener(event -> {
			if (selectedDocument.getOid() != null) {
				GxSecurityPolicyDocument cloned = new GxSecurityPolicyDocument();
				cloned.setIsDefault(false);
				cloned.setDocumentJson(selectedDocument.getDocumentJson());
				selectedDocument = cloned;
				getEntity().addSecurityPolicyDocument(selectedDocument);
				securityPolicyDocumentComboBox.setValue(selectedDocument);
			}
		});

		Button deleteButton = new Button("Delete");
		deleteButton.addClickListener(event -> {
			if (selectedDocument != null) {
				getEntity().removeSecurityPolicyDocument(selectedDocument);
				securityPolicyDocumentComboBox.setItems(getEntity().getSecurityPolicyDocuments());
				securityPolicyDocumentComboBox.clear();
				jsonDocumentTextArea.clear();
			}
		});

		Button makeDefaultButton = new Button("Make Default");
		makeDefaultButton.addClickListener(event -> {
			if (selectedDocument != null) {
				List<GxSecurityPolicyDocument> documents = new ArrayList<>(getEntity().getSecurityPolicyDocuments());
				documents.forEach(bean -> {
					bean.setIsDefault(false);
				});
				selectedDocument.setIsDefault(true);
				makeDefaultButton.setEnabled(false);
			}
		});

		securityPolicyDocumentComboBox = new ComboBox<>();

		createButton.setEnabled(true);
		cloneButton.setEnabled(false);
		makeDefaultButton.setEnabled(false);
		deleteButton.setEnabled(false);

		securityPolicyDocumentComboBox.addValueChangeListener(event -> {
			selectedDocument = (GxSecurityPolicyDocument) securityPolicyDocumentComboBox.getValue();
			if (selectedDocument != null) {
				makeDefaultButton.setEnabled(!selectedDocument.getIsDefault());
				deleteButton.setEnabled(true);
				if (selectedDocument.getOid() != null) {
					cloneButton.setEnabled(true);
				} else {
					cloneButton.setEnabled(false);
				}
				jsonDocumentTextArea.setEnabled(true);
				jsonDocumentTextArea.setRequired(true);
				if (selectedDocument.getDocumentJson() != null)
					jsonDocumentTextArea.setValue(selectedDocument.getDocumentJson());
				jsonDocumentTextArea.focus();
			} else {
				makeDefaultButton.setEnabled(false);
				cloneButton.setEnabled(false);
				deleteButton.setEnabled(false);
				jsonDocumentTextArea.setEnabled(false);
			}
		});

		HorizontalLayout policyDocumentToolbar = new HorizontalLayout(securityPolicyDocumentComboBox, createButton, cloneButton, makeDefaultButton, deleteButton);
		jsonDocumentTextArea = new TextArea("Statements");
		jsonDocumentTextArea.getStyle().set("padding-top", "0px");
		jsonDocumentTextArea.setEnabled(true);
		jsonDocumentTextArea.setWidth("100%");
		jsonDocumentTextArea.setHeight("250px");
		jsonDocumentTextArea.addValueChangeListener(event -> {
			if (isEntityBound()) {
				if (selectedDocument != null) {
					selectedDocument.setDocumentJson(event.getValue());
				}
			}
		});

		resourceTable.setVisible(false);
		crudMenuBar.setVisible(false);

		VerticalLayout policyDocumentLayout = new VerticalLayout(policyDocumentToolbar, tabs, jsonDocumentTextArea, crudMenuBar, resourceTable);
		policyDocumentLayout.setWidthFull();
		policyDocumentLayout.setPadding(false);
		setColspan(policyDocumentLayout, 2);
		tabs.addSelectedChangeListener(event -> {
			if (event.getSelectedTab().equals(sourceTab)) {
				jsonDocumentTextArea.clear();
				Map<String, List<GxStatementBean>> groupedBeans = statementBeans.stream().collect(Collectors.groupingBy(sb -> sb.getPath()));
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
						jsonDocumentTextArea.setValue(jsonDocumentTextArea.getValue().concat("grant all on " + gBean.getPath() + "\n"));
						continue;
					}
					if (gBean.isDelete()) {
						jsonDocumentTextArea.setValue(jsonDocumentTextArea.getValue().concat("grant delete on " + gBean.getPath() + "\n"));
					}
					if (gBean.isEdit()) {
						jsonDocumentTextArea.setValue(jsonDocumentTextArea.getValue().concat("grant edit on " + gBean.getPath() + "\n"));
					}
					if (gBean.isView()) {
						jsonDocumentTextArea.setValue(jsonDocumentTextArea.getValue().concat("grant view on " + gBean.getPath() + "\n"));
					}
					if (gBean.isExecute()) {
						jsonDocumentTextArea.setValue(jsonDocumentTextArea.getValue().concat("grant execute on " + gBean.getPath() + "\n"));
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
						Pattern pattern = Pattern.compile("(?<permission>grant|revoke)\\s+(?<action>\\w+)\\s+on\\s+(?<resource>[\\w-]+)");

						Matcher matcher = pattern.matcher(st);
						if (matcher.find()) {
							if (!matcher.group("permission").equals("revoke")) {
								GxStatementBean gBean = new GxStatementBean();
								statementBeans.add(gBean.makeStatementBean(matcher));
							}
						}
					}
					List<GxStatementBean> mybeans = new ArrayList<>();
					Map<String, List<GxStatementBean>> groupedBeans = statementBeans.stream().collect(Collectors.groupingBy(sb -> sb.getPath()));
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

		securityGroups = new TwinColGrid<GxSecurityGroup>().addFilterableColumn(GxSecurityGroup::getSecurityGroupName, "Group Name", "Group Name", true)
				.withAvailableGridCaption("Available").withSelectionGridCaption("Selected").withDragAndDropSupport();
		securityGroups.setSizeFull();

		userAccounts = new TwinColGrid<GxUserAccount>().addFilterableColumn(GxUserAccount::getUsername, "User Name", "User Name", true).withAvailableGridCaption("Available")
				.withSelectionGridCaption("Selected").withDragAndDropSupport();
		userAccounts.setSizeFull();
	}

	@Override
	protected void preBinding(GxSecurityPolicy entity) {
		jsonDocumentTextArea.clear();
		securityGroups.setItems(dataService.findSecurityGroupByNamespaceActive(entity.getNamespace()));
		userAccounts.setItems(dataService.findUserAccountByNamespace(entity.getNamespace()));
		securityPolicyDocumentComboBox.setItems(entity.getSecurityPolicyDocuments());
		if (entity.getOid() != null && entity.defaultDocument() != null) {
			securityPolicyDocumentComboBox.setValue(entity.defaultDocument());
			jsonDocumentTextArea.setValue(entity.defaultDocument().getDocumentJson());
		}
		tabs.setSelectedTab(sourceTab);
	}

	@Override
	protected void addTabsToForm(List<GxTabItem> tabItems) {
		tabItems.add(GxTabItem.create(1, "Users", userAccounts));
		tabItems.add(GxTabItem.create(2, "Groups", securityGroups));
	}

	@Override
	protected String dialogWidth() {
		return "800px";
	}

}