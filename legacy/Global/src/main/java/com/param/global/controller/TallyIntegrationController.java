package com.param.global.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.ITallyIntegrationService;
import com.param.global.dto.TallyIntegrationDto;


@RestController
@RequestMapping("api/global")
@SuppressWarnings({ "rawtypes", "unchecked","unused" })

public class TallyIntegrationController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(TallyIntegrationController.class);

	@Autowired
	private ITallyIntegrationService iTallyIntegrationService;

	@RequestMapping(value="/getIpLedger", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE  )
	public @ResponseBody Response getIpLedger(@RequestBody TallyIntegrationDto tallyIntegrationDto) {
		try {
				return iTallyIntegrationService.getIpLedger(tallyIntegrationDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getOpLedger", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE  )
	public @ResponseBody Response getOpLedger(@RequestBody TallyIntegrationDto tallyIntegrationDto) {
		try {
				return iTallyIntegrationService.getOpLedger(tallyIntegrationDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value="/getInventoryLedger", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE  )
	public @ResponseBody Response getInvetoryLedger(@RequestBody TallyIntegrationDto tallyIntegrationDto) {
		try {
				return iTallyIntegrationService.getInvetoryLedger(tallyIntegrationDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	
	@RequestMapping(value="/getDoctorShareLedger", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE  )
	public @ResponseBody Response getDoctorShareLedger(@RequestBody TallyIntegrationDto tallyIntegrationDto) {
		try {
				return iTallyIntegrationService.getDoctorShareLedger(tallyIntegrationDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/sendTallyData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE  )
	public @ResponseBody Response sendIPLedger(@RequestBody List<TallyIntegrationDto> tallyIntegrationDto) {
		try {
				return iTallyIntegrationService.sendTallyData(tallyIntegrationDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/exportTallyData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE  )
	public @ResponseBody Response exportTallyData(@RequestBody List<TallyIntegrationDto> tallyIntegrationDto) {
		try {
				return iTallyIntegrationService.exportTallyData(tallyIntegrationDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getAllLedgersNameFromHis", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE  )
	public @ResponseBody Response getAllLedgersNameFromHis(@RequestBody TallyIntegrationDto tallyIntegrationDto) {
		try {
				return iTallyIntegrationService.getAllLedgersNameFromHis(tallyIntegrationDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/sendLedgerNameRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE  )
	public @ResponseBody Response sendLedgerNameRequest(@RequestBody TallyIntegrationDto tallyIntegrationDto) {
		try {
				return iTallyIntegrationService.sendLedgerNameRequest(tallyIntegrationDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/verifyLedgerNames", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE  )
	public @ResponseBody Response verifyLedgerNames(@RequestBody TallyIntegrationDto tallyIntegrationDto) {
		try {
				return iTallyIntegrationService.verifyLedgerNames(tallyIntegrationDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/sendCreateLedgerRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE  )
	public @ResponseBody Response sendCreateLedgerRequest(@RequestBody TallyIntegrationDto tallyIntegrationDto) {
		try {
				return iTallyIntegrationService.sendCreateLedgerRequest(tallyIntegrationDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/refreshTallyLedgerNameList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE  )
	public @ResponseBody Response refreshTallyLedgerNameList(@RequestBody TallyIntegrationDto tallyIntegrationDto) {
		try {
				return iTallyIntegrationService.refreshTallyLedgerNameList(tallyIntegrationDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getTallyInterfaceCommunicationKeys", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE  )
	public @ResponseBody Response getTallyInterfaceCommunicationKeys(@RequestBody TallyIntegrationDto tallyIntegrationDto) {
		try {
				return iTallyIntegrationService.getTallyInterfaceCommunicationKeys(tallyIntegrationDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/sendRequestToTally", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE  )
	public @ResponseBody Response sendRequestToTally(@RequestBody TallyIntegrationDto tallyIntegrationDto) {
		try {
			return iTallyIntegrationService.sendRequestToTally(tallyIntegrationDto);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
}
