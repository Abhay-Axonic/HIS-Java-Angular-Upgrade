package com.param.global.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.global.transaction.model.ServiceTarrifMaster;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.dao.IServiceMasterDao;
import com.param.global.dao.IServiceTarrifMasterDao;
import com.param.global.dao.ITarrifBedCategoryDao;
import com.param.global.dao.ITarrifPatientCategoryDao;
import com.param.global.dao.ITarrifPaymentCategoryDao;
import com.param.global.dao.IVariablePricingSequenceDao;
import com.param.global.dto.MySequenceDto;
import com.param.global.dto.ServiceChargesReqDto;
import com.param.global.dto.ServicePriceDetailsDto;
import com.param.global.dto.TarrifBedCategoryMpprDto;
import com.param.global.dto.TarrifPatientCategoryMpprDto;
import com.param.global.dto.TarrifPaymentEntitlementMpprDto;
import com.param.global.dto.VariableFactorDto;
import com.param.global.dto.VariablePricingDto;
import com.param.global.dto.VariablePricingSequenceDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@Transactional
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ServiceTarrifMasterServiceImpl implements IServiceTarrifMasterService, ICommonConstants, IError{
	final static Logger logger = Logger.getLogger(ServiceTarrifMasterServiceImpl.class);


	@Autowired
	IServiceTarrifMasterDao iServiceTarrifMasterDao;
	
	@Autowired
	ITarrifBedCategoryDao iTarrifBedCategoryDao;
	
	@Autowired
	ITarrifPatientCategoryDao iTarrifPatientCategoryDao;
	
	@Autowired
	ITarrifPaymentCategoryDao iTarrifPaymentCategoryDao;
	
	@Autowired
	IServiceMasterDao iServiceMasterDao;
	
	@Autowired
	IVariablePricingSequenceDao iServicePricingSequenceDao;
	
	
	@Override
	public Response saveServiceTarrifMaster(VariablePricingDto variablePricingDto) 
			throws ApplicationException {
		try{
			Response daoResponse =  iServiceTarrifMasterDao.saveServiceTarrifMaster(variablePricingDto);
			if(daoResponse.getStatus().equals(SUCCESS)){
				ServiceTarrifMaster serviceTarrifMaster = (ServiceTarrifMaster) daoResponse.getObject();
				
				//Save tarrif bed category factor list
				List<TarrifBedCategoryMpprDto> lisTarrifBedCategoryMpprDto = variablePricingDto.getListBedCategoryDto(); 
				
				if(lisTarrifBedCategoryMpprDto != null)
				for(TarrifBedCategoryMpprDto bedCategoryMpprDto : lisTarrifBedCategoryMpprDto){
					bedCategoryMpprDto.setServiceTarrifId(serviceTarrifMaster.getServiceTarrifMasterId());
					bedCategoryMpprDto.setOrgnisationId(variablePricingDto.getOrganisationId());
					bedCategoryMpprDto.setUnitId(variablePricingDto.getUnitId());
					iTarrifBedCategoryDao.saveServiceTarrifMaster(bedCategoryMpprDto);
				}
				
				// Save tarrif Patient Category factor details
				List<TarrifPatientCategoryMpprDto> listPatientCategoryMpprDto = variablePricingDto.getListPatientCategoryDto();
				if(listPatientCategoryMpprDto != null)
					for(TarrifPatientCategoryMpprDto patientCategoryMpprDto : listPatientCategoryMpprDto){
						patientCategoryMpprDto.setServiceTarrifMasterId(serviceTarrifMaster.getServiceTarrifMasterId());
						iTarrifPatientCategoryDao.savePatientCategoryMultiplicationFactor(patientCategoryMpprDto);
					}

				// Save tarrif Patient Category factor details
				List<TarrifPaymentEntitlementMpprDto> listpayEntitlementMpprDto = variablePricingDto.getListPaymentEntitlementDto();
				if(listpayEntitlementMpprDto != null)
					for(TarrifPaymentEntitlementMpprDto paymentEntitlementMpprDto : listpayEntitlementMpprDto){
						paymentEntitlementMpprDto.setServiceTarrifMasterId(serviceTarrifMaster.getServiceTarrifMasterId());
						iTarrifPaymentCategoryDao.savePaymentEntitlementFactor(paymentEntitlementMpprDto);
					}
				
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, serviceTarrifMaster);
				
			}
			
		}catch(Exception ex){
		logger.error("saveServiceTarrifMaster failed", ex);
		}
		return new Response<>(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}


	@Override
	public Response getServiceTarrifMaster(VariablePricingDto variablePricingDto)
			throws ApplicationException {
		
		Response responseDto = iServiceTarrifMasterDao.getServiceTarrifMaster(variablePricingDto);
		
		if(responseDto.getCode() == SUCCESS_CODE && !(responseDto.getListObject().isEmpty())){
			VariablePricingDto responseVariablePricingDto = (VariablePricingDto) responseDto.getListObject().get(0);
			
			//GET BedType multiplication factor
			Response responseBedTypeDto = iTarrifBedCategoryDao.getBedCategoryFactorByTarrifId(responseVariablePricingDto.getServiceTarrifMasterId());
			if(responseBedTypeDto.getCode() == SUCCESS_CODE){
				responseVariablePricingDto.setListBedCategoryDto(responseBedTypeDto.getListObject());
			}
			
			//GET Patient type multiplication factor
			Response responsePatientTypeDto = iTarrifPatientCategoryDao.getPatientCategoryMultiplicationFactorByTarrifId(responseVariablePricingDto.getServiceTarrifMasterId());
			if(responsePatientTypeDto.getCode() == SUCCESS_CODE){
				responseVariablePricingDto.setListPatientCategoryDto(responsePatientTypeDto.getListObject());
			}
			
			//GET Patient type multiplication factor
			Response responsePaymentTypeDto = iTarrifPaymentCategoryDao.getPaymentCategoryMultiplicationFactorByTarrifId(responseVariablePricingDto.getServiceTarrifMasterId());
			if(responsePatientTypeDto.getCode() == SUCCESS_CODE){
				responseVariablePricingDto.setListPaymentEntitlementDto(responsePaymentTypeDto.getListObject());
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, responseVariablePricingDto);	
		}
		
		return null;
	}


	@Override
	public Response listServiceMasterWithBasePriceByVisitType(Integer orgId,
			Integer unitId, Integer visitTypeId, Integer offset,
			Integer recordPerPage) throws ApplicationException {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = TimeZoneComponent.getDateByzone(unitId);
		//System.out.println("Start Time : "+dateFormat.format(date));
			double bedTypeFactor =0, patientTypeFactor =0, paymentTypeFactor=0, statTimeFactor=0;
			int bedTypeId = 1, patientTypeId = 1, paymentTypeId = 1, isStatTime=1;
			List<ServicePriceDetailsDto> listResultServicePriceDetailsDto = new ArrayList();
			//Servicelist with base price list
			try{
				com.param.global.common.Response iServiceBasePriceList = iServiceMasterDao.listServiceMasterWithBasePriceByVisitType(orgId, unitId, visitTypeId, offset, recordPerPage);
				
				if(iServiceBasePriceList.getStatus() == SUCCESS){
					List<ServicePriceDetailsDto> daoServicePriceDetails = iServiceBasePriceList.getListObject();
					
					//GET VARIABLE PRICING TARRIF DETAILS & SEQUENCE DETAILS
					VariablePricingSequenceDto variablePricingSequenceDto = new VariablePricingSequenceDto();
					variablePricingSequenceDto.setOrgId(orgId);
					variablePricingSequenceDto.setUnitId(unitId);
					variablePricingSequenceDto.setVisitType(visitTypeId);
					
					VariablePricingDto variablePricingDto = new VariablePricingDto();
					variablePricingDto.setOrganisationId(orgId);
					variablePricingDto.setUnitId(unitId);
					variablePricingDto.setVisitTypeId(visitTypeId);
					
					Response VPSeqDto = iServicePricingSequenceDao.getVariablePricingSequenceByOrganisationId(variablePricingSequenceDto);
					Response VFDto = iServiceTarrifMasterDao.getVariableFactorDetailsByVisitType(variablePricingDto);
					
					List<VariableFactorDto> listVariableFactorDto = null;
					if(VFDto.getCode() == SUCCESS_CODE && !VFDto.getListObject().isEmpty()){
						listVariableFactorDto = VFDto.getListObject();
						// GET ALL FACTOR VALUES 
						for(VariableFactorDto variableFactorDto : listVariableFactorDto){
							if(variableFactorDto.getBedTypeId() == bedTypeId && variableFactorDto.getTypeDiff() == 1)
								bedTypeFactor = variableFactorDto.getMultiplicationFactor();
							else if(variableFactorDto.getBedTypeId() == patientTypeId && variableFactorDto.getTypeDiff() == 2)
								patientTypeFactor = variableFactorDto.getMultiplicationFactor();
							else if(variableFactorDto.getBedTypeId() == paymentTypeId && variableFactorDto.getTypeDiff() == 3)
								paymentTypeFactor = variableFactorDto.getMultiplicationFactor();
						}
					}
					
					if(VPSeqDto.getCode() == SUCCESS_CODE && !VPSeqDto.getListObject().isEmpty() && listVariableFactorDto !=null){
						VariablePricingSequenceDto seqDto = (VariablePricingSequenceDto) VPSeqDto.getListObject().get(0);
						
						List<MySequenceDto> listMyDto = new ArrayList();
						listMyDto.add(new MySequenceDto(seqDto.getPatientTypeSeqNo(), "PT"));
						listMyDto.add(new MySequenceDto(seqDto.getBillingBedCategorySeqNo(), "BT"));
						listMyDto.add(new MySequenceDto(seqDto.getPaymentEntitlementSeqNo(), "PET"));
						listMyDto.add(new MySequenceDto(seqDto.getStatChargeSeqNo(), "ST"));
						Collections.sort(listMyDto, new MySequenceDto());
						
						
						for(ServicePriceDetailsDto servicePriceDetailsDto : daoServicePriceDetails){
							Double tariffPrice = servicePriceDetailsDto.getBasePrice();
							
							ServicePriceDetailsDto resultServicePriceDetailsDto = new ServicePriceDetailsDto();
							resultServicePriceDetailsDto = servicePriceDetailsDto;
							for(MySequenceDto m : listMyDto){
								////System.out.println(m.getSeqNo() + "-"+ m.getSeqDesc());
								
								if(m.getSeqDesc().equals("BT")){
									tariffPrice = tariffPrice + (tariffPrice*bedTypeFactor/100);
									resultServicePriceDetailsDto.setBedCategoryFactor(tariffPrice);
									resultServicePriceDetailsDto.setBedCategoryMultiplicationFactor(bedTypeFactor);
								}else if(m.getSeqDesc().equals("PT")){
									tariffPrice = tariffPrice + (tariffPrice*patientTypeFactor/100);
									resultServicePriceDetailsDto.setPatientTypeFactor(tariffPrice);
									resultServicePriceDetailsDto.setPatientTypeMultiplicationFactor(patientTypeFactor);
								}else if(m.getSeqDesc().equals("PET")){
									tariffPrice = tariffPrice + (tariffPrice*paymentTypeFactor/100);
									resultServicePriceDetailsDto.setPaymentEntitlementFactor(tariffPrice);
									resultServicePriceDetailsDto.setPaymentEntitlementMultiplicationFactor(paymentTypeFactor);
								}else if(m.getSeqDesc().equals("ST")){
									//stat charges calculation is remainng
								}
							}
							resultServicePriceDetailsDto.setBasePrice(tariffPrice);
							listResultServicePriceDetailsDto.add(resultServicePriceDetailsDto);
						}
						
					}else{
						return new Response<>(ERROR, ERR_204, ERR_201_MESSAGE, null, null);
					}
				}
				Date date1 = TimeZoneComponent.getDateByzone(unitId);
				//System.out.println("End Time : "+dateFormat.format(date1));
				long difference = date1.getTime() - date.getTime();
				//System.out.println("time taken: "+difference);
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, listResultServicePriceDetailsDto);	
			}catch(Exception ex){
			logger.error("listServiceMasterWithBasePriceByVisitType failed", ex);
			}
		return null;
	}


	@Override
	public Response getServiceCharges(ServiceChargesReqDto serviceChargesReqDto)throws ApplicationException {

		/*DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = TimeZoneComponent.getDateByzone(unitId);
		//System.out.println("Start Time : "+dateFormat.format(date));
			double bedTypeFactor =0, patientTypeFactor =0, paymentTypeFactor=0, statTimeFactor=0;
			//int bedTypeId = 1, patientTypeId = 1, paymentTypeId = 1, isStatTime=1;
			List<ServicePriceDetailsDto> listResultServicePriceDetailsDto = new ArrayList();*/
			//Servicelist with base price list
			try{
				com.param.global.common.Response iServiceBasePriceList = iServiceMasterDao.listServiceMasterByServiceId(serviceChargesReqDto.getOrgId(), serviceChargesReqDto.getUnitId(),
						serviceChargesReqDto.getListServiceId());
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, iServiceBasePriceList.getListObject());
				/*if(iServiceBasePriceList.getStatus() == SUCCESS){
					List<ServicePriceDetailsDto> daoServicePriceDetails = iServiceBasePriceList.getListObject();
					
					//GET VARIABLE PRICING TARRIF DETAILS & SEQUENCE DETAILS
					VariablePricingSequenceDto variablePricingSequenceDto = new VariablePricingSequenceDto();
					variablePricingSequenceDto.setOrgId(serviceChargesReqDto.getOrgId());
					variablePricingSequenceDto.setUnitId(serviceChargesReqDto.getUnitId());
					variablePricingSequenceDto.setVisitType(serviceChargesReqDto.getVisitTypeId());
					
					VariablePricingDto variablePricingDto = new VariablePricingDto();
					variablePricingDto.setOrganisationId(serviceChargesReqDto.getOrgId());
					variablePricingDto.setUnitId(serviceChargesReqDto.getUnitId());
					variablePricingDto.setVisitTypeId(serviceChargesReqDto.getVisitTypeId());
					
					Response VPSeqDto = iServicePricingSequenceDao.getVariablePricingSequenceByOrganisationId(variablePricingSequenceDto);
					Response VFDto = iServiceTarrifMasterDao.getVariableFactorDetailsByVisitType(variablePricingDto);
					
					List<VariableFactorDto> listVariableFactorDto = null;
					if(VFDto.getCode() == SUCCESS_CODE && !VFDto.getListObject().isEmpty()){
						listVariableFactorDto = VFDto.getListObject();
						// GET ALL FACTOR VALUES 
						for(VariableFactorDto variableFactorDto : listVariableFactorDto){
							if(variableFactorDto.getBedTypeId() == serviceChargesReqDto.getBedTypeId() && variableFactorDto.getTypeDiff() == 1){
								bedTypeFactor = variableFactorDto.getMultiplicationFactor();
							}else if(variableFactorDto.getBedTypeId() == serviceChargesReqDto.getPatientTypeId() && variableFactorDto.getTypeDiff() == 2){
								patientTypeFactor = variableFactorDto.getMultiplicationFactor();
							}else if(variableFactorDto.getBedTypeId() == serviceChargesReqDto.getPaymentTypeId() && variableFactorDto.getTypeDiff() == 3){
								paymentTypeFactor = variableFactorDto.getMultiplicationFactor();
							}
						}
					}
					
					if(VPSeqDto.getCode() == SUCCESS_CODE && !VPSeqDto.getListObject().isEmpty() && listVariableFactorDto !=null){
						VariablePricingSequenceDto seqDto = (VariablePricingSequenceDto) VPSeqDto.getListObject().get(0);
						
						List<MySequenceDto> listMyDto = new ArrayList();
						listMyDto.add(new MySequenceDto(seqDto.getPatientTypeSeqNo(), "PT"));
						listMyDto.add(new MySequenceDto(seqDto.getBillingBedCategorySeqNo(), "BT"));
						listMyDto.add(new MySequenceDto(seqDto.getPaymentEntitlementSeqNo(), "PET"));
						listMyDto.add(new MySequenceDto(seqDto.getStatChargeSeqNo(), "ST"));
						Collections.sort(listMyDto, new MySequenceDto());
						
						
						for(ServicePriceDetailsDto servicePriceDetailsDto : daoServicePriceDetails){
							Double tariffPrice = servicePriceDetailsDto.getBasePrice();
							
							ServicePriceDetailsDto resultServicePriceDetailsDto = new ServicePriceDetailsDto();
							resultServicePriceDetailsDto = servicePriceDetailsDto;
							for(MySequenceDto m : listMyDto){
								////System.out.println(m.getSeqNo() + "-"+ m.getSeqDesc());
								
								// REMIANING
								if(m.getSeqDesc().equals("BT")){
									tariffPrice = tariffPrice + (tariffPrice*bedTypeFactor/100);
									resultServicePriceDetailsDto.setBedCategoryFactor(tariffPrice);
									resultServicePriceDetailsDto.setBedCategoryMultiplicationFactor(bedTypeFactor);
								}else if(m.getSeqDesc().equals("PT")){
									tariffPrice = tariffPrice + (tariffPrice*patientTypeFactor/100);
									resultServicePriceDetailsDto.setPatientTypeFactor(tariffPrice);
									resultServicePriceDetailsDto.setPatientTypeMultiplicationFactor(patientTypeFactor);
								}else if(m.getSeqDesc().equals("PET")){
									tariffPrice = tariffPrice + (tariffPrice*paymentTypeFactor/100);
									resultServicePriceDetailsDto.setPaymentEntitlementFactor(tariffPrice);
									resultServicePriceDetailsDto.setPaymentEntitlementMultiplicationFactor(paymentTypeFactor);
								}else if(m.getSeqDesc().equals("ST")){
									//stat charges calculation is remainng
								}
							}
							resultServicePriceDetailsDto.setBasePrice(tariffPrice);
							listResultServicePriceDetailsDto.add(resultServicePriceDetailsDto);
						}
						
					}else{
						return new Response<>(ERROR, ERR_204, ERR_201_MESSAGE, null, null);
					}
				}
				//return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, listResultServicePriceDetailsDto);	*/
			}catch(Exception ex){
			logger.error("getServiceCharges failed", ex);
			}
		return null;
	}

}
