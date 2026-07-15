package com.param.opd.coversheet.model;

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

import com.param.adt.master.global.model.UserMaster;
import com.param.entity.model.master.Frequency;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RouteOfAdministration;
import com.param.entity.model.master.StrengthUnit;

@NamedQueries({
		@NamedQuery(name="GET_LIST_OF_INFUSION_SOLUSION", 
				query="SELECT "
				+ " infusionSolutionMaster.infusionSolutionId as infusionSolutionId , "
				+ " infusionSolutionMaster.itemId as itemId , "
				+ " infusionSolutionMaster.genericId as genericId , "
				+ " infusionSolutionMaster.strengthUnitId as strengthUnitId , "
				+ " infusionSolutionMaster.roaId as roaId , "
				+ " infusionSolutionMaster.frequencyId as frequencyId , "
				+ " infusionSolutionMaster.priority as priority , "
				+ " to_char(infusionSolutionMaster.startDate, 'dd/mm/yyyy') as startDate , "
				+ " to_char(infusionSolutionMaster.endDate, 'dd/mm/yyyy') as endDate , "
				+ " to_char(infusionSolutionMaster.updatedDate, 'dd/mm/yyyy') as updatedDate , "
				+ " to_char(infusionSolutionMaster.createdDate, 'dd/mm/yyyy') as createdDate , "
				+ " infusionSolutionMaster.totalVolume as totalVolume , "
				+ " infusionSolutionMaster.duration as duration , "
				+ " infusionSolutionMaster.infusionRate as infusionRate , "
				+ " infusionSolutionMaster.isStartFlag as isStartFlag , "
				+ " infusionSolutionMaster.justificationStopInfusion as justificationStopInfusion , "
				+ " infusionSolutionMaster.comments as comments , "
				+ " infusionSolutionMaster.instructionId as instructionId , "
				+ " infusionSolutionMaster.organizationId as organizationId , "
				+ " infusionSolutionMaster.unitId as unitId , "
				+ " infusionSolutionMaster.status as status , "
				+ " infusionSolutionMaster.solutionVolume as solutionVolume , "
				+ " itemMaster.name as solutionName, "
				+ " routeOfAdministration.description as roaDescription , "
				+ " strengthUnitMaster.units as solutionUnit,"
				+ " (case "
				+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
				+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) "
				+ " else 'Admin' end) as userName,"
//				+ " infusionAdditiveMaster.additiveName as additiveName, "
				+ " frequencyMaster.description as frequencyDesc "
//				+ " infusionSolutionMaster.formulationTypeId as formulationTypeId , "
//				+ " infusionAdditiveMaster.volume as volume "
				+ " FROM InfusionSolutionMaster infusionSolutionMaster "
//				+ " INNER JOIN infusionSolutionMaster.routeOfAdministration routeOfAdministration"
				+ " INNER JOIN infusionSolutionMaster.itemMaster itemMaster"
				+ " INNER JOIN infusionSolutionMaster.frequencyMaster frequencyMaster"
				+ " INNER JOIN infusionSolutionMaster.strengthUnitMaster strengthUnitMaster"
				+ " INNER JOIN infusionSolutionMaster.routeOfAdministration routeOfAdministration "
				+ " LEFT JOIN infusionSolutionMaster.userMaster um "
				+ " LEFT JOIN um.employeeMastersList empmst "
				+ " LEFT JOIN um.doctorMastersList docmst "
				+ " WHERE  infusionSolutionMaster.organizationId= :organizationId "
				+ " AND infusionSolutionMaster.unitId= :unitId"
				+ " AND infusionSolutionMaster.patientId= :patientId"
				),
		/*@NamedQuery(name="GET_ADDITIVE_LIST_BY_ID" ,
		query="SELECT "
				+ " infusionSolutionMaster.infusionSolutionId as infusionSolutionId , "
				+ " infusionSolutionMaster.solutionVolume as solutionVolume , "
				+ " infusionAdditiveMaster.additiveName as additiveName, "
				+ " infusionAdditiveMaster.volume as volume "
				+ " FROM InfusionSolutionMaster infusionSolutionMaster "
				+ " INNER JOIN infusionSolutionMaster.infusionAdditiveMaster infusionAdditiveMaster"
				+ " WHERE  infusionAdditiveMaster.organizationId= :organizationId "
				+ " AND infusionAdditiveMaster.unitId= :unitId"
				+ " AND infusionAdditiveMaster.infusionSolutionId= :infusionSolutionId"
				)*/
})
//+ " INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id "

