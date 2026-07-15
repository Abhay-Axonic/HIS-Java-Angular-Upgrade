package com.param.global.opd.controller;

import com.param.global.common.ServletUtils;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.service.IUnitApplicationBillingConfigurationService;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.billing.dto.AdmissionListIpdChargesResDto;
import com.param.global.common.DocUploadUtility;
import com.param.global.common.ICommonConstants;
import com.param.global.common.ISmsUtilityConstants;
import com.param.global.common.Response;
import com.param.global.dto.TemplateTransactionDto;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.global.opd.service.IEncounterAssignmenMapperService;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.service.ITemplateTransactionService;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.opd.encounter.dto.EncounterAssignmenMapperDto;
import com.param.opd.encounter.dto.EncounterDocDetailsDto;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;
import com.sendgrid.Attachments;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
/*FUNCTIONALITY : OLD ENCOUNTER FUNCTIONALITY
DATE:18/05/2018
CREATED BY: JYOTI*/

@RestController
@RequestMapping("api/opd")
@SuppressWarnings({"rawtypes","unchecked"})
public class EncounterAssignmenMapperController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(EncounterAssignmenMapperController.class);


	@Autowired
	private IEncounterAssignmenMapperService iEncounterAssignmenMapperService;
	
	@Autowired
	private ITemplateTransactionService iTemplateTransactionService;
	
	@Autowired
	private IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	IEncounterMasterService iEncounterMasterService;
	
	@Autowired
	IEncounterMasterDao iEncounterMasterDao;
	
	@Autowired
	private ISmsUtilityConfigService smsUtilityService;

	@Autowired
	private IUnitApplicationBillingConfigurationService unitApplicationBillingConfigurationService;
	
	@Autowired
    DocUploadUtility docUploadUtility;


	@RequestMapping(value="/getOldEncounterList", method=RequestMethod.GET)
	public @ResponseBody Response getOldEncounterList(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId, @RequestParam("patientId") int patientId) {
		try
		{
			EncounterAssignmenMapperDto encounterAssignmenMapperDto= new EncounterAssignmenMapperDto();
			encounterAssignmenMapperDto.setUnitId(unitId);
			encounterAssignmenMapperDto.setOrganizationId(organizationId);
			encounterAssignmenMapperDto.setPatientId(patientId);
			return iEncounterAssignmenMapperService.getOldListEncounterAssignment(encounterAssignmenMapperDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/encounterDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveEncounterAssignment(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody EncounterAssignmenMapperDto mpprDto) throws Exception {
		try
		{
			mpprDto.setUnitId(unitId);
			mpprDto.setOrganizationId(organizationId);
			Response encounterRes = iEncounterAssignmenMapperService.saveEncounterAssignment(mpprDto);
			if(encounterRes.getStatus().equals(SUCCESS))
			{
				if(mpprDto.getIsConsultationStatus()!=null && mpprDto.getIsConsultationStatus()=='Y') {
				
					Response admissionDto = iEncounterMasterService.getAdmissionsById(mpprDto.getEncounterMasterDto().getAdmissionId());
					AdmissionListIpdChargesResDto adminDto = (AdmissionListIpdChargesResDto) admissionDto.getObject();
					
					//Send Template
					TemplateTransactionDto dto = new TemplateTransactionDto();
					dto.setTemplateTypeId(23);//1.FeedBackForm
					dto.setPatientId(mpprDto.getPatientId());
					dto.setEncounterId(mpprDto.getEncounterId());
					dto.setAdmissionId(mpprDto.getEncounterMasterDto().getAdmissionId());
					dto.setDoctorId(mpprDto.getEncounterMasterDto().getDoctorId());
					dto.setDoctorName(adminDto.getDoctorName());
					dto.setAppointmentDate(adminDto.getDoa());
					dto.setOrgId(organizationId);
					dto.setUnitId(unitId);
					iTemplateTransactionService.sendTemplateURLS(dto);
					
					SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
					smsInfo.setPatientId(mpprDto.getPatientId());
					smsInfo.setOrganizationId(mpprDto.getOrganizationId());
					smsInfo.setOrgId(mpprDto.getOrganizationId());
					smsInfo.setUnitId(mpprDto.getUnitId());
					smsInfo.setAdmissionId(mpprDto.getEncounterMasterDto().getAdmissionId());
					if(mpprDto.getIsSendMailOnMailPrescription() != null && mpprDto.getIsSendMailOnMailPrescription() =='Y' ) {
						smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.PRISCRIPTION_IP, smsInfo);
						smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.PRISCRIPTION_IP, smsInfo);
					}
				}
				
				 ByteArrayOutputStream byteOutPutStream = null;
			       CloseableHttpClient httpClient = null;
			       OutputStream output = null;
			    
				   httpClient = HttpClientBuilder.create().build();
				   byteOutPutStream  = new ByteArrayOutputStream();
			       String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(mpprDto.getOrganizationId(),mpprDto.getUnitId());
			       
			       if(API_URL!=null){
			       
			    	   if(mpprDto.getAdmissionId() != null){
			    		   
			    	  
			      String reportParam=" {"
		               +"'appName': 'HIS',"
		               +"'appSecretKey': 'f306b6eccd9d5069d2dc71afe139271a',"
		               +"'reportDesignFilePath': 'NewCoverSheetIPD.rpt',"
		               +"'reportFormat': 'PDF',"
		               +"'exportFileName': 'Coversheet',"
		               +"'reportParams': ["
		                 +"{"
			               +"'paramName': 'Patientid',"
					       +"'paramType': 'Integer',"
					       +"'paramValue':"+mpprDto.getPatientId()
			              +"}," 
			              +"{"
			               +"'paramName': 'Unitid',"
					       +"'paramType': 'Integer',"
					       +"'paramValue':"+mpprDto.getUnitId()// $scope.unitId
			              +"}," 
			              +"{"
			               +"'paramName': 'admission_id',"
					       +"'paramType': 'Integer',"
					       +"'paramValue':"+mpprDto.getAdmissionId()// $scope.encounterDetailsObj.encounterId
			              +"}" 
		               +"]"
		               +"}" ;
			      
			       HttpPost  post = new HttpPost(API_URL);
			       post.addHeader("Content-Type","text/json");
			       post.setEntity(new StringEntity(reportParam));
			       HttpResponse result = httpClient.execute(post);
			       HttpEntity resEntity = result.getEntity(); 
			       resEntity.writeTo(byteOutPutStream);
			       
			       byteOutPutStream.toByteArray();
			       httpClient.close();
			       byteOutPutStream.close();
			       String b64 = Base64.getEncoder().encodeToString(byteOutPutStream.toByteArray());
			       InputStream inputStream = new ByteArrayInputStream(byteOutPutStream.toByteArray());
					String filePath = docUploadUtility.uploadCompleteConsultationOPD(inputStream, mpprDto);
					System.out.println(filePath);
					
			    	  Date date = TimeZoneComponent.getDateByzone(mpprDto.getUnitId());  
						 SimpleDateFormat formatter = new SimpleDateFormat("dd-M-YYYY HH:mm:ss");
					    String currentDate= formatter.format(date);
					    
					EncounterDocDetailsDto encounterDocDetailsDto = new EncounterDocDetailsDto();
			    	  encounterDocDetailsDto.setEncounterId(mpprDto.getEncounterId());
			    	  encounterDocDetailsDto.setPatientId(mpprDto.getPatientId());
			    	  encounterDocDetailsDto.setDocumentTypeId(102);
			    	  encounterDocDetailsDto.setOrganisationId(mpprDto.getOrganizationId());
			    	  encounterDocDetailsDto.setUnitId(mpprDto.getUnitId());
			    	  encounterDocDetailsDto.setExpenseId(0);
			    	  encounterDocDetailsDto.setIncidentReportingMapperId(0);
			    	  encounterDocDetailsDto.setCompanyExpenseId(0);
			    	  encounterDocDetailsDto.setGrnId(0);
			    	  String keyName ="";
			    	  if (mpprDto.getAdmissionId() != null) {
			    		  keyName = mpprDto.getOrganizationId()+"_"+mpprDto.getUnitId()+"_"+mpprDto.getAdmissionId() + "_" + "complete_consultation.pdf";
			    	  } else {
			    		  keyName = mpprDto.getOrganizationId()+"_"+mpprDto.getUnitId() + "_" + "complete_consultation.pdf";
			    	  }
			    	  encounterDocDetailsDto.setLabSampleDtlsId(0);
			    	  encounterDocDetailsDto.setUploadDoccumentDate(currentDate);
			    	  encounterDocDetailsDto.setFileName(keyName);
	                  encounterDocDetailsDto.setRemark(null);
	                  encounterDocDetailsDto.setPoId(null);
	                  encounterDocDetailsDto.setDeathPatientId(0);
	                  encounterDocDetailsDto.setCreatedDate(currentDate);
	                  encounterDocDetailsDto.setOpbId(0);
	                  encounterDocDetailsDto.setVendorId(0);
	                  encounterDocDetailsDto.setDocumentPath(filePath);
		             iEncounterMasterDao.saveDocumentAgainstEncounter(encounterDocDetailsDto);
			       }
			       }
				
			}
			return encounterRes;
		}catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		//return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateEncounterDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateEncounterAssignment(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody EncounterAssignmenMapperDto mpprDto, HttpServletRequest request) {
		try
		{
			mpprDto.setUnitId(unitId);
			mpprDto.setOrganizationId(organizationId);
			Response encounterRes = iEncounterAssignmenMapperService.updateEncounterAssignment(mpprDto);
			if(encounterRes.getStatus().equals(SUCCESS))
			{
				if(mpprDto.getIsConsultationStatus()!=null && mpprDto.getIsConsultationStatus()=='Y') {
					if(mpprDto.getIsEHC()!=null && mpprDto.getIsEHC() == 'N') {
						Response encRes = iEncounterMasterService.getEncounterDetailsById(mpprDto.getEncounterId());
						EncounterMasterDto encounterMasterDto = (EncounterMasterDto) encRes.getObject();
						//Send Template
						TemplateTransactionDto dto = new TemplateTransactionDto();
						dto.setTemplateTypeId(23);//1.FeedBackForm
						dto.setPatientId(mpprDto.getPatientId());
						dto.setEncounterId(mpprDto.getEncounterId());
						dto.setAdmissionId(mpprDto.getAdmissionId());
						dto.setDoctorId(mpprDto.getDoctorId());
						dto.setDoctorName(encounterMasterDto.getDoctorName() != null ? encounterMasterDto.getDoctorName():null);
						dto.setAppointmentDate(encounterMasterDto.getEncounterDateString2());
						dto.setOrgId(organizationId);
						dto.setUnitId(unitId);
						iTemplateTransactionService.sendTemplateURLS(dto);	
					}

					String resPublicUrl =  unitApplicationBillingConfigurationService.getPublicUrl(unitId);
					SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
					smsInfo.setPatientId(mpprDto.getPatientId());
					smsInfo.setOrganizationId(mpprDto.getOrganizationId());
					smsInfo.setOrgId(mpprDto.getOrganizationId());
					smsInfo.setUnitId(mpprDto.getUnitId());
					smsInfo.setEncounterId(mpprDto.getEncounterId());

					String criticalUrl= "";
					if(resPublicUrl == null) {
						criticalUrl= ServletUtils.getBaseUrl(request)+"/#/api/outPatientFeedback/"+unitId+"/"+mpprDto.getOrganizationId()+"/"
								+mpprDto.getPatientId()+"/"+mpprDto.getVisitType()+"/"+mpprDto.getVisitTypeId()+"/"+mpprDto.getEncounterId();
					}else {
						criticalUrl= resPublicUrl+"/#/api/outPatientFeedback/"+unitId+"/"+mpprDto.getOrganizationId()+"/"
								+mpprDto.getPatientId()+"/"+mpprDto.getVisitType()+"/"+mpprDto.getVisitTypeId()+"/"+mpprDto.getEncounterId();
					}
					smsInfo.setCriticalUrl(criticalUrl);

					smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.COMPLETEDCOVERSHEET, smsInfo);
					smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.COMPLETEDCOVERSHEET, smsInfo);
					if( mpprDto.getIsSendMailOnMailPrescription() != null && mpprDto.getIsSendMailOnMailPrescription() =='Y') {
						smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.PRISCRIPTION, smsInfo);
						smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.PRISCRIPTION, smsInfo);
					}
					
					//upload consulltation pdf on s3 bucket ..

						   ByteArrayOutputStream byteOutPutStream = null;
					       CloseableHttpClient httpClient = null;
					       OutputStream output = null;
					    
						   httpClient = HttpClientBuilder.create().build();
						   byteOutPutStream  = new ByteArrayOutputStream();
					       String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(mpprDto.getOrganizationId(),mpprDto.getUnitId());
					       
					       if(API_URL!=null){
					       
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
							       +"'paramValue':"+mpprDto.getPatientId()
					              +"}," 
					              +"{"
					               +"'paramName': 'Unitid',"
							       +"'paramType': 'Integer',"
							       +"'paramValue':"+mpprDto.getUnitId()// $scope.unitId
					              +"}," 
					              +"{"
					               +"'paramName': 'Encounter_id',"
							       +"'paramType': 'Integer',"
							       +"'paramValue':"+mpprDto.getEncounterId()// $scope.encounterDetailsObj.encounterId
					              +"}" 
				               +"]"
				               +"}" ;
					      
					       HttpPost  post = new HttpPost(API_URL);
					       post.addHeader("Content-Type","text/json");
					       post.setEntity(new StringEntity(reportParam));
					       HttpResponse result = httpClient.execute(post);
					       HttpEntity resEntity = result.getEntity(); 
					       resEntity.writeTo(byteOutPutStream);
					       
					       byteOutPutStream.toByteArray();
					       httpClient.close();
					       byteOutPutStream.close();
					       String b64 = Base64.getEncoder().encodeToString(byteOutPutStream.toByteArray());
					       InputStream inputStream = new ByteArrayInputStream(byteOutPutStream.toByteArray());
							String filePath = docUploadUtility.uploadCompleteConsultationOPD(inputStream, mpprDto);
							System.out.println(filePath);
							
					    	  Date date = TimeZoneComponent.getDateByzone(mpprDto.getUnitId());  
								 SimpleDateFormat formatter = new SimpleDateFormat("dd-M-YYYY HH:mm:ss");
							    String currentDate= formatter.format(date);
							    
							EncounterDocDetailsDto encounterDocDetailsDto = new EncounterDocDetailsDto();
					    	  encounterDocDetailsDto.setEncounterId(mpprDto.getEncounterId());
					    	  encounterDocDetailsDto.setPatientId(mpprDto.getPatientId());
					    	  encounterDocDetailsDto.setDocumentTypeId(102);
					    	  encounterDocDetailsDto.setOrganisationId(mpprDto.getOrganizationId());
					    	  encounterDocDetailsDto.setUnitId(mpprDto.getUnitId());
					    	  encounterDocDetailsDto.setExpenseId(0);
					    	  encounterDocDetailsDto.setIncidentReportingMapperId(0);
					    	  encounterDocDetailsDto.setCompanyExpenseId(0);
					    	  encounterDocDetailsDto.setGrnId(0);
					    	  String keyName = "";
					    	  if (mpprDto.getEncounterId() != null) {
					    		  keyName = mpprDto.getOrganizationId()+"_"+mpprDto.getUnitId()+"_"+mpprDto.getEncounterId() + "_" + "complete_consultation.pdf";
					    	  } else {
					    		  keyName = mpprDto.getOrganizationId()+"_"+mpprDto.getUnitId() + "_" + "complete_consultation.pdf";
					    	  }
					    	  encounterDocDetailsDto.setLabSampleDtlsId(0);
					    	  encounterDocDetailsDto.setUploadDoccumentDate(currentDate);
					    	  encounterDocDetailsDto.setFileName(keyName);
			                  encounterDocDetailsDto.setRemark(null);
			                  encounterDocDetailsDto.setPoId(null);
			                  encounterDocDetailsDto.setDeathPatientId(0);
			                  encounterDocDetailsDto.setCreatedDate(currentDate);
			                  encounterDocDetailsDto.setOpbId(0);
			                  encounterDocDetailsDto.setVendorId(0);
			                  encounterDocDetailsDto.setDocumentPath(filePath);
				          iEncounterMasterDao.saveDocumentAgainstEncounter(encounterDocDetailsDto);
					       }
						
				}
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/markAsCritical", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response markAsCritical(@RequestBody EncounterMasterDto encounterMasterDto) {
		try
		{
			return iEncounterAssignmenMapperService.updatePatientCritical(encounterMasterDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="/updateDoctorIdForEncouner", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateDoctorIdForEncouner(@RequestBody EncounterMasterDto encounterMasterDto) {
		try
		{
			return iEncounterAssignmenMapperService.updateDoctorIdForEncouner(encounterMasterDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getCabinListByDoctorId/{doctorId}/{unitId}", method=RequestMethod.GET)
	public @ResponseBody Response getCabinListByDoctorId(@PathVariable("doctorId") Integer doctorId,@PathVariable("unitId") Integer unitId) {
		try
		{
			return iEncounterAssignmenMapperService.getCabinListByDoctorId(doctorId,unitId);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getCabinListByRMOMappedDoctorList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCabinListByRMOMappedDoctorList(@RequestBody EncounterMasterDto encounterMasterDto) {
		try
		{
			return iEncounterAssignmenMapperService.getCabinListByRMOMappedDoctorList(encounterMasterDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateEncounterDetailsForNewEMR", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateEncounterDetailsForNewEMR(@RequestParam(value="unit_id") int unitId,
			@RequestParam(value="organization_id") int organizationId,@RequestBody EncounterAssignmenMapperDto mpprDto) {
		try
		{
			mpprDto.setUnitId(unitId);
			mpprDto.setOrganizationId(organizationId);
			Response encounterRes = iEncounterAssignmenMapperService.updateEncounterAssignment(mpprDto);
			if(encounterRes.getStatus().equals(SUCCESS))
			{
				if(mpprDto.getIsConsultationStatus()!=null && mpprDto.getIsConsultationStatus()=='Y') {
					if(mpprDto.getIsEHC()!=null && mpprDto.getIsEHC() == 'N') {
						Response encRes = iEncounterMasterService.getEncounterDetailsById(mpprDto.getEncounterId());
						EncounterMasterDto encounterMasterDto = (EncounterMasterDto) encRes.getObject();
						//Send Template
						TemplateTransactionDto dto = new TemplateTransactionDto();
						dto.setTemplateTypeId(23);//1.FeedBackForm
						dto.setPatientId(mpprDto.getPatientId());
						dto.setEncounterId(mpprDto.getEncounterId());
						dto.setAdmissionId(mpprDto.getAdmissionId());
						dto.setDoctorId(mpprDto.getDoctorId());
						dto.setDoctorName(encounterMasterDto.getDoctorName() != null ? encounterMasterDto.getDoctorName():null);
						dto.setAppointmentDate(encounterMasterDto.getEncounterDateString2());
						dto.setOrgId(organizationId);
						dto.setUnitId(unitId);
						iTemplateTransactionService.sendTemplateURLS(dto);	
					}
					
					SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
					smsInfo.setPatientId(mpprDto.getPatientId());
					smsInfo.setOrganizationId(mpprDto.getOrganizationId());
					smsInfo.setOrgId(mpprDto.getOrganizationId());
					smsInfo.setUnitId(mpprDto.getUnitId());
					smsInfo.setEncounterId(mpprDto.getEncounterId());
					smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.COMPLETEDCOVERSHEET, smsInfo);
					if( mpprDto.getIsSendMailOnMailPrescription() != null && mpprDto.getIsSendMailOnMailPrescription() =='Y') {
						smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.PRISCRIPTION, smsInfo);
						smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.PRISCRIPTION, smsInfo);
					}
				}
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@RequestMapping(value="/updateEncounterDetailsMultilingual", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateEncounterDetailsMultilingual(@RequestHeader(value="unit_id") int unitId,
			@RequestHeader(value="organization_id") int organizationId,@RequestBody EncounterAssignmenMapperDto mpprDto) {
		try
		{
			mpprDto.setUnitId(unitId);
			mpprDto.setOrganizationId(organizationId);
			Response encounterRes = iEncounterAssignmenMapperService.updateEncounterDetailsMultilingual(mpprDto);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



	@RequestMapping(value="/updateEncounterStopTime", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateEncounterStopTime(@RequestBody EncounterAssignmenMapperDto mpprDto) throws Exception {
		try
		{

						Date date = TimeZoneComponent.getDateByzone(mpprDto.getUnitId());
//						SimpleDateFormat formatter = new SimpleDateFormat("dd-M-YYYY HH:mm:ss");
//						String currentDate= formatter.format(date);
//						mpprDto.setAssignmentStopTime(currentDate);
                       iEncounterMasterService.updateEncounterStopTime(mpprDto);

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch(Exception e)
		{
			throw e;
		}
		//return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
