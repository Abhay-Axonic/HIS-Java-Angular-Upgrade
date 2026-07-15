package com.param.assetManagement.itemForAuction.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class ItemAuctionDetails {
	
	private Integer id;
	
	private Integer auctionRequisitionDetailId;
	
	private Integer auctionVendorApprovalId;
	
	private Integer itemAuctionDetailId;
	
	private Integer assetId;
	
    private String assetCode;
    
    private String assetName;
    
    private String serialNo;
    
    private String lotNo;
    
    private String modelNo;
    
    private Integer vendorId;
    
    private String vendorName;
    
    private Integer grnId;
    
    private String grnNo;
    
    private Date grnDate;
    
    private Double cop;
    
    private Integer quantity;
    
    private List<ItemAuctionAccessories> accessories;
    
    private List<VendorDetailsResponse> vendorDetails;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAssetId() {
		return assetId;
	}

	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public String getModelNo() {
		return modelNo;
	}

	public void setModelNo(String modelNo) {
		this.modelNo = modelNo;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Integer getGrnId() {
		return grnId;
	}

	public void setGrnId(Integer grnId) {
		this.grnId = grnId;
	}

	public String getGrnNo() {
		return grnNo;
	}

	public void setGrnNo(String grnNo) {
		this.grnNo = grnNo;
	}

	public Date getGrnDate() {
		return grnDate;
	}

	public void setGrnDate(Date grnDate) {
		this.grnDate = grnDate;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public List<ItemAuctionAccessories> getAccessories() {
		return accessories;
	}

	public void setAccessories(List<ItemAuctionAccessories> accessories) {
		this.accessories = accessories;
	}

	public Integer getAuctionRequisitionDetailId() {
		return auctionRequisitionDetailId;
	}

	public void setAuctionRequisitionDetailId(Integer auctionRequisitionDetail) {
		this.auctionRequisitionDetailId = auctionRequisitionDetail;
	}

	public List<VendorDetailsResponse> getVendorDetails() {
		return vendorDetails;
	}

	public void setVendorDetails(List<VendorDetailsResponse> vendorDetails) {
		this.vendorDetails = vendorDetails;
	}

	public Integer getAuctionVendorApprovalId() {
		return auctionVendorApprovalId;
	}

	public void setAuctionVendorApprovalId(Integer auctionVendorApprovalId) {
		this.auctionVendorApprovalId = auctionVendorApprovalId;
	}

	public Integer getItemAuctionDetailId() {
		return itemAuctionDetailId;
	}

	public void setItemAuctionDetailId(Integer itemAuctionDetailId) {
		this.itemAuctionDetailId = itemAuctionDetailId;
	}

	public ItemAuctionDetails() {
		super();
	}

	public ItemAuctionDetails(Integer id, Integer auctionRequisitionDetailId, Integer assetId, String assetCode, String assetName, String serialNo,
			String lotNo, String modelNo, Integer grnId, String grnNo, Date grnDate, Double cop,
			Integer quantity) {
		super();
		this.id = id;
		this.auctionRequisitionDetailId = auctionRequisitionDetailId;
		this.assetId = assetId;
		this.assetCode = assetCode;
		this.assetName = assetName;
		this.serialNo = serialNo;
		this.lotNo = lotNo;
		this.modelNo = modelNo;
		this.grnId = grnId;
		this.grnNo = grnNo;
		this.grnDate = grnDate;
		this.cop = cop;
		this.quantity = quantity;
	}

	public ItemAuctionDetails(Integer id, Integer auctionVendorApprovalId, Integer itemAuctionDetailId, Integer assetId,
			String assetCode, String assetName) {
		super();
		this.id = id;
		this.auctionVendorApprovalId = auctionVendorApprovalId;
		this.itemAuctionDetailId = itemAuctionDetailId;
		this.assetId = assetId;
		this.assetCode = assetCode;
		this.assetName = assetName;
	}
	
}