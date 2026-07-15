package com.param.entity.lis.unit;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.lis.common.LocalTimeConverter;

@NamedQueries({
	@NamedQuery(name = "GET_MACHINE_PARAMETER_BY_MACHINE_ID", query = "SELECT machineParameter.machinParameterId as machinParameterId,"
			+ " machineParameter.machineId as machineId," 
			+ " machineParameter.machineParameterDesc as machineParameterDesc,"
			+ " machineParameter.status as status,"
			+ " machineParameter.code as code,"
			+ " machineParameter.createdBy as createdBy,"
			+ " machineParameter.createdDate as createdDate,"
			+ " machineParameter.updatedBy as updatedBy,"
			+ " machineParameter.unitId as unitId,"
			+ " machinemsr.machineDesc as machine ,"
			+ " machineParameter.updatedDate as updatedDate"
			+ " FROM MachineParameter machineParameter"
			+ " JOIN  machineParameter.machineMaster machinemsr"
			+ " WHERE machineParameter.organisationId=:orgId "
			+ " AND machineParameter.unitId=:unitId "
			+ " AND machineParameter.machinParameterId=:machinParameterId "),

	@NamedQuery(name = "GET_MACHINE_PARAMETER_BY_CODE", query = "SELECT machineParameter.machinParameterId as machinParameterId,"
			+ " machineParameter.machineId as machineId," 
			+ " machineParameter.machineParameterDesc as machineParameterDesc,"
			+ " machineParameter.status as status,"
			+ " machineParameter.code as code,"
			+ " machineParameter.createdBy as createdBy,"
			+ " machineParameter.createdDate as createdDate,"
			+ " machineParameter.updatedBy as updatedBy,"
			+ " machineParameter.unitId as unitId,"
			+ " machinemsr.machineId as machineId ,"
			+ " machinemsr.machineDesc as machine ,"
			+ " machineParameter.updatedDate as updatedDate"
			+ " FROM MachineParameter machineParameter"
			+ " JOIN  machineParameter.machineMaster machinemsr"
			+ " WHERE machineParameter.organisationId=:orgId "
			+ " AND machineParameter.unitId=:unitId "
			+ " AND lower( machineParameter.code) = lower(:code)"),

	@NamedQuery(name = "UPDATE_GET_MACHINE_PARAMETER_BY_CODE", query = "SELECT machineParameter.machinParameterId as machinParameterId,"
			+ " machineParameter.machineId as machineId," 
			+ " machineParameter.machineParameterDesc as machineParameterDesc,"
			+ " machineParameter.status as status,"
			+ " machineParameter.code as code,"
			+ " machineParameter.createdBy as createdBy,"
			+ " machineParameter.createdDate as createdDate,"
			+ " machineParameter.updatedBy as updatedBy,"
			+ " machineParameter.unitId as unitId,"
			+ " machinemsr.machineDesc as machine ,"
			+ " machinemsr.machineId as machineId ,"
			+ " machineParameter.updatedDate as updatedDate"
			+ " FROM MachineParameter machineParameter"
			+ " JOIN  machineParameter.machineMaster machinemsr"
			+ " WHERE machineParameter.organisationId=:orgId "
			+ " AND machineParameter.unitId=:unitId "
			+ " AND lower(machineParameter.code) = lower(:code)"
			+ " AND machineParameter.machinParameterId <> :machinParameterId "),

	@NamedQuery(name = "GET_PAGINATED_MACHINE_PARAMETER_MASTER_LIST", query = "SELECT machineParameter.machinParameterId as machinParameterId,"
			+ " machineParameter.machineId as machineId," 
			+ " machineParameter.machineParameterDesc as machineParameterDesc,"
			+ " machineParameter.status as status,"
			+ " machineParameter.code as code,"
			+ " machineParameter.createdBy as createdBy,"
			+ " machineParameter.createdDate as createdDate,"
			+ " machineParameter.updatedBy as updatedBy,"
			+ " machineParameter.unitId as unitId,"
			+ " machinemsr.machineDesc as machine ,"
			+ " machineParameter.updatedDate as updatedDate"
			+ " FROM MachineParameter machineParameter "
			+ " JOIN  machineParameter.machineMaster machinemsr "
			+ " WHERE machineParameter.organisationId=:orgId "
			+ " AND machineParameter.unitId=:unitId "
			+ " ORDER BY machineParameter.machineParameterDesc ASC "),
	
	@NamedQuery(name = "GET_ACTIVE_MACHINE_PARAMETER_LIST", query = "SELECT machineParameter.machinParameterId as machinParameterId,"
			+ " machineParameter.machineId as machineId," 
			+ " machineParameter.machineParameterDesc as machineParameterDesc,"
			+ " machineParameter.status as status,"
			+ " machineParameter.code as code,"
			+ " machineParameter.createdBy as createdBy,"
			+ " machineParameter.createdDate as createdDate,"
			+ " machineParameter.updatedBy as updatedBy,"
			+ " machineParameter.unitId as unitId,"
			+ " machineParameter.updatedDate as updatedDate,"
			+ " machinemsr.machineDesc as machine "
            + " FROM MachineParameter machineParameter "
			+ " JOIN  machineParameter.machineMaster machinemsr"
			+ " WHERE machineParameter.organisationId=:orgId "
			+ " AND machineParameter.unitId=:unitId "),
	
	@NamedQuery(name = "GET_MACHINE_PARAMETER_LIST", query = " SELECT machineParameter.machinParameterId as machinParameterId,"
			+ " machineParameter.machineId as machineId," 
			+ " machineParameter.machineParameterDesc as machineParameterDesc,"
			+ " machineParameter.status as status,"
			+ " machineParameter.code as code,"
			+ " machineParameter.createdBy as createdBy,"
			+ " machineParameter.createdDate as createdDate,"
			+ " machineParameter.updatedBy as updatedBy," 
			+ " machineParameter.unitId as unitId,"
			+ " machineParameter.updatedDate as updatedDate "
		    + " FROM MachineParameter machineParameter "
			+ " WHERE machineParameter.organisationId=:orgId "
			+ " AND machineParameter.unitId=:unitId "
			+ " AND machineParameter.machineId=:machineId "
			+ " AND machineParameter.status ='A' "
			+ " ORDER BY machineParameter.machineParameterDesc ASC ")
			
			
			
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_TOTAL_MACHINE_PARAMETER_RECORD", query = "select count(*) from  lab.m_machine_parameter where "
			+ "organisation_id = :orgId and unit_id = :unitId ") })



