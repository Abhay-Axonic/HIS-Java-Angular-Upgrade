package com.param.global.billing.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.multipart.MultipartFile;

import com.param.global.billing.dto.DepositCollectionReqDto;
import com.param.global.billing.dto.DepositMasterDto;
import com.param.global.billing.dto.DepositSearchReqDto;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.billing.dto.VisitTypeEncounterReqDto;
import com.param.global.billing.service.IDepositMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.ISmsUtilityConstants;
import com.param.global.common.Response;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;

@RestController
@RequestMapping("/api/billing/unit")
@SuppressWarnings("rawtypes")
public class DepositController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(DepositController.class);

	@Autowired
	private IDepositMasterService iDepositMasterService;
	
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	private ISmsUtilityConfigService smsUtilityService;
	
	/**
	 * TO ADD DEPOSITE AGAINST PATIENT OR ADMISSION
	 * @param reqDto
	 * @return
	 */
	
	@RequestMapping(value = "/deposit" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response  addDeposit(@RequestBody DepositCollectionReqDto reqDto){
		try{
			
			if(reqDto.getListPaymentDepositDetailsDto() != null) {
				for (PaymentDepositDetailsDto dto : reqDto.getListPaymentDepositDetailsDto()) {
					
					if(dto.getPaymentModeId() == 12 && dto.getPaymentGatewayProviderId() == 2 ) {
						Response resCcavenue = iDepositMasterService.ccAvenueCheckvalidation(dto.getTransactionNo());
						if(resCcavenue.getObject() == null) {
							return new Response<>(ERROR, ERROR_CODE, dto.getTransactionNo()+" This already used in other trancation", null, null);
						}
					}
				}
					
			}
			
			
			Response res =   iDepositMasterService.addDeposit(reqDto);
			
			Date currentDate = TimeZoneComponent.getDateByzone(reqDto.getDepositMasterDto().getUnitId());
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String strCurrentDate = formatter.format(currentDate);
			
			
			if(res.getResObject()!=null) {
				DepositMasterDto deposit = (DepositMasterDto) res.getResObject();
//				{
				
					
					
					Integer depositId = (Integer) res.getObject();
					SmsUtilityConfigInfoDto smsInfo=new SmsUtilityConfigInfoDto();
						smsInfo.setUnitId(reqDto.getDepositMasterDto().getUnitId());
						smsInfo.setPatientId(reqDto.getDepositMasterDto().getPatientId());
						smsInfo.setTreatingDoctorId((Integer)res.getObject());
						smsInfo.setBillAmount(new BigDecimal(deposit.getDepositAmount()));
						smsInfo.setDepositNo(deposit.getDepositNo());
						smsInfo.setDateTime(strCurrentDate);
						smsInfo.setDepositId(depositId);
						smsInfo.setOrganizationId(reqDto.getDepositMasterDto().getOrgId());
				    smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.ADVANCE,smsInfo);
					smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.ADVANCE,smsInfo);
				
			}
			return res;
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	
/*	@RequestMapping(value = "/deposit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSample(@RequestBody String sampleMasterDto) {
		
		
		Response<DepositCollectionReqDto, Integer> response = null;
		try {
			
			JsonCheck.mapJsonToObject(sampleMasterDto, DepositCollectionReqDto.class);
			
			//  response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
			 // response;
			 
			return null;
		} catch (Exception e) {
			
			return new Response(ERROR, ERROR_CODE, null, null, null);
		}
	}
*/
	@RequestMapping(value = "/recharge/card" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response rechargeLoyaltyCard(@RequestBody DepositCollectionReqDto reqDto){
		try{
			return iDepositMasterService.rechargeLoyaltyCard(reqDto);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	/**
	 * METHOS TO SEARCH DEPOSITE DETIALS BY GIVEN FILTER
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/deposit/search" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response addDeposit(@RequestBody DepositSearchReqDto reqDto){
		try{
			return iDepositMasterService.searchDeposit(reqDto);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	/**
	 * METHOD TO GET DEPOSITE AGAINST ENCOUNTER
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/deposit/encounters" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getEncountersForDeposit(@RequestBody VisitTypeEncounterReqDto reqDto){
		try{
			return iDepositMasterService.getEncountersForDeposit(reqDto);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	/**
	 * METHOD TO GET AVAILABLE DEPOSITE AGAINST PATIENT & ENCOUNTER ID
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/deposit/patient/{orgId}/{unitId}/{patientId}/{encounterId}/{visitTypeId}/{otReservFlag}/{depositType}" , method = RequestMethod.GET)
	public Response getAvailableDepositByPatient(@PathVariable Map<String,String> params){
		try{
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			int patientId = Integer.parseInt(params.get("patientId").trim());
			int encounterId = Integer.parseInt(params.get("encounterId").trim());
			int visitTypeId = Integer.parseInt(params.get("visitTypeId").trim());
			int otReservFlag = Integer.parseInt(params.get("otReservFlag").trim());
			Boolean  depositType = Boolean.parseBoolean(params.get("depositType").trim());
			return iDepositMasterService.getAvailableDepositByPatient(orgId, unitId, patientId,encounterId,visitTypeId,otReservFlag,depositType);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	/**
	 * METHOD TO GET AVAILABLE DEPOSITE AGAINST PATIENT
	 * @param patientId
	 * @return
	 */
	@RequestMapping(value = "/deposit/patient/{unitId}/{patientId}" , method = RequestMethod.GET)
	public Response getAvailableDepositByPatient(@PathVariable Integer unitId, @PathVariable Integer patientId){
		try{
			return iDepositMasterService.getAvailableDepositByPatient(patientId,unitId);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	/**
	 * METHOD TO GET DEPOSITE LIST FOR REFUND
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/deposit/refund/{orgId}/{unitId}/{patientId}/{depositTypeId}" , method = RequestMethod.GET)
	public Response getDepositListForRefund(@PathVariable Map<String,String> params){
		try{
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			int patientId = Integer.parseInt(params.get("patientId").trim());
			int depositTypeId = Integer.parseInt(params.get("depositTypeId").trim());
			return iDepositMasterService.getDepositListForRefund(orgId, unitId, patientId,depositTypeId);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	/**
	 * METHOD TO GET DEPOSIT DETAILS BY DEPOSITE ID
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/deposit/details/{orgId}/{unitId}/{depositMasterId}" , method = RequestMethod.GET)
	public Response getDepositDetailsByDepositMaster(@PathVariable Map<String,String> params){
		try{
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			int depositMasterId = Integer.parseInt(params.get("depositMasterId").trim());
			return iDepositMasterService.getDepositDetailsByDepositMaster(orgId, unitId, depositMasterId);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@RequestMapping(value = "/deposit/consumption/{depositId}" , method = RequestMethod.GET)
	public Response getDepositConsumtionDetails(@PathVariable Map<String,String> params){
		try{
			int depositId = Integer.parseInt(params.get("depositId").trim());
			return iDepositMasterService.getDepositConsumtionDetails(depositId);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	  @RequestMapping(value = "/bill/Report" , method = RequestMethod.POST)
	  public  Response printReport(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException 
	 {
		   ByteArrayOutputStream byteOutPutStream = null;
	       CloseableHttpClient httpClient = null;
	       OutputStream output = null;
	   try{
	    
		   //System.out.println("===============report Start===================");
		   httpClient = HttpClientBuilder.create().build();
		   byteOutPutStream  = new ByteArrayOutputStream();
	       servletResponse.setContentType("application/pdf");
	       String reportParam = servletRequest.getParameter("reportParam");
	       String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(Integer.parseInt(servletRequest.getParameter("orgId")),Integer.parseInt(servletRequest.getParameter("unitId")));
	       
	       if(API_URL==null)
	    	   return null;
	       
	       HttpPost  post = new HttpPost(API_URL);
	       post.addHeader("Content-Type","application/json");
	       post.setEntity(new StringEntity(reportParam));
	    
	       HttpResponse result = httpClient.execute(post);
	       HttpEntity resEntity = result.getEntity(); 
	       resEntity.writeTo(byteOutPutStream);
	       
	       output = servletResponse.getOutputStream();
	       output.write(byteOutPutStream.toByteArray());
	       output.close();
	       httpClient.close();
	       byteOutPutStream.close();
	       
	       //System.out.println("===============report end===================");
	       return null;
	   }catch(Exception e)
	   {
		   logger.error("printReport failed", e);
		   throw new BillingException(e.getMessage());
	   }
	   finally {
		   //System.out.println("===============finally report Start===================");
		   if(output!=null)
			   output.close();
	       if(httpClient!=null)
	    	   httpClient.close();
	       if(byteOutPutStream!=null)
	    	   byteOutPutStream.close();
	       //System.out.println("===============finally report end===================");
	    }
	  }


	  @RequestMapping(value = "/bill/Report/MIS" , method = RequestMethod.POST)
	  public  Response printReportMIS(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException 
	 {
		   ByteArrayOutputStream byteOutPutStream = null;
	       CloseableHttpClient httpClient = null;
	       OutputStream output = null;
	   try{
	    
		   //System.out.println("===============report Start===================");
		   httpClient = HttpClientBuilder.create().build();
		   byteOutPutStream  = new ByteArrayOutputStream();
	       servletResponse.setContentType("application/pdf");
	       String reportParam = servletRequest.getParameter("reportParam");
	       String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGenerationMis(Integer.parseInt(servletRequest.getParameter("orgId")),Integer.parseInt(servletRequest.getParameter("unitId")));
	       
	       if(API_URL==null)
	    	   return null;
	       
	       HttpPost  post = new HttpPost(API_URL);
	       post.addHeader("Content-Type","application/json");
	       post.setEntity(new StringEntity(reportParam));
	    
	       HttpResponse result = httpClient.execute(post);
	       HttpEntity resEntity = result.getEntity(); 
	       resEntity.writeTo(byteOutPutStream);
	       
	       output = servletResponse.getOutputStream();
	       output.write(byteOutPutStream.toByteArray());
	       output.close();
	       httpClient.close();
	       byteOutPutStream.close();
	       
	       //System.out.println("===============report end===================");
	       return null;
	   }catch(Exception e)
	   {
		   logger.error("printReportMIS failed", e);
		   throw new BillingException(e.getMessage());
	   }
	   finally {
		   //System.out.println("===============finally report Start===================");
		   if(output!=null)
			   output.close();
	       if(httpClient!=null)
	    	   httpClient.close();
	       if(byteOutPutStream!=null)
	    	   byteOutPutStream.close();
	       //System.out.println("===============finally report end===================");
	    }
	  }
	  
	  
	  
	  @RequestMapping(value = "/bill/Report/file/{orgId}/{unitId}" , method = RequestMethod.POST)
	  public synchronized void donwloadFile(@RequestBody String reqDto,@PathVariable("orgId") int orgId,@PathVariable("unitId") int unitId,
			  HttpServletRequest request,
              HttpServletResponse response) throws IOException 
	  {
		  ByteArrayOutputStream byteOutPutStream = null;
	       CloseableHttpClient httpClient = null;
	       ServletOutputStream  output = null;
	   try{
		  
		   //System.out.println("===============excel report Start===================");
		   httpClient = HttpClientBuilder.create().build();
		   byteOutPutStream  = new ByteArrayOutputStream();
		   
		   response.setContentType("application/octet-stream");
		   //response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		  
	       String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(orgId,unitId);
	       HttpPost  post = new HttpPost(API_URL);
	       post.addHeader("Content-Type","application/json");
	       post.setEntity(new StringEntity(reqDto));
	       HttpResponse result = httpClient.execute(post);
	       HttpEntity resEntity = result.getEntity(); 
	       resEntity.writeTo(byteOutPutStream);
	       response.addHeader("Content-Disposition", "attachment; filename=admission.xlsx");
	       response.setContentLength(byteOutPutStream.size());
	       output = response.getOutputStream();
	       byteOutPutStream.writeTo(output);
	       output.close();
	       httpClient.close();
	       byteOutPutStream.close();
	       
	       
	       
	       //System.out.println("===============excel report end===================");
	   }catch(Exception e)
	   {
		   logger.error("donwloadFile failed", e);
		   throw new BillingException(e.getMessage());
	   }
	   finally {
		   //System.out.println("===============finally report Start===================");
	       httpClient.close();
	       output.close();
	       byteOutPutStream.close();
	       //System.out.println("===============finally report end===================");
	    }
	  }
	  
	  
	  
	  
	  
	  
		@RequestMapping(value = "/getMysqlData" , method = RequestMethod.GET)
		public Response getMySqlData(){
			try{
				return iDepositMasterService.getMySqlData();
			}catch(Exception e){
				
				throw new BillingException(e.getMessage());
			}
		}
		
		
	@RequestMapping(value = "/signature/image/{orgId}/{unitId}/{depositId}", headers = ("content-type=multipart/*"), method = RequestMethod.POST)
	public @ResponseBody Response updateSignatureImage(@PathVariable("orgId") int orgId, @PathVariable("unitId") int unitId,
			@PathVariable("depositId") int depositId, @RequestParam("file") MultipartFile inputFile) {
		try {
			return iDepositMasterService.updateSignatureImage(orgId, unitId, depositId, inputFile);
		} catch (Exception e) {
			logger.error("updateSignatureImage failed", e);
			throw new BillingException(e.getMessage());
		}
	}
	

	@RequestMapping(value = "/getPrintDateWiseCoversheet", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response printPreviousCoversheet(@RequestBody EncounterMasterDto encounterMasterDto) throws IOException {
		ByteArrayOutputStream byteOutPutStream = null;
		CloseableHttpClient httpClient = null;
		OutputStream output = null;
		try {

			httpClient = HttpClientBuilder.create().build();
			byteOutPutStream = new ByteArrayOutputStream();
			String API_URL = iUnitApplicationBillingConfigurationDao
					.getURLForPdfGeneration(encounterMasterDto.getOrganizationId(), encounterMasterDto.getUnitId());

			if (API_URL == null)
				return null;

			String reportParam=" {"
					+"'appName': 'HIS',"
					+"'appSecretKey': 'f306b6eccd9d5069d2dc71afe139271a',"
					+"'reportDesignFilePath': 'NewCoverSheet_datewise.rpt',"
					+"'reportFormat': 'PDF',"
					+"'exportFileName': 'Coversheet',"
					+"'reportParams': ["
					+"{"
					+"'paramName': 'TransDate',"
					+"'paramType': 'string',"
					// +"'paramValue':"+"'"+encounterMasterDto.getEncounterDateString().toString()+"'"// $scope.encounterDateString
					+"'paramValue':'"+encounterMasterDto.getEncounterDateString()+"'"
					+"},"
					+"{"
					+"'paramName': 'Encounter_id',"
					+"'paramType': 'Integer',"
					+"'paramValue':"+encounterMasterDto.getEncounterId()// $scope.encounterDetailsObj.encounterId
					+"}"
					+"]"
					+"}" ;

			HttpPost post = new HttpPost(API_URL);
			post.addHeader("Content-Type", "application/json");
			post.setEntity(new StringEntity(reportParam));

			HttpResponse result = httpClient.execute(post);
			HttpEntity resEntity = result.getEntity();
			resEntity.writeTo(byteOutPutStream);

			byteOutPutStream.toByteArray();
			httpClient.close();
			byteOutPutStream.close();
			return new Response<>(SUCCESS, null, null, null, byteOutPutStream.toByteArray());
		} catch (Exception e) {
			logger.error("printPreviousCoversheet failed", e);
			throw new BillingException(e.getMessage());
		} finally {
			if (output != null)
				output.close();
			if (httpClient != null)
				httpClient.close();
			if (byteOutPutStream != null)
				byteOutPutStream.close();
		}
	}
	@RequestMapping(value = "/GRN/details/{orgId}/{unitId}/{GRNId}" , method = RequestMethod.GET)
	public Response getGRNDetailsByGRNMaster(@PathVariable Map<String,String> params){
		try{
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			int GRNId = Integer.parseInt(params.get("GRNId").trim());
			return iDepositMasterService.getGRNDetailsByGRNMaster(orgId, unitId, GRNId);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@RequestMapping(value = "/cancelDepositReceipt" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response cancelDepositReceipt(@RequestBody DepositMasterDto reqDto){
		try{
			return iDepositMasterService.cancelDepositReceipt(reqDto);
		}catch(Exception e){

			throw new BillingException(e.getMessage());
		}
	}

	@RequestMapping(value = "/validateDepositAccess/{userId}/{unitId}/{organizationId}",method = RequestMethod.GET)
	public @ResponseBody Response validateMrdUser(@PathVariable Integer userId,
												  @PathVariable Integer unitId,
												  @PathVariable Integer organizationId){
		try {
			return iDepositMasterService.validateDepositAccess(userId,unitId,organizationId);
		} catch (Exception e) {
			logger.error("validateMrdUser failed", e);
			throw new BillingException(e.getMessage());
		}
	}
}