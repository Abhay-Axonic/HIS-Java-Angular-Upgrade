package com.param.assetManagement.auctionRequisition.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.AccessoriesDetails;

@JsonInclude(Include.NON_NULL)
public class ItemAuctionRequisitionResponse {

	protected Integer id;

	protected Integer assetId;

	protected String assetCode;

	protected String assetName;

	protected String modelNo;

	protected String grnNo;

	protected Integer grnId;

	protected Date grnDate;

	protected String lotNo;

	protected Double cop;

	protected String serialNo;

	protected Integer vendorId;

	protected String vendorName;

	protected Integer manufactureYear;

	protected Long availableQty;

	protected Integer auctionQty;

	protected Long batchId;

	protected String remark;

	protected Integer statusId;

	protected List<AssetDetailResponse> accessories;

	protected List<AccessoriesDetails> accessoriesDetails;

	public ItemAuctionRequisitionResponse() {
		super();
	}

	public ItemAuctionRequisitionResponse(Integer id, Integer assetId, String assetCode, String assetName,
			String modalNo, String grnNo, Integer grnId, String serialNo, Integer vendorId, String vendorName,
			Integer manufactureYear, Integer auctionQty, Long availableQty, List<AssetDetailResponse> assetDetails,
			String remark, Long batchId) {
		super();
		this.id = id;
		this.assetId = assetId;
		this.assetCode = assetCode;
		this.assetName = assetName;
		this.modelNo = modalNo;
		this.grnNo = grnNo;
		this.grnId = grnId;
		this.serialNo = serialNo;
		this.vendorId = vendorId;
		this.vendorName = vendorName;
		this.manufactureYear = manufactureYear;
		this.auctionQty = auctionQty;
		this.availableQty = availableQty;
		this.accessories = accessories;
		this.remark = remark;
		this.batchId = batchId;
	}

	public ItemAuctionRequisitionResponse(Integer id, Integer assetId, String assetCode, String assetName,
			String modelNo, String grnNo, Integer grnId, Date grnDate, String lotNo, Double cop, String serialNo,
			Integer vendorId, String vendorName, Integer manufactureYear, Integer auctionQty,
			List<AssetDetailResponse> assetDetails, Integer statusId) {
		super();
		this.id = id;
		this.assetId = assetId;
		this.assetCode = assetCode;
		this.assetName = assetName;
		this.modelNo = modelNo;
		this.grnNo = grnNo;
		this.grnId = grnId;
		this.grnDate = grnDate;
		this.lotNo = lotNo;
		this.cop = cop;
		this.serialNo = serialNo;
		this.vendorId = vendorId;
		this.vendorName = vendorName;
		this.manufactureYear = manufactureYear;
		this.auctionQty = auctionQty;
		this.accessories = accessories;
		this.statusId = statusId;
	}

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

	public String getModelNo() {
		return modelNo;
	}

	public void setModelNo(String modalNo) {
		this.modelNo = modalNo;
	}

	public String getGrnNo() {
		return grnNo;
	}

	public void setGrnNo(String grnNo) {
		this.grnNo = grnNo;
	}

	public Integer getGrnId() {
		return grnId;
	}

	public void setGrnId(Integer grnId) {
		this.grnId = grnId;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
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

	public Integer getManufactureYear() {
		return manufactureYear;
	}

	public void setManufactureYear(Integer manufactureYear) {
		this.manufactureYear = manufactureYear;
	}

	public Long getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(Long availableQty) {
		this.availableQty = availableQty;
	}

	public Integer getAuctionQty() {
		return auctionQty;
	}

	public void setAuctionQty(Integer auctionQty) {
		this.auctionQty = auctionQty;
	}

	public List<AssetDetailResponse> getAccessories() {
		return accessories;
	}

	public void setAccessories(List<AssetDetailResponse> accessories) {
		this.accessories = accessories;
	}

	public void addAssetDetail(AssetDetailResponse assetDetailResponse) {
		if (this.accessories == null)
			this.accessories = new ArrayList<AssetDetailResponse>();

		this.accessories.add(assetDetailResponse);
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getGrnDate() {
		return grnDate;
	}

	public void setGrnDate(Date grnDate) {
		this.grnDate = grnDate;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public List<AccessoriesDetails> getAccessoriesDetails() {
		return accessoriesDetails;
	}

	public void setAccessoriesDetails(List<AccessoriesDetails> accessoriesDetails) {
		this.accessoriesDetails = accessoriesDetails;
	}

}
