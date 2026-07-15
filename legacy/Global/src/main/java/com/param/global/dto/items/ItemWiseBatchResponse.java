package com.param.global.dto.items;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemWiseBatchResponse {
    protected Integer storeId;

    protected  String storeName;

    protected Integer itemId;

    protected String itemCode;

    protected  String itemName;

    protected String batchNo;

    protected Date expiry;

    protected Integer availableStockQuantity;

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

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public Integer getAvailableStockQuantity() {
        return availableStockQuantity;
    }

    public void setAvailableStockQuantity(Integer availableStockQuantity) {
        this.availableStockQuantity = availableStockQuantity;
    }

    public ItemWiseBatchResponse() {
        super();
    }

    public ItemWiseBatchResponse(Integer storeId, String storeName, Integer itemId, String itemCode, String itemName, String batchNo, Date expiry, Integer availableStockQuantity) {
        super();
        this.storeId = storeId;
        this.storeName = storeName;
        this.itemId = itemId;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.batchNo = batchNo;
        this.expiry = expiry;
        this.availableStockQuantity = availableStockQuantity;
    }
}
