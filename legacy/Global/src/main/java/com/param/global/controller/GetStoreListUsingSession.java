package com.param.global.controller;

import com.param.common.dto.ErrorResponse;
import com.param.global.common.ICommonConstants;
import com.param.global.common.ServiceException;
import com.param.global.dto.getStoreListBySessionUserId.StoreResponseList;
import com.param.global.service.IGetStoreListUsingsessionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping(value = "/api/global/store")
@SessionAttributes("StoreId")
public class GetStoreListUsingSession implements ICommonConstants
{

	private static final Logger logger = Logger.getLogger(GetStoreListUsingSession.class);

	@Autowired
	IGetStoreListUsingsessionService iGetStoreListUsingsessionService;
	
	@RequestMapping(value = "/getStoreListById/{userId}/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Material Issue Pending details by id", response = StoreResponseList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getStoreListUsingSessionid(@PathVariable Integer userId, @PathVariable Integer unitId) throws Exception {
		try {
			return new ResponseEntity<Object>(iGetStoreListUsingsessionService.getStoreListUsingSession(userId, unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the material issue pending details", ex);
			return new ResponseEntity<Object>(
					new ErrorResponse("HIS-1264", "Error while fetching the material issue pending details", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/sessionstoreid/{storeId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Material Issue Pending details by id", response = StoreResponseList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getStoreId(@PathVariable Integer storeId, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		try {
			response.addCookie(this.iGetStoreListUsingsessionService.setStoreIdToSession(request, storeId));
			return new ResponseEntity<Object>("StoreId saved successfully", HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while setting the store id in session", ex);
			return new ResponseEntity<Object>(
					new ErrorResponse("HIS-1264", "Error while setting the store id in session", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
