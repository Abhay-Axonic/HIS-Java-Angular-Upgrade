package com.param.pharmacy.qms.controller;

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
import com.param.pharmacy.qms.dto.QMSKioskMasterDto;
import com.param.pharmacy.qms.service.IQMSKioskMasterService;
@RestController
@RequestMapping(value="/api/pharmacy/")
@SuppressWarnings("rawtypes")
public class QMSKioskMasterController implements ICommonConstants {
	
	@Autowired
	IQMSKioskMasterService iQMSKioskMasterService;
	
	private static final Logger logger = Logger.getLogger(QMSKioskMasterController.class);

	@RequestMapping(value="/saveQMSMaster",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveQMSMaster(@RequestBody QMSKioskMasterDto dto){
		try{
			return iQMSKioskMasterService.saveQMSMaster(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/updateQMSMaster",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateQMSMaster(@RequestBody QMSKioskMasterDto dto){
		try{
			return iQMSKioskMasterService.updateQMSMaster(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/updateQMSMasterStatus",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateQMSMasterStatus(@RequestBody QMSKioskMasterDto dto){
		try{
			return iQMSKioskMasterService.updateQMSMasterStatus(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/getQMSMasterList",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getQMSMasterList(@RequestBody QMSKioskMasterDto dto){
		try{
			return iQMSKioskMasterService.getQMSMasterList(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/getQMSMasterCount",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getQMSMasterCount(@RequestBody QMSKioskMasterDto dto){
		try{
			return iQMSKioskMasterService.getQMSMasterCount(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/getQMSMasterById",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getQMSMasterById(@RequestBody QMSKioskMasterDto dto){
		try{
			return iQMSKioskMasterService.getQMSMasterById(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/getActiveQMSMasterList",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveQMSMasterList(@RequestBody QMSKioskMasterDto dto){
		try{
			return iQMSKioskMasterService.getActiveQMSMasterList(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
