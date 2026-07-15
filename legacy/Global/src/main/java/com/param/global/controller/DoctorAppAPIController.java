package com.param.global.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.BASE64DecodedMultipartFile;
import com.param.global.common.BASE64DecodedMultipartFilePDF;
import com.param.global.common.Base64EnCodeDeCode;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.LabReportReqDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.UserMasterDto;
import com.param.global.dto.global.InstructionMasterDto;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.org.common.service.IInstructionMasterService;
import com.param.global.org.service.IGlobalOrganizationMasterService;
import com.param.global.service.IICPCMapperService;
import com.param.global.service.IPatientRegistrationService;
import com.param.global.service.IUnitApplicationBillingConfigurationService;
import com.param.global.service.IUserMasterService;
import com.param.global.unit.dto.AllergyPatientMapperDto;
import com.param.global.unit.dto.LabConfigurationDto;
import com.param.global.unit.service.ILabConfigurationService;
import com.param.opd.coversheet.dto.AppointmentVitalMapperDto;
import com.param.opd.coversheet.dto.DiagnosisPatientAppointmentMapperDto;
import com.param.opd.coversheet.dto.MedicationHistoryDto;
import com.param.opd.coversheet.dto.PatientFamilyHistoryDto;
import com.param.opd.coversheet.dto.PatientHabitDetailsDto;
import com.param.opd.coversheet.dto.PatientMedicalHistoryDto;
import com.param.opd.coversheet.dto.PatientPersonalHistoryDetailsDto;
import com.param.opd.coversheet.dto.PatientSurgicalHistoryDto;
import com.param.opd.coversheet.dto.PatientTravelHistoryDetailsDto;
import com.param.opd.coversheet.dto.PatientTreatmentHistoryDetailsDto;
import com.param.opd.coversheet.dto.PrescriptionDto;
import com.param.opd.coversheet.service.IAllergyPatientMapperService;
import com.param.opd.coversheet.service.IAppointmentVitalMapperService;
import com.param.opd.coversheet.service.IComplaintAppointmentMapperService;
import com.param.opd.coversheet.service.IMedicationService;
import com.param.opd.coversheet.service.IPatientDiagnosisService;
import com.param.opd.coversheet.service.IPatientHabitDetailsMapperService;
import com.param.opd.coversheet.service.IPatientMedicalHistoryService;
import com.param.opd.coversheet.service.IPatientPersonalHistoryDetailsService;
import com.param.opd.coversheet.service.IPatientSurgicalHistoryService;
import com.param.opd.coversheet.service.IPatientTravelHistoryDetailsService;
import com.param.opd.coversheet.service.IPatientTreatmentHistoryDetailsService;
import com.param.opd.coversheet.service.IVaccineImmunizationMapperService;
import com.param.opd.coversheet.service.IpatientFamilyHistoryService;
import com.param.opd.encounter.dto.EncounterDocDetailsDto;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;

