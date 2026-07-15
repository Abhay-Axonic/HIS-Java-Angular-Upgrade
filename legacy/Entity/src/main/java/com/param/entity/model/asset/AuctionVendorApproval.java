package com.param.entity.model.asset;

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
import com.param.entity.model.master.Status;
import com.param.entity.model.master.User;

@Entity(name = "AuctionVendorApproval")
@Table(name = "t_auction_vendor_approval", schema = "assets")
public class AuctionVendorApproval extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_auction_id", nullable = false)
	private ItemAuction itemAuction;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private User approvedBy;
	
	@Column(name = "approved_on")
	private Date appprovedOn;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "auctionVendorApproval", cascade = CascadeType.ALL)
	private List<AuctionVendorApprovalDetail> auctionVendorApprovalDetail;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ItemAuction getItemAuction() {
		return itemAuction;
	}

	public void setItemAuction(ItemAuction itemAuction) {
		this.itemAuction = itemAuction;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getAppprovedOn() {
		return appprovedOn;
	}

	public void setAppprovedOn(Date appprovedOn) {
		this.appprovedOn = appprovedOn;
	}

	public List<AuctionVendorApprovalDetail> getAuctionVendorApprovalDetail() {
		return auctionVendorApprovalDetail;
	}

	public void setAuctionVendorApprovalDetail(List<AuctionVendorApprovalDetail> auctionVendorApprovalDetail) {
		this.auctionVendorApprovalDetail = auctionVendorApprovalDetail;
	}

	public AuctionVendorApprovalDetail addAuctionVendorApprovalDetail(AuctionVendorApprovalDetail auctionVendorApprovalDetail) {
		if (getAuctionVendorApprovalDetail() == null)
			this.auctionVendorApprovalDetail = new ArrayList<AuctionVendorApprovalDetail>();

		getAuctionVendorApprovalDetail().add(auctionVendorApprovalDetail);
		auctionVendorApprovalDetail.setAuctionVendorApproval(this);

		return auctionVendorApprovalDetail;
	}
	
	public AuctionVendorApproval() {
		super();
	}

	public AuctionVendorApproval(ItemAuction itemAuction, Status status) {
		super();
		this.itemAuction = itemAuction;
		this.status = status;
	}

}