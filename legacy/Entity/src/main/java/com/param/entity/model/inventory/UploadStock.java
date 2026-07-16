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

@Entity(name = "UploadStock")
@Table(name = "t_upload_stock", schema = "inventory")
public class UploadStock extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	protected User approvedBy;

	@Column(name = "approved_date")
	private Date approvedDate;

	@Column(name = "cancel_note", length = 100)
	private String cancelNote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approval_status_id", nullable = false)
	private Status approvalStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stop_transaction_id")
	private StopTransaction stopTransaction;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@Column(name = "upload_no")
	private String uploadNo;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "uploadStock", cascade = CascadeType.ALL)
	private List<UploadStockDetail> uploadStockDetails;
	
	@Column(name = "remark")
	private String remark;

	public UploadStock() {
		super();
	}

	public UploadStock(Status status, StopTransaction stopTransaction, Store store, String uploadNo,
			Status approvalStatus) {
		super();
		this.status = status;
		this.stopTransaction = stopTransaction;
		this.store = store;
		this.uploadNo = uploadNo;
		this.approvalStatus = approvalStatus;
	}

	public UploadStock(Integer id) {
		super();
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Status getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Status approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public StopTransaction getStopTransaction() {
		return stopTransaction;
	}

	public void setStopTransaction(StopTransaction stopTransaction) {
		this.stopTransaction = stopTransaction;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public String getUploadNo() {
		return uploadNo;
	}

	public void setUploadNo(String uploadNo) {
		this.uploadNo = uploadNo;
	}

	public List<UploadStockDetail> getUploadStockDetails() {
		return uploadStockDetails;
	}

	public void setUploadStockDetails(List<UploadStockDetail> uploadStockDetails) {
		this.uploadStockDetails = uploadStockDetails;
	}

	public UploadStockDetail addUploadStockDetail(UploadStockDetail uploadStockDetail) {
		if (getUploadStockDetails() == null)
			this.uploadStockDetails = new ArrayList<UploadStockDetail>();

		getUploadStockDetails().add(uploadStockDetail);
		uploadStockDetail.setUploadStock(this);

		return uploadStockDetail;
	}

	public UploadStockDetail removeUploadStockDetail(UploadStockDetail uploadStockDetail) {
		getUploadStockDetails().remove(uploadStockDetail);
		uploadStockDetail.setUploadStock(null);

		return uploadStockDetail;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
