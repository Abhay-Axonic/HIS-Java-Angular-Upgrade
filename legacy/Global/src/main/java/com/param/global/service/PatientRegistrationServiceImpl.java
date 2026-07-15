package com.param.global.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.param.global.dao.*;
import com.param.global.dto.*;
import com.param.global.dto.global.Gender;
import com.param.global.dto.sync.PatientMasterDto;
import com.param.global.dto.sync.PatientSyncDto;
import com.param.global.dto.sync.ScheduleAppointmentReqDto;
import com.param.global.model.*;
import com.param.global.org.dto.UnitMasterDto;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.json.JSONObject;
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
import com.param.global.common.DocUploadUtility;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ScheduleActions;
import com.param.global.common.ScheduleTables;
import com.param.global.common.ServiceException;
import com.param.global.dao.ICityMasterDao;
import com.param.global.dao.IDistrictMasterDao;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IKinDetailsDao;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dao.IOrderMasterDao;
import com.param.global.dao.IPatientRegistrationDao;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dao.IStateMasterDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dao.IUnknownPatientRegistrationDao;
import com.param.global.dto.AreaMasterDto;
import com.param.global.dto.CityMasterDto;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.global.dto.CustomDataPostDto;
import com.param.global.dto.CustomPatientDetailsDto;
import com.param.global.dto.DistrictMasterDto;
import com.param.global.dto.FingerPrintSearchDto;
import com.param.global.dto.InvestigationOrderMasterNewDto;
import com.param.global.dto.KinDetailsDto;
import com.param.global.dto.NewONPRegistrationDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.PatientDetailsDto;
import com.param.global.dto.PatientRegistrationDocDetailsDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.dto.PatientSearchDto;
import com.param.global.dto.PatientVisitsDto;
import com.param.global.dto.ScheduleLogsDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.StateMasterDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.global.InstructionMasterDto;
import com.param.global.folder.creation.BucketUtility;
import com.param.global.model.StateMaster;
import com.param.global.opd.dao.IAppointmentSchedulingDao;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.global.org.common.dao.IInstructionMasterDao;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.unit.dto.AllergyPatientMapperDto;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.opd.appointment.model.AppointmentMaster;
import com.param.opd.coversheet.dao.IAllergyPatientMapperDao;
import com.param.opd.coversheet.dao.IAppointmentVitalMapperDao;
import com.param.opd.coversheet.dao.IComplaintAppointmentMapperDao;
import com.param.opd.coversheet.dao.IConsultDoctorDao;
import com.param.opd.coversheet.dao.ICoversheetInstructionDao;
import com.param.opd.coversheet.dao.ICoversheetTemaplateDao;
import com.param.opd.coversheet.dao.IDoctorFavoritesDao;
import com.param.opd.coversheet.dao.IMedicationDAO;
import com.param.opd.coversheet.dao.IMedicationHistoryDao;
import com.param.opd.coversheet.dao.IObservationDao;
import com.param.opd.coversheet.dao.IPatientDiagnosisDao;
import com.param.opd.coversheet.dao.IPatientFamilyHistoryDao;
import com.param.opd.coversheet.dao.IPatientHabitDetailsMapperDao;
import com.param.opd.coversheet.dao.IPatientMedicalHistoryDao;
import com.param.opd.coversheet.dao.IPatientPersonalHistoryDetailsDao;
import com.param.opd.coversheet.dao.IPatientSurgicalHistoryDao;
import com.param.opd.coversheet.dao.IPatientTravelHistoryDetailsDao;
import com.param.opd.coversheet.dao.IPatientTreatmentHistoryDetailsDao;
import com.param.opd.coversheet.dto.AppointmentVitalMapperDto;
import com.param.opd.coversheet.dto.ComplaintAppointmentDto;
import com.param.opd.coversheet.dto.ComplaintAppointmentMapperDto;
import com.param.opd.coversheet.dto.ConsultDoctorDto;
import com.param.opd.coversheet.dto.CoversheetInstructionDto;
import com.param.opd.coversheet.dto.DiagnosisPatientAppointmentMapperDto;
import com.param.opd.coversheet.dto.DoctorFavoritesDto;
import com.param.opd.coversheet.dto.MedicationDTO;
import com.param.opd.coversheet.dto.MedicationHistoryDto;
import com.param.opd.coversheet.dto.ObservationDto;
import com.param.opd.coversheet.dto.PatientFamilyHistoryDto;
import com.param.opd.coversheet.dto.PatientHabitDetailsDto;
import com.param.opd.coversheet.dto.PatientMedicalHistoryDto;
import com.param.opd.coversheet.dto.PatientPersonalHistoryDetailsDto;
import com.param.opd.coversheet.dto.PatientSurgicalHistoryDto;
import com.param.opd.coversheet.dto.PatientTravelHistoryDetailsDto;
import com.param.opd.coversheet.dto.PatientTreatmentHistoryDetailsDto;
import com.param.opd.coversheet.dto.PrescriptionDetailsDto;
import com.param.opd.coversheet.dto.PrescriptionDto;
import com.param.opd.coversheet.service.IPatientDiagnosisService;
import com.param.opd.encounter.dto.EncounterDocDetailsDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked","unused"})
public class PatientRegistrationServiceImpl implements IPatientRegistrationService, ICommonConstants,IBillingCommonConstants{

	private static final Logger logger = Logger.getLogger(PatientRegistrationServiceImpl.class);

	@Autowired
	private IPatientRegistrationDao iPatientRegistrationDao;
	
	@Autowired
	private IOrderMasterService iOrderMasterService; 
	
	@Autowired
    IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;
	
	@Autowired 
	IUnknownPatientRegistrationDao iUnknownPatientRegistrationDao;
    
    @Autowired
    BucketUtility bucketUtility;
    
    @Autowired
    DocUploadUtility docUploadUtility;
    
    @Autowired
    IKinDetailsDao iKinDetailsDao;
    
    @Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
    
    @Autowired
    IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
    
    @Autowired
	private IScheduleLogsDao isheduleDao;
	
    @Autowired
    @Qualifier("applicationEventMulticaster")
    ApplicationEventPublisher applicationEventPublisher;
    
    @Autowired
	IAppointmentSchedulingDao iAppointmentSchedulingDao;
    
    @Autowired
	private IAreaMasterService iAreaMaster;
    
    @Autowired
	private ICoversheetTemaplateDao iCoversheetTemaplateDao;
    
    @Autowired
	private IComplaintAppointmentMapperDao iComplaintAppointmentMapperDao;
	
	@Autowired
	private IAppointmentVitalMapperDao iAppointmentVitalMapperDao;
	
	@Autowired
	IPatientDiagnosisDao iPatientDiagnosisDao;
	
	@Autowired
	private IMedicationDAO iMedicationDAO;
	
	@Autowired
	IOrderMasterDao iOrderMasterDao;
	
	@Autowired
	IOrderDetailsMasterDao iOrderDetailsMasterDao;
	
	@Autowired
	ICoversheetInstructionDao iCoversheetInstructionDao;
	
	@Autowired
	IOrderDetailsMasterService iOrderDetailsMasterService; 
	
	@Autowired
	IPatientDiagnosisService iPatientDiagnosisService;
	
	@Autowired
	IInstructionMasterDao iInspectionMasterDao;
	
	@Autowired
	IDoctorFavoritesDao iDoctorFavoritesDao;

	@Autowired
	IScheduleLogsDao iScheduleLogsDao;

	@Autowired
	private IGenderMasterDao iGenderMasterDao;
	
	@Autowired
	ICityMasterDao iCityDao; 
	
	@Autowired
	SessionFactory sessionFactory;
	
    @Autowired
    RestTemplate restTemplate;
    
    @Autowired
    private Gson gson;
    
    @Autowired
    IPatientRegistrationDao iPatientMasterDao;

	@Autowired
	private ICountryMasterService iCountryService;

	@Autowired
	private IStateMasterService iStateMaster;
    
	
	private static final Random random = new Random();
	private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	
	@Autowired
	private IStateMasterDao iStateDao;
	
	@Autowired
	private IDistrictMasterDao iDistrictMaster;

	private Cache<Integer, List<PatientRegistrationDto>> databaseLinkMasterCache;

	@PostConstruct
	public void initCache() {
		databaseLinkMasterCache = CacheBuilder.newBuilder()
				.maximumSize(500)
				.build();
	}
	
