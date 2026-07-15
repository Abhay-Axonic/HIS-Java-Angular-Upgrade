package com.param.entity.model.asset;

import java.io.Serializable;
import java.util.Date;

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
import com.param.entity.model.master.Vendor;

@Entity(name = "VendorAuctionMapper")
@Table(name = "t_vendor_auction_mapper", schema = "assets")
public class VendorAuctionMapper extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id", nullable = false)
	private Vendor vendor;

	@Column(name = "vendor_amount")
	private Double vendorAmount;
	
	@Column(name = "payment_terms")
	private String paymentTerms;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_auction_detail_id", nullable = false)
	private ItemAuctionDetail itemAuctionDetail;
	
	@Column(name = "reference_number")
	private String referenceNumber;
	
	@Column(name = "reference_date")
	private Date referenceDate;

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

	public Double getVendorAmount() {
		return vendorAmount;
	}

	public void setVendorAmount(Double vendorAmount) {
		this.vendorAmount = vendorAmount;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public ItemAuctionDetail getItemAuctionDetail() {
		return itemAuctionDetail;
	}

	public void setItemAuctionDetail(ItemAuctionDetail itemAuctionDetail) {
		this.itemAuctionDetail = itemAuctionDetail;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public Date getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(Date referenceDate) {
		this.referenceDate = referenceDate;
	}

	public VendorAuctionMapper() {
		super();
	}

	public VendorAuctionMapper(Vendor vendor, Double vendorAmount, String paymentTerms, String referenceNumber,
			Date referenceDate) {
		super();
		this.vendor = vendor;
		this.vendorAmount = vendorAmount;
		this.paymentTerms = paymentTerms;
		this.referenceNumber = referenceNumber;
		this.referenceDate = referenceDate;
	}
	
	public void updateVendorAuctionMapper(Vendor vendor, Double vendorAmount, String paymentTerms, String referenceNumber,
			Date referenceDate) {
		this.vendor = vendor;
		this.vendorAmount = vendorAmount;
		this.paymentTerms = paymentTerms;
		this.referenceNumber = referenceNumber;
		this.referenceDate = referenceDate;
	}

	public VendorAuctionMapper(Integer id) {
		super();
		this.id = id;
	}
	
}