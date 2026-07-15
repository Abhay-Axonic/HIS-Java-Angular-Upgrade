package com.param.bloodBank.model;

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
	@NamedQuery(name="GET_BLOOD_BAG_LOAN_OUT_LIST",
	query="SELECT mstr.bloodBagLoanOutId as bloodBagLoanOutId, "
				+ " mstr.loanOutNumber as loanOutNumber, "
				+ " mstr.createdBy as createdBy, "
				+ " mstr.updatedBy as updatedBy, "
				+ " mstr.status as status, "
				+ " to_char(mstr.createdDate,'DD-MM-YYYY') as createdDate, "
				+ " to_char(mstr.updatedDate,'DD-MM-YYYY') as updatedDate, "
				+ " (case " 
				+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) " 
				+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) " 
				+ " else 'Admin' end) as userName "
				+ " FROM BloodBagLoanOut mstr "
				+ " LEFT JOIN mstr.userMaster um " 
				+ " LEFT JOIN um.employeeMastersList empmst " 
				+ " LEFT JOIN um.doctorMastersList  docmst "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.loanOutStatusId = 16 "),

})

@Entity
@Table(name = "t_blood_bag_loan_out", schema = "blood_bank")
@SequenceGenerator(name = "blood_bag_loan_out_seq", sequenceName = "blood_bank.blood_bag_loan_out_seq", allocationSize = 1)
public class BloodBagLoanOut {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blood_bag_loan_out_seq")
	@Column(name = "blood_bag_loan_out_id")
	private Integer bloodBagLoanOutId;
	
	@Column(name = "loan_out_number")
	private String loanOutNumber;
	
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
	
	@Column(name = "approved_by")
	private Integer approvedBy;
	
	@Column(name = "loan_out_status_id")
	private Integer loanOutStatusId;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="created_by", insertable=false, updatable=false)
	private UserMaster userMaster;
	
	public Integer getBloodBagLoanOutId() {
		return bloodBagLoanOutId;
	}

	public void setBloodBagLoanOutId(Integer bloodBagLoanOutId) {
		this.bloodBagLoanOutId = bloodBagLoanOutId;
	}

	public String getLoanOutNumber() {
		return loanOutNumber;
	}

	public void setLoanOutNumber(String loanOutNumber) {
		this.loanOutNumber = loanOutNumber;
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

	public Integer getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Integer approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Integer getLoanOutStatusId() {
		return loanOutStatusId;
	}

	public void setLoanOutStatusId(Integer loanOutStatusId) {
		this.loanOutStatusId = loanOutStatusId;
	}
	
	
	
}
