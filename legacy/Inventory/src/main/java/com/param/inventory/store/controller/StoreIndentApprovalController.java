package com.param.inventory.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;
import com.param.inventory.store.dto.storeIndent.StoreIndentRequest;
import com.param.inventory.store.dto.storeIndentApproval.StoreIndentApprovalRequest;
import com.param.inventory.store.service.storeIndentApproval.IStoreIndentApprovalService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/inventory/stores/indents/approval")
public class StoreIndentApprovalController {

	private static final Logger logger = Logger.getLogger(StoreIndentApprovalController.class);

	@Autowired
	IStoreIndentApprovalService StoreIndentApprovalService;
	
	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = StoreIndentDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(StoreIndentApprovalService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching dropdowns", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of store indent approval by search", response = StoreIndentDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getStoreIndentList(@RequestBody StoreIndentRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(StoreIndentApprovalService.getStoreIndentList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the store indent list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get store indent details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getStoreIndent(@PathVariable Integer id,@PathVariable Integer unitId,@PathVariable Integer orgId)
			throws Exception {
		try {
			return new ResponseEntity<Object>(StoreIndentApprovalService.getStoreIndent(id,unitId,orgId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the store indent", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve store indent quantity", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveStoreIndent(@PathVariable Integer id, @RequestBody StoreIndentApprovalRequest request)
			throws Exception {
		try {
            Response statausres = StoreIndentApprovalService.StoreIndentStatus(id);
			
			if (statausres.getObject() == com.param.common.constants.Status.Completed.getStatus()
					|| statausres.getObject() == com.param.common.constants.Status.Closed.getStatus() || statausres.getObject()==com.param.common.constants.Status.Approved.getStatus()) {
				CommonResponse response = new CommonResponse();
				response.setMessage("This transaction already processed by other user");
				return new ResponseEntity<Object>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			request.setId(id);
			return new ResponseEntity<Object>(StoreIndentApprovalService.approveStoreIndent(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while approving the store indent", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel store indent", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelStoreIndent(@PathVariable Integer id, @RequestBody StoreIndentApprovalRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(StoreIndentApprovalService.cancelStoreIndent(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while cancelling the store indent", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/updateStoreIndentForForceClose/{id}", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "update list of store indent approval by status", response = StoreIndentDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> updateStoreIndentForForceClose(@PathVariable Integer id, @RequestBody StoreIndentApprovalRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(StoreIndentApprovalService.updateStoreIndentForForceClose(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while updating the store indent for force close", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	/*@Autowired
	StoreIndentApprovalService siaService;
	
	@RequestMapping(value = "/dropdowns", method = RequestMethod.GET)
	@ApiOperation(value = "Get list Of Dropdowns",notes = "Get list of Opening Balance by storeId", response = StoreIndentMaster.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<ApprovalDropDownDto> saveDataEntry() {
		ApprovalDropDownDto list = siaService.getStoreIndentApprovalDropDown();
		return new ResponseEntity<ApprovalDropDownDto>(list,HttpStatus.OK);
		}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get list of  StoreIndent",notes = "Get list of StoreIndent by storeId", response = StoreIndentMaster.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<ApprovalStoreIndentMasterDto>> getStoreIndents(@RequestBody StoreIndentMaster storeIndentMaster) {
						
		List<ApprovalStoreIndentMasterDto> list = siaService.retrieveStoreIndentsForApprovalSearch(storeIndentMaster);
		return new ResponseEntity<List<ApprovalStoreIndentMasterDto>>(list,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/details/{storeIndentMasterId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of Details",notes = "Get list", response = StoreIndentMaster.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<ApprovalDetailsDto>> getStoreApprovalDetailsData(@PathVariable("storeIndentMasterId") Integer sIndentMasterId) {
		StoreIndentDetails sIndentDetails = new StoreIndentDetails();
		sIndentDetails.setsIndentMasterId(sIndentMasterId);
		
		List<ApprovalDetailsDto> list = siaService.retrieveStoreIndentsApprovalDetails(sIndentDetails);
		return new ResponseEntity<List<ApprovalDetailsDto>>(list,HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value = "/searchNew", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get list of  StoreIndent",notes = "Get list of StoreIndent by storeId", response = StoreIndentMaster.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<ApprovalStoreIndentMasterDto>> getStoreIndentsSearchNew(@RequestBody StoreIndentMaster storeIndentMaster) {
						
		List<ApprovalStoreIndentMasterDto> list = siaService.retrieveStoreIndentsForApprovalSearch(storeIndentMaster);
		return new ResponseEntity<List<ApprovalStoreIndentMasterDto>>(list,HttpStatus.OK);
	}
	
	
	
	 * Search according too Fromdate & Todate
	 
	
	@RequestMapping(value = "/searchDate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get list of  StoreIndent",notes = "Get list of StoreIndent by storeId", response = StoreIndentMaster.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<ApprovalStoreIndentMasterDto>> getStoreIndentsByDate(@RequestBody StoreIndentMasterDto storeIndentMasterDto) {
						
		List<ApprovalStoreIndentMasterDto> list = siaService.retrieveStoreIndentsForApprovalSearch(storeIndentMasterDto);
		return new ResponseEntity<List<ApprovalStoreIndentMasterDto>>(list,HttpStatus.OK);
	}
	
	 * New details list of Store Indent Approval
	 
	@RequestMapping(value = "/newdetails/{storeIndentMasterId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of Details",notes = "Get list", response = StoreIndentMaster.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<ApprovalStoreIndentMasterDto> getStoreApprovalDetails(@PathVariable("storeIndentMasterId") Integer storeIndentMasterId) {
        StoreIndentMasterDto storeIndentMasterDto = new StoreIndentMasterDto();
        storeIndentMasterDto.setStoreIndentMasterId(storeIndentMasterId);
        
		ApprovalStoreIndentMasterDto list = siaService.retrieveStoreIndentsApprovalDetails(storeIndentMasterDto);
		return new ResponseEntity<ApprovalStoreIndentMasterDto>(list,HttpStatus.OK);
	}*/
}