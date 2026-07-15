package com.param.global.unit.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.unit.dao.ICabinMasterDao;
import com.param.global.unit.dao.IOutsideHospitalMasterDao;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.global.unit.dto.OutsideHospitalDoctorMapperDto;
import com.param.global.unit.dto.OutsideHospitalMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes"})

public class OutsideHospitalMasterServiceImpl implements IOutsideHospitalMasterService ,  ICommonConstants{

	private static final Logger logger = Logger.getLogger(OutsideHospitalMasterServiceImpl.class);

	@Autowired
	IOutsideHospitalMasterDao iOutsideHospitalMasterDao;

	@Autowired
	IOutsideHospitalDoctorMapperDao iOutsideHospitalDoctorMapperDao;

	@Override
	public Response saveOutsideHospitalMaster(OutsideHospitalMasterDto outsideHospitalMasterDto)
			throws ApplicationException {

		Response res=iOutsideHospitalMasterDao.getOutsideHospitalMasterByName(outsideHospitalMasterDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){

				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iOutsideHospitalMasterDao.saveOutsideHospitalMaster(outsideHospitalMasterDto);
			}

		} catch (Exception e) {
			logger.error("saveOutsideHospitalMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateOutsideHospitalMaster(OutsideHospitalMasterDto outsideHospitalMasterDto)
			throws ApplicationException {

		Response res=iOutsideHospitalMasterDao.getOutsideHospitalMasterByNameNotId(outsideHospitalMasterDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){

				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iOutsideHospitalMasterDao.updateOutsideHospitalMaster(outsideHospitalMasterDto);
			}

		} catch (Exception e) {
			logger.error("updateOutsideHospitalMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateOutsideHospitalMasterStatus(OutsideHospitalMasterDto outsideHospitalMasterDto)
			throws ApplicationException {
		try {

			return iOutsideHospitalMasterDao.updateOutsideHospitalMasterStatus(outsideHospitalMasterDto);

	} catch (Exception e) {
		logger.error("updateOutsideHospitalMasterStatus failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getOutsideHospitalMasterById(Integer outsideHospitalId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {

			return iOutsideHospitalMasterDao.getOutsideHospitalMasterById(outsideHospitalId, organizationId, unitId);

	} catch (Exception e) {
		logger.error("getOutsideHospitalMasterById failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getOutsideHospitalMasterList(OutsideHospitalMasterDto outsideHospitalMasterDto)
			throws ApplicationException {
		try {

			return iOutsideHospitalMasterDao.getOutsideHospitalMasterList(outsideHospitalMasterDto);

	} catch (Exception e) {
		logger.error("getOutsideHospitalMasterList failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getOutsideHospitalMasterCount(OutsideHospitalMasterDto outsideHospitalMasterDto)
			throws ApplicationException {
		try {

			return iOutsideHospitalMasterDao.getOutsideHospitalMasterCount(outsideHospitalMasterDto);

	} catch (Exception e) {
		logger.error("getOutsideHospitalMasterCount failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getActiveOutsideHospitalMaster(Integer organizationId) throws ApplicationException {
		try {

			return iOutsideHospitalMasterDao.getActiveOutsideHospitalMaster(organizationId);

	} catch (Exception e) {
		logger.error("getActiveOutsideHospitalMaster failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getOutsideHospitalMasterByName(OutsideHospitalMasterDto outsideHospitalMasterDto) throws ApplicationException{
		try {

			return iOutsideHospitalMasterDao.getOutsideHospitalMasterByName(outsideHospitalMasterDto);

	} catch (Exception e) {
		logger.error("getOutsideHospitalMasterByName failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getOutsideHospitalMasterByNameNotId(OutsideHospitalMasterDto outsideHospitalMasterDto) throws ApplicationException{
			try {

				return iOutsideHospitalMasterDao.getOutsideHospitalMasterByNameNotId(outsideHospitalMasterDto);

		} catch (Exception e) {
			logger.error("getOutsideHospitalMasterByNameNotId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveOutsideHospitalDoctorMapper(OutsideHospitalDoctorMapperDto outsideHospitalDoctorMapperDto)
			throws ApplicationException {
		Response res=iOutsideHospitalDoctorMapperDao.getOutsideHospitalDoctorMapperByName(outsideHospitalDoctorMapperDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){

				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iOutsideHospitalDoctorMapperDao.saveOutsideHospitalDoctorMapper(outsideHospitalDoctorMapperDto);
			}

		} catch (Exception e) {
			logger.error("saveOutsideHospitalDoctorMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOutsideHospitalDoctorMapperList(OutsideHospitalDoctorMapperDto outsideHospitalDoctorMapperDto)
			throws ApplicationException {
		try {

			return iOutsideHospitalDoctorMapperDao.getOutsideHospitalDoctorMapperList(outsideHospitalDoctorMapperDto);

	} catch (Exception e) {
		logger.error("getOutsideHospitalDoctorMapperList failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response updateOutsideHospitalDoctorMapperStatus(
			OutsideHospitalDoctorMapperDto outsideHospitalDoctorMapperDto) throws ApplicationException {
try {

			return iOutsideHospitalDoctorMapperDao.updateOutsideHospitalDoctorMapperStatus(outsideHospitalDoctorMapperDto);

	} catch (Exception e) {
		logger.error("updateOutsideHospitalDoctorMapperStatus failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}


	@Override
	public Response getOutsideHospitalDoctorMapperById(OutsideHospitalDoctorMapperDto outsideHospitalDoctorMapperDto)
			throws ApplicationException {
		try {

			return iOutsideHospitalDoctorMapperDao.getOutsideHospitalDoctorMapperById(outsideHospitalDoctorMapperDto);

		} catch (Exception e) {
			logger.error("getOutsideHospitalDoctorMapperById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response searchOutsideHospitalDoctorList(OutsideHospitalDoctorMapperDto outsideHospitalDoctorMapperDto)
			throws ApplicationException {
		try {

			return iOutsideHospitalDoctorMapperDao.searchOutsideHospitalDoctorList(outsideHospitalDoctorMapperDto);

		} catch (Exception e) {
			logger.error("searchOutsideHospitalDoctorList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllOutsideHospitalDoctorList(OutsideHospitalDoctorMapperDto outsideHospitalDoctorMapperDto)
			throws ApplicationException {
		try {

			return iOutsideHospitalDoctorMapperDao.getAllOutsideHospitalDoctorList(outsideHospitalDoctorMapperDto);

		} catch (Exception e) {
			logger.error("getAllOutsideHospitalDoctorList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
