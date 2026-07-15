package com.param.pharmacy.master.global.service.equipment;

import java.util.Date;

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
import com.param.global.dto.ServiceMasterDto;
import com.param.pharmacy.master.global.dao.equipment.IGenericScheduleMasterDao;
import com.param.pharmacy.master.global.dto.equipment.GenericScheduleMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GenericScheduleMasterServiceImpl implements IGenericScheduleMasterService, ICommonConstants, IError {

	private static final Logger logger = Logger.getLogger(GenericScheduleMasterServiceImpl.class);

	@Autowired
	private IGenericScheduleMasterDao iGenericScheduleMasterDao;

	@Autowired
	private SessionFactory sessionFactory;

	

	@Override
	@Transactional
	public Response listScheduleDesc(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			return iGenericScheduleMasterDao.listScheduleDesc(orgId, unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}



	@Override
	@Transactional
	public Response searchServicesByNameAndCode(ServiceMasterDto serviceMasterDto) throws ApplicationException {
		try {
			return iGenericScheduleMasterDao.searchServicesByNameAndCode(serviceMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}



	@Override
	@Transactional
	public Response saveGenericScheduleMaster(GenericScheduleMasterDto genericScheduleMasterDto)
			throws ApplicationException {
		try {
			return iGenericScheduleMasterDao.saveGenericScheduleMaster(genericScheduleMasterDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}



	@Override
	@Transactional
	public Response getGenericScheduleMasterById(Integer orgId, Integer unitId, Integer genericScheduleId)
		throws ApplicationException {
			try {
				return iGenericScheduleMasterDao.getGenericScheduleMasterById(orgId, unitId, genericScheduleId);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}
		}


	@Override
	@Transactional
	public Response updateGenericScheduleMaster(GenericScheduleMasterDto genericScheduleMasterDto) throws ApplicationException {
		try {
			/*GenericScheduleMasterDto GenericScheduleMstrDto = (GenericScheduleMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_GET_GENERIC_SCHEDULE_BY_CODE")
					.setParameter("orgId", genericScheduleMasterDto.getOrgId())
					.setParameter("unitId", genericScheduleMasterDto.getUnitId())
					.setParameter("genericScheduleId", genericScheduleMasterDto.getGenericScheduleId())
					.setResultTransformer(Transformers.aliasToBean(GenericScheduleMasterDto.class)).uniqueResult();
			if (GenericScheduleMstrDto == null) {
				//System.out.println(TimeZoneComponent.getDateByzone(unitId).getTime());
				genericScheduleMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));*/
				return iGenericScheduleMasterDao.updateGenericScheduleMaster(genericScheduleMasterDto);
			/*} else {
				return new Response(ERROR, ERR_500, "SCHEDULE_CODE_EXISIS", null, null);
			}*/

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}


	@Override
	@Transactional
	public Response activeInactiveGenericSchedule(Integer orgId, Integer genericScheduleId, Character status)
			throws ApplicationException {
		try {
			return iGenericScheduleMasterDao.activeInactiveGenericSchedule(orgId, genericScheduleId, status);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}



	@Override
	@Transactional
	public Response listGenericScheduleMaster(Integer orgId, Integer unitId,
			Integer offset, Integer recordPerPage) throws ApplicationException {
		try {
			return iGenericScheduleMasterDao.listGenericScheduleMaster(orgId, unitId, offset, recordPerPage);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	@Transactional
	public Response getTotalGenericScheduleMasterRecord(Integer orgId,
			Integer unitId) throws ApplicationException {
		try {
			return iGenericScheduleMasterDao.getTotalGenericScheduleMasterRecord(orgId, unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}



	@Override
	@Transactional
	public Response listserviceCode(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			return iGenericScheduleMasterDao.listserviceCode(orgId, unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}



	@Override
	@Transactional
	public Response getServicecodeList(Integer genericScheduleId, Integer orgId, Integer unitId)throws ApplicationException {
	try {
		return iGenericScheduleMasterDao.getServicecodeList(genericScheduleId,orgId, unitId);
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
}
}
