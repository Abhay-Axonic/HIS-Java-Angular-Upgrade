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
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;

@Entity(name = "ItemAuction")
@Table(name = "t_item_auction", schema = "assets")
public class ItemAuction extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "request_no")
	private String requestNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auction_requisition_id", nullable = false)
	private AuctionRequisition auctionRequision;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private User approvedBy;
	
	@Column(name = "approved_on")
	private Date appprovedOn;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "itemAuction", cascade = CascadeType.ALL)
	private List<ItemAuctionDetail> itemAuctionDetail;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "itemAuction", cascade = CascadeType.ALL)
	private List<AuctionVendorApproval> auctionVendorApproval;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public List<ItemAuctionDetail> getItemAuctionDetail() {
		return itemAuctionDetail;
	}

	public void setItemAuctionDetail(List<ItemAuctionDetail> itemAuctionDetail) {
		this.itemAuctionDetail = itemAuctionDetail;
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

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public AuctionRequisition getAuctionRequision() {
		return auctionRequision;
	}

	public void setAuctionRequision(AuctionRequisition auctionRequision) {
		this.auctionRequision = auctionRequision;
	}

	public List<AuctionVendorApproval> getAuctionVendorApproval() {
		return auctionVendorApproval;
	}

	public void setAuctionVendorApproval(List<AuctionVendorApproval> auctionVendorApproval) {
		this.auctionVendorApproval = auctionVendorApproval;
	}
	
	public AuctionVendorApproval addAuctionVendorApproval(AuctionVendorApproval auctionVendorApproval) {
		if (getAuctionVendorApproval() == null)
			this.auctionVendorApproval = new ArrayList<AuctionVendorApproval>();

		getAuctionVendorApproval().add(auctionVendorApproval);
		auctionVendorApproval.setItemAuction(this);

		return auctionVendorApproval;
	}
	
	public ItemAuction() {
		super();
	}

	public ItemAuction(String requestNo, Status status, Store store) {
		super();
		this.requestNo = requestNo;
		this.status = status;
		this.store = store;
	}

	public ItemAuction(Integer id) {
		super();
		this.id = id;
	}

	public ItemAuction(String requestNo, Status status, Store store, AuctionRequisition auctionRequisition) {
		
		this.requestNo = requestNo;
		this.status = status;
		this.store = store;
		this.auctionRequision = auctionRequisition;
	}

	public ItemAuctionDetail addItemAuctionRequisiDetail(ItemAuctionDetail itemAuctionDetail) {

		if (getItemAuctionDetail() == null)
			this.itemAuctionDetail = new ArrayList<ItemAuctionDetail>();

		getItemAuctionDetail().add(itemAuctionDetail);
		itemAuctionDetail.setItemAuction(this);
						
		return itemAuctionDetail;
	}

}