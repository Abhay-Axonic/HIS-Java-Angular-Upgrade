package com.param.bloodBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.DonorScreeningDetailDto;
import com.param.bloodBank.service.IDonorScreeningDetailService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/bloodbank")
@SuppressWarnings({"rawtypes"})
public class DonorScreeningDetailController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(DonorScreeningDetailController.class);
 
	
	@Autowired
	private IDonorScreeningDetailService donorScreeningDetailService;
	
	@RequestMapping(value = "/saveDonorScreeningDetail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveDonorScreeningDetail(@RequestBody DonorScreeningDetailDto donorScreeningDetailDto) {
		try {
			return donorScreeningDetailService.saveDonorScreeningDetail(donorScreeningDetailDto);

		} catch (Exception e) {
			logger.error("saveDonorScreeningDetail failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateDonorScreeningDeferralStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDonorScreeningDeferralStatus(@RequestBody DonorScreeningDetailDto donorScreeningDetailDto) {
		try {
			return donorScreeningDetailService.updateDonorScreeningDeferralStatus(donorScreeningDetailDto);

		} catch (Exception e) {
			logger.error("updateDonorScreeningDeferralStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getDonorScreeningDetailByDonorId/{donorId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getDonorScreeningDetailByDonorId(@PathVariable ("donorId") Integer donorId,@PathVariable ("organizationId") Integer organizationId,
									@PathVariable ("unitId") Integer unitId){
		try {
			return donorScreeningDetailService.getDonorScreeningDetailByDonorId(donorId, organizationId, unitId);
			
		} catch (Exception e) {
			logger.error("getDonorScreeningDetailByDonorId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/searchDonor", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchDonor(@RequestBody DonorScreeningDetailDto donorScreeningDetailDto){
		try {
			return donorScreeningDetailService.searchDonor(donorScreeningDetailDto);
			
		} catch (Exception e) {
			logger.error("searchDonor failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getDonorRegisttrationAndScreeningDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDonorRegisttrationAndScreeningDetails(@RequestBody DonorScreeningDetailDto donorScreeningDetailDto){
		try {
			return donorScreeningDetailService.getDonorRegisttrationAndScreeningDetails(donorScreeningDetailDto);
			
		} catch (Exception e) {
			logger.error("getDonorRegisttrationAndScreeningDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	}
	

