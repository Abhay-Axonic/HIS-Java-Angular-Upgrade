package com.param.service.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_package_version ", schema = "service")
@SequenceGenerator(name = "t_seq_package_version", sequenceName = "service.t_seq_package_version", allocationSize = 1)
public class PackageVersionMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_package_version")
	@Column(name = "id")
	private int id;

	@Column(name = "parent_package_id")
	private Integer parentPackageId;
	
	@Column(name = "status")
	private char status;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "version_id")
	private Integer versionId;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "unit_id")
	private Integer unitId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getParentPackageId() {
		return parentPackageId;
	}

	public void setParentPackageId(Integer parentPackageId) {
		this.parentPackageId = parentPackageId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getVersionId() {
		return versionId;
	}

	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}
	
	
	
	
}
