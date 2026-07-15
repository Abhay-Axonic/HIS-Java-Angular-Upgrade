package com.param.inventory.stockAdjustments.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.ServiceMasterDto;
import com.param.inventory.stockAdjustments.dto.StockAdjustmentDropdownResponse;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockRequest;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockResponse;
import com.param.inventory.stockAdjustments.service.uploadStock.IUploadStockService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@EnableWebMvc
@RestController
@RequestMapping(value = "/api/inventory/stock/adjusments/upload/stock")
public class UploadStockController {

	private static final Logger logger = Logger.getLogger(UploadStockController.class);

	@Autowired
	IUploadStockService UploadStockService;

	@RequestMapping(value = "/dropdown/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of dropdown", response = StockAdjustmentDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) {
		try {
			return new ResponseEntity<Object>(UploadStockService.getDropdownList(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getDropdownList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get Upload Stock list", response = UploadStockResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getUploadStockList(@RequestBody UploadStockRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(UploadStockService.getUploadStockList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getUploadStockList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Upload Stock details", response = UploadStockResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getUploadStockDetails(@PathVariable Integer id , @PathVariable Integer unitId) throws Exception {
		try {
			return new ResponseEntity<Object>(UploadStockService.getUploadStockDetails(id , unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getUploadStockDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "save upload stock", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> saveUploadStock(@RequestBody UploadStockRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(UploadStockService.saveUploadStock(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("saveUploadStock failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/get/request", method = RequestMethod.POST)
	@ApiOperation(value = "Get upload stock item list", response = UploadStockResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getUploadStockItemList(@RequestBody UploadStockRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(UploadStockService.getUploadStockItemList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getUploadStockItemList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/add/item", method = RequestMethod.POST)
	@ApiOperation(value = "Get upload stock item list", response = UploadStockResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getuploadStockAddItemList(@RequestBody UploadStockRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(UploadStockService.getuploadStockAddItemList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getuploadStockAddItemList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel upload stock", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelUploadStock(@PathVariable Integer id, @RequestBody UploadStockRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(UploadStockService.cancelUploadStock(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelUploadStock failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve upload stock", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveUploadStock(@RequestBody UploadStockRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(UploadStockService.approveUploadStock(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approveUploadStock failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update upload stock", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateUploadStock(@PathVariable Integer id, @RequestBody UploadStockRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(UploadStockService.updateUploadStock(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateUploadStock failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}	
	
	
	@RequestMapping(value = "/get/batch", method = RequestMethod.POST)
	@ApiOperation(value = "Get upload stock item list", response = UploadStockResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getuploadStockAddItemListOldBatch(@RequestBody UploadStockRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(UploadStockService.getuploadStockAddItemListOldBatch(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getuploadStockAddItemListOldBatch failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	

	@RequestMapping(value = "/updateStockAdjustment", method = RequestMethod.POST)
	public Response saveData(HttpServletRequest request,HttpServletResponse res,HttpSession session,@RequestHeader Integer organizationId,
			@RequestHeader Integer unitId,@RequestHeader Integer createdBy,@RequestHeader Integer updatedBy,
			@RequestHeader Integer storeId, @RequestHeader Integer statusId , @RequestHeader Integer approvalStatusId,  @RequestHeader Integer stopTransactionId ,
			@RequestParam("file") MultipartFile file){
		try {
			
			UploadStockRequest dto=new UploadStockRequest();
			dto.setUnitId(unitId);
			dto.setOrganizationId(organizationId);
			dto.setUserId(createdBy);
			dto.setFile(file);
			dto.setApprovalStatusId(approvalStatusId);
			dto.setStopTransactionId(stopTransactionId);
			dto.setStoreId(storeId);
			dto.setStatusId(statusId);
			return UploadStockService.readCSVFile(dto);

			} catch (Exception e) {
				logger.error("saveData failed", e);
				throw new ServiceException(e.getMessage());
			}
		
	}



}
