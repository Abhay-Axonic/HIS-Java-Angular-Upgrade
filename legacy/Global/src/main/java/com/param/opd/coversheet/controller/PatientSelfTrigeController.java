package com.param.opd.coversheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.PatientSelfTrigeDto;
import com.param.opd.coversheet.dto.StPatientAllergyDto;
import com.param.opd.coversheet.service.IPatientSelfTrigeService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("api")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PatientSelfTrigeController  implements ICommonConstants {
	final static Logger logger = Logger.getLogger(PatientSelfTrigeController.class);

	@Autowired
	private IPatientSelfTrigeService iPatientSelfTrigeService;
	
	@RequestMapping(value="/patient/selftrige/{uuid}/{tabName}", method=RequestMethod.GET)
	public @ResponseBody Response getListOfPatientSelfTrige(@PathVariable("uuid") String uuid, @PathVariable("tabName") String tabName) {
		try
		{
			PatientSelfTrigeDto patientSelfTrigeDto= new PatientSelfTrigeDto();
			patientSelfTrigeDto.setUuid(uuid);
			patientSelfTrigeDto.setTabName(tabName);
			return iPatientSelfTrigeService.getListOfPatientSelfTrige(patientSelfTrigeDto);
		}catch(Exception e)
		{
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
}
