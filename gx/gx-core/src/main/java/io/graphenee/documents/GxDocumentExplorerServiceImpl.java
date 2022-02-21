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
import io.graphenee.core.storage.FileStorage;
import io.graphenee.core.util.JpaSpecificationBuilder;

@Service
public class GxDocumentExplorerServiceImpl implements GxDocumentExplorerService {

	@Autowired
	GxDocumentRepository docRepo;

	@Autowired
	GxFolderRepository folderRepo;

	@Autowired
	GxDocumentTypeRepository docTypeRepo;

	@Override
	public List<GxFolder> findFolder(GxNamespace namespace, String... sortKey) {
		JpaSpecificationBuilder<GxFolder> sb = JpaSpecificationBuilder.get();
		sb.isNull("folder");
		return folderRepo.findAll(sb.build(), Sort.by(sortKey));
	}

	@Override
	public List<GxFolder> findFolder(GxFolder parent, String... sortKey) {
		JpaSpecificationBuilder<GxFolder> sb = JpaSpecificationBuilder.get();
		sb.eq("folder", parent);
		return folderRepo.findAll(sb.build(), Sort.by(sortKey));
	}

	@Override
	public List<GxDocument> findDocument(GxNamespace namespace, String... sortKey) {
		JpaSpecificationBuilder<GxDocument> sb = JpaSpecificationBuilder.get();
		sb.isNull("folder");
		return docRepo.findAll(sb.build(), Sort.by(sortKey));
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
	public List<GxFolder> saveFolder(List<GxFolder> folders) {
		return folderRepo.saveAll(folders);
	}

	@Override
	public List<GxDocument> saveDocument(List<GxDocument> documents) {
		return docRepo.saveAll(documents);
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
	public Long countFolder(GxNamespace namespace) {
		JpaSpecificationBuilder<GxFolder> sb = JpaSpecificationBuilder.get();
		sb.isNull("folder");
		return folderRepo.count(sb.build());
	}

	@Override
	public Long countDocuments(GxNamespace namespace) {
		JpaSpecificationBuilder<GxDocument> sb = JpaSpecificationBuilder.get();
		sb.isNull("folder");
		return docRepo.count(sb.build());
	}

	@Override
	public Long countChildren(GxNamespace namespace) {
		JpaSpecificationBuilder<GxFolder> sb = JpaSpecificationBuilder.get();
		sb.isNull("folder");
		return folderRepo.count(sb.build());
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
	public List<GxDocumentExplorerItem> findExplorerItem(GxNamespace namespace, String... sortKey) {
		List<GxDocumentExplorerItem> items = new ArrayList<>();

		JpaSpecificationBuilder<GxFolder> sb = JpaSpecificationBuilder.get();
		sb.isNull("folder");
		items.addAll(folderRepo.findAll(sb.build(), Sort.by(sortKey)));

		JpaSpecificationBuilder<GxDocument> dsb = JpaSpecificationBuilder.get();
		dsb.isNull("folder");
		items.addAll(docRepo.findAll(dsb.build(), Sort.by(sortKey)));

		return items;
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
	public List<GxDocumentExplorerItem> saveExplorerItem(List<GxDocumentExplorerItem> items) {
		if (items != null) {
			for (GxDocumentExplorerItem item : items) {
				if (item.isFile()) {
					saveDocument(List.of((GxDocument) item));
				} else {
					saveFolder(List.of((GxFolder) item));
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
