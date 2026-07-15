package com.param.global.service.sync;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.param.global.dto.*;
import com.param.global.model.CountryMaster;
import com.param.global.model.StateMaster;
import com.param.global.service.ICountryMasterService;
import com.param.global.service.IStateMasterService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;
import com.param.global.common.IAppointmentAndSlotStatusMasterConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGenderMasterDao;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IPatientRegistrationDao;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.sync.PatientSyncDto;
import com.param.global.dto.sync.ScheduleAppointmentReqDto;
import com.param.global.dto.sync.SlotIdReqDto;
import com.param.global.opd.dao.IAppointmentSchedulingDao;
import com.param.global.opd.dao.ISlotMasterGlobalDao;
import com.param.global.org.dto.UnitMasterDto;
import com.param.global.service.IDoctorConsultationServiceMapperService;
import com.param.global.service.IPatientRegistrationService;
import com.param.opd.appointment.model.AppointmentMaster;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
@Service
@SuppressWarnings({"rawtypes"})
public class AppointmentServiceImpl implements IAppointmentService, ICommonConstants{
	final static Logger logger = Logger.getLogger(AppointmentServiceImpl.class);


	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private IPatientRegistrationDao iPatientRegistrationDao;
	@Autowired
	private ISlotMasterGlobalDao iSlotDao;
	@Autowired
	private IAppointmentSchedulingDao iAppointmentSchedulingDao;
	@Autowired
	IScheduleLogsDao iScheduleLogsDao;
	@Autowired
	private IPatientRegistrationDao iPatientMasterDao;
	@Autowired
	private Gson gson;
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	@Autowired
	private IPatientRegistrationService iPatientRegistrationService;
	@Autowired
	private IGenderMasterDao iGenderMasterDao;
	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	@Autowired
	private IDoctorConsultationServiceMapperService iDoctorConsultationServiceMapperService;
	
	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private ICountryMasterService iCountryService;

	@Autowired
	private IStateMasterService iStateMaster;
	
