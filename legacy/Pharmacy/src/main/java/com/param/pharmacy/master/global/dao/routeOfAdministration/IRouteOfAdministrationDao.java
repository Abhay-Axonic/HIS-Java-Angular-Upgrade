package com.param.pharmacy.master.global.dao.routeOfAdministration;

import java.util.List;

import com.param.entity.model.master.RouteOfAdministration;

public interface IRouteOfAdministrationDao {

	List<RouteOfAdministration> getAll(Integer orgId);

	RouteOfAdministration getById(Integer id);

	Integer create(RouteOfAdministration routeOfAdministration);

	Integer update(RouteOfAdministration routeOfAdministration);

	Long getRouteOfAdministrationCountFromFormulationROA(Integer id);

}
