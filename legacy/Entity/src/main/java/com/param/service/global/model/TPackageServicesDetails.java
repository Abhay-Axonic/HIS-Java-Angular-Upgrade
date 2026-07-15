package com.param.service.global.model;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.ServiceMaster;

@NamedNativeQueries({ @NamedNativeQuery(name = "GET_SERVICE_DETAILS_BY_PACKAGE_ID_FOR_BILLING", query = " select "
		+ "	ps.package_service_details_id as \"packageServiceDetailsId\", "
		+ "	ps.consultation_speciality_id as \"consultationSpecialityId\", " 
		+ "	ps.package_id as \"packageId\", "
		+ "	ps.service_id as \"serviceId\", " 
		+ " ps.service_id as \"serviceMasterId\", "
		+ "	ps.number_to_be_use as \"numberToBeUse\", " 
		+ "	ps.service_price as \"servicePrice\", "
		+ "	coalesce(ps.apportioned_price, " 
		+ "	srvtrf.rate) as \"apportionedPrice\", "
		+ "	ps.package_either_or_group_id as \"packageEitherOrGroupId\", "
		+ "	ps.is_service_item as \"isServiceItem\", " 
		+ "	ps.item_id as \"itemId\", "
		+ "	item.\"name\" as \"itemName\", " 
		+ "	ps.periodicity_id as \"periodicityId\", "
		+ "	srv.service_standard_name as \"serviceName\", " 
		+ "	srv.service_standard_code as \"serviceCode\", "
		+ "	spc.speciality_id as \"specialityId\", "
//		+ " ps.consultation_speciality_id as \"consultationSpecialityId\","  
		+ "	srv.sub_speciality_id as \"subSpecialityId\",  "
		+ "	spc.speciality_name as \"specialityName\", " 
		+ "	subspc.sub_speciality_name as \"subSpecialityName\", "
		+ "	usmd.is_ref_doctor_share as \"isRefDoctorShare\", " 
		+ "	usmd.is_doc_req as \"isDocReq\" ,"
		+ "	coalesce(usmd.ref_share_per,0) as \"refSharePer\" ,"
		+ "	coalesce(usmd.doc_ref_per,0) as \"docSharePer\" ,"
		+ " ps.is_cap as \"isCap\" ,"
		+ " ps.service_price as \"serviceCap\" ,"
		+ " usmd.product_category_id as \"productCategoryId\", "
        + " usmd.pre_requisite as \"preRequisite\", "
        + " usmd.pre_requisite_details as \"preRequisiteDetails\" "
		+ " from "
		+ "	service.t_package_services_details ps " 
		+ " left join service.m_service_master srv on "
		+ "	srv.service_master_id = ps.service_id " 
		+ " left join service.t_unit_service_master_details usmd on "
		+ " ps.service_id = usmd.service_master_id	and  usmd.status = 'A'  and ps.unit_id = usmd.unit_id "
		+ " left join doctor.m_speciality_master spc on " 
		+ "	spc.speciality_id = srv.speciality_id "
		+ " left join doctor.m_sub_speciality_master subspc on "
		+ "	subspc.sub_speciality_id = srv.sub_speciality_id " 
		+ " left join service.m_service_tarrif_master srvtrf on "
		+ "	srvtrf.service_master_id = ps.service_id " 
		+ " left join public.m_item item on "
		+ "	ps.item_id = item.id	 " 
		+ " where " 
		+ "	ps.package_id = :packageId "
		+ "	order by ps.package_service_details_id asc "),
	
	@NamedNativeQuery(name = "GET_SERVICE_DETAILS_BY_PACKAGE_ID_FOR_BILLING_TRANSACTION", 
	query =  "select "
			+ "	tpcm.package_id as \"packageId\", "
			+ "	tpcsd.service_id as \"serviceId\", "
			+ "    tpcsd.service_id as \"serviceMasterId\", "
			+ "	tpcsd.balance_package_quantity as \"balancePackageQuantity\", "
			+ "	tpcsd.package_quantity as \"packageQuantity\", "
			+ "	tpcsd.service_cap as \"servicePrice\", "
			+ "	tpcsd.available_cap as \"availableCap\", "
			+ "	coalesce(tpcsd.apportioned_price, "
			+ "	srvtrf.rate) as \"apportionedPrice\", "
			+ "	tpcsd.is_service_item as \"isServiceItem\", "
			+ "	tpcsd.item_id as \"itemId\", "
			+ "item.code as \"itemCode\","			
			+ "	item.\"name\" as \"itemName\", "
			+ "	srv.service_standard_name as \"serviceName\", "
			+ "	srv.service_standard_code as \"serviceCode\", "
			+ "	spc.speciality_id as \"specialityId\", "
			+ "	srv.sub_speciality_id as \"subSpecialityId\", "
			+ "	spc.speciality_name as \"specialityName\", "
			+ "	subspc.sub_speciality_name as \"subSpecialityName\", "
			+ "	usmd.is_ref_doctor_share as \"isRefDoctorShare\", "
			+ "	usmd.is_doc_req as \"isDocReq\" , "
			+ "	coalesce(usmd.ref_share_per,0) as \"refSharePer\" , "
			+ "	coalesce(usmd.doc_ref_per,0) as \"docSharePer\" , "
			+ " tpcsd.is_cap as \"isCap\" , "
			+ " usmd.product_category_id as \"productCategoryId\","
			+ "tpcsd.is_groupwise_exlusion as \"isGroupwiseExlusion\", (coalesce(tpcsd.service_cap,0) - coalesce(tpcsd.available_cap,0)) as \"consumedAmt\","
			+ " (coalesce(tpcsd.package_quantity,0) - coalesce(tpcsd.balance_package_quantity,0)) as \"consumedQty\" "
			+ " from billing.t_package_consumption_master as tpcm "
			+ " inner join  billing.t_package_consumption_service_detail tpcsd on tpcm.package_consumption_master_id=tpcsd.package_consumption_master_id "
			+ " left join service.m_service_master srv on "
			+ "	srv.service_master_id = tpcsd.service_id "
			+ " left join service.t_unit_service_master_details usmd on "
			+ " tpcsd.service_id = usmd.service_master_id	and  usmd.status = 'A'  and tpcm.unit_id = usmd.unit_id "
			+ " left join doctor.m_speciality_master spc on "
			+ "	spc.speciality_id = srv.speciality_id or spc.speciality_id=tpcsd.dept_id "
			+ " left join doctor.m_sub_speciality_master subspc on "
			+ "	subspc.sub_speciality_id = srv.sub_speciality_id  or subspc.sub_speciality_id=tpcsd.sub_dept_id "
			+ " left join service.m_service_tarrif_master srvtrf on "
			+ "	srvtrf.service_master_id = tpcsd.service_id "
			+ " left join public.m_item item on "
			+ "	tpcsd.item_id = item.id "
			+ " where "
			+ "	tpcsd.package_consumption_master_id=:consumptionMasterId "
			+ "   and tpcm.package_id=:packageId and tpcsd.status='A' "),

		@NamedNativeQuery(name = "GET_SERVICE_DETAILS_BY_PACKAGE_ID_FOR_EDIT", query = "select "
				+ "	ps.package_service_details_id as \"packageServiceDetailsId\", "
				+ "	ps.consultation_speciality_id as \"consultationSpecialityId\", " 
				+ "	ps.package_id as \"packageId\", " 
				+ "	ps.service_id as \"serviceId\", "
				+ "	ps.service_id as \"serviceMasterId\", " 
				+ "	ps.number_to_be_use as \"numberToBeUse\", "
				+ "	ps.service_price as \"servicePrice\", "
				+ "	coalesce(ps.apportioned_price,ps.service_price) as \"apportionedPrice\", "
				+ "	ps.package_either_or_group_id as \"packageEitherOrGroupId\", "
				+ "	ps.is_service_item as \"isServiceItem\", " 
				+ "	ps.periodicity_id as \"periodicityId\", "
				+ "	srv.service_standard_name as \"serviceName\", " 
				+ "	srv.service_standard_code as \"serviceCode\", "
				+ "	spc.speciality_id as \"specialityId\", "
				+ "	srv.sub_speciality_id as \"subSpecialityId\", "
				+ "	spc.speciality_name as \"specialityName\", "
				+ "	subspc.sub_speciality_name as \"subSpecialityName\", "
				+ "	usmd.is_ref_doctor_share as \"isRefDoctorShare\", " 
				+ "	usmd.is_doc_req as \"isDocReq\" " 
				+ " from "
				+ "	service.t_package_services_details ps " 
				+ " inner join service.m_service_master srv on "
				+ "	srv.service_master_id = ps.service_id "
				+ " inner join service.t_unit_service_master_details usmd on "
				+ "	ps.service_id = usmd.service_master_id " 
				+ "	and usmd.status = 'A' "
				+ "	and ps.unit_id = usmd.unit_id " 
				+ " left join doctor.m_speciality_master spc on "
				+ "	spc.speciality_id = srv.speciality_id " 
				+ " left join doctor.m_sub_speciality_master subspc on "
				+ "	subspc.sub_speciality_id = srv.sub_speciality_id " 
				+ " where " 
				+ "	ps.package_id = :packageId "
				+ " order by " 
				+ "	ps.package_service_details_id asc"),

		@NamedNativeQuery(name = "GET_EITHER_OR_GROUP_SERVICE_DETAILS_BY_PACKAGE_ID_FOR_EDIT", query = "select "
				+ "	ps.package_service_details_id as \"packageServiceDetailsId\", "
				+ "	ps.package_id as \"packageId\", " + "	ps.service_id as \"serviceId\", "
				+ "	ps.service_id as \"serviceMasterId\", " + "	ps.number_to_be_use as \"numberToBeUse\", "
				+ "	ps.service_price as \"servicePrice\", "
				+ "	coalesce(ps.apportioned_price,ps.service_price) as \"apportionedPrice\", "
				+ "	ps.package_either_or_group_id as \"packageEitherOrGroupId\", "
				+ "	ps.is_service_item as \"isServiceItem\", " + "	ps.periodicity_id as \"periodicityId\", "
				+ "	srv.service_standard_name as \"serviceName\", " + "	srv.service_standard_code as \"serviceCode\", "
				+ "	spc.speciality_id as \"specialityId\", " + "	srv.sub_speciality_id as \"subSpecialityId\", "
				+ "	spc.speciality_name as \"specialityName\", "
				+ "	subspc.sub_speciality_name as \"subSpecialityName\", "
				+ "	usmd.is_ref_doctor_share as \"isRefDoctorShare\", " + "	usmd.is_doc_req as \"isDocReq\" " + "from "
				+ "	service.t_package_services_details ps " + "inner join service.m_service_master srv on "
				+ "	srv.service_master_id = ps.service_id "
				+ "inner join service.t_unit_service_master_details usmd on "
				+ "	ps.service_id = usmd.service_master_id " + "	and usmd.status = 'A' "
				+ "	and ps.unit_id = usmd.unit_id " + "left join doctor.m_speciality_master spc on "
				+ "	spc.speciality_id = srv.speciality_id " + "left join doctor.m_sub_speciality_master subspc on "
				+ "	subspc.sub_speciality_id = srv.sub_speciality_id " + "where "
				+ "	ps.package_either_or_group_id = :packageGroupDetailsId " + "order by "
				+ "	ps.package_service_details_id asc"),
		@NamedNativeQuery(name = "GET_EITHER_OR_GROUP_SERVICE_DETAILS_BY_PACKAGE_ID_FOR_TRANSACTION", 
		  query = "select "
				  + "	tpcm.package_id as \"packageId\", "
				  + "	tpcsd.service_id as \"serviceId\", "
				  + "    tpcsd.service_id as \"serviceMasterId\", "
				  + "	tpcsd.balance_package_quantity as \"balancePackageQuantity\", "
				  + "	tpcsd.package_quantity as \"packageQuantity\", "
				  + "	tpcsd.service_cap as \"servicePrice\", "
				  + "	coalesce(tpcsd.apportioned_price, "
				  + "	srvtrf.rate) as \"apportionedPrice\", "
				  + "	tpcsd.is_service_item as \"isServiceItem\", "
				  + "	tpcsd.item_id as \"itemId\", "
				  + "item.code as \"itemCode\", "
				  + "	item.\"name\" as \"itemName\", "
				  + "	srv.service_standard_name as \"serviceName\", "
				  + "	srv.service_standard_code as \"serviceCode\", "
				  + "	spc.speciality_id as \"specialityId\", "
				  + "	srv.sub_speciality_id as \"subSpecialityId\", "
				  + "	spc.speciality_name as \"specialityName\", "
				  + "	subspc.sub_speciality_name as \"subSpecialityName\", "
				  + "	usmd.is_ref_doctor_share as \"isRefDoctorShare\", "
				  + "	usmd.is_doc_req as \"isDocReq\" , "
				  + "	coalesce(usmd.ref_share_per,0) as \"refSharePer\" , "
				  + "	coalesce(usmd.doc_ref_per,0) as \"docSharePer\" , "
				  + " tpcsd.is_cap as \"isCap\" , "
				  + " usmd.product_category_id as \"productCategoryId\", "
				  + "tpcsd.is_groupwise_exlusion as \"isGroupwiseExlusion\" "
				  + " from billing.t_package_consumption_master as tpcm "
				  + " inner join  billing.t_package_consumption_service_detail tpcsd on tpcm.package_consumption_master_id=tpcsd.package_consumption_master_id "
				  + " left join service.m_service_master srv on "
				  + "	srv.service_master_id = tpcsd.service_id "
				  + " left join service.t_unit_service_master_details usmd on "
				  + " tpcsd.service_id = usmd.service_master_id	and  usmd.status = 'A'  and tpcm.unit_id = usmd.unit_id "
				  + " left join doctor.m_speciality_master spc on "
				  + "	spc.speciality_id = srv.speciality_id or spc.speciality_id=tpcsd.dept_id "
				  + " left join doctor.m_sub_speciality_master subspc on "
				  + "	subspc.sub_speciality_id = srv.sub_speciality_id  or subspc.sub_speciality_id=tpcsd.sub_dept_id "
				  + " left join service.m_service_tarrif_master srvtrf on "
				  + "	srvtrf.service_master_id = tpcsd.service_id "
				  + " left join public.m_item item on "
				  + "	tpcsd.item_id = item.id "
				  + " where "
				  + "	tpcsd.package_consumption_master_id=:consumptionMasterId "
				  + "   and tpcm.package_id=:packageId "
				  + "   and tpcsd.package_eo_detail_id=:groupId and tpcsd.status='A' "),

		@NamedNativeQuery(name = "GET_ITEM_DETAILS_BY_PACKAGE_ID_FOR_EDIT", query = "select "
				+ "	ps.package_service_details_id as \"packageServiceDetailsId\", "
				+ "	ps.package_id as \"packageId\", " + "	ps.number_to_be_use as \"numberToBeUse\", "
				+ "	coalesce(ps.apportioned_price, 0) as \"apportionedPrice\", "
				+ "	ps.package_either_or_group_id as \"packageEitherOrGroupId\", "
				+ "	ps.is_service_item as \"isServiceItem\", " + "	ps.item_id as \"itemId\", "
				+ "	item.\"name\" as \"itemName\", " + "	ps.periodicity_id as \"periodicityId\",ps.service_price as \"servicePrice\" " + "from "
				+ "	service.t_package_services_details ps " + "inner join public.m_item item on "
				+ "	  ps.item_id = item.id " + "where " + "	ps.package_id = :packageId " + "order by "
				+ "	ps.package_service_details_id asc"),

		@NamedNativeQuery(name = "GET_EXISITNG_PACKAGE_DETAILS_BY_ENCOUNTERID_VISITTYPE_PATIENTID", query =  "select "
				+ "	pcm.package_consumption_master_id as \"packageConsumptionMasterId\", "
				+ "	packMaster.package_master_id as \"packageId\", "
				+ "	ordMaster.order_no as \"orderNo\", "
				+ "	to_char(ordMaster.order_date, 'DD-MM-YYYY') as \"orderDate\", "
				+ "	srv.service_standard_name as \"packageName\", "
				+ "	srv.service_standard_code as \"packageCode\", "
				+ "	packMaster.package_price as \"packagePrice\", "
				+ "	packMaster.package_type_id as \"packageTypeId\", "
				+ "	packMaster.service_id as \"packageServiceId\", "
				+ "	packMaster.billing_bed_category_id as \"billingBedCategoryId\", "
				+ "	pcm.order_detail_id as \"orderDetailId\", "
				+ "	to_char(pcm.validity_from, 'DD-MM-YYYY') as \"validityFrom\", "
				+ "	to_char(pcm.validity_to, 'DD-MM-YYYY HH24:MI:SS') as \"validityTo\", "
				+ "	to_char(pcm.package_applicable_date, 'DD-MM-YYYY') as \"packageApplicableDate\", "
				+ "	pcm.applicable_no_of_visit as \"applicableNoOfVisit\", "
				+ "	pcm.total_deposite as \"totalDeposite\", "
				+ "	pcm.balance_deposite as \"balanceDeposite\", "
				+ "	pcm.is_cancelled as \"isCancelled\", "
				+ "	pcm.is_discountinued as \"isDiscountinued\", "
				+ "	to_char(pcm.cancelled_date, 'DD-MM-YYYY') as \"cancelledDate\", "
				+ "	pcm.cancelled_by as \"cancelledBy\", "
				+ "	pcm.cancelled_reason_id as \"cancelledReasonId\", "
				+ "	pcm.cancelled_remark as \"cancelledRemark\", "
				+ "	pcm.discountined_by as \"discountinuedBy\", "
				+ "	pcm.discountinued_reason_id as \"discountinuedReasonId\", "
				+ "	to_char(pcm.discountined_date, 'DD-MM-YYYY') as \"discountinuedDate\", "
				+ "	pcm.discountinued_remark as \"discountinuedRemark\", "
				+ "	pcm.tariff_id as \"tariffId\", "
				+ "	bm.bill_status_id as \"billStatusId\", "
				+ "	exists( select "
				+ "			pbbc.billing_bed_category_id "
				+ "		from "
				+ "			billing.package_billing_bed_category pbbc "
				+ "		where "
				+ "			pbbc.package_consumption_master_id = pcm.package_consumption_master_id "
				+ "			and pbbc.billing_bed_category_id =:billingBedCategoryId) as \"billingBedExist\" ,"
				+ " pcm.is_force_close as \"isForceClose\" "
				+ " from "
				+ "	billing.t_package_consumption_master pcm "
				+ " inner join service.m_package_master packMaster on "
				+ "		packMaster.package_master_id = pcm.package_id "
				+ " inner join service.m_service_master srv on "
				+ "		packMaster.service_id = srv.service_master_id "
				+ " inner join public.t_order_details ordDetails on "
				+ "		ordDetails.order_details_id = pcm.order_detail_id "
				+ " inner join public.t_order_master ordMaster on "
				+ "		ordMaster.order_id = ordDetails.order_id "	
				+ "	left join billing.t_billing_details bd on "
				+ "		bd.order_details_id = ordDetails.order_details_id "
				+ "	left join billing.m_billing_master bm on "
				+ "		bm.billing_master_id = bd.billing_master_id and bm.bill_cancelled = 'N' "
				+ " where "
				+ "	pcm.patient_id =:patientId "
				+ "	and ((pcm.encounter_id =:encounterId "
				+ "	and pcm.visit_type =:visitTypeId) "
				+ "	or (pcm.admission_id =:encounterId "
				+ "	and pcm.visit_type =:visitTypeId)) "
				+ "	and pcm.organisation_id =:organisationId "
				+ "	and pcm.unit_id =:unitId "
				+ "	and pcm.status = 'A' AND ordDetails.status='A' "
				+ "	and pcm.is_cancelled = 'N' "
				+ "	and pcm.is_discountinued = 'N' "
				//+ " and pcm.is_force_close != 0 "
				+ "	and pcm.package_type_id != 5 " //except multivisit which includes 1	EHC Package, 3 Package with CAP, 4 All Inclusive Package ///HIS-19517
				+ "	and ((case when bm.bill_status_id is not null then bm.bill_status_id!=1 when (bm.bill_status_id is null and :isPharamcy = true) then bm.bill_status_id = 3 else bm.bill_status_id is null end) "
				+ "	or bm.visit_type_id !=1 ) "
				+ " union all "
				+ " select "
				+ "	pcm.package_consumption_master_id as \"packageConsumptionMasterId\", "
				+ "	packMaster.package_master_id as \"packageId\", "
				+ "	ordMaster.order_no as \"orderNo\", "
				+ "	to_char(ordMaster.order_date, 'DD-MM-YYYY') as \"orderDate\", "
				+ "	srv.service_standard_name as \"packageName\", "
				+ "	srv.service_standard_code as \"packageCode\", "
				+ "	packMaster.package_price as \"packagePrice\", "
				+ "	packMaster.package_type_id as \"packageTypeId\", "
				+ "	packMaster.service_id as \"packageServiceId\", "
				+ "	packMaster.billing_bed_category_id as \"billingBedCategoryId\", "
				+ "	pcm.order_detail_id as \"orderDetailId\", "
				+ "	to_char(pcm.validity_from, 'DD-MM-YYYY') as \"validityFrom\", "
				+ "	to_char(pcm.validity_to, 'DD-MM-YYYY HH24:MI:SS') as \"validityTo\", "
				+ "	to_char(pcm.package_applicable_date, 'DD-MM-YYYY') as \"packageApplicableDate\", "
				+ "	pcm.applicable_no_of_visit as \"applicableNoOfVisit\", "
				+ "	pcm.total_deposite as \"totalDeposite\", "
				+ "	pcm.balance_deposite as \"balanceDeposite\", "
				+ "	pcm.is_cancelled as \"isCancelled\", "
				+ "	pcm.is_discountinued as \"isDiscountinued\", "
				+ "	to_char(pcm.cancelled_date, 'DD-MM-YYYY') as \"cancelledDate\", "
				+ "	pcm.cancelled_by as \"cancelledBy\", "
				+ "	pcm.cancelled_reason_id as \"cancelledReasonId\", "
				+ "	pcm.cancelled_remark as \"cancelledRemark\", "
				+ "	pcm.discountined_by as \"discountinuedBy\", "
				+ "	pcm.discountinued_reason_id as \"discountinuedReasonId\", "
				+ "	to_char(pcm.discountined_date, 'DD-MM-YYYY') as \"discountinuedDate\", "
				+ "	pcm.discountinued_remark as \"discountinuedRemark\", "
				+ "	pcm.tariff_id as \"tariffId\", "
				+ "	bm.bill_status_id as \"billStatusId\", "
				+ "	exists( select "
				+ "			pbbc.billing_bed_category_id "
				+ "		from "
				+ "			billing.package_billing_bed_category pbbc "
				+ "		where "
				+ "			pbbc.package_consumption_master_id = pcm.package_consumption_master_id "
				+ "			and pbbc.billing_bed_category_id =:billingBedCategoryId) as \"billingBedExist\" ,"
				+ " pcm.is_force_close as \"isForceClose\" "
				+ " from "
				+ "	billing.t_package_consumption_master pcm "
				+ " inner join service.m_package_master packMaster on "
				+ "	packMaster.package_master_id = pcm.package_id "
				+ " inner join service.m_service_master srv on "
				+ "	packMaster.service_id = srv.service_master_id "
				+ " inner join public.t_order_details ordDetails on "
				+ "	ordDetails.order_details_id = pcm.order_detail_id "
				+ " inner join public.t_order_master ordMaster on "
				+ "	ordMaster.order_id = ordDetails.order_id "
				+ "	left join billing.t_billing_details bd on "
				+ "	bd.order_details_id = ordDetails.order_details_id "
				+ "	left join billing.m_billing_master bm on "
				+ "	bm.billing_master_id = bd.billing_master_id and bm.bill_cancelled = 'N' "
				+ " where "
				+ "	pcm.patient_id =:patientId "
				//+ " and pcm.is_force_close != 0 "
				+ "	and pcm.package_type_id = 5 " //only multivisit is patient specific 
				+ " AND ordDetails.status='A' "
				+ " and pcm.organisation_id =:organisationId and  pcm.unit_id=:unitId and  pcm.applicable_no_of_visit > 0  AND pcm.status= 'A' and pcm.is_cancelled = 'N' and pcm.is_discountinued = 'N'  "
				//+ "	and ((case when bm.bill_status_id is not null then bm.bill_status_id!=1 else bm.bill_status_id is null  end) or bm.visit_type_id !=1 ) "
				+ "	and ((case when bm.bill_status_id is not null then bm.bill_status_id!=1 when (bm.bill_status_id is null and :isPharamcy = true) then  (bm.bill_status_id is null or bm.bill_status_id = 3) else bm.bill_status_id is null end) or bm.visit_type_id !=1 )"
				+ "	and ((case when bm.bill_status_id is null  and  (pcm.visit_type=1 and pcm.encounter_id < coalesce(:encounterId,0)) then bm.bill_status_id in (2,3) else (bm.bill_status_id is null or bm.bill_status_id != 1) end) or bm.visit_type_id !=1 ) "),

		@NamedNativeQuery(name = "GET_EXISITNG_PACKAGE_ACTIVE_ME_PACKAGE", query = " select pcm.package_consumption_master_id as \"packageConsumptionMasterId\", packMaster.package_master_id as \"packageId\", ordMaster.order_no as \"orderNo\", to_char(ordMaster.order_date, 'DD-MM-YYYY') as \"orderDate\", srv.service_standard_name as \"packageName\","
				+ " srv.service_standard_code as \"packageCode\", packMaster.package_price as \"packagePrice\", packMaster.package_type_id as \"packageTypeId\", packMaster.service_id as \"packageServiceId\","
				+ " pcm.order_detail_id as \"orderDetailId\", to_char(pcm.validity_from, 'DD-MM-YYYY') as \"validityFrom\",  to_char(pcm.validity_to, 'DD-MM-YYYY') as \"validityTo\", pcm.applicable_no_of_visit as \"applicableNoOfVisit\","
				+ " pcm.total_deposite as \"totalDeposite\", pcm.balance_deposite as \"balanceDeposite\", pcm.is_cancelled as \"isCancelled\", pcm.is_discountinued as \"isDiscountinued\","
				+ "  to_char(pcm.cancelled_date, 'DD-MM-YYYY') as \"cancelledDate\", pcm.cancelled_by as \"cancelledBy\", pcm.cancelled_reason_id as \"cancelledReasonId\", pcm.cancelled_remark as \"cancelledRemark\","
				+ " pcm.discountined_by as \"discountinuedBy\", pcm.discountinued_reason_id as \"discountinuedReasonId\",  to_char(pcm.discountined_date, 'DD-MM-YYYY') as \"discountinuedDate\",  pcm.discountinued_remark as \"discountinuedRemark\" ,pcm.encounter_speciality_id as \"encounterSpecialityId\", pcm.encounter_id as \"encounterId\",pcm.is_visited as \"isVisited\" "
				+ " from billing.t_package_consumption_master pcm"
				+ "	inner join service.m_package_master packMaster on packMaster.package_master_id = pcm.package_id"
				+ " inner join service.m_service_master srv on packMaster.service_id = srv.service_master_id"
				+ " inner join public.t_order_details ordDetails on ordDetails.order_details_id = pcm.order_detail_id"
				+ " inner join public.t_order_master ordMaster on ordMaster.order_id = ordDetails.order_id"
				+ " where    pcm.patient_id =:patientId  and  pcm.visit_type =:visitTypeId and pcm.package_type_id = 5 and to_date(to_char(now(),'DD-MM-YYYY'),'DD-MM-YYYY') between pcm.validity_from and pcm.validity_to "// multi
																																																							// encounter
																																																							// package
				+ " and pcm.organisation_id =:organisationId AND pcm.unit_id=:unitId and  pcm.applicable_no_of_visit > -1 and  pcm.unit_id=:unitId and pcm.status= 'A' and pcm.is_cancelled = 'N' and pcm.is_discountinued = 'N' "),

		@NamedNativeQuery(name = "GET_PACAKAGE_CONSUMPTION_SERVICE_DETAILS_BY_PACKAGEID_PATIENTID", query = "select "
				+ "	srv.service_master_id as \"serviceId\", "
				+ "	srv.service_standard_code as \"serviceCode\", "
				+ "	srv.service_standard_name as \"serviceStandardName\", "
				+ "	pcsd.item_id as \"itemId\", "
				+ "	item.\"name\" as \"itemName\", "
				+ "	item.code as \"itemCode\", "
				+ "	pcsd.package_quantity as \"packageQuantity\", "
				+ "	coalesce(pcsd.balance_package_quantity, 0) as \"balanceQuantity\", "
				+ "	pcsd.package_consumption_master_id as \"packageConsumptionMasterId\" , "
				+ "	pcsd.is_service_item as \"isServiceItem\", "
				+ "	pcsd.is_inclusive_exclusive as \"isInclusiveExclusive\", "
				+ "	pcsd.package_eo_detail_id as \"packageEODetailId\", "
				+ "	srv.service_speciality_id as \"specialityId\", "
				+ "	srv.service_sub_speciality_id as \"subSpecialityId\", "
				+ "	sm.speciality_name as \"specialityName\", "
				+ "	ssm.sub_speciality_name as \"subSpecialityName\", "
				+ "	pcsd.pacakge_consumption_service_detail_id as \"packageConsumptionServiceDetailId\", "
				+ "	pcsd.available_cap as \"availableCap\", "
				+ "	usmd.bed_category_id as \"serviceBedCategoryId\", "
				+ "	usmd.product_category_id as \"serviceProductCategoryId\" "
				+ "from "
				+ "	billing.t_package_consumption_master pcm "
				+ "inner join billing.t_package_consumption_service_detail pcsd on "
				+ "	pcsd.package_consumption_master_id = pcm.package_consumption_master_id "
				+ "inner join service.t_unit_service_master_details usmd on "
				+ "	pcsd.service_id = usmd.service_master_id "
				+ "left join service.m_service_master srv on "
				+ "	pcsd.service_id = srv.service_master_id "
				+ "left join public.m_item item on "
				+ "	pcsd.item_id = item.id "
				+ "left join doctor.m_speciality_master sm on "
				+ "	sm.speciality_id = srv.speciality_id "
				+ "left join doctor.m_sub_speciality_master ssm on "
				+ "	ssm.sub_speciality_id = srv.sub_speciality_id "
				+ "where "
				+ "	package_id =:packageId "
				+ "	and pcsd.is_cap = 1 "
				+ "	and pcm.status = 'A' "
				+ "	and usmd.unit_id = pcsd.unit_id "
				+ "	and pcm.patient_id =:patientId "
				+ "	and pcm.status = 'A' "
				+ "order by "
				+ "	pcsd.pacakge_consumption_service_detail_id"),
		
		
		
		
		@NamedNativeQuery(name = "GET_PACAKAGE_CONSUMPTION_SERVICE_DETAILS_BY_CONSUMPTION_ID", query = "select "
				+ "	srv.service_master_id as \"serviceId\", "
				+ "	srv.service_standard_code as \"serviceCode\", "
				+ "	srv.service_standard_name as \"serviceStandardName\", "
				+ "	pcsd.item_id as \"itemId\", "
				+ "	item.\"name\" as \"itemName\", "
				+ "	item.code as \"itemCode\", "
				+ "	pcsd.package_quantity as \"packageQuantity\", "
				+ "	pcsd.balance_package_quantity as \"balanceQuantity\", "
				+ "	pcsd.package_consumption_master_id as \"packageConsumptionMasterId\" , "
				+ "	pcsd.is_service_item as \"isServiceItem\", "
				+ "	pcsd.is_inclusive_exclusive as \"isInclusiveExclusive\", "
				+ "	pcsd.package_eo_detail_id as \"packageEODetailId\", "
				+ "	srv.service_speciality_id as \"specialityId\", "
				+ "	srv.service_sub_speciality_id as \"subSpecialityId\", "
				+ "	sm.speciality_name as \"specialityName\", "
				+ "	ssm.sub_speciality_name as \"subSpecialityName\", "
				+ "	pcsd.pacakge_consumption_service_detail_id as \"packageConsumptionServiceDetailId\", "
				+ "	pcsd.available_cap as \"availableCap\", "
				+ "	usmd.bed_category_id as \"serviceBedCategoryId\", "
				+ "	pcsd.product_category_id as \"productCategoryId\", "
				+ "	usmd.product_category_id as \"serviceProductCategoryId\", "
				+ " tpsd.consultation_speciality_id as \"consultationSpecialityId\" "
				+ "from "
				+ "	billing.t_package_consumption_master pcm "
				+ "inner join billing.t_package_consumption_service_detail pcsd on "
				+ "	pcsd.package_consumption_master_id = pcm.package_consumption_master_id "
				+ "left join service.t_unit_service_master_details usmd on  "
				+ "	pcsd.service_id = usmd.service_master_id and usmd.unit_id = pcsd.unit_id and usmd.status = 'A'"
				+ "left join service.m_service_master srv on "
				+ "	pcsd.service_id = srv.service_master_id "
				+ "left join public.m_item item on "
				+ "	pcsd.item_id = item.id "
				+ "left join doctor.m_speciality_master sm on "
				+ "	sm.speciality_id = srv.speciality_id "
				+ "left join doctor.m_sub_speciality_master ssm on "
				+ "	ssm.sub_speciality_id = srv.sub_speciality_id "
				+ "left join service.t_package_services_details tpsd on "
				+ "tpsd.package_id = pcm.package_id "
				+ "and tpsd.service_id = pcsd.service_id "
				+ "where "
				+ "	pcm.package_consumption_master_id =:packageConsumptionMasterId"
				+ "	and pcsd.is_cap = 1 "
				+ "	and pcm.status = 'A' and pcsd.status = 'A' "
				+ " order by "
				+ "	pcsd.pacakge_consumption_service_detail_id"),

		@NamedNativeQuery(name = "GET_PACAKAGE_CONSUMPTION_SERVICE_DETAILS_BY_PACKAGEID_PATIENTID_WITH_MULTY_ENC", query = " select "
				+ "  	pcsd.pacakge_consumption_service_detail_id as \"pacakgeConsumptionServiceDetailId\", "
				+ "  	pcsd.package_consumption_master_id as \"packageConsumptionMasterId\", "
				+ "  	pcsd.service_id as \"serviceId\", " + "  	pcsd.package_quantity as \"packageQuantity\", "
				+ "  	pcsd.balance_package_quantity as \"balancePackageQuantity\", "
				+ "  	cast(pcsd.apportioned_price as double precision) as \"apportionedPrice\", "
				+ "  	pcsd.is_service_item as \"isServiceItem\", " + "  	pcsd.item_id as \"itemId\", "
				+ "  	pcsd.package_eo_detail_id as \"packageEoDetailId\", "
				+ "  	pcsd.is_inclusive_exclusive as \"isInclusiveExclusive\", "
				+ "  	srv.service_standard_code as \"serviceCode\", "
				+ "  	srv.service_standard_name as \"serviceName\", "
				+ "  	spc.speciality_name as \"specialityName\", " + "  	spc.speciality_id as \"specialityId\", "
				+ "  	subspc.sub_speciality_id as \"subSpecialityId\", "
				+ "  	subspc.sub_speciality_name as \"subSpecialityName\" " + "  from "
				+ "  	billing.t_package_consumption_service_detail pcsd "
				+ "  left join service.m_service_master srv on " + "  	srv.service_master_id = pcsd.service_id "
				+ "  left join doctor.m_speciality_master spc on " + "  	spc.speciality_id = srv.speciality_id "
				+ "  left join doctor.m_sub_speciality_master subspc on "
				+ "  	subspc.sub_speciality_id = srv.sub_speciality_id "
				+ "  inner join billing.t_package_consumption_master pcm on "
				+ "  	pcsd.package_consumption_master_id = pcm.package_consumption_master_id " + "  where "
				+ "  	(pcm.encounter_id = :encounterId " + "  	and pcm.visit_type =:visitTypeId) "
				+ "  	or (pcm.admission_id = :encounterId " + "  	and pcm.visit_type =:visitTypeId) "
				+ "  	and pcsd.is_service_item = 1 " + "  	and pcm.package_id = :packageId " + "  union all "
				+ "  select "
				+ "  	pcsd.pacakge_consumption_service_detail_id as \"pacakgeConsumptionServiceDetailId\", "
				+ "  	pcsd.package_consumption_master_id as \"packageConsumptionMasterId\", "
				+ "  	pcsd.service_id as \"serviceId\", " + "  	pcsd.package_quantity as \"packageQuantity\", "
				+ "  	pcsd.balance_package_quantity as \"balancePackageQuantity\", "
				+ "  	cast(pcsd.apportioned_price as double precision) as \"apportionedPrice\", "
				+ "  	pcsd.is_service_item as \"isServiceItem\", " + "  	pcsd.item_id as \"itemId\", "
				+ "  	pcsd.package_eo_detail_id as \"packageEoDetailId\", "
				+ "  	pcsd.is_inclusive_exclusive as \"isInclusiveExclusive\", "
				+ "  	srv.service_standard_code as \"serviceCode\", "
				+ "  	srv.service_standard_name as \"serviceName\", "
				+ "  	spc.speciality_name as \"specialityName\", " + "  	spc.speciality_id as \"specialityId\", "
				+ "  	subspc.sub_speciality_id as \"subSpecialityId\", "
				+ "  	subspc.sub_speciality_name as \"subSpecialityName\" " + "  from "
				+ "  	billing.t_package_consumption_service_detail pcsd "
				+ "  left join service.m_service_master srv on " + "  	srv.service_master_id = pcsd.service_id "
				+ "  left join doctor.m_speciality_master spc on " + "  	spc.speciality_id = srv.speciality_id "
				+ "  left join doctor.m_sub_speciality_master subspc on "
				+ "  	subspc.sub_speciality_id = srv.sub_speciality_id "
				+ "  inner join billing.t_package_consumption_master pcm on "
				+ "  	pcsd.package_consumption_master_id = pcm.package_consumption_master_id " + "  where "
				+ "  	 pcm.visit_type =:visitTypeId "
				// + " and pcsd.is_service_item = 1 "
				+ "  	and pcm.package_id = :packageId " + "  	and pcm.package_type_id  = 5 "
				+ "  	and pcm.patient_id  = :patientId "),

		@NamedNativeQuery(name = "GET_INCLUSION_EXCLUSION_ORDERED_SERVICES", query = "select "
				+ "    service_mst.service_master_id as \"serviceId\", "
				+ "	service_mst.service_standard_name \"serviceStandardName\", "
				+ "	service_mst.service_standard_code \"serviceCode\", "
				+ "	ord_dtls.ord_cancelled as \"isOrdCanceled\" " + "from "
				+ "	billing.t_package_consumption_master pkg_mst " + "inner join public.t_order_details ord_dtls on "
				+ "	ord_dtls.package_id = pkg_mst.package_id " + "inner join service.m_service_master service_mst on "
				+ "	ord_dtls.service_id = service_mst.service_master_id "
				+ "inner join service.t_unit_service_mapper usmppr on "
				+ "	usmppr.service_id = service_mst.service_master_id " + "	and usmppr.unit_id = pkg_mst.unit_id "
				+ "where " + "	pkg_mst.package_consumption_master_id = :packageConsumptionMasterId "
				+ "	and ord_dtls.is_package_service = 'Y' " + "	order by ord_dtls.order_details_id desc"),

		@NamedNativeQuery(name = "GET_INCLUSION_EXCLUSION_ORDERED_ITEMS", query = "select "
				+ "    item.id as \"itemId\", " + "	item.\"name\" as \"itemName\", " + "	item.code as \"itemCode\", "
				+ "	ord_dtls.ord_cancelled as \"isOrdCanceled\" " + "from "
				+ "	billing.t_package_consumption_master pkg_mst " + "inner join public.t_order_details ord_dtls on "
				+ "	ord_dtls.package_id = pkg_mst.package_id " + "inner join public.m_item item on "
				+ "  item.id = ord_dtls.drug_id " + "where "
				+ "	pkg_mst.package_consumption_master_id = :packageConsumptionMasterId "
				+ "	and ord_dtls.is_package_service = 'Y' " + "    order by ord_dtls.order_details_id desc") })
