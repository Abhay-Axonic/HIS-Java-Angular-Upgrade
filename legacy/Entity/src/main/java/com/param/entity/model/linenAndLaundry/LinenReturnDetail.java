package com.param.entity.model.linenAndLaundry;

import java.io.Serializable;
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
import com.param.entity.model.master.Status;



@Entity
@Table(name="t_linen_return_detail", schema="linen_and_laundry")
public class LinenReturnDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name="accepted_quantity")
	private Integer acceptedQuantity;

	@ManyToOne
	@JoinColumn(name="batch_id")
	private Batch batch;

	@Column(name="reject_note")
	private String rejectNote;

	@Column(name="reject_quantity")
	private Integer rejectQuantity;

	@ManyToOne
	@JoinColumn(name="reject_reason_id")
	private RejectReason rejectReason;

	@Column(name="return_quantity")
	private Integer returnQuantity;

	@ManyToOne
	@JoinColumn(name="return_reason_id")
	private ReturnReason returnReason;

	@ManyToOne
	@JoinColumn(name="status_id")
	private Status status;

	@ManyToOne
	@JoinColumn(name="return_id")
	private LinenReturn linenReturn;

	@OneToMany(mappedBy="linenReturnDetail", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<RejectedAcceptanceDetail> rejectedAcceptanceDetails;

	public LinenReturnDetail() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAcceptedQuantity() {
		return this.acceptedQuantity;
	}

	public void setAcceptedQuantity(Integer acceptedQuantity) {
		this.acceptedQuantity = acceptedQuantity;
	}

	public String getRejectNote() {
		return this.rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public Integer getRejectQuantity() {
		return this.rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}

	public Integer getReturnQuantity() {
		return this.returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public LinenReturn getLinenReturn() {
		return linenReturn;
	}

	public void setLinenReturn(LinenReturn linenReturn) {
		this.linenReturn = linenReturn;
	}

	public List<RejectedAcceptanceDetail> getRejectedAcceptanceDetails() {
		return rejectedAcceptanceDetails;
	}

	public void setRejectedAcceptanceDetails(List<RejectedAcceptanceDetail> rejectedAcceptanceDetails) {
		this.rejectedAcceptanceDetails = rejectedAcceptanceDetails;
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

	public ReturnReason getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(ReturnReason returnReason) {
		this.returnReason = returnReason;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LinenReturnDetail(Batch batch, Integer returnQuantity, ReturnReason returnReason, Status status) {
		super();
		this.batch = batch;
		this.returnQuantity = returnQuantity;
		this.returnReason = returnReason;
		this.status = status;
	}

	public LinenReturnDetail(Integer id) {
		super();
		this.id = id;
	}

	public void updateLinenReturnDetail(Integer acceptedQuantity, Batch batch, String rejectNote, Integer rejectQuantity,
			RejectReason rejectReason, Status status) {
		this.acceptedQuantity = acceptedQuantity;
		this.batch = batch;
		this.rejectNote = rejectNote;
		this.rejectQuantity = rejectQuantity;
		this.rejectReason = rejectReason;
		this.status = status;
	}

	public void updateLinenReturnDetail(Integer returnQuantity, ReturnReason returnReason) {
		this.returnQuantity = returnQuantity;
		this.returnReason = returnReason;
	}

	public RejectedAcceptanceDetail addRejectedAcceptanceDetail(RejectedAcceptanceDetail rejectedAcceptanceDetails) {
		getRejectedAcceptanceDetails().add(rejectedAcceptanceDetails);
		rejectedAcceptanceDetails.setLinenReturnDetail(this);

		return rejectedAcceptanceDetails;
	}

	public RejectedAcceptanceDetail removeRejectedAcceptanceDetail(RejectedAcceptanceDetail rejectedAcceptanceDetails) {
		getRejectedAcceptanceDetails().remove(rejectedAcceptanceDetails);
		rejectedAcceptanceDetails.setLinenReturnDetail(null);

		return rejectedAcceptanceDetails;
	}

}