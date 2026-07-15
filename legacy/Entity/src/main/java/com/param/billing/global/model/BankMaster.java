package com.param.billing.global.model;

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
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.model.pharmacy.SalePaymentMode;


@NamedQueries({ 
	
		@NamedQuery(name = "GET_ACTIVE_BANK_MASTER", 
				query = "SELECT  bankMaster.bankId as bankId, "
						+ " bankMaster.branchId AS branchId,"
						+ " bankMaster.orgnisationId AS orgnisationId,"
						+ " bankMaster.bankName AS bankName,"
						+ " bankMaster.unitId AS unitId, "
						+ " bankMaster.status AS status, "
						+ " bankMaster.orgnisationId AS orgnisationId "
						+ " FROM BankMaster bankMaster"
						+ " WHERE bankMaster.status='A' "
						//+ " bankMaster.unitId=:unitId AND"
						+ " AND bankMaster.orgnisationId=:orgId"
						+" ORDER BY bankMaster.bankName ASC"),
	
		@NamedQuery(name = "GET_BANK_MASTER_BY_NAME",
				query = "SELECT bankMaster.bankId as bankId,"
						+ " bankMaster.bankName AS bankName,"
						+ " bankMaster.orgnisationId AS orgnisationId"
						+ " FROM BankMaster bankMaster"
						+ " WHERE LOWER(bankMaster.bankName)=:bankName AND"
						+ " bankMaster.orgnisationId=:orgId" // added by minesh
						),
				
		@NamedQuery(name = "GET_BANKMASTER_BY_NAME_NOT_ID",
				query = " SELECT "
						+ " bankMaster.bankId as bankId, "
						+ " bankMaster.bankName AS bankName "
						+ " FROM BankMaster bankMaster "
						+ " WHERE bankMaster.bankId != :bankId"
						+ " AND LOWER(bankMaster.bankName)=:bankName"
						+ " AND bankMaster.orgnisationId=:orgId" // added by minesh
						),
		
		@NamedQuery(name = "GET_BANK_MASTER_LIST_BY_ID",
				query = "SELECT "
						+ " bankMaster.bankId as bankId,"
						+ " bankMaster.branchId AS branchId,"
						+ " bankMaster.unitId AS unitId,"
						+ " bankMaster.bankCode as bankCode,"
						+ " bankMaster.bankName as bankName,"
						+ " bankMaster.bankAddress as bankAddress,"
						+ " bankMaster.orgnisationId AS orgnisationId "
						+ " FROM BankMaster bankMaster"
						+ " WHERE bankMaster.bankId =:bankId"
						+ " AND bankMaster.orgnisationId =:orgId"),
				
		@NamedQuery(name = "GET_BANK_MASTER_LIST",
				query = "SELECT bankMaster.bankId as bankId, "
						+ " bankMaster.branchId AS branchId,"
						+ " bankMaster.status AS status, "
						+ " bankMaster.orgnisationId AS orgnisationId,"
						+ " bankMaster.unitId AS unitId,"
						+ " bankMaster.bankName AS bankName,"
						+ " bankMaster.bankCode AS bankCode,"
						+ " bankMaster.bankAddress AS bankAddress,"
						+ " bankBranchMaster.branchDesc AS branchDesc"
						+ " FROM BankMaster bankMaster "
						+ " INNER JOIN bankMaster.bankBranchMaster bankBranchMaster"
						+ " WHERE bankMaster.orgnisationId =:orgId ORDER BY bankMaster.bankId asc"
						)
		})



@NamedNativeQueries({
	
	@NamedNativeQuery(name="GET_HOSPITAL_BANK",query= "select "
			+ "	ha.hospital_account_id as \"id\", "
			+ "	ha.hospital_account_desc as \"name\", "
			+ "	ha.is_default as \"isDefault\", "
			+ "	ha.organization_id as \"organizationId\", "
			+ "	ha.unit_id as \"unitId\" "
			+ "from "
			+ "	billing.m_hospital_accounts ha "
			+ "where "
			+ "	ha.organization_id = :orgId "
			+ "	and ha.unit_id = :unitId "
			+ "	AND ha.status = 'A' order by ha.is_default,ha.hospital_account_desc ASC")
})


@Entity
@Table(name = "m_bank_master", schema = "billing")
@SequenceGenerator(name = "bank_master_seq", sequenceName = "billing.bank_master_seq", allocationSize = 1)
public class BankMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_master_seq")
	@Column(name = "bank_id")
	private int bankId;
	
	@Column(name = "branch_id")
	private Integer branchId;
	
	@Column(name = "bank_code")
	private String bankCode;

	@Column(name = "bank_name")
	private String bankName;
	
	@Column(name = "bank_address")
	private String bankAddress;

	@Column(name = "orgnisation_id")
	private Integer orgnisationId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "status")
	private char status;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;
//
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy ="bankMaster")
//	private List<BankBranchMaster> listBankBranchMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "branch_id", insertable = false, updatable = false)
	BankBranchMaster bankBranchMaster;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bankMaster", cascade = CascadeType.ALL)
	List<SalePaymentMode> salePaymentModes;

	/*
	 * @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	 * "bankMaster") private List<DebitNoteDetails> listDebitNoteDetails;
	 */

	public List<SalePaymentMode> getSalePaymentModes() {
		return salePaymentModes;
	}

	public void setSalePaymentModes(List<SalePaymentMode> salePaymentModes) {
		this.salePaymentModes = salePaymentModes;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "bankMaster")
	private List<PaymentDepositDetails> listPaymentDepositDetails;

	public List<PaymentDepositDetails> getListPaymentDepositDetails() {
		return listPaymentDepositDetails;
	}

	public void setListPaymentDepositDetails(List<PaymentDepositDetails> listPaymentDepositDetails) {
		this.listPaymentDepositDetails = listPaymentDepositDetails;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public Integer getOrgnisationId() {
		return orgnisationId;
	}

	public void setOrgnisationId(Integer orgnisationId) {
		this.orgnisationId = orgnisationId;
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

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public BankBranchMaster getBankBranchMaster() {
		return bankBranchMaster;
	}

	public void setBankBranchMaster(BankBranchMaster bankBranchMaster) {
		this.bankBranchMaster = bankBranchMaster;
	}

	


}
