package com.param.pharmacy.master.unit.dao.applicationConfiguration;

import java.util.List;

import com.param.entity.model.master.ApplicationConfiguration;

public interface IApplicationConfigurationDao {

	List<ApplicationConfiguration> getAll(Integer unitId);
	
	ApplicationConfiguration getApplicationConfiguration(Integer unitId);
	
	//Long getCountApplicationConfiguration();

	Integer update(ApplicationConfiguration applicationConfiguration);

	ApplicationConfiguration saveApplicationConfiguration(ApplicationConfiguration applicationConfiguration);

	//ApplicationConfiguration getApplicationConfiguration(Integer id);

	Long getCountApplicationConfiguration(Integer unitId);

	ApplicationConfiguration getApplicationConfigurationByUnitId(Integer unitId);

}
