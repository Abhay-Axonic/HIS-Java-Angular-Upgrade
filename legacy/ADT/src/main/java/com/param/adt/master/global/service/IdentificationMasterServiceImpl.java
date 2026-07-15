package com.param.adt.master.global.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.billing.global.model.BankMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dao.IIdentificationMasterDao;
import com.param.adt.master.global.dto.IdentificationMasterDto;
import com.param.global.model.IdentificationMaster;
import com.param.global.model.RelationMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class IdentificationMasterServiceImpl implements ICommonConstants, IIdentificationMasterService
{

	@Autowired
	private IIdentificationMasterDao identificationDao;
	
	@Autowired
	IEmrService EmrService;

	private Cache<Integer, List<IdentificationMasterDto>> identificationMasterCache;

	@PostConstruct
	public void initCache() {
		identificationMasterCache = CacheBuilder.newBuilder()
				.maximumSize(100)
				.build();
	}
	
	@Override
	public Response addIdentificationMaster(IdentificationMasterDto identificationMasterDto)
			throws ApplicationException {
		try {

			Response deptResponse = identificationDao.getIdentificationByName(identificationMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				
					return new Response(ERROR, null, ALREADY_EXIST, null, null);

				} else {
					/*identificationMasterDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				identificationMasterDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
					Response res = identificationDao.addIdentificationMaster(identificationMasterDto);
					
					if(res.getObject() != null) {
						IdentificationMaster dto = new IdentificationMaster();
						dto = (IdentificationMaster) res.getObject();
						identificationMasterDto.setIdentificationId(dto.getIdentificationId());

						Integer orgId = dto.getOrganizationId();
						identificationMasterCache.invalidate(orgId);

						Request request = new Request();
						Payload payload = new Payload();
						payload.setActionType("create");
						payload.setData(identificationMasterDto);
						request.setKey(MasterKeyConstants.Identification.toString());
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
	public Response getIdentificationMasterList(IdentificationMasterDto identificationMasterDto) throws ApplicationException {
		try {
			return identificationDao.getIdentificationMasterList(identificationMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateIdentificationMaster(IdentificationMasterDto identificationMasterDto) throws ApplicationException {
		try {
			Response deptResponse = identificationDao.getIdentificationByNameNotId(identificationMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*identificationMasterDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/

				Response updateRes = identificationDao.updateIdentificationMaster(identificationMasterDto);
				if(updateRes.getStatus().equals(SUCCESS) && updateRes.getObject() != null) {
					IdentificationMaster master = (IdentificationMaster) updateRes.getObject();
					Integer orgId = master.getOrganizationId();
					identificationMasterCache.invalidate(orgId);
				}
				identificationMasterDto.setCreatedBy(identificationMasterDto.getUpdatedBy());
				identificationMasterDto.setCreatedDate(identificationMasterDto.getUpdatedDate());

				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(identificationMasterDto);
				request.setKey(MasterKeyConstants.Identification.toString());
				request.setPayload(payload);
				//EmrService.publish(request);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getIdentificationById(IdentificationMasterDto identificationMasterDto) throws ApplicationException {
		try {
			return identificationDao.getIdentificationById(identificationMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForIdentification(IdentificationMasterDto identificationMasterDto)
			throws ApplicationException {
		try {
			Response deptResponse = identificationDao.getIdentificationById(identificationMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				/*//System.out.println("Date:"+GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				identificationMasterDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res =identificationDao.updateIdentificationStatus(identificationMasterDto);
				if(res.getStatus().equals(SUCCESS) && res.getObject() != null) {
					Integer filterId = (Integer) res.getObject();
					identificationMasterCache.invalidate(identificationMasterDto.getOrganizationId() != null ? identificationMasterDto.getOrganizationId() : filterId);
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
	public Response getActiveIdentificationList(Integer orgId) throws ApplicationException {
		try {

			List<IdentificationMasterDto> responseCache = identificationMasterCache.getIfPresent(orgId);
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = identificationDao.getActiveIdentificationList(orgId);

			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				identificationMasterCache.put(orgId, res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getIdentificationCount(IdentificationMasterDto identificationMasterDto)
			throws ApplicationException {
		try {
			return identificationDao.getCount(identificationMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveIdentificationMasterListByOrgId(IdentificationMasterDto identificationMasterDto)
			throws ApplicationException {
		try {
			return identificationDao.getActiveIdentificationList(identificationMasterDto.getOrganizationId());
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

}
