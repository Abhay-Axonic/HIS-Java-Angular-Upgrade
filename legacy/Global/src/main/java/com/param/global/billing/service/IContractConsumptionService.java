package com.param.global.billing.service;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.ServiceSearchResDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IContractConsumptionService {

	Response consumeServiceFromContract(ContractConsumptionDto consumptionDto)throws ApplicationException;
	
	Response addCapAmount(ContractConsumptionDto consumptionDto)throws ApplicationException;

	Response getActivePaymentEntitlement(ContractConsumptionDto consumptionDto)throws ApplicationException;

	Response consumePackageFromContract(ServiceSearchResDto searchResDto)throws ApplicationException;
	
	Response getSerivceRateGlobal(ContractConsumptionDto consumptionDto,boolean isProcedure, boolean isFromUpdatePayee)throws ApplicationException;

	Response consumeServiceFromContractPackage(ContractConsumptionDto consumptionDto,List<ContractConsumptionDto>  contractConsumptionList);

	OrderMasterDto  getPatientClassByVisit(OrderMasterDto orderMasterDto) 	throws ApplicationException;
	
	Response resetContractCapping(Integer encounterId, Integer admissionId) throws ApplicationException;

	Response addCapContract(ContractConsumptionDto consumptionDto) throws ApplicationException;

	Response addCapPackage(ContractConsumptionDto consumptionDto) throws ApplicationException;

	Response isConsultationAmountPaidBySelf(ContractConsumptionDto consumptionDto)throws ApplicationException;

	Response getSerivceRateGlobalAfterTariffChange(ContractConsumptionDto consumptionDto, boolean b, boolean c);

	Character getIsBedChargesService(ContractConsumptionDto contractResp);

	boolean getGstApplicableForBillingBedCategory(Integer unitId);

	List<ContractConsumptionDto> getgstDetails(Integer billingBedCategoryId);
}
