package com.param.procurement.consignment.dto.implantReturnAcceptance;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ImplantReturnAcceptanceResponseItems
{
	private Integer id;
	
	private Integer itemId;
	
	private String itemName;
	
	private String genericName;
	
	private String batchNo;
	
	private Date expiry;
	
	private String uom;
	
	private String uomUnit;
	
	private Double rate;
	
	private Integer returnQuantity;
	
	private String reasonForReturn;
	
	private Integer issueDetailBatchMapper;

	private Integer indentDetailId;
	
	private Integer statusId;
	
	private String status;
	
	
	
	public ImplantReturnAcceptanceResponseItems(Integer id, Integer itemId, String itemName, String genericName, String batchNo, Date expiry,
			String uom, String uomUnit, Double rate, Integer returnQuantity, String reasonForReturn,
			Integer issueDetailBatchMapper) {
		super();
		this.id = id;
		this.itemName = itemName;
		this.genericName = genericName;
		this.batchNo = batchNo;
		this.expiry = expiry;
		this.uom = uom;
		this.uomUnit = uomUnit;
		this.rate = rate;
		this.returnQuantity = returnQuantity;
		this.reasonForReturn = reasonForReturn;
		this.issueDetailBatchMapper = issueDetailBatchMapper;
		
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

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
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

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public String getReasonForReturn() {
		return reasonForReturn;
	}

	public void setReasonForReturn(String reasonForReturn) {
		this.reasonForReturn = reasonForReturn;
	}

	public Integer getIssueDetailBatchMapper() {
		return issueDetailBatchMapper;
	}

	public void setIssueDetailBatchMapper(Integer issueDetailBatchMapper) {
		this.issueDetailBatchMapper = issueDetailBatchMapper;
	}

	public Integer getIndentDetailId() {
		return indentDetailId;
	}

	public void setIndentDetailId(Integer indentDetailId) {
		this.indentDetailId = indentDetailId;
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
	   
	
}