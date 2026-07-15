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
import com.param.billing.global.transaction.model.TariffMaster;
import com.param.global.model.AssociatedCompanyMaster;
import com.param.global.model.CompanyMaster;
import com.param.global.model.PaymentEntitlementMaster;
import com.param.global.model.VisitTypeMaster;

@NamedQueries({
@NamedQuery(name="GET_COMPANY_CONTRACT_BY_NAME_NOT_ID",
query="SELECT mCompanyContractMaster.contractId AS contractId "
			+ "	FROM MCompanyContractMaster mCompanyContractMaster"
			+ " WHERE (LOWER(mCompanyContractMaster.contractName)=:contractName OR mCompanyContractMaster.contractName=:contractName) "
			)
})

@NamedNativeQueries({
@NamedNativeQuery(name = "SEARCH_FOR_COMPANY_CONTRACT",
query = "select "
		+ " 	contractmst.contract_id as \"contractId\", "
		+ " 	company.company_desc as \"assCompanyName\", "
		+ " 	tariffmst.tariff_desc as \"tariffName\", "
		+ " 	paymst.payment_entitlement_desc as \"paymentEntitlementName\", "
		+ " 	contractmst.contract_name as \"contractName\", "
		+ " 	contractmst.validity_from as \"validityFrom\", "
		+ " 	contractmst.validity_to as \"validityTo\", "
		+ "     to_char(contractmst.validity_from,'dd/mm/yyyy') as \"validityFromString\", "
		+ "     to_char(contractmst.validity_to,'dd/mm/yyyy') as \"validityToString\", "
		+ " 	insurance.insurance_desc as \"insuranceName\", "
		+ " 	contractmst.co_share_percentage as \"companyShare\", "
		+ " 	contractmst.grade_id as \"gradeId\", "
		+ " 	grademst.grade_desc as \"gradeName\", "
		+ " 	company.ar_code_id as \"arCodeId\", "
		+ " 	arcode.ar_code_desc as \"arCodeDesc\", "
		+ " 	company.company_id as \"companyId\", "
		+ " 	paymst.payment_entitlement_id as \"paymentEntitlementId\", "
		+ " 	tariffmst.tariff_id as \"tariffId\", "
		+ " 	contractmst.visit_type_id as \"visitTypeId\", "
		+ " 	contractmst.is_tariff_rate_applicable as \"isTariffRateApplicable\", "
		+ " 	contractmst.associate_company_id as \"associateCompanyId\", "
		+ " 	contractmst.is_smart_applicable as \"isSmartApplicable\", "
		+ " 	visitTypeMaster.visit_type_name as \"visitTypeName\", "
		+ "		contractmst.version_no as \"versionNo\", "
		+ "		contractmst.is_versioning as \"isVersioning\", "
		+ "		contractmst.parent_contract_id as \"parentContractId\", "
		+ "		contractmst.old_contract_id as \"oldContractId\", "
		+ "		contractmst.status as \"status\", "
		+ "		contractmst.general_ledger_id as \"generalLedgerId\" "
		+ " from "
		+ " 	service.m_company_contract_master contractmst "
		+ " inner join t_company_master company on "
		+ " 	company.company_id = contractmst.company_id "
		+ " inner join public.m_payment_entitlement_master paymst on "
		+ " 	paymst.payment_entitlement_id = contractmst.payment_entitlement_id "
		+ " left join t_company_master associateCompany on "
		+ " 	associateCompany.company_id = contractmst.associate_company_id "
		+ " left join adt.t_insurance insurance on "
		+ " 	insurance.insurance_id = contractmst.associate_company_id "
		+ " inner join service.m_tariff_master tariffmst on "
		+ " 	tariffmst.tariff_id = contractmst.tariff_id "
		+ " left join service.m_contract_grade_master grademst on "
		+ " 	grademst.contract_grade_master_id = contractmst.grade_id "
		+ " left join m_ar_code_master arcode on "
		+ " 	arcode.ar_code_id = company.ar_code_id "
		+ " inner join public.m_visit_type_master visitTypeMaster on "
		+ " 	visitTypeMaster.visit_type_id = contractmst.visit_type_id "
		+ " WHERE "
		+ "   contractmst.org_id =:orgId "
		+ "   AND contractmst.unit_id =:orgUnitId "
		+ "    "),    //order by contractmst.contract_id desc

@NamedNativeQuery(name = "SEARCH_FOR_COMPANY_CONTRACT_FOR_VERSION",
query = "select "
		+ " 	contractmst.contract_id as \"contractId\", "
		+ " 	company.company_desc as \"assCompanyName\", "
		+ " 	tariffmst.tariff_desc as \"tariffName\", "
		+ " 	paymst.payment_entitlement_desc as \"paymentEntitlementName\", "
		+ " 	contractmst.contract_name as \"contractName\", "
		+ " 	contractmst.validity_from as \"validityFrom\", "
		+ " 	contractmst.validity_to as \"validityTo\", "
		+ " 	insurance.insurance_desc as \"insuranceName\", "
		+ " 	contractmst.co_share_percentage as \"companyShare\", "
		+ " 	contractmst.grade_id as \"gradeId\", "
		+ " 	grademst.grade_desc as \"gradeName\", "
		+ " 	company.ar_code_id as \"arCodeId\", "
		+ " 	arcode.ar_code_desc as \"arCodeDesc\", "
		+ " 	company.company_id as \"companyId\", "
		+ " 	paymst.payment_entitlement_id as \"paymentEntitlementId\", "
		+ " 	tariffmst.tariff_id as \"tariffId\", "
		+ " 	contractmst.visit_type_id as \"visitTypeId\", "
		+ " 	contractmst.is_tariff_rate_applicable as \"isTariffRateApplicable\", "
		+ " 	contractmst.associate_company_id as \"associateCompanyId\", "
		+ " 	contractmst.is_smart_applicable as \"isSmartApplicable\", "
		+ " 	visitTypeMaster.visit_type_name as \"visitTypeName\", "
		+ "		contractmst.version_no as \"versionNo\", "
		+ "		contractmst.is_versioning as \"isVersioning\", "
		+ "		contractmst.parent_contract_id as \"parentContractId\", "
		+ "		contractmst.old_contract_id as \"oldContractId\", "
		+ "		contractmst.status as \"status\" "
		+ " from "
		+ " 	service.m_company_contract_master contractmst "
		+ " inner join t_company_master company on "
		+ " 	company.company_id = contractmst.company_id "
		+ " inner join public.m_payment_entitlement_master paymst on "
		+ " 	paymst.payment_entitlement_id = contractmst.payment_entitlement_id "
		+ " left join t_company_master associateCompany on "
		+ " 	associateCompany.company_id = contractmst.associate_company_id "
		+ " left join adt.t_insurance insurance on "
		+ " 	insurance.insurance_id = contractmst.associate_company_id "
		+ " inner join service.m_tariff_master tariffmst on "
		+ " 	tariffmst.tariff_id = contractmst.tariff_id "
		+ " left join service.m_contract_grade_master grademst on "
		+ " 	grademst.contract_grade_master_id = contractmst.grade_id "
		+ " left join m_ar_code_master arcode on "
		+ " 	arcode.ar_code_id = company.ar_code_id "
		+ " inner join public.m_visit_type_master visitTypeMaster on "
		+ " 	visitTypeMaster.visit_type_id = contractmst.visit_type_id "
		+ " WHERE "
		+ "   contractmst.org_id =:orgId "
		+ "   AND contractmst.unit_id =:orgUnitId "
		+ "    "),

@NamedNativeQuery(name = "GET_CONTRACT_BY_COMPANYID",
query = "SELECT "
		+ "   contractmst.contract_id AS \"contractId\", "
		+ "   assocompany.company_desc as \"assCompanyName\", "
		+ "   assocompany.company_id as \"companyId\", "
		+ "	  contractmst.tariff_id as \"tariffId\",	"
		+ "	  contractmst.visit_type_id as \"visitTypeId\",	"
		+ "	  contractmst.billing_bed_category_id as \"billingBedCategoryId\",	"
		+ "   tariffmst.tariff_desc as \"tariffName\", "
		+ "   paymst.payment_entitlement_desc AS \"paymentEntitlementName\", "
		+ "   contractmst.contract_name AS \"contractName\", "
		+ "   to_char(contractmst.validity_from,'dd/MM/yyyy') AS \"validityFrom\", "
		+ "   to_char(contractmst.validity_to,'dd/MM/yyyy') AS \"validityTo\" "
		+ " FROM "
		+ "   service.m_company_contract_master contractmst "
		+ "   INNER JOIN public.m_payment_entitlement_master paymst on paymst.payment_entitlement_id = contractmst.payment_entitlement_id "
		+ "   INNER JOIN  t_company_master assocompany on assocompany.company_id = contractmst.company_id "
		+ "   INNER JOIN  service.m_tariff_master tariffmst on tariffmst.tariff_id = contractmst.tariff_id "
		+ " WHERE "
		+ "   contractmst.org_id =:orgId "
		+ "   AND contractmst.unit_id =:unitId "
		+ "	  AND contractmst.company_id =:companyId "
		+ "   AND contractmst.payment_entitlement_id =:companyTypeId "
		+ "	  AND contractmst.status='A' "),

@NamedNativeQuery(name = "GET_CONTRACT_BY_COMPANYID_FOR_INSURANCE",
query = "SELECT "
		+ "   contractmst.contract_id AS \"contractId\", "
		+ "   assocompany.company_desc as \"assCompanyName\", "
		+ "   assocompany.company_id as \"companyId\", "
		+ "	  contractmst.tariff_id as \"tariffId\",	"
		+ "	  contractmst.visit_type_id as \"visitTypeId\",	"
		+ "	  contractmst.billing_bed_category_id as \"billingBedCategoryId\",	"
		+ "   tariffmst.tariff_desc as \"tariffName\", "
		+ "   paymst.payment_entitlement_desc AS \"paymentEntitlementName\", "
		+ "   contractmst.contract_name AS \"contractName\", "
		+ "   to_char(contractmst.validity_from,'dd/MM/yyyy') AS \"validityFrom\", "
		+ "   to_char(contractmst.validity_to,'dd/MM/yyyy') AS \"validityTo\" "
		+ " FROM "
		+ "   service.m_company_contract_master contractmst "
		+ "   INNER JOIN public.m_payment_entitlement_master paymst on paymst.payment_entitlement_id = contractmst.payment_entitlement_id "
		+ "   INNER JOIN  t_company_master assocompany on assocompany.company_id = contractmst.company_id "
		+ "   INNER JOIN  service.m_tariff_master tariffmst on tariffmst.tariff_id = contractmst.tariff_id "
		+ " WHERE "
		+ "   contractmst.org_id =:orgId "
		+ "   AND contractmst.status='A' AND contractmst.unit_id =:unitId AND contractmst.company_id =:companyId AND contractmst.payment_entitlement_id =:companyTypeId AND contractmst.associate_company_id=:associateCompanyId "),

@NamedNativeQuery(name="GET_CONTARCT_CONSUMPTION_SERVICE_DETAILS_BY_CONTARCTID_SERVICEID",query="SELECT"
			 + "   csd.contract_id AS \"contractId\","
			 + "   csd.final_price AS \"finalPrice\" " 
			 + "   FROM service.t_contract_service_details csd " 
			 + "   inner join service.m_company_contract_master ccm on csd.contract_id = ccm.contract_id "
			 + "    inner join service.t_contract_billing_bed_category_detail details on details.contract_id = ccm.contract_id "
			 + "   WHERE csd.contract_id=:contractId "
			 + "  AND csd.service_id=:serviceId "
			 + "  and ccm.visit_type_id=:visitTypeId "
			 + "  and details.billing_bed_category_id=:billingBedCategoryid "
			 + "  and ccm.patient_type_id =:patientTypeId "
			 //+ "  and ccm.payment_entitlement_id=:paymentEntitlementId "
			 + "  and ccm.org_id=:organizationId "
			 + "  and ccm.unit_id=:unitId "
			 + "  and :currentDate between ccm.validity_from and ccm.validity_to "
			 + " limit 1"),

@NamedNativeQuery(name="GET_CHARGE_CONVERSION_LIST_BY_CUREENT_PAYER_PAYEE_ID",
query="SELECT" 
		+ "   om.order_no as \"orderNo\","
		+ "   od.service_id as \"serviceId\","
		+ "   od.order_details_id as \"orderDetailsId\","
		+ "   to_char(om.order_date,'dd-mm-yyyy') as \"orderDate\","
		+ "   msm.service_standard_code as \"serviceCode\","
		+ "   msm.service_standard_name as \"serviceName\","
		+ "   cast(od.service_amt as double precision) as \"serviceAmount\","
		+ "   od.order_qty as \"orderQyt\","
		+ "   cast(od.ord_total_amt as double precision ) as \"totalAmount\","
		+ "   cast(od.ord_concession as double precision) as \"concession\","
		+ "   cast(od.ord_discount as double precision) as \"discountAmount\","
		+ "   cast(od.tax_amount as double precision) as \"gstAmt\","
		+ "   cast(od.net_amount as double precision) as \"netAmount\""
		+ " FROM"
		+ "   public.t_order_details od"
		+ "   INNER JOIN public.t_order_master om on om.order_id = od.order_id"
		+ "   INNER JOIN service.m_service_master msm on msm.service_master_id = od.service_id"
		+ " WHERE od.payee_id=:payeeId "
		+ "  AND om.encounter_id= :encounterId "
		+ "  AND  ord_cancelled = 'N' "), //AND service_is_billed =0

@NamedNativeQuery(name="GET_CHARGE_CONVERSION_LIST_BY_CONVERT_PAYER_CONTARCT_ID",
query="SELECT "
		+ "   cast(csd.service_price as double precision) as \"serviceAmount\","
		+ "   csd.service_id as \"serviceId\","
		+ "   ccm.contract_id as \"contractId\""
		+ " FROM"
		+ "   service.m_company_contract_master ccm"
		+ "   INNER JOIN service.t_contract_service_details csd on csd.contract_id = ccm.contract_id"
		+ " WHERE"
		+ "   ccm.contract_id =:contractId " //6
		+ "   and csd.service_id IN (:serviceId) "), //76

//Added by sidheshwar
@NamedNativeQuery(name="GET_COMPANY_CONTRACT_SERVICE_DETAIL_BY_CONTRACT_ID",
query=			"select "
		+ " 	msm.service_standard_code as \"serviceCode\", "
		+ " 	spl.speciality_id as \"departmentId\", "
		+ " 	subspl.sub_speciality_id as \"subDepartmentId\", "
		+ " 	subspl.sub_speciality_name as \"subDepartmentName\", "
		+ " 	spl.speciality_name as \"departmentName\", "
		+ "		conserdet.service_id as \"serviceId\", "
		+ " 	msm.service_standard_name as \"serviceName\", "
		+ " 	conserdet.contract_id as \"contractId\", "
		+ " 	conserdet.service_price as \"servicePrice\", "
		+ " 	conserdet.final_price as \"finalPrice\", "
		+ " 	conserdet.apportioned_price as \"apportionedPrice\", "
		+ " 	conserdet.self_payable as \"selfPayable\", "
		+ " 	conserdet.credit_payable as \"creditPayable\", "
		+ " 	conserdet.tax_id as \"taxId\", "
		+ " 	coalesce(conserdet.tax_percentage,0) as \"taxPercentage\" "
		+ " from "
		+ " 	service.t_contract_service_details conserdet "
		+ " inner join service.m_service_master msm on "
		+ " 	msm.service_master_id = conserdet.service_id "
		+ " inner join doctor.m_speciality_master spl on "
		+ "		spl.speciality_id=	msm.speciality_id "
		+ " inner join doctor.m_sub_speciality_master subspl on "
		+ "		subspl.sub_speciality_id=msm.sub_speciality_id "
		+ " inner join service.t_unit_service_master_details usmd on "
		+ "		usmd.service_master_id=msm.service_master_id  "
		/*+ " left join m_tax tax on "
		+ "		tax.id= usmd.gst_type_id "*/
		+ " where "
		+ " 	usmd.unit_id=conserdet.unit_id "
		+ " 	and conserdet.contract_id =:contractId "
		+ " 	and conserdet.org_id =:orgId "
		+ " 	and conserdet.unit_id =:unitId	 "
		+ "		and conserdet.is_package='N' "),

@NamedNativeQuery(name="GET_COMPANY_CONTRACT_PACKAGE_DETAIL_BY_CONTRACT_ID",
query=			"select "
		+ " 	msm.service_standard_code as \"serviceCode\", "
		+ " 	spl.speciality_id as \"departmentId\", "
		+ " 	subspl.sub_speciality_id as \"subDepartmentId\", "
		+ " 	subspl.sub_speciality_name as \"subDepartmentName\", "
		+ " 	spl.speciality_name as \"departmentName\", "
		+ "		conserdet.service_id as \"serviceId\", "
		+ " 	msm.service_standard_name as \"serviceName\", "
		+ " 	conserdet.contract_id as \"contractId\", "
		+ " 	conserdet.service_price as \"servicePrice\", "
		+ " 	conserdet.final_price as \"finalPrice\", "
		+ " 	conserdet.apportioned_price as \"apportionedPrice\", "
		+ " 	conserdet.self_payable as \"selfPayable\", "
		+ " 	conserdet.credit_payable as \"creditPayable\", "
		+ " 	conserdet.tax_id as \"taxId\", "
		+ " 	bbc.billing_bed_category_id as \"billingBedCategoryId\", "
		+ " 	bbc.billing_bed_desc as \"billingBedCategoryDesc\", "
		+ " 	pcm.patient_category as \"patientCategoryDesc\", "
		+ " 	pcm.patient_category_id as \"patientCategoryId\", "
		+ "		conserdet.package_master_id as \"packageMasterId\", "
		+ "		pm.package_type_id as \"packagTypeId\", "
		+ " 	coalesce(conserdet.tax_percentage,0) as \"taxPercentage\" "
		+ " from "
		+ " 	service.t_contract_service_details conserdet "
		+ " inner join service.m_service_master msm on "
		+ " 	msm.service_master_id = conserdet.service_id "
		+ " inner join doctor.m_speciality_master spl on "
		+ "		spl.speciality_id=	msm.speciality_id "
		+ " inner join doctor.m_sub_speciality_master subspl on "
		+ "		subspl.sub_speciality_id=msm.sub_speciality_id "
		+ " inner join service.t_unit_service_master_details usmd on "
		+ "		usmd.service_master_id=msm.service_master_id  "
		+ " left join adt.m_billing_bed_category_master bbc on "
		+ "		bbc.billing_bed_category_id =conserdet.billing_bed_category_id  "
		+ " left join public.m_patient_category_master pcm on "
		+ "		pcm.patient_category_id =conserdet.patient_category_id  "
        + " left join service.m_package_master pm on "
        + "		pm.package_master_id =conserdet.package_master_id  "

	/*	+ " left join m_tax tax on "
		+ "		tax.id= usmd.gst_type_id "*/
		+ " where "
		+ " 	usmd.unit_id=conserdet.unit_id "
		+ " 	and conserdet.contract_id =:contractId "
		+ " 	and conserdet.org_id =:orgId "
		+ " 	and conserdet.unit_id =:unitId	 "
		+ "		and conserdet.is_package='Y' "),


@NamedNativeQuery(name="GET_COMPANY_CONTRACT_BILLING_BED_CATEGORY_BY_CONTRACT_ID",
query="select bcm.billing_bed_desc as \"billingBedCategory\", "
        + " bcm.billing_bed_category_id as \"billingBedCategoryId\" "
		+ " from service.t_contract_billing_bed_category_detail cbbcd"
		+ "   inner join adt.m_billing_bed_category_master bcm on bcm.billing_bed_category_id=cbbcd.billing_bed_category_id "
		+ " where cbbcd.organisation_id=:orgId and cbbcd.unit_id=:unitId and cbbcd.contract_id=:contractId  "),  

@NamedNativeQuery(name="GET_COMPANY_CONTRACT_PROCEDURE_DETAIL_BY_CONTRACT_ID",
query=    "select "
		+ " 	cpd.department_id as \"departmentId\", "
		+ " 	sm.speciality_name as \"specialityName\", "
		+ " 	serm.service_standard_name as \"procedureName\", "
		+ "		cpd.procedure_id as \"procedureId\", "
		+ " 	cpd.cap_amount as \"capAmount\" "
		+ " from "
		+ " 	service.t_contract_procedure_details cpd "
		+ " inner join service.m_service_master serm on "
		+ " 	serm.service_master_id = cpd.procedure_id "
		+ " inner join doctor.m_speciality_master sm on "
		+ " 	sm.speciality_id = cpd.department_id "
		+ " where "
		+ " 	cpd.contract_id =:contractId "
		+ " 	and cpd.org_id =:orgId "
		+ " 	and cpd.unit_id =:unitId "),

@NamedNativeQuery(name="GET_COMPANY_CONTRACT_CAP_DETAIL_LIST_BY_CONTRACT_ID",
query=	  "select "
		+ " 	ccd.department_id as \"departmentId\", "
		+ " 	ccd.sub_department_id as \"subDepartmentId\", "
		+ " 	sm.speciality_name as \"specialityName\", "
		+ " 	ccd.department_cap_amount as \"departmentCapAmount\" "
		+ " from "
		+ " 	service.t_contract_cap_details ccd "
		+ " inner join doctor.m_speciality_master sm on "
		+ " 	sm.speciality_id = ccd.department_id "
		+ " where "
		+ " 	ccd.contract_id =:contractId "
		+ " 	and ccd.org_id =:orgId "
		+ " 	and ccd.unit_id =:unitId "
		+ "		and ccd.department_cap_amount is not null "),


@NamedNativeQuery(name="GET_COMPANY_CONTRACT_SERVICE_CAP_DETAIL_BY_CONTRACT_ID",
query= "select "
		+ " 	ccd.service_id as \"serviceId\", "
		+ " 	service.service_standard_name as \"serviceName\", "
		+ " 	ccd.amount as \"amount\", "
		+ " 	cast(ccd.quantity as double precision) as \"quantity\", "
		+ " 	ccd.applicable_days as \"applicableDays\", "
		+ " 	spl.speciality_name as \"groupDesc\", "
		+ " 	ccd.till_discharge as \"tillDischargeFlag\", "
		+ " 	sub.sub_speciality_name as \"subGroupDesc\" "
		+ " from "
		+ " 	service.t_contract_cap_details ccd "
		+ " inner join service.m_service_master service on "
		+ " 	service.service_master_id = ccd.service_id "
		+ " inner join doctor.m_speciality_master spl on "
		+ " 	spl.speciality_id = service.speciality_id "
		+ " inner join doctor.m_sub_speciality_master sub on "
		+ " 	sub.sub_speciality_id = service.sub_speciality_id "
		+ " where "
		+ " 	ccd.contract_id = :contractId "
		+ " 	and ccd.org_id = :orgId "
		+ " 	and ccd.unit_id = :unitId "),

@NamedNativeQuery(name="GET_COMPANY_CONTRACT_PRODUCT_CAP_DETAIL_LIST_BY_CONTRACT_ID",
query=    "select "
		+ "	pcat.category as \"productCategory\", "
		+ "	capdet.quantity as \"quantity\", "
		+ "	capdet.amount as \"amount\", "
		+ "	pcat.asset_category_id as \"assetCategoryId\", "
		+ "	capdet.product_category_id as \"productCategoryId\", "
		+ "	capdet.prod_cat_co_share as \"prodCatCoShare\" "
		+ "from "
		+ "	service.t_contract_cap_details capdet "
		+ "inner join public.m_product_category pcat on "
		+ "	pcat.id = capdet.product_category_id "
		+ "where "
		+ "	capdet.contract_id =:contractId "
		+ "	and capdet.org_id =:orgId "
		+ "	and capdet.unit_id =:unitId "),

@NamedNativeQuery(name="GET_COMPANY_CONTRACT_GROP_EXCLUSION_CONTRACT_ID",
query="select pcat.category as \"productCategory\",capdet.quantity as \"quantity\" "
		+ " from service.t_contract_cap_details capdet"
		+ "   inner join public.m_product_category pcat on pcat.id=capdet.product_category_id "
		+ " WHERE"
		+ "   capdet.contract_id =:contractId " 
		+ "   and capdet.org_id=:orgId and capdet.unit_id=:unitId "),

@NamedNativeQuery(name="GET_COMPANY_CONTRACT_GROP_DETAILS_BY_CONTRACT_ID",query=
		" select "
		+ " 	contract_id as \"contractId\", "
		+ " 	department_id as \"departmentId\", "
		+ " 	sub_department_id as \"subDepartmentId\", "
		+ " 	spl.speciality_name as \"groupDesc\", "
		+ " 	sub.sub_speciality_name as \"subGroupDesc\", "
		+ " 	co_share_percentage as \"coShare\","
		+ "		variance_up_down as \"varianceUpDown\", "
		+ "		variance_percentage as \"apportionedPercent\"  "
		+ " from "
		+ " 	service.t_contract_group_details dtls "
		+ " inner join doctor.m_speciality_master spl on "
		+ " 	spl.speciality_id = dtls.department_id "
		+ " inner join doctor.m_sub_speciality_master sub on "
		+ " 	sub.sub_speciality_id = dtls.sub_department_id "
		+ " where "
		+ " 	dtls.unit_id = :unitId "
		+ " 	and contract_id = :contractId "),

@NamedNativeQuery(name="GET_COMPANY_CONTRACT_GROP_EXCLUSION_BY_CONTRACT_ID",query=
		"select "
		+ " 	contract_id as \"contractId\", "
		+ " 	department_id as \"departmentId\", "
		+ " 	sub_department_id as \"subDepartmentId\", "
		+ " 	spl.speciality_name as \"groupDesc\", "
		+ " 	sub.sub_speciality_name as \"subGroupDesc\" "
		+ " from "
		+ " 	service.t_contract_group_exclusion ex "
		+ " inner join doctor.m_speciality_master spl on "
		+ " 	spl.speciality_id = ex.department_id "
		+ " inner join doctor.m_sub_speciality_master sub on "
		+ " 	sub.sub_speciality_id = ex.sub_department_id "
		+ " where "
		+ " 	ex.unit_id = :unitId "
		+ " 	and contract_id = :contractId "),

@NamedNativeQuery(name="GET_COMPANY_CONTRACT_PHARMACY_EXCLUSION_BY_CONTRACT_ID",query="select "
		+ " 	contract_id as \"contractId\", "
		+ " 	product_category_id as \"productCategoryId\", "
		+ " 	prodcat.category as \"category\", "
		+ "		prodcat.asset_category_id as \"assetCategoryId\" "
		+ " from "
		+ " 	service.t_contract_pharmacy_exclusion phexcl "
		+ " inner join m_product_category prodcat on "
		+ " 	prodcat.id = phexcl.product_category_id "
		+ " where "
		+ " 	phexcl.unit_id =:unitId "
		+ " 	and contract_id = :contractId "),

@NamedNativeQuery(name="GET_CONTRACT_ITEM_INCLUDED_AFTER_PHARMACY_EXCLUSION_BY_CONTRACT_ID",query=" select "
		+ " 	contractItem.contract_id as \"contractId\", "
		+ " 	contractItem.product_category_id as \"productCategoryId\", "
		+ " 	prodcat.category as \"category\", "
		+ " 	contractItem.item_id as \"itemId\", "
		+ " 	item.code as \"itemCode\", "
		+ " 	item.name as \"itemName\", "
		+ " 	prodcat.asset_category_id as \"assetCategoryId\" "
		+ " from "
		+ " 	service.t_contract_item_details contractItem "
		+ " inner join m_product_category prodcat on "
		+ " 	prodcat.id = contractItem.product_category_id "
		+ " inner join m_item item on "
		+ " 	item.id = contractItem.item_id "
		+ " where "
		+ " 	contractItem.product_category_id in ( "
		+ " 		select product_category_id "
		+ " 	from "
		+ " 		service.t_contract_pharmacy_exclusion phexcl "
		+ " 	where "
		+ " 		phexcl.unit_id = :unitId "
		+ " 		and phexcl.contract_id = :contractId) "
		+ " 	and contractItem.contract_id = :contractId "
		+ "		and contractItem.unit_id=:unitId "
		+ "		and contractItem.is_equipment='N' "),
})



