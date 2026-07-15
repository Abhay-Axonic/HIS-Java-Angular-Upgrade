package com.param.pharmacy.master.global.dao.equipment;

import java.math.BigInteger;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.entity.model.pharmacy.GenericScheduleMaster;
import com.param.entity.model.pharmacy.GenericScheduleServiceCode;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.ServiceMasterDto;
import com.param.pharmacy.master.global.dto.equipment.GenericScheduleMasterDto;
import com.param.pharmacy.master.global.dto.equipment.ScheduleMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GenericScheduleMasterrDaoImpl extends GenericDao<GenericScheduleMaster, Integer>
		implements IGenericScheduleMasterDao, ICommonConstants, IError {

	public GenericScheduleMasterrDaoImpl() {
		super(GenericScheduleMaster.class);
	}

	final static Logger logger = Logger.getLogger(GenericScheduleMaster.class);

	@Override
	public Response listScheduleDesc(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			List<ScheduleMasterDto> listTechniqueMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SCHEDULE_LIST")
					.setParameter("orgId", orgId)
//					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(ScheduleMasterDto.class)).list();
			return new Response(SUCCESS, null, null, listTechniqueMasterDto, null);

		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response searchServicesByNameAndCode(ServiceMasterDto serviceMasterDto) throws ApplicationException {
		try {
			String[] searchString = serviceMasterDto.getSearchKeyword().split("---");
			//System.out.println(searchString.toString());
			String searchStr = null;
			if (searchString.length > 1) {
				searchStr = searchString[0];
			} else {
				searchStr = searchString[0];
			}
			//System.out.println(searchStr.toString());
			//System.out.println(searchStr);
			List<ServiceMasterDto> trustMtrDto = sessionFactory.getCurrentSession().createSQLQuery(
					" select ser.service_master_id as \"serviceMasterId\",ser.service_standard_name as \"serviceStandardName\",service_standard_code as \"serviceStandardCode\" "
							+ " from service.m_service_master ser "
							+ " join public.t_lab_configuration lab on ser.speciality_id=lab.lab_speciality_id "
							+ " WHERE " + " ser.organization_id=:orgId " + " AND lab.unit_id =:unitId "
							+ " AND ser.service_standard_name  like '%" + searchStr.toLowerCase() + "%'")
					.setParameter("unitId", serviceMasterDto.getUnitId())
					.setParameter("orgId", serviceMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, trustMtrDto, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveGenericScheduleMaster(GenericScheduleMasterDto genericScheduleMasterDto)
			throws ApplicationException {
		try {
			if(genericScheduleMasterDto.getGenericId() != null && genericScheduleMasterDto.getScheduleId() != null) {

				Response resCheck = checkGenericScheduleExistOrNot(genericScheduleMasterDto);
				if (resCheck.getStatus().equals(SUCCESS) && resCheck.getListObject() != null && !resCheck.getListObject().isEmpty()) {
					return new Response(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
				}

				GenericScheduleMaster genericScheduleMaster = new GenericScheduleMaster();
				genericScheduleMaster.setGenericId(genericScheduleMasterDto.getGenericId());
				genericScheduleMaster.setScheduleId(genericScheduleMasterDto.getScheduleId());
				genericScheduleMaster.setIsEmergencyMedicine(genericScheduleMasterDto.getIsEmergencyMedicine());
				genericScheduleMaster.setIsInvestgation(genericScheduleMasterDto.getIsInvestgation());
				genericScheduleMaster.setStatus(genericScheduleMasterDto.getStatus());
				genericScheduleMaster.setUnitId(genericScheduleMasterDto.getUnitId());
				genericScheduleMaster.setOrgId(genericScheduleMasterDto.getOrgId());
				genericScheduleMaster.setCreatedBy(genericScheduleMasterDto.getCreatedBy());
				genericScheduleMaster.setCreatedDate(TimeZoneComponent.getDateByzone(genericScheduleMasterDto.getUnitId()));
				genericScheduleMaster.setUpdatedBy(genericScheduleMasterDto.getUpdatedBy());
				genericScheduleMaster.setUpdatedBy(genericScheduleMasterDto.getUpdatedBy());
				sessionFactory.getCurrentSession().save(genericScheduleMaster);

				for (GenericScheduleMasterDto serviceCodeIdObj : genericScheduleMasterDto.getServiceCodeId()) {
					GenericScheduleServiceCode genericScheduleServiceCode = new GenericScheduleServiceCode();
					genericScheduleServiceCode.setServiceMasterId(serviceCodeIdObj.getId());
					genericScheduleServiceCode.setGenericScheduleId(genericScheduleMaster.getGenericScheduleId());
					genericScheduleServiceCode.setUnitId(genericScheduleMasterDto.getUnitId());
					genericScheduleServiceCode.setOrgId(genericScheduleMasterDto.getOrgId());
					genericScheduleServiceCode.setCreatedBy(genericScheduleMasterDto.getCreatedBy());
					genericScheduleServiceCode.setStatus('A');
					genericScheduleServiceCode.setCreatedDate(TimeZoneComponent.getDateByzone(genericScheduleMasterDto.getUnitId()));
					genericScheduleServiceCode.setUpdatedBy(genericScheduleMasterDto.getCreatedBy());
					genericScheduleServiceCode.setUpdatedDate(TimeZoneComponent.getDateByzone(genericScheduleMasterDto.getUnitId()));
					sessionFactory.getCurrentSession().save(genericScheduleServiceCode);
				}
				return new Response<>(SUCCESS, null, null, null, null);
			}
			return new Response(ERROR, null, null, null, null);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response updateGenericScheduleMaster(GenericScheduleMasterDto genericScheduleMasterDto)
			throws ApplicationException {
		try {
			GenericScheduleMaster genericScheduleMaster = new GenericScheduleMaster();
			genericScheduleMaster.setGenericScheduleId(genericScheduleMasterDto.getGenericScheduleId());
			genericScheduleMaster.setGenericId(genericScheduleMasterDto.getGenericId());
			genericScheduleMaster.setScheduleId(genericScheduleMasterDto.getScheduleId());
			genericScheduleMaster.setIsEmergencyMedicine(genericScheduleMasterDto.getIsEmergencyMedicine());
			genericScheduleMaster.setIsInvestgation(genericScheduleMasterDto.getIsInvestgation());
			genericScheduleMaster.setStatus(genericScheduleMasterDto.getStatus());
			genericScheduleMaster.setUnitId(genericScheduleMasterDto.getUnitId());
			genericScheduleMaster.setOrgId(genericScheduleMasterDto.getOrgId());
			genericScheduleMaster.setCreatedBy(genericScheduleMasterDto.getCreatedBy());
			genericScheduleMaster.setCreatedDate(TimeZoneComponent.getDateByzone(genericScheduleMasterDto.getUnitId()));
			genericScheduleMaster.setUpdatedBy(genericScheduleMasterDto.getUpdatedBy());
			genericScheduleMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(genericScheduleMasterDto.getUnitId()));
			sessionFactory.getCurrentSession().update(genericScheduleMaster);
			Integer count = sessionFactory.getCurrentSession().createQuery(
					" update GenericScheduleServiceCode tvm set status='I' where tvm.genericScheduleId=:genericScheduleId ")
					.setParameter("genericScheduleId", genericScheduleMasterDto.getGenericScheduleId()).executeUpdate();

			for (GenericScheduleMasterDto serviceCodeIdObj : genericScheduleMasterDto.getServiceCodeId()) {
				GenericScheduleServiceCode genericScheduleServiceCode = new GenericScheduleServiceCode();
				genericScheduleServiceCode.setServiceMasterId(serviceCodeIdObj.getId());
				genericScheduleServiceCode.setGenericScheduleId(genericScheduleMaster.getGenericScheduleId());
				genericScheduleServiceCode.setUnitId(genericScheduleMasterDto.getUnitId());
				genericScheduleServiceCode.setOrgId(genericScheduleMasterDto.getOrgId());
				genericScheduleServiceCode.setCreatedBy(genericScheduleMasterDto.getCreatedBy());
				genericScheduleServiceCode.setStatus('A');
				genericScheduleServiceCode.setCreatedDate(TimeZoneComponent.getDateByzone(genericScheduleMasterDto.getUnitId()));
				genericScheduleServiceCode.setUpdatedBy(genericScheduleMasterDto.getCreatedBy());
				genericScheduleServiceCode.setUpdatedDate(TimeZoneComponent.getDateByzone(genericScheduleMasterDto.getUnitId()));
				sessionFactory.getCurrentSession().save(genericScheduleServiceCode);
			}
			return new Response<>(SUCCESS, null, null, null, null);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response activeInactiveGenericSchedule(Integer orgId, Integer genericScheduleId, Character status)
			throws ApplicationException {
		try {
			GenericScheduleMaster genericScheduleMaster = findById(genericScheduleId);
			if (genericScheduleMaster.getGenericScheduleId() != 0) {
				genericScheduleMaster.setStatus(status);
				GenericScheduleMaster departmentMst = update(genericScheduleMaster);
				if (departmentMst.getStatus() == ACTIVE) {
					return new Response(SUCCESS, null, "SCHEDULE_TEST_ACTIVATE_SUCC", null, null);
				} else if (departmentMst.getStatus() == INACTIVE) {
					return new Response(SUCCESS, null, "SCHEDULE_TEST_INACTIVATE_SUCC", null, null);
				} else {
					if (status == ACTIVE) {
						return new Response(SUCCESS, null, "SCHEDULE_ACTIVATE_FAIL", null, null);
					} else {
						return new Response(SUCCESS, null, "SCHEDULE_INACTIVATE_FAIL", null, null);
					}
				}
			} else {
				return new Response(SUCCESS, null, "SCHEDULE_NOT_FOUND", null, null);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, "SCHEDULE_NOT_FOUND", null, null);
		}
	}

	@Override
	public Response listGenericScheduleMaster(Integer orgId, Integer unitId, Integer offset, Integer recordPerPage)
			throws ApplicationException {
		try {
			List<GenericScheduleMasterDto> listTechniqueMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_GENERIC_SCHEDULE_MASTER_LIST").setParameter("orgId", orgId)
					.setParameter("unitId", unitId).setFirstResult(offset != null ? offset : 0)
					.setMaxResults(recordPerPage != null ? recordPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(GenericScheduleMasterDto.class)).list();

			if (listTechniqueMasterDto.isEmpty()) {
				return new Response(ERROR, ERR_500, "SCHEDULE_RECORDS_NOT_FOUND", null, null);
			} else {
				return new Response(SUCCESS, null, null, listTechniqueMasterDto, null);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getTotalGenericScheduleMasterRecord(Integer orgId, Integer unitId) {
		try {
			BigInteger genericscheduleMasterTotalRecordCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TOTAL_GENERIC_SCHEDULE_RECORD").setParameter("unitId", unitId)
					.setParameter("orgId", orgId).uniqueResult();

			if (genericscheduleMasterTotalRecordCount.compareTo(BigInteger.ZERO) == 1) {
				return new Response(SUCCESS, null, null, null, genericscheduleMasterTotalRecordCount);
			} else {
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getGenericScheduleMasterById(Integer orgId, Integer unitId, Integer genericScheduleId)
			throws ApplicationException {
		try {

			GenericScheduleMasterDto genericScheduleMasterDto = (GenericScheduleMasterDto) sessionFactory
					.getCurrentSession().getNamedQuery("GET_GENERIC_SCHEDULE_BY_SCHEDULE_ID").setParameter("orgId", orgId)
					.setParameter("genericScheduleId", genericScheduleId).setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(GenericScheduleMasterDto.class)).uniqueResult();
			List<GenericScheduleMasterDto> servicecodeDto = sessionFactory.getCurrentSession().createSQLQuery(
					" select gsm.generic_schedule_service_code_id as \"genericScheduleServiceCodeId\",gsm.generic_schedule_id as \"genericScheduleId\",ser.service_standard_name as \"serviceStandardName\",ser.service_master_id as \"serviceMasterId\" "
							+ " from pharmacy.m_generic_schedule_service_code gsm "
							+ " join service.m_service_master ser on gsm.service_master_id=ser.service_master_id "
							+ " WHERE " + " gsm.generic_schedule_id=:genericScheduleId" + " AND gsm.org_id=:orgId "
							+ " AND gsm.unit_id =:unitId " + " AND gsm.status='A'")
					.setParameter("genericScheduleId", genericScheduleId).setParameter("unitId", unitId)
					.setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(GenericScheduleMasterDto.class)).list();
			return new Response(SUCCESS, null, null, servicecodeDto, genericScheduleMasterDto);

		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, "SCHEDULE_NOT_FOUND", null, null);
		}

	}

	@Override
	public Response listserviceCode(Integer orgId, Integer unitId) throws ApplicationException {
		try {
		/*	List<ServiceMasterDto> trustMtrDto = sessionFactory.getCurrentSession().createSQLQuery(
					" select ser.service_master_id as \"serviceMasterId\",ser.service_standard_name as \"serviceStandardName\",service_standard_code as \"serviceStandardCode\" "
							+ " from service.m_service_master ser "
							+ " join public.t_lab_configuration lab on ser.speciality_id=lab.lab_speciality_id "
							+ " WHERE " + " ser.organization_id=:orgId " + " AND lab.unit_id =:unitId ")
					.setParameter("unitId", unitId).setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();*/
			
			List<ServiceMasterDto> trustMtrDto = sessionFactory.getCurrentSession().createSQLQuery(
					" select " 
							 + " 	ser.service_master_id as \"serviceMasterId\", " 
							 + " 	ser.service_standard_name as \"serviceStandardName\", " 
							 + " 	service_standard_code as \"serviceStandardCode\" " 
							 + " from " 
							 + " 	service.m_service_master ser " 
							 + " inner join service.t_unit_service_mapper usm " 
							 + " 	on ser.service_master_id = usm.service_id  " 
							 + " join public.t_lab_configuration lab on " 
							 + " 	ser.speciality_id = lab.lab_speciality_id " 
							 + " where " 
							 + " 	ser.organization_id =:orgId " 
							 + " 	and usm.unit_id =:unitId " 
							 + " 	and usm.status ='A' " 
							 + " 	and lab.unit_id =:unitId " 
							 + " 	and ser.status ='A' "
							 + " ORDER BY ser.service_standard_name ASC ")
						.setParameter("orgId", orgId)
						.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(ServiceMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, trustMtrDto, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getServicecodeList(Integer genericScheduleId, Integer orgId, Integer unitId)
			throws ApplicationException {
		try {
			List<GenericScheduleMasterDto> trustMtrDto = sessionFactory.getCurrentSession().createSQLQuery(
					" select gsm.generic_schedule_service_code_id as \"genericScheduleServiceCodeId\",gsm.generic_schedule_id as \"genericScheduleId\",ser.service_standard_name as \"serviceStandardName\",ser.service_master_id as \"serviceMasterId\" "
							+ " from pharmacy.m_generic_schedule_service_code gsm "
							+ " join service.m_service_master ser on gsm.service_master_id=ser.service_master_id "
							+ " WHERE " + " gsm.generic_schedule_id=:genericScheduleId" + " AND gsm.org_id=:orgId "
							+ " AND gsm.unit_id =:unitId " + " AND gsm.status='A'")
					.setParameter("genericScheduleId", genericScheduleId).setParameter("unitId", unitId)
					.setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(GenericScheduleMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, trustMtrDto, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response checkGenericScheduleExistOrNot(GenericScheduleMasterDto genericScheduleMasterDto)
			throws ApplicationException {
		try {
			List<GenericScheduleMasterDto> genericScheduleList = sessionFactory.getCurrentSession().createSQLQuery(
							" select gsm.generic_schedule_id as \"genericScheduleId\" "
									+ " from pharmacy.m_generic_schedule_master gsm "
									+ " WHERE " + " gsm.generic_id = :genericId"
									+ " AND gsm.schedule_id = :scheduleId "
									+ " AND gsm.org_id=:orgId "
									+ " AND gsm.unit_id =:unitId " + " AND gsm.status='A'")
					.setParameter("genericId", genericScheduleMasterDto.getGenericId() != null ? genericScheduleMasterDto.getGenericId() : 0)
					.setParameter("scheduleId", genericScheduleMasterDto.getScheduleId() != null ? genericScheduleMasterDto.getScheduleId() : 0)
					.setParameter("unitId", genericScheduleMasterDto.getUnitId())
					.setParameter("orgId", genericScheduleMasterDto.getOrgId())
					.setResultTransformer(Transformers.aliasToBean(GenericScheduleMasterDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, genericScheduleList, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}
