package com.param.global.controller;

import com.param.global.common.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.ErrorResponse;
import com.param.global.dto.items.BatchListResponse;
import com.param.global.dto.items.ItemsDetailsListResponse;
import com.param.global.dto.items.ItemsDetailsResponse;
import com.param.global.dto.items.ItemsRequest;
import com.param.global.dto.items.ItemsSearchResponse;
import com.param.global.service.items.CustomException;
import com.param.global.service.items.IItemsService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
import static com.param.global.common.ICommonConstants.*;

@RestController
@RequestMapping(value = "/api/global/items")
public class ItemsController {
	final static Logger logger = Logger.getLogger(ItemsController.class);


	@Autowired
	IItemsService ItemsService;

	@RequestMapping(value = "{unitId}/{isOTC}/{search}/{storeId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of items", response = ItemsSearchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> search(@PathVariable("unitId") Integer unitId,@PathVariable("isOTC") boolean isOtc, @PathVariable("search") String search,@PathVariable("storeId") Integer storeId)
			throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.itemsSearch(unitId,isOtc, search,storeId), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("search failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0101", "Error while fetching the items list", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/searchbarcodescanitem/{itemId}/{batchId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get barcode scan list of items", response = ItemsSearchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> searchBarcodeScanItem( @PathVariable("itemId") Integer itemId, @PathVariable("batchId") Integer batchId)
			throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.searchBarcodeScanItem( itemId , batchId), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("searchBarcodeScanItem failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0101", "Error while fetching the items list", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value = "/searchgenericscanitem/{productcode}", method = RequestMethod.GET)
	@ApiOperation(value = "Get barcode scan list of items", response = ItemsSearchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> searchGenericScanItem( @PathVariable("itemId") Integer itemId)
			throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.searchGenericScanItem(itemId), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("searchGenericScanItem failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0101", "Error while fetching the items list", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/{unitId}/{storeId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get item details by itemId", response = ItemsDetailsResponse.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> itemDetail(@PathVariable int unitId,@PathVariable int storeId,@PathVariable int id) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.itemDetail(id,unitId,storeId), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("itemDetail failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0102", "Error while fetching the item detail", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value = "/details/org/{orgId}/unit/{unitId}/{storeId}/{id}/{entitlementId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get item details by itemId", response = ItemsDetailsResponse.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> detail(@PathVariable("id") int id, @PathVariable("entitlementId") int entitlementId,@PathVariable("unitId") int unitId, @PathVariable("orgId") int orgId,@PathVariable("storeId") int storeId) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.itemsDetail(id, entitlementId,unitId,orgId,storeId), HttpStatus.OK);
		} catch (CustomException ex) {
		logger.error("detail failed", ex);
            return new ResponseEntity<Object>(
                    new ErrorResponse("HIS-0102", "Selected item is out of stock", ex),
                    HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("detail failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0102", "Error while fetching the item detail", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/details/org/{orgId}/unit/{unitId}/{storeId}/{id}/{entitlementId}/{encounterId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get item details by itemId", response = ItemsDetailsResponse.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> detailSalesWorkList(@PathVariable("id") int id, 
			@PathVariable("entitlementId") int entitlementId,
			@PathVariable("encounterId") int encounterId,@PathVariable("unitId") int unitId, @PathVariable("orgId") int orgId,@PathVariable("storeId") int storeId) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.itemsDetailSalesWorkList(id, entitlementId, encounterId,null,null,unitId,orgId,storeId), HttpStatus.OK);
		} catch (CustomException ex) {
		logger.error("detailSalesWorkList failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0102", "Selected item is out of stock", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception ex) {
		logger.error("detailSalesWorkList failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0102", "Error while fetching the item detail", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value = "/details/walkin", method = RequestMethod.POST)
	@ApiOperation(value = "Get item details by itemId", response = ItemsDetailsResponse.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> detailSalesWorkList(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.itemsDetailSalesWorkList(itemsRequest.getItemId(),
					itemsRequest.getEntitlementId(), itemsRequest.getEncounterId(), itemsRequest.getPatientCategoryId(),
					itemsRequest.getPatientClassId(), itemsRequest.getUnitId(), itemsRequest.getOrganizationId(),
					itemsRequest.getStoreId()), HttpStatus.OK);
		} catch (CustomException ex) {
		logger.error("detailSalesWorkList failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0102", "Selected item is out of stock", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception ex) {
		logger.error("detailSalesWorkList failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0102", "Error while fetching the item detail", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	

	@RequestMapping(value = "/details/ip/{unitId}/{id}/{entitlementId}/{admissionId}/{storeId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get item details by itemId", response = ItemsDetailsResponse.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> itemsDetailSalesWorkListByAdmission(@PathVariable int unitId,@PathVariable("id") int id, 
			@PathVariable("entitlementId") int entitlementId,
			@PathVariable("admissionId") int admissionId,
			@PathVariable("storeId") int storeId) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.itemsDetailSalesWorkListByAdmission(id, entitlementId, admissionId,storeId,unitId), HttpStatus.OK);
		} catch (CustomException ex) {
		logger.error("itemsDetailSalesWorkListByAdmission failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0102", "Selected item is out of stock", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception ex) {
		logger.error("itemsDetailSalesWorkListByAdmission failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0102", "Error while fetching the item detail", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "unit/{unitId}", method = RequestMethod.POST)
	@ApiOperation(value = "Get items list", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getItemsList(@RequestBody ItemsRequest itemsRequest,@PathVariable("unitId") Integer unitId) throws Exception {
		try {
			itemsRequest.setUnitId(unitId);
			return new ResponseEntity<Object>(ItemsService.itemsList(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("getItemsList failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0103", "Error while fetching the items list", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/storeIndent", method = RequestMethod.POST)
	@ApiOperation(value = "Get items list", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getItemsForStoreIndentList(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.getItemsForStoreIndentList(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("getItemsForStoreIndentList failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0103", "Error while fetching the items list", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/consumableIndent", method = RequestMethod.POST)
	@ApiOperation(value = "Get consumable items list", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getConsumableItemsList(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.itemsListConsumable(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("getConsumableItemsList failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0103", "Error while fetching the items list", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/consumableIndent/consignment", method = RequestMethod.POST)
	@ApiOperation(value = "Get consumable items list", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getConsumableItemsListByConsignment(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.consumableItemsListByConsignment(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("getConsumableItemsListByConsignment failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0103", "Error while fetching the items list", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/consumableIndent/consignment/indent", method = RequestMethod.POST)
	@ApiOperation(value = "Get consumable items list", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getConsumableItemsListByConsignmentIndent(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.consumableItemsListByConsignmentIndent(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("getConsumableItemsListByConsignmentIndent failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0103", "Error while fetching the items list", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/searchByTax", method = RequestMethod.POST)
	@ApiOperation(value = "Get items list", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getItemsListWithTax(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.itemsListWithTax(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("getItemsListWithTax failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0103", "Error while fetching the items list", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/details/org/{orgId}/unit/{unitId}", method = RequestMethod.POST)
	@ApiOperation(value = "Get items details by itemIds", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> details(@PathVariable("orgId") int orgId,@PathVariable("unitId") int unitId,@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			itemsRequest.setOrganizationId(orgId);
			itemsRequest.setUnitId(unitId);
			return new ResponseEntity<Object>(ItemsService.itemsDetails(itemsRequest), HttpStatus.OK);
		}

		catch (Exception ex) {
		logger.error("details failed", ex);
		return new ResponseEntity<Object>(
				new ErrorResponse("HIS-0104", "Error while fetching the items details", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/details/consumable/indent", method = RequestMethod.POST)
	@ApiOperation(value = "Get items details by itemIds", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> detailsConsumableIndent(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.itemsDetailsOpeningBalancePT(itemsRequest), HttpStatus.OK);
		}

		catch (Exception ex) {
		logger.error("detailsConsumableIndent failed", ex);
		return new ResponseEntity<Object>(
				new ErrorResponse("HIS-0104", "Error while fetching the items details", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/details/opening/balance", method = RequestMethod.POST)
	@ApiOperation(value = "Get items details by itemIds", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> detailsOpeningBalance(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.itemsDetailsOpeningBalance(itemsRequest), HttpStatus.OK);
		}

		catch (Exception ex) {
		logger.error("detailsOpeningBalance failed", ex);
		return new ResponseEntity<Object>(
				new ErrorResponse("HIS-0104", "Error while fetching the items details", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@RequestMapping(value = "/details/grnWithPendingqty", method = RequestMethod.POST)
	@ApiOperation(value = "Get items details by itemIds", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> detailsGrnWithPendingqty(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.itemsDetailsForGRNWithPendingQty(itemsRequest), HttpStatus.OK);
		}

		catch (Exception ex) {
		logger.error("detailsGrnWithPendingqty failed", ex);
		return new ResponseEntity<Object>(
				new ErrorResponse("HIS-0104", "Error while fetching the items details", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/directPR/details", method = RequestMethod.POST)
	@ApiOperation(value = "Get items details by itemIds", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> directPOdetails(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.itemsDetailsForDirectPurchaseRequest(itemsRequest), HttpStatus.OK);
		}

		catch (Exception ex) {
		logger.error("directPOdetails failed", ex);
		return new ResponseEntity<Object>(
				new ErrorResponse("HIS-0104", "Error while fetching the items details", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/batches", method = RequestMethod.POST)
	@ApiOperation(value = "Get batch list", response = BatchListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> batchList(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.batchList(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("batchList failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0105", "Error while fetching the batch list", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/batches/billableConsumption/new", method = RequestMethod.POST)
	@ApiOperation(value = "Get batch list", response = BatchListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> batchListBillableConsumption(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.batchListBillableConsumption(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("batchListBillableConsumption failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0105", "Error while fetching the batch list", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/batches/billableConsumption/unit/{unitId}", method = RequestMethod.POST)
	@ApiOperation(value = "Get batch list", response = BatchListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> batchListBillableConsumptionNew(@PathVariable("unitId") int unitId, @RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			itemsRequest.setUnitId(unitId);
			return new ResponseEntity<Object>(ItemsService.batchListBillableConsumptionNew(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("batchListBillableConsumptionNew failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0105", "Error while fetching the batch list", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/batches/billableConsumption/storeWise", method = RequestMethod.POST)
	@ApiOperation(value = "Get batch list", response = BatchListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> batchListBillableConsumptionStoreWise(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.batchListBillableConsumptionStoreWise(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("batchListBillableConsumptionStoreWise failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0105", "Error while fetching the batch list", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/batches/billableConsumption/storeWise/withoutGeneric", method = RequestMethod.POST)
	@ApiOperation(value = "Get batch list Without Generic", response = BatchListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> batchListBillableConsumptionStoreWiseWithoutGeneric(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.batchListBillableConsumptionStoreWiseWithoutGeneric(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("batchListBillableConsumptionStoreWiseWithoutGeneric failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0105", "Error while fetching the batch list", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@RequestMapping(value = "/batches/availableQty", method = RequestMethod.POST)
	@ApiOperation(value = "Get batch list", response = BatchListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> batchListWithAvailableQty(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.batchListWithAvailableQty(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("batchListWithAvailableQty failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0105", "Error while fetching the batch list", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/batches/tariff", method = RequestMethod.POST)
	@ApiOperation(value = "Get batch list", response = BatchListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> batchListWithTariff(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.batchListWithTariff(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("batchListWithTariff failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0105", "Error while fetching the batch list", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/batches/{isOTC}/{search}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of batches", response = ItemsSearchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> batchSearch(@PathVariable("isOTC") boolean isOtc,
			@PathVariable("search") String search) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.batchSearch(isOtc, search), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("batchSearch failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0106", "Error while fetching the batch list", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/workOrder", method = RequestMethod.POST)
	@ApiOperation(value = "Get items list", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getWorkOrderItemsList(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.workOrderItemsList(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("getWorkOrderItemsList failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0103", "Error while fetching the items list", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/batches/details", method = RequestMethod.POST)
	@ApiOperation(value = "Get items and batch details by itemIds", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> batchDetails(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.batchDetails(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("batchDetails failed", ex);
		return new ResponseEntity<Object>(
				new ErrorResponse("HIS-0107", "Error while fetching the items details", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/cssd/batches/details", method = RequestMethod.POST)
	@ApiOperation(value = "Get items and batch details by itemIds", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> batchDetailsCSSD(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.batchDetailsCSSD(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("batchDetailsCSSD failed", ex);
		return new ResponseEntity<Object>(
				new ErrorResponse("HIS-0107", "Error while fetching the items details", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*/ Items by near expiry /*/
	@RequestMapping(value = "/batches/near/expiry", method = RequestMethod.POST)
	@ApiOperation(value = "Get items list", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getItemsListByExpiry(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.itemsListByBatchExpiry(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("getItemsListByExpiry failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0108", "Error while fetching the items list", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/details/batches", method = RequestMethod.POST)
	@ApiOperation(value = "Get items details by itemIds", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> detailsWithBatches(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.itemsDetailsWithBatch(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("detailsWithBatches failed", ex);
		return new ResponseEntity<Object>(
				new ErrorResponse("HIS-0104", "Error while fetching the items details", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/workOrder/details/batches", method = RequestMethod.POST)
	@ApiOperation(value = "Get items details by itemIds", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> detailsWithBatchesForWorkOrder(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.itemsDetailsWithBatchForWorkOrder(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("detailsWithBatchesForWorkOrder failed", ex);
		return new ResponseEntity<Object>(
				new ErrorResponse("HIS-0104", "Error while fetching the items details", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/expiryAndAvailableQuantityWisebatches/{storeId}/{unitId}/{isOTC}/{search}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of batches", response = ItemsSearchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> expiryAndAvailableQuantityWisebatches(@PathVariable("isOTC") boolean isOtc,
			@PathVariable("search") String search,@PathVariable("unitId") int unitId,@PathVariable("storeId") int storeId) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.expiryAndAvailableQuantityWisebatches(isOtc, search,unitId,storeId),
					HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("expiryAndAvailableQuantityWisebatches failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0109", "Error while fetching the batch list", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/batches/{isPharmacy}/{isReturn}", method = RequestMethod.POST)
	@ApiOperation(value = "Get batch list", response = BatchListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> batchList(@RequestBody ItemsRequest itemsRequest,
			@PathVariable("isPharmacy") boolean isPharmacy, @PathVariable("isReturn") boolean isReturn)
			throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.batchList(itemsRequest, isPharmacy, isReturn),
					HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("batchList failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0110", "Error while fetching the batch list", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/smartissue", method = RequestMethod.POST)
	@ApiOperation(value = "Smart Issue", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> smartIssue(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			// return new ResponseEntity<Object>(ItemsService.batchDetails(itemsRequest),
			// HttpStatus.OK);
			return new ResponseEntity<Object>(ItemsService.smartIssue(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
		logger.error("smartIssue failed", ex);
		return new ResponseEntity<Object>(
				new ErrorResponse("HIS-0111", "Error while fetching the items details", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//get Otem Details with Store Id
    	@RequestMapping(value = "/{id}/store/{storeId}/{unitId}", method = RequestMethod.GET)
		@ApiOperation(value = "Get item details by itemId", response = ItemsDetailsResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
        public ResponseEntity<Object> detail(@PathVariable("id")int id,@PathVariable("storeId") int storeId,@PathVariable("unitId") int unitId) throws Exception {
			try {
                return new ResponseEntity<Object>(ItemsService.itemsDetailsWithStoreId(id,storeId,unitId), HttpStatus.OK);
			} catch (CustomException ex) {
			logger.error("detail failed", ex);
			return new ResponseEntity<Object>(new ErrorResponse("HIS-0102", "Selected item is out of stock", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
			} catch (Exception ex) {
			logger.error("detail failed", ex);
			return new ResponseEntity<Object>(new ErrorResponse("HIS-0102", "Error while fetching the item detail", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		@RequestMapping(value = "/{isConsignment}", method = RequestMethod.POST)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> getIsConsignedItemsList(@RequestBody ItemsRequest itemsRequest, 
				@PathVariable("isConsignment") boolean isConsignment) throws Exception {
			try {
				return new ResponseEntity<Object>(ItemsService.getIsConsignedItemsList(itemsRequest, isConsignment), HttpStatus.OK);
			} catch (Exception ex) {
			logger.error("getIsConsignedItemsList failed", ex);
			return new ResponseEntity<Object>(new ErrorResponse("HIS-0103", "Error while fetching the items list", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		
		@RequestMapping(value = "/batches/{isConsignment}", method = RequestMethod.POST)
		@ApiOperation(value = "Get consigned batch list", response = BatchListResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> consignedBatchList(@RequestBody ItemsRequest itemsRequest, 
				@PathVariable("isConsignment") boolean isConsignment) throws Exception {
			try {
				return new ResponseEntity<Object>(ItemsService.consignedBatchList(itemsRequest, isConsignment), HttpStatus.OK);
			} catch (Exception ex) {
			logger.error("consignedBatchList failed", ex);
			return new ResponseEntity<Object>(new ErrorResponse("HIS-0105", "Error while fetching the batch list", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		@RequestMapping(value = "/getItemList/onTemplet", method = RequestMethod.POST)
		@ApiOperation(value = "Get items list", response = ItemsDetailsListResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> getItemsListOnTemplet(@RequestBody ItemsRequest itemsRequest) throws Exception {
			try {
				return new ResponseEntity<Object>(ItemsService.getItemsListOnTemplet(itemsRequest), HttpStatus.OK);
			} catch (Exception ex) {
			logger.error("getItemsListOnTemplet failed", ex);
			return new ResponseEntity<Object>(new ErrorResponse("HIS-0103", "Error while fetching the items list", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		@RequestMapping(value = "/otims/getItemAndBatchList", method = RequestMethod.POST)
		@ApiOperation(value = "Get Item and batch list", response = BatchListResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> itemAndBatchList(@RequestBody ItemsRequest itemsRequest) throws Exception {
			try {
				return new ResponseEntity<Object>(ItemsService.itemAndBatchList(itemsRequest), HttpStatus.OK);
			} catch (Exception ex) {
			logger.error("itemAndBatchList failed", ex);
			return new ResponseEntity<Object>(new ErrorResponse("HIS-0105", "Error while fetching the batch list", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		
		/*
		 * Method Author : sharad Rindhe(30-09-2019)
		 * Purpose : Search item based on generic name
		 * */
		
		@RequestMapping(value = "/generic/{unitId}/{isOTC}/{search}/{storeId}", method = RequestMethod.GET)
		@ApiOperation(value = "Get list of items based on generic", response = ItemsSearchResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> searchGeneric(@PathVariable("unitId") Integer unitId, @PathVariable("isOTC") boolean isOtc, @PathVariable("search") String search,  @PathVariable("storeId") Integer storeId)
				throws Exception {
			try {
				////System.out.println("in search generic");
				return new ResponseEntity<Object>(ItemsService.searchGeneric(isOtc, search,storeId,unitId), HttpStatus.OK);
			} catch (Exception ex) {
			logger.error("searchGeneric failed", ex);
			return new ResponseEntity<Object>(new ErrorResponse("HIS-0101", "Error while fetching the items list", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		@RequestMapping(value = "/listWithMappedStores/unit/{unitId}", method = RequestMethod.POST)
		@ApiOperation(value = "Get items list", response = ItemsDetailsListResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> getIssuingAndReuestedStoreWiseItemsList(@RequestBody ItemsRequest itemsRequest, @PathVariable("unitId") int unitId) throws Exception {
			try {
				itemsRequest.setUnitId(unitId);
				return new ResponseEntity<Object>(ItemsService.getIssuingAndReuestedStoreWiseItemsList(itemsRequest), HttpStatus.OK);
			} catch (Exception ex) {
			logger.error("getIssuingAndReuestedStoreWiseItemsList failed", ex);
			return new ResponseEntity<Object>(new ErrorResponse("HIS-0103", "Error while fetching the items list", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		@RequestMapping(value = "/listWithMappedStoreAccessories/unit/{unitId}", method = RequestMethod.POST)
		@ApiOperation(value = "Get items list", response = ItemsDetailsListResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> getIssuingAndReuestedStoreWiseItemsListAccessories(@RequestBody ItemsRequest itemsRequest, @PathVariable("unitId") int unitId) throws Exception {
			try {
				itemsRequest.setUnitId(unitId);
				return new ResponseEntity<Object>(ItemsService.getIssuingAndReuestedStoreWiseItemsListAccessories(itemsRequest), HttpStatus.OK);
			} catch (Exception ex) {
			logger.error("getIssuingAndReuestedStoreWiseItemsListAccessories failed", ex);
			return new ResponseEntity<Object>(new ErrorResponse("HIS-0103", "Error while fetching the items list", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		@RequestMapping(value = "/listWithMappedStoresWithCompareAvalablity/unit/{unitId}", method = RequestMethod.POST)
		@ApiOperation(value = "Get items list", response = ItemsDetailsListResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> listWithMappedStoresWithCompareAvalablity(@RequestBody ItemsRequest itemsRequest,@PathVariable("unitId") int unitId) throws Exception {
			try {
					itemsRequest.setUnitId(unitId);
				return new ResponseEntity<Object>(ItemsService.listWithMappedStoresWithCompareAvalablity(itemsRequest), HttpStatus.OK);
			} catch (Exception ex) {
			logger.error("listWithMappedStoresWithCompareAvalablity failed", ex);
			return new ResponseEntity<Object>(new ErrorResponse("HIS-0103", "Error while fetching the items list", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		
		@RequestMapping(value = "getlistofitems", method = RequestMethod.POST)
		@ApiOperation(value = "Get items list", response = ItemsDetailsListResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> getListOfItems(@RequestBody ItemsRequest itemsRequest) throws Exception {
			try {
				return new ResponseEntity<Object>(ItemsService.itemsLgetListOfItemsist(itemsRequest), HttpStatus.OK);
			} catch (Exception ex) {
			logger.error("getListOfItems failed", ex);
			return new ResponseEntity<Object>(new ErrorResponse("HIS-0103", "Error while fetching the items list", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		/*
		 * Method Author : sharad Rindhe(17-10-2019)
		 * Purpose : Search item based on expired medications flag on
		 * */
		@RequestMapping(value = "/batches/expiredmedications", method = RequestMethod.POST)
		@ApiOperation(value = "Get batch list", response = BatchListResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> batchListExpiredMedications(@RequestBody ItemsRequest itemsRequest) throws Exception {
			try {
				return new ResponseEntity<Object>(ItemsService.batchListExpiredMedications(itemsRequest), HttpStatus.OK);
			} catch (Exception ex) {
			logger.error("batchListExpiredMedications failed", ex);
			return new ResponseEntity<Object>(new ErrorResponse("HIS-0105", "Error while fetching the batch list", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		/*
		 * Method Author : sharad Rindhe(17-10-2019)
		 * Purpose : Search item based on expired medications flag OFF
		 * */
		@RequestMapping(value = "/batches/near/expired", method = RequestMethod.POST)
		@ApiOperation(value = "Get items list", response = ItemsDetailsListResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> getItemsListByExpired(@RequestBody ItemsRequest itemsRequest) throws Exception {
			try {
				return new ResponseEntity<Object>(ItemsService.getItemsListByExpired(itemsRequest), HttpStatus.OK);
			} catch (Exception ex) {
			logger.error("getItemsListByExpired failed", ex);
			return new ResponseEntity<Object>(new ErrorResponse("HIS-0108", "Error while fetching the items list", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		/*
		 * Method Author : sharad Rindhe(22-10-2019)
		 * Purpose : Search item based on Accessories and spare flag 
		 * */
		@RequestMapping(value = "/flagwiseitemslist/{unitId}/{id}", method = RequestMethod.POST)
		@ApiOperation(value = "Get items list", response = ItemsDetailsListResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> getFlagWiseItemsList(@PathVariable("id") Integer id,@PathVariable("unitId") Integer unitId, @RequestBody ItemsRequest itemsRequest) throws Exception {
			try {
				itemsRequest.setUnitId(unitId);
				return new ResponseEntity<Object>(ItemsService.getFlagWiseItemsList(id , itemsRequest), HttpStatus.OK);
			} catch (Exception ex) {
			logger.error("getFlagWiseItemsList failed", ex);
			return new ResponseEntity<Object>(new ErrorResponse("HIS-0103", "Error while fetching the items list", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		/*
		 * Method Author : sharad Rindhe(31-10-2019)
		 * Purpose : Search item based on expired date
		 * */
		@RequestMapping(value = "/batches/near/dateexpired", method = RequestMethod.POST)
		@ApiOperation(value = "Get items list", response = ItemsDetailsListResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> getItemsListByDateExpired(@RequestBody ItemsRequest itemsRequest) throws Exception {
			try {
				return new ResponseEntity<Object>(ItemsService.getItemsListByDateExpired(itemsRequest), HttpStatus.OK);
			} catch (Exception ex) {
			logger.error("getItemsListByDateExpired failed", ex);
			return new ResponseEntity<Object>(new ErrorResponse("HIS-0108", "Error while fetching the items list", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		@RequestMapping(value = "/batches/billableConsumption/loanOut", method = RequestMethod.POST)
		@ApiOperation(value = "Get batch list", response = BatchListResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> batchListBillableConsumptionForloanOut(@RequestBody ItemsRequest itemsRequest) throws Exception {
			try {
				return new ResponseEntity<Object>(ItemsService.batchListBillableConsumptionForLoanOut(itemsRequest), HttpStatus.OK);
			} catch (Exception ex) {
			logger.error("batchListBillableConsumptionForloanOut failed", ex);
			return new ResponseEntity<Object>(new ErrorResponse("HIS-0105", "Error while fetching the batch list", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		@RequestMapping(value = "available/unit/{unitId}", method = RequestMethod.POST)
		@ApiOperation(value = "Get items list", response = ItemsDetailsListResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> getItemsList1(@RequestBody ItemsRequest itemsRequest,@PathVariable("unitId") Integer unitId) throws Exception {
			try {
				itemsRequest.setUnitId(unitId);
				return new ResponseEntity<Object>(ItemsService.itemsList1(itemsRequest), HttpStatus.OK);
			} catch (Exception ex) {
			logger.error("getItemsList1 failed", ex);
			return new ResponseEntity<Object>(new ErrorResponse("HIS-0103", "Error while fetching the items list", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		
		@RequestMapping(value = "{unitId}/{isOTC}/{isNarcotic}/{search}/{storeId}", method = RequestMethod.GET)
		@ApiOperation(value = "Get list of items", response = ItemsSearchResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> search(@PathVariable("unitId") Integer unitId,@PathVariable("isOTC") boolean isOtc, @PathVariable("isNarcotic") boolean isNarcotic, @PathVariable("search") String search,@PathVariable("storeId") Integer storeId)
				throws Exception {
			try {
				return new ResponseEntity<Object>(ItemsService.itemsSearchForPendingIssueItemSearch(unitId,isOtc,isNarcotic, search,storeId), HttpStatus.OK);
			} catch (Exception ex) {
			logger.error("search failed", ex);
			return new ResponseEntity<Object>(new ErrorResponse("HIS-0101", "Error while fetching the items list", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		@RequestMapping(value = "/details/walkin/getKitItemsOnWalkInSales", method = RequestMethod.POST)
		@ApiOperation(value = "Get item details by itemId", response = ItemsDetailsResponse.class )
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> getKitItemsOnWalkInSales(@RequestBody ItemsRequest itemsRequest) throws Exception {
			try {
				return new ResponseEntity<Object>(ItemsService.getKitItemsOnWalkInSales(itemsRequest), HttpStatus.OK);
			} catch (CustomException ex) {
			logger.error("getKitItemsOnWalkInSales failed", ex);
			return new ResponseEntity<Object>(new ErrorResponse("HIS-0102", "Selected item is out of stock", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
			} catch (Exception ex) {
			logger.error("getKitItemsOnWalkInSales failed", ex);
			return new ResponseEntity<Object>(new ErrorResponse("HIS-0102", "Error while fetching the item detail", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		@RequestMapping(value = "/details/org/{orgId}/unit/{unitId}/genericColorMapper", method = RequestMethod.GET)
		@ApiOperation(value = "Get item details by itemId", response = ItemsDetailsResponse.class )
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> detail(@PathVariable("unitId") int unitId, @PathVariable("orgId") int orgId) throws Exception {
			try {
				return new ResponseEntity<Object>(ItemsService.getGenericColorMapping(unitId,orgId), HttpStatus.OK);
			}  catch (Exception ex) {
			logger.error("detail failed", ex);
			return new ResponseEntity<Object>(new ErrorResponse("HIS-0102", "Error while fetching the item detail", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		@RequestMapping(value = "{unitId}/{isOTC}/{search}/{storeId}/asset/{assetTypeId}", method = RequestMethod.GET)
		@ApiOperation(value = "Get list of items", response = ItemsSearchResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> searchStockAdjustment(@PathVariable("unitId") Integer unitId,@PathVariable("isOTC") boolean isOtc, @PathVariable("search") String search,@PathVariable("storeId") Integer storeId,@PathVariable("assetTypeId") Integer assetTypeId)
				throws Exception {
			try {
				return new ResponseEntity<Object>(ItemsService.itemsSearchStockAdjustment(unitId,isOtc, search,storeId,assetTypeId), HttpStatus.OK);
			} catch (Exception ex) {
			logger.error("searchStockAdjustment failed", ex);
			return new ResponseEntity<Object>(new ErrorResponse("HIS-0101", "Error while fetching the items list", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		@RequestMapping(value = "/details/org/{orgId}/unit/{unitId}/{storeId}/{id}/{entitlementId}/walkInPrescription", method = RequestMethod.GET)
		@ApiOperation(value = "Get item details by itemId", response = ItemsDetailsResponse.class )
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> details(@PathVariable("id") int id, @PathVariable("entitlementId") int entitlementId,@PathVariable("unitId") int unitId, @PathVariable("orgId") int orgId,@PathVariable("storeId") int storeId) throws Exception {
			try {
				return new ResponseEntity<Object>(ItemsService.itemsDetailWalkInPrescription(id, entitlementId,unitId,orgId,storeId), HttpStatus.OK);
			}  catch (Exception ex) {
			logger.error("details failed", ex);
			return new ResponseEntity<Object>(new ErrorResponse("HIS-0102", "Error while fetching the item detail", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

	@RequestMapping(value = "/details/getUOMForCoversheet", method = RequestMethod.POST)
	@ApiOperation(value = "Get item details by itemId", response = ItemsDetailsResponse.class )
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getUOMForCoversheet(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemsService.getUOMForCoversheet(itemsRequest), HttpStatus.OK);
		} catch (CustomException ex) {
		logger.error("getUOMForCoversheet failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0102", "Selected item is out of stock", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception ex) {
		logger.error("getUOMForCoversheet failed", ex);
		return new ResponseEntity<Object>(new ErrorResponse("HIS-0102", "Error while fetching the item detail", ex),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    @RequestMapping(value = "/billableConsumption/updateBatchQuantity", method = RequestMethod.POST)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    public Response updateBatchQuantityForBillable(@RequestBody List<BatchListResponse> batchList) throws Exception {
        try {
             Response res = ItemsService.updateBatchQuantityForBillable(batchList);
             return  res;
        } catch (Exception ex) {
        logger.error("updateBatchQuantityForBillable failed", ex);
            return new Response (ERROR, ERROR_CODE, ERROR, null, null);

        }
    }
}
