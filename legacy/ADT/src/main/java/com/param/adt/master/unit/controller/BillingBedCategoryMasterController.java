package com.param.adt.master.unit.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.unit.dto.BillingBedCategoryMasterDto;
import com.param.adt.master.unit.service.IBillingBedCategoryMasterService;
import com.param.global.common.ServiceException;


@Controller
@RequestMapping("/adt")
@SuppressWarnings({"rawtypes","unchecked"})
public class BillingBedCategoryMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(BillingBedCategoryMasterController.class);

	@Autowired
	IBillingBedCategoryMasterService iBillingBedCategoryMasterService;

	@RequestMapping(value="/getActiveBillingBedCategoryList/orgId/{orgId}/unitId/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getActiveBillingBedCategoryList(@PathVariable("unitId") Integer unitId,
			@PathVariable("orgId") Integer orgId) {
		try{
			BillingBedCategoryMasterDto bedCategoryMasterDto=new BillingBedCategoryMasterDto();
				bedCategoryMasterDto.setOrganizationId(orgId);
				bedCategoryMasterDto.setUnitId(unitId);

			return iBillingBedCategoryMasterService.getActiveBillingBedCategoryList(bedCategoryMasterDto);

		}catch(Exception e){
			logger.error("getActiveBillingBedCategoryList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	/*@RequestMapping(value="/getActiveBillingBedCategoryList/orgId/{orgId}", method = RequestMethod.GET)
	public @ResponseBody Response getActiveBillingBedCategoryMasterList(@PathVariable("orgId") Integer orgId) {
		try{
			BillingBedCategoryMasterDto bedCategoryMasterDto=new BillingBedCategoryMasterDto();
				bedCategoryMasterDto.setOrganizationId(orgId);
			return iBillingBedCategoryMasterService.getActiveBillingBedCategoryMasterList(orgId);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}*/
	
	@RequestMapping(value = "/getBillingBedCategoryListByBedCategory", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBillingBedCategoryListByBedCategory(
			@RequestBody BillingBedCategoryMasterDto billingBedCategoryMasterDto) {
		try {
			return iBillingBedCategoryMasterService.getActiveBillingBedCategoryByBedCat(billingBedCategoryMasterDto);
		} catch (Exception e) {
			logger.error("getBillingBedCategoryListByBedCategory failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/saveBillingBedCategoryMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveBillingBedCategoryMaster(@RequestBody BillingBedCategoryMasterDto billingBedCategoryMasterDto) {
		try {
			return iBillingBedCategoryMasterService.saveBillingBedCategoryMaster(billingBedCategoryMasterDto);
		} catch (Exception e) {
			logger.error("saveBillingBedCategoryMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getBillingBedCategoryById/{billingBedId}/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getBillingBedCategoryById(@PathVariable("billingBedId") int billingBedId,
			@PathVariable("orgId") int orgId, @PathVariable("unitId") int unitId) {
		try {
			return iBillingBedCategoryMasterService.getBillingBedCategoryById(billingBedId, orgId, unitId);
		} catch (Exception e) {
			logger.error("getBillingBedCategoryById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getBillingBedCategoryList", method = RequestMethod.POST)
	public @ResponseBody Response getBillingBedCategoryList(@RequestBody BillingBedCategoryMasterDto billingBedCategoryMasterDto) {
		try {
			return iBillingBedCategoryMasterService.getBillingBedCategoryList(billingBedCategoryMasterDto);
		} catch (Exception e) {
			logger.error("getBillingBedCategoryList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/updateBillingBedCategoryMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateBillingBedCategoryMaster(@RequestBody BillingBedCategoryMasterDto billingBedCategoryMasterDto) {
		try {
			return iBillingBedCategoryMasterService.updateBillingBedCategoryMaster(billingBedCategoryMasterDto);
		} catch (Exception e) {
			logger.error("updateBillingBedCategoryMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/updateBillingBedCategoryMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateBillingBedCategoryMasterStatus(@RequestBody BillingBedCategoryMasterDto billingBedCategoryMasterDto) {
		try {
			return iBillingBedCategoryMasterService.updateBillingBedCategoryMasterStatus(billingBedCategoryMasterDto);
		} catch (Exception e) {
			logger.error("updateBillingBedCategoryMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getBillingBedCategoryMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBillingBedCategoryMasterCount(@RequestBody BillingBedCategoryMasterDto billingBedCategoryMasterDto) {
		try {
			return iBillingBedCategoryMasterService.getBillingBedCategoryMasterCount(billingBedCategoryMasterDto);
		} catch (Exception e) {
			logger.error("getBillingBedCategoryMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBillingBedCategoryByVistTypeId/{visitTypeId}/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getBillingBedCategoryByVistTypeId(@PathVariable("visitTypeId") int visitTypeId,
			@PathVariable("orgId") int orgId, @PathVariable("unitId") int unitId) {
		try {
			return iBillingBedCategoryMasterService.getBillingBedCategoryByVistTypeId(visitTypeId,orgId,unitId);
		} catch (Exception e) {
			logger.error("getBillingBedCategoryByVistTypeId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBillingBedCategoryByVistTypeIdMultiple", method = RequestMethod.POST)
	public @ResponseBody Response getBillingBedCategoryByVistTypeIdMultiple(@RequestBody BillingBedCategoryMasterDto billingBedCategoryMasterDto) {
		try {
			return iBillingBedCategoryMasterService.getBillingBedCategoryByVistTypeIdMultiple(billingBedCategoryMasterDto);
		} catch (Exception e) {
			logger.error("getBillingBedCategoryByVistTypeIdMultiple failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getVisitTypeListByBillingBedCategory", method = RequestMethod.POST)
	public @ResponseBody Response getVisitTypeListByBillingBedCategory(@RequestBody BillingBedCategoryMasterDto billingBedCategoryMasterDto) {
		try {
			return iBillingBedCategoryMasterService.getVisitTypeListByBillingBedCategory(billingBedCategoryMasterDto);
		} catch (Exception e) {
			logger.error("getVisitTypeListByBillingBedCategory failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
