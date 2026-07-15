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
import com.param.pharmacy.qms.dto.QMSDeptMasterDto;
import com.param.pharmacy.qms.service.IQMSDeptMasterService;

@RestController
@RequestMapping(value="/api/pharmacy")
@SuppressWarnings("rawtypes")
public class QMSDeptMasterController implements ICommonConstants{

	@Autowired
	IQMSDeptMasterService iQMSDeptMasterService;

	private static final Logger logger = Logger.getLogger(QMSDeptMasterController.class);

	@RequestMapping(value="/saveQMSDeptMaster",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveQMSDeptMaster(@RequestBody QMSDeptMasterDto dto){
		try{
			return iQMSDeptMasterService.saveQMSDeptMaster(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/updateQMSDeptMaster",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateQMSDeptMaster(@RequestBody QMSDeptMasterDto dto){
		try{
			return iQMSDeptMasterService.updateQMSDeptMaster(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/updateQMSDeptMasterStatus",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateQMSDeptMasterStatus(@RequestBody QMSDeptMasterDto dto){
		try{
			return iQMSDeptMasterService.updateQMSDeptMasterStatus(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value="/getQMSDeptMasterList",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getQMSDeptMasterList(@RequestBody QMSDeptMasterDto dto){
		try{
			return iQMSDeptMasterService.getQMSDeptMasterList(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/getQMSDeptMasterCount",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getQMSDeptMasterCount(@RequestBody QMSDeptMasterDto dto){
		try{
			return iQMSDeptMasterService.getQMSDeptMasterCount(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/getQMSDeptMasterById",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getQMSDeptMasterById(@RequestBody QMSDeptMasterDto dto){
		try{
			return iQMSDeptMasterService.getQMSDeptMasterById(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/getActiveQMSDeptMasterList",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveQMSDeptMasterList(@RequestBody QMSDeptMasterDto dto){
		try{
			return iQMSDeptMasterService.getActiveQMSDeptMasterList(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/getReceptionListByKioskId",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getReceptionListByKiosk(@RequestBody QMSDeptMasterDto dto){
		try{
			return iQMSDeptMasterService.getReceptionListByKiosk(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/getActiveDeptListByKioskId",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveDeptListByKioskId(@RequestBody QMSDeptMasterDto dto){
		try{
			return iQMSDeptMasterService.getActiveDeptListByKioskId(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
