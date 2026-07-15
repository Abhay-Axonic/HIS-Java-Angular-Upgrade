package com.param.global.dto.global;

import java.util.Date;
import java.util.List;

public class AgainstPOSearchResponse extends AgainstPurchaseOrder{
	
	    private Integer id;
	
	    private String storeName;
	    
	    private String poNo;

        private Date poDate;
        
        private Integer assetTypeId;
        
        private Integer storeId;
        private Integer discountTypeId;
        private Double discount;
        private Double totalDiscount;
        private Integer fromStoreId;
        private String fromStoreName;
        private Integer vendorId;
        private Double netAmount;
        private Double roundOffAmount;
        private String dueDate;
        public String getDueDate() {
			return dueDate;
		}

		public void setDueDate(String dueDate) {
			this.dueDate = dueDate;
		}

		public Integer getVendorId() {
			return vendorId;
		}

		public void setVendorId(Integer vendorId) {
			this.vendorId = vendorId;
		}

		public Integer getFromStoreId() {
			return fromStoreId;
		}

		public void setFromStoreId(Integer fromStoreId) {
			this.fromStoreId = fromStoreId;
		}

		public String getFromStoreName() {
			return fromStoreName;
		}

		public void setFromStoreName(String fromStoreName) {
			this.fromStoreName = fromStoreName;
		}

		public Integer getDiscountTypeId() {
			return discountTypeId;
		}

		public void setDiscountTypeId(Integer discountTypeId) {
			this.discountTypeId = discountTypeId;
		}

		public Double getDiscount() {
			return discount;
		}

		public void setDiscount(Double discount) {
			this.discount = discount;
		}

		public Double getTotalDiscount() {
			return totalDiscount;
		}

		public void setTotalDiscount(Double totalDiscount) {
			this.totalDiscount = totalDiscount;
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

		List<PurchaseOrderDetailResponse> items;

		public AgainstPOSearchResponse() {
			super();
		}

		public AgainstPOSearchResponse(Integer id,String vendorName, Integer storeId,String storeName, String poNo, Date poDate) {
			super(vendorName,storeId);
			this.id = id;
			this.storeName = storeName;
			this.poNo = poNo;
			this.poDate = poDate;
		}
		
		public AgainstPOSearchResponse(Integer id, String poNo) {
			super();
			this.id = id;
			this.poNo = poNo;
		}
		
		public AgainstPOSearchResponse(Integer id, String poNo,Integer vendorId, String vendorName) {
			super();
			this.id = id;
			this.poNo = poNo;
			this.vendorId=vendorId;
			this.vendorName=vendorName;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getStoreName() {
			return storeName;
		}

		public void setStoreName(String storeName) {
			this.storeName = storeName;
		}

		public String getPoNo() {
			return poNo;
		}

		public void setPoNo(String poNo) {
			this.poNo = poNo;
		}

		public Date getPoDate() {
			return poDate;
		}

		public void setPoDate(Date poDate) {
			this.poDate = poDate;
		}

		public List<PurchaseOrderDetailResponse> getItems() {
			return items;
		}

		public void setItems(List<PurchaseOrderDetailResponse> items) {
			this.items = items;
		}
        
        
		private List<POOtherCharges> otherCharges;

		public List<POOtherCharges> getOtherCharges() {
			return otherCharges;
		}

		public void setOtherCharges(List<POOtherCharges> otherCharges) {
			this.otherCharges = otherCharges;
		}

		public Double getRoundOffAmount() {
			return roundOffAmount;
		}

		public void setRoundOffAmount(Double roundOffAmount) {
			this.roundOffAmount = roundOffAmount;
		}

		public Double getNetAmount() {
			return netAmount;
		}

		public void setNetAmount(Double netAmount) {
			this.netAmount = netAmount;
		}
		
        

}
