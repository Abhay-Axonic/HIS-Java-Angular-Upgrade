
package com.param.global.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.ErrorResponse;
import com.param.entity.model.inventory.PMSFrequency;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.pharmacy.SaleType;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.StoreStockRequestDto;
import com.param.global.dto.global.AgainstGrnRequest;
import com.param.global.dto.global.AgainstPOSearchResponse;
import com.param.global.dto.global.AgainstPurchaseOrder;
import com.param.global.dto.global.AgainstPurchaseRequest;
import com.param.global.dto.global.AgainstQuotationRequest;
import com.param.global.dto.global.AgainstWORequest;
import com.param.global.dto.global.AssetCategory;
import com.param.global.dto.global.AssetType;
import com.param.global.dto.global.CancelReason;
import com.param.global.dto.global.Currency;
import com.param.global.dto.global.DateRequest;
import com.param.global.dto.global.Doctor;
import com.param.global.dto.global.Floor;
import com.param.global.dto.global.FormulationType;
import com.param.global.dto.global.FormulationTypeAndROAList;
import com.param.global.dto.global.InsuranceCompanyDetails;
import com.param.global.dto.global.ItemDropdownResponse;
import com.param.global.dto.global.Manufacturer;
import com.param.global.dto.global.ManufacturerResponseDto;
import com.param.global.dto.global.Patient;
import com.param.global.dto.global.PatientDetailRequest;
import com.param.global.dto.global.PatientDetailResponse;
import com.param.global.dto.global.PatientIssueDropdown;
import com.param.global.dto.global.ProductCategory;
import com.param.global.dto.global.PurchaseOrderHistory;
import com.param.global.dto.global.PurchaseRequestSearchDto;
import com.param.global.dto.global.RejectReason;
import com.param.global.dto.global.ScrapReason;
import com.param.global.dto.global.Status;
import com.param.global.dto.global.Store;
import com.param.global.dto.global.StorewiseGenericMapperDropdown;
import com.param.global.dto.global.TariffRequest;
import com.param.global.dto.global.TariffResponse;
import com.param.global.dto.global.TaxComponentDropdown;
import com.param.global.dto.global.UomUnit;
import com.param.global.dto.global.User;
import com.param.global.dto.global.VendorResponseDto;
import com.param.global.dto.global.Ward;
import com.param.global.dto.items.ItemsDetailsListResponse;
import com.param.global.service.global.IGlobalService;
import in.param.exception.ApplicationException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/global")
public class GlobalController implements ICommonConstants, IError {

	private static final Logger logger = Logger.getLogger(GlobalController.class);

	@Autowired
	IGlobalService GlobalService;

