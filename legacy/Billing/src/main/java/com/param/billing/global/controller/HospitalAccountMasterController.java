package com.param.billing.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.Response;
import com.param.billing.global.dto.HospitalAccountMasterDto;
import com.param.billing.global.service.IHospitalAccountMasterService;

@RestController
@RequestMapping("api/billing")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class HospitalAccountMasterController implements ICommonConstants{

		@Autowired
		private IHospitalAccountMasterService iHospitalAccountMasterService;
		
		@RequestMapping(value="/activeHospitalAccountMaster",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
		public Response getActiveHospitalAccountMaster(@RequestBody HospitalAccountMasterDto hospitalAccountMasterDto) {
			try {
				return iHospitalAccountMasterService.getActiveHospitalAccountMaster(hospitalAccountMasterDto);
			}catch (Exception e) {
				
				throw e;
			}
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
		
		@RequestMapping(value = "/saveHospitalAccountMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response saveHospitalAccountMaster(
				@RequestBody HospitalAccountMasterDto hospitalAccountMasterDto) {
			try {
				return iHospitalAccountMasterService.saveHospitalAccountMaster(hospitalAccountMasterDto);
			} catch (Exception e) {
				
				throw e;
			}
			//return new Response(ERROR, null, null, null, null);
		}

		@RequestMapping(value = "/getHospitalAccountMasterById/{hospitalAccountId}/{orgId}/{unitId}", method = RequestMethod.GET)
		public @ResponseBody Response getHospitalAccountMasterById(@PathVariable("hospitalAccountId") Integer hospitalAccountId,
				@PathVariable("orgId") Integer orgId,@PathVariable("unitId") Integer unitId) {
			try {
				return iHospitalAccountMasterService.getHospitalAccountMasterById(hospitalAccountId, orgId,unitId);
			} catch (Exception e) {
				
				throw e;
			}
			//return new Response(ERROR, null, null, null, null);
		}

		@RequestMapping(value = "/getHospitalAccountMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getHospitalAccountMasterList(@RequestBody HospitalAccountMasterDto hospitalAccountMasterDto) {
			try {
				return iHospitalAccountMasterService.getHospitalAccountMasterList(hospitalAccountMasterDto);
			} catch (Exception e) {
				
				throw e;
			}
		//	return new Response(ERROR, null, null, null, null);
		}

		@RequestMapping(value = "/updateHospitalAccountMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response updateHospitalAccountMaster(
				@RequestBody HospitalAccountMasterDto hospitalAccountMasterDto) {
			try {
				return iHospitalAccountMasterService.updateHospitalAccountMaster(hospitalAccountMasterDto);
			} catch (Exception e) {
				
				throw e;
			}
			//return new Response(ERROR, null, null, null, null);
		}

		@RequestMapping(value = "/updateHospitalAccountMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response updateHospitalAccountMasterStatus(
				@RequestBody HospitalAccountMasterDto hospitalAccountMasterDto) {
			try {
				return iHospitalAccountMasterService.updateHospitalAccountMasterStatus(hospitalAccountMasterDto);
			} catch (Exception e) {
				
				throw e;
			}
			//return new Response(ERROR, null, null, null, null);
		}
		@RequestMapping(value = "/getHospitalAccountMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getHospitalAccountMasterCount(
				@RequestBody HospitalAccountMasterDto hospitalAccountMasterDto) {
			try {
				return iHospitalAccountMasterService.getHospitalAccountMasterCount(hospitalAccountMasterDto);
			} catch (Exception e) {
				
				throw e;
			}
			//return new Response(ERROR, null, null, null, null);
		}
}
