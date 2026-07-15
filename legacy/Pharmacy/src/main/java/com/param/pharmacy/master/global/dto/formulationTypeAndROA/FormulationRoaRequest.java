package com.param.pharmacy.master.global.dto.formulationTypeAndROA;

import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class FormulationRoaRequest extends CookieObjectDto {

	private Integer formulationtypeid;
	private List<Integer> routeOfAdministrationId;
	private Boolean status;

	public Integer getFormulationtypeid() {
		return formulationtypeid;
	}

	public void setFormulationtypeid(Integer formulationtypeid) {
		this.formulationtypeid = formulationtypeid;
	}

	public List<Integer> getRouteOfAdministrationId() {
		return routeOfAdministrationId;
	}

	public void setRouteOfAdministrationId(List<Integer> routeOfAdministrationId) {
		this.routeOfAdministrationId = routeOfAdministrationId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}
