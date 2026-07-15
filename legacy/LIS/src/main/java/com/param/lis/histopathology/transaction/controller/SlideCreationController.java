package com.param.lis.histopathology.transaction.controller;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.JsonCheck;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.histopathology.transaction.dto.HistoParamDto;
import com.param.lis.histopathology.transaction.dto.TBlockMasterDto;
import com.param.lis.histopathology.transaction.dto.TGrossMasterDto;
import com.param.lis.histopathology.transaction.dto.TSlideMasterDto;
import com.param.lis.histopathology.transaction.dto.TSubSpecimanMasterDto;
import com.param.lis.histopathology.transaction.service.ISlideCreationService;
import io.swagger.annotations.Api;

@RestController
@Api(value = "Slide Creation Controller", tags = "TSlideMaster")
@SuppressWarnings({ "rawtypes", "unchecked" })
@RequestMapping(value = "/LIS/histopathology")
public class SlideCreationController implements ICommonConstants, IError {
	final Logger logger = Logger.getLogger(SlideCreationController.class);

	@Autowired
	private ISlideCreationService iSlideCreationService;

	@RequestMapping(value = "/slide/creation", method = RequestMethod.POST)
	public @ResponseBody Response getSlideCreationWorkList(@RequestBody SearchDto searchDto) {
		Response<TSubSpecimanMasterDto, Integer> response = null;
		try {
			response = iSlideCreationService.getSlideCreationWorkList(searchDto);
			return response;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@RequestMapping(value = "/slide/creation/count", method = RequestMethod.POST)
	public @ResponseBody Response getSlideCreationWorkListCount(@RequestBody SearchDto searchDto) {
		Response<TSubSpecimanMasterDto, Integer> response = null;
		try {
			response = iSlideCreationService.getSlideCreationWorkListCount(searchDto);
			return response;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@RequestMapping(value = "/slide", method = RequestMethod.POST)
	public @ResponseBody Response getCreatedBlockList(@RequestBody TSubSpecimanMasterDto tSubSpecimanMasterDto) {
		Response<TSubSpecimanMasterDto, Integer> response = null;
		try {
			response = iSlideCreationService.getCreatedBlockList(tSubSpecimanMasterDto);
			return response;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@RequestMapping(value = "/slides", method = RequestMethod.POST)
	public @ResponseBody Response createSlides(@RequestBody List<TGrossMasterDto> listTGrossMasterDto) {
		Response<TGrossMasterDto, Integer> response = null;
		try {
			response = iSlideCreationService.createSlides(listTGrossMasterDto);
			return response;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@RequestMapping(value = "/cyto/slides", method = RequestMethod.POST)
	public @ResponseBody Response createSlidesCytology(@RequestBody List<TSlideMasterDto> listTSlideMaster) {
		Response<TGrossMasterDto, Integer> response = null;
		try {
			response = iSlideCreationService.createSlidesCytology(listTSlideMaster);
			return response;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@RequestMapping(value ="/subspecimanbyspecimanId/{specimanId}",method = RequestMethod.GET)
	public @ResponseBody Response getSubSpecimanBySpecimanId(@PathVariable int specimanId)
	{
		try{
			Response response= iSlideCreationService.getSubSpecimanBySpecimanId(specimanId);
			return response;
		}catch(Exception e){
			logger.error("Exception", e);
			throw e;
		}
	}
	
	

}
