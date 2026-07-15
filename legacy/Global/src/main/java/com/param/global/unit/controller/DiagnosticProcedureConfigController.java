package com.param.global.unit.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.DiagnosticProcedureConfigDto;
import com.param.global.unit.service.IDiagnosticProcedureConfigService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
	@RestController
	@RequestMapping("/api/unit/clinical")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public class DiagnosticProcedureConfigController  implements ICommonConstants{
	final static Logger logger = Logger.getLogger(DiagnosticProcedureConfigController.class);


		@Autowired
		private IDiagnosticProcedureConfigService iDiagnosticProcedureConfigService;
		
		
		
		@RequestMapping(value = "/saveDiagnosticProcedureConfig", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response saveDiagnosticProcedureConfig(@RequestBody DiagnosticProcedureConfigDto diagnosticProcedureConfigDto) {
			try {
				return iDiagnosticProcedureConfigService.saveDiagnosticProcedureConfig(diagnosticProcedureConfigDto);
			}catch(Exception e) {
			logger.error("method failed", e);
			}
			return new Response(ERROR, null, null, null, null);
		}
		
		@RequestMapping(value = "/getDiagnosticProcedureConfigList", method = RequestMethod.POST)
		public @ResponseBody Response getDiagnosticProcedureConfigList(@RequestBody DiagnosticProcedureConfigDto diagnosticProcedureConfigDto) {
			try {
				return iDiagnosticProcedureConfigService.getDiagnosticProcedureConfigList(diagnosticProcedureConfigDto);
			}catch(Exception e) {
			logger.error("method failed", e);
			}
			return new Response(ERROR, null, null, null, null);
		}
		
		@RequestMapping(value = "/updateDiagnosticProcedureConfig", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response updateDiagnosticProcedureConfig(@RequestBody DiagnosticProcedureConfigDto diagnosticProcedureConfigDto) {
			try {
				return iDiagnosticProcedureConfigService.updateDiagnosticProcedureConfig(diagnosticProcedureConfigDto);
			}catch(Exception e) {
			logger.error("method failed", e);
			}
			return new Response(ERROR, null, null, null, null);
		}
		
		@RequestMapping(value = "/updateDiagnosticProcedureConfigStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response updateDiagnosticProcedureConfigStatus(@RequestBody DiagnosticProcedureConfigDto diagnosticProcedureConfigDto) {
			try {
				return iDiagnosticProcedureConfigService.updateDiagnosticProcedureConfigStatus(diagnosticProcedureConfigDto);
			}catch(Exception e) {
			logger.error("method failed", e);
			}
			return new Response(ERROR, null, null, null, null);
		}
		
		@RequestMapping(value = "/getDiagnosticProcedureConfigCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getDiagnosticProcedureConfigCount(@RequestBody DiagnosticProcedureConfigDto diagnosticProcedureConfigDto) {
			try {
				return iDiagnosticProcedureConfigService.getDiagnosticProcedureConfigCount(diagnosticProcedureConfigDto);
			}catch(Exception e) {
			logger.error("method failed", e);
			}
			return new Response(ERROR, null, null, null, null);
		}
		
		@RequestMapping(value = "/getDiagnosticProcedureConfigById/{diagnosticProcedureConfigId}/{organizationId}/{unitId}", method = RequestMethod.GET)
		public Response getDiagnosticProcedureConfigById(@PathVariable ("diagnosticProcedureConfigId") Integer diagnosticProcedureConfigId,
										@PathVariable ("organizationId") Integer organizationId,
										@PathVariable ("unitId") Integer unitId){
			try {
				return iDiagnosticProcedureConfigService.getDiagnosticProcedureConfigById(diagnosticProcedureConfigId,organizationId,unitId);
			}catch(Exception e) {
			logger.error("getDiagnosticProcedureConfigById failed", e);
			}
			return new Response(ERROR, null, null, null, null);
		}
		
		
	}


