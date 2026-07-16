package com.param.entity.model.inventory;

import java.io.Serial;
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

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;

@Entity(name = "VarianceApproval")
@Table(name = "t_variance_approval", schema = "inventory")
public class VarianceApproval extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "cancel_note", length = 100)
	private String cancelNote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upload_stock_id")
	private UploadStock uploadStock;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "compute_stock_id")
	private ComputeStock computeStock;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "varianceApproval", cascade = CascadeType.ALL)
	private List<VarianceApprovalDetail> varianceApprovalDetails;

	public VarianceApproval() {
		super();
	}

	public VarianceApproval(Integer id) {
		super();
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public ComputeStock getComputeStock() {
		return computeStock;
	}

	public void setComputeStock(ComputeStock computeStock) {
		this.computeStock = computeStock;
	}

	public UploadStock getUploadStock() {
		return uploadStock;
	}

	public void setUploadStock(UploadStock uploadStock) {
		this.uploadStock = uploadStock;
	}

	public List<VarianceApprovalDetail> getVarianceApprovalDetails() {
		return varianceApprovalDetails;
	}

	public void setVarianceApprovalDetails(List<VarianceApprovalDetail> varianceApprovalDetails) {
		this.varianceApprovalDetails = varianceApprovalDetails;
	}

	public VarianceApprovalDetail addVarianceApprovalDetail(VarianceApprovalDetail varianceApprovalDetail) {

		if (getVarianceApprovalDetails() == null)
			this.varianceApprovalDetails = new ArrayList<VarianceApprovalDetail>();

		getVarianceApprovalDetails().add(varianceApprovalDetail);
		varianceApprovalDetail.setVarianceApproval(this);

		return varianceApprovalDetail;
	}

	public VarianceApprovalDetail removeVarianceApprovalDetail(VarianceApprovalDetail varianceApprovalDetail) {
		getVarianceApprovalDetails().remove(varianceApprovalDetail);
		varianceApprovalDetail.setVarianceApproval(null);

		return varianceApprovalDetail;
	}

	public VarianceApproval(Status status, Store store, UploadStock uploadStock, ComputeStock computeStock) {
		super();
		this.status = status;
		this.store = store;
		this.uploadStock = uploadStock;
		this.computeStock = computeStock;
	}

}
