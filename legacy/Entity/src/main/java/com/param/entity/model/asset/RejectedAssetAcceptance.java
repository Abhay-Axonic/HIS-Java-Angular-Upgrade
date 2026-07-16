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
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;

@Entity(name = "RejectedAssetAcceptance")
@Table(name = "t_rejected_asset_acceptance", schema = "assets")
public class RejectedAssetAcceptance extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_issue_id")
	private AssetIssue assetIssue;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rejected_by")
	private User rejectedBy;

	/*
	 * @Column(name = "rejected_date") private Date rejectedDate;
	 */

	@Column(name = "rejected_on")
	private Date rejectedOn;

	private String remark;

	@Column(name = "return_no")
	private String returnNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_store_id", nullable = false)
	private Store fromStore;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_store_id", nullable = false)
	private Store toStore;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_return_id")
	private AssetReturn assetReturn;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rejectedAssetAcceptance", cascade = CascadeType.ALL)
	private List<RejectedAssetAcceptanceDetail> rejectedAssetAcceptanceDetails;

	public RejectedAssetAcceptance() {
		super();
	}

	public RejectedAssetAcceptance(AssetReturn assetReturn, User rejectedBy, Date rejectedOn, String returnNo,
			Status status, Store fromStore, Store toStore) {
		super();
		this.assetReturn = assetReturn;
		this.rejectedBy = rejectedBy;
		this.rejectedOn = rejectedOn;
		this.returnNo = returnNo;
		this.status = status;
		this.fromStore = fromStore;
		this.toStore = toStore;
	}
	
	public RejectedAssetAcceptance(AssetIssue assetIssue, User rejectedBy, Date rejectedOn, String returnNo,
			Status status, Store fromStore, Store toStore) {
		super();
		this.assetIssue = assetIssue;
		this.rejectedBy = rejectedBy;
		this.rejectedOn = rejectedOn;
		this.returnNo = returnNo;
		this.status = status;
		this.fromStore = fromStore;
		this.toStore = toStore;
	}

	public RejectedAssetAcceptance(User rejectedBy, Date rejectedOn, String returnNo, Status status, Store fromStore,
			Store toStore) {
		super();
		this.rejectedBy = rejectedBy;
		this.rejectedOn = rejectedOn;
		this.returnNo = returnNo;
		this.status = status;
		this.fromStore = fromStore;
		this.toStore = toStore;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AssetIssue getAssetIssue() {
		return assetIssue;
	}

	public void setAssetIssue(AssetIssue assetIssue) {
		this.assetIssue = assetIssue;
	}

	public User getRejectedBy() {
		return rejectedBy;
	}

	public void setRejectedBy(User rejectedBy) {
		this.rejectedBy = rejectedBy;
	}

	public Date getRejectedOn() {
		return rejectedOn;
	}

	public void setRejectedOn(Date rejectedOn) {
		this.rejectedOn = rejectedOn;
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

	public AssetReturn getAssetReturn() {
		return assetReturn;
	}

	public void setAssetReturn(AssetReturn assetReturn) {
		this.assetReturn = assetReturn;
	}

	public List<RejectedAssetAcceptanceDetail> getRejectedAssetAcceptanceDetails() {
		return rejectedAssetAcceptanceDetails;
	}

	public void setRejectedAssetAcceptanceDetails(List<RejectedAssetAcceptanceDetail> rejectedAssetAcceptanceDetails) {
		this.rejectedAssetAcceptanceDetails = rejectedAssetAcceptanceDetails;
	}

	public RejectedAssetAcceptanceDetail addRejectedAssetAcceptanceDetail(
			RejectedAssetAcceptanceDetail rejectedAssetAcceptanceDetail) {
		if (getRejectedAssetAcceptanceDetails() == null)
			this.rejectedAssetAcceptanceDetails = new ArrayList<RejectedAssetAcceptanceDetail>();

		getRejectedAssetAcceptanceDetails().add(rejectedAssetAcceptanceDetail);
		rejectedAssetAcceptanceDetail.setRejectedAssetAcceptance(this);

		return rejectedAssetAcceptanceDetail;
	}

	public RejectedAssetAcceptanceDetail removeRejectedAssetAcceptanceDetail(
			RejectedAssetAcceptanceDetail rejectedAssetAcceptanceDetail) {
		getRejectedAssetAcceptanceDetails().remove(rejectedAssetAcceptanceDetail);
		rejectedAssetAcceptanceDetail.setRejectedAssetAcceptance(null);

		return rejectedAssetAcceptanceDetail;
	}

}
