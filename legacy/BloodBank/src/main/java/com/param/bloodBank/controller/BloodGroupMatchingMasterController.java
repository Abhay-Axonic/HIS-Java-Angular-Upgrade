package com.param.bloodBank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.param.bloodBank.dto.BloodGroupMatchingMasterDto;
import com.param.bloodBank.service.IBloodGroupMatchingMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/api/bloodBank")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BloodGroupMatchingMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodGroupMatchingMasterController.class);


	@Autowired
	private IBloodGroupMatchingMasterService iBloodBagMatchingMasterService;
	
	
	@RequestMapping(value = "/saveBloodGroupMatchingMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveBloodGroupMatchingMaster(@RequestBody List<BloodGroupMatchingMasterDto> bloodGroupMatchingMasterDto) {
		try {
			return iBloodBagMatchingMasterService.saveBloodGroupMatchingMaster(bloodGroupMatchingMasterDto);
		} catch (Exception e) {
			logger.error("saveBloodGroupMatchingMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getBloodGroupMatchingMasterById/{componentId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getBloodGroupMatchingMasterById(@PathVariable ("componentId") Integer componentId,
												@PathVariable ("organizationId") Integer organizationId,
												@PathVariable ("unitId") Integer unitId) {
		try {
			return iBloodBagMatchingMasterService.getBloodGroupMatchingMasterById(componentId, organizationId, unitId);
		} catch (Exception e) {
			logger.error("getBloodGroupMatchingMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBloodGroupsByComponentAndDonorBloodGroupId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBloodGroupsByComponentAndDonorBloodGroupId(@RequestBody BloodGroupMatchingMasterDto bloodGroupMatchingMasterDto) {
		try {
			return iBloodBagMatchingMasterService.getBloodGroupsByComponentAndDonorBloodGroupId(bloodGroupMatchingMasterDto);
		} catch (Exception e) {
			logger.error("getBloodGroupsByComponentAndDonorBloodGroupId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
}
