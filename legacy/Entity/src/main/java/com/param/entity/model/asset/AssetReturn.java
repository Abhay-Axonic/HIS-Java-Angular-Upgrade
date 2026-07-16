package com.param.entity.model.asset;

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
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;

@Entity(name = "AssetReturn")
@Table(name = "t_asset_return", schema = "assets")
public class AssetReturn extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approval_status_id", nullable = false)
	private Status approvalStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	protected User approvedBy;

	@Column(name = "approved_date")
	private Date approvedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "carrier_id")
	private User carrier;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_store_id", nullable = false)
	private Store fromStore;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_store_id", nullable = false)
	private Store toStore;

	@Column(name = "return_no")
	private String returnNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;

	private String remark;

	@Column(name = "cancel_note", length = 100)
	private String cancelNote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assetReturn", cascade = CascadeType.ALL)
	private List<AssetReturnDetail> assetReturnDetail;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assetReturn")
	private List<RejectedAssetAcceptance> rejectedAssetAcceptance;

	public AssetReturn() {
		super();
	}

	public AssetReturn(Integer id) {
		super();
		this.id = id;
	}

	public AssetReturn(Status approvalStatus, User carrier, Store fromStore, Store toStore, String returnNo,
			Status status) {
		super();
		this.approvalStatus = approvalStatus;
		this.carrier = carrier;
		this.fromStore = fromStore;
		this.toStore = toStore;
		this.returnNo = returnNo;
		this.status = status;
	}

	public void updateAssetreturn(User carrier, String remark) {
		this.carrier = carrier;
		this.remark = remark;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Status getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Status approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public List<RejectedAssetAcceptance> getRejectedAssetAcceptance() {
		return rejectedAssetAcceptance;
	}

	public void setRejectedAssetAcceptance(List<RejectedAssetAcceptance> rejectedAssetAcceptance) {
		this.rejectedAssetAcceptance = rejectedAssetAcceptance;
	}

	public User getCarrier() {
		return carrier;
	}

	public void setCarrier(User carrier) {
		this.carrier = carrier;
	}

	public Store getFromStore() {
		return fromStore;
	}

	public void setFromStore(Store fromStore) {
		this.fromStore = fromStore;
	}

	public Store getToStore() {
		return toStore;
	}

	public void setToStore(Store toStore) {
		this.toStore = toStore;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<AssetReturnDetail> getAssetReturnDetail() {
		return assetReturnDetail;
	}

	public void setAssetReturnDetail(List<AssetReturnDetail> assetReturnDetail) {
		this.assetReturnDetail = assetReturnDetail;
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

	public AssetReturnDetail addAssetReturnDetail(AssetReturnDetail assetReturnDetail) {
		if (getAssetReturnDetail() == null)
			this.assetReturnDetail = new ArrayList<AssetReturnDetail>();

		getAssetReturnDetail().add(assetReturnDetail);
		assetReturnDetail.setAssetReturn(this);

		return assetReturnDetail;
	}

	/*
	 * public List<RejectedAssetAcceptance> getRejectedAssetAcceptance() { return
	 * rejectedAssetAcceptance; }
	 * 
	 * public void setRejectedAssetAcceptance(List<RejectedAssetAcceptance>
	 * rejectedAssetAcceptance) { this.rejectedAssetAcceptance =
	 * rejectedAssetAcceptance; }
	 * 
	 * public RejectedAssetAcceptance
	 * addRejectedAssetAcceptance(RejectedAssetAcceptance rejectedAssetAcceptance) {
	 * getRejectedAssetAcceptance().add(rejectedAssetAcceptance);
	 * rejectedAssetAcceptance.setAssetReturn(this);
	 * 
	 * return rejectedAssetAcceptance; }
	 * 
	 * public RejectedAssetAcceptance
	 * removeRejectedAssetAcceptance(RejectedAssetAcceptance
	 * rejectedAssetAcceptance) {
	 * getRejectedAssetAcceptance().remove(rejectedAssetAcceptance);
	 * rejectedAssetAcceptance.setAssetReturn(null);
	 * 
	 * return rejectedAssetAcceptance; }
	 */

}
