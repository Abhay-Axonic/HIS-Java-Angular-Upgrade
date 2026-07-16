package com.param.opd.unit.service;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IDoctorMasterDao;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.doctor.dao.IDoctorScheduleDao;
import com.param.global.dto.BlockSlotReqDto;
import com.param.global.dto.DayMasterDto;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.dto.MyLifeConnectionLinkDto;
import com.param.global.dto.ScheduleMasterDto;
import com.param.global.dto.SlotBlockUnblockRequestDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.SlotPatientDetails;
import com.param.global.dto.TimeSlotsDto;
import com.param.global.dto.sync.DoctorScheduleSyncDto;
import com.param.global.dto.sync.OrganisationMasterDtoSync;
import com.param.global.opd.service.SlotsCreationService;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.org.dto.UnitMasterDto;
import com.param.opd.coversheet.dto.WeeklySlotsDto;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.opd.unit.dao.ISlotBlockUnblockDetailDao;
import com.param.opd.unit.dao.ISlotMasterDao;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SlotMasterServiceImpl implements ISlotMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(SlotMasterServiceImpl.class);


	@Autowired
	ISlotMasterDao iSlotMasterDao;
	@Autowired
	IDoctorMasterDao iDoctorMasterDao;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	SlotsCreationService slotsCreationService;
	@Autowired
	private Gson gson;
	@Autowired
	ISlotBlockUnblockDetailDao iSlotBlockUnblockDetailDao;
	@Autowired
	private IDoctorScheduleDao iDoctorScheduleDao;
	@Autowired
	@Qualifier("applicationEventMulticaster")
	private ApplicationEventPublisher applicationEventPublisher;
	@Autowired
	private IScheduleLogsDao isheduleDao;
	
	@Override
	public Response generateSlots(SlotMasterDto slotMasterDto) throws ApplicationException {
		
		try {
			
			List<List<DayMasterDto>> datesMasterList = new ArrayList<>();
			List<SlotMasterDto> listForSaveCall = new LinkedList<>();

			Iterator<SlotMasterDto> itr = slotMasterDto.getSlotMasterDtosList().iterator();

			while (itr.hasNext()) {
				List<String> timeSlotsList = new ArrayList<String>();
				Integer docId=null,speId = null,modalityId=null,subSpecilaityId=null;
				List<TimeSlotsDto> timeSlotsDtos = new LinkedList<>();
				SlotMasterDto slotMasterDtosObj = itr.next();

				//Check schedule already already exist for doctor
				if(slotMasterDto.getSlotTypeId()==1)
				{
					String Global_Scheduling_URI = null;
					Response globalRes=isheduleDao.getConnectionStringForMyLife();
					MyLifeConnectionLinkDto connectionLinkDto=(MyLifeConnectionLinkDto)globalRes.getObject();
					if(connectionLinkDto!=null && connectionLinkDto.getMyLifeLink()!=null && connectionLinkDto.getMyLifeLink()!="" && connectionLinkDto.getGlobalSchedulingLink()!=null && connectionLinkDto.getGlobalSchedulingLink()!="")
					{
						Global_Scheduling_URI = connectionLinkDto.getGlobalSchedulingLink();

						if(Global_Scheduling_URI!=null && Global_Scheduling_URI!="")
						{

						  /*1. Sunday
							2. Monday
							3. Tuesday
							4. Wednesday
							5. Thursday
							6. Friday
							7. Saturday*/

							List<Integer> dayIdsForGDS = new LinkedList<>();
							for(Integer dayId :slotMasterDtosObj.getDayArrayList())
							{
								switch (dayId) {

									case 1: dayIdsForGDS.add(2);
											break;
									case 2: dayIdsForGDS.add(3);
											break;
									case 3: dayIdsForGDS.add(4);
											break;
									case 4: dayIdsForGDS.add(5);
											break;
									case 5: dayIdsForGDS.add(6);
											break;
									case 6: dayIdsForGDS.add(7);
											break;
									case 7: dayIdsForGDS.add(1);
											break;
								}
							}

							List<DoctorScheduleSyncDto> finalData = new LinkedList<>();
							URI url = null;
							Response commonResponse = null;
							DoctorScheduleSyncDto schedule =new DoctorScheduleSyncDto();
								schedule.setFromDate(GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(slotMasterDtosObj.getFromDate(), "yyyy-MM-dd"), "dd-MM-yyyy"));
								schedule.setToDate(GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(slotMasterDtosObj.getToDate(), "yyyy-MM-dd"), "dd-MM-yyyy"));
								schedule.setFromTime(slotMasterDtosObj.getFromTime().toString());
								schedule.setToTime(slotMasterDtosObj.getToTime().toString());
								schedule.setApplicationId(2);
								schedule.setDayIds(dayIdsForGDS);
							for(DoctorMasterDto docDto : slotMasterDto.getDoctorArrayList())
							{
								schedule.setDoctorId(iDoctorMasterDao.getGlobalDocId(docDto.getDoctorId()));
								finalData.add(schedule);
							}
							HttpHeaders headers = new HttpHeaders();
							Integer channelId=isheduleDao.getchannelIdForSync(slotMasterDto.getOrganizationId());
							if(channelId!=null) {
							headers.set("channelId", channelId.toString());
							url = URI.create(Global_Scheduling_URI + "/doctor/overlappingschedule");
							HttpEntity <List<DoctorScheduleSyncDto>> request = new HttpEntity<>(finalData, headers);
							String stringResponse = restTemplate.postForObject(url, request, String.class);
							//String stringResponse = restTemplate.postForObject(url, finalData, String.class);
							commonResponse = gson.fromJson(stringResponse, Response.class);
							Double count = (Double) commonResponse.getObject();
							if(commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS) && count > 0)
							{
								return new Response(ERROR, ALREADY_EXIST_CODE, "There are some overlapping global schedules found for selected doctor...!!", null, 'N');
							}
							}

						}
					}

				}

				if(slotMasterDto.getOverride()!=null && slotMasterDto.getOverride()=='Y')
				{
					iSlotMasterDao.getOldSlotDetails(slotMasterDtosObj, slotMasterDto.getDoctorArrayList(),
							slotMasterDto.getUnitId(), slotMasterDto.getSlotTypeId(),'Y');
				}else{
					Response res = iSlotMasterDao.getOldSlotDetails(slotMasterDtosObj, slotMasterDto.getDoctorArrayList(),
							slotMasterDto.getUnitId(), slotMasterDto.getSlotTypeId(),'N');
					if(res.getStatus().equals(ERROR))
					{
						return res;
					}
				}

				// Generating list of time slots
				timeSlotsList = slotsCreationService.getTimeSlot(slotMasterDtosObj.getFromTime().toString(),
						slotMasterDtosObj.getToTime().toString(),
						slotMasterDtosObj.getTimePerSlot());

				if((timeSlotsList.isEmpty())){
					 return new Response(ERROR, null, "Please enter accurate slot time !!", null, null);
				}
				// checking whether the break time list is blank


				TimeSlotsDto timeSlotsDto = null;
				for (int k = 0; k < (timeSlotsList.size() - 1); k++)
				{
					timeSlotsDto = new TimeSlotsDto();
					timeSlotsDto.setFromTime(timeSlotsList.get(k));
					timeSlotsDto.setToTime(timeSlotsList.get(k + 1));
					// checking slots are in between break time or not


					if(slotMasterDtosObj.getBreakSlotsList().getFirst().getBreakFromTime()!="" && slotMasterDtosObj.getBreakSlotsList().getFirst().getBreakToTime()!="")
					{
						if (slotsCreationService.isSlotIsBetweenBreakTime(slotMasterDtosObj.getBreakSlotsList(),timeSlotsDto) == true)
						timeSlotsDtos.add(timeSlotsDto);
					}
					else
						timeSlotsDtos.add(timeSlotsDto);
				}

				//generating list of dates excluding holidays
				datesMasterList = slotsCreationService.getListOfDatesAsPerDayBetweenTwoDates(
						slotMasterDtosObj.getFromDate(),
						slotMasterDtosObj.getToDate(),
						slotMasterDtosObj.getDayArrayList(),
						slotMasterDto.getOrganizationId(),
						slotMasterDto.getUnitId());


				ListIterator doctorItr = slotMasterDto.getDoctorArrayList().listIterator();
				while (doctorItr.hasNext())
				{
					DoctorMasterDto doctorMasterDto = (DoctorMasterDto) doctorItr.next();
						if(slotMasterDto.getSlotTypeId()==1){
							docId = doctorMasterDto.getDoctorId();
							speId = doctorMasterDto.getSpecialityId();
						}
						if(slotMasterDto.getSlotTypeId()==2){
							modalityId=doctorMasterDto.getModalityId();
							subSpecilaityId=doctorMasterDto.getSubSpecialityId();
							speId = doctorMasterDto.getSpecialityId();
						}
						if(slotMasterDto.getSlotTypeId()==4){
							speId = doctorMasterDto.getSpecialityId();
						}

					ListIterator superItr = datesMasterList.listIterator();	//Iterator for list of dto's
					ListIterator childItr = null; //Iterator for days and dates from DayMasterDto
					while (superItr.hasNext())
					{
						List<List<DayMasterDto>> childList = (List<List<DayMasterDto>>) superItr.next();

						childItr = childList.listIterator();
						while (childItr.hasNext())
						{
							DayMasterDto data = (DayMasterDto) childItr.next();
								String date = data.getDay();
								Integer dayId = data.getDayId();

							ListIterator timeSlotsDtosItrList = timeSlotsDtos.listIterator();
							while (timeSlotsDtosItrList.hasNext())
							{

								TimeSlotsDto timeSlotsDto2 = (TimeSlotsDto) timeSlotsDtosItrList.next();

								SlotMasterDto slotMasterDtoNew = new SlotMasterDto();
									slotMasterDtoNew.setOrganizationId(slotMasterDto.getOrganizationId());
									slotMasterDtoNew.setUnitId(slotMasterDto.getUnitId());
									slotMasterDtoNew.setSpecialityId(speId);
									slotMasterDtoNew.setDoctorId(docId);
									slotMasterDtoNew.setFromDate(date);// appointmentDate
									slotMasterDtoNew.setDayId(dayId);
									slotMasterDtoNew.setFromTime(timeSlotsDto2.getFromTime());
									slotMasterDtoNew.setToTime(timeSlotsDto2.getToTime());
									slotMasterDtoNew.setCreatedBy(slotMasterDto.getCreatedBy());
									slotMasterDtoNew.setUpdatedBy(slotMasterDto.getUpdatedBy());
									slotMasterDtoNew.setCreatedDate(slotMasterDto.getCreatedDate());
									slotMasterDtoNew.setUpdatedDate(slotMasterDto.getUpdatedDate());
									slotMasterDtoNew.setStatus('A');
									slotMasterDtoNew.setSlotTypeId(slotMasterDto.getSlotTypeId());
									slotMasterDtoNew.setModalityId(modalityId);
									slotMasterDtoNew.setVisitTypeId(slotMasterDtosObj.getVisitTypeId());
									slotMasterDtoNew.setSubSpecialityId(subSpecilaityId);
									slotMasterDtoNew.setIsBlocked(2);	//unblocked
								/*Response res = iSlotMasterDao.isSlotAlreadyExist(slotMasterDtoNew);
								Character yseNo = (Character) res.getObject();

								if(yseNo.equals('N'))*/

								listForSaveCall.add(slotMasterDtoNew);

							}
						}
					}
				}
			}
			iSlotMasterDao.generateSlots(listForSaveCall,slotMasterDto.getSlotTypeId());
			if(slotMasterDto.getSlotTypeId()==1){
				calculateTimeslots(slotMasterDto);
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
		logger.error("generateSlots failed", e);
			return new Response(ERROR, null, null, null, null);
		}

	}


	//Function for calculate timeslots for sych to global
	public Response calculateTimeslots(SlotMasterDto slotMasterDto){	
		try{			
			
			
			TimeSlotsDto timeSlotDto = null;	
			Integer docId = null, speId = null, modalityId = null, subSpecilaityId=null;		  
			ListIterator doctorItr = slotMasterDto.getDoctorArrayList().listIterator();	
			while (doctorItr.hasNext()) 	
			{	
				DoctorMasterDto doctorMasterDto = (DoctorMasterDto) doctorItr.next();	
				if(slotMasterDto.getSlotTypeId()==1){	
					docId = doctorMasterDto.getDoctorId();	
					speId = doctorMasterDto.getSpecialityId();	
					}	
				if(slotMasterDto.getSlotTypeId()==2){
					modalityId=doctorMasterDto.getModalityId();
					subSpecilaityId=doctorMasterDto.getSubSpecialityId();
					speId = doctorMasterDto.getSpecialityId();	
					}	
					
			Iterator slotMstDtoIterator = slotMasterDto.getSlotMasterDtosList().iterator();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");	
			while(slotMstDtoIterator.hasNext())
			{	
				//iterate through main list	
				List<TimeSlotsDto> listTimeSlotDtos = new ArrayList<>();	
				SlotMasterDto tempSlotMasterDto = (SlotMasterDto) slotMstDtoIterator.next();	
				
				List<Integer> dayIdsForGDS = new LinkedList<>();
				for(Integer dayId :tempSlotMasterDto.getDayArrayList())
				{
					switch (dayId) {
						
						case 1: dayIdsForGDS.add(2);
								break;
						case 2: dayIdsForGDS.add(3);
								break;
						case 3: dayIdsForGDS.add(4);
								break;
						case 4: dayIdsForGDS.add(5);
								break;
						case 5: dayIdsForGDS.add(6);
								break;
						case 6: dayIdsForGDS.add(7);
								break;
						case 7: dayIdsForGDS.add(1);
								break;		
					}
				}
				
				Date currentDate = TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId());
				String fromDateString = tempSlotMasterDto.getFromDate().substring(0,10);
				Date startDDate = GlobalCommonDateUtils.getDate(fromDateString,"yyyy-MM-dd");
				if(startDDate.compareTo(currentDate) < 0) 
				{
					fromDateString = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()),"yyyy-MM-dd");
				} 
				
				Date fromDate = dateFormat.parse(fromDateString);	
				Date toDate = dateFormat.parse(tempSlotMasterDto.getToDate());

				Instant fromTime = tempSlotMasterDto.getFromTime();
				Instant toTime = tempSlotMasterDto.getToTime();
				
				if(tempSlotMasterDto.getBreakSlotsList().getFirst().getBreakFromTime()!="" && tempSlotMasterDto.getBreakSlotsList().getFirst().getBreakToTime()!="")	
				{					
					Iterator breakTimeSlotIte = tempSlotMasterDto.getBreakSlotsList().iterator();	
					//MultiValueMap mValueMap = new LinkedMultiValueMap<>();	
					Instant bEndTime = null;
					while(breakTimeSlotIte.hasNext())
					{	
						TimeSlotsDto breakTimeslotDto =(TimeSlotsDto) breakTimeSlotIte.next();

						LocalTime bFromTime1 = LocalTime.parse(breakTimeslotDto.getBreakFromTime());
						LocalDate today = LocalDate.now();
						Instant bFromTime = bFromTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

						LocalTime bToTime1 = LocalTime.parse( breakTimeslotDto.getBreakToTime());
						Instant bToTime = bToTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();


						if(bFromTime.isAfter(fromTime)&& bFromTime.isBefore(toTime)&& bToTime.isAfter(fromTime)&& bToTime.isBefore(toTime))
						{
							timeSlotDto = new TimeSlotsDto();
							if(bEndTime!=null)
							{	
								timeSlotDto.setFromTime(bEndTime.toString());
								timeSlotDto.setToTime(bFromTime.toString());	
							}	
							else
							{	
								timeSlotDto.setFromTime(fromTime.toString());
								timeSlotDto.setToTime(bFromTime.toString());
							}	
								bEndTime = bToTime;
								listTimeSlotDtos.add(timeSlotDto);
							}else{					
								return new Response(ERROR, COMMON_ERROR_CODE, "Wrong Break Time selection!!", null, null);			
							}						
						if(bEndTime!=null)
						{	
							timeSlotDto = new TimeSlotsDto();						
							timeSlotDto.setFromTime(bEndTime.toString());
							timeSlotDto.setToTime(toTime.toString());
							listTimeSlotDtos.add(timeSlotDto);
							//mValueMap.add(bEndTime,toTime);
							}											
						}//END BREAKTIME LIST ITERATOR				
				}else
				{					
					timeSlotDto = new TimeSlotsDto();				
					timeSlotDto.setFromTime(fromTime.toString());		
					timeSlotDto.setToTime(toTime.toString());		
					listTimeSlotDtos.add(timeSlotDto);			
				}				
																											
				//LOOP THROUGH FROM_DATE & TO_DATE				
				Calendar startCalender = Calendar.getInstance();			
				startCalender.setTime(fromDate);			   
				
				Calendar endCalendar = Calendar.getInstance();			  
				endCalendar.setTime(toDate);			    			
				final Calendar current = Calendar.getInstance();			    
				current.setTime(fromDate);
				
				List<ScheduleMasterDto> scheduleMasterDtosList = new LinkedList<>();
				while (!current.getTime().after(toDate))				    
				{	
					
					for(TimeSlotsDto tsDto : listTimeSlotDtos)
					{		
						Calendar calendar = Calendar.getInstance();
				        calendar.setTime(GlobalCommonDateUtils.getDate(current.getTime(), "yyyy-MM-dd"));
						if(dayIdsForGDS.contains(calendar.get(Calendar.DAY_OF_WEEK)))
						{
							ScheduleMasterDto dto = new ScheduleMasterDto();	
								dto.setDoctorId(docId);				    	
								dto.setSpecialityId(speId);				    	
								dto.setScheduleDate(GlobalCommonDateUtils.getStringDate(current.getTime(), "yyyy-MM-dd"));	
								dto.setFromTime(tsDto.getFromTime().toString());				    
								dto.setToTime(tsDto.getToTime().toString());	
								dto.setCreatedBy(slotMasterDto.getCreatedBy());
								dto.setOrganisationId(slotMasterDto.getOrganizationId());				 
								dto.setUnitId(slotMasterDto.getUnitId());
								dto.setTimePerSlot(tempSlotMasterDto.getTimePerSlot());
								dto.setAddRemove(1);
							scheduleMasterDtosList.add(dto);
						}
					}				    	
					current.add(Calendar.DATE, 1);				  
				}
				iDoctorScheduleDao.saveDoctorSchedule(scheduleMasterDtosList,"INSERT");				
				
				}
			}		
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);		
			}catch(Exception e){
			logger.error("calculateTimeslots failed", e);
			}		
		return null;
	}
	


	
	@Override
	public Response generateSlotsDoctorSheduling(SlotMasterDto slotMasterDto) throws ApplicationException {
	try {

		String jsonData1;
		Iterator<SlotMasterDto> itrs = slotMasterDto.getSlotMasterDtosList().iterator();

		while (itrs.hasNext()) {
			List<List<DayMasterDto>> datesMasterList = new ArrayList<>();
			List<SlotMasterDto> listForSaveCall = new LinkedList<>();
			List<TimeSlotsDto> timeSlotsDtos = new LinkedList<>();
			List<String> timeSlotsList = new ArrayList<String>();
			Integer docId=null,speId = null,modalityId=null,subSpecilaityId=null;
			SlotMasterDto slotMasterDtosObj = itrs.next();

			//Check schedule already already exist for doctor 
			if(slotMasterDto.getSlotTypeId()==1)
			{
				String Global_Scheduling_URI = null;
				Response globalRes=isheduleDao.getConnectionStringForMyLife();
				MyLifeConnectionLinkDto connectionLinkDto=(MyLifeConnectionLinkDto)globalRes.getObject();
				if(connectionLinkDto!=null && connectionLinkDto.getMyLifeLink()!=null && connectionLinkDto.getMyLifeLink()!="" && connectionLinkDto.getGlobalSchedulingLink()!=null && connectionLinkDto.getGlobalSchedulingLink()!="")
				{
					Global_Scheduling_URI = connectionLinkDto.getGlobalSchedulingLink(); 
					Response checkFlagScheduling=iSlotMasterDao.getFlagCheckMyLife(slotMasterDto);
					OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto)checkFlagScheduling.getObject();
					if(organizationMasterDto !=null && organizationMasterDto.getIsSyncScheduling() == true){
					if(Global_Scheduling_URI!=null && Global_Scheduling_URI!="")
					{

						List<Integer> dayIdsForGDS = new LinkedList<>();
						for(Integer dayId :slotMasterDtosObj.getDayArrayList())
						{
							switch (dayId) {
								
								case 1: dayIdsForGDS.add(2);
										break;
								case 2: dayIdsForGDS.add(3);
										break;
								case 3: dayIdsForGDS.add(4);
										break;
								case 4: dayIdsForGDS.add(5);
										break;
								case 5: dayIdsForGDS.add(6);
										break;
								case 6: dayIdsForGDS.add(7);
										break;
								case 7: dayIdsForGDS.add(1);
										break;		
							}
						}
						
						List<DoctorScheduleSyncDto> finalData = new LinkedList<>();
						URI url = null;
						Response commonResponse = null;
						DoctorScheduleSyncDto schedule =new DoctorScheduleSyncDto();
							schedule.setFromDate(GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(slotMasterDtosObj.getFromDate(), "yyyy-MM-dd"), "dd-MM-yyyy"));
							schedule.setToDate(GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(slotMasterDtosObj.getToDate(), "yyyy-MM-dd"), "dd-MM-yyyy"));
							schedule.setFromTime(slotMasterDtosObj.getFromTime().toString());
							schedule.setToTime(slotMasterDtosObj.getToTime().toString());
							schedule.setApplicationId(2);
							schedule.setDayIds(dayIdsForGDS);
						for(DoctorMasterDto docDto : slotMasterDto.getDoctorArrayList())
						{
							schedule.setDoctorId(iDoctorMasterDao.getGlobalDocId(docDto.getDoctorId()));
							finalData.add(schedule);
						}
						
						ObjectMapper objectMapper = new ObjectMapper(); // Initialize your JSON serialization library
					       
			            try {
			            	jsonData1 = objectMapper.writeValueAsString(finalData);
			            	System.out.println("My-OverLappingSchedule Json is :"+jsonData1);
						} catch (JsonProcessingException e) {
						logger.error("generateSlotsDoctorSheduling: JSON serialization failed", e);
						}
				       try {
						if(finalData!= null && finalData.size()>0 && schedule.getDoctorId() != null) {
						////url = URI.create(Global_Scheduling_URI + "/doctor/overlappingschedule");
						//String stringResponse = restTemplate.postForObject(url, finalData, String.class);
						HttpHeaders headers = new HttpHeaders();
						Integer channelId=isheduleDao.getchannelIdForSync(slotMasterDto.getOrganizationId());
						if(channelId!=null) {
						headers.set("channelId", channelId.toString());
						url = URI.create(Global_Scheduling_URI + "/doctor/overlappingschedule");
						HttpEntity <List<DoctorScheduleSyncDto>> request = new HttpEntity<>(finalData, headers);
						String stringResponse = restTemplate.postForObject(url, request, String.class);
						commonResponse = gson.fromJson(stringResponse, Response.class);
						Double count = (Double) commonResponse.getObject();
						if(commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS) && count > 0)
						{
							return new Response(ERROR, ALREADY_EXIST_CODE, "There are some overlapping global schedules found for selected doctor...!!", null, 'N');
						}
						}
						}
				       }catch (Exception e) {
				       logger.error("generateSlotsDoctorSheduling failed", e);
				       }
						
					}
				}
				}
				
			}
			
				if(slotMasterDto.getOverride()!=null && slotMasterDto.getOverride()=='Y')
				{
					iSlotMasterDao.getOldSlotDetails(slotMasterDtosObj, slotMasterDto.getDoctorArrayList(),
							slotMasterDto.getUnitId(), slotMasterDto.getSlotTypeId(),'Y');
				}else{
					Response res = iSlotMasterDao.getOldSlotDetails(slotMasterDtosObj, slotMasterDto.getDoctorArrayList(),
							slotMasterDto.getUnitId(), slotMasterDto.getSlotTypeId(),'N');
					if(res.getStatus().equals(ERROR))
					{
						return res;
					}
				}
				
				
			}
		
		
		
				Iterator<SlotMasterDto> itr = slotMasterDto.getSlotMasterDtosList().iterator();

				while (itr.hasNext()) {
					List<List<DayMasterDto>> datesMasterList = new ArrayList<>();
					List<SlotMasterDto> listForSaveCall = new LinkedList<>();
					List<TimeSlotsDto> timeSlotsDtos = new LinkedList<>();
					List<String> timeSlotsList = new ArrayList<String>();
					Integer docId=null,speId = null,modalityId=null,subSpecilaityId=null;
					SlotMasterDto slotMasterDtosObj = itr.next();

					//Check schedule already already exist for doctor 
					if(slotMasterDto.getSlotTypeId()==1)
					{
						String Global_Scheduling_URI = null;
						Response globalRes=isheduleDao.getConnectionStringForMyLife();
						MyLifeConnectionLinkDto connectionLinkDto=(MyLifeConnectionLinkDto)globalRes.getObject();
						Response checkFlagScheduling=iSlotMasterDao.getFlagCheckMyLife(slotMasterDto);
						OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto)checkFlagScheduling.getObject();
						if(organizationMasterDto !=null && organizationMasterDto.getIsSyncScheduling() == true){
						if(connectionLinkDto!=null && connectionLinkDto.getMyLifeLink()!=null && connectionLinkDto.getMyLifeLink()!="" && connectionLinkDto.getGlobalSchedulingLink()!=null && connectionLinkDto.getGlobalSchedulingLink()!="")
						{
							Global_Scheduling_URI = connectionLinkDto.getGlobalSchedulingLink(); 
							
							if(Global_Scheduling_URI!=null && Global_Scheduling_URI!="")
							{
								
							  /*1. Sunday 
								2. Monday
								3. Tuesday
								4. Wednesday
								5. Thursday
								6. Friday
								7. Saturday*/

								List<Integer> dayIdsForGDS = new LinkedList<>();
								for(Integer dayId :slotMasterDtosObj.getDayArrayList())
								{
									switch (dayId) {
										
										case 1: dayIdsForGDS.add(2);
												break;
										case 2: dayIdsForGDS.add(3);
												break;
										case 3: dayIdsForGDS.add(4);
												break;
										case 4: dayIdsForGDS.add(5);
												break;
										case 5: dayIdsForGDS.add(6);
												break;
										case 6: dayIdsForGDS.add(7);
												break;
										case 7: dayIdsForGDS.add(1);
												break;		
									}
								}
								
								List<DoctorScheduleSyncDto> finalData = new LinkedList<>();
								URI url = null;
								Response commonResponse = null;
								DoctorScheduleSyncDto schedule =new DoctorScheduleSyncDto();
									schedule.setFromDate(GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(slotMasterDtosObj.getFromDate(), "yyyy-MM-dd"), "dd-MM-yyyy"));
									schedule.setToDate(GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(slotMasterDtosObj.getToDate(), "yyyy-MM-dd"), "dd-MM-yyyy"));
									schedule.setFromTime(slotMasterDtosObj.getFromTime().toString());
									schedule.setToTime(slotMasterDtosObj.getToTime().toString());
									schedule.setApplicationId(2);
									schedule.setDayIds(dayIdsForGDS);
								for(DoctorMasterDto docDto : slotMasterDto.getDoctorArrayList())
								{
									schedule.setDoctorId(iDoctorMasterDao.getGlobalDocId(docDto.getDoctorId()));
									finalData.add(schedule);
								}
								ObjectMapper objectMapper = new ObjectMapper(); // Initialize your JSON serialization library
							       
					            try {
					            	jsonData1 = objectMapper.writeValueAsString(finalData);
					            	System.out.println("My-OverLappingSchedule Json is :"+jsonData1);
								} catch (JsonProcessingException e) {
								logger.error("generateSlotsDoctorSheduling failed", e);
								}
								try {
								if(finalData!= null && finalData.size()>0 && schedule.getDoctorId()!= null) {
								//url = URI.create(Global_Scheduling_URI + "/doctor/overlappingschedule");
								//String stringResponse = restTemplate.postForObject(url, finalData, String.class);
									
								HttpHeaders headers = new HttpHeaders();
								Integer channelId=isheduleDao.getchannelIdForSync(slotMasterDto.getOrganizationId());
								if(channelId!=null) {
									headers.set("channelId", channelId.toString());
								url = URI.create(Global_Scheduling_URI + "/doctor/overlappingschedule");
								HttpEntity <List<DoctorScheduleSyncDto>> request = new HttpEntity<>(finalData, headers);
								String stringResponse = restTemplate.postForObject(url, request, String.class);
								
								commonResponse = gson.fromJson(stringResponse, Response.class);
								Double count = (Double) commonResponse.getObject();
								if(commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS) && count > 0)
								{
									return new Response(ERROR, ALREADY_EXIST_CODE, "There are some overlapping global schedules found for selected doctor...!!", null, 'N');
								}
								}
								}
								}catch (Exception e) {
								logger.error("generateSlotsDoctorSheduling failed", e);
								}
								
							}
						}
						}
						
					}
					
					/*if(slotMasterDto.getOverride()!=null && slotMasterDto.getOverride()=='Y')
					{
						iSlotMasterDao.getOldSlotDetails(slotMasterDtosObj, slotMasterDto.getDoctorArrayList(),
								slotMasterDto.getUnitId(), slotMasterDto.getSlotTypeId(),'Y');
					}else{
						Response res = iSlotMasterDao.getOldSlotDetails(slotMasterDtosObj, slotMasterDto.getDoctorArrayList(),
								slotMasterDto.getUnitId(), slotMasterDto.getSlotTypeId(),'N');
						if(res.getStatus().equals(ERROR))
						{
							return res;
						}
					}*/
					
					
						// Generating list of time slots
						timeSlotsList = slotsCreationService.getTimeSlot(slotMasterDtosObj.getFromTime().toString(),
								slotMasterDtosObj.getToTime().toString(), 
								slotMasterDtosObj.getTimePerSlot());
						
						if((timeSlotsList.isEmpty())){ 
							 return new Response(ERROR, null, "Please enter accurate slot time !!", null, null); 
						}
						// checking whether the break time list is blank 
						
						
							TimeSlotsDto timeSlotsDto = null; 
							for (int k = 0; k < (timeSlotsList.size() - 1); k++) 
							{
								timeSlotsDto = new TimeSlotsDto();
								timeSlotsDto.setFromTime(timeSlotsList.get(k));
								timeSlotsDto.setToTime(timeSlotsList.get(k + 1));
								// checking slots are in between break time or not
									
									
								if(slotMasterDtosObj.getBreakSlotsList().getFirst().getBreakFromTime()!="" && slotMasterDtosObj.getBreakSlotsList().getFirst().getBreakToTime()!="")	
								{
									if (slotsCreationService.isSlotIsBetweenBreakTime(slotMasterDtosObj.getBreakSlotsList(),timeSlotsDto) == true)
									timeSlotsDtos.add(timeSlotsDto);
								}
								else
									timeSlotsDtos.add(timeSlotsDto);
							}
						
						//generating list of dates excluding holidays
						datesMasterList = slotsCreationService.getListOfDatesAsPerDayBetweenTwoDates(
								slotMasterDtosObj.getFromDate(), 
								slotMasterDtosObj.getToDate(),
								slotMasterDtosObj.getDayArrayList(), 
								slotMasterDto.getOrganizationId(),
								slotMasterDto.getUnitId());
	
						
						ListIterator doctorItr = slotMasterDto.getDoctorArrayList().listIterator();
						while (doctorItr.hasNext()) 
						{
							DoctorMasterDto doctorMasterDto = (DoctorMasterDto) doctorItr.next();
								if(slotMasterDto.getSlotTypeId()==1){
									docId = doctorMasterDto.getDoctorId();
									speId = doctorMasterDto.getSpecialityId();
								}
								if(slotMasterDto.getSlotTypeId()==2){
									modalityId=doctorMasterDto.getModalityId();
									subSpecilaityId=doctorMasterDto.getSubSpecialityId();
									speId = doctorMasterDto.getSpecialityId();
								}
								if(slotMasterDto.getSlotTypeId()==4){
									speId = doctorMasterDto.getSpecialityId();
								}
								
							ListIterator superItr = datesMasterList.listIterator();	//Iterator for list of dto's
							ListIterator childItr = null; //Iterator for days and dates from DayMasterDto
							while (superItr.hasNext()) 
							{
								List<List<DayMasterDto>> childList = (List<List<DayMasterDto>>) superItr.next();
	
								childItr = childList.listIterator();
								while (childItr.hasNext()) 
								{
									DayMasterDto data = (DayMasterDto) childItr.next();
										String date = data.getDay();
										Integer dayId = data.getDayId();
	
									ListIterator timeSlotsDtosItrList = timeSlotsDtos.listIterator();
									while (timeSlotsDtosItrList.hasNext()) 
									{
	
										TimeSlotsDto timeSlotsDto2 = (TimeSlotsDto) timeSlotsDtosItrList.next();
	
										SlotMasterDto slotMasterDtoNew = new SlotMasterDto();
											slotMasterDtoNew.setOrganizationId(slotMasterDto.getOrganizationId());
											slotMasterDtoNew.setUnitId(slotMasterDto.getUnitId());
											slotMasterDtoNew.setSpecialityId(speId);
											slotMasterDtoNew.setDoctorId(docId);
											slotMasterDtoNew.setFromDate(date);// appointmentDate
											slotMasterDtoNew.setDayId(dayId);
											slotMasterDtoNew.setFromTime(timeSlotsDto2.getFromTime());
											slotMasterDtoNew.setToTime(timeSlotsDto2.getToTime());
											slotMasterDtoNew.setCreatedBy(slotMasterDto.getCreatedBy());
											slotMasterDtoNew.setUpdatedBy(slotMasterDto.getUpdatedBy());
											slotMasterDtoNew.setCreatedDate(slotMasterDto.getCreatedDate());
											slotMasterDtoNew.setUpdatedDate(slotMasterDto.getUpdatedDate());
											slotMasterDtoNew.setStatus('A');
											slotMasterDtoNew.setSlotTypeId(slotMasterDto.getSlotTypeId());
											slotMasterDtoNew.setModalityId(modalityId);
											slotMasterDtoNew.setVisitTypeId(slotMasterDtosObj.getVisitTypeId());
											slotMasterDtoNew.setSubSpecialityId(subSpecilaityId);
											slotMasterDtoNew.setIsBlocked(2);	//unblocked
										/*Response res = iSlotMasterDao.isSlotAlreadyExist(slotMasterDtoNew);	
										Character yseNo = (Character) res.getObject();
											
										if(yseNo.equals('N'))*/
											
										listForSaveCall.add(slotMasterDtoNew);
										
											
									}
								}
							}
						}
						
						iSlotMasterDao.generateSlots(listForSaveCall,slotMasterDto.getSlotTypeId());
					}
