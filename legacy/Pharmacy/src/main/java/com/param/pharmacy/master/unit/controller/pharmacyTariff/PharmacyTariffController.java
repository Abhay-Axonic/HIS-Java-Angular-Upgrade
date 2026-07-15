package com.param.pharmacy.master.unit.controller.pharmacyTariff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.pharmacy.master.unit.dto.pharmacyTariff.PharmacyTariffDto;
import com.param.pharmacy.master.unit.service.pharmacyTariff.IPharmacyTariffService;

@RestController
@RequestMapping("/api/units/pharmacy/tariff")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PharmacyTariffController implements ICommonConstants
{

	@Autowired
	IPharmacyTariffService iPharmacyTariffService;

	private static final Logger logger = Logger.getLogger(PharmacyTariffController.class);

	@RequestMapping(value="/save" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response savePharmacyTariff(@RequestBody PharmacyTariffDto pharmacyTariffDto){
		try{
			return iPharmacyTariffService.savePharmacyTariff(pharmacyTariffDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value="/getAll/{orgId}/{unitId}" , method = RequestMethod.GET)
	public @ResponseBody Response getAllPharmacyTariff(@PathVariable("orgId") Integer orgId,@PathVariable("unitId") Integer unitId){
		try{
			return iPharmacyTariffService.getAllPharmacyTariff(orgId,unitId);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value="/update/status/{id}/{status}" , method = RequestMethod.GET)
	public @ResponseBody Response updateStatus(@PathVariable("id") Integer id,@PathVariable("status") Character status){
		try{
			return iPharmacyTariffService.updateStatus(id,status);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value="/update" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updatePharmacyTariff(@RequestBody PharmacyTariffDto pharmacyTariffDto){
		try{
			return iPharmacyTariffService.updatePharmacyTariff(pharmacyTariffDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
}
