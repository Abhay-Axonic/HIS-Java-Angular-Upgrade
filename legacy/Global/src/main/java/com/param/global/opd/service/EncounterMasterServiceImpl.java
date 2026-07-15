package com.param.global.opd.service;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.param.global.common.*;
import com.param.global.dto.sync.PatientMasterDto;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.opd.encounter.dto.*;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.dozer.DozerBeanMapper;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.engine.hl7.Client;
import com.engine.hl7.v22.Header;
import com.engine.hl7.v22.OBR;
import com.engine.hl7.v22.ORC;
import com.engine.hl7.v22.PID;
import com.engine.hl7.v22.PV1;
import com.engine.hl7.v22.RadOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.param.billing.packages.model.TPackageConsumptionMaster;
import com.param.entity.model.master.Tax;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.service.IContractConsumptionService;
import com.param.global.common.AppointmentStatusMasterConstant;
import com.param.global.common.DocUploadUtility;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IAppointmentAndSlotStatusMasterConstants;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IKinDetailsDao;
import com.param.global.dao.IMPackageMasterDao;
import com.param.global.dao.IOPConsultationConfigurationMasterDao;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dao.IPatientRegistrationDao;
import com.param.global.dao.IRoomModalityMasterDao;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dao.ITPackageConsumptionMasterDao;
import com.param.global.dao.ITPackageConsumptionServiceDetailDao;
import com.param.global.dao.ITPackageServicesDetailsDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dao.IUnitHolidayCalenderDao;
import com.param.global.dao.IWeekendMasterDao;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.DoctorConsultationServiceMapperDto;
import com.param.global.dto.HolidayCalenderMasterDto;
import com.param.global.dto.KinDetailsDto;
import com.param.global.dto.MPackageMasterDto;
import com.param.global.dto.MyLifeConnectionLinkDto;
import com.param.global.dto.OPConsultationConfigurationMasterDto;
import com.param.global.dto.OrderDetailsByEncounterIdDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.PACSDto;
import com.param.global.dto.PackageBBCategoryMpprDto;
import com.param.global.dto.PatientConsentMapperDto;
import com.param.global.dto.PatientDetailsDto;
import com.param.global.dto.PatientSponsorDetailsDto;
import com.param.global.dto.PhysiotherapyOrdersListDto;
import com.param.global.dto.RadiologyOrdersListDto;
import com.param.global.dto.ReferalDetailsDto;
import com.param.global.dto.ReferralMasterDto;
import com.param.global.dto.RoomModalityMasterDto;
import com.param.global.dto.ServiceForPackageReqDto;
import com.param.global.dto.ServiceMasterDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.TPackageConsumptionMasterDto;
import com.param.global.dto.TPackageConsumptionServiceDetailDto;
import com.param.global.dto.TPackageServicesDetailsDto;
import com.param.global.dto.TemplateTransactionDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.UnitHolidayCalenderMapperDto;
import com.param.global.dto.UnitServiceTariffMasterDto;
import com.param.global.dto.WeekendMasterDto;
import com.param.global.dto.sync.AppointmentSyncSchedulingDto;
import com.param.global.dto.sync.ScheduleAppointmentReqDto;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.opd.dao.IAppointmentMasterDao;
import com.param.global.opd.dao.IAppointmentSchedulingDao;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.service.IDoctorConsultationServiceMapperService;
import com.param.global.service.IMPackageMasterService;
import com.param.global.service.IOrderDetailsMasterService;
import com.param.global.service.IOrderMasterService;
import com.param.global.service.IPatientRegistrationService;
import com.param.global.service.IPatientSponsorDetailsService;
import com.param.global.service.IReferalDetailsService;
import com.param.global.service.IReferralMasterService;
import com.param.global.service.IServiceMasterService;
import com.param.global.service.ITemplateTransactionService;
import com.param.opd.appointment.dto.AppointmentStatusSyncDto;
import com.param.opd.appointment.dto.HemodialysisAppointmentMapperDto;
import com.param.opd.appointment.model.AppointmentMaster;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
@Service
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public class EncounterMasterServiceImpl
		implements IEncounterMasterService, ICommonConstants, IAppointmentAndSlotStatusMasterConstants {
	final static Logger logger = Logger.getLogger(EncounterMasterServiceImpl.class);


	@Autowired
	IAppointmentSchedulingService iAppointmentSchedulingService;

	@Autowired
	IEncounterMasterDao iEncounterMasterDao;

	@Autowired
	IKinDetailsDao iKinDetailsDao;

	@Autowired
	IAppointmentSchedulingDao iAppointmentSchedulingDao;

	@Autowired
	IOrderMasterService iOrderMasterService;

	@Autowired
	IDoctorConsultationServiceMapperService iDoctorConsultationServiceMapperService;

	@Autowired
	IOPConsultationConfigurationMasterDao iOPConsultationConfigurationMasterService;

	@Autowired
	IUnitHolidayCalenderDao iUnitHolidayCalenderDao;

	@Autowired
	IWeekendMasterDao iWeekendMasterService;

	@Autowired
	SlotsCreationService slotsCreationService;

	@Autowired
	IPatientSponsorDetailsService iPatientSponsorDetailsService;

	@Autowired
	IReferalDetailsService iReferalDetailsService;

	@Autowired
	IScheduleLogsDao iScheduleLogsDao;

	@Autowired
	IServiceMasterService iServiceMasterService;

	@Autowired
	IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;

	@Autowired
	DocUploadUtility docUploadUtility;

	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;

	@Autowired
	IMPackageMasterService iMPackageMasterService;

	@Autowired
	ITPackageConsumptionMasterDao iTPackageConsumptionMasterDao;

	@Autowired
	IContractConsumptionService iContractConsumptionService;

	@Autowired
	private IReferralMasterService iReferralMasterService;

	@Autowired
	IPatientRegistrationService iPatientRegistrationService;

	@Autowired
	IRoomModalityMasterDao iRoomModalityMasterDao;

	@Autowired
	private SessionService sessionService;

	@Autowired
	IOrderDetailsMasterService iOrderDetailsMasterService;

	@Autowired
	IEncounterAssignmenMapperService iEncounterAssignmenMapperService;

	@Autowired
	IGlobalUnitMasterDao iGlobalUnitMasterDao;

	@Autowired
	IOrderDetailsMasterDao iOrderDetailsMasterDao;
	
	@Autowired
	ITPackageServicesDetailsDao iTPackageServicesDetailsDao;
	
	@Autowired
	ITemplateTransactionService iTemplateTransactionService;
	
	@Autowired
	ITPackageConsumptionServiceDetailDao iTPackageConsumptionServiceDetailDao;
	
	@Autowired
	IMPackageMasterDao iMPackageMasterDao;

	@Autowired
	IPatientRegistrationDao iPatientRegistrationDao;
	
	@Autowired
	private DozerBeanMapper mapper;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private IAppointmentMasterDao iAppointmentMasterDao;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	private Gson gson;

	@Autowired
	private SmsUtility smsUtility;

	@Autowired
	private ISmsUtilityConfigService smsUtilityService;
	
	@Override
	@Transactional
	public Response generateEncounter(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return saveEncounterMaster(encounterMasterDto);
	}

	@Override
	public Response saveEncounterMaster(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			Character isNursingAssessmentRequiredForUnit = null;
			Character isPrePostBilling = null;
			Character isFromAppointmentScheduling = encounterMasterDto.getIsFromAppointmentScheduling()!=null ? encounterMasterDto.getIsFromAppointmentScheduling() : 'N';
			Character isKiosk = null;
			Character isPaidAppointment = encounterMasterDto.getIsPaidAppointment() != null	? encounterMasterDto.getIsPaidAppointment()	: 'N';
			Character isKioskRequired = null;
			Character updateRegistrationCharges = (encounterMasterDto.getRegistrationTypeId()!=null && encounterMasterDto.getRegistrationTypeId()==5 ? 'N' : 'Y');
			Integer defaultPatientCategoryId = null;
			List<Integer> serviceList = new LinkedList<>();
			Response orderRes = null;
			UnitApplicationConfigurationDto unitApplicationConfigurationDtos = null;
			Response refRes = null;
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setOrganizationId(encounterMasterDto.getOrganizationId());
			unitApplicationConfigurationDto.setUnitId(encounterMasterDto.getUnitId());
			Response appConfigRes = iUnitApplicationBillingConfigurationDao
					.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
			AppointmentMaster appointmentMaster = null;
			if (appConfigRes.getObject() != null) {
				unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto) appConfigRes
						.getObject();

				if (unitApplicationConfigurationDtos.getDefaultSelfTariffId() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Default tariff is not defined !!", null, null);
				else
					encounterMasterDto
							.setDefaultSelfTariffId(unitApplicationConfigurationDtos.getDefaultSelfTariffId());

				if (unitApplicationConfigurationDtos.getOpdBillingCatId() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Opd Billing Category Id is not defined !!", null,
							null);
				else
					encounterMasterDto.setBillingBedCategoryId(unitApplicationConfigurationDtos.getOpdBillingCatId());

				if (unitApplicationConfigurationDtos.getSelfPayeeId() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Self Payee is not defined !!", null, null);
				else
					encounterMasterDto.setPayeeId(unitApplicationConfigurationDtos.getSelfPayeeId());

				if (unitApplicationConfigurationDtos.getConsultationSpecialityId() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Self Consultation Speciality is not defined !!",
							null, null);
				else
					encounterMasterDto.setConsultationSpecialityId(
							unitApplicationConfigurationDtos.getConsultationSpecialityId());

				if (unitApplicationConfigurationDtos.getIsPreOrPostBilling() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Pre billing post billing is not defined !!", null,
							null);
				else
					isPrePostBilling = unitApplicationConfigurationDtos.getIsPreOrPostBilling();

				if (unitApplicationConfigurationDtos.getIsKioskRequired() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Kiosk required is not defined !!", null, null);
				else
					isKiosk = unitApplicationConfigurationDtos.getIsKioskRequired();

				if (unitApplicationConfigurationDtos.getIsNursingAssessment() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Is Nursing Assessment Required is not defined !!",
							null, null);
				else
					isNursingAssessmentRequiredForUnit = unitApplicationConfigurationDtos
							.getIsNursingAssessment() != null
									? unitApplicationConfigurationDtos.getIsNursingAssessment()
									: 'Y';

				if (unitApplicationConfigurationDtos.getIsKioskRequired() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Is Kiosk Required is not defined !!", null, null);
				else
					isKioskRequired = unitApplicationConfigurationDtos.getIsKioskRequired() != null
							? unitApplicationConfigurationDtos.getIsKioskRequired()
							: 'N';

			} else {
				return new Response<>(ERROR, COMMON_ERROR_CODE, "Application Configuration is yet to set !!", null,
						null);
			}
			
			Response responseRate =null;
			if(encounterMasterDto.getEhcPackageId()!=null &&  encounterMasterDto.getEhcPackageId()>0 &&encounterMasterDto.getEncounterTypeId().intValue()==3 ) 
			  { 
				  ServiceForPackageReqDto reqDto=new ServiceForPackageReqDto();
				  PatientSponsorDetailsDto sponsorDetails = encounterMasterDto.getPatientSponsorDetailsDtosList().stream().filter(x -> x.getPriorityId().intValue()==1).findAny().orElse(null); 
					  reqDto.setPaymentEntitlementId(sponsorDetails!=null ? sponsorDetails.getPaymentEntitlementId() : SELF);
					  reqDto.setVisitTypeId(OP);
					  reqDto.setPatientTypeId(encounterMasterDto.getPatientCategoryId());
					  reqDto.setBillingBedCategoryId(encounterMasterDto.getBillingBedCategoryId());
					  reqDto.setServiceId(encounterMasterDto.getServiceId());
					  reqDto.setUnitId(encounterMasterDto.getUnitId());
					  reqDto.setOrgId(encounterMasterDto.getOrganizationId());
			          responseRate = iMPackageMasterService.getPackageRate(reqDto);
			     
			     if(responseRate.getListObject()==null || responseRate.getListObject().size()==0) {
			    	 return new Response<>(ERROR, COMMON_ERROR_CODE, "Package Configuration is yet to set !!", null,null);
			     }
			  } 
			encounterMasterDto.setIsPreOrPostBilling(isPrePostBilling);
			Character isNursingAssessmentRequiredForSpeciality = encounterMasterDto
					.getIsNursingAssessmentRequiredForSpeciality() != null
							? encounterMasterDto.getIsNursingAssessmentRequiredForSpeciality()
							: 'Y';
			Integer serviceId = null;
			Double serviceAmount = null;
			Integer tokenNumber = null;
			if (encounterMasterDto.getAppointmentId() != null && encounterMasterDto.getAppointmentId() > 0
					&& encounterMasterDto.getSlotId() != null && encounterMasterDto.getEncounterTypeId()!=7 && encounterMasterDto.getEncounterTypeId()!=3) 
			{
				iAppointmentSchedulingDao.updateSlotId(encounterMasterDto.getAppointmentId(),
						encounterMasterDto.getSlotId(), encounterMasterDto.getOldSlotId());
			}

			//update Cyclic appointment
			if(encounterMasterDto.getEncounterTypeId()==7 && encounterMasterDto.getAppointmentId()!=null) {
				Response res=iAppointmentSchedulingDao.updateCyclicAppointment(encounterMasterDto);
				EncounterMasterDto encounterDto2=(EncounterMasterDto)res.getObject();
				encounterMasterDto.setUnitTokenNumber(encounterDto2.getUnitTokenNumber());
				encounterMasterDto.setAppointmentId(encounterMasterDto.getAppointmentId());
			}
			
			if((encounterMasterDto.getIsAppointmentFromMyLife()!=null && encounterMasterDto.getIsAppointmentFromMyLife().equals('Y')) 
				|| (encounterMasterDto.getIsFromWebAppointment()!=null && encounterMasterDto.getIsFromWebAppointment().equals('Y'))	) {
				encounterMasterDto.setAppointmentObject(null);
			}
			SlotMasterDto st=null;
			if (encounterMasterDto.getAppointmentObject() != null )
			{
				Response res = iAppointmentSchedulingService.scheduleAppointment(encounterMasterDto.getAppointmentObject());
				
				if(res.getResObject()!= null) {
				st = (SlotMasterDto) res.getResObject();
				encounterMasterDto.setSyncScheduling(st.isSyncScheduling());
				}
				if (res.getStatus().equals(SUCCESS)) 
				{
					appointmentMaster = (AppointmentMaster) res.getObject();

					SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
					smsInfo.setUnitId(st.getUnitId());
					smsInfo.setOrganizationId(st.getOrganizationId());
					smsInfo.setPatientId(st.getPatientId());
					smsInfo.setReferringDoctorId(encounterMasterDto.getDoctorId());
					smsInfo.setTreatingDoctorId(encounterMasterDto.getDoctorId());
					smsInfo.setAdmitingDoctorId(encounterMasterDto.getDoctorId());
					smsInfo.setConsultingDoctorId(encounterMasterDto.getDoctorId());



					if(encounterMasterDto.getIsReshedule() != null && encounterMasterDto.getIsReshedule() == 'Y'){
						smsInfo.setDateTime(st.getAppointmentDate2()+" "+st.getFromTimeString());
						smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.RESCHEDULE_APPOITMENT,smsInfo);
					}
					else{
						smsInfo.setDateTime((st.getAppointmentDate2() != null && st.getAppointmentDate2() != "")
								? (st.getAppointmentDate2().substring(0, 10) + " at "
								+ st.getFromTimeString())
								: (st.getAppointmentDate().substring(0, 10) + " at "
								+ st.getFromTimeString()));
						smsInfo.setAppointmentId(appointmentMaster.getAppointmentId());
						smsInfo.setAppointmentUniqueId(appointmentMaster.getAppointmentUniqueId() != null ? appointmentMaster.getAppointmentUniqueId() : null);
							smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.APPOITMENT,smsInfo);
					}

					encounterMasterDto.setAppointmentId(appointmentMaster.getAppointmentId());
					encounterMasterDto.setAppoitmentDate(encounterMasterDto.getAppointmentObject().getAppointmentDate());
					encounterMasterDto.setEncounterDate(appointmentMaster.getAppDateTime().getTime());
					encounterMasterDto.setEncounterTime(GlobalCommonDateUtils.getStringDate(appointmentMaster.getAppDateTime(), "HH:mm:ss"));
					
					
					//Ganesh send template
					//Send Template
					TemplateTransactionDto dto = new TemplateTransactionDto();
					dto.setSpecialityId(encounterMasterDto.getSpecialityId()!=null ? encounterMasterDto.getSpecialityId() : encounterMasterDto.getDeptId());
					dto.setTemplateTypeId(10);//1.Questionnarire
					dto.setPatientId(encounterMasterDto.getPatientId());
					dto.setAppointmentDate(encounterMasterDto.getAppointmentObject().getAppointmentDate());
					dto.setAppointmentId(appointmentMaster.getAppointmentId());
					dto.setTimeSlot(GlobalCommonDateUtils.getStringDate(appointmentMaster.getAppDateTime(), "HH:mm:ss"));
					dto.setDoctorId(encounterMasterDto.getDoctorId());
					if(encounterMasterDto.getDoctorId()!=null)
						dto.setDoctorName(encounterMasterDto.getDoctorName());
					else
						dto.setSpecialityName(encounterMasterDto.getSpecialityName());
						
						
					dto.setOrgId(encounterMasterDto.getOrganizationId());
					dto.setUnitId(encounterMasterDto.getUnitId());
					iTemplateTransactionService.sendTemplateURLS(dto);
				} else {
					return res;
				}
			}else{
				encounterMasterDto.setEncounterDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()).getTime());
				encounterMasterDto.setEncounterTime(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()), "HH:mm:ss"));
			}

			if (encounterMasterDto.getListKinDetailsDto()!=null && encounterMasterDto.getListKinDetailsDto().size() > 0) 
			{
				Iterator<KinDetailsDto> itrKinDetailsDto = encounterMasterDto.getListKinDetailsDto().iterator();
				int index = 0;
				while (itrKinDetailsDto.hasNext()) 
				{
					KinDetailsDto kinDetailsDto = itrKinDetailsDto.next();
					if (kinDetailsDto.getKinDetailsId() == 0) 
					{
						if (kinDetailsDto.getKinName().equals(""))
							continue;
						Response res = iKinDetailsDao.saveKinDetails(kinDetailsDto);
						if (index == 0) 
						{
							if (encounterMasterDto.getKinDetailsId() == null) 
							{
								Integer kinDetailsId = (Integer) res.getObject();
								encounterMasterDto.setKinDetailsId(kinDetailsId);
							}
						}
					} else {
						iKinDetailsDao.updateKinDetails(kinDetailsDto);
						encounterMasterDto.setKinDetailsId(kinDetailsDto.getKinDetailsId());
					}
					index++;
				}
			}

			String encountrNo = iGlobalUnitMasterDao.getGeneratedNo(0, encounterMasterDto.getUnitId(),IBillingCommonConstants.ENCOUNTER);
			encounterMasterDto.setEncounterNumber(encountrNo != null ? encountrNo : generateEncounterNumber());

			if (encounterMasterDto.getUserDefinedVisitTypeId() != null
					&& encounterMasterDto.getUserDefinedVisitTypeId() != 4
					&& encounterMasterDto.getUserDefinedVisitTypeId() != 5
					&& encounterMasterDto.getUserDefinedVisitTypeId() != 6 && encounterMasterDto.getUserDefinedVisitTypeId() != 7) 
			{
				List<PatientSponsorDetailsDto> priorityOne = new LinkedList<>();
				Stream<PatientSponsorDetailsDto> expenseDataSet = encounterMasterDto.getPatientSponsorDetailsDtosList().stream();
				priorityOne = expenseDataSet.filter(str -> str.getPriorityId().equals(1)).collect(Collectors.toList());
				
				DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto = new DoctorConsultationServiceMapperDto();
					doctorConsultationServiceMapperDto.setOrganizationId(encounterMasterDto.getOrganizationId());
					doctorConsultationServiceMapperDto.setUnitId(encounterMasterDto.getUnitId());
					doctorConsultationServiceMapperDto.setDoctorId(encounterMasterDto.getDoctorId());
					doctorConsultationServiceMapperDto.setSpecialityId(encounterMasterDto.getSpecialityId());
					doctorConsultationServiceMapperDto.setDeptWiseAppointment(encounterMasterDto.getDeptWiseAppointment());
					doctorConsultationServiceMapperDto.setPaymentEntitlementId(encounterMasterDto.getPaymentEntitlementTypeId());
					

					if (!priorityOne.isEmpty() && priorityOne.get(0).getPaymentEntitlementId() != SELF) {

					    doctorConsultationServiceMapperDto.setCompanyId(priorityOne.get(0).getPayeeId());
					} 

					
					if (!priorityOne.isEmpty()) {

					    if (priorityOne.get(0).getPaymentEntitlementId() == INSURANCE) {

					        doctorConsultationServiceMapperDto.setAssoCompanyId(priorityOne.get(0).getInsuranceId());
					    }
					}
					
				Response response = iDoctorConsultationServiceMapperService.getDoctorConsultationServiceBySpecialityAndDoctorId(doctorConsultationServiceMapperDto);
				if (response.getStatus().equals(SUCCESS) && response.getListObject().size() > 0) {
					DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto2 = (DoctorConsultationServiceMapperDto) response.getListObject().get(0);
					if (encounterMasterDto.getUserDefinedVisitTypeId() == 1) 
					{
						serviceId = doctorConsultationServiceMapperDto2.getNewVisitServiceId();
						serviceAmount = doctorConsultationServiceMapperDto2.getNewVisitRate();
						encounterMasterDto.setIsDocShare(doctorConsultationServiceMapperDto2.getNewVisitDoctorShare());
						encounterMasterDto.setIsDocReferral(doctorConsultationServiceMapperDto2.getNewVisitReferralShare());
					} else if (encounterMasterDto.getUserDefinedVisitTypeId() == 2) 
					{
						serviceId = doctorConsultationServiceMapperDto2.getFollowupVisitServiceId();
						serviceAmount = doctorConsultationServiceMapperDto2.getFollowupVisitRate();
						encounterMasterDto.setIsDocShare(doctorConsultationServiceMapperDto2.getFollowUpVisitDoctorShare());
						encounterMasterDto.setIsDocReferral(doctorConsultationServiceMapperDto2.getFollowUpVisitReferralShare());
					} else if (encounterMasterDto.getUserDefinedVisitTypeId() == 3) 
					{
						serviceId = doctorConsultationServiceMapperDto2.getSecondaryVisitServiceId();
						serviceAmount = doctorConsultationServiceMapperDto2.getSecondaryVisitRate();
						encounterMasterDto.setIsDocShare(doctorConsultationServiceMapperDto2.getSecondaryVisitDoctorShare());
						encounterMasterDto.setIsDocReferral(doctorConsultationServiceMapperDto2.getSecondaryVisitReferralShare());
					}
					encounterMasterDto.setServiceId(serviceId);
					encounterMasterDto.setServiceAmount(serviceAmount);

				}
			}

			
			if ((encounterMasterDto.getEncounterTypeId() == 1 || encounterMasterDto.getEncounterTypeId() == 7 || encounterMasterDto.getEncounterTypeId() == 9) && isPaidAppointment == 'N' && unitApplicationConfigurationDtos.getIsAppointmentRequired()=='Y') 
			{
				encounterMasterDto.setUnitTokenNumber(encounterMasterDto.getUnitTokenNumber() != null ? encounterMasterDto.getUnitTokenNumber(): (appointmentMaster != null && appointmentMaster.getTokenNumber() != null ? appointmentMaster.getTokenNumber() : null));
			}
			
			if(encounterMasterDto.getUnitTokenNumber() == null) {
				
				Response restoken = iAppointmentSchedulingService.getTokenNo(encounterMasterDto.getAppointmentId());
				
				if(restoken.getObject()!= null ) {
					AppointmentMaster dtoAppo = (AppointmentMaster) restoken.getObject();
					encounterMasterDto.setUnitTokenNumber(dtoAppo.getTokenNumber());
				}
				
			}

			if (encounterMasterDto.getDeptWiseAppointment().equals('N') && encounterMasterDto.getDoctorId() != null	 && isPaidAppointment == 'N'  && unitApplicationConfigurationDtos.getIsAppointmentRequired()=='Y') 
			{
				encounterMasterDto.setDocQueueNumber(this.generateDocQueueNumber(encounterMasterDto));
			}

			if (encounterMasterDto.getEncounterTypeId() == 5 || (encounterMasterDto.getDeptWiseAppointment().equals('Y') && isPaidAppointment == 'N') && unitApplicationConfigurationDtos.getIsAppointmentRequired()=='Y') 
			{
				encounterMasterDto.setSpecialityQueueNumber(this.generateSpecialityQueueNumber(encounterMasterDto));
				if (encounterMasterDto.getAppointmentId()!=null && encounterMasterDto.getAppointmentId() > 0 && encounterMasterDto.getEncounterTypeId() == 5) 
				{
					iAppointmentSchedulingDao.updateAppointmentTypeId(encounterMasterDto.getAppointmentId());
				}
			}
			// Saving encounter
			if(isPrePostBilling=='N' || encounterMasterDto.getEncounterTypeId() != 6)
				encounterMasterDto.setStatusId(8);
			else
				encounterMasterDto.setStatusId(5);
			
			if(encounterMasterDto.getDeptWiseAppointment()!=null && encounterMasterDto.getDeptWiseAppointment()=='Y')
			{
				encounterMasterDto.setPreferredDocId(encounterMasterDto.getDoctorId());
				encounterMasterDto.setDoctorId(null);
			}
			
			String patHis = "Diagnosis:\n\nMedical History: \n\n";
			encounterMasterDto.setPatHistory(patHis);
			Response encounterResponse = iEncounterMasterDao.saveEncounterMaster(encounterMasterDto);
			if(appointmentMaster!=null){
				encounterMasterDto.setAppointmentId(appointmentMaster.getAppointmentId());
			}

			if (encounterResponse.getStatus().equals(SUCCESS)) 
			{
				
				encounterResponse.setResObject(responseRate);
				EncounterMasterDto encounterMasterDto2 = (EncounterMasterDto) encounterResponse.getObject();
				if (encounterMasterDto.getEncounterTypeId() != 6) 
				{
					
					 iTPackageConsumptionMasterDao.expireMEPkgByVisitByPatientId(encounterMasterDto2.getPatientId(),1);//1 is visit type op
					// Saving Patient Sponsor
					 
					encounterMasterDto2.getPatientSponsorDetailsDtosList().forEach(psd -> psd.setApplicableDate(GlobalCommonDateUtils.getStringDate(encounterMasterDto.getEncounterDate(),"dd-MM-yyyy").concat(" 00:00:00")));
					
					Response patientSponsorDetailsResponse = iPatientSponsorDetailsService.savePatientSponsorDetails(encounterMasterDto2.getPatientSponsorDetailsDtosList(),	encounterMasterDto2.getEncounterId(), null,unitApplicationConfigurationDtos.getSelfPayeeId());
					if (isPaidAppointment == 'N' && updateRegistrationCharges=='Y') 
					{
						encounterResponse.setCode("Y");
					}
					if (isFromAppointmentScheduling=='Y' && updateRegistrationCharges=='Y' ) 
					{
						encounterResponse.setCode("Y");
					}
					if (encounterMasterDto.getEncounterTypeId() == 1 || encounterMasterDto.getEncounterTypeId() == 9) 
					{
						// Adding the doctorConsultationService in order master against encounter id
						if (serviceId != null) 
						{
							serviceList.add(serviceId);
							encounterResponse.setListObject(serviceList);
						}

					}
					
					if(encounterResponse.getStatus().equals(SUCCESS)&& encounterMasterDto2.getReferralMasterDto()!=null)
					{
						
						Response refRess = iReferralMasterService.saveReferralMaster(encounterMasterDto2.getReferralMasterDto());
						ReferralMasterDto dto = (ReferralMasterDto)refRess.getObject();
						if(refRess.getStatus().equals(SUCCESS) && refRess.getObject()!=null)
						{
							encounterMasterDto2.setReferralMasterDto((ReferralMasterDto)refRess.getObject());
							encounterMasterDto2.setReferralId(encounterMasterDto2.getReferralMasterDto().getReferralId());
							encounterMasterDto2.setReferralTypeId(encounterMasterDto2.getReferralMasterDto().getReferralTypeId());
						}else{
							encounterMasterDto2.setReferralId(dto.getReferralId());
							encounterMasterDto2.setReferralTypeId(dto.getReferralTypeId());
						}

					}

					if (encounterResponse.getStatus().equals(SUCCESS) && encounterMasterDto2.getReferralId() != null) 
					{
						ReferalDetailsDto referalDetailsDto = new ReferalDetailsDto();
							referalDetailsDto.setOrganizationId(encounterMasterDto2.getOrganizationId());
							referalDetailsDto.setUnitId(encounterMasterDto2.getUnitId());
							referalDetailsDto.setVisitTypeId(encounterMasterDto2.getVisitTypeId());
							referalDetailsDto.setEncounterId(encounterMasterDto2.getEncounterId());
							referalDetailsDto.setReferralId(encounterMasterDto2.getReferralId());
							referalDetailsDto.setReferralTypeId(encounterMasterDto2.getReferralTypeId());
							referalDetailsDto.setReferralRemark(encounterMasterDto2.getReferralRemark());
							referalDetailsDto.setCreatedBy(encounterMasterDto2.getCreatedBy());
							referalDetailsDto.setCreatedDate(GlobalCommonDateUtils.getStringDateFromLongDate(encounterMasterDto2.getCreatedDate(), "dd-MM-yyyy HH:mm:ss"));
							referalDetailsDto.setUpdatedBy(encounterMasterDto2.getUpdatedBy());
							referalDetailsDto.setUpdatedDate(GlobalCommonDateUtils.getStringDateFromLongDate(encounterMasterDto2.getUpdatedDate(), "dd-MM-yyyy HH:mm:ss"));
							referalDetailsDto.setStatus('A');
						refRes = iReferalDetailsService.saveReferalDetails(referalDetailsDto);

					}
					if (isPrePostBilling.equals('N')) 
					{
						if (encounterMasterDto.getAppointmentId() != null
								&& encounterMasterDto.getAppointmentId() > 0) 
						{
							SlotMasterDto slotMasterDto = new SlotMasterDto();
								slotMasterDto.setAppointmentId(encounterMasterDto.getAppointmentId());
								slotMasterDto.setAppointmentStatusId(APPOINTMENTSTATUSID5);
								slotMasterDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(encounterMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
								slotMasterDto.setUpdatedBy(encounterMasterDto.getUpdatedBy());
							iAppointmentSchedulingService.updateAppointmentStatus(slotMasterDto);
						}

						if (encounterMasterDto.getEncounterId() > 0) {
							EncounterMasterDto encounterMasterDtonew = new EncounterMasterDto();
								encounterMasterDtonew.setEncounterId(encounterMasterDto.getEncounterId());
								//encounterMasterDtonew.setCurrentDate(GlobalCommonDateUtils.getStringDate(encounterMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
								encounterMasterDtonew.setStatusId(5);
								encounterMasterDtonew.setUpdatedBy(encounterMasterDto.getUpdatedBy());
							this.updateEncounterStatus(encounterMasterDtonew);
						}
					}
					// <----For skipping nursing assessment
					if (isNursingAssessmentRequiredForUnit != null && (isNursingAssessmentRequiredForUnit == 'N'
							|| isNursingAssessmentRequiredForSpeciality == 'N')) 
					{
						iEncounterAssignmenMapperService.saveEncounterAssignmentIfAssessignmentIsFalse(encounterMasterDto);

						if (isKioskRequired == 'Y') 
						{
							encounterMasterDto.setIsCalled('Y');
							patientCalled(encounterMasterDto);
						}
					}
					// For skipping nursing assessment---->

				} else {

					// in case of review appointment the patient will redirected to doctor desk
					// directly.
					if (encounterResponse.getStatus().equals(SUCCESS) && encounterMasterDto2.getReferralId() != null) 
					{
						ReferalDetailsDto referalDetailsDto = new ReferalDetailsDto();
							referalDetailsDto.setOrganizationId(encounterMasterDto2.getOrganizationId());
							referalDetailsDto.setUnitId(encounterMasterDto2.getUnitId());
							referalDetailsDto.setVisitTypeId(encounterMasterDto2.getVisitTypeId());
							referalDetailsDto.setEncounterId(encounterMasterDto2.getEncounterId());
							referalDetailsDto.setReferralId(encounterMasterDto2.getReferralId());
							referalDetailsDto.setReferralTypeId(encounterMasterDto2.getReferralTypeId());
							referalDetailsDto.setReferralRemark(encounterMasterDto2.getReferralRemark());
							referalDetailsDto.setCreatedBy(encounterMasterDto2.getCreatedBy());
							referalDetailsDto.setCreatedDate(GlobalCommonDateUtils.getStringDateFromLongDate(encounterMasterDto2.getCreatedDate(), "dd-MM-yyyy HH:mm:ss"));
							referalDetailsDto.setUpdatedBy(encounterMasterDto2.getUpdatedBy());
							referalDetailsDto.setUpdatedDate(GlobalCommonDateUtils.getStringDateFromLongDate(encounterMasterDto2.getUpdatedDate(), "dd-MM-yyyy HH:mm:ss"));
							referalDetailsDto.setStatus('A');
						refRes = iReferalDetailsService.saveReferalDetails(referalDetailsDto);

					}
					
					// update appointment status
					if (encounterMasterDto.getAppointmentId() != null && encounterMasterDto.getAppointmentId() > 0) {
						SlotMasterDto slotMasterDto = new SlotMasterDto();
							slotMasterDto.setAppointmentId(encounterMasterDto.getAppointmentId());
							slotMasterDto.setAppointmentStatusId(APPOINTMENTSTATUSID5);
							slotMasterDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(encounterMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
							slotMasterDto.setUpdatedBy(encounterMasterDto.getUpdatedBy());
						iAppointmentSchedulingService.updateAppointmentStatus(slotMasterDto);
					}

					if (encounterMasterDto.getEncounterId() > 0) {
						EncounterMasterDto encounterMasterDtonew = new EncounterMasterDto();
							encounterMasterDtonew.setEncounterId(encounterMasterDto.getEncounterId());
							//encounterMasterDtonew.setCurrentDate(GlobalCommonDateUtils.getStringDate(encounterMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
							encounterMasterDtonew.setStatusId(5);
							encounterMasterDtonew.setUpdatedBy(encounterMasterDto.getUpdatedBy());
						this.updateEncounterStatus(encounterMasterDto);
					}

					// <----For skipping nursing assessment
					iEncounterAssignmenMapperService.saveEncounterAssignmentIfAssessignmentIsFalse(encounterMasterDto);

					encounterMasterDto.setIsCalled('Y');
					patientCalled(encounterMasterDto);
					// For skipping nursing assessment---->

					for (Integer orderDetailsId : encounterMasterDto.getOrderDetailsIds()) {
						iOrderDetailsMasterDao.updateReviewedServiceStatus(orderDetailsId);
					}

				}
				// <----

				iEncounterMasterDao.checkReferalAndUpdate(encounterMasterDto2);

				// ---->

			}
			if(unitApplicationConfigurationDtos.getAdminServiceMasterId() != null){
                EncounterMasterDto resEncDto = (EncounterMasterDto) encounterResponse.getObject();
                resEncDto.setAdminServiceMasterId(unitApplicationConfigurationDtos.getAdminServiceMasterId());

                return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, encounterResponse.getListObject(), resEncDto);
            }
			if(appointmentMaster!=null) {
//				encounterResponse.setResObject(appointmentMaster.getAppointmentUniqueId());
				encounterResponse.setListObject2(List.of(appointmentMaster.getAppointmentUniqueId()));
			}
			return encounterResponse;

		} catch (Exception e) {
		logger.error("saveEncounterMaster failed", e);
			System.out.println("Error................."+e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	

	@Override
	public Integer generateDocQueueNumber(EncounterMasterDto encounterMasterDto) {
		try {
			Integer docQueueNumber = iEncounterMasterDao.getPreviousDocQueueNumber(encounterMasterDto);
			if (docQueueNumber != null)
				return docQueueNumber + 1;
			else
				return 1;

		} catch (Exception e) {
		logger.error("generateDocQueueNumber failed", e);
		}
		return null;
	}

	private Integer generateUnitTokenNumber(EncounterMasterDto encounterMasterDto) {
		try {
			Integer unitTokenNumber = iEncounterMasterDao.getPreviousUnitTokenNumber(encounterMasterDto);
			if (unitTokenNumber != null)
				return unitTokenNumber + 1;
			else
				return 1;
		} catch (Exception e) {
		logger.error("generateUnitTokenNumber failed", e);
		}
		return null;
	}

	@Override
	public Integer generateSpecialityQueueNumber(EncounterMasterDto encounterMasterDto) {
		try {
			Integer specialityQueueNumber = iEncounterMasterDao.getPreviousSpecialityQueueNumber(encounterMasterDto);
			if (specialityQueueNumber != null)
				return specialityQueueNumber + 1;
			else
				return 1;

		} catch (Exception e) {
		logger.error("generateSpecialityQueueNumber failed", e);
		}
		return null;
	}

	@Override
	@Transactional
	public Response getEncounterDetailsById(Integer encounterId) throws ApplicationException {
		try {
			Response res = iEncounterMasterDao.getEncounterDetailsById(encounterId);
			EncounterMasterDto encounterMasterDto = (EncounterMasterDto) res.getObject();
			Response patientSponsorRes = iPatientSponsorDetailsService.getSponsorDetailsByEncounterId(encounterId);
            if(patientSponsorRes.getListObject() != null && patientSponsorRes.getListObject().size() > 0) {
            	encounterMasterDto.setPatientSponsorDetailsDtosList(patientSponsorRes.getListObject());
            }
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, encounterMasterDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Transactional
	private String generateEncounterNumber() throws ApplicationException {
		try {
			Long incrementedId = iEncounterMasterDao.getNextValEncounterMasterId();
			String encouterPrefix = "ENC-";

			StringBuilder encounter = new StringBuilder(encouterPrefix);

			int length = Long.valueOf(incrementedId).toString().length();
			encounter = length == 1 ? (encounter.append("000000").append(incrementedId))
					: length == 2 ? (encounter.append("00000").append(incrementedId))
							: length == 3 ? (encounter.append("0000").append(incrementedId))
									: length == 4 ? (encounter.append("000").append(incrementedId))
											: length == 5 ? (encounter.append("00").append(incrementedId))
													: length == 6 ? (encounter.append("0").append(incrementedId))
															: length == 7 ? (encounter.append(incrementedId))
																	: encounter.append("");
			return encounter.toString();
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
		}
	}

	@Transactional
	public Integer assignVisitTypeIdAccordingToConditions(SlotMasterDto encounterMasterDto)
			throws ApplicationException {
		try {
			Integer visitTypeId = 0;
			boolean breakList = false;
			String encounterDate = null;
			List<EncounterMasterDto> encounterMasterDtosNewList = new ArrayList<>();
			List<Integer> listForVisigtTypeCount = new ArrayList<>();
			Set<String> listOfHolidayAndLinkedSet = new LinkedHashSet<>();
			Integer daysCount = 0;
			Integer dateConflictCount = 0;
			String currentDate = encounterMasterDto.getCurrentDate();
			List<String> totalDates = null;
			ListIterator<String> datesItr = null;
			DateTime extendedDate = null;
			DateTime lastDate = null;

			OPConsultationConfigurationMasterDto opConsultationConfigurationMasterDto = new OPConsultationConfigurationMasterDto();
				opConsultationConfigurationMasterDto.setOrganizationId(encounterMasterDto.getOrganizationId());
				opConsultationConfigurationMasterDto.setUnitId(encounterMasterDto.getUnitId());
				opConsultationConfigurationMasterDto.setSpecialityId(encounterMasterDto.getSpecialityId());
				opConsultationConfigurationMasterDto.setDoctorId(encounterMasterDto.getDoctorId());
			Response res = iOPConsultationConfigurationMasterService.getOPConsultationConfigurationBySpecialityId(opConsultationConfigurationMasterDto);

			if (res.getStatus().equals(SUCCESS)) {
				// <-----(1) Fetching last encounter details of patient with same doctor till
				// last primary visit
				EncounterMasterDto enMasterDto = new EncounterMasterDto();
				enMasterDto.setOrganizationId(encounterMasterDto.getOrganizationId());
				enMasterDto.setUnitId(encounterMasterDto.getUnitId());
				enMasterDto.setDoctorId(encounterMasterDto.getDoctorId());
				enMasterDto.setPatientId(encounterMasterDto.getPatientId());
				enMasterDto.setSpecialityId(encounterMasterDto.getSpecialityId());
				// --Fetching records in descending order--//
				List<EncounterMasterDto> encounterMasterDtosList = iEncounterMasterDao
						.getEncounterDetailsByPatientDoctorVisitTypeId(enMasterDto);

				// <--- (1.1) checking whether the list is empty or not if not that means
				// patient has some entries in encounter master with same doctor
				if (!encounterMasterDtosList.isEmpty() || encounterMasterDtosList.size() > 0) {
					ListIterator<EncounterMasterDto> itr = encounterMasterDtosList.listIterator();
					while (itr.hasNext() && breakList == false) {
						EncounterMasterDto masterDto = itr.next();

						/*
						 * //--created a new list(encounterMasterDtosNewList) which will contain records
						 * till his last primary visit as we are fetching the list in descending
						 * order--//
						 */
						encounterMasterDtosNewList.add(masterDto);

						// --created a list(listForVisigtTypeCount) which will store only the visit
						// types of all records in the encounterMasterDtosNewList--//
						listForVisigtTypeCount.add(masterDto.getUserDefinedVisitTypeId());

						if (masterDto.getUserDefinedVisitTypeId() == 1) {

							// --getting encounter date of last primary visit--//
							encounterDate = masterDto.getEncounterDateString();

							// -- then breaking the loop as we do not require rest of the data--//
							breakList = true;
						}

					}
					// ---->(1.1)
				} else {
					/*
					 * //<---(1.2) if the list is empty, that means that patient does not have any
					 * appointments in the past with that specific doctor, then it has to be its
					 * primary visit--->
					 */
					return visitTypeId = 1;
				}

				// <-----(2) Getting total followUp Visits in followUpCount and secondary visit
				// in secondaryCount
				Integer followUpCount = Collections.frequency(listForVisigtTypeCount, 2);
				Integer secondaryCount = Collections.frequency(listForVisigtTypeCount, 3);
				// ----->(2)

				// <-----Retrieving list of holidays
				UnitHolidayCalenderMapperDto unitHolidayCalenderMapperDto = new UnitHolidayCalenderMapperDto();
				unitHolidayCalenderMapperDto.setOrganizationId(encounterMasterDto.getOrganizationId());
				unitHolidayCalenderMapperDto.setUnitId(encounterMasterDto.getUnitId());
				Response holidayListRes = iUnitHolidayCalenderDao
						.getActiveUnitHolidayCalenerList(unitHolidayCalenderMapperDto);
				List<HolidayCalenderMasterDto> holidayList = holidayListRes.getListObject();
				// ----->

				// <-----Retrieving list of weekends
				WeekendMasterDto weekendMasterDto = new WeekendMasterDto();
				weekendMasterDto.setOrganizationId(encounterMasterDto.getOrganizationId());
				weekendMasterDto.setUnitId(encounterMasterDto.getUnitId());
				Response weekendRes = iWeekendMasterService.getWeekendMasterList(weekendMasterDto);
				List<WeekendMasterDto> weekendsList = weekendRes.getListObject();
				// ----->

				// <-----Merging holidays and weekends list to avoid duplication. (Used linked
				// hash set)
				ListIterator<HolidayCalenderMasterDto> holidayItr = holidayList.listIterator();
				while (holidayItr.hasNext()) {
					HolidayCalenderMasterDto holidayCalenderMasterDto = holidayItr.next();
					listOfHolidayAndLinkedSet.add(holidayCalenderMasterDto.getHolidayDate().substring(0, 10));
				}

				ListIterator<WeekendMasterDto> weekendItr = weekendsList.listIterator();
				while (weekendItr.hasNext()) {
					WeekendMasterDto weekendMasterDto2 = weekendItr.next();
					listOfHolidayAndLinkedSet.add(weekendMasterDto2.getWeekendDate().substring(0, 10));
				}

				// <-- Converting set to list
				List<String> ListOfHolidayAndLinkedList = new ArrayList<>(listOfHolidayAndLinkedSet);
				// -->
				// ----->

				// <-----(3)Calling OP Consultation Configuration of speciality.
				/*
				 * OPConsultationConfigurationMasterDto opConsultationConfigurationMasterDto =
				 * new OPConsultationConfigurationMasterDto();
				 * opConsultationConfigurationMasterDto.setOrganizationId(encounterMasterDto.
				 * getOrganizationId());
				 * opConsultationConfigurationMasterDto.setUnitId(encounterMasterDto.getUnitId()
				 * ); opConsultationConfigurationMasterDto.setSpecialityId(encounterMasterDto.
				 * getSpecialityId()); Response res = iOPConsultationConfigurationMasterService
				 * .getOPConsultationConfigurationBySpecialityId(
				 * opConsultationConfigurationMasterDto);
				 * 
				 * if (res.getStatus().equals(SUCCESS)) {
				 */
				OPConsultationConfigurationMasterDto opConsultationConfigurationMasterRes = (OPConsultationConfigurationMasterDto) res
						.getObject();

				// <-----(3.1) First of all check whether current date is between first visit to
				// last date of secondary
				if(opConsultationConfigurationMasterRes.getFollowupVisitDays() != null && opConsultationConfigurationMasterRes.getSecondaryVisitDays() != null) {
					
					Integer lastDateFromEncounterDateCount = opConsultationConfigurationMasterRes.getFollowupVisitDays()
							+ opConsultationConfigurationMasterRes.getSecondaryVisitDays();

					DateTime enounterDateOfLastPrimaryVisit = new DateTime(encounterDate);
					DateTime lastDateFromEncounterDate = enounterDateOfLastPrimaryVisit
							.plusDays(lastDateFromEncounterDateCount);

					totalDates = slotsCreationService.getListOfDatesAsPerDayBetweenTwoDates(enounterDateOfLastPrimaryVisit,
							lastDateFromEncounterDate);

					datesItr = totalDates.listIterator();
					while (datesItr.hasNext()) {

						if (slotsCreationService.isBetweenHolidaysAndWeekends(ListOfHolidayAndLinkedList,
								datesItr.next()) == true)
							// ---counting the date which are within holiday and weekend then---//
							dateConflictCount++;

					}

					// --- adding the dateConflictCount in daysCount to get actual extension of
					// dates.
					dateConflictCount = lastDateFromEncounterDateCount + dateConflictCount;
					extendedDate = enounterDateOfLastPrimaryVisit.plusDays(dateConflictCount);
					dateConflictCount = 0;
					// <---(3.1.1) Checking whether the current date lies between encounterDate and
					// extendedDate
					if (slotsCreationService.isDateBetweenTwoSpecifiedDates(encounterDate,
							extendedDate.toString().substring(0, 10), currentDate) == false) {
						// <--(3.1.1.1) If not then visit type has to be 1
						return visitTypeId = 1;
						// -->
					} else {

						// <--(3.1.1.2) If current date lies between encounterDate and extendedDate that
						// means he is eligible for either follow up or secondary visit
						ListIterator<EncounterMasterDto> encounterMasterDtosNewListItr = encounterMasterDtosNewList
								.listIterator();

						// --fetching only the last record of encounterMasterDtosNewList in
						// lastEncounterOfPatientsObj --//
						EncounterMasterDto lastEncounterOfPatientsObj = null;
						while (encounterMasterDtosNewListItr.hasNext()) {
							lastEncounterOfPatientsObj = encounterMasterDtosNewListItr.next();
							break;
						}
						// <--(3.1.1.2.1) if visit type of last encounter is primary then
						if (lastEncounterOfPatientsObj != null
								&& lastEncounterOfPatientsObj.getUserDefinedVisitTypeId() == 1) {

							// <--(3.1.1.2.1.1) check is that patient is eligible for follow up or not
							daysCount = opConsultationConfigurationMasterRes.getFollowupVisitDays();

							lastDate = enounterDateOfLastPrimaryVisit.plusDays(daysCount);

							totalDates = slotsCreationService
									.getListOfDatesAsPerDayBetweenTwoDates(enounterDateOfLastPrimaryVisit, lastDate);

							datesItr = totalDates.listIterator();
							while (datesItr.hasNext()) {

								if (slotsCreationService.isBetweenHolidaysAndWeekends(ListOfHolidayAndLinkedList,
										datesItr.next()) == true)
									dateConflictCount++;
							}

							dateConflictCount = daysCount + dateConflictCount;
							extendedDate = enounterDateOfLastPrimaryVisit.plusDays(dateConflictCount);
							dateConflictCount = 0;
							if (slotsCreationService.isDateBetweenTwoSpecifiedDates(encounterDate,
									extendedDate.toString().substring(0, 10), currentDate) == true) {
								// <--(3.1.1.2.1.1.1) if he is eligible for follow up then visit type will set
								// to 2
								return visitTypeId = 2;
								// -->
							} else {
								// <--(3.1.1.2.1.1.2)else visit type will set to 3
								return visitTypeId = 3;
								// -->
							}
							// -->(3.1.1.2.1.1)
						}
						// -->(3.1.1.2.1)

						// <--(3.1.1.2.2)if visit type of last encounter is follow up then
						if (lastEncounterOfPatientsObj.getUserDefinedVisitTypeId() == 2) {

							// <--(3.1.1.2.2.1) first check the follow up count of patient and follow up
							// count specified in OPConsultationMaster
							if (followUpCount >= opConsultationConfigurationMasterRes.getFollowupVisitCount()) {
								// <--(3.1.1.2.2.1.1) If followUpCount is less than or equal to
								// getFollowupVisitCount then patient is eligible for secondary
								daysCount = opConsultationConfigurationMasterRes.getSecondaryVisitDays();

								lastDate = enounterDateOfLastPrimaryVisit.plusDays(daysCount);

								totalDates = slotsCreationService
										.getListOfDatesAsPerDayBetweenTwoDates(enounterDateOfLastPrimaryVisit, lastDate);

								datesItr = totalDates.listIterator();
								while (datesItr.hasNext()) {

									if (slotsCreationService.isBetweenHolidaysAndWeekends(ListOfHolidayAndLinkedList,
											datesItr.next()) == true)
										dateConflictCount++;
								}

								dateConflictCount = daysCount + dateConflictCount;
								extendedDate = enounterDateOfLastPrimaryVisit.plusDays(dateConflictCount);
								dateConflictCount = 0;
								// <--(3.1.1.2.2.1.1.1) If it exceeds then patient is eligible for secondary
								if (slotsCreationService.isDateBetweenTwoSpecifiedDates(encounterDate,
										extendedDate.toString().substring(0, 10), currentDate) == true) {
									// <--(3.1.1.2.2.1.1.1) If yes then visit type will set to 3
									return visitTypeId = 3;
									// -->(3.1.1.2.2.1.1.1)
								} else {
									// <--(3.1.1.2.2.1.1.2) else visit type will set to 1
									return visitTypeId = 1;
									// -->(3.1.1.2.2.1.1.2)
								}

								// -->(3.1.1.2.2.1.1.1)
								// -->(3.1.1.2.2.1.1)

							}
							// -->(3.1.1.2.2.1)

							// <--(3.1.1.2.2.2) else check either that patient is eligible for another
							// follow up
							else {

								daysCount = opConsultationConfigurationMasterRes.getFollowupVisitDays();

								lastDate = enounterDateOfLastPrimaryVisit.plusDays(daysCount);

								totalDates = slotsCreationService
										.getListOfDatesAsPerDayBetweenTwoDates(enounterDateOfLastPrimaryVisit, lastDate);

								datesItr = totalDates.listIterator();
								while (datesItr.hasNext()) {
									if (slotsCreationService.isBetweenHolidaysAndWeekends(ListOfHolidayAndLinkedList,
											datesItr.next()) == true)
										dateConflictCount++;
								}

								dateConflictCount = daysCount + dateConflictCount;
								extendedDate = enounterDateOfLastPrimaryVisit.plusDays(dateConflictCount);
								dateConflictCount = 0;

								if (slotsCreationService.isDateBetweenTwoSpecifiedDates(encounterDate,
										extendedDate.toString().substring(0, 10), currentDate) == true) {
									// <--(3.1.1.2.2.2.1) if eligible for next follow up then visit type will set to
									// 2
									return visitTypeId = 2;
									// -->(3.1.1.2.2.2.1)
								} else {
									// <--(3.1.1.2.2.2.2)
									return visitTypeId = 3;
									// <--(3.1.1.2.2.2.2)
								}
							}
							// -->(3.1.1.2.2.2)

						}
						// -->(3.1.1.2.2)

						// <--(3.1.1.2.1)if visit type of last encounter is secondary then
						if (lastEncounterOfPatientsObj.getUserDefinedVisitTypeId() == 3) {

							// <--(3.1.1.2.1.1) if secondaryCount is less than or equal to
							// getSecondaryVisitCount of OP Consultation Configuration
							if (secondaryCount >= opConsultationConfigurationMasterRes.getSecondaryVisitCount())
								// <--(3.1.1.2.1.1.1) Set visit type to 1
								return visitTypeId = 1;
							// -->(3.1.1.2.1.1.1)
							// -->(3.1.1.2.1.1)

							// <--(3.1.1.2.1.2)else check whether that patient is eligible for another
							// secondary visit
							else {

								daysCount = opConsultationConfigurationMasterRes.getSecondaryVisitDays();

								lastDate = enounterDateOfLastPrimaryVisit.plusDays(daysCount);

								totalDates = slotsCreationService
										.getListOfDatesAsPerDayBetweenTwoDates(enounterDateOfLastPrimaryVisit, lastDate);

								datesItr = totalDates.listIterator();
								while (datesItr.hasNext()) {
									if (slotsCreationService.isBetweenHolidaysAndWeekends(ListOfHolidayAndLinkedList,
											datesItr.next()) == true)

										dateConflictCount++;
								}

								dateConflictCount = daysCount + dateConflictCount;
								extendedDate = enounterDateOfLastPrimaryVisit.plusDays(dateConflictCount);
								dateConflictCount = 0;

								if (slotsCreationService.isDateBetweenTwoSpecifiedDates(encounterDate,
										extendedDate.toString().substring(0, 10), currentDate) == true) {
									// <--(3.1.1.2.1.2.1)
									return visitTypeId = 3;
									// -->(3.1.1.2.1.2.1)
								} else {
									// <--(3.1.1.2.1.2.2)
									return visitTypeId = 1;
									// -->(3.1.1.2.1.2.2)
								}

							} // -->(3.1.1.2.1.2)
						}
						// -->(3.1.1.2.1)
					}
				}
				

			} else {
				//System.out.println("OPConsultationConfigurationForSpecialityIdDoesNotExist");
			}

			// ----->(3)
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
		}
		return 1;
	}

	@Override
	public Response saveOrderDetails(EncounterMasterDto encounterMasterDto,Integer serviceId,Integer turn)
	{
		try{
			
			BigDecimal serviceAmt = BigDecimal.ZERO;
			BigDecimal taxAmt = BigDecimal.ZERO;
			BigDecimal netAmt = BigDecimal.ZERO;
			
			Integer companyOrSelf = 0 ; //self=1 contract=1
			ContractConsumptionDto consumptionDto = null;
			
			Long newDate = TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()).getTime();
			
			OrderMasterDto orderMasterDto = new OrderMasterDto();
				orderMasterDto.setVisitTypeId(OP);// op==1
				orderMasterDto.setEncounterId(encounterMasterDto.getEncounterId());
				orderMasterDto.setPatientId(encounterMasterDto.getPatientId());
				orderMasterDto.setCreatedBy(encounterMasterDto.getCreatedBy());
				orderMasterDto.setUpdatedBy(encounterMasterDto.getUpdatedBy());
				orderMasterDto.setCreatedDate(newDate);
				orderMasterDto.setUpdatedDate(newDate);
				orderMasterDto.setOrderDate(newDate);
				orderMasterDto.setOrderStatus('A');
				orderMasterDto.setOrgId(encounterMasterDto.getOrganizationId());
				orderMasterDto.setOrgUnitId(encounterMasterDto.getUnitId());
				orderMasterDto.setOrderStatusId(1);// order status==1
				orderMasterDto.setPatientTypeId(encounterMasterDto.getPatientCategoryId());
				orderMasterDto.setBillingBedCategoryId(encounterMasterDto.getBillingBedCategoryId());
				orderMasterDto.setPaymentEntitlementTypeId(encounterMasterDto.getPaymentEntitlementTypeId());
				orderMasterDto.setReferralId(encounterMasterDto.getReferralId());
			//-------order details-------//
			OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
				orderDetailsMasterDto.setServiceId(serviceId);
				orderDetailsMasterDto.setServiceIsBilled(0);
				orderDetailsMasterDto.setOrderQty(1);
				orderDetailsMasterDto.setCreatedBy(encounterMasterDto.getCreatedBy());
				orderDetailsMasterDto.setUpdatedBy(encounterMasterDto.getUpdatedBy());
				orderDetailsMasterDto.setOrderDate(newDate);
				orderDetailsMasterDto.setOrderSchDate(newDate);
				orderDetailsMasterDto.setOrdRenderDatetime(newDate);
				orderDetailsMasterDto.setCreatedDate(newDate);
				orderDetailsMasterDto.setUpdatedDate(newDate);
				orderDetailsMasterDto.setStatus('A');
				orderDetailsMasterDto.setOrgId(encounterMasterDto.getOrganizationId());
				orderDetailsMasterDto.setOrgUnitId(encounterMasterDto.getUnitId());
				orderDetailsMasterDto.setOrdDocId(encounterMasterDto.getDoctorId()!=null ? encounterMasterDto.getDoctorId() : encounterMasterDto.getPreferredDocId());
				orderDetailsMasterDto.setOrdDocSplId(encounterMasterDto.getSpecialityId());
				orderDetailsMasterDto.setServiceRenderingDeptId(encounterMasterDto.getConsultationSpecialityId());
				orderDetailsMasterDto.setDepartmentId(encounterMasterDto.getConsultationSpecialityId());
				orderDetailsMasterDto.setOrdCancelled('N');
				orderDetailsMasterDto.setNetAmount(BigDecimal.ZERO);
				orderDetailsMasterDto.setPatientCouponTemplateMapperId(encounterMasterDto.getPatientCouponTemplateMapperId()!= null ? encounterMasterDto.getPatientCouponTemplateMapperId() : null);

				orderDetailsMasterDto.setTariffId(encounterMasterDto.getDefaultSelfTariffId());
				if(encounterMasterDto.getIsAdminService() == null || encounterMasterDto.getIsAdminService() != Boolean.TRUE){
					orderDetailsMasterDto.setIsConsultationService('Y');
				}

				
				if(encounterMasterDto.getEncounterTypeId()==9)
				{
					Tax tax = iEncounterMasterDao.getTaxPercentageByServiceId(encounterMasterDto.getServiceId(),encounterMasterDto.getUnitId());
					
					orderDetailsMasterDto.setPatientClassPercentage(BigDecimal.ZERO);
					orderDetailsMasterDto.setServiceAmt(BigDecimal.ZERO);
					orderDetailsMasterDto.setDummyServiceAmt(BigDecimal.ZERO);
					orderDetailsMasterDto.setTariffId(encounterMasterDto.getDefaultSelfTariffId());
					orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
					orderDetailsMasterDto.setOrderEmergencyFlag('N');
					orderDetailsMasterDto.setServiceRendered(0);
					orderDetailsMasterDto.setOrdRenderDatetime(newDate);
					orderDetailsMasterDto.setServiceChargeable(1);
					orderDetailsMasterDto.setServiceIsBilled(0);
					orderDetailsMasterDto.setIsDrug('N');
					orderDetailsMasterDto.setOrdTotalAmt(BigDecimal.ZERO);
					orderDetailsMasterDto.setOrdConcession(BigDecimal.ZERO);
					orderDetailsMasterDto.setTaxAmount(BigDecimal.ZERO);
					orderDetailsMasterDto.setNetAmount(BigDecimal.ZERO);
					if(tax != null) {
					orderDetailsMasterDto.setTaxPer(BigDecimal.valueOf(tax.getTaxPercentage()));
					orderDetailsMasterDto.setTaxId(tax.getId());
					}else {
						orderDetailsMasterDto.setTaxPer(BigDecimal.ZERO);
					}
					orderDetailsMasterDto.setSelfPayable(BigDecimal.ZERO);
					orderDetailsMasterDto.setPayeeId(encounterMasterDto.getPayeeId());
					orderDetailsMasterDto.setCreditPayable(BigDecimal.ZERO);
					//orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
					orderDetailsMasterDto.setPackageConsumptionAmt(BigDecimal.ZERO);
					orderDetailsMasterDto.setCoPayPer(new BigDecimal(100));
					orderDetailsMasterDto.setBedCategoryId(encounterMasterDto.getBillingBedCategoryId());
					List<OrderDetailsPayeeMapperDto> list = new LinkedList<>();
					OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
						detailsPayeeMapperDto.setPayeeId(encounterMasterDto.getPayeeId());
						detailsPayeeMapperDto.setPayeeAmount(BigDecimal.ZERO);
						detailsPayeeMapperDto.setServiceId(serviceId);
					list.add(detailsPayeeMapperDto);	
					orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(list);
				
					List<OrderDetailsPackageMapperDto> mpprList = new ArrayList<>();
					OrderDetailsPackageMapperDto detailsPackageMapper = new OrderDetailsPackageMapperDto();
						detailsPackageMapper.setIsPackageService('N');
						detailsPackageMapper.setIsCapService('N');
						detailsPackageMapper.setPackageAmount(BigDecimal.ZERO);
						detailsPackageMapper.setServiceId(serviceId);
						detailsPackageMapper.setServiceAmount(BigDecimal.ZERO);
						detailsPackageMapper.setStatus('A');
						mpprList.add(detailsPackageMapper);
					orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(mpprList);
					
					List orderDetailsList = new ArrayList();
					orderDetailsList.add(orderDetailsMasterDto);
					orderMasterDto.setListOrderDetailsMasterDto(orderDetailsList);
					
					Response orderRes = iOrderMasterService.saveOrderMaster(orderMasterDto);
					return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
				
				}else{
					
				consumptionDto = new ContractConsumptionDto();
					consumptionDto.setOrganizationId(encounterMasterDto.getOrganizationId());
					consumptionDto.setUnitId(encounterMasterDto.getUnitId());
					consumptionDto.setVisitTypeId(OP);
					consumptionDto.setPatientTypeId(encounterMasterDto.getPatientCategoryId());
					consumptionDto.setBillingBedCategoryId(encounterMasterDto.getBillingBedCategoryId());
					consumptionDto.setPaymentEntitlementId(SELF);
					consumptionDto.setServiceId(serviceId);
					//consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDateFromLongDate(newDate, "yyyy-MM-dd"));
					consumptionDto.setContractId(encounterMasterDto.getContractId());
					consumptionDto.setEncounterId(encounterMasterDto.getEncounterId());
					consumptionDto.setDefaultSelfTariffId(encounterMasterDto.getDefaultSelfTariffId());
					consumptionDto.setPatientId(encounterMasterDto.getPatientId());
					consumptionDto.setSelfPayeeId(encounterMasterDto.getPayeeId());
				    Response res = null;
				    if(encounterMasterDto.getServiceAmount()!=null)
				    	consumptionDto.setConsultationAmount(encounterMasterDto.getServiceAmount());
				    consumptionDto.setPatientClassId(encounterMasterDto.getPatientClassId());	
				    consumptionDto.setIsConsultationService('Y');
				    consumptionDto.setDocSplId(encounterMasterDto.getDeptId() != null ? encounterMasterDto.getDeptId() :null);
				    consumptionDto.setIsFromEncounter(1);
				    res =iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
						consumptionDto=(ContractConsumptionDto)res.getObject();
				
					if(res.getObject()!=null)
					{
						orderDetailsMasterDto.setPatientClassPercentage(new BigDecimal(consumptionDto.getPatientClassPercentage()));
						orderDetailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
						orderDetailsMasterDto.setDummyServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
						orderDetailsMasterDto.setTariffId(encounterMasterDto.getDefaultSelfTariffId());
						orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
						orderDetailsMasterDto.setOrderEmergencyFlag('N');
						orderDetailsMasterDto.setServiceRendered(0);
						orderDetailsMasterDto.setOrdRenderDatetime(newDate);
						orderDetailsMasterDto.setServiceChargeable(1);
						orderDetailsMasterDto.setServiceIsBilled(0);
						orderDetailsMasterDto.setIsDrug('N');
						orderDetailsMasterDto.setOrdTotalAmt(new BigDecimal(consumptionDto.getServiceRate()));
						orderDetailsMasterDto.setOrdConcession(new BigDecimal(consumptionDto.getConcession()));
						orderDetailsMasterDto.setTaxId(consumptionDto.getTaxId());
						orderDetailsMasterDto.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()));
						if(encounterMasterDto.getDiscountAmount() != null && encounterMasterDto.getDiscountAmount()>0){
							orderDetailsMasterDto.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice() - encounterMasterDto.getDiscountAmount()));
							orderDetailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable() - encounterMasterDto.getDiscountAmount()));
							orderDetailsMasterDto.setOrdDiscount(new BigDecimal(encounterMasterDto.getDiscountAmount()));
							
							if(consumptionDto.getOrderDetailsPayeeMapperDtosList() != null){
								consumptionDto.getOrderDetailsPayeeMapperDtosList().get(0).setPayeeAmount(new BigDecimal(consumptionDto.getFinalPrice() - encounterMasterDto.getDiscountAmount()));
								consumptionDto.getOrderDetailsPayeeMapperDtosList().get(0).setSpiltBillAmt(new BigDecimal(consumptionDto.getFinalPrice() - encounterMasterDto.getDiscountAmount()));
							}
							
						}else{
							orderDetailsMasterDto.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()));
							orderDetailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
							orderDetailsMasterDto.setOrdDiscount(new BigDecimal(0.00));
						}
						
						orderDetailsMasterDto.setTaxPer(BigDecimal.valueOf(consumptionDto.getTaxPercentage()));
						orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
						orderDetailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
						orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
						orderDetailsMasterDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
						orderDetailsMasterDto.setCoPayPer(new BigDecimal(consumptionDto.getCoShare()));
						orderDetailsMasterDto.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
						orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
						orderDetailsMasterDto.setIsRefDoctorShare(consumptionDto.getIsRefReq());
						orderDetailsMasterDto.setIsDocReq(consumptionDto.getIsDocReq());
						if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
							orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
					}
				
				List orderDetailsList = new ArrayList();
				orderDetailsList.add(orderDetailsMasterDto);
				orderMasterDto.setListOrderDetailsMasterDto(orderDetailsList);
				
				if(turn ==2)
					consumptionDto.setContractId(null);
				
				if(consumptionDto.getContractId()!=null)
					return new Response<>(SUCCESS, SUCCESS_CODE, null, null, serviceId);
				
				
				Response orderRes = iOrderMasterService.saveOrderMaster(orderMasterDto);
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
				}
				
			
			} catch (Exception e) {
			logger.error("saveOrderDetails failed", e);
			}
		return new Response<>(ERROR, null, null, null, null);
		}

	@Override
	public Integer saveIPConsultationOrder(EncounterMasterDto encounterMasterDto, Integer serviceId) {
		try {
			Integer companyOrSelf = 0; // self=1 contract=0
			BigDecimal serviceAmt = BigDecimal.ZERO;
			BigDecimal taxAmt = BigDecimal.ZERO;
			BigDecimal netAmt = BigDecimal.ZERO;
			
			Long newDate = TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()).getTime();
			
			OrderMasterDto orderMasterDto = new OrderMasterDto();
			orderMasterDto.setVisitTypeId(IP);// IP==2
			orderMasterDto.setAdmissionId(encounterMasterDto.getAdmissionId());
			orderMasterDto.setPatientId(encounterMasterDto.getPatientId());
			orderMasterDto.setCreatedBy(encounterMasterDto.getCreatedBy());
			orderMasterDto.setUpdatedBy(encounterMasterDto.getUpdatedBy());
			orderMasterDto.setCreatedDate(newDate);
			orderMasterDto.setUpdatedDate(newDate);
			orderMasterDto.setOrderDate(newDate);
			orderMasterDto.setOrderStatus('A');
			orderMasterDto.setOrgId(encounterMasterDto.getOrganizationId());
			orderMasterDto.setOrgUnitId(encounterMasterDto.getUnitId());
			orderMasterDto.setOrderStatusId(1);// order status==1

			// -------order details-------//
			OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
				orderDetailsMasterDto.setServiceId(serviceId);
				orderDetailsMasterDto.setServiceIsBilled(0);
				orderDetailsMasterDto.setOrderQty(1);
				orderDetailsMasterDto.setCreatedBy(encounterMasterDto.getCreatedBy());
				orderDetailsMasterDto.setUpdatedBy(encounterMasterDto.getUpdatedBy());
				orderDetailsMasterDto.setOrderDate(newDate);
				orderDetailsMasterDto.setOrderSchDate(newDate);
				orderDetailsMasterDto.setOrdRenderDatetime(newDate);
				orderDetailsMasterDto.setCreatedDate(newDate);
				orderDetailsMasterDto.setUpdatedDate(newDate);
				orderDetailsMasterDto.setStatus('A');
				orderDetailsMasterDto.setOrgId(encounterMasterDto.getOrganizationId());
				orderDetailsMasterDto.setOrgUnitId(encounterMasterDto.getUnitId());
				orderDetailsMasterDto.setOrdDocId(encounterMasterDto.getDoctorId());
				orderDetailsMasterDto.setOrdDocSplId(encounterMasterDto.getSpecialityId());
				orderDetailsMasterDto.setServiceRenderingDeptId(encounterMasterDto.getConsultationSpecialityId());
				orderDetailsMasterDto.setOrdCancelled('N');

			ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
				consumptionDto.setOrganizationId(encounterMasterDto.getOrganizationId());
				consumptionDto.setUnitId(encounterMasterDto.getUnitId());
				consumptionDto.setVisitTypeId(IP);
				consumptionDto.setPatientTypeId(encounterMasterDto.getPatientCategoryId());
				consumptionDto.setBillingBedCategoryId(encounterMasterDto.getBillingBedCategoryId());
				consumptionDto.setPaymentEntitlementId(encounterMasterDto.getPaymentEntitlementId());
				consumptionDto.setServiceId(serviceId);
				//consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDateFromLongDate(newDate, "yyyy-MM-dd"));
				consumptionDto.setContractId(encounterMasterDto.getContractId());
				consumptionDto.setAdmissionId(encounterMasterDto.getAdmissionId());
				consumptionDto.setDefaultSelfTariffId(encounterMasterDto.getDefaultSelfTariffId());
				consumptionDto.setSelfPayeeId(encounterMasterDto.getPayeeId());
				consumptionDto.setPatientId(encounterMasterDto.getPatientId());
			
			Response res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
			consumptionDto = (ContractConsumptionDto) res.getObject();

			if (res.getObject() != null) 
			{
				orderDetailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
				orderDetailsMasterDto.setDummyServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
				orderDetailsMasterDto.setOrdConcession(new BigDecimal(consumptionDto.getConcession()));
				orderDetailsMasterDto.setPatientClassPercentage(new BigDecimal(consumptionDto.getPatientClassPercentage()));
				orderDetailsMasterDto.setOrdDiscount(new BigDecimal(0.00));
				orderDetailsMasterDto.setTariffId(consumptionDto.getTariffId());
				orderDetailsMasterDto.setBillingClassId(1);// By default its OPD ---> Id==1
				orderDetailsMasterDto.setOrderEmergencyFlag('N');
				orderDetailsMasterDto.setServiceRendered(1);
				orderDetailsMasterDto.setOrdRenderDatetime(newDate);
				orderDetailsMasterDto.setServiceChargeable(1);
				orderDetailsMasterDto.setServiceIsBilled(0);
				orderDetailsMasterDto.setIsDrug('N');
				orderDetailsMasterDto.setOrdTotalAmt(new BigDecimal(consumptionDto.getServiceRate()));
				orderDetailsMasterDto.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()));
				orderDetailsMasterDto.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()));
				orderDetailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
				orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
				orderDetailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
				orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
				orderDetailsMasterDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
				orderDetailsMasterDto.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
				if (consumptionDto.getPayeeId() == encounterMasterDto.getPayeeId()&& consumptionDto.getContractId() == null) 
				{
					List<OrderDetailsPayeeMapperDto> list = new LinkedList<>();
					OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
						detailsPayeeMapperDto.setPayeeId(consumptionDto.getPayeeId());
						detailsPayeeMapperDto.setPayeeAmount(new BigDecimal(consumptionDto.getFinalPrice()));
						detailsPayeeMapperDto.setServiceId(consumptionDto.getServiceId());
					list.add(detailsPayeeMapperDto);
					orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(list);
				} else
					orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
				
				if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
					orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
			} else {
				return 0;
			}
			List orderDetailsList = new ArrayList();
			orderDetailsList.add(orderDetailsMasterDto);

			orderMasterDto.setListOrderDetailsMasterDto(orderDetailsList);
			iOrderMasterService.saveOrderMaster(orderMasterDto);
			return 1;
		} catch (Exception e) {
		logger.error("saveIPConsultationOrder failed", e);
		}
		return 0;
	}

	@Override
	public Response updateAutorenderServices(EncounterMasterDto encounterMasterDto, Integer turn) {

		try {
			// --------get Auto-Rendered Services---------//
			Integer companyOrSelf = 0; // self=1 contract=1
			OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
			encounterMasterDto.setVisitTypeId(OP);
			List<ServiceMasterDto> listServiceMasterDto = iEncounterMasterDao.getAutoRenderedServicesByPatient(encounterMasterDto).getListObject();
			Long newDate = TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()).getTime();
			if (listServiceMasterDto.size() > 0) {
				ListIterator<ServiceMasterDto> iterator = listServiceMasterDto.listIterator();

				while (iterator.hasNext()) {
					ServiceMasterDto masterDto = iterator.next();
					BigDecimal serviceAmt = BigDecimal.ZERO;
					BigDecimal taxAmt = BigDecimal.ZERO;
					BigDecimal netAmt = BigDecimal.ZERO;
					ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
						consumptionDto.setOrganizationId(encounterMasterDto.getOrganizationId());
						consumptionDto.setUnitId(encounterMasterDto.getUnitId());
						consumptionDto.setVisitTypeId(OP);
						consumptionDto.setPatientTypeId(encounterMasterDto.getPatientCategoryId());
						consumptionDto.setBillingBedCategoryId(encounterMasterDto.getBillingBedCategoryId());
						consumptionDto.setPaymentEntitlementId(SELF);
						consumptionDto.setServiceId(masterDto.getServiceMasterId());
						consumptionDto.setCurrentDate(encounterMasterDto.getCurrentDate());
						consumptionDto.setContractId(encounterMasterDto.getContractId());
						consumptionDto.setEncounterId(encounterMasterDto.getEncounterId());
						consumptionDto.setDefaultSelfTariffId(encounterMasterDto.getDefaultSelfTariffId());
						consumptionDto.setSelfPayeeId(encounterMasterDto.getPayeeId());
						consumptionDto.setPatientId(encounterMasterDto.getPatientId());
						consumptionDto.setPatientClassId(encounterMasterDto.getPatientClassId());
						consumptionDto.setIsFromEncounter(1);
					Response res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
					consumptionDto = (ContractConsumptionDto) res.getObject();

					if (res.getObject() != null) 
					{
						if (masterDto.getRenderingDepartmentId() == null)
							orderDetailsMasterDto
									.setServiceRenderingDeptId(encounterMasterDto.getConsultationSpecialityId());
						else
							orderDetailsMasterDto.setServiceRenderingDeptId(masterDto.getRenderingDepartmentId());
						
						if(masterDto.getIsDocReq()!=null && masterDto.getIsDocReq().equals('Y'))
						{
							orderDetailsMasterDto.setOrdDocId(encounterMasterDto.getDoctorId());
							orderDetailsMasterDto.setOrdDocSplId(encounterMasterDto.getDeptId());
						}
							
						orderDetailsMasterDto.setVisitTypeId(OP);
						orderDetailsMasterDto.setOrderId(masterDto.getOrderId());
						orderDetailsMasterDto.setEncounterId(encounterMasterDto.getEncounterId());
						orderDetailsMasterDto.setOrderDetailsId(masterDto.getOrderDetailsId());
						orderDetailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
						orderDetailsMasterDto.setDummyServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
						orderDetailsMasterDto.setOrdConcession(new BigDecimal(consumptionDto.getConcession()));
						orderDetailsMasterDto.setPatientClassPercentage(new BigDecimal(consumptionDto.getPatientClassPercentage()));
						orderDetailsMasterDto.setOrdDiscount(new BigDecimal(0.00));
						orderDetailsMasterDto.setTariffId(encounterMasterDto.getDefaultSelfTariffId());
						orderDetailsMasterDto.setBillingClassId(1);// By default its OPD ---> Id==1
						orderDetailsMasterDto.setOrderEmergencyFlag('N');
						orderDetailsMasterDto.setServiceRendered(1);
						orderDetailsMasterDto.setOrdRenderDatetime(newDate);
						orderDetailsMasterDto.setServiceChargeable(1);
						orderDetailsMasterDto.setServiceIsBilled(0);
						orderDetailsMasterDto.setIsDrug('N');
						orderDetailsMasterDto.setPriorityId(2);
						orderDetailsMasterDto.setIsOutsourced('N');
					    orderDetailsMasterDto.setTaxPer(new BigDecimal(consumptionDto.getTaxPercentage()));
						orderDetailsMasterDto.setOrdTotalAmt(new BigDecimal(consumptionDto.getServiceRate()));
						orderDetailsMasterDto.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()));
						orderDetailsMasterDto.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()));
						orderDetailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
						orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
						orderDetailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
						orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
						orderDetailsMasterDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
						orderDetailsMasterDto.setCoPayPer(new BigDecimal(consumptionDto.getCoShare()));
						orderDetailsMasterDto.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
						if (consumptionDto.getPayeeId() == encounterMasterDto.getPayeeId()
								&& consumptionDto.getContractId() == null) 
						{
							List<OrderDetailsPayeeMapperDto> list = new LinkedList<>();
							OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
							detailsPayeeMapperDto.setPayeeId(consumptionDto.getPayeeId());
							detailsPayeeMapperDto.setPayeeAmount(new BigDecimal(consumptionDto.getFinalPrice()));
							detailsPayeeMapperDto.setServiceId(consumptionDto.getServiceId());
							list.add(detailsPayeeMapperDto);
							orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(list);
						} else
							orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
						if (turn == 2)
							consumptionDto.setContractId(null);

						
						if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
							orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
						
						if (consumptionDto.getContractId() == null)
							iEncounterMasterDao.updateAutorenderServices(orderDetailsMasterDto);
						else
							return new Response<>(SUCCESS, SUCCESS_CODE, null, null, encounterMasterDto);

					}
				}
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
		logger.error("updateAutorenderServices failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response updateFutureOrderedServices(EncounterMasterDto encounterMasterDto) {
		try {
			Integer companyOrSelf = 0; // self=1 contract=1
			Long newDate = TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()).getTime();
			encounterMasterDto.setVisitTypeId(encounterMasterDto.getVisitTypeId());
			List<ServiceMasterDto> listServiceMasterDto = encounterMasterDto.getListServiceMasterDto();
			if (listServiceMasterDto.size() > 0) {
				ListIterator<ServiceMasterDto> iterator = listServiceMasterDto.listIterator();

				while (iterator.hasNext()) 
				{
					ServiceMasterDto masterDto = iterator.next();
					OrderDetailsMaster orderDetailsMaster = iOrderDetailsMasterDao.getOrderDetailsByIdForPayeeUpdate(masterDto.getOrderDetailsId());
					OrderDetailsMasterDto orderDetailsMasterDto = mapper.map(orderDetailsMaster, OrderDetailsMasterDto.class, "OrderDetailsMaster_to_OrderDetailsMasterDto");
					orderDetailsMasterDto.setIsCapService(orderDetailsMasterDto.getIsCapService()=='\u0000'?'N':orderDetailsMasterDto.getIsCapService());
					//orderDetailsMaster.setIsPackageService(orderDetailsMaster.getIsPackageService()=='\u0000'?'N':orderDetailsMaster.getIsPackageService());
					orderDetailsMasterDto.setIsProcedure(orderDetailsMasterDto.getIsProcedure()=='\u0000'?'N':orderDetailsMasterDto.getIsProcedure());
					orderDetailsMasterDto.setIsProcedureService(orderDetailsMasterDto.getIsProcedureService()=='\u0000'?'N':orderDetailsMasterDto.getIsProcedureService());
					ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
						consumptionDto.setOrganizationId(encounterMasterDto.getOrganizationId());
						consumptionDto.setUnitId(encounterMasterDto.getUnitId());
						consumptionDto.setVisitTypeId(encounterMasterDto.getVisitTypeId());
						consumptionDto.setPatientTypeId(encounterMasterDto.getPatientCategoryId());
						consumptionDto.setBillingBedCategoryId(encounterMasterDto.getBillingBedCategoryId());
						consumptionDto.setPaymentEntitlementId(SELF);
						consumptionDto.setServiceId(masterDto.getServiceMasterId());
						consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()), "yyyy-MM-dd"));
						consumptionDto.setContractId(encounterMasterDto.getContractId());
						consumptionDto.setEncounterId(encounterMasterDto.getEncounterId());
						consumptionDto.setAdmissionId(encounterMasterDto.getAdmissionId());
						consumptionDto.setDefaultSelfTariffId(encounterMasterDto.getDefaultSelfTariffId());
						consumptionDto.setSelfPayeeId(encounterMasterDto.getPayeeId());
						consumptionDto.setPatientId(encounterMasterDto.getPatientId());
						
						Response res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
						consumptionDto = (ContractConsumptionDto) res.getObject();

						if (res.getObject() != null) 
						{
							if (masterDto.getRenderingDepartmentId() == null)
								orderDetailsMasterDto
										.setServiceRenderingDeptId(encounterMasterDto.getConsultationSpecialityId());
							else
								orderDetailsMasterDto.setServiceRenderingDeptId(masterDto.getRenderingDepartmentId());
							
							orderDetailsMasterDto.setVisitTypeId(encounterMasterDto.getVisitTypeId());
							orderDetailsMasterDto.setOrderId(masterDto.getOrderId());
							orderDetailsMasterDto.setEncounterId(encounterMasterDto.getEncounterId());
							orderDetailsMasterDto.setAdmissionId(encounterMasterDto.getAdmissionId());
							orderDetailsMasterDto.setOrderDetailsId(masterDto.getOrderDetailsId());
							orderDetailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
							orderDetailsMasterDto.setDummyServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
							orderDetailsMasterDto.setOrdConcession(new BigDecimal(consumptionDto.getConcession()));
							orderDetailsMasterDto.setOrdDiscount(new BigDecimal(0.00));
							orderDetailsMasterDto.setTariffId(encounterMasterDto.getDefaultSelfTariffId());
							orderDetailsMasterDto.setBillingClassId(1);// By default its OPD ---> Id==1
							orderDetailsMasterDto.setOrderEmergencyFlag('N');
							//orderDetailsMasterDto.setOrdRenderDatetime(encounterMasterDto.getCreatedDate());
							orderDetailsMasterDto.setServiceChargeable(1);
							if(encounterMasterDto.getDischargeStatusId()==null || encounterMasterDto.getDischargeStatusId()!=3)
							{
						    orderDetailsMasterDto.setServiceRendered(0);
							orderDetailsMasterDto.setServiceIsBilled(0);
							}
							orderDetailsMasterDto.setIsDrug('N');
							orderDetailsMasterDto.setPriorityId(2);
							orderDetailsMasterDto.setIsOutsourced('N');
							orderDetailsMasterDto.setOrdTotalAmt(new BigDecimal(consumptionDto.getServiceRate()));
							orderDetailsMasterDto.setTaxId(consumptionDto.getTaxId());
							orderDetailsMasterDto.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()));
							orderDetailsMasterDto.setTaxPer(new BigDecimal(consumptionDto.getTaxPercentage()));
							orderDetailsMasterDto.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()));
							orderDetailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
							orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
							orderDetailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
							orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
							orderDetailsMasterDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
							orderDetailsMasterDto.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
							orderDetailsMasterDto.setOrdDocId(encounterMasterDto.getDoctorId());
							orderDetailsMasterDto.setOrdDocSplId(encounterMasterDto.getSpecialityId()!=null?encounterMasterDto.getSpecialityId():null);
							orderDetailsMasterDto.setCoPayPer(new BigDecimal(consumptionDto.getCoShare()));
							orderDetailsMasterDto.setPatientClassPercentage(new BigDecimal(consumptionDto.getPatientClassPercentage()));
							orderDetailsMasterDto.setDepartmentId(consumptionDto.getSpecialityId());
							orderDetailsMasterDto.setBedId(encounterMasterDto.getBedId());
							orderDetailsMasterDto.setWardId(encounterMasterDto.getWardId());
							orderDetailsMasterDto.setRoomId(encounterMasterDto.getRoomId());
							orderDetailsMasterDto.setOrderQty(1);
							//<----Referal & Doctor Share logic
							Double serviceAmt = (orderDetailsMasterDto.getOrdTotalAmt().subtract(orderDetailsMasterDto.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession()))).doubleValue();
							orderDetailsMasterDto.setDiscountOwnership(orderDetailsMasterDto.getDiscountOwnership()!=null ? orderDetailsMasterDto.getDiscountOwnership() : 0);
							
							if(serviceAmt >0 && orderDetailsMasterDto.getOrdDocId()!=null && orderDetailsMasterDto.getOrdDocId()>0 && consumptionDto.getIsDocReq()!=null && consumptionDto.getIsDocReq().equals('Y'))
								orderDetailsMasterDto.setDocSharePer(new BigDecimal(iOrderMasterService.getDoctorSharePercentage(orderDetailsMasterDto,encounterMasterDto.getVisitTypeId(),encounterMasterDto.getBillingBedCategoryId(),encounterMasterDto.getPatientCategoryId(),encounterMasterDto.getPaymentEntitlementId())));
							else
								orderDetailsMasterDto.setDocSharePer(null);
							
							if(serviceAmt >0 && encounterMasterDto.getReferralId()!=null && consumptionDto.getIsRefReq()!=null && consumptionDto.getIsRefReq().equals('Y'))
								orderDetailsMasterDto.setRefDocSharePer(new BigDecimal(iOrderMasterService.getDoctorReferralPercentage(orderDetailsMasterDto,encounterMasterDto.getReferralId(),encounterMasterDto.getVisitTypeId(),encounterMasterDto.getBillingBedCategoryId(),encounterMasterDto.getPatientCategoryId(),encounterMasterDto.getPaymentEntitlementId())));
							else
								orderDetailsMasterDto.setRefDocSharePer(null);
							
							if(serviceAmt >0 && orderDetailsMasterDto.getDocSharePer()!=null && (orderDetailsMasterDto.getDiscountOwnership()!=null && orderDetailsMasterDto.getDiscountOwnership()!=0) && (orderDetailsMasterDto.getOrdDiscount()!=null || orderDetailsMasterDto.getOrdConcession()!=null))
							{
								if(orderDetailsMasterDto.getDiscountOwnership()==1) //Discount owned by doctor
								{
									orderDetailsMasterDto.setDocSharePer(orderDetailsMasterDto.getDocSharePer().subtract(orderDetailsMasterDto.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession())));
								}
							}
							if(serviceAmt >0 && orderDetailsMasterDto.getRefDocSharePer()!=null && orderDetailsMasterDto.getRefDocSharePer().doubleValue()>0 && (orderDetailsMasterDto.getDiscountOwnership()!=null && orderDetailsMasterDto.getDiscountOwnership()!=0) && (orderDetailsMasterDto.getOrdDiscount()!=null || orderDetailsMasterDto.getOrdConcession()!=null))
							{
								if(orderDetailsMasterDto.getDiscountOwnership()==3) //Discount owned by referral
								{
									orderDetailsMasterDto.setRefDocSharePer(orderDetailsMasterDto.getRefDocSharePer().subtract(orderDetailsMasterDto.getOrdDiscount().add(orderDetailsMasterDto.getOrdConcession())));
								}
							}
							
							if(serviceAmt >0 && orderDetailsMasterDto.getDocSharePer()!=null && orderDetailsMasterDto.getDocSharePer().doubleValue() > 0 && orderDetailsMasterDto.getRefDocSharePer()!=null && orderDetailsMasterDto.getRefDocSharePer().doubleValue() >0)
							{
								Double totalShare = orderDetailsMasterDto.getDocSharePer().doubleValue() +  orderDetailsMasterDto.getRefDocSharePer().doubleValue();
								
								if(orderDetailsMasterDto.getDocSharePer().doubleValue()==serviceAmt)
								{
									orderDetailsMasterDto.setRefDocSharePer(BigDecimal.ZERO);
								}else if(totalShare > serviceAmt)
								{
									orderDetailsMasterDto.setRefDocSharePer(new BigDecimal(serviceAmt-orderDetailsMasterDto.getDocSharePer().doubleValue()));
								}
							}
							orderDetailsMasterDto.setHospitalShare(new BigDecimal(serviceAmt-((orderDetailsMasterDto.getRefDocSharePer()!=null ? orderDetailsMasterDto.getRefDocSharePer().doubleValue() : 0.00)+(orderDetailsMasterDto.getDocSharePer()!=null ? orderDetailsMasterDto.getDocSharePer().doubleValue() : 0.00))));
							//Referal & Doctor Share logic---->
							
							
							if (consumptionDto.getPayeeId() == encounterMasterDto.getPayeeId()
									&& consumptionDto.getContractId() == null) 
							{
								List<OrderDetailsPayeeMapperDto> list = new LinkedList<>();
								OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
								detailsPayeeMapperDto.setPayeeId(consumptionDto.getPayeeId());
								detailsPayeeMapperDto.setPayeeAmount(new BigDecimal(consumptionDto.getFinalPrice()));
								detailsPayeeMapperDto.setServiceId(consumptionDto.getServiceId());
								list.add(detailsPayeeMapperDto);
								orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(list);
							} else
								orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(
										consumptionDto.getOrderDetailsPayeeMapperDtosList());
							
							if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
								orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());

							iEncounterMasterDao.updateAutorenderServices(orderDetailsMasterDto);
						}

					
				}
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getEncounterDetails(EncounterMasterDto encounterMasterDto) {
		try {
			return iEncounterMasterDao.getEncounterDetails(encounterMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getListOfEncounterMasterDao(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.getListOfEncounterMasterDao(slotMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getSearchedListEncounterMaster(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.getSearchedListEncounterMaster(slotMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getListOfAppointmentStatus(AppointmentStatusMasterDto appointmentStatusMasterDto)
			throws ApplicationException {
		try {
			return iEncounterMasterDao.getListOfAppointmentStatus(appointmentStatusMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getListOfEncountersByPatientId(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.getListOfEncountersByPatientId(encounterMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

		
	}

	@Override
	// @Transactional
	public Response updateEncounterStatus(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.updateEncounterStatus(encounterMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

		
	}

	@Override
	@Transactional
	public Response saveDocumentFromMylife(PatientMasterDto patientMasterDto) throws ApplicationException {
		try {

			Response res = iPatientRegistrationDao.getPatientDetailsByuuid(patientMasterDto.getUuid());

			PatientRegistrationDto patRegdto = (PatientRegistrationDto) res.getObject();
			EncounterDocDetailsDto encounterDocDetailsDto = new EncounterDocDetailsDto();
			encounterDocDetailsDto.setDocumentTypeId(patientMasterDto.getDocumentTypeId());
			encounterDocDetailsDto.setPatientId(patRegdto.getPatientId());
			encounterDocDetailsDto.setDocumentPath(patientMasterDto.getDocumentPath());
			encounterDocDetailsDto.setStatus('A');
			encounterDocDetailsDto.setOrganisationId(patRegdto.getOrganizationId());
			encounterDocDetailsDto.setUnitId(patRegdto.getUnitId());

			encounterDocDetailsDto.setCreatedBy(0);

			DateTimeFormatter formatter =
					DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

			String currentDateTime =
					LocalDateTime.now().format(formatter);
			if(encounterDocDetailsDto.getCreatedDate()==null) {
				encounterDocDetailsDto.setCreatedDate(currentDateTime);
			}
			encounterDocDetailsDto.setUploadDoccumentDate(currentDateTime);

			encounterDocDetailsDto.setDocumentName("Health ATM test result");
			encounterDocDetailsDto.setFileName("Health ATM test result");
			encounterDocDetailsDto.setDocumentTypeId(70);

			return iEncounterMasterDao.saveDocumentAgainstEncounter(encounterDocDetailsDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response saveDocumentAgainstEncounter(EncounterDocDetailsDto encounterDocDetailsDto)
			throws ApplicationException {
		try {
			Date date = TimeZoneComponent.getDateByzone(encounterDocDetailsDto.getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			String docPath = "";
			if (encounterDocDetailsDto.getUnitId() > 0) 
			{
				Response orgRes = iGlobalOrganisationMasterDao.getOrganiazationFolderPath(encounterDocDetailsDto.getOrganisationId());
				OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();

				if (encounterDocDetailsDto.getDocumentTypeId() == 19) 
				{
					docPath = docUploadUtility.uploadCorporateDoc(encounterDocDetailsDto.getFile(),
							organizationMasterDto.getOrganizationFolder(), encounterDocDetailsDto);
				} else {
					docPath = docUploadUtility.uploadEncounterDoc(encounterDocDetailsDto.getFile(),
							organizationMasterDto.getOrganizationFolder(), encounterDocDetailsDto);
				}
				encounterDocDetailsDto.setDocumentPath(docPath);
				if(!encounterDocDetailsDto.getCreatedDate().equals("null") && !encounterDocDetailsDto.getCreatedDate().equals("")) {
					encounterDocDetailsDto.setCreatedDate(encounterDocDetailsDto.getCreatedDate());
				}else {
	                encounterDocDetailsDto.setCreatedDate(dateTime);
				}
				
				encounterDocDetailsDto.setUpdatedDate(dateTime);
				encounterDocDetailsDto.setUploadDoccumentDate(encounterDocDetailsDto.getUploadDoccumentDate());
				iEncounterMasterDao.saveDocumentAgainstEncounter(encounterDocDetailsDto);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, docPath);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response saveDocumentAgainstEncounterInsuranceAuthorizationsDoc(
			EncounterDocDetailsDto encounterDocDetailsDto) throws ApplicationException {
		try {
			Date date = TimeZoneComponent.getDateByzone(encounterDocDetailsDto.getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");

			if (encounterDocDetailsDto.getUnitId() > 0 && !encounterDocDetailsDto.getFile().isEmpty()) {
				Response orgRes = iGlobalOrganisationMasterDao
						.getOrganiazationFolderPath(encounterDocDetailsDto.getOrganisationId());
				OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();
				String docPath = docUploadUtility.uploadInsuranceAuthorizationsDoc(encounterDocDetailsDto.getFile(),
						organizationMasterDto, encounterDocDetailsDto);
				encounterDocDetailsDto.setDocumentPath(docPath);
				encounterDocDetailsDto.setCreatedDate(dateTime);
				encounterDocDetailsDto.setUpdatedDate(dateTime);
				iEncounterMasterDao.saveDocumentAgainstEncounter(encounterDocDetailsDto);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public UnitServiceTariffMasterDto getBasePriceByServiceTariffMaster(
			UnitServiceTariffMasterDto unitServiceTariffMasterDto) throws ApplicationException {
		try {
			UnitServiceTariffMasterDto masterDto = new UnitServiceTariffMasterDto();
			UnitServiceTariffMasterDto lastObject = new UnitServiceTariffMasterDto();
			List<Long> dayCountList = new LinkedList<>();
			Date effectiveDate = null;
			Date currentDate = GlobalCommonDateUtils.getDate(unitServiceTariffMasterDto.getOrderDate(), "yyyy-M-dd");
			List<UnitServiceTariffMasterDto> list = iEncounterMasterDao
					.getBasePriceByServiceTariffMaster(unitServiceTariffMasterDto);
			ListIterator<UnitServiceTariffMasterDto> iterator = list.listIterator();
			while (iterator.hasNext()) {

				masterDto = iterator.next();

				effectiveDate = GlobalCommonDateUtils.getDate(masterDto.getEffectiveDate(), "yyyy-M-dd");
				// in milliseconds
				long diff = currentDate.getTime() - effectiveDate.getTime();
				Long dayDiffDay = diff / (24 * 60 * 60 * 1000);
				dayCountList.add(dayDiffDay);
			}
			if (dayCountList.size() > 0) {
				int minIndex = dayCountList.indexOf(Collections.min(dayCountList));
				lastObject = list.get(minIndex);
			}

			return lastObject;

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

		
	}

	@Override
	@Transactional
	public Response acceptRadiologyRequest(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException {
		try {

			List<Integer> orderDetailsId = new LinkedList<>();
			orderDetailsId.add(radiologyOrdersListDto.getOrderDetailsId());
			Response res = iOrderDetailsMasterService.renderAndPostRates(orderDetailsId);
			if(res.getStatus().equals(ERROR))
			{
				return res;
			}
			
			
			res = iEncounterMasterDao.acceptRadiologyRequest(radiologyOrdersListDto);
			Response res1 = iEncounterMasterDao.saveRadiologyMapper(radiologyOrdersListDto);
			if (res.getStatus().equals(SUCCESS)) {

			
				//for rendering packages
				if (radiologyOrdersListDto.getIsPACSRequired() == 'Y') {

					// get host and port number
//					Long currentDate=TimeZoneComponent.getDateByzone(unitId).getTime();
//					Response configRes=	iScheduleLogsDao.getConnectionStringForMyLife();

					/*
					 * MyLifeConnectionLinkDto dto =null; if(configRes.getObject()!=null){
					 * dto=(MyLifeConnectionLinkDto)configRes.getObject(); }
					 */
					Header header = new Header();
					header.setMsh17_countrycode(radiologyOrdersListDto.getCountryCode());
					header.setMsh3_sendingapplication("HIS");
					header.setMsh12_versionid("1");
					header.setMsh13_sequencenumber("HIS001");
					header.setMsh4_sendingfacility("MPSHAH");
					header.setMsh5_receivingapplicaiton("MPSHAH");
					header.setMsh6_receivingfacility("TRS");

					header.setMsh7_datetime(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()), "yyyyMMddHHmmss"));
					header.setMsh11_processingid(radiologyOrdersListDto.getOrderNo());
					header.setMsh12_versionid("1");

					PID patientDemographics = new PID();

					patientDemographics.setPid2_patientid(radiologyOrdersListDto.getPatientId().toString());
					ArrayList<String> identifier = new ArrayList<>();
					identifier.add((radiologyOrdersListDto.getIdentificationNumber() != null
							&& radiologyOrdersListDto.getIdentificationNumber() != "")
									? radiologyOrdersListDto.getIdentificationNumber()
									: "123456");
					patientDemographics.setPid3_patientidentifier(identifier);
					patientDemographics.setPid5_patientname(radiologyOrdersListDto.getPatientName());

					// get patient Details by patient ID
					Response patientResponse = iPatientRegistrationService
							.getPatientDetailsById(radiologyOrdersListDto.getPatientId());
					if (patientResponse.getObject() != null) {
						PatientDetailsDto patientDtoRes = new PatientDetailsDto();
						patientDtoRes = (PatientDetailsDto) patientResponse.getObject();
						//System.out.println("patient Name=" + radiologyOrdersListDto.getPatientName());
						header.setMsh17_countrycode(
								patientDtoRes.getCountryCode() != null ? patientDtoRes.getCountryCode() : "IND");

						patientDemographics.setPid10_race(
								patientDtoRes.getRaceName() != null ? patientDtoRes.getRaceName() : "Asia");
						patientDemographics.setPid11_patientaddress(
								patientDtoRes.getAddress() != null ? patientDtoRes.getAddress() : "");
						patientDemographics.setPid16_maritalstatus(
								patientDtoRes.getMaritalStatus() != null ? patientDtoRes.getMaritalStatus() : "");
						patientDemographics.setPid28_nationality(
								patientDtoRes.getNationalityName() != null ? patientDtoRes.getNationalityName()
										: "Indiain");
						patientDemographics.setPid12_countrycode(
								patientDtoRes.getCountryCode() != null ? patientDtoRes.getCountryCode() : "IND");

						if (patientDtoRes.getDobPacs() != null && patientDtoRes.getDobPacs() != "") {
//							Long dobLong=GlobalCommonDateUtils.getLongDateFromStrigDate(patientDtoRes.getDobPacs());
							patientDemographics.setPid7_dateofbirth(patientDtoRes.getDobPacs());
						}

						patientDemographics.setPid8_administrativesex(
								patientDtoRes.getGenderCode() != null ? patientDtoRes.getGenderCode() : "M");

					}

					PV1 patientVisit = new PV1();

					patientVisit.setPv1_2_patientclass(radiologyOrdersListDto.getPatientCategory() != null
							? radiologyOrdersListDto.getPatientCategory()
							: "National");
					patientVisit.setPv1_19_visitnumber(
							radiologyOrdersListDto.getVisitNumber() != null ? radiologyOrdersListDto.getVisitNumber()
									: "");
					patientVisit.setPv1_4_admissiontype(
							radiologyOrdersListDto.getVisitName() != null ? radiologyOrdersListDto.getVisitName()
									: "OP");

					ORC orderDetails = new ORC();
					orderDetails.setOrc_1_ordercontrol("NW"); // for new order it shold be "NW"
					orderDetails.setOrc_2_placerordernumber(radiologyOrdersListDto.getOrderNo());
					orderDetails.setOrc_5_orderstatus("Renderd");
					orderDetails.setOrc_9_datetimeoftransaction(
							GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()), "yyyyMMddHHmmss"));
//					orderDetails.setOrc_17_enteringorganization("Organization Name="+sessionService.getCookieObjectDto().getOrgNme()+"Unit Name="+sessionService.getCookieObjectDto().getUnitNme() );

					RoomModalityMasterDto roomModalityDto = new RoomModalityMasterDto();
					roomModalityDto.setModalityId(radiologyOrdersListDto.getModalityId());
					roomModalityDto.setOrganizationId(sessionService.getOrganizationId());
					roomModalityDto.setUnitId(sessionService.getUnitId());
					if (radiologyOrdersListDto.getModalityId() != null) {
						Response modalityRes = iRoomModalityMasterDao.getModalityDetailsByModalityId(roomModalityDto);
						if (modalityRes.getListObject() != null && modalityRes.getListObject().size() > 0) {
							roomModalityDto = (RoomModalityMasterDto) modalityRes.getListObject().get(0);
						}
					}

					OBR observation = new OBR();
					observation.setObr_2_placerordernumber(radiologyOrdersListDto.getOrderNo());
					observation.setObr_3_fillerordernumber(radiologyOrdersListDto.getOrderNo());
					observation.setObr_5_priority(radiologyOrdersListDto.getPriorityId().toString());
					observation.setObr_4_universalserviceidentifier(radiologyOrdersListDto.getAccessionNumber() != null
							? radiologyOrdersListDto.getAccessionNumber()
							: "-");
					observation.setObr_5_priority("2");
					observation.setObr_6_requesteddatetime(
							GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()), "yyyyMMddHHmmss"));
					observation.setObr_12_dangercode("Green");
//					u observation.setObr_24_diagnosticservicesectorid(roomModalityDto.getModalityDesc()==null ? "CT" : roomModalityDto.getModalityDesc() +"@"+roomModalityDto.getRoomName()==null ? "AECT" : roomModalityDto.getRoomName()); //it should be modalityName@BedName ex. CT@AECT
//					 observation.setObr_24_diagnosticservicesectorid(roomModalityDto.getModalityDesc()+"@"+roomModalityDto.getRoomName());
					observation.setObr_24_diagnosticservicesectorid("CT@AECT");
					RadOrder radOrder = new RadOrder();
					radOrder.setHeader(header);
					radOrder.setObservation(observation);
					radOrder.setOrderDetails(orderDetails);
					radOrder.setPatientDemographics(patientDemographics);
					radOrder.setPatientVisit(patientVisit);

					// generating json

					String json = radOrder.GetRadOrderJson(radOrder);
					//System.out.println("PACS Json=" + json);

					// validating json
					String status = radOrder.validateRadOrder(json);

					if (status.equals("true")) {
						// send json
						Client client = new Client();
//						 if(dto.getPacsHost()!=null && dto.getPacsPort()!=null){
						//System.out.println("length=" + json.length());
						// sendtowebroot
//							    String response= client.SendString(json, dto.getPacsHost(), Integer.parseInt(dto.getPacsPort()));
						String response = client.SendtoWebhook(json, "WebReceive");
						//System.out.println("Response =" + response);
						/*
						 * String response= client.SendtoWebhook(json, "WebReceive");
						 * //System.out.println("Response ="+response);
						 */
//						 }						

					} else {
						//System.out.println("do nothing");
					}
				} else {

				}

			}

//			return iEncounterMasterDao.acceptRadiologyRequest(radiologyOrdersListDto);
			return res;

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response retestRadiologyResultEntry(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException {
		try {

			List<Integer> orderDetailsId = new LinkedList<>();
			orderDetailsId.add(radiologyOrdersListDto.getOrderDetailsId());
			Response res = iEncounterMasterDao.retestRadiologyResultEntry(radiologyOrdersListDto);
			return res;

		} catch (Exception e) {
			throw e;
		}
	}


	@Override
	@Transactional
	public Response getEHCPatientsList(EncounterMasterDto encounterMasterDto, Character isPreOrPostBilling)
			throws ApplicationException {
		try {
			return iEncounterMasterDao.getEHCPatientsList(encounterMasterDto, isPreOrPostBilling);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getRadiologyListForSampleCollection(RadiologyOrdersListDto radiologyOrdersListDto)
			throws ApplicationException {
		try {
			return iEncounterMasterDao.getRadiologyListForSampleCollection(radiologyOrdersListDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getPatientDocDetails(PatientDocReqDto patientDocReqDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.getPatientDocDetails(patientDocReqDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getInsuranceDocByPatientIDDetails(PatientDocReqDto patientDocReqDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.getInsuranceDocByPatientIDDetails(patientDocReqDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getOpPatientListByDocId(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.getOpPatientListByDocId(encounterMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response getTokenList(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.getTokenList(encounterMasterDto);
		} catch (Exception e) {
		logger.error("getTokenList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response updatePatientCritical(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.updatePatientCritical(encounterMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response opPatientListByDocIdForCasualty(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.opPatientListByDocIdForCasualty(encounterMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response updateDoctorIdForEncouner(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.updateDoctorIdForEncouner(encounterMasterDto);
		} catch (Exception e) {
		logger.error("updateDoctorIdForEncouner failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response patientCalled(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.patientCalled(encounterMasterDto);
		} catch (Exception e) {
		logger.error("patientCalled failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorTokenList(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.getDoctorTokenList(encounterMasterDto);
		} catch (Exception e) {
		logger.error("getDoctorTokenList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNursingTokenList(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.getNursingTokenList(encounterMasterDto);
		} catch (Exception e) {
		logger.error("getNursingTokenList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response scheduleAppointmentFromKiosk(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {

			Response arrivalRes = iAppointmentSchedulingDao.markArrival(encounterMasterDto.getAppointmentId(),
					encounterMasterDto.getCurrentDate(), encounterMasterDto.getUnitId());
			if (arrivalRes.getStatus().equals(SUCCESS))
				encounterMasterDto.setUnitTokenNumber((Integer) arrivalRes.getObject());

			Response encRes = saveEncounterMaster(encounterMasterDto);

			return encRes;
		} catch (Exception e) {
		logger.error("scheduleAppointmentFromKiosk failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response billAmountForKiosk(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {

			Integer serviceId = null;
			Double serviceAmount = null;
			Integer visitType = 1;
			Integer patientRegServiceId = 1;

			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setOrganizationId(encounterMasterDto.getOrganizationId());
			unitApplicationConfigurationDto.setUnitId(encounterMasterDto.getUnitId());
			Response appConfigRes = iUnitApplicationBillingConfigurationDao
					.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);

			if (appConfigRes.getObject() != null)
				unitApplicationConfigurationDto = (UnitApplicationConfigurationDto) appConfigRes.getObject();

			if (unitApplicationConfigurationDto.getDefaultSelfTariffId() == null)
				return new Response<>(ERROR, COMMON_ERROR_CODE, "Default tariff is not defined !!", null, null);
			else
				encounterMasterDto.setDefaultSelfTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());

			if (unitApplicationConfigurationDto.getOpdBillingCatId() == null)
				return new Response<>(ERROR, COMMON_ERROR_CODE, "Opd Billing Category Id is not defined !!", null,
						null);
			else
				encounterMasterDto.setBillingBedCategoryId(unitApplicationConfigurationDto.getOpdBillingCatId());

			if (unitApplicationConfigurationDto.getSelfPayeeId() == null)
				return new Response<>(ERROR, COMMON_ERROR_CODE, "Self Payee is not defined !!", null, null);
			else
				encounterMasterDto.setPayeeId(unitApplicationConfigurationDto.getSelfPayeeId());

			if (encounterMasterDto.getPatientId() != null) 
			{
				SlotMasterDto dto = new SlotMasterDto();
					dto.setPatientId(encounterMasterDto.getPatientId());
					dto.setDoctorId(encounterMasterDto.getDoctorId());
					dto.setOrganizationId(encounterMasterDto.getOrganizationId());
					dto.setUnitId(encounterMasterDto.getUnitId());
					dto.setCurrentDate(encounterMasterDto.getCurrentDate());
					dto.setSpecialityId(encounterMasterDto.getSpecialityId());
				visitType = assignVisitTypeIdAccordingToConditions(dto);
			} else {
				visitType = 1; // primary visit for pre-registered patients.
			}

			DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto = new DoctorConsultationServiceMapperDto();
				doctorConsultationServiceMapperDto.setOrganizationId(encounterMasterDto.getOrganizationId());
				doctorConsultationServiceMapperDto.setUnitId(encounterMasterDto.getUnitId());
				doctorConsultationServiceMapperDto.setDoctorId(encounterMasterDto.getDoctorId());
				doctorConsultationServiceMapperDto.setSpecialityId(encounterMasterDto.getSpecialityId());
				doctorConsultationServiceMapperDto.setDeptWiseAppointment(encounterMasterDto.getDeptWiseAppointment());
				doctorConsultationServiceMapperDto.setPaymentEntitlementId(SELF);
			Response response = iDoctorConsultationServiceMapperService.getDoctorConsultationServiceBySpecialityAndDoctorId(doctorConsultationServiceMapperDto);
			if (response.getStatus().equals(SUCCESS) && response.getListObject().size() > 0) 
			{
				DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto2 = (DoctorConsultationServiceMapperDto) response
						.getListObject().get(0);
				if (visitType == 1) {
					serviceId = doctorConsultationServiceMapperDto2.getNewVisitServiceId();
					serviceAmount = doctorConsultationServiceMapperDto2.getNewVisitRate();
				}
				if (visitType == 2) {
					serviceId = doctorConsultationServiceMapperDto2.getFollowupVisitServiceId();
					serviceAmount = doctorConsultationServiceMapperDto2.getFollowupVisitRate();
				}
				if (visitType == 3) {
					serviceId = doctorConsultationServiceMapperDto2.getSecondaryVisitServiceId();
					serviceAmount = doctorConsultationServiceMapperDto2.getSecondaryVisitRate();
				}
				encounterMasterDto.setServiceId(serviceId);
				encounterMasterDto.setServiceAmount(serviceAmount);

				if (serviceId == null) {
					return new Response<>(ERROR, null, null, null, null);
				}

				ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
					consumptionDto = new ContractConsumptionDto();
					consumptionDto.setOrganizationId(encounterMasterDto.getOrganizationId());
					consumptionDto.setUnitId(encounterMasterDto.getUnitId());
					consumptionDto.setVisitTypeId(OP);
					if(encounterMasterDto.getPatientCategoryId()!=null) {
					 consumptionDto.setPatientTypeId(encounterMasterDto.getPatientCategoryId());
					}else {
						 Response patientCategoryRes=iPatientRegistrationDao.getPatientCategoryIdByPatientId(encounterMasterDto.getPatientId());
						 consumptionDto.setPatientTypeId((Integer)patientCategoryRes.getObject());
						 encounterMasterDto.setPatientCategoryId(consumptionDto.getPatientTypeId());
					}
					consumptionDto.setBillingBedCategoryId(encounterMasterDto.getBillingBedCategoryId());
					consumptionDto.setPaymentEntitlementId(SELF);
					consumptionDto.setServiceId(serviceId);
					consumptionDto.setCurrentDate(encounterMasterDto.getCurrentDate());
					consumptionDto.setContractId(encounterMasterDto.getContractId());
					consumptionDto.setEncounterId(encounterMasterDto.getEncounterId());
					consumptionDto.setDefaultSelfTariffId(encounterMasterDto.getDefaultSelfTariffId());
					consumptionDto.setSelfPayeeId(encounterMasterDto.getPayeeId());
					consumptionDto.setPatientClassId(encounterMasterDto.getPatientClassId());
				Response res = null;
				if (encounterMasterDto.getServiceAmount() != null) 
					consumptionDto.setConsultationAmount(encounterMasterDto.getServiceAmount());

				res = iContractConsumptionService.consumeServiceFromContract(consumptionDto);
				consumptionDto = (ContractConsumptionDto) res.getObject();
				
				consumptionDto.setSystemDefiedVisitType(visitType);
				consumptionDto.setServiceId(serviceId);
				consumptionDto.setPatientCategoryId(encounterMasterDto.getPatientCategoryId());
				Tax tax = iEncounterMasterDao.getTaxPercentageByServiceId(serviceId,consumptionDto.getUnitId());
				if (tax != null) {
					consumptionDto.setTaxAmt((consumptionDto.getServiceRate() * (tax.getTaxPercentage() / 100)));
					consumptionDto.setFinalPrice(consumptionDto.getServiceRate() + consumptionDto.getTaxAmt());
				} else {
					consumptionDto.setTaxAmt(0.00);
					consumptionDto.setFinalPrice(consumptionDto.getServiceRate());
				}

				return new Response<>(SUCCESS, null, null, null, consumptionDto);
			}

		} catch (Exception e) {
		logger.error("billAmountForKiosk failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	public Response getCheckedInPatientByDoctorId(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.getCheckedInPatientByDoctorId(encounterMasterDto);
		} catch (Exception e) {
		logger.error("getCheckedInPatientByDoctorId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateCallCount(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.updateCallCount(encounterMasterDto);
		} catch (Exception e) {
		logger.error("updateCallCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getListOfRadiologyTests(Integer admissionId, Integer orgId, Integer orgUnitId)
			throws ApplicationException {
		try {
			return iEncounterMasterDao.getListOfRadiologyTests(admissionId, orgId, orgUnitId);
		} catch (Exception e) {
		logger.error("getListOfRadiologyTests failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDoctorFromNursingWorkList(Integer encounterId, Integer oldDocId, Integer docId,
			Integer reasonId) throws ApplicationException {
		try {
			return iEncounterMasterDao.updateDoctorFromNursingWorkList(encounterId, oldDocId, docId, reasonId);
		} catch (Exception e) {
		logger.error("updateDoctorFromNursingWorkList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response saveOrderDetailsAfterEncounter(EncounterMasterDto encounterMasterDto, Integer serviceId,
			Integer turn) {
		return saveOrderDetails(encounterMasterDto, serviceId, 2);
	}

	@Override
	@Transactional
	public Response updateAutorenderServicesAfterEncounter(EncounterMasterDto encounterMasterDto, Integer turn) {
		return updateAutorenderServices(encounterMasterDto, 2);
	}

	@Override
	public Response getBillDetailsOnEncouter(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			
			if(encounterMasterDto.getPatientId()!=null && encounterMasterDto.getPatientId()>0)
			{
				List<OrderDetailsMasterDto> orderDetailsMasterDtosList = new LinkedList<OrderDetailsMasterDto>();
				Character isPrePostBilling = null;
				List<Integer> serviceList = new LinkedList<>();
				Response orderRes = null;
				encounterMasterDto.setVisitTypeId(OP);
				OrderMasterDto orderMasterDto = null;
				Double serviceAmount = null;
				UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
				unitApplicationConfigurationDto.setOrganizationId(encounterMasterDto.getOrganizationId());
				unitApplicationConfigurationDto.setUnitId(encounterMasterDto.getUnitId());
				Response appConfigRes = iUnitApplicationBillingConfigurationDao
						.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
				AppointmentMaster appointmentMaster = null;
				if (appConfigRes.getObject() != null) {
					UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto) appConfigRes
							.getObject();

					if (unitApplicationConfigurationDtos.getDefaultSelfTariffId() == null)
						return new Response<>(ERROR, COMMON_ERROR_CODE, "Default tariff is not defined !!", null, null);
					else
						encounterMasterDto
								.setDefaultSelfTariffId(unitApplicationConfigurationDtos.getDefaultSelfTariffId());

					if (unitApplicationConfigurationDtos.getOpdBillingCatId() == null)
						return new Response<>(ERROR, COMMON_ERROR_CODE, "Opd Billing Category Id is not defined !!", null,
								null);
					else
						encounterMasterDto.setBillingBedCategoryId(unitApplicationConfigurationDtos.getOpdBillingCatId());

					if (unitApplicationConfigurationDtos.getSelfPayeeId() == null)
						return new Response<>(ERROR, COMMON_ERROR_CODE, "Self Payee is not defined !!", null, null);
					else
						encounterMasterDto.setPayeeId(unitApplicationConfigurationDtos.getSelfPayeeId());

					if (unitApplicationConfigurationDtos.getConsultationSpecialityId() == null)
						return new Response<>(ERROR, COMMON_ERROR_CODE, "Self Consultation Speciality is not defined !!",
								null, null);
					else
						encounterMasterDto.setConsultationSpecialityId(
								unitApplicationConfigurationDtos.getConsultationSpecialityId());

					if (unitApplicationConfigurationDtos.getIsPreOrPostBilling() == null)
						return new Response<>(ERROR, COMMON_ERROR_CODE, "Pre billing post billing is not defined !!", null,
								null);
					else
						isPrePostBilling = unitApplicationConfigurationDtos.getIsPreOrPostBilling();

				} else {
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Application Configuration is yet to set !!", null,
							null);
				}

				Integer serviceId = null;
				String serviceName = null;
				String specialityName = null;
				Integer tokenNumber = null;

				if (encounterMasterDto.getUserDefinedVisitTypeId() != null
						&& encounterMasterDto.getUserDefinedVisitTypeId() != 4
						&& encounterMasterDto.getUserDefinedVisitTypeId() != 5 && encounterMasterDto.getIsCyclicAppointment()!=null && encounterMasterDto.getIsCyclicAppointment()=='N') 
				{
					DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto = new DoctorConsultationServiceMapperDto();
						doctorConsultationServiceMapperDto.setOrganizationId(encounterMasterDto.getOrganizationId());
						doctorConsultationServiceMapperDto.setUnitId(encounterMasterDto.getUnitId());
						doctorConsultationServiceMapperDto.setDoctorId(encounterMasterDto.getDoctorId());
						doctorConsultationServiceMapperDto.setSpecialityId(encounterMasterDto.getSpecialityId());
						doctorConsultationServiceMapperDto.setDeptWiseAppointment(encounterMasterDto.getDeptWiseAppointment());
						doctorConsultationServiceMapperDto.setPaymentEntitlementId(SELF);
					Response response = iDoctorConsultationServiceMapperService
							.getDoctorConsultationServiceBySpecialityAndDoctorId(doctorConsultationServiceMapperDto);
					if (response.getStatus().equals(SUCCESS) && response.getListObject().size() > 0) 
					{
						DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto2 = (DoctorConsultationServiceMapperDto) response.getListObject().get(0);
						if (encounterMasterDto.getUserDefinedVisitTypeId() == 1) 
						{
							serviceId = doctorConsultationServiceMapperDto2.getNewVisitServiceId();
							serviceName = doctorConsultationServiceMapperDto2.getNewVisitServiceName();
							specialityName = doctorConsultationServiceMapperDto2.getNewVisitSpecialityName();
							serviceAmount = doctorConsultationServiceMapperDto2.getNewVisitRate();
							encounterMasterDto.setIsDocShare(doctorConsultationServiceMapperDto2.getNewVisitDoctorShare());
						}
						if (encounterMasterDto.getUserDefinedVisitTypeId() == 2) {
							serviceId = doctorConsultationServiceMapperDto2.getFollowupVisitServiceId();
							serviceName = doctorConsultationServiceMapperDto2.getFollowupVisitServiceName();
							specialityName = doctorConsultationServiceMapperDto2.getFollowupVisitSpecialityName();
							serviceAmount = doctorConsultationServiceMapperDto2.getFollowupVisitRate();
							encounterMasterDto
									.setIsDocShare(doctorConsultationServiceMapperDto2.getFollowUpVisitDoctorShare());
						}
						if (encounterMasterDto.getUserDefinedVisitTypeId() == 3) {
							serviceId = doctorConsultationServiceMapperDto2.getSecondaryVisitServiceId();
							serviceName = doctorConsultationServiceMapperDto2.getSecondaryVisitServiceName();
							specialityName = doctorConsultationServiceMapperDto2.getSecondarySpecialityName();
							serviceAmount = doctorConsultationServiceMapperDto2.getSecondaryVisitRate();
							encounterMasterDto
									.setIsDocShare(doctorConsultationServiceMapperDto2.getSecondaryVisitDoctorShare());
						 }
						encounterMasterDto.setServiceId(serviceId);
						encounterMasterDto.setServiceAmount(serviceAmount);
						encounterMasterDto.setServiceDesc(serviceName);
						encounterMasterDto.setServiceSpecialityName(specialityName);
					}
				}
				if (serviceId != null)
					orderMasterDto = getOrderObject(encounterMasterDto, serviceId);

				List<ServiceMasterDto> listServiceMasterDto = iEncounterMasterDao
						.getAutoRenderedServicesByPatient(encounterMasterDto).getListObject();
				
				OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
				if (listServiceMasterDto.size() > 0) {
					ListIterator<ServiceMasterDto> iterator = listServiceMasterDto.listIterator();

					while (iterator.hasNext()) 
					{
						ServiceMasterDto masterDto = iterator.next();

						ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
						consumptionDto = new ContractConsumptionDto();
						consumptionDto.setOrganizationId(encounterMasterDto.getOrganizationId());
						consumptionDto.setUnitId(encounterMasterDto.getUnitId());
						consumptionDto.setVisitTypeId(OP);
						consumptionDto.setPatientTypeId(encounterMasterDto.getPatientCategoryId());
						consumptionDto.setBillingBedCategoryId(encounterMasterDto.getBillingBedCategoryId());
						consumptionDto.setPaymentEntitlementId(SELF);
						consumptionDto.setServiceId(masterDto.getServiceMasterId());
						consumptionDto.setCurrentDate(encounterMasterDto.getCurrentDate());
						consumptionDto.setContractId(encounterMasterDto.getContractId());
						consumptionDto.setEncounterId(encounterMasterDto.getEncounterId());
						consumptionDto.setDefaultSelfTariffId(encounterMasterDto.getDefaultSelfTariffId());
						consumptionDto.setSelfPayeeId(encounterMasterDto.getPayeeId());
						consumptionDto.setPatientId(encounterMasterDto.getPatientId());
						consumptionDto.setConsumeService(1);
						consumptionDto.setPatientClassId(encounterMasterDto.getPatientClassId());
						consumptionDto.setIsFromEncounter(1);
						Response res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
						consumptionDto = (ContractConsumptionDto) res.getObject();

						if (res.getObject() != null) {
							if (masterDto.getRenderingDepartmentId() == null)
								orderDetailsMasterDto
										.setServiceRenderingDeptId(encounterMasterDto.getConsultationSpecialityId());
							else
								orderDetailsMasterDto.setServiceRenderingDeptId(masterDto.getRenderingDepartmentId());
							orderDetailsMasterDto.setVisitTypeId(OP);
							orderDetailsMasterDto.setOrderId(masterDto.getOrderId());
							orderDetailsMasterDto.setEncounterId(encounterMasterDto.getEncounterId());
							orderDetailsMasterDto.setOrderDetailsId(masterDto.getOrderDetailsId());
							orderDetailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
							orderDetailsMasterDto.setDummyServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
							orderDetailsMasterDto.setOrdConcession(new BigDecimal(consumptionDto.getConcession()));
							orderDetailsMasterDto.setOrdDiscount(new BigDecimal(0.00));
							orderDetailsMasterDto.setTariffId(consumptionDto.getTariffId());
							orderDetailsMasterDto.setBillingClassId(1);// By default its OPD ---> Id==1
							orderDetailsMasterDto.setOrderEmergencyFlag('N');
							orderDetailsMasterDto.setServiceRendered(1);
							orderDetailsMasterDto.setOrdRenderDatetime(encounterMasterDto.getCreatedDate());
							orderDetailsMasterDto.setServiceChargeable(1);
							orderDetailsMasterDto.setServiceIsBilled(0);
							orderDetailsMasterDto.setIsDrug('N');
							orderDetailsMasterDto.setPriorityId(2);
							orderDetailsMasterDto.setIsOutsourced('N');
							orderDetailsMasterDto.setSpecialityName(consumptionDto.getSpecialityName());
							orderDetailsMasterDto.setServiceName(consumptionDto.getServiceName());
							orderDetailsMasterDto.setPatientClassPercentage(new BigDecimal(consumptionDto.getPatientClassPercentage()));
							orderDetailsMasterDto.setOrdTotalAmt(new BigDecimal(consumptionDto.getServiceRate()));
							orderDetailsMasterDto.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()));
							orderDetailsMasterDto.setNetAmount((new BigDecimal(consumptionDto.getFinalPrice())));
							orderDetailsMasterDto.setSelfPayable((new BigDecimal(consumptionDto.getSelfPayable())));
							orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
							orderDetailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
							orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
							orderDetailsMasterDto.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
							if (orderDetailsMasterDto.getNetAmount().doubleValue() > 0.00) 
							{
								orderMasterDto.getListOrderDetailsMasterDto().add(orderDetailsMasterDto);
								if (consumptionDto.getPayeeId() == encounterMasterDto.getPayeeId()
										&& consumptionDto.getContractId() == null) {
									List<OrderDetailsPayeeMapperDto> list = new LinkedList<>();
									OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
									detailsPayeeMapperDto.setPayeeId(consumptionDto.getPayeeId());
									detailsPayeeMapperDto.setPayeeAmount(new BigDecimal(consumptionDto.getFinalPrice()));
									detailsPayeeMapperDto.setServiceId(consumptionDto.getServiceId());
									list.add(detailsPayeeMapperDto);
									orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(list);
								} else
									orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(
											consumptionDto.getOrderDetailsPayeeMapperDtosList());
							}
							if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
								orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
						}
					}
				}
				return new Response<>(SUCCESS, null, null, null, orderMasterDto);
			}else{
				
				List<OrderDetailsMasterDto> orderDetailsMasterDtosList = new LinkedList<OrderDetailsMasterDto>();
				Character isPrePostBilling = null;
				List<Integer> serviceList = new LinkedList<>();
				Response orderRes = null;
				encounterMasterDto.setVisitTypeId(OP);
				OrderMasterDto orderMasterDto = null;
				Double serviceAmount = null;
				Integer patientRegistartionService = null;
				UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
				unitApplicationConfigurationDto.setOrganizationId(encounterMasterDto.getOrganizationId());
				unitApplicationConfigurationDto.setUnitId(encounterMasterDto.getUnitId());
				Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
				AppointmentMaster appointmentMaster = null;
				if (appConfigRes.getObject() != null) {
					UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto) appConfigRes
							.getObject();

					if (unitApplicationConfigurationDtos.getDefaultSelfTariffId() == null)
						return new Response<>(ERROR, COMMON_ERROR_CODE, "Default tariff is not defined !!", null, null);
					else
						encounterMasterDto
								.setDefaultSelfTariffId(unitApplicationConfigurationDtos.getDefaultSelfTariffId());

					if (unitApplicationConfigurationDtos.getOpdBillingCatId() == null)
						return new Response<>(ERROR, COMMON_ERROR_CODE, "Opd Billing Category Id is not defined !!", null,
								null);
					else
						encounterMasterDto.setBillingBedCategoryId(unitApplicationConfigurationDtos.getOpdBillingCatId());

					if (unitApplicationConfigurationDtos.getSelfPayeeId() == null)
						return new Response<>(ERROR, COMMON_ERROR_CODE, "Self Payee is not defined !!", null, null);
					else
						encounterMasterDto.setPayeeId(unitApplicationConfigurationDtos.getSelfPayeeId());

					if (unitApplicationConfigurationDtos.getConsultationSpecialityId() == null)
						return new Response<>(ERROR, COMMON_ERROR_CODE, "Self Consultation Speciality is not defined !!",
								null, null);
					else
						encounterMasterDto.setConsultationSpecialityId(
								unitApplicationConfigurationDtos.getConsultationSpecialityId());

					if (unitApplicationConfigurationDtos.getIsPreOrPostBilling() == null)
						return new Response<>(ERROR, COMMON_ERROR_CODE, "Pre billing post billing is not defined !!", null,
								null);
					else
						isPrePostBilling = unitApplicationConfigurationDtos.getIsPreOrPostBilling();

					patientRegistartionService = unitApplicationConfigurationDtos.getPatientRegSerId();
					
				} else {
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Application Configuration is yet to set !!", null,
							null);
				}

				Integer serviceId = null;
				String serviceName = null;
				String specialityName = null;
				Integer tokenNumber = null;

				if (encounterMasterDto.getUserDefinedVisitTypeId() != null
						&& encounterMasterDto.getUserDefinedVisitTypeId() != 4
						&& encounterMasterDto.getUserDefinedVisitTypeId() != 5 && encounterMasterDto.getIsCyclicAppointment()!=null && encounterMasterDto.getIsCyclicAppointment()=='N') 
				{
					DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto = new DoctorConsultationServiceMapperDto();
					doctorConsultationServiceMapperDto.setOrganizationId(encounterMasterDto.getOrganizationId());
					doctorConsultationServiceMapperDto.setUnitId(encounterMasterDto.getUnitId());
					doctorConsultationServiceMapperDto.setDoctorId(encounterMasterDto.getDoctorId());
					doctorConsultationServiceMapperDto.setSpecialityId(encounterMasterDto.getSpecialityId());
					doctorConsultationServiceMapperDto.setDeptWiseAppointment(encounterMasterDto.getDeptWiseAppointment());
					doctorConsultationServiceMapperDto.setPaymentEntitlementId(SELF);
					Response response = iDoctorConsultationServiceMapperService.getDoctorConsultationServiceBySpecialityAndDoctorId(doctorConsultationServiceMapperDto);
					if (response.getStatus().equals(SUCCESS) && response.getListObject().size() > 0) {
						DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto2 = (DoctorConsultationServiceMapperDto) response
								.getListObject().get(0);
						if (encounterMasterDto.getUserDefinedVisitTypeId() == 1) {
							serviceId = doctorConsultationServiceMapperDto2.getNewVisitServiceId();
							serviceName = doctorConsultationServiceMapperDto2.getNewVisitServiceName();
							specialityName = doctorConsultationServiceMapperDto2.getNewVisitSpecialityName();
							serviceAmount = doctorConsultationServiceMapperDto2.getNewVisitRate();
							encounterMasterDto.setIsDocShare(doctorConsultationServiceMapperDto2.getNewVisitDoctorShare());
						}
						if (encounterMasterDto.getUserDefinedVisitTypeId() == 2) {
							serviceId = doctorConsultationServiceMapperDto2.getFollowupVisitServiceId();
							serviceName = doctorConsultationServiceMapperDto2.getFollowupVisitServiceName();
							specialityName = doctorConsultationServiceMapperDto2.getFollowupVisitSpecialityName();
							serviceAmount = doctorConsultationServiceMapperDto2.getFollowupVisitRate();
							encounterMasterDto
									.setIsDocShare(doctorConsultationServiceMapperDto2.getFollowUpVisitDoctorShare());
						}
						if (encounterMasterDto.getUserDefinedVisitTypeId() == 3) {
							serviceId = doctorConsultationServiceMapperDto2.getSecondaryVisitServiceId();
							serviceName = doctorConsultationServiceMapperDto2.getSecondaryVisitServiceName();
							specialityName = doctorConsultationServiceMapperDto2.getSecondarySpecialityName();
							serviceAmount = doctorConsultationServiceMapperDto2.getSecondaryVisitRate();
							encounterMasterDto
									.setIsDocShare(doctorConsultationServiceMapperDto2.getSecondaryVisitDoctorShare());
						 }
						encounterMasterDto.setServiceId(serviceId);
						encounterMasterDto.setServiceAmount(serviceAmount);
						encounterMasterDto.setServiceDesc(serviceName);
						encounterMasterDto.setServiceSpecialityName(specialityName);
					}
				}
				if (serviceId != null)
					orderMasterDto = getOrderObject(encounterMasterDto, serviceId);

				
				if(patientRegistartionService!=null)
				{
					if(orderMasterDto==null)
					{
						orderMasterDto = new OrderMasterDto();
						orderMasterDto.setVisitTypeId(OP);// op==1
						orderMasterDto.setCreatedBy(encounterMasterDto.getCreatedBy());
						orderMasterDto.setUpdatedBy(encounterMasterDto.getUpdatedBy());
						orderMasterDto.setCreatedDate(encounterMasterDto.getCreatedDate());
						orderMasterDto.setUpdatedDate(encounterMasterDto.getUpdatedDate());
						orderMasterDto.setOrderDate(encounterMasterDto.getCreatedDate());
						orderMasterDto.setOrderStatus('A');
						orderMasterDto.setOrgId(encounterMasterDto.getOrganizationId());
						orderMasterDto.setOrgUnitId(encounterMasterDto.getUnitId());
						orderMasterDto.setOrderStatusId(1);// order status==1
					}
					
					OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();

					ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
						consumptionDto = new ContractConsumptionDto();
						consumptionDto.setOrganizationId(encounterMasterDto.getOrganizationId());
						consumptionDto.setUnitId(encounterMasterDto.getUnitId());
						consumptionDto.setVisitTypeId(OP);
						consumptionDto.setPatientTypeId(encounterMasterDto.getPatientCategoryId());
						consumptionDto.setBillingBedCategoryId(encounterMasterDto.getBillingBedCategoryId());
						consumptionDto.setPaymentEntitlementId(SELF);
						consumptionDto.setServiceId(patientRegistartionService);
						consumptionDto.setCurrentDate(encounterMasterDto.getCurrentDate());
						consumptionDto.setContractId(encounterMasterDto.getContractId());
						consumptionDto.setDefaultSelfTariffId(encounterMasterDto.getDefaultSelfTariffId());
						consumptionDto.setSelfPayeeId(encounterMasterDto.getPayeeId());
						consumptionDto.setConsumeService(1);
						consumptionDto.setPatientClassId(encounterMasterDto.getPatientClassId());
						consumptionDto.setIsFromEncounter(1);
						consumptionDto.setDocSplId(encounterMasterDto.getSpecialityId());
						Response res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
						consumptionDto = (ContractConsumptionDto) res.getObject();

						if (res.getObject() != null) 
						{
							if (consumptionDto.getSpecialityId() == null)
								orderDetailsMasterDto
										.setServiceRenderingDeptId(consumptionDto.getSpecialityId());
							else
								orderDetailsMasterDto.setServiceRenderingDeptId(consumptionDto.getSpecialityId());
							orderDetailsMasterDto.setVisitTypeId(OP);
							orderDetailsMasterDto.setEncounterId(encounterMasterDto.getEncounterId());
							orderDetailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
							orderDetailsMasterDto.setDummyServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
							orderDetailsMasterDto.setOrdConcession(new BigDecimal(consumptionDto.getConcession()));
							orderDetailsMasterDto.setOrdDiscount(new BigDecimal(0.00));
							orderDetailsMasterDto.setTariffId(consumptionDto.getTariffId());
							orderDetailsMasterDto.setBillingClassId(1);// By default its OPD ---> Id==1
							orderDetailsMasterDto.setOrderEmergencyFlag('N');
							orderDetailsMasterDto.setServiceRendered(1);
							orderDetailsMasterDto.setOrdRenderDatetime(encounterMasterDto.getCreatedDate());
							orderDetailsMasterDto.setServiceChargeable(1);
							orderDetailsMasterDto.setServiceIsBilled(0);
							orderDetailsMasterDto.setIsDrug('N');
							orderDetailsMasterDto.setPriorityId(2);
							orderDetailsMasterDto.setIsOutsourced('N');
							orderDetailsMasterDto.setSpecialityName(consumptionDto.getSpecialityName());
							orderDetailsMasterDto.setServiceName(consumptionDto.getServiceName());
							orderDetailsMasterDto.setPatientClassPercentage(new BigDecimal(consumptionDto.getPatientClassPercentage()));
							orderDetailsMasterDto.setOrdTotalAmt(new BigDecimal(consumptionDto.getServiceRate()));
							orderDetailsMasterDto.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()));
							orderDetailsMasterDto.setNetAmount((new BigDecimal(consumptionDto.getFinalPrice())));
							orderDetailsMasterDto.setSelfPayable((new BigDecimal(consumptionDto.getSelfPayable())));
							orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
							orderDetailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
							orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
							orderDetailsMasterDto.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
							if (consumptionDto.getFinalPrice().doubleValue() > 0.00) 
							{
								if(orderMasterDto.getListOrderDetailsMasterDto()!=null)
								{
									orderMasterDto.getListOrderDetailsMasterDto().add(orderDetailsMasterDto);
								}else{
									List orderDetailsList = new ArrayList();
									orderDetailsList.add(orderDetailsMasterDto);
									orderMasterDto.setListOrderDetailsMasterDto(orderDetailsList);	
								}
								if (consumptionDto.getPayeeId().equals(encounterMasterDto.getPayeeId())
										&& consumptionDto.getContractId() == null) 
								{
									List<OrderDetailsPayeeMapperDto> list = new LinkedList<>();
									OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
									detailsPayeeMapperDto.setPayeeId(consumptionDto.getPayeeId());
									detailsPayeeMapperDto.setPayeeAmount(new BigDecimal(consumptionDto.getFinalPrice()));
									detailsPayeeMapperDto.setServiceId(consumptionDto.getServiceId());
									list.add(detailsPayeeMapperDto);
									orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(list);
								}
							}
							if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
								orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
						}
					}
					return new Response<>(SUCCESS, null, null, null, orderMasterDto);
				}

		} catch (Exception e) {
		logger.error("getBillDetailsOnEncouter failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	public OrderMasterDto getOrderObject(EncounterMasterDto encounterMasterDto, Integer serviceId) 
	{
		try {
			Integer companyOrSelf = 0; // self=1 contract=1
			ContractConsumptionDto consumptionDto = null;
			OrderMasterDto orderMasterDto = new OrderMasterDto();
			orderMasterDto.setVisitTypeId(OP);// op==1
			orderMasterDto.setEncounterId(encounterMasterDto.getEncounterId());
			orderMasterDto.setPatientId(encounterMasterDto.getPatientId());
			orderMasterDto.setCreatedBy(encounterMasterDto.getCreatedBy());
			orderMasterDto.setUpdatedBy(encounterMasterDto.getUpdatedBy());
			orderMasterDto.setCreatedDate(encounterMasterDto.getCreatedDate());
			orderMasterDto.setUpdatedDate(encounterMasterDto.getUpdatedDate());
			orderMasterDto.setOrderDate(encounterMasterDto.getCreatedDate());
			orderMasterDto.setOrderStatus('A');
			orderMasterDto.setOrgId(encounterMasterDto.getOrganizationId());
			orderMasterDto.setOrgUnitId(encounterMasterDto.getUnitId());
			orderMasterDto.setOrderStatusId(1);// order status==1

			// -------order details-------//
			OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
			orderDetailsMasterDto.setServiceId(serviceId);
			orderDetailsMasterDto.setServiceIsBilled(0);
			orderDetailsMasterDto.setOrderQty(1);
			orderDetailsMasterDto.setCreatedBy(encounterMasterDto.getCreatedBy());
			orderDetailsMasterDto.setUpdatedBy(encounterMasterDto.getUpdatedBy());
			orderDetailsMasterDto.setOrderDate(encounterMasterDto.getCreatedDate());
			orderDetailsMasterDto.setOrderSchDate(encounterMasterDto.getCreatedDate());
			orderDetailsMasterDto.setOrdRenderDatetime(encounterMasterDto.getCreatedDate());
			orderDetailsMasterDto.setCreatedDate(encounterMasterDto.getCreatedDate());
			orderDetailsMasterDto.setUpdatedDate(encounterMasterDto.getUpdatedDate());
			orderDetailsMasterDto.setStatus('A');
			orderDetailsMasterDto.setOrgId(encounterMasterDto.getOrganizationId());
			orderDetailsMasterDto.setOrgUnitId(encounterMasterDto.getUnitId());
			orderDetailsMasterDto.setOrdDocId(encounterMasterDto.getDoctorId());
			orderDetailsMasterDto.setOrdDocSplId(encounterMasterDto.getSpecialityId());
			orderDetailsMasterDto.setServiceRenderingDeptId(encounterMasterDto.getConsultationSpecialityId());
			orderDetailsMasterDto.setOrdCancelled('N');
			orderDetailsMasterDto.setNetAmount(BigDecimal.ZERO);
			orderDetailsMasterDto.setIsRefDoctorShare(encounterMasterDto.getIsDocShare());
			UnitServiceTariffMasterDto unitServiceTariffMasterDto = null;



			
				consumptionDto = new ContractConsumptionDto();
				consumptionDto.setOrganizationId(encounterMasterDto.getOrganizationId());
				consumptionDto.setUnitId(encounterMasterDto.getUnitId());
				consumptionDto.setVisitTypeId(OP);
				consumptionDto.setPatientTypeId(encounterMasterDto.getPatientCategoryId());
				consumptionDto.setBillingBedCategoryId(encounterMasterDto.getBillingBedCategoryId());
				consumptionDto.setPaymentEntitlementId(SELF);
				consumptionDto.setServiceId(serviceId);
				consumptionDto.setCurrentDate(encounterMasterDto.getCurrentDate());
				consumptionDto.setContractId(encounterMasterDto.getContractId());
				consumptionDto.setEncounterId(encounterMasterDto.getEncounterId());
				consumptionDto.setDefaultSelfTariffId(encounterMasterDto.getDefaultSelfTariffId());
				consumptionDto.setSelfPayeeId(encounterMasterDto.getPayeeId());
				consumptionDto.setPatientId(encounterMasterDto.getPatientId());
				consumptionDto.setPatientClassId(encounterMasterDto.getPatientClassId());
				consumptionDto.setConsumeService(1);
				Response res = null;
				if (encounterMasterDto.getServiceAmount() != null) {
					consumptionDto.setConsultationAmount(encounterMasterDto.getServiceAmount());
				}
				
				res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
				consumptionDto = (ContractConsumptionDto) res.getObject();

				if (res.getObject() != null) {
					orderDetailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
					orderDetailsMasterDto.setDummyServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
					orderDetailsMasterDto.setOrdConcession(new BigDecimal(consumptionDto.getConcession()));
					orderDetailsMasterDto.setOrdDiscount(new BigDecimal(0.00));
					orderDetailsMasterDto.setTariffId(encounterMasterDto.getDefaultSelfTariffId());
					orderDetailsMasterDto.setBillingClassId(1);// By default its OPD ---> Id==1
					orderDetailsMasterDto.setOrderEmergencyFlag('N');
					orderDetailsMasterDto.setServiceRendered(1);
					orderDetailsMasterDto.setOrdRenderDatetime(encounterMasterDto.getCreatedDate());
					orderDetailsMasterDto.setServiceChargeable(1);
					orderDetailsMasterDto.setServiceIsBilled(0);
					orderDetailsMasterDto.setIsDrug('N');
					orderDetailsMasterDto.setSpecialityName(encounterMasterDto.getServiceSpecialityName());
					orderDetailsMasterDto.setServiceName(encounterMasterDto.getServiceDesc());
					orderDetailsMasterDto.setPatientClassPercentage(new BigDecimal(consumptionDto.getPatientClassPercentage()));
					orderDetailsMasterDto.setOrdTotalAmt(new BigDecimal(consumptionDto.getServiceRate()));
					orderDetailsMasterDto.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()));
					orderDetailsMasterDto.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()));
					orderDetailsMasterDto.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
					orderDetailsMasterDto.setSpecialityId(consumptionDto.getSpecialityId());
					orderDetailsMasterDto.setSubDepartmentId(consumptionDto.getSubSpecialityId());
					if(encounterMasterDto.getIsCyclicAppointment()!=null && encounterMasterDto.getIsCyclicAppointment()=='Y')
					{
						orderDetailsMasterDto.setNetAmount(BigDecimal.ZERO);
					}
					
					orderDetailsMasterDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
					orderDetailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
					orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
					orderDetailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
					orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
					if (consumptionDto.getPayeeId() == encounterMasterDto.getPayeeId()
							&& consumptionDto.getContractId() == null) {
						List<OrderDetailsPayeeMapperDto> list = new LinkedList<>();
						OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
						detailsPayeeMapperDto.setPayeeId(consumptionDto.getPayeeId());
						detailsPayeeMapperDto.setPayeeAmount(orderDetailsMasterDto.getNetAmount());
						detailsPayeeMapperDto.setServiceId(consumptionDto.getServiceId());
						list.add(detailsPayeeMapperDto);
						orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(list);
					} else
						orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(
								consumptionDto.getOrderDetailsPayeeMapperDtosList());
				}
				
				if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
					orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());

			List orderDetailsList = new ArrayList();
			
			//if (orderDetailsMasterDto.getNetAmount().doubleValue() > 0.00)
				orderDetailsList.add(orderDetailsMasterDto);

			orderMasterDto.setListOrderDetailsMasterDto(orderDetailsList);

			return orderMasterDto;
		} catch (Exception e) {
		logger.error("getOrderObject failed", e);
		}
		return null;
	}

	@Override
	@Transactional
	public Response getTokenMsgList(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.getTokenMsgList(encounterMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getEnounterDataForEMR(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.getEnounterDataForEMR(encounterMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response savePACSDetails(PACSDto dto) throws ApplicationException {
		try {
			return iEncounterMasterDao.savePACSDetails(dto);
		} catch (Exception e) {
		logger.error("savePACSDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPendingServicesOfEncounter(Integer encounterId) throws ApplicationException {
		try {
			return iEncounterMasterDao.getPendingServicesOfEncounter(encounterId);
		} catch (Exception e) {
		logger.error("getPendingServicesOfEncounter failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Transactional
	@Override
	public Response getNephrologyWorkbenchWorklist(RadiologyOrdersListDto radiologyOrdersListDto)
			throws ApplicationException {
		try {
			return iEncounterMasterDao.getNephrologyWorkbenchWorklist(radiologyOrdersListDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Transactional
	@Override
	public Response acceptHemodialysisRequest(HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto)
			throws ApplicationException {
		try {
			List<Integer> orderDetailsId = new LinkedList<>();
			orderDetailsId.add(hemodialysisAppointmentMapperDto.getOrderDetailsId());
			OrderDetailsMasterDto orderDetailsMasterDto1 = new OrderDetailsMasterDto();
			orderDetailsMasterDto1.setOrderDetailsId(hemodialysisAppointmentMapperDto.getOrderDetailsId());
			orderDetailsMasterDto1.setOrdDocId(hemodialysisAppointmentMapperDto.getDialysisDoctorId());
			orderDetailsMasterDto1.setOrdDocSplId(hemodialysisAppointmentMapperDto.getHemodialysisSpecialityId());
			iOrderDetailsMasterDao.updateOrdDocIdAndSpecialityId(orderDetailsMasterDto1);
			iOrderDetailsMasterService.updateOrderDetailsForDeptWiseAppointment(
					hemodialysisAppointmentMapperDto.getOrderDetailsId(),
					hemodialysisAppointmentMapperDto.getDialysisDoctorId(),
					hemodialysisAppointmentMapperDto.getHemodialysisSpecialityId());
			Response res1 = iOrderDetailsMasterService.renderAndPostRates(orderDetailsId);
			return res1;

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response getPatientServicesForFutureDate(PatientDocReqDto patientDocReqDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.getPatientServicesForFutureDate(patientDocReqDto);
		} catch (Exception e) {
		logger.error("getPatientServicesForFutureDate failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response rejectHemodialysisRequest(HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto)
			throws ApplicationException {
		try {
			Response res = iEncounterMasterDao.rejectHemodialysisRequest(hemodialysisAppointmentMapperDto);
			if(res.getStatus().equals(SUCCESS))
			{
				ContractConsumptionDto consumptionDto =new ContractConsumptionDto();
				  consumptionDto.setEncounterId(hemodialysisAppointmentMapperDto.getEncounterId());
				  consumptionDto.setAdmissionId(hemodialysisAppointmentMapperDto.getAdmissionId());
				  consumptionDto.setSpecialityId(hemodialysisAppointmentMapperDto.getSpecialityId());
				 // consumptionDto.setOrderDetailsId(hemodialysisAppointmentMapperDto.getOrderDetailsId());
				  consumptionDto.setOrderDetailsPackageMapperDtosList(hemodialysisAppointmentMapperDto.getOrderDetailsPackageMapperDtosList());
				  consumptionDto.setOrderDetailsPayeeMapperDtosList(hemodialysisAppointmentMapperDto.getOrderDetailsPayeeMapperDtosList());
				  consumptionDto.setServiceId(hemodialysisAppointmentMapperDto.getServiceId());
				  
				iContractConsumptionService.addCapAmount(consumptionDto);
			}
			return new Response(SUCCESS, null, "Request rejected...!!", null, null);
			
		} catch (Exception e) {
		logger.error("rejectHemodialysisRequest failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@org.springframework.transaction.annotation.Transactional
	public Response getReviewApplicableServicesByPatient(Integer patientId, Integer orgId, Integer unitId)
			throws ApplicationException {
		try {
			Date date = TimeZoneComponent.getDateByzone(unitId);
			return iEncounterMasterDao.getReviewApplicableServicesByPatient(patientId, orgId, unitId, date);
		} catch (Exception e) {
		logger.error("getReviewApplicableServicesByPatient failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@org.springframework.transaction.annotation.Transactional
	public Response getEHCConsultation(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			Response res = iEncounterMasterDao.getEHCConsultation(encounterMasterDto);
			if (res.getListObject() != null && res.getListObject().size() > 0) {
				List<EncounterEhcConsultationMpprDto> list = res.getListObject();
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					EncounterEhcConsultationMpprDto dto = (EncounterEhcConsultationMpprDto) iterator.next();
					Response response = iServiceMasterService.getConsultationDeptByServiceId(dto.getServiceId(),
							dto.getUnitId());
					if (response.getListObject() != null && response.getListObject().size() > 0) {
						dto.setSpecialityList(response.getListObject());
					}
				}
			}
			return res;
		} catch (Exception e) {
		logger.error("getEHCConsultation failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveChildEncounter(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			Character isNursingAssessmentRequiredForUnit = null;
			Character isPrePostBilling = null;
			Character isKiosk = null;
			Character isKioskRequired = null;
			List<Integer> serviceList = new LinkedList<>();
			Response orderRes = null;
			Response refRes = null;
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setOrganizationId(encounterMasterDto.getOrganizationId());
			unitApplicationConfigurationDto.setUnitId(encounterMasterDto.getUnitId());
			Response appConfigRes = iUnitApplicationBillingConfigurationDao
					.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
			AppointmentMaster appointmentMaster = null;
			if (appConfigRes.getObject() != null) {
				UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto) appConfigRes
						.getObject();

				if (unitApplicationConfigurationDtos.getDefaultSelfTariffId() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Default tariff is not defined !!", null, null);
				else
					encounterMasterDto
							.setDefaultSelfTariffId(unitApplicationConfigurationDtos.getDefaultSelfTariffId());

				if (unitApplicationConfigurationDtos.getOpdBillingCatId() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Opd Billing Category Id is not defined !!", null,
							null);
				else
					encounterMasterDto.setBillingBedCategoryId(unitApplicationConfigurationDtos.getOpdBillingCatId());

				if (unitApplicationConfigurationDtos.getSelfPayeeId() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Self Payee is not defined !!", null, null);
				else
					encounterMasterDto.setPayeeId(unitApplicationConfigurationDtos.getSelfPayeeId());

				if (unitApplicationConfigurationDtos.getConsultationSpecialityId() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Self Consultation Speciality is not defined !!",
							null, null);
				else
					encounterMasterDto.setConsultationSpecialityId(
							unitApplicationConfigurationDtos.getConsultationSpecialityId());

				if (unitApplicationConfigurationDtos.getIsPreOrPostBilling() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Pre billing post billing is not defined !!", null,
							null);
				else
					isPrePostBilling = unitApplicationConfigurationDtos.getIsPreOrPostBilling();

				if (unitApplicationConfigurationDtos.getIsKioskRequired() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Kiosk required is not defined !!", null, null);
				else
					isKiosk = unitApplicationConfigurationDtos.getIsKioskRequired();

				if (unitApplicationConfigurationDtos.getIsNursingAssessment() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Is Nursing Assessment Required is not defined !!",
							null, null);
				else
					isNursingAssessmentRequiredForUnit = unitApplicationConfigurationDtos
							.getIsNursingAssessment() != null
									? unitApplicationConfigurationDtos.getIsNursingAssessment()
									: 'Y';

				if (unitApplicationConfigurationDtos.getIsKioskRequired() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Is Kiosk Required is not defined !!", null, null);
				else
					isKioskRequired = unitApplicationConfigurationDtos.getIsKioskRequired() != null
							? unitApplicationConfigurationDtos.getIsKioskRequired()
							: 'N';

			} else {
				return new Response<>(ERROR, COMMON_ERROR_CODE, "Application Configuration is yet to set !!", null,
						null);
			}
			encounterMasterDto.setIsPreOrPostBilling(isPrePostBilling);
			Character isNursingAssessmentRequiredForSpeciality = encounterMasterDto
					.getIsNursingAssessmentRequiredForSpeciality() != null
							? encounterMasterDto.getIsNursingAssessmentRequiredForSpeciality()
							: 'Y';
			Integer serviceId = null;
			Double serviceAmount = null;
			Integer tokenNumber = null;
			if (encounterMasterDto.getAppointmentObject() != null) {
				encounterMasterDto.getAppointmentObject().setIsFromCoversheet('Y');
				encounterMasterDto.getAppointmentObject().setAppointmentStatusId(APPOINTMENTSTATUSID5);
				Response res = iAppointmentSchedulingService
						.scheduleAppointment(encounterMasterDto.getAppointmentObject());
				if (res.getStatus().equals(SUCCESS)) {
					appointmentMaster = (AppointmentMaster) res.getObject();
					encounterMasterDto.setAppointmentId(appointmentMaster.getAppointmentId());

				} else {
					return res;
				}
			}

			if (encounterMasterDto.getAppointmentId() != null && encounterMasterDto.getAppointmentId() > 0
					&& encounterMasterDto.getSlotId() != null) {
				iAppointmentSchedulingDao.updateSlotId(encounterMasterDto.getAppointmentId(),
						encounterMasterDto.getSlotId(), encounterMasterDto.getOldSlotId());
			}

			String encountrNo = iGlobalUnitMasterDao.getGeneratedNo(0, encounterMasterDto.getUnitId(),
					IBillingCommonConstants.ENCOUNTER);
			encounterMasterDto.setEncounterNumber(encountrNo != null ? encountrNo : generateEncounterNumber());

			if (encounterMasterDto.getEncounterTypeId() != 4 && encounterMasterDto.getEncounterTypeId() != 3
					&& encounterMasterDto.getEncounterTypeId() != 2 && isKiosk == 'Y') {
				encounterMasterDto.setUnitTokenNumber(
						encounterMasterDto.getUnitTokenNumber() != null ? encounterMasterDto.getUnitTokenNumber()
								: appointmentMaster.getTokenNumber());
			}

			if (encounterMasterDto.getDeptWiseAppointment().equals('N') && encounterMasterDto.getDoctorId() != null
					&& isKiosk == 'Y') {
				encounterMasterDto.setDocQueueNumber(this.generateDocQueueNumber(encounterMasterDto));
			}

			if (encounterMasterDto.getEncounterTypeId() == 5
					|| encounterMasterDto.getDeptWiseAppointment().equals('Y')) {
				encounterMasterDto.setSpecialityQueueNumber(this.generateSpecialityQueueNumber(encounterMasterDto));
				if (encounterMasterDto.getAppointmentId() > 0 && encounterMasterDto.getEncounterTypeId() == 5) {
					iAppointmentSchedulingDao.updateAppointmentTypeId(encounterMasterDto.getAppointmentId());
				}
			}
			if (encounterMasterDto.getAppointmentId() != null
					&& encounterMasterDto.getAppointmentId() > 0) {
				SlotMasterDto slotMasterDto = new SlotMasterDto();
				slotMasterDto.setAppointmentId(encounterMasterDto.getAppointmentId());
				slotMasterDto.setAppointmentStatusId(APPOINTMENTSTATUSID5);
				slotMasterDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(encounterMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				slotMasterDto.setUpdatedBy(encounterMasterDto.getUpdatedBy());
				iAppointmentSchedulingService.updateAppointmentStatus(slotMasterDto);
			}

			// Saving encounter
			encounterMasterDto.setStatusId(5); // checkIn
			Response encounterResponse = iEncounterMasterDao.saveEncounterMaster(encounterMasterDto);
			if (encounterResponse.getStatus().equals(SUCCESS)) {
				EncounterMasterDto encounterMasterDto2 = (EncounterMasterDto) encounterResponse.getObject();

				// <----For skipping nursing assessment
				iEncounterAssignmenMapperService.saveEncounterAssignmentIfAssessignmentIsFalse(encounterMasterDto);

				// Change Ehc Consultation Mppr Status
				encounterMasterDto.setStatusId(5);// Set Appointment status
				iEncounterMasterDao.updateEncounterServiceMpperStatus(encounterMasterDto);
				encounterMasterDto.setChildEncounterId(encounterMasterDto2.getEncounterId());
				iEncounterMasterDao.updateChildEncounter(encounterMasterDto);
				encounterMasterDto.setIsCalled('Y');
				EncounterMasterDto tempDto=encounterMasterDto;	
				
				patientCalled(encounterMasterDto);
				// For skipping nursing assessment---->

				iEncounterMasterDao.checkReferalAndUpdate(encounterMasterDto2);

			}
			// for calculating doctor share at ehc encounter worklist 
			iOrderDetailsMasterService.updateOrderDetailsForDeptWiseAppointment(encounterMasterDto.getOrderDetailId(),encounterMasterDto.getDoctorId(),encounterMasterDto.getSpecialityId());

			
			  //for render package
				OrderDetailsMaster orderDetailsMasterDto = iOrderDetailsMasterDao.getOrderDetailsByIdForPayeeUpdate(encounterMasterDto.getOrderDetailId());
			    Response packageMpprRes = iOrderDetailsMasterDao.getOrderDetailsPackageMapperByOrderDetailsId(encounterMasterDto.getOrderDetailId());
			    List<OrderDetailsPackageMapperDto> mpprList = packageMpprRes.getListObject();
	          for (OrderDetailsPackageMapperDto mpperDto : mpprList) {
	            if(mpperDto.getIsPackageService()=='Y')
	            {
	          	  AdmissionsForBatchProcessDto vistDetailsObj=new  AdmissionsForBatchProcessDto();
	          	     vistDetailsObj.setEncounterId(encounterMasterDto.getAgainstEncId());
	                  orderDetailsMasterDto.setServiceRendered(1);
	                  orderDetailsMasterDto.setOrdRenderDatetime(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()).getTime());
	                  iMPackageMasterService.renderPackageServiceFrPostBillig(orderDetailsMasterDto,vistDetailsObj,mpperDto.getPackageId());
	            }
	          }
			return encounterResponse;

		} catch (Exception e) {
		logger.error("saveChildEncounter failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response rejectRadiologyRequest(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException {
		try {

			return iEncounterMasterDao.saveRadiologyMapper(radiologyOrdersListDto);
		} catch (Exception e) {
		logger.error("rejectRadiologyRequest failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getEncounterDetailsForCoversheet(EncounterMasterDto encounterMasterDto)
			throws ApplicationException {
		try {
			return iEncounterMasterDao.getEncounterDetailsForCoversheet(encounterMasterDto);
		} catch (Exception e) {
		logger.error("getEncounterDetailsForCoversheet failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getOtherServicesForRendering(RadiologyOrdersListDto radiologyOrdersListDto)
			throws ApplicationException {
		try {
			return iEncounterMasterDao.getOtherServicesForRendering(radiologyOrdersListDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response renderOtherServices(RadiologyOrdersListDto radiologyOrdersListDto) {
		try {

				List<Integer> orderDetailsId = new LinkedList<>();
				orderDetailsId.add(radiologyOrdersListDto.getOrderDetailsId());
				return iOrderDetailsMasterService.renderAndPostRates(orderDetailsId);
			
		} catch (Exception e) {
		logger.error("renderOtherServices failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPatientListForPhysiotherapyWorkbench(PhysiotherapyOrdersListDto physiotherapyOrdersListDto) {
		try {
			return iEncounterMasterDao.getPatientListForPhysiotherapyWorkbench(physiotherapyOrdersListDto);
		} catch (Exception e) {
		logger.error("getPatientListForPhysiotherapyWorkbench failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	
	public Response  postCyclicOrderDetail(EncounterMasterDto encounterMasterDto) 
	{
		try{
			Response orderDetailsRes=iEncounterMasterDao.getOrderDetailByAppointment(encounterMasterDto);
				OrderDetailsByEncounterIdDto orderDetailsObj = (OrderDetailsByEncounterIdDto) orderDetailsRes.getObject();
				if(orderDetailsObj!=null) {
					ContractConsumptionDto consumptionDto = null;
					
					
					BigDecimal serviceAmt = BigDecimal.ZERO;
					BigDecimal taxAmt = BigDecimal.ZERO;
					BigDecimal netAmt = BigDecimal.ZERO;
					
					//-------order details-------//
					OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
					    orderDetailsMasterDto.setOrderDetailsId(orderDetailsObj.getOrderDetailsId());
						orderDetailsMasterDto.setServiceId(orderDetailsObj.getServiceMasterId());
						orderDetailsMasterDto.setServiceIsBilled(0);
						orderDetailsMasterDto.setOrderQty(1);
						orderDetailsMasterDto.setCreatedBy(encounterMasterDto.getCreatedBy());
						orderDetailsMasterDto.setUpdatedBy(encounterMasterDto.getUpdatedBy());
						orderDetailsMasterDto.setOrderDate(encounterMasterDto.getCreatedDate());
						orderDetailsMasterDto.setOrderSchDate(encounterMasterDto.getCreatedDate());
						orderDetailsMasterDto.setOrdRenderDatetime(encounterMasterDto.getCreatedDate());
						orderDetailsMasterDto.setCreatedDate(encounterMasterDto.getCreatedDate());
						orderDetailsMasterDto.setUpdatedDate(encounterMasterDto.getUpdatedDate());
						orderDetailsMasterDto.setStatus('I');
						orderDetailsMasterDto.setOrgId(encounterMasterDto.getOrganizationId());
						orderDetailsMasterDto.setOrgUnitId(encounterMasterDto.getUnitId());
					    orderDetailsMasterDto.setOrdDocId(encounterMasterDto.getDoctorId());
						orderDetailsMasterDto.setOrdDocSplId(encounterMasterDto.getSpecialityId());
						orderDetailsMasterDto.setServiceRenderingDeptId(orderDetailsObj.getServiceRenderingDeptId());
						orderDetailsMasterDto.setOrdCancelled('N');
						orderDetailsMasterDto.setTariffId(encounterMasterDto.getDefaultSelfTariffId());
						orderDetailsMasterDto.setIsOutsourced(orderDetailsObj.getIsOutsourced());
						orderDetailsMasterDto.setPriorityId(orderDetailsObj.getPriorityId());
						orderDetailsMasterDto.setOrderId(orderDetailsObj.getOrderId());
					
						if(encounterMasterDto.getConsultationSpecialityId()!=null && encounterMasterDto.getConsultationSpecialityId().equals(orderDetailsObj.getServiceSpecialityId()))
								{
							       orderDetailsMasterDto.setIsConsultationService('Y');
								}
							else {
							    orderDetailsMasterDto.setIsConsultationService('N');
						     }
						
						UnitServiceTariffMasterDto unitServiceTariffMasterDto =  null;
									 consumptionDto = new ContractConsumptionDto();
										consumptionDto.setOrganizationId(encounterMasterDto.getOrganizationId());
										consumptionDto.setUnitId(encounterMasterDto.getUnitId());
										consumptionDto.setVisitTypeId(OP);
										consumptionDto.setPatientTypeId(encounterMasterDto.getPatientCategoryId());
										consumptionDto.setBillingBedCategoryId(encounterMasterDto.getBillingBedCategoryId());
										consumptionDto.setPaymentEntitlementId(SELF);
										consumptionDto.setServiceId(orderDetailsObj.getServiceMasterId());
										consumptionDto.setCurrentDate(encounterMasterDto.getCurrentDate());
										consumptionDto.setContractId(encounterMasterDto.getContractId());
										consumptionDto.setEncounterId(encounterMasterDto.getEncounterId());
										consumptionDto.setDefaultSelfTariffId(encounterMasterDto.getDefaultSelfTariffId());
										consumptionDto.setPatientId(encounterMasterDto.getPatientId());
										consumptionDto.setSelfPayeeId(encounterMasterDto.getPayeeId());
										Response res = null;
										//consumptionDto.setConsultationAmount();
										res =iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
										consumptionDto=(ContractConsumptionDto)res.getObject();
										
									
									if(res.getObject()!=null)
									{
										orderDetailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
										orderDetailsMasterDto.setDummyServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
										orderDetailsMasterDto.setOrdConcession(new BigDecimal(consumptionDto.getConcession()));
										orderDetailsMasterDto.setPatientClassPercentage(new BigDecimal(consumptionDto.getPatientClassPercentage()));
										orderDetailsMasterDto.setOrdDiscount(new BigDecimal(0.00));
										orderDetailsMasterDto.setTariffId(consumptionDto.getTariffId());
										orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
										orderDetailsMasterDto.setOrderEmergencyFlag('N');
										orderDetailsMasterDto.setServiceRendered(0);
										orderDetailsMasterDto.setOrdRenderDatetime(encounterMasterDto.getCreatedDate());
										orderDetailsMasterDto.setServiceChargeable(1);
										orderDetailsMasterDto.setServiceIsBilled(0);
										orderDetailsMasterDto.setIsDrug('N');
										orderDetailsMasterDto.setOrdTotalAmt(BigDecimal.valueOf(consumptionDto.getServiceRate()));
										serviceAmt = BigDecimal.valueOf(consumptionDto.getServiceRate());
										
										Tax tax = iEncounterMasterDao.getTaxPercentageByServiceId(orderDetailsObj.getServiceMasterId(),encounterMasterDto.getUnitId());
										if(tax != null)
										{
											orderDetailsMasterDto.setTaxId(tax.getId());
											orderDetailsMasterDto.setTaxPer(new BigDecimal(tax.getTaxPercentage()));
											serviceAmt = orderDetailsMasterDto.getServiceAmt();
										}else
										{
											orderDetailsMasterDto.setTaxPer(new BigDecimal(0.00));
											serviceAmt = orderDetailsMasterDto.getServiceAmt();
										}
										
										
										orderDetailsMasterDto.setNetAmount(BigDecimal.ZERO);
										orderDetailsMasterDto.setSelfPayable(BigDecimal.ZERO);
										orderDetailsMasterDto.setCreditPayable(BigDecimal.ZERO);
										orderDetailsMasterDto.setSelfPayable(BigDecimal.ZERO);
										orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
										orderDetailsMasterDto.setCreditPayable(BigDecimal.ZERO);
										orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
										orderDetailsMasterDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
										orderDetailsMasterDto.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
										if(consumptionDto.getPayeeId()==encounterMasterDto.getPayeeId() && consumptionDto.getContractId()==null)
										{
											List<OrderDetailsPayeeMapperDto> list = new LinkedList<>();
											OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
												detailsPayeeMapperDto.setPayeeId(consumptionDto.getPayeeId());
												detailsPayeeMapperDto.setPayeeAmount(BigDecimal.ZERO);
												detailsPayeeMapperDto.setServiceId(consumptionDto.getServiceId());
											list.add(detailsPayeeMapperDto);	
											orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(list);
										}else
											orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
										
										if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
											orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
										iOrderDetailsMasterDao.updateOrderDetails(orderDetailsMasterDto,'N',encounterMasterDto.getPayeeId());
										iOrderDetailsMasterDao.updateEncounterToOrderMaster(encounterMasterDto,orderDetailsObj.getOrderId());
									}
				    }
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
				} catch (Exception e) {
				logger.error("postCyclicOrderDetail failed", e);
				}
		
		return new Response<>(ERROR, null, null, null, null);
	}

	@Override
	public Response billAmountForAppointment(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			List<OrderDetailsMasterDto> orderDetailsMasterDtosList = new LinkedList<OrderDetailsMasterDto>();
			Character isPrePostBilling = null;
			List<Integer> serviceList = new LinkedList<>();
			Response orderRes = null;
			encounterMasterDto.setVisitTypeId(OP);
			OrderMasterDto orderMasterDto = null;
			Double serviceAmount = null;
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			encounterMasterDto.setDefaultSelfTariffId(unitApplicationConfigurationDto.getDefaultSelfTariffId());
			unitApplicationConfigurationDto.setOrganizationId(encounterMasterDto.getOrganizationId());
			unitApplicationConfigurationDto.setUnitId(encounterMasterDto.getUnitId());
			Response appConfigRes = iUnitApplicationBillingConfigurationDao
					.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
			AppointmentMaster appointmentMaster = null;
			if (appConfigRes.getObject() != null) {
				UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto) appConfigRes
						.getObject();

				if (unitApplicationConfigurationDtos.getDefaultSelfTariffId() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Default tariff is not defined !!", null, null);
				else
					encounterMasterDto
							.setDefaultSelfTariffId(unitApplicationConfigurationDtos.getDefaultSelfTariffId());

				if (unitApplicationConfigurationDtos.getOpdBillingCatId() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Opd Billing Category Id is not defined !!", null,
							null);
				else
					encounterMasterDto.setBillingBedCategoryId(unitApplicationConfigurationDtos.getOpdBillingCatId());

				if (unitApplicationConfigurationDtos.getSelfPayeeId() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Self Payee is not defined !!", null, null);
				else
					encounterMasterDto.setPayeeId(unitApplicationConfigurationDtos.getSelfPayeeId());

				if (unitApplicationConfigurationDtos.getConsultationSpecialityId() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Self Consultation Speciality is not defined !!",
							null, null);
				else
					encounterMasterDto.setConsultationSpecialityId(
							unitApplicationConfigurationDtos.getConsultationSpecialityId());

				if (unitApplicationConfigurationDtos.getIsPreOrPostBilling() == null)
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Pre billing post billing is not defined !!", null,
							null);
				else
					isPrePostBilling = unitApplicationConfigurationDtos.getIsPreOrPostBilling();

			} else {
				return new Response<>(ERROR, COMMON_ERROR_CODE, "Application Configuration is yet to set !!", null,
						null);
			}

			Integer serviceId = null;
			String serviceName = null;
			String specialityName = null;
			Integer tokenNumber = null;
			Integer visitType = 1;
			if (encounterMasterDto.getUserDefinedVisitTypeId() != null
					&& encounterMasterDto.getUserDefinedVisitTypeId() != 4
					&& encounterMasterDto.getUserDefinedVisitTypeId() != 5 ) 
			{
				DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto = new DoctorConsultationServiceMapperDto();
					doctorConsultationServiceMapperDto.setOrganizationId(encounterMasterDto.getOrganizationId());
					doctorConsultationServiceMapperDto.setUnitId(encounterMasterDto.getUnitId());
					doctorConsultationServiceMapperDto.setDoctorId(encounterMasterDto.getDoctorId());
					doctorConsultationServiceMapperDto.setSpecialityId(encounterMasterDto.getSpecialityId());
					doctorConsultationServiceMapperDto.setDeptWiseAppointment(encounterMasterDto.getDeptWiseAppointment());
					doctorConsultationServiceMapperDto.setPaymentEntitlementId(SELF);
				Response response = iDoctorConsultationServiceMapperService
						.getDoctorConsultationServiceBySpecialityAndDoctorId(doctorConsultationServiceMapperDto);
				if (response.getStatus().equals(SUCCESS) && response.getListObject().size() > 0) {
					DoctorConsultationServiceMapperDto doctorConsultationServiceMapperDto2 = (DoctorConsultationServiceMapperDto) response
							.getListObject().get(0);
					if (encounterMasterDto.getUserDefinedVisitTypeId() == 1) {
						serviceId = doctorConsultationServiceMapperDto2.getNewVisitServiceId();
						serviceName = doctorConsultationServiceMapperDto2.getNewVisitServiceName();
						specialityName = doctorConsultationServiceMapperDto2.getNewVisitSpecialityName();
						serviceAmount = doctorConsultationServiceMapperDto2.getNewVisitRate();
						encounterMasterDto.setIsDocShare(doctorConsultationServiceMapperDto2.getNewVisitDoctorShare());
					}
					if (encounterMasterDto.getUserDefinedVisitTypeId() == 2) {
						serviceId = doctorConsultationServiceMapperDto2.getFollowupVisitServiceId();
						serviceName = doctorConsultationServiceMapperDto2.getFollowupVisitServiceName();
						specialityName = doctorConsultationServiceMapperDto2.getFollowupVisitSpecialityName();
						serviceAmount = doctorConsultationServiceMapperDto2.getFollowupVisitRate();
						encounterMasterDto
								.setIsDocShare(doctorConsultationServiceMapperDto2.getFollowUpVisitDoctorShare());
					}
					if (encounterMasterDto.getUserDefinedVisitTypeId() == 3) {
						serviceId = doctorConsultationServiceMapperDto2.getSecondaryVisitServiceId();
						serviceName = doctorConsultationServiceMapperDto2.getSecondaryVisitServiceName();
						specialityName = doctorConsultationServiceMapperDto2.getSecondarySpecialityName();
						serviceAmount = doctorConsultationServiceMapperDto2.getSecondaryVisitRate();
						encounterMasterDto
								.setIsDocShare(doctorConsultationServiceMapperDto2.getSecondaryVisitDoctorShare());
					 }
					encounterMasterDto.setServiceId(serviceId);
					encounterMasterDto.setServiceAmount(serviceAmount);
					encounterMasterDto.setServiceDesc(serviceName);
					encounterMasterDto.setServiceSpecialityName(specialityName);
				}
			}
			
			
			if (encounterMasterDto.getPatientId() != null) {
				SlotMasterDto dto = new SlotMasterDto();
					dto.setPatientId(encounterMasterDto.getPatientId());
					dto.setDoctorId(encounterMasterDto.getDoctorId());
					dto.setOrganizationId(encounterMasterDto.getOrganizationId());
					dto.setUnitId(encounterMasterDto.getUnitId());
					dto.setCurrentDate(encounterMasterDto.getCurrentDate());
					dto.setSpecialityId(encounterMasterDto.getSpecialityId());
				visitType = assignVisitTypeIdAccordingToConditions(dto);
			} else {
				visitType = 1; // primary visit for pre-registered patients.
			}
			
			
			if (serviceId != null)
			{
				orderMasterDto = getOrderObject(encounterMasterDto, serviceId);
				orderMasterDto.setSystemDefiedVisitType(visitType);

				List<ServiceMasterDto> listServiceMasterDto = new ArrayList<ServiceMasterDto>();
				if (encounterMasterDto.getPatientId() != null && encounterMasterDto.getPatientId() > 0)
				listServiceMasterDto = iEncounterMasterDao
						.getAutoRenderedServicesByPatient(encounterMasterDto).getListObject();
				
				OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
				if (listServiceMasterDto.size() > 0) 
				{
					ListIterator<ServiceMasterDto> iterator = listServiceMasterDto.listIterator();

					while (iterator.hasNext()) 
					{
						ServiceMasterDto masterDto = iterator.next();

						ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
						consumptionDto = new ContractConsumptionDto();
						consumptionDto.setOrganizationId(encounterMasterDto.getOrganizationId());
						consumptionDto.setUnitId(encounterMasterDto.getUnitId());
						consumptionDto.setVisitTypeId(OP);
						consumptionDto.setPatientTypeId(encounterMasterDto.getPatientCategoryId());
						consumptionDto.setBillingBedCategoryId(encounterMasterDto.getBillingBedCategoryId());
						consumptionDto.setPaymentEntitlementId(SELF);
						consumptionDto.setServiceId(masterDto.getServiceMasterId());
						consumptionDto.setCurrentDate(encounterMasterDto.getCurrentDate());
						consumptionDto.setContractId(encounterMasterDto.getContractId());
						consumptionDto.setEncounterId(encounterMasterDto.getEncounterId());
						consumptionDto.setDefaultSelfTariffId(encounterMasterDto.getDefaultSelfTariffId());
						consumptionDto.setSelfPayeeId(encounterMasterDto.getPayeeId());
						consumptionDto.setPatientId(encounterMasterDto.getPatientId());
						consumptionDto.setConsumeService(1);
						consumptionDto.setPatientClassId(encounterMasterDto.getPatientClassId());
						consumptionDto.setIsFromEncounter(1);
						Response res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
						consumptionDto = (ContractConsumptionDto) res.getObject();
						
						if (res.getObject() != null) {
							if (masterDto.getRenderingDepartmentId() == null)
								orderDetailsMasterDto
										.setServiceRenderingDeptId(encounterMasterDto.getConsultationSpecialityId());
							else
								orderDetailsMasterDto.setServiceRenderingDeptId(masterDto.getRenderingDepartmentId());
							orderDetailsMasterDto.setVisitTypeId(OP);
							orderDetailsMasterDto.setOrderId(masterDto.getOrderId());
							orderDetailsMasterDto.setEncounterId(encounterMasterDto.getEncounterId());
							orderDetailsMasterDto.setOrderDetailsId(masterDto.getOrderDetailsId());
							orderDetailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
							orderDetailsMasterDto.setDummyServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
							orderDetailsMasterDto.setOrdConcession(new BigDecimal(consumptionDto.getConcession()));
							orderDetailsMasterDto.setOrdDiscount(new BigDecimal(0.00));
							orderDetailsMasterDto.setTariffId(consumptionDto.getTariffId());
							orderDetailsMasterDto.setBillingClassId(1);// By default its OPD ---> Id==1
							orderDetailsMasterDto.setOrderEmergencyFlag('N');
							orderDetailsMasterDto.setServiceRendered(1);
							orderDetailsMasterDto.setOrdRenderDatetime(encounterMasterDto.getCreatedDate());
							orderDetailsMasterDto.setServiceChargeable(1);
							orderDetailsMasterDto.setServiceIsBilled(0);
							orderDetailsMasterDto.setIsDrug('N');
							orderDetailsMasterDto.setPriorityId(2);
							orderDetailsMasterDto.setIsOutsourced('N');
							orderDetailsMasterDto.setSpecialityName(consumptionDto.getSpecialityName());
							orderDetailsMasterDto.setServiceName(consumptionDto.getServiceName());
							orderDetailsMasterDto.setPatientClassPercentage(new BigDecimal(consumptionDto.getPatientClassPercentage()));
							orderDetailsMasterDto.setOrdTotalAmt(new BigDecimal(consumptionDto.getServiceRate()));
							orderDetailsMasterDto.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()));
							orderDetailsMasterDto.setNetAmount((new BigDecimal(consumptionDto.getFinalPrice())));
				 			orderDetailsMasterDto.setSelfPayable((new BigDecimal(consumptionDto.getSelfPayable())));
							orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
							orderDetailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
							orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
							orderDetailsMasterDto.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
							orderDetailsMasterDto.setSpecialityId(consumptionDto.getSpecialityId());
							orderDetailsMasterDto.setSubDepartmentId(consumptionDto.getSubSpecialityId());
							if(orderDetailsMasterDto.getNetAmount().doubleValue() > 0.00) 
							{
								orderMasterDto.getListOrderDetailsMasterDto().add(orderDetailsMasterDto);
								if (consumptionDto.getPayeeId() == encounterMasterDto.getPayeeId()
										&& consumptionDto.getContractId() == null) {
									List<OrderDetailsPayeeMapperDto> list = new LinkedList<>();
									OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
									detailsPayeeMapperDto.setPayeeId(consumptionDto.getPayeeId());
									detailsPayeeMapperDto.setPayeeAmount(new BigDecimal(consumptionDto.getFinalPrice()));
									detailsPayeeMapperDto.setServiceId(consumptionDto.getServiceId());
									list.add(detailsPayeeMapperDto);
									orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(list);
								} else
									orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(
									consumptionDto.getOrderDetailsPayeeMapperDtosList());
							}
							if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
							orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
						}
					}
				}

				return new Response<>(SUCCESS, null, null, null, orderMasterDto);
			}

		} catch (Exception e) {
		logger.error("billAmountForAppointment failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response savePackageConsumptionForDirectEHCEncounter(List<OrderDetailsMasterDto> listToBeAdded) throws ApplicationException {
		try {
			ListIterator<OrderDetailsMasterDto> packageItr = listToBeAdded.listIterator();
			if (listToBeAdded != null && listToBeAdded.size() > 0) 
			{
				while (packageItr.hasNext()) {
					OrderDetailsMasterDto orderDtls = packageItr.next();
					if (orderDtls.isPackage() && orderDtls.getPackageId() != null
							&& orderDtls.getIsPackageService() != 'Y' && (orderDtls.getStatus()!=null && orderDtls.getStatus()=='A')) {

					

						Integer createdBy = orderDtls.getCreatedBy();
						Integer updatedBy = orderDtls.getUpdatedBy();
						String createdDate = GlobalCommonDateUtils.getStringDate(
								new Date(orderDtls.getCreatedDate() != null ? orderDtls.getCreatedDate()
										: TimeZoneComponent.getDateByzone(orderDtls.getOrgUnitId()).getTime()),
								"dd-M-yyyy HH:mm:ss");
						String updatedDate = GlobalCommonDateUtils.getStringDate(
								new Date(orderDtls.getUpdatedDate() != null ? orderDtls.getUpdatedDate()
										: TimeZoneComponent.getDateByzone(orderDtls.getOrgUnitId()).getTime()),
								"dd-M-yyyy HH:mm:ss");

						// save package into package consumption
						Response packRes = iMPackageMasterDao.getPackageById(orderDtls.getPackageId());
						if (packRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && packRes.getObject() != null) 
						{
							MPackageMasterDto mPackage = (MPackageMasterDto) packRes.getObject();
							TPackageConsumptionMasterDto consMstDto = new TPackageConsumptionMasterDto();
							consMstDto.setPackageConsumptionMasterId(orderDtls.getPackageConsumptionMasterId());
//							consMstDto.setAdmissionId(orderDtls.getAdmissionId());
							consMstDto.setApplicableNoOfVisit(mPackage.getNoOfEncounters());
//							consMstDto.setEncounterSpecialityId(orderDtls.get);
							consMstDto.setEncounterId(orderDtls.getEncounterId());
							consMstDto.setOrderDetailId(orderDtls.getOrderDetailsId());
							consMstDto.setOrganisationId(orderDtls.getOrgId());
							consMstDto.setPackageId(mPackage.getPackageMasterId());
							consMstDto.setPackageTypeId(mPackage.getPackageTypeId());
							consMstDto.setPatientId(orderDtls.getPatientId());
							consMstDto.setPayeeId(orderDtls.getPayeeId());
							consMstDto.setRate(mPackage.getPackagePrice());
							consMstDto.setStatus(ACTIVE);
							consMstDto.setUnitId(orderDtls.getOrgUnitId());
							consMstDto.setVisitType(orderDtls.getVisitTypeId());
							consMstDto.setCreatedBy(createdBy);
							consMstDto.setCreatedDate(createdDate);
							consMstDto.setUpdatedBy(updatedBy);
							consMstDto.setUpdatedDate(updatedDate);
							consMstDto.setVisisted(false);
							consMstDto.setBillingBedCategoryId(mPackage.getBillingBedCategoryId());
							consMstDto.setTariffId(mPackage.getTariffId());
							consMstDto.setParentPackageId(mPackage.getParentPackageId());

							Response packConsRes = iTPackageConsumptionMasterDao.saveTPackageConsumptionMaster(consMstDto);
							
							if (packConsRes.getCode().equalsIgnoreCase(SUCCESS_CODE)
									&& packConsRes.getObject() != null) 
							{
								TPackageConsumptionMaster consMaster = (TPackageConsumptionMaster) packConsRes
										.getObject();
								Integer packageConsumptionId = consMaster.getPackageConsumptionMasterId();
								orderDtls.setPackageConsumptionMasterId(packageConsumptionId);
								
								Response packageBbbMpprRes =  	iTPackageConsumptionMasterDao.getBillingBedCategoryIdsByParentPackageId(consMstDto);
								List<PackageBBCategoryMpprDto> mpprList = packageBbbMpprRes.getListObject();
								for (Iterator iterator = mpprList.iterator(); iterator.hasNext();) {
									PackageBBCategoryMpprDto mpprDto = (PackageBBCategoryMpprDto) iterator.next();
									mpprDto.setPackageId(consMstDto.getPackageId());
									mpprDto.setPackageConsumptionMasterId(packageConsumptionId);
									iTPackageConsumptionMasterDao.savePackageBbcMppr(mpprDto);
								}

								Response packServRes = iTPackageServicesDetailsDao
										.getPackageServicesByPackageId(mPackage.getPackageMasterId());
								if (packServRes.getCode().equalsIgnoreCase(SUCCESS_CODE)
										&& packServRes.getListObject() != null
										&& packServRes.getListObject().size() > 0) {

									// save package service wise details
									ListIterator<TPackageServicesDetailsDto> srvItr = packServRes.getListObject()
											.listIterator();
									TPackageConsumptionServiceDetailDto consumpServDtls = null;
									while (srvItr.hasNext()) {
										TPackageServicesDetailsDto servDts = srvItr.next();
										consumpServDtls = new TPackageConsumptionServiceDetailDto();
										consumpServDtls.setApportionedPrice(servDts.getApportionedPrice());
										
										if(servDts.getNumberToBeUse()!=null)
										consumpServDtls.setBalancePackageQuantity(
												mPackage.getPackageTypeId() == 1 ? (servDts.getNumberToBeUse() - 1)
														: servDts.getNumberToBeUse());
										
										consumpServDtls.setIsServiceItem(servDts.getIsServiceItem());
										consumpServDtls.setItemId(servDts.getItemId());
										consumpServDtls.setOrganisationId(orderDtls.getOrgId());
										consumpServDtls.setPackageConsumptionMasterId(packageConsumptionId);
										consumpServDtls.setPackageEoDetailId(servDts.getPackageEitherOrGroupId());
										consumpServDtls.setPackageQuantity(servDts.getNumberToBeUse());
										consumpServDtls.setServiceId(servDts.getServiceId());
										consumpServDtls.setStatus(ACTIVE);
										consumpServDtls.setUnitId(orderDtls.getOrgUnitId());
										consumpServDtls.setCreatedBy(createdBy);
										consumpServDtls.setCreatedDate(createdDate);
										consumpServDtls.setUpdatedBy(updatedBy);
										consumpServDtls.setUpdatedDate(updatedDate);
										consumpServDtls.setIsCap(servDts.getIsCap());
										consumpServDtls.setServiceCap(servDts.getServiceCap());
										consumpServDtls.setAvailableCap(servDts.getServiceCap());
										consumpServDtls.setIsInclusiveExclusive(2);
										consumpServDtls.setProductCategoryId(servDts.getProductCategoryId());
										iTPackageConsumptionServiceDetailDao.saveTPackageConsumptionServiceDetail(consumpServDtls);
									}
								}
							}
						}
					}
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
		logger.error("savePackageConsumptionForDirectEHCEncounter failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getEncountersDetailsForCanelledBills(Integer encounterId) {
		try {
			return iEncounterMasterDao.getEncountersDetailsForCanelledBills(encounterId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response appendCensentData(PatientConsentMapperDto dto) throws ApplicationException {
		try {
			
			Pattern pattern = Pattern.compile("@\\w+");
			List<String> paramNames = new ArrayList<>();
			Matcher matcher = pattern.matcher(dto.getTemplateDesc());
			List<PatientConsentMapperDto> appointmentStatusMasterDtoList  = new ArrayList<>();
			String htmlContent = dto.getTemplateDesc();
			Integer nullFlag = 0;
			
			while (matcher.find())
			{
			    paramNames.add(matcher.group());
			}
			if(paramNames.size()>0)
			{
				Response res = iEncounterMasterDao.getConsentParamDetails(paramNames);
				List<PatientConsentMapperDto> listDto = res.getListObject();
				Map<String,String> qMap = new HashMap<>();
				Map<String,String> qMapResults = new HashMap<>();
				
				for(PatientConsentMapperDto tDto : listDto)
				{
					qMap.put(tDto.getCode(), tDto.getQuery());
				}
				
					for(String q : paramNames)
					{
						if (qMap.containsKey(q))
						{
							
							if(qMapResults.containsKey(q))
							{
								htmlContent = htmlContent.replace(q, qMapResults.get(q));
							}else{
								
								String query = qMap.get(q);
								String result = "";
								if(dto.getPatientId()!=null)
									query = query.replace(":patientId", dto.getPatientId().toString());
								
								if(dto.getEncounterId()!=null)
									query = query.replace(":encounterId", dto.getEncounterId().toString());
								
								if(dto.getAdmissionId()!=null)
									query = query.replace(":admissionId", dto.getAdmissionId().toString());
								
								if(dto.getOrderDetailsId()!=null)
									query = query.replace(":orderDetailsId", dto.getOrderDetailsId().toString());
								
								if(dto.getLabSampleDtlsId()!=null)
									query = query.replace(":labSampleDtlsId", dto.getLabSampleDtlsId().toString());
								
								if(dto.getConsentMasterId() != null)
									query = query.replace(":consentMasterId", dto.getConsentMasterId().toString());
								
								if(dto.getConsentTypeId() != null)
									query = query.replace(":consentTypeMasterId", dto.getConsentTypeId().toString());
								if(dto.getReportEntryBy() != null)
									query = query.replace(":reportEntryBy", dto.getReportEntryBy().toString());
								if(dto.getValidatedBy() != null)
									query = query.replace(":validatedBy", dto.getValidatedBy().toString());
								
								
								if(dto.getConsentMasterId() != null ) {
									
									if(dto.getEncounterId()==null)
										query = query.replace(":encounterId",nullFlag.toString());
									
									if(dto.getAdmissionId()==null)
										query = query.replace(":admissionId", nullFlag.toString());
								}
								if(dto.getBatchId() != null){
									if(dto.getBonusBatchFlag()!=null && dto.getBonusBatchFlag() && q.toString().equalsIgnoreCase("@inventoryBatchNo")){
										int id= Integer.parseInt(dto.getGrnId() != null ? dto.getGrnId() : null);
										query = "select "
												+ "tgrnd.batch_no as \"grnBatchNo\", tgbbe.batch_no as \"batchNo\","
												+ " to_char (tgbbe.expiry_date ,'DD-MM-YYYY HH:mm:ss') as \"bonusBatchExpiry\""
												+ " from procurement.t_good_receipt_note_detail tgrnd"
												+ " left join procurement.t_grn_bonus_batch_expiry tgbbe on tgbbe.grn_detail_id = tgrnd.id"
												+ " where tgrnd.id = "+ id;
										
									}
									else if ((dto.getBarcodeScreen()!=null && dto.getBarcodeScreen() && q.toString().equalsIgnoreCase("@inventoryBatchNo"))){
										int id= Integer.parseInt(dto.getGrnId() != null ? dto.getGrnId() : null);
										query = "select "
												+ " tgbbe.batch_no as \"grnBatchNo\","
												+ " to_char (tgbbe.expiry_date ,'DD-MM-YYYY HH:mm:ss') as \"bonusBatchExpiry\""
												+ " from procurement.t_grn_bonus_batch_expiry tgbbe "
												+ " where tgbbe.grn_bonus_batch_expiry_id = "+ id;
									}
									else{
										query = query.replace(":batchId", dto.getBatchId().toString());
									}
									
								}
									
									
								
								if(q.toString().equalsIgnoreCase("@wristBandPrnSize") && q!=null)
								{
									result = dto.getWristBandPrnSize();
									htmlContent = htmlContent.replace(q, result);
								}
								else if(q.toString().equalsIgnoreCase("@prnCount") && q!=null)
								{
									result = dto.getPrnCount();
									htmlContent = htmlContent.replace(q, result);
								}
								else
								{
									if((dto.getBonusBatchFlag()!=null && dto.getBonusBatchFlag() && q.equalsIgnoreCase("@inventoryBatchNo")) || (dto.getBarcodeScreen()!=null && dto.getBarcodeScreen() && q.toString().equalsIgnoreCase("@inventoryBatchNo"))){
										
										String grnBatchNo = "";
										String batchNos ="";
										appointmentStatusMasterDtoList = sessionFactory.getCurrentSession()
										.createSQLQuery(query)
										.setResultTransformer(Transformers.aliasToBean(PatientConsentMapperDto.class)).list();
										
										if(appointmentStatusMasterDtoList.size()>0){
											
										for(int i=0; i<appointmentStatusMasterDtoList.size(); i++){
											 if(i<appointmentStatusMasterDtoList.size()-1){
												 if(i==0){
													 grnBatchNo = appointmentStatusMasterDtoList.get(0).getGrnBatchNo();
												 }
												 if(!grnBatchNo.equalsIgnoreCase(appointmentStatusMasterDtoList.get(i).getBatchNo().toString()))
												 batchNos += appointmentStatusMasterDtoList.get(i).getBatchNo()+"|";
											 }
											 else{
												 if(i==0){
													 grnBatchNo = appointmentStatusMasterDtoList.get(0).getGrnBatchNo();
												 }
												if(!dto.getBarcodeScreen()){
												 if(!grnBatchNo.equalsIgnoreCase(appointmentStatusMasterDtoList.get(i).getBatchNo().toString()))
												 batchNos += appointmentStatusMasterDtoList.get(i).getBatchNo();
											   }
											 }
											
										}
										if(batchNos != ""){
											grnBatchNo = grnBatchNo +"|"+ batchNos;
										}
										
										dto.setBonusBatchFlag(false);
										htmlContent = htmlContent.replace(q, grnBatchNo);
									}
//									
								  }else{
									result = iEncounterMasterDao.getResult(query);
									String batchExpiry = "";
									 if((dto.getBonusBatchFlag()!=null && dto.getBonusBatchFlag() && q.equalsIgnoreCase("@inventoryBatchExpiry")) || (dto.getBarcodeScreen()!=null && dto.getBarcodeScreen() && q.toString().equalsIgnoreCase("@inventoryBatchExpiry"))){
										 for(int i=0; i<appointmentStatusMasterDtoList.size(); i++){
											 if(dto.getBarcodeScreen() && appointmentStatusMasterDtoList.size()>0){
												 batchExpiry = appointmentStatusMasterDtoList.get(0).getBonusBatchExpiry();
											 }
										 else
										   {
											 if(i<appointmentStatusMasterDtoList.size()-1){
												 if(!appointmentStatusMasterDtoList.get(0).getGrnBatchNo().equalsIgnoreCase(appointmentStatusMasterDtoList.get(i).getBatchNo().toString()))
													 batchExpiry += appointmentStatusMasterDtoList.get(i).getBonusBatchExpiry()+"|";
											 }
											 else{
												 if(!appointmentStatusMasterDtoList.get(0).getGrnBatchNo().equalsIgnoreCase(appointmentStatusMasterDtoList.get(i).getBatchNo().toString()))
												 batchExpiry += appointmentStatusMasterDtoList.get(i).getBonusBatchExpiry();
											 }
										  }
										 }
										 if(dto.getBarcodeScreen()!=null && dto.getBarcodeScreen() && appointmentStatusMasterDtoList.size()>0){
											 result = batchExpiry;
											}
										 else
										 result = result +"|"+ batchExpiry;
									}
									 if((q.equalsIgnoreCase("@inventoryMrp") || q.equalsIgnoreCase("@inventorySellingPrice")) && !result.isEmpty()){
										 try{
											 double d = Double.parseDouble(result);
											 result = String.format("%.2f", d);
										 }
										 catch (Exception e){
										 logger.error("appendCensentData: numeric formatting failed for placeholder " + q, e);
										 }
									 }
									 
									htmlContent = htmlContent.replace(q, result);
								  }
								}
								qMapResults.put(q, result);
							}
							
						}else{
							htmlContent = htmlContent.replace(q, " - ");
						}
					}
					
					dto.setTemplateDesc(htmlContent);
					return new Response(SUCCESS, null, COMMON_SUCCESS, null, dto,qMapResults);
				
			}else{
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, dto);
			}
			
		} catch (Exception e) {
		logger.error("appendCensentData failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSystemGeneratedVisitTypeIdForApp(ScheduleAppointmentReqDto scheduleAppointmentReqDto)
			throws ApplicationException {
		try {
			Integer visitTypeId=null;
			Response localIdsRes= iGlobalUnitMasterDao.getAllLocalIdsByGlobalIds(scheduleAppointmentReqDto);
			if(localIdsRes.getObject()!=null) {
				ScheduleAppointmentReqDto scheduleAppointmentReqDto1=(ScheduleAppointmentReqDto)localIdsRes.getObject();
				SlotMasterDto dto=new SlotMasterDto();
					dto.setOrganizationId(scheduleAppointmentReqDto1.getHospitalId());
					dto.setUnitId(scheduleAppointmentReqDto1.getUnitId());
					dto.setPatientId(scheduleAppointmentReqDto1.getPatientId());
					dto.setDoctorId(scheduleAppointmentReqDto1.getDoctorId());
					dto.setSpecialityId(scheduleAppointmentReqDto.getDepartmentId());
					dto.setCurrentDate(scheduleAppointmentReqDto.getAppointmentDate());
				visitTypeId=assignVisitTypeIdAccordingToConditions(dto);
			}else {
				//if patient not found for org means it is new patient
				visitTypeId=1;
			}
			
			if(visitTypeId!=null) {
				return new Response(SUCCESS, null, null, null, visitTypeId==3?1:visitTypeId);
			}else {
				return new Response(ERROR, null, null, null, null);
			}
		} catch (Exception e) {
		logger.error("getSystemGeneratedVisitTypeIdForApp failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
   @Transactional
	public Response getAdmissionsById(int admissionId) throws BillingException {
		try {
			return iEncounterMasterDao.getAdmissionsById(admissionId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response checkFeedBackFormSubmitted(TemplateTransactionDto dto) throws ApplicationException {
		try {
			 Response feedBackFormRes = iEncounterMasterDao.checkFeedBackFormSubmitted(dto);
			 if(feedBackFormRes.getObject()!=null)
			 {
				 dto = (TemplateTransactionDto) feedBackFormRes.getObject();
				 if(!dto.isFeedbackFormSubmitted())
				 {
					 dto.setTemplateTypeId(23);
					 iTemplateTransactionService.sendTemplateURLS(dto);
				 }
				 return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
			 }
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	@Transactional
	public Response updateBackFormSubmitted(TemplateTransactionDto dto) throws ApplicationException {
		try {
			return iEncounterMasterDao.updateBackFormSubmitted(dto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response updateFollowUpDateOfPatient(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.updateFollowUpDateOfPatient(encounterMasterDto);
		} catch (Exception e) {
		logger.error("updateFollowUpDateOfPatient failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

    @Override
    @Transactional
    public Response getSearchedListEncounterMasterorNursingAssToken(SlotMasterDto slotMasterDto)
            throws ApplicationException {
        try {
            return iEncounterMasterDao.getSearchedListEncounterMasterForNursingAssToken(slotMasterDto);
        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
        
    }

    @Override
    @Transactional
    public Response universalSearchOpPatientListByDocId(EncounterMasterDto encounterMasterDto)
            throws ApplicationException {
        try {
            return iEncounterMasterDao.universalSearchOpPatientListByDocId(encounterMasterDto);
        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
        
    }

    @Override
    @Transactional
    public Response getUniversalSearchedListEncounterMaster(SlotMasterDto slotMasterDto) throws ApplicationException {
        try {
            return iEncounterMasterDao.getUniversalSearchedListEncounterMaster(slotMasterDto);
        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
        
    }

	@Override
	@Transactional
	public EncounterMasterDto getEncounterDetailsForUpdatePackage(Integer encounterId) {
		return iEncounterMasterDao.getEncounterDetailsForUpdatePackage(encounterId);
	}

	@Override
	@Transactional
	public Response updateEncounterDetailsPatientByEncounterId(EncounterMasterDto encounterMasterDto)
			throws ApplicationException {
        try {
            return iEncounterMasterDao.updateEncounterDetailsPatientByEncounterId(encounterMasterDto);
        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
        
    }

	@Override
	@Transactional
	public Response getWardAndFloorAndConsultingDoctorDetails(RadiologyOrdersListDto dto) throws ApplicationException {
		 try {
	            return iEncounterMasterDao.getWardAndFloorAndConsultingDoctorDetails(dto);
	        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	        
	}

	@Override
	@Transactional
	public Response getPatientClassIdByEncounter(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		 try {
	            return iEncounterMasterDao.getPatientClassIdByEncounter(encounterMasterDto);
	        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	        
	}

	@Override
	public Response getListOfEncountersByPatientIdForWalkinSale(EncounterMasterDto encounterMasterDto)
			throws ApplicationException {
		try {
			return iEncounterMasterDao.getListOfEncountersByPatientIdForWalkinSale(encounterMasterDto);
		} catch (Exception e) {
		logger.error("getListOfEncountersByPatientIdForWalkinSale failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getFollowUpDateEncounterList(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		 try {
	            return iEncounterMasterDao.getFollowUpDateEncounterList(encounterMasterDto);
	        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	        
	}

	@Override
	public Response updateFollowUpDateEncounterRemark(EncounterMasterDto encounterMasterDto)
			throws ApplicationException {
		 try {
	            return iEncounterMasterDao.updateFollowUpDateEncounterRemark(encounterMasterDto);
	        } catch (Exception e) {
	        logger.error("updateFollowUpDateEncounterRemark failed", e);
	        }
	        return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getLastEncounteredListOfPatient(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
            return iEncounterMasterDao.getLastEncounteredListOfPatient(encounterMasterDto);
        } catch (Exception e) {
        logger.error("getLastEncounteredListOfPatient failed", e);
        }
        return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response updateLatestCount(SlotMasterDto dto) throws ApplicationException {
		try {
            return iEncounterMasterDao.updateLatestCount(dto);
        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
        
	}

	@Override
	@Transactional
	public Response updateDoctorLatestCount(SlotMasterDto dto) throws ApplicationException {
		try {
            return iEncounterMasterDao.updateDoctorLatestCount(dto);
        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
        
	}

	@Override
	public Response getLastEncounterDataOfPatientForWebAPP(EncounterMasterDto encounterMasterDto)
			throws ApplicationException {
		try {
			if(encounterMasterDto.getEncounterId() != null) {
               return iEncounterMasterDao.getLastEncounterDataOfPatientForWebAPP(encounterMasterDto);
			}else {
				 return iEncounterMasterDao.getLastAdmisionDataOfPatientForWebAPP(encounterMasterDto);
			}
        } catch (Exception e) {
        logger.error("getLastEncounterDataOfPatientForWebAPP failed", e);
        }
        return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getSubSpecialityById(Integer serviceId) throws ApplicationException {
		try {
            return iEncounterMasterDao.getSubSpecialityById(serviceId);
        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
        
	}

	@Override
	public Response getReferalDetailsByEncounterId(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
            return iEncounterMasterDao.getReferalDetailsByEncounterId(encounterMasterDto);
        } catch (Exception e) {
        logger.error("getReferalDetailsByEncounterId failed", e);
        }
        return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response PatientDocStatusUpdate(PatientDocReqDto patientDocReqDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.PatientDocStatusUpdate(patientDocReqDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getPackageOrderCount(OrderDetailsMasterDto ordDtlsDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.getPackageOrderCount(ordDtlsDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getOpPatientListByDocIdForVideo(AppointmentStatusSyncDto encounterMasterDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.getOpPatientListByDocIdForVideo(encounterMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	@Override
	@Transactional
	public Response getPatientDocDetailsForNabh(PatientDocReqDto patientDocReqDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.getPatientDocDetailsForNabh(patientDocReqDto);
		}catch (Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	public Response saveDocumentAgainstNabh(EncounterDocDetailsDto encounterDocDetailsDto, MultipartFile[] files) throws ApplicationException{
		try {
				
			for (MultipartFile file : files) {
				encounterDocDetailsDto.setFileName(file.getOriginalFilename());
			Date date = TimeZoneComponent.getDateByzone(encounterDocDetailsDto.getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");

			if (encounterDocDetailsDto.getUnitId() > 0) 
				
			{
				Response orgRes = iGlobalOrganisationMasterDao.getOrganiazationFolderPath(encounterDocDetailsDto.getOrganisationId());
				OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();
				String docPath = "";
				if (encounterDocDetailsDto.getDocumentTypeId() == 19) 
				{
					docPath = docUploadUtility.uploadCorporateDoc(file,organizationMasterDto.getOrganizationFolder(), encounterDocDetailsDto);
				} else {
					docPath = docUploadUtility.uploadEncounterDoc(file,organizationMasterDto.getOrganizationFolder(), encounterDocDetailsDto);
				}
				encounterDocDetailsDto.setDocumentPath(docPath);
				if(!encounterDocDetailsDto.getCreatedDate().equals("null") && !encounterDocDetailsDto.getCreatedDate().equals("")) {
					encounterDocDetailsDto.setCreatedDate(encounterDocDetailsDto.getCreatedDate());
				}else {
	                encounterDocDetailsDto.setCreatedDate(dateTime);
				}
				
				encounterDocDetailsDto.setUpdatedDate(dateTime);
				encounterDocDetailsDto.setUploadDoccumentDate(encounterDocDetailsDto.getUploadDoccumentDate());
				iEncounterMasterDao.saveDocumentAgainstEncounterForNbh(encounterDocDetailsDto);
			}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e){
		logger.error("saveDocumentAgainstNabh failed", e);
			 String errorMsg =  EncounterMasterServiceImpl.class.getName() + ":" +e.getMessage();
			 return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
		}
	}

	@Override
	public Response saveDocumentAgainstIncident(EncounterDocDetailsDto encounterDocDetailsDto, MultipartFile[] files) throws ApplicationException{
		try {

			for (MultipartFile file : files) {
				encounterDocDetailsDto.setFileName(file.getOriginalFilename());
				Date date = TimeZoneComponent.getDateByzone(encounterDocDetailsDto.getUnitId());
				String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");

				if (encounterDocDetailsDto.getUnitId() > 0)

				{
					Response orgRes = iGlobalOrganisationMasterDao.getOrganiazationFolderPath(encounterDocDetailsDto.getOrganisationId());
					OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();
					String docPath = "";
					if (encounterDocDetailsDto.getDocumentTypeId() == 19)
					{
						docPath = docUploadUtility.uploadCorporateDoc(file,organizationMasterDto.getOrganizationFolder(), encounterDocDetailsDto);
					} else {
						docPath = docUploadUtility.uploadEncounterDoc(file,organizationMasterDto.getOrganizationFolder(), encounterDocDetailsDto);
					}
					encounterDocDetailsDto.setDocumentPath(docPath);
					if(!encounterDocDetailsDto.getCreatedDate().equals("null") && !encounterDocDetailsDto.getCreatedDate().equals("")) {
						encounterDocDetailsDto.setCreatedDate(encounterDocDetailsDto.getCreatedDate());
					}else {
						encounterDocDetailsDto.setCreatedDate(dateTime);
					}

					encounterDocDetailsDto.setUpdatedDate(dateTime);
					encounterDocDetailsDto.setUploadDoccumentDate(encounterDocDetailsDto.getUploadDoccumentDate());
					iEncounterMasterDao.saveDocumentAgainstEncounterForIncident(encounterDocDetailsDto);
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e){
		logger.error("saveDocumentAgainstIncident failed", e);
			String errorMsg =  EncounterMasterServiceImpl.class.getName() + ":" +e.getMessage();
			return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
		}
	}

	@Override
	@Transactional
	public Response getPatientDocDetailsForIncident(PatientDocReqDto patientDocReqDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.getPatientDocDetailsForIncident(patientDocReqDto);
		}catch (Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response getServiceDetails(List<Integer> serviceIds, Integer unitId) throws ApplicationException {
		try {
			return iEncounterMasterDao.getServiceDetails(serviceIds,unitId);
		}catch (Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response analyzeReportsWithAI(HttpServletRequest servletRequest, EncounterDocDetailsDto encounterDocDetailsDto) throws ApplicationException {
		try {

			String analyzeUrl = iEncounterMasterDao.getDocumentAnalyzeUrl(encounterDocDetailsDto);
			if(analyzeUrl != null) {
				StringBuffer requestURL = servletRequest.getRequestURL();
				String domain = servletRequest.getScheme() + "://" + servletRequest.getServerName() +
						(servletRequest.getServerPort() == 80 || servletRequest.getServerPort() == 443 ? "" : ":" + servletRequest.getServerPort());

//				domain = "https://75d2-219-91-251-54.ngrok-free.app";

				JSONArray jsonArray = new JSONArray();
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("fileurl", encounterDocDetailsDto.getDocumentPath());
				jsonObject.put("callbackurl", domain +encounterDocDetailsDto.getCallBackBaseUrl() + "/saveAnalyzedReport");
				jsonObject.put("type", "Lab");
				jsonObject.put("encounterDocId", encounterDocDetailsDto.getEncounterDocId());
				jsonArray.put(jsonObject);

				String requestJson = jsonArray.toString().replaceAll("[\\[\\]]", "");

				OkHttpClient client = new OkHttpClient().newBuilder().build();
				MediaType mediaType = MediaType.parse("application/json");
				RequestBody body = RequestBody.create(mediaType, requestJson);
				Request request = new Request.Builder()
						.url(analyzeUrl)
						.method("POST", body)
						.addHeader("Content-Type", "application/json")
						.addHeader("x-api-key", "yXLnxTVdmo6uGxHKGnAB21IzyxXMtWSCztp_twH8FqPmwmQ9CNbbT3ZxRQxahSHJ")
						.build();
				okhttp3.Response response = client.newCall(request).execute();

				String responseBody = response.body().string();

				if (response.isSuccessful() && response.body() != null) {
//					System.out.println(responseBody);
//					JSONObject jsonObject1 = new JSONObject(responseBody);
//					System.out.println(jsonObject1.toString(4));
					return new Response<>(SUCCESS, SUCCESS_CODE, "Document sent for analysis", null, responseBody);
				} else {
//					System.out.println("Request failed: " + response.code());
//					System.out.println(responseBody);
					return new Response<>(ERROR, String.valueOf(response.code()), "Unable to send for analysis", null, responseBody);
				}
			}
			return new Response<>(ERROR, ERROR_CODE, "Analyze url not found!", null, null);
		}catch (Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response saveAnalyzedReport(EncounterDocDetailsDto encounterDocDetailsDto, String json) throws ApplicationException {
		try {
			return iEncounterMasterDao.saveAnalyzedReport(encounterDocDetailsDto, json);
		}catch (Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response getAnalyzedReport(EncounterDocDetailsDto encounterDocDetailsDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.getAnalyzedReport(encounterDocDetailsDto);
		}catch (Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public void updateEncounterStopTime(EncounterAssignmenMapperDto mpprDto) throws ApplicationException {
		try {
			 iEncounterMasterDao.updateEncounterStopTime(mpprDto);
		}catch (Exception e){
		throw new ApplicationException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional
	public Response updateAiSummaryAndQuestion(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		return iEncounterMasterDao.updateAiSummaryAndQuestion(encounterMasterDto);
	}

	@Override
	@Transactional
	public Response getAugmentDoctorList(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.getAugmentDoctorList(encounterMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response updateAugmentDoctor(EncounterMasterDto encounterMasterDto) throws ApplicationException {
		try {
			return iEncounterMasterDao.updateAugmentDoctor(encounterMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
}
