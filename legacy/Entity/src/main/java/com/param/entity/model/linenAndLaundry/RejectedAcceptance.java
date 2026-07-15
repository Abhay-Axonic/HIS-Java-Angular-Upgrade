package com.param.entity.model.linenAndLaundry;

import java.io.Serializable;
import java.sql.Timestamp;
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
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;

@Entity
@Table(name="t_rejected_acceptance", schema="linen_and_laundry")
public class RejectedAcceptance extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="from_store_id")
	private Store fromStore;

	@ManyToOne
	@JoinColumn(name="rejected_by")
	private User rejectedBy;

	@Column(name="rejected_on")
	private Timestamp rejectedOn;

	private String remark;

	@Column(name="return_no")
	private String returnNo;

	@ManyToOne
	@JoinColumn(name="status_id")
	private Status status;

	@ManyToOne
	@JoinColumn(name="to_store_id")
	private Store toStore;
	
	@ManyToOne
	@JoinColumn(name="linen_issue_id")
	private LinenIssue linenIssue;

	@ManyToOne
	@JoinColumn(name="return_id")
	private LinenReturn linenReturn;

	@OneToMany(mappedBy="rejectedAcceptance", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<RejectedAcceptanceDetail> rejectedAcceptanceDetails;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setAddedDate(Timestamp addedDate) {
		this.addedDate = addedDate;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Timestamp getRejectedOn() {
		return this.rejectedOn;
	}

	public void setRejectedOn(Timestamp rejectedOn) {
		this.rejectedOn = rejectedOn;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public LinenIssue getLinenIssue() {
		return linenIssue;
	}

	public void setLinenIssue(LinenIssue linenIssue) {
		this.linenIssue = linenIssue;
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

	public Store getFromStore() {
		return fromStore;
	}

	public void setFromStore(Store fromStore) {
		this.fromStore = fromStore;
	}

	public User getRejectedBy() {
		return rejectedBy;
	}

	public void setRejectedBy(User rejectedBy) {
		this.rejectedBy = rejectedBy;
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

	public RejectedAcceptanceDetail addRejectedAcceptanceDetail(RejectedAcceptanceDetail rejectedAcceptanceDetails) {
		if (getRejectedAcceptanceDetails() == null)
			this.rejectedAcceptanceDetails = new ArrayList<RejectedAcceptanceDetail>();
		
		getRejectedAcceptanceDetails().add(rejectedAcceptanceDetails);
		rejectedAcceptanceDetails.setRejectedAcceptance(this);

		return rejectedAcceptanceDetails;
	}
	
	public RejectedAcceptanceDetail removeRejectedAcceptanceDetail(RejectedAcceptanceDetail rejectedAcceptanceDetail) {
		getRejectedAcceptanceDetails().remove(rejectedAcceptanceDetail);
		rejectedAcceptanceDetail.setRejectedAcceptance(null);

		return rejectedAcceptanceDetail;
	}

	public RejectedAcceptance() {
		super();
	}

	public RejectedAcceptance(String returnNo, Store fromStore, User rejectedBy, Timestamp rejectedOn, Status status,
			Store toStore, LinenIssue linenIssue) {
		super();
		this.returnNo = returnNo;
		this.fromStore = fromStore;
		this.rejectedBy = rejectedBy;
		this.rejectedOn = rejectedOn;
		this.status = status;
		this.toStore = toStore;
		this.linenIssue = linenIssue;
	}

	public RejectedAcceptance(Store fromStore, User rejectedBy, Timestamp rejectedOn, String returnNo,
			Status status, Store toStore, LinenReturn linenReturn) {
		super();
		this.fromStore = fromStore;
		this.rejectedBy = rejectedBy;
		this.rejectedOn = rejectedOn;
		this.returnNo = returnNo;
		this.status = status;
		this.toStore = toStore;
		this.linenReturn = linenReturn;
	}
	
}