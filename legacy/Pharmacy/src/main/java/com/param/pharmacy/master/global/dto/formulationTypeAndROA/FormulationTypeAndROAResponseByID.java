package com.param.pharmacy.master.global.dto.formulationTypeAndROA;

import java.util.ArrayList;
import java.util.List;

import com.param.pharmacy.master.unit.dto.itemRestriction.ItemRestrictionResponse;

public class FormulationTypeAndROAResponseByID {

	protected Integer id;

	protected String formulationType;

	protected String routeOfAdministration;
	
	protected Boolean status;
	
	private Integer formulationTypeID;
	
	private Integer routeOfAdministrationID;

	public FormulationTypeAndROAResponseByID() {
		super();
	}

	public FormulationTypeAndROAResponseByID(Integer id, String formulationType, String routeOfAdministration,Integer formulationTypeID,Integer routeOfAdministrationID
			,Boolean status) {
		super();
		this.id = id;
		this.formulationType = formulationType;
		this.routeOfAdministration = routeOfAdministration;
		this.formulationTypeID=formulationTypeID;
		this.routeOfAdministrationID=routeOfAdministrationID;
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

	public Integer getFormulationTypeID() {
		return formulationTypeID;
	}

	public void setFormulationTypeID(Integer formulationTypeID) {
		this.formulationTypeID = formulationTypeID;
	}

	public Integer getRouteOfAdministrationID() {
		return routeOfAdministrationID;
	}

	public void setRouteOfAdministrationID(Integer routeOfAdministrationID) {
		this.routeOfAdministrationID = routeOfAdministrationID;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}


}
