package com.param.lis.global.controller;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.param.global.common.ICommonConstants;
import com.param.global.dto.TestCodeHeadDto;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.service.ITestCostHeadService;
import com.param.service.global.SessionService;
import com.param.service.global.model.CookieObjectDto;

@RestController
@RequestMapping("/LIS/global")
@SuppressWarnings({"rawtypes","unchecked"})
public class TestCostHeadController implements ICommonConstants,IError{
	@Autowired
	private ITestCostHeadService iTestCostHeadService;
	
	@Autowired
	SessionService sessionService;

	@RequestMapping(value = "/addTestCodeHead" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response addTestCodeHead(@RequestBody TestCodeHeadDto testCodeHeadDto){
		try{
			return iTestCostHeadService.addTestCodeHead(testCodeHeadDto);
		}catch(Exception e){
			
			throw e;
		}
	}
	@RequestMapping(value = "/updateTestCodeHead" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateTestCodeHead(@RequestBody TestCodeHeadDto testCodeHeadDto){
		try{
			return iTestCostHeadService.updateTestCodeHead(testCodeHeadDto);
		}catch(Exception e){
			
			throw e;
		}
	}
	@RequestMapping(value = "/getTestCodeHeadList/{orgId}/{offset}/{recordPerPage}" , method = RequestMethod.GET)
	public Response getTestCodeHeadList(@PathVariable ("orgId") Integer  orgId,@PathVariable(value = "offset") Integer offset,
			@PathVariable(value = "recordPerPage") Integer recordPerPage,HttpServletRequest request) throws Exception{
		try{
			TestCodeHeadDto dto=new TestCodeHeadDto();
			dto.setOrganizationId(orgId);
			dto.setOffset(offset);
			dto.setNoOfRecordsPerPage(recordPerPage);
			if(request.getCookies()!=null)
        	{
        		 List<Cookie> cookies = Arrays.stream(request.getCookies())
                         .filter(cookie -> cookie.getName().equals("cookieObject")).collect(Collectors.toList());
        		 if (cookies != null && cookies.size() > 0) {
                     sessionService.setCookieObjectDto(new Gson().fromJson(java.net.URLDecoder.decode(cookies.getFirst().getValue(), StandardCharsets.UTF_8.name()), CookieObjectDto.class));
                 dto.setUnitId(sessionService.getUnitId());
        		 }
        	}
			return iTestCostHeadService.getTestCodeHeadList(dto);
		}catch(Exception e){
			
			throw e;
		}
	}
	@RequestMapping(value = "/getTestCodeHeadListById" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTestCodeHeadListById(@RequestBody TestCodeHeadDto testCodeHeadDto){
		try{
			return iTestCostHeadService.getTestCodeHeadListById(testCodeHeadDto);
		}catch(Exception e){
			
			throw e;
		}
	}
	@RequestMapping(value="/updateTestCodeHeadStatus", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response updateCompanyMasterStatus(@RequestBody TestCodeHeadDto testCodeHeadDto) {
		try {
			return iTestCostHeadService.updateTestCodeHeadStatus(testCodeHeadDto);
		}catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value="/countOfTestCodeHeadList/{orgId}", method=RequestMethod.GET)
	public Response countOfTestCodeHeadList(@PathVariable ("orgId") Integer  orgId) {
		try {
			TestCodeHeadDto dto=new TestCodeHeadDto();
			dto.setOrganizationId(orgId);			
			return iTestCostHeadService.countOfTestCodeHeadList(dto);
		}catch (Exception e) {
			
			throw e;
		}
	}
	@RequestMapping(value = "/getActiveTestCodeHeadList/{orgId}" , method = RequestMethod.GET)
	public Response getActiveTestCodeHeadList(@PathVariable ("orgId") Integer  orgId ){
		try{
			TestCodeHeadDto dto=new TestCodeHeadDto();
			dto.setOrganizationId(orgId);
			return iTestCostHeadService.getActiveTestCodeHeadList(dto);
		}catch(Exception e){
			
			throw e;
		}
	}
}
