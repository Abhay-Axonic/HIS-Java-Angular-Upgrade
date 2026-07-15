package com.param.pharmacy.master.global.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.apache.log4j.Logger;

import com.param.bloodBank.dto.DonorRegistrationDto;
import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.generic.Generic;
import com.param.global.dto.global.FormulationType;
import com.param.global.dto.items.ItemsDetailsListResponse;
import com.param.global.dto.items.ItemsRequest;
import com.param.global.dto.items.MultiItemsRequest;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dto.item.ItemDetailsResponse;
import com.param.pharmacy.master.global.dto.item.ItemDropdownResponse;
import com.param.pharmacy.master.global.dto.item.ItemListResponse;
import com.param.pharmacy.master.global.dto.item.ItemRequest;
import com.param.pharmacy.master.global.dto.item.ItemSearchByNameResponse;
import com.param.pharmacy.master.global.service.item.IItemService;
import com.param.pharmacy.master.global.service.unitOfMeasurement.IUnitOfMeasurementService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@SuppressWarnings({ "unchecked", "rawtypes" })
@RestController
@RequestMapping(value = "/api/globals/items")
public class ItemController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(ItemController.class);

	@Autowired
	IItemService ItemService;

	@Autowired
	IUnitOfMeasurementService unitOfMeasurementService;
	
	@RequestMapping(value = "/dropdown/{orgId}/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = ItemDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdown(@PathVariable Integer orgId,@PathVariable Integer unitId) throws Exception {
		return new ResponseEntity<Object>(ItemService.getDropdowns(orgId,unitId), HttpStatus.OK);
	}
	
	

	@RequestMapping(value = "/getIsVaccineEnabledItems", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Vaccine Enabled Items", response = ItemListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public List<ItemListResponse> getIsVacineEnabledItems() 
	{
		 ItemService.getIsVaccineEnabledItems();
		return  ItemService.getIsVaccineEnabledItems();
	}
	/* Need to remove this api */
	@RequestMapping(value = "/formulation/type/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = FormulationType.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getFormulationTypes(@PathVariable Integer orgId) throws Exception {
		return new ResponseEntity<Object>(ItemService.getFormulationTypeList(orgId), HttpStatus.OK);
	}

	@RequestMapping(value = "/generic/{orgId}/{productCategoryId}/{searchGenericName}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = Generic.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getGenerics(@PathVariable Integer productCategoryId,@PathVariable Integer orgId, @PathVariable String searchGenericName) throws Exception {
		return new ResponseEntity<Object>(ItemService.getGenericList(productCategoryId,orgId,searchGenericName), HttpStatus.OK);
	}

	@RequestMapping(value = "/{orgId}/{offset}/{noOfRecordsPerPage}/{searchKeyward}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Items", response = ItemListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<ItemListResponse>> GetAll(@PathVariable Integer orgId,@PathVariable Integer offset,@PathVariable Integer noOfRecordsPerPage,@PathVariable String searchKeyward) throws Throwable {
		ResponseEntity<List<ItemListResponse>> response = new ResponseEntity<List<ItemListResponse>>(
				ItemService.getAll(orgId,offset,noOfRecordsPerPage, searchKeyward), HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Item By Id", response = ItemDetailsResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public synchronized ResponseEntity<ItemDetailsResponse> GetById(@PathVariable("id") Integer id,@PathVariable Integer orgId) {

		ResponseEntity<ItemDetailsResponse> response = new ResponseEntity<ItemDetailsResponse>(ItemService.getById(id,orgId),
				HttpStatus.OK);
		return response;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Item", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Item Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Item"),
			@ApiResponse(code = 403, message = "Create Item is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Create(@RequestBody ItemRequest itemRequest) {
		return new ResponseEntity<CommonResponse>(ItemService.create(itemRequest), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Item", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Product Category Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Product Category"),
			@ApiResponse(code = 403, message = "Update Product Category is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<Object> Update(@PathVariable("id") Integer id,@PathVariable Integer orgId, @RequestBody ItemRequest itemRequest) {
		itemRequest.setId(id);
		try {
			itemRequest.setOrganizationId(orgId);
			Response res=ItemService.updatePreviousItemInActive(itemRequest);
			return new ResponseEntity<Object>(ItemService.update(itemRequest), HttpStatus.CREATED);
		} catch (CustomeException Erespone) {
			logger.error(Erespone.getMessage(), Erespone);
			throw new ServiceException(Erespone.getMessage());
		}

	}

	@RequestMapping(value = "/list/org/{orgId}", method = RequestMethod.POST)
	@ApiOperation(value = "Get items list", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getItemsList(@RequestBody ItemsRequest itemsRequest,@PathVariable("orgId") int orgId) throws Exception {
		try {
				itemsRequest.setOrganizationId(orgId);
			return new ResponseEntity<Object>(ItemService.itemsList(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/item/search/{name}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of Purchase Order", notes = "Get list of  Purchase Order", response = ItemSearchByNameResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> itemSearch(@PathVariable("name") String name)
			throws Exception {
		return new ResponseEntity<Object>(ItemService.itemSearch(name), HttpStatus.OK);

	}
	
	@RequestMapping(value = "/multiplestorelist", method = RequestMethod.POST)
	@ApiOperation(value = "Get items list", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> multipleStoreList(@RequestBody MultiItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemService.multipleStoreList(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}


	@RequestMapping(value = "/typeahead/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get items list", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> itemTypeaheadSearch(@RequestBody MultiItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemService.itemTypeaheadSearch(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}



	@RequestMapping(value = "/uploadItemMaster", method = RequestMethod.POST)
	public Response uploadItemMaster(HttpServletRequest request,HttpServletResponse res,HttpSession session,@RequestHeader Integer organizationId,
			@RequestHeader Integer unitId,@RequestHeader Integer createdBy,@RequestHeader Integer updatedBy,
			@RequestHeader Integer storeId,
			@RequestParam("file") MultipartFile file){
		try {
			
			ItemRequest dto=new ItemRequest();
			dto.setUnitId(unitId);
			dto.setOrganizationId(organizationId);
			dto.setUserId(createdBy);
			dto.setFile(file);
			dto.setStoreId(storeId);
			return ItemService.uploadItemMaster(dto);

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}

	}
	
	@RequestMapping(value = "/dropdown/{orgId}/{unitId}/{searchClassificationName}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = ItemDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> searchClassificationName(@PathVariable Integer orgId,@PathVariable Integer unitId,@PathVariable String searchClassificationName) throws Exception {
		return new ResponseEntity<Object>(ItemService.searchClassificationName(orgId,unitId,searchClassificationName), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/dropdownManufacture/{orgId}/{unitId}/{searchManufactureName}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = ItemDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> searchManufactureName(@PathVariable Integer orgId,@PathVariable Integer unitId,@PathVariable String searchManufactureName) throws Exception {
		return new ResponseEntity<Object>(ItemService.searchManufactureName(orgId,unitId,searchManufactureName), HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value="/checkForUniqueItemName", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody CommonResponse checkForUniqueItemName(@RequestBody ItemRequest dto) {
		try {
			return ItemService.checkForUniqueItemName(dto);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
}
