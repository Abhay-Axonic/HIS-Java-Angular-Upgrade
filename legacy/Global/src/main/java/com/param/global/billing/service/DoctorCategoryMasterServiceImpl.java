package com.param.global.billing.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
import com.param.global.billing.dao.IDoctorCategoryMasterDao;
import com.param.global.billing.dto.DoctorCategoryMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.model.DoctorCategoryMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DoctorCategoryMasterServiceImpl implements IDoctorCategoryMasterService, ICommonConstants {

	final static Logger logger = Logger.getLogger(DoctorCategoryMasterServiceImpl.class);

	@Autowired
	IDoctorCategoryMasterDao iDoctorCategoryMasterDao;
	
	@Autowired
	private IOtimsService iOtimsService;
	
	@Autowired
	IEmrService EmrService;


	@Override
	public Response saveDoctorCategoryMaster(DoctorCategoryMasterDto doctorCategoryMasterDto)
			throws ApplicationException {
		Response response = iDoctorCategoryMasterDao.getDoctorCategoryMasterByName(doctorCategoryMasterDto);

		try {
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				Response res= iDoctorCategoryMasterDao.saveDoctorCategoryMaster(doctorCategoryMasterDto);
				
				if(res.getObject() !=null) {
					DoctorCategoryMaster dto = new DoctorCategoryMaster();
					dto= (DoctorCategoryMaster) res.getObject();
					doctorCategoryMasterDto.setDoctorCategoryId(dto.getDoctorCategoryId());
					
					CommonResponse respo = new CommonResponse();
					respo.setMessage("DoctorCategory Updated Successfully");
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(doctorCategoryMasterDto);
					request.setKey(MasterKeyConstants.DoctorCategory.toString());
					request.setPayload(payload);
					////iOtimsService.publish(request);
					//EmrService.publish(request);
					
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				}
			}

		} catch (Exception e) {
			logger.error("saveDoctorCategoryMaster failed", e);
			throw new ServiceException(e.getMessage());
		}

		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getDoctorCategoryMasterById(int doctorId, int orgId) throws ApplicationException {
		try {
			return iDoctorCategoryMasterDao.getDoctorCategoryMasterById(doctorId, orgId);

		} catch (Exception e) {
			logger.error("getDoctorCategoryMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDoctorCategoryMasterList(DoctorCategoryMasterDto doctorCategoryMasterDto) throws ApplicationException {
		try {
			return iDoctorCategoryMasterDao.getDoctorCategoryMasterList(doctorCategoryMasterDto);
		} catch (Exception e) {
			logger.error("getDoctorCategoryMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDoctorCategoryMaster(DoctorCategoryMasterDto doctorCategoryMasterDto)throws ApplicationException {
		Response response = iDoctorCategoryMasterDao.getDoctorCategoryMasterByNameNotId(doctorCategoryMasterDto);

		try {
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				Response res =  iDoctorCategoryMasterDao.updateDoctorCategoryMaster(doctorCategoryMasterDto);
				
				if(res.getObject() != null) {
					
					CommonResponse respo = new CommonResponse();
					respo.setMessage("DoctorCategory Updated Successfully");
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("update");
					payload.setData(doctorCategoryMasterDto);
					request.setKey(MasterKeyConstants.DoctorCategory.toString());
					request.setPayload(payload);
					////iOtimsService.publish(request);
					//EmrService.publish(request);
					return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
					
				}
				
			}

		} catch (Exception e) {
			logger.error("updateDoctorCategoryMaster failed", e);
			throw new ServiceException(e.getMessage());
		}

		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateDoctorCategoryMasterStatus(DoctorCategoryMasterDto doctorCategoryMasterDto)
			throws ApplicationException {
		try {
			return iDoctorCategoryMasterDao.updateDoctorCategoryMasterStatus(doctorCategoryMasterDto);
		} catch (Exception e) {
			logger.error("updateDoctorCategoryMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDoctorCategoryCount(DoctorCategoryMasterDto doctorCategoryMasterDto) throws ApplicationException {
		try{
			return iDoctorCategoryMasterDao.getDoctorCategoryCount(doctorCategoryMasterDto);
		}catch (Exception e) {
			logger.error("getDoctorCategoryCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveDoctorCategoryMasterList(int orgId) throws ApplicationException {
		try{
			return iDoctorCategoryMasterDao.getActiveDoctorCategoryMasterList(orgId);
		}catch (Exception e) {
			logger.error("getActiveDoctorCategoryMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
