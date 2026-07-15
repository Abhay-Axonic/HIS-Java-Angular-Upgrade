package com.param.billing.unit.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.TariffMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")

public interface ITariffMasterService {

	@Transactional
	Response saveTariffMaster(TariffMasterDto tariffMasterDto)throws ApplicationException;

	@Transactional
	Response getTariffMasterList(TariffMasterDto tariffMasterDto)throws ApplicationException;

	@Transactional
	Response getPaymentEntitlementListByTariffid(TariffMasterDto tariffMasterDto)throws ApplicationException;

	@Transactional
	Response getTariffListById(TariffMasterDto tariffMasterDto)throws ApplicationException;

	@Transactional
	Response updateTariffMaster(TariffMasterDto tariffMasterDto)throws ApplicationException;

	@Transactional
	Response updateStatusForTariff(TariffMasterDto tariffMasterDto)throws ApplicationException;

	@Transactional
	Response updateStatusForTariffPaymentMapper(TariffMasterDto tariffMasterDto)throws ApplicationException;

	@Transactional
	Response getActiveTariffMasterList(TariffMasterDto tariffMasterDto)throws ApplicationException;

	@Transactional
	Response getPaymentEntitlementListByTariffIdList(TariffMasterDto tariffMasterDto)throws ApplicationException;
	
	@Transactional
	Response getVisitTypeListByTariffIdList(TariffMasterDto tariffMasterDto) throws ApplicationException;
	
	@Transactional
	Response getPatientTypeListByTariffIdList(TariffMasterDto tariffMasterDto) throws ApplicationException;
	
	@Transactional
	Response getBillingBedCategoryByTariffIdList(TariffMasterDto tariffMasterDto) throws ApplicationException;

	@Transactional
	Response getVisitTypeListByTariffId(TariffMasterDto tariffMasterDto) throws ApplicationException;

	@Transactional
	Response updateStatusForTariffVisitTypeMapper(TariffMasterDto tariffMasterDto) throws ApplicationException;

	@Transactional
	Response getPatientTypeListByTariffId(TariffMasterDto tariffMasterDto) throws ApplicationException;

	@Transactional
	Response updateStatusForTariffPatientTypeMapper(TariffMasterDto tariffMasterDto) throws ApplicationException;

	@Transactional
	Response getBillingBedCategoryListByTariffId(TariffMasterDto tariffMasterDto) throws ApplicationException;

	@Transactional
	Response updateStatusForTariffBillingBedCategoryMapper(TariffMasterDto tariffMasterDto) throws ApplicationException;
	
	@Transactional
	Response tariffMasterCount(TariffMasterDto tariffMasterDto) throws ApplicationException;

	Response getTariffMasterListOpdBilling(TariffMasterDto tariffMasterDto);

}
