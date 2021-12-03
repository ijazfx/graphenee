package io.graphenee.jbpm.embedded.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;

/**
 * The persistent class for the Task database table.
 * 
 */
@Entity
@NamedQuery(name = "Task.findAll", query = "SELECT t FROM Task t")
public class Task implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	private Timestamp activationTime;

	@Column(name = "actualOwner_id")
	private String actualOwnerId;

	private String allowedToDelegate;

	private Integer archived;

	private String createdBy_id;

	private Timestamp createdOn;

	private String deploymentId;

	private String description;

	private Integer documentAccessType;

	private Integer documentContentId;

	private String documentType;

	private Timestamp expirationTime;

	private Integer faultAccessType;

	private Integer faultContentId;

	private String faultName;

	private String faultType;

	private String formName;

	private String name;

	@Column(name = "OPTLOCK")
	private Integer optlock;

	private Integer outputAccessType;

	private Integer outputContentId;

	private String outputType;

	private Integer parentId;

	private Integer previousStatus;

	private Integer priority;

	private String processId;

	private Integer processInstanceId;

	private Integer processSessionId;

	private Boolean skipable;

	private String status;

	private String subject;

	private String subTaskStrategy;

	private String taskInitiator_id;

	private String taskType;

	private Integer workItemId;


	//bi-directional many-to-many association to OrganizationalEntity
	@ManyToMany
	@JoinTable(
		name="PeopleAssignments_PotOwners"
		, joinColumns={
			@JoinColumn(name="task_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="entity_id")
			}
		)
	private List<OrganizationalEntity> organizationalEntities;

	public Task() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Timestamp getActivationTime() {
		return this.activationTime;
	}

	public void setActivationTime(Timestamp activationTime) {
		this.activationTime = activationTime;
	}

	public String getAllowedToDelegate() {
		return this.allowedToDelegate;
	}

	public void setAllowedToDelegate(String allowedToDelegate) {
		this.allowedToDelegate = allowedToDelegate;
	}

	public Integer getArchived() {
		return this.archived;
	}

	public void setArchived(Integer archived) {
		this.archived = archived;
	}

	public String getCreatedBy_id() {
		return this.createdBy_id;
	}

	public void setCreatedBy_id(String createdBy_id) {
		this.createdBy_id = createdBy_id;
	}

	public Timestamp getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public String getDeploymentId() {
		return this.deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDocumentAccessType() {
		return this.documentAccessType;
	}

	public void setDocumentAccessType(Integer documentAccessType) {
		this.documentAccessType = documentAccessType;
	}

	public Integer getDocumentContentId() {
		return this.documentContentId;
	}

	public void setDocumentContentId(Integer documentContentId) {
		this.documentContentId = documentContentId;
	}

	public String getDocumentType() {
		return this.documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public Timestamp getExpirationTime() {
		return this.expirationTime;
	}

	public void setExpirationTime(Timestamp expirationTime) {
		this.expirationTime = expirationTime;
	}

	public Integer getFaultAccessType() {
		return this.faultAccessType;
	}

	public void setFaultAccessType(Integer faultAccessType) {
		this.faultAccessType = faultAccessType;
	}

	public Integer getFaultContentId() {
		return this.faultContentId;
	}

	public void setFaultContentId(Integer faultContentId) {
		this.faultContentId = faultContentId;
	}

	public String getFaultName() {
		return this.faultName;
	}

	public void setFaultName(String faultName) {
		this.faultName = faultName;
	}

	public String getFaultType() {
		return this.faultType;
	}

	public void setFaultType(String faultType) {
		this.faultType = faultType;
	}

	public String getFormName() {
		return this.formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOptlock() {
		return this.optlock;
	}

	public void setOptlock(Integer optlock) {
		this.optlock = optlock;
	}

	public Integer getOutputAccessType() {
		return this.outputAccessType;
	}

	public void setOutputAccessType(Integer outputAccessType) {
		this.outputAccessType = outputAccessType;
	}

	public Integer getOutputContentId() {
		return this.outputContentId;
	}

	public void setOutputContentId(Integer outputContentId) {
		this.outputContentId = outputContentId;
	}

	public String getOutputType() {
		return this.outputType;
	}

	public void setOutputType(String outputType) {
		this.outputType = outputType;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getPreviousStatus() {
		return this.previousStatus;
	}

	public void setPreviousStatus(Integer previousStatus) {
		this.previousStatus = previousStatus;
	}

	public Integer getPriority() {
		return this.priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getProcessId() {
		return this.processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public Integer getProcessInstanceId() {
		return this.processInstanceId;
	}

	public void setProcessInstanceId(Integer processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Integer getProcessSessionId() {
		return this.processSessionId;
	}

	public void setProcessSessionId(Integer processSessionId) {
		this.processSessionId = processSessionId;
	}

	public Boolean getSkipable() {
		return this.skipable;
	}

	public void setSkipable(Boolean skipable) {
		this.skipable = skipable;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubTaskStrategy() {
		return this.subTaskStrategy;
	}

	public void setSubTaskStrategy(String subTaskStrategy) {
		this.subTaskStrategy = subTaskStrategy;
	}

	public String getTaskInitiator_id() {
		return this.taskInitiator_id;
	}

	public void setTaskInitiator_id(String taskInitiator_id) {
		this.taskInitiator_id = taskInitiator_id;
	}

	public String getTaskType() {
		return this.taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public Integer getWorkItemId() {
		return this.workItemId;
	}

	public void setWorkItemId(Integer workItemId) {
		this.workItemId = workItemId;
	}

	public String getActualOwnerId() {
		return actualOwnerId;
	}

	public void setActualOwnerId(String actualOwnerId) {
		this.actualOwnerId = actualOwnerId;
	}

	public List<OrganizationalEntity> getOrganizationalEntities() {
		return this.organizationalEntities;
	}

	public void setOrganizationalEntities(List<OrganizationalEntity> organizationalEntities) {
		this.organizationalEntities = organizationalEntities;
	}
}