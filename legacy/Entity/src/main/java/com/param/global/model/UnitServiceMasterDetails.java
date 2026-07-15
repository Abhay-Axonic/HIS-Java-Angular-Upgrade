package com.param.global.model;

import java.math.BigDecimal;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.DateConverter;

@NamedNativeQueries({
		@NamedNativeQuery(name = "GET_PAGINATED_UNIT_SERVICE_MASTER_DETAILS_LIST", query = "SELECT details.unit_service_master_details_id as \"unitServiceMasterDetailsId\",  details.service_master_id as \"serviceMasterId\","
				+ " details.is_outsource as \"isOutsource\", "+ " 	details.is_outsource_job as \"isOutsourceJob\", "
				+ " details.surgery_grade_id as \"surgeryGradeId\", msgm.surgery_grade as \"surgeryGrade\", details.is_cyclic as \"isCyclicFlag\", details.is_package as \"isPackage\", details.is_rate_editable as \"isRateEditable\","
				+ " details.rate_editble_min_value as \"rateEditbleMinValue\", details.rate_editable_max_value as \"rateEditableMaxValue\","
				+ " details.is_tax_applicable as \"isTaxApplicable\", details.is_ref_doctor_share as \"isRefDoctorShare\",details.procedure_type_id as\"procedureTypeId\",details.is_pacs_req as \"isPacsReq\" , "
				+ " details.is_discount as \"isDiscount\", details.gst_type_id as \"gstTypeId\", details.otc_gst_type_id as \"otcGstTypeId\","
				+ " service.service_standard_name as \"serviceStandardName\", service.service_standard_code as \"serviceStandardCode\",speciality.speciality_name as \"specialityName\","
				+ " subspe.sub_speciality_name as\"subSpecialityName\", "
				+ " subspe.sub_speciality_id as\"subSpecialityId\", "
				+ " coalesce(msbpm.cost_of_service,0) as \"costOfService\", "
				+ " msbpm.base_price as\"basePrice\", details.is_doc_share_per_wise as \"isDocSharePerWise\" , details.is_ref_share_per_wise as \"isRefSharePerWise\",  "
				+ "	details.is_form_f_applicable as \"isFormFApplicable\"  "
				+ " FROM service.t_unit_service_master_details details "
				+ " INNER JOIN service.m_service_master service ON details.service_master_id = service.service_master_id"
				+ " left join public.m_surgery_grade_master msgm on " + 
				"	details.surgery_grade_id = msgm.surgery_grade_id "
				+ " INNER JOIN  doctor.m_speciality_master speciality ON speciality.speciality_id = service.speciality_id"
				+ " INNER JOIN doctor.m_sub_speciality_master subspe ON subspe.sub_speciality_id = service.sub_speciality_id"
				+ " LEFT JOIN  service.m_service_base_price_master msbpm ON details.service_master_id = msbpm.service_master_id	and msbpm.unit_id = details.unit_id "
				+ " WHERE details.organization_id =:orgId " + " AND details.unit_id =:unitId "
				+ " AND details.status = 'A' " + " ORDER BY details.unit_service_master_details_id DESC "),
		
		@NamedNativeQuery(name = "GET_SERVICE_MASTER_DETAILS_LIST_FOR_CYCLIC", query = "SELECT details.unit_service_master_details_id as \"unitServiceMasterDetailsId\",  details.service_master_id as \"serviceMasterId\","
				+ " details.is_outsource as \"isOutsource\", details.is_cyclic as \"isCyclicFlag\", details.is_package as \"isPackage\", details.is_rate_editable as \"isRateEditable\","
				+ " details.rate_editble_min_value as \"rateEditbleMinValue\", details.rate_editable_max_value as \"rateEditableMaxValue\","
				+ " details.is_tax_applicable as \"isTaxApplicable\", details.is_ref_doctor_share as \"isRefDoctorShare\",details.procedure_type_id as\"procedureTypeId\",details.is_pacs_req as \"isPacsReq\" , "
				+ " details.is_discount as \"isDiscount\", details.gst_type_id as \"gstTypeId\", details.otc_gst_type_id as \"otcGstTypeId\","
				+ " service.service_standard_name as \"serviceStandardName\", service.service_standard_code as \"serviceStandardCode\",speciality.speciality_name as \"specialityName\","
				+ " subspe.sub_speciality_name as\"subSpecialityName\", "
				+ " subspe.sub_speciality_id as\"subSpecialityId\", "
				+ " coalesce(msbpm.cost_of_service,0) as \"costOfService\", "
				+ " msbpm.base_price as\"basePrice\" "
				+ " FROM service.t_unit_service_master_details details "
				+ " INNER JOIN service.m_service_master service ON details.service_master_id = service.service_master_id"
				+ " INNER JOIN  doctor.m_speciality_master speciality ON speciality.speciality_id = service.speciality_id"
				+ " INNER JOIN doctor.m_sub_speciality_master subspe ON subspe.sub_speciality_id = service.sub_speciality_id"
				+ " LEFT JOIN  service.m_service_base_price_master msbpm ON details.service_master_id = msbpm.service_master_id and msbpm.unit_id = details.unit_id 	 "
				+ " WHERE details.organization_id =:orgId " + " AND details.unit_id =:unitId "
				+ " AND details.status = 'A' AND details.is_cyclic = 'Y' ORDER BY details.unit_service_master_details_id DESC "),
		
		@NamedNativeQuery(name = "GET_SERVICE_MASTER_QTY_DETAILS", query = "select " 
				+"	msm.service_standard_name as \"serviceStandardName\", "  
				+"	msm.service_master_id as \"serviceMasterId\", " 
				+"	msm.service_standard_code as \"serviceStandardCode\", " 
				+"	sum(tpcsd.balance_package_quantity) as \"availableQty\", " 
				+"	mbm.bill_number as \"billNumber\", " 
				+"	to_char(mbm.bill_date_time,'DD-MM-YYYY') as \"billDate\","
				+ "	tpcsd.service_cap as   \"serviceAmount\","  
				+"	msm.speciality_id as \"specialityId\"," 
				+"	msm.sub_speciality_id as \"subSpecialityId\", " 
				+ " tax.id as \"taxId\"," 
				+"   tax.tax_percentage as \"taxPercentage\" ,"
				+ " pcm.package_id as \"packageId\","
				+ " pcm.package_consumption_master_id as \"packageConsumptionMasterId\","
				+ " tpcsd.pacakge_consumption_service_detail_id as \"packageConsumptionServiceDetailId\","
				+ " tpcsd.package_eo_detail_id as \"packageEoDetailId\","
				+ " mbm.billing_master_id as \"id\" ,"
				+ " om.encounter_id as \"encounterId\" ,om.admission_id as \"admissionId\", "
				+ " (SELECT CAST(DATE_PART('day',cast(pcm.validity_to as timestamp)  - cast(pcm.validity_from as timestamp)) AS int4)) as \"packageDuration\","
				//cast(pcm.validity_from as timestamp)) 
				+ " to_char(pcm.validity_to,'YYYY-MM-DD') as \"packageValidityToDate\",om.order_id as \"orderId\",om.visit_type_id as \"visitTypeId\" "
				+" from "  
				+"	public.t_order_master as om " 
				+"inner join public.t_order_details as od on " 
				+"	om.order_id = od.order_id " 
				+"inner join billing.t_package_consumption_master as pcm on " 
				+"	od.package_id = pcm.package_id and pcm.package_type_id=5 "  
				+"	and od.is_package = true and pcm.patient_id=:patientId and pcm.order_detail_id=od.order_details_id "  
				+"inner join billing.t_package_consumption_service_detail as tpcsd on " 
				+"	pcm.package_consumption_master_id = tpcsd.package_consumption_master_id "  
				+"inner join service.m_service_master as msm on " 
				+"	msm.service_master_id = tpcsd.service_id " 
				+"inner join service.t_unit_service_master_details as tusmd on "
				+"	tusmd.service_master_id = msm.service_master_id "  
				+"left join billing.t_billing_details as tbd on "  
				+"	tbd.order_details_id = od.order_details_id "  
				+"left join billing.m_billing_master as mbm on "  
				+"	tbd.billing_master_id = mbm.billing_master_id and mbm.bill_cancelled = 'N' " 
				+ " left join public.m_tax tax on tax.id=tusmd.gst_type_id "
				+ " left join t_unit_application_billing_configuration appConfig on "
				+ " appConfig.unit_id = om.org_unit_id "
				+"where "  
				+"	om.patient_id =:patientId "  
				+"	and tusmd.organization_id =:orgId " 
				+"	and tusmd.unit_id =:unitId " 
				+"	and tusmd.status = 'A' " 
				+"	and od.status = 'A' " 
				+"	and tusmd.is_cyclic = 'Y' "
				//+"  and od.is_from_addservice='Y' " 
				+"	and tpcsd.balance_package_quantity>1 "
				+ "	and ((om.visit_type_id = 1	and mbm.bill_status_id != 1 and tbd.status='A' and appConfig.is_pre_or_post_billing='Y') "
				+ "	or  (om.visit_type_id = 1 and appConfig.is_pre_or_post_billing='N') or (om.visit_type_id != 1)) "
				+ "	group by od.service_id,tpcsd.balance_package_quantity,tpcsd.service_cap,msm.service_standard_name,msm.service_master_id,mbm.bill_number,mbm.bill_date_time,msm.speciality_id " 
				+"	,msm.sub_speciality_id,tax.tax_percentage,tax.id,mbm.billing_master_id,"
				+ "pcm.package_id,pcm.package_consumption_master_id,tpcsd.pacakge_consumption_service_detail_id,tpcsd.package_eo_detail_id,om.encounter_id,om.order_id "
				
				+" union "  
				+"select " 
				+"	msm.service_standard_name as \"serviceStandardName\", "  
				+"	msm.service_master_id as \"serviceMasterId\", "
				+"	msm.service_standard_code as \"serviceStandardCode\", " 
				+"	sum(od.order_qty) as \"availableQty\", " 
				+"	mbm.bill_number as \"billNumber\", " 
				+"	to_char(mbm.bill_date_time,'DD-MM-YYYY') as \"billDate\","
				//+ "	od.service_amt as   \"serviceAmount\","
				+ "	0 as   \"serviceAmount\","
				+"	msm.speciality_id as \"specialityId\"," 
				+"	msm.sub_speciality_id as \"subSpecialityId\","
				+ " tax.id as \"taxId\"," 
				+"   tax.tax_percentage as \"taxPercentage\","
				+ " 0 as \"packageId\","
				+ " 0 as \"packageConsumptionMasterId\","
				+ " 0 as \"packageConsumptionServiceDetailId\","
				+ " 0 as \"packageEoDetailId\","
				+ " mbm.billing_master_id as \"id\","
				+ "  om.encounter_id as \"encounterId\",om.admission_id as \"admissionId\","
				+ " 0 as \"packageDuration\", "
				+ " '-' as \"packageValidityToDate\",om.order_id as \"orderId\",om.visit_type_id as \"visitTypeId\" "
				+" from " 
				+"	public.t_order_master as om " 
				+"inner join public.t_order_details as od on "  
				+"	om.order_id = od.order_id "  
				+"inner join service.m_service_master as msm on " 
				+"	msm.service_master_id = od.service_id "  
				+"inner join service.t_unit_service_master_details as tusmd on "  
				+"	tusmd.service_master_id = msm.service_master_id "  
				+"left join billing.t_billing_details as tbd on " 
				+"	tbd.order_details_id = od.order_details_id " 
				+"left join billing.m_billing_master as mbm on " 
				+"	tbd.billing_master_id = mbm.billing_master_id and mbm.bill_cancelled = 'N' "
//				+"	and tbd.is_interim_or_final_bill = 2 "
				+ " left join public.m_tax tax on tax.id=tusmd.gst_type_id " 
				+ " left join t_unit_application_billing_configuration appConfig on "
				+ " appConfig.unit_id = om.org_unit_id "
				+"where "  
				+"	om.patient_id =:patientId"  
				+"	and tusmd.organization_id =:orgId" 
				+"	and tusmd.unit_id =:unitId "  
				+"	and tusmd.status = 'A' "  
				+"	and od.status = 'A' "  
				+"	and tusmd.is_cyclic = 'Y'"
				//+" and od.is_from_addservice='Y' " 
				+ "	and ((om.visit_type_id = 1	and mbm.bill_status_id != 1 and tbd.status='A' and appConfig.is_pre_or_post_billing='Y') "
				+ "	or  (om.visit_type_id = 1 and appConfig.is_pre_or_post_billing='N') or (om.visit_type_id != 1)) "
				+ "	group by od.service_id,msm.service_standard_name,msm.service_master_id,mbm.bill_number,mbm.bill_date_time,msm.speciality_id " 
				+"	,msm.sub_speciality_id,tax.tax_percentage,tax.id,mbm.billing_master_id,om.encounter_id,om.order_id having sum(od.order_qty)>1 "),
		
		
		@NamedNativeQuery(name = "GET_SERVICE_MASTER_QTY_DETAILS_FOR_SCHEDULING_WORKING", query = "select " 
				+"	msm.service_standard_name as \"serviceStandardName\", "  
				+"	msm.service_master_id as \"serviceMasterId\", " 
				+"	msm.service_standard_code as \"serviceStandardCode\", " 
				+"	sum(tpcsd.balance_package_quantity) as \"availableQty\", " 
				+"	mbm.bill_number as \"billNumber\", " 
				+"	to_char(mbm.bill_date_time,'DD-MM-YYYY') as \"billDate\","
				+ "	tpcsd.service_cap as   \"serviceAmount\","  
				+"	msm.speciality_id as \"specialityId\"," 
				+"	msm.sub_speciality_id as \"subSpecialityId\", " 
				+ " tax.id as \"taxId\"," 
				+"   tax.tax_percentage as \"taxPercentage\" ,"
				+ " pcm.package_id as \"packageId\","
				+ " pcm.package_consumption_master_id as \"packageConsumptionMasterId\","
				+ " tpcsd.pacakge_consumption_service_detail_id as \"packageConsumptionServiceDetailId\","
				+ " tpcsd.package_eo_detail_id as \"packageEoDetailId\","
				+ " mbm.billing_master_id as \"id\" ,"
				+ " om.encounter_id as \"encounterId\" ,om.admission_id as \"admissionId\", "
				+ " (SELECT CAST(DATE_PART('day',cast(pcm.validity_to as timestamp)  - cast(pcm.validity_from as timestamp)) AS int4)) as \"packageDuration\","
				+ " to_char(pcm.validity_to,'YYYY-MM-DD') as \"packageValidityToDate\",om.order_id as \"orderId\",om.visit_type_id as \"visitTypeId\" "
				+" from "  
				+"	public.t_order_master as om " 
				+"inner join public.t_order_details as od on " 
				+"	om.order_id = od.order_id " 
				+"inner join billing.t_package_consumption_master as pcm on " 
				+"	od.package_id = pcm.package_id and pcm.package_type_id=5 "  
				+"	and od.is_package = true and pcm.patient_id=:patientId and pcm.order_detail_id=od.order_details_id "  
				+"inner join billing.t_package_consumption_service_detail as tpcsd on " 
				+"	pcm.package_consumption_master_id = tpcsd.package_consumption_master_id "  
				+"inner join service.m_service_master as msm on " 
				+"	msm.service_master_id = tpcsd.service_id " 
				+"inner join service.t_unit_service_master_details as tusmd on "
				+"	tusmd.service_master_id = msm.service_master_id "
				+"left join billing.t_billing_details as tbd on "  
				+"	tbd.order_details_id = od.order_details_id "  
				+"left join billing.m_billing_master as mbm on "  
				+"	tbd.billing_master_id = mbm.billing_master_id and mbm.bill_cancelled = 'N' " 
				+ " left join public.m_tax tax on tax.id=tusmd.gst_type_id "
				+ " left join t_unit_application_billing_configuration appConfig on "
				+ " appConfig.unit_id = om.org_unit_id "
				+"where "  
				+"	om.patient_id =:patientId "  
				+"	and tusmd.organization_id =:orgId " 
				+"	and tusmd.unit_id =:unitId " 
				+ " and to_char(mbm.bill_date_time, 'YYYY-MM-DD') between :fromDate and :toDate  "  
				+"	and tusmd.status = 'A' " 
				+"	and od.status = 'A' " 
				+"	and tusmd.is_appointable = 'Y' "
				+"	and tpcsd.balance_package_quantity>1 "
				+ "	and ((om.visit_type_id = 1	and mbm.bill_status_id != 1 and tbd.status='A' and appConfig.is_pre_or_post_billing='Y') "
				+ "	or  (om.visit_type_id = 1 and appConfig.is_pre_or_post_billing='N') or (om.visit_type_id != 1)) "
				+ "	group by od.service_id,tpcsd.balance_package_quantity,tpcsd.service_cap,msm.service_standard_name,msm.service_master_id,mbm.bill_number,mbm.bill_date_time,msm.speciality_id " 
				+"	,msm.sub_speciality_id,tax.tax_percentage,tax.id,mbm.billing_master_id,"
				+ "pcm.package_id,pcm.package_consumption_master_id,tpcsd.pacakge_consumption_service_detail_id,tpcsd.package_eo_detail_id,om.encounter_id,om.order_id "
				
				+" union "  
				+"select " 
				+"	msm.service_standard_name as \"serviceStandardName\", "  
				+"	msm.service_master_id as \"serviceMasterId\", "
				+"	msm.service_standard_code as \"serviceStandardCode\", " 
				+"	sum(od.order_qty) as \"availableQty\", " 
				+"	mbm.bill_number as \"billNumber\", " 
				+"	to_char(mbm.bill_date_time,'DD-MM-YYYY') as \"billDate\","
				+ "	0 as   \"serviceAmount\","
				+"	msm.speciality_id as \"specialityId\"," 
				+"	msm.sub_speciality_id as \"subSpecialityId\","
				+ " tax.id as \"taxId\"," 
				+"   tax.tax_percentage as \"taxPercentage\","
				+ " 0 as \"packageId\","
				+ " 0 as \"packageConsumptionMasterId\","
				+ " 0 as \"packageConsumptionServiceDetailId\","
				+ " 0 as \"packageEoDetailId\","
				+ " mbm.billing_master_id as \"id\","
				+ "  om.encounter_id as \"encounterId\",om.admission_id as \"admissionId\","
				+ " 0 as \"packageDuration\", "
				+ " '-' as \"packageValidityToDate\",om.order_id as \"orderId\",om.visit_type_id as \"visitTypeId\" "
				+" from " 
				+"	public.t_order_master as om " 
				+"inner join public.t_order_details as od on "  
				+"	om.order_id = od.order_id "  
				+"inner join service.m_service_master as msm on " 
				+"	msm.service_master_id = od.service_id "  
				+"inner join service.t_unit_service_master_details as tusmd on "  
				+"	tusmd.service_master_id = msm.service_master_id "  
				+"left join billing.t_billing_details as tbd on " 
				+"	tbd.order_details_id = od.order_details_id " 
				+"left join billing.m_billing_master as mbm on " 
				+"	tbd.billing_master_id = mbm.billing_master_id and mbm.bill_cancelled = 'N' "
				+ " left join public.m_tax tax on tax.id=tusmd.gst_type_id " 
				+ " left join t_unit_application_billing_configuration appConfig on "
				+ " appConfig.unit_id = om.org_unit_id "
				+"where "  
				+"	om.patient_id =:patientId"  
				+"	and tusmd.organization_id =:orgId" 
				+"	and tusmd.unit_id =:unitId"
				+ " and to_char(mbm.bill_date_time, 'YYYY-MM-DD') between :fromDate and :toDate  "  
				+"	and tusmd.status = 'A' "  
				+"	and od.status = 'A' "  
				+"	and tusmd.is_appointable = 'Y'"
				+ "	and ((om.visit_type_id = 1	and mbm.bill_status_id != 1 and tbd.status='A' and appConfig.is_pre_or_post_billing='Y') "
				+ "	or  (om.visit_type_id = 1 and appConfig.is_pre_or_post_billing='N') or (om.visit_type_id != 1)) "
				+ "	group by od.service_id,msm.service_standard_name,msm.service_master_id,mbm.bill_number,mbm.bill_date_time,msm.speciality_id " 
				+"	,msm.sub_speciality_id,tax.tax_percentage,tax.id,mbm.billing_master_id,om.encounter_id,om.order_id "),
		
		
		
		@NamedNativeQuery(name = "GET_SERVICE_MASTER_QTY_DETAILS_FOR_SCHEDULING_WORKING_LIST", query = "select " 
				+"	msm.service_standard_name as \"serviceStandardName\", "  
				+"	msm.service_master_id as \"serviceMasterId\", " 
				+"	msm.service_standard_code as \"serviceStandardCode\", " 
				+"	mbm.bill_number as \"billNumber\", " 
				+"	to_char(mbm.bill_date_time,'DD-MM-YYYY') as \"billDate\","
				+ "	tpcsd.service_cap as   \"serviceAmount\","  
				+"	msm.speciality_id as \"specialityId\"," 
				+"	msm.sub_speciality_id as \"subSpecialityId\", " 
				+ " tax.id as \"taxId\"," 
				+"   tax.tax_percentage as \"taxPercentage\" ,"
				+ " pcm.package_id as \"packageId\","
				+ " pcm.package_consumption_master_id as \"packageConsumptionMasterId\","
				+ " tpcsd.pacakge_consumption_service_detail_id as \"packageConsumptionServiceDetailId\","
				+ " tpcsd.package_eo_detail_id as \"packageEoDetailId\","
				+ " mbm.billing_master_id as \"id\" ,"
				+ " om.encounter_id as \"encounterId\" ,om.admission_id as \"admissionId\", "
				+ " od.order_details_id as \"orderDetailsId\", "
				+ " (SELECT CAST(DATE_PART('day',cast(pcm.validity_to as timestamp)  - cast(pcm.validity_from as timestamp)) AS int4)) as \"packageDuration\","
				+ " to_char(pcm.validity_to,'YYYY-MM-DD') as \"packageValidityToDate\",om.order_id as \"orderId\",om.visit_type_id as \"visitTypeId\" "
				+" from "  
				+"	public.t_order_details as od  " 
				+"inner join public.t_order_master as om on " 
				+"	om.order_id = od.order_id " 
				+"inner join billing.t_package_consumption_master as pcm on " 
				+"	od.package_id = pcm.package_id and pcm.package_type_id=5 "  
				+"	and od.is_package = true and pcm.order_detail_id=od.order_details_id "  
				+"inner join billing.t_package_consumption_service_detail as tpcsd on " 
				+"	pcm.package_consumption_master_id = tpcsd.package_consumption_master_id "  
				+"inner join service.m_service_master as msm on " 
				+"	msm.service_master_id = tpcsd.service_id " 
				+"inner join service.t_unit_service_master_details as tusmd on "
				+"	tusmd.service_master_id = msm.service_master_id "
				+"left join billing.t_billing_details as tbd on "  
				+"	tbd.order_details_id = od.order_details_id "  
				+"left join billing.m_billing_master as mbm on "  
				+"	tbd.billing_master_id = mbm.billing_master_id and mbm.bill_cancelled = 'N' " 
				+ " left join public.m_tax tax on tax.id=tusmd.gst_type_id "
				+ " left join t_unit_application_billing_configuration appConfig on "
				+ " appConfig.unit_id = om.org_unit_id "
				+"where "  
				+"  om.order_id in(:orderIds) and tusmd.organization_id =:orgId " 
				+"	and tusmd.unit_id =:unitId " 
				+ " and tusmd.status = 'A' " 
				+"	and od.status = 'A' " 
				+"	and tusmd.is_appointable = 'Y' "
				+"	and tpcsd.balance_package_quantity>1 "
				+ "	and ((om.visit_type_id = 1	and mbm.bill_status_id != 1 and tbd.status='A' and appConfig.is_pre_or_post_billing='Y') "
				+ "	or  (om.visit_type_id = 1 and appConfig.is_pre_or_post_billing='N') or (om.visit_type_id != 1))  "
				+ " and od.order_details_id not in(select order_detail_id from opd.t_cyclic_appointment_mapper where order_id in(:orderIds) ) "
				+ " union all "  
				+"select " 
				+"	msm.service_standard_name as \"serviceStandardName\", "  
				+"	msm.service_master_id as \"serviceMasterId\", "
				+"	msm.service_standard_code as \"serviceStandardCode\", " 
				+"	mbm.bill_number as \"billNumber\", " 
				+"	to_char(mbm.bill_date_time,'DD-MM-YYYY') as \"billDate\","
				+ "	0 as   \"serviceAmount\","
				+"	msm.speciality_id as \"specialityId\"," 
				+"	msm.sub_speciality_id as \"subSpecialityId\","
				+ " tax.id as \"taxId\"," 
				+"   tax.tax_percentage as \"taxPercentage\","
				+ " 0 as \"packageId\","
				+ " 0 as \"packageConsumptionMasterId\","
				+ " 0 as \"packageConsumptionServiceDetailId\","
				+ " 0 as \"packageEoDetailId\","
				+ " mbm.billing_master_id as \"id\","
				+ "  om.encounter_id as \"encounterId\",om.admission_id as \"admissionId\","
				+ " od.order_details_id as \"orderDetailsId\", "
				+ " 0 as \"packageDuration\", "
				+ " '-' as \"packageValidityToDate\",om.order_id as \"orderId\",om.visit_type_id as \"visitTypeId\" "
				+" from " 
				+"	public.t_order_details as od  " 
				+"inner join public.t_order_master as om on "  
				+"	om.order_id = od.order_id "  
				+"inner join service.m_service_master as msm on " 
				+"	msm.service_master_id = od.service_id "  
				+"inner join service.t_unit_service_master_details as tusmd on "  
				+"	tusmd.service_master_id = msm.service_master_id "  
				+"left join billing.t_billing_details as tbd on " 
				+"	tbd.order_details_id = od.order_details_id " 
				+"left join billing.m_billing_master as mbm on " 
				+"	tbd.billing_master_id = mbm.billing_master_id and mbm.bill_cancelled = 'N' "
				+ " left join public.m_tax tax on tax.id=tusmd.gst_type_id " 
				+ " left join t_unit_application_billing_configuration appConfig on "
				+ " appConfig.unit_id = om.org_unit_id "
				+"where "  
				+"	 om.order_id in(:orderIds) and tusmd.organization_id =:orgId" 
				+"	and tusmd.unit_id =:unitId"
				+ " and tusmd.status = 'A' "  
				+"	and od.status = 'A' "  
				+"	and tusmd.is_appointable = 'Y'"
				+ "	and ((om.visit_type_id = 1	and mbm.bill_status_id != 1 and tbd.status='A' and appConfig.is_pre_or_post_billing='Y') "
				+ "	or  (om.visit_type_id = 1 and appConfig.is_pre_or_post_billing='N') or (om.visit_type_id != 1)) "
				+ " and od.order_details_id not in(select order_detail_id from opd.t_cyclic_appointment_mapper where order_id in(:orderIds) ) "
				+ " "),
		
		

		@NamedNativeQuery(name = "GET_UNIT_SERVICE_MASTER_DETAILS_COUNT", query = "SELECT count(*) "
				+ " FROM service.t_unit_service_master_details details"
				+ " INNER JOIN service.m_service_master service ON details.service_master_id = service.service_master_id"
				+ " INNER JOIN  doctor.m_speciality_master speciality ON speciality.speciality_id = service.speciality_id"
				+ " INNER JOIN doctor.m_sub_speciality_master subspe ON subspe.sub_speciality_id = service.sub_speciality_id"
				+ " WHERE details.organization_id =:orgId " + " AND details.unit_id =:unitId "
				+ " AND details.status = 'A' "),

		@NamedNativeQuery(name = "GET_SERVICE_MASTER_DETAILS_BY_ID", query =    " select "
				 + " 	details.unit_service_master_details_id as \"unitServiceMasterDetailsId\", "
				 + " 	details.service_master_id as \"serviceMasterId\", "
				 + " 	details.is_outsource as \"isOutsource\", "
				 + " 	details.is_outsource_job as \"isOutsourceJob\", " 
				 + " 	details.is_cyclic as \"isCyclicFlag\", "
				 + " 	details.is_package as \"isPackage\",  details.surgery_grade_id as \"surgeryGradeId\","
				 + "    msgm.surgery_grade as \"surgeryGrade\","
				 + " 	details.is_rate_editable as \"isRateEditable\", "
				 + " 	details.rate_editble_min_value as \"rateEditbleMinValue\", "
				 + " 	details.rate_editable_max_value as \"rateEditableMaxValue\", "
				 + " 	details.is_tax_applicable as \"isTaxApplicable\", "
				 + " 	details.is_ref_doctor_share as \"isRefDoctorShare\", "
				 + " 	details.is_auto_render as \"isAutoRender\", "
				 + " 	details.is_allow_multiple_quantity as \"isQuantityEditable\", "
				 + " 	details.is_panel as \"isPanel\", "
				 + " 	details.is_discount as \"isDiscount\", "
				 + " 	details.is_procedure as \"isProcedure\", "
				 + " 	details.is_doc_req as \"isDocReq\", "
				 + " 	details.is_bed_side as \"isBedSide\", "
				 + " 	details.is_poc as \"isPoc\", "
				 + " 	details.is_periodicity as \"isPeriodicity\", "
				 + " 	details.discount_value as \"discountValue\" , "
				 + " 	details.gst_type_id as \"gstTypeId\", "
				 + " 	details.otc_gst_type_id as \"otcGstTypeId\", "
				 + " 	details.organization_id as \"organizationId\", "
				 + " 	details.unit_id as \"unitId\", "
				 + " 	details.created_by as \"createdBy\", "
				 + " 	details.status as \"status\", "
				 + " 	details.updated_by as \"updatedBy\", "
				 + " 	details.procedure_type_id as \"procedureTypeId\", "
				 + " 	details.is_pacs_req as \"isPacsReq\" , "
				 + " 	service.service_standard_name as \"serviceStandardName\", "
				 + " 	service.service_standard_code as \"serviceStandardCode\", "
				 + " 	service.speciality_id as \"specialityId\", "
				 + " 	service.sub_speciality_id as \"subSpecialityId\", "
				 + " 	speciality.speciality_name as \"specialityName\", "
				 + " 	sub_speciality.sub_speciality_name as \"subSpecialityName\", "
				 + " 	details.is_appointable as \"isAppointable\", "
				 + " 	details.gender as \"gender\", "
				 + " 	details.ref_share_amt as \"refShareAmt\", "
				 + " 	details.ref_share_per as \"refSharePer\", "
				 + " 	details.is_review_applicable as \"isReviewApplicable\", "
				 + " 	details.review_time as \"reviewTime\", "
				 + " 	details.bill_of_material as \"billOfMaterial\", "
				 + " 	details.doc_ref_per as \"docRefPer\", "
				 + " 	coalesce(msbpm.base_price,0) as \"basePrice\", "
				 + " 	coalesce(msbpm.cost_of_service,0) as \"costOfService\", "
				 + "	msbpm.service_base_price_id as \"serviceBasePriceId\","
				 + "    details.sample_collection as \"sampleCollection\", "
				 + "	details.is_doc_share_per_wise as \"isDocSharePerWise\" , "
				 + "	details.is_ref_share_per_wise as \"isRefSharePerWise\" , "
				 + "	details.lab_auto_mail_exclusion as \"labAutoMailExclusion\" , "
				 + "	details.pre_requisite as \"preRequisite\",  "
				 + "	details.pre_requisite_details as \"preRequisiteDetails\",  "
				 + "	details.ventilator_requiered as \"ventilatorRequiered\",  "
				 + "	details.is_form_f_applicable as \"isFormFApplicable\",  "
				 + "	details.is_admin_service as \"isAdminService\"  "
				 + " from "
				 + " 	service.t_unit_service_master_details details "
				 + " inner join service.m_service_master service on "
				 + " 	details.service_master_id = service.service_master_id "
				 + " inner join doctor.m_speciality_master speciality on "
				 + " 	service.speciality_id = speciality.speciality_id "
				 + " inner join doctor.m_sub_speciality_master sub_speciality on "
				 + " 	service.sub_speciality_id = sub_speciality.sub_speciality_id "
				 + " left join public.m_surgery_grade_master msgm on " + 
					"	details.surgery_grade_id = msgm.surgery_grade_id "
				 + " left join service.m_service_base_price_master msbpm on "
				 + " 	msbpm.service_master_id = details.service_master_id "
				 + "	and msbpm.unit_id = details.unit_id 	 "
				 + " where "
				 + "	msbpm.unit_id =details.unit_id "
				 + " 	and details.unit_service_master_details_id =:unitServiceMasterDetailsId "),
		
		@NamedNativeQuery(name = "GET_ALL_ACTIVE_TAX", query = "SELECT tax.id as \"id\",tax.name as \"name\" "
				+ "FROM public.m_tax tax "
				+ "WHERE tax.is_active = true AND tax.is_deleted = false AND tax.organization_id = :organizationId"),

		@NamedNativeQuery(name = "GET_PERIODICITY_SERVICES_FOR_SEARCH", query = " select "
				+ " 	srvmst.service_standard_name as \"serviceName\", "
				+ " 	srvmst.service_standard_code as \"serviceCode\", "
				+ " 	srvmst.service_master_id as \"serviceId\" " + " from " + " 	service.m_service_master srvmst "
				+ " inner join service.t_unit_service_master_details srvmppr on "
				+ " 	srvmppr.service_master_id = srvmst.service_master_id "
				+ " inner join service.m_periodicity_service_config perSer on "
				+ " 	perSer.service_id = srvmst.service_master_id " + " where " + " 	srvmppr.status = 'A' "
				+ " 	and srvmppr.is_periodicity = 'Y' " + " 	and srvmppr.unit_id = :unitId "
				// + " and srvmppr.organization_id = :orgId "

				+ " 	and (lower(trim(srvmst.service_standard_name)) like :serviceName "
				+ " 	or lower(trim(srvmst.service_standard_code)) like :serviceName ) "),

		@NamedNativeQuery(name = "GET_PROCEDURE_SERVICES_FOR_SEARCH", query = "select srvmst.service_standard_name as \"serviceName\", srvmst.service_standard_code as \"serviceCode\", srvmst.service_master_id as \"serviceId\","
				+ " spc.speciality_name as \"department\", subspc.sub_speciality_name as \"subDepartment\","
				+ " srvmst.codification_service_name as \"serviceStandardCode\"  "
				+ " from service.m_service_master srvmst "
				+ " inner join service.t_unit_service_master_details srvmppr "
				+ " on srvmppr.service_master_id = srvmst.service_master_id "
				+ " inner join doctor.m_speciality_master spc " + " on spc.speciality_id =  srvmst.speciality_id "
				+ " inner join doctor.m_sub_speciality_master subspc "
				+ " on subspc.sub_speciality_id = srvmst.sub_speciality_id "
				+ " where srvmppr.status = 'A' and srvmppr.is_procedure = 'Y'  "
				+ " and srvmppr.unit_id = :unitId and srvmppr.organization_id = :orgId "
				+ " and srvmst.speciality_id= :specialityId "
				+ " and (lower(trim(srvmst.service_standard_name)) like :serviceName "
				+ " or  lower(trim(srvmst.service_standard_code)) like :serviceName )"),

		@NamedNativeQuery(name = "GET_PAGINATED_UNIT_SERVICE_MASTER_DETAILS_LIST_SEARCH", query = "SELECT details.unit_service_master_details_id as \"unitServiceMasterDetailsId\",  details.service_master_id as \"serviceMasterId\","
				+ " details.is_outsource as \"isOutsource\",details.is_outsource_job as \"isOutsourceJob\", details.is_cyclic as \"isCyclicFlag\", details.is_package as \"isPackage\", details.is_rate_editable as \"isRateEditable\","
				+ " details.rate_editble_min_value as \"rateEditbleMinValue\", details.rate_editable_max_value as \"rateEditableMaxValue\","
				+ " details.is_tax_applicable as \"isTaxApplicable\", details.is_ref_doctor_share as \"isRefDoctorShare\",details.procedure_type_id as\"procedureTypeId\",details.is_pacs_req as \"isPacsReq\" , "
				+ " details.is_discount as \"isDiscount\", details.gst_type_id as \"gstTypeId\", details.otc_gst_type_id as \"otcGstTypeId\","
				+ " service.service_standard_name as \"serviceStandardName\", service.service_standard_code as \"serviceStandardCode\",speciality.speciality_name as \"specialityName\","
				+ " subspe.sub_speciality_name as\"subSpecialityName\","
				+ " coalesce(msbpm.cost_of_service,0) as \"costOfService\", "
				+ " msbpm.base_price as\"basePrice\" "
				+ " FROM service.t_unit_service_master_details details "
				+ " INNER JOIN service.m_service_master service ON details.service_master_id = service.service_master_id"
				+ " INNER JOIN  doctor.m_speciality_master speciality ON speciality.speciality_id = service.speciality_id"
				+ " INNER JOIN doctor.m_sub_speciality_master subspe ON subspe.sub_speciality_id = service.sub_speciality_id"
				+ " LEFT JOIN  service.m_service_base_price_master msbpm ON details.service_master_id = msbpm.service_master_id	and msbpm.unit_id = details.unit_id "
				+ " WHERE details.organization_id =:orgId"
				+ " AND (lower(trim(service.service_standard_name)) like :serviceName  or  lower(trim(service.service_standard_code)) like :serviceName )  "
				+ " AND details.unit_id =:unitId " + " AND details.status = 'A' "
				+ " ORDER BY details.unit_service_master_details_id DESC "), })

