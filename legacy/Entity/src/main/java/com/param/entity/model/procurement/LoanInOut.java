package com.param.entity.model.procurement;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.User;

@Entity(name = "LoanInOut")
@Table(name = "t_loan_in_out", schema = "procurement")
public class LoanInOut extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "loan_in_out_no")
	private String loanInOutNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "loan_in_id")
	private LoanIn loanIn;
	
	@Column(name = "remark")
	private String remark;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="approved_by")
	private User approvedBy;

	@Column(name = "approval_on")
	private Date approvalOn;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;
	
	@Column(name = "cancel_note", length = 100)
	private String cancelNote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;
	
	@Column(name = "total_amount")
	private Double totalAmount;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "loanInOut", cascade = CascadeType.ALL)
	private List<LoanInOutDetail> loanInOutDetails;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovalOn() {
		return approvalOn;
	}

	public void setApprovalOn(Date approvalOn) {
		this.approvalOn = approvalOn;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public CancelReason getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(CancelReason cancelReason) {
		this.cancelReason = cancelReason;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getLoanInOutNo() {
		return loanInOutNo;
	}

	public void setLoanInOutNo(String loanInOutNo) {
		this.loanInOutNo = loanInOutNo;
	}

	public LoanIn getLoanIn() {
		return loanIn;
	}

	public void setLoanIn(LoanIn loanIn) {
		this.loanIn = loanIn;
	}

	public List<LoanInOutDetail> getLoanInOutDetails() {
		return loanInOutDetails;
	}

	public void setLoanInOutDetails(List<LoanInOutDetail> loanInOutDetails) {
		this.loanInOutDetails = loanInOutDetails;
	}

	public LoanInOutDetail addLoanInOutDetail(LoanInOutDetail loanInOutDetails) {
		if(getLoanInOutDetails() == null)
			this.loanInOutDetails = new ArrayList<LoanInOutDetail>();
		
		getLoanInOutDetails().add(loanInOutDetails);
		loanInOutDetails.setLoanInOut(this);

		return loanInOutDetails;
	}
	
	public LoanInOut() {
		super();
	}

	public LoanInOut(String loanInOutNo, LoanIn loanIn, String remark, Status status, Double totalAmount) {
		super();
		this.loanInOutNo = loanInOutNo;
		this.loanIn = loanIn;
		this.remark = remark;
		this.status = status;
		this.totalAmount = totalAmount;
	}

}