package com.param.CSSD.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.CSSD.dto.SterilizationRequestDto;
import com.param.CSSD.service.ISterilizationRequestService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping("/cssd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SterilizationRequestController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(SterilizationRequestController.class);


	@Autowired
	private ISterilizationRequestService iSterilizationRequestService;

	
	/**
	 * This method is used to get kit list available to request sterilization.
	 * @param requestingStore
	 * @param issueingStore
	 * @return
	 */
	@RequestMapping(value = "/getKitListForSterilizationRequest", method = RequestMethod.GET)
	public @ResponseBody Response getKitListForSterilizationRequest(@RequestParam("organizationId") Integer organizationId,@RequestParam("unitId") Integer unitId,
			@RequestParam("requestingStoreId") Integer requestingStoreId,@RequestParam("issueingStoreId") Integer issueingStoreId,@RequestParam("name") String name) {
		try {
			return iSterilizationRequestService.getKitListForSterilizationRequest(organizationId,unitId,requestingStoreId,issueingStoreId,name);
		} catch (Exception e) {
			logger.error("getKitListForSterilizationRequest failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * This method is used to create sterilization request.
	 * @param sterilizationRequestDto
	 * @return Response														
	 */
	@RequestMapping(value = "/saveSterilizationRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveSterilizationRequest(@RequestBody SterilizationRequestDto sterilizationRequestDto) {
		try {
			return iSterilizationRequestService.createSterilizationRequest(sterilizationRequestDto);
		} catch (Exception e) {
			logger.error("saveSterilizationRequest failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * This method is used to get sterilization request list by multiple filters.
	 * @param reuqestingStore
	 * @param issueingStore
	 * @param requestNo
	 * @param status
	 * @return Response
	 */
	@RequestMapping(value = "/getSterilizationRequestByMultipleFields", method = RequestMethod.GET)
	public @ResponseBody Response getSterilizationRequestByMultipleFields(
			@RequestParam("fromDate")@DateTimeFormat(pattern="dd-MM-yyyy") Date fromDate,
			@RequestParam("toDate")@DateTimeFormat(pattern="dd-MM-yyyy") Date toDate,
			@RequestParam(value="requestingStoreId",required=false) Integer requestingStoreId,
			@RequestParam(value="issueingStoreId",required=false) Integer issueingStoreId,
			@RequestParam(value="requestNo",required=false) Integer requestNo,
			@RequestParam(value="status",required=false) String status,
			@RequestParam("organizationId")Integer organizationId,
			@RequestParam("unitId")Integer unitId) {
		try {
			return iSterilizationRequestService.getSterilizationRequestByMultipleFields(fromDate,toDate,requestingStoreId,issueingStoreId,requestNo,status,organizationId,unitId);
		} catch (Exception e) {
			logger.error("getSterilizationRequestByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * This method is used to get sterilization request list by multiple filters.
	 * @param reuqestingStore
	 * @param issueingStore
	 * @param requestNo
	 * @param status
	 * @return Response
	 */
	@RequestMapping(value = "/getSterilizationRequestForAcceptanceByMultipleFields", method = RequestMethod.GET)
	public @ResponseBody Response getSterilizationRequestForAcceptanceByMultipleFields(
			@RequestParam("fromDate")@DateTimeFormat(pattern="dd-MM-yyyy") Date fromDate,
			@RequestParam("toDate")@DateTimeFormat(pattern="dd-MM-yyyy") Date toDate,
			@RequestParam(value="requestingStoreId",required=false) Integer requestingStoreId,
			@RequestParam(value="issueingStoreId",required=false) Integer issueingStoreId,
			@RequestParam(value="requestNo",required=false) Integer requestNo,
			@RequestParam(value="status",required=false) String status,
			@RequestParam("organizationId")Integer organizationId,
			@RequestParam("unitId")Integer unitId) {
		try {
			return iSterilizationRequestService.getSterilizationRequestForAcceptanceByMultipleFields(fromDate,toDate,requestingStoreId,issueingStoreId,requestNo,status,organizationId,unitId);
		} catch (Exception e) {
			logger.error("getSterilizationRequestForAcceptanceByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * This method is used to get particular sterilization request by request number.
	 * @param requestNo
	 * @return Response
	 */
	@RequestMapping(value = "/getSterilizationRequestByRequestNo", method = RequestMethod.GET)
	public @ResponseBody Response getSterilizationRequestByRequestNo(
			@RequestParam("requestNo") Integer requestNo,
			@RequestParam("organizationId") Integer organizationId,
			@RequestParam("unitId") Integer unitId) {
		try {
			return iSterilizationRequestService.getSterilizationRequestByRequestNo(requestNo,organizationId,unitId);
		} catch (Exception e) {
			logger.error("getSterilizationRequestByRequestNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to update sterilization request.
	 * @param sterilizationRequestDtoList
	 * @return Response														
	 */
	@RequestMapping(value = "/updateSterilizationRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateSterilizationRequest(@RequestBody SterilizationRequestDto sterilizationRequestDto) {
		try {
			return iSterilizationRequestService.updateSterilizationRequest(sterilizationRequestDto);
		} catch (Exception e) {
			logger.error("updateSterilizationRequest failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to update status for sterilization request.
	 * @param sterilizationRequestDtoList
	 * @return Response														
	 */
	@RequestMapping(value = "/updateSterilizationRequestStatus", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateSterilizationRequestStatus(@RequestBody List<SterilizationRequestDto> sterilizationRequestDtoList) {
		try {
			return iSterilizationRequestService.updateSterilizationRequestStatus(sterilizationRequestDtoList);
		} catch (Exception e) {
			logger.error("updateSterilizationRequestStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to cancel kit return.
	 * @param sterilizationRequestDto
	 * @return Response														
	 */
	@RequestMapping(value = "/cancelSterilizationRequest", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelSterilizationRequest(@RequestBody SterilizationRequestDto sterilizationRequestDto ){
		try {
			return iSterilizationRequestService.cancelSterilizationRequest(sterilizationRequestDto);
		} catch (Exception e) {
			logger.error("cancelSterilizationRequest failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to accept sterilization request .
	 * @param sterilizationRequestDto
	 * @return Response														
	 */
	@RequestMapping(value = "/acceptSterilizationRequest", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response acceptSterilizationRequest(@RequestBody SterilizationRequestDto sterilizationRequestDto ){
		try {
			return iSterilizationRequestService.acceptSterilizationRequest(sterilizationRequestDto);
		} catch (Exception e) {
			logger.error("acceptSterilizationRequest failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	
	
	
	
	
/*-----------------------------------------------------------------------------------------------------------------------------------------------------------------*/	
	
	
	
	
	
	
	
	
	/**
	 * This method is used to get kit return list.
	 * @return Response
	 */
	@RequestMapping(value = "/getSterilizationRequestList", method = RequestMethod.GET)
	public @ResponseBody Response getKitReturnList() {
		try {
			return iSterilizationRequestService.getKitReturnList();
		} catch (Exception e) {
			logger.error("getKitReturnList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to get the list of kits in a kit return by return number.
	 * @param ReturnNo
	 * @return
	 */
	@RequestMapping(value = "/getKitListByRequestNo", method = RequestMethod.GET)
	public @ResponseBody Response getKitListByReturnNo(@RequestParam("ReturnNo") Integer ReturnNo) {
		try {
			return iSterilizationRequestService.getKitListByReturnNo(ReturnNo);
		} catch (Exception e) {
			logger.error("getKitListByReturnNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
}
