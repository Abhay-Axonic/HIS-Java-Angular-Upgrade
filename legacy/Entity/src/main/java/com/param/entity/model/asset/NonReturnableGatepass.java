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
import com.param.entity.model.master.RequestType;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;

@Entity(name = "NonReturnableGatepass")
@Table(name = "t_non_returnable_gatepass", schema = "assets")
public class NonReturnableGatepass extends BaseEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "non_ret_gp_no")
	private String nonRetGpNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id", nullable = false)
	private Vendor vendor;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "request_type_id", nullable = false)
	private RequestType requestType;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "auction_requisition_id")
	private AuctionRequisition auctionRequisition;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approval_status_id")
	private Status approvalStatus;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;
	
	@Column(name = "cancel_note")
	private String cancelNote;
	
	@Column(name = "approved_on")
	private Date approvedOn;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private User approvedBy;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "nonReturnableGatepass", cascade = CascadeType.ALL)
	private List<NonReturnableGatepassDetail> nonReturnableGatepassDetail;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public String getNonRetGpNo() {
		return nonRetGpNo;
	}

	public void setNonRetGpNo(String nonRetGpNo) {
		this.nonRetGpNo = nonRetGpNo;
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

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public AuctionRequisition getAuctionRequisition() {
		return auctionRequisition;
	}

	public void setAuctionRequisition(AuctionRequisition auctionRequisition) {
		this.auctionRequisition = auctionRequisition;
	}

	public Status getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Status approvalStatus) {
		this.approvalStatus = approvalStatus;
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

	public Date getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public List<NonReturnableGatepassDetail> getNonReturnableGatepassDetail() {
		return nonReturnableGatepassDetail;
	}

	public void setNonReturnableGatepassDetail(List<NonReturnableGatepassDetail> nonReturnableGatepassDetail) {
		this.nonReturnableGatepassDetail = nonReturnableGatepassDetail;
	}

	public NonReturnableGatepassDetail addNonReturnableGatepassDetail(NonReturnableGatepassDetail nonReturnableGatepassDetail) {
		if (getNonReturnableGatepassDetail() == null)
			this.nonReturnableGatepassDetail = new ArrayList<NonReturnableGatepassDetail>();

		getNonReturnableGatepassDetail().add(nonReturnableGatepassDetail);
		nonReturnableGatepassDetail.setNonReturnableGatepass(this);

		return nonReturnableGatepassDetail;
	}
	
	public NonReturnableGatepass() {
		super();
	}

	public NonReturnableGatepass(String nonRetGpNo, Store store, Vendor vendor, Status status, RequestType requestType) {
		super();
		this.nonRetGpNo = nonRetGpNo;
		this.store = store;
		this.vendor = vendor;
		this.status = status;
		this.requestType = requestType;
	}

}