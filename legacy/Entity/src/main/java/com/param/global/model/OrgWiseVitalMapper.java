package com.param.global.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.opd.coversheet.model.VitalQualifireMapper;


@Entity
@Table(name = "t_org_wise_vital_mapper", schema = "emr")
@SequenceGenerator(name = "t_org_wise_vital_mapper_seq", sequenceName = "emr.t_org_wise_vital_mapper_seq", allocationSize = 1)
public class OrgWiseVitalMapper {
	@Id
	@Column(name = "org_wise_vital_mppr_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_org_wise_vital_mapper_seq")
	private Integer orgWiseVitalMpprId;
	
	@Column(name = "vital_id")
	private Integer vitalId;

	@Column(name = "vital_order_opd")
	private Integer vitalOrderOpd;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "createdDate")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
//	@Column(name = "unit_id")
//	private Integer unitId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name="is_required")
	private Character isRequired;

//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "orgWiseVitalMapper", cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<VitalMaster> listVitalMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vital_id", insertable = false, nullable = false, updatable = false)
	private VitalMaster vitalMaster;
	
	public Integer getOrgWiseVitalMpprId() {
		return orgWiseVitalMpprId;
	}

	public void setOrgWiseVitalMpprId(Integer orgWiseVitalMpprId) {
		this.orgWiseVitalMpprId = orgWiseVitalMpprId;
	}

	public Integer getVitalId() {
		return vitalId;
	}

	public void setVitalId(Integer vitalId) {
		this.vitalId = vitalId;
	}

	public Integer getVitalOrderOpd() {
		return vitalOrderOpd;
	}

	public void setVitalOrderOpd(Integer vitalOrderOpd) {
		this.vitalOrderOpd = vitalOrderOpd;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

//	public Integer getUnitId() {
//		return unitId;
//	}
//
//	public void setUnitId(Integer unitId) {
//		this.unitId = unitId;
//	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Character getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(Character isRequired) {
		this.isRequired = isRequired;
	}

	
	
}
