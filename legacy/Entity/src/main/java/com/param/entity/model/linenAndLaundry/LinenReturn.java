package com.param.entity.model.linenAndLaundry;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="t_linen_return", schema="linen_and_laundry")
public class LinenReturn extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="approval_status_id")
	private Status approvalStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="approved_by")
	private User approvedBy;

	@Column(name="approved_on")
	private Timestamp approvedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="carrier_id")
	private User carrier;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="from_store_id", nullable = false)
	private Store fromStore;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="cancel_reason_id")
	private CancelReason cancelReason;
	
	@Column(name="cancel_note")
	private String cancelNote;

	@Column(name="return_no")
	private String returnNo;
	
	@Column(name="remark")
	private String remark;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="status_id")
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="to_store_id", nullable = false)
	private Store toStore;

	@OneToMany(mappedBy="linenReturn", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<LinenReturnDetail> linenReturnDetails;

	@OneToMany(mappedBy="linenReturn", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<RejectedAcceptance> rejectedAcceptances;

	public LinenReturn() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setAddedDate(Timestamp addedDate) {
		this.addedDate = addedDate;
	}

	public Timestamp getApprovedOn() {
		return this.approvedOn;
	}

	public void setApprovedOn(Timestamp approvedOn) {
		this.approvedOn = approvedOn;
	}

	public String getReturnNo() {
		return this.returnNo;
	}

	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public List<LinenReturnDetail> getLinenReturnDetails() {
		return linenReturnDetails;
	}

	public void setLinenReturnDetails(List<LinenReturnDetail> linenReturnDetails) {
		this.linenReturnDetails = linenReturnDetails;
	}

	public List<RejectedAcceptance> getRejectedAcceptances() {
		return rejectedAcceptances;
	}

	public void setRejectedAcceptances(List<RejectedAcceptance> rejectedAcceptances) {
		this.rejectedAcceptances = rejectedAcceptances;
	}

	public Status getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Status approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public User getCarrier() {
		return carrier;
	}

	public void setCarrier(User carrier) {
		this.carrier = carrier;
	}

	public Store getFromStore() {
		return fromStore;
	}

	public void setFromStore(Store fromStore) {
		this.fromStore = fromStore;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Store getToStore() {
		return toStore;
	}

	public void setToStore(Store toStore) {
		this.toStore = toStore;
	}

	public CancelReason getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(CancelReason cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public LinenReturn(Status approvalStatus, User carrier, Store fromStore, String returnNo, String remark,
			Status status, Store toStore) {
		super();
		this.approvalStatus = approvalStatus;
		this.carrier = carrier;
		this.fromStore = fromStore;
		this.returnNo = returnNo;
		this.remark = remark;
		this.status = status;
		this.toStore = toStore;
	}

	public LinenReturn(Integer id) {
		super();
		this.id = id;
	}

	public void updateLinenReturn(User carrier, String remark) {
		this.carrier = carrier;
		this.remark = remark;
	}

	public LinenReturnDetail addLinenReturnDetail(LinenReturnDetail linenReturnDetails) {
		if (getLinenReturnDetails() == null)
			this.linenReturnDetails = new ArrayList<LinenReturnDetail>();
		
		getLinenReturnDetails().add(linenReturnDetails);
		linenReturnDetails.setLinenReturn(this);
	
		return linenReturnDetails;
	}
	public RejectedAcceptance removeRejectedAcceptance(RejectedAcceptance rejectedAcceptance) {
		getRejectedAcceptances().remove(rejectedAcceptance);
		rejectedAcceptance.setLinenReturn(null);

		return rejectedAcceptance;
	}

}