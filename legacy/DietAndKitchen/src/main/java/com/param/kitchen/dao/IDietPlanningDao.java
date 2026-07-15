package com.param.kitchen.dao;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.kitchen.dto.DietPlanningDto;

import java.text.ParseException;

public interface IDietPlanningDao extends ICommonConstants {
	
	@SuppressWarnings("rawtypes")
	public Response saveDietPlanning(DietPlanningDto dietPlanningDto) throws ParseException;
	
	/*@SuppressWarnings("rawtypes")
	public Response getAllDietPlanning();
	*/
	
	/*@SuppressWarnings("rawtypes")
	public Response getDietPlanningByPatientId(Integer patientId);*/
	
	/*@SuppressWarnings("rawtypes")
	public Response updateDietPlanning(DietPlanningDto dietPlanningDto);
*/
	/*@SuppressWarnings("rawtypes")
	public Response deleteDietPlanning(Integer patientId);*/
	
	@SuppressWarnings("rawtypes")
	public Response getDietPreferenceByPatientId(Integer patientId);
	

}
