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
import com.param.entity.model.master.ReturnReason;
import com.param.entity.model.master.User;

@Entity(name = "AssetReturnAccessory")
@Table(name = "t_asset_return_accessories", schema = "assets")
public class AssetReturnAccessory extends BaseEntity implements Serializable {

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

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "asset_return_detail_id")
	private AssetReturnDetail assetReturnDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id", nullable = false)
	private Batch batch;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rejected_reason_id")
	private RejectReason rejectReason;

	@Column(name = "rejected_note")
	private String rejectedNote;

	@Column(name = "rejected_quantity")
	private Integer rejectedQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reason_id")
	private ReturnReason returnReason;

	@Column(name = "return_quantity")
	private Integer returnQuantity;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assetReturnAccessories", cascade = CascadeType.ALL)
	private List<RejectAssetAccessory> rejectAssetAccessories;

	public AssetReturnAccessory() {
		super();
	}

	public AssetReturnAccessory(Integer id) {
		super();
		this.id = id;
	}

	public AssetReturnAccessory(Batch batch, ReturnReason returnReason, Integer returnQuantity) {
		super();
		this.batch = batch;
		this.returnReason = returnReason;
		this.returnQuantity = returnQuantity;
	}

	public void updateAssetReturnAccessories(Batch batch, ReturnReason returnReason, Integer returnQuantity) {
		this.batch = batch;
		this.returnReason = returnReason;
		this.returnQuantity = returnQuantity;
	}

	public void updateAssetReturnAccessories(Integer acceptedQuantity, RejectReason rejectReason, String rejectedNote,
			Integer rejectedQuantity) {
		this.acceptedQuantity = acceptedQuantity;
		this.rejectReason = rejectReason;
		this.rejectedNote = rejectedNote;
		this.rejectedQuantity = rejectedQuantity;
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

	public AssetReturnDetail getAssetReturnDetail() {
		return assetReturnDetail;
	}

	public void setAssetReturnDetail(AssetReturnDetail assetReturnDetail) {
		this.assetReturnDetail = assetReturnDetail;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public RejectReason getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(RejectReason rejectReason) {
		this.rejectReason = rejectReason;
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

	public ReturnReason getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(ReturnReason returnReason) {
		this.returnReason = returnReason;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

}