	@Override
	@Transactional
    public Response confirmAppointment(ScheduleAppointmentReqDto reqDto) {
		try{

				Integer patientId=null;
				Integer slotId=null;
				Integer genderId = 1;
				Integer prefixId = 1;
				Character isNewPatient='N';
				Response patientCategoryRes=null;
				
	            Response res=iScheduleLogsDao.getConnectionStringForMyLife();
	            MyLifeConnectionLinkDto connectionLinkDto=(MyLifeConnectionLinkDto)res.getObject();
	            String MY_LIFE_URI =connectionLinkDto.getMyLifeLink(); 
			    UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
					unitApplicationConfigurationDto.setOrganizationId(reqDto.getHospitalId());
					unitApplicationConfigurationDto.setUnitId(reqDto.getUnitId());
				Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
					unitApplicationConfigurationDto=(UnitApplicationConfigurationDto)appConfigRes.getObject();
				
				//check for patient id
				if(reqDto.getGlobalUhid() != null)
				{
					//get patient id by UUID
					patientId= iPatientMasterDao.getPatientIdByUuidAndHospitalId(reqDto);
				}
				
				
				if(reqDto.getGender().toLowerCase().equals("male"))
				{
					genderId = 1;
				}else if(reqDto.getGender().toLowerCase().equals("female"))
				{
					genderId = 2;
				}
				
				String prefixDesc=null;
				prefixDesc = reqDto.getPrefixDesc();
				
				prefixId=iGenderMasterDao.getPrefixFromGenderId(genderId,reqDto.getHospitalId(),prefixDesc);
				
				if(patientId != null && patientId != 0){
					if(reqDto.getMobileNumber()!=null)
					{
					Session session=sessionFactory.getCurrentSession();
					session.createSQLQuery(" update patient.t_patient_registration set mobile_number ='"+reqDto.getMobileNumber()+"',email ='"+reqDto.getPatMailId()+"' where patient_id = "+patientId)
					.executeUpdate();
					}
					reqDto.setPatientId(patientId);
				}else{
					PatientRegistrationDto patient = new PatientRegistrationDto();
					
					UnitMasterDto unitMasterDto = iGlobalUnitMasterDao.getUnitsAddress(reqDto.getUnitId());
					
					Response contryRes=	iPatientMasterDao.getcontryIdFromCountryName(reqDto);
					reqDto=(ScheduleAppointmentReqDto)contryRes.getObject();

					if(reqDto.getCountryId() == null && reqDto.getCountry() != null){
						CountryMasterDto countryMasterDto = new CountryMasterDto();
						java.util.Date createdUtilDate = TimeZoneComponent.getDateByzone(reqDto.getUnitId());
						SimpleDateFormat sdf = new SimpleDateFormat("DD-MM-YYYY HH:mm:ss");
						String formattedDate = sdf.format(createdUtilDate);

						countryMasterDto.setCountryCode(reqDto.getCountry().substring(0,2).toUpperCase());
						countryMasterDto.setCountryInitial(reqDto.getCountry().substring(0,3));
						countryMasterDto.setCountryName(reqDto.getCountry());
						countryMasterDto.setContactDigit("10");
						countryMasterDto.setCountryCallingCode(reqDto.getCode());
						countryMasterDto.setCreatedBy(1);
						countryMasterDto.setOrganizationId(reqDto.getHospitalId());
						countryMasterDto.setCreatedDate(formattedDate);
						countryMasterDto.setUpdatedBy(1);
						countryMasterDto.setUpdatedDate(formattedDate);
						countryMasterDto.setStatus('A');

						Response countryMasterResponse = iCountryService.addCountryMaster(countryMasterDto);
						Response countryMasterRes = (Response) countryMasterResponse.getObject();
						CountryMaster countryMaster=null;
						if(countryMasterRes!=null) {
							 countryMaster = (CountryMaster) countryMasterRes.getObject();
						}
						List<CountryMasterDto> nationalityMasterDtoList =countryMasterResponse.getListObject();
						if(countryMaster == null)
						{
							reqDto.setCountryId(nationalityMasterDtoList.get(0).getCountryId());
						}
						if(countryMaster != null && countryMaster.getCountryId() > 0){
							reqDto.setCountryId(countryMaster.getCountryId());
						}
					}

					if(reqDto.getStateId() == null && reqDto.getState() != null){
						StateMasterDto stateMasterDto = new StateMasterDto();
						java.util.Date createdUtilDate = TimeZoneComponent.getDateByzone(reqDto.getUnitId());
						SimpleDateFormat sdf = new SimpleDateFormat("DD-MM-YYYY HH:mm:ss");
						String formattedDate = sdf.format(createdUtilDate);

						stateMasterDto.setStateCode(reqDto.getState().substring(0,2).toUpperCase());
						stateMasterDto.setCountryId(reqDto.getCountryId());
						stateMasterDto.setStateName(reqDto.getState());
						stateMasterDto.setCountryName(reqDto.getCountry());
						stateMasterDto.setCreatedBy(1);
						stateMasterDto.setOrganizationId(reqDto.getHospitalId());
						stateMasterDto.setCreatedDate(formattedDate);
						stateMasterDto.setUpdatedBy(1);
						stateMasterDto.setUpdatedDate(formattedDate);
						stateMasterDto.setStatus('A');

						Response stateMasterResponse = iStateMaster.addStateMaster(stateMasterDto);
						Response stateMasterRes = (Response) stateMasterResponse.getObject();
						StateMaster stateMaster = (StateMaster)stateMasterRes.getObject();

						if(stateMaster != null && stateMaster.getStateId() > 0){
							reqDto.setStateId(stateMaster.getStateId());
						}
					}

					if(reqDto.getNationalityId() == null && reqDto.getNationality() != null){

						Response response = iAppointmentSchedulingDao.addNationalityMaster(reqDto);

						if(response.getStatus().equals(SUCCESS)) {
							Integer nationalityId = iAppointmentSchedulingDao.getNationalityIdByNationalityName(reqDto);

							if(nationalityId != null && nationalityId > 0) {
								reqDto.setNationalityId(nationalityId);
							}
						}
					}

					patient.setCountryId(reqDto.getCountryId());
					patient.setStateId(reqDto.getStateId());
					patient.setCityId(reqDto.getCityId());
					patient.setNationalityId(reqDto.getNationalityId());

					if(reqDto.getCityId() == null && reqDto.getCity() != null){
						reqDto.setPatAddressDesc(reqDto.getPatAddressDesc() + ", " +reqDto.getCity());
					}

//					if(unitMasterDto!=null)
//					{
//						if(unitMasterDto.getUnitCountryId()!=null)
//						{
//							patient.setCountryId(unitMasterDto.getUnitCountryId());
//							patient.setPermanentCountryId(unitMasterDto.getUnitCountryId());
//						}
//						if(unitMasterDto.getUnitStateId()!=null)
//						{
//							patient.setStateId(unitMasterDto.getUnitStateId());
//							patient.setPermanentStateId(unitMasterDto.getUnitStateId());
//						}
//						if(unitMasterDto.getUnitDistrictId()!=null)
//						{
//							patient.setDistrictId(unitMasterDto.getUnitDistrictId());
//							patient.setPermanentDistrictId(unitMasterDto.getUnitDistrictId());
//						}
//						if(unitMasterDto.getUnitCityId()!=null)
//						{
//							patient.setCityId(unitMasterDto.getUnitCityId());
//							patient.setPermanentCityId(unitMasterDto.getUnitCityId());
//						}
//						if(unitMasterDto.getUnitAreaId()!=null)
//						{
//							patient.setAreaId(unitMasterDto.getUnitAreaId());
//							patient.setPermanentAreaId(unitMasterDto.getUnitAreaId());
//						}
//						if(unitMasterDto.getPostCode()!=null)
//						{
//							patient.setZipCode(unitMasterDto.getPostCode());
//							patient.setPermanentZipCode(unitMasterDto.getPostCode());
//						}
//						if(unitMasterDto.getCountryCallingCode()!=null)
//						{
//							patient.setCountryCallingCode(unitMasterDto.getCountryCallingCode());
//							patient.setWhatsappCallingCode(unitMasterDto.getCountryCallingCode());
//						}
//						
//					}
					patient.setIsPreReg('N');
					patient.setIsPrimary('Y');
					patient.setIsOtcReg('N');
					patient.setRegistrationTypeId(1);
					patient.setIsUnknownReg('N');
					patient.setFirstName(reqDto.getFirstName());
					patient.setMiddleName(reqDto.getMiddleName() != null ? reqDto.getMiddleName() : "");
					patient.setLastName(reqDto.getLastName() != null ? reqDto.getLastName() : "");
					patient.setDob(reqDto.getDateOfBirth()+" 00:00:00");
					patient.setOrganizationId(reqDto.getHospitalId());
					patient.setUnitId(reqDto.getUnitId());
					patient.setPatientCategoryId(unitApplicationConfigurationDto.getDefaultPatientCategoryId());
					patient.setGenderId(genderId);
					patient.setPrefixId(prefixId);
					patient.setUuid(reqDto.getUuid());
					patient.setZipCode(reqDto.getPinCode());
					patient.setCountryCallingCode(reqDto.getCode());
					patient.setWhatsappCallingCode(reqDto.getWhatsappCallingCode());
					patient.setAddress(reqDto.getPatAddressDesc());
					
					if(reqDto.getMobileNumber()!=null)
					{
						patient.setMobileNumber(reqDto.getMobileNumber());
						patient.setWhatsappNo(reqDto.getTwitterId());
					}
					if(reqDto.getFamilyId()!=null)
					{
						patient.setFamilyId(reqDto.getFamilyId());
					}
					if(reqDto.getPatMailId()!=null)
						patient.setEmail(reqDto.getPatMailId());
					
					if(reqDto.getPatSkypeId()!=null)
						patient.setSkypeId(reqDto.getPatSkypeId());
					
					Response patRes = iPatientRegistrationService.registerPatient(patient);
					if(((patRes.getStatus().equalsIgnoreCase(SUCCESS) && patRes.getObject() != null) || patRes.getCode().equals(ALREADY_EXIST_CODE)))
					{
						isNewPatient='Y';
						patientId = (Integer)patRes.getObject();
						reqDto.setPatientId(patientId);
						
						PatientSyncDto patientSynDto =new PatientSyncDto();
							patientSynDto.setFirstName(reqDto.getFirstName());
							patientSynDto.setMiddleName(reqDto.getMiddleName() != null ? reqDto.getMiddleName() : "");
							patientSynDto.setLastName(reqDto.getLastName() != null ? reqDto.getLastName() : "");
							patientSynDto.setGender(reqDto.getGender());
							patientSynDto.setMobileNumber(reqDto.getMobileNumber());
							patientSynDto.setDateOfBirth(reqDto.getDateOfBirth());
							patientSynDto.setHospitalId(reqDto.getHospitalId());
							patientSynDto.setApplicationId(2);
							patientSynDto.setPatientId(patientId);
							patientSynDto.setLocalUhid(patientId.toString());
							patientSynDto.setGlobalOrgId(reqDto.getGlobalOrgId());
							patientSynDto.setUuid(reqDto.getUuid());
							patientSynDto.setCode("+91");
							patientSynDto.setCountryId(1);
							patientSynDto.setStatus('A');
						URI url = URI.create(MY_LIFE_URI + "/sync/patients/realtime");
						String stringResponse = restTemplate.postForObject(url, patientSynDto, String.class);
						Response commonResponse = gson.fromJson(stringResponse, Response.class);
						if(commonResponse.getObject() != null) {
							//iPatientMasterDao.updateUuidByPatient(patientId, (String)commonResponse.getObject());
						 }
					  }
				}
				reqDto.setPatientId(patientId);
				
				SlotIdReqDto slotIdReq = new SlotIdReqDto();
				slotIdReq.setAppointmentDate(reqDto.getAppointmentDate());
				slotIdReq.setDoctorId(reqDto.getDoctorId());
				slotIdReq.setOrgId(reqDto.getHospitalId());
				slotIdReq.setSpecialtyId(reqDto.getDepartmentId());
				slotIdReq.setTimeSlot(reqDto.getTimeSlot());
				slotIdReq.setUnitId(reqDto.getUnitId());
				//to get slot id and his doctor id by global doctor id doctorId from reqDto is globalDoctorId
				Response slotRes = iSlotDao.getSlotIdForMyLifeAppSync(slotIdReq);
				SlotIdReqDto slotResObj=(SlotIdReqDto)slotRes.getObject();
				
				Date currentDate = TimeZoneComponent.getDateByzone(reqDto.getUnitId());  
			    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");  
			    String strCurrentDate= formatter.format(currentDate);
			    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");  
			    String strCurrentDate2= formatter1.format(currentDate);
			    
				if(slotResObj != null)
				{
					//confirm appointment
					slotId=slotResObj.getSlotId();
					reqDto.setDoctorLocalId(slotResObj.getDoctorId());
					
					SlotMasterDto appointmentDto = new SlotMasterDto();
						appointmentDto.setUnitId(reqDto.getUnitId());
						appointmentDto.setOrganizationId(reqDto.getHospitalId());
						appointmentDto.setUpdatedBy(0);
						appointmentDto.setUpdatedDate(strCurrentDate);
						appointmentDto.setCreatedDate(strCurrentDate);
						appointmentDto.setStatus('A');
					String splitAppDate[]=reqDto.getAppointmentDate().split("-");
						
					appointmentDto.setAppointmentDate(reqDto.getAppointmentDate()+" 00:00:00");
					appointmentDto.setIsFromEncounter('N');
					appointmentDto.setSlotId(slotId);
					if(appointmentDto.getSlotStatusId()==null) 
					{
						appointmentDto.setSlotStatusId(4);
					}
					//appointmentDto.setSlotStatusId(reqDto);
					appointmentDto.setPatientId(reqDto.getPatientId());
					//appointmentDto.setCreatedBy(reqDto.getDoctorLocalId());
					appointmentDto.setAppointmentStatusId(IAppointmentAndSlotStatusMasterConstants.Pending);
					appointmentDto.setAppointmentTypeId(APPOINTMENTTYPEID1);
					appointmentDto.setPriorityId(1);
					appointmentDto.setRemark(reqDto.getPatientAppointmentNote());
					appointmentDto.setIsTeleconsultationAppointment(reqDto.getIsVideo()!=null?reqDto.getIsVideo():0);
					
					boolean isRateConfigured = false;
						DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto = new DoctorConsultationServiceMapperDto();
						doctorConsultationServiceMapperDto.setOrganizationId(reqDto.getHospitalId());
						doctorConsultationServiceMapperDto.setUnitId(reqDto.getUnitId());
						doctorConsultationServiceMapperDto.setDoctorId(slotResObj.getDoctorId());
						doctorConsultationServiceMapperDto.setSpecialityId(reqDto.getDepartmentId());
						doctorConsultationServiceMapperDto.setDeptWiseAppointment('N');
						doctorConsultationServiceMapperDto.setPaymentEntitlementId(SELF);
					Response response = iDoctorConsultationServiceMapperService.getDoctorConsultationServiceBySpecialityAndDoctorId(doctorConsultationServiceMapperDto);
					if (response.getStatus().equals(SUCCESS) && response.getListObject().size() > 0) {
						appointmentDto.setIsPaidAppointment('Y');
						isRateConfigured = true;
					}else{
						appointmentDto.setIsPaidAppointment('N');
						isRateConfigured = false;
					}	
						
					Response appRes = iAppointmentSchedulingDao.scheduleAppointmentFromMyLife(appointmentDto);
					AppointmentMaster app = (AppointmentMaster)appRes.getObject();
					
					int appointmentId = app.getAppointmentId();
					
					if(isNewPatient=='N') {
						patientCategoryRes=iPatientRegistrationDao.getPatientCategoryIdByPatientId(reqDto.getPatientId());
					}
					
					if(reqDto.getAppointmentId()!=null)
					{
						return iAppointmentSchedulingDao.rescheduleAppointmentForMylife(reqDto.getAppointmentId(), app.getAppointmentId());
					}
					
					if(appRes.getCode().equals(SUCCESS_CODE) && appRes.getObject() != null && isRateConfigured)
					{
						//this setappointment of format YYYY-MM-DD is required for encounter date
						appointmentDto.setAppointmentDate(splitAppDate[2]+"-"+splitAppDate[1]+"-"+splitAppDate[0]);
						//create encounter object for encounter and bill settlement
						EncounterMasterDto encounterMasterDto=new EncounterMasterDto();
							encounterMasterDto.setAppointmentId(app.getAppointmentId());
							encounterMasterDto.setPatientId(reqDto.getPatientId());
							encounterMasterDto.setIsAppointmentFromMyLife('Y');
							encounterMasterDto.setOrganizationId(reqDto.getHospitalId());
							encounterMasterDto.setUnitId(reqDto.getUnitId());
							encounterMasterDto.setDoctorId(reqDto.getDoctorLocalId());
							encounterMasterDto.setDeptId(reqDto.getDepartmentId());
							encounterMasterDto.setEncounterDate(currentDate.getTime());
							encounterMasterDto.setEncounterTime(reqDto.getTimeSlot()); 
							encounterMasterDto.setPaymentEntitlementTypeId(1);
							encounterMasterDto.setCreatedBy(0);
							encounterMasterDto.setUpdatedBy(0);
							encounterMasterDto.setCreatedDate(currentDate.getTime());
							encounterMasterDto.setUpdatedDate(currentDate.getTime());
							encounterMasterDto.setUserDefinedVisitTypeId((reqDto.getVisitTypeId()==OP ||reqDto.getVisitTypeId()==ER) ? 1 : (reqDto.getVisitTypeId()==IP ||reqDto.getVisitTypeId()==5) ? IP : OP);
							encounterMasterDto.setStatus('A');
							if(isNewPatient=='N') 
							{
								encounterMasterDto.setPatientCategoryId(patientCategoryRes.getObject()!=null ? (Integer)patientCategoryRes.getObject() : unitApplicationConfigurationDto.getDefaultPatientCategoryId());
							}else {
								encounterMasterDto.setPatientCategoryId(unitApplicationConfigurationDto.getDefaultPatientCategoryId());
							}
							encounterMasterDto.setDefaultSelfTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
							encounterMasterDto.setIsScheduledAppointment('A');
							encounterMasterDto.setSpecialityId(reqDto.getDepartmentId());
							encounterMasterDto.setEncounterTypeId(1);
							encounterMasterDto.setPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
							encounterMasterDto.setBillingBedCategoryId(1);
							encounterMasterDto.setIsPreOrPostBilling('Y');
							encounterMasterDto.setDeptWiseAppointment('N');
							encounterMasterDto.setIsTransferToActiveDeposit('N');
							encounterMasterDto.setIsReshedule('N');
							encounterMasterDto.setTotalBillAmt(reqDto.getPaidAmount());
							encounterMasterDto.setPaidAmount(reqDto.getPaidAmount());
							encounterMasterDto.setCurrentDate(strCurrentDate2);
							//encounterMasterDto.setIsReshedule('N');
						List<PaymentReceiptDetailsDto> paymentReceiptDetailsDtosList=new ArrayList<PaymentReceiptDetailsDto>();
						PaymentReceiptDetailsDto paymentReceiptDetailsDto=new PaymentReceiptDetailsDto();
							paymentReceiptDetailsDto.setPaymentModeId(8);
							paymentReceiptDetailsDto.setAmount(reqDto.getPaidAmount());
							paymentReceiptDetailsDto.setTransactionNo(reqDto.getTransactionId());
							paymentReceiptDetailsDto.setTransTransactionDate(strCurrentDate);
							paymentReceiptDetailsDto.setCreatedBy(0);
							paymentReceiptDetailsDtosList.add(paymentReceiptDetailsDto);
						encounterMasterDto.setPaymentReceiptDetailsDtosList(paymentReceiptDetailsDtosList);
						
						List<PatientSponsorDetailsDto> patientSponsorDetailsDtosList=new ArrayList<PatientSponsorDetailsDto>();
						PatientSponsorDetailsDto patientSponsorDetailsDto=new PatientSponsorDetailsDto();
							patientSponsorDetailsDto.setOrganizationId(reqDto.getHospitalId());
							patientSponsorDetailsDto.setUnitId(reqDto.getUnitId());
							patientSponsorDetailsDto.setVisitTypeId(OP);
							patientSponsorDetailsDto.setPaymentEntitlementId(SELF);
							patientSponsorDetailsDto.setPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());
							patientSponsorDetailsDto.setPriorityId(1);
							patientSponsorDetailsDto.setTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
							patientSponsorDetailsDto.setCreatedBy(0);
							patientSponsorDetailsDto.setUpdatedBy(0);
							patientSponsorDetailsDto.setCreatedDate(strCurrentDate);
							patientSponsorDetailsDto.setUpdatedDate(strCurrentDate);
							patientSponsorDetailsDto.setStatus('A');
						patientSponsorDetailsDtosList.add(patientSponsorDetailsDto);
						encounterMasterDto.setPatientSponsorDetailsDtosList(patientSponsorDetailsDtosList);
						encounterMasterDto.setAppointmentObject(appointmentDto);
						encounterMasterDto.setListKinDetailsDto(new ArrayList<KinDetailsDto>());
						
						return new Response<>(SUCCESS, SUCCESS_CODE, "Appointment Confirmed", null, encounterMasterDto);
					}else{
						if(isRateConfigured)
							return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
						else
							return new Response<>(SUCCESS, SUCCESS_CODE, "Appointment Confirmed", null, null,appointmentId);
					}
				}else{
					return new Response<>(ERROR, ERROR_CODE, "Slot not found", null, null);
			}
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response cancelorRescheduleAppointment(ScheduleAppointmentReqDto reqDto) throws ApplicationException {
		try{
			
			    Date date = TimeZoneComponent.getDateByzone(reqDto.getUnitId());  
			    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");  
			    String strDate= formatter.format(date);
					SlotMasterDto appointment = new SlotMasterDto();
					appointment.setUnitId(reqDto.getUnitId());
					appointment.setOrganizationId(reqDto.getHospitalId());
					//appointment.setUpdatedBy(reqDto.getDoctorLocalId());
					appointment.setUpdatedDate(strDate);
					appointment.setCreatedDate(strDate);
					appointment.setAppointmentDate(strDate);
					if(reqDto.getAppointmentStatusId()==APPOINTMENTSTATUSID4) {
						appointment.setAppointmentStatusId(APPOINTMENTSTATUSID3);
					}
					if(reqDto.getAppointmentStatusId()==APPOINTMENTSTATUSID7) {
						appointment.setAppointmentStatusId(APPOINTMENTSTATUSID2);
					}
					appointment.setAppointmentId(reqDto.getAppointmentId());
					Response appRes = iAppointmentSchedulingDao.rescheduleAppointmentFromMyMylife(appointment);
					if(appRes.getStatus().equals(SUCCESS)){
						return new Response<>(SUCCESS, SUCCESS_CODE, "Appointment Rescheduled", null,null);
					}else{
						return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
					}
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getAppointmentUniqueId(SlotMasterDto slotMasterDtoss) throws ApplicationException {
		try {
			return iAppointmentSchedulingDao.getAppointmentUniqueId(slotMasterDtoss);
		} catch (Exception e) {
			
			throw e;
		}
	}


}
