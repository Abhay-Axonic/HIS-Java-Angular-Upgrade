package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.PatientPOCMasterDTO;
import com.param.global.service.IPatientPOCMasterService;

@RestController
@RequestMapping("api/global")
public class PatientPOCMasterController implements ICommonConstants {

	@Autowired
	IPatientPOCMasterService iPatientPOCMasterService;

	@RequestMapping(value = "/savePocMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response savePOCMaster(@RequestBody PatientPOCMasterDTO patientPOCMasterDTO) throws Exception {

		try {
			return iPatientPOCMasterService.savePOCMaster(patientPOCMasterDTO);
		} catch (Exception e) {
			throw e;
		}

	}

	@RequestMapping(value = "/searchPocMaster/{patientSearch}", method = RequestMethod.GET)
	public Response searchPOCMaster(@PathVariable("patientSearch") String patientSearch) {

		try {
			return iPatientPOCMasterService.searchPOCMaster(patientSearch);
		} catch (Exception e) {
			throw e;
		}

	}

	@RequestMapping(value = "/updatePocMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updatePocMaster(@RequestBody PatientPOCMasterDTO patientPOCMasterDTO) {
		try {
			return iPatientPOCMasterService.updatePocMaster(patientPOCMasterDTO);
		} catch (Exception e) {
			throw e;
		}
	}

}
