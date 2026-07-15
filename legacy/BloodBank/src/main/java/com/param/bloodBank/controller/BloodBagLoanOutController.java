package com.param.bloodBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.BloodBagLoanOutDto;
import com.param.bloodBank.service.IBloodBagLoanOutService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/bloodbag/loanOut")
@SuppressWarnings({"rawtypes"})
public class BloodBagLoanOutController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBagLoanOutController.class);


	
	@Autowired
	private IBloodBagLoanOutService iBloodBagLoanOutService;
	
	@RequestMapping(value = "/saveLoanOut", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveloanout(@RequestBody BloodBagLoanOutDto dto) {
		try {
			return iBloodBagLoanOutService.saveloanout(dto);

		} catch (Exception e) {
			logger.error("saveloanout failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getLoanOutList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getLoanOutList(@RequestBody BloodBagLoanOutDto dto) {
		try {
			return iBloodBagLoanOutService.getLoanOutList(dto);
		} catch (Exception e) {
			logger.error("getLoanOutList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getLoanOutDeatilsList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getLoanOutDeatilsList(@RequestBody BloodBagLoanOutDto dto) {
		try {
			return iBloodBagLoanOutService.getLoanOutDeatilsList(dto);
		} catch (Exception e) {
			logger.error("getLoanOutDeatilsList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/loanOutListApproval", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response loanOutListApproval(@RequestBody BloodBagLoanOutDto dto) {
		try {
			return iBloodBagLoanOutService.loanOutListApproval(dto);
		} catch (Exception e) {
			logger.error("loanOutListApproval failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
