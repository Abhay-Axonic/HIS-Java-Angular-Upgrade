package com.param.entity.model.inventory;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.UomType;
import com.param.utility.TimeZoneComponent;

@Entity(name = "BatchExpDetail")
@Table(name = "t_batch_exp_detail", schema = "inventory")
public class BatchExpDetail extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Temporal(TemporalType.DATE)
	@Column(name = "new_batch_expiry" )
	private Date newBatchExpiry;

	@Column(name = "new_batch_no",  length = 100)
	private String newBatchNo;

	@Column(name = "new_mrp", nullable = false)
	private Double newMrp;

	@Temporal(TemporalType.DATE)
	@Column(name = "old_batch_expiry")
	private Date oldBatchExpiry;

	@Column(name = "old_batch_no", length = 100)
	private String oldBatchNo;

	@Column(name = "old_mrp", nullable = false)
	private Double oldMrp;
	@Column(name = "lcop", nullable = true)
	private Double lcop;
	@Column(name = "cop", nullable = true)
	private Double cop;

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	@Column(name = "reject_note", length = 100)
	private String rejectNote;

	public Double getLcop() {
		return lcop;
	}

	public void setLcop(Double lcop) {
		this.lcop = lcop;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reject_reason_id")
	private RejectReason rejectReason;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id", nullable = false)
	private Batch batch;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_expiry_id", nullable = false)
	private BatchExp batchExp;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mrp_uom_type_id")
	private UomType mrpUomType;

	public UomType getMrpUomType() {
		return mrpUomType;
	}

	public void setMrpUomType(UomType mrpUomType) {
		this.mrpUomType = mrpUomType;
	}

	public BatchExpDetail() {
		super();
	}

	public BatchExpDetail(Date newBatchExpiry, String newBatchNo, Double newMrp, Date oldBatchExpiry, String oldBatchNo,
			Double oldMrp, Status status, Batch batch, UomType mrpUomType) {
		super();
		this.newBatchExpiry = newBatchExpiry;
		this.newBatchNo = newBatchNo;
		this.newMrp = newMrp;
		this.oldBatchExpiry = oldBatchExpiry;
		this.oldBatchNo = oldBatchNo;
		this.oldMrp = oldMrp;
		this.status = status;
		this.batch = batch;
		this.mrpUomType = mrpUomType;
	}
	
	public BatchExpDetail(Date newBatchExpiry, String newBatchNo, Double newMrp, Date oldBatchExpiry, String oldBatchNo,
			Double oldMrp, Status status, Batch batch, UomType mrpUomType, Double lcop) {
		super();
		this.newBatchExpiry = newBatchExpiry;
		this.newBatchNo = newBatchNo;
		this.newMrp = newMrp;
		this.oldBatchExpiry = oldBatchExpiry;
		this.oldBatchNo = oldBatchNo;
		this.oldMrp = oldMrp;
		this.status = status;
		this.batch = batch;
		this.mrpUomType = mrpUomType;
		this.lcop = lcop;
	}

	public void updateDetails(Date newBatchExpiry, String newBatchNo, Double newMrp, UomType mrpUomType) {
		this.newBatchExpiry = newBatchExpiry;
		this.newBatchNo = newBatchNo;
		this.newMrp = newMrp;
		this.mrpUomType = mrpUomType;
	}

	public void approveBatchChangeDetail(Status status, String rejectNote, RejectReason rejectReason , Integer  userId) {
		this.status = status;
		this.rejectNote = rejectNote;
		this.rejectReason = rejectReason;
		this.updatedDate = TimeZoneComponent.getDateByzone(0);
		//this.updatedBy = this.getUser();
		this.updatedByUser =userId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getNewBatchExpiry() {
		return newBatchExpiry;
	}

	public void setNewBatchExpiry(Date newBatchExpiry) {
		this.newBatchExpiry = newBatchExpiry;
	}

	public String getNewBatchNo() {
		return newBatchNo;
	}

	public void setNewBatchNo(String newBatchNo) {
		this.newBatchNo = newBatchNo;
	}

	public Double getNewMrp() {
		return newMrp;
	}

	public void setNewMrp(Double newMrp) {
		this.newMrp = newMrp;
	}

	public Date getOldBatchExpiry() {
		return oldBatchExpiry;
	}

	public void setOldBatchExpiry(Date oldBatchExpiry) {
		this.oldBatchExpiry = oldBatchExpiry;
	}

	public String getOldBatchNo() {
		return oldBatchNo;
	}

	public void setOldBatchNo(String oldBatchNo) {
		this.oldBatchNo = oldBatchNo;
	}

	public Double getOldMrp() {
		return oldMrp;
	}

	public void setOldMrp(Double oldMrp) {
		this.oldMrp = oldMrp;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public BatchExp getBatchExp() {
		return batchExp;
	}

	public void setBatchExp(BatchExp batchExp) {
		this.batchExp = batchExp;
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
}