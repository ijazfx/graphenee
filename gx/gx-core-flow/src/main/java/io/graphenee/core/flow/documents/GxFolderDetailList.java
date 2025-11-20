package io.graphenee.core.flow.documents;

import java.util.Collection;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.core.model.entity.GxDocumentExplorerItem;
import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.documents.GxDocumentExplorerService;
import io.graphenee.util.storage.FileStorage;
import io.graphenee.vaadin.flow.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.GxAbstractEntityList;
import io.graphenee.vaadin.flow.component.ResourcePreviewPanel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("serial")
@SpringComponent
@Scope("prototype")
public class GxFolderDetailList extends GxAbstractEntityList<GxDocumentExplorerItem> {

	@Autowired
	GxDocumentExplorerService documentService;

	@Autowired
	GxDocumentExplorerItemForm form;

	@Autowired
	FileStorage storage;

	private GxFolder selectedFolder;

	public GxFolderDetailList() {
		super(GxDocumentExplorerItem.class);
	}

	@Override
	protected Stream<GxDocumentExplorerItem> getData() {
		GxUserAccount user = (loggedInUser() instanceof GxUserAccount) ? ((GxUserAccount) loggedInUser()) : null;
		return documentService.findFolderItems(user, selectedFolder).stream();
	}

	@Override
	protected String[] visibleProperties() {
		return new String[] { "extension", "name", "size", "version" };
	}

	@Override
	protected void preEdit(GxDocumentExplorerItem entity) {
		setEditable(false);
	}

	@Override
	protected void postBuild() {
		setEditable(false);
	}

	@Override
	protected void decorateColumn(String propertyName, Column<GxDocumentExplorerItem> column) {
		if (propertyName.equals("extension")) {
			column.setHeader("");
			column.setAutoWidth(false);
			column.setWidth("50px");
			column.setTextAlign(ColumnTextAlign.CENTER);
		}
		if (propertyName.equals("name")) {
			column.setAutoWidth(false);
			column.setWidth("350px");
		}
	}

	@Override
	protected Renderer<GxDocumentExplorerItem> rendererForProperty(String propertyName,
			PropertyDefinition<GxDocumentExplorerItem, ?> propertyDefinition) {
		if (propertyName.equals("extension")) {
			return new ComponentRenderer<>(s -> {
				Icon icon = null;
				if (!s.isFile()) {
					icon = VaadinIcon.FOLDER_O.create();
				} else {
					icon = VaadinIcon.FILE_O.create();
					icon.addClickListener(cl -> {
						GxDocument document = (GxDocument) s;
						String extension = s.getExtension();
						String mimeType = s.getMimeType();
						if (mimeType.startsWith("image") || extension.equals("pdf") || mimeType.startsWith("audio")
								|| mimeType.startsWith("video")) {
							try {
								String src = document.getPath();
								String resourcePath = storage.resourcePath("documents", src);
								ResourcePreviewPanel resourcePreviewPanel = new ResourcePreviewPanel(document.getName(),
										() -> {
											try {
												return storage.resolve(resourcePath);
											} catch (Exception ex) {
												return null;
											}
										});
								resourcePreviewPanel.showInDialog();
							} catch (Exception e) {
								log.error("Failed to resolve file from storage", e);
							}

						}
					});
				}
				return icon;
			});
		}
		return super.rendererForProperty(propertyName, propertyDefinition);
	}

	@Override
	protected GxAbstractEntityForm<GxDocumentExplorerItem> getEntityForm(GxDocumentExplorerItem entity) {
		form.setEditable(false);
		return form;
	}

	@Override
	protected void onSave(GxDocumentExplorerItem entity) {
	}

	@Override
	protected void onDelete(Collection<GxDocumentExplorerItem> entities) {
	}

	public void initializeWithFolder(GxFolder folder) {
		this.selectedFolder = folder;
		refresh();
	}

}
