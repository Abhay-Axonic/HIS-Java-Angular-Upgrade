package com.param.global.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.global.dto.PrefixMasterDto;
import com.param.global.model.PrefixMaster;
import com.param.global.utility.UserComponent;
import net.sf.saxon.lib.SaxonOutputKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGenderMasterDao;
import com.param.global.dto.GenderMasterDto;
import com.param.global.model.GenderMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"unchecked","rawtypes"})
@Service
public class GenderMasterServiceImpl implements IGenderMasterService,ICommonConstants{
	final static Logger logger = Logger.getLogger(GenderMasterServiceImpl.class);

	
	@Autowired
	private IGenderMasterDao iGenderMasterDao;
	
	@Autowired
	IEmrService EmrService;

	private Cache<String, List<GenderMasterDto>> genderMasterCache;

	@PostConstruct
	public void initCache() {
		genderMasterCache = CacheBuilder.newBuilder()
				.maximumSize(500)
				.build();
	}

	@Override
	public Response getGenderMasterList(GenderMasterDto genderMasterDto) throws ApplicationException {
		
		try {
			return iGenderMasterDao.getGenderMasterList(genderMasterDto);
		} catch (Exception e) {
		logger.error("getGenderMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response addGenderMaster(GenderMasterDto genderMasterDto) {
		// TODO Auto-generated method stub
		try {

			Response deptResponse = iGenderMasterDao.getGenderByName(genderMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				
					return new Response(ERROR, null, ALREADY_EXIST, null, null);
			
			} else {
				/*genderMasterDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				genderMasterDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res = iGenderMasterDao.addGenderMaster(genderMasterDto);

				if(res.getObject() != null) {
				
					GenderMaster dto = new GenderMaster();
					dto = (GenderMaster) res.getObject();

					genderMasterCache.invalidate("genderMaster");

					genderMasterDto.setId(dto.getId());

					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(genderMasterDto);
					request.setKey(MasterKeyConstants.Gender.toString());
					request.setPayload(payload);
					//EmrService.publish(request);
					return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
				
				}
			}
		} catch (Exception e) {
		logger.error("addGenderMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getGenderById(GenderMasterDto genderMasterDto) throws ApplicationException {
		try {
			return iGenderMasterDao.getGenderByID(genderMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response updateGenderMaster(GenderMasterDto genderMasterDto) {
		try {
			Response deptResponse = iGenderMasterDao.getGenderByNameNotId(genderMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*genderMasterDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response updateRes = iGenderMasterDao.updateGenderMaster(genderMasterDto);

				if (updateRes.getStatus().equals(SUCCESS)) {
					genderMasterCache.invalidate("genderMaster");
				}

				genderMasterDto.setCreatedBy(genderMasterDto.getUpdatedBy());
				genderMasterDto.setCreatedDate(genderMasterDto.getUpdatedDate());

				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(genderMasterDto);
				request.setKey(MasterKeyConstants.Gender.toString());
				request.setPayload(payload);
				//EmrService.publish(request);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
		logger.error("updateGenderMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForGender(GenderMasterDto genderMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iGenderMasterDao.getGenderByID(genderMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				/*genderMasterDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				iGenderMasterDao.updateGenderStatus(genderMasterDto);
				genderMasterCache.invalidate("genderMaster");
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}
		} catch (Exception e) {
		logger.error("updateStatusForGender failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveGenderList() throws ApplicationException {
		try {
			List<GenderMasterDto> responseCache = genderMasterCache.getIfPresent("genderMaster");
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iGenderMasterDao.getActiveGenderList();

			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				genderMasterCache.put("genderMaster", res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		} catch (Exception e) {
		logger.error("getActiveGenderList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getActiveGenderList(GenderMasterDto genderMasterDto) throws ApplicationException {
		try {
			return iGenderMasterDao.getCount(genderMasterDto);
		} catch (Exception e) {
		logger.error("getActiveGenderList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

}
