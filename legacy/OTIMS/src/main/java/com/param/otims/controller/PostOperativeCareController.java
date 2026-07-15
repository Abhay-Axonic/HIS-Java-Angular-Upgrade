package com.param.otims.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.PostOperativeCareDto;
import com.param.otims.service.IPostOperativeCareService;

@RestController
@RequestMapping(value = "/api/otims/PostOperativeCareDetails")
@SuppressWarnings({"rawtypes"})
public class PostOperativeCareController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(PostOperativeCareController.class);

	@Autowired
	IPostOperativeCareService iPostOperativeCareService;


	@RequestMapping(value = "/savePostOperativeCare" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response savePostOperativeCare(@RequestBody PostOperativeCareDto postOperativeCareDto){
		try{
			Response res =  iPostOperativeCareService.savePostOperativeCare(postOperativeCareDto);
			if(postOperativeCareDto.getPostOperativeCareId()!=null) {
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null,res.getObject());
			}else {
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null,res.getObject());
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}


	@RequestMapping(value = "/getNotesListByProReqId/{orgId}/{unitId}/{proReqId}", method = RequestMethod.GET)
	public Response getNotesListByProConfigId(@PathVariable("orgId")Integer orgId ,
			@PathVariable("unitId")Integer unitId , @PathVariable("proReqId")Integer proReqId ) {
		try {
			return iPostOperativeCareService.getNotesListByProConfigId(orgId,unitId,proReqId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
}
