package com.param.global.otims.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dao.ISurgeryResourceConfigDao;
import com.param.global.otims.dto.SurgeryResourceConfigByCritertiaReqDto;
import com.param.global.otims.dto.SurgeryResourceConfigDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings("rawtypes")
public class SurgeryResourceConfigServiceImpl implements ISurgeryResourceConfigService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(SurgeryResourceConfigServiceImpl.class);

	@Autowired
	ISurgeryResourceConfigDao iSurgeryResourceConfigDao;

	@Override
	public Response saveSurgeryResourceConfig(SurgeryResourceConfigDto surgeryResourceConfigDto)
			throws ApplicationException {
		Response res=iSurgeryResourceConfigDao.checkAlreadyExistOrNot(surgeryResourceConfigDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){
				return new Response<>(ERROR,ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				Response  respdto = iSurgeryResourceConfigDao.saveSurgeryResourceConfig(surgeryResourceConfigDto);
				
				if(respdto != null && respdto.getObject() != null) {
					SurgeryResourceConfigDto newDto =(SurgeryResourceConfigDto)respdto.getObject();
					newDto.setListSelectedResourceBillOfServicesMapperDto(surgeryResourceConfigDto.getListSelectedResourceBillOfServicesMapperDto());
					return iSurgeryResourceConfigDao.mapperResorceSave(newDto);
				}
			}

		} catch (Exception e) {
			logger.error("saveSurgeryResourceConfig failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateSurgeryResourceConfig(SurgeryResourceConfigDto surgeryResourceConfigDto)
			throws ApplicationException {
		try {
			return iSurgeryResourceConfigDao.updateSurgeryResourceConfig(surgeryResourceConfigDto);
		} catch (Exception e) {
			logger.error("updateSurgeryResourceConfig failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSurgeryResourceConfigList(SurgeryResourceConfigDto surgeryResourceConfigDto)
			throws ApplicationException {
		try {
			return iSurgeryResourceConfigDao.getSurgeryResourceConfigList(surgeryResourceConfigDto);
		} catch (Exception e) {
			logger.error("getSurgeryResourceConfigList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSurgeryResourceConfigListById(Integer surgeryResourceConfigId) throws ApplicationException {
		try {
			return iSurgeryResourceConfigDao.getSurgeryResourceConfigListById(surgeryResourceConfigId);
		} catch (Exception e) {
			logger.error("getSurgeryResourceConfigListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSurgeryResourceConfigListCount(SurgeryResourceConfigDto surgeryResourceConfigDto)
			throws ApplicationException {
		try {
			return iSurgeryResourceConfigDao.getSurgeryResourceConfigListCount(surgeryResourceConfigDto);
		} catch (Exception e) {
			logger.error("getSurgeryResourceConfigListCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateSurgeryResourceConfigStatus(SurgeryResourceConfigDto surgeryResourceConfigDto)
			throws ApplicationException {
		try {
			return iSurgeryResourceConfigDao.updateSurgeryResourceConfigStatus(surgeryResourceConfigDto);
		} catch (Exception e) {
			logger.error("updateSurgeryResourceConfigStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkAlreadyExistOrNot(SurgeryResourceConfigDto surgeryResourceConfigDto)
			throws ApplicationException {
		try {
			return iSurgeryResourceConfigDao.checkAlreadyExistOrNot(surgeryResourceConfigDto);
		} catch (Exception e) {
			logger.error("checkAlreadyExistOrNot failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getConfigForProcPostingByCriteria(SurgeryResourceConfigByCritertiaReqDto reqDto)throws ApplicationException {
		try {
			return iSurgeryResourceConfigDao.getConfigForProcPostingByCriteria(reqDto);
		} catch (Exception e) {
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response getSurgeryResourceConfigListByProcedureId(SurgeryResourceConfigDto surgeryResourceConfigDto)
			throws ApplicationException {
		try {
			return iSurgeryResourceConfigDao.getSurgeryResourceConfigListByProcedureId(surgeryResourceConfigDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

}
