package com.param.global.org.common.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.global.dto.PatientCategoryMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.master.global.model.ReasonMaster;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.common.dao.IReasonMasterDao;
import com.param.global.org.common.dto.ReasonMasterDto;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import javax.annotation.PostConstruct;
import java.util.List;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"unchecked","unused","rawtypes"})
public class ReasonMasterServiceImpl implements IReasonMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(ReasonMasterServiceImpl.class);


	@Autowired
	IReasonMasterDao iReasonMasterDao;
	
	@Autowired
	private IOtimsService iOtimsService;

	@Autowired
	IEmrService EmrService;

	private Cache<Integer, List<ReasonMasterDto>> reasonMasterCache;

	@PostConstruct
	public void initCache() {
		reasonMasterCache = CacheBuilder.newBuilder()
				.maximumSize(100)
				.build();
	}

	@Override
	public Response getReasonMasterById(int reasonId, int orgId) throws ApplicationException {
		try {
			return iReasonMasterDao.getReasonMasterById(reasonId, orgId);
		} catch (Exception e) {
		logger.error("getReasonMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response saveReasonMaster(ReasonMasterDto reasonMasterDto) throws ApplicationException {
		Response response = iReasonMasterDao.getReasonMasterByName(reasonMasterDto);
		try {
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				Response res =  iReasonMasterDao.saveReasonMaster(reasonMasterDto);
				
				if(res.getObject()!=null) {

					reasonMasterCache.invalidate(reasonMasterDto.getOrganizationId());

					ReasonMaster dto = new ReasonMaster();
					dto = (ReasonMaster) res.getObject();
					reasonMasterDto.setReasonMasterId(dto.getReasonMasterId());
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(reasonMasterDto);
					request.setKey(MasterKeyConstants.Cancellation.toString());
					request.setPayload(payload);
					////EmrService.publish(request);
					//iOtimsService.publish(request);
				}
				
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}

		} catch (Exception e) {
		logger.error("saveReasonMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getReasonMasterList(ReasonMasterDto reasonMasterDto) throws ApplicationException {
		try {
			return iReasonMasterDao.getReasonMasterList(reasonMasterDto);
		} catch (Exception e) {
		logger.error("getReasonMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response updateReasonMaster(ReasonMasterDto reasonMasterDto) throws ApplicationException {
		Response response = iReasonMasterDao.getReasonMasterByNameNotById(reasonMasterDto);
		try {
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				Response updateRes = iReasonMasterDao.updateReasonMaster(reasonMasterDto);

				if(updateRes.getObject()!=null) {
					ReasonMaster master = (ReasonMaster) updateRes.getObject();
					reasonMasterCache.invalidate(master.getOrganizationId());
				}

				reasonMasterDto.setCreatedBy(reasonMasterDto.getUpdatedBy());
				reasonMasterDto.setCreatedDate(reasonMasterDto.getUpdatedDate());
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(reasonMasterDto);
				request.setKey(MasterKeyConstants.Cancellation.toString());
				request.setPayload(payload);
				////EmrService.publish(request);
				//iOtimsService.publish(request);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
		logger.error("updateReasonMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateReasonMasterStatus(ReasonMasterDto reasonMasterDto) throws ApplicationException {
		try {
			Response res = iReasonMasterDao.updateReasonMasterStatus(reasonMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getObject() != null) {
				Integer organizationId = (Integer) res.getObject();
				reasonMasterCache.invalidate(reasonMasterDto.getOrganizationId() != null ? reasonMasterDto.getOrganizationId() : organizationId);
			}
			return res;
		} catch (Exception e) {
		logger.error("updateReasonMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getReasonMasterCount(ReasonMasterDto reasonMasterDto) throws ApplicationException {
		try {
			return iReasonMasterDao.getReasonMasterCount(reasonMasterDto);
		} catch (Exception e) {
		logger.error("getReasonMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveReasonMasterList(ReasonMasterDto reasonMasterDto) throws ApplicationException {
		try {
			List<ReasonMasterDto> responseCache = reasonMasterCache.getIfPresent(reasonMasterDto.getOrganizationId());
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iReasonMasterDao.getActiveReasonMasterList(reasonMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				reasonMasterCache.put(reasonMasterDto.getOrganizationId(), res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;

		} catch (Exception e) {
		logger.error("getActiveReasonMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getReasonMasterListByReasonTypeId(ReasonMasterDto reasonMasterDto) throws ApplicationException {
		try {
			return iReasonMasterDao.getReasonMasterListByReasonTypeId(reasonMasterDto);
		} catch (Exception e) {
		logger.error("getReasonMasterListByReasonTypeId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
