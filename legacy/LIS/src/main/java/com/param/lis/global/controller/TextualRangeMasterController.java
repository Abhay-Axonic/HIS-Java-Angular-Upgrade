package com.param.lis.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.param.global.common.ICommonConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.dto.TextualRangeMasterDto;
import com.param.lis.global.service.ITextualRangeMasterService;

@RestController
@RequestMapping(value="/LIS/global")
@SuppressWarnings({"rawtypes"})
public class TextualRangeMasterController implements ICommonConstants{
	
	@Autowired
	ITextualRangeMasterService iTextualRangeMasterService;
	
	@RequestMapping(value = "/saveTextualRangeMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveTextualRangeMaster(@RequestBody TextualRangeMasterDto textualRangeMasterDto){
		try{
			return iTextualRangeMasterService.saveTextualRangeMaster(textualRangeMasterDto);
		}catch(Exception e){
			
			throw e;
		}
		 
	}
	 
	
	@RequestMapping(value="/updateTextualRangeMaster" , method= RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateTextualRangeMaster(@RequestBody TextualRangeMasterDto textualRangeMasterDto){
		try{
			return iTextualRangeMasterService.updateTextualRangeMaster(textualRangeMasterDto);
		}catch (Exception e) {
			
			throw e;
		}
	}
	
	
	@RequestMapping(value="/updateTextualRangeMasterStatus" , method= RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateTextualRangeMasterStatus(@RequestBody TextualRangeMasterDto textualRangeMasterDto){
		try{
			return iTextualRangeMasterService.updateTextualRangeMasterStatus(textualRangeMasterDto);
		}catch (Exception e) {
			
			throw e;
		}
	}
	
	
	@RequestMapping(value="/getTextualRangeMasterList" , method= RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTextualRangeMasterList(@RequestBody TextualRangeMasterDto textualRangeMasterDto){
		try{
			return iTextualRangeMasterService.getTextualRangeMasterList(textualRangeMasterDto);
		}catch (Exception e) {
			
			throw e;
		}
	}
	
	
	@RequestMapping(value="/getTextualRangeMasterById/{textualRangeId}/{orgId}/{orgUnitId}" , method= RequestMethod.GET)
	public Response getTextualRangeMasterById(@PathVariable ("textualRangeId") Integer textualRangeId, @PathVariable ("orgId") Integer orgId, @PathVariable ("orgUnitId") Integer orgUnitId){
		try{
			return iTextualRangeMasterService.getTextualRangeMasterById(textualRangeId, orgId, orgUnitId);
		}catch (Exception e) {
			
			throw e;
		}
	}
	
	
	@RequestMapping(value="/getActiveTextualRangeMasterList" , method= RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveTextualRangeMasterList(@RequestBody TextualRangeMasterDto textualRangeMasterDto){
		try{
			return iTextualRangeMasterService.getActiveTextualRangeMasterList(textualRangeMasterDto);
		}catch (Exception e) {
			
			throw e;
		}
	}
	
	
	@RequestMapping(value="/getTextualRangeMasterCount" , method= RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTextualRangeMasterCount(@RequestBody TextualRangeMasterDto textualRangeMasterDto){
		try{
			return iTextualRangeMasterService.getTextualRangeMasterCount(textualRangeMasterDto);
		}catch (Exception e) {
			
			throw e;
		}
	}

}
