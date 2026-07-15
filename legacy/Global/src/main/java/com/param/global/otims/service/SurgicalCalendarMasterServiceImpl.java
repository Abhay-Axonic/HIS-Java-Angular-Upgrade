package com.param.global.otims.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IUnitHolidayCalenderDao;
import com.param.global.dto.DayMasterDto;
import com.param.global.dto.HolidayCalenderMasterDto;
import com.param.global.dto.TimeSlotsDto;
import com.param.global.dto.UnitHolidayCalenderMapperDto;
import com.param.global.dto.WeekendMasterDto;
import com.param.global.otims.dao.ISurgicalCalendarMasterDao;
import com.param.global.otims.dto.SurgicalCalendarDetailsDto;
import com.param.global.otims.dto.SurgicalCalendarMasterDto;
import com.param.global.otims.dto.SurgicalCalendarReqDto;
import com.param.global.otims.dto.SurgicalCalenderMapperDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes", "unchecked"})

public class SurgicalCalendarMasterServiceImpl implements ISurgicalCalendarMasterService, ICommonConstants, IError{

	private static final Logger logger = Logger.getLogger(SurgicalCalendarMasterServiceImpl.class);

	@Autowired
	ISurgicalCalendarMasterDao iSurgicalCalendarMasterDao;
	
	@Autowired
	IUnitHolidayCalenderDao iUnitHolidayCalenderDao;
	
