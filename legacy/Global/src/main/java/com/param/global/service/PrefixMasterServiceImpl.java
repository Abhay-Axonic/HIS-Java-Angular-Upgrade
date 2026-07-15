package com.param.global.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.billing.global.model.BankMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IPrefixMasterDao;
import com.param.global.dto.PrefixMasterDto;
import com.param.global.model.PrefixMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import javax.annotation.PostConstruct;
import java.util.List;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes","unchecked"})
@Service
public class PrefixMasterServiceImpl implements IPrefixMasterService,ICommonConstants
{
	final static Logger logger = Logger.getLogger(PrefixMasterServiceImpl.class);


	@Autowired
	private IPrefixMasterDao iPrefixMasterDao; 
	
	@Autowired
	IEmrService EmrService;

	private Cache<Integer, List<PrefixMasterDto>> prefixMasterCache;

	@PostConstruct
	public void initCache() {
		prefixMasterCache = CacheBuilder.newBuilder()
				.maximumSize(100)
				.build();
	}

	@Override
	public Response addPrefixMaster(PrefixMasterDto prefixMasterDto) throws ApplicationException {
		try {

			Response deptResponse = iPrefixMasterDao.getPrefixByName(prefixMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				
					return new Response(ERROR, null, ALREADY_EXIST, null, null);
				
			} else {
				/*prefixMasterDto
						.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				prefixMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res=iPrefixMasterDao.addPrefixTypeMaster(prefixMasterDto);
				if(res.getObject() != null) {
			
					PrefixMaster dto = new PrefixMaster();
					dto= (PrefixMaster) res.getObject();
					prefixMasterDto.setPrefixId(dto.getPrefixId());

					Integer orgId = dto.getOrganizationId();
					prefixMasterCache.invalidate(orgId);

					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(prefixMasterDto);
					request.setKey(MasterKeyConstants.Prefix.toString());
					request.setPayload(payload);
					//EmrService.publish(request);
				}
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
		logger.error("addPrefixMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPrefixMasterList(PrefixMasterDto prefixMasterDto) throws ApplicationException {
		try {
			return iPrefixMasterDao.getPrefixMasterList(prefixMasterDto);
		} catch (Exception e) {
		logger.error("getPrefixMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updatePrefixMaster(PrefixMasterDto prefixMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iPrefixMasterDao.getPrefixByNameNotId(prefixMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*prefixMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response updateRes = iPrefixMasterDao.updatePrefixMaster(prefixMasterDto);

				if(updateRes.getStatus().equals(SUCCESS) && updateRes.getObject() != null) {
					PrefixMaster master = (PrefixMaster) updateRes.getObject();
					Integer orgId = master.getOrganizationId();
					prefixMasterCache.invalidate(orgId);
				}

				prefixMasterDto.setCreatedBy(prefixMasterDto.getUpdatedBy());
				prefixMasterDto.setCreatedDate(prefixMasterDto.getUpdatedDate());
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(prefixMasterDto);
				request.setKey(MasterKeyConstants.Prefix.toString());
				request.setPayload(payload);
				//EmrService.publish(request);
				
				
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
		logger.error("updatePrefixMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPrefixById(PrefixMasterDto prefixMasterDto) throws ApplicationException {
		try {
			return iPrefixMasterDao.getPrefixByID(prefixMasterDto);
		} catch (Exception e) {
		logger.error("getPrefixById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForPrefix(PrefixMasterDto prefixMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iPrefixMasterDao.getPrefixByID(prefixMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				/*prefixMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res = iPrefixMasterDao.updatePrefixStatus(prefixMasterDto);
				if(res.getStatus().equals(SUCCESS) && res.getObject() != null) {
					Integer filterId = (Integer) res.getObject();
					prefixMasterCache.invalidate(prefixMasterDto.getOrganizationId() != null ? prefixMasterDto.getOrganizationId() : filterId);
				}
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}
		} catch (Exception e) {
		logger.error("updateStatusForPrefix failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActivePrefixList(Integer orgId) throws ApplicationException {
		try {
			List<PrefixMasterDto> responseCache = prefixMasterCache.getIfPresent(orgId);
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iPrefixMasterDao.getActivePrefixList(orgId);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				prefixMasterCache.put(orgId, res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;

		} catch (Exception e) {
		logger.error("getActivePrefixList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPrefixCount(PrefixMasterDto prefixMasterDto) throws ApplicationException {
		try {
			return iPrefixMasterDao.getCount(prefixMasterDto);
		} catch (Exception e) {
		logger.error("getPrefixCount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActivePrefixListForWebPage(PrefixMasterDto prefixMasterDto) throws ApplicationException {
		try {
			return iPrefixMasterDao.getActivePrefixListForWebPage(prefixMasterDto);
		} catch (Exception e) {
		logger.error("getActivePrefixListForWebPage failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPrefixMasterAndMedallPrefixMaster(PrefixMasterDto prefixMasterDto) throws ApplicationException {
		try {
			return iPrefixMasterDao.getPrefixMasterAndMedallPrefixMaster(prefixMasterDto);
		} catch (Exception e) {
		logger.error("getPrefixMasterAndMedallPrefixMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	
}
