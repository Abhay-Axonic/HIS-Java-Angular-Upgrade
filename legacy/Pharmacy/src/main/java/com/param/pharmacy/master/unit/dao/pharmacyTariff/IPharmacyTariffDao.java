package com.param.pharmacy.master.unit.dao.pharmacyTariff;

import com.param.global.common.Response;
import com.param.pharmacy.master.unit.dto.pharmacyTariff.PharmacyTariffDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IPharmacyTariffDao {

	Response savePharmacyTariff(PharmacyTariffDto pharmacyTariffDto)throws ApplicationException;

	Response getAllPharmacyTariff(Integer orgId, Integer unitId)throws ApplicationException;

	Response updateStatus(Integer id, Character status) throws ApplicationException;

	Response updatePharmacyTariff(PharmacyTariffDto pharmacyTariffDto)throws ApplicationException;

}
