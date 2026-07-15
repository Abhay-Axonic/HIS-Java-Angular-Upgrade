package com.param.global.unit.dao;

import java.math.BigInteger;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.DoctorCabinMapper;
import com.param.global.unit.dto.DoctorCabinMapperDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class DoctorCabinMapperDaoImpl extends GenericDao<DoctorCabinMapper, Integer> implements IDoctorCabinMapperDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(DoctorCabinMapperDaoImpl.class);


	public DoctorCabinMapperDaoImpl() {
		super(DoctorCabinMapper.class);
	}
	
	@Autowired
	SessionFactory sessionFactory;
		
	@Override
	public Response saveDoctorCabinMapper(DoctorCabinMapperDto doctorCabinMapperDto) throws ApplicationException {
		try{
			DoctorCabinMapper doctorCabinMapperr=new DoctorCabinMapper();
			doctorCabinMapperr.setDoctorId(doctorCabinMapperDto.getDoctorId());
			doctorCabinMapperr.setCabinId(doctorCabinMapperDto.getCabinId());
			doctorCabinMapperr.setOrganisationId(doctorCabinMapperDto.getOrganisationId());
			doctorCabinMapperr.setUnitId(doctorCabinMapperDto.getUnitId());
			doctorCabinMapperr.setCreatedBy(doctorCabinMapperDto.getCreatedBy());
			doctorCabinMapperr.setStatus(doctorCabinMapperDto.getStatus());
			doctorCabinMapperr.setIsDeleted('N');
			doctorCabinMapperr.setCreatedDate(GlobalCommonDateUtils.getDate(doctorCabinMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			doctorCabinMapperr.setUpdatedBy(doctorCabinMapperDto.getUpdatedBy());
			doctorCabinMapperr.setUpdatedDate(GlobalCommonDateUtils.getDate(doctorCabinMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			
			save(doctorCabinMapperr);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("saveDoctorCabinMapper failed", he);
		} catch (Exception e) {
		logger.error("saveDoctorCabinMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateDoctorCabinMapper(List<DoctorCabinMapperDto> listDoctorCabinMapperDto) throws ApplicationException {
		try{
			
			
			Integer result = sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_DOCTOR_CABIN_MAPPER")
					.setParameter("isDeleted", 'Y')
					.setParameter("organisationId", listDoctorCabinMapperDto.get(0).getOrganisationId())
					.setParameter("unitId", listDoctorCabinMapperDto.get(0).getUnitId())
					.setParameter("doctorId", listDoctorCabinMapperDto.get(0).getDoctorId())
					.executeUpdate();
			if (result > 0) {
				for (Iterator iterator = listDoctorCabinMapperDto.iterator(); iterator.hasNext();) {
					DoctorCabinMapperDto listDoctorCabinMapper = (DoctorCabinMapperDto) iterator
							.next();
					DoctorCabinMapper doctorCabinMapperr=new DoctorCabinMapper();
					doctorCabinMapperr.setCabinId(listDoctorCabinMapper.getCabinId());
					doctorCabinMapperr.setDoctorId(listDoctorCabinMapper.getDoctorId());
					doctorCabinMapperr.setOrganisationId(listDoctorCabinMapper.getOrganisationId());
					doctorCabinMapperr.setUnitId(listDoctorCabinMapper.getUnitId());
					doctorCabinMapperr.setIsDeleted('N');
					doctorCabinMapperr.setCreatedBy(listDoctorCabinMapper.getCreatedBy());
					doctorCabinMapperr.setStatus(listDoctorCabinMapper.getStatus());
					doctorCabinMapperr.setUpdatedBy(listDoctorCabinMapper.getUpdatedBy());
					doctorCabinMapperr.setCreatedDate(GlobalCommonDateUtils.getDate(listDoctorCabinMapper.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
					doctorCabinMapperr.setUpdatedDate(GlobalCommonDateUtils.getDate(listDoctorCabinMapper.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

					
					save(doctorCabinMapperr);
				}
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} 
		} catch (HibernateException he) {
		logger.error("updateDoctorCabinMapper failed", he);
		} catch (Exception e) {
		logger.error("updateDoctorCabinMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDoctorCabinMapperStatus(Integer doctorId, Integer organisationId, Integer unitId,
			Character status) throws ApplicationException {
		try{
			if (doctorId != 0) {

				Integer result = sessionFactory.getCurrentSession()
						.getNamedQuery("UPDATE_DOCTOR_CABIN_MAPPER_STATUS")
						.setParameter("organisationId", organisationId)
						.setParameter("unitId", unitId)
						.setParameter("status", status)
						.setParameter("doctorId", doctorId)
						.executeUpdate();
				if (result > 0) {
					if (status.equals(ACTIVE)) {
						return new Response(SUCCESS,SUCCESS_CODE, COMMON_UPDATE, null, null);
					} else if (status.equals(INACTIVE)) {
						return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
					}
				} else {
					return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
				}

			} else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateDoctorCabinMapperStatus failed", he);
		} catch (Exception e) {
		logger.error("updateDoctorCabinMapperStatus failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getDoctorCabinMapperlist(DoctorCabinMapperDto doctorCabinMapperDto) throws ApplicationException {
		try{
			List<DoctorCabinMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTOR_CABIN_MAPPER_LIST")
					.setParameter("organisationId", doctorCabinMapperDto.getOrganisationId())
					.setParameter("unitId", doctorCabinMapperDto.getUnitId())
					.setFirstResult(doctorCabinMapperDto.getOffset() != null ? doctorCabinMapperDto.getOffset() : 0)
					.setMaxResults(doctorCabinMapperDto.getNoOfRecordsPerPage() != null ? doctorCabinMapperDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(DoctorCabinMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getDoctorCabinMapperlist failed", he);
		} catch (Exception e) {
		logger.error("getDoctorCabinMapperlist failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getDoctorCabinMapperCount(Integer organisationId, Integer unitId) throws ApplicationException {
		try{
			BigInteger doctorCabinMapperrCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTOR_CABIN_MAPPER_COUNT")
					.setParameter("organisationId", organisationId)
					.setParameter("unitId", unitId)
					.uniqueResult();
				if (doctorCabinMapperrCount.compareTo(BigInteger.ZERO) == 1) {
					return new Response(SUCCESS, SUCCESS_CODE, null, null, doctorCabinMapperrCount);
				} else {
					return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, BigInteger.ZERO);
				}
		} catch (HibernateException he) {
		logger.error("getDoctorCabinMapperCount failed", he);
		} catch (Exception e) {
		logger.error("getDoctorCabinMapperCount failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getActiveDoctorCabinMapper(Integer organisationId, Integer unitId) throws ApplicationException {
		try{
			
			List<DoctorCabinMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_DOCTOR_CABIN_MAPPER_LIST")
					.setParameter("organisationId", organisationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DoctorCabinMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getActiveDoctorCabinMapper failed", he);
		} catch (Exception e) {
		logger.error("getActiveDoctorCabinMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response getDoctorCabinMapperById(Integer doctorId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try{
			List<DoctorCabinMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTOR_CABIN_MAPPER_BY_ID")
					.setParameter("doctorId", doctorId)
					.setParameter("organisationId", organisationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DoctorCabinMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getDoctorCabinMapperById failed", he);
		} catch (Exception e) {
		logger.error("getDoctorCabinMapperById failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response checkDoctorCabinMapperAvailable(DoctorCabinMapperDto doctorCabinMapperDto)
			throws ApplicationException {
		try{
			List<DoctorCabinMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_DOCTOR_CABIN_MAPPER_AVAILABLE")
					.setParameter("doctorId", doctorCabinMapperDto.getDoctorId())
					.setParameter("organisationId", doctorCabinMapperDto.getOrganisationId())
					.setParameter("unitId", doctorCabinMapperDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DoctorCabinMapperDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("checkDoctorCabinMapperAvailable failed", he);
		} catch (Exception e) {
		logger.error("checkDoctorCabinMapperAvailable failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getDoctorList(Integer organisationId, Integer unitId) throws ApplicationException {
		try{
			List<DoctorCabinMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_DOCTOR")
					.setParameter("organisationId", organisationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DoctorCabinMapperDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getDoctorList failed", he);
		} catch (Exception e) {
		logger.error("getDoctorList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getDetailedCabinList(Integer doctorId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try{
			List<DoctorCabinMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DETAILED_CABIN_LIST")
					.setParameter("doctorId", doctorId)
					.setParameter("organisationId", organisationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DoctorCabinMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getDetailedCabinList failed", he);
		} catch (Exception e) {
		logger.error("getDetailedCabinList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getCabinByDocId(Integer doctorId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try{
			DoctorCabinMapperDto cabinMapperDto=(DoctorCabinMapperDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CABIN_BY_DOC_ID")
					.setParameter("doctorId", doctorId)
					.setParameter("organisationId", organisationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DoctorCabinMapperDto.class)).uniqueResult();
			
				return new Response(SUCCESS, SUCCESS_CODE, null, null, cabinMapperDto);
		} catch (HibernateException he) {
		logger.error("getCabinByDocId failed", he);
		} catch (Exception e) {
		logger.error("getCabinByDocId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
