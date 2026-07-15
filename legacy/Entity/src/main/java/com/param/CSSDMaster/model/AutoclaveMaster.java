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

import io.swagger.annotations.ApiModelProperty;

@NamedQueries({

		@NamedQuery(name = "GET_ALL_ACTIVE_AUTOCLAVE_LIST", query = "SELECT autoclaveMaster.id as id, "
				+ "autoclaveMaster.machine_name as machine_name,"
				+ "autoclaveMaster.status as status, "
				+ "autoclaveMaster.machine_description as machine_description, "
				+ "autoclaveMaster.sterilization_type_id as sterilization_type_id, "
				+ "strTypeMst.name as sterilization_type, "
				+ "autoclaveMaster.biological_indicator_day as biological_indicator_day, "
				+ "autoclaveMaster.sterilization_temperature as sterilization_temperature, "
				+ "autoclaveMaster.sterilization_pressure as sterilization_pressure, "
				+ " autoclaveMaster.sterilization_color_id as sterilization_color_id, "
				+ "autoclaveMaster.created_by as created_by, "
				+ "to_char(autoclaveMaster.created_date,'dd-MM-yyyy') as created_date, "
				+ "autoclaveMaster.updated_by as updated_by, "
				+ "to_char(autoclaveMaster.updated_date,'dd-MM-yyyy') as updated_date "
				+ "FROM AutoclaveMaster as autoclaveMaster "
				+ "INNER JOIN autoclaveMaster.sterilizationTypeMaster as strTypeMst "
				+ "WHERE autoclaveMaster.status='A'"
				+ "AND autoclaveMaster.organizationId=:organizationId "
				+ "AND autoclaveMaster.unitId=:unitId "),
		
		
		@NamedQuery(name = "GET_ALL_AUTOCLAVE_LIST", query = "SELECT autoclaveMaster.id as id, "
				+ "autoclaveMaster.machine_name as machine_name, "
				+ "autoclaveMaster.status as status, "
				+ "autoclaveMaster.machine_description as machine_description, "
				+ "autoclaveMaster.sterilization_type_id as sterilization_type_id, "
				+ "strTypeMst.name as sterilization_type, "
				+ "autoclaveMaster.biological_indicator_day as biological_indicator_day, "
				+ "autoclaveMaster.sterilization_temperature as sterilization_temperature, "
				+ "autoclaveMaster.sterilization_pressure as sterilization_pressure,"
				+ " autoclaveMaster.sterilization_color_id as sterilization_color_id, "
				+ "autoclaveMaster.created_by as created_by, "
				+ "to_char(autoclaveMaster.created_date,'dd-MM-yyyy') as created_date, "
				+ "autoclaveMaster.updated_by as updated_by, "
				+ "to_char(autoclaveMaster.updated_date,'dd-MM-yyyy') as updated_date "
				+ "FROM AutoclaveMaster as autoclaveMaster "
				+ "INNER JOIN autoclaveMaster.sterilizationTypeMaster as strTypeMst "
				+ "WHERE autoclaveMaster.organizationId=:organizationId  "
				+ "AND autoclaveMaster.unitId=:unitId ")
				

})

@Entity
@Table(name = "m_autoclave_master", schema = "cssd")
@SequenceGenerator(name = "m_autoclave_master_id_seq", sequenceName = "cssd.m_autoclave_master_id_seq", allocationSize = 1)
public class AutoclaveMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_autoclave_master_id_seq")
	@Column(name = "id")
	private int id;

	@Column(name = "machine_name")
	private String machine_name;

	@Column(name = "machine_description")
	private String machine_description;

	@Column(name = "sterilization_type")
	private int sterilization_type_id;

	// @ApiModelProperty(value = "Added Date")

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;
	
	/*
	 * @Column(name = "store_id") private Integer storeId;
	 */
	
	@Column(name = "biological_indicator_day")
	private Integer biological_indicator_day;

	@Column(name = "sterilization_temperature")
	private int sterilization_temperature;

	@Column(name = "sterilization_pressure")
	private int sterilization_pressure;

    @Column(name="sterilization_color_id")
    private Integer sterilization_color_id;
    
	/*
	 * @Column(name = "is_active") private Boolean is_active;
	 * 
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sterilization_type", insertable = false, updatable = false)
	private SterilizationTypeMaster sterilizationTypeMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", insertable = false, updatable = false)
	private Store store;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMachine_name() {
		return machine_name;
	}

	public void setMachine_name(String machine_name) {
		this.machine_name = machine_name;
	}

	public String getMachine_description() {
		return machine_description;
	}

	public void setMachine_description(String machine_description) {
		this.machine_description = machine_description;
	}

	public int getSterilization_type_id() {
		return sterilization_type_id;
	}

	public void setSterilization_type_id(int sterilization_type_id) {
		this.sterilization_type_id = sterilization_type_id;
	}

	public Integer getBiological_indicator_day() {
		return biological_indicator_day;
	}

	public void setBiological_indicator_day(Integer biological_indicator_day) {
		this.biological_indicator_day = biological_indicator_day;
	}

	public int getSterilization_pressure() {
		return sterilization_pressure;
	}

	public void setSterilization_pressure(int sterilization_pressure) {
		this.sterilization_pressure = sterilization_pressure;
	}

	public SterilizationTypeMaster getSterilizationTypeMaster() {
		return sterilizationTypeMaster;
	}

	public int getSterilization_temperature() {
		return sterilization_temperature;
	}

	public void setSterilization_temperature(int sterilization_temperature) {
		this.sterilization_temperature = sterilization_temperature;
	}

	public void setSterilizationTypeMaster(SterilizationTypeMaster sterilizationTypeMaster) {
		this.sterilizationTypeMaster = sterilizationTypeMaster;
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

	public Integer getSterilization_color_id() {
		return sterilization_color_id;
	}

	public void setSterilization_color_id(Integer sterilization_color_id) {
		this.sterilization_color_id = sterilization_color_id;
	}
	
	

}
