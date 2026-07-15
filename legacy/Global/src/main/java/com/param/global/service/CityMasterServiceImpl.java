package com.param.global.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.global.dto.PrefixMasterDto;
import com.param.global.model.PrefixMaster;
import com.param.global.utility.UserComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IAreaMasterDao;
import com.param.global.dao.ICityMasterDao;
import com.param.global.dao.IReferalDetailsDao;
import com.param.global.dao.IReferralMasterDao;
import com.param.global.dto.AreaMasterDto;
import com.param.global.dto.CityMasterDto;
import com.param.global.dto.DistrictMasterDto;
import com.param.global.model.CityMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import in.param.exception.ApplicationException;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CityMasterServiceImpl implements ICityMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(CityMasterServiceImpl.class);


	@Autowired
	private ICityMasterDao iCityDao;
	
	@Autowired
	private IOtimsService iOtimsService;

	@Autowired
	IEmrService EmrService;   
	
	@Autowired
	IReferralMasterDao iReferralMasterDao;

	@Autowired
	private IAreaMasterService iAreaMasterService;

	private Cache<Integer, List<DistrictMasterDto>> districtListByStateCache;

	@PostConstruct
	public void initCache() {
		districtListByStateCache = CacheBuilder.newBuilder()
				.maximumSize(500)
				.build();
	}

	@Override
	public void invalidateDistrictListByStateCache(Integer stateId) {
		districtListByStateCache.invalidate(stateId);
	}

	@Override
	public Response addCityMaster(CityMasterDto cityMasterDto) throws ApplicationException {
		try {

			Response deptResponse = iCityDao.getCityByName(cityMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*cityMasterDto
						.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				cityMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res= iCityDao.addCityMaster(cityMasterDto);
				if(res.getObject() != null) {
					CityMaster dto = new CityMaster();
					dto = (CityMaster) res.getObject();
					cityMasterDto.setCityId(dto.getCityId());

					iAreaMasterService.invalidateCityListByDistrictMasterCache(dto.getDistrictId());

					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(cityMasterDto);
					request.setKey(MasterKeyConstants.City.toString());
					request.setPayload(payload);
					//EmrService.publish(request);
					//iOtimsService.publish(request);
				}
				
				
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
		logger.error("addCityMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCityMasterList(CityMasterDto cityMasterDto) throws ApplicationException {
		try {
			return iCityDao.getCityMasterList(cityMasterDto);
		} catch (Exception e) {
		logger.error("getCityMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateCityMaster(CityMasterDto citytMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iCityDao.getCityByNameNotId(citytMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*citytMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/

				Response updateRes = iCityDao.updateCityMaster(citytMasterDto);
				if(updateRes.getStatus().equals(SUCCESS) && updateRes.getObject() != null) {
					CityMaster master = (CityMaster) updateRes.getObject();
					iAreaMasterService.invalidateCityListByDistrictMasterCache(master.getDistrictId());
				}

				citytMasterDto.setCreatedBy(citytMasterDto.getUpdatedBy());
				citytMasterDto.setCreatedDate(citytMasterDto.getUpdatedDate());
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(citytMasterDto);
				request.setKey(MasterKeyConstants.City.toString());
				request.setPayload(payload);
				//EmrService.publish(request);
				//iOtimsService.publish(request);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
		logger.error("updateCityMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCityById(CityMasterDto cityMasterDto) throws ApplicationException {
		try {
			return iCityDao.getCityByID(cityMasterDto);
		} catch (Exception e) {
		logger.error("getCityById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForDistrict(CityMasterDto cityMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iCityDao.getCityByID(cityMasterDto);
			AreaMasterDto areaMasterDto = new AreaMasterDto();
			areaMasterDto.setCityId(cityMasterDto.getCityId());
			
			
			Response resp = iReferralMasterDao.getAreaByCityId(areaMasterDto);
			
			if(resp.getListObject() != null && resp.getListObject().size() > 0) {
				return new Response(ERROR, ALREADY_EXIST_CODE, "This city already is in use! ", null, null);
			}
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				/*cityMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				iCityDao.updateCityStatus(cityMasterDto);
				iAreaMasterService.invalidateCityListByDistrictMasterCache(cityMasterDto.getDistrictId());
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}
		} catch (Exception e) {
		logger.error("updateStatusForDistrict failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveCityList() throws ApplicationException {
		try {
			return iCityDao.getActiveCityList();
		} catch (Exception e) {
		logger.error("getActiveCityList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveDistrictListByStateId(DistrictMasterDto districtMasterDto) throws ApplicationException {
		try {
			List<DistrictMasterDto> responseCache = districtListByStateCache.getIfPresent(districtMasterDto.getStateId());
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iCityDao.getActiveDistrictListByStateId(districtMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				districtListByStateCache.put(districtMasterDto.getStateId(), res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;

		} catch (Exception e) {
		logger.error("getActiveDistrictListByStateId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCityCount(CityMasterDto cityMasterDto) throws ApplicationException {
		try {
			return iCityDao.getCount(cityMasterDto);
		} catch (Exception e) {
		logger.error("getCityCount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCityByStateIs(CityMasterDto cityMasterDto)throws ApplicationException {
		try {
			return iCityDao.getCityByStateId(cityMasterDto);
		} catch (Exception e) {
		logger.error("getCityByStateIs failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	

}
