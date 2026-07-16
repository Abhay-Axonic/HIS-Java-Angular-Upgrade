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

@Entity(name = "LoanIn")
@Table(name = "t_loan_in", schema = "procurement")
public class LoanIn extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "inward_no")
	private String inwardNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id")
	private Vendor vendor;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_type_id")
	private AssetType assetType;
	
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
	
	@Column(name = "dc_no")
	private String dcNo;
	
	@Column(name = "dc_date")
	private Date dcDate;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "loanIn", cascade = CascadeType.ALL)
	private List<LoanInDetail> loanInDetails;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getInwardNo() {
		return inwardNo;
	}

	public void setInwardNo(String inwardNo) {
		this.inwardNo = inwardNo;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
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

	public String getDcNo() {
		return dcNo;
	}

	public void setDcNo(String dcNo) {
		this.dcNo = dcNo;
	}

	public Date getDcDate() {
		return dcDate;
	}

	public void setDcDate(Date dcDate) {
		this.dcDate = dcDate;
	}

	public List<LoanInDetail> getLoanInDetails() {
		return loanInDetails;
	}

	public void setLoanInDetails(List<LoanInDetail> loanInDetails) {
		this.loanInDetails = loanInDetails;
	}

	public LoanInDetail addLoanInDetail(LoanInDetail loanInDetails) {
		if(getLoanInDetails() == null)
			this.loanInDetails = new ArrayList<LoanInDetail>();
		
		getLoanInDetails().add(loanInDetails);
		loanInDetails.setLoanIn(this);

		return loanInDetails;
	}
	
	public LoanIn() {
		super();
	}

	public LoanIn(Integer id, String inwardNo, Store store, Vendor vendor, AssetType assetType, String remark,
			User approvedBy, Date approvalOn, Status status, String cancelNote, CancelReason cancelReason,
			Double totalAmount, String dcNo, Date dcDate) {
		super();
		this.id = id;
		this.inwardNo = inwardNo;
		this.store = store;
		this.vendor = vendor;
		this.assetType = assetType;
		this.remark = remark;
		this.approvedBy = approvedBy;
		this.approvalOn = approvalOn;
		this.status = status;
		this.cancelNote = cancelNote;
		this.cancelReason = cancelReason;
		this.totalAmount = totalAmount;
		this.dcNo = dcNo;
		this.dcDate = dcDate;
	}

	public LoanIn(String inwardNo, Store store, Vendor vendor, AssetType assetType, String remark,
			Status status, Double totalAmount, String dcNo, Date dcDate) {
		super();
		this.inwardNo = inwardNo;
		this.store = store;
		this.vendor = vendor;
		this.assetType = assetType;
		this.remark = remark;
		this.status = status;
		this.totalAmount = totalAmount;
		this.dcNo = dcNo;
		this.dcDate = dcDate;
	}

	public LoanIn(Integer id) {
		super();
		this.id = id;
	}

}