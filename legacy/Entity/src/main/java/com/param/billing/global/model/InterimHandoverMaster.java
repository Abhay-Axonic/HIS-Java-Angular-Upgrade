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
	
	@NamedQuery(name="GET_INTERIM_HANDOVER_MASTER_LIST",
		query="SELECT interimHandoverMaster.id AS id, "
					+"interimHandoverMaster.ichNumber AS ichNumber, "
					+"interimHandoverMaster.statusId AS statusId, "
					+"to_char(interimHandoverMaster.createdDate,'DD-MM-YYYY') AS createdDate, "
					+"interimHandoverMaster.cashHandoverAmount AS cashHandoverAmount, "
					+"interimHandoverMaster.remarks AS remarks, "
					+"interimHandoverMaster.cashHandoverById AS cashHandoverById, "
					+"interimHandoverMaster.cashHandoverToId AS cashHandoverToId, "
					+"interimHandoverMaster.organizationId AS organizationId, "
					+"concat(coalesce(prefix.prefixCode),'.',' ',coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as handoverByUser, "
					+"concat(coalesce(prefx.prefixCode),'.',' ',coalesce(emp.firstName),' ',coalesce(emp.middleName),' ',coalesce(emp.lastName)) as handoverToUser, "
					+"interimHandoverMaster.unitId AS unitId, "
					+"interimHandoverMaster.status AS status, "
					+"interimHandoverMaster.modeOfTransaction AS modeOfTransaction  " 
					+"FROM InterimHandoverMaster interimHandoverMaster "
					+"INNER JOIN interimHandoverMaster.handoverByUser handoverByUser "
					+"LEFT JOIN handoverByUser.employeeMastersList employee "
					+"LEFT JOIN employee.prefixMaster prefix "
					+"INNER JOIN interimHandoverMaster.handoverToUser handoverToUser "
					+"LEFT JOIN handoverToUser.employeeMastersList emp "
					+"LEFT JOIN emp.prefixMaster prefx "
					+"WHERE interimHandoverMaster.organizationId= :organizationId "
					+"AND interimHandoverMaster.modeOfTransaction='Cash' "
					+"AND interimHandoverMaster.unitId= :unitId "
					+"AND interimHandoverMaster.cashHandoverById= :cashHandoverById "
					+"AND to_char(interimHandoverMaster.createdDate,'YYYY-MM-DD') BETWEEN :fromDate AND :toDate "
					),
	
	@NamedQuery(name="GET_ICHNUMBER_LIST",
	query="SELECT interimHandoverMaster.id AS id, "
				+" interimHandoverMaster.ichNumber AS ichNumber "
				+ " FROM InterimHandoverMaster interimHandoverMaster"
				+ " WHERE  interimHandoverMaster.organizationId= :organizationId "
				+ " AND interimHandoverMaster.unitId= :unitId "
				),
	
	@NamedQuery(name="UPDATE_INTERIMHANDOVER_BY_ID",
	query="UPDATE InterimHandoverMaster  "
				+" SET exportedToShiftClose = true "
				+ " WHERE  organizationId= :orgnizationId "
				+ " AND unitId= :unitId "
				+ " AND id= :interimHandoverId "
				)
	
})

@Entity
@Table(name = "t_interim_handover_details", schema = "public")
@SequenceGenerator(name = "t_intrim_cash_handover_details_id_seq", sequenceName = "public.t_intrim_cash_handover_details_id_seq", allocationSize = 1)
public class InterimHandoverMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_intrim_cash_handover_details_id_seq")
	@Column(name = "id")
	private Integer id;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="status")
	private String status;
	
	@Column(name="ich_number")
	private String ichNumber;
	
	@Column(name="cash_handover_by_id")
	private Integer cashHandoverById;
	
	@Column(name="cash_handover_to_id")
	private Integer cashHandoverToId;
	
	@Column(name="cash_handover_amount")
	private Double cashHandoverAmount;
	
	@Column(name="remarks")
	private String remarks;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="status_id")
	private Integer statusId;
	
	@Column(name="mode_of_transaction")
	private String modeOfTransaction;
	
	@Column(name="exported_to_shift_close")
	private boolean exportedToShiftClose;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cash_handover_by_id", insertable = false, updatable = false)
	private UserMaster handoverByUser;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cash_handover_to_id", insertable = false, updatable = false)
	private UserMaster handoverToUser;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIchNumber() {
		return ichNumber;
	}

	public void setIchNumber(String ichNumber) {
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

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getModeOfTransaction() {
		return modeOfTransaction;
	}

	public void setModeOfTransaction(String modeOfTransaction) {
		this.modeOfTransaction = modeOfTransaction;
	}

	public boolean isExportedToShiftClose() {
		return exportedToShiftClose;
	}

	public void setExportedToShiftClose(boolean exportedToShiftClose) {
		this.exportedToShiftClose = exportedToShiftClose;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	
	
}
