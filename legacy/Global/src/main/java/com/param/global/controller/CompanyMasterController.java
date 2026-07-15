package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.CompanyMasterDto;
import com.param.global.service.ICompanyMasterService;

@RestController
@RequestMapping("api/global")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CompanyMasterController implements ICommonConstants{
	private static final Logger logger = Logger.getLogger(CompanyMasterController.class);

	@Autowired
	private ICompanyMasterService iCompanyMasterService;

	@RequestMapping(value="/companyMaster/unit/{unitId}/org/{orgId}", method=RequestMethod.GET)
	public Response getActiveCompanyMasterList(@PathVariable("unitId")Integer unitId, @PathVariable("orgId") Integer orgId) {
		try {
			CompanyMasterDto companyMasterDto = new CompanyMasterDto();
			companyMasterDto.setUnitId(unitId);
			companyMasterDto.setOrganizationId(orgId);

			return iCompanyMasterService.getActiveCompanyMasterList(companyMasterDto);

		}catch (Exception e) {
			logger.error("Error while fetching the active company master list", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, null, null, null);
	}
	@RequestMapping(value="/saveCompanyMaster", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response saveCompanyMaster(@RequestBody CompanyMasterDto companyMasterDto) {
		try {
			return iCompanyMasterService.saveCompanyMaster(companyMasterDto);
		}catch (Exception e) {
			logger.error("Error while saving the company master", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, null, null, null);
	}

	@RequestMapping(value="/getCompanyMasterList", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getCompanyMasterList(@RequestBody CompanyMasterDto companyMasterDto) {
		try {
			return iCompanyMasterService.getCompanyMasterList(companyMasterDto);
		}catch (Exception e) {
			logger.error("Error while fetching the company master list", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, null, null, null);
	}
	@RequestMapping(value="/getCompanyMasterListById/unit/{unitId}/org/{orgId}/company/{companyId}", method=RequestMethod.GET)
	public Response getCompanyMasterListById(@PathVariable("unitId") Integer unitId,@PathVariable("orgId") Integer orgId,
			@PathVariable("companyId") Integer companyId) {
		try {
			return iCompanyMasterService.getCompanyMasterListById(unitId,orgId,companyId);
		}catch (Exception e) {
			logger.error("Error while fetching the company master list by id", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, null, null, null);
	}
	@RequestMapping(value="/updateCompanyMaster", method=RequestMethod.PUT,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response updateCompanyMaster(@RequestBody CompanyMasterDto companyMasterDto) {
		try {
			return iCompanyMasterService.updateCompanyMaster(companyMasterDto);
		}catch (Exception e) {
			logger.error("Error while updating the company master", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, null, null, null);
	}
	@RequestMapping(value="/updateCompanyMasterStatus", method=RequestMethod.PUT,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response updateCompanyMasterStatus(@RequestBody CompanyMasterDto companyMasterDto) {
		try {
			return iCompanyMasterService.updateCompanyMasterStatus(companyMasterDto);
		}catch (Exception e) {
			logger.error("Error while updating the company master status", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, null, null, null);
	}
	@RequestMapping(value="/countOfCompanyMasterList", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response countOfCompanyMasterList(@RequestBody CompanyMasterDto companyMasterDto) {
		try {
			return iCompanyMasterService.countOfCompanyMasterList(companyMasterDto);
		}catch (Exception e) {
			logger.error("Error while fetching the count of company master list", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, null, null, null);
	}
	@RequestMapping(value="/getCompanyContractGradeByCompanyId/{companyId}", method=RequestMethod.GET)
	public Response getCompanyContractGradeByCompanyId(@PathVariable("companyId") Integer companyId) {
		try {
			CompanyMasterDto companyMasterDto=new CompanyMasterDto();
			companyMasterDto.setCompanyId(companyId);
			return iCompanyMasterService.getCompanyContractGradeByCompanyId(companyMasterDto);
		}catch (Exception e) {
			logger.error("Error while fetching the company contract grade by company id", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, null, null, null);
	}
	@RequestMapping(value="/getActiveTPA", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveTPA(@RequestBody CompanyMasterDto companyMasterDto) {
		try {
			return iCompanyMasterService.getActiveTPA(companyMasterDto);
		}catch (Exception e) {
			logger.error("Error while fetching the active TPA", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, null, null, null);
	}
	 @RequestMapping(value = "/company/image/{orgId}/{unitId}/{companyId}", headers=("content-type=multipart/*"), method = RequestMethod.POST)
     public @ResponseBody Response savePatientImage(@PathVariable("orgId") int orgId,
                           @PathVariable("unitId") int unitId,
                           @PathVariable("companyId") int companyId,
                           @RequestParam("file") MultipartFile inputFile)
         {
	      try
	      {
	         return iCompanyMasterService.saveCompanyImage(orgId,unitId,companyId,inputFile);
	       }catch(Exception e)
	      {
	           logger.error("Error while saving the company image", e);
	      }
	      return new Response<>(ERROR, null, null, null, null);
       }
}
