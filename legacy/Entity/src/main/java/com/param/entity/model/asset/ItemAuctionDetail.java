package com.param.entity.model.asset;

import java.io.Serializable;
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

@Entity(name = "ItemAuctionDetail")
@Table(name = "t_item_auction_detail", schema = "assets")
public class ItemAuctionDetail extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_auction_id", nullable = false)
	private ItemAuction itemAuction;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auction_requisition_detail_id")
	private AuctionRequisitionDetail auctionRequisitionDetail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "itemAuctionDetail", cascade = CascadeType.ALL)
	private List<VendorAuctionMapper> vendorAuctionMapper;
	
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

	public AuctionRequisitionDetail getAuctionRequisitionDetail() {
		return auctionRequisitionDetail;
	}

	public void setAuctionRequisitionDetail(AuctionRequisitionDetail auctionRequisitionDetail) {
		this.auctionRequisitionDetail = auctionRequisitionDetail;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<VendorAuctionMapper> getVendorAuctionMapper() {
		return vendorAuctionMapper;
	}

	public void setVendorAuctionMapper(List<VendorAuctionMapper> vendorAuctionMapper) {
		this.vendorAuctionMapper = vendorAuctionMapper;
	}
	
	public VendorAuctionMapper addVendorAuctionMapper(VendorAuctionMapper vendorAuctionMapper) {
		if (this.vendorAuctionMapper == null)
			this.vendorAuctionMapper = new ArrayList<VendorAuctionMapper>();

		this.vendorAuctionMapper.add(vendorAuctionMapper);
		vendorAuctionMapper.setItemAuctionDetail(this);

		return vendorAuctionMapper;
	}
	
	public ItemAuctionDetail() {
		super();
	}

	public ItemAuctionDetail(ItemAuction itemAuction, AuctionRequisitionDetail auctionRequisitionDetail) {
		super();
		this.itemAuction = itemAuction;
		this.auctionRequisitionDetail = auctionRequisitionDetail;
	}

	public ItemAuctionDetail(Integer id) {
		super();
		this.id = id;
	}

	public ItemAuctionDetail(AuctionRequisitionDetail auctionRequisitionDetail, Status status) {

		this.auctionRequisitionDetail = auctionRequisitionDetail;
		this.status = status;
	}

}