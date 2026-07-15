package com.param.adt.discharge.service;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.param.global.common.*;
import com.param.global.service.IUnitApplicationBillingConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.discharge.dao.IMarkedForDischargeDao;
import com.param.global.billing.dao.IAutoPostingConfigDao;
import com.param.global.billing.dao.IContractConsumptionDao;
import com.param.global.billing.dto.AutoPostingServicesConfigDto;
import com.param.global.billing.service.IContractConsumptionService;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.DischargeDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.ScheduleLogsDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.global.service.IMPackageMasterService;
import com.param.global.service.IOrderMasterService;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import javax.servlet.http.HttpServletRequest;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MarkedForDischargeServiceimpl implements IMarkedForDischargeService, ICommonConstants {

	@Autowired
	IMarkedForDischargeDao iMarkedForDischargeDao;
	
	@Autowired
	private IMPackageMasterService iMPackageMasterService;
	@Autowired
	private IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;

	@Autowired
	private IUnitApplicationBillingConfigurationService unitApplicationBillingConfigurationService;

	@Autowired
	private IOrderMasterService iOrderMasterService;
	@Autowired
	IContractConsumptionService iContractConsumptionService;
	@Autowired
	IContractConsumptionDao iContractConsumptionDao;
	@Autowired
	IEncounterMasterDao iEncounterMasterDao;
	@Autowired
	IAutoPostingConfigDao iAutoPostingConfigDao;
    @Autowired
	private ISmsUtilityConfigService smsUtilityService;
	@Autowired
	
	@Qualifier("applicationEventMulticaster")
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private IScheduleLogsDao isheduleDao;
	
	@Override
	public Response getDischagePatientListForNursing(DischargeDto dischargeDto) throws ApplicationException {
		try {

			return iMarkedForDischargeDao.getDischagePatientListForNursing(dischargeDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response updatePatientServiceOrder(AdmissionDto admissionDto) throws ApplicationException {
		try {

			return iMarkedForDischargeDao.updatePatientServiceOrder(admissionDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientReadyForBillingList(DischargeDto dischargeDto) throws ApplicationException {
		try {

			return iMarkedForDischargeDao.getPatientReadyForBillingList(dischargeDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updatePatientReadyForBillingStatus(DischargeDto dischargeDto) throws ApplicationException {
		try {

			Response disResponse=iMarkedForDischargeDao.updatePatientReadyForBillingStatus(dischargeDto);
			if(dischargeDto.getVisitTypeId()!=null && dischargeDto.getVisitTypeId()==IP){
				Response res=iMarkedForDischargeDao.getAdmissionDetailsByAdmissionId(dischargeDto);
				
				AdmissionsForBatchProcessDto admissionsForBatchProcessDto=(AdmissionsForBatchProcessDto) res.getObject();
				admissionsForBatchProcessDto.setDischargeDateTime(dischargeDto.getUpdatedDate());
				postRoomRentChargesForDischargeInitiate(admissionsForBatchProcessDto);
			}
			return disResponse;
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientFinalDischargeList(DischargeDto dischargeDto) throws ApplicationException {
		try {

			return iMarkedForDischargeDao.getPatientFinalDischargeList(dischargeDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response vacateBed(DischargeDto dischargeDto, HttpServletRequest request) throws ApplicationException {
		try {

			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setOrganizationId(dischargeDto.getOrganizationId());
			unitApplicationConfigurationDto.setUnitId(dischargeDto.getUnitId());
			com.param.global.common.Response appConfigRes= iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
			if(appConfigRes.getObject()!=null)
			{
				UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto)appConfigRes.getObject();
				
				if(unitApplicationConfigurationDtos.getSkipHousekeeping() != null) 
					dischargeDto.setSkipHousekeeping(unitApplicationConfigurationDtos.getSkipHousekeeping());
				else
					dischargeDto.setSkipHousekeeping('N');
			}
			
			//dischargeDto.setBedStatusId(3);//previously it was 2 (Under Housekeeping)
			dischargeDto.setBedStatusId(dischargeDto.getSkipHousekeeping() == 'N'? 2 :3);
			dischargeDto.setStatus('A');
			dischargeDto.setCreatedDate(dischargeDto.getUpdatedDate());
			dischargeDto.setCreatedBy(dischargeDto.getUpdatedBy());
			
			Response res= iMarkedForDischargeDao.vacateBed(dischargeDto);
			try{
				syncDischargePatientForMyLife(dischargeDto);
			}catch(Exception e){
				
			}
			DischargeDto dischargeDto2=(DischargeDto) res.getObject();
			Response res2 = iMarkedForDischargeDao.saveHousekeepingLog(dischargeDto2);
			if(res.getStatus().equals(SUCCESS)&&res2.getStatus().equals(SUCCESS)){
				String resPublicUrl =  unitApplicationBillingConfigurationService.getPublicUrl(dischargeDto.getUnitId());
				SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
					smsInfo.setUnitId(dischargeDto.getUnitId());
					smsInfo.setOrganizationId(dischargeDto.getOrganizationId());
					smsInfo.setPatientId(dischargeDto.getPatientId());
					smsInfo.setReferringDoctorId(dischargeDto.getDoctorId());
					smsInfo.setTreatingDoctorId(dischargeDto.getDoctorId());
					smsInfo.setDateTime(dischargeDto.getUpdatedDate());
					smsInfo.setBedDetails(dischargeDto.getWardName()+", "+dischargeDto.getBedNumber());
					smsInfo.setAdmitingDoctorId(dischargeDto.getDoctorId());
					smsInfo.setAdmissionNo(dischargeDto.getAdmissionNumber()!=null ? dischargeDto.getAdmissionNumber(): null);

				String criticalUrl= "";
				if(resPublicUrl == null) {
					criticalUrl= ServletUtils.getBaseUrl(request)+"/#/api/inPatientFeedback/"+smsInfo.getUnitId()+"/"+smsInfo.getOrganizationId()+"/"
							+smsInfo.getPatientId()+"/"+dischargeDto.getVisitType()+"/"+dischargeDto.getVisitTypeId()+"/"+dischargeDto.getAdmissionId();
				}else {
					criticalUrl= resPublicUrl+"/#/api/inPatientFeedback/"+smsInfo.getUnitId()+"/"+smsInfo.getOrganizationId()+"/"
							+smsInfo.getPatientId()+"/"+dischargeDto.getVisitType()+"/"+dischargeDto.getVisitTypeId()+"/"+dischargeDto.getAdmissionId();
				}
				smsInfo.setCriticalUrl(criticalUrl);

				smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.DISCHARGE,smsInfo);
				smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.DISCHARGE,smsInfo);

			}
			// close all package except multivisit for same visit type by dinesh start
			/*com.param.global.common.Response packageResponse = iTPackageConsumptionMasterDao.getExistingActivePackageByPatientEncounter(dischargeDto.getPatientId(), dischargeDto.getAdmissionId(), dischargeDto.getVisitTypeId(), dischargeDto.getOrganizationId(), dischargeDto.getUnitId(),false,0);
			if (packageResponse.getCode().equals(SUCCESS_CODE) && packageResponse.getListObject() != null) 
			{
				List<PackageConsumptionMasterResDto> listResDto = packageResponse.getListObject();
				
				//for package iteration
				for (PackageConsumptionMasterResDto resDto : listResDto) 
				{
					boolean cancelPackage = true;
					if(resDto.getPackageTypeId()!=null && resDto.getPackageTypeId()==5) 
					{
						Date validTo = GlobalCommonDateUtils.getDate(resDto.getValidityTo(), "dd-M-yyyy HH:mm:ss");
						Date currentDate =  GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()), "dd-M-yyyy HH:mm:ss");
						if(validTo.compareTo(currentDate) >= 0)
							cancelPackage = false;
					}
					if(cancelPackage)
					{
						iTPackageConsumptionMasterDao.cancelPackageConsumption(
								 resDto.getPackageId(), 
								 dischargeDto.getPatientId(), 
								 null,
								 0, 
								 "", 
								 dischargeDto.getAdmissionId(),
								 dischargeDto.getVisitTypeId(),
								 resDto.getOrderDetailId());
					}
				}
			}*/
			// end
			
			return res;
			
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public void postRoomRentChargesForDischargeInitiate(AdmissionsForBatchProcessDto admission) {
		try{
				List<OrderDetailsMasterDto> orderDetailsList = new LinkedList<>();
				UnitApplicationConfigurationDto uAppConfDto = new UnitApplicationConfigurationDto();
				uAppConfDto.setOrganizationId(admission.getOrganizationId());
				uAppConfDto.setUnitId(admission.getUnitId());
				com.param.global.common.Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(uAppConfDto);
				if(appConfigRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && appConfigRes.getObject() != null){
					uAppConfDto = (UnitApplicationConfigurationDto)appConfigRes.getObject();
				}
				
				ContractConsumptionDto consumptionDto = null;
				OrderMasterDto orderMasterDto = new OrderMasterDto();
					orderMasterDto.setVisitTypeId(IP);// op==1
					orderMasterDto.setPatientId(admission.getPatientId());
					orderMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(admission.getUnitId()).getTime());
					orderMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(admission.getUnitId()).getTime());
					orderMasterDto.setOrderDate(TimeZoneComponent.getDateByzone(admission.getUnitId()).getTime());
					orderMasterDto.setOrderStatus('A');
					orderMasterDto.setOrgId(admission.getOrganizationId());
					orderMasterDto.setOrgUnitId(admission.getUnitId());
					orderMasterDto.setOrderStatusId(1);// order status==1
					orderMasterDto.setAdmissionId(admission.getAdmissionId());
					orderMasterDto.setPatientTypeId(admission.getPatientTypeId());
					orderMasterDto.setBillingBedCategoryId(admission.getBillingBedCategoryId());
					orderMasterDto.setPaymentEntitlementTypeId(admission.getPaymentEntitlementId());
					orderMasterDto.setReferralId(admission.getReferralId());
					orderMasterDto.setDefaultDiscountOwnership(uAppConfDto.getDefaultDiscountOwnership());
				com.param.global.common.Response serviceConfigRes=iAutoPostingConfigDao.getAutoPostingConfigList(admission.getOrganizationId(), admission.getUnitId());
					List<AutoPostingServicesConfigDto> autoPostingServicesConfigDtoList = new LinkedList<>();
				
					if(serviceConfigRes.getListObject().size()>0) {
					  autoPostingServicesConfigDtoList = serviceConfigRes.getListObject();
					if(!(autoPostingServicesConfigDtoList.isEmpty())){
					ListIterator<AutoPostingServicesConfigDto> iterator = autoPostingServicesConfigDtoList.listIterator();	
					while(iterator.hasNext())
					{
						AutoPostingServicesConfigDto dto = iterator.next();
						Integer serviceChargeStatus = autoServiceChargesCalculation(dto,admission);
						//-------order details-------//
						if(serviceChargeStatus!=2) 
						{
							OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
								
								Integer bedCateServiceId = admission.getServiceIdForBedCharges();
								orderDetailsMasterDto.setServiceId((dto.getIsBedChargesService()!=null && dto.getIsBedChargesService()=='Y') ? bedCateServiceId>0 ? bedCateServiceId : dto.getServiceId() : dto.getServiceId());
								orderDetailsMasterDto.setIsChargedHalfDay(serviceChargeStatus==0 ? 'Y' : 'N');
								orderDetailsMasterDto.setServiceIsBilled(0);
								orderDetailsMasterDto.setOrderQty(1);
								orderDetailsMasterDto.setOrderDate(TimeZoneComponent.getDateByzone(admission.getUnitId()).getTime());
								orderDetailsMasterDto.setOrderSchDate(TimeZoneComponent.getDateByzone(admission.getUnitId()).getTime());
								orderDetailsMasterDto.setOrdRenderDatetime(TimeZoneComponent.getDateByzone(admission.getUnitId()).getTime());
								orderDetailsMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(admission.getUnitId()).getTime());
								orderDetailsMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(admission.getUnitId()).getTime());
								orderDetailsMasterDto.setStatus('A');
								orderDetailsMasterDto.setOrgId(admission.getOrganizationId());
								orderDetailsMasterDto.setOrgUnitId(admission.getUnitId());
								orderDetailsMasterDto.setServiceRenderingDeptId(dto.getSpecialityId());
								orderDetailsMasterDto.setOrdCancelled('N');
								orderDetailsMasterDto.setNetAmount(BigDecimal.ZERO);
								orderDetailsMasterDto.setIsPostedWhileDischarge('Y');
								orderDetailsMasterDto.setOrdDiscount(new BigDecimal(0.00));
								orderDetailsMasterDto.setTariffId(uAppConfDto.getDefaultSelfTariffId());
								orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
								orderDetailsMasterDto.setOrderEmergencyFlag('N');
								orderDetailsMasterDto.setServiceRendered(1);
								orderDetailsMasterDto.setOrdRenderDatetime(TimeZoneComponent.getDateByzone(admission.getUnitId()).getTime());
								orderDetailsMasterDto.setServiceChargeable(1);
								orderDetailsMasterDto.setServiceIsBilled(0);
								orderDetailsMasterDto.setIsDrug('N');
								orderDetailsMasterDto.setWardId(admission.getWardId()  != null ? admission.getWardId() : null);
								orderDetailsMasterDto.setRoomId(admission.getRoomId() != null ? admission.getRoomId() : null);
								orderDetailsMasterDto.setBedId(admission.getBedId() != null ? admission.getBedId() : null);
						
							 consumptionDto = new ContractConsumptionDto();
								consumptionDto.setOrganizationId(admission.getOrganizationId());
								consumptionDto.setUnitId(admission.getUnitId());
								consumptionDto.setVisitTypeId(IP);
								consumptionDto.setPatientTypeId(admission.getPatientTypeId());
								consumptionDto.setBillingBedCategoryId(admission.getBillingBedCategoryId());
								consumptionDto.setPaymentEntitlementId(SELF);
								consumptionDto.setServiceId((dto.getIsBedChargesService()!=null && dto.getIsBedChargesService()=='Y') ? bedCateServiceId>0 ? bedCateServiceId : dto.getServiceId() : dto.getServiceId());
								consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(admission.getUnitId()), "yyyy-MM-dd HH:mm:ss"));
								consumptionDto.setContractId(admission.getContractId());
								consumptionDto.setDefaultSelfTariffId(uAppConfDto.getDefaultSelfTariffId());
								consumptionDto.setSelfPayeeId(admission.getPayeeId());
								consumptionDto.setAdmissionId(admission.getAdmissionId());
								consumptionDto.setEncounterId(admission.getAdmissionId());
								consumptionDto.setPatientId(admission.getPatientId());
                                consumptionDto.setServiceChargeStatus(serviceChargeStatus);
							com.param.global.common.Response res =iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
								consumptionDto=(ContractConsumptionDto)res.getObject();
							
							if(consumptionDto!=null)
							{
								orderDetailsMasterDto.setOrdConcession(new BigDecimal(consumptionDto.getConcession()));
								orderDetailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
								orderDetailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
								orderDetailsMasterDto.setOrdTotalAmt(new BigDecimal(consumptionDto.getServiceRate()));
								orderDetailsMasterDto.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()));
								orderDetailsMasterDto.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()));
								orderDetailsMasterDto.setTaxPer(new BigDecimal(consumptionDto.getTaxPercentage()));
								orderDetailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
								orderDetailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
								orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
								orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
								orderDetailsMasterDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
								orderDetailsMasterDto.setIsDocReq(consumptionDto.getIsDocReq());
								orderDetailsMasterDto.setCoPayPer(new BigDecimal(consumptionDto.getCoShare()));
								orderDetailsMasterDto.setIsRefDoctorShare(consumptionDto.getIsRefReq());
								orderDetailsMasterDto.setDiscountOwnership(uAppConfDto.getDefaultDiscountOwnership());
								if(consumptionDto.getIsDocReq()!=null && consumptionDto.getIsDocReq()=='Y')
								{
									orderDetailsMasterDto.setOrdDocId(admission.getDoctorId());
									orderDetailsMasterDto.setOrdDocSplId(admission.getSpecialityId());
								}
							}
							
							if(admission.getPayeeId() == 1) {
								orderDetailsMasterDto.setCoPayPer(new BigDecimal(100));
							}
							
						  if(orderDetailsMasterDto.getOrdTotalAmt().doubleValue() > 0)
							{
								orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
								if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
									orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
								
								orderDetailsList.add(orderDetailsMasterDto);
							}
						}
					}
					orderMasterDto.setListOrderDetailsMasterDto(orderDetailsList);
					iOrderMasterService.saveOrderMaster(orderMasterDto);
					}
			}//end of ServiceAutoConfig
					
		}catch(Exception e){
			
		}
	}
	
	
	private Integer autoServiceChargesCalculation(AutoPostingServicesConfigDto autoPostingServicesConfigDto,
				AdmissionsForBatchProcessDto admission) {
		Instant btime=null;
		Instant halfDayPlusBufferTime=null;
			 String dischargeTime= admission.getDischargeDateTime().substring(11, 16);
			 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			
			 LocalDate appDate = LocalDate.parse(admission.getAdmissionDateTime(), formatter);
				LocalDate currentDate = LocalDate.now();
				long diffInDays = ChronoUnit.DAYS.between(appDate, currentDate);
			 if(autoPostingServicesConfigDto.getPostingTime()==1) {
				 autoPostingServicesConfigDto.setServicePostingTime("00:00");
			 }
			 else {
				 autoPostingServicesConfigDto.setServicePostingTime("12:00");
			 }
			 
			 if(autoPostingServicesConfigDto.getIsHalfDay().equals('Y')) {
				
				 LocalTime disTime1 = LocalTime.parse(dischargeTime);
				 LocalDate today = LocalDate.now();
				 Instant disTime = disTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();


				 LocalTime halfDay1 = LocalTime.parse(autoPostingServicesConfigDto.getHalfDayHrs()<10 ? "0"+autoPostingServicesConfigDto.getHalfDayHrs().toString()+":00" : autoPostingServicesConfigDto.getHalfDayHrs().toString()+":00");
				 Instant halfDay = halfDay1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

					if(autoPostingServicesConfigDto.getBufferTimeForAdmission()!=null) {
						LocalTime btime1 = LocalTime.parse(autoPostingServicesConfigDto.getBufferTimeForAdmission()<10 ? "0"+autoPostingServicesConfigDto.getBufferTimeForAdmission().toString()+":00" : autoPostingServicesConfigDto.getBufferTimeForAdmission().toString()+":00");
						btime = btime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();
					}
				 Instant totalTime;

				 LocalTime batchJobTime1 = LocalTime.parse( autoPostingServicesConfigDto.getServicePostingTime());
				 Instant batchJobTime = batchJobTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

				 if (diffInDays == 0) {
					 // Extract the time portion (HH:mm) from admission's admissionDateTime
					 String admissionTime = admission.getAdmissionDateTime().substring(11, 16); // "HH:mm"

					 // Parse admissionTime string into LocalTime (only hours and minutes)
					 LocalTime addTime = LocalTime.parse(admissionTime);

					 // Convert disTime (Instant) to ZonedDateTime in UTC or desired time zone
					 ZonedDateTime disZonedDateTime = disTime.atZone(ZoneId.of("UTC")); // Use the correct time zone if necessary

					 // Subtract hours and minutes from disTime (ZonedDateTime)
					 totalTime = disZonedDateTime.minusHours(addTime.getHour())
							 .minusMinutes(addTime.getMinute())
							 .toInstant();
				 } else {
					 // Convert disTime (Instant) to ZonedDateTime in UTC or desired time zone
					 ZonedDateTime disZonedDateTime = disTime.atZone(ZoneId.of("UTC")); // Use the correct time zone if necessary

					 // Extract hours and minutes from batchJobTime (which is an Instant)
					 ZonedDateTime batchJobZonedDateTime = batchJobTime.atZone(ZoneId.of("UTC"));
					 int batchJobHour = batchJobZonedDateTime.getHour();
					 int batchJobMinute = batchJobZonedDateTime.getMinute();

					 // Subtract the hours and minutes from disTime
					 totalTime = disZonedDateTime.minusHours(batchJobHour)
							 .minusMinutes(batchJobMinute)
							 .toInstant();
				 }
					
					//System.out.println("totalTime-"+totalTime); 
					if(btime!=null)
						if(totalTime.isAfter(btime)){
					// halfDayPlusBufferTime = halfDay.plusHours(btime.getHour());

				 			ZonedDateTime btimeZoned = btime.atZone(ZoneId.of("UTC"));
							ZonedDateTime halfDayZoned = halfDay.atZone(ZoneId.of("UTC"));
							halfDayPlusBufferTime = halfDayZoned.plusHours(btimeZoned.getHour()).toInstant();
						}
					else {
							halfDayPlusBufferTime = halfDay;
						}
					//System.out.println("Half day and Buffer Time-"+halfDayPlusBufferTime); 
					
					
					     if(totalTime.isBefore(halfDayPlusBufferTime) || totalTime.equals(halfDayPlusBufferTime)) {
						  //System.out.println("Half Day"); 
						  return 0;	
					    }
					    else
					    {
					    	//System.out.println("Full Day");
					    	return 1;
					    }
				   
			 }
			 else {
				 return 1;
				 /*   String halfDuration="12:00";
				    LocalTime disTime = LocalTime.parse(dischargeTime);
					LocalTime batchJobTime=LocalTime.parse(autoPostingServicesConfigDto.getServicePostingTime());
					LocalTime halfDurationTime=LocalTime.parse(halfDuration);
					 
					LocalTime totalTime;
					if(diffInDays==0) {
						 String admissionTime= admission.getAdmissionDateTime().substring(11, 16);
						 LocalTime addTime = LocalTime.parse(admissionTime);
						totalTime=disTime.minusHours(addTime.getHour()).minusMinutes(addTime.getMinute());
					}
					else {
						 totalTime = disTime.minusHours(batchJobTime.getHour()).minusMinutes(batchJobTime.getMinute());
					}
					//System.out.println("totalTime-"+totalTime); 
					   //Buffer time and halfDay hours not provided
					   if(totalTime.isBefore(halfDurationTime) || totalTime.equals(halfDurationTime)) {
						   //System.out.println("Half Day "); 
						   return 0;
					    }
					    else
					    {
					    	//System.out.println("Full Day");
					    	return 1;
					    }*/
					   
					   
			 }
		}
	
	public void syncDischargePatientForMyLife(DischargeDto dischargeDto) {
		ScheduleLogsDto logDto = new ScheduleLogsDto(this);
		// insert into log table
		 String dateTime = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()), "dd-M-yyyy HH:mm:ss");
			 logDto.setAction(ScheduleActions.INSERT);
			 logDto.setAddedBy(dischargeDto.getUpdatedBy());
			 logDto.setAddedDate(dateTime);
			 logDto.setDateTime(dateTime);
			 logDto.setApplication(ScheduleActions.HIS);
			 logDto.setErrorCount(0);
			 logDto.setPriority(ScheduleActions.PRIORITY_NORM);
			 logDto.setRecordId(dischargeDto.getAdmissionId());
			 logDto.setStatus(ScheduleActions.STATUS_PENDING);
			 logDto.setTableName(ScheduleTables.ADMISSION_DETAILS);
			 logDto.setOrgId(dischargeDto.getOrganizationId());
		// applicationEventPublisher.publishEvent(logDto);
		 isheduleDao.saveScheduleLogs(logDto);
}


	@Override
	public Response getDischagePatientListByDoctorId(DischargeDto dischargeDto) throws ApplicationException {
		try {
			return iMarkedForDischargeDao.getDischagePatientListByDoctorId(dischargeDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getPatientDischargedList(DischargeDto dischargeDto) throws ApplicationException {
		try {

			return iMarkedForDischargeDao.getPatientDischargedList(dischargeDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getPatientDischargedListForPrint(DischargeDto dischargeDto) throws ApplicationException {
		try {

			return iMarkedForDischargeDao.getPatientDischargedListForPrint(dischargeDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response getPatientFinalDischargeListNewForListAndCount(DischargeDto dischargeDto) throws ApplicationException {
		try {

			return iMarkedForDischargeDao.getPatientFinalDischargeListNewForListAndCount(dischargeDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response getDischargePatientListForNursingByVisitTypeId(DischargeDto dischargeDto)
			throws ApplicationException {
		try {

			return iMarkedForDischargeDao.getDischargePatientListForNursingByVisitTypeId(dischargeDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getDischargedPatientsList(DischargeDto dischargeDto) throws ApplicationException {
		try {
			return iMarkedForDischargeDao.getDischargedPatientsList(dischargeDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
}


