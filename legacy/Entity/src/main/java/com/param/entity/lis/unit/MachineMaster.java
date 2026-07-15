package com.param.entity.lis.unit;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.lis.common.LocalTimeConverter;

@NamedQueries({
	@NamedQuery(name = "GET_MACHINE_BY_MACHINE_ID", query = "SELECT machineMaster.machineId as machineId,"
			+ " machineMaster.code as code," 
			+ " machineMaster.machineDesc as machineDesc,"
			+ " machineMaster.status as status,"
			+ " machineMaster.createdBy as createdBy,"
			+ " machineMaster.createdDate as createdDate,"
			+ " machineMaster.updatedBy as updatedBy,"
			+ " machineMaster.unitId as unitId,"
			+ " machineMaster.updatedDate as updatedDate"
			+ " FROM MachineMaster machineMaster"
			+ " WHERE machineMaster.organisationId=:orgId "
			+ " AND machineMaster.unitId=:unitId "
			+ " AND machineMaster.machineId=:machineId "),

	@NamedQuery(name = "GET_MACHINE_BY_CODE", query = "SELECT machineMaster.machineId as machineId,"
			+ " machineMaster.code as code," 
			+ " machineMaster.machineDesc as machineDesc,"
			+ " machineMaster.status as status,"
			+ " machineMaster.createdBy as createdBy,"
			+ " machineMaster.createdDate as createdDate,"
			+ " machineMaster.updatedBy as updatedBy,"
			+ " machineMaster.unitId as unitId,"
			+ " machineMaster.updatedDate as updatedDate"
			+ " FROM MachineMaster machineMaster"
			+ " WHERE machineMaster.organisationId=:orgId "
			+ " AND machineMaster.unitId=:unitId "
			+ " AND lower( machineMaster.code) = lower(:code)"),

	@NamedQuery(name = "UPDATE_GET_MACHINE_BY_CODE", query = "SELECT machineMaster.machineId as machineId,"
			+ " machineMaster.code as code," 
			+ " machineMaster.machineDesc as machineDesc,"
			+ " machineMaster.status as status,"
			+ " machineMaster.createdBy as createdBy,"
			+ " machineMaster.createdDate as createdDate,"
			+ " machineMaster.updatedBy as updatedBy,"
			+ " machineMaster.unitId as unitId,"
			+ " machineMaster.updatedDate as updatedDate"
			+ " FROM MachineMaster machineMaster"
			+ " WHERE machineMaster.organisationId=:orgId "
			+ " AND machineMaster.unitId=:unitId "
			+ " AND lower(machineMaster.code) = lower(:code)"
			+ " AND machineMaster.machineId <> :machineId "),

	@NamedQuery(name = "GET_PAGINATED_MACHINE_MASTER_LIST", query = "SELECT machineMaster.machineId as machineId,"
			+ " machineMaster.code as code," 
			+ " machineMaster.machineDesc as machineDesc,"
			+ " machineMaster.status as status,"
			+ " machineMaster.createdBy as createdBy,"
			+ " machineMaster.createdDate as createdDate,"
			+ " machineMaster.updatedBy as updatedBy,"
			+ " machineMaster.unitId as unitId,"
			+ " machineMaster.updatedDate as updatedDate"
			+ " FROM MachineMaster machineMaster"
			+ " WHERE machineMaster.organisationId=:orgId "
			+ " AND machineMaster.unitId=:unitId "
			+ " ORDER BY machineId DESC"),
	
	@NamedQuery(name = "GET_ACTIVE_MACHINE_LIST", query = "SELECT machineMaster.machineId as machineId,"
			+ " machineMaster.code as code," 
			+ " machineMaster.machineDesc as machineDesc,"
			+ " machineMaster.status as status,"
			+ " machineMaster.unitId as unitId "
	        + " FROM MachineMaster machineMaster"
			+ " WHERE machineMaster.organisationId=:orgId "
			+ " AND machineMaster.unitId=:unitId"
			+ " AND machineMaster.status='A' "
			+ " ORDER BY machineMaster.machineDesc ASC ")
			
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_TOTAL_MACHINE_RECORD", query = "select count(*) from lab.m_machine_master where "
			+ "organisation_id = :orgId and unit_id = :unitId ") })



@Entity 
@Table(name = "m_machine_master", schema = "lab")
@SequenceGenerator(name = "seq_m_machine_master", sequenceName = "lab.seq_m_machine_master", allocationSize = 1)
public class MachineMaster {

	@Id
	@Column(name = "machine_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_machine_master")
	private Integer machineId;

	@Column(name = "code")
	private String code;

	@Column(name = "machine_desc")
	private String machineDesc;

	@Column(name = "status")
	private char status;

	@Column(name = "created_date")
	@Convert(converter = LocalTimeConverter.class)
	private Long createdDate;
	

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	@Convert(converter = LocalTimeConverter.class)
	private Long updatedDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "organisation_id")
	private Integer organisationId;

	@Column(name = "unit_id")
	private Integer unitId;
	

	public Integer getMachineId() {
		return machineId;
	}

	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMachineDesc() {
		return machineDesc;
	}

	public void setMachineDesc(String machineDesc) {
		this.machineDesc = machineDesc;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	
	
	
	

}
