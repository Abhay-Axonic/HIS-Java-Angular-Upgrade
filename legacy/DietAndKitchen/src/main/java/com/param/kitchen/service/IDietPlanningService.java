package com.param.kitchen.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.kitchen.dto.DietPlanningDto;

@SuppressWarnings("rawtypes")
public interface IDietPlanningService {
	
	
	/**
	 * This method is used to save diet planning.
	 * @param dietPlanningDto
	 * @return Response
	 */
	@Transactional
	public Response saveDietPlanning(DietPlanningDto dietPlanningDto);
	
	
	/**
	 * This method returns the list of all diet plannings.
	 * @return Response
	 */
	/*@Transactional
	public Response getAllDietPlanning();*/
	
	
	/**
	 * This method returns the diet planning for particular patient.
	 * @param patientId
	 * @return Response
	 */
	/*@Transactional
	public Response getDietPlanningByPatientId(Integer patientId);*/
	
	/**
	 * This method is used to update diet planning.
	 * @param dietPlanningDto
	 * @return Response
	 */
	/*@Transactional
	public Response updateDietPlanning(DietPlanningDto dietPlanningDto);*/
	
	/**
	 * This method is used to delete particular diet planning by patient id.
	 * @param patientId
	 * @return Response
	 */
	/*@Transactional
	public Response deleteDietPlanning(Integer patientId);
	*/
	
	@Transactional
	public Response getDietPreferenceByPatientId(Integer patientId);
}
