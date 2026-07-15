package com.param.CSSDMaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.CSSDMaster.dto.KitMasterDto;
import com.param.CSSDMaster.dto.WrapperExpiryMasterDto;
import com.param.CSSDMaster.service.IKitMasterService;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping("/cssd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CssdKitMasterController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(CssdKitMasterController.class);


	@Autowired
	private IKitMasterService iKitMasterService;

	@RequestMapping(value = "/updateKit", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateResponseBody(@RequestBody KitMasterDto MasterDto) {
		try {
			return iKitMasterService.updateMaster(MasterDto);
		} catch (Exception e) {
			logger.error("updateResponseBody failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateKitStatus", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateKitStatus(@RequestBody KitMasterDto MasterDto) {
		try {
			return iKitMasterService.updateKitStatus(MasterDto);
		} catch (Exception e) {
			logger.error("updateKitStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/createKit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response createKit(@RequestBody KitMasterDto kitMasterDto) {
		try {
			return iKitMasterService.createMaster(kitMasterDto);
		} catch (Exception e) {
			logger.error("createKit failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getAllKitsList", method = RequestMethod.GET)
	public @ResponseBody Response getKitsList(@RequestParam(value="organizationId")Integer organizationId,
			@RequestParam(value="unitId")Integer unitId,
			@RequestParam(value="offset")Integer offset,
			@RequestParam(value="noOfRecordsPerPage")Integer noOfRecordsPerPage
			) {
		try {
			return iKitMasterService.getAllMasterList(organizationId,unitId,offset,noOfRecordsPerPage);
		} catch (Exception e) {
			logger.error("getKitsList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getAllSurgeonList", method = RequestMethod.GET)
	public @ResponseBody Response getAllSurgeonList(@RequestParam(value="organizationId")Integer organizationId,
			@RequestParam(value="unitId")Integer unitId) {
		try {
			return iKitMasterService.getAllSurgeonList(organizationId,unitId);
		} catch (Exception e) {
			logger.error("getAllSurgeonList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getActiveKitsList", method = RequestMethod.GET)
	public @ResponseBody Response getActiveKitsList(@RequestParam(value="organizationId")Integer organizationId,
			@RequestParam(value="unitId")Integer unitId,@RequestParam(value="storeId")Integer storeId) {
		try {
			return iKitMasterService.getActiveMasterList(organizationId,unitId,storeId);
		} catch (Exception e) {
			logger.error("getActiveKitsList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getKitWithEquipments", method = RequestMethod.GET)
	public @ResponseBody Response getKitWithEquipments(@RequestParam(value="organizationId")Integer organizationId,
			@RequestParam(value="unitId")Integer unitId,@RequestParam(value="storeId")Integer storeId,@RequestParam("kitId") Integer kitId) {
		try {
			return iKitMasterService.getKitWithEquipments(organizationId,unitId,storeId,kitId);
		} catch (Exception e) {
			logger.error("getKitWithEquipments failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/searchKitByKitName", method = RequestMethod.GET)
	public @ResponseBody Response searchKitByKitName(@RequestParam(value="organizationId")Integer organizationId,
			@RequestParam(value="unitId")Integer unitId,@RequestParam("name") String name) {
		try {
			return iKitMasterService.searchKitByKitName(organizationId,unitId,name);
		} catch (Exception e) {
			logger.error("searchKitByKitName failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/searchInstrumentByKitName", method = RequestMethod.GET)
	public @ResponseBody Response searchInstrumentByKitName(@RequestParam(value="organizationId")Integer organizationId,
			@RequestParam(value="unitId")Integer unitId,@RequestParam(value="storeId")Integer storeId,@RequestParam("name") String name) {
		try {
			return iKitMasterService.searchInstrumentByKitName(organizationId,unitId,storeId,name);
		} catch (Exception e) {
			logger.error("searchInstrumentByKitName failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/searchInstrumentAsKit", method = RequestMethod.GET)
	public @ResponseBody Response searchInstrumentAsKit(@RequestParam(value="organizationId")Integer organizationId,
			@RequestParam(value="unitId")Integer unitId,@RequestParam("name") String name) {
		try {
			return iKitMasterService.searchInstrumentAsKit(organizationId,unitId,name);
		} catch (Exception e) {
			logger.error("searchInstrumentAsKit failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/searchInstrument", method = RequestMethod.GET)
	public @ResponseBody Response searchInstrumentByName(@RequestParam(value="organizationId")Integer organizationId,
			@RequestParam(value="unitId")Integer unitId,@RequestParam(value="storeId")Integer storeId,@RequestParam("name") String name) {
		try {
			return iKitMasterService.searchInstrumentByName(organizationId,unitId,storeId,name);
		} catch (Exception e) {
			logger.error("searchInstrumentByName failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getAllInstrumentList", method = RequestMethod.GET)
	public @ResponseBody Response getAllInstrumentList(@RequestParam(value="organizationId")Integer organizationId,
			@RequestParam(value="unitId")Integer unitId) {
		try {
			return iKitMasterService.getAllInstrumentList(organizationId,unitId);
		} catch (Exception e) {
			logger.error("getAllInstrumentList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/searchInstrumentByMultipleFields", method = RequestMethod.GET)
	public @ResponseBody Response searchInstrumentByMultipleFields(@RequestParam(value="code",required=false)String code,
			@RequestParam(value="name",required=false)String name,
			@RequestParam(value="instrumentNo",required=false) Integer instrumentNo,
			@RequestParam(value="organizationId")Integer organizationId,
			@RequestParam(value="unitId")Integer unitId,
			@RequestParam(value="storeId")Integer storeId) {
		try {
			return iKitMasterService.searchInstrumentByMultipleFields(code,name,instrumentNo,organizationId,unitId,storeId);
		} catch (Exception e) {
			logger.error("searchInstrumentByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getKitListcount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getKitListcount(
			@RequestBody KitMasterDto kitMasterDto) {
		try {
			return iKitMasterService.getKitListcount(kitMasterDto);
		} catch (Exception e) {
			logger.error("getKitListcount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getEquipmentList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getEquipmentList(
			@RequestBody KitMasterDto kitMasterDto) {
		try {
			return iKitMasterService.getEquipmentList(kitMasterDto);
		} catch (Exception e) {
			logger.error("getEquipmentList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
