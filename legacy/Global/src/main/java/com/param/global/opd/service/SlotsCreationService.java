package com.param.global.opd.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.Response;
import com.param.global.dao.IUnitHolidayCalenderDao;
import com.param.global.dao.IWeekendMasterDao;
import com.param.global.dto.DayMasterDto;
import com.param.global.dto.HolidayCalenderMasterDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.TimeSlotsDto;
import com.param.global.dto.UnitHolidayCalenderMapperDto;
import com.param.global.dto.WeekendMasterDto;
import com.param.utility.TimeZoneComponent;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SlotsCreationService {
	final static Logger logger = Logger.getLogger(SlotsCreationService.class);


	@Autowired
	private IUnitHolidayCalenderDao iUnitHolidayCalenderDao;
	
/*	@Autowired
	ISlotMasterDao iSlotMasterDao;
*/
	@Autowired
	IWeekendMasterDao iWeekendMasterDao;
	
	public List<List<DayMasterDto>> getListOfDatesAsPerDayBetweenTwoDates(String startDateString, String endDateString,
			List<Integer> dayArray, Integer orgId, Integer unitId) {
		try {
			List<List<DayMasterDto>> dateMasterList = new LinkedList<>();
			List<DayMasterDto> dateList = new LinkedList<>();
			DayMasterDto dayMasterDto = null;
			//Set<String> ListOfHolidayAndLinkedList = new LinkedHashSet<>();
			
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
				Date currentDate = TimeZoneComponent.getDateByzone(unitId);
				Date startDDate = GlobalCommonDateUtils.getDate(startDateString.substring(0, 10),"yyyy-MM-dd");
				if(startDDate.compareTo(currentDate) < 0) 
				{
					startDateString = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId),"yyyy-MM-dd");
				} 
				org.joda.time.LocalDate startDate = new org.joda.time.LocalDate(startDateString.substring(0, 10));
				org.joda.time.LocalDate endDate = new org.joda.time.LocalDate(endDateString.substring(0, 10));
				org.joda.time.LocalDate day = startDate.withDayOfWeek(dayArray.get(i));

				if (startDate.isAfter(day)) {
					startDate = day.plusWeeks(1);
				} else {
					startDate = day;
				}
				while (startDate.isBefore(endDate)||startDate.isEqual(endDate)) 
				{

					if (this.isHoliday(startDate.toString(),holidayList) == true) 
					{
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
		logger.error("getListOfDatesAsPerDayBetweenTwoDates failed", e);
		}
		return null;
	}


	public List<String> getTimeSlot(String startTime, String endTime, int SlotTime) {
		try {
			List<String> slotList = new LinkedList<>();
			// Parse start and end times as ZonedDateTime (including date, time, and time zone)
			ZonedDateTime start = ZonedDateTime.parse(startTime);
			ZonedDateTime end = ZonedDateTime.parse(endTime);

			// Adjust the end time if the start time is later (crosses into the next day)
			if (start.isAfter(end)) {
				end = end.plusDays(1); // Add one day to end if start is later than end
			}

			// Calculate the time difference in minutes
			long timeDifference = Duration.between(start, end).toMinutes();

			// Calculate the number of slots and the remainder
			int slots = (int) (timeDifference / SlotTime);
			int rem = (int) (timeDifference % SlotTime);

			// Only proceed if the remainder is 0 (even slots)
			if (rem == 0) {
				// Start time for slot generation (use LocalTime from ZonedDateTime)
				LocalDateTime localStart = start.toLocalDateTime(); // Convert ZonedDateTime to LocalDateTime
				LocalDateTime localTime = localStart; // This will store the current slot time

				// Add the first slot
				slotList.add(localTime.toLocalTime().toString());

				// Generate subsequent slots by adding SlotTime minutes to the previous time
				for (int i = 0; i < slots; i++) {
					localTime = localTime.plusMinutes(SlotTime);
					slotList.add(localTime.toLocalTime().toString()); // Add slot to the list
				}
			}

			return slotList;
		} catch (Exception e) {
		logger.error("getTimeSlot failed", e);
		}
		return null;
	}
	

	// Method to check whether the slot times are between break time or not  
	public boolean isSlotIsBetweenBreakTime(List<TimeSlotsDto> timeSlotsDtosList, TimeSlotsDto timeSlotsDto) 
	{
		java.time.LocalTime start = java.time.LocalTime.parse(timeSlotsDto.getFromTime());
		java.time.LocalTime end = java.time.LocalTime.parse(timeSlotsDto.getToTime());
		ListIterator breakListItr = timeSlotsDtosList.listIterator();
		while (breakListItr.hasNext()) 
		{
			TimeSlotsDto timeSlotsDto2 = (TimeSlotsDto) breakListItr.next();
			java.time.LocalTime startBreak = java.time.LocalTime.parse(timeSlotsDto2.getBreakFromTime());
			java.time.LocalTime endBreak = java.time.LocalTime.parse(timeSlotsDto2.getBreakToTime());

			if (((start.isAfter(startBreak) && start.isBefore(endBreak))
					&& (end.isAfter(startBreak) && end.isBefore(endBreak)) || start.isBefore(startBreak) && end.isAfter(startBreak) || start.isBefore(endBreak) && end.isAfter(endBreak)
						|| start.compareTo(startBreak) == 0
							|| end.compareTo(endBreak) == 0)) 
			{
				return false;
			}else{
				return true;
			}

		}
		return true;
	}

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
	
	public Boolean isSlotAvailble(List<SlotMasterDto> appointmentsForMonthsList, String date) 
	{
		ListIterator appointmentsForMonthsListItr = appointmentsForMonthsList.listIterator();
		while (appointmentsForMonthsListItr.hasNext()) {
			SlotMasterDto slotMasterDto = (SlotMasterDto) appointmentsForMonthsListItr.next();
			if ((slotMasterDto.getAppointmentDate().equals(date))) {
				return true;
			}
		}
		return false;
	}
	
	public Boolean isBetweenHolidaysAndWeekends(List<String> HolidaysAndWeekendsMergedList, String date)
	{
		
		ListIterator<String> HolidaysAndWeekendsMergedListItr = HolidaysAndWeekendsMergedList.listIterator();
		while(HolidaysAndWeekendsMergedListItr.hasNext())
		{
			if(HolidaysAndWeekendsMergedListItr.next().equals(date))
			{
				return true;
			}
		}
		
		return false;
		
	}
	
	public List<String> getListOfDatesAsPerDayBetweenTwoDates(DateTime startDateString, DateTime endDateString) {
		try{
		LocalDate start = LocalDate.parse(startDateString.toString().substring(0,10));
		LocalDate end = LocalDate.parse(endDateString.toString().substring(0,10));
		List<String> totalDates = new ArrayList<>();
		while (!start.isAfter(end)) {
		    totalDates.add(start.toString().substring(0, 10));
		    start = start.plusDays(1);
		}
		return totalDates;
		}catch(Exception e)
		{
		logger.error("getListOfDatesAsPerDayBetweenTwoDates failed", e);
		}
		return null;
	}
	
	public Boolean isDateBetweenTwoSpecifiedDates(String startDateString, String endDateString, String dateToBeChecked) {
		Boolean result = false;
		try {
			//System.out.println(startDateString+"-"+endDateString+"--"+dateToBeChecked+"---"+result);
			LocalDate startDate = new LocalDate(startDateString);
			LocalDate endDate = new LocalDate(endDateString);
			LocalDate event = new LocalDate(dateToBeChecked);
			if (!event.isBefore(startDate) && !event.isAfter(endDate)) {
				result = true;
			} else {
				result = false;
			}
			return result;
		} catch (Exception e) {
		logger.error("isDateBetweenTwoSpecifiedDates failed", e);
		}
		//System.out.println(startDateString+"-"+endDateString+"--"+dateToBeChecked+"---"+result);
		return result;
	}
	
	public static void main(String[] args) {
		
		SlotsCreationService slotsCreationService=new SlotsCreationService();
		
		
		/*////System.out.println(slotsCreationService.isDateBetweenTwoSpecifiedDates("2018-10-04","2018-01-25","2018-04-12"));
		List<String> abc = slotsCreationService.getTimeSlot("09:00", "12:00", 30);
		for (Iterator iterator = abc.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			//System.out.println(string);
		}*/
		List<Integer> a  = (Arrays.asList(1,2));
		slotsCreationService.getListOfDatesAsPerDayBetweenTwoDates("02-10-2018", "06-18-2018",a , 1, 1);
	
	}
	
	//2018-10-04-2018-01-25--2018-04-12---false
	
}

