package com.param.batchjobs.scheduler;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.param.global.common.IServiceIdentityDao;
import com.param.global.common.ServerIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.param.batchjobs.service.IBatchDao;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.SlotMasterDto;
import com.param.global.mail.IMailingService;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.global.service.IMessageQueueService;
import com.param.opd.unit.service.ISlotMasterService;
import com.param.utility.TimeZoneComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@SuppressWarnings({ "deprecation", "rawtypes" ,"unchecked","unused"})
@Service
public class RevertAllocatedQuantity {

	private static final Logger logger = LoggerFactory.getLogger(RevertAllocatedQuantity.class);


	@Autowired
	IWalkInSalesService iWalkInSalesService;

	@Autowired
	IBatchDao iBatchDao;
	
	@Autowired
	IMessageQueueService iMessageQueueService;
	
	@Autowired
	MidNightBatchWithTimeZone midNightBatchWithTimeZoneService;
	
	@Autowired
	ISlotMasterService iSlotMasterService;
	
	@Autowired
	IMailingService iMailingService;

	@Autowired
	IServiceIdentityDao iServiceIdentityDao;

	@Autowired
	private ServerIdentityService serverIdentityService;
	
	static final String croneString = "0 0/30 * * * ?";
	// Trigger
	int MIN_STOCK_QTY=1;
	// Execution Day
	String WEEKLY="Monday";
	int MONTHLY=1;
	int YEARLY=1;
	String WEEKLYEXECUTION="Weekly";
	String MONTHLYEXECUTION="Monthly";
	String DAILYEXECUTION="Daily";
	String YEARLYEXECUTION="Yearly";


