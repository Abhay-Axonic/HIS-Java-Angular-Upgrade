package com.param.global.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.param.global.dto.*;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Repository;

import com.param.billing.global.transaction.model.ServiceProfitMargin;
import com.param.billing.global.transaction.model.UnitServiceTariffMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"unchecked","rawtypes"})
public class UnitServiceTariffMasterDaoImpl extends GenericDao<UnitServiceTariffMaster, Integer>implements IUnitServiceTariffMasterDao,ICommonConstants{

	final static Logger logger = Logger.getLogger(UnitServiceTariffMasterDaoImpl.class);

	public UnitServiceTariffMasterDaoImpl() {
		super(UnitServiceTariffMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	HikariDataSource driverManager;

	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Override
	public Response saveUnitServiceTariffMaster(List<UnitServiceTariffMasterDto> unitServiceTariffMasterDtoList)
			throws ApplicationContextException {
		Connection connection =null;
		PreparedStatement statement = null;
		try {
			int count=0;
			connection = driverManager.getConnection();
			connection.setAutoCommit(false);
			 statement = connection.prepareStatement("INSERT INTO service.m_unit_service_tariff_master "
	        		+ "(organization_id, unit_id, service_id, tariff_id, payment_entitlement_id, visit_type_id, patient_type_id, billing_bed_category_id,rate,stat_per,effective_date,service_code, created_by, updated_by, created_date, updated_date)"
	        		+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
			 int i = 0;
			 for(UnitServiceTariffMasterDto dto:unitServiceTariffMasterDtoList)
			 {
				 java.util.Date createdUtilDate = TimeZoneComponent.getDateByzone(dto.getUnitId());
				 Timestamp timestamp = new Timestamp(createdUtilDate.getTime());

				 statement.setInt(1, dto.getOrganizationId());
				 statement.setInt(2, dto.getUnitId());
				 statement.setInt(3, dto.getServiceId());
				 statement.setInt(4, dto.getTariffId());
				 statement.setInt(5, dto.getPaymentEntitlementId());
				 statement.setInt(6, dto.getVisitTypeId());
				 statement.setInt(7, dto.getPatientTypeId());
				 statement.setInt(8, dto.getBillingBedCatId());
				 statement.setDouble(9, dto.getRate());
				 statement.setDouble(10, (dto.getStatPer()!=null ? Double.parseDouble(dto.getStatPer()) : 0.00));
				 statement.setDate(11, new java.sql.Date( GlobalCommonDateUtils.getDate(dto.getEffectiveDate(),"yyyy-M-dd").getTime()));
				 statement.setString(12, dto.getServiceCode());
				 statement.setInt(13, dto.getCreatedBy()!=null ? dto.getCreatedBy() : null);
				 statement.setInt(14, dto.getUpdatedBy() != null ? dto.getUpdatedBy() : null);
				 statement.setTimestamp(15,  timestamp);
				 statement.setTimestamp(16,  timestamp);
				  statement.addBatch();
				  i++;

		            if (i % 200 == 0 || i == unitServiceTariffMasterDtoList.size()) {
		                statement.executeBatch(); // Execute every 20 items.
		                connection.commit();  
		            }
			 }
			 return new Response(SUCCESS, null, null, null, null);
		}catch (Exception e) {
			logger.error("saveUnitServiceTariffMaster failed", e);
			throw new ServiceException(e.getMessage());
		}finally{
			try{
				if (statement != null) {
					statement.close();
				}
				connection.setAutoCommit(true);
				connection.close();
			}catch(Exception e){
				logger.error("saveUnitServiceTariffMaster cleanup failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	}

	@Override
	public Response getUnitServiceTariffsUniqueness(UnitServiceTariffMasterDto unitServiceTariffMasterDto)
			throws ApplicationContextException {
		try {

			Query query = sessionFactory.getCurrentSession().createSQLQuery(
					"SELECT unitserviceTariff.unit_service_tariff_id AS \"unitServiceTriffId\" " +
							"FROM service.m_unit_service_tariff_master unitserviceTariff " +
							"WHERE unitserviceTariff.service_id = :serviceId " +
							"AND unitserviceTariff.tariff_id = :tariffId " +
							"AND unitserviceTariff.payment_entitlement_id = :paymentEntitlementId " +
							"AND unitserviceTariff.visit_type_id = :visitTypeId " +
							"AND unitserviceTariff.patient_type_id = :patientTypeId " +
							"AND unitserviceTariff.billing_bed_category_id = :billingBedCatId " +
							"AND unitserviceTariff.organization_id = :organizationId " +
							"AND unitserviceTariff.unit_id = :unitId " +
							"AND unitserviceTariff.effective_date = :effectiveDate"
			);

			query.setParameter("serviceId", unitServiceTariffMasterDto.getServiceId())
					.setParameter("tariffId", unitServiceTariffMasterDto.getTariffId())
					.setParameter("paymentEntitlementId", unitServiceTariffMasterDto.getPaymentEntitlementId())
					.setParameter("visitTypeId", unitServiceTariffMasterDto.getVisitTypeId())
					.setParameter("patientTypeId", unitServiceTariffMasterDto.getPatientTypeId())
					.setParameter("billingBedCatId", unitServiceTariffMasterDto.getBillingBedCatId())
					.setParameter("organizationId", unitServiceTariffMasterDto.getOrganizationId())
					.setParameter("unitId", unitServiceTariffMasterDto.getUnitId())
					.setParameter("effectiveDate", unitServiceTariffMasterDto.getEffectiveDate());

			List<UnitServiceTariffMasterDto> areaMasterDtoList = query
					.setResultTransformer(Transformers.aliasToBean(UnitServiceTariffMasterDto.class))
					.list();


			return new Response(SUCCESS, null, null, areaMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getUnitServiceTariffsUniqueness failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getUnitServiceTariffsUniqueness failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<UnitServiceTariffMasterDto> getBasePriceByServiceTariffMaster(UnitServiceTariffMasterDto unitServiceTariffMasterDto)
			throws ApplicationContextException {
		try {
			
			Query query = sessionFactory.getCurrentSession().createQuery("SELECT ustm.unitServiceTriffId as unitServiceTriffId, "
			+ "ustm.rate as rate, "
			+ "to_char(ustm.effectiveDate,'yyyy-mm-dd') as effectiveDate " 
			+ "FROM UnitServiceTariffMaster ustm "
			+ "WHERE ustm.serviceId=:serviceId "
			+ "AND ustm.tariffId=:tariffId "
			+ "AND ustm.paymentEntitlementId=:paymentEntitlementId "
			+ "AND ustm.visitTypeId=:visitTypeId "
			+ "AND ustm.patientTypeId=:patientTypeId "
			+ "AND ustm.billingBedCategoryId=:billingBedCategoryId "
			+ "AND ustm.organizationId=:organizationId "
			+ "AND ustm.unitId=:unitId "
			+ "AND ustm.effectiveDate < '"+unitServiceTariffMasterDto.getOrderDate()+"'"
			+ "ORDER BY ustm.effectiveDate DESC ");
			
			List<UnitServiceTariffMasterDto> unitServiceTariffMasterDtos = query
					.setParameter("serviceId", unitServiceTariffMasterDto.getServiceId())
					.setParameter("tariffId", unitServiceTariffMasterDto.getTariffId())
					.setParameter("paymentEntitlementId", unitServiceTariffMasterDto.getPaymentEntitlementId())
					.setParameter("visitTypeId", unitServiceTariffMasterDto.getVisitTypeId())
					.setParameter("patientTypeId", unitServiceTariffMasterDto.getPatientTypeId())
					.setParameter("billingBedCategoryId", unitServiceTariffMasterDto.getBillingBedCategoryId())
					.setParameter("organizationId", unitServiceTariffMasterDto.getOrganizationId())
					.setParameter("unitId", unitServiceTariffMasterDto.getUnitId())
					//.setDate("orderDate", GlobalCommonDateUtils.getDate(,"yyyy-mm-dd"))
					.setResultTransformer(Transformers.aliasToBean(UnitServiceTariffMasterDto.class)).list();
			
			return unitServiceTariffMasterDtos;

		} catch (HibernateException he) {
			logger.error("getBasePriceByServiceTariffMaster failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getBasePriceByServiceTariffMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<UnitServiceTariffMasterDto> getBasePriceOfServicesFromTariffMasterByServiceList(
			UnitServiceTariffMasterDto unitServiceTariffMasterDto) throws ApplicationContextException {
		try {
			
			Query query = sessionFactory.getCurrentSession().createQuery("SELECT ustm.unitServiceTriffId as unitServiceTriffId, "
			+ "ustm.rate as rate, " 
			+ "to_char(ustm.effectiveDate,'yyyy-mm-dd') as effectiveDate " 
			+ "FROM UnitServiceTariffMaster ustm "
			+ "WHERE ustm.organizationId=:organizationId "
			+ "AND ustm.unitId=:unitId "
			+ "AND ustm.tariffId=:tariffId "
			+ "AND ustm.paymentEntitlementId=:paymentEntitlementId "
			+ "AND ustm.visitTypeId=:visitTypeId "
			+ "AND ustm.patientTypeId=:patientTypeId "
			+ "AND ustm.billingBedCategoryId=:billingBedCategoryId "
			+ "AND ustm.effectiveDate < '"+unitServiceTariffMasterDto.getOrderDate()+"' "
			+ "AND ustm.serviceId IN (:serviceIdList) ");
			
			List<UnitServiceTariffMasterDto> unitServiceTariffMasterDtos = query
					.setParameterList("serviceIdList", unitServiceTariffMasterDto.getServiceList())
					.setParameter("tariffId", unitServiceTariffMasterDto.getTariffId())
					.setParameter("paymentEntitlementId", unitServiceTariffMasterDto.getPaymentEntitlementId())
					.setParameter("visitTypeId", unitServiceTariffMasterDto.getVisitTypeId())
					.setParameter("patientTypeId", unitServiceTariffMasterDto.getPatientTypeId())
					.setParameter("billingBedCategoryId", unitServiceTariffMasterDto.getBillingBedCategoryId())
					.setParameter("organizationId", unitServiceTariffMasterDto.getOrganizationId())
					.setParameter("unitId", unitServiceTariffMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(UnitServiceTariffMasterDto.class)).list();
			
			

			return unitServiceTariffMasterDtos;

		} catch (HibernateException he) {
			logger.error("getBasePriceOfServicesFromTariffMasterByServiceList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getBasePriceOfServicesFromTariffMasterByServiceList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getUnitServiceTariffMasterList(UnitServiceTariffMasterDto unitServiceTariffMasterDto)
			throws ApplicationContextException {
		try {

			// Build the base query using the NamedQuery
			StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_UNIT_SERVICE_TARIFF_MASTER_LIST")
					.getQueryString());

// Dynamically add conditions to the query based on available lists in the DTO
			if (unitServiceTariffMasterDto.getTariffMasterIdList() != null)
				stringQuery.append(" AND ustm.tariffId IN (:tariffMasterIdList) ");

			if (unitServiceTariffMasterDto.getVisitTypeIdList() != null)
				stringQuery.append(" AND ustm.visitTypeId IN (:visitTypeIdList) ");

			if (unitServiceTariffMasterDto.getPatientTypeIdList() != null)
				stringQuery.append(" AND ustm.patientTypeId IN (:patientTypeIdList) ");

			if (unitServiceTariffMasterDto.getPaymentEntitlementIdList() != null)
				stringQuery.append(" AND ustm.paymentEntitlementId IN (:paymentEntitlementIdList) ");

			if (unitServiceTariffMasterDto.getBedCategoryIdList() != null)
				stringQuery.append(" AND ustm.billingBedCategoryId IN (:billingBedCategoryIdList) ");

			if (unitServiceTariffMasterDto.getServiceList() != null)
				stringQuery.append(" AND ustm.serviceId IN (:serviceIdList) ");

// Create the query with the dynamically constructed string
			Query query = sessionFactory.getCurrentSession().createQuery(stringQuery.toString());

// Set the parameters if the corresponding lists are provided in the DTO
			if (unitServiceTariffMasterDto.getTariffMasterIdList() != null)
				query.setParameterList("tariffMasterIdList", unitServiceTariffMasterDto.getTariffMasterIdList());

			if (unitServiceTariffMasterDto.getVisitTypeIdList() != null)
				query.setParameterList("visitTypeIdList", unitServiceTariffMasterDto.getVisitTypeIdList());

			if (unitServiceTariffMasterDto.getPatientTypeIdList() != null)
				query.setParameterList("patientTypeIdList", unitServiceTariffMasterDto.getPatientTypeIdList());

			if (unitServiceTariffMasterDto.getPaymentEntitlementIdList() != null)
				query.setParameterList("paymentEntitlementIdList", unitServiceTariffMasterDto.getPaymentEntitlementIdList());

			if (unitServiceTariffMasterDto.getBedCategoryIdList() != null)
				query.setParameterList("billingBedCategoryIdList", unitServiceTariffMasterDto.getBedCategoryIdList());

			if (unitServiceTariffMasterDto.getServiceList() != null)
				query.setParameterList("serviceIdList", unitServiceTariffMasterDto.getServiceList());

// Set the mandatory parameters for organizationId and unitId
			    query.setParameter("organizationId", unitServiceTariffMasterDto.getOrganizationId())
					.setParameter("unitId", unitServiceTariffMasterDto.getUnitId());

// Execute the query and map the result to DTO
			List<UnitServiceTariffMasterDto> unitServiceTariffMasterDtosList = query
					.setResultTransformer(Transformers.aliasToBean(UnitServiceTariffMasterDto.class))
					.list();


			return new Response(SUCCESS, null, null, unitServiceTariffMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getUnitServiceTariffMasterList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getUnitServiceTariffMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getProfitMarginList(UnitServiceTariffMasterDto unitServiceTariffMasterDto) {
try {
			
	StringBuilder stringQuery1 = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_UNIT_PROFIT_MARGIN_LIST").getQueryString());
	
//	if(unitServiceTariffMasterDto.getTariffMasterIdList()!=null)
	//	stringQuery.append(" AND ustm.tariffId IN (:tariffMasterIdList) ");
	
	if(unitServiceTariffMasterDto.getVisitTypeIdList()!=null)
		stringQuery1.append(" AND ustm.visit_type_id IN (:visitTypeList) " );
	
	if(unitServiceTariffMasterDto.getPatientTypeIdList()!=null)
		stringQuery1.append(" AND ustm.patient_type_id IN (:patientTypeIdList) ");
	
	if(unitServiceTariffMasterDto.getPaymentEntitlementIdList()!=null)
		stringQuery1.append(" AND ustm.payment_entitlement_id IN (:paymentEntitlementIdList)");
	
	if(unitServiceTariffMasterDto.getBedCategoryIdList()!=null)
		stringQuery1.append(" AND ustm.billing_bed_category_id IN (:billingBedCategogyIdList)");
	
	if(unitServiceTariffMasterDto.getServiceList()!=null)
		stringQuery1.append(" AND ustm.service_id IN (:serviceIdList)" );
	
	Query query1 = sessionFactory.getCurrentSession().createSQLQuery(stringQuery1.toString());
			
	//if(unitServiceTariffMasterDto.getTariffMasterIdList()!=null)
	//	query.setParameterList("tariffMasterIdList", unitServiceTariffMasterDto.getTariffMasterIdList());
	
	if(unitServiceTariffMasterDto.getVisitTypeIdList()!=null)
		query1.setParameterList("visitTypeList", unitServiceTariffMasterDto.getVisitTypeIdList());
	
	if(unitServiceTariffMasterDto.getPatientTypeIdList()!=null)
		query1.setParameterList("patientTypeIdList", unitServiceTariffMasterDto.getPatientTypeIdList());
	
	if(unitServiceTariffMasterDto.getPaymentEntitlementIdList()!=null)
		query1.setParameterList("paymentEntitlementIdList", unitServiceTariffMasterDto.getPaymentEntitlementIdList());
	
	if(unitServiceTariffMasterDto.getBedCategoryIdList()!=null)
		query1.setParameterList("billingBedCategogyIdList", unitServiceTariffMasterDto.getBedCategoryIdList());
	
	if(unitServiceTariffMasterDto.getServiceList()!=null)
		query1.setParameterList("serviceIdList", unitServiceTariffMasterDto.getServiceList());
	
	
			List<UnitServiceTariffMasterDto> unitServiceTariffMasterDtosList = query1
					.setParameter("organizationId",unitServiceTariffMasterDto.getOrganizationId())
					.setParameter("unitId", unitServiceTariffMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(UnitServiceTariffMasterDto.class)).list();
			
			

			return new Response(SUCCESS, null, null, unitServiceTariffMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getProfitMarginList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getProfitMarginList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveServiceProfitMargin(List<ServiceProfitMarginDto> serviceProfitMarginDto) {
		try {
			List<ServiceProfitMarginDto> serviceProfitMarginDt = new ArrayList<ServiceProfitMarginDto>();
		
			for(ServiceProfitMarginDto serviceProfitMarginDtList : serviceProfitMarginDto) {
			

				ServiceProfitMargin serviceProfitMargin = new ServiceProfitMargin();
				
				serviceProfitMarginDtList.setCreatedBy(serviceProfitMargin.getCreatedBy());
				serviceProfitMarginDtList.setCreatedDate(GlobalCommonDateUtils.getStringDate(serviceProfitMargin.getCreatedDate(),"yyyy-mm-dd"));
				serviceProfitMarginDtList.setUpdatedDate(GlobalCommonDateUtils.getStringDate(serviceProfitMargin.getUpdatedDate(),"yyyy-mm-dd"));
				serviceProfitMarginDtList.setBillingBedCategoryId(serviceProfitMargin.getBillingBedCategoryId());
				serviceProfitMarginDtList.setCostofservice(serviceProfitMargin.getCostofservice());
				serviceProfitMarginDtList.setOrganizationId(serviceProfitMargin.getOrganizationId());
				serviceProfitMarginDtList.setPatientTypeId(serviceProfitMargin.getPatientTypeId());
				serviceProfitMarginDtList.setPaymentEntitlementId(serviceProfitMargin.getPaymentEntitlementId());
				serviceProfitMarginDtList.setVisitTypeId(serviceProfitMargin.getVisitTypeId());
				serviceProfitMarginDtList.setPrice(serviceProfitMargin.getPrice());
				serviceProfitMarginDtList.setProfit(serviceProfitMargin.getProfit());
				serviceProfitMarginDtList.setUnitId(serviceProfitMargin.getUnitId());
				sessionFactory.getCurrentSession().save(serviceProfitMargin);
	
				
			}
					

		return new Response(SUCCESS, SUCCESS_CODE, null, serviceProfitMarginDt, null);

		} catch (HibernateException he) {
			logger.error("saveServiceProfitMargin failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveServiceProfitMargin failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getServiceListBySubSpecialityId(UnitServiceTariffMasterDto unitServiceTariffMasterDto)
			throws ApplicationContextException {
		try{
			List<UnitServiceTariffMasterDto> unitServiceTariffMasterDtosList = sessionFactory.getCurrentSession().getNamedQuery("GET_SERVICELIST_BY_SUBSPECIALITYID")
					.setParameter("organizationId",unitServiceTariffMasterDto.getOrganizationId())
					.setParameter("unitId", unitServiceTariffMasterDto.getUnitId())
					.setParameter("tariffId", unitServiceTariffMasterDto.getTariffId())
					.setParameter("patientTypeId", unitServiceTariffMasterDto.getPatientTypeId())
					.setParameter("visitTypeId", unitServiceTariffMasterDto.getVisitTypeId())
					.setParameter("billingBedCategoryId", unitServiceTariffMasterDto.getBillingBedCategoryId())
					.setParameter("paymentEntitlementId", unitServiceTariffMasterDto.getPaymentEntitlementId())
					.setDate("effectiveDate", GlobalCommonDateUtils.getDate(unitServiceTariffMasterDto.getEffectiveDate(),"yyyy-M-dd"))
					.setParameterList("subSpecialityList", unitServiceTariffMasterDto.getSubSpecialityList())
					.setResultTransformer(Transformers.aliasToBean(UnitServiceTariffMasterDto.class)).list();
			return new Response(SUCCESS, null, null, unitServiceTariffMasterDtosList, null);
		}catch (HibernateException he) {
			logger.error("getServiceListBySubSpecialityId failed", he);
			throw new ServiceException(he.getMessage());
		}catch (Exception e) {
			logger.error("getServiceListBySubSpecialityId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getServiceListBySpecialityIdSubSpecialityList(UnitServiceTariffMasterDto unitServiceTariffMasterDto)
			throws ApplicationContextException {
		try{
			
			
			String pattern = "yyyy-MM-dd";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

			String date = simpleDateFormat.format(TimeZoneComponent.getDateByzone(unitServiceTariffMasterDto.getUnitId()));
			String query ="select  "
			 + " 	servicemas1_.service_master_id as \"serviceId\",  "
			 + " 	servicemas1_.service_standard_name as \"serviceDesc\",  "
			 + " 	servicemas1_.service_standard_code as \"serviceCode\",  "
			 + " 	basePrice.base_price as \"serviceRate\",  "
			 + " 	servicemas1_.speciality_id as \"specialityId\",  "
			 + " 	speciality2_.speciality_name as \"specialityDesc\",  "
			 + " 	servicemas1_.sub_speciality_id as \"subSpecialityId\",  "
			 + " 	subspecial3_.sub_speciality_name as \"subSpecialityDesc\",  "
			 + " 	details.gst_type_id as \"taxId\",  "
			 + " 	coalesce(tax.tax_percentage,0) as \"taxPercentage\",  "
			 + " 	(basePrice.base_price +(basePrice.base_price * (coalesce(tax.tax_percentage,0)*0.01))) as \"rate\"  "
			 + " from  "
			 + " 	service.m_service_master servicemas1_  "
			 + " inner join service.t_unit_service_master_details details on  "
			 + " 	servicemas1_.service_master_id = details.service_master_id  "
			 + " inner join service.m_service_base_price_master basePrice on  "
			 + " 	basePrice.service_master_id = servicemas1_.service_master_id  "
			 + " left join m_tax tax on  "
			 + " 	tax.id = details.gst_type_id  "
			 + " inner join doctor.m_speciality_master speciality2_ on  "
			 + " 	servicemas1_.speciality_id = speciality2_.speciality_id  "
			 + " inner join doctor.m_sub_speciality_master subspecial3_ on  "
			 + " 	servicemas1_.sub_speciality_id = subspecial3_.sub_speciality_id  "
			 + " where  "
			 + " 	details.unit_id=basePrice.unit_id  "
			 + " 	and details.organization_id =:orgId  "
			 + " 	and details.unit_id =:unitId  "
			 + "	and details.is_periodicity='N' "
			 + "	and details.is_package='I' "
			 + " 	and (servicemas1_.speciality_id in (:specialityList))  "
			 + " 	and (servicemas1_.sub_speciality_id not in (:subSpecialityList))  "
			 + " 	and '"+date+"' between basePrice.from_date and basePrice.to_date "
			 + "	ORDER BY 1 ";
			
			List<UnitServiceTariffMasterDto> unitServiceTariffMasterDtosList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orgId",unitServiceTariffMasterDto.getOrganizationId())
					.setParameter("unitId", unitServiceTariffMasterDto.getUnitId())
					.setParameterList("subSpecialityList", unitServiceTariffMasterDto.getSubSpecialityList())
					.setParameterList("specialityList", unitServiceTariffMasterDto.getSpecialityList())
					.setResultTransformer(Transformers.aliasToBean(UnitServiceTariffMasterDto.class)).list();
			return new Response(SUCCESS, null, null, unitServiceTariffMasterDtosList, null);
		}catch (HibernateException he) {
			logger.error("getServiceListBySpecialityIdSubSpecialityList failed", he);
			throw new ServiceException(he.getMessage());
		}catch (Exception e) {
			logger.error("getServiceListBySpecialityIdSubSpecialityList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getUniqueEffectiveDate(UnitServiceTariffMasterDto unitServiceTariffMasterDto) throws ApplicationContextException {
		try{
			List <Integer> alreadyExsistIds = new LinkedList<>();
			Query query= sessionFactory.getCurrentSession().createSQLQuery("select unitservic0_.unit_service_tariff_id as \"unitServiceTriffId\"  from "
					+ "service.m_unit_service_tariff_master unitservic0_ "
					+ " WHERE unitservic0_.unit_id=:unitId "
					+ " AND unitservic0_.effective_date=:effectiveDate "
					+ " AND unitservic0_.tariff_id=:tariffId "
					+ " AND unitservic0_.payment_entitlement_id IN (:paymentEntitlementId) "
					+ " AND unitservic0_.visit_type_id IN (:visittypeId)  "
					+ " AND unitservic0_.patient_type_id IN (:patientTypeList) "
					+ " AND unitservic0_.billing_bed_category_id IN (:billingBedCatList) "
					+ " AND unitservic0_.service_id IN (:serviceList) "
					+ " AND unitservic0_.status='A' ");
			
			List<UnitServiceTariffMasterDto> unitServiceTariffMasterDtoList = query
					.setParameterList("patientTypeList", unitServiceTariffMasterDto.getPatientTypeIdList())
					.setParameterList("billingBedCatList", unitServiceTariffMasterDto.getBedCategoryIdList())
					.setParameterList("serviceList", unitServiceTariffMasterDto.getServiceList())
					.setParameterList("visittypeId", unitServiceTariffMasterDto.getVisitTypeIdList())
					.setParameterList("paymentEntitlementId", unitServiceTariffMasterDto.getPaymentEntitlementIdList())
					.setParameter("unitId",unitServiceTariffMasterDto.getUnitId())
					.setParameter("effectiveDate",unitServiceTariffMasterDto.getEffectiveDate())
					.setParameter("tariffId",unitServiceTariffMasterDto.getTariffId())
					.setResultTransformer(Transformers.aliasToBean(UnitServiceTariffMasterDto.class)).list();
			if(unitServiceTariffMasterDtoList!=null && unitServiceTariffMasterDtoList.size()>0)
			{
				for(UnitServiceTariffMasterDto dto : unitServiceTariffMasterDtoList)
					alreadyExsistIds.add(dto.getUnitServiceTriffId());
			}
			
			
			
			return new Response(SUCCESS, null, null, alreadyExsistIds, unitServiceTariffMasterDtoList.size());
		}catch(Exception e){
			logger.error("getUniqueEffectiveDate failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getServiceListBySubSpecialityIdNotByTariff (UnitServiceTariffMasterDto unitServiceTariffMasterDto )throws ApplicationContextException {
try{
			
			StringBuilder query = new StringBuilder("	select "
					 + " 	serviceMaster.service_master_id as \"serviceId\", "
					 + " 	serviceMaster.service_standard_name as \"serviceDesc\", "
					 + " 	serviceMaster.service_standard_code as \"serviceCode\", "
					 + " 	basePrice.base_price as \"serviceRate\", "
					 + " 	serviceMaster.speciality_id as \"specialityId\", "
					 + " 	speciality.speciality_name as \"specialityDesc\", "
					 + " 	serviceMaster.sub_speciality_id as \"subSpecialityId\", "
					 + " 	subSpeciality.sub_speciality_name as \"subSpecialityDesc\", "
					 + " 	details.gst_type_id as \"taxId\", "
					 + " 	coalesce(tax.tax_percentage,0) as \"taxPercentage\", "
					 + " 	(basePrice.base_price +(basePrice.base_price * (coalesce(tax.tax_percentage,0)*0.01))) as \"rate\" "
					 + " from "
					 + " 	service.m_service_master serviceMaster "
					 + " inner join service.t_unit_service_master_details details on "
					 + " 	serviceMaster.service_master_id = details.service_master_id "
					 + " inner join service.m_service_base_price_master basePrice on "
					 + " 	basePrice.service_master_id = serviceMaster.service_master_id "
					 + " left join m_tax tax on "
					 + " 	tax.id = details.gst_type_id "
					 + " inner join doctor.m_speciality_master speciality on "
					 + " 	serviceMaster.speciality_id = speciality.speciality_id "
					 + " inner join doctor.m_sub_speciality_master subSpeciality on "
					 + " 	serviceMaster.sub_speciality_id = subSpeciality.sub_speciality_id "
					 + " where "
					 + " 	details.organization_id =:organizationId "
					 + " 	and details.unit_id =:unitId "
					 + "	and details.is_periodicity='N' "
					 + "	and details.is_package='I' "
					 + " 	and details.unit_id = basePrice.unit_id "
					 + " 	and (serviceMaster.sub_speciality_id in (:subSpecialityList)) ");
			
			if(unitServiceTariffMasterDto.getContractId()!=null && unitServiceTariffMasterDto.getContractId()>0)
			{
				query.append(" and serviceMaster.service_master_id not in (select distinct(tcsd.service_id) from service.t_contract_service_details tcsd where contract_id =:contractId )");
			}
			
			Query createQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("organizationId",unitServiceTariffMasterDto.getOrganizationId())
					.setParameter("unitId", unitServiceTariffMasterDto.getUnitId())
					.setParameterList("subSpecialityList", unitServiceTariffMasterDto.getSubSpecialityList());

	if(unitServiceTariffMasterDto.getContractId()!=null && unitServiceTariffMasterDto.getContractId()>0)
	{
		createQuery.setParameter("contractId",unitServiceTariffMasterDto.getContractId());
	}

	List<UnitServiceTariffMasterDto> list = createQuery.setResultTransformer(Transformers.aliasToBean(UnitServiceTariffMasterDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);
		}catch (HibernateException he) {
			logger.error("getServiceListBySubSpecialityIdNotByTariff failed", he);
			throw new ServiceException(he.getMessage());
		}catch (Exception e) {
			logger.error("getServiceListBySubSpecialityIdNotByTariff failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public void inactiveDuplicateIds(List<Integer> duplicateIdsList) {
		try{
			
			String query = "update service.m_unit_service_tariff_master set status='I' where unit_service_tariff_id in (:ids)";
			
			 sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameterList("ids", duplicateIdsList).executeUpdate();
		}catch (HibernateException he) {
			logger.error("inactiveDuplicateIds failed", he);
			throw new ServiceException(he.getMessage());
		}catch (Exception e) {
			logger.error("inactiveDuplicateIds failed", e);
			throw new ServiceException(e.getMessage());
			}

	}

	@Override
	public Response searchServicesByNameAndCode(ServiceMasterDto serviceMasterDto) {
		try {
			
			StringBuilder query = new StringBuilder(" select " 
					 + " 	distinct (service.service_master_id) as \"serviceMasterId\", " 
					 + " 	service.service_standard_name as \"serviceStandardName\" , " 
					 + " 	service.service_standard_code as \"serviceStandardCode\", " 
					 + " 	cast ('0' as double precision) as \"concession\", " 
					 + " 	speciality.speciality_id as \"specialityId\", " 
					 + " 	speciality.speciality_name as \"specialityName\", " 
					 + " 	sub_speciality.sub_speciality_id as \"subSpecialityId\", " 
					 + " 	sub_speciality.sub_speciality_name as \"subSpecialityName\", " 
					 + " 	details.is_rate_editable as \"isRateEditable\", " 
					 + " 	details.is_tax_applicable as \"isTaxApplicable\", " 
					 + " 	details.is_discount as \"isDiscountApplicable\", " 
					 + " 	details.gst_type_id as \"taxId\", " 
					 + " 	details.is_ref_doctor_share as \"isRefDoctorShare\", " 
					 + " 	details.is_doc_req as \"isDoctorRequired\", " 
					 + " 	ustm.rate as \"basePriceForContract\", "
					 + " 	details.is_allow_multiple_quantity as \"isQuantityEditable\", " 
					 + " 	details.is_appointable as \"isAppointable\", " 
					 + " 	to_char(ustm.effective_date,'YYYY-MM-DD') as \"effectiveDate\" "
					 + " from " 
					 + " 	service.m_unit_service_tariff_master ustm " 
					 + " inner join service.m_service_master service on " 
					 + " 	service.service_master_id = ustm.service_id " 
					 + " inner join service.t_unit_service_master_details details on " 
					 + " 	details.service_master_id = service.service_master_id " 
					 + " inner join doctor.m_speciality_master speciality on " 
					 + " 	service.speciality_id = speciality.speciality_id " 
					 + " inner join doctor.m_sub_speciality_master sub_speciality on " 
					 + " 	service.sub_speciality_id = sub_speciality.sub_speciality_id " 
					 + " where "
					 + " 	ustm.unit_id = :unitId " 
					 + " 	and details.unit_id = :unitId " 
					 + " 	and service.status = 'A' " 
					 + " 	and details.is_package = 'I' " 
					 + " 	and details.status = 'A' " 
					 + " 	and (details.gender =:gender " 
					 + " 	or details.gender = 0) " 
					 + " 	and(lower(service.service_standard_name) like :searchKeyword " 
					 + " 	or lower(service.service_standard_code) like :searchKeyword ) " 
					 + " 	and ustm.patient_type_id =:patientTypeId " 
					 + " 	and ustm.billing_bed_category_id =:billingBed " 
					 + " 	and ustm.visit_type_id =:visitTypeId " 
					 + " 	and to_char(ustm.effective_date,'YYYY-MM-DD') <=:currentDate ");

				 query.append("	order by service.service_master_id,to_char(ustm.effective_date,'YYYY-MM-DD') desc ");
			
			String[] searchString = serviceMasterDto.getSearchKeyword().split("---");
			Integer gender=0;
			String searchStr=null;
			if(searchString.length>1)
			{
				searchStr=searchString[0];
				gender=Integer.parseInt(searchString[1]);
			}else{
					searchStr=searchString[0];					
				}
			
			List<ServiceSearchResDto> listServiceMasterDto = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString())
					.setParameter("searchKeyword", searchStr.toLowerCase()+"%")
					.setParameter("unitId", serviceMasterDto.getUnitId())
					.setParameter("gender", gender)
					.setParameter("currentDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(serviceMasterDto.getUnitId()), "yyyy-MM-dd"))
					.setParameter("visitTypeId", serviceMasterDto.getVisitTypeId()) 
					.setParameter("billingBed", serviceMasterDto.getBillingBedCategoryId())
					.setParameter("patientTypeId", serviceMasterDto.getPatientCategoryId()) 
					.setResultTransformer(Transformers.aliasToBean(ServiceSearchResDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
		}catch (HibernateException he) {
			logger.error("searchServicesByNameAndCode failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("searchServicesByNameAndCode failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getUnitServiceTariffMasterListForWeb(UnitServiceTariffMasterDto unitServiceTariffMasterDto) {
		try {

			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setOrganizationId(unitServiceTariffMasterDto.getOrganizationId());
			unitApplicationConfigurationDto.setUnitId(unitServiceTariffMasterDto.getUnitId());
			Response response = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);

			UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto)response.getObject();

			StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_UNIT_SERVICE_TARIFF_MASTER_LIST_FOR_WEB")
					.getQueryString());


				stringQuery.append(" AND ustm.tariffId  =:tariffMasterIdList ");

				stringQuery.append(" AND ustm.visitTypeId IN (1) ");

				stringQuery.append(" AND ustm.patientTypeId  =:patientTypeIdList ");

				stringQuery.append(" AND ustm.paymentEntitlementId IN (1) ");

				stringQuery.append(" AND ustm.billingBedCategoryId =:billingBedCategoryIdList ");



			Query query = sessionFactory.getCurrentSession().createQuery(stringQuery.toString());

				query.setParameter("tariffMasterIdList", unitApplicationConfigurationDtos.getDefaultSelfTariffId());

				query.setParameter("patientTypeIdList",unitApplicationConfigurationDtos.getDefaultPatientCategoryId());

				query.setParameter("billingBedCategoryIdList", unitApplicationConfigurationDtos.getOpdBillingCatId());


			query.setParameter("organizationId", unitServiceTariffMasterDto.getOrganizationId())
					.setParameter("unitId", unitServiceTariffMasterDto.getUnitId());

			List<UnitServiceTariffMasterDto> unitServiceTariffMasterDtosList = query
					.setResultTransformer(Transformers.aliasToBean(UnitServiceTariffMasterDto.class))
					.list();


			return new Response(SUCCESS, null, null, unitServiceTariffMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getUnitServiceTariffMasterListForWeb failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getUnitServiceTariffMasterListForWeb failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
