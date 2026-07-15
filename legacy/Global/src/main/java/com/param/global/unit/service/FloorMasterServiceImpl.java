package com.param.global.unit.service;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.unit.dao.IFloorMasterDao;
import com.param.global.unit.dto.FloorMasterDto;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class FloorMasterServiceImpl implements IFloorMasterService,ICommonConstants
{
	private static final Logger logger = Logger.getLogger(FloorMasterServiceImpl.class);

	@Autowired
	IFloorMasterDao iFloorDao;
	
	@Override
	public Response addFloorMaster(FloorMasterDto floorMasterDto) throws ApplicationException {
		try {

			Response deptResponse = iFloorDao.getFloorByName(floorMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*floorMasterDto
						.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				floorMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				*/
				iFloorDao.addFloorMaster(floorMasterDto);
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
			logger.error("addFloorMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getFloorMasterList(FloorMasterDto floorMasterDto) throws ApplicationException {
		try {
			return iFloorDao.getFloorMasterList(floorMasterDto);
		} catch (Exception e) {
			logger.error("getFloorMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateFloorMaster(FloorMasterDto floorMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iFloorDao.getFloorByNameNotId(floorMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*floorMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				iFloorDao.updateFloorMaster(floorMasterDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
			logger.error("updateFloorMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getFloorById(FloorMasterDto floorMasterDto) throws ApplicationException {
		try {
			return iFloorDao.getFloorByID(floorMasterDto);
		} catch (Exception e) {
			logger.error("getFloorById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForFloor(FloorMasterDto floorMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iFloorDao.getFloorByID(floorMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				iFloorDao.updateFloorStatus(floorMasterDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}
		} catch (Exception e) {
			logger.error("updateStatusForFloor failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveFloorList() throws ApplicationException {
		try {
			return iFloorDao.getActiveFloorList();
		} catch (Exception e) {
			logger.error("getActiveFloorList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getFloorCount(FloorMasterDto floorMasterDto) throws ApplicationException {
		try {
			return iFloorDao.getCount(floorMasterDto);
		} catch (Exception e) {
			logger.error("getFloorCount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


}
