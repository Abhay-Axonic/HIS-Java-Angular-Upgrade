package com.param.pharmacy.master.unit.genralinsurancedao;

import java.util.List;

import com.param.entity.model.master.Area;
import com.param.entity.model.master.GeneralInsurance;

public interface IGeneralInsuranceMasterDao {

	List<GeneralInsurance> getAll();

	Integer save(GeneralInsurance genralInsurance);

	GeneralInsurance getById(Integer id);

	Area getArea(Integer areaId);

	Integer update(GeneralInsurance generalInsurance);
	
}
