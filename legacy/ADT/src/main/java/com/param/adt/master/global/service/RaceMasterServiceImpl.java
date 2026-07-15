package com.param.adt.master.global.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.billing.global.model.BankMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dao.IRaceMasterDao;
import com.param.adt.master.global.dto.RaceMasterDto;
import com.param.adt.master.global.model.RaceMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RaceMasterServiceImpl implements IRaceMasterService, ICommonConstants {

	@Autowired
	private IRaceMasterDao iRaceMasterDao;
	
	@Autowired
	IEmrService EmrService;

	private Cache<Integer, List<RaceMasterDto>> raceMasterCache;
	private Cache<Integer, List<RaceMasterDto>> defaultRaceMasterCache;

	@PostConstruct
	public void initCache() {
		raceMasterCache = CacheBuilder.newBuilder()
				.maximumSize(100)
				.build();

		defaultRaceMasterCache = CacheBuilder.newBuilder()
				.maximumSize(100)
				.build();
	}

	@Override
	public Response addRaceMaster(RaceMasterDto raceMasterDto) throws ApplicationException {
		try {

			Response deptResponse = iRaceMasterDao.getRaceByName(raceMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*raceMasterDto
						.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				raceMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				if(raceMasterDto.getIsDefault()=='A'){
					iRaceMasterDao.updateDefault();
				}
				Response res = iRaceMasterDao.addRaceTypeMaster(raceMasterDto);
				
				if(res.getObject() != null) {
					RaceMaster dto = new RaceMaster();
					dto = (RaceMaster) res.getObject();
					raceMasterDto.setRaceId(dto.getRaceId());
					Integer orgId = dto.getOrganizationId();
					raceMasterCache.invalidate(orgId);
					defaultRaceMasterCache.invalidate(orgId);

					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(raceMasterDto);
					request.setKey(MasterKeyConstants.RaceMaster.toString());
					request.setPayload(payload);
					//EmrService.publish(request);
				}
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getRaceMasterList(RaceMasterDto raceMasterDto) throws ApplicationException {
		try {
			List<RaceMasterDto> responseCache = raceMasterCache.getIfPresent(raceMasterDto.getOrganizationId());
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}
			Response res = iRaceMasterDao.getRaceMasterList(raceMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				raceMasterCache.put(raceMasterDto.getOrganizationId(), res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateRaceMaster(RaceMasterDto raceMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iRaceMasterDao.getRaceByNameNotId(raceMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*raceMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				if(raceMasterDto.getIsDefault()=='A'){
					iRaceMasterDao.updateDefault();
				}

				Response updateRes = iRaceMasterDao.updateRaceMaster(raceMasterDto);
				if(updateRes.getStatus().equals(SUCCESS) && updateRes.getObject() != null) {
					RaceMaster master = (RaceMaster) updateRes.getObject();
					Integer orgId = master.getOrganizationId();
					raceMasterCache.invalidate(orgId);
					defaultRaceMasterCache.invalidate(orgId);
				}

				raceMasterDto.setCreatedBy(raceMasterDto.getUpdatedBy());
				raceMasterDto.setCreatedDate(raceMasterDto.getUpdatedDate());
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(raceMasterDto);
				request.setKey(MasterKeyConstants.RaceMaster.toString());
				request.setPayload(payload);
				//EmrService.publish(request);
				
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getRaceById(RaceMasterDto raceMasterDto) throws ApplicationException {
		try {
			return iRaceMasterDao.getRaceByID(raceMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForRace(RaceMasterDto raceMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iRaceMasterDao.getRaceByID(raceMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				/*raceMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res =iRaceMasterDao.updateRaceStatus(raceMasterDto);
				if(res.getStatus().equals(SUCCESS) && res.getObject() != null) {
					Integer filterId = (Integer) res.getObject();
					raceMasterCache.invalidate(raceMasterDto.getOrganizationId() != null ? raceMasterDto.getOrganizationId() : filterId);
					defaultRaceMasterCache.invalidate(raceMasterDto.getOrganizationId() != null ? raceMasterDto.getOrganizationId() : filterId);
				}
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveRaceList(Integer orgId) throws ApplicationException {
		try {
			return iRaceMasterDao.getActiveRaceList(orgId);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);

	}

	@Override
	public Response getRaceCount(RaceMasterDto raceMasterDto) throws ApplicationException {
		try {
			return iRaceMasterDao.getCount(raceMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDefaultRaceList(Integer organizationId) throws ApplicationException {
		try {
			List<RaceMasterDto> responseCache = defaultRaceMasterCache.getIfPresent(organizationId);
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iRaceMasterDao.getDefaultRaceList(organizationId);

			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				defaultRaceMasterCache.put(organizationId, res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

}
