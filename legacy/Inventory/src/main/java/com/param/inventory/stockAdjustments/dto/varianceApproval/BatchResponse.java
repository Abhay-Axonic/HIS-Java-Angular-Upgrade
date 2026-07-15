package com.param.inventory.stockAdjustments.dto.varianceApproval;
import java.util.Date;

public class BatchResponse {

	private Integer id;

	private Long batchId;

	private String batchNo;

	private Double cop;

	private Double mrp;

	private Long availableQuantity;	

	private Integer Quantity;

	private Integer uomTypeId;

	private String uom;

	private Integer uomUnitId;

	private String uomUnit;
	
	private Integer uploadedQuantity;
	
	private Date expiry; 
	
	private Boolean Isnew;
	
	private Boolean notAnOption;
	
	private Integer rejectedQuantity;	

	public BatchResponse() {
		super();
	}

	public BatchResponse(Long batchId, String batchNo, Double cop) {
		super();
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.cop = cop;
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

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Long getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(Long availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
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

	public Integer getUploadedQuantity() {
		return uploadedQuantity;
	}

	public void setUploadedQuantity(Integer uploadedQuantity) {
		this.uploadedQuantity = uploadedQuantity;
	}

	public Date getExpiryDate() {
		return expiry;
	}

	public void setExpiryDate(Date expiry) {
		this.expiry = expiry;
	}
	
	public Boolean getIsnew() {
		return Isnew;
	}

	public void setIsnew(Boolean isnew) {
		Isnew = isnew;
	}
	public Boolean getNotAnOption() {
		return notAnOption;
	}

	public void setNotAnOption(Boolean notAnOption) {
		this.notAnOption = notAnOption;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getQuantity() {
		return Quantity;
	}

	public void setQuantity(Integer quantity) {
		Quantity = quantity;
	}

	public Integer getRejectedQuantity() {
		return rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
	}
}
