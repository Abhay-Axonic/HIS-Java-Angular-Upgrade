package com.param.assetManagement.auctionRequisition.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AssetDetailResponse {

	protected Integer id;

	protected Integer assetId;

	protected String assetCode;

	protected String assetName;

	protected String serialNo;

	protected Integer manufacturingYear;

	protected Long batchId;

	protected String specification;

	protected Integer auctionQuantity;

	public AssetDetailResponse() {
		super();
	}

	public AssetDetailResponse(Integer id, Integer assetId, String assetCode, String assetName, String serialNo,
			Integer manufacturingYear, Long batchId) {
		super();
		this.id = id;
		this.assetId = assetId;
		this.assetCode = assetCode;
		this.assetName = assetName;
		this.serialNo = serialNo;
		this.manufacturingYear = manufacturingYear;
		this.batchId = batchId;
	}

	public AssetDetailResponse(Integer id, Integer assetId, String assetCode, String assetName, String serialNo,
			Integer manufacturingYear) {
		super();
		this.id = id;
		this.assetId = assetId;
		this.assetCode = assetCode;
		this.assetName = assetName;
		this.serialNo = serialNo;
		this.manufacturingYear = manufacturingYear;
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

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Integer getManufacturingYear() {
		return manufacturingYear;
	}

	public void setManufacturingYear(Integer manufacturingYear) {
		this.manufacturingYear = manufacturingYear;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Integer getAuctionQuantity() {
		return auctionQuantity;
	}

	public void setAuctionQuantity(Integer auctionQuantity) {
		this.auctionQuantity = auctionQuantity;
	}

}
