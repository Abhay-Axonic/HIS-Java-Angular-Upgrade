package com.param.opd.coversheet.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.opd.coversheet.dto.DiagnosisMapperDto;
import com.param.opd.coversheet.service.IDiagnosisMapperService;

@RestController
@SuppressWarnings({ "rawtypes", "unchecked" })
@RequestMapping("/api/opd")
public class DiagnosisMapperController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(DiagnosisMapperController.class);

	@Autowired
	IDiagnosisMapperService iDiagnosisMapper;

	@RequestMapping(value = "/saveDiagnosisMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveDiagnosisMapper(@RequestBody DiagnosisMapperDto diagnosisMapperDto) {
		try {
			return iDiagnosisMapper.saveDiagnosisMapper(diagnosisMapperDto);
		} catch (Exception e) {
			logger.error("saveDiagnosisMapper failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/searchDiagnosisByCodeOrDescription", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchDiagnosisByCodeOrDescription(@RequestBody DiagnosisMapperDto diagnosisMapperDto) {
		try {
			return iDiagnosisMapper.searchDiagnosisByCodeOrDescription(diagnosisMapperDto);
		} catch (Exception e) {
			logger.error("searchDiagnosisByCodeOrDescription failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getListOfDiagnosisMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getListOfDiagnosisMapper(@RequestBody DiagnosisMapperDto diagnosisMapperDto) {
		try {
			return iDiagnosisMapper.getListOfDiagnosisMapper(diagnosisMapperDto);
		} catch (Exception e) {
			logger.error("getListOfDiagnosisMapper failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/updateDiagnosisMapperStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateDiagnosisMapperStatus(@RequestBody DiagnosisMapperDto diagnosisMapperDto) {
		try {
			return iDiagnosisMapper.updateDiagnosisMapperStatus(diagnosisMapperDto);
		} catch (Exception e) {
			logger.error("updateDiagnosisMapperStatus failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getGenderByDiagnosisId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getGenderByDiagnosisId(@RequestBody DiagnosisMapperDto diagnosisMapperDto) {
		try {
			return iDiagnosisMapper.getGenderByDiagnosisId(diagnosisMapperDto);
		} catch (Exception e) {
			logger.error("getGenderByDiagnosisId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getDiagnosisByGenderId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDiagnosisByGenderId(@RequestBody DiagnosisMapperDto diagnosisMapperDto) {
		try {
			return iDiagnosisMapper.getDiagnosisByGenderId(diagnosisMapperDto);
		} catch (Exception e) {
			logger.error("getDiagnosisByGenderId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getDiagnosisMapperById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDiagnosisMapperById(@RequestBody DiagnosisMapperDto diagnosisMapperDto) {
		try {
			return iDiagnosisMapper.getDiagnosisMapperById(diagnosisMapperDto);
		} catch (Exception e) {
			logger.error("getDiagnosisMapperById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/updateDiagnosisMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateDiagnosisMapper(@RequestBody DiagnosisMapperDto diagnosisMapperDto) {
		try {
			return iDiagnosisMapper.updateDiagnosisMapper(diagnosisMapperDto);
		} catch (Exception e) {
			logger.error("updateDiagnosisMapper failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getDiagnosisMapperCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDiagnosisMapperCount(@RequestBody DiagnosisMapperDto diagnosisMapperDto) {
		try {
			return iDiagnosisMapper.getDiagnosisMapperCount(diagnosisMapperDto);
		} catch (Exception e) {
			logger.error("getDiagnosisMapperCount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
}
