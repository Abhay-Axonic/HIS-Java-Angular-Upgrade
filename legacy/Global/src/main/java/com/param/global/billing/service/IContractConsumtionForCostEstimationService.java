package com.param.global.billing.service;


import com.param.global.common.Response;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.ServiceSearchResDto;
import com.param.opd.encounter.dto.EncounterMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IContractConsumtionForCostEstimationService {

	Response consumeServiceFromContract(ContractConsumptionDto consumptionDto)throws ApplicationException;
	
	Response consumeConsultationServiceFromContract(ContractConsumptionDto consumptionDto)throws ApplicationException;

	Response addCapAmount(ContractConsumptionDto consumptionDto)throws ApplicationException;

	Response getVertualServiceDetailsByContractId(ContractConsumptionDto consumptionDto,EncounterMasterDto encounterMasterDto)throws ApplicationException;
	
	Response getActivePaymentEntitlement(ContractConsumptionDto consumptionDto)throws ApplicationException;

	Response consumeServiceFromContractForPeriodicityBilling(ContractConsumptionDto consumptionDto)throws ApplicationException;

	Response consumePackageFromContract(ServiceSearchResDto searchResDto)throws ApplicationException;
	
	Response consumeProcedureFromContract(ContractConsumptionDto consumptionDto,boolean isProcedure)throws ApplicationException;
	

}
