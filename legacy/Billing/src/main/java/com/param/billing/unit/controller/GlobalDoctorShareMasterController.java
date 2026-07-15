package com.param.billing.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.unit.dto.DoctorShareReqDto;
import com.param.billing.unit.dto.GlobalDocShareServiceRateDto;
import com.param.billing.unit.dto.IndividualDoctorShareReqDto;
import com.param.billing.unit.service.IGlobalDoctorShareService;
import com.param.billing.unit.service.IIndividualDoctorShareService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.service.dto.CommonDto;


@RestController
@RequestMapping(value="unit/DoctorShare")
@SuppressWarnings({"rawtypes","unchecked"})
public class GlobalDoctorShareMasterController  implements ICommonConstants{
	@Autowired
	private IGlobalDoctorShareService iGlobalDoctorShareService;
	
	@Autowired
	private IIndividualDoctorShareService iIndividualDoctorShareService;
	
	/**
	 * TO SAVE GLOBAL DOCTOR SHARE CONFIGURATION
	 * @param doctorShareReqDto
	 * @return
	 */
	@RequestMapping(value = "/saveGlobalDoctorShare" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveGlobalDoctorShare(@RequestBody DoctorShareReqDto doctorShareReqDto){
		Response<DoctorShareReqDto, Integer> response = null;
		try{
			response= iGlobalDoctorShareService.saveGlobalDoctorShare(doctorShareReqDto);
			return response;
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/duplicate/check/globalDoctorShare" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response duplicateCheckForGlobalDoctorShare(@RequestBody DoctorShareReqDto doctorShareReqDto){
		Response<DoctorShareReqDto, Integer> response = null;
		try{
			response= iGlobalDoctorShareService.duplicateCheckForGlobalDoctorShare(doctorShareReqDto);
			return response;
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * TO SAVE INDIVIDUAL LEVER DOCTOR SHARE CONFIGURATION
	 * @param individualDoctorShareReqDto
	 * @return
	 */
	@RequestMapping(value = "/saveIndividualDoctorShare" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveIndividualDoctorShare(@RequestBody IndividualDoctorShareReqDto individualDoctorShareReqDto){
		Response<IndividualDoctorShareReqDto, Integer> response = null;
		try{
			 response= iIndividualDoctorShareService.saveIndividualDoctorShare(individualDoctorShareReqDto);
			return response;
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	
/*	@RequestMapping(value="/searchCompanyContract" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchCompanyContract(@RequestBody ServiceForCompnayContarctReqDto reqDto){
		try{
			return iCompanyContractService.searchCompanyContract(reqDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}*/
	
	/**
	 * TO GET DOCTOR GRADE MASTER LIST
	 * @param orgId
	 * @param orgUnitId
	 * @return
	 */
	@RequestMapping(value = "/getDoctorGradeMaster/{orgId}/{orgUnitId}", method = RequestMethod.GET)
	public @ResponseBody Response getDoctorGradeMaster(@PathVariable(value = "orgId") Integer orgId,@PathVariable(value = "orgUnitId") Integer orgUnitId)
	{
		Response<CommonDto, Integer> response = null;
		try
		{
			response = iGlobalDoctorShareService.getDoctorGradeMaster(orgId, orgUnitId);
			return response;
		} catch (Exception e)
		{
			return new Response(ERROR, ERROR, ERROR, null, null);
		}
	}
	
	@RequestMapping(value = "/getGlobalDoctorShareDepartmentMasterList",method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getGlobalDoctorShareMasterList(@RequestBody DoctorShareReqDto doctorShareReqDto)
	{
		Response<CommonDto, Integer> response = null;
		try
		{
			response = iGlobalDoctorShareService.getGlobalDoctorShareMasterList(doctorShareReqDto);
			return response;
		} catch (Exception e)
		{
			return new Response(ERROR, ERROR, ERROR, null, null);
		}
	}
	
	@RequestMapping(value = "/getGlobalDoctorShareDepartmentMasterListSearch",method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getGlobalDoctorShareDepartmentMasterListSearch(@RequestBody DoctorShareReqDto doctorShareReqDto)
	{
		Response<CommonDto, Integer> response = null;
		try
		{
			response = iGlobalDoctorShareService.getGlobalDoctorShareDepartmentMasterListSearch(doctorShareReqDto);
			return response;
		} catch (Exception e)
		{
			return new Response(ERROR, ERROR, ERROR, null, null);
		}
	}
	
	@RequestMapping(value = "/getGlobalDoctorShareDetailsByDepartmentId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getGlobalDoctorShareDetailsByDepartmentId(@RequestBody DoctorShareReqDto doctorShareReqDto){
		Response<DoctorShareReqDto, Integer> response = null;
		try{
			response= iGlobalDoctorShareService.getGlobalDoctorShareDetailsByDepartmentId(doctorShareReqDto);
			return response;
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getDoctorShareDepartmentMasterList",method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorShareDepartmentMasterList(@RequestBody IndividualDoctorShareReqDto doctorShareReqDto)
	{
		Response<CommonDto, Integer> response = null;
		try
		{
			response = iIndividualDoctorShareService.getDoctorShareDepartmentMasterList(doctorShareReqDto);
			return response;
		} catch (Exception e)
		{
			return new Response(ERROR, ERROR, ERROR, null, null);
		}
	}
	
	@RequestMapping(value = "/getDoctorShareDepartmentMasterListSearch",method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorShareDepartmentMasterListSearch(@RequestBody IndividualDoctorShareReqDto doctorShareReqDto)
	{
		Response<CommonDto, Integer> response = null;
		try
		{
			response = iIndividualDoctorShareService.getDoctorShareDepartmentMasterListSearch(doctorShareReqDto);
			return response;
		} catch (Exception e)
		{
			return new Response(ERROR, ERROR, ERROR, null, null);
		}
	}
	
	@RequestMapping(value = "/getDoctorShareDetailsByDepartmentIdAndDoctorId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorShareDetailsByDepartmentIdAndDoctorId(@RequestBody IndividualDoctorShareReqDto doctorShareReqDto){
		Response<IndividualDoctorShareReqDto, Integer> response = null;
		try{
			response= iIndividualDoctorShareService.getDoctorShareDetailsByDepartmentIdAndDoctorId(doctorShareReqDto);
			return response;
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getRatesForSelectedService" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getRatesForSelectedService(@RequestBody GlobalDocShareServiceRateDto doctorShareReqDto){
		Response response = null;
		try{
			response= iGlobalDoctorShareService.getRatesForSelectedService(doctorShareReqDto);
			return response;
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/saveReferralShare" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveReferralShare(@RequestBody IndividualDoctorShareReqDto individualDoctorShareReqDto){
		Response<IndividualDoctorShareReqDto, Integer> response = null;
		try{
			 response= iIndividualDoctorShareService.saveReferralShare(individualDoctorShareReqDto);
			return response;
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getIndividualDoctorReferralDetailsByDepartmentId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getIndividualDoctorReferralDetailsByDepartmentId(@RequestBody IndividualDoctorShareReqDto doctorShareReqDto){
		Response<DoctorShareReqDto, Integer> response = null;
		try{
			response= iGlobalDoctorShareService.getIndividualDoctorReferralDetailsByDepartmentId(doctorShareReqDto);
			return response;
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getIndividualDoctorReferralDetailsByDepartmentIdSearch" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getIndividualDoctorReferralDetailsByDepartmentIdSearch(@RequestBody IndividualDoctorShareReqDto doctorShareReqDto){
		Response<DoctorShareReqDto, Integer> response = null;
		try{
			response= iGlobalDoctorShareService.getIndividualDoctorReferralDetailsByDepartmentIdSearch(doctorShareReqDto);
			return response;
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getIndividualDoctorReferralDetailsByReferralId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getIndividualDoctorReferralDetailsByReferralId(@RequestBody IndividualDoctorShareReqDto doctorShareReqDto){
		Response<DoctorShareReqDto, Integer> response = null;
		try{
			response= iGlobalDoctorShareService.getIndividualDoctorReferralDetailsByReferralId(doctorShareReqDto);
			return response;
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/updateStatusByReferralId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusByReferralId(@RequestBody IndividualDoctorShareReqDto doctorShareReqDto1){
		Response<DoctorShareReqDto, Integer> response = null;
		try{
			response= iGlobalDoctorShareService.updateStatusByReferralId(doctorShareReqDto1);
			return response;
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
}
