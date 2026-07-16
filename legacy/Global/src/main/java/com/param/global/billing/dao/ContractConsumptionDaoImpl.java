package com.param.global.billing.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.billing.dto.CommonBillingItemsDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.TaxDto;
import com.param.global.pharmacy.dto.SaleDetailPayeeMapperDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"unchecked","rawtypes"})
public class ContractConsumptionDaoImpl implements IContractConsumptionDao,ICommonConstants{

	final static Logger logger = Logger.getLogger(ContractConsumptionDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public List<ContractConsumptionDto> getServiceDetailsByContractId(ContractConsumptionDto contractConsumptionDto)throws ServiceException {
		try {
			
			List<ContractConsumptionDto> consumptionDto = new ArrayList<ContractConsumptionDto>();
			
			if(contractConsumptionDto.getAdmissionId() !=null)
			{
				
			StringBuilder query= new StringBuilder(" select " 
					+ " 	csd.service_id as \"serviceId\", " 
					+ " 	spons.priority_id as \"priorityId\", " 
					+ " 	coalesce(cast(unitservice.rate_editable_max_value as double precision), 0) as \"maxRateEditable\", " 
					+ " 	coalesce(cast(unitservice.rate_editble_min_value as double precision), 0) as \"minRateEditable\", " 
					+ " 	csd.contract_id as \"contractId\", " 
					+ " 	csd.apportioned_price as \"serviceRate\", " 
					+ " 	com.company_desc as \"companyName\", " 
					+ " 	CAST( CASE   WHEN (csd.final_price = 0 or csd.self_payable = 0) THEN 0  ELSE (csd.self_payable / csd.final_price) * 100   END AS NUMERIC(15, 10)) AS \"coShare\" , "
					+ " 	spons.payee_id as \"payeeId\", " 
					+ " 	spons.insurance_id as \"associateCompanyId\", " 
					+ " 	csd.tax_id as \"taxId\", " 
					+ " 	coalesce(csd.tax_percentage, 0) as \"taxPercentage\", " 
					+ " 	csd.final_price as \"finalPrice\", " 
					+ " 	csd.self_payable as \"selfPayable\", " 
					+ " 	csd.credit_payable as \"creaditPayable\", " 
					+ " 	( " 
					+ " 	select " 
					+ " 		ccd.balance_cap_amount " 
					+ " 	from " 
					+ " 		service.t_vist_contract_cap_mapper ccd " 
					+ " 	where " 
					+ " 		ccd.department_id = service.speciality_id " 
					+ " 		and ccd.contract_id = csd.contract_id " 
					+ " 		and ccd.admission_id =:admissionId " 
					+ " 		and ccd.status = 'A' " 
					+ " 		and ccd.service_id is null) as \"balanceCapAmount\", " 
					+ " 	( " 
					+ " 	select " 
					+ " 		billing_bed_category_id " 
					+ " 	from " 
					+ " 		service.t_contract_billing_bed_category_detail " 
					+ " 	where " 
					+ " 		contract_id =( " 
					+ " 		select " 
					+ " 			contract_id " 
					+ " 		from " 
					+ " 			opd.t_patient_sponsor_details spons " 
					+ " 		where " 
					+ " 			status = 'A' " 
					+ " 			and priority_id = 1 " 
					+ " 			and admission_id = adm.admission_id) " 
					+ " 			and billing_bed_category_id =:billingBed) as \"billingBedExist\", " 
					+ " 	0 as \"amount\", " 
					+ " 	0 as \"balanceAmount\", " 
					+ " 	0 as \"quantity\", " 
					+ " 	0 as \"balanceQuantity\", " 
					+ " 	0 as \"applicableDays\", " 
					+ " 	'' as \"applicableDate\", " 
					+ " 	service.speciality_id as \"specialityId\", " 
					+ " 	service.sub_speciality_id as \"subSpecialityId\", " 
					+ " 	ccm.tariff_id as \"tariffId\", " 
					+ " 	to_char(adm.doa, 'yyyy-MM-dd') as \"doa\", " 
					+ " 	false as \"tillDischarge\", " 
					+ " 	ccm.is_tariff_rate_applicable as \"isTariffRateApplicable\", " 
					+ " 	1 as \"type\", " 
					+ " 	coalesce(unitservice.is_rate_editable, cast('N' as char)) as \"isRateEditable\", " 
					+ " 	coalesce(unitservice.is_allow_multiple_quantity, cast('N' as char)) as \"isQuantityEditable\", " 
					+ " 	coalesce(unitservice.is_doc_req, cast('N' as char)) as \"isDocReq\", " 
					+ " 	coalesce(unitservice.is_ref_doctor_share, cast('N' as char)) as \"isRefReq\", " 
					+ " 	appConfig.self_payee_id as \"selfPayeeId\", " 
					+ " 	preAuth.balance_amount as \"preAuthBalAmt\", " 
					+ " 	preAuth.preauth_id as \"preauthId\", " 
					+ " 	spons.payment_entitlement_id as \"paymentEntitlementId\", " 
					+ " 	coalesce(unitservice.is_discount, 'N') as \"isDiscount\", " 
					+ " 	coalesce(appConfig.is_scheme_discount, 'N') as \"isSchemeDiscount\", " 
					+ " 	coalesce(unitservice.is_auto_render, 'N') as \"isAutoRender\","
					+ "     appConfig.ambulance_charges_service_id as \"ambulanceChargesServiceId\" " 
					+ " from " 
					+ " 	opd.t_patient_sponsor_details spons " 
					+ " inner join t_company_master com on " 
					+ " 	com.company_id = spons.payee_id " 
					+ " inner join adt.t_admission_details adm on " 
					+ " 	adm.admission_id = spons.admission_id " 
					+ " 	and adm.status = 'A' " 
					+ " inner join service.t_contract_service_details csd on " 
					+ " 	spons.contract_id = csd.contract_id " 
					+ " inner join service.m_company_contract_master ccm on " 
					+ " 	csd.contract_id = ccm.contract_id " 
					+ " inner join service.m_service_master service on " 
					+ " 	service.service_master_id = csd.service_id " 
					+ " inner join service.t_unit_service_master_details unitservice on " 
					+ " 	service.service_master_id = unitservice.service_master_id " 
					+ " left join billing.m_preauth_master preAuth on " 
					+ " 	preAuth.patient_sponsor_id = spons.patient_sponsor_details_id " 
					+ " 	and preAuth.authorized_amount > 0 " 
					+ " inner join t_unit_application_billing_configuration appConfig on " 
					+ " 	appConfig.unit_id = unitservice.unit_id " 
					+ " where " 
					+ " 	spons.admission_id =:admissionId " 
					+ " 	and ccm.is_tariff_rate_applicable = 'N' " 
					+ " 	and spons.status = 'A' " 
					+ " 	and csd.service_id =:serviceId " 
					+ " 	and unitservice.unit_id = ccm.unit_id " 
					+ " 	and :currentDate >= to_char(ccm.validity_from, 'YYYY-MM-DD') "
					+ " 	and :currentDate <= to_char(ccm.validity_to, 'YYYY-MM-DD') "
					+ " 	and :currentDate >= to_char(spons.applicable_date, 'YYYY-MM-DD') "
					+ " union " 
					+ " select " 
					+ " 	csd.service_id as \"serviceId\", " 
					+ " 	spo.priority_id as \"priorityId\", " 
					+ " 	coalesce(cast(unitservice.rate_editable_max_value as double precision), 0) as \"maxRateEditable\", " 
					+ " 	coalesce(cast(unitservice.rate_editble_min_value as double precision), 0) as \"minRateEditable\", " 
					+ " 	csd.contract_id as \"contractId\", " 
					+ " 	0 as \"serviceRate\", " 
					+ " 	com.company_desc as \"companyName\", " 
					+ " 	( " 
					+ " 	select " 
					+ " 		cast(co_share_percentage as double precision) " 
					+ " 	from " 
					+ " 		service.t_contract_group_details " 
					+ " 	where " 
					+ " 		contract_id = csd.contract_id " 
					+ " 		and sub_department_id = service.sub_speciality_id) as \"coShare\", " 
					+ " 	spo.payee_id as \"payeeId\", " 
					+ " 	spo.insurance_id as \"associateCompanyId\", " 
					+ " 	unitservice.gst_type_id as \"taxId\", " 
					+ " 	coalesce(tax.tax_percentage, 0) as \"taxPercentage\", " 
					+ " 	0 as \"finalPrice\", " 
					+ " 	0 as \"selfPayable\", " 
					+ " 	0 as \"creaditPayable\", " 
					+ " 	0 as \"balanceCapAmount\", " 
					+ " 	( " 
					+ " 	select " 
					+ " 		billing_bed_category_id " 
					+ " 	from " 
					+ " 		service.t_contract_billing_bed_category_detail " 
					+ " 	where " 
					+ " 		contract_id =( " 
					+ " 		select " 
					+ " 			contract_id " 
					+ " 		from " 
					+ " 			opd.t_patient_sponsor_details spons " 
					+ " 		where " 
					+ " 			status = 'A' " 
					+ " 			and priority_id = 1 " 
					+ " 			and admission_id = adm.admission_id) " 
					+ " 		and billing_bed_category_id =:billingBed) as \"billingBedExist\", " 
					+ " 	csd.amount as \"amount\", " 
					+ " 	csd.balance_amount as \"balanceAmount\", " 
					+ " 	csd.quantity as \"quantity\", " 
					+ " 	csd.balance_quantity as \"balanceQuantity\", " 
					+ " 	csd.applicable_days as \"applicableDays\", " 
					+ " 	to_char(csd.applicable_date, 'yyyy-MM-dd') as \"applicableDate\", " 
					+ " 	service.speciality_id as \"specialityId\", " 
					+ " 	service.sub_speciality_id as \"subSpecialityId\", " 
					+ " 	ccm.tariff_id as \"tariffId\", " 
					+ " 	to_char(adm.doa, 'yyyy-MM-dd') as \"doa\", " 
					+ " 	csd.till_discharge as \"tillDischarge\", " 
					+ " 	ccm.is_tariff_rate_applicable as \"isTariffRateApplicable\", " 
					+ " 	2 as \"type\", " 
					+ " 	coalesce(unitservice.is_rate_editable, cast('N' as char)) as \"isRateEditable\", " 
					+ " 	coalesce(unitservice.is_allow_multiple_quantity, cast('N' as char)) as \"isQuantityEditable\", " 
					+ " 	coalesce(unitservice.is_doc_req, cast('N' as char)) as \"isDocReq\", " 
					+ " 	coalesce(unitservice.is_ref_doctor_share, cast('N' as char)) as \"isRefReq\", " 
					+ " 	appConfig.self_payee_id as \"selfPayeeId\" , " 
					+ " 	preAuth.balance_amount as \"preAuthBalAmt\", " 
					+ " 	preAuth.preauth_id as \"preauthId\", " 
					+ " 	spo.payment_entitlement_id as \"paymentEntitlementId\", " 
					+ " 	coalesce(unitservice.is_discount, 'N') as \"isDiscount\", " 
					+ " 	coalesce(appConfig.is_scheme_discount, 'N') as \"isSchemeDiscount\", " 
					+ " 	coalesce(unitservice.is_auto_render, 'N') as \"isAutoRender\", " 
					+ "     appConfig.ambulance_charges_service_id as \"ambulanceChargesServiceId\" " 
					+ " from " 
					+ " 	service.t_vist_contract_cap_mapper csd " 
					+ " inner join opd.t_patient_sponsor_details spo on " 
					+ " 	spo.contract_id = csd.contract_id " 
					+ " 	and spo.admission_id = csd.admission_id " 
					+ " 	and spo.status = 'A' " 
					+ " inner join adt.t_admission_details adm on " 
					+ " 	adm.admission_id = csd.admission_id " 
					+ " 	and adm.status = 'A' " 
					+ " inner join service.m_company_contract_master ccm on " 
					+ " 	csd.contract_id = ccm.contract_id " 
					+ " inner join t_company_master com on " 
					+ " 	com.company_id = ccm.company_id " 
					+ " inner join service.m_service_master service on " 
					+ " 	service.service_master_id = csd.service_id " 
					+ " inner join service.t_unit_service_master_details unitservice on " 
					+ " 	service.service_master_id = unitservice.service_master_id " 
					+ " left join m_tax tax on " 
					+ " 	tax.id = unitservice.gst_type_id " 
					+ " 	and tax.unit_id = ccm.unit_id " 
					+ " left join billing.m_preauth_master preAuth on " 
					+ " 	preAuth.patient_sponsor_id = spo.patient_sponsor_details_id " 
					+ " 	and preAuth.authorized_amount > 0 " 
					+ " inner join t_unit_application_billing_configuration appConfig on " 
					+ " 	appConfig.unit_id = unitservice.unit_id " 
					+ " where " 
					+ " 	csd.service_id =:serviceId " 
					+ " 	and csd.admission_id =:admissionId " 
					+ " 	and unitservice.unit_id = ccm.unit_id " 
					+ " 	and csd.status = 'A' " 
					+ " 	and :currentDate >= to_char(ccm.validity_from, 'YYYY-MM-DD') "
					+ " 	and :currentDate <= to_char(ccm.validity_to, 'YYYY-MM-DD') "
					+ " 	and :currentDate >= to_char(spo.applicable_date, 'YYYY-MM-DD') "
					+ " union " 
					+ " select " 
					+ " 	ustm.service_id as \"serviceId\", " 
					+ " 	spons.priority_id as \"priorityId\", " 
					+ " 	coalesce(cast(unitservice.rate_editable_max_value as double precision), 0) as \"maxRateEditable\", " 
					+ " 	coalesce(cast(unitservice.rate_editble_min_value as double precision), 0) as \"minRateEditable\", " 
					+ " 	ccm.contract_id as \"contractId\", " 
					+ " 	ustm.rate as \"serviceRate\", " 
					+ " 	com.company_desc as \"companyName\", " 
					+ " 	ccm.co_share_percentage as \"coShare\", " 
					+ " 	spons.payee_id as \"payeeId\", " 
					+ " 	spons.insurance_id as \"associateCompanyId\", " 
					+ " 	unitservice.gst_type_id as \"taxId\", " 
					+ " 	coalesce(tax.tax_percentage, 0) as \"taxPercentage\", " 
					+ " 	(ustm.rate +(ustm.rate*(coalesce(tax.tax_percentage, 0)* 0.01))) as \"finalPrice\", " 
					+ " 	((ustm.rate +(ustm.rate*(coalesce(tax.tax_percentage, 0)* 0.01)))*(coalesce(ccm.co_share_percentage, 0)* 0.01)) as \"selfPayable\", " 
					+ " 	((ustm.rate +(ustm.rate*(coalesce(tax.tax_percentage, 0)* 0.01)))-((ustm.rate +(ustm.rate*(coalesce(tax.tax_percentage, 0)* 0.01)))*(coalesce(ccm.co_share_percentage, 0)* 0.01))) as \"creaditPayable\", " 
					+ " 	null as \"balanceCapAmount\", " 
					+ " 	1 as \"billingBedExist\", " 
					+ " 	0 as \"amount\", " 
					+ " 	0 as \"balanceAmount\", " 
					+ " 	0 as \"quantity\", " 
					+ " 	0 as \"balanceQuantity\", " 
					+ " 	0 as \"applicableDays\", " 
					+ " 	'-' as \"applicableDate\", " 
					+ " 	service.speciality_id as \"specialityId\", " 
					+ " 	service.sub_speciality_id as \"subSpecialityId\", " 
					+ " 	ccm.tariff_id as \"tariffId\", " 
					+ " 	to_char(adm.doa, 'yyyy-MM-dd') as \"doa\", " 
					+ " 	false as \"tillDischarge\", " 
					+ " 	ccm.is_tariff_rate_applicable as \"isTariffRateApplicable\", " 
					+ " 	1 as \"type\", " 
					+ " 	coalesce(unitservice.is_rate_editable, cast('N' as char)) as \"isRateEditable\", " 
					+ " 	coalesce(unitservice.is_allow_multiple_quantity, cast('N' as char)) as \"isQuantityEditable\", " 
					+ " 	coalesce(unitservice.is_doc_req, cast('N' as char)) as \"isDocReq\", " 
					+ " 	coalesce(unitservice.is_ref_doctor_share, cast('N' as char)) as \"isRefReq\", " 
					+ " 	appConfig.self_payee_id as \"selfPayeeId\" , " 
					+ " 	preAuth.balance_amount as \"preAuthBalAmt\", " 
					+ " 	preAuth.preauth_id as \"preauthId\", " 
					+ " 	spons.payment_entitlement_id as \"paymentEntitlementId\", " 
					+ " 	coalesce(unitservice.is_discount, 'N') as \"isDiscount\", " 
					+ " 	coalesce(appConfig.is_scheme_discount, 'N') as \"isSchemeDiscount\", " 
					+ " 	coalesce(unitservice.is_auto_render, 'N') as \"isAutoRender\" ,"
					+ "     appConfig.ambulance_charges_service_id as \"ambulanceChargesServiceId\" " 
					+ " from " 
					+ " 	opd.t_patient_sponsor_details spons " 
					+ " inner join t_company_master com on " 
					+ " 	com.company_id = spons.payee_id " 
					+ " inner join service.m_company_contract_master ccm on " 
					+ " 	spons.contract_id = ccm.contract_id " 
					+ " inner join service.m_unit_service_tariff_master ustm on " 
					+ " 	ustm.tariff_id = ccm.tariff_id " 
					+ " inner join adt.t_admission_details adm on " 
					+ " 	adm.admission_id = spons.admission_id " 
					+ " 	and adm.status = 'A' " 
					+ " inner join service.m_service_master service on " 
					+ " 	ustm.service_id = service.service_master_id " 
					+ " inner join service.t_unit_service_master_details unitservice on " 
					+ " 	ustm.service_id = unitservice.service_master_id " 
					+ " left join m_tax tax on " 
					+ " 	tax.id = unitservice.gst_type_id " 
					+ " inner join doctor.m_speciality_master speciality on " 
					+ " 	service.speciality_id = speciality.speciality_id " 
					+ " inner join doctor.m_sub_speciality_master subSpeciality on " 
					+ " 	service.sub_speciality_id = subSpeciality.sub_speciality_id " 
					+ " left join billing.m_preauth_master preAuth on " 
					+ " 	preAuth.patient_sponsor_id = spons.patient_sponsor_details_id " 
					+ " 	and preAuth.authorized_amount > 0 " 
					+ " inner join t_unit_application_billing_configuration appConfig on " 
					+ " 	appConfig.unit_id = unitservice.unit_id " 
					+ " where " 
					+ " 	spons.admission_id =:admissionId " 
					+ " 	and spons.status = 'A' " 
					+ " 	and ccm.is_tariff_rate_applicable = 'Y' " 
					+ " 	and ustm.organization_id =:organizationId " 
					+ " 	and ustm.unit_id =:unitId " 
					+ " 	and unitservice.unit_id = ustm.unit_id " 
					+ " 	and ustm.tariff_id = ccm.tariff_id " 
					+ " 	and ustm.patient_type_id =:patientTypeId " 
					+ " 	and ustm.billing_bed_category_id =:billingBed " 
					+ " 	and ustm.visit_type_id =:visitTypeId " 
					+ " 	and ustm.payment_entitlement_id =:paymentEntitlementId " 
					+ " 	and ustm.service_id = :serviceId " 
					+ " 	and :currentDate >= to_char(ccm.validity_from, 'YYYY-MM-DD') "
					+ " 	and :currentDate <= to_char(ccm.validity_to, 'YYYY-MM-DD') "
					+ " 	and :currentDate >= to_char(spons.applicable_date, 'YYYY-MM-DD') "
					+ " 	and ustm.effective_date =( " 
					+ " 	select " 
					+ " 		max(ustm.effective_date) " 
					+ " 	from " 
					+ " 		service.m_unit_service_tariff_master ustm " 
					+ " 	inner join service.m_service_master serviceMaster on " 
					+ " 		ustm.service_id = serviceMaster.service_master_id " 
					+ " 	inner join service.t_unit_service_master_details details on " 
					+ " 		ustm.service_id = details.service_master_id " 
					+ " 	left join m_tax tax on " 
					+ " 		tax.id = details.gst_type_id " 
					+ " 	inner join doctor.m_speciality_master speciality on " 
					+ " 		serviceMaster.speciality_id = speciality.speciality_id " 
					+ " 	inner join doctor.m_sub_speciality_master subSpeciality on " 
					+ " 		serviceMaster.sub_speciality_id = subSpeciality.sub_speciality_id " 
					+ " 	where " 
					+ " 		ustm.organization_id =:organizationId " 
					+ " 		and details.unit_id = ustm.unit_id " 
					+ " 		and ustm.unit_id =:unitId " 
					+ " 		and spons.status = 'A' " 
					+ " 		and ustm.tariff_id = ccm.tariff_id " 
					+ " 		and ustm.patient_type_id = :patientTypeId " 
					+ " 		and ustm.billing_bed_category_id =:billingBed " 
					+ " 		and ustm.visit_type_id =:visitTypeId " 
					+ " 		and ustm.payment_entitlement_id =:paymentEntitlementId " 
					+ " 		and to_char(ustm.effective_date, 'YYYY-MM-DD') <= :currentDate "
					+ " 		and ustm.service_id = :serviceId) " 
					+ " order by " 
					+ " 	\"priorityId\" ");
				
				consumptionDto = sessionFactory.getCurrentSession()
						.createSQLQuery(query.toString())
						.setParameter("admissionId",contractConsumptionDto.getAdmissionId())
						.setParameter("billingBed",contractConsumptionDto.getBillingBedCategoryId())
						.setParameter("serviceId",contractConsumptionDto.getServiceId())
						.setParameter("patientTypeId", contractConsumptionDto.getPatientCategoryId()!=null ? contractConsumptionDto.getPatientCategoryId() : contractConsumptionDto.getPatientTypeId())
						.setParameter("organizationId", contractConsumptionDto.getOrganizationId())
						.setParameter("unitId", contractConsumptionDto.getUnitId())
						.setParameter("paymentEntitlementId", 1)
						.setParameter("visitTypeId", contractConsumptionDto.getVisitTypeId())
						.setParameter("currentDate", contractConsumptionDto.getCurrentDate().substring(0,10))
						//.setDate("effectiveDate", GlobalCommonDateUtils.getDate(contractConsumptionDto.getCurrentDate(),"yyyy-MM-dd"))
						.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
						.list();
			
			}
			
			
			if(contractConsumptionDto.getEncounterId() != null ){
			
				String query=" select " 
						 + " 	csd.service_id as \"serviceId\", " 
						 + " 	spons.priority_id as \"priorityId\", " 
						 + " 	coalesce(cast(unitservice.rate_editable_max_value as double precision), 0) as \"maxRateEditable\", " 
						 + " 	coalesce(cast(unitservice.rate_editble_min_value as double precision), 0) as \"minRateEditable\", " 
						 + " 	csd.contract_id as \"contractId\", " 
						 + " 	csd.apportioned_price as \"serviceRate\", " 
						 + " 	com.company_desc as \"companyName\", " 
						+ " 	CAST( CASE   WHEN (csd.final_price = 0 or csd.self_payable = 0) THEN 0  ELSE (csd.self_payable / csd.final_price) * 100   END AS NUMERIC(15, 10)) AS \"coShare\" , "
						+ " 	spons.payee_id as \"payeeId\", "
						 + " 	spons.insurance_id as \"associateCompanyId\", "
						 + " 	csd.tax_id as \"taxId\", " 
						 + " 	coalesce(csd.tax_percentage, 0) as \"taxPercentage\", " 
						 + " 	csd.final_price as \"finalPrice\", " 
						 + " 	csd.self_payable as \"selfPayable\", " 
						 + " 	csd.credit_payable as \"creaditPayable\", " 
						 + " 	( " 
						 + " 	select " 
						 + " 		ccd.balance_cap_amount " 
						 + " 	from " 
						 + " 		service.t_vist_contract_cap_mapper ccd " 
						 + " 	where " 
						 + " 		ccd.department_id = service.speciality_id " 
						 + " 		and ccd.contract_id = csd.contract_id " 
						 + " 		and ccd.encounter_id =:encounterId " 
						 + " 		and ccd.status = 'A') as \"balanceCapAmount\", " 
						 + " 	0 as \"amount\", " 
						 + " 	0 as \"balanceAmount\", " 
						 + " 	0 as \"quantity\", " 
						 + " 	0 as \"balanceQuantity\", " 
						 + " 	0 as \"applicableDays\", " 
						 + " 	'' as \"applicableDate\", " 
						 + " 	service.speciality_id as \"specialityId\", " 
						 + " 	service.sub_speciality_id as \"subSpecialityId\", " 
						 + " 	ccm.tariff_id as \"tariffId\", " 
						 + " 	1 as \"dayDiff\", " 
						 + " 	1 as \"type\", " 
						 + " 	coalesce(unitservice.is_rate_editable, cast('N' as char)) as \"isRateEditable\", " 
						 + " 	coalesce(unitservice.is_allow_multiple_quantity, cast('N' as char)) as \"isQuantityEditable\", " 
						 + " 	coalesce(unitservice.is_doc_req, cast('N' as char)) as \"isDocReq\", " 
						 + " 	coalesce(unitservice.is_ref_doctor_share, cast('N' as char)) as \"isRefReq\", " 
						 + " 	1 as \"billingBedExist\", " 
						 + " 	false as \"tillDischarge\", " 
						 + " 	ccm.is_tariff_rate_applicable as \"isTariffRateApplicable\", " 
						 + " 	appConfig.self_payee_id as \"selfPayeeId\" , " 
						 + " 	preAuth.balance_amount as \"preAuthBalAmt\", " 
						 + " 	preAuth.preauth_id as \"preauthId\", " 
						 + " 	spons.payment_entitlement_id as \"paymentEntitlementId\", " 
						 + " 	coalesce(unitservice.is_discount, 'N') as \"isDiscount\", " 
						 + " 	coalesce(appConfig.is_scheme_discount, 'N') as \"isSchemeDiscount\", " 
						 + " 	coalesce(unitservice.is_auto_render, 'N') as \"isAutoRender\", " 
						 + "    appConfig.ambulance_charges_service_id as \"ambulanceChargesServiceId\" " 
						 + " from " 
						 + " 	opd.t_patient_sponsor_details spons " 
						 + " inner join t_company_master com on " 
						 + " 	com.company_id = spons.payee_id " 
						 + " inner join service.t_contract_service_details csd on " 
						 + " 	spons.contract_id = csd.contract_id " 
						 + " inner join service.m_company_contract_master ccm on " 
						 + " 	csd.contract_id = ccm.contract_id " 
						 + " inner join service.m_service_master service on " 
						 + " 	service.service_master_id = csd.service_id " 
						 + " inner join service.t_unit_service_master_details unitservice on " 
						 + " 	service.service_master_id = unitservice.service_master_id " 
						 + " left join billing.m_preauth_master preAuth on " 
						 + " 	preAuth.patient_sponsor_id = spons.patient_sponsor_details_id " 
						 + " 	and preAuth.authorized_amount > 0 " 
						 + " inner join t_unit_application_billing_configuration appConfig on " 
						 + " 	appConfig.unit_id = unitservice.unit_id " 
						 + " where " 
						 + " 	spons.encounter_id =:encounterId " 
						 + " 	and ccm.is_tariff_rate_applicable = 'N' " 
						 + " 	and spons.status = 'A' " 
						 + " 	and csd.service_id =:serviceId " 
						 + " 	and unitservice.unit_id = ccm.unit_id " 
						 + " 	and :currentDate >= to_char(ccm.validity_from, 'YYYY-MM-DD') "
						 + " 	and :currentDate <= to_char(ccm.validity_to, 'YYYY-MM-DD') "
						 + " 	and :currentDate >= to_char(spons.applicable_date, 'YYYY-MM-DD') "
						 + " union " 
						 + " select " 
						 + " 	csd.service_id as \"serviceId\", " 
						 + " 	spo.priority_id as \"priorityId\", " 
						 + " 	coalesce(cast(unitservice.rate_editable_max_value as double precision), 0) as \"maxRateEditable\", " 
						 + " 	coalesce(cast(unitservice.rate_editble_min_value as double precision), 0) as \"minRateEditable\", " 
						 + " 	csd.contract_id as \"contractId\", " 
						 + " 	0 as \"serviceRate\", " 
						 + " 	com.company_desc as \"companyName\", " 
						 + " 	( " 
						 + " 	select " 
						 + " 		cast(co_share_percentage as double precision) " 
						 + " 	from " 
						 + " 		service.t_contract_group_details " 
						 + " 	where " 
						 + " 		contract_id = csd.contract_id " 
						 + " 		and sub_department_id = service.sub_speciality_id) as \"coShare\", " 
						 + " 	spo.payee_id as \"payeeId\", " 
						 + " 	spo.insurance_id as \"associateCompanyId\", " 
						 + " 	unitservice.gst_type_id as \"taxId\", " 
						 + " 	coalesce(tax.tax_percentage, 0) as \"taxPercentage\", " 
						 + " 	0 as \"finalPrice\", " 
						 + " 	0 as \"selfPayable\", " 
						 + " 	0 as \"creaditPayable\", " 
						 + " 	0 as \"balanceCapAmount\", " 
						 + " 	csd.amount as \"amount\", " 
						 + " 	csd.balance_amount as \"balanceAmount\", " 
						 + " 	csd.quantity as \"quantity\", " 
						 + " 	csd.balance_quantity as \"balanceQuantity\", " 
						 + " 	csd.applicable_days as \"applicableDays\", " 
						 + " 	'' as \"applicableDate\", " 
						 + " 	service.speciality_id as \"specialityId\", " 
						 + " 	service.sub_speciality_id as \"subSpecialityId\", " 
						 + " 	ccm.tariff_id as \"tariffId\", " 
						 + " 	1 as \"dayDiff\", " 
						 + " 	2 as \"type\", " 
						 + " 	coalesce(unitservice.is_rate_editable, cast('N' as char)) as \"isRateEditable\", " 
						 + " 	coalesce(unitservice.is_allow_multiple_quantity, cast('N' as char)) as \"isQuantityEditable\", " 
						 + " 	coalesce(unitservice.is_doc_req, cast('N' as char)) as \"isDocReq\", " 
						 + " 	coalesce(unitservice.is_ref_doctor_share, cast('N' as char)) as \"isRefReq\", " 
						 + " 	1 as \"billingBedExist\", " 
						 + " 	csd.till_discharge as \"tillDischarge\", " 
						 + " 	ccm.is_tariff_rate_applicable as \"isTariffRateApplicable\", " 
						 + " 	appConfig.self_payee_id as \"selfPayeeId\" , " 
						 + " 	preAuth.balance_amount as \"preAuthBalAmt\", " 
						 + " 	preAuth.preauth_id as \"preauthId\", " 
						 + " 	spo.payment_entitlement_id as \"paymentEntitlementId\", " 
						 + " 	coalesce(unitservice.is_discount, 'N') as \"isDiscount\", " 
						 + " 	coalesce(appConfig.is_scheme_discount, 'N') as \"isSchemeDiscount\", " 
						 + " 	coalesce(unitservice.is_auto_render, 'N') as \"isAutoRender\" ,"
						 + "    appConfig.ambulance_charges_service_id as \"ambulanceChargesServiceId\" " 
						 + " from " 
						 + " 	service.t_vist_contract_cap_mapper csd " 
						 + " inner join opd.t_patient_sponsor_details spo on  " 
						 + " 	spo.contract_id =csd.contract_id  " 
						 + " 	and spo.encounter_id = csd.encounter_id  " 
						 + " 	and spo.status = 'A' " 
						 + " inner join service.m_company_contract_master ccm on " 
						 + " 	csd.contract_id = ccm.contract_id " 
						 + " inner join t_company_master com on " 
						 + " 	com.company_id = spo.payee_id  " 
						 + " inner join service.m_service_master service on " 
						 + " 	service.service_master_id = csd.service_id " 
						 + " inner join service.t_unit_service_master_details unitservice on " 
						 + " 	service.service_master_id = unitservice.service_master_id " 
						 + " left join m_tax tax on " 
						 + " 	tax.id = unitservice.gst_type_id " 
						 + " 	and tax.unit_id = ccm.unit_id " 
						 + " left join billing.m_preauth_master preAuth on " 
						 + " 	preAuth.patient_sponsor_id = spo.patient_sponsor_details_id  " 
						 + " 	and preAuth.authorized_amount > 0 " 
						 + " left join t_unit_application_billing_configuration appConfig on " 
						 + " 	appConfig.unit_id = unitservice.unit_id " 
						 + " where " 
						 + " 	csd.service_id =:serviceId " 
						 + " 	and csd.encounter_id =:encounterId " 
						 + " 	and unitservice.unit_id = ccm.unit_id " 
						 + " 	and csd.status = 'A' " 
						 + " 	and :currentDate >= to_char(ccm.validity_from, 'YYYY-MM-DD') "
						 + " 	and :currentDate <= to_char(ccm.validity_to, 'YYYY-MM-DD') "
						 + " 	and :currentDate >= to_char(spo.applicable_date, 'YYYY-MM-DD') "
						 + " union " 
						 + " select " 
						 + " 	ustm.service_id as \"serviceId\", " 
						 + " 	spons.priority_id as \"priorityId\", " 
						 + " 	coalesce(cast(unitservice.rate_editable_max_value as double precision), 0) as \"maxRateEditable\", " 
						 + " 	coalesce(cast(unitservice.rate_editble_min_value as double precision), 0) as \"minRateEditable\", " 
						 + " 	ccm.contract_id as \"contractId\", " 
						 + " 	ustm.rate as \"serviceRate\", " 
						 + " 	com.company_desc as \"companyName\", " 
						 + " 	ccm.co_share_percentage as \"coShare\", " 
						 + " 	spons.payee_id as \"payeeId\", " 
						 + " 	spons.insurance_id as \"associateCompanyId\", " 
						 + " 	unitservice.gst_type_id as \"taxId\", " 
						 + " 	coalesce(tax.tax_percentage, 0) as \"taxPercentage\", " 
						 + " 	(ustm.rate +(ustm.rate*(coalesce(tax.tax_percentage, 0)* 0.01))) as \"finalPrice\", " 
						 + " 	((ustm.rate +(ustm.rate*(coalesce(tax.tax_percentage, 0)* 0.01)))*(coalesce(ccm.co_share_percentage, 0)* 0.01)) as \"selfPayable\", " 
						 + " 	((ustm.rate +(ustm.rate*(coalesce(tax.tax_percentage, 0)* 0.01)))-((ustm.rate +(ustm.rate*(coalesce(tax.tax_percentage, 0)* 0.01)))*(coalesce(ccm.co_share_percentage, 0)* 0.01))) as \"creaditPayable\", " 
						 + " 	null as \"balanceCapAmount\", " 
						 + " 	0 as \"amount\", " 
						 + " 	0 as \"balanceAmount\", " 
						 + " 	0 as \"quantity\", " 
						 + " 	0 as \"balanceQuantity\", " 
						 + " 	0 as \"applicableDays\", " 
						 + " 	'' as \"applicableDate\", " 
						 + " 	service.speciality_id as \"specialityId\", " 
						 + " 	service.sub_speciality_id as \"subSpecialityId\", " 
						 + " 	ccm.tariff_id as \"tariffId\", " 
						 + " 	1 as \"dayDiff\", " 
						 + " 	1 as \"type\", " 
						 + " 	coalesce(unitservice.is_rate_editable, cast('N' as char)) as \"isRateEditable\", " 
						 + " 	coalesce(unitservice.is_allow_multiple_quantity, cast('N' as char)) as \"isQuantityEditable\", " 
						 + " 	coalesce(unitservice.is_doc_req, cast('N' as char)) as \"isDocReq\", " 
						 + " 	coalesce(unitservice.is_ref_doctor_share, cast('N' as char)) as \"isRefReq\", " 
						 + " 	1 as \"billingBedExist\", " 
						 + " 	false as \"tillDischarge\", " 
						 + " 	ccm.is_tariff_rate_applicable as \"isTariffRateApplicable\", " 
						 + " 	appConfig.self_payee_id as \"selfPayeeId\" , " 
						 + " 	preAuth.balance_amount as \"preAuthBalAmt\", " 
						 + " 	preAuth.preauth_id as \"preauthId\", " 
						 + " 	spons.payment_entitlement_id as \"paymentEntitlementId\", " 
						 + " 	coalesce(unitservice.is_discount, 'N') as \"isDiscount\", " 
						 + " 	coalesce(appConfig.is_scheme_discount, 'N') as \"isSchemeDiscount\", " 
						 + " 	coalesce(unitservice.is_auto_render, 'N') as \"isAutoRender\", "
						 + "    appConfig.ambulance_charges_service_id as \"ambulanceChargesServiceId\" " 
						 + " from " 
						 + " 	opd.t_patient_sponsor_details spons " 
						 + " inner join t_company_master com on " 
						 + " 	com.company_id = spons.payee_id " 
						 + " inner join service.m_company_contract_master ccm on " 
						 + " 	spons.contract_id = ccm.contract_id " 
						 + " inner join service.m_unit_service_tariff_master ustm on " 
						 + " 	ustm.tariff_id = ccm.tariff_id " 
						 + " inner join service.m_service_master service on " 
						 + " 	ustm.service_id = service.service_master_id " 
						 + " inner join service.t_unit_service_master_details unitservice on " 
						 + " 	ustm.service_id = unitservice.service_master_id " 
						 + " left join m_tax tax on " 
						 + " 	tax.id = unitservice.gst_type_id " 
						 + " inner join doctor.m_speciality_master speciality on " 
						 + " 	service.speciality_id = speciality.speciality_id " 
						 + " inner join doctor.m_sub_speciality_master subSpeciality on " 
						 + " 	service.sub_speciality_id = subSpeciality.sub_speciality_id " 
						 + " left join billing.m_preauth_master preAuth on " 
						 + " 	preAuth.patient_sponsor_id = spons.patient_sponsor_details_id " 
						 + " 	and preAuth.authorized_amount > 0 " 
						 + " inner join t_unit_application_billing_configuration appConfig on " 
						 + " 	appConfig.unit_id = unitservice.unit_id " 
						 + " where " 
						 + " 	spons.encounter_id =:encounterId " 
						 + " 	and spons.status = 'A' " 
						 + " 	and ccm.is_tariff_rate_applicable = 'Y' " 
						 + " 	and ustm.organization_id =:organizationId " 
						 + " 	and ustm.unit_id =:unitId " 
						 + " 	and unitservice.unit_id = ustm.unit_id " 
						 + " 	and ustm.tariff_id = ccm.tariff_id " 
						 + " 	and ustm.patient_type_id =:patientTypeId " 
						 + " 	and ustm.billing_bed_category_id =:billingBedCategoryId " 
						 + " 	and ustm.visit_type_id = 1 " 
						 + " 	and ustm.payment_entitlement_id = 1 " 
						 + " 	and ustm.service_id = :serviceId " 
						 + " 	and :currentDate >= to_char(ccm.validity_from, 'YYYY-MM-DD') "
						 + " 	and :currentDate <= to_char(ccm.validity_to, 'YYYY-MM-DD') "
						 + " 	and :currentDate >= to_char(spons.applicable_date, 'YYYY-MM-DD') "
						 + " 	and ustm.effective_date =( " 
						 + " 	select " 
						 + " 		max(ustm.effective_date) " 
						 + " 	from " 
						 + " 		service.m_unit_service_tariff_master ustm " 
						 + " 	inner join service.m_service_master serviceMaster on " 
						 + " 		ustm.service_id = serviceMaster.service_master_id " 
						 + " 	inner join service.t_unit_service_master_details details on " 
						 + " 		ustm.service_id = details.service_master_id " 
						 + " 	left join m_tax tax on " 
						 + " 		tax.id = details.gst_type_id " 
						 + " 	inner join doctor.m_speciality_master speciality on " 
						 + " 		serviceMaster.speciality_id = speciality.speciality_id " 
						 + " 	inner join doctor.m_sub_speciality_master subSpeciality on " 
						 + " 		serviceMaster.sub_speciality_id = subSpeciality.sub_speciality_id " 
						 + " 	where " 
						 + " 		ustm.organization_id =:organizationId " 
						 + " 		and details.unit_id = ustm.unit_id " 
						 + " 		and ustm.unit_id =:unitId " 
						 + " 		and ustm.tariff_id = ccm.tariff_id " 
						 + " 		and ustm.patient_type_id =:patientTypeId " 
						 + " 		and ustm.billing_bed_category_id =:billingBedCategoryId " 
						 + " 		and ustm.visit_type_id = 1 " 
						 + " 		and ustm.payment_entitlement_id = 1 " 
						 + " 		and to_char(ustm.effective_date, 'YYYY-MM-DD') <= :currentDate "
						 + " 		and ustm.service_id = :serviceId) " 
						 + " order by " 
						 + " 	\"priorityId\" ";
				
				consumptionDto = sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setParameter("encounterId",contractConsumptionDto.getEncounterId())
						.setParameter("serviceId",contractConsumptionDto.getServiceId())
						.setParameter("patientTypeId", contractConsumptionDto.getPatientCategoryId()!=null ? contractConsumptionDto.getPatientCategoryId() : contractConsumptionDto.getPatientTypeId())
						.setParameter("billingBedCategoryId", contractConsumptionDto.getBillingBedCategoryId())
						.setParameter("organizationId", contractConsumptionDto.getOrganizationId())
						.setParameter("unitId", contractConsumptionDto.getUnitId())
						.setParameter("currentDate", contractConsumptionDto.getCurrentDate().substring(0,10))
						.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
						.list();
			}
			
			return consumptionDto;
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
		}

	
	@Override
	public List<ContractConsumptionDto> getPackageDetailsByContractId(ContractConsumptionDto contractConsumptionDto)throws ServiceException {
		try {
			
			List<ContractConsumptionDto> consumptionDto = new ArrayList<ContractConsumptionDto>();
			
			if(contractConsumptionDto.getAdmissionId() !=null){
				
			StringBuilder query= new StringBuilder("select " 
					 + " 	csd.service_id as \"serviceId\", " 
					 + " 	spons.priority_id as \"priorityId\", " 
					 + " 	coalesce(cast(unitservice.rate_editable_max_value as double precision), 0) as \"maxRateEditable\", " 
					 + " 	coalesce(cast(unitservice.rate_editble_min_value as double precision), 0) as \"minRateEditable\", " 
					 + " 	csd.contract_id as \"contractId\", " 
					 + " 	csd.apportioned_price as \"serviceRate\", " 
					 + " 	com.company_desc as \"companyName\", " 
					 + " 	cast(((csd.self_payable/csd.final_price)*100) as numeric(15,10)) as \"coShare\", " 
					 + " 	ccm.company_id as \"payeeId\", "
					 + "	ccm.associate_company_id as \"associateCompanyId\", " 
					 + " 	csd.tax_id as \"taxId\", " 
					 + " 	coalesce(csd.tax_percentage,0) as \"taxPercentage\", " 
					 + " 	csd.final_price as \"finalPrice\", " 
					 + " 	csd.self_payable as \"selfPayable\", " 
					 + " 	csd.credit_payable as \"creaditPayable\", " 
					 + " 	( " 
					 + " 	select " 
					 + " 		ccd.balance_cap_amount " 
					 + " 	from " 
					 + " 		service.t_vist_contract_cap_mapper ccd " 
					 + " 	where " 
					 + " 		ccd.department_id = service.speciality_id " 
					 + " 		and ccd.contract_id = csd.contract_id " 
					 + " 		and ccd.admission_id =:admissionId " 
					 + " 		and ccd.status = 'A' " 
					 + " 		and ccd.service_id is null) as \"balanceCapAmount\", " 
					 + " 	( " 
					 + " 	select " 
					 + " 		billing_bed_category_id " 
					 + " 	from " 
					 + " 		service.t_contract_billing_bed_category_detail " 
					 + " 	where " 
					 + " 		contract_id =( " 
					 + " 		select " 
					 + " 			contract_id " 
					 + " 		from " 
					 + " 			opd.t_patient_sponsor_details spons " 
					 + " 		where " 
					 + " 			status = 'A' " 
					 + " 			and priority_id = 1 " 
					 + " 			and admission_id = adm.admission_id) " 
					 + " 		and billing_bed_category_id =:billingBed) as \"billingBedExist\", " 
					 + " 	service.speciality_id as \"specialityId\", " 
					 + " 	ccm.tariff_id as \"tariffId\", " 
					 + " 	cast(date_part('day', now() - adm.doa) as int) as \"dayDiff\", " 
					 + " 	false as \"tillDischarge\", " 
					 + " 	1 as \"type\", " 
					 + "    ccm.is_tariff_rate_applicable as \"isTariffRateApplicable\", "
					 + " 	coalesce(unitservice.is_rate_editable, cast('N' as char)) as \"isRateEditable\", " 
					 + " 	coalesce(unitservice.is_allow_multiple_quantity, cast('N' as char)) as \"isQuantityEditable\", " 
					 + " 	appConfig.self_payee_id as \"selfPayeeId\" , " 
					 + "	preAuth.balance_amount as \"preAuthBalAmt\", "
					 + "    preAuth.preauth_id as \"preauthId\", "
					 + " 	coalesce(unitservice.is_discount,'N') as \"isDiscount\", " 
					 + " 	coalesce(appConfig.is_scheme_discount,'N') as \"isSchemeDiscount\" "
					 + " from " 
					 + " 	opd.t_patient_sponsor_details spons " 
					 + " inner join t_company_master com on " 
					 + " 	com.company_id = spons.payee_id " 
					 + " inner join adt.t_admission_details adm on " 
					 + " 	adm.admission_id = spons.admission_id " 
					 + " 	and adm.status = 'A' " 
					 + " inner join service.t_contract_service_details csd on " 
					 + " 	spons.contract_id = csd.contract_id " 
					 + " inner join service.m_company_contract_master ccm on " 
					 + " 	csd.contract_id = ccm.contract_id " 
					 + " inner join service.m_service_master service on " 
					 + " 	service.service_master_id = csd.service_id " 
					 + " inner join service.t_unit_service_master_details unitservice on " 
					 + " 	service.service_master_id = unitservice.service_master_id " 
					 + " left join billing.m_preauth_master preAuth on "
					 + "  		preAuth.patient_sponsor_id = spons.patient_sponsor_details_id and preAuth.authorized_amount > 0	"
					 + " inner join t_unit_application_billing_configuration appConfig on " 
					 + " 	appConfig.unit_id = unitservice.unit_id " 
					 + " where " 
					 + " 	spons.admission_id =:admissionId " 
					 + " 	and ccm.is_tariff_rate_applicable = 'N' " 
					 + " 	and csd.service_id =:serviceId "
					 + "	and csd.package_master_id=:packageMasterId " 
					 + " 	and unitservice.unit_id =:unitId " 
					 + " 	and spons.status = 'A' " 
					 + "    and :currentDate >= to_char(ccm.validity_from,'YYYY-MM-DD') "
					 + "    and :currentDate <= to_char(ccm.validity_to,'YYYY-MM-DD') "
					 + " 	and :currentDate >= to_char(spons.applicable_date, 'YYYY-MM-DD') "
					 + " union " 
					 + " select " 
					 + " 	pack.service_id as \"serviceId\", " 
					 + " 	spons.priority_id as \"priorityId\", " 
					 + " 	coalesce(cast(unitservice.rate_editable_max_value as double precision), 0) as \"maxRateEditable\", " 
					 + " 	coalesce(cast(unitservice.rate_editble_min_value as double precision), 0) as \"minRateEditable\", " 
					 + " 	ccm.contract_id as \"contractId\", " 
					 + " 	pack.package_price as \"serviceRate\", " 
					 + " 	com.company_desc as \"companyName\", " 
					 + " 	ccm.co_share_percentage as \"coShare\", " 
					 + " 	ccm.company_id as \"payeeId\", " 
					 + "	ccm.associate_company_id as \"associateCompanyId\", "
					 + " 	unitservice.gst_type_id as \"taxId\", " 
					 + " 	coalesce(tax.tax_percentage,0) as \"taxPercentage\", " 
					 + " 	(pack.package_price +(pack.package_price*(coalesce(tax.tax_percentage, 0)* 0.01))) as \"finalPrice\", " 
					 + " 	((pack.package_price +(pack.package_price*(coalesce(tax.tax_percentage, 0)* 0.01)))*(coalesce(ccm.co_share_percentage, 0)* 0.01)) as \"selfPayable\", " 
					 + " 	((pack.package_price +(pack.package_price*(coalesce(tax.tax_percentage, 0)* 0.01)))-((pack.package_price +(pack.package_price*(coalesce(tax.tax_percentage, 0)* 0.01)))*(coalesce(ccm.co_share_percentage, 0)* 0.01))) as \"creaditPayable\", " 
					 + " 	null as \"balanceCapAmount\", " 
					 + " 	( "
					 + " 	select " 
					 + " 		billing_bed_category_id " 
					 + " 	from " 
					 + " 		service.t_contract_billing_bed_category_detail " 
					 + " 	where " 
					 + " 		contract_id =( " 
					 + " 		select " 
					 + " 			contract_id " 
					 + " 		from " 
					 + " 			opd.t_patient_sponsor_details spons " 
					 + " 		where " 
					 + " 			status = 'A' " 
					 + " 			and priority_id = 1 " 
					 + " 			and admission_id = adm.admission_id) " 
					 + " 		and billing_bed_category_id =:billingBed) as \"billingBedExist\", " 
					 + " 	service.speciality_id as \"specialityId\", " 
					 + " 	ccm.tariff_id as \"tariffId\", " 
					 + " 	cast(date_part('day', now() - adm.doa) as int) as \"dayDiff\", " 
					 + " 	false as \"tillDischarge\", " 
					 + " 	1 as \"type\", " 
					 + "    ccm.is_tariff_rate_applicable as \"isTariffRateApplicable\", "
					 + " 	coalesce(unitservice.is_rate_editable, cast('N' as char)) as \"isRateEditable\", " 
					 + " 	coalesce(unitservice.is_allow_multiple_quantity, cast('N' as char)) as \"isQuantityEditable\", " 
					 + " 	appConfig.self_payee_id as \"selfPayeeId\" , " 
					 + "	preAuth.balance_amount as \"preAuthBalAmt\", "
					 + "  	preAuth.preauth_id as \"preauthId\", "
					 + " 	coalesce(unitservice.is_discount,'N') as \"isDiscount\", " 
					 + " 	coalesce(appConfig.is_scheme_discount,'N') as \"isSchemeDiscount\" "
					 + " from " 
					 + " 	opd.t_patient_sponsor_details spons " 
					 + " inner join t_company_master com on " 
					 + " 	com.company_id = spons.payee_id " 
					 + " inner join service.m_company_contract_master ccm on " 
					 + " 	spons.contract_id = ccm.contract_id " 
					 + " inner join service.m_package_master pack on " 
					 + " 	pack.package_master_id = :packageMasterId " 
					 + " inner join service.m_service_master service on " 
					 + " 	service.service_master_id = pack.service_id " 
					 + " inner join service.t_unit_service_master_details unitservice on " 
					 + " 	service.service_master_id = unitservice.service_master_id " 
					 + " left join adt.t_admission_details adm on " 
					 + " 	adm.admission_id = spons.admission_id " 
					 + " 	and adm.status = 'A' " 
					 + " left join m_tax tax on " 
					 + " 	tax.id = unitservice.gst_type_id " 
					 + " inner join doctor.m_speciality_master speciality on " 
					 + " 	service.speciality_id = speciality.speciality_id " 
					 + " inner join doctor.m_sub_speciality_master subSpeciality on " 
					 + " 	service.sub_speciality_id = subSpeciality.sub_speciality_id " 
					 + " left join billing.m_preauth_master preAuth on "
					 + "  		preAuth.patient_sponsor_id = spons.patient_sponsor_details_id and preAuth.authorized_amount > 0 "
					 + " inner join t_unit_application_billing_configuration appConfig on " 
					 + " 	appConfig.unit_id = unitservice.unit_id " 
					 + " where " 
					 + " 	spons.admission_id =:admissionId " 
					 + " 	and ccm.is_tariff_rate_applicable = 'Y' " 
					 + " 	and spons.organization_id =:organizationId " 
					 + " 	and spons.unit_id =:unitId "
					 + "	and spons.status ='A' " 
					 + " 	and unitservice.unit_id =:unitId " 
					 + "    and :currentDate >= to_char(ccm.validity_from,'YYYY-MM-DD') "
					 + "    and :currentDate <= to_char(ccm.validity_to,'YYYY-MM-DD') "
					 + " 	and :currentDate >= to_char(spons.applicable_date, 'YYYY-MM-DD') "
					 + " 	order by \"priorityId\" " );
				
				consumptionDto = sessionFactory.getCurrentSession()
						.createSQLQuery(query.toString())
						.setParameter("admissionId",contractConsumptionDto.getAdmissionId())
						.setParameter("packageMasterId",contractConsumptionDto.getPackageMasterId())
						.setParameter("billingBed",contractConsumptionDto.getBillingBedCategoryId())
						.setParameter("serviceId",contractConsumptionDto.getServiceId())
						//.setParameter("patientTypeId", contractConsumptionDto.getPatientCategoryId()!=null ? contractConsumptionDto.getPatientCategoryId() : contractConsumptionDto.getPatientTypeId())
						.setParameter("organizationId", contractConsumptionDto.getOrganizationId())
						.setParameter("unitId", contractConsumptionDto.getUnitId())
						.setParameter("currentDate", contractConsumptionDto.getCurrentDate().substring(0,10))
						//.setParameter("paymentEntitlementId", 1)
						//.setParameter("visitTypeId", contractConsumptionDto.getVisitTypeId())
						//.setDate("effectiveDate", GlobalCommonDateUtils.getDate(contractConsumptionDto.getCurrentDate(),"yyyy-MM-dd"))
						.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
						.list();
			
			}
			
			
			if(contractConsumptionDto.getEncounterId() != null ){
			
				String query=  " select " 
						 + " 	csd.service_id as \"serviceId\", " 
						 + " 	spons.priority_id as \"priorityId\", " 
						 + " 	coalesce(cast(unitservice.rate_editable_max_value as double precision), 0) as \"maxRateEditable\", " 
						 + " 	coalesce(cast(unitservice.rate_editble_min_value as double precision), 0) as \"minRateEditable\", " 
						 + " 	csd.contract_id as \"contractId\", " 
						 + " 	csd.apportioned_price as \"serviceRate\", " 
						 + " 	com.company_desc as \"companyName\", " 
						 + " 	cast(((csd.self_payable / csd.final_price)* 100) as numeric(15, 10)) as \"coShare\", " 
						 + " 	ccm.company_id as \"payeeId\", " 
						 + " 	ccm.associate_company_id as \"associateCompanyId\", " 
						 + " 	csd.tax_id as \"taxId\", " 
						 + " 	coalesce(csd.tax_percentage, 0) as \"taxPercentage\", " 
						 + " 	csd.final_price as \"finalPrice\", " 
						 + " 	csd.self_payable as \"selfPayable\", " 
						 + " 	csd.credit_payable as \"creaditPayable\", " 
						 + " 	( " 
						 + " 	select " 
						 + " 		ccd.balance_cap_amount " 
						 + " 	from " 
						 + " 		service.t_vist_contract_cap_mapper ccd " 
						 + " 	where " 
						 + " 		ccd.department_id = service.speciality_id " 
						 + " 		and ccd.contract_id = csd.contract_id " 
						 + " 		and ccd.encounter_id =5544 " 
						 + " 		and ccd.status = 'A') as \"balanceCapAmount\", " 
						 + " 	service.speciality_id as \"specialityId\", " 
						 + " 	ccm.tariff_id as \"tariffId\", " 
						 + " 	1 as \"dayDiff\", " 
						 + " 	1 as \"type\", " 
						 + " 	coalesce(unitservice.is_rate_editable, cast('N' as char)) as \"isRateEditable\", " 
						 + " 	coalesce(unitservice.is_allow_multiple_quantity, cast('N' as char)) as \"isQuantityEditable\", " 
						 + " 	1 as \"billingBedExist\", " 
						 + " 	false as \"tillDischarge\", " 
						 + " 	ccm.is_tariff_rate_applicable as \"isTariffRateApplicable\", " 
						 + " 	appConfig.self_payee_id as \"selfPayeeId\" , " 
						 + " 	preAuth.balance_amount as \"preAuthBalAmt\", " 
						 + " 	preAuth.preauth_id as \"preauthId\" , " 
						 + " 	coalesce(unitservice.is_discount, 'N') as \"isDiscount\", " 
						 + " 	coalesce(appConfig.is_scheme_discount, 'N') as \"isSchemeDiscount\" " 
						 + " from " 
						 + " 	opd.t_patient_sponsor_details spons " 
						 + " inner join t_company_master com on " 
						 + " 	com.company_id = spons.payee_id " 
						 + " inner join service.t_contract_service_details csd on " 
						 + " 	spons.contract_id = csd.contract_id " 
						 + " inner join service.m_company_contract_master ccm on " 
						 + " 	csd.contract_id = ccm.contract_id " 
						 + " inner join service.m_service_master service on " 
						 + " 	service.service_master_id = csd.service_id " 
						 + " inner join service.t_unit_service_master_details unitservice on " 
						 + " 	service.service_master_id = unitservice.service_master_id " 
						 + " left join billing.m_preauth_master preAuth on " 
						 + " 	preAuth.patient_sponsor_id = spons.patient_sponsor_details_id " 
						 + " 	and preAuth.authorized_amount > 0 " 
						 + " inner join t_unit_application_billing_configuration appConfig on " 
						 + " 	appConfig.unit_id = unitservice.unit_id " 
						 + " where " 
						 + " 	spons.encounter_id =:encounterId " 
						 + " 	and csd.service_id =:serviceId " 
						 + " 	and spons.status ='A' " 
						 + " 	and csd.package_master_id =:packageMasterId " 
						 + " 	and unitservice.unit_id = :unitId " 
						 + " 	and :currentDate >= to_char(ccm.validity_from, 'YYYY-MM-DD') "
						 + " 	and :currentDate <= to_char(ccm.validity_to, 'YYYY-MM-DD') "
						 + " 	and :currentDate >= to_char(spons.applicable_date, 'YYYY-MM-DD') "
						 + " 	and ccm.is_tariff_rate_applicable = 'N' " 
						 + " union " 
						 + " select " 
						 + " 	pack.service_id as \"serviceId\", " 
						 + " 	spons.priority_id as \"priorityId\", " 
						 + " 	coalesce(cast(unitservice.rate_editable_max_value as double precision), 0) as \"maxRateEditable\", " 
						 + " 	coalesce(cast(unitservice.rate_editble_min_value as double precision), 0) as \"minRateEditable\", " 
						 + " 	ccm.contract_id as \"contractId\", " 
						 + " 	cast( pack.package_price as numeric ) as \"serviceRate\", " 
						 + " 	com.company_desc as \"companyName\", " 
						 + " 	ccm.co_share_percentage as \"coShare\", " 
						 + " 	ccm.company_id as \"payeeId\", " 
						 + " 	ccm.associate_company_id as \"associateCompanyId\", " 
						 + " 	unitservice.gst_type_id as \"taxId\", " 
						 + " 	coalesce(tax.tax_percentage, 0) as \"taxPercentage\", " 
						 + " 	(pack.package_price +(pack.package_price*(coalesce(tax.tax_percentage, 0)* 0.01))) as \"finalPrice\", " 
						 + " 	((pack.package_price +(pack.package_price*(coalesce(tax.tax_percentage, 0)* 0.01)))*(coalesce(ccm.co_share_percentage, 0)* 0.01)) as \"selfPayable\", " 
						 + " 	((pack.package_price +(pack.package_price*(coalesce(tax.tax_percentage, 0)* 0.01)))-((pack.package_price +(pack.package_price*(coalesce(tax.tax_percentage, 0)* 0.01)))*(coalesce(ccm.co_share_percentage, 0)* 0.01))) as \"creaditPayable\", " 
						 + " 	null as \"balanceCapAmount\", " 
						 + " 	service.speciality_id as \"specialityId\", " 
						 + " 	ccm.tariff_id as \"tariffId\", " 
						 + " 	1 as \"dayDiff\", " 
						 + " 	1 as \"type\", " 
						 + " 	coalesce(unitservice.is_rate_editable, cast('N' as char)) as \"isRateEditable\", " 
						 + " 	coalesce(unitservice.is_allow_multiple_quantity, cast('N' as char)) as \"isQuantityEditable\", " 
						 + " 	1 as \"billingBedExist\", " 
						 + " 	false as \"tillDischarge\", " 
						 + " 	ccm.is_tariff_rate_applicable as \"isTariffRateApplicable\", " 
						 + " 	appConfig.self_payee_id as \"selfPayeeId\" , " 
						 + " 	preAuth.balance_amount as \"preAuthBalAmt\", " 
						 + " 	preAuth.preauth_id as \"preauthId\", " 
						 + " 	coalesce(unitservice.is_discount, 'N') as \"isDiscount\", " 
						 + " 	coalesce(appConfig.is_scheme_discount, 'N') as \"isSchemeDiscount\" " 
						 + " from " 
						 + " 	opd.t_patient_sponsor_details spons " 
						 + " inner join t_company_master com on " 
						 + " 	com.company_id = spons.payee_id " 
						 + " inner join service.m_company_contract_master ccm on " 
						 + " 	spons.contract_id = ccm.contract_id " 
						 + " inner join service.m_package_master pack on " 
						 + " 	pack.package_master_id =:packageMasterId " 
						 + " inner join service.m_service_master service on " 
						 + " 	service.service_master_id = pack.service_id " 
						 + " inner join service.t_unit_service_master_details unitservice on " 
						 + " 	service.service_master_id = unitservice.service_master_id " 
						 + " left join m_tax tax on " 
						 + " 	tax.id = unitservice.gst_type_id " 
						 + " inner join doctor.m_speciality_master speciality on " 
						 + " 	service.speciality_id = speciality.speciality_id " 
						 + " inner join doctor.m_sub_speciality_master subSpeciality on " 
						 + " 	service.sub_speciality_id = subSpeciality.sub_speciality_id " 
						 + " left join billing.m_preauth_master preAuth on " 
						 + " 	preAuth.patient_sponsor_id = spons.patient_sponsor_details_id " 
						 + " 	and preAuth.authorized_amount > 0 " 
						 + " inner join t_unit_application_billing_configuration appConfig on " 
						 + " 	appConfig.unit_id = unitservice.unit_id " 
						 + " where " 
						 + " 	spons.encounter_id =:encounterId " 
						 + " 	and ccm.is_tariff_rate_applicable = 'Y' " 
						 + " 	and spons.organization_id =:organizationId " 
						 + " 	and spons.unit_id =:unitId " 
						 + " 	and spons.status ='A' " 
						 + " 	and unitservice.unit_id =:unitId " 
						 + " 	and :currentDate >= to_char(ccm.validity_from, 'YYYY-MM-DD') "
						 + " 	and :currentDate <= to_char(ccm.validity_to, 'YYYY-MM-DD') "
						 + " 	and :currentDate >= to_char(spons.applicable_date, 'YYYY-MM-DD') "
						 + " order by " 
						 + " 	\"priorityId\" " ;
				
				consumptionDto = sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setParameter("encounterId",contractConsumptionDto.getEncounterId())
						.setParameter("packageMasterId",contractConsumptionDto.getPackageMasterId())
						.setParameter("serviceId",contractConsumptionDto.getServiceId())
						.setParameter("organizationId", contractConsumptionDto.getOrganizationId())
						.setParameter("unitId", contractConsumptionDto.getUnitId())
						.setParameter("currentDate", contractConsumptionDto.getCurrentDate().substring(0,10))
						.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
						.list();
			}
			
			return consumptionDto;
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
		}
	
	
	@Override
	public ContractConsumptionDto getItemDetailsByContractId(ContractConsumptionDto itemsForContractDto,Integer itemId)
			throws ServiceException {
		try {
			
			//Date currentDate  =  TimeZoneComponent.getDateByzone(itemsForContractDto.getUnitId());
			String pharmacyContractQuery = null;
			ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
			if(itemsForContractDto.getEncounterId()!=null && itemsForContractDto.getEncounterId()>0)
			{
				 pharmacyContractQuery =
						 " select " 
								 + " 	balance_amount as \"balanceAmount\", " 
								 + " 	balance_quantity as \"balanceQuantity\", " 
								 + " 	( " 
								 + " 	select " 
								 + " 		prod_cat_co_share " 
								 + " 	from " 
								 + " 		service.t_contract_cap_details " 
								 + " 	where " 
								 + " 		product_category_id = vccm.product_category_id " 
								 + " 		and contract_id = vccm.contract_id ) as \"coShare\", " 
								 + " 	mccm.company_id as \"payeeId\", "
								 + "	1 as \"type\" " 
								 + " from " 
								 + " 	service.t_vist_contract_cap_mapper vccm " 
								 + " inner join service.m_company_contract_master mccm on  " 
								 + " 		mccm.contract_id = vccm.contract_id "
								 + " inner join opd.t_patient_sponsor_details tpsd on "
								 + " 		tpsd.patient_sponsor_details_id = vccm.patient_sponsor_details_id " 
								 + " where " 
								 + " 	vccm.contract_id = :contractId " 
								 + " 	and vccm.product_category_id =:prodCaT " 
								 + " 	and vccm.encounter_id = "+itemsForContractDto.getEncounterId() 
								 + " 	and '"+itemsForContractDto.getOrderDate().substring(0,10)+"' >= to_char(mccm.validity_from,'YYYY-MM-DD') "
								 + " 	and '"+itemsForContractDto.getOrderDate().substring(0,10)+"' <= to_char(mccm.validity_to,'YYYY-MM-DD') "
								 + " 	and '"+itemsForContractDto.getOrderDate().substring(0,10)+"' >= to_char(tpsd.applicable_date, 'YYYY-MM-DD') "
								 + " 	and tpsd.status ='A' "
								 + " 	and vccm.status = 'A' " 
								 + " 	and (balance_amount > 0 " 
								 + " 	or balance_quantity > 0) " 
								 + " 	and not exists( " 
								 + " 	select " 
								 + " 		tcpe.contract_pharmacy_exclusion_id " 
								 + " 	from " 
								 + " 		service.t_contract_pharmacy_exclusion tcpe " 
								 + " 	where " 
								 + " 		product_category_id =:prodCaT " 
								 + " 		and contract_id = :contractId) " 
								 + " union " 
								 + " select " 
								 + " 	balance_amount as \"balanceAmount\", " 
								 + " 	balance_quantity as \"balanceQuantity\", " 
								 + " 	( " 
								 + " 	select " 
								 + " 		prod_cat_co_share " 
								 + " 	from " 
								 + " 		service.t_contract_cap_details " 
								 + " 	where " 
								 + " 		product_category_id = vccm.product_category_id " 
								 + " 		and contract_id = vccm.contract_id ) as \"coShare\", " 
								 + " 		mccm.company_id as \"payeeId\",	"
								 + "	2 as \"type\" "
								 + " from " 
								 + " 	service.t_vist_contract_cap_mapper vccm " 
								 + " inner join service.m_company_contract_master mccm on  " 
								 + " 		mccm.contract_id = vccm.contract_id	 " 
								 + " inner join opd.t_patient_sponsor_details tpsd on "
								 + " 		tpsd.patient_sponsor_details_id = vccm.patient_sponsor_details_id "
								 + " where " 
								 + " 	vccm.contract_id = :contractId " 
								 + " 	and vccm.product_category_id =:prodCaT " 
								 + " 	and vccm.encounter_id ="+itemsForContractDto.getEncounterId() 
								 + " 	and vccm.status = 'A' " 
								 + " 	and '"+itemsForContractDto.getOrderDate().substring(0,10)+"' >= to_char(mccm.validity_from,'YYYY-MM-DD') "
								 + " 	and '"+itemsForContractDto.getOrderDate().substring(0,10)+"' <= to_char(mccm.validity_to,'YYYY-MM-DD') "
								 + " 	and '"+itemsForContractDto.getOrderDate().substring(0,10)+"' >= to_char(tpsd.applicable_date, 'YYYY-MM-DD') "
								 + " 	and tpsd.status ='A' "
								 + " 	and (balance_amount > 0 " 
								 + " 	or balance_quantity > 0) " 
								 + " 	and exists( " 
								 + " 	select " 
								 + " 		tcpe.contract_pharmacy_exclusion_id " 
								 + " 	from " 
								 + " 		service.t_contract_pharmacy_exclusion tcpe " 
								 + " 	where " 
								 + " 		product_category_id =:prodCaT " 
								 + " 		and contract_id = :contractId) " 
								 + " 	and exists( " 
								 + " 	select " 
								 + " 		item_id " 
								 + " 	from " 
								 + " 		service.t_contract_item_details " 
								 + " 	where " 
								 + " 		item_id =:itemId " 
								 + " 		and contract_id = :contractId) ";
						 
			}else if(itemsForContractDto.getAdmissionId()!=null && itemsForContractDto.getAdmissionId()>0)
			{
					 pharmacyContractQuery = " select " 
									 + " 	balance_amount as \"balanceAmount\", " 
									 + " 	balance_quantity as \"balanceQuantity\", " 
									 + " 	( " 
									 + " 	select " 
									 + " 		prod_cat_co_share " 
									 + " 	from " 
									 + " 		service.t_contract_cap_details " 
									 + " 	where " 
									 + " 		product_category_id = vccm.product_category_id " 
									 + " 		and contract_id = vccm.contract_id ) as \"coShare\", " 
									 + " 	mccm.company_id as \"payeeId\",	 "
									 + "	1 as \"type\" "
									 + " from " 
									 + " 	service.t_vist_contract_cap_mapper vccm " 
									 + " inner join service.m_company_contract_master mccm on  " 
									 + " 		mccm.contract_id = vccm.contract_id " 
									 + " inner join opd.t_patient_sponsor_details tpsd on "
									 + " 		tpsd.patient_sponsor_details_id = vccm.patient_sponsor_details_id "
									 + " where " 
									 + " 	vccm.contract_id = :contractId " 
									 + " 	and vccm.product_category_id =:prodCaT " 
									 + " 	and vccm.admission_id ="+itemsForContractDto.getAdmissionId()
									 + " 	and '"+itemsForContractDto.getOrderDate().substring(0,10)+"' >= to_char(mccm.validity_from,'YYYY-MM-DD') "
									 + " 	and '"+itemsForContractDto.getOrderDate().substring(0,10)+"' <= to_char(mccm.validity_to,'YYYY-MM-DD') "
									 + " 	and '"+itemsForContractDto.getOrderDate().substring(0,10)+"' >= to_char(tpsd.applicable_date, 'YYYY-MM-DD') "
									 + " 	and tpsd.status ='A' "
									 + " 	and vccm.status = 'A' " 
									 + " 	and (balance_amount > 0 " 
									 + " 	or balance_quantity > 0) " 
									 + " 	and not exists( " 
									 + " 	select " 
									 + " 		tcpe.contract_pharmacy_exclusion_id " 
									 + " 	from " 
									 + " 		service.t_contract_pharmacy_exclusion tcpe " 
									 + " 	where " 
									 + " 		product_category_id =:prodCaT " 
									 + " 		and contract_id = :contractId) " 
									 + " union " 
									 + " select " 
									 + " 	balance_amount as \"balanceAmount\", " 
									 + " 	balance_quantity as \"balanceQuantity\", " 
									 + " 	( " 
									 + " 	select " 
									 + " 		prod_cat_co_share " 
									 + " 	from " 
									 + " 		service.t_contract_cap_details " 
									 + " 	where " 
									 + " 		product_category_id = vccm.product_category_id " 
									 + " 		and contract_id = vccm.contract_id ) as \"coShare\", " 
									 + " 	mccm.company_id as \"payeeId\",	 "
									 + "	2 as \"type\" "
									 + " from " 
									 + " 	service.t_vist_contract_cap_mapper vccm " 
									 + " inner join service.m_company_contract_master mccm on  " 
									 + " 		mccm.contract_id = vccm.contract_id	 " 
									 + " inner join opd.t_patient_sponsor_details tpsd on "
									 + " 		tpsd.patient_sponsor_details_id = vccm.patient_sponsor_details_id "
									 + " where " 
									 + " 	vccm.contract_id = :contractId " 
									 + " 	and vccm.product_category_id =:prodCaT " 
									 + " 	and vccm.admission_id ="+itemsForContractDto.getAdmissionId()
									 + " 	and vccm.status = 'A' " 
									 + " 	and '"+itemsForContractDto.getOrderDate().substring(0,10)+"' >= to_char(mccm.validity_from,'YYYY-MM-DD') "
									 + " 	and '"+itemsForContractDto.getOrderDate().substring(0,10)+"' <= to_char(mccm.validity_to,'YYYY-MM-DD') "
									 + " 	and '"+itemsForContractDto.getOrderDate().substring(0,10)+"' >= to_char(tpsd.applicable_date, 'YYYY-MM-DD') "
									 + " 	and tpsd.status ='A' "
									 + " 	and (balance_amount > 0 " 
									 + " 	or balance_quantity > 0) " 
									 + " 	and exists( " 
									 + " 	select " 
									 + " 		tcpe.contract_pharmacy_exclusion_id " 
									 + " 	from " 
									 + " 		service.t_contract_pharmacy_exclusion tcpe " 
									 + " 	where " 
									 + " 		product_category_id =:prodCaT " 
									 + " 		and contract_id = :contractId) " 
									 + " 	and exists( " 
									 + " 	select " 
									 + " 		item_id " 
									 + " 	from " 
									 + " 		service.t_contract_item_details " 
									 + " 	where " 
									 + " 		item_id =:itemId " 
									 + " 		and contract_id = :contractId) ";
				}else if((itemsForContractDto.getEncounterId()==null ||itemsForContractDto.getEncounterId()==0) && (itemsForContractDto.getAdmissionId()==null||itemsForContractDto.getAdmissionId()==0) && itemsForContractDto.getPatientId()!=null && itemsForContractDto.getPatientId()>0)
				{
            	   pharmacyContractQuery =" select " 
							 + " 	balance_amount as \"balanceAmount\", " 
							 + " 	balance_quantity as \"balanceQuantity\", " 
							 + " 	( " 
							 + " 	select " 
							 + " 		prod_cat_co_share " 
							 + " 	from " 
							 + " 		service.t_contract_cap_details " 
							 + " 	where " 
							 + " 		product_category_id = vccm.product_category_id " 
							 + " 		and contract_id = vccm.contract_id ) as \"coShare\", " 
							 + " 	mccm.company_id as \"payeeId\",	 "
							 + "	1 as \"type\" " 
							 + " from " 
							 + " 	service.t_vist_contract_cap_mapper vccm " 
							 + " inner join service.m_company_contract_master mccm on  " 
							 + " 		mccm.contract_id = vccm.contract_id " 
							 + " inner join opd.t_patient_sponsor_details tpsd on "
							 + " 		tpsd.patient_sponsor_details_id = vccm.patient_sponsor_details_id "
							 + " where " 
							 + " 	vccm.contract_id = :contractId " 
							 + " 	and vccm.product_category_id =:prodCaT " 
							 + " 	and vccm.admission_id is null "
							 + " 	and vccm.encounter_id is null "
							 + " 	and vccm.patient_id ="+itemsForContractDto.getPatientId()
							 + " 	and '"+itemsForContractDto.getOrderDate().substring(0,10)+"' >= to_char(mccm.validity_from,'YYYY-MM-DD') "
							 + " 	and '"+itemsForContractDto.getOrderDate().substring(0,10)+"' <= to_char(mccm.validity_to,'YYYY-MM-DD') "
							 + " 	and '"+itemsForContractDto.getOrderDate().substring(0,10)+"' >= to_char(tpsd.applicable_date, 'YYYY-MM-DD') "
							 + " 	and tpsd.status ='A' "
							 + " 	and vccm.status = 'A' " 
							 + " 	and (balance_amount > 0 " 
							 + " 	or balance_quantity > 0) " 
							 + " 	and not exists( " 
							 + " 	select " 
							 + " 		tcpe.contract_pharmacy_exclusion_id " 
							 + " 	from " 
							 + " 		service.t_contract_pharmacy_exclusion tcpe " 
							 + " 	where " 
							 + " 		product_category_id =:prodCaT " 
							 + " 		and contract_id = :contractId) " 
							 + " union " 
							 + " select " 
							 + " 	balance_amount as \"balanceAmount\", " 
							 + " 	balance_quantity as \"balanceQuantity\", " 
							 + " 	( " 
							 + " 	select " 
							 + " 		prod_cat_co_share " 
							 + " 	from " 
							 + " 		service.t_contract_cap_details " 
							 + " 	where " 
							 + " 		product_category_id = vccm.product_category_id " 
							 + " 		and contract_id = vccm.contract_id ) as \"coShare\", " 
							 + " 		mccm.company_id as \"payeeId\",	 "
							 + "		2 as \"type\" " 
							 + " from " 
							 + " 	service.t_vist_contract_cap_mapper vccm " 
							 + " inner join service.m_company_contract_master mccm on  " 
							 + " 		mccm.contract_id = vccm.contract_id	 " 
							 + " inner join opd.t_patient_sponsor_details tpsd on "
							 + " 		tpsd.patient_sponsor_details_id = vccm.patient_sponsor_details_id "
							 + " where " 
							 + " 	vccm.contract_id = :contractId " 
							 + " 	and vccm.product_category_id =:prodCaT " 
							 + " 	and vccm.admission_id is null "
							 + " 	and vccm.encounter_id is null "
							 + " 	and vccm.patient_id ="+itemsForContractDto.getPatientId()
							 + " 	and vccm.status = 'A' " 
							 + " 	and '"+itemsForContractDto.getOrderDate().substring(0,10)+"' >= to_char(mccm.validity_from,'YYYY-MM-DD') "
							 + " 	and '"+itemsForContractDto.getOrderDate().substring(0,10)+"' <= to_char(mccm.validity_to,'YYYY-MM-DD') "
							 + " 	and '"+itemsForContractDto.getOrderDate().substring(0,10)+"' >= to_char(tpsd.applicable_date, 'YYYY-MM-DD') "
							 + " 	and tpsd.status ='A' "
							 + " 	and (balance_amount > 0 " 
							 + " 	or balance_quantity > 0) " 
							 + " 	and exists( " 
							 + " 	select " 
							 + " 		tcpe.contract_pharmacy_exclusion_id " 
							 + " 	from " 
							 + " 		service.t_contract_pharmacy_exclusion tcpe " 
							 + " 	where " 
							 + " 		product_category_id =:prodCaT " 
							 + " 		and contract_id = :contractId) " 
							 + " 	and exists( " 
							 + " 	select " 
							 + " 		item_id " 
							 + " 	from " 
							 + " 		service.t_contract_item_details " 
							 + " 	where " 
							 + " 		item_id =:itemId " 
							 + " 		and contract_id = :contractId) ";
            			   
               }
			
			consumptionDto = (ContractConsumptionDto)sessionFactory.getCurrentSession()
					.createSQLQuery(pharmacyContractQuery)
					.setParameter("contractId",itemsForContractDto.getContractId())
					.setParameter("prodCaT", itemsForContractDto.getProductCategoryId())
					.setParameter("itemId",itemId)
					.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
					.uniqueResult();

			return consumptionDto;
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public List<ContractConsumptionDto> getSponsorDetailsByEncounterId(ContractConsumptionDto consumptionDto) throws ServiceException {
		try {

			List<ContractConsumptionDto> list =  new ArrayList<ContractConsumptionDto>();
			if(consumptionDto.getEncounterId()!= null)
			{
				list = sessionFactory.getCurrentSession()
							.createSQLQuery("select contract_id as \"contractId\", "
									+ "priority_id as \"priorityId\" "
									+ "FROM opd.t_patient_sponsor_details "
									+ " WHERE encounter_id = :encounterId "
									+ "	and payment_entitlement_id !=1 "
									+ " order by priority_id ")
							.setParameter("encounterId",consumptionDto.getEncounterId())
							.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
							.list();
			}
			if(consumptionDto.getAdmissionId()!=null)
			{
				list = sessionFactory.getCurrentSession()
							.createSQLQuery("select contract_id as \"contractId\", "
									+ "priority_id as \"priorityId\" "
									+ "FROM opd.t_patient_sponsor_details "
									+ " WHERE admission_id =:admissionId "
									+ "	and payment_entitlement_id !=1 "
									+ " order by priority_id ")
							.setParameter("admissionId",consumptionDto.getAdmissionId())
							.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
							.list();
			}
			
			
			return  list;
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
	}
	
	
	@Override
	public List<ContractConsumptionDto> getSponsorDetailsByEncounterPharmacy(ContractConsumptionDto consumptionDto)
			throws ServiceException {
		try {

			List<ContractConsumptionDto> list = new ArrayList<ContractConsumptionDto>();
			if(consumptionDto.getEncounterId()!= null )
			{
				list = sessionFactory.getCurrentSession()
							.createSQLQuery("select "
									+ "	distinct psd.contract_id as \"contractId\", "
									+ "	cm.company_desc as \"companyName\", "
									+ "	cm.company_id as \"payeeId\", "
									+ "	psd.priority_id as \"priority\" , "
									+ "	ccm.is_tariff_rate_applicable as \"isTariffRateApplicable\", "
									+ " ccm.co_share_percentage as \"coShare\", "
									+ "	ccm.associate_company_id as \"associateCompanyId\", "
									+ " preAuth.balance_amount as \"preAuthBalAmt\", "
									+ " preAuth.preauth_id as \"preauthId\", "
									+ " tuabc.self_payee_id as \"selfPayeeId\", "
									+ " tuabc.default_self_tariff_id as \"defaultSelfTariffId\" "
									+ "from "
									+ "		opd.t_patient_sponsor_details psd "
									+ "	inner join public.t_company_master cm on "
									+ "		psd.payee_id = cm.company_id "
									+ "	inner join service.m_company_contract_master ccm on "
									+ "		psd.contract_id = ccm.contract_id "
									+ " inner join t_unit_application_billing_configuration tuabc on "
									+ "		tuabc.unit_id = psd.unit_id "
									+ " left join billing.m_preauth_master preAuth on "
									+ "		preAuth.patient_sponsor_id = psd.patient_sponsor_details_id "
									+ "		and psd.encounter_id=preAuth.encounter_id "
									+ "		and preAuth.authorized_amount > 0	"
									+ "where "
									+ "		psd.encounter_id = :encounterId "
									+ "		and psd.payment_entitlement_id != 1 "
									+ "		and psd.status = 'A' "
									+ " 	and :orderDate >= to_char(psd.applicable_date, 'YYYY-MM-DD') "
									+ " order by "
									+ "	\"priority\"")
							.setParameter("encounterId",consumptionDto.getEncounterId())
						    .setParameter("orderDate",consumptionDto.getOrderDate().substring(0,10))
							.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
							.list();
			}
			
			if(consumptionDto.getAdmissionId()!= null)
			{
				list = sessionFactory.getCurrentSession()
							.createSQLQuery("select "
									+ "	distinct psd.contract_id as \"contractId\", "
									+ "	cm.company_desc as \"companyName\", "
									+ "	cm.company_id as \"payeeId\", "
									+ "	psd.priority_id as \"priority\" , "
									+ "	ccm.is_tariff_rate_applicable as \"isTariffRateApplicable\", "
									+ " ccm.co_share_percentage as \"coShare\", "
									+ "	ccm.associate_company_id as \"associateCompanyId\", "
									+ " preAuth.balance_amount as \"preAuthBalAmt\", "
									+ " preAuth.preauth_id as \"preauthId\", "
									+ " tuabc.self_payee_id as \"selfPayeeId\", "
									+ " tuabc.default_self_tariff_id as \"defaultSelfTariffId\" "
									+ "from "
									+ "		opd.t_patient_sponsor_details psd "
									+ "	inner join public.t_company_master cm on "
									+ "		psd.payee_id = cm.company_id "
									+ "	inner join service.m_company_contract_master ccm on "
									+ "		psd.contract_id = ccm.contract_id "
									+ " inner join t_unit_application_billing_configuration tuabc on "
									+ "		tuabc.unit_id = psd.unit_id "
									+ " left join billing.m_preauth_master preAuth on "
									+ "		preAuth.patient_sponsor_id = psd.patient_sponsor_details_id "
									+ "		and psd.admission_id=preAuth.admission_id "
									+ "		and preAuth.authorized_amount > 0	"
									+ "where "
									+ "		psd.admission_id = :admissionId "
									+ "		and psd.payment_entitlement_id != 1 "
									+ "		and psd.status = 'A' "
									+ " 	and :orderDate >= to_char(psd.applicable_date, 'YYYY-MM-DD') "
									+ "order by "
									+ "	\"priority\"")
							.setParameter("admissionId",consumptionDto.getAdmissionId())
						    .setParameter("orderDate",consumptionDto.getOrderDate().substring(0,10))
							.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
							.list();
			}
			if(consumptionDto.getPatientId()!=null&& consumptionDto.getPatientId()>0 && consumptionDto.getEncounterId()==null && consumptionDto.getAdmissionId()==null)
			{
				list = sessionFactory.getCurrentSession()
						.createSQLQuery( "select "
								+ "	distinct psd.contract_id as \"contractId\", "
								+ "	cm.company_desc as \"companyName\", "
								+ "	cm.company_id as \"payeeId\", "
								+ "	psd.priority_id as \"priority\", "
								+ "	ccm.is_tariff_rate_applicable as \"isTariffRateApplicable\", "
								+ " ccm.co_share_percentage as \"coShare\", "
								+ "	ccm.associate_company_id as \"associateCompanyId\", "
								+ " preAuth.balance_amount as \"preAuthBalAmt\", "
								+ " preAuth.preauth_id as \"preauthId\", "
								+ " tuabc.self_payee_id as \"selfPayeeId\", "
								+ " tuabc.default_self_tariff_id as \"defaultSelfTariffId\" "
								+ " from "
								+ "	opd.t_patient_sponsor_details psd "
								+ " inner join PUBLIC.t_company_master cm on psd.payee_id = cm.company_id "
								+ " inner join t_unit_application_billing_configuration tuabc on tuabc.unit_id = psd.unit_id "
								+ " inner join service.m_company_contract_master ccm on psd.contract_id = ccm.contract_id "
								+ " left join billing.m_preauth_master preAuth on preAuth.patient_sponsor_id = psd.patient_sponsor_details_id and preAuth.authorized_amount > 0	"
								+ "where "
								+ "	psd.patient_id = :patientId "
								+ "	and psd.admission_id is null "
								+ "	and psd.encounter_id is null "
								+ "	and psd.payment_entitlement_id != 1 "
								+ "	and psd.status = 'A' "
								+ " and :orderDate >= to_char(psd.applicable_date, 'YYYY-MM-DD') "
								+ "order by "
								+ "	\"priority\"")
						.setParameter("patientId",consumptionDto.getPatientId())
						.setParameter("orderDate",consumptionDto.getOrderDate().substring(0,10))
						.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
						.list();
			}
			
			return  list;
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
	}
	

	@Override
	public void updateCapAmount(ContractConsumptionDto consumptionDto, Double creaditPayable) throws ServiceException {
		try {
			
			if(consumptionDto.getEncounterId()!=null){
				sessionFactory.getCurrentSession()
						.createSQLQuery( "UPDATE service.t_vist_contract_cap_mapper SET balance_cap_amount=balance_cap_amount - :creaditPayable " 
								 + " 	WHERE contract_id=:contractId  " 
								 + " 	AND department_id=:specialityId  " 
								 + " 	AND encounter_id=:encounterId"
								 + "	AND status='A'")
						.setParameter("encounterId",consumptionDto.getEncounterId())
						.setParameter("specialityId",consumptionDto.getSpecialityId())
						.setParameter("contractId",consumptionDto.getContractId())
						.setDouble("creaditPayable",creaditPayable).executeUpdate();
				}
				if(consumptionDto.getAdmissionId()!=null){
					sessionFactory.getCurrentSession()
							.createSQLQuery( "UPDATE service.t_vist_contract_cap_mapper SET balance_cap_amount=balance_cap_amount - :creaditPayable " 
									 + " 	WHERE contract_id=:contractId  " 
									 + " 	AND department_id=:specialityId  " 
									 + " 	AND admission_id=:admissionId"
									 + "	AND status='A'")
							.setParameter("admissionId",consumptionDto.getAdmissionId())
							.setParameter("specialityId",consumptionDto.getSpecialityId())
							.setParameter("contractId",consumptionDto.getContractId())
							.setDouble("creaditPayable",creaditPayable).executeUpdate();
					}
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
		
	}
	
	@Override
	public void updatePreAuthAmount(Integer preAuthId, Double amount) throws ServiceException {
		try {
				Session session = sessionFactory.openSession();
				Transaction tr = session.beginTransaction();
					session.createSQLQuery(" update billing.m_preauth_master mpm set balance_amount=balance_amount - :amount where preauth_id=:preAuthId ")
							.setParameter("preAuthId",preAuthId)
							.setDouble("amount",amount).executeUpdate();
				tr.commit();
				session.close();
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
		
	}
	
	@Override
	public void updateServiceCapAmount(ContractConsumptionDto consumptionDto, Double creaditPayable,Double qty) throws ServiceException {
		try {
			
			if(creaditPayable!=null && creaditPayable>0)
			{
				if(consumptionDto.getEncounterId()!=null)
				{
					
					if(consumptionDto.getType()==3)
					{
						sessionFactory.getCurrentSession()
						.createSQLQuery( "UPDATE service.t_vist_contract_cap_mapper SET balance_amount=balance_amount - :creaditPayable " 
								 + " 	WHERE contract_id=:contractId  " 
								 + " 	AND service_id=:serviceId  " 
								 + " 	AND encounter_id=:encounterId"
								 + "	AND status='A'")
						.setParameter("encounterId",consumptionDto.getEncounterId())
						.setParameter("serviceId",consumptionDto.getServiceId())
						.setParameter("contractId",consumptionDto.getContractId())
						.setDouble("creaditPayable",creaditPayable)
						.executeUpdate();
					}
					
					if(consumptionDto.getType()==4)
					{
						sessionFactory.getCurrentSession()
						.createSQLQuery( "UPDATE service.t_vist_contract_cap_mapper SET applicable_date =:applicableDate, balance_amount=amount - :creaditPayable " 
								 + " 	WHERE contract_id=:contractId  " 
								 + " 	AND service_id=:serviceId  " 
								 + " 	AND encounter_id=:encounterId"
								 + "	AND status='A'")
						.setParameter("encounterId",consumptionDto.getEncounterId())
						.setParameter("serviceId",consumptionDto.getServiceId())
						.setParameter("contractId",consumptionDto.getContractId())
						.setDouble("creaditPayable",creaditPayable)
						.setDate("applicableDate",GlobalCommonDateUtils.getDate(consumptionDto.getApplicableDate(),"yyyy-MM-dd"))
						.executeUpdate();
					}
					
				}
				if(consumptionDto.getAdmissionId()!=null)
				{
					
					if(consumptionDto.getType()==3)
					{
						sessionFactory.getCurrentSession()
						.createSQLQuery( "UPDATE service.t_vist_contract_cap_mapper SET  balance_amount=balance_amount - :creaditPayable " 
								 + " 	WHERE contract_id=:contractId  " 
								 + " 	AND service_id=:serviceId  " 
								 + " 	AND admission_id=:admissionId"
								 + "	AND status='A'")
						.setParameter("admissionId",consumptionDto.getAdmissionId())
						.setParameter("serviceId",consumptionDto.getServiceId())
						.setParameter("contractId",consumptionDto.getContractId())
						.setDouble("creaditPayable",creaditPayable)
						.executeUpdate();
					}
					if(consumptionDto.getType()==4)
					{
						sessionFactory.getCurrentSession()
						.createSQLQuery( "UPDATE service.t_vist_contract_cap_mapper SET applicable_date =:applicableDate, balance_amount=amount - :creaditPayable " 
								 + " 	WHERE contract_id=:contractId  " 
								 + " 	AND service_id=:serviceId  " 
								 + " 	AND admission_id=:admissionId"
								 + "	AND status='A'")
						.setParameter("admissionId",consumptionDto.getAdmissionId())
						.setParameter("serviceId",consumptionDto.getServiceId())
						.setParameter("contractId",consumptionDto.getContractId())
						.setDouble("creaditPayable",creaditPayable)
						.setDate("applicableDate",GlobalCommonDateUtils.getDate(consumptionDto.getApplicableDate(),"yyyy-MM-dd"))
						.executeUpdate();
					}
				}
			}
			
			if(qty!=null)
			{
				if(consumptionDto.getEncounterId()!=null)
				{
					
					if(consumptionDto.getType()==3)
					{
						sessionFactory.getCurrentSession()
						.createSQLQuery( "UPDATE service.t_vist_contract_cap_mapper SET balance_quantity=balance_quantity - :qty " 
								 + " 	WHERE contract_id=:contractId  " 
								 + " 	AND service_id=:serviceId  " 
								 + " 	AND encounter_id=:encounterId"
								 + "	AND status='A'")
						.setParameter("encounterId",consumptionDto.getEncounterId())
						.setParameter("serviceId",consumptionDto.getServiceId())
						.setParameter("contractId",consumptionDto.getContractId())
						.setDouble("qty",qty)
						.executeUpdate();
					}
					
					if(consumptionDto.getType()==4)
					{
						sessionFactory.getCurrentSession()
						.createSQLQuery( "UPDATE service.t_vist_contract_cap_mapper SET applicable_date =:applicableDate, balance_quantity=balance_quantity - :qty " 
								 + " 	WHERE contract_id=:contractId  " 
								 + " 	AND service_id=:serviceId  " 
								 + " 	AND encounter_id=:encounterId"
								 + "	AND status='A'")
						.setParameter("encounterId",consumptionDto.getEncounterId())
						.setParameter("serviceId",consumptionDto.getServiceId())
						.setParameter("contractId",consumptionDto.getContractId())
						.setDouble("qty",qty)
						.setDate("applicableDate",GlobalCommonDateUtils.getDate(consumptionDto.getApplicableDate(),"yyyy-MM-dd"))
						.executeUpdate();
					}
					
					
				}
				if(consumptionDto.getAdmissionId()!=null)
				{
					
					if(consumptionDto.getType()==3)
					{
						sessionFactory.getCurrentSession()
						.createSQLQuery( "UPDATE service.t_vist_contract_cap_mapper SET  balance_quantity=balance_quantity - :qty " 
								 + " 	WHERE contract_id=:contractId  " 
								 + " 	AND service_id=:serviceId  " 
								 + " 	AND admission_id=:admissionId"
								 + "	AND status='A'")
						.setParameter("admissionId",consumptionDto.getAdmissionId())
						.setParameter("serviceId",consumptionDto.getServiceId())
						.setParameter("contractId",consumptionDto.getContractId())
						.setDouble("qty",qty)
						.executeUpdate();
					}
					
					if(consumptionDto.getType()==4)
					{
						sessionFactory.getCurrentSession()
						.createSQLQuery( "UPDATE service.t_vist_contract_cap_mapper SET applicable_date =:applicableDate, balance_quantity=balance_quantity - :qty " 
								 + " 	WHERE contract_id=:contractId  " 
								 + " 	AND service_id=:serviceId  " 
								 + " 	AND admission_id=:admissionId"
								 + "	AND status='A'")
						.setParameter("admissionId",consumptionDto.getAdmissionId())
						.setParameter("serviceId",consumptionDto.getServiceId())
						.setParameter("contractId",consumptionDto.getContractId())
						.setDouble("qty",qty)
						.setDate("applicableDate",GlobalCommonDateUtils.getDate(consumptionDto.getApplicableDate(),"yyyy-MM-dd"))
						.executeUpdate();
					}
					
				}
			}
			
			
			
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
		
	}
	
	@Override
	public Integer updatePharmacyCapQuantity(ContractConsumptionDto consumptionDto, Double creditPayable,Integer quantity) throws ServiceException {
		try {
			
			String pharamacyCapUpdateQuery = null;
			StringBuilder sb = new StringBuilder();
			Integer result  = 0;
			Double crPayble  = BigDecimal.valueOf(creditPayable).doubleValue();
			if(quantity!=null &&quantity!=0)
			{
				pharamacyCapUpdateQuery = "UPDATE service.t_vist_contract_cap_mapper SET balance_quantity=balance_quantity +:quantity  "
						 + " 	WHERE contract_id=:contractId  "
						 + " 	AND product_category_id=:productCategoryId  "
						 + "	AND status='A'" ;

			}

			else if(crPayble!=null && crPayble!=0)
			{
				
				pharamacyCapUpdateQuery = "UPDATE service.t_vist_contract_cap_mapper SET balance_amount=balance_amount +:crPayble "
						 + " 	WHERE contract_id=:contractId  " 
						 + " 	AND product_category_id=:productCategoryId  " 
						 + "	AND status='A'" ;
				
			}
			
			if(consumptionDto.getPatientId()!=null &&consumptionDto.getPatientId()>0 && consumptionDto.getEncounterId()==null && consumptionDto.getAdmissionId()==null) 
			{
				if(quantity!=null &&quantity!=0)
				{
					pharamacyCapUpdateQuery = "UPDATE service.t_vist_contract_cap_mapper SET balance_quantity=balance_quantity +:quantity "
							 + " 	WHERE contract_id=:contractId  " 
							 + " 	AND product_category_id=:productCategoryId  " 
							 + " 	AND admission_id is null  " 
							 + " 	AND encounter_id is null  " 
							 + " 	AND patient_id =:PatientId  "
							 + "	AND status='A'";
					
				}
				else if(crPayble!=null && crPayble!=0)
				{
					
					pharamacyCapUpdateQuery = "UPDATE service.t_vist_contract_cap_mapper SET balance_amount=balance_amount +:crPayble "
							 + " 	WHERE contract_id=:contractId  " 
							 + " 	AND product_category_id=:productCategoryId  " 
							 + " 	AND admission_id is null  " 
							 + " 	AND encounter_id is null  " 
							 + " 	AND patient_id =:PatientId "
							 + "	AND status='A'";
					
				}
		
			}
			sb.append(pharamacyCapUpdateQuery);
			sb.append(consumptionDto.getEncounterId()!=null&&consumptionDto.getEncounterId()>0?" AND encounter_id=:encounterId ":" ");
	    	sb.append(consumptionDto.getAdmissionId()!=null&&consumptionDto.getAdmissionId()>0?" AND admission_id=:admissionId ":" ");


			Session session = sessionFactory.openSession();
			Transaction t = session.beginTransaction();
			//for walkin patient

			Query sqlQuery=	session.createSQLQuery(sb.toString())
			.setParameter("productCategoryId",consumptionDto.getProductCategoryId())
			.setParameter("contractId",consumptionDto.getContractId());

			if(quantity!=null &&quantity!=0)
			{
				sqlQuery.setParameter("quantity",quantity);
			}
			else if(crPayble!=null && crPayble!=0)
			{
				sqlQuery.setParameter("crPayble", (Math.round(crPayble * 100.0)/100.0) );
			}
			if (consumptionDto.getEncounterId() != null && consumptionDto.getEncounterId() > 0) {
				sqlQuery.setParameter("encounterId", consumptionDto.getEncounterId());
			}
			if (consumptionDto.getAdmissionId() != null && consumptionDto.getAdmissionId() > 0) {
				sqlQuery.setParameter("admissionId", consumptionDto.getAdmissionId());
			}

			if(consumptionDto.getPatientId()!=null &&consumptionDto.getPatientId()>0 && consumptionDto.getEncounterId()==null && consumptionDto.getAdmissionId()==null)
			{
				sqlQuery.setParameter("PatientId", consumptionDto.getPatientId());
			}
			result = (Integer)sqlQuery.executeUpdate();

			t.commit();
			session.close();

			return result;
			
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
	
	}

	@Override
	public Integer resetContractCapping(Integer encounterId ,Integer admissionId) throws ServiceException {
		try {
			Integer i = 0 ;
						if(encounterId!=null)
						{
							 i = sessionFactory.getCurrentSession()
									.createSQLQuery( "update service.t_vist_contract_cap_mapper set "
											+ "balance_cap_amount=department_cap_amount, "
											+ "balance_quantity=quantity,"
											+ "balance_amount=amount "
											+ "where encounter_id=:encounterId and status='A'")
									.setParameter("encounterId",encounterId)
									.executeUpdate();
							 
							 
							sessionFactory.getCurrentSession()
									.createSQLQuery(
											" update billing.m_preauth_master "
											+ "set balance_amount=authorized_amount "
											+ "where encounter_id =:encounterId ")
									.setParameter("encounterId",encounterId)
									.executeUpdate();
							}
							if(admissionId!=null)
							{
								i = sessionFactory.getCurrentSession()
										.createSQLQuery( "update service.t_vist_contract_cap_mapper set "
												+ "balance_cap_amount=department_cap_amount, "
												+ "balance_quantity=quantity,"
												+ "balance_amount=amount "
												+ "where admission_id=:admissionId "
												+ "and status='A'")
										.setParameter("admissionId",admissionId)
										.executeUpdate();
								 
								 
								sessionFactory.getCurrentSession()
										.createSQLQuery(
												" update billing.m_preauth_master set balance_amount=authorized_amount "
												+ "where admission_id =:admissionId ")
										.setParameter("admissionId",admissionId)
										.executeUpdate();
								 
							}
			return i;
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
	}
	
	
	
	@Override
	public Integer addCapAmount(ContractConsumptionDto consumptionDto) throws ServiceException {
		try {
			Integer i = 0 ;
			if(consumptionDto.getOrderDetailsPayeeMapperDtosList() !=null && consumptionDto.getOrderDetailsPayeeMapperDtosList().size()>0)
			{
				for(OrderDetailsPayeeMapperDto dto : consumptionDto.getOrderDetailsPayeeMapperDtosList())
				{
					if(dto.getContractId()!=null && dto.getPayeeAmount()!=null &&  dto.getPayeeAmount().doubleValue()>0)
					{
						if(consumptionDto.getEncounterId()!=null)
						{
							 i = sessionFactory.getCurrentSession()
									.createSQLQuery( " update " 
											 + " 	service.t_vist_contract_cap_mapper " 
											 + " set " 
											 + "	balance_cap_amount = "
											 + " 		(case "
											 + " 			when department_cap_amount is not null and (balance_cap_amount + :creaditPayable) > department_cap_amount "
											 + "				then department_cap_amount "
											 + "		 	when  department_cap_amount is not null and (balance_cap_amount + :creaditPayable) <= department_cap_amount"
											 + "				then balance_cap_amount + :creaditPayable "
											 + " 		end), 	"
											 + "	balance_amount = "
											 + "		(case "
											 + "			when amount is not null and (balance_amount + :creaditPayable) > amount "
											 + "				then amount "
											 + "			when amount is not null and (balance_amount + :creaditPayable) <= amount	"
											 + "				then balance_amount + :creaditPayable "
											 + "		end), "
											 + " 	balance_quantity = "
											 + " 		(case "
											 + " 			when quantity is not null and (balance_quantity + :qty) > quantity "
											 + "				then quantity "
											 + " 			when quantity is not null and (balance_quantity + :qty) <= quantity "
											 + "				then balance_quantity + :qty "
											 + " 		end) " 
											 + " where " 
											 + " 	contract_id = :contractId " 
											 + " 	and ((department_id = :specialityId	and balance_amount is null and quantity is null) " 
											 + " 	or (service_id = :serviceId	and balance_amount is not null) " 
											 + " 	or (service_id = :serviceId	and quantity is not null)) " 
											 + " 	and encounter_id =:encounterId " 
											 + " 	and status = 'A' " )
									.setParameter("encounterId",consumptionDto.getEncounterId())
									.setParameter("specialityId",consumptionDto.getSpecialityId())
									.setParameter("contractId",dto.getContractId())
									.setParameter("serviceId",consumptionDto.getServiceId())
									.setDouble("creaditPayable",dto.getPayeeAmount().doubleValue())
									.setDouble("qty", consumptionDto.getQuantity()<0 ? (consumptionDto.getQuantity()*(-1)) : consumptionDto.getQuantity())
									.executeUpdate();
							 
							 /*i = sessionFactory.getCurrentSession()
										.createSQLQuery( "UPDATE service.t_vist_contract_cap_mapper SET balance_amount=balance_amount + :creaditPayable " 
												 + " 	WHERE contract_id=:contractId  " 
												 + " 	AND service_id=:serviceId  " 
												 + " 	AND encounter_id=:encounterId"
												 + "	AND status='A' "
												 + "	AND balance_amount is not null ")
										.setParameter("encounterId",consumptionDto.getEncounterId())
										.setParameter("serviceId",consumptionDto.getServiceId())
										.setParameter("contractId",dto.getContractId())
										.setDouble("creaditPayable",dto.getPayeeAmount().doubleValue()).executeUpdate();*/
							 
							 /*i = sessionFactory.getCurrentSession()
										.createSQLQuery( "UPDATE service.t_vist_contract_cap_mapper SET balance_quantity=balance_quantity + 1 " 
												 + " 	WHERE contract_id=:contractId  " 
												 + " 	AND service_id=:serviceId  " 
												 + " 	AND encounter_id=:encounterId"
												 + "	AND status='A' "
												 + "	AND quantity is not null ")
										.setParameter("encounterId",consumptionDto.getEncounterId())
										.setParameter("serviceId",consumptionDto.getServiceId())
										.setParameter("contractId",dto.getContractId())
										.executeUpdate();*/
							 if(consumptionDto.isApplyAuth())
							 {
								 sessionFactory.getCurrentSession()
									.createSQLQuery(
											" update billing.m_preauth_master set balance_amount=balance_amount + :payeeAmount "
											+ "where payee_id=:payeeId "
											+ "and encounter_id =:encounterId "
											+ "and contract_id=:contractId ")
									.setParameter("encounterId",consumptionDto.getEncounterId())
									.setParameter("contractId",dto.getContractId())
									.setParameter("payeeAmount",dto.getPayeeAmount() !=null ? dto.getPayeeAmount().doubleValue() : 0.00)
									.setParameter("payeeId",dto.getPayeeId())
									.executeUpdate();
							 }
									
							
							}
							if(consumptionDto.getAdmissionId()!=null)
							{
								 i = sessionFactory.getCurrentSession()
										.createSQLQuery( " update " 
												 + " 	service.t_vist_contract_cap_mapper set " 
												 + "	balance_cap_amount = "
												 + " 		(case "
												 + " 			when department_cap_amount is not null then balance_cap_amount + :creaditPayable "
												 + " 		end), 	"
												 + "	balance_amount = "
												 + "		(case "
												 + "			when amount is not null then balance_amount + :creaditPayable "
												 + "		end), "
												 + " 	balance_quantity = "
												 + " 		(case "
												 + " 			when quantity is not null and (balance_quantity + :qty) >= quantity then "
												 + "				quantity "
												 + " 			else "
												 + "				balance_quantity + :qty "
												 + " 		end) "
												 + " where " 
												 + " 	contract_id =:contractId " 
												 + " 	and ((department_id =:specialityId and balance_amount is null and quantity is null) " 
												 + " 	or (service_id =:serviceId	and balance_amount is not null) " 
												 + " 	or (service_id =:serviceId 	and quantity is not null)) " 
												 + " 	and admission_id =:admissionId " 
												 + " 	and status = 'A' ")
										.setParameter("admissionId",consumptionDto.getAdmissionId())
										.setParameter("specialityId",consumptionDto.getSpecialityId())
										.setParameter("serviceId",consumptionDto.getServiceId())
										.setParameter("contractId",dto.getContractId())
										.setDouble("creaditPayable",dto.getPayeeAmount().doubleValue())
										.setDouble("qty", consumptionDto.getQuantity()<0 ? (consumptionDto.getQuantity()*(-1)) : consumptionDto.getQuantity())
										.executeUpdate();
								 
								/* i = sessionFactory.getCurrentSession()
											.createSQLQuery( "UPDATE service.t_vist_contract_cap_mapper SET balance_amount=balance_amount + :creaditPayable " 
													 + " 	WHERE contract_id=:contractId  " 
													 + " 	AND service_id=:serviceId  " 
													 + " 	AND admission_id=:admissionId"
													 + "	AND status='A' "
													 + "	AND balance_amount is not null ")
											.setParameter("admissionId",consumptionDto.getAdmissionId())
											.setParameter("serviceId",consumptionDto.getServiceId())
											.setParameter("contractId",dto.getContractId())
											.setDouble("creaditPayable",dto.getPayeeAmount().doubleValue()).executeUpdate();*/
								 
								 /*i = sessionFactory.getCurrentSession()
											.createSQLQuery( "UPDATE service.t_vist_contract_cap_mapper SET balance_quantity=balance_quantity + 1 " 
													 + " 	WHERE contract_id=:contractId  " 
													 + " 	AND service_id=:serviceId  " 
													 + " 	AND admission_id=:admissionId"
													 + "	AND status='A' "
													 + "	AND quantity is not null ")
											.setParameter("admissionId",consumptionDto.getAdmissionId())
											.setParameter("serviceId",consumptionDto.getServiceId())
											.setParameter("contractId",dto.getContractId())
											.executeUpdate();
								 */
								 if(consumptionDto.isApplyAuth())
								 {
							 		sessionFactory.getCurrentSession()
										.createSQLQuery(
												" update billing.m_preauth_master set balance_amount=balance_amount + :amount "
												+ "where payee_id=:payeeId "
												+ "and admission_id =:admissionId "
												+ "and contract_id=:contractId ")
										.setDouble("amount", dto.getPayeeAmount() !=null ? dto.getPayeeAmount().doubleValue() : 0.00)
										.setParameter("admissionId",consumptionDto.getAdmissionId())
										.setParameter("contractId",dto.getContractId())
										.setParameter("payeeId",dto.getPayeeId())
										.executeUpdate();
								 }	 	
								 
							}
						}
					}
			}
			return i;
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
	}
	
	@Override
	public Integer addCapAmountPharmacy(CommonBillingItemsDto itemRequest) 
	{
		try {
		Integer i = 0 ;
		if(itemRequest.getSaleDetailPayeeMapperDtoList() !=null && itemRequest.getSaleDetailPayeeMapperDtoList().size()>0)
		{
			for (SaleDetailPayeeMapperDto dto : itemRequest.getSaleDetailPayeeMapperDtoList())
			{
				if(dto.getContractId()!=null && dto.getContractId()>0)
				{
					String pharamacyCapUpdateQuery = null;
					String preAuthUpdateQuery = null;
					StringBuilder sb = new StringBuilder();
					StringBuilder preAuthQuery = new StringBuilder();
					Double crPayble  = dto.getPayeeAmount().doubleValue();
						
				    if(crPayble!=null && crPayble!=0)
					{
						pharamacyCapUpdateQuery = "UPDATE service.t_vist_contract_cap_mapper SET balance_amount=balance_amount +"+crPayble 
								 + " 	WHERE contract_id=:contractId  " 
								 + " 	AND product_category_id=:productCategoryId  " 
								 + "	AND status='A'" ;
						preAuthUpdateQuery = " update billing.m_preauth_master set balance_amount=balance_amount + :amount "
								+ "where payee_id=:payeeId "
								+ "and contract_id=:contractId ";
						
					}
					if(itemRequest.getPatientId()!=null &&itemRequest.getPatientId()>0 && itemRequest.getEncounterId()==null && itemRequest.getAdmissionId()==null) 
					{
					    if(crPayble!=null && crPayble!=0)
						{
							pharamacyCapUpdateQuery = "UPDATE service.t_vist_contract_cap_mapper SET balance_amount=balance_amount +"+crPayble 
									 + " 	WHERE contract_id=:contractId  " 
									 + " 	AND product_category_id=:productCategoryId  " 
									 + " 	AND admission_id is null  " 
									 + " 	AND encounter_id is null  " 
									 + " 	AND patient_id =  "+itemRequest.getPatientId() 
									 + "	AND status='A'";
							
							preAuthUpdateQuery = " update billing.m_preauth_master set balance_amount=balance_amount + :amount "
									+ " where payee_id=:payeeId "
									+ " and contract_id=:contractId "
									+ " AND admission_id is null  " 
									+ " AND encounter_id is null  " 
									+ " and patient_id =  "+itemRequest.getPatientId();
						}
					}
					sb.append(pharamacyCapUpdateQuery);
					sb.append(itemRequest.getEncounterId()!=null&&itemRequest.getEncounterId()>0?" AND encounter_id="+itemRequest.getEncounterId():" ");
			    	sb.append(itemRequest.getAdmissionId()!=null&&itemRequest.getAdmissionId()>0?" AND admission_id="+itemRequest.getAdmissionId():" ");
			    	
			    	preAuthQuery.append(preAuthUpdateQuery);
			    	preAuthQuery.append(itemRequest.getEncounterId()!=null&&itemRequest.getEncounterId()>0?" AND encounter_id="+itemRequest.getEncounterId():" ");
			    	preAuthQuery.append(itemRequest.getAdmissionId()!=null&&itemRequest.getAdmissionId()>0?" AND admission_id="+itemRequest.getAdmissionId():" ");
					
						 Session session = sessionFactory.openSession();
						 Transaction t = session.beginTransaction();
						//for walkin patient
						 i = (Integer)session.createSQLQuery(sb.toString())
											 .setParameter("productCategoryId",dto.getProductCategoryId() != null ? dto.getProductCategoryId() : itemRequest.getProductCategoryId())
											 .setParameter("contractId",dto.getContractId())
											 .executeUpdate();
						 
						 if(!itemRequest.isAddPreAuth())
						 {
							 session.createSQLQuery(preAuthQuery.toString())
								.setDouble("amount", dto.getPayeeAmount() !=null ? dto.getPayeeAmount().doubleValue() : 0.00)
								.setParameter("contractId",dto.getContractId())
								.setParameter("payeeId",dto.getPayeeId())
								.executeUpdate();
						 }
						t.commit();
						session.close();
				}
			}
		}
		 return i;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}

	
	@Override
	public Integer consumeCapAmountPharmacy(CommonBillingItemsDto itemRequest) {
		try {
			Integer i = 0 ;
			if(itemRequest.getSaleDetailPayeeMapperDtoList() !=null && itemRequest.getSaleDetailPayeeMapperDtoList().size()>0)
			{
				for (SaleDetailPayeeMapperDto dto : itemRequest.getSaleDetailPayeeMapperDtoList())
				{
					if(dto.getContractId()!=null && dto.getContractId()>0)
					{
						String pharamacyCapUpdateQuery = null;
						String preAuthUpdateQuery = null;
						StringBuilder sb = new StringBuilder();
						StringBuilder preAuth = new StringBuilder();
						Double crPayble  = dto.getPayeeAmount().doubleValue()*-1;
							
					    if(crPayble!=null && crPayble!=0)
						{
							pharamacyCapUpdateQuery = "UPDATE service.t_vist_contract_cap_mapper SET balance_amount=balance_amount +"+crPayble 
									 + " 	WHERE contract_id=:contractId  " 
									 + " 	AND product_category_id=:productCategoryId  " 
									 + "	AND status='A'" ;
							
							sb.append(pharamacyCapUpdateQuery);
							sb.append(itemRequest.getEncounterId()!=null&&itemRequest.getEncounterId()>0?" AND encounter_id="+itemRequest.getEncounterId():" ");
					    	sb.append(itemRequest.getAdmissionId()!=null&&itemRequest.getAdmissionId()>0?" AND admission_id="+itemRequest.getAdmissionId():" ");
							
				    		preAuthUpdateQuery = " update billing.m_preauth_master set balance_amount=balance_amount + :amount "
									+ "where payee_id=:payeeId "
									+ "and contract_id=:contractId ";
					    	
					    	preAuth.append(preAuthUpdateQuery);
					    	preAuth.append(itemRequest.getEncounterId()!=null&&itemRequest.getEncounterId()>0?" AND encounter_id="+itemRequest.getEncounterId():" ");
					    	preAuth.append(itemRequest.getAdmissionId()!=null&&itemRequest.getAdmissionId()>0?" AND admission_id="+itemRequest.getAdmissionId():" ");
							
							
					    	Session session = sessionFactory.openSession();
							Transaction t = session.beginTransaction();
								i = (Integer)session.createSQLQuery(sb.toString())
													 .setParameter("productCategoryId",itemRequest.getProductCategoryId())
													 .setParameter("contractId",dto.getContractId())
													 .executeUpdate();
							 session.createSQLQuery(preAuth.toString())
									.setDouble("amount", crPayble)
									.setParameter("contractId",dto.getContractId())
									.setParameter("payeeId",dto.getPayeeId())
									.executeUpdate();
							
							 t.commit();
							session.close();
						}
					    
						if(itemRequest.getPatientId()!=null &&itemRequest.getPatientId()>0 && itemRequest.getEncounterId()==null && itemRequest.getAdmissionId()==null) 
						{
						    if(crPayble!=null && crPayble!=0)
							{
								pharamacyCapUpdateQuery = "UPDATE service.t_vist_contract_cap_mapper SET balance_amount=balance_amount +"+crPayble 
										 + " 	WHERE contract_id=:contractId  " 
										 + " 	AND product_category_id=:productCategoryId  " 
										 + " 	AND admission_id is null  " 
										 + " 	AND encounter_id is null  " 
										 + " 	AND patient_id =  "+itemRequest.getPatientId() 
										 + "	AND status='A'";
								
								preAuthUpdateQuery = " update billing.m_preauth_master set balance_amount=balance_amount + :amount "
										+ " where payee_id=:payeeId "
										+ " and contract_id=:contractId "
										+ " AND admission_id is null  " 
										+ " AND encounter_id is null  " 
										+ " and patient_id =  "+itemRequest.getPatientId();
								
								Session session = sessionFactory.openSession();
								 Transaction t = session.beginTransaction();
								//for walkin patient
								 i = (Integer)session.createSQLQuery(sb.toString())
													 .setParameter("productCategoryId",itemRequest.getProductCategoryId())
													 .setParameter("contractId",dto.getContractId())
													 .executeUpdate();
								
								 session.createSQLQuery(preAuth.toString())
									.setDouble("amount", crPayble)
									.setParameter("contractId",dto.getContractId())
									.setParameter("payeeId",dto.getPayeeId())
									.executeUpdate();
								 
								 
								t.commit();
								session.close();
								
							}
						}
							 
					}
				}
			}
			 return i;
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
	}

	
	@Override
	public ContractConsumptionDto getVertualServiceDetailsByContractId(ContractConsumptionDto contractConsumptionDto)throws ServiceException {
		try {
			
			ContractConsumptionDto consumptionDto =new ContractConsumptionDto();
				String query=" SELECT " 
						 + " csd.contract_id as \"contractId\", " 
						 + " csd.final_price as \"serviceRate\", "
						 + " service.service_standard_name as \"serviceName\", " 
						 + " sm.speciality_name as \"specialityName\", "
						 + " ccm.co_share_percentage as \"coShare\", "
						 + " ccm.company_id as \"payeeId\", "
						 + " unitservice.gst_type_id as \"taxId\", "
						 + " tax.tax_percentage as \"taxPercentage\", "
						 + " (csd.final_price+(csd.final_price * (tax.tax_percentage/100))) as \"finalPrice\", " 
						 + " (select ccd.sub_department_cap_amount "
						 + " FROM service.t_contract_cap_details ccd  "
							 	+ " WHERE ccd.department_id=service.speciality_id  "
							 	+ " AND ccd.contract_id=csd.contract_id  "
							 	+ " AND ccd.sub_department_id=service.sub_speciality_id  "
								+ " AND ccd.status='A')  " 
						 + " as \"balanceCapAmount\", "
						 + " service.sub_speciality_id as \"subSpecialityId\", "
						 + " (select percentage FROM billing.m_patient_type_service_wise_details WHERE patient_type_id = "+contractConsumptionDto.getPatientClassId()+" and service_id =csd.service_id) as \"patientClassPercentage\", " 
				         + "(select markupStatus FROM billing.m_patient_type_service_wise_details WHERE patient_type_id = "+contractConsumptionDto.getPatientClassId()+" and service_id =csd.service_id) as \"markupStatus\" "
						 + " FROM service.t_contract_service_details csd  " 
						 + " INNER JOIN service.m_company_contract_master ccm on csd.contract_id = ccm.contract_id  " 
						 + " INNER JOIN service.m_service_master service on service.service_master_id=csd.service_id	"
						 + " INNER JOIN doctor.m_speciality_master sm on service.speciality_id = sm.speciality_id "
						 + " INNER JOIN service.t_unit_service_master_details unitservice on service.service_master_id = unitservice.service_master_id "
						 + " LEFT JOIN m_tax tax on tax.id = unitservice.gst_type_id and tax.unit_id=ccm.unit_id " 
						 + " WHERE csd.contract_id=:contractId  " 
						 + " AND csd.service_id=:serviceId "
						 + " AND unitservice.unit_id = ccm.unit_id " 
					 + " AND '"+contractConsumptionDto.getCurrentDate().substring(0,10)+"' between ccm.validity_from AND ccm.validity_to ";
				
				consumptionDto = (ContractConsumptionDto)sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setParameter("encounterId",contractConsumptionDto.getEncounterId())
						.setParameter("contractId",contractConsumptionDto.getContractId())
						.setParameter("serviceId",contractConsumptionDto.getServiceId())
						.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
						.uniqueResult();
			
			return consumptionDto;
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public TaxDto getTaxPerByServiceId(Integer serviceId, Integer unitId) {
		try {
			
			SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery("SELECT tax.id as \"id\"," 
					+	"tax.tax_percentage as \"taxPercentage\" "
					+	"FROM public.m_tax tax "
					+	"INNER JOIN service.t_unit_service_master_details usmd on usmd.gst_type_id = tax.id "
					+	"WHERE usmd.service_master_id = "+serviceId+" "
					+ 	"AND tax.unit_id = usmd.unit_id "
	
			);
			
			TaxDto tax = (TaxDto)query
				.setResultTransformer(Transformers.aliasToBean(TaxDto.class)).uniqueResult();

			return tax;

		} catch (HibernateException he) {
			logger.error("getTaxPerByServiceId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getTaxPerByServiceId failed", e);
			throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response getActivePaymentEntitlement(ContractConsumptionDto consumptionDto) throws ApplicationException {
		try {

			List<ContractConsumptionDto> list =  new ArrayList<ContractConsumptionDto>();
			if(consumptionDto.getEncounterId()!= null && consumptionDto.getEncounterId() >0)
			{
				list = sessionFactory.getCurrentSession()
							.createSQLQuery("select "
									+ "	tpsd.patient_sponsor_details_id as \"patientSponsorDetailsId\", "
									+ "	tpsd.contract_id as \"contractId\", "
									+ "	tpsd.payee_id as \"companyId\", "
									+ "	tpsd.admission_id as \"admissionId\", "
									+ "	tpsd.encounter_id as \"encounterId\", "
									+ "	encmst.patient_id as \"patientId\", "
									+ " 1 as \"visitTypeId\", "
									+ "	tpsd.payment_entitlement_id as \"paymentEntitlementId\", "
									+ "	cm.company_desc as \"companyName\", "
									+ "	ccm.is_smart_applicable as \"isSmartApplicable\", "
									+ "	ccm.contract_name as \"contractName\", "
									+ "	ti.insurance_desc as \"insuranceName\", "
									+ "	tpsd.insurance_number as \"insuranceNumber\", "
									+ "	tpsd.insured_amount as \"insuredAmount\", "
									+ "	to_char(tpsd.valid_from, 'DD-MM-YYYY') as \"startDate\", "
									+ "	to_char(tpsd.valid_to, 'DD-MM-YYYY') as \"endDate\", "
									+ "	tpsd.priority_id as \"priority\", "
									+ "	pem.payment_entitlement_desc as \"paymentEntitlementName\", "
									+ "	tpsd.is_contract_changed as \"isContractChanged\", "
									+ "	tpsd.is_payee_updated as \"isPayeeUpdated\", "
									+ " tpsd.organization_id as \"organizationId\", "
									+ " tpsd.unit_id as \"unitId\" ,"
									+ " tpsd.status as \"status\" ,"
									+ " coalesce(ccm.is_tariff_rate_applicable,'Y') as \"isTariffRateApplicable\" "
									+ "from "
									+ "	opd.t_patient_sponsor_details tpsd "
									+ "inner join public.m_payment_entitlement_master pem on "
									+ "	tpsd.payment_entitlement_id = pem.payment_entitlement_id "
									+ "inner join opd.t_encounter_master encmst on "
									+ "	tpsd.encounter_id = encmst.encounter_id "
									+ "left join service.m_company_contract_master ccm on "
									+ "	tpsd.contract_id = ccm.contract_id "
									+ "inner join public.t_company_master cm on "
									+ "	tpsd.payee_id = cm.company_id "
									+ "left join adt.t_insurance ti on "
									+ "	tpsd.insurance_id = ti.insurance_id "
									+ "where "
									+ "	tpsd.encounter_id = :encounterId "
									+ "	and tpsd.status = 'A' "
									+ "order by "
									+ "	tpsd.priority_id ")
							.setParameter("encounterId",consumptionDto.getEncounterId())
							.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
							.list();
			}
			if(consumptionDto.getAdmissionId()!=null && consumptionDto.getAdmissionId() >0)
			{
				list = sessionFactory.getCurrentSession()
							.createSQLQuery("select "
									+ "	tpsd.patient_sponsor_details_id as \"patientSponsorDetailsId\", "
									+ "	tpsd.contract_id as \"contractId\", "
									+ "	tpsd.payee_id as \"companyId\", "
									+ "	tpsd.admission_id as \"admissionId\", "
									+ "	tpsd.encounter_id as \"encounterId\", "
									+ "	adm.patient_id as \"patientId\", "
									+ "	adm.visit_type_id as \"visitTypeId\" ,"
									+ "	tpsd.payment_entitlement_id as \"paymentEntitlementId\", "
									+ "	cm.company_desc as \"companyName\", "
									+ "	ccm.is_smart_applicable as \"isSmartApplicable\", "
									+ "	ccm.contract_name as \"contractName\", "
									+ "	ti.insurance_desc as \"insuranceName\", "
									+ "	tpsd.insurance_number as \"insuranceNumber\", "
									+ "	tpsd.insured_amount as \"insuredAmount\", "
									+ "	to_char(tpsd.valid_from, 'DD-MM-YYYY') as \"startDate\", "
									+ "	to_char(tpsd.valid_to, 'DD-MM-YYYY') as \"endDate\", "
									+ "	tpsd.priority_id as \"priority\", "
									+ "	pem.payment_entitlement_desc as \"paymentEntitlementName\", "
									+ "	tpsd.is_contract_changed as \"isContractChanged\", "
									+ "	tpsd.is_payee_updated as \"isPayeeUpdated\", "
									+ " tpsd.organization_id as \"organizationId\", "
									+ " tpsd.unit_id as \"unitId\", "
									+ " tpsd.status as \"status\" ,"
									+ " coalesce(ccm.is_tariff_rate_applicable,'Y') as \"isTariffRateApplicable\" "
									+ "from "
									+ "	opd.t_patient_sponsor_details tpsd "
									+ "inner join public.m_payment_entitlement_master pem on "
									+ "	tpsd.payment_entitlement_id = pem.payment_entitlement_id "
									+ "inner join adt.t_admission adm on "
									+ "	tpsd.admission_id = adm.admission_id "
									+ "left join service.m_company_contract_master ccm on "
									+ "	tpsd.contract_id = ccm.contract_id "
									+ "inner join public.t_company_master cm on "
									+ "	tpsd.payee_id = cm.company_id "
									+ "left join adt.t_insurance ti on "
									+ "	tpsd.insurance_id = ti.insurance_id "
									+ "where "
									+ "	tpsd.admission_id =:admissionId "
									+ "	and tpsd.status = 'A' "
									+ "order by "
									+ "	tpsd.priority_id" )
							.setParameter("admissionId",consumptionDto.getAdmissionId())
							.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
							.list();
			}
			if(consumptionDto.getPatientId()!=null && (consumptionDto.getEncounterId()==null||consumptionDto.getEncounterId()==0) && (consumptionDto.getAdmissionId()==null||consumptionDto.getAdmissionId()==0)){
				list = sessionFactory.getCurrentSession()
						.createSQLQuery("select "
							+ " tpsd.patient_sponsor_details_id as \"patientSponsorDetailsId\","
							+" 	tpsd.contract_id as \"contractId\", "
							+ "	tpsd.payee_id as \"companyId\", "
							+" 	tpsd.admission_id as \"admissionId\", "
							+" 	tpsd.encounter_id as \"encounterId\", "
							+"  patient.patient_id as \"patientId\","
							+"  1 as \"visitTypeId\","
							+" 	tpsd.payment_entitlement_id as \"paymentEntitlementId\", "
							+" 	cm.company_desc as \"companyName\", "
							+" 	ccm.contract_name as \"contractName\", "
							+" 	ccm.is_smart_applicable as \"isSmartApplicable\", "
							+" 	ti.insurance_desc as \"insuranceName\", "
							+" 	tpsd.insurance_number as \"insuranceNumber\", "
							+" 	tpsd.insured_amount as \"insuredAmount\", "
							+" 	to_char(tpsd.valid_from,'DD-MM-YYYY') as \"startDate\", "
							+" 	to_char(tpsd.valid_to,'DD-MM-YYYY') as \"endDate\", "
							+" 	tpsd.priority_id as \"priority\", "
							+" 	pem.payment_entitlement_desc as \"paymentEntitlementName\", "
							+" 	tpsd.is_contract_changed as \"isContractChanged\", "
							+" 	tpsd.is_payee_updated as \"isPayeeUpdated\", "
							+ " tpsd.organization_id as \"organizationId\", "
							+ " tpsd.unit_id as \"unitId\", "
							+ " tpsd.status as \"status\" ,"
							+ " coalesce(ccm.is_tariff_rate_applicable,'Y') as \"isTariffRateApplicable\" "
							+" from "
							+" 	opd.t_patient_sponsor_details tpsd "
							+" inner join public.m_payment_entitlement_master pem on "
							+" 	tpsd.payment_entitlement_id = pem.payment_entitlement_id "
							+" left join patient.t_patient_registration patient on "
							+" tpsd.patient_id = patient.patient_id and patient.registration_type_id <> 7 " 
							+" left join service.m_company_contract_master ccm on "
							+" 	tpsd.contract_id = ccm.contract_id "
							+" inner join public.t_company_master cm on "
							+" 	tpsd.payee_id = cm.company_id "
							+" left join adt.t_insurance ti on "
							+" 	tpsd.insurance_id = ti.insurance_id	 "
							+" where "
//							+" 	 tpsd.payment_entitlement_id !=1 and "
							+ "    tpsd.patient_id =:patientId "
							+" 	 and  tpsd.status = 'A' "
							+" 	 and  tpsd.admission_id is null "
							+" 	 and  tpsd.encounter_id is null "
//							+" 	 and  tpsd.priority_id!=1 "
							+ " order by  tpsd.priority_id ")
						.setParameter("patientId",consumptionDto.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
						.list();
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list,null);
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
	}
	@Override
	public Response isConsultationAmountPaidBySelf(ContractConsumptionDto dto) throws ApplicationException {
		try {

			BigInteger isConsultationSelfPaid = BigInteger.ZERO;
			if(dto.getEncounterId()!= null && dto.getEncounterId() >0)
			{
				isConsultationSelfPaid = (BigInteger) sessionFactory.getCurrentSession()
						.createSQLQuery("select "
								+ " (case when config.self_paid_consult_alert=true then count(tod.order_details_id) else 0 end) "
								+ "from "
								+ "	public.t_order_master tom "
								+ "inner join public.t_order_details tod on "
								+ "	tod.order_id = tom.order_id "
								+ "	and tod.is_consultation_service = 'Y' "
								+ "inner join opd.t_patient_sponsor_details tpsd on "
								+ "     tpsd.payment_entitlement_id <> 1 "
								+ "     and tpsd.encounter_id = tom.encounter_id "
								+ "   inner join public.t_unit_application_billing_configuration config " +
								"     on config.unit_id  = tom.org_unit_id "
								+ "where "
								+ "	tom.encounter_id = :encounterId "
								+ "	and tod.net_amount  = tod.self_payable "
								+"  and tpsd.status ='A' "
								+ "	and tod.status ='A' group by config.self_paid_consult_alert")
						.setParameter("encounterId",dto.getEncounterId())
						.uniqueResult();
			}
			if(dto.getAdmissionId()!=null && dto.getAdmissionId() >0)
			{
				isConsultationSelfPaid = (BigInteger)sessionFactory.getCurrentSession()
						.createSQLQuery("select "
								+ "	(case when config.self_paid_consult_alert=true then count(tod.order_details_id) else 0 end)  "
								+ "from "
								+ "	public.t_order_master tom "
								+ "inner join public.t_order_details tod on "
								+ "	tod.order_id = tom.order_id "
								+ "	and tod.is_consultation_service = 'Y' "
								+ "inner join opd.t_patient_sponsor_details tpsd on "
								+ "     tpsd.payment_entitlement_id <> 1 "
								+ "     and tpsd.admission_id = tom.admission_id "
								+ "   inner join public.t_unit_application_billing_configuration config " +
								"     on config.unit_id  = tom.org_unit_id "
								+ "where "
								+ "	tom.admission_id = :admissionId "
								+ "	and tod.net_amount  = tod.self_payable "
								+"  and tpsd.status ='A' "
								+ "	and tod.status ='A' group by config.self_paid_consult_alert")
						.setParameter("admissionId",dto.getAdmissionId())
						.uniqueResult();
			}
			isConsultationSelfPaid = isConsultationSelfPaid!=null?isConsultationSelfPaid : BigInteger.ZERO;
          return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null,isConsultationSelfPaid);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}

	}


	@Override
	public List<ContractConsumptionDto> getServiceCapDetailsByContractId(ContractConsumptionDto contractConsumptionDto) {
		try {
			
			List<ContractConsumptionDto> consumptionDto = new ArrayList<ContractConsumptionDto>();
			if(contractConsumptionDto.getAdmissionId() !=null && contractConsumptionDto.getAdmissionId()>0){
				
			String query="  SELECT  "
			 + "  distinct(csd.service_id) as \"serviceId\",  "
			 + "  coalesce(cast(unitservice.rate_editable_max_value as double precision),0) as \"maxRateEditable\",  "
			 + "  coalesce(cast(unitservice.rate_editble_min_value as double precision),0) as \"minRateEditable\",  "
			 + "  csd.contract_id as \"contractId\",  "
			 + "  ccm.company_id as \"payeeId\",  "
			 + "  unitservice.gst_type_id as \"taxId\",  "
			 + "  tax.tax_percentage as \"taxPercentage\",  "
			 + "  csd.balance_amount as \"balanceAmount\",  "
			 + "  csd.balance_quantity as \"balanceQuantity\",  "
			 + "  service.sub_speciality_id as \"subSpecialityId\", "
			 + "  ccm.tariff_id as \"tariffId\", "
			 + "  cast(DATE_PART('day', now() - adm.doa) as int) as \"dayDiff\", "
			 + " (select percentage FROM billing.m_patient_type_service_wise_details WHERE patient_type_id = "+contractConsumptionDto.getPatientClassId()+" and service_id =csd.service_id) as \"patientClassPercentage\", " 
	         + " (select markupStatus FROM billing.m_patient_type_service_wise_details WHERE patient_type_id = "+contractConsumptionDto.getPatientClassId()+" and service_id =csd.service_id) as \"markupStatus\" "
			 + "  FROM service.t_vist_contract_cap_mapper csd   "
			 + "  INNER JOIN adt.t_admission_details adm on adm.admission_id = csd.admission_id and adm.status='A' "
			 + "  INNER JOIN service.m_company_contract_master ccm on csd.contract_id = ccm.contract_id   "
			 + "  INNER JOIN service.m_service_master service on service.service_master_id=csd.service_id	 "
			 + "  INNER JOIN service.t_unit_service_master_details unitservice on service.service_master_id = unitservice.service_master_id  "
			 + "  LEFT JOIN m_tax tax on tax.id = unitservice.gst_type_id and tax.unit_id=ccm.unit_id  "
			 + "  WHERE csd.contract_id=:contractId   "
			 + "  AND csd.service_id=:serviceId  "
			 + "  and csd.admission_id=:admisisonId "
			 + "  AND unitservice.unit_id = ccm.unit_id  "
			 + "  AND '"+contractConsumptionDto.getCurrentDate().substring(0,10)+"' between ccm.validity_from AND ccm.validity_to  "
			 + "  order by 1 desc  limit 1 ";
				
				consumptionDto = sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setParameter("admissionId",contractConsumptionDto.getAdmissionId())
						.setParameter("contractId",contractConsumptionDto.getContractId())
						.setParameter("serviceId",contractConsumptionDto.getServiceId())
						.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
						.list();
			
			}
			
			if(contractConsumptionDto.getEncounterId() != null  && contractConsumptionDto.getEncounterId()>0 ){
			
				String query="  SELECT  "
						 + "  distinct(csd.service_id) as \"serviceId\",  "
						 + "  coalesce(cast(unitservice.rate_editable_max_value as double precision),0) as \"maxRateEditable\",  "
						 + "  coalesce(cast(unitservice.rate_editble_min_value as double precision),0) as \"minRateEditable\",  "
						 + "  csd.contract_id as \"contractId\",  "
						 + "  ccm.company_id as \"payeeId\",  "
						 + "  unitservice.gst_type_id as \"taxId\",  "
						 + "  tax.tax_percentage as \"taxPercentage\",  "
						 + "  csd.balance_amount as \"balanceAmount\",  "
						 + "  csd.balance_quantity as \"balanceQuantity\",  "
						 + "  service.sub_speciality_id as \"subSpecialityId\", "
						 + "  ccm.tariff_id as \"tariffId\", "
						 + "  1 as \"dayDiff\", "
						 + " (select percentage FROM billing.m_patient_type_service_wise_details WHERE patient_type_id = "+contractConsumptionDto.getPatientClassId()+" and service_id =csd.service_id) as \"patientClassPercentage\", " 
				         + " (select markupStatus FROM billing.m_patient_type_service_wise_details WHERE patient_type_id = "+contractConsumptionDto.getPatientClassId()+" and service_id =csd.service_id) as \"markupStatus\" "
						 + "  FROM service.t_vist_contract_cap_mapper csd   "
						 + "  INNER JOIN service.m_company_contract_master ccm on csd.contract_id = ccm.contract_id   "
						 + "  INNER JOIN service.m_service_master service on service.service_master_id=csd.service_id	 "
						 + "  INNER JOIN service.t_unit_service_master_details unitservice on service.service_master_id = unitservice.service_master_id  "
						 + "  LEFT JOIN m_tax tax on tax.id = unitservice.gst_type_id and tax.unit_id=ccm.unit_id  "
						 + "  WHERE csd.contract_id=:contractId   "
						 + "  AND csd.service_id=:serviceId  "
						 + "  and csd.encounter_id=:encounterId "
						 + "  AND unitservice.unit_id = ccm.unit_id  "
						 + "  AND '"+contractConsumptionDto.getCurrentDate().substring(0,10)+"' between ccm.validity_from AND ccm.validity_to  "
						 + "  order by 1 desc  limit 1 ";
				
				consumptionDto = sessionFactory.getCurrentSession()
						.createSQLQuery(query)
						.setParameter("encounterId",contractConsumptionDto.getEncounterId())
						.setParameter("contractId",contractConsumptionDto.getContractId())
						.setParameter("serviceId",contractConsumptionDto.getServiceId())
						.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
						.list();
			}
			
			return consumptionDto;
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Integer getFallBacktariff(Integer encounterId,Integer admissionId) {
		try{
			String query = null;
			if(encounterId!=null && admissionId==null)
				query="select tariff_id as \"tariffId\" from service.m_company_contract_master where is_tariff_rate_applicable = 'Y' and contract_id = (select contract_id from opd.t_patient_sponsor_details where encounter_id="+encounterId+" and priority_id=1 and status='A') ";
			
			if(encounterId==null && admissionId!=null)
				query="select tariff_id as \"tariffId\" from service.m_company_contract_master where is_tariff_rate_applicable = 'Y' and contract_id = (select contract_id from opd.t_patient_sponsor_details where admission_id="+admissionId+" and priority_id=1 and status='A') ";
			
			
			ContractConsumptionDto consumptionDto = (ContractConsumptionDto)sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
					.uniqueResult();
			
			return consumptionDto==null?null:consumptionDto.getTariffId();
		}catch(Exception e){
			logger.error("getFallBacktariff failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public ContractConsumptionDto getProcedureDetailsByContract(ContractConsumptionDto procedureForContract,boolean isProcedure)
			throws ServiceException {
		try {
			
			Date currentDate  =  TimeZoneComponent.getDateByzone(procedureForContract.getUnitId());
			String pharmacyContractQuery = null;
			ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
			   if(procedureForContract.getAdmissionId()!=null && procedureForContract.getAdmissionId()>0)
					{
				      
				   if(isProcedure)
						 pharmacyContractQuery ="select "
								 + "	( "
								 + "		select balance_amount "
								 + "	from "
								 + "		service.t_vist_contract_cap_mapper "
								 + "	where "
								 + "		service_id =:serviceId "
								 + "		and contract_id =:contractId "
								 + "		and balance_amount > 0 "
								 + "		and admission_id = :admissionId) as \"balanceAmount\", "
								 + "	( "
								 + "		select company_id "
								 + "	from "
								 + "		service.m_company_contract_master "
								 + "	where "
								 + "		 contract_id =:contractId "
								 + "		) as \"payeeId\", "
								 + "	"
								 + "from "
								 + "   service.m_company_contract_master ccmst "
								 + "	inner join service.t_contract_procedure_details cpd "
								 + "	on ccmst.contract_id = cpd.contract_id "
								 + "where "
								 + "	cpd.procedure_id =:serviceId "
								 + "	and ccmst.contract_id =:contractId "
								 + "	AND '"+GlobalCommonDateUtils.getStringDate(currentDate, "yyyy-MM-dd")+"' between ccmst.validity_from AND ccmst.validity_to";
				   
				   else
						 pharmacyContractQuery ="select "
								 + "	( "
								 + "		select balance_amount "
								 + "	from "
								 + "		service.t_vist_contract_cap_mapper "
								 + "	where "
								 + "		service_id =:serviceId "
								 + "		and contract_id =:contractId "
								 + "		and balance_amount > 0 "
								 + "		and admission_id = :admissionId) as \"balanceAmount\", "
								 + "	( "
								 + "		select company_id "
								 + "	from "
								 + "		service.m_company_contract_master "
								 + "	where "
								 + "		 contract_id =:contractId "
								 + "		) as \"payeeId\" "
								 + "from "
								 + "   service.m_company_contract_master ccmst "
								 + "	inner join service.t_vist_contract_cap_mapper vccm "
								 + "	on ccmst.contract_id = vccm.contract_id and vccm.status = 'A' "
								 + "where "
								 + "	vccm.service_id =:serviceId "
								 + "	and ccmst.contract_id =:contractId "
								 + "    and vccm.admission_id = :admissionId "
								 + "	AND '"+GlobalCommonDateUtils.getStringDate(currentDate, "yyyy-MM-dd")+"' between ccmst.validity_from AND ccmst.validity_to";
					}
			
			consumptionDto = (ContractConsumptionDto)sessionFactory.getCurrentSession()
					.createSQLQuery(pharmacyContractQuery)
					.setParameter("contractId",procedureForContract.getContractId())
					.setParameter("serviceId", procedureForContract.getProcedureId())
					.setParameter("admissionId", procedureForContract.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
					.uniqueResult();

			return consumptionDto;
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Integer updateProcedureCapAmount(ContractConsumptionDto consumptionDto, Double creditPayable) {
    try {
    	
    	    Integer  result = 0;
			if(creditPayable!=null && creditPayable>0)
			{
				if(consumptionDto.getAdmissionId()!=null)
				{
					Session session = sessionFactory.openSession();
					Transaction t = session.beginTransaction();
					result = (Integer)session
					.createSQLQuery("UPDATE service.t_vist_contract_cap_mapper SET balance_amount=balance_amount - :creaditPayable " 
							 + " 	WHERE contract_id=:contractId  " 
							 + " 	AND service_id=:serviceId  " 
							 + " 	AND admission_id=:admissionId"
							 + "	AND status ='A'")
					.setParameter("admissionId",consumptionDto.getAdmissionId())
					.setParameter("serviceId",consumptionDto.getProcedureId())
					.setParameter("contractId",consumptionDto.getContractId())
					.setDouble("creaditPayable",creditPayable).executeUpdate();
					
					t.commit();
					session.close();
				}
			}
			return result;
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
	}


	@Override
	public OrderMasterDto getPatientClassByVisit(OrderMasterDto orderMasterDto) throws ApplicationException {
		try {
	    	
    	    String query = null;
    	    if(orderMasterDto.getVisitTypeId().intValue()==OP)
    	    {
    	    	query = " select "
    	    			+ " 	enc.patient_class_id as \"patientClassId\", "
    	    			+ " 	trd.referral_id as \"referralId\" "
    	    			+ " from  "
    	    			+ " 	opd.t_encounter_master enc "
    	    			+ " left join t_referal_details trd on "
    	    			+ " 	trd.encounter_id = enc.encounter_id and  trd.status = 'A' "
        	    		+ "	where enc.encounter_id = "+orderMasterDto.getEncounterId();
    	    }
    	    else {
    	    	 query = " select "
    	    			+ " 	adm.patient_class_id as \"patientClassId\", "
    	    			+ " 	trd.referral_id as \"referralId\" "
    	    			+ " from  "
    	    			+ " 	adt.t_admission adm "
    	    			+ " left join t_referal_details trd on  "
    	    			+ " 	trd.admission_id = adm.admission_id and  trd.status = 'A' "
    	    	    	+ "	where	adm.admission_id = "+orderMasterDto.getAdmissionId();
    	       }
    	    		
    	    		
    	    OrderMasterDto dto = (OrderMasterDto)sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString())
					.setResultTransformer(Transformers.aliasToBean(OrderMasterDto.class))
					.uniqueResult();
    	    
    	    if(dto!=null)
    	    {
    	    	if(dto.getPatientClassId()!=null)
    	    		orderMasterDto.setPatientClassId(dto.getPatientClassId());
    	    	if(dto.getReferralId()!=null)
    	    		orderMasterDto.setReferralId(dto.getReferralId());
    	    }
    	    return orderMasterDto;
    	    
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
	}


	@Override
	public ContractConsumptionDto getPatientClassAndPercentageByVisit(ContractConsumptionDto contractConsumptionDto)
			throws ApplicationException 
	{
		try{
			String query = null;
			if(contractConsumptionDto.getAdmissionId()!=null)
			{
				query = " select " 
						 + " 	pc1.percentage as \"patientClassPercentage\", " 
						 + " 	pc1.markup_status as \"markupStatus\", " 
						 + " 	1 as \"priorityId\", "
						 + "	pc1.patient_type_group_id as \"id\" " 
						 + " from " 
						 + " 	adt.t_admission admission " 
						 + " inner join service.m_service_master service on " 
						 + " 	service.service_master_id =:serviceId " 
						 + " left join billing.m_patient_type_group_wise_details pc1 on " 
						 + " 	pc1.service_id = service.service_master_id " 
						 + " 	and pc1.patient_type_id = admission.patient_class_id " 
						 + " where " 
						 + " 	admission.admission_id = :admissionId " 
						 + " 	and pc1.percentage is not null " 
						 + "    and pc1.visit_type_id =:visitTypeId "
						 + " 	and pc1.payment_entitlement_id =(select payment_entitlement_id from opd.t_patient_sponsor_details tpsd where admission_id=:admissionId and status='A' and priority_id=1 limit 1) "
						 + " 	and pc1.patient_category_id =:patientCategoryId "
						 + " 	and pc1.tariff_id=:tariffId "
						 + "	and pc1.status='A' "
						 + " union " 
						 + " select " 
						 + " 	pc1.percentage as \"patientClassPercentage\", " 
						 + " 	pc1.markup_status as \"markupStatus\", " 
						 + " 	2 as \"priorityId\", "
						 + "	pc1.patient_type_group_id as \"id\" " 
						 + " from " 
						 + " 	adt.t_admission admission " 
						 + " inner join service.m_service_master service on " 
						 + " 	service.service_master_id =:serviceId " 
						 + " left join billing.m_patient_type_group_wise_details pc1 on " 
						 + " 	pc1.sub_speciality_id = service.sub_speciality_id " 
						 + " 	and pc1.patient_type_id = admission.patient_class_id " 
						 + " where " 
						 + " 	admission.admission_id = :admissionId " 
						 + " 	and pc1.service_id is null " 
						 + " 	and pc1.percentage is not null " 
						 + "    and pc1.visit_type_id =:visitTypeId "
						 + " 	and pc1.payment_entitlement_id =(select payment_entitlement_id from opd.t_patient_sponsor_details tpsd where admission_id=:admissionId and status='A' and priority_id=1 limit 1) "
						 + " 	and pc1.patient_category_id =:patientCategoryId "
						 + " 	and pc1.tariff_id=:tariffId "
						 + "	and pc1.status='A' "
						 + " union " 
						 + " select " 
						 + " 	pc3.percentage as \"patientClassPercentage\", " 
						 + " 	pc3.markup_status as \"markupStatus\", " 
						 + " 	3 as \"priorityId\", "
						 + "	pc3.patient_type_group_id as \"id\" " 
						 + " from " 
						 + " 	adt.t_admission admission " 
						 + " inner join service.m_service_master service on " 
						 + " 	service.service_master_id =:serviceId " 
						 + " left join billing.m_patient_type_group_wise_details pc3 on " 
						 + " 	pc3.speciality_id = service.speciality_id " 
						 + " 	and pc3.patient_type_id = admission.patient_class_id " 
						 + " where " 
						 + " 	admission.admission_id = :admissionId " 
						 + " 	and pc3.sub_speciality_id is null " 
						 + " 	and pc3.service_id is null " 
						 + " 	and pc3.percentage is not null "
						 + " 	and pc3.payment_entitlement_id =(select payment_entitlement_id from opd.t_patient_sponsor_details tpsd where admission_id=:admissionId and status='A' and priority_id=1 limit 1) "
						 + " 	and pc3.patient_category_id =:patientCategoryId "
						 + " 	and pc3.visit_type_id =:visitTypeId "
						 + " 	and pc3.tariff_id=:tariffId "
						 + "	and pc3.status='A' "
						 + " union " 
						 + " select "
						 + " 	0 as \"patientClassPercentage\", " 
						 + " 	false as \"markupStatus\", "
						 + " 	4 as \"priorityId\" , "
						 + " 	0 as \"id\" "
						 + " order by " 
						 + " 	\"priorityId\",\"id\" desc " 
						 + " limit 1 " ;
				
				ContractConsumptionDto dto = (ContractConsumptionDto)sessionFactory.getCurrentSession()
						.createSQLQuery(query.toString())
						.setParameter("admissionId", contractConsumptionDto.getAdmissionId())
						//.setParameter("paymentEntitlementId", contractConsumptionDto.getPaymentEntitlementId())
						.setParameter("serviceId", contractConsumptionDto.getServiceId())
						.setParameter("patientCategoryId", contractConsumptionDto.getPatientCategoryId()!=null ? contractConsumptionDto.getPatientCategoryId() : contractConsumptionDto.getPatientTypeId())
						.setParameter("visitTypeId", contractConsumptionDto.getVisitTypeId())
						.setParameter("tariffId", contractConsumptionDto.getTariffId()!=null ? contractConsumptionDto.getTariffId() : contractConsumptionDto.getDefaultSelfTariffId())
						.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
						.uniqueResult();
			
			return dto;
			}if(contractConsumptionDto.getEncounterId()!=null)
			{
				query = " select " 
						 + " 	pc1.percentage as \"patientClassPercentage\", " 
						 + " 	pc1.markup_status as \"markupStatus\", " 
						 + " 	1 as \"priorityId\", "
						 + "	pc1.patient_type_group_id as \"id\" " 
						 + " from " 
						 + " 	opd.t_encounter_master tem " 
						 + " inner join service.m_service_master service on " 
						 + " 	service.service_master_id =:serviceId " 
						 + " left join billing.m_patient_type_group_wise_details pc1 on " 
						 + " 	pc1.service_id = service.service_master_id " 
						 + " 	and pc1.patient_type_id = tem.patient_class_id " 
						 + " where " 
						 + " 	tem.encounter_id =:encounterId " 
						 + " 	and pc1.percentage is not null " 
						 + "    and pc1.visit_type_id =:visitTypeId "
						 + " 	and pc1.payment_entitlement_id =(select payment_entitlement_id from opd.t_patient_sponsor_details tpsd where encounter_id=:encounterId and status='A' and priority_id=1 limit 1) "
						 + " 	and pc1.patient_category_id =:patientCategoryId "
						 + " 	and pc1.tariff_id=:tariffId "
						 + " union " 
						 + " select " 
						 + " 	pc1.percentage as \"patientClassPercentage\", " 
						 + " 	pc1.markup_status as \"markupStatus\", " 
						 + " 	2 as \"priorityId\", " 
						 + "	pc1.patient_type_group_id as \"id\" " 
						 + " from " 
						 + " 	opd.t_encounter_master tem " 
						 + " inner join service.m_service_master service on " 
						 + " 	service.service_master_id =:serviceId " 
						 + " left join billing.m_patient_type_group_wise_details pc1 on " 
						 + " 	pc1.sub_speciality_id = service.sub_speciality_id " 
						 + " 	and pc1.patient_type_id = tem.patient_class_id " 
						 + " where " 
						 + " 	tem.encounter_id =:encounterId " 
						 + " 	and pc1.service_id is null " 
						 + " 	and pc1.percentage is not null " 
						 + "    and pc1.visit_type_id =:visitTypeId "
						 + " 	and pc1.payment_entitlement_id =(select payment_entitlement_id from opd.t_patient_sponsor_details tpsd where encounter_id=:encounterId and status='A' and priority_id=1 limit 1) "
						 + " 	and pc1.patient_category_id =:patientCategoryId "
						 + " 	and pc1.tariff_id=:tariffId "
						 + " union " 
						 + " select " 
						 + " 	pc3.percentage as \"patientClassPercentage\", " 
						 + " 	pc3.markup_status as \"markupStatus\", " 
						 + " 	3 as \"priorityId\", " 
						 + "	pc3.patient_type_group_id as \"id\" " 
						 + " from " 
						 + " 	opd.t_encounter_master tem " 
						 + " inner join service.m_service_master service on " 
						 + " 	service.service_master_id =:serviceId " 
						 + " left join billing.m_patient_type_group_wise_details pc3 on " 
						 + " 	pc3.speciality_id = service.speciality_id " 
						 + " 	and pc3.patient_type_id = tem.patient_class_id " 
						 + " where " 
						 + " 	tem.encounter_id =:encounterId " 
						 + " 	and pc3.sub_speciality_id is null " 
						 + " 	and pc3.service_id is null " 
						 + " 	and pc3.percentage is not null "
						 + " 	and pc3.payment_entitlement_id =(select payment_entitlement_id from opd.t_patient_sponsor_details tpsd where encounter_id=:encounterId and status='A' and priority_id=1 limit 1) "
						 + " 	and pc3.patient_category_id =:patientCategoryId "
						 + " 	and pc3.visit_type_id =:visitTypeId "
						 + " 	and pc3.tariff_id=:tariffId "
						 + " 	and pc3.status='A' " 
						 + " union " 
						 + " select "
						 + " 	0 as \"patientClassPercentage\", " 
						 + " 	false as \"markupStatus\", "
						 + " 	4 as \"priorityId\" , "
						 + " 	0 as \"id\" "
						 + " order by " 
						 + " 	\"priorityId\",\"id\" desc " 
						 + " limit 1 " ;
				
				
				ContractConsumptionDto dto = (ContractConsumptionDto)sessionFactory.getCurrentSession()
						.createSQLQuery(query.toString())
						.setParameter("encounterId", contractConsumptionDto.getEncounterId())
						//.setParameter("paymentEntitlementId", contractConsumptionDto.getPaymentEntitlementId())
						.setParameter("serviceId", contractConsumptionDto.getServiceId())
						.setParameter("patientCategoryId", contractConsumptionDto.getPatientCategoryId()!=null ? contractConsumptionDto.getPatientCategoryId() : contractConsumptionDto.getPatientTypeId())
						.setParameter("visitTypeId", contractConsumptionDto.getVisitTypeId())
						.setParameter("tariffId",  contractConsumptionDto.getTariffId()!=null ? contractConsumptionDto.getTariffId() : contractConsumptionDto.getDefaultSelfTariffId())
						.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
						.uniqueResult();
			
			return dto;
			}
			 
			
			
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
		return null;
		
	}


	@Override
	public ContractConsumptionDto getPatientClassAndPercentageByVisitWithountEncounter(
			ContractConsumptionDto contractConsumptionDto) {
		try {
			
				if(contractConsumptionDto.getPatientClassId()!=null)
				{
					String query = " select  " 
							 + "  	pc1.percentage as \"patientClassPercentage\",  " 
							 + "  	pc1.markup_status as \"markupStatus\",  " 
							 + "  	1 as \"priorityId\",  " 
							 + " 	pc1.patient_type_group_id as \"id\"  " 
							 + "  from  " 
							 + "  	 service.m_service_master service  " 
							 + "  left join billing.m_patient_type_group_wise_details pc1 on  " 
							 + "  	pc1.service_id = service.service_master_id  " 
							 + "  	and pc1.patient_type_id = :patientClassId " 
							 + "  where  " 
							 + "  	pc1.percentage is not null  " 
							 + "     and pc1.visit_type_id =1 " 
							 + "  	and pc1.payment_entitlement_id =1  " 
							 + "  	and pc1.patient_category_id =:patientCategoryId  " 
							 + "  	and pc1.tariff_id=:tariffId  " 
							 + "  	and service.service_master_id =:serviceId  " 
							 + "  union  " 
							 + "  select  " 
							 + "  	pc1.percentage as \"patientClassPercentage\",  " 
							 + "  	pc1.markup_status as \"markupStatus\",  " 
							 + "  	2 as \"priorityId\",  " 
							 + " 	pc1.patient_type_group_id as \"id\"  " 
							 + "  from  " 
							 + "  	service.m_service_master service  " 
							 + "  left join billing.m_patient_type_group_wise_details pc1 on  " 
							 + "  	pc1.sub_speciality_id = service.sub_speciality_id  " 
							 + "  	and pc1.patient_type_id = :patientClassId " 
							 + "  where  " 
							 + "   	service.service_master_id =:serviceId  " 
							 + "  	and pc1.service_id is null  " 
							 + "  	and pc1.percentage is not null  " 
							 + "     and pc1.visit_type_id =1  " 
							 + "  	and pc1.payment_entitlement_id =1  " 
							 + "  	and pc1.patient_category_id =:patientCategoryId  " 
							 + "  	and pc1.tariff_id=:tariffId  " 
							 + "  union  " 
							 + "  select  " 
							 + "  	pc3.percentage as \"patientClassPercentage\",  " 
							 + "  	pc3.markup_status as \"markupStatus\",  " 
							 + "  	3 as \"priorityId\",  " 
							 + " 	pc3.patient_type_group_id as \"id\"  " 
							 + "  from  " 
							 + "  	service.m_service_master service  " 
							 + "  left join billing.m_patient_type_group_wise_details pc3 on  " 
							 + "  	pc3.speciality_id = service.speciality_id  " 
							 + "  	and pc3.patient_type_id = :patientClassId " 
							 + "  where  " 
							 + "  	service.service_master_id =:serviceId  " 
							 + "  	and pc3.sub_speciality_id is null  " 
							 + "  	and pc3.service_id is null  " 
							 + "  	and pc3.percentage is not null  " 
							 + "  	and pc3.payment_entitlement_id =1  " 
							 + "  	and pc3.patient_category_id =:patientCategoryId  " 
							 + "  	and pc3.visit_type_id =1 " 
							 + "  	and pc3.tariff_id=:tariffId  " 
							 + "  	and pc3.status='A'  " 
							 + "  union  " 
							 + "  select  " 
							 + "  	0 as \"patientClassPercentage\",  " 
							 + "  	false as \"markupStatus\",  " 
							 + "  	4 as \"priorityId\" ,  " 
							 + "  	0 as \"id\"  " 
							 + "  order by  " 
							 + "  	\"priorityId\",\"id\" desc  " 
							 + "  limit 1  "  ;
					
					
					ContractConsumptionDto dto = (ContractConsumptionDto)sessionFactory.getCurrentSession()
							.createSQLQuery(query.toString())
							.setParameter("serviceId", contractConsumptionDto.getServiceId())
							.setParameter("patientCategoryId", contractConsumptionDto.getPatientCategoryId()!=null ? contractConsumptionDto.getPatientCategoryId() : contractConsumptionDto.getPatientTypeId())
							.setParameter("patientClassId", contractConsumptionDto.getPatientClassId()!=null ? contractConsumptionDto.getPatientClassId() :contractConsumptionDto.getPatientClassId())
							.setParameter("tariffId",  contractConsumptionDto.getTariffId()!=null ? contractConsumptionDto.getTariffId() : contractConsumptionDto.getDefaultSelfTariffId())
							.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
							.uniqueResult();
				
					return dto;
				}

		}catch(Exception e) {
			logger.error("getPatientClassAndPercentageByVisitWithountEncounter failed", e);
			throw new ServiceException(e.getMessage());
		}
		return null;
	}


	@Override
	public boolean getPreAuthAmutByVisitId(Integer encounterId, Integer admissionId) {
		try {
				StringBuilder query = new StringBuilder("select count(preauth_id) from billing.m_preauth_master where status='A' and "); 
				
				StringBuilder isTariffApplicable =  new StringBuilder("select "
							+ " 	count(is_tariff_rate_applicable) "
							+ " from service.m_company_contract_master mccm "
							+ " inner join opd.t_patient_sponsor_details tpsd on "
							+ " 	tpsd.contract_id = mccm.contract_id "
							+ " where "
							+ " 	is_tariff_rate_applicable = 'N' "
							+ "		and ");
				
				if(encounterId!=null && encounterId>0)
				{
					query.append(" encounter_id = "+encounterId);
					isTariffApplicable.append(" encounter_id = "+encounterId);
				}else{
					query.append(" admission_id = "+admissionId);
					isTariffApplicable.append(" admission_id = "+admissionId);
				}
				
				BigInteger preAuthCount = (BigInteger) sessionFactory.getCurrentSession().createSQLQuery(query.toString()).uniqueResult();
				BigInteger isTariffApplicableCount = (BigInteger) sessionFactory.getCurrentSession().createSQLQuery(isTariffApplicable.toString()).uniqueResult();
				if(preAuthCount.intValue()>0 || isTariffApplicableCount.intValue()>0)
				{
					return true;
				}

			}catch(Exception e) {
				logger.error("getPreAuthAmutByVisitId failed", e);
				throw new ServiceException(e.getMessage());
			}
			return false;
	}
	@Override
	public List<ContractConsumptionDto> getServiceDetailsByContractIdAfterTariffChange(ContractConsumptionDto contractConsumptionDto)throws ServiceException {
		try {
			
			List<ContractConsumptionDto> consumptionDto = new ArrayList<ContractConsumptionDto>();
			
			if(contractConsumptionDto !=null)
			{
				
			String query=" select " 
					+ " 	unitservice.rate as \"serviceRate\" " 
					+ " from service.m_unit_service_tariff_master unitservice "
					+ " 	where " 
					+ " 		unitservice.organization_id =:organizationId " 
					+ " 		and unitservice.unit_id =:unitId " 
					+ " 		and unitservice.tariff_id =:tariffId " 
					+ " 		and unitservice.patient_type_id = :patientTypeId " 
					+ " 		and unitservice.billing_bed_category_id =:billingBed " 
					+ " 		and unitservice.visit_type_id =:visitTypeId " 
//					+ " 		and unitservice.payment_entitlement_id =:paymentEntitlementId " 
					+ " 		and unitservice.service_id = :serviceId " 
					+ " order by " 
					+ " 	\"effective_date\" desc limit 1 ";
				
				consumptionDto = sessionFactory.getCurrentSession()
						.createSQLQuery(query.toString())
						.setParameter("billingBed",contractConsumptionDto.getBillingBedCategoryId())
						.setParameter("serviceId",contractConsumptionDto.getServiceId())
						.setParameter("patientTypeId", contractConsumptionDto.getPatientCategoryId()!=null ? contractConsumptionDto.getPatientCategoryId() : contractConsumptionDto.getPatientTypeId())
						.setParameter("organizationId", contractConsumptionDto.getOrganizationId())
						.setParameter("unitId", contractConsumptionDto.getUnitId())
//						.setParameter("paymentEntitlementId", contractConsumptionDto.getPaymentEntitlementId())
						.setParameter("visitTypeId", contractConsumptionDto.getVisitTypeId())
						.setParameter("tariffId", contractConsumptionDto.getTariffId())
						//.setDate("effectiveDate", GlobalCommonDateUtils.getDate(contractConsumptionDto.getCurrentDate(),"yyyy-MM-dd"))
						.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
						.list();
			
			}
			return consumptionDto;
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
		}


	@Override
	public boolean getGstApplicableForBillingBedCategory(Integer unitId) {
		 boolean gstFlag=(boolean) sessionFactory.getCurrentSession().createSQLQuery("select gst_for_billing_bed_category from t_unit_application_billing_configuration where unit_id="+unitId)
		        .uniqueResult();
		 return gstFlag;
	}


	@Override
	public List<ContractConsumptionDto> getgstDetails(Integer billingBedCategoryId) {
//		List<ContractConsumptionDto> gstList=(List<ContractConsumptionDto>) sessionFactory.getCurrentSession()
//				.createSQLQuery("select is_gst_applicable as \"isGstApplicable\",gst_amount_range as \"gstAmountRange\""
//						+ " ,gst_percentage as \"gstPercentage\" from adt.m_billing_bed_category_master where status='A' and billing_bed_category_id="+billingBedCategoryId)
//			        .list();
//			 return gstList;
		String query="select is_gst_applicable as \"isGstApplicable\",gst_amount_range as \"gstAmountRange\" "
						+ " ,gst_percentage as \"gstPercentage\" from adt.m_billing_bed_category_master"
						+ " where status='A' and billing_bed_category_id=:billingBedCategoryId ";
			 List<ContractConsumptionDto> consumptionDto = new ArrayList<ContractConsumptionDto>();
			 consumptionDto = sessionFactory.getCurrentSession()
						.createSQLQuery(query.toString())
						.setParameter("billingBedCategoryId",billingBedCategoryId)
						.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
						.list();
			 return consumptionDto;
	}

}
