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

@NamedQueries({

		@NamedQuery(name = "GET_OPEN_HANDOVER_MASTER_LIST", query = "SELECT cashHandoverMaster.id AS id, "
				+ " cashHandoverMaster.cobNumber AS cobNumber, "
				+ "to_char(cashHandoverMaster.createdDate,'DD-MM-YYYY') AS createdDate, "
				+ "to_char(cashHandoverMaster.date,'DD-MM-YYYY') AS date, "
				+ "cashHandoverMaster.cashHandoverAmount AS cashHandoverAmount, "
				+ "cashHandoverMaster.remarks AS remarks, "
				+ "cashHandoverMaster.statusId AS statusId, "
				+ "cashHandoverMaster.cashHandoverById AS cashHandoverById, "
				+ "cashHandoverMaster.cashHandoverToId AS cashHandoverToId, "
				+ "cashHandoverMaster.organizationId AS organizationId, "
				+ "concat(coalesce(prefix.prefixCode),'.',' ',coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as handoverByUser, "
				+ "concat(coalesce(prefx.prefixCode),'.',' ',coalesce(emp.firstName),' ',coalesce(emp.middleName),' ',coalesce(emp.lastName)) as handoverToUser, "
				+ "cashHandoverMaster.unitId AS unitId, " + "cashHandoverMaster.status AS status "
				+ "FROM CashHandoverMaster cashHandoverMaster "
				+ "INNER JOIN cashHandoverMaster.handoverByUser handoverByUser "
				+ "LEFT JOIN handoverByUser.employeeMastersList employee " + "LEFT JOIN employee.prefixMaster prefix "
				+ "INNER JOIN cashHandoverMaster.handoverToUser handoverToUser "
				+ "LEFT JOIN handoverToUser.employeeMastersList emp " + "LEFT JOIN emp.prefixMaster prefx "
				+ "WHERE  cashHandoverMaster.organizationId= :organizationId "
				+ "AND cashHandoverMaster.unitId= :unitId "
				+ "AND cashHandoverMaster.cashHandoverById= :cashHandoverById "
				+ "AND to_char(cashHandoverMaster.createdDate,'YYYY-MM-DD') BETWEEN :fromDate AND :toDate "),

		@NamedQuery(name = "TO_CHECK_SHIFT_CLOSE_OR_NOT", query = "SELECT id as id " + "FROM CashHandoverMaster "
				+ " where shiftClose = false and " + " cashHandoverToId = :handoverTo and"
				+ " organizationId = :organizationId and " + " unitId =:unitId ")

})

@Entity
@Table(name = "t_cash_handover_details", schema = "public")
@SequenceGenerator(name = "cash_handover_details_seq", sequenceName = "public.cash_handover_details_seq", allocationSize = 1)

public class CashHandoverMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cash_handover_details_seq")
	@Column(name = "id")
	private Integer id;

	@Column(name = "date")
	private Date date;

	@Column(name = "cob_number")
	private String cobNumber;

	@Column(name = "ich_number")
	private Integer ichNumber;

	@Column(name = "cash_handover_by_id")
	private Integer cashHandoverById;

	@Column(name = "cash_handover_to_id")
	private Integer cashHandoverToId;

	@Column(name = "cash_handover_amount")
	private Double cashHandoverAmount;

	@Column(name = "denomination_id")
	private Integer denominationId;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "technician_id")
	private Integer technicianId;

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

	@Column(name = "shift_close")
	private boolean shiftClose;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cash_handover_by_id", insertable = false, updatable = false)
	private UserMaster handoverByUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cash_handover_to_id", insertable = false, updatable = false)
	private UserMaster handoverToUser;
	
	@Column(name = "status_id")
	private Integer statusId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCobNumber() {
		return cobNumber;
	}

	public void setCobNumber(String cobNumber) {
		this.cobNumber = cobNumber;
	}

	public Integer getIchNumber() {
		return ichNumber;
	}

	public void setIchNumber(Integer ichNumber) {
		this.ichNumber = ichNumber;
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

	public Integer getDenominationId() {
		return denominationId;
	}

	public void setDenominationId(Integer denominationId) {
		this.denominationId = denominationId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getTechnicianId() {
		return technicianId;
	}

	public void setTechnicianId(Integer technicianId) {
		this.technicianId = technicianId;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public boolean isShiftClose() {
		return shiftClose;
	}

	public void setShiftClose(boolean shiftClose) {
		this.shiftClose = shiftClose;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

}
