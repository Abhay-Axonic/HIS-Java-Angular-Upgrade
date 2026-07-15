package com.param.global.billing.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.TaxDto;

import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"unchecked","rawtypes"})
public class ContractConsumptionCostEstimationDaoImpl implements IContractConsumptionCostEstimationDao,ICommonConstants{

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public List<ContractConsumptionDto> getServiceDetailsByContractId(ContractConsumptionDto contractConsumptionDto)
			throws ServiceException {
		try {
			
			List<ContractConsumptionDto> consumptionDto =null;
			
			StringBuilder query= new StringBuilder("select "
					+ " 	csd.service_id as \"serviceId\", "
					+ " 	spons.priority_id as \"priorityId\", "
					+ " 	coalesce(cast(unitservice.rate_editable_max_value as double precision), 0) as \"maxRateEditable\", "
					+ " 	coalesce(cast(unitservice.rate_editble_min_value as double precision), 0) as \"minRateEditable\", "
					+ " 	csd.contract_id as \"contractId\", "
					+ " 	csd.apportioned_price as \"serviceRate\", "
					+ " 	com.company_desc as \"companyName\", "
					+ " 	( "
					+ " 	select "
					+ " 		cast(co_share_percentage as double precision) "
					+ " 	from "
					+ " 		service.t_contract_group_details "
					+ " 	where "
					+ " 		contract_id = csd.contract_id "
					+ " 		and sub_department_id = service.sub_speciality_id) as \"coShare\", "
					+ " 	ccm.company_id as \"payeeId\", "
					+ " 	unitservice.gst_type_id as \"taxId\", "
					+ " 	tax.tax_percentage as \"taxPercentage\", "
					+ " 	csd.final_price as \"finalPrice\", "
					+ " 	csd.self_payable as \"selfPayable\", "
					+ " 	csd.credit_payable as \"creaditPayable\", "
					+ " 	( "
					+ " 	select "
					+ " 		ccd.balance_cap_amount "
					+ " 	from "
					+ " 		service.t_vist_contract_cap_mapper_cost_estimation ccd "
					+ " 	where "
					+ " 		ccd.department_id = service.speciality_id "
					+ " 		and ccd.contract_id = csd.contract_id "
					+ " 		and ccd.patient_id =:patientId "
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
					+ " 			opd.t_patient_sponsor_details_cost_estimation spons "
					+ " 		where "
					+ " 			status = 'A' "
					+ " 			and priority_id = 1 "
					+ " 			and patient_id = :patientId) "
					+ " 		and billing_bed_category_id =:billingBed) as \"billingBedExist\", "
					+ " 	0 as \"amount\", "
					+ " 	0 as \"balanceAmount\", "
					+ " 	0 as \"quantity\", "
					+ " 	0 as \"balanceQuantity\", "
					+ " 	0 as \"applicableDays\", "
					+ " 	'' as \"applicableDate\", "
					+ " 	service.speciality_id as \"specialityId\", "
					+ " 	ccm.tariff_id as \"tariffId\", "
					+ " 	1 as \"dayDiff\", "
					+ " 	false as \"tillDischarge\", "
					+ " 	1 as \"type\", "
					+ " 	coalesce(unitservice.is_rate_editable, cast('N' as char)) as \"isRateEditable\", "
					+ " 	coalesce(unitservice.is_allow_multiple_quantity, cast('N' as char)) as \"isQuantityEditable\", "
					+ " 	( "
					+ " 	select "
					+ " 		self_payee_id "
					+ " 	from "
					+ " 		t_unit_application_billing_configuration "
					+ " 	where "
					+ " 		unit_id = spons.unit_id "
					+ " 		and status = 'A') as \"selfPayeeId\", "
					+ " 	service.service_standard_name as \"serviceName\", "
					+ " 	spl.speciality_name as \"specialityName\", "
					+ "		ccm.is_tariff_rate_applicable as \"isTariffRateApplicable\" "
					+ " from "
					+ " 	opd.t_patient_sponsor_details_cost_estimation spons "
					+ " inner join t_company_master com on "
					+ " 	com.company_id = spons.payee_id "
					+ " inner join service.t_contract_service_details csd on "
					+ " 	spons.contract_id = csd.contract_id "
					+ " inner join service.m_company_contract_master ccm on "
					+ " 	csd.contract_id = ccm.contract_id "
					+ " inner join service.m_service_master service on "
					+ " 	service.service_master_id = csd.service_id "
					+ "inner join doctor.m_speciality_master spl on "
					+ "		service.speciality_id = spl.speciality_id "	
					+ " inner join service.t_unit_service_master_details unitservice on "
					+ " 	service.service_master_id = unitservice.service_master_id "
					+ " left join m_tax tax on "
					+ " 	tax.id = unitservice.gst_type_id "
					+ " 	and tax.unit_id = ccm.unit_id "
					+ " where "
					+ " 	spons.patient_id =:patientId "
					+ " 	and ccm.is_tariff_rate_applicable = 'Y' "
					+ " 	and csd.service_id =:serviceId "
					+ " 	and unitservice.unit_id = ccm.unit_id "
					+ " 	and spons.status = 'A' "
					+ " 	and :currentDate >= to_char(ccm.validity_from, 'YYYY-MM-DD') "
					 + " 	and :currentDate <= to_char(ccm.validity_to, 'YYYY-MM-DD') "
					+ " union "
					+ " select "
					+ " 	csd.service_id as \"serviceId\", "
					+ " 	( "
					+ " 	select "
					+ " 		priority_id "
					+ " 	from "
					+ " 		opd.t_patient_sponsor_details_cost_estimation "
					+ " 	where "
					+ " 		contract_id = csd.contract_id "
					+ " 		and patient_id = csd.patient_id "
					+ " 		and status = 'A') as \"priorityId\", "
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
					+ " 	ccm.company_id as \"payeeId\", "
					+ " 	unitservice.gst_type_id as \"taxId\", "
					+ " 	tax.tax_percentage as \"taxPercentage\", "
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
					+ " 			opd.t_patient_sponsor_details_cost_estimation spons "
					+ " 		where "
					+ " 			status = 'A' "
					+ " 			and priority_id = 1 "
					+ " 			and patient_id = :patientId) "
					+ " 		and billing_bed_category_id =:billingBed) as \"billingBedExist\", "
					+ " 	csd.amount as \"amount\", "
					+ " 	csd.balance_amount as \"balanceAmount\", "
					+ " 	csd.quantity as \"quantity\", "
					+ " 	csd.balance_quantity as \"balanceQuantity\", "
					+ " 	csd.applicable_days as \"applicableDays\", "
					+ " 	to_char(csd.applicable_date, 'yyyy-MM-dd') as \"applicableDate\", "
					+ " 	service.speciality_id as \"specialityId\", "
					+ " 	ccm.tariff_id as \"tariffId\", "
					+ " 	1 as \"dayDiff\", "
					+ " 	csd.till_discharge as \"tillDischarge\", "
					+ " 	2 as \"type\", "
					+ " 	coalesce(unitservice.is_rate_editable, cast('N' as char)) as \"isRateEditable\", "
					+ " 	coalesce(unitservice.is_allow_multiple_quantity, cast('N' as char)) as \"isQuantityEditable\", "
					+ " 	( "
					+ " 	select "
					+ " 		self_payee_id "
					+ " 	from "
					+ " 		t_unit_application_billing_configuration "
					+ " 	where "
					+ " 		unit_id = csd.unit_id "
					+ " 		and status = 'A') as \"selfPayeeId\", "
					+ " 	service.service_standard_name as \"serviceName\", "
					+ " 	spl.speciality_name as \"specialityName\", "
					+ "		ccm.is_tariff_rate_applicable as \"isTariffRateApplicable\" "	
					+ " from "
					+ " 	service.t_vist_contract_cap_mapper csd "
					+ " inner join service.m_company_contract_master ccm on "
					+ " 	csd.contract_id = ccm.contract_id "
					+ " inner join t_company_master com on "
					+ " 	com.company_id = ccm.company_id "
					+ " inner join service.m_service_master service on "
					+ " 	service.service_master_id = csd.service_id "
					+ "inner join doctor.m_speciality_master spl on "
					+ "		service.speciality_id = spl.speciality_id "	
					+ " inner join service.t_unit_service_master_details unitservice on "
					+ " 	service.service_master_id = unitservice.service_master_id "
					+ " left join m_tax tax on "
					+ " 	tax.id = unitservice.gst_type_id "
					+ " 	and tax.unit_id = ccm.unit_id "
					+ " where "
					+ " 	csd.service_id =:serviceId "
					+ " 	and csd.patient_id =:patientId "
					+ " 	and unitservice.unit_id = ccm.unit_id "
					+ " 	and csd.status = 'A' "
					+ " 	and :currentDate >= to_char(ccm.validity_from, 'YYYY-MM-DD') "
					 + " 	and :currentDate <= to_char(ccm.validity_to, 'YYYY-MM-DD') "
					+ " union "
					+ " select "
					+ " 	ustm.service_id as \"serviceId\", "
					+ " 	( "
					+ " 	select "
					+ " 		priority_id "
					+ " 	from "
					+ " 		opd.t_patient_sponsor_details_cost_estimation "
					+ " 	where "
					+ " 		contract_id = spons.contract_id "
					+ " 		and patient_id = spons.patient_id "
					+ " 		and status = 'A') as \"priorityId\", "
					+ " 	coalesce(cast(unitservice.rate_editable_max_value as double precision), 0) as \"maxRateEditable\", "
					+ " 	coalesce(cast(unitservice.rate_editble_min_value as double precision), 0) as \"minRateEditable\", "
					+ " 	ccm.contract_id as \"contractId\", "
					+ " 	ustm.rate as \"serviceRate\", "
					+ " 	com.company_desc as \"companyName\", "
					+ " 	ccm.co_share_percentage as \"coShare\", "
					+ " 	ccm.company_id as \"payeeId\", "
					+ " 	unitservice.gst_type_id as \"taxId\", "
					+ " 	tax.tax_percentage as \"taxPercentage\", "
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
					+ " 	ccm.tariff_id as \"tariffId\", "
					+ " 	1 as \"dayDiff\", "
					+ " 	false as \"tillDischarge\", "
					+ " 	1 as \"type\", "
					+ " 	coalesce(unitservice.is_rate_editable, cast('N' as char)) as \"isRateEditable\", "
					+ " 	coalesce(unitservice.is_allow_multiple_quantity, cast('N' as char)) as \"isQuantityEditable\", "
					+ " 	( "
					+ " 	select "
					+ " 		self_payee_id "
					+ " 	from "
					+ " 		t_unit_application_billing_configuration "
					+ " 	where "
					+ " 		unit_id = ccm.unit_id "
					+ " 		and status = 'A') as \"selfPayeeId\", "
					+ " 	service.service_standard_name as \"serviceName\", "
					+ " 	speciality.speciality_name as \"specialityName\", "
					+ "		ccm.is_tariff_rate_applicable as \"isTariffRateApplicable\" "	
					+ " from "
					+ " 	opd.t_patient_sponsor_details_cost_estimation spons "
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
					+ " where "
					+ " 	spons.patient_id =:patientId "
					+ "		and spons.status ='A' "
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
					+ " 	where "
					+ " 		ustm.organization_id =:organizationId "
					+ " 		and details.unit_id = ustm.unit_id "
					+ " 		and ustm.unit_id =:unitId "
					+ " 		and ustm.tariff_id = ccm.tariff_id "
					+ " 		and ustm.patient_type_id = :patientTypeId "
					+ " 		and ustm.billing_bed_category_id =:billingBed "
					+ " 		and ustm.visit_type_id =:visitTypeId "
					+ " 		and ustm.payment_entitlement_id =:paymentEntitlementId "
					+ " 		and ustm.effective_date <=:effectiveDate "
					+ " 		and ustm.service_id = :serviceId) "
					+ " order by "
					+ " 	\"priorityId\" ");
				
				consumptionDto = sessionFactory.getCurrentSession()
						.createSQLQuery(query.toString())
						.setParameter("patientId",contractConsumptionDto.getPatientId())
						.setParameter("billingBed",contractConsumptionDto.getBillingBedCategoryId())
						.setParameter("serviceId",contractConsumptionDto.getServiceId())
						.setParameter("patientTypeId", contractConsumptionDto.getPatientCategoryId()!=null ? contractConsumptionDto.getPatientCategoryId() : contractConsumptionDto.getPatientTypeId())
						.setParameter("organizationId", contractConsumptionDto.getOrganizationId())
						.setParameter("unitId", contractConsumptionDto.getUnitId())
						.setParameter("paymentEntitlementId", 1)
						.setParameter("visitTypeId", contractConsumptionDto.getVisitTypeId())
						.setParameter("effectiveDate", GlobalCommonDateUtils.getDate(contractConsumptionDto.getCurrentDate(),"yyyy-MM-dd"))
						.setParameter("currentDate", contractConsumptionDto.getCurrentDate().substring(0,10))
						.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
						.list();
			
			return consumptionDto;
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public ContractConsumptionDto getItemDetailsByContractId(ContractConsumptionDto itemsForContractDto)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContractConsumptionDto getProcedureDetailsByContract(ContractConsumptionDto procedureForContract,
			boolean isProcedure) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContractConsumptionDto> getSponsorDetailsByEncounterId(ContractConsumptionDto consumptionDto)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContractConsumptionDto> getSponsorDetailsByEncounterPharmacy(ContractConsumptionDto consumptionDto)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateCapAmount(ContractConsumptionDto consumptionDto, Double creaditPayable) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer addCapAmount(ContractConsumptionDto consumptionDto) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContractConsumptionDto getVertualServiceDetailsByContractId(ContractConsumptionDto contractConsumptionDto)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaxDto getTaxPerByServiceId(Integer serviceId, Integer unitId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getActivePaymentEntitlement(ContractConsumptionDto consumptionDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer updatePharmacyCapQuantity(ContractConsumptionDto consumptionDto, Double creditPayable,
			Integer quantity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer updateProcedureCapAmount(ContractConsumptionDto consumptionDto, Double creditPayable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContractConsumptionDto> getServiceCapDetailsByContractId(ContractConsumptionDto consumptionDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateServiceCapAmount(ContractConsumptionDto consumptionDto, Double creaditPayable, Double qty)
			throws ServiceException {
try {
			
			if(creaditPayable!=null && creaditPayable>0)
			{
					
				if(consumptionDto.getType()==3)
				{
					sessionFactory.getCurrentSession()
					.createSQLQuery( "UPDATE service.t_vist_contract_cap_mapper_cost_estimation SET  balance_amount=balance_amount - :creaditPayable " 
							 + " 	WHERE contract_id=:contractId  " 
							 + " 	AND service_id=:serviceId  " 
							 + " 	AND patient_id=:patientId"
							 + "	AND status='A'")
					.setParameter("patientId",consumptionDto.getPatientId())
					.setParameter("serviceId",consumptionDto.getServiceId())
					.setParameter("contractId",consumptionDto.getContractId())
					.setParameter("creaditPayable",creaditPayable)
					.executeUpdate();
				}
				if(consumptionDto.getType()==4)
				{
					sessionFactory.getCurrentSession()
					.createSQLQuery( "UPDATE service.t_vist_contract_cap_mapper_cost_estimation SET applicable_date =:applicableDate, balance_amount=amount - :creaditPayable " 
							 + " 	WHERE contract_id=:contractId  " 
							 + " 	AND service_id=:serviceId  " 
							 + " 	AND patient_id=:patientId"
							 + "	AND status='A'")
					.setParameter("patientId",consumptionDto.getPatientId())
					.setParameter("serviceId",consumptionDto.getServiceId())
					.setParameter("contractId",consumptionDto.getContractId())
					.setParameter("creaditPayable",creaditPayable)
					.setParameter("applicableDate",GlobalCommonDateUtils.getDate(consumptionDto.getApplicableDate(),"yyyy-MM-dd"))
					.executeUpdate();
				}
			}
			
			if(qty!=null)
			{
				if(consumptionDto.getType()==3)
				{
					sessionFactory.getCurrentSession()
					.createSQLQuery( "UPDATE service.t_vist_contract_cap_mapper_cost_estimation SET  balance_quantity=balance_quantity - :qty " 
							 + " 	WHERE contract_id=:contractId  " 
							 + " 	AND service_id=:serviceId  " 
							 + " 	AND patient_id=:patientId"
							 + "	AND status='A'")
					.setParameter("patientId",consumptionDto.getPatientId())
					.setParameter("serviceId",consumptionDto.getServiceId())
					.setParameter("contractId",consumptionDto.getContractId())
					.setParameter("qty",qty)
					.executeUpdate();
				}
				
				if(consumptionDto.getType()==4)
				{
					sessionFactory.getCurrentSession()
					.createSQLQuery( "UPDATE service.t_vist_contract_cap_mapper_cost_estimation SET applicable_date =:applicableDate, balance_quantity=quantity - :qty " 
							 + " 	WHERE contract_id=:contractId  " 
							 + " 	AND service_id=:serviceId  " 
							 + " 	AND patient_id=:patientId"
							 + "	AND status='A'")
					.setParameter("patientId",consumptionDto.getPatientId())
					.setParameter("serviceId",consumptionDto.getServiceId())
					.setParameter("contractId",consumptionDto.getContractId())
					.setParameter("qty",qty)
					.setParameter("applicableDate",GlobalCommonDateUtils.getDate(consumptionDto.getApplicableDate(),"yyyy-MM-dd"))
					.executeUpdate();
				}
					
			}	
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
		
	}

	@Override
	public Integer getFallBacktariff(Integer contractId, Integer admission) {
		// TODO Auto-generated method stub
		return null;
	}

}