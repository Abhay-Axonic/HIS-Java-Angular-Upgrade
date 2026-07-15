package com.param.billing.global.transaction.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.DateConverter;

@Entity
@Table(name = "t_company_receipt_service_details", schema = "billing")
@SequenceGenerator(name = "t_company_receipt_service_details_seq", sequenceName = "billing.t_company_receipt_service_details_seq", allocationSize = 1)
public class CompanyReceiptServiceDetails {
	@Id
	@Column(name = "comp_rece_ser_detls_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_company_receipt_service_details_seq")
	private Integer compReceiptServiceDetlsId;

	@Column(name = "com_receipt_dtl_id")
	private Integer comReceiptDtlId;

	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "item_id")
	private Integer itemId;

	@Column(name = "received_amount")
	private Double receivedAmount;

	@Column(name = "tds_amount")
	private Double tdsAmount;
	
	@Column(name = "disallowed_amount")
	private Double disallowedAmount;
	
	@Column(name = "other_deduction")
	private Double otherDeduction;
	
	@Column(name = "org_unit_id")
	private Integer orgUnitId;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_date")
	@Convert(converter = DateConverter.class)
	private Long createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	@Convert(converter = DateConverter.class)
	private Long updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	public Double getOtherDeduction() {
		return otherDeduction;
	}

	public void setOtherDeduction(Double otherDeduction) {
		this.otherDeduction = otherDeduction;
	}

	public Double getTdsAmount() {
		return tdsAmount;
	}

	public void setTdsAmount(Double tdsAmount) {
		this.tdsAmount = tdsAmount;
	}

	public Integer getCompReceiptServiceDetlsId() {
		return compReceiptServiceDetlsId;
	}

	public void setCompReceiptServiceDetlsId(Integer compReceiptServiceDetlsId) {
		this.compReceiptServiceDetlsId = compReceiptServiceDetlsId;
	}

	public Integer getComReceiptDtlId() {
		return comReceiptDtlId;
	}

	public void setComReceiptDtlId(Integer comReceiptDtlId) {
		this.comReceiptDtlId = comReceiptDtlId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Double getReceivedAmount() {
		return receivedAmount;
	}

	public void setReceivedAmount(Double receivedAmount) {
		this.receivedAmount = receivedAmount;
	}

	public Double getDisallowedAmount() {
		return disallowedAmount;
	}

	public void setDisallowedAmount(Double disallowedAmount) {
		this.disallowedAmount = disallowedAmount;
	}

	

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	

}
