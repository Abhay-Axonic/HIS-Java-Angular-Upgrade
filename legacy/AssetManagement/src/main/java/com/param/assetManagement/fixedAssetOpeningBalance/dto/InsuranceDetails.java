package com.param.assetManagement.fixedAssetOpeningBalance.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class InsuranceDetails {

	protected Integer id;
	
	protected String insuranceCompany;
	
	protected Integer insuranceCompanyId;
	
	protected String policyNumber;
	
	protected Date policyFromDate;
	
	protected Date policyToDate;
	
	protected Double amountInsured;
	
	protected String remark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getInsuranceCompany() {
		return insuranceCompany;
	}

	public void setInsuranceCompany(String insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public Date getPolicyFromDate() {
		return policyFromDate;
	}

	public void setPolicyFromDate(Date policyFromDate) {
		this.policyFromDate = policyFromDate;
	}

	public Date getPolicyToDate() {
		return policyToDate;
	}

	public void setPolicyToDate(Date policyToDate) {
		this.policyToDate = policyToDate;
	}

	public Double getAmountInsured() {
		return amountInsured;
	}

	public void setAmountInsured(Double amountInsured) {
		this.amountInsured = amountInsured;
	}

	public Integer getInsuranceCompanyId() {
		return insuranceCompanyId;
	}

	public void setInsuranceCompanyId(Integer insuranceCompanyId) {
		this.insuranceCompanyId = insuranceCompanyId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public InsuranceDetails() {
		super();
	}

	public InsuranceDetails(Integer id, String insuranceCompany, Integer insuranceCompanyId, String policyNumber, Date policyFromDate,
			Date policyToDate, Double amountInsured, String remark) {
		super();
		this.id = id;
		this.insuranceCompany = insuranceCompany;
		this.insuranceCompanyId = insuranceCompanyId;
		this.policyNumber = policyNumber;
		this.policyFromDate = policyFromDate;
		this.policyToDate = policyToDate;
		this.amountInsured = amountInsured;
		this.remark = remark;
	}
	
}