@NamedNativeQueries({
	
	@NamedNativeQuery(name="GET_INFUSION_SOLUSION_ITEM_LIST",
		query="SELECT "
					+ " itemMaster.id as \"solutionItemId\" , "
					+ " itemMaster.organization_id AS \"organizationId\" , "
					+ " itemMaster.unit_id AS \"unitId\" , "
					+ " itemMaster.name as \"solutionName\" , "
					+ " itemMaster.strength as \"strength\" , "
					+ " itemMaster.formulation_type_id as \"formulationTypeId\", "
					+ " generic.id as \"solutiongenericId\" , "
					+ " generic.name as \"solutiongenericName\"  "
					+ " FROM public.m_item  itemMaster "
					+ " INNER JOIN public.m_generic generic ON generic.id=itemMaster.generic_id  "
					+ " WHERE  itemMaster.organization_id= :organizationId "
					+ " AND itemMaster.unit_id= :unitId"
					+ " AND itemMaster.is_active= TRUE"
					+ " AND itemMaster.is_deleted= FALSE"
					+ " AND itemMaster.is_infusion= TRUE"
					+ " AND lower(itemMaster.name) LIKE :name "
					),
	
	@NamedNativeQuery(name="GET_INFUSION_ADDITIVE_ITEM_LIST",
	query="SELECT"
			+ " itemMaster.id as \"additiveItemId\" , "
			+ " itemMaster.organization_id AS \"organizationId\" , "
			+ " itemMaster.unit_id AS \"unitId\" , "
			+ " itemMaster.name as \"additiveName\" , "
			+ " itemMaster.formulation_type_id as \"formulationTypeId\", "
			+ " generic.id as \"additivegenericId\" , "
			+ " generic.name as \"additivegenericName\"  "
			+ " FROM public.m_item  itemMaster"
			+ " INNER JOIN public.m_generic generic ON generic.id=itemMaster.generic_id  "
			+ " WHERE  itemMaster.organization_id= :organizationId "
			+ " AND itemMaster.unit_id= :unitId"
			+ " AND itemMaster.is_active= TRUE"
			+ " AND itemMaster.is_deleted= FALSE"
//			+ " AND itemMaster.formulation_type_id= 29"
			+ " AND itemMaster.is_injection= TRUE"
			+ " AND lower(itemMaster.name) LIKE :name "
				),
})


@Entity
@Table(name = "m_infusion_solution", schema = "opd")
@SequenceGenerator(name = "m_infusion_solution_seq", sequenceName = "opd.m_infusion_solution_seq", allocationSize = 1)
public class InfusionSolutionMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_infusion_solution_seq")
	
	@Column(name = "infusion_solution_id")
	private Integer infusionSolutionId;
	
	@Column(name = "item_id")
	private Integer itemId;
	
	@Column(name = "generic_id")
	private Integer genericId;
	
	@Column(name = "solution_volume")
	private Double solutionVolume;
	
	@Column(name = "strength_unit_id")
	private Integer strengthUnitId;
	
	@Column(name = "roa_id")
	private Integer roaId;
	
	@Column(name = "frequency_id")
	private Integer frequencyId;
	
	@Column(name = "priority")
	private Integer priority;
	
	@Column(name = "start_date")
	private Date startDate;
	
	@Column(name = "end_date")
	private Date endDate;
	
	@Column(name = "total_volume")
	private Integer totalVolume;
	
	@Column(name = "duration")
	private Integer duration;
	
	@Column(name = "infusion_rate")
	private Double infusionRate;
	
	@Column(name = "comments")
	private String comments;
	
	@Column(name = "instruction_id")
	private Integer instructionId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "is_start_flag")
	private Character isStartFlag;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	

	@Column(name = "justification_stop_infusion")
	private String justificationStopInfusion;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="infusion_solution_id", insertable=false, updatable=false)
	private InfusionAdditiveMaster infusionAdditiveMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="item_id", insertable=false, updatable=false)
	private Item itemMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="frequency_id", insertable=false, updatable=false)
	private Frequency frequencyMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="strength_unit_id", insertable=false, updatable=false)
	private StrengthUnit strengthUnitMaster ;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="roa_id", insertable=false, updatable=false)
	private RouteOfAdministration routeOfAdministration ;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="created_by", insertable=false, updatable=false)
	private UserMaster userMaster ;
	
	public Integer getInfusionSolutionId() {
		return infusionSolutionId;
	}

	public void setInfusionSolutionId(Integer infusionSolutionId) {
		this.infusionSolutionId = infusionSolutionId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	
	public Integer getStrengthUnitId() {
		return strengthUnitId;
	}

	public void setStrengthUnitId(Integer strengthUnitId) {
		this.strengthUnitId = strengthUnitId;
	}

	public Integer getRoaId() {
		return roaId;
	}

	public void setRoaId(Integer roaId) {
		this.roaId = roaId;
	}

	public Integer getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Integer frequencyId) {
		this.frequencyId = frequencyId;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(Integer totalVolume) {
		this.totalVolume = totalVolume;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(Integer instructionId) {
		this.instructionId = instructionId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public InfusionAdditiveMaster getInfusionAdditiveMaster() {
		return infusionAdditiveMaster;
	}

	public void setInfusionAdditiveMaster(InfusionAdditiveMaster infusionAdditiveMaster) {
		this.infusionAdditiveMaster = infusionAdditiveMaster;
	}

	public Double getInfusionRate() {
		return infusionRate;
	}

	public Character getIsStartFlag() {
		return isStartFlag;
	}

	public void setIsStartFlag(Character isStartFlag) {
		this.isStartFlag = isStartFlag;
	}

	public void setInfusionRate(Double infusionRate) {
		this.infusionRate = infusionRate;
	}

	public String getJustificationStopInfusion() {
		return justificationStopInfusion;
	}

	public void setJustificationStopInfusion(String justificationStopInfusion) {
		this.justificationStopInfusion = justificationStopInfusion;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Double getSolutionVolume() {
		return solutionVolume;
	}

	public void setSolutionVolume(Double solutionVolume) {
		this.solutionVolume = solutionVolume;
	}
	
	
}
