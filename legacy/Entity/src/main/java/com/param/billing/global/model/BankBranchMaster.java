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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({ 
		@NamedQuery(name = "GET_BANK_BRANCH_MASTER_BY_NAME",
				query = "SELECT  "
						+ " bankBranchMaster.branchId as branchId, "
						+ " bankBranchMaster.branchDesc AS branchDesc, "
						+ " bankBranchMaster.orgnisationId AS orgnisationId "
						+ " FROM BankBranchMaster bankBranchMaster  "
						+ " WHERE (LOWER(bankBranchMaster.branchDesc)=:branchDesc OR bankBranchMaster.branchDesc=:branchDesc)  "//add bracket by minesh
						+ " AND bankBranchMaster.orgnisationId= :orgnisationId"
						),
		
		@NamedQuery(name = "GET_BANK_BRANCH_MASTER_BY_NAME_NOT_ID", 
				query = "SELECT "
						+ " bankBranchMaster.branchId as branchId, "
						+ " bankBranchMaster.branchDesc as branchDesc  "
						+ " FROM BankBranchMaster bankBranchMaster " 
						+ " WHERE bankBranchMaster.branchId !=:branchId "
						+ " AND (LOWER(bankBranchMaster.branchDesc)=:branchDesc OR bankBranchMaster.branchDesc=:branchDesc)"
						+ " AND bankBranchMaster.orgnisationId= :orgnisationId" //added by minesh
						),
		
		@NamedQuery(name = "GET_BANK_BRANCH_MASTER_BY_ID", 
				query = "SELECT "
						+ " bankBranchMaster.branchId as branchId,"
						+ " bankBranchMaster.branchDesc AS branchDesc,"
						+ " bankBranchMaster.branchCode as branchCode, "
						+ " bankBranchMaster.status AS status,"
						+ " bankBranchMaster.orgnisationId AS orgnisationId "
						+ " FROM BankBranchMaster bankBranchMaster "
						+ " WHERE bankBranchMaster.branchId=:branchId "
						+ " AND bankBranchMaster.orgnisationId = :orgnisationId "
						),
		
		@NamedQuery(name = "GET_BANK_BRANCH_MASTER_LIST",
				query = "SELECT "
						+ " bankBranchMaster.branchId as branchId,"
						+ " bankBranchMaster.branchDesc as branchDesc,"
						+ " bankBranchMaster.branchCode as branchCode,"
						+ " bankBranchMaster.status AS status,"
						+ " bankBranchMaster.branchAddress as branchAddress,"
						+ " bankBranchMaster.unitId as unitId,"
						+ " bankBranchMaster.orgnisationId AS orgnisationId "
						+ " FROM BankBranchMaster bankBranchMaster "
						+ " WHERE bankBranchMaster.orgnisationId= :orgnisationId"
						),
	
		@NamedQuery(name = "GET_ACTIVE_BANK_BRANCH_MASTER_LIST",
				query = "SELECT "
						+ " bankBranchMaster.branchId as branchId,"
						+ " bankBranchMaster.branchDesc as branchDesc,"
						+ " bankBranchMaster.branchCode as branchCode,"
						+ " bankBranchMaster.orgnisationId AS orgnisationId,"
						+ " bankBranchMaster.bankId AS bankId,"
						+ " bankBranchMaster.status AS status"
						+ " FROM BankBranchMaster bankBranchMaster"
						+ " WHERE bankBranchMaster.orgnisationId= :orgnisationId AND bankBranchMaster.status='A' " 
						),
		})

@Entity
@Table(name = "m_bank_branch_master", schema = "billing")
@SequenceGenerator(name = "bank_branch_master_seq", sequenceName = "billing.bank_branch_master_seq", allocationSize = 1)
public class BankBranchMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_branch_master_seq")

	@Column(name = "branch_id")
	private Integer branchId;
	
	@Column(name = "bank_id")
	private Integer bankId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "orgnisation_id")
	private Integer orgnisationId;

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

	@Column(name = "branch_code")
	private String branchCode;

	@Column(name = "branch_address")
	private String branchAddress;

	@Column(name = "branch_desc")
	private String branchDesc;
	
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "bank_id", insertable = false, updatable = false)
//	BankMaster bankMaster;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy ="bankBranchMaster")
	private List<BankMaster> listBankMaster;
	
	
	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchAddress() {
		return branchAddress;
	}

	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}

	public String getBranchDesc() {
		return branchDesc;
	}

	public void setBranchDesc(String branchDesc) {
		this.branchDesc = branchDesc;
	}

	public List<BankMaster> getListBankMaster() {
		return listBankMaster;
	}

	public void setListBankMaster(List<BankMaster> listBankMaster) {
		this.listBankMaster = listBankMaster;
	}

	
	
	
	
	
	
	
}
