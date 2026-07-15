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

import com.param.global.billing.dto.DoctorCategoryMasterDto;
import com.param.global.billing.service.IDoctorCategoryMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping("/billing")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DoctorCategoryMasterController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(DoctorCategoryMasterController.class);

	@Autowired
	IDoctorCategoryMasterService iDoctorCategoryMasterService;

	@RequestMapping(value = "/saveDoctorCategoryMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveDoctorCategoryMaster(@RequestBody DoctorCategoryMasterDto doctorCategoryMasterDto) {
		try {
			return iDoctorCategoryMasterService.saveDoctorCategoryMaster(doctorCategoryMasterDto);
		} catch (Exception e) {
			logger.error("saveDoctorCategoryMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getDoctorCategoryMasterById/{doctorId}/{orgId}", method = RequestMethod.GET)
	public @ResponseBody Response getDoctorCategoryMasterById(@PathVariable("doctorId") int doctorId,@PathVariable("orgId") int orgId) {
		try {
			return iDoctorCategoryMasterService.getDoctorCategoryMasterById(doctorId,orgId);
		} catch (Exception e) {
			logger.error("getDoctorCategoryMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/getDoctorCategoryMasterList", method = RequestMethod.POST , consumes= MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorCategoryMasterList(@RequestBody DoctorCategoryMasterDto doctorCategoryMasterDto) {
		try {
			return iDoctorCategoryMasterService.getDoctorCategoryMasterList(doctorCategoryMasterDto);
		} catch (Exception e) {
			logger.error("getDoctorCategoryMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/updateDoctorCategoryMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateDoctorCategoryMaster(@RequestBody DoctorCategoryMasterDto doctorCategoryMasterDto) {
		try {
			return iDoctorCategoryMasterService.updateDoctorCategoryMaster(doctorCategoryMasterDto);
		} catch (Exception e) {
			logger.error("updateDoctorCategoryMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/updateDoctorCategoryMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateDoctorCategoryMasterStatus(@RequestBody DoctorCategoryMasterDto doctorCategoryMasterDto) {
		try {
			return iDoctorCategoryMasterService.updateDoctorCategoryMasterStatus(doctorCategoryMasterDto);
		} catch (Exception e) {
			logger.error("updateDoctorCategoryMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/getDoctorCategoryCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorCategoryCount(@RequestBody DoctorCategoryMasterDto doctorCategoryMasterDto){
		try{
			
			return iDoctorCategoryMasterService.getDoctorCategoryCount(doctorCategoryMasterDto);
		}catch(Exception e){
			logger.error("getDoctorCategoryCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/getActiveDoctorCategoryMasterList/{orgId}", method = RequestMethod.GET)
	public @ResponseBody Response getActiveDoctorCategoryMasterList(@PathVariable("orgId") int orgId) {
		try {
			return iDoctorCategoryMasterService.getActiveDoctorCategoryMasterList(orgId);
		} catch (Exception e) {
			logger.error("getActiveDoctorCategoryMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
