package com.param.procurement.consignment.dto.patientConsumption;

import java.util.Date;
import java.util.List;

import com.param.global.dto.items.BatchResponse;

public class PatientConsumtionIssueDetailListItem {
	
	private Integer id;

	private Integer itemId;

	private String itemCode;

	private String itemName;

	private String genericName;

	private Long batchId;

	private String batchNo;

	private Date expiry;

	private String uomType;

	private Integer uomTypeId;

	private Integer uomUnitId;

	private String uomUnit;

	private Integer indentQuantity;

	private Long availableStockQuantity;

	private Integer issueQuantity;

	private Integer patientIndentDetailId;

	private Integer implantIndentDetailId;
	
	private String manufacturerDeatils;
	
	private String vendorName;

	private List<BatchResponse> batch;

	private Integer statusId;

	private String status;

	
	/*public PatientConsumtionIssueDetailListItem(Integer id, Integer itemId, String itemCode, String itemName,String manufacturerDeatils,
			String uomType, String uomUnit, Integer indentQuantity, Integer issueQuantity, Long availableStockQuantity,
			List<BatchResponse> batch) {
		super();
		this.id = id;		this.itemId = itemId;		this.itemCode = itemCode;		this.itemName = itemName;
		this.manufacturerDeatils=manufacturerDeatils;	
		this.uomType = uomType;		this.uomUnit = uomUnit;		this.indentQuantity = indentQuantity;
		this.issueQuantity = issueQuantity;		this.availableStockQuantity = availableStockQuantity;		this.batch = batch;
	}
	
	issueDetail.getId(), issueDetail.getPatientIndentDetail().getItem().getId(), 
						issueDetail.getPatientIndentDetail().getItem().getCode(), issueDetail.getPatientIndentDetail().getItem().getName(),
						issueDetail.getPatientIndentDetail().getItem().getManufacturer().getName(), batchMapper.getBatch().getId(), batchMapper.getBatch().getBatchNo(),
						batchMapper.getBatch().getExpiryDate(), batchMapper.getUomType().getType(), batchMapper.getUomType().getId(),
						batchMapper.getUomUnit().getId(), batchMapper.getUomUnit().getUnits(), issueDetail.getPatientIndentDetail().getIndentQuantity(), 
						stockAvailable, batchMapper.getIssueQuantity()
	 
	*/
	public PatientConsumtionIssueDetailListItem(Integer id, Integer itemId, String itemCode, String itemName, String manufacturerDeatils,
			Long batchId, String batchNo, Date expiry, String uomType, Integer uomTypeId, Integer uomUnitId, String uomUnit,
			Integer indentQuantity, Long availableStockQuantity, Integer issueQuantity)
	{
		super();
		this.id = id;		this.itemId = itemId;		this.itemCode = itemCode;		this.itemName = itemName;
		this.manufacturerDeatils=manufacturerDeatils;	
		this.batchId=batchId;
		this.batchNo=batchNo;
		this.expiry=expiry;
		this.uomType = uomType;	
		this.uomTypeId=uomTypeId;
		this.uomUnitId=uomUnitId;
		this.uomUnit = uomUnit;
		this.indentQuantity = indentQuantity;
		this.availableStockQuantity = availableStockQuantity;
		this.issueQuantity=issueQuantity;
		
		
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

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
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

	public String getUomType() {
		return uomType;
	}

	public void setUomType(String uomType) {
		this.uomType = uomType;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public Integer getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}

	public String getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}

	public Integer getIndentQuantity() {
		return indentQuantity;
	}

	public void setIndentQuantity(Integer indentQuantity) {
		this.indentQuantity = indentQuantity;
	}

	public Long getAvailableStockQuantity() {
		return availableStockQuantity;
	}

	public void setAvailableStockQuantity(Long availableStockQuantity) {
		this.availableStockQuantity = availableStockQuantity;
	}

	public Integer getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Integer issueQuantity) {
		this.issueQuantity = issueQuantity;
	}

	public Integer getPatientIndentDetailId() {
		return patientIndentDetailId;
	}

	public void setPatientIndentDetailId(Integer patientIndentDetailId) {
		this.patientIndentDetailId = patientIndentDetailId;
	}

	public Integer getImplantIndentDetailId() {
		return implantIndentDetailId;
	}

	public void setImplantIndentDetailId(Integer implantIndentDetailId) {
		this.implantIndentDetailId = implantIndentDetailId;
	}

	public List<BatchResponse> getBatch() {
		return batch;
	}

	public void setBatch(List<BatchResponse> batch) {
		this.batch = batch;
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

	public String getManufacturerDeatils() {
		return manufacturerDeatils;
	}

	public void setManufacturerDeatils(String manufacturerDeatils) {
		this.manufacturerDeatils = manufacturerDeatils;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}


}
