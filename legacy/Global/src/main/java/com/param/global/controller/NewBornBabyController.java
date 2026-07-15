package com.param.global.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.BabyStillAssmntDto;
import com.param.global.dto.NewBornApgarDto;
import com.param.global.dto.NewBornAssessmentDto;
import com.param.global.dto.NewBornBabyDto;
import com.param.global.service.INewBornApgarService;
import com.param.global.service.INewBornAssessmentService;
import com.param.global.service.INewBornBabyDisplayService;
import com.param.global.service.INewBornBabyService;


@Controller
@RequestMapping("/adt")
@SuppressWarnings({"rawtypes","unchecked"})
public class NewBornBabyController implements ICommonConstants
{

	final static Logger logger = Logger.getLogger(NewBornBabyController.class);
	@Autowired
	private INewBornBabyService iNewBornBabyService;
	
	@Autowired
	private INewBornBabyDisplayService iNewBornBabyDisplayService;
	
	@Autowired
	private INewBornAssessmentService iAssessmentService;
	
	@Autowired
	private INewBornApgarService iApgarService;
	
	
	@RequestMapping(value="/saveNewBornBabyReg" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveNewBornBabyReg(@RequestBody NewBornBabyDto newBornBabyDto){
		try{
			return iNewBornBabyService.saveNewBornBabyReg(newBornBabyDto);
		}catch(Exception e){
			logger.error("Error while saving new born baby registration", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getBabyDetailsByParentId", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBabyDetailsByParentId(@RequestBody NewBornBabyDto newBornBabyDto){
		try{
			return iNewBornBabyService.getBabyDetailsByParentId(newBornBabyDto);
		}catch(Exception e){
			logger.error("Error while fetching baby details by parent id", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/saveStillBirthAssessment" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveStillBirthAssessment(@RequestBody BabyStillAssmntDto babyStillAssmntDto){
		try{
			return iNewBornBabyDisplayService.saveStillBirthAssessment(babyStillAssmntDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getStillBabyDetails", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getStillBabyDetails(@RequestBody BabyStillAssmntDto babyStillAssmntDto){
		try{
			return iNewBornBabyDisplayService.getStillBabyDetails(babyStillAssmntDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getSingleStillBaby", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSingleStillBaby(@RequestBody BabyStillAssmntDto babyStillAssmntDto){
		try{
			return iNewBornBabyDisplayService.getSingleStillBaby(babyStillAssmntDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateStillBirthAssessment" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStillBirthAssessment(@RequestBody BabyStillAssmntDto babyStillAssmntDto){
		try{
			return iNewBornBabyDisplayService.updateStillBirthAssessment(babyStillAssmntDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/saveNewBornAssessment" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveNewBornAssessment(@RequestBody NewBornAssessmentDto dto){
		try{
			return iAssessmentService.saveNewBornAssessment(dto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getNewBornAssessmentList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getNewBornAssessmentDetails(@RequestBody NewBornAssessmentDto dto){
		try{
			return iAssessmentService.getNewBornAssessmentDetails(dto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateNewBornAssessment" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateNewBornAssessment(@RequestBody NewBornAssessmentDto dto){
		try{
			return iAssessmentService.updateNewBornAssessment(dto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	
	@RequestMapping(value="/getNewBornApgarList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getNewBornApgarList(@RequestBody NewBornApgarDto dto){
		try{
			return iApgarService.getNewBornApgarList(dto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateNewBornApgar", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateNewBornApgar(@RequestBody NewBornApgarDto dto){
		try{
			return iApgarService.updateNewBornApgar(dto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/saveSingleApgarScore", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveSingleApgarScore(@RequestBody NewBornApgarDto dto){
		try{
			return iApgarService.saveSingleApgarScore(dto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	
	
	
}
