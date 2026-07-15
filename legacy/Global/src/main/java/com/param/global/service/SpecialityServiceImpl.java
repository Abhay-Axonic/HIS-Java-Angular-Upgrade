package com.param.global.service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.billing.global.model.BankMaster;
import com.param.global.dto.PrefixMasterDto;
import com.param.global.model.PrefixMaster;
import com.param.global.model.SpecialityMaster;
import com.param.global.utility.UserComponent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.ISpecialityMasterDao;
import com.param.global.dto.SpecialityMasterDto;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SpecialityServiceImpl implements ISpecialityService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(SpecialityServiceImpl.class);

	@Autowired
	private ISpecialityMasterDao iSpecialityMasterDao;

	/*
	 * @Autowired SessionObject sessionObject;
	 */
	
	@Autowired
	private IOtimsService iOtimsService;
	
	@Autowired
	IEmrService EmrService;


	private Cache<Integer, List<SpecialityMasterDto>> specialityMasterCache;

	@PostConstruct
	public void initCache() {
		specialityMasterCache = CacheBuilder.newBuilder()
				.maximumSize(100)
				.build();
	}

	@Transactional
	@Override
	public Response getSpecialityMasterList(SpecialityMasterDto specialityMasterDto) throws ApplicationException {

		// TODO Auto-generated method stub
		try {
			return iSpecialityMasterDao.getSpecialityMasterList(specialityMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Transactional
	@Override
	public Response addSpecialityMaster(SpecialityMasterDto specialityMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iSpecialityMasterDao.getSpecialityByName(specialityMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				specialityMasterDto.setStatus('A');
//				specialityMasterDto.setGeneralLedgerId(001);
				Response res = iSpecialityMasterDao.addSpecialityMaster(specialityMasterDto);
				if(res.getStatus().equals(SUCCESS) && res.getObject() != null) {
					Integer Id = specialityMasterDto.getUnitId();
					specialityMasterCache.invalidate(Id);
				}
				return res;

			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Transactional
	@Override
	public Response updateSpecialityMaster(SpecialityMasterDto SpecialityMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iSpecialityMasterDao.getSpecialityByNameNotId(SpecialityMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				SpecialityMasterDto.setCreatedBy(SpecialityMasterDto.getCreatedBy());

				Response updateRes = iSpecialityMasterDao.updateSpecialityMaster(SpecialityMasterDto);
				if(updateRes.getStatus().equals(SUCCESS) && updateRes.getObject() != null) {
					Integer Id = SpecialityMasterDto.getUnitId();
					specialityMasterCache.invalidate(Id);
				}
				CommonResponse response = new CommonResponse();
				response.setMessage("subSpeciality Updated Successfully");
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(SpecialityMasterDto);
				request.setKey(MasterKeyConstants.Speciality.toString());
				request.setPayload(payload);
				//iOtimsService.publish(request);
				//EmrService.publish(request);
				
				
				
				
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Transactional
	@Override
	public Response getSpecialityByName(SpecialityMasterDto SpecialityMasterDto) throws ApplicationException {
		try {
			return iSpecialityMasterDao.getSpecialityByName(SpecialityMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	/*
	 * @Override public Response getActiveSpecialityList() throws
	 * ApplicationException { // TODO Auto-generated method stub return null; }
	 */

	@Transactional
	@Override
	public Response getSpecialityById(SpecialityMasterDto specialityMasterDto) {
		try {
			return iSpecialityMasterDao.getSpecialityById(specialityMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Transactional
	@Override
	public Response updateStatus(SpecialityMasterDto specialityMasterDto) {
		try {
			Response deptResponse = iSpecialityMasterDao.getSpecialityCheckWithUnitMapper(specialityMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				
				return new Response(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);

			} else {
				
				iSpecialityMasterDao.updateSpecialityStatus(specialityMasterDto);
//				specialityMasterCache.invalidate(specialityMasterDto.getUnitId());
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Transactional
	@Override
	public Response getActiveSpecialityMasterList(SpecialityMasterDto specialityMasterDto) throws ApplicationException {
		try {
			List<SpecialityMasterDto> responseCache = specialityMasterCache.getIfPresent(specialityMasterDto.getUnitId());
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iSpecialityMasterDao.getActiveSpecialityList(specialityMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				specialityMasterCache.put(specialityMasterDto.getUnitId(), res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	@Transactional
	@Override
	public Response getSpecialityListForSubSpeciality(int orgId) throws ApplicationException {
		try {
			return iSpecialityMasterDao.getSpecialityListForSubSpeciality(orgId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	@Transactional
	@Override
	public Response getSpecialityCount(SpecialityMasterDto specialityMasterDto) throws ApplicationException {
		try {
			return iSpecialityMasterDao.getCount(specialityMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	@Transactional
	@Override
	public Response getOrgActiveSpecialityList(SpecialityMasterDto specialityMasterDto) throws ApplicationException {
		try {
			return iSpecialityMasterDao.getOrgActiveSpecialityList(specialityMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getSpecialityNameById(SpecialityMasterDto specialityMasterDto) throws ApplicationException {
		try {
			return iSpecialityMasterDao.getSpecialityNameById(specialityMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response getStoreBySpecialtyId(SpecialityMasterDto specialityMasterDto) throws ApplicationException {
		try {
			return iSpecialityMasterDao.getStoreBySpecialtyId(specialityMasterDto);
		} catch (Exception e) {
			logger.error("Exception in getStoreBySpecialtyId", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSpecialityListOfActiveInactive(SpecialityMasterDto specialityMasterDto)
			throws ApplicationException {
		try {
			return iSpecialityMasterDao.getSpecialityListOfActiveInactive(specialityMasterDto);
		} catch (Exception e) {
			logger.error("Exception in getSpecialityListOfActiveInactive", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	@Transactional
	public Response departmentMasterUniversalSearch(SpecialityMasterDto specialityMasterDto)
			throws ApplicationException {
		try {
			return iSpecialityMasterDao.departmentMasterUniversalSearch(specialityMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getActiveSpecialityListForModality(SpecialityMasterDto specialityMasterDto)
			throws ApplicationException {
		try {
			return iSpecialityMasterDao.getActiveSpecialityListForModality(specialityMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public void invalidateSpecialityMasterCache(Integer unitId){
		specialityMasterCache.invalidate(unitId);
	}
}
