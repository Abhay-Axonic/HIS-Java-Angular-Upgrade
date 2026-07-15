package com.param.entity.model.asset;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;

@Entity(name = "VendorApprovalAuctionMapper")
@Table(name = "t_vendor_approval_auction_mapper", schema = "assets")
public class VendorApprovalAuctionMapper extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auction_vendor_approval_detail_id", nullable = false)
	private AuctionVendorApprovalDetail auctionVendorApprovalDetail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_auction_mapper_id", nullable = false)
	private VendorAuctionMapper vendorAuctionMapper;
	
	@Column(name = "is_accept")
	private Boolean isAccept;
	
	@Column(name = "reject_note")
	private String rejectNote;
	
	@Column(name = "approved_quantity")
	private Integer approvedQuantity;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AuctionVendorApprovalDetail getAuctionVendorApprovalDetail() {
		return auctionVendorApprovalDetail;
	}

	public void setAuctionVendorApprovalDetail(AuctionVendorApprovalDetail auctionVendorApprovalDetail) {
		this.auctionVendorApprovalDetail = auctionVendorApprovalDetail;
	}

	public VendorAuctionMapper getVendorAuctionMapper() {
		return vendorAuctionMapper;
	}

	public void setVendorAuctionMapper(VendorAuctionMapper vendorAuctionMapper) {
		this.vendorAuctionMapper = vendorAuctionMapper;
	}

	public Boolean getIsAccept() {
		return isAccept;
	}

	public void setIsAccept(Boolean isAccept) {
		this.isAccept = isAccept;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public VendorApprovalAuctionMapper(VendorAuctionMapper vendorAuctionMapper) {
		super();
		this.vendorAuctionMapper = vendorAuctionMapper;
	}

	public VendorApprovalAuctionMapper() {
		super();
	}

}