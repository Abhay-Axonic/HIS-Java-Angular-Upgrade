package com.param.billing.unit.dao;

import com.param.billing.global.transaction.model.TariffMaster;
import com.param.global.common.Response;
import com.param.global.dto.TariffMasterDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ITariffMasterDao extends IGenericDao<TariffMaster,Integer>{

	Response saveTariffMaster(TariffMasterDto tariffMasterDto)throws ApplicationException;
	
	Response saveTariffPaymentEntitlementMapper(TariffMasterDto tariffMasterDto)throws ApplicationException;

	Response getTariffListByName(TariffMasterDto tariffMasterDto)throws ApplicationException;

	Response getTariffMasterList(TariffMasterDto tariffMasterDto)throws ApplicationException;

	Response getPaymentEntitlementListByTariffid(TariffMasterDto tariffMasterDto)throws ApplicationException;

	Response getTariffListById(TariffMasterDto tariffMasterDto)throws ApplicationException;

	Response getTariffListByNameNotId(TariffMasterDto tariffMasterDto)throws ApplicationException;

	Response updateTariffMaster(TariffMasterDto tariffMasterDto)throws ApplicationException;

	Response inactiveOldPaymentEntitlements(TariffMasterDto tariffMasterDto)throws ApplicationException;

	Response updateStatusForTariff(TariffMasterDto tariffMasterDto)throws ApplicationException;

	Response updateStatusForTariffPaymentMapper(TariffMasterDto tariffMasterDto)throws ApplicationException;

	Response getActiveTariffMasterList(TariffMasterDto tariffMasterDto)throws ApplicationException;

	Response getPaymentEntitlementListByTariffIdList(TariffMasterDto tariffMasterDto)throws ApplicationException;

	Response saveVisitTypeMapper(TariffMasterDto tariffMasterDto) throws ApplicationException;

	Response saveTariffPatientTypeMapper(TariffMasterDto tariffMasterDto) throws ApplicationException;

	//Response saveTariffBillingBedCategoryMapper(TariffMasterDto tariffMasterDto) throws ApplicationException;

	Response getVisitTypeListByTariffIdList(TariffMasterDto tariffMasterDto) throws ApplicationException;

	Response getPatientTypeListByTariffIdList(TariffMasterDto tariffMasterDto) throws ApplicationException;

	Response getBillingBedCategoryByTariffIdList(TariffMasterDto tariffMasterDto) throws ApplicationException;

	Response updateStatusForTariffBillingBedCategoryMapper(TariffMasterDto tariffMasterDto) throws ApplicationException;

	Response getBillingBedCategoryListByTariffId(TariffMasterDto tariffMasterDto) throws ApplicationException;

	Response updateStatusForTariffPatientTypeMapper(TariffMasterDto tariffMasterDto) throws ApplicationException;

	Response getPatientTypeListByTariffId(TariffMasterDto tariffMasterDto) throws ApplicationException;

	Response updateStatusForTariffVisitTypeMapper(TariffMasterDto tariffMasterDto) throws ApplicationException;

	Response getVisitTypeListByTariffId(TariffMasterDto tariffMasterDto) throws ApplicationException;


	Response tariffMasterCount(TariffMasterDto tariffMasterDto) throws ApplicationException;

	Response tariffPaymentEntitlementMapper(TariffMasterDto tariffMasterDto) throws ApplicationException;

	Response tariffVisitTypeMapper(TariffMasterDto tariffMasterDto)throws ApplicationException;

	Response tariffTariffPatientTypeMapper(TariffMasterDto tariffMasterDto)throws ApplicationException;

	Response getTariffMasterListOpdBilling(TariffMasterDto tariffMasterDto);
	
}
