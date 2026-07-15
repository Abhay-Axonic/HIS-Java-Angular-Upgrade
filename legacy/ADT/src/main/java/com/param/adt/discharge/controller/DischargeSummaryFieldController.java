package com.param.adt.discharge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.adt.discharge.dto.DischargeSummaryFieldDto;
import com.param.adt.discharge.service.IDischargeSummaryFieldService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@RestController
@RequestMapping("/adt")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DischargeSummaryFieldController implements ICommonConstants{
	
	@Autowired
	IDischargeSummaryFieldService iDischargeSummaryFieldService;
	
	@RequestMapping(value = "/saveDischargeSummaryField", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveDischargeSummaryField(@RequestBody DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try {
			return iDischargeSummaryFieldService.saveDischargeSummaryField(dischargeSummaryFieldDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateDischargeSummaryField", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDischargeSummaryField(@RequestBody DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try {
			return iDischargeSummaryFieldService.updateDischargeSummaryField(dischargeSummaryFieldDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateDischargeSummaryFieldStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDischargeSummaryFieldStatus(@RequestBody DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try {
			return iDischargeSummaryFieldService.updateDischargeSummaryFieldStatus(dischargeSummaryFieldDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getDischargeSummaryFieldById/{dischargeFieldId}", method = RequestMethod.GET)
	public Response getDischargeSummaryFieldById(@PathVariable ("dischargeFieldId") Integer dischargeFieldId ) {
		try {
			return iDischargeSummaryFieldService.getDischargeSummaryFieldById(dischargeFieldId);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getDischargeSummaryFieldMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDischargeSummaryFieldMasterList(@RequestBody DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try {
			return iDischargeSummaryFieldService.getDischargeSummaryFieldMasterList(dischargeSummaryFieldDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getDischargeSummaryFieldCount",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDischargeSummaryFieldCount(@RequestBody DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try {
			return iDischargeSummaryFieldService.getDischargeSummaryFieldCount(dischargeSummaryFieldDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getActiveDischargeSummaryField/{organisationId}", method = RequestMethod.GET)
	public Response getActiveDischargeSummaryField(@PathVariable("organisationId") Integer organisationId) {
		try {
			return iDischargeSummaryFieldService.getActiveDischargeSummaryField(organisationId);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getActiveDischargeSummaryFieldMapper/{organisationId}", method = RequestMethod.GET)
	public Response getActiveDischargeSummaryFieldMapper(@PathVariable("organisationId") Integer organisationId) {
		try {
			return iDischargeSummaryFieldService.getActiveDischargeSummaryFieldMapper(organisationId);
		} catch (Exception e) {

		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getDischargeSumColumnDetails",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDischargeSumColumnDetails(@RequestBody DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try {
			return iDischargeSummaryFieldService.getDischargeSumColumnDetails(dischargeSummaryFieldDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateDischSumColumnsToPrint",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDischSumColumnsToPrint(@RequestBody DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try {
			return iDischargeSummaryFieldService.updateDischSumColumnsToPrint(dischargeSummaryFieldDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getDischSumColumnsDetailsForPrint",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDischSumColumnsDetailsForPrint(@RequestBody DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try {
			return iDischargeSummaryFieldService.getDischSumColumnsDetailsForPrint(dischargeSummaryFieldDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/getDischSumColumnsList",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDischSumColumnsList(@RequestBody DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try {
			return iDischargeSummaryFieldService.getDischSumColumnsList(dischargeSummaryFieldDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
//	@RequestMapping(value = "/updateDischSumColumnsRequired",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public Response updateDischSumColumnsRequired(@RequestBody DischargeSummaryFieldDto dischargeSummaryFieldDto) {
//		try {
//			return iDischargeSummaryFieldService.updateDischSumColumnsRequired(dischargeSummaryFieldDto);
//		} catch (Exception e) {
//			
//		}
//		return new Response(ERROR, null, null, null, null);
//	}
}
