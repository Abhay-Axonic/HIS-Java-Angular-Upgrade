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

import com.param.CSSD.dto.KitReturnDto;
import com.param.CSSD.service.IKitReturnService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.JsonCheck;
import com.param.global.common.Response;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping("/cssd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class KitReturnController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(KitReturnController.class);


	@Autowired
	private IKitReturnService iKitReturnService;

	/**
	 * This method is used to get kit list available to return.
	 * 
	 * @param fromStore
	 * @param toStore
	 * @return
	 */
	@RequestMapping(value = "/getKitListForKitReturn", method = RequestMethod.GET)
	public @ResponseBody Response getKitListForKitReturn(@RequestParam("organizationId") Integer organizationId,
			@RequestParam("unitId") Integer unitId, @RequestParam("fromStoreId") Integer fromStoreId,
			@RequestParam("toStoreId") Integer toStoreId, @RequestParam("name") String name) {
		try {
			return iKitReturnService.getKitListForKitReturn(organizationId, unitId, fromStoreId, toStoreId, name);
		} catch (Exception e) {
			logger.error("getKitListForKitReturn failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * This method is used to create kit return.
	 * 
	 * @param kitReturnDto
	 * @return Response
	 */
	@RequestMapping(value = "/saveKitReturn", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveKitReturn(@RequestBody KitReturnDto kitReturnDto) {
		try {
			return iKitReturnService.createKitReturn(kitReturnDto);
		} catch (Exception e) {
			logger.error("saveKitReturn failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * This method is used to get kit return list by multiple filters.
	 * 
	 * @param fromStore
	 * @param toStore
	 * @param returnNo
	 * @param status
	 * @return Response
	 */
	@RequestMapping(value = "/getKitReturnByMultipleFields", method = RequestMethod.GET)
	public @ResponseBody Response getKitReturnByMultipleFields(
			@RequestParam("fromDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date fromDate,
			@RequestParam("toDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date toDate,
			@RequestParam(value = "fromStore", required = false) Integer fromStore,
			@RequestParam(value = "toStore", required = false) Integer toStore,
			@RequestParam(value = "returnNo", required = false) Integer returnNo,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam("organizationId") Integer organizationId, @RequestParam("unitId") Integer unitId) {
		try {
			return iKitReturnService.getKitReturnByMultipleFields(fromDate, toDate, fromStore, toStore, returnNo,
					status, organizationId, unitId);
		} catch (Exception e) {
			logger.error("getKitReturnByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * This method is used to get particular kit return by return number.
	 * 
	 * @param returnNo
	 * @return Response
	 */
	@RequestMapping(value = "/getKitReturnByReturnNo", method = RequestMethod.GET)
	public @ResponseBody Response getKitReturnByReturnNo(@RequestParam("returnNo") Integer returnNo,
			@RequestParam("organizationId") Integer organizationId, @RequestParam("unitId") Integer unitId) {
		try {
			return iKitReturnService.getKitReturnByReturnNo(returnNo, organizationId, unitId);
		} catch (Exception e) {
			logger.error("getKitReturnByReturnNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * This method is used to update kit return.
	 * 
	 * @param kitReturnDtoList
	 * @return Response
	 */
	@RequestMapping(value = "/updateKitReturn", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateKitReturn(@RequestBody KitReturnDto kitReturnDto) {
		try {
			return iKitReturnService.updateKitReturn(kitReturnDto);
		} catch (Exception e) {
			logger.error("updateKitReturn failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * This method is used to update status for kit return.
	 * 
	 * @param kitReturnDtoList
	 * @return Response
	 */
	@RequestMapping(value = "/updateKitReturnStatus", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateKitReturnStatus(@RequestBody List<KitReturnDto> kitReturnDtoList) {
		try {
			return iKitReturnService.updateKitReturnStatus(kitReturnDtoList);
		} catch (Exception e) {
			logger.error("updateKitReturnStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * This method is used to cancel kit return.
	 * 
	 * @param kitReturnDto
	 * @return Response
	 */
	@RequestMapping(value = "/cancelKitReturn", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelKitReturn(@RequestBody KitReturnDto kitReturnDto) {
		try {
			return iKitReturnService.cancelKitReturn(kitReturnDto);
		} catch (Exception e) {
			logger.error("cancelKitReturn failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	/*-----------------------------------------------------------------------------------------------------------------------------------------------------------------*/

	/**
	 * This method is used to get kit return list.
	 * 
	 * @return Response
	 */
	@RequestMapping(value = "/getKitReturnList", method = RequestMethod.GET)
	public @ResponseBody Response getKitReturnList() {
		try {
			return iKitReturnService.getKitReturnList();
		} catch (Exception e) {
			logger.error("getKitReturnList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * This method is used to get the list of kits in a kit return by return number.
	 * 
	 * @param ReturnNo
	 * @return
	 */
	@RequestMapping(value = "/getKitListByReturnNo", method = RequestMethod.GET)
	public @ResponseBody Response getKitListByReturnNo(@RequestParam("ReturnNo") Integer ReturnNo) {
		try {
			return iKitReturnService.getKitListByReturnNo(ReturnNo);
		} catch (Exception e) {
			logger.error("getKitListByReturnNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
