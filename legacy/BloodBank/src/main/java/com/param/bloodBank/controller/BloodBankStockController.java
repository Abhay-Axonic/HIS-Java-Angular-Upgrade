package com.param.bloodBank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.BloodBagLoanInDto;
import com.param.bloodBank.dto.BloodBankStockDto;
import com.param.bloodBank.service.IBloodBankStockService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/bloodbank/stock")
@SuppressWarnings({"rawtypes","unchecked"})
public class BloodBankStockController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBankStockController.class);


	@Autowired
	private IBloodBankStockService iBloodBankStockService;
	
	@RequestMapping(value = "/saveBloodBankStock", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveBloodBankStock(@RequestBody List<BloodBankStockDto> list) {
		try {
			return iBloodBankStockService.saveBloodBankStock(list);

		} catch (Exception e) {
			logger.error("saveBloodBankStock failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*@RequestMapping(value = "/saveBloodBankStock", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSample(@RequestBody String sampleMasterDto) {
		
		
		Response<BloodBankStockDto, Integer> response = null;
		try {
			
			JsonCheck.mapJsonToObject(sampleMasterDto, BloodBankStockDto.class);
			
			//  response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
			 // response;
			 
			return null;
		} catch (Exception e) {
			
			return new Response(ERROR, ERROR_CODE, null, null, null);
		}
	}*/

	@RequestMapping(value = "/searchLoanInData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchLoanInData(@RequestBody BloodBagLoanInDto dto) {
		try {
			return iBloodBankStockService.searchLoanInData(dto);

		} catch (Exception e) {
			logger.error("searchLoanInData failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/searchBloodBankStock", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchBloodBankStock(@RequestBody BloodBankStockDto dto) {
		try {
			return iBloodBankStockService.searchBloodBankStock(dto);

		} catch (Exception e) {
			logger.error("searchBloodBankStock failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateBloodBankStockStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateBloodBankStockStatus(@RequestBody BloodBankStockDto dto) {
		try {
			return iBloodBankStockService.updateBloodBankStockStatus(dto);

		} catch (Exception e) {
			logger.error("updateBloodBankStockStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateLoanInStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateLoanInStatus(@RequestBody List<BloodBankStockDto> dto) {
		try {
			for (BloodBankStockDto  bloodBankStockDto: dto) {
					 iBloodBankStockService.updateLoanInStatus(bloodBankStockDto);
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
			logger.error("updateLoanInStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/searchLoanInDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchLoanInDetails(@RequestBody BloodBankStockDto dto) {
		try {
			return iBloodBankStockService.searchLoanInDetails(dto);

		} catch (Exception e) {
			logger.error("searchLoanInDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getAvailableBloodGroupByBankID", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAvailableBloodGroupByBankID(@RequestBody BloodBankStockDto dto) {
		try {
			return iBloodBankStockService.getAvailableBloodGroupByBankID(dto);

		} catch (Exception e) {
			logger.error("getAvailableBloodGroupByBankID failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBagNoListByComponentAndGroupId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBagNoListByComponentAndGroupId(@RequestBody BloodBankStockDto dto) {
		try {
			return iBloodBankStockService.getBagNoListByComponentAndGroupId(dto);

		} catch (Exception e) {
			logger.error("getBagNoListByComponentAndGroupId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getLoanInDataForApproval", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getLoanInDataForApproval(@RequestBody BloodBagLoanInDto dto) {
		try {
			return iBloodBankStockService.getLoanInDataForApproval(dto);

		} catch (Exception e) {
			logger.error("getLoanInDataForApproval failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/updateDonorBloodInStock", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDonorBloodInStock(@RequestBody List<BloodBankStockDto> list) {
		try {
			
			for (BloodBankStockDto bloodBankStockDto : list) {
				 
				 iBloodBankStockService.updateDonorBloodInStock(bloodBankStockDto);
				 
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		} catch (Exception e) {
			logger.error("updateDonorBloodInStock failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/searchBagNoListByMultipleCriteria", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchBagNoListByMultipleCriteria(@RequestBody BloodBankStockDto dto) {
		try {
			return iBloodBankStockService.searchBagNoListByMultipleCriteria(dto);

		} catch (Exception e) {
			logger.error("searchBagNoListByMultipleCriteria failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
}
