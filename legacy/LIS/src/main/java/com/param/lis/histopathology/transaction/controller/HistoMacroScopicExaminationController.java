package com.param.lis.histopathology.transaction.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.lis.common.dto.CommonDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.dto.SampleMasterDto;
import com.param.lis.histopathology.transaction.dto.HistoParamDto;
import com.param.lis.histopathology.transaction.dto.TSpecimanMasterDto;
import com.param.lis.histopathology.transaction.service.IHistoMacroScopicExaminationService;
import com.param.lis.transaction.dto.SearchCommonDto;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Histopathology Macroscopic Examination", tags = "TSpcimanMaster")
@SuppressWarnings({ "rawtypes", "unchecked" })
@RequestMapping(value = "/LIS/histopathology")
public class HistoMacroScopicExaminationController implements ICommonConstants, IError
{
	final static Logger logger = Logger.getLogger(HistoMacroScopicExaminationController.class);
	
	@Autowired 
	private IHistoMacroScopicExaminationService iHistoMicroScopicExaminationService;
	
	
	@RequestMapping(value = "/Specimans", method = RequestMethod.POST)
	public @ResponseBody Response getCollectedSpecimens(@RequestBody SearchDto  searchDto) 
	{
		Response<TSpecimanMasterDto, Integer> response = null;
		try {
			response = iHistoMicroScopicExaminationService.listSpecimanReceipt(searchDto);
			return response;
		} catch (Exception e) 
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@RequestMapping(value = "/Specimans/count", method = RequestMethod.POST)
	public @ResponseBody Response getCollectedSpecimensCount(@RequestBody SearchDto  searchDto) {
		Response<TSpecimanMasterDto, Integer> response = null;
		try {
			response = iHistoMicroScopicExaminationService.getCollectedSpecimensCount(searchDto);
			return response;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/mascroscopic/examination", method = RequestMethod.POST)
	public @ResponseBody Response saveMicroscopicExaminationDetails(@RequestBody TSpecimanMasterDto tSpecimanMasterDto ) {
		Response<TSpecimanMasterDto, Integer> response = null;
		try {
			response = iHistoMicroScopicExaminationService.saveMicroscopicExaminationDetails(tSpecimanMasterDto);
			return response;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/specimen/receipt", method = RequestMethod.POST)
	public @ResponseBody Response saveSpecimanDetails(@RequestBody TSpecimanMasterDto tSpecimanMasterDto) {
		Response<SampleMasterDto, Integer> response = null;
		try {
			response = iHistoMicroScopicExaminationService.saveSpecimanDetails(tSpecimanMasterDto);
			return response;
		} catch (Exception e) 
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	
	/**Code for Lab Search*/
	@RequestMapping(value = "/patient/search", method = RequestMethod.POST)
	public @ResponseBody Response searchAcceptedSpecimanByPatient(@RequestBody SearchCommonDto searchCommonDto) {
		Response<CommonDto, Integer> response = null;
		try {
			response = iHistoMicroScopicExaminationService.searchAcceptedSpecimanByPatient(searchCommonDto);
			return response;
		} catch (Exception e) 
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	@RequestMapping(value = "/patient/visit/list", method = RequestMethod.POST)
	public @ResponseBody Response searchPatientbyVisitType(@RequestBody SearchCommonDto searchCommonDto) {
		Response<CommonDto, Integer> response = null;
		try {
			response = iHistoMicroScopicExaminationService.searchPatientbyVisitType(searchCommonDto);
			return response;
		} catch (Exception e) 
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/specimen/list", method = RequestMethod.POST)
	public @ResponseBody Response searchBySpecimenType(@RequestBody SearchCommonDto searchCommonDto) {
		Response<CommonDto, Integer> response = null;
		try {
			response = iHistoMicroScopicExaminationService.searchBySpecimenType(searchCommonDto);
			return response;
		} catch (Exception e) 
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/test/list", method = RequestMethod.POST)
	public @ResponseBody Response searchByTest(@RequestBody SearchCommonDto searchCommonDto) {
		Response<CommonDto, Integer> response = null;
		try {
			response = iHistoMicroScopicExaminationService.searchByTest(searchCommonDto);
			return response;
		} catch (Exception e) 
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@RequestMapping(value = "/specimen/search", method = RequestMethod.POST)
	public @ResponseBody Response accetedSpecimenSearch(@RequestBody SearchDto searchDto) 
	{
		Response<CommonDto, Integer> response = null;
		try {
			response = iHistoMicroScopicExaminationService.accetedSpecimenSearch(searchDto);
			return response;
		} catch (Exception e) 
		{
			logger.error("Exception", e);
			throw e;
		}
	}
	
	
	
	@RequestMapping(value = "/getMicroscopicExamDataBySubSpecimenId/{orgId}/{orgUnitId}/{subSpecimanId}/{isHistoCyto}", method = RequestMethod.GET)
	public @ResponseBody Response getTotalSampleMasterRecord(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "orgUnitId") Integer orgUnitId,@PathVariable(value = "subSpecimanId") Integer subSpecimanId,@PathVariable(value = "isHistoCyto") Character isHistoCyto) {
		Response<SampleMasterDto, Integer> response = null;
		try {
			response = iHistoMicroScopicExaminationService.getMicroscopicExamDataBySubSpecimenId(subSpecimanId, orgId, orgUnitId,isHistoCyto);
			return response;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/getSubSpeciman/{labSampleDtlsId}/{orgId}/{orgUnitId}",method = RequestMethod.GET)
	public @ResponseBody Response getSubSpecimanByLabSampleDtlsId(@PathVariable(value = "labSampleDtlsId") Integer labSampleDtlsId, 
			@PathVariable(value = "orgId") Integer orgId,@PathVariable(value = "orgUnitId") Integer orgUnitId)
	{
		try
		{
			Response response = iHistoMicroScopicExaminationService.getSubSpecimanByLabSampleDtlsId(labSampleDtlsId, orgId, orgUnitId);
			return response;
		}catch (Exception e) {
			throw e;
		}
		
	}
	
	
	
	@RequestMapping(value = "/test/list/{orgId}/{orgUnitId}", method = RequestMethod.GET)
	public @ResponseBody Response getTestList(@PathVariable ("orgId") Integer orgId,
			@PathVariable ("orgUnitId") Integer orgUnitId) {
		Response<CommonDto, Integer> response = null;
		try {
			response = iHistoMicroScopicExaminationService.getTestList(orgId,orgUnitId);
			return response;
		} catch (Exception e) 
		{
			logger.error("Exception", e);
			throw e;
		}
	}
}