	@Scheduled(fixedDelayString = "80000")
	@Transactional
	public void saveServiceIdentity(){
		try {
			String serverId = serverIdentityService.getServerId();
			iServiceIdentityDao.saveServiceId(serverId);
			Boolean check = iServiceIdentityDao.checkServiceIdAvailable(serverId);
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	//need to add 30min timer 0 0/30 * * * ?
		@Scheduled(cron = croneString)
		@Transactional
		public void revertQuantity() {
			try {

				String serverId = serverIdentityService.getServerId();
				Boolean check = iServiceIdentityDao.checkServiceIdAvailable(serverId);

				Map<String, List<Integer>> timeZoneAndUnitsMap = TimeZoneComponent.getUnitTimeZoneList();
				if (timeZoneAndUnitsMap.size() > 0 && check)
				{
					for (Map.Entry<String, List<Integer>> entry : timeZoneAndUnitsMap.entrySet()) 
					{
						Date currentZoneDate = TimeZoneComponent.getDateByzone(entry.getValue().get(0));
						Integer zoneDateMin = currentZoneDate.getMinutes();
						Integer zoneDateHr = currentZoneDate.getHours();

						if (zoneDateHr == 02 && zoneDateMin >= 05 && zoneDateMin <= 55) 
						{
							iBatchDao.batchJobToUpdateAppointmentStatus();
						}
						
						if (zoneDateHr == 14 && zoneDateMin >= 05 && zoneDateMin <= 55) 
						{
							iBatchDao.batchJobToUpdateAppointmentStatus();
						}
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}
		}


	@Scheduled(fixedDelayString = "300000")
	public void sendMessageFromQueqe(){
		try {
			String serverId = serverIdentityService.getServerId();
//			Boolean check = iServiceIdentityDao.checkServiceIdAvailable(serverId);
//			if(check){
				int email = 1;
				int textMegs = 3;
				int whatsAppMegs = 2;
				iMessageQueueService.getMessageFromQueqe(email);
				iMessageQueueService.getMessageFromQueqe(textMegs);
				iMessageQueueService.getMessageFromQueqe(whatsAppMegs);
//			}

		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}




	//need to add 30min timer 0 0/30 * * * ?
	@Scheduled(cron = croneString)
	@Transactional
	public void midNightBatch() {
		try {
			Map<String, List<Integer>> timeZoneAndUnitsMap = TimeZoneComponent.getUnitTimeZoneList();
			String serverId = serverIdentityService.getServerId();
			Boolean check = iServiceIdentityDao.checkServiceIdAvailable(serverId);
			if (timeZoneAndUnitsMap.size() > 0 && check)
			{
				for (Map.Entry<String, List<Integer>> entry : timeZoneAndUnitsMap.entrySet()) 
				{
					
					Date currentZoneDate = TimeZoneComponent.getDateByzone(entry.getValue().get(0));
					Integer zoneDateMin = currentZoneDate.getMinutes();
					Integer zoneDateHr = currentZoneDate.getHours();
					if (zoneDateHr == 00 && zoneDateMin >= 05 && zoneDateMin <= 55) 
					{
						midNightBatchWithTimeZoneService.saveLog(entry.getKey(), entry.getValue(),"IN at 00 5 AM midNightBatch ");
						midNightBatchWithTimeZoneService.midnightBatch(entry.getKey(), entry.getValue());
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}



	//need to add 30min timer 0 0/30 * * * ?
	@Scheduled(cron = croneString)
	@Transactional
	public void roomRentMidNightBatch() {
		try {
			Map<String, List<Integer>> timeZoneAndUnitsMap = TimeZoneComponent.getUnitTimeZoneList();
			String serverId = serverIdentityService.getServerId();
			Boolean check = iServiceIdentityDao.checkServiceIdAvailable(serverId);
			if (timeZoneAndUnitsMap.size() > 0 && check)
			{
				for (Map.Entry<String, List<Integer>> entry : timeZoneAndUnitsMap.entrySet()) 
				{
					Date currentZoneDate = TimeZoneComponent.getDateByzone(entry.getValue().get(0));
					Integer zoneDateMin = currentZoneDate.getMinutes();
					Integer zoneDateHr = currentZoneDate.getHours();
					if (zoneDateHr == 00 && zoneDateMin >= 05 && zoneDateMin <= 55) 
					{
						midNightBatchWithTimeZoneService.saveLog(entry.getKey(), entry.getValue(),"IN at 00 5 AM postRoomRent ");
						midNightBatchWithTimeZoneService.postRoomRent(entry.getKey(), entry.getValue());
						
						Format dayFormat = new SimpleDateFormat("EEEE"); 
						String zoneDay = dayFormat.format(currentZoneDate);
	                    Integer zoneDate = currentZoneDate.getDate();
	                    Integer zoneMonth = currentZoneDate.getMonth()+1;
						midNightBatchWithTimeZoneService.triggerScheduledMailsDaily(DAILYEXECUTION, entry.getValue());
						if(WEEKLY.equalsIgnoreCase(zoneDay))
						{
							midNightBatchWithTimeZoneService.triggerScheduledMailsWeekly(WEEKLYEXECUTION, entry.getValue());
						}
						 if(MONTHLY==zoneDate)
						{
							midNightBatchWithTimeZoneService.triggerScheduledMailsMonthly(MONTHLYEXECUTION, entry.getValue());
						}
						 if(YEARLY==zoneMonth)
							{
								midNightBatchWithTimeZoneService.triggerScheduledMailsYearly(YEARLYEXECUTION, entry.getValue());
							}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Transactional
	public void ForceFullyMidNightBatch() {
		try {
			midNightBatchWithTimeZoneService.saveLog("", null,"IN ForceFullyMidNightBatch");
			Map<String, List<Integer>> timeZoneAndUnitsMap = TimeZoneComponent.getUnitTimeZoneList();

			if (timeZoneAndUnitsMap.size() > 0) 
			{
				for (Map.Entry<String, List<Integer>> entry : timeZoneAndUnitsMap.entrySet()) 
				{
					Date currentZoneDate = TimeZoneComponent.getDateByzone(entry.getValue().get(0));
					Integer zoneDateMin = currentZoneDate.getMinutes();
					Integer zoneDateHr = currentZoneDate.getHours();
					// in between 00:05 to 00:15
					// if(formattedTime>="00:05" && formattedTime<="00:15") {
						midNightBatchWithTimeZoneService.saveLog(entry.getKey(), entry.getValue(),"postRoomRent");
						midNightBatchWithTimeZoneService.midnightBatch(entry.getKey(), entry.getValue());
						midNightBatchWithTimeZoneService.postRoomRent(entry.getKey(), entry.getValue());
						midNightBatchWithTimeZoneService.doctorShare(entry.getValue());
						iBatchDao.batchJobToUpdateAppointmentStatus();
						
						SlotMasterDto dto = new SlotMasterDto();
						List<SlotMasterDto> list;
						Response res1 = iSlotMasterService.copyBackdatedSlots(dto);
						list = res1.getListObject();
						if (null != list) {
							iSlotMasterService.saveBackDatedSlotsToSlotLogs(list);
							iSlotMasterService.deleteBackdatedSlotsFromSlotMaster(list);
						}
						/*Format dayFormat = new SimpleDateFormat("EEEE"); 
						String zoneDay = dayFormat.format(currentZoneDate);
	                    Integer zoneDate = currentZoneDate.getDate();
	                    Integer zoneMonth = currentZoneDate.getMonth()+1;
						midNightBatchWithTimeZoneService.triggerScheduledMailsDaily(DAILYEXECUTION, entry.getValue());
						if(WEEKLY.equalsIgnoreCase(zoneDay))
						{
							midNightBatchWithTimeZoneService.triggerScheduledMailsWeekly(WEEKLYEXECUTION, entry.getValue());
						}
						 if(MONTHLY==zoneDate)
						{
							midNightBatchWithTimeZoneService.triggerScheduledMailsMonthly(MONTHLYEXECUTION, entry.getValue());
						}
						 if(YEARLY==zoneMonth)
							{
								midNightBatchWithTimeZoneService.triggerScheduledMailsYearly(YEARLYEXECUTION, entry.getValue());
							}*/
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Transactional
	public void template(List<Integer> unitId) {

		midNightBatchWithTimeZoneService.dailyBasisPendingAmountIpPatientSMS(unitId);
		
	}
	
	@Scheduled(fixedDelayString = "10800000")
	@Transactional
	public void doctorShare(){
		try {
			String serverId = serverIdentityService.getServerId();
			Boolean check = iServiceIdentityDao.checkServiceIdAvailable(serverId);
			Map<String, List<Integer>> timeZoneAndUnitsMap = TimeZoneComponent.getUnitTimeZoneList();
			if (timeZoneAndUnitsMap.size() > 0 && check)
			{
				for (Map.Entry<String, List<Integer>> entry : timeZoneAndUnitsMap.entrySet()) 
				{
					midNightBatchWithTimeZoneService.doctorShare(entry.getValue());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Scheduled(cron = "0 55 23 * * *")
	@Transactional
	public void deleteBackDatetedSlots() {
		try {

			String serverId = serverIdentityService.getServerId();
			Boolean check = iServiceIdentityDao.checkServiceIdAvailable(serverId);

			 if(check){
				 System.out.println("deleteBackDatetedSlots Schedular starts");
				 SlotMasterDto dto = new SlotMasterDto();
				 List<SlotMasterDto> list;
				 Response res1 = iSlotMasterService.copyBackdatedSlots(dto);
				 list = res1.getListObject();
				 if (null != list) {
					 iSlotMasterService.saveBackDatedSlotsToSlotLogs(list);
					 iSlotMasterService.deleteBackdatedSlotsFromSlotMaster(list);
				 }
				 System.out.println("deleteBackDatetedSlots Schedular ends");

			 }


		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Scheduled(cron = croneString)
	@Transactional
	public void checkBatchJobExecutedOrNot() {
		try {
			String serverId = serverIdentityService.getServerId();
			Boolean check = iServiceIdentityDao.checkServiceIdAvailable(serverId);
			Map<String, List<Integer>> timeZoneAndUnitsMap = TimeZoneComponent.getUnitTimeZoneList();
			if (timeZoneAndUnitsMap.size() > 0 && check)
			{
				for (Map.Entry<String, List<Integer>> entry : timeZoneAndUnitsMap.entrySet()) 
				{
					Date currentZoneDate = TimeZoneComponent.getDateByzone(entry.getValue().get(0));
					Integer zoneDateMin = currentZoneDate.getMinutes();
					Integer zoneDateHr = currentZoneDate.getHours();
					if (zoneDateHr == 02 && zoneDateMin >= 05 && zoneDateMin <= 55) 
					{
						Response res= midNightBatchWithTimeZoneService.checkBatchJobExecutedOrNotForpostRoomRent(entry.getKey(), entry.getValue());
						
						if(res.getListObject()== null || res.getListObject().size() == 0){
							iMailingService.sendMailForpostRoomRentAutoJobFail(entry.getKey(), entry.getValue());
						}
						
						Response res1=  midNightBatchWithTimeZoneService.checkBatchJobExecutedOrNotFormidnightBatch(entry.getKey(), entry.getValue());
						if(res1.getListObject()== null || res1.getListObject().size() == 0){
							iMailingService.sendMailForpostRoomRentAutoJobFail(entry.getKey(), entry.getValue());
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


}
