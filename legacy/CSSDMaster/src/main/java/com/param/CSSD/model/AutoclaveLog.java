package com.param.CSSD.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.CSSDMaster.model.AutoclaveMaster;
import com.param.entity.model.master.Store;


@Entity
@Table(name = "t_autoclave_log", schema = "cssd")
@SequenceGenerator(name = "t_autoclave_log_id_seq", sequenceName = "cssd.t_autoclave_log_id_seq", allocationSize = 1)
public class AutoclaveLog {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_autoclave_log_id_seq")
	@Column(name = "id")
	private Integer id;

	@Column(name = "autoclave_id")
	private Integer autoclaveNumber;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "autoclave_id", insertable = false, updatable = false)
	private AutoclaveMaster  autoclaveMaster;
	
	@Column(name = "bio_indi_date")
	private Date bioIndiDate;
		
	@Column(name = "control")
	private String control;
	
	@Column(name = "test")
	private String test;
	
	@Column(name = "created_by")
	private Integer created_by;

	@Column(name = "updated_by")
	private Integer updated_by;

	@Column(name = "created_date")
	private Date created_date;

	@Column(name = "updated_date")
	private Date updated_date;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "store_id")
	private Integer storeId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", insertable = false, updatable = false)
	private Store store;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAutoclaveNumber() {
		return autoclaveNumber;
	}

	public void setAutoclaveNumber(Integer autoclaveNumber) {
		this.autoclaveNumber = autoclaveNumber;
	}

	public AutoclaveMaster getAutoclaveMaster() {
		return autoclaveMaster;
	}

	public void setAutoclaveMaster(AutoclaveMaster autoclaveMaster) {
		this.autoclaveMaster = autoclaveMaster;
	}

	
	public Date getBioIndiDate() {
		return bioIndiDate;
	}

	public void setBioIndiDate(Date bioIndiDate) {
		this.bioIndiDate = bioIndiDate;
	}

	public String getControl() {
		return control;
	}

	public void setControl(String control) {
		this.control = control;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

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

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	
	
}
