package com.param.pharmacy.master.global.dto.formulationTypeAndROA;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class FormulationRoaResponse {

	protected BigInteger id;

	protected Boolean status;

	protected Integer formulationtypeid;

	protected Integer routeOfAdministrationId;
	
	protected List<FormulationRoaResponse> items;

	
	public FormulationRoaResponse() {
		super();
	}

	public FormulationRoaResponse(BigInteger id, Boolean status, Integer formulationtypeid,
			Integer routeOfAdministrationId, List<FormulationRoaResponse> items) {
		super();
		this.id = id;
		this.status = status;
		this.formulationtypeid = formulationtypeid;
		this.routeOfAdministrationId = routeOfAdministrationId;
		this.items = items;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getFormulationtypeid() {
		return formulationtypeid;
	}

	public void setFormulationtypeid(Integer formulationtypeid) {
		this.formulationtypeid = formulationtypeid;
	}

	public Integer getRouteOfAdministrationId() {
		return routeOfAdministrationId;
	}

	public void setRouteOfAdministrationId(Integer routeOfAdministrationId) {
		this.routeOfAdministrationId = routeOfAdministrationId;
	}

	public List<FormulationRoaResponse> getItems() {
		return items;
	}

	public void setItems(List<FormulationRoaResponse> items) {
		this.items = items;
	}
	

	public void addItems(FormulationRoaResponse item) {
		if (this.items == null)
			this.items = new ArrayList<FormulationRoaResponse>();

		this.items.add(item);
	}
}
