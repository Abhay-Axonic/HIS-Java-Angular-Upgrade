package com.param.service.global.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_contract_group_details" , schema = "service")
@SequenceGenerator(name = "t_contract_group_details_seq" , sequenceName = "service.t_contract_group_details_seq" , allocationSize = 1)
public class TContractGroupDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_contract_group_details_seq")
	@Column(name = "contract_group_detail_id")
	private int contractGroupDetailId;
	
	@Column(name = "contract_id")
	private Integer contractId;
	
	@Column(name = "department_id")
	private Integer departmentId;
	
	@Column(name = "sub_department_id")
	private Integer subDepartmentId;
	
	@Column(name = "variance_up_down")
	private Integer varianceUpDown;
	
	@Column(name = "variance_percentage")
	private BigDecimal variancePercentage;
	
	@Column(name = "co_share_percentage")
	private BigDecimal coSharePercentage;
	
	@Column(name = "organization_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name="status")
	private Character status;

	public int getContractGroupDetailId() {
		return contractGroupDetailId;
	}

	public void setContractGroupDetailId(int contractGroupDetailId) {
		this.contractGroupDetailId = contractGroupDetailId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getSubDepartmentId() {
		return subDepartmentId;
	}

	public void setSubDepartmentId(Integer subDepartmentId) {
		this.subDepartmentId = subDepartmentId;
	}

	public Integer getVarianceUpDown() {
		return varianceUpDown;
	}

	public void setVarianceUpDown(Integer varianceUpDown) {
		this.varianceUpDown = varianceUpDown;
	}

	public BigDecimal getVariancePercentage() {
		return variancePercentage;
	}

	public void setVariancePercentage(BigDecimal variancePercentage) {
		this.variancePercentage = variancePercentage;
	}

	public BigDecimal getCoSharePercentage() {
		return coSharePercentage;
	}

	public void setCoSharePercentage(BigDecimal coSharePercentage) {
		this.coSharePercentage = coSharePercentage;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}
	
}
