package com.param.billing.unit.dao;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.IError;
import com.param.billing.unit.dto.ChargeConversionDto;
import com.param.global.billing.dto.TContractServiceDetailsDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.CompanyMasterDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.ItemsForContractDto;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.service.dto.CommonDto;
import com.param.service.dto.CompanyContractReqDto;
import com.param.service.dto.ContractProductCategoryDto;
import com.param.service.dto.MCompanyContractMasterDto;
import com.param.service.dto.ServiceForCompnayContarctReqDto;
import com.param.service.dto.TContractBedCategoryDetailDto;
import com.param.service.dto.TContractCapDetailsDto;
import com.param.service.dto.TContractGroupDetailsDto;
import com.param.service.dto.TContractGroupPharmacyDetailsDto;
import com.param.service.dto.TContractProcedureDetailsDto;
import com.param.service.global.model.MCompanyContractMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;


@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CompanyContractDaoImpl extends GenericDao<MCompanyContractMaster, Integer> implements ICompanyContractDao,ICommonConstants, IError{

	final static Logger logger = Logger.getLogger(CompanyContractDaoImpl.class);
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	SessionFactory sessionFactory;
	
	public CompanyContractDaoImpl() {
		super(MCompanyContractMaster.class);
	}


	@Override
	public Response getCompanyMasterList(CompanyMasterDto companyMasterDto) throws ApplicationException {
		List<CommonDto>  listCompanyMasterList = null; 
		try
		{
			if(companyMasterDto.getCompanyTypeId()!=null) {
				listCompanyMasterList = (List<CommonDto>) sessionFactory.getCurrentSession()
						.createSQLQuery("SELECT "
								+"	companymst.company_id AS id, "
								+"	companymst.company_desc  AS name "
								+"FROM "
								+"	t_company_master  companymst "
								+"WHERE "
								+" companymst.company_type_id=:companyTypeId"
								+"	AND companymst.status = 'A' "
								// HIS-20753 - Fornt end remove but in query not remove 
								//+"	and '"+GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(companyMasterDto.getUnitId()), "yyyy-MM-dd")+"' between to_char(companymst.start_date,'YYYY-MM-DD') and to_char(companymst.end_date,'YYYY-MM-DD')"
								+"	AND companymst.unit_id = :orgUnitId order by companymst.company_desc asc")
						.setParameter("companyTypeId", companyMasterDto.getCompanyTypeId())
						//.setInteger("orgId", companyMasterDto.getOrganizationId())
						.setParameter("orgUnitId", companyMasterDto.getUnitId())
	                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_CODE, listCompanyMasterList, null);
			}
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}




	@Override
	public Response getAssociateCompanyMaster(CompanyMasterDto companyMasterDto) throws ApplicationException {
		List<CommonDto>  listAssociateCompanyMasterList = null; 
		try
		{
			if(companyMasterDto.getCompanyId()!=null) {
				listAssociateCompanyMasterList = (List<CommonDto>) sessionFactory.getCurrentSession()
						.createSQLQuery("SELECT "
								+"	insu.insurance_id AS id, "
								+"	insu.insurance_desc   AS name "
								+"FROM "
								+"	service.t_insurance_tpa_mapper iMapp "
								+" INNER JOIN adt.t_insurance insu on insu.insurance_id =iMapp.insurance_id "
								+"WHERE "
								+"	iMapp.status = 'A' "
								+"	AND iMapp.organization_id = :orgId "
								+"	AND iMapp.unit_id = :orgUnitId "
								+"	AND iMapp.company_id = :companyId order by insu.insurance_desc asc")
						.setParameter("companyId", companyMasterDto.getCompanyId())
						.setParameter("orgId", companyMasterDto.getOrganizationId())
						.setParameter("orgUnitId", companyMasterDto.getUnitId())
	                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_CODE, listAssociateCompanyMasterList, null);
			}
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		} catch (Exception e)
		{
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	

	@Override
	public Response getGradeListByAssociateCompanyId(CompanyMasterDto companyMasterDto) throws ApplicationException {
		List<CommonDto>  listAssociateCompanyMasterList = null; 
		try
		{
			listAssociateCompanyMasterList = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT "
							+" grademst.contract_grade_master_id AS id, "
							+"	grademst.grade_desc AS name "
							+"FROM "
							+"	service.t_company_contarct_grade_mpper tcompany "
							+ " inner join service.m_contract_grade_master grademst on grademst.contract_grade_master_id = tcompany.contract_grade_master_id "
							+" WHERE "
							+" tcompany.org_id  = :orgId "
							+"	AND tcompany.org_unit_id = :orgUnitId "
							+ " GROUP BY "
							+ "   grademst.contract_grade_master_id "
							+ "ORDER BY grademst.grade_desc ASC")
					.setParameter("orgId", companyMasterDto.getOrganizationId())
					.setParameter("orgUnitId", companyMasterDto.getUnitId())
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_CODE, listAssociateCompanyMasterList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response getVisitTypeList(CompanyMasterDto companyMasterDto) throws ApplicationException {
		List<CommonDto>  listVisitType = null; 
		try
		{
			listVisitType = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT "
							+"	visittype.visit_type_id AS id, "
							+"	visittype.visit_type_name  AS name "
							+"FROM "
							+"	m_visit_type_master visittype "
							+"WHERE "
							+"	visittype.status = 'A' "
							+" ORDER BY visittype.visit_type_name ASC LIMIT 4")
					    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_CODE, listVisitType, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response createCompanyContarct(MCompanyContractMasterDto masterDto)
			throws ApplicationException {
		try{
			MCompanyContractMaster mCompanyContractMaster = new MCompanyContractMaster();
			mCompanyContractMaster.setBillingBedCategoryId(masterDto.getBillingBedCategoryId());
			mCompanyContractMaster.setCreatedBy(masterDto.getCreatedBy());
			mCompanyContractMaster.setCreatedDate(TimeZoneComponent.getDateByzone(masterDto.getUnitId()));
			mCompanyContractMaster.setUnitId(masterDto.getUnitId());
			mCompanyContractMaster.setOrgId(masterDto.getOrgId());
			mCompanyContractMaster.setUpdatedBy(masterDto.getUpdatedBy());
			//mCompanyContractMaster.setUpdatedDate(masterDto.getUpdatedDate() != null && !masterDto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(masterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(unitId));
			mCompanyContractMaster.setValidityFrom(masterDto.getValidityFrom() != null && !masterDto.getValidityFrom().isEmpty() ? GlobalCommonDateUtils.getDate(masterDto.getValidityFrom(), "MM-dd-yyyy") : TimeZoneComponent.getDateByzone(masterDto.getUnitId()));
			mCompanyContractMaster.setValidityTo(masterDto.getValidityTo() != null && !masterDto.getValidityTo().isEmpty() ? GlobalCommonDateUtils.getDate(masterDto.getValidityTo(), "MM-dd-yyyy") : TimeZoneComponent.getDateByzone(masterDto.getUnitId()));
			mCompanyContractMaster.setVisitTypeId(masterDto.getVisitTypeId());
			mCompanyContractMaster.setPatientTypeId(1);
			mCompanyContractMaster.setAssociateCompanyId(masterDto.getAssociateCompanyId());
			mCompanyContractMaster.setBillingBedCategoryId(masterDto.getBillingBedCategoryId());
			mCompanyContractMaster.setCompanyId(masterDto.getCompanyId());
			mCompanyContractMaster.setCoSharePercentage(masterDto.getCoSharePercentage());
			mCompanyContractMaster.setContractName(masterDto.getContractName());
			mCompanyContractMaster.setGradeId(masterDto.getGradeId());
			mCompanyContractMaster.setIsTariffRateApplicable(masterDto.getIsTariffRateApplicable());
			mCompanyContractMaster.setTariffId(masterDto.getTariffId());
			mCompanyContractMaster.setPaymentEntitlementId(masterDto.getPaymentEntitlementId());
			mCompanyContractMaster.setStatus('A');
			mCompanyContractMaster.setIsSmartApplicable(masterDto.getIsSmartApplicable());
			mCompanyContractMaster.setVersionNo(masterDto.getVersionNo());
			mCompanyContractMaster.setOldContractId(masterDto.getOldContractId());
			mCompanyContractMaster.setParentContractId(masterDto.getParentContractId());
			mCompanyContractMaster.setGeneralLedgerId(masterDto.getGeneralLedgerId());
			mCompanyContractMaster = save(mCompanyContractMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, mCompanyContractMaster);
		}catch(Exception e){
			
			//throw new ServiceException(e.getMessage());
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	public Response searchCompanyContract(ServiceForCompnayContarctReqDto reqDto) throws ServiceException {
		try{
			   StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("SEARCH_FOR_COMPANY_CONTRACT").getQueryString());
				query.append(reqDto.getTariffId() != null ? " and tariffmst.tariff_id =:tariffId " : " ");
				query.append(reqDto.getPaymentEntitlementId() != null ? " and paymst.payment_entitlement_id =:paymentEntitlementId " : " ");
				
				if(reqDto.getPaymentEntitlementId() != null)
					query.append(reqDto.getPaymentEntitlementId()==COMPANY ? " and contractmst.company_id =:companyId " : " and contractmst.associate_company_id =:associateCompanyId ");
				
				//query.append(reqDto.getAssociateCompanyId() != null ? " and insurance.insurance_id  =" + reqDto.getAssociateCompanyId() : " ");
				query.append(" and contractmst.is_versioning='N' "); 
				query.append(" order by contractmst.contract_id desc ");  
			         Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							 .setParameter("orgId", reqDto.getOrgId())
							 .setParameter("orgUnitId", reqDto.getUnitId());

					if(reqDto.getTariffId() != null)
					 sqlQuery.setParameter("tariffId", reqDto.getTariffId());

					if(reqDto.getPaymentEntitlementId() != null) {
						sqlQuery.setParameter("paymentEntitlementId", reqDto.getPaymentEntitlementId());

						if(reqDto.getPaymentEntitlementId()==COMPANY)
							sqlQuery.setParameter("companyId", reqDto.getCompanyId());
						else
							sqlQuery.setParameter("associateCompanyId", reqDto.getAssociateCompanyId());

					}


			List<ServiceForCompnayContarctReqDto> listServiceForCompnayContarctResDto = sqlQuery
					.setFirstResult(reqDto.getOffset() != null ? reqDto.getOffset() : 0)
					.setMaxResults(reqDto.getNoOfRecordsPerPage() != null ? reqDto.getNoOfRecordsPerPage() : 10)
	               
					.setResultTransformer(Transformers.aliasToBean(ServiceForCompnayContarctReqDto.class)).list();
			         
			         
			         Session session = sessionFactory.getCurrentSession();
			         List<ServiceForCompnayContarctReqDto> listServiceForCompnayCount=  sqlQuery
//							 .setParameter("orgId", reqDto.getOrgId())
//							 .setParameter("orgUnitId", reqDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ServiceForCompnayContarctReqDto.class)).list();
				        
			         
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceForCompnayContarctResDto, listServiceForCompnayCount.size());
		}catch(Exception e){

			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response CompanyContractListSearch(ServiceForCompnayContarctReqDto reqDto) throws ServiceException {
		try{
			   StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("SEARCH_FOR_COMPANY_CONTRACT").getQueryString());
				query.append(reqDto.getTariffId() != null ? " and tariffmst.tariff_id =:tariffId " : " ");
				query.append(reqDto.getPaymentEntitlementId() != null ? " and paymst.payment_entitlement_id =:paymentEntitlementId " : " ");
				
				if(reqDto.getPaymentEntitlementId() != null)
					query.append(reqDto.getPaymentEntitlementId()==COMPANY ? " and contractmst.company_id =:companyId " : " and contractmst.associate_company_id =:associateCompanyId ");
				
				if(reqDto.getAssCompanyName() != null) {
					query.append(" and (lower(company.company_desc) like :assCompanyName "
							+ "or lower(paymst.payment_entitlement_desc) like :assCompanyName "
									+ "or lower( insurance.insurance_desc ) like :assCompanyName "
											+ "or lower( tariffmst.tariff_desc )  like :assCompanyName "
													+ "or lower(contractmst.contract_name) like :assCompanyName) ");
				}
					
				
				
				//query.append(reqDto.getAssociateCompanyId() != null ? " and insurance.insurance_id  =" + reqDto.getAssociateCompanyId() : " ");
				query.append(" and contractmst.is_versioning='N' "); 
				query.append(" order by contractmst.contract_id desc ");   
			         Query sqlQuery	 = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							 .setParameter("orgId", reqDto.getOrgId())
							 .setParameter("orgUnitId", reqDto.getUnitId());

			if(reqDto.getTariffId() != null)
				sqlQuery.setParameter("tariffId", reqDto.getTariffId());

			if(reqDto.getPaymentEntitlementId() != null) {

				sqlQuery.setParameter("paymentEntitlementId", reqDto.getPaymentEntitlementId());

				if(reqDto.getPaymentEntitlementId()==COMPANY)
					sqlQuery.setParameter("companyId", reqDto.getCompanyId());
				else
					sqlQuery.setParameter("associateCompanyId", reqDto.getAssociateCompanyId());
			}

			if(reqDto.getAssCompanyName() != null)
				sqlQuery.setParameter("assCompanyName", "%" + reqDto.getAssCompanyName().trim().toLowerCase() + "%");

			List<ServiceForCompnayContarctReqDto> listServiceForCompnayContarctResDto = sqlQuery
					.setFirstResult(reqDto.getOffset() != null ? reqDto.getOffset() : 0)
					.setMaxResults(reqDto.getNoOfRecordsPerPage() != null ? reqDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(ServiceForCompnayContarctReqDto.class)).list();
			         
			         
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceForCompnayContarctResDto, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getContractByCompanyId(CompanyMasterDto reqDto) throws ApplicationException {
		try
		{
		List<MCompanyContractMasterDto> contractList = null;
				if(reqDto.getCompanyTypeId() == 3)
				{
					
					 StringBuilder query = new StringBuilder("SELECT "
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
								//+ "	  AND contractmst.company_id =:companyId "
								+ "   AND contractmst.payment_entitlement_id =:companyTypeId "
								+ "	  AND contractmst.status='A' ");
					
					query.append(reqDto.getContractDesc()!=null ? "AND contractmst.contract_name like :contractDesc " : "");
					query.append(reqDto.getCompanyId()!=null ? "AND contractmst.company_id =:companyId " : "");
					query.append(reqDto.getVisitTypeId() != null ? " AND contractmst.visit_type_id =:visitTypeId " : " ");
					query.append(reqDto.getBillingBedCategoryId() != null ? "  AND contractmst.billing_bed_category_id =:billingBedCategoryId " : " ");
					query.append("   AND :date between to_char(contractmst.validity_from,'YYYY-MM-DD') and to_char(contractmst.validity_to,'YYYY-MM-DD')");
					query.append("ORDER BY contractmst.contract_name ASC");

					Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
								.setParameter("orgId", reqDto.getOrganizationId())
								.setParameter("unitId", reqDto.getUnitId())
								.setParameter("companyTypeId", reqDto.getCompanyTypeId())
								.setParameter("date", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()),"yyyy-MM-dd"));
								//.setInteger("companyId", reqDto.getCompanyId())

					if(reqDto.getContractDesc()!=null)
						sqlQuery.setParameter("contractDesc", "%"+reqDto.getContractDesc().toLowerCase()+"%");

					if(reqDto.getCompanyId()!=null)
						sqlQuery.setParameter("companyId", reqDto.getCompanyId());

					if(reqDto.getVisitTypeId()!=null)
						sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());

					if(reqDto.getBillingBedCategoryId()!=null)
						sqlQuery.setParameter("billingBedCategoryId", reqDto.getBillingBedCategoryId());

					contractList = sqlQuery.setResultTransformer(Transformers.aliasToBean(MCompanyContractMasterDto.class)).list();
					
					return new Response(SUCCESS, null, null, contractList, null);
		
					
				}
				else if(reqDto.getCompanyTypeId() == 2)
				{
					/*contractList = sessionFactory.getCurrentSession()
							.getNamedQuery("GET_CONTRACT_BY_COMPANYID_FOR_INSURANCE")
							.setInteger("orgId", reqDto.getOrganizationId())
							.setInteger("unitId", reqDto.getUnitId())
							.setInteger("companyTypeId", reqDto.getCompanyTypeId())
							.setInteger("companyId", reqDto.getCompanyId())
							.setInteger("associateCompanyId", reqDto.getAssociateCompanyId())
							.setInteger("visitTypeId", reqDto.getVisitTypeId())
							.setInteger("billingBedCategoryId", reqDto.getBillingBedCategoryId())
							.setResultTransformer(Transformers.aliasToBean(MCompanyContractMasterDto.class)).list();*/
					
					StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_CONTRACT_BY_COMPANYID_FOR_INSURANCE").getQueryString());
					query.append(reqDto.getVisitTypeId() != null ? " AND contractmst.visit_type_id =:visitTypeId " : " ");
					query.append(reqDto.getBillingBedCategoryId() != null ? "  AND contractmst.billing_bed_category_id =:billingBedCategoryId " : " ");
					query.append("   AND :date between to_char(contractmst.validity_from,'YYYY-MM-DD') and to_char(contractmst.validity_to,'YYYY-MM-DD')");
					query.append("ORDER BY contractmst.contract_name ASC");
					Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							.setParameter("orgId", reqDto.getOrganizationId())
							.setParameter("unitId", reqDto.getUnitId())
							.setParameter("companyTypeId", reqDto.getCompanyTypeId())
							.setParameter("companyId", reqDto.getCompanyId())
							.setParameter("associateCompanyId", reqDto.getAssociateCompanyId())
							.setParameter("date", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()),"yyyy-MM-dd"));

					if(reqDto.getVisitTypeId()!=null)
						sqlQuery.setParameter("visitTypeId", reqDto.getVisitTypeId());

					if(reqDto.getBillingBedCategoryId()!=null)
						sqlQuery.setParameter("billingBedCategoryId", reqDto.getBillingBedCategoryId());

					contractList = sqlQuery.setResultTransformer(Transformers.aliasToBean(MCompanyContractMasterDto.class)).list();
					
					return new Response(SUCCESS, null, null, contractList, null);
				}
				
		}catch (HibernateException he) {
		}catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
		
		
	}


	@Override
	public Response getServiceRateByCompanyId(CompanyMasterDto reqDto) throws ServiceException {
		try {
			List<ContractConsumptionDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CONTARCT_CONSUMPTION_SERVICE_DETAILS_BY_CONTARCTID_SERVICEID")
					.setParameter("contractId",reqDto.getContractId())
					.setParameter("serviceId", reqDto.getServiceId())
					.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
					.list();
			return new  Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getChargeConversionListByCurrentPayer(ChargeConversionDto reqDto) throws ServiceException {
		try {
			List<ChargeConversionDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CHARGE_CONVERSION_LIST_BY_CUREENT_PAYER_PAYEE_ID")
					.setParameter("payeeId",reqDto.getPayeeId())
					.setParameter("encounterId", reqDto.getEncounterId())
					.setResultTransformer(Transformers.aliasToBean(ChargeConversionDto.class))
					.list();
			return new  Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getChargeConversionListByConvertPayer(ChargeConversionDto reqDto) throws ServiceException {
		try {
			List<ChargeConversionDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CHARGE_CONVERSION_LIST_BY_CONVERT_PAYER_CONTARCT_ID")
					.setParameter("contractId",reqDto.getContractId())
					.setParameterList("serviceId", reqDto.getServiceListIds())
					.setResultTransformer(Transformers.aliasToBean(ChargeConversionDto.class))
					.list();
			return new  Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getItemListByAssetAndProductCategory(ItemsForContractDto itemsForContractDto)
			throws ServiceException {
		try {
			
			/*String query="select  "
			 +" item.id as \"itemId\",  "
			 +" ac.id as \"assetCategoryId\",  "
			 +" ac.category as \"assetCategory\",  "
			 +" pc.category as \"productCategory\",  "
			 +" pc.id as \"productCategoryId\",  "
			 +" item.code as \"itemCode\",  "
			 +" item.name as \"itemName\"  "
			 +" from public.m_item as item  "
			 +" INNER JOIN public.m_generic generic ON generic.id = item.generic_id and generic.is_deleted = false  "
			 +" and generic.is_active = true  "
			 +" inner join public.m_product_category pc  on pc.id = generic.product_category_id and pc.is_deleted = false  "
			 +" and pc.is_active = true  "
			 +" inner join public.m_asset_category ac on ac.id = pc.asset_category_id and ac.is_deleted = false  "
			 +" and ac.is_active = true  "
			 +" inner join public.m_manufacturer mf  on mf.id = item.manufacturer_id and  mf.is_deleted = false  "
			 +" and mf.is_active = true  "
			 +" where ac.asset_type_id = 1  "
			 +" and item.is_deleted = false  "
			 +" and item.is_active = true  "
			 +" and item.unit_id=:unitId  "
			 +" and ac.id IN(:assetCategoryList)  "
			 +" and pc.id  IN (:productCategoryList)  "
			 +" order by item.id ";*/
	
	
	String query= "with items as "
	+ "(	 "
	+ " select   "
	+ " item.id as \"itemId\",   "
	+ " (case when item.product_category_id is null then generic.product_category_id else item.product_category_id end) as \"productCategoryId\",   "
	+ " item.code as \"itemCode\",   "
	+ " item.name as \"itemName\"   "
	+ " from public.m_item as item   "
	+ " left JOIN public.m_generic generic ON generic.id = item.generic_id and generic.is_deleted = false   "
	+ " and generic.is_active = true    "
	+ " inner join public.m_manufacturer mf  on mf.id = item.manufacturer_id and  mf.is_deleted = false   "
	+ " and mf.is_active = true   "
	+ " where  "
	+ " "
	+ "  item.is_deleted = false   "
	+ " and item.is_active = true   "
	+ " and item.unit_id=:unitId   "
	+ " order by item.id  "
	+ " ) select it.*, "
	+ "   ac.category as \"assetCategory\", "
	+ "   pc.category as \"productCategory\", ac.id as \"assetCategoryId\" from items it "
	+ "  inner join public.m_product_category pc  on pc.id = it.\"productCategoryId\" and pc.is_deleted = false   "
	+ " and pc.is_active = true   "
	+ " inner join public.m_asset_category ac on ac.id = pc.asset_category_id and ac.is_deleted = false   "
	+ " and ac.is_active = true   "
	+ " where  "
	+ "  ac.asset_type_id = 1   ";

	StringBuffer sb = new StringBuffer(query);	
	if(itemsForContractDto.getAssetCategoryList()!=null && itemsForContractDto.getAssetCategoryList().size()>0)
	{
		sb.append(" and ac.id IN(:assetCategoryList)   ");
//		sb.append(" and ac.id IN("+GlobalCommonDateUtils.convertToStringJoiner(itemsForContractDto.getAssetCategoryList())+")   ");
	}
	if(itemsForContractDto.getProductCategoryList()!=null && itemsForContractDto.getProductCategoryList().size()>0)
	{
		sb.append( " and pc.id  IN (:productCategoryList)");
	}
	if(itemsForContractDto.getItemName()!=null)
	{
		sb.append( " and  lower(it.\"itemName\") like lower(:itemName)");
	}
	if(itemsForContractDto.getItemCode()!=null)
	{
		sb.append( " and lower(it.\"itemCode\") like lower(:itemCode)");
	}
	sb.append(" order by it.\"itemId\" ");
			Query sqlQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setInteger("unitId", itemsForContractDto.getUnitId());

			if(itemsForContractDto.getAssetCategoryList()!=null && !itemsForContractDto.getAssetCategoryList().isEmpty())
				sqlQuery.setParameterList("assetCategoryList", itemsForContractDto.getAssetCategoryList());

			if(itemsForContractDto.getProductCategoryList()!=null && !itemsForContractDto.getProductCategoryList().isEmpty())
				sqlQuery.setParameterList("productCategoryList", itemsForContractDto.getProductCategoryList());

			if(itemsForContractDto.getItemName()!=null)
				sqlQuery.setParameter("itemName", "%" + itemsForContractDto.getItemName().toLowerCase() + "%");

			if(itemsForContractDto.getItemCode()!=null)
				sqlQuery.setParameter("itemCode", "%" + itemsForContractDto.getItemCode().toLowerCase() + "%");


			List<ItemsForContractDto> list  = sqlQuery.setResultTransformer(Transformers.aliasToBean(ItemsForContractDto.class))
					.list();
			return new  Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getProductAssetInclusionList(ItemsForContractDto itemsForContractDto) throws ServiceException {
		try {
			
			String query="select  "
					 +" ac.id as \"assetCategoryId\",  "
					 +" ac.category as \"assetCategory\",  "
					 +" pc.category as \"productCategory\",  "
					 +" pc.id as \"productCategoryId\"  "
					 +" From public.m_product_category pc "
					 +" inner join public.m_asset_category ac on ac.id = pc.asset_category_id and ac.is_deleted = false  "
					 +" and ac.is_active = true  "
					 +" where ac.asset_type_id = 1  "
					 +" and pc.is_deleted = false  "
					 +" and pc.is_active = true  "
					 +" and pc.unit_id=:unitId  "
					 +" and ac.id NOT IN (:assetCategoryList)  "
					 +" and pc.id  NOT IN (:productCategoryList)  "
					 +" order by ac.id ";
			List<ItemsForContractDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameterList("productCategoryList",itemsForContractDto.getProductCategoryList())
					.setParameterList("assetCategoryList", itemsForContractDto.getAssetCategoryList())
					.setParameter("unitId", itemsForContractDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ItemsForContractDto.class))
					.list();
			return new  Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getInclusionItemList(TContractGroupPharmacyDetailsDto contractGroupPharmacyExclusionDto,Set<Integer> excludeProdCatList)
			throws ServiceException {
try {
	
	
	String query= "with items as "
	+ "(	 "
	+ " select   "
	+ " item.id as \"itemId\",   "
	+ " (case when item.product_category_id is null then generic.product_category_id else item.product_category_id end) as \"productCategoryId\",   "
	+ " item.code as \"itemCode\",   "
	+ " item.name as \"itemName\"   "
	+ " from public.m_item as item   "
	+ " left JOIN public.m_generic generic ON generic.id = item.generic_id and generic.is_deleted = false   "
	+ " and generic.is_active = true    "
	+ " inner join public.m_manufacturer mf  on mf.id = item.manufacturer_id and  mf.is_deleted = false   "
	+ " and mf.is_active = true   "
	+ " where  "
	+ "  item.is_deleted = false   "
	+ " and item.is_active = true   "
	+ " and item.unit_id=:unitId   "
	+ " order by item.id  "
	+ " ) select it.*, "
	+ "   ac.category as \"assetCategory\", "
	+ "   pc.category as \"productCategory\", ac.id as \"assetCategoryId\" from items it "
	+ "  inner join public.m_product_category pc  on pc.id = it.\"productCategoryId\" and pc.is_deleted = false   "
	+ " and pc.is_active = true   "
	+ " inner join public.m_asset_category ac on ac.id = pc.asset_category_id and ac.is_deleted = false   "
	+ " and ac.is_active = true   "
	+ " where  "
	+ "  ac.asset_type_id = 1   "
	+ " and pc.id  IN (:productCategoryList) "
	+ " and pc.id NOT IN (:excludeProdCatList) "
	+ " order by it.\"itemId\"   ";
	
			/*
			 * String query="select  " +" item.id as \"itemId\",  "
			 * +" pc.id as \"productCategoryId\" " +" from public.m_item as item  "
			 * +" INNER JOIN public.m_generic generic ON generic.id = item.generic_id and generic.is_deleted = false  "
			 * +" and generic.is_active = true  "
			 * +" inner join public.m_product_category pc  on pc.id = generic.product_category_id and pc.is_deleted = false  "
			 * +" and pc.is_active = true  "
			 * +" inner join public.m_asset_category ac on ac.id = pc.asset_category_id and ac.is_deleted = false  "
			 * +" and ac.is_active = true  "
			 * +" inner join public.m_manufacturer mf  on mf.id = item.manufacturer_id and  mf.is_deleted = false  "
			 * +" and mf.is_active = true  " +" where ac.asset_type_id = 1  "
			 * +" and item.is_deleted = false  " +" and item.is_active = true  "
			 * +" and item.unit_id=:unitId  " //+" and ac.id NOT IN(:assetCategoryList)  "
			 * +" and pc.id NOT IN (:productCategoryList)  " +" order by item.id ";
			 */
			List<ItemsForContractDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameterList("productCategoryList",contractGroupPharmacyExclusionDto.getProductCategoryList())
					.setParameterList("excludeProdCatList", (excludeProdCatList!=null && excludeProdCatList.size()>0)?excludeProdCatList:Arrays.asList(0))
					.setParameter("unitId", contractGroupPharmacyExclusionDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ItemsForContractDto.class))
					.list();
			return new  Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
}
public Response getItemListByProductCategoryForEquipment(ItemsForContractDto itemsForContractDto)throws ServiceException {
	try {
		
		String query="select  "
					 +" item.id as \"itemId\",  "
					 +" pc.category as \"productCategory\",  "
					 +" pc.id as \"productCategoryId\",  "
					 +" item.code as \"itemCode\",  "
					 +" item.name as \"itemName\"  "
					 +" from public.m_item as item  "
					 +" inner join public.m_product_category pc  on pc.id = item.product_category_id and pc.is_deleted = false  "
					 +" and pc.is_active = true  "
					 +" where item.is_deleted = false  "
					 +" and item.is_active = true  "
					 +" and item.unit_id=:unitId  "
					 +" and pc.id  =:productCategoryId  "
					 +" and item.is_chargeable_equpiment = true "
					 +" order by item.id ";
			List<ChargeConversionDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("productCategoryId",itemsForContractDto.getProductCategoryId())
					.setParameter("unitId", itemsForContractDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ItemsForContractDto.class))
					.list();
			return new  Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}


@Override
public List<TContractServiceDetailsDto> getTariffRatesForContract(MCompanyContractMasterDto contractMasterDto)
		throws ServiceException {
try {
		
	String pattern = "yyyy-MM-dd";
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

	String date = simpleDateFormat.format(TimeZoneComponent.getDateByzone(contractMasterDto.getUnitId()));
	
		String query=" select "
				 + " 	distinct(ustm.service_master_id) as \"serviceId\", "
				 + " 	ustm.base_price as \"servicePrice\", "
				 + " 	ustm.base_price as \"apportionedPrice\", "
				 + " 	ustm.base_price +(ustm.base_price*(coalesce(tax.tax_percentage, 0)* 0.01)) as \"finalPrice\", "
				 + " 	(ustm.base_price +(ustm.base_price*(coalesce(tax.tax_percentage, 0)* 0.01))) - ((ustm.base_price +(ustm.base_price*(coalesce(tax.tax_percentage, 0) * 0.01))) * (:coShare*0.01)) as \"creditPayable\", "
				 + " 	((ustm.base_price +(ustm.base_price*(coalesce(tax.tax_percentage, 0)* 0.01))) * (:coShare*0.01)) as \"selfPayable\" "
				 + " from "
				 + " 	service.m_service_base_price_master ustm "
				 + " inner join service.t_unit_service_master_details usmd on "
				 + " 	usmd.service_master_id = ustm.service_master_id "
				 + " left join m_tax tax on "
				 + " 	tax.id = usmd.gst_type_id "
				 + " where "
				 + " 	usmd.unit_id = ustm.unit_id "
				 + " 	and ustm.organisation_id =:organizationId "
				 + " 	and ustm.unit_id =:unitId "
				 + " 	and '"+date+"' between ustm.from_date and ustm.to_date ";
		List<TContractServiceDetailsDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("organizationId",contractMasterDto.getOrgId())
					.setParameter("unitId", contractMasterDto.getUnitId())
					.setParameter("coShare", contractMasterDto.getCoSharePercentage())
					.setResultTransformer(Transformers.aliasToBean(TContractServiceDetailsDto.class))
					.list();
			return list;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
}


@Override
public Response getCompanyContractDetailByContractId(CompanyContractReqDto companyContractDto) throws ServiceException {
	try
	{
		List<TContractServiceDetailsDto> servicelist = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_COMPANY_CONTRACT_SERVICE_DETAIL_BY_CONTRACT_ID")
			    .setParameter("contractId",companyContractDto.getContractId())
				.setParameter("orgId", companyContractDto.getOrgId())
				.setParameter("unitId", companyContractDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(TContractServiceDetailsDto.class))
				.list();
		
		List<TContractServiceDetailsDto> packagelist = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_COMPANY_CONTRACT_PACKAGE_DETAIL_BY_CONTRACT_ID")
			    .setParameter("contractId",companyContractDto.getContractId())
				.setParameter("orgId", companyContractDto.getOrgId())
				.setParameter("unitId", companyContractDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(TContractServiceDetailsDto.class))
				.list();
		
		
		List<TContractBedCategoryDetailDto> bedCategoryList = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_COMPANY_CONTRACT_BILLING_BED_CATEGORY_BY_CONTRACT_ID")
			    .setParameter("contractId",companyContractDto.getContractId())
				.setParameter("orgId", companyContractDto.getOrgId())
				.setParameter("unitId", companyContractDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(TContractBedCategoryDetailDto.class))
				.list();
		
		List<TContractProcedureDetailsDto> procedureList = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_COMPANY_CONTRACT_PROCEDURE_DETAIL_BY_CONTRACT_ID")
			    .setParameter("contractId",companyContractDto.getContractId())
				.setParameter("orgId", companyContractDto.getOrgId())
				.setParameter("unitId", companyContractDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(TContractProcedureDetailsDto.class))
				.list();
		
		List<TContractCapDetailsDto> capDetailList = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_COMPANY_CONTRACT_CAP_DETAIL_LIST_BY_CONTRACT_ID")
			    .setParameter("contractId",companyContractDto.getContractId())
				.setParameter("orgId", companyContractDto.getOrgId())
				.setParameter("unitId", companyContractDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(TContractCapDetailsDto.class))
				.list();
		
		List<TContractCapDetailsDto> serviceCapDetailList = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_COMPANY_CONTRACT_SERVICE_CAP_DETAIL_BY_CONTRACT_ID")
			    .setParameter("contractId",companyContractDto.getContractId())
				.setParameter("orgId", companyContractDto.getOrgId())
				.setParameter("unitId", companyContractDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(TContractCapDetailsDto.class))
				.list();
				
		List<ContractProductCategoryDto> capProductList = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_COMPANY_CONTRACT_PRODUCT_CAP_DETAIL_LIST_BY_CONTRACT_ID")
			    .setParameter("contractId",companyContractDto.getContractId())
				.setParameter("orgId", companyContractDto.getOrgId())
				.setParameter("unitId", companyContractDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(ContractProductCategoryDto.class))
				.list();
		
		List<TContractGroupDetailsDto> groupDetails = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_COMPANY_CONTRACT_GROP_DETAILS_BY_CONTRACT_ID")
			    .setParameter("contractId",companyContractDto.getContractId())
				.setParameter("unitId", companyContractDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(TContractGroupDetailsDto.class))
				.list();
		
		List<TContractGroupDetailsDto> groupExclusionDetails = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_COMPANY_CONTRACT_GROP_EXCLUSION_BY_CONTRACT_ID")
			    .setParameter("contractId",companyContractDto.getContractId())
				.setParameter("unitId", companyContractDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(TContractGroupDetailsDto.class))
				.list();
		
		List<TContractGroupPharmacyDetailsDto> itemList = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_CONTRACT_ITEM_INCLUDED_AFTER_PHARMACY_EXCLUSION_BY_CONTRACT_ID")
			    .setParameter("contractId",companyContractDto.getContractId())
				.setParameter("unitId", companyContractDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(TContractGroupPharmacyDetailsDto.class))
				.list();
		
		List<TContractGroupPharmacyDetailsDto> pharmacyExclusion = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_COMPANY_CONTRACT_PHARMACY_EXCLUSION_BY_CONTRACT_ID")
			    .setParameter("contractId",companyContractDto.getContractId())
				.setParameter("unitId", companyContractDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(TContractGroupPharmacyDetailsDto.class))
				.list();
		
		companyContractDto.setListTContractPackageDetailsDto(packagelist);
		companyContractDto.setListContractServiceWiseCapDetails(serviceCapDetailList);
		companyContractDto.setListTContractServiceDetailsDto(servicelist);
		companyContractDto.setListTContractBedCategoryDetailDto(bedCategoryList);
		companyContractDto.setProcedureDetailsDtosList(procedureList);
		companyContractDto.setListTContractCapDetailsDto(capDetailList);
		companyContractDto.setContractCapProductList(capProductList);
		companyContractDto.setListContractGroupExclusion(groupExclusionDetails);
		companyContractDto.setListContractGroupWiseDetails(groupDetails);
		companyContractDto.setGroupPharmacyExclusionDetailsList(pharmacyExclusion);
		companyContractDto.setItemList(itemList);
		return new  Response<>(SUCCESS, SUCCESS_CODE, null, null, companyContractDto);
	}catch(Exception e){
		
		return new  Response<>(ERROR, ERROR_CODE, null, null, null);
	}
}


@Override
public Response updateStatus(Integer contractId, Character status,  Character isVersioning,Integer parConId) throws ServiceException {
	try {
			if(parConId != null && parConId > 0) {
				sessionFactory.getCurrentSession().createQuery("UPDATE MCompanyContractMaster set status=:status,is_versioning=:isVersioning,parent_contract_id =:parConId where contractId=:contractId ")
				.setParameter("contractId",contractId)
				.setParameter("status", status)
				.setParameter("isVersioning", isVersioning)
				.setParameter("parConId",parConId)
				.executeUpdate();
				return new  Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}else {
				sessionFactory.getCurrentSession().createQuery("UPDATE MCompanyContractMaster set status=:status,is_versioning=:isVersioning where contractId=:contractId ")
				.setParameter("contractId",contractId)
				.setParameter("status", status)
				.setParameter("isVersioning", isVersioning)
				.executeUpdate();
				return new  Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}
			
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
}


@Override
public Response searchCompanyContractForVersion(ServiceForCompnayContarctReqDto reqDto) throws ServiceException {
	try{
		   StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("SEARCH_FOR_COMPANY_CONTRACT_FOR_VERSION").getQueryString());
			query.append(reqDto.getParentContractId() != null ? " and contractmst.parent_contract_id =:parentContractId " : " ");
			query.append(" and is_versioning='Y' "); 
			query.append(" order by contractmst.contract_id asc ");  
		         Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						 .setParameter("orgId", reqDto.getOrgId())
						 .setParameter("orgUnitId", reqDto.getUnitId());

			if(reqDto.getParentContractId() != null)
				sqlQuery.setParameter("parentContractId", reqDto.getParentContractId());

		List<ServiceForCompnayContarctReqDto> listServiceForCompnayContarctResDto = sqlQuery
				.setResultTransformer(Transformers.aliasToBean(ServiceForCompnayContarctReqDto.class)).list();

		         Session session = sessionFactory.getCurrentSession();
		         List<ServiceForCompnayContarctReqDto> listServiceForCompnayCount=  sqlQuery
						 .setParameter("orgId", reqDto.getOrgId())
						 .setParameter("orgUnitId", reqDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(ServiceForCompnayContarctReqDto.class)).list();
			        
		         
		return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceForCompnayContarctResDto, listServiceForCompnayCount.size());
	}catch(Exception e){
		
		throw new ServiceException(e.getMessage());
	}
}


@Override
public Response getContractDetailsBySubDept(TContractGroupDetailsDto reqDto) throws ServiceException {
try {
		
		String query= " select " 
				 + " 	mccm.contract_id as \"contractId\", " 
				 + " 	mccm.contract_name as \"contractName\" , " 
				 + " 	tcgd.co_share_percentage as \"coShare\", " 
				 + " 	:rate as \"rate\", " 
				 + " 	:rate as \"apportionedPrice\", " 
				 + " 	:serviceId as \"serviceId\", " 
				 + " 	coalesce(mt.tax_percentage, 0) as \"taxPercentage\", " 
				 + " 	:rate + (:rate*(coalesce(mt.tax_percentage, 0)* 0.01)) as \"finalRate\", " 
				 + " 	(:rate + (:rate*(coalesce(mt.tax_percentage, 0)* 0.01))) * (tcgd.co_share_percentage*0.01) as \"selfPayable\", " 
				 + " 	(:rate + (:rate*(coalesce(mt.tax_percentage, 0)* 0.01))) * ((100-tcgd.co_share_percentage)* 0.01) as \"creditPayable\" " 
				 + " from " 
				 + " 	service.t_contract_group_details tcgd " 
				 + " inner join service.m_company_contract_master mccm on " 
				 + " 	tcgd.contract_id = mccm.contract_id " 
				 + " left join m_tax mt on " 
				 + " 	mt.id =:taxId "
				 + " where " 
				 + " 	tcgd.sub_department_id =:subDeptId " 
				 + " 	and tcgd.unit_id =:unitId " 
				 + " 	and mccm.status = 'A' " 
				 + " 	and mccm.contract_id not in ( " 
				 + " 	select " 
				 + " 		distinct tcsd.contract_id " 
				 + " 	from " 
				 + " 		service.t_contract_service_details tcsd " 
				 + " 	where " 
				 + " 		service_id = :serviceId " 
				 + " 		and unit_id =:unitId) " 
				 +" ORDER BY mccm.contract_name ASC";
			List<TContractGroupDetailsDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("subDeptId",reqDto.getSubDepartmentId())
					.setParameter("unitId", reqDto.getUnitId())
					.setParameter("rate", reqDto.getRate())
					.setParameter("serviceId", reqDto.getServiceId())
					.setParameter("taxId", reqDto.getTaxId())
					//.setInteger("taxid", reqDto.getTaxId())
					
					.setResultTransformer(Transformers.aliasToBean(TContractGroupDetailsDto.class))
					.list();
			return new  Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
}


	

@Override
public Response getAssociateCompanyMasterByMultipleCompanyIds(CompanyMasterDto companyMasterDto) {
	List<CommonDto>  listAssociateCompanyMasterList = null; 
	try
	{
		if(companyMasterDto.getCompanyIds()!=null) {
			listAssociateCompanyMasterList = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT "
							+"	insu.insurance_id AS id, "
							+"	insu.insurance_desc   AS name "
							+"FROM "
							+"	service.t_insurance_tpa_mapper iMapp "
							+" INNER JOIN adt.t_insurance insu on insu.insurance_id =iMapp.insurance_id "
							+"WHERE "
							+"	iMapp.status = 'A' "
							+"	AND iMapp.organization_id = :orgId "
							+"	AND iMapp.unit_id = :orgUnitId "
							+"	AND iMapp.company_id in (:companyIds) order by insu.insurance_desc asc")
					.setParameterList("companyIds", companyMasterDto.getCompanyIds())
					.setParameter("orgId", companyMasterDto.getOrganizationId())
					.setParameter("orgUnitId", companyMasterDto.getUnitId())
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_CODE, listAssociateCompanyMasterList, null);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	} catch (Exception e)
	{
		logger.error("Exection", e);
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}
}


@Override
public Response getCompanyContarctName(MCompanyContractMasterDto companyContractMasterDto) throws ApplicationException {
	try {
		
		if(companyContractMasterDto.getContractId() == null) {
			List<MCompanyContractMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_COMPANY_CONTRACT_BY_NAME_NOT_ID")
					//.setInteger("unitId", companyContractMasterDto.getUnitId())
					.setParameter("contractName", companyContractMasterDto.getContractName().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(MCompanyContractMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		}
	   return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
	
	} catch (HibernateException he) {
	} catch (Exception e) {
		
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	
}
