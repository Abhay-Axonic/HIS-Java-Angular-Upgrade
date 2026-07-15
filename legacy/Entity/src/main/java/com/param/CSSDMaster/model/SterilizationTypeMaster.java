
package com.param.CSSDMaster.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.model.master.Store;

@NamedQueries({ @NamedQuery(name = "GET_STERILIZATION_TYPE_LIST", query = "SELECT sterilizationTypeMaster.id as id, "
		+ "sterilizationTypeMaster.name as name,sterilizationTypeMaster.status as status, "
		+ "sterilizationTypeMaster.code as code, " 
		+ "sterilizationTypeMaster.created_by as created_by, "
		+ "to_char(sterilizationTypeMaster.created_date,'dd-MM-yyyy') as created_date, "
		+ "sterilizationTypeMaster.updated_by as updated_by, "
		+ "to_char(sterilizationTypeMaster.updated_date,'dd-MM-yyyy') as updated_date "
		+ "FROM SterilizationTypeMaster as sterilizationTypeMaster "
		+ "WHERE sterilizationTypeMaster.organizationId=:organizationId "
		+ "AND sterilizationTypeMaster.unitId=:unitId  "),
	
		@NamedQuery(name = "GET_ACTIVE_STERILIZATION_TYPE_LIST", query = "SELECT sterilizationTypeMaster.id as id, "
				+ "sterilizationTypeMaster.name as name,sterilizationTypeMaster.status as status, "
				+ "sterilizationTypeMaster.code as code, " 
				+ "sterilizationTypeMaster.created_by as created_by, "
				+ "to_char(sterilizationTypeMaster.created_date,'dd-MM-yyyy') as created_date, "
				+ "sterilizationTypeMaster.updated_by as updated_by, "
				+ "to_char(sterilizationTypeMaster.updated_date,'dd-MM-yyyy') as updated_date "
				+ "FROM SterilizationTypeMaster as sterilizationTypeMaster "
				+ "WHERE sterilizationTypeMaster.status='A'  "
				+ "AND sterilizationTypeMaster.organizationId=:organizationId "
				+ "AND sterilizationTypeMaster.unitId=:unitId "
				+ " ORDER BY sterilizationTypeMaster.name ASC ") })

@Entity
@Table(name = "m_sterilization_type_master", schema = "cssd")
@SequenceGenerator(name = "m_sterilization_type_master_id_seq", sequenceName = "cssd.m_sterilization_type_master_id_seq", allocationSize = 1)
public class SterilizationTypeMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_sterilization_type_master_id_seq")
	@Column(name = "id")
	private Integer id;

	@Column(name = "code")
	private String code;

	@Column(name = "name")
	private String name;
	
	@Column(name = "organizationId")
	private Integer organizationId;
	
	@Column(name = "unitId")
	private Integer unitId;

	/*
	 * @Column(name = "is_active") private Boolean is_active;
	 * and sterilizationTypeMaster.storeId=:storeId
	 * @Column(name = "is_deleted") private Boolean is_deleted;
	 */

	@Column(name = "created_by")
	private Integer created_by;

	@Column(name = "updated_by")
	private Integer updated_by;

	@Column(name = "created_date")
	private Date created_date;

	@Column(name = "updated_date")
	private Date updated_date;
	
	@Column(name = "status")
	private Character status;
	
	/*
	 * @Column(name = "store_id") private Integer storeId;
	 */
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", insertable = false, updatable = false)
	private Store store;
	

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Integer getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(Integer updated_by) {
		this.updated_by = updated_by;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public Date getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(Date updated_date) {
		this.updated_date = updated_date;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	
}
