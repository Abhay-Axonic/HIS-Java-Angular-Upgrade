package com.param.global.org.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.param.global.dto.CommonDto;
import com.param.global.dto.UserMasterDto;
import com.param.global.org.dto.UnitSubscriptionConfigDto;
import com.param.global.org.dto.UnitSubscriptionDetailsDto;
import com.param.global.service.IRazorPayService;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.param.common.dto.CommonResponse;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.org.dto.UnitMasterDto;
import com.param.global.org.service.IGlobalUnitMasterService;
import com.param.utility.ITimeZoneService;
import com.param.utility.TimeZoneDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SuppressWarnings({"rawtypes","unchecked"})
public class GlobalUnitMasterController  implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(GlobalUnitMasterController.class);

	@Autowired
	private IGlobalUnitMasterService iGlobalUnitMasterService;
	
	@Autowired
	ITimeZoneService iTimeZoneService;

	@Autowired
	IRazorPayService razorPayService;



	
	@RequestMapping(value = "/unit/save" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create Generic", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "prucedureConfiguration Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized Create to prucedureConfiguration"),
			@ApiResponse(code = 403, message = "Create prucedureConfiguration is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> saveUnitMaster(@RequestBody UnitMasterDto unitMasterDto,HttpServletRequest request) throws ApplicationException{
		  StringBuilder basePath = new StringBuilder();
		  String scheme = request.getScheme();
		  String domain = request.getServerName();
		  int port = request.getServerPort();
		  basePath.append(scheme);
		  basePath.append("://");
		  basePath.append(domain);
		  if("http".equalsIgnoreCase(scheme) && 80 != port) {
		    basePath.append(":").append(String.valueOf(port));
		  } else if("https".equalsIgnoreCase(scheme) && port != 443) {
		    basePath.append(":").append(String.valueOf(port));
		  }
		  
		  String contextPath=request.getContextPath().toString();
		  if(contextPath!=null) {
			  if(!contextPath.equals("/")) {
				  basePath.append(contextPath);
			  }
		  }
		 unitMasterDto.setApplicationUrl(basePath.toString());
      return new ResponseEntity<CommonResponse>(iGlobalUnitMasterService.saveUnitMaster(unitMasterDto), HttpStatus.CREATED);
		
	}
	
	/*
	@RequestMapping(value="/unit/save" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveUnitMaster(@RequestBody UnitMasterDto unitMasterDto){
		try{
			return iGlobalUnitMasterService.saveUnitMaster(unitMasterDto);
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	*/
	
	
	@RequestMapping(value = "/unit/update" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create Generic", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "prucedureConfiguration Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized Create to prucedureConfiguration"),
			@ApiResponse(code = 403, message = "Create prucedureConfiguration is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	
	
	public ResponseEntity<CommonResponse> updateUnitMaster(@RequestBody UnitMasterDto unitMasterDto,HttpServletRequest request) throws ApplicationException{
		  StringBuilder basePath = new StringBuilder();
		  String scheme = request.getScheme();
		  String domain = request.getServerName();
		  int port = request.getServerPort();
		  basePath.append(scheme);
		  basePath.append("://");
		  basePath.append(domain);
		  if("http".equalsIgnoreCase(scheme) && 80 != port) {
		    basePath.append(":").append(String.valueOf(port));
		  } else if("https".equalsIgnoreCase(scheme) && port != 443) {
		    basePath.append(":").append(String.valueOf(port));
		  }
		  
		  String contextPath=request.getContextPath().toString();
		  if(contextPath!=null) {
			  if(!contextPath.equals("/")) {
				  basePath.append(contextPath);
			  }
		  }
		 unitMasterDto.setApplicationUrl(basePath.toString());
		return new ResponseEntity<CommonResponse>(iGlobalUnitMasterService.updateUnitMaster(unitMasterDto), HttpStatus.CREATED);
		
	}
	/*@RequestMapping(value="/unit/update" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateUnitMaster(@RequestBody UnitMasterDto unitMasterDto){
		try{
			return iGlobalUnitMasterService.updateUnitMaster(unitMasterDto);
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, null, null, null, null);
	}*/
	
	@RequestMapping(value="/unit/{unitId}" , method = RequestMethod.GET)
	public @ResponseBody Response getUnitMasterById(@PathVariable("unitId") Integer unitId){
		try{
			return iGlobalUnitMasterService.getUnitById(unitId);
		}catch(Exception e){
			logger.error("getUnitMasterById failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value="/unit/count/{orgId}" , method = RequestMethod.GET)
	public @ResponseBody Response getUnitMasterCount(@PathVariable("orgId") Integer orgId){
		try{
			return iGlobalUnitMasterService.getUnitTotalCount(orgId);//minesh
		}catch(Exception e){
			logger.error("getUnitMasterCount failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value="/unit/{offSet}/{noOfRecordsPerPage}/{orgId}" , method = RequestMethod.GET)
	public @ResponseBody Response getAllUnitList(@PathVariable("offSet") Integer offSet , @PathVariable("noOfRecordsPerPage") Integer noOfRecordsPerPage,@PathVariable("orgId") Integer orgId){
		try{
			UnitMasterDto unitMasterDto = new UnitMasterDto();
			unitMasterDto.setOffSet(offSet);
			unitMasterDto.setNoOfRecordsPerPage(noOfRecordsPerPage);
			unitMasterDto.setOrganizationId(orgId);
			return iGlobalUnitMasterService.getAllUnitList(unitMasterDto);
		}catch(Exception e){
			logger.error("getAllUnitList failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value="/unit/status/{unitId}/{status}" , method = RequestMethod.GET)
	public @ResponseBody Response updateUnitStatus(@PathVariable("unitId") Integer unitId , @PathVariable("status") char status){
		try{
			UnitMasterDto unitMasterDto = new UnitMasterDto();
			unitMasterDto.setUnitId(unitId);
			unitMasterDto.setStatus(status);
			return iGlobalUnitMasterService.updateUnitStatusById(unitMasterDto);
		}catch(Exception e){
			logger.error("updateUnitStatus failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/unit/image/{unitId}", headers = ("content-type=multipart/*"), method = RequestMethod.POST)
	public @ResponseBody Response saveUnitImage(@PathVariable("unitId") int unitId,
			@RequestParam("file") MultipartFile inputFile) {
		try {
			return iGlobalUnitMasterService.saveUnitImage(unitId, inputFile);
		} catch (Exception e) {
			logger.error("saveUnitImage failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value="/unit/org/{orgId}" , method = RequestMethod.GET)
	public @ResponseBody Response getUnitByOrganisation(@PathVariable("orgId") Integer orgId){
		try{
			return iGlobalUnitMasterService.getUnitByOrganisation(orgId);
		}catch(Exception e){
			logger.error("getUnitByOrganisation failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value="/unit/prefixNumber/{unitId}/{storeId}/{screenId}" , method = RequestMethod.GET)
	public @ResponseBody String getGeneratedNo(@PathVariable("storeId") Integer storeId,@PathVariable("unitId") Integer unitId,@PathVariable("screenId") Integer screenId){
		try{
			return iGlobalUnitMasterService.getGeneratedNo(storeId, unitId, screenId);
		}catch(Exception e){
			logger.error("getGeneratedNo failed", e);
		}
		return null;
	}
	
	@RequestMapping(value = "/unit/sendMailInvite/{doctorMail}/{patientMail}/{date}/{time}/{remark}" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response sendMailInvite(@PathVariable("doctorMail") String doctorMail,@PathVariable("patientMail") String patientMail,
			@PathVariable("date") String date,
			@PathVariable("time") String time,
			@PathVariable("remark") String remark){
		try{
			return iGlobalUnitMasterService.sendMailInvite(doctorMail, patientMail, date,time,remark);
		}catch(Exception e){
			logger.error("sendMailInvite failed", e);
		}
		return null;
	}
	
	@RequestMapping(value="/global/gtMyLifeBaseUrl" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response gtMyLifeBaseUrl(){
		try{
			
			return iGlobalUnitMasterService.gtMyLifeBaseUrl();
		}catch(Exception e){
			logger.error("gtMyLifeBaseUrl failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/timezones" , method = RequestMethod.GET)
	public @ResponseBody Response getTimezones(){
		try{
			 List<TimeZoneDto> tzList = iTimeZoneService.getTimeZones();
			 return new Response<>(SUCCESS, null, null, tzList, null);
		}catch(Exception e){
			logger.error("getTimezones failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}


	 final String SUBSCRIPTION_URL = "/unit/subcription";

	@RequestMapping(value=SUBSCRIPTION_URL +"/types" , method = RequestMethod.GET)
	public @ResponseBody Response getSubScriptionTypes(){
		try{
			return iGlobalUnitMasterService.getSubScriptionTypes();
		}catch(Exception e){
			logger.error("getSubScriptionTypes failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	//Subscription Model
	@RequestMapping(value = SUBSCRIPTION_URL + "/config" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addUpdateSubscriptionConfig(@RequestBody UnitSubscriptionConfigDto dto){
		try{
			return iGlobalUnitMasterService.addUpdateSubscriptionConfig(dto);

		}catch(Exception e){
			logger.error("addUpdateSubscriptionConfig failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}


	@RequestMapping(value = SUBSCRIPTION_URL + "/config/details" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getUnitSubscriptionConfigDetals(@RequestBody UnitMasterDto dto){
		try{
			return iGlobalUnitMasterService.getSubScriptionConfig(dto);
		}catch(Exception e){
			logger.error("getUnitSubscriptionConfigDetals failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}

	@RequestMapping(value = SUBSCRIPTION_URL + "/details" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSubScrtionDetails(@RequestBody UnitSubscriptionDetailsDto dto){
		try{
			return iGlobalUnitMasterService.addSubScrtionDetails(dto);
		}catch(Exception e){
			logger.error("addSubScrtionDetails failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}

	@RequestMapping(value = SUBSCRIPTION_URL + "/fetch" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSubscriptionById(@RequestBody UnitSubscriptionDetailsDto dto){
		try{
			return iGlobalUnitMasterService.getSubscriptionById(dto);
		}catch(Exception e){
			logger.error("getSubscriptionById failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}

	@RequestMapping(value = SUBSCRIPTION_URL + "/unitDetails" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSubcriptionDetails(@RequestBody UnitSubscriptionDetailsDto dto){
		try{
			return iGlobalUnitMasterService.getSubcriptionDetails(dto);
		}catch(Exception e){
			logger.error("getSubcriptionDetails failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}

	@RequestMapping(value = SUBSCRIPTION_URL , method = RequestMethod.POST )
	public ModelAndView addUnitSubscriptionCallBack(@RequestBody String payload,HttpServletRequest request){
		//String baseUrl = request.getRequestURL().toString();
		//String contextPath = request.getContextPath();
		//baseUrl = baseUrl.substring(0, baseUrl.indexOf(contextPath) + contextPath.length());
		StringBuilder baseUrlBuilder = new StringBuilder();
		baseUrlBuilder.append(request.getScheme()).append("://").append(request.getServerName());
		int port = request.getServerPort();
		if (port != 80 && port != 443) {
			baseUrlBuilder.append(":").append(port);
		}
		String baseUrl = baseUrlBuilder.toString();
		ModelAndView modelAndView = new ModelAndView("redirect:" + baseUrl);
		try{

			String razorPayPaymentId = request.getParameter("razorpay_payment_id");
			String razorPayOrderId = request.getParameter("razorpay_order_id");
			String razorPaySignature = request.getParameter("razorpay_signature");
			if(razorPayPaymentId!=null && !razorPayPaymentId.isEmpty()){
			System.out.println("Payment is successful");
			String status = "paid";
			razorPayService.updatePaymentLinkLog(status,razorPayOrderId);
			Response razorPayRes = iGlobalUnitMasterService.processUnitSubscription(razorPayOrderId);
			}
			else {
				System.out.println("Payment is failed");
			}
			return modelAndView;
		} catch (Exception e) {
			logger.error("addUnitSubscriptionCallBack failed", e);
			return modelAndView;
		}
	}

	@RequestMapping(value = SUBSCRIPTION_URL + "/check" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response checkUnitSubscription(@RequestBody UnitMasterDto dto){
		try{
			return iGlobalUnitMasterService.checkUnitSubscription(dto,dto.getCreatedBy());
		}catch(Exception e){
			logger.error("checkUnitSubscription failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}

}
