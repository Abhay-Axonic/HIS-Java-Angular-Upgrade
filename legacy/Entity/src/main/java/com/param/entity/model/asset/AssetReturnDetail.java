package com.param.entity.model.asset;

import java.io.Serializable;
import java.util.ArrayList;
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

import com.param.adt.master.global.model.ReasonMaster;
import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ReturnReason;
import com.param.entity.model.master.Status;

@Entity(name = "AssetReturnDetail")
@Table(name = "t_asset_return_detail", schema = "assets")
public class AssetReturnDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "accepted_quantity")
	private Integer acceptedQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id")
	private Batch batch;

	@Column(name = "reject_note", length = 100)
	private String rejectNote;

	@Column(name = "reject_quantity")
	private Integer rejectQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reject_reason_id")
	private RejectReason rejectReason;

	@Column(name = "return_quantity")
	private Integer returnQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "return_reason_id")
	private ReasonMaster returnReason;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_details_id" , nullable=true)
	private AssetIssueDetail assetIssueDetail;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_batch_mapper_id" , nullable=false)
	private AssetIssueBatchMapper assetIssueBatchMapper;
	
	
	public AssetIssueBatchMapper getAssetIssueBatchMapper() {
		return assetIssueBatchMapper;
	}

	public void setAssetIssueBatchMapper(AssetIssueBatchMapper assetIssueBatchMapper) {
		this.assetIssueBatchMapper = assetIssueBatchMapper;
	}

	public AssetIssueDetail getAssetIssueDetail() {
		return assetIssueDetail;
	}

	public void setAssetIssueDetail(AssetIssueDetail assetIssueDetail) {
		this.assetIssueDetail = assetIssueDetail;
	}

	// bi-directional many-to-one association to TAssetReturn
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "return_id")
	private AssetReturn assetReturn;
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;
	

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assetReturnDetail", cascade = CascadeType.PERSIST)
	private List<AssetReturnAccessory> assetReturnAccessories;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assetReturnDetail", cascade = CascadeType.ALL)
	private List<RejectedAssetAcceptanceDetail> rejectedAssetAcceptanceDetails;

	public AssetReturnDetail() {
		super();
	}

	public AssetReturnDetail(Integer id) {
		super();
		this.id = id;
	}

	public AssetReturnDetail(Batch batch, Integer returnQuantity, ReasonMaster returnReason, Status status) {
		super();
		this.batch = batch;
		this.returnQuantity = returnQuantity;
		this.returnReason = returnReason;
		this.status = status;
	}

	public AssetReturnDetail(Batch batch, RejectReason rejectReason, Integer returnQuantity, Status status) {
		super();
		this.batch = batch;
		this.rejectReason = rejectReason;
		this.returnQuantity = returnQuantity;
		this.status = status;
	}

	public void updateAssetReturnDetail(Integer acceptedQuantity, String rejectNote, Integer rejectQuantity,
			RejectReason rejectReason, Status status) {
		this.acceptedQuantity = acceptedQuantity;
		this.rejectNote = rejectNote;
		this.rejectQuantity = rejectQuantity;
		this.rejectReason = rejectReason;
		this.status = status;
	}

	public void updateAssetReturn(ReasonMaster returnReason, Integer returnQuantity) {
		this.returnReason = returnReason;
		this.returnQuantity = returnQuantity;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}

	public RejectReason getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(RejectReason rejectReason) {
		this.rejectReason = rejectReason;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public ReasonMaster getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(ReasonMaster returnReason) {
		this.returnReason = returnReason;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public AssetReturn getAssetReturn() {
		return assetReturn;
	}

	public void setAssetReturn(AssetReturn assetReturn) {
		this.assetReturn = assetReturn;
	}

	public List<AssetReturnAccessory> getAssetReturnAccessories() {
		return assetReturnAccessories;
	}

	public void setAssetReturnAccessories(List<AssetReturnAccessory> assetReturnAccessories) {
		this.assetReturnAccessories = assetReturnAccessories;
	}

	public AssetReturnAccessory addAssetReturnAccessories(AssetReturnAccessory assetReturnAccessory) {
		if (getAssetReturnAccessories() == null)
			this.assetReturnAccessories = new ArrayList<AssetReturnAccessory>();

		getAssetReturnAccessories().add(assetReturnAccessory);
		assetReturnAccessory.setAssetReturnDetail(this);

		return assetReturnAccessory;
	}
}