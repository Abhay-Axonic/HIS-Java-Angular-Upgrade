package com.param.entity.model.master;

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

@Entity(name = "CombinationGeneric")
@Table(name = "m_combination_generic", schema = "public")
public class CombinationGeneric extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "strength_unit")
	protected Double strengthUnit;

	@Column(name = "strength_unit_id")
	protected Integer strengthUnitId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "generic_id")
	private Generic generic;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sub_generic_id")
	private Generic subGeneric;
	
	
	public CombinationGeneric() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Generic getGeneric() {
		return generic;
	}

	public void setGeneric(Generic generic) {
		this.generic = generic;
	}

	public Generic getSubGeneric() {
		return subGeneric;
	}

	public void setSubGeneric(Generic subGeneric) {
		this.subGeneric = subGeneric;
	}

	public CombinationGeneric(Generic subGeneric, Double strengthUnit, Integer strengthUnitId) {
		super();
		this.subGeneric = subGeneric;
		this.strengthUnit = strengthUnit;
		this.strengthUnitId = strengthUnitId;
	}
	public void updateCombinationGeneric( Double strengthUnit, Integer strengthUnitId) {
		this.strengthUnit = strengthUnit;
		this.strengthUnitId = strengthUnitId;
	}

	public CombinationGeneric(Generic subGeneric, Double strengthUnit, Integer strengthUnitId, Generic generic) {
		super();
		this.subGeneric = subGeneric;
		this.strengthUnit = strengthUnit;
		this.strengthUnitId = strengthUnitId;
		this.generic = generic;
	}

	public Double getStrengthUnit() {
		return strengthUnit;
	}

	public void setStrengthUnit(Double strengthUnit) {
		this.strengthUnit = strengthUnit;
	}

	public Integer getStrengthUnitId() {
		return strengthUnitId;
	}

	public void setStrengthUnitId(Integer strengthUnitId) {
		this.strengthUnitId = strengthUnitId;
	}
}
