package com.param.adt.admission.model;

import java.util.Date;

import javax.persistence.Column;
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
@NamedQueries({
	
	@NamedQuery(name="GET_MEWS_MAPPER_LIST",
		query="SELECT mewsMapper.mewsMapperId AS mewsMapperId, mewsMapper.patientId AS patientId, "
					+ " mewsMapper.totalScore AS totalScore, to_char(mewsMapper.createdDate, 'DD/MM/YYYY')as createdDate, "
					+ " to_char(mewsMapper.createdDate, 'HH:mm:ss')as createdTime, mewsMapper.organizationId AS organizationId,"
					+ " mewsMapper.unitId AS unitId, mewsMapper.status AS status"
					+ " FROM MewsMapper mewsMapper"
					+ " WHERE  mewsMapper.organizationId= :organizationId "
					+ " AND mewsMapper.patientId= :patientId"
					+ " AND mewsMapper.unitId= :unitId"
					+ " ORDER BY mewsMapper.mewsMapperId asc"
					),
	
	@NamedQuery(name="GET_COUNT_ON_MEWS_MAPPER",
	query="SELECT mewsMapper.mewsMapperId AS mewsMapperId, mewsMapper.patientId AS patientId, "
				+ " mewsMapper.totalScore AS totalScore, to_char(mewsMapper.createdDate, 'DD/MM/YYYY')as createdDate, "
				+ " to_char(mewsMapper.createdDate, 'HH:mm:ss')as createdTime, mewsMapper.organizationId AS organizationId,"
				+ " mewsMapper.unitId AS unitId, mewsMapper.status AS status"
				+ " FROM MewsMapper mewsMapper"
				+ " WHERE mewsMapper.mewsMapperId=(select max(mews.mewsMapperId) AS mewsMapperId"
				+ " from MewsMapper mews "
				+ " where mews.organizationId= :organizationId "
				+ " AND mews.patientId= :patientId"
				+ " AND mews.unitId= :unitId)"
				+ " ORDER BY mewsMapper.mewsMapperId asc"
				),
})

