package com.param.billing.ipd.controller;

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

import com.param.billing.common.ICommonConstants;
import com.param.billing.global.transaction.dto.MPreauthRequestMasterDto;
import com.param.billing.global.transaction.model.MPreauthRequestMaster;
import com.param.billing.ipd.dto.PreAuthorizationRequestDto;
import com.param.billing.ipd.service.IPreAuthorizationRequestService;
import com.param.global.common.Response;
import com.param.opd.encounter.dto.PatientDocReqDto;

@RestController
@RequestMapping(value = "/api/ipd")
@SuppressWarnings({"rawtypes"})
public class PreAuthorizationRequestController  implements ICommonConstants {

	@Autowired
	private IPreAuthorizationRequestService iPreAuthorizationRequestService;
	
	/**
	 * TO GET PRE-AUTHORIZATION REQUEST LIST
	 */
	@RequestMapping(value = "/getPreAuthorizationRequestList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPreAuthorizationRequestList(@RequestBody PreAuthorizationRequestDto  preAuthorizationRequestDto) {
		try {
			//JsonCheck.mapJsonToObject(tDoctorShareDto, TDoctorShareReqDto.class);
			return iPreAuthorizationRequestService.getPreAuthorizationRequestList(preAuthorizationRequestDto);
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getPreAuthorizationRequestListCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPreAuthorizationRequestListCount(@RequestBody PreAuthorizationRequestDto  preAuthorizationRequestDto) {
		try {
			//JsonCheck.mapJsonToObject(tDoctorShareDto, TDoctorShareReqDto.class);
			return iPreAuthorizationRequestService.getPreAuthorizationRequestListCount(preAuthorizationRequestDto);
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	/**
	 * TO SAVE NEW PRE AUTHORIZATION REQUEST
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/saveNewPreAuthorizationRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveNewPreAuthorizationRequest(@RequestBody MPreauthRequestMasterDto  reqDto) {
		try {
			return iPreAuthorizationRequestService.saveNewPreAuthorizationRequest(reqDto);
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	/**
	 * TO UPDATE AUTHORIZATION REQUEST
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/updateAuthorizationRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateAuthorizationRequest(@RequestBody MPreauthRequestMasterDto  reqDto) {
		try {
			return iPreAuthorizationRequestService.updateAuthorizationRequest(reqDto);
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	/**
	 * TO GET PREAUTH REQ LIST BY COMPANY ID
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/getPreAuthorizationRequestListByPayeeId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPreAuthorizationRequestListByPayeeId(@RequestBody MPreauthRequestMasterDto  reqDto) {
		try {
			return iPreAuthorizationRequestService.getPreAuthorizationRequestListByPayeeId(reqDto);
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getPreAuthorizationRequestListByPayeeIdIPDCharges", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPreAuthorizationRequestListByPayeeIdIPDCharges(@RequestBody MPreauthRequestMasterDto  reqDto) {
		try {
			return iPreAuthorizationRequestService.getPreAuthorizationRequestListByPayeeIdIPDCharges(reqDto);
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	/**
	 * TO CANCEL PREAUTH REQUEST BY ID
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/cancelNewPreAuthorizationRequestByPreAuthorizationId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelNewPreAuthorizationRequestByPreAuthorizationId(@RequestBody MPreauthRequestMasterDto  reqDto) {
		try {
			return iPreAuthorizationRequestService.cancelNewPreAuthorizationRequestByPreAuthorizationId(reqDto);
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	/**
	 * TO DOCUMENT UPLOAD WHILE AUTHORIZATION SAVE
	 * @param organizationId
	 * @param inputFile
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/insuranceAuthorizationDoc/{organizationId}",headers = ("content-type=multipart/*"), method = RequestMethod.POST)
	  public @ResponseBody Response saveOrganizationImage( 
	      @PathVariable("organizationId") int organizationId,
	      @RequestParam("file") MultipartFile inputFile,
	      @RequestBody MPreauthRequestMaster reqDto) {
	    try {
	      return iPreAuthorizationRequestService.saveInsuranceAuthorizationDoc(organizationId, inputFile, reqDto);
	    } catch (Exception e) {
	      
	    }
	    return new Response<>(ERROR, null, null, null, null);
	  }
	
	/**
	 * METHOD TO UPDATE SPLIT STATUS BY PRE AUTHORIZATION ID AFTER SPLIT FUNCTIONALITY
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/updateIsSplitStatusByPreAuthorizationId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateIsSplitStatusByPreAuthorizationId(@RequestBody MPreauthRequestMasterDto  reqDto) {
		try {
			return iPreAuthorizationRequestService.updateIsSplitStatusByPreAuthorizationId(reqDto);
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	 @RequestMapping(value="/getPatientDocumentNameForPreAuthorization", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public Response getPatientDocumentNameForPreAuthorization(@RequestBody MPreauthRequestMasterDto patientDocReqDto) {
			try {
				return iPreAuthorizationRequestService.getPatientDocumentNameForPreAuthorization(patientDocReqDto);
			}catch (Exception e) {
				
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
     

}
