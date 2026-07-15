package com.param.entity.lis.micro;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.entity.lis.common.LocalTimeConverter;
import com.param.entity.lis.common.LocalTimeStampConvertor;
import com.param.global.common.InstantConverter;


@NamedNativeQueries({
	
	@NamedNativeQuery(name="GET_INCUBATION_OBSERVATION_LIST",query=    " SELECT  "
			 +" micro_result_entry.microbio_result_entry_id AS  \"microbioResultEntryId\", "
			 +" micro_result_entry.is_incubation_complete AS  \"isIncubationComplete\", "
			 +" micro_result_entry.is_macro_exa_complete AS  \"isMacro_exaComplete\", "
			 +" micro_result_entry.is_micro_exa_complete AS  \"isMicro_exaComplete\", "
			 +" to_char(micro_result_entry.incubation_start_time,'HH12:MI:SS') AS  \"incuStartTime\", "
			 +" to_char(micro_result_entry.incubation_start_date,'MM-DD-YYYY') AS  \"incuStartDate\", "
			 +"	vistmst.visit_type_name AS \"visitType\", "
			 +"	lbsampledtls.patient_id AS \"patientId\", "
			 +"	lbsampledtls.doctor_id AS \"doctorId\", "
			 +"	lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\", "
			 +"	lbsampledtls.sample_status_id AS \"sampleStatusId\", "
			 +"	lbsampledtls.is_alliquote_req AS \"isAlliquoteReq\", "
			 +"	lbsampledtls.sample_recollect_flag AS \"sampleRecollectFlag\", "
			 +"	pati_mst.uhid_number AS uhid, "
			 +"	test_mst.test_desc AS \"testDesc\", "
			 +"	pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || pati_mst.last_name || ' ' || calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code AS \"patientDetails\", "
			 +"	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name AS \"doctorDetails\", "
			 +"	sample_mst.sample_desc AS \"sampleDesc\", "
			 +"	sample_mst.sample_id AS \"sampleId\", "
			 +"	pri_mst.priority_name AS \"priorityName\", "
			 +"	pri_mst.color_code AS \"colorCode\", "
			 +"	wardmast.ward_code AS \"wardCode\", "
			 +"	bedmst.bed_number AS \"bedNumber\", "
			 +"	lbsampledtls.sample_barcode AS \"sampleBarcode\", "
			 +"	lab.generate_sample_no( "
			 +"		cast(lbsampledtls.sample_no AS text),'SM-',	7) AS \"labSampleNo\" "
			 +"FROM "
			 +"	lab.t_lab_sample_dtls lbsampledtls "
			 +"INNER JOIN lab.m_test_master test_mst ON "
			 +"	lbsampledtls.test_id = test_mst.test_id "
			 +"INNER JOIN lab.m_sample_master sample_mst ON "
			 +"	sample_mst.sample_id = test_mst.sample_id	 "
			 +"INNER JOIN patient.t_patient_registration pati_mst ON "
			 +"	lbsampledtls.patient_id = pati_mst.patient_id "
			 +"INNER JOIN doctor.m_doctor_master doct_mst ON "
			 +"	lbsampledtls.doctor_id = doct_mst.doctor_id "
			 +"INNER JOIN m_prefix_master pre_mst ON "
			 +"	pati_mst.prefix_id = pre_mst.prefix_id "
			 +"INNER JOIN m_gender_master gen_mst ON "
			 +"	pati_mst.gender_id = gen_mst.gender_id "
			 +"INNER JOIN lab.m_priority_master pri_mst ON "
			 +"	lbsampledtls.priority_id = pri_mst.priority_id "
			 +"INNER JOIN doctor.m_sub_speciality_master subspe_mst ON "
			 +"	subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id "
			 +"INNER JOIN lab.m_container_master contai_mst ON "
			 +"	contai_mst.container_id = lbsampledtls.container_id "
			 +"INNER JOIN lab.t_lab_sample_master labsammst ON "
			 +"	labsammst.lab_sample_id = lbsampledtls.lab_sample_id "
			 +"INNER JOIN m_visit_type_master vistmst ON "
			 +"	vistmst.visit_type_id = labsammst.visit_type_id "
			 +"INNER JOIN public.t_order_details ord_details ON "
			 +"	ord_details.order_details_id = lbsampledtls.order_details_id "
			 +"LEFT JOIN adt.m_ward_master wardmast ON "
			 +"	ord_details.ward_id = wardmast.ward_id "
			 +"LEFT JOIN adt.m_bed_master bedmst ON "
			 +"	ord_details.bed_id = bedmst.bed_id "
			 +"LEFT JOIN lab.t_microbio_result_entry micro_result_entry ON "
			 +"	micro_result_entry.lab_sample_dtls_id = lbsampledtls.lab_sample_dtls_id	 "
			 +"WHERE "
			 +"	lbsampledtls.sample_status_id IN(:sampleStatusIds)  "
			 +"	AND lbsampledtls.org_id = :orgId "
			 +"	AND lbsampledtls.org_unit_id = :orgUnitId "
			 +"	AND lbsampledtls.dept_id = :deptId  "
			 +"	AND lbsampledtls.sub_dept_id = :subDeptId  "
			 +" AND	test_mst.is_outsourced = 'N' "
			 + "  and ord_details.ord_cancelled = 'N' "
			+ "  and ord_details.status = 'A' "
			 +"ORDER BY "
			 +"	lbsampledtls.priority_id, "
			 +"	lbsampledtls.created_date  "),
	
	@NamedNativeQuery(name="GET_INCUBATION_OBSERVATION_LIST_COUNT",query= " SELECT  "
			 +"	COUNT(*)  "
			 +" FROM  "
			 +"	lab.t_lab_sample_dtls lbsampledtls  "
			 +" INNER JOIN lab.m_test_master test_mst ON "
			 +"	lbsampledtls.test_id = test_mst.test_id "
			 +" WHERE  "
			 +"	lbsampledtls.sample_status_id IN(:sampleStatusIds)  "
			 +"	AND lbsampledtls.org_id = :orgId  "
			 +"	AND lbsampledtls.org_unit_id = :orgUnitId "
			 +"	AND lbsampledtls.dept_id = :deptId  "
			 +"	AND lbsampledtls.sub_dept_id = :subDeptId  "
			 +" AND	test_mst.is_outsourced = 'N' "),
			 
	 @NamedNativeQuery(name = "GET_SENSITIVITY_TESTING_LIST", query = " SELECT   "
	      + "  visit_type_master.visit_type_code AS \"visitType\",   "
	      + "  panel_master.panel_code AS \"panelCode\",   "
	      + "  panel_master.panel_id AS \"panelId\",   "
	      + "  lbsampledtls.gender_id AS \"genderId\",   "
	      + "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\",   "
	      + " test_mst.sample_id AS \"sampleId\", "
	      + "  lbsampledtls.sample_recollect_flag AS \"sampleRecollectFlag\",   "
	      + "  lbsampledtls.sample_recollect_against_id AS \"sampleRecollectAgainstId\",   "
	      + "  pati_mst.uhid_number AS uhid,   "
	      + "  pati_mst.image_path AS \"imagePath\",   "
	      + "  sample_mst.sample_no AS \"labSampleNo\",   "
          + "    bill_mst.bill_number as \"billNumber\", " 
	      + "  test_mst.test_desc AS \"testDesc\",   "
	      + "  order_master.admission_id AS \"visitAdmId\",   "
	      + "  order_master.visit_type_id AS \"visitTypeId\",   "
	      + "  lbsampledtls.dept_id AS \"deptId\",   " + " lbsampledtls.test_id AS \"testId\",   "
	      + "  test_mst.test_type AS \"testType\",   "
	      + "  lbsampledtls.sub_dept_id AS \"subDeptId\",   "
	      + "  lbsampledtls.patient_id AS \"patientId\",   "
	      + "  lbsampledtls.order_details_id AS \"orderDetailsId\",   "
	      + "  pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name)||' ' || pati_mst.last_name || ' ' || calculate_age(pati_mst.dob)  || '/' || gen_mst.gender_code AS \"patientDetails\",   "
	      + "  CAST(lbsampledtls.patient_visit_age AS DATE) - CAST(pati_mst.dob AS DATE) AS \"patientAgeDays\",   "
	      + "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' AS \"doctorDetails\",   "
	      + "  doct_mst.doctor_id AS \"doctorId\","
	      + "  lbsampledtls.sample_barcode AS \"sampleBarcode\",   "
	      + "  order_details.created_date AS \"orderDateTime\",   "
	      + "  lbsampledtls.sample_collection_datetime AS \"sampleCollectionDatetime\" ,  "
	      + "  to_char(order_details.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"orderDateTimeString\",   "
	      + "  to_char(lbsampledtls.sample_collection_datetime, 'DD/MM/YYYY HH24:MI:SS') AS \"sampleCollectionDatetimeString\" ,  "
	      + "  wardmast.ward_code AS \"wardCode\" ,  " 
	      + "  bedmst.bed_code AS \"bedNumber\",   "
	      + "  test_mst.test_code AS \"testCode\" ,  "
	      + "  samp_mst.sample_desc AS \"sampleType\" ,  "
	      + "  sensitivity_result_mst.sensitivity_result_id AS \"sensitivityResultId\" ,  " 
	      +" (case when order_master.encounter_id > 0 then " +
	      " ( tpsd.payment_entitlement_id ) " +
	      " else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
	      + " stsMstr.status_name  as \"statusName\", "
	      + " tltr.lab_test_res_id  as \"labTestResId\" , "
	      + "order_details.is_dummy_service as \"isDummyService\" "
	      + "FROM   "
	      + "  lab.t_lab_sample_dtls lbsampledtls   "
	      + "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id   "
	      + "  INNER JOIN public.t_order_master order_master ON order_master.order_id = order_details.order_id   "
	      + "  INNER JOIN lab.t_lab_sample_master sample_mst ON lbsampledtls.lab_sample_id = sample_mst.lab_sample_id  "
	      + "  INNER JOIN public.m_visit_type_master visit_type_master ON visit_type_master.visit_type_id = order_master.visit_type_id   "
	      + "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id   "
	      + "  INNER JOIN patient.t_patient_registration pati_mst ON lbsampledtls.patient_id = pati_mst.patient_id   "
	      + "  INNER JOIN m_prefix_master pre_mst ON pati_mst.prefix_id = pre_mst.prefix_id   "
	      + "  INNER JOIN m_gender_master gen_mst ON pati_mst.gender_id = gen_mst.gender_id   "
	      + "  INNER JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id   "
	      + "  INNER JOIN lab.m_test_master test_master ON test_master.test_id = lbsampledtls.test_id   "
	      + "  INNER JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id   "
	      + "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_master.sample_id   "
	      + "  LEFT JOIN doctor.m_doctor_master doct_mst ON order_master.ord_doctor_id = doct_mst.doctor_id   "
	      + "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = order_master.ord_doc_spl_id  "
	      + " left join adt.t_admission ta on ta.admission_id = order_master.admission_id and ta.status='A' "
			+ " left join opd.t_patient_sponsor_details tpsd "
			+ " on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
			+ " left join opd.t_patient_sponsor_details tpsd2 "
			+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
	      + "  LEFT JOIN adt.t_admission_details admission_dtls ON order_master.admission_id = admission_dtls.admission_id "
	      + "  and admission_dtls.status = 'A' " 
	  	  + "  and admission_dtls.nursing_acceptance = 'Y' "
	      +"  INNER JOIN lab.m_test_status_master stsMstr on lbsampledtls.sample_status_id = stsMstr.test_status_id "
          + " left join billing.t_billing_details bill_dtls on " 
          + "     order_details.order_details_id = bill_dtls.order_details_id " 
          + " left join billing.m_billing_master bill_mst on " 
          + "     bill_mst.billing_master_id = bill_dtls.billing_master_id " 
          + "     and bill_mst.bill_cancelled = 'N' " 

	      + "  LEFT JOIN adt.m_ward_master wardmast ON admission_dtls.ward_id = wardmast.ward_id   "
	      + "  LEFT JOIN adt.m_bed_master bedmst ON admission_dtls.bed_id = bedmst.bed_id   "
	      + "  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id   "
	      + "  LEFT JOIN lab.t_sesitivity_test_result sensitivity_result_mst ON  lbsampledtls.lab_sample_dtls_id = sensitivity_result_mst.lab_sample_dtls_id   "
	      + " left join billing.t_credit_note_detail tcnd on "
	      + " tcnd.order_detail_id = order_details.order_details_id "
	      + " and tcnd.status = 'A' "
	      + " and tcnd.cancel_or_discount = 1 "
	      + "  left join lab.t_lab_test_result tltr on tltr.lab_sample_dtls_id  = lbsampledtls.lab_sample_dtls_id  "
	      + " WHERE   " 
	      + "  (lbsampledtls.sample_status_id IN (:sampleStatusIds) or  lbsampledtls.sample_status_id IN (34) )"
	      + "  AND lbsampledtls.org_id = :orgId   " 
	      + "  and order_details.ord_cancelled = 'N' "
		  + "  and order_details.status = 'A' "
	      + "  and (case when order_master.visit_type_id = 1 then bill_dtls.status = 'A' else order_details.status = 'A' end) "
		  + " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
	      + "  AND lbsampledtls.org_unit_id = :orgUnitId   "
	      + "  AND lbsampledtls.dept_id = :deptId   "
	      + "  AND lbsampledtls.sub_dept_id = :subDeptId   " 
	      +"   AND lbsampledtls.is_outsourced = 'N' "
	   ),

	  /** Sample Pending Acceptance Count */
	  @NamedNativeQuery(name = "GET_SENSITIVITY_TESTING_LIST_COUNT",
	      query = "  SELECT  " 
	          + "  COUNT(*)  " 
	    	  + "FROM  " 
	          + "  lab.t_lab_sample_dtls lbsampledtls  "
	          + "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id   "
	          + "  INNER JOIN public.t_order_master order_master ON lbsampledtls.order_id = order_master.order_id   "
	          + "WHERE  " 
	          + "  lbsampledtls.sample_status_id IN (:sampleStatusIds)  "
	          + "  AND lbsampledtls.org_id = :orgId  "
	          + "  AND lbsampledtls.org_unit_id = :orgUnitId  "
	          + "  AND lbsampledtls.dept_id = :deptId  "
	          + "  AND lbsampledtls.sub_dept_id = :subDeptId  "
	          + "  AND lbsampledtls.is_outsourced = 'N' "),

			
	
})

