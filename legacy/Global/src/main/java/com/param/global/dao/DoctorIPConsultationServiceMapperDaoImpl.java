package com.param.global.dao;

import java.math.BigInteger;
import java.util.List;


import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.DoctorIPConsultationServiceMapperDto;
import com.param.global.model.DoctorIPConsultationServiceMapper;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class DoctorIPConsultationServiceMapperDaoImpl extends GenericDao<DoctorIPConsultationServiceMapper, Integer> implements IDoctorIPConsultationServiceMapperDao, ICommonConstants{

	static final Logger logger = Logger.getLogger(DoctorIPConsultationServiceMapperDaoImpl.class);

	public DoctorIPConsultationServiceMapperDaoImpl() {
		super(DoctorIPConsultationServiceMapper.class);
	
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveDoctorIPConsultationServiceMapper(
			DoctorIPConsultationServiceMapperDto doctorIPConsultationServiceMapperDto) throws ApplicationException {
		try{
			DoctorIPConsultationServiceMapper doctorIPConsultationServiceMapper=new DoctorIPConsultationServiceMapper();
			doctorIPConsultationServiceMapper.setDoctorId(doctorIPConsultationServiceMapperDto.getDoctorId());
			doctorIPConsultationServiceMapper.setFirstConsultationServiceId(doctorIPConsultationServiceMapperDto.getFirstConsultationServiceId());
			doctorIPConsultationServiceMapper.setFollowupRoutineServiceId(doctorIPConsultationServiceMapperDto.getFollowupRoutineServiceId());
			doctorIPConsultationServiceMapper.setIcuPrimaryConsultationServiceId(doctorIPConsultationServiceMapperDto.getIcuPrimaryConsultationServiceId());
			doctorIPConsultationServiceMapper.setNoOfRoutineServices(doctorIPConsultationServiceMapperDto.getNoOfRoutineServices());
			doctorIPConsultationServiceMapper.setRoutineConsultationServiceId(doctorIPConsultationServiceMapperDto.getRoutineConsultationServiceId());
			doctorIPConsultationServiceMapper.setOrganizationId(doctorIPConsultationServiceMapperDto.getOrganizationId());
			doctorIPConsultationServiceMapper.setUnitId(doctorIPConsultationServiceMapperDto.getUnitId());
			doctorIPConsultationServiceMapper.setCreatedId(doctorIPConsultationServiceMapperDto.getCreatedId());
			doctorIPConsultationServiceMapper.setCreatedDate(GlobalCommonDateUtils.getDate(doctorIPConsultationServiceMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			doctorIPConsultationServiceMapper.setUpdatedId(doctorIPConsultationServiceMapperDto.getUpdatedId());
			doctorIPConsultationServiceMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(doctorIPConsultationServiceMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			doctorIPConsultationServiceMapper.setStatus(doctorIPConsultationServiceMapperDto.getStatus());
			
			save(doctorIPConsultationServiceMapper);
			
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	} catch (HibernateException he) {
		logger.error("saveDoctorIPConsultationServiceMapper failed", he);
	} catch (Exception e) {
		logger.error("saveDoctorIPConsultationServiceMapper failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDoctorIPConsultationServiceMapper(
			DoctorIPConsultationServiceMapperDto doctorIPConsultationServiceMapperDto) throws ApplicationException {
		try{
			DoctorIPConsultationServiceMapper doctorIPConsultationServiceMapper=findById(doctorIPConsultationServiceMapperDto.getDoctorIPConsultationServiceMapperId());
			doctorIPConsultationServiceMapper.setDoctorId(doctorIPConsultationServiceMapperDto.getDoctorId());
			doctorIPConsultationServiceMapper.setFirstConsultationServiceId(doctorIPConsultationServiceMapperDto.getFirstConsultationServiceId());
			doctorIPConsultationServiceMapper.setFollowupRoutineServiceId(doctorIPConsultationServiceMapperDto.getFollowupRoutineServiceId());
			doctorIPConsultationServiceMapper.setIcuPrimaryConsultationServiceId(doctorIPConsultationServiceMapperDto.getIcuPrimaryConsultationServiceId());
			doctorIPConsultationServiceMapper.setNoOfRoutineServices(doctorIPConsultationServiceMapperDto.getNoOfRoutineServices());
			doctorIPConsultationServiceMapper.setRoutineConsultationServiceId(doctorIPConsultationServiceMapperDto.getRoutineConsultationServiceId());
			doctorIPConsultationServiceMapper.setOrganizationId(doctorIPConsultationServiceMapperDto.getOrganizationId());
			doctorIPConsultationServiceMapper.setUnitId(doctorIPConsultationServiceMapperDto.getUnitId());
			doctorIPConsultationServiceMapper.setUpdatedId(doctorIPConsultationServiceMapperDto.getUpdatedId());
			doctorIPConsultationServiceMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(doctorIPConsultationServiceMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			
			update(doctorIPConsultationServiceMapper);
			
		return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	} catch (HibernateException he) {
		logger.error("updateDoctorIPConsultationServiceMapper failed", he);
	} catch (Exception e) {
		logger.error("updateDoctorIPConsultationServiceMapper failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getDoctorIPConsultationServiceMapperList(
			DoctorIPConsultationServiceMapperDto doctorIPConsultationServiceMapperDto) throws ApplicationException {
		try{
			List<DoctorIPConsultationServiceMapperDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_DOCTOR_IP_CONSULTATION_SERVICE_MAPPER_LIST")
				.setParameter("unitId", doctorIPConsultationServiceMapperDto.getUnitId())
				.setParameter("organizationId", doctorIPConsultationServiceMapperDto.getOrganizationId())
				.setFirstResult(doctorIPConsultationServiceMapperDto.getOffset() != null ? doctorIPConsultationServiceMapperDto.getOffset() : 0)
				.setMaxResults(doctorIPConsultationServiceMapperDto.getNoOfRecordsPerPage() != null ? doctorIPConsultationServiceMapperDto.getNoOfRecordsPerPage() : 10)
				.setResultTransformer(Transformers.aliasToBean(DoctorIPConsultationServiceMapperDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	} catch (HibernateException he) {
		logger.error("getDoctorIPConsultationServiceMapperList failed", he);
	} catch (Exception e) {
		logger.error("getDoctorIPConsultationServiceMapperList failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorIPConsultationServiceMapperById(Integer doctorIPConsultationServiceMapperId, Integer organizationId, Integer unitId) throws ApplicationException {
		try{
			List<DoctorIPConsultationServiceMapperDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_DOCTOR_IP_CONSULTATION_SERVICE_MAPPER_BY_ID")
				.setParameter("doctorIPConsultationServiceMapperId", doctorIPConsultationServiceMapperId)
				.setParameter("unitId", unitId)
				.setParameter("organizationId", organizationId)
				.setResultTransformer(Transformers.aliasToBean(DoctorIPConsultationServiceMapperDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	} catch (HibernateException he) {
		logger.error("getDoctorIPConsultationServiceMapperById failed", he);
	} catch (Exception e) {
		logger.error("getDoctorIPConsultationServiceMapperById failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Integer checkDoctorIPConsultationServiceMapperAvailable(
			DoctorIPConsultationServiceMapperDto doctorIPConsultationServiceMapperDto) throws ApplicationException {
		try{
			DoctorIPConsultationServiceMapperDto doctorIPConsultationServiceMapper=(DoctorIPConsultationServiceMapperDto)sessionFactory.getCurrentSession()
			.getNamedQuery("CHECK_DOCTOR_IP_CONSULTATION_SERVICE_MAPPER_AVAILABLE")
			.setParameter("organizationId", doctorIPConsultationServiceMapperDto.getOrganizationId())
			.setParameter("unitId", doctorIPConsultationServiceMapperDto.getUnitId())
			.setParameter("doctorId", doctorIPConsultationServiceMapperDto.getDoctorId())
			//.setParameter("firstConsultationServiceId", doctorIPConsultationServiceMapperDto.getFirstConsultationServiceId())
			//.setParameter("followupRoutineServiceId",doctorIPConsultationServiceMapperDto.getFollowupRoutineServiceId())
			//.setParameter("icuPrimaryConsultationServiceId", doctorIPConsultationServiceMapperDto.getIcuPrimaryConsultationServiceId())
			//.setParameter("noOfRoutineServices", doctorIPConsultationServiceMapperDto.getNoOfRoutineServices())
			//.setParameter("routineConsultationServiceId", doctorIPConsultationServiceMapperDto.getRoutineConsultationServiceId())
			.setResultTransformer(Transformers.aliasToBean(DoctorIPConsultationServiceMapperDto.class)).uniqueResult();
			Integer id=null;
			if(doctorIPConsultationServiceMapper!=null)
				id =doctorIPConsultationServiceMapper.getDoctorIPConsultationServiceMapperId();
			
			return id;
		}catch (HibernateException he) {
			logger.error("checkDoctorIPConsultationServiceMapperAvailable failed", he);
		} catch (Exception e) {
			logger.error("checkDoctorIPConsultationServiceMapperAvailable failed", e);
		}
		return null;
	}
	
	@Override
	public Response getinactivePreviousEntry(Integer id)
			throws ApplicationException {
		try {
			sessionFactory.getCurrentSession().createQuery("UPDATE DoctorIPConsultationServiceMapper doctorIPConsultationServiceMapper set doctorIPConsultationServiceMapper.status='I' "
					+ "WHERE doctorIPConsultationServiceMapper.doctorIPConsultationServiceMapperId=:doctorIPConsultationServiceMapperId ")
					.setParameter("doctorIPConsultationServiceMapperId", id).executeUpdate();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch (HibernateException he) {
			logger.error("getinactivePreviousEntry failed", he);
		} catch (Exception e) {
			logger.error("getinactivePreviousEntry failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDoctorIPConsultationByDoctorId(Integer doctorId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try{
			DoctorIPConsultationServiceMapperDto dto=(DoctorIPConsultationServiceMapperDto)sessionFactory.getCurrentSession()
				.getNamedQuery("GET_DOCTOR_IP_CONSULTATION_BY_DOCTOR_ID")
				.setParameter("docId", doctorId)
				.setParameter("unitId", unitId)
				.setParameter("orgId", organizationId)
				.setResultTransformer(Transformers.aliasToBean(DoctorIPConsultationServiceMapperDto.class)).uniqueResult();
		return new Response(SUCCESS, SUCCESS_CODE, null, null, dto);
	} catch (HibernateException he) {
		logger.error("getDoctorIPConsultationByDoctorId failed", he);
	} catch (Exception e) {
		logger.error("getDoctorIPConsultationByDoctorId failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorIPConsultationServiceMapperListCount(
			DoctorIPConsultationServiceMapperDto doctorIPConsultationServiceMapperDto) throws ApplicationException {
		try{
			BigInteger count = (BigInteger)sessionFactory.getCurrentSession()
				.getNamedQuery("GET_DOCTOR_IP_CONSULTATION_SERVICE_MAPPER_LIST_COUNT")
				.setParameter("unitId", doctorIPConsultationServiceMapperDto.getUnitId())
				.setParameter("organizationId", doctorIPConsultationServiceMapperDto.getOrganizationId()).uniqueResult();
		return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
	} catch (HibernateException he) {
		logger.error("getDoctorIPConsultationServiceMapperListCount failed", he);
	} catch (Exception e) {
		logger.error("getDoctorIPConsultationServiceMapperListCount failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
