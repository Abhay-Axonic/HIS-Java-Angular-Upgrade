package com.param.inventory.nursing.dto.nonBillableConsumption;

public class NonBillableConsumptionDto {
		private Integer id;
		private Integer itemId;
		private String itemCode;
		private String itemName;
		private String batchNo;
		private String expiryDate;
		private String uomDetails;
		private Integer consumedQty;
		private Integer acceptedQty;
		private Integer availableQty;
		public Integer getItemId() {
			return itemId;
		}
		public void setItemId(Integer itemId) {
			this.itemId = itemId;
		}
		public String getItemCode() {
			return itemCode;
		}
		public void setItemCode(String itemCode) {
			this.itemCode = itemCode;
		}
		public String getItemName() {
			return itemName;
		}
		public void setItemName(String itemName) {
			this.itemName = itemName;
		}
		public String getBatchNo() {
			return batchNo;
		}
		public void setBatchNo(String batchNo) {
			this.batchNo = batchNo;
		}
		public String getExpiryDate() {
			return expiryDate;
		}
		public void setExpiryDate(String expiryDate) {
			this.expiryDate = expiryDate;
		}
		public String getUomDetails() {
			return uomDetails;
		}
		public void setUomDetails(String uomDetails) {
			this.uomDetails = uomDetails;
		}
		public Integer getConsumedQty() {
			return consumedQty;
		}
		public void setConsumedQty(Integer consumedQty) {
			this.consumedQty = consumedQty;
		}
		public Integer getAcceptedQty() {
			return acceptedQty;
		}
		public void setAcceptedQty(Integer acceptedQty) {
			this.acceptedQty = acceptedQty;
		}
		public Integer getAvailableQty() {
			return availableQty;
		}
		public void setAvailableQty(Integer availableQty) {
			this.availableQty = availableQty;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		
		
}
