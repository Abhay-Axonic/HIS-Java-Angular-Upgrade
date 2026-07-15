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

		@NamedQuery(name = "CHECK_FOR_UNIQUE_SHIFTCLOSING_SUMMRY", query = "SELECT mstr.id as id "
				+ " FROM ShiftClosingDetailsMaster mstr" + " WHERE mstr.handoverBy= :handoverBy"
				+ " AND mstr.organizationId= :organizationId" + " AND mstr.unitId= :unitId"
				//+ " AND mstr.handoverTo= :handoverTo "
				+ " AND to_char(mstr.createdDate ,'dd-MM-yyyy') = :createdDate ") ,
			
			@NamedQuery(name="SEARCH_SHIFT_CLOSING_LIST_BY_MULTIPLE_CRITERIA",
				query="SELECT scm.closingNumber AS closingNumber, "
							+"to_char(scm.createdDate,'yyyy-MM-dd HH:mm:ss') as createdDate ,"
							+"scm.amount as amount, "
							+"scm.difference as difference , "
							+"scm.cashierName as cashierName ,"
							+"scm.handoverBy as handoverBy, "
							+"concat(coalesce(prefx.prefixCode),'.',' ',coalesce(emp.firstName),' ',coalesce(emp.middleName),' ',coalesce(emp.lastName)) as userName "
							+"FROM ShiftClosingDetailsMaster scm "
							+"INNER JOIN scm.handoverToUser handoverToUser "
							+"LEFT JOIN handoverToUser.employeeMastersList emp "
							+"LEFT JOIN emp.prefixMaster prefx "
							+"WHERE  scm.organizationId= :organizationId "
							+"AND scm.unitId= :unitId "
							+"AND scm.handoverBy= :handoverBy "
							+"AND to_char(scm.createdDate,'YYYY-MM-DD') BETWEEN :fromDate AND :toDate "
							)
		
})

@Entity
@Table(name = "m_shift_close_master", schema = "billing")
@SequenceGenerator(name = "m_shift_close_master_id_seq", sequenceName = "billing.m_shift_close_master_id_seq", allocationSize = 1)
public class ShiftClosingDetailsMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_shift_close_master_id_seq")
	@Column(name = "id")
	private Integer id;
	
	@Column(name="closing_number")
	private String closingNumber;
	
	@Column(name="difference")
	private Double difference;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="amount")
	private Double amount;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="handover_by")
	private Integer handoverBy;
	
	@Column(name="handover_to")
	private Integer handoverTo;
	
	@Column(name="cashier_name")
	private String cashierName;
	
	@Column(name="status")
	private String status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "handover_by", insertable = false, updatable = false)
	private UserMaster handoverByUser;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "handover_to", insertable = false, updatable = false)
	private UserMaster handoverToUser;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClosingNumber() {
		return closingNumber;
	}

	public void setClosingNumber(String closingNumber) {
		this.closingNumber = closingNumber;
	}

	public Double getDifference() {
		return difference;
	}

	public void setDifference(Double difference) {
		this.difference = difference;
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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
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

	public Integer getHandoverBy() {
		return handoverBy;
	}

	public void setHandoverBy(Integer handoverBy) {
		this.handoverBy = handoverBy;
	}

	public Integer getHandoverTo() {
		return handoverTo;
	}

	public void setHandoverTo(Integer handoverTo) {
		this.handoverTo = handoverTo;
	}

	public String getCashierName() {
		return cashierName;
	}

	public void setCashierName(String cashierName) {
		this.cashierName = cashierName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
