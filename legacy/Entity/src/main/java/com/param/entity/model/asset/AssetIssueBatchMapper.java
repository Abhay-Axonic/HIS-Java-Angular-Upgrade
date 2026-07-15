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
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.User;

@Entity(name = "AssetIssueBatchMapper")
@Table(name = "t_asset_issue_batch_mapper", schema = "assets")
public class AssetIssueBatchMapper extends BaseEntity implements Serializable {

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
	private Integer acceptedQuantity = 0;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id", nullable = false)
	private Batch batch;

	@Column(name = "issue_quantity")
	private Integer issueQuantity;

	@Column(name = "rejected_note")
	private String rejectedNote;

	@Column(name = "rejected_quantity")
	private Integer rejectedQuantity = 0;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rejected_reason_id")
	private RejectReason rejectReason;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "asset_issue_detail_id")
	private AssetIssueDetail assetIssueDetail;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assetIssueBatchMapper", cascade = CascadeType.ALL)
	private List<AssetIssueAccessory> assetIssueAccessories;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assetIssueBatchMapper", cascade = CascadeType.ALL)
	private List<RejectedAssetAcceptanceDetail> rejectedAssetAcceptanceDetails;


	public AssetIssueBatchMapper() {
		super();
	}

	public AssetIssueBatchMapper(Integer id) {
		super();
		this.id = id;
	}

	public AssetIssueBatchMapper(Batch batch, Integer issueQuantity) {
		super();
		this.batch = batch;
		this.issueQuantity = issueQuantity;
	}

	public void updateBatchMapper(Batch batch, Integer issueQuantity) {
		this.batch = batch;
		this.issueQuantity = issueQuantity;
	}

	public void updateAssetIssueBatchMapper(Integer id, Integer acceptedQuantity, String rejectedNote,
			Integer rejectedQuantity, RejectReason rejectReason) {
		this.id = id;
		this.acceptedQuantity = acceptedQuantity;
		this.rejectedNote = rejectedNote;
		this.rejectedQuantity = rejectedQuantity;
		this.rejectReason = rejectReason;
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

	public Integer getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Integer issueQuantity) {
		this.issueQuantity = issueQuantity;
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

	public AssetIssueDetail getAssetIssueDetail() {
		return assetIssueDetail;
	}

	public void setAssetIssueDetail(AssetIssueDetail assetIssueDetail) {
		this.assetIssueDetail = assetIssueDetail;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<AssetIssueAccessory> getAssetIssueAccessories() {
		return assetIssueAccessories;
	}

	public void setAssetIssueAccessories(List<AssetIssueAccessory> assetIssueAccessories) {
		this.assetIssueAccessories = assetIssueAccessories;
	}

	public AssetIssueAccessory addAccessories(AssetIssueAccessory assetIssueAccessory) {
		if (this.assetIssueAccessories == null)
			this.assetIssueAccessories = new ArrayList<AssetIssueAccessory>();

		this.assetIssueAccessories.add(assetIssueAccessory);
		assetIssueAccessory.setAssetIssueBatchMapper(this);
		;

		return assetIssueAccessory;
	}

}
