package com.param.global.unit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dao.ICabinMasterDao;
import com.param.global.unit.dao.IDoctorInCabinDao;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.global.unit.dto.CoversheetAccessMasterDto;
import com.param.global.unit.dto.DoctorInCabinDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})

public class CabinMasterServiceImpl implements ICabinMasterService, ICommonConstants{
	final static Logger logger = Logger.getLogger(CabinMasterServiceImpl.class);


	@Autowired
	ICabinMasterDao iCabinMasterDao;
	
	@Autowired
	IDoctorInCabinDao iDoctorInCabinDao;
	
	@Override
	public Response saveCabinMaster(CabinMasterDto cabinMasterDto) throws ApplicationException {
		Response res=iCabinMasterDao.getCabinMasterByName(cabinMasterDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iCabinMasterDao.saveCabinMaster(cabinMasterDto);
			}

		} catch (Exception e) {
		logger.error("saveCabinMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateCabinMaster(CabinMasterDto cabinMasterDto) throws ApplicationException{
		Response res=iCabinMasterDao.getCabinMasterByNameNotId(cabinMasterDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iCabinMasterDao.updateCabinMaster(cabinMasterDto);
			}

		} catch (Exception e) {
		logger.error("updateCabinMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateCabinMasterStatus(CabinMasterDto cabinMasterDto) throws ApplicationException{
		try {
			
				return iCabinMasterDao.updateCabinMasterStatus(cabinMasterDto);
		
		} catch (Exception e) {
		logger.error("updateCabinMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCabinMasterById(Integer cabinId, Integer organizationId, Integer unitId) throws ApplicationException {
			try {
				
				return iCabinMasterDao.getCabinMasterById(cabinId, organizationId, unitId);
		
		} catch (Exception e) {
		logger.error("getCabinMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCabinMasterList(CabinMasterDto cabinMasterDto) throws ApplicationException{
			try {
				
				return iCabinMasterDao.getCabinMasterList(cabinMasterDto);
		
		} catch (Exception e) {
		logger.error("getCabinMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCabinMasterCount(CabinMasterDto cabinMasterDto) throws ApplicationException{
			try {
				
				return iCabinMasterDao.getCabinMasterCount(cabinMasterDto);
		
		} catch (Exception e) {
		logger.error("getCabinMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveCabinMaster(Integer organizationId, Integer unitId) throws ApplicationException{
			try {
				
				return iCabinMasterDao.getActiveCabinMaster(organizationId, unitId);
		
		} catch (Exception e) {
		logger.error("getActiveCabinMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCabinMasterByName(CabinMasterDto cabinMasterDto) throws ApplicationException{
			try {
				
				return iCabinMasterDao.getCabinMasterByName(cabinMasterDto);
		
		} catch (Exception e) {
		logger.error("getCabinMasterByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCabinMasterByNameNotId(CabinMasterDto cabinMasterDto) throws ApplicationException{
			try {
				
				return iCabinMasterDao.getCabinMasterByNameNotId(cabinMasterDto);
		
		} catch (Exception e) {
		logger.error("getCabinMasterByNameNotId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCoversheetAccessMaster(CoversheetAccessMasterDto coversheetAccessMasterDto)
			throws ApplicationException {
		try {
			
			return iCabinMasterDao.getCoversheetAccessMaster(coversheetAccessMasterDto);
	
	} catch (Exception e) {
	logger.error("getCoversheetAccessMaster failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response saveDoctorInCabin(DoctorInCabinDto dto) throws ApplicationException {

	try {
					iDoctorInCabinDao.inactivePreviousDoctorInCabin(dto);
			return iDoctorInCabinDao.saveDoctorInCabin(dto);
	
	} catch (Exception e) {
	logger.error("saveDoctorInCabin failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response inactivePreviousDoctorInCabin(DoctorInCabinDto dto) throws ApplicationException {
		try {
			return iDoctorInCabinDao.inactivePreviousDoctorInCabin(dto);

		} catch (Exception e) {
		logger.error("inactivePreviousDoctorInCabin failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorInCabin(DoctorInCabinDto dto) throws ApplicationException {
		try {
			return iDoctorInCabinDao.getDoctorInCabin(dto);

		} catch (Exception e) {
		logger.error("getDoctorInCabin failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
