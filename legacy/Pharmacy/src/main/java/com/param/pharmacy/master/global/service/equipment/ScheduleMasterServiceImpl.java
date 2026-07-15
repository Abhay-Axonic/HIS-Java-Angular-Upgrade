package com.param.pharmacy.master.global.service.equipment;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.pharmacy.master.global.dao.equipment.IScheduleMasterDao;
import com.param.pharmacy.master.global.dto.equipment.ScheduleMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ScheduleMasterServiceImpl implements IScheduleMasterService, ICommonConstants, IError {
	@Autowired
	private IScheduleMasterDao iScheduleMasterDao;

	@Autowired
	private SessionFactory sessionFactory;

	private static final Logger logger = Logger.getLogger(ScheduleMasterServiceImpl.class);

	@Override
	@Transactional
	public Response saveScheduleMaster(ScheduleMasterDto scheduleMasterDto) throws ApplicationException {
		try {
			ScheduleMasterDto departmentMat = (ScheduleMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SCHEDULE_BY_CODE")
					.setParameter("code", scheduleMasterDto.getScheduleCode().trim().toLowerCase())
					.setParameter("orgId", scheduleMasterDto.getOrgId())
					.setParameter("unitId", scheduleMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ScheduleMasterDto.class)).uniqueResult();
			if (departmentMat == null) {
				scheduleMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(scheduleMasterDto.getUnitId()));
				Response response = iScheduleMasterDao.saveScheduleMaster(scheduleMasterDto);
				return response;
			} else {
				return new Response(ERROR, ERR_500, "SCHEDULE_CODE_EXISIS", null, null);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getScheduleMasterById(Integer orgId, Integer unitId, Integer scheduleId)
			throws ApplicationException {
		try {
			return iScheduleMasterDao.getScheduleMasterById(orgId, unitId, scheduleId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response updateScheduleMaster(ScheduleMasterDto scheduleMasterDto) throws ApplicationException {

		try {
			ScheduleMasterDto SeroTestMasterDto = (ScheduleMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_GET_SCHEDULE_BY_CODE")
					.setParameter("code", scheduleMasterDto.getScheduleCode().trim().toLowerCase())
					.setParameter("orgId", scheduleMasterDto.getOrgId())
//					.setParameter("unitId", scheduleMasterDto.getUnitId())
					.setParameter("scheduleId", scheduleMasterDto.getScheduleId())
					.setResultTransformer(Transformers.aliasToBean(ScheduleMasterDto.class)).uniqueResult();
			if (SeroTestMasterDto == null) {
				//System.out.println(TimeZoneComponent.getDateByzone(unitId).getTime());
				scheduleMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(scheduleMasterDto.getUnitId()));
				return iScheduleMasterDao.updateScheduleMaster(scheduleMasterDto);
			} else {
				return new Response(ERROR, ERR_500, "SCHEDULE_CODE_EXISIS", null, null);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	@Transactional
	public Response ActivateInactivateScheduleMaster(Integer orgId, Integer scheduleId, Character scheduleStatus)
			throws ApplicationException {
		try {
			return iScheduleMasterDao.ActivateInactivateScheduleMaster(orgId, scheduleId, scheduleStatus);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response listScheduleMaster(Integer orgId, Integer unitId, Integer offset, Integer recordPerPage)
			throws ApplicationException {
		try {
			return iScheduleMasterDao.listScheduleMaster(orgId, unitId, offset, recordPerPage);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getToTalScheduleMasterRecord(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			return iScheduleMasterDao.getToTalScheduleMasterRecord(orgId, unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}
