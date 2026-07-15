package com.param.pharmacy.master.global.dto.formulationTypeAndROA;

import java.util.ArrayList;
import java.util.List;

import com.param.pharmacy.master.unit.dto.itemRestriction.ItemRestrictionResponse;

public class FormulationTypeAndROAResponse {

	protected Integer id;

	protected String formulationType;

	protected String routeOfAdministration;
	
	protected Boolean status;
	

	public FormulationTypeAndROAResponse() {
		super();
	}

	public FormulationTypeAndROAResponse(Integer id, String formulationType, String routeOfAdministration,
			Boolean status) {
		super();
		this.id = id;
		this.formulationType = formulationType;
		this.routeOfAdministration = routeOfAdministration;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFormulationType() {
		return formulationType;
	}

	public void setFormulationType(String formulationType) {
		this.formulationType = formulationType;
	}

	public String getRouteOfAdministration() {
		return routeOfAdministration;
	}

	public void setRouteOfAdministration(String routeOfAdministration) {
		this.routeOfAdministration = routeOfAdministration;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}


}
