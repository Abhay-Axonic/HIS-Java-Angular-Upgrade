package com.param.entity.model.asset;

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

@Entity(name = "AuctionRequisition")
@Table(name = "t_auction_requisition", schema = "assets")
public class AuctionRequisition extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "auction_requisition_no")
	private String auctionRequisitionNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = true)
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approval_status_id", nullable = false)
	private Status approvalStatus;

	@Column(name = "remark")
	private String remark;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	protected User approvedBy;

	@Column(name = "approved_on")
	private Date approvedOn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;

	@Column(name = "cancel_note")
	private String cancelNote;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "auctionRequisition", cascade = CascadeType.ALL)
	private List<AuctionRequisitionDetail> auctionRequisitionDetails;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "auctionRequision", cascade = CascadeType.ALL)
	private List<ItemAuction> itemAuctions;

	public AuctionRequisition() {
		super();
	}

	public AuctionRequisition(Integer id) {
		super();
		this.id = id;
	}

	public AuctionRequisition(Store store, String remark, Status status, String auctionRequisitionNo,
			Status approvalStatus) {
		this.store = store;
		this.remark = remark;
		this.status = status;
		this.auctionRequisitionNo = auctionRequisitionNo;
		this.approvalStatus = approvalStatus;
	}

	public AuctionRequisition(Integer id, Status approvalStatus) {
		this.id = id;
		this.approvalStatus = approvalStatus;
	}

	public AuctionRequisition(Status approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAuctionRequisitionNo() {
		return auctionRequisitionNo;
	}

	public void setAuctionRequisitionNo(String auctionRequisitionNo) {
		this.auctionRequisitionNo = auctionRequisitionNo;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<AuctionRequisitionDetail> getAuctionRequisitionDetails() {
		return auctionRequisitionDetails;
	}

	public void setAuctionRequisitionDetails(List<AuctionRequisitionDetail> auctionRequisitionDetails) {
		this.auctionRequisitionDetails = auctionRequisitionDetails;
	}

	public AuctionRequisitionDetail addAuctionRequisitionDetailList(AuctionRequisitionDetail auctionRequisitionDetail) {
		if (getAuctionRequisitionDetails() == null)
			this.auctionRequisitionDetails = new ArrayList<AuctionRequisitionDetail>();

		getAuctionRequisitionDetails().add(auctionRequisitionDetail);
		auctionRequisitionDetail.setAuctionRequisition(this);

		return auctionRequisitionDetail;
	}

	public Status getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Status approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public void approveAuctionRequisition(Status status, Status approvalStatus) {
		this.status = status;
		this.approvalStatus = approvalStatus;
	}

	public List<ItemAuction> getItemAuctions() {
		return itemAuctions;
	}

	public void setItemAuctions(List<ItemAuction> itemAuctions) {
		this.itemAuctions = itemAuctions;
	}

	public void updateAuctionRequisition(Store store, String remark) {
		this.store = store;
		this.remark = remark;
	}

}
