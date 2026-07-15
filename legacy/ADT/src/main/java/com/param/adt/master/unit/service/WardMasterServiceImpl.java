package com.param.adt.master.unit.service;

import java.util.List;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.adt.master.unit.model.WardMaster;
import com.param.global.dto.CompanyMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.master.unit.dao.IWardMasterDao;
import com.param.adt.master.unit.dto.WardGenderMapperDto;
import com.param.adt.master.unit.dto.WardMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.MultiSelectDto;

import in.param.exception.ApplicationException;

import javax.annotation.PostConstruct;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class WardMasterServiceImpl implements IWardMasterService, ICommonConstants {

	@Autowired
	IWardMasterDao iWardMasterDao;

	private Cache<Integer, List<WardMasterDto>> activeWardMasterCache;

	@PostConstruct
	public void initCache() {
		activeWardMasterCache = CacheBuilder.newBuilder()
				.maximumSize(500)
				.build();
	}

	@Override
	public Response saveWardMasterMaster(WardMasterDto wardMasterDto) throws ApplicationException {

		Response response = iWardMasterDao.getWardMasterByName(wardMasterDto);

		try {
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				Response res = iWardMasterDao.saveWardMasterMaster(wardMasterDto);

				if(res.getStatus().equals(SUCCESS)) {
					activeWardMasterCache.invalidate(wardMasterDto.getUnitId());
				}

				Integer wardId = (Integer) res.getObject();
				for(MultiSelectDto multiSelectDto : wardMasterDto.getGenderIdList())
				{
					WardGenderMapperDto wardGenderMapperDto = new WardGenderMapperDto();
						wardGenderMapperDto.setWardId(wardId);
						wardGenderMapperDto.setGenderId(multiSelectDto.getId());
						wardGenderMapperDto.setStatus('A');
						wardGenderMapperDto.setCreatedBy(wardMasterDto.getCreatedBy());
						wardGenderMapperDto.setCreatedDate(wardMasterDto.getCreatedDate());
						wardGenderMapperDto.setUpdatedBy(wardMasterDto.getUpdatedBy());
						wardGenderMapperDto.setUpdatedDate(wardMasterDto.getUpdatedDate());
					iWardMasterDao.saveWardGenderMapper(wardGenderMapperDto);
				}
				return res;
			}
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWardMasterById(int wardId, int orgId, int unitId) throws ApplicationException {
		try {
			Response res =  iWardMasterDao.getWardMasterById(wardId, orgId, unitId);
			List<WardMasterDto> wardMasterList = res.getListObject();
			for(WardMasterDto wardMastertr : wardMasterList)
			{
				Response res2 = iWardMasterDao.getGenderListByWardId(wardMastertr.getWardId());
				wardMastertr.setGenderIdList(res2.getListObject());
			}
			return res;
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWardMasterList(WardMasterDto wardMasterDto) throws ApplicationException {
		try {
			Response res = iWardMasterDao.getWardMasterList(wardMasterDto);
			List<WardMasterDto> wardMasterList = res.getListObject();
			for(WardMasterDto wardMastertr : wardMasterList)
			{
				Response res2 = iWardMasterDao.getGenderListByWardId(wardMastertr.getWardId());
				wardMastertr.setGenderIdList(res2.getListObject());
			}
			return res;
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateWardMaster(WardMasterDto wardMasterDto) throws ApplicationException {
		Response response = iWardMasterDao.getWardMasterByNameNotById(wardMasterDto);

		try {
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {

				Response updateRes = iWardMasterDao.updateWardMaster(wardMasterDto);

				if(updateRes.getStatus().equals(SUCCESS) && updateRes.getObject() != null) {
					WardMaster master = (WardMaster) updateRes.getObject();
					activeWardMasterCache.invalidate(master.getUnitId());
				}

				iWardMasterDao.updateWardGenderStatus(wardMasterDto.getWardId());
				for(MultiSelectDto multiSelectDto : wardMasterDto.getGenderIdList())
				{
					WardGenderMapperDto wardGenderMapperDto = new WardGenderMapperDto();
						wardGenderMapperDto.setWardId(wardMasterDto.getWardId());
						wardGenderMapperDto.setGenderId(multiSelectDto.getId());
						wardGenderMapperDto.setStatus('A');
						wardGenderMapperDto.setCreatedBy(wardMasterDto.getUpdatedBy());
						wardGenderMapperDto.setCreatedDate(wardMasterDto.getUpdatedDate());
						wardGenderMapperDto.setUpdatedBy(wardMasterDto.getUpdatedBy());
						wardGenderMapperDto.setUpdatedDate(wardMasterDto.getUpdatedDate());
					iWardMasterDao.saveWardGenderMapper(wardGenderMapperDto);
				}
				return new Response (SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null); 
			}
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateWardMasterStatus(WardMasterDto wardMasterDto) throws ApplicationException {
		try {
			Response res = iWardMasterDao.updateWardMasterStatus(wardMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getObject() != null) {
					Integer filterId = (Integer) res.getObject();
				activeWardMasterCache.invalidate(wardMasterDto.getUnitId() != null ? wardMasterDto.getUnitId() : filterId);
			}
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWardMasterCount(WardMasterDto wardMasterDto) throws ApplicationException {
		try {
			return iWardMasterDao.getWardMasterCount(wardMasterDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveWardMasterList(WardMasterDto wardMasterDto) throws ApplicationException {
		try {
			List<WardMasterDto> responseCache = activeWardMasterCache.getIfPresent(wardMasterDto.getUnitId());
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iWardMasterDao.getActiveWardMasterList(wardMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				activeWardMasterCache.put(wardMasterDto.getUnitId(), res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWardListNotMappedToNursingStation(WardMasterDto wardMasterDto) throws ApplicationException {
		try {
			return iWardMasterDao.getWardListNotMappedToNursingStation(wardMasterDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveWardListForER(WardMasterDto wardMasterDto) throws ApplicationException {
		
		try {
			return iWardMasterDao.getActiveWardListForER(wardMasterDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		
	}

	@Override
	public Response getWardForDirectTransfer(int genderId, int visitTypeId, int unitId) throws ApplicationException {
		try {
			return iWardMasterDao.getWardForDirectTransfer(genderId,visitTypeId,unitId);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWardByVisitType(WardMasterDto wardMasterDto) throws ApplicationException {
		try {
			return iWardMasterDao.getWardByVisitType(wardMasterDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

}
