package com.param.entity.lis.transaction;

import java.math.BigInteger;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.lis.common.LocalTimeConverter;
import com.param.entity.lis.global.LabConfigurationMaster;
import com.param.entity.lis.histo.TTemplateResult;
import com.param.entity.lis.unit.TestMaster;
import com.param.global.model.DoctorMaster;
import com.param.global.model.GenderMaster;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.model.OrderMaster;
import com.param.global.model.PatientRegistration;

@NamedNativeQueries({

		/** @QUERY SEND TO CR LIST */
		@NamedNativeQuery(name = "GET_COLLECTED_SAMPLE_LIST", query = " SELECT  "
				+ "  panel_master.panel_code AS \"panelCode\",  "
				+ "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\",  "
				+ "  lbsampledtls.org_id AS \"orgId\",  "
				+ "  lbsampledtls.org_unit_id AS \"orgUnitId\",  "
				+ "  lbsampledtls.assign_unit AS \"assignUnit\",  "
				+ "  visit_type_master.visit_type_code AS \"visitType\",  "
				+ "  test_master.test_desc AS \"testDesc\",  " 
				+ "  test_master.test_code  AS \"testCode\","
				+ " test_master.test_id as \"testId\", "
				+ " test_master.service_id as \"serviceId\", "
				+ "  lbsampledtls.perform_test_id as \"performTestId\", "
				+ "  samp_mst.sample_desc  AS \"sampleType\",  "
				+ "  sample_mst.sample_no AS \"labSampleNo\",  "
				+ "  pati_mst.uhid_number AS uhid,  "
				+ "  pre_mst.prefix_code || ' ' || pati_mst.first_name ||  ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || calculate_age(pati_mst.dob)"
				+ " || '/' || gen_mst.gender_code AS \"patientDetails\",  "
				+ "  subspe_mst.sub_speciality_name AS \"deptName\",  "
				+ "  subspe_mst.sub_speciality_id AS \"subDeptId\",  "
				+ "  contai_mst.container_name AS \"containerName\",  "
				+ "  order_details.created_date AS \"orderDateTime\",  "
				+ "  lbsampledtls.sample_collection_datetime AS \"sampleCollectionDatetime\",  " 
				+ "  to_char(order_details.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"orderDateTimeString\",  "
				+ "  to_char(lbsampledtls.sample_collection_datetime, 'DD/MM/YYYY HH24:MI:SS') AS \"sampleCollectionDatetimeString\",  " 
				+" (case when order_master.encounter_id > 0 then " +
				" ( tpsd.payment_entitlement_id ) " +
				" else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
	            + " order_details.is_dummy_service as \"isDummyService\" "
				+ "  FROM  "
				+ "  lab.t_lab_sample_dtls lbsampledtls  "
				+ "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id  "
				+ "  INNER JOIN public.t_order_master order_master ON order_master.order_id = order_details.order_id  "
				+ "  INNER JOIN lab.t_lab_sample_master sample_mst ON lbsampledtls.lab_sample_id = sample_mst.lab_sample_id  "
				+ "  INNER JOIN public.m_visit_type_master visit_type_master ON visit_type_master.visit_type_id = order_master.visit_type_id  "
				+ "  INNER JOIN patient.t_patient_registration pati_mst ON lbsampledtls.patient_id = pati_mst.patient_id  "
				+ "  INNER JOIN m_prefix_master pre_mst ON pati_mst.prefix_id = pre_mst.prefix_id  "
				+ "  INNER JOIN m_gender_master gen_mst ON pati_mst.gender_id = gen_mst.gender_id  "
				+ "  INNER JOIN lab.m_test_master test_master ON test_master.test_id = lbsampledtls.test_id  "
				+ "  INNER JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id  "
				+ "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_master.sample_id "
				+ " left join adt.t_admission ta on ta.admission_id = order_master.admission_id and ta.status='A' "
				+ " left join opd.t_patient_sponsor_details tpsd "
				+ " on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
				+ " left join opd.t_patient_sponsor_details tpsd2 "
				+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
				+ "  LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id  "
				+ "  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id  "
				+ "  INNER join public.t_lab_configuration lab_config on "
				+ "	 lab_config.organisation_id = :orgId and lab_config.unit_id = :orgUnitId  "
				+ " left join billing.t_billing_details bill_dtls on "
				+ " 	order_details.order_details_id = bill_dtls.order_details_id "
				+ " left join billing.m_billing_master bill_mst on "
				+ " 	bill_mst.billing_master_id = bill_dtls.billing_master_id "
				+ " left join billing.t_credit_note_detail tcnd on "
				+ " tcnd.order_detail_id = order_details.order_details_id "
				+ " and tcnd.status = 'A' "
				+ " and tcnd.cancel_or_discount = 1 "
				+ "WHERE  " 
				+ "  lbsampledtls.sample_status_id IN(:sampleStatusIds)  "
				+ "  AND lbsampledtls.org_id = :orgId  "
				+ "  AND lbsampledtls.org_unit_id = :orgUnitId "
				+ " AND lbsampledtls.dept_id  in (:deptId) "
				//+ "  AND lbsampledtls.dept_id = :deptId  "
				+ "  AND test_master.is_histo_cyto in('N','S') "
				+ "  and order_details.ord_cancelled = 'N' "
				+ "  and order_details.status = 'A' "
				+ " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
				+"	and to_char(lbsampledtls.created_date,'YYYY-MM-DD') >= :fromDate "
				+"	and to_char(lbsampledtls.created_date,'YYYY-MM-DD') <= :toDate "
				+ " and (case when  order_master.visit_type_id =1 then (case when bill_mst.billing_master_id  is not null then  bill_mst.bill_cancelled = 'N' else bill_mst.billing_master_id  is null  end )  else order_details.status = 'A' end) "
				+ " ORDER BY  " 
				+ "  lbsampledtls.priority_id,  " 
				+ "  lbsampledtls.sample_accept_datetime  "),

		/** @QUERY SEND TO CR LIST COUNT */
		@NamedNativeQuery(name = "GET_COUNT_FROM_SEND_TO_CR_LIST", query = "  SELECT  " 
		        + "  COUNT(*)  " 
				+ " FROM  "
				+ "  lab.t_lab_sample_dtls lbsampledtls  "
				+ "  INNER JOIN public.t_order_master order_master ON order_master.order_id = lbsampledtls.order_id  "
				+ "  INNER JOIN lab.m_test_master test_master ON test_master.test_id = lbsampledtls.test_id  "
				+ "  INNER join public.t_lab_configuration lab_config on "
				+ "	 lab_config.organisation_id = :orgId and lab_config.unit_id = :orgUnitId  "
				+ "  INNER JOIN public.m_visit_type_master visit_type_master ON visit_type_master.visit_type_id = order_master.visit_type_id  "
				+ "WHERE  " 
				+ "  lbsampledtls.sample_status_id IN(:sampleStatusIds)  "
				+ "  AND lbsampledtls.sub_dept_id IN(lab_config.biochemistry_id,lab_config.hematology_id,lab_config.clinical_pathology_id)  " 
				+ "  AND lbsampledtls.org_id = :orgId  "
				+ "  AND lbsampledtls.dept_id = :deptId  " 
				+ "  AND order_master.visit_type_id = :visitTypeId  "
				+ "  AND test_master.is_histo_cyto ='N' " 
				+ "  AND lbsampledtls.org_unit_id = :orgUnitId "
				//+ "  AND order_master.created_date >= :fromDate " 
				//+ "  AND order_master.created_date <= :toDate" 
				),

		@NamedNativeQuery(name = "SEND_SAMPLE_TO_CENTRAL_RECEIVING", query = "	UPDATE " 
		        + "	lab.t_lab_sample_dtls  "
				+ " SET " 
		        + "	sample_status_id =:sampleStatusId, " 
		        + "	sample_sendtocr_by =:sampleSendToCrBy ,"
		        + "	performed_unit =:performedUnit, "
		       // + "	test_id =:performTestId, " // swapping testID testId to performTestId
		        //+ "	assign_unit =:assignUnit, "
				+ "	updated_by =:updatedBy ," 
		        + "	updated_date =:updatedDate ,"
				+ " sample_sendtocr_datetime =:sampleSendtocrDatetime "
				+ " WHERE lab_sample_dtls_id = :labSampleDtlsId"),

		@NamedNativeQuery(name = "COLLECT_SAMPLE_OUT_PATIENT", query = " SELECT  "
				+ "  visit_type_master.visit_type_code AS \"visitType\",  "
				 + "    bill_mst.bill_number as \"billNumber\", " 
				+ " order_details.priority_id AS \"priorityId\", "
				+ "  panel_master.panel_code AS \"panelCode\",  "
				+ "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\",  "
				+ "  sample_mst.sample_no AS \"labSampleNo\",  "
				+ "  lbsampledtls.is_alliquote_req AS \"isAlliquoteReq\",  "
				+ "  lbsampledtls.sample_pending_count AS \"samplePendingCount\",  "
				+ "  test_master.test_code  AS \"testCode\",  " 
				+ "  samp_mst.sample_desc  AS \"sampleType\",  "
				+ "  pati_mst.uhid_number AS uhid,  " 
				+ "  test_mst.test_desc AS \"testDesc\",  "
				+ "  subspe_mst.sub_speciality_id AS \"subDeptId\",  "
				+ "  test_mst.is_centrifugation_requried AS \"isCentrifugationReq\",  "
				+ "  pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' '|| pati_mst.last_name || ' ' || calculate_age(pati_mst.dob)  || '/' || gen_mst.gender_code AS \"patientDetails\",  "
				+ "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' AS \"doctorDetails\",  "
				+ "  order_details.created_date AS \"orderDateTime\",  "
				+ "  lbsampledtls.sample_collection_datetime AS \"sampleSendtocrDatetime\",  " 
				+ "  to_char(order_details.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"orderDateAndTime\",  "
				+ "  to_char(lbsampledtls.sample_collection_datetime, 'DD/MM/YYYY HH24:MI:SS') AS \"sampleSendtocrDateAndtime\",  " 
				+" (case when order_master.encounter_id > 0 then " +
				" ( tpsd.payment_entitlement_id ) " +
				" else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\" "
				+ " FROM  "
				+ "  lab.t_lab_sample_dtls lbsampledtls  "
				+ "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id  "
				+ "  INNER JOIN public.t_order_master order_master ON order_master.order_id = order_details.order_id  "
				+ "  INNER JOIN lab.t_lab_sample_master sample_mst ON lbsampledtls.lab_sample_id = sample_mst.lab_sample_id  "
				+ "  INNER JOIN public.m_visit_type_master visit_type_master ON visit_type_master.visit_type_id = order_master.visit_type_id  "
				+ "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id  "
				+ "  INNER JOIN patient.t_patient_registration pati_mst ON lbsampledtls.patient_id = pati_mst.patient_id  "
				+ "  INNER JOIN m_prefix_master pre_mst ON pati_mst.prefix_id = pre_mst.prefix_id  "
				+ "  INNER JOIN m_gender_master gen_mst ON pati_mst.gender_id = gen_mst.gender_id  "
				+ "  INNER JOIN lab.m_test_master test_master ON test_master.test_id = lbsampledtls.test_id  "
				+ "  INNER JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id  "
				+ "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_master.sample_id "
				+ "  LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id  "
				+ "  LEFT JOIN doctor.m_doctor_master doct_mst ON order_master.ord_doctor_id = doct_mst.doctor_id  "
				+ "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = order_master.ord_doc_spl_id   "
				+ " left join adt.t_admission ta on ta.admission_id = order_master.admission_id and ta.status='A' "
				+ " left join opd.t_patient_sponsor_details tpsd "
				+ " on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
				+ " left join opd.t_patient_sponsor_details tpsd2 "
				+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
				+ "  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id  "
				+ "  INNER join public.t_lab_configuration lab_config on "
				+ "	 lab_config.organisation_id = :orgId and lab_config.unit_id = :orgUnitId  "
				+ " left join billing.t_billing_details bill_dtls on " 
				 + " 	order_details.order_details_id = bill_dtls.order_details_id " 
				 + " left join billing.m_billing_master bill_mst on " 
				 + " 	bill_mst.billing_master_id = bill_dtls.billing_master_id " 
				+ " left join billing.t_credit_note_detail tcnd on "
				+ " tcnd.order_detail_id = order_details.order_details_id "
				+ " and tcnd.status = 'A' "
				+ " and tcnd.cancel_or_discount = 1 "
				+ "WHERE  " 
				+ "  lbsampledtls.sample_status_id in (:sampleStatusIds)  "
				+ "  AND lbsampledtls.org_id = :orgId  "
				+ "  AND lbsampledtls.org_unit_id = :orgUnitId  "
				 + "  and order_details.ord_cancelled = 'N'"
				 + " and (case when  order_master.visit_type_id not in(1) then (case when bill_mst.billing_master_id  is not null then  bill_mst.bill_cancelled = 'N' else bill_mst.billing_master_id  is null  end )  else bill_mst.billing_master_id  is not null and bill_mst.bill_cancelled = 'N' end) "
				//+ " and (case when (lab_config.serology_id is not null or lab_config.serology_id <> 0) then lbsampledtls.sub_dept_id not in (lab_config.serology_id) else  order_details.status = 'A' end)	 "
				+ " and order_details.status = 'A' "
				+ " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
				+ "  AND order_master.visit_type_id IN (:visitTypeIds)  " ),

		@NamedNativeQuery(name = "GET_COUNT_FOR_OUT_PATIENT", query = " SELECT  " + "  COUNT(*)  " + "FROM  "
				+ "  lab.t_lab_sample_dtls lbsampledtls  "
				+ "  INNER JOIN public.t_order_master order_master ON lbsampledtls.order_id = order_master.order_id "
				+ "  INNER JOIN lab.t_lab_sample_master labsammst ON labsammst.lab_sample_id = lbsampledtls.lab_sample_id  "
				+ "  inner join public.t_lab_configuration lab_config on "
				+ "	 lab_config.organisation_id = :orgId and lab_config.unit_id = :orgUnitId  "
				+ " WHERE  " 
				+ "  lbsampledtls.sample_status_id in (:sampleStatusIds)  "
				+ "  AND lbsampledtls.org_id = :orgId  " 
				//+ "  AND lbsampledtls.sub_dept_id IN(lab_config.biochemistry_id,lab_config.hematology_id,lab_config.clinical_pathology_id)  "
				+ "  AND lbsampledtls.org_unit_id = :orgUnitId  " 
				+ "  AND labsammst.visit_type_id  = :visitTypeId  "
				),

		@NamedNativeQuery(name = "COLLECT_SAMPLE_IN_PATIENT", query = " SELECT  "
				+ "  visit_type_master.visit_type_code AS \"visitType\",  "
				 + "    bill_mst.bill_number as \"billNumber\", " 
				+ " order_details.priority_id AS \"priorityId\", "
				+ "  panel_master.panel_code AS \"panelCode\",  "
				+ "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\",  "
				+ "  sample_mst.sample_no AS \"labSampleNo\",  "
				+ "  lbsampledtls.is_alliquote_req AS \"isAlliquoteReq\",  "
				+ "  lbsampledtls.sample_pending_count AS \"samplePendingCount\",  "
				+ "  test_master.test_code  AS \"testCode\",  " 
				+ "  samp_mst.sample_desc  AS \"sampleType\",  "
				+ "  test_mst.is_centrifugation_requried AS \"isCentrifugationReq\",  "
				+ "  pati_mst.uhid_number AS uhid,  " 
				+ "  test_mst.test_desc AS \"testDesc\",  "
				+ "  subspe_mst.sub_speciality_id AS \"subDeptId\",  "
				+ "  pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' '|| pati_mst.last_name || ' ' || calculate_age(pati_mst.dob)"
				+ " || '/' || gen_mst.gender_code AS \"patientDetails\",  "
				+ "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' AS \"doctorDetails\",  "
				+ "  wardmast.ward_code AS \"wardCode\",  " 
				+ "  bedmst.bed_number AS \"bedNumber\",  "
				+ "  order_details.created_date AS \"orderDateTime\",  "
				+ "  lbsampledtls.sample_collection_datetime AS \"sampleSendtocrDatetime\",  " 
				+ "  to_char(order_details.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"orderDateAndTime\",  "
				+ "  to_char(lbsampledtls.sample_collection_datetime, 'DD/MM/YYYY HH24:MI:SS') AS \"sampleSendtocrDateAndtime\",  " 
				+" (case when order_master.encounter_id > 0 then " +
				" ( tpsd.payment_entitlement_id ) " +
				" else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
	            + "order_details.is_dummy_service as \"isDummyService\" "
				+ "  FROM  "
				+ "  lab.t_lab_sample_dtls lbsampledtls  "
				+ "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id  "
				+ "  INNER JOIN public.t_order_master order_master ON order_master.order_id = order_details.order_id  "
				+ "  INNER JOIN lab.t_lab_sample_master sample_mst ON lbsampledtls.lab_sample_id = sample_mst.lab_sample_id  "
				+ "  INNER JOIN public.m_visit_type_master visit_type_master ON visit_type_master.visit_type_id = order_master.visit_type_id  "
				+ "  LEFT JOIN adt.t_admission_details admision_dtls ON admision_dtls.admission_id = order_master.admission_id "
				+ "  and admision_dtls.status = 'A' " 
				+ "  and admision_dtls.nursing_acceptance = 'Y' "
				+ "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id  "
				+ "  INNER JOIN patient.t_patient_registration pati_mst ON lbsampledtls.patient_id = pati_mst.patient_id  "
				+ "  INNER JOIN m_prefix_master pre_mst ON pati_mst.prefix_id = pre_mst.prefix_id  "
				+ "  INNER JOIN m_gender_master gen_mst ON pati_mst.gender_id = gen_mst.gender_id  "
				+ "  INNER JOIN lab.m_test_master test_master ON test_master.test_id = lbsampledtls.test_id  "
				+ "  INNER JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id  "
				+ "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_mst.sample_id "
				+ "  LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id  "
				+ "  LEFT JOIN doctor.m_doctor_master doct_mst ON order_master.ord_doctor_id = doct_mst.doctor_id  "
				+ "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = order_master.ord_doc_spl_id "
				+ " left join adt.t_admission ta on ta.admission_id = order_master.admission_id and ta.status='A' "
				+ " left join opd.t_patient_sponsor_details tpsd "
				+ " on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
				+ " left join opd.t_patient_sponsor_details tpsd2 "
				+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
				+ "  LEFT JOIN adt.m_ward_master wardmast ON admision_dtls.ward_id = wardmast.ward_id  "
				+ "  LEFT JOIN adt.m_bed_master bedmst ON admision_dtls.bed_id = bedmst.bed_id  "
				+ "  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id  "
				+ "  INNER join public.t_lab_configuration lab_config on "
				+ "	 lab_config.organisation_id = :orgId and lab_config.unit_id = :orgUnitId  "
				+ " left join billing.t_billing_details bill_dtls on " 
				 + " 	order_details.order_details_id = bill_dtls.order_details_id " 
				 + " left join billing.m_billing_master bill_mst on " 
				 + " 	bill_mst.billing_master_id = bill_dtls.billing_master_id " 
				+ " left join billing.t_credit_note_detail tcnd on "
				+ " tcnd.order_detail_id = order_details.order_details_id "
				+ " and tcnd.status = 'A' "
				+ " and tcnd.cancel_or_discount = 1 "
				+ "WHERE  " 
				+ "  lbsampledtls.sample_status_id in (:sampleStatusIds)"
				+ "  AND lbsampledtls.org_id = :orgId  "
				+ "  and order_details.ord_cancelled = 'N' "
				+ "  and order_details.status = 'A' "
				+ " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
				+ "  AND lbsampledtls.org_unit_id = :orgUnitId "
				 + " and (case when  order_master.visit_type_id not in(1) then (case when bill_mst.billing_master_id  is not null then  bill_mst.bill_cancelled = 'N' else bill_mst.billing_master_id  is null  end )  else bill_mst.billing_master_id  is not null end) "
				+ "  AND order_master.visit_type_id IN (:visitTypeIds)  "),

		@NamedNativeQuery(name = "GET_COUNT_FOR_IN_PATIENT", query = " SELECT  " 
		        + "  COUNT(*)  " 
				+ " FROM  "
				+ "  lab.t_lab_sample_dtls lbsampledtls  "
				+ "  INNER JOIN public.t_order_master order_master ON lbsampledtls.order_id = order_master.order_id   "
				+ "  INNER JOIN lab.t_lab_sample_master labsammst ON labsammst.lab_sample_id = lbsampledtls.lab_sample_id  "
				+ "  INNER JOIN public.m_visit_type_master visit_type_master ON visit_type_master.visit_type_id = order_master.visit_type_id  "
				+ "  INNER join public.t_lab_configuration lab_config on "
				+ "	lab_config.organisation_id = :orgId and lab_config.unit_id = :orgUnitId  "
				+ "WHERE  " 
				+ "  lbsampledtls.sample_status_id in (:sampleStatusIds) "
				//+ "  AND lbsampledtls.sub_dept_id IN(lab_config.biochemistry_id,lab_config.hematology_id,lab_config.clinical_pathology_id)  " 
				+ "  AND lbsampledtls.org_id = :orgId  "
				+ "  AND lbsampledtls.org_unit_id = :orgUnitId  "
				//+ "  AND labsammst.visit_type_id IN (:visitTypeIds)  "
				),

		@NamedNativeQuery(name = "CENTRIFUGATION_WORK_LIST", query = " SELECT  "
				+ "  visit_type_master.visit_type_code AS \"visitType\",  "
				 + "    bill_mst.bill_number as \"billNumber\", " 
				+ " order_details.priority_id AS \"priorityId\", "
				+ "  panel_master.panel_code AS \"panelCode\",  "
				+ "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\",  "
				+ "  sample_mst.sample_no AS \"labSampleNo\",  "
				+ "  lbsampledtls.is_alliquote_req AS \"isAlliquoteReq\",  " + "  pati_mst.uhid_number AS uhid,  "
				+ "  test_mst.test_desc AS \"testDesc\",  " + "  test_mst.test_code AS \"testCode\",  "
				+ " samp_mst.sample_desc  AS \"sampleType\",  "
				+ "  pre_mst.prefix_code || ' ' || pati_mst.first_name || ' '|| coalesce(pati_mst.middle_name) ||' ' || pati_mst.last_name || ' ' ||"
				+ " calculate_age(pati_mst.dob)  || '/' || gen_mst.gender_code AS \"patientDetails\",  "
				+ "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' AS \"doctorDetails\",  "
				+ "  wardmast.ward_code AS \"wardCode\",  " + "  bedmst.bed_number AS \"bedNumber\",  "
				+ "  lbsampledtls.sample_barcode AS \"sampleBarcode\",  "
				+ "  order_details.created_date AS \"orderDateTime\",  "
				+ "  lbsampledtls.sample_collection_datetime AS \"sampleCollectionDatetime\",  " 
				+ "  to_char(order_details.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"orderDateAndTime\",  "
				+ "  to_char(lbsampledtls.sample_collection_datetime, 'DD/MM/YYYY HH24:MI:SS') AS \"sampleCollectionDateAndtime\",  " 
				+" (case when order_master.encounter_id > 0 then " +
				" ( tpsd.payment_entitlement_id ) " +
				" else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\" "
				+ "  FROM  "
				+ "  lab.t_lab_sample_dtls lbsampledtls  "
				+ "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id  "
				+ "  INNER JOIN public.t_order_master order_master ON order_master.order_id = order_details.order_id  "
				+ "  INNER JOIN lab.t_lab_sample_master sample_mst ON lbsampledtls.lab_sample_id = sample_mst.lab_sample_id  "
				+ "  INNER JOIN public.m_visit_type_master visit_type_master ON visit_type_master.visit_type_id = order_master.visit_type_id  "
				+ "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id  "
				+ "  INNER JOIN patient.t_patient_registration pati_mst ON lbsampledtls.patient_id = pati_mst.patient_id  "
				+ "  INNER JOIN m_prefix_master pre_mst ON pati_mst.prefix_id = pre_mst.prefix_id  "
				+ "  INNER JOIN m_gender_master gen_mst ON pati_mst.gender_id = gen_mst.gender_id  "
				+ "  INNER JOIN lab.m_test_master test_master ON test_master.test_id = lbsampledtls.test_id  "
				+ "  INNER JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id  "
				+ "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_master.sample_id "
				+ "  LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id  "
				+ "  LEFT JOIN doctor.m_doctor_master doct_mst ON order_master.ord_doctor_id = doct_mst.doctor_id  "
				//+ "  LEFT JOIN doctor.t_doctor_speciality_mapper doct_speciality_mppr ON doct_speciality_mppr.doctor_id = order_master.ord_doctor_id and doct_speciality_mppr.status = 'A' "
				+ "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = order_master.ord_doc_spl_id   "
				+ "  LEFT JOIN adt.t_admission_details admision_dtls ON admision_dtls.admission_id = order_master.admission_id "
				+ "  and admision_dtls.status = 'A' " 
				+ "  and admision_dtls.nursing_acceptance = 'Y' "
				+ "  LEFT JOIN adt.m_ward_master wardmast ON admision_dtls.ward_id = wardmast.ward_id  "
				+ "  LEFT JOIN adt.m_bed_master bedmst ON admision_dtls.bed_id = bedmst.bed_id  "
				+ "  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id  "
				+ " left join adt.t_admission ta on ta.admission_id = order_master.admission_id and ta.status='A' "
				+ " left join opd.t_patient_sponsor_details tpsd "
				+ " on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
				+ " left join opd.t_patient_sponsor_details tpsd2 "
				+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
				+ " left join billing.t_billing_details bill_dtls on " 
				 + " 	order_details.order_details_id = bill_dtls.order_details_id " 
				 + " left join billing.m_billing_master bill_mst on " 
				 + " 	bill_mst.billing_master_id = bill_dtls.billing_master_id " 
				 + " 	and bill_mst.bill_cancelled = 'N' " 
				+ "  INNER join public.t_lab_configuration lab_config on "
				+ "	 lab_config.organisation_id = :orgId and lab_config.unit_id = :orgUnitId  "
				+ "WHERE  " 
				+ "  lbsampledtls.sample_status_id IN (:sampleStatusId)  "
				+ "  AND test_mst.is_centrifugation_requried = :isCentrifugationReq  "
				+ "  AND lbsampledtls.sub_dept_id IN(lab_config.biochemistry_id,lab_config.hematology_id,lab_config.clinical_pathology_id)  " 
				+ "  AND lbsampledtls.org_id = :orgId  "
				+ "  AND lbsampledtls.org_unit_id = :orgUnitId  " 
				+ "  AND lbsampledtls.dept_id = :deptId  "
				+ "  and order_details.ord_cancelled = 'N' "
				+ "  and order_details.status = 'A' "),

		@NamedNativeQuery(name = "GET_COUNT_CEONTRIFUGATION_WORKLIST", query = " SELECT  " 
		        + "  COUNT(*)  " 
				+ " FROM  "
				+ "  lab.t_lab_sample_dtls lbsampledtls  "
				+ "  INNER JOIN public.t_order_master order_master ON lbsampledtls.order_id = order_master.order_id   "
				+ "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id  " 
				+ "  INNER join public.t_lab_configuration lab_config on "
				+ "	 lab_config.organisation_id = :orgId and lab_config.unit_id = :orgUnitId  "
				+ " WHERE  "
				+ "  lbsampledtls.sample_status_id IN(:sampleStatusId) " 
				+ "  AND lbsampledtls.org_id = :orgId  "
				+ "  AND lbsampledtls.dept_id = :deptId  " 
				+ "  AND lbsampledtls.org_unit_id = :orgUnitId  "
				+ "  AND lbsampledtls.sub_dept_id IN(lab_config.biochemistry_id,lab_config.hematology_id,lab_config.clinical_pathology_id)  "
				+ "  AND test_mst.is_centrifugation_requried = :isCentrifugationReq  "),

		/** Sample Pending Acceptance */
		@NamedNativeQuery(name = "GET_ACCEPTANCE_PENDING_LIST", query = " SELECT   "
				+ "  visit_type_master.visit_type_code AS \"visitType\",   "
				+ "  panel_master.panel_code AS \"panelCode\",   " 
				+ "  panel_master.panel_id AS \"panelId\",   "
				+ "  lbsampledtls.gender_id AS \"genderId\",   " 
				+ "  lbsampledtls.org_id AS \"orgId\",   "
				+ "  lbsampledtls.org_unit_id AS \"orgUnitId\",   "
				+ "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\",   "
				+ "  test_mst.sample_id AS \"sampleId\", "
				+ "  lbsampledtls.sample_recollect_flag AS \"sampleRecollectFlag\",   "
				+ "  lbsampledtls.sample_recollect_against_id AS \"sampleRecollectAgainstId\",   "
				+ "  pati_mst.uhid_number AS uhid,   "
				+ "  sample_mst.sample_no AS \"sampleNo\",   "
				+ "  test_mst.test_desc AS \"testDesc\",   " 
				+ "  order_master.admission_id AS \"visitAdmId\",   "
				+ "  order_master.visit_type_id AS \"visitTypeId\",   "
				+ "  lbsampledtls.dept_id AS \"deptId\",    lbsampledtls.test_id AS \"testId\",   "
				+ "  test_mst.test_type AS \"testType\",   " 
				+ "  lbsampledtls.sub_dept_id AS \"subDeptId\",   "
				+ "  lbsampledtls.patient_id AS \"patientId\",   "
				+ "  lbsampledtls.order_details_id AS \"orderDetailsId\",   "
				+ "  pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' ||"
				+ " calculate_age(pati_mst.dob)  || '/' || gen_mst.gender_code AS \"patientDetails\",   "
				+ "  CAST(lbsampledtls.patient_visit_age AS DATE) - CAST(pati_mst.dob AS DATE) AS \"patientAgeDays\",   "
				+ "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' AS \"doctorDetails\",   "
				+ "  lbsampledtls.sample_barcode AS \"sampleBarcode\",   "
				+ "  order_details.created_date AS \"orderDateTime\",   "
				+ "  lbsampledtls.sample_collection_datetime AS \"sampleCollectionDatetime\" ,  "
	            + "  to_char(order_details.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"orderDateTimeString\",  "
	            + "  to_char(lbsampledtls.sample_collection_datetime, 'DD/MM/YYYY HH24:MI:SS') AS \"sampleCollectionDatetimeString\",  "
				+ "  wardmast.ward_code AS \"wardCode\" ,  " 
				+ "  bedmst.bed_code AS \"bedNumber\",   "
				+ "  test_mst.test_code AS \"testCode\" ,  " 
				+ "  samp_mst.sample_desc AS \"sampleType\" ,  "
				+" (case when order_master.encounter_id > 0 then " +
				" ( tpsd.payment_entitlement_id ) " +
				" else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
				+ "order_details.is_dummy_service as \"isDummyService\" "
				+ "  FROM   "
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
				+ " left join adt.t_admission ta on ta.admission_id = order_master.admission_id and ta.status='A' "
				+ " left join opd.t_patient_sponsor_details tpsd "
				+ " on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
				+ " left join opd.t_patient_sponsor_details tpsd2 "
				+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
				+ "  LEFT JOIN doctor.m_doctor_master doct_mst ON order_master.ord_doctor_id = doct_mst.doctor_id   "
				+ "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = order_master.ord_doc_spl_id   "
				+ "  LEFT JOIN adt.t_admission_details admission_dtls ON order_master.admission_id = admission_dtls.admission_id "
			    + "  and admission_dtls.status = 'A' " 
				+ "  and admission_dtls.nursing_acceptance = 'Y' "
				+ "  LEFT JOIN adt.m_ward_master wardmast ON admission_dtls.ward_id = wardmast.ward_id   "
				+ "  LEFT JOIN adt.m_bed_master bedmst ON admission_dtls.bed_id = bedmst.bed_id   "
				+ "  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id   "
				+ " left join billing.t_credit_note_detail tcnd on "
				+ " tcnd.order_detail_id = order_details.order_details_id "
				+ " and tcnd.status = 'A' "
				+ " and tcnd.cancel_or_discount = 1 "
				+ "WHERE   " 
				+ "  lbsampledtls.sample_status_id IN(:sampleStatusId) "
				+ "  AND lbsampledtls.org_id = :orgId   " 
				+ "  AND lbsampledtls.org_unit_id = :orgUnitId   "
				+ "  AND lbsampledtls.dept_id = :deptId   " 
				+ "  AND lbsampledtls.sub_dept_id = :subDeptId   "
				+ "  and order_details.ord_cancelled = 'N' "
				+ "  and order_details.status = 'A' "
				+ " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
				+ "  AND lbsampledtls.is_outsourced = 'N' "),

		/** Sample Pending Acceptance Count */
		@NamedNativeQuery(name = "GET_ACCEPTANCE_PENDING_COUNT", query = "  SELECT  " + "  COUNT(*)  " + "FROM  "
				+ "  lab.t_lab_sample_dtls lbsampledtls  "
				+ "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id   "
				+ "  INNER JOIN public.t_order_master order_master ON lbsampledtls.order_id = order_master.order_id   "
				+ " WHERE  "
				+ "  lbsampledtls.sample_status_id IN(:sampleStatusId) " 
				+ "  AND lbsampledtls.org_id = :orgId  "
				+ "  AND lbsampledtls.org_unit_id = :orgUnitId  " 
				+ "  AND lbsampledtls.dept_id = :deptId  "
				+ "  AND lbsampledtls.is_outsourced = 'N' "
				+ "  AND lbsampledtls.sub_dept_id = :subDeptId  "),

		/** List Rejected Sample */
		@NamedNativeQuery(name = "GET_REJECTED_SAMPLE_LIST", query = " SELECT "
				+ "  visit_mst.visit_type_code AS \"visitType\", "
				 + "    bill_mst.bill_number as \"billNumber\", " 
				+ "  order_mst.encounter_id AS \"encounterId\", "
				+ "  order_mst.admission_id AS \"admissionId\", "
				+ "  order_mst.visit_type_id AS \"visitTypeId\", "
				+ "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\", "
				+ "  lbsampledtls.profile_id  AS \"profileId\", "
				+ "  panel_master.panel_code AS \"panelCode\", " 
				+ "  lbsampledtls.patient_id AS \"patientId\", "
				+ "  lbsampledtls.doctor_id AS \"doctorId\", " 
				+ "  lbsampledtls.priority_id AS \"priorityId\", "
				+ "  lbsampledtls.order_details_id AS \"orderDetailsId\", " 
				+ "  lbsampledtls.org_id AS \"orgId\", "
				+ "  lbsampledtls.org_unit_id AS \"orgUnitId\", " 
				+ "  lbsampledtls.order_id AS \"orderId\", "
				+ "  lbsampledtls.lab_sample_id AS \"labSampleId\", "
				+ "  lbsampledtls.is_outsourced AS \"isOutsourced\", " 
				+ "  lbsampledtls.package_id AS \"packageId\", "
				+ "  lbsampledtls.created_by AS \"createdBy\", " 
				+ "  lbsampledtls.created_date AS \"createdDate\", "
				+ "  lbsampledtls.test_id AS \"testId\", " 
				+ "  lbsampledtls.gender_id AS \"genderId\", "
				+ "  lbsampledtls.sample_req_count AS \"sampleReqCount\", "
				+ "  lbsampledtls.sample_pending_count AS \"samplePendingCount\", "
				+ "  lbsampledtls.sample_barcode AS \"labSampleNo\",  "
				+ "  lbsampledtls.is_alliquote_req AS \"isAlliquoteReq\", " 
				+ "  pati_mst.uhid_number AS uhid, "
				+ "  test_mst.test_desc AS \"testDesc\", " 
				+ "  test_mst.test_code AS \"testCode\", "
				+ "  contai_mst.container_name AS \"containerName\", "
				+ "  lbsampledtls.container_id AS \"containerId\", "
				+ "  subspe_mst.sub_speciality_name AS \"deptName\", "
				+ "  lbsampledtls.sample_type_id AS \"sampleTypeId\", " 
				+ "  subspe_mst.speciality_id AS \"deptId\", "
				+ "  subspe_mst.sub_speciality_id AS \"subDeptId\", "
				+ "  pre_mst.prefix_code || ' ' || pati_mst.first_name  || ' ' || coalesce(pati_mst.middle_name)|| ' ' || pati_mst.last_name || ' ' ||calculate_age(pati_mst.dob) "
				+ "  || '/' || gen_mst.gender_code AS \"patientDetails\", "
				+ "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' AS \"doctorDetails\", "
				+ "  lbsampledtls.sample_barcode AS \"sampleBarcode\", " 
				+ "  samp_mst.sample_desc AS \"sampleType\", "
				+ "  rejection_master.reason_desc AS \"sampleRejectReason\", "
				+ "  wardmast.ward_code AS \"wardCode\", " 
				+ "  bedmst.bed_code AS \"bedNumber\", "
				+ "  test_mst.sample_volume AS \"sampleVolume\", " 
				+ "  unit_mst.unit_name AS \"unitName\","
				+ " lbsampledtls.assign_unit AS \"assignUnit\","
				+ " lbsampledtls.perform_test_id AS \"performTestId\","
				+ " lbsampledtls.performed_unit AS \"performedUnit\", "
				+" (case when order_mst.encounter_id > 0 then " +
				" ( tpsd.payment_entitlement_id ) " +
				" else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
	            + "order_details.is_dummy_service as \"isDummyService\" "
				+ " FROM "
				+ "  lab.t_lab_sample_dtls lbsampledtls "
				+ "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id "
				+ "  INNER JOIN public.t_order_master order_mst ON order_mst.order_id = order_details.order_id "
				 + " LEFT JOIN billing.t_billing_details bill_dtls on " 
	    		 + " 	order_details.order_details_id = bill_dtls.order_details_id " 
	    		 + " LEFT JOIN billing.m_billing_master bill_mst on " 
	    		 + " 	bill_mst.billing_master_id = bill_dtls.billing_master_id " 
	    		// + " 	and bill_mst.bill_cancelled = 'N' " 
//	    		 + " 	and bill_mst.bill_status_id <> 1 " 
				+ "  INNER JOIN lab.t_lab_sample_master sample_mst ON lbsampledtls.lab_sample_id = sample_mst.lab_sample_id  "
				+ "  INNER JOIN patient.t_patient_registration pati_mst ON pati_mst.patient_id = order_mst.patient_id "
				+ "  INNER JOIN public.m_visit_type_master visit_mst ON visit_mst.visit_type_id = order_mst.visit_type_id "
				+ "  INNER JOIN m_prefix_master pre_mst ON pati_mst.prefix_id = pre_mst.prefix_id "
				+ "  INNER JOIN m_gender_master gen_mst ON pati_mst.gender_id = gen_mst.gender_id "
				+ " left join adt.t_admission ta on ta.admission_id = order_mst.admission_id and ta.status='A' "
				+ " left join opd.t_patient_sponsor_details tpsd "
				+ " on order_mst.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
				+ " left join opd.t_patient_sponsor_details tpsd2 "
				+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
				+ "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id "
				+ "  INNER JOIN lab.m_unit_master unit_mst ON unit_mst.unit_id = test_mst.unit_id "
				+ "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_mst.sample_id "
				+ "  INNER JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id "
				+ "  LEFT JOIN doctor.m_doctor_master doct_mst ON order_mst.ord_doctor_id = doct_mst.doctor_id "
				+ "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = order_mst.ord_doc_spl_id  "
				+ "  LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id "
				+ "  LEFT JOIN adt.t_admission_details admission_dtls ON order_mst.admission_id = admission_dtls.admission_id "
			    + "  and admission_dtls.status = 'A' " 
				+ "  and admission_dtls.nursing_acceptance = 'Y' "
				+ "  LEFT JOIN adt.m_reason_master rejection_master ON rejection_master.reason_master_id = lbsampledtls.sample_reject_reason_id "
				+ "  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id "
				+ "  LEFT JOIN adt.m_ward_master wardmast ON admission_dtls.ward_id = wardmast.ward_id "
				+ "  LEFT JOIN adt.m_bed_master bedmst ON admission_dtls.bed_id = bedmst.bed_id " 
				+ " left join billing.t_credit_note_detail tcnd on "
				+ " tcnd.order_detail_id = order_details.order_details_id "
				+ " and tcnd.status = 'A' "
				+ " and tcnd.cancel_or_discount = 1 "
				+ " WHERE "
				+ "  lbsampledtls.sample_status_id IN(:sampleStatusIds) " 
				+ "  AND lbsampledtls.org_id = :orgId "
				+ "  AND lbsampledtls.dept_id = :deptId " 
				+ "  AND lbsampledtls.org_unit_id = :orgUnitId "
				+ "  and order_details.ord_cancelled = 'N' "
				+ "  and order_details.status = 'A' "
				+ "and (case when  order_mst.visit_type_id not in(1) then (case when bill_mst.billing_master_id  is not null then  bill_mst.bill_cancelled = 'N' else bill_mst.billing_master_id  is null  end )  else bill_mst.billing_master_id  is not null and bill_mst.bill_cancelled = 'N' end) "
				+ " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "

			),

		/** GET COUNT FOR REJECTED SAMPLE */
		@NamedNativeQuery(name = "GET_COUNT_FOR_REJECTED_SAMPLE", query = " SELECT " + "  count(*) " + "FROM "
				+ "	lab.t_lab_sample_dtls lbsampledtls " 
				+ "  INNER JOIN public.t_order_master order_mst ON order_mst.order_id = lbsampledtls.order_id "
				+ "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id   "
				+ "  WHERE "
				+ "	 lbsampledtls.sample_status_id IN (:sampleStatusIds) " 
				+ "  AND lbsampledtls.org_id = :orgId "
				+ "  AND lbsampledtls.dept_id = :deptId " 
				+ "  AND lbsampledtls.org_unit_id = :orgUnitId "),
		/* + " AND test_mst.is_outsourced = 'N' " */

		@NamedNativeQuery(name = "UPDATE_SAMPLE_AFTER_RECOLLECTION", query = "UPDATE " + "	lab.t_lab_sample_dtls "
				+ "SET " + "	sample_recollect_flag =:sampleRecollectFlag, " + "	sample_status_id =:sampleStatusId, "
				+ "	updated_by =:updatedBy, " + "	updated_date =:updatedDate " + " WHERE "
				+ "	lab_sample_dtls_id =:labSampleDtlsId "),

		@NamedNativeQuery(name = "GENERATE_SAMPLE_NO", query = "SELECT nextval('lab.sample_no_genarator');"),

		/** Biochemistry Worklist */
		@NamedNativeQuery(name = "GET_BIOCHEMISTRY_WORKLIST", query = "  SELECT  "
				+ "  visit_type_master.visit_type_code AS \"visitType\",  "
				+ "  panel_master.panel_code AS \"panelCode\",  "
				+ "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\",  "
				+ "  lbsampledtls.is_alliquote_req AS \"isAlliquoteReq\",  " 
				+ "  pati_mst.uhid_number AS uhid,  "
				+ "  test_mst.test_desc AS \"testDesc\",  " 
				+ "  test_mst.test_code AS \"testCode\",  "
				+ " samp_mst.sample_desc  AS \"sampleType\",  "
                + "  pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || calculate_age(pati_mst.dob) "
				+ " || '/' || gen_mst.gender_code AS \"patientDetails\",  "
				+ "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' AS \"doctorDetails\",  "
				+ "  wardmast.ward_code AS \"wardCode\",  " + "  bedmst.bed_number AS \"bedNumber\",  "
				+ "  lbsampledtls.sample_barcode AS \"sampleBarcode\",  "
				+ "  sample_mst.sample_no AS \"labSampleNo\",  "
				+ "  order_details.created_date AS \"orderDateTime\",  "
				+ "  lbsampledtls.sample_collection_datetime AS \"sampleCollectionDatetime\" , "
	            + "  to_char(order_details.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"orderDateTimeString\",  "
	            + "  to_char(lbsampledtls.sample_collection_datetime, 'DD/MM/YYYY HH24:MI:SS') AS \"sampleCollectionDatetimeString\",  "
				+" (case when order_master.encounter_id > 0 then " +
				" ( tpsd.payment_entitlement_id ) " +
				" else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
	             + "order_details.is_dummy_service as \"isDummyService\" "
				+ " FROM  "
				+ "  lab.t_lab_sample_dtls lbsampledtls  "
				+ "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id  "
				+ "  INNER JOIN public.t_order_master order_master ON order_master.order_id = order_details.order_id  "
				+ "  INNER JOIN lab.t_lab_sample_master sample_mst ON lbsampledtls.lab_sample_id = sample_mst.lab_sample_id  "
				+ "  INNER JOIN public.m_visit_type_master visit_type_master ON visit_type_master.visit_type_id = order_master.visit_type_id  "
				+ "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id  "
				+ "  INNER JOIN patient.t_patient_registration pati_mst ON lbsampledtls.patient_id = pati_mst.patient_id  "
				+ "  INNER JOIN m_prefix_master pre_mst ON pati_mst.prefix_id = pre_mst.prefix_id  "
				+ "  INNER JOIN m_gender_master gen_mst ON pati_mst.gender_id = gen_mst.gender_id  "
				+ "  INNER JOIN lab.m_test_master test_master ON test_master.test_id = lbsampledtls.test_id  "
				+ "  INNER JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id  "
				+ "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_master.sample_id "
				+ "  LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id  "
				+ "  LEFT JOIN doctor.m_doctor_master doct_mst ON order_master.ord_doctor_id = doct_mst.doctor_id  "
				+ "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = order_master.ord_doc_spl_id   "
				+ " left join adt.t_admission ta on ta.admission_id = order_master.admission_id and ta.status='A' "
				+ " left join opd.t_patient_sponsor_details tpsd "
				+ " on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
				+ " left join opd.t_patient_sponsor_details tpsd2 "
				+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
				+ "  LEFT JOIN adt.t_admission_details admission_dtls ON order_master.admission_id = admission_dtls.admission_id "
			    + "  and admission_dtls.status = 'A' " 
				+ "  and admission_dtls.nursing_acceptance = 'Y' "
				+ "  LEFT JOIN adt.m_ward_master wardmast ON admission_dtls.ward_id = wardmast.ward_id  "
				+ "  LEFT JOIN adt.m_bed_master bedmst ON admission_dtls.bed_id = bedmst.bed_id  "
				+ "  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id  "
				+ " left join billing.t_credit_note_detail tcnd on "
				+ " tcnd.order_detail_id = order_details.order_details_id "
				+ " and tcnd.status = 'A' "
				+ " and tcnd.cancel_or_discount = 1 "
				+ "WHERE  " 
				+ "  lbsampledtls.sample_status_id IN (:sampleStatusIds)  "
				+ "  AND lbsampledtls.org_id = :orgId  " 
				+ "  AND lbsampledtls.org_unit_id = :orgUnitId  "
				+ "  AND lbsampledtls.dept_id = :deptId  " 
				+ "  AND lbsampledtls.sub_dept_id = :subDeptId  "
				+ "  and order_details.ord_cancelled = 'N' "
				+ "  and order_details.status = 'A' "
				+ " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
				+ "  AND lbsampledtls.is_outsourced = 'N' "	),

		@NamedNativeQuery(name = "GET_BIOCHEMISTRY_WORKLIST_COUNT", query = "  SELECT  " + "  COUNT(*)  " + "FROM  "
				+ "  lab.t_lab_sample_dtls lbsampledtls  " 
				+ "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id   "
				+ "  INNER JOIN public.t_order_master order_master ON lbsampledtls.order_id = order_master.order_id  "
				+ " WHERE  "
				+ "  lbsampledtls.sample_status_id IN (:sampleStatusIds)  " 
				+ "  AND lbsampledtls.org_id = :orgId  "
				+ "  AND lbsampledtls.org_unit_id = :orgUnitId  " 
				+ "  AND lbsampledtls.dept_id = :deptId  "
				+ "  AND lbsampledtls.sub_dept_id = :subDeptId  "
				+ "  AND lbsampledtls.is_outsourced = 'N' "),

		@NamedNativeQuery(name = "GET_FIRST_LEVEL_RESULT", query = "SELECT labresdtls.first_level_result FROM lab.t_lab_res_dtls labresdtls "
				+ " WHERE labresdtls.lab_result_id = :labResultId " + " AND labresdtls.org_id = :orgId "
				+ " AND labresdtls.org_unit_id = :orgUnitId "),

		@NamedNativeQuery(name = "LAB_PATIENT_SEARCH", query =  "select "
				+ "	distinct pati_mst.patient_id as \"id\", "
				+ "	pati_mst.first_name as \"firstName\", "
				+ "	pati_mst.last_name as \"lastName\", "
				+ "	pati_mst.uhid_number as \"uhid\" "
				+ "from "
				+ "	public.t_order_master ord_mst "
				+ "inner join patient.t_patient_registration pati_mst on "
				+ "	ord_mst.patient_id = pati_mst.patient_id "
				+ "where "
				+ "	ord_mst.org_id =:orgId "
				//+ "	and ord_mst.org_unit_id =:orgUnitId "
				+ "	and ( lower(pati_mst.first_name||' '||pati_mst.last_name) like (:searchKeyword) "
				+ "	or lower(pati_mst.last_name) like lower(:searchKeyword) "
				+ "	or lower(pati_mst.uhid_number) like lower(:searchKeyword))"),

		@NamedNativeQuery(name = "SEARCH_SAMPLE_NO", query = " select "
				+ "	distinct sample_mst.sample_id as \"sampleNo\", "
				+ "	sample_mst.lab_sample_id as \"labSampleId\" "
				+ "from "
				+ "    lab.t_lab_sample_master sample_mst "
				+ "    inner join lab.t_lab_sample_dtls dtls on sample_mst.lab_sample_id = dtls.lab_sample_id "
				+ "where "
				+ "	sample_mst.org_id =:orgId "
				+ "	and sample_mst.org_unit_id =:orgUnitId "
				+ "	and dtls.dept_id =:deptId "
				+ "	and dtls.sub_dept_id =:subDeptId "
				+ "	and (cast(sample_mst.sample_id as text) like (:searchKeyword))" ),
		
		@NamedNativeQuery(name = "GET_REJECTED_SAMPLE_LIST_PATIENT_SEARCH", query = "  SELECT "
				 + " DISTINCT(pati_mst.patient_id) as \"id\",  pati_mst.first_name as \"name\",  pati_mst.uhid_number as \"uhid\", pati_mst.first_name as \"firstName\",pati_mst.last_name as \"lastName\","  
				+"   pati_mst.first_name || ' ' || pati_mst.last_name || '' || pati_mst.uhid_number AS \"patientWithUhid\" " 
				+" FROM "
				+"  lab.t_lab_sample_dtls lbsampledtls "  
				+"  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id "  
				+"  INNER JOIN public.t_order_master order_mst ON order_mst.order_id = order_details.order_id "  
				+"  INNER JOIN patient.t_patient_registration pati_mst ON pati_mst.patient_id = order_mst.patient_id " 
				+"  INNER JOIN m_prefix_master pre_mst ON pati_mst.prefix_id = pre_mst.prefix_id "
				+"  INNER JOIN m_gender_master gen_mst ON pati_mst.gender_id = gen_mst.gender_id "  
				+ " WHERE "
				+ "  lbsampledtls.sample_status_id IN(:sampleStatusIds) " 
				+ "  AND lbsampledtls.org_id = :orgId "
				+ "  AND lbsampledtls.dept_id = :deptId " 
				+ "  AND lbsampledtls.org_unit_id = :orgUnitId "
				+ "  and order_details.ord_cancelled = 'N' "
				+ "  and order_details.status = 'A' ")

})
@Entity
@Table(name = "t_lab_sample_dtls", schema = "lab")
@SequenceGenerator(name = "m_seq_lab_sample_dtls", sequenceName = "lab.m_seq_lab_sample_dtls", allocationSize = 1)
public class LabSampleDetailsMaster {
	@Id
	@Column(name = "lab_sample_dtls_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_seq_lab_sample_dtls")
	private int labSampleDtlsId;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "org_unit_id")
	private Integer orgUnitId;

	@Column(name = "test_id")
	private Integer testId;

	@Column(name = "dept_id")
	private Integer deptId;

	@Column(name = "sub_dept_id")
	private Integer subDeptId;

	@Column(name = "lab_sample_id")
	private Integer labSampleId;

	@Column(name = "sample_barcode")
	private String sampleBarcode;

	@Column(name = "sample_no")
	private BigInteger sampleNo;

	@Column(name = "order_details_id")
	private Integer orderDetailsId;

	@Column(name = "order_id")
	private Integer orderId;

	@Column(name = "profile_id")
	private Integer profileId;

	@Column(name = "package_id")
	private Integer packageId;

	@Column(name = "is_centrifugation_req")
	private Character isCentrifugationReq;

	@Column(name = "is_alliquote_req")
	private Character isAlliquoteReq;

	@Column(name = "sample_type_id")
	private Integer sampleTypeId;

	@Column(name = "container_id")
	private Integer containerId;

	@Column(name = "sample_req_count")
	private Integer sampleReqCount;

	@Column(name = "sample_pending_count")
	private Integer samplePendingCount;

	@Column(name = "sample_gen_datetime")
	@Convert(converter = LocalTimeConverter.class)
	private Long sampleGenDatetime;

	@Column(name = "sample_gen_by")
	private Integer sampleGenBy;

	@Column(name = "curr_status")
	private Character currStatus;

	@Column(name = "sample_status_id")
	private Integer sampleStatusId;

	@Column(name = "is_outsourced")
	private Character isOutsourced;

	@Column(name = "outsourced_lab_id")
	private Integer outsourcedLabId;

	@Column(name = "patient_id")
	private Integer patientId;

	@Column(name = "doctor_id")
	private Integer doctorId;

	@Column(name = "priority_id")
	private Integer priorityId;

	@Column(name = "samp_wrk_status_id")
	private Integer sampWrkStatusId;

	@Column(name = "sample_sendtocr_datetime")
	@Convert(converter = LocalTimeConverter.class)
	private Long sampleSendtocrDatetime;

	@Column(name = "sample_sendtocr_by")
	private Integer sampleSendtocrBy;

	@Column(name = "sample_collection_datetime")
	@Convert(converter = LocalTimeConverter.class)
	private Long sampleCollectionDatetime;

	@Column(name = "sample_collection_by")
	private Integer sampleCollectionBy;

	@Column(name = "sample_centrifugation_datetime")
	@Convert(converter = LocalTimeConverter.class)
	private Long sampleCentrifugationDatetime;

	@Column(name = "sample_centrifugation_by")
	private Integer sampleCentrifugationBy;

	@Column(name = "sample_accept_datetime")
	@Convert(converter = LocalTimeConverter.class)
	private Long sampleAcceptDatetime;

	@Column(name = "sample_accept_by")
	private Integer sampleAcceptBy;

	@Column(name = "sample_reject_datetime")
	@Convert(converter = LocalTimeConverter.class)
	private Long sampleRejectDatetime;

	@Column(name = "sample_reject_by")
	private Integer sampleRejectBy;

	@Column(name = "sample_reject_reason_id")
	private Integer sampleRejectReasonId;

	@Column(name = "sample_reject_reason")
	private String sampleRejectReason;

	@Column(name = "sample_recollect_flag")
	private Character sampleRecollectFlag;

	@Column(name = "sample_recollect_against_id")
	private Integer sampleRecollectAgainstId;

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

	@Column(name = "sample_test_time")
	@Convert(converter = LocalTimeConverter.class)
	private Long sampleTestTime;

	@Column(name = "patient_visit_age")
	@Convert(converter = LocalTimeConverter.class)
	private Long patientVisitAge;
	
	@Column(name = "review_time")
	@Convert(converter = LocalTimeConverter.class)
	private Long reviewTime;

	@Column(name = "gender_id",updatable = false)
	private Integer genderId;
	
	@Column(name = "assign_unit")
	private Integer assignUnit;

	@Column(name = "performed_unit")
	private Integer performedUnit;
	
	@Column(name = "micro_template_status_id")
	private Integer microTemplateStatusId;
	
	@Column(name = "perform_test_id")
	private Integer performTestId;
	
	@Column(name = "p_image")
	private String pImage;
	
	@Column(name = "p_image_name")
	private String pImageName;
	
	@Column(name = "internal_notes")
	private String internalNotes;
	
	@Column(name = "srf_id")
	private String srfId;
	
	@Column(name = "icmr_id")
	private String icmrId;
	
    @Column(name = "is_on_hold_parameter")
    private boolean isOnHoldParameter;
    
    @Column(name = "provisional")
    private Boolean provisional;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lab_sample_id", insertable = false, nullable = false, updatable = false)
	private LabSampleMaster labSampleMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sample_status_id", insertable = false, nullable = false, updatable = false)
	private SampleStatusMaster sampleStatusMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sub_dept_id", insertable = false, nullable = false, updatable = false)
	private LabConfigurationMaster labConfigurationMaster;

	/*
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "order_id", insertable = false, nullable = false,
	 * updatable = false) private OrderMaster orderMaster;
	 */

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gender_id", insertable = false, nullable = false, updatable = false)
	private GenderMaster genderMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_details_id", insertable = false, nullable = false, updatable = false)
	private OrderDetailsMaster orderDetailsMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", insertable = false, nullable = false, updatable = false)
	private OrderMaster orderMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, nullable = false, updatable = false)
	private PatientRegistration patientRegistration;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id", insertable = false, nullable = false, updatable = false)
	private DoctorMaster doctorMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test_id", insertable = false, nullable = false, updatable = false)
	private TestMaster testMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "perform_test_id", insertable = false, nullable = false, updatable = false)
	private TestMaster performTestMaster;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "labSampleDetailsMaster")
	private List<TTemplateResult> listTTemplateResult;
	
	@Column(name="is_mail_send")
	private boolean isMailSend;
	
	@Column(name="is_report_recall")
	private boolean isReportRecall;
	
	@Column(name = "manual_sample_collection_date_time")
	@Convert(converter = LocalTimeConverter.class)
	private Long manualSampleCollectionDateTimeInLong;

	public int getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(int labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
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

	public Integer getSubDeptId() {
		return subDeptId;
	}

	public void setSubDeptId(Integer subDeptId) {
		this.subDeptId = subDeptId;
	}

	public Integer getLabSampleId() {
		return labSampleId;
	}

	public void setLabSampleId(Integer labSampleId) {
		this.labSampleId = labSampleId;
	}

	public String getSampleBarcode() {
		return sampleBarcode;
	}

	public void setSampleBarcode(String sampleBarcode) {
		this.sampleBarcode = sampleBarcode;
	}

	public BigInteger getSampleNo() {
		return sampleNo;
	}

	public void setSampleNo(BigInteger sampleNo) {
		this.sampleNo = sampleNo;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getProfileId() {
		return profileId;
	}

	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Character getIsCentrifugationReq() {
		return isCentrifugationReq;
	}

	public void setIsCentrifugationReq(Character isCentrifugationReq) {
		this.isCentrifugationReq = isCentrifugationReq;
	}

	public Character getIsAlliquoteReq() {
		return isAlliquoteReq;
	}

	public void setIsAlliquoteReq(Character isAlliquoteReq) {
		this.isAlliquoteReq = isAlliquoteReq;
	}

	public Integer getSampleTypeId() {
		return sampleTypeId;
	}

	public void setSampleTypeId(Integer sampleTypeId) {
		this.sampleTypeId = sampleTypeId;
	}

	public Integer getContainerId() {
		return containerId;
	}

	public void setContainerId(Integer containerId) {
		this.containerId = containerId;
	}

	public Integer getSampleReqCount() {
		return sampleReqCount;
	}

	public void setSampleReqCount(Integer sampleReqCount) {
		this.sampleReqCount = sampleReqCount;
	}

	public Integer getSamplePendingCount() {
		return samplePendingCount;
	}

	public void setSamplePendingCount(Integer samplePendingCount) {
		this.samplePendingCount = samplePendingCount;
	}

	public Long getSampleGenDatetime() {
		return sampleGenDatetime;
	}

	public void setSampleGenDatetime(Long sampleGenDatetime) {
		this.sampleGenDatetime = sampleGenDatetime;
	}

	public Integer getSampleGenBy() {
		return sampleGenBy;
	}

	public void setSampleGenBy(Integer sampleGenBy) {
		this.sampleGenBy = sampleGenBy;
	}

	public Character getCurrStatus() {
		return currStatus;
	}

	public void setCurrStatus(Character currStatus) {
		this.currStatus = currStatus;
	}

	public Character getIsOutsourced() {
		return isOutsourced;
	}

	public void setIsOutsourced(Character isOutsourced) {
		this.isOutsourced = isOutsourced;
	}

	public Integer getOutsourcedLabId() {
		return outsourcedLabId;
	}

	public void setOutsourcedLabId(Integer outsourcedLabId) {
		this.outsourcedLabId = outsourcedLabId;
	}

	public Long getSampleSendtocrDatetime() {
		return sampleSendtocrDatetime;
	}

	public void setSampleSendtocrDatetime(Long sampleSendtocrDatetime) {
		this.sampleSendtocrDatetime = sampleSendtocrDatetime;
	}

	public Integer getSampleSendtocrBy() {
		return sampleSendtocrBy;
	}

	public void setSampleSendtocrBy(Integer sampleSendtocrBy) {
		this.sampleSendtocrBy = sampleSendtocrBy;
	}

	public Long getSampleCollectionDatetime() {
		return sampleCollectionDatetime;
	}

	public void setSampleCollectionDatetime(Long sampleCollectionDatetime) {
		this.sampleCollectionDatetime = sampleCollectionDatetime;
	}

	public Integer getSampleCollectionBy() {
		return sampleCollectionBy;
	}

	public void setSampleCollectionBy(Integer sampleCollectionBy) {
		this.sampleCollectionBy = sampleCollectionBy;
	}

	public Long getSampleCentrifugationDatetime() {
		return sampleCentrifugationDatetime;
	}

	public void setSampleCentrifugationDatetime(Long sampleCentrifugationDatetime) {
		this.sampleCentrifugationDatetime = sampleCentrifugationDatetime;
	}

	public Integer getSampleCentrifugationBy() {
		return sampleCentrifugationBy;
	}

	public void setSampleCentrifugationBy(Integer sampleCentrifugationBy) {
		this.sampleCentrifugationBy = sampleCentrifugationBy;
	}

	public Long getSampleAcceptDatetime() {
		return sampleAcceptDatetime;
	}

	public void setSampleAcceptDatetime(Long sampleAcceptDatetime) {
		this.sampleAcceptDatetime = sampleAcceptDatetime;
	}

	public Integer getSampleAcceptBy() {
		return sampleAcceptBy;
	}

	public void setSampleAcceptBy(Integer sampleAcceptBy) {
		this.sampleAcceptBy = sampleAcceptBy;
	}

	public Long getSampleRejectDatetime() {
		return sampleRejectDatetime;
	}

	public void setSampleRejectDatetime(Long sampleRejectDatetime) {
		this.sampleRejectDatetime = sampleRejectDatetime;
	}

	public Integer getSampleRejectBy() {
		return sampleRejectBy;
	}

	public void setSampleRejectBy(Integer sampleRejectBy) {
		this.sampleRejectBy = sampleRejectBy;
	}

	public Integer getSampleRejectReasonId() {
		return sampleRejectReasonId;
	}

	public void setSampleRejectReasonId(Integer sampleRejectReasonId) {
		this.sampleRejectReasonId = sampleRejectReasonId;
	}

	public String getSampleRejectReason() {
		return sampleRejectReason;
	}

	public void setSampleRejectReason(String sampleRejectReason) {
		this.sampleRejectReason = sampleRejectReason;
	}

	public Character getSampleRecollectFlag() {
		return sampleRecollectFlag;
	}

	public void setSampleRecollectFlag(Character sampleRecollectFlag) {
		this.sampleRecollectFlag = sampleRecollectFlag;
	}

	public Integer getSampleRecollectAgainstId() {
		return sampleRecollectAgainstId;
	}

	public void setSampleRecollectAgainstId(Integer sampleRecollectAgainstId) {
		this.sampleRecollectAgainstId = sampleRecollectAgainstId;
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

	public Integer getSampleStatusId() {
		return sampleStatusId;
	}

	public void setSampleStatusId(Integer sampleStatusId) {
		this.sampleStatusId = sampleStatusId;
	}

	public LabSampleMaster getLabSampleMaster() {
		return labSampleMaster;
	}

	public void setLabSampleMaster(LabSampleMaster labSampleMaster) {
		this.labSampleMaster = labSampleMaster;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getSampWrkStatusId() {
		return sampWrkStatusId;
	}

	public void setSampWrkStatusId(Integer sampWrkStatusId) {
		this.sampWrkStatusId = sampWrkStatusId;
	}

	public SampleStatusMaster getSampleStatusMaster() {
		return sampleStatusMaster;
	}

	public void setSampleStatusMaster(SampleStatusMaster sampleStatusMaster) {
		this.sampleStatusMaster = sampleStatusMaster;
	}

	/*
	 * public OrderMaster getOrderMaster() { return orderMaster; }
	 * 
	 * public void setOrderMaster(OrderMaster orderMaster) { this.orderMaster =
	 * orderMaster; }
	 */

	public OrderDetailsMaster getOrderDetailsMaster() {
		return orderDetailsMaster;
	}

	public void setOrderDetailsMaster(OrderDetailsMaster orderDetailsMaster) {
		this.orderDetailsMaster = orderDetailsMaster;
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

	public Long getSampleTestTime() {
		return sampleTestTime;
	}

	public void setSampleTestTime(Long sampleTestTime) {
		this.sampleTestTime = sampleTestTime;
	}

	public Long getPatientVisitAge() {
		return patientVisitAge;
	}

	public void setPatientVisitAge(Long patientVisitAge) {
		this.patientVisitAge = patientVisitAge;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public GenderMaster getGenderMaster() {
		return genderMaster;
	}

	public void setGenderMaster(GenderMaster genderMaster) {
		this.genderMaster = genderMaster;
	}

	public PatientRegistration getPatientRegistration() {
		return patientRegistration;
	}

	public void setPatientRegistration(PatientRegistration patientRegistration) {
		this.patientRegistration = patientRegistration;
	}

	public DoctorMaster getDoctorMaster() {
		return doctorMaster;
	}

	public void setDoctorMaster(DoctorMaster doctorMaster) {
		this.doctorMaster = doctorMaster;
	}

	public TestMaster getTestMaster() {
		return testMaster;
	}

	public void setTestMaster(TestMaster testMaster) {
		this.testMaster = testMaster;
	}

	public List<TTemplateResult> getListTTemplateResult() {
		return listTTemplateResult;
	}

	public void setListTTemplateResult(List<TTemplateResult> listTTemplateResult) {
		this.listTTemplateResult = listTTemplateResult;
	}

	public OrderMaster getOrderMaster() {
		return orderMaster;
	}

	public void setOrderMaster(OrderMaster orderMaster) {
		this.orderMaster = orderMaster;
	}

	public Integer getAssignUnit() {
		return assignUnit;
	}

	public void setAssignUnit(Integer assignUnit) {
		this.assignUnit = assignUnit;
	}

	public Integer getPerformedUnit() {
		return performedUnit;
	}

	public void setPerformedUnit(Integer performedUnit) {
		this.performedUnit = performedUnit;
	}

	public Long getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Long reviewTime) {
		this.reviewTime = reviewTime;
	}

	public Integer getMicroTemplateStatusId() {
		return microTemplateStatusId;
	}

	public void setMicroTemplateStatusId(Integer microTemplateStatusId) {
		this.microTemplateStatusId = microTemplateStatusId;
	}

	public Integer getPerformTestId() {
		return performTestId;
	}

	public void setPerformTestId(Integer performTestId) {
		this.performTestId = performTestId;
	}

	public TestMaster getPerformTestMaster() {
		return performTestMaster;
	}

	public void setPerformTestMaster(TestMaster performTestMaster) {
		this.performTestMaster = performTestMaster;
	}

	public String getpImage() {
		return pImage;
	}

	public void setpImage(String pImage) {
		this.pImage = pImage;
	}

	public String getpImageName() {
		return pImageName;
	}

	public void setpImageName(String pImageName) {
		this.pImageName = pImageName;
	}

	public boolean getIsMailSend() {
		return isMailSend;
	}

	public void setIsMailSend(boolean isMailSend) {
		this.isMailSend = isMailSend;
	}

	public String getInternalNotes() {
		return internalNotes;
	}

	public void setInternalNotes(String internalNotes) {
		this.internalNotes = internalNotes;
	}

	public String getSrfId() {
		return srfId;
	}

	public void setSrfId(String srfId) {
		this.srfId = srfId;
	}

	public String getIcmrId() {
		return icmrId;
	}

	public void setIcmrId(String icmrId) {
		this.icmrId = icmrId;
	}

	public boolean getIsReportRecall() {
		return isReportRecall;
	}

	public void setIsReportRecall(boolean isReportRecall) {
		this.isReportRecall = isReportRecall;
	}
    public boolean getIsOnHoldParameter() {
        return isOnHoldParameter;
    }

    public void setIsOnHoldParameter(boolean isOnHoldParameter) {
        this.isOnHoldParameter = isOnHoldParameter;
    }

	public Long getManualSampleCollectionDateTimeInLong() {
		return manualSampleCollectionDateTimeInLong;
	}

	public void setManualSampleCollectionDateTimeInLong(Long manualSampleCollectionDateTimeInLong) {
		this.manualSampleCollectionDateTimeInLong = manualSampleCollectionDateTimeInLong;
	}

	public Boolean isProvisional() {
		return provisional;
	}

	public void setProvisional(Boolean provisional) {
		this.provisional = provisional;
	}

}
