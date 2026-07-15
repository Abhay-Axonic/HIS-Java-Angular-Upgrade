package com.param.entity.lis.transaction;


import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import com.param.entity.lis.common.LocalTimeConverter;

@NamedNativeQueries({
	
			
		@NamedNativeQuery(name="GET_PATENT_ARRIVAL_LIST",query= 
		    " SELECT "
		        +"  vistmst.visit_type_code AS \"visitType\", "
				 + "    bill_mst.bill_number as \"billNumber\", " 
		        +"  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\", "
		        +"  lbsampledtls.is_alliquote_req AS \"isAlliquoteReq\", "
		        +"  pati_mst.uhid_number AS uhid, "
		        +"  max(patientarr.status_id) as \"statusId\", "
		        +"  test_mst.test_desc AS \"testDesc\", "
		        +"  test_mst.test_code AS \"testCode\", "
		        +"  samp_mst.sample_desc AS \"sampleType\", "
		        +"  panel_master.panel_code AS \"panelCode\", "
		        +"  pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || pati_mst.middle_name ||' ' || pati_mst.last_name || ' ' || calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code AS \"patientDetails\", "
		        /*+"  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' AS \"doctorDetails\", "*/
		        +"  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' ' || doct_mst.last_name  AS \"doctorDetails\", "
		        +"  wardmast.ward_code AS \"wardCode\", "
		        +"  bedmst.bed_number AS \"bedNumber\", "
		        +"  lbsampledtls.sample_barcode AS \"sampleBarcode\", "
		        +"  lbsampledtls.order_details_id AS \"orderDetailsId\", "
		        +"  lbsampledtls.test_id AS \"testId\", "
		        +"  labsmpmst.admission_id AS \"visitAdmId\", "
		        +"  lbsampledtls.lab_sample_id AS \"labSampleId\", "
		        +"  lbsampledtls.order_id AS \"orderId\", "
		        +"  lbsampledtls.patient_id AS \"patientId\", "
		        +"  lbsampledtls.doctor_id AS \"doctorId\", "
		        +"  lbsampledtls.sample_pending_count AS \"samplePendingCount\", "
		        +"  lbsampledtls.sub_dept_id AS \"deptId\", "
		        +"  lab.generate_sample_no(CAST(lbsampledtls.sample_no AS \"text\"), 'SM-', 7) AS \"labSampleNo\", "
		        +"  MAX(patientarr.created_date) AS \"arrivalTime\", "
		          +" (case when labsammst.encounter_id > 0 then " +
		          " ( tpsd.payment_entitlement_id ) " +
		          " else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\" "
		        +"FROM "
		        +"  lab.t_lab_sample_dtls lbsampledtls "
		        +"  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id "
		        +"  INNER JOIN patient.t_patient_registration pati_mst ON lbsampledtls.patient_id = pati_mst.patient_id "
		        +"  INNER JOIN doctor.m_doctor_master doct_mst ON lbsampledtls.doctor_id = doct_mst.doctor_id "
		        +"  INNER JOIN m_prefix_master pre_mst ON pati_mst.prefix_id = pre_mst.prefix_id "
		        +"  INNER JOIN m_gender_master gen_mst ON pati_mst.gender_id = gen_mst.gender_id "
		        +"  INNER JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id "
		        +"  INNER JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id "
		        +"  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_mst.sample_id "
		        +"  INNER JOIN lab.t_lab_sample_master labsammst ON labsammst.lab_sample_id = lbsampledtls.lab_sample_id "
		        + " left join adt.t_admission ta on ta.admission_id = labsammst.admission_id and ta.status='A' "
	            + " left join opd.t_patient_sponsor_details tpsd "
	            + " on labsammst.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
	            + " left join opd.t_patient_sponsor_details tpsd2 "
	            + " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
		        +"  INNER JOIN m_visit_type_master vistmst ON vistmst.visit_type_id = labsammst.visit_type_id "
		        +"  INNER JOIN public.t_order_details ord_details ON ord_details.order_details_id = lbsampledtls.order_details_id "
		        + " left join billing.t_billing_details bill_dtls on " 
				 + " 	ord_details.order_details_id = bill_dtls.order_details_id " 
				 + " left join billing.m_billing_master bill_mst on " 
				 + " 	bill_mst.billing_master_id = bill_dtls.billing_master_id " 
				// + " 	and bill_mst.bill_cancelled = 'N' " 
//				 + " 	and bill_mst.bill_status_id <> 1 " 
		        +"  INNER JOIN lab.t_patient_arrival_mpper patientarr ON patientarr.patient_id = lbsampledtls.patient_id "
		        +"  INNER JOIN lab.t_lab_sample_master labsmpmst ON labsmpmst.lab_sample_id = lbsampledtls.lab_sample_id "
		        +"  LEFT JOIN adt.m_ward_master wardmast ON ord_details.ward_id = wardmast.ward_id "
		        +"  LEFT JOIN adt.m_bed_master bedmst ON ord_details.bed_id = bedmst.bed_id "
		        +"  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id "
		        +"  INNER join public.t_lab_configuration lab_config on "
				+ "	lab_config.organisation_id = :orgId and lab_config.unit_id = :orgUnitId  "
				+ " left join billing.t_credit_note_detail tcnd on "
				+ " tcnd.order_detail_id = ord_details.order_details_id "
				+ " and tcnd.status = 'A' "
				+ " and tcnd.cancel_or_discount = 1 "
		        +"WHERE "
		        +"  lbsampledtls.org_id = :orgId "
		        +"  AND lbsampledtls.dept_id IN (lab_config.biochemistry_id,lab_config.hematology_id,lab_config.microbiology_id,lab_config.histopathology_id,lab_config.cystopathology_id,lab_config.serology_id) "
		        +"  AND lbsampledtls.sample_pending_count > 0 "
		        +"  AND lbsampledtls.org_unit_id = :orgUnitId "
		        +"  AND patientarr.org_id = :orgId "
		        +"  AND patientarr.org_unit_id = :orgUnitId "
		        +"  AND lbsampledtls.is_outsourced = :isOutSource "
		        +"  AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDate "
		        + " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDate "
		        + "	and ord_details.ord_cancelled = 'N' "
	    		+ "	and ord_details.status = 'A' "
	    		+ " and (case when  labsmpmst.visit_type_id not in(1) then (case when bill_mst.billing_master_id  is not null then  bill_mst.bill_cancelled = 'N' else bill_mst.billing_master_id  is null  end )  else bill_mst.billing_master_id  is not null and bill_mst.bill_cancelled = 'N' end) "
	    		+ " and (ord_details.status = 'A' and tcnd.credit_note_detail_id is null) "
		        +" GROUP BY "
		        +"  vistmst.visit_type_id, "
		        + " bill_mst.bill_number, "
		        +"  lbsampledtls.lab_sample_dtls_id, "
		        +"  pati_mst.uhid_number, "
		        +"  test_mst.test_id, "
		        +"  pre_mst.prefix_id, "
		        +"  pati_mst.patient_id, "
		        +"  gen_mst.gender_id, "
		        +"  doct_mst.doctor_id, "
		        +"  wardmast.ward_id, "
		        +"  bedmst.bed_id, "
		        +"  samp_mst.sample_id, "
		        +"  panel_master.panel_id, "
		        +"  labsmpmst.admission_id, "
		        +"  labsammst.encounter_id, "
		        +"  labsammst.admission_id, "
		        + " tpsd.payment_entitlement_id,"
		        + " tpsd2.payment_entitlement_id  "
		      //  +"  speciality_master.speciality_id "
		        +"having "
		        +"  max(patientarr.status_id) = :statusId "
		        +"ORDER BY "
		        +"  lbsampledtls.priority_id, "
		        +"  lbsampledtls.sample_sendtocr_datetime "),
					 
					 
					   
		
		 @NamedNativeQuery(name="GET_PATENT_ARRIVAL_COUNT",
		   query=  
		   " WITH temp_count AS( "
				   +"	SELECT "
				   +"		COALESCE( "
				   +"			COUNT(*), "
				   +"			0 "
				   +"		) AS COUNT "
				   +"	FROM "
				   +"		lab.t_lab_sample_dtls lbsampledtls "
				   +"	LEFT JOIN lab.t_patient_arrival_mpper patientarr ON "
				   +"		patientarr.patient_id = lbsampledtls.patient_id "
				   +"   INNER join public.t_lab_configuration lab_config on "
				   + "	lab_config.organisation_id = :orgId and lab_config.unit_id = :orgUnitId "
				   + "  INNER JOIN public.t_order_details ord_details ON ord_details.order_details_id = lbsampledtls.order_details_id  "
				   +"	WHERE "
				   +"		lbsampledtls.org_id =:orgId "
				   +"		AND lbsampledtls.dept_id IN (lab_config.biochemistry_id,lab_config.hematology_id,lab_config.microbiology_id,lab_config.histopathology_id,lab_config.cystopathology_id,lab_config.serology_id) "
				   +"		AND lbsampledtls.sample_pending_count > 0 "
				   +"		AND lbsampledtls.is_outsourced =:isOutSource "
				   +"		AND lbsampledtls.org_unit_id =:orgUnitId "
				   + "	and ord_details.ord_cancelled = 'N' "
				   + "	and ord_details.status = 'A' "
				   +"  		AND to_char(ord_details.order_date,'YYYY-MM-DD') >=:fromDate "
			       + " 		AND to_char(ord_details.order_date,'YYYY-MM-DD') <=:toDate "
				   +"	GROUP BY "
				   +"		patientarr.patient_id, "
				   +"		lbsampledtls.lab_sample_dtls_id "
				   +"	HAVING "
				   +"		MAX( patientarr.status_id )=:statusId "
				   +") SELECT "
				   +"	COUNT(*) "
				   +"FROM "
				   +"	temp_count "),
		 @NamedNativeQuery(name="GET_PATENT_ARRIVAL_LIST_PATIENT_SEARCH",query= 
		    " SELECT " 
		    +"  distinct (pati_mst.uhid_number) AS \"uhid\", pati_mst.first_name as \"name\", pati_mst.first_name as \"firstName\" ,pati_mst.last_name as \"lastName\", "  
		    +"  pati_mst.first_name || ' ' || pati_mst.last_name || ' ' || pati_mst.uhid_number as \"patientWithUhid\" ," 
		    +"  lbsampledtls.patient_id AS \"id\" " 
		    + "FROM " 
		    + "  lab.t_lab_sample_dtls lbsampledtls" 
		    + "  INNER JOIN patient.t_patient_registration pati_mst ON lbsampledtls.patient_id = pati_mst.patient_id " 
		    + "  INNER JOIN m_prefix_master pre_mst ON pati_mst.prefix_id = pre_mst.prefix_id "
		    + "  INNER JOIN m_gender_master gen_mst ON pati_mst.gender_id = gen_mst.gender_id " 
		    + " INNER JOIN lab.t_patient_arrival_mpper patientarr ON patientarr.patient_id = lbsampledtls.patient_id " 
		    + "  INNER join public.t_lab_configuration lab_config on "
			+ "	lab_config.organisation_id = :orgId and lab_config.unit_id = :orgUnitId  "
	        +"WHERE "
	        +"  lbsampledtls.org_id = :orgId "
	        +"  AND lbsampledtls.dept_id IN (lab_config.biochemistry_id,lab_config.hematology_id,lab_config.microbiology_id,lab_config.histopathology_id,lab_config.cystopathology_id,lab_config.serology_id) "
	        +"  AND lbsampledtls.sample_pending_count > 0 "
	        +"  AND lbsampledtls.org_unit_id = :orgUnitId "
	        +"  AND patientarr.org_id = :orgId "
	        +"  AND patientarr.org_unit_id = :orgUnitId "
	        +"  AND lbsampledtls.is_outsourced = :isOutSource "),
})
@Entity
@Table(name = "t_patient_arrival_mpper", schema = "lab")
public class LabPatientArrivalMapperMaster 
{

