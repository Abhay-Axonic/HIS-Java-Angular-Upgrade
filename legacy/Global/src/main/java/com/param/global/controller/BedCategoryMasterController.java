package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.BedCategoryMasterDto;
import com.param.global.service.IBedCategoryMasterService;

import java.util.ArrayList;
@Controller
@RequestMapping("/adt")
@SuppressWarnings({"rawtypes","unchecked"})
public class BedCategoryMasterController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(BedCategoryMasterController.class);

	@Autowired
	IBedCategoryMasterService iBedCategoryMaster;

	@RequestMapping(value="/saveBedCategoryMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveBedCategoryMaster(@RequestBody BedCategoryMasterDto BedCategoryMasterDto){
		try{

			return iBedCategoryMaster.addBedCategoryMaster(BedCategoryMasterDto);
		}catch(Exception e){
			logger.error("Error while saving the bed category master", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getBedCategoryMasterList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBedCategoryMasterList(@RequestBody BedCategoryMasterDto BedCategoryMasterDto){
		try{
			return iBedCategoryMaster.getBedCategoryMasterList(BedCategoryMasterDto);
		}catch(Exception e){
			logger.error("Error while fetching the bed category master list", e);
		}
		return new Response(ERROR, null, null, null,null);
	}


	@RequestMapping(value="/updateBedCategoryMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateBedCategoryMaster(@RequestBody BedCategoryMasterDto BedCategoryMasterDto){
		try{
			return iBedCategoryMaster.updateBedCategoryMaster(BedCategoryMasterDto);
		}catch(Exception e){
			logger.error("Error while updating the bed category master", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getBedCategoryById" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBedCategoryById(@RequestBody BedCategoryMasterDto BedCategoryMasterDto){
		try{

			return iBedCategoryMaster.getBedCategoryById(BedCategoryMasterDto);
		}catch(Exception e){
			logger.error("Error while fetching the bed category by id", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/updateStatusForBedCategory" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusForBedCategory(@RequestBody BedCategoryMasterDto BedCategoryMasterDto){
		try{

			return iBedCategoryMaster.updateStatusForBedCategory(BedCategoryMasterDto);
		}catch(Exception e){
			logger.error("Error while updating the status for bed category", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getActiveBedCategoryList", method = RequestMethod.GET)
	public @ResponseBody Response getActiveCityList(){
		try{
			return iBedCategoryMaster.getActiveBedCategoryList();
		}catch(Exception e){
			logger.error("Error while fetching the active bed category list", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getBedCategoryCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBedCategoryCount(@RequestBody BedCategoryMasterDto BedCategoryMasterDto){
		try{

			return iBedCategoryMaster.getBedCategoryCount(BedCategoryMasterDto);
		}catch(Exception e){
			logger.error("Error while fetching the bed category count", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/bedCategory/availableBeds" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAvailableBedCategoryAccordingToVacateBeds(@RequestBody BedCategoryMasterDto bedCategoryMasterDto){
		try{
			return iBedCategoryMaster.getAvailableBedCategoryAccordingToVacateBeds(bedCategoryMasterDto);

		}catch(Exception e){
			logger.error("Error while fetching the available bed category according to vacate beds", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/updateBedCategoryMaster/updateHierarchy" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateBedCategoryMasterHierarchy(@RequestBody BedCategoryMasterDto BedCategoryMasterDto){
		try{
			return iBedCategoryMaster.updateBedCategoryMasterHierarchy(BedCategoryMasterDto);
		}catch(Exception e){
			logger.error("Error while updating the bed category master hierarchy", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
}
