package com.param.CSSD.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.CSSD.dto.PreSterilizationProcessDto;
import com.param.CSSD.service.IPreSterilizationProcessService;
import com.param.CSSDMaster.dto.CssdKitDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping("/cssd")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PreSterilizationProcessController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(PreSterilizationProcessController.class);


	@Autowired
	private IPreSterilizationProcessService iPreSterilizationProcessService;

	@RequestMapping(value = "/saveCleanedWorklist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveCleanedWorklist(@RequestBody PreSterilizationProcessDto preSterilizationProcessDto) {
		try {
			
			Response res=iPreSterilizationProcessService.checkStockAvaiability(preSterilizationProcessDto);
			if(res.getStatus().equals("success")) {
				
			if(preSterilizationProcessDto.getIsInstrument()) {
			 Response resp = iPreSterilizationProcessService.saveInstrumentAsKit(preSterilizationProcessDto);
			 PreSterilizationProcessDto dto = (PreSterilizationProcessDto)(resp.getObject());
			 Response resp1 = iPreSterilizationProcessService.getKitInstrumentId(dto.getKitId(),preSterilizationProcessDto.getKitInstrumentList().get(0).getInstrumentId());
			 Integer kitInstrumentId = (Integer) resp1.getObject();
			 preSterilizationProcessDto.getKitInstrumentList().get(0).setKitInstrumentId(kitInstrumentId);
			 Response res1= iPreSterilizationProcessService.saveCleanedWorklist(preSterilizationProcessDto);
			}else {
				Response res2=iPreSterilizationProcessService.saveCleanedWorklist(preSterilizationProcessDto); 
			}
			return iPreSterilizationProcessService.updateInTransitStock(preSterilizationProcessDto);
		  }else {
			  return res;
		  }
		} catch (Exception e) {
			logger.error("saveCleanedWorklist failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method returns the list of kits.
	 * 
	 * @param from
	 * @param to
	 * @param name
	 * @param status
	 * @return Response
	 */
	@RequestMapping(value = "/getCleanedWorklistByMultipleFields", method = RequestMethod.GET)
	public @ResponseBody Response getCleanedWorklistByMultipleFields(
			@RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date to, @RequestParam(value="name",required=false) String name,
			@RequestParam(value="status",required=false) String status,
			@RequestParam("organizationId") Integer organizationId,
			@RequestParam("unitId") Integer unitId,
			@RequestParam("storeId") Integer storeId) {
		try {
			return iPreSterilizationProcessService.getCleanedWorklistByMultipleFields(from, to, name, status,organizationId,unitId,storeId);
		} catch (Exception e) {
			logger.error("getCleanedWorklistByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * This method returns the list of kits.
	 * 
	 * @param from
	 * @param to
	 * @param name
	 * @param status
	 * @return Response
	 */
	@RequestMapping(value = "/getPendingForCleaningWorklistByMultipleFields", method = RequestMethod.GET)
	public @ResponseBody Response getPendingForCleaningWorklistByMultipleFields(
			@RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date to, @RequestParam(value="name",required=false) String name,
			@RequestParam("organizationId") Integer organizationId,
			@RequestParam("unitId") Integer unitId,
			@RequestParam("storeId") Integer storeId) {
		try {
			return iPreSterilizationProcessService.getPendingForCleaningWorklistByMultipleFields(from, to, name,organizationId,unitId,storeId);
		} catch (Exception e) {
			logger.error("getPendingForCleaningWorklistByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	@RequestMapping(value = "/getInspectedWorklistByMultipleFields", method = RequestMethod.GET)
	public @ResponseBody Response getInspectedWorklistByMultipleFields(
			@RequestParam("from") @DateTimeFormat(pattern = "dd-MM-yyyy") Date from,
			@RequestParam("to") @DateTimeFormat(pattern = "dd-MM-yyyy") Date to, @RequestParam(value="name",required=false) String name,
			@RequestParam(value="status",required=false) String status,
			@RequestParam("organizationId") Integer organizationId,
			@RequestParam("unitId") Integer unitId,
			@RequestParam("storeId") Integer storeId) {
		try {
			return iPreSterilizationProcessService.getInspectedWorklistByMultipleFields(from, to, name, status,organizationId,unitId,storeId);
		} catch (Exception e) {
			logger.error("getInspectedWorklistByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getDisinfectedWorklistByMultipleFields", method = RequestMethod.GET)
	public @ResponseBody Response getDisinfectedWorklistByMultipleFields(
			@RequestParam("from") @DateTimeFormat(pattern = "dd-MM-yyyy") Date from,
			@RequestParam("to") @DateTimeFormat(pattern = "dd-MM-yyyy") Date to, @RequestParam(value="name",required=false) String name,
			@RequestParam(value="status",required=false) String status,
			@RequestParam("organizationId") Integer organizationId,
			@RequestParam("unitId") Integer unitId,
			@RequestParam("storeId") Integer storeId) {
		try {
			return iPreSterilizationProcessService.getDisinfectedWorklistByMultipleFields(from, to, name, status,organizationId,unitId,storeId);
		} catch (Exception e) {
			logger.error("getDisinfectedWorklistByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getPreSterilizationProcessByTransitionNumber", method = RequestMethod.GET)
	public @ResponseBody Response getPreSterilizationProcessByTransitionNumber(@RequestParam("organizationId") Integer organizationId,
			@RequestParam("unitId") Integer unitId,
			@RequestParam("transitionNumber") Integer transitionNumber) {
		try {
			return iPreSterilizationProcessService.getPreSterilizationProcessByTransitionNumber(organizationId,unitId,transitionNumber);
		} catch (Exception e) {
			logger.error("getPreSterilizationProcessByTransitionNumber failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/formkit", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response formKit(@RequestBody PreSterilizationProcessDto preSterilizationProcessDto) {
		try {
			return iPreSterilizationProcessService.formKit(preSterilizationProcessDto);
		} catch (Exception e) {
			logger.error("formKit failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getKitFormationWorklistByMultipleFields", method = RequestMethod.GET)
	public @ResponseBody Response getKitFormationWorklistByMultipleFields(
			@RequestParam("from") @DateTimeFormat(pattern = "dd-MM-yyyy") Date from,
			@RequestParam("to") @DateTimeFormat(pattern = "dd-MM-yyyy") Date to, @RequestParam(value="name",required=false) String name,
			@RequestParam(value="status",required=false) String status,
			@RequestParam("organizationId") Integer organizationId,
			@RequestParam("unitId") Integer unitId,
			@RequestParam("storeId") Integer storeId) {
		try {
			return iPreSterilizationProcessService.getKitFormationWorklistByMultipleFields(from, to, name, status,organizationId,unitId,storeId);
		} catch (Exception e) {
			logger.error("getKitFormationWorklistByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getKitFormationWorklistByTransitionNumber", method = RequestMethod.GET)
	public @ResponseBody Response getKitFormationWorklistByTransitionNumber(@RequestParam("organizationId") Integer organizationId,
			@RequestParam("unitId") Integer unitId,@RequestParam("storeId") Integer storeId,
			@RequestParam("transitionNumber") Integer transitionNumber) {
		try {
			return iPreSterilizationProcessService.getKitFormationWorklistByTransitionNumber(organizationId,unitId,storeId,transitionNumber);
		} catch (Exception e) {
			logger.error("getKitFormationWorklistByTransitionNumber failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/replaceInstrument", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response replaceInstrument(@RequestParam("toBeReplaced")Integer toBeReplaced,@RequestParam("replaceWith")Integer replaceWith) {
		try {
			return iPreSterilizationProcessService.replaceInstrument(toBeReplaced,replaceWith);
		} catch (Exception e) {
			logger.error("replaceInstrument failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/getSterilizedKitsByKitId", method = RequestMethod.GET)
	public @ResponseBody Response getSterilizedKitsByKitId(@RequestParam("organizationId") Integer organizationId,
			@RequestParam("unitId") Integer unitId,@RequestParam("storeId") Integer storeId,
			@RequestParam("kitId") Integer kitId,
			@RequestParam(value="limit",required=false) Integer limit) {
		try {
			return iPreSterilizationProcessService.getSterilizedKitsByKitId(organizationId,unitId,storeId,kitId,limit);
		} catch (Exception e) {
			logger.error("getSterilizedKitsByKitId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	
	/**
	 * This method is used to update the kit.
	 * 
	 * @param preSterilizationProcessDto
	 * @return Response
	 */

	@RequestMapping(value = "/updatePreSterilizationProcess", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updatePreSterilizationProcess(
			@RequestBody PreSterilizationProcessDto preSterilizationProcessDto) {
		try {
			return iPreSterilizationProcessService.updatePreSterilizationProcess(preSterilizationProcessDto);
		} catch (Exception e) {
			logger.error("updatePreSterilizationProcess failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * This method is used for Kit Formation
	 * 
	 * @param kitFormationRequestDto
	 * @return Response
	 */

	

	/**
	 * This method returns the list of kits that are under presterilization
	 * process.
	 * 
	 * @return Response
	 */
	@RequestMapping(value = "/getPreSterilizationProcessList", method = RequestMethod.GET)
	public @ResponseBody Response getPreSterilizationProcessList() {
		try {
			return iPreSterilizationProcessService.getPreSterilizationProcessList();
		} catch (Exception e) {
			logger.error("getPreSterilizationProcessList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/searchPreSterilizationProcessList", method = RequestMethod.GET)
	public @ResponseBody Response searchPreSterilizationProcessList(@RequestParam("name") String name) {
		try {
			return iPreSterilizationProcessService.searchPreSterilizationProcessList(name);
		} catch (Exception e) {
			logger.error("searchPreSterilizationProcessList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * This method returns list of kits pending for inspection.
	 * 
	 * @return Response
	 */
	@RequestMapping(value = "/getKitListPendingForInspection", method = RequestMethod.GET)
	public @ResponseBody Response getKitListPendingForInspection() {
		try {
			return iPreSterilizationProcessService.getKitListPendingForInspection();
		} catch (Exception e) {
			logger.error("getKitListPendingForInspection failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * This method returns list of kits pending for disinfection.
	 * 
	 * @return Response
	 */
	@RequestMapping(value = "/getKitListPendingForDisInfection", method = RequestMethod.GET)
	public @ResponseBody Response getKitListPendingForDisInfection() {
		try {
			return iPreSterilizationProcessService.getKitListPendingForDisInfection();
		} catch (Exception e) {
			logger.error("getKitListPendingForDisInfection failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * This method returns list of kits pending for formation.
	 * 
	 * @return Response
	 */
	@RequestMapping(value = "/getKitListPendingForKitFormation", method = RequestMethod.GET)
	public @ResponseBody Response getKitListPendingForKitFormation() {
		try {
			return iPreSterilizationProcessService.getKitListPendingForKitFormation();
		} catch (Exception e) {
			logger.error("getKitListPendingForKitFormation failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * This method returns list of formedKits.
	 * 
	 * @return Response
	 */
	@RequestMapping(value = "/getFormedKitList", method = RequestMethod.GET)
	public @ResponseBody Response getFormedKitList() {
		try {
			return iPreSterilizationProcessService.getFormedKitList();
		} catch (Exception e) {
			logger.error("getFormedKitList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/**
	 * This method returns the list of kits that are under presterilization
	 * process.
	 * 
	 * @return Response
	 */
	@RequestMapping(value = "/getPreSterilizationProcessKitById/{kitID}", method = RequestMethod.GET)
	public @ResponseBody Response getPreSterilizationProcessKitById(@PathVariable("kitID") Integer kitId) {
		try {
			return iPreSterilizationProcessService.getPreSterilizationProcessKitById(kitId);
		} catch (Exception e) {
			logger.error("getPreSterilizationProcessKitById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	@RequestMapping(value = "/getCssdStoreList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCssdStoreList(@RequestBody PreSterilizationProcessDto preSterilizationProcessDto) {
		try {
			return iPreSterilizationProcessService.getCssdStoreList(preSterilizationProcessDto);
		} catch (Exception e) {
			logger.error("getCssdStoreList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	

	@RequestMapping(value = "/searchSterilizedKitByKitName", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSterilizedKitByKitName(@RequestBody CssdKitDto kitDto) {
		try {
			return iPreSterilizationProcessService.getSterilizedKitByKitName(kitDto);
		} catch (Exception e) {
			logger.error("getSterilizedKitByKitName failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getSterilizedKitBatchList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSterilizedKitBatchList(@RequestBody CssdKitDto kitDto) {
		try {
			return iPreSterilizationProcessService.getSterilizedKitBatchList(kitDto);
		} catch (Exception e) {
			logger.error("getSterilizedKitBatchList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
