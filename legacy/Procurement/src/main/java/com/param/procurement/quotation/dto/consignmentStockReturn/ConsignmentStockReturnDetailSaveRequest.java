package com.param.procurement.quotation.dto.consignmentStockReturn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.param.global.dto.global.OtherChargesResponse;
import com.param.global.dto.global.TaxDropDown;
import com.param.service.global.model.CookieObjectDto;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsignmentStockReturnDetailSaveRequest extends CookieObjectDto{
	
		protected Integer id;
		
		protected Integer grnId;

		protected Integer assetTypeId;

		protected String assetType;

		protected Integer grnTypeId;

		protected String grnType;
		
		protected String grnNumber;
		
		protected Integer statusId;

		protected String status;

		protected Integer approvalStatusId;

		protected String approvalStatus;

		protected Integer storeId;

		protected String storeName;

		protected Boolean isConsignment;

		protected Double totalAmount;
		protected Double totalTaxAmount;
		protected Double totalNetAmount;

		protected Integer vendorId;
		

		

		protected String vendorName;

		protected String remark;

		protected String addedBy;

		protected List<ConsignmentStockReturnDetailResponse> items;
		
		protected List<OtherChargesResponse> otherChargesList;

		protected List<TaxDropDown> taxList;
		
		//use for save response
		protected Date grnDate;

		public Date getGrnDate() {
			return grnDate;
		}

		public void setGrnDate(Date grnDate) {
			this.grnDate = grnDate;
		}
		// end for response

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}
		
		public Integer getGrnId() {
			return grnId;
		}
		
		public void setGrnId(Integer grnId) {
			this.grnId = grnId;
		}

		public Integer getAssetTypeId() {
			return assetTypeId;
		}

		public void setAssetTypeId(Integer assetTypeId) {
			this.assetTypeId = assetTypeId;
		}

		public String getAssetType() {
			return assetType;
		}

		public void setAssetType(String assetType) {
			this.assetType = assetType;
		}

		public Integer getGrnTypeId() {
			return grnTypeId;
		}

		public void setGrnTypeId(Integer grnTypeId) {
			this.grnTypeId = grnTypeId;
		}

		public String getGrnType() {
			return grnType;
		}

		public void setGrnType(String grnType) {
			this.grnType = grnType;
		}

		public String getGrnNumber() {
			return grnNumber;
		}

		public void setGrnNumber(String grnNumber) {
			this.grnNumber = grnNumber;
		}

		public Integer getStatusId() {
			return statusId;
		}

		public void setStatusId(Integer statusId) {
			this.statusId = statusId;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public Integer getApprovalStatusId() {
			return approvalStatusId;
		}

		public void setApprovalStatusId(Integer approvalStatusId) {
			this.approvalStatusId = approvalStatusId;
		}

		public String getApprovalStatus() {
			return approvalStatus;
		}

		public void setApprovalStatus(String approvalStatus) {
			this.approvalStatus = approvalStatus;
		}

		public Integer getStoreId() {
			return storeId;
		}

		public void setStoreId(Integer storeId) {
			this.storeId = storeId;
		}

		public String getStoreName() {
			return storeName;
		}

		public void setStoreName(String storeName) {
			this.storeName = storeName;
		}

		public Boolean getIsConsignment() {
			return isConsignment;
		}

		public void setIsConsignment(Boolean isConsignment) {
			this.isConsignment = isConsignment;
		}

		public Double getTotalAmount() {
			return totalAmount;
		}

		public void setTotalAmount(Double totalAmount) {
			this.totalAmount = totalAmount;
		}
		
		public Double getTotalTaxAmount() {
			return totalTaxAmount;
		}

		public void setTotalTaxAmount(Double totalTaxAmount) {
			this.totalTaxAmount = totalTaxAmount;
		}

		public Double getTotalNetAmount() {
			return totalNetAmount;
		}

		public void setTotalNetAmount(Double totalNetAmount) {
			this.totalNetAmount = totalNetAmount;
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

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public List<ConsignmentStockReturnDetailResponse> getItems() {
			return items;
		}

		public void setItems(List<ConsignmentStockReturnDetailResponse> items) {
			this.items = items;
		}

		public String getAddedBy() {
			return addedBy;
		}

		public void setAddedBy(String addedBy) {
			this.addedBy = addedBy;
		}

		public List<OtherChargesResponse> getOtherChargesList() {
			return otherChargesList;
		}

		public void setOtherChargesList(List<OtherChargesResponse> otherChargesList) {
			this.otherChargesList = otherChargesList;
		}

		public List<TaxDropDown> getTaxList() {
			return taxList;
		}

		public void setTaxList(List<TaxDropDown> taxList) {
			this.taxList = taxList;
		}

		public void addItems(ConsignmentStockReturnDetailResponse item) {
			if (this.items == null)
				this.items = new ArrayList<ConsignmentStockReturnDetailResponse>();
			this.items.add(item);
		}

}