//				}
			 
			 if(slotMasterDto.getSlotTypeId()==1){
				 calculateTimeslots(slotMasterDto);
			 }
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	}catch (Exception e) {
	logger.error("generateSlotsDoctorSheduling failed", e);
	}
	return null;
		} 


	//Function for calculate timeslots for sych to global
		public Response calculateTimeslotsDoctor(SlotMasterDto slotMasterDto){	
			try{			
				
				
				TimeSlotsDto timeSlotDto = null;	
				Integer docId = null, speId = null, modalityId = null, subSpecilaityId=null;		  
				ListIterator doctorItr = slotMasterDto.getDoctorArrayList().listIterator();	
				
				while (doctorItr.hasNext()) 	
				{	
					DoctorMasterDto doctorMasterDto = (DoctorMasterDto) doctorItr.next();	
					if(slotMasterDto.getSlotTypeId()==1){	
						docId = doctorMasterDto.getDoctorId();	
						speId = doctorMasterDto.getSpecialityId();	
						}	
					if(slotMasterDto.getSlotTypeId()==2){
						modalityId=doctorMasterDto.getModalityId();
						subSpecilaityId=doctorMasterDto.getSubSpecialityId();
						speId = doctorMasterDto.getSpecialityId();	
						}	
				ListIterator unitItr = slotMasterDto.getUnitArrayList().listIterator();	
				
				while (unitItr.hasNext()) 	
				{
					
					DoctorMasterDto doctorMasterDto55 = (DoctorMasterDto) unitItr.next();
				  if(docId.equals(doctorMasterDto55.getDoctorId())) {
				Iterator slotMstDtoIterator = slotMasterDto.getSlotMasterDtosList().iterator();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");	
				while(slotMstDtoIterator.hasNext())
				{	
					//iterate through main list	
					List<TimeSlotsDto> listTimeSlotDtos = new ArrayList<>();	
					SlotMasterDto tempSlotMasterDto = (SlotMasterDto) slotMstDtoIterator.next();	
					
					List<Integer> dayIdsForGDS = new LinkedList<>();
					for(Integer dayId :tempSlotMasterDto.getDayArrayList())
					{
						switch (dayId) {
							
							case 1: dayIdsForGDS.add(2);
									break;
							case 2: dayIdsForGDS.add(3);
									break;
							case 3: dayIdsForGDS.add(4);
									break;
							case 4: dayIdsForGDS.add(5);
									break;
							case 5: dayIdsForGDS.add(6);
									break;
							case 6: dayIdsForGDS.add(7);
									break;
							case 7: dayIdsForGDS.add(1);
									break;		
						}
					}
					
					Date currentDate = TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId());
					String fromDateString = tempSlotMasterDto.getFromDate().substring(0,10);
					Date startDDate = GlobalCommonDateUtils.getDate(fromDateString,"yyyy-MM-dd");
					if(startDDate.compareTo(currentDate) < 0) 
					{
						fromDateString = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()),"yyyy-MM-dd");
					} 
					
					Date fromDate = dateFormat.parse(fromDateString);	
					Date toDate = dateFormat.parse(tempSlotMasterDto.getToDate());

					Instant fromTime = tempSlotMasterDto.getFromTime();
					Instant toTime = tempSlotMasterDto.getToTime();
					
					if(tempSlotMasterDto.getBreakSlotsList().getFirst().getBreakFromTime()!="" && tempSlotMasterDto.getBreakSlotsList().getFirst().getBreakToTime()!="")	
					{					
						Iterator breakTimeSlotIte = tempSlotMasterDto.getBreakSlotsList().iterator();	
						//MultiValueMap mValueMap = new LinkedMultiValueMap<>();	
						Instant bEndTime = null;
						while(breakTimeSlotIte.hasNext())
						{	
							TimeSlotsDto breakTimeslotDto =(TimeSlotsDto) breakTimeSlotIte.next();

							LocalTime bFromTime1 = LocalTime.parse(breakTimeslotDto.getBreakFromTime());
							LocalDate today = LocalDate.now();
							Instant bFromTime = bFromTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

							LocalTime bToTime1 = LocalTime.parse(breakTimeslotDto.getBreakToTime());
							Instant bToTime = bToTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

							if(bFromTime.isAfter(fromTime)&& bFromTime.isBefore(toTime)&& bToTime.isAfter(fromTime)&& bToTime.isBefore(toTime))
							{
								timeSlotDto = new TimeSlotsDto();
								if(bEndTime!=null)
								{	
									timeSlotDto.setFromTime(bEndTime.toString());
									timeSlotDto.setToTime(bFromTime.toString());	
								}	
								else
								{	
									timeSlotDto.setFromTime(fromTime.toString());
									timeSlotDto.setToTime(bFromTime.toString());
								}	
									bEndTime = bToTime;
									listTimeSlotDtos.add(timeSlotDto);
								}else{					
									return new Response(ERROR, COMMON_ERROR_CODE, "Wrong Break Time selection!!", null, null);			
								}						
							if(bEndTime!=null)
							{	
								timeSlotDto = new TimeSlotsDto();						
								timeSlotDto.setFromTime(bEndTime.toString());
								timeSlotDto.setToTime(toTime.toString());
								listTimeSlotDtos.add(timeSlotDto);
								//mValueMap.add(bEndTime,toTime);
								}											
							}//END BREAKTIME LIST ITERATOR				
					}else
					{					
						timeSlotDto = new TimeSlotsDto();				
						timeSlotDto.setFromTime(fromTime.toString());		
						timeSlotDto.setToTime(toTime.toString());		
						listTimeSlotDtos.add(timeSlotDto);			
					}				
																												
					//LOOP THROUGH FROM_DATE & TO_DATE				
					Calendar startCalender = Calendar.getInstance();			
					startCalender.setTime(fromDate);			   
					
					Calendar endCalendar = Calendar.getInstance();			  
					endCalendar.setTime(toDate);			    			
					final Calendar current = Calendar.getInstance();			    
					current.setTime(fromDate);
					
					List<ScheduleMasterDto> scheduleMasterDtosList = new LinkedList<>();
					while (!current.getTime().after(toDate))				    
					{	
						
						for(TimeSlotsDto tsDto : listTimeSlotDtos)
						{		
							Calendar calendar = Calendar.getInstance();
					        calendar.setTime(GlobalCommonDateUtils.getDate(current.getTime(), "yyyy-MM-dd"));
							if(dayIdsForGDS.contains(calendar.get(Calendar.DAY_OF_WEEK)))
							{
								ScheduleMasterDto dto = new ScheduleMasterDto();	
									dto.setDoctorId(docId);				    	
									dto.setSpecialityId(speId);				    	
									dto.setScheduleDate(GlobalCommonDateUtils.getStringDate(current.getTime(), "yyyy-MM-dd"));	
									dto.setFromTime(tsDto.getFromTime().toString());				    
									dto.setToTime(tsDto.getToTime().toString());	
									dto.setCreatedBy(slotMasterDto.getCreatedBy());
									dto.setOrganisationId(slotMasterDto.getOrganizationId());				 
									dto.setUnitId(doctorMasterDto55.getUnitId());
									dto.setTimePerSlot(tempSlotMasterDto.getTimePerSlot());
									dto.setAddRemove(1);
								scheduleMasterDtosList.add(dto);
							}
						}				    	
						current.add(Calendar.DATE, 1);				  
					}
					iDoctorScheduleDao.saveDoctorSchedule(scheduleMasterDtosList,"INSERT");				
					
					  }
				     }
				   }
				}		
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);		
				}catch(Exception e){
				logger.error("calculateTimeslotsDoctor failed", e);
				}		
		return null;
		}
	

	
	@Override
	public Response generateSlotsForECH(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			List<List<DayMasterDto>> datesMasterList = new ArrayList<>();
			List<SlotMasterDto> listForSaveCall = new LinkedList<>();
			for (int i = 0; i < slotMasterDto.getSlotMasterDtosList().size(); i++) {
				Iterator<SlotMasterDto> itr = slotMasterDto.getSlotMasterDtosList().iterator();

				while (itr.hasNext()) {

					SlotMasterDto slotMasterDtosObj = itr.next();

					// Generating list of dates excluding holidays
					datesMasterList = slotsCreationService.getListOfDatesAsPerDayBetweenTwoDates(
							slotMasterDtosObj.getFromDate(), slotMasterDtosObj.getToDate(),
							slotMasterDtosObj.getDayArrayList(), slotMasterDto.getOrganizationId(),
							slotMasterDto.getUnitId());

					ListIterator superItr = datesMasterList.listIterator();	// Iterator for list of dto's
					ListIterator childItr = null; // Iterator for child list for retrieving days and dates from superItr.
					while (superItr.hasNext()) {
						List<List<DayMasterDto>> childList = (List<List<DayMasterDto>>) superItr.next();

						childItr = childList.listIterator();
						while (childItr.hasNext()) {
							DayMasterDto data = (DayMasterDto) childItr.next();
								String date = data.getDay();
								Integer dayId = data.getDayId();
							// Iterating loop till number of slots	
							for (int j = 0; j < slotMasterDtosObj.getNoOfSlots(); j++) {
								SlotMasterDto slotMasterDtoNew = new SlotMasterDto();
									slotMasterDtoNew.setOrganizationId(slotMasterDto.getOrganizationId());
									slotMasterDtoNew.setUnitId(slotMasterDto.getUnitId());
									slotMasterDtoNew.setFromDate(date);// AppointmentDate
									slotMasterDtoNew.setDayId(dayId);
									slotMasterDtoNew.setCreatedBy(slotMasterDto.getCreatedBy());
									slotMasterDtoNew.setUpdatedBy(slotMasterDto.getUpdatedBy());
									slotMasterDtoNew.setCreatedDate(slotMasterDto.getCreatedDate());
									slotMasterDtoNew.setUpdatedDate(slotMasterDto.getUpdatedDate());
									slotMasterDtoNew.setStatus('A');
									slotMasterDtoNew.setSlotTypeId(slotMasterDto.getSlotTypeId());
									/*Response res = iSlotMasterDao.isSlotAlreadyExist(slotMasterDtoNew);	
									Character yseNo = (Character) res.getObject();

									if(yseNo.equals('N'))*/
									listForSaveCall.add(slotMasterDtoNew);
							}
						}
					}

				}
			}
			iSlotMasterDao.generateSlots(listForSaveCall,slotMasterDto.getSlotTypeId());
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
		logger.error("generateSlotsForECH failed", e);
			return new Response(ERROR, null, null, null, null);
		}
	}
	
	@Override
	@Transactional
	public Response getSlots(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iSlotMasterDao.getSlots(slotMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response getSlotCount(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iSlotMasterDao.getSlotCount(slotMasterDto);
		} catch (Exception e) {
		logger.error("getSlotCount failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSlotDetails(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iSlotMasterDao.getSlotDetails(slotMasterDto);
		} catch (Exception e) {
		logger.error("getSlotDetails failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateSlotStatus(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iSlotMasterDao.updateSlotStatus(slotMasterDto);
		} catch (Exception e) {
		logger.error("updateSlotStatus failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response updateStatusOfSlot(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iSlotMasterDao.updateStatusOfSlot(slotMasterDto);
		} catch (Exception e) {
		logger.error("updateStatusOfSlot failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getMonthlySlots(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {

			DateTime dt = new DateTime().withYear(slotMasterDto.getYear()).withMonthOfYear(slotMasterDto.getMonth());
			
			DateTime start = dt.withDayOfMonth(1).withTimeAtStartOfDay();
			DateTime end = start.plusMonths(1).minusMillis(1);
			
			slotMasterDto.setFromDate(start.toString().substring(0, 10));
			slotMasterDto.setToDate(end.toString().substring(0, 10));
			List<SlotMasterDto> appointmentsForMonthsList = (List<SlotMasterDto>)iSlotMasterDao.getAppointmentsForMonth(slotMasterDto);
			
			List<String> finalDateList = new LinkedList<>();
			while (!start.isAfter(end)) {

				int nameOfWeekday = start.getDayOfWeek();
				if(slotsCreationService.isSlotAvailble(appointmentsForMonthsList,start.toString().substring(0,10))==true)
					finalDateList.add(nameOfWeekday+"_"+start.toString().substring(0,10)+"_Y");
				else
					finalDateList.add(nameOfWeekday+"_"+start.toString().substring(0,10)+"_N");
			    
				start = start.plusDays(1);
			
			}
			
			List<List<String>> finalList=new LinkedList<>();
			List<String> newList=new LinkedList<>();
			ListIterator<String> itr=finalDateList.listIterator();
			int index=0;
			while(itr.hasNext())
			{
				String addData=itr.next();
				Integer i = (Integer.parseInt(addData.substring(0,1)));
				newList.add(addData);
				index++;
				if(i==7 || (index==finalDateList.size())){
					finalList.add(newList);
					newList=new LinkedList<>();
				}
				
			}

			return new Response<>(SUCCESS,null,null,finalList,null);
		} catch (Exception e) {
		logger.error("getMonthlySlots failed", e);
		}
		return new Response<>(ERROR,null,null,null,null);
	}

	@Override
	public Response getYearlySlots(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iSlotMasterDao.getYearlySlots(slotMasterDto);
		} catch (Exception e) {
		logger.error("getYearlySlots failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDailySlots(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			
			Integer slotId=0;
			WeeklySlotsDto masterDto = null;
			List<WeeklySlotsDto> slotMasterDtoFinalList= new LinkedList<>();
			Response weeklySlotsRes =  iSlotMasterDao.getDailySlots(slotMasterDto);
			List<SlotMasterDto> weeklySlotsList =  weeklySlotsRes.getListObject();
			List<SlotPatientDetails> details2 = new LinkedList<>();
			
			
			ListIterator<SlotMasterDto> iterator = weeklySlotsList.listIterator(); //main list
			while(iterator.hasNext())
			{
				SlotMasterDto iteratorObj = iterator.next();
				
				if(slotId.equals(iteratorObj.getSlotId()))
				{
					SlotPatientDetails patientDetails2 = new SlotPatientDetails();
						patientDetails2.setAppointmentId(iteratorObj.getAppointmentId());
						patientDetails2.setPatientId(iteratorObj.getPatientId());
						patientDetails2.setPatientName(iteratorObj.getPatientName());
						patientDetails2.setAddress(iteratorObj.getAddress());
						patientDetails2.setEmail(iteratorObj.getEmail());
						patientDetails2.setMobileNumber(iteratorObj.getMobileNumber());
						patientDetails2.setUhidNumber(iteratorObj.getUhidNumber());	
						patientDetails2.setIsVip(iteratorObj.getIsVip());
						
					details2.add(patientDetails2);
					masterDto.setSlotPatientDetailsList(details2);	
					
					int lastEntry = slotMasterDtoFinalList.size();
					slotMasterDtoFinalList.remove(lastEntry-1);
					
					masterDto.setSlotPatientDetailsListsSize(details2.size());
					slotMasterDtoFinalList.add(masterDto);
					
				}
				else
				{
					WeeklySlotsDto newSlotMasterDtoObj = new WeeklySlotsDto();
						newSlotMasterDtoObj.setSlotId(iteratorObj.getSlotId());
						newSlotMasterDtoObj.setSpecialityId(iteratorObj.getSpecialityId());
						newSlotMasterDtoObj.setDoctorId(iteratorObj.getDoctorId());
						newSlotMasterDtoObj.setAppointmentDate(iteratorObj.getAppointmentDate());
						newSlotMasterDtoObj.setAppointmentDate2(iteratorObj.getAppointmentDate2());
						newSlotMasterDtoObj.setFromTimeString(iteratorObj.getFromTimeString());
						newSlotMasterDtoObj.setFromTimeString2(iteratorObj.getFromTimeString2());
						newSlotMasterDtoObj.setToTimeString(iteratorObj.getToTimeString());
						newSlotMasterDtoObj.setToTimeString2(iteratorObj.getToTimeString2());
						newSlotMasterDtoObj.setDoctorName(iteratorObj.getDoctorName());
						newSlotMasterDtoObj.setRegistrationNumber(iteratorObj.getRegistrationNumber());
						newSlotMasterDtoObj.setSpecialityName(iteratorObj.getSpecialityName());
						newSlotMasterDtoObj.setSlotStatusId(iteratorObj.getSlotStatusId());
						newSlotMasterDtoObj.setIsBlockedUnblocked(iteratorObj.getIsBlockedUnblocked());
						newSlotMasterDtoObj.setOriginalFromTime(iteratorObj.getOriginalFromTime());
						newSlotMasterDtoObj.setSlotTypeId(iteratorObj.getSlotTypeId());
						newSlotMasterDtoObj.setIsVip(iteratorObj.getIsVip()!=null ? iteratorObj.getIsVip() : 'N');
						newSlotMasterDtoObj.setAllowedOverBookingSlots(iteratorObj.getAllowedOverBookingSlots());
						if(iteratorObj.getAppointmentId()!=null)
						{
							SlotPatientDetails patientDetails2 = new SlotPatientDetails();
								patientDetails2.setAppointmentId(iteratorObj.getAppointmentId());
								patientDetails2.setPatientId(iteratorObj.getPatientId());
								patientDetails2.setPatientName(iteratorObj.getPatientName());
								patientDetails2.setAddress(iteratorObj.getAddress());
								patientDetails2.setEmail(iteratorObj.getEmail());
								patientDetails2.setMobileNumber(iteratorObj.getMobileNumber());
								patientDetails2.setUhidNumber(iteratorObj.getUhidNumber());	
								patientDetails2.setIsVip(iteratorObj.getIsVip());
								
							List<SlotPatientDetails> details = new LinkedList<>();
							details.add(patientDetails2);
							newSlotMasterDtoObj.setSlotPatientDetailsList(details);	
							details2=details;
							newSlotMasterDtoObj.setSlotPatientDetailsListsSize(details.size());
						}
						else
						{
							newSlotMasterDtoObj.setSlotPatientDetailsListsSize(0);
						}
						slotMasterDtoFinalList.add(newSlotMasterDtoObj);
					
						slotId=iteratorObj.getSlotId();
						masterDto=newSlotMasterDtoObj;
					
				}		
			}
			return new Response(SUCCESS, null, null, slotMasterDtoFinalList, null);
			
			
		} catch (Exception e) {
		logger.error("getDailySlots failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWeeklySlots(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			Integer slotId=0;
			WeeklySlotsDto masterDto = null;
			List<WeeklySlotsDto> slotMasterDtoFinalList= new LinkedList<>();
			Response weeklySlotsRes =  iSlotMasterDao.getWeeklySlots(slotMasterDto);
			List<SlotMasterDto> weeklySlotsList =  weeklySlotsRes.getListObject();
			List<SlotPatientDetails> details2 = new LinkedList<>();
			
			
			ListIterator<SlotMasterDto> iterator = weeklySlotsList.listIterator(); //main list
			while(iterator.hasNext())
			{
				SlotMasterDto iteratorObj = iterator.next();
				
				if(slotId.equals(iteratorObj.getSlotId()))
				{
					SlotPatientDetails patientDetails2 = new SlotPatientDetails();
						patientDetails2.setAppointmentId(iteratorObj.getAppointmentId());
						patientDetails2.setPatientId(iteratorObj.getPatientId());
						patientDetails2.setPatientName(iteratorObj.getPatientName());
						patientDetails2.setAddress(iteratorObj.getAddress());
						patientDetails2.setEmail(iteratorObj.getEmail());
						patientDetails2.setMobileNumber(iteratorObj.getMobileNumber());
						patientDetails2.setUhidNumber(iteratorObj.getUhidNumber());	
						patientDetails2.setIsVip(iteratorObj.getIsVip());
						
					details2.add(patientDetails2);
					masterDto.setSlotPatientDetailsList(details2);	
					
					int lastEntry = slotMasterDtoFinalList.size();
					slotMasterDtoFinalList.remove(lastEntry-1);
					
					masterDto.setSlotPatientDetailsListsSize(details2.size());
					slotMasterDtoFinalList.add(masterDto);
					
				}
				else
				{
				WeeklySlotsDto newSlotMasterDtoObj = new WeeklySlotsDto();
					newSlotMasterDtoObj.setSlotId(iteratorObj.getSlotId());
					newSlotMasterDtoObj.setSpecialityId(iteratorObj.getSpecialityId());
					newSlotMasterDtoObj.setDoctorId(iteratorObj.getDoctorId());
					newSlotMasterDtoObj.setAppointmentDate(iteratorObj.getAppointmentDate());
					newSlotMasterDtoObj.setAppointmentDate2(iteratorObj.getAppointmentDate2());
					newSlotMasterDtoObj.setFromTimeString(iteratorObj.getFromTimeString());
					newSlotMasterDtoObj.setFromTimeString2(iteratorObj.getFromTimeString2());
					newSlotMasterDtoObj.setToTimeString(iteratorObj.getToTimeString());
					newSlotMasterDtoObj.setToTimeString2(iteratorObj.getToTimeString2());
					newSlotMasterDtoObj.setDoctorName(iteratorObj.getDoctorName());
					newSlotMasterDtoObj.setSpecialityName(iteratorObj.getSpecialityName());
					newSlotMasterDtoObj.setSlotStatusId(iteratorObj.getSlotStatusId());
					newSlotMasterDtoObj.setIsBlockedUnblocked(iteratorObj.getIsBlockedUnblocked());
					newSlotMasterDtoObj.setOriginalFromTime(iteratorObj.getOriginalFromTime());
					newSlotMasterDtoObj.setDayId(iteratorObj.getDayId());
					newSlotMasterDtoObj.setDay(iteratorObj.getDay());
					newSlotMasterDtoObj.setIsVip(iteratorObj.getIsVip()!=null ? iteratorObj.getIsVip() : 'N');
					newSlotMasterDtoObj.setAllowedOverBookingSlots(iteratorObj.getAllowedOverBookingSlots());
					if(iteratorObj.getAppointmentId()!=null)
					{
						SlotPatientDetails patientDetails2 = new SlotPatientDetails();
							patientDetails2.setAppointmentId(iteratorObj.getAppointmentId());
							patientDetails2.setPatientId(iteratorObj.getPatientId());
							patientDetails2.setPatientName(iteratorObj.getPatientName());
							patientDetails2.setAddress(iteratorObj.getAddress());
							patientDetails2.setEmail(iteratorObj.getEmail());
							patientDetails2.setMobileNumber(iteratorObj.getMobileNumber());
							patientDetails2.setUhidNumber(iteratorObj.getUhidNumber());	
							patientDetails2.setIsVip(iteratorObj.getIsVip());
							
						List<SlotPatientDetails> details = new LinkedList<>();
						details.add(patientDetails2);
						newSlotMasterDtoObj.setSlotPatientDetailsList(details);	
						details2=details;
						newSlotMasterDtoObj.setSlotPatientDetailsListsSize(details.size());
					}
					else
					{
						newSlotMasterDtoObj.setSlotPatientDetailsListsSize(0);
					}
					slotMasterDtoFinalList.add(newSlotMasterDtoObj);
				
					slotId=iteratorObj.getSlotId();
					masterDto=newSlotMasterDtoObj;
					
					
				}		
			}
			return new Response(SUCCESS, null, null, slotMasterDtoFinalList, weeklySlotsRes.getObject());
			
			//return iSlotMasterDao.getWeeklySlots(slotMasterDto);
		} catch (Exception e) {
		logger.error("getWeeklySlots failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response blockUnblockDoctorSlots(BlockSlotReqDto blockSlotReqDto)
			throws ApplicationException {
		try{
			Response chkSlotResponse  = iSlotMasterDao.checkSlotAlreadyBookedOrNot(blockSlotReqDto);
			if(chkSlotResponse.getListObject()!=null && chkSlotResponse.getListObject().size()>0){
				return chkSlotResponse;
			}
			
			Response updateSlotResponse  = iSlotMasterDao.blockUnblockSlots(blockSlotReqDto);
			if(updateSlotResponse.getStatus()==SUCCESS)
			{
				Response updateSlotResponse2 = iSlotBlockUnblockDetailDao.saveUpdatePrevSlotBlockDetails(blockSlotReqDto);
			
				if(updateSlotResponse2.getStatus()==SUCCESS)
				{
					iSlotBlockUnblockDetailDao.saveSlotBlockDetails(blockSlotReqDto);
				}
				
			}	
			return updateSlotResponse;
		}catch(Exception e){
		logger.error("blockUnblockDoctorSlots failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSlotsForBlockUnblock(
			SlotBlockUnblockRequestDto slotReqDto) throws ApplicationException {
		try {
			return iSlotMasterDao.getSlotsForBlockUnblock(slotReqDto);
		} catch (Exception e) {
		logger.error("getSlotsForBlockUnblock failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBlockedUnblockedSlotDetails(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iSlotMasterDao.getBlockedUnblockedSlotDetails(slotMasterDto);
		} catch (Exception e) {
		logger.error("getBlockedUnblockedSlotDetails failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public  Response isOverBookingAllowedOrNot(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			
			boolean res = iSlotMasterDao.isOverBookingAllowedOrNot(slotMasterDto);
			return new Response(SUCCESS, null, null, null, res);
		} catch (Exception e) {
		logger.error("isOverBookingAllowedOrNot failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response deactiveSlotsDateAndDoctorWise(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iSlotMasterDao.deactiveSlotsDateAndDoctorWise(slotMasterDto);
		} catch (Exception e) {
		logger.error("deactiveSlotsDateAndDoctorWise failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response isEHCSlotAvailable(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			SlotMasterDto dto = new SlotMasterDto();
			Response res = iSlotMasterDao.isEHCSlotAvailable(slotMasterDto);
			List<SlotMasterDto> weeklySlotsList =  res.getListObject();
			List<SlotMasterDto> masterDtosList = new LinkedList<>();
			ListIterator<SlotMasterDto> iterator = weeklySlotsList.listIterator(); //main list
			while(iterator.hasNext())
			{
				SlotMasterDto iteratorObj = iterator.next();
				
				if(iteratorObj.getAppointmentId()!=null)
				{
					
				}else{
					masterDtosList.add(iteratorObj);
				}
			}
			
			if(masterDtosList.size()>0)
			{
				 dto = masterDtosList.getFirst();
				 return new Response(SUCCESS, null, null, null, dto.getSlotId());
			}else{
				return new Response(ERROR, null, "No slots available for EHC...!!", null, null);
			}
			
		} catch (Exception e) {
		logger.error("isEHCSlotAvailable failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response blockSlot(Integer slotId) throws ApplicationException {
		try {
			return iSlotMasterDao.blockSlot(slotId);
		} catch (Exception e) {
		logger.error("blockSlot failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	  @Override
	  public Response getAsset(Integer orgId) throws ApplicationException {

	    try {
	      return iSlotMasterDao.getAsset(orgId);
	    } catch (Exception e) {
	    logger.error("getAsset failed", e);
	    }
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);

	  }

	@Override
	public Response generateSlotsForHemodialysis(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			List<List<DayMasterDto>> datesMasterList = new ArrayList<>();
			List<SlotMasterDto> listForSaveCall = new LinkedList<>();
			
			// Iterating master list
			for (int i = 0; i < slotMasterDto.getSlotMasterDtosList().size(); i++) {
				Iterator<SlotMasterDto> itr = slotMasterDto.getSlotMasterDtosList().iterator();

				while (itr.hasNext()) {

					SlotMasterDto slotMasterDtosObj = itr.next();

						//generating list of dates excluding holidays
						datesMasterList = slotsCreationService.getListOfDatesAsPerDayBetweenTwoDates(
								slotMasterDtosObj.getFromDate(), 
								slotMasterDtosObj.getToDate(),
								slotMasterDtosObj.getDayArrayList(), 
								slotMasterDto.getOrganizationId(),
								slotMasterDto.getUnitId());
	
							ListIterator superItr = datesMasterList.listIterator();	//Iterator for list of dto's
							ListIterator childItr = null; //Iterator for days and dates from DayMasterDto
							while (superItr.hasNext()) {
								List<List<DayMasterDto>> childList = (List<List<DayMasterDto>>) superItr.next();
	
								childItr = childList.listIterator();
								while (childItr.hasNext()) {
									DayMasterDto data = (DayMasterDto) childItr.next();
										String date = data.getDay();
										Integer dayId = data.getDayId();
	
									for (int j = 0; j < slotMasterDtosObj.getNoOfSlots(); j++) {
										SlotMasterDto slotMasterDtoNew = new SlotMasterDto();
											slotMasterDtoNew.setOrganizationId(slotMasterDto.getOrganizationId());
											slotMasterDtoNew.setUnitId(slotMasterDto.getUnitId());
											slotMasterDtoNew.setSpecialityId(slotMasterDto.getSpecialityId());
											slotMasterDtoNew.setBedId(slotMasterDto.getBedId());
											slotMasterDtoNew.setFromDate(date);// appointmentDate
											slotMasterDtoNew.setDayId(dayId);
											slotMasterDtoNew.setCreatedBy(slotMasterDto.getCreatedBy());
											slotMasterDtoNew.setUpdatedBy(slotMasterDto.getUpdatedBy());
											slotMasterDtoNew.setCreatedDate(slotMasterDto.getCreatedDate());
											slotMasterDtoNew.setUpdatedDate(slotMasterDto.getUpdatedDate());
											slotMasterDtoNew.setStatus('A');
											slotMasterDtoNew.setSlotTypeId(slotMasterDto.getSlotTypeId());
											slotMasterDtoNew.setIsBlocked(2);	//unblocked
										listForSaveCall.add(slotMasterDtoNew);
											
									}
								}
							}
					}
				}
			 iSlotMasterDao.generateSlots(listForSaveCall,slotMasterDto.getSlotTypeId());
			//calculateTimeslots(slotMasterDto);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
		logger.error("generateSlotsForHemodialysis failed", e);
			return new Response(ERROR, null, null, null, null);
		}

	}

	@Override
	public Response getHemoSlots(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iSlotMasterDao.getHemoSlots(slotMasterDto);
		} catch (Exception e) {
		logger.error("getHemoSlots failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSlotHemoCount(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iSlotMasterDao.getSlotHemoCount(slotMasterDto);
		} catch (Exception e) {
		logger.error("getSlotHemoCount failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response deactiveSlotsWithInDateRange(SlotMasterDto slotMasterDto) {
		try {
			return iSlotMasterDao.deactiveSlotsWithInDateRange(slotMasterDto);
		} catch (Exception e) {
		logger.error("deactiveSlotsWithInDateRange failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response deleteUnusedSlots(List<Integer> unitIds) {
		try {
			return iSlotMasterDao.deleteUnusedSlots(unitIds);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response getSlotDetailsBySlotId(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iSlotMasterDao.getSlotDetailsBySlotId(slotMasterDto);
		} catch (Exception e) {
		logger.error("getSlotDetailsBySlotId failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	@Transactional
	public Response getSlotsNew(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String lastSlotDateString = "";
			List<SlotMasterDto> list = new ArrayList<>();


			List<SlotMasterDto> slotMasterDtosList = iSlotMasterDao.getSlotsNew(slotMasterDto);


				if(slotMasterDto.getSlotTypeId() == 1) {
					if ( slotMasterDtosList != null && slotMasterDtosList.size() > 0) {

						list = iSlotMasterDao.getLastAppointmentDate(slotMasterDtosList, slotMasterDto.getOrganizationId() != null ? slotMasterDto.getOrganizationId() : 0);
						if(list != null && list.size()>0){
							for (SlotMasterDto dto : slotMasterDtosList) {

								SlotMasterDto doctorIdFound = list.stream()
									.filter(order -> dto.getDoctorId().equals(order.getDoctorId()) && dto.getSpecialityId().equals(order.getSpecialityId()))
									.findAny()
									.orElse(null);

								if (doctorIdFound != null) {
									try {
										lastSlotDateString = dateFormat.format(doctorIdFound.getLastActiveSlotDate() != null ? doctorIdFound.getLastActiveSlotDate() : "00-00-00 00:00:00");
									} catch (Exception e) {
									logger.error("getSlotsNew failed", e);
									}
									dto.setLastSlotDate(!lastSlotDateString.isEmpty() ? lastSlotDateString : "");
								} else {
									dto.setLastSlotDate("");
								}
							}
						}
					}
				}
			return new Response(SUCCESS, null, null, slotMasterDtosList, null);
		} catch (Exception e) {
			throw e;
		}
	}


	@Override
	@Transactional
	public Response copyBackdatedSlots(SlotMasterDto dto) throws ApplicationException {
		try {
			return iSlotMasterDao.copyBackdatedSlots(dto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	public Response saveBackDatedSlotsToSlotLogs(List<SlotMasterDto> dto) throws ApplicationException {
		try {
			return iSlotMasterDao.saveBackDatedSlotsToSlotLogs(dto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
		}
	}


	@Override
	public Response deleteBackdatedSlotsFromSlotMaster(List<SlotMasterDto> dto) throws ApplicationException {
		try {
			List<Integer> slotIdList = new ArrayList<>();
			System.out.println("service deleteBackdatedSlotsFromSlotMaster");
			for (SlotMasterDto slot : dto) {
				slotIdList.add(slot.getSlotId());
			}
			return iSlotMasterDao.deleteBackdatedSlotsFromSlotMaster(slotIdList);
		} catch (Exception e) {
		logger.error("deleteBackdatedSlotsFromSlotMaster failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
}
