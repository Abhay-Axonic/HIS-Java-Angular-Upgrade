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

import com.param.adt.admission.model.Admission;
import com.param.adt.admission.model.AdmissionDetails;
import com.param.adt.master.global.model.UserMaster;
import com.param.entity.model.master.Frequency;
import com.param.global.model.DoctorMaster;
import com.param.global.model.NursingOrderMaster;
import com.param.global.model.PatientRegistration;
@NamedQueries({
	
	@NamedQuery(name="GET_NURSING_ORDER_MAPPER_BY_ID",
		query="SELECT nursingOrderMapper.nursingOrderMapperId AS nursingOrderMapperId, nursingOrderMapper.nursingOrderId AS nursingOrderId, "
				    + " nursingOrderMapper.frequencyId AS frequencyId, "  + " nursingOrderMapper.instruction AS instruction, "
				    + 	" to_char(nursingOrderMapper.startDate, 'dd/mm/yyyy') AS startDate, "
				    + " to_char(nursingOrderMapper.stopDate, 'dd/mm/yyyy') AS stopDate, " 
				    + " nursingOrderMapper.patientId AS patientId, "
				    + " nursingOrderMapper.admissionId AS admissionId, "
				    + " nursingOrderMapper.doctorId AS doctorId, "
					+ " nursingOrderMapper.organizationId AS organizationId, "
					+ "nursingOrderMapper.unitId AS unitId, "
					+ "nursingOrderMapper.status AS status, "
					+ " frequencyMaster.description AS frequency, "
					+ " nurMst.nursingOrderDesc AS nursingOrderDesc"
					+ " FROM NursingOrderMapper nursingOrderMapper "
					+ " INNER JOIN nursingOrderMapper.nursingOrderMaster nurMst"
					+ " LEFT JOIN nursingOrderMapper.frequencyMaster frequencyMaster"
					+ " WHERE  nursingOrderMapper.organizationId= :organizationId "
					+ " AND nursingOrderMapper.unitId= :unitId"
					+ " AND nursingOrderMapper.nursingOrderMapperId= :nursingOrderMapperId"
					+ " ORDER BY nursingOrderMapper.nursingOrderMapperId asc"
					),
	
	@NamedQuery(name="GET_NURSING_ORDER_MAPPER_LIST",
	query="SELECT nursingOrderMapper.nursingOrderMapperId AS nursingOrderMapperId, nursingOrderMapper.nursingOrderId AS nursingOrderId, "
			  + " nursingOrderMapper.frequencyId AS frequencyId, "  + " nursingOrderMapper.instruction AS instruction, "
			    + 	" to_char(nursingOrderMapper.startDate, 'dd/mm/yyyy') AS startDate, "
			    + " to_char(nursingOrderMapper.stopDate, 'dd/mm/yyyy') AS stopDate, " 
			    + " nursingOrderMapper.patientId AS patientId, "
			    + " nursingOrderMapper.admissionId AS admissionId, "
			    + " nursingOrderMapper.doctorId AS doctorId, "
			    + " nursingOrderMapper.checkStatus AS checkStatus, "
				+ " nursingOrderMapper.organizationId AS organizationId, "
				+ " nursingOrderMapper.unitId AS unitId, "
				+ " nursingOrderMapper.status AS status, "
				+ " frequencyMaster.description AS frequency, "
				+ " nurMst.nursingOrderDesc AS nursingOrderDesc"
				+ " FROM NursingOrderMapper nursingOrderMapper"
				+ " INNER JOIN nursingOrderMapper.nursingOrderMaster nurMst"
				+ " LEFT JOIN nursingOrderMapper.frequencyMaster frequencyMaster"
				+ " WHERE  nursingOrderMapper.organizationId= :organizationId "
				+ " AND nursingOrderMapper.unitId= :unitId"
				+ " AND nursingOrderMapper.patientId= :patientId"
				+ " ORDER BY nursingOrderMapper.nursingOrderMapperId asc"
				),
	
	@NamedQuery(name="GET_NURSING_ORDER_MAPPER_LIST_BY_ADMISSION_ID",
	query="SELECT nursingOrderMapper.nursingOrderMapperId AS nursingOrderMapperId, nursingOrderMapper.nursingOrderId AS nursingOrderId, "
			  + " nursingOrderMapper.frequencyId AS frequencyId, "  + " nursingOrderMapper.instruction AS instruction, "
			    + 	" to_char(nursingOrderMapper.startDate, 'dd/mm/yyyy') AS startDate, "
			    + " to_char(nursingOrderMapper.stopDate, 'dd/mm/yyyy') AS stopDate, " 
			    + " to_char(nursingOrderMapper.createdDate, 'DD/MM/YYYY') AS createdDate, "
			    + " nursingOrderMapper.patientId AS patientId, "
			    + " nursingOrderMapper.admissionId AS admissionId, "
			    + " nursingOrderMapper.doctorId AS doctorId, "
			    + " nursingOrderMapper.checkStatus AS checkStatus, "
				+ " nursingOrderMapper.organizationId AS organizationId, "
				+ " nursingOrderMapper.unitId AS unitId, "
				+ " nursingOrderMapper.status AS status, "
				+ " frequencyMaster.description AS frequency, "
				+ " (case " 
				+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) " 
				+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) " 
				+ " else 'Admin' end) as userName,"
				+ " nurMst.nursingOrderDesc AS nursingOrderDesc"
				+ " FROM NursingOrderMapper nursingOrderMapper"
				+ " INNER JOIN nursingOrderMapper.nursingOrderMaster nurMst"
				+ " LEFT JOIN nursingOrderMapper.frequencyMaster frequencyMaster "
				+ " LEFT JOIN nursingOrderMapper.userMaster um " 
				+ " LEFT JOIN um.employeeMastersList empmst " 
				+ " LEFT JOIN um.doctorMastersList  docmst "
				+ " WHERE  nursingOrderMapper.organizationId= :organizationId "
				+ " AND nursingOrderMapper.unitId= :unitId"
				+ " AND nursingOrderMapper.patientId= :patientId"
				+ " AND nursingOrderMapper.admissionId= :admissionId"
				+ " ORDER BY nursingOrderMapper.nursingOrderMapperId asc"
				),
	
	@NamedQuery(name="GET_ACTIVE_NURSING_ORDER_MAPPER_LIST",
	query="SELECT nursingOrderMapper.nursingOrderMapperId AS nursingOrderMapperId, nursingOrderMapper.nursingOrderId AS nursingOrderId,"
			  + " nursingOrderMapper.frequencyId AS frequencyId, "  + " nursingOrderMapper.instruction AS instruction, "
			    + 	" to_char(nursingOrderMapper.startDate, 'dd/mm/yyyy') AS startDate, "
			    + " to_char(nursingOrderMapper.stopDate, 'dd/mm/yyyy') AS stopDate, " 
			    + " nursingOrderMapper.patientId AS patientId, "
			    + " nursingOrderMapper.admissionId AS admissionId, "
			    + " nursingOrderMapper.doctorId AS doctorId, "
				+ " nursingOrderMapper.organizationId AS organizationId, "
				+ " nursingOrderMapper.unitId AS unitId, "
				+ " nursingOrderMapper.status AS status, "
				+ " frequencyMaster.description AS frequency, "
				+ " nurMst.nursingOrderDesc AS nursingOrderDesc"
				+ " FROM NursingOrderMapper nursingOrderMapper"
				+ " INNER JOIN nursingOrderMapper.nursingOrderMaster nurMst"
				+ " LEFT JOIN nursingOrderMapper.frequencyMaster frequencyMaster"
				+ " WHERE  nursingOrderMapper.organizationId= :organizationId "
				+ " AND nursingOrderMapper.unitId= :unitId"
				+ " AND nursingOrderMapper.patientId= :patientId"
				+ " AND nursingOrderMapper.status= 'A'"
				+ " ORDER BY nursingOrderMapper.nursingOrderMapperId asc"
				),
	
	@NamedQuery(name="GET_LIST_OF_PATIENT_OF_NURSING_ORDERS",
	query="SELECT  distinct(nursingOrderMapper.patientId) AS patientId, "
//			    + " nursingOrderMapper.nursingOrderMapperId as nursingOrderMapperId, "
			    + "nursingOrderMapper.remark as remark, "
			    + "nursingOrderMapper.nurseStatus as nurseStatus, "
			    + " nursingOrderMapper.admissionId AS admissionId, "
			    + " nursingOrderMapper.doctorId AS doctorId, "
				+ " nursingOrderMapper.organizationId AS organizationId, "
				+ " nursingOrderMapper.unitId AS unitId, "
				+ " nursingOrderMapper.status AS status, "
				+ " concat(coalesce(prefix.prefixCode,'') ,' ',coalesce(patReg.firstName) ,' ', coalesce(patReg.middleName) ,' ', coalesce(patReg.lastName)) as patientName, "
				+ " concat(coalesce(doctorMaster.firstName) ,' ', coalesce(doctorMaster.middleName) ,' ', coalesce(doctorMaster.lastName)) as doctorName, "
				+ " patReg.uhidNumber as uhidNumber, "
				+ " wrdMst.wardName AS wardName, "
				+ " roomMst.roomDesc AS roomDesc,"
				+ " bedMst.bedDesc AS bedDesc,"
				+ " floorMst.floorName AS floorName "
				+ " FROM NursingOrderMapper nursingOrderMapper"
				+ " INNER JOIN nursingOrderMapper.patientRegistration patReg"
				+ " INNER JOIN patReg.prefixMaster as prefix "
				+ " INNER JOIN nursingOrderMapper.admissionObj.admissionDetailsList admDtls "
				//+ " INNER JOIN nursingOrderMapper.admissionDetails  admDtls "
				+ " INNER JOIN admDtls.wardMaster wrdMst"
				+ " INNER JOIN admDtls.roomMaster roomMst"
				+ " INNER JOIN admDtls.floorMaster floorMst"
				+ " INNER JOIN admDtls.bedMaster bedMst"
				+" INNER JOIN nursingOrderMapper.doctorMaster doctorMaster"
				+ " WHERE  nursingOrderMapper.organizationId= :organizationId "
				+ " AND nursingOrderMapper.unitId= :unitId"
				+ " AND admDtls.status='A' "
//				+ " ORDER BY nursingOrderMapper.nursingOrderMapperId asc"
				),
})
@NamedNativeQueries({
						@NamedNativeQuery(name="GET_PENDING_NUR_ORD_LIST_BY_PATIENT_ID", 
								query="select" + 
										"	pnd.pending_nur_ord_administration_id as \"pendingNurOrdAdministrationId\"," + 
										"	pnd.nursing_order_mapper_id as \"nursingOrderMapperId\"," + 
										"	to_char(" + 
										"		pnd.pending_nur_ord_administration_date," + 
										"		'YYYY-MM-DD'" + 
										"	) as \"pendingNurOrdAdministrationDate\"," + 
										"	to_char(" + 
										"		pnd.pending_nur_ord_administration_time," + 
										"		'HH24:MI'" + 
										"	) as \"pendingNurOrdAdministrationTime\"," + 
										"	to_char(" + 
										"		pnd.administration_time," + 
										"		'HH24:MI'" + 
										"	) as \"administrationTime\"," + 
										"	pnd.reason_id as \"reasonId\"," + 
										"	rsn.reason_desc as \"reasonDesc\"," + 
										"	pnd.remark as \"remark\"" + 
										" from" + 
										"	adt.t_pending_nursing_order_administration pnd" + 
										" inner join adt.m_reason_master rsn on" + 
										"	rsn.reason_master_id = pnd.reason_id" + 
										" where" + 
										"	pnd.patient_id =:patientId") 
})
@Entity
@Table(name = "t_nursing_order_mapper", schema = "adt")
@SequenceGenerator(name = "t_nursing_order_mapper_seq", sequenceName = "adt.t_nursing_order_mapper_seq", allocationSize = 1)
public class NursingOrderMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_nursing_order_mapper_seq")
	
	@Column(name = "nursing_order_mapper_id")
	private Integer nursingOrderMapperId;
	
	@Column(name = "nursing_order_id")
	private Integer nursingOrderId;
	
	@Column(name = "frequency_id")
	private Integer frequencyId;
	
	@Column(name = "instruction")
	private String instruction;
	
	@Column(name = "start_date")
	private Date startDate;
	
	@Column(name = "stop_date")
	private Date stopDate;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
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
	
	@Column(name = "check_status")
	private Boolean checkStatus;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "nurse_status")
	private Character nurseStatus;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="nursing_order_id", insertable=false, updatable=false)
	private NursingOrderMaster nursingOrderMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="patient_id", insertable=false, updatable=false)
	private PatientRegistration patientRegistration;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="frequency_id", insertable=false, updatable=false)
	private Frequency frequencyMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="admission_id", insertable=false, updatable=false)
	private AdmissionDetails admissionDetails;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="admission_id", insertable=false, updatable=false)
	private Admission admissionObj;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="doctor_id", insertable=false, updatable=false)
	private DoctorMaster doctorMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="created_by", insertable=false, updatable=false)
	private UserMaster userMaster;
	
	public Integer getNursingOrderMapperId() {
		return nursingOrderMapperId;
	}

	public void setNursingOrderMapperId(Integer nursingOrderMapperId) {
		this.nursingOrderMapperId = nursingOrderMapperId;
	}

	public Integer getNursingOrderId() {
		return nursingOrderId;
	}

	public void setNursingOrderId(Integer nursingOrderId) {
		this.nursingOrderId = nursingOrderId;
	}

	public Integer getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Integer frequencyId) {
		this.frequencyId = frequencyId;
	}
	
	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStopDate() {
		return stopDate;
	}

	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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

	public Boolean getCheckStatus() {
		return checkStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Character getNurseStatus() {
		return nurseStatus;
	}

	public void setNurseStatus(Character nurseStatus) {
		this.nurseStatus = nurseStatus;
	}

	public void setCheckStatus(Boolean checkStatus) {
		this.checkStatus = checkStatus;
	}

	
}
