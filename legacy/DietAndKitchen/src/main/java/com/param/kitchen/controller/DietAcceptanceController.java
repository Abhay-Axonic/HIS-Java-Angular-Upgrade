package com.param.kitchen.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.master.dto.DishDto;
import com.param.kitchen.dto.DietAcceptanceRequestDto;
import com.param.kitchen.dto.DietOrderDto;
import com.param.kitchen.dto.PatientDetailsDto;
import com.param.kitchen.service.IDietAcceptanceService;



@RestController
@RequestMapping(value = "/api/kitchen/dietAcceptance")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DietAcceptanceController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(DietAcceptanceController.class);

	@Autowired
	private IDietAcceptanceService iDietAcceptanceService;
	
	
	/**
	 * This method is used to accept or reject the diet order according to the value set for
	 * accept parameter in the request object.
	 * e.g. "accept":true will accept the particular diet and "accept":false will reject the 
	 * particular diet. 
	 * @param dietOrderDto
	 * @return Response
	 */
	@RequestMapping(method = RequestMethod.POST )
	public @ResponseBody Response acceptDietOrderByDietOrderId(@RequestBody List<PatientDetailsDto> patientDetailsList) {
		try {
			return iDietAcceptanceService.acceptDietOrderByDietOrderId(patientDetailsList);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	/**
	 * This method returns the list of diet orders that are issued and ready for acceptance.
	 * 
	 * @param dietDate
	 * @param floorId
	 * @param wardId
	 * @param mealId
	 * @return Response
	 */
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Response getDietAcceptanceByMultipleFields(@RequestParam(value="dietDate") String dietDate,
			@RequestParam(value="floorId", required=false) Integer floorId,@RequestParam(value="wardId",required=false ) Integer wardId,
			@RequestParam(value="mealId",required=false) Integer mealId, @RequestParam(value="transition",required=false) Integer transition,
			@RequestParam(value="organizationId") Integer organizationId, @RequestParam(value="unitId") Integer unitId) {
		try {
			DietAcceptanceRequestDto dietAcceptanceRequestDto=new DietAcceptanceRequestDto();
			dietAcceptanceRequestDto.setDietDate(dietDate);
			dietAcceptanceRequestDto.setFloorId(floorId);
			dietAcceptanceRequestDto.setWardId(wardId);
			dietAcceptanceRequestDto.setMealId(mealId);
			dietAcceptanceRequestDto.setTransition(transition);
			return iDietAcceptanceService.getDietAcceptanceByMultipleFields(dietAcceptanceRequestDto,organizationId,unitId );
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/getByDate",method = RequestMethod.GET)
	public @ResponseBody Response getDietAcceptanceByOnlyDate(@RequestParam(value="dietDate") String dietDate,
			@RequestParam(value="organizationId") Integer organizationId, @RequestParam(value="unitId") Integer unitId) {
		try {
			DietAcceptanceRequestDto dietAcceptanceRequestDto=new DietAcceptanceRequestDto();
			dietAcceptanceRequestDto.setDietDate(dietDate);
			return iDietAcceptanceService.getDietAcceptanceByOnlyDate(dietAcceptanceRequestDto,organizationId,unitId );
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
