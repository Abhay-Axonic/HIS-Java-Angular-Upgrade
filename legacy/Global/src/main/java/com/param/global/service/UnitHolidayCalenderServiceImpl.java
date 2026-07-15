package com.param.global.service;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IUnitHolidayCalenderDao;
import com.param.global.dto.UnitHolidayCalenderMapperDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UnitHolidayCalenderServiceImpl implements IUnitHolidayCalenerService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(UnitHolidayCalenderServiceImpl.class);

	@Autowired
	IUnitHolidayCalenderDao iUnitHolidayCalenderDao;

	@Override
	public Response saveUnitHolidayCalener(UnitHolidayCalenderMapperDto unitHolidayCalenderMapperDto)
			throws ApplicationException {
//		Response resTrunc = iUnitHolidayCalenderDao.truncateUnitHolidayCalener(unitHolidayCalenderMapperDto);
//		if (resTrunc.getStatus() == SUCCESS) {
			Iterator<UnitHolidayCalenderMapperDto> itr = unitHolidayCalenderMapperDto
					.getUnitHolidayCalenderMapperDtosList().iterator();
			while (itr.hasNext()) {
				UnitHolidayCalenderMapperDto UnitHolidayCalenderMapperDto = new UnitHolidayCalenderMapperDto();

				UnitHolidayCalenderMapperDto obj = itr.next();

				UnitHolidayCalenderMapperDto.setUnitId(unitHolidayCalenderMapperDto.getUnitId());
				UnitHolidayCalenderMapperDto.setOrganizationId(unitHolidayCalenderMapperDto.getOrganizationId());
				UnitHolidayCalenderMapperDto.setHolidayId(obj.getHolidayId());			
				UnitHolidayCalenderMapperDto.setStatus('A');
				UnitHolidayCalenderMapperDto.setCreatedBy(unitHolidayCalenderMapperDto.getCreatedBy());
				UnitHolidayCalenderMapperDto.setCreatedDate(unitHolidayCalenderMapperDto.getCreatedDate());
				UnitHolidayCalenderMapperDto.setUpdatedBy(unitHolidayCalenderMapperDto.getUpdatedBy());
				UnitHolidayCalenderMapperDto.setUpdatedDate(unitHolidayCalenderMapperDto.getUpdatedDate());

				iUnitHolidayCalenderDao.saveUnitHolidayCalener(UnitHolidayCalenderMapperDto);

			}
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
//		} else {
//			return new Response(ERROR, null, null, null, null);
//		}

	}

	@Override
	public Response getUnitHolidayCalenerList(UnitHolidayCalenderMapperDto unitHolidayCalenderMapperDto)
			throws ApplicationException {
		try {

			return iUnitHolidayCalenderDao.getUnitHolidayCalenerList(unitHolidayCalenderMapperDto);

		} catch (Exception e) {
		logger.error("getUnitHolidayCalenerList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response updateStatusForUnitHolidayCalener(UnitHolidayCalenderMapperDto unitHolidayCalenderMapperDto)
			throws ApplicationException {
		try {
			return	iUnitHolidayCalenderDao.updateStatusForUnitHolidayCalener(unitHolidayCalenderMapperDto);
			/*Response deptResponse = iUnitHolidayCalenderDao.getUnitHolidayCalenerById(unitHolidayCalenderMapperDto);
			if (deptResponse.getStatus().equals(SUCCESS)) {

			 	iUnitHolidayCalenderDao.updateStatusForUnitHolidayCalener(unitHolidayCalenderMapperDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, "ID does not exist", null, null);
			}*/
		} catch (Exception e) {
		logger.error("updateStatusForUnitHolidayCalener failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getUnitHolidayCalenerCount(UnitHolidayCalenderMapperDto unitHolidayCalenderMapperDto)
			throws ApplicationException {
		try {
			return iUnitHolidayCalenderDao.getCount(unitHolidayCalenderMapperDto);
		} catch (Exception e) {
		logger.error("getUnitHolidayCalenerCount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveUnitHolidayCalenerList(UnitHolidayCalenderMapperDto unitHolidayCalenderMapperDto)
			throws ApplicationException {
		try {
			return iUnitHolidayCalenderDao.getActiveUnitHolidayCalenerList(unitHolidayCalenderMapperDto);
		} catch (Exception e) {
		logger.error("getActiveUnitHolidayCalenerList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getHolidayCalenderListNotMappedToUnit(UnitHolidayCalenderMapperDto unitHolidayCalenderMapperDto)
			throws ApplicationException {
		try {
			return iUnitHolidayCalenderDao.getHolidayCalenderListNotMappedToUnit(unitHolidayCalenderMapperDto);
		} catch (Exception e) {
		logger.error("getHolidayCalenderListNotMappedToUnit failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

}
