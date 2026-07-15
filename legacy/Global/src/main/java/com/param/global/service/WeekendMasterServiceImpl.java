package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IWeekendMasterDao;
import com.param.global.dto.WeekendMasterDto;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class WeekendMasterServiceImpl implements IWeekendMasterService,ICommonConstants{
	final static Logger logger = Logger.getLogger(WeekendMasterServiceImpl.class);


	@Autowired
	IWeekendMasterDao iWeekendMasterDao;
	
	@Override
	public Response saveWeekendMaster(WeekendMasterDto weekendMasterDto) throws ApplicationException {
		try {
			
		/*	List<String> datesList = GlobalCommonDateUtils.getDatesOfSpecificDayInSingleYear(weekendMasterDto.getYear(),
					weekendMasterDto.getDayId());

			ListIterator<String> itr = datesList.listIterator();
			while (itr.hasNext()) {
				weekendMasterDto.setWeekendDate(itr.next());
				iWeekendMasterDao.saveWeekendMaster(weekendMasterDto);
			}*/
			
			Response response=iWeekendMasterDao.getWeekendMasterByName(weekendMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iWeekendMasterDao.saveWeekendMaster(weekendMasterDto);
				
			}
			
		} catch (Exception e) {
		logger.error("saveWeekendMaster failed", e);
		}
		return new Response(ERROR, ERROR_CODE, null, null, null);

	}

	@Override
	public Response getWeekendMasterList(WeekendMasterDto weekendMasterDto) throws ApplicationException {
		try {
				return iWeekendMasterDao.getWeekendMasterList(weekendMasterDto);

		} catch (Exception e) {
		logger.error("getWeekendMasterList failed", e);
		}
	return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateWeekendMaster(WeekendMasterDto weekendMasterDto) throws ApplicationException {
		try {
			Response response=iWeekendMasterDao.getWeekendMasterByNameNotId(weekendMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iWeekendMasterDao.updateWeekendMaster(weekendMasterDto);
			}
				
		} catch (Exception e) {
		logger.error("updateWeekendMaster failed", e);
		}
	return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateWeekendMasterStatus(WeekendMasterDto weekendMasterDto) throws ApplicationException {
		try {
			return iWeekendMasterDao.updateWeekendMasterStatus(weekendMasterDto);

		} catch (Exception e) {
		logger.error("updateWeekendMasterStatus failed", e);
		}
	return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getListOfWeekendMaster(WeekendMasterDto weekendMasterDto) throws ApplicationException {
		try {
			return iWeekendMasterDao.getListOfWeekendMaster(weekendMasterDto);

		} catch (Exception e) {
		logger.error("getListOfWeekendMaster failed", e);
		}
	return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getWeekendMasterCount(WeekendMasterDto weekendMasterDto) throws ApplicationException {
		try {
			return iWeekendMasterDao.getWeekendMasterCount(weekendMasterDto);

		} catch (Exception e) {
		logger.error("getWeekendMasterCount failed", e);
		}
	return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	

	@Override
	public Response getWeekendMasterByName(WeekendMasterDto weekendMasterDto) throws ApplicationException {
		try {
			return iWeekendMasterDao.getWeekendMasterByName(weekendMasterDto);

		} catch (Exception e) {
		logger.error("getWeekendMasterByName failed", e);
		}
	return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getWeekendMasterByNameNotId(WeekendMasterDto weekendMasterDto) throws ApplicationException {
		try {
			return iWeekendMasterDao.getWeekendMasterByNameNotId(weekendMasterDto);
	
		} catch (Exception e) {
		logger.error("getWeekendMasterByNameNotId failed", e);
		}
	return new Response(ERROR, ERROR_CODE, null, null, null);
	}
	@Override
	public Response getWeekendMasterById(Integer weekendId, Integer organizationId, Integer unitId)throws ApplicationException {
			
		try {
			return iWeekendMasterDao.getWeekendMasterById(weekendId, organizationId, unitId);

	} catch (Exception e) {
	logger.error("getWeekendMasterById failed", e);
	}
return new Response(ERROR, ERROR_CODE, null, null, null);
}
	

}
