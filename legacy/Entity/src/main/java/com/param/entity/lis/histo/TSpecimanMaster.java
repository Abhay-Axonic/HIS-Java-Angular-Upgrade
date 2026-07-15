package com.param.entity.lis.histo;

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
import com.param.entity.lis.global.SpecimanMaster;



@NamedNativeQueries(
{ 

@NamedNativeQuery(name = "GET_SPECIMAN_RECEIPT_COUNT", query =  "SELECT count(*) "
		 +" FROM "
		 +"	public.t_order_master ord_mst "
		 +" INNER JOIN public.t_order_details ord_details ON "
		 +"	ord_details.order_id = ord_mst.order_id "
		 +" INNER JOIN lab.m_test_master test_mst ON "
		 +"	test_mst.service_id = ord_details.service_id "
		 +" INNER JOIN lab.m_speciman_master speciman_mst ON "
		 +"	speciman_mst.speciman_id = test_mst.speciman_id "
		 +" INNER JOIN public.t_lab_configuration config on ord_mst.org_unit_id = config.unit_id "
		 +" WHERE "
		 +"	ord_mst.org_id =:orgId "
		 +"	AND ord_mst.org_unit_id =:orgUnitId "
		 +"	AND ord_mst.order_status_id NOT IN(:orderStatusId) "
		 +"	AND ord_details.service_rendered =:serviceRendered "
		 +"	AND ord_details.service_is_billed IN (:serviceIsBilled) "
		 +"	AND ord_details.service_rendering_deptid in ( coalesce(config.histopathology_id,0),coalesce(config.cystopathology_id,0) ) "
		 +"	AND ord_details.ord_cancelled ='N' "
		 +"	AND ord_details.is_outsourced ='N' "
		 +"	AND test_mst.is_histo_cyto IN ('H','C') "
		 +" AND test_mst.test_status = 'A' "
		),

@NamedNativeQuery(name = "GET_SPECIMAN_RECEIPT_COUNT_PANEL", query =  "SELECT count(*) "
		 +" FROM "
		 + "	public.t_order_master ord_mst "
		 + " INNER JOIN public.t_order_details ord_details ON "
		 + "	ord_details.order_id = ord_mst.order_id "
		 + " INNER JOIN service.t_unit_service_master_details ser_dtls ON "
		 + "	ord_details.service_id = ser_dtls.service_master_id "
		 + " INNER JOIN patient.t_patient_registration pati_mst ON "
		 + "	ord_mst.patient_id = pati_mst.patient_id "
		 + " INNER JOIN m_gender_master gen_mst ON "
		 + "	gen_mst.gender_id = pati_mst.gender_id "
		 + " INNER JOIN m_prefix_master pre_mst ON "
		 + "	pre_mst.prefix_id = pati_mst.prefix_id "
		 + " INNER JOIN m_visit_type_master vistmst ON "
		 + "	vistmst.visit_type_id = ord_mst.visit_type_id "
		 + " INNER JOIN lab.t_panel_master panel_mst ON "
		 + "	panel_mst.service_id = ord_details.service_id "
		 + " INNER JOIN lab.t_panel_details panel_dtls ON "
		 + "	panel_mst.panel_id = panel_dtls.panel_id "
		 + " INNER JOIN lab.m_test_master test_mst ON "
		 + "	test_mst.test_id = panel_dtls.test_id "
		 + " INNER JOIN lab.m_speciman_master speciman_mst ON "
		 + "	speciman_mst.speciman_id = test_mst.speciman_id "
		 +" INNER JOIN public.t_lab_configuration config on ord_mst.org_unit_id = config.unit_id "
		 +" WHERE "
		 +"	ord_mst.org_id =:orgId "
		 +"	AND ord_mst.org_unit_id =:orgUnitId "
		 +"	AND ord_mst.order_status_id NOT IN(:orderStatusId) "
		 +"	AND ord_details.service_rendered =:serviceRendered "
		 +"	AND ord_details.service_is_billed IN (:serviceIsBilled) "
		 +"	AND ord_details.service_rendering_deptid in ( coalesce(config.histopathology_id,0),coalesce(config.cystopathology_id,0) ) "
		 +"	AND ord_details.ord_cancelled ='N' "
		 +"	AND ord_details.is_outsourced ='N' "
		 +"	AND test_mst.is_histo_cyto IN ('H','C') "
		 +" AND test_mst.test_status = 'A' "
		 +" and test_mst.test_status = 'A' "
	     +" and panel_mst.status  ='A' "
		 +" and panel_dtls.is_deleted = 'N' "
		 +" and ser_dtls.is_panel = 'Y' "
		 +" and ser_dtls.unit_id = ord_details.org_unit_id"
		),


		 @NamedNativeQuery(name = "GET_PAGINATED_SPECIMAN_RECEIPT_LIST_PREBILL", query = "SELECT "
				 + "	subspe_mst.sub_speciality_name AS \"deptName\", "
				 + "	speciman_mst.speciman_name AS \"specimanType\", "
				 + "	speciman_mst.speciman_id AS \"specimanTypeId\", "
				 + "	vistmst.visit_type_code AS \"visitType\", "
				 + "	ord_mst.order_id AS \"orderId\", "
				 + "	ord_details.order_details_id AS \"orderDetailsId\", "
				 + "	ord_details.package_id AS \"packageId\", "
				 + "	test_mst.dept_id AS \"subDeptId\", "
				 + "	test_mst.test_id AS \"testId\", "
				 + "	test_mst.test_desc AS \"testDesc\", "
				 + "	test_mst.sample_id AS \"sampleId\", "
				 + "	test_mst.container_id AS \"containerId\", "
				 + "	test_mst.is_outsourced AS \"isOutsourced\", "
				 + "	test_mst.is_histo_cyto AS \"isHistoCyto\", "
				 + "	test_mst.is_gynac AS \"isGynac\", "
				 + "	test_mst.is_fnac AS \"isFnac\", "
				 + "	gen_mst.gender_id AS \"genderId\", "
				 + "	ord_mst.order_status_id AS \"orderStatusId\", "
				 + "	ord_mst.org_id AS \"orgId\", "
				 + "	ord_mst.org_unit_id AS \"orgUnitId\", "
				 + "	ord_mst.visit_type_id AS \"visitTypeId\", "
				 + "	ord_mst.ord_doctor_id AS \"doctorId\", "
				 + "	ord_mst.patient_id AS \"patientId\", "
				 + "	ord_mst.admission_id AS \"visitAdmId\", "
				 + " ord_mst.encounter_id AS \"encounterId\", "
				 + "	pati_mst.uhid_number AS \"uhid\", "
				 + "	pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || pati_mst.last_name || ' ' || calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code AS \"patientDetails\", "
				 + "	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name AS \"doctorDetails\", "
				 + "  wardmast.ward_code AS \"wardCode\", "
				 + "  bedmst.bed_number AS \"bedNumber\", "
				 + "	pri_mst.priority_id AS \"priorityId\", "
				 + "	pri_mst.priority_name AS \"priorityName\", "
				 + "	pri_mst.color_code AS \"colorCode\", "
				 + "	null as \"panelCode\" "
				 + " FROM "
				 + "	public.t_order_master ord_mst "
				 + " INNER JOIN public.t_order_details ord_details ON "
				 + "	ord_details.order_id = ord_mst.order_id "
				 + " INNER JOIN lab.m_test_master test_mst ON "
				 + "	test_mst.service_id = ord_details.service_id "
				 + " INNER JOIN patient.t_patient_registration pati_mst ON "
				 + "	ord_mst.patient_id = pati_mst.patient_id "
				 + " INNER JOIN m_gender_master gen_mst ON "
				 + "	gen_mst.gender_id = pati_mst.gender_id "
				 + " INNER JOIN m_prefix_master pre_mst ON "
				 + "	pre_mst.prefix_id = pati_mst.prefix_id "
				 + " INNER JOIN m_visit_type_master vistmst ON "
				 + "	vistmst.visit_type_id = ord_mst.visit_type_id "
				 + " INNER JOIN lab.m_speciman_master speciman_mst ON "
				 + "	speciman_mst.speciman_id = test_mst.speciman_id "
				 + " LEFT JOIN doctor.m_doctor_master doct_mst ON "
				 + "	ord_mst.ord_doctor_id = doct_mst.doctor_id "
				 + " LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON "
				 + "	subspe_mst.sub_speciality_id = test_mst.dept_id "
				 + " LEFT JOIN lab.m_priority_master pri_mst ON "
				 + "	ord_mst.priority_id = pri_mst.priority_id "
				 + " LEFT JOIN adt.t_admission_details admision_dtls ON admision_dtls.admission_id = ord_mst.admission_id and admision_dtls.status = 'A'  and admision_dtls.nursing_acceptance = 'Y' "
				 + " LEFT JOIN adt.m_ward_master wardmast ON admision_dtls.ward_id = wardmast.ward_id "
				 + " LEFT JOIN adt.m_bed_master bedmst ON admision_dtls.bed_id = bedmst.bed_id "
				 + " INNER JOIN public.t_lab_configuration config on ord_mst.org_unit_id = config.unit_id "
				 + " WHERE "
				 + "	ord_mst.org_id =:orgId "
				 + "	AND ord_mst.org_unit_id =:orgUnitId "
				 + "	AND ord_mst.order_status_id NOT IN(:orderStatusId) "
				 + "	AND ord_details.service_rendered =:serviceRendered "
				 + "	AND ord_details.service_is_billed in (:serviceIsBilled) "
				 + "	AND ord_details.service_rendering_deptid in ( coalesce(config.histopathology_id,0),coalesce(config.cystopathology_id,0) ) "
				 + "	AND ord_details.ord_cancelled ='N' "
				 + "	AND ord_details.is_outsourced ='N' "
				 + "	AND test_mst.is_histo_cyto IN ('C','H') "
				 + "    and test_mst.test_status = 'A' "
				 + " union "
				 + " SELECT "
				 + "	subspe_mst.sub_speciality_name AS \"deptName\", "
				 + "	speciman_mst.speciman_name AS \"specimanType\", "
				 + "	speciman_mst.speciman_id AS \"specimanTypeId\", "
				 + "	vistmst.visit_type_code AS \"visitType\", "
				 + "	ord_mst.order_id AS \"orderId\", "
				 + "	ord_details.order_details_id AS \"orderDetailsId\", "
				 + "	ord_details.package_id AS \"packageId\", "
				 + "	test_mst.dept_id AS \"subDeptId\", "
				 + "	test_mst.test_id AS \"testId\", "
				 + "	test_mst.test_desc AS \"testDesc\", "
				 + "	test_mst.sample_id AS \"sampleId\", "
				 + "	test_mst.container_id AS \"containerId\", "
				 + "	test_mst.is_outsourced AS \"isOutsourced\", "
				 + "	test_mst.is_histo_cyto AS \"isHistoCyto\", "
				 + "	test_mst.is_gynac AS \"isGynac\", "
				 + "	test_mst.is_fnac AS \"isFnac\", "
				 + "	gen_mst.gender_id AS \"genderId\", "
				 + "	ord_mst.order_status_id AS \"orderStatusId\", "
				 + "	ord_mst.org_id AS \"orgId\", "
				 + "	ord_mst.org_unit_id AS \"orgUnitId\", "
				 + "	ord_mst.visit_type_id AS \"visitTypeId\", "
				 + "	ord_mst.ord_doctor_id AS \"doctorId\", "
				 + "	ord_mst.patient_id AS \"patientId\", "
				 + "	ord_mst.admission_id AS \"visitAdmId\", "
				 + " ord_mst.encounter_id AS \"encounterId\", "
				 + "	pati_mst.uhid_number AS \"uhid\", "
				 + "	pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || pati_mst.last_name || ' ' || calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code AS \"patientDetails\", "
				 + "	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name AS \"doctorDetails\", "
				 + "  wardmast.ward_code AS \"wardCode\", "
				 + "  bedmst.bed_number AS \"bedNumber\", "
				 + "	pri_mst.priority_id AS \"priorityId\", "
				 + "	pri_mst.priority_name AS \"priorityName\", "
				 + "	pri_mst.color_code AS \"colorCode\", "
				 + "	panel_mst.panel_code as \"panelCode\" "
				 + " FROM "
				 + "	public.t_order_master ord_mst "
				 + " INNER JOIN public.t_order_details ord_details ON "
				 + "	ord_details.order_id = ord_mst.order_id "
				 + " INNER JOIN service.t_unit_service_master_details ser_dtls ON "
				 + "	ord_details.service_id = ser_dtls.service_master_id "
				 + " INNER JOIN patient.t_patient_registration pati_mst ON "
				 + "	ord_mst.patient_id = pati_mst.patient_id "
				 + " INNER JOIN m_gender_master gen_mst ON "
				 + "	gen_mst.gender_id = pati_mst.gender_id "
				 + " INNER JOIN m_prefix_master pre_mst ON "
				 + "	pre_mst.prefix_id = pati_mst.prefix_id "
				 + " INNER JOIN m_visit_type_master vistmst ON "
				 + "	vistmst.visit_type_id = ord_mst.visit_type_id "
				 + " INNER JOIN lab.t_panel_master panel_mst ON "
				 + "	panel_mst.service_id = ord_details.service_id "
				 + " INNER JOIN lab.t_panel_details panel_dtls ON "
				 + "	panel_mst.panel_id = panel_dtls.panel_id "
				 + "INNER JOIN lab.m_test_master test_mst ON "
				 + "	test_mst.test_id = panel_dtls.test_id "
				 + " INNER JOIN lab.m_speciman_master speciman_mst ON "
				 + "	speciman_mst.speciman_id = test_mst.speciman_id "
				 + " LEFT JOIN doctor.m_doctor_master doct_mst ON "
				 + "	ord_mst.ord_doctor_id = doct_mst.doctor_id "
				 + " LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON "
				 + "	subspe_mst.sub_speciality_id = test_mst.dept_id "
				 + " LEFT JOIN lab.m_priority_master pri_mst ON "
				 + "	ord_mst.priority_id = pri_mst.priority_id "
				 + " LEFT JOIN adt.t_admission_details admision_dtls ON admision_dtls.admission_id = ord_mst.admission_id and admision_dtls.status = 'A'  and admision_dtls.nursing_acceptance = 'Y' "
				 + " LEFT JOIN adt.m_ward_master wardmast ON admision_dtls.ward_id = wardmast.ward_id "
				 + " LEFT JOIN adt.m_bed_master bedmst ON admision_dtls.bed_id = bedmst.bed_id "
				 + " INNER JOIN public.t_lab_configuration config on ord_mst.org_unit_id = config.unit_id "
				 + " WHERE "
				 + "	ord_mst.org_id =:orgId "
				 + "	AND ord_mst.org_unit_id =:orgUnitId "
				 + "	AND ord_mst.order_status_id NOT IN(:orderStatusId) "
				 + "	AND ord_details.service_rendered =:serviceRendered "
				 + "	AND ord_details.service_is_billed in (:serviceIsBilled) "
				 + "	AND test_mst.dept_id in ( coalesce(config.histopathology_id,0),coalesce(config.cystopathology_id,0) ) "
				 + "	AND ord_details.ord_cancelled ='N' "
				 + "	AND ord_details.is_outsourced ='N' "
				 + "	AND test_mst.is_histo_cyto IN ('C','H') "
				 + "    and test_mst.test_status = 'A' "
				 + "    and panel_mst.status  ='A' "
				 + "    and panel_dtls.is_deleted = 'N' "
				 + "    and ser_dtls.is_panel = 'Y' "
				 + "    and ser_dtls.unit_id = ord_details.org_unit_id"),
		 
		 
		 @NamedNativeQuery(name = "GET_PAGINATED_SPECIMAN_RECEIPT_LIST_POSTBILL", query = "SELECT "
				 + "	subspe_mst.sub_speciality_name AS \"deptName\", "
				 + "	speciman_mst.speciman_name AS \"specimanType\", "
				 + "	speciman_mst.speciman_id AS \"specimanTypeId\", "
				 + "	vistmst.visit_type_code AS \"visitType\", "
				 + "	ord_mst.order_id AS \"orderId\", "
				 + "	ord_details.order_details_id AS \"orderDetailsId\", "
				 + "	ord_details.package_id AS \"packageId\", "
				 + "	test_mst.dept_id AS \"subDeptId\", "
				 + "	test_mst.test_id AS \"testId\", "
				 + "	test_mst.test_desc AS \"testDesc\", "
				 + "	test_mst.sample_id AS \"sampleId\", "
				 + "	test_mst.container_id AS \"containerId\", "
				 + "	test_mst.is_outsourced AS \"isOutsourced\", "
				 + "	test_mst.is_histo_cyto AS \"isHistoCyto\", "
				 + "	test_mst.is_gynac AS \"isGynac\", "
				 + "	test_mst.is_fnac AS \"isFnac\", "
				 + "	gen_mst.gender_id AS \"genderId\", "
				 + "	ord_mst.order_status_id AS \"orderStatusId\", "
				 + "	ord_mst.org_id AS \"orgId\", "
				 + "	ord_mst.org_unit_id AS \"orgUnitId\", "
				 + "	ord_mst.visit_type_id AS \"visitTypeId\", "
				 + "	ord_mst.ord_doctor_id AS \"doctorId\", "
				 + "	ord_mst.patient_id AS \"patientId\", "
				 + "	ord_mst.admission_id AS \"visitAdmId\", "
				 + " ord_mst.encounter_id AS \"encounterId\", "
				 + "	pati_mst.uhid_number AS \"uhid\", "
				 + "	pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || pati_mst.last_name || ' ' || calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code AS \"patientDetails\", "
				 + "	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name AS \"doctorDetails\", "
				 + "  wardmast.ward_code AS \"wardCode\", "
				 + "  bedmst.bed_number AS \"bedNumber\", "
				 + "	pri_mst.priority_id AS \"priorityId\", "
				 + "	pri_mst.priority_name AS \"priorityName\", "
				 + "	pri_mst.color_code AS \"colorCode\", "
				 + "	null as \"panelCode\" "
				 + " FROM "
				 + "	public.t_order_master ord_mst "
				 + " INNER JOIN public.t_order_details ord_details ON "
				 + "	ord_details.order_id = ord_mst.order_id "
				 + " INNER JOIN lab.m_test_master test_mst ON "
				 + "	test_mst.service_id = ord_details.service_id "
				 + " INNER JOIN patient.t_patient_registration pati_mst ON "
				 + "	ord_mst.patient_id = pati_mst.patient_id "
				 + " INNER JOIN m_gender_master gen_mst ON "
				 + "	gen_mst.gender_id = pati_mst.gender_id "
				 + " INNER JOIN m_prefix_master pre_mst ON "
				 + "	pre_mst.prefix_id = pati_mst.prefix_id "
				 + " INNER JOIN m_visit_type_master vistmst ON "
				 + "	vistmst.visit_type_id = ord_mst.visit_type_id "
				 + " INNER JOIN lab.m_speciman_master speciman_mst ON "
				 + "	speciman_mst.speciman_id = test_mst.speciman_id "
				 + " LEFT JOIN doctor.m_doctor_master doct_mst ON "
				 + "	ord_mst.ord_doctor_id = doct_mst.doctor_id "
				 + " LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON "
				 + "	subspe_mst.sub_speciality_id = test_mst.dept_id "
				 + " LEFT JOIN lab.m_priority_master pri_mst ON "
				 + "	ord_mst.priority_id = pri_mst.priority_id "
				 + " LEFT JOIN adt.t_admission_details admision_dtls ON admision_dtls.admission_id = ord_mst.admission_id and admision_dtls.status = 'A'  and admision_dtls.nursing_acceptance = 'Y' "
				 + " LEFT JOIN adt.m_ward_master wardmast ON admision_dtls.ward_id = wardmast.ward_id "
				 + " LEFT JOIN adt.m_bed_master bedmst ON admision_dtls.bed_id = bedmst.bed_id "
				 + " INNER JOIN public.t_lab_configuration config on ord_mst.org_unit_id = config.unit_id "
				 + " WHERE "
				 + "	ord_mst.org_id =:orgId "
				 + "	AND ord_mst.org_unit_id =:orgUnitId "
				 + "	AND ord_mst.order_status_id NOT IN(:orderStatusId) "
				 + "	AND ord_details.service_rendered =:serviceRendered "
				 + "	AND ord_details.service_is_billed in (:serviceIsBilled) "
				 + "	AND ord_details.service_rendering_deptid in ( coalesce(config.histopathology_id,0),coalesce(config.cystopathology_id,0) ) "
				 + "	AND ord_details.ord_cancelled ='N' "
				 + "	AND ord_details.is_outsourced ='N' "
				 + "	AND test_mst.is_histo_cyto IN ('C','H') "
				 + "    and test_mst.test_status = 'A' "
				 + " union "
				 + " SELECT "
				 + "	subspe_mst.sub_speciality_name AS \"deptName\", "
				 + "	speciman_mst.speciman_name AS \"specimanType\", "
				 + "	speciman_mst.speciman_id AS \"specimanTypeId\", "
				 + "	vistmst.visit_type_code AS \"visitType\", "
				 + "	ord_mst.order_id AS \"orderId\", "
				 + "	ord_details.order_details_id AS \"orderDetailsId\", "
				 + "	ord_details.package_id AS \"packageId\", "
				 + "	test_mst.dept_id AS \"subDeptId\", "
				 + "	test_mst.test_id AS \"testId\", "
				 + "	test_mst.test_desc AS \"testDesc\", "
				 + "	test_mst.sample_id AS \"sampleId\", "
				 + "	test_mst.container_id AS \"containerId\", "
				 + "	test_mst.is_outsourced AS \"isOutsourced\", "
				 + "	test_mst.is_histo_cyto AS \"isHistoCyto\", "
				 + "	test_mst.is_gynac AS \"isGynac\", "
				 + "	test_mst.is_fnac AS \"isFnac\", "
				 + "	gen_mst.gender_id AS \"genderId\", "
				 + "	ord_mst.order_status_id AS \"orderStatusId\", "
				 + "	ord_mst.org_id AS \"orgId\", "
				 + "	ord_mst.org_unit_id AS \"orgUnitId\", "
				 + "	ord_mst.visit_type_id AS \"visitTypeId\", "
				 + "	ord_mst.ord_doctor_id AS \"doctorId\", "
				 + "	ord_mst.patient_id AS \"patientId\", "
				 + "	ord_mst.admission_id AS \"visitAdmId\", "
				 + " ord_mst.encounter_id AS \"encounterId\", "
				 + "	pati_mst.uhid_number AS \"uhid\", "
				 + "	pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || pati_mst.last_name || ' ' || calculate_age(pati_mst.dob) || '/' || gen_mst.gender_code AS \"patientDetails\", "
				 + "	'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name AS \"doctorDetails\", "
				 + "  wardmast.ward_code AS \"wardCode\", "
				 + "  bedmst.bed_number AS \"bedNumber\", "
				 + "	pri_mst.priority_id AS \"priorityId\", "
				 + "	pri_mst.priority_name AS \"priorityName\", "
				 + "	pri_mst.color_code AS \"colorCode\", "
				 + "	panel_mst.panel_code as \"panelCode\" "
				 + " FROM "
				 + "	public.t_order_master ord_mst "
				 + " INNER JOIN public.t_order_details ord_details ON "
				 + "	ord_details.order_id = ord_mst.order_id "
				 + " INNER JOIN service.t_unit_service_master_details ser_dtls ON "
				 + "	ord_details.service_id = ser_dtls.service_master_id "
				 + " INNER JOIN patient.t_patient_registration pati_mst ON "
				 + "	ord_mst.patient_id = pati_mst.patient_id "
				 + " INNER JOIN m_gender_master gen_mst ON "
				 + "	gen_mst.gender_id = pati_mst.gender_id "
				 + " INNER JOIN m_prefix_master pre_mst ON "
				 + "	pre_mst.prefix_id = pati_mst.prefix_id "
				 + " INNER JOIN m_visit_type_master vistmst ON "
				 + "	vistmst.visit_type_id = ord_mst.visit_type_id "
				 + " INNER JOIN lab.t_panel_master panel_mst ON "
				 + "	panel_mst.service_id = ord_details.service_id "
				 + " INNER JOIN lab.t_panel_details panel_dtls ON "
				 + "	panel_mst.panel_id = panel_dtls.panel_id "
				 + "INNER JOIN lab.m_test_master test_mst ON "
				 + "	test_mst.test_id = panel_dtls.test_id "
				 + " INNER JOIN lab.m_speciman_master speciman_mst ON "
				 + "	speciman_mst.speciman_id = test_mst.speciman_id "
				 + " LEFT JOIN doctor.m_doctor_master doct_mst ON "
				 + "	ord_mst.ord_doctor_id = doct_mst.doctor_id "
				 + " LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON "
				 + "	subspe_mst.sub_speciality_id = test_mst.dept_id "
				 + " LEFT JOIN lab.m_priority_master pri_mst ON "
				 + "	ord_mst.priority_id = pri_mst.priority_id "
				 + " LEFT JOIN adt.t_admission_details admision_dtls ON admision_dtls.admission_id = ord_mst.admission_id and admision_dtls.status = 'A'  and admision_dtls.nursing_acceptance = 'Y' "
				 + " LEFT JOIN adt.m_ward_master wardmast ON admision_dtls.ward_id = wardmast.ward_id "
				 + " LEFT JOIN adt.m_bed_master bedmst ON admision_dtls.bed_id = bedmst.bed_id "
				 + " INNER JOIN public.t_lab_configuration config on ord_mst.org_unit_id = config.unit_id "
				 + " WHERE "
				 + "	ord_mst.org_id =:orgId "
				 + "	AND ord_mst.org_unit_id =:orgUnitId "
				 + "	AND ord_mst.order_status_id NOT IN(:orderStatusId) "
				 + "	AND ord_details.service_rendered =:serviceRendered "
				 + "	AND ord_details.service_is_billed in (:serviceIsBilled) "
				 + "	AND test_mst.dept_id in ( coalesce(config.histopathology_id,0),coalesce(config.cystopathology_id,0) ) "
				 + "	AND ord_details.ord_cancelled ='N' "
				 + "	AND ord_details.is_outsourced ='N' "
				 + "	AND test_mst.is_histo_cyto IN ('C','H') "
				 + "    and test_mst.test_status = 'A' "
				 + "    and panel_mst.status  ='A' "
				 + "    and panel_dtls.is_deleted = 'N' "
				 + "    and ser_dtls.is_panel = 'Y' "
				 + "    and ser_dtls.unit_id = ord_details.org_unit_id")
})

