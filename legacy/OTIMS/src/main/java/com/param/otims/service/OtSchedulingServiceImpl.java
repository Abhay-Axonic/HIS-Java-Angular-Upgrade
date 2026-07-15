package com.param.otims.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dao.IProcedureOTScheduleDao;
import com.param.global.otims.dto.OtSchedulingDto;
import com.param.global.otims.dto.OtSchedulingTimeDetailsDto;
import com.param.otims.dao.IOtSchedulingDao;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class OtSchedulingServiceImpl  implements IOtSchedulingService ,ICommonConstants {

	private static final Logger logger = Logger.getLogger(OtSchedulingServiceImpl.class);

	@Autowired
	IOtSchedulingDao iOtSchedulingDao;
	
	@Autowired
	IProcedureOTScheduleDao iProcedureOTScheduleDao;
	
	@Override
	public Response saveOtScheduling(OtSchedulingDto otSchedulingDto) throws ApplicationException {
		try {
			List<OtSchedulingDto> list = new ArrayList<>();
			for(Integer tableId :otSchedulingDto.getTableList()) {
				
				for(OtSchedulingTimeDetailsDto schedDetails : otSchedulingDto.getTimeDetails()) {
					
					Map<String,Integer> DateList= GlobalCommonDateUtils.getMonthValue(schedDetails.getFromDate(),schedDetails.getToDate(),schedDetails.getDays());
					if(!DateList.isEmpty()) {
						for (Entry<String, Integer> a : DateList.entrySet()) {
							
							OtSchedulingDto dto = new OtSchedulingDto();
							dto.setCreatedBy(otSchedulingDto.getCreatedBy());
							dto.setCreatedDate(otSchedulingDto.getCreatedDate());
							dto.setDayId(a.getValue());
							dto.setOtDate(a.getKey());
							dto.setFromTime(schedDetails.getFromTime());
							dto.setOrgId(otSchedulingDto.getOrgId());
							dto.setStatus(otSchedulingDto.getStatus());
							dto.setSwitchOverTime(schedDetails.getSwitchOverTime());
							dto.setTableId(tableId);
							dto.setTheatreId(otSchedulingDto.getTheatreId());
							dto.setToTime(schedDetails.getToTime());
							dto.setUnitId(otSchedulingDto.getUnitId());
							dto.setUpdatedBy(otSchedulingDto.getUpdatedBy());
							dto.setUpdatedDate(otSchedulingDto.getUpdatedDate());
							
							Response res = iOtSchedulingDao.checkOtScheduling(dto);
							
							if(res.getListObject()!=null && res.getListObject().size()>0) {
								return new Response<>(ERROR, ERROR_CODE, "Slot Already Exist", null, null);
							}
							list.add(dto);
						}
					
					}else {
						return new Response<>(ERROR, ERROR_CODE, "Selected Day Not Present in From and ToDate", null, null);

					}
				}
			}
			
			return	iOtSchedulingDao.saveOtScheduling(list);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getOtScheduling(OtSchedulingDto otSchedulingDto) throws ApplicationException {
	try {
			return iOtSchedulingDao.getOtScheduling(otSchedulingDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response deactivateOtSchedule(OtSchedulingDto otSchedulingDto) throws ApplicationException {
		try {
			return iOtSchedulingDao.deactivateOtSchedule(otSchedulingDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response toCheckSlotAvailability(OtSchedulingDto otSchedulingDto) throws ApplicationException {
		try {
			Response res =  iOtSchedulingDao.toCheckSlotAvailability(otSchedulingDto);
			
			if(res.getListObject() != null && res.getListObject().size()>0) {
				
				Response reolcvd = iProcedureOTScheduleDao.checkUsedSlot(otSchedulingDto);
				
					if(reolcvd.getListObject()!=null && reolcvd.getListObject().size()>0) {
						return new Response(ERROR, ERROR_CODE, "OT slot's are not available", null,null);
					}
					
						return new Response(SUCCESS, SUCCESS_CODE, null, res.getListObject(),null);
						
			}else {

				return new Response(ERROR, ERROR_CODE, "OT slot's are not available", null,null);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getOtSchedulingForOTcalender(OtSchedulingDto otSchedulingDto) throws ApplicationException {
	try {
			return iOtSchedulingDao.getOtSchedulingForOTcalender(otSchedulingDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
