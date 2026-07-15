package com.param.adt.master.global.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.global.dto.PatientCategoryMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dao.IReferralTypeMasterDao;
import com.param.adt.master.global.dto.ReferralTypeMasterDto;
import com.param.adt.master.global.model.ReferralTypeMaster;
import com.param.global.model.AreaMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReferralTypeMasterServiceImpl implements IReferralTypeMasterService, ICommonConstants {

	@Autowired
	private IReferralTypeMasterDao iReferralTypeMasterDao;
	
	@Autowired
	IEmrService EmrService;

	private Cache<Integer, List<ReferralTypeMasterDto>> referralTypeMasterCache;

	@PostConstruct
	public void initCache() {
		referralTypeMasterCache = CacheBuilder.newBuilder()
				.maximumSize(100)
				.build();
	}
	@Override
	public Response addReferralTypeMaster(ReferralTypeMasterDto referralTypeMasterDto) throws ApplicationException {
		try {

			Response deptResponse = iReferralTypeMasterDao.getReferralTypeByName(referralTypeMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*referralTypeMasterDto
						.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				referralTypeMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res = iReferralTypeMasterDao.addReferralTypeTypeMaster(referralTypeMasterDto);
				
				if(res.getObject() != null) {

					referralTypeMasterCache.invalidate(referralTypeMasterDto.getOrganizationId());

					ReferralTypeMaster dto = new ReferralTypeMaster();
					dto = (ReferralTypeMaster) res.getObject();
					referralTypeMasterDto.setId(dto.getId());
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(referralTypeMasterDto);
					request.setKey(MasterKeyConstants.ReferralType.toString());
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
	public Response getReferralTypeMasterList(ReferralTypeMasterDto referralTypeMasterDto) throws ApplicationException {
		try {
			return iReferralTypeMasterDao.getReferralTypeMasterList(referralTypeMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateReferralTypeMaster(ReferralTypeMasterDto referralTypeMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iReferralTypeMasterDao.getReferralTypeByNameNotId(referralTypeMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*referralTypeMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/

				Response updateRes = iReferralTypeMasterDao.updateReferralTypeMaster(referralTypeMasterDto);

				if(updateRes.getStatus().equals(SUCCESS) && updateRes.getObject() != null) {
					ReferralTypeMaster dto = (ReferralTypeMaster) updateRes.getObject();
					referralTypeMasterCache.invalidate(dto.getOrganizationId());
				}

				referralTypeMasterDto.setCreatedBy(referralTypeMasterDto.getUpdatedBy());
				referralTypeMasterDto.setCreatedDate(referralTypeMasterDto.getUpdatedDate());
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(referralTypeMasterDto);
				request.setKey(MasterKeyConstants.ReferralType.toString());
				request.setPayload(payload);
				//EmrService.publish(request);
				
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getReferralTypeById(ReferralTypeMasterDto referralTypeMasterDto) throws ApplicationException {
		try {
			return iReferralTypeMasterDao.getReferralTypeById(referralTypeMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForReferralType(ReferralTypeMasterDto referralTypeMasterDto)
			throws ApplicationException {
		try {
			Response deptResponse = iReferralTypeMasterDao.getReferralTypeById(referralTypeMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
			/*	referralTypeMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res = iReferralTypeMasterDao.updateStatusForReferralType(referralTypeMasterDto);
				if(res.getStatus().equals(SUCCESS) && res.getObject() != null) {
					Integer filterId = (Integer) res.getObject();
					referralTypeMasterCache.invalidate(referralTypeMasterDto.getOrganizationId() != null ? referralTypeMasterDto.getOrganizationId() : filterId);
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
	public Response getReferralTypeCount(ReferralTypeMasterDto referralTypeMasterDto) throws ApplicationException {
		try {
			return iReferralTypeMasterDao.getCount(referralTypeMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveReferralTypeList(ReferralTypeMasterDto referralTypeMasterDto) throws ApplicationException {
		try {

			List<ReferralTypeMasterDto> responseCache = referralTypeMasterCache.getIfPresent(referralTypeMasterDto.getOrganizationId());
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}


			Response res = iReferralTypeMasterDao.getActiveReferralTypeList(referralTypeMasterDto);

			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				referralTypeMasterCache.put(referralTypeMasterDto.getOrganizationId(), res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

}
