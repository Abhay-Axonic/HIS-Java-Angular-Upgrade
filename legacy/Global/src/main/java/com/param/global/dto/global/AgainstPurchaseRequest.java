package com.param.global.dto.global;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.service.global.model.CookieObjectDto;

@JsonInclude(Include.NON_NULL)
public class AgainstPurchaseRequest extends CookieObjectDto {
	
    private Date fromDate;
	
	private Date toDate;
	
	private Integer screenId;
	private String vqrNumber;
	private String qrNo;
	
	private int isPoOrGrn;
	
	public String getQrNo() {
		return qrNo;
	}


	public void setQrNo(String qrNo) {
		this.qrNo = qrNo;
	}


	public String getVqrNumber() {
		return vqrNumber;
	}


	public void setVqrNumber(String vqrNumber) {
		this.vqrNumber = vqrNumber;
	}

//	protected String prNo;
	protected String prNo;
	
	protected Integer prId;
	
	protected Integer assetTypeId;	
	
	protected Integer storeId;
	
	protected Integer vendorId;
	
	private List<Integer> itemIds;
	
	
	public AgainstPurchaseRequest() {
		super();
	}


	public Date getFromDate() {
		return fromDate;
	}


	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}


	public Date getToDate() {
		return toDate;
	}


	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getPrNo() {
		return prNo;
	}


	public void setPrNo(String prNo) {
		this.prNo = prNo;
	}


	public Integer getStoreId() {
		return storeId;
	}


	public void setStoreId(Integer storeId) { 
		this.storeId = storeId;
	}


	public List<Integer> getItemIds() {
		return itemIds;
	}


	public void setItemIds(List<Integer> itemIds) {
		this.itemIds = itemIds;
	}


	public Integer getPrId() {
		return prId;
	}


	public void setPrId(Integer prId) {
		this.prId = prId;
	}


	public Integer getAssetTypeId() {
		return assetTypeId;
	}


	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}


	public Integer getVendorId() {
		return vendorId;
	}


	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}


	public Integer getScreenId() {
		return screenId;
	}


	public void setScreenId(Integer screenId) {
		this.screenId = screenId;
	}


	public int getIsPoOrGrn() {
		return isPoOrGrn;
	}


	public void setIsPoOrGrn(int isPoOrGrn) {
		this.isPoOrGrn = isPoOrGrn;
	}
	
}
