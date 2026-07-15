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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.taglibs.standard.extra.spath.Step;

import com.param.adt.admission.model.Admission;
import com.param.global.model.PatientRegistration;
import com.param.global.model.SystemMaster;
import com.param.global.model.SystemObervationMaster;
import com.param.global.model.SystemObervationPropertyMaster;
import com.param.opd.appointment.model.AppointmentMaster;

@NamedQueries({
	
	@NamedQuery(name="GET_GENERAL_EXAM_LIST",
			query=" SELECT 			generalExam.generalExamAppoMapperId as generalExamAppoMapperId, generalExam.systemId as systemId,  generalExam.descriptiveExamination as descriptiveExamination, "
					+ " 			sysMst.systemName as systemName, generalExam.remark as remarkObs ,"
				+ "					generalExam.systemObervationId as systemObervationId, systmObsMst.observationName as observationName, generalExam.admissionId as admissionId ,  generalExam.encounterId as encounterId ,"
				+ "					generalExam.systemObervationPropertyId as systemObervationPropertyId, sysObsrProMst.propertyName as propertyName, "
				+ "					generalExam.isNADValue as isNADValue, generalExam.remark as remark, generalExam.status as status,"
				+ "					generalExam.encounterId as encounterId, generalExam.patientId as patientId, "
				+ "					generalExam.unitId as unitId, generalExam.organizationId as organizationId,"
				+ "					generalExam.createdBy as createdBy, generalExam.updatedBy as updatedBy,"
				+ "					to_char(generalExam.createdDate,'DD-MM-YYYY')as createdDate, to_char(generalExam.updatedDate,'DD-MM-YYYY') as updatedDate "
				+ " FROM 			GeneralExaminationAppoMapper generalExam "
				+ " LEFT JOIN 		generalExam.systemObervationMaster systmObsMst "
				+ " LEFT JOIN 		generalExam.systemObervationPropertyMaster sysObsrProMst"
				+ " LEFT JOIN 		generalExam.systemMaster sysMst "
				+ " WHERE 			generalExam.patientId=:patientId "
				+ " AND 			generalExam.unitId=:unitId"
				+ " AND 			generalExam.status='A' "
				+ " AND 			generalExam.organizationId=:organizationId "
				+ " AND generalExam.typeId=:typeId "
//				+ " AND generalExam.isNADValue='Y' "
			),
	
	
	@NamedQuery(name="GET_SAVED_GENERAL_EXAM_LIST",
	query=" SELECT 			generalExam.generalExamAppoMapperId as generalExamAppoMapperId, generalExam.systemId as systemId, generalExam.descriptiveExamination as descriptiveExamination, "
		+ "					generalExam.systemObervationId as systemObervationId, generalExam.systemObervationPropertyId as systemObervationPropertyId, "
		+ "					generalExam.isNADValue as isNADValue, generalExam.remark as remark, generalExam.status as status,"
		+ "					generalExam.encounterId as encounterId, generalExam.patientId as patientId, "
		+ "					generalExam.unitId as unitId, generalExam.organizationId as organizationId,"
		+ "					generalExam.createdBy as createdBy, generalExam.updatedBy as updatedBy,"
		+ "					to_char(generalExam.createdDate,'DD-MM-YYYY')as createdDate, to_char(generalExam.updatedDate,'DD-MM-YYYY') as updatedDate "
		+ " FROM 			GeneralExaminationAppoMapper generalExam "
		+ " WHERE 			generalExam.patientId=:patientId "
//		+ " AND 			generalExam.unitId=:unitId"
		+ " AND 			generalExam.organizationId=:organizationId AND generalExam.typeId=:typeId "
	),
	
	@NamedQuery(name="CHECK_GENERAL_EXAMINATION_MAPPER_AVAILABLE",
	query=" SELECT 			generalExam.status as status , generalExam.typeId as typeId ,"
		+ "					generalExam.patientId as patientId, "
		+ "					generalExam.organizationId as organizationId "
		+ " FROM 			GeneralExaminationAppoMapper generalExam "
		+ " WHERE 			generalExam.patientId=:patientId "
		+ " AND 			generalExam.organizationId=:organizationId "
		+ " AND 			generalExam.status='A' "
		+ " AND generalExam.typeId=:typeId "
	),
	
	@NamedQuery(name="GET_EXAMINATION_LIST_HISTORY",
	query=" SELECT 			generalExam.generalExamAppoMapperId as generalExamAppoMapperId, generalExam.systemId as systemId,  generalExam.descriptiveExamination as descriptiveExamination, "
			+ " 			sysMst.systemName as systemName, generalExam.remark as remarkObs , generalExam.admissionId as admissionId ,  generalExam.encounterId as encounterId ,"
		+ "					generalExam.systemObervationId as systemObervationId, systmObsMst.observationName as observationName, "
		+ "					generalExam.systemObervationPropertyId as systemObervationPropertyId, sysObsrProMst.propertyName as propertyName, "
		+ "					generalExam.isNADValue as isNADValue, generalExam.remark as remark, generalExam.status as status,"
		+ "					generalExam.encounterId as encounterId, generalExam.patientId as patientId, "
		+ "					generalExam.unitId as unitId, generalExam.organizationId as organizationId,"
		+ "					generalExam.createdBy as createdBy, generalExam.updatedBy as updatedBy,"
		+ "                 to_char(generalExam.createdDate,'DD-MM-YYYY HH24:MI') as createdDateString, "
		+ "                 to_char(encounterMaster.encounterDate,'DD-MM-YYYY HH24:MI') as encounterDateString, "
		+ "                 encounterMaster.encounterNumber as encounterNumber, "
		+ "                 concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName, "
		+ "                 speciality.specialityName as specialityName, "
		+ "					to_char(generalExam.createdDate,'DD-MM-YYYY')as createdDate, to_char(generalExam.updatedDate,'DD-MM-YYYY') as updatedDate "
		+ " FROM 			GeneralExaminationAppoMapper generalExam "
		+ " LEFT JOIN 		generalExam.systemObervationMaster systmObsMst "
		+ " LEFT JOIN 		generalExam.systemObervationPropertyMaster sysObsrProMst"
		+ " LEFT JOIN 		generalExam.encounterMaster encounterMaster "
		+ " LEFT JOIN       encounterMaster.doctorMaster doc "
		+ " LEFT JOIN       encounterMaster.specialityMaster speciality "
		+ " LEFT JOIN 		generalExam.systemMaster sysMst "
		+ " WHERE 			generalExam.patientId=:patientId "
		+ " AND 			generalExam.unitId=:unitId"
		//+ " AND 			generalExam.status='I' "
		+ " AND 			generalExam.organizationId=:organizationId "
		+ " AND generalExam.typeId=:typeId "
		+ " order by  generalExam.generalExamAppoMapperId desc"
	),
	
	@NamedQuery(name="GET_EXAMINATION_LIST_HISTORY_BY_ADMISSION",
	query=" SELECT 			generalExam.generalExamAppoMapperId as generalExamAppoMapperId, generalExam.systemId as systemId,  generalExam.descriptiveExamination as descriptiveExamination, "
			+ " 			sysMst.systemName as systemName, generalExam.remark as remarkObs , generalExam.admissionId as admissionId ,  generalExam.encounterId as encounterId ,"
		+ "					generalExam.systemObervationId as systemObervationId, systmObsMst.observationName as observationName, "
		+ "					generalExam.systemObervationPropertyId as systemObervationPropertyId, sysObsrProMst.propertyName as propertyName, "
		+ "					generalExam.isNADValue as isNADValue, generalExam.remark as remark, generalExam.status as status,"
		+ "					generalExam.encounterId as encounterId, generalExam.patientId as patientId, "
		+ "					generalExam.unitId as unitId, generalExam.organizationId as organizationId,"
		+ "					generalExam.createdBy as createdBy, generalExam.updatedBy as updatedBy,"
		+ "                 to_char(generalExam.createdDate,'DD-MM-YYYY HH24:MI') as createdDateString, "
		+ "                 to_char(admission.createdDate,'DD-MM-YYYY HH24:MI') as encounterDateString, "
		+ "                 admission.admissionNumber as encounterNumber, "
		+ "                 concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName, "
		+ "                 speciality.specialityName as specialityName, "
		+ "					to_char(generalExam.createdDate,'DD-MM-YYYY')as createdDate, to_char(generalExam.updatedDate,'DD-MM-YYYY') as updatedDate "
		+ " FROM 			GeneralExaminationAppoMapper generalExam "
		+ " LEFT JOIN 		generalExam.systemObervationMaster systmObsMst "
		+ " LEFT JOIN 		generalExam.systemObervationPropertyMaster sysObsrProMst"
		+ " LEFT JOIN 		generalExam.admission admission "
		+ " LEFT JOIN       admission.doctorMaster doc "
		+ " LEFT JOIN       admission.specialityMaster speciality "
		+ " LEFT JOIN 		generalExam.systemMaster sysMst "
		+ " WHERE 			generalExam.patientId=:patientId "
		+ " AND 			generalExam.unitId=:unitId"
		//+ " AND 			generalExam.status='I' "
		+ " AND 			generalExam.organizationId=:organizationId "
		+ " AND generalExam.typeId=:typeId "
		+ " order by  generalExam.generalExamAppoMapperId desc"
	),
})


