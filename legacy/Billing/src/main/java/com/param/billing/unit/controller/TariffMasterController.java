package com.param.billing.unit.controller;

import com.param.global.dto.PrefixMasterDto;
import com.param.global.utility.UserComponent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.billing.unit.service.ITariffMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TariffMasterDto;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value="api/unit")
@SuppressWarnings({"rawtypes","unchecked"})
public class TariffMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(TariffMasterController.class);

	@Autowired
	private ITariffMasterService iTariffMasterService;

	@RequestMapping(value="/saveTariffMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveTariffMaster(@RequestBody TariffMasterDto tariffMasterDto){
		try{
			return iTariffMasterService.saveTariffMaster(tariffMasterDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/getTariffMasterList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getTariffMasterList(@RequestBody TariffMasterDto tariffMasterDto){
		try{
			return iTariffMasterService.getTariffMasterList(tariffMasterDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/getPaymentEntitlementListByTariffid", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPaymentEntitlementListByTariffid(@RequestBody TariffMasterDto tariffMasterDto){
		try{
			return iTariffMasterService.getPaymentEntitlementListByTariffid(tariffMasterDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value="/getTariffListById" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getTariffListById(@RequestBody TariffMasterDto tariffMasterDto){
		try{
			return iTariffMasterService.getTariffListById(tariffMasterDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	 
	@RequestMapping(value="/updateTariffMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateTariffMaster(@RequestBody TariffMasterDto tariffMasterDto){
		try{
			
			return iTariffMasterService.updateTariffMaster(tariffMasterDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/updateTariffMasterStatus" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusForTariff(@RequestBody TariffMasterDto tariffMasterDto){
		try{
			return iTariffMasterService.updateStatusForTariff(tariffMasterDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/updateStatusForTariffPaymentMapper" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusForTariffPaymentMapper(@RequestBody TariffMasterDto tariffMasterDto){
		try{
			return iTariffMasterService.updateStatusForTariffPaymentMapper(tariffMasterDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/getActiveTariffMasterList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveTariffMasterList(@RequestBody TariffMasterDto tariffMasterDto){
		try{

			return iTariffMasterService.getActiveTariffMasterList(tariffMasterDto);

		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/getPaymentEntitlementListByTariffIdList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPaymentEntitlementListByTariffIdList(@RequestBody TariffMasterDto tariffMasterDto){
		try{
			return iTariffMasterService.getPaymentEntitlementListByTariffIdList(tariffMasterDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/getVisitTypeListByTariffIdList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getVisitTypeListByTariffIdList(@RequestBody TariffMasterDto tariffMasterDto){
		try{
			return iTariffMasterService.getVisitTypeListByTariffIdList(tariffMasterDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/getPatientTypeListByTariffIdList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientTypeListByTariffIdList(@RequestBody TariffMasterDto tariffMasterDto){
		try{
			return iTariffMasterService.getPatientTypeListByTariffIdList(tariffMasterDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/getBillingBedCategoryByTariffIdList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBillingBedCategoryByTariffIdList(@RequestBody TariffMasterDto tariffMasterDto){
		try{
			return iTariffMasterService.getBillingBedCategoryByTariffIdList(tariffMasterDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/getVisitTypeListByTariffId", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getVisitTypeListByTariffId(@RequestBody TariffMasterDto tariffMasterDto){
		try{
			return iTariffMasterService.getVisitTypeListByTariffId(tariffMasterDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/updateStatusForTariffVisitTypeMapper" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusForTariffVisitTypeMapper(@RequestBody TariffMasterDto tariffMasterDto){
		try{
			return iTariffMasterService.updateStatusForTariffVisitTypeMapper(tariffMasterDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/getPatientTypeListByTariffId", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientTypeListByTariffId(@RequestBody TariffMasterDto tariffMasterDto){
		try{
			return iTariffMasterService.getPatientTypeListByTariffId(tariffMasterDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/updateStatusForTariffPatientTypeMapper" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusForTariffPatientTypeMapper(@RequestBody TariffMasterDto tariffMasterDto){
		try{
			return iTariffMasterService.updateStatusForTariffPatientTypeMapper(tariffMasterDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/getBillingBedCategoryListByTariffId", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBillingBedCategoryListByTariffId(@RequestBody TariffMasterDto tariffMasterDto){
		try{
			return iTariffMasterService.getBillingBedCategoryListByTariffId(tariffMasterDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/updateStatusForTariffBillingBedCategoryMapper" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusForTariffBillingBedCategoryMapper(@RequestBody TariffMasterDto tariffMasterDto){
		try{
			return iTariffMasterService.updateStatusForTariffBillingBedCategoryMapper(tariffMasterDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/tariffMasterCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response tariffMasterCount(@RequestBody TariffMasterDto tariffMasterDto){
		try{
			return iTariffMasterService.tariffMasterCount(tariffMasterDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/getTariffMasterListOpdBilling", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getTariffMasterListOpdBilling(@RequestBody TariffMasterDto tariffMasterDto){
		try{
			return iTariffMasterService.getTariffMasterListOpdBilling(tariffMasterDto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
}
