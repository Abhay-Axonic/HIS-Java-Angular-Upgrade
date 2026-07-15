package com.param.billing.unit.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.MultiSelectDto;
import com.param.global.dto.ServiceMasterDto;
import com.param.global.dto.ServiceProfitMarginDto;
import com.param.global.dto.UnitServiceMapperDto;
import com.param.global.dto.UnitServiceTariffMasterDto;
import com.param.global.service.IUnitServiceMapperService;
import com.param.global.service.IUnitServiceTariffMasterService;

@RestController
@RequestMapping(value="api/unit")
@SuppressWarnings({"rawtypes","unchecked"})
public class UnitServiceTariffMasterController implements ICommonConstants{

	@Autowired
	IUnitServiceTariffMasterService iUnitServiceTariffMasterService; 
	
	@Autowired
	private IUnitServiceMapperService iUnitServiceMapperService;
	
	@RequestMapping(value="/getMasterServiceListByMultipleParameters", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getMasterServiceListByMultipleParameters(@RequestBody UnitServiceTariffMasterDto unitServiceTariffMasterDto){
		try{
			return iUnitServiceTariffMasterService.getMasterServiceListByMultipleParameters(unitServiceTariffMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getUnitServiceTariffMasterList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getUnitServiceTariffMasterList(@RequestBody UnitServiceTariffMasterDto unitServiceTariffMasterDto){
		try{
			return iUnitServiceTariffMasterService.getUnitServiceTariffMasterList(unitServiceTariffMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/saveUnitServiceTariffMaster", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveUnitServiceTariffMaster(@RequestBody UnitServiceTariffMasterDto unitServiceTariffMasterDto){
		try{
			return iUnitServiceTariffMasterService.saveUnitServiceTariffMaster(unitServiceTariffMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/service/tariff/uniqueCheck", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response uniqueCheck(@RequestBody UnitServiceTariffMasterDto unitServiceTariffMasterDto){
		try{
			return iUnitServiceTariffMasterService.uniqueCheck(unitServiceTariffMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getBasePriceOfServicesFromTariffMasterByServiceList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBasePriceOfServicesFromTariffMasterByServiceList(@RequestBody UnitServiceTariffMasterDto unitServiceTariffMasterDto){
		try{
			return iUnitServiceTariffMasterService.getBasePriceOfServicesFromTariffMasterByServiceList(unitServiceTariffMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getBasePriceOfServicesFromTariffMasterByServiceId", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody UnitServiceTariffMasterDto getBasePriceOfServicesFromTariffMasterByServiceId(@RequestBody UnitServiceTariffMasterDto unitServiceTariffMasterDto){
		try{
			return iUnitServiceTariffMasterService.getBasePriceByServiceTariffMaster(unitServiceTariffMasterDto);
		}catch(Exception e){
			
		}
		return null;
	}
	
	@RequestMapping(value="/getProfitMarginList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getProfitMarginList(@RequestBody UnitServiceTariffMasterDto unitServiceTariffMasterDto){
		try{
			return iUnitServiceTariffMasterService.getProfitMarginList(unitServiceTariffMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/saveServiceProfitMargin", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response saveServiceProfitMargin(@RequestBody List<ServiceProfitMarginDto> serviceProfitMarginDto) {
		try
		{
			return iUnitServiceTariffMasterService.saveServiceProfitMargin(serviceProfitMarginDto);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getServiceListBySubSpecialityId", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getServiceListBySubSpecialityId(@RequestBody UnitServiceTariffMasterDto unitServiceTariffMasterDto) {
		try
		{
			return iUnitServiceTariffMasterService.getServiceListBySubSpecialityId(unitServiceTariffMasterDto);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getServiceListBySubSpecialityIdNotByTariff", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getServiceListBySubSpecialityIdNotByTariff(@RequestBody UnitServiceTariffMasterDto unitServiceTariffMasterDto) {
		try
		{
			return iUnitServiceTariffMasterService.getServiceListBySubSpecialityIdNotByTariff(unitServiceTariffMasterDto);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getServiceListBySpecialityIdSubSpecialityList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getServiceListBySpecialityIdSubSpecialityList(@RequestBody UnitServiceTariffMasterDto unitServiceTariffMasterDto) {
		try
		{
			return iUnitServiceTariffMasterService.getServiceListBySpecialityIdSubSpecialityList(unitServiceTariffMasterDto);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/filterPatientType", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response filterPatientType(@RequestBody UnitServiceTariffMasterDto unitServiceTariffMasterDto) {
		try
		{
			
			List<MultiSelectDto> newPatientTypeList = new LinkedList<>();
			List<MultiSelectDto> newSelectedPatientTypeList = new LinkedList<>();
			
			if(unitServiceTariffMasterDto.getPaymentEntitlementId()==SELF)
			{
				for(MultiSelectDto patientType : unitServiceTariffMasterDto.getPatientTypeList())
				{
					if(patientType.getId()!=unitServiceTariffMasterDto.getCompanyPatientCategory())
					{
						if(patientType.getId()!=unitServiceTariffMasterDto.getInsurancePatientCategory())
						{
							newPatientTypeList.add(patientType);
						}
					}
				}
				for(MultiSelectDto selectedpatientType : unitServiceTariffMasterDto.getSelectedPatientTypeList())
				{
					if(selectedpatientType.getId()!=unitServiceTariffMasterDto.getCompanyPatientCategory())
					{
						if(selectedpatientType.getId()!=unitServiceTariffMasterDto.getInsurancePatientCategory())
						{
							newSelectedPatientTypeList.add(selectedpatientType);
						}
					}
				}
			}
			
			if(unitServiceTariffMasterDto.getPaymentEntitlementId()==INSURANCE)
			{
				for(MultiSelectDto patientType : unitServiceTariffMasterDto.getPatientTypeList())
				{
					if(patientType.getId()==unitServiceTariffMasterDto.getInsurancePatientCategory())
					{
						newPatientTypeList.add(patientType);
					}
				}
				for(MultiSelectDto selectedpatientType : unitServiceTariffMasterDto.getSelectedPatientTypeList())
				{
					if(selectedpatientType.getId()==unitServiceTariffMasterDto.getInsurancePatientCategory())
					{
						newSelectedPatientTypeList.add(selectedpatientType);
					}
				}
			}
			
			if(unitServiceTariffMasterDto.getPaymentEntitlementId()==COMPANY)
			{
				for(MultiSelectDto patientType : unitServiceTariffMasterDto.getPatientTypeList())
				{
					if(patientType.getId()==unitServiceTariffMasterDto.getCompanyPatientCategory())
					{
						newPatientTypeList.add(patientType);
					}
				}
				for(MultiSelectDto selectedpatientType : unitServiceTariffMasterDto.getSelectedPatientTypeList())
				{
					if(selectedpatientType.getId()==unitServiceTariffMasterDto.getCompanyPatientCategory())
					{
						newSelectedPatientTypeList.add(selectedpatientType);
					}
				}
			}
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, newSelectedPatientTypeList, newPatientTypeList);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/tariff/serviceMaster",method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchServicesByNameAndCode(@RequestBody ServiceMasterDto serviceMasterDto) {
		try {
			return iUnitServiceTariffMasterService.searchServicesByNameAndCode(serviceMasterDto);
		}catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/saveUnitServiceTariffMasterFromToUnit", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveUnitServiceTariffMasterFromToUnit(@RequestBody UnitServiceTariffMasterDto unitServiceTariffMasterDto){
		try{
			
			List<Integer> serviceIds = new ArrayList<Integer>();
			Integer countser=0;
			for (UnitServiceTariffMasterDto dto : unitServiceTariffMasterDto.getUnitServiceTariffMasterDtosList()) {
				
				if(countser == 0){
					serviceIds.add(dto.getServiceId());
					countser = 1;
				}else{
					
					Boolean checkser= true;
					for (Integer serId : serviceIds) {
						if(serId.equals(dto.getServiceId())){
							 checkser= false;
						}
					}
					
					if(checkser){
						serviceIds.add(dto.getServiceId());
					}
				}
				
			}
			
			Response res = iUnitServiceTariffMasterService.checkUnitServiceMasterMapping(serviceIds,unitServiceTariffMasterDto.getUnitServiceTariffMasterDtosList().get(0).getUnitId());
		
			List<UnitServiceTariffMasterDto> unitservicMapperForSaveInToUnitSerMapper = new ArrayList<UnitServiceTariffMasterDto>();
			
			if(res.getListObject() != null && res.getListObject().size()>0){
				
				 List<UnitServiceTariffMasterDto> list = res.getListObject();
				 
				 for (UnitServiceTariffMasterDto unitServiceFromReq : unitServiceTariffMasterDto.getUnitServiceTariffMasterDtosList()) {
					
					 Boolean Check = true;
					 
					 for (UnitServiceTariffMasterDto unitServiceFromDB : list) {
							
						 if(unitServiceFromReq.getServiceId().equals(unitServiceFromDB.getServiceId())){
							 Check = false;
						 }
					 }
					 
					 if(Check == true){
						 unitservicMapperForSaveInToUnitSerMapper.add(unitServiceFromReq);
					 }
					 
				 }
				 
			}else{
				
				unitservicMapperForSaveInToUnitSerMapper.addAll(unitServiceTariffMasterDto.getUnitServiceTariffMasterDtosList());
			}
			
			if(unitservicMapperForSaveInToUnitSerMapper != null && unitservicMapperForSaveInToUnitSerMapper.size()>0){
				
				
				
				List<UnitServiceTariffMasterDto> fillterUnitservicMapperForSaveInToUnitSerMapper = new ArrayList<UnitServiceTariffMasterDto>();
				Integer count =0 ;
				
				for (UnitServiceTariffMasterDto serdto : unitservicMapperForSaveInToUnitSerMapper) {
					
					if(count == 0 ){
						
						fillterUnitservicMapperForSaveInToUnitSerMapper.add(serdto);
						count = 1;
					}else{
						
						Boolean checkExits = true;
						
						for (UnitServiceTariffMasterDto fildto : fillterUnitservicMapperForSaveInToUnitSerMapper) {
							
							if(fildto.getServiceId().equals(serdto.getServiceId())){
								checkExits = false;
							}
						}
						
						if(checkExits){
							fillterUnitservicMapperForSaveInToUnitSerMapper.add(serdto);
						}
					}
				}
				
				Date date = new Date();
				UnitServiceMapperDto unitServiceMapperDto = new UnitServiceMapperDto();
				unitServiceMapperDto.setUnitId(unitServiceTariffMasterDto.getUnitServiceTariffMasterDtosList().get(0).getUnitId());
				unitServiceMapperDto.setStatus('A');
				unitServiceMapperDto.setOrgnisationId(unitServiceTariffMasterDto.getUnitServiceTariffMasterDtosList().get(0).getOrganizationId());
				unitServiceMapperDto.setCreatedBy(unitServiceTariffMasterDto.getUnitServiceTariffMasterDtosList().get(0).getCreatedBy());
				unitServiceMapperDto.setUpdatedBy(unitServiceTariffMasterDto.getUnitServiceTariffMasterDtosList().get(0).getCreatedBy());
				unitServiceMapperDto.setCreatedDate(date.getTime());
				unitServiceMapperDto.setUpdatedDate(date.getTime());
				
				List<UnitServiceMapperDto> unitSer = new ArrayList<UnitServiceMapperDto>();
				
				for (UnitServiceTariffMasterDto fromReqdto : fillterUnitservicMapperForSaveInToUnitSerMapper) {
					UnitServiceMapperDto dto = new UnitServiceMapperDto();
					dto.setServiceId(fromReqdto.getServiceId());
					dto.setServiceStandardCode(fromReqdto.getServiceCode());
					dto.setServiceStandardName(fromReqdto.getServiceDesc());
					unitSer.add(dto);
				}
				unitServiceMapperDto.setListUnitServiceMapperDto(unitSer);
				
				iUnitServiceMapperService.saveUnitServiceMapper(unitServiceMapperDto);
			}
			
			return iUnitServiceTariffMasterService.saveUnitServiceTariffMaster(unitServiceTariffMasterDto);
			
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getUnitServiceTariffMasterListForWeb", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getUnitServiceTariffMasterListForWeb(@RequestBody UnitServiceTariffMasterDto unitServiceTariffMasterDto){
		try{
			return iUnitServiceTariffMasterService.getUnitServiceTariffMasterListForWeb(unitServiceTariffMasterDto);
		}catch(Exception e){

		}
		return new Response(ERROR, null, null, null,null);
	}


}