@NamedNativeQueries({
	@NamedNativeQuery(name="GET_LIST_OF_ALLERGY",
	query="SELECT alrgyMap.allergy_id as \"allergyId\" , "
					+ "alrgyMap.allergy_patient_mapp_id as \"allergyPatientMappId\" , "
					+ "alrgyMap.patient_id as \"patientId\", "
					+ "alrgyMap.allergy_type_id as \"allergyTypeId\" ,"
					+ "alrgyMap.type as \"type\" , "
					+ "alrgyMap.nature_of_reaction as \"natureOfReaction\" ,"
					+ "alrgyMap.comments as \"comments\" ,"
					+ "alrgyMap.severity_id as \"severityId\" ,"
					+ "servtyMst.severity_desc as \"severityDesc\" ,"
					+ "alrgyMap.remark as \"remark\" , "
					+ "alrgyMap.is_no_known_allergies as \"isNoKnownAllergies\" ,"
					+ "alrgyMst.allergy_name as \"allergyName\" ,"
					+ "alrgyTypeMst.allergy_type_name as \"allergyTypeName\" ,"
					+ "alrgyMap.is_reviewed_flag as \"isReviewedFlag\" ,"
					+ "genericMst.id as \"genericId\" ,"
					+ "genericMst.name as \"genericName\"  "
					+ "FROM patient.t_allergy_patient_mapper alrgyMap "
					+ "LEFT JOIN 	emr.m_allergy_master alrgyMst on alrgyMst.allergy_id=alrgyMap.allergy_id  "
					+ "LEFT JOIN 	public.m_generic genericMst on genericMst.id=alrgyMap.generic_id  "
					+ "LEFT JOIN 	emr.m_allergy_type_master alrgyTypeMst on alrgyTypeMst.allergy_type_id=alrgyMap.allergy_type_id  "
					+ "LEFT JOIN 	emr.m_severity_master servtyMst on servtyMst.severity_id=alrgyMap.severity_id  "
					+ "WHERE alrgyMap.patient_id=:patientId "
					+ "AND alrgyMap.unit_id=:unitId "
					+ "AND alrgyMap.organization_id=:organizationId "
					//+ " AND alrgyMap.status = 'A' "
				),
	
	@NamedNativeQuery(name="GET_LIST_OF_ORDERED_PROCEDURE",
	query="select spl.speciality_name as \"specialityName\" , "
			+ "pom.procedure_order_id as \"procedureOrderId\", "
			+ "pom.urgency as \"urgency\", "
			+ "prm.patient_id as \"patientId\", "
			+ "service_standard_name \"procedureName\", "
			+ " concat(coalesce(dm.first_name,''),' ',coalesce(dm.middle_name,''),' ',coalesce(dm.last_name,'')) as \"doctorName\", "
			+ "to_char(pom.prefered_date_time,'dd/mm/yyyy') as \"preferedDateTime\","
			+ "to_char(pom.prefered_time,'HH:MI') as \"preferedTime\", "
			+ "pom.request_reason as \"requestReason\","
			+ "prm.created_by as \"createdBy\","
			+ "pom.is_icu_required as \"isIcuRequired\","
			+ "pom.is_primary_secondary as \"isPrimarySecondary\","
			+ "pom.is_blood_component_required as  \"isBloodComponentRequired\","
			+ "pom.is_pac_required as \"isPacRequired\" "
			+"FROM otims.t_procedure_req_mst prm "
			+"inner join otims.t_procedure_order_master pom on prm.pro_req_id=pom.pro_req_id "
			+"inner join service.m_service_master ser on ser.service_master_id= pom.service_id "
			+"inner join doctor.m_speciality_master spl on spl.speciality_id=ser.speciality_id "
			+"inner join doctor.m_doctor_master dm on dm.user_id=prm.created_by "
			+"where  pom.status ='A' and prm.patient_id=:patientId "
			+ "AND prm.unit_id=:unitId "
			+ "AND prm.org_id=:organizationId "
		),
})
@Entity
@Table(name = "t_mews_mapper", schema = "adt")
@SequenceGenerator(name = "t_mews_mapper_seq", sequenceName = "adt.t_mews_mapper_seq", allocationSize = 1)
public class MewsMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_mews_mapper_seq")
	
	@Column(name = "mews_mapper_id")
	private Integer mewsMapperId;
	
	@Column(name = "patient_id ")
	private Integer patientId ;
	
	@Column(name = "systolic_blood_pressure_id ")
	private Integer systolicBloodPressureId ;
	
	@Column(name = "heart_rate_id ")
	private Integer heartRateId ;
	
	@Column(name = "temperature_id")
	private Integer temperatureId ;
	
	@Column(name = "avpu_score_id")
	private Integer avpuScoreId ;
	
	@Column(name = "respiratory_rate_id")
	private Integer respiratoryRateId ;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "total_score")
	private Integer totalScore;
	
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

	

	public Integer getMewsMapperId() {
		return mewsMapperId;
	}

	public void setMewsMapperId(Integer mewsMapperId) {
		this.mewsMapperId = mewsMapperId;
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

	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
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

	public Integer getSystolicBloodPressureId() {
		return systolicBloodPressureId;
	}

	public void setSystolicBloodPressureId(Integer systolicBloodPressureId) {
		this.systolicBloodPressureId = systolicBloodPressureId;
	}

	public Integer getHeartRateId() {
		return heartRateId;
	}

	public void setHeartRateId(Integer heartRateId) {
		this.heartRateId = heartRateId;
	}

	public Integer getTemperatureId() {
		return temperatureId;
	}

	public void setTemperatureId(Integer temperatureId) {
		this.temperatureId = temperatureId;
	}

	public Integer getAvpuScoreId() {
		return avpuScoreId;
	}

	public void setAvpuScoreId(Integer avpuScoreId) {
		this.avpuScoreId = avpuScoreId;
	}

	public Integer getRespiratoryRateId() {
		return respiratoryRateId;
	}

	public void setRespiratoryRateId(Integer respiratoryRateId) {
		this.respiratoryRateId = respiratoryRateId;
	}
	
	
}
