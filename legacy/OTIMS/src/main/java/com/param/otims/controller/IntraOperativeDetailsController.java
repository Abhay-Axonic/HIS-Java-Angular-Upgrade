package com.param.otims.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.IntraOperativeDetailsDto;
import com.param.global.otims.dto.IntraOperativeImageDto;
import com.param.global.otims.dto.OTChecklistDetailsDto;
import com.param.otims.service.IIntraOperativeDetailsService;

import in.param.exception.ApplicationException;

@RestController
@RequestMapping(value = "/api/otims/IntraOperativeDetails")
@SuppressWarnings({"rawtypes","unused"})
public class IntraOperativeDetailsController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(IntraOperativeDetailsController.class);

	@Autowired
	IIntraOperativeDetailsService iIntraOperativeDetailsService;



	@RequestMapping(value = "/saveIntraOperativeDetails" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveIntraOperativeDetails(@RequestBody IntraOperativeDetailsDto intraOperativeDetailsDto){
		try{
			Response res =  iIntraOperativeDetailsService.saveIntraOperativeDetails(intraOperativeDetailsDto);
			if(intraOperativeDetailsDto.getIntraOperativeId()!=null) {
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null,res.getObject());
			}else {
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null,res.getObject());
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}



	 @RequestMapping(value = "/saveIntraImage", headers=("content-type=multipart/*"), method = RequestMethod.POST)
     public @ResponseBody Response saveIntraImage(
    		 @RequestParam("intraOperativeId") Integer intraOperativeId,
    		 @RequestParam("createdBy") Integer createdBy,
    		 @RequestParam("unitId") Integer unitId,
    		 @RequestParam("organizationId") Integer organizationId,
    		 @RequestParam(value="createdDate", required = false) String createdDate,
    		 @RequestParam("file") MultipartFile file
    		 )
         {
	      try
	      {
	    	  IntraOperativeImageDto dto = new IntraOperativeImageDto();
	    	  dto.setCreatedBy(createdBy);
	    	  dto.setUpdatedBy(createdBy);
	    	  dto.setOrganizationId(organizationId);
	    	  dto.setIntraOperativeId(intraOperativeId);
	    	  dto.setCreatedDate(createdDate);
	    	  dto.setUpdatedDate(createdDate);
	    	  dto.setUnitId(unitId);
	    	  dto.setFile(file);
	    	  dto.setFileName(file.getOriginalFilename());
	         return iIntraOperativeDetailsService.saveIntraImage(dto);
	       }catch(Exception e)
	      {
	           logger.error(e.getMessage(), e);
	           throw new ServiceException(e.getMessage());
	      }
       }


		@RequestMapping(value = "/getIntraOperativeByProReqId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		Response getIntraOperativeByProReqId(@RequestBody IntraOperativeDetailsDto dto)
				throws ApplicationException {
			try {
					return iIntraOperativeDetailsService.getIntraOperativeByProReqId(dto);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}
		}



		 @RequestMapping(value = "/saveIntraVideo", headers=("content-type=multipart/*"), method = RequestMethod.POST)
	     public @ResponseBody Response saveIntraVideo(
	    		 @RequestParam("intraOperativeId") Integer intraOperativeId,
	    		 @RequestParam("createdBy") Integer createdBy,
	    		 @RequestParam("unitId") Integer unitId,
	    		 @RequestParam("organizationId") Integer organizationId,
	    		 @RequestParam(value="createdDate", required = false) String createdDate,
	    		 @RequestParam("file") MultipartFile file
	    		 )
	         {
		      try
		      {
		    	  IntraOperativeImageDto dto = new IntraOperativeImageDto();
		    	  dto.setCreatedBy(createdBy);
		    	  dto.setUpdatedBy(createdBy);
		    	  dto.setOrganizationId(organizationId);
		    	  dto.setIntraOperativeId(intraOperativeId);
		    	  dto.setCreatedDate(createdDate);
		    	  dto.setUpdatedDate(createdDate);
		    	  dto.setUnitId(unitId);
		    	  dto.setFile(file);
		    	  dto.setFileName(file.getOriginalFilename());
		         return iIntraOperativeDetailsService.saveIntraVideo(dto);
		       }catch(Exception e)
		      {
		           logger.error(e.getMessage(), e);
		           throw new ServiceException(e.getMessage());
		      }
	       }


}