@NamedQueries({

/*	@NamedQuery(name = "GET_SPCIMEN_LIST", query = "SELECT tSpecimanMaster.tSpecimanId AS tSpecimanId,"
			+ " tSpecimanMaster.orgId AS orgId,"
			+ " tSpecimanMaster.orgUnitId AS orgUnitId,"
			+ " tSpecimanMaster.specimanExaminination AS specimanExaminination,"
			+ " tSpecimanMaster.labSampleDtlsId AS labSampleDtlsId,"
			+ " tSpecimanMaster.histopathlogyNumber AS histopathlogyNumber,"
			+ " tSpecimanMaster.barcodeNo AS barcodeNo,"
			+ " tSpecimanMaster.specimanTypeId AS specimanTypeId,"
			+ " tSpecimanMaster.specimanId AS specimanId,"
			+ " tSpecimanMaster.specimanName AS specimanName "
			+ " FROM TSpecimanMaster tSpecimanMaster "
			+ " WHERE tSpecimanMaster.orgId = :orgId "
			+ " AND tSpecimanMaster.orgUnitId = :orgUnitId"
			+ " AND tSpecimanMaster.isDeleted ='N'"),*/
	
@NamedQuery(name = "GET_SPCIMEN_LIST", query = " SELECT "
		     +"	tSpecimanMaster.tSpecimanId AS tSpecimanId, billMst.billNumber as billNumber, "
		     +"	to_char(ordermst.orderDate,'dd-MM-yyyy') AS orderDate, "
		     + "  to_char(ordermst.orderDate, 'DD/MM/YYYY HH24:MI:SS') AS orderDateAndTimeString,  "
             +" ordermst.orderDate AS orderDateAndTime, "
		     +"	visitMst.visitTypeCode AS visitType, "
		     +"	testMst.testDesc AS testDesc, "
		     +"	speciMst.desc AS specimanType, "
			 +"	labSampleMst.visitTypeId AS visitTypeId, "
			 +"	testMst.testDesc AS testDesc, "
			 +"	speciMst.specimanId AS specimanId, "
			 +"	tSpecimanMaster.orgId AS orgId, "
			 +"	tSpecimanMaster.orgUnitId AS orgUnitId, "
			 +"	tSpecimanMaster.specimanExaminination AS specimanExaminination, "
			 +"	tSpecimanMaster.labSampleDtlsId AS labSampleDtlsId, "
			 +"	tSpecimanMaster.histopathlogyNumber AS histopathlogyNumber, "
			 +"	tSpecimanMaster.barcodeNo AS barcodeNo, "
			 +"	tSpecimanMaster.specimanTypeId AS specimanTypeId, "
			 +"	tSpecimanMaster.specimanId AS specimanId, "
			 +"	tSpecimanMaster.specimanName AS specimanName, "
			 +"	patientReg.uhidNumber AS uhid, "
			 +"	genderMst.desc AS genderName, "
			 +" genderMst.id AS genderId, "
			 +" patientReg.imagePath AS imagePath, "
			 +"	patientReg.dob AS dob, "
			 +"	prefixMst.prefixCode || ' ' || patientReg.firstName || ' ' || coalesce(patientReg.middleName) ||' '|| patientReg.lastName AS patientDetails, "
			 +"	'Dr.' || ' ' || doctorMst.firstName || ' ' || doctorMst.lastName AS doctorDetails ,"
			 +" bedMst.bedNumber AS bedNumber, "  
	         +" wardmst.wardCode AS wardCode, "
	         + " (CASE WHEN encPaymentEntitlementMaster.paymentEntitlementDesc is not null  THEN "
	         + " encPaymentEntitlementMaster.paymentEntitlementDesc "
	         + " ELSE admiPaymentEntitlementMaster.paymentEntitlementDesc  END)  AS entitlementDesc "
			 +"FROM "
			 +"	TSpecimanMaster tSpecimanMaster "
			 +"INNER JOIN tSpecimanMaster.labSampleDetailsMaster labSampleDetailsMst "
	         +"INNER JOIN labSampleDetailsMst.labSampleMaster labSampleMst "
	         +"INNER JOIN labSampleMst.orderMaster ordermst "
	         +"INNER JOIN labSampleDetailsMst.orderDetailsMaster ordDtl "
	         + " left join ordDtl.billingDetailsList billDtl "
	         + " left join billDtl.billingMaster billMst "
			 +"INNER JOIN labSampleDetailsMst.testMaster testMst WITH testMst.isHistoCyto ='H' "
			 +"INNER JOIN testMst.specimanMaster speciMst "
			 +"INNER JOIN labSampleDetailsMst.labSampleMaster labSampleMst "
			 +"INNER JOIN labSampleMst.visitTypeMaster visitMst "
			 +"INNER JOIN labSampleDetailsMst.patientRegistration patientReg "
			 +"INNER JOIN patientReg.prefixMaster prefixMst "
			 +"INNER JOIN labSampleDetailsMst.genderMaster genderMst "
			 +"INNER JOIN labSampleDetailsMst.orderMaster ordMst "
			 +"LEFT JOIN labSampleDetailsMst.doctorMaster doctorMst "
	         +"LEFT JOIN ordMst.admission admsion "
	         +"LEFT JOIN admsion.admissionDetailsList addetails WITH addetails.nursingAcceptance = 'Y' AND addetails.status = 'A' "
	         +"LEFT JOIN addetails.bedMaster bedMst "
	         +"LEFT JOIN addetails.wardMaster wardmst "
	         +"LEFT JOIN ordMst.encounterMaster encounterMaster "
	         +"LEFT JOIN encounterMaster.patientSponsorDetailsList encounterPatientSponsorDetailsList  with encounterPatientSponsorDetailsList.priorityId = 1 and  encounterPatientSponsorDetailsList.status = 'A'"
	         +"LEFT JOIN encounterPatientSponsorDetailsList.paymentEntitlementMaster encPaymentEntitlementMaster "
	         +"LEFT JOIN admsion.patientSponsorDetailsList adtPatientSponsorDetailsList  with adtPatientSponsorDetailsList.priorityId = 1 and  adtPatientSponsorDetailsList.status = 'A' "
	         +"LEFT JOIN adtPatientSponsorDetailsList.paymentEntitlementMaster admiPaymentEntitlementMaster "
			 +"WHERE "
			 +"	tSpecimanMaster.orgId = :orgId "
			 +"	AND tSpecimanMaster.orgUnitId = :orgUnitId "
			 +"	AND tSpecimanMaster.isDeleted = 'N' "
			 +"	AND labSampleDetailsMst.sampleStatusId = :sampleStatusId "),
	
	@NamedQuery(name = "GET_SPCIMEN_LIST_COUNT", query = "SELECT COUNT(*)"
			 +"	FROM TSpecimanMaster tSpecimanMaster "
			 +" INNER JOIN tSpecimanMaster.labSampleDetailsMaster labSampleDetailsMst "
	         +" INNER JOIN labSampleDetailsMst.labSampleMaster labSampleMst "
	         +" INNER JOIN labSampleMst.orderMaster ordermst "
			 +" INNER JOIN labSampleDetailsMst.testMaster testMst WITH testMst.isHistoCyto ='H'  "
			 +" INNER JOIN testMst.specimanMaster speciMst "
			 +" INNER JOIN labSampleDetailsMst.labSampleMaster labSampleMst "
			 +" INNER JOIN labSampleMst.visitTypeMaster visitMst "
			 +" INNER JOIN labSampleDetailsMst.patientRegistration patientReg "
			 +" INNER JOIN patientReg.prefixMaster prefixMst "
			 +" INNER JOIN labSampleDetailsMst.genderMaster genderMst "
			 +" INNER JOIN labSampleDetailsMst.orderMaster ordMst "
			+ " WHERE tSpecimanMaster.orgId = :orgId "
			+ " AND tSpecimanMaster.orgUnitId = :orgUnitId"
			+ " AND tSpecimanMaster.isDeleted ='N'"
			+ " AND labSampleDetailsMst.sampleStatusId = :sampleStatusId"),
	
	
	@NamedQuery(name = "SEARCH_ACCPTED_SPECIMEN_BY_PATIENT", query = 
			 " SELECT DISTINCT "
					 +"	patientReg.patientId AS id, "
					 +"	patientReg.firstName AS firstName , "
					 +" patientReg.lastName AS lastName , "
					 +"	patientReg.uhidNumber AS uhid "
					 +"FROM "
					 +"	TSpecimanMaster tSpecimanMaster "
					 +"INNER JOIN tSpecimanMaster.labSampleDetailsMaster labSampleDetailsMst "
					 +"INNER JOIN labSampleDetailsMst.patientRegistration patientReg "
					 +"WHERE "
					 +"	tSpecimanMaster.orgId = :orgId "
					 +"	AND tSpecimanMaster.orgUnitId = :orgUnitId "
					 +"	AND tSpecimanMaster.isDeleted = 'N' "
					 +"	AND labSampleDetailsMst.sampleStatusId = :sampleStatusId "
					 +"	AND ( LOWER( patientReg.firstName ) LIKE (:searchKeyword) "
					 +"	OR lower( patientReg.lastName ) LIKE lower(:searchKeyword) "
					 +"	OR lower( patientReg.uhidNumber ) LIKE lower(:searchKeyword) ) "),
	
	/*@NamedQuery(name = "SEARCH_SPECIMEN_ACCEPTED_TEST", query = 
			 " SELECT DISTINCT "
					 +"	testMst.testId AS id, "
					 +"	testMst.testDesc AS label "
					 +"FROM "
					 +"	TSpecimanMaster tSpecimanMaster "
					 +"INNER JOIN tSpecimanMaster.labSampleDetailsMaster labSampleDetailsMst "
					 +"INNER JOIN labSampleDetailsMst.testMaster testMst "
					 +"WHERE "
					 +"	tSpecimanMaster.orgId = :orgId "
					 +"	AND tSpecimanMaster.orgUnitId = :orgUnitId "
					 +"	AND tSpecimanMaster.isDeleted = 'N' "
					 +"	AND labSampleDetailsMst.sampleStatusId = :sampleStatusId " )*/
	
	
	
})


