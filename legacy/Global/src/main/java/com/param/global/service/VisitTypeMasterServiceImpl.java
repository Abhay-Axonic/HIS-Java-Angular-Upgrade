package com.param.global.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.global.dto.PatientCategoryMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IVisitTypeMasterDao;
import com.param.global.dto.VisitTypeMasterDto;

import in.param.exception.ApplicationException;

import javax.annotation.PostConstruct;
import java.util.List;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings("rawtypes")
public class VisitTypeMasterServiceImpl implements IVisitTypeMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(VisitTypeMasterServiceImpl.class);

	@Autowired
	private IVisitTypeMasterDao iVisitTypeMasterDao;

	private Cache<String, List<VisitTypeMasterDto>> vistiTypeMasterCache;

	@PostConstruct
	public void initCache() {
		vistiTypeMasterCache = CacheBuilder.newBuilder()
				.maximumSize(100)
				.build();
	}

	@Override
	public Response getActiveVisitTypeList(Integer unitId, Integer orgId) throws ApplicationException {
		try {
			List<VisitTypeMasterDto> responseCache = vistiTypeMasterCache.getIfPresent("visitTypeMaster");
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iVisitTypeMasterDao.getActiveVisitTypeList(unitId, orgId);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				vistiTypeMasterCache.put("visitTypeMaster", res.getListObject());
				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		} catch (Exception e) {
		logger.error("getActiveVisitTypeList failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVisitTypeList(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException {
		try {
			return iVisitTypeMasterDao.getVisitTypeList(visitTypeMasterDto);
		} catch (Exception e) {
		logger.error("getVisitTypeList failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVisitTypeCount(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException {
		try {
			return iVisitTypeMasterDao.getVisitTypeCount(visitTypeMasterDto);
		} catch (Exception e) {
		logger.error("getVisitTypeCount failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateVisitTypeStatus(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException {
		try {
			return iVisitTypeMasterDao.updateVisitTypeStatus(visitTypeMasterDto);
		} catch (Exception e) {
		logger.error("updateVisitTypeStatus failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateVisitType(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException {
		Response res = iVisitTypeMasterDao.getVisitTypeByNameNotById(visitTypeMasterDto);
		try {
			if (res.getListObject().size() > 0 && res.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				Response updateRes = iVisitTypeMasterDao.updateVisitType(visitTypeMasterDto);
				if (updateRes.getStatus().equals(SUCCESS)) {
					vistiTypeMasterCache.invalidateAll();
				}
				return updateRes;

			}
		} catch (Exception e) {
		logger.error("updateVisitType failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVisitTypeById(Integer visitId, Integer orgId) throws ApplicationException {
		try {
			return iVisitTypeMasterDao.getVisitTypeById(visitId, orgId);
		} catch (Exception e) {
		logger.error("getVisitTypeById failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveVisitType(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException {
		Response res = iVisitTypeMasterDao.getVisitTypeByName(visitTypeMasterDto);
		try {
			if (res.getListObject().size() > 0 && res.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {

				Response saveRes = iVisitTypeMasterDao.saveVisitType(visitTypeMasterDto);
				if(saveRes.getStatus().equals(SUCCESS)){
					vistiTypeMasterCache.invalidateAll();
				}
				return saveRes;
			}

		} catch (Exception e) {
		logger.error("saveVisitType failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
