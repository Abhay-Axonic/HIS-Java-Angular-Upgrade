package com.param.pharmacy.master.unit.service.applicationConfiguration;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.unit.dto.applicationConfiguration.ApplicationConfigurationRequest;
import com.param.pharmacy.master.unit.dto.applicationConfiguration.ApplicationConfigurationResponse;

public interface IApplicationConfigurationService {
	
	List<ApplicationConfigurationResponse> getAll(Integer unitId);
	
	CommonResponse saveNew(ApplicationConfigurationRequest applicationConfigurationRequest);
	
	CommonResponse updateApplicationConfiguration(ApplicationConfigurationRequest applicationConfigurationRequest);

	
	CommonResponse saveApplicationConfiguration(ApplicationConfigurationRequest applicationConfigurationRequest);

	CommonResponse update(ApplicationConfigurationRequest applicationConfigurationRequest);

	CommonResponse savePharmacyApplicationConfiguration(ApplicationConfigurationRequest applicationConfigurationRequest);

	List<ApplicationConfigurationResponse> getAllPharmacy();

	CommonResponse updatePharmacyApplicationConfiguration(ApplicationConfigurationRequest applicationConfigurationRequest);

}
