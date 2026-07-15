package com.param.otims.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.mail.IMailingService;
import com.param.global.otims.dao.IProcedureEquipmentScheduleDao;
import com.param.global.otims.dao.IProcedureOTScheduleDao;
import com.param.global.otims.dao.IProcedureStaffScheduleDao;
import com.param.global.otims.dto.ChecklistMasterDto;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.OtSchedulingDto;
import com.param.global.otims.dto.ProcedureEquipmentScheduleDto;
import com.param.global.otims.dto.ProcedureOTScheduleDto;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.global.otims.dto.ProcedureSchedulingDto;
import com.param.global.otims.dto.ProcedureStaffScheduleDto;
import com.param.global.otims.dto.StaffSchedulingDto;
import com.param.otims.dao.IOTIMSDashboardDao;
import com.param.otims.dao.IOtSchedulingDao;
import com.param.otims.dao.IStaffSchedulingDao;
import com.param.otims.model.OTIMSDashboard;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes","unchecked" })
public class OTIMSDashboardServiceImpl implements IOTIMSDashboardService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(OTIMSDashboardServiceImpl.class);

	@Autowired
	IProcedureOTScheduleDao iProcedureOTScheduleDao;

	@Autowired
	IProcedureStaffScheduleDao iProcedureStaffScheduleDao;
	
	@Autowired
	IProcedureEquipmentScheduleDao iProcedureEquipmentScheduleDao;
	
	@Autowired
	IOTIMSDashboardDao iOTIMSDashboardDao;
	
	@Autowired 
	IMailingService iMailingService;
	
	@Autowired
	IOtSchedulingDao iOtSchedulingDao;
	
	@Autowired
	IStaffSchedulingDao iStaffSchedulingDao;
	
	
	@Override
	@Transactional
	public Response saveProcedureSchedulingDetails(ProcedureSchedulingDto procedureSchedulingDto, int otreservation) throws ApplicationException {
		try {
			Response  res = null;
			Response ressave = iOTIMSDashboardDao.saveProcedureSchedule(procedureSchedulingDto);
			if(ressave.getObject()!=null) {
				OTIMSDashboard dto = (OTIMSDashboard) ressave.getObject();
				dto.setProReqId(procedureSchedulingDto.getProReqId());
				if(procedureSchedulingDto.getProcedureOTScheduleList().size()>0) {
					for (ProcedureOTScheduleDto procedureOTScheduleDto : procedureSchedulingDto.getProcedureOTScheduleList()) {
						procedureOTScheduleDto.setProReqId(procedureSchedulingDto.getProReqId());
						procedureOTScheduleDto.setProcedureScheduleId(dto.getProcedureScheduleId());
						OtSchedulingDto switchovertimeDto = new OtSchedulingDto();
						switchovertimeDto.setOrgId(procedureOTScheduleDto.getOrganizationId());
						switchovertimeDto.setUnitId(procedureOTScheduleDto.getUnitId());
						switchovertimeDto.setTheatreId(procedureOTScheduleDto.getTheatreId());
						switchovertimeDto.setTableId(procedureOTScheduleDto.getTableId());
						switchovertimeDto.setOtDate(GlobalCommonDateUtils.getStringOnlyDate(procedureOTScheduleDto.getProdeureDate()));
						switchovertimeDto.setFromTime(procedureOTScheduleDto.getFromTime());
						switchovertimeDto.setToTime(procedureOTScheduleDto.getToTime());
						List<OtSchedulingDto> switchovertimelist = new ArrayList<>();
						Response switchovertime =  iOtSchedulingDao.toCheckSlotAvailability(switchovertimeDto);
						if(switchovertime.getListObject().size()>0) {
							switchovertimelist.addAll(switchovertime.getListObject()); 
							 String myTime = procedureOTScheduleDto.getToTime();
							 SimpleDateFormat df = new SimpleDateFormat("HH:mm");
							 Date d = df.parse(myTime); 
							 Calendar cal = Calendar.getInstance();
							 cal.setTime(d);
							 cal.add(Calendar.MINUTE, switchovertimelist.get(0).getSwitchOverTime());
							 String newTime = df.format(cal.getTime());
							 //System.out.println(newTime);
							// procedureOTScheduleDto.setToTime(newTime);
							 res = iProcedureOTScheduleDao.saveProcedureOTSchedule(procedureOTScheduleDto);
						}
					}
				}
				
				if(procedureSchedulingDto.getProcedureStaffScheduleList().size()>0) {
					for (ProcedureStaffScheduleDto procedureStaffScheduleDto : procedureSchedulingDto.getProcedureStaffScheduleList()) {
						procedureStaffScheduleDto.setProReqId(procedureSchedulingDto.getProReqId());
						procedureStaffScheduleDto.setProcedureScheduleId(dto.getProcedureScheduleId());
						
						List<StaffSchedulingDto> switchoverStaffList = new  ArrayList<>();
						
						StaffSchedulingDto  switchovertimeDto = new StaffSchedulingDto();
						switchovertimeDto.setOrgId(procedureStaffScheduleDto.getOrganizationId());
						switchovertimeDto.setUnitId(procedureStaffScheduleDto.getUnitId());
						switchovertimeDto.setStaffSchedulingDate(GlobalCommonDateUtils.getStringOnlyDate(procedureStaffScheduleDto.getProdeureDate()));
						switchovertimeDto.setFromTime(procedureStaffScheduleDto.getFromTime());
						switchovertimeDto.setToTime(procedureStaffScheduleDto.getToTime());
						switchovertimeDto.setRoleId(procedureStaffScheduleDto.getRoleId());
						switchovertimeDto.setUserId(procedureStaffScheduleDto.getUserId());
						Response switchovertimestaff =  iStaffSchedulingDao.checkStaffAvailableOrNot(switchovertimeDto);
						
						if(switchovertimestaff.getListObject().size()>0) {
							switchoverStaffList.addAll(switchovertimestaff.getListObject()); 
							 String myTime = procedureStaffScheduleDto.getToTime();
							 SimpleDateFormat df = new SimpleDateFormat("HH:mm");
							 Date d = df.parse(myTime); 
							 Calendar cal = Calendar.getInstance();
							 cal.setTime(d);
							 cal.add(Calendar.MINUTE, switchoverStaffList.get(0).getSwitchOverTime());
							 String newTime = df.format(cal.getTime());
							 //System.out.println(newTime);
							// procedureStaffScheduleDto.setToTime(newTime);
							 res = iProcedureStaffScheduleDao.saveProcedureStaffSchedule(procedureStaffScheduleDto);
						}else {
							 res = iProcedureStaffScheduleDao.saveProcedureStaffSchedule(procedureStaffScheduleDto);
						}
					}
				}
				
				if(procedureSchedulingDto.getProcedureEquipmentScheduleList().size()>0) {
					for (ProcedureEquipmentScheduleDto procedureEquipmentScheduleDto : procedureSchedulingDto.getProcedureEquipmentScheduleList()) {
						procedureEquipmentScheduleDto.setProReqId(procedureSchedulingDto.getProReqId());
						procedureEquipmentScheduleDto.setProcedureScheduleId(dto.getProcedureScheduleId());
						  res = iProcedureEquipmentScheduleDao.saveProcedureEquipmentSchedule(procedureEquipmentScheduleDto);
					}
				}
				
				if(procedureSchedulingDto.getProcedureStaffScheduleList().size()>0) {
					for (ProcedureStaffScheduleDto procedureStaffScheduleDto : procedureSchedulingDto.getProcedureStaffScheduleList()) {
						Response roleres = iProcedureStaffScheduleDao.getRoleTypeByRoleId(procedureStaffScheduleDto);
						
						if(roleres.getObject()!=null){
							ProcedureStaffScheduleDto roleTypeDto = (ProcedureStaffScheduleDto)roleres.getObject();
							procedureStaffScheduleDto.setRoleTypeId(roleTypeDto.getRoleTypeId());
							iMailingService.sendSchedulingNotification(procedureStaffScheduleDto,procedureSchedulingDto);
						}
					}
				}
				
				if(otreservation == 0) {
					if(res.getStatus().equals(SUCCESS)) {
						// need to pass changed of date and time
						iProcedureOTScheduleDao.updateProcedureReqStatus(procedureSchedulingDto.getProReqId(), 3, procedureSchedulingDto.getChangeProcedureDate() ,procedureSchedulingDto.getChangeTime());
						return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, ressave);
					}
				}else {
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dto);
					
				}
				
			}else {
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	@Transactional
	public Response getProcedureSchedulingDetails(ProcedureSchedulingDto procedureSchedulingDto)
			throws ApplicationException {
		try {
			ProcedureSchedulingDto newDto = new ProcedureSchedulingDto();
			Response  otResponse = null;
			Response  staffResponse = null;
			Response  equipmentResponse = null;
			
			Response resSchedule = iOTIMSDashboardDao.getProcedureSchedule(procedureSchedulingDto);
			
			if(resSchedule.getObject()!=null) {
				OTIMSDashboard dto = new OTIMSDashboard();
				dto = (OTIMSDashboard) resSchedule.getObject();
				newDto.setTotalDuration(dto.getTotalDuration());
			}
			
			otResponse = iProcedureOTScheduleDao.getProcedureOTScheduleById(procedureSchedulingDto);
			if(otResponse.getListObject()!=null && otResponse.getListObject().size()>0) {
				newDto.setProcedureOTScheduleList(otResponse.getListObject());
			}
			
			staffResponse = iProcedureStaffScheduleDao.getProcedureStaffScheduleById(procedureSchedulingDto);
			
			if(staffResponse.getListObject()!=null && staffResponse.getListObject().size()>0) {
				newDto.setProcedureStaffScheduleList(staffResponse.getListObject());
			}
			
			equipmentResponse = iProcedureEquipmentScheduleDao.getProcedureEquipmentScheduleById(procedureSchedulingDto);
			
			if(equipmentResponse.getListObject()!=null && equipmentResponse.getListObject().size()>0) {
				newDto.setProcedureEquipmentScheduleList(equipmentResponse.getListObject());
			}
			
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, newDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	@Transactional
	public Response rescheduleProcedure(ProcedureSchedulingDto procedureSchedulingDto) throws ApplicationException {
		try {

			Response res = iProcedureOTScheduleDao.updateProcedureReqStatus(procedureSchedulingDto.getProReqId(),1, procedureSchedulingDto.getChangeProcedureDate() ,procedureSchedulingDto.getChangeTime());

			if(res.getObject()!=null) {
				iOTIMSDashboardDao.inActivePlannedProcedure(procedureSchedulingDto.getProReqId());
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}


		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@SuppressWarnings("unused")
	@Override
	@Transactional
	public Response getSurgeriesOfTheDayList(ProcedureSchedulingDto procedureSchedulingDto)
			throws ApplicationException {
		try {
			
			Response res =  iOTIMSDashboardDao.getSurgeriesOfTheDayList(procedureSchedulingDto);
			List<ProcedureSchedulingDto>  list =new  ArrayList<>();
			if(res.getListObject() != null  && res.getListObject().size()>0) {
				list.addAll(res.getListObject());
				for (ProcedureSchedulingDto procedureSchedulingDto2 : list) {
					ProcedureStaffScheduleDto dto = new ProcedureStaffScheduleDto();
					dto.setProcedureScheduleId(procedureSchedulingDto2.getProcedureScheduleId());
					List<ProcedureStaffScheduleDto> staffNames = new ArrayList<>();
					Response res1 = iOTIMSDashboardDao.getProcedureSchedulingSurgeonList(dto);
					if(res1.getListObject() != null  && res1.getListObject().size()>0) {
						staffNames.addAll(res1.getListObject());
						String staffName= "";
						for (ProcedureStaffScheduleDto staff : staffNames) {
							int i =0 ;
							if(i==0) {
								staffName = staff.getStaffName();
								i++;
							}else {
								staffName = staffName+", "+staff.getStaffName(); 
							}
						}
						procedureSchedulingDto2.setSurgeonName(staffName);
					}
					
				}
				
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			}


		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getProcedureSchedulingSurgeonList(ProcedureStaffScheduleDto dto) throws ApplicationException {
		try {

			 return iOTIMSDashboardDao.getProcedureSchedulingSurgeonList(dto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response cancelProcedureSchedulingDetails(OTReservationDto oTReservationdto) throws ApplicationException {
		try {
			Response  res = null;
			 res = iOTIMSDashboardDao.cancelProcedureSchedule(oTReservationdto);
			 res = iProcedureOTScheduleDao.saveProcedureOTSchedule(oTReservationdto);
			res = iProcedureStaffScheduleDao.saveProcedureStaffSchedule(oTReservationdto);
			res = iProcedureEquipmentScheduleDao.saveProcedureEquipmentSchedule(oTReservationdto);
			
			Response  staff = iProcedureStaffScheduleDao.getotimsRoleByProReqId(oTReservationdto);
			
			Response  otres = iOTIMSDashboardDao.getOtDeatils(oTReservationdto);
			
			List<ProcedureStaffScheduleDto> list = new ArrayList<>();
			if(staff.getListObject().size()>0) {
				list.addAll(staff.getListObject());
				
				if(otres.getObject()!=null) {
					OTReservationDto dto = (OTReservationDto)otres.getObject();
					for (ProcedureStaffScheduleDto procedureStaffScheduleDto : list) {
						
						iMailingService.sendSchedulingConcelledNotification(procedureStaffScheduleDto,dto);
					}
				}
				
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, res);


		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public Response getSchedulingByProReqId(ProcedureSchedulingDto dto) throws ApplicationException {
		try {

			 return iOTIMSDashboardDao.getSchedulingByProReqId(dto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getOtTableByScheduleId(ProcedureSchedulingDto dto) throws ApplicationException {
		try {

			 return iProcedureOTScheduleDao.getProcedureOTScheduleById(dto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getOtCheckList(ProcedureSchedulingDto dto) throws ApplicationException {
		try {
			 return iProcedureOTScheduleDao.getOtCheckList(dto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPreOparetiveDeta(ProcedureSchedulingDto dto) throws ApplicationException {
		try {
			ProcedureOrderMasterDto Obj = new ProcedureOrderMasterDto();
			Response res =  iProcedureOTScheduleDao.getPreOparetiveDeta(dto);
			List<ProcedureOrderMasterDto>  orderMasterList =  new ArrayList<>(); 
			if(res.getListObject() != null && res.getListObject().size() >0) {
				orderMasterList.addAll(res.getListObject());
				String reason =  ""; 
				int i = 0;
				for (ProcedureOrderMasterDto procedureOrderMasterDto : orderMasterList) {
					if(procedureOrderMasterDto.getIsBloodComponentRequired() == 'Y') {
						Obj.setIsBloodComponentRequired(procedureOrderMasterDto.getIsBloodComponentRequired());
					}
					if(procedureOrderMasterDto.getIsIcuRequired() == 'Y') {
						Obj.setIsIcuRequired(procedureOrderMasterDto.getIsIcuRequired());
					}
					if(procedureOrderMasterDto.getIsPacRequired() == 'Y') {
						Obj.setIsPacRequired(procedureOrderMasterDto.getIsPacRequired());
					}
					if(i == 0) {
						if(procedureOrderMasterDto.getRequestReason() != null) {
							reason = procedureOrderMasterDto.getRequestReason();
							i++;
						}
						
					}else {
						if(procedureOrderMasterDto.getRequestReason() != null) {
							reason = ","+procedureOrderMasterDto.getRequestReason();
							i++;
						}
					}
					
				}
				if(reason != "")
				Obj.setRequestReason(reason);
				
				Response checkListResp  =   iProcedureOTScheduleDao.getPreOparetiveCheckListDeta(orderMasterList.get(0).getProReqId());
				List<ChecklistMasterDto> checkList =  new ArrayList<>();
				if(checkListResp.getListObject() != null && checkListResp.getListObject().size() >0) {
					checkList.addAll(checkListResp.getListObject());
				}
				
				Response wheelInTimeSheetRes  =   iProcedureOTScheduleDao.getWheelInDataByProReqId(orderMasterList.get(0).getProReqId());
				if(wheelInTimeSheetRes.getStatus().equals(SUCCESS) && wheelInTimeSheetRes.getObject()!=null) {
					ProcedureOrderMasterDto wheelInTimeSheetObj = (ProcedureOrderMasterDto)wheelInTimeSheetRes.getObject();
					Obj.setOtCompletionTimeSheetId(wheelInTimeSheetObj.getOtCompletionTimeSheetId());
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, null, checkList, Obj);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
