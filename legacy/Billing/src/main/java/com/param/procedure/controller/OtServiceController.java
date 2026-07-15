package com.param.procedure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.procedure.service.IOtServicesService;

@RestController
@RequestMapping(value = "/api/procedure")
@SuppressWarnings("rawtypes")
public class OtServiceController implements ICommonConstants{
	
	@Autowired
	private IOtServicesService iOtServicesService;
	
	/**
	 * TO GET OT SERVICES BY PROCEDURE REQUEST ID
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/otservice/proreq/{id}")
	public Response getOtServicesByProReqId(@PathVariable("id") int id){
		try{
			return iOtServicesService.getOtServicesByProReqId(id);
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
}
