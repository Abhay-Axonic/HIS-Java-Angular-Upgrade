package com.param.global.dto.items;

import java.util.Date;
import java.util.List;


public class OpeningBalanceItem {
	
private Integer id; /*grnDetailID*/
	
	private Integer itemId;
	
    private String itemCode;
    
    private String itemName;
    
    private String batchNo;
    
   
    private Date expiryDate;
    
   
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public OpeningBalanceItem()
	{
		super();
	}
	
	public OpeningBalanceItem(Integer id) {
		super();
		this.id = id;
	}


	public OpeningBalanceItem(String itemCode, String itemName, String batchNo) {
		super();
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.batchNo = batchNo;
	}
}
