package com.param.kitchen.controller;

import java.util.List;

import com.param.kitchen.dto.DietIssueDto;
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
import com.param.kitchen.dto.DietIssueRequestDto;
import com.param.kitchen.dto.DietOrderDto;
import com.param.kitchen.service.IDietIssueService;


@RestController
@RequestMapping(value = "/api/kitchen/dietIssue")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DietIssueController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(DietIssueController.class);

	@Autowired
	private IDietIssueService iDietIssueService;
	
	/**
	 * This method is used to issue a particular diet order.
	 * 
	 * @param dietOrderDto
	 * @return Response
	 */
	@RequestMapping(method = RequestMethod.POST )
	public @ResponseBody Response issueDietOrderByDietOrderId(@RequestBody List<Integer> dietOrderIdList) {
		try {
			return iDietIssueService.issueDietOrderByDietOrderList(dietOrderIdList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Response getDietIssueByMultipleFields(@RequestParam(value="dietDate") String dietDate,
			@RequestParam(value="floorId", required=false) Integer floorId,@RequestParam(value="wardId",required=false ) Integer wardId,
			@RequestParam(value="mealId",required=false) Integer mealId,
			@RequestParam(value="organizationId") Integer organizationId,
			@RequestParam(value="unitId") Integer unitId) {
		try {
			DietIssueRequestDto dietIssueRequestDto=new DietIssueRequestDto();
			dietIssueRequestDto.setDietDate(dietDate);
			dietIssueRequestDto.setFloorId(floorId);
			dietIssueRequestDto.setWardId(wardId);
			dietIssueRequestDto.setMealId(mealId);
			return iDietIssueService.getDietIssueByMultipleFields(dietIssueRequestDto,organizationId,unitId);
		} catch (Exception e) {
			throw e;
		}
//		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value="/getByDate",method = RequestMethod.GET)
	public @ResponseBody Response getDietIssueByOnlyDate(@RequestParam(value="dietDate") String dietDate,
			@RequestParam(value="organizationId") Integer organizationId,
			@RequestParam(value="unitId") Integer unitId) {
		try {
			DietIssueRequestDto dietIssueRequestDto=new DietIssueRequestDto();
			dietIssueRequestDto.setDietDate(dietDate);
			return iDietIssueService.getDietIssueByOnlyDate(dietIssueRequestDto,organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/updateStatus", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatus(@RequestBody DietIssueDto dietIssueDto) {
		try {
			return iDietIssueService.updateStatus(dietIssueDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
}
