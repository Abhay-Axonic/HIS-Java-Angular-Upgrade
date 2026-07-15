package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.dto.PrefixMasterDto;
import com.param.global.model.PatientCategoryMaster;
import com.param.global.model.PrefixMaster;
import com.param.global.utility.UserComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.param.exception.ApplicationException;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IPatientCategoryMasterDao;
import com.param.global.dto.PatientCategoryMasterDto;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@Transactional
@SuppressWarnings({"rawtypes","unchecked"})
public class PatientCategoryMasterServiceImp implements IPatientCategoryMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(PatientCategoryMasterServiceImp.class);


	@Autowired
	IPatientCategoryMasterDao iPatientCategoryMasterDao;

	@Override
	public Response getPatientCategoryMaster(PatientCategoryMasterDto patientCategoryMasterDto)
			throws ApplicationException {
		try {
			return iPatientCategoryMasterDao.getPatientCategoryMaster(patientCategoryMasterDto);
		} catch (Exception e) {
		logger.error("getPatientCategoryMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);

	}

	@Override
	public Response savePatientCategoryMaster(PatientCategoryMasterDto patientCategoryMasterDto)
			throws ApplicationException {
		Response response = iPatientCategoryMasterDao.getPatientCategoryMasterByName(patientCategoryMasterDto);

		try {
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				return iPatientCategoryMasterDao.savePatientCategoryMaster(patientCategoryMasterDto);

			}
		} catch (Exception e) {
		logger.error("savePatientCategoryMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getPatientCategoryMasterById(int patientCategoryId, int orgId) throws ApplicationException {
		try {
			return iPatientCategoryMasterDao.getPatientCategoryMasterById(patientCategoryId, orgId);
		} catch (Exception e) {
		logger.error("getPatientCategoryMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updatePatientCategoryMaster(PatientCategoryMasterDto patientCategoryMasterDto)
			throws ApplicationException {
		Response response = iPatientCategoryMasterDao.getPatientCategoryMasterByNameNotId(patientCategoryMasterDto);

		try {
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				return iPatientCategoryMasterDao.updatePatientCategoryMaster(patientCategoryMasterDto);

			}
		} catch (Exception e) {
		logger.error("updatePatientCategoryMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updatePatientCategoryMasterStatus(PatientCategoryMasterDto patientCategoryMasterDto)
			throws ApplicationException {
		try {
			return iPatientCategoryMasterDao.updatePatientCategoryMasterStatus(patientCategoryMasterDto);
		} catch (Exception e) {
		logger.error("updatePatientCategoryMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getPatientCategoryMasterList(PatientCategoryMasterDto patientCategoryMasterDto) throws ApplicationException {
		try {
			return iPatientCategoryMasterDao.getPatientCategoryMasterList(patientCategoryMasterDto);
		} catch (Exception e) {
		logger.error("getPatientCategoryMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getPatientCategoryMasterCount(PatientCategoryMasterDto patientCategoryMasterDto)
			throws ApplicationException {
		try {
			return iPatientCategoryMasterDao.getPatientCategoryMasterCount(patientCategoryMasterDto);
		} catch (Exception e) {
		logger.error("getPatientCategoryMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getPatientCategoryByOrg(int orgId) throws ApplicationException {
		return iPatientCategoryMasterDao.getPatientCategoryByOrg(orgId);
	}

}
