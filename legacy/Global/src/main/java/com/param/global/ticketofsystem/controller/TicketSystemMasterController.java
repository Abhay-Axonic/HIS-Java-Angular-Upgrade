package com.param.global.ticketofsystem.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.ticketofsystem.dto.TicketSystemMasterDto;
import com.param.global.ticketofsystem.service.ITicketSystemMasterService;


import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/api/ticketsystemmaster") 
@SuppressWarnings({"rawtypes","unchecked"})
public class TicketSystemMasterController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(TicketSystemMasterController.class);

	
	@Autowired
	ITicketSystemMasterService iTicketSystemMasterService; 
	 
	@RequestMapping (value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addTicketSystemMaster( @RequestBody TicketSystemMasterDto ticketSystemMasterDto) {
		try {
 			   return iTicketSystemMasterService.addTicketSystemMaster(ticketSystemMasterDto);

		} catch (Exception e) {
		logger.error("method failed", e);
			String exceptionErrorMsg = TicketSystemMasterController.class.getName()+" : "+e.getMessage();
			return new Response(ERROR, ERROR_CODE, exceptionErrorMsg, null, null);
		}
	}
	
	@RequestMapping (value="/getall/{unitId}/{organizationId}",method=RequestMethod.GET)
	public @ResponseBody Response getAllTicketSystemMaster( @PathVariable Integer unitId, @PathVariable Integer organizationId) {
		try {
 			   return iTicketSystemMasterService.getAllTicketSystemMasterList(unitId,organizationId);

		} catch (Exception e) {
		logger.error("method failed", e);
			String exceptionErrorMsg = TicketSystemMasterController.class.getName()+" : "+e.getMessage();
			return new Response(ERROR, ERROR_CODE, exceptionErrorMsg, null, null);
		}
	}
    
	@RequestMapping (value = "/update", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateTicketSystemMaster(@RequestBody TicketSystemMasterDto ticketSystemMasterDto) {
		
		try {
			return iTicketSystemMasterService.updateTicketSystemMaster(ticketSystemMasterDto);

		} catch (Exception e) {
		logger.error("method failed", e);
			String exceptionErrorMsg = TicketSystemMasterController.class.getName()+" : "+e.getMessage();
			return new Response(ERROR, ERROR_CODE, exceptionErrorMsg, null, null);
		}
	}

	@RequestMapping (value="/get/{ticketMasterId}/{unitId}/{organizationId}",method=RequestMethod.GET)
	public @ResponseBody Response getTicketSystemMasterById(@PathVariable Integer ticketMasterId,
			@PathVariable Integer unitId, @PathVariable Integer organizationId) {
		try {
			return iTicketSystemMasterService.getTicketSystemMasterById(ticketMasterId,unitId,organizationId);

		} catch (Exception e) {
		logger.error("method failed", e);
			String exceptionErrorMsg = TicketSystemMasterController.class.getName()+" : "+e.getMessage();
			return new Response(ERROR, ERROR_CODE, exceptionErrorMsg, null, null);
		}
	}
	
	@RequestMapping (value="/remove/{ticketMasterId}/{unitId}/{organizationId}/{ticketStatus}",method=RequestMethod.PATCH)
	public @ResponseBody Response removeTicketSystemMaster(@PathVariable Integer ticketMasterId ,
			@PathVariable Integer unitId, @PathVariable Integer organizationId,@PathVariable Character ticketStatus) {
		
		try {
			return iTicketSystemMasterService.changeTicketSystemMasterStatus(ticketMasterId, unitId, organizationId,ticketStatus);

		} catch (Exception e) {
		logger.error("method failed", e);
			String exceptionErrorMsg = TicketSystemMasterController.class.getName()+" : "+e.getMessage();
			return new Response(ERROR, ERROR_CODE, exceptionErrorMsg, null, null);
		}
	}
	
	@RequestMapping (value = "/savefile", headers=("content-type=multipart/*"), method = RequestMethod.POST)	
	public @ResponseBody Response saveTicketSystemMasterFile(
			@RequestParam("updateFileFlage") Boolean updateFileFlage ,
			@RequestParam("ticketMasterId") Integer ticketMasterId,
			@RequestParam("userId") Integer userId,
			@RequestParam("unitId") Integer unitId, 
			@RequestParam("organizationId") Integer organizationId,
			@RequestParam("organizationName") String organizationName,
		    @RequestParam("files") MultipartFile[] files  ) {
		try {
			   return iTicketSystemMasterService.saveTicketSystemMasterfile(ticketMasterId,organizationName, unitId, userId, organizationId,files,updateFileFlage);

		} catch (Exception e) {
		logger.error("method failed", e);
			String exceptionErrorMsg = TicketSystemMasterController.class.getName()+" : "+e.getMessage();
			return new Response(ERROR, ERROR_CODE, exceptionErrorMsg, null, null);
		}

	}
	
	@RequestMapping (value="/getfile/byticketid/{ticketMasterId}/{unitId}/{organizationId}",method=RequestMethod.GET)
	public @ResponseBody Response getAllTicketSystemMasterFilesByTicketMasteId(@PathVariable Integer ticketMasterId,
			@PathVariable Integer unitId, @PathVariable Integer organizationId) {
		
		try {
			   return iTicketSystemMasterService.getAllTicketSystemMasterfile(ticketMasterId, unitId, organizationId);

		} catch (Exception e) {
		logger.error("method failed", e);
			String exceptionErrorMsg = TicketSystemMasterController.class.getName()+" : "+e.getMessage();
			return new Response(ERROR, ERROR_CODE, exceptionErrorMsg, null, null);
		}

	}
	
	@RequestMapping (value="/removefile/{fileId}/{unitId}/{organizationId}",method=RequestMethod.PATCH)
	public @ResponseBody Response removeTicketSystemMasterFile(@PathVariable Integer fileId,
			@PathVariable Integer unitId, @PathVariable Integer organizationId) {
		
		try {
			   return iTicketSystemMasterService.removeTicketSystemMasterfile(fileId, unitId, organizationId);

		} catch (Exception e) {
		logger.error("method failed", e);
			String exceptionErrorMsg = TicketSystemMasterController.class.getName()+" : "+e.getMessage();
			return new Response(ERROR, ERROR_CODE, exceptionErrorMsg, null, null);
		}

	}
	
	@RequestMapping (value="/removeallfile/{ticketMasterId}/{unitId}/{organizationId}",method=RequestMethod.PATCH)
	public @ResponseBody Response removeAllTicketSystemMasterFile(@PathVariable Integer ticketMasterId,
			@PathVariable Integer unitId, @PathVariable Integer organizationId ) {
		try {
			return  iTicketSystemMasterService.removeAllTicketSystemMasterfile(ticketMasterId, unitId, organizationId);
		}catch (Exception e) {
		logger.error("method failed", e);
			String exceptionErrorMsg = TicketSystemMasterController.class.getName()+" : "+e.getMessage();
			return new Response(ERROR, ERROR_CODE, exceptionErrorMsg, null, null);
		}

	}
	
	@RequestMapping (value="/search",method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchTicketSystem (@RequestBody TicketSystemMasterDto ticketSystemMasterDto ) {
		
		try {
			return  iTicketSystemMasterService.searchTicketSystem(ticketSystemMasterDto);
		}catch (Exception e) {
		logger.error("method failed", e);
			String exceptionErrorMsg = TicketSystemMasterController.class.getName()+" : "+e.getMessage();
			return new Response(ERROR, ERROR_CODE, exceptionErrorMsg, null, null);
		}
	}
	 
}
