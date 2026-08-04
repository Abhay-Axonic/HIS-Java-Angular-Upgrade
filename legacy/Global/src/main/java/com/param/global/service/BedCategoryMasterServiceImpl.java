package com.param.global.service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.global.dto.PatientCategoryMasterDto;
import com.param.global.model.BedCategoryMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IBedCategoryMasterDao;
import com.param.global.dto.BedCategoryMasterDto;
import com.param.global.service.VerifiedIdentityService;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class BedCategoryMasterServiceImpl implements IBedCategoryMasterService,ICommonConstants 
{
	final static Logger logger = Logger.getLogger(BedCategoryMasterServiceImpl.class);


	@Autowired
	private IBedCategoryMasterDao iBedCategoryDao;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	private Cache<Integer, List<BedCategoryMasterDto>> bedCategoryMasterCache;
	private Cache<String, List<BedCategoryMasterDto>> bedCategoryCache;

	@PostConstruct
	public void initCache() {
		bedCategoryMasterCache = CacheBuilder.newBuilder()
				.maximumSize(500)
				.build();

		bedCategoryCache = CacheBuilder.newBuilder()
				.maximumSize(500)
				.build();
	}
	
	@Override
	public Response addBedCategoryMaster(BedCategoryMasterDto bedCategoryMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant (client org/unit were trusted)
		bedCategoryMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		bedCategoryMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			Response deptResponse = iBedCategoryDao.getBedCategoryByName(bedCategoryMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {

				
				bedCategoryMasterDto.setxHierarchyId(bedCategoryMasterDto.getHierarchyId());
				bedCategoryMasterDto.setxOccupancyUnitId(bedCategoryMasterDto.getOccupancyUnitId());
				Response res = iBedCategoryDao.addBedCategoryTypeMaster(bedCategoryMasterDto);

				if(res.getStatus().equals(SUCCESS)) {
					bedCategoryMasterCache.invalidate(bedCategoryMasterDto.getUnitId());
					bedCategoryCache.invalidate("bedCategory");
				}

				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
		logger.error("addBedCategoryMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getBedCategoryMasterList(BedCategoryMasterDto bedCategoryMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant (client org/unit were trusted)
		bedCategoryMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		bedCategoryMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			return iBedCategoryDao.getBedCategoryMasterList(bedCategoryMasterDto);
		} catch (Exception e) {
		logger.error("getBedCategoryMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateBedCategoryMaster(BedCategoryMasterDto bedCategoryMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant (client org/unit were trusted)
		bedCategoryMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		bedCategoryMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			Response deptResponse = iBedCategoryDao.getBedCategoryByNameNotId(bedCategoryMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {

				/*bedCategoryMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response updateRes = iBedCategoryDao.updateBedCategoryMaster(bedCategoryMasterDto);
				if(updateRes.getStatus().equals(SUCCESS) && updateRes.getObject() != null) {
					BedCategoryMaster master = (BedCategoryMaster) updateRes.getObject();
					bedCategoryMasterCache.invalidate(master.getUnitId());
					bedCategoryCache.invalidate("bedCategory");
				}

				// BATCH-5 fix (silent-failure class): this method previously returned unconditional
				// SUCCESS, masking the DAO's rejection (e.g. cross-tenant update blocked) as success.
				// Propagate the DAO outcome so a blocked write is reported as an error.
				return updateRes;
			}
		} catch (Exception e) {
		logger.error("updateBedCategoryMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getBedCategoryById(BedCategoryMasterDto bedCategoryMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant (client org/unit were trusted)
		bedCategoryMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		bedCategoryMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			return iBedCategoryDao.getBedCategoryById(bedCategoryMasterDto);
		} catch (Exception e) {
		logger.error("getBedCategoryById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForBedCategory(BedCategoryMasterDto bedCategoryMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant (client org/unit were trusted)
		bedCategoryMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		bedCategoryMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			Response deptResponse = iBedCategoryDao.getBedCategoryById(bedCategoryMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				/*.*/

				Response res = iBedCategoryDao.updateBedCategoryStatus(bedCategoryMasterDto);
				if(res.getStatus().equals(SUCCESS) && res.getObject() != null) {
					Integer filterId = (Integer) res.getObject();
					bedCategoryMasterCache.invalidate(bedCategoryMasterDto.getUnitId() != null ? bedCategoryMasterDto.getUnitId() : filterId);
					bedCategoryCache.invalidate("bedCategory");
				}

				// BATCH-5 fix (silent-failure class): was unconditional SUCCESS, masking DAO rejection.
				return res;

			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}
		} catch (Exception e) {
		logger.error("updateStatusForBedCategory failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveBedCategoryList() throws ApplicationException {
		try {
			return iBedCategoryDao.getActiveBedCategoryList();
		} catch (Exception e) {
		logger.error("getActiveBedCategoryList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getBedCategoryCount(BedCategoryMasterDto bedCategoryMasterDto) throws ApplicationException {
		// BATCH-5 org-trust fix: bind verified tenant (client org/unit were trusted)
		bedCategoryMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		bedCategoryMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			return iBedCategoryDao.getCount(bedCategoryMasterDto);
		} catch (Exception e) {
		logger.error("getBedCategoryCount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAvailableBedCategoryAccordingToVacateBeds(BedCategoryMasterDto bedCategoryMasterDto) {
		// BATCH-5 org-trust fix: bind verified tenant (client org/unit were trusted)
		bedCategoryMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		bedCategoryMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
			List<BedCategoryMasterDto> responseCache = bedCategoryMasterCache.getIfPresent(bedCategoryMasterDto.getUnitId());
			List<BedCategoryMasterDto> categoryCache = bedCategoryCache.getIfPresent("bedCategory");
			if ((responseCache != null && !responseCache.isEmpty()) && (categoryCache != null && !categoryCache.isEmpty())) {
				bedCategoryMasterDto.setDayCareIpBedCategoryList(categoryCache);
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, bedCategoryMasterDto);
			}
			bedCategoryCache.invalidate("bedCategory");
			Response res = iBedCategoryDao.getAvailableBedCategoryAccordingToVacateBeds(bedCategoryMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				bedCategoryMasterCache.put(bedCategoryMasterDto.getUnitId(), res.getListObject());
				BedCategoryMasterDto dto = (BedCategoryMasterDto) res.getObject();
				bedCategoryCache.put("bedCategory", dto.getDayCareIpBedCategoryList());

				return new Response(SUCCESS_CODE, SUCCESS, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), dto);
			}
			return res;

		} catch (Exception e) {
		logger.error("getAvailableBedCategoryAccordingToVacateBeds failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	@Transactional
	public Response updateBedCategoryMasterHierarchy(BedCategoryMasterDto bedCategoryMasterDto) {
		// BATCH-5 org-trust fix: bind verified tenant (client org/unit were trusted)
		bedCategoryMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		bedCategoryMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		try {
				for (BedCategoryMasterDto bedCategoryMasterDto1 : bedCategoryMasterDto.getDayCareIpBedCategoryList()) {
					// BATCH-5 org-trust fix: child DTOs carry client org — bind verified tenant on each
					bedCategoryMasterDto1.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
					bedCategoryMasterDto1.setUnitId(verifiedIdentityService.getVerifiedUnitId());
					Response hierarchyRes = iBedCategoryDao.updateBedCategoryMasterHierarchy(bedCategoryMasterDto1);
					if (hierarchyRes == null || !hierarchyRes.getStatus().equals(SUCCESS)) {
						return hierarchyRes != null ? hierarchyRes : new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
					}
				}
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

}
