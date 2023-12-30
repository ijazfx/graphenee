package io.graphenee.documents;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.core.model.entity.GxDocumentExplorerItem;
import io.graphenee.core.model.entity.GxDocumentFilter;
import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.jpa.repository.GxDocumentRepository;
import io.graphenee.core.model.jpa.repository.GxDocumentTypeRepository;
import io.graphenee.core.model.jpa.repository.GxFolderRepository;
import io.graphenee.util.JpaSpecificationBuilder;

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
		List<GxDocument> docs = docRepo.findByOidFolder(parent.getOid());
		return docs;
	}

	@Override
	public List<GxDocument> findDocumentVersion(GxDocument document, String... sortKey) {
		JpaSpecificationBuilder<GxDocument> sb = JpaSpecificationBuilder.get();
		sb.eq("document", document);
		List<GxDocument> versions = docRepo.findAll(sb.build(), Sort.by(sortKey).descending());
		versions.add(document);
		return versions;
	}

	@Override
	public List<GxFolder> saveFolder(GxFolder parent, List<GxFolder> folders) {
		return folderRepo.saveAll(folders);
	}

	@Override
	public List<GxDocument> saveDocument(GxFolder parent, List<GxDocument> documents) {
		return docRepo.saveAll(documents);
	}

	@Override
	public GxDocument createDocumentVersion(GxDocument parentDocument, GxDocument newDocument) {
		while (parentDocument.getDocument() != null) {
			parentDocument = parentDocument.getDocument();
		}
		Integer maxVersion = docRepo.findMaxVersionByDocument(parentDocument);
		if (maxVersion == null) {
			maxVersion = 0;
		}
		newDocument.setName(parentDocument.getName());
		newDocument.setVersionNo(maxVersion + 1);
		newDocument.setDocument(parentDocument);
		newDocument.setFolder(parentDocument.getFolder());
		return docRepo.save(newDocument);
	}

	@Override
	public void deleteFolder(List<GxFolder> folders) {
		folderRepo.deleteAllInBatch(folders);
	}

	@Override
	public void deleteDocument(List<GxDocument> documents) {
		docRepo.deleteAllInBatch(documents);
	}

	@Override
	public Long countChildren(GxDocumentExplorerItem parent, GxDocumentExplorerItem searchEntity, GxDocumentFilter filter) {
		if (parent.isFile()) {
			JpaSpecificationBuilder<GxDocument> dsb = JpaSpecificationBuilder.get();
			dsb.eq("document", parent);
			return docRepo.count(dsb.build());
		}
		Long count = 0L;

		GxFolder folder = (GxFolder) parent;

		JpaSpecificationBuilder<GxFolder> fsb = JpaSpecificationBuilder.get();
		fsb.like("name", searchEntity.getName());
		fsb.eq("folder", folder);
		count = folderRepo.count(fsb.build());

		JpaSpecificationBuilder<GxDocument> dsb = JpaSpecificationBuilder.get();
		dsb.like("name", searchEntity.getName());
		dsb.eq("folder", folder);
		List<GxDocument> docs = docRepo.findByFolder(folder.getOid());

		if (filter != null) {
			count = count + docs.stream().filter(f -> filter.test(f)).count();
		} else {
			count = count + docs.size();
		}

		return count;
	}

	@Override
	public List<GxDocumentExplorerItem> findExplorerItem(GxDocumentExplorerItem parent, GxDocumentExplorerItem searchEntity, GxDocumentFilter filter, String... sortKey) {
		List<GxDocumentExplorerItem> items = new ArrayList<>();
		if (parent.isFile()) {
			JpaSpecificationBuilder<GxDocument> dsb = JpaSpecificationBuilder.get();
			dsb.like("name", searchEntity.getName());
			dsb.eq("document", parent);
			List<GxDocument> docs = docRepo.findAll(dsb.build(), Sort.by(sortKey));
			if (filter != null) {
				docs = docs.stream().filter(f -> filter.test(f)).collect(Collectors.toList());
			}
			if (!docs.isEmpty()) {
				items.addAll(docs);
			}
		} else {
			GxFolder folder = (GxFolder) parent;
			JpaSpecificationBuilder<GxFolder> fsb = JpaSpecificationBuilder.get();
			fsb.like("name", searchEntity.getName());
			fsb.eq("folder", folder);
			List<GxFolder> folders = folderRepo.findAll(fsb.build(), Sort.by(sortKey));
			if (!folders.isEmpty()) {
				items.addAll(folders);
			}

			JpaSpecificationBuilder<GxDocument> dsb = JpaSpecificationBuilder.get();
			dsb.like("name", searchEntity.getName());
			dsb.eq("folder", folder);
			List<GxDocument> docs = docRepo.findByFolder(folder.getOid());
			if (filter != null) {
				docs = docs.stream().filter(f -> filter.test(f)).collect(Collectors.toList());
			}
			if (!docs.isEmpty()) {
				items.addAll(docs);
			}
		}
		items.sort((a, b) -> b.isFile().compareTo(a.isFile()));
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

	@Override
	public void positionBefore(List<GxDocumentExplorerItem> items, GxDocumentExplorerItem targetItem) {
		changeParent(items, targetItem.getParent());
		List<GxDocumentExplorerItem> sortList = new ArrayList<>();
		sortList.addAll(items);
		sortList.add(targetItem);
		resetSortOrder(sortList);
	}

	private void resetSortOrder(List<GxDocumentExplorerItem> sortList) {
		AtomicInteger min = new AtomicInteger(sortList.stream().mapToInt(GxDocumentExplorerItem::getSortOrder).min().orElse(999999));
		List<GxFolder> folders = new ArrayList<>();
		List<GxDocument> documents = new ArrayList<>();
		sortList.forEach(i -> {
			if (i instanceof GxDocument) {
				((GxDocument) i).setSortOrder(min.getAndIncrement());
				documents.add((GxDocument) i);
			} else if (i instanceof GxFolder) {
				((GxFolder) i).setSortOrder(min.getAndIncrement());
				folders.add((GxFolder) i);
			}
		});

		if (!documents.isEmpty()) {
			docRepo.saveAll(documents);
		}
		if (!folders.isEmpty()) {
			folderRepo.saveAll(folders);
		}
	}

	@Override
	public void positionAfter(List<GxDocumentExplorerItem> items, GxDocumentExplorerItem targetItem) {
		changeParent(items, targetItem.getParent());
		List<GxDocumentExplorerItem> sortList = new ArrayList<>();
		sortList.add(targetItem);
		sortList.addAll(items);
		resetSortOrder(sortList);
	}

	@Override
	public void changeParent(List<GxDocumentExplorerItem> items, GxDocumentExplorerItem parent) {
		List<GxFolder> folders = new ArrayList<>();
		List<GxDocument> documents = new ArrayList<>();
		GxFolder folder = (GxFolder) (parent.isFile() ? parent.getParent() : parent);
		items.stream().filter(f -> f != parent && f.getParent() != f && !isAncestorOf(f, parent)).forEach(i -> {
			if (i instanceof GxDocument) {
				((GxDocument) i).setFolder(folder);
				documents.add((GxDocument) i);
			} else if (i instanceof GxFolder) {
				((GxFolder) i).setFolder(folder);
				folders.add((GxFolder) i);
			}
		});

		if (!documents.isEmpty()) {
			docRepo.saveAll(documents);
		}
		if (!folders.isEmpty()) {
			folderRepo.saveAll(folders);
		}
	}

	private boolean isAncestorOf(GxDocumentExplorerItem node1, GxDocumentExplorerItem node2) {
		GxDocumentExplorerItem p = node2.getParent();
		while (p != null) {
			if (p.equals(node1))
				return true;
			p = p.getParent();
		}
		return false;
	}

}
