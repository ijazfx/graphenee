package io.graphenee.jbpm.embedded.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import io.graphenee.jbpm.embedded.model.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {

	List<Task> findAllByActualOwnerId(String ownerId);

	List<Task> findAllByActualOwnerIdIn(List<String> ownerIds);

	Page<Task> findPageByActualOwnerId(String ownerId, Pageable pageRequest);

	Page<Task> findPageByActualOwnerIdAndStatusIn(String ownerId, List<String> statuses, Pageable pageRequest);

	Page<Task> findPageByActualOwnerIdIn(List<String> ownerIds, Pageable pageRequest);

	Page<Task> findPageByActualOwnerIdInAndStatusIn(List<String> ownerIds, List<String> statuses, Pageable pageRequest);

	Page<Task> findPageByPeopleAssignmentsPotOwnersEntityIdInAndStatusIn(List<String> entityIds, List<String> statuses, Pageable pageRequest);

}