	@EmbeddedId
	private PatientArrivalMasterId patientArrivalMasterId;
	 
	@Column(name = "org_unit_id")
	private Integer orgUnitId;

	@Column(name = "test_id")
	private Integer testId;
	
	@Column(name = "dept_id")
	private Integer deptId;
	
	@Column(name = "pending_count")
	private Integer pendingCount;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "collection_time")
	@Convert(converter = LocalTimeConverter.class)
	private Long collectionTime;

	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "order_details_id")
	private Integer orderDetailsId;
	
	
	@Column(name = "createdDate")
	@Convert(converter = LocalTimeConverter.class)
	private Long createdDate;

	public PatientArrivalMasterId getPatientArrivalMasterId() {
		return patientArrivalMasterId;
	}

	public void setPatientArrivalMasterId(
			PatientArrivalMasterId patientArrivalMasterId) {
		this.patientArrivalMasterId = patientArrivalMasterId;
	}

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	public Integer getTestId() {
		return testId;
	}

	public void setTestId(Integer testId) {
		this.testId = testId;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Integer getPendingCount() {
		return pendingCount;
	}

	public void setPendingCount(Integer pendingCount) {
		this.pendingCount = pendingCount;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	

	public Long getCollectionTime() {
		return collectionTime;
	}

	public void setCollectionTime(Long collectionTime) {
		this.collectionTime = collectionTime;
	}


	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
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

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}
	

	
}
