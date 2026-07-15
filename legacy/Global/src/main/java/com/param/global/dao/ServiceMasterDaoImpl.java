package com.param.global.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Repository;

import com.param.common.dto.CommonResponse;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.dto.CommonDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.OrderDetailsByEncounterIdDto;
import com.param.global.dto.OrderDetailsDiscountDto;
import com.param.global.dto.ServiceMasterDto;
import com.param.global.dto.ServicePriceDetailsDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.UnitServiceMapperDto;
import com.param.global.model.ServiceMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ServiceMasterDaoImpl extends GenericDao<ServiceMaster, Integer>
		implements IServiceMasterDao, ICommonConstants, IError {

	public ServiceMasterDaoImpl() {
		super(ServiceMaster.class);
	}


	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	HikariDataSource driverManager;

	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;

	final static Logger logger = Logger.getLogger(ServiceMasterDaoImpl.class);

	@Override
	public Response saveOrganizationServiceMaster(ServiceMasterDto xServiceMasterDto) throws ApplicationException {
		try {
			// ----------- check if Service Name alreay exist ---------------//
			Long count = (Long) sessionFactory.getCurrentSession()
					.createQuery(
							"select count(*) from ServiceMaster mst where (LOWER(mst.serviceStandardName) =:serviceStandardName OR mst.serviceStandardName =:serviceStandardName) "
							+ "AND mst.organizationId=:orgId ")
					.setParameter("serviceStandardName", xServiceMasterDto.getServiceStandardName())
					.setParameter("orgId",xServiceMasterDto.getOrganizationId()).uniqueResult();
			boolean exists = count > 0;
			if (exists)
				return new Response(ERROR, ALREADY_EXIST_CODE, SERVICE_NAME_ALREADY_EXIST, null, null);
			else {
				ServiceMaster xServiceMaster = mapper.map(xServiceMasterDto, ServiceMaster.class,
						"billing_XServiceMasterDto_to_XServiceMaster");
				xServiceMaster.setIsOutsourceService(xServiceMaster.getIsOutsourceService()=='\u0000'?'N':xServiceMaster.getIsOutsourceService());
				xServiceMaster.setIsAvailableForPackage(xServiceMaster.getIsAvailableForPackage()=='\u0000'?'A':xServiceMaster.getIsAvailableForPackage());
				xServiceMaster.setIsOtProcedure(xServiceMaster.getIsOtProcedure()=='\u0000'?'N':xServiceMaster.getIsOtProcedure());
				xServiceMaster.setIsOrderSet(xServiceMaster.getIsOrderSet()=='\u0000'?'N':xServiceMaster.getIsOrderSet());
				xServiceMaster.setIsRateEditable(xServiceMaster.getIsRateEditable()=='\u0000'?'N':xServiceMaster.getIsRateEditable());
				xServiceMaster.setIsDiscountApplicable(xServiceMaster.getIsDiscountApplicable()=='\u0000'?'Y':xServiceMaster.getIsDiscountApplicable());
				xServiceMaster.setIsAllowMultipleQuantity(xServiceMaster.getIsAllowMultipleQuantity()=='\u0000'?'Y':xServiceMaster.getIsAllowMultipleQuantity());
				xServiceMaster.setIsDoctorRequired(xServiceMaster.getIsDoctorRequired()=='\u0000'?'Y':xServiceMaster.getIsDoctorRequired());
				xServiceMaster.setIsTaxApplicable(xServiceMaster.getIsTaxApplicable()=='\u0000'?'Y':xServiceMaster.getIsTaxApplicable());
				xServiceMaster.setIsAutorendered(xServiceMaster.getIsAutorendered()=='\u0000'?'N':xServiceMaster.getIsAutorendered());
				xServiceMaster.setStatus('A');
				xServiceMaster = save(xServiceMaster);
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, xServiceMaster);
			}
		} catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("saveOrganizationServiceMaster failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response changeOrganizationServiceMasterStatus(ServiceMasterDto xServiceMasterDto)
			throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().getNamedQuery("CHANGE_GLOBAL_SERVICE_MASTER_STATUS")
					.setParameter("serviceMasterId", xServiceMasterDto.getServiceMasterId())
					.setParameter("organizationId", xServiceMasterDto.getOrganizationId())
					.setParameter("status", xServiceMasterDto.getStatus()).executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("changeOrganizationServiceMasterStatus failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response listOrganizationServiceMasterMaster(Integer orgId, Integer offset, Integer recordPerPage)
			throws ApplicationException {
		try {
			List<ServiceMasterDto> listXServiceMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_SERVICE_MASTER_LIST").setParameter("orgId", orgId)
					.setFirstResult(offset != null ? offset : 0)
					.setMaxResults(recordPerPage != null ? recordPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listXServiceMasterDto, null);
		} catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("listOrganizationServiceMasterMaster failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getOrganizationServiceMasterTotalCount(Integer orgId) throws ApplicationException {
		try {
			BigInteger serviceMasterCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_MASTER_COUNT").setParameter("orgId", orgId).uniqueResult();
			if (serviceMasterCount.compareTo(BigInteger.ZERO) == 1) {
				return new Response(SUCCESS, SUCCESS_CODE, null, null, serviceMasterCount);
			} else {
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
			}
		} catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("getOrganizationServiceMasterTotalCount failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getOrganizationServiceMasterById(ServiceMasterDto xServiceMasterDto) throws ApplicationException {
		try {
			xServiceMasterDto = (ServiceMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_MASTER_BY_ID")
					.setParameter("serviceMasterId", xServiceMasterDto.getServiceMasterId())
					.setParameter("orgId", xServiceMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, xServiceMasterDto);
		} catch (Exception e) {
		logger.error("getOrganizationServiceMasterById failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response updateOrganizationServiceMaster(ServiceMasterDto xServiceMasterDto) throws ApplicationException {
		try {
			// ----------- check if Service Name alreay exist ---------------//
			Long count = (Long) sessionFactory.getCurrentSession()
					.createQuery(
							"select count(*) from ServiceMaster mst "
							+ "where (lower(mst.serviceStandardName) = lower(:serviceStandardName) OR lower(mst.serviceStandardCode) = lower(:serviceStandardCode)) "
							+ "and mst.serviceMasterId!=:serviceMasterId "
							+ "and mst.organizationId=:orgId ")
					.setParameter("serviceStandardName", xServiceMasterDto.getServiceStandardName())
					.setParameter("serviceStandardCode", xServiceMasterDto.getServiceStandardCode())
					.setParameter("serviceMasterId", xServiceMasterDto.getServiceMasterId())
					.setParameter("orgId", xServiceMasterDto.getOrganizationId())
					.uniqueResult();
			boolean exists = count > 0;
			if (exists)
				return new Response(ERROR, ALREADY_EXIST_CODE, SERVICE_NAME_ALREADY_EXIST, null, null);
			else {
				ServiceMaster xServiceMaster = mapper.map(xServiceMasterDto, ServiceMaster.class,
						"billing_XServiceMasterDto_to_XServiceMaster");
				xServiceMaster.setIsOutsourceService(xServiceMaster.getIsOutsourceService()=='\u0000'?'N':xServiceMaster.getIsOutsourceService());
				xServiceMaster.setIsAvailableForPackage(xServiceMaster.getIsAvailableForPackage()=='\u0000'?'A':xServiceMaster.getIsAvailableForPackage());
				xServiceMaster.setIsOtProcedure(xServiceMaster.getIsOtProcedure()=='\u0000'?'N':xServiceMaster.getIsOtProcedure());
				xServiceMaster.setIsOrderSet(xServiceMaster.getIsOrderSet()=='\u0000'?'N':xServiceMaster.getIsOrderSet());
				xServiceMaster.setIsRateEditable(xServiceMaster.getIsRateEditable()=='\u0000'?'N':xServiceMaster.getIsRateEditable());
				xServiceMaster.setIsDiscountApplicable(xServiceMaster.getIsDiscountApplicable()=='\u0000'?'Y':xServiceMaster.getIsDiscountApplicable());
				xServiceMaster.setIsAllowMultipleQuantity(xServiceMaster.getIsAllowMultipleQuantity()=='\u0000'?'Y':xServiceMaster.getIsAllowMultipleQuantity());
				xServiceMaster.setIsDoctorRequired(xServiceMaster.getIsDoctorRequired()=='\u0000'?'Y':xServiceMaster.getIsDoctorRequired());
				xServiceMaster.setIsTaxApplicable(xServiceMaster.getIsTaxApplicable()=='\u0000'?'Y':xServiceMaster.getIsTaxApplicable());
				xServiceMaster.setIsAutorendered(xServiceMaster.getIsAutorendered()=='\u0000'?'N':xServiceMaster.getIsAutorendered());
				xServiceMaster.setStatus('A');
				xServiceMaster = update(xServiceMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, xServiceMaster);
			}
		} catch (Exception e) {
		logger.error("updateOrganizationServiceMaster failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response listServiceMasterWithBasePriceByVisitType(Integer orgId, Integer unitId, Integer visitTypeId,
			Integer offset, Integer recordPerPage) throws ApplicationException {
		try {
			List<ServicePriceDetailsDto> listXServiceMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_SERVICE_MASTER_LIST_WITH_BASEPRICE_BYVISITTYPE")
					.setParameter("unitId", unitId)
					.setParameter("orgId", orgId)
					.setParameter("visitTypeId", visitTypeId)
					.setFirstResult(offset != null ? offset : 0)
					.setMaxResults(recordPerPage != null ? recordPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(ServicePriceDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listXServiceMasterDto, null);
		} catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("listServiceMasterWithBasePriceByVisitType failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getOrganizationServiceMasterBySpecialityAndSubSpeciality(ServiceMasterDto xServiceMasterDto)
			throws ApplicationException {
		try {
			List<ServiceMasterDto> listXServiceMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_MASTER_BY_SPECIALITY_AND_SUB_SPECIALITY")
					.setParameter("orgId", xServiceMasterDto.getOrganizationId())
					.setParameter("specialityId", xServiceMasterDto.getSpecialityId())
					.setParameter("subSpecialityId", xServiceMasterDto.getSubSpecialityId())
					.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();
			List al = new ArrayList<>();
			for (Iterator itr = listXServiceMasterDto.iterator(); itr.hasNext();)
			{
				ServiceMasterDto serviceMasterDto2 = (ServiceMasterDto) itr.next();
				al.add(serviceMasterDto2.getServiceMasterId());
			}
			Response res = checkServiceAvailableInUnitServiceMapper(al,xServiceMasterDto.getOrganizationId(),xServiceMasterDto.getUnitId());
			List<Integer> al1 = new LinkedList();
			List<Integer> al2 = new LinkedList<>();
			if (res.getListObject()!=null && res.getListObject().size() > 0) {
				al1 = res.getListObject();

				for (Iterator itr = al1.iterator(); itr.hasNext();)
				{
					UnitServiceMapperDto unitServiceMapperDto = (UnitServiceMapperDto) itr.next();
					al2.add(unitServiceMapperDto.getServiceId());
				}
			}
			List<ServiceMasterDto> listXServiceMasterDto2 ;
			String getOutPatientQuery = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_MASTER_BY_SPECIALITY_AND_SUB_SPECIALITY_NOT_MAPPED").getQueryString()
					.toString();
			StringBuilder getOutPatientSearchQueryPart1 = new StringBuilder(getOutPatientQuery);

			if (al2.size() > 0) {

				String text =  al2.toString().replace("[", "").replace("]", "");

				getOutPatientSearchQueryPart1.append(" AND service.service_master_id NOT IN ("+text+")");
				  Query createdQuery=sessionFactory.getCurrentSession().createSQLQuery(getOutPatientSearchQueryPart1.toString());

					listXServiceMasterDto2 = createdQuery
							.setParameter("orgId", xServiceMasterDto.getOrganizationId())
							.setParameter("specialityId", xServiceMasterDto.getSpecialityId())
							.setParameter("subSpecialityId", xServiceMasterDto.getSubSpecialityId())
							.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();
			}
			else{
				Query createdQuery = sessionFactory.getCurrentSession()
						.createSQLQuery(getOutPatientSearchQueryPart1.toString());

				 listXServiceMasterDto2 = createdQuery
						.setParameter("orgId", xServiceMasterDto.getOrganizationId())
						.setParameter("specialityId", xServiceMasterDto.getSpecialityId())
						.setParameter("subSpecialityId", xServiceMasterDto.getSubSpecialityId())
						.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, null, listXServiceMasterDto2, null);
		} catch (Exception e) {
		logger.error("getOrganizationServiceMasterBySpecialityAndSubSpeciality failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	public Response checkServiceAvailableInUnitServiceMapper(List al,Integer orgId,Integer unitId) throws ApplicationException {
		try {
			if(al.size()>0){
			List<UnitServiceMapperDto> serviceMasterDtolist = sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_SERVICE_AVAILABLE_IN_UNIT_SERVICE_MAPPER")
					.setParameterList("serviceId", al)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(UnitServiceMapperDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, serviceMasterDtolist, null);
			}else{
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}
		} catch (Exception e) {
		logger.error("checkServiceAvailableInUnitServiceMapper failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}



	@Override
	public Response listServiceMasterByServiceId(Integer orgId, Integer unitId, List<Integer> listServiceId)
			throws ApplicationException {
		// return list of services details to calling method 17-01-2018

		try {

			List<ServicePriceDetailsDto> listServiceMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_DETAILS_WITH_BASEPRICE_BYSERVICELIST")
					.setParameterList("listServiceId", listServiceId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(ServicePriceDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
		} catch (Exception e) {
		logger.error("listServiceMasterByServiceId failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getServiceDetailsByServiceId(ServiceMasterDto serviceMasterDto) throws ApplicationException {
		try {
			serviceMasterDto = (ServiceMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_DETAILS_BY_ID")
					.setParameter("serviceId", serviceMasterDto.getServiceMasterId())
					.setParameter("unitId", serviceMasterDto.getUnitId())
					.setParameter("orgId", serviceMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, serviceMasterDto);
		} catch (Exception e) {
		logger.error("getServiceDetailsByServiceId failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getConsultationService(Integer orgId,Integer unitId,Integer splId,Integer subSplId) throws ApplicationException {
		try{

		if(splId==0 && subSplId==0)	{
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
				unitApplicationConfigurationDto.setOrganizationId(orgId);
				unitApplicationConfigurationDto.setUnitId(unitId);
			Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);

			if(appConfigRes.getObject()!=null)
			{
				UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto)appConfigRes.getObject();


				if(unitApplicationConfigurationDtos.getConsultationSpecialityId()==null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Speciality id for consultation is not defined !!", null, null);
				else
					splId=unitApplicationConfigurationDtos.getConsultationSpecialityId();

				if(unitApplicationConfigurationDtos.getConsultationSubSpecialityId()==null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Sub Speciality id for consultation is not defined !!", null, null);
				else
					subSplId=unitApplicationConfigurationDtos.getConsultationSubSpecialityId();

			}else{
				return new Response<>(ERROR, COMMON_ERROR_CODE, "Unit Application Configuration is yet to set !!", null, null);
			}
		}
			List<ServiceMasterDto> serviceMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICES_BY_UNIT_AND_ORG")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setParameter("specialityId", splId)
					.setParameter("subSpecialityId", subSplId)
					.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, serviceMasterDtosList, null);
		}catch(HibernateException e){
		logger.error("getConsultationService failed", e);
		}catch(Exception e){
		logger.error("getConsultationService failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getAutoCompleteServicesLab(int orgId, int orgUnitId, int deptId, String serviceName)
			throws ApplicationException {
		List<CommonDto>  listAutoComplete  = null;
		try
		{
			listAutoComplete = (List<CommonDto>) sessionFactory.getCurrentSession()
					.createSQLQuery("select "
							+ "	service_mst.service_master_id as id, "
							+ "	service_mst.service_standard_name as name "
							+ "from "
							+ "	service.t_unit_service_mapper mppr "
							+ "inner join service.m_service_master service_mst on "
							+ "	mppr.service_id = service_mst.service_master_id "
							+ "where "
							+ "	mppr.orgnisation_id = :orgId "
							+ "	and mppr.unit_id = :unitId "
							+ "	and service_mst.status = 'A' "
							+ "	and mppr.status = 'A' "
							+ "	and service_mst.speciality_id =:deptId "
							+ "	and lower(trim(service_mst.service_standard_name)) like :serviceName "
							+ " and service_mst.service_master_id not in "
							+ " (select coalesce(testMstr.service_id) from lab.m_test_master testMstr where testMstr.org_unit_id = :unitId and testMstr.test_status ='A' and testMstr.service_id is not null)")
					.setParameter("orgId", orgId)
					.setParameter("unitId", orgUnitId)
					.setParameter("deptId", deptId)
					.setParameter("serviceName", "%"+serviceName.trim().toLowerCase()+"%")
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, listAutoComplete, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override

	public Response getListOfServiceMaster(Integer orgId, Integer unitId) throws ApplicationException {
		try{
			List<ServiceMasterDto> list = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_LIST_OF_SERVICE_MASTER")
				.setParameter("orgId", orgId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();
		return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
	}catch(HibernateException e){
	logger.error("getListOfServiceMaster failed", e);
	}catch(Exception e){
	logger.error("getListOfServiceMaster failed", e);
	}
	return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
}



	public Response readCSVFile(List<ServiceMasterDto> services) throws ApplicationException {
		int x[];
		Connection connection =null;
		PreparedStatement statement =null;
			try {
					connection = driverManager.getConnection();
					connection.setAutoCommit(false);

					 statement = connection.prepareStatement("INSERT INTO service.m_service_master "
			        		+ "(organization_id, service_standard_code,service_standard_name,created_by,updated_by,speciality_id,sub_speciality_id, general_ledger_id)"
			        		+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?);");

			        int i = 0;

			        for (ServiceMasterDto entity : services)
			        {
			        	statement.setInt(1, entity.getOrganizationId());
			        	statement.setString(2, entity.getServiceStandardCode());
			        	statement.setString(3, entity.getServiceStandardName());
			        	statement.setInt(4,entity.getCreatedBy());
			        	statement.setInt(5,entity.getUpdatedBy());
			        	statement.setInt(6,entity.getSpecialityId());
			        	statement.setInt(7,entity.getSubSpecialityId());
			        	statement.setInt(8,entity.getGeneralLedgerId());
			            statement.addBatch();
			            i++;

			            if (i % 500 == 0 || i == services.size())
			            {
			               x = statement.executeBatch(); // Execute every 500 items.
			                connection.commit();
			                //System.out.println(x);
			            }
			        }
			} catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("readCSVFile failed", e);
		}finally {
			if (statement != null) {
                try {
                    statement.close();
					connection.setAutoCommit(true);
					connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getServiceDetailsById(ServiceMasterDto serviceMasterDto) throws ApplicationException {
		try {
			serviceMasterDto = (ServiceMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_MASTER_DETAILS_BY_SERVICE_MASTER_ID")
					.setParameter("serviceMasterId", serviceMasterDto.getServiceMasterId())
					.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, serviceMasterDto);
		} catch (Exception e) {
		logger.error("getServiceDetailsById failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getSurchargeServiceDetails(ServiceMasterDto serviceMasterDto) throws ApplicationException {
		try {


			    Integer id  =  serviceMasterDto.getServiceMasterId();
				OrderDetailsDiscountDto OrderDetailsDiscountDtos = new OrderDetailsDiscountDto();
				String query = null;
				if(serviceMasterDto.getAdmissionId()!=null)
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
							 + " 	and pc1.markup_status=true "
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
							 + " 	and pc1.markup_status=true "
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
							 + " 	and pc3.markup_status=true "
							 + " union "
							 + " select "
							 + " 	0 as \"patientClassPercentage\", "
							 + " 	true as \"markupStatus\", "
							 + " 	4 as \"priorityId\" , "
							 + " 	0 as \"id\" "
							 + " order by "
							 + " 	\"priorityId\",\"id\" desc "
							 + " limit 1 " ;

					ContractConsumptionDto dto = (ContractConsumptionDto)sessionFactory.getCurrentSession()
							.createSQLQuery(query.toString())
							.setParameter("admissionId", serviceMasterDto.getAdmissionId())
							.setParameter("serviceId", id)
							.setParameter("patientCategoryId", serviceMasterDto.getPatientCategoryId()!=null ? serviceMasterDto.getPatientCategoryId() : serviceMasterDto.getPatientTypeId())
							.setParameter("visitTypeId", serviceMasterDto.getVisitTypeId())
							.setParameter("tariffId", serviceMasterDto.getTariffId()!=null ? serviceMasterDto.getTariffId() : serviceMasterDto.getDefaultSelfTariffId())
							.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
							.uniqueResult();

					OrderDetailsDiscountDtos.setDiscountPercentage(new BigDecimal(dto.getPatientClassPercentage()));
					OrderDetailsDiscountDtos.setServiceId(id);
				}
				if(serviceMasterDto.getEncounterId()!=null)
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
							 + " 	and pc1.markup_status=true "
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
							 + " 	and pc1.markup_status=true "
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
							 + " 	and pc3.markup_status=true "
							 + " union "
							 + " select "
							 + " 	0 as \"patientClassPercentage\", "
							 + " 	true as \"markupStatus\", "
							 + " 	4 as \"priorityId\" , "
							 + " 	0 as \"id\" "
							 + " order by "
							 + " 	\"priorityId\",\"id\" desc "
							 + " limit 1 " ;


					ContractConsumptionDto dto = (ContractConsumptionDto)sessionFactory.getCurrentSession()
							.createSQLQuery(query.toString())
							.setParameter("encounterId", serviceMasterDto.getEncounterId())
							.setParameter("serviceId", id)
							.setParameter("patientCategoryId", serviceMasterDto.getPatientCategoryId()!=null ? serviceMasterDto.getPatientCategoryId() : serviceMasterDto.getPatientTypeId())
							.setParameter("visitTypeId", serviceMasterDto.getVisitTypeId())
							.setParameter("tariffId", serviceMasterDto.getTariffId())
							.setResultTransformer(Transformers.aliasToBean(ContractConsumptionDto.class))
							.uniqueResult();

					OrderDetailsDiscountDtos.setDiscountPercentage(new BigDecimal(dto.getPatientClassPercentage()));
					OrderDetailsDiscountDtos.setServiceId(id);
				}

			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, OrderDetailsDiscountDtos);
		} catch (Exception e) {
		logger.error("getSurchargeServiceDetails failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getCancelServices(Integer admissionId, Integer visitTypeId,Integer orgId) throws ApplicationException {
		try {
			 StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_CANCEL_SERVICES_BY_ENCOUNTER_ID").getQueryString());
//			 stringQuery.append(" AND orderDetails.ordCancelled='Y' AND orderDetails.status='I' ");

			 List<OrderDetailsByEncounterIdDto> listServiceMasterDto = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString())
						.setParameter("encounterId", admissionId)
						.setParameter("visitTypeId", visitTypeId)
						.setParameter("orgId", orgId)
						.setResultTransformer(Transformers.aliasToBean(OrderDetailsByEncounterIdDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
		} catch (Exception e) {
		logger.error("getCancelServices failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getAutoRenderedServiceDetails(Integer orgId, Integer unitId) throws ApplicationException {
		try {

			String query = " SELECT service.service_master_id as \"serviceMasterId\", "
					+ " service.service_standard_name as \"serviceStandardName\" "
					+ " FROM service.t_unit_service_mapper mapper "
					+ " INNER JOIN service.m_service_master service ON service.service_master_id = mapper.service_id "
					+ " INNER JOIN service.t_unit_service_master_details serviceDtls ON service.service_master_id = serviceDtls.service_master_id "
					+ " WHERE mapper.unit_id = serviceDtls.unit_id "
					+ " AND mapper.orgnisation_id=:orgId"
					+ " AND mapper.status ='A' "
					+ " AND mapper.unit_id =:unitId "
					+ " AND serviceDtls.is_auto_render='Y' "
					+ " ORDER BY service.service_standard_name ASC ";

			List<ServiceMasterDto> serviceMasterDtosList = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, serviceMasterDtosList, null);
		} catch (Exception e) {
		logger.error("getAutoRenderedServiceDetails failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getConsultationDeptByServiceId(Integer serviceId, Integer unitId) throws ApplicationException {
		try {

			String query = "select "
				 + " 	smaster.speciality_id as \"specialityId\", "
				 + " 	smaster.speciality_name as \"specialityName\", "
				 + " 	usmp.dept_wise_appointment as \"deptWiseAppointment\" "
				 + " from "
				 + " 	doctor.m_speciality_master smaster "
				 + " 	inner join t_unit_speciality_mapper usmp  "
				 + " 		on usmp.speciality_id = smaster.speciality_id  "
				 + " where "
				 + " 	usmp.is_clinical_speciality = 'Y' "
				 + " 	and usmp.unit_id = :unitId "
				 + " 	and smaster.status = 'A' "
				 + " order by "
				 + " 	smaster.speciality_name ";

			List<ServiceMasterDto> serviceMasterDtosList = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					/* .setParameter("serviceId", serviceId) */
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, serviceMasterDtosList, null);
		} catch (Exception e) {
		logger.error("getConsultationDeptByServiceId failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response serviceMasterUniversalSearch(ServiceMasterDto serviceMasterDto) throws ApplicationException {
		try {
			List<ServiceMasterDto> listServiceMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_SERVICE_MASTER_DETAILS_LIST_SEARCH")
					.setParameter("orgId", serviceMasterDto.getOrganizationId())
					.setParameter("serviceStandardName", "%"+serviceMasterDto.getServiceStandardName().trim().toLowerCase()+"%")
					.setFirstResult(serviceMasterDto.getOffset() != null ? serviceMasterDto.getOffset() : 0)
					.setMaxResults(serviceMasterDto.getNoOfRecordsPerPage() != null ? serviceMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, null);
		}catch (HibernateException he) {
		logger.error("serviceMasterUniversalSearch failed", he);
		} catch (Exception e) {
		logger.error("serviceMasterUniversalSearch failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public CommonResponse checkForUniqueServiceCode(ServiceMasterDto xServiceMasterDto) throws ApplicationException {
		try {
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("CHECK_FOR_UNIQUE_SERVICE_CODE").getQueryString().toString());
			if(xServiceMasterDto.getServiceMasterId()!=null) {
				query.append(" AND service.service_master_id !="+xServiceMasterDto.getServiceMasterId());
			}
			List<ServiceMasterDto> serviceMasterList = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString())
					.setParameter("orgId", xServiceMasterDto.getOrganizationId())
					.setParameter("serviceStandardCode", xServiceMasterDto.getServiceStandardCode().trim().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();
			return new CommonResponse<>(SUCCESS, SUCCESS_CODE, null, serviceMasterList, null);

		} catch (HibernateException he) {
		logger.error("checkForUniqueServiceCode failed", he);
		} catch (Exception e) {
		logger.error("checkForUniqueServiceCode failed", e);
		}
		return new CommonResponse<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public CommonResponse checkForUniqueServiceName(ServiceMasterDto xServiceMasterDto) throws ApplicationException {
		try {
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("CHECK_FOR_UNIQUE_SERVICE_NAME").getQueryString().toString());
			if(xServiceMasterDto.getServiceMasterId()!=null) {
				query.append(" AND service.service_master_id !="+xServiceMasterDto.getServiceMasterId());
			}
			List<ServiceMasterDto> serviceMasterList = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString())
					.setParameter("orgId", xServiceMasterDto.getOrganizationId())
					.setParameter("serviceStandardName", xServiceMasterDto.getServiceStandardName().trim().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();
			return new CommonResponse<>(SUCCESS, SUCCESS_CODE, null, serviceMasterList, null);

		} catch (HibernateException he) {
		logger.error("checkForUniqueServiceName failed", he);
		} catch (Exception e) {
		logger.error("checkForUniqueServiceName failed", e);
		}
		return new CommonResponse<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllServicesByOrgId(Integer orgId)throws ApplicationException {
		try {
			List<ServiceMasterDto> listXServiceMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ALL_SERVICE_MASTER_LIST")
					.setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listXServiceMasterDto, null);
		} catch (HibernateException he) {
			logger.error("Exection", he);
		} catch (Exception e) {
		logger.error("getAllServicesByOrgId failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response listOfServicesForWebAppIntegration(Integer orgId, Integer unitId) throws ApplicationException {
		try{
			List<ServiceMasterDto> list = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_LIST_OF_SERVICES_FOR_WEBAPP_INTEGRATION")
				.setParameter("orgId", orgId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();
		return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
	}catch(HibernateException e){
	logger.error("listOfServicesForWebAppIntegration failed", e);
	}catch(Exception e){
	logger.error("listOfServicesForWebAppIntegration failed", e);
	}
	return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getOutSourcedService(Integer unitId,String serviceName) throws ApplicationException {
		try {

			String query = " SELECT service.service_master_id as \"serviceMasterId\", "
					+ " service.service_standard_name as \"serviceStandardName\" "
					+ " FROM service.t_unit_service_mapper mapper "
					+ " INNER JOIN service.m_service_master service ON service.service_master_id = mapper.service_id "
					+ " INNER JOIN service.t_unit_service_master_details serviceDtls ON service.service_master_id = serviceDtls.service_master_id "
					+ " WHERE mapper.unit_id = serviceDtls.unit_id "
					+ " AND mapper.status ='A' AND service.status='A' "
					+ " AND mapper.unit_id =:unitId "
					//+ "	and lower(trim(service.service_standard_name)) like :serviceName "
					+ " AND serviceDtls.is_outsource_job='Y' "
					+ " ORDER BY service.service_standard_name ASC ";

			List<ServiceMasterDto> serviceMasterDtosList = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("unitId", unitId)
					//.setParameter("serviceName", "%"+serviceName.trim().toLowerCase()+"%")
					.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, serviceMasterDtosList, null);
		} catch (Exception e) {
		logger.error("getOutSourcedService failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}
}

