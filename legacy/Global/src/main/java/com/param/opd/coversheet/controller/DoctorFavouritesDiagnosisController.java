package com.param.opd.coversheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.DoctorFavouritesDiagnosisDto;
import com.param.opd.coversheet.dto.DoctorFavouritesDiagnosisDto;
import com.param.opd.coversheet.service.IDoctorFavouritesDiagnosisService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@SuppressWarnings({"rawtypes", "unchecked"})
@RequestMapping("api/")
public class DoctorFavouritesDiagnosisController implements ICommonConstants,IError{
	final static Logger logger = Logger.getLogger(DoctorFavouritesDiagnosisController.class);

	@Autowired
	IDoctorFavouritesDiagnosisService iDoctorFavouritesDiagnosisService;

	@RequestMapping(value="/saveDoctorFavouriteDiagnosis", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response savePatientDiagnosis(@RequestBody DoctorFavouritesDiagnosisDto doctorFavouritesDiagnosisDto) {
		try {

			return iDoctorFavouritesDiagnosisService.saveDoctorFavouriteDiagnosis(doctorFavouritesDiagnosisDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value="/getDoctorFavouriteDiagnosisListByDocId", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorFavouriteDiagnosisListByDocId(@RequestBody DoctorFavouritesDiagnosisDto doctorFavouritesDiagnosisDto) {
		try{
			return iDoctorFavouritesDiagnosisService.getDoctorFavouriteDiagnosisListByDocId(doctorFavouritesDiagnosisDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
