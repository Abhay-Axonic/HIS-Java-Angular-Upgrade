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
import com.param.pharmacy.qms.dto.QMSTokenMasterDto;
import com.param.pharmacy.qms.service.IQMSTokenMasterService;

@RestController
@RequestMapping(value="/api/pharmacy/")
@SuppressWarnings("rawtypes")
public class QMSTokenMasterController implements ICommonConstants{

	@Autowired
	IQMSTokenMasterService iQMSTokenMasterService;

	private static final Logger logger = Logger.getLogger(QMSTokenMasterController.class);


	@RequestMapping(value="/saveToken",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response saveToken(@RequestBody QMSTokenMasterDto dto){
		try{
			return iQMSTokenMasterService.saveToken(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/getTokenList",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTokenList(@RequestBody QMSTokenMasterDto dto){
		try{
			return iQMSTokenMasterService.getTokenList(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/updateToken",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateToken(@RequestBody QMSTokenMasterDto dto){
		try{
			return iQMSTokenMasterService.updateToken(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/transferToken",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response transferToken(@RequestBody QMSTokenMasterDto dto){
		try{
			return iQMSTokenMasterService.transferToken(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/resetTokenSequence",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response resetTokenSequence(@RequestBody QMSTokenMasterDto dto){
		try{
			return iQMSTokenMasterService.resetTokenSequence(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/getTokenListForDisplay",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTokenListForDisplay(@RequestBody QMSTokenMasterDto dto){
		try{
			return iQMSTokenMasterService.getTokenListForDisplay(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/getTokenDisplayMessage",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTokenDisplayMessage(@RequestBody QMSTokenMasterDto dto){
		try{
			return iQMSTokenMasterService.getTokenDisplayMessage(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/checkingIsCalledOrNot",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response checkingIsCalledOrNot(@RequestBody QMSTokenMasterDto dto){
		try{
			return iQMSTokenMasterService.checkingIsCalledOrNot(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/releaseTokenNumber",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response releaseTokenNumber(@RequestBody QMSTokenMasterDto dto){
		try{
			return iQMSTokenMasterService.releaseTokenNumber(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
}