@NamedQueries({

	@NamedQuery(name = "GET_MEDIA_BY_SAMPLE_ID", query =" SELECT "
			 +"	sampleTypeMediaMappingMaster.mediaId AS mediaId "
			 +"FROM "
			 +"	SampleTypeMediaMappingMaster sampleTypeMediaMappingMaster "
			 +"WHERE "
			 +"	sampleTypeMediaMappingMaster.sampleId = :sampleId "
			 +"	AND sampleTypeMediaMappingMaster.orgId = :orgId "
			 +"	AND sampleTypeMediaMappingMaster.isDeleted = 'N' "
			 +"	AND sampleTypeMediaMappingMaster.sampleMediaStatus = 'A' "),

	

	
	
	@NamedQuery(name="UPDATE_MICROSCOPIC_EXAMINATION", query=" UPDATE "
			 +"	MicrobioResultEntryMaster microbioResultEntryMaster "
			 +"SET "
			 +"	microbioResultEntryMaster.microscopicExamination =:microscopicExamination, "
			 +"	microbioResultEntryMaster.isMacro_exaComplete =:isMacro_exaComplete, "
			 +"	microbioResultEntryMaster.updatedBy =:updatedBy, "
			 +"	microbioResultEntryMaster.updatedDate =:updatedDate "
			 +"WHERE "
			 +"	microbioResultEntryMaster.microbioResultEntryId =:microbioResultEntryId "),
	
	@NamedQuery(name="GET_MICRO_RESULT_ENTRY_BY_ID", query=" SELECT "
			+ " microbioResultEntryMaster.microbioResultEntryId AS microbioResultEntryId, "
			+ " microbioResultEntryMaster.orgId AS orgId, "
			+ " microbioResultEntryMaster.orgUnitId AS orgUnitId, "
			+ " microbioResultEntryMaster.patientId AS patientId, "
			+ " microbioResultEntryMaster.doctorId AS doctorId, "
			+ " microbioResultEntryMaster.labSampleDtlsId AS labSampleDtlsId, "
			+ " microbioResultEntryMaster.microscopicExamination AS microscopicExamination "
			+"	FROM MicrobioResultEntryMaster microbioResultEntryMaster "
			+"  WHERE microbioResultEntryMaster.microbioResultEntryId = :microbioResultEntryId"
			+"	AND microbioResultEntryMaster.orgId =:orgId "
			+ " AND microbioResultEntryMaster.orgUnitId =:orgUnitId "
			+ " AND microbioResultEntryMaster.isDeleted = 'N' ")
	

})

