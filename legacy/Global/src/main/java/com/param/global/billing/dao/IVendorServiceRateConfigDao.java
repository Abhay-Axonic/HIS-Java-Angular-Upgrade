package com.param.global.billing.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.VendorServiceRateConfigDto;
import com.param.global.pharmacy.dto.PendingServicesForWorkOrderCreationDto;
import com.param.global.pharmacy.dto.VendorRateDto;
@SuppressWarnings("rawtypes")
public interface IVendorServiceRateConfigDao {

	Response saveVendorServiceRateConfig(List<VendorServiceRateConfigDto> vendorServiceRateConfigDtoList);

	void inactiveDuplicateIds(List<Integer> duplicateIdsList);

	Response getUniqueEffectiveDate(VendorServiceRateConfigDto vendorServiceRateConfigDto);

	Response getVendorServiceRateConfigList(VendorServiceRateConfigDto vendorServiceRateConfigDto);

	List<VendorRateDto> getVendorRatesListForService(PendingServicesForWorkOrderCreationDto creationDto);

	Response getEHCPackageList(VendorServiceRateConfigDto vendorServiceRateConfigDto);

	Response getEHCPackageServices(VendorServiceRateConfigDto vendorServiceRateConfigDto);

	List<VendorServiceRateConfigDto> getEHCPackageServicesForGeneratingList(VendorServiceRateConfigDto vendorServiceRateConfigDto);

}
