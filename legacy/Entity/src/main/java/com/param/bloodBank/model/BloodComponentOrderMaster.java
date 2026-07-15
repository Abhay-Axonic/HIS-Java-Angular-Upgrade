package com.param.bloodBank.model;

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

import com.param.adt.admission.model.Admission;
import com.param.adt.master.global.model.UserMaster;
import com.param.global.model.DoctorMaster;
import com.param.global.model.PatientRegistration;
import com.param.global.model.ServiceMaster;
import com.param.otims.model.ProcedureOrderMaster;

@NamedQueries({
	
	@NamedQuery(name="GET_BLOOD_COMPONENT_ORDER_MASTER_LIST",
		query=" SELECT  bloodComponentOrderMaster.bloodComponentOrderId AS bloodComponentOrderId, "
				+ " bloodComponentOrderMaster.bloodComponentId AS bloodComponentId, "
				+ " bloodComponentOrderMaster.unit AS unit, "
				+ " bloodComponentMaster.bloodComponentDesc as bloodComponentDesc, "
				+ " bloodComponentOrderMaster.patientId AS patientId, "
                + " to_char(bloodComponentOrderMaster.requisitionDate, 'dd/mm/yyyy') AS requisitionDate, "
		        + " to_char(bloodComponentOrderMaster.requisitionOnDate, 'dd/mm/yyyy') AS requisitionOnDate, "
				+ " to_char(bloodComponentOrderMaster.createdDate, 'DD/MM/YYYY') AS createdDate, "
				+ " bloodComponentOrderMaster.procedureOrderId AS procedureOrderId, "
				+ " bloodComponentOrderMaster.comments AS comments, "
				+ " bloodComponentOrderMaster.instruction AS instruction, "
				+ " bloodComponentOrderMaster.priority AS priority, "
				+ " bloodComponentOrderMaster.organizationId AS organizationId,"
				+ " bloodComponentOrderMaster.unitId AS unitId,"
				+ " bloodComponentOrderMaster.isRequired AS isRequired, "
				+ " bloodComponentOrderMaster.status AS status, "
				+ " (case "
				+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
				+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) "
				+ " else 'Admin' end) as userName,"
				+ " serviceMaster.serviceStandardName as procedureDesc"
				+ " FROM BloodComponentOrderMaster bloodComponentOrderMaster "
				+ " INNER JOIN bloodComponentOrderMaster.bloodComponentMaster bloodComponentMaster"
				+ " left JOIN bloodComponentOrderMaster.procedureOrderMaster procedureOrderMaster"
				+ " LEFT JOIN procedureOrderMaster.serviceMaster serviceMaster"
				+ " LEFT JOIN bloodComponentOrderMaster.userMaster um "
				+ " LEFT JOIN um.employeeMastersList empmst "
				+ " LEFT JOIN um.doctorMastersList docmst "
				+ " WHERE bloodComponentOrderMaster.organizationId= :organizationId"
				+ " AND bloodComponentOrderMaster.unitId= :unitId"
				+ " AND bloodComponentOrderMaster.patientId= :patientId"
				+ " ORDER BY bloodComponentMaster.bloodComponentDesc asc "
				),
	
	@NamedQuery(name="GET_BLOOD_COMPONENT_ORDER_MASTER_BY_ID",
			query=" SELECT  bloodComponentOrderMaster.bloodComponentOrderId AS bloodComponentOrderId, "
					+ " bloodComponentOrderMaster.bloodComponentId AS bloodComponentId, "
					+ " bloodComponentOrderMaster.unit AS unit, "
					+ " bloodComponentOrderMaster.patientId AS patientId, "
					+ " bloodComponentOrderMaster.procedureOrderId AS procedureOrderId, "
					+ " bloodComponentOrderMaster.comments AS comments, "
					+ " bloodComponentOrderMaster.instruction AS instruction, "
					+ " to_char(bloodComponentOrderMaster.requisitionDate, 'dd/mm/yyyy') AS requisitionDate, "
					+ " to_char(bloodComponentOrderMaster.requisitionOnDate, 'dd/mm/yyyy') AS requisitionOnDate, "
					+ " bloodComponentOrderMaster.priority AS priority, "
					+ " bloodComponentOrderMaster.organizationId AS organizationId,"
					+ " bloodComponentOrderMaster.unitId AS unitId,"
					+ " bloodComponentOrderMaster.status AS status "
					+ " FROM BloodComponentOrderMaster bloodComponentOrderMaster"
					+ " WHERE bloodComponentOrderMaster.bloodComponentOrderId= :bloodComponentOrderId"
					+ " AND bloodComponentOrderMaster.organizationId= :organizationId"
					+ " AND bloodComponentOrderMaster.unitId= :unitId"
				),
	
	@NamedQuery(name="GET_ACTIVE_BLOOD_COMPONENT_ORDER_MASTER",
			query=" SELECT  bloodComponentOrderMaster.bloodComponentOrderId AS bloodComponentOrderId, "
					+ " bloodComponentOrderMaster.bloodComponentId AS bloodComponentId, "
					+ " bloodComponentOrderMaster.unit AS unit, "
					+ " bloodComponentOrderMaster.patientId AS patientId, "
					+ " bloodComponentOrderMaster.procedureOrderId AS procedureOrderId, "
					+ " to_char(bloodComponentOrderMaster.requisitionDate, 'dd/mm/yyyy') AS requisitionDate, "
					+ " to_char(bloodComponentOrderMaster.requisitionOnDate, 'dd/mm/yyyy') AS requisitionOnDate, "
					+ " bloodComponentOrderMaster.comments AS comments, "
					+ " bloodComponentOrderMaster.instruction AS instruction, "
					+ " bloodComponentOrderMaster.priority AS priority, "
					+ " bloodComponentOrderMaster.organizationId AS organizationId,"
					+ " bloodComponentOrderMaster.unitId AS unitId,"
					+ " bloodComponentOrderMaster.status AS status "
					+ " FROM BloodComponentOrderMaster bloodComponentOrderMaster"
					+ " WHERE bloodComponentOrderMaster.status= 'A'"
					+ " AND bloodComponentOrderMaster.organizationId= :organizationId"
					+ " AND bloodComponentOrderMaster.unitId= :unitId"
					+ " ORDER BY bloodComponentOrderMaster.bloodComponentId asc "
				),
	
	@NamedQuery(name="GET_LIST_OF_PROCEDURE_BY_PATIENT_ID",
	query=" SELECT  bloodComponentOrderMaster.bloodComponentOrderId AS bloodComponentOrderId, "
			+ " bloodComponentOrderMaster.organizationId AS organizationId,"
			+ " bloodComponentOrderMaster.unitId AS unitId,"
			+ " bloodComponentOrderMaster.status AS status "
			+ " FROM BloodComponentOrderMaster bloodComponentOrderMaster"
			+ " WHERE bloodComponentOrderMaster.patientId= :patientId"
			+ " AND bloodComponentOrderMaster.organizationId= :organizationId"
			+ " AND bloodComponentOrderMaster.unitId= :unitId"
		),
	
	@NamedQuery(name="SEARCH_ORDERED_BLOOD_COMPONENT",
			query=" SELECT "
					+ " distinct (bloodComponentOrderMaster.bloodComponentOrderId) AS  bloodComponentOrderId ," //primary key
					+  " bloodComponentOrderMaster.requisitionNumber AS requisitionNumber, "
					+ " bloodComponentOrderMaster.organizationId AS organizationId,"
					+ " bloodComponentOrderMaster.unitId AS unitId,"
					+ " bloodComponentOrderMaster.isRequired AS isRequired,"
					+ " bloodComponentOrderMaster.priority AS priority,"
					+ " to_char(bloodComponentOrderMaster.requisitionDate,'DD-MM-YYYY') AS requisitionDate,"
					+ " to_char(bloodComponentOrderMaster.requisitionOnDate,'DD-MM-YYYY') AS issueDate, "
					+ " bloodComponentOrderMaster.unit AS unit,"
					+ " doctor.doctorId AS doctorId,"
					+ " bloodComponentMaster.bloodComponentDesc AS bloodComponentDesc, "
					+ " bloodComponentMaster.bloodComponentId AS bloodComponentId, "
					+ " concat('Dr. ',coalesce(doctor.firstName,''),' ',coalesce(doctor.middleName,''),' ',coalesce(doctor.lastName,'')) AS doctorName, "
					+ " gender.desc AS genderName,"
					+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, patient.dob)),'M ') AS age, "
					+ " bloodComponentOrderMaster.status AS status, "
					+ " patient.uhidNumber AS uhidNumber, "
					+ " to_char(patient.dob,'DD-MM-YYYY') AS dob, "
					+ " visitTypeMaster.visitTypeName AS visitTypeName, "
					+ " bloodComponentOrderMaster.patientId AS patientId, "
					+ " concat(coalesce(patient.firstName,'') ,' ', coalesce(patient.middleName,'') ,' ', coalesce(patient.lastName,'')) AS patientName, "
					+ " admission.admissionId AS admissionId, "
					+ " wardMaster.wardName AS wardName "
					//+ " bedMaster.bedNumber AS bedNumber "
					+ " FROM BloodComponentOrderMaster bloodComponentOrderMaster "
					+ " INNER JOIN bloodComponentOrderMaster.bloodComponentMaster bloodComponentMaster  "
					+ " INNER JOIN bloodComponentOrderMaster.patientRegistration patient  "
					+ " INNER JOIN patient.genderMaster gender  "
					+ " INNER JOIN bloodComponentOrderMaster.doctorMaster doctor "
					+ " LEFT JOIN bloodComponentOrderMaster.admission admission  "
					+ " LEFT JOIN admission.admissionDetailsList adtDetails  "
					+ " LEFT JOIN adtDetails.wardMaster wardMaster  "
					//+ " LEFT JOIN adtDetails.bedMaster bedMaster  "
					+ " LEFT JOIN admission.visitTypeMaster visitTypeMaster  "
					+ " WHERE bloodComponentOrderMaster.organizationId= :organizationId "
					+ " AND bloodComponentOrderMaster.unitId= :unitId "
					+ " AND bloodComponentOrderMaster.isExported= false "
					+ " AND bloodComponentOrderMaster.isRequisitionClosed= false "),
			
	@NamedQuery(name="GET_COMPONENT_LIST_BLOOD_ORDER_ID",
	query=" SELECT  bloodComponentOrderMaster.bloodComponentOrderId AS bloodComponentOrderId, "
			+ " bloodComponentOrderMaster.organizationId AS organizationId,"
			+ " bloodComponentOrderMaster.unitId AS unitId,"
			+ " bloodComponentOrderMaster.isRequired AS isRequired,"
			+ " to_char(bloodComponentOrderMaster.requisitionDate,'DD-MM-YYYY') AS requisitionDate,"
			+ " bloodComponentOrderMaster.bloodComponentOrderId AS requisitionNo,"
			+ " bloodComponentOrderMaster.unit AS bloodUnit,"
			+ " bloodComponentOrderMaster.patientId AS patientId, "
			+ " concat(coalesce(patient.firstName,'') ,' ', coalesce(patient.middleName,'') ,' ', coalesce(patient.lastName,'')) AS patientName, "
			+ " bloodComponentMaster.bloodComponentDesc AS bloodComponentDesc, "
			+ " bloodComponentMaster.bloodComponentId AS bloodComponentId "
			+ " FROM BloodComponentOrderMaster bloodComponentOrderMaster "
			+ " INNER JOIN bloodComponentOrderMaster.patientRegistration patient  "
			+ " INNER JOIN bloodComponentOrderMaster.bloodComponentMaster bloodComponentMaster  "
			+ " WHERE bloodComponentOrderMaster.organizationId= :organizationId "
			+ " AND bloodComponentOrderMaster.unitId= :unitId "
			+ " AND bloodComponentOrderMaster.patientId= :patientId ")
	
	
		
})

