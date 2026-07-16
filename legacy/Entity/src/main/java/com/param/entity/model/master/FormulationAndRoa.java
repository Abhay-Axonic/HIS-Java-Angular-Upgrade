package com.param.entity.model.master;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;

@Entity(name = "FormulationAndRoa")
@Table(name = "m_formulation_and_roa", schema = "public")

public class FormulationAndRoa extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "formulation_type_id")
	private FormulationType formulationType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roa_id")
	private RouteOfAdministration routeOfAdministration;

	public FormulationAndRoa() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public FormulationType getFormulationType() {
		return formulationType;
	}

	public void setFormulationType(FormulationType formulationType) {
		this.formulationType = formulationType;
	}

	public RouteOfAdministration getRouteOfAdministration() {
		return routeOfAdministration;
	}

	public void setRouteOfAdministration(RouteOfAdministration routeOfAdministration) {
		this.routeOfAdministration = routeOfAdministration;
	}

	public FormulationAndRoa( FormulationType formulationType, RouteOfAdministration routeOfAdministration) {
		super();
		this.formulationType = formulationType;
		this.routeOfAdministration = routeOfAdministration;
	}
	
}