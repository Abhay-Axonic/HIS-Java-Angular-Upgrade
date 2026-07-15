package com.param.global.procedure.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.procedure.dto.BillOfMaterialDto;
import com.param.global.procedure.service.IBillOfMaterialService;

@RestController
@RequestMapping(value = "/api/procedure/BillOfMaterial")
@SuppressWarnings({"rawtypes"})
public class BillOfMaterialController  implements ICommonConstants{

	final static Logger logger = Logger.getLogger(BillOfMaterialController.class);

	@Autowired
	private IBillOfMaterialService iBillOfMaterialService;
	
	
	@RequestMapping(value = "/saveBillOfMaterial", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveBillOfMaterial(@RequestBody BillOfMaterialDto billOfMaterialDto){
		
		try {
			return iBillOfMaterialService.saveBillOfMaterial(billOfMaterialDto);

		} catch (Exception e) {
			logger.error("saveBillOfMaterial failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getBillOfMaterialServices", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBillOfMaterialServices(@RequestBody BillOfMaterialDto billOfMaterialDto){
		
		try {
			return iBillOfMaterialService.getBillOfMaterialServices(billOfMaterialDto);

		} catch (Exception e) {
			logger.error("getBillOfMaterialServices failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getIsProcedure", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getIsProcedure(@RequestBody BillOfMaterialDto billOfMaterialDto){
		
		try {
			return iBillOfMaterialService.getIsProcedure(billOfMaterialDto);

		} catch (Exception e) {
			logger.error("getIsProcedure failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBillOfMaterialListByProcedureId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBillOfMaterialListByProcedureId(@RequestBody BillOfMaterialDto billOfMaterialDto){
		
		try {
			return iBillOfMaterialService.getBillOfMaterialListByProcedureId(billOfMaterialDto);

		} catch (Exception e) {
			logger.error("getBillOfMaterialListByProcedureId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getBillOfMaterialServiceAmount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBillOfMaterialServiceAmount(@RequestBody List<BillOfMaterialDto> billOfMaterialDto){
		
		try {
			return iBillOfMaterialService.getBillOfMaterialServiceAmount(billOfMaterialDto);

		} catch (Exception e) {
			logger.error("getBillOfMaterialServiceAmount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateReservationAgainst/admission/encounter", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateReservationAgainstAdmissionEncounter(@RequestBody OTReservationDto oTReservationDto){
		
		try {
			return iBillOfMaterialService.updateReservationAgainstAdmissionEncounter(oTReservationDto);

		} catch (Exception e) {
			logger.error("updateReservationAgainstAdmissionEncounter failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/reAssignOtReservationProcedure", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response reAssignOtReservationProcedureAdmissionEncounter(@RequestBody OTReservationDto oTReservationDto){
		
		try {
			return iBillOfMaterialService.reAssignOtReservationProcedureAdmissionEncounter(oTReservationDto);

		} catch (Exception e) {
			logger.error("reAssignOtReservationProcedureAdmissionEncounter failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	

	@RequestMapping(value = "/getCongServicesByProReqId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCongServicesByProReqId(@RequestBody OTReservationDto oTReservationDto){
		
		try {
			return iBillOfMaterialService.getCongServicesByProReqId(oTReservationDto);

		} catch (Exception e) {
			logger.error("getCongServicesByProReqId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBillOfMaterialListByProcedureList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBillOfMaterialListByProcedureList(@RequestBody BillOfMaterialDto billOfMaterialDto) throws Exception{
		
		try {
			return iBillOfMaterialService.getBillOfMaterialListByProcedureList(billOfMaterialDto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/updateStatusProcedure", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateStatusProcedure(@RequestBody BillOfMaterialDto billOfMaterialDto) throws Exception{
		
		try {
			return iBillOfMaterialService.updateStatusProcedure(billOfMaterialDto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/getBillOfMaterialListByProcedureIdAll", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBillOfMaterialListByProcedureIdAll(@RequestBody BillOfMaterialDto billOfMaterialDto){
		
		try {
			return iBillOfMaterialService.getBillOfMaterialListByProcedureIdAll(billOfMaterialDto);

		} catch (Exception e) {
			logger.error("getBillOfMaterialListByProcedureIdAll failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
