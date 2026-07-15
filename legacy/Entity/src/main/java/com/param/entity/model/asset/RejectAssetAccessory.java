package com.param.entity.model.asset;

import java.io.Serializable;
import java.util.Date;

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
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.User;

@Entity(name = "RejectAssetAccessory")
@Table(name = "t_asset_reject_accessories", schema = "assets")
public class RejectAssetAccessory extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accepted_by")
	private User acceptedBy;

	@Column(name = "accepted_on")
	private Date acceptedOn;

	@Column(name = "accepted_quantity")
	private Integer acceptedQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_issue_accessories_id")
	private AssetIssueAccessory assetIssueAccessories;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_reject_detail_id")
	private RejectedAssetAcceptanceDetail rejectedAssetAcceptanceDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_return_accessories_id")
	private AssetReturnAccessory assetReturnAccessories;

	@Column(name = "rejected_note")
	private String rejectedNote;

	@Column(name = "rejected_quantity")
	private Integer rejectedQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rejected_reason_id")
	private RejectReason rejectReason;

	@Column(name = "rejected_reject_quantity")
	private Integer rejectedRejectQuantity;

	public RejectAssetAccessory() {
		super();
	}

	public RejectAssetAccessory(AssetIssueAccessory assetIssueAccessories, String rejectedNote,
			Integer rejectedQuantity, RejectReason rejectReason) {
		super();
		this.assetIssueAccessories = assetIssueAccessories;
		this.rejectedNote = rejectedNote;
		this.rejectedQuantity = rejectedQuantity;
		this.rejectReason = rejectReason;
	}

	public RejectAssetAccessory(AssetReturnAccessory assetReturnAccessories, String rejectedNote,
			Integer rejectedQuantity, RejectReason rejectReason) {
		super();
		this.assetReturnAccessories = assetReturnAccessories;
		this.rejectedNote = rejectedNote;
		this.rejectedQuantity = rejectedQuantity;
		this.rejectReason = rejectReason;
	}

	public void updateRejectAssetAccessory(Integer acceptedQuantity, AssetIssueAccessory assetIssueAccessories,
			Integer rejectedRejectQuantity, RejectReason rejectReason) {
		this.acceptedQuantity = acceptedQuantity;
		this.assetIssueAccessories = assetIssueAccessories;
		this.rejectedRejectQuantity = rejectedRejectQuantity;
		this.rejectReason = rejectReason;
	}

	public void updateRejectAssetAccessory(Integer acceptedQuantity, AssetReturnAccessory assetReturnAccessories,
			Integer rejectedRejectQuantity, RejectReason rejectReason) {
		this.acceptedQuantity = acceptedQuantity;
		this.assetReturnAccessories = assetReturnAccessories;
		this.rejectedRejectQuantity = rejectedRejectQuantity;
		this.rejectReason = rejectReason;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getAcceptedBy() {
		return acceptedBy;
	}

	public void setAcceptedBy(User acceptedBy) {
		this.acceptedBy = acceptedBy;
	}

	public Date getAcceptedOn() {
		return acceptedOn;
	}

	public void setAcceptedOn(Date acceptedOn) {
		this.acceptedOn = acceptedOn;
	}

	public Integer getAcceptedQuantity() {
		return acceptedQuantity;
	}

	public void setAcceptedQuantity(Integer acceptedQuantity) {
		this.acceptedQuantity = acceptedQuantity;
	}

	public AssetIssueAccessory getAssetIssueAccessories() {
		return assetIssueAccessories;
	}

	public void setAssetIssueAccessories(AssetIssueAccessory assetIssueAccessories) {
		this.assetIssueAccessories = assetIssueAccessories;
	}

	public RejectedAssetAcceptanceDetail getRejectedAssetAcceptanceDetail() {
		return rejectedAssetAcceptanceDetail;
	}

	public void setRejectedAssetAcceptanceDetail(RejectedAssetAcceptanceDetail rejectedAssetAcceptanceDetail) {
		this.rejectedAssetAcceptanceDetail = rejectedAssetAcceptanceDetail;
	}

	public AssetReturnAccessory getAssetReturnAccessories() {
		return assetReturnAccessories;
	}

	public void setAssetReturnAccessories(AssetReturnAccessory assetReturnAccessories) {
		this.assetReturnAccessories = assetReturnAccessories;
	}

	public String getRejectedNote() {
		return rejectedNote;
	}

	public void setRejectedNote(String rejectedNote) {
		this.rejectedNote = rejectedNote;
	}

	public Integer getRejectedQuantity() {
		return rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
	}

	public RejectReason getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(RejectReason rejectReason) {
		this.rejectReason = rejectReason;
	}

	public Integer getRejectedRejectQuantity() {
		return rejectedRejectQuantity;
	}

	public void setRejectedRejectQuantity(Integer rejectedRejectQuantity) {
		this.rejectedRejectQuantity = rejectedRejectQuantity;
	}

}
