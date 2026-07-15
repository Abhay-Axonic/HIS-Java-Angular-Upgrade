package com.param.entity.lis.unit;

import java.util.Date;

import javax.persistence.Column;
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

/*@NamedQueries({
	@NamedQuery(name = "ACTIVE_PARAMETER_LIST", query = "SELECT machineParameter.machineParameterMpprId as machineParameterMpprId,"
			+ " machineParameter.machineId as machineId," 
			+ " machineParameter.machinParameterId as machinParameterId,"
			+ " machineParameter.parameterId as parameterId,"
			+ " machineParameter.status as status,"
			+ " machineParameter.createdDate as createdDate,"
			+ " machineParameter.createdBy as createdBy,"
			+ " machineParameter.updatedDate as updatedDate,"
			+ " machineParameter.updatedBy as updatedBy,"
			+ " machinemsr.organisationId as organisationId ,"
			+ " machineParameter.updatedDate as updatedDate ,"
			+ " machineParameter.unitId as unitId "
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
			+ " ORDER BY machinParameterId DESC"),
	
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
	
})*/
/*@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_TOTAL_MACHINE_PARAMETER_RECORD", query = "select count(*) from  lab.m_machine_parameter where "
			+ "organisation_id = :orgId and unit_id = :unitId ") })*/



@Entity 
@Table(name = "t_machine_parameter_mppr", schema = "lab")
@SequenceGenerator(name = "seq_t_machine_parameter_mppr", sequenceName = "lab.seq_t_machine_parameter_mppr", allocationSize = 1)
public class MachineParameterMpper {

	@Id
	@Column(name = "machine_parameter_mppr_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_t_machine_parameter_mppr")
	private int machineParameterMpprId;

	@Column(name = "machine_id")
	private Integer machineId;
	
	@Column(name = "machin_parameter_id")
	private Integer machinParameterId;

	@Column(name = "parameter_id")
	private Integer parameterId;

	@Column(name = "status")
	private char status;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "organisation_id")
	private Integer organisationId;

	@Column(name = "unit_id")
	private Integer unitId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "machine_id", insertable = false, nullable = false, updatable = false)
	private MachineMaster machineMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "machin_parameter_id", insertable = false, nullable = false, updatable = false)
	private MachineParameter machineParameter;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parameter_id", insertable = false, nullable = false, updatable = false)
	private ParameterMaster parameterMaster;

	public int getMachineParameterMpprId() {
		return machineParameterMpprId;
	}

	public void setMachineParameterMpprId(int machineParameterMpprId) {
		this.machineParameterMpprId = machineParameterMpprId;
	}

	public Integer getMachineId() {
		return machineId;
	}

	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}

	public Integer getMachinParameterId() {
		return machinParameterId;
	}

	public void setMachinParameterId(Integer machinParameterId) {
		this.machinParameterId = machinParameterId;
	}

	public Integer getParameterId() {
		return parameterId;
	}

	public void setParameterId(Integer parameterId) {
		this.parameterId = parameterId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status == '\u0000') ? 'A' : status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
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

	public MachineParameter getMachineParameter() {
		return machineParameter;
	}

	public void setMachineParameter(MachineParameter machineParameter) {
		this.machineParameter = machineParameter;
	}

	public ParameterMaster getParameterMaster() {
		return parameterMaster;
	}

	public void setParameterMaster(ParameterMaster parameterMaster) {
		this.parameterMaster = parameterMaster;
	}
	
	
}
