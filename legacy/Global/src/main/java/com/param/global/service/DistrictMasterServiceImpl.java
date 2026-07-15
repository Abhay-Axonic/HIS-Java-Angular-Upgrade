package com.param.global.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.global.dto.PrefixMasterDto;
import com.param.global.model.PrefixMaster;
import com.param.global.utility.UserComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IAreaMasterDao;
import com.param.global.dao.ICityMasterDao;
import com.param.global.dao.IDistrictMasterDao;
import com.param.global.dto.CityMasterDto;
import com.param.global.dto.DistrictMasterDto;
import com.param.global.dto.StateMasterDto;
import com.param.global.model.DistrictMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import in.param.exception.ApplicationException;
import com.param.global.service.ICityMasterService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

@SuppressWarnings({"rawtypes","unchecked"})
@Repository
public class DistrictMasterServiceImpl implements IDistrictMasterService,ICommonConstants
{

	private static final Logger logger = Logger.getLogger(DistrictMasterServiceImpl.class);

	@Autowired
	private IDistrictMasterDao iDistrictMaster;

	@Autowired
	IEmrService EmrService;  
	 
	@Autowired
	IAreaMasterDao iAreaDao;

	@Autowired
	private ICityMasterService ICityMasterService;

	private Cache<Integer, List<StateMasterDto>> stateByCountryMasterCache;

	@PostConstruct
	public void initCache() {
		stateByCountryMasterCache = CacheBuilder.newBuilder()
				.maximumSize(500)
				.build();
	}
	
	@Override
	public Response addDistrictMaster(DistrictMasterDto districtMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iDistrictMaster.getDistrictByName(districtMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				
					return new Response(ERROR, null, ALREADY_EXIST, null, null);
				
			} else {
				/*districtMasterDto
						.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				districtMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res= iDistrictMaster.addDistrictMaster(districtMasterDto);
				
				if(res.getObject() != null) {
					DistrictMaster dto = new DistrictMaster();
					dto = (DistrictMaster) res.getObject();
					districtMasterDto.setDistrictId(dto.getDistrictId());

					ICityMasterService.invalidateDistrictListByStateCache(dto.getStateId());

					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(districtMasterDto);
					request.setKey(MasterKeyConstants.District.toString());
					request.setPayload(payload);
					//EmrService.publish(request);
				}
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
			logger.error("addDistrictMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDistrictMasterList(DistrictMasterDto districtMasterDto) throws ApplicationException {
		try {
			return iDistrictMaster.getDistrictMasterList(districtMasterDto);
		} catch (Exception e) {
			logger.error("getDistrictMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateDistrictMaster(DistrictMasterDto districtMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iDistrictMaster.getDistrictByNameNotId(districtMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*districtMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/

				Response updateRes = iDistrictMaster.updateDistrictMaster(districtMasterDto);

				if(updateRes.getStatus().equals(SUCCESS) && updateRes.getObject() != null) {
					DistrictMaster master = (DistrictMaster) updateRes.getObject();
					ICityMasterService.invalidateDistrictListByStateCache(master.getStateId());
				}

				districtMasterDto.setCreatedBy(districtMasterDto.getUpdatedBy());
				districtMasterDto.setCreatedDate(districtMasterDto.getUpdatedDate());

				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(districtMasterDto);
				request.setKey(MasterKeyConstants.District.toString());
				request.setPayload(payload);
				//EmrService.publish(request);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
			logger.error("updateDistrictMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDistrictById(DistrictMasterDto districtMasterDto) throws ApplicationException {
		try {
			return iDistrictMaster.getDistrictById(districtMasterDto);
		} catch (Exception e) {
			logger.error("getDistrictById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForDistrict(DistrictMasterDto districtMasterDto) throws ApplicationException {
		try {
			/*
			 * Response deptResponse = iDistrictMaster.getDistrictById(districtMasterDto);
			 * if (deptResponse.getListObject() != null &&
			 * deptResponse.getListObject().size() > 0) {
			 */
				
				/*districtMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
			CityMasterDto dist =new CityMasterDto();
			dist.setDistrictId(districtMasterDto.getDistrictId());
			Response resp = iAreaDao.getActiveCityListByDistrictId(dist);
			
			if(resp.getListObject() != null && resp.getListObject().size() > 0) {
				return new Response(ERROR, ALREADY_EXIST_CODE, "This district already is in use! ", null, null);
			}
			
				Response res = iDistrictMaster.updateDistrictStatus(districtMasterDto);
				if(res.getStatus().equals(SUCCESS) && res.getObject() != null) {
					Integer filterId = (Integer) res.getObject();
					ICityMasterService.invalidateDistrictListByStateCache(districtMasterDto.getStateId() != null ? districtMasterDto.getStateId() : filterId);
				}
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			/*} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}*/
		} catch (Exception e) {
			logger.error("updateStatusForDistrict failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveDistrictList(DistrictMasterDto districtMasterDto) throws ApplicationException {
		try {
			return iDistrictMaster.getActiveDistrictList(districtMasterDto);
		} catch (Exception e) {
			logger.error("getActiveDistrictList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveStateListByCountryId(StateMasterDto stateMasterDto) throws ApplicationException {
		try {

			List<StateMasterDto> responseCache = stateByCountryMasterCache.getIfPresent(stateMasterDto.getCountryId());
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iDistrictMaster.getActiveStateListByCountryId(stateMasterDto);

			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				stateByCountryMasterCache.put(stateMasterDto.getCountryId(), res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;

		} catch (Exception e) {
			logger.error("getActiveStateListByCountryId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDistrictCount(DistrictMasterDto districtMasterDto) throws ApplicationException {
		try {
			return iDistrictMaster.getCount(districtMasterDto);
		} catch (Exception e) {
			logger.error("getDistrictCount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDistrictMasterListByCountryId(DistrictMasterDto districtMasterDto) throws ApplicationException {
		try {
			return iDistrictMaster.getDistrictMasterListByCountryId(districtMasterDto);
		} catch (Exception e) {
			logger.error("getDistrictMasterListByCountryId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

}
