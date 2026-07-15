package com.param.bloodBank.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.param.bloodBank.dto.DonorRegistrationDto;
import com.param.bloodBank.service.IDonorRegistrationService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.SlotMasterDto;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="api/bloodbank")
@SuppressWarnings({ "unchecked", "rawtypes"})
public class DonorRegistrationController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(DonorRegistrationController.class);

	
	@Autowired
	private IDonorRegistrationService iDonorRegistrationService;
	
	
	@RequestMapping(value="/donorRegistration", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody Response saveDonorRegistration(@RequestBody DonorRegistrationDto patientRegistrationDto) {
		try {
			
			Response res1 = iDonorRegistrationService.registerDonor(patientRegistrationDto);
			return res1;
		}catch (Exception e) {
			logger.error("saveDonorRegistration failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*@RequestMapping(value = "/donorRegistration", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSample(@RequestBody String patientRegistrationDto) {
		Response response = null;
		try {
			JsonCheck.mapJsonToObject(patientRegistrationDto, DonorRegistrationDto.class);
			*//**
			 * response = iSampleMasterService.addSampleMaster(sampleMasterDto); return*
			 * response;
			 *//*
			return null;
		} catch (Exception e) {
			
			return new Response(ERROR, "error", "error", null, null);
		}
	}*/
	
	@RequestMapping(value="/updateDonorRegistration", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody Response updateDonorRegistration(@RequestBody DonorRegistrationDto patientRegistrationDto) {
		try 
		{
			return iDonorRegistrationService.updateDonorRegistration(patientRegistrationDto);
			
		}catch (Exception e) {
			logger.error("updateDonorRegistration failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/getDonorDetails",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDonorDetails(@RequestBody DonorRegistrationDto patientRegistrationDto) {
		try {
			return iDonorRegistrationService.getDonorDetails(patientRegistrationDto);
		}catch (Exception e) {
			logger.error("getDonorDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/donorSearch",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response donorSearch(@RequestBody DonorRegistrationDto donorRegistrationDto) {
		try {
			return iDonorRegistrationService.donorSearch(donorRegistrationDto);
		}catch (Exception e) {
			logger.error("donorSearch failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	 @RequestMapping(value = "/patient/image/{orgId}/{unitId}/{donorId}", headers=("content-type=multipart/*"), method = RequestMethod.POST)
     public @ResponseBody Response savePatientImage(@PathVariable("orgId") int orgId,
                           @PathVariable("unitId") int unitId,
                           @PathVariable("donorId") int donorId,
                           @RequestParam("file") MultipartFile inputFile)
         {
	      try
	      {
	         return iDonorRegistrationService.savePatientImage(orgId,unitId,donorId,inputFile);
	       }catch(Exception e)
	      {
	      	logger.error("savePatientImage failed", e);
	      	throw new ServiceException(e.getMessage());
	      }
       }
	
	 
	 @RequestMapping(value="/patientAutofillSearch", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response patientAutofillSearch(@RequestBody SlotMasterDto slotMasterDto) {
			try {
					return iDonorRegistrationService.patientAutofillSearch(slotMasterDto);
				
			}catch (Exception e) {
				logger.error("patientAutofillSearch failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
}
