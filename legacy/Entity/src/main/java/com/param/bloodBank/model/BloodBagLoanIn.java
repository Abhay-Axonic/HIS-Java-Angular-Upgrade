package com.param.bloodBank.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(name="SEARCH_LOAN_IN_DETAILS",
			query="SELECT  mstr.loanInId as loanInId,"
						+ " mstr.loanInNumber as loanInNumber, "
						+ " mstr.unitId as unitId, "
						+ " to_char(mstr.createdDate,'DD-MM-YYYY') as createdDate, "
						+ " mstr.status as status, "
						+ " mstr.organizationId as organizationId "
						+ " FROM BloodBagLoanIn mstr "
						+ " WHERE mstr.organizationId= :organizationId "
						+ " AND mstr.unitId= :unitId "
						),
	
	@NamedQuery(name="SEARCH_LOAN_IN_DETAILS_FOR_APPROVAL",
						query="SELECT distinct mstr.loanInId as loanInId,"
								+ " mstr.loanInNumber as loanInNumber, "
								+ " mstr.unitId as unitId, "
								+ " to_char(mstr.createdDate,'DD-MM-YYYY') as createdDate, "
								+ " mstr.status as status, "
								+ " mstr.organizationId as organizationId "
								+ " FROM BloodBagLoanIn mstr "
								+ " INNER JOIN mstr.bloodBankStock  dtls "
								+ " WHERE mstr.organizationId= :organizationId "
								+ " AND mstr.unitId= :unitId "
								+ " AND dtls.stockStatusId = 4 "
								)
	
		})

@Entity
@Table(name="t_loan_in", schema="blood_bank")
@SequenceGenerator(name="loan_in_seq",sequenceName="blood_bank.loan_in_seq", allocationSize= 1)
public class BloodBagLoanIn {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_in_seq")
	@Column(name = "loan_in_id")
	private Integer loanInId;
	
	@Column(name = "loan_in_number")
	private String loanInNumber;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "bloodBagLoanIn", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BloodBankStock> bloodBankStock;

	public Integer getLoanInId() {
		return loanInId;
	}

	public void setLoanInId(Integer loanInId) {
		this.loanInId = loanInId;
	}

	public String getLoanInNumber() {
		return loanInNumber;
	}

	public void setLoanInNumber(String loanInNumber) {
		this.loanInNumber = loanInNumber;
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
	
	

}
