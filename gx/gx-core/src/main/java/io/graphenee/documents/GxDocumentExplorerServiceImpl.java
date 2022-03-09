package io.graphenee.documents;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.core.model.entity.GxDocumentExplorerItem;
import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.jpa.repository.GxDocumentRepository;
import io.graphenee.core.model.jpa.repository.GxDocumentTypeRepository;
import io.graphenee.core.model.jpa.repository.GxFolderRepository;
import io.graphenee.util.JpaSpecificationBuilder;
import io.graphenee.util.storage.FileStorage;

@Service
public class GxDocumentExplorerServiceImpl implements GxDocumentExplorerService {

	@Autowired
	GxDocumentRepository docRepo;

	@Autowired
	GxFolderRepository folderRepo;

	@Autowired
	GxDocumentTypeRepository docTypeRepo;

	@Override
	synchronized public GxFolder findOrCreateNamespaceFolder(GxNamespace namespace) {
		JpaSpecificationBuilder<GxFolder> sb = JpaSpecificationBuilder.get();
		sb.eq("namespace", namespace);
		sb.eq("name", namespace.getNamespace());
		List<GxFolder> folders = folderRepo.findAll(sb.build());
		if (folders.isEmpty()) {
			GxFolder folder = new GxFolder();
			folder.setNamespace(namespace);
			folder.setName(namespace.getNamespace());
			folder = folderRepo.save(folder);
			return folder;
		}
		return folders.get(0);
	}

	@Override
	public List<GxFolder> findFolder(GxFolder parent, String... sortKey) {
		JpaSpecificationBuilder<GxFolder> sb = JpaSpecificationBuilder.get();
		sb.eq("folder", parent);
		return folderRepo.findAll(sb.build(), Sort.by(sortKey));
	}

	@Override
	public List<GxDocument> findDocument(GxFolder parent, String... sortKey) {
		JpaSpecificationBuilder<GxDocument> sb = JpaSpecificationBuilder.get();
		sb.eq("folder", parent);
		return docRepo.findAll(sb.build(), Sort.by(sortKey));
	}

	@Override
	public List<GxDocument> findDocumentVersion(GxDocument document, String... sortKey) {
		JpaSpecificationBuilder<GxDocument> sb = JpaSpecificationBuilder.get();
		sb.eq("document", document);
		return docRepo.findAll(sb.build(), Sort.by(sortKey));
	}

	@Override
	public List<GxFolder> saveFolder(GxFolder parent, List<GxFolder> folders) {
		JpaSpecificationBuilder<GxFolder> sb = JpaSpecificationBuilder.get();
		sb.eq("folder", parent);
		List<GxFolder> existing = folderRepo.findAll(sb.build());
		List<GxFolder> newFolders = new ArrayList<>();
		folders.forEach(f -> {
			if (!existing.contains(f)) {
				newFolders.add(f);
			}
		});
		return folderRepo.saveAll(newFolders);
	}

	@Override
	public List<GxDocument> saveDocument(GxFolder parent, List<GxDocument> documents) {
		JpaSpecificationBuilder<GxDocument> sb = JpaSpecificationBuilder.get();
		sb.eq("folder", parent);
		sb.eq("versionNo", 0);
		List<GxDocument> existing = docRepo.findAll(sb.build());
		List<GxDocument> newDocuments = new ArrayList<>();
		documents.forEach(d -> {
			if (!existing.contains(d)) {
				newDocuments.add(d);
			}
		});
		return docRepo.saveAll(newDocuments);
	}

	@Override
	public void deleteFolder(List<GxFolder> folders) {
		folderRepo.deleteInBatch(folders);
	}

	@Override
	public void deleteDocument(List<GxDocument> documents) {
		docRepo.deleteInBatch(documents);
	}

	@Override
	public GxDocument createVersion(GxDocument document, FileStorage storage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long countChildren(GxDocumentExplorerItem parent) {
		if (parent.isFile()) {
			JpaSpecificationBuilder<GxDocument> sb = JpaSpecificationBuilder.get();
			sb.eq("document", parent);
			return docRepo.count(sb.build());
		}
		Long count = 0L;
		JpaSpecificationBuilder<GxFolder> sb = JpaSpecificationBuilder.get();
		sb.eq("folder", parent);
		count = folderRepo.count(sb.build());

		JpaSpecificationBuilder<GxDocument> dsb = JpaSpecificationBuilder.get();
		dsb.eq("folder", parent);
		count = count + docRepo.count(dsb.build());

		return count;
	}

	@Override
	public List<GxDocumentExplorerItem> findExplorerItem(GxDocumentExplorerItem parent, String... sortKey) {
		List<GxDocumentExplorerItem> items = new ArrayList<>();

		if (parent.isFile()) {
			JpaSpecificationBuilder<GxDocument> dsb = JpaSpecificationBuilder.get();
			dsb.eq("document", parent);
			items.addAll(docRepo.findAll(dsb.build(), Sort.by(sortKey)));
		} else {
			JpaSpecificationBuilder<GxFolder> sb = JpaSpecificationBuilder.get();
			sb.eq("folder", parent);
			items.addAll(folderRepo.findAll(sb.build(), Sort.by(sortKey)));

			JpaSpecificationBuilder<GxDocument> dsb = JpaSpecificationBuilder.get();
			dsb.eq("folder", parent);
			items.addAll(docRepo.findAll(dsb.build(), Sort.by(sortKey)));
		}
		return items;
	}

	@Override
	public List<GxDocumentExplorerItem> saveExplorerItem(GxDocumentExplorerItem parent, List<GxDocumentExplorerItem> items) {
		if (items != null) {
			for (GxDocumentExplorerItem item : items) {
				if (item.isFile()) {
					saveDocument((GxFolder) parent, List.of((GxDocument) item));
				} else {
					saveFolder((GxFolder) parent, List.of((GxFolder) item));
				}
			}
		}
		return items;
	}

	@Override
	public void deleteExplorerItem(List<GxDocumentExplorerItem> items) {
		if (items != null) {
			for (GxDocumentExplorerItem item : items) {
				if (item.isFile()) {
					deleteDocument(List.of((GxDocument) item));
				} else {
					deleteFolder(List.of((GxFolder) item));
				}
			}
		}
	}

}
