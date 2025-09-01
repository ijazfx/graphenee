package io.graphenee.documents.impl;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import io.graphenee.core.model.GxMappedSuperclass;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.core.model.entity.GxDocumentExplorerItem;
import io.graphenee.core.model.entity.GxDocumentFilter;
import io.graphenee.core.model.entity.GxFileTag;
import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.jpa.repository.GxDocumentRepository;
import io.graphenee.core.model.jpa.repository.GxDocumentTypeRepository;
import io.graphenee.core.model.jpa.repository.GxFolderRepository;
import io.graphenee.documents.GxDocumentExplorerService;
import io.graphenee.util.JpaSpecificationBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class GxDocumentExplorerServiceImpl implements GxDocumentExplorerService {

	@Autowired
	GxDocumentRepository docRepo;

	@Autowired
	GxFolderRepository folderRepo;

	@Autowired
	GxDocumentTypeRepository docTypeRepo;

	@PersistenceContext
	private EntityManager em;

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
	synchronized public GxFolder findOrCreateFolder(GxNamespace namespace, String folderName) {
		GxFolder namespaceFolder = findOrCreateNamespaceFolder(namespace);
		JpaSpecificationBuilder<GxFolder> sb = JpaSpecificationBuilder.get();
		sb.eq("namespace", namespace);
		sb.eq("name", folderName);
		List<GxFolder> folders = folderRepo.findAll(sb.build());
		if (folders.isEmpty()) {
			GxFolder folder = new GxFolder();
			folder.setNamespace(namespace);
			folder.setName(folderName);
			folder.setFolder(namespaceFolder);
			folder = folderRepo.save(folder);
			return folder;
		}
		return folders.get(0);
	}

	@Override
	synchronized public GxFolder findOrCreateFolder(GxFolder parentFolder, String folderName) {
		JpaSpecificationBuilder<GxFolder> sb = JpaSpecificationBuilder.get();
		sb.eq("namespace", parentFolder.getNamespace());
		sb.eq("folder", parentFolder);
		sb.eq("name", folderName);
		List<GxFolder> folders = folderRepo.findAll(sb.build());
		if (folders.isEmpty()) {
			GxFolder folder = new GxFolder();
			folder.setNamespace(parentFolder.getNamespace());
			folder.setName(folderName);
			folder.setFolder(parentFolder);
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

	@Transactional
	@Override
	public List<GxFolder> saveFolder(GxFolder parent, List<GxFolder> folders) {
		for (GxFolder folder : folders) {
			if (folder.getOid() == null && folder.getFileTags() != null && !folder.getFileTags().isEmpty()) {
				List<GxFileTag> managedTags = new ArrayList<>();
				for (GxFileTag tag : folder.getFileTags()) {
					if (tag.getOid() != null) {
						managedTags.add(em.merge(tag));
					} else {
						managedTags.add(tag);
					}
				}
				folder.setFileTags(managedTags);
			}
		}
		return folderRepo.saveAll(folders);
	}

	@Override
	@Transactional
	public List<GxDocument> saveDocument(GxFolder parent, List<GxDocument> documents) {
		for (GxDocument doc : documents) {
			if (doc.getOid() == null && doc.getFileTags() != null && !doc.getFileTags().isEmpty()) {
				List<GxFileTag> managedTags = new ArrayList<>();
				for (GxFileTag tag : doc.getFileTags()) {
					if (tag.getOid() != null) {
						managedTags.add(em.merge(tag));
					} else {
						managedTags.add(tag);
					}
				}
				doc.setFileTags(managedTags);
			}
		}
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
		List<Integer> tagIds = searchEntity.getFileTags().stream().map(GxFileTag::getOid).toList();
		if (parent.isFile()) {
			JpaSpecificationBuilder<GxDocument> dsb = JpaSpecificationBuilder.get();
			dsb.eq("document", parent);
			dsb.join("fileTags", "oid", tagIds);
			return docRepo.count(dsb.build());
//			List<Integer> ids = searchEntity.getFileTags().stream().map(GxFileTag::getOid).toList();
//			return docRepo.countByParentAndTag(parent.getOid(), tagIds).longValue();
		}
		Long count = 0L;

		GxFolder folder = (GxFolder) parent;

		JpaSpecificationBuilder<GxFolder> fsb = JpaSpecificationBuilder.get();
		fsb.like("name", searchEntity.getName());
		fsb.eq("folder", folder);
//		fsb.in("fileTags.oid", tagIds);
		fsb.join("fileTags", "oid", tagIds);
		count = folderRepo.count(fsb.build());
//		count = folderRepo.countByNameFolderAndTag(folder.getOid(), tagIds, searchEntity.getName()).longValue();

		JpaSpecificationBuilder<GxDocument> dsb = JpaSpecificationBuilder.get();
		dsb.like("name", searchEntity.getName());
		dsb.eq("folder", folder);
//		dsb.in("fileTags.oid", tagIds);
		dsb.join("fileTags", "oid", tagIds);
		List<GxDocument> docs = docRepo.findAll(dsb.build());
//		if (!searchEntity.getFileTags().isEmpty()) {
//			docs = docRepo.findByFolderAndTag(folder.getOid(), tagIds);
//		} else {
//			docs = docRepo.findByFolder(folder.getOid());
//		}

		if (filter != null) {
			count = count + docs.stream().filter(f -> filter.test(f)).count();
		} else {
			count = count + docs.size();
		}

		return count;
	}

	@Override
	public List<GxDocumentExplorerItem> findExplorerItem(GxDocumentExplorerItem parent, GxDocumentExplorerItem searchEntity, GxDocumentFilter filter, String... sortKey) {
		List<Integer> tagIds = searchEntity.getFileTags().stream().map(GxFileTag::getOid).toList();
		List<GxDocumentExplorerItem> items = new ArrayList<>();
		if (parent.isFile()) {
			JpaSpecificationBuilder<GxDocument> dsb = JpaSpecificationBuilder.get();
			dsb.like("name", searchEntity.getName());
			dsb.eq("document", parent);
//			dsb.in("fileTags.oid", tagIds);
			dsb.join("fileTags", "oid", tagIds);
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
//			fsb.in("fileTags.oid", tagIds);
			fsb.join("fileTags", "oid", tagIds);
			List<GxFolder> folders = folderRepo.findAll(fsb.build(), Sort.by(sortKey));
//			List<GxFolder> folders = folderRepo.findByNameFolderAndTag(folder.getOid(), tagIds, searchEntity.getName());
			if (!folders.isEmpty()) {
				items.addAll(folders);
			}

			JpaSpecificationBuilder<GxDocument> dsb = JpaSpecificationBuilder.get();
			dsb.like("name", searchEntity.getName());
			dsb.eq("folder", folder);
//			dsb.in("fileTags.oid", tagIds);
			dsb.join("fileTags", "oid", tagIds);
			List<GxDocument> docs = docRepo.findAll(dsb.build());
//			if (!searchEntity.getFileTags().isEmpty()) {
//				docs = docRepo.findByFolderAndTag(folder.getOid(), tagIds);
//			} else {
//				docs = docRepo.findByFolder(folder.getOid());
//			}
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
