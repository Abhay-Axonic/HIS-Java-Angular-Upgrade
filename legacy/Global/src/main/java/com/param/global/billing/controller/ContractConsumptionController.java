package com.param.global.billing.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.billing.service.IContractConsumptionService;
import com.param.global.billing.service.IContractConsumtionForCostEstimationService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.ServiceSearchResDto;
import com.param.global.pharmacy.service.IWalkInSalesService;

@RestController
@RequestMapping("/global/consumption")
@SuppressWarnings({"unchecked","rawtypes"})
public class ContractConsumptionController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(ContractConsumptionController.class);

	@Autowired
	IContractConsumptionService iContractConsumptionService;
	
	@Autowired
	IWalkInSalesService iWalkInSalesService;
	
	@Autowired
	IContractConsumtionForCostEstimationService iContractConsumptionServiceCostEstimation;
	
	@RequestMapping(value="/service" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response consumeServiceFromContract(@RequestBody ContractConsumptionDto consumptionDto){
		try{
			return iContractConsumptionService.consumeServiceFromContract(consumptionDto);
		}catch(Exception e){
			logger.error("consumeServiceFromContract failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/service/cancel" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addCapAmount(@RequestBody ContractConsumptionDto consumptionDto){
		try{
			Response res = iContractConsumptionService.addCapAmount(consumptionDto);
			//if service is a pharmacy service
			if(consumptionDto.getSaleId()!=null && consumptionDto.getSaleId()>0)
			{
				// 9 static screen id
				iWalkInSalesService.revertAllocatedQty(consumptionDto.getSaleId().longValue(), 9,consumptionDto.getUnitId(),null,null,null);
				iWalkInSalesService.inactiveSaleBySaleId(consumptionDto.getSaleId());
			}
			return res;
		}catch(Exception e){
			logger.error("addCapAmount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/getActivePaymentEntitlement" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActivePaymentEntitlement(@RequestBody ContractConsumptionDto consumptionDto){
		try{
			return iContractConsumptionService.getActivePaymentEntitlement(consumptionDto);
		}catch(Exception e){
			logger.error("getActivePaymentEntitlement failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value="/checkConsultation" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response isConsultationAmountPaidBySelf(@RequestBody ContractConsumptionDto dto){
		try{
			return iContractConsumptionService.isConsultationAmountPaidBySelf(dto);
		}catch(Exception e){
			logger.error("isConsultationAmountPaidBySelf failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/package" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response consumePackageFromContract(@RequestBody ServiceSearchResDto searchResDto){
		try{
			return iContractConsumptionService.consumePackageFromContract(searchResDto);
		}catch(Exception e){
			logger.error("consumePackageFromContract failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/costEstimation/service" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response consumeServiceFromContractFostCostEstimation(@RequestBody ContractConsumptionDto consumptionDto){
		try{
			return iContractConsumptionServiceCostEstimation.consumeServiceFromContract(consumptionDto);
		}catch(Exception e){
			logger.error("consumeServiceFromContractFostCostEstimation failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value="/costEstimation/service/billingbed" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response consumeServiceFromContractFostCostEstimationBillingbed(@RequestBody List<ContractConsumptionDto> consumptionDto){
		try{
			List<ContractConsumptionDto> serviceDetailslist = new ArrayList<>();
			for (ContractConsumptionDto contractConsumptionDto : consumptionDto) {
				Response res = iContractConsumptionServiceCostEstimation.consumeServiceFromContract(contractConsumptionDto);
				if(res.getObject() != null){
					
					ContractConsumptionDto dto = (ContractConsumptionDto)res.getObject();
					serviceDetailslist.add(dto);
				}
			}
			return new Response(SUCCESS,SUCCESS_CODE,null,serviceDetailslist,serviceDetailslist.get(0));
		}catch(Exception e){
			logger.error("consumeServiceFromContractFostCostEstimationBillingbed failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value="/getSerivceRateGlobal" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSerivceRateGlobal(@RequestBody ContractConsumptionDto consumptionDto){
		try{
			
			return  iContractConsumptionService.getSerivceRateGlobal(consumptionDto, false, false);

		}catch(Exception e){
			logger.error("getSerivceRateGlobal failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value="/getSerivceRateGlobalAfterTariffChange" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSerivceRateGlobalAfterTariffChange(@RequestBody ContractConsumptionDto consumptionDto){
		try{
			
			return  iContractConsumptionService.getSerivceRateGlobalAfterTariffChange(consumptionDto, false, false);

		}catch(Exception e){
			logger.error("getSerivceRateGlobalAfterTariffChange failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/packageForCostE" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response packageForCostE(@RequestBody List<ServiceSearchResDto> searchResDto){
		try{
			ContractConsumptionDto dto = new ContractConsumptionDto();
			List<ContractConsumptionDto> list = new ArrayList<ContractConsumptionDto>();
			for (ServiceSearchResDto serviceSearchResDto : searchResDto) {
				Response res = iContractConsumptionService.consumePackageFromContract(serviceSearchResDto);
				
				if(res.getObject() != null ){
					ContractConsumptionDto finalDto =  (ContractConsumptionDto)res.getObject();	
					list.add(finalDto);
					dto = (ContractConsumptionDto)res.getObject();	
				}
			}
			
			return new Response(SUCCESS,SUCCESS_CODE,null,list,dto);
		}catch(Exception e){
			logger.error("packageForCostE failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value="/getSerivceRateGlobalList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSerivceRateGlobalList(@RequestBody List<ContractConsumptionDto> consumptionDto){
		try{
			
			List<ContractConsumptionDto> rateList = new ArrayList<>();
			
			for (ContractConsumptionDto dto : consumptionDto) {
				
				Response res =  iContractConsumptionService.getSerivceRateGlobal(dto, false, false);
				
				if(res.getObject() != null){
					
					ContractConsumptionDto obj =  (ContractConsumptionDto) res.getObject();
					obj.setKeyIndex(dto.getKeyIndex());
					rateList.add(obj);
				}
			}
			 
			return new Response(SUCCESS,SUCCESS_CODE,null,rateList,null);

		}catch(Exception e){
			logger.error("getSerivceRateGlobalList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


}
