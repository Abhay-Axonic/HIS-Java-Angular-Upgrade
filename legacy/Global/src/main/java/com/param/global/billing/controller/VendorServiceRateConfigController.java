package com.param.global.billing.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.global.billing.service.IVendorServiceRateConfigService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.VendorServiceRateConfigDto;

@Controller
@RequestMapping("/api/vendorServiceRateConfig")
@SuppressWarnings({"rawtypes","unchecked"})
public class VendorServiceRateConfigController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(VendorServiceRateConfigController.class);

	@Autowired
	IVendorServiceRateConfigService iVendorServiceRateConfigService;

	@RequestMapping(value="/list", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getVendorServiceRateConfigList(@RequestBody VendorServiceRateConfigDto vendorServiceRateConfigDto){
		try{
			return iVendorServiceRateConfigService.getVendorServiceRateConfigList(vendorServiceRateConfigDto);
		}catch(Exception e){
			logger.error("getVendorServiceRateConfigList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value="/generateList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response generateVendorServiceRateConfigList(@RequestBody VendorServiceRateConfigDto vendorServiceRateConfigDto){
		try{
			return iVendorServiceRateConfigService.generateVendorServiceRateConfigList(vendorServiceRateConfigDto);
		}catch(Exception e){
			logger.error("generateVendorServiceRateConfigList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value="/uniqueCheck", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response uniqueCheck(@RequestBody VendorServiceRateConfigDto vendorServiceRateConfigDto){
		try{
			return iVendorServiceRateConfigService.uniqueCheck(vendorServiceRateConfigDto);
		}catch(Exception e){
			logger.error("uniqueCheck failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@RequestMapping(value="/save", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveVendorServiceRateConfig(@RequestBody VendorServiceRateConfigDto vendorServiceRateConfigDto){
		try{
			return iVendorServiceRateConfigService.saveVendorServiceRateConfig(vendorServiceRateConfigDto);
		}catch(Exception e){
			logger.error("saveVendorServiceRateConfig failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value="/ehcPackage/list", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getEHCPackageList(@RequestBody VendorServiceRateConfigDto vendorServiceRateConfigDto){
		try{
			return iVendorServiceRateConfigService.getEHCPackageList(vendorServiceRateConfigDto);
		}catch(Exception e){
			logger.error("getEHCPackageList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value="/ehcPackage/services", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getEHCPackageServices(@RequestBody VendorServiceRateConfigDto vendorServiceRateConfigDto){
		try{
			return iVendorServiceRateConfigService.getEHCPackageServices(vendorServiceRateConfigDto);
		}catch(Exception e){
			logger.error("getEHCPackageServices failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
