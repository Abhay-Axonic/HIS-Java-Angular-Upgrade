package com.param.entity.model.linenAndLaundry;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.User;

@Entity
@Table(name="t_rejected_acceptance_detail", schema="linen_and_laundry")
public class RejectedAcceptanceDetail extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;


	@ManyToOne
	@JoinColumn(name="accepted_by")
	private User acceptedBy;

	@Column(name="accepted_on")
	private Timestamp acceptedOn;

	@Column(name="accepted_quantity")
	private Integer acceptedQuantity;

	@ManyToOne
	@JoinColumn(name="batch_id")
	private Batch batch;

	@Column(name="rejected_note")
	private String rejectedNote;

	@Column(name="rejected_quantity")
	private Integer rejectedQuantity;

	@ManyToOne
	@JoinColumn(name="rejected_reason_id")
	private RejectReason rejectedReason;

	@ManyToOne
	@JoinColumn(name="linen_issue_batch_mapper_id")
	private LinenIssueBatchMapper linenIssueBatchMapper;

	@ManyToOne
	@JoinColumn(name="return_detail_id")
	private LinenReturnDetail linenReturnDetail;

	@ManyToOne
	@JoinColumn(name="reject_id")
	private RejectedAcceptance rejectedAcceptance;

	public RejectedAcceptanceDetail() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getAcceptedOn() {
		return this.acceptedOn;
	}

	public void setAcceptedOn(Timestamp acceptedOn) {
		this.acceptedOn = acceptedOn;
	}

	public Integer getAcceptedQuantity() {
		return this.acceptedQuantity;
	}

	public void setAcceptedQuantity(Integer acceptedQuantity) {
		this.acceptedQuantity = acceptedQuantity;
	}

	public String getRejectedNote() {
		return this.rejectedNote;
	}

	public void setRejectedNote(String rejectedNote) {
		this.rejectedNote = rejectedNote;
	}

	public Integer getRejectedQuantity() {
		return this.rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
	}

	public LinenIssueBatchMapper getLinenIssueBatchMapper() {
		return linenIssueBatchMapper;
	}

	public void setLinenIssueBatchMapper(LinenIssueBatchMapper linenIssueBatchMapper) {
		this.linenIssueBatchMapper = linenIssueBatchMapper;
	}

	public LinenReturnDetail getLinenReturnDetail() {
		return linenReturnDetail;
	}

	public void setLinenReturnDetail(LinenReturnDetail linenReturnDetail) {
		this.linenReturnDetail = linenReturnDetail;
	}

	public RejectedAcceptance getRejectedAcceptance() {
		return rejectedAcceptance;
	}

	public void setRejectedAcceptance(RejectedAcceptance rejectedAcceptance) {
		this.rejectedAcceptance = rejectedAcceptance;
	}

	public User getAcceptedBy() {
		return acceptedBy;
	}

	public void setAcceptedBy(User acceptedBy) {
		this.acceptedBy = acceptedBy;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public RejectReason getRejectedReason() {
		return rejectedReason;
	}

	public void setRejectedReason(RejectReason rejectedReason) {
		this.rejectedReason = rejectedReason;
	}

	public RejectedAcceptanceDetail(Batch batch, String rejectedNote, Integer rejectedQuantity,
			RejectReason rejectedReason, LinenIssueBatchMapper linenIssueBatchMapper) {
		super();
		this.batch = batch;
		this.rejectedNote = rejectedNote;
		this.rejectedQuantity = rejectedQuantity;
		this.rejectedReason = rejectedReason;
		this.linenIssueBatchMapper = linenIssueBatchMapper;
	}

	public RejectedAcceptanceDetail(Batch batch, String rejectedNote,
			Integer rejectedQuantity, RejectReason rejectedReason, LinenReturnDetail linenReturnDetail) {
		super();
		this.batch = batch;
		this.rejectedNote = rejectedNote;
		this.rejectedQuantity = rejectedQuantity;
		this.rejectedReason = rejectedReason;
		this.linenReturnDetail = linenReturnDetail;
	}

}