@Entity
@Table(name = "t_speciaman_master", schema = "lab")
@SequenceGenerator(name = "m_seq_speciman_master", sequenceName = "lab.m_seq_speciman_master", allocationSize = 1)
public class TSpecimanMaster {
	
	@Id
	@Column(name = "t_speciman_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_seq_speciman_master")
	private int tSpecimanId;
	
	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "org_unit_id")
	private Integer orgUnitId;

	@Column(name = "speciman_examinination")
	private String specimanExaminination;
	
	@Column(name = "capture_note")
	private String captureNote;

	@Column(name = "lab_sample_dtls_id")
	private Integer labSampleDtlsId;
	
	@Column(name = "histopathlogy_number")
	private String histopathlogyNumber;
	
	@Column(name = "barcode_no")
	private String barcodeNo;
	
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
	
	@Column(name = "speciman_type_id")
	private Integer specimanTypeId;
	
	@Column(name = "speciman_id")
	private Integer specimanId;
	
	@Column(name = "speciman_name")
	private String specimanName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lab_sample_dtls_id", insertable = false, nullable = false, updatable = false)
	private LabSampleDetailsMaster labSampleDetailsMaster;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tSpecimanMaster")
	private List<TSubSpecimanMaster> listTSubSpecimanMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "speciman_type_id", insertable = false, nullable = false, updatable = false)
	private SpecimanMaster specimanMaster;

	public int gettSpecimanId() {
		return tSpecimanId;
	}

	public void settSpecimanId(int tSpecimanId) {
		this.tSpecimanId = tSpecimanId;
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

	public String getSpecimanExaminination() {
		return specimanExaminination;
	}

	public void setSpecimanExaminination(String specimanExaminination) {
		this.specimanExaminination = specimanExaminination;
	}

	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	public String getHistopathlogyNumber() {
		return histopathlogyNumber;
	}

	public void setHistopathlogyNumber(String histopathlogyNumber) {
		this.histopathlogyNumber = histopathlogyNumber;
	}

	public String getBarcodeNo() {
		return barcodeNo;
	}

	public void setBarcodeNo(String barcodeNo) {
		this.barcodeNo = barcodeNo;
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

	public LabSampleDetailsMaster getLabSampleDetailsMaster() {
		return labSampleDetailsMaster;
	}

	public void setLabSampleDetailsMaster(LabSampleDetailsMaster labSampleDetailsMaster) {
		this.labSampleDetailsMaster = labSampleDetailsMaster;
	}


	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public List<TSubSpecimanMaster> getListTSubSpecimanMaster() {
		return listTSubSpecimanMaster;
	}

	public void setListTSubSpecimanMaster(List<TSubSpecimanMaster> listTSubSpecimanMaster) {
		this.listTSubSpecimanMaster = listTSubSpecimanMaster;
	}

	public Integer getSpecimanTypeId() {
		return specimanTypeId;
	}

	public void setSpecimanTypeId(Integer specimanTypeId) {
		this.specimanTypeId = specimanTypeId;
	}

	public Integer getSpecimanId() {
		return specimanId;
	}

	public void setSpecimanId(Integer specimanId) {
		this.specimanId = specimanId;
	}

	public String getSpecimanName() {
		return specimanName;
	}

	public void setSpecimanName(String specimanName) {
		this.specimanName = specimanName;
	}

	public String getCaptureNote() {
		return captureNote;
	}

	public void setCaptureNote(String captureNote) {
		this.captureNote = captureNote;
	}

	public SpecimanMaster getSpecimanMaster() {
		return specimanMaster;
	}

	public void setSpecimanMaster(SpecimanMaster specimanMaster) {
		this.specimanMaster = specimanMaster;
	}
	
}
