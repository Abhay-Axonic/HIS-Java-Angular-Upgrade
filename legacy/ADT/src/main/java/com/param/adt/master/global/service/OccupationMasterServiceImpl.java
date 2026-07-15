package com.param.adt.master.global.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.billing.global.model.BankMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dao.IOccupationMasterDao;
import com.param.adt.master.global.dto.OccupationMasterDto;
import com.param.adt.master.global.model.OccupationMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import javax.annotation.PostConstruct;
import java.util.List;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
public class OccupationMasterServiceImpl implements IOccupationMasterService, ICommonConstants {

	@Autowired
	private IOccupationMasterDao iOccupationMasterDao;
	
	@Autowired
	IEmrService EmrService;

	private Cache<Integer, List<OccupationMasterDto>> occupationMasterCache;

	@PostConstruct
	public void initCache() {
		occupationMasterCache = CacheBuilder.newBuilder()
				.maximumSize(100)
				.build();
	}

	@Override
	public Response getOccupationMasterList(OccupationMasterDto occupationMasterDto) throws ApplicationException {
		try {
			return iOccupationMasterDao.getOccupationMasterList(occupationMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addOccupationMaster(OccupationMasterDto occupationMasterDto) throws ApplicationException {
		try {

			Response deptResponse = iOccupationMasterDao.getOccupationByName(occupationMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*occupationMasterDto
						.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				occupationMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res = iOccupationMasterDao.addOccupationMaster(occupationMasterDto);
				if(res.getObject() != null) {
					
					OccupationMaster dto = new OccupationMaster();
					dto = (OccupationMaster) res.getObject();
					occupationMasterDto.setId(dto.getId());

					Integer orgId = dto.getOrganizationId();
					occupationMasterCache.invalidate(orgId);

					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(occupationMasterDto);
					request.setKey(MasterKeyConstants.Occupation.toString());
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
	public Response getOccupationById(OccupationMasterDto occupationMasterDto) throws ApplicationException {
		try {
			return iOccupationMasterDao.getOccupationByID(occupationMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateOccupationMaster(OccupationMasterDto occupationMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iOccupationMasterDao.getOccupationByNameNotId(occupationMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*occupationMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response updateRes = iOccupationMasterDao.updateOccupationMaster(occupationMasterDto);

				if(updateRes.getStatus().equals(SUCCESS) && updateRes.getObject() != null) {
					OccupationMaster master = (OccupationMaster) updateRes.getObject();
					Integer orgId = master.getOrganizationId();
					occupationMasterCache.invalidate(orgId);
				}

				occupationMasterDto.setCreatedBy(occupationMasterDto.getUpdatedBy());
				occupationMasterDto.setCreatedDate(occupationMasterDto.getUpdatedDate());
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(occupationMasterDto);
				request.setKey(MasterKeyConstants.Occupation.toString());
				request.setPayload(payload);
				//EmrService.publish(request);
				
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForOccupation(OccupationMasterDto occupationMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iOccupationMasterDao.getOccupationByID(occupationMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
			/*	occupationMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res = iOccupationMasterDao.updateOccupationStatus(occupationMasterDto);
				if(res.getStatus().equals(SUCCESS) && res.getObject() != null) {
					Integer filterId = (Integer) res.getObject();
					occupationMasterCache.invalidate(occupationMasterDto.getOrganizationId() != null ? occupationMasterDto.getOrganizationId() : filterId);
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
	public Response getActiveMaritalStatusList(Integer orgId) throws ApplicationException {
		try {
			List<OccupationMasterDto> responseCache = occupationMasterCache.getIfPresent(orgId);
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iOccupationMasterDao.getActiveOccupationList(orgId);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				occupationMasterCache.put(orgId, res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getOccupationCount(OccupationMasterDto occupationMasterDto) throws ApplicationException {
		try {
			return iOccupationMasterDao.getCount(occupationMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

}
