package com.param.entity.lis.transaction;

import java.util.List;
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

@NamedNativeQueries({

    @NamedNativeQuery(name = "GET_BIOCHEM_RESULT_ENTRY_LIST", query = "  SELECT  "
        + "  visit_type_master.visit_type_code AS \"visitType\",  "
		 + "    bill_mst.bill_number as \"billNumber\", " 
        + "  lbsampledtls.org_id AS \"orgId\",  "
        + "  order_details.priority_id AS \"priorityId\", "
		+ "  lbsampledtls.org_unit_id AS \"orgUnitId\",  "
        + "  panel_master.panel_code AS \"panelCode\",  "
        + "  panel_master.panel_id AS \"panelId\",  "
        + "  lbsampledtls.gender_id AS \"genderId\",  " 
        + "  lbsampledtls.is_on_hold_parameter AS \"isOnHoldParameter\",  " 
        + "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\",  "
        + "  lbsampledtls.sample_recollect_flag AS \"sampleRecollectFlag\",  "
        + "  lbsampledtls.sample_recollect_against_id AS \"sampleRecollectAgainstId\",  "
        + "  pati_mst.uhid_number AS uhid,  "
        + "  sample_mst.sample_id AS \"sampleNo\",  "
        + "  test_mst.test_desc AS \"testDesc\",  "
        + "  order_master.admission_id AS \"visitAdmId\",  "
        + "  order_master.visit_type_id AS \"visitTypeId\",  "
        + "  lbsampledtls.dept_id AS \"deptId\",  " 
        + "  lbsampledtls.test_id AS \"testId\",  "
        + "  test_mst.test_type AS \"testType\",  "
        + "  lbsampledtls.sub_dept_id AS \"subDeptId\",  "
        + "  lbsampledtls.patient_id AS \"patientId\",  "
        + "  lbsampledtls.order_details_id AS \"orderDetailsId\",  "
        + " pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || "
        + " calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code AS \"patientDetails\",  "
        + "  CAST(lbsampledtls.patient_visit_age AS DATE) - CAST(pati_mst.dob AS DATE) AS \"patientAgeDays\",  "
        + "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' AS \"doctorDetails\",  "
        + "  lbsampledtls.sample_barcode AS \"sampleBarcode\",  "
        + "  order_details.created_date AS \"orderDateTime\",  "
        + "  lbsampledtls.sample_collection_datetime AS \"sampleCollectionDatetime\" , "
        + "  to_char(order_details.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"orderDateTimeString\",  "
        + "  to_char(lbsampledtls.sample_collection_datetime, 'DD/MM/YYYY HH24:MI:SS') AS \"sampleCollectionDatetimeString\",  "
        + "  wardmast.ward_code AS \"wardCode\" , " 
        + "  bedmst.bed_code AS \"bedNumber\",  " 
        + "  test_mst.test_code AS \"testCode\" , "
        + "  pati_mst.image_path AS \"imagePath\", " 
        + "  samp_mst.sample_desc AS \"sampleType\" ,"
        + " doct_mst.email as \"doctorMail\", "
        + " pati_mst.email as \"patientMail\", "
        +" (case when order_master.encounter_id > 0 then " +
        " ( tpsd.payment_entitlement_id ) " +
        " else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
        +" (case when order_master.encounter_id > 0 then " +
        " ( tpsd.payee_id ) " +
        " else ( tpsd2.payee_id ) end ) as \"payeeId\", "
    //    + "  (select count(*) from lab.t_sample_audit_trail sample_audit_trail where sample_audit_trail.sample_dtls_id = lbsampledtls.lab_sample_dtls_id and test_status_id = 7) as \"retestCount\", "
        +" order_master.order_no as \"orderNo\","
        + " tltr.lab_test_res_id as \"labTestResId\", "
        + "order_details.is_dummy_service as \"isDummyService\" "
        + " FROM  "
        + "  lab.t_lab_sample_dtls lbsampledtls  "
        + "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id  "
        + " left join billing.t_billing_details bill_dtls on " 
		 + " 	order_details.order_details_id = bill_dtls.order_details_id " 
		 + " left join billing.m_billing_master bill_mst on " 
		 + " 	bill_mst.billing_master_id = bill_dtls.billing_master_id " 
        + "  LEFT JOIN lab.t_lab_test_result tltr ON tltr.lab_test_res_id = (select lab_test_res_id from lab.t_lab_test_result ltlsc where ltlsc.lab_sample_dtls_id = lbsampledtls.lab_sample_dtls_id"
        + " and ltlsc.status = 'A' order by ltlsc.lab_test_res_id desc  limit 1) "
        + "  INNER JOIN public.t_order_master order_master ON order_master.order_id = order_details.order_id  "
        + "  INNER JOIN lab.t_lab_sample_master sample_mst ON lbsampledtls.lab_sample_id = sample_mst.lab_sample_id  "
        + "  INNER JOIN public.m_visit_type_master visit_type_master ON visit_type_master.visit_type_id = order_master.visit_type_id  "
        + "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id  "
        + "  INNER JOIN patient.t_patient_registration pati_mst ON lbsampledtls.patient_id = pati_mst.patient_id  "
        + "  INNER JOIN m_prefix_master pre_mst ON pati_mst.prefix_id = pre_mst.prefix_id  "
        + "  INNER JOIN m_gender_master gen_mst ON pati_mst.gender_id = gen_mst.gender_id  "
        + "  INNER JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id  "
        + "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_mst.sample_id  "
        + "  LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id  "
        + "  LEFT JOIN doctor.m_doctor_master doct_mst ON order_master.ord_doctor_id = doct_mst.doctor_id  "
        + "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = order_master.ord_doc_spl_id   "
        + " left join adt.t_admission ta on ta.admission_id = order_master.admission_id and ta.status='A' "
		+ " left join opd.t_patient_sponsor_details tpsd "
		+ " on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
		+ " left join opd.t_patient_sponsor_details tpsd2 "
		+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
        +"   LEFT JOIN adt.t_admission_details admission_dtls ON order_master.admission_id = admission_dtls.admission_id "
        + "  and admission_dtls.status = 'A' " 
    	+ "  and admission_dtls.nursing_acceptance = 'Y' "
        + "  LEFT JOIN adt.m_ward_master wardmast ON admission_dtls.ward_id = wardmast.ward_id  "
        + "  LEFT JOIN adt.m_bed_master bedmst ON admission_dtls.bed_id = bedmst.bed_id  "
        + "  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id  "
      /*  + " left join " 
		+ "	(select count(sample_dtls_id) retestCount  , sample_dtls_id from lab.t_sample_audit_trail sample_audit_trail "  
		+ "	where test_status_id = 1 group by sample_dtls_id ) as retest on retest.sample_dtls_id = lbsampledtls.lab_sample_dtls_id "*/
		+ " left join billing.t_credit_note_detail tcnd on "
		+ " tcnd.order_detail_id = order_details.order_details_id "
		+ " and tcnd.status = 'A' "
		+ " and tcnd.cancel_or_discount = 1 "
        + "WHERE  " 
        + "   lbsampledtls.org_id = :orgId  " 
        + "  AND lbsampledtls.org_unit_id = :orgUnitId  "
        + "  AND lbsampledtls.dept_id = :deptId  " 
        + "  AND lbsampledtls.sub_dept_id = :subDeptId "
        + "  AND lbsampledtls.is_outsourced = 'N' "
        + "  and order_details.ord_cancelled = 'N' "
		+ "  and order_details.status = 'A' "
		+ " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null)"
		+ "  and (case when  order_master.visit_type_id not in(1) then (case when bill_mst.billing_master_id  is not null then  bill_mst.bill_cancelled = 'N' else bill_mst.billing_master_id  is null  end )  else bill_mst.billing_master_id  is not null and bill_mst.bill_cancelled = 'N' end) "  
      ),


    @NamedNativeQuery(name = "GET_BIOCHEM_RESULT_ENTRY_LIST_COUNT",
        query = " SELECT  " 
            + "  COUNT(*)  " 
            + "FROM  " 
            + "  lab.t_lab_sample_dtls lbsampledtls  "
        	+ "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id   "
        	+ "  INNER JOIN public.t_order_master order_master ON order_master.order_id = lbsampledtls.order_id  "
            + "WHERE  " 
//        	+ "  lbsampledtls.sample_status_id IN(:sampleStatusId) "
       	    + "   test_mst.is_outsourced = 'N' "
            + "  AND lbsampledtls.org_id = :orgId  "
            + "  AND lbsampledtls.org_unit_id = :orgUnitId  "
            + "  AND lbsampledtls.dept_id = :deptId  "
            + "  AND lbsampledtls.sub_dept_id = :subDeptId  "
        	+ "  AND lbsampledtls.is_outsourced = 'N' "),

    @NamedNativeQuery(name = "GET_ALL_SUBDEPTS_RESULT_ENTRY_LIST", query = "  SELECT  "
    		+ "subspe_mst.sub_speciality_name as \"subSpecialityName\","
            + "  visit_type_master.visit_type_code AS \"visitType\", "
            + " order_details.priority_id as \"priorityId\", "
            + "    bill.bill_number as \"billNumber\", " 
            + "  lbsampledtls.org_id AS \"orgId\",  "
    		+ "  lbsampledtls.org_unit_id AS \"orgUnitId\",  "
            + "  panel_master.panel_code AS \"panelCode\",  "
            + "  panel_master.panel_id AS \"panelId\",  "
            + "  lbsampledtls.gender_id AS \"genderId\",  " 
            + "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\",  "
            + "  lbsampledtls.sample_recollect_flag AS \"sampleRecollectFlag\",  "
            + "  lbsampledtls.sample_recollect_against_id AS \"sampleRecollectAgainstId\",  "
            + "  pati_mst.uhid_number AS uhid,  "
            + "  sample_mst.sample_id AS \"sampleNo\",  "
            + "  test_mst.test_desc AS \"testDesc\",  "
            + "  order_master.admission_id AS \"visitAdmId\",  "
            + "  order_master.visit_type_id AS \"visitTypeId\",  "
            + "  lbsampledtls.is_on_hold_parameter AS \"isOnHoldParameter\",  "
            + "  lbsampledtls.dept_id AS \"deptId\",  " 
            + "  lbsampledtls.test_id AS \"testId\",  "
            + "  test_mst.test_type AS \"testType\",  "
            + "  lbsampledtls.sub_dept_id AS \"subDeptId\",  "
            + "  lbsampledtls.patient_id AS \"patientId\",  "
            + "  lbsampledtls.order_details_id AS \"orderDetailsId\",  "
            + "  pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' ||  calculate_age(pati_mst.dob) "
            + "  || '/' || gen_mst.gender_code AS \"patientDetails\",  "
            + "  CAST(lbsampledtls.patient_visit_age AS DATE) - CAST(pati_mst.dob AS DATE) AS \"patientAgeDays\",  "
            + "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' AS \"doctorDetails\",  "
            + "  lbsampledtls.sample_barcode AS \"sampleBarcode\",  "
            + "  order_details.created_date AS \"orderDateTime\",  "
            + "  lbsampledtls.sample_collection_datetime AS \"sampleCollectionDatetime\" , " 
            + "  to_char(order_details.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"orderDateTimeString\",  "
            + "  to_char(lbsampledtls.sample_collection_datetime, 'DD/MM/YYYY HH24:MI:SS') AS \"sampleCollectionDatetimeString\" , " 
            + "  wardmast.ward_code AS \"wardCode\" , " 
            + "  bedmst.bed_code AS \"bedNumber\",  " 
            + "  test_mst.test_code AS \"testCode\" , "
            + "  pati_mst.image_path AS \"imagePath\", " 
            + "  samp_mst.sample_desc AS \"sampleType\" ,"
            + " doct_mst.email as \"doctorMail\", "
            + " pati_mst.email as \"patientMail\", "
            + " tltr.lab_test_res_id as \"labTestResId\", "
            +" (case when order_master.encounter_id > 0 then " +
            " ( tpsd.payment_entitlement_id ) " +
            " else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
            +" order_master.order_no as \"orderNo\", "
            +" (case when order_master.encounter_id > 0 then " +
            " ( tpsd.payee_id ) " +
            " else ( tpsd2.payee_id ) end ) as \"payeeId\",  "
            + "order_details.is_dummy_service as \"isDummyService\" "
            + " FROM  "
            + "  lab.t_lab_sample_dtls lbsampledtls  "
            + "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id  "
            + "  INNER JOIN public.t_order_master order_master ON order_master.order_id = order_details.order_id  "
            + " left join  billing.t_billing_details tbd on tbd.order_details_id =order_details.order_details_id and tbd.status ='A' "
            + " left join billing.m_billing_master bill  on bill.billing_master_id =tbd.billing_master_id and bill.bill_cancelled = 'N' "
            + "  INNER JOIN lab.t_lab_sample_master sample_mst ON lbsampledtls.lab_sample_id = sample_mst.lab_sample_id  "
			+ "  LEFT JOIN lab.t_lab_test_result tltr ON tltr.lab_test_res_id = (select lab_test_res_id from lab.t_lab_test_result ltlsc where ltlsc.lab_sample_dtls_id = lbsampledtls.lab_sample_dtls_id"
			+ " and ltlsc.status = 'A' order by ltlsc.lab_test_res_id desc limit 1) "
            + "  INNER JOIN public.m_visit_type_master visit_type_master ON visit_type_master.visit_type_id = order_master.visit_type_id  "
            + "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id  "
            + "  INNER JOIN patient.t_patient_registration pati_mst ON lbsampledtls.patient_id = pati_mst.patient_id  "
            + "  INNER JOIN m_prefix_master pre_mst ON pati_mst.prefix_id = pre_mst.prefix_id  "
            + "  INNER JOIN m_gender_master gen_mst ON pati_mst.gender_id = gen_mst.gender_id  "
            + "  INNER JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id  "
            + "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_mst.sample_id  "
            + "  LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id  "
            + "  LEFT JOIN doctor.m_doctor_master doct_mst ON order_master.ord_doctor_id = doct_mst.doctor_id  "
            + "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = order_master.ord_doc_spl_id   "
            + " left join adt.t_admission ta on ta.admission_id = order_master.admission_id and ta.status='A' "
			+ " left join opd.t_patient_sponsor_details tpsd "
			+ " on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
			+ " left join opd.t_patient_sponsor_details tpsd2 "
			+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
            +"   LEFT JOIN adt.t_admission_details admission_dtls ON order_master.admission_id = admission_dtls.admission_id "
            + "  and admission_dtls.status = 'A' " 
        	+ "  and admission_dtls.nursing_acceptance = 'Y' "
            + "  LEFT JOIN adt.m_ward_master wardmast ON admission_dtls.ward_id = wardmast.ward_id  "
            + "  LEFT JOIN adt.m_bed_master bedmst ON admission_dtls.bed_id = bedmst.bed_id  "
            + "  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id "
            + " inner join t_lab_configuration tlc on tlc.organisation_id = lbsampledtls.org_id and tlc.unit_id = lbsampledtls.assign_unit "
            + " left join billing.t_credit_note_detail tcnd on "
    		+ " tcnd.order_detail_id = order_details.order_details_id "
    		+ " and tcnd.status = 'A' "
    		+ " and tcnd.cancel_or_discount = 1 "
			+ "  INNER JOIN lab.m_report_type_master report_mst ON test_mst.report_type_id = report_mst.report_type_id  "
            + " WHERE  "
            + "   lbsampledtls.org_id = :orgId  " 
            + "  AND lbsampledtls.org_unit_id = :orgUnitId  "
           // + "  AND lbsampledtls.dept_id = :deptId  " 
            + " AND lbsampledtls.dept_id  in (:deptId) "
            + "  AND lbsampledtls.is_outsourced = 'N' "
            + "  and order_details.ord_cancelled = 'N' "
    		+ "  and order_details.status = 'A' "
    		+ " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
    		+ " and report_mst.report_type_status = 'A' "
    		+ " and COALESCE(report_mst.is_template, false) = false "
//    		 + "  AND (case when tlc.consolidated_result_entry = 'N' then lbsampledtls.sub_dept_id not in (tlc.biochemistry_id ,tlc.hematology_id ,tlc.clinical_pathology_id ,tlc.serology_id) else lbsampledtls.sub_dept_id not in (tlc.serology_id)  end) "
          ),
    
    @NamedNativeQuery(name = "GET_ALL_SUBDEPTS_RESULT_ENTRY_LIST_COUNT", query = "  SELECT  "
            + "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\" "
			+ " FROM  "
			+ "  lab.t_lab_sample_dtls lbsampledtls  "
			+ "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id  "
			+ "  INNER JOIN public.t_order_master order_master ON order_master.order_id = order_details.order_id  "
			+ " left join  billing.t_billing_details tbd on tbd.order_details_id =order_details.order_details_id and tbd.status ='A' "
			+ " left join billing.m_billing_master bill  on bill.billing_master_id =tbd.billing_master_id and bill.bill_cancelled = 'N' "
			+ "  INNER JOIN lab.t_lab_sample_master sample_mst ON lbsampledtls.lab_sample_id = sample_mst.lab_sample_id  "
			+ "  LEFT JOIN lab.t_lab_test_result tltr ON tltr.lab_test_res_id = (select lab_test_res_id from lab.t_lab_test_result ltlsc where ltlsc.lab_sample_dtls_id = lbsampledtls.lab_sample_dtls_id"
			+ " and ltlsc.status = 'A' order by ltlsc.lab_test_res_id desc limit 1) "
			+ "  INNER JOIN public.m_visit_type_master visit_type_master ON visit_type_master.visit_type_id = order_master.visit_type_id  "
			+ "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id  "
			+ "  INNER JOIN patient.t_patient_registration pati_mst ON lbsampledtls.patient_id = pati_mst.patient_id  "
			+ "  INNER JOIN m_prefix_master pre_mst ON pati_mst.prefix_id = pre_mst.prefix_id  "
			+ "  INNER JOIN m_gender_master gen_mst ON pati_mst.gender_id = gen_mst.gender_id  "
			+ "  INNER JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id  "
			+ "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_mst.sample_id  "
			+ "  LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id  "
			+ "  LEFT JOIN doctor.m_doctor_master doct_mst ON order_master.ord_doctor_id = doct_mst.doctor_id  "
			+ "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = order_master.ord_doc_spl_id   "
			+ " left join adt.t_admission ta on ta.admission_id = order_master.admission_id and ta.status='A' "
			+ " left join opd.t_patient_sponsor_details tpsd "
			+ " on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
			+ " left join opd.t_patient_sponsor_details tpsd2 "
			+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
			+"   LEFT JOIN adt.t_admission_details admission_dtls ON order_master.admission_id = admission_dtls.admission_id "
			+ "  and admission_dtls.status = 'A' "
			+ "  and admission_dtls.nursing_acceptance = 'Y' "
			+ "  LEFT JOIN adt.m_ward_master wardmast ON admission_dtls.ward_id = wardmast.ward_id  "
			+ "  LEFT JOIN adt.m_bed_master bedmst ON admission_dtls.bed_id = bedmst.bed_id  "
			+ "  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id "
			+ " inner join t_lab_configuration tlc on tlc.organisation_id = lbsampledtls.org_id and tlc.unit_id = lbsampledtls.assign_unit "
			+ " left join billing.t_credit_note_detail tcnd on "
			+ " tcnd.order_detail_id = order_details.order_details_id "
			+ " and tcnd.status = 'A' "
			+ " and tcnd.cancel_or_discount = 1 "
			+ "  INNER JOIN lab.m_report_type_master report_mst ON test_mst.report_type_id = report_mst.report_type_id  "
			+ "WHERE  "
			+ "   lbsampledtls.org_id = :orgId  "
			+ "  AND lbsampledtls.org_unit_id = :orgUnitId  "
			// + "  AND lbsampledtls.dept_id = :deptId  "
			+ " AND lbsampledtls.dept_id  in (:deptId) "
			+ "  AND lbsampledtls.is_outsourced = 'N' "
			+ "  and order_details.ord_cancelled = 'N' "
			+ "  and order_details.status = 'A' "
			+ " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
			+ " and report_mst.report_type_status = 'A' "
			+ " and COALESCE(report_mst.is_template, false) = false "
          //  + "  AND (case when tlc.consolidated_result_entry = 'N' then lbsampledtls.sub_dept_id not in (biochemistry_id ,hematology_id ,clinical_pathology_id ,serology_id) else lbsampledtls.sub_dept_id not in (serology_id)  end) "

          ),
    
    @NamedNativeQuery(name = "GET_ALL_SERO_RESULT_ENTRY_LIST", query = "  SELECT  "
    		+ "subspe_mst.sub_speciality_name as \"subSpecialityName\","
   		 + "    bill_mst.bill_number as \"billNumber\", " 
    		+ " order_details.priority_id as \"priorityId\", "
            + "  visit_type_master.visit_type_code AS \"visitType\",  "
            + "  lbsampledtls.org_id AS \"orgId\",  "
    		+ "  lbsampledtls.org_unit_id AS \"orgUnitId\",  "
            + "  panel_master.panel_code AS \"panelCode\",  "
            + "  panel_master.panel_id AS \"panelId\",  "
            + "  lbsampledtls.gender_id AS \"genderId\",  " 
            + "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\",  "
            + "  lbsampledtls.sample_recollect_flag AS \"sampleRecollectFlag\",  "
            + "  lbsampledtls.sample_recollect_against_id AS \"sampleRecollectAgainstId\",  "
            + "  pati_mst.uhid_number AS uhid,  "
            + "  sample_mst.sample_id AS \"sampleNo\",  "
            + "  test_mst.test_desc AS \"testDesc\",  "
            + "  order_master.admission_id AS \"visitAdmId\",  "
            + "  order_master.visit_type_id AS \"visitTypeId\",  "
            + "  lbsampledtls.dept_id AS \"deptId\",  " 
            + "  lbsampledtls.test_id AS \"testId\",  "
            + "  test_mst.test_type AS \"testType\",  "
            + "  lbsampledtls.sub_dept_id AS \"subDeptId\",  "
            + "  lbsampledtls.is_on_hold_parameter AS \"isOnHoldParameter\",  "
            + "  lbsampledtls.patient_id AS \"patientId\",  "
            + "  lbsampledtls.order_details_id AS \"orderDetailsId\",  "
            + " pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || "
            + " calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code AS \"patientDetails\",  "
            + "  CAST(lbsampledtls.patient_visit_age AS DATE) - CAST(pati_mst.dob AS DATE) AS \"patientAgeDays\",  "
            + "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' AS \"doctorDetails\",  "
            + "  lbsampledtls.sample_barcode AS \"sampleBarcode\",  "
            + "  order_details.created_date AS \"orderDateTime\",  "
            + "  lbsampledtls.sample_collection_datetime AS \"sampleCollectionDatetime\" , " 
            + "  to_char(order_details.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"orderDateTimeString\",  "
            + "  to_char(lbsampledtls.sample_collection_datetime, 'DD/MM/YYYY HH24:MI:SS') AS \"sampleCollectionDatetimeString\" , " 
            + "  wardmast.ward_code AS \"wardCode\" , " 
            + "  bedmst.bed_code AS \"bedNumber\",  " 
            + "  test_mst.test_code AS \"testCode\" , "
            + "  pati_mst.image_path AS \"imagePath\", " 
            + "  samp_mst.sample_desc AS \"sampleType\" ,"
            + " doct_mst.email as \"doctorMail\", "
            + " pati_mst.email as \"patientMail\", " 
            +" (case when order_master.encounter_id > 0 then " +
            " ( tpsd.payment_entitlement_id ) " +
            " else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
            +" order_master.order_no as \"orderNo\", "
            +" (case when order_master.encounter_id > 0 then " +
            " ( tpsd.payee_id ) " +
            " else ( tpsd2.payee_id ) end ) as \"payeeId\","
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
            + "  INNER JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id  "
            + "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_mst.sample_id  "
            + "  LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id  "
            + "  LEFT JOIN doctor.m_doctor_master doct_mst ON order_master.ord_doctor_id = doct_mst.doctor_id  "
            + "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = order_master.ord_doc_spl_id   "
            + " left join adt.t_admission ta on ta.admission_id = order_master.admission_id and ta.status='A' "
			+ " left join opd.t_patient_sponsor_details tpsd "
			+ " on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
			+ " left join opd.t_patient_sponsor_details tpsd2 "
			+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
            +"   LEFT JOIN adt.t_admission_details admission_dtls ON order_master.admission_id = admission_dtls.admission_id "
            + "  and admission_dtls.status = 'A' " 
        	+ "  and admission_dtls.nursing_acceptance = 'Y' "
            + "  LEFT JOIN adt.m_ward_master wardmast ON admission_dtls.ward_id = wardmast.ward_id  "
            + "  LEFT JOIN adt.m_bed_master bedmst ON admission_dtls.bed_id = bedmst.bed_id  "
            + "  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id  "
            + " inner join t_lab_configuration tlc on tlc.organisation_id = lbsampledtls.org_id and tlc.unit_id = lbsampledtls.assign_unit "
            + " left join billing.t_billing_details bill_dtls on " 
	   		 + " 	order_details.order_details_id = bill_dtls.order_details_id " 
	   		 + " left join billing.m_billing_master bill_mst on " 
	   		 + " 	bill_mst.billing_master_id = bill_dtls.billing_master_id " 
	   		 + " 	and bill_mst.bill_cancelled = 'N' " 
//            + " left join " 
//    		+ " (	select count(sample_dtls_id) retestCount  , sample_dtls_id from lab.t_sample_audit_trail sample_audit_trail "  
//    		+ "	where test_status_id = 1 group by sample_dtls_id ) as retest on retest.sample_dtls_id = lbsampledtls.lab_sample_dtls_id " 
    		+ " left join billing.t_credit_note_detail tcnd on "
    		+ " tcnd.order_detail_id = order_details.order_details_id "
    		+ " and tcnd.status = 'A' "
    		+ " and tcnd.cancel_or_discount = 1 "
            + "WHERE  " 
            + "   lbsampledtls.org_id = :orgId  " 
            + "  AND lbsampledtls.org_unit_id = :orgUnitId  "
           // + "  AND lbsampledtls.dept_id = :deptId  " 
           + " AND lbsampledtls.dept_id  in (:deptId) "
//           + " AND test_mst.dept_id IN (:deptId) "

            + "  AND lbsampledtls.is_outsourced = 'N' "
            + " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
            + "  AND (case when tlc.consolidated_result_entry = 'N' then lbsampledtls.sub_dept_id in (0) else (case " + 
            "			when tlc.serology_id is not null and tlc.serology_id <> 0  then lbsampledtls.sub_dept_id in (tlc.serology_id) " + 
            "				else lbsampledtls.sub_dept_id in (0) " + 
            "			end)  end) "
            + "  and order_details.ord_cancelled = 'N' "
    		+ "  and order_details.status = 'A' "
          ),
    
    
    @NamedNativeQuery(name = "GET_ALL_SERO_RESULT_ENTRY_LIST_COUNT", query = "  SELECT  "
            + "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\"  "
			+ " FROM  "
            + "  lab.t_lab_sample_dtls lbsampledtls  "
            + "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id  "
            + "  INNER JOIN public.t_order_master order_master ON order_master.order_id = order_details.order_id  "
            + "  INNER JOIN lab.t_lab_sample_master sample_mst ON lbsampledtls.lab_sample_id = sample_mst.lab_sample_id  "
            + "  INNER JOIN public.m_visit_type_master visit_type_master ON visit_type_master.visit_type_id = order_master.visit_type_id  "
            + "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id  "
            + "  INNER JOIN patient.t_patient_registration pati_mst ON lbsampledtls.patient_id = pati_mst.patient_id  "
            + " inner join t_lab_configuration tlc on tlc.organisation_id = lbsampledtls.org_id and tlc.unit_id = lbsampledtls.assign_unit "
            + "WHERE  " 
          //  + "  lbsampledtls.sample_status_id IN(:sampleStatusId) "
            + "   lbsampledtls.org_id = :orgId  " 
            + "  AND lbsampledtls.org_unit_id = :orgUnitId  "
          //  + "  AND lbsampledtls.dept_id = :deptId  "
            + " AND lbsampledtls.dept_id  in (:deptId) "
          
            
//            + " AND test_mst.dept_id IN (:deptId) "
            + "  AND lbsampledtls.is_outsourced = 'N' "
            + "  AND (case when tlc.consolidated_result_entry = 'N' then lbsampledtls.sub_dept_id in (0) else (case " + 
            "			when tlc.serology_id is not null and tlc.serology_id <> 0  then lbsampledtls.sub_dept_id in (tlc.serology_id) " + 
            "				else lbsampledtls.sub_dept_id in (0) " + 
            "			end)  end) "

          ),
    
    


    @NamedNativeQuery(name = "GET_BIOCHEM_VALIDATION_ENTRY", query = " SELECT  "
        + "  max(labresmst.lab_test_res_id) AS \"labTestResId\",  "
		 + "    bill_mst.bill_number as \"billNumber\", " 
        + "  lbsampledtls.org_id AS \"orgId\",  "
		+ "  lbsampledtls.org_unit_id AS \"orgUnitId\",  "
        + "  labresmst.test_id AS \"testId\",  "
        + "  labresmst.dept_id AS \"deptId\",  " 
        + "  labresmst.sub_dept_id AS \"subDeptId\",  " 
        + "  labresmst.patient_id AS \"patientId\",  "
        + "  labresmst.visit_type_id AS \"visitTypeId\",  "
        + "  labresmst.admission_id AS \"visitAdmId\",  "
        + "  labresmst.order_details_id AS \"orderDetailsId\",  "
        + "  labresmst.sample_no AS \"sampleNo\",  "
        + "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\",  "
        + "  labresmst.result_enter_datetime AS \"resultEnterDatetime\",  "
        + "  labresmst.result_enter_by AS \"resultEnterBy\",  "
        + "  labresmst.result_validated_datetime AS \"resultValidatedDatetime\",  "
        + "  labresmst.result_validated_by AS \"resultValidatedBy\",  "
        + "  labresmst.result_authorised_datetime AS \"resultAuthorisedDatetime\",  "
        + "  labresmst.result_authorised_by AS \"resultAuthorisedBy\",  "
        + "  labresmst.result_authorised_flag AS \"resultAuthorisedFlag\",  "
        + "  labresmst.result_handover_datetime AS \"resultHandoverDatetime\",  "
        + "  labresmst.result_handover_by AS \"resultHandoverBy\",  "
        + "  labresmst.suggetion_notes AS \"suggetionNotes\",  "
        + "  labresmst.foot_notes AS \"footsNotes\",  "
        + "  labresmst.report_type AS \"reportType\",  "
        + "  panel_master.panel_code AS \"panelCode\",  "
        + "  panel_master.panel_id AS \"panelId\",  "
        + "  lbsampledtls.gender_id AS \"genderId\",  " 
        + "  lbsampledtls.is_on_hold_parameter AS \"isOnHoldParameter\",  " 
        + "  lbsampledtls.sample_recollect_flag AS \"sampleRecollectFlag\",  "
        + "  lbsampledtls.sample_recollect_against_id AS \"sampleRecollectAgainstId\",  "
        + "  pati_mst.uhid_number AS uhid,  " 
        + "  sample_mst.sample_no AS \"labSampleNo\",  "
        + "  test_mst.test_desc AS \"testDesc\",  "
        + "  test_mst.test_type AS \"testType\",  "
        + "  pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || "
        + "  calculate_age(pati_mst.dob)  || '/' || gen_mst.gender_code AS \"patientDetails\",  "
        + "  CAST(lbsampledtls.patient_visit_age AS DATE) - CAST(pati_mst.dob AS DATE) AS \"patientAgeDays\",  "
        + "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' AS \"doctorDetails\",  "
        + "  lbsampledtls.sample_barcode AS \"sampleBarcode\",  "
        + "  visit_type_master.visit_type_code AS \"visitType\",  "
        + "  order_details.created_date AS \"orderDateTime\",  "
        + "  lbsampledtls.sample_collection_datetime AS \"sampleCollectionDatetime\",  "
        + "  to_char(order_details.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"orderDateTimeString\",  "
        + "  to_char(lbsampledtls.sample_collection_datetime, 'DD/MM/YYYY HH24:MI:SS') AS \"sampleCollectionDatetimeString\",  "
        + "  wardmast.ward_code AS \"wardCode\" , " 
        + "  bedmst.bed_code AS \"bedNumber\",  " 
        + "  test_mst.test_code AS \"testCode\" , " 
        + "  samp_mst.sample_desc AS \"sampleType\" , "
        + "  pati_mst.image_path AS \"imagePath\", " 
        +" (case when order_master.encounter_id > 0 then " +
        " ( tpsd.payment_entitlement_id ) " +
        " else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
        + "  order_master.order_no AS \"orderNo\","
		//+ " cast ( (coalesce (retest.retestCount ,0) ) as BIGINT) as \"retestCount\", "     
        + "order_details.is_dummy_service as \"isDummyService\" "
		+ " FROM  "
        + "  lab.t_lab_sample_dtls lbsampledtls  "
        + "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id  "
        + "  INNER JOIN public.t_order_master order_master ON order_master.order_id = order_details.order_id  "
        + " left join billing.t_billing_details bill_dtls on " 
		 + " 	order_details.order_details_id = bill_dtls.order_details_id " 
		 + " left join billing.m_billing_master bill_mst on " 
		 + " 	bill_mst.billing_master_id = bill_dtls.billing_master_id " 
        + "  INNER JOIN lab.t_lab_sample_master sample_mst ON lbsampledtls.lab_sample_id = sample_mst.lab_sample_id  "
        + "  INNER JOIN public.m_visit_type_master visit_type_master ON visit_type_master.visit_type_id = order_master.visit_type_id  "
        + "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id  "
        + "  INNER JOIN patient.t_patient_registration pati_mst ON lbsampledtls.patient_id = pati_mst.patient_id  "
        + "  INNER JOIN m_prefix_master pre_mst ON pati_mst.prefix_id = pre_mst.prefix_id  "
        + "  INNER JOIN m_gender_master gen_mst ON pati_mst.gender_id = gen_mst.gender_id  "
        + "  INNER JOIN lab.m_test_master test_master ON test_master.test_id = lbsampledtls.test_id  "
        + "  INNER JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id  "
        + "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_master.sample_id  "
        + "  INNER JOIN lab.t_lab_test_result labresmst ON labresmst.lab_sample_dtls_id = lbsampledtls.lab_sample_dtls_id  "
        + "  LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id  "
        + "  LEFT JOIN doctor.m_doctor_master doct_mst ON order_master.ord_doctor_id = doct_mst.doctor_id  "
        + "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = order_master.ord_doc_spl_id  "
        + " left join adt.t_admission ta on ta.admission_id = order_master.admission_id and ta.status='A' "
		+ " left join opd.t_patient_sponsor_details tpsd "
		+ " on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
		+ " left join opd.t_patient_sponsor_details tpsd2 "
		+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
        +"   LEFT JOIN adt.t_admission_details admission_dtls ON order_master.admission_id = admission_dtls.admission_id "
        + "  and admission_dtls.status = 'A' " 
    	+ "  and admission_dtls.nursing_acceptance = 'Y' "
        + "  LEFT JOIN adt.m_ward_master wardmast ON admission_dtls.ward_id = wardmast.ward_id  "
        + "  LEFT JOIN adt.m_bed_master bedmst ON admission_dtls.bed_id = bedmst.bed_id  "
        + "  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id  "
      /*  + " left join " 
		+ "	( select count(sample_dtls_id) retestCount  , sample_dtls_id from lab.t_sample_audit_trail sample_audit_trail "  
		+ "	where test_status_id = 1 group by sample_dtls_id ) as retest on retest.sample_dtls_id = lbsampledtls.lab_sample_dtls_id " */
        + "  LEFT join ( select MAX(labres.lab_test_res_id) lab_test_res , lab_sample_dtls_id " 
        + "  from lab.t_lab_test_result labres group by lab_sample_dtls_id ) temp ON lbsampledtls.lab_sample_dtls_id = temp.lab_sample_dtls_id "
        + " left join billing.t_credit_note_detail tcnd on "
		+ " tcnd.order_detail_id = order_details.order_details_id "
		+ " and tcnd.status = 'A' "
		+ " and tcnd.cancel_or_discount = 1 "
        + "WHERE  " 
        + "  lbsampledtls.org_id = :orgId  " 
        + "  AND lbsampledtls.org_unit_id = :orgUnitId  "
        + "  AND lbsampledtls.dept_id = :deptId  " 
        + "  AND lbsampledtls.sub_dept_id = :subDeptId  "
        + "  AND lbsampledtls.is_outsourced = 'N' "
        + "  and order_details.ord_cancelled = 'N' "
		+ "  and order_details.status = 'A' "
		+ " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
        + " and labresmst.lab_test_res_id = temp.lab_test_res "
        + " and (case when  order_master.visit_type_id not in(1) then (case when bill_mst.billing_master_id  is not null then  bill_mst.bill_cancelled = 'N' else bill_mst.billing_master_id  is null  end )  else bill_mst.billing_master_id  is not null and bill_mst.bill_cancelled = 'N' end) " ),


    @NamedNativeQuery(name = "GET_BIOCHEM_VALIDATION_ENTRY_LIST_COUNT",
        query = " SELECT " + "	COUNT(*) " 
            + "FROM " 
            + "	lab.t_lab_sample_dtls lbsampledtls "
            + "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id   "
            + "  INNER JOIN public.t_order_master order_master ON lbsampledtls.order_id = order_master.order_id  "
            + " WHERE " 
            //+ "	lbsampledtls.sample_status_id = :sampleStatusId "
            + "	 lbsampledtls.org_id = :orgId " 
            + "	AND lbsampledtls.org_unit_id = :orgUnitId "
            + "	AND lbsampledtls.dept_id = :deptId "
            + " AND lbsampledtls.sub_dept_id =:subDeptId "
       	    + " AND lbsampledtls.is_outsourced = 'N' "),
    
    
    
    
    @NamedNativeQuery(name = "CONSOLIDATED_VALIDATION_ENTRY", query = " SELECT  "
    		+ "subspe_mst.sub_speciality_name as \"subSpecialityName\","
    		+ " order_details.priority_id AS \"priorityId\" ,"
			 + "    bill.bill_number as \"billNumber\", " 
            + "  max(labresmst.lab_test_res_id) AS \"labTestResId\",  "
            + "  lbsampledtls.org_id AS \"orgId\",  "
    		+ "  lbsampledtls.org_unit_id AS \"orgUnitId\",  "
            + "  labresmst.test_id AS \"testId\",  "
            + "  labresmst.dept_id AS \"deptId\",  " 
            + "  labresmst.sub_dept_id AS \"subDeptId\",  " 
            + "  labresmst.patient_id AS \"patientId\",  "
            + "  labresmst.visit_type_id AS \"visitTypeId\",  "
            + "  labresmst.admission_id AS \"visitAdmId\",  "
            + "  labresmst.order_details_id AS \"orderDetailsId\",  "
            + "  labresmst.sample_no AS \"sampleNo\",  "
            + "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\",  "
            + "  labresmst.result_enter_datetime AS \"resultEnterDatetime\",  "
            + "  labresmst.result_enter_by AS \"resultEnterBy\",  "
            + "  labresmst.result_validated_datetime AS \"resultValidatedDatetime\",  "
            + "  labresmst.result_validated_by AS \"resultValidatedBy\",  "
            + "  labresmst.result_authorised_datetime AS \"resultAuthorisedDatetime\",  "
            + "  labresmst.result_authorised_by AS \"resultAuthorisedBy\",  "
            + "  labresmst.result_authorised_flag AS \"resultAuthorisedFlag\",  "
            + "  labresmst.result_handover_datetime AS \"resultHandoverDatetime\",  "
            + "  labresmst.result_handover_by AS \"resultHandoverBy\",  "
            + "  labresmst.suggetion_notes AS \"suggetionNotes\",  "
            + "  labresmst.foot_notes AS \"footsNotes\",  "
            + "  labresmst.report_type AS \"reportType\",  "
            + "  panel_master.panel_code AS \"panelCode\",  "
            + "  panel_master.panel_id AS \"panelId\",  "
            + "  lbsampledtls.gender_id AS \"genderId\",  " 
            + "  lbsampledtls.sample_recollect_flag AS \"sampleRecollectFlag\",  "
            + "  lbsampledtls.sample_recollect_against_id AS \"sampleRecollectAgainstId\",  "
            + "  pati_mst.uhid_number AS uhid,  " 
            + "  sample_mst.sample_no AS \"labSampleNo\",  "
            + "  test_mst.test_desc AS \"testDesc\",  "
            + "  test_mst.test_type AS \"testType\",  "
            + " pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || "
            + " calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code AS \"patientDetails\",  "
            + "  CAST(lbsampledtls.patient_visit_age AS DATE) - CAST(pati_mst.dob AS DATE) AS \"patientAgeDays\",  "
            + "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' AS \"doctorDetails\",  "
            + "  lbsampledtls.sample_barcode AS \"sampleBarcode\",  "
            + "  visit_type_master.visit_type_code AS \"visitType\",  "
            + "  order_details.created_date AS \"orderDateTime\",  "
            + "  lbsampledtls.sample_collection_datetime AS \"sampleCollectionDatetime\",  "
            + "  to_char(order_details.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"orderDateTimeString\",  "
            + "  to_char(lbsampledtls.sample_collection_datetime, 'DD/MM/YYYY HH24:MI:SS') AS \"sampleCollectionDatetimeString\",  "
            + "  wardmast.ward_code AS \"wardCode\" , " 
            + "  bedmst.bed_code AS \"bedNumber\",  " 
            + "  test_mst.test_code AS \"testCode\" , " 
            + "  samp_mst.sample_desc AS \"sampleType\" , "
            + "  pati_mst.image_path AS \"imagePath\", "
            +" (case when order_master.encounter_id > 0 then " +
            " ( tpsd.payment_entitlement_id ) " +
            " else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
            + "  order_master.order_no AS \"orderNo\","
			//+ " cast ( (coalesce (retest.retestCount ,0) ) as BIGINT) as \"retestCount\", "
            + "order_details.is_dummy_service as \"isDummyService\", "
            + " intervention.clinical_intervention_procedure_id as \"clinicalInterventionProcedureId\", "
            + " intervention.intervention_processed_doctor_id as \"interventionCompletedDoctorId\", "
            + " intervention.is_clinical_intervention_required as \"isClinicalInterventionRequired\", "
			+ "  to_char(intervention.intervention_completed_date_time, 'DD/MM/YYYY HH24:MI:SS') AS \"interventionEndDate\",  "
			+ "  intervention.intervention_remark as \"interventionRemarks\"  "
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
            + "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_master.sample_id  "
            + "  INNER JOIN lab.t_lab_test_result labresmst ON labresmst.lab_sample_dtls_id = lbsampledtls.lab_sample_dtls_id  "
            + "  LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id  "
            + "  LEFT JOIN doctor.m_doctor_master doct_mst ON order_master.ord_doctor_id = doct_mst.doctor_id  "
            + "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = order_master.ord_doc_spl_id  "
            + " left join adt.t_admission ta on ta.admission_id = order_master.admission_id and ta.status='A' "
			+ " left join opd.t_patient_sponsor_details tpsd "
			+ " on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
			+ " left join opd.t_patient_sponsor_details tpsd2 "
			+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
            +"   LEFT JOIN adt.t_admission_details admission_dtls ON order_master.admission_id = admission_dtls.admission_id "
            + "  and admission_dtls.status = 'A' " 
        	+ "  and admission_dtls.nursing_acceptance = 'Y' "
            + "  LEFT JOIN adt.m_ward_master wardmast ON admission_dtls.ward_id = wardmast.ward_id  "
            + "  LEFT JOIN adt.m_bed_master bedmst ON admission_dtls.bed_id = bedmst.bed_id  "
            + "  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id  "
            + " left join  billing.t_billing_details tbd on tbd.order_details_id =order_details.order_details_id and tbd.status ='A' "
            + " left join billing.m_billing_master bill  on bill.billing_master_id =tbd.billing_master_id "
          /*  + " left join " 
    		+ " (	select count(sample_dtls_id) retestCount  , sample_dtls_id from lab.t_sample_audit_trail sample_audit_trail "  
    		+ "	where test_status_id = 1 group by sample_dtls_id ) as retest on retest.sample_dtls_id = lbsampledtls.lab_sample_dtls_id " */
            + "  LEFT join ( select MAX(labres.lab_test_res_id) lab_test_res , lab_sample_dtls_id " 
            + "  from lab.t_lab_test_result labres group by lab_sample_dtls_id ) temp ON lbsampledtls.lab_sample_dtls_id = temp.lab_sample_dtls_id "
            + " left join billing.t_credit_note_detail tcnd on "
    		+ " tcnd.order_detail_id = order_details.order_details_id "
    		+ " and tcnd.status = 'A' "
    		+ " and tcnd.cancel_or_discount = 1 "
			+ "  INNER JOIN lab.m_report_type_master report_mst ON test_mst.report_type_id = report_mst.report_type_id  "
			+ " LEFT JOIN service.t_unit_service_master_details service_dtls on "
			+ " 	order_details.service_id = service_dtls.service_master_id "
			+ " LEFT JOIN public.t_clinical_intervention_procedure intervention on "
			+ " 	lbsampledtls.lab_sample_dtls_id = intervention.lab_sample_dtls_id "
			+ " 	and intervention.status = 'A' "
            + "WHERE  "
            + "   lbsampledtls.org_id = :orgId  " 
            + "  AND lbsampledtls.org_unit_id = :orgUnitId  "
            + " AND lbsampledtls.dept_id  in (:deptId) "
            + "  AND lbsampledtls.is_outsourced = 'N' "
            + "  and order_details.ord_cancelled = 'N' "
    		+ "  and order_details.status = 'A' "
    		+ " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
    		+ " and (case when  order_master.visit_type_id not in(1) then (case when bill.billing_master_id  is not null then  bill.bill_cancelled = 'N' else bill.billing_master_id  is null  end )  else bill.billing_master_id  is not null and bill.bill_cancelled = 'N' end) " 
            + " and labresmst.lab_test_res_id = temp.lab_test_res "
			+ " and report_mst.report_type_status = 'A' "
			+ " and COALESCE(report_mst.is_template, false) = false "
               ),
    
    
    @NamedNativeQuery(name = "GET_BIOCHEM_REPORT_RELEASE", query = 
    		"SELECT "
    		+ "  max(labresmst.lab_test_res_id) AS \"labTestResId\", "
    		+ "  lbsampledtls.assign_unit AS \"assingnedFrom\","
			 + "    bill_mst.bill_number as \"billNumber\", " 
    		+ "  lbsampledtls.org_id AS \"orgId\", "
    		+ "  lbsampledtls.org_unit_id AS \"orgUnitId\", "
    		+ "  labresmst.test_id AS \"testId\", "
    		+ "  labresmst.dept_id AS \"deptId\", "
    		+ "  labresmst.sub_dept_id AS \"subDeptId\", "
    		+ "  labresmst.patient_id AS \"patientId\", "
    		+ "  labresmst.visit_type_id AS \"visitTypeId\", "
    		+ "  labresmst.admission_id AS \"visitAdmId\", "
    		+ "  labresmst.order_details_id AS \"orderDetailsId\", "
    		+ "  labresmst.sample_no AS \"sampleNo\", "
    		+ "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\", "
    		+ "  lbsampledtls.is_on_hold_parameter AS \"isOnHoldParameter\", "
    		+ "  labresmst.result_enter_datetime AS \"resultEnterDatetime\", "
    		+ "  labresmst.result_enter_by AS \"resultEnterBy\", "
    		+ "  labresmst.result_validated_datetime AS \"resultValidatedDatetime\", "
    		+ "  labresmst.result_validated_by AS \"resultValidatedBy\", "
    		+ "  labresmst.result_authorised_datetime AS \"resultAuthorisedDatetime\", "
    		+ "  labresmst.result_authorised_by AS \"resultAuthorisedBy\", "
    		+ "  labresmst.result_authorised_flag AS \"resultAuthorisedFlag\", "
    		+ "  labresmst.result_handover_datetime AS \"resultHandoverDatetime\", "
    		+ "  labresmst.result_handover_by AS \"resultHandoverBy\", "
    		+ "  labresmst.suggetion_notes AS \"suggetionNotes\", "
    		+ "  labresmst.foot_notes AS \"footsNotes\", "
    		+ "  labresmst.report_type AS \"reportType\", "
    		+ "  panel_master.panel_code AS \"panelCode\", "
    		+ "  panel_master.panel_id AS \"panelId\", "
    		+ "  lbsampledtls.gender_id AS \"genderId\", "
    		+ "  lbsampledtls.sample_recollect_flag AS \"sampleRecollectFlag\", "
    		+ "  lbsampledtls.sample_recollect_against_id AS \"sampleRecollectAgainstId\", "
    		+ "  pati_mst.uhid_number AS uhid, "
    		+ " order_details.priority_id as \"priorityId\", "
    		+ "  sample_mst.sample_no AS \"labSampleNo\", "
    		+ "  test_mst.test_desc AS \"testDesc\", "
    		+ "  test_mst.test_type AS \"testType\", "
    		+ "  test_mst.pro_release AS \"proRelease\", "
    		+ "  subspe_mst.sub_speciality_name AS \"deptName\", "
    		+ "  usmd.is_review_applicable as \"isReviewApplicable\", "
            + " ( pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || "
            + " calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code ) AS \"patientDetails\"  , "
    		+ "  CAST(lbsampledtls.patient_visit_age AS DATE) - CAST(pati_mst.dob AS DATE) AS \"patientAgeDays\", "
    		+ "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' AS \"doctorDetails\", "
    		+ "  lbsampledtls.sample_barcode AS \"sampleBarcode\", "
    		+ "  visit_type_master.visit_type_code AS \"visitType\", "
    		+ "  order_details.created_date AS \"orderDateTime\", "
    		+ "  lbsampledtls.sample_collection_datetime AS \"sampleCollectionDatetime\", "
    		+ "  to_char(order_details.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"orderDateTimeString\",  "
            + "  to_char(lbsampledtls.sample_collection_datetime, 'DD/MM/YYYY HH24:MI:SS') AS \"sampleCollectionDatetimeString\",  "
    		+ "  wardmast.ward_code AS \"wardCode\" , "
    		+ "  bedmst.bed_code AS \"bedNumber\", "
    		+ "  test_mst.test_code AS \"testCode\" , "
    		+ "  samp_mst.sample_desc AS \"sampleType\" , "
    		+ "   gen_mst.gender_name as \"patientGender\", "
    		+ "   pati_mst.email as \"patientMail\", "
    		+ "   pati_mst.mobile_number as \"mobileNo\", "
    		+ "   pati_mst.image_path AS \"imagePath\", "
    		+ "   doct_mst.email as \"doctorMail\", "
    		+ "   order_master.order_id as \"orderId\","
    		+ "  EXTRACT( "
    		+ "    YEAR "
    		+ "    FROM "
    		+ "      age(lbsampledtls.patient_visit_age, pati_mst.dob) "
    		+ "  ) AS \"patientAgeInYears\", "
    		+" (case when order_master.encounter_id > 0 then " +
    		" ( tpsd.payment_entitlement_id ) " +
    		" else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
    		+ " order_master.order_no AS \"orderNo\","
    		+" (case when order_master.encounter_id > 0 then " +
    		" ( tpsd.payee_id ) " +
    		" else ( tpsd2.payee_id ) end ) as \"payeeId\", "
			//+ " cast ( (coalesce (retest.retestCount ,0) ) as BIGINT) as \"retestCount\" ,"
			 + "order_details.is_dummy_service as \"isDummyService\" "
			+ " FROM "
    		+ "  lab.t_lab_sample_dtls lbsampledtls "
    		+ "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id "
    		+ "  INNER JOIN public.t_order_master order_master ON order_master.order_id = order_details.order_id "
    		 + " left join billing.t_billing_details bill_dtls on " 
    		 + " 	order_details.order_details_id = bill_dtls.order_details_id " 
    		 + " left join billing.m_billing_master bill_mst on " 
    		 + " 	bill_mst.billing_master_id = bill_dtls.billing_master_id " 
    		+ "  INNER JOIN lab.t_lab_sample_master sample_mst ON lbsampledtls.lab_sample_id = sample_mst.lab_sample_id "
    		+ "  INNER JOIN public.m_visit_type_master visit_type_master ON visit_type_master.visit_type_id = order_master.visit_type_id "
    		+ "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id "
    		+ "  INNER JOIN patient.t_patient_registration pati_mst ON lbsampledtls.patient_id = pati_mst.patient_id "
    		+ "  INNER JOIN m_prefix_master pre_mst ON pati_mst.prefix_id = pre_mst.prefix_id "
    		+ "  INNER JOIN m_gender_master gen_mst ON pati_mst.gender_id = gen_mst.gender_id "
    		+ "  INNER JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id "
    		+ "  INNER JOIN lab.m_test_master test_master ON test_master.test_id = lbsampledtls.test_id "
    		+ "  INNER JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id "
    		+ "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_master.sample_id "
    		+ "  INNER JOIN lab.t_lab_test_result labresmst ON labresmst.lab_sample_dtls_id = lbsampledtls.lab_sample_dtls_id "
    		+ "  INNER JOIN service.t_unit_service_master_details usmd ON order_details.service_id = usmd.service_master_id and order_details.org_unit_id = usmd.unit_id and usmd.status = 'A' "
    		+ "  LEFT JOIN doctor.m_doctor_master doct_mst ON order_master.ord_doctor_id = doct_mst.doctor_id "
    		+ "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = order_master.ord_doc_spl_id "
    		+ " left join adt.t_admission ta on ta.admission_id = order_master.admission_id and ta.status='A' "
			+ " left join opd.t_patient_sponsor_details tpsd "
			+ " on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
			+ " left join opd.t_patient_sponsor_details tpsd2 "
			+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
    		+ "   LEFT JOIN adt.t_admission_details admission_dtls ON order_master.admission_id = admission_dtls.admission_id "
    		+ "  and admission_dtls.status = 'A' "
    		+ "  and admission_dtls.nursing_acceptance = 'Y' "
    		+ "  LEFT JOIN adt.m_ward_master wardmast ON admission_dtls.ward_id = wardmast.ward_id "
    		+ "  LEFT JOIN adt.m_bed_master bedmst ON admission_dtls.bed_id = bedmst.bed_id "
    		+ "  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id "
    		/*+ " left join " 
    		+ "	( select count(sample_dtls_id) retestCount  , sample_dtls_id from lab.t_sample_audit_trail sample_audit_trail "  
    		+ "	where test_status_id = 1 group by sample_dtls_id ) as retest on retest.sample_dtls_id = lbsampledtls.lab_sample_dtls_id " */
    		+ "  LEFT join ( select MAX(labres.lab_test_res_id) lab_test_res , lab_sample_dtls_id " 
    	    + "  from lab.t_lab_test_result labres group by lab_sample_dtls_id ) temp ON lbsampledtls.lab_sample_dtls_id = temp.lab_sample_dtls_id "
    	    + " left join billing.t_credit_note_detail tcnd on "
    		+ " tcnd.order_detail_id = order_details.order_details_id "
    		+ " and tcnd.status = 'A' "
    		+ " and tcnd.cancel_or_discount = 1 "
    		+ "WHERE "
    		+ " lbsampledtls.org_id = :orgId "
    		+ "  AND lbsampledtls.org_unit_id = :orgUnitId "
    		+ "  AND lbsampledtls.dept_id = :deptId "
    		+ "  AND lbsampledtls.sub_dept_id = :subDeptId "
    		+ "  AND lbsampledtls.is_outsourced = 'N' "
    		+ "  and order_details.ord_cancelled = 'N' "
    		+ "  and order_details.status = 'A' "
    		+ " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
    		+ " and labresmst.lab_test_res_id = temp.lab_test_res "
    		+ " and (case when  order_master.visit_type_id not in(1) then (case when bill_mst.billing_master_id  is not null then  bill_mst.bill_cancelled = 'N' else bill_mst.billing_master_id  is null  end )  else bill_mst.billing_master_id  is not null and bill_mst.bill_cancelled = 'N' end) "
    		
     ),


    @NamedNativeQuery(name = "GET_BIOCHEM_REPORT_RELEASE_COUNT",
        query = " SELECT " 
            + "  COUNT(*) " 
            + "FROM " 
            + " lab.t_lab_sample_dtls lbsampledtls "
            + "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id   "
            + "  INNER JOIN public.t_order_master order_master ON lbsampledtls.order_id = order_master.order_id  "
            + "  INNER JOIN public.t_order_details order_details "
            + " ON  lbsampledtls.order_details_id = order_details.order_details_id "
            + "WHERE " 
//            + "  lbsampledtls.sample_status_id IN(:sampleStatusIds) "
          //  +"  AND order_details.created_date >= :fromDate"
         //   +"  AND order_details.created_date <= :toDate"
            +"   lbsampledtls.org_id = :orgId " 
            +"  AND lbsampledtls.org_unit_id = :orgUnitId "
            +"  AND lbsampledtls.dept_id = :deptId "
            +"  AND lbsampledtls.sub_dept_id =:subDeptId "
            + "  and order_details.ord_cancelled = 'N' "
    		+ "  and order_details.status = 'A' "
            +"  AND lbsampledtls.is_outsourced = 'N' "),
    
    
    @NamedNativeQuery(name = "CONSOLIDATED_RELEASE", query = 
	"SELECT "
	+ "  max(labresmst.lab_test_res_id) AS \"labTestResId\", "
	+ "  lbsampledtls.assign_unit AS \"assingnedFrom\","
	 + "    bill.bill_number as \"billNumber\", " 
	+ "  order_details.priority_id AS \"priorityId\", "
	+ "  lbsampledtls.org_id AS \"orgId\", "
	+ "  lbsampledtls.org_unit_id AS \"orgUnitId\", "
	+ "  labresmst.test_id AS \"testId\", "
	+ "  labresmst.dept_id AS \"deptId\", "
	+ "  labresmst.sub_dept_id AS \"subDeptId\", "
	+ "  labresmst.patient_id AS \"patientId\", "
	+ "  labresmst.visit_type_id AS \"visitTypeId\", "
	+ "  labresmst.admission_id AS \"visitAdmId\", "
	+ "  labresmst.order_details_id AS \"orderDetailsId\", "
	+ "  labresmst.sample_no AS \"sampleNo\", "
	+ "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\", "
	+ "  labresmst.result_enter_datetime AS \"resultEnterDatetime\", "
	+ "  labresmst.result_enter_by AS \"resultEnterBy\", "
	+ "  labresmst.result_validated_datetime AS \"resultValidatedDatetime\", "
	+ "  labresmst.result_validated_by AS \"resultValidatedBy\", "
	+ "  labresmst.result_authorised_datetime AS \"resultAuthorisedDatetime\", "
	+ "  labresmst.result_authorised_by AS \"resultAuthorisedBy\", "
	+ "  labresmst.result_authorised_flag AS \"resultAuthorisedFlag\", "
	+ "  labresmst.result_handover_datetime AS \"resultHandoverDatetime\", "
	+ "  labresmst.result_handover_by AS \"resultHandoverBy\", "
	+ "  labresmst.suggetion_notes AS \"suggetionNotes\", "
	+ "  labresmst.foot_notes AS \"footsNotes\", "
	+ "  labresmst.report_type AS \"reportType\", "
	+ "  panel_master.panel_code AS \"panelCode\", "
	+ "  panel_master.panel_id AS \"panelId\", "
	+ "  lbsampledtls.gender_id AS \"genderId\", "
	+ "  lbsampledtls.sample_recollect_flag AS \"sampleRecollectFlag\", "
	+ "  lbsampledtls.sample_recollect_against_id AS \"sampleRecollectAgainstId\", "
	+ "  pati_mst.uhid_number AS uhid, "
	+ "  sample_mst.sample_no AS \"labSampleNo\", "
	+ "  test_mst.test_desc AS \"testDesc\", "
	+ "  test_mst.test_type AS \"testType\", "
	+ "  test_mst.pro_release AS \"proRelease\", "
	+ "  subspe_mst.sub_speciality_name AS \"deptName\", "
	+ "  usmd.is_review_applicable as \"isReviewApplicable\", "
    + "  pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || "
    + " calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code || ')' AS \"patientDetails\" "
	+ ", "
	+ "  CAST(lbsampledtls.patient_visit_age AS DATE) - CAST(pati_mst.dob AS DATE) AS \"patientAgeDays\", "
	+ "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' AS \"doctorDetails\", "
	+ "  lbsampledtls.sample_barcode AS \"sampleBarcode\", "
	+ "  visit_type_master.visit_type_code AS \"visitType\", "
	+ "  order_details.created_date AS \"orderDateTime\", "
	+ "  lbsampledtls.sample_collection_datetime AS \"sampleCollectionDatetime\", "
	+ "  to_char(order_details.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"orderDateTimeString\", "
	+ "  to_char(lbsampledtls.sample_collection_datetime, 'DD/MM/YYYY HH24:MI:SS') AS \"sampleCollectionDatetimeString\", "
	+ "  wardmast.ward_code AS \"wardCode\" , "
	+ "  bedmst.bed_code AS \"bedNumber\", "
	+ "  test_mst.test_code AS \"testCode\" , "
	+ "  samp_mst.sample_desc AS \"sampleType\" , "
	+ "   gen_mst.gender_name as \"patientGender\", "
	+ "   pati_mst.email as \"patientMail\", "
	+ "   pati_mst.mobile_number as \"mobileNo\", "
	+ "   pati_mst.image_path AS \"imagePath\", "
	+ "   doct_mst.email as \"doctorMail\", "
    + " subspe_mst.sub_speciality_name as \"subSpecialityName\","
/*	+ "  EXTRACT( "
	+ "    YEAR "
	+ "    FROM "
	+ "      age(lbsampledtls.patient_visit_age, pati_mst.dob) "
	+ "  ) AS \"patientAgeInYears\", "*/
	+" (case when order_master.encounter_id > 0 then " +
	" ( tpsd.payment_entitlement_id ) " +
	" else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
	+ " order_master.order_no as \"orderNo\","
	+ " order_master.order_id as \"orderId\","
	+" (case when order_master.encounter_id > 0 then " +
	" ( tpsd.payee_id ) " +
	" else ( tpsd2.payee_id ) end ) as \"payeeId\", "
	//+ " cast ( (coalesce (retest.retestCount ,0) ) as BIGINT) as \"retestCount\","
    + " order_details.is_dummy_service as \"isDummyService\" "
	+ " FROM "
	+ "  lab.t_lab_sample_dtls lbsampledtls "
	+ "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id "
	+ "  INNER JOIN public.t_order_master order_master ON order_master.order_id = order_details.order_id "
	 + " left join  billing.t_billing_details tbd on tbd.order_details_id =order_details.order_details_id and tbd.status ='A' "
     + " left join billing.m_billing_master bill  on bill.billing_master_id =tbd.billing_master_id "
	+ "  INNER JOIN lab.t_lab_sample_master sample_mst ON lbsampledtls.lab_sample_id = sample_mst.lab_sample_id "
	+ "  INNER JOIN public.m_visit_type_master visit_type_master ON visit_type_master.visit_type_id = order_master.visit_type_id "
	+ "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id "
	+ "  INNER JOIN patient.t_patient_registration pati_mst ON lbsampledtls.patient_id = pati_mst.patient_id "
	+ "  INNER JOIN m_prefix_master pre_mst ON pati_mst.prefix_id = pre_mst.prefix_id "
	+ "  INNER JOIN m_gender_master gen_mst ON pati_mst.gender_id = gen_mst.gender_id "
	+ "  INNER JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id "
	+ "  INNER JOIN lab.m_test_master test_master ON test_master.test_id = lbsampledtls.test_id "
	+ "  INNER JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id "
	+ "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_master.sample_id "
	+ "  INNER JOIN lab.t_lab_test_result labresmst ON labresmst.lab_sample_dtls_id = lbsampledtls.lab_sample_dtls_id "
	+ "  INNER JOIN service.t_unit_service_master_details usmd ON order_details.service_id = usmd.service_master_id and order_details.org_unit_id = usmd.unit_id and usmd.status = 'A' "
	+ "  LEFT JOIN doctor.m_doctor_master doct_mst ON order_master.ord_doctor_id = doct_mst.doctor_id "
	+ "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = order_master.ord_doc_spl_id "
	+ "  left join adt.t_admission ta on ta.admission_id = order_master.admission_id and ta.status='A' "
	+ "  left join opd.t_patient_sponsor_details tpsd "
	+ "  on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
	+ "  left join opd.t_patient_sponsor_details tpsd2 "
	+ "  on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
	+ "   LEFT JOIN adt.t_admission_details admission_dtls ON order_master.admission_id = admission_dtls.admission_id "
	+ "  and admission_dtls.status = 'A' "
	+ "  and admission_dtls.nursing_acceptance = 'Y' "
	+ "  LEFT JOIN adt.m_ward_master wardmast ON admission_dtls.ward_id = wardmast.ward_id "
	+ "  LEFT JOIN adt.m_bed_master bedmst ON admission_dtls.bed_id = bedmst.bed_id "
	+ "  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id "
	/*+ " left join " 
	+ "	( select count(sample_dtls_id) retestCount  , sample_dtls_id from lab.t_sample_audit_trail sample_audit_trail "  
	+ "	where test_status_id = 1 group by sample_dtls_id ) as retest on retest.sample_dtls_id = lbsampledtls.lab_sample_dtls_id " */
	+ "  LEFT join ( select MAX(labres.lab_test_res_id) lab_test_res , lab_sample_dtls_id " 
    + "  from lab.t_lab_test_result labres group by lab_sample_dtls_id ) temp ON lbsampledtls.lab_sample_dtls_id = temp.lab_sample_dtls_id "
    + " left join billing.t_credit_note_detail tcnd on "
	+ " tcnd.order_detail_id = order_details.order_details_id "
	+ " and tcnd.status = 'A' "
	+ " and tcnd.cancel_or_discount = 1 "
	+ "  INNER JOIN lab.m_report_type_master report_mst ON test_mst.report_type_id = report_mst.report_type_id  "
	+ " WHERE "
	+ "    lbsampledtls.org_id = :orgId "
	+ "  AND lbsampledtls.org_unit_id = :orgUnitId "
	//+ "  AND lbsampledtls.dept_id = :deptId "
	+ "  AND lbsampledtls.dept_id  in (:deptId) "
	+ "  AND lbsampledtls.is_outsourced = 'N' "
	+ "  and order_details.ord_cancelled = 'N' "
	+ "  and order_details.status = 'A' "
	+ " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
	+ " and labresmst.lab_test_res_id = temp.lab_test_res "
	+ " and report_mst.report_type_status = 'A' "
	+ " and COALESCE(report_mst.is_template, false) = false "
),
    
    

    /** REPORT HAND_OVER_ENTRY_LIST */
    @NamedNativeQuery(name = "GET_BIOCHEM_REPORT_HAND_OVER_ENTRY_LIST",
        query = " SELECT "
            +" labresmst.lab_test_res_id as \"labTestResId\","
            + " test_mst.test_id AS \"testId\", "
			 + "    bill_mst.bill_number as \"billNumber\", " 
            +"  labresmst.dept_id AS \"deptId\", "
            +"  test_mst.dept_id AS \"subDeptId\", "
            +"  lbsampledtls.patient_id AS \"patientId\", "
            +"  labresmst.visit_type_id AS \"visitTypeId\", "
            +"  labresmst.created_by AS \"createdBy\", "
            +"  labresmst.admission_id AS \"visitAdmId\", "
        	+ "	ord_mst.order_date as \"orderDateTime\", "
        	+ "  to_char(ord_mst.order_date, 'DD/MM/YYYY HH24:MI:SS') AS \"orderDateTimeString\",   "
            +"  labresmst.order_details_id AS \"orderDetailsId\", "
            +"  subspe_mst.sub_speciality_name AS \"deptName\", "
            +"  labresmst.result_enter_datetime AS \"resultEnterDatetime\", "
            +"  labresmst.result_enter_by AS \"resultEnterBy\", "
            +"  vistmst.visit_type_code AS \"visitType\", "
            +"  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\", "
            +"  lbsampledtls.internal_notes AS \"internalNotes\", "
            + " lbsampledtls.srf_id as \"srfId\", "
		    + " lbsampledtls.icmr_id as \"icmrId\", "
            +"  pati_mst.uhid_number AS uhid, "
            +"  sample_mst.sample_no AS \"labSampleNo\",  "
            +"  test_mst.test_desc AS \"testDesc\", "
            +"  test_mst.test_type AS \"testType\", "
            +"  (pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || calculate_age(pati_mst.dob) ) AS \"patientDetails\", "
            /*+"  (EXTRACT( "
            +"    YEAR "
            +"    FROM "
            +"     age(lbsampledtls.patient_visit_age, pati_mst.dob) "
            +"  )::float8) AS \"patientAgeInYears\", "*/
            +"  gen_mst.gender_code \"patientGender\", "
            +"  CURRENT_DATE - CAST(pati_mst.dob AS DATE) AS \"patientAgeDays\", "
            +"  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name AS \"doctorDetails\", "
            +"  lbsampledtls.sample_barcode AS \"sampleBarcode\", "
            +"  lbsampledtls.assign_unit AS \"assingnedFrom\" ,"
            + " lbsampledtls.org_id AS \"orgId\", "
            + " lbsampledtls.org_unit_id AS \"orgUnitId\", "
            + " doct_mst.doctor_id AS \"doctorId\", "
            + " doct_mst.email as \"doctorMail\", "
            + " lbsampledtls.gender_id AS \"genderId\", "
            + " pati_mst.email as \"patientMail\", "
            + " pati_mst.mobile_number as \"mobileNo\", "
            + " pati_mst.image_path AS \"imagePath\", "
            + " test_mst.pro_release AS \"proRelease\", "
            + " labresmst.report_type AS \"reportType\", "
            + " labresmst.result_authorised_flag AS \"resultAuthorisedFlag\", "
            + " labresmst.result_validated_by AS \"resultValidatedBy\", "
            + " labresmst.result_validated_datetime AS \"resultValidatedDatetime\", "
            + " lbsampledtls.sample_collection_datetime AS \"sampleCollectionDatetime\", "
            + " labresmst.sample_no AS \"sampleNo\", "
            + " lbsampledtls.sample_recollect_flag AS \"sampleRecollectFlag\", "
            + " test_mst.test_code AS \"testCode\" , "
            + " samp_mst.sample_desc AS \"sampleType\","
            + " lbsampledtls.print AS \"print\", "
            + " lbsampledtls.re_print AS \"rePrint\","
            + "coalesce((case when app.is_pre_or_post_billing = 'Y' then tbpd.due_amount = 0 when app.is_pre_or_post_billing = 'N' then true end), false) as \"billSettled\","
            + " lbsampledtls.p_image as \"pImage\", "
        	+ "  lbsampledtls.sample_recollect_against_id AS \"sampleRecollectAgainstId\", "
            +" (case when ord_mst.encounter_id > 0 then " +
            " ( tpsd.payment_entitlement_id ) " +
            " else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
            +" ord_mst.order_no AS \"orderNo\","
            +" ord_mst.order_id AS \"orderId\","
            +" (case when ord_mst.encounter_id > 0 then " +
            " ( tpsd.payee_id ) " +
            " else ( tpsd2.payee_id ) end ) as \"payeeId\", "
            + " lbsampledtls.updated_date AS \"releaseDate\", "
            + "  to_char(lbsampledtls.updated_date, 'DD/MM/YYYY HH24:MI:SS') AS \"releaseDateString\",   "
            + " lbsampledtls.p_image_name as \"fileName\" ,"
            + " (case when " + 
            " (case when ord_mst.encounter_id > 0 then " + 
            " ( tpsd.payment_entitlement_id ) " + 
            " else ( tpsd2.payment_entitlement_id ) end ) = 2 then (select block_report_print from adt.t_insurance where insurance_id = " + 
            " (case when ord_mst.encounter_id > 0 then " + 
            " ( tpsd.insurance_id ) " + 
            " else ( tpsd2.insurance_id ) end ))  else ( select block_report_print from t_company_master   where company_id = " + 
            " (case when ord_mst.encounter_id > 0 then " + 
            " ( tpsd.payee_id ) " + 
            " else ( tpsd2.payee_id ) end )) " + 
            " end) as \"blockReportPrint\","
            + "order_details.is_dummy_service as \"isDummyService\" "
            +" FROM "
            +"  lab.t_lab_sample_dtls lbsampledtls "
            +"  INNER JOIN lab.t_lab_sample_master sample_mst ON lbsampledtls.lab_sample_id = sample_mst.lab_sample_id  "
            + "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id "
            + " left join billing.t_billing_details bill_dtls on " 
   		 + " 	order_details.order_details_id = bill_dtls.order_details_id " 
   		 + " left join billing.m_billing_master bill_mst on " 
   		 + " 	bill_mst.billing_master_id = bill_dtls.billing_master_id " 
            + "inner join t_unit_application_billing_configuration app on " + 
            " app.unit_id = lbsampledtls.org_unit_id " + 
            "  left join billing.t_billing_details tbd on " + 
            " lbsampledtls.order_details_id = tbd.order_details_id " + 
            " left join billing.m_billing_master mbm on " + 
            " mbm.billing_master_id = tbd.billing_master_id and mbm.status='A' and mbm.bill_cancelled = 'N'" + 
            " left join billing.t_bill_payee_details tbpd on " + 
            " tbpd.bill_id = mbm.billing_master_id " + 
            " and tbpd.payee_id = app.self_payee_id "
            +"  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id "
            + "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_mst.sample_id "
            +"  INNER JOIN patient.t_patient_registration pati_mst ON lbsampledtls.patient_id = pati_mst.patient_id "
            +"  INNER JOIN m_prefix_master pre_mst ON pati_mst.prefix_id = pre_mst.prefix_id "
            +"  INNER JOIN m_gender_master gen_mst ON pati_mst.gender_id = gen_mst.gender_id "
            +"  INNER JOIN m_visit_type_master vistmst ON vistmst.visit_type_id = sample_mst.visit_type_id "
            +"  INNER JOIN public.t_order_master ord_mst ON ord_mst.order_id = lbsampledtls.order_id "
            +"  LEFT JOIN lab.t_lab_test_result labresmst ON labresmst.lab_sample_dtls_id = lbsampledtls.lab_sample_dtls_id and labresmst.sample_status_id = 8 and labresmst.status ='A' "
            +"  LEFT JOIN doctor.m_doctor_master doct_mst ON lbsampledtls.doctor_id = doct_mst.doctor_id "
        	+ "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = ord_mst.ord_doc_spl_id "
            + " left join opd.t_patient_sponsor_details tpsd "
            + " on ord_mst.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
            + " left join opd.t_patient_sponsor_details tpsd2 "
            + " on tpsd2.admission_id = ord_mst.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
            +"  LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id "
			+ " left join billing.t_credit_note_detail tcnd on "
			+ " tcnd.order_detail_id = order_details.order_details_id "
			+ " and tcnd.status = 'A' "
			+ " and tcnd.cancel_or_discount = 1 "
			+ "  INNER JOIN lab.m_report_type_master report_mst ON test_mst.report_type_id = report_mst.report_type_id  "
            +"WHERE "
            +"  lbsampledtls.sample_status_id = :sampleStatusId "
            +"  and (tbd.is_cancelled isnull or tbd.is_cancelled='N') "
            +"  AND lbsampledtls.org_id = :orgId "
            +"  AND lbsampledtls.org_unit_id = :orgUnitId "
           + "  AND lbsampledtls.dept_id  in (:deptId) "
            + "  and order_details.ord_cancelled = 'N' "
        	+ "  and order_details.status = 'A' "
        	+ " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
        	  + " and (case when  sample_mst.visit_type_id not in(1) then (case when bill_mst.billing_master_id  is not null then  bill_mst.bill_cancelled = 'N' else bill_mst.billing_master_id  is null  end )  else bill_mst.billing_master_id  is not null and bill_mst.bill_cancelled = 'N' end) "
            +"  AND lbsampledtls.sample_status_id <> 4 "
			+ " and report_mst.report_type_status = 'A' "
			+ " and COALESCE(report_mst.is_template, false) = false "
),

    
    @NamedNativeQuery(name = "BULK_EMAIL_WORKLIST_PATIENT_SEARCH",
    query = " SELECT "
    	+" distinct  labresmst.lab_test_res_id as \"labTestResId\","
        +"  labresmst.test_id AS \"testId\", "
        +"  labresmst.dept_id AS \"deptId\", "
        +"  test_mst.dept_id AS \"subDeptId\", "
        + " ord_mst.order_id AS \"orderId\", "
        + " sample_mst.sample_id AS \"sampleId\", "
        +"  lbsampledtls.patient_id AS \"patientId\", "
        +"  labresmst.visit_type_id AS \"visitTypeId\", "
        +"  labresmst.created_by AS \"createdBy\", "
        +"  labresmst.admission_id AS \"visitAdmId\", "
    	+ "	ord_mst.order_date as \"orderDateTime\", "
    	+ "  to_char(ord_mst.order_date, 'DD/MM/YYYY HH24:MI:SS') AS \"orderDateTimeString\",   "
        +"  labresmst.order_details_id AS \"orderDetailsId\", "
        +"  subspe_mst.sub_speciality_name AS \"deptName\", "
        +"  labresmst.result_enter_datetime AS \"resultEnterDatetime\", "
        +"  labresmst.result_enter_by AS \"resultEnterBy\", "
        +"  vistmst.visit_type_code AS \"visitType\", "
        +"  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\", "
        +"  pati_mst.uhid_number AS uhid, "
        +"  sample_mst.sample_no AS \"labSampleNo\",  "
        +"  test_mst.test_desc AS \"testDesc\", "
        +"  test_mst.test_type AS \"testType\", "
        +"  pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name AS \"patientDetails\", "
        +"  EXTRACT( "
        +"    YEAR "
        +"    FROM "
        +"      age(lbsampledtls.patient_visit_age, pati_mst.dob) "
        +"  ) AS \"patientAgeInYears\", "
        +"  gen_mst.gender_code \"patientGender\", "
        +"  CURRENT_DATE - CAST(pati_mst.dob AS DATE) AS \"patientAgeDays\", "
        +"  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name AS \"doctorDetails\", "
        +"  lbsampledtls.sample_barcode AS \"sampleBarcode\", "
        +"  lbsampledtls.assign_unit AS \"assingnedFrom\" ,"
        + " lbsampledtls.org_id AS \"orgId\", "
        + " lbsampledtls.org_unit_id AS \"orgUnitId\", "
        + " lbsampledtls.is_mail_send AS \"isMailSend\", "
        + " doct_mst.doctor_id AS \"doctorId\", "
        + " doct_mst.email as \"doctorMail\", "
        + " lbsampledtls.gender_id AS \"genderId\", "
        + " pati_mst.email as \"patientMail\", "
        + " pati_mst.mobile_number as \"mobileNo\", "
        + " pati_mst.image_path AS \"imagePath\", "
        + " test_mst.pro_release AS \"proRelease\", "
        + " labresmst.report_type AS \"reportType\", "
        + " labresmst.result_authorised_flag AS \"resultAuthorisedFlag\", "
        + " labresmst.result_validated_by AS \"resultValidatedBy\", "
        + " labresmst.result_validated_datetime AS \"resultValidatedDatetime\", "
        + " lbsampledtls.sample_collection_datetime AS \"sampleCollectionDatetime\", "
        + " labresmst.sample_no AS \"sampleNo\", "
        + " lbsampledtls.sample_recollect_flag AS \"sampleRecollectFlag\", "
        + " test_mst.test_code AS \"testCode\" , "
        + " samp_mst.sample_desc AS \"sampleType\","
        + " lbsampledtls.print AS \"print\", "
        + " lbsampledtls.re_print AS \"rePrint\","
        + "coalesce((case when app.is_pre_or_post_billing = 'Y' then tbpd.due_amount = 0 when app.is_pre_or_post_billing = 'N' then true end), false) as \"billSettled\","
        + " lbsampledtls.p_image as \"pImage\", "
    	+ "  lbsampledtls.sample_recollect_against_id AS \"sampleRecollectAgainstId\", "
        +" (case when ord_mst.encounter_id > 0 then " +
        " ( tpsd.payment_entitlement_id ) " +
        " else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
        +" ord_mst.order_no AS \"orderNo\","
        +" (case when ord_mst.encounter_id > 0 then " +
        " ( tpsd.payee_id ) " +
        " else ( tpsd2.payee_id ) end ) as \"payeeId\", "
        + " lbsampledtls.updated_date AS \"releaseDate\", "
        + "  to_char(lbsampledtls.updated_date, 'DD/MM/YYYY HH24:MI:SS') AS \"releaseDateString\",   "
        + " lbsampledtls.p_image_name as \"fileName\", "
        + "order_details.is_dummy_service as \"isDummyService\" "
        +"FROM "
        +"  lab.t_lab_sample_dtls lbsampledtls "
        + "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id "
        +"  INNER JOIN lab.t_lab_sample_master sample_mst ON lbsampledtls.lab_sample_id = sample_mst.lab_sample_id  "
        + "inner join t_unit_application_billing_configuration app on " + 
        " app.unit_id = lbsampledtls.org_unit_id " + 
        "  left join billing.t_billing_details tbd on " + 
        " lbsampledtls.order_details_id = tbd.order_details_id " + 
        " left join billing.m_billing_master mbm on " + 
        " mbm.billing_master_id = tbd.billing_master_id " + 
        " left join billing.t_bill_payee_details tbpd on " + 
        " tbpd.bill_id = mbm.billing_master_id " + 
        " and tbpd.payee_id = app.self_payee_id "
        +"  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id "
        + "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_mst.sample_id "
        +"  LEFT JOIN lab.t_lab_test_result labresmst ON labresmst.lab_sample_dtls_id = lbsampledtls.lab_sample_dtls_id and labresmst.sample_status_id = 8 and labresmst.status  = 'A' "
        +"  INNER JOIN patient.t_patient_registration pati_mst ON lbsampledtls.patient_id = pati_mst.patient_id "
        +"  INNER JOIN m_prefix_master pre_mst ON pati_mst.prefix_id = pre_mst.prefix_id "
        +"  INNER JOIN m_gender_master gen_mst ON pati_mst.gender_id = gen_mst.gender_id "
        +"  INNER JOIN lab.t_lab_sample_master labsammst ON labsammst.lab_sample_id = lbsampledtls.lab_sample_id "
        +"  INNER JOIN m_visit_type_master vistmst ON vistmst.visit_type_id = labsammst.visit_type_id "
        +"  INNER JOIN public.t_order_master ord_mst ON ord_mst.order_id = lbsampledtls.order_id "
        +"  LEFT JOIN doctor.m_doctor_master doct_mst ON lbsampledtls.doctor_id = doct_mst.doctor_id "
    	+ "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = ord_mst.ord_doc_spl_id "
        + " left join opd.t_patient_sponsor_details tpsd "
        + " on ord_mst.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
        + " left join opd.t_patient_sponsor_details tpsd2 "
        + " on tpsd2.admission_id = ord_mst.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
        +"  LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id "
        +"  LEFT JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id "
    	+"  INNER join public.t_lab_configuration lab_config on "
		+"	lab_config.organisation_id = :orgId and lab_config.unit_id = :orgUnitId  "
		+ " left join billing.t_credit_note_detail tcnd on "
		+ " tcnd.order_detail_id = order_details.order_details_id "
		+ " and tcnd.status = 'A' "
		+ " and tcnd.cancel_or_discount = 1 "
        +"WHERE "
        +"  lbsampledtls.sample_status_id = :sampleStatusId "
        +"  AND lbsampledtls.org_id = :orgId "
        +"  AND lbsampledtls.org_unit_id = :orgUnitId "
        +"  AND lbsampledtls.dept_id = :deptId "
        + "  and order_details.ord_cancelled = 'N' "
    	+ "  and order_details.status = 'A' "
    	+ " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
        +"  AND lbsampledtls.sample_status_id <> 4 "
),

    /** REPORT_HAND_OVER_COUNT */
    @NamedNativeQuery(name = "GET_BIOCHEM_REPORT_HAND_OVER_COUNT",
        query = "  SELECT "
            +"  COUNT(*) "
            +"FROM "
            +"  lab.t_lab_sample_dtls lbsampledtls "
            +"  INNER JOIN public.t_order_master ord_mst ON ord_mst.order_id = lbsampledtls.order_id "
        	+ " inner join public.t_lab_configuration lab_config on "
			+ "	lab_config.organisation_id = :orgId and lab_config.unit_id = :orgUnitId  "
			+"  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id "
			+ "  INNER JOIN lab.m_report_type_master report_mst ON test_mst.report_type_id = report_mst.report_type_id  "
            +"WHERE "
            +"  lbsampledtls.sample_status_id = :sampleStatusId "
            +"  AND lbsampledtls.org_id = :orgId "
            +"  AND lbsampledtls.sample_status_id <> 4 "
            +"  AND lbsampledtls.org_unit_id = :orgUnitId "
            +"  AND lbsampledtls.dept_id = :deptId "
            +"  AND lbsampledtls.sub_dept_id IN(lab_config.biochemistry_id,lab_config.hematology_id,lab_config.microbiology_id,lab_config.histopathology_id,lab_config.cystopathology_id,lab_config.serology_id,lab_config.clinical_pathology_id) "
			+ " and report_mst.report_type_status = 'A' "
			+ " and COALESCE(report_mst.is_template, false) = false "
	),


    /** @Query for Lab Parameter history */
    @NamedNativeQuery(name = "GET_PARAMETER_FOR_HISTORY",
        query = "  SELECT  " 
            + " parameter_master.parameter_id AS \"parameterId\",  "
            + " parameter_master.parameter_name AS \"parameterName\", " 
            + " parameter_master.refrange_type_id AS \"refrangeTypeId\"  " 
            + "FROM  "
            + " lab.m_test_master test_master  "
            + "INNER JOIN lab.t_test_parameter_mppr test_parameter_mppr ON  "
            + " test_parameter_mppr.test_id = test_master.test_id  "
            + "INNER JOIN lab.m_parameter_master parameter_master ON  "
            + " parameter_master.parameter_id = test_parameter_mppr.parameter_id  " + "WHERE  "
            + " test_master.test_id =:testId  "
            + " AND test_master.is_histo_cyto = 'N'  "
            + " AND parameter_master.status = 'A'  "
            + " AND test_parameter_mppr.is_deleted = 'N'  "
            + " AND test_parameter_mppr.org_id = :orgId  "
            + " AND test_parameter_mppr.org_unit_id =:orgUnitId   "),
    
    
    @NamedNativeQuery(name="GET_RETEST_RECOLLECT_PARAMETERS",query=" SELECT "
        +"  CAST( "
        +"      lab_res_dtls.final_result AS TEXT "
        +"  ) AS \"result\", "
        +"  lab_res_dtls.created_date AS \"createdDate\" "
        +"FROM "
        +"  lab.t_lab_res_dtls lab_res_dtls "
        +"WHERE "
        +"  lab_res_dtls.lab_result_id =:labResultId "
        +"  AND lab_res_dtls.parameter_id =:parameterId "
        +"ORDER BY "
        +"  lab_res_dtls.created_date DESC "),

    @NamedNativeQuery(name = "GET_PRIVIOUS_TEST_RELEASE_ID", query = " SELECT  "
        +" MAX( lab_test_result.lab_test_res_id )  "
        +"FROM  "
        +" lab.t_lab_test_result lab_test_result  "
        +"INNER JOIN lab.t_lab_res_dtls lab_res_dtls ON  "
        +" lab_res_dtls.lab_result_id = lab_test_result.lab_test_res_id  "
        +"WHERE  "
        +" lab_test_result.result_level = 3  "
        +" AND lab_test_result.sample_status_id = :sampleStatusId  "
        +" AND lab_test_result.org_id =:orgId  "
        +" AND lab_test_result.org_unit_id =:orgUnitId  "
        +" AND lab_test_result.dept_id =:deptId  "
        +" AND lab_test_result.sub_dept_id =:subDeptId  "
        +" AND lab_test_result.patient_id =:patientId  "
        +" AND lab_test_result.test_id =:testId  "),



    @NamedNativeQuery(name = "GET_RETEST_RECOLLECT_LAB_RESULT_ID",
        query =   " WITH RECURSIVE sample_dtls AS(  "
            +"  SELECT  "
            +"    labsamp_one.lab_sample_dtls_id,  "
            +"    labsamp_one.sample_recollect_against_id,  "
            +"    labsamp_one.test_id  "
            +"  FROM  "
            +"    lab.t_lab_sample_dtls labsamp_one  "
            +"  WHERE  "
            +"    labsamp_one.lab_sample_dtls_id = :labSampleDtlsId  "
            +"    AND labsamp_one.sample_recollect_against_id IS NOT NULL  "
            +"  UNION  "
            +"  SELECT  "
            +"    labsamp_two.lab_sample_dtls_id,  "
            +"    labsamp_two.sample_recollect_against_id,  "
            +"    labsamp_two.test_id  "
            +"  FROM  "
            +"    lab.t_lab_sample_dtls labsamp_two,  "
            +"    sample_dtls  "
            +"  WHERE  "
            +"    labsamp_two.lab_sample_dtls_id = sample_dtls.sample_recollect_against_id  "
            +"    AND labsamp_two.sample_recollect_against_id IS NOT NULL  "
            +")  "
            +"SELECT  "
            +"  lab_test_result.lab_test_res_id AS \"labTestResId\",  "
            +"  test_mst.test_type AS \"testType\",  "
            +"  test_mst.test_id AS \"testId\",  "
            +"  lab_test_result.result_enter_datetime AS \"resultEnterDatetime\",  "
            +"  result_enter.user_name AS \"resultEnterByUser\",  "
            +"  lab_test_result.result_validated_datetime AS \"resultValidatedDatetime\",  "
            +"  result_val.user_name AS \"resultValidatedByUser\",  "
            +"  lab_test_result.result_authorised_datetime AS \"resultAuthorisedDatetime\",  "
            +"  result_auth.user_name AS \"resultAuthorisedByUser\",  "
            +"  lab_test_result.foot_notes AS \"footsNotes\" , "
            +"  lab_test_result.sample_no AS \"labSampleNo\" , "
            +"  lab_test_result.created_date AS \"createdDate\"  "
            +"FROM  "
            +"  sample_dtls sampledtls  "
            +"  INNER JOIN lab.t_lab_test_result lab_test_result ON lab_test_result.lab_sample_dtls_id = sampledtls.sample_recollect_against_id  "
            +"  INNER JOIN lab.m_test_master test_mst ON test_mst.test_id = sampledtls.test_id  "
            +"  LEFT JOIN public.m_user_master result_enter ON result_enter.user_id = lab_test_result.result_enter_by  "
            +"  LEFT JOIN public.m_user_master result_val ON result_val.user_id = lab_test_result.result_validated_by  "
            +"  LEFT JOIN public.m_user_master result_auth ON result_auth.user_id = lab_test_result.result_authorised_by  "
            +"WHERE  "
          /*  +"  lab_test_result.result_level = :resultLevel  AND"*/
            +"   lab_test_result.sample_status_id = :recollect  "
            +"UNION  "
            +"SELECT  "
            +"  lab_test_result.lab_test_res_id AS \"labTestResId\",  "
            +"  test_mst.test_type AS \"testType\",  "
            +"  test_mst.test_id AS \"testId\",  "
            +"  lab_test_result.result_enter_datetime AS \"resultEnterDatetime\",  "
            +"  result_enter.user_name AS \"resultEnterByUser\",  "
            +"  lab_test_result.result_validated_datetime AS \"resultValidatedDatetime\",  "
            +"  result_val.user_name AS \"resultValidatedByUser\",  "
            +"  lab_test_result.result_authorised_datetime AS \"resultAuthorisedDatetime\",  "
            +"  result_auth.user_name AS \"resultAuthorisedByUser\",  "
            +"  lab_test_result.foot_notes AS \"footsNotes\" , "
            +"  lab_test_result.sample_no AS \"labSampleNo\" , "
            +"  lab_test_result.created_date AS \"createdDate\"  "
            +"FROM  "
            +"  lab.t_lab_test_result lab_test_result  "
            +"  INNER JOIN lab.m_test_master test_mst ON test_mst.test_id = lab_test_result.test_id  "
            +"  LEFT JOIN public.m_user_master result_enter ON result_enter.user_id = lab_test_result.result_enter_by  "
            +"  LEFT JOIN public.m_user_master result_val ON result_val.user_id = lab_test_result.result_validated_by  "
            +"  LEFT JOIN public.m_user_master result_auth ON result_auth.user_id = lab_test_result.result_authorised_by  "
            +"WHERE  "
            +"  lab_test_result.sample_status_id = :retest  "
            +"  AND lab_test_result.org_id = :orgId  "
            +"  AND lab_test_result.org_unit_id = :orgUnitId  "
            +"  AND lab_test_result.dept_id = :deptId  "
            +"  AND lab_test_result.sub_dept_id = :subDeptId  "
            +"  AND lab_test_result.patient_id = :patientId  "
            +"  AND lab_test_result.test_id = :testId  "
            +"  AND lab_test_result.lab_sample_dtls_id = :labSampleDtlsId  "
          /*  +"  AND lab_test_result.result_level = :resultLevel  "*/

    ),


    @NamedNativeQuery(name = "GET_RESULT_ENTERY_BY_DATE_TIME",
      /*  query = " SELECT  " + " lab_test_result.result_enter_datetime AS \"resultEnterDatetime\",  "
        		+ " lab_test_result.machine_id AS \"machineId\", "
            + " user_master.user_name AS \"resultEnterByUser\" ,lab_test_result.foot_notes AS \"footsNotes\"  "
        		+ "FROM  "
            + " lab.t_lab_test_result lab_test_result  "
            + " LEFT JOIN public.m_user_master user_master ON  "
            + " user_master.user_id = lab_test_result.result_enter_by  "
            + " LEFT JOIN lab.t_lab_sample_dtls dtl ON  "
            + " WHERE  "
            + " lab_test_result.lab_test_res_id =:labTestResId  "
            + " AND lab_test_result.org_id =:orgId  "
            + " AND lab_test_result.org_unit_id =:orgUnitId  "*/ 
				query = " SELECT   lab_test_result.result_enter_datetime AS \"resultEnterDatetime\",  "
						+ " lab_test_result.machine_id AS \"machineId\", "
						+ " dtl.internal_notes as \"internalNotes\", " + " dtl.srf_id as \"srfId\", "
						+ " dtl.icmr_id as \"icmrId\", dtl.is_on_hold_parameter as \"isOnHoldParameter\", "
						+ " user_master.user_name AS \"resultEnterByUser\" ,lab_test_result.foot_notes AS \"footsNotes\" "
						+ " FROM " + " lab.t_lab_test_result lab_test_result  "
						+ " LEFT JOIN public.m_user_master user_master ON  "
						+ " user_master.user_id = lab_test_result.result_enter_by  "
						+ " LEFT JOIN lab.t_lab_sample_dtls dtl ON  dtl.lab_sample_dtls_id =lab_test_result.lab_sample_dtls_id "
						+ " WHERE  " + " lab_test_result.lab_test_res_id =:labTestResId  "
						+ " AND lab_test_result.org_id =:orgId  " + " AND lab_test_result.org_unit_id =:orgUnitId  "
    	            ),


    @NamedNativeQuery(name = "GET_RESULT_VALIDATED_BY_DATE_TIME",
        query = "SELECT  " + " lab_test_result.result_enter_datetime AS \"resultEnterDatetime\",  "
        		+ " lab_test_result.machine_id AS \"machineId\", "
            + " result_enter.user_name AS \"resultEnterByUser\",  "
            + " dtl.internal_notes as \"internalNotes\", "
            + " dtl.srf_id as \"srfId\", " 
			+ " dtl.icmr_id as \"icmrId\", "
			+ " dtl.is_on_hold_parameter as \"isOnHoldParameter\", "
            + " lab_test_result.result_validated_datetime AS \"resultValidatedDatetime\",  "
            + " result_val.user_name AS \"resultValidatedByUser\" ,  lab_test_result.foot_notes AS \"footsNotes\" " 
            + " FROM  "
            + " lab.t_lab_test_result lab_test_result  "
            + "LEFT JOIN public.m_user_master result_enter ON  "
            + " result_enter.user_id = lab_test_result.result_enter_by  "
            + "LEFT JOIN public.m_user_master result_val ON  "
            + " result_val.user_id = lab_test_result.result_validated_by  "
            + " LEFT JOIN lab.t_lab_sample_dtls dtl ON  dtl.lab_sample_dtls_id =lab_test_result.lab_sample_dtls_id " 
            + "WHERE  "
            + " lab_test_result.lab_test_res_id =:labTestResId  "
            + " AND lab_test_result.org_id =:orgId  "
           // + " AND lab_test_result.org_unit_id =:orgUnitId "
            + " "),

    @NamedNativeQuery(name = "GET_RESULT_RELEASE_BY_DATE_TIME",
        query = " select " + 
        		"	lab_test_result.result_enter_datetime as \"resultEnterDatetime\", " + 
        		"	result_enter.user_name as \"resultEnterByUser\", " + 
        		"	lab_test_result.result_validated_datetime as \"resultValidatedDatetime\", " + 
        		"	result_val.user_name as \"resultValidatedByUser\", " + 
        		"	lab_test_result.result_authorised_datetime as \"resultAuthorisedDatetime\", " + 
        		"	result_auth.user_name as \"resultAuthorisedByUser\", " + 
        		"	sample_dtls.is_on_hold_parameter as \"isOnHoldParameter\" " + 
        		" from " + 
        		"	lab.t_lab_test_result lab_test_result " + 
        		" left join public.m_user_master result_enter on " + 
        		"	result_enter.user_id = lab_test_result.result_enter_by " + 
        		" left join public.m_user_master result_val on " + 
        		"	result_val.user_id = lab_test_result.result_validated_by " + 
        		" left join public.m_user_master result_auth on " + 
        		"	result_auth.user_id = lab_test_result.result_authorised_by " + 
        		" left join lab.t_lab_sample_dtls sample_dtls on " + 
        		"	sample_dtls.lab_sample_dtls_id = lab_test_result.lab_sample_dtls_id  " + 
        		" where " + 
        		"	lab_test_result.lab_test_res_id =:labTestResId " + 
        		"	and lab_test_result.org_id =:orgId " + 
        		"	and lab_test_result.org_unit_id =:orgUnitId"),
    
    
    @NamedNativeQuery(name = "SEARCH_LAB_PATIENT", query = 
    " SELECT  "
             +"  pati_mst.patient_id AS \"id\",  "
             +"  pati_mst.first_name AS \"firstName\",  "
             +"  pati_mst.last_name AS \"lastName\",  "
             +"  pati_mst.uhid_number AS \"uhid\"  "
             +"FROM  "
             +"  patient.t_patient_registration pati_mst "
             +"WHERE  "
             +"  pati_mst.organization_id = :orgId  "
             +"  AND pati_mst.unit_id = :orgUnitId  "
             +"  AND pati_mst.status = 'A'  "
             +"  AND (  "
             +"    LOWER(pati_mst.first_name) LIKE (:searchKeyword)  "
             +"    OR lower(pati_mst.last_name) LIKE lower(:searchKeyword)  "
             +"    OR lower(pati_mst.uhid_number) LIKE lower(:searchKeyword)) "),
    @NamedNativeQuery(name = "PRINT_COVER_SHEET_REPORTS", query = 
    		 " select "
    		 +" labresmst.lab_test_res_id as \"labTestResId\","
    		 +" labresmst.test_id as \"testId\","
    		 +" labresmst.dept_id as \"deptId\","
    		 +" labresmst.sub_dept_id as \"subDeptId\","
    		 +" labresmst.patient_id as \"patientId\","
    		 +" labresmst.sample_no as \"sampleNo\","
    		 +" labresmst.org_id as \"orgId\","
    		 +" labresmst.org_unit_id as \"orgUnitId\","
    		 +" lbsampledtls.lab_sample_dtls_id as \"labSampleDtlsId\","
    		 +" labresmst.result_enter_datetime as \"resultEnterDatetime\","
    		 +"labresmst.result_enter_by as \"resultEnterBy\","
    		 +" labresmst.result_validated_datetime as \"resultValidatedDatetime\","
    		 +" labresmst.result_validated_by as \"resultValidatedBy\","
    		 +" labresmst.result_authorised_datetime as \"resultAuthorisedDatetime\","
    		 +" labresmst.result_authorised_by as \"resultAuthorisedBy\","
    		 +" labresmst.result_authorised_flag as \"resultAuthorisedFlag\","
    		 +" labresmst.result_handover_datetime as \"resultHandoverDatetime\","
    		 +"labresmst.result_handover_by as \"resultHandoverBy\","
    		 +" labresmst.suggetion_notes as \"suggetionNotes\","
    		 +" labresmst.foot_notes as \"footsNotes\","
    		 +" labresmst.report_type as \"reportType\","
    		 +" panel_master.panel_id as \"panelId\","
    		 +" lbsampledtls.gender_id as \"genderId\","
    		 +" lbsampledtls.sample_recollect_flag as \"sampleRecollectFlag\","
    		 +" lbsampledtls.sample_recollect_against_id as \"sampleRecollectAgainstId\","
    		 +" pati_mst.uhid_number as \"uhid\","
    		 +"samp_mst.sample_no as \"labSampleNo\","
    		 +" test_mst.test_desc as \"testDesc\","
    		 +" test_mst.test_type as \"testType\","
    		 +" subspe_mst.sub_speciality_name as \"deptName\","
    		 +"  EXTRACT( "
    	        +"    YEAR "
    	        +"    FROM "
    	        +"      age(lbsampledtls.patient_visit_age, pati_mst.dob) "
    	        +"  ) AS \"patientAgeInYears\", "
    		 +" pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || pati_mst.last_name || ' ' || calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code as \"patientDetails\","
    		+" cast(lbsampledtls.patient_visit_age as date) - cast(pati_mst.dob as date) as \"patientAgeDays\","
    		//+" 'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' as \"doctorDetails\","
    		+" 'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name  as \"doctorDetails\","
    		+" lbsampledtls.sample_collection_datetime as \"sampleCollectionDatetime\","
    		+" test_mst.test_code as \"testCode\","
    		+" gen_mst.gender_name \"patientGender\" "
    		 
    		+" from  lab.t_lab_test_result labresmst "
    		+" inner join lab.t_lab_sample_dtls lbsampledtls on "
    		+" labresmst.lab_sample_dtls_id = lbsampledtls.lab_sample_dtls_id "
    		+" inner join lab.t_lab_sample_master samp_mst on "
    		+" lbsampledtls.lab_sample_id = samp_mst.lab_sample_id "
    		+" inner join public.t_order_master order_master on "
    		+" order_master.order_id = lbsampledtls.order_id "
    		+" inner join lab.m_test_master test_mst on "
    		+" lbsampledtls.test_id = test_mst.test_id "
    		+" inner join patient.t_patient_registration pati_mst on "
    		+" lbsampledtls.patient_id = pati_mst.patient_id "
    		+" left join m_prefix_master pre_mst on "
    		+" pati_mst.prefix_id = pre_mst.prefix_id "
    		+" left join m_gender_master gen_mst on "
    		+" pati_mst.gender_id = gen_mst.gender_id "
    		+" left join doctor.m_sub_speciality_master subspe_mst on "
    	   +"  subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id "
    		+" left join doctor.m_doctor_master doct_mst on "
    		+" order_master.ord_doctor_id = doct_mst.doctor_id "
    		/*+" left join doctor.t_doctor_speciality_mapper doct_speciality_mppr on "
    		+" doct_speciality_mppr.doctor_id = order_master.ord_doctor_id "*/
    		+" left join doctor.m_speciality_master speciality_master on "
    		+" speciality_master.speciality_id = order_master.ord_doc_spl_id "
    		+"	left join lab.m_priority_master pri_mst on "
    		+" lbsampledtls.priority_id = pri_mst.priority_id "
    		+" left join lab.t_panel_master panel_master on "
    		+" panel_master.panel_id = lbsampledtls.profile_id "
    		+"	where labresmst.lab_test_res_id=:labResultId"),


    @NamedNativeQuery(name = "GET_TEST_FOR_RESULT", query = "SELECT "
    		+ "  distinct (tm.test_id) AS \"testId\", "
    		+ "  tm.test_type AS \"testType\", "
    		+ "  tm.test_desc AS \"testDesc\", "
    		+ "  tm.test_code AS \"testCode\","
    		+ "  tm.report_type_id AS \"reportType\" ,"
    		+ "  technique.technique_name AS \"techniqueName\", "
    		+ "  panel.panel_alies AS \"panelAlies\", "
    		+ "  panel.panel_code AS \"panelCode\", "
    		+ "  smpdtls.sample_barcode AS \"labSampleNo\", "
    		+ "  smpdtls.lab_sample_dtls_id AS \"labSampleDtlsId\", "
    		+ "  tm.org_id AS \"orgId\", "
    		+ "  tm.org_unit_id AS \"unitId\","
    		+ "  tm.service_id as \"serviceId\", "
    		+ " smpdtls.srf_id as \"srfId\", "
    		+ "  smpdtls.is_on_hold_parameter AS \"isOnHoldParameter\","
    		+"   smpdtls.org_unit_id as \"assignUnit\","
    		//+"   smpdtls.test_id as \"testId\", "
    		+ "  smpdtls.perform_test_id as \"performTestId\", "
    		+ " tltr.lab_test_res_id as \"labTestResId\" "
    		+ "FROM "
    		+ "  lab.t_lab_sample_dtls smpdtls "
    		+ "INNER JOIN lab.m_test_master tm ON "
    		+ "  smpdtls.test_id = tm.test_id "
    		+ "INNER JOIN lab.m_technique_master technique ON "
    		+ "  tm.technique_id = technique.technique_id "
    		+ "LEFT JOIN lab.t_panel_master panel ON "
    		+ "  smpdtls.profile_id = panel.panel_id "
    		+ " LEFT JOIN lab.t_lab_test_result tltr ON " + 
    		"  tltr.lab_sample_dtls_id = smpdtls.lab_sample_dtls_id and tltr.status = 'A' and tltr.sample_status_id not in(7) "
    		+ " WHERE "
    		+ "  tm.test_id = :testId "
    		+ "  AND tm.test_status = 'A' "
    		+ "  AND smpdtls.lab_sample_dtls_id = :labSampleDtlsId"
          ),
    
    @NamedNativeQuery(name = "GET_TEST_FOR_RESULT_FOR_VALIDATION_AND_RELEASE", query = "select "
    		+ "	tm.test_id as \"testId\", "
    		+ "	tm.test_type as \"testType\", "
    		+ "	tm.test_desc as \"testDesc\", "
    		+ "	tm.test_code as \"testCode\", "
            + "  tm.pro_release AS \"proRelease\", "
    		+ "	technique.technique_name as \"techniqueName\", "
    		+ "	panel.panel_alies as \"panelAlies\", "
    		+ "	panel.panel_code as \"panelCode\", "
    		+ "	smpdtls.sample_barcode as \"labSampleNo\", "
    		+ "	tm.org_id as \"orgId\", "
    		+ "	tm.org_unit_id as \"unitId\", "
    		+ "	ltres.foot_notes as \"footsNotes\", "
    		+ "	smpdtls.lab_sample_dtls_id as \"labSampleDtlsId\", "
    		 + "  smpdtls.is_on_hold_parameter AS \"isOnHoldParameter\",  " 
    		+ "	ltres.result_enter_datetime as \"resultEnterDatetime\", "
    		+ "	ltres.result_validated_datetime as \"resultValidatedDatetime\", "
    		+ "	ltres.result_authorised_datetime as \"resultAuthorisedDatetime\", "
    		+ "	(case "
    		+ "		when empmst1.employee_id>0 then concat(prefixmst1.prefix_code,' ', coalesce(empmst1.first_name, ''), ' ', coalesce(empmst1.last_name, '')) "
    		+ "		when  doct1.doctor_id>0  then concat('Dr.', coalesce(doct1.first_name, ''),' ', coalesce(doct1.last_name, '')) "
    		+ "	end) as \"resultEnterByUser\", "
    		+ "	(case "
    		+ "		when empmst2.employee_id>0 then concat(prefixmst2.prefix_code,' ', coalesce(empmst2.first_name, ''), ' ', coalesce(empmst2.last_name, '')) "
    		+ "		when doct2.doctor_id>0  then concat('Dr.', coalesce(doct2.first_name, ''),' ', coalesce(doct2.last_name, '')) "
    		+ "	end) as \"resultValidatedByUser\", "
    		+ "	(case "
    		+ "		when empmst3.employee_id>0 then concat(prefixmst3.prefix_code,' ',coalesce(empmst3.first_name, ''), ' ', coalesce(empmst3.last_name, '')) "
    		+ "		when doct3.doctor_id>0 then concat('Dr.', coalesce(doct3.first_name, ''),' ', coalesce(doct3.last_name, '')) "
    		+ "	end) as \"resultAuthorisedByUser\","
    		+ "ltres.machine_id as \"machineId\","
    		+ "smpdtls.internal_notes as \"internalNotes\","
    		+ " smpdtls.srf_id as \"srfId\", " 
			+ " smpdtls.icmr_id as \"icmrId\", "
    		+ "smpdtls.perform_test_id as \"performTestId\","
    		+ "smpdtls.assign_unit as \"assignUnit\" ,"
			+"smpdtls.org_unit_id as \"assingnedFrom\","
    		+ "smpdtls.performed_unit  as \"performedUnit\" "
    		+ " from "
    		+ "	lab.t_lab_sample_dtls smpdtls "
    		+ "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = smpdtls.order_details_id "
    		+ "inner join lab.t_lab_test_result ltres on "
    		+ "	ltres.lab_sample_dtls_id = smpdtls.lab_sample_dtls_id and ltres.status = 'A' "
    		+ "inner join lab.m_test_master tm on smpdtls.test_id = tm.test_id"
    		+ " inner join lab.m_technique_master technique on "
    		+ "	tm.technique_id = technique.technique_id "
    		+ "left join lab.t_panel_master panel on "
    		+ "	smpdtls.profile_id = panel.panel_id "
    		+ "left join public.m_employee_master empmst1 on "
    		+ "	empmst1.user_id = ltres.result_enter_by "
    		+ "left join public.m_prefix_master prefixmst1 on "
    		+ "	empmst1.prefix_id = prefixmst1.prefix_id "
    		+ "left join public.m_employee_master empmst2 on "
    		+ "	empmst2.user_id = ltres.result_validated_by "
    		+ "left join public.m_prefix_master prefixmst2 on "
    		+ "	empmst2.prefix_id = prefixmst2.prefix_id "
    		+ "left join public.m_employee_master empmst3 on "
    		+ "	empmst3.user_id = ltres.result_authorised_by "
    		+ "left join public.m_prefix_master prefixmst3 on "
    		+ "	empmst3.prefix_id = prefixmst3.prefix_id "
    		+ "left join doctor.m_doctor_master doct1 on "
    		+ "	doct1.user_id = ltres.result_enter_by "
    		+ "left join doctor.m_doctor_master doct2 on "
    		+ "	doct2.user_id = ltres.result_validated_by "
    		+ "left join doctor.m_doctor_master doct3 on "
    		+ "	doct3.user_id = ltres.result_authorised_by "
    		+ " left join billing.t_credit_note_detail tcnd on "
    		+ " tcnd.order_detail_id = order_details.order_details_id "
    		+ " and tcnd.status = 'A' "
    		+ " and tcnd.cancel_or_discount = 1 "
    		+ "where "
    		+ "	tm.test_id = :testId "
    		+ "	and tm.test_status = 'A' "
    		+ "	and smpdtls.lab_sample_dtls_id = :labSampleDtlsId "
    		+ "	and ltres.lab_test_res_id = :labTestResId"
    		+ " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
    		 + "  and order_details.ord_cancelled = 'N' "
         	+ "  and order_details.status = 'A' "
          ),


    @NamedNativeQuery(name = "GET_PARAMETER_FOR_RESULT", query = "select "
    		+ "	pm.parameter_id as \"parameterId\", "
    		+ "	mppr.header_id as \"headerId\", "
    		+ " header.total_count as \"totalCount\","
    		+ " header.is_parameter_calculation as \"isParameterCalculation\" ,"
    		+ "	header.header_code as \"headerCode\", "
    		+ "	header.header_desc as \"headerDesc\", "
    		+ "	mppr.test_id as \"testId\", "
    		+ "	pm.parameter_name as \"paramName\", "
    		+ "	unit.unit_name as \"paramUnit\", "
    		+ "	smpdtls.sample_barcode as \"sampleNo\", "
    		+ "	mppr.para_sequence as \"paramPrintOrder\", "
    		+ "	pm.refrange_type_id as \"refrangeTypeId\", "
    		+ "	pm.is_formula as \"isFormula\", "
    		+ " pm.is_editable as \"isEditable\", "
    		+ "	pm.param_precision as \"paramPrecision\", "
    		+ "	formula_mst.formula as \"parameterFormula\", "
    		+ "	pm.delta_per as \"deltaPer\", "
    		+ "	pm.delta_days_id as \"deltaDaysId\","
    		+ " pm.age_group_reqiured as \"ageGroupReqiured\" , "
    		+ " pm.gender_reqiured as \"genderReqiured\" , "
    		+ "	smpdtls.gender_id as \"genderId\", "
    		+ "	techniqueParam.technique_name as \"techniqueName\" "
    		+ "from "
    		+ "	lab.t_lab_sample_dtls smpdtls "
    		+ "inner join lab.m_test_master tm on "
    		+ "	smpdtls.test_id = tm.test_id "
    		+ "inner join lab.t_test_parameter_mppr mppr on "
    		+ "	mppr.test_id = tm.test_id "
    		+ "inner join lab.m_parameter_master pm on "
    		+ "	mppr.parameter_id = pm.parameter_id "
    		+ "inner join lab.m_technique_master technique on "
    		+ "	tm.technique_id = technique.technique_id "
    		+ "inner join lab.m_header_master header on "
    		+ "	  mppr.header_id = header.header_id "
    		+ "inner join lab.m_unit_master unit on "
    		+ "	pm.unit_id = unit.unit_id "
    		+ "left join lab.t_panel_master panel on "
    		+ "	smpdtls.profile_id = panel.panel_id "
    		+ "left join lab.m_formula_master formula_mst on "
    		+ "	pm.parameter_id = formula_mst.parameter_id  and formula_mst.status='A' "
    		+ " left join lab.m_technique_master techniqueParam on pm.technique_id = techniqueParam.technique_id "
    		+ "where "
    		+ "	tm.test_id = :testId "
    		+ "	and tm.test_status = 'A' "
    		+ "	and mppr.is_deleted = 'N' "
    		+ "	and smpdtls.lab_sample_dtls_id = :labSampleDtlsId order by mppr.header_sequence ,mppr.para_sequence "
          ),
    @NamedNativeQuery(name = "GET_NUMERIC_REF_RANGE_BY_PARAMETER", query = "SELECT "
    		+ "numref.refreng_id  as \"refrengId\", "
    		+ "	numref.min_value AS \"parameterMin\", "
    		+ "	numref.max_value AS \"parameterMax\", "
    		+ "	numref.less_than AS \"paramAbnrmlMin\", "
    		+"  numref.remark AS \"remarks\",  "
    		+ "	numref.more_than AS \"paramAbnrmlMax\" "
    		+ "FROM "
    		+ "	lab.m_param_refreng_master numref "
    		+ "Inner JOIN lab.m_age_group_master age_group ON "
    		+ "	numref.age_group_id = age_group.age_group_id "
    		+ "	AND numref.is_deleted = 'N' "
    		+ "	AND numref.parameter_id = :parameterId "
    		),
    
    @NamedNativeQuery(name = "GET_NUMERIC_REF_RANGE_BY_PARAMETER_WITHOUT_INNER", query = "SELECT "
    		+ "numref.refreng_id  as \"refrengId\", "
    		+ "	numref.min_value AS \"parameterMin\", "
    		+ "	numref.max_value AS \"parameterMax\", "
    		+ "	numref.less_than AS \"paramAbnrmlMin\", "
    		+"  numref.remark AS \"remarks\",  "
    		+ "	numref.more_than AS \"paramAbnrmlMax\" "
    		+ "FROM "
    		+ "	lab.m_param_refreng_master numref where numref.is_deleted = 'N' "
    		+ "	AND numref.parameter_id = :parameterId "
    		),
    
    
    @NamedNativeQuery(name = "GET_NUMERIC_REF_RANGE_BY_MACHINE_ID", query = "SELECT  " + 
    		"	numref.min_value AS \"parameterMin\",  " + 
    		"	numref.max_value AS \"parameterMax\",  " + 
    		"	numref.less_than AS \"paramAbnrmlMin\",  " + 
    		" 	numref.remark AS \"remarks\",   " + 
    		"	numref.more_than AS \"paramAbnrmlMax\" ,"+
    		 " numref.parameter_id as \"parameterId\", " + 
    		 "	lbunitmst.unit_name AS \"paramUnit\", "+
    		 " defaultLbunitmst.unit_name AS \"defaultParamUnit\" "+
    		"FROM  " + 
    		"	lab.m_param_refreng_master numref  " + 
    		"inner join lab.t_test_parameter_mppr ttpm  on ttpm.parameter_id  = numref.parameter_id   " + 
    		"INNER JOIN lab.m_age_group_master age_group ON  " + 
    		"	numref.age_group_id = age_group.age_group_id  " + 
    		"	AND numref.is_deleted = 'N'  " + 
    		"	and ttpm.test_id  = :testId " + 
    		"	and ttpm.test_para_status = 'A' " + 
    		"	AND numref.gender_id = :genderId  " + 
    		"	and numref.machine_id = :machineId " + 
    		"	AND age_group.age_group_status = 'A'  " + 
    		"	AND :ageInDays>=age_group.age_from_day " + 
    		"    AND :ageInDays<=age_group.age_to_day "+
    		" LEFT JOIN lab.m_unit_master lbunitmst on lbunitmst.unit_id = numref.unit_id "+
    		" INNER JOIN lab.m_parameter_master paramMst on paramMst.parameter_id = numref.parameter_id "+
    		" INNER JOIN lab.m_unit_master defaultLbunitmst on defaultLbunitmst.unit_id = paramMst.unit_id "
            ),
    @NamedNativeQuery(name = "GET_TEXTAUL_REF_RANGE_BY_PARAMETER", query =  "select "
    		+ "	 txt_range.textual_range_id as \"textualRangeId\", "
    		+"  txt_range.remark AS \"remarks\",  "
    		+ "	texrengemst.textual_range_name as \"textualRangeName\" "
    		+ "from "
    		+ "	 lab.m_param_textual_range txt_range "
    		+ "inner join lab.m_age_group_master age_group on "
    		+ "	txt_range.age_group_id = age_group.age_group_id "
    		+ "inner join lab.m_textual_range_master texrengemst on "
    		+ "    txt_range.textual_range_id = texrengemst.textual_range_id "
    		+ "	and txt_range.is_deleted = 'N' "
    		+ "	and txt_range.parameter_id = :parameterId " ),
    
    @NamedNativeQuery(name = "GET_TEXTAUL_REF_RANGE_BY_PARAMETER_WITHOUT_INNER", query =  "select "
    		+ "	 txt_range.textual_range_id as \"textualRangeId\", "
    		+"  txt_range.remark AS \"remarks\",  "
    		+ "	texrengemst.textual_range_name as \"textualRangeName\" "
    		+ "from "
    		+ "	 lab.m_param_textual_range txt_range "
    		+ "inner join lab.m_textual_range_master texrengemst on "
    		+ "    txt_range.textual_range_id = texrengemst.textual_range_id "
    		+ "	where txt_range.is_deleted = 'N' "
    		+ "	and txt_range.parameter_id = :parameterId " ),
    
    @NamedNativeQuery(name = "GET_TEXTAUL_REF_RANGE_BY_MACHINE_ID", query =  "select " + 
    		"	txt_range.textual_range_id as \"textualRangeId\", " + 
    		"  	txt_range.remark AS \"remarks\",  " + 
    		"	texrengemst.textual_range_name as \"textualRangeName\" ," +
    		"   txt_range.parameter_id as \"parameterId\" ," + 
    		"	lbunitmst.unit_name AS \"paramUnit\", "+
    		 " defaultLbunitmst.unit_name AS \"defaultParamUnit\" "+
    		"from " + 
    		"	 lab.m_param_textual_range txt_range  " + 
    		"inner join lab.t_test_parameter_mppr ttpm  on ttpm.parameter_id  = txt_range.parameter_id  " + 
    		"inner join lab.m_age_group_master age_group on  " + 
    		"	txt_range.age_group_id = age_group.age_group_id  " + 
    		"inner join lab.m_textual_range_master texrengemst on " + 
    		"    txt_range.textual_range_id = texrengemst.textual_range_id  " + 
    		"	and txt_range.is_deleted = 'N'  " + 
    		"	and ttpm.test_id  = :testId " + 
    		"	and ttpm.test_para_status = 'A' " + 
    		"	and txt_range.gender_id = :genderId  " + 
    		"	and txt_range.machine_id = :machineId  " + 
    		"	and age_group.age_group_status = 'A'  " + 
    		"	and :ageInDays>=age_group.age_from_day  " + 
    		"    and :ageInDays<=age_group.age_to_day "+
    		" LEFT JOIN lab.m_unit_master lbunitmst on lbunitmst.unit_id = txt_range.unit_id "+
    		" INNER JOIN lab.m_parameter_master paramMst on paramMst.parameter_id = txt_range.parameter_id "+
    		" INNER JOIN lab.m_unit_master defaultLbunitmst on defaultLbunitmst.unit_id = paramMst.unit_id "
            ),
    @NamedNativeQuery(name = "GET_MULTEXTUAL_REF_RANGE_BY_PARAMETER", query = "select "
    		+"	mulref.param_multitextual_range_id  as \"textualRangeId\", "  
    		+"  mulref.remark AS \"remarks\",  "
    		+ "	mulref.multitextaul_range as \"multitextaulRange\" "
    		+ "from "
    		+ "	lab.m_param_multitextual_range mulref "
    		+ "inner join lab.m_age_group_master age_group on "
    		+ "	mulref.age_group_id = age_group.age_group_id "
    		+ "	and mulref.is_deleted = 'N' "
    		+ "	and mulref.parameter_id = :parameterId "),
    
    @NamedNativeQuery(name = "GET_MULTEXTUAL_REF_RANGE_BY_PARAMETER_WITHOUT_INNER", query = "select "
    		+"	mulref.param_multitextual_range_id  as \"textualRangeId\", "  
    		+"  mulref.remark AS \"remarks\",  "
    		+ "	mulref.multitextaul_range as \"multitextaulRange\" "
    		+ "from "
    		+ "	lab.m_param_multitextual_range mulref "
    		+ " where mulref.is_deleted = 'N' "
    		+ "	and mulref.parameter_id = :parameterId "),
    
    @NamedNativeQuery(name = "GET_MULTEXTUAL_REF_RANGE_BY_MACHINE_ID", query = " select  " + 
    		"	mulref.remark AS \"remarks\",   " + 
    		"	mulref.multitextaul_range as \"multitextaulRange\","+
    		"   mulref.parameter_id as \"parameterId\", " + 
    		"	lbunitmst.unit_name AS \"paramUnit\", "+
    		 " defaultLbunitmst.unit_name AS \"defaultParamUnit\" "+
    		"from  " + 
    		"	lab.m_param_multitextual_range mulref  " + 
    		"inner join lab.t_test_parameter_mppr ttpm  on ttpm.parameter_id  = mulref.parameter_id  " + 
    		"inner join lab.m_age_group_master age_group on  " + 
    		"	mulref.age_group_id = age_group.age_group_id  " + 
    		"	and mulref.is_deleted = 'N'  " + 
    		"	and ttpm.test_id  = :testId " + 
    		"	and ttpm.test_para_status = 'A' " + 
    		"	and mulref.gender_id = :genderId  " + 
    		"	and mulref.machine_id = :machineId  " + 
    		"	and age_group.age_group_status = 'A'  " + 
    		"	and :ageInDays>=age_group.age_from_day  " + 
    		"    and :ageInDays<=age_group.age_to_day "+
    		" LEFT JOIN lab.m_unit_master lbunitmst on lbunitmst.unit_id = mulref.unit_id "+
    		" INNER JOIN lab.m_parameter_master paramMst on paramMst.parameter_id = mulref.parameter_id "+
    		" INNER JOIN lab.m_unit_master defaultLbunitmst on defaultLbunitmst.unit_id = paramMst.unit_id "
    		),
    
    @NamedNativeQuery(name = "GET_ORDER_ID_BY_ORDER_NUMBER", query = "select " + 
    		" tom.order_id as \"orderId\", " + 
    		" tom.order_no as \"orderNo\"  " + 
    		" from public.t_order_master tom   " + 
    		" where tom.order_no  in(:orderNos) and tom.org_unit_id =:unitId"),
    
    @NamedNativeQuery(name = "GET_COMPANY_ID_BY_ORDER_ID", query = "select " + 
    		" (case when tom.encounter_id > 0 then  ( tpsd.payee_id ) else ( tpsd2.payee_id ) end ) as \"payeeId\", "
    		+ "(case when tom.encounter_id > 0 then  ( tpsd.insurance_id ) else ( tpsd2.insurance_id ) end ) as \"insuranceId\"," + 
    		" (case when tom.encounter_id > 0 then " + 
    		" ( tpsd.payment_entitlement_id )  " + 
    		" else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\","
    		+ "tom.visit_type_id as \"visitTypeId\","
    		+ "unitpp.block_report_print as \"blockReportPrint\","
    		+ "tom.encounter_id as \"encounterId\" " + 
    		" from t_order_master tom  " + 
    		" left join opd.t_patient_sponsor_details tpsd on  " + 
    		" tom.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A'  " + 
    		" left join opd.t_patient_sponsor_details tpsd2 on " + 
    		" tom.admission_id = tpsd2.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A'  "
    		+ "inner join t_unit_application_billing_configuration unitpp on unitpp.unit_id = tom.org_unit_id" + 
    		" where tom.order_id = :orderId"),

		@NamedNativeQuery(name = "GET_ALL_POC_SUBDEPTS_RESULT_ENTRY_LIST", query = "  SELECT  "
				+ "subspe_mst.sub_speciality_name as \"subSpecialityName\","
				+ "  visit_type_master.visit_type_code AS \"visitType\", "
				+ " order_details.priority_id as \"priorityId\", "
				+ "    bill.bill_number as \"billNumber\", "
				+ "  lbsampledtls.org_id AS \"orgId\",  "
				+ "  lbsampledtls.org_unit_id AS \"orgUnitId\",  "
				+ "  panel_master.panel_code AS \"panelCode\",  "
				+ "  panel_master.panel_id AS \"panelId\",  "
				+ "  lbsampledtls.gender_id AS \"genderId\",  "
				+ "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\",  "
				+ "  lbsampledtls.sample_recollect_flag AS \"sampleRecollectFlag\",  "
				+ "  lbsampledtls.sample_recollect_against_id AS \"sampleRecollectAgainstId\",  "
				+ "  pati_mst.uhid_number AS uhid,  "
				+ "  sample_mst.sample_id AS \"sampleNo\",  "
				+ "  test_mst.test_desc AS \"testDesc\",  "
				+ "  order_master.admission_id AS \"visitAdmId\",  "
				+ "  order_master.visit_type_id AS \"visitTypeId\",  "
				+ "  lbsampledtls.is_on_hold_parameter AS \"isOnHoldParameter\",  "
				+ "  lbsampledtls.dept_id AS \"deptId\",  "
				+ "  lbsampledtls.test_id AS \"testId\",  "
				+ "  test_mst.test_type AS \"testType\",  "
				+ "  lbsampledtls.sub_dept_id AS \"subDeptId\",  "
				+ "  lbsampledtls.patient_id AS \"patientId\",  "
				+ "  lbsampledtls.order_details_id AS \"orderDetailsId\",  "
				+ "  pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' ||  calculate_age(pati_mst.dob) "
				+ "  || '/' || gen_mst.gender_code AS \"patientDetails\",  "
				+ "  CAST(lbsampledtls.patient_visit_age AS DATE) - CAST(pati_mst.dob AS DATE) AS \"patientAgeDays\",  "
				+ "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' AS \"doctorDetails\",  "
				+ "  lbsampledtls.sample_barcode AS \"sampleBarcode\",  "
				+ "  order_details.created_date AS \"orderDateTime\",  "
				+ "  lbsampledtls.sample_collection_datetime AS \"sampleCollectionDatetime\" , "
				+ "  to_char(order_details.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"orderDateTimeString\",  "
				+ "  to_char(lbsampledtls.sample_collection_datetime, 'DD/MM/YYYY HH24:MI:SS') AS \"sampleCollectionDatetimeString\" , "
				+ "  wardmast.ward_code AS \"wardCode\" , "
				+ "  bedmst.bed_code AS \"bedNumber\",  "
				+ "  test_mst.test_code AS \"testCode\" , "
				+ "  pati_mst.image_path AS \"imagePath\", "
				+ "  samp_mst.sample_desc AS \"sampleType\" ,"
				+ " doct_mst.email as \"doctorMail\", "
				+ " pati_mst.email as \"patientMail\", "
				+ " tltr.lab_test_res_id as \"labTestResId\", "
				+" (case when order_master.encounter_id > 0 then " +
				" ( tpsd.payment_entitlement_id ) " +
				" else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
				+" order_master.order_no as \"orderNo\", "
				+" (case when order_master.encounter_id > 0 then " +
				" ( tpsd.payee_id ) " +
				" else ( tpsd2.payee_id ) end ) as \"payeeId\",  "
				+ "order_details.is_dummy_service as \"isDummyService\", "
				+ "contain_mst.container_name as \"containerName\", "
				+ " (case "
				+ " 	when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) "
				+ " 	when lbsampledtls.lab_sample_dtls_id is null then concat(' ') "
				+ " 	else 'Super Admin' "
				+ " end) as \"userName\" "
				+ " FROM  "
				+ "  lab.t_lab_sample_dtls lbsampledtls  "
				+ "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id  "
				+ "  INNER JOIN public.t_order_master order_master ON order_master.order_id = order_details.order_id  "
				+ " left join  billing.t_billing_details tbd on tbd.order_details_id =order_details.order_details_id and tbd.status ='A' "
				+ " left join billing.m_billing_master bill  on bill.billing_master_id =tbd.billing_master_id and bill.bill_cancelled = 'N' "
				+ "  INNER JOIN lab.t_lab_sample_master sample_mst ON lbsampledtls.lab_sample_id = sample_mst.lab_sample_id  "
				+ "  LEFT JOIN lab.t_lab_test_result tltr ON tltr.lab_test_res_id = (select lab_test_res_id from lab.t_lab_test_result ltlsc where ltlsc.lab_sample_dtls_id = lbsampledtls.lab_sample_dtls_id"
				+ " and ltlsc.status = 'A' order by ltlsc.lab_test_res_id desc limit 1) "
				+ "  INNER JOIN public.m_visit_type_master visit_type_master ON visit_type_master.visit_type_id = order_master.visit_type_id  "
				+ "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id  "
				+ "  INNER JOIN patient.t_patient_registration pati_mst ON lbsampledtls.patient_id = pati_mst.patient_id  "
				+ "  INNER JOIN m_prefix_master pre_mst ON pati_mst.prefix_id = pre_mst.prefix_id  "
				+ "  INNER JOIN m_gender_master gen_mst ON pati_mst.gender_id = gen_mst.gender_id  "
				+ "  INNER JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id  "
				+ "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_mst.sample_id  "
				+ "  LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id  "
				+ "  LEFT JOIN doctor.m_doctor_master doct_mst ON order_master.ord_doctor_id = doct_mst.doctor_id  "
				+ "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = order_master.ord_doc_spl_id   "
				+ " left join adt.t_admission ta on ta.admission_id = order_master.admission_id and ta.status='A' "
				+ " left join opd.t_patient_sponsor_details tpsd "
				+ " on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
				+ " left join opd.t_patient_sponsor_details tpsd2 "
				+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
				+"   LEFT JOIN adt.t_admission_details admission_dtls ON order_master.admission_id = admission_dtls.admission_id "
				+ "  and admission_dtls.status = 'A' "
				+ "  and admission_dtls.nursing_acceptance = 'Y' "
				+ "  LEFT JOIN adt.m_ward_master wardmast ON admission_dtls.ward_id = wardmast.ward_id  "
				+ "  LEFT JOIN adt.m_bed_master bedmst ON admission_dtls.bed_id = bedmst.bed_id  "
				+ "  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id "
				+ " inner join t_lab_configuration tlc on tlc.organisation_id = lbsampledtls.org_id and tlc.unit_id = lbsampledtls.assign_unit "
				+ " left join billing.t_credit_note_detail tcnd on "
				+ " tcnd.order_detail_id = order_details.order_details_id "
				+ " and tcnd.status = 'A' "
				+ " and tcnd.cancel_or_discount = 1 "
				+ "  INNER JOIN lab.m_report_type_master report_mst ON test_mst.report_type_id = report_mst.report_type_id  "
				+ " INNER JOIN service.t_unit_service_master_details service_dtls on "
				+ " 	order_details.service_id = service_dtls.service_master_id "
				+ " 	and service_dtls.status = 'A' "
				+ " 	and service_dtls.is_poc = 'Y' "
				+ " INNER JOIN lab.m_container_master contain_mst on "
				+ " 	contain_mst.container_id = test_mst.container_id "
				+ " LEFT JOIN public.m_user_master um on "
				+ " 	lbsampledtls.sample_collection_by = um.user_id "
				+ " LEFT JOIN public.m_employee_master empmst on "
				+ " 	um.user_id = empmst.user_id "
				+ " WHERE  "
				+ "   lbsampledtls.org_id = :orgId  "
				+ "  AND lbsampledtls.org_unit_id = :orgUnitId  "
				// + "  AND lbsampledtls.dept_id = :deptId  "
				+ " AND lbsampledtls.dept_id  in (:deptId) "
				+ "  AND lbsampledtls.is_outsourced = 'N' "
				+ "  and order_details.ord_cancelled = 'N' "
				+ "  and order_details.status = 'A' "
				+ " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
				+ " and report_mst.report_type_status = 'A' "
				+ " and COALESCE(report_mst.is_template, false) = false "
//    		 + "  AND (case when tlc.consolidated_result_entry = 'N' then lbsampledtls.sub_dept_id not in (tlc.biochemistry_id ,tlc.hematology_id ,tlc.clinical_pathology_id ,tlc.serology_id) else lbsampledtls.sub_dept_id not in (tlc.serology_id)  end) "
		),

		@NamedNativeQuery(name = "GET_ALL_POC_SUBDEPTS_RESULT_ENTRY_LIST_COUNT", query = "  SELECT  "
				+ "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\" "
				+ " FROM  "
				+ "  lab.t_lab_sample_dtls lbsampledtls  "
				+ "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id  "
				+ "  INNER JOIN public.t_order_master order_master ON order_master.order_id = order_details.order_id  "
				+ " left join  billing.t_billing_details tbd on tbd.order_details_id =order_details.order_details_id and tbd.status ='A' "
				+ " left join billing.m_billing_master bill  on bill.billing_master_id =tbd.billing_master_id and bill.bill_cancelled = 'N' "
				+ "  INNER JOIN lab.t_lab_sample_master sample_mst ON lbsampledtls.lab_sample_id = sample_mst.lab_sample_id  "
				+ "  LEFT JOIN lab.t_lab_test_result tltr ON tltr.lab_test_res_id = (select lab_test_res_id from lab.t_lab_test_result ltlsc where ltlsc.lab_sample_dtls_id = lbsampledtls.lab_sample_dtls_id"
				+ " and ltlsc.status = 'A' order by ltlsc.lab_test_res_id desc limit 1) "
				+ "  INNER JOIN public.m_visit_type_master visit_type_master ON visit_type_master.visit_type_id = order_master.visit_type_id  "
				+ "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id  "
				+ "  INNER JOIN patient.t_patient_registration pati_mst ON lbsampledtls.patient_id = pati_mst.patient_id  "
				+ "  INNER JOIN m_prefix_master pre_mst ON pati_mst.prefix_id = pre_mst.prefix_id  "
				+ "  INNER JOIN m_gender_master gen_mst ON pati_mst.gender_id = gen_mst.gender_id  "
				+ "  INNER JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id  "
				+ "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_mst.sample_id  "
				+ "  LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id  "
				+ "  LEFT JOIN doctor.m_doctor_master doct_mst ON order_master.ord_doctor_id = doct_mst.doctor_id  "
				+ "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = order_master.ord_doc_spl_id   "
				+ " left join adt.t_admission ta on ta.admission_id = order_master.admission_id and ta.status='A' "
				+ " left join opd.t_patient_sponsor_details tpsd "
				+ " on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
				+ " left join opd.t_patient_sponsor_details tpsd2 "
				+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
				+"   LEFT JOIN adt.t_admission_details admission_dtls ON order_master.admission_id = admission_dtls.admission_id "
				+ "  and admission_dtls.status = 'A' "
				+ "  and admission_dtls.nursing_acceptance = 'Y' "
				+ "  LEFT JOIN adt.m_ward_master wardmast ON admission_dtls.ward_id = wardmast.ward_id  "
				+ "  LEFT JOIN adt.m_bed_master bedmst ON admission_dtls.bed_id = bedmst.bed_id  "
				+ "  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id "
				+ " inner join t_lab_configuration tlc on tlc.organisation_id = lbsampledtls.org_id and tlc.unit_id = lbsampledtls.assign_unit "
				+ " left join billing.t_credit_note_detail tcnd on "
				+ " tcnd.order_detail_id = order_details.order_details_id "
				+ " and tcnd.status = 'A' "
				+ " and tcnd.cancel_or_discount = 1 "
				+ "  INNER JOIN lab.m_report_type_master report_mst ON test_mst.report_type_id = report_mst.report_type_id  "
				+ " INNER JOIN service.t_unit_service_master_details service_dtls on "
				+ " 	order_details.service_id = service_dtls.service_master_id "
				+ " 	and service_dtls.status = 'A' "
				+ " 	and service_dtls.is_poc = 'Y' "
				+ " INNER JOIN lab.m_container_master contain_mst on "
				+ " 	contain_mst.container_id = test_mst.container_id "
				+ "WHERE  "
				+ "   lbsampledtls.org_id = :orgId  "
				+ "  AND lbsampledtls.org_unit_id = :orgUnitId  "
				// + "  AND lbsampledtls.dept_id = :deptId  "
				+ " AND lbsampledtls.dept_id  in (:deptId) "
				+ "  AND lbsampledtls.is_outsourced = 'N' "
				+ "  and order_details.ord_cancelled = 'N' "
				+ "  and order_details.status = 'A' "
				+ " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
				+ " and report_mst.report_type_status = 'A' "
				+ " and COALESCE(report_mst.is_template, false) = false "
				//  + "  AND (case when tlc.consolidated_result_entry = 'N' then lbsampledtls.sub_dept_id not in (biochemistry_id ,hematology_id ,clinical_pathology_id ,serology_id) else lbsampledtls.sub_dept_id not in (serology_id)  end) "
		),

		@NamedNativeQuery(name = "GET_ALL_POC_SERO_RESULT_ENTRY_LIST", query = "  SELECT  "
				+ "subspe_mst.sub_speciality_name as \"subSpecialityName\","
				+ "    bill_mst.bill_number as \"billNumber\", "
				+ " order_details.priority_id as \"priorityId\", "
				+ "  visit_type_master.visit_type_code AS \"visitType\",  "
				+ "  lbsampledtls.org_id AS \"orgId\",  "
				+ "  lbsampledtls.org_unit_id AS \"orgUnitId\",  "
				+ "  panel_master.panel_code AS \"panelCode\",  "
				+ "  panel_master.panel_id AS \"panelId\",  "
				+ "  lbsampledtls.gender_id AS \"genderId\",  "
				+ "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\",  "
				+ "  lbsampledtls.sample_recollect_flag AS \"sampleRecollectFlag\",  "
				+ "  lbsampledtls.sample_recollect_against_id AS \"sampleRecollectAgainstId\",  "
				+ "  pati_mst.uhid_number AS uhid,  "
				+ "  sample_mst.sample_id AS \"sampleNo\",  "
				+ "  test_mst.test_desc AS \"testDesc\",  "
				+ "  order_master.admission_id AS \"visitAdmId\",  "
				+ "  order_master.visit_type_id AS \"visitTypeId\",  "
				+ "  lbsampledtls.dept_id AS \"deptId\",  "
				+ "  lbsampledtls.test_id AS \"testId\",  "
				+ "  test_mst.test_type AS \"testType\",  "
				+ "  lbsampledtls.sub_dept_id AS \"subDeptId\",  "
				+ "  lbsampledtls.is_on_hold_parameter AS \"isOnHoldParameter\",  "
				+ "  lbsampledtls.patient_id AS \"patientId\",  "
				+ "  lbsampledtls.order_details_id AS \"orderDetailsId\",  "
				+ " pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || "
				+ " calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code AS \"patientDetails\",  "
				+ "  CAST(lbsampledtls.patient_visit_age AS DATE) - CAST(pati_mst.dob AS DATE) AS \"patientAgeDays\",  "
				+ "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' AS \"doctorDetails\",  "
				+ "  lbsampledtls.sample_barcode AS \"sampleBarcode\",  "
				+ "  order_details.created_date AS \"orderDateTime\",  "
				+ "  lbsampledtls.sample_collection_datetime AS \"sampleCollectionDatetime\" , "
				+ "  to_char(order_details.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"orderDateTimeString\",  "
				+ "  to_char(lbsampledtls.sample_collection_datetime, 'DD/MM/YYYY HH24:MI:SS') AS \"sampleCollectionDatetimeString\" , "
				+ "  wardmast.ward_code AS \"wardCode\" , "
				+ "  bedmst.bed_code AS \"bedNumber\",  "
				+ "  test_mst.test_code AS \"testCode\" , "
				+ "  pati_mst.image_path AS \"imagePath\", "
				+ "  samp_mst.sample_desc AS \"sampleType\" ,"
				+ " doct_mst.email as \"doctorMail\", "
				+ " pati_mst.email as \"patientMail\", "
				+" (case when order_master.encounter_id > 0 then " +
				" ( tpsd.payment_entitlement_id ) " +
				" else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
				+" order_master.order_no as \"orderNo\", "
				+" (case when order_master.encounter_id > 0 then " +
				" ( tpsd.payee_id ) " +
				" else ( tpsd2.payee_id ) end ) as \"payeeId\","
				+ "order_details.is_dummy_service as \"isDummyService\", "
				+ "contain_mst.container_name as \"containerName\", "
				+ " (case "
				+ " 	when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) "
				+ " 	when lbsampledtls.lab_sample_dtls_id is null then concat(' ') "
				+ " 	else 'Super Admin' "
				+ " end) as \"userName\" "
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
				+ "  INNER JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id  "
				+ "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_mst.sample_id  "
				+ "  LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id  "
				+ "  LEFT JOIN doctor.m_doctor_master doct_mst ON order_master.ord_doctor_id = doct_mst.doctor_id  "
				+ "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = order_master.ord_doc_spl_id   "
				+ " left join adt.t_admission ta on ta.admission_id = order_master.admission_id and ta.status='A' "
				+ " left join opd.t_patient_sponsor_details tpsd "
				+ " on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
				+ " left join opd.t_patient_sponsor_details tpsd2 "
				+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
				+"   LEFT JOIN adt.t_admission_details admission_dtls ON order_master.admission_id = admission_dtls.admission_id "
				+ "  and admission_dtls.status = 'A' "
				+ "  and admission_dtls.nursing_acceptance = 'Y' "
				+ "  LEFT JOIN adt.m_ward_master wardmast ON admission_dtls.ward_id = wardmast.ward_id  "
				+ "  LEFT JOIN adt.m_bed_master bedmst ON admission_dtls.bed_id = bedmst.bed_id  "
				+ "  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id  "
				+ " inner join t_lab_configuration tlc on tlc.organisation_id = lbsampledtls.org_id and tlc.unit_id = lbsampledtls.assign_unit "
				+ " left join billing.t_billing_details bill_dtls on "
				+ " 	order_details.order_details_id = bill_dtls.order_details_id "
				+ " left join billing.m_billing_master bill_mst on "
				+ " 	bill_mst.billing_master_id = bill_dtls.billing_master_id "
				+ " 	and bill_mst.bill_cancelled = 'N' "
//            + " left join "
//    		+ " (	select count(sample_dtls_id) retestCount  , sample_dtls_id from lab.t_sample_audit_trail sample_audit_trail "
//    		+ "	where test_status_id = 1 group by sample_dtls_id ) as retest on retest.sample_dtls_id = lbsampledtls.lab_sample_dtls_id "
				+ " left join billing.t_credit_note_detail tcnd on "
				+ " tcnd.order_detail_id = order_details.order_details_id "
				+ " and tcnd.status = 'A' "
				+ " and tcnd.cancel_or_discount = 1 "
				+ " INNER JOIN service.t_unit_service_master_details service_dtls on "
				+ " 	order_details.service_id = service_dtls.service_master_id "
				+ " 	and service_dtls.status = 'A' "
				+ " 	and service_dtls.is_poc = 'Y' "
				+ " INNER JOIN lab.m_container_master contain_mst on "
				+ " 	contain_mst.container_id = test_mst.container_id "
				+ " LEFT JOIN public.m_user_master um on "
				+ " 	lbsampledtls.sample_collection_by = um.user_id "
				+ " LEFT JOIN public.m_employee_master empmst on "
				+ " 	um.user_id = empmst.user_id "
				+ "WHERE  "
				+ "   lbsampledtls.org_id = :orgId  "
				+ "  AND lbsampledtls.org_unit_id = :orgUnitId  "
				// + "  AND lbsampledtls.dept_id = :deptId  "
				+ " AND lbsampledtls.dept_id  in (:deptId) "
//           + " AND test_mst.dept_id IN (:deptId) "

				+ "  AND lbsampledtls.is_outsourced = 'N' "
				+ " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
				+ "  AND (case when tlc.consolidated_result_entry = 'N' then lbsampledtls.sub_dept_id in (0) else (case " +
				"			when tlc.serology_id is not null and tlc.serology_id <> 0  then lbsampledtls.sub_dept_id in (tlc.serology_id) " +
				"				else lbsampledtls.sub_dept_id in (0) " +
				"			end)  end) "
				+ "  and order_details.ord_cancelled = 'N' "
				+ "  and order_details.status = 'A' "
		),

		@NamedNativeQuery(name = "GET_ALL_POC_SERO_RESULT_ENTRY_LIST_COUNT", query = "  SELECT  "
				+ "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\"  "
				+ " FROM  "
				+ "  lab.t_lab_sample_dtls lbsampledtls  "
				+ "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id  "
				+ "  INNER JOIN public.t_order_master order_master ON order_master.order_id = order_details.order_id  "
				+ "  INNER JOIN lab.t_lab_sample_master sample_mst ON lbsampledtls.lab_sample_id = sample_mst.lab_sample_id  "
				+ "  INNER JOIN public.m_visit_type_master visit_type_master ON visit_type_master.visit_type_id = order_master.visit_type_id  "
				+ "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id  "
				+ "  INNER JOIN patient.t_patient_registration pati_mst ON lbsampledtls.patient_id = pati_mst.patient_id  "
				+ " inner join t_lab_configuration tlc on tlc.organisation_id = lbsampledtls.org_id and tlc.unit_id = lbsampledtls.assign_unit "
				+ " INNER JOIN service.t_unit_service_master_details service_dtls on "
				+ " 	order_details.service_id = service_dtls.service_master_id "
				+ " 	and service_dtls.status = 'A' "
				+ " 	and service_dtls.is_poc = 'Y' "
				+ "WHERE  "
				//  + "  lbsampledtls.sample_status_id IN(:sampleStatusId) "
				+ "   lbsampledtls.org_id = :orgId  "
				+ "  AND lbsampledtls.org_unit_id = :orgUnitId  "
				//  + "  AND lbsampledtls.dept_id = :deptId  "
				+ " AND lbsampledtls.dept_id  in (:deptId) "


//            + " AND test_mst.dept_id IN (:deptId) "
				+ "  AND lbsampledtls.is_outsourced = 'N' "
				+ "  AND (case when tlc.consolidated_result_entry = 'N' then lbsampledtls.sub_dept_id in (0) else (case " +
				"			when tlc.serology_id is not null and tlc.serology_id <> 0  then lbsampledtls.sub_dept_id in (tlc.serology_id) " +
				"				else lbsampledtls.sub_dept_id in (0) " +
				"			end)  end) "

		),
    
    
})


