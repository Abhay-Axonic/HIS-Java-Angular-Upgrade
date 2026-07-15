package com.param.workbench.controller;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.param.global.dto.OrderMasterDto;
import org.apache.commons.io.IOUtils;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.google.gson.Gson;
import com.param.global.billing.service.IBillingService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.JsonCheck;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.common.ServletUtils;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.RadiologyOrdersListDto;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.service.IUnitApplicationBillingConfigurationService;
import com.param.workbench.service.IRISWorkbenchService;

@RestController
@RequestMapping("api/opd/RIS")
@SuppressWarnings({"rawtypes","unchecked"})
public class RISWorkbenchController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(RISWorkbenchController.class);

	@Autowired
	IRISWorkbenchService iRISWorkbenchService;
	
	@Autowired
	IBillingService globalBillingService;
	
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	private IUnitApplicationBillingConfigurationService unitApplicationBillingConfigurationService;
	
	@RequestMapping(value="getRadiologyWorkbenchWorklist", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getRadiologyWorkbenchWorklist(@RequestBody RadiologyOrdersListDto radiologyOrdersListDto) {
		try {
			return iRISWorkbenchService.getRadiologyWorkbenchWorklist(radiologyOrdersListDto);
		}catch (Exception e) {
		logger.error("getRadiologyWorkbenchWorklist failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="getRadiologyWorkbenchWorklistPagination", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getRadiologyWorkbenchWorklistPagination(@RequestBody RadiologyOrdersListDto radiologyOrdersListDto) {
		try {
			return iRISWorkbenchService.getRadiologyWorkbenchWorklistPagination(radiologyOrdersListDto);
		}catch (Exception e) {
		logger.error("getRadiologyWorkbenchWorklistPagination failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="getRadiologyResultEntrylist", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getRadiologyResultEntrylist(@RequestBody RadiologyOrdersListDto radiologyOrdersListDto) {
		try {
			return iRISWorkbenchService.getRadiologyResultEntrylist(radiologyOrdersListDto);
		}catch (Exception e) {
		logger.error("getRadiologyResultEntrylist failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="getradiologyReportReleaseWorkList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getradiologyReportReleaseWorkList(@RequestBody RadiologyOrdersListDto radiologyOrdersListDto) {
		try {
			return iRISWorkbenchService.getradiologyReportReleaseWorkList(radiologyOrdersListDto);
		}catch (Exception e) {
		logger.error("getradiologyReportReleaseWorkList failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="getradiologyWorkListForPrinting", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getradiologyWorkListForPrinting(@RequestBody RadiologyOrdersListDto radiologyOrdersListDto) {
		try {
			return iRISWorkbenchService.getradiologyWorkListForPrinting(radiologyOrdersListDto);
		}catch (Exception e) {
		logger.error("getradiologyWorkListForPrinting failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/printReport", method = RequestMethod.POST)
	public Response printRadiologyReport(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException {
		try {
			
			String reportData=servletRequest.getParameter("reportParam");
			Gson g = new Gson();
			RadiologyOrdersListDto dto = g.fromJson(reportData, RadiologyOrdersListDto.class);
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			byteStream = iRISWorkbenchService.printRadiologyReport(dto);
			OutputStream output = null;
			servletResponse.setContentType("application/pdf");
			output = servletResponse.getOutputStream();
			output.write(byteStream.toByteArray());
			output.close();
			byteStream.close();
		    return null;
			}catch(Exception e){
			logger.error("printRadiologyReport failed", e);
			}
		return null;
	}
	
	
	
	@RequestMapping(value = "/lab/printReport", method = RequestMethod.POST)
	public Response labPrintReport(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException {
		try {
			
			String reportData=servletRequest.getParameter("reportParam");
			Gson g = new Gson();
			RadiologyOrdersListDto dto = g.fromJson(reportData, RadiologyOrdersListDto.class);
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			byteStream = iRISWorkbenchService.labPrintReport(dto);
			OutputStream output = null;
			servletResponse.setContentType("application/pdf");
			output = servletResponse.getOutputStream();
			output.write(byteStream.toByteArray());
			output.close();
			byteStream.close();
		    return null;
			}catch(Exception e){
			logger.error("labPrintReport failed", e);
			}
		return null;
	}

	@RequestMapping(value = "/printReportTest", method = RequestMethod.POST)
	public Response printReportTest(@RequestBody RadiologyOrdersListDto radiologyOrdersListDto) throws IOException {
		try {
			//JsonCheck.mapJsonToObject(radiologyOrdersListDto, RadiologyOrdersListDto.class);
			return iRISWorkbenchService.printReportTest(radiologyOrdersListDto);
			
			}catch(Exception e){
			logger.error("printReportTest failed", e);
			}
		return null;
	}
	
	@RequestMapping(value = "/printReportTestLab", method = RequestMethod.POST)
	public Response printReportTestLab(@RequestBody RadiologyOrdersListDto radiologyOrdersListDto) throws IOException {
		try {
			//JsonCheck.mapJsonToObject(radiologyOrdersListDto, RadiologyOrdersListDto.class);
			return iRISWorkbenchService.printReportTestLab(radiologyOrdersListDto);
			
			}catch(Exception e){
			logger.error("printReportTestLab failed", e);
			}
		return null;
	}
	
	@RequestMapping(value = "/sendRadiologyReportOnMail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response sendReportOnMail(@RequestBody List<RadiologyOrdersListDto> list, HttpServletRequest request ) throws Exception
	{
		try
		{


			Boolean SendMail = true;

				RadiologyOrdersListDto orderDto = new RadiologyOrdersListDto();
				orderDto =list.get(0);
				if(orderDto.getIsFinalRelease() != null && orderDto.getIsFinalRelease().equals('Y')){
					if(orderDto.getEncounterId() != null ) {
						Response checkDueAmount = iRISWorkbenchService.checkDueAmount(orderDto);

						if (checkDueAmount.getObject() != null && checkDueAmount.getObject().equals(SendMail)) {
							SendMail = true;
						} else {
							SendMail = false;
						}
					}
				}



			if(SendMail ) {


				String resPublicUrl = unitApplicationBillingConfigurationService.getPublicUrl(list.get(0).getUnitId());
				for (RadiologyOrdersListDto radiologyOrdersListDto : list) {

					int lowerLimit = 97;
					int upperLimit = 122;
					Random random = new Random();
					StringBuffer r = new StringBuffer(10);
					for (int i = 0; i < 10; i++) {
						int nextRandomChar = lowerLimit + (int) (random.nextFloat() * (upperLimit - lowerLimit + 1));
						r.append((char) nextRandomChar);
					}

					String str = "";
					if (resPublicUrl == null) {
						str = ServletUtils.getBaseUrl(request) + "/api/opd/RIS/RADIOLOGY/" + radiologyOrdersListDto.getTemplateTransactionId() + "/" + radiologyOrdersListDto.getUnitId()
								+ "/" + radiologyOrdersListDto.getOrganizationId() + "/" + r.toString();
					} else {
						str = resPublicUrl + "/api/opd/RIS/RADIOLOGY/" + radiologyOrdersListDto.getTemplateTransactionId() + "/" + radiologyOrdersListDto.getUnitId()
								+ "/" + radiologyOrdersListDto.getOrganizationId() + "/" + r.toString();
					}

					Response res = iRISWorkbenchService.updateTemplateTransForRadiologyReportUrl(radiologyOrdersListDto, str);
					radiologyOrdersListDto.setRadiologyReportUrl(str);

				}
				return iRISWorkbenchService.sendRadiologyReportOnMail(list, "");
			}

			return new Response(ERROR, null, "Due amount pending", null, null);
		} catch (Exception e)
		{
//			logger.error("Exection", e);
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/RADIOLOGY/{templateTransactionId}/{unitId}/{orgId}/{randomNumber}")
	public void getReportByQRCode(@PathVariable("templateTransactionId")Integer templateTransactionId,@PathVariable("unitId")Integer unitId,@PathVariable("orgId")int orgId,@PathVariable("randomNumber")String randomNumber,HttpServletRequest request,HttpServletResponse response){
		try{
			

			String rptString = "{\"appName\": \"HIS\"," + 
					" \"appSecretKey\": \"f306b6eccd9d5069d2dc71afe139271a\"," + 
					"\"reportDesignFilePath\": \"RadiologyReportMail.rpt\"," + 
					"\"reportFormat\": \"PDF\"," + 
					"\"exportFileName\": \"radiologyReport\"," + 
					"\"reportParams\": [" + 
					"{" + 
					" \"paramName\": \"template_transaction_id\"," + 
					" \"paramType\": \"String\"," + 
					" \"paramValue\":"+templateTransactionId.toString()+ 
					" }" + 
					"]}";
			String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(orgId, unitId);
			ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();
			HttpPost pst = new HttpPost(API_URL);
			pst.addHeader("Content-Type", "application/json");
			pst.setEntity(new StringEntity(rptString));
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse result = httpClient.execute(pst);
			HttpEntity resEntity = result.getEntity();
			resEntity.writeTo(byteOutPutStream);
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "inline; filename=randomNumber.pdf");
			ServletOutputStream output = response.getOutputStream();
			output.write(byteOutPutStream.toByteArray());
			output.close();
		}catch(Exception e){
		logger.error("getReportByQRCode failed", e);
		}
	}
	
      
}
