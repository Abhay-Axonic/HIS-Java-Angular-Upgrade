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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.CSSDMaster.model.AutoclaveMaster;
import com.param.entity.model.master.Store;

@NamedQueries({
	@NamedQuery(name = "GET_LOADED_BATCH_LIST", query = "SELECT loadBatch.batchNumber as batchNumber, "
			+ "loadBatch.autoclaveNumber as autoclaveNumber,to_char(loadBatch.autoclaveDate,'dd-MM-yyyy') as autoclaveDate, " 
			+ "to_char(autoclaveMaster.updated_date + autoclaveMaster.biological_indicator_day,'dd-MM-yyyy') as expiryDate, "
			+ "loadBatch.status as status FROM LoadBatch loadBatch INNER JOIN loadBatch.autoclaveMaster as autoclaveMaster "),	
	@NamedQuery(name = "GET_BATCH_LIST_TO_SEND_TO_MICROBIOLOGY", query = "SELECT loadBatch.batchNumber as batchNumber, "
			+ "loadBatch.autoclaveNumber as autoclaveNumber,to_char(loadBatch.autoclaveDate,'dd-MM-yyyy') as autoclaveDate, " 
			+ "to_char(autoclaveMaster.updated_date + autoclaveMaster.biological_indicator_day,'dd-MM-yyyy') as expiryDate, "
			+ "loadBatch.status as status FROM LoadBatch loadBatch INNER JOIN loadBatch.autoclaveMaster as autoclaveMaster "
			+ "WHERE loadBatch.biologicalIndicator=true AND loadBatch.status='batchloaded'"),
	@NamedQuery(name = "GET_LOADED_BATCH_LIST_BY_MULTIPLE_FIELDS", query = "SELECT loadBatch.batchNumber as batchNumber, "
			+ "loadBatch.autoclaveNumber as autoclaveNumber,to_char(loadBatch.autoclaveDate,'dd-MM-yyyy') as autoclaveDate, " 
			+ "to_char(autoclaveMaster.updated_date + autoclaveMaster.biological_indicator_day,'dd-MM-yyyy') as expiryDate, "
			+ "loadBatch.status as status FROM LoadBatch loadBatch INNER JOIN loadBatch.autoclaveMaster as autoclaveMaster "
			+ "WHERE loadBatch.batchNumber=:batchNo AND loadBatch.status=:status AND loadBatch.created_date BETWEEN :from AND :to"),
	@NamedQuery(name = "GET_LOADED_BATCH_LIST_FOR_APPLICATION", query = "SELECT loadBatch.batchNumber as batchNumber, "
			+ "loadBatch.autoclaveNumber as autoclaveNumber,to_char(loadBatch.autoclaveDate,'dd-MM-yyyy') as autoclaveDate, " 
			+ "to_char(autoclaveMaster.updated_date + autoclaveMaster.biological_indicator_day,'dd-MM-yyyy') as expiryDate, "
			+ "loadBatch.status as status FROM LoadBatch loadBatch INNER JOIN loadBatch.autoclaveMaster as autoclaveMaster "
			+ "WHERE loadBatch.batchNumber=:batchNo AND loadBatch.status=:status AND loadBatch.created_date BETWEEN :fromDate AND :toDate "
			+ "AND loadBatch.autoclaveNumber=:autoclaveNo"),
	@NamedQuery(name = "GET_BATCH_NO_LIST_FOR_CONTROL", query = "SELECT batch.batchNo as batchNo "
			+ "FROM Batch batch INNER JOIN batch.item as item WHERE item.name='Control' "),
	@NamedQuery(name = "GET_BATCH_NO_LIST_FOR_TEST", query = "SELECT batch.batchNo as batchNo "
			+ "FROM Batch batch INNER JOIN batch.item as item WHERE item.name='Test' ")
	
})






@Entity
@Table(name = "t_load_batch", schema = "cssd")
@SequenceGenerator(name = "t_load_batch_load_batch_id_seq", sequenceName = "cssd.t_load_batch_load_batch_id_seq", allocationSize = 1)
public class LoadBatch {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_load_batch_load_batch_id_seq")
	@Column(name = "load_batch_id")
	private Integer batchNumber;

	@Column(name = "autoclave_id")
	private Integer autoclaveNumber;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "autoclave_id", insertable = false, updatable = false)
	private AutoclaveMaster  autoclaveMaster;
	
	/*@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "load_batch_id", insertable = false, updatable = false)
	private List<LoadBatchKitMapper>  formedKitList;*/
	
	@Column(name = "autoclave_date")
	private Date autoclaveDate;
	
	@Column(name = "expiry_date")
	private Date expiryDate;
	
	@Column(name = "biological_indicator")
	private Boolean biologicalIndicator;

	@Column(name = "created_by")
	private Integer created_by;

	@Column(name = "updated_by")
	private Integer updated_by;

	@Column(name = "created_date")
	private Date created_date;

	@Column(name = "updated_date")
	private Date updated_date;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "store_id")
	private Integer storeId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", insertable = false, updatable = false)
	private Store store;
	
	@Column(name = "sterilization_temperature")
	private Integer sterilizationTemperature;
	
	@Column(name = "pressure")
	private Integer pressure;
	
		
	@Column(name = "color")
	private String color;
	
	@Column(name = "control")
	private String control;
	
	@Column(name = "test")
	private String test;
	
	@Column(name = "is_sterilized")
	private Boolean isSterilized;

	public Integer getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(Integer batchNumber) {
		this.batchNumber = batchNumber;
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

	public Date getAutoclaveDate() {
		return autoclaveDate;
	}

	public void setAutoclaveDate(Date autoclaveDate) {
		this.autoclaveDate = autoclaveDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Boolean getBiologicalIndicator() {
		return biologicalIndicator;
	}

	public void setBiologicalIndicator(Boolean biologicalIndicator) {
		this.biologicalIndicator = biologicalIndicator;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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

	public Integer getSterilizationTemperature() {
		return sterilizationTemperature;
	}

	public void setSterilizationTemperature(Integer sterilizationTemperature) {
		this.sterilizationTemperature = sterilizationTemperature;
	}

	public Integer getPressure() {
		return pressure;
	}

	public void setPressure(Integer pressure) {
		this.pressure = pressure;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
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

	public Boolean getIsSterilized() {
		return isSterilized;
	}

	public void setIsSterilized(Boolean isSterilized) {
		this.isSterilized = isSterilized;
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
