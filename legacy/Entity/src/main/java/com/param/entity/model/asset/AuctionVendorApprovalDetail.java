package com.param.entity.model.asset;

import java.io.Serial;
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

@Entity(name = "AuctionVendorApprovalDetail")
@Table(name = "t_auction_vendor_approval_detail", schema = "assets")
public class AuctionVendorApprovalDetail extends BaseEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_auction_detail_id", nullable = false)
	private ItemAuctionDetail itemAuctionDetail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auction_vendor_approval_id", nullable = false)
	private AuctionVendorApproval auctionVendorApproval;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "auctionVendorApprovalDetail", cascade = CascadeType.ALL)
	private List<VendorApprovalAuctionMapper> vendorApprovalAuctionMapper;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ItemAuctionDetail getItemAuctionDetail() {
		return itemAuctionDetail;
	}

	public void setItemAuctionDetail(ItemAuctionDetail itemAuctionDetail) {
		this.itemAuctionDetail = itemAuctionDetail;
	}

	public AuctionVendorApproval getAuctionVendorApproval() {
		return auctionVendorApproval;
	}

	public void setAuctionVendorApproval(AuctionVendorApproval auctionVendorApproval) {
		this.auctionVendorApproval = auctionVendorApproval;
	}

	public List<VendorApprovalAuctionMapper> getVendorApprovalAuctionMapper() {
		return vendorApprovalAuctionMapper;
	}

	public void setVendorApprovalAuctionMapper(List<VendorApprovalAuctionMapper> vendorApprovalAuctionMapper) {
		this.vendorApprovalAuctionMapper = vendorApprovalAuctionMapper;
	}

	public VendorApprovalAuctionMapper addVendorApprovalAuctionMapper(VendorApprovalAuctionMapper vendorApprovalAuctionMapper) {
		if (getVendorApprovalAuctionMapper() == null)
			this.vendorApprovalAuctionMapper = new ArrayList<VendorApprovalAuctionMapper>();

		getVendorApprovalAuctionMapper().add(vendorApprovalAuctionMapper);
		vendorApprovalAuctionMapper.setAuctionVendorApprovalDetail(this);

		return vendorApprovalAuctionMapper;
	}
	
	public AuctionVendorApprovalDetail() {
		super();
	}

	public AuctionVendorApprovalDetail(ItemAuctionDetail itemAuctionDetail) {
		super();
		this.itemAuctionDetail = itemAuctionDetail;
	}

	public AuctionVendorApprovalDetail(Integer id) {
		super();
		this.id = id;
	}
	
}