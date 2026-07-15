package com.param.opd.coversheet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dao.INursingOrderMapperDao;
import com.param.opd.coversheet.dao.IPendingNursingOrderAdministrationDao;
import com.param.opd.coversheet.dto.NursingOrderMapperDto;
import com.param.opd.coversheet.dto.PendingNursingOrderAdministrationDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class NursingOrderMapperServiceImpl implements INursingOrderMapperService, ICommonConstants{
	final static Logger logger = Logger.getLogger(NursingOrderMapperServiceImpl.class);

	@Autowired
	INursingOrderMapperDao iNursingOrderMapperDao;
	
	@Autowired
	IPendingNursingOrderAdministrationDao iPendingNursingOrderAdministrationDao;

	@Override
	public Response saveNursingOrderMapper(NursingOrderMapperDto nursingOrderMapperDto) throws ApplicationException {
		try {
				return iNursingOrderMapperDao.saveNursingOrderMapper(nursingOrderMapperDto.getNursingOrderMapperDtoList());

		} catch (Exception e) {
		logger.error("saveNursingOrderMapper failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response updateNursingOrderMapper(NursingOrderMapperDto nursingOrderMapperDto) throws ApplicationException {
		try {
			return iNursingOrderMapperDao.updateNursingOrderMapper(nursingOrderMapperDto.getNursingOrderMapperDtoList());

	} catch (Exception e) {
	logger.error("updateNursingOrderMapper failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getNursingOrderMapperById(Integer nursingOrderMapperId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			return iNursingOrderMapperDao.getNursingOrderMapperById(nursingOrderMapperId, organizationId, unitId);

	} catch (Exception e) {
	logger.error("getNursingOrderMapperById failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getNursingOrderMapperList(NursingOrderMapperDto nursingOrderMapperDto) throws ApplicationException {
		try {
			return iNursingOrderMapperDao.getNursingOrderMapperList(nursingOrderMapperDto);

	} catch (Exception e) {
	logger.error("getNursingOrderMapperList failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
	@Override
	public Response getActiveNursingOrderMapperList(NursingOrderMapperDto nursingOrderMapperDto)
			throws ApplicationException {
		try {
			return iNursingOrderMapperDao.getActiveNursingOrderMapperList(nursingOrderMapperDto);

	} catch (Exception e) {
	logger.error("getActiveNursingOrderMapperList failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
	@Override
	public Response getListOfNursingOrders(NursingOrderMapperDto nursingOrderMapperDto) throws ApplicationException {
		try {
			return iNursingOrderMapperDao.getListOfNursingOrders(nursingOrderMapperDto);

	} catch (Exception e) {
	logger.error("getListOfNursingOrders failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
	@Override
	public Response updateNursingOrdersStatusByNurse(NursingOrderMapperDto nursingOrderMapperDto)
			throws ApplicationException {
			try {
				return iNursingOrderMapperDao.updateNursingOrdersStatusByNurse(nursingOrderMapperDto);

		} catch (Exception e) {
		logger.error("updateNursingOrdersStatusByNurse failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response updateNursingOrdersStartOrStop(NursingOrderMapperDto nursingOrderMapperDto)
		throws ApplicationException {
			try {
				return iNursingOrderMapperDao.updateNursingOrdersStartOrStop(nursingOrderMapperDto);

		} catch (Exception e) {
		logger.error("updateNursingOrdersStartOrStop failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getNursingOrderMapperListByAdmissionId(NursingOrderMapperDto nursingOrderMapperDto)
			throws ApplicationException {
		try {
			return iNursingOrderMapperDao.getNursingOrderMapperListByAdmissionId(nursingOrderMapperDto);

	} catch (Exception e) {
	logger.error("getNursingOrderMapperListByAdmissionId failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
	@Override
	public Response getNurseOrdersForPendingOrdersScreen(NursingOrderMapperDto nursingOrderMapperDto)
			throws ApplicationException {
		try {
			return iNursingOrderMapperDao.getNurseOrdersForPendingOrdersScreen(nursingOrderMapperDto);

	} catch (Exception e) {
	logger.error("getNurseOrdersForPendingOrdersScreen failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
	@Override
	public Response getNurseOrdersForPendingOrdersScreenByDate(NursingOrderMapperDto nursingOrderMapperDto)
			throws ApplicationException {
		try {
			return iNursingOrderMapperDao.getNurseOrdersForPendingOrdersScreenByDate(nursingOrderMapperDto);

	} catch (Exception e) {
	logger.error("getNurseOrdersForPendingOrdersScreenByDate failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
	@Override
	public Response getNurseOrdersDateWiseList(NursingOrderMapperDto nursingOrderMapperDto)
			throws ApplicationException {
		try {
			return iNursingOrderMapperDao.getNurseOrdersDateWiseList(nursingOrderMapperDto);

	} catch (Exception e) {
	logger.error("getNurseOrdersDateWiseList failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
	@Override
	public Response saveActualAdministrationTime(PendingNursingOrderAdministrationDto pendingNursingOrderAdministrationDto)
			throws ApplicationException {
		try {
			return iPendingNursingOrderAdministrationDao.saveActualAdministrationTime(pendingNursingOrderAdministrationDto);

	} catch (Exception e) {
	logger.error("saveActualAdministrationTime failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
	@Override
	public Response getPendingNurOrdListByPatientId(
			PendingNursingOrderAdministrationDto pendingNursingOrderAdministrationDto) throws ApplicationException {
		try {
			return iPendingNursingOrderAdministrationDao.getPendingNurOrdListByPatientId(pendingNursingOrderAdministrationDto);

	} catch (Exception e) {
	logger.error("getPendingNurOrdListByPatientId failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
	@Override
	public Response getNurseOrdersDateWiseListWebApp(NursingOrderMapperDto nursingOrderMapperDto)
			throws ApplicationException {
		try {
			return iNursingOrderMapperDao.getNurseOrdersDateWiseListWebApp(nursingOrderMapperDto);

	} catch (Exception e) {
	logger.error("getNurseOrdersDateWiseListWebApp failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
  }
	
}
