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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.global.model.BedsideLabInvestigationMaster;
import com.param.global.model.VaccinationMaster;
import com.param.opd.coversheet.model.InfusionAdditiveMaster;
import com.param.opd.coversheet.model.InfusionSolutionMaster;

@Entity
@Table(name = "m_strength_unit", schema = "public")
@NamedQuery(name = "StrengthUnit.findAll", query = "SELECT s FROM StrengthUnit s")
public class StrengthUnit extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(length = 50)
	private String code;

	@Column(name = "unit", length = 100)
	private String units;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "strengthUnit", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<VaccinationMaster> vaccinationMasterlist;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "strengthUnitMaster", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<InfusionSolutionMaster> ListInfusionSolutionMaster;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "strengthUnitMaster", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<InfusionAdditiveMaster> ListOfInfusionAdditiveMaster;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "strengthUnitMaster", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<BedsideLabInvestigationMaster> ListOfBedsideLabInvestigation;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "strengthUnitPresDTL", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<PrescriptionDetail> listPrescriptionDetail;
	
	public StrengthUnit() {
	}

	public StrengthUnit(Integer id) {
		super();
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public List<PrescriptionDetail> getListPrescriptionDetail() {
		return listPrescriptionDetail;
	}

	public void setListPrescriptionDetail(List<PrescriptionDetail> listPrescriptionDetail) {
		this.listPrescriptionDetail = listPrescriptionDetail;
	}

}