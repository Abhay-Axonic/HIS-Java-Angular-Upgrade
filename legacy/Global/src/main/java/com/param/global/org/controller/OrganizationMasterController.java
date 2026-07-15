package com.param.global.org.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.param.common.dto.CommonResponse;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.folder.creation.BucketUtility;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.org.dto.OrganizationUnitLicenceDetailsDto;
import com.param.global.org.dto.UnitMasterDto;
import com.param.global.org.service.IGlobalOrganizationMasterService;
import com.param.global.org.service.IGlobalUnitMasterService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@Controller
@SuppressWarnings("rawtypes")
public class OrganizationMasterController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(OrganizationMasterController.class);


	@Autowired
	IGlobalOrganizationMasterService iGlobalOrganizationMasterService;
	
	@Autowired
	BucketUtility bucketUtility;
	
	@Autowired
	IGlobalUnitMasterService globalUnitMasterService;
	
	
	@RequestMapping(value = "/organization/save" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create organization", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "prucedureConfiguration Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized Create to prucedureConfiguration"),
			@ApiResponse(code = 403, message = "Create prucedureConfiguration is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	
	
	public ResponseEntity<CommonResponse> saveOrganizationMaster(@RequestBody OrganizationMasterDto organizationMasterDto) {
		
			return new ResponseEntity<CommonResponse>(iGlobalOrganizationMasterService.saveOrganizationMaster(organizationMasterDto), HttpStatus.CREATED);
		
	}
	
	
	@RequestMapping(value = "/organization/update" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create Generic", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "prucedureConfiguration Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized Create to prucedureConfiguration"),
			@ApiResponse(code = 403, message = "Create prucedureConfiguration is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	
	
	public ResponseEntity<CommonResponse> updateOrganizationMaster(@RequestBody OrganizationMasterDto organizationMasterDto){
		
			return new ResponseEntity<CommonResponse>(iGlobalOrganizationMasterService.updateOrganizationMaster(organizationMasterDto), HttpStatus.CREATED);
		
	}
	
	
	@RequestMapping(value="/organization/{organizationId}" , method=RequestMethod.GET)
	public @ResponseBody Response getOrganizationById(@PathVariable("organizationId") int organizationId){
		try{
			return iGlobalOrganizationMasterService.getOrganizationById(organizationId);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value="/allArganizationList/{offSet}/{noOfRecords}" , method=RequestMethod.GET)
	public @ResponseBody Response allArganizationList(@PathVariable("offSet") int offSet , @PathVariable("noOfRecords") int noOfRecords){
		try{
			OrganizationMasterDto organizationMasterDto = new OrganizationMasterDto();
			organizationMasterDto.setOffset(offSet);
			organizationMasterDto.setNoOfRecordsPerPage(noOfRecords);
			return iGlobalOrganizationMasterService.getAllOrganizationMasterList(organizationMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value="/organization/count" , method=RequestMethod.GET)
	public @ResponseBody Response getOrganizationCount(){
		try{
			return iGlobalOrganizationMasterService.getOrganizationCount();
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value="/organization/status/{organizationId}/{status}" , method=RequestMethod.GET)
	public @ResponseBody Response updateOrganizationStatus(@PathVariable("organizationId") int organizationId , @PathVariable("status") char status){
		try{
			OrganizationMasterDto organizationMasterDto = new OrganizationMasterDto();
			organizationMasterDto.setOrganizationId(organizationId);
			organizationMasterDto.setStatus(status);
			return iGlobalOrganizationMasterService.updateOrganizationStatus(organizationMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value="/licenceDetailsByOrganization" , method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getOrganizationById(@RequestBody OrganizationUnitLicenceDetailsDto organizationUnitLicenceDetailsDto){
		try{
			return iGlobalOrganizationMasterService.getLicenceDetailsByOrgUnitId(organizationUnitLicenceDetailsDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value="/organization/active" , method=RequestMethod.GET)
	public @ResponseBody Response getActiveOrganizations(){
		try{
			return iGlobalOrganizationMasterService.getActiveOrganizationList();
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
  @RequestMapping(value = "/organization/image/{organizationId}",headers = ("content-type=multipart/*"), method = RequestMethod.POST)
  public @ResponseBody Response saveOrganizationImage(
      @PathVariable("organizationId") int organizationId,
      @RequestParam("file") MultipartFile inputFile) {
    try {
      return iGlobalOrganizationMasterService.saveOrganizationImage(organizationId, inputFile);
    } catch (Exception e) {
    logger.error("method failed", e);
    }
    return new Response<>(ERROR, null, null, null, null);
  }
  
  @RequestMapping(value="/createorgfoldermanually" , method=RequestMethod.GET)
	public @ResponseBody Response createOrgFolderManually(){
		try{
			//o-for org folder creation 1 for unit folder creation
			Response orgRes=iGlobalOrganizationMasterService.getActiveOrganizationList();
			List<OrganizationMasterDto> orgList=orgRes.getListObject();
			Integer count=0;
			for (OrganizationMasterDto organizationMasterDto : orgList) {
				if (organizationMasterDto.getOrganizationFolder().isEmpty()) {
					String orgniazationName = organizationMasterDto.getOrganizationName().replaceAll("[\\s,.]", "")
							.trim().toLowerCase();
					orgniazationName = orgniazationName + organizationMasterDto.getOrganizationId() + "/";
					boolean flag = bucketUtility.create(0, orgniazationName);
					if (flag) {
						count++;
						organizationMasterDto.setOrganizationFolder(orgniazationName);
						Response orgFolderUpdateRes=iGlobalOrganizationMasterService.updateOrganizationFolderPath(organizationMasterDto);
					}
				}
			}
			if (count > 0) {
				return new Response<>(SUCCESS, null, "Org folder created:" + count, null, null);
			} else {
				return new Response<>(ERROR, null, "Org folder created already", null, null);
			}
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
  
  @RequestMapping(value="/createunitfoldermanually" , method=RequestMethod.GET)
	public @ResponseBody Response createunitfoldermanually(){
		try{
			// o-for org folder creation 1 for unit folder creation
			Response unitRes = globalUnitMasterService.getActiveUnitListForFolderCreation();
			List<UnitMasterDto> unitList = unitRes.getListObject();
			Integer count = 0;
			for (UnitMasterDto unitMasterDto : unitList) {
					String unitFolderName = unitMasterDto.getUnitName().replaceAll("[\\s,.]", "").trim().toLowerCase();
					unitMasterDto.setUnitFolder(
							unitMasterDto.getOrganizationFolder() + unitFolderName + unitMasterDto.getUnitId() + "/");
					boolean flag = bucketUtility.create(1, unitMasterDto.getUnitFolder());
					if (flag) {
						count++;
						Response unitFolderUpdateRes=globalUnitMasterService.updateFolderPath(unitMasterDto);
					}
			}
			if (count > 0) {
				return new Response<>(SUCCESS, null, "Unit folder created:" + count, null, null);
			} else {
				return new Response<>(ERROR, null, "Unit folder created already", null, null);
			}
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
}
