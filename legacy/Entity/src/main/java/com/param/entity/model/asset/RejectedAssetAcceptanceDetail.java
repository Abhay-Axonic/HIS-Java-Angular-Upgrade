package com.param.entity.model.asset;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.User;

@Entity(name = "RejectedAssetAcceptanceDetail")
@Table(name = "t_rejected_asset_acceptance_detail", schema = "assets")
public class RejectedAssetAcceptanceDetail extends BaseEntity implements Serializable {
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
	@JoinColumn(name = "batch_id")
	private Batch batch;

	@Column(name = "rejected_note", length = 100)
	private String rejectedNote;

	@Column(name = "rejected_quantity")
	private Integer rejectedQuantity;

	@Column(name = "rejected_reject_quantity")
	private Integer rejectedRejectQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rejected_reason_id")
	private RejectReason rejectReason;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_issue_batch_mapper_id")
	private AssetIssueBatchMapper assetIssueBatchMapper;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_return_detail_id")
	private AssetReturnDetail assetReturnDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_reject_id")
	private RejectedAssetAcceptance rejectedAssetAcceptance;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rejectedAssetAcceptanceDetail", cascade = CascadeType.ALL)
	private List<RejectAssetAccessory> rejectAssetAccessories;

	public RejectedAssetAcceptanceDetail() {
	}

	public RejectedAssetAcceptanceDetail(String rejectedNote, Integer rejectedQuantity, RejectReason rejectReason,
			AssetIssueBatchMapper assetIssueBatchMapper) {
		super();
		this.rejectedNote = rejectedNote;
		this.rejectedQuantity = rejectedQuantity;
		this.rejectReason = rejectReason;
		this.assetIssueBatchMapper = assetIssueBatchMapper;
	}

	public RejectedAssetAcceptanceDetail(String rejectedNote, Integer rejectedQuantity, RejectReason rejectReason,
			AssetReturnDetail assetReturnDetail) {
		super();
		this.rejectedNote = rejectedNote;
		this.rejectedQuantity = rejectedQuantity;
		this.rejectReason = rejectReason;
		this.assetReturnDetail = assetReturnDetail;
	}

	public Integer getId() {
		return this.id;
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

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
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

	public AssetIssueBatchMapper getAssetIssueBatchMapper() {
		return assetIssueBatchMapper;
	}

	public void setAssetIssueBatchMapper(AssetIssueBatchMapper assetIssueBatchMapper) {
		this.assetIssueBatchMapper = assetIssueBatchMapper;
	}

	public RejectedAssetAcceptance getRejectedAssetAcceptance() {
		return rejectedAssetAcceptance;
	}

	public void setRejectedAssetAcceptance(RejectedAssetAcceptance rejectedAssetAcceptance) {
		this.rejectedAssetAcceptance = rejectedAssetAcceptance;
	}

	public AssetReturnDetail getAssetReturnDetail() {
		return assetReturnDetail;
	}

	public void setAssetReturnDetail(AssetReturnDetail assetReturnDetail) {
		this.assetReturnDetail = assetReturnDetail;
	}

	public List<RejectAssetAccessory> getRejectAssetAccessories() {
		return rejectAssetAccessories;
	}

	public void setRejectAssetAccessories(List<RejectAssetAccessory> rejectAssetAccessories) {
		this.rejectAssetAccessories = rejectAssetAccessories;
	}

	public Integer getRejectedRejectQuantity() {
		return rejectedRejectQuantity;
	}

	public void setRejectedRejectQuantity(Integer rejectedRejectQuantity) {
		this.rejectedRejectQuantity = rejectedRejectQuantity;
	}

	public RejectAssetAccessory addRejectedAssetAcceptanceDetail(RejectAssetAccessory rejectAssetAccessory) {
		if (getRejectAssetAccessories() == null)
			this.rejectAssetAccessories = new ArrayList<RejectAssetAccessory>();

		getRejectAssetAccessories().add(rejectAssetAccessory);
		rejectAssetAccessory.setRejectedAssetAcceptanceDetail(this);

		return rejectAssetAccessory;
	}

	public RejectAssetAccessory addRejectAssetAccessory(RejectAssetAccessory rejectAssetAccessory) {
		if (getRejectAssetAccessories() == null)
			this.rejectAssetAccessories = new ArrayList<RejectAssetAccessory>();

		getRejectAssetAccessories().add(rejectAssetAccessory);
		rejectAssetAccessory.setRejectedAssetAcceptanceDetail(this);

		return rejectAssetAccessory;
	}
}
