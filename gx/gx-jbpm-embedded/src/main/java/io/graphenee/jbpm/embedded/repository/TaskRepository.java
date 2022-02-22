package io.graphenee.jbpm.embedded.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.graphenee.jbpm.embedded.model.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {

	List<Task> findAllByActualOwnerId(String ownerId);

	List<Task> findAllByActualOwnerIdIn(List<String> ownerIds);

	Page<Task> findPageByActualOwnerId(String ownerId, Pageable pageRequest);

	Page<Task> findPageByActualOwnerIdAndStatusIn(String ownerId, List<String> statuses, Pageable pageRequest);

	Page<Task> findPageByActualOwnerIdIn(List<String> ownerIds, Pageable pageRequest);

	Page<Task> findPageByActualOwnerIdInAndStatusIn(List<String> ownerIds, List<String> statuses, Pageable pageRequest);

	Page<Task> findPageByOrganizationalEntitiesIdInAndStatusIn(List<String> entityIds, List<String> statuses, Pageable pageRequest);

	@Query("select t from Task t where t.processInstanceId=:processInstanceId")
	List<Task> findTasksByProcessId(@Param("processInstanceId") Integer processInstanceId);

	@Query("select DISTINCT t from Task t \n" + "left join t.organizationalEntities oe \n" + "where (t.actualOwnerId in (:entityIds) or oe.id in (:entityIds) )\n"
			+ "and t.archived=0 \n" + "and t.status in (:statuses)")
	Page<Task> findOwnedAndGroupTasks(@Param("entityIds") List<String> entityIds, @Param("statuses") List<String> statuses, Pageable pageRequest);

	@Query("select DISTINCT t from Task t \n" + "left join t.organizationalEntities oe \n" + "where (t.actualOwnerId in (:entityIds) or oe.id in (:entityIds) )\n"
			+ "and t.archived=0 \n" + "and t.status in (:statuses)\n" + "and (lower(t.description) like lower(:searchText) or lower(t.subject) like lower(:searchText))")
	Page<Task> searchOwnedAndGroupTasks(@Param("entityIds") List<String> entityIds, @Param("statuses") List<String> statuses, @Param("searchText") String searchText,
			Pageable pageRequest);

	List<Task> findAllByDescriptionAndProcessId(String string, String processId);

}
