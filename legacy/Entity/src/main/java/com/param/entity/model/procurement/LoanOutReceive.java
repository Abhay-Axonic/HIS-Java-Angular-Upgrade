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
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;

@Entity(name = "LoanOutReceive")
@Table(name = "t_loanout_receive", schema = "procurement")
public class LoanOutReceive extends BaseEntity implements Serializable {
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

	@Column(name = "loanout_receive_no")
	private String loanoutReceiveNo;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "loan_out_id")
	private LoanOut loanOut;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "loanOutReceive", cascade = CascadeType.ALL)
	private List<LoanOutReceiveDetail> loanOutReceiveDetails;

	public LoanOutReceive() {

	}

	public LoanOutReceive(AssetType assetType, String loanoutReceiveNo, String remark, Status status, Store store,
			Double totalAmount, Vendor vendor, LoanOut loanOut) {
		super();
		this.assetType = assetType;
		this.loanoutReceiveNo = loanoutReceiveNo;
		this.remark = remark;
		this.status = status;
		this.store = store;
		this.totalAmount = totalAmount;
		this.vendor = vendor;
		this.loanOut = loanOut;
	}

	public Integer getId() {
		return id;
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

	public String getLoanoutReceiveNo() {
		return loanoutReceiveNo;
	}

	public void setLoanoutReceiveNo(String loanoutReceiveNo) {
		this.loanoutReceiveNo = loanoutReceiveNo;
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

	public LoanOut getLoanOut() {
		return loanOut;
	}

	public void setLoanOut(LoanOut loanOut) {
		this.loanOut = loanOut;
	}

	public List<LoanOutReceiveDetail> getLoanOutReceiveDetails() {
		return loanOutReceiveDetails;
	}

	public void setLoanOutReceiveDetails(List<LoanOutReceiveDetail> loanOutReceiveDetails) {
		this.loanOutReceiveDetails = loanOutReceiveDetails;
	}

	public LoanOutReceiveDetail addLoanOutReceiveDetail(LoanOutReceiveDetail loanOutReceiveDetail) {
		if (getLoanOutReceiveDetails() == null)
			this.loanOutReceiveDetails = new ArrayList<LoanOutReceiveDetail>();

		getLoanOutReceiveDetails().add(loanOutReceiveDetail);
		loanOutReceiveDetail.setLoanOutReceive(this);

		return loanOutReceiveDetail;
	}

	public LoanOutReceiveDetail removeLoanOutReceiveDetail(LoanOutReceiveDetail loanOutReceiveDetail) {
		getLoanOutReceiveDetails().remove(loanOutReceiveDetail);
		loanOutReceiveDetail.setLoanOutReceive(null);

		return loanOutReceiveDetail;
	}

}