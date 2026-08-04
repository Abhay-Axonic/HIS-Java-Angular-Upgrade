package com.param.global.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.global.dto.PatientCategoryMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.service.VerifiedIdentityService;
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
	private VerifiedIdentityService verifiedIdentityService;

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
		// BR-161-class read fix (2026-08-04, live-proven): the list previously
		// trusted client-supplied org/unit from the URL path -- forged org-999
		// reads returned org-1 rows. Bind the verified tenant on every read.
		orgId = verifiedIdentityService.getVerifiedOrganizationId();
		unitId = verifiedIdentityService.getVerifiedUnitId();
		try {
			// BR-161-class fix (2026-08-04): the cache was keyed by the CONSTANT
			// "visitTypeMaster", so any org shared one entry -- an org-999 session
			// would have been served org-1's cached rows. Key by org+unit.
			String cacheKey = "visitTypeMaster:" + orgId + ":" + unitId;
			List<VisitTypeMasterDto> responseCache = vistiTypeMasterCache.getIfPresent(cacheKey);
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iVisitTypeMasterDao.getActiveVisitTypeList(unitId, orgId);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				vistiTypeMasterCache.put(cacheKey, res.getListObject());
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
		// BR-161-class read fix: GET_VISIT_TYPE_LIST had NO org filter at all
		// (forged-999 returned the same rows as org-1). Bind verified tenant.
		visitTypeMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
				try {
			return iVisitTypeMasterDao.getVisitTypeList(visitTypeMasterDto);
		} catch (Exception e) {
		logger.error("getVisitTypeList failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVisitTypeCount(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException {
		// BR-161-class read fix: count filtered by client-supplied org while the
		// list was unscoped (list-vs-count disagreed). Bind verified tenant.
		visitTypeMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
				try {
			return iVisitTypeMasterDao.getVisitTypeCount(visitTypeMasterDto);
		} catch (Exception e) {
		logger.error("getVisitTypeCount failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateVisitTypeStatus(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException {
		// BR-161-class fix: rebind verified tenant on the status-toggle path.
		visitTypeMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
				try {
			return iVisitTypeMasterDao.updateVisitTypeStatus(visitTypeMasterDto);
		} catch (Exception e) {
		logger.error("updateVisitTypeStatus failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateVisitType(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException {
		// BR-161-class fix: rebind verified tenant on the write path.
		visitTypeMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
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
		// BR-161-class read fix: orgId came from the URL path (client-supplied).
		orgId = verifiedIdentityService.getVerifiedOrganizationId();
		try {
			return iVisitTypeMasterDao.getVisitTypeById(visitId, orgId);
		} catch (Exception e) {
		logger.error("getVisitTypeById failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveVisitType(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException {
		// BR-161-class fix: writes also carried client-supplied org/unit in the
		// DTO body. Bind the verified tenant so save/dup-check/update are scoped
		// to the caller's identity (same pattern as batch-2 write fixes).
		visitTypeMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
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