	@Override
	@Transactional
	public Response getPatientDetailsById(Integer patientId) throws ApplicationException {
		try {
			return iPatientRegistrationDao.getPatientDetailsById(patientId);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	
	@Override
	@Transactional
	public Response registerPatient(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
		try {
			return this.savePatientRegistration(patientRegistrationDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	@Override
	@Transactional
	public Response savePatientRegistration(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
		try {
			
		String responseMessage = null;
		List<Integer> orderIdsRes = null;
		
		Integer patientRegServiceId = null;	
		UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setOrganizationId(patientRegistrationDto.getOrganizationId());
			unitApplicationConfigurationDto.setUnitId(patientRegistrationDto.getUnitId());
		Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
		UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto)appConfigRes.getObject();
		
		if(unitApplicationConfigurationDtos != null)
		{
			patientRegServiceId=unitApplicationConfigurationDtos.getPatientRegSerId();
			patientRegistrationDto.setSender(unitApplicationConfigurationDtos.getSenderId());
			patientRegistrationDto.setUpdatedBy(unitApplicationConfigurationDtos.getWebAppUserId()!=null ? unitApplicationConfigurationDtos.getWebAppUserId() : patientRegistrationDto.getCreatedBy());
			patientRegistrationDto.setCreatedBy(unitApplicationConfigurationDtos.getWebAppUserId()!=null ? unitApplicationConfigurationDtos.getWebAppUserId(): patientRegistrationDto.getCreatedBy());
		}
		else
			return new Response(ERROR, null, "Unit application configuraion not yet defined.!!", null, null);
		
			Response res = iPatientRegistrationDao.checkUniquePatient(patientRegistrationDto);
			if(patientRegistrationDto.getIsAlreadyExist() == null) {
				patientRegistrationDto.setIsAlreadyExist('N');
			}
			if (res.getListObject().size() > 0 && patientRegistrationDto.getIsAlreadyExist() == 'N')
			{
				List<PatientDetailsDto> getPatientDetaisList = res.getListObject();
				PatientDetailsDto alreadyExist = getPatientDetaisList.get(0);
				return new Response(ERROR, ALREADY_EXIST_CODE, "Patient already exists.."+alreadyExist.getUHIDNumber(), null, alreadyExist.getPatientId());
			} else {
				//<---save patient
				if(patientRegistrationDto.getStateId() != null && patientRegistrationDto.getStateId()== 0)
				{
					StateMasterDto stateMasterDto = new StateMasterDto();
					stateMasterDto.setStateCode(patientRegistrationDto.getStateCode());
		            stateMasterDto.setStateName(patientRegistrationDto.getStateName());
		            stateMasterDto.setCountryId(patientRegistrationDto.getCountryId());
		            stateMasterDto.setStatus('A');
		            stateMasterDto.setCreatedBy(patientRegistrationDto.getCreatedBy());
		            stateMasterDto.setUpdatedBy(patientRegistrationDto.getCreatedBy());
		            stateMasterDto.setOrganizationId(patientRegistrationDto.getOrganizationId());
		            stateMasterDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
		            stateMasterDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
		            
					Response resState = iStateDao.addStateMaster(stateMasterDto);
					
					if(resState.getObject() != null) {
						StateMaster dtoState = new StateMaster();
						dtoState = (StateMaster) resState.getObject();
						patientRegistrationDto.setStateId(dtoState.getStateId());
					}
				}
				
				if(patientRegistrationDto.getDistrictId() != null && patientRegistrationDto.getDistrictId()== 0)
				{
					    DistrictMasterDto districtMasterDto = new DistrictMasterDto();
					    districtMasterDto.setDistrictCode(patientRegistrationDto.getDistrictCode());
			            districtMasterDto.setDistrictName(patientRegistrationDto.getDistrictName());
			            districtMasterDto.setCountryId(patientRegistrationDto.getCountryId());
			            districtMasterDto.setStateId(patientRegistrationDto.getStateId());
			            districtMasterDto.setStatus('A');
			            districtMasterDto.setCreatedBy(patientRegistrationDto.getCreatedBy());
			            districtMasterDto.setUpdatedBy(patientRegistrationDto.getCreatedBy());
			            districtMasterDto.setOrganizationId(patientRegistrationDto.getOrganizationId());
			            districtMasterDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
			            districtMasterDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
		            
		            Response resDist = iDistrictMaster.addDistrictMaster(districtMasterDto);
					
					if(resDist.getObject() != null) {
						DistrictMaster dto = new DistrictMaster();
						dto = (DistrictMaster) resDist.getObject();
						patientRegistrationDto.setDistrictId(dto.getDistrictId());
					}
				}
				
				if(patientRegistrationDto.getCityId() != null && patientRegistrationDto.getCityId()== 0)
				{
					CityMasterDto cityMasterDto = new CityMasterDto();
					cityMasterDto.setCityCode(patientRegistrationDto.getDistrictCode());
					cityMasterDto.setCityName(patientRegistrationDto.getDistrictName());
					cityMasterDto.setCountryId(patientRegistrationDto.getCountryId());
					cityMasterDto.setStateId(patientRegistrationDto.getStateId());
					cityMasterDto.setStatus('A');
					if(patientRegistrationDto.getCreatedBy()!=null) {
						cityMasterDto.setCreatedBy(patientRegistrationDto.getCreatedBy());
						cityMasterDto.setUpdatedBy(patientRegistrationDto.getCreatedBy());
					}
					cityMasterDto.setDistrictId(patientRegistrationDto.getDistrictId());
					cityMasterDto.setOrganizationId(patientRegistrationDto.getOrganizationId());
					cityMasterDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
					cityMasterDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
		            
			            Response resCity= iCityDao.addCityMaster(cityMasterDto);
						if(resCity.getObject() != null) {
							CityMaster dtoCity = new CityMaster();
							dtoCity = (CityMaster) resCity.getObject();
						patientRegistrationDto.setCityId(dtoCity.getCityId());
					}
				}
				if(patientRegistrationDto.getGenderId() == null || patientRegistrationDto.getGenderId() == 0){

					GenderMasterDto gender = new GenderMasterDto();
					if (patientRegistrationDto.getGenderCode() != null
							&& !patientRegistrationDto.getGenderCode().isEmpty()) {

						gender.setCode(
								patientRegistrationDto.getGenderCode()
										.substring(0, 1)
										.toUpperCase()
						);
					}
					//gender.setCode(patientRegistrationDto.getGender());
					gender.setDesc(patientRegistrationDto.getGenderCode());
					gender.setCreatedBy(1);
					gender.setUpdatedBy(1);
					gender.setOrganizationId(1);
					gender.setUpdatedBy(1);
					gender.setStatus('A');
					Response genRes = iGenderMasterDao.getGenderByName(gender);
					if (genRes.getStatus().equals(SUCCESS) && genRes.getListObject() != null && !genRes.getListObject().isEmpty()){
						List<GenderMasterDto> genderList = genRes.getListObject();
						gender = genderList.get(0);
						patientRegistrationDto.setGenderId(gender.getId());
						patientRegistrationDto.setPrefixId(gender.getPreFixId());
					}
					else {
						Response resGender = iGenderMasterDao.addGenderMaster(gender);
						if (resGender.getObject() != null) {
							GenderMaster genderMaster =  (GenderMaster) resGender.getObject();
							patientRegistrationDto.setGenderId(genderMaster.getId());
						}
					}
				}
				
					
				if(patientRegistrationDto.getArea()!=null && patientRegistrationDto.getArea().length()>0 && patientRegistrationDto.getAreaId()==null)
				{
					AreaMasterDto areaMasterDto = new AreaMasterDto();
						areaMasterDto.setAreaName(patientRegistrationDto.getArea());
						areaMasterDto.setAreaCode(patientRegistrationDto.getArea());
						areaMasterDto.setCityId(patientRegistrationDto.getCityId());
						areaMasterDto.setDistrictId(patientRegistrationDto.getDistrictId());
						areaMasterDto.setStateId(patientRegistrationDto.getStateId());
						areaMasterDto.setCountryId(patientRegistrationDto.getCountryId());
						areaMasterDto.setOrganizationId(patientRegistrationDto.getOrganizationId());
						areaMasterDto.setPostCode(patientRegistrationDto.getZipCode());
						areaMasterDto.setCreatedBy(patientRegistrationDto.getCreatedBy());
						areaMasterDto.setUpdatedBy(patientRegistrationDto.getUpdatedBy());
						areaMasterDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
						areaMasterDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
						areaMasterDto.setStatus('A');
					Response areaRes = iAreaMaster.addAreaMaster(areaMasterDto);	
					if(areaRes.getObject()!=null)
					{
						areaMasterDto = (AreaMasterDto)areaRes.getObject();
						patientRegistrationDto.setAreaId(areaMasterDto.getAreaId());
						patientRegistrationDto.setZipCode(areaMasterDto.getPostCode());
						
						if(patientRegistrationDto.getArea().equals(patientRegistrationDto.getPermanentArea()))
						{
							patientRegistrationDto.setPermanentAreaId(areaMasterDto.getAreaId());
							patientRegistrationDto.setPermanentZipCode(areaMasterDto.getPostCode());
						}else{
							AreaMasterDto perAreaMasterDto = new AreaMasterDto();
								perAreaMasterDto.setAreaName(patientRegistrationDto.getPermanentArea());
								perAreaMasterDto.setAreaCode(patientRegistrationDto.getPermanentArea());
								perAreaMasterDto.setCityId(patientRegistrationDto.getPermanentCityId());
								perAreaMasterDto.setDistrictId(patientRegistrationDto.getPermanentDistrictId());
								perAreaMasterDto.setStateId(patientRegistrationDto.getPermanentStateId());
								perAreaMasterDto.setCountryId(patientRegistrationDto.getPermanentCountryId());
								perAreaMasterDto.setOrganizationId(patientRegistrationDto.getOrganizationId());
								perAreaMasterDto.setCreatedBy(patientRegistrationDto.getCreatedBy());
								perAreaMasterDto.setUpdatedBy(patientRegistrationDto.getUpdatedBy());
								perAreaMasterDto.setPostCode(patientRegistrationDto.getPermanentZipCode());
								perAreaMasterDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
								perAreaMasterDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
								perAreaMasterDto.setStatus('A');
							Response perAreaRes = iAreaMaster.addAreaMaster(perAreaMasterDto);	
							if(perAreaRes.getObject()!=null)
							{
								perAreaMasterDto = (AreaMasterDto)perAreaRes.getObject();
								patientRegistrationDto.setPermanentAreaId(perAreaMasterDto.getAreaId());
								patientRegistrationDto.setPermanentZipCode(perAreaMasterDto.getPostCode());
							}
						}
					}
				}
				
				patientRegistrationDto.setIsPrimary('Y');
						String uhidNo = iGlobalUnitMasterDao.getGeneratedGlobalNo(0, patientRegistrationDto.getOrganizationId(),patientRegistrationDto.getUnitId(), generateUhidConfig(patientRegistrationDto.getRegistrationTypeId()));
						if( uhidNo!=null && ! uhidNo.isEmpty() )
	                        patientRegistrationDto.setUhidNumber(uhidNo);
	                    else
	                        patientRegistrationDto.setUhidNumber(generateUhid(patientRegistrationDto.getRegistrationTypeId()));
					
						patientRegistrationDto.setPatientCategoryId((patientRegistrationDto.getPatientCategoryId()!=null ? patientRegistrationDto.getPatientCategoryId() : unitApplicationConfigurationDtos.getDefaultPatientCategoryId()));
				Response res1 = iPatientRegistrationDao.savePatientRegistration(patientRegistrationDto);
				if (res1.getStatus().equals(SUCCESS)) {
					if(patientRegistrationDto.getFamilyId()==null){
						String str= generateFamilyId(patientRegistrationDto.getPatientId());
						patientRegistrationDto.setFamilyId(str);
					}
					Response patientResponse = iPatientRegistrationDao.savePatientDetails(patientRegistrationDto);
					
					
					
					if(patientRegistrationDto.getKinList()!=null && patientRegistrationDto.getKinList().size()>0) 
					{
						if(patientRegistrationDto.getKinDetailsId()!=null && patientRegistrationDto.getKinDetailsId() != 0) 
						{
							for(KinDetailsDto kinDetailDto:patientRegistrationDto.getKinList()) 
							{
								if (!kinDetailDto.getKinName().equals("")) 
								{
									kinDetailDto.setKinDetailsId(patientRegistrationDto.getKinDetailsId());
									kinDetailDto.setPatientId(patientRegistrationDto.getPatientId());
									iKinDetailsDao.updateKinDetails(kinDetailDto);
								}
							}
						}else 
						{
							for(KinDetailsDto kinDetailDto:patientRegistrationDto.getKinList()) 
							{
									kinDetailDto.setPatientId(patientRegistrationDto.getPatientId());
								 iKinDetailsDao.saveKinDetails(kinDetailDto);
							}
						}
					}
					
					
					if(patientResponse.getStatus().equals(SUCCESS)) {
						PatientRegistrationDto patientRegistrationdt= (PatientRegistrationDto)res1.getObject();
						//fingerprint
						if(patientRegistrationDto.getThumbImage()!=null)
						{
							patientRegistrationDto.setPatientId(patientRegistrationdt.getPatientId());
							this.savePatientFingerPrint(patientRegistrationDto);
						}
						
						//Avoiding OTC patient for auto rendered service
						if(patientRegistrationDto.getRegistrationTypeId()==2)
						{
							return new Response(SUCCESS, SUCCESS_CODE, patientRegistrationDto.getUhidNumber()+" "+COMMON_SUCCESS, null, patientRegistrationDto.getPatientId());
						}else 
						{
						
							//-------order master-------//
								
							if(patientRegServiceId!=null)
							{	
								OrderMasterDto orderMasterDto = new OrderMasterDto();
									orderMasterDto.setPatientId(patientRegistrationDto.getPatientId());
									orderMasterDto.setCreatedBy(patientRegistrationDto.getCreatedBy());
									orderMasterDto.setUpdatedBy(patientRegistrationDto.getUpdatedBy());
									orderMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()).getTime());
									orderMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()).getTime());
									orderMasterDto.setOrderDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()).getTime());
									orderMasterDto.setOrderStatus('A');
									orderMasterDto.setOrgId(patientRegistrationDto.getOrganizationId());
									orderMasterDto.setOrgUnitId(patientRegistrationDto.getUnitId());
									orderMasterDto.setOrderStatusId(1);// order status==1
									//-------order details-------//
									OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
										orderDetailsMasterDto.setServiceId(patientRegServiceId);
										orderDetailsMasterDto.setServiceIsBilled(0);
										orderDetailsMasterDto.setCreatedBy(patientRegistrationDto.getCreatedBy());
										orderDetailsMasterDto.setUpdatedBy(patientRegistrationDto.getUpdatedBy());
										orderDetailsMasterDto.setOrdRenderDatetime(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()).getTime());
										orderDetailsMasterDto.setOrderDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()).getTime());
										orderDetailsMasterDto.setOrderSchDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()).getTime());
										orderDetailsMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()).getTime());
										orderDetailsMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()).getTime());
										orderDetailsMasterDto.setStatus('A');
										orderDetailsMasterDto.setOrgId(patientRegistrationDto.getOrganizationId());
										orderDetailsMasterDto.setOrgUnitId(patientRegistrationDto.getUnitId());
										orderDetailsMasterDto.setOrderQty(1);
										orderDetailsMasterDto.setOrdCancelled('N');
									List orderDetailsList = new ArrayList();
									orderDetailsList.add(orderDetailsMasterDto);
									orderMasterDto.setListOrderDetailsMasterDto(orderDetailsList);
								Response orderSaveRes = iOrderMasterService.saveOrderMaster(orderMasterDto);
								responseMessage = COMMON_SUCCESS;
								orderIdsRes = orderSaveRes.getListObject();
							}else{
								responseMessage = COMMON_SUCCESS + "Without placing registartion service !!";
							}
							if(patientRegistrationDto.getIsFromSelf()!=null)
							{
								SlotMasterDto appointmentObj = new SlotMasterDto();
									appointmentObj.setPatientId(patientRegistrationDto.getPatientId());
									appointmentObj.setOrganizationId(patientRegistrationDto.getOrganizationId());
									appointmentObj.setUnitId(patientRegistrationDto.getUnitId());
									appointmentObj.setCreatedDate(patientRegistrationDto.getCreatedDate());
									appointmentObj.setUpdatedDate(patientRegistrationDto.getCreatedDate());
									appointmentObj.setUpdatedBy(patientRegistrationDto.getUpdatedBy());
									appointmentObj.setCreatedBy(patientRegistrationDto.getCreatedBy());
									appointmentObj.setAppointmentStatusId(APPOINTMENTSTATUSID1); //Pending
									appointmentObj.setAppointmentTypeId(APPOINTMENTTYPEID1); //temporary 
									appointmentObj.setArrivalTime(patientRegistrationDto.getCreatedDate());
									appointmentObj.setTokenNumber(iAppointmentSchedulingDao.getTokenNumber(patientRegistrationDto.getAppointmentDate().substring(0,10), patientRegistrationDto.getUnitId()));
									appointmentObj.setAppointmentDate(patientRegistrationDto.getAppointmentDate());
									appointmentObj.setStatus('A');
								
								Response res2 = iAppointmentSchedulingDao.scheduleAppointment(appointmentObj);
								if(res2.getStatus().equals(ERROR))
									return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
								
								/*
								 * AppointmentMaster appointmentMaster = null;
								 * if(res2.getCode().equals(SUCCESS_CODE) && res2.getObject() != null){
								 * appointmentMaster = (AppointmentMaster)res2.getObject(); try{
								 * syncAppointmentForMyLife(appointmentMaster,appointmentObj);
								 * syncAppointmentDetailForMyLife(appointmentMaster,appointmentObj);
								 * }catch(Exception e){  } }
								 */
								//return new Response(SUCCESS, SUCCESS_CODE, patientRegistrationDto.getUhidNumber()+" "+responseMessage, orderIdsRes, res2.getObject());
								return new Response(SUCCESS, SUCCESS_CODE,responseMessage, orderIdsRes, res2.getObject());
							}
							
							
							if(patientRegistrationDto.getAppointmentId()!=null){
								
									iAppointmentSchedulingDao.assignPatientToAppointmentFromPatientRegistration(patientRegistrationDto.getAppointmentId(),patientRegistrationDto.getPatientId());
									patientRegistrationDto.setAppointmentTypeId(APPOINTMENTTYPEID1);
								//return new Response(SUCCESS, SUCCESS_CODE, patientRegistrationDto.getUhidNumber()+" "+responseMessage, orderIdsRes, patientRegistrationDto);
									return new Response(SUCCESS, SUCCESS_CODE,responseMessage, orderIdsRes, patientRegistrationDto);
								
							}
							
							//return new Response(SUCCESS, SUCCESS_CODE, patientRegistrationDto.getUhidNumber()+" "+responseMessage, orderIdsRes, patientRegistrationDto.getPatientId(),patientRegistrationDto);
							return new Response(SUCCESS, SUCCESS_CODE, responseMessage, orderIdsRes, patientRegistrationDto.getPatientId(),patientRegistrationDto);
						}
					}else {
						return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
					}
				}
				else
					return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public void saveBabyRegistration(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
		try {
			Response res1 = iPatientRegistrationDao.savePatientRegistration(patientRegistrationDto);
			if (res1.getStatus().equals(SUCCESS)) {
				Response patientResponse = iPatientRegistrationDao.savePatientDetails(patientRegistrationDto);
			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
		}
	}
	
	@Override
	@Transactional
	public Response getPatientDetais(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
		try {
			return iPatientRegistrationDao.getPatientDetais(patientRegistrationDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	private String generateUhid(int registrationTypeId) throws ApplicationException {
		try {
			Long incrementedId = iPatientRegistrationDao.getNextValPatientId();
			//System.out.println(incrementedId);
			String uhidPrefix=null;
			if(registrationTypeId!=2)
				uhidPrefix = "PC-18-";
			
			if(registrationTypeId==5)
				uhidPrefix = "PR-18-";
			
			if(registrationTypeId==2)
				uhidPrefix = "O-18-";
			
			StringBuilder uhid=new StringBuilder(uhidPrefix);
			
				int length = Long.valueOf(incrementedId).toString().length();
				uhid = length == 1 ? (uhid.append("000000").append(incrementedId))
						: length == 2 ? (uhid.append("00000").append(incrementedId))
								: length == 3 ? (uhid.append("0000").append(incrementedId))
										: length == 4 ? (uhid.append("000").append(incrementedId))
												: length == 5 ? (uhid.append("00").append(incrementedId))
														: length == 6 ? (uhid.append("0").append(incrementedId))
																: length == 7 ? (uhid.append(incrementedId)) : uhid.append("");
				return uhid.toString();
			} catch (Exception e) {
			logger.error("generateUhid failed", e);
			}

		return "error";
	}
	
	private int generateUhidConfig(int registrationTypeId) throws ApplicationException {
		Integer screenId = 0;
		try {
		
			if(registrationTypeId!=2)
				screenId = NORMAL_PATIENT_UHID;
			
			if(registrationTypeId==5)
				screenId = PRE_REGESTRATION_UHID;
			
			if(registrationTypeId==2)
				screenId = OTC_PATIENT_UHID;
			
	
			} catch (Exception e) {
			logger.error("generateUhidConfig failed", e);
			}
			
		return screenId;
	}

	@Override
	@Transactional
	public Response patientSearch(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
		try {
			if(patientRegistrationDto.getRegistrationTypeId()!=null)
			{
				if(patientRegistrationDto.getRegistrationTypeId()==0)
					return iPatientRegistrationDao.commonPatientSearch(patientRegistrationDto);
				
				if(patientRegistrationDto.getRegistrationTypeId()==2)
					return iPatientRegistrationDao.patientSearchOTC(patientRegistrationDto);
				
				if(patientRegistrationDto.getRegistrationTypeId()==4)
					return iPatientRegistrationDao.patientSearchER(patientRegistrationDto);
				
				if(patientRegistrationDto.getRegistrationTypeId()==1)
					return iPatientRegistrationDao.patientSearchNormal(patientRegistrationDto);
				
				if(patientRegistrationDto.getRegistrationTypeId()==5)
					return iPatientRegistrationDao.patientSearchPre(patientRegistrationDto);
			}
			if(patientRegistrationDto.getRegistrationTypeIdList()!=null && patientRegistrationDto.getRegistrationTypeIdList().size()>0)
				return iPatientRegistrationDao.patientSearchByRegistrationTypeIdList(patientRegistrationDto);
			
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response<>(ERROR, COMMON_ERROR_CODE, null, null, null);
	}

	@Override
	@Transactional
	public Response updatepatientRegistration(PatientRegistrationDto patientRegistrationDto)
			throws Exception {

		Response res = iPatientRegistrationDao.checkUniquePatient(patientRegistrationDto);
		if(res.getStatus().equals(SUCCESS)&&res.getListObject().size() > 0  && patientRegistrationDto.getHealthNumber() == null){
			return new Response(ERROR, null, "Already exist", null, null);
		}else{
			
			Response res2 = iPatientRegistrationDao.patientSearchNormalByPatientId(patientRegistrationDto.getPatientId(),patientRegistrationDto.getOrganizationId(),patientRegistrationDto.getUnitId());
			Iterator<PatientRegistrationDto> itr = res2.getListObject().iterator();
			 
			  while (itr.hasNext()) {
				  PatientRegistrationDto newPatientRegistrationDto =new PatientRegistrationDto();
				  PatientRegistrationDto obj = itr.next();
				  
				  newPatientRegistrationDto.setPatientId(obj.getPatientId());
				  newPatientRegistrationDto.setOrganizationId(obj.getOrganizationId());
				  newPatientRegistrationDto.setUnitId(obj.getUnitId());
				  newPatientRegistrationDto.setUhidNumber(obj.getUhidNumber());
				  newPatientRegistrationDto.setPrefixId(obj.getPrefixId());
				  newPatientRegistrationDto.setFirstName(obj.getFirstName());
				  newPatientRegistrationDto.setMiddleName(obj.getMiddleName());
				  newPatientRegistrationDto.setLastName(obj.getLastName());
				  newPatientRegistrationDto.setGenderId(obj.getGenderId());
				  newPatientRegistrationDto.setDob(obj.getDob());
				  newPatientRegistrationDto.setMobileNumber(obj.getMobileNumber());
				  newPatientRegistrationDto.setCountryCallingCode(obj.getCountryCallingCode());
				  newPatientRegistrationDto.setBarCode(obj.getBarCode());
				  newPatientRegistrationDto.setIsVip(obj.getIsVip());
				  newPatientRegistrationDto.setVipRemark(obj.getVipRemark());
				  newPatientRegistrationDto.setIsBlacklist(obj.getIsBlacklist());
				  newPatientRegistrationDto.setIdentificationTypeId(obj.getIdentificationTypeId());
				  newPatientRegistrationDto.setIdentificationNumber(obj.getIdentificationNumber());
				  newPatientRegistrationDto.setEmail(obj.getEmail());
				  newPatientRegistrationDto.setPatientCategoryId(obj.getPatientCategoryId());
				  newPatientRegistrationDto.setIsUnknownReg(obj.getIsUnknownReg());
				  newPatientRegistrationDto.setNationalityId(obj.getNationalityId());
				  newPatientRegistrationDto.setRaceId(obj.getRaceId());
				  newPatientRegistrationDto.setMaritalStatusId(obj.getMaritalStatusId());
				  newPatientRegistrationDto.setAddress(obj.getAddress());
				  newPatientRegistrationDto.setCountryId(obj.getCountryId());
				  newPatientRegistrationDto.setStateId(obj.getStateId());
				  newPatientRegistrationDto.setDistrictId(obj.getDistrictId());
				  newPatientRegistrationDto.setCityId(obj.getCityId());
				  newPatientRegistrationDto.setAreaId(obj.getAreaId());
				  newPatientRegistrationDto.setZipCode(obj.getZipCode());
				  newPatientRegistrationDto.setPhoneCode(obj.getPhoneCode());
				  newPatientRegistrationDto.setPhoneNumber(obj.getPhoneNumber());
				  newPatientRegistrationDto.setCompanyName(obj.getCompanyName());
				  newPatientRegistrationDto.setCompanyAddress(obj.getCompanyAddress());
				  newPatientRegistrationDto.setCompanyCountryId(obj.getCompanyCountryId());
				  newPatientRegistrationDto.setCompanyStateId(obj.getCompanyStateId());
				  newPatientRegistrationDto.setCompanyDistrictId(obj.getCompanyDistrictId());
				  newPatientRegistrationDto.setCompanyCityId(obj.getCompanyCityId());
				  newPatientRegistrationDto.setCompanyAreaId(obj.getCompanyAreaId());
				  newPatientRegistrationDto.setCompanyZipCode(obj.getCompanyZipCode());
				  newPatientRegistrationDto.setCompanyMobileNumber(obj.getCompanyMobileNumber());
				  newPatientRegistrationDto.setPermanentAddress(obj.getPermanentAddress());
				  newPatientRegistrationDto.setPermanentCountryId(obj.getPermanentCountryId());
				  newPatientRegistrationDto.setPermanentStateId(obj.getPermanentStateId());
				  newPatientRegistrationDto.setPermanentDistrictId(obj.getPermanentDistrictId());
				  newPatientRegistrationDto.setPermanentCityId(obj.getPermanentCityId());
				  newPatientRegistrationDto.setPermanentAreaId(obj.getPermanentAreaId());
				  newPatientRegistrationDto.setPermanentZipCode(obj.getPermanentZipCode());
				  newPatientRegistrationDto.setPermanentPhoneCode(obj.getPermanentPhoneCode());
				  newPatientRegistrationDto.setPermanentPhoneNumber(obj.getPermanentPhoneNumber());
				  newPatientRegistrationDto.setAliseName(obj.getAliseName());
				  newPatientRegistrationDto.setIdentificationExpiryDate(obj.getIdentificationExpiryDate());
				  newPatientRegistrationDto.setOccupationId(obj.getOccupationId());
				  newPatientRegistrationDto.setRegistrationTypeId(obj.getRegistrationTypeId());
				  newPatientRegistrationDto.setPreviousId(obj.getPreviousId());
				  newPatientRegistrationDto.setIsOtcReg(obj.getIsOtcReg());
				  newPatientRegistrationDto.setIsPreReg(obj.getIsPreReg());
				  newPatientRegistrationDto.setIsExpiryRequired(obj.getIsExpiryRequired());
				  newPatientRegistrationDto.setCreatedBy(obj.getCreatedBy());
				  newPatientRegistrationDto.setUpdatedBy(patientRegistrationDto.getUpdatedBy()!=null?patientRegistrationDto.getUpdatedBy():obj.getUpdatedBy());
				  newPatientRegistrationDto.setCreatedDate(obj.getCreatedDate());
				  newPatientRegistrationDto.setFamilyId(obj.getFamilyId());
				  newPatientRegistrationDto.setUpdatedDate(patientRegistrationDto.getUpdatedDate()!= null?patientRegistrationDto.getUpdatedDate():obj.getUpdatedDate());
				  newPatientRegistrationDto.setImagePath(patientRegistrationDto.getImagePath() != null ? patientRegistrationDto.getImagePath() : "");

				  iPatientRegistrationDao.savePatientRegistrationLog(newPatientRegistrationDto);
				  if(obj.getUuid()!=null)
				  {
					  patientRegistrationDto.setUuid(obj.getUuid()); 
				  }
				  patientRegistrationDto.setIsPrimary('Y');
				  
				  if(patientRegistrationDto.getArea()!=null && patientRegistrationDto.getArea().length()>0 && patientRegistrationDto.getAreaId()==null)
					{
						AreaMasterDto areaMasterDto = new AreaMasterDto();
							areaMasterDto.setAreaName(patientRegistrationDto.getArea());
							areaMasterDto.setAreaCode(patientRegistrationDto.getArea());
							areaMasterDto.setCityId(patientRegistrationDto.getCityId());
							areaMasterDto.setDistrictId(patientRegistrationDto.getDistrictId());
							areaMasterDto.setStateId(patientRegistrationDto.getStateId());
							areaMasterDto.setCountryId(patientRegistrationDto.getCountryId());
							areaMasterDto.setOrganizationId(patientRegistrationDto.getOrganizationId());
							areaMasterDto.setPostCode(patientRegistrationDto.getZipCode());
							areaMasterDto.setCreatedBy(patientRegistrationDto.getCreatedBy());
							areaMasterDto.setUpdatedBy(patientRegistrationDto.getUpdatedBy());
							areaMasterDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
							areaMasterDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
							areaMasterDto.setStatus('A');
						Response areaRes = iAreaMaster.addAreaMaster(areaMasterDto);	
						if(areaRes.getObject()!=null)
						{
							areaMasterDto = (AreaMasterDto)areaRes.getObject();
								patientRegistrationDto.setAreaId(areaMasterDto.getAreaId());
								patientRegistrationDto.setZipCode(areaMasterDto.getPostCode());
							
							if(patientRegistrationDto.getArea().equals(patientRegistrationDto.getPermanentArea()))
							{
								patientRegistrationDto.setPermanentAreaId(areaMasterDto.getAreaId());
								patientRegistrationDto.setPermanentZipCode(areaMasterDto.getPostCode());
							}else{
								AreaMasterDto perAreaMasterDto = new AreaMasterDto();
									perAreaMasterDto.setAreaName(patientRegistrationDto.getPermanentArea());
									perAreaMasterDto.setAreaCode(patientRegistrationDto.getPermanentArea());
									perAreaMasterDto.setCityId(patientRegistrationDto.getPermanentCityId());
									perAreaMasterDto.setDistrictId(patientRegistrationDto.getPermanentDistrictId());
									perAreaMasterDto.setStateId(patientRegistrationDto.getPermanentStateId());
									perAreaMasterDto.setCountryId(patientRegistrationDto.getPermanentCountryId());
									perAreaMasterDto.setOrganizationId(patientRegistrationDto.getOrganizationId());
									perAreaMasterDto.setCreatedBy(patientRegistrationDto.getCreatedBy());
									perAreaMasterDto.setUpdatedBy(patientRegistrationDto.getUpdatedBy());
									perAreaMasterDto.setPostCode(patientRegistrationDto.getPermanentZipCode());
									perAreaMasterDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
									perAreaMasterDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
									perAreaMasterDto.setStatus('A');
								Response perAreaRes = iAreaMaster.addAreaMaster(perAreaMasterDto);	
								if(perAreaRes.getObject()!=null)
								{
									perAreaMasterDto = (AreaMasterDto)perAreaRes.getObject();
									patientRegistrationDto.setPermanentAreaId(perAreaMasterDto.getAreaId());
									patientRegistrationDto.setPermanentZipCode(perAreaMasterDto.getPostCode());
								}
							}
						}
					}
				  
				    patientRegistrationDto.setFileSequenceNumber(patientRegistrationDto.getFileSequenceNumber());
				    patientRegistrationDto.setUpdatedDate(patientRegistrationDto.getUpdatedDate()!= null?patientRegistrationDto.getUpdatedDate():obj.getUpdatedDate());
				    patientRegistrationDto.setUpdatedBy(patientRegistrationDto.getUpdatedBy()!=null?patientRegistrationDto.getUpdatedBy():obj.getUpdatedBy());
				    patientRegistrationDto.setUhidNumber(obj.getUhidNumber()!= null ? obj.getUhidNumber() : obj.getUhidnumber());
				    patientRegistrationDto.setCreatedDate(obj.getCreatedDate());
				    patientRegistrationDto.setUnitId(obj.getUnitId());
				    patientRegistrationDto.setCreatedBy(obj.getCreatedBy());
				    patientRegistrationDto.setFamilyId(obj.getFamilyId());
				    patientRegistrationDto.setImagePath(patientRegistrationDto.getImagePath());
				 	Response updateres = iPatientRegistrationDao.updatePatient(patientRegistrationDto);
				 
				 	if(patientRegistrationDto.getKinList()!=null && patientRegistrationDto.getKinList().size()>0) 
					{
						if(patientRegistrationDto.getKinDetailsId()!=null && patientRegistrationDto.getKinDetailsId() != 0) 
						{
							for(KinDetailsDto kinDetailDto:patientRegistrationDto.getKinList()) 
							{
								if (!kinDetailDto.getKinName().equals("")) 
								{
									kinDetailDto.setKinDetailsId(patientRegistrationDto.getKinDetailsId());
									kinDetailDto.setPatientId(patientRegistrationDto.getPatientId());
									iKinDetailsDao.updateKinDetails(kinDetailDto);
								}
							}
						}else 
						{
							for(KinDetailsDto kinDetailDto:patientRegistrationDto.getKinList()) 
							{
									kinDetailDto.setPatientId(patientRegistrationDto.getPatientId());
								 iKinDetailsDao.saveKinDetails(kinDetailDto);
							}
						}
					}
				
				 if(updateres.getStatus().equals(SUCCESS))
				 {
					 ScheduleLogsDto logDto = new ScheduleLogsDto(this);	
					 String dateTime = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()), "dd-M-yyyy HH:mm:ss");
						logDto.setAction(ScheduleActions.INSERT);					    		
						logDto.setAddedBy(patientRegistrationDto.getUpdatedBy());			
						logDto.setAddedDate(dateTime);					    		
						logDto.setDateTime(dateTime);					    		
						logDto.setApplication(ScheduleActions.HIS);					
						logDto.setErrorCount(0);					    		 
						logDto.setPriority(ScheduleActions.PRIORITY_NORM);			
						logDto.setRecordId(patientRegistrationDto.getPatientId());					   
						logDto.setStatus(ScheduleActions.STATUS_PENDING);				
						logDto.setTableName(ScheduleTables.PATIENT_MASTER);		
						logDto.setOrgId(patientRegistrationDto.getOrganizationId());
						//applicationEventPublisher.publishEvent(logDto);
						isheduleDao.saveScheduleLogs(logDto);
						
						 ScheduleLogsDto logDtos = new ScheduleLogsDto(this);	
						 String dateTimes = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()), "dd-M-yyyy HH:mm:ss");
						 logDtos.setAction(ScheduleActions.INSERT);					    		
						 logDtos.setAddedBy(patientRegistrationDto.getUpdatedBy());			
						 logDtos.setAddedDate(dateTimes);					    		
						 logDtos.setDateTime(dateTimes);					    		
						 logDtos.setApplication(ScheduleActions.HIS);					
						 logDtos.setErrorCount(0);					    		 
						 logDtos.setPriority(ScheduleActions.PRIORITY_NORM);			
						 logDtos.setRecordId(patientRegistrationDto.getKinDetailsId());					   
						 logDtos.setStatus(ScheduleActions.STATUS_PENDING);				
						 logDtos.setTableName(ScheduleTables.PATIENT_KIN_DETAILS);		
						 logDtos.setOrgId(patientRegistrationDto.getOrganizationId());
							//applicationEventPublisher.publishEvent(logDto);
							isheduleDao.saveScheduleLogs(logDtos);
				 }
				 
				 Response ds = iPatientRegistrationDao.getDatabaseLink(newPatientRegistrationDto);
					PatientRegistrationDto resDto= (PatientRegistrationDto)ds.getObject();
					if(resDto.getMysqlOnp() != null && resDto.getMysqlOnp() != "") {
						 checkSqlConnectionForUpdate(patientRegistrationDto);
					}
				
				 return updateres;
					
			  }
				  
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, null, null, null);
	}

	
	@Override
	@Transactional
	public Response patientDetailsByIdForBilling(int patientId) throws ApplicationException {
		try{
			return iPatientRegistrationDao.patientDetailsByIdForBilling(patientId);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response checkUniquePatient(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
		try{
			return iPatientRegistrationDao.checkUniquePatient(patientRegistrationDto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	@Transactional
	public Response convertPatient(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
		
		Integer patientId=null;
		Response res = iPatientRegistrationDao.checkUniquePatient(patientRegistrationDto);
		if(res.getStatus().equals(SUCCESS) && res.getListObject().size()>0)
		{
			return new Response(ERROR, null, "Already exist", null, null);
		}else{
			
			if(patientRegistrationDto.getRegistrationTypeId()==6){
				patientId=patientRegistrationDto.getPatientId();
				patientRegistrationDto.setIsPrimary('Y');
			}else
				patientId=patientRegistrationDto.getPreviousId();
				
				
			Response res2 = iPatientRegistrationDao.patientSearchNormalByPatientId(patientId,patientRegistrationDto.getOrganizationId(),patientRegistrationDto.getUnitId());
			Iterator<PatientRegistrationDto> itr = res2.getListObject().iterator();
			 
			  while (itr.hasNext()) {
				  PatientRegistrationDto newPatientRegistrationDto =new PatientRegistrationDto();
				  PatientRegistrationDto obj = itr.next();
				  
				  String uhidNo = iGlobalUnitMasterDao.getGeneratedGlobalNo(0, patientRegistrationDto.getOrganizationId(), patientRegistrationDto.getUnitId(),generateUhidConfig(patientRegistrationDto.getRegistrationTypeId()));
					if( uhidNo!=null && ! uhidNo.isEmpty() )
                      patientRegistrationDto.setUhidNumber(uhidNo);
                  else
                      patientRegistrationDto.setUhidNumber(generateUhid(patientRegistrationDto.getRegistrationTypeId()));
				  
				  
				  newPatientRegistrationDto.setPatientId(obj.getPatientId());
				  newPatientRegistrationDto.setOrganizationId(obj.getOrganizationId());
				  newPatientRegistrationDto.setUnitId(obj.getUnitId());
				  newPatientRegistrationDto.setPrevUhidNumber(obj.getUhidNumber());
				  newPatientRegistrationDto.setUhidNumber(obj.getUhidNumber());
				  newPatientRegistrationDto.setPrefixId(obj.getPrefixId());
				  newPatientRegistrationDto.setFirstName(obj.getFirstName());
				  newPatientRegistrationDto.setMiddleName(obj.getMiddleName());
				  newPatientRegistrationDto.setLastName(obj.getLastName());
				  newPatientRegistrationDto.setGenderId(obj.getGenderId());
				  newPatientRegistrationDto.setDob(obj.getDob());
				  newPatientRegistrationDto.setMobileNumber(obj.getMobileNumber());
				  newPatientRegistrationDto.setCountryCallingCode(obj.getCountryCallingCode());
				  newPatientRegistrationDto.setBarCode(obj.getBarCode());
				  newPatientRegistrationDto.setIsVip(obj.getIsVip());
				  newPatientRegistrationDto.setVipRemark(obj.getVipRemark());
				  newPatientRegistrationDto.setIsBlacklist(obj.getIsBlacklist());
				  newPatientRegistrationDto.setIdentificationTypeId(obj.getIdentificationTypeId());
				  newPatientRegistrationDto.setIdentificationNumber(obj.getIdentificationNumber());
				  newPatientRegistrationDto.setEmail(obj.getEmail());
				  newPatientRegistrationDto.setPatientCategoryId(obj.getPatientCategoryId());
				  newPatientRegistrationDto.setIsUnknownReg(obj.getIsUnknownReg());
				  newPatientRegistrationDto.setNationalityId(obj.getNationalityId());
				  newPatientRegistrationDto.setRaceId(obj.getRaceId());
				  newPatientRegistrationDto.setMaritalStatusId(obj.getMaritalStatusId());
				  newPatientRegistrationDto.setAddress(obj.getAddress());
				  newPatientRegistrationDto.setCountryId(obj.getCountryId());
				  newPatientRegistrationDto.setStateId(obj.getStateId());
				  newPatientRegistrationDto.setDistrictId(obj.getDistrictId());
				  newPatientRegistrationDto.setCityId(obj.getCityId());
				  newPatientRegistrationDto.setAreaId(obj.getAreaId());
				  newPatientRegistrationDto.setZipCode(obj.getZipCode());
				  newPatientRegistrationDto.setPhoneCode(obj.getPhoneCode());
				  newPatientRegistrationDto.setPhoneNumber(obj.getPhoneNumber());
				  newPatientRegistrationDto.setCompanyName(obj.getCompanyName());
				  newPatientRegistrationDto.setCompanyAddress(obj.getCompanyAddress());
				  newPatientRegistrationDto.setCompanyCountryId(obj.getCompanyCountryId());
				  newPatientRegistrationDto.setCompanyStateId(obj.getCompanyStateId());
				  newPatientRegistrationDto.setCompanyDistrictId(obj.getCompanyDistrictId());
				  newPatientRegistrationDto.setCompanyCityId(obj.getCompanyCityId());
				  newPatientRegistrationDto.setCompanyAreaId(obj.getCompanyAreaId());
				  newPatientRegistrationDto.setCompanyZipCode(obj.getCompanyZipCode());
				  newPatientRegistrationDto.setCompanyMobileNumber(obj.getCompanyMobileNumber());
				  newPatientRegistrationDto.setPermanentAddress(obj.getPermanentAddress());
				  newPatientRegistrationDto.setPermanentCountryId(obj.getPermanentCountryId());
				  newPatientRegistrationDto.setPermanentStateId(obj.getPermanentStateId());
				  newPatientRegistrationDto.setPermanentDistrictId(obj.getPermanentDistrictId());
				  newPatientRegistrationDto.setPermanentCityId(obj.getPermanentCityId());
				  newPatientRegistrationDto.setPermanentAreaId(obj.getPermanentAreaId());
				  newPatientRegistrationDto.setPermanentZipCode(obj.getPermanentZipCode());
				  newPatientRegistrationDto.setPermanentPhoneCode(obj.getPermanentPhoneCode());
				  newPatientRegistrationDto.setPermanentPhoneNumber(obj.getPermanentPhoneNumber());
				  newPatientRegistrationDto.setAliseName(obj.getAliseName());
				  newPatientRegistrationDto.setIdentificationExpiryDate(obj.getIdentificationExpiryDate());
				  newPatientRegistrationDto.setOccupationId(obj.getOccupationId());
				  newPatientRegistrationDto.setRegistrationTypeId(obj.getRegistrationTypeId());
				  newPatientRegistrationDto.setPreviousId(obj.getPreviousId());
				  newPatientRegistrationDto.setIsOtcReg(obj.getIsOtcReg());
				  newPatientRegistrationDto.setIsPreReg(obj.getIsPreReg());
				  newPatientRegistrationDto.setIsExpiryRequired(obj.getIsExpiryRequired());
				  newPatientRegistrationDto.setCreatedBy(patientRegistrationDto.getUpdatedBy());
				  newPatientRegistrationDto.setUpdatedBy(patientRegistrationDto.getUpdatedBy());
				  newPatientRegistrationDto.setCreatedDate(patientRegistrationDto.getUpdatedDate());
				  newPatientRegistrationDto.setUpdatedDate(patientRegistrationDto.getUpdatedDate());
				  
				  iPatientRegistrationDao.savePatientRegistrationLog(newPatientRegistrationDto);
				 
				  
				       iPatientRegistrationDao.updateIsPrimaryFlag(patientRegistrationDto.getPreviousId());
				 patientRegistrationDto.setPatientDetailsId(patientRegistrationDto.getPatientDetailsId());
				 patientRegistrationDto.setRegistrationTypeId(1);//Converted to normal patient
				 //patientRegistrationDto.setUhidNumber(newPatientRegistrationDto.getUhidNumber());
				 patientRegistrationDto.setFileSequenceNumber(patientRegistrationDto.getFileSequenceNumber());
				 // convert patient with previous patientId
				 patientRegistrationDto.setPatientId(patientId);
				 patientRegistrationDto.setPatientDetailsId(obj.getPatientDetailsId());
				 patientRegistrationDto.setIsPrimary('Y');
				  if(obj.getUuid()!=null)
				  {
				  patientRegistrationDto.setUuid(obj.getUuid());
				  }
				 Response res5 = iPatientRegistrationDao.updatePatient(patientRegistrationDto);
				
				 //kin details
				if(patientRegistrationDto.getKinList()!=null && patientRegistrationDto.getKinList().size()>0) 
				{
					if(patientRegistrationDto.getKinDetailsId()!=null && patientRegistrationDto.getKinDetailsId() != 0) 
					{
						for(KinDetailsDto kinDetailDto:patientRegistrationDto.getKinList()) 
						{
							if (!kinDetailDto.getKinName().equals("")) 
							{
								kinDetailDto.setKinDetailsId(patientRegistrationDto.getKinDetailsId());
								kinDetailDto.setPatientId(patientRegistrationDto.getPatientId());
								iKinDetailsDao.updateKinDetails(kinDetailDto);
							}
						}
					}else 
					{
						for(KinDetailsDto kinDetailDto:patientRegistrationDto.getKinList()) 
						{
								kinDetailDto.setPatientId(patientRegistrationDto.getPatientId());
							 iKinDetailsDao.saveKinDetails(kinDetailDto);
						}
					}
				}
					
				 if(res5.getStatus().equals(SUCCESS))
					 return new Response(SUCCESS,res5.getCode(),res5.getCode()+" "+COMMON_SUCCESS, null, res5.getObject());
				  
			  }
				  
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, null, null, null);
	}

	/*@Transactional
	private int generateUhidForConversion(int registrationTypeId) throws ApplicationException {
		try {
			
			
			Integer screenId = 0;
			try {
			
				if(registrationTypeId!=2)
					screenId = NORMAL_PATIENT_UHID;
				
				if(registrationTypeId==5)
					screenId = PRE_REGESTRATION_UHID;
				
				if(registrationTypeId==2)
					screenId = OTC_PATIENT_UHID;
				
		
				} catch (Exception e) {
					
				}
				
			return screenId;
			
			
			
			
			Long incrementedId = null;
			
			if(registrationTypeId==4)
				incrementedId = iPatientRegistrationDao.getNextValPatientId();
			else
				incrementedId=(long) patientId;
			
			String uhidPrefix=null;
			if(registrationTypeId!=2)
				uhidPrefix = "PC-18-";
			
			if(registrationTypeId==5)
				uhidPrefix = "PR-18-";
			
			if(registrationTypeId==2)
				uhidPrefix = "O-18-";
			
			StringBuilder uhid=new StringBuilder(uhidPrefix);
			
				int length = Long.valueOf(incrementedId).toString().length();
				uhid = length == 1 ? (uhid.append("000000").append(incrementedId))
						: length == 2 ? (uhid.append("00000").append(incrementedId))
								: length == 3 ? (uhid.append("0000").append(incrementedId))
										: length == 4 ? (uhid.append("000").append(incrementedId))
												: length == 5 ? (uhid.append("00").append(incrementedId))
														: length == 6 ? (uhid.append("0").append(incrementedId))
																: length == 7 ? (uhid.append(incrementedId)) : uhid.append("");
				return uhid.toString();
			} catch (Exception e) {
				
			}
			
		return "error";
	}*/

  @Override
  @Transactional
  public Response savePatientImage(int orgId, int unitId, int patientId, MultipartFile inputFile)
      throws ApplicationException {
    try{
    	String base64Image="";
    	int bucketType = (int) sessionFactory.getCurrentSession()
				.createSQLQuery("select bucket_flag from t_unit_application_billing_configuration where unit_id ="+unitId+" and organization_id ="+orgId)
				.uniqueResult();
    	
      if(orgId>0&&unitId>0&&patientId>0&&!inputFile.isEmpty())
      {
        Response unitRes =  iGlobalOrganisationMasterDao.getOrganiazationFolderPath(orgId);
        OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) unitRes.getObject();
        String patientImagePath =  docUploadUtility.uploadPatientImage(inputFile, organizationMasterDto,unitId,patientId);
        
        
        if(bucketType == 2) {
	        try {
	            URL url = new URL(patientImagePath);
	            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	            try (InputStream inputStream = url.openStream()) {
	                byte[] buffer = new byte[4096];
	                int bytesRead;
	                while ((bytesRead = inputStream.read(buffer)) != -1) {
	                    outputStream.write(buffer, 0, bytesRead);
	                }
	            }

	            byte[] imageBytes = outputStream.toByteArray();
	            base64Image = Base64.getEncoder().encodeToString(imageBytes);

	            System.out.println("MY Patient IMAGE ISsssssssss:"+base64Image);
	        } catch (IOException e) {
	        logger.error("savePatientImage failed", e);
	        }        
        PatientRegistrationDto patientRegistrationDto = new PatientRegistrationDto();
        patientRegistrationDto.setImage(inputFile.getBytes());
        patientRegistrationDto.setPatientId(patientId);
        patientRegistrationDto.setOrganizationId(orgId);
        patientRegistrationDto.setUnitId(unitId);
        patientRegistrationDto.setImagePath(base64Image);
        Response res = iPatientRegistrationDao.updatePatientImagePath(patientRegistrationDto);
        PatientRegistrationDocDetailsDto patientRegistrationDocDetailsDto = new PatientRegistrationDocDetailsDto();
        patientRegistrationDocDetailsDto.setPatientId(patientId);
        if(bucketType == 2) {
        patientRegistrationDocDetailsDto.setDocumentPath(base64Image);
        }else {
        patientRegistrationDocDetailsDto.setDocumentPath(patientImagePath);	
        }
        patientRegistrationDocDetailsDto.setStatus('A');
        patientRegistrationDocDetailsDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"));
        patientRegistrationDocDetailsDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"));
        patientRegistrationDocDetailsDto.setUnitId(unitId);
        patientRegistrationDocDetailsDto.setOrganizationId(orgId);
        patientRegistrationDocDetailsDto.setFileName(inputFile.getOriginalFilename());
        iPatientRegistrationDao.savePatientRegistrationDocDetails(patientRegistrationDocDetailsDto);
       // return res;
        return new Response(SUCCESS, SUCCESS_CODE, null, null, null,bucketType);
        }else {
        	PatientRegistrationDto patientRegistrationDto = new PatientRegistrationDto();
            patientRegistrationDto.setImage(inputFile.getBytes());
            patientRegistrationDto.setPatientId(patientId);
            patientRegistrationDto.setOrganizationId(orgId);
            patientRegistrationDto.setUnitId(unitId);
            patientRegistrationDto.setImagePath(patientImagePath);
            Response res = iPatientRegistrationDao.updatePatientImagePath(patientRegistrationDto);
            PatientRegistrationDocDetailsDto patientRegistrationDocDetailsDto = new PatientRegistrationDocDetailsDto();
            patientRegistrationDocDetailsDto.setPatientId(patientId);
            patientRegistrationDocDetailsDto.setDocumentPath(patientImagePath);
            patientRegistrationDocDetailsDto.setStatus('A');
            patientRegistrationDocDetailsDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"));
            patientRegistrationDocDetailsDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"));
            patientRegistrationDocDetailsDto.setUnitId(unitId);
            patientRegistrationDocDetailsDto.setOrganizationId(orgId);
            patientRegistrationDocDetailsDto.setFileName(inputFile.getOriginalFilename());
            iPatientRegistrationDao.savePatientRegistrationDocDetails(patientRegistrationDocDetailsDto);
            //return res;
            return new Response(SUCCESS, SUCCESS_CODE, null, null, null,bucketType);
        	
        }
        
        
        
      }
    return null;
  }catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
  
  }

  @Override
  @Transactional
public Response getPatientDetaisForHealthCard(PatientRegistrationDto patientRegistrationDto)
		throws ApplicationException {
	  try {
		  	int patientId = patientRegistrationDto.getPatientId();
		  	int unitId = patientRegistrationDto.getUnitId();
		  	int organizationId = patientRegistrationDto.getOrganizationId();
		  	List<String> allergies = new LinkedList<>();
		  	
			Response res = iPatientRegistrationDao.getPatientDetaisForHealthCard(patientId);
			patientRegistrationDto = (PatientRegistrationDto) res.getObject();
			
			Response res2 =iKinDetailsDao.getKinDetailsByPatientId(patientId,null,null);
			
			if(res2.getListObject().size()>0){
				List<KinDetailsDto> list = res2.getListObject();
				KinDetailsDto kin = list.get(0);
				patientRegistrationDto.setKinDetailsDto(kin);
			}
			
			Response res3 = iPatientRegistrationDao.getPatientAllergies(patientId, unitId, organizationId);
			
			if(res3.getListObject().size()>0){
				List<AllergyPatientMapperDto> list2 = res3.getListObject();
				for(AllergyPatientMapperDto dto : list2)
				{
					Date created_date = TimeZoneComponent.getDateByzone(unitId);
					allergies.add(dto.getAllergyName());
					
				}
				patientRegistrationDto.setAllergies(allergies);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, null, null, patientRegistrationDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
}
  
  @Override
  @Transactional
	public Response patientAutofillSearch(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iPatientRegistrationDao.patientAutofillSearch(slotMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

  @Override
  @Transactional
	public Response patientAutofillSearchPlain(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iPatientRegistrationDao.patientAutofillSearchPlain(slotMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
  
@Override
@Transactional
public Response patientTypeHeadSearchForSelfRegistration(SlotMasterDto slotMasterDto) throws ApplicationException {
	try {
		return iPatientRegistrationDao.patientTypeHeadSearchForSelfRegistration(slotMasterDto);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
}

@Override
@Transactional
public Response savePatientFingerPrint(PatientRegistrationDto dto) throws ApplicationException {
	try {
		
		
		JSONObject jsonString1 = new JSONObject();
		jsonString1.put("BiometricData",dto.getThumbImage());
		JSONArray jsonArguments = new JSONArray();
		jsonArguments.put(jsonString1);
		JSONObject jsonString = new JSONObject();
		jsonString.put("Uuid", "0");
		jsonString.put("Biometrics", jsonArguments);

		if (dto.getThumbImage() != null && !dto.getThumbImage().isEmpty()) {
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost("http://mabisdemo.mantratecapp.com/4/clinivantage/biometric/enroll");
			post.addHeader("Content-Type", "application/json;charset=UTF-8");
			post.addHeader("Accept", "application/json");
			//StringEntity input = new StringEntity(jsonString.toString(),Charset.forName("UTF-8"));
			//input.setContentType("application/json");
			//post.setEntity(input);
			HttpResponse response = httpClient.execute(post);
			if (response != null) {
				if (response.getStatusLine().getStatusCode() == new Integer(SUCCESS_CODE)) {
					String json = EntityUtils.toString(response.getEntity(),"UTF-8");
					JSONObject jsonObj = new JSONObject(json);
					Integer errorCode = Integer.parseInt(jsonObj.getString("ErrorCode"));
					if(errorCode.intValue()==0)
					{
						Integer uuid = Integer.parseInt(jsonObj.getString("Uuid"));
						FingerPrintInterFace fingerInterface = new FingerPrintInterFace();
						fingerInterface.setUuid(uuid);
						fingerInterface.setOrgnisationId(dto.getOrganizationId());
						fingerInterface.setPatientId(dto.getPatientId());
						fingerInterface.setStatus('A');
						fingerInterface.setUnitId(dto.getUnitId());
						fingerInterface.setThumbImage(dto.getThumbImage());
						return iPatientRegistrationDao.savePatientThumb(fingerInterface);
					}
				}
			}
			else{
				throw new IOException("client connection to " + post.getURI() + " fail: no connection");
				
				
			}
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
}


@Override
@Transactional
public Response searchPatientByFingerPrint(FingerPrintSearchDto dto) throws ApplicationException {
try {
	    
		
		JSONObject jsonString1 = new JSONObject();
		jsonString1.put("BiometricData",dto.getThumbImage());
		JSONArray jsonArguments = new JSONArray();
		jsonArguments.put(jsonString1);
		JSONObject jsonString = new JSONObject();
		jsonString.put("Group", "");
		jsonString.put("ReqInfo", "");
		jsonString.put("Biometrics", jsonArguments);

		if (dto.getThumbImage() != null && !dto.getThumbImage().isEmpty()) {
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost("http://mabisdemo.mantratecapp.com/4/clinivantage/biometric/identify");
			post.addHeader("Content-Type", "application/json;charset=UTF-8");
			post.addHeader("Accept", "application/json");
			StringEntity input = new StringEntity(jsonString.toString(),Charset.forName("UTF-8"));
			input.setContentEncoding("UTF-8");
			input.setContentType("application/json");
			post.setEntity(input);
			HttpResponse response = httpClient.execute(post);
			if (response != null) {
				if (response.getStatusLine().getStatusCode() == new Integer(SUCCESS_CODE)) 
				{
					String json = EntityUtils.toString(response.getEntity(),"UTF-8");
					JSONObject jsonObj = new JSONObject(json);
					Integer errorCode = Integer.parseInt(jsonObj.getString("ErrorCode"));
					if(errorCode==0)
					{
						List<Integer> uuids = new ArrayList();
						JSONArray matchUserArray = jsonObj.getJSONArray("MatchUser");
					     for (int i = 0; i < matchUserArray.length(); i++) {
					    	 JSONObject userObj = matchUserArray.getJSONObject(i);
					    	 Integer uuid = Integer.parseInt(userObj.getString("uuid"));
					    	 uuids.add(uuid);
						}
						//System.out.println("uuid==>"+uuids);
						if(uuids.size()>0 && uuids!=null)
						{
							Response patientRes = iPatientRegistrationDao.getPatientByuuid(uuids, dto.getOrganizationId(), dto.getUnitId());
							if(patientRes.getListObject()!=null&&patientRes.getListObject().size()>0)
							{  
								List<Integer> patientIds = patientRes.getListObject();
								PatientSearchDto patientSearchDto = new  PatientSearchDto();
								patientSearchDto.setPatientId(patientIds.get(0));
								patientSearchDto.setOrganizationId(dto.getOrganizationId());
								patientSearchDto.setUnitId(dto.getUnitId());
								patientSearchDto.setPatientType(1);
								return iUnknownPatientRegistrationDao.patientSearchByMultipleCriteria(patientSearchDto);
							}
							else{
								return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
							}
						}
					}
					else{
						return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
					}
				}
			}
			else{
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	} catch (Exception e) {
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
		 logDto.setTableName(ScheduleTables.APPOINTMENT_MASTER);
		 isheduleDao.saveScheduleLogs(logDto);
		 
	}
}


public void syncAppointmentDetailForMyLife(AppointmentMaster appointmentMaster, SlotMasterDto slotmasterDto) {
	 String dateTime = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(slotmasterDto.getUnitId()), "dd-M-yyyy HH:mm:ss");
	 ScheduleLogsDto logDto1 = new ScheduleLogsDto(this);
	 logDto1.setAction(ScheduleActions.INSERT);
	 logDto1.setAddedBy(slotmasterDto.getCreatedBy());
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


public void syncPatientRegistrationForMyLife(PatientRegistrationDto patientRegistrationDto,PatientRegistrationDto patientRegistrationdt) {
	String dateTime = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(patientRegistrationDto.getUnitId()), "dd-M-yyyy HH:mm:ss");
	//PatientRegistration patientRegistration=new PatientRegistration();
	
	
	//sync only normal registrations	
	if(patientRegistrationDto.getRegistrationTypeId() != 2 || patientRegistrationDto.getRegistrationTypeId() != 5){
			ScheduleLogsDto scheduleLogsDto=new ScheduleLogsDto(this);
		 	scheduleLogsDto.setAction(ScheduleActions.INSERT);
		 	scheduleLogsDto.setAddedBy(patientRegistrationDto.getCreatedBy());
		 	scheduleLogsDto.setAddedDate(dateTime);
		 	scheduleLogsDto.setApplication(ScheduleActions.HIS);
		 	scheduleLogsDto.setDateTime(dateTime);
		 	scheduleLogsDto.setErrorCount(0);
		 	scheduleLogsDto.setPriority(ScheduleActions.PRIORITY_NORM);
		 	scheduleLogsDto.setRecordId(patientRegistrationdt.getPatientId());
		 	scheduleLogsDto.setStatus(ScheduleActions.STATUS_PENDING);
		 	scheduleLogsDto.setTableName(ScheduleTables.PATIENT_MASTER);
		 	scheduleLogsDto.setOrgId(patientRegistrationdt.getOrganizationId());
			//applicationEventPublisher.publishEvent(scheduleLogsDto);
		 	isheduleDao.saveScheduleLogs(scheduleLogsDto);
		}
	
	/*if(patientRegistrationDto.getRegistrationTypeId() == 3 || patientRegistrationDto.getRegistrationTypeId()==4 || patientRegistrationDto.getRegistrationTypeId()==6 ){
		ScheduleLogsDto scheduleLogsDto=new ScheduleLogsDto(this);
	 	scheduleLogsDto.setAction(ScheduleActions.INSERT);
	 	scheduleLogsDto.setAddedBy(patientRegistrationDto.getCreatedBy());
	 	scheduleLogsDto.setAddedDate(dateTime);
	 	scheduleLogsDto.setApplication(ScheduleActions.HIS);
	 	scheduleLogsDto.setDateTime(dateTime);
	 	scheduleLogsDto.setErrorCount(0);
	 	scheduleLogsDto.setPriority(ScheduleActions.PRIORITY_NORM);
	 	scheduleLogsDto.setRecordId(patientRegistrationdt.getPatientId());
	 	scheduleLogsDto.setStatus(ScheduleActions.STATUS_PENDING);
	 	scheduleLogsDto.setTableName(ScheduleTables.PATIENT_MASTER);
		applicationEventPublisher.publishEvent(scheduleLogsDto);
	}*/
}

@Transactional
public String getAdmissionNumber() {
	try {
		Long incrementedId = iUnknownPatientRegistrationDao.getNextValAdmissionId();
		String encouterPrefix = "ADM-18-";

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

@Override
public Response PatientDemography(int patientId) throws ApplicationException {
	try{
		return iPatientRegistrationDao.PatientDemography(patientId);
	}catch(Exception e){
		
		throw e;
	}
}


@Transactional
@Override
public Response updatePatientAsAbscound(PatientRegistrationDto dto) throws ApplicationException {
	try{
		return iPatientRegistrationDao.updatePatientAsAbscound(dto);
	}catch(Exception e){
		
		throw e;
	}
}

@Transactional
@Override
public Response checkEmailDetails(Integer orgId, String emailId) throws ApplicationException {
	try{
		return iPatientRegistrationDao.checkEmailDetails(orgId,emailId);
	}catch(Exception e){
		
		throw e;
	}
}
	@Override
	@Transactional
	public Response patientDetailsByIdForCyclic(int patientId) throws ApplicationException {
		try{
			return iPatientRegistrationDao.patientDetailsByIdForCyclic(patientId);
		}catch(Exception e){
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response getAllPatientVisits(PatientVisitsDto patientVisitsDto) throws ApplicationException {
		try{
			return iPatientRegistrationDao.getAllPatientVisits(patientVisitsDto);
		}catch(Exception e){
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response getAllPastVisitsDataForInfo(PatientVisitsDto patientVisitsDto) throws ApplicationException {
		try{
			return iPatientRegistrationDao.getAllPastVisitsDataForInfo(patientVisitsDto);
		}catch(Exception e){
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response deletePatientImage(int patientId,int organizationId) throws ApplicationException {
		try{
			return iPatientRegistrationDao.deletePatientImage(patientId,organizationId);
		}catch(Exception e){
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response changeStatus(PatientRegistrationDto patientRegistrationDto) {
		try{
			return iPatientRegistrationDao.changeStatus(patientRegistrationDto);
		}catch(Exception e){
			
			throw e;
		}
	}


	@Override
	@Transactional
	public String getUUIDByPatientId(Integer patientId) {
		try{
			return iPatientRegistrationDao.getUUIDByPatientId(patientId);
		}catch(Exception e){
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response getPatientDocuments(EncounterDocDetailsDto encounterDocDetailsDto) throws ApplicationException {
		try{
			return iPatientRegistrationDao.getPatientDocuments(encounterDocDetailsDto);
		}catch(Exception e){
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response deletePatientDocument(EncounterDocDetailsDto encounterDocDetailsDto) throws ApplicationException {
		try{
			return iPatientRegistrationDao.deletePatientDocument(encounterDocDetailsDto);
		}catch(Exception e){
			
			throw e;
		}
	
	}


	@Override
	@Transactional
	public Response patientSearchPatientList(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException {
		try {

				return iPatientRegistrationDao.patientSearchPatientList(patientRegistrationDto);

		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response getPatientDetailsByUhid(PatientRegistrationDto dto) throws ApplicationException {
		try {
			return iPatientRegistrationDao.getPatientDetailsByUhid(dto);
	}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}


	@Override
	@Transactional
	public Response saveCustomPatientDetails(CustomPatientDetailsDto customPatientDetailsObj)
			throws ApplicationException {
		try {
			return iPatientRegistrationDao.saveCustomPatientDetails(customPatientDetailsObj);
	}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}


	@Override
	@Transactional
	public Response getPatientInfoForHospitalVisit(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException {
		try {
			Response res =  iPatientRegistrationDao.getPatientInfoForHospitalVisit(patientRegistrationDto);
			if(res.getObject()!=null) {
				PatientRegistrationDto dto = (PatientRegistrationDto) res.getObject();
				
				if(dto.getCustomDataPost()!=null) {
					ObjectMapper objectMapper = new ObjectMapper();
					CustomDataPostDto cutomDto = objectMapper.readValue(dto.getCustomDataPost(),CustomDataPostDto.class);
					dto.setCustomDataPostDto(cutomDto);
				}
				
				Response res2 = iKinDetailsDao.getKinDetailsByPatientId(dto.getPatientId(), null, null);
				if(res2.getListObject()!=null && res2.getListObject().size()>0) {
					dto.setKinList(res2.getListObject());
					res.setObject(dto);
				}
			}
			return res;
	}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}


	@Override
	@Transactional
	public Response updateCustomDetails(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
		try {
			return iPatientRegistrationDao.updateCustomDetails(patientRegistrationDto);
	}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

    @Override
    @Transactional
    public Response getPatientDocumentDetailList(PatientRegistrationDto patientRegistrationDto)
            throws ApplicationException {
    try {
            return iPatientRegistrationDao.getPatientDocumentDetailList(patientRegistrationDto);
    }catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
    
    }


	@Override
	@Transactional
	public Response sendPatientSmsFromCoversheet(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException {
		return iPatientRegistrationDao.sendPatientSmsFromCoversheet(patientRegistrationDto);
		
		}


	@Override
	@Transactional
	public Response patientTypeHeadSearchForAppointmentUnique(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iPatientRegistrationDao.patientTypeHeadSearchForAppointmentUnique(slotMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response patientTypeHeadSearchForAppointmentUniqueForNurseCheckIn(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iPatientRegistrationDao.patientTypeHeadSearchForAppointmentUniqueForNurseCheckIn(slotMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response getPatientDetailsListForNewEMR(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException {
		try {
			Response res= iPatientRegistrationDao.getPatientDetailsListForNewEMR(patientRegistrationDto);
			CoversheetTemplateMasterDto dto=new CoversheetTemplateMasterDto();
			dto.setOrganizationId(patientRegistrationDto.getOrganizationId());
			dto.setUnitId(patientRegistrationDto.getUnitId());
			dto.setPatientId(patientRegistrationDto.getPatientid());
			dto.setEncounterId(patientRegistrationDto.getEncounterId());
			Response resObj= iCoversheetTemaplateDao.getCoversheetListData(dto);
			PatientRegistrationDto resDto= (PatientRegistrationDto)resObj.getObject();
			
			/****Favorites Lists*****/
			//prescription
			PrescriptionDto prescriptionDtoSave=new PrescriptionDto();
			prescriptionDtoSave.setOrganizationId(patientRegistrationDto.getOrganizationId());
			prescriptionDtoSave.setUnitId(patientRegistrationDto.getUnitId());
			prescriptionDtoSave.setPatientId(patientRegistrationDto.getPatientId());
			prescriptionDtoSave.setEncounterId(patientRegistrationDto.getEncounterId());
			prescriptionDtoSave.setDoctorId(patientRegistrationDto.getDoctorId());
			//Response detailedFavoriteMedications = iMedicationDAO.getFrequentlyUsedMedicationList(prescriptionDtoSave);
		//	dto.setListPrescriptionFavoriteData(detailedFavoriteMedications.getListObject());
			//Diagnosis
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto=new DiagnosisPatientAppointmentMapperDto();
			diagnosisPatientAppointmentMapperDto.setOrganizationId(patientRegistrationDto.getOrganizationId());
			diagnosisPatientAppointmentMapperDto.setPatientId(patientRegistrationDto.getPatientId());
		//	Response diagnosisFavoriteList = iPatientDiagnosisDao.getFrequentlyUsedDiagnosisList(diagnosisPatientAppointmentMapperDto);
	//		dto.setListDiagnosisFavoriteData(diagnosisFavoriteList.getListObject());
			
			//Advice
			InstructionMasterDto instructionDto=new InstructionMasterDto();
			instructionDto.setOrganizationId(patientRegistrationDto.getOrganizationId());
			instructionDto.setUnitId(patientRegistrationDto.getUnitId());
			//Response adviceListFavoriteData = iInspectionMasterDao.getMostFrequentlyUsedInstructions(instructionDto);
			//dto.setListAdviceFavoriteData(adviceListFavoriteData.getListObject());

			//Lab order
			DoctorFavoritesDto doctorFavoritesDto =new DoctorFavoritesDto();
			doctorFavoritesDto.setOrganizationId(patientRegistrationDto.getOrganizationId());
			doctorFavoritesDto.setUnitId(patientRegistrationDto.getUnitId());
			doctorFavoritesDto.setDoctorId(patientRegistrationDto.getDoctorId());
			//Response labOrderFavoriteListData =iDoctorFavoritesDao.getMostlyUsedFavoritesServiceList(doctorFavoritesDto);
			//dto.setListLabOrderFavoriteData(labOrderFavoriteListData.getListObject());

			/***Get Lists***/
			//1 For complaints
			ComplaintAppointmentMapperDto complaintAppointmentMapperDto=new ComplaintAppointmentMapperDto();
			complaintAppointmentMapperDto.setOrganizationId(patientRegistrationDto.getOrganizationId());
			complaintAppointmentMapperDto.setUnitId(patientRegistrationDto.getUnitId());
			complaintAppointmentMapperDto.setEncounterId(patientRegistrationDto.getEncounterId());
			Response complaintListRes = iComplaintAppointmentMapperDao.getListOfComplaintAppointmentMapper(complaintAppointmentMapperDto);
			//Response complaintFavoriteListRes = iComplaintAppointmentMapperDao.getMostSavedComplaint(complaintAppointmentMapperDto);
			dto.setComplaintsPatientList(complaintListRes.getListObject());
		//	dto.setListComplaintFavoriteData(complaintFavoriteListRes.getListObject());
			
			//14 for Vital
			AppointmentVitalMapperDto appointmentVitalMapperDto=new AppointmentVitalMapperDto();
			appointmentVitalMapperDto.setOrganizationId(patientRegistrationDto.getOrganizationId());
			appointmentVitalMapperDto.setUnitId(patientRegistrationDto.getUnitId());
			appointmentVitalMapperDto.setPatientId(patientRegistrationDto.getPatientId());
			appointmentVitalMapperDto.setEncounterId(patientRegistrationDto.getEncounterId());
			appointmentVitalMapperDto.setCreatedDate(patientRegistrationDto.getCreatedDate());
			Response patientViatalList = iAppointmentVitalMapperDao.getListOfVitalsByPatientIdForCoversheet(appointmentVitalMapperDto);
			dto.setPatientViatalList(patientViatalList.getListObject());
			
			if(resDto.getIsDataAvailable() == true) {
				//11 for prescription 
				Response detailedMedications = iMedicationDAO.getMedicationDetailsByDoctorId(prescriptionDtoSave);
				dto.setListPrescriptionData(detailedMedications.getListObject());

				//15 for Diagnosis
				Response diagnosisPatientList = iPatientDiagnosisDao.getPatientDiagnosisByPatientId(diagnosisPatientAppointmentMapperDto);
				dto.setListDiagnosisData(diagnosisPatientList.getListObject());
				
				
				////15 for Advice
				Response adviceListData = iCoversheetInstructionDao.coversheetInstructionList(patientRegistrationDto.getEncounterId(),patientRegistrationDto.getVisitTypeId());
				dto.setListAdviceData(adviceListData.getListObject());
				
				////15 for Lab order 

				Response labOrderListData =  iOrderDetailsMasterDao.getInvestigationsListForNewEMR(patientRegistrationDto.getPatientid(),false,patientRegistrationDto.getUnitId());
				dto.setListLabOrderData(labOrderListData.getListObject());
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, res.getListObject(),resDto.getIsDataAvailable(),dto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response savePatientRemarksForNewEMR(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException {
		try {
			return iPatientRegistrationDao.savePatientRemarksForNewEMR(patientRegistrationDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
    @Override
    @Transactional
    public Response checkSqlConnection(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
        try {
            return iPatientRegistrationDao.checkSqlConnection(patientRegistrationDto);
        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
        
    }

    @Override
    @Transactional
    public Response checkSqlConnectionForUpdate(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
        try {
            return iPatientRegistrationDao.checkSqlConnectionForUpdate(patientRegistrationDto);
        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
        
    }


	@Override
	@Transactional
	public Response checkSqlConnectionForUpdatePatientSource(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException {
        try {
            return iPatientRegistrationDao.checkSqlConnectionForUpdatePatientSource(patientRegistrationDto);
        } catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
        
    }


	@Override
	@Transactional
	public Response getPatientDetailsListForNewEMROnlyFavoritesList(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException {
		try {
			//Response res= iPatientRegistrationDao.getPatientDetailsListForNewEMR(patientRegistrationDto);
			CoversheetTemplateMasterDto dto=new CoversheetTemplateMasterDto();
			dto.setOrganizationId(patientRegistrationDto.getOrganizationId());
			dto.setUnitId(patientRegistrationDto.getUnitId());
			dto.setPatientId(patientRegistrationDto.getPatientid());
			dto.setEncounterId(patientRegistrationDto.getEncounterId());
			
			/****Favorites Lists*****/
			//prescription
			PrescriptionDto prescriptionDtoSave=new PrescriptionDto();
			prescriptionDtoSave.setOrganizationId(patientRegistrationDto.getOrganizationId());
			prescriptionDtoSave.setUnitId(patientRegistrationDto.getUnitId());
			prescriptionDtoSave.setPatientId(patientRegistrationDto.getPatientId());
			prescriptionDtoSave.setEncounterId(patientRegistrationDto.getEncounterId());
			prescriptionDtoSave.setDoctorId(patientRegistrationDto.getDoctorId());
			//Diagnosis
			DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto=new DiagnosisPatientAppointmentMapperDto();
			diagnosisPatientAppointmentMapperDto.setOrganizationId(patientRegistrationDto.getOrganizationId());
			diagnosisPatientAppointmentMapperDto.setPatientId(patientRegistrationDto.getPatientId());
			Response diagnosisFavoriteList = iPatientDiagnosisDao.getFrequentlyUsedDiagnosisList(diagnosisPatientAppointmentMapperDto);
			dto.setListDiagnosisFavoriteData(diagnosisFavoriteList.getListObject());
			
			//Advice
			InstructionMasterDto instructionDto=new InstructionMasterDto();
			instructionDto.setOrganizationId(patientRegistrationDto.getOrganizationId());
			instructionDto.setUnitId(patientRegistrationDto.getUnitId());
			Response adviceListFavoriteData = iInspectionMasterDao.getMostFrequentlyUsedInstructions(instructionDto);
			dto.setListAdviceFavoriteData(adviceListFavoriteData.getListObject());

			//Lab order
			DoctorFavoritesDto doctorFavoritesDto =new DoctorFavoritesDto();
			doctorFavoritesDto.setOrganizationId(patientRegistrationDto.getOrganizationId());
			doctorFavoritesDto.setUnitId(patientRegistrationDto.getUnitId());
			doctorFavoritesDto.setDoctorId(patientRegistrationDto.getDoctorId());
			Response labOrderFavoriteListData =iDoctorFavoritesDao.getMostlyUsedFavoritesServiceList(doctorFavoritesDto);
			dto.setListLabOrderFavoriteData(labOrderFavoriteListData.getListObject());
			
            //complaint
			ComplaintAppointmentMapperDto complaintAppointmentMapperDto=new ComplaintAppointmentMapperDto();
			complaintAppointmentMapperDto.setOrganizationId(patientRegistrationDto.getOrganizationId());
			complaintAppointmentMapperDto.setUnitId(patientRegistrationDto.getUnitId());
			complaintAppointmentMapperDto.setEncounterId(patientRegistrationDto.getEncounterId());
			Response complaintFavoriteListRes = iComplaintAppointmentMapperDao.getMostSavedComplaint(complaintAppointmentMapperDto);
			dto.setListComplaintFavoriteData(complaintFavoriteListRes.getListObject());
			return new Response(SUCCESS, SUCCESS_CODE, null, null,null,dto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response getPatientMLCDocumentDetailList(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException {
		 try {
	            return iPatientRegistrationDao.getPatientMLCDocumentDetailList(patientRegistrationDto);
	    }catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	    
	    }


	@Override
	@Transactional
	public Response getDatabaseLink(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
		 try {
			 List<PatientRegistrationDto> responseCache = databaseLinkMasterCache.getIfPresent(patientRegistrationDto.getUnitId());
			 if (responseCache != null && !responseCache.isEmpty()) {
				 return new Response(SUCCESS, SUCCESS_CODE, "List Size Cache: " +String.valueOf(responseCache.size()), responseCache, null);
			 }

			 Response res = iPatientRegistrationDao.getDatabaseLink(patientRegistrationDto);
			 if(res.getStatus().equals(SUCCESS) && res.getListObject() != null && !res.getListObject().isEmpty()){
				 databaseLinkMasterCache.put(patientRegistrationDto.getUnitId(), res.getListObject());

				 return new Response(SUCCESS, SUCCESS_CODE, "List Size DB: " +String.valueOf(res.getListObject().size()), res.getListObject(), null);
			 }
			 return res;
	    }catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	    
	    }
    
	/*@Override
	@Transactional
	public Response patientRegisterONP(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
>>>>>>> Stashed changes
		try {
			return iPatientRegistrationDao.checkSqlConnection(patientRegistrationDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}*/

	private String generateFamilyId(int patId) throws ApplicationException {
		try {
			String str = "1_" + generateRandomString() + "_" + patId;
			return str;
		} catch (Exception e) {
		logger.error("generateFamilyId failed", e);
		}

		return "error";
	}
	
	
	public static String generateRandomString(){
		try{

			StringBuilder salt = new StringBuilder();

	        while (salt.length() < 9) { 
	            int index = (int) (random.nextFloat() * CHARS.length());

	            salt.append(CHARS.charAt(index));

	        }

	        String saltStr = salt.toString();

	        return saltStr;

		}catch(Exception e){
		logger.error("generateRandomString failed", e);
		}

		return null;

	}


	@Override
	public Response getVideoConsultationLinkForAppFromHIS(String uuid) throws ApplicationException {
		 try {
	            return iPatientRegistrationDao.getVideoConsultationLinkForAppFromHIS(uuid);
	    }catch (Exception e) {
	    logger.error("getVideoConsultationLinkForAppFromHIS failed", e);
	    }
	    return new Response<>(ERROR, COMMON_ERROR_CODE, null, null, null);
	    }


	@Override
	public Response updatePatientAsDeath(PatientRegistrationDto patientRegistrationDto) throws ApplicationException {
        try {
            return iPatientRegistrationDao.updatePatientAsDeath(patientRegistrationDto);
        } catch (Exception e) {
        logger.error("updatePatientAsDeath failed", e);
        }
        return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }




	@Override
	@Transactional
	public Response patientSearchPatientListForABDM(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException {
		try {

			return iPatientRegistrationDao.patientSearchPatientListForABDM(patientRegistrationDto);

	}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
}


	@Override
	@Transactional
	public Response updatePatientRegistrationFromMyLife(PatientMasterDto patientMasterDto) throws ApplicationException, Exception {
		Integer patientId=null;
		Integer slotId=null;
		Integer genderId = 1;
		Integer prefixId = 1;
		Character isNewPatient='N';
		Response patientCategoryRes=null;

		ScheduleAppointmentReqDto reqDto = new ScheduleAppointmentReqDto();

		Response res = iScheduleLogsDao.getConnectionStringForMyLife();
		MyLifeConnectionLinkDto connectionLinkDto=(MyLifeConnectionLinkDto)res.getObject();
		String MY_LIFE_URI =connectionLinkDto.getMyLifeLink();
		UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();

		unitApplicationConfigurationDto.setOrganizationId(patientMasterDto.getHospitalId());
		unitApplicationConfigurationDto.setUnitId(patientMasterDto.getUnitId());
		reqDto.setUnitId(patientMasterDto.getUnitId());
		reqDto.setHospitalId(patientMasterDto.getHospitalId());
		reqDto.setUuid(patientMasterDto.getUuid());
		reqDto.setCountry(patientMasterDto.getCountry());
		reqDto.setCity(patientMasterDto.getCity());
		reqDto.setState(patientMasterDto.getState());
		reqDto.setNationality(patientMasterDto.getNationality());
		Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
		unitApplicationConfigurationDto=(UnitApplicationConfigurationDto)appConfigRes.getObject();

		if(patientMasterDto.getGlobalUhid() != null)
		{
			//get patient id by UUID
			patientId= iPatientRegistrationDao.getPatientIdByUuidAndHospitalId(reqDto);//******
		}

		if(patientMasterDto.getGender().toLowerCase().equals("male"))
		{
			genderId = 1;
		}else if(patientMasterDto.getGender().toLowerCase().equals("female"))
		{
			genderId = 2;
		}

		String prefixDesc=null;
		prefixDesc = patientMasterDto.getPrefix();//*****

		prefixId=iGenderMasterDao.getPrefixFromGenderId(genderId,patientMasterDto.getHospitalId(),prefixDesc);

		if(patientId != null && patientId > 0) {
			PatientRegistrationDto patient = new PatientRegistrationDto();

			UnitMasterDto unitMasterDto = iGlobalUnitMasterDao.getUnitsAddress(reqDto.getUnitId());

			Response contryRes = iPatientRegistrationDao.getcontryIdFromCountryName(reqDto);
			reqDto = (ScheduleAppointmentReqDto) contryRes.getObject();

			if(reqDto.getCountryId() == null && reqDto.getCountry() != null){
				CountryMasterDto countryMasterDto = new CountryMasterDto();
				java.util.Date createdUtilDate = TimeZoneComponent.getDateByzone(reqDto.getUnitId());
				SimpleDateFormat sdf = new SimpleDateFormat("DD-MM-YYYY HH:mm:ss");
				String formattedDate = sdf.format(createdUtilDate);

				countryMasterDto.setCountryCode(reqDto.getCountry().substring(0,2));
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
				CountryMaster countryMaster = (CountryMaster)countryMasterRes.getObject();

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

			if(reqDto.getCityId() == null && reqDto.getCity() != null){
				reqDto.setPatAddressDesc(reqDto.getPatAddressDesc() + " , " +reqDto.getCity());
			}

			patient.setCountryId(reqDto.getCountryId());
			patient.setCityId(reqDto.getCityId());
			patient.setStateId(reqDto.getStateId());
			patient.setNationalityId(reqDto.getNationalityId());
			patient.setPatientId(patientId);
			patient.setIsPreReg('N');
			patient.setIsPrimary('Y');
			patient.setIsOtcReg('N');
			patient.setRegistrationTypeId(1);
			patient.setIsUnknownReg('N');
			patient.setFirstName(patientMasterDto.getFirstName());
			patient.setMiddleName(patientMasterDto.getMiddleName() != null ? patientMasterDto.getMiddleName() : "");
			patient.setLastName(patientMasterDto.getLastName() != null ? patientMasterDto.getLastName() : "");
			patient.setDob(patientMasterDto.getDateOfBirth() + " 00:00:00");
			patient.setOrganizationId(patientMasterDto.getHospitalId());
			patient.setUnitId(patientMasterDto.getUnitId());
			patient.setPatientCategoryId(unitApplicationConfigurationDto.getDefaultPatientCategoryId());
			patient.setGenderId(genderId);
			patient.setPrefixId(prefixId);
			patient.setUuid(patientMasterDto.getUuid());
			patient.setZipCode(patientMasterDto.getPincode());
			patient.setCountryCallingCode(reqDto.getCode());// *******
			patient.setWhatsappCallingCode(reqDto.getWhatsappCallingCode());//*****
			patient.setAddress(patientMasterDto.getAddressDesc()); //++++
			patient.setUnitId(patientMasterDto.getUnitId()); //++++
			patient.setOrganizationId(patientMasterDto.getHospitalId()); //++++
			patient.setImagePath(patientMasterDto.getPatientPhoto()); //++++

			if (patientMasterDto.getMobileNumber() != null) {
				patient.setMobileNumber(patientMasterDto.getMobileNumber());
				patient.setWhatsappNo(patientMasterDto.getTwitterId());
			}
			if (patientMasterDto.getFamilyId() != null) {
				patient.setFamilyId(patientMasterDto.getFamilyId());//++++
			}
			if (patientMasterDto.getEmailId() != null) {
				patient.setEmail(patientMasterDto.getEmailId());
			}
			Response updatePatientRes = updatepatientRegistration(patient);

			return updatePatientRes;
		}
		return new Response(ERROR, COMMON_ERROR_CODE, "Patient Id not Found", null, null);
	}


	public Response patientSearchPatientListForABDMForDeepLinking(PatientRegistrationDto patientRegistrationDto)
			throws ApplicationException {
		try {

			return iPatientRegistrationDao.patientSearchPatientListForABDMForDeepLinking(patientRegistrationDto);

	}catch (Exception e) {
	logger.error("patientSearchPatientListForABDMForDeepLinking failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, null, null, null);
}


	@Override
    @Transactional
    public Response sendRealTimeSyncCallToMyLife(Integer patientId) {
        try
        {
            String MY_LIFE_URI = null;
            Response res=iScheduleLogsDao.getConnectionStringForMyLife();
            MyLifeConnectionLinkDto connectionLinkDto=(MyLifeConnectionLinkDto)res.getObject();
            if(connectionLinkDto!=null && connectionLinkDto.getMyLifeLink()!=null && connectionLinkDto.getMyLifeLink()!="" && connectionLinkDto.getGlobalSchedulingLink()!=null && connectionLinkDto.getGlobalSchedulingLink()!="")
            {
                MY_LIFE_URI = connectionLinkDto.getMyLifeLink(); 
            }
            Response commonResponse = null;
            URI url = null;
            Response response = null;
            response = iPatientMasterDao.getPatientByIdForSync(patientId);
            PatientSyncDto patient = (PatientSyncDto)response.getObject();
            if(patient != null){
				patient.setPatientFromHIS(1);
				patient.setHospitalId(patient.getGlobalOrgId() != null ? patient.getGlobalOrgId() : patient.getHospitalId());
//				url = URI.create("http://localhost:9091/MyLifeGlobal/api" + "/sync/patients/realtime");
				url = URI.create(MY_LIFE_URI + "/sync/patients/realtime");
//				url = URI.create("https://e478233d9631.ngrok-free.app/MyLifeGlobal/api" + "/sync/patients/realtime");
				HttpHeaders headers = new HttpHeaders();
				headers.set("channelId", (patient.getChannelId() != null && patient.getChannelId() != 0) ? patient.getChannelId().toString():"1");
				HttpEntity<PatientSyncDto> request = new HttpEntity<>(patient, headers);
				ObjectMapper mapper = new ObjectMapper();
				System.out.println(
						mapper.writerWithDefaultPrettyPrinter()
								.writeValueAsString(request.getBody())
				);
            	String stringResponse = restTemplate.postForObject(url, request, String.class);
            	commonResponse = gson.fromJson(stringResponse, Response.class);
				if(commonResponse.getStatus().equalsIgnoreCase(ICommonConstants.SUCCESS)){
					if(commonResponse.getObject() != null)
						iPatientMasterDao.updateUuidByPatient(patient.getPatientId(), (String)commonResponse.getObject());
						iScheduleLogsDao.markScheduleLogsCompletedUsingPatientId(patient.getPatientId());
                }else{
                    iScheduleLogsDao.markScheduleLogsError(patient.getPatientId());
                }
            }
            return new Response(SUCCESS, SUCCESS_CODE, null, null,null,null);
        }catch (Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}

    }

	@Override
	@Transactional
	public Response getInitalAssesmentData(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			return iPatientMasterDao.getInitalAssesmentData(slotMasterDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
    
	
}