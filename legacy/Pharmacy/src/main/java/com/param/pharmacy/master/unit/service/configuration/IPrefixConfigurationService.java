package com.param.pharmacy.master.unit.service.configuration;

import java.util.List;

import com.param.pharmacy.master.unit.dto.configuration.PrefixConfigurationDropdown;
import com.param.pharmacy.master.unit.dto.configuration.PrefixConfigurationDto;

public interface IPrefixConfigurationService {

	PrefixConfigurationDropdown getDropdownList();

	Object savePrefixConfiguration(PrefixConfigurationDto request);

	Object update(PrefixConfigurationDto prefixConfigurationDto);

	PrefixConfigurationDto getById(Integer id);

	List<PrefixConfigurationDto> getAll(Boolean flag, Integer orgId, Integer unitId);

}
