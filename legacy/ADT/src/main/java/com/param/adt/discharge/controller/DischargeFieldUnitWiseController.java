package com.param.adt.discharge.controller;

import javax.persistence.Column;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.param.adt.discharge.dto.DischargeSummaryFieldDto;
import com.param.adt.discharge.model.DischargeFieldUnitWise;
import com.param.adt.discharge.model.DischargeFieldUnitWiseDTO;
import com.param.adt.discharge.service.IDischargeFieldUnitWiseService;
import com.param.entity.model.inventory.returnableBonusBatchExpiry;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@RestController
@RequestMapping("/adt")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DischargeFieldUnitWiseController implements ICommonConstants {

	@Autowired
	IDischargeFieldUnitWiseService iDischargeFieldUnitWiseService;

	@RequestMapping(value = "/saveDischargeFieldUnitWise", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveDischargeFieldUnitWise(@RequestBody DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO) {

		try {
			return iDischargeFieldUnitWiseService.saveDischargeFieldUnitWise(dischargeFieldUnitWiseDTO);
		} catch (Exception e) {

		}

		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/updateDischargeFieldUnitWise", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDischargeFieldUnitWise(@RequestBody DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO) {

		try {
			return iDischargeFieldUnitWiseService.updateDischargeFieldUnitWise(dischargeFieldUnitWiseDTO);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return new Response<>(ERROR, null, null, null, null);

	}

	@RequestMapping(value = "/updateDischargeFieldUnitWiseStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDischargeFieldUnitWiseStatus(
			@RequestBody DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO) {

		try {
			return iDischargeFieldUnitWiseService.updateDischargeFieldUnitWiseStatus(dischargeFieldUnitWiseDTO);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getDischargeFieldUnitWiseById/{dischargeFieldUnitWiseId}", method = RequestMethod.GET)
	public Response getDischargeFieldUnitWiseById(
			@PathVariable("dischargeFieldUnitWiseId") Integer dischargeFieldUnitWiseId) {

		try {
			return iDischargeFieldUnitWiseService.getDischargeFieldUnitWiseById(dischargeFieldUnitWiseId);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getDischargeFieldUnitWiseList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDischargeFieldUnitWiseMasterList(
			@RequestBody DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO) {
		try {
            return iDischargeFieldUnitWiseService.getDischargeFieldUnitWiseMasterList(dischargeFieldUnitWiseDTO);

		} catch (Exception e) {

		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getDischargeFieldUnitWiseCount/{organizationId}/{unitId}/{searchKeyword}", method = RequestMethod.GET)
	public Response getDischargeFieldUnitWiseCount(@PathVariable("organizationId") Integer organizationId, @PathVariable("unitId") Integer unitId,
												   @PathVariable(value = "searchKeyword") String searchKeyword) {
		try {
			searchKeyword = (searchKeyword == null || searchKeyword.equalsIgnoreCase("null")) ? "" : searchKeyword;
			return iDischargeFieldUnitWiseService.getDischargeFieldUnitWiseCount(organizationId, unitId, searchKeyword);
		} catch (Exception e) {

		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getActiveDischargeFieldUnitWise/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveDischargeFieldUnitWise(@PathVariable("organizationId") Integer organizationId, @PathVariable("unitId") Integer unitId) {
		try {
			return iDischargeFieldUnitWiseService.getActiveDischargeFieldUnitWise(organizationId, unitId);
		} catch (Exception e) {

		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getDischargeFieldUnitWiseListnew", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDischargeFieldUnitWiseListnew(
			@RequestBody DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO) {
		try {
			return iDischargeFieldUnitWiseService.getDischargeFieldUnitWiseList(dischargeFieldUnitWiseDTO);
		} catch (Exception e) {

		}
		return new Response(ERROR, null, null, null, null);
	}
	
}
