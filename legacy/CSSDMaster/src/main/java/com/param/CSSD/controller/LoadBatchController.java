package com.param.CSSD.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.CSSD.dto.LoadBatchDto;
import com.param.CSSD.service.ILoadBatchService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping("/cssd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class LoadBatchController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(LoadBatchController.class);


	@Autowired
	private ILoadBatchService iLoadBatchService;

	
	
	@RequestMapping(value = "/searchKitsToLoadBatch", method = RequestMethod.GET)
	public  Response searchKitsToLoadBatch(@RequestParam("organizationId")Integer organizationId,
			@RequestParam("unitId")Integer unitId,@RequestParam("storeId")Integer storeId,
			@RequestParam("name")String name
			) {
		try {
			return iLoadBatchService.searchKitsToLoadBatch(organizationId,unitId,storeId,name);
		} catch (Exception e) {
			logger.error("searchKitsToLoadBatch failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getRecentBioIndiDate", method = RequestMethod.GET)
	public  Response getRecentBioIndiDate(@RequestParam("organizationId")Integer organizationId,
			@RequestParam("unitId")Integer unitId,@RequestParam("storeId")Integer storeId,
			@RequestParam("autoclaveId")Integer autoclaveId
			) {
		try {
			return iLoadBatchService.getRecentBioIndiDate(organizationId,unitId,storeId,autoclaveId);
		} catch (Exception e) {
			logger.error("getRecentBioIndiDate failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/loadBatch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response loadBatch(@RequestBody LoadBatchDto loadBatchDto) {
		try {
			return iLoadBatchService.loadBatch(loadBatchDto);
		} catch (Exception e) {
			logger.error("loadBatch failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getLoadBatchListByMultipleFields", method = RequestMethod.GET)
	public @ResponseBody Response getLoadBatchListByMultipleFields(@RequestParam("organizationId") Integer organizationId,
			@RequestParam("unitId") Integer unitId,@RequestParam("storeId")Integer storeId,
			@RequestParam("from") @DateTimeFormat(pattern="dd-MM-yyyy") Date from,
			@RequestParam("to") @DateTimeFormat(pattern="dd-MM-yyyy") Date to,
			@RequestParam(value="biologicalIndicator",required=false ) Boolean biologicalIndicator,
			@RequestParam(value="batchNo",required=false) Integer batchNo,@RequestParam(value="status",required=false) String status) {
		try {
			return iLoadBatchService.getLoadBatchListByMultipleFields(organizationId,unitId,storeId,from,to,batchNo,biologicalIndicator,status);
		} catch (Exception e) {
			logger.error("getLoadBatchListByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/savePendingIndicators", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response savePendingIndicators(@RequestBody LoadBatchDto loadBatchDto) {
		try {
			return iLoadBatchService.savePendingIndicators(loadBatchDto);
		} catch (Exception e) {
			logger.error("savePendingIndicators failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBatchDetailsByBatchNumber", method = RequestMethod.GET)
	public @ResponseBody Response getBatchDetailsByBatchNumber(@RequestParam("organizationId")Integer organizationId,
			@RequestParam("unitId")Integer unitId,
			@RequestParam("storeId")Integer storeId,
			@RequestParam("batchNo")Integer batchNo) {
		try {
			return iLoadBatchService.getBatchDetailsByBatchNumber(organizationId,unitId,storeId,batchNo);
		} catch (Exception e) {
			logger.error("getBatchDetailsByBatchNumber failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getLoadBatchListToSendToMicrobiologyByMultipleFields", method = RequestMethod.GET)
	public @ResponseBody Response getLoadBatchListToSendToMicrobiologyByMultipleFields(@RequestParam("organizationId") Integer organizationId,
			@RequestParam("unitId") Integer unitId,@RequestParam("storeId")Integer storeId,
			@RequestParam("from") @DateTimeFormat(pattern="dd-MM-yyyy") Date from,
			@RequestParam("to") @DateTimeFormat(pattern="dd-MM-yyyy") Date to,
			@RequestParam(value="batchNo",required=false) Integer batchNo,@RequestParam(value="status",required=false) String status) {
		try {
			return iLoadBatchService.getLoadBatchListToSendToMicrobiologyByMultipleFields(organizationId,unitId,storeId,from,to,batchNo,status);
		} catch (Exception e) {
			logger.error("getLoadBatchListToSendToMicrobiologyByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to send batch to the Microbiology.
	 * @param loadBatchDto
	 * @return Response
	 */
	@RequestMapping(value = "/sendToMicrobiology", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response sendToMicrobiology(@RequestBody LoadBatchDto loadBatchDto) {
		try {
			return iLoadBatchService.sendToMicrobiology(loadBatchDto);
		} catch (Exception e) {
			logger.error("sendToMicrobiology failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * This method is used to load a batch into machine.
	 * @param loadBatchDto
	 * @return Response
	 */
	/*@RequestMapping(value = "/loadBatch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response loadBatch(@RequestBody LoadBatchDto loadBatchDto) {
		try {
			return iLoadBatchService.loadBatch(loadBatchDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}*/
	
	/**
	 * This method is used to get loaded batch list
	 * @return Response
	 */
	@RequestMapping(value = "/getLoadBatchList", method = RequestMethod.GET)
	public @ResponseBody Response getLoadBatchList() {
		try {
			return iLoadBatchService.getLoadBatchList();
		} catch (Exception e) {
			logger.error("getLoadBatchList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to get kit list by batch number.
	 * @param batchNo
	 * @return Response
	 */
	@RequestMapping(value = "/getKitListByBatchNumber", method = RequestMethod.GET)
	public @ResponseBody Response getKitListByBatchNumber(@RequestParam("batchNo")Integer batchNo) {
		try {
			return iLoadBatchService.getKitListByBatchNumber(batchNo);
		} catch (Exception e) {
			logger.error("getKitListByBatchNumber failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to get loaded batch list by using multiple filters.
	 * @param from
	 * @param to
	 * @param batchNo
	 * @param status
	 * @return Response
	 */
	/*@RequestMapping(value = "/getLoadBatchListByMultipleFields", method = RequestMethod.GET)
	public @ResponseBody Response getLoadBatchListByMultipleFields(@RequestParam("from") @DateTimeFormat(pattern="dd-MM-yyyy") Date from,
			@RequestParam("to") @DateTimeFormat(pattern="dd-MM-yyyy") Date to,
			@RequestParam("batchNo") Integer batchNo,@RequestParam("status") String status) {
		try {
			return iLoadBatchService.getLoadBatchListByMultipleFields(from,to,batchNo,status);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}*/
	
	/**
	 * This method is used to get the list of batch numbers for Control indicator.
	 * @return Response
	 */
	@RequestMapping(value = "/getBatchNoListForControl", method = RequestMethod.GET)
	public @ResponseBody Response getBatchNoListForControl() {
		try {
			return iLoadBatchService.getBatchNoListForControl();
		} catch (Exception e) {
			logger.error("getBatchNoListForControl failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method is used to get the list of batch numbers for Test indicator.
	 * @return Response
	 */
	@RequestMapping(value = "/getBatchNoListForTest", method = RequestMethod.GET)
	public @ResponseBody Response getBatchNoListForTest() {
		try {
			return iLoadBatchService.getBatchNoListForTest();
		} catch (Exception e) {
			logger.error("getBatchNoListForTest failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * This method is used to get batch list for Application Screen.
	 * @param batchNo
	 * @param fromDate
	 * @param toDate
	 * @param autoclaveNo
	 * @param status
	 * @return Response
	 */
	@RequestMapping(value = "/getBatchListForApplication", method = RequestMethod.GET)
	public @ResponseBody Response getBatchListForApplication(@RequestParam("batchNo") Integer batchNo,
			@RequestParam("fromDate") @DateTimeFormat(pattern="dd-MM-yyyy")Date fromDate,
			@RequestParam("toDate") @DateTimeFormat(pattern="dd-MM-yyyy")Date toDate,
			@RequestParam("autoclaveNo") Integer autoclaveNo,@RequestParam("status") String status
			) {
		try {
			return iLoadBatchService.getBatchListForApplication(batchNo,fromDate,toDate,autoclaveNo,status);
		} catch (Exception e) {
			logger.error("getBatchListForApplication failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
}