@Entity
@Table(name="t_general_examination_appo_mapper", schema="patient")
@SequenceGenerator(name="t_general_examination_appo_mapper_seq", sequenceName="patient.t_general_examination_appo_mapper_seq", allocationSize=1)
public class GeneralExaminationAppoMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_general_examination_appo_mapper_seq")
	@Column(name="general_exam_appo_mapper_id")
	private Integer generalExamAppoMapperId;
	
	@Column(name="system_id")
	private Integer systemId;
	
	@Column(name="system_obervation_id")
	private Integer systemObervationId;
	
	@Column(name="system_obervation_property_id")
	private Integer systemObervationPropertyId;
	
	@Column(name="is_nad_status")
	private char isNADValue;
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="status")
	private char status;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="type_id")
	private Integer typeId;
	
	@Column(name="admission_id")
	private Integer admissionId;

	@Column(name="descriptive_examination")
	private String descriptiveExamination;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "encounter_id", insertable = false, nullable = false, updatable = false)
	private EncounterMaster encounterMaster;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "system_obervation_id", insertable = false, nullable = false, updatable = false)
	private SystemObervationMaster systemObervationMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "system_obervation_property_id", insertable = false, nullable = false, updatable = false)
	private SystemObervationPropertyMaster systemObervationPropertyMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "system_id", insertable = false, nullable = false, updatable = false)
	private SystemMaster systemMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, nullable = false, updatable = false)
	private PatientRegistration patientRegistration;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admission_id", insertable = false, nullable = false, updatable = false)
	private Admission admission;
	
	
	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public EncounterMaster getEncounterMaster() {
		return encounterMaster;
	}

	public void setEncounterMaster(EncounterMaster encounterMaster) {
		this.encounterMaster = encounterMaster;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getGeneralExamAppoMapperId() {
		return generalExamAppoMapperId;
	}

	public void setGeneralExamAppoMapperId(Integer generalExamAppoMapperId) {
		this.generalExamAppoMapperId = generalExamAppoMapperId;
	}


	public Integer getSystemId() {
		return systemId;
	}

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}

	public Integer getSystemObervationId() {
		return systemObervationId;
	}

	public void setSystemObervationId(Integer systemObervationId) {
		this.systemObervationId = systemObervationId;
	}

	public Integer getSystemObervationPropertyId() {
		return systemObervationPropertyId;
	}

	public void setSystemObervationPropertyId(Integer systemObervationPropertyId) {
		this.systemObervationPropertyId = systemObervationPropertyId;
	}

	
	public char getIsNADValue() {
		return isNADValue;
	}

	public void setIsNADValue(char isNADValue) {
		this.isNADValue = isNADValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public SystemObervationMaster getSystemObervationMaster() {
		return systemObervationMaster;
	}

	public void setSystemObervationMaster(
			SystemObervationMaster systemObervationMaster) {
		this.systemObervationMaster = systemObervationMaster;
	}

	public SystemObervationPropertyMaster getSystemObervationPropertyMaster() {
		return systemObervationPropertyMaster;
	}

	public void setSystemObervationPropertyMaster(
			SystemObervationPropertyMaster systemObervationPropertyMaster) {
		this.systemObervationPropertyMaster = systemObervationPropertyMaster;
	}

	public SystemMaster getSystemMaster() {
		return systemMaster;
	}

	public void setSystemMaster(SystemMaster systemMaster) {
		this.systemMaster = systemMaster;
	}

	public PatientRegistration getPatientRegistration() {
		return patientRegistration;
	}

	public void setPatientRegistration(PatientRegistration patientRegistration) {
		this.patientRegistration = patientRegistration;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public String getDescriptiveExamination() {
		return descriptiveExamination;
	}

	public void setDescriptiveExamination(String descriptiveExamination) {
		this.descriptiveExamination = descriptiveExamination;
	}
	
	
}
