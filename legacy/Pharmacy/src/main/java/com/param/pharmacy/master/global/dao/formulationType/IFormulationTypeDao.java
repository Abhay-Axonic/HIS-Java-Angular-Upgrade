package com.param.pharmacy.master.global.dao.formulationType;

import java.util.List;

import com.param.entity.model.master.FormulationType;
import com.param.entity.model.master.ProductCategory;
import com.param.pharmacy.master.global.dto.formulationType.FormulationTypeListResponse;

public interface IFormulationTypeDao {

	List<FormulationType> getAll(Integer orgId);
	
	FormulationType getById(Integer id, Integer orgId);
	
	ProductCategory getId(Integer id);

	Integer create(FormulationType formulationType);

	Integer update(FormulationType formulationType);

	Long getFormulationTypeCountFromItem(Integer id);

	Long getFormulationTypeCountFromFormulaROA(Integer id);

	List<FormulationTypeListResponse> getAllFormulation(Integer organizationId);

//	List<FormulationType> getFormulationTypeList();
}
