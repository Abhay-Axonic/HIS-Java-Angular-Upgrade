package com.param.global.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.param.global.dto.sync.PatientMasterDto;
import com.param.global.dto.sync.ScheduleAppointmentReqDto;
import com.param.opd.appointment.model.AppointmentMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.param.global.common.HealthCardPdf;
import com.param.global.common.ICommonConstants;
import com.param.global.common.ISmsUtilityConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.common.SmsUtility;
import com.param.global.dto.FingerPrintSearchDto;
import com.param.global.dto.NewONPRegistrationDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.dto.PatientVisitsDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.mail.IMailingService;
import com.param.global.service.IPatientRegistrationService;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;

@RestController
@RequestMapping(value="api/global")
@SuppressWarnings({ "unchecked", "rawtypes"})
public class PatientRegistrationController implements ICommonConstants,ISmsUtilityConstants{

	private static final Logger logger = Logger.getLogger(PatientRegistrationController.class);

	@Autowired
	private IPatientRegistrationService iPatientRegistrationService;
	
	@Autowired
	private HealthCardPdf healthCardPdf;
	
	@Autowired
	SmsUtility smsUtility;
	
	@Autowired
	 IMailingService iMailingService;
	
	@Autowired
	private ISmsUtilityConfigService smsUtilityService;
	
