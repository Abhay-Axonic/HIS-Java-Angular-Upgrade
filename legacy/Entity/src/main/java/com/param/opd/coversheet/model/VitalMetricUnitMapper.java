package com.param.opd.coversheet.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQuery(name="GET_VITAL_METRIC_UNIT_LIST",
query="SELECT vitalMetricUnitMapper.vitalMetricUnitId AS vitalMetricUnitId, "
			+ " vitalMetricUnitMapper.vitalMetricUnitCode AS vitalMetricUnitCode, "
			+ " vitalMetricUnitMapper.vitalMetricUnitDesc AS vitalMetricUnitDesc "
			+ " FROM VitalMetricUnitMapper vitalMetricUnitMapper "
			+ " WHERE vitalMetricUnitMapper.vitalId= :vitalId "
			+ " AND vitalMetricUnitMapper.organizationId = :organizationId "
			)

@Entity
@Table(name = "t_vital_metric_unit_mapper", schema = "emr")
@SequenceGenerator(name = "t_vital_metric_unit_mapper_seq", sequenceName = "emr.t_vital_metric_unit_mapper_seq", allocationSize = 1)

public class VitalMetricUnitMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_vital_metric_unit_mapper_seq")
	
	@Column(name = "vital_metric_unit_id")
	private Integer vitalMetricUnitId;
	
	@Column(name = "vital_id")
	private Integer vitalId;
	
	@Column(name = "vital_metric_unit_code")
	private String vitalMetricUnitCode;
	
	@Column(name = "vital_metric_unit_desc")
	private String vitalMetricUnitDesc;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;

	public Integer getVitalMetricUnitId() {
		return vitalMetricUnitId;
	}

	public void setVitalMetricUnitId(Integer vitalMetricUnitId) {
		this.vitalMetricUnitId = vitalMetricUnitId;
	}

	public Integer getVitalId() {
		return vitalId;
	}

	public void setVitalId(Integer vitalId) {
		this.vitalId = vitalId;
	}

	public String getVitalMetricUnitCode() {
		return vitalMetricUnitCode;
	}

	public void setVitalMetricUnitCode(String vitalMetricUnitCode) {
		this.vitalMetricUnitCode = vitalMetricUnitCode;
	}

	public String getVitalMetricUnitDesc() {
		return vitalMetricUnitDesc;
	}

	public void setVitalMetricUnitDesc(String vitalMetricUnitDesc) {
		this.vitalMetricUnitDesc = vitalMetricUnitDesc;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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
	
	
}
