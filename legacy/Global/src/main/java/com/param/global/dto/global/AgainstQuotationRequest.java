package com.param.global.dto.global;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AgainstQuotationRequest {
	
	 private Date fromDate;
		
		private Date toDate;
		
		private Integer screenId;
		
		protected String qrNo;
		
		protected Integer prId;
		
		protected Integer qrId;
		
		public Integer getQrId() {
			return qrId;
		}

		public void setQrId(Integer qrId) {
			this.qrId = qrId;
		}

		protected Integer assetTypeId;	
		
		protected Integer storeId;
		
		protected Integer vendorId;
		
		private List<Integer> itemIds;

		public AgainstQuotationRequest() {
			super();
			// TODO Auto-generated constructor stub
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

		public Integer getScreenId() {
			return screenId;
		}

		public void setScreenId(Integer screenId) {
			this.screenId = screenId;
		}

		public String getQrNo() {
			return qrNo;
		}

		public void setQrNo(String qrNo) {
			this.qrNo = qrNo;
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

		public Integer getStoreId() {
			return storeId;
		}

		public void setStoreId(Integer storeId) {
			this.storeId = storeId;
		}

		public Integer getVendorId() {
			return vendorId;
		}

		public void setVendorId(Integer vendorId) {
			this.vendorId = vendorId;
		}

		public List<Integer> getItemIds() {
			return itemIds;
		}

		public void setItemIds(List<Integer> itemIds) {
			this.itemIds = itemIds;
		}
		
		

}
