package com.param.service.global.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.unit.model.BillingBedCategoryMaster;
import com.param.billing.global.model.PackageTypeMaster;
import com.param.billing.global.transaction.model.TariffMaster;
import com.param.global.model.GenderMaster;
import com.param.global.model.PatientCategoryMaster;
import com.param.global.model.PaymentEntitlementMaster;
import com.param.global.model.ServiceMaster;
import com.param.global.model.VisitTypeMaster;

@NamedNativeQueries({ @NamedNativeQuery(name = "GET_PACKAGE_TYPE_MASTER_LIST", query = "SELECT "
		+ " packagetypemast.package_type_id AS \"id\"," + " packagetypemast.package_type AS \"name\" " + " FROM "
		+ "	service.m_package_type_master packagetypemast " + "WHERE  packagetypemast.staus = 'A'  "
		+"ORDER BY packagetypemast.package_type ASC"),

		@NamedNativeQuery(name = "SEARCH_EHC_PACKAGES", query = "SELECT serv.service_standard_code AS \"packageCode\", serv.service_standard_name AS \"packageName\", "
				+ " packType.package_type as \"packageType\",vistmst.visit_type_name as \"visitType\", pack.status as \"status\", pack.package_master_id as \"packageId\" "
				+ " FROM service.m_package_master pack " + " inner join service.m_package_type_master packType "
				+ " on packType.package_type_id = pack.package_type_id " + " inner join service.m_service_master serv  "
				+ " on serv.service_master_id = pack.service_id " + "inner join public.m_visit_type_master  vistmst "
				+ "on vistmst.visit_type_id = pack.visit_type_id "
				+ " WHERE pack.org_id =:orgId and pack.unit_id =:unitId "
				+ " ORDER BY packType.package_type ASC"),
//'OP' as \"visitType\",

		@NamedNativeQuery(name = "SEARCH_PACKAGE_SERVICES_FOR_BILLING", query = "select service.service_master_id as \"serviceMasterId\", pack.package_master_id as \"packageMasterId\",  service.service_standard_name as \"serviceStandardName\" , service.service_standard_code as \"serviceStandardCode\",  "
				+ " CAST ('0' AS DOUBLE PRECISION) as \"concession\", speciality.speciality_id as \"specialityId\", speciality.speciality_name as \"specialityName\", "
				+ " sub_speciality.sub_speciality_id as \"subSpecialityId\", sub_speciality.sub_speciality_name as \"subSpecialityName\", details.is_rate_editable as \"isRateEditable\", "
				+ " details.rate_editble_min_value as \"minRateEditable\", details.rate_editble_min_value as \"maxRateEditable\", details.is_tax_applicable as \"isTaxApplicable\", "
				+ " details.is_discount as \"isDiscountApplicable\", details.discount_value as \"discount\", details.gst_type_id as \"taxId\", tax.name as \"taxName\",  "
				+ " coalesce(tax.tax_percentage,0) as \"taxPercentage\", details.is_allow_multiple_quantity as \"isQuantityEditable\", cast(pack.package_price as numeric) as \"basePrice\", "
				+ " cast(1 as integer) as \"quantity\", packType.package_type as \"packageType\",pack.package_type_id as \"packageTypeId\",pack.min_age as \"minAge\", pack.max_age as \"maxAge\" "
				+ " FROM service.t_unit_service_mapper mapper   "
				+ " inner join service.m_service_master service on service.service_master_id  = mapper.service_id "
				+ " inner join service.t_unit_service_master_details details on details.service_master_id = service.service_master_id "
				+ " inner join doctor.m_speciality_master speciality on service.speciality_id = speciality.speciality_id "
				+ " inner join doctor.m_sub_speciality_master sub_speciality on service.sub_speciality_id = sub_speciality.sub_speciality_id  "
				+ " inner join service.m_package_master pack on pack.service_id = service.service_master_id "
				+ " inner join service.m_package_type_master packType on packType.package_type_id = pack.package_type_id "
				+ " left join public.m_tax tax on tax.id=details.gst_type_id  "
				+ " inner join service.m_parent_package_master parent on parent.parent_package_id = pack.parent_package_id "
				+ " where mapper.unit_id =:unitId AND mapper.orgnisation_id=:orgId AND service.status='A' "),

		@NamedNativeQuery(name = "GET_PACKAGE_BY_ID", query = "select pack.package_master_id as \"packageMasterId\","
				+ "	pack.parent_package_id as \"parentPackageId\", "
				+ " pack.patient_type_id as \"patientTypeId\", pack.package_type_id as \"packageTypeId\",  "
				+ " pack.billing_bed_category_id as \"billingBedCategoryId\", pack.payment_entitlement_type_id as \"paymentEntitlementTypeId\", pack.sex_id as \"sexId\", "
				+ " to_char(pack.validity_from_date,'DD-MM-YYYY') as \"validityFromDate\", to_char(pack.validity_to_date,'DD-MM-YYYY') as \"validityToDate\",  "
				+ " pack.status as \"status\", pack.package_cost as \"packageCost\", pack.markup_down_in_percentage as \"markupDownInPercentage\", pack.package_price as \"packagePrice\", "
				+ " pack.is_manual_rounding_is_allow as \"isManualRoundingIsAllow\", pack.manual_roundoff_amount as \"manualRoundoffAmount\",  "
				+ " pack.validity_period_indays as \"validityPeriodIndays\", pack.min_age as \"minAge\", pack.max_age as \"maxAge\", pack.service_id as \"serviceId\", "
				+ " pack.tariff_id as \"tariffId\", pack.is_markup_down as \"isMarkupDown\", pack.round_off_posit_neg as \"roundOffPositNeg\", pack.no_of_encounters as \"noOfEncounters\", "
				+ " srv.service_standard_name as \"packageName\" " + " from service.m_package_master pack "
				+ " inner join service.m_service_master srv  " + " on srv.service_master_id = pack.service_id "
				+ " where  pack.package_master_id = :packageId "),/*pack.status = 'A' and*/

		@NamedNativeQuery(name = "GET_PACKAGE_LIST_BY_TYPE", query = "select "
																	+ "	parent.parent_package_id as \"parentPackageId\", "
																	+ "	parent.version_id as \"versionId\", "
																	+ "	ptype.package_type as \"packageType\", "
																	+ "	srv.service_standard_code as \"packageCode\", "
																	+ "	srv.service_standard_name as \"packageName\", "
																	+ "	parent.status as \"status\" "
																	+ "from "
																	+ "	service.m_parent_package_master parent "
																	+ "inner join service.m_package_type_master ptype on "
																	+ "	ptype.package_type_id = parent.package_type_id "
																	+ "inner join service.m_service_master srv on "
																	+ "	srv.service_master_id = parent.service_id "
																	+ "where "
																	+ "	parent.package_type_id =:packageTypeId "
																	+ "	and parent.org_id =:orgId "
																	+ "	and parent.unit_id =:unitId "),

		@NamedNativeQuery(name = "GET_PACKAGE_LIST_FOR_WEB", query = "select "
				+ "	parent.parent_package_id as \"parentPackageId\", "
				+ "	mpm.package_master_id as \"packageId\", "
				+ "	parent.package_type_id as \"packageTypeId\", "
				+ "	parent.version_id as \"versionId\", "
				+ "	ptype.package_type as \"packageType\", "
				+ "	srv.service_standard_code as \"packageCode\", "
				+ "	srv.service_standard_name as \"packageName\", "
				+ "	parent.status as \"status\" ," +
				" msm.speciality_name as \"specialityName\", " +
				" mssm.sub_speciality_name  as \"subSpecialityName\"," +
				" mpm.package_price as \"packagePrice\"  "
				+ " from "
				+ "	service.m_parent_package_master parent "
				+ "inner join service.m_package_type_master ptype on "
				+ "	ptype.package_type_id = parent.package_type_id "
				+ "inner join service.m_service_master srv on "
				+ "	srv.service_master_id = parent.service_id " +
				" inner join doctor.m_speciality_master msm  on msm.speciality_id  = srv.speciality_id " +
				" inner join doctor.m_sub_speciality_master mssm  on mssm.sub_speciality_id  = srv.sub_speciality_id" +
				" inner join service.m_package_master mpm   on mpm.parent_package_id  = parent.parent_package_id  " +
				" inner join t_unit_application_billing_configuration tuabc  on tuabc.unit_id  = parent.unit_id   "
				+ " where "
				+ "	parent.package_type_id in(1,5) "
				+ "	and parent.org_id =:orgId "
				+ "	and parent.unit_id =:unitId " +
				" and mpm.visit_type_id  = 1 " +
				" and mpm.patient_type_id = tuabc.default_patient_category_id  " +
				" and mpm.billing_bed_category_id = tuabc.opd_billing_cat_id  " +
				" and mpm.status = 'A' " +
				" and mpm.payment_entitlement_type_id =1 "),
		
		@NamedNativeQuery(name = "GET_PACKAGE_LOG", query = "select  pkg_log.package_quantity as \"packageQuantity\", pkg_log.balance_package_quantity as \"balancePackageQuantity\", pkg_log.is_ordered_cancelled as \"isOrderedCancelled\", to_char(pkg_log.created_date,'dd/mm/yyyy') as \"createdDate\", "
				+ " (case when em.user_id is not null then coalesce( em.first_name,'',em.last_name,'' ) " + " else "
				+ " coalesce( dm.first_name,'',dm.last_name,'' ) end) as \"empName\" "
				+ " from billing.t_package_consumption_service_detail_log pkg_log "
				+ " inner join billing.t_package_consumption_service_detail pkg_details "
				+ " on pkg_log.pacakge_consumption_service_detail_id = pkg_details.pacakge_consumption_service_detail_id  "
				+ " left join public.m_employee_master em on em.user_id=pkg_log.created_by "
				+ " left join doctor.m_doctor_master dm on dm.user_id=pkg_log.created_by"
				+ " where pkg_log.pacakge_consumption_service_detail_id=:packageConsumptionServiceDetailId order by pkg_log.log_id asc"),

		@NamedNativeQuery(name = "CHECK_PACKAGE_SERVICE_EXISTS_OR_NOT", query = " select  " + "	count(*)  " + "from  "
				+ "	service.m_package_master pck_mst  " + "where  " + "	pck_mst.org_id = :orgId  "
				+ "	and pck_mst.unit_id = :unitId  " + "	and pck_mst.service_id = :serviceId  "
				+ "	and pck_mst.status = 'A'  "),

		@NamedNativeQuery(name = "GET_PACKAGE_LIST_FOR_IPD_PACKAGE_ORDER", query = 
				"select "
				+ "	pcm.package_consumption_master_id as \"packageConsumptionMasterId\", "
				+ "	packMaster.package_master_id as \"packageId\", "
				+ "	ordMaster.order_no as \"orderNo\", "
				+ "	to_char(ordMaster.order_date, 'DD-MM-YYYY') as \"orderDate\", "
				+ "	to_char(ordMaster.order_date,'DD/MM/YYYY HH24:MI:SS') as \"orderDateString\", "
				+ "	ordMaster.order_date as \"orderDateNew\", "
				+ "	srv.service_standard_name as \"packageName\", "
				+ "	srv.service_standard_code as \"packageCode\", "
				+ "	packMaster.package_price as \"packagePrice\", "
				+ "	packMaster.package_type_id as \"packageTypeId\", "
				+ "	packMaster.service_id as \"packageServiceId\", "
				+ "	pcm.order_detail_id as \"orderDetailId\", "
				+ "	to_char(pcm.validity_from, 'DD-MM-YYYY') as \"validityFrom\", "
				+ "	to_char(coalesce(pcm.validity_to, now()), 'DD-MM-YYYY') as \"validityTo\", "
                + " to_char(pcm.package_applicable_date, 'DD-MM-YYYY') as \"packageApplicableDate\", "
                + " pcm.package_applicable_date as \"packageApplicableDateNew\", "
                + " to_char(pcm.package_applicable_date, 'MM/dd/yyyy') as \"packageApplicableDateDt\", "
                + " to_char(pcm.package_applicable_date, 'MM/dd/yyyy') as \"packageApplicableDateDtold\", "
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
				+ "	to_char(pcm.created_date, 'DD/MM/YYYY') as \"createdDate\", "
				+ "	pcm.discountinued_remark as \"discountinuedRemark\", "
				+ "	pcm.tariff_id as \"tariffId\" , "
				+ "	(case "
				+ "		when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) "
				+ "		when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) "
				+ "		else 'Admin' end) as \"userName\", "
				+ "	pcm.is_force_close as \"isForceClose\", "
				+ "	bm.is_intermediate_bill as \"isIntermediateBill\" "
				+ "from "
				+ "	billing.t_package_consumption_master pcm "
				+ "inner join service.m_package_master packMaster on "
				+ "	packMaster.package_master_id = pcm.package_id "
				+ "inner join service.m_service_master srv on "
				+ "	packMaster.service_id = srv.service_master_id "
				+ "inner join public.t_order_details ordDetails on "
				+ "	ordDetails.order_details_id = pcm.order_detail_id "
				+ "inner join public.t_order_master ordMaster on "
				+ "	ordMaster.order_id = ordDetails.order_id "
				+ "left join billing.t_billing_details bd on "
				+ "	bd.order_details_id = ordDetails.order_details_id and bd.status = 'A' "
				+ "left join billing.m_billing_master bm on "
				+ "	bm.billing_master_id = bd.billing_master_id and bm.bill_cancelled = 'N' "
				+ "left join PUBLIC.m_employee_master empmst on "
				+ "	ordMaster.created_by = empmst.user_id "
				+ "left join doctor.m_doctor_master docmst on "
				+ "	ordMaster.created_by = docmst.user_id "
				+ "where "
				+ "	pcm.patient_id =:patientId "
				+ "	and ((pcm.encounter_id =:encounterId "
				+ "	and pcm.visit_type =:visitTypeId) "
				+ "	or (pcm.admission_id =:encounterId "
				+ "	and pcm.visit_type =:visitTypeId)) "
				+ "	and pcm.organisation_id =:organisationId "
				+ "	and pcm.unit_id =:unitId "
				+ "	and pcm.status = 'A' "
				+ "	and pcm.is_cancelled = 'N' "
				+ "	and pcm.is_discountinued = 'N' "
				+ "	and pcm.package_type_id !=5 "
				+ "	and ( ( "
				+ "	case "
				+ "		when bm.bill_status_id is not null then bm.bill_status_id != 1 "
				+ "		else bm.bill_status_id is null end) "
				+ "	or bm.visit_type_id != 1 ) "
				+ "union all "
				+ "select "
				+ "	pcm.package_consumption_master_id as \"packageConsumptionMasterId\", "
				+ "	packMaster.package_master_id as \"packageId\", "
				+ "	ordMaster.order_no as \"orderNo\", "
				+ "	to_char(ordMaster.order_date, 'DD-MM-YYYY') as \"orderDate\", "
				+ "	to_char(ordMaster.order_date,'DD/MM/YYYY HH24:MI:SS') as \"orderDateString\", "
				+ "	ordMaster.order_date as \"orderDateNew\", "
				+ "	srv.service_standard_name as \"packageName\", "
				+ "	srv.service_standard_code as \"packageCode\", "
				+ "	packMaster.package_price as \"packagePrice\", "
				+ "	packMaster.package_type_id as \"packageTypeId\", "
				+ "	packMaster.service_id as \"packageServiceId\", "
				+ "	pcm.order_detail_id as \"orderDetailId\", "
				+ "	to_char(pcm.validity_from, 'DD-MM-YYYY') as \"validityFrom\", "
				+ "	to_char(pcm.validity_to, 'DD-MM-YYYY') as \"validityTo\", "
				+ " to_char(pcm.package_applicable_date, 'DD-MM-YYYY') as \"packageApplicableDate\", "
				+ " pcm.package_applicable_date as \"packageApplicableDateNew\", "
				  + " to_char(pcm.package_applicable_date, 'MM/dd/yyyy') as \"packageApplicableDateDt\", "
				  + " to_char(pcm.package_applicable_date, 'MM/dd/yyyy') as \"packageApplicableDateDtold\", "
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
				+ "	to_char(pcm.created_date, 'DD/MM/YYYY') as \"createdDate\", "
				+ "	pcm.discountinued_remark as \"discountinuedRemark\", "
				+ "	pcm.tariff_id as \"tariffId\", "
				+ "	(case "
				+ "		when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) "
				+ "		when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) "
				+ "		else 'Admin' end) as \"userName\" , "
				+ "	pcm.is_force_close as \"isForceClose\", "
				+ "	bm.is_intermediate_bill as \"isIntermediateBill\" "
				+ "from "
				+ "	billing.t_package_consumption_master pcm "
				+ "inner join service.m_package_master packMaster on "
				+ "	packMaster.package_master_id = pcm.package_id "
				+ "inner join service.m_service_master srv on "
				+ "	packMaster.service_id = srv.service_master_id "
				+ "inner join public.t_order_details ordDetails on "
				+ "	ordDetails.order_details_id = pcm.order_detail_id "
				+ "inner join public.t_order_master ordMaster on "
				+ "	ordMaster.order_id = ordDetails.order_id "
				+ "left join billing.t_billing_details bd on "
				+ "	bd.order_details_id = ordDetails.order_details_id and bd.status = 'A' "
				+ "left join billing.m_billing_master bm on "
				+ "	bm.billing_master_id = bd.billing_master_id and bm.bill_cancelled = 'N' "
				+ "left join PUBLIC.m_employee_master empmst on "
				+ "	ordMaster.created_by = empmst.user_id "
				+ "left join doctor.m_doctor_master docmst on "
				+ "	ordMaster.created_by = docmst.user_id "
				+ "where "
				+ "	pcm.patient_id =:patientId "
				+ "	and pcm.package_type_id = 5 "
				+ "	and pcm.organisation_id =:organisationId "
				+ "	and pcm.unit_id =:unitId "
				+ "	and pcm.applicable_no_of_visit > 0 "
				+ "	and pcm.status = 'A' "
				+ "	and pcm.is_cancelled = 'N' "
				+ "	and pcm.is_discountinued = 'N' "
				+ "	and ( ( "
				+ "	case "
				+ "		when bm.bill_status_id is not null then bm.bill_status_id != 1 "
				+ "		else bm.bill_status_id is null end) "
				+ "	or bm.visit_type_id != 1 )" 
				+ " and ((case when bm.bill_status_id is null  and  (pcm.visit_type=1 and pcm.encounter_id < coalesce(:encounterId,0)) then bm.bill_status_id in (2,3) else (bm.bill_status_id is null or bm.bill_status_id != 1) end) or bm.visit_type_id != 1 ) "
				),
		
		@NamedNativeQuery(name = "GET_PACKAGE_LIST_FOR_CREDIT_NOTE", query = 
		"select "
		+ "	pcm.package_consumption_master_id as \"packageConsumptionMasterId\", "
		+ "	packMaster.package_master_id as \"packageId\", "
		+ "	ordMaster.order_no as \"orderNo\", "
		+ "	to_char(ordMaster.order_date, 'DD-MM-YYYY') as \"orderDate\", "
		+ "	to_char(ordMaster.order_date,'DD/MM/YYYY HH24:MI:SS') as \"orderDateString\", "
		+ "	ordMaster.order_date as \"orderDateNew\", "
		+ "	srv.service_standard_name as \"packageName\", "
		+ "	srv.service_standard_code as \"packageCode\", "
		+ "	packMaster.package_price as \"packagePrice\", "
		+ "	packMaster.package_type_id as \"packageTypeId\", "
		+ "	packMaster.service_id as \"packageServiceId\", "
		+ "	pcm.order_detail_id as \"orderDetailId\", "
		+ "	to_char(pcm.validity_from, 'DD-MM-YYYY') as \"validityFrom\", "
		+ "	to_char(coalesce(pcm.validity_to, now()), 'DD-MM-YYYY') as \"validityTo\", "
        + " to_char(pcm.package_applicable_date, 'DD-MM-YYYY') as \"packageApplicableDate\", "
        + " pcm.package_applicable_date as \"packageApplicableDateNew\", "
        + " to_char(pcm.package_applicable_date, 'MM/dd/yyyy') as \"packageApplicableDateDt\", "
        + " to_char(pcm.package_applicable_date, 'MM/dd/yyyy') as \"packageApplicableDateDtold\", "
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
		+ "	to_char(pcm.created_date, 'DD/MM/YYYY') as \"createdDate\", "
		+ "	pcm.discountinued_remark as \"discountinuedRemark\", "
		+ "	pcm.tariff_id as \"tariffId\" , "
		+ "	(case "
		+ "		when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) "
		+ "		when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) "
		+ "		else 'Admin' end) as \"userName\", "
		+ "	pcm.is_force_close as \"isForceClose\", "
		+ "	bm.is_intermediate_bill as \"isIntermediateBill\" "
		+ "from "
		+ "	billing.t_package_consumption_master pcm "
		+ "inner join service.m_package_master packMaster on "
		+ "	packMaster.package_master_id = pcm.package_id "
		+ "inner join service.m_service_master srv on "
		+ "	packMaster.service_id = srv.service_master_id "
		+ "inner join public.t_order_details ordDetails on "
		+ "	ordDetails.order_details_id = pcm.order_detail_id "
		+ "inner join public.t_order_master ordMaster on "
		+ "	ordMaster.order_id = ordDetails.order_id "
		+ "left join billing.t_billing_details bd on "
		+ "	bd.order_details_id = ordDetails.order_details_id and bd.status = 'A' "
		+ "left join billing.m_billing_master bm on "
		+ "	bm.billing_master_id = bd.billing_master_id and bm.bill_cancelled = 'N' "
		+ "left join PUBLIC.m_employee_master empmst on "
		+ "	ordMaster.created_by = empmst.user_id "
		+ "left join doctor.m_doctor_master docmst on "
		+ "	ordMaster.created_by = docmst.user_id "
		+ "where "
		+ "	pcm.patient_id =:patientId "
		+ "	and ((pcm.encounter_id =:encounterId "
		+ "	and pcm.visit_type =:visitTypeId) "
		+ "	or (pcm.admission_id =:encounterId "
		+ "	and pcm.visit_type =:visitTypeId)) "
		+ "	and pcm.organisation_id =:organisationId "
		+ "	and pcm.unit_id =:unitId "
		+ "	and pcm.status = 'A' "
		+ "	and pcm.is_cancelled = 'N' "
		+ "	and pcm.is_discountinued = 'N' "
		+ "	and pcm.package_type_id !=5 "
		+ "union all "
		+ "select "
		+ "	pcm.package_consumption_master_id as \"packageConsumptionMasterId\", "
		+ "	packMaster.package_master_id as \"packageId\", "
		+ "	ordMaster.order_no as \"orderNo\", "
		+ "	to_char(ordMaster.order_date, 'DD-MM-YYYY') as \"orderDate\", "
		+ "	to_char(ordMaster.order_date,'DD/MM/YYYY HH24:MI:SS') as \"orderDateString\", "
		+ "	ordMaster.order_date as \"orderDateNew\", "
		+ "	srv.service_standard_name as \"packageName\", "
		+ "	srv.service_standard_code as \"packageCode\", "
		+ "	packMaster.package_price as \"packagePrice\", "
		+ "	packMaster.package_type_id as \"packageTypeId\", "
		+ "	packMaster.service_id as \"packageServiceId\", "
		+ "	pcm.order_detail_id as \"orderDetailId\", "
		+ "	to_char(pcm.validity_from, 'DD-MM-YYYY') as \"validityFrom\", "
		+ "	to_char(pcm.validity_to, 'DD-MM-YYYY') as \"validityTo\", "
		+ " to_char(pcm.package_applicable_date, 'DD-MM-YYYY') as \"packageApplicableDate\", "
		+ " pcm.package_applicable_date as \"packageApplicableDateNew\", "
		  + " to_char(pcm.package_applicable_date, 'MM/dd/yyyy') as \"packageApplicableDateDt\", "
		  + " to_char(pcm.package_applicable_date, 'MM/dd/yyyy') as \"packageApplicableDateDtold\", "
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
		+ "	to_char(pcm.created_date, 'DD/MM/YYYY') as \"createdDate\", "
		+ "	pcm.discountinued_remark as \"discountinuedRemark\", "
		+ "	pcm.tariff_id as \"tariffId\", "
		+ "	(case "
		+ "		when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) "
		+ "		when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) "
		+ "		else 'Admin' end) as \"userName\" , "
		+ "	pcm.is_force_close as \"isForceClose\", "
		+ "	bm.is_intermediate_bill as \"isIntermediateBill\" "
		+ "from "
		+ "	billing.t_package_consumption_master pcm "
		+ "inner join service.m_package_master packMaster on "
		+ "	packMaster.package_master_id = pcm.package_id "
		+ "inner join service.m_service_master srv on "
		+ "	packMaster.service_id = srv.service_master_id "
		+ "inner join public.t_order_details ordDetails on "
		+ "	ordDetails.order_details_id = pcm.order_detail_id "
		+ "inner join public.t_order_master ordMaster on "
		+ "	ordMaster.order_id = ordDetails.order_id "
		+ "left join billing.t_billing_details bd on "
		+ "	bd.order_details_id = ordDetails.order_details_id and bd.status = 'A' "
		+ "left join billing.m_billing_master bm on "
		+ "	bm.billing_master_id = bd.billing_master_id and bm.bill_cancelled = 'N' "
		+ "left join PUBLIC.m_employee_master empmst on "
		+ "	ordMaster.created_by = empmst.user_id "
		+ "left join doctor.m_doctor_master docmst on "
		+ "	ordMaster.created_by = docmst.user_id "
		+ "where "
		+ "	pcm.patient_id =:patientId "
		+ "	and pcm.package_type_id = 5 "
		+ "	and pcm.organisation_id =:organisationId "
		+ "	and pcm.unit_id =:unitId "
		+ "	and pcm.applicable_no_of_visit > 0 "
		+ "	and pcm.status = 'A' "
		+ "	and pcm.is_cancelled = 'N' "
		+ "	and pcm.is_discountinued = 'N' "
		+ "	and ( ( "
		+ "	case "
		+ "		when bm.bill_status_id is not null then bm.bill_status_id in (1, 2, 3) "
		+ "		else bm.bill_status_id is null end) "
		+ "	or bm.visit_type_id != 1 )" 
		+ " and ((case when bm.bill_status_id is null  and  (pcm.visit_type=1 and pcm.encounter_id < coalesce(:encounterId,0)) then bm.bill_status_id in (1,2,3) else (pcm.unit_id =:unitId) end) or bm.visit_type_id != 1 ) "
		),
		
		
		
		@NamedNativeQuery(name = "GET_PACKAGE_LIST_FOR_IPD_PACKAGEORDER_FOR_BOOKVIEW", query = 
		"select "
		+ "	pcm.package_consumption_master_id as \"packageConsumptionMasterId\", "
		+ "	packMaster.package_master_id as \"packageId\", "
		+ "	ordMaster.order_no as \"orderNo\", "
		+ "	to_char(ordMaster.order_date, 'DD-MM-YYYY') as \"orderDate\", "
		+ "	srv.service_standard_name as \"packageName\", "
		+ "	srv.service_standard_code as \"packageCode\", "
		+ "	packMaster.package_price as \"packagePrice\", "
		+ "	packMaster.package_type_id as \"packageTypeId\", "
		+ "	packMaster.service_id as \"packageServiceId\", "
		+ "	pcm.order_detail_id as \"orderDetailId\", "
		+ "	to_char(pcm.validity_from, 'DD-MM-YYYY') as \"validityFrom\", "
		+ "	to_char(coalesce(pcm.validity_to,now()), 'DD-MM-YYYY') as \"validityTo\", "
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
		+ "	pcm.tariff_id as \"tariffId\" ,"
		+ "  (case when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) "
		+ "		when docmst.doctor_id is not null then concat('Dr. ',docmst.first_name, ' ', docmst.last_name) 	else 	'Admin' 	end) "
		+ "				 as \"userName\",pcm.is_force_close as \"isForceClose\" "
		+ "   "
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
		+ "left join billing.t_billing_details bd on "
		+ "	bd.order_details_id = ordDetails.order_details_id and bd.status = 'A' "
		+ "left join billing.m_billing_master bm on "
		+ "	bm.billing_master_id = bd.billing_master_id and bm.bill_cancelled = 'N' "
		+ "left join PUBLIC.m_employee_master empmst on "
		+ "	ordMaster.created_by = empmst.user_id "
		+ "left join doctor.m_doctor_master docmst on "
		+ "	ordMaster.created_by = docmst.user_id "
		+ " where "
		+ " to_char(pcm.created_date , 'YYYY-MM-DD') BETWEEN :fromDate " 
		+ " AND :toDate "
		+ "	AND pcm.patient_id =:patientId "
				
		+ "	and pcm.status = 'A' "
		+ "	and pcm.is_cancelled = 'N' "
		+ "	and pcm.is_discountinued = 'N' "
		
		),
		@NamedNativeQuery(name = "SEARCH_PACKAGE_COMMON_FILTER", 
		     query = "select  pkgtypmstr.package_type as \"packageType\" , parentpckgmstr.package_type_id as \"packageTypeId\"," 
		    	+ "	  srvismstr.service_standard_code as \"packageCode\" ,srvismstr.service_standard_name as \"packageName\","
		    	 + "	parentpckgmstr.parent_package_id as \"parentPackageId\", srvismstr.service_master_id as \"serviceId\" "
		    	 + "	from service.m_parent_package_master parentpckgmstr "
		    	 + "	 inner join service.m_service_master srvismstr on srvismstr.service_master_id = parentpckgmstr.service_id " 
		    	 + "	 inner join service.m_package_type_master pkgtypmstr on pkgtypmstr.package_type_id=parentpckgmstr.package_type_id " 
		    	 + "	 where parentpckgmstr.org_id=:orgId " 
		    	 + "	 and parentpckgmstr.unit_id=:unitId "
		    	 + "  and parentpckgmstr.package_type_id =:packageTypeId and parentpckgmstr.status = 'A' "),
		
		@NamedNativeQuery(name = "SEARCH_PACKAGE_FOR_BILLING", query =  "select "
				+ "	service.service_master_id as \"serviceMasterId\", "
				+ "	pack.package_master_id as \"packageMasterId\", "
				+ "	service.service_standard_name as \"serviceStandardName\" , "
				+ "	service.service_standard_code as \"serviceStandardCode\", "
				+ "	cast ( '0' as double precision ) as \"concession\", "
				+ "	speciality.speciality_id as \"specialityId\", "
				+ "	speciality.speciality_name as \"specialityName\", "
				+ "	sub_speciality.sub_speciality_id as \"subSpecialityId\", "
				+ "	sub_speciality.sub_speciality_name as \"subSpecialityName\", "
				+ "	details.is_rate_editable as \"isRateEditable\", "
				+ "	details.rate_editble_min_value as \"minRateEditable\", "
				+ "	details.rate_editble_min_value as \"maxRateEditable\", "
				+ "	details.is_tax_applicable as \"isTaxApplicable\", "
				+ "	details.is_discount as \"isDiscountApplicable\", "
				+ "	cast(0 as numeric) as \"discount\", "
				+ "	details.gst_type_id as \"taxId\","
				+ " details.is_ref_doctor_share as \"isRefDoctorShare\", "
				+ "	tax.name as \"taxName\", "
				+ "	coalesce( tax.tax_percentage, "
				+ "	0 ) as \"taxPercentage\", "
				+ "	details.is_allow_multiple_quantity as \"isQuantityEditable\", "
				+ "	cast( pack.package_price as numeric ) as \"basePrice\", "
				+ "	cast( 1 as integer ) as \"quantity\", "
				+ "	packType.package_type as \"packageType\", "
				+ "	pack.package_type_id as \"packageTypeId\" ,pack.min_age as \"minAge\", pack.max_age as \"maxAge\",pack.sex_id as \"sexId\" "
				+ " from "
				+ "	service.m_parent_package_master parentMstr "
				+ "	inner join service.m_package_master pack on parentMstr.parent_package_id=pack.parent_package_id "
				+ "	inner join service.m_package_type_master packType on packType.package_type_id = pack.package_type_id "
				+ "	inner join service.m_service_master service on service.service_master_id  = parentMstr.service_id "
				+ "    inner join service.t_unit_service_master_details details on details.service_master_id = service.service_master_id "
				+ "    inner join doctor.m_speciality_master speciality on service.speciality_id = speciality.speciality_id "
				+ "    inner join doctor.m_sub_speciality_master sub_speciality on service.sub_speciality_id = sub_speciality.sub_speciality_id "
				+ "    left join public.m_tax tax on tax.id=details.gst_type_id "
				+ "where "
				+ "	parentMstr.unit_id =:unitId "
				+ "  and details.unit_id=parentMstr.unit_id "
				+ "	and parentMstr.org_id =:orgId "
				+ "	and parentMstr.status = 'A'"
				+"  and pack.status = 'A'"),
		@NamedNativeQuery(name = "GET_PACKAGE_LIST_COUNT", query = "select count(*) "  
				+ " from " 
				+ "	service.m_parent_package_master parent "
				+ " inner join service.m_package_type_master ptype on 	ptype.package_type_id = parent.package_type_id "
				+ " inner join service.m_service_master srv on srv.service_master_id = parent.service_id "
				+ " where "
				+ "	parent.package_type_id =:packageTypeId "
				+ "	and parent.org_id =:orgId "
				+ "	and parent.unit_id =:unitId  "),


})

