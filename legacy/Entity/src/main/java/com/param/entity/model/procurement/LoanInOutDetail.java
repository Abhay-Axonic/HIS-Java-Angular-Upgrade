package com.param.entity.model.procurement;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.Status;

@Entity(name = "LoanInOutDetail")
@Table(name = "t_loan_in_out_detail", schema = "procurement")
public class LoanInOutDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "loan_in_out_id")
	private LoanInOut loanInOut;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "loan_in_detail_id")
	private LoanInDetail loanInDetail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;
	
	@Column(name = "return_quantity")
	private Integer returnQuantity;
	
	@Column(name = "total_amount")
	private Double totalAmount;
	
	@Column(name = "remark")
	private String remark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LoanInOut getLoanInOut() {
		return loanInOut;
	}

	public void setLoanInOut(LoanInOut loanInOut) {
		this.loanInOut = loanInOut;
	}

	public LoanInDetail getLoanInDetail() {
		return loanInDetail;
	}

	public void setLoanInDetail(LoanInDetail loanInDetail) {
		this.loanInDetail = loanInDetail;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LoanInOutDetail() {
		super();
	}

	public LoanInOutDetail(Integer id, LoanInOut loanInOut, LoanInDetail loanInDetail,
			Integer returnQuantity, Double totalAmount, String remark) {
		super();
		this.id = id;
		this.loanInOut = loanInOut;
		this.loanInDetail = loanInDetail;
		this.returnQuantity = returnQuantity;
		this.totalAmount = totalAmount;
		this.remark = remark;
	}

	public LoanInOutDetail(LoanInDetail loanInDetail, Integer returnQuantity, Double totalAmount,
			String remark) {
		super();
		this.loanInDetail = loanInDetail;
		this.returnQuantity = returnQuantity;
		this.totalAmount = totalAmount;
		this.remark = remark;
	}
	
	public void updateLoanInOutDetail(LoanInDetail loanInDetail, Integer returnQuantity, Double totalAmount,
			String remark) {
		this.loanInDetail = loanInDetail;
		this.returnQuantity = returnQuantity;
		this.totalAmount = totalAmount;
		this.remark = remark;
	}
	
}