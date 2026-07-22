package com.param.global.controller;

import com.param.entity.model.master.DifferentTypesScreenImageDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.RegistrationFieldConfigurationDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.service.IUnitApplicationBillingConfigurationService;

@Controller
@RequestMapping("/api/unit/applicationConfiguration")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UnitApplicationBillingConfigurationController implements ICommonConstants
{

	final static Logger logger = Logger.getLogger(UnitApplicationBillingConfigurationController.class);

	@Autowired
	private IUnitApplicationBillingConfigurationService unitApplicationBillingConfigurationService;
	
	@RequestMapping(value="/billing/orgId/{orgId}/unitId/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getUnitApplicationBillingConfigByOrgUnit(@PathVariable("unitId") Integer unitId,
			@PathVariable("orgId") Integer orgId) {
		try {
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setUnitId(unitId);
			unitApplicationConfigurationDto.setOrganizationId(orgId);
			Response res = unitApplicationBillingConfigurationService.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
			// Security fix, docs/DECISION_NEEDED_UnitApplicationConfigCredentialDisclosure.md --
			// this is the client-facing boundary; strip secret-shaped fields here, not inside the
			// shared service (which RazorPaymentController also calls for real payment processing).
			if (res.getObject() instanceof UnitApplicationConfigurationDto) {
				((UnitApplicationConfigurationDto) res.getObject()).stripClientFacingSecrets();
			}
			return res;
		}catch (Exception e) {
			logger.error("getUnitApplicationBillingConfigByOrgUnit failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/saveUnitApplicationBillingConfig", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response saveUnitApplicationBillingConfig(@RequestBody UnitApplicationConfigurationDto unitApplicationConfigurationDto) {
		try {
				return unitApplicationBillingConfigurationService.saveUnitApplicationBillingConfig(unitApplicationConfigurationDto);
		}catch (Exception e) {
			logger.error("saveUnitApplicationBillingConfig failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/updateUnitApplicationBillingConfig", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE  )
	public @ResponseBody Response updateUnitApplicationBillingConfig(@RequestBody UnitApplicationConfigurationDto unitApplicationConfigurationDto) {
		try {
				return unitApplicationBillingConfigurationService.updateUnitApplicationBillingConfig(unitApplicationConfigurationDto);
		}catch (Exception e) {
			logger.error("updateUnitApplicationBillingConfig failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/updateUnitApplicationBillingConfigStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE  )
	public @ResponseBody Response updateUnitApplicationBillingConfigStatus(@RequestBody UnitApplicationConfigurationDto unitApplicationConfigurationDto) {
		try {
				return unitApplicationBillingConfigurationService.updateUnitApplicationBillingConfigStatus(unitApplicationConfigurationDto);
		}catch (Exception e) {
			logger.error("updateUnitApplicationBillingConfigStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/getUnitApplicationBillingConfigList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE  )
	public @ResponseBody Response getUnitApplicationBillingConfigList(@RequestBody UnitApplicationConfigurationDto unitApplicationConfigurationDto) {
		try {
				return unitApplicationBillingConfigurationService.getUnitApplicationBillingConfigList(unitApplicationConfigurationDto);
		}catch (Exception e) {
			logger.error("getUnitApplicationBillingConfigList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value="/getUnitApplicationBillingConfigCount/{organizationId}/{unitId}", method = RequestMethod.GET  )
	public @ResponseBody Response getUnitApplicationBillingConfigCount( @PathVariable ("organizationId") Integer organizationId, 
			  															@PathVariable ("unitId") Integer unitId) {
		try {
				return unitApplicationBillingConfigurationService.getUnitApplicationBillingConfigCount(organizationId,unitId);
		}catch (Exception e) {
			logger.error("getUnitApplicationBillingConfigCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/getOPDBillingBedCatagory/{organizationId}/{unitId}", method = RequestMethod.GET  )
	public @ResponseBody Response getOPDBillingBedCatagory( @PathVariable ("organizationId") Integer organizationId, 
			  												@PathVariable ("unitId") Integer unitId) {
		try {
				return unitApplicationBillingConfigurationService.getOPDBillingBedCatagory(organizationId,unitId);
		}catch (Exception e) {
			logger.error("getOPDBillingBedCatagory failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/getSelfPayeeList/{organizationId}/{unitId}", method = RequestMethod.GET  )
	public @ResponseBody Response getSelfPayeeList(@PathVariable ("organizationId") Integer organizationId, 
			  									   @PathVariable ("unitId") Integer unitId) {
		try {
				return unitApplicationBillingConfigurationService.getSelfPayeeList(organizationId,unitId);
		}catch (Exception e) {
			logger.error("getSelfPayeeList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/saveUnitApplicationRegistrationFieldConfig", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response saveUnitApplicationRegistrationFieldConfig(@RequestBody RegistrationFieldConfigurationDto registrationFieldConfigurationDto) {
		try {
				return unitApplicationBillingConfigurationService.saveUnitApplicationRegistrationFieldConfig(registrationFieldConfigurationDto);
		}catch (Exception e) {
			logger.error("saveUnitApplicationRegistrationFieldConfig failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/getUnitApplicationRegistrationFieldConfig/{organizationId}/{unitId}", method = RequestMethod.GET  )
	public @ResponseBody Response getUnitApplicationRegistrationFieldConfig(@PathVariable ("organizationId") Integer organizationId, 
			  									   @PathVariable ("unitId") Integer unitId) {
		try {
			return unitApplicationBillingConfigurationService.getUnitApplicationRegistrationFieldConfig(organizationId,unitId);

		}catch (Exception e) {
			logger.error("getUnitApplicationRegistrationFieldConfig failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/getQMSPrnFormat", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getQMSPrnFormat(@RequestBody UnitApplicationConfigurationDto unitApplicationConfigurationDto) {
		try {
				return unitApplicationBillingConfigurationService.getQMSPrnFormat(unitApplicationConfigurationDto);
		}catch (Exception e) {
			logger.error("getQMSPrnFormat failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/screen-images", headers=("content-type=multipart/*"), method = RequestMethod.POST)
	public @ResponseBody Response saveScreenImage(
							@RequestParam("imageTypeId") Integer imageTypeId,
							@RequestParam("createdBy") Integer createdBy,
							@RequestParam("updatedBy") Integer updatedBy,
							@RequestParam("status") String status,
							@RequestParam("organisationId") Integer organisationId,
							@RequestParam("unitId") Integer unitId,
							@RequestParam(value="createdDate", required = false) String createdDate,
							@RequestParam(value="updatedDate", required = false) String updatedDate,
							@RequestParam(value="logoName", required = false) String logoName,
							@RequestParam(value = "file", required = false) MultipartFile inputFile) throws Exception {
		
		try {
			Character statusChar = status.charAt(0);

			DifferentTypesScreenImageDTO differentTypesScreenImageDTO = new DifferentTypesScreenImageDTO();

			differentTypesScreenImageDTO.setImageTypeId(imageTypeId);
			differentTypesScreenImageDTO.setCreatedBy(createdBy);
			differentTypesScreenImageDTO.setUpdatedBy(updatedBy);
			differentTypesScreenImageDTO.setStatus(statusChar);
			differentTypesScreenImageDTO.setOrganizationId(organisationId);
			differentTypesScreenImageDTO.setUnitId(unitId);
			differentTypesScreenImageDTO.setCreatedDate(createdDate);
			differentTypesScreenImageDTO.setUpdatedDate(updatedDate);
			differentTypesScreenImageDTO.setLogoName(logoName);

		 	if(inputFile == null){
				return unitApplicationBillingConfigurationService.saveScreenApplicationName(differentTypesScreenImageDTO);
			}else{
				return unitApplicationBillingConfigurationService.saveScreenImage(differentTypesScreenImageDTO, inputFile);
			}

		} catch (Exception e) {
			throw e;
		}

	}

	@RequestMapping(value="/screen-images/{orgId}/{imageTypeId}", method = RequestMethod.GET  )
	public @ResponseBody Response getScreenImageById(@PathVariable("orgId") Integer organisationId,@PathVariable("imageTypeId") Integer imageTypeId) throws Exception{

		try {
			return unitApplicationBillingConfigurationService.getScreenImageById(organisationId, imageTypeId);
		}catch (Exception e){
			throw e;
		}

	}

}