@Entity
@Table(name = "m_company_contract_master" , schema = "service")
@SequenceGenerator(name = "m_company_contract_master_seq" , sequenceName = "service.m_company_contract_master_seq" , allocationSize = 1)
public class MCompanyContractMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "m_company_contract_master_seq")
	@Column(name = "contract_id")
	private int contractId;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "payment_entitlement_id")
	private Integer paymentEntitlementId;
	
	@Column(name = "associate_company_id")
	private Integer associateCompanyId;
	
	@Column(name = "contract_name")
	private String contractName;
	
	@Column(name = "validity_from")
	private Date validityFrom;
	
	@Column(name = "validity_to")
	private Date validityTo;
	
	@Column(name = "grade_id")
	private Integer gradeId;
	
	@Column(name = "co_share_percentage")
	private double coSharePercentage;
	
	@Column(name = "tariff_id")
	private Integer tariffId;
	
	@Column(name = "is_tariff_rate_applicable")
	private char isTariffRateApplicable;
	
	@Column(name = "billing_bed_category_id")
	private Integer billingBedCategoryId;
	
	@Column(name = "visit_type_id")
	private Integer visitTypeId;
	
	@Column(name = "patient_type_id")
	private Integer patientTypeId;
	
	@Column(name = "company_id")
	private Integer companyId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "is_smart_applicable")
	private Character isSmartApplicable;
	
	@Column(name = "version_no")
	private Integer versionNo;
	
	@Column(name = "old_contract_id")
	private Integer oldContractId;
	
	@Column(name = "parent_contract_id")
	private Integer parentContractId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name  = "associate_company_id" , insertable = false , updatable = false , nullable = false)
	private AssociatedCompanyMaster associatedCompanyMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name  = "billing_bed_category_id" , insertable = false , updatable = false , nullable = false)
	private BillingBedCategoryMaster billingBedCategoryMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name  = "company_id" , insertable = false , updatable = false , nullable = false)
	private CompanyMaster companyMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name  = "grade_id" , insertable = false , updatable = false , nullable = false)
	private ContractGradeMaster contractGradeMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name  = "payment_entitlement_id" , insertable = false , updatable = false , nullable = false)
	private PaymentEntitlementMaster paymentEntitlementMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name  = "tariff_id" , insertable = false , updatable = false , nullable = false)
	private TariffMaster tariffMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name  = "visit_type_id" , insertable = false , updatable = false , nullable = false)
	private VisitTypeMaster visitTypeMaster;
	
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "mCompanyContractMaster")
	private List<TContractCapDetails> listTContractCapDetails;
	
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "mCompanyContractMaster")
	private List<TContractServiceDetails> listTContractServiceDetails;

	@Column(name = "general_ledger_id")
	private Integer generalLedgerId;
	
	public Integer getParentContractId() {
		return parentContractId;
	}

	public void setParentContractId(Integer parentContractId) {
		this.parentContractId = parentContractId;
	}

	public Integer getOldContractId() {
		return oldContractId;
	}

	public void setOldContractId(Integer oldContractId) {
		this.oldContractId = oldContractId;
	}

	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public List<TContractServiceDetails> getListTContractServiceDetails() {
		return listTContractServiceDetails;
	}

	public void setListTContractServiceDetails(List<TContractServiceDetails> listTContractServiceDetails) {
		this.listTContractServiceDetails = listTContractServiceDetails;
	}

	public List<TContractCapDetails> getListTContractCapDetails() {
		return listTContractCapDetails;
	}

	public void setListTContractCapDetails(List<TContractCapDetails> listTContractCapDetails) {
		this.listTContractCapDetails = listTContractCapDetails;
	}

	public int getContractId() {
		return contractId;
	}

	public void setContractId(int contractId) {
		this.contractId = contractId;
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

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getAssociateCompanyId() {
		return associateCompanyId;
	}

	public void setAssociateCompanyId(Integer associateCompanyId) {
		this.associateCompanyId = associateCompanyId;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public Date getValidityFrom() {
		return validityFrom;
	}

	public void setValidityFrom(Date validityFrom) {
		this.validityFrom = validityFrom;
	}

	public Date getValidityTo() {
		return validityTo;
	}

	public void setValidityTo(Date validityTo) {
		this.validityTo = validityTo;
	}

	public Integer getGradeId() {
		return gradeId;
	}

	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}

	public double getCoSharePercentage() {
		return coSharePercentage;
	}

	public void setCoSharePercentage(double coSharePercentage) {
		this.coSharePercentage = coSharePercentage;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public char getIsTariffRateApplicable() {
		return isTariffRateApplicable;
	}

	public void setIsTariffRateApplicable(char isTariffRateApplicable) {
		this.isTariffRateApplicable = isTariffRateApplicable;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
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

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public AssociatedCompanyMaster getAssociatedCompanyMaster() {
		return associatedCompanyMaster;
	}

	public void setAssociatedCompanyMaster(AssociatedCompanyMaster associatedCompanyMaster) {
		this.associatedCompanyMaster = associatedCompanyMaster;
	}

	public BillingBedCategoryMaster getBillingBedCategoryMaster() {
		return billingBedCategoryMaster;
	}

	public void setBillingBedCategoryMaster(BillingBedCategoryMaster billingBedCategoryMaster) {
		this.billingBedCategoryMaster = billingBedCategoryMaster;
	}

	public CompanyMaster getCompanyMaster() {
		return companyMaster;
	}

	public void setCompanyMaster(CompanyMaster companyMaster) {
		this.companyMaster = companyMaster;
	}

	public ContractGradeMaster getContractGradeMaster() {
		return contractGradeMaster;
	}

	public void setContractGradeMaster(ContractGradeMaster contractGradeMaster) {
		this.contractGradeMaster = contractGradeMaster;
	}

	public PaymentEntitlementMaster getPaymentEntitlementMaster() {
		return paymentEntitlementMaster;
	}

	public void setPaymentEntitlementMaster(PaymentEntitlementMaster paymentEntitlementMaster) {
		this.paymentEntitlementMaster = paymentEntitlementMaster;
	}

	public TariffMaster getTariffMaster() {
		return tariffMaster;
	}

	public void setTariffMaster(TariffMaster tariffMaster) {
		this.tariffMaster = tariffMaster;
	}

	public VisitTypeMaster getVisitTypeMaster() {
		return visitTypeMaster;
	}

	public void setVisitTypeMaster(VisitTypeMaster visitTypeMaster) {
		this.visitTypeMaster = visitTypeMaster;
	}

	public Character getIsSmartApplicable() {
		return isSmartApplicable;
	}

	public void setIsSmartApplicable(Character isSmartApplicable) {
		 this.isSmartApplicable = (isSmartApplicable == null) ? 'N' : isSmartApplicable;
	}

	public Integer getGeneralLedgerId() {
		return generalLedgerId;
	}

	public void setGeneralLedgerId(Integer generalLedgerId) {
		this.generalLedgerId = generalLedgerId;
	}
	
	
}
