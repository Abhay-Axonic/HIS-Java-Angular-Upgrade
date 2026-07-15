package com.param.global.billing.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Repository;

import com.param.billing.global.transaction.model.VendorServiceRateConfig;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.ServiceSearchResDto;
import com.param.global.dto.VendorServiceRateConfigDto;
import com.param.global.pharmacy.dto.PendingServicesForWorkOrderCreationDto;
import com.param.global.pharmacy.dto.VendorRateDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"unchecked","rawtypes"})
public class VendorServiceRateConfigDaoImpl extends GenericDao<VendorServiceRateConfig, Integer>implements IVendorServiceRateConfigDao,ICommonConstants
{

	final static Logger logger = Logger.getLogger(VendorServiceRateConfigDaoImpl.class);

	public VendorServiceRateConfigDaoImpl() {
		super(VendorServiceRateConfig.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	HikariDataSource driverManager;
	
	
	@Override
	public Response saveVendorServiceRateConfig(List<VendorServiceRateConfigDto> vendorServiceRateConfigDtoList) {
		Connection connection =null;
		PreparedStatement statement = null;
		try {
			connection = driverManager.getConnection();
			connection.setAutoCommit(false);
			
			 statement = connection.prepareStatement("INSERT INTO service.t_vendor_service_rate_config "
	        		+ "(organization_id, unit_id, service_id, vendor_id, payment_entitlement_id, visit_type_id, patient_type_id, billing_bed_category_id, rate, effective_date, created_by, created_date, updated_by, updated_date,package_id)"
	        		+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"); 
			 int i = 0;
			 String currentDate = null;
			 for(VendorServiceRateConfigDto dto:vendorServiceRateConfigDtoList)
			 {
				  currentDate = currentDate != null ?currentDate :GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "yyy-MM-dd HH:mm:ss");
				 statement.setInt(1, dto.getOrganizationId());
				 statement.setInt(2, dto.getUnitId());
				 statement.setInt(3, dto.getServiceId());
				 statement.setInt(4, dto.getVendorId());
				 statement.setInt(5, dto.getPaymentEntitlementId());
				 statement.setInt(6, dto.getVisitTypeId());
				 statement.setInt(7, dto.getPatientTypeId());
				 statement.setInt(8, dto.getBillingBedCatId());
				 statement.setDouble(9, dto.getRate());
				 statement.setDate(10, new java.sql.Date( GlobalCommonDateUtils.getDate(dto.getEffectiveDate(),"yyyy-M-dd").getTime()));
				 statement.setInt(11, dto.getCreatedBy());
				 statement.setDate(12, new java.sql.Date( GlobalCommonDateUtils.getDate(currentDate,"yyyy-M-dd").getTime()));
				 statement.setInt(13, dto.getCreatedBy());
				 statement.setDate(14, new java.sql.Date( GlobalCommonDateUtils.getDate(currentDate,"yyyy-M-dd").getTime()));
				 statement.setInt(15, dto.getPackageId());
				 statement.addBatch();
				 i++;

		            if (i % 200 == 0 || i == vendorServiceRateConfigDtoList.size()) {
		                statement.executeBatch(); // Execute every 20 items.
		                connection.commit();  
		            }
			 }
			 return new Response(SUCCESS, null, null, null, null);
		}catch (Exception e) {
			logger.error("saveVendorServiceRateConfig failed", e);
			throw new ServiceException(e.getMessage());
		}finally{
			try{
				if (statement != null) {
					statement.close();
				}
				connection.setAutoCommit(true);
				connection.close();
			}catch(Exception e){
				logger.error("saveVendorServiceRateConfig cleanup failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	}

	@Override
	public void inactiveDuplicateIds(List<Integer> duplicateIdsList) {
		try {
			String query = "update service.t_vendor_service_rate_config set status='I' where vendor_service_rate_config_id in (:ids)";
			sessionFactory.getCurrentSession().createSQLQuery(query).setParameterList("ids", duplicateIdsList)
					.executeUpdate();
		} catch (HibernateException he) {
			logger.error("inactiveDuplicateIds failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("inactiveDuplicateIds failed", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public Response getUniqueEffectiveDate(VendorServiceRateConfigDto vendorServiceRateConfigDto) {
		try{
			List <Integer> alreadyExsistIds = new LinkedList<>();
			Query query= null;
			List<VendorServiceRateConfigDto> vendorServiceRateConfigDtoList = null;
			
			//with package ids
			if(vendorServiceRateConfigDto.getPackageIdList()!=null && vendorServiceRateConfigDto.getPackageIdList().size()>0)
			{
				String querys = "select vendor_service_rate_config_id as \"vendorServiceRateConfigId\" from " +
						"service.t_vendor_service_rate_config config " +
						"inner join service.m_package_master mpm on " +
						"mpm.package_master_id = config.package_id " +
						"WHERE config.unit_id = :unitId " +
						"AND to_char(config.effective_date, 'YYYY-MM-DD') = :effectiveDate " +
						"AND config.vendor_id IN (:vendorsList) " +
						"AND config.payment_entitlement_id IN (:payamentEntitlementList) " +
						"AND config.visit_type_id IN (:visitList) " +
						"AND config.patient_type_id IN (:patientTypeList) " +
						"AND config.billing_bed_category_id IN (:billingBedCatList) " +
						"AND config.service_id IN (:serviceList) " +
						"AND mpm.parent_package_id IN (:packageList) " +
						"AND config.status = 'A'";
				Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(querys);
				sqlQuery.setParameter("unitId", vendorServiceRateConfigDto.getUnitId());
				sqlQuery.setParameter("effectiveDate", vendorServiceRateConfigDto.getEffectiveDate().substring(0, 10));
				sqlQuery.setParameterList("patientTypeList", vendorServiceRateConfigDto.getPatientTypeIdList());
				sqlQuery.setParameterList("billingBedCatList", vendorServiceRateConfigDto.getBedCategoryIdList());
				sqlQuery.setParameterList("serviceList", vendorServiceRateConfigDto.getListServiceId());
				sqlQuery.setParameterList("vendorsList", vendorServiceRateConfigDto.getVenderIdList());
				sqlQuery.setParameterList("visitList", vendorServiceRateConfigDto.getVisitTypeIdList());
				sqlQuery.setParameterList("payamentEntitlementList", vendorServiceRateConfigDto.getPaymentEntitlementIdList());
				sqlQuery.setParameterList("packageList", vendorServiceRateConfigDto.getPackageIdList());

			 vendorServiceRateConfigDtoList = sqlQuery
						.setResultTransformer(Transformers.aliasToBean(VendorServiceRateConfigDto.class))
						.list();


			}else{ //without package ids
				String querys  = "select vendor_service_rate_config_id as \"vendorServiceRateConfigId\"  from "
						+ " service.t_vendor_service_rate_config config "
						+ " WHERE config.unit_id=:unitId"
						+ " AND to_char(config.effective_date, 'YYYY-MM-DD')=:effectiveDate "
						+ " AND config.vendor_id IN (:vendorsList) "
						+ " AND config.payment_entitlement_id IN (:payamentEntitlementList) "
						+ " AND config.visit_type_id IN (:visitList) "
						+ " AND config.patient_type_id IN (:patientTypeList) "
						+ " AND config.billing_bed_category_id IN (:billingBedCatList) "
						+ " AND config.service_id IN (:serviceList) "
						+ " AND config.package_id = 0 "
						+ " AND config.status='A' ";

				Query sqlQuery =  sessionFactory.getCurrentSession().createSQLQuery(querys)
				       .setParameter("unitId", vendorServiceRateConfigDto.getUnitId())
				       .setParameter("effectiveDate", vendorServiceRateConfigDto.getEffectiveDate().substring(0, 10))
						.setParameterList("patientTypeList", vendorServiceRateConfigDto.getPatientTypeIdList())
						.setParameterList("billingBedCatList", vendorServiceRateConfigDto.getBedCategoryIdList())
						.setParameterList("serviceList", vendorServiceRateConfigDto.getListServiceId())
						.setParameterList("vendorsList", vendorServiceRateConfigDto.getVenderIdList())
						.setParameterList("visitList", vendorServiceRateConfigDto.getVisitTypeIdList())
						.setParameterList("payamentEntitlementList", vendorServiceRateConfigDto.getPaymentEntitlementIdList());
				vendorServiceRateConfigDtoList=sqlQuery.setResultTransformer(Transformers.aliasToBean(VendorServiceRateConfigDto.class)).list();
			}
			
			
			
			
			if(vendorServiceRateConfigDtoList!=null && vendorServiceRateConfigDtoList.size()>0)
			{
				for(VendorServiceRateConfigDto dto : vendorServiceRateConfigDtoList)
					alreadyExsistIds.add(dto.getVendorServiceRateConfigId());
			}
			
			return new Response(SUCCESS, null, null, alreadyExsistIds, vendorServiceRateConfigDtoList.size());
		}catch(Exception e){
			logger.error("getUniqueEffectiveDate failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getVendorServiceRateConfigList(VendorServiceRateConfigDto vendorServiceRateConfigDto) {
		try {

			StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VENDOR_SERVICE_RATE_CONFIG_LIST").getQueryString());

			if (vendorServiceRateConfigDto.getVenderIdList() != null)
				stringQuery.append(" AND vsrc.vendor_id IN (:venderList) ");

			if (vendorServiceRateConfigDto.getVisitTypeIdList() != null)
				stringQuery.append(" AND vsrc.visit_type_id IN (:visitTypeIdList) ");

			if (vendorServiceRateConfigDto.getPatientTypeIdList() != null)
				stringQuery.append(" AND vsrc.patient_type_id IN (:patientTypeIdList) ");

			if (vendorServiceRateConfigDto.getPaymentEntitlementIdList() != null)
				stringQuery.append(" AND vsrc.payment_entitlement_id IN (:paymentEntitlementIdList) ");

			if (vendorServiceRateConfigDto.getBedCategoryIdList() != null)
				stringQuery.append(" AND vsrc.billing_bed_category_id IN (:billingBedCategogyIdList) ");

			if (vendorServiceRateConfigDto.getListServiceId() != null)
				stringQuery.append(" AND vsrc.service_id IN (:serviceIdList) ");
			
			if (vendorServiceRateConfigDto.getPackageIdList() != null)
				stringQuery.append(" AND vsrc.package_id IN (:packageIdList) ");

			stringQuery.append(" order by vsrc.package_id,vsrc.vendor_id,vsrc.service_id ");
			
			Query query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());

			if (vendorServiceRateConfigDto.getVenderIdList() != null)
				query.setParameterList("venderList", vendorServiceRateConfigDto.getVenderIdList());

			if (vendorServiceRateConfigDto.getVisitTypeIdList() != null)
				query.setParameterList("visitTypeIdList", vendorServiceRateConfigDto.getVisitTypeIdList());

			if (vendorServiceRateConfigDto.getPatientTypeIdList() != null)
				query.setParameterList("patientTypeIdList", vendorServiceRateConfigDto.getPatientTypeIdList());

			if (vendorServiceRateConfigDto.getPaymentEntitlementIdList() != null)
				query.setParameterList("paymentEntitlementIdList",
						vendorServiceRateConfigDto.getPaymentEntitlementIdList());

			if (vendorServiceRateConfigDto.getBedCategoryIdList() != null)
				query.setParameterList("billingBedCategogyIdList", vendorServiceRateConfigDto.getBedCategoryIdList());

			if (vendorServiceRateConfigDto.getListServiceId() != null)
				query.setParameterList("serviceIdList", vendorServiceRateConfigDto.getListServiceId());
			
			if (vendorServiceRateConfigDto.getPackageIdList() != null)
				query.setParameterList("packageIdList", vendorServiceRateConfigDto.getPackageIdList());

			List<VendorServiceRateConfigDto> vendorServiceRateConfigDtosList = query
					.setParameter("unitId", vendorServiceRateConfigDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(VendorServiceRateConfigDto.class)).list();

			return new Response(SUCCESS, null, null, vendorServiceRateConfigDtosList, null);

		} catch (HibernateException he) {
			logger.error("getVendorServiceRateConfigList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getVendorServiceRateConfigList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<VendorRateDto> getVendorRatesListForService(PendingServicesForWorkOrderCreationDto creationDto) {
		try{

			String queryStr = "select "
					+ "mv.id as \"vendorId\", "
					+ "mv.\"name\" as \"vendorName\", "
					+ "tvsrc.rate as \"rate\" "
					+ "from service.t_vendor_service_rate_config tvsrc "
					+ "inner join m_vendor mv on "
					+ "mv.id = tvsrc.vendor_id "
					+ "where "
					+ "tvsrc.service_id = :serviceId "
					+ "and tvsrc.billing_bed_category_id = :billingBedCategoryId "
					+ "and tvsrc.visit_type_id = :visitTypeId "
					+ "and tvsrc.patient_type_id = :patientCategoryId "
					+ "and tvsrc.payment_entitlement_id = :payementEntitlementId "
					+ "and tvsrc.package_id = :packageId "
					+ "and tvsrc.effective_date = ( "
					+ "select max(tvsrc.effective_date) "
					+ "from service.t_vendor_service_rate_config tvsrc "
					+ "inner join m_vendor mv on "
					+ "mv.id = tvsrc.vendor_id "
					+ "where "
					+ "tvsrc.service_id = :serviceId "
					+ "and tvsrc.billing_bed_category_id = :billingBedCategoryId "
					+ "and tvsrc.visit_type_id = :visitTypeId "
					+ "and tvsrc.patient_type_id = :patientCategoryId "
					+ "and tvsrc.package_id = :packageId "
					+ "and tvsrc.payment_entitlement_id = :payementEntitlementId "
					+ "and to_char(tvsrc.effective_date, 'YYYY-MM-DD') <= :currentDate )";

			Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr);
			query.setParameter("serviceId", creationDto.getServiceId());
			query.setParameter("billingBedCategoryId", creationDto.getBillingBedCategoryId());
			query.setParameter("visitTypeId", creationDto.getVisitTypeId());
			query.setParameter("patientCategoryId", creationDto.getPatientCategoryId());
			query.setParameter("payementEntitlementId", creationDto.getPaymentEntitlemnetId());
			query.setParameter("packageId", creationDto.getPackageId());
			query.setParameter("currentDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(creationDto.getUnitId()), "yyyy-MM-dd"));
			List<VendorRateDto> vendorRateDtoslist = query
					.setResultTransformer(Transformers.aliasToBean(VendorRateDto.class))
					.list();
			return vendorRateDtoslist;
		}catch(Exception e){
			logger.error("getVendorRatesListForService failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getEHCPackageList(VendorServiceRateConfigDto vendorServiceRateConfigDto) {
		try{
			Query query= sessionFactory.getCurrentSession().createSQLQuery(
							   " select " 
							 + " 	parentMstr.parent_package_id as \"packageMasterId\", " 
							 + " 	service.service_standard_name as \"serviceStandardName\"  " 
							 + " from " 
							 + " 	service.m_parent_package_master parentMstr " 
							 + " inner join service.m_service_master service on " 
							 + " 	service.service_master_id = parentMstr.service_id " 
							 + " where " 
							 + " 	parentMstr.status = 'A' " 
							 + " 	and parentMstr.package_type_id in  (1,5) " 
							 + " 	and parentMstr.parent_package_id in ( " 
							 + " 	select " 
							 + " 		pack.parent_package_id " 
							 + " 	from " 
							 + " 		service.m_package_master pack " 
							 + " 	where " 
							 + " 		pack.unit_id =:unitId " 
							 + " 		and pack.visit_type_id in (:visitList) " 
							 + " 			and pack.payment_entitlement_type_id in (:payamentEntitlementList) " 
							 + " 				and pack.billing_bed_category_id in (:billingBedCatList) " 
							 + " 					and pack.patient_type_id in (:patientTypeList) " 
							 + " 						and pack.status = 'A') "
							 + "  ORDER BY service.service_standard_name ASC ");
			
			List<ServiceSearchResDto> vendorServiceRateConfigDtoList = query
					.setParameterList("patientTypeList", vendorServiceRateConfigDto.getPatientTypeIdList())
					.setParameterList("billingBedCatList", vendorServiceRateConfigDto.getBedCategoryIdList())
					.setParameterList("visitList", vendorServiceRateConfigDto.getVisitTypeIdList())
					.setParameterList("payamentEntitlementList", vendorServiceRateConfigDto.getPaymentEntitlementIdList())
					.setParameter("unitId", vendorServiceRateConfigDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ServiceSearchResDto.class)).list();
			
			return new Response(SUCCESS, null, null, vendorServiceRateConfigDtoList, null);
		}catch(Exception e){
			logger.error("getEHCPackageList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<VendorServiceRateConfigDto> getEHCPackageServicesForGeneratingList(VendorServiceRateConfigDto vendorServiceRateConfigDto) {
		try{
			Query query= sessionFactory.getCurrentSession().createSQLQuery(
							   " select " 
							 + " 	ps.package_id as \"packageId\", " 
							 + " 	ps.service_id as \"serviceId\", " 
							 + " 	srv.service_standard_name as \"serviceDesc\", " 
							 + " 	srv.service_standard_code as \"serviceCode\", " 
							 + " 	service.service_standard_name as \"packageDesc\", " 
							 + " 	mpm.billing_bed_category_id as \"billingBedCatId\", " 
							 + " 	mpm.visit_type_id as \"visitTypeId\", " 
							 + " 	mpm.patient_type_id as \"patientTypeId\", " 
							 + " 	mpm.payment_entitlement_type_id as \"paymentEntitlementId\", " 
							 + "    ps.package_service_details_id as \"packageServiceDetailsId\", "
							 + " 	mpem.payment_entitlement_desc as \"paymentEntitlementDesc\", " 
							 + " 	mvtm.visit_type_code as \"visitTypeDesc\", " 
							 + " 	mpcm.patient_category as \"patientTypeDesc\", " 
							 + " 	mbbcm.billing_bed_desc as \"billingBedCatDesc\" "
							 + " from " 
							 + " 	service.t_package_services_details ps " 
							 + " left join service.m_service_master srv on " 
							 + " 	srv.service_master_id = ps.service_id " 
							 + " left join service.t_unit_service_master_details usmd on " 
							 + " 	ps.service_id = usmd.service_master_id " 
							 + " 	and usmd.status = 'A' " 
							 + " 	and ps.unit_id = usmd.unit_id " 
							 + " left join service.m_package_master mpm on " 
							 + " 	mpm.package_master_id = ps.package_id " 
							 + " inner join service.m_parent_package_master parentMstr on " 
							 + " 	parentMstr.parent_package_id = mpm.parent_package_id " 
							 + " inner join service.m_service_master service on " 
							 + " 	service.service_master_id = parentMstr.service_id " 
							 + " inner join m_payment_entitlement_master mpem on " 
							 + " 	mpm.payment_entitlement_type_id = mpem.payment_entitlement_id " 
							 + " inner join m_visit_type_master mvtm on " 
							 + " 	mpm.visit_type_id = mvtm.visit_type_id " 
							 + " inner join m_patient_category_master mpcm on " 
							 + " 	mpm.patient_type_id = mpcm.patient_category_id " 
							 + " inner join adt.m_billing_bed_category_master mbbcm on " 
							 + " 	mpm.billing_bed_category_id = mbbcm.billing_bed_category_id "
							 + " where " 
							 + " 	ps.parent_package_id in (:packageIds) "
							 + "	and mpm.visit_type_id in (:visitList) "
							 + "	and mpm.payment_entitlement_type_id in (:payamentEntitlementList) "
							 + "	and mpm.billing_bed_category_id in (:billingBedCatList) "
							 + "	and mpm.patient_type_id in (:patientTypeList) "
							 + "	and ps.service_id in (:serviceIds) " 
							 + "	and usmd.is_outsource_job = 'Y' " 
							 + " order by " 
							 + " 	service.service_standard_name,srv.service_standard_name asc ");
			
			List<VendorServiceRateConfigDto> vendorServiceRateConfigDtoList = query
					.setParameterList("packageIds", vendorServiceRateConfigDto.getPackageIdList())
					.setParameterList("patientTypeList", vendorServiceRateConfigDto.getPatientTypeIdList())
					.setParameterList("billingBedCatList", vendorServiceRateConfigDto.getBedCategoryIdList())
					.setParameterList("visitList", vendorServiceRateConfigDto.getVisitTypeIdList())
					.setParameterList("payamentEntitlementList", vendorServiceRateConfigDto.getPaymentEntitlementIdList())
					.setParameterList("serviceIds", vendorServiceRateConfigDto.getListServiceId())
					.setResultTransformer(Transformers.aliasToBean(VendorServiceRateConfigDto.class)).list();
			
			return vendorServiceRateConfigDtoList;
		}catch(Exception e){
			logger.error("getEHCPackageServicesForGeneratingList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getEHCPackageServices(VendorServiceRateConfigDto vendorServiceRateConfigDto) {
		try{
			Query query= sessionFactory.getCurrentSession().createSQLQuery(
							   " select " 
							 + " 	ps.service_id as \"serviceId\", " 
							 + " 	srv.service_standard_name as \"serviceDesc\" " 
							 + " from " 
							 + " 	service.t_package_services_details ps " 
							 + " left join service.m_service_master srv on " 
							 + " 	srv.service_master_id = ps.service_id " 
							 + " left join service.t_unit_service_master_details usmd on " 
							 + " 	ps.service_id = usmd.service_master_id " 
							 + " 	and usmd.status = 'A' " 
							 + " 	and ps.unit_id = usmd.unit_id " 
							 + " left join service.m_package_master mpm on " 
							 + " 	mpm.package_master_id = ps.package_id " 
							 + " where " 
							 + " 	mpm.parent_package_id in (:packageIds) " 
							 + " 	and usmd.is_outsource_job = 'Y' " 
							 + " group by " 
							 + " 	ps.service_id, " 
							 + " 	srv.service_standard_name "
							 + " order by "
							 + "	srv.service_standard_name ");
			
			List<VendorServiceRateConfigDto> vendorServiceRateConfigDtoList = query
					.setParameterList("packageIds", vendorServiceRateConfigDto.getPackageIdList())
					.setResultTransformer(Transformers.aliasToBean(VendorServiceRateConfigDto.class)).list();
			
			return new Response(SUCCESS, null, null, vendorServiceRateConfigDtoList, null);
		}catch(Exception e){
			logger.error("getEHCPackageServices failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
