package com.param.entity.lis.micro;

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
import com.param.entity.lis.common.LocalTimeConverter;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;

@NamedQueries({@NamedQuery(name = "UPDATE_LAB_SAMPLE_DETAILS",
    query = " UPDATE " + "  LabSampleDetailsMaster labSampleDetailsMaster " + "SET "
        + "  labSampleDetailsMaster.sampleStatusId =:sampleStatausId, " // sampleStatusId
        + "  labSampleDetailsMaster.assignUnit =:assignUnit, "
         + "  labSampleDetailsMaster.performedUnit =:performedUnit, "// sampleStatusId
        + "  labSampleDetailsMaster.updatedBy =:updatedBy, "
        + "  labSampleDetailsMaster.updatedDate =:updatedDate " + "WHERE "
        + "  labSampleDetailsMaster.labSampleDtlsId =:labSampleDtlsId "),})

@NamedNativeQueries({

    /** Sample Pending Acceptance */
    @NamedNativeQuery(name = "MICROBIOLOGY_WORKLIST_BY_STATUS", query = " SELECT   "
        + "  visit_type_master.visit_type_code AS \"visitType\",   "
        + "  subspe_mst.sub_speciality_name AS \"deptName\",  "
        + "  panel_master.panel_code AS \"panelCode\",   "
        + "  panel_master.panel_id AS \"panelId\",   "
        + "  lbsampledtls.gender_id AS \"genderId\",   "
        + "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\",   "
        + "  test_mst.sample_id AS \"sampleId\", "
        + "  lbsampledtls.sample_recollect_flag AS \"sampleRecollectFlag\",   "
        + "  lbsampledtls.sample_recollect_against_id AS \"sampleRecollectAgainstId\",   "
        + "  pati_mst.uhid_number AS uhid,   "
    	+ "  sample_mst.sample_no AS \"labSampleNo\",  "
        + "  test_mst.test_desc AS \"testDesc\",   "
        + "  order_master.admission_id AS \"visitAdmId\",   "
        + "  order_master.visit_type_id AS \"visitTypeId\",   "
        + "  lbsampledtls.dept_id AS \"deptId\",   " 
        + "  lbsampledtls.test_id AS \"testId\",   "
        + "  test_mst.test_type AS \"testType\",   "
        + "  lbsampledtls.sub_dept_id AS \"subDeptId\",   "
        + "  lbsampledtls.patient_id AS \"patientId\",   "
        + "  lbsampledtls.order_details_id AS \"orderDetailsId\",   "
        + "  pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' '|| pati_mst.last_name || ' ' || calculate_age(pati_mst.dob) "
        + " || '/' || gen_mst.gender_code AS \"patientDetails\",   "
        + "  CAST(lbsampledtls.patient_visit_age AS DATE) - CAST(pati_mst.dob AS DATE) AS \"patientAgeDays\",   "
        + "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' AS \"doctorDetails\",   "
        + "  lbsampledtls.sample_barcode AS \"sampleBarcode\",   "
        + "  order_details.created_date AS \"orderDateTime\",   "
        + "  lbsampledtls.sample_collection_datetime AS \"sampleCollectionDatetime\" ,  "
        + "  wardmast.ward_code AS \"wardCode\" ,  "
        + "  bedmst.bed_code AS \"bedNumber\",   "
        + "  test_mst.test_code AS \"testCode\" ,  " 
        + "  samp_mst.sample_desc AS \"sampleType\",   "
        +" (case when order_master.encounter_id > 0 then " +
        " ( tpsd.payment_entitlement_id ) " +
        " else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\" ,"
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
        + "  INNER JOIN lab.m_test_master test_master ON test_master.test_id = lbsampledtls.test_id   "
        + "  INNER JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id   "
        + "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_master.sample_id   "
        + "  INNER JOIN public.t_lab_configuration lab_config ON lab_config.unit_id = lbsampledtls.org_unit_id   "
        + "  LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id   "
        + "  LEFT JOIN doctor.m_doctor_master doct_mst ON order_master.ord_doctor_id = doct_mst.doctor_id   "
        + "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = order_master.ord_doc_spl_id   "
        + " left join adt.t_admission ta on ta.admission_id = order_master.admission_id and ta.status='A' "
		+ " left join opd.t_patient_sponsor_details tpsd "
		+ " on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
		+ " left join opd.t_patient_sponsor_details tpsd2 "
		+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
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
      //  + " (case when (lab_config.serology_id = lbsampledtls.sub_dept_id) then lbsampledtls.sample_status_id=2 "
       // + " else lbsampledtls.sample_status_id IN (:sampleStatusIds) end) "
        + " lbsampledtls.sample_status_id IN (:sampleStatusIds) "
        + "  AND lbsampledtls.org_id = :orgId   " 
        + "  AND lbsampledtls.org_unit_id = :orgUnitId   "
        + "  AND lbsampledtls.dept_id = :deptId   "
        + "  AND lbsampledtls.sub_dept_id in (lab_config.microbiology_id,lab_config.serology_id)   " 
        + "  and order_details.ord_cancelled = 'N' "
		+ "  and order_details.status = 'A' "
		+ " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
        +"   AND lbsampledtls.is_outsourced = 'N' "),

    /** Sample Pending Acceptance Count */
    @NamedNativeQuery(name = "MICROBIOLOGY_WORKLIST_COUNT_BY_STATUS",
        query = "  SELECT  " 
            + "  COUNT(*)  " 
        	+ "FROM  " 
            + "  lab.t_lab_sample_dtls lbsampledtls  "
        	+ "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id   "
        	+ "  INNER JOIN public.t_order_master order_master ON order_master.order_id = lbsampledtls.order_id   "
            + "  INNER JOIN public.t_lab_configuration lab_config ON lab_config.unit_id = lbsampledtls.org_unit_id   "
            + " WHERE  " 
        	+ "  lbsampledtls.sample_status_id IN (:sampleStatusIds)  "
            + "  AND lbsampledtls.org_id = :orgId  "
            + "  AND lbsampledtls.org_unit_id = :orgUnitId  "
            + "  AND lbsampledtls.dept_id = :deptId  "
            + "  AND lbsampledtls.sub_dept_id in (lab_config.microbiology_id,lab_config.serology_id)   " 
            +"   AND lbsampledtls.is_outsourced = 'N' "),
    
    
    @NamedNativeQuery(name = "MICROBIOLOGY_WORKLIST_DEPT", query = " SELECT   "
            + "  visit_type_master.visit_type_code AS \"visitType\",   "
            + "  subspe_mst.sub_speciality_name AS \"deptName\",  "
            + "  panel_master.panel_code AS \"panelCode\",   "
            + "  panel_master.panel_id AS \"panelId\",   "
            + "  lbsampledtls.gender_id AS \"genderId\",   "
            + "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\",   "
            + "  test_mst.sample_id AS \"sampleId\", "
            + "  lbsampledtls.sample_recollect_flag AS \"sampleRecollectFlag\",   "
            + "  lbsampledtls.sample_recollect_against_id AS \"sampleRecollectAgainstId\",   "
            + "  pati_mst.uhid_number AS uhid,   "
        	+ "  sample_mst.sample_no AS \"labSampleNo\",  "
            + "  test_mst.test_desc AS \"testDesc\",   "
            + "  order_master.admission_id AS \"visitAdmId\",   "
            + "  order_master.visit_type_id AS \"visitTypeId\",   "
            + "  lbsampledtls.dept_id AS \"deptId\",   " 
            + "  lbsampledtls.test_id AS \"testId\",   "
            + "  test_mst.test_type AS \"testType\",   "
            + "  lbsampledtls.sub_dept_id AS \"subDeptId\",   "
            + "  lbsampledtls.patient_id AS \"patientId\",   "
            + "  lbsampledtls.order_details_id AS \"orderDetailsId\",   "
            + "  pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) ||' '|| pati_mst.last_name || ' ' || calculate_age(pati_mst.dob)"
            + " || '/' || gen_mst.gender_code AS \"patientDetails\",   "
            + "  CAST(lbsampledtls.patient_visit_age AS DATE) - CAST(pati_mst.dob AS DATE) AS \"patientAgeDays\",   "
            + "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' AS \"doctorDetails\",   "
            + "  lbsampledtls.sample_barcode AS \"sampleBarcode\",   "
            + "  order_details.created_date AS \"orderDateTime\",   "
            + "  lbsampledtls.sample_collection_datetime AS \"sampleCollectionDatetime\" ,  "
            + "  wardmast.ward_code AS \"wardCode\" ,  "
            + "  bedmst.bed_code AS \"bedNumber\",   "
            + "  test_mst.test_code AS \"testCode\" ,  " 
            + "  samp_mst.sample_desc AS \"sampleType\",   "
            +" (case when order_master.encounter_id > 0 then " +
            " ( tpsd.payment_entitlement_id ) " +
            " else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\" ,"
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
            + "  INNER JOIN lab.m_test_master test_master ON test_master.test_id = lbsampledtls.test_id   "
            + "  INNER JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id   "
            + "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_master.sample_id   "
            + "  INNER JOIN public.t_lab_configuration lab_config ON lab_config.unit_id = lbsampledtls.org_unit_id   "
            + "  LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id   "
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
            + "  LEFT JOIN adt.m_ward_master wardmast ON admission_dtls.ward_id = wardmast.ward_id   "
            + "  LEFT JOIN adt.m_bed_master bedmst ON admission_dtls.bed_id = bedmst.bed_id   "
            + "  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id   "
            + " left join billing.t_credit_note_detail tcnd on "
            + " tcnd.order_detail_id = order_details.order_details_id "
            + " and tcnd.status = 'A' "
            + " and tcnd.cancel_or_discount = 1 "
            + "WHERE   " 
            + "  lbsampledtls.sample_status_id IN (:sampleStatusIds)    "
            + "  AND lbsampledtls.org_id = :orgId   " 
            + "  AND lbsampledtls.org_unit_id = :orgUnitId   "
            + "  AND lbsampledtls.dept_id = :deptId   "
            + "  AND lbsampledtls.sub_dept_id in (lab_config.microbiology_id)   " 
            + "  and order_details.ord_cancelled = 'N' "
            + "  and order_details.status = 'A' "
            + "  and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
            + "  AND lbsampledtls.is_outsourced = 'N' " ),

        /** Sample Pending Acceptance Count */
        @NamedNativeQuery(name = "MICROBIOLOGY_WORKLIST_DEPT_COUNT",
            query = "  SELECT  " 
                + "  COUNT(*)  " 
            	+ "FROM  " 
                + "  lab.t_lab_sample_dtls lbsampledtls  "
            	+ "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id   "
            	+ "  INNER JOIN public.t_order_master order_master ON order_master.order_id = lbsampledtls.order_id   "
                + "  INNER JOIN public.t_lab_configuration lab_config ON lab_config.unit_id = lbsampledtls.org_unit_id   "
                + " WHERE  " 
            	+ "  lbsampledtls.sample_status_id IN (:sampleStatusIds)  "
                + "  AND lbsampledtls.org_id = :orgId  "
                + "  AND lbsampledtls.org_unit_id = :orgUnitId  "
                + "  AND lbsampledtls.dept_id = :deptId  "
                + "  AND lbsampledtls.sub_dept_id in (lab_config.microbiology_id)   " 
                +"   AND lbsampledtls.is_outsourced = 'N' "),


    @NamedNativeQuery(name = "INCUBATION_OBJSERVATION_WORKLIST", query = " SELECT   "
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
        + "  sample_mst.sample_no AS \"labSampleNo\",  "
        + "  test_mst.test_desc AS \"testDesc\",   "
        + "  order_master.admission_id AS \"visitAdmId\",   "
        + "  order_master.visit_type_id AS \"visitTypeId\",   "
        + "  lbsampledtls.dept_id AS \"deptId\",   " 
        + "  lbsampledtls.test_id AS \"testId\",   "
        + "  test_mst.test_type AS \"testType\",   "
        + "  lbsampledtls.sub_dept_id AS \"subDeptId\",   "
        + "  lbsampledtls.patient_id AS \"patientId\",   "
        + "  lbsampledtls.order_details_id AS \"orderDetailsId\",   "
        + "  pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) ||' ' || pati_mst.last_name || ' ' || calculate_age(pati_mst.dob)"
        + " || '/' || gen_mst.gender_code AS \"patientDetails\",   "
        + "  CAST(lbsampledtls.patient_visit_age AS DATE) - CAST(pati_mst.dob AS DATE) AS \"patientAgeDays\",   "
        + "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' AS \"doctorDetails\",   "
        + "  lbsampledtls.sample_barcode AS \"sampleBarcode\",   "
        + "  order_details.created_date AS \"orderDateTime\",   "
        + "  lbsampledtls.sample_collection_datetime AS \"sampleCollectionDatetime\" ,  "
        + "  wardmast.ward_code AS \"wardCode\" ,  " + "  bedmst.bed_code AS \"bedNumber\",   "
        + "  test_mst.test_code AS \"testCode\" ,  "
        + "  samp_mst.sample_desc AS \"sampleType\" ,  "
        + "  micro_incubation.t_incubation_id AS \"tIncubationId\",   " 
        +" (case when order_master.encounter_id > 0 then " +
        " ( tpsd.payment_entitlement_id ) " +
        " else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
        + "    bill_mst.bill_number as \"billNumber\", " 
        + " stsMstr.status_name  as \"statusName\" ,"
        + "order_details.is_dummy_service as \"isDummyService\" ,"
        + "test_mst.culture_test as \"cultureTest\" ,"
        + "doct_mst.doctor_id  \"doctorId\"  "
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
        + "  LEFT JOIN doctor.m_doctor_master doct_mst ON  doct_mst.doctor_id = lbsampledtls.doctor_id   "
        + "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = order_master.ord_doc_spl_id   "
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
        + "  LEFT JOIN lab.t_micro_incubation micro_incubation ON lbsampledtls.lab_sample_dtls_id = micro_incubation.lab_sample_dtls_id   "
        + " left join billing.t_credit_note_detail tcnd on "
        + " tcnd.order_detail_id = order_details.order_details_id "
        + " and tcnd.status = 'A' "
        + " and tcnd.cancel_or_discount = 1 "
        + "WHERE   " 
        + "  lbsampledtls.sample_status_id IN (:sampleStatusIds)    "
        + "  AND lbsampledtls.org_id = :orgId   " 
        + "  AND lbsampledtls.org_unit_id = :orgUnitId   "
        + "  AND lbsampledtls.dept_id = :deptId   "
        + "  AND lbsampledtls.sub_dept_id = :subDeptId   "
        + "  and order_details.ord_cancelled = 'N' "
        + " and  (case when order_master.visit_type_id = 1 then bill_dtls.status = 'A' else order_details.status = 'A' end  )"
		+ "  and order_details.status = 'A' "
		+ " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "
	    + "  AND lbsampledtls.is_outsourced = 'N' " ),
    
    @NamedNativeQuery(name = "MICROBIOLOGY_TEMPLATE_WORKBEANCH", query = " SELECT   "
            + "  visit_type_master.visit_type_code AS \"visitType\",   "
            + "  order_details.priority_id AS \"priorityId\" ,"
            + "  subspe_mst.sub_speciality_name AS \"deptName\",  "
            + "  panel_master.panel_code AS \"panelCode\",   "
            + " ttm.is_html_print as \"isHtmlPrint\", " 
            + "  panel_master.panel_id AS \"panelId\",   "
            + "  lbsampledtls.gender_id AS \"genderId\",   "
            + "  lbsampledtls.lab_sample_dtls_id AS \"labSampleDtlsId\",   "
            + "  test_mst.sample_id AS \"sampleId\", "
            + "  lbsampledtls.sample_recollect_flag AS \"sampleRecollectFlag\",   "
            + "  lbsampledtls.sample_recollect_against_id AS \"sampleRecollectAgainstId\",   "
            + "  pati_mst.uhid_number AS uhid,   "
        	+ "  sample_mst.sample_no AS \"labSampleNo\",  "
            + "  test_mst.test_desc AS \"testDesc\",   "
            + "  test_mst.service_id AS \"serviceId\", "
            + "  gen_mst.gender_code AS \"genderCode\", "
            + "  bedmst.bed_number AS \"bedNumber\", "
            + "  order_master.admission_id AS \"visitAdmId\",   "
            + "  order_master.visit_type_id AS \"visitTypeId\",   "
            + "  lbsampledtls.dept_id AS \"deptId\",   " 
            + "  lbsampledtls.test_id AS \"testId\",   "
            + "  test_mst.test_type AS \"testType\",   "
            + "  lbsampledtls.sub_dept_id AS \"subDeptId\",   "
            + "  lbsampledtls.patient_id AS \"patientId\",   "
            + "  lbsampledtls.order_details_id AS \"orderDetailsId\",   "
            + "  to_char(pati_mst.dob, 'DD-MM-YYYY') AS \"dob\", "
            + "  to_char(admission_dtls.doa,'dd/MM/yyyy') as doa, " 
			+ "  to_char(admission_dtls.pdd,'dd/MM/yyyy') as pdd, "
			+ "	calculate_age(pati_mst.dob) as \"ageString\", "
			+ "	pati_mst.is_vip as \"isVip\", "
			+ "	(case "
			+ "		when pati_mst.is_vip = 'Y' then coalesce(pati_mst.alise_name, pati_mst.first_name) "
			+ "		else concat(coalesce(pati_mst.first_name) , ' ', coalesce(pati_mst.middle_name) , ' ', coalesce(pati_mst.last_name)) "
			+ "	end) as \"patientName\", "
			+ "	to_char(order_master.order_date, 'dd/mm/yyyy') as \"orderDate\","
			+ "	to_char(order_master.order_date, 'dd/MM/yyyy HH12:MI AM') as \"orderDtTime\","
			+ " pre_mst.prefix_code as  \"prefixCode\", "
            + "  pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name)||' ' || pati_mst.last_name || ' ' || calculate_age(pati_mst.dob) "
            + "  || '/' || gen_mst.gender_code AS \"patientDetails\",   "
            + "  CAST(lbsampledtls.patient_visit_age AS DATE) - CAST(pati_mst.dob AS DATE) AS \"patientAgeDays\",   "
            + "  'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' AS \"doctorDetails\",   "
            + "  lbsampledtls.sample_barcode AS \"sampleBarcode\",   "
            + "  order_details.created_date AS \"orderDateTime\",   "
            + "  to_char(order_details.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"orderDateTimeString\" , " 
            + "  lbsampledtls.sample_collection_datetime AS \"sampleCollectionDatetime\" ,  "
            + " to_char(microTemp.report_entry_date, 'dd/MM/yyyy HH12:MI AM')as \"reportEntryDate\" , " 
    		+ " to_char(microTemp.report_validation_date,'dd/MM/yyyy HH12:MI AM') as \"reportValidationDate\" , "  
    		+ " to_char(microTemp.report_release_date ,'dd/MM/yyyy HH12:MI AM') as \"reportReleaseDate\" , "  
    		+ "	microTemp.is_report_recall as \"isReportRecall\", "
            + "  wardmast.ward_code AS \"wardCode\" ,  "
            + "  test_mst.test_code AS \"testCode\" ,  " 
            + "  samp_mst.sample_desc AS \"sampleType\" ,"
            + " lbsampledtls.micro_template_status_id as \"microTemplateStatusId\","
            + " order_master.order_no as \"orderNo\", "
            + " order_master.admission_id as \"admissionId\","
            + " order_master.encounter_id as \"encounterId\","
            + " microTemp.is_provisional_release as \"isProvisionalRelease\"," 
            + "	microTemp.template_object as \"templateObject\", "
        	+ " refM.referral_name as \"referralName\","
			+ " pati_mst.email as \"email\" , "
            + " microTemp.is_final_release as \"isFinalRelease\","
            + "(case " + 
            "  when order_master.encounter_id > 0" + 
            " then tem.encounter_number " + 
            " else " + 
            " ta.admission_number " + 
            " end) as \"visitNo\",  "
            +" (case when order_master.encounter_id > 0 then " +
            " ( tpsd.payment_entitlement_id ) " +
            " else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\", "
            + " (case when order_master.encounter_id > 0 then  " + 
    		" ( tcm.address )  " + 
    		" else ( tcm1.address ) end ) as \"comAddress\" ,"
    		+ " (case when order_master.encounter_id > 0 then  " + 
    		" ( tcm.company_desc )  " + 
    		" else ( tcm1.company_desc ) end ) as \"companyDesc\" ,"
    		+ "(case when order_master.encounter_id > 0 then " + 
    		" ( to_char(tem.encounter_date, 'dd/MM/yyyy HH12:MI AM')) " + 
    		" else (  to_char(admission_dtls.doa, 'dd/MM/yyyy HH12:MI AM') ) end ) as \"registerDate\" ,"
            + "order_details.is_dummy_service as \"isDummyService\", "
            + "order_details.tariff_id as \"tariffId\" "
            + " FROM   "
            + "  lab.t_lab_sample_dtls lbsampledtls   "
            + "  LEFT join lab.t_microbiology_template microTemp on lbsampledtls.lab_sample_dtls_id = microTemp.lab_sample_dtls_id  "
            + "  INNER JOIN public.t_order_details order_details ON order_details.order_details_id = lbsampledtls.order_details_id   "
            + "  left join public.t_template_master ttm on ttm.template_id = microTemp.template_id"
            + "  INNER JOIN public.t_order_master order_master ON order_master.order_id = order_details.order_id   "
            + "  INNER JOIN lab.t_lab_sample_master sample_mst ON lbsampledtls.lab_sample_id = sample_mst.lab_sample_id  "
            + "  INNER JOIN public.m_visit_type_master visit_type_master ON visit_type_master.visit_type_id = order_master.visit_type_id   "
            + "  INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id  "
            + "  inner join lab.m_report_type_master reportType on test_mst.report_type_id = reportType.report_type_id "
            + "  INNER JOIN patient.t_patient_registration pati_mst ON lbsampledtls.patient_id = pati_mst.patient_id   "
            + "  INNER JOIN m_prefix_master pre_mst ON pati_mst.prefix_id = pre_mst.prefix_id   "
            + "  INNER JOIN m_gender_master gen_mst ON pati_mst.gender_id = gen_mst.gender_id   "
            + "  INNER JOIN lab.m_test_master test_master ON test_master.test_id = lbsampledtls.test_id   "
            + "  INNER JOIN lab.m_container_master contai_mst ON contai_mst.container_id = lbsampledtls.container_id   "
            + "  INNER JOIN lab.m_sample_master samp_mst ON samp_mst.sample_id = test_master.sample_id   "
            + "  INNER JOIN public.t_lab_configuration lab_config ON lab_config.unit_id = lbsampledtls.org_unit_id   "
            + "  LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id   "
            + "  LEFT JOIN doctor.m_doctor_master doct_mst ON order_master.ord_doctor_id = doct_mst.doctor_id   "
            + "  LEFT JOIN doctor.m_speciality_master speciality_master ON speciality_master.speciality_id = order_master.ord_doc_spl_id  "
            + " left join adt.t_admission ta on ta.admission_id = order_master.admission_id "
			+ " left join opd.t_patient_sponsor_details tpsd "
			+ "left join public.t_company_master tcm  on tcm.company_id  =  tpsd.payee_id "
			+ " on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
			+ " left join opd.t_patient_sponsor_details tpsd2 "
			+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
			+ " left join public.t_company_master tcm1  on tcm1.company_id  =  tpsd2.payee_id "
            + "  LEFT JOIN adt.t_admission_details admission_dtls ON order_master.admission_id = admission_dtls.admission_id "
        	+ "  and admission_dtls.nursing_acceptance = 'Y' and ( (admission_dtls.is_discharge = 'D' and admission_dtls.status = 'I') " + 
        	"	or (admission_dtls.is_discharge = 'N' and admission_dtls.status = 'A')) "
            + "  LEFT JOIN adt.m_ward_master wardmast ON admission_dtls.ward_id = wardmast.ward_id   "
            + "  LEFT JOIN adt.m_bed_master bedmst ON admission_dtls.bed_id = bedmst.bed_id   "
            + "  LEFT JOIN lab.t_panel_master panel_master ON panel_master.panel_id = lbsampledtls.profile_id   "
            + "left join t_referal_details dtls1 on " + 
			" (case " + 
			" when order_master.encounter_id > 0 then dtls1.encounter_id = order_master.encounter_id " + 
			" else dtls1.admission_id = order_master.admission_id end) and dtls1.status = 'A' " + 
			" left join adt.m_referral_master refM on " + 
			" dtls1.referral_id = refM.referral_id "
			+ " left join opd.t_encounter_master tem on tem.encounter_id = order_master.encounter_id "
			+ " left join billing.t_credit_note_detail tcnd on "
	        + " tcnd.order_detail_id = order_details.order_details_id "
	        + " and tcnd.status = 'A' "
	        + " and tcnd.cancel_or_discount = 1 "
            + "WHERE   " 
            + "   lbsampledtls.org_id = :orgId   " 
            + "  AND lbsampledtls.org_unit_id = :orgUnitId   "
            + "  AND lbsampledtls.dept_id = lab_config.lab_speciality_id  "
            + "  AND lbsampledtls.is_outsourced = 'N' "
            + "  and reportType.is_template = true "
            + " and order_details.ord_cancelled = 'N' " + 
            " and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "+
            "	and order_details.status = 'A' "),
      
    @NamedNativeQuery(name = "MICROBIOLOGY_TEMPLATE_WORKBEANCH_ATFER_WORKBEANCH", query = " select " + 
    		" visit_type_master.visit_type_code as \"visitType\", "
    		+ " order_details.priority_id AS \"priorityId\", " + 
    		" subspe_mst.sub_speciality_name as \"deptName\", " + 
    		" panel_master.panel_code as \"panelCode\", " + 
    		" ttm.is_html_print as \"isHtmlPrint\", " +
    		" panel_master.panel_id as \"panelId\", " + 
    		" lbsampledtls.gender_id as \"genderId\", " + 
    		" lbsampledtls.lab_sample_dtls_id as \"labSampleDtlsId\", " + 
    		" test_mst.sample_id as \"sampleId\", " + 
    		" lbsampledtls.sample_recollect_flag as \"sampleRecollectFlag\", " + 
    		" lbsampledtls.sample_recollect_against_id as \"sampleRecollectAgainstId\", " + 
    		" pati_mst.uhid_number as uhid, " + 
    		" sample_mst.sample_no as \"labSampleNo\", " + 
    		" test_mst.test_desc as \"testDesc\", " + 
    		" test_mst.service_id as \"serviceId\", " + 
    		" gen_mst.gender_code as \"genderCode\", " + 
    		" bedmst.bed_number as \"bedNumber\", " + 
    		" order_master.admission_id as \"visitAdmId\", " + 
    		" order_master.visit_type_id as \"visitTypeId\", " + 
    		" lbsampledtls.dept_id as \"deptId\", " + 
    		" lbsampledtls.test_id as \"testId\", " + 
    		" test_mst.test_type as \"testType\", " + 
    		" lbsampledtls.sub_dept_id as \"subDeptId\", " + 
    		" lbsampledtls.patient_id as \"patientId\", " + 
    		" lbsampledtls.order_details_id as \"orderDetailsId\", " + 
    		" to_char(pati_mst.dob, 'DD-MM-YYYY') as \"dob\", " + 
    		" to_char(admission_dtls.doa, 'dd/MM/yyyy') as doa, " + 
    		" to_char(admission_dtls.pdd, 'dd/MM/yyyy') as pdd, " + 
    		" calculate_age(pati_mst.dob) as \"ageString\", " + 
    		" pati_mst.is_vip as \"isVip\", " + 
    		" (case " + 
    		" when pati_mst.is_vip = 'Y' then coalesce(pati_mst.alise_name, pati_mst.first_name) " + 
    		" else concat(coalesce(pati_mst.first_name) , ' ', coalesce(pati_mst.middle_name) , ' ', coalesce(pati_mst.last_name)) " + 
    		" end) as \"patientName\", " + 
    		" to_char(order_master.order_date, 'dd/mm/yyyy') as \"orderDate\", " + 
    		"	to_char(order_master.order_date, 'dd/MM/yyyy HH12:MI AM') as \"orderDtTime\","+
    		" pre_mst.prefix_code as \"prefixCode\", " + 
    		" pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name)||' ' || pati_mst.last_name || ' ' || calculate_age(pati_mst.dob) "
    		+ " || '/' || gen_mst.gender_code as \"patientDetails\", " + 
    		" cast(lbsampledtls.patient_visit_age as DATE) - cast(pati_mst.dob as DATE) as \"patientAgeDays\", " + 
    		" 'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' as \"doctorDetails\", " + 
    		" lbsampledtls.sample_barcode as \"sampleBarcode\", " + 
    		" order_details.created_date as \"orderDateTime\", " + 
    		 "  to_char(order_details.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"orderDateTimeString\" , " +
    		" lbsampledtls.sample_collection_datetime as \"sampleCollectionDatetime\" , " + 
    		" to_char(microTemp.report_entry_date, 'dd/MM/yyyy HH12:MI AM') as \"reportEntryDate\" , " +
    		" to_char(microTemp.report_validation_date,'dd/MM/yyyy HH12:MI AM') as \"reportValidationDate\" , " + 
    		" to_char(microTemp.report_release_date ,'dd/MM/yyyy HH12:MI AM') as \"reportReleaseDate\" , " + 
    		"	microTemp.is_report_recall as \"isReportRecall\", "+
    		" wardmast.ward_code as \"wardCode\" , " + 
    		" test_mst.test_code as \"testCode\" , " + 
    		" samp_mst.sample_desc as \"sampleType\" , " + 
    		" lbsampledtls.micro_template_status_id as \"microTemplateStatusId\", " + 
    		" order_master.order_no as \"orderNo\", " + 
    		" order_master.admission_id as \"admissionId\", " + 
    		" order_master.encounter_id as \"encounterId\", " + 
    		" microTemp.is_provisional_release as \"isProvisionalRelease\", " + 
    		" microTemp.microbiology_template_id as \"microbiologyTemplateId\", " + 
    		" microTemp.template_object as \"templateObject\", " + 
    		" refM.referral_name as \"referralName\", " + 
    		" pati_mst.email as \"email\" , " + 
    		" microTemp.is_final_release as \"isFinalRelease\", " + 
    		" (case " + 
    		" when order_master.encounter_id > 0 then tem.encounter_number " + 
    		" else ta.admission_number " + 
    		" end) as \"visitNo\", " + 
    		" (case " + 
    		" when app.is_pre_or_post_billing = 'Y' then tbpd.due_amount = 0 " + 
    		" when app.is_pre_or_post_billing = 'N' then true " + 
    		" end) as \"billSettled\", " + 
    		" (case when order_master.encounter_id > 0 then " +
    		" ( tpsd.payment_entitlement_id ) " +
    		" else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\","
    		+ " (case when order_master.encounter_id > 0 then  " + 
    		" ( tcm.address )  " + 
    		" else ( tcm1.address ) end ) as \"comAddress\" ,"
    		+ " (case when order_master.encounter_id > 0 then  " + 
    		" ( tcm.company_desc )  " + 
    		" else ( tcm1.company_desc ) end ) as \"companyDesc\" ,"
    		+ "(case when order_master.encounter_id > 0 then " + 
    		" ( to_char(tem.encounter_date, 'dd/MM/yyyy HH12:MI AM')) " + 
    		" else (  to_char(admission_dtls.doa, 'dd/MM/yyyy HH12:MI AM') ) end ) as \"registerDate\", "
    		 + "order_details.is_dummy_service as \"isDummyService\", "
			+ " microTemp.created_by as \"reportEntryBy\", "
			+ " microTemp.validated_by as \"validatedBy\", "
			+ " order_details.tariff_id as \"tariffId\" "
    		+" from " +
    		" lab.t_lab_sample_dtls lbsampledtls " + 
    		" inner join lab.t_microbiology_template microTemp on " + 
    		" lbsampledtls.lab_sample_dtls_id = microTemp.lab_sample_dtls_id " + 
    		" inner join public.t_template_master ttm on ttm.template_id = microTemp.template_id"+
    		" inner join public.t_order_details order_details on " + 
    		" order_details.order_details_id = lbsampledtls.order_details_id " + 
    		" inner join t_unit_application_billing_configuration app on " + 
    		" app.unit_id = order_details.org_unit_id " + 
    		" left join billing.t_billing_details tbd on " + 
    		" order_details.order_details_id = tbd.order_details_id " + 
    		" left join billing.m_billing_master mbm on " + 
    		" mbm.billing_master_id = tbd.billing_master_id " + 
    		" left join billing.t_bill_payee_details tbpd on " + 
    		" tbpd.bill_id = mbm.billing_master_id " + 
    		" and tbpd.payee_id = app.self_payee_id " + 
    		" inner join public.t_order_master order_master on " + 
    		" order_master.order_id = order_details.order_id " + 
    		" inner join lab.t_lab_sample_master sample_mst on " + 
    		" lbsampledtls.lab_sample_id = sample_mst.lab_sample_id " + 
    		" inner join public.m_visit_type_master visit_type_master on " + 
    		" visit_type_master.visit_type_id = order_master.visit_type_id " + 
    		" inner join lab.m_test_master test_mst on " + 
    		" lbsampledtls.test_id = test_mst.test_id " + 
    		" inner join lab.m_report_type_master reportType on " + 
    		" test_mst.report_type_id = reportType.report_type_id " + 
    		" inner join patient.t_patient_registration pati_mst on " + 
    		" lbsampledtls.patient_id = pati_mst.patient_id " + 
    		" inner join m_prefix_master pre_mst on " + 
    		" pati_mst.prefix_id = pre_mst.prefix_id " + 
    		" inner join m_gender_master gen_mst on " + 
    		" pati_mst.gender_id = gen_mst.gender_id " + 
    		" inner join lab.m_test_master test_master on " + 
    		" test_master.test_id = lbsampledtls.test_id " + 
    		" inner join lab.m_container_master contai_mst on " + 
    		" contai_mst.container_id = lbsampledtls.container_id " + 
    		" inner join lab.m_sample_master samp_mst on " + 
    		" samp_mst.sample_id = test_master.sample_id " + 
    		" inner join public.t_lab_configuration lab_config on " + 
    		" lab_config.unit_id = lbsampledtls.org_unit_id " + 
    		" left join doctor.m_sub_speciality_master subspe_mst on " + 
    		" subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id " + 
    		" left join doctor.m_doctor_master doct_mst on " + 
    		" order_master.ord_doctor_id = doct_mst.doctor_id " + 
    		" left join doctor.m_speciality_master speciality_master on " + 
    		" speciality_master.speciality_id = order_master.ord_doc_spl_id " + 
    		 " left join adt.t_admission ta on ta.admission_id = order_master.admission_id  "
			+ " left join opd.t_patient_sponsor_details tpsd "
			+ "left join public.t_company_master tcm  on tcm.company_id  =  tpsd.payee_id "
			+ " on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
			+ " left join opd.t_patient_sponsor_details tpsd2 "
			+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
			+ " left join public.t_company_master tcm1  on tcm1.company_id  =  tpsd2.payee_id "
    		+" left join adt.t_admission_details admission_dtls on " + 
    		" order_master.admission_id = admission_dtls.admission_id " + 
    		" and admission_dtls.nursing_acceptance = 'Y'  and ( (admission_dtls.is_discharge = 'D' and admission_dtls.status = 'I') " + 
    		"  or (admission_dtls.is_discharge = 'N' and admission_dtls.status = 'A'))  " + 
    		" left join adt.m_ward_master wardmast on " + 
    		" admission_dtls.ward_id = wardmast.ward_id " + 
    		" left join adt.m_bed_master bedmst on " + 
    		" admission_dtls.bed_id = bedmst.bed_id " + 
    		" left join lab.t_panel_master panel_master on " + 
    		" panel_master.panel_id = lbsampledtls.profile_id " + 
    	/*	" left join t_referal_details dtls1 on " + 
    		" case " + 
    		" when order_master.encounter_id > 0 then dtls1.encounter_id = order_master.encounter_id " + 
    		" else dtls1.admission_id = order_master.admission_id " + 
    		" end " + */
    		" left join t_referal_details dtls1 on " + 
    		" (dtls1.encounter_id = order_master.encounter_id and order_master.visit_type_id = 1 " + 
    		" or " + 
    		" dtls1.admission_id = order_master.admission_id and order_master.visit_type_id != 1) and dtls1.status = 'A' "+
    		" left join adt.m_referral_master refM on " + 
    		" dtls1.referral_id = refM.referral_id " + 
    		" left join opd.t_encounter_master tem on " + 
    		" tem.encounter_id = order_master.encounter_id " 
    		+ " left join billing.t_credit_note_detail tcnd on "
            + " tcnd.order_detail_id = order_details.order_details_id "
            + " and tcnd.status = 'A' "
            + " and tcnd.cancel_or_discount = 1 "
    		+" where " + 
    		" lbsampledtls.org_id = :orgId " + 
    		" and lbsampledtls.org_unit_id = :orgUnitId " + 
    		" and lbsampledtls.dept_id = lab_config.lab_speciality_id " + 
    		" and lbsampledtls.is_outsourced = 'N' " + 
    		" and reportType.is_template = true " + 
    		" and order_details.ord_cancelled = 'N' " + 
    		" and (order_details.status = 'A' and tcnd.credit_note_detail_id is null) "+
    		" and order_details.status = 'A' "),
    
    /** Sample Pending Acceptance Count */
    @NamedNativeQuery(name = "INCUBATION_OBJSERVATION_WORKLIST_COUNT",
        query = "  SELECT  " + "  COUNT(*)  " 
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



@Entity
@Table(name = "t_micro_incubation", schema = "lab")
@SequenceGenerator(name = "t_seq_micro_incubation", sequenceName = "lab.t_seq_micro_incubation",
    allocationSize = 1)
public class TMicroIncubationMaster {


  @Id
  @Column(name = "t_incubation_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_micro_incubation")
  private int tIncubationId;

  @Column(name = "org_id")
  private Integer orgId;

  @Column(name = "org_unit_id")
  private Integer orgUnitId;

  @Column(name = "lab_sample_dtls_id")
  private Integer labSampleDtlsId;

  @Column(name = "is_growth_found")
  private Character isGrowthFound;

  @Column(name = "is_completed")
  private Character isCompleted;

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

  @Column(name = "incubation_due_time")
  @Convert(converter = LocalTimeConverter.class)
  private Long incubationDueTime;
  
  @Column(name = "incubation_type")
  private Integer incubationType; 
  
  @Column(name = "partially")
  private Character partially;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lab_sample_dtls_id", insertable = false, nullable = false, updatable = false)
  private LabSampleDetailsMaster labSampleDetailsMaster;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tMicroIncubationMaster")
  private List<TMicroIncubationDetailsMaster> listTMicroIncubationDetailsMaster;

  public int gettIncubationId() {
    return tIncubationId;
  }

  public void settIncubationId(int tIncubationId) {
    this.tIncubationId = tIncubationId;
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

  public Integer getLabSampleDtlsId() {
    return labSampleDtlsId;
  }

  public void setLabSampleDtlsId(Integer labSampleDtlsId) {
    this.labSampleDtlsId = labSampleDtlsId;
  }

  public Character getIsGrowthFound() {
    return isGrowthFound;
  }

  public void setIsGrowthFound(Character isGrowthFound) {
    this.isGrowthFound = (isGrowthFound == '\u0000') ? 'N' : isGrowthFound;

  }

  public Character getIsCompleted() {
    return isCompleted;
  }

  public void setIsCompleted(Character isCompleted) {
    this.isCompleted = (isCompleted == '\u0000') ? 'N' : isCompleted;
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
    this.isDeleted = (isDeleted == '\u0000') ? 'N' : isDeleted;

  }

  public LabSampleDetailsMaster getLabSampleDetailsMaster() {
    return labSampleDetailsMaster;
  }

  public void setLabSampleDetailsMaster(LabSampleDetailsMaster labSampleDetailsMaster) {
    this.labSampleDetailsMaster = labSampleDetailsMaster;
  }

  public List<TMicroIncubationDetailsMaster> getListTMicroIncubationDetailsMaster() {
    return listTMicroIncubationDetailsMaster;
  }

  public void setListTMicroIncubationDetailsMaster(
      List<TMicroIncubationDetailsMaster> listTMicroIncubationDetailsMaster) {
    this.listTMicroIncubationDetailsMaster = listTMicroIncubationDetailsMaster;
  }

  public Long getIncubationDueTime() {
    return incubationDueTime;
  }

  public void setIncubationDueTime(Long incubationDueTime) {
    this.incubationDueTime = incubationDueTime;
  }

public Integer getIncubationType() {
	return incubationType;
}

public void setIncubationType(Integer incubationType) {
	this.incubationType = incubationType;
}

public Character getPartially() {
	return partially;
}

public void setPartially(Character partially) {
	this.partially = partially;
}



}
