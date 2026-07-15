package com.param.service.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.billing.global.transaction.model.InsuranceMaster;
import com.param.global.model.CompanyMaster;

@NamedQueries({
	@NamedQuery(name="GET_INSURANCE_TPA_MAPPER_LIST",
			query="SELECT insurTpaMpper.insuranceTpaMapperId AS insuranceTpaMapperId,"
					+ " insMater.insuranceCode AS insuranceCode,"
					+ " insMater.insuranceDesc AS insuranceDesc,"
					+ " cmnyMster.companyDesc AS companyDesc,"
					+ " insurTpaMpper.status AS status "
					+ " FROM InsuranceTPAMapper insurTpaMpper "
					+ " INNER JOIN insurTpaMpper.companyMaster cmnyMster "
					+ " INNER JOIN insurTpaMpper.insuranceMaster insMater "
					+ " WHERE insurTpaMpper.unitId=:unitId "
					+ " AND insurTpaMpper.organizationId=:orgId AND  insurTpaMpper.status='A'"
					+ " order by insurTpaMpper.insuranceTpaMapperId desc "),


})
@NamedNativeQueries({
@NamedNativeQuery(name="GET_INSURANCE_TPA_MAPPER_LIST_BY_ID",
			query="SELECT insurTpaMpper.insurance_tpa_mapper_id AS \"insuranceTpaMapperId\","
					+ " cmnyMster.company_desc AS \"companyDesc\",insurMster.insurance_desc AS \"insuranceDesc\", "
					+ " insurTpaMpper.status AS \"status\",insurTpaMpper.company_id AS \"companyId\", insurTpaMpper.insurance_id AS \"insuranceId\","
					+ " cmnyMster.company_code AS \"companyCode\",insurMster.insurance_code AS \"insuranceCode\"  "
					+ " FROM service.t_insurance_tpa_mapper insurTpaMpper "
					+ " INNER JOIN  public.t_company_master cmnyMster ON insurTpaMpper.company_id=cmnyMster.company_id "
					+ " INNER JOIN adt.t_insurance insurMster on insurMster.insurance_id=insurTpaMpper.insurance_id "
					+ " WHERE insurTpaMpper.insurance_tpa_mapper_id=:insuranceTpaMapperId "),
			@NamedNativeQuery(name="GET_INSURANCE_LIST_NOT_MAPPED_TO_MAPPER",
			query="SELECT insuranceMstr.insurance_desc AS \"insuranceDesc\","
					+ " insuranceMstr.insurance_code AS \"insuranceCode\",insuranceMstr.insurance_id AS \"insuranceId\", "
					+ " insuranceMstr.status AS \"status\" "
				    + " FROM adt.t_insurance insuranceMstr "
					+ " WHERE insuranceMstr.organization_id=:orgId "
					+ " AND insuranceMstr.unit_id=:unitId "
					+ " AND insuranceMstr.status='A' ")
})
//mpr.company_id=:companyId replace status
/*select * 
from adt.t_insurance  ins
where ins.insurance_id not in(select insurance_id from service.t_insurance_tpa_mapper mpr where mpr.company_id=1)*/
@Entity
@Table(name="t_insurance_tpa_mapper",schema="service")
@SequenceGenerator(name = "insurance_tpa_mapper_seq", sequenceName = "service.insurance_tpa_mapper_seq", allocationSize = 1)
public class InsuranceTPAMapper {
	@Id
	@Column(name="insurance_tpa_mapper_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="insurance_tpa_mapper_seq")
	private Integer insuranceTpaMapperId; 
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="company_id")
	private Integer companyId;
	
	@Column(name="insurance_id")
	private Integer insuranceId;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "company_id", insertable = false, updatable = false)
	private CompanyMaster companyMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="insurance_id",insertable=false,updatable=false)
	private InsuranceMaster insuranceMaster;

	public Integer getInsuranceTpaMapperId() {
		return insuranceTpaMapperId;
	}

	public void setInsuranceTpaMapperId(Integer insuranceTpaMapperId) {
		this.insuranceTpaMapperId = insuranceTpaMapperId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(Integer insuranceId) {
		this.insuranceId = insuranceId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	 

}
