package com.param.global.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.global.dto.PatientCategoryMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.master.global.model.ReferralMaster;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IReferralMasterDao;
import com.param.global.dto.AreaMasterDto;
import com.param.global.dto.ReferralMasterDto;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReferralMasterServiceImpl implements IReferralMasterService, ICommonConstants {

	final static Logger logger = Logger.getLogger(ReferralMasterServiceImpl.class);

	@Autowired
	private IReferralMasterDao iReferralMasterDao;
	
	@Autowired
	IEmrService EmrService;

	private Cache<Integer, List<AreaMasterDto>> areaByCityPaginationCache;
	private Cache<Integer, List<AreaMasterDto>> areaByCityCache;

	@PostConstruct
	public void initCache() {
		areaByCityPaginationCache = CacheBuilder.newBuilder()
				.maximumSize(200)
				.build();

		areaByCityCache = CacheBuilder.newBuilder()
				.maximumSize(500)
				.build();
	}


	public void invalidateAreaByCityPaginationCache(Integer cityId) {
		areaByCityPaginationCache.invalidate(cityId);
		areaByCityCache.invalidate(cityId);
	}

	/*@Override
	public Response addReferralMaster(ReferralMasterDto referralMasterDto) throws ApplicationException {
		try{
			return saveReferralMaster(referralMasterDto);	
		}catch (Exception e) {
			
		}
		
		return new Response(ERROR, null, null, null, null);
	}*/


	@Override
	public Response getReferralMasterList(ReferralMasterDto referralMasterDto) throws ApplicationException {
		try {
			return iReferralMasterDao.getReferralMasterList(referralMasterDto);
		} catch (Exception e) {
			logger.error("getReferralMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateReferralMaster(ReferralMasterDto referralMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iReferralMasterDao.getReferralByNameNotId(referralMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*referralMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				iReferralMasterDao.updateReferralMaster(referralMasterDto);
				
				referralMasterDto.setCreatedBy(referralMasterDto.getUpdatedBy());
				referralMasterDto.setCreatedDate(referralMasterDto.getUpdatedDate());
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(referralMasterDto);
				request.setKey(MasterKeyConstants.ReferralMaster.toString());
				request.setPayload(payload);
				//EmrService.publish(request);
				
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			}
		} catch (Exception e) {
			logger.error("updateReferralMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getReferralById(ReferralMasterDto referralMasterDto) throws ApplicationException {
		try {
			return iReferralMasterDao.getReferralById(referralMasterDto);
		} catch (Exception e) {
			logger.error("getReferralById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForReferral(ReferralMasterDto referralMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iReferralMasterDao.getReferralById(referralMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) 
			{
				/*referralMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				iReferralMasterDao.updateReferralMasterStatus(referralMasterDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			}
		} catch (Exception e) {
			logger.error("updateStatusForReferral failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveReferralList() throws ApplicationException {
		try {
			return iReferralMasterDao.getActiveReferralList();
		} catch (Exception e) {
			logger.error("getActiveReferralList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAreaByCityId(AreaMasterDto areaMasterDto) throws ApplicationException {
		try {
			List<AreaMasterDto> responseCache = areaByCityCache.getIfPresent(areaMasterDto.getCityId());
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iReferralMasterDao.getAreaByCityId(areaMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				areaByCityCache.put(areaMasterDto.getCityId(), res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		} catch (Exception e) {
			logger.error("getAreaByCityId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getReferralCount(ReferralMasterDto referralMasterDto) throws ApplicationException {
		try {
			return iReferralMasterDao.getCount(referralMasterDto);
		} catch (Exception e) {
			logger.error("getReferralCount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getReferralListByReferralTypeId(ReferralMasterDto referralMasterDto) throws ApplicationException {
		try {
			return iReferralMasterDao.getReferralListByReferralTypeId(referralMasterDto);
		} catch (Exception e) {
			logger.error("getReferralListByReferralTypeId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveReferralMaster(ReferralMasterDto referralMasterDto) throws ApplicationException {
		try {

			Response deptResponse = iReferralMasterDao.getReferralByName(referralMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, "1", "Referral you are trying to add is "+ALREADY_EXIST, null, deptResponse.getListObject().get(0));
			} else {
				Response res = iReferralMasterDao.addReferralTypeTypeMaster(referralMasterDto);
				
				if(res.getObject() != null) {
					ReferralMasterDto dto = new ReferralMasterDto();
					dto = (ReferralMasterDto) res.getObject();
					referralMasterDto.setReferralId(dto.getReferralId());
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(referralMasterDto);
					request.setKey(MasterKeyConstants.ReferralMaster.toString());
					request.setPayload(payload);
					//EmrService.publish(request);
				}
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, res.getObject());
			}
		} catch (Exception e) {
			logger.error("saveReferralMaster failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getReferralListByReferralTypeIds(ReferralMasterDto referralMasterDto) {
		try {
			return iReferralMasterDao.getReferralListByReferralTypeIds(referralMasterDto);
		} catch (Exception e) {
			logger.error("getReferralListByReferralTypeIds failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getReferralListForTypeSearch(ReferralMasterDto referralMasterDto) {
		try {
			return iReferralMasterDao.getReferralListForTypeSearch(referralMasterDto);
		} catch (Exception e) {
			logger.error("getReferralListForTypeSearch failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAreaByCityIdPagination(AreaMasterDto areaMasterDto) throws ApplicationException {
		try {
			List<AreaMasterDto> responseCache = areaByCityPaginationCache.getIfPresent(areaMasterDto.getCityId());
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iReferralMasterDao.getAreaByCityIdPagination(areaMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				areaByCityPaginationCache.put(areaMasterDto.getCityId(), res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		} catch (Exception e) {
			logger.error("getAreaByCityIdPagination failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getReferralListByReferralName(ReferralMasterDto referralMasterDto) throws ApplicationException {
		try {
			return iReferralMasterDao.getReferralListByReferralName(referralMasterDto);
		} catch (Exception e) {
			logger.error("getReferralListByReferralName failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getReferralListByOrgId(ReferralMasterDto referralMasterDto) throws ApplicationException {
		try {
			return iReferralMasterDao.getReferralListByOrgId(referralMasterDto);
		} catch (Exception e) {
			logger.error("getReferralListByOrgId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

}
