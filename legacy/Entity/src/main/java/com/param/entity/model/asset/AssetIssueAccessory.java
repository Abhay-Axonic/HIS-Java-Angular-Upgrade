package com.param.entity.model.asset;

import java.io.Serial;
import java.io.Serializable;
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

@Entity(name = "AssetIssueAccessory")
@Table(name = "t_asset_issue_accessories", schema = "assets")
public class AssetIssueAccessory extends BaseEntity implements Serializable {
	@Serial
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
	@JoinColumn(name = "asset_issue_batch_mapper_id")
	private AssetIssueBatchMapper assetIssueBatchMapper;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_indent_accessories_id")
	private AssetIndentAccessories assetIndentAccessories;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assetIssueAccessories", cascade = CascadeType.ALL)
	private List<RejectAssetAccessory> rejectAssetAccessories;


	public AssetIssueAccessory() {
		super();
	}

	public AssetIssueAccessory(Integer id) {
		super();
		this.id = id;
	}

	public AssetIssueAccessory(Batch batch, Integer issueQuantity) {
		super();
		this.batch = batch;
		this.issueQuantity = issueQuantity;
	}

	public AssetIssueAccessory(Batch batch) {
		super();
		this.batch = batch;
	}

	public void updateAssetIssueAccessory(Integer acceptedQuantity, String rejectedNote, Integer rejectedQuantity,
			RejectReason rejectReason) {
		this.acceptedQuantity = acceptedQuantity;
		this.rejectedNote = rejectedNote;
		this.rejectedQuantity = rejectedQuantity;
		this.rejectReason = rejectReason;
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

	/*
	 * public AssetIssueDetail getAssetIssueDetail() { return assetIssueDetail; }
	 * 
	 * public void setAssetIssueDetail(AssetIssueDetail assetIssueDetail) {
	 * this.assetIssueDetail = assetIssueDetail; }
	 */
	public AssetIndentAccessories getAssetIndentAccessories() {
		return assetIndentAccessories;
	}

	public AssetIssueBatchMapper getAssetIssueBatchMapper() {
		return assetIssueBatchMapper;
	}

	public void setAssetIssueBatchMapper(AssetIssueBatchMapper assetIssueBatchMapper) {
		this.assetIssueBatchMapper = assetIssueBatchMapper;
	}

	public void setAssetIndentAccessories(AssetIndentAccessories assetIndentAccessories) {
		this.assetIndentAccessories = assetIndentAccessories;
	}

}