	@RequestMapping(value="patient/{patientId}",method=RequestMethod.GET)
	public Response saveOrganizationServiceMaster(@PathVariable("patientId") Integer patientId) {
		try {
			return iPatientRegistrationService.getPatientDetailsById(patientId);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
 /*  1- Normal Registrations.
	 2- OTC Registrations.
	 3- OTC to Normal Conversion.
	 4- Temp to Normal Conversion.
	 5- Pre-Registration. 
	 6- Pre  to Normal Conversion*/
	
	@RequestMapping(value="/patientRegistration", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody Response savePatientRegistration(@RequestBody PatientRegistrationDto patientRegistrationDto) {
		try {
			 
			Response res1 = iPatientRegistrationService.savePatientRegistration(patientRegistrationDto);
            Integer patientId;

            if(res1.getStatus().equals(SUCCESS) && (patientRegistrationDto.getIsFromSelf()!=null && patientRegistrationDto.getIsFromSelf()=='Y')) {
                AppointmentMaster appointmentMaster = (AppointmentMaster) res1.getObject();
                patientId = appointmentMaster.getPatientId();
            }
            else{
                patientId = (Integer) res1.getObject();
            }
			/*if(res1.getStatus().equals(SUCCESS)) {
				smsUtility.patientRegistrationSms(patientRegistrationDto);
			}
			return res1;*/
			if(res1.getStatus().equals(SUCCESS) && (patientRegistrationDto.getIsFromSelf()==null || patientRegistrationDto.getIsFromSelf()=='N')) 
			{
				
				SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
					smsInfo.setOrganizationId(patientRegistrationDto.getOrganizationId());
					smsInfo.setUnitId(patientRegistrationDto.getUnitId());
					smsInfo.setPatientId(patientRegistrationDto.getPatientId());
				smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.REGISTRATION,smsInfo);
				
				if(patientRegistrationDto.getEmail()!=null && patientRegistrationDto.getEmail().length()>0) 
				{
					//iMailingService.sentRegistrationMailToPatient(patientRegistrationDto);
					SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
					emailinfo.setUnitId(patientRegistrationDto.getUnitId());
					emailinfo.setOrganizationId(patientRegistrationDto.getOrganizationId());
					emailinfo.setPatientEmail(patientRegistrationDto.getEmail());
//					emailinfo.setPatientId((Integer)res1.getObject()); to solve HIS-21240
					emailinfo.setPatientId(patientRegistrationDto.getPatientId());
					smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.REGISTRATION, emailinfo);
				}
				
				Response res1D = iPatientRegistrationService.getDatabaseLink(patientRegistrationDto);
				PatientRegistrationDto resDto= (PatientRegistrationDto)res1D.getObject();
				if(resDto.getMysqlOnp() != null && resDto.getMysqlOnp() != "") {
					iPatientRegistrationService.checkSqlConnection(patientRegistrationDto);
				}
				iPatientRegistrationService.sendRealTimeSyncCallToMyLife(patientId);
			}
			return res1;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/convertPatient", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody Response convertPatient(@RequestBody PatientRegistrationDto patientRegistrationDto) {
		try {
			return iPatientRegistrationService.convertPatient(patientRegistrationDto);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updatePatient", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody Response updatepatientRegistration(@RequestBody PatientRegistrationDto patientRegistrationDto) {
		try 
		{
			return iPatientRegistrationService.updatepatientRegistration(patientRegistrationDto);

		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getPatientDetails",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientDetais(@RequestBody PatientRegistrationDto patientRegistrationDto) {
		try {
			return iPatientRegistrationService.getPatientDetais(patientRegistrationDto);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value="/patientSearch",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response patientSearch(@RequestBody PatientRegistrationDto patientRegistrationDto) {
		try {

			return iPatientRegistrationService.patientSearch(patientRegistrationDto);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	
	@RequestMapping(value="/patientSearchAmb",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response patientSearchAmb(@RequestBody PatientRegistrationDto patientRegistrationDto) {
		try {
			return iPatientRegistrationService.patientSearch(patientRegistrationDto);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/patient/billing/{patientId}" , method = RequestMethod.GET)
	public @ResponseBody Response patientDetailsByIdForBilling(@PathVariable("patientId") int patientId){
		try{
			return iPatientRegistrationService.patientDetailsByIdForBilling(patientId);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null,e.getMessage());
		}
	}
	
	 @RequestMapping(value = "/patient/image/{orgId}/{unitId}/{patientId}", headers=("content-type=multipart/*"), method = RequestMethod.POST)
     public @ResponseBody Response savePatientImage(@PathVariable("orgId") int orgId,
                           @PathVariable("unitId") int unitId,
                           @PathVariable("patientId") int patientId,
                           @RequestParam("file") MultipartFile inputFile)
         {
	      try
	      {
	         return iPatientRegistrationService.savePatientImage(orgId,unitId,patientId,inputFile);
	       }catch(Exception e)
	      {
	      logger.error("method failed", e);
	      }
		return new Response<>(ERROR, null, null, null, null);
       }
	 @RequestMapping(value = "/getHealthCard", method = RequestMethod.POST)
		public Response printHealthCard(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
		{
			Response<Document, Integer> response = null;
			try
			{
				if (servletRequest.getParameter("nameOfReport") != null)
				{
					servletResponse.setContentType("application/pdf");
					ObjectMapper mapper = new ObjectMapper();
					String labTestResultObj = servletRequest.getParameter("nameOfReport");
					//System.out.println("labTestResultObj"+labTestResultObj);
					PatientRegistrationDto patientRegistrationDto = mapper.readValue(labTestResultObj, PatientRegistrationDto.class);
					Response response1 = iPatientRegistrationService.getPatientDetaisForHealthCard(patientRegistrationDto);
					PatientRegistrationDto patientRegistrationDto1 = (PatientRegistrationDto)response1.getObject();
					healthCardPdf.generateHealthCard(patientRegistrationDto1,servletResponse.getOutputStream());
					 return response;
				}
		
			 return response;
			} catch (Exception e)
			{
			logger.error("printHealthCard failed", e);
			return null;
			}
		}
	 
	 @RequestMapping(value="/patientAutofillSearch", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response patientAutofillSearch(@RequestBody SlotMasterDto slotMasterDto) {
			try {
					return iPatientRegistrationService.patientAutofillSearch(slotMasterDto);
				
			}catch (Exception e) {
			logger.error("method failed", e);
			}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 @RequestMapping(value="/patientAutofillSearchPlain", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response patientAutofillSearchPlain(@RequestBody SlotMasterDto slotMasterDto) {
			try {
					return iPatientRegistrationService.patientAutofillSearchPlain(slotMasterDto);
				
			}catch (Exception e) {
			logger.error("method failed", e);
			}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 
	 @RequestMapping(value="/patientTypeHeadSearchForSelfRegistration", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response patientTypeHeadSearchForSelfRegistration(@RequestBody SlotMasterDto slotMasterDto) {
			try {
					return iPatientRegistrationService.patientTypeHeadSearchForSelfRegistration(slotMasterDto);
			}catch (Exception e) {
			logger.error("method failed", e);
			}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 @RequestMapping(value="/patientTypeHeadSearchForAppointmentUnique", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response patientTypeHeadSearchForAppointmentUnique(@RequestBody SlotMasterDto slotMasterDto) {
			try {
					return iPatientRegistrationService.patientTypeHeadSearchForAppointmentUnique(slotMasterDto);
			}catch (Exception e) {
			logger.error("method failed", e);
			}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@RequestMapping(value="/patientTypeHeadSearchForAppointmentUniqueForNurseCheckIn", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response patientTypeHeadSearchForAppointmentUniqueForNurseCheckIn(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			return iPatientRegistrationService.patientTypeHeadSearchForAppointmentUniqueForNurseCheckIn(slotMasterDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	 
	 @RequestMapping(value="/saveFingerPring", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response saveFingerPring(@RequestBody PatientRegistrationDto dto) {
			try {
					return iPatientRegistrationService.savePatientFingerPrint(dto);
			}catch (Exception e) {
			logger.error("method failed", e);
			}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 @RequestMapping(value="/searchByFingerPrint", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response searchPatientByFingerPrint(@RequestBody FingerPrintSearchDto dto) {
		
		
			try {
					return iPatientRegistrationService.searchPatientByFingerPrint(dto);
				
			}catch (Exception e) {
			logger.error("method failed", e);
			}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 @RequestMapping(value = "/PatientDemography/{patientId}" , method = RequestMethod.GET)
		public @ResponseBody Response PatientDemography(@PathVariable("patientId") int patientId){
			try{
				return iPatientRegistrationService.PatientDemography(patientId);
			}catch(Exception e){
			logger.error("method failed", e);
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null,null);
			}
		}
	 @RequestMapping(value = "/updatePatientAsAbscound" , method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response updatePatientAsAbscound(@RequestBody PatientRegistrationDto patientRegistrationDto){
			try{
				return iPatientRegistrationService.updatePatientAsAbscound(patientRegistrationDto);
			}catch(Exception e){
			logger.error("method failed", e);
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null,null);
			}
		}
	 @RequestMapping(value = "/checkEmailDetails/{orgId}/{emailId}" , method=RequestMethod.GET)
		public @ResponseBody Response checkEmailDetails(@PathVariable("orgId") Integer orgId, @PathVariable("emailId") String emailId){
			try{
				return iPatientRegistrationService.checkEmailDetails(orgId,emailId);
			}catch(Exception e){
			logger.error("method failed", e);
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null,null);
			}
		}
	 
	 @RequestMapping(value = "/patient/unique" , method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response checkUniquePatient(@RequestBody PatientRegistrationDto patientRegistrationDto){
			try{
				return iPatientRegistrationService.checkUniquePatient(patientRegistrationDto);
			}catch(Exception e){
			logger.error("method failed", e);
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null,null);
			}
		}
	 
	 @RequestMapping(value = "/patient/billingCyclic/{patientId}" , method = RequestMethod.GET)
		public @ResponseBody Response patientDetailsByIdForCyclic(@PathVariable("patientId") int patientId){
			try{
				return iPatientRegistrationService.patientDetailsByIdForCyclic(patientId);
			}catch(Exception e){
			logger.error("method failed", e);
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null,e.getMessage());
			}
		}
	 
	 
	 @RequestMapping(value = "/getAllPatientVisits" , method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getAllPatientVisits(@RequestBody PatientVisitsDto patientVisitsDto){
			try{
				return iPatientRegistrationService.getAllPatientVisits(patientVisitsDto);
			}catch(Exception e){
			logger.error("method failed", e);
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null,e.getMessage());
			}
		}
	 
	 @RequestMapping(value = "/getAllPastVisitsDataForInfo" , method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getAllPastVisitsDataForInfo(@RequestBody PatientVisitsDto patientVisitsDto){
			try{
				return iPatientRegistrationService.getAllPastVisitsDataForInfo(patientVisitsDto);
			}catch(Exception e){
			logger.error("method failed", e);
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null,e.getMessage());
			}
		}
	 
	 
	 @RequestMapping(value = "/patient/deletePatientImage/{patientId}/{organizationId}" , method = RequestMethod.GET)
		public @ResponseBody Response deletePatientImage(@PathVariable("patientId") int patientId,@PathVariable("organizationId") int organizationId){
			try{
				return iPatientRegistrationService.deletePatientImage(patientId,organizationId);
			}catch(Exception e){
			logger.error("method failed", e);
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null,e.getMessage());
			}
		}
	 
	 @RequestMapping(value = "/status" , method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response changeStatus(@RequestBody PatientRegistrationDto patientRegistrationDto){
			try{
				return iPatientRegistrationService.changeStatus(patientRegistrationDto);
			}catch(Exception e){
			logger.error("method failed", e);
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null,e.getMessage());
			}
		}
	 
	 @RequestMapping(value="/patientSearchPatientList",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response patientSearchPatientList(@RequestBody PatientRegistrationDto patientRegistrationDto) {
			try {
				return iPatientRegistrationService.patientSearchPatientList(patientRegistrationDto);
			}catch (Exception e) {
			logger.error("method failed", e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		}
	 
	 
	 @RequestMapping(value="/getPatientInfoForHospitalVisit",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getPatientInfoForHospitalVisit(@RequestBody PatientRegistrationDto patientRegistrationDto) {
			try {
				return iPatientRegistrationService.getPatientInfoForHospitalVisit(patientRegistrationDto);
			}catch (Exception e) {
			logger.error("method failed", e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		}
	 
	 @RequestMapping(value="/customDetails/updateCustomDetails",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response updateCustomDetails(@RequestBody PatientRegistrationDto patientRegistrationDto) {
			try {
				return iPatientRegistrationService.updateCustomDetails(patientRegistrationDto);
			}catch (Exception e) {
			logger.error("method failed", e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		}
	 
     @RequestMapping(value="/getPatientDocumentDetailList",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody Response getPatientDocumentDetailList(@RequestBody PatientRegistrationDto patientRegistrationDto) {
         try {
             return iPatientRegistrationService.getPatientDocumentDetailList(patientRegistrationDto);
         }catch (Exception e) {
         logger.error("method failed", e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
         }
     }
     
     @RequestMapping(value="/sendPatientSmsFromCoversheet",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody Response sendPatientSmsFromCoversheet(@RequestBody PatientRegistrationDto patientRegistrationDto) {
         try {
        	 return iPatientRegistrationService.sendPatientSmsFromCoversheet(patientRegistrationDto);
        		
			
         }catch (Exception e) {
         logger.error("method failed", e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
         }
     }
     
     @RequestMapping(value="/getPatientDetailsListForNewEMR",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
 	public @ResponseBody Response getPatientDetailsListForNewEMR(@RequestBody PatientRegistrationDto patientRegistrationDto) {
 		try {
 			return iPatientRegistrationService.getPatientDetailsListForNewEMR(patientRegistrationDto);
 		}catch (Exception e) {
 		logger.error("method failed", e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
 		}
 	}
	 
     @RequestMapping(value="/savePatientRemarksForNewEMR",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
  	public @ResponseBody Response savePatientRemarksForNewEMR(@RequestBody PatientRegistrationDto patientRegistrationDto) {
  		try {
  			return iPatientRegistrationService.savePatientRemarksForNewEMR(patientRegistrationDto);
  		}catch (Exception e) {
  		logger.error("method failed", e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
  		}
  	}
     
     
     @RequestMapping(value="/checkSqlConnection",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
    	public @ResponseBody Response checkSqlConnection(@RequestBody PatientRegistrationDto patientRegistrationDto) {
    		try {
    			return iPatientRegistrationService.checkSqlConnection(patientRegistrationDto);
    		}catch (Exception e) {
    		logger.error("method failed", e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    		}
     }
     
     @RequestMapping(value="/checkSqlConnectionForUpdate",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
 	public @ResponseBody Response checkSqlConnectionForUpdate(@RequestBody PatientRegistrationDto patientRegistrationDto) {
 		try {
 			return iPatientRegistrationService.checkSqlConnectionForUpdate(patientRegistrationDto);
 		}catch (Exception e) {
 		logger.error("method failed", e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
 		}
  }
  
   /*  @RequestMapping(value="/patientRegisterONP",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
   	public @ResponseBody Response patientRegisterONP(@RequestBody PatientRegistrationDto patientRegistrationDto) {
>>>>>>> Stashed changes
   		try {
   			return iPatientRegistrationService.checkSqlConnection(patientRegistrationDto);
   		}catch (Exception e) {
   			
   			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
   		}
   	}*/
     
     
     @RequestMapping(value="/getPatientDetailsListForNewEMROnlyFavoritesList",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
  	public @ResponseBody Response getPatientDetailsListForNewEMROnlyFavoritesList(@RequestBody PatientRegistrationDto patientRegistrationDto) {
  		try {
  			return iPatientRegistrationService.getPatientDetailsListForNewEMROnlyFavoritesList(patientRegistrationDto);
  		}catch (Exception e) {
  		logger.error("method failed", e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
  		}
  	}
     
     @RequestMapping(value="/getPatientMLCDocumentDetailList",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody Response getPatientMLCDocumentDetailList(@RequestBody PatientRegistrationDto patientRegistrationDto) {
         try {
             return iPatientRegistrationService.getPatientMLCDocumentDetailList(patientRegistrationDto);
         }catch (Exception e) {
         logger.error("method failed", e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
         }
     }     
     
     @RequestMapping(value="/getDatabaseLink",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
     public @ResponseBody Response getDatabaseLink(@RequestBody PatientRegistrationDto patientRegistrationDto) {
         try {
             return iPatientRegistrationService.getDatabaseLink(patientRegistrationDto);
         }catch (Exception e) {
         logger.error("method failed", e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
         }
     }     
     
     
     
     @RequestMapping(value = "/getVideoConsultationLinkFromHIS/{uuid}" , method = RequestMethod.GET)
		public @ResponseBody Response getVideoConsultationLinkForAppFromHIS(@PathVariable("uuid") String uuid){
			try{
				return iPatientRegistrationService.getVideoConsultationLinkForAppFromHIS(uuid);
			}catch(Exception e){
			logger.error("method failed", e);
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null,e.getMessage());
			}
		}
     
     @RequestMapping(value="/updatePatientAsDeath",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
  	public @ResponseBody Response updatePatientAsDeath(@RequestBody PatientRegistrationDto patientRegistrationDto) {
  		try {
  			return iPatientRegistrationService.updatePatientAsDeath(patientRegistrationDto);
  		}catch (Exception e) {
  		logger.error("method failed", e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
  		}
   }

	@RequestMapping(value="/updatePatientRegistrationFromMyLife",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updatePatientRegistrationFromMyLife(@RequestBody PatientMasterDto patientMasterDto) {
		try {
			return iPatientRegistrationService.updatePatientRegistrationFromMyLife(patientMasterDto);
		}catch (Exception e) {
		logger.error("method failed", e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
   
     
     @RequestMapping(value="/patientSearchPatientListForABDM",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response patientSearchPatientListForABDM(@RequestBody PatientRegistrationDto patientRegistrationDto) {
			try {
				return iPatientRegistrationService.patientSearchPatientListForABDM(patientRegistrationDto);
			}catch (Exception e) {
			logger.error("method failed", e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		}
     
     
     @RequestMapping(value="/patientSearchPatientListForABDMForDeepLinking",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response patientSearchPatientListForABDMForDeepLinking(@RequestBody PatientRegistrationDto patientRegistrationDto) {
			try {
				return iPatientRegistrationService.patientSearchPatientListForABDMForDeepLinking(patientRegistrationDto);
			}catch (Exception e) {
			logger.error("method failed", e);
			return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		}


	@RequestMapping(value="/getInitalAssesmentData", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getInitalAssesmentData(@RequestBody SlotMasterDto slotMasterDto) {
		try {
			return iPatientRegistrationService.getInitalAssesmentData(slotMasterDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
     
     
}