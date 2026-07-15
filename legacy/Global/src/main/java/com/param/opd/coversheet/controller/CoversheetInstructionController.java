package com.param.opd.coversheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.CoversheetInstructionDto;
import com.param.opd.coversheet.service.ICoversheetInstructionService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Controller
@RequestMapping("api/opd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CoversheetInstructionController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(CoversheetInstructionController.class);



	@Autowired
	ICoversheetInstructionService iCoversheetInstructionService;
 	
	@RequestMapping(value="/coversheetInstruction", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveCoversheetInstruction(@RequestBody CoversheetInstructionDto coversheetInstructionDto) {
		try{
			return iCoversheetInstructionService.saveCoversheetInstruction(coversheetInstructionDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/coversheetInstructionList/{visitTypeId}", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response coversheetInstructionList(@RequestBody CoversheetInstructionDto coversheetInstructionDto,@PathVariable(value="visitTypeId") int visitTypeId) {
		try{
			return iCoversheetInstructionService.coversheetInstructionList(coversheetInstructionDto.getEncounterId(),visitTypeId);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateCoversheetInstructionStatus", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateCoversheetInstructionStatus(@RequestBody CoversheetInstructionDto coversheetInstructionDto) {
		try{
			return iCoversheetInstructionService.updateCoversheetInstructionStatus(coversheetInstructionDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/coversheetInstructionListForBookView", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response coversheetInstructionListForBookView(@RequestBody CoversheetInstructionDto coversheetInstructionDto) {
		try{
			return iCoversheetInstructionService.coversheetInstructionListForBookView(coversheetInstructionDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
}
