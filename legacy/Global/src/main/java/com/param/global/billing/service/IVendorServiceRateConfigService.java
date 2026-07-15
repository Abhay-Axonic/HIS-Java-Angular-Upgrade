package com.param.global.billing.service;

import com.param.global.common.Response;
import com.param.global.dto.VendorServiceRateConfigDto;

public interface IVendorServiceRateConfigService {

	Response saveVendorServiceRateConfig(VendorServiceRateConfigDto vendorServiceRateConfigDto);

	Response uniqueCheck(VendorServiceRateConfigDto vendorServiceRateConfigDto);

	Response getVendorServiceRateConfigList(VendorServiceRateConfigDto vendorServiceRateConfigDto);

	Response generateVendorServiceRateConfigList(VendorServiceRateConfigDto vendorServiceRateConfigDto);

	Response getEHCPackageList(VendorServiceRateConfigDto vendorServiceRateConfigDto);
	
	Response getEHCPackageServices(VendorServiceRateConfigDto vendorServiceRateConfigDto);

}
