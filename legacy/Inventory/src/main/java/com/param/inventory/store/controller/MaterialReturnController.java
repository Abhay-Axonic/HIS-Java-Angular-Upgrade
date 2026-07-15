package com.param.inventory.store.controller;

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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import org.apache.log4j.Logger;

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.inventory.store.dto.materialIssueList.MaterialIssueListDropdownResponse;
import com.param.inventory.store.dto.materialReturn.MaterialResponse;
import com.param.inventory.store.dto.materialReturn.MaterialReurnRequest;
import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;
import com.param.inventory.store.service.materialReturn.IMaterialReturnService;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientAgainstIssueResponse;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnRequest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@EnableWebMvc
@RestController
@RequestMapping(value = "/api/inventory/stores/material/return")
public class MaterialReturnController {

	private static final Logger logger = Logger.getLogger(MaterialReturnController.class);

	@Autowired
	IMaterialReturnService MaterialReturnService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = StoreIndentDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialReturnService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching dropdowns", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/dropdowns/return/reason/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown for reject reason", response = StoreIndentDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownForRejectReason(@PathVariable Integer unitId) throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialReturnService.getDropdownForReturnReason(unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching dropdowns for reject reason", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/carrier/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = MaterialIssueListDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> dropdownMaterialReturnCarrierList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialReturnService.dropdownMaterialReturnCarrierList(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching carrier dropdowns", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of Material Return List by search", response = MaterialResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getMaterialReturnList(@RequestBody MaterialReurnRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialReturnService.getMaterialReturnList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the material return list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save Material Return", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveMaterialReturn(@RequestBody MaterialReurnRequest request) throws Exception {
		try {
			if(request.getId()!=null) {
			 Response statausres = MaterialReturnService.materialReturnStatus(request.getId());
	            
	            if (statausres.getObject() == com.param.common.constants.Status.Completed.getStatus()
	                    || statausres.getObject() == com.param.common.constants.Status.Closed.getStatus()) {
	                CommonResponse response = new CommonResponse();
	                response.setMessage("This transaction already processed by other user");
	                return new ResponseEntity<Object>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	            }
			}
			return new ResponseEntity<Object>(MaterialReturnService.saveMaterialReturn(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving material return", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Material Return", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateMaterialReturn(@PathVariable Integer id,
			@RequestBody MaterialReurnRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(MaterialReturnService.updateMaterialReturn(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while updating material return", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Material Return details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getMaterialReturn(@PathVariable Integer id,@PathVariable Integer unitId) throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialReturnService.getMaterialReturnDetails(id,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the material return details", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Material Return", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveMaterialReturn(@RequestBody MaterialReurnRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialReturnService.approveMaterialReturn(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while approving the material return", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Material Return", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelMaterialReturn(@PathVariable Integer id,
			@RequestBody MaterialReurnRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(MaterialReturnService.cancelMaterialReturn(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while cancelling the material return", ex);
			throw new ServiceException(ex.getMessage());
		}
	}


	@RequestMapping(value = "/againstIssue/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get Patient Against Issue list ", response = PatientAgainstIssueResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientAgainstIssueSearch(@RequestBody PatientIssueReturnRequest patientIssueReturnRequest) {
		try
		{
			return new ResponseEntity<Object>(MaterialReturnService.patientAgainstIssueSearch(patientIssueReturnRequest), HttpStatus.OK);

		}
		catch(Exception ex)
		{
			logger.error("Error while searching against issue", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	@RequestMapping(value = "/againstExpireItem/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get Patient Against Issue list ", response = PatientAgainstIssueResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> againstExpireItemSearch(@RequestBody PatientIssueReturnRequest patientIssueReturnRequest) {
		try
		{
			return new ResponseEntity<Object>(MaterialReturnService.againstExpireItemSearch(patientIssueReturnRequest), HttpStatus.OK);

		}
		catch(Exception ex)
		{
			logger.error("Error while searching against expire item", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
    
    @RequestMapping(value = "/materialReturn/getDetailsByIssueId/{ids}/{fromStoreId}/{toStoreId}", method = RequestMethod.GET)
    @ApiOperation(value = "Get Material Return details by id", response = CommonResponse.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    public ResponseEntity<Object> getMaterialReturn1(@PathVariable List<Integer> ids,@PathVariable Integer fromStoreId,@PathVariable Integer toStoreId) throws Exception {
        try {
            return new ResponseEntity<Object>(MaterialReturnService.getMaterialReturnDetails1(ids,fromStoreId,toStoreId), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error while fetching the material return details by issue id", ex);
            throw new ServiceException(ex.getMessage());
        }
    }
	
}
