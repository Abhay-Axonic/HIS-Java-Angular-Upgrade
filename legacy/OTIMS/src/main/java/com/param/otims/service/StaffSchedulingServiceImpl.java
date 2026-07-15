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
import com.param.global.otims.dao.IProcedureStaffScheduleDao;
import com.param.global.otims.dto.OtSchedulingTimeDetailsDto;
import com.param.global.otims.dto.StaffSchedulingDto;
import com.param.otims.dao.IStaffSchedulingDao;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class StaffSchedulingServiceImpl  implements IStaffSchedulingService ,ICommonConstants {

	private static final Logger logger = Logger.getLogger(StaffSchedulingServiceImpl.class);

	@Autowired
	IStaffSchedulingDao iStaffSchedulingDao;
	
	@Autowired
	IProcedureStaffScheduleDao iProcedureStaffScheduleDao;
	
	@Override
	public Response saveStaffScheduling(StaffSchedulingDto staffSchedulingDto) throws ApplicationException {
		try {
			List<StaffSchedulingDto> list = new ArrayList<>();
			for(Integer userId :staffSchedulingDto.getUserList()) {
				
				for(OtSchedulingTimeDetailsDto schedDetails : staffSchedulingDto.getTimeDetails()) {
					
					Map<String,Integer> DateList= GlobalCommonDateUtils.getMonthValue(schedDetails.getFromDate(),schedDetails.getToDate(),schedDetails.getDays());
					if(!DateList.isEmpty()) {
						for (Entry<String, Integer> a : DateList.entrySet()) {
							
							StaffSchedulingDto dto = new StaffSchedulingDto();
							dto.setCreatedBy(staffSchedulingDto.getCreatedBy());
							dto.setCreatedDate(staffSchedulingDto.getCreatedDate());
							dto.setDayId(a.getValue());
							dto.setRoleId(staffSchedulingDto.getRoleId());
							dto.setUserId(userId);
							dto.setStaffSchedulingDate(a.getKey());
							dto.setFromTime(schedDetails.getFromTime());
							dto.setOrgId(staffSchedulingDto.getOrgId());
							dto.setStatus(staffSchedulingDto.getStatus());
							dto.setSwitchOverTime(schedDetails.getSwitchOverTime());
							dto.setToTime(schedDetails.getToTime());
							dto.setUnitId(staffSchedulingDto.getUnitId());
							dto.setUpdatedBy(staffSchedulingDto.getUpdatedBy());
							dto.setUpdatedDate(staffSchedulingDto.getUpdatedDate());
							
							Response res = iStaffSchedulingDao.checkStaffScheduling(dto);
							
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
			
			return	iStaffSchedulingDao.saveStaffScheduling(list);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getStaffScheduling(StaffSchedulingDto staffSchedulingDto) throws ApplicationException {
	try {
			return iStaffSchedulingDao.getStaffScheduling(staffSchedulingDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response deactivateStaffSchedule(StaffSchedulingDto staffSchedulingDto) throws ApplicationException {
		try {
			return iStaffSchedulingDao.deactivateStaffSchedule(staffSchedulingDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getStaffListByRoleId(StaffSchedulingDto staffSchedulingDto) throws ApplicationException {
		try {
			return iStaffSchedulingDao.getStaffListByRoleId(staffSchedulingDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response checkStaffAvailableOrNot(StaffSchedulingDto staffSchedulingDto) throws ApplicationException {
		try {
			
			Response res =  iStaffSchedulingDao.checkStaffAvailableOrNot(staffSchedulingDto);
			if(res.getListObject()!=null && res.getListObject().size()>0) {
				Response reolcvd = iProcedureStaffScheduleDao.checkStaffUsedSlot(staffSchedulingDto);
				
				if(reolcvd.getListObject()!=null && reolcvd.getListObject().size()>0) {
					return new Response(ERROR, ERROR_CODE, "OT slot's are not available", null,null);
				}
				
					return new Response(SUCCESS, SUCCESS_CODE, null, res.getListObject(),null);
			}else {
				return new Response(ERROR, ERROR_CODE, "staff slot's are not available", null,null);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getOTIMSStaffList(StaffSchedulingDto staffSchedulingDto) throws ApplicationException {
		try {
				//Response res = iStaffSchedulingDao.getOtimsRoles(staffSchedulingDto);
			//	List<StaffSchedulingDto> list = new ArrayList<>();
				List<StaffSchedulingDto> finalStafflist = new ArrayList<>();
				//if(res.getListObject().size()>0) {
				//	list  = res.getListObject();
					for (int i=8;i<=12;i++) {
						StaffSchedulingDto finalObj = new StaffSchedulingDto();
						staffSchedulingDto.setRoleId(i);
						Response staffRes =  iStaffSchedulingDao.getStaffListByRoleTypeId(staffSchedulingDto);
						//finalObj.setRoleId(i);
						
						if(staffRes.getListObject().size()>0) {
							finalObj.setStaffList(staffRes.getListObject());
							finalObj.setRoleId(finalObj.getStaffList().get(0).getRoleId());
							finalStafflist.add(finalObj);
						}
					}
					
				//}
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, finalStafflist, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response checkStaffListAvailableOrNot(List<StaffSchedulingDto> staffSchedulingDto)
			throws ApplicationException {
		try {
			List<StaffSchedulingDto> staffSchedulingList  =  new ArrayList<StaffSchedulingDto>();
			for (StaffSchedulingDto staffSchedulingDto2 : staffSchedulingDto) {
				StaffSchedulingDto dto = new StaffSchedulingDto(); 
				Response res =  iStaffSchedulingDao.checkStaffAvailableOrNot(staffSchedulingDto2);
				if(res.getListObject()!=null && res.getListObject().size()>0) {
					Response reolcvd = iProcedureStaffScheduleDao.checkStaffUsedSlot(staffSchedulingDto2);
					
					if(reolcvd.getListObject()!=null && reolcvd.getListObject().size()>0) {
						dto.setUserId(staffSchedulingDto2.getUserId());
						dto.setRoleId(staffSchedulingDto2.getRoleId());
						dto.setColor("red");
						staffSchedulingList.add(dto);
					}else {
						dto.setUserId(staffSchedulingDto2.getUserId());
						dto.setRoleId(staffSchedulingDto2.getRoleId());
						dto.setColor("green");
						staffSchedulingList.add(dto);
					}
					
				}else {
					dto.setUserId(staffSchedulingDto2.getUserId());
					dto.setRoleId(staffSchedulingDto2.getRoleId());
					dto.setColor("red");
					staffSchedulingList.add(dto);
				}
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, staffSchedulingList, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
