package com.param.pharmacy.master.global.dao.formulationTypeAndROA;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.FormulationAndRoa;
import com.param.entity.model.master.FormulationType;
import com.param.entity.model.master.RouteOfAdministration;
import com.param.pharmacy.master.global.dto.formulationTypeAndROA.FormulationRoaRequest;

public interface IFormulationTypeAndROADao {

	List<FormulationAndRoa> getAll(Integer orgId);

//	Integer create(FormulationAndRoa formulationAndRoa);
	
	FormulationAndRoa saveFormulationTypeAndROA(FormulationAndRoa formulationAndRoa);

	FormulationAndRoa getById(Integer id);

	FormulationType getFormulationType(Integer formulationtypeid);

	RouteOfAdministration getRouteOfAdministration(Integer formulationtypeid);

	Integer update(FormulationAndRoa formulationAndRoa);
	
	List<RouteOfAdministration> getDropdownList(Integer orgId);

	Long getFormulationROACountFromFormulationType(Integer id);

	Long getFormulationROACountFromROA(Integer id);

	Long getCount(Integer orgId);

	List<FormulationAndRoa> getFormulationTypeAndROA(Integer offset, Integer noOfRecordsPerPage, Integer orgId);

	
}
