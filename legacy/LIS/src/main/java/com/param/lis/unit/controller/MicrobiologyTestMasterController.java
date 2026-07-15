package com.param.lis.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.lis.global.common.Response;
import com.param.lis.unit.dto.MicrobiologyTestMasterDto;
import com.param.lis.unit.service.IMicrobiologyTestMasterService;



@RestController
@RequestMapping(value="/LIS/unit")
@SuppressWarnings({ "rawtypes"})
public class MicrobiologyTestMasterController implements ICommonConstants {
	@Autowired
	private IMicrobiologyTestMasterService iMicrobiologyTestMasterService;
	
	@RequestMapping(value = "/saveMicrobiologyTestMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public  Response saveMicrobiologyTestMaster(@RequestBody MicrobiologyTestMasterDto microbiologyTestMasterDto) {
		
		try {
			return iMicrobiologyTestMasterService.saveMicrobiologyTestMaster(microbiologyTestMasterDto);
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/updateMicrobiologyTestMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateMicrobiologyTestMaster(@RequestBody MicrobiologyTestMasterDto microbiologyTestMasterDto) {
		
		try {
			return iMicrobiologyTestMasterService.updateMicrobiologyTestMaster(microbiologyTestMasterDto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/updateMicrobiologyTestMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateMicrobiologyTestMasterStatus(@RequestBody MicrobiologyTestMasterDto microbiologyTestMasterDto) {
		
		try {
			return iMicrobiologyTestMasterService.updateMicrobiologyTestMasterStatus(microbiologyTestMasterDto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/getMicrobiologyTestMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getMicrobiologyTestMasterList(@RequestBody MicrobiologyTestMasterDto microbiologyTestMasterDto) {
		
		try {
			return iMicrobiologyTestMasterService.getMicrobiologyTestMasterList(microbiologyTestMasterDto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/getMicrobiologyTestMasterById/{microTestId}/{orgId}/{orgUnitId}", method = RequestMethod.GET)
	public Response getMicrobiologyTestMasterById(@PathVariable ("microTestId") Integer microTestId,
																@PathVariable ("orgId") Integer orgId,
																@PathVariable ("orgUnitId") Integer orgUnitId) {
		
		try {
			return iMicrobiologyTestMasterService.getMicrobiologyTestMasterById(microTestId, orgId, orgUnitId);
			
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/getMicrobiologyTestMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getMicrobiologyTestMasterCount(@RequestBody MicrobiologyTestMasterDto microbiologyTestMasterDto) {
		
		try {
			return iMicrobiologyTestMasterService.getMicrobiologyTestMasterCount(microbiologyTestMasterDto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/getActiveMicrobiologyTestMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveMicrobiologyTestMasterList(@RequestBody MicrobiologyTestMasterDto microbiologyTestMasterDto) {
		
		try {
			return iMicrobiologyTestMasterService.getActiveMicrobiologyTestMasterList(microbiologyTestMasterDto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}
}
