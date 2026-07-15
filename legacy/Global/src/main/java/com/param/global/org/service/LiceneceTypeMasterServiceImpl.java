package com.param.global.org.service;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.dao.ILiceneceTypeMasterDao;
import com.param.global.org.dto.LiceneceTypeMasterDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class LiceneceTypeMasterServiceImpl implements ILiceneceTypeMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(LiceneceTypeMasterServiceImpl.class);


	@Autowired
	ILiceneceTypeMasterDao iLiceneceTypeMasterDao;

	@Override
	public Response saveLiceneceTypeMaster(LiceneceTypeMasterDto liceneceTypeMasterDto) throws ApplicationException {
		try {
			Response response = iLiceneceTypeMasterDao.getLiceneceTypeMasterByType(liceneceTypeMasterDto);
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				return iLiceneceTypeMasterDao.saveLiceneceTypeMaster(liceneceTypeMasterDto);
			}

		} catch (HibernateException he) {
		logger.error("saveLiceneceTypeMaster failed", he);
		} catch (Exception e) {
		logger.error("saveLiceneceTypeMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getLiceneceTypeMasterById(int licenceId, int orgId) throws ApplicationException {
		try {
			return iLiceneceTypeMasterDao.getLiceneceTypeMasterById(licenceId, orgId);
		} catch (HibernateException he) {
		logger.error("getLiceneceTypeMasterById failed", he);
		} catch (Exception e) {
		logger.error("getLiceneceTypeMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getLiceneceTypeMasterList(int orgId) throws ApplicationException {
		try {
			return iLiceneceTypeMasterDao.getLiceneceTypeMasterList(orgId);
		} catch (Exception e) {
		logger.error("getLiceneceTypeMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateLiceneceTypeMaster(LiceneceTypeMasterDto liceneceTypeMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iLiceneceTypeMasterDao.getLicenceByNameNotId(liceneceTypeMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}else{
				return iLiceneceTypeMasterDao.updateLiceneceTypeMaster(liceneceTypeMasterDto);
			}
			
		} catch (Exception e) {
		logger.error("updateLiceneceTypeMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateLiceneceTypeMasterStatus(LiceneceTypeMasterDto liceneceTypeMasterDto)
			throws ApplicationException {
		try {
			return iLiceneceTypeMasterDao.updateLiceneceTypeMasterStatus(liceneceTypeMasterDto);
		} catch (Exception e) {
		logger.error("updateLiceneceTypeMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

}
