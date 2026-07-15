package com.param.pharmacy.qms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.pharmacy.qms.dto.QMSCounterMasterDto;
import com.param.pharmacy.qms.service.IQMSCounterMasterService;

@RestController
@RequestMapping(value="/api/pharmacy")
@SuppressWarnings("rawtypes")
public class QMSCounterMasterController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(QMSCounterMasterController.class);

	@Autowired
	IQMSCounterMasterService iQMSCounterMasterService;

	@RequestMapping(value="/saveQMSCounterMaster",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveQMSCounterMaster(@RequestBody QMSCounterMasterDto dto){
		try{
			return iQMSCounterMasterService.saveQMSCounterMaster(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/updateQMSCounterMaster",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateQMSCounterMaster(@RequestBody QMSCounterMasterDto dto){
		try{
			return iQMSCounterMasterService.updateQMSCounterMaster(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/updateQMSCounterMasterStatus",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateQMSCounterMasterStatus(@RequestBody QMSCounterMasterDto dto){
		try{
			return iQMSCounterMasterService.updateQMSCounterMasterStatus(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value="/getQMSCounterMasterList",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getQMSCounterMasterList(@RequestBody QMSCounterMasterDto dto){
		try{
			return iQMSCounterMasterService.getQMSCounterMasterList(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/getQMSCounterMasterCount",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getQMSCounterMasterCount(@RequestBody QMSCounterMasterDto dto){
		try{
			return iQMSCounterMasterService.getQMSCounterMasterCount(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/getQMSCounterMasterById",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getQMSCounterMasterById(@RequestBody QMSCounterMasterDto dto){
		try{
			return iQMSCounterMasterService.getQMSCounterMasterById(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/getQMSCounterListByDeptId",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getQMSCounterListByDeptId(@RequestBody QMSCounterMasterDto dto){
		try{
			return iQMSCounterMasterService.getQMSCounterListByDeptId(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}
