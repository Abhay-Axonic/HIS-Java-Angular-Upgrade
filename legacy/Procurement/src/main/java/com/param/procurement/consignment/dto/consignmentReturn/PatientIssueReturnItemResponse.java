package com.param.procurement.consignment.dto.consignmentReturn;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class PatientIssueReturnItemResponse
{
	private Integer id;
	
	private String issueNo;
	
	private Date issueDate;
	
	private String itemName;
	
	private String itemCode;
	
	private String genericName;
	
	private String batchNo;
	
	private Date expiry;
	
	private String uom;
	
	private String uomUnit;
	
	private Integer quantityIssued;
	
	private Integer batchMapperId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
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

	public Integer getQuantityIssued() {
		return quantityIssued;
	}

	public void setQuantityIssued(Integer quantityIssued) {
		this.quantityIssued = quantityIssued;
	}

	public Integer getBatchMapperId() {
		return batchMapperId;
	}

	public void setBatchMapperId(Integer batchMapperId) {
		this.batchMapperId = batchMapperId;
	}

	public PatientIssueReturnItemResponse()
	{
		super();
	}
	
	public PatientIssueReturnItemResponse(Integer id, String issueNo, Date issueDate, String itemName, String itemCode,
			String genericName, String batchNo, Date expiry, String uom, String uomUnit, Integer quantityIssued,
			Integer batchMapperId) {
		super();
		this.id = id;
		this.issueNo = issueNo;
		this.issueDate = issueDate;
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.genericName = genericName;
		this.batchNo = batchNo;
		this.expiry = expiry;
		this.uom = uom;
		this.uomUnit = uomUnit;
		this.quantityIssued = quantityIssued;
		this.batchMapperId = batchMapperId;
	}
	
}