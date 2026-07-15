package com.param.billing.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(name = "GET_TRANSACTION_TYPES",
				query= "SELECT trn.transactionTypeId as id, trn.transactionTypeDesc as name, "
					+ " trn.orgId as organizationId "
					 + "FROM	TransactionTypeMaster trn "
					 + "WHERE 	trn.status = 'A' "
					 + "AND trn.orgId =:orgId "
					 //+ "AND trn.unitId =:unitId "
					 +" ORDER BY trn.transactionTypeDesc ASC "
					 ),
	
	@NamedQuery(name = "GET_TRANSACTION_TYPE_LIST", query = "SELECT transactionTypeMaster.transactionTypeId as transactionTypeId, "
			+ "transactionTypeMaster.transactionTypeDesc as transactionTypeDesc, " + "transactionTypeMaster.transactionTypeName as transactionTypeName, "
			+ "transactionTypeMaster.status as status "
			+ "FROM TransactionTypeMaster as transactionTypeMaster "
					+ "WHERE transactionTypeMaster.orgId=:orgId "
					+ "ORDER BY transactionTypeMaster.transactionTypeId DESC"),
	
	@NamedQuery(name = "GET_TRANSACTION_TYPE_LIST_BY_NAME", query = "SELECT transactionTypeMaster.transactionTypeId as transactionTypeId, transactionTypeMaster.transactionTypeName as transactionTypeName, "
			+ "transactionTypeMaster.transactionTypeDesc as transactionTypeDesc, "
			+ "transactionTypeMaster.status as status "
			+ "FROM TransactionTypeMaster as transactionTypeMaster "
			+ "WHERE (LOWER(transactionTypeMaster.transactionTypeName)=:transactionTypeName OR transactionTypeMaster.transactionTypeName=:transactionTypeName) "
			+ "AND transactionTypeMaster.orgId=:orgId "),
	
	@NamedQuery(name = "GET_TRANSACTION_TYPE_LIST_BY_NAME_NOT_ID", query = "SELECT transactionTypeMaster.transactionTypeId as transactionTypeId, "
			+ "transactionTypeMaster.transactionTypeDesc as transactionTypeDesc, " + "transactionTypeMaster.transactionTypeName as transactionTypeName, "
			+ "transactionTypeMaster.status as status "
			+ "FROM TransactionTypeMaster as transactionTypeMaster "
			+ "WHERE (LOWER(transactionTypeMaster.transactionTypeName)=:transactionTypeName OR transactionTypeMaster.transactionTypeName=:transactionTypeName) "
			+ "AND transactionTypeMaster.transactionTypeId!=:transactionTypeId "
			+ "AND transactionTypeMaster.orgId=:orgId ")
	
	
	
	
	
})
@Entity
@Table(name = "m_transaction_type_master" , schema = "billing")
@SequenceGenerator(name = "m_transaction_type_seq" , sequenceName = "billing.m_transaction_type_seq" , allocationSize = 1)
public class TransactionTypeMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "m_transaction_type_seq")
	@Column(name = "transaction_type_id")
	private int transactionTypeId;
	
	@Column(name = "transaction_type_name")
	private String transactionTypeName;
	
	@Column(name = "transaction_type_desc")
	private String transactionTypeDesc;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;

	public int getTransactionTypeId() {
		return transactionTypeId;
	}

	public void setTransactionTypeId(int transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}

	public String getTransactionTypeName() {
		return transactionTypeName;
	}

	public void setTransactionTypeName(String transactionTypeName) {
		this.transactionTypeName = transactionTypeName;
	}

	public String getTransactionTypeDesc() {
		return transactionTypeDesc;
	}

	public void setTransactionTypeDesc(String transactionTypeDesc) {
		this.transactionTypeDesc = transactionTypeDesc;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'I');
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
}
