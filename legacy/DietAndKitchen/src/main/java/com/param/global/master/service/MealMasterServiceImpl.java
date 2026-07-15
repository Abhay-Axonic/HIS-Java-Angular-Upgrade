package com.param.global.master.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.master.dao.IMealMasterDao;
import com.param.global.master.dto.MealMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MealMasterServiceImpl implements IMealMasterService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(MealMasterServiceImpl.class);

	@Autowired
	IMealMasterDao iMealMasterDao;

	@Override
	public Response saveMealMaster(MealMasterDto mealMasterDto) throws ApplicationException {
		Response deptResponse = iMealMasterDao.getMealMasterByName(mealMasterDto);
		Response resp = iMealMasterDao.getMealMasterByTime(mealMasterDto);
		try {
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, null, "Meal with same code already exists", null, null);
			}
			else if (resp.getListObject() != null && resp.getListObject().size() > 0) {
				return new Response(ERROR, null, "Meal with same serving time already exists", null, null);
			}
			else {
				return iMealMasterDao.saveMealMaster(mealMasterDto);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAllMealMasterList(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			return iMealMasterDao.getAllMealMasterList(organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveMealMasterList(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			return iMealMasterDao.getActiveMealMasterList(organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getMealMasterDropDown(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			return iMealMasterDao.getMealMasterDropDown(organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateStatus(MealMasterDto mealMasterDto) {
		try {
			return iMealMasterDao.updateStatus(mealMasterDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response update(MealMasterDto mealMasterDto) {
		Response deptResponse = iMealMasterDao.getMealMasterByNameNotById(mealMasterDto);//start code for condition for already exist. by minesh
		Response resp = iMealMasterDao.getMealMasterByTimeNotById(mealMasterDto);
		try {
			//return iMealMasterDao.update(mealMasterDto); //coment by minesh.
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, null, "Meal with same code already exists", null, null);
				}else if (resp.getListObject() != null && resp.getListObject().size() > 0) {
					return new Response(ERROR, null, "Meal with same serving time already exists", null, null);
					}
				else {
					return iMealMasterDao.update(mealMasterDto);
				}//end code for condition for already exist. by minesh

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAllMealMasterListPaginationList(MealMasterDto mealMasterDto) throws ApplicationException {
		try {
			
			return iMealMasterDao.getAllMealMasterListPaginationList(mealMasterDto);

	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getAllMealMasterListPaginationListCount(MealMasterDto mealMasterDto) throws ApplicationException {
		try {
			
			return iMealMasterDao.getAllMealMasterListPaginationListCount(mealMasterDto);

	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
}

}
