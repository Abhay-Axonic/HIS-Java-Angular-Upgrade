package com.param.pharmacy.master.unit.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.pharmacy.master.unit.dto.kit.GetKitMasterResponseListByName;
import com.param.pharmacy.master.unit.dto.kit.KitItemMasterResponse;
import com.param.pharmacy.master.unit.dto.kit.KitMasterResponseList;
import com.param.pharmacy.master.unit.dto.kit.KitTypeMasterRequest;
import com.param.pharmacy.master.unit.dto.kit.KitTypeMasterResponse;
import com.param.pharmacy.master.unit.service.kit.IKitMasterService;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.items.ItemsSearchResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/units/kit/master")
public class KitMasterController {
	
	
	@Autowired
	IKitMasterService IKitMasterService;

	private static final Logger logger = Logger.getLogger(KitMasterController.class);

	@RequestMapping(value = "/dropdowns", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = KitTypeMasterResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList() throws Exception {

		try {
			return new ResponseEntity<Object>(IKitMasterService.getDropdowns(), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/save", method = RequestMethod.POST,consumes = "application/json")
	@ApiOperation(value = "Save Kit Master", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveMaterialInward(@RequestBody KitTypeMasterRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(IKitMasterService.saveKitMaster(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{orgId}/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Kit  Master", response = KitMasterResponseList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<KitItemMasterResponse>> GetAll(@PathVariable("orgId") Integer orgId , @PathVariable("unitId") Integer unitId) {
		ResponseEntity<List<KitItemMasterResponse>> response = new ResponseEntity<List<KitItemMasterResponse>>(IKitMasterService.getAll(orgId,unitId),
				HttpStatus.OK);
		return response;
	}
	

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Kit Item Master List By Id", response = KitItemMasterResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<KitItemMasterResponse> GetById(@PathVariable("id") Integer id) {
		ResponseEntity<KitItemMasterResponse> response = new ResponseEntity<KitItemMasterResponse>(IKitMasterService.getByKitMasterId(id),
				HttpStatus.OK);
		return response;
	}	

	
	 
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Update Kit Type", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Equipment Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Equipment"),
			@ApiResponse(code = 403, message = "Update Equipment is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<Object> Update(@PathVariable("id") Integer id,
			@RequestBody KitTypeMasterRequest KitTypeMasterRequest) {
		KitTypeMasterRequest.setId(id);
		return new ResponseEntity<Object>(IKitMasterService.update(KitTypeMasterRequest), HttpStatus.OK);
	/*	return new ResponseEntity<CommonResponse>(IKitMasterService.update(KitTypeMasterRequest),
				HttpStatus.CREATED);*/
	}
	
	//get Medication Kit
	@RequestMapping(value = "/search/{name}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of Medication Kit Item Master", notes = "Get list of Kit Item Master", response = GetKitMasterResponseListByName.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getMedicationKitByCodeAndDecriptionSearch(@PathVariable("name") String name)
			throws Exception {
		return new ResponseEntity<Object>(IKitMasterService.getMedicationKitByCodeAndDecriptionSearch(name), HttpStatus.OK);

	}
	
	//get Medication Kit
		@RequestMapping(value = "/getKitDetails/{storeId}/{kitId}/{kitTypeId}/{orgId}/{unitId}/{paymentEntitlementId}/{admissionId}/{encounterId}/{patientTypeId}", method = RequestMethod.GET)
		@ApiOperation(value = "Get list of Medication Kit Item Master", notes = "Get list of Kit Item Master", response = GetKitMasterResponseListByName.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public Response getKitDetailsByIdAndType(@PathVariable("storeId") Integer storeId,
				@PathVariable("kitId") Integer kitId,@PathVariable("kitTypeId") Integer kitTypeId,
				@PathVariable("orgId") Integer orgId,@PathVariable("unitId") Integer unitId,
				@PathVariable("paymentEntitlementId") Integer paymentEntitlementId,
				@PathVariable("admissionId") Integer admissionId,
				@PathVariable("encounterId") Integer encounterId,
				@PathVariable("patientTypeId") Integer patientTypeId)
				throws  ApplicationException {
			return IKitMasterService.getKitDetailsByIdAndType(storeId,kitId,kitTypeId,orgId,unitId,paymentEntitlementId,admissionId,encounterId,patientTypeId);

		}

		@RequestMapping(value = "/{isImplant}/{search}/{paymentEntitlementId}/{admissionId}/{encounterId}/{patientTypeId}", method = RequestMethod.GET)
		@ApiOperation(value = "Get list of items", response = ItemsSearchResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public Response searchItems(@PathVariable("isImplant") boolean isImplant, @PathVariable("search") String search,
				@PathVariable("paymentEntitlementId") Integer paymentEntitlementId,
				@PathVariable("admissionId") Integer admissionId,
				@PathVariable("encounterId") Integer encounterId,
				@PathVariable("patientTypeId") Integer patientTypeId) throws Exception {
				return IKitMasterService.searchItems(isImplant,search,paymentEntitlementId,admissionId,encounterId,patientTypeId);
		}
		
		
		@RequestMapping(value = "/updateStatus", method = RequestMethod.POST,consumes = "application/json")
		@ApiOperation(value = "Save Kit Master", response = CommonResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> updateStatus(@RequestBody KitTypeMasterRequest request) throws Exception {
			try {
				return new ResponseEntity<Object>(IKitMasterService.updateStatus(request), HttpStatus.OK);
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
				throw new ServiceException(ex.getMessage());
			}
		}
		
		@RequestMapping(value = "/{kitId}/{storeId}/{unitId}", method = RequestMethod.GET)
		@ApiOperation(value = "Get list of items", response = ItemsSearchResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public Response getKitDetails( @PathVariable("kitId") Integer kitId, @PathVariable("storeId") Integer storeId, @PathVariable("unitId") Integer unitId) throws Exception {
				return IKitMasterService.getKitDetails(kitId,storeId,unitId);
		}
		
		
}
