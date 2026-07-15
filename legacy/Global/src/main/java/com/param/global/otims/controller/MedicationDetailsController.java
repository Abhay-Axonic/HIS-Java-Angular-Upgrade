package com.param.global.otims.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.MedicationDetailsDto;
import com.param.global.otims.service.IMedicationDetailsService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/api/otims")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MedicationDetailsController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(MedicationDetailsController.class);


	@Autowired
	private IMedicationDetailsService iMedicationDetailsService;

	@RequestMapping(value = "/saveMedicationDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveMedicationDetails(@RequestBody  List<MedicationDetailsDto> iMedicationDetailsDto) {
		try {
			return iMedicationDetailsService.saveMedicationDetails(iMedicationDetailsDto);
		} catch (Exception e) {
		logger.error("saveMedicationDetails failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getMedicationDetailsByProcedureId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getMedicationDetailsByProcedureId(@RequestBody  MedicationDetailsDto iMedicationDetailsDto) {
		try {
			return iMedicationDetailsService.getMedicationDetailsByProcedureId(iMedicationDetailsDto);
		} catch (Exception e) {
		logger.error("getMedicationDetailsByProcedureId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getMedicationDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getMedicationDetails(@RequestBody  MedicationDetailsDto iMedicationDetailsDto) {
		try {
			return iMedicationDetailsService.getMedicationDetails(iMedicationDetailsDto);
		} catch (Exception e) {
		logger.error("getMedicationDetails failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateMedicationDetailsIsExported", method = RequestMethod.POST)
	public Response updateMedicationDetailsIsExported(@RequestBody List<MedicationDetailsDto> list){
	try {
		return iMedicationDetailsService.updateMedicationDetailsIsExported(list);

	} catch (Exception e) {
	logger.error("updateMedicationDetailsIsExported failed", e);
	}
	return new Response<>("error", "500", "error", null, null);
	}
	
	@RequestMapping(value = "/getMedicationDetailsByProcedureIdOnOTIMS", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getMedicationDetailsByProcedureIdOnOTIMS(@RequestBody  MedicationDetailsDto iMedicationDetailsDto) {
		try {
			return iMedicationDetailsService.getMedicationDetailsByProcedureIdOnOTIMS(iMedicationDetailsDto);
		} catch (Exception e) {
		logger.error("getMedicationDetailsByProcedureIdOnOTIMS failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

}
