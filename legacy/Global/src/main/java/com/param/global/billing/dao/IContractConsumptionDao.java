package com.param.global.billing.dao;

import java.util.List;

import com.param.global.billing.dto.CommonBillingItemsDto;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.TaxDto;

import in.param.exception.ApplicationException;
public interface IContractConsumptionDao {

	List<ContractConsumptionDto> getServiceDetailsByContractId(ContractConsumptionDto itemsForContractDto) throws ServiceException;
	
	ContractConsumptionDto getItemDetailsByContractId(ContractConsumptionDto itemsForContractDto,Integer itemId) throws ServiceException;
	
	ContractConsumptionDto getProcedureDetailsByContract(ContractConsumptionDto procedureForContract,boolean isProcedure) throws ServiceException;

	List<ContractConsumptionDto> getSponsorDetailsByEncounterId(ContractConsumptionDto consumptionDto)throws ServiceException;
	
	List<ContractConsumptionDto> getSponsorDetailsByEncounterPharmacy(ContractConsumptionDto consumptionDto)throws ServiceException;
	

	void updateCapAmount(ContractConsumptionDto consumptionDto, Double creaditPayable)throws ServiceException;

	Integer addCapAmount(ContractConsumptionDto consumptionDto)throws ServiceException;

	ContractConsumptionDto getVertualServiceDetailsByContractId(ContractConsumptionDto contractConsumptionDto)throws ServiceException;

	TaxDto getTaxPerByServiceId(Integer serviceId, Integer unitId);
	
	//walk in sales
     Response getActivePaymentEntitlement(ContractConsumptionDto consumptionDto)throws ApplicationException;

	 //coroprate and insurance
	 Response isConsultationAmountPaidBySelf(ContractConsumptionDto dto)throws ApplicationException;
     
     
     Integer updateProcedureCapAmount(ContractConsumptionDto consumptionDto,Double creditPayable);

     List<ContractConsumptionDto> getServiceCapDetailsByContractId(ContractConsumptionDto consumptionDto);

	void updateServiceCapAmount(ContractConsumptionDto consumptionDto, Double creaditPayable, Double qty)throws ServiceException;

	Integer getFallBacktariff(Integer contractId, Integer admission);

	List<ContractConsumptionDto> getPackageDetailsByContractId(ContractConsumptionDto contractConsumptionDto)
			throws ServiceException;
	
	OrderMasterDto getPatientClassByVisit(OrderMasterDto orderMasterDto) 	throws ApplicationException;
	
	ContractConsumptionDto  getPatientClassAndPercentageByVisit(ContractConsumptionDto contractConsumptionDto) 	throws ApplicationException;
	
	
	Integer addCapAmountPharmacy(CommonBillingItemsDto itemRequest);
	
	Integer consumeCapAmountPharmacy(CommonBillingItemsDto itemRequest);
    
    Integer updatePharmacyCapQuantity(ContractConsumptionDto consumptionDto,Double creditPayable,Integer quantity);

	void updatePreAuthAmount(Integer preAuthId, Double amount) throws ServiceException;

	Integer resetContractCapping(Integer encounterId, Integer admissionId) throws ServiceException;

	ContractConsumptionDto getPatientClassAndPercentageByVisitWithountEncounter(ContractConsumptionDto consumptionDto);

	boolean getPreAuthAmutByVisitId(Integer encounterId, Integer admissionId);

	List<ContractConsumptionDto> getServiceDetailsByContractIdAfterTariffChange(ContractConsumptionDto consumptionDto);

	boolean getGstApplicableForBillingBedCategory(Integer unitId);

	List<ContractConsumptionDto> getgstDetails(Integer billingBedCategoryId);

}
