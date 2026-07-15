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
import com.param.kitchen.dto.RejectedDietListRequestDto;
import com.param.kitchen.service.IRejectedDietListService;


@RestController
@RequestMapping(value = "/api/kitchen/rejectedDietList")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RejectedDietListController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(RejectedDietListController.class);

	@Autowired
	private IRejectedDietListService iRejectedDietListService;
	
	/**
	 * This method is used to reissue a diet order.
	 * @param dietOrderDto
	 * @return Response
	 */
	@RequestMapping(method = RequestMethod.POST )
	public @ResponseBody Response reIssueRejectedDietOrderByDietOrderId(@RequestBody List<Integer> orderIds) {
		try {
			return iRejectedDietListService.reIssueRejectedDietOrderByDietOrderId(orderIds);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	/**
	 * This method returns the list of diet orders that have been rejected and are ready to reissue.
	 * @param dietDate
	 * @param floorId
	 * @param wardId
	 * @param mealId
	 * @return Response
	 */
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Response getRejectedDietListByMultipleFields(@RequestParam(value="dietDate") String dietDate,
			@RequestParam(value="floorId", required=false) Integer floorId,@RequestParam(value="wardId",required=false ) Integer wardId,
			@RequestParam(value="mealId",required=false) Integer mealId,
			@RequestParam(value="organizationId") Integer organizationId,
			@RequestParam(value="unitId") Integer unitId) {
		try {
			RejectedDietListRequestDto rejectedDietListRequestDto=new RejectedDietListRequestDto();
			rejectedDietListRequestDto.setDietDate(dietDate);
			rejectedDietListRequestDto.setFloorId(floorId);
			rejectedDietListRequestDto.setWardId(wardId);
			rejectedDietListRequestDto.setMealId(mealId);
			return iRejectedDietListService.getRejectedDietListByMultipleFields(rejectedDietListRequestDto,organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value="/getByDate",method = RequestMethod.GET)
	public @ResponseBody Response getRejectedDietListByOnlyDate(@RequestParam(value="dietDate") String dietDate,
			@RequestParam(value="organizationId") Integer organizationId,
			@RequestParam(value="unitId") Integer unitId) {
		try {
			RejectedDietListRequestDto rejectedDietListRequestDto=new RejectedDietListRequestDto();
			rejectedDietListRequestDto.setDietDate(dietDate);
			return iRejectedDietListService.getRejectedDietListByOnlyDate(rejectedDietListRequestDto,organizationId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
