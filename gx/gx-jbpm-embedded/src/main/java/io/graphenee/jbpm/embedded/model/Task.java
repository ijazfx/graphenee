package io.graphenee.jbpm.embedded.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
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

	private Date activationTime;

	@Column(name = "actualOwner_id")
	private String actualOwnerId;

	private String allowedToDelegate;

	private short archived;

	private String createdBy_id;

	private Date createdOn;

	private String deploymentId;

	private String description;

	private Integer documentAccessType;

	private BigDecimal documentContentId;

	private String documentType;

	private Date expirationTime;

	private Integer faultAccessType;

	private BigDecimal faultContentId;

	private String faultName;

	private String faultType;

	private String formName;

	private String name;

	@Column(name = "OPTLOCK")
	private Integer optlock;

	private Integer outputAccessType;

	private BigDecimal outputContentId;

	private String outputType;

	private BigDecimal parentId;

	private Integer previousStatus;

	private Integer priority;

	private String processId;

	private BigDecimal processInstanceId;

	private BigDecimal processSessionId;

	private Boolean skipable;

	private String status;

	private String subject;

	private String subTaskStrategy;

	private String taskInitiator_id;

	private String taskType;

	private BigDecimal workItemId;


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

	public Date getActivationTime() {
		return this.activationTime;
	}

	public void setActivationTime(Date activationTime) {
		this.activationTime = activationTime;
	}

	public String getAllowedToDelegate() {
		return this.allowedToDelegate;
	}

	public void setAllowedToDelegate(String allowedToDelegate) {
		this.allowedToDelegate = allowedToDelegate;
	}

	public short getArchived() {
		return this.archived;
	}

	public void setArchived(short archived) {
		this.archived = archived;
	}

	public String getCreatedBy_id() {
		return this.createdBy_id;
	}

	public void setCreatedBy_id(String createdBy_id) {
		this.createdBy_id = createdBy_id;
	}

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
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

	public BigDecimal getDocumentContentId() {
		return this.documentContentId;
	}

	public void setDocumentContentId(BigDecimal documentContentId) {
		this.documentContentId = documentContentId;
	}

	public String getDocumentType() {
		return this.documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public Date getExpirationTime() {
		return this.expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	public Integer getFaultAccessType() {
		return this.faultAccessType;
	}

	public void setFaultAccessType(Integer faultAccessType) {
		this.faultAccessType = faultAccessType;
	}

	public BigDecimal getFaultContentId() {
		return this.faultContentId;
	}

	public void setFaultContentId(BigDecimal faultContentId) {
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

	public BigDecimal getOutputContentId() {
		return this.outputContentId;
	}

	public void setOutputContentId(BigDecimal outputContentId) {
		this.outputContentId = outputContentId;
	}

	public String getOutputType() {
		return this.outputType;
	}

	public void setOutputType(String outputType) {
		this.outputType = outputType;
	}

	public BigDecimal getParentId() {
		return this.parentId;
	}

	public void setParentId(BigDecimal parentId) {
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

	public BigDecimal getProcessInstanceId() {
		return this.processInstanceId;
	}

	public void setProcessInstanceId(BigDecimal processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public BigDecimal getProcessSessionId() {
		return this.processSessionId;
	}

	public void setProcessSessionId(BigDecimal processSessionId) {
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

	public BigDecimal getWorkItemId() {
		return this.workItemId;
	}

	public void setWorkItemId(BigDecimal workItemId) {
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