@Entity
@Table(name = "t_unit_service_master_details", schema = "service")
@SequenceGenerator(name = "unit_service_master_details_seq", sequenceName = "service.unit_service_master_details_seq", allocationSize = 1)
public class UnitServiceMasterDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unit_service_master_details_seq")
	@Column(name = "unit_service_master_details_id")
	private int unitServiceMasterDetailsId;

	@Column(name = "service_master_id")
	private Integer serviceMasterId;

	@Column(name = "is_outsource")
	private char isOutsource;
	
	@Column(name = "is_outsource_job")
	private char isOutsourceJob;

	@Column(name = "is_package")
	private char isPackage;

	@Column(name = "is_rate_editable")
	private char isRateEditable;

	@Column(name = "rate_editble_min_value")
	private BigDecimal rateEditbleMinValue;

	@Column(name = "rate_editable_max_value")
	private BigDecimal rateEditableMaxValue;

	@Column(name = "is_tax_applicable")
	private char isTaxApplicable;

	@Column(name = "is_ref_doctor_share")
	private char isRefDoctorShare;

	@Column(name = "is_discount")
	private char isDiscount;

	@Column(name = "discount_value")
	private BigDecimal discountValue;

	@Column(name = "gst_type_id")
	private Integer gstTypeId;

	@Column(name = "otc_gst_type_id")
	private Integer otcGstTypeId;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	@Convert(converter = DateConverter.class)
	private Long createdDate;

	@Column(name = "status")
	private char status;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "is_panel")
	private Character isPanel;

	@Column(name = "is_allow_multiple_quantity")
	private Character isQuantityEditable;

	@Column(name = "updated_date")
	@Convert(converter = DateConverter.class)
	private Long updatedDate;

	@Column(name = "is_procedure")
	private Character isProcedure;

	@Column(name = "is_doc_req")
	private Character isDocReq;

	@Column(name = "is_bed_side")
	private Character isBedSide;

	@Column(name = "is_poc")
	private Character isPoc;

	@Column(name = "is_periodicity")
	private Character isPeriodicity;

	@Column(name = "is_auto_render")
	private Character isAutoRender;

	@Column(name = "is_appointable")
	private Character isAppointable;

	@Column(name = "gender")
	private Integer gender;

	@Column(name = "ref_share_amt")
	private BigDecimal refShareAmt;

	@Column(name = "procedure_type_id")
	private Integer procedureTypeId;
	
	@Column(name = "surgery_grade_id")
	private Integer surgeryGradeId;

	@Column(name = "is_pacs_req")
	private Character isPacsReq;

	@Column(name = "is_review_applicable")
	private Character isReviewApplicable;

	@Column(name = "review_time")
	private int reviewTime;

	@Column(name = "ref_share_per")
	private BigDecimal refSharePer;

	@Column(name = "product_category_id")
	private Integer productCategoryId;

	@Column(name = "bill_of_material")
	private Character billOfMaterial;
	
	@Column(name = "bed_category_id")
	private Integer bedCategoryId;
	
	@Column(name = "doc_ref_per")
	private BigDecimal docRefPer;

	@Column(name = "is_cyclic")
	private Character isCyclicFlag;
	
	@Column(name = "sample_collection")
	private Character sampleCollection;
	
	@Column(name = "is_doc_share_per_wise")
	private Character isDocSharePerWise;
	
	@Column(name = "is_ref_share_per_wise")
	private Character isRefSharePerWise;
	
	@Column(name = "lab_auto_mail_exclusion")
	private Character labAutoMailExclusion;

	@Column(name = "pre_requisite")
	private Character preRequisite;
	
	@Column(name = "pre_requisite_details")
	private String preRequisiteDetails;
	
	
	@Column(name = "ventilator_requiered")
	private Character ventilatorRequiered;

	@Column(name = "is_form_f_applicable")
	private Character isFormFApplicable;

    @Column(name = "is_admin_service")
    private Character isAdminService;


    public Character getVentilatorRequiered() {
		return ventilatorRequiered;
	}

	public void setVentilatorRequiered(Character ventilatorRequiered) {
		this.ventilatorRequiered = ventilatorRequiered;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_master_id", insertable = false, updatable = false, nullable = false)
	private ServiceMaster serviceMaster;

	public Character getIsRefSharePerWise() {
		return isRefSharePerWise;
	}

	public void setIsRefSharePerWise(Character isRefSharePerWise) {
		this.isRefSharePerWise = isRefSharePerWise;
	}

	public Character getIsDocSharePerWise() {
		return isDocSharePerWise;
	}

	public void setIsDocSharePerWise(Character isDocSharePerWise) {
		this.isDocSharePerWise = isDocSharePerWise;
	}

	public BigDecimal getRefSharePer() {
		return refSharePer;
	}

	public void setRefSharePer(BigDecimal refSharePer) {
		this.refSharePer = refSharePer;
	}

	public BigDecimal getRefShareAmt() {
		return refShareAmt;
	}

	public void setRefShareAmt(BigDecimal refShareAmt) {
		this.refShareAmt = refShareAmt;
	}

	public Character getIsAppointable() {
		return isAppointable;
	}

	public void setIsAppointable(Character isAppointable) {
		this.isAppointable = (isAppointable == '\u0000') ? 'N' : isAppointable;
	}

	public Character getIsQuantityEditable() {
		return isQuantityEditable;
	}

	public void setIsQuantityEditable(Character isQuantityEditable) {
		this.isQuantityEditable = (isQuantityEditable == '\u0000') ? 'N' : isQuantityEditable;
	}

	public int getUnitServiceMasterDetailsId() {
		return unitServiceMasterDetailsId;
	}

	public void setUnitServiceMasterDetailsId(int unitServiceMasterDetailsId) {
		this.unitServiceMasterDetailsId = unitServiceMasterDetailsId;
	}

	public Integer getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(Integer serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}

	public char getIsOutsourceJob() {
		return isOutsourceJob;
	}

	public void setIsOutsourceJob(char isOutsourceJob) {
		this.isOutsourceJob = (isOutsourceJob == '\u0000') ? 'N' : isOutsourceJob;
	}

	public char getIsOutsource() {
		return isOutsource;
	}

	public void setIsOutsource(char isOutsource) {
		this.isOutsource = (isOutsource == '\u0000') ? 'N' : isOutsource;
	}

	public char getIsPackage() {
		return isPackage;
	}

	public void setIsPackage(char isPackage) {
		this.isPackage = (isPackage == '\u0000') ? 'I' : isPackage;
	}

	public char getIsRateEditable() {
		return isRateEditable;
	}

	public void setIsRateEditable(char isRateEditable) {
		this.isRateEditable = (isRateEditable == '\u0000') ? 'I' : isRateEditable;
	}

	public BigDecimal getRateEditbleMinValue() {
		return rateEditbleMinValue;
	}

	public void setRateEditbleMinValue(BigDecimal rateEditbleMinValue) {
		this.rateEditbleMinValue = rateEditbleMinValue;

	}

	public BigDecimal getRateEditableMaxValue() {
		return rateEditableMaxValue;
	}

	public void setRateEditableMaxValue(BigDecimal rateEditableMaxValue) {
		this.rateEditableMaxValue = rateEditableMaxValue;
	}

	public char getIsTaxApplicable() {
		return isTaxApplicable;
	}

	public void setIsTaxApplicable(char isTaxApplicable) {
		this.isTaxApplicable = (isTaxApplicable == '\u0000') ? 'I' : isTaxApplicable;
	}

	public char getIsRefDoctorShare() {
		return isRefDoctorShare;
	}

	public void setIsRefDoctorShare(char isRefDoctorShare) {
		this.isRefDoctorShare = (isRefDoctorShare == '\u0000') ? 'I' : isRefDoctorShare;
	}

	public char getIsDiscount() {
		return isDiscount;
	}

	public void setIsDiscount(char isDiscount) {
		this.isDiscount = (isDiscount == '\u0000') ? 'I' : isDiscount;
	}

	public Integer getGstTypeId() {
		return gstTypeId;
	}

	public void setGstTypeId(Integer gstTypeId) {
		this.gstTypeId = gstTypeId;
	}

	public Integer getOtcGstTypeId() {
		return otcGstTypeId;
	}

	public void setOtcGstTypeId(Integer otcGstTypeId) {
		this.otcGstTypeId = otcGstTypeId;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status == '\u0000') ? 'I' : status;
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

	public BigDecimal getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(BigDecimal discountValue) {
		this.discountValue = discountValue;
	}

	public Character getIsPanel() {
		return isPanel;
	}

	public void setIsPanel(Character isPanel) {
		this.isPanel = (isPanel == '\u0000') ? 'I' : isPanel;
	}

	public Character getIsProcedure() {
		return isProcedure;
	}

	public void setIsProcedure(Character isProcedure) {
		this.isProcedure = (isProcedure == '\u0000') ? 'I' : isProcedure;
	}

	public Character getIsDocReq() {
		return isDocReq;
	}

	public void setIsDocReq(Character isDocReq) {
		this.isDocReq = (isDocReq == '\u0000') ? 'I' : isDocReq;
	}

	public Character getIsBedSide() {
		return isBedSide;
	}

	public void setIsBedSide(Character isBedSide) {
		this.isBedSide = (isBedSide == '\u0000') ? 'I' : isBedSide;
	}

	public Character getIsPoc() {
		return isPoc;
	}

	public void setIsPoc(Character isPoc) {
		this.isPoc = (isPoc == '\u0000') ? 'I' : isPoc;
	}

	public Character getIsPeriodicity() {
		return isPeriodicity;
	}

	public void setIsPeriodicity(Character isPeriodicity) {
		this.isPeriodicity = (isPeriodicity == '\u0000') ? 'I' : isPeriodicity;
	}

	public ServiceMaster getServiceMaster() {
		return serviceMaster;
	}

	public void setServiceMaster(ServiceMaster serviceMaster) {
		this.serviceMaster = serviceMaster;
	}

	public Character getIsAutoRender() {
		return isAutoRender;
	}

	public void setIsAutoRender(Character isAutoRender) {
		this.isAutoRender = isAutoRender;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Integer getProcedureTypeId() {
		return procedureTypeId;
	}

	public void setProcedureTypeId(Integer procedureTypeId) {
		this.procedureTypeId = procedureTypeId;
	}

	public Character getIsPacsReq() {
		return isPacsReq;
	}

	public void setIsPacsReq(Character isPacsReq) {
		this.isPacsReq = isPacsReq;
	}

	public Character getIsReviewApplicable() {
		return isReviewApplicable;
	}

	public void setIsReviewApplicable(Character isReviewApplicable) {
		this.isReviewApplicable = (isReviewApplicable == null) ? 'N' : isReviewApplicable;
	}

	public int getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(int reviewTime) {
		this.reviewTime = reviewTime;
	}

	public Character getBillOfMaterial() {
		return billOfMaterial;
	}

	public void setBillOfMaterial(Character billOfMaterial) {
		this.billOfMaterial = billOfMaterial;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}
	
	public Character getIsCyclicFlag() {
		return isCyclicFlag;
	}

	public void setIsCyclicFlag(Character isCyclicFlag) {
		this.isCyclicFlag = (isCyclicFlag == null) ? 'N' : isCyclicFlag;
	}

	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}

	public BigDecimal getDocRefPer() {
		return docRefPer;
	}

	public void setDocRefPer(BigDecimal docRefPer) {
		this.docRefPer = docRefPer;
	}

	public Character getSampleCollection() {
		return sampleCollection;
	}

	public void setSampleCollection(Character sampleCollection) {
		this.sampleCollection = sampleCollection;
	}

	public Integer getSurgeryGradeId() {
		return surgeryGradeId;
	}

	public void setSurgeryGradeId(Integer surgeryGradeId) {
		this.surgeryGradeId = surgeryGradeId;
	}

	public Character getLabAutoMailExclusion() {
		return labAutoMailExclusion;
	}

	public void setLabAutoMailExclusion(Character labAutoMailExclusion) {
		this.labAutoMailExclusion = labAutoMailExclusion;
	}

	public Character getPreRequisite() {
		return preRequisite;
	}

	public void setPreRequisite(Character preRequisite) {
		this.preRequisite = preRequisite;
	}

	public String getPreRequisiteDetails() {
		return preRequisiteDetails;
	}

	public void setPreRequisiteDetails(String preRequisiteDetails) {
		this.preRequisiteDetails = preRequisiteDetails;
	}

	public Character getIsFormFApplicable() {
		return isFormFApplicable;
	}

	public void setIsFormFApplicable(Character isFormFApplicable) {
		this.isFormFApplicable = isFormFApplicable;
	}

    public Character getIsAdminService() {
        return isAdminService;
    }

    public void setIsAdminService(Character isAdminService) {
        this.isAdminService = isAdminService;
    }
}
