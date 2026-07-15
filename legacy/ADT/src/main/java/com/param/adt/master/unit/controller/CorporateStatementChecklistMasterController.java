package com.param.adt.master.unit.controller;

import org.hibernate.HibernateException;
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
import com.param.adt.master.unit.dto.CorporateStatementChecklistMasterDto;
import com.param.adt.master.unit.model.CorporateStatementChecklistMaster;
import com.param.adt.master.unit.service.IBillingBedCategoryMasterService;
import com.param.adt.master.unit.service.ICorporateStatementChecklistMasterService;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.service.ICorporateEmployeeMasterService;

import in.param.exception.ApplicationException;


@Controller
@RequestMapping("/adt")
@SuppressWarnings({"rawtypes","unchecked"})
public class CorporateStatementChecklistMasterController implements ICommonConstants{
	
	@Autowired
	ICorporateStatementChecklistMasterService iCorporateStatementChecklistMasterService;
	
	
	@RequestMapping(value="/getActiveCorporateStatementChecklistMaster/orgId/{orgId}/unitId/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getActiveCorporateStatementChecklistMaster(@PathVariable("unitId") Integer unitId,
			@PathVariable("orgId") Integer orgId) {
		try{
			CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto=new CorporateStatementChecklistMasterDto();
			corporateStatementChecklistMasterDto.setOrganizationId(orgId);
			corporateStatementChecklistMasterDto.setUnitId(unitId);
			return iCorporateStatementChecklistMasterService.getActiveCorporateStatementChecklistMaster(corporateStatementChecklistMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	
	@RequestMapping(value = "/saveCorporateStatementChecklistMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveCorporateStatementChecklistMaster(@RequestBody CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto) {
		try {
			return iCorporateStatementChecklistMasterService.saveCorporateStatementChecklistMaster(corporateStatementChecklistMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value = "/getCorporateStatementChecklistMasterListAll", method = RequestMethod.POST)
	public @ResponseBody Response getCorporateStatementChecklistMasterListAll(@RequestBody CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto) {
		try {
			return iCorporateStatementChecklistMasterService.getCorporateStatementChecklistMasterListAll(corporateStatementChecklistMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getCorporateStatementChecklistMasterList", method = RequestMethod.POST)
	public @ResponseBody Response getCorporateStatementChecklistMasterList(@RequestBody CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto) {
		try {
			return iCorporateStatementChecklistMasterService.getCorporateStatementChecklistMasterList(corporateStatementChecklistMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value = "/getCorporateStatementChecklistMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCorporateStatementChecklistMasterCount(@RequestBody CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto) {
		try {
			return iCorporateStatementChecklistMasterService.getCorporateStatementChecklistMasterCount(corporateStatementChecklistMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getCorporateStatementChecklistMasterById/{corporateStatementChecklistId}/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getCorporateStatementChecklistMasterById(@PathVariable("corporateStatementChecklistId") int corporateStatementChecklistId,
			@PathVariable("orgId") int orgId, @PathVariable("unitId") int unitId) {
		try {
			return iCorporateStatementChecklistMasterService.getCorporateStatementChecklistMasterById(corporateStatementChecklistId, orgId, unitId);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateCorporateStatementChecklistMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateCorporateStatementChecklistMaster(@RequestBody CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto) {
		try {
			return iCorporateStatementChecklistMasterService.updateCorporateStatementChecklistMaster(corporateStatementChecklistMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateCorporateStatementChecklistMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateCorporateStatementChecklistMasterStatus(@RequestBody CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto) {
		try {
			return iCorporateStatementChecklistMasterService.updateCorporateStatementChecklistMasterStatus(corporateStatementChecklistMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
}
