package com.param.procurement.purchase.dto.goodReceiptNote;

import java.util.Date;

public class GrnAccessoriesMapper {
	
	private Integer id;

	private Integer itemId;
	
	private String itemName;
	
	private String itemCode;

	private String serialNo;

	private Date warrentyFrom;

	private Date warrentyTo;
	
	private Integer quantity;
	
	private String specification;

	private Integer grnDetailId;
	
	public GrnAccessoriesMapper() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public GrnAccessoriesMapper(Integer id, Integer itemId, String itemName, String itemCode, String serialNo,
			Date warrentyFrom, Date warrentyTo,Integer quantity,String specification) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.serialNo = serialNo;
		this.warrentyFrom = warrentyFrom;
		this.warrentyTo = warrentyTo;
		this.quantity = quantity;
		this.specification = specification;	
	}

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

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Date getWarrentyTo() {
		return warrentyTo;
	}

	public void setWarrentyTo(Date warrentyTo) {
		this.warrentyTo = warrentyTo;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Date getWarrentyFrom() {
		return warrentyFrom;
	}

	public void setWarrentyFrom(Date warrentyFrom) {
		this.warrentyFrom = warrentyFrom;
	}

	public Integer getGrnDetailId() {
		return grnDetailId;
	}

	public void setGrnDetailId(Integer grnDetailId) {
		this.grnDetailId = grnDetailId;
	}
	
}