@Entity
@Table(name = "t_lab_test_result", schema = "lab")
@SequenceGenerator(name = "m_seq_result_entry", sequenceName = "lab.m_seq_result_entry",
    allocationSize = 1)
public class LabResultMaster {
  @Id
  @Column(name = "lab_test_res_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_seq_result_entry")
  private int labTestResId;


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

  @Column(name = "patient_id")
  private Integer patientId;

  @Column(name = "visit_type_id")
  private Integer visitTypeId;

  @Column(name = "admission_id")
  private Integer visitAdmId;

  @Column(name = "order_details_id")
  private Integer orderDetailsId;

  @Column(name = "sample_no")
  private String sampleNo;

  @Column(name = "lab_sample_dtls_id")
  private Integer labSampleDtlsId;

  @Column(name = "result_enter_datetime")
  @Convert(converter = LocalTimeConverter.class)
  private Long resultEnterDatetime;

  @Column(name = "result_enter_by")
  private Integer resultEnterBy;

  @Column(name = "result_validated_datetime")
  @Convert(converter = LocalTimeConverter.class)
  private Long resultValidatedDatetime;

  @Column(name = "result_validated_by")
  private Integer resultValidatedBy;

  @Column(name = "result_authorised_datetime")
  @Convert(converter = LocalTimeConverter.class)
  private Long resultAuthorisedDatetime;