@Entity
@Table(name = "m_blood_component_order_master", schema = "blood_bank")
@SequenceGenerator(name = "m_blood_component_order_master_seq", sequenceName = "blood_bank.m_blood_component_order_master_seq", allocationSize = 1)
public class BloodComponentOrderMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_blood_component_order_master_seq")
	
	@Column(name = "blood_component_order_id")
	private Integer bloodComponentOrderId;
	
	@Column(name = "blood_component_id")
	private Integer bloodComponentId;
	
	@Column(name = "unit")
	private Integer unit;
	
	@Column(name = "requisition_date")
	private Date requisitionDate;
	
	@Column(name = "requisition_on_date")
	private Date requisitionOnDate;
	
	@Column(name = "procedure_order_id")
	private Integer procedureOrderId;
	
	@Column(name = "comments")
	private String comments;
	
	@Column(name = "instruction")
	private String instruction;
	
	@Column(name = "priority")
	private Integer priority;

	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "is_required")
	private Character isRequired;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "is_exported")
	private Boolean isExported;
	
	@Column(name = "is_requisition_closed")
	private Boolean isRequisitionClosed;
	
	@Column(name = "requisition_number")
	private String requisitionNumber;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="blood_component_id", insertable=false, updatable=false)
	private BloodComponentMaster bloodComponentMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="procedure_order_id", insertable=false, updatable=false)
	private ProcedureOrderMaster procedureOrderMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="procedure_order_id", insertable=false, updatable=false)
	private ServiceMaster serviceMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, updatable = false)
	private PatientRegistration patientRegistration;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="doctor_id", insertable=false, updatable=false)
	private DoctorMaster doctorMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="admission_id", insertable=false, updatable=false)
	private Admission admission;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="created_by", insertable=false, updatable=false)
	private UserMaster userMaster;
	
	public Integer getBloodComponentOrderId() {
		return bloodComponentOrderId;
	}

	public void setBloodComponentOrderId(Integer bloodComponentOrderId) {
		this.bloodComponentOrderId = bloodComponentOrderId;
	}

	public Integer getBloodComponentId() {
		return bloodComponentId;
	}

	public void setBloodComponentId(Integer bloodComponentId) {
		this.bloodComponentId = bloodComponentId;
	}

	public Integer getUnit() {
		return unit;
	}

	public void setUnit(Integer unit) {
		this.unit = unit;
	}

	public Date getRequisitionDate() {
		return requisitionDate;
	}

	public void setRequisitionDate(Date requisitionDate) {
		this.requisitionDate = requisitionDate;
	}

	public Date getRequisitionOnDate() {
		return requisitionOnDate;
	}

	public void setRequisitionOnDate(Date requisitionOnDate) {
		this.requisitionOnDate = requisitionOnDate;
	}

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Character getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(Character isRequired) {
		this.isRequired = isRequired;
	}

	public BloodComponentMaster getBloodComponentMaster() {
		return bloodComponentMaster;
	}

	public void setBloodComponentMaster(BloodComponentMaster bloodComponentMaster) {
		this.bloodComponentMaster = bloodComponentMaster;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	
	public ProcedureOrderMaster getProcedureOrderMaster() {
		return procedureOrderMaster;
	}

	public void setProcedureOrderMaster(ProcedureOrderMaster procedureOrderMaster) {
		this.procedureOrderMaster = procedureOrderMaster;
	}

	public ServiceMaster getServiceMaster() {
		return serviceMaster;
	}

	public void setServiceMaster(ServiceMaster serviceMaster) {
		this.serviceMaster = serviceMaster;
	}

	public PatientRegistration getPatientRegistration() {
		return patientRegistration;
	}

	public void setPatientRegistration(PatientRegistration patientRegistration) {
		this.patientRegistration = patientRegistration;
	}

	public Admission getAdmission() {
		return admission;
	}

	public void setAdmission(Admission admission) {
		this.admission = admission;
	}

	public Boolean getIsExported() {
		return isExported;
	}

	public void setIsExported(Boolean isExported) {
		this.isExported = isExported;
	}

	public Boolean getIsRequisitionClosed() {
		return isRequisitionClosed;
	}

	public void setIsRequisitionClosed(Boolean isRequisitionClosed) {
		this.isRequisitionClosed = isRequisitionClosed;
	}
	public DoctorMaster getDoctorMaster() {
		return doctorMaster;
	}

	public void setDoctorMaster(DoctorMaster doctorMaster) {
		this.doctorMaster = doctorMaster;
	}

	public String getRequisitionNumber() {
		return requisitionNumber;
	}

	public void setRequisitionNumber(String requisitionNumber) {
		this.requisitionNumber = requisitionNumber;
	}

}
