package com.param.inventory.store.controller;

import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.inventory.store.dto.storeStock.StoreStockRequest;
import com.param.inventory.store.dto.storeStock.StoreStockResponse;
import com.param.inventory.store.dto.storeStockRegister.StoreStockRegisterRequest;
import com.param.inventory.store.dto.storeStockRegister.StoreStockRegisterResponse;
import com.param.inventory.store.service.storeStockRegister.IStoreStockRegisterService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/inventory/stores/stock/register")
public class StoreStockRegisterController {
    private static final Logger logger = Logger.getLogger(StoreStockRegisterController.class);

    @Autowired
    IStoreStockRegisterService registerService;

    @RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
    @ApiOperation(value = "Get store stock register", response = StoreStockRegisterResponse.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    public @ResponseBody
    ResponseEntity<Object> getStoreIndentList(@RequestBody StoreStockRegisterRequest request)
            throws Exception {
        try {
            return new ResponseEntity<Object>(registerService.getStoreStockRegister(request), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error while fetching the store stock register", ex);
            throw new ServiceException(ex.getMessage());
        }
    }
}
