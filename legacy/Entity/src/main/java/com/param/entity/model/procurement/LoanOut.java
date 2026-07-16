package com.param.entity.model.procurement;

import java.io.Serial;
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
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;

@Entity(name = "LoanOut")
@Table(name = "t_loan_out", schema = "procurement")
public class LoanOut extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "approval_on")
	private Date approvalOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private User approvedBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_type_id")
	private AssetType assetType;

	@Column(name = "cancel_note", length = 100)
	private String cancelNote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;

	@Column(name = "dc_date")
	private Date dcDate;

	@Column(name = "dc_no")
	private String dcNo;

	@Column(name = "loan_out_no")
	private String loanOutNo;

	private String remark;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@Column(name = "total_amount")
	private Double totalAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id")
	private Vendor vendor;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "loanOut", cascade = CascadeType.ALL)
	private List<LoanOutDetail> loanOutDetails;

	public LoanOut() {
	}

	public LoanOut(Integer id) {
		super();
		this.id = id;
	}

	public LoanOut(AssetType assetType, Date dcDate, String dcNo, String loanOutNo, String remark, Status status,
			Store store, Double totalAmount, Vendor vendor) {
		super();
		this.assetType = assetType;
		this.dcDate = dcDate;
		this.dcNo = dcNo;
		this.loanOutNo = loanOutNo;
		this.remark = remark;
		this.status = status;
		this.store = store;
		this.totalAmount = totalAmount;
		this.vendor = vendor;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getApprovalOn() {
		return approvalOn;
	}

	public void setApprovalOn(Date approvalOn) {
		this.approvalOn = approvalOn;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
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

	public Date getDcDate() {
		return dcDate;
	}

	public void setDcDate(Date dcDate) {
		this.dcDate = dcDate;
	}

	public String getDcNo() {
		return dcNo;
	}

	public void setDcNo(String dcNo) {
		this.dcNo = dcNo;
	}

	public String getLoanOutNo() {
		return loanOutNo;
	}

	public void setLoanOutNo(String loanOutNo) {
		this.loanOutNo = loanOutNo;
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

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public List<LoanOutDetail> getLoanOutDetails() {
		return this.loanOutDetails;
	}

	public void setLoanOutDetails(List<LoanOutDetail> loanOutDetails) {
		this.loanOutDetails = loanOutDetails;
	}

	public LoanOutDetail addLoanOutDetail(LoanOutDetail loanOutDetail) {
		if (getLoanOutDetails() == null)
			this.loanOutDetails = new ArrayList<LoanOutDetail>();

		getLoanOutDetails().add(loanOutDetail);
		loanOutDetail.setLoanOut(this);

		return loanOutDetail;
	}

	public LoanOutDetail removeTLoanOutDetail(LoanOutDetail loanOutDetail) {
		getLoanOutDetails().remove(loanOutDetail);
		loanOutDetail.setLoanOut(null);

		return loanOutDetail;
	}

}