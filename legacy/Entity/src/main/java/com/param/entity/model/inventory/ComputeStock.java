package com.param.entity.model.inventory;

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
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;

@Entity(name = "ComputeStock")
@Table(name = "t_compute_stock", schema = "inventory")
public class ComputeStock extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upload_stock_id", nullable = true)
	private UploadStock uploadStock;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = true)
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@Column(name = "cancel_by")
	private Integer cancelBy;

	@Column(name = "cancel_on")
	private Date cancelDate;
	@Column(name = "cancel_note")
	private String cancelNote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	protected User approvedBy;

	@Column(name = "approved_on")
	private Date approvedDate;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ComputeStock", cascade = CascadeType.ALL)
	private List<ComputeStockDetail> computeStockDetail;

	/*
	 * @OneToMany(fetch = FetchType.LAZY, mappedBy = "computeStock", cascade =
	 * CascadeType.ALL) private List<ConfirmStock> confirmStock;
	 */

	public ComputeStock() {
		super();
	}

	public ComputeStock(Integer id) {
		super();
		this.id = id;
	}

	public ComputeStock(UploadStock uploadStock, Status status, Store store) {
		super();
		this.uploadStock = uploadStock;
		this.status = status;
		this.store = store;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UploadStock getUploadStock() {
		return uploadStock;
	}

	public void setUploadStock(UploadStock uploadStock) {
		this.uploadStock = uploadStock;
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

	public Integer getCancelBy() {
		return cancelBy;
	}

	public void setCancelBy(Integer cancelBy) {
		this.cancelBy = cancelBy;
	}

	public Date getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
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

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public List<ComputeStockDetail> getComputeStockDetail() {
		return computeStockDetail;
	}

	public void setComputeStockDetail(List<ComputeStockDetail> computeStockDetail) {
		this.computeStockDetail = computeStockDetail;
	}

	public ComputeStockDetail addComputeStockDetail(ComputeStockDetail computeStockDetail) {
		if (getComputeStockDetail() == null)
			this.computeStockDetail = new ArrayList<ComputeStockDetail>();

		getComputeStockDetail().add(computeStockDetail);
		computeStockDetail.setComputeStock(this);

		return computeStockDetail;
	}

	/*
	 * public List<ConfirmStock> getConfirmStock() { return confirmStock; }
	 * 
	 * public void setConfirmStock(List<ConfirmStock> confirmStock) {
	 * this.confirmStock = confirmStock; }
	 */
}
