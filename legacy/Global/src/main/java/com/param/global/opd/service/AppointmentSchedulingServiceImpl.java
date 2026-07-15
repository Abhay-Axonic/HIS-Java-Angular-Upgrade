package com.param.global.opd.service;

import java.math.BigDecimal;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.transaction.Transactional;

import com.param.global.dto.global.AIAppointmentQueueDto;
import com.param.global.opd.dao.IAiAppointmentQueueDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.param.global.billing.dao.IBillingDao;
import com.param.global.billing.dto.BillingDetailsDto;
import com.param.global.billing.service.IContractConsumptionService;
import com.param.global.common.AppointmentStatusMasterConstant;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.ISmsUtilityConstants;
import com.param.global.common.Response;
import com.param.global.common.ScheduleActions;
import com.param.global.common.ScheduleTables;
import com.param.global.common.SmsUtility;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dao.IOrderMasterDao;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.doctor.dao.IDoctorScheduleDao;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.CustomPatientDetailsDto;
import com.param.global.dto.MyLifeConnectionLinkDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.dto.PatientSponsorDetailsDto;
import com.param.global.dto.ScheduleLogsDto;
import com.param.global.dto.ScheduleMasterDto;
import com.param.global.dto.ServiceMasterDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.sync.AppointmentSyncSchedulingDto;
import com.param.global.opd.dao.IAppointmentMasterDao;
import com.param.global.opd.dao.IAppointmentSchedulingDao;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.global.service.IOrderMasterService;
import com.param.global.service.IPatientRegistrationService;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.opd.appointment.model.AppointmentMaster;
import com.param.opd.encounter.dto.EncounterDocDetailsDto;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.opd.unit.model.SlotMaster;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class AppointmentSchedulingServiceImpl implements IAppointmentSchedulingService,ICommonConstants{

	private static final Logger logger = Logger.getLogger(AppointmentSchedulingServiceImpl.class);

	@Autowired
	IAiAppointmentQueueDao iAiAppointmentQueueDao;

	@Autowired
	IAppointmentSchedulingDao iAppointmentSchedulingDao;
	
	@Autowired
	IScheduleLogsDao iScheduleLogsDao;
	
	@Autowired
	IPatientRegistrationService patientRegistrationServiceImpl;
	
	@Autowired
	IOrderMasterDao iOrderMasterDao;
	
	@Autowired
	IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	@Autowired
	@Qualifier("applicationEventMulticaster")
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	IEncounterMasterService iEncounterMasterService;
	
	@Autowired
	private SmsUtility smsUtility;
	
	@Autowired
	private ISmsUtilityConfigService smsUtilityService;
	
	@Autowired
	private IScheduleLogsDao isheduleDao;
	
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	private IOrderMasterService iOrderMasterService; 
	
	@Autowired
	IContractConsumptionService iContractConsumptionService;
	
	@Autowired
	IOrderDetailsMasterDao iOrderDetailsMasterDao;
	
	@Autowired
	IEncounterMasterDao iEncounterMasterDao;
	
	@Autowired
	private IBillingDao iBillingDao;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	IDoctorScheduleDao iDoctorScheduleDao;
	
	@Autowired
	IPatientRegistrationService iPatientRegistrationService;
	
	@Autowired
	private IAppointmentMasterDao iAppointmentMasterDao;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	private Gson gson;
	
	@Override
	@Transactional
	public Response generateAppointment(SlotMasterDto slotMasterDto){
		try{
				return scheduleAppointmentForToken(slotMasterDto);

		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
		
	}
	
	@Transactional
	private Response scheduleAppointmentForToken(SlotMasterDto slotMasterDto) 
	{
		try{
				Response res2 = null;
				
				SlotMasterDto appointmentObj = new SlotMasterDto();
					appointmentObj.setOrganizationId(slotMasterDto.getOrganizationId());
					appointmentObj.setUnitId(slotMasterDto.getUnitId());
					appointmentObj.setCreatedDate(slotMasterDto.getCreatedDate());
					appointmentObj.setUpdatedDate(slotMasterDto.getCreatedDate());
					appointmentObj.setUpdatedBy(slotMasterDto.getCreatedBy());
					appointmentObj.setCreatedBy(slotMasterDto.getCreatedBy());
					appointmentObj.setAppointmentStatusId(APPOINTMENTSTATUSID1); //Pending
					appointmentObj.setAppointmentTypeId(APPOINTMENTTYPEID1); //temporary 
					appointmentObj.setArrivalTime(slotMasterDto.getCreatedDate());
					appointmentObj.setTokenNumber(iAppointmentSchedulingDao.getTokenNumber(slotMasterDto.getAppointmentDate().substring(0,10), slotMasterDto.getUnitId()));
					appointmentObj.setAppointmentDate(slotMasterDto.getAppointmentDate().substring(0,10)+" 00:00:00");
					appointmentObj.setStatus('A');
					appointmentObj.setPatientId(slotMasterDto.getPatientId()!=null ? slotMasterDto.getPatientId() : null);
				if(slotMasterDto.getPatientId()!=null)
				{	
					Integer i = iAppointmentSchedulingDao.appointmentCheckForKioskPatient(appointmentObj);
					if(i.equals(0)){
						res2 = iAppointmentSchedulingDao.scheduleAppointment(appointmentObj);
					}
					else{
						return new Response(ERROR, ALREADY_EXIST_CODE, "Patient already has an appointment for today's date from kiosk where doctor or department is yet to be assigned..!!", null, null);
					}
				}else{
					res2 = iAppointmentSchedulingDao.scheduleAppointment(appointmentObj);
				}

			AppointmentMaster appointmentMaster = null;

			appointmentMaster = (AppointmentMaster)res2.getObject();
			slotMasterDto.setAppointmentId(appointmentMaster.getAppointmentId());
			slotMasterDto.setPatientId(appointmentMaster.getPatientId());
			slotMasterDto.setNewPatient(false);
//			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, res2.getObject());
            return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, res2.getObject(), slotMasterDto);

		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Transactional
	@Override
	public Response scheduleAppointment(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			
			Character isReshedule = slotMasterDto.getReSchedule() != null ? slotMasterDto.getReSchedule() : 'N';
			
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
				unitApplicationConfigurationDto.setOrganizationId(slotMasterDto.getOrganizationId());
				unitApplicationConfigurationDto.setUnitId(slotMasterDto.getUnitId());
			Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
		
			unitApplicationConfigurationDto = (UnitApplicationConfigurationDto)appConfigRes.getObject();
					slotMasterDto.setSenderId(unitApplicationConfigurationDto.getSenderId());
					slotMasterDto.setIsKiosk(unitApplicationConfigurationDto.getIsKioskRequired());
			String message = "Appointment Scheduled...!!";
			
			Response appRes = null;
			if(isReshedule=='N' && slotMasterDto.getIsFromCoversheet()==null && (slotMasterDto.getAppointmentTypeId()!=APPOINTMENTTYPEID6 && slotMasterDto.getAppointmentTypeId()!=APPOINTMENTTYPEID3 && slotMasterDto.getAppointmentTypeId()!=APPOINTMENTTYPEID5 && slotMasterDto.getAppointmentTypeId()!=APPOINTMENTTYPEID7))
			{
				if(this.isAppointmentForSameDateTimeExist(slotMasterDto)){
					return new Response(ERROR, ALREADY_EXIST_CODE, "This patient already has an appointment on this date and time !!", null, null);
				}
				if(this.isAppointmentIsForSameDoctorWithoutCompletionOfPreviousAppintment(slotMasterDto)){
					return new Response(ERROR, ALREADY_EXIST_CODE, "Previous appointment of same doctor or specialty is not yet closed !!", null, null);
				}
			}
			if(slotMasterDto.getAppointmentTypeId().intValue()==APPOINTMENTTYPEID3) 
			{
				if(iAppointmentSchedulingDao.getEHCAppointmentsByPatienId(slotMasterDto))
					return new Response(ERROR, ALREADY_EXIST_CODE, "This patient already has an appointment on this date and time !!", null, null);
			}
			if (slotMasterDto.getIsFromEncounter() != null) 
			{
				if (slotMasterDto.getIsFromEncounter().equals('Y')) 
				{
					slotMasterDto.setAppointmentStatusId(APPOINTMENTSTATUSID8); // in-process
				}else{
					slotMasterDto.setAppointmentStatusId(APPOINTMENTSTATUSID1);
				}
			}else
			{
				slotMasterDto.setAppointmentStatusId(APPOINTMENTSTATUSID1);
			}
		
			if(slotMasterDto.getSlotStatusId()==null)
			    slotMasterDto.setSlotStatusId(4);
				
			boolean res = false;
			if(slotMasterDto.getAppointmentTypeId()!=APPOINTMENTTYPEID5 && slotMasterDto.getSlotId() != null && slotMasterDto.getSlotId() > 0)
			{
				res = iAppointmentSchedulingDao.isAppointmentValid(slotMasterDto);
				if(res == false)
				{
					if(slotMasterDto.getAppointmentTypeId()==APPOINTMENTTYPEID3)
					{
						int newslot = iAppointmentSchedulingDao.getEHCFreeSlot(slotMasterDto);
						if(newslot>0)
						{
							slotMasterDto.setSlotId(newslot);
						}else{
							return new Response(ERROR, ALREADY_EXIST_CODE, "No slots available for EHC...!!", null, null);
						}
					}else{
						return new Response(ERROR, ALREADY_EXIST_CODE, "Slot is already booked, please select another slot...!!", null, null);
					}
				}
			}
			//	Response blockAppointment = iAppointmentSchedulingDao.blockAppointment(slotMasterDto);
			//If patient is not registered then there will be a pre-registration of a patient who's patientTypeId == 5
			if(slotMasterDto.getPatientId()==0)
			{
				PatientRegistrationDto patientRegistrationDto = new PatientRegistrationDto();
					patientRegistrationDto.setOrganizationId(slotMasterDto.getOrganizationId());
					patientRegistrationDto.setUnitId(slotMasterDto.getUnitId());
					patientRegistrationDto.setPrefixId(slotMasterDto.getPrefixId());
					patientRegistrationDto.setFirstName(slotMasterDto.getFirstName());
					patientRegistrationDto.setMiddleName(slotMasterDto.getMiddleName());
					patientRegistrationDto.setLastName(slotMasterDto.getLastName());
					patientRegistrationDto.setDob(slotMasterDto.getDob());
					patientRegistrationDto.setGenderId(slotMasterDto.getGenderId());
					patientRegistrationDto.setMobileNumber(slotMasterDto.getMobileNumber());
					patientRegistrationDto.setEmail(slotMasterDto.getEmail()!=null ? slotMasterDto.getEmail() : null);
                    patientRegistrationDto.setRegistrationTypeId(slotMasterDto.getRegistrationTypeId());
					patientRegistrationDto.setCreatedBy(slotMasterDto.getCreatedBy());
					patientRegistrationDto.setCreatedDate(slotMasterDto.getCreatedDate());
					patientRegistrationDto.setUpdatedBy(slotMasterDto.getUpdatedBy());
					patientRegistrationDto.setUpdatedDate(slotMasterDto.getCreatedDate());
					patientRegistrationDto.setPatientCategoryId(unitApplicationConfigurationDto.getDefaultPatientCategoryId());
					patientRegistrationDto.setStatus('A');
					patientRegistrationDto.setIdentificationNumber(slotMasterDto.getIdentificationNumber());
					patientRegistrationDto.setIdentificationTypeId(slotMasterDto.getIdentificationTypeId());
					patientRegistrationDto.setCountryCallingCode(slotMasterDto.getCountryCallingCode());
					patientRegistrationDto.setBloodGroup(slotMasterDto.getBloodGroup() != null ? slotMasterDto.getBloodGroup()  : null);
				Response patientRegistraion = patientRegistrationServiceImpl.savePatientRegistration(patientRegistrationDto);
				if(patientRegistraion.getStatus().equals(SUCCESS))
					slotMasterDto.setPatientId((Integer)patientRegistraion.getObject());
				else
					return new Response(ERROR, null, "Patient Already Exist !!", null, null);
				
				String patientResMessage = patientRegistraion.getMessage();
				String[] splitStr = patientResMessage.trim().split("\\s+");
				slotMasterDto.setNewPatient(true);
				message="Appointment scheduled for pre registered patient "+splitStr[0]+"..!!";
			}else{
				slotMasterDto.setNewPatient(false);
			}

			if(slotMasterDto.getGenerateToken()!=null && (slotMasterDto.getAppointmentTypeId()==APPOINTMENTTYPEID1 || slotMasterDto.getAppointmentTypeId() == APPOINTMENTTYPEID7 || slotMasterDto.getAppointmentTypeId() == APPOINTMENTTYPEID9))
			{
				if(slotMasterDto.getGenerateToken()=='Y' && slotMasterDto.getAppointmentDate() != null)
				{
						slotMasterDto.setTokenNumber(slotMasterDto.getTokenNumber() != null ? slotMasterDto.getTokenNumber(): iAppointmentSchedulingDao.getTokenNumber(
												slotMasterDto.getAppointmentDate().substring(0, 10),
												slotMasterDto.getUnitId()));
					slotMasterDto.setAppointmentDate(slotMasterDto.getAppointmentDate());
					slotMasterDto.setArrivalTime(slotMasterDto.getCreatedDate());
				}
			}
			AppointmentMaster appointmentMaster = null;
			appRes = iAppointmentSchedulingDao.scheduleAppointment(slotMasterDto);
			appointmentMaster = (AppointmentMaster)appRes.getObject();
			
			//to remove it from clinic if patient is not sync to clinic
			if(slotMasterDto.getAppointmentTypeId()==APPOINTMENTTYPEID1 && iAppointmentSchedulingDao.isPatientSync(slotMasterDto.getPatientId())==false && slotMasterDto.getSlotId()!=null)
			{
				List<ScheduleMasterDto> scheduleMasterDtosList = new ArrayList();
				
				SlotMaster master = (SlotMaster) sessionFactory.getCurrentSession().get(SlotMaster.class, slotMasterDto.getSlotId());
				
				ScheduleMasterDto dto = new ScheduleMasterDto();	
					dto.setDoctorId(master.getDoctorId());				    	
					dto.setSpecialityId(master.getSpecialityId());				    	
					dto.setOrganisationId(master.getOrganizationId());				 
					dto.setUnitId(master.getUnitId());
					dto.setDeleteAllSlots('N');
					dto.setScheduleDate(GlobalCommonDateUtils.getStringDate(master.getAppointmentDate(),"dd-MM-yyyy"));
					dto.setFromTime(master.getFromTime().toString());
					dto.setToTime(master.getToTime().toString());
					dto.setAddRemove(1);
				scheduleMasterDtosList.add(dto);
	
				iDoctorScheduleDao.saveDoctorSchedule(scheduleMasterDtosList,"DELETE");
			}
			
			/*if(appRes.getStatus().equals(SUCCESS) && slotMasterDto.getDoctorId()!=null){
				SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
					smsInfo.setOrganizationId(slotMasterDto.getOrganizationId());
					smsInfo.setUnitId(slotMasterDto.getUnitId());
				smsInfo.setDateTime(slotMasterDto.getAppointmentDate2() != null
						? (slotMasterDto.getAppointmentDate2().substring(0, 10) + " at "
								+ slotMasterDto.getFromTimeString())
						: (slotMasterDto.getAppointmentDate().substring(0, 10) + " at "
								+ slotMasterDto.getFromTimeString()));
					smsInfo.setPatientId(slotMasterDto.getPatientId());
					smsInfo.setConsultingDoctorId(slotMasterDto.getDoctorId());
					smsInfo.setAppointmentId(appointmentMaster.getAppointmentId());
					smsInfo.setAppointmentUniqueId(appointmentMaster.getAppointmentUniqueId() != null ? appointmentMaster.getAppointmentUniqueId() : null);
				smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.APPOITMENT,smsInfo);
				smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.APPOITMENT,smsInfo);
				
			}*/
			
			Boolean isSyncScheduling=(Boolean)sessionFactory.getCurrentSession()
					.createSQLQuery("  select om.is_sync_scheduling as \"isSyncScheduling\" from public.m_organization_master om where  om.organization_id=:organizationId and om.status='A' ")
					.setInteger("organizationId", slotMasterDto.getOrganizationId())
					.uniqueResult();
			
			if(isSyncScheduling != null) {
			slotMasterDto.setSyncScheduling(isSyncScheduling);
			}
			slotMasterDto.setAppointmentId(appointmentMaster.getAppointmentId());
			return new Response<>(SUCCESS, SUCCESS_CODE, message, null, appointmentMaster, slotMasterDto);
		
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	private boolean isAppointmentIsForSameDoctorWithoutCompletionOfPreviousAppintment(SlotMasterDto slotMasterDto) {
		try {
			Boolean result = false; 
		 	Response res = iAppointmentSchedulingDao.isAppointmentIsForSameDoctorWithoutCompletionOfPreviousAppintment(slotMasterDto);
		 	if(!(res.getListObject().size()>0))
		 		return false;
		 	
		 	SlotMasterDto masterDto = (SlotMasterDto)res.getObject();
		 	
		 	ListIterator<SlotMasterDto> iterator = res.getListObject().listIterator();
		 	while(iterator.hasNext())
		 	{
		 		SlotMasterDto dto = iterator.next();
				if (slotMasterDto.getAppointmentTypeId() == APPOINTMENTTYPEID1) {
					if(masterDto.getDoctorId()!=null)
					{
						if(masterDto.getDoctorId().equals(dto.getDoctorId())&& masterDto.getSpecialityId().equals(dto.getSpecialityId())) {
							result = true;
							if(result)
			 					return result;
						} else {
							result = false;
						}
					}else{
						if(masterDto.getSpecialityId().equals(dto.getSpecialityId())) {
							result = true;
							if(result)
			 					return result;
						} else {
							result = false;
						}
					}
				}
				if (slotMasterDto.getAppointmentTypeId() == APPOINTMENTTYPEID2) {
					if (masterDto.getSubSpecialityId().equals(dto.getSubSpecialityId())
							&& masterDto.getSpecialityId().equals(dto.getSpecialityId())
								&& masterDto.getModalityId().equals(dto.getModalityId())) {
						result = true;
					} else {
						result = false;
					}
				}
		 	}
		 	return result;
	} catch (Exception e) {
	logger.error("isAppointmentIsForSameDoctorWithoutCompletionOfPreviousAppintment failed", e);
	}
	return false;
	}

	public Boolean isAppointmentForSameDateTimeExist(SlotMasterDto slotMasterDto) {
		try {
				Boolean result = false; 
			
				Response res = iAppointmentSchedulingDao.isAppointmentForSameDateTimeExist(slotMasterDto);
				if(!(res.getListObject().size()>0))
			 		return false;
				
			 	SlotMasterDto masterDto = (SlotMasterDto)res.getObject();
			 	LocalTime slotFromTime=LocalTime.parse(masterDto.getFromTimeString());
			 	//LocalTime slotToTime=LocalTime.parse(masterDto.getToTimeString());
			 	
			 	ListIterator<SlotMasterDto> listIterator = res.getListObject().listIterator();
			 	while(listIterator.hasNext())
			 	{
			 		SlotMasterDto dto = listIterator.next();
			 		LocalTime fromTime=LocalTime.parse(dto.getFromTimeString());
			 		LocalTime toTime=LocalTime.parse(dto.getToTimeString());
			 		
			 		if(dto.getAppointmentDate().equals(masterDto.getAppointmentDate())){
			 			if(slotFromTime.isAfter(fromTime) && slotFromTime.isBefore(toTime)){
			 				result = true;
			 				if(result)
			 					return result;
			 			}else{
			 				result =  false;
			 			}
			 		}else{
			 			result = false;
			 		}
			 	}
			 return result;	
		} catch (Exception e) {
		logger.error("isAppointmentForSameDateTimeExist failed", e);
		}
		return false;
		
	}

	@Override
	@Transactional
	public Response scheduledAppointmentList(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iAppointmentSchedulingDao.scheduledAppointmentList(slotMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response cancelScheduledAppointment(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
				unitApplicationConfigurationDto.setOrganizationId(slotMasterDto.getOrganizationId());
				unitApplicationConfigurationDto.setUnitId(slotMasterDto.getUnitId());
			Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
			
			UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto)appConfigRes.getObject();

				if(slotMasterDto.getUpdatedBy() == null){
					slotMasterDto.setUpdatedBy(unitApplicationConfigurationDtos.getUpdatedBy());
				}

			Response result= iAppointmentSchedulingDao.cancelScheduledAppointment(slotMasterDto);
			
			if(iAppointmentSchedulingDao.isMyLifeAppointment(slotMasterDto.getAppointmentId()))
			{
				List<ScheduleMasterDto> scheduleMasterDtosList = new ArrayList();
				
				SlotMaster master = (SlotMaster) sessionFactory.getCurrentSession().get(SlotMaster.class, slotMasterDto.getSlotId());
				
					ScheduleMasterDto dto = new ScheduleMasterDto();	
						dto.setDoctorId(master.getDoctorId());				    	
						dto.setSpecialityId(master.getSpecialityId());				    	
						dto.setOrganisationId(master.getOrganizationId());				 
						dto.setUnitId(master.getUnitId());
						dto.setDeleteAllSlots('N');
						dto.setScheduleDate(GlobalCommonDateUtils.getStringDate(master.getAppointmentDate(),"dd-MM-yyyy"));
						dto.setFromTime(master.getFromTime().toString());
						dto.setToTime(master.getToTime().toString());
						dto.setAddRemove(2);
					scheduleMasterDtosList.add(dto);
	
				iDoctorScheduleDao.saveDoctorSchedule(scheduleMasterDtosList,"DELETE");
			}
			
			if(result.getStatus().equals(SUCCESS)) {
				
				 SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
					smsInfo.setUnitId(slotMasterDto.getUnitId());
					smsInfo.setOrganizationId(slotMasterDto.getOrganizationId());
					smsInfo.setPatientId(slotMasterDto.getPatientId());
					smsInfo.setReferringDoctorId(slotMasterDto.getDoctorId());
					smsInfo.setTreatingDoctorId(slotMasterDto.getDoctorId());
					smsInfo.setConsultingDoctorId(slotMasterDto.getDoctorId());
					smsInfo.setAdmitingDoctorId(slotMasterDto.getDoctorId());
					smsInfo.setDateTime(slotMasterDto.getAppointmentDate2()+" "+slotMasterDto.getFromTimeString());
				smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.CANCEL_APPOITMENT,smsInfo);
				//smsUtility.sendAppointmentCancelationSms(slotMasterDto);
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, result);
		} catch (Exception e) {
		logger.error("cancelScheduledAppointment failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response rescheduleAppointment(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
				unitApplicationConfigurationDto.setOrganizationId(slotMasterDto.getOrganizationId());
				unitApplicationConfigurationDto.setUnitId(slotMasterDto.getUnitId());
			Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
				UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto) appConfigRes.getObject();
					slotMasterDto.setSenderId(unitApplicationConfigurationDtos.getSenderId());

				boolean isMyLife = iAppointmentSchedulingDao.isMyLifeAppointment(slotMasterDto.getAppointmentId());
				if(isMyLife)
				{
					List<ScheduleMasterDto> scheduleMasterDtosList = new ArrayList();
					
					SlotMaster master = (SlotMaster) sessionFactory.getCurrentSession().get(SlotMaster.class, slotMasterDto.getSlotId());
					
						ScheduleMasterDto dto = new ScheduleMasterDto();	
							dto.setDoctorId(master.getDoctorId());				    	
							dto.setSpecialityId(master.getSpecialityId());				    	
							dto.setOrganisationId(master.getOrganizationId());				 
							dto.setUnitId(master.getUnitId());
							dto.setDeleteAllSlots('N');
							dto.setScheduleDate(GlobalCommonDateUtils.getStringDate(master.getAppointmentDate(),"dd-MM-yyyy"));
							dto.setFromTime(master.getFromTime().toString());
							dto.setToTime(master.getToTime().toString());
							dto.setAddRemove(2);
						scheduleMasterDtosList.add(dto);
		
					iDoctorScheduleDao.saveDoctorSchedule(scheduleMasterDtosList,"DELETE");
				}		
					
				slotMasterDto.setIsAppointmentFromMyLife(isMyLife ? 'Y' : 'N');
			Response result= iAppointmentSchedulingDao.rescheduleAppointment(slotMasterDto);
			AppointmentMaster appointmentMaster = (AppointmentMaster) result.getObject();
			slotMasterDto.setAppointmentIdOld(slotMasterDto.getAppointmentId());
			slotMasterDto.setAppointmentId(appointmentMaster.getAppointmentId());
			if(result.getStatus().equals(SUCCESS) && appointmentMaster.getAppointmentId() >0){
				syncAppointmentForMyLife(appointmentMaster, slotMasterDto);
			}
			appointmentMaster.setAppointmentStatusId(APPOINTMENTSTATUSID1);
			if(result.getStatus().equals(SUCCESS)) {

				String[] parts = slotMasterDto.getAppointmentDate2().split(" ");
				String appointmentDate = "";
				if(parts.length>0){
					appointmentDate = parts[0];
				}

				 SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
					smsInfo.setUnitId(slotMasterDto.getUnitId());
					smsInfo.setOrganizationId(slotMasterDto.getOrganizationId());
					smsInfo.setPatientId(slotMasterDto.getPatientId());
					smsInfo.setReferringDoctorId(slotMasterDto.getDoctorId());
					smsInfo.setTreatingDoctorId(slotMasterDto.getDoctorId());
					smsInfo.setAdmitingDoctorId(slotMasterDto.getDoctorId());
					smsInfo.setConsultingDoctorId(slotMasterDto.getDoctorId());
					smsInfo.setDateTime(appointmentDate +" "+ slotMasterDto.getFromTimeString());
				smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.RESCHEDULE_APPOITMENT,smsInfo);
				
				//smsUtility.rescheduledAppointmentSms(slotMasterDto);
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, result.getObject());
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response searchAppointmentsByCriteria(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iAppointmentSchedulingDao.searchAppointmentsByCriteria(slotMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response updateAppointmentStatus(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iAppointmentSchedulingDao.updateAppointmentStatus(slotMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response blockedScheduleAppointmentList(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iAppointmentSchedulingDao.blockedScheduleAppointmentList(slotMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response scheduleRadiologyAppointmentForInPatient(SlotMasterDto slotMasterDto) {
		try{
			boolean res = iAppointmentSchedulingDao.isAppointmentValid(slotMasterDto);
			if(res==false){
				return new Response(ERROR, COMMON_ERROR_CODE, "Slot is already booked !!", null, null);
			}else{
				Response res2 = iAppointmentSchedulingDao.scheduleAppointment(slotMasterDto);
				iAppointmentSchedulingDao.updateOrderDetailsIsAppointableServiceStatus(slotMasterDto.getOrderDetailsId(),slotMasterDto.getUpdatedBy());
				return res2;
			}
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	public Response markArrival(Integer appointmentId,String currentDate,Integer unitId)throws ApplicationException{
		try{
			return iAppointmentSchedulingDao.markArrival(appointmentId,currentDate,unitId);
		}catch(Exception e){
		logger.error("markArrival failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		
	}

	@Override
	@Transactional
	public Response markArrivalForPaidAppointment(EncounterMasterDto encounterMasterDto) {
		try{
			return iAppointmentSchedulingDao.markArrivalForPaidAppointments(encounterMasterDto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	@Override
	@Transactional
	public Response afterConversionPaidAppointment(EncounterMasterDto encounterMasterDto) {
		try{
			return iAppointmentSchedulingDao.afterConversionPaidAppointment(encounterMasterDto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	@Override
	@Transactional
	public Response batchJobToUpdateAppointmentStatus() throws ApplicationException {
		try{
			return iAppointmentSchedulingDao.batchJobToUpdateAppointmentStatus();
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	@Override
	@Transactional
	public Response batchJobToUpdateMultiVisitPackage(List<Integer> unitIds) throws ApplicationException {
		try{
			return iAppointmentSchedulingDao.batchJobToUpdateMultiVisitPackage(unitIds);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response assignCounterNumber(SlotMasterDto slotMasterDto) throws ApplicationException {
		try{
			return iAppointmentSchedulingDao.assignCounterNumber(slotMasterDto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response searchInProcessAppointment(Integer appointmentId) throws ApplicationException {
		try{
			return iAppointmentSchedulingDao.searchInProcessAppointment(appointmentId);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	@Override
	@Transactional
	public Response updateCallCount(Integer appointmentId,Integer callCount) throws ApplicationException {
		try{
			return iAppointmentSchedulingDao.updateCallCount(appointmentId,callCount);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response updateToken(Integer appointmentId, Integer tokenNumber) throws ApplicationException {
		try{
			return iAppointmentSchedulingDao.updateToken(appointmentId,tokenNumber);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	@Override
	@Transactional
	public Response assignPatientToAppointmentFromPatientRegistration(Integer appointmentId, Integer patientId) throws ApplicationException {
		try{
			return iAppointmentSchedulingDao.assignPatientToAppointmentFromPatientRegistration(appointmentId,patientId);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response updateTokenNursing(Integer encounterId, Integer tokenNumber ,Integer orderDetailsId) throws ApplicationException {
		try{
			return iAppointmentSchedulingDao.updateTokenNursing(encounterId,tokenNumber,orderDetailsId);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	@Override
	@Transactional
	public Response isAppointmentAlreadyExist(SlotMasterDto slotMasterDto) throws ApplicationException {
		try{
			if(this.isAppointmentForSameDateTimeExist(slotMasterDto)){
				return new Response(ERROR, ALREADY_EXIST_CODE, "This patient already has an appointment on this date and time !!", null, null);
			}
			if(this.isAppointmentIsForSameDoctorWithoutCompletionOfPreviousAppintment(slotMasterDto)){
				return new Response(ERROR, ALREADY_EXIST_CODE, "Previous appointment of same doctor or specialty is not yet closed !!", null, null);
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	public void syncAppointmentForMyLife(AppointmentMaster appointmentMaster,SlotMasterDto slotMasterDto) {
		if(appointmentMaster.getAppointmentTypeId() == APPOINTMENTTYPEID1){
			ScheduleLogsDto logDto = new ScheduleLogsDto(this);
			// insert into log table
			 String dateTime = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss");
			 logDto.setAction(ScheduleActions.INSERT);
			 logDto.setAddedBy(slotMasterDto.getCreatedBy());
			 logDto.setAddedDate(dateTime);
			 logDto.setDateTime(dateTime);
			 logDto.setApplication(ScheduleActions.HIS);
			 logDto.setErrorCount(0);
			 logDto.setPriority(ScheduleActions.PRIORITY_NORM);
			 logDto.setRecordId(appointmentMaster.getAppointmentId());
			 logDto.setStatus(ScheduleActions.STATUS_PENDING);
			 logDto.setOrgId(appointmentMaster.getOrganizationId());
			 logDto.setTableName(ScheduleTables.APPOINTMENT_MASTER);
			 logDto.setAppointmentStatusId(1);
			 isheduleDao.saveScheduleLogs(logDto);
			 
		}
	}
	
	public void syncAppointmentDetailsForMyLife(AppointmentMaster appointmentMaster,SlotMasterDto slotMasterDto) {
		if(appointmentMaster.getAppointmentTypeId() == APPOINTMENTTYPEID1){
			String dateTime = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss");
			 ScheduleLogsDto logDto1 = new ScheduleLogsDto(this);
						 logDto1.setAction(ScheduleActions.INSERT);
						 logDto1.setAddedBy(slotMasterDto.getCreatedBy());
						 logDto1.setAddedDate(dateTime);
						 logDto1.setDateTime(dateTime);
						 logDto1.setApplication(ScheduleActions.HIS);
						 logDto1.setErrorCount(0);
						 logDto1.setPriority(ScheduleActions.PRIORITY_NORM);
						 logDto1.setRecordId(appointmentMaster.getAppointmentId());
						 logDto1.setStatus(ScheduleActions.STATUS_PENDING);
						 logDto1.setTableName(ScheduleTables.APPOINTMENT_DETAILS);
						 isheduleDao.saveScheduleLogs(logDto1);
		}
	}
	public void syncRescheduleAppointmentForMyLife(AppointmentMaster appointmentMaster,SlotMasterDto slotMasterDto) {
		if(appointmentMaster.getAppointmentTypeId() == APPOINTMENTTYPEID1){
			ScheduleLogsDto logDto = new ScheduleLogsDto(this);
			// insert into log table
			 String dateTime = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss");
			 logDto.setAction(ScheduleActions.INSERT);
			 logDto.setAddedBy(slotMasterDto.getCreatedBy());
			 logDto.setAddedDate(dateTime);
			 logDto.setDateTime(dateTime);
			 logDto.setApplication(ScheduleActions.HIS);
			 logDto.setErrorCount(0);
			 logDto.setPriority(ScheduleActions.PRIORITY_NORM);
			 logDto.setRecordId(slotMasterDto.getAppointmentId());//previous appointment Id Log
			 logDto.setStatus(ScheduleActions.STATUS_PENDING);
			 logDto.setTableName(ScheduleTables.APPOINTMENT_MASTER);
			 isheduleDao.saveScheduleLogs(logDto);
		}
	}
	
	
	public void syncCancelAppointmentForMyLife(SlotMasterDto slotMasterDto) {
		ScheduleLogsDto logDto = new ScheduleLogsDto(this);
		// insert into log table
		 String dateTime = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss");
		 logDto.setAction(ScheduleActions.INSERT);
		 logDto.setAddedBy(slotMasterDto.getUpdatedBy());
		 logDto.setAddedDate(dateTime);
		 logDto.setDateTime(dateTime);
		 logDto.setApplication(ScheduleActions.HIS);
		 logDto.setErrorCount(0);
		 logDto.setPriority(ScheduleActions.PRIORITY_NORM);
		 logDto.setRecordId(slotMasterDto.getAppointmentId());
		 logDto.setStatus(ScheduleActions.STATUS_PENDING);
		 logDto.setTableName(ScheduleTables.APPOINTMENT_MASTER);
		 isheduleDao.saveScheduleLogs(logDto);
	}


	@Override
	@Transactional
	public Response blockSlot(Integer slotId, Integer statusId) {
		try{
			return iAppointmentSchedulingDao.blockSlot(slotId,statusId);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	@Override
	@Transactional
	public Response searchAppointmentsByPatienId(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iAppointmentSchedulingDao.searchAppointmentsByPatienId(slotMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response generateCyclicServiceAppointment(List<SlotMasterDto> slotMasterDto) {
		try {
			Date currentDate = TimeZoneComponent.getDateByzone(slotMasterDto.get(0).getUnitId());
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String strCurrentDate = formatter.format(currentDate);
			BigDecimal serviceAmt = BigDecimal.ZERO;
			BigDecimal taxAmt = BigDecimal.ZERO;
			BigDecimal netAmt = BigDecimal.ZERO;
			List<OrderDetailsMasterDto> orderDetailsMasterList = new ArrayList<OrderDetailsMasterDto>();
			List<OrderDetailsPayeeMapperDto> orderPayeeList = new ArrayList<OrderDetailsPayeeMapperDto>();
			List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtosList = new ArrayList<>();
			List<Integer> orderDetailsIdList = new LinkedList<>();
			Integer i = 0;
		
			EncounterMasterDto encounterMasterDto =new EncounterMasterDto();
			
			if (slotMasterDto.get(0).getParentPackageId() != null || slotMasterDto.get(0).getIsFromCoversheet().equals('Y')) {
				Response res = iEncounterMasterService.getEncounterDetailsById(slotMasterDto.get(0).getEncounterId());
				encounterMasterDto = (EncounterMasterDto) res.getObject();
			}
			// create order detail list
			for (SlotMasterDto slotDto : slotMasterDto) {
				if (slotDto.getParentPackageId() != null || slotDto.getIsFromCoversheet().equals('Y')) {
					String appDate[] = slotDto.getAppointmentDate().split("-");
					String scheduleOrderDate = appDate[2] + "-" + appDate[1] + "-" + appDate[0] + " 00:00:00";
					OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
					orderDetailsMasterDto.setServiceId(slotDto.getServiceMasterId());
					orderDetailsMasterDto.setCreatedBy(slotDto.getCreatedBy());
					orderDetailsMasterDto.setUpdatedBy(slotDto.getUpdatedBy());
					orderDetailsMasterDto
							.setOrderDate(GlobalCommonDateUtils.getLongDateFromStrigDate(scheduleOrderDate));
					orderDetailsMasterDto
							.setOrderSchDate(GlobalCommonDateUtils.getLongDateFromStrigDate(scheduleOrderDate));
					// orderDetailsMasterDto.setOrdRenderDatetime(GlobalCommonDateUtils.getLongDateFromStrigDate(slotDto.getCreatedDate()));
					orderDetailsMasterDto
							.setCreatedDate(GlobalCommonDateUtils.getLongDateFromStrigDate(strCurrentDate));
					orderDetailsMasterDto
							.setUpdatedDate(GlobalCommonDateUtils.getLongDateFromStrigDate(strCurrentDate));
					orderDetailsMasterDto.setStatus('A');
					orderDetailsMasterDto.setOrgId(slotDto.getOrganizationId());
					orderDetailsMasterDto.setOrgUnitId(slotDto.getUnitId());
					orderDetailsMasterDto.setOrderQty(1);
					orderDetailsMasterDto.setOrdCancelled('N');
					orderDetailsMasterDto.setServiceRenderingDeptId(slotDto.getSubSpecialityId());
					if (slotDto.getAdmissionId() != null) {
						orderDetailsMasterDto.setServiceRendered(0);
						orderDetailsMasterDto.setServiceIsBilled(0);
					} else {
						// for package as bill already paid
						orderDetailsMasterDto.setServiceRendered(0);
						orderDetailsMasterDto.setServiceIsBilled(1);
						orderDetailsMasterDto.setServiceBillId(slotDto.getBillId());
					}
					orderDetailsMasterDto.setPriorityId(2);
					orderDetailsMasterDto.setIsOutsourced('N');
					orderDetailsMasterDto.setOrdConcession(BigDecimal.ZERO);
					orderDetailsMasterDto.setOrdDiscount(BigDecimal.ZERO);
					orderDetailsMasterDto.setTariffId(slotDto.getDefaultSelfTariffId());
					orderDetailsMasterDto.setOrderEmergencyFlag('N');
					orderDetailsMasterDto.setIsDrug('N');
					orderDetailsMasterDto.setIsInvestigationOrder('N');
					orderDetailsMasterDto.setIsAppointableRadiology('N');
					orderDetailsMasterDto.setPatientClassPercentage(BigDecimal.ZERO);
					// orderDetailsMasterDto.setServiceIsBilled(0);
					orderDetailsMasterDto.setServiceAmt(slotDto.getServiceAmount());
					if (slotDto.getParentPackageId() != null)
						orderDetailsMasterDto.setPackageId(slotDto.getParentPackageId());

					if (slotDto.getTaxId() != null) {
						orderDetailsMasterDto.setTaxId(slotDto.getTaxId());
						orderDetailsMasterDto.setTaxPer(slotDto.getTaxPercentage());
						serviceAmt = orderDetailsMasterDto.getServiceAmt();
						taxAmt = serviceAmt.multiply((slotDto.getTaxPercentage().multiply(new BigDecimal(0.01))));
						netAmt = serviceAmt.add(taxAmt);
					} else {
						orderDetailsMasterDto.setTaxPer(new BigDecimal(0.00));
						serviceAmt = orderDetailsMasterDto.getServiceAmt();
						netAmt = serviceAmt;
					}

					orderDetailsMasterDto.setOrdTotalAmt(netAmt);
					orderDetailsMasterDto.setTaxAmount(taxAmt);
					orderDetailsMasterDto.setNetAmount(netAmt);
					orderDetailsMasterDto.setSelfPayable(serviceAmt);
					orderDetailsMasterDto.setPackageConsumptionAmt(BigDecimal.ZERO);
					// orderDetailsMasterDto.setPayeeId();
					orderDetailsMasterDto.setCreditPayable(serviceAmt);
					if (orderDetailsPackageMapperDtosList != null && orderDetailsPackageMapperDtosList.size() > 0) {
						orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(orderDetailsPackageMapperDtosList);
					}
					orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(orderPayeeList);
					orderDetailsMasterList.add(orderDetailsMasterDto);
				}
			}

			// place order master
			if (slotMasterDto.get(0).getParentPackageId() != null
					|| slotMasterDto.get(0).getIsFromCoversheet().equals('Y')) {
				SlotMasterDto slotMasterDto1 = slotMasterDto.get(0);
				OrderMasterDto orderMasterDto = new OrderMasterDto();
				orderMasterDto.setPatientId(slotMasterDto1.getPatientId());
				orderMasterDto.setCreatedBy(slotMasterDto1.getCreatedBy());
				orderMasterDto.setUpdatedBy(slotMasterDto1.getUpdatedBy());
				String appDate[] = slotMasterDto1.getAppointmentDate().split("-");
				String scheduleOrderDate = appDate[2] + "-" + appDate[1] + "-" + appDate[0] + " 00:00:00";
				orderMasterDto.setCreatedDate(GlobalCommonDateUtils.getLongDateFromStrigDate(strCurrentDate));
				orderMasterDto.setUpdatedDate(GlobalCommonDateUtils.getLongDateFromStrigDate(strCurrentDate));
				orderMasterDto.setOrderDate(GlobalCommonDateUtils.getLongDateFromStrigDate(scheduleOrderDate));
				orderMasterDto.setOrderStatus('A');
				orderMasterDto.setOrgId(slotMasterDto1.getOrganizationId());
				orderMasterDto.setOrgUnitId(slotMasterDto1.getUnitId());
				 orderMasterDto.setVisitTypeId(slotMasterDto1.getVisitTypeId());
				orderMasterDto.setPriorityId(2);
				orderMasterDto.setIsPharmacy('N');
				orderMasterDto.setOrderStatusId(1);//

				if (slotMasterDto1.getEncounterId() != null) {
					orderMasterDto.setEncounterId(slotMasterDto1.getEncounterId());
				}

				if (slotMasterDto1.getAdmissionId() != null) {
					orderMasterDto.setAdmissionId(slotMasterDto1.getAdmissionId());
				}

				// if service from package

				if (slotMasterDto1.getParentPackageId() != null) {
					OrderDetailsPackageMapperDto orderDetailsPackageMapperDto = new OrderDetailsPackageMapperDto();
					orderDetailsPackageMapperDto
							.setPackageId(slotMasterDto1.getOrderDetailsPackageMapperDto().getPackageId());
					orderDetailsPackageMapperDto.setPackageConsumptionMasterId(
							slotMasterDto1.getOrderDetailsPackageMapperDto().getPackageConsumptionMasterId());
					orderDetailsPackageMapperDto.setPacakgeConsumptionServiceDetailId(
							slotMasterDto1.getOrderDetailsPackageMapperDto().getPacakgeConsumptionServiceDetailId());
					orderDetailsPackageMapperDto.setPackageConsumptionEODetailId(
							slotMasterDto1.getOrderDetailsPackageMapperDto().getPackageConsumptionEODetailId());
					orderDetailsPackageMapperDto.setPackageConsumptionCapDetailsId(0);
					orderDetailsPackageMapperDto
							.setPayeeId(slotMasterDto1.getOrderDetailsPackageMapperDto().getPayeeId());
					// orderDetailsPackageMapperDto.setPayeeId(slotMasterDto1.getOrderDetailsPackageMapperDto().getPayeeId());
					orderDetailsPackageMapperDto.setPackageQty(1);
					orderDetailsPackageMapperDto.setServiceAmount(slotMasterDto1.getServiceAmount());
					orderDetailsPackageMapperDto.setPackageAmount(slotMasterDto1.getServiceAmount());
					orderDetailsPackageMapperDto.setIsPackageService('Y');
					orderDetailsPackageMapperDto.setIsCapService('Y');
					orderDetailsPackageMapperDto.setServiceId(slotMasterDto1.getServiceMasterId());
					orderDetailsPackageMapperDtosList.add(orderDetailsPackageMapperDto);
				}

				OrderDetailsPayeeMapperDto orderDetailPayeeMapperDto = new OrderDetailsPayeeMapperDto();
				orderDetailPayeeMapperDto.setPayeeAmount(slotMasterDto1.getServiceAmount());
				orderDetailPayeeMapperDto.setPayeeId(slotMasterDto1.getOrderDetailsPackageMapperDto().getPayeeId());
				orderDetailPayeeMapperDto.setServiceId(slotMasterDto1.getServiceMasterId());
				// orderDetailPayeeMapperDto.setContractId(contractId);
				orderPayeeList.add(orderDetailPayeeMapperDto);
				orderMasterDto.setListOrderDetailsMasterDto(orderDetailsMasterList);
				Response orderRes = iOrderMasterService.saveOrderMasterWithDiscount(orderMasterDto, null);
				orderDetailsIdList.addAll(orderRes.getListObject());
				// Response res=iOrderMasterService.saveOrderMaster(orderMasterDto);
				// slotDto.setOrderDetailsId((Integer)res.getResObject());

			}

			// place entry in bill detail incase of package
			if (slotMasterDto.get(0).getParentPackageId() != null
					|| slotMasterDto.get(0).getIsFromCoversheet().equals('Y')) {
				OrderDetailsMasterDto orderDetailsMasterDto = orderDetailsMasterList.get(0);

				if (slotMasterDto.get(0).getParentPackageId() != null && slotMasterDto.get(0).getBillId() != null) {
					for (i = 0; i < slotMasterDto.size(); i++) {
						BillingDetailsDto billingDetailsDto = new BillingDetailsDto();
						billingDetailsDto.setBillingMasterId(slotMasterDto.get(0).getBillId());
						billingDetailsDto.setServiceId(orderDetailsMasterDto.getServiceId());
						billingDetailsDto.setRate(orderDetailsMasterDto.getServiceAmt().doubleValue());
						billingDetailsDto.setQuantity(1.00);
						billingDetailsDto.setConcession(0.00);
						billingDetailsDto.setGrossAmount(orderDetailsMasterDto.getServiceAmt().doubleValue());
						billingDetailsDto.setCoPayPercentage( Double.valueOf(0.00));
						billingDetailsDto.setUnitId(slotMasterDto.get(0).getUnitId());
						billingDetailsDto.setOrganizationId(slotMasterDto.get(0).getOrganizationId());
						billingDetailsDto.setStatus('A');
						billingDetailsDto.setCreatedBy(orderDetailsMasterDto.getCreatedBy());
						billingDetailsDto.setUpdatedBy(orderDetailsMasterDto.getUpdatedBy());
						billingDetailsDto
								.setCreatedDate(GlobalCommonDateUtils.getLongDateFromStrigDate(strCurrentDate));
						billingDetailsDto
								.setUpdatedDate(GlobalCommonDateUtils.getLongDateFromStrigDate(strCurrentDate));
						billingDetailsDto.setDiscount(0.00);

						if (slotMasterDto.get(0).getTaxId() != null) {
							billingDetailsDto.setTaxId(slotMasterDto.get(0).getTaxId());
							billingDetailsDto
									.setTaxPer( Double.valueOf(slotMasterDto.get(0).getTaxPercentage().doubleValue()));
							serviceAmt = orderDetailsMasterDto.getServiceAmt();
							taxAmt = serviceAmt
									.multiply((slotMasterDto.get(0).getTaxPercentage().multiply(new BigDecimal(0.01))));
							netAmt = serviceAmt.add(taxAmt);

						} else {
							billingDetailsDto.setTaxPer(0.00);
							serviceAmt = orderDetailsMasterDto.getServiceAmt();
							netAmt = serviceAmt;
						}

						billingDetailsDto.setTaxAmount( Double.valueOf(taxAmt.doubleValue()));
						billingDetailsDto.setNetAmt(0.00);
						billingDetailsDto.setSelfPayable(0.00);
						billingDetailsDto.setCreditPayable(0.00);
						billingDetailsDto.setPayeeId(orderDetailsMasterDto.getPayeeId());
						billingDetailsDto.setOrderDetailsId(orderDetailsIdList.get(i));
						billingDetailsDto.setPackageId(orderDetailsMasterDto.getPackageId());
						billingDetailsDto.setPackageConsumptionAmt(orderDetailsMasterDto.getServiceAmt().doubleValue());
						billingDetailsDto.setIsDoctorSharePosted('N');

						List<OrderDetailsPayeeMapperDto> orderPayeeBillList = new ArrayList<OrderDetailsPayeeMapperDto>();
						OrderDetailsPayeeMapperDto orderDetailBillPayeeMapperDto = new OrderDetailsPayeeMapperDto();
						orderDetailBillPayeeMapperDto.setPayeeAmount(new BigDecimal(billingDetailsDto.getNetAmt()));
						orderDetailBillPayeeMapperDto.setPayeeId(slotMasterDto.get(0).getOrderDetailsPackageMapperDto().getPayeeId());
						orderDetailBillPayeeMapperDto.setServiceId(slotMasterDto.get(0).getServiceMasterId());
						// orderDetailPayeeMapperDto.setContractId(null);
						orderPayeeBillList.add(orderDetailBillPayeeMapperDto);

						billingDetailsDto.setOrderDetailsPayeeMapperDtosList(orderPayeeBillList);
						iBillingDao.saveBillingDetails(billingDetailsDto);
					}
				}
			}

			// appointment save call
			for (i = 0; i < slotMasterDto.size(); i++) {
				SlotMasterDto appointmentDto = slotMasterDto.get(i);

				if (appointmentDto.getOrderDetailsId() == null) {
					appointmentDto.setOrderDetailsId(orderDetailsIdList.get(i));
				}
				appointmentDto.setServiceId(appointmentDto.getServiceMasterId());
				appointmentDto.setIsCyclicAppointment('Y');
				appointmentDto.setAppointmentTypeId(APPOINTMENTTYPEID8);
				// generate appointment
				Response appRes = generateAppointment(appointmentDto);
				AppointmentMaster appMaster = (AppointmentMaster) appRes.getObject();
				appointmentDto.setAppointmentId(appMaster.getAppointmentId());
				iAppointmentSchedulingDao.createCyclicAppointmentLog(appointmentDto);
				if (appointmentDto.getParentPackageId() == null && appointmentDto.getIsFromCoversheet().equals('N')) {
					iAppointmentSchedulingDao.updateOrderDetailForServiceAppointment(appointmentDto);
				}
			}

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response searchCyclicServiceAppointmentsByPatienId(SlotMasterDto slotMasterDto) {
		try {
			return iAppointmentSchedulingDao.searchCyclicServiceAppointmentsByPatienId(slotMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response rescheduleCyclicServiceAppointment(SlotMasterDto slotMasterDto) {
		try {
			//render only lab services
			
			return iAppointmentSchedulingDao.rescheduleCyclicServiceAppointment(slotMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response getScheduledServiceAppointmentList(SlotMasterDto slotMasterDto) {
		try {
			return iAppointmentSchedulingDao.getScheduledServiceAppointmentList(slotMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response getDeptWiseServiceListForCyclicOrder(SlotMasterDto slotMasterDto) throws ApplicationException{
		try {
			return iAppointmentSchedulingDao.getDeptWiseServiceListForCyclicOrder(slotMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	@Override
	@Transactional
	public Response getCyclicDocWiseAndDeptWiseAppointmentListByPatientId(Integer patientId, Integer oranizationId) {
		try {
			return iAppointmentSchedulingDao.getCyclicDocWiseAndDeptWiseAppointmentListByPatientId(patientId,oranizationId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response getServiceListForCyclicOrder(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iAppointmentSchedulingDao.getServiceListForCyclicOrder(slotMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response rescheduleAppointmentForMylife(Integer oldAppointmentId,Integer newAppointmentId) throws ApplicationException {
		try {
			
			return iAppointmentSchedulingDao.rescheduleAppointmentForMylife(oldAppointmentId,newAppointmentId);
			
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response updateAppointmentPaidStatus(Integer appointmentId) throws ApplicationException {
    try {
			
			return iAppointmentSchedulingDao.updateAppointmentPaidStatus(appointmentId);
			
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	
	@Override
	@Transactional
	public Response savePatientDetailsFromWebApp(EncounterMasterDto encounterMasterDto) throws ApplicationException {
	try {
		SlotMasterDto slotMasterDto = new SlotMasterDto();
		slotMasterDto.setOrganizationId(encounterMasterDto.getOrganizationId());
		slotMasterDto.setUnitId(encounterMasterDto.getUnitId());
		slotMasterDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
		slotMasterDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
		slotMasterDto.setStatus('A');
		slotMasterDto.setSlotStatusId(4);
		slotMasterDto.setAppointmentStatusId(APPOINTMENTSTATUSID1);
		slotMasterDto.setPriorityId(1);
		if(encounterMasterDto.getAppointmentTypeId()!=null) {
			slotMasterDto.setAppointmentTypeId(encounterMasterDto.getAppointmentTypeId());
		}else {
			slotMasterDto.setAppointmentTypeId(encounterMasterDto.getAppointmentObject()!=null? encounterMasterDto.getAppointmentObject().getAppointmentTypeId():null);
		}
		
		slotMasterDto.setAppointmentDate2(encounterMasterDto.getAppointmentDate());
		slotMasterDto.setPatientId(encounterMasterDto.getPatientRegistrationDto().getPatientId());
		slotMasterDto.setSlotId(encounterMasterDto.getSlotId());
		slotMasterDto.setFromTimeString(encounterMasterDto.getFromTimeString());
		slotMasterDto.setDoctorId(encounterMasterDto.getDoctorId());
		slotMasterDto.setSpecialityId(encounterMasterDto.getSpecialityId());
		UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
		unitApplicationConfigurationDto.setOrganizationId(slotMasterDto.getOrganizationId());
		unitApplicationConfigurationDto.setUnitId(slotMasterDto.getUnitId());
	Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);

	unitApplicationConfigurationDto = (UnitApplicationConfigurationDto)appConfigRes.getObject();
		slotMasterDto.setUpdatedBy(unitApplicationConfigurationDto.getWebAppUserId()!=null ? unitApplicationConfigurationDto.getWebAppUserId():null);//need to ask
		slotMasterDto.setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId()!=null ? unitApplicationConfigurationDto.getWebAppUserId():null);
	
		if(slotMasterDto.getAppointmentTypeId() == APPOINTMENTTYPEID1) {
					slotMasterDto.setSenderId(unitApplicationConfigurationDto.getSenderId());
					slotMasterDto.setIsKiosk(unitApplicationConfigurationDto.getIsKioskRequired());
			String message = "Appointment Scheduled...!!";
			
			Response appRes = null;
			if(slotMasterDto.getAppointmentTypeId() == APPOINTMENTTYPEID1)
			{
				if(this.isAppointmentForSameDateTimeExist(slotMasterDto)){
					return new Response(ERROR, ALREADY_EXIST_CODE, "This patient already has an appointment on this date and time !!", null, null);
				}
				if(this.isAppointmentIsForSameDoctorWithoutCompletionOfPreviousAppintment(slotMasterDto)){
					return new Response(ERROR, ALREADY_EXIST_CODE, "Previous appointment of same doctor or specialty is not yet closed !!", null, null);
				}
			}
			boolean res = false;
			if(slotMasterDto.getAppointmentTypeId()==APPOINTMENTTYPEID1 && slotMasterDto.getSlotId() != null && slotMasterDto.getSlotId() > 0)
			{
				res = iAppointmentSchedulingDao.isAppointmentValid(slotMasterDto);
				if(res == false)
				{
					
						return new Response(ERROR, ALREADY_EXIST_CODE, "Slot is already booked, please select another slot...!!", null, null);
					
				}
			}
			if(slotMasterDto.getPatientId()==0)
			{
				PatientRegistrationDto patientRegistrationDto = new PatientRegistrationDto();
					patientRegistrationDto.setOrganizationId(slotMasterDto.getOrganizationId());
					patientRegistrationDto.setUnitId(slotMasterDto.getUnitId());
					patientRegistrationDto.setPrefixId(encounterMasterDto.getPatientRegistrationDto().getPrefixId());
					patientRegistrationDto.setFirstName(encounterMasterDto.getPatientRegistrationDto().getFirstName());
					patientRegistrationDto.setMiddleName(encounterMasterDto.getPatientRegistrationDto().getMiddleName());
					patientRegistrationDto.setLastName(encounterMasterDto.getPatientRegistrationDto().getLastName());
					patientRegistrationDto.setDob(encounterMasterDto.getPatientRegistrationDto().getDob());
					patientRegistrationDto.setGenderId(encounterMasterDto.getPatientRegistrationDto().getGenderId());
					patientRegistrationDto.setMobileNumber(encounterMasterDto.getPatientRegistrationDto().getMobileNumber());
					patientRegistrationDto.setEmail(encounterMasterDto.getPatientRegistrationDto().getEmail()!=null ? encounterMasterDto.getPatientRegistrationDto().getEmail() : null);
					patientRegistrationDto.setRegistrationTypeId(1);
					patientRegistrationDto.setCreatedBy(slotMasterDto.getCreatedBy());
					patientRegistrationDto.setCreatedDate(slotMasterDto.getCreatedDate());
					patientRegistrationDto.setUpdatedBy(slotMasterDto.getUpdatedBy());
					patientRegistrationDto.setUpdatedDate(slotMasterDto.getCreatedDate());
					patientRegistrationDto.setPatientCategoryId(unitApplicationConfigurationDto.getDefaultPatientCategoryId());
					patientRegistrationDto.setStatus('A');
					patientRegistrationDto.setIdentificationNumber(encounterMasterDto.getPatientRegistrationDto().getIdentificationNumber());
					patientRegistrationDto.setIdentificationTypeId(encounterMasterDto.getPatientRegistrationDto().getIdentificationTypeId());
					patientRegistrationDto.setCountryCallingCode(encounterMasterDto.getPatientRegistrationDto().getCountryCallingCode());
					patientRegistrationDto.setCountryId(encounterMasterDto.getPatientRegistrationDto().getCountryId()!=null ? encounterMasterDto.getPatientRegistrationDto().getCountryId():null);
					patientRegistrationDto.setDistrictId(encounterMasterDto.getPatientRegistrationDto().getDistrictId()!=null ? encounterMasterDto.getPatientRegistrationDto().getDistrictId():null);
					if(encounterMasterDto.getPatientRegistrationDto().getKinList()!=null && encounterMasterDto.getPatientRegistrationDto().getKinList().size()>0) {
						encounterMasterDto.getPatientRegistrationDto().getKinList().get(0).setOrganizationId(slotMasterDto.getOrganizationId());
						encounterMasterDto.getPatientRegistrationDto().getKinList().get(0).setUnitId(slotMasterDto.getUnitId());
						encounterMasterDto.getPatientRegistrationDto().getKinList().get(0).setCreatedBy(slotMasterDto.getCreatedBy());
						encounterMasterDto.getPatientRegistrationDto().getKinList().get(0).setUpdatedBy(slotMasterDto.getUpdatedBy());
					}
					patientRegistrationDto.setKinList(encounterMasterDto.getPatientRegistrationDto().getKinList()!=null ? encounterMasterDto.getPatientRegistrationDto().getKinList():null);
				Response patientRegistraion = patientRegistrationServiceImpl.savePatientRegistration(patientRegistrationDto);
				if(patientRegistraion.getStatus().equals(SUCCESS)) {
					slotMasterDto.setPatientId((Integer)patientRegistraion.getObject());
					
					
					//send sms and email
					if(patientRegistrationDto.getMobileNo()!=null) {
						SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
						smsInfo.setOrganizationId(patientRegistrationDto.getOrganizationId());
						smsInfo.setUnitId(patientRegistrationDto.getUnitId());
						smsInfo.setPatientId(patientRegistrationDto.getPatientId());
					smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.REGISTRATION,smsInfo);
					}
				
				if(patientRegistrationDto.getEmail()!=null && patientRegistrationDto.getEmail().length()>0) 
				{
					//iMailingService.sentRegistrationMailToPatient(patientRegistrationDto);
					SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
					emailinfo.setUnitId(patientRegistrationDto.getUnitId());
					emailinfo.setOrganizationId(patientRegistrationDto.getOrganizationId());
					emailinfo.setPatientEmail(patientRegistrationDto.getEmail());
					emailinfo.setPatientId((Integer)patientRegistraion.getObject());
					smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.REGISTRATION, emailinfo);
				}
				
				}
				else {
					return new Response(ERROR, null, "Patient Already Exist !!", null, null);
				}
				
				String patientResMessage = patientRegistraion.getMessage();
				String[] splitStr = patientResMessage.trim().split("\\s+");
				
				message="Appointment scheduled for pre registered patient "+splitStr[0]+"..!!";
			}
		
			AppointmentMaster appointmentMaster = null;
			appRes = iAppointmentSchedulingDao.scheduleAppointment(slotMasterDto);
			appointmentMaster = (AppointmentMaster)appRes.getObject();
			
			//save Custom Patient Details if any
			if(encounterMasterDto.getPatientRegistrationDto().getCustomDataPost()!=null || encounterMasterDto.getPatientRegistrationDto().getCustomDataDisplay()!=null) {
				CustomPatientDetailsDto customPatientDetailsObj = new CustomPatientDetailsDto();
				customPatientDetailsObj.setPatientId(slotMasterDto.getPatientId());
				customPatientDetailsObj.setAppointmentId(appointmentMaster.getAppointmentId());
				customPatientDetailsObj.setEncounterId(null);//in case of hospital visit required
				customPatientDetailsObj.setOrganizationId(slotMasterDto.getOrganizationId());
				customPatientDetailsObj.setUnitId(slotMasterDto.getUnitId());
				customPatientDetailsObj.setStatus('A');
				customPatientDetailsObj.setCreatedBy(slotMasterDto.getCreatedBy());
				customPatientDetailsObj.setCreatedDate(slotMasterDto.getCreatedDate());
				customPatientDetailsObj.setUpdatedBy(slotMasterDto.getUpdatedBy());
				customPatientDetailsObj.setUpdatedDate(slotMasterDto.getCreatedDate());
				customPatientDetailsObj.setCustomDataPost(encounterMasterDto.getPatientRegistrationDto().getCustomDataPost()!=null 
						? encounterMasterDto.getPatientRegistrationDto().getCustomDataPost():null);
				customPatientDetailsObj.setCustomDataDisplay(encounterMasterDto.getPatientRegistrationDto().getCustomDataDisplay()!=null
						? encounterMasterDto.getPatientRegistrationDto().getCustomDataDisplay():null);
				Response customePatientRegistration = patientRegistrationServiceImpl.saveCustomPatientDetails(customPatientDetailsObj);
			}
			
			
			//to remove it from clinic if patient is not sync to clinic
			if(slotMasterDto.getAppointmentTypeId()==APPOINTMENTTYPEID1 && iAppointmentSchedulingDao.isPatientSync(slotMasterDto.getPatientId())==false && slotMasterDto.getSlotId()!=null)
			{
				List<ScheduleMasterDto> scheduleMasterDtosList = new ArrayList();
				
				SlotMaster master = (SlotMaster) sessionFactory.getCurrentSession().get(SlotMaster.class, slotMasterDto.getSlotId());
				
				ScheduleMasterDto dto = new ScheduleMasterDto();	
					dto.setDoctorId(master.getDoctorId());				    	
					dto.setSpecialityId(master.getSpecialityId());				    	
					dto.setOrganisationId(master.getOrganizationId());				 
					dto.setUnitId(master.getUnitId());
					dto.setDeleteAllSlots('N');
					dto.setScheduleDate(GlobalCommonDateUtils.getStringDate(master.getAppointmentDate(),"dd-MM-yyyy"));
					dto.setFromTime(master.getFromTime().toString());
					dto.setToTime(master.getToTime().toString());
					dto.setAddRemove(1);
				scheduleMasterDtosList.add(dto);

				iDoctorScheduleDao.saveDoctorSchedule(scheduleMasterDtosList,"DELETE");
			}
			
			if(appRes.getStatus().equals(SUCCESS) && slotMasterDto.getDoctorId()!=null){
				SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
					smsInfo.setOrganizationId(slotMasterDto.getOrganizationId());
					smsInfo.setUnitId(slotMasterDto.getUnitId());
				smsInfo.setDateTime(slotMasterDto.getAppointmentDate2() != null
						? (slotMasterDto.getAppointmentDate2().substring(0, 10) + " at "
								+ slotMasterDto.getFromTimeString())
						: (slotMasterDto.getAppointmentDate().substring(0, 10) + " at "
								+ slotMasterDto.getFromTimeString()));
					smsInfo.setPatientId(slotMasterDto.getPatientId());
					smsInfo.setConsultingDoctorId(slotMasterDto.getDoctorId());
					smsInfo.setAppointmentId(appointmentMaster.getAppointmentId());
				smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.APPOITMENT,smsInfo);
			}
			return new Response(SUCCESS, SUCCESS_CODE, message, null, appointmentMaster);
			
			
		}else if(slotMasterDto.getAppointmentTypeId() == APPOINTMENTTYPEID7) {
			Integer orderId = null;
			boolean isExistingPatient = false;
			encounterMasterDto.setUpdatedBy(unitApplicationConfigurationDto.getWebAppUserId()!=null ? unitApplicationConfigurationDto.getWebAppUserId():null);//need to ask
			encounterMasterDto.setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId()!=null ? unitApplicationConfigurationDto.getWebAppUserId():null);
			encounterMasterDto.setStatus('A');
			encounterMasterDto.setPaymentEntitlementId(SELF);
			encounterMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()).getTime());
			encounterMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()).getTime());
			List<PatientSponsorDetailsDto> sponserList = new ArrayList<>();
			PatientSponsorDetailsDto patientSponsorDetailsDto = new PatientSponsorDetailsDto();
			patientSponsorDetailsDto.setCreatedBy(encounterMasterDto.getCreatedBy());
			patientSponsorDetailsDto.setOrganizationId(encounterMasterDto.getOrganizationId());
			patientSponsorDetailsDto.setUnitId(encounterMasterDto.getUnitId());
			patientSponsorDetailsDto.setVisitTypeId(OP);
			patientSponsorDetailsDto.setPaymentEntitlementId(SELF);
			patientSponsorDetailsDto.setPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
			patientSponsorDetailsDto.setPriorityId(1);
			patientSponsorDetailsDto.setTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
			patientSponsorDetailsDto.setCreatedBy(0);
			patientSponsorDetailsDto.setUpdatedBy(0);
			patientSponsorDetailsDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			patientSponsorDetailsDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			patientSponsorDetailsDto.setStatus('A');
			sponserList.add(patientSponsorDetailsDto);
			encounterMasterDto.setPatientSponsorDetailsDtosList(sponserList);
			if(encounterMasterDto.getPatientSponsorDetailsDtosList()!=null && encounterMasterDto.getPatientSponsorDetailsDtosList().size()>0)
			{
				for(PatientSponsorDetailsDto patientSponsorDetailsDto1 : encounterMasterDto.getPatientSponsorDetailsDtosList())
				{
					if(patientSponsorDetailsDto1.getPriorityId()==1){
						encounterMasterDto.setPaymentEntitlementId(patientSponsorDetailsDto1.getPaymentEntitlementId());
						encounterMasterDto.setPaymentEntitlementTypeId(patientSponsorDetailsDto1.getPaymentEntitlementId());
					}
						
				}
			}
			
			
			//set kin details
			if(encounterMasterDto.getPatientRegistrationDto().getKinList()!=null && encounterMasterDto.getPatientRegistrationDto().getKinList().size()>0) {
				encounterMasterDto.getPatientRegistrationDto().getKinList().get(0).setOrganizationId(slotMasterDto.getOrganizationId());
				encounterMasterDto.getPatientRegistrationDto().getKinList().get(0).setUnitId(slotMasterDto.getUnitId());
				encounterMasterDto.getPatientRegistrationDto().getKinList().get(0).setCreatedBy(slotMasterDto.getCreatedBy());
				encounterMasterDto.getPatientRegistrationDto().getKinList().get(0).setUpdatedBy(slotMasterDto.getUpdatedBy());
			}
			if(encounterMasterDto.getPatientRegistrationDto().getPatientId()==null || encounterMasterDto.getPatientRegistrationDto().getPatientId()==0)
			{
				encounterMasterDto.getPatientRegistrationDto().setRegistrationTypeId(1);
				encounterMasterDto.getPatientRegistrationDto().setDob(encounterMasterDto.getPatientRegistrationDto().getDob()+" 00:00:00");
				encounterMasterDto.getPatientRegistrationDto().setCreatedBy(encounterMasterDto.getCreatedBy());
				encounterMasterDto.getPatientRegistrationDto().setUpdatedBy(encounterMasterDto.getUpdatedBy());
				Response patRes = iPatientRegistrationService.registerPatient(encounterMasterDto.getPatientRegistrationDto());
				if(patRes.getStatus().equals(SUCCESS))
				{
					encounterMasterDto.setPatientRegistrationDto((PatientRegistrationDto)patRes.getResObject());
					encounterMasterDto.setPatientId((Integer)patRes.getObject());
					encounterMasterDto.setPatientCategoryId(encounterMasterDto.getPatientRegistrationDto().getPatientCategoryId());
					if(encounterMasterDto.getPatientRegistrationDto()!=null) {
						//send sms and email
						if(encounterMasterDto.getPatientRegistrationDto().getMobileNo()!=null) {
							SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
							smsInfo.setUnitId(encounterMasterDto.getUnitId());
							smsInfo.setOrganizationId(encounterMasterDto.getOrganizationId());
							smsInfo.setPatientId(encounterMasterDto.getPatientId());
						smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.REGISTRATION,smsInfo);
						}
					
					if( encounterMasterDto.getPatientRegistrationDto().getEmail()!=null && encounterMasterDto.getPatientRegistrationDto().getEmail().length()>0) 
						{
							SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
							emailinfo.setUnitId(encounterMasterDto.getUnitId());
							emailinfo.setOrganizationId(encounterMasterDto.getOrganizationId());
							emailinfo.setPatientEmail(encounterMasterDto.getPatientRegistrationDto().getEmail());
							emailinfo.setPatientId(encounterMasterDto.getPatientId());
							smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.REGISTRATION, emailinfo);
						}
					}
					
					
					
					if(patRes.getListObject()!=null) {
						orderId = (Integer)patRes.getListObject().get(0);
					}else {
						isExistingPatient =true;
					}
					if(encounterMasterDto.getAppointmentObject()!=null)
						encounterMasterDto.getAppointmentObject().setPatientId((Integer)patRes.getObject());
				}else if(patRes.getCode().equals(ALREADY_EXIST_CODE)){
					return patRes;
				}
				
			}else {
				isExistingPatient = true;
				if(encounterMasterDto.getAppointmentObject()!=null) {
					encounterMasterDto.getAppointmentObject().setPatientId(encounterMasterDto.getPatientRegistrationDto().getPatientId());
					encounterMasterDto.setPatientId(encounterMasterDto.getPatientRegistrationDto().getPatientId());
				}
					
			}
			Response res = iEncounterMasterService.generateEncounter(encounterMasterDto);
				encounterMasterDto = (EncounterMasterDto)res.getObject();
			if(res.getStatus().equals(SUCCESS))	
			{
				if(encounterMasterDto.getListServiceMasterDto()!=null && encounterMasterDto.getListServiceMasterDto().size()>0) {
					
					if(isExistingPatient) {
						//save order mater
						OrderMasterDto orderMasterDto = new OrderMasterDto();
						orderMasterDto.setVisitTypeId(OP);// op==1
						orderMasterDto.setPatientId(encounterMasterDto.getPatientId());
						orderMasterDto.setAdmissionId(null);
						orderMasterDto.setEncounterId(encounterMasterDto.getEncounterId());
						orderMasterDto.setCreatedBy(encounterMasterDto.getCreatedBy());
						orderMasterDto.setUpdatedBy(encounterMasterDto.getCreatedBy());
						orderMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()).getTime());
						orderMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()).getTime());
						orderMasterDto.setOrderDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()).getTime());
						orderMasterDto.setOrderStatus('A');
						orderMasterDto.setOrderNo(iGlobalUnitMasterDao.getGeneratedNo(0, encounterMasterDto.getUnitId(), IBillingCommonConstants.ORDER_NUMBER));
						orderMasterDto.setOrgId(encounterMasterDto.getOrganizationId());
						orderMasterDto.setOrgUnitId(encounterMasterDto.getUnitId());
						orderMasterDto.setOrderStatusId(1);// order status==1
					Response orderMaterRes = iOrderMasterDao.saveOrderMaster(orderMasterDto);
						if(orderMaterRes!=null && orderMaterRes.getObject()!=null) {
							orderId = (Integer)orderMaterRes.getObject();
						}
					}else {
						//update order master
						OrderMasterDto orderMasterDto = new OrderMasterDto();
						orderMasterDto.setVisitTypeId(OP);
						orderMasterDto.setEncounterId(encounterMasterDto.getEncounterId());
						orderMasterDto.setOrderId(orderId);
						iOrderMasterService.updateOrderMasterForWebApp(orderMasterDto);
					}
					for (ServiceMasterDto serviceMasterDto : encounterMasterDto.getListServiceMasterDto()) {
						ContractConsumptionDto contrcDto = new ContractConsumptionDto();
						
						contrcDto.setOrganizationId(encounterMasterDto.getOrganizationId());
						contrcDto.setUnitId(encounterMasterDto.getUnitId());
						contrcDto.setEncounterId(encounterMasterDto.getEncounterId());
						contrcDto.setBillingBedCategoryId(unitApplicationConfigurationDto.getOpdBillingCatId());
						contrcDto.setPatientCategoryId(unitApplicationConfigurationDto.getDefaultPatientCategoryId());
						contrcDto.setVisitTypeId(OP);
						contrcDto.setServiceId(serviceMasterDto.getServiceMasterId());
						contrcDto.setTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
						contrcDto.setProcedureId(null);
						contrcDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()),"yyyy-MM-dd"), "yyyy-MM-dd"));
						Response serviceRateRes = iContractConsumptionService.getSerivceRateGlobal(contrcDto, false, false);
						ContractConsumptionDto consumptionDto=(ContractConsumptionDto)serviceRateRes.getObject();
						if(serviceRateRes.getObject()!=null && consumptionDto.getFinalPrice()>0)
						{
							OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
							orderDetailsMasterDto.setServiceId(serviceMasterDto.getServiceMasterId());
							orderDetailsMasterDto.setServiceIsBilled(0);
							orderDetailsMasterDto.setOrderQty(1);
							orderDetailsMasterDto.setCreatedBy(encounterMasterDto.getCreatedBy());
							orderDetailsMasterDto.setUpdatedBy(encounterMasterDto.getUpdatedBy());
							orderDetailsMasterDto.setOrderDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId() != null ? encounterMasterDto.getUnitId():0).getTime());
							orderDetailsMasterDto.setOrderSchDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId() != null ? encounterMasterDto.getUnitId():0).getTime());
							orderDetailsMasterDto.setOrdRenderDatetime(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId() != null ? encounterMasterDto.getUnitId():0).getTime());
							orderDetailsMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId() != null ? encounterMasterDto.getUnitId():0).getTime());
							orderDetailsMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId() != null ? encounterMasterDto.getUnitId():0).getTime());
							orderDetailsMasterDto.setStatus('A');
							orderDetailsMasterDto.setOrgId(encounterMasterDto.getOrganizationId());
							orderDetailsMasterDto.setOrgUnitId(encounterMasterDto.getUnitId());
							orderDetailsMasterDto.setOrdCancelled('N');
							orderDetailsMasterDto.setNetAmount(BigDecimal.ZERO);
							orderDetailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
							orderDetailsMasterDto.setOrdConcession(BigDecimal.valueOf(consumptionDto.getConcession()));
							orderDetailsMasterDto.setTaxAmount(BigDecimal.valueOf(consumptionDto.getTaxAmt()));
							orderDetailsMasterDto.setTaxPer(BigDecimal.valueOf(consumptionDto.getTaxPercentage()));
							orderDetailsMasterDto.setOrdDiscount(new BigDecimal(0.00));
							orderDetailsMasterDto.setTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
							orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
							orderDetailsMasterDto.setOrderEmergencyFlag('N');
							//orderDetailsMasterDto.setServiceRendered(serviceMasterDto.getServiceMasterId().equals(serviceMasterDto.getServiceMasterId()) ? 1 : 0);
							orderDetailsMasterDto.setServiceRendered(0);
							orderDetailsMasterDto.setOrdRenderDatetime(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId() != null ? encounterMasterDto.getUnitId():0).getTime());
							orderDetailsMasterDto.setServiceChargeable(1);
							orderDetailsMasterDto.setServiceIsBilled(0);
							orderDetailsMasterDto.setIsDrug('N');
							orderDetailsMasterDto.setPriorityId(2);
							orderDetailsMasterDto.setServiceRenderingDeptId(consumptionDto.getSubSpecialityId()!= null ? consumptionDto.getSubSpecialityId() : null);
							orderDetailsMasterDto.setIsOrderVisibleDepartment('A');
							orderDetailsMasterDto.setIsOutsourced('N');
							orderDetailsMasterDto.setDepartmentId(consumptionDto.getSpecialityId());
							orderDetailsMasterDto.setIsDocReq(consumptionDto.getIsDocReq());
							orderDetailsMasterDto.setIsRefDoctorShare(consumptionDto.getIsRefReq());
							orderDetailsMasterDto.setOrdTotalAmt(new BigDecimal(consumptionDto.getServiceRate()));
							orderDetailsMasterDto.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()));
							orderDetailsMasterDto.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()));
							orderDetailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
							orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
							orderDetailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
							orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
							orderDetailsMasterDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
							orderDetailsMasterDto.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
							orderDetailsMasterDto.setDiscountOwnership(unitApplicationConfigurationDto.getDefaultDiscountOwnership());
							orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
							if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
								orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
							orderDetailsMasterDto.setOrderId(orderId);
							iOrderDetailsMasterDao.saveOrderDetailsMaster(orderDetailsMasterDto);
							
							
						}
					}
				}
				
				//save Custom Patient Details if any
				if(encounterMasterDto.getPatientRegistrationDto().getCustomDataPost()!=null || encounterMasterDto.getPatientRegistrationDto().getCustomDataDisplay()!=null) {
					CustomPatientDetailsDto customPatientDetailsObj = new CustomPatientDetailsDto();
					customPatientDetailsObj.setPatientId(encounterMasterDto.getPatientId());
					customPatientDetailsObj.setAppointmentId(null);
					customPatientDetailsObj.setEncounterId(encounterMasterDto.getEncounterId());//in case of hospital visit required
					customPatientDetailsObj.setOrganizationId(encounterMasterDto.getOrganizationId());
					customPatientDetailsObj.setUnitId(encounterMasterDto.getUnitId());
					customPatientDetailsObj.setStatus('A');
					customPatientDetailsObj.setCreatedBy(encounterMasterDto.getCreatedBy());
					customPatientDetailsObj.setCreatedDate(slotMasterDto.getCreatedDate());
					customPatientDetailsObj.setUpdatedBy(slotMasterDto.getUpdatedBy());
					customPatientDetailsObj.setUpdatedDate(slotMasterDto.getCreatedDate());
					customPatientDetailsObj.setCustomDataPost(encounterMasterDto.getPatientRegistrationDto().getCustomDataPost()!=null 
							? encounterMasterDto.getPatientRegistrationDto().getCustomDataPost():null);
					customPatientDetailsObj.setCustomDataDisplay(encounterMasterDto.getPatientRegistrationDto().getCustomDataDisplay()!=null
							? encounterMasterDto.getPatientRegistrationDto().getCustomDataDisplay():null);
					Response customePatientRegistration = patientRegistrationServiceImpl.saveCustomPatientDetails(customPatientDetailsObj);
				}
				
				
				//send appointment sms and emial
				if(encounterMasterDto != null  && res.getStatus().equals(SUCCESS))
				{
					String appointmentDate;
					String appointmentTime;
					
					appointmentDate = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()), "dd/MM/yyyy") ;
					appointmentTime = encounterMasterDto.getAppoitmentTime() != null
							? encounterMasterDto.getAppoitmentTime()
							: (encounterMasterDto.getAppointmentObject() != null
									? encounterMasterDto.getAppointmentObject().getFromTimeString()
									: encounterMasterDto.getEncounterTime().toString()); 
						
					SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
						smsInfo.setUnitId(encounterMasterDto.getUnitId());
						smsInfo.setPatientId(encounterMasterDto.getPatientId());
						smsInfo.setConsultingDoctorId(encounterMasterDto.getDoctorId()!= null ? encounterMasterDto.getDoctorId() : null);
						smsInfo.setReferringDoctorId(encounterMasterDto.getReferralId() != null ? encounterMasterDto.getReferralId() : null);
						smsInfo.setDateTime(appointmentDate+" at "+appointmentTime);
						smsInfo.setEncounterTypeId(encounterMasterDto.getEncounterTypeId());
						smsInfo.setOrganizationId(encounterMasterDto.getOrganizationId());
						smsInfo.setEncounterId(encounterMasterDto.getEncounterId());
					smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.ENCOUNTER,smsInfo);
					
						
						smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.ENCOUNTER, smsInfo);
				}
			}
			
			return res;
		}
			
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
		

	@Override
	@Transactional
	public Response getAppointMentDetailsByPatientId(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iAppointmentSchedulingDao.getAppointMentDetailsByPatientId(slotMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response savePatientDetailsFromWebAndMobileApp(PatientRegistrationDto  patientRegistrationDto)
			throws ApplicationException {
		try {
			
			EncounterMasterDto encounterMasterDto = new EncounterMasterDto();
			encounterMasterDto.setOrganizationId(patientRegistrationDto.getOrganizationId());
			encounterMasterDto.setUnitId(patientRegistrationDto.getUnitId());
			encounterMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()).getTime());
			encounterMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()).getTime());
			encounterMasterDto.setEncounterTypeId(7);
			encounterMasterDto.setVisitTypeId(OP);
				SlotMasterDto appointmentObject = new SlotMasterDto();
				appointmentObject.setOrganizationId(encounterMasterDto.getOrganizationId());
				appointmentObject.setUnitId(encounterMasterDto.getUnitId());
				appointmentObject.setAppointmentTypeId(encounterMasterDto.getEncounterTypeId());
			encounterMasterDto.setAppointmentObject(appointmentObject);
			encounterMasterDto.setPatientTypeId(1);
			encounterMasterDto.setPatientRegistrationDto(patientRegistrationDto);
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setOrganizationId(encounterMasterDto.getOrganizationId());
			unitApplicationConfigurationDto.setUnitId(encounterMasterDto.getUnitId());
			Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
				unitApplicationConfigurationDto = (UnitApplicationConfigurationDto)appConfigRes.getObject();
				
				encounterMasterDto.setUpdatedBy(unitApplicationConfigurationDto.getWebAppUserId()!=null ? unitApplicationConfigurationDto.getWebAppUserId():null);//need to ask
				encounterMasterDto.setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId()!=null ? unitApplicationConfigurationDto.getWebAppUserId():null);
			
			if(encounterMasterDto.getEncounterTypeId() == 7) {
				Integer orderId = null;
				encounterMasterDto.setUpdatedBy(unitApplicationConfigurationDto.getWebAppUserId()!=null ? unitApplicationConfigurationDto.getWebAppUserId():null);//need to ask
				encounterMasterDto.setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId()!=null ? unitApplicationConfigurationDto.getWebAppUserId():null);
				encounterMasterDto.setStatus('A');
				encounterMasterDto.setPaymentEntitlementId(SELF);
				encounterMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()).getTime());
				encounterMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()).getTime());
				encounterMasterDto.setDeptWiseAppointment('N');
				List<PatientSponsorDetailsDto> sponserList = new ArrayList<>();
				PatientSponsorDetailsDto patientSponsorDetailsDto = new PatientSponsorDetailsDto();
				patientSponsorDetailsDto.setCreatedBy(encounterMasterDto.getCreatedBy());
				patientSponsorDetailsDto.setOrganizationId(encounterMasterDto.getOrganizationId());
				patientSponsorDetailsDto.setUnitId(encounterMasterDto.getUnitId());
				patientSponsorDetailsDto.setVisitTypeId(OP);
				patientSponsorDetailsDto.setPaymentEntitlementId(SELF);
				patientSponsorDetailsDto.setPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
				patientSponsorDetailsDto.setPriorityId(1);
				patientSponsorDetailsDto.setTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
				patientSponsorDetailsDto.setCreatedBy(0);
				patientSponsorDetailsDto.setUpdatedBy(0);
				patientSponsorDetailsDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
				patientSponsorDetailsDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
				patientSponsorDetailsDto.setStatus('A');
				sponserList.add(patientSponsorDetailsDto);
				encounterMasterDto.setPatientSponsorDetailsDtosList(sponserList);
				if(encounterMasterDto.getPatientSponsorDetailsDtosList()!=null && encounterMasterDto.getPatientSponsorDetailsDtosList().size()>0)
				{
					for(PatientSponsorDetailsDto patientSponsorDetailsDto1 : encounterMasterDto.getPatientSponsorDetailsDtosList())
					{
						if(patientSponsorDetailsDto1.getPriorityId()==1){
							encounterMasterDto.setPaymentEntitlementId(patientSponsorDetailsDto1.getPaymentEntitlementId());
							encounterMasterDto.setPaymentEntitlementTypeId(patientSponsorDetailsDto1.getPaymentEntitlementId());
						}
							
					}
				}
				
				
				//set kin details
				if(patientRegistrationDto.getKinList()!=null && patientRegistrationDto.getKinList().size()>0) {
					patientRegistrationDto.getKinList().get(0).setOrganizationId(encounterMasterDto.getOrganizationId());
					patientRegistrationDto.getKinList().get(0).setUnitId(encounterMasterDto.getUnitId());
					patientRegistrationDto.getKinList().get(0).setCreatedBy(encounterMasterDto.getCreatedBy());
					patientRegistrationDto.getKinList().get(0).setUpdatedBy(encounterMasterDto.getUpdatedBy());
				}
				if(patientRegistrationDto.getPatientId()==null || patientRegistrationDto.getPatientId()==0)
				{
					patientRegistrationDto.setRegistrationTypeId(1);
					patientRegistrationDto.setDob(patientRegistrationDto.getDob()+" 00:00:00");
					patientRegistrationDto.setCreatedBy(encounterMasterDto.getCreatedBy());
					patientRegistrationDto.setUpdatedBy(encounterMasterDto.getUpdatedBy());
					Response patRes = iPatientRegistrationService.registerPatient(patientRegistrationDto);
					if(patRes.getStatus().equals(SUCCESS))
					{
						encounterMasterDto.setPatientRegistrationDto((PatientRegistrationDto)patRes.getResObject());
						encounterMasterDto.setPatientId((Integer)patRes.getObject());
						encounterMasterDto.setPatientCategoryId(encounterMasterDto.getPatientRegistrationDto().getPatientCategoryId());
						if(encounterMasterDto.getPatientRegistrationDto()!=null) {
							//send sms and email
							if(encounterMasterDto.getPatientRegistrationDto().getMobileNo()!=null) {
								SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
								smsInfo.setUnitId(encounterMasterDto.getUnitId());
								smsInfo.setPatientId(encounterMasterDto.getPatientId());
							smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.REGISTRATION,smsInfo);
							}
						
						if( encounterMasterDto.getPatientRegistrationDto().getEmail()!=null && encounterMasterDto.getPatientRegistrationDto().getEmail().length()>0) 
							{
								SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
								emailinfo.setUnitId(encounterMasterDto.getUnitId());
								emailinfo.setOrganizationId(encounterMasterDto.getOrganizationId());
								emailinfo.setPatientEmail(encounterMasterDto.getPatientRegistrationDto().getEmail());
								emailinfo.setPatientId(encounterMasterDto.getPatientId());
								smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.REGISTRATION, emailinfo);
							}
						}
						
						
						
						if(patRes.getListObject()!=null) {
							orderId = (Integer)patRes.getListObject().get(0);
						}
						if(encounterMasterDto.getAppointmentObject()!=null)
							encounterMasterDto.getAppointmentObject().setPatientId((Integer)patRes.getObject());
					}else if(patRes.getCode().equals(ALREADY_EXIST_CODE)){
						return patRes;
					}
					
				}else {
					if(encounterMasterDto.getAppointmentObject()!=null) {
						encounterMasterDto.getAppointmentObject().setPatientId(patientRegistrationDto.getPatientId());
						encounterMasterDto.setPatientId(patientRegistrationDto.getPatientId());
					}
						
				}
				Response res = iEncounterMasterService.generateEncounter(encounterMasterDto);
					encounterMasterDto = (EncounterMasterDto)res.getObject();
				if(res.getStatus().equals(SUCCESS))	
				{
					//save Custom Patient Details if any
					if(encounterMasterDto.getPatientRegistrationDto().getCustomDataPost()!=null || encounterMasterDto.getPatientRegistrationDto().getCustomDataDisplay()!=null) {
						CustomPatientDetailsDto customPatientDetailsObj = new CustomPatientDetailsDto();
						customPatientDetailsObj.setPatientId(encounterMasterDto.getPatientId());
						customPatientDetailsObj.setAppointmentId(null);
						customPatientDetailsObj.setEncounterId(encounterMasterDto.getEncounterId());//in case of hospital visit required
						customPatientDetailsObj.setOrganizationId(encounterMasterDto.getOrganizationId());
						customPatientDetailsObj.setUnitId(encounterMasterDto.getUnitId());
						customPatientDetailsObj.setStatus('A');
						customPatientDetailsObj.setCreatedBy(encounterMasterDto.getCreatedBy());
						customPatientDetailsObj.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
						customPatientDetailsObj.setUpdatedBy(encounterMasterDto.getUpdatedBy());
						customPatientDetailsObj.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
						customPatientDetailsObj.setCustomDataPost(encounterMasterDto.getPatientRegistrationDto().getCustomDataPost()!=null 
								? encounterMasterDto.getPatientRegistrationDto().getCustomDataPost():null);
						customPatientDetailsObj.setCustomDataDisplay(encounterMasterDto.getPatientRegistrationDto().getCustomDataDisplay()!=null
								? encounterMasterDto.getPatientRegistrationDto().getCustomDataDisplay():null);
						Response customePatientRegistration = patientRegistrationServiceImpl.saveCustomPatientDetails(customPatientDetailsObj);
					}
					//send appointment sms and emial
					if(encounterMasterDto != null  && res.getStatus().equals(SUCCESS))
					{
						String appointmentDate;
						String appointmentTime;
						
						appointmentDate = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()), "dd/MM/yyyy") ;
						appointmentTime = encounterMasterDto.getAppoitmentTime() != null
								? encounterMasterDto.getAppoitmentTime()
								: (encounterMasterDto.getAppointmentObject() != null
										? encounterMasterDto.getAppointmentObject().getFromTimeString()
										: encounterMasterDto.getEncounterTime().toString()); 
							
						SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
							smsInfo.setUnitId(encounterMasterDto.getUnitId());
							smsInfo.setPatientId(encounterMasterDto.getPatientId());
							smsInfo.setConsultingDoctorId(encounterMasterDto.getDoctorId()!= null ? encounterMasterDto.getDoctorId() : null);
							smsInfo.setReferringDoctorId(encounterMasterDto.getReferralId() != null ? encounterMasterDto.getReferralId() : null);
							smsInfo.setDateTime(appointmentDate+" at "+appointmentTime);
							smsInfo.setEncounterTypeId(encounterMasterDto.getEncounterTypeId());
							smsInfo.setOrganizationId(encounterMasterDto.getOrganizationId());
							smsInfo.setEncounterId(encounterMasterDto.getEncounterId());
						smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.ENCOUNTER,smsInfo);
						
							
							smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.ENCOUNTER, smsInfo);
					}
				}
				
				return res;
				
			}
			
			} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	@Transactional
	public Response scheduleAppointmentForWebAppointment(SlotMasterDto slotMasterDto) throws ApplicationException {
		try{
			if(slotMasterDto.getIsFromKioskToken()!=null)
			{
				if(slotMasterDto.getIsBlocked()!=null && slotMasterDto.getSlotId()!=null)
				{
					return iAppointmentSchedulingDao.blockSlot(slotMasterDto.getSlotId(),4);
				}
				return scheduleAppointmentForToken(slotMasterDto);
			}
           return scheduleAppointmentForWebAppointmentt(slotMasterDto);
			
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
		
	}
	

	@Override
	@Transactional
	public Response scheduleAppointmentForWebAppointmentt(SlotMasterDto slotMasterDto)  throws ApplicationException {
		try {
			
			Character isReshedule = slotMasterDto.getReSchedule() != null ? slotMasterDto.getReSchedule() : 'N';
			
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
				unitApplicationConfigurationDto.setOrganizationId(slotMasterDto.getOrganizationId());
				unitApplicationConfigurationDto.setUnitId(slotMasterDto.getUnitId());
			Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
		
			unitApplicationConfigurationDto = (UnitApplicationConfigurationDto)appConfigRes.getObject();
					slotMasterDto.setSenderId(unitApplicationConfigurationDto.getSenderId());
					slotMasterDto.setIsKiosk(unitApplicationConfigurationDto.getIsKioskRequired());
			String message = "Appointment Scheduled...!!";
			
			Response appRes = null;
			if(isReshedule=='N' && slotMasterDto.getIsFromCoversheet()==null && (slotMasterDto.getAppointmentTypeId()!=APPOINTMENTTYPEID6 && slotMasterDto.getAppointmentTypeId()!=APPOINTMENTTYPEID3 && slotMasterDto.getAppointmentTypeId()!=APPOINTMENTTYPEID5 && slotMasterDto.getAppointmentTypeId()!=APPOINTMENTTYPEID7))
			{
				if(this.isAppointmentForSameDateTimeExist(slotMasterDto)){
					return new Response(ERROR, ALREADY_EXIST_CODE, "This patient already has an appointment on this date and time !!", null, null);
				}
				if(this.isAppointmentIsForSameDoctorWithoutCompletionOfPreviousAppintment(slotMasterDto)){
					return new Response(ERROR, ALREADY_EXIST_CODE, "Previous appointment of same doctor or specialty is not yet closed !!", null, null);
				}
			}
			if(slotMasterDto.getAppointmentTypeId().intValue()==APPOINTMENTTYPEID3) 
			{
				if(iAppointmentSchedulingDao.getEHCAppointmentsByPatienId(slotMasterDto))
					return new Response(ERROR, ALREADY_EXIST_CODE, "This patient already has an appointment on this date and time !!", null, null);
			}
			if (slotMasterDto.getIsFromEncounter() != null) 
			{
				if (slotMasterDto.getIsFromEncounter().equals('Y')) 
				{
					slotMasterDto.setAppointmentStatusId(APPOINTMENTSTATUSID8); // in-process
				}else{
					slotMasterDto.setAppointmentStatusId(APPOINTMENTSTATUSID1);
				}
			}else
			{
				slotMasterDto.setAppointmentStatusId(APPOINTMENTSTATUSID1);
			}
		
			if(slotMasterDto.getSlotStatusId()==null)
			    slotMasterDto.setSlotStatusId(4);
				
			boolean res = false;
			if(slotMasterDto.getAppointmentTypeId()!=APPOINTMENTTYPEID5 && slotMasterDto.getSlotId() != null && slotMasterDto.getSlotId() > 0)
			{
				res = iAppointmentSchedulingDao.isAppointmentValid(slotMasterDto);
				if(res == false)
				{
					if(slotMasterDto.getAppointmentTypeId()==APPOINTMENTTYPEID3)
					{
						int newslot = iAppointmentSchedulingDao.getEHCFreeSlot(slotMasterDto);
						if(newslot>0)
						{
							slotMasterDto.setSlotId(newslot);
						}else{
							return new Response(ERROR, ALREADY_EXIST_CODE, "No slots available for EHC...!!", null, null);
						}
					}else{
						return new Response(ERROR, ALREADY_EXIST_CODE, "Slot is already booked, please select another slot...!!", null, null);
					}
				}
			}
			//	Response blockAppointment = iAppointmentSchedulingDao.blockAppointment(slotMasterDto);
			//If patient is not registered then there will be a pre-registration of a patient who's patientTypeId == 5
			if(slotMasterDto.getPatientId()==0)
			{
				PatientRegistrationDto patientRegistrationDto = new PatientRegistrationDto();
					patientRegistrationDto.setOrganizationId(slotMasterDto.getOrganizationId());
					patientRegistrationDto.setUnitId(slotMasterDto.getUnitId());
					patientRegistrationDto.setPrefixId(slotMasterDto.getPrefixId());
					patientRegistrationDto.setFirstName(slotMasterDto.getFirstName());
					patientRegistrationDto.setMiddleName(slotMasterDto.getMiddleName());
					patientRegistrationDto.setLastName(slotMasterDto.getLastName());
					patientRegistrationDto.setDob(slotMasterDto.getDob());
					patientRegistrationDto.setGenderId(slotMasterDto.getGenderId());
					patientRegistrationDto.setMobileNumber(slotMasterDto.getMobileNumber());
					patientRegistrationDto.setEmail(slotMasterDto.getEmail()!=null ? slotMasterDto.getEmail() : null);
					patientRegistrationDto.setRegistrationTypeId(slotMasterDto.getRegistrationTypeId());
					patientRegistrationDto.setCreatedBy(slotMasterDto.getCreatedBy());
					patientRegistrationDto.setCreatedDate(slotMasterDto.getCreatedDate());
					patientRegistrationDto.setUpdatedBy(slotMasterDto.getUpdatedBy());
					patientRegistrationDto.setUpdatedDate(slotMasterDto.getCreatedDate());
					patientRegistrationDto.setPatientCategoryId(unitApplicationConfigurationDto.getDefaultPatientCategoryId());
					patientRegistrationDto.setStatus('A');
					patientRegistrationDto.setIdentificationNumber(slotMasterDto.getIdentificationNumber());
					patientRegistrationDto.setIdentificationTypeId(slotMasterDto.getIdentificationTypeId());
					patientRegistrationDto.setCountryCallingCode(slotMasterDto.getCountryCallingCode());
				Response patientRegistraion = patientRegistrationServiceImpl.savePatientRegistration(patientRegistrationDto);
				if(patientRegistraion.getStatus().equals(SUCCESS))
					slotMasterDto.setPatientId((Integer)patientRegistraion.getObject());
				else
					slotMasterDto.setPatientId((Integer)patientRegistraion.getObject());
//					return new Response(ERROR, null, "Patient Already Exist !!", null, null);
				
				String patientResMessage = patientRegistraion.getMessage();
				String[] splitStr = patientResMessage.trim().split("\\s+");
				
				message="Appointment scheduled for pre registered patient "+splitStr[0]+"..!!";
			}
			if(slotMasterDto.getGenerateToken()!=null && slotMasterDto.getIsKiosk()=='Y' && (slotMasterDto.getAppointmentTypeId()==APPOINTMENTTYPEID1 || slotMasterDto.getAppointmentTypeId() == APPOINTMENTTYPEID7 || slotMasterDto.getAppointmentTypeId() == APPOINTMENTTYPEID9))
			{
				if(slotMasterDto.getGenerateToken()=='Y' )
				{
						slotMasterDto.setTokenNumber(slotMasterDto.getTokenNumber() != null ? slotMasterDto.getTokenNumber(): iAppointmentSchedulingDao.getTokenNumber(
												slotMasterDto.getAppointmentDate().substring(0, 10),
												slotMasterDto.getUnitId()));
					slotMasterDto.setAppointmentDate(slotMasterDto.getAppointmentDate());
					slotMasterDto.setArrivalTime(slotMasterDto.getCreatedDate());
				}
			}
			AppointmentMaster appointmentMaster = null;
			appRes = iAppointmentSchedulingDao.scheduleAppointment(slotMasterDto);
			appointmentMaster = (AppointmentMaster)appRes.getObject();
			
			//to remove it from clinic if patient is not sync to clinic
			if(slotMasterDto.getAppointmentTypeId()==APPOINTMENTTYPEID1 && iAppointmentSchedulingDao.isPatientSync(slotMasterDto.getPatientId())==false && slotMasterDto.getSlotId()!=null)
			{
				List<ScheduleMasterDto> scheduleMasterDtosList = new ArrayList();
				
				SlotMaster master = (SlotMaster) sessionFactory.getCurrentSession().get(SlotMaster.class, slotMasterDto.getSlotId());
				
				ScheduleMasterDto dto = new ScheduleMasterDto();	
					dto.setDoctorId(master.getDoctorId());				    	
					dto.setSpecialityId(master.getSpecialityId());				    	
					dto.setOrganisationId(master.getOrganizationId());				 
					dto.setUnitId(master.getUnitId());
					dto.setDeleteAllSlots('N');
					dto.setScheduleDate(GlobalCommonDateUtils.getStringDate(master.getAppointmentDate(),"dd-MM-yyyy"));
					dto.setFromTime(master.getFromTime().toString());
					dto.setToTime(master.getToTime().toString());
					dto.setAddRemove(1);
				scheduleMasterDtosList.add(dto);
	
				iDoctorScheduleDao.saveDoctorSchedule(scheduleMasterDtosList,"DELETE");
			}
			
			if(appRes.getStatus().equals(SUCCESS) && slotMasterDto.getDoctorId()!=null){
				SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
					smsInfo.setOrganizationId(slotMasterDto.getOrganizationId());
					smsInfo.setUnitId(slotMasterDto.getUnitId());
				smsInfo.setDateTime(slotMasterDto.getAppointmentDate2() != null
						? (slotMasterDto.getAppointmentDate2().substring(0, 10) + " at "
								+ slotMasterDto.getFromTimeString())
						: (slotMasterDto.getAppointmentDate().substring(0, 10) + " at "
								+ slotMasterDto.getFromTimeString()));
					smsInfo.setPatientId(slotMasterDto.getPatientId());
					smsInfo.setConsultingDoctorId(slotMasterDto.getDoctorId());
					smsInfo.setAppointmentId(appointmentMaster.getAppointmentId());
					smsInfo.setAppointmentUniqueId(appointmentMaster.getAppointmentUniqueId() != null ? appointmentMaster.getAppointmentUniqueId() : null);
				smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.APPOITMENT,smsInfo);
				smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.APPOITMENT,smsInfo);
				
			}
			

			
			return new Response<>(SUCCESS, SUCCESS_CODE, message, null, appointmentMaster, slotMasterDto);
		
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response prnConfiguration(SlotMasterDto slotMasterDto) {
		
		return iAppointmentSchedulingDao.prnConfiguration(slotMasterDto);
	}


	@Override
	@Transactional
	public Response getTokenNo(Integer appointmentId) throws ApplicationException {
		return iAppointmentSchedulingDao.getTokenNo(appointmentId);
	}


	@Override
	@Transactional
	public Response searchInProcessAppointmentWeb(Integer appointmentId) throws ApplicationException {
		try{
			return iAppointmentSchedulingDao.searchInProcessAppointmentWeb(appointmentId);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response searchFutureAppointmentsByPatientId(SlotMasterDto slotMasterDto) throws ApplicationException {
		try{
			return iAppointmentSchedulingDao.searchFutureAppointmentsByPatientId(slotMasterDto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response saveAIAppointmentInQueue(AIAppointmentQueueDto dto) {
		try{
			return iAiAppointmentQueueDao.saveAIAppointmentInQueue(dto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getAIAppointmentCount(AIAppointmentQueueDto dto) {
		try{
			return iAiAppointmentQueueDao.getAIAppointmentCount(dto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getAISummaryData(AIAppointmentQueueDto dto) {
		try{
			return iAiAppointmentQueueDao.getAISummaryData(dto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


}
