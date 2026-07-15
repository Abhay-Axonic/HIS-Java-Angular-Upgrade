package com.param.global.billing.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.billing.dto.CreditNoteReasonMasterDto;
import com.param.global.billing.service.ICreditNoteReasonMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping("/global/billing ")
@SuppressWarnings({ "rawtypes"})

public class CreditNoteReasonMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(CreditNoteReasonMasterController.class);

	@Autowired
	private ICreditNoteReasonMasterService iCreditNoteReasonMasterService;

	
	@RequestMapping(value = "/saveCreditNoteReasonMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveCreditNoteReasonMaster(@RequestBody CreditNoteReasonMasterDto creditNoteReasonMasterDto) {
		try {
			return iCreditNoteReasonMasterService.saveCreditNoteReasonMaster(creditNoteReasonMasterDto);
		} catch (Exception e) {
			logger.error("saveCreditNoteReasonMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/updateCreditNoteReasonMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateCreditNoteReasonMaster(@RequestBody CreditNoteReasonMasterDto creditNoteReasonMasterDto) {
		try {
			return iCreditNoteReasonMasterService.updateCreditNoteReasonMaster(creditNoteReasonMasterDto);
		} catch (Exception e) {
			logger.error("updateCreditNoteReasonMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/updateCreditNoteReasonMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateCreditNoteReasonMasterStatus(@RequestBody CreditNoteReasonMasterDto creditNoteReasonMasterDto) {
		try {
			return iCreditNoteReasonMasterService.updateCreditNoteReasonMasterStatus(creditNoteReasonMasterDto);
		} catch (Exception e) {
			logger.error("updateCreditNoteReasonMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getCreditNoteReasonMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCreditNoteReasonMasterList(@RequestBody CreditNoteReasonMasterDto creditNoteReasonMasterDto) {
		try {
			return iCreditNoteReasonMasterService.getCreditNoteReasonMasterList(creditNoteReasonMasterDto);
		} catch (Exception e) {
			logger.error("getCreditNoteReasonMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getCreditNoteReasonMasterById/{creditNoteReasonId}/{organisationId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getCreditNoteReasonMasterById(@PathVariable ("creditNoteReasonId") Integer creditNoteReasonId, 
			                                                    @PathVariable ("organisationId") Integer organisationId,
			                                                    @PathVariable ("unitId") Integer unitId){
		try {
			return iCreditNoteReasonMasterService.getCreditNoteReasonMasterById(creditNoteReasonId, organisationId, unitId );
		} catch (Exception e) {
			logger.error("getCreditNoteReasonMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getCreditNoteReasonMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCreditNoteReasonMasterCount(@RequestBody CreditNoteReasonMasterDto creditNoteReasonMasterDto) {
		try {
			return iCreditNoteReasonMasterService.getCreditNoteReasonMasterCount(creditNoteReasonMasterDto);
		} catch (Exception e) {
			logger.error("getCreditNoteReasonMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getActiveCreditNoteReasonMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveCreditNoteReasonMasterList(@RequestBody CreditNoteReasonMasterDto creditNoteReasonMasterDto) {
		try {
			return iCreditNoteReasonMasterService.getActiveCreditNoteReasonMasterList(creditNoteReasonMasterDto);
		} catch (Exception e) {
			logger.error("getActiveCreditNoteReasonMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
