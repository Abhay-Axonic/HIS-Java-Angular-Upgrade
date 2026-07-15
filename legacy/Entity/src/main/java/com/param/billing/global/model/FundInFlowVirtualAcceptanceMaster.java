package com.param.billing.global.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "m_fund_inflow_virtualacceptance_details", schema = "billing")
@SequenceGenerator(name = "m_fund_inflow_virtualacceptance_details_seq", sequenceName = "billing.m_fund_inflow_virtualacceptance_details_seq", allocationSize = 1)

public class FundInFlowVirtualAcceptanceMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_fund_inflow_virtualacceptance_details_seq")
	@Column(name = "fund_inflow_virtualacceptance_details_id")
	private Integer fundInflowVirtualacceptanceDetailsId;
	
	@Column(name = "fund_inflow_id")
	private Integer fundInflowId;
	
	@Column(name = "fundsanctionrequest_approval_details_id")
	private Integer fundsanctionrequestApprovalDetailsId;
	
	@Column(name = "fund_approval_amount")
	private Double fundApprovalAmount;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="virtual_acceptance_paid_amount")
	private Double virtualAcceptancePaidAmmount;
	
	
	public Integer getFundInflowVirtualacceptanceDetailsId() {
		return fundInflowVirtualacceptanceDetailsId;
	}

	public void setFundInflowVirtualacceptanceDetailsId(Integer fundInflowVirtualacceptanceDetailsId) {
		this.fundInflowVirtualacceptanceDetailsId = fundInflowVirtualacceptanceDetailsId;
	}

	public Integer getFundInflowId() {
		return fundInflowId;
	}

	public void setFundInflowId(Integer fundInflowId) {
		this.fundInflowId = fundInflowId;
	}

	public Integer getFundsanctionrequestApprovalDetailsId() {
		return fundsanctionrequestApprovalDetailsId;
	}

	public void setFundsanctionrequestApprovalDetailsId(Integer fundsanctionrequestApprovalDetailsId) {
		this.fundsanctionrequestApprovalDetailsId = fundsanctionrequestApprovalDetailsId;
	}

	public Double getFundApprovalAmount() {
		return fundApprovalAmount;
	}

	public void setFundApprovalAmount(Double fundApprovalAmount) {
		this.fundApprovalAmount = fundApprovalAmount;
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

	public Double getVirtualAcceptancePaidAmmount() {
		return virtualAcceptancePaidAmmount;
	}

	public void setVirtualAcceptancePaidAmmount(Double virtualAcceptancePaidAmmount) {
		this.virtualAcceptancePaidAmmount = virtualAcceptancePaidAmmount;
	}
	
	
	
	
	


	
	
	


	
}