package com.param.billing.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.global.transaction.dto.DoctorReferralShareMapperDto;
import com.param.billing.global.transaction.model.DoctorReferralShareMapper;
import com.param.billing.unit.dto.DoctorShareReqDto;
import com.param.billing.unit.dto.MDoctorReferralShareReqDto;
import com.param.billing.unit.service.IMDoctorReferralShareService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;


@RestController
@RequestMapping(value="unit/DoctorShare")
@SuppressWarnings({"rawtypes","unchecked"})
public class MDoctorReferralShareController  implements ICommonConstants{
	@Autowired
	private IMDoctorReferralShareService iMDoctorReferralShareService;
	
	@RequestMapping(value = "/saveDoctorReferralShare" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveGlobalDoctorShare(@RequestBody MDoctorReferralShareReqDto mDoctorReferralShareReqDto){
		Response<MDoctorReferralShareReqDto, Integer> response = null;
		try{
			response= iMDoctorReferralShareService.saveDoctorReferralShare(mDoctorReferralShareReqDto);
			return response;
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getDoctorReferralShareDetails" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorReferralShareDetails(@RequestBody MDoctorReferralShareReqDto mDoctorReferralShareReqDto){
		try{
				return iMDoctorReferralShareService.getDoctorReferralShareDetails(mDoctorReferralShareReqDto);
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/getDoctorReferralShareDetailsByRefDocId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorReferralShareDetailsByRefDocId(@RequestBody MDoctorReferralShareReqDto mDoctorReferralShareReqDto){
		try{
				return iMDoctorReferralShareService.getDoctorReferralShareDetailsByRefDocId(mDoctorReferralShareReqDto);
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/updateStatusDoctorReferralShare" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusDoctorReferralShare(@RequestBody DoctorReferralShareMapperDto dto){
		try{
				return iMDoctorReferralShareService.updateStatusDoctorReferralShare(dto);
		}catch(ServiceException se){
			throw se;
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
	}
	/*@RequestMapping(value = "/getDoctorGradeMaster/{orgId}/{orgUnitId}", method = RequestMethod.GET)
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
	*/
	
}
