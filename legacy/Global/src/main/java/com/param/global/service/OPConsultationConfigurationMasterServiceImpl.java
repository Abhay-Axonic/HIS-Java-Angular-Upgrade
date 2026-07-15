package com.param.global.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IOPConsultationConfigurationMasterDao;
import com.param.global.dto.OPConsultationConfigurationMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class OPConsultationConfigurationMasterServiceImpl implements IOPConsultationConfigurationMasterService,ICommonConstants{

	private static final Logger logger = Logger.getLogger(OPConsultationConfigurationMasterServiceImpl.class);

	@Autowired
	IOPConsultationConfigurationMasterDao iopConsultationConfigurationMasterDao;

	@Override
	public Response OPConsultationConfiguration(
			OPConsultationConfigurationMasterDto opConsultationConfigurationMasterDto) throws ApplicationException {
		try {

			Response response = iopConsultationConfigurationMasterDao.checkOPConsultationExistOrNot(opConsultationConfigurationMasterDto);
			if (response.getListObject() != null && response.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				return iopConsultationConfigurationMasterDao.saveOPConsultationConfiguration(opConsultationConfigurationMasterDto);
			}
		} catch (Exception e) {
			logger.error("OPConsultationConfiguration failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getOPConsultationConfigurationList(
			OPConsultationConfigurationMasterDto opConsultationConfigurationMasterDto) throws ApplicationException {
		try {
				return iopConsultationConfigurationMasterDao.getOPConsultationConfigurationList(opConsultationConfigurationMasterDto);

		} catch (Exception e) {
			logger.error("getOPConsultationConfigurationList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getOPConsultationConfigurationListById(
			OPConsultationConfigurationMasterDto opConsultationConfigurationMasterDto) throws ApplicationException {
		try {
			return iopConsultationConfigurationMasterDao.getOPConsultationConfigurationListById(opConsultationConfigurationMasterDto);

	} catch (Exception e) {
		logger.error("getOPConsultationConfigurationListById failed", e);
	}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateOPConsultationConfigurationMaster(
			OPConsultationConfigurationMasterDto opConsultationConfigurationMasterDto) throws ApplicationException {
		try {
			return iopConsultationConfigurationMasterDao
					.updateOPConsultationConfigurationMaster(opConsultationConfigurationMasterDto);

		} catch (Exception e) {
			logger.error("updateOPConsultationConfigurationMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getOPConsultationConfigurationBySpecialityId(
			OPConsultationConfigurationMasterDto opConsultationConfigurationMasterDto) {
		try {
			return iopConsultationConfigurationMasterDao
					.getOPConsultationConfigurationBySpecialityId(opConsultationConfigurationMasterDto);

		} catch (Exception e) {
			logger.error("getOPConsultationConfigurationBySpecialityId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateOPConsultationConfigurationStatus(
			OPConsultationConfigurationMasterDto opConsultationConfigurationMasterDto) throws ApplicationException {
		try {
			return iopConsultationConfigurationMasterDao
					.updateOPConsultationConfigurationStatus(opConsultationConfigurationMasterDto);

		} catch (Exception e) {
			logger.error("updateOPConsultationConfigurationStatus failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

}
