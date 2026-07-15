package com.param.global.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Repository;

import com.param.global.billing.dto.TContractServiceDetailsDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.PeriodicityServiceResDto;
import com.param.global.dto.UnitServiceMasterDetailsDto;
import com.param.global.model.UnitServiceMasterDetails;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UnitServiceMasterDetailsDaoImpl extends GenericDao<UnitServiceMasterDetails, Integer> implements IUnitServiceMasterDetailsDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(UnitServiceMasterDetailsDaoImpl.class);

	public UnitServiceMasterDetailsDaoImpl() {
		super(UnitServiceMasterDetails.class);
	}
	

	@Autowired
	private DozerBeanMapper mapper;
	
	@Autowired
	HikariDataSource driverManager;
	
	@Override
	public Response saveUnitServiceMasterDetails(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
			throws ApplicationException {
		/*try {
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT COUNT(*) FROM UnitServiceMasterDetails service WHERE service.serviceMasterId = :serviceMasterId AND service.unitId =:unitId AND service.organizationId =:orgId");
			Long count = (Long) sessionFactory.getCurrentSession().createQuery(sb.toString())
					.setParameter("serviceMasterId", unitServiceMasterDetailsDto.getServiceMasterId())
					.setParameter("unitId", unitServiceMasterDetailsDto.getUnitId())
					.setParameter("orgId", unitServiceMasterDetailsDto.getOrganizationId())
					.uniqueResult();
			boolean exist = count > 0;
			if(exist) {
				return new Response(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else {
				UnitServiceMasterDetails unitServiceMasterDetails = mapper.map(unitServiceMasterDetailsDto, UnitServiceMasterDetails.class, "global_UnitServiceMasterDetailsDto_to_UnitServiceMasterDetails"); 
				save(unitServiceMasterDetails);
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}
		}catch (HibernateException he) {
			h
		} catch (Exception e) {
			
		} */
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPaginatedUnitServiceMasterDetailsList(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
			throws ApplicationException {
		try {
			List<UnitServiceMasterDetailsDto> listUnitServiceMasterDetailsDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_UNIT_SERVICE_MASTER_DETAILS_LIST")
					.setParameter("orgId", unitServiceMasterDetailsDto.getOrganizationId())
					.setParameter("unitId", unitServiceMasterDetailsDto.getUnitId())
					.setFirstResult(unitServiceMasterDetailsDto.getOffset() != null ? unitServiceMasterDetailsDto.getOffset() : 0)
					.setMaxResults(unitServiceMasterDetailsDto.getRecordPerPage() != null ? unitServiceMasterDetailsDto.getRecordPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listUnitServiceMasterDetailsDto, null);
		}catch (HibernateException he) {
			logger.error("getPaginatedUnitServiceMasterDetailsList failed", he);
		} catch (Exception e) {
			logger.error("getPaginatedUnitServiceMasterDetailsList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUnitServiceMasterDetailsCount(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
			throws ApplicationException {
		try {
			BigInteger UnitServiceMasterDetailsCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_UNIT_SERVICE_MASTER_DETAILS_COUNT")
					.setParameter("orgId", unitServiceMasterDetailsDto.getOrganizationId())
					.setParameter("unitId", unitServiceMasterDetailsDto.getUnitId())
					.uniqueResult();
			if (UnitServiceMasterDetailsCount.compareTo(BigInteger.ZERO) == 1)
			{
				return new Response(SUCCESS, SUCCESS_CODE, null, null, UnitServiceMasterDetailsCount);
			} else
			{
				return new Response(ERROR, null, null, null, BigInteger.ZERO);
			}
		}catch (HibernateException he) {
			logger.error("getUnitServiceMasterDetailsCount failed", he);
		} catch (Exception e) {
			logger.error("getUnitServiceMasterDetailsCount failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUnitServiceMasterDetailsById(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
			throws ApplicationException {
		try {
			 unitServiceMasterDetailsDto = (UnitServiceMasterDetailsDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_MASTER_DETAILS_BY_ID")
					.setParameter("unitServiceMasterDetailsId", unitServiceMasterDetailsDto.getUnitServiceMasterDetailsId())
					.setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class)).uniqueResult();;
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null,unitServiceMasterDetailsDto);
		}catch (HibernateException he) {
			logger.error("getUnitServiceMasterDetailsById failed", he);
		} catch (Exception e) {
			logger.error("getUnitServiceMasterDetailsById failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateUnitServiceMasterDetails(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
			throws ApplicationException {
		try {
			UnitServiceMasterDetails unitServiceMasterDetails = mapper.map(unitServiceMasterDetailsDto, UnitServiceMasterDetails.class, "global_UnitServiceMasterDetailsDto_to_UnitServiceMasterDetails"); 
			unitServiceMasterDetails.setIsPackage(unitServiceMasterDetailsDto.getIsPackage());

			unitServiceMasterDetails.setStatus('A');
			unitServiceMasterDetails.setIsOutsource(unitServiceMasterDetails.getIsOutsource()=='\u0000'?'I':unitServiceMasterDetails.getIsOutsource());
			unitServiceMasterDetails.setIsOutsourceJob(unitServiceMasterDetails.getIsOutsourceJob()=='\u0000'?'N':unitServiceMasterDetails.getIsOutsourceJob());
			unitServiceMasterDetails.setIsPackage(unitServiceMasterDetails.getIsPackage()=='\u0000'?'I':unitServiceMasterDetails.getIsPackage());
			unitServiceMasterDetails.setIsRateEditable(unitServiceMasterDetails.getIsRateEditable()=='\u0000'?'I':unitServiceMasterDetails.getIsRateEditable());
			unitServiceMasterDetails.setIsTaxApplicable(unitServiceMasterDetails.getIsTaxApplicable()=='\u0000'?'I':unitServiceMasterDetails.getIsTaxApplicable());
			unitServiceMasterDetails.setIsRefDoctorShare(unitServiceMasterDetails.getIsRefDoctorShare()=='\u0000'?'I':unitServiceMasterDetails.getIsRefDoctorShare());
			unitServiceMasterDetails.setIsDiscount(unitServiceMasterDetails.getIsDiscount()=='\u0000'?'I':unitServiceMasterDetails.getIsDiscount());
			if(unitServiceMasterDetailsDto.getIsCyclicFlag()!=null) {
				unitServiceMasterDetails.setIsCyclicFlag(unitServiceMasterDetailsDto.getIsCyclicFlag());
			}
			if(unitServiceMasterDetails.getIsDocReq().equals('N'))
				unitServiceMasterDetails.setDocRefPer(null);
			if(unitServiceMasterDetails.getIsRefDoctorShare()=='N')
				unitServiceMasterDetails.setRefSharePer(null);
			
			if(unitServiceMasterDetails.getIsDocReq().equals('Y') && unitServiceMasterDetails.getDocRefPer()==null)
				unitServiceMasterDetails.setDocRefPer(BigDecimal.ZERO);
			if(unitServiceMasterDetails.getIsRefDoctorShare()=='Y' && unitServiceMasterDetails.getRefSharePer()==null)
				unitServiceMasterDetails.setRefSharePer(BigDecimal.ZERO);
			
			unitServiceMasterDetails.setIsDocSharePerWise(unitServiceMasterDetailsDto.getIsDocSharePerWise());
			unitServiceMasterDetails.setIsRefSharePerWise(unitServiceMasterDetailsDto.getIsRefSharePerWise());
			unitServiceMasterDetails.setIsFormFApplicable(unitServiceMasterDetailsDto.getIsFormFApplicable());
			unitServiceMasterDetails.setIsAdminService(unitServiceMasterDetailsDto.getIsAdminService());

			update(unitServiceMasterDetails);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch (HibernateException he) {
			logger.error("updateUnitServiceMasterDetails failed", he);
		} catch (Exception e) {
			logger.error("updateUnitServiceMasterDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllActiveTax(Integer organizationId) throws ApplicationException {
		try {
			List<UnitServiceMasterDetailsDto> listUnitServiceMasterDetailsDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ALL_ACTIVE_TAX")
					.setParameter("organizationId", organizationId)
					.setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listUnitServiceMasterDetailsDto, null);
		}catch (HibernateException he) {
			logger.error("getAllActiveTax failed", he);
		} catch (Exception e) {
			logger.error("getAllActiveTax failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response searchPeriodicityServices(int visitTypeId, int unitId, String serviceName)throws ApplicationException {
		StatelessSession session = null;
		try {
			session = sessionFactory.openStatelessSession();
			String query = null;
		    query = 
		    		" select "
                            + "     srvmst.service_standard_name as \"serviceName\", "
                            + "     srvmst.service_standard_code as \"serviceCode\", "
                            + "     srvmst.service_master_id as \"serviceId\", "
                            + "     baseprice.base_price as \"basePrice\" "
                           // + "		con.product_category_id as \"productCategoryId\", "
                           // + "		con.asset_category_id as \"assetCategoryId\" "
                            + " from "
                            + "     service.m_service_master srvmst "
                            + " inner join service.t_unit_service_master_details srvmppr on "
                            + "     srvmppr.service_master_id = srvmst.service_master_id "
                            + " inner join service.m_service_base_price_master baseprice on "
                            + "    srvmst.service_master_id  = baseprice.service_master_id "
                          /*  + " left join service.m_periodicity_service_config con on " 
                            + "    con.service_id = srvmppr.service_master_id "*/
                            + " where "
                            + "     srvmppr.status = 'A' "
                            + "     and srvmppr.is_periodicity = 'Y' "
                            + "     and srvmppr.unit_id = :unitId "
                            + "    and baseprice.unit_id=srvmppr.unit_id "
                           // + "     and srvmppr.organization_id = :orgId "
                            + "     and (lower(trim(srvmst.service_standard_name)) like :serviceName "
                            + "     or lower(trim(srvmst.service_standard_code)) like :serviceName ) ";
			if(visitTypeId > 0){
				
				query = 
						" select "
								 + " 	srvmst.service_standard_name as \"serviceName\", "
								 + " 	srvmst.service_standard_code as \"serviceCode\", "
								 + " 	srvmst.service_master_id as \"serviceId\", "
								 + "	con.product_category_id as \"productCategoryId\", "
								 + "	con.asset_category_id as \"assetCategoryId\", "
								 + "	con.periodicity_service_config_id as \"periodicityServiceConfigId\", "
								 + "	con.equipment_id as \"equipmentId\", "
								 + "	item.name as \"equipmentName\", "
								 + "	(case when con.equipment_id > 0 then "
								 + "		concat(srvmst.service_standard_name,' | ',item.name) "
								 + "	else  "
								 + "		srvmst.service_standard_name end ) as \"displayName\" "
								 + " from "
								 + " 	service.m_service_master srvmst "
								 + " inner join service.t_unit_service_master_details srvmppr on "
								 + " 	srvmppr.service_master_id = srvmst.service_master_id "
								 + " left join service.m_periodicity_service_config con on " 
		                         + "    con.service_id = srvmppr.service_master_id "
		                         + " left join m_item item on "
		                         + " 	item.id = con.equipment_id "
								 + " where "
								 + " 	srvmppr.status = 'A' "
								 + " 	and srvmppr.is_periodicity = 'Y' "
								 + " 	and srvmppr.unit_id = :unitId "
								// + " 	and srvmppr.organization_id = :orgId "
								 + " 	and (lower(trim(srvmst.service_standard_name)) like :serviceName "
								 + " 	or lower(trim(srvmst.service_standard_code)) like :serviceName ) "
								 + " 	and con.visit_type_id = "+visitTypeId;
			}
			
			List<PeriodicityServiceResDto> listPeriodicityServiceResDto = session.createSQLQuery(query)
					.setParameter("unitId", unitId)
					.setParameter("serviceName", "%"+serviceName.trim().toLowerCase()+"%")
					.setResultTransformer(Transformers.aliasToBean(PeriodicityServiceResDto.class))
					.list();
			
			
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listPeriodicityServiceResDto, null);
		}catch(Exception e){
			
			throw e;
		}finally{
			if(session != null)
				session.close();
		}
	}

	@Override
	public Response searchProcedureServices(int orgId, int unitId, String serviceName,int specialityId) throws ApplicationException {
		StatelessSession session = null;
		try {
			session = sessionFactory.openStatelessSession();
			List<PeriodicityServiceResDto> listPeriodicityServiceResDto = session.getNamedQuery("GET_PROCEDURE_SERVICES_FOR_SEARCH")
					.setParameter("unitId", unitId)
					.setParameter("orgId", orgId)
					.setParameter("specialityId", specialityId)
					.setParameter("serviceName", "%"+serviceName.trim().toLowerCase()+"%")
					.setResultTransformer(Transformers.aliasToBean(PeriodicityServiceResDto.class))
					.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listPeriodicityServiceResDto, null);
		}catch(Exception e){
			
			throw e;
		}finally{
			if(session != null)
				session.close();
		}
	}

	@Override
	public Response getProcedureTypeList() throws ApplicationException {
		try {
			List<UnitServiceMasterDetailsDto> listUnitServiceMasterDetailsDto = sessionFactory.getCurrentSession()
					.createSQLQuery("select procedure_type_id as\"procedureTypeId\", procedure_type_name as\"procedureTypeName\" "
							+ " from  public.m_procedure_type_master")
					.setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listUnitServiceMasterDetailsDto, null);
		}catch (HibernateException he) {
			logger.error("getProcedureTypeList failed", he);
		} catch (Exception e) {
			logger.error("getProcedureTypeList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getProcedureListByProcedureType(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
			throws ApplicationException {
		try {
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PROCEDURE_SERVICES_FOR_SEARCH").getQueryString());
			
			if(unitServiceMasterDetailsDto.getProcedureTypeIdList()!=null)
				query.append("AND srvmppr.procedure_type_id IN( "+unitServiceMasterDetailsDto.getProcedureTypeIdList().get(0)+","+unitServiceMasterDetailsDto.getProcedureTypeIdList().get(1)+")");
			else
				query.append("AND srvmppr.procedure_type_id="+unitServiceMasterDetailsDto.getProcedureTypeId());
			
			Query createQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString())
					.setParameter("orgId", unitServiceMasterDetailsDto.getOrganizationId())
					.setParameter("unitId", unitServiceMasterDetailsDto.getUnitId())
					.setParameter("serviceName", "%"+unitServiceMasterDetailsDto.getServiceStandardName().trim().toLowerCase()+"%");


			List<PeriodicityServiceResDto> listPeriodicityServiceResDto = createQuery.setResultTransformer(Transformers.aliasToBean(PeriodicityServiceResDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, listPeriodicityServiceResDto, null);
		} catch (HibernateException he) {
			logger.error("getProcedureListByProcedureType failed", he);
		} catch (Exception e) {
			logger.error("getProcedureListByProcedureType failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPaginatedUnitServiceMasterDetailsListSearch(
			UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) throws ApplicationException {
		try {
			List<UnitServiceMasterDetailsDto> listUnitServiceMasterDetailsDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_UNIT_SERVICE_MASTER_DETAILS_LIST_SEARCH")
					.setParameter("orgId", unitServiceMasterDetailsDto.getOrganizationId())
					.setParameter("unitId", unitServiceMasterDetailsDto.getUnitId())
					.setParameter("serviceName", "%"+unitServiceMasterDetailsDto.getServiceStandardName().trim().toLowerCase()+"%")
					.setFirstResult(unitServiceMasterDetailsDto.getOffset() != null ? unitServiceMasterDetailsDto.getOffset() : 0)
					.setMaxResults(unitServiceMasterDetailsDto.getRecordPerPage() != null ? unitServiceMasterDetailsDto.getRecordPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listUnitServiceMasterDetailsDto, null);
		}catch (HibernateException he) {
			logger.error("getPaginatedUnitServiceMasterDetailsListSearch failed", he);
		} catch (Exception e) {
			logger.error("getPaginatedUnitServiceMasterDetailsListSearch failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUnitServiceMasterDetailsListForCyclic(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
			throws ApplicationException {
		try {
			List<UnitServiceMasterDetailsDto> listUnitServiceMasterDetailsDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_MASTER_DETAILS_LIST_FOR_CYCLIC")
					.setParameter("orgId", unitServiceMasterDetailsDto.getOrganizationId())
					.setParameter("unitId", unitServiceMasterDetailsDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listUnitServiceMasterDetailsDto, null);
		}catch (HibernateException he) {
			logger.error("getUnitServiceMasterDetailsListForCyclic failed", he);
		} catch (Exception e) {
			logger.error("getUnitServiceMasterDetailsListForCyclic failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUnitServiceMasterQtyDetailsByServiceId(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
			throws ApplicationException {
		try {
			List<UnitServiceMasterDetailsDto> listUnitServiceMasterDetailsDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_MASTER_QTY_DETAILS")
					.setParameter("patientId", unitServiceMasterDetailsDto.getPatientId())
					.setParameter("orgId", unitServiceMasterDetailsDto.getOrganizationId())
					.setParameter("unitId", unitServiceMasterDetailsDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class)).list();
			
			List<UnitServiceMasterDetailsDto> finalUnitServiceList=new ArrayList<>();
			//check whether cyclic appointment generated against bill 
			if(listUnitServiceMasterDetailsDto.size()>0) {
				for(UnitServiceMasterDetailsDto dto:listUnitServiceMasterDetailsDto)	{
					List<UnitServiceMasterDetailsDto> unitServiceMasterDetailsDtoList=sessionFactory.getCurrentSession().createSQLQuery("select cam.bill_id as \"id\" "
					 		+ " from opd.t_cyclic_appointment_mapper cam where cam.order_id=:id and cam.service_id=:servId ")
							.setParameter("id", dto.getOrderId())
							.setParameter("servId", dto.getServiceMasterId())
							.setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class)).list();

					
					if(dto.getPackageId()==0) {
					List<Integer> orderDetailsList=sessionFactory.getCurrentSession().createSQLQuery("select ordD.order_details_id from public.t_order_details ordD " 
							+" where ordD.order_id=:orderId and ordD.service_id=:serviceId ")
							.setParameter("serviceId", dto.getServiceMasterId())
							.setParameter("orderId", dto.getOrderId())
							.list();
					  if(!orderDetailsList.isEmpty()) {
						  dto.setOrderList(orderDetailsList); 
					  }
					}
					
					if(unitServiceMasterDetailsDtoList.isEmpty()) {
						finalUnitServiceList.add(dto);
					}
				}
				
				
			}
		
			
			return new Response(SUCCESS, SUCCESS_CODE, null, finalUnitServiceList, null);
		}catch (HibernateException he) {
			logger.error("getUnitServiceMasterQtyDetailsByServiceId failed", he);
		} catch (Exception e) {
			logger.error("getUnitServiceMasterQtyDetailsByServiceId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateUnitServiceMasterDetailsStatus(Integer serviceId, Integer orgId, Integer orgUnitid,
			Character status) {
		try {
			Integer count = sessionFactory.getCurrentSession()
					.createSQLQuery("UPDATE service.t_unit_service_master_details " +
							"SET status = :status " +
							"WHERE service_master_id = :serviceId " +
							"AND organization_id = :orgId " +
							"AND unit_id = :orgUnitid")
					.setParameter("status", status)
					.setParameter("serviceId", serviceId)
					.setParameter("orgId", orgId)
					.setParameter("orgUnitid", orgUnitid)
					.executeUpdate();

			if(count>0) {
				return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
			}else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

		}catch (HibernateException he) {
			logger.error("updateUnitServiceMasterDetailsStatus failed", he);
		} catch (Exception e) {
			logger.error("updateUnitServiceMasterDetailsStatus failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUnitServiceMasterDetailsByServiceId(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
			throws ApplicationException {
		try {
			String query="select tusmd.is_appointable  as \"isAppointable\" from service.t_unit_service_master_details as tusmd where service_master_id=:serviceId and unit_id=:unitId and status='A'";
			 unitServiceMasterDetailsDto = (UnitServiceMasterDetailsDto) sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("serviceId", unitServiceMasterDetailsDto.getServiceMasterId())
					.setParameter("unitId", unitServiceMasterDetailsDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class)).uniqueResult();;
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null,unitServiceMasterDetailsDto);
		}catch (HibernateException he) {
			logger.error("getUnitServiceMasterDetailsByServiceId failed", he);
		} catch (Exception e) {
			logger.error("getUnitServiceMasterDetailsByServiceId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveContractServiceDetails(List<TContractServiceDetailsDto> masterDtoList)
			throws ApplicationException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {

			connection = driverManager.getConnection();
			connection.setAutoCommit(false);
			 statement = connection.prepareStatement("INSERT INTO service.t_contract_service_details "
					+ "(org_id, unit_id, contract_id, service_price, final_price, service_id, credit_payable, self_payable, created_by, created_date,updated_by,updated_date,apportioned_price,is_package,package_master_id,billing_bed_category_id,patient_category_id,tax_id,tax_percentage)"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)");
			int i = 0;
			for (TContractServiceDetailsDto dto : masterDtoList) 
			{
				if(dto.getIsPackage()==null)
					dto.setIsPackage('N');
				
				statement.setInt(1, dto.getOrgId());
				statement.setInt(2, dto.getUnitId());
				statement.setInt(3, dto.getContractId());
				statement.setDouble(4, dto.getServicePrice());
				statement.setDouble(5, dto.getFinalPrice());
				statement.setInt(6, dto.getServiceId());
				statement.setDouble(7, dto.getCreditPayable());
				statement.setDouble(8, dto.getSelfPayable());
				statement.setInt(9, dto.getCreatedBy());
				statement.setDate(10,new java.sql.Date(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "yyyy-MM-dd").getTime()));
				statement.setInt(11, dto.getCreatedBy());
				statement.setDate(12,new java.sql.Date(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "yyyy-MM-dd").getTime()));
				statement.setDouble(13, dto.getApportionedPrice());
				statement.setBoolean(14,dto.getIsPackage()!=null ? (dto.getIsPackage()=='Y' ? true : false) : false);
				statement.setInt(15, dto.getPackageMasterId()!=null ? dto.getPackageMasterId() : 0);
				statement.setInt(16, dto.getBillingBedCategoryId() !=null ? dto.getBillingBedCategoryId() : 0);
				statement.setInt(17, dto.getPatientCategoryId() !=null ? dto.getPatientCategoryId() : 0);
				statement.setInt(18, dto.getTaxId() !=null ? dto.getTaxId() :0);
				statement.setDouble(19, dto.getTaxPercentage() !=null ? dto.getTaxPercentage() : 0);
				statement.addBatch();
				i++;
				if (i % 200 == 0 || i == masterDtoList.size()) {
					statement.executeBatch();
					// Execute every 200 items.
					connection.commit();
				}
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
			
			throw new ServiceException(e.getMessage());
		} finally {

			try {
				if (statement != null) {
					statement.close();
				}
				connection.setAutoCommit(true);
				connection.close();
			} catch (Exception e) {
				logger.error("saveContractServiceDetails cleanup failed", e);
			}

		}
	}

	@Override
	public Response updateTestByServiceMaster(Integer unitId, Integer serviceMasterId, char isOutsource)
			throws ApplicationException {
		try{

			sessionFactory.getCurrentSession().createSQLQuery(
							"UPDATE lab.m_test_master " +
									"SET is_outsourced = :isOutsource " +
									"WHERE service_id = :serviceMasterId " +
									"AND org_unit_id = :unitId")
					.setParameter("isOutsource", isOutsource)
					.setParameter("serviceMasterId", serviceMasterId)
					.setParameter("unitId", unitId)
					.executeUpdate();

			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);

		}catch (HibernateException he) {
			logger.error("updateTestByServiceMaster failed", he);
		} catch (Exception e) {
			logger.error("updateTestByServiceMaster failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response UnitServiceMasterDetailsForSchedulingWorking(
			UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) throws ApplicationException {
		try {
			List<UnitServiceMasterDetailsDto> listUnitServiceMasterDetailsDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_MASTER_QTY_DETAILS_FOR_SCHEDULING_WORKING")
					.setParameter("patientId", unitServiceMasterDetailsDto.getPatientId())
					.setParameter("orgId", unitServiceMasterDetailsDto.getOrganizationId())
					.setParameter("unitId", unitServiceMasterDetailsDto.getUnitId())
					.setParameter("fromDate", unitServiceMasterDetailsDto.getFromDate())
					.setParameter("toDate", unitServiceMasterDetailsDto.getToDate())
					.setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class)).list();
			
			List<UnitServiceMasterDetailsDto> tempFinalUnitServiceList=new ArrayList<>();
			if(listUnitServiceMasterDetailsDto.size()>0) {
				
				
				for(UnitServiceMasterDetailsDto dto:listUnitServiceMasterDetailsDto)	{
					
					if(tempFinalUnitServiceList.size() == 0) {
						tempFinalUnitServiceList.add(dto);
					}else {
						Boolean flag = false;
						for(UnitServiceMasterDetailsDto billDto : tempFinalUnitServiceList)	{
							
							if(billDto.getBillNumber().equals(dto.getBillNumber())) {
								flag = true;
							}
						}
						if(flag == false) {
							tempFinalUnitServiceList.add(dto);
						}
						
					}
				}
				
				for(UnitServiceMasterDetailsDto billDto : tempFinalUnitServiceList)	{
					List<Integer> orderIds = new ArrayList<Integer>();
					for(UnitServiceMasterDetailsDto dto:listUnitServiceMasterDetailsDto)	{
						
						if(billDto.getBillNumber().equals(dto.getBillNumber())) {
							orderIds.add(dto.getOrderId());
						}	
					}
					
					billDto.setOrderList(orderIds);
				}
				
			}
			List<UnitServiceMasterDetailsDto> finalUnitServiceList=new ArrayList<>();
			for(UnitServiceMasterDetailsDto billDto : tempFinalUnitServiceList)	{
				
				List<UnitServiceMasterDetailsDto> checkList = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_SERVICE_MASTER_QTY_DETAILS_FOR_SCHEDULING_WORKING_LIST")
						.setParameter("orgId", unitServiceMasterDetailsDto.getOrganizationId())
						.setParameter("unitId", unitServiceMasterDetailsDto.getUnitId())
						.setParameterList("orderIds",billDto.getOrderList() )
						.setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class)).list();
				
				if(checkList != null && checkList.size()>0) {
					finalUnitServiceList.add(billDto);
				}
			}


			return new Response(SUCCESS, SUCCESS_CODE, null, finalUnitServiceList, null);
		}catch (HibernateException he) {
			logger.error("UnitServiceMasterDetailsForSchedulingWorking failed", he);
		} catch (Exception e) {
			logger.error("UnitServiceMasterDetailsForSchedulingWorking failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response unitServiceMasterDetailsForSchedulingWorkingDetails(
			UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) throws ApplicationException {
		try {
			List<UnitServiceMasterDetailsDto> listUnitServiceMasterDetailsDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_MASTER_QTY_DETAILS_FOR_SCHEDULING_WORKING_LIST")
					.setParameter("orgId", unitServiceMasterDetailsDto.getOrganizationId())
					.setParameter("unitId", unitServiceMasterDetailsDto.getUnitId())
					.setParameterList("orderIds",unitServiceMasterDetailsDto.getOrderList() )
					.setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, listUnitServiceMasterDetailsDto, null);
		}catch (HibernateException he) {
		logger.error("unitServiceMasterDetailsForSchedulingWorkingDetails failed", he);
		} catch (Exception e) {
		logger.error("unitServiceMasterDetailsForSchedulingWorkingDetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

    @Override
    public Response getAdminServicesForConfiguration(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
            throws ApplicationException {
        try {
            List<UnitServiceMasterDetailsDto> unitServiceMasterDetailsList = new ArrayList<>();
            String query=" select "
                    + " 	details.unit_service_master_details_id as \"unitServiceMasterDetailsId\", "
                    + " 	details.service_master_id as \"serviceMasterId\", "
                    + " 	details.is_package as \"isPackage\",  "
                    + " 	details.unit_id as \"unitId\", "
                    + " 	details.status as \"status\", "
                    + " 	service.service_standard_name as \"serviceStandardName\", "
                    + " 	service.service_standard_code as \"serviceStandardCode\", "
                    + " 	service.speciality_id as \"specialityId\", "
                    + " 	service.sub_speciality_id as \"subSpecialityId\", "
                    + " 	speciality.speciality_name as \"specialityName\", "
                    + " 	sub_speciality.sub_speciality_name as \"subSpecialityName\", "
                    + "	    details.is_admin_service as \"isAdminService\"  "
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
                    + "	msbpm.unit_id = :unitId "
                    + " 	and details.is_admin_service = 'Y' ";

            unitServiceMasterDetailsList = sessionFactory.getCurrentSession()
                    .createSQLQuery(query)
                    .setParameter("unitId", unitServiceMasterDetailsDto.getUnitId())
                    .setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class)).list();

            return new Response<>(SUCCESS, SUCCESS_CODE, null, unitServiceMasterDetailsList,null);
        }catch (HibernateException he) {
        logger.error("getAdminServicesForConfiguration failed", he);
        } catch (Exception e) {
        logger.error("getAdminServicesForConfiguration failed", e);
        }
        return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }
}
