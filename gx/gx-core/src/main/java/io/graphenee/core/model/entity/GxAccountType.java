package io.graphenee.core.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the gx_account_type database table.
 * 
 */
@Entity
@Table(name="gx_account_type")
@NamedQuery(name="GxAccountType.findAll", query="SELECT g FROM GxAccountType g")
public class GxAccountType extends io.graphenee.core.model.GxMappedSuperclass implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer oid;

	@Column(name="type_code")
	private String typeCode;

	@Column(name="type_name")
	private String typeName;

	//bi-directional many-to-one association to GxAccount
	@OneToMany(mappedBy="gxAccountType")
	private List<GxAccount> gxAccounts;

	public GxAccountType() {
	}

	public Integer getOid() {
		return this.oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getTypeCode() {
		return this.typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public List<GxAccount> getGxAccounts() {
		return this.gxAccounts;
	}

	public void setGxAccounts(List<GxAccount> gxAccounts) {
		this.gxAccounts = gxAccounts;
	}

	public GxAccount addGxAccount(GxAccount gxAccount) {
		getGxAccounts().add(gxAccount);
		gxAccount.setGxAccountType(this);

		return gxAccount;
	}

	public GxAccount removeGxAccount(GxAccount gxAccount) {
		getGxAccounts().remove(gxAccount);
		gxAccount.setGxAccountType(null);

		return gxAccount;
	}

}