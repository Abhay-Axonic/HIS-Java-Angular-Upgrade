package com.param.bloodBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dao.DonorScreeningMasterDto;
import com.param.bloodBank.dto.CampMasterDto;
import com.param.bloodBank.service.IDonorScreeningMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/global/bloodBank")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DonorScreeningMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(DonorScreeningMasterController.class);


	@Autowired
	private IDonorScreeningMasterService iDonorScreeningMasterService;
	
	@RequestMapping(value = "/saveDonorScreenigMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveDonorScreenigMaster(@RequestBody DonorScreeningMasterDto donorScreeningMasterDto) {
		try {
			return iDonorScreeningMasterService.saveDonorScreenigMaster(donorScreeningMasterDto);
		} catch (Exception e) {
			logger.error("saveDonorScreenigMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getAllDonorScreeningList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAllDonorScreeningList(@RequestBody DonorScreeningMasterDto donorScreeningMasterDto) {
		try {
			return iDonorScreeningMasterService.getAllDonorScreeningList(donorScreeningMasterDto);
		} catch (Exception e) {
			logger.error("getAllDonorScreeningList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateDonorScreeningMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDonorScreeningMaster(@RequestBody DonorScreeningMasterDto donorScreeningMasterDto) {
		try {
			return iDonorScreeningMasterService.updateDonorScreeningMaster(donorScreeningMasterDto);
		} catch (Exception e) {
			logger.error("updateDonorScreeningMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateDonorScreeningMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDonorScreeningMasterStatus(@RequestBody DonorScreeningMasterDto donorScreeningMasterDto) {
		try {
			return iDonorScreeningMasterService.updateDonorScreeningMasterStatus(donorScreeningMasterDto);
		} catch (Exception e) {
			logger.error("updateDonorScreeningMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getDonorScreeningListByGenderId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDonorScreeningListByGenderId(@RequestBody DonorScreeningMasterDto donorScreeningMasterDto) {
		try {
			return iDonorScreeningMasterService.getDonorScreeningListByGenderId(donorScreeningMasterDto);
		} catch (Exception e) {
			logger.error("getDonorScreeningListByGenderId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getDefferedTypeMaster", method = RequestMethod.GET)
	public Response getDefferedTypeMaster() {
		try {
			return iDonorScreeningMasterService.getDefferedTypeMaster();
		} catch (Exception e) {
			logger.error("getDefferedTypeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getAllDonorScreeningListPaginationList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAllDonorScreeningListPaginationList(@RequestBody DonorScreeningMasterDto donorScreeningMasterDto) {

		try {
			return iDonorScreeningMasterService.getAllDonorScreeningListPaginationList(donorScreeningMasterDto);

		} catch (Exception e) {
			logger.error("getAllDonorScreeningListPaginationList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getAllDonorScreeningListCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAllDonorScreeningListCount(@RequestBody DonorScreeningMasterDto donorScreeningMasterDto) {
		
		try {
			return iDonorScreeningMasterService.getAllDonorScreeningListCount(donorScreeningMasterDto);
			
		} catch (Exception e) {
			logger.error("getAllDonorScreeningListCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
