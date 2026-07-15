package com.param.global.service;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.billing.global.model.BankMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.ICountryMasterDao;
import com.param.global.dao.IStateMasterDao;
import com.param.global.dto.CountryMasterDto;
import com.param.global.dto.StateMasterDto;
import com.param.global.model.CountryMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import javax.annotation.PostConstruct;
import java.util.List;


import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Service

public class CountryMasterServiceImpl implements ICountryMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(CountryMasterServiceImpl.class);


	@Autowired
	private ICountryMasterDao iCountryDao;
	
	@Autowired
	private IOtimsService iOtimsService;
	
	@Autowired
	IEmrService EmrService;
	
	@Autowired
	private IStateMasterDao iStateMasterDao;

	private Cache<Integer, List<CountryMasterDto>> countryByIdMasterCache;
	private Cache<Integer, List<CountryMasterDto>> countryMasterCache;

	@PostConstruct
	public void initCache() {
		countryByIdMasterCache = CacheBuilder.newBuilder()
				.maximumSize(500)
				.build();

		countryMasterCache = CacheBuilder.newBuilder()
				.maximumSize(100)
				.build();
	}

	@Override
	public Response addCountryMaster(CountryMasterDto countryMasterDto) throws ApplicationException {
		try {

			Response deptResponse = iCountryDao.getCountryByName(countryMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, deptResponse.getListObject(), null);

			} else {
				/*countryMasterDto
						.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				countryMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				*/
				Response res = iCountryDao.addCountryMaster(countryMasterDto);
				if(res.getObject() != null) {
					CountryMaster dto = new CountryMaster();
					dto = (CountryMaster) res.getObject();
					countryMasterDto.setCountryId(dto.getCountryId());

					countryByIdMasterCache.invalidate(countryMasterDto.getCountryId());
					countryMasterCache.invalidate(countryMasterDto.getOrganizationId());

					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(countryMasterDto);
					request.setKey(MasterKeyConstants.Country.toString());
					request.setPayload(payload);
					//EmrService.publish(request);
					//iOtimsService.publish(request);
				}
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, res);
			}
		} catch (Exception e) {
		logger.error("addCountryMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCountryMasterList(CountryMasterDto countryMasterDto) throws ApplicationException {
		try {
			return iCountryDao.getCountryMasterList(countryMasterDto);
		} catch (Exception e) {
		logger.error("getCountryMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateCountryMaster(CountryMasterDto countryMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iCountryDao.getCountryByNameNotId(countryMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*countryMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/

				Response updateRes = iCountryDao.updateCountryMaster(countryMasterDto);
				if(updateRes.getStatus().equals(SUCCESS) && updateRes.getObject() != null) {
					CountryMaster master = (CountryMaster) updateRes.getObject();
					Integer Id = master.getCountryId();
					Integer OrgId = master.getOrganizationId();
					countryByIdMasterCache.invalidate(Id);
					countryMasterCache.invalidate(OrgId);
				}
				countryMasterDto.setCreatedBy(countryMasterDto.getUpdatedBy());
				countryMasterDto.setCreatedDate(countryMasterDto.getUpdatedDate());

				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(countryMasterDto);
				request.setKey(MasterKeyConstants.Country.toString());
				request.setPayload(payload);
				//EmrService.publish(request);
				//iOtimsService.publish(request);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
		logger.error("updateCountryMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCountryById(CountryMasterDto countryMasterDto) throws ApplicationException {
		try {
			List<CountryMasterDto> responseCache = countryByIdMasterCache.getIfPresent(countryMasterDto.getCountryId());
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iCountryDao.getCountryByID(countryMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				countryByIdMasterCache.put(countryMasterDto.getCountryId(), res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;

		} catch (Exception e) {
		logger.error("getCountryById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForCountry(CountryMasterDto countryMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iCountryDao.getCountryByID(countryMasterDto);
			
			//checke in use or not 
			StateMasterDto state=new StateMasterDto();
			state.setCountryId(countryMasterDto.getCountryId());
			Response resp = iStateMasterDao.getStateByCountryId(state);
			if(resp.getListObject() != null && resp.getListObject().size() > 0) {
				return new Response(ERROR, ALREADY_EXIST_CODE, "This country already is in use! ", null, null);
			}
			
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				/*System.out
						.println("Date:" + GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				countryMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res = iCountryDao.updateCountryStatus(countryMasterDto);
				if(res.getStatus().equals(SUCCESS)) {
					if(res.getObject() != null) {
						Integer filterId = (Integer) res.getObject();
						countryMasterCache.invalidate(countryMasterDto.getOrganizationId() != null ? countryMasterDto.getOrganizationId() : filterId);
					}
					countryByIdMasterCache.invalidate(countryMasterDto.getCountryId());
				}

				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}
		} catch (Exception e) {
		logger.error("updateStatusForCountry failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveCountryList(Integer orgId) throws ApplicationException {
		try {
			List<CountryMasterDto> responseCache = countryMasterCache.getIfPresent(orgId);
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iCountryDao.getActiveCountryList(orgId);

			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				countryMasterCache.put(orgId, res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;

		} catch (Exception e) {
		logger.error("getActiveCountryList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCountryCount(CountryMasterDto countryMasterDto) throws ApplicationException {
		try {
			return iCountryDao.getCount(countryMasterDto);
		} catch (Exception e) {
		logger.error("getCountryCount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCountryMasterList() throws ApplicationException {
		try {
			return iCountryDao.getCountryMasterList();
		} catch (Exception e) {
		logger.error("getCountryMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

}
