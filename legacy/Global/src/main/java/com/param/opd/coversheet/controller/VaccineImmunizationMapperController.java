package com.param.opd.coversheet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.VaccineImmunizationMapperDto;
import com.param.opd.coversheet.service.IVaccineImmunizationMapperService;

import org.apache.log4j.Logger;

import com.param.global.common.ServiceException;

@RestController
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes" })
public class VaccineImmunizationMapperController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(VaccineImmunizationMapperController.class);

	@Autowired
	private IVaccineImmunizationMapperService ivaccineImmunizationMapperService;

	@RequestMapping(value = "/saveVaccineImmunizationMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveVaccineImmunizationMapper(@RequestBody List<VaccineImmunizationMapperDto> vaccineImmunizationMapperDto){
		
		try {
			Response res = ivaccineImmunizationMapperService.saveVaccineImmunizationMapper(vaccineImmunizationMapperDto);
//			if(res.getObject() != null) {
//				VaccineImmunizationMapperDto dto=new VaccineImmunizationMapperDto();
//				dto=(VaccineImmunizationMapperDto)res.getObject();
//				return ivaccineImmunizationMapperService.updateVaccineImmunizationMapper(dto);
//			}else {
				return res;
//			}
			
		} catch (Exception e) {
			logger.error("saveVaccineImmunizationMapper failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}



	@RequestMapping(value = "/getListOfVaccineImmunizationMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getListOfVaccineImmunizationMapper(@RequestBody VaccineImmunizationMapperDto vaccineImmunizationMapperDto){
		
		try {
			return ivaccineImmunizationMapperService.getListOfVaccineImmunizationMapper(vaccineImmunizationMapperDto);

		} catch (Exception e) {
			logger.error("getListOfVaccineImmunizationMapper failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateVaccineImmunizationMapperForNewCov", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateVaccineImmunizationMapperForNewCov(@RequestBody VaccineImmunizationMapperDto vaccineImmunizationMapperDto){
		
		try {
			return ivaccineImmunizationMapperService.updateVaccineImmunizationMapperForNewCov(vaccineImmunizationMapperDto);

		} catch (Exception e) {
			logger.error("updateVaccineImmunizationMapperForNewCov failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/removeFreeTextVaccineForNewCov", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response removeFreeTextVaccineForNewCov(@RequestBody VaccineImmunizationMapperDto vaccineImmunizationMapperDto){
		
		try {
			return ivaccineImmunizationMapperService.removeFreeTextVaccineForNewCov(vaccineImmunizationMapperDto);

		} catch (Exception e) {
			logger.error("removeFreeTextVaccineForNewCov failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

}
