package com.param.bloodBank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.param.bloodBank.dto.CampMasterDto;
import com.param.bloodBank.service.ICampMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("/global/bloodBank")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CampMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(CampMasterController.class);


	@Autowired
	ICampMasterService iCampMasterService;

	@RequestMapping(value = "/saveCampMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveCampMaster(@RequestBody CampMasterDto campMasterDto) {
		try {
			return iCampMasterService.saveCampMaster(campMasterDto);
		} catch (Exception e) {
			logger.error("saveCampMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getCampMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCampMasterList(@RequestBody CampMasterDto campMasterDto) {
		try {
			return iCampMasterService.getCampMasterList(campMasterDto);
		} catch (Exception e) {
			logger.error("getCampMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateCampMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateCampMasterStatus(@RequestBody CampMasterDto campMasterDto) {
		try {
			return iCampMasterService.updateCampMasterStatus(campMasterDto);
		} catch (Exception e) {
			logger.error("updateCampMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateCampMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateCampMaster(@RequestBody CampMasterDto campMasterDto) {
		try {
			return iCampMasterService.updateCampMaster(campMasterDto);
		} catch (Exception e) {
			logger.error("updateCampMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getCampMasterListPaginationList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCampMasterListPaginationList(@RequestBody CampMasterDto campMasterDto) {

		try {
			return iCampMasterService.getCampMasterListPaginationList(campMasterDto);

		} catch (Exception e) {
			logger.error("getCampMasterListPaginationList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getCampMasterListCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCampMasterListCount(@RequestBody CampMasterDto campMasterDto) {
		
		try {
			return iCampMasterService.getCampMasterListCount(campMasterDto);
			
		} catch (Exception e) {
			logger.error("getCampMasterListCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
