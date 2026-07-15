package com.param.pharmacy.master.unit.controller;

import java.util.List;

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
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.unit.dto.implantItemMaster.GetItemResponse;
import com.param.pharmacy.master.unit.dto.implantItemMaster.ImplantItemMasterDropdown;
import com.param.pharmacy.master.unit.dto.implantItemMaster.ImplantItemMasterListResponse;
import com.param.pharmacy.master.unit.dto.implantItemMaster.ImplantItemMasterRequest;
import com.param.pharmacy.master.unit.dto.implantItemMaster.ImplantItemMasterResponse;
import com.param.pharmacy.master.unit.service.implantItemMaster.IImplantItemMasterService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.bytebuddy.implementation.bytecode.Throw;

@RestController
@RequestMapping(value = "/api/globals/item/implantItemMaster")
public class ImplantItemMasterController
{
	
	@Autowired
	IImplantItemMasterService iImplantItemMasterService;
	
	@RequestMapping(value = "/dropdown/{unitId}/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = ImplantItemMasterDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdown(@PathVariable Integer unitId,@PathVariable Integer orgId) throws Exception {
		return new ResponseEntity<Object>(iImplantItemMasterService.getDropdowns(unitId,orgId), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/unitId/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Implant Item Master", response = ImplantItemMasterListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<ImplantItemMasterResponse>> GetAll(@PathVariable ("unitId") Integer unitId) {
		ResponseEntity<List<ImplantItemMasterResponse>> response = new ResponseEntity<List<ImplantItemMasterResponse>>(iImplantItemMasterService.getAll(unitId),
				HttpStatus.OK);
		return response;
	}
		
	@RequestMapping(value = "/save", method = RequestMethod.POST ,consumes = "application/json")
	@ApiOperation(value = "Implant Item Master Saved", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = " Saved successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Tax"),
			@ApiResponse(code = 403, message = "Implant Item is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> implantItemMasterSave(@RequestBody ImplantItemMasterRequest implantItemMasterrequest) {
		return new ResponseEntity<CommonResponse>(iImplantItemMasterService.implantItemMasterSave(implantItemMasterrequest), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Implant Item Master List By Id", response = GetItemResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<GetItemResponse> GetById(@PathVariable("id") Integer id) {
		ResponseEntity<GetItemResponse> response = new ResponseEntity<GetItemResponse>(iImplantItemMasterService.getById(id),
				HttpStatus.OK);

		return response;
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Item", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Product Category Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Product Category"),
			@ApiResponse(code = 403, message = "Update Product Category is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<GetItemResponse> Update(@PathVariable("id") Integer id, @RequestBody ImplantItemMasterRequest itemRequest) throws CustomeException {
		itemRequest.setId(id);
		 
			  GetItemResponse response = iImplantItemMasterService.update(itemRequest);
			   return new ResponseEntity<GetItemResponse>(response, HttpStatus.OK);
		       
		 

	}

}
