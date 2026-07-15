package com.param.global.service;

import java.util.List;

import javax.transaction.Transactional;
import com.param.global.common.Response;
import com.param.global.dto.ServiceMasterDto;
import com.param.global.dto.ServiceProfitMarginDto;
import com.param.global.dto.UnitServiceTariffMasterDto;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IUnitServiceTariffMasterService {

	@Transactional
	Response getMasterServiceListByMultipleParameters(UnitServiceTariffMasterDto unitServiceTariffMasterDto)throws ApplicationException;

	@Transactional
	Response saveUnitServiceTariffMaster(UnitServiceTariffMasterDto unitServiceTariffMasterDto)throws ApplicationException;
	
	@Transactional
	UnitServiceTariffMasterDto getBasePriceByServiceTariffMaster(UnitServiceTariffMasterDto unitServiceTariffMasterDto)throws ApplicationException;

	@Transactional
	Response getBasePriceOfServicesFromTariffMasterByServiceList(UnitServiceTariffMasterDto unitServiceTariffMasterDto)throws ApplicationException;

	@Transactional
	Response getUnitServiceTariffMasterList(UnitServiceTariffMasterDto unitServiceTariffMasterDto)throws ApplicationException;

	@Transactional
	Response getProfitMarginList(UnitServiceTariffMasterDto unitServiceTariffMasterDto);

	@Transactional
	Response saveServiceProfitMargin(List<ServiceProfitMarginDto> serviceProfitMarginDto);
	
	@Transactional
	Response getServiceListBySubSpecialityId(UnitServiceTariffMasterDto unitServiceTariffMasterDto)throws ApplicationException;
	
	@Transactional
	Response getServiceListBySpecialityIdSubSpecialityList(UnitServiceTariffMasterDto unitServiceTariffMasterDto)throws ApplicationException;

	Response uniqueCheck(UnitServiceTariffMasterDto unitServiceTariffMasterDto);

	@Transactional
	Response getServiceListBySubSpecialityIdNotByTariff(UnitServiceTariffMasterDto unitServiceTariffMasterDto);
	
	@Transactional
	Response searchServicesByNameAndCode(ServiceMasterDto serviceMasterDto);

	@Transactional
	Response checkUnitServiceMasterMapping(List<Integer> serviceIds, Integer unitId)throws ApplicationException;
	@Transactional
	Response getUnitServiceTariffMasterListForWeb(UnitServiceTariffMasterDto unitServiceTariffMasterDto);
}
