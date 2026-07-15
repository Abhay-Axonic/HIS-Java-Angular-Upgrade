package com.param.pharmacy.master.global.dto.formulationTypeAndROA;

import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class FormulationTypeAndROARequest extends CookieObjectDto{

	protected Integer id;

	protected Integer formulationtypeid;

	protected Integer routeOfAdministrationId;
	
	protected List<Integer> itemsList;
	
	protected Boolean status;
	
	
	public FormulationTypeAndROARequest() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public List<Integer> getItemsList() {
		return itemsList;
	}

	public void setItemsList(List<Integer> itemsList) {
		this.itemsList = itemsList;
	}

}