@NamedQueries({

		@NamedQuery(name = "GET_EHC_PACKAGES_LIST", query = "SELECT pm.packageMasterId as packageMasterId, "
				+ "pm.serviceId as serviceId, " + "service.serviceStandardName as serviceDescription "
				+ "FROM MPackageMaster pm " + "INNER JOIN pm.serviceMaster service " + "WHERE pm.sexId=:sexId "
				+ "AND :age > pm.minAge " + "AND :age < pm.maxAge " + "AND pm.orgId=:orgId " + "AND pm.unitId=:unitId "
				+ "AND pm.packageTypeId=1 " // packageTypeId=1 --> For EHC package
				+ "AND service.status='A' " + "AND pm.status='A' "),

})

@Entity
@Table(name = "m_package_master", schema = "service")
@SequenceGenerator(name = "package_master_seq", sequenceName = "service.package_master_seq", allocationSize = 1)
public class MPackageMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "package_master_seq")
	@Column(name = "package_master_id")
	private int packageMasterId;

	@Column(name = "parent_package_id")
	private Integer parentPackageId;

	@Column(name = "visit_type_id")
	private Integer visitTypeId;

	@Column(name = "patient_type_id")
	private Integer patientTypeId;

	@Column(name = "package_type_id")
	private Integer packageTypeId;

	@Column(name = "billing_bed_category_id")
	private Integer billingBedCategoryId;

	@Column(name = "payment_entitlement_type_id")
	private Integer paymentEntitlementTypeId;

	@Column(name = "sex_id")
	private Integer sexId;

	@Column(name = "validity_from_date")
	private Date validityFromDate;

	@Column(name = "validity_to_date")
	private Date validityToDate;

	@Column(name = "status")
	private char status;

	@Column(name = "package_cost")
	private double packageCost;

	@Column(name = "markup_down_in_percentage")
	private double markupDownInPercentage;

	@Column(name = "package_price")
	private double packagePrice;

	@Column(name = "is_manual_rounding_is_allow")
	private char isManualRoundingIsAllow;

	@Column(name = "manual_roundoff_amount")
	private double manualRoundoffAmount;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "validity_period_indays")
	private int validityPeriodIndays;

	@Column(name = "min_age")
	private Integer minAge;

	@Column(name = "max_age")
	private Integer maxAge;

	@Column(name = "service_id")
	private Integer serviceId;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "tariff_id")
	private Integer tariffId;

	@Column(name = "is_markup_down")
	private Integer isMarkupDown;

	@Column(name = "round_off_posit_neg")
	private Integer roundOffPositNeg;

	@Column(name = "no_of_encounters")
	private Integer noOfEncounters;
	
	@Column(name = "version_id")
	private Integer versionId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "billing_bed_category_id", insertable = false, updatable = false, nullable = false)
	private BillingBedCategoryMaster billingBedCategoryMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_entitlement_type_id", insertable = false, updatable = false, nullable = false)
	private PaymentEntitlementMaster paymentEntitlementMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id", insertable = false, updatable = false, nullable = false)
	private ServiceMaster serviceMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sex_id", insertable = false, updatable = false, nullable = false)
	private GenderMaster genderMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_type_id", insertable = false, updatable = false, nullable = false)
	private PackageTypeMaster packageTypeMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_type_id", insertable = false, updatable = false, nullable = false)
	private PatientCategoryMaster patientCategoryMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "visit_type_id", insertable = false, updatable = false, nullable = false)
	private VisitTypeMaster visitTypeMaster;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "mPackageMaster")
	private List<TPackageServicesDetails> listTPackageServicesDetails;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "mPackageMaster")
	private List<TPackageBedCategoryDetail> listTPackageBedCategoryDetail;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "mPackageMaster")
	private List<TPackageCapDetails> listTPackageCapDetails;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tariff_id", insertable = false, updatable = false, nullable = false)
	private TariffMaster tariffMaster;

	public Integer getNoOfEncounters() {
		return noOfEncounters;
	}

	public void setNoOfEncounters(Integer noOfEncounters) {
		this.noOfEncounters = noOfEncounters;
	}

	public Integer getIsMarkupDown() {
		return isMarkupDown;
	}

	public void setIsMarkupDown(Integer isMarkupDown) {
		this.isMarkupDown = isMarkupDown;
	}

	public Integer getRoundOffPositNeg() {
		return roundOffPositNeg;
	}

	public void setRoundOffPositNeg(Integer roundOffPositNeg) {
		this.roundOffPositNeg = roundOffPositNeg;
	}

	public TariffMaster getTariffMaster() {
		return tariffMaster;
	}

	public void setTariffMaster(TariffMaster tariffMaster) {
		this.tariffMaster = tariffMaster;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public int getPackageMasterId() {
		return packageMasterId;
	}

	public void setPackageMasterId(int packageMasterId) {
		this.packageMasterId = packageMasterId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}

	public Integer getPackageTypeId() {
		return packageTypeId;
	}

	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Integer getPaymentEntitlementTypeId() {
		return paymentEntitlementTypeId;
	}

	public void setPaymentEntitlementTypeId(Integer paymentEntitlementTypeId) {
		this.paymentEntitlementTypeId = paymentEntitlementTypeId;
	}

	public Integer getSexId() {
		return sexId;
	}

	public void setSexId(Integer sexId) {
		this.sexId = sexId;
	}

	public Date getValidityFromDate() {
		return validityFromDate;
	}

	public void setValidityFromDate(Date validityFromDate) {
		this.validityFromDate = validityFromDate;
	}

	public Date getValidityToDate() {
		return validityToDate;
	}

	public void setValidityToDate(Date validityToDate) {
		this.validityToDate = validityToDate;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public double getPackageCost() {
		return packageCost;
	}

	public void setPackageCost(double packageCost) {
		this.packageCost = packageCost;
	}

	public double getMarkupDownInPercentage() {
		return markupDownInPercentage;
	}

	public void setMarkupDownInPercentage(double markupDownInPercentage) {
		this.markupDownInPercentage = markupDownInPercentage;
	}

	public double getPackagePrice() {
		return packagePrice;
	}

	public void setPackagePrice(double packagePrice) {
		this.packagePrice = packagePrice;
	}

	public char getIsManualRoundingIsAllow() {
		return isManualRoundingIsAllow;
	}

	public void setIsManualRoundingIsAllow(char isManualRoundingIsAllow) {
		this.isManualRoundingIsAllow = (isManualRoundingIsAllow != '\u0000' ? isManualRoundingIsAllow : 'N');
	}

	public double getManualRoundoffAmount() {
		return manualRoundoffAmount;
	}

	public void setManualRoundoffAmount(double manualRoundoffAmount) {
		this.manualRoundoffAmount = manualRoundoffAmount;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public int getValidityPeriodIndays() {
		return validityPeriodIndays;
	}

	public void setValidityPeriodIndays(int validityPeriodIndays) {
		this.validityPeriodIndays = validityPeriodIndays;
	}

	public Integer getMinAge() {
		return minAge;
	}

	public void setMinAge(Integer minAge) {
		this.minAge = minAge;
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public BillingBedCategoryMaster getBillingBedCategoryMaster() {
		return billingBedCategoryMaster;
	}

	public void setBillingBedCategoryMaster(BillingBedCategoryMaster billingBedCategoryMaster) {
		this.billingBedCategoryMaster = billingBedCategoryMaster;
	}

	public PaymentEntitlementMaster getPaymentEntitlementMaster() {
		return paymentEntitlementMaster;
	}

	public void setPaymentEntitlementMaster(PaymentEntitlementMaster paymentEntitlementMaster) {
		this.paymentEntitlementMaster = paymentEntitlementMaster;
	}

	public ServiceMaster getServiceMaster() {
		return serviceMaster;
	}

	public void setServiceMaster(ServiceMaster serviceMaster) {
		this.serviceMaster = serviceMaster;
	}

	public GenderMaster getGenderMaster() {
		return genderMaster;
	}

	public void setGenderMaster(GenderMaster genderMaster) {
		this.genderMaster = genderMaster;
	}

	public PackageTypeMaster getPackageTypeMaster() {
		return packageTypeMaster;
	}

	public void setPackageTypeMaster(PackageTypeMaster packageTypeMaster) {
		this.packageTypeMaster = packageTypeMaster;
	}

	public PatientCategoryMaster getPatientCategoryMaster() {
		return patientCategoryMaster;
	}

	public void setPatientCategoryMaster(PatientCategoryMaster patientCategoryMaster) {
		this.patientCategoryMaster = patientCategoryMaster;
	}

	public VisitTypeMaster getVisitTypeMaster() {
		return visitTypeMaster;
	}

	public void setVisitTypeMaster(VisitTypeMaster visitTypeMaster) {
		this.visitTypeMaster = visitTypeMaster;
	}

	public List<TPackageServicesDetails> getListTPackageServicesDetails() {
		return listTPackageServicesDetails;
	}

	public void setListTPackageServicesDetails(List<TPackageServicesDetails> listTPackageServicesDetails) {
		this.listTPackageServicesDetails = listTPackageServicesDetails;
	}

	public List<TPackageBedCategoryDetail> getListTPackageBedCategoryDetail() {
		return listTPackageBedCategoryDetail;
	}

	public void setListTPackageBedCategoryDetail(List<TPackageBedCategoryDetail> listTPackageBedCategoryDetail) {
		this.listTPackageBedCategoryDetail = listTPackageBedCategoryDetail;
	}

	public List<TPackageCapDetails> getListTPackageCapDetails() {
		return listTPackageCapDetails;
	}

	public void setListTPackageCapDetails(List<TPackageCapDetails> listTPackageCapDetails) {
		this.listTPackageCapDetails = listTPackageCapDetails;
	}

	public Integer getParentPackageId() {
		return parentPackageId;
	}

	public void setParentPackageId(Integer parentPackageId) {
		this.parentPackageId = parentPackageId;
	}

	public Integer getVersionId() {
		return versionId;
	}

	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}
	
	

}