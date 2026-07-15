package com.param.kitchen.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.kitchen.dto.DietOrderRequestDto;
import com.param.kitchen.service.IDietOrderService;


@RestController
@RequestMapping(value = "/api/kitchen/dietOrder")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DietOrderController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(DietOrderController.class);

	@Autowired
	private IDietOrderService iDietOrderService;
	
	/**
	 * This method returns the list of orders for particular meal type for particular date. 
	 * @param dietDate
	 * @param floorId
	 * @param wardId
	 * @param mealId
	 * @return Response
	 */
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Response getDietOrderByMultipleFields(@RequestParam(value="dietDate") String dietDate,
			@RequestParam(value="floorId", required=false) Integer floorId,@RequestParam(value="wardId",required=false ) Integer wardId,
			@RequestParam(value="mealId",required=false) Integer mealId,
			@RequestParam(value="organizationId") Integer organizationId,
			@RequestParam(value="unitId") Integer unitId) {
		try {
			DietOrderRequestDto dietOrderRequestDto=new DietOrderRequestDto();
			dietOrderRequestDto.setDietDate(dietDate);
			dietOrderRequestDto.setFloorId(floorId);
			dietOrderRequestDto.setWardId(wardId);
			dietOrderRequestDto.setMealId(mealId);
			return iDietOrderService.getDietOrderByMultipleFields(dietOrderRequestDto,organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value="/getPatientDetailsByPatientId",method = RequestMethod.GET)
	public @ResponseBody Response getPatientDetailsByPatientId(@RequestParam(value="patientId") Integer patientId,
			@RequestParam(value="organizationId") Integer organizationId,@RequestParam(value="unitId") Integer unitId ) {
		try {
			return iDietOrderService.getPatientDetailsByPatientId(patientId,organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