@Entity
@Table(name = "t_package_services_details", schema = "service")
@SequenceGenerator(name = "package_service_details_seq", sequenceName = "service.package_service_details_seq", allocationSize = 1)
public class TPackageServicesDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "package_service_details_seq")
	@Column(name = "package_service_details_id")
	private int packageServiceDetailsId;

	@Column(name = "package_id")
	private Integer packageId;

	@Column(name = "service_id")
	private Integer serviceId;

	@Column(name = "number_to_be_use")
	private Integer numberToBeUse;

	@Column(name = "service_price")
	private Double servicePrice;

	@Column(name = "apportioned_price")
	private Double apportionedPrice;

	@Column(name = "orgnisation_id")
	private Integer orgnisationId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "status")
	private char status;

	@Column(name = "package_either_or_group_id")
	private Integer packageEitherOrGroupId;

	@Column(name = "is_service_item")
	private Integer isServiceItem;

	@Column(name = "item_id")
	private Integer itemId;

	@Column(name = "periodicity_id")
	private Integer periodicityId;

	@Column(name = "consultation_speciality_id")
	private Integer consultationSpecialityId;

	@Column(name = "parent_package_id")
	private Integer parentPackageId;

	@Column(name = "is_cap")
	private int isCap;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_id", insertable = false, updatable = false, nullable = false)
	private MPackageMaster mPackageMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id", insertable = false, updatable = false, nullable = false)
	private ServiceMaster serviceMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_either_or_group_id", insertable = false, updatable = false, nullable = false)
	private TPackageEitherorGroupDetails tPackageEitherorGroupDetails;

	public MPackageMaster getmPackageMaster() {
		return mPackageMaster;
	}

	public void setmPackageMaster(MPackageMaster mPackageMaster) {
		this.mPackageMaster = mPackageMaster;
	}

	public ServiceMaster getServiceMaster() {
		return serviceMaster;
	}

	public void setServiceMaster(ServiceMaster serviceMaster) {
		this.serviceMaster = serviceMaster;
	}

	public TPackageEitherorGroupDetails gettPackageEitherorGroupDetails() {
		return tPackageEitherorGroupDetails;
	}

	public void settPackageEitherorGroupDetails(TPackageEitherorGroupDetails tPackageEitherorGroupDetails) {
		this.tPackageEitherorGroupDetails = tPackageEitherorGroupDetails;
	}

	public int getPackageServiceDetailsId() {
		return packageServiceDetailsId;
	}

	public void setPackageServiceDetailsId(int packageServiceDetailsId) {
		this.packageServiceDetailsId = packageServiceDetailsId;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getNumberToBeUse() {
		return numberToBeUse;
	}

	public void setNumberToBeUse(Integer numberToBeUse) {
		this.numberToBeUse = numberToBeUse;
	}

	public Double getServicePrice() {
		return servicePrice;
	}

	public void setServicePrice(Double servicePrice) {
		this.servicePrice = servicePrice;
	}

	public Double getApportionedPrice() {
		return apportionedPrice;
	}

	public void setApportionedPrice(Double apportionedPrice) {
		this.apportionedPrice = apportionedPrice;
	}

	public Integer getOrgnisationId() {
		return orgnisationId;
	}

	public void setOrgnisationId(Integer orgnisationId) {
		this.orgnisationId = orgnisationId;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getPackageEitherOrGroupId() {
		return packageEitherOrGroupId;
	}

	public void setPackageEitherOrGroupId(Integer packageEitherOrGroupId) {
		this.packageEitherOrGroupId = packageEitherOrGroupId;
	}

	public Integer getIsServiceItem() {
		return isServiceItem;
	}

	public void setIsServiceItem(Integer isServiceItem) {
		this.isServiceItem = isServiceItem;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getPeriodicityId() {
		return periodicityId;
	}

	public void setPeriodicityId(Integer periodicityId) {
		this.periodicityId = periodicityId;
	}

	public Integer getConsultationSpecialityId() {
		return consultationSpecialityId;
	}

	public void setConsultationSpecialityId(Integer consultationSpecialityId) {
		this.consultationSpecialityId = consultationSpecialityId;
	}

	public Integer getParentPackageId() {
		return parentPackageId;
	}

	public void setParentPackageId(Integer parentPackageId) {
		this.parentPackageId = parentPackageId;
	}

	public int getIsCap() {
		return isCap;
	}

	public void setIsCap(int isCap) {
		this.isCap = isCap;
	}

}
