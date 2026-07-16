package com.param.entity.model.master;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;

import com.param.global.model.VaccinationMaster;

import com.param.entity.model.opd.MedicationHistory;

@Entity
@Table(name = "m_route_of_administration", schema="public")

public class RouteOfAdministration extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(nullable = false, length = 100)
	private String code;

	@Column(nullable = false, length = 100)
	private String description;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "routeOfAdministration", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<VaccinationMaster> vaccinationMasterList;

	public List<VaccinationMaster> getVaccinationMasterList() {
		return vaccinationMasterList;
	}

	public void setVaccinationMasterList(List<VaccinationMaster> vaccinationMasterList) {
		this.vaccinationMasterList = vaccinationMasterList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "routeOfAdministration")
	private List<MedicationHistory> routeOfAdministrationList;


	public List<MedicationHistory> getRouteOfAdministrationList() {
		return routeOfAdministrationList;
	}

	public void setRouteOfAdministrationList(List<MedicationHistory> routeOfAdministrationList) {
		this.routeOfAdministrationList = routeOfAdministrationList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public RouteOfAdministration(Integer id) {
		super();
		this.id = id;
	}

	public RouteOfAdministration() {
		super();	
	}

}