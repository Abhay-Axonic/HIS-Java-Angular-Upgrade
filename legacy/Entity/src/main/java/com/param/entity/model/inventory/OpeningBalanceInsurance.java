package com.param.entity.model.inventory;

import java.util.Date;

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
import com.param.entity.model.master.Company;

@Entity(name = "OpeningBalanceInsurance")
@Table(name = "t_opening_balance_insurance", schema = "inventory")
public class OpeningBalanceInsurance extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "opening_balance_detail_id")
	private OpeningBalanceDetail openingBalanceDetail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "insurance_company_id")
	private Company insuranceCompany;
	
	@Column(name = "policy_number", length = 50)
	private String policyNumber;
	
	@Column(name = "policy_from_date")
	private Date policyFromDate;
	
	@Column(name = "policy_to_date")
	private Date policyToDate;
	
	@Column(name = "amount_insured")
	private Double amountInsured;
	
	@Column(name = "remark")
	private String remark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public OpeningBalanceDetail getOpeningBalanceDetail() {
		return openingBalanceDetail;
	}

	public void setOpeningBalanceDetail(OpeningBalanceDetail openingBalanceDetail) {
		this.openingBalanceDetail = openingBalanceDetail;
	}

	public Company getInsuranceCompany() {
		return insuranceCompany;
	}

	public void setInsuranceCompany(Company insuranceCompany) {
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public OpeningBalanceInsurance() {
		super();
	}

	public OpeningBalanceInsurance(Integer id, OpeningBalanceDetail openingBalanceDetail, Company insuranceCompany,
			String policyNumber, Date policyFromDate, Date policyToDate, Double amountInsured, String remark) {
		super();
		this.id = id;
		this.openingBalanceDetail = openingBalanceDetail;
		this.insuranceCompany = insuranceCompany;
		this.policyNumber = policyNumber;
		this.policyFromDate = policyFromDate;
		this.policyToDate = policyToDate;
		this.amountInsured = amountInsured;
		this.remark = remark;
	}

	public OpeningBalanceInsurance(Company insuranceCompany, String policyNumber, Date policyFromDate,
			Date policyToDate, Double amountInsured, String remark) {
		super();
		this.insuranceCompany = insuranceCompany;
		this.policyNumber = policyNumber;
		this.policyFromDate = policyFromDate;
		this.policyToDate = policyToDate;
		this.amountInsured = amountInsured;
		this.remark = remark;
	}
	
	public void updateInsurance(Company insuranceCompany, String policyNumber, Date policyFromDate,
			Date policyToDate, Double amountInsured, String remark) {
		this.insuranceCompany = insuranceCompany;
		this.policyNumber = policyNumber;
		this.policyFromDate = policyFromDate;
		this.policyToDate = policyToDate;
		this.amountInsured = amountInsured;
		this.remark = remark;
	}

}