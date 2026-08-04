package com.param.adt.master.global.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.billing.global.model.BankMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dao.IMaritalStatusMasterDao;
import com.param.adt.master.global.dto.MaritalStatusDto;
import com.param.adt.master.global.model.MaritalStatusMaster;
import com.param.global.model.IdentificationMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import com.param.global.service.VerifiedIdentityService;

import in.param.exception.ApplicationException;

import javax.annotation.PostConstruct;
import java.util.List;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
public class MaritalStatusMasterServiceImpl implements IMaritalStatusMasterService, ICommonConstants {

	@Autowired
	private IMaritalStatusMasterDao iMaritalStatusMasterDao;
	
	@Autowired
	IEmrService EmrService;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	private Cache<Integer, List<MaritalStatusDto>> maritalMasterCache;

	@PostConstruct
	public void initCache() {
		maritalMasterCache = CacheBuilder.newBuilder()
				.maximumSize(100)
				.build();
	}

	@Override
	public Response addMaritalStatusMaster(MaritalStatusDto maritalStatusDto) throws ApplicationException {
		try {

			maritalStatusDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			Response deptResponse = iMaritalStatusMasterDao.getMaritalStatusByName(maritalStatusDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*maritalStatusDto
						.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				maritalStatusDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res = iMaritalStatusMasterDao.addMaritalStatusMaster(maritalStatusDto);
				
				if(res.getObject() != null) {
					MaritalStatusMaster dto = new MaritalStatusMaster();
					dto = (MaritalStatusMaster) res.getObject();
					maritalStatusDto.setId(dto.getId());

					Integer orgId = dto.getOrganizationId();
					maritalMasterCache.invalidate(orgId);

					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(maritalStatusDto);
					request.setKey(MasterKeyConstants.MaritalStatus.toString());
					request.setPayload(payload);
					//EmrService.publish(request);
				}
				// propagate DAO status — do not mask a failed save as SUCCESS
				if (res.getStatus() != null && res.getStatus().equals(SUCCESS)) {
					return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
				}
				return new Response(ERROR, null, null, null, null);
			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getMaritalById(MaritalStatusDto maritalStatusDto) throws ApplicationException {
		try {
			maritalStatusDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			return iMaritalStatusMasterDao.getMaritalStatusByID(maritalStatusDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateMaritalStatusMaster(MaritalStatusDto maritalStatusDto) throws ApplicationException {
		try {
			maritalStatusDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			Response deptResponse = iMaritalStatusMasterDao.getMaritalStatusByNameNotId(maritalStatusDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*maritalStatusDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/

				Response updateRes = iMaritalStatusMasterDao.updateMaritalStatusMaster(maritalStatusDto);

				if(updateRes.getStatus().equals(SUCCESS) && updateRes.getObject() != null) {
					MaritalStatusMaster master = (MaritalStatusMaster) updateRes.getObject();
					Integer orgId = master.getOrganizationId();
					maritalMasterCache.invalidate(orgId);
				} else {
					// propagate DAO status — do not mask a rejected/failed update as SUCCESS
					return updateRes;
				}

				maritalStatusDto.setCreatedBy(maritalStatusDto.getUpdatedBy());
				maritalStatusDto.setCreatedDate(maritalStatusDto.getUpdatedDate());

				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(maritalStatusDto);
				request.setKey(MasterKeyConstants.MaritalStatus.toString());
				request.setPayload(payload);
				//EmrService.publish(request);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForMarital(MaritalStatusDto maritalStatusDto) throws ApplicationException {
		try {
			maritalStatusDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			Response deptResponse = iMaritalStatusMasterDao.getMaritalStatusByID(maritalStatusDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				/*maritalStatusDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res =iMaritalStatusMasterDao.updateMaritalStatus(maritalStatusDto);
				if(res.getStatus().equals(SUCCESS) && res.getObject() != null){
					Integer filterId = (Integer) res.getObject();
					maritalMasterCache.invalidate(maritalStatusDto.getOrganizationId() != null ? maritalStatusDto.getOrganizationId() : filterId);
				} else {
					// propagate DAO status — do not mask a rejected/failed toggle as SUCCESS
					return res;
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
			// active-list reads bind the JWT-verified org, never the client param
			orgId = verifiedIdentityService.getVerifiedOrganizationId();
			List<MaritalStatusDto> responseCache = maritalMasterCache.getIfPresent(orgId);
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iMaritalStatusMasterDao.getActiveMaritalStatusList(orgId);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				maritalMasterCache.put(orgId, res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getMaritalStatusMasterList(MaritalStatusDto maritalStatusDto) throws ApplicationException {
		try {
			maritalStatusDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			return iMaritalStatusMasterDao.getMaritalStatusMasterList(maritalStatusDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getMaritalStatusCount(MaritalStatusDto maritalStatusDto) throws ApplicationException {
		try {
			maritalStatusDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			return iMaritalStatusMasterDao.getCount(maritalStatusDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

}
