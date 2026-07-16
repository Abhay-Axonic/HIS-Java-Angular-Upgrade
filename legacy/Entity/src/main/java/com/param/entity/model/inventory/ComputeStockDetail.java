package com.param.entity.model.inventory;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;

@Entity(name = "ComputeStockDetail")
@Table(name = "t_compute_stock_detail", schema = "inventory")
public class ComputeStockDetail extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "compute_stock_id")
	private ComputeStock ComputeStock;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upload_stock_detail_id")
	private UploadStockDetail uploadStockDetail;

	@Column(name = "new_batch_no")
	private String newBatchNo;

	@Column(name = "new_expiry_date")
	private Date newExpiryDate;

	@Column(name = "new_mrp")
	private Double newMrp;

	@Column(name = "approved_quantity")
	private Integer approvedQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reject_reason_id")
	private RejectReason rejectReason;

	@Column(name = "reject_quantity")
	private Integer rejectQuantity;

	@Column(name = "reject_note")
	private String rejectNote;

	@Column(name = "remark")
	private String remark;

	@Column(name = "cancel_by")
	private Integer cancelBy;

	@Column(name = "cancel_on")
	private Date cancelDate;

	@Column(name = "old_batch_no")
	private String oldBatchNo;

	@Column(name = "old_expiry_date")
	private Date oldExpiryDate;

	@Column(name = "old_mrp")
	private Double oldMrp;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id")
	private Batch batch;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = true)
	private Status status;
	
	
	@Column(name = "variance_cop")
	private Double varianceAtCOP; 
	
	@Column(name = "variance_price")
	private Double varianceAtMrp; 
	

	@Column(name = "item_id")
	private Integer itemId; 
	
	public ComputeStockDetail() {
		super();
	}

	public ComputeStockDetail(Integer id) {
		super();
		this.id = id;
	}

	public ComputeStockDetail(UploadStockDetail uploadStockDetail, Batch batch) {
		super();
		this.uploadStockDetail = uploadStockDetail;
		this.batch = batch;
	}
	
	public ComputeStockDetail(UploadStockDetail uploadStockDetail, Batch batch,Integer itemId) {
		super();
		this.uploadStockDetail = uploadStockDetail;
		this.batch = batch;
		this.itemId = itemId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ComputeStock getComputeStock() {
		return ComputeStock;
	}

	public void setComputeStock(ComputeStock computeStock) {
		this.ComputeStock = computeStock;
	}

	public UploadStockDetail getUploadStockDetail() {
		return uploadStockDetail;
	}

	public void setUploadStockDetail(UploadStockDetail uploadStockDetail) {
		this.uploadStockDetail = uploadStockDetail;
	}

	public String getNewBatchNo() {
		return newBatchNo;
	}

	public void setNewBatchNo(String newBatchNo) {
		this.newBatchNo = newBatchNo;
	}

	public Date getNewExpiryDate() {
		return newExpiryDate;
	}

	public void setNewExpiryDate(Date newExpiryDate) {
		this.newExpiryDate = newExpiryDate;
	}

	public Double getNewMrp() {
		return newMrp;
	}

	public void setNewMrp(Double newMrp) {
		this.newMrp = newMrp;
	}

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
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

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getOldBatchNo() {
		return oldBatchNo;
	}

	public void setOldBatchNo(String oldBatchNo) {
		this.oldBatchNo = oldBatchNo;
	}

	public Date getOldExpiryDate() {
		return oldExpiryDate;
	}

	public void setOldExpiryDate(Date oldExpiryDate) {
		this.oldExpiryDate = oldExpiryDate;
	}

	public Double getOldMrp() {
		return oldMrp;
	}

	public void setOldMrp(Double oldMrp) {
		this.oldMrp = oldMrp;
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
	
		public Double getvarianceAtCOP() {
			return varianceAtCOP;
		}

		public void setvarianceAtCOP(Double varianceAtCOP) {
			this.varianceAtCOP = varianceAtCOP;
		}
		public Double getvarianceAtMrp() {
			return varianceAtMrp;
		}

		public void setvarianceAtMrp(Double varianceAtMrp) {
			this.varianceAtMrp = varianceAtMrp;
		}
		public Integer getItemId() {
			return itemId;
		}

		public void setItemId(Integer itemId) {
			this.itemId = itemId;
		}
		
}
