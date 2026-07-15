package com.param.entity.model.inventory;

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
import com.param.entity.model.master.Status;

@Entity(name = "ConfirmStockDetail")
@Table(name = "t_confirm_stock_detail", schema = "inventory")
public class ConfirmStockDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "compute_stock_detail_id")
	private ComputeStockDetail computeStockDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "variance_approval_detail_id")
	private VarianceApprovalDetail varianceApprovalDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "confirm_stock_id")
	private ConfirmStock confirmStock;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id")
	private Batch batch;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = true)
	private Status status;
	
	@Column(name = "reject_note", length = 100)
	private String rejectNote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reject_reason_id")
	private RejectReason rejectReason;
	
	@Column(name = "reject_quantity", length = 100)
	private Integer rejectQuantity;
	
	@Column(name = "approval_quantity", length = 100)
	private Integer approvalQuantity;


	public ConfirmStockDetail() {
	}

	public ConfirmStockDetail(ComputeStockDetail computeStockDetail, VarianceApprovalDetail varianceApprovalDetail,
			Batch batch) {
		super();
		this.computeStockDetail = computeStockDetail;
		this.varianceApprovalDetail = varianceApprovalDetail;
		this.batch = batch;
	}
	
	public ConfirmStockDetail(ComputeStockDetail computeStockDetail, 
			Batch batch) {
		super();
		this.computeStockDetail = computeStockDetail;	
		this.batch = batch;
	} 

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ComputeStockDetail getComputeStockDetail() {
		return computeStockDetail;
	}

	public void setComputeStockDetail(ComputeStockDetail computeStockDetail) {
		this.computeStockDetail = computeStockDetail;
	}

	public VarianceApprovalDetail getVarianceApprovalDetail() {
		return varianceApprovalDetail;
	}

	public void setVarianceApprovalDetail(VarianceApprovalDetail varianceApprovalDetail) {
		this.varianceApprovalDetail = varianceApprovalDetail;
	}

	public ConfirmStock getConfirmStock() {
		return confirmStock;
	}

	public void setConfirmStock(ConfirmStock confirmStock) {
		this.confirmStock = confirmStock;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public RejectReason getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(RejectReason rejectReason) {
		this.rejectReason = rejectReason;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}

	public Integer getApprovalQuantity() {
		return approvalQuantity;
	}

	public void setApprovalQuantity(Integer approvalQuantity) {
		this.approvalQuantity = approvalQuantity;
	}

}