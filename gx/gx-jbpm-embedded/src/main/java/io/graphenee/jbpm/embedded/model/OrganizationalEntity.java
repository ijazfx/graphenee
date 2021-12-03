package io.graphenee.jbpm.embedded.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;


/**
 * The persistent class for the OrganizationalEntity database table.
 * 
 */
@Entity
@NamedQuery(name="OrganizationalEntity.findAll", query="SELECT o FROM OrganizationalEntity o")
public class OrganizationalEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="DTYPE")
	private String dtype;

	//bi-directional many-to-many association to Task
	@ManyToMany(mappedBy="organizationalEntities")
	private List<Task> tasks;

	public OrganizationalEntity() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDtype() {
		return this.dtype;
	}

	public void setDtype(String dtype) {
		this.dtype = dtype;
	}

	public List<Task> getTasks() {
		return this.tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

}