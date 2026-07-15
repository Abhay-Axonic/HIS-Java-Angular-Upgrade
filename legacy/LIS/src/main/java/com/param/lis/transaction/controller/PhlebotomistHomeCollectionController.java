package com.param.lis.transaction.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.Response;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.transaction.dto.PhlebotomistHomeCollectionDto;
import com.param.lis.transaction.service.IPhlebotomistHomeCollectionservice;

@RestController
@RequestMapping(value = "/LIS/PhlebotomistHomeCollection")
@SuppressWarnings("rawtypes")
public class PhlebotomistHomeCollectionController implements ICommonConstants, IError{

	@Autowired
	IPhlebotomistHomeCollectionservice iPhlebotomistHomeCollectionservice;
	
	
	
	@RequestMapping(value = "/save", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response savePhlebotomistHomeCollection(@RequestBody List<PhlebotomistHomeCollectionDto> list){
		try{
			return iPhlebotomistHomeCollectionservice.savePhlebotomistHomeCollection(list);
		}catch(Exception e){
			
			throw e;
		}
	}	
	
	
	@RequestMapping(value = "/validateUserAlocation", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response validateUserAlocation(@RequestBody PhlebotomistHomeCollectionDto dto){
		try{
			return iPhlebotomistHomeCollectionservice.validateUserAlocation(dto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
}
