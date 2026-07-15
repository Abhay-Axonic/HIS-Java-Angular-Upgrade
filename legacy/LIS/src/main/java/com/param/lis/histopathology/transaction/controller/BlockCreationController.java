package com.param.lis.histopathology.transaction.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.dto.SampleMasterDto;
import com.param.lis.histopathology.transaction.dto.TGrossMasterDto;
import com.param.lis.histopathology.transaction.dto.TSubSpecimanMasterDto;
import com.param.lis.histopathology.transaction.service.IBlockCreationService;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Block Creation Controller", tags = "TBlockMaster")
@SuppressWarnings({"rawtypes", "unchecked"})
@RequestMapping(value = "/LIS/histopathology")
public class BlockCreationController implements ICommonConstants, IError {

  final static Logger logger = Logger.getLogger(BlockCreationController.class);

  @Autowired
  private IBlockCreationService iBlockCreationService;

  @RequestMapping(value = "/block/creation", method = RequestMethod.POST)
  public @ResponseBody Response getBlockCreationList(@RequestBody SearchDto searchDto) {
    Response<TSubSpecimanMasterDto, Integer> response = null;
    try {
      response = iBlockCreationService.getBlockCreationList(searchDto);
      return response;
    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }

  @RequestMapping(value = "/block/creation/count", method = RequestMethod.POST)
  public @ResponseBody Response getBlockCreationListCount(
      @RequestBody SearchDto searchDto) {
    Response<TSubSpecimanMasterDto, Integer> response = null;
    try {
      response = iBlockCreationService.getBlockCreationListCount(searchDto);
      return response;
    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }

  @RequestMapping(value = "/gross", method = RequestMethod.POST)
  public @ResponseBody Response getCreatedGross(
      @RequestBody TSubSpecimanMasterDto tSubSpecimanMasterDto) {
    Response<SampleMasterDto, Integer> response = null;
    try {
      response = iBlockCreationService.getCreatedGross(tSubSpecimanMasterDto);
      return response;
    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }

  @RequestMapping(value = "/blocks", method = RequestMethod.POST)
  public @ResponseBody Response creteBlocks(
      @RequestBody List<TGrossMasterDto> listTGrossMasterDto) {
    Response<TGrossMasterDto, Integer> response = null;
    try {
      response = iBlockCreationService.creteBlocks(listTGrossMasterDto);
      return response;
    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }
 
@RequestMapping(value ="/subspeciman/{subSpecimanId}",method = RequestMethod.GET)
public @ResponseBody Response getSubSpeciman(@PathVariable int subSpecimanId)
{
	try{
	Response response = iBlockCreationService.getSubSpeciman(subSpecimanId);
	return response;
	}catch(Exception e){
		logger.error("Exception", e);
	      throw e;
	}
}
  
}
