package com.param.global.nabhprocess.controller;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.nabhprocess.dto.NabhProcessMasterDto;
import com.param.global.nabhprocess.service.INabhProcessMasterService;

@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping("/api/unit")
public class NabhProcessMasterController implements ICommonConstants {
	private static final Logger logger = Logger.getLogger(NabhProcessMasterController.class);

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired 
	private INabhProcessMasterService iNabhProcessMasterService;
	
	@RequestMapping(value = "/saveNabhProcessMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveNabhProcessMaster(@RequestBody NabhProcessMasterDto nabhProcessMasterDto){
		try{
			return iNabhProcessMasterService.saveNabhProcessMaster(nabhProcessMasterDto);
			
		}catch(Exception e){
			logger.error("saveNabhProcessMaster failed", e);
			String errorMsg = NabhProcessMasterController.class.getName()+" :"+e.getMessage();
			return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
		}
	}
//	@RequestMapping(value = "/updateIncidentReportMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
//	public Response updateNabhProcessMaster(@RequestBody NabhProcessMasterDto nabhProcessMasterDto){
//		try{
//			return iNabhProcessMasterService.updateNabhProcessMaster(nabhProcessMasterDto);
//			
//		}catch(Exception e){
//			
//			return new Response<>(ERROR, null, null, null, null);
//		}
//	}
	
	@RequestMapping(value = "/getNabhProcessMasterById" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getNabhProcessMasterById(@RequestBody NabhProcessMasterDto nabhProcessMasterDto){
		try{
			return iNabhProcessMasterService.getNabhProcessMasterById(nabhProcessMasterDto);

		}catch(Exception e){
			logger.error("getNabhProcessMasterById failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/getNabhProcessMasterList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getNabhProcessMasterList(@RequestBody NabhProcessMasterDto nabhProcessMasterDto){
		try{
			return iNabhProcessMasterService.getNabhProcessMasterList(nabhProcessMasterDto);
			
		}catch(Exception e){
			logger.error("getNabhProcessMasterList failed", e);
			String errorMsg = NabhProcessMasterController.class.getName()+" :"+e.getMessage();
			return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
		}
	}
//
//	@RequestMapping(value = "/getTemplateByIncidentReportId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
//	public Response getTemplateByNabhProcessId(@RequestBody NabhProcessMasterDto nabhProcessMasterDto){
//		try{
//			return iNabhProcessMasterService.getTemplateByNabhProcessId(nabhProcessMasterDto);
//			
//		}catch(Exception e){
//			
//			return new Response<>(ERROR, null, null, null, null);
//		}
//	}
//	
//	@RequestMapping(value = "/getRoleByIncidentReportId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
//	public Response getRoleByNabhProcessId(@RequestBody NabhProcessMasterDto nabhProcessMasterDto){
//		try{
//			return iNabhProcessMasterService.getRoleByNabhProcessId(nabhProcessMasterDto);
//			
//		}catch(Exception e){
//			
//			return new Response<>(ERROR, null, null, null, null);               
//		}
//	}
	@RequestMapping(value = "/updateNabhProcessMasterStatus" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateNabhProcessMasterStatus(@RequestBody NabhProcessMasterDto nabhProcessMasterDto){
		try{
			return iNabhProcessMasterService.updateNabhProcessMasterStatus(nabhProcessMasterDto);

		}catch(Exception e){
			logger.error("updateNabhProcessMasterStatus failed", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/getActiveNabhProcessMasterList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveNabhProcessMasterList(@RequestBody NabhProcessMasterDto nabhProcessMasterDto){
		try{
		return iNabhProcessMasterService.getActiveNabhProcessMasterList(nabhProcessMasterDto);

		}catch(Exception e){
		logger.error("getActiveNabhProcessMasterList failed", e);
		return new Response<>(ERROR, null, null, null, null);
		}
	}
//	
//	@RequestMapping(value = "/getIncidentReportMasterCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
//	public Response getNabhProcessMasterCount(@RequestBody NabhProcessMasterDto nabhProcessMasterDto){
//			try{
//			return iNabhProcessMasterService.getNabhProcessMasterById(nabhProcessMasterDto);
//		
//			}catch(Exception e){
//			
//			return new Response<>(ERROR, null, null, null, null);
//			}
//	}
}