@Entity
@Table(name = "t_microbio_result_entry", schema = "lab")
@SequenceGenerator(name = "t_seq_microbio_result_entry", sequenceName = "lab.t_seq_microbio_result_entry", allocationSize = 1)
public class MicrobioResultEntryMaster {
	
	@Id
	@Column(name = "microbio_result_entry_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_microbio_result_entry")
	private int microbioResultEntryId;
	
	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "org_unit_id")
	private Integer orgUnitId;

	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "lab_sample_dtls_id")
	private Integer labSampleDtlsId;
	
	@Column(name = "microscopic_examination")
	private String microscopicExamination;
	
	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	@Convert(converter = LocalTimeConverter.class)
	private Long createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	@Convert(converter = LocalTimeConverter.class)
	private Long updatedDate;
	
	@Column(name = "is_deleted")
	private Character isDeleted;
	
	@Column(name = "incubation_start_date")
	@Convert(converter =  LocalTimeConverter.class)
	private Long incubationStartDate;
	
	@Column(name = "incubation_start_time")
	@Convert(converter = InstantConverter.class)
	private Instant incubationStartTime;
	
	@Column(name = "is_incubation_complete")
	private Character isIncubationComplete;
	
	@Column(name = "is_macro_exa_complete")
	private Character isMacro_exaComplete;
	
	@Column(name = "is_micro_exa_complete")
	private Character isMicro_exaComplete;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "microbioResultEntryMaster")
	private List<MicrobioResultDetailsMaster> listMicrobioResultDetailsMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lab_sample_dtls_id", insertable = false, nullable = false, updatable = false)
	private LabSampleDetailsMaster labSampleDetailsMaster;

	public int getMicrobioResultEntryId() {
		return microbioResultEntryId;
	}

	public void setMicrobioResultEntryId(int microbioResultEntryId) {
		this.microbioResultEntryId = microbioResultEntryId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	
	public Integer getPatientId() {
		return patientId;
	}
	
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	
	public Integer getDoctorId() {
		return doctorId;
	}
	
	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	public String getMicroscopicExamination() {
		return microscopicExamination;
	}

	public void setMicroscopicExamination(String microscopicExamination) {
		this.microscopicExamination = microscopicExamination;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Instant getIncubationStartTime() {
		return incubationStartTime;
	}

	public void setIncubationStartTime(Instant incubationStartTime) {
		this.incubationStartTime = incubationStartTime;
	}

	public List<MicrobioResultDetailsMaster> getListMicrobioResultDetailsMaster() {
		return listMicrobioResultDetailsMaster;
	}

	public void setListMicrobioResultDetailsMaster(List<MicrobioResultDetailsMaster> listMicrobioResultDetailsMaster) {
		this.listMicrobioResultDetailsMaster = listMicrobioResultDetailsMaster;
	}

	public LabSampleDetailsMaster getLabSampleDetailsMaster() {
		return labSampleDetailsMaster;
	}

	public void setLabSampleDetailsMaster(LabSampleDetailsMaster labSampleDetailsMaster) {
		this.labSampleDetailsMaster = labSampleDetailsMaster;
	}

	public Character getIsIncubationComplete() {
		return isIncubationComplete;
	}

	public void setIsIncubationComplete(Character isIncubationComplete) {
		this.isIncubationComplete = isIncubationComplete;
	}

	public Character getIsMacro_exaComplete() {
		return isMacro_exaComplete;
	}

	public void setIsMacro_exaComplete(Character isMacro_exaComplete) {
		this.isMacro_exaComplete = isMacro_exaComplete;
	}

	public Character getIsMicro_exaComplete() {
		return isMicro_exaComplete;
	}

	public void setIsMicro_exaComplete(Character isMicro_exaComplete) {
		this.isMicro_exaComplete = isMicro_exaComplete;
	}

	public Long getIncubationStartDate() {
		return incubationStartDate;
	}

	public void setIncubationStartDate(Long incubationStartDate) {
		this.incubationStartDate = incubationStartDate;
	}
	
	
	

	
}
