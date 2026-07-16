package com.param.global.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IAreaMasterDao;
import com.param.global.dto.AreaMasterDto;
import com.param.global.dto.CityMasterDto;
import com.param.global.model.AreaMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import javax.annotation.PostConstruct;
import java.util.List;


import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AreaMasterServiceImpl implements ICommonConstants, IAreaMasterService {
	final static Logger logger = Logger.getLogger(AreaMasterServiceImpl.class);

	@Autowired
	private IAreaMasterDao iAreaDao;
	
	@Autowired
	IEmrService EmrService;

	@Autowired
	private IReferralMasterService	iReferralMasterService;

	private Cache<Integer, List<CityMasterDto>> cityListByDistrictCache;

	@PostConstruct
	public void initCache() {
		cityListByDistrictCache = CacheBuilder.newBuilder()
				.maximumSize(500)
				.build();
	}

	@Override
	public void invalidateCityListByDistrictMasterCache(Integer districtId) {
		cityListByDistrictCache.invalidate(districtId);
	}

	@Override
	public Response addAreaMaster(AreaMasterDto areaMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iAreaDao.getAreaByName(areaMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, deptResponse.getListObject().getFirst());

			} else {
				/*areaMasterDto
						.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				areaMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res = iAreaDao.addAreaMaster(areaMasterDto);
				
				if(res.getObject() != null) {
					AreaMasterDto dto = new AreaMasterDto();
					dto = (AreaMasterDto) res.getObject();
					areaMasterDto.setAreaId(dto.getAreaId());

					iReferralMasterService.invalidateAreaByCityPaginationCache(areaMasterDto.getCityId());

					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(areaMasterDto);
					request.setKey(MasterKeyConstants.Area.toString());
					request.setPayload(payload);
					//EmrService.publish(request);
				}
				return res;
			}
		} catch (Exception e) {
		logger.error("addAreaMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAreaMasterList(AreaMasterDto areaMasterDto) throws ApplicationException {
		try {
			return iAreaDao.getAreaMasterList(areaMasterDto);
		} catch (Exception e) {
		logger.error("getAreaMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateAreaMaster(AreaMasterDto areaMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iAreaDao.getAreaByNameNotId(areaMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*areaMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/

				Response updateRes = iAreaDao.updateAreaMaster(areaMasterDto);

				if(updateRes.getStatus().equals(SUCCESS) && updateRes.getObject() != null) {
					AreaMaster master = (AreaMaster) updateRes.getObject();
					iReferralMasterService.invalidateAreaByCityPaginationCache(master.getCityId());
				}
				areaMasterDto.setCreatedBy(areaMasterDto.getUpdatedBy());
				areaMasterDto.setCreatedDate(areaMasterDto.getUpdatedDate());

				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(areaMasterDto);
				request.setKey(MasterKeyConstants.Area.toString());
				request.setPayload(payload);
				//EmrService.publish(request);
				
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
		logger.error("updateAreaMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAreaById(AreaMasterDto areaMasterDto) throws ApplicationException {
		try {
			return iAreaDao.getAreaById(areaMasterDto);
		} catch (Exception e) {
		logger.error("getAreaById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForArea(AreaMasterDto areaMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iAreaDao.getAreaById(areaMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				/*areaMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				iAreaDao.updateAreaStatus(areaMasterDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}
		} catch (Exception e) {
		logger.error("updateStatusForArea failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveAreaList() throws ApplicationException {
		try {
			return iAreaDao.getActiveAreaList();
		} catch (Exception e) {
		logger.error("getActiveAreaList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveCityListByDistrictId(CityMasterDto cityMasterDto) throws ApplicationException {
		try {
			List<CityMasterDto> responseCache = cityListByDistrictCache.getIfPresent(cityMasterDto.getDistrictId());
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iAreaDao.getActiveCityListByDistrictId(cityMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				cityListByDistrictCache.put(cityMasterDto.getDistrictId(), res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		} catch (Exception e) {
		logger.error("getActiveCityListByDistrictId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAreaCount(AreaMasterDto areaMasterDto) throws ApplicationException {
		try {
			return iAreaDao.getCount(areaMasterDto);
		} catch (Exception e) {
		logger.error("getAreaCount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDataByPincode(String code, Integer orgId) {
		try {
			return iAreaDao.getDataByPincode(code,orgId);
		} catch (Exception e) {
		logger.error("getDataByPincode failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

}
