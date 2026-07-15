package com.param.pharmacy.master.unit.dao.configuration;

import java.util.List;

import com.param.entity.model.master.PrefixConfiguration;
import com.param.entity.model.master.PrefixRunningNumber;
import com.param.entity.model.master.Store;
import com.param.pharmacy.master.unit.dto.configuration.PrefixConfigurationDto;

public interface IPrefixConfigurationDao {

	Integer create(PrefixConfiguration prefixConfiguration);

	PrefixConfiguration getById(Integer id);

	Integer updatePrefixConfiguration(PrefixConfiguration prefixConfiguration);

	List<PrefixConfiguration> getAll(Boolean flag, Integer orgId, Integer unitId);

	List<Store> getStoreList(int unitId);

	Long save(PrefixRunningNumber prefixRunningNumber);

	PrefixConfiguration getByScreenId(PrefixConfigurationDto obj);

}
