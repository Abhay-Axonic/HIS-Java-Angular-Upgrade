package com.param.bloodBank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.CrossMatchRegisterDto;
import com.param.bloodBank.service.ICrossMatchRegisterService;
import com.param.bloodBank.service.IPatientRequisitionDetailsService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/bloodbank/crossMatch")
@SuppressWarnings({"rawtypes","unused"})
public class CrossMatchRegisterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(CrossMatchRegisterController.class);


	@Autowired
	private ICrossMatchRegisterService iCrossMatchRegisterService;
	
	@Autowired
	IPatientRequisitionDetailsService iPatientRequisitionDetailsService;
	
	@RequestMapping(value = "/saveCrossMatchRegister", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveCrossMatchRegister(@RequestBody CrossMatchRegisterDto crossMatchRegisterDto) {
		try {
			return iCrossMatchRegisterService.saveCrossMatchRegister(crossMatchRegisterDto);

		} catch (Exception e) {
			logger.error("saveCrossMatchRegister failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getCrossMatchRegisteredOnBagReservation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCrossMatchRegisteredOnBagReservation(@RequestBody CrossMatchRegisterDto dto) {
		try {
			return iCrossMatchRegisterService.getCrossMatchRegisteredOnBagReservation(dto);

		} catch (Exception e) {
			logger.error("getCrossMatchRegisteredOnBagReservation failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*@RequestMapping(value = "/saveCrossMatchRegister", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSample(@RequestBody String sampleMasterDto) {


	Response<CrossMatchRegisterDto, Integer> response = null;
	try {

	JsonCheck.mapJsonToObject(sampleMasterDto, CrossMatchRegisterDto.class);
	
	* response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
	* response;
	
	return null;
	} catch (Exception e) {
	
	return new Response(ERROR, null, null, null, null);
	}
	}*/
	
	@RequestMapping(value = "/updateCrossMatchRegisteredOnBagReservation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateCrossMatchRegisteredOnBagReservation(@RequestBody List<CrossMatchRegisterDto> dto) {
		try {
			Response res=  iCrossMatchRegisterService.updateCrossMatchRegisteredOnBagReservation(dto);
			if(res.getListObject().size()>0) {
					List<CrossMatchRegisterDto> list = dto;
					if(list.size()>0) {
						for (CrossMatchRegisterDto crossMatchRegisterDto : list) {
							Response res1 =   iPatientRequisitionDetailsService.updatePatientRequisitionDetails(crossMatchRegisterDto);
							Response res3=  iCrossMatchRegisterService.updateStock(crossMatchRegisterDto);
						}
					}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
			logger.error("updateCrossMatchRegisteredOnBagReservation failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getCrossMatchRegisteredOnIssue", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCrossMatchRegisteredOnIssue(@RequestBody CrossMatchRegisterDto dto) {
		try {
			return iCrossMatchRegisterService.getCrossMatchRegisteredOnIssue(dto);

		} catch (Exception e) {
			logger.error("getCrossMatchRegisteredOnIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getCrossMatchRegisterDetailsByCrossMatchID", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCrossMatchRegisterDetailsByCrossMatchID(@RequestBody CrossMatchRegisterDto dto) {
		try {
			return iCrossMatchRegisterService.getCrossMatchRegisterDetailsByCrossMatchID(dto);

		} catch (Exception e) {
			logger.error("getCrossMatchRegisterDetailsByCrossMatchID failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
