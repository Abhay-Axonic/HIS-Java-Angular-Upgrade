package com.param.global.procedure.service;

import javax.transaction.Transactional;


import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.procedure.dao.IAnesthesiaTypeMasterDao;
import com.param.global.procedure.dto.AnesthesiaTypeMasterDto;
import com.param.otims.model.AnesthesiaTypeMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class AnesthesiaTypeServiceImpl implements IAnesthesiaTypeService, ICommonConstants{

	private static final Logger logger = Logger.getLogger(AnesthesiaTypeServiceImpl.class);

	@Autowired
	private IAnesthesiaTypeMasterDao iAnesthesiaTypeMasterDao;
	
	@Autowired
	private IOtimsService iOtimsService;
	
	@Autowired
	IEmrService EmrService;
	
	@Override
	@Transactional
	public Response getAnesthesiaTypeList(int orgId, int unitId) throws ApplicationException {
		try{
			return iAnesthesiaTypeMasterDao.getAnesthesiaTypeList(orgId, unitId);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response saveAnesthesiaTypeMaster(AnesthesiaTypeMasterDto anesthesiaTypeMasterDto)
			throws ApplicationException {
		try {
				Response response=iAnesthesiaTypeMasterDao.getAnesthesiaTypeMasterByName(anesthesiaTypeMasterDto);
				
				if(response.getListObject().size()>0 && response.getListObject()!=null){
					
					return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
				}else{
					
					AnesthesiaTypeMaster anesthesiaTypeMaster = new AnesthesiaTypeMaster();
					AnesthesiaTypeMasterDto Dto = new AnesthesiaTypeMasterDto();
					Response res = iAnesthesiaTypeMasterDao.saveAnesthesiaTypeMaster(anesthesiaTypeMasterDto);
				
					anesthesiaTypeMaster = (AnesthesiaTypeMaster) res.getObject();
				
					Dto.setAnesthesiaTypeId(anesthesiaTypeMaster.getAnesthesiaTypeId());
					Dto.setAnesthesiaTypeName(anesthesiaTypeMaster.getAnesthesiaTypeName());
					Dto.setAnesthesiaTypeCode(anesthesiaTypeMaster.getAnesthesiaTypeCode());
					Dto.setAnesthesiaTypeDesc(anesthesiaTypeMaster.getAnesthesiaTypeDesc());
					Dto.setCreatedBy(anesthesiaTypeMasterDto.getCreatedBy());
					Dto.setCreatedDate(anesthesiaTypeMasterDto.getCreatedDate());
					Dto.setUpdatedBy(anesthesiaTypeMasterDto.getUpdatedBy());
					Dto.setUpdatedDate(anesthesiaTypeMasterDto.getUpdatedDate());
					Dto.setStatus(anesthesiaTypeMasterDto.getStatus());
					Dto.setOrgId(anesthesiaTypeMasterDto.getOrgId());
					Dto.setUnitId(anesthesiaTypeMasterDto.getUnitId());
					
					CommonResponse response1 = new CommonResponse();
					response1.setMessage("AnesthesiaType master Inserted Successfully");
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(Dto);
					request.setKey(MasterKeyConstants.AnesthesiaType.toString());
					request.setPayload(payload);
					//iOtimsService.publish(request);
					//EmrService.publish(request);
				
					return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				}
		} catch (Exception e) {
			logger.error("saveAnesthesiaTypeMaster failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateAnesthesiaTypeMaster(AnesthesiaTypeMasterDto anesthesiaTypeMasterDto)
			throws ApplicationException {
		try {
			Response response=iAnesthesiaTypeMasterDao.getAnesthesiaTypeMasterByNameNotId(anesthesiaTypeMasterDto);
			
			if(response.getListObject().size()>0 && response.getListObject()!=null){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				iAnesthesiaTypeMasterDao.updateAnesthesiaTypeMaster(anesthesiaTypeMasterDto);
				
				AnesthesiaTypeMasterDto Dto = new AnesthesiaTypeMasterDto();
				Dto.setAnesthesiaTypeId(anesthesiaTypeMasterDto.getAnesthesiaTypeId());
				Dto.setAnesthesiaTypeName(anesthesiaTypeMasterDto.getAnesthesiaTypeName());
				Dto.setAnesthesiaTypeCode(anesthesiaTypeMasterDto.getAnesthesiaTypeCode());
				Dto.setAnesthesiaTypeDesc(anesthesiaTypeMasterDto.getAnesthesiaTypeDesc());
				Dto.setUpdatedBy(anesthesiaTypeMasterDto.getUpdatedBy());
				Dto.setUpdatedDate(anesthesiaTypeMasterDto.getUpdatedDate());
				Dto.setStatus(anesthesiaTypeMasterDto.getStatus());
				Dto.setOrgId(anesthesiaTypeMasterDto.getOrgId());
				Dto.setUnitId(anesthesiaTypeMasterDto.getUnitId());
				
				CommonResponse response1 = new CommonResponse();
				response1.setMessage("AnesthesiaType master Inserted Successfully");
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(Dto);
				request.setKey(MasterKeyConstants.AnesthesiaType.toString());
				request.setPayload(payload);
				//iOtimsService.publish(request);
				//EmrService.publish(request);
				
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

			}
		} catch (Exception e) {
			logger.error("updateAnesthesiaTypeMaster failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateAnesthesiaTypeMasterStatus(AnesthesiaTypeMasterDto anesthesiaTypeMasterDto)
			throws ApplicationException {
		try {
			return iAnesthesiaTypeMasterDao.updateAnesthesiaTypeMasterStatus(anesthesiaTypeMasterDto);
		} catch (Exception e) {
			logger.error("updateAnesthesiaTypeMasterStatus failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getListAnesthesiaTypeMaster(AnesthesiaTypeMasterDto anesthesiaTypeMasterDto)
			throws ApplicationException {
		try {
			return iAnesthesiaTypeMasterDao.getListAnesthesiaTypeMaster(anesthesiaTypeMasterDto);
		} catch (Exception e) {
			logger.error("getListAnesthesiaTypeMaster failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getAnesthesiaTypeMasterById(Integer anesthesiaTypeId, Integer orgId, Integer unitId)
			throws ApplicationException {
		try {
			return iAnesthesiaTypeMasterDao.getAnesthesiaTypeMasterById(anesthesiaTypeId, orgId, unitId);
		} catch (Exception e) {
			logger.error("getAnesthesiaTypeMasterById failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getAnesthesiaTypeMasterCount(AnesthesiaTypeMasterDto anesthesiaTypeMasterDto)
			throws ApplicationException {
		try {
			return iAnesthesiaTypeMasterDao.getAnesthesiaTypeMasterCount(anesthesiaTypeMasterDto);
		} catch (Exception e) {
			logger.error("getAnesthesiaTypeMasterCount failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getAnesthesiaTypeMasterByName(AnesthesiaTypeMasterDto anesthesiaTypeMasterDto)
			throws ApplicationException {
		try {
			return iAnesthesiaTypeMasterDao.getAnesthesiaTypeMasterByName(anesthesiaTypeMasterDto);
		} catch (Exception e) {
			logger.error("getAnesthesiaTypeMasterByName failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getAnesthesiaTypeMasterByNameNotId(AnesthesiaTypeMasterDto anesthesiaTypeMasterDto)
			throws ApplicationException {
		try {
			return iAnesthesiaTypeMasterDao.getAnesthesiaTypeMasterByNameNotId(anesthesiaTypeMasterDto);
		} catch (Exception e) {
			logger.error("getAnesthesiaTypeMasterByNameNotId failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
