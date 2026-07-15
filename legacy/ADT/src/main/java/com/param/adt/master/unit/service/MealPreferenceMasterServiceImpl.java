package com.param.adt.master.unit.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.adt.master.unit.model.MealPreferenceMaster;
import com.param.global.dto.PatientCategoryMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.master.unit.dao.IMealPreferenceMasterDao;
import com.param.adt.master.unit.dto.MealPreferenceMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import in.param.exception.ApplicationException;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@SuppressWarnings("rawtypes")
public class MealPreferenceMasterServiceImpl  implements IMealPreferenceMasterService, ICommonConstants{

	@Autowired
	IMealPreferenceMasterDao iMealPreferenceMasterDao;

	private Cache<Integer, List<MealPreferenceMasterDto>> mealPreferenceMasterCache;

	@PostConstruct
	public void initCache() {
		mealPreferenceMasterCache = CacheBuilder.newBuilder()
				.maximumSize(500)
				.build();
	}
	
	@Override
	public Response saveMealPreferenceMaster(MealPreferenceMasterDto mealPreferenceMasterDto)
			throws ApplicationException {
		Response res = iMealPreferenceMasterDao.getMealPreferenceMasterByName(mealPreferenceMasterDto);
		try {
			if (res.getListObject().size() > 0 && res.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {

				Response saveRes = iMealPreferenceMasterDao.saveMealPreferenceMaster(mealPreferenceMasterDto);
				if(saveRes.getStatus().equals(SUCCESS)){
					mealPreferenceMasterCache.invalidate(mealPreferenceMasterDto.getUnitId());
				}
				return saveRes;
			}
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getMealPreferenceMasterById(int mealPrefernceId, int orgId, int unitId)
			throws ApplicationException {
		try {
			return iMealPreferenceMasterDao.getMealPreferenceMasterById(mealPrefernceId, orgId, unitId);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getMealPreferenceMasterList(MealPreferenceMasterDto mealPreferenceMasterDto)
			throws ApplicationException {
		try {
			return iMealPreferenceMasterDao.getMealPreferenceMasterList(mealPreferenceMasterDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateNursingStationMaster(MealPreferenceMasterDto mealPreferenceMasterDto){
	Response res=iMealPreferenceMasterDao.getMealPreferenceMasterByNameNotById(mealPreferenceMasterDto);
	try {
		if(res.getListObject().size()>0&&res.getListObject()!=null){
			return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
		}else{
			Response updateRes = iMealPreferenceMasterDao.updateNursingStationMaster(mealPreferenceMasterDto);
			if(updateRes.getStatus().equals(SUCCESS) && updateRes.getObject() != null){
				MealPreferenceMaster master = (MealPreferenceMaster) updateRes.getObject();
				mealPreferenceMasterCache.invalidate(master.getUnitId());
			}
			return updateRes;
		}
	}catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateMealPreferenceMasterStatus(MealPreferenceMasterDto mealPreferenceMasterDto)
			throws ApplicationException {
		try {
			Response res = iMealPreferenceMasterDao.updateMealPreferenceMasterStatus(mealPreferenceMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getObject() != null){
				Integer filterId = (Integer) res.getObject();
				mealPreferenceMasterCache.invalidate(mealPreferenceMasterDto.getUnitId() != null ? mealPreferenceMasterDto.getUnitId() : filterId);
			}
			return res;

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getMealPreferenceMasterCount(MealPreferenceMasterDto mealPreferenceMasterDto)
			throws ApplicationException {
		try {
			return iMealPreferenceMasterDao.getMealPreferenceMasterCount(mealPreferenceMasterDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveMealPreferenceMasterList(MealPreferenceMasterDto mealPreferenceMasterDto)
			throws ApplicationException {
		try {
			List<MealPreferenceMasterDto> responseCache = mealPreferenceMasterCache.getIfPresent(mealPreferenceMasterDto.getUnitId());
			if (responseCache != null && !responseCache.isEmpty()) {
				return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			}

			Response res = iMealPreferenceMasterDao.getActiveMealPreferenceMasterList(mealPreferenceMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				mealPreferenceMasterCache.put(mealPreferenceMasterDto.getUnitId(), res.getListObject());

				return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			}
			return res;
		} catch (Exception e) {

		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
