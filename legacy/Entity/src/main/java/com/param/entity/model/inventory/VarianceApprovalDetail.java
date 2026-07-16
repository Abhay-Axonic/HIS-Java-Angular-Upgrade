package com.param.entity.model.inventory;

import java.io.Serial;
import java.io.Serializable;

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

@Entity(name = "VarianceApprovalDetail")
@Table(name = "t_variance_approval_detail", schema = "inventory")
public class VarianceApprovalDetail extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "is_approved")
	private Boolean isApproved;

	@Column(name = "reject_note", length = 100)
	private String rejectNote;

	/*
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "item_id") private Item item;
	 */

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reject_reason_id")
	private RejectReason rejectReason;

	/*
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "status_id", nullable = false) private Status status;
	 */

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id")
	private Batch batch;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "variance_approval_id")
	private VarianceApproval varianceApproval;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upload_stoock_detail_id")
	private UploadStockDetail uploadStockDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "compute_stock_detail_id")
	private ComputeStockDetail computeStockDetail;

	public VarianceApprovalDetail() {
	}

	public VarianceApprovalDetail(Integer id) {
		super();
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	/*
	 * public Item getItem() { return item; }
	 * 
	 * public void setItem(Item item) { this.item = item; }
	 */

	public RejectReason getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(RejectReason rejectReason) {
		this.rejectReason = rejectReason;
	}

	/*
	 * public Status getStatus() { return status; }
	 * 
	 * public void setStatus(Status status) { this.status = status; }
	 */

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public VarianceApproval getVarianceApproval() {
		return varianceApproval;
	}

	public void setVarianceApproval(VarianceApproval varianceApproval) {
		this.varianceApproval = varianceApproval;
	}

	public UploadStockDetail getUploadStockDetail() {
		return uploadStockDetail;
	}

	public void setUploadStockDetail(UploadStockDetail uploadStockDetail) {
		this.uploadStockDetail = uploadStockDetail;
	}

	public ComputeStockDetail getComputeStockDetail() {
		return computeStockDetail;
	}

	public void setComputeStockDetail(ComputeStockDetail computeStockDetail) {
		this.computeStockDetail = computeStockDetail;
	}

	public VarianceApprovalDetail(Batch batch, ComputeStockDetail computeStockDetail, UploadStockDetail uploadStockDetail) {
		super();
		this.batch = batch;
		this.computeStockDetail = computeStockDetail;
		this.uploadStockDetail =uploadStockDetail;
	}

}