  @Column(name = "result_authorised_by")
  private Integer resultAuthorisedBy;

  @Column(name = "result_authorised_flag")
  private Character resultAuthorisedFlag;

  @Column(name = "result_handover_datetime")
  @Convert(converter = LocalTimeConverter.class)
  private Long resultHandoverDatetime;

  @Column(name = "result_handover_by")
  private Integer resultHandoverBy;

  @Column(name = "suggetion_notes")
  private String suggetionNotes;

  @Column(name = "foot_notes")
  private String footsNotes;

  @Column(name = "created_by")
  private Integer createdBy;

  @Column(name = "createdDate")
  @Convert(converter = LocalTimeConverter.class)
  private Long createdDate;

  @Column(name = "updated_by")
  private Integer updatedBy;

  @Column(name = "updated_date")
  @Convert(converter = LocalTimeConverter.class)
  private Long updatedDate;

  @Column(name = "report_type")
  private Integer reportType;

  @Column(name = "result_level")
  private Integer resultLevel;

  @Column(name = "sample_status_id")
  private Integer sampleStatusId;
  
  @Column(name = "machine_id")
  private Integer machineId;
  
  @Column(name="status")
  private Character status;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "labSampleMaster")
  private List<LabResultDetailsMaster> listLabSampleResultDetailsMaster;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lab_sample_dtls_id", insertable = false, nullable = false, updatable = false)
  private LabSampleDetailsMaster labSampleDetailsMaster;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sample_status_id", insertable = false, nullable = false, updatable = false)
  private SampleStatusMaster sampleStatusMaster;
  
  @Column(name = "manual_result_enter_time")
  @Convert(converter = LocalTimeConverter.class)
  private Long manualResultEnterTime;
  
  @Column(name = "manual_validation_time")
  @Convert(converter = LocalTimeConverter.class)
  private Long manualValidationTime;
  
  @Column(name = "manual_release_time")
  @Convert(converter = LocalTimeConverter.class)
  private Long manualReleaseTime;


  public int getLabTestResId() {
    return labTestResId;
  }

  public void setLabTestResId(int labTestResId) {
    this.labTestResId = labTestResId;
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

  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  public Integer getVisitTypeId() {
    return visitTypeId;
  }

  public void setVisitTypeId(Integer visitTypeId) {
    this.visitTypeId = visitTypeId;
  }

  public Integer getVisitAdmId() {
    return visitAdmId;
  }

  public void setVisitAdmId(Integer visitAdmId) {
    this.visitAdmId = visitAdmId;
  }

  public Integer getOrderDetailsId() {
    return orderDetailsId;
  }

  public void setOrderDetailsId(Integer orderDetailsId) {
    this.orderDetailsId = orderDetailsId;
  }

  public String getSampleNo() {
    return sampleNo;
  }

  public void setSampleNo(String sampleNo) {
    this.sampleNo = sampleNo;
  }

  public Integer getLabSampleDtlsId() {
    return labSampleDtlsId;
  }

  public void setLabSampleDtlsId(Integer labSampleDtlsId) {
    this.labSampleDtlsId = labSampleDtlsId;
  }

  public Long getResultEnterDatetime() {
    return resultEnterDatetime;
  }

  public void setResultEnterDatetime(Long resultEnterDatetime) {
    this.resultEnterDatetime = resultEnterDatetime;
  }

  public Integer getResultEnterBy() {
    return resultEnterBy;
  }

  public void setResultEnterBy(Integer resultEnterBy) {
    this.resultEnterBy = resultEnterBy;
  }

  public Long getResultValidatedDatetime() {
    return resultValidatedDatetime;
  }

  public void setResultValidatedDatetime(Long resultValidatedDatetime) {
    this.resultValidatedDatetime = resultValidatedDatetime;
  }

  public Integer getResultValidatedBy() {
    return resultValidatedBy;
  }

  public void setResultValidatedBy(Integer resultValidatedBy) {
    this.resultValidatedBy = resultValidatedBy;
  }

  public Long getResultAuthorisedDatetime() {
    return resultAuthorisedDatetime;
  }

  public void setResultAuthorisedDatetime(Long resultAuthorisedDatetime) {
    this.resultAuthorisedDatetime = resultAuthorisedDatetime;
  }

  public Integer getResultAuthorisedBy() {
    return resultAuthorisedBy;
  }

  public void setResultAuthorisedBy(Integer resultAuthorisedBy) {
    this.resultAuthorisedBy = resultAuthorisedBy;
  }

  public Character getResultAuthorisedFlag() {
    return resultAuthorisedFlag;
  }

  public void setResultAuthorisedFlag(Character resultAuthorisedFlag) {
    this.resultAuthorisedFlag = resultAuthorisedFlag;
  }

  public Long getResultHandoverDatetime() {
    return resultHandoverDatetime;
  }

  public void setResultHandoverDatetime(Long resultHandoverDatetime) {
    this.resultHandoverDatetime = resultHandoverDatetime;
  }

  public Integer getResultHandoverBy() {
    return resultHandoverBy;
  }

  public void setResultHandoverBy(Integer resultHandoverBy) {
    this.resultHandoverBy = resultHandoverBy;
  }

  public String getSuggetionNotes() {
    return suggetionNotes;
  }

  public void setSuggetionNotes(String suggetionNotes) {
    this.suggetionNotes = suggetionNotes;
  }

  public String getFootsNotes() {
    return footsNotes;
  }

  public void setFootsNotes(String footsNotes) {
    this.footsNotes = footsNotes;
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

  public List<LabResultDetailsMaster> getListLabSampleResultDetailsMaster() {
    return listLabSampleResultDetailsMaster;
  }

  public void setListLabSampleResultDetailsMaster(
      List<LabResultDetailsMaster> listLabSampleResultDetailsMaster) {
    this.listLabSampleResultDetailsMaster = listLabSampleResultDetailsMaster;
  }

  public LabSampleDetailsMaster getLabSampleDetailsMaster() {
    return labSampleDetailsMaster;
  }

  public void setLabSampleDetailsMaster(LabSampleDetailsMaster labSampleDetailsMaster) {
    this.labSampleDetailsMaster = labSampleDetailsMaster;
  }

  public Integer getReportType() {
    return reportType;
  }

  public void setReportType(Integer reportType) {
    this.reportType = reportType;
  }

  public Integer getSubDeptId() {
    return subDeptId;
  }

  public void setSubDeptId(Integer subDeptId) {
    this.subDeptId = subDeptId;
  }

  public Integer getResultLevel() {
    return resultLevel;
  }

  public void setResultLevel(Integer resultLevel) {
    this.resultLevel = resultLevel;
  }

  public Integer getSampleStatusId() {
    return sampleStatusId;
  }

  public void setSampleStatusId(Integer sampleStatusId) {
    this.sampleStatusId = sampleStatusId;
  }

  public SampleStatusMaster getSampleStatusMaster() {
    return sampleStatusMaster;
  }

  public void setSampleStatusMaster(SampleStatusMaster sampleStatusMaster) {
    this.sampleStatusMaster = sampleStatusMaster;
  }

public Integer getMachineId() {
	return machineId;
}

public void setMachineId(Integer machineId) {
	this.machineId = machineId;
}

public Character getStatus() {
	return status;
}

public void setStatus(Character status) {
	this.status = status;
}

public Long getManualResultEnterTime() {
	return manualResultEnterTime;
}

public void setManualResultEnterTime(Long manualResultEnterTime) {
	this.manualResultEnterTime = manualResultEnterTime;
}

public Long getManualValidationTime() {
	return manualValidationTime;
}

public void setManualValidationTime(Long manualValidationTime) {
	this.manualValidationTime = manualValidationTime;
}

public Long getManualReleaseTime() {
	return manualReleaseTime;
}

public void setManualReleaseTime(Long manualReleaseTime) {
	this.manualReleaseTime = manualReleaseTime;
}


}
