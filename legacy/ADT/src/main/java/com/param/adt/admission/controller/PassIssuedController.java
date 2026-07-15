package com.param.adt.admission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.adt.admission.model.VisitorAgainstPatient;
import com.param.adt.admission.service.IPassIssuedService;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.BedMasterDto;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.global.VisitorDto;
@Controller
@RequestMapping("/adt")
@SuppressWarnings({"rawtypes","unchecked"})
public class PassIssuedController implements ICommonConstants
{
	
	@Autowired
	IPassIssuedService iPassIssuedService;
	
	
	@RequestMapping(value="/getVisitorTypeList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getVisitorTypeList(@RequestBody AdmissionDto admissionDto){
		try{			
			
			return iPassIssuedService.getVisitorTypeList(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/saveVisitors" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveVisitors(@RequestBody VisitorDto visitorDto){
		try{			
			
			return iPassIssuedService.saveVisitorAgainsPatient(visitorDto);
//			Response res= iPassIssuedService.saveVisitorAgainsPatient(visitorDto);
//			VisitorAgainstPatient visitorAgainstPatient=(VisitorAgainstPatient) res.getObject();
			/*visitorDto.setVisitorPatientId(visitorAgainstPatient.getVisitorPatientId());
			//System.out.println(visitorDto.getVisitorPatientId());
			if(res.getStatus().equals(SUCCESS))
			{
				return iPassIssuedService.saveVisitorPatientMapper(visitorDto);
			}*/
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/getVisitorsList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getVisitorsList(@RequestBody VisitorDto visitorDto){
		try{			
			
			return iPassIssuedService.getVisitorsList(visitorDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/updateVisitorsDetails" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateVisitorsDetails(@RequestBody VisitorDto visitorDto){
		try{			
			
			return iPassIssuedService.updateVisitorsDetails(visitorDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/getBedListByWardId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBedListByWardId(@RequestBody BedMasterDto bedMasterDto){
		try{			
			
			return iPassIssuedService.getBedListByWardId(bedMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/getAdmittedPatientListByMultipleCriteria" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAdmittedPatientListByMultipleCriteria(@RequestBody AdmissionDto admissionDto){
		try{			
			return iPassIssuedService.getAdmittedPatientListByMultipleCriteria(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	@RequestMapping(value="/checkPassNumberAlreadyIssueOrNot" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response checkPassNumberAlreadyIssueOrNot(@RequestBody VisitorDto visitorDto){
		try{			
			return iPassIssuedService.getPassNumberAlreadyIssueOrNot(visitorDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	@RequestMapping(value="/checkNoOfPassIssueAgainstAdmission" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response checkNoOfPassIssueAgainstAdmission(@RequestBody VisitorDto visitorDto){
		try{			
			return iPassIssuedService.checkNoOfPassIssueAgainstAdmission(visitorDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	@RequestMapping(value="/generatePassNumber{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response generatePassNumber(@PathVariable ("orgId") Integer orgId,
			@PathVariable ("unitId") Integer unitId){
		try{			
			return iPassIssuedService.generatePassNumber(orgId,unitId);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	@RequestMapping(value="/getVisitorPassList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getVisitorPassList(@RequestBody AdmissionDto admissionDto){
		try{			
			
			return iPassIssuedService.getVisitorPassList(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
}
