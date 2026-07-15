package com.param.pharmacy.master.unit.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.assetManagement.maintenance.dto.VendorDto;
import com.param.common.dto.CommonResponse;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.UserMasterDto;
import com.param.global.org.dto.GeneralLedgerMasterDto;
import com.param.global.service.IDoctorMasterService;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.unit.dao.vendor.VendorDao;
import com.param.pharmacy.master.unit.dto.vendor.AreaDto;
import com.param.pharmacy.master.unit.dto.vendor.CityDto;
import com.param.pharmacy.master.unit.dto.vendor.CountryDto;
import com.param.pharmacy.master.unit.dto.vendor.PaymentModeDto;
import com.param.pharmacy.master.unit.dto.vendor.StateDto;
//import com.param.entity.dto.CommonResponse;
import com.param.pharmacy.master.unit.dto.vendor.VendorDetailsResponse;
import com.param.pharmacy.master.unit.dto.vendor.VendorDropdownRespose;
import com.param.pharmacy.master.unit.dto.vendor.VendorListResponse;
import com.param.pharmacy.master.unit.dto.vendor.VendorRequest;
import com.param.pharmacy.master.unit.service.vendor.IVendorService;
import com.param.utility.TimeZoneComponent;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/unit/vendors/")
public class VendorController {

	private static final Logger logger = Logger.getLogger(VendorController.class);

	private static final int CommonResponse = 0;

	@Autowired
	IVendorService VendorService;
	
	@Autowired
	private IDoctorMasterService iDoctorMasterService;
	

