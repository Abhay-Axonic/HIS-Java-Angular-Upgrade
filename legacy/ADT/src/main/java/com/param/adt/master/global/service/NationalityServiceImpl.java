package com.param.adt.master.global.service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.global.dto.RelationMasterDto;
import com.param.global.model.RelationMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dao.INationalityMasterDao;
import com.param.adt.master.global.dto.NationalityMasterDto;
import com.param.adt.master.global.model.NationalityMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import java.util.List;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NationalityServiceImpl implements INationalityMasterService, ICommonConstants {

	@Autowired
	INationalityMasterDao iNatioanlityMasterDao;
	
	@Autowired
	IEmrService EmrService;

	private Cache<Integer, List<NationalityMasterDto>> nationalityCache;
	private Cache<Integer, List<NationalityMasterDto>> nationalityByCountryCache;

	@PostConstruct
	public void initCache() {
		nationalityCache = CacheBuilder.newBuilder()
				.maximumSize(100)
				.build();

		nationalityByCountryCache = CacheBuilder.newBuilder()
				.maximumSize(500)
				.build();
	}

	@Transactional
	@Override
	public Response addNationalityMaster(NationalityMasterDto nationalityMasterDto) throws ApplicationException {
		try {

			Response deptResponse = iNatioanlityMasterDao.getNationalityByName(nationalityMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				
					return new Response(ERROR, null, ALREADY_EXIST, null, null);
				
			} else {
				/*nationalityMasterDto
						.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				nationalityMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				
				Response res = iNatioanlityMasterDao.addNationalityTypeMaster(nationalityMasterDto);
				
				if(res.getObject() != null) {
					
					NationalityMaster dto = new NationalityMaster();
					dto = (NationalityMaster) res.getObject();
					nationalityMasterDto.setNationalityId(dto.getNationalityId());

					Integer orgId = dto.getOrganizationId();
					nationalityCache.invalidate(orgId);

					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(nationalityMasterDto);
					request.setKey(MasterKeyConstants.Nationality.toString());
					request.setPayload(payload);
					//EmrService.publish(request);
				
				}
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Transactional
	@Override
	public Response getNationalityMasterList(NationalityMasterDto nationalityMasterDto) throws ApplicationException {
		try {
			return iNatioanlityMasterDao.getNationalityMasterList(nationalityMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	

	@Transactional
	@Override
	public Response updateNationalityMaster(NationalityMasterDto nationalityMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iNatioanlityMasterDao.getNationalityByNameNotId(nationalityMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*nationalityMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/

				Response updateRes =  iNatioanlityMasterDao.updateNationalityMaster(nationalityMasterDto);

				if(updateRes.getStatus().equals(SUCCESS) && updateRes.getObject() != null) {
					NationalityMaster master = (NationalityMaster) updateRes.getObject();
					Integer orgId = master.getOrganizationId();
					nationalityCache.invalidate(orgId);
				}

				nationalityMasterDto.setCreatedBy(nationalityMasterDto.getUpdatedBy());
				nationalityMasterDto.setCreatedDate(nationalityMasterDto.getUpdatedDate());
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(nationalityMasterDto);
				request.setKey(MasterKeyConstants.Nationality.toString());
				request.setPayload(payload);
				//EmrService.publish(request);
			
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Transactional
	@Override
	public Response getNationalityById(NationalityMasterDto nationalityMasterDto) throws ApplicationException {
		try {
			return iNatioanlityMasterDao.getNationalityByID(nationalityMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Transactional
	@Override
	public Response updateStatusForNationality(NationalityMasterDto nationalityMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iNatioanlityMasterDao.getNationalityByID(nationalityMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				/*nationalityMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res = iNatioanlityMasterDao.updateNationalityStatus(nationalityMasterDto);
				if(res.getStatus().equals(SUCCESS) && res.getObject() != null) {
					Integer filterId = (Integer) res.getObject();
					nationalityCache.invalidate(nationalityMasterDto.getOrganizationId() != null ? nationalityMasterDto.getOrganizationId() : filterId);
				}
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Transactional
	@Override
	public Response getActiveNationalityList(Integer orgId) throws ApplicationException {
		try {

			List<NationalityMasterDto> responseCache = nationalityCache.getIfPresent(orgId);
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res =  iNatioanlityMasterDao.getActiveNationalityMasterList(orgId);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				nationalityCache.put(orgId, res.getListObject());
				return res;
			}
			return res;
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	@Transactional
	@Override
	public Response getNationalityCount(NationalityMasterDto nationalityMasterDto) throws ApplicationException {
		try {
			return iNatioanlityMasterDao.getCount(nationalityMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	@Transactional
	@Override
	public Response getNationalityByCountryId(NationalityMasterDto nationalityMasterDto) throws ApplicationException {
		try {
			List<NationalityMasterDto> responseCache = nationalityByCountryCache.getIfPresent(nationalityMasterDto.getCountryId());
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iNatioanlityMasterDao.getNationalityByCountryId(nationalityMasterDto);

			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				nationalityByCountryCache.put(nationalityMasterDto.getCountryId(), res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getActiveNationalityMasterListByOrgId(NationalityMasterDto nationalityMasterDto)
			throws ApplicationException {
		try {
			return iNatioanlityMasterDao.getActiveNationalityMasterList(nationalityMasterDto.getOrganizationId());
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

}
