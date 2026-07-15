package com.param.global.org.common.dao;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.org.common.dto.ShiftMasterDto;
import com.param.global.org.common.model.ShiftMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class ShiftMasterDaoImpl extends GenericDao<ShiftMaster, Integer> implements IShiftMasterDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(ShiftMasterDaoImpl.class);

	public ShiftMasterDaoImpl() {
		super(ShiftMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveShiftMaster(ShiftMasterDto shiftMasterDto) throws ApplicationException {
		try {
			ShiftMaster shiftMaster=new ShiftMaster();
			shiftMaster.setShiftName(shiftMasterDto.getShiftName());
			shiftMaster.setOrganizationId(shiftMasterDto.getOrganizationId());

			LocalTime fromTime1 = LocalTime.parse( shiftMasterDto.getFromTime());
			LocalDate today = LocalDate.now();
			Instant fromTime = fromTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

			LocalTime toTime1 = LocalTime.parse( shiftMasterDto.getToTime());
			Instant toTime = toTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();


			shiftMaster.setFromTime(fromTime);
			shiftMaster.setToTime(toTime);
			shiftMaster.setIsWeeklyOff(shiftMasterDto.getIsWeeklyOff());
			shiftMaster.setStatus(shiftMasterDto.getStatus());
			shiftMaster.setUnitId(shiftMasterDto.getUnitId());
			shiftMaster.setCreatedBy(shiftMasterDto.getCreatedBy());
			shiftMaster.setCreatedDate(GlobalCommonDateUtils.getDate(shiftMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			shiftMaster.setUpdatedBy(shiftMasterDto.getUpdatedBy());
			shiftMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(shiftMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 save(shiftMaster);
			 
			return new Response(SUCCESS, SUCCESS_CODE, "Shift Added Successfully", null, null);
		}catch (HibernateException he) {
			logger.error("saveShiftMaster failed", he);
		} catch (Exception e) {
			logger.error("saveShiftMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getByShiftName(ShiftMasterDto shiftMasterDto) throws ApplicationException {
		try {
			ShiftMasterDto dto=(ShiftMasterDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SHIFT_MASTER_LIST_BY_NAME")
					.setString("shiftName", shiftMasterDto.getShiftName())
					.setInteger("orgId", shiftMasterDto.getOrganizationId())
					.setInteger("unitId", shiftMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ShiftMasterDto.class)).uniqueResult();
			
			if(dto!=null) {
				return new Response<>(SUCCESS, null, null, null, null);
			}
			else {
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

		}catch (HibernateException he) {
			logger.error("getByShiftName failed", he);
		} catch (Exception e) {
			logger.error("getByShiftName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getShiftMasterList(ShiftMasterDto shiftMasterDto) throws ApplicationException {
		try {
			List<ShiftMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SHIFT_MASTER_LIST")
					.setInteger("orgId", shiftMasterDto.getOrganizationId())
					.setInteger("unitId", shiftMasterDto.getUnitId())
					.setFirstResult(shiftMasterDto.getOffset() != null ? shiftMasterDto.getOffset()  : 0)
					.setMaxResults(shiftMasterDto.getNoOfRecordsPerPage() != null ? shiftMasterDto.getNoOfRecordsPerPage()  : 10)
			.setResultTransformer(Transformers.aliasToBean(ShiftMasterDto.class)).list();
				return new Response<>(SUCCESS, null, null, list, null);

		}catch (HibernateException he) {
			logger.error("getShiftMasterList failed", he);
		} catch (Exception e) {
			logger.error("getShiftMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateShiftMaster(ShiftMasterDto shiftMasterDto) throws ApplicationException {
		try {
			LocalTime fromTime1 = LocalTime.parse(shiftMasterDto.getFromTime());
			LocalDate today = LocalDate.now();
			Instant fromTime = fromTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

			LocalTime toTime1 = LocalTime.parse(shiftMasterDto.getToTime());
			Instant toTime = toTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

			ShiftMaster shiftMaster=(ShiftMaster)sessionFactory.getCurrentSession().createQuery(" from ShiftMaster sm where sm.shiftId=:shiftId ")
					.setInteger("shiftId", shiftMasterDto.getShiftId()).uniqueResult();
			shiftMaster.setShiftName(shiftMasterDto.getShiftName());
			shiftMaster.setOrganizationId(shiftMasterDto.getOrganizationId());
			shiftMaster.setFromTime(fromTime);
			shiftMaster.setToTime(toTime);
			shiftMaster.setIsWeeklyOff(shiftMasterDto.getIsWeeklyOff());
			shiftMaster.setStatus(shiftMasterDto.getStatus());
			shiftMaster.setUnitId(shiftMasterDto.getUnitId());
			shiftMaster.setUpdatedBy(shiftMasterDto.getUpdatedBy());
			shiftMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(shiftMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 update(shiftMaster);
			 
			return new Response(SUCCESS, SUCCESS_CODE, "Shift Updated Successfully", null, null);
		}catch (HibernateException he) {
			logger.error("updateShiftMaster failed", he);
		} catch (Exception e) {
			logger.error("updateShiftMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateShiftMasterStatus(ShiftMasterDto shiftMasterDto) throws ApplicationException {
	try {
		ShiftMaster shiftMaster=findById(shiftMasterDto.getShiftId());
		shiftMaster.setShiftId(shiftMasterDto.getShiftId());
		shiftMaster.setStatus(shiftMasterDto.getStatus());
		shiftMaster.setUpdatedBy(shiftMasterDto.getUpdatedBy());
		shiftMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(shiftMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		 update(shiftMaster);
		 
		return new Response(SUCCESS, SUCCESS_CODE, "Shift Status Updated Successfully", null, null);
	}catch (HibernateException he) {
		logger.error("updateShiftMasterStatus failed", he);
	} catch (Exception e) {
		logger.error("updateShiftMasterStatus failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getShiftMasterCount(ShiftMasterDto shiftMasterDto) throws ApplicationException {
    try {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(ShiftMaster.class);
			crit.add(Restrictions.eq("organizationId", shiftMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", shiftMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);

		} catch (HibernateException he) {
			logger.error("getShiftMasterCount failed", he);
		} catch (Exception e) {
			logger.error("getShiftMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getShiftMasterByNameNotId(ShiftMasterDto shiftMasterDto) throws ApplicationException {
		try {
			List<ShiftMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SHIFT_MASTER_LIST_BY_NAME_NOT_BY_ID")
					.setString("shiftName", shiftMasterDto.getShiftName())
					.setInteger("shiftId", shiftMasterDto.getShiftId())
					.setInteger("orgId", shiftMasterDto.getOrganizationId())
					.setInteger("unitId", shiftMasterDto.getUnitId())

					.setResultTransformer(Transformers.aliasToBean(ShiftMasterDto.class)).list();
			 if(list.size()>0) {
				 return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			 }
			 else {
				 return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			 }


		} catch (HibernateException he) {
			logger.error("getShiftMasterByNameNotId failed", he);
		} catch (Exception e) {
			logger.error("getShiftMasterByNameNotId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getShiftMasterById(Integer shiftId, Integer organizationId, Integer unitId)
			throws ApplicationException {
	try {
		ShiftMasterDto dto=(ShiftMasterDto)sessionFactory.getCurrentSession()
				.getNamedQuery("GET_SHIFT_MASTER_LIST_BY_ID")
				.setInteger("shiftId", shiftId)
				.setInteger("orgId", organizationId)
				.setInteger("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(ShiftMasterDto.class)).uniqueResult();
		 return new Response(SUCCESS, SUCCESS_CODE, null, null, dto);
	} catch (HibernateException he) {
		logger.error("getShiftMasterById failed", he);
	} catch (Exception e) {
		logger.error("getShiftMasterById failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveShiftMasterList(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<ShiftMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_SHIFT_MASTER_LIST")
					.setInteger("orgId", organizationId)
					.setInteger("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(ShiftMasterDto.class)).list();
			 if(list.size()>0) {
				 return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			 }
			 else {
				 return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			 }


		} catch (HibernateException he) {
			logger.error("getActiveShiftMasterList failed", he);
		} catch (Exception e) {
			logger.error("getActiveShiftMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getShiftlistByTime(ShiftMasterDto shiftMasterDto) throws ApplicationException {
		try {
			List<ShiftMasterDto> shiftList =sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT shift_id as\"shiftId\" , shift_name as\"shiftName\" "
							+ " FROM public.m_shift_master"
							+ " where "
				            + "  ((from_time < to_time AND '"+shiftMasterDto.getFromTime()+"' BETWEEN from_time AND to_time)"
							+ " OR (to_time < from_time AND '"+shiftMasterDto.getFromTime()+"' < from_time AND '"+shiftMasterDto.getFromTime()+"' < to_time)"
							+ " OR (to_time < from_time AND '"+shiftMasterDto.getFromTime()+"' > from_time))"
//							+ " and status='A' and (is_weekly_off = 'N' or is_weekly_off is null)"
							+ " and organization_id="+shiftMasterDto.getOrganizationId()+" "
							+ " and unit_id ="+shiftMasterDto.getUnitId()+" "
									+ " ORDER BY shift_name ASC"
							)
				   
					.setResultTransformer(Transformers.aliasToBean(ShiftMasterDto.class)).list();
		
				return new Response<>(SUCCESS, null, null, shiftList, null);
			
			

			}catch (HibernateException he) {
				logger.error("getShiftlistByTime failed", he);
			} catch (Exception e) {
				logger.error("getShiftlistByTime failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
}
