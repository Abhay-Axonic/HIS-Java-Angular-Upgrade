package com.param.data.display;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.data.dto.OtherAppDataReqDto;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@RestController
@RequestMapping("/api/otherAppData")
@SuppressWarnings("rawtypes")
public class OtherDBDataDisplayController  implements ICommonConstants
{
	
	@Autowired
	private IOtherDBDataDisplayService iOtherDBDataDisplayService;
	
	@RequestMapping(value = "/encounterList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response encounterList(@RequestBody OtherAppDataReqDto reqDto){
		try{
			return iOtherDBDataDisplayService.encounterList(reqDto);
			
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/quickSummery" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response quickSummery(@RequestBody OtherAppDataReqDto reqDto){
		try{
			return iOtherDBDataDisplayService.quickSummery(reqDto);
			
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/triageList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTriageList(@RequestBody OtherAppDataReqDto reqDto){
		try{
			return iOtherDBDataDisplayService.getTriageList(reqDto);
			
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/history" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getHistory(@RequestBody OtherAppDataReqDto reqDto){
		try{
			return iOtherDBDataDisplayService.getHistory(reqDto);
			
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/physicalExamination" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPhysicalExamination(@RequestBody OtherAppDataReqDto reqDto){
		try{
			return iOtherDBDataDisplayService.getPhysicalExamination(reqDto);
			
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/prescription" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPrecriptionList(@RequestBody OtherAppDataReqDto reqDto){
		try{
			return iOtherDBDataDisplayService.getPrecriptionList(reqDto);
			
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/laboratory" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getLabOrderedsList(@RequestBody OtherAppDataReqDto reqDto){
		try{
			return iOtherDBDataDisplayService.getLabOrderedsList(reqDto);
			
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/diagonosys" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDiagnosysList(@RequestBody OtherAppDataReqDto reqDto){
		try{
			return iOtherDBDataDisplayService.getDiagnosysList(reqDto);
			
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	

	
	@RequestMapping(value = "/procedures" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getProceduresList(@RequestBody OtherAppDataReqDto reqDto){
		try{
			return iOtherDBDataDisplayService.getProceduresList(reqDto);
			
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/radiology" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getRadiologyOrdersList(@RequestBody OtherAppDataReqDto reqDto){
		try{
			return iOtherDBDataDisplayService.getRadiologyOrdersList(reqDto);
			
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/reviewNotes" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getreviewNotesList(@RequestBody OtherAppDataReqDto reqDto){
		try{
			return iOtherDBDataDisplayService.getreviewNotesList(reqDto);
			
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	@RequestMapping(value = "/provisionalDiagnosys" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getProvisionalDiagonsys(@RequestBody OtherAppDataReqDto reqDto){
		try{
			return iOtherDBDataDisplayService.getProvisionalDiagonsys(reqDto);
			
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
}
