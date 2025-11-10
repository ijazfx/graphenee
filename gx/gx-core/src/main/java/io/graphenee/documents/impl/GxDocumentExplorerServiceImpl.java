package io.graphenee.documents.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.qos.logback.core.util.StringUtil;
import io.graphenee.common.GxAuthenticatedUser;
import io.graphenee.core.model.entity.GxDocument;
import io.graphenee.core.model.entity.GxDocumentExplorerItem;
import io.graphenee.core.model.entity.GxDocumentFilter;
import io.graphenee.core.model.entity.GxFolder;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxSecurityGroup;
import io.graphenee.core.model.entity.GxTag;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.core.model.jpa.repository.GxDocumentRepository;
import io.graphenee.core.model.jpa.repository.GxDocumentTypeRepository;
import io.graphenee.core.model.jpa.repository.GxFolderRepository;
import io.graphenee.core.model.jpa.repository.GxSecurityGroupRepository;
import io.graphenee.core.model.jpa.repository.GxTagRepository;
import io.graphenee.core.model.jpa.repository.GxUserAccountRepository;
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

	@Autowired
	GxTagRepository tagRepository;

	@Autowired
	GxUserAccountRepository userAccountRepo;

	@Autowired
	GxSecurityGroupRepository securityGroupRepo;

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
		folders.forEach(folder -> {
			if (folder.getTags() != null) {
				folder.setTags(folder.getTags().stream().flatMap(tag -> {
					if (tag.getOid() != null) {
						return tagRepository.findById(tag.getOid()).stream();
					}
					return java.util.stream.Stream.of(tag);
				}).collect(Collectors.toSet()));
			}
			if (folder.getGrants() != null) {
				Set<Principal> grants = folder.getGrants();
				List<Integer> oidUsers = grants.stream().filter(i -> i instanceof GxUserAccount)
						.map(i -> ((GxUserAccount) i).getOid()).toList();
				List<Integer> oidGroups = grants.stream().filter(i -> i instanceof GxSecurityGroup)
						.map(i -> ((GxSecurityGroup) i).getOid()).toList();
				List<GxUserAccount> users = userAccountRepo.findAll(oidUsers);
				List<GxSecurityGroup> groups = securityGroupRepo.findAll(oidGroups);
				folder.getUsers().clear();
				if (!users.isEmpty()) {
					folder.getUsers().addAll(users);
				}
				folder.getGroups().clear();
				if (!groups.isEmpty()) {
					folder.getGroups().addAll(groups);
				}
			}
		});
		return folderRepo.saveAll(folders);
	}

	@Override
	@Transactional
	public List<GxDocument> saveDocument(GxFolder parent, List<GxDocument> documents) {
		documents.forEach(document -> {
			if (document.getTags() != null) {
				document.setTags(document.getTags().stream().flatMap(tag -> {
					if (tag.getOid() != null) {
						return tagRepository.findById(tag.getOid()).stream();
					}
					return java.util.stream.Stream.of(tag);
				}).collect(Collectors.toSet()));
			}
			if (document.getGrants() != null) {
				Set<Principal> grants = document.getGrants();
				List<Integer> oidUsers = grants.stream().filter(i -> i instanceof GxUserAccount)
						.map(i -> ((GxUserAccount) i).getOid()).toList();
				List<Integer> oidGroups = grants.stream().filter(i -> i instanceof GxSecurityGroup)
						.map(i -> ((GxSecurityGroup) i).getOid()).toList();
				List<GxUserAccount> users = userAccountRepo.findAll(oidUsers);
				List<GxSecurityGroup> groups = securityGroupRepo.findAll(oidGroups);
				document.getUsers().clear();
				if (!users.isEmpty()) {
					document.getUsers().addAll(users);
				}
				document.getGroups().clear();
				if (!groups.isEmpty()) {
					document.getGroups().addAll(groups);
				}
			}
		});
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
	public Long countChildren(GxAuthenticatedUser user, GxDocumentExplorerItem parent,
			GxDocumentExplorerItem searchEntity,
			GxDocumentFilter filter) {
		List<Integer> tagIds = searchEntity.getTags().stream().map(GxTag::getOid).toList();
		if (parent.isFile()) {
            return 0L;
//			JpaSpecificationBuilder<GxDocument> dsb = JpaSpecificationBuilder.get();
//			dsb.eq("document", parent);
//			dsb.join("tags", "oid", tagIds);
//			return docRepo.count(dsb.build());
		}
		Long count = 0L;

		GxFolder folder = (GxFolder) parent;

		JpaSpecificationBuilder<GxFolder> fsb = JpaSpecificationBuilder.get();
		fsb.like("name", searchEntity.getName());
		fsb.eq("folder", folder);
		fsb.join("tags", "oid", tagIds);
		count = folderRepo.findAll(fsb.build()).stream().filter(f -> f.isGranted(user)).count();

		JpaSpecificationBuilder<GxDocument> dsb = JpaSpecificationBuilder.get();
		dsb.like("name", searchEntity.getName());
		dsb.eq("folder", folder);
		dsb.join("tags", "oid", tagIds);
		// find all docs, then filter for the highest version of each name.
		List<GxDocument> docs = docRepo.findAll(dsb.build()).stream().distinct().filter(f -> f.isGranted(user))
				.collect(Collectors.groupingBy(GxDocument::getName, Collectors.collectingAndThen(Collectors.maxBy(java.util.Comparator.comparing(GxDocument::getVersionNo)),
						opt -> opt.orElse(null))))
				.values().stream().filter(d -> d != null).collect(Collectors.toList());

		if (filter != null) {
			count = count + docs.stream().filter(f -> filter.test(f)).count();
		} else {
			count = count + docs.size();
		}

		return count;
	}

	@Override
	public List<GxDocumentExplorerItem> findExplorerItem(GxAuthenticatedUser user, GxDocumentExplorerItem parent,
			GxDocumentExplorerItem searchEntity, GxDocumentFilter filter, String... sortKey) {
		List<Integer> tagIds = searchEntity.getTags().stream().map(GxTag::getOid).toList();
		List<GxDocumentExplorerItem> items = new ArrayList<>();
		if (parent.isFile()) {
            return items;
//			JpaSpecificationBuilder<GxDocument> dsb = JpaSpecificationBuilder.get();
//			dsb.like("name", searchEntity.getName());
//			dsb.eq("document", parent);
//			dsb.join("tags", "oid", tagIds);
//			List<GxDocument> docs;
//			if (filter != null) {
//				docs = docRepo.findAll(dsb.build(), Sort.by(sortKey)).stream().distinct()
//						.filter(f -> f.isGranted(user) && filter.test(f)).collect(Collectors.toList());
//			} else {
//				docs = docRepo.findAll(dsb.build(), Sort.by(sortKey)).stream().distinct().filter(f -> f.isGranted(user))
//						.collect(Collectors.toList());
//			}
//			if (!docs.isEmpty()) {
//				items.addAll(docs);
//			}
		} else {
			GxFolder folder = (GxFolder) parent;
			JpaSpecificationBuilder<GxFolder> fsb = JpaSpecificationBuilder.get();
			fsb.like("name", searchEntity.getName());
			fsb.eq("folder", folder);
			fsb.join("tags", "oid", tagIds);
			List<GxFolder> folders = folderRepo.findAll(fsb.build(), Sort.by(sortKey));
			if (!folders.isEmpty()) {
				items.addAll(folders.stream().filter(f -> f.isGranted(user)).toList());
			}

			JpaSpecificationBuilder<GxDocument> dsb = JpaSpecificationBuilder.get();
			dsb.like("name", searchEntity.getName());
			dsb.eq("folder", folder);
			dsb.join("tags", "oid", tagIds);
			// find all docs, then filter for the highest version of each name.
			List<GxDocument> docs = docRepo.findAll(dsb.build()).stream().distinct().filter(f -> f.isGranted(user))
					.collect(Collectors.groupingBy(GxDocument::getName, Collectors.collectingAndThen(Collectors.maxBy(java.util.Comparator.comparing(GxDocument::getVersionNo)),
							opt -> opt.orElse(null))))
					.values().stream().filter(d -> d != null).collect(Collectors.toList());

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
	public List<GxDocumentExplorerItem> findAll(GxDocumentExplorerItem item) {
		List<GxDocumentExplorerItem> items = new ArrayList<>();

		List<Integer> tagIds = item.getTags().stream().map(GxTag::getOid).toList();

		JpaSpecificationBuilder<GxDocument> dsb = JpaSpecificationBuilder.get();
		dsb.like("name", item.getName());
		dsb.join("tags", "oid", tagIds);
		List<GxDocument> docs = docRepo.findAll(dsb.build());
		items.addAll(docs);

		JpaSpecificationBuilder<GxFolder> fsb = JpaSpecificationBuilder.get();
		if (StringUtil.notNullNorEmpty(item.getName())) {
			fsb.like("name", item.getName());
		} else {
			fsb.ne("name", "io.graphenee.system");
		}
		fsb.join("tags", "oid", tagIds);
		List<GxFolder> folders = folderRepo.findAll(fsb.build());
		items.addAll(folders);

		return items;
	}

	@Override
	public Long countAll(GxDocumentExplorerItem item) {
		List<Integer> tagIds = item.getTags().stream().map(GxTag::getOid).toList();

		JpaSpecificationBuilder<GxDocument> dsb = JpaSpecificationBuilder.get();
		dsb.like("name", item.getName());
		dsb.join("tags", "oid", tagIds);
		Long docCount = docRepo.count(dsb.build());

		JpaSpecificationBuilder<GxFolder> fsb = JpaSpecificationBuilder.get();
		if (StringUtil.notNullNorEmpty(item.getName())) {
			fsb.like("name", item.getName());
		} else {
			fsb.ne("name", "io.graphenee.system");
		}
		fsb.join("tags", "oid", tagIds);
		Long folderCount = folderRepo.count(fsb.build());
		return docCount + folderCount;
	}

	@Override
	public List<GxDocumentExplorerItem> saveExplorerItem(GxDocumentExplorerItem parent,
			List<GxDocumentExplorerItem> items) {
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
		AtomicInteger min = new AtomicInteger(
				sortList.stream().mapToInt(GxDocumentExplorerItem::getSortOrder).min().orElse(999999));
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
