package com.param.global.unit.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.http.HttpResponse;
import com.google.gson.Gson;
import com.param.global.common.DocUploadUtility;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.MyLifeConnectionLinkDto;
import com.param.global.dto.TemplateReqDto;
import com.param.global.dto.TemplateResponseDto;
import com.param.global.dto.TemplateTransactionDto;
import com.param.global.dto.sync.RadiologySyncDto;
import com.param.global.dto.sync.ServiceappoServiceMpprSyncDto;
import com.param.global.opd.service.IAppointmentMasterService;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.service.ITemplateMasterService;
import com.param.global.service.ITemplateTransactionService;
import com.param.workbench.service.IRISWorkbenchService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Controller
@RequestMapping("/global")
@SuppressWarnings({"rawtypes","unchecked"})
public class TemplateTrasactionController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(TemplateTrasactionController.class);


	@Autowired
	ITemplateTransactionService iTemplateTransactionService;

	
	@Autowired
	ITemplateMasterService iTemplateMasterService;
	
	@Autowired
	IRISWorkbenchService iRISWorkbenchService;
	
	@Autowired
	DocUploadUtility docUploadUtility;
	
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	IAppointmentMasterService iAppointmentMasterService;
	 
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	private Gson gson;
	
	@RequestMapping(value="/template/transaction" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody synchronized Response saveTemplateTransaction(@RequestBody TemplateTransactionDto templateTransactionDto){
		try{
			
			return iTemplateTransactionService.saveTemplateTransaction(templateTransactionDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	@RequestMapping(value="/template/transaction" , method = RequestMethod.PUT , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateTemplateTransaction(@RequestBody TemplateTransactionDto dto){
		try{
			Response res= iTemplateTransactionService.updateTemplateTransaction(dto);
			if(res.getObject() == null ){
				return res;
			}
			dto.setTemplateTransactionId((Integer)res.getObject());
			 Response isSyncRes=iRISWorkbenchService.getMyLifeDetail(dto.getOrgId());
			 OrganizationMasterDto dtoOrg = (OrganizationMasterDto) isSyncRes.getObject();
			if (isSyncRes.getObject() != null && dtoOrg.getMyLifeLink() != null && dtoOrg.getIsSyncScheduling() == true 
					&& (dto.getIsFinalRelease() != null && dto.getIsFinalRelease() == 'Y')) {
						int lowerLimit = 97;
						int upperLimit = 122;
						Random random = new Random();
						StringBuffer r = new StringBuffer(10);
						for (int i = 0; i < 10; i++) {
							int nextRandomChar = lowerLimit
									+ (int) (random.nextFloat() * (upperLimit - lowerLimit + 1));
							r.append((char) nextRandomChar);
						}

						String str = "";
						ByteArrayOutputStream byteOutPutStreamHtml = null;
						CloseableHttpClient httpClientHtml = null;
						InputStream htmlInputStream = null;
						byteOutPutStreamHtml = new ByteArrayOutputStream();
						httpClientHtml = HttpClientBuilder.create().build();
						String rptString = "{  " + " \"appName\": \"HIS\",  "
								+ " \"appSecretKey\": \"f306b6eccd9d5069d2dc71afe139271a\",  "
								+ "	\"reportDesignFilePath\": \"RadiologyReport.rpt\",  "
								+ " \"reportFormat\": \"PDF\",  "
								+ "	\"exportFileName\": \"RadiologyReport\",  "
								+ " \"reportParams\": [  "
								+ " {  "
								+ " \"paramName\": \"template_transaction_id\",  "
								+ " \"paramType\": \"Integer\",  "
								+ " \"paramValue\": " + dto.getTemplateTransactionId()
								+ " },{  "
								+ " \"paramName\": \"isOriginal\",  "
								+ " \"paramType\": \"Integer\",  "
								+ " \"paramValue\": 1  "
								+ " },{  "
								+ " \"paramName\": \"isHeader\",  "
								+ " \"paramType\": \"Integer\",  "
								+ " \"paramValue\": 0  "
								+ " }  " + "				        		]  "
								+ " }";
						String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(dto.getOrgId(),
								dto.getUnitId());
						ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();
						HttpPost pst = new HttpPost(API_URL);
						pst.addHeader("Content-Type", "application/json");
						pst.setEntity(new StringEntity(rptString));
						CloseableHttpClient httpClient = HttpClientBuilder.create().build();
						CloseableHttpResponse response = httpClient.execute(pst);
						HttpEntity resEntity = response.getEntity();
						resEntity.writeTo(byteOutPutStream);

						com.param.global.common.Response orgRes = iRISWorkbenchService
								.getOrganiazationFolderPath(dto.getOrgId());
						OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();
						String docPath = "";

						docPath = docUploadUtility.updateRISPdfFile(byteOutPutStream,
								organizationMasterDto.getOrganizationFolder(), r.toString(), dto.getOrgId(), dto.getUnitId());
						Response resNew = iRISWorkbenchService.updateRISPdfPathByTemplateTransactionId(docPath, dto);
						
						if(organizationMasterDto !=null && organizationMasterDto.getIsSyncScheduling() == true){
						String MY_LIFE_URI = null;
						URI url = null;
						Response responseNew = null;
						Response res1 = iRISWorkbenchService.getConnectionStringForMyLife();
						if (res1.getObject() != null) {
							MyLifeConnectionLinkDto connectionLinkDto = (MyLifeConnectionLinkDto) res1.getObject();
							if (connectionLinkDto != null && connectionLinkDto.getMyLifeLink() != null
									&& connectionLinkDto.getMyLifeLink() != "")
								MY_LIFE_URI = connectionLinkDto.getMyLifeLink();
							 //MY_LIFE_URI = "http://10.10.0.195:9091/MyLifeGlobal/api";
							 System.out.println("In the APIS.....2222222");
							if (MY_LIFE_URI != null) {

								responseNew = iAppointmentMasterService
										.syncPatientRadiologyReport(dto.getTemplateTransactionId());
								if (responseNew.getCode().equals(ICommonConstants.SUCCESS_CODE)
										&& responseNew.getObject() != null) {
									RadiologySyncDto serviceappoServiceMpprSyncDto = (RadiologySyncDto) responseNew
											.getObject();
									if (serviceappoServiceMpprSyncDto != null) {

										url = URI.create(MY_LIFE_URI + "/sync/opd/appointment/radiologyreport");
										String ss = new Gson().toJson(serviceappoServiceMpprSyncDto);
										String stringResponse = restTemplate.postForObject(url,
												serviceappoServiceMpprSyncDto, String.class);
										return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null,
												stringResponse);
									}
								} else {
									return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null,dto.getTemplateTransactionId());
								}
//         				}

							}
						}
						}else {
							return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null,dto.getTemplateTransactionId());
						}
			}
			else {
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, dto.getTemplateTransactionId());
			}		
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	
	@RequestMapping(value="/petient/template" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getTemplateByPatient(@RequestBody TemplateReqDto templateReqDto){
		try{
			
			return iTemplateTransactionService.getTemplateByPatient(templateReqDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/petient/template/otims" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getTemplateByPatientInOtims(@RequestBody TemplateReqDto templateReqDto){
		try{
			
			return iTemplateTransactionService.getTemplateByPatientInOtims(templateReqDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
/*	
	@RequestMapping(value="/petient/template" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getTemplateByPatient(@RequestBody TemplateReqDto templateReqDto){
		try{
			
			return iTemplateTransactionService.getTemplateByPatient(templateReqDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}*/
	
	@RequestMapping(value="/petient/quesionnaaire" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getTemplateQuestionaaire(@RequestBody TemplateReqDto templateReqDto){
		try{
			
			return iTemplateTransactionService.getTemplateQuestionaaire(templateReqDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/petient/quesionnaaire/radiology" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getTemplateQuestionaaireRadiology(@RequestBody TemplateReqDto templateReqDto){
		try{
			
			return iTemplateTransactionService.getTemplateQuestionaaireRadiology(templateReqDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/petient/quesionnaaire/details" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getQuestionaaiireDetails(@RequestBody TemplateReqDto templateReqDto){
		try{
			
			return iTemplateTransactionService.getQuestionaaiireDetails(templateReqDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value = "/templates/admissionConcent", method = RequestMethod.POST)
	public @ResponseBody Response saveAdmissionConcent(@RequestBody AdmissionDto dto) {
		try {
			return iTemplateTransactionService.saveAdmissionConcent(dto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/template/transaction/update" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateTemplateAsInactive(@RequestBody TemplateTransactionDto dto){
		try{
			return iTemplateTransactionService.updateTemplateAsInactive(dto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value = "/templates/getSavedTemplateCount", method = RequestMethod.POST)
	public @ResponseBody Response getSavedTemplateCount(@RequestBody TemplateTransactionDto templateTransactionDto) {
		try {
			return iTemplateTransactionService.getSavedTemplateCount(templateTransactionDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/template/saveTemplateTransactionList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveTemplateTransactionList(@RequestBody List<TemplateTransactionDto> templateList){
		try{
			for(TemplateTransactionDto dto:templateList) {
			Response response=iTemplateTransactionService.saveTemplateTransaction(dto);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	

	@RequestMapping(value="/template/transaction/RIS" , method = RequestMethod.PUT , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateTemplateTransactionRIS(@RequestBody TemplateTransactionDto dto){
		try{
			Response res= iTemplateTransactionService.updateTemplateTransactionRIS(dto);
			Response isSyncRes=iRISWorkbenchService.getMyLifeDetail(dto.getOrgId());
			 OrganizationMasterDto dtoOrg = (OrganizationMasterDto) isSyncRes.getObject();
			if ((dto.getIsFinalRelease() != null && dto.getIsFinalRelease() == 'Y')) {
						int lowerLimit = 97;
						int upperLimit = 122;
						Random random = new Random();
						StringBuffer r = new StringBuffer(10);
						for (int i = 0; i < 10; i++) {
							int nextRandomChar = lowerLimit
									+ (int) (random.nextFloat() * (upperLimit - lowerLimit + 1));
							r.append((char) nextRandomChar);
						}

						String str = "";
						ByteArrayOutputStream byteOutPutStreamHtml = null;
						CloseableHttpClient httpClientHtml = null;
						InputStream htmlInputStream = null;
						byteOutPutStreamHtml = new ByteArrayOutputStream();
						httpClientHtml = HttpClientBuilder.create().build();
						String rptString = "{  " + " \"appName\": \"HIS\",  "
								+ " \"appSecretKey\": \"f306b6eccd9d5069d2dc71afe139271a\",  "
								+ "	\"reportDesignFilePath\": \"RadiologyReport.rpt\",  "
								+ " \"reportFormat\": \"PDF\",  "
								+ "	\"exportFileName\": \"RadiologyReport\",  "
								+ " \"reportParams\": [  "
								+ " {  "
								+ " \"paramName\": \"template_transaction_id\",  "
								+ " \"paramType\": \"Integer\",  "
								+ " \"paramValue\": " + dto.getTemplateTransactionId()
								+ " },{  "
								+ " \"paramName\": \"isOriginal\",  "
								+ " \"paramType\": \"Integer\",  "
								+ " \"paramValue\": 1  "
								+ " },{  "
								+ " \"paramName\": \"isHeader\",  "
								+ " \"paramType\": \"Integer\",  "
								+ " \"paramValue\": 0  "
								+ " }  " + "				        		]  "
								+ " }";
						String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(dto.getOrgId(),
								dto.getUnitId());
						ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();
						HttpPost pst = new HttpPost(API_URL);
						pst.addHeader("Content-Type", "application/json");
						pst.setEntity(new StringEntity(rptString));
						CloseableHttpClient httpClient = HttpClientBuilder.create().build();
						CloseableHttpResponse response = httpClient.execute(pst);
						HttpEntity resEntity = response.getEntity();
						resEntity.writeTo(byteOutPutStream);

						com.param.global.common.Response orgRes = iRISWorkbenchService
								.getOrganiazationFolderPath(dto.getOrgId());
						OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();
						String docPath = "";

						docPath = docUploadUtility.updateRISPdfFile(byteOutPutStream,
								organizationMasterDto.getOrganizationFolder(), r.toString(), dto.getOrgId(), dto.getUnitId());
						Response resNew = iRISWorkbenchService.updateRISPdfPathByTemplateTransactionId(docPath, dto);

						if(isSyncRes.getObject() != null && dtoOrg != null && dtoOrg.getMyLifeLink() != null && dtoOrg.getIsSyncScheduling() == true) {
							String MY_LIFE_URI = null;
							URI url = null;
							Response responseNew = null;
							Response res1 = iRISWorkbenchService.getConnectionStringForMyLife();
							if (res1.getObject() != null) {
								MyLifeConnectionLinkDto connectionLinkDto = (MyLifeConnectionLinkDto) res1.getObject();
								if (connectionLinkDto != null && connectionLinkDto.getMyLifeLink() != null
										&& connectionLinkDto.getMyLifeLink() != "")
									MY_LIFE_URI = connectionLinkDto.getMyLifeLink();
								System.out.println("In the APIS.....1111111");
								//MY_LIFE_URI="http://10.10.0.195:9091/MyLifeGlobal/api";

								if (MY_LIFE_URI != null) {

									responseNew = iAppointmentMasterService
											.syncPatientRadiologyReport(dto.getTemplateTransactionId());
									if (responseNew.getCode().equals(ICommonConstants.SUCCESS_CODE)
											&& responseNew.getObject() != null) {
										RadiologySyncDto serviceappoServiceMpprSyncDto = (RadiologySyncDto) responseNew
												.getObject();
										if (serviceappoServiceMpprSyncDto != null) {

											url = URI.create(MY_LIFE_URI + "/sync/opd/appointment/radiologyreport");
											String ss = new Gson().toJson(serviceappoServiceMpprSyncDto);
											String stringResponse = restTemplate.postForObject(url,
													serviceappoServiceMpprSyncDto, String.class);
											return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, dto.getTemplateTransactionId());

										}
									} else {
										return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, dto.getTemplateTransactionId());
									}
//        				}

								}
							} else {
								return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, dto.getTemplateTransactionId());
							}
						}


				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, dto.getTemplateTransactionId());
			}
			else {
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, dto.getTemplateTransactionId());
			}		

		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value = "/formsEngineUrl", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response sendTemplateURLS(@RequestBody TemplateTransactionDto dto) {
		try {
			return iTemplateTransactionService.sendTemplateURLS(dto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	
	
	@RequestMapping(value = "/saveTemplateSummery", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveTemplateSummery(@RequestBody TemplateResponseDto dto){

			try{
				Response res =  iTemplateTransactionService.saveTemplateSummery(dto);
				
			/*	if(res.getResObject() !=null) {
					
					TemplateTransactionDto dto1  = (TemplateTransactionDto) res.getResObject();
					
					if(dto1.getTemplateId() != null) {
						
						iTemplateTransactionService.updateNABH(dto1);
						
					}
				}*/
				 
				return res;
			}catch(Exception e){
			logger.error("method failed", e);
			}
			return new Response(ERROR, null, null, null,null);
		}

	@RequestMapping(value = "/printHTMLTemplate", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response printHTMLTemplate(@RequestBody TemplateTransactionDto dto){

			try{
				return iTemplateTransactionService.printHTMLTemplate(dto);
			}catch(Exception e){
			logger.error("method failed", e);
			}
			return new Response(ERROR, null, null, null,null);
		}
	
//try {
//			
////			String reportData=servletRequest.getParameter("reportParam");
////			Gson g = new Gson();
////			RadiologyOrdersListDto dto = g.fromJson(reportData, RadiologyOrdersListDto.class);
////			System.out.print(dto.getPatientName());
//			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//			byteStream = iTemplateTransactionService.printHTMLTemplate(dto);
////			OutputStream output = null;
////			servletResponse.setContentType("application/pdf");
////			output = servletResponse.getOutputStream();
////			output.write(byteStream.toByteArray());
////			output.close();
//			byteStream.close();
//		    return null;
//			}catch(Exception e){
//				
//			}
//		return null;


	@RequestMapping(value="/getDataByDateFromTemplateTransaction", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getDataForIPDCoversheetByDate(@RequestBody TemplateTransactionDto dto) {
		try
		{
			return iTemplateTransactionService.getTemplateTransactionForIPDCoversheetByDate(dto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/petient/getUniqueDateFromTemplateTransaction", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getUniqueDateForIPDCoversheet(@RequestBody TemplateTransactionDto dto) {
		try
		{
			return iTemplateTransactionService.getUniqueDateForIPDCoversheet(dto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/updateNABH", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response updateNABH(@RequestBody TemplateTransactionDto dto) {
		try
		{
			return iTemplateTransactionService.updateNABH(dto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}