@Entity
@Table(name = "m_machine_parameter", schema = "lab")
@SequenceGenerator(name = "seq_m_machine_parameter", sequenceName = "lab.seq_m_machine_parameter", allocationSize = 1)
public class MachineParameter 
{


	@Id
	@Column(name = "machin_parameter_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_machine_parameter")
	private Integer machinParameterId;

	@Column(name = "machine_id")
	private Integer machineId;
	
	@Column(name = "code")
	private String code;

	@Column(name = "machine_parameter_desc")
	private String machineParameterDesc;

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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "machine_id", insertable = false, nullable = false, updatable = false)
	private MachineMaster machineMaster;

	
	public Integer getMachinParameterId() {
		return machinParameterId;
	}

	public void setMachinParameterId(Integer machinParameterId) {
		this.machinParameterId = machinParameterId;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMachineParameterDesc() {
		return machineParameterDesc;
	}

	public void setMachineParameterDesc(String machineParameterDesc) {
		this.machineParameterDesc = machineParameterDesc;
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

	public MachineMaster getMachineMaster() {
		return machineMaster;
	}

	public void setMachineMaster(MachineMaster machineMaster) {
		this.machineMaster = machineMaster;
	}

	public Integer getMachineId() {
		return machineId;
	}

	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}

	
	

}