@RestController
@RequestMapping("/api/doctorApp")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DoctorAppAPIController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(DoctorAppAPIController.class);

	@Autowired
	private IEncounterMasterService iEncounterMasterService;

	@Autowired
	private IGlobalOrganizationMasterService iGlobalOrganizationMasterService;

	@Autowired
	IUserMasterService iUserMasterService;
	
	@Autowired
	IICPCMapperService iICPCMapperService;
	
	@Autowired
	IPatientDiagnosisService iPatientDiagnosisService;
	
	@Autowired
	private IComplaintAppointmentMapperService iComplaintAppointmentMapperService;
	
	@Autowired
	IInstructionMasterService iInstructionMasterService;
	
	@Autowired
	IMedicationService iMedicationService;
	
	@Autowired
	private IPatientRegistrationService iPatientRegistrationService;
	
	@Autowired
	IAppointmentVitalMapperService iAppointmentVitalMapperService;
	
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	IPatientMedicalHistoryService iPatientMedicalHistoryService;
	@Autowired	
	IPatientSurgicalHistoryService iPatientSurgicalHistoryService;
	

	@Autowired
    IpatientFamilyHistoryService iPatientFamilyHistoryService;
	
	@Autowired
	IAllergyPatientMapperService iAllergyPatientMapperService;
	
	@Autowired
	IPatientHabitDetailsMapperService iPatientHabitDetailsMapperService;
	
	@Autowired
	IUnitApplicationBillingConfigurationService iUnitApplicationBillingConfigurationService;
	
	@Autowired
	ILabConfigurationService iLabConfigurationService;
	
	@Autowired
	IPatientPersonalHistoryDetailsService iPatientPersonalHistoryDetailsService;
	
	@Autowired
	IPatientTravelHistoryDetailsService iPatientTravelHistoryDetailsService;
	
	@Autowired
	IPatientTreatmentHistoryDetailsService iPatientTreatmentHistoryDetailsService;
	
	@Autowired
	IVaccineImmunizationMapperService ivaccineImmunizationMapperService;
	
	@RequestMapping(value = "/getOrganizationList/{orgName}", method = RequestMethod.GET)
	public Response getOrganizationListByOrgName(@PathVariable("orgName") String orgName) {
		try {
			return iGlobalOrganizationMasterService.getOrganizationListByOrgName(orgName);
		} catch (Exception e) {
			logger.error("Error while fetching the organization list by name", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value= "/userLogin" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody  Response userLogin(@RequestBody UserMasterDto userMasterDto){
		try{
			return iUserMasterService.validateLoginForDoctorApp(userMasterDto);
		}catch(Exception e){
			logger.error("Error while validating the doctor app user login", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value = "/getAppointmentList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getOpPatientListByDocId(@RequestBody EncounterMasterDto encounterMasterDto) {
		try {
			String currDate=GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()), "yyyy-MM-dd");
			encounterMasterDto.setFromDate(currDate);
			encounterMasterDto.setToDate(currDate);
			return iEncounterMasterService.getOpPatientListByDocId(encounterMasterDto);
		} catch (Exception e) {
			logger.error("Error while fetching the OP patient list by doctor id", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getICPCListByName/{IcpcName}", method = RequestMethod.GET)
	public Response getICPCByName(@PathVariable("IcpcName") String IcpcName) {
		try {
			return iICPCMapperService.getICPCByName(IcpcName);
		} catch (Exception e) {
			logger.error("Error while fetching the ICPC list by name", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value = "/searchMedicationByBrandName", method = RequestMethod.GET)
	public @ResponseBody Response searchMedicationByBrandName(@RequestParam("unitId") Integer unitId,
			@RequestParam("orgId") Integer orgId, @RequestParam("searchKeyword") String searchKeyword) {
		try {
			return iMedicationService.searchMedicationByBrandName(unitId, orgId, searchKeyword);
		} catch (Exception e) {
			logger.error("Error while searching the medication by brand name", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@RequestMapping(value = "/searchMedicationByGenericName", method = RequestMethod.GET)
	public @ResponseBody Response searchMedicationByGenericName(@RequestParam("unitId") Integer unitId,
			@RequestParam("orgId") Integer orgId, @RequestParam("searchKeyword") String searchKeyword) {
		try {
			return iMedicationService.getGenericListForGenericSearch(unitId, orgId, searchKeyword);
		} catch (Exception e) {
			logger.error("Error while searching the medication by generic name", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getPatientDiagnosis", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientDiagnosisByPatientId(@RequestBody DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto) {
		try {
			diagnosisPatientAppointmentMapperDto.setPatientId(diagnosisPatientAppointmentMapperDto.getPatientId());
			diagnosisPatientAppointmentMapperDto.setOrganizationId(diagnosisPatientAppointmentMapperDto.getOrganizationId());
			return iPatientDiagnosisService.getPatientDiagnosisByPatientId(diagnosisPatientAppointmentMapperDto);
		} catch (Exception e) {
			logger.error("Error while fetching the patient diagnosis by patient id", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getMedicationInstructions", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getMedicationInstructions(@RequestBody InstructionMasterDto instructionMasterDto) {
		try {
			return iInstructionMasterService.getActiveInspectionMasterList(instructionMasterDto);
		} catch (Exception e) {
			logger.error("Error while fetching the medication instructions", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getPatientMedicationHistory", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientMedicationHistory(@RequestBody MedicationHistoryDto medicationHistoryDto) {
		try {
			return iMedicationService.getPatientMedicationHistoryForDoctorApp(medicationHistoryDto);
		} catch (Exception e) {
			logger.error("Error while fetching the patient medication history", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getPatientMedications", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientMedications(@RequestBody PrescriptionDto prescriptionDto) {
		try {
			//return iMedicationService.getMedicationDetailsByDoctorId(prescriptionDto);
			return iMedicationService.getOrderedMedicationsForDoctorApp(prescriptionDto);
		} catch (Exception e) {
			logger.error("Error while fetching the patient medications", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value="/getPatientVitals", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientVitals(@RequestBody AppointmentVitalMapperDto appointmentVitalMapperDto) {
		try
		{
			return iAppointmentVitalMapperService.getListOfVitalsByPatientIdForDoctorApp(appointmentVitalMapperDto);
		}catch(Exception e)
		{
			logger.error("Error while fetching the patient vitals", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="getPreviousAppointmentCasesheets", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getEncounterDetailsForCoversheet(@RequestBody EncounterMasterDto encounterMasterDto) {
		try {
			return iEncounterMasterService.getEncounterDetailsForCoversheet(encounterMasterDto);
		}catch (Exception e) {
			logger.error("Error while fetching the previous appointment casesheets", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="printCoversheet", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response printPreviousCoversheet(@RequestBody EncounterMasterDto encounterMasterDto) throws IOException {
		   ByteArrayOutputStream byteOutPutStream = null;
	       CloseableHttpClient httpClient = null;
	       OutputStream output = null;
	   try{
	    
		   httpClient = HttpClientBuilder.create().build();
		   byteOutPutStream  = new ByteArrayOutputStream();
	       String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(encounterMasterDto.getOrganizationId(),encounterMasterDto.getUnitId());
	       
	       if(API_URL==null)
	    	   return null;
	       
	      String reportParam=" {"
               +"'appName': 'HIS',"
               +"'appSecretKey': 'f306b6eccd9d5069d2dc71afe139271a',"
               +"'reportDesignFilePath': 'NewCoverSheet.rpt',"
               +"'reportFormat': 'PDF',"
               +"'exportFileName': 'Coversheet',"
               +"'reportParams': ["
                 +"{"
	               +"'paramName': 'Patientid',"
			       +"'paramType': 'Integer',"
			       +"'paramValue':"+encounterMasterDto.getPatientId()
	              +"}," 
	              +"{"
	               +"'paramName': 'Unitid',"
			       +"'paramType': 'Integer',"
			       +"'paramValue':"+encounterMasterDto.getUnitId()// $scope.unitId
	              +"}," 
	              +"{"
	               +"'paramName': 'Encounter_id',"
			       +"'paramType': 'Integer',"
			       +"'paramValue':"+encounterMasterDto.getEncounterId()// $scope.encounterDetailsObj.encounterId
	              +"}" 
               +"]"
               +"}" ;
	      
	       HttpPost  post = new HttpPost(API_URL);
	       post.addHeader("Content-Type","application/json");
	       post.setEntity(new StringEntity(reportParam));
	    
	       HttpResponse result = httpClient.execute(post);
	       HttpEntity resEntity = result.getEntity(); 
	       resEntity.writeTo(byteOutPutStream);
	       
	       byteOutPutStream.toByteArray();
	       httpClient.close();
	       byteOutPutStream.close();
	       return new Response<>(SUCCESS, null,null, null, byteOutPutStream.toByteArray());
	   }catch(Exception e)
	   {
		   logger.error("Error while printing the previous coversheet", e);
	   }
	   finally {
		   if(output!=null)
			   output.close();
	       if(httpClient!=null)
	    	   httpClient.close();
	       if(byteOutPutStream!=null)
	    	   byteOutPutStream.close();
	    }
	   return null;
	  }

	@RequestMapping(value="/getPatientMedicalHistory", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientMedicalHistory(@RequestBody PatientMedicalHistoryDto patientMedicalHistoryDto) {
		try
		{
			
			return iPatientMedicalHistoryService.getPatientMedicalHistory(patientMedicalHistoryDto);
		}catch(Exception e)
		{
			logger.error("Error while fetching the patient medical history", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getPatientSurgicalHistory",  method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientSurgicalHistory(@RequestBody PatientSurgicalHistoryDto patientSurgicalHistoryDto) {
		try
		{
			return iPatientSurgicalHistoryService.getPatientSurgicalHistory(patientSurgicalHistoryDto);
		}catch(Exception e)
		{
			logger.error("Error while fetching the patient surgical history", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@RequestMapping(value="/getPatientFamilyHistory",  method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientFamilyHistory(@RequestBody PatientFamilyHistoryDto patientFamilyHistoryDto) {
		try
		{
			return iPatientFamilyHistoryService.getPatientFamilyHistory(patientFamilyHistoryDto);
		}catch(Exception e)
		{
			logger.error("Error while fetching the patient family history", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getPersonalHistory",  method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPersonalHistory(@RequestBody PatientPersonalHistoryDetailsDto patientPersonalHistoryDetailsDto) {
		try
		{
			return iPatientPersonalHistoryDetailsService.getPersonalHistory(patientPersonalHistoryDetailsDto);
		}catch(Exception e)
		{
			logger.error("Error while fetching the patient personal history", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getPatientTravelHistory",  method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientTravelHistory(@RequestBody PatientTravelHistoryDetailsDto patientTravelHistoryDetailsDto) {
		try
		{
			return iPatientTravelHistoryDetailsService.getPatientTravelHistory(patientTravelHistoryDetailsDto);
		}catch(Exception e)
		{
			logger.error("Error while fetching the patient travel history", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getInvestigationHistory",  method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getInvestigationHistory(@RequestBody PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) {
		try
		{
			return iPatientTreatmentHistoryDetailsService.getInvestigationHistory(patientTreatmentHistoryDetailsDto);
		}catch(Exception e)
		{
			logger.error("Error while fetching the patient investigation history", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/uploadPatientDocument",  method=RequestMethod.POST)
	public @ResponseBody Response uploadPatientDocument(@RequestBody EncounterDocDetailsDto encounterDocDetailsDto) {
		try
		{
			String base64String[] = encounterDocDetailsDto.getFileContent().split(",");
			String contentTypeString=base64String[0];
			String contentType=contentTypeString.substring((contentTypeString.indexOf(":"))+1,contentTypeString.indexOf(";"));
			String fileContent=base64String[1];
			byte[] imageByte = Base64EnCodeDeCode.decodeImage(fileContent.getBytes());
			if(contentType.equals("application/pdf"))
			    encounterDocDetailsDto.setFile(new BASE64DecodedMultipartFilePDF(imageByte));
			else
				encounterDocDetailsDto.setFile(new BASE64DecodedMultipartFile(imageByte));
			encounterDocDetailsDto.setUploadDoccumentDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterDocDetailsDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));	
			return iEncounterMasterService.saveDocumentAgainstEncounter(encounterDocDetailsDto);
		}catch(Exception e)
		{
			logger.error("Error while uploading the patient document", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getPatientAllergies",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientAllergies(@RequestBody AllergyPatientMapperDto allergyPatientMapperDto) {
		try
		{
			return iAllergyPatientMapperService.getPatientAllergies(allergyPatientMapperDto);
		}catch(Exception e)
		{
			logger.error("Error while fetching the patient allergies", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getPatientDocuments",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientDocuments(@RequestBody EncounterDocDetailsDto encounterDocDetailsDto) {
		try
		{
			return iPatientRegistrationService.getPatientDocuments(encounterDocDetailsDto);
		}catch(Exception e)
		{
			logger.error("Error while fetching the patient documents", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getPatientHabits",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientHabits(@RequestBody PatientHabitDetailsDto patientHabitDetailsDto) {
		try
		{
			return iPatientHabitDetailsMapperService.getCurrentPatientHabitDetails(patientHabitDetailsDto);
		}catch(Exception e)
		{
			logger.error("Error while fetching the patient habits", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="generatePatientCasesheet", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response printCasesheet(@RequestBody EncounterMasterDto encounterMasterDto) throws IOException {
		   ByteArrayOutputStream byteOutPutStream = null;
	       CloseableHttpClient httpClient = null;
	       OutputStream output = null;
	   try{
	    
		   httpClient = HttpClientBuilder.create().build();
		   byteOutPutStream  = new ByteArrayOutputStream();
	       String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(encounterMasterDto.getOrganizationId(),encounterMasterDto.getUnitId());
	       
	       if(API_URL==null)
	    	   return null;
	       
	      String reportParam=" {"
               +"'appName': 'HIS',"
               +"'appSecretKey': 'f306b6eccd9d5069d2dc71afe139271a',"
               +"'reportDesignFilePath': 'NewCoverSheetIPD.rpt',"
               +"'reportFormat': 'PDF',"
               +"'exportFileName': 'CompleteConsultation',"
               +"'reportParams': ["
                 +"{"
	               +"'paramName': 'Patientid',"
			       +"'paramType': 'Integer',"
			       +"'paramValue':"+encounterMasterDto.getPatientId()
	              +"}," 
	              +"{"
	               +"'paramName': 'Unitid',"
			       +"'paramType': 'Integer',"
			       +"'paramValue':"+encounterMasterDto.getUnitId()// $scope.unitId
	              +"}," 
	              +"{"
	               +"'paramName': 'admission_id',"
			       +"'paramType': 'Integer',"
			       +"'paramValue':"+encounterMasterDto.getAdmissionId()// $scope.encounterDetailsObj.encounterId
	              +"}" 
               +"]"
               +"}" ;
	      
	       HttpPost  post = new HttpPost(API_URL);
	       post.addHeader("Content-Type","application/json");
	       post.setEntity(new StringEntity(reportParam));
	    
	       HttpResponse result = httpClient.execute(post);
	       HttpEntity resEntity = result.getEntity(); 
	       resEntity.writeTo(byteOutPutStream);
	       
	       byteOutPutStream.toByteArray();
	       httpClient.close();
	       byteOutPutStream.close();
	       return new Response<>(SUCCESS, null,null, null, byteOutPutStream.toByteArray());
	   }catch(Exception e)
	   {
		   logger.error("Error while printing the patient casesheet", e);
	   }
	   finally {
		   if(output!=null)
			   output.close();
	       if(httpClient!=null)
	    	   httpClient.close();
	       if(byteOutPutStream!=null)
	    	   byteOutPutStream.close();
	    }
	   return null;
	  }

	@RequestMapping(value="/getApplicationConfig",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getApplicationConfigForDocApp(@RequestBody UnitApplicationConfigurationDto unitApplicationConfigurationDto) {
		try
		{
			return iUnitApplicationBillingConfigurationService.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
		}catch(Exception e)
		{
			logger.error("Error while fetching the application config for doctor app", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/deletePatientDocument",  method=RequestMethod.POST)
	public @ResponseBody Response updatePatientDocumentStatus(@RequestBody EncounterDocDetailsDto encounterDocDetailsDto) {
		try
		{
			return iPatientRegistrationService.deletePatientDocument(encounterDocDetailsDto);
		}catch(Exception e)
		{
			logger.error("Error while deleting the patient document", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/printLabReport",  method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response printLabReport(@RequestBody LabReportReqDto labReportReqDto) throws IOException {
		   ByteArrayOutputStream byteOutPutStream = null;
	       CloseableHttpClient httpClient = null;
	       OutputStream output = null;
	   try{
		   String reportParam="";
		   httpClient = HttpClientBuilder.create().build();
		   byteOutPutStream  = new ByteArrayOutputStream();
	       String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(labReportReqDto.getOrganizationId(),labReportReqDto.getUnitId());
	       if(API_URL==null)
	    	   return null;
	       Response labRes=iLabConfigurationService.getLabConfigurationByOrgUnit(labReportReqDto.getOrganizationId(),labReportReqDto.getUnitId());
	       LabConfigurationDto labConfigDto=(LabConfigurationDto) labRes.getObject();
	       
	       if(labConfigDto.getBiochemistryId().equals(labReportReqDto.getSubDeptId()) ||
	    		   labConfigDto.getClinicalPathologyId().equals(labReportReqDto.getSubDeptId()) || 
	    		   labConfigDto.getHematologyId().equals(labReportReqDto.getSubDeptId())) {
	    	   reportParam=" {"
	    	            +"'appName': 'HIS',"
	    	            +"'appSecretKey': 'f306b6eccd9d5069d2dc71afe139271a',"
	    	            +"'reportDesignFilePath': 'pathologyReport.rpt',"
	    	            +"'reportFormat': 'PDF',"
	    	            +"'exportFileName': 'pathologyReport',"
	    	            +"'reportParams': ["
	    	              +"{"
	    		               +"'paramName': 'labtesresid',"
	    				       +"'paramType': 'Integer',"
	    				       +"'paramValue':"+labReportReqDto.getLabTestResId()
	    		              +"}," 
	    		              +"{"
	    		               +"'paramName': 'isOriginal',"
	    				       +"'paramType': 'Integer',"
	    				       +"'paramValue':1"
	    		              +"}" 
	    	            +"]"
	    	            +"}" ; 
	       }else if(labConfigDto.getMicrobiologyId().equals(labReportReqDto.getSubDeptId())) {
	    	   reportParam=" {"
	    	            +"'appName': 'HIS',"
	    	            +"'appSecretKey': 'f306b6eccd9d5069d2dc71afe139271a',"
	    	            +"'reportDesignFilePath': 'pathologyReport_Sensitivity.rpt',"
	    	            +"'reportFormat': 'PDF',"
	    	            +"'exportFileName': 'pathologyReport',"
	    	            +"'reportParams': ["
	    	              +"{"
	    		               +"'paramName': 'lab_sample_dtls_id',"
	    				       +"'paramType': 'Integer',"
	    				       +"'paramValue':"+labReportReqDto.getLabSampleDtlsId()
	    		              +"}," 
	    		              +"{"
	    		               +"'paramName': 'isOriginal',"
	    				       +"'paramType': 'Integer',"
	    				       +"'paramValue':1"
	    		              +"}" 
	    	            +"]"
	    	            +"}" ;
	       }else if(labConfigDto.getHistopathologyId().equals(labReportReqDto.getSubDeptId()) ||
	    		   labConfigDto.getCystopathologyId().equals(labReportReqDto.getSubDeptId()) ) {
	    	   reportParam=" {"
	    	            +"'appName': 'HIS',"
	    	            +"'appSecretKey': 'f306b6eccd9d5069d2dc71afe139271a',"
	    	            +"'reportDesignFilePath': 'GetHistopathologyReport.rpt',"
	    	            +"'reportFormat': 'PDF',"
	    	            +"'exportFileName': 'pathologyReport',"
	    	            +"'reportParams': ["
	    	              +"{"
	    		               +"'paramName': 'labtesresid',"
	    				       +"'paramType': 'Integer',"
	    				       +"'paramValue':"+labReportReqDto.getLabSampleDtlsId()
	    		              +"}," 
	    		              +"{"
	    		               +"'paramName': 'isOriginal',"
	    				       +"'paramType': 'Integer',"
	    				       +"'paramValue':1"
	    		              +"}" 
	    	            +"]"
	    	            +"}" ;
	       }else if(labConfigDto.getSerologyId().equals(labReportReqDto.getSubDeptId())) {
	    	   reportParam=" {"
	    	            +"'appName': 'HIS',"
	    	            +"'appSecretKey': 'f306b6eccd9d5069d2dc71afe139271a',"
	    	            +"'reportDesignFilePath': 'SerologyReport.rpt',"
	    	            +"'reportFormat': 'PDF',"
	    	            +"'exportFileName': 'pathologyReport',"
	    	            +"'reportParams': ["
	    	              +"{"
	    		               +"'paramName': 'lab_sample_dtls_id',"
	    				       +"'paramType': 'Integer',"
	    				       +"'paramValue':"+labReportReqDto.getLabSampleDtlsId()
	    		              +"}," 
	    		              +"{"
	    		               +"'paramName': 'isOriginal',"
	    				       +"'paramType': 'Integer',"
	    				       +"'paramValue':1"
	    		              +"}" 
	    	            +"]"
	    	            +"}" ;
	       }
	     
	      
	       HttpPost  post = new HttpPost(API_URL);
	       post.addHeader("Content-Type","application/json");
	       post.setEntity(new StringEntity(reportParam));
	    
	       HttpResponse result = httpClient.execute(post);
	       HttpEntity resEntity = result.getEntity(); 
	       resEntity.writeTo(byteOutPutStream);
	       
	       byteOutPutStream.toByteArray();
	       httpClient.close();
	       byteOutPutStream.close();
	       return new Response<>(SUCCESS, null,null, null, byteOutPutStream.toByteArray());
	   }catch(Exception e)
	   {
		   logger.error("Error while printing the lab report", e);
	   }
	   finally {
		   if(output!=null)
			   output.close();
	       if(httpClient!=null)
	    	   httpClient.close();
	       if(byteOutPutStream!=null)
	    	   byteOutPutStream.close();
	    }
	   return null;
	}


}
