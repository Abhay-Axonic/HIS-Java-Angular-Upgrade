package com.param.kitchen.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.kitchen.dto.DietPlanningDto;
import com.param.kitchen.service.IDietPlanningService;


@RestController
@RequestMapping(value = "/api/kitchen/dietPlanning")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DietPlanningController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(DietPlanningController.class);

	@Autowired
	private IDietPlanningService iDietPlanningService; 
	
	
	/**
	 * This method is used to save diet planning.
	 * @param dietPlanningDto
	 * @return Response
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveDietPlanning(@RequestBody DietPlanningDto dietPlanningDto) {
		try {
			return iDietPlanningService.saveDietPlanning(dietPlanningDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value="getDietPreferenceByPatientId",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDietPreferenceByPatientId(@RequestParam("patientId") Integer patientId ) {
		try {
			return iDietPlanningService.getDietPreferenceByPatientId(patientId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	
	
	
	
	/**
	 * This method returns the list of all diet plannings.
	 * @return Response
	 */

	/*@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAllDietPlanning() {
		try {
			return iDietPlanningService.getAllDietPlanning();
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}*/
	
	
	/**
	 * This method returns the diet planning for particular patient.
	 * @param patientId
	 * @return Response
	 */
	
	/*@RequestMapping(value="/{patientId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDietPlanningByPatientId(@PathVariable("patientId") Integer patientId) {
		try {
			return iDietPlanningService.getDietPlanningByPatientId(patientId);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}*/
	
	
	/**
	 * This method is used to update diet planning.
	 * @param dietPlanningDto
	 * @return Response
	 */
	
	/*@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateDietPlanning(@RequestBody DietPlanningDto dietPlanningDto) {
		try {
			return iDietPlanningService.updateDietPlanning(dietPlanningDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}*/
	
	
	/**
	 * This method is used to delete particular diet planning by patient id.
	 * @param patientId
	 * @return Response
	 */
	
	/*@RequestMapping(value="/{patientId}",method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response deleteDietPlanning(@PathVariable("patientId") Integer patientId ) {
		try {
			return iDietPlanningService.deleteDietPlanning(patientId);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}*/
	
	
	
	
}