	@RequestMapping(value = "/{orgId}/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Vendors", response = VendorListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<VendorListResponse>> GetAll(@PathVariable Integer orgId,@PathVariable Integer unitId) {
		ResponseEntity<List<VendorListResponse>> response = new ResponseEntity<List<VendorListResponse>>(
				VendorService.getAll(orgId, unitId), HttpStatus.OK);

		return response;
	}
	
	@RequestMapping(value = "active/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Vendors", response = VendorListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<VendorDto>> getActiveVendors(@PathVariable Integer unitId) {
		ResponseEntity<List<VendorDto>> response = new ResponseEntity<List<VendorDto>>(
				VendorService.getActiveVendorList(unitId), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Vendors By Id", response = VendorDetailsResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<VendorDetailsResponse> GetById(@PathVariable("id") Integer id) {
		ResponseEntity<VendorDetailsResponse> response = new ResponseEntity<VendorDetailsResponse>(
				VendorService.getById(id), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Vendors", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Vendors Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Vendors"),
			@ApiResponse(code = 403, message = "Create Vendors is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Create(@RequestBody VendorRequest vendorRequest) {
		return new ResponseEntity<CommonResponse>(VendorService.create(vendorRequest), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Vendors", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Vendors Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Vendors"),
			@ApiResponse(code = 403, message = "Update Vendors is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<Object> Update(@PathVariable("id") Integer id, @RequestBody VendorRequest vendorRequest) {
		vendorRequest.setId(id);
		try {
			return new ResponseEntity<Object>(VendorService.update(vendorRequest), HttpStatus.CREATED);
		} catch (CustomeException CustomeRespone) {
			logger.error(CustomeRespone.getMessage(), CustomeRespone);
			throw new ServiceException(CustomeRespone.getMessage());
		}
	}
	/*
	 * @RequestMapping(value = "/vendors", method = RequestMethod.POST, consumes =
	 * MediaType.APPLICATION_JSON_VALUE, produces =
	 * MediaType.APPLICATION_JSON_VALUE)
	 * 
	 * @ApiOperation("Create vendors")
	 * 
	 * @ApiResponses(value = {
	 * 
	 * @ApiResponse(code = 200, message = "Vendor Created successfully"),
	 * 
	 * @ApiResponse(code = 401, message =
	 * "You are Not authorized to create Vendor"),
	 * 
	 * @ApiResponse(code = 403, message = "Create Vendor is forbidden"),
	 * 
	 * @ApiResponse(code = 404, message = "Resource Not found"),
	 * 
	 * @ApiResponse(code = 405, message = "Method Not Allowed")}) public
	 * ResponseEntity<Integer> saveVendorDataEntry(@RequestBody Vendor vendor) {
	 * return new ResponseEntity<Integer>(vendorService.createVendor(vendor),
	 * HttpStatus.CREATED); }
	 * 
	 * @RequestMapping(value = "/vendors/{vendorId}", method = RequestMethod.DELETE)
	 * 
	 * @ApiOperation(value="Delete vendors")
	 * 
	 * @ApiResponses(value = {
	 * 
	 * @ApiResponse(code = 200, message = "Vendor Deleted successfully"),
	 * 
	 * @ApiResponse(code = 401, message =
	 * "You are Not authorized to Delete Vendor"),
	 * 
	 * @ApiResponse(code = 403, message = "Delete Vendor is forbidden"),
	 * 
	 * @ApiResponse(code = 404, message = "Resource Not found"),
	 * 
	 * @ApiResponse(code = 405, message = "Method Not Allowed")}) public
	 * ResponseEntity<Boolean> deleteVendor(@PathVariable("vendorId") Integer
	 * vendorId) { Boolean isVendorDeleted = vendorService.deleteVendor(vendorId);
	 * if (!isVendorDeleted) { return new
	 * ResponseEntity<Boolean>(HttpStatus.NOT_FOUND); } else { return new
	 * ResponseEntity<Boolean>(true, HttpStatus.ACCEPTED); } }
	 * 
	 * @RequestMapping(value="/vendors/{vendorId}", method=RequestMethod.PATCH)
	 * 
	 * @ApiOperation(value="Update Vendors", response = Vendor.class)
	 * 
	 * @ApiResponses(value = {
	 * 
	 * @ApiResponse(code = 200, message = "Vendor Update successfully"),
	 * 
	 * @ApiResponse(code = 401, message =
	 * "You are Not authorized to Update Vendor"),
	 * 
	 * @ApiResponse(code = 403, message = "Update Vendor is forbidden"),
	 * 
	 * @ApiResponse(code = 404, message = "Resource Not found"),
	 * 
	 * @ApiResponse(code = 405, message = "Method Not Allowed")}) public
	 * ResponseEntity<Boolean> updateVendor(@RequestBody Vendor
	 * vendor,@PathVariable("vendorId") Integer vendorId) {
	 * vendor.setVendorId(vendorId); Boolean isVendorUpdated
	 * =vendorService.updateVendor(vendor) ; if (!isVendorUpdated) { return new
	 * ResponseEntity<Boolean>(HttpStatus.NOT_FOUND); } else { return new
	 * ResponseEntity<Boolean>(true, HttpStatus.ACCEPTED); } }
	 * 
	 * @RequestMapping(value = "/vendors/{vendorId}", method = RequestMethod.GET)
	 * 
	 * @ApiOperation(value="Get Vendor By vendorId", response = Vendor.class)
	 * 
	 * @ApiResponses(value = {
	 * 
	 * @ApiResponse(code = 200, message = "Suceess|OK"),
	 * 
	 * @ApiResponse(code = 401, message = "not authorized!"),
	 * 
	 * @ApiResponse(code = 403, message = "forbidden!!!"),
	 * 
	 * @ApiResponse(code = 404, message = "not found!!!") }) public
	 * ResponseEntity<Vendor> vendors(@PathVariable("vendorId") Integer vendorId) {
	 * Vendor vendor = vendorService.findVendorById(vendorId); return new
	 * ResponseEntity<Vendor>(vendor,HttpStatus.OK); }
	 */

	@RequestMapping(value = "/countries", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = CountryDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getCountries() throws JSONException {
		return new ResponseEntity<Object>(VendorService.getCountryList(), HttpStatus.OK);
	}

	@RequestMapping(value = "/states/{orgId}/{countryId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = StateDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getStates(@PathVariable Integer orgId, @PathVariable Integer countryId) throws JSONException {
		return new ResponseEntity<Object>(VendorService.getStates(countryId,orgId), HttpStatus.OK);
	}

	@RequestMapping(value = "/cities/{orgId}/{stateId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = CityDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getCities(@PathVariable Integer orgId,@PathVariable Integer stateId) throws JSONException {
		return new ResponseEntity<Object>(VendorService.getCities(stateId,orgId), HttpStatus.OK);
	}

	@RequestMapping(value = "/areas/{orgId}/{cityId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = AreaDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAreas(@PathVariable Integer orgId,@PathVariable Integer cityId) throws JSONException {
		return new ResponseEntity<Object>(VendorService.getAreas(cityId,orgId), HttpStatus.OK);
	}

	@RequestMapping(value = "/paymentModes", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = PaymentModeDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getPaymentModes() throws JSONException {
		return new ResponseEntity<Object>(VendorService.getPaymentModes(), HttpStatus.OK);
	}

	@RequestMapping(value = "/dropdown/{unitId}/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Dropdowns Vendors", response = VendorDropdownRespose.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> GetDropdowns(@PathVariable Integer unitId,@PathVariable Integer orgId) {
		ResponseEntity<Object> response = new ResponseEntity<Object>(VendorService.getDropdowns(unitId,orgId), HttpStatus.OK);

		return response;
	}
	
	
	@RequestMapping(value = "/createGLCode", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Vendors For GLcode", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Vendors Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Vendors"),
			@ApiResponse(code = 403, message = "Create Vendors is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> CreateGLCode(@RequestBody VendorRequest vendorRequest) {
	    return new ResponseEntity<CommonResponse>(VendorService.CreateGLCode(vendorRequest), HttpStatus.CREATED);
		
	}
	
}