	@RequestMapping(value = "/assets/types/{unitId}/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = AssetType.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAssetTypes(@PathVariable Integer unitId, @PathVariable Integer orgId)
			throws JSONException {
		return new ResponseEntity<Object>(GlobalService.getAssetTypeList(unitId, orgId), HttpStatus.OK);
	}

	@RequestMapping(value = "/assets/categories/{assetTypeId}/{unitId}/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = AssetCategory.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAssetCategories(@PathVariable Integer assetTypeId,
			@PathVariable Integer unitId, @PathVariable Integer orgId) throws JSONException {
		return new ResponseEntity<Object>(GlobalService.getAssetCategoryList(assetTypeId, unitId, orgId),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/products/categories/{assetCategoryId}/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = ProductCategory.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getProductCategories(@PathVariable Integer assetCategoryId,
			@PathVariable Integer orgId) throws JSONException {
		return new ResponseEntity<Object>(GlobalService.getProductCategoryList(assetCategoryId, orgId), HttpStatus.OK);
	}

	@RequestMapping(value = "/formulation/types/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = FormulationType.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getformulationTypes(@PathVariable Integer orgId) throws JSONException {
		return new ResponseEntity<Object>(GlobalService.getFormulationTypeList(orgId), HttpStatus.OK);
	}

	@RequestMapping(value = "/manufacturers/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = Manufacturer.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getManufacturers(@PathVariable Integer orgId) throws JSONException {
		return new ResponseEntity<Object>(GlobalService.getManufacturersList(orgId), HttpStatus.OK);
	}

	@RequestMapping(value = "/uom/units/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = UomUnit.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getUomUnits(@PathVariable Integer orgId) throws JSONException {
		return new ResponseEntity<Object>(GlobalService.getUomUnitsList(orgId), HttpStatus.OK);
	}

	@RequestMapping(value = "/currencies/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Currency", response = Currency.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getCurrency(@PathVariable Integer unitId) {
		ResponseEntity<Object> response = new ResponseEntity<Object>(GlobalService.getCurrency(unitId), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/status", method = RequestMethod.GET)
	@ApiOperation(value = "Get list Of Status", response = Status.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAllStatus() throws Exception {
		return new ResponseEntity<Object>(GlobalService.getStatusList(), HttpStatus.OK);
	}

	@RequestMapping(value = "/visitType", method = RequestMethod.GET)
	@ApiOperation(value = "Get list Of Status", response = VisitType.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAllVisitType() throws Exception {
		return new ResponseEntity<Object>(GlobalService.getVisitTypeList(), HttpStatus.OK);
	}

	@RequestMapping(value = "/pharmacy/patient/issue/dropdown/{orgId}/{unitId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list Of Status", response = PatientIssueDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getPatientIssueDropdown(@PathVariable Integer orgId,
			@PathVariable Integer unitId, @PathVariable Integer userId) throws Exception {
		return new ResponseEntity<Object>(GlobalService.getPatientIssueDropdown(orgId, unitId, userId), HttpStatus.OK);
	}

	@RequestMapping(value = "/sale/type", method = RequestMethod.GET)
	@ApiOperation(value = "Get list Of Status", response = SaleType.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAllSaleType() throws Exception {
		return new ResponseEntity<Object>(GlobalService.getSaleTypeList(), HttpStatus.OK);
	}

	@RequestMapping(value = "/reject/reasons/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list Of Reject Reason", response = RejectReason.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAllRejectReason(@PathVariable Integer unitId) throws Exception {
		return new ResponseEntity<Object>(GlobalService.getRejectReasonList(unitId), HttpStatus.OK);
	}

	@RequestMapping(value = "/cancel/reasons", method = RequestMethod.GET)
	@ApiOperation(value = "Get list Of Cancel Reason", response = CancelReason.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAllCancelReason() throws Exception {
		return new ResponseEntity<Object>(GlobalService.getCancelReasonList(), HttpStatus.OK);
	}

	@RequestMapping(value = "/user/stores/{orgId}/{unitId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list Of stores mapped to the user", response = Store.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getUserStore(@PathVariable Integer orgId, @PathVariable Integer unitId,
			@PathVariable Integer userId) throws Exception {
		return new ResponseEntity<Object>(GlobalService.getUserStoreList(orgId, unitId, userId), HttpStatus.OK);
	}

	/* Dropdown For storeType */
	@RequestMapping(value = "/store/type/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list Of stores types", response = Store.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getStoreType(@PathVariable Integer orgId) throws Exception {
		return new ResponseEntity<Object>(GlobalService.getStoreTypeList(orgId), HttpStatus.OK);
	}

	@RequestMapping(value = "/stores/indents/{unitId}/{fromStoreId}/{toStoreId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get list Of store indents", response = Store.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getStoreIndents(@PathVariable Integer unitId,
			@PathVariable Integer fromStoreId, @PathVariable Integer toStoreId, @RequestBody DateRequest dateRequest)
			throws Exception {
		dateRequest.setUnitId(unitId);
		return new ResponseEntity<Object>(GlobalService.getStoreIndentList(fromStoreId, toStoreId, dateRequest),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/patient/search/{unitId}/{name}/{visitType}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Patient details list ", response = PatientDetailResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientIndentPatientSearch(@PathVariable("unitId") Integer unitId,
			@PathVariable("name") String name, @PathVariable("visitType") Integer visitType,
			@RequestBody PatientDetailRequest patientDetailsSearch) {
		try {
			patientDetailsSearch.setUnitId(unitId);
			patientDetailsSearch.setVisitType(visitType);
			return new ResponseEntity<Object>(GlobalService.patientIndentPatientSearch(patientDetailsSearch),
					HttpStatus.OK);

		} catch (Exception ex) {
			logger.error("Error while searching Patient details", ex);
			return new ResponseEntity<Object>(new ErrorResponse("", "Error while searching Patient details", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/patient/search/patientIndent/{orgId}/{unitId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Patient details list ", response = PatientDetailResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientIndentPatientSearchIndent(@PathVariable Integer orgId,
			@PathVariable Integer unitId, @RequestBody PatientDetailRequest patientDetailsSearch) {
		try {
			return new ResponseEntity<Object>(
					GlobalService.patientIndentPatientSearchIndent(patientDetailsSearch, unitId, orgId), HttpStatus.OK);

		} catch (Exception ex) {
			logger.error("Error while searching Patient details", ex);
			return new ResponseEntity<Object>(new ErrorResponse("", "Error while searching Patient details", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/patient/search/patientIndentForNursing", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Patient details list ", response = PatientDetailResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientIndentPatientSearchIndentForNursing(
			@RequestBody PatientDetailRequest patientDetailsSearch) {
		try {
			return new ResponseEntity<Object>(
					GlobalService.patientIndentPatientSearchIndentForNursing(patientDetailsSearch), HttpStatus.OK);

		} catch (Exception ex) {
			logger.error("Error while searching Patient details", ex);
			return new ResponseEntity<Object>(new ErrorResponse("", "Error while searching Patient details", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get user details ", response = PatientDetailResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> userSearch(@PathVariable Integer id) {
		try {
			return new ResponseEntity<Object>(GlobalService.userSearch(id), HttpStatus.OK);

		} catch (Exception ex) {
			logger.error("Error while searching user details", ex);
			return new ResponseEntity<Object>(new ErrorResponse("", "Error while searching user details", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/patient/search/{unitId}/{name}/{visitType}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of item Details By Name", response = Patient.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<Patient>> patientSearch(@PathVariable("unitId") Integer unitId,
			@PathVariable("name") String name, @PathVariable("visitType") Integer visitType) {
		return new ResponseEntity<List<Patient>>(GlobalService.patientSearch(name, visitType, unitId), HttpStatus.OK);
	}

	@RequestMapping(value = "/patient/search/issue/return/{unitId}/{name}/{visitType}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of item Details By Name", response = Patient.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<Patient>> patientSearchIssueReturn(@PathVariable("unitId") Integer unitId,
			@PathVariable("name") String name, @PathVariable("visitType") Integer visitType) {
		return new ResponseEntity<List<Patient>>(GlobalService.patientSearchIssueReturn(unitId, name, visitType),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/patient/search/patient/indent/{unitId}/{name}/{visitType}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of item Details By Name", response = Patient.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<Patient>> patientSearchPatientIndent(@PathVariable("unitId") Integer unitId,
			@PathVariable("name") String name, @PathVariable("visitType") Integer visitType) {
		return new ResponseEntity<List<Patient>>(GlobalService.patientSearchPatientIndent(unitId, name, visitType),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/patient/search/{unitId}/{name}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of patient Details By Name", response = Patient.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<Patient>> billListPatientSearch(@PathVariable Integer unitId,
			@PathVariable("name") String name) {
		return new ResponseEntity<List<Patient>>(GlobalService.billListPatientSearch(name, unitId), HttpStatus.OK);
	}

	@RequestMapping(value = "/patient/search/uhid/{uhid}/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of item Details By uhid", response = Patient.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<Patient>> patientSearchByUhid(@PathVariable("uhid") String uhid,
			@PathVariable("orgId") Integer orgId) {
		return new ResponseEntity<List<Patient>>(GlobalService.patientSearchByUhid(uhid, orgId), HttpStatus.OK);
	}

	@RequestMapping(value = "patient/search/sales/return/uhid/{uhid}/{visitType}/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of item Details By uhid", response = Patient.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<Patient>> patientSearchByUhid(@PathVariable("uhid") String uhid,
			@PathVariable("visitType") Integer visitType, @PathVariable("orgId") Integer orgId) {
		return new ResponseEntity<List<Patient>>(GlobalService.salesReturnPatientSearchByUhid(uhid, visitType, orgId),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/doctor/search/{orgId}/{unitId}/{name}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of item Details By Name", response = Doctor.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<Doctor>> doctorSearch(@PathVariable("orgId") Integer orgId,
			@PathVariable("unitId") Integer unitId, @PathVariable("name") String name) {
		return new ResponseEntity<List<Doctor>>(GlobalService.doctorSearch(orgId, unitId, name), HttpStatus.OK);
	}

	@RequestMapping(value = "/ward", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of wards", response = Doctor.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<Ward>> getWardList() {
		return new ResponseEntity<List<Ward>>(GlobalService.getWardList(), HttpStatus.OK);
	}

	@RequestMapping(value = "/floor", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of floors", response = Doctor.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<Floor>> getFloorList() {
		return new ResponseEntity<List<Floor>>(GlobalService.getFloorList(), HttpStatus.OK);
	}

	@RequestMapping(value = "/vendor/search/{name}/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of Purchase Order", notes = "Get list of  Purchase Order", response = VendorResponseDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getItemRequestVendorAutoPopulateList(@PathVariable("name") String name,
			@PathVariable("unitId") Integer unitId) throws Exception {
		return new ResponseEntity<Object>(GlobalService.getVendorList(name, unitId), HttpStatus.OK);

	}

	@RequestMapping(value = "/service/provider/search/{orgId}/{name}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of Purchase Order", notes = "Get list of  Purchase Order", response = VendorResponseDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getServiceProviderList(@PathVariable("orgId") Integer orgId,
			@PathVariable("name") String name) throws Exception {
		return new ResponseEntity<Object>(GlobalService.getServiceProviderList(name, orgId), HttpStatus.OK);

	}

	@RequestMapping(value = "/insurance/company/search/{unitId}/{name}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of Purchase Order", notes = "Get list of  Purchase Order", response = InsuranceCompanyDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getInsuranceCompanyList(@PathVariable("unitId") Integer unitId,
			@PathVariable("name") String name) throws Exception {
		return new ResponseEntity<Object>(GlobalService.getInsuranceCompanyList(name, unitId), HttpStatus.OK);

	}

	@RequestMapping(value = "/manufacturer/search/{orgId}/{name}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of Purchase Order", notes = "Get list of  Purchase Order", response = ManufacturerResponseDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getItemRequestManufacturerAutoPopulateList(@PathVariable("orgId") Integer orgId,
			@PathVariable("name") String name) throws Exception {
		return new ResponseEntity<Object>(GlobalService.getManufacturerList(name, orgId), HttpStatus.OK);

	}

	@RequestMapping(value = "/against/purchase/request/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get list of Purchase Request", notes = "Get list of  Purchase Request", response = PurchaseRequestSearchDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAgainstPurchseRequestSearch(
			@RequestBody AgainstPurchaseRequest againstPurchaseRequest) throws Exception {

		return new ResponseEntity<Object>(GlobalService.getAgainstPurchseRequestSearch(againstPurchaseRequest),
				HttpStatus.OK);

	}

	@RequestMapping(value = "/against/quotation/request/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get list of Purchase Request", notes = "Get list of  Purchase Request", response = PurchaseRequestSearchDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAgainstQuotationRequestSearch(
			@RequestBody AgainstPurchaseRequest againstPurchaseRequest) throws Exception {

		return new ResponseEntity<Object>(GlobalService.getAgainstQuotationRequestSearch(againstPurchaseRequest),
				HttpStatus.OK);

	}

	@RequestMapping(value = "/against/WOQuotation/request/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get list of Purchase Request", notes = "Get list of  Purchase Request", response = PurchaseRequestSearchDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAgainstWOQuotationRequestSearch(
			@RequestBody AgainstWORequest againstWORequest) throws Exception {

		return new ResponseEntity<Object>(GlobalService.getAgainstWOQuotationRequestSearch(againstWORequest),
				HttpStatus.OK);

	}

	@RequestMapping(value = "/against/purchase/request/details/{orgId}/{unitId}", method = RequestMethod.POST)
	@ApiOperation(value = "Get list items against purchase requests  ", response = PurchaseRequestSearchDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getItemDetailsAgainstPurchaseRequest(
			@RequestBody List<AgainstPurchaseRequest> againstPurchaseRequestList, @PathVariable("orgId") Integer orgId,
			@PathVariable("unitId") Integer unitId) throws Exception {

		return new ResponseEntity<Object>(
				GlobalService.getItemsDetailsAgainstPurchseRequest(againstPurchaseRequestList, orgId, unitId),
				HttpStatus.OK);

	}

	@RequestMapping(value = "/against/quotation/request/details/{orgId}/{unitId}", method = RequestMethod.POST)
	@ApiOperation(value = "Get list items against purchase requests  ", response = PurchaseRequestSearchDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getItemDetailsAgainstQuotationRequest(
			@PathVariable("orgId") Integer orgId, @PathVariable("unitId") Integer unitId,
			@RequestBody List<AgainstQuotationRequest> againstQuotationRequestList) throws Exception {
		return new ResponseEntity<Object>(
				GlobalService.getItemsDetailsAgainstQuotationRequest(againstQuotationRequestList, orgId, unitId),
				HttpStatus.OK);

	}

	@RequestMapping(value = "/against/quotation/request/details/{orgId}/{unitId}/{vendorId}", method = RequestMethod.POST)
	@ApiOperation(value = "Get list items against purchase requests  ", response = PurchaseRequestSearchDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getItemDetailsAgainstQuotationRequestByVendorId(
			@RequestBody List<AgainstQuotationRequest> againstQuotationRequestList, @PathVariable Integer vendorId,
			@PathVariable Integer orgId, @PathVariable Integer unitId) throws Exception {
		return new ResponseEntity<Object>(GlobalService.getItemDetailsAgainstQuotationRequestByVendorId(
				againstQuotationRequestList, vendorId, orgId, unitId), HttpStatus.OK);

	}

	@RequestMapping(value = "/direct/purchase/request/details/{orgId}/{unitId}", method = RequestMethod.POST)
	@ApiOperation(value = "Get list of items direct Purchase Request", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getItemDetailsDirectPurchaseRequest(
			@RequestBody AgainstPurchaseRequest againstPurchaseRequest, @PathVariable("orgId") Integer orgId,
			@PathVariable("unitId") Integer unitId) throws Exception {

		againstPurchaseRequest.setUnitId(unitId);
		againstPurchaseRequest.setOrganizationId(orgId);
		return new ResponseEntity<Object>(GlobalService.getItemDetailsDirectPurchaseRequest(againstPurchaseRequest),
				HttpStatus.OK);

	}

	@RequestMapping(value = "/direct/purchase/requestWithPendingQty/details/{orgId}/{unitId}", method = RequestMethod.POST)
	@ApiOperation(value = "Get list of items direct Purchase Request", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getItemDetailsDirectPurchaseRequestWithPendingQty(
			@RequestBody AgainstPurchaseRequest againstPurchaseRequest, @PathVariable("orgId") Integer orgId,
			@PathVariable("unitId") Integer unitId) throws Exception {
		againstPurchaseRequest.setOrganizationId(orgId);
		againstPurchaseRequest.setUnitId(unitId);
		return new ResponseEntity<Object>(
				GlobalService.getItemDetailsDirectPurchaseRequestWithPendingQty(againstPurchaseRequest), HttpStatus.OK);

	}

	@RequestMapping(value = "/against/purchase/order/search/{storeId}", method = RequestMethod.POST)
	@ApiOperation(value = "Get list of Purchase Order", notes = "Get list of  Purchase Order", response = AgainstPOSearchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAgainstPurchseOrderSearch(
			@RequestBody AgainstPurchaseOrder againstPurchaseOrder, @PathVariable Integer storeId) throws Exception {
		againstPurchaseOrder.setStoreId(storeId);
		return new ResponseEntity<Object>(GlobalService.getAgainstPurchseOrderSearch(againstPurchaseOrder),
				HttpStatus.OK);

	}

	@RequestMapping(value = "/against/purchase/order/details/org/{orgId}/unit/{unitId}", method = RequestMethod.POST)
	@ApiOperation(value = "Get list items against purchase requests  ", response = PurchaseRequestSearchDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getItemDetailsAgainstPurchaseOrder(
			@RequestBody List<AgainstPurchaseOrder> againstPurchaseOrderList, @PathVariable Integer orgId,
			@PathVariable Integer unitId) throws Exception {
		return new ResponseEntity<Object>(
				GlobalService.getItemDetailsAgainstPurchaseOrder(againstPurchaseOrderList, orgId, unitId),
				HttpStatus.OK);

	}

	@RequestMapping(value = "/fixed/assets/types", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = AssetType.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getFixedAssetTypes() throws JSONException {
		return new ResponseEntity<Object>(GlobalService.getFixedAssetTypes(), HttpStatus.OK);
	}

	@RequestMapping(value = "/racks/{storeId}/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of racks", response = AssetType.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getRacks(@PathVariable Integer storeId, @PathVariable Integer orgId)
			throws JSONException {
		return new ResponseEntity<Object>(GlobalService.getRacks(storeId, orgId), HttpStatus.OK);
	}

	@RequestMapping(value = "/racks/shelves/{rackId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of shelves", response = AssetType.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getShelves(@PathVariable Integer rackId) throws JSONException {
		return new ResponseEntity<Object>(GlobalService.getShelves(rackId), HttpStatus.OK);
	}

	@RequestMapping(value = "/against/grn", method = RequestMethod.POST)
	@ApiOperation(value = "Get list of GRN", notes = "Get list of GRN", response = AgainstPOSearchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAgainstGrn(@RequestBody AgainstGrnRequest request) throws Exception {
		return new ResponseEntity<Object>(GlobalService.getAgainstGrn(request), HttpStatus.OK);
	}

	@RequestMapping(value = "/against/grn/return", method = RequestMethod.POST)
	@ApiOperation(value = "Get list of GRN", notes = "Get list of GRN", response = AgainstPOSearchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAgainstGrnReturn(@RequestBody AgainstGrnRequest request)
			throws Exception {
		return new ResponseEntity<Object>(GlobalService.getAgainstGrnReturn(request), HttpStatus.OK);
	}

	@RequestMapping(value = "/against/rejectedgrn", method = RequestMethod.POST)
	@ApiOperation(value = "Get list of GRN", notes = "Get list of GRN", response = AgainstPOSearchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAgainstrjectedGrn(@RequestBody AgainstGrnRequest request)
			throws Exception {
		return new ResponseEntity<Object>(GlobalService.getAgainstrjectedGrn(request), HttpStatus.OK);
	}

	// for Barcode Printing Get Approve Qty API

	@RequestMapping(value = "/against/grn/ApprovedQty", method = RequestMethod.POST)

	@ApiOperation(value = "Get list of GRN", notes = "Get list of GRN", response = AgainstPOSearchResponse.class)

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),

			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),

			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getBarcodeAgainstGrn(@RequestBody AgainstGrnRequest request)
			throws Exception {
		return new ResponseEntity<Object>(GlobalService.getBarcodeAgainstGrn(request), HttpStatus.OK);
	}

	@RequestMapping(value = "/scrap/reasons/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list Of Scrap Reason", response = ScrapReason.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAllScrapReason(@PathVariable Integer orgId) throws Exception {
		return new ResponseEntity<Object>(GlobalService.getScrapReasonList(orgId), HttpStatus.OK);
	}

	@RequestMapping(value = "/inWardBy/search/{orgId}/{unitId}{name}", method = RequestMethod.GET)
	@ApiOperation(value = "Get InWard By list ", response = User.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> inWardBySearch(@PathVariable Integer orgId, @PathVariable Integer unitId,
			@PathVariable("name") String name) {
		try {
			return new ResponseEntity<Object>(GlobalService.inWardBySearch(name, unitId, orgId), HttpStatus.OK);

		} catch (Exception ex) {
			logger.error("Error while searching inward by", ex);
			return new ResponseEntity<Object>(new ErrorResponse("", "Error while searching inward by", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/items/dropdown/{orgId}/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = ItemDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdown(@PathVariable Integer unitId, @PathVariable Integer orgId)
			throws Exception {
		return new ResponseEntity<Object>(GlobalService.getItemDropdowns(unitId, orgId), HttpStatus.OK);
	}

	@RequestMapping(value = "/tax/components/taxcomponentlist", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = TaxComponentDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getTaxComponentList() throws JSONException {
		return new ResponseEntity<Object>(GlobalService.getTaxComponentList(), HttpStatus.OK);
	}

	@RequestMapping(value = "/formulationTypeAndROA/dropdown", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = FormulationTypeAndROAList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getFormulationTypeAndROA() throws JSONException {
		return new ResponseEntity<Object>(GlobalService.getFormulationTypeAndROADropdown(), HttpStatus.OK);
	}

	@RequestMapping(value = "/storewiseGenericMapper/dropdown/{orgId}/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = StorewiseGenericMapperDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getStorewiseGenericDropdown(@PathVariable Integer unitId,
			@PathVariable Integer orgId) throws JSONException {
		return new ResponseEntity<Object>(GlobalService.getStorewiseGenericDropdown(unitId, orgId), HttpStatus.OK);
	}

	@RequestMapping(value = "/purchase/order/poHistory/{unitId}/{itemId}/{poId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for Purchase Order History", response = PurchaseOrderHistory.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getPurchaseOrderHistoryByItemId(@PathVariable Integer unitId,
			@PathVariable Integer itemId, @PathVariable Integer poId) throws JSONException {
		return new ResponseEntity<Object>(GlobalService.getPurchaseOrderHistoryByItemId(itemId, unitId, poId),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/purchase/order/poHistory/{unitId}/{itemId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for Purchase Order History", response = PurchaseOrderHistory.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getPurchaseOrderHistoryByItemId1(@PathVariable Integer unitId,
			@PathVariable Integer itemId) throws JSONException {
		return new ResponseEntity<Object>(GlobalService.getPurchaseOrderHistoryByItemId(itemId, unitId, 0),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/stock/consumption", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update batch and stock to minus consumed stocks", response = PurchaseOrderHistory.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> updateStockConsuption(@RequestBody StoreStockRequestDto store) {
		/* try { */

		return new ResponseEntity<Object>(GlobalService.manageStockConsumption(store), HttpStatus.OK);
		/*
		 * } catch (Exception e) {  return new
		 * ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); }
		 */

	}

	@RequestMapping(value = "/frequencyList/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of frequency", response = PMSFrequency.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getFrequencyList(@PathVariable("orgId") Integer orgId)
			throws JSONException {
		return new ResponseEntity<Object>(GlobalService.getFrequencyList(orgId), HttpStatus.OK);
	}

	/*
	 * @RequestMapping(value = "/frequencyList", method = RequestMethod.GET)
	 * 
	 * @ApiOperation(value = "Get list of frequency", response = PMSFrequency.class)
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
	 * 
	 * @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code =
	 * 403, message = "forbidden!!!"),
	 * 
	 * @ApiResponse(code = 404, message = "not found!!!") }) public @ResponseBody
	 * ResponseEntity<Object> getFrequencyList() throws Exception { return new
	 * ResponseEntity<Object>(GlobalService.getFrequencyList(), HttpStatus.OK); }
	 */
	@RequestMapping(value = "/allvendor/search/unit/{unitId}/vendorName/{vendorName}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of Purchase Order", notes = "Get list of  Purchase Order", response = VendorResponseDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getItemRequestAllVendorAutoPopulateList(@PathVariable("unitId") Integer unitId,@PathVariable("vendorName") String vendorName)
			throws Exception {
		return new ResponseEntity<Object>(GlobalService.getAllVendorList(unitId,vendorName), HttpStatus.OK);

	}

	@RequestMapping(value = "/cancel/prescription/{prescriptionId}", method = RequestMethod.GET)
	@ApiOperation(value = "Cancel the prescription", notes = "Cancel the prescription", response = VendorResponseDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelPrescription(@PathVariable("prescriptionId") Integer prescriptionId)
			throws Exception {
		GlobalService.cancelPrescription(prescriptionId);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@RequestMapping(value = "/tariff", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Patient details list ", response = PatientDetailResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getTariff(@RequestBody TariffRequest tariffReq) {
		try {
			List<TariffResponse> tariffResps = GlobalService.getTariffData(tariffReq);
			return new ResponseEntity<Object>(tariffResps, HttpStatus.OK);

		} catch (Exception ex) {
			logger.error("Error while fetching tariff data", ex);
			return new ResponseEntity<Object>(new ErrorResponse("", "Error while fetching tariff data", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/getPatientNameSearchByName", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response getPatientCategoryMaster(@RequestBody PatientDetailRequest patientDetailsSearch) {
		try {
			return GlobalService.getPatientNameSearchByName(patientDetailsSearch);
		} catch (ApplicationException ae) {
			logger.error("Error while fetching the patient name search by name", ae);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@RequestMapping(value = "/inWardBy/searchUser/{organizationId}/{unitId}/{name}", method = RequestMethod.GET)
	@ApiOperation(value = "Get InWard By list ", response = User.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> inWardBySearchUser(@PathVariable("organizationId") Integer organizationId,
			@PathVariable("unitId") Integer unitId, @PathVariable("name") String name) {
		try {
			return new ResponseEntity<Object>(GlobalService.inWardBySearchUser(organizationId, unitId, name),
					HttpStatus.OK);

		} catch (Exception ex) {
			logger.error("Error while searching inward by", ex);
			return new ResponseEntity<Object>(new ErrorResponse("", "Error while searching inward by", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
