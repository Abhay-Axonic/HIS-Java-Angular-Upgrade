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

import com.param.global.billing.dto.BankBranchMasterDto;
import com.param.global.billing.service.IBankBranchMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping("api/billing/ ")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BankBranchMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(BankBranchMasterController.class);

	@Autowired
	private IBankBranchMasterService iBankBranchMasterService;

	
	@RequestMapping(value = "/saveBankBranchMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveBankBranchMaster(
			@RequestBody BankBranchMasterDto bankBranchMasterDto) {
		try {
			return iBankBranchMasterService.saveBankBranchMaster(bankBranchMasterDto);
		} catch (Exception e) {
			logger.error("saveBankBranchMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getBankBranchMasterById/{branchId}/{orgnisationId}", method = RequestMethod.GET)
	public @ResponseBody Response getBankBranchMasterById(@PathVariable("branchId") int branchId,
			@PathVariable("orgnisationId") int orgnisationId) {
		try {
			return iBankBranchMasterService.getBankBranchMasterById(branchId, orgnisationId);
		} catch (Exception e) {
			logger.error("getBankBranchMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getBankBranchMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getBankBranchMasterList(@RequestBody BankBranchMasterDto bankBranchMasterDto) {
		try {
			return iBankBranchMasterService.getBankBranchMasterList(bankBranchMasterDto);
		} catch (Exception e) {
			logger.error("getBankBranchMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/updateBankBranchMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateBankBranchMaster(
			@RequestBody BankBranchMasterDto bankBranchMasterDto) {
		try {
			return iBankBranchMasterService.updateBankBranchMaster(bankBranchMasterDto);
		} catch (Exception e) {
			logger.error("updateBankBranchMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/updateBankBranchMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateBankBranchMasterStatus(
			@RequestBody BankBranchMasterDto bankBranchMasterDto) {
		try {
			return iBankBranchMasterService.updateBankBranchMasterStatus(bankBranchMasterDto);
		} catch (Exception e) {
			logger.error("updateBankBranchMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/getBankBranchMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBankBranchMasterCount(
			@RequestBody BankBranchMasterDto bankBranchMasterDto) {
		try {
			return iBankBranchMasterService.getBankBranchMasterCount(bankBranchMasterDto);
		} catch (Exception e) {
			logger.error("getBankBranchMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getActiveBankBranchMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveBankBranchMasterList(@RequestBody BankBranchMasterDto bankBranchMasterDto) {
		try {
			return iBankBranchMasterService.getActiveBankBranchMasterList(bankBranchMasterDto);
		} catch (Exception e) {
			logger.error("getActiveBankBranchMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
