package com.param.entity.model.inventory;

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
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
@Entity(name = "ConfirmStock")
@Table(name = "t_confirm_stock", schema = "inventory")
public class ConfirmStock extends BaseEntity implements Serializable {
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
	@JoinColumn(name = "compute_stock_id")
	private ComputeStock computeStock;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private User approvedBy;

	@Column(name="approved_date")
	private Date approvedDate;
	
	@Column(name = "stock_adjustment_no")
	private String stockAdjustmentNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "variance_approval_id")
	private VarianceApproval varianceApproval;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = true)
	private Status status;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "confirmStock", cascade = CascadeType.ALL)
	private List<ConfirmStockDetail> confirmStockDetails;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store; //dilip

	public ConfirmStock() {
	}

	public ConfirmStock(ComputeStock computeStock, String stockAdjustmentNo, VarianceApproval varianceApproval, Status status) {
		super();
		this.computeStock = computeStock;
		this.stockAdjustmentNo = stockAdjustmentNo;
		this.varianceApproval = varianceApproval;
		this.status = status;
	}
	public ConfirmStock(ComputeStock computeStock, String stockAdjustmentNo, VarianceApproval varianceApproval, Status status, Store store) {
		super();
		this.computeStock = computeStock;
		this.stockAdjustmentNo = stockAdjustmentNo;
		this.varianceApproval = varianceApproval;
		this.status = status;
		this.store = store;
	}
	
	public ConfirmStock(ComputeStock computeStock, String stockAdjustmentNo,Status status, Store store) {
		super();
		this.computeStock = computeStock;
		this.stockAdjustmentNo = stockAdjustmentNo;	
		this.status = status;
		this.store = store;
	}//dilip

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

	public ComputeStock getComputeStock() {
		return computeStock;
	}

	public void setComputeStock(ComputeStock computeStock) {
		this.computeStock = computeStock;
	}

	public String getStockAdjustmentNo() {
		return stockAdjustmentNo;
	}

	public void setStockAdjustmentNo(String stockAdjustmentNo) {
		this.stockAdjustmentNo = stockAdjustmentNo;
	}

	public VarianceApproval getVarianceApproval() {
		return varianceApproval;
	}

	public void setVarianceApproval(VarianceApproval varianceApproval) {
		this.varianceApproval = varianceApproval;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<ConfirmStockDetail> getConfirmStockDetails() {
		return this.confirmStockDetails;
	}

	public void setConfirmStockDetails(List<ConfirmStockDetail> confirmStockDetails) {
		this.confirmStockDetails = confirmStockDetails;
	}

	public ConfirmStockDetail addConfirmStockDetail(ConfirmStockDetail confirmStockDetail) {
		if (getConfirmStockDetails() == null)
			this.confirmStockDetails = new ArrayList<ConfirmStockDetail>();

		getConfirmStockDetails().add(confirmStockDetail);
		confirmStockDetail.setConfirmStock(this);

		return confirmStockDetail;
	}

	public ConfirmStockDetail removeConfirmStockDetail(ConfirmStockDetail confirmStockDetail) {
		getConfirmStockDetails().remove(confirmStockDetail);
		confirmStockDetail.setConfirmStock(null);

		return confirmStockDetail;
	}
	
	//dilip
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

}