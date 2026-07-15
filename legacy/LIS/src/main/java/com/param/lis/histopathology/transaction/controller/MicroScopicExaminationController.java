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

import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.histopathology.transaction.dto.TMicroExaDetailsDto;
import com.param.lis.histopathology.transaction.dto.TSlideMasterDto;
import com.param.lis.histopathology.transaction.dto.TSpecimanMasterDto;
import com.param.lis.histopathology.transaction.dto.TSubSpecimanMasterDto;
import com.param.lis.histopathology.transaction.service.IMicroScopicExaminationService;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Histopathology Microscopic Examination", tags = "TSpcimanMaster")
@SuppressWarnings({ "rawtypes", "unchecked" })
@RequestMapping(value = "/LIS/histopathology")
public class MicroScopicExaminationController implements ICommonConstants, IError
{
	final Logger logger = Logger.getLogger(MicroScopicExaminationController.class);
	
	@Autowired 
	private IMicroScopicExaminationService iMicroScopicExaminationService;
	
	
	@RequestMapping(value = "/microscopic/examination", method = RequestMethod.POST)
	public @ResponseBody Response getMicroscopicWorkList(@RequestBody SearchDto searchDto) 
	{
		Response<TSubSpecimanMasterDto, Integer> response = null;
		try {
			response = iMicroScopicExaminationService.getMicroscopicWorkList(searchDto);
			return response;
		} catch (Exception e) 
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@RequestMapping(value = "/microscopic/examination/count", method = RequestMethod.POST)
	public @ResponseBody Response getMicroscopicWorkListCount(@RequestBody SearchDto searchDto) 
	{
		Response<TSpecimanMasterDto, Integer> response = null;
		try {
			response = iMicroScopicExaminationService.getMicroscopicWorkListCount(searchDto);
			return response;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/miscroscopic/slides", method = RequestMethod.POST)
	public @ResponseBody Response getMiscroscopicSlides(@RequestBody TSubSpecimanMasterDto tSubSpecimanMasterDto)
	{
		Response<TSlideMasterDto, Integer> response = null;
		try {
			response = iMicroScopicExaminationService.getMiscroscopicSlides(tSubSpecimanMasterDto);
			return response;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/miscroscopic/slide/examination", method = RequestMethod.POST)
	public @ResponseBody Response saveSlideExaminationsDetails(@RequestBody List<TMicroExaDetailsDto> listTMicroExaDetailsDto)
	{
		Response<TSlideMasterDto, Integer> response = null;
		try {
			response = iMicroScopicExaminationService.saveSlideExaminationsDetails(listTMicroExaDetailsDto);
			return response;
		} catch (Exception e) 
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/microscopic/examination/labSampleDetails", method = RequestMethod.POST)
	public @ResponseBody Response getMicroscopicWorkListLabSampleDetails(@RequestBody SearchDto searchDto) 
	{
		Response<TSubSpecimanMasterDto, Integer> response = null;
		try {
			response = iMicroScopicExaminationService.getMicroscopicWorkListLabSampleDetails(searchDto);
			return response;
		} catch (Exception e) 
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/microscopic/examination/getSubSpecimanBySpeciman/{subSpecimanId}", method = RequestMethod.GET)
	public @ResponseBody Response getMicroscopicWorkListBySubSpeciman(@PathVariable int subSpecimanId) 
	{
		try {
			Response response = iMicroScopicExaminationService.getMicroscopicWorkListBySubSpeciman(subSpecimanId);
			return response;
		} catch (Exception e) 
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
}