	@Override
	public Response saveSurgicalCalendarMaster(SurgicalCalendarMasterDto dto)
			throws ApplicationException {
		try
		{
			Date date = TimeZoneComponent.getDateByzone(dto.getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "yyyy-M-dd");
			List<SurgicalCalendarDetailsDto> listForSaveCall = new LinkedList<>();
			
			
		for (Iterator iterator = dto.getSurgicalCalendarDetailsDtoList().iterator(); iterator.hasNext();) 
		{
			List<List<DayMasterDto>> datesMasterList = new ArrayList<>();
			SurgicalCalendarDetailsDto detailsDto = (SurgicalCalendarDetailsDto) iterator.next();
			
			datesMasterList =   this.datesAsPerDayBetweenTwoDatesForSurgicalCalender(dto.getFromDate(), dto.getToDate(), Arrays.asList(detailsDto.getDayId()), dto.getOrganisationId(), dto.getUnitId());
			for (List<DayMasterDto> list : datesMasterList) 
			{
			   for (DayMasterDto dayMasterDto : list)
			   {
				   List<String> timeSlotsList = null;
				   List<TimeSlotsDto> timeSlotsDtos = new LinkedList<>();
				   timeSlotsList = this.getTimeSlot(detailsDto.getFromTime().toString(),
						   detailsDto.getToTime(),30);
					
					if((timeSlotsList.isEmpty())){ 
						 return new Response(ERROR, null, "Please enter accurate slot time !!", null, null); 
					}
					
					TimeSlotsDto timeSlotsDto = null;
					for (int k = 0; k < (timeSlotsList.size() - 1); k++) 
					{
						timeSlotsDto = new TimeSlotsDto();
							timeSlotsDto.setFromTime(timeSlotsList.get(k));
							timeSlotsDto.setToTime(timeSlotsList.get(k + 1));
							timeSlotsDtos.add(timeSlotsDto);
					}
					
					for (TimeSlotsDto tmSlot : timeSlotsDtos) {

						LocalTime fromTime1 = LocalTime.parse(tmSlot.getFromTime());
						LocalDate today = LocalDate.now();
						Instant fromTime = fromTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

						LocalTime toTime1 = LocalTime.parse(tmSlot.getToTime());
						Instant toTime = toTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

						SurgicalCalendarDetailsDto scDto  = new SurgicalCalendarDetailsDto();
						scDto.setOrganisationId(detailsDto.getOrganisationId());
						scDto.setUnitId(dto.getUnitId());
						scDto.setDoctorId(dto.getDoctorId());
						scDto.setSpecialityId(dto.getSpecialityId());
						scDto.setDayId(dayMasterDto.getDayId());
						scDto.setProcedureDate(dayMasterDto.getDay());
						scDto.setFromDate(dto.getFromDate());
						scDto.setToDate(dto.getToDate());
						scDto.setFromTime(tmSlot.getFromTime());
						scDto.setToTime(tmSlot.getToTime());
						scDto.setFromTimeToSave(fromTime);
						scDto.setToTimeToSave(toTime);
						scDto.setCreatedBy(dto.getCreatedBy());
						scDto.setUpdatedBy(dto.getUpdatedBy());
						scDto.setCreatedDate(dateTime);
						scDto.setUpdatedDate(dateTime);
						scDto.setStatus('A');
						scDto.setIsDeleted('N');
						scDto.setCalenderStatusId(1);
						listForSaveCall.add(scDto);
					}
					
				
			   }	
			}
			
			
		}
			
		   iSurgicalCalendarMasterDao.saveSurgicalCalendarMaster(listForSaveCall);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listForSaveCall, null);
		} catch (Exception e)
		{
			logger.error("saveSurgicalCalendarMaster failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	




	@Override
	public Response getWeelkyCalenderSlots(SurgicalCalendarReqDto dto) throws ApplicationException {
		try
		{
			
			Date fromDate = GlobalCommonDateUtils.getDate(dto.getFromDate(), "yyyy-M-dd");
			Date toDate = GlobalCommonDateUtils.getDate(dto.getToDate(), "yyyy-M-dd");
		  Response surgicalCalenderRes = iSurgicalCalendarMasterDao.getWeelkyCalenderSlots(dto);
			List<SurgicalCalendarDetailsDto> listMst = surgicalCalenderRes.getListObject();
			List<SurgicalCalendarMasterDto> finalList = new ArrayList<>();
			
			Integer maxSlots = 0;
				   Calendar calendar = new GregorianCalendar();
				    calendar.setTime(fromDate);
				     
				    Calendar endCalendar = new GregorianCalendar();
				    endCalendar.setTime(toDate);
				  for (int i = 0; i < 7; i++) {
				        Date result = calendar.getTime();
				        SurgicalCalendarMasterDto addDto = new SurgicalCalendarMasterDto();
				        addDto.setProcedureDate(GlobalCommonDateUtils.getStringDate(result, "dd-M-yyyy HH:mm:ss"));
				        addDto.setProcedureInDate(result);
				        List<SurgicalCalendarDetailsDto> list = new ArrayList<>();
				        
				        list = listMst.stream()                
				                .filter(line -> line.getProcedureDate().equals(addDto.getProcedureDate()))
				                .sorted((o1, o2)->o1.getSurgicalCalenderId().compareTo(o2.getSurgicalCalenderId()))
				                .collect(Collectors.toList()); 
				        
				        Integer tempCount = list.size();
				        if(tempCount>maxSlots)
				        {
				        	maxSlots=tempCount;
				        }
				     /*   list = (List<SurgicalCalendarDetailsDto>) listMst.stream()
				                .filter(x -> x.getProcedureDate().equals(addDto.getProcedureDate()))
				                .findAny()
				                .orElse(null)
				                .collect(Collectors.toList());*/
				     /*   Map<Date, List<SurgicalCalendarDetailsDto>> byDay = listMst.stream()
				                .collect(groupingBy(d -> d.ge));*/
				       /* Map<Date, SurgicalCalendarDetailsDto> likesPerType = listMst.stream()
				        		  .collect(groupingBy(SurgicalCalendarDetailsDto::getProcedureInDate,Collectors.toList()));*/
				        addDto.setSurgicalCalendarDetailsDtoList(list);
				        finalList.add(addDto);
				        calendar.add(Calendar.DATE, 1);
				    }
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, finalList, maxSlots);
		} catch (Exception e)
		{
			logger.error("getWeelkyCalenderSlots failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response saveCalenderSlotMpper(SurgicalCalenderMapperDto dto) throws ApplicationException {
		try
		{
			 iSurgicalCalendarMasterDao.saveCalenderSlotMpper(dto);
			 iSurgicalCalendarMasterDao.updateSlotStatusCalenderId(dto.getSurgicalCalenderId(), 2);
			 return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e)
		{
			logger.error("saveCalenderSlotMpper failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	
	public List<List<DayMasterDto>> datesAsPerDayBetweenTwoDatesForSurgicalCalender(String startDateString, String endDateString,
			List<Integer> dayArray, Integer orgId, Integer unitId) {
		try {
			List<List<DayMasterDto>> dateMasterList = new LinkedList<>();
			List<DayMasterDto> dateList = new LinkedList<>();
			DayMasterDto dayMasterDto = null;
			Set<String> ListOfHolidayAndLinkedList = new LinkedHashSet<>();
			
			//<-----Retrieving list of holidays
			UnitHolidayCalenderMapperDto unitHolidayCalenderMapperDto = new UnitHolidayCalenderMapperDto();
				unitHolidayCalenderMapperDto.setOrganizationId(orgId);
				unitHolidayCalenderMapperDto.setUnitId(unitId);
			Response holidayListRes = iUnitHolidayCalenderDao.getActiveUnitHolidayCalenerList(unitHolidayCalenderMapperDto);
			List<HolidayCalenderMasterDto> holidayList = holidayListRes.getListObject();
			//----->
			
			/*//<-----Retrieving list of weekends
			WeekendMasterDto weekendMasterDto=new WeekendMasterDto();
				weekendMasterDto.setOrganizationId(orgId);
				weekendMasterDto.setUnitId(unitId);
			Response weekendRes = iWeekendMasterDao.getWeekendMasterList(weekendMasterDto);
			List<WeekendMasterDto> weekendsList = weekendRes.getListObject();
			//----->
			
			//<-----Need to merge holiday and linked list to avoid duplication.
			ListIterator<HolidayCalenderMasterDto> holidayItr = holidayList.listIterator();
			while(holidayItr.hasNext())
			{
				HolidayCalenderMasterDto holidayCalenderMasterDto = holidayItr.next();
					ListOfHolidayAndLinkedList.add(holidayCalenderMasterDto.getHolidayDate());
			}
			
			ListIterator<WeekendMasterDto> weekendItr = weekendsList.listIterator();
			while(weekendItr.hasNext())
			{
				WeekendMasterDto weekendMasterDto2 = weekendItr.next();
				ListOfHolidayAndLinkedList.add(weekendMasterDto2.getWeekendDate());
			}*/
			
			for (int i = 0; i < dayArray.size(); i++) 
			{
				org.joda.time.LocalDate startDate = new org.joda.time.LocalDate(startDateString.substring(0, 10));
				org.joda.time.LocalDate endDate = new org.joda.time.LocalDate(endDateString.substring(0, 10));
				org.joda.time.LocalDate day = startDate.withDayOfWeek(dayArray.get(i));

				if (startDate.isAfter(day)) {
					startDate = day.plusWeeks(1);
				} else {
					startDate = day;
				}
				while (startDate.isBefore(endDate)) {

					if (this.isHoliday(startDate.toString(),holidayList) == true) {
						dayMasterDto = new DayMasterDto();
						dayMasterDto.setDay(startDate.toString());
						dayMasterDto.setDayId(dayArray.get(i));
						dateList.add(dayMasterDto);
					}
					startDate = startDate.plusWeeks(1);
				}
				//System.out.println(dateList);
			}
			dateMasterList.add(dateList);
			return dateMasterList;
		} catch (Exception e) {
			logger.error("datesAsPerDayBetweenTwoDatesForSurgicalCalender failed", e);
		}
		return null;
	}

	
	public List<String> getTimeSlot(String startTime, String endTime, int SlotTime) {
		try {
			List<String> slotList = new LinkedList<>();
			java.time.LocalDateTime start = java.time.LocalDateTime.of(java.time.LocalDate.now(),java.time.LocalTime.parse(startTime));
			java.time.LocalDateTime end = java.time.LocalDateTime.of(java.time.LocalDate.now(),java.time.LocalTime.parse(endTime));

			if ((start.compareTo(end)) > 0) // Comparing start time and end time are from different dates
				end = java.time.LocalDateTime.of(java.time.LocalDate.now().plusDays(1),
						java.time.LocalTime.parse(endTime));

			Integer timeDifference = (int) java.time.Duration.between(start, end).toMinutes();

			int slots = timeDifference / SlotTime;
			int rem = timeDifference % SlotTime;
			//if remainder is not zero then no slots will be generated 
			if (rem == 0) {
				org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm");
				org.joda.time.LocalTime time = formatter.parseLocalTime(startTime);
				slotList.add((java.time.LocalTime.parse(time.toString())).toString());
				for (int j = 0; j < slots; j++) {
					time = time.plusMinutes(SlotTime);
					slotList.add((java.time.LocalTime.parse(time.toString())).toString());
				}
			}
			return slotList;
		} catch (Exception e) {
			logger.error("getTimeSlot failed", e);
		}
		return null;
	}
	

	
	@Override
	public Response listSergicalCalender(SurgicalCalendarReqDto surgicalCalendarReqDto) throws ApplicationException {
		try
		{
			return iSurgicalCalendarMasterDao.listSergicalCalender(surgicalCalendarReqDto);
		} catch (Exception e)
		{
			logger.error("listSergicalCalender failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response countSergicalCalender(SurgicalCalendarReqDto surgicalCalendarReqDto) throws ApplicationException {
		try
		{
			return iSurgicalCalendarMasterDao.countSergicalCalender(surgicalCalendarReqDto);
		} catch (Exception e)
		{
			logger.error("countSergicalCalender failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response updateStatusByCalenderId(Integer surgicalCalenderId, char status) throws ApplicationException {
		try
		{
			return iSurgicalCalendarMasterDao.updateStatusByCalenderId(surgicalCalenderId, status);
		} catch (Exception e)
		{
			logger.error("updateStatusByCalenderId failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response updateStatusByProcedureDate(SurgicalCalendarReqDto surgicalCalendarReqDto)
			throws ApplicationException {
		try
		{
			return iSurgicalCalendarMasterDao.updateStatusByProcedureDate(surgicalCalendarReqDto);
		} catch (Exception e)
		{
			logger.error("updateStatusByProcedureDate failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response surgicalCalenderDetails(SurgicalCalendarReqDto surgicalCalendarReqDto) throws ApplicationException {
		try
		{
			return iSurgicalCalendarMasterDao.surgicalCalenderDetails(surgicalCalendarReqDto);
		} catch (Exception e)
		{
			logger.error("surgicalCalenderDetails failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	
	/*// Method to check whether the slot times are between break time or not  
	public boolean isSlotIsBetweenBreakTime(List<TimeSlotsDto> timeSlotsDtosList, TimeSlotsDto timeSlotsDto) {
		java.time.LocalTime start = java.time.LocalTime.parse(timeSlotsDto.getFromTime());
		java.time.LocalTime end = java.time.LocalTime.parse(timeSlotsDto.getToTime());
		ListIterator breakListItr = timeSlotsDtosList.listIterator();
		while (breakListItr.hasNext()) {
			TimeSlotsDto timeSlotsDto2 = (TimeSlotsDto) breakListItr.next();
			java.time.LocalTime startBreak = java.time.LocalTime.parse(timeSlotsDto2.getBreakFromTime());
			java.time.LocalTime endBreak = java.time.LocalTime.parse(timeSlotsDto2.getBreakToTime());

			if (((start.isAfter(startBreak) && start.isBefore(endBreak))
					&& (end.isAfter(startBreak) && end.isBefore(endBreak)) || start.compareTo(startBreak) == 0
					|| end.compareTo(endBreak) == 0)) {
				return false;
			}

		}
		return true;
	}*/

	// Method to check whether the date is in holiday list or not 
	public boolean isHoliday(String startDate, List<HolidayCalenderMasterDto> holidayList) 
	{
		ListIterator holidayListItr = holidayList.listIterator();
		while (holidayListItr.hasNext()) 
		{
			HolidayCalenderMasterDto holidayCalenderMasterDto = (HolidayCalenderMasterDto) holidayListItr.next();
			String date = holidayCalenderMasterDto.getHolidayDate();
			
			if ((date.equals(startDate)))
			{
				return false;
			}
		}
		return true;
	
	}
	// Method to check whether the date is in weekend list or not 
	public boolean isWeekend(String startDate, List<WeekendMasterDto> weekendMasterDtosList) 
	{
		ListIterator holidayListItr = weekendMasterDtosList.listIterator();
		while (holidayListItr.hasNext()) 
		{
			WeekendMasterDto weekendMasterDto = (WeekendMasterDto) holidayListItr.next();
			String date = weekendMasterDto.getWeekendDate();
			if ((date.equals(startDate)))
			{
				return false;
			}
		}
		return true;
	
	}





	









	

}