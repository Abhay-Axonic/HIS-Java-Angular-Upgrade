package com.param.bloodBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.BloodTestResultMasterDto;
import com.param.bloodBank.service.IBloodTestResultMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("global/bloodBank")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BloodTestResultMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodTestResultMasterController.class);


		@Autowired
		IBloodTestResultMasterService iBloodTestResultMasterService;

		@RequestMapping(value = "/saveBloodTestResultMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response saveBloodTestResultMaster(@RequestBody BloodTestResultMasterDto bloodTestResultMasterDto) {
			try {
				return iBloodTestResultMasterService.saveBloodTestResultMaster(bloodTestResultMasterDto);
			} catch (Exception e) {
				logger.error("saveBloodTestResultMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
		
		@RequestMapping(value = "/updateBloodTestResultMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response updateBloodTestResultMaster(@RequestBody BloodTestResultMasterDto bloodTestResultMasterDto) {
			try {
				return iBloodTestResultMasterService.updateBloodTestResultMaster(bloodTestResultMasterDto);
			} catch (Exception e) {
				logger.error("updateBloodTestResultMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
		
		@RequestMapping(value = "/updateBloodTestResultMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response updateBloodTestResultMasterStatus(@RequestBody BloodTestResultMasterDto bloodTestResultMasterDto) {
			try {
				return iBloodTestResultMasterService.updateBloodTestResultMasterStatus(bloodTestResultMasterDto);
			} catch (Exception e) {
				logger.error("updateBloodTestResultMasterStatus failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
		
		@RequestMapping(value = "/getBloodTestResultMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response getBloodTestResultMasterList(@RequestBody BloodTestResultMasterDto bloodTestResultMasterDto) {
			try {
				return iBloodTestResultMasterService.getBloodTestResultMasterList(bloodTestResultMasterDto);
			} catch (Exception e) {
				logger.error("getBloodTestResultMasterList failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
		
		@RequestMapping(value = "/getBloodTestResultMasterById/{bloodTestResultId}/{organizationId}/{unitId}", method = RequestMethod.GET)
		public Response getBloodTestResultMasterById(@PathVariable ("bloodTestResultId") Integer bloodTestResultId,
													@PathVariable ("organizationId") Integer organizationId,
													@PathVariable ("unitId") Integer unitId) {
			try {
				return iBloodTestResultMasterService.getBloodTestResultMasterById(bloodTestResultId, organizationId, unitId);
			} catch (Exception e) {
				logger.error("getBloodTestResultMasterById failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
		
		@RequestMapping(value = "/getActiveBloodTestResultMaster/{organizationId}/{unitId}", method = RequestMethod.GET)
		public Response getActiveBloodTestResultMaster(@PathVariable ("organizationId") Integer organizationId,
													   @PathVariable ("unitId") Integer unitId) {
			try {
				return iBloodTestResultMasterService.getActiveBloodTestResultMaster(organizationId, unitId);
			} catch (Exception e) {
				logger.error("getActiveBloodTestResultMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
		
		@RequestMapping(value = "/getBloodTestResultMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response getBloodTestResultMasterCount(@RequestBody BloodTestResultMasterDto bloodTestResultMasterDto) {
			try {
				return iBloodTestResultMasterService.getBloodTestResultMasterCount(bloodTestResultMasterDto);
			} catch (Exception e) {
				logger.error("getBloodTestResultMasterCount failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	}
