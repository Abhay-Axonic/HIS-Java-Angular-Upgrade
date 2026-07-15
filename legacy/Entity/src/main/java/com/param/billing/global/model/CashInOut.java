package com.param.billing.global.model;

import java.util.Date;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.UserMaster;
import com.param.global.model.HospitalAccountMaster;


@NamedQueries({

		@NamedQuery(name = "GET_CASH_IN_OUT_LIST", 
				query = "SELECT cashHandoverMaster.id AS id, "
				+ " cashHandoverMaster.cobNumber AS cobNumber, "
				+ "to_char(cashHandoverMaster.createdDate,'DD-MM-YYYY') AS createdDate, "
				+ "to_char(cashHandoverMaster.date,'DD-MM-YYYY') AS date, "
				+ "cashHandoverMaster.cashHandoverAmount AS cashHandoverAmount, "
				+ "cashHandoverMaster.remarks AS remarks, "
				+ "cashHandoverMaster.cashHandoverById AS cashHandoverById, "
				+ "cashHandoverMaster.cashHandoverToId AS cashHandoverToId, "
				+ "cashHandoverMaster.organizationId AS organizationId, "
				+ "concat(coalesce(prefix.prefixCode),'.',' ',coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as handoverByUser, "
				+ "concat(coalesce(prefx.prefixCode),'.',' ',coalesce(emp.firstName),' ',coalesce(emp.middleName),' ',coalesce(emp.lastName)) as handoverToUser, "
				+ "cashHandoverMaster.unitId AS unitId,"
				+ "cashHandoverMaster.cashTransactionTypeId as cashTransactionTypeId ," 
				+ "concat(coalesce(hostp.hospitalAccountDesc),' / ',coalesce(hostp.hospitalAccountCode)) as hospitalAccount, "
				+ "cashHandoverMaster.status AS status "
				+ "FROM CashInOut cashHandoverMaster "
				+ "INNER JOIN cashHandoverMaster.handoverByUser handoverByUser "
				+ "LEFT JOIN handoverByUser.employeeMastersList employee "
				+ "LEFT JOIN employee.prefixMaster prefix "
				+ "INNER JOIN cashHandoverMaster.handoverToUser handoverToUser "
				+ "LEFT JOIN handoverToUser.employeeMastersList emp " 
				+ "LEFT JOIN emp.prefixMaster prefx "
				+ "LEFT JOIN cashHandoverMaster.hospitalAccountMaster hostp "
				+ "WHERE  cashHandoverMaster.organizationId= :organizationId "
				+ "AND cashHandoverMaster.unitId= :unitId "
				+ "AND to_char(cashHandoverMaster.createdDate,'YYYY-MM-DD') BETWEEN :fromDate AND :toDate "),


})


@Entity
@Table(name = "t_cash_in_out", schema = "public")
@SequenceGenerator(name = "t_cash_in_out_seq", sequenceName = "public.t_cash_in_out_seq", allocationSize = 1)
public class CashInOut {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_cash_in_out_seq")
	@Column(name = "id")
	private Integer id;

	@Column(name = "cash_date")
	private Date date;

	@Column(name = "cob_number")
	private String cobNumber;

	@Column(name = "cash_handover_by_id")
	private Integer cashHandoverById;

	@Column(name = "cash_handover_to_id")
	private Integer cashHandoverToId;

	@Column(name = "cash_handover_amount")
	private Double cashHandoverAmount;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "status")
	private String status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cash_handover_by_id", insertable = false, updatable = false)
	private UserMaster handoverByUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cash_handover_to_id", insertable = false, updatable = false)
	private UserMaster handoverToUser;

	@Column(name = "cash_transaction_type_id")
	private Integer cashTransactionTypeId;
	
	@Column(name = "hospital_account_id")
	private Integer hospitalAccountId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hospital_account_id", insertable = false, updatable = false)
	private HospitalAccountMaster hospitalAccountMaster;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCobNumber() {
		return cobNumber;
	}

	public void setCobNumber(String cobNumber) {
		this.cobNumber = cobNumber;
	}

	public Integer getCashHandoverById() {
		return cashHandoverById;
	}

	public void setCashHandoverById(Integer cashHandoverById) {
		this.cashHandoverById = cashHandoverById;
	}

	public Integer getCashHandoverToId() {
		return cashHandoverToId;
	}

	public void setCashHandoverToId(Integer cashHandoverToId) {
		this.cashHandoverToId = cashHandoverToId;
	}

	public Double getCashHandoverAmount() {
		return cashHandoverAmount;
	}

	public void setCashHandoverAmount(Double cashHandoverAmount) {
		this.cashHandoverAmount = cashHandoverAmount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getCashTransactionTypeId() {
		return cashTransactionTypeId;
	}

	public void setCashTransactionTypeId(Integer cashTransactionTypeId) {
		this.cashTransactionTypeId = cashTransactionTypeId;
	}

	public Integer getHospitalAccountId() {
		return hospitalAccountId;
	}

	public void setHospitalAccountId(Integer hospitalAccountId) {
		this.hospitalAccountId = hospitalAccountId;
	}
}
