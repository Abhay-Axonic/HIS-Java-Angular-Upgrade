package com.param.global.pharmacy.dto;

import java.math.BigInteger;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SubstituteItemDto {
	
	private Integer assignPickerId;
	
	private Integer parentAssignPickerId;
	
	private Integer assignPickerDetailId;
	
	private Integer prescriptionDetailId;
	
	private Integer itemId;
	
	private String itemName;
	
	private String itemCode;
	
	private Integer genericId;
	
	private String genericName;
	
	private String frequency;
	
	private Double dose;
	
	private Date startDate;
	
	private Date stopDate;
	
	private Integer statusId;
	
	private String status;
	private String manufacturer;

	private Boolean interventionFlag;
	private Boolean isNarcoticSubstitute;

	private Integer issuedQuantity;
	private Integer pickedQuantity;
	private Integer parentSubstituteId;
	private Integer parentIssueDetailsId;
	//private Integer parentsubstituteId;
	private Integer parentsubstituteId;

	private Long batchId;
	private Integer quantityOrdered;
	private Integer quantityIssued;
	private Integer markUpDown;
	private Double markUpDownPer;

	private String batchNo;
	
	private String rackNo;

	private String uomType;
	private String tax;

	private Integer uomTypeId;
	private Integer taxId;
	private Integer batchid;
	private BigInteger batchID;

	private Integer uomUnitId;
	private Integer issueDtlId;
	private BigInteger prescriptionDetailid;
	private Double consessionPercentage;

	private Double netAmount;
	private Double rate;
	private Double totalAmount;
	private Double selfPayable;
	private Double creditPayable;
	private Double discountAmount;
	private Double taxAmount;
	private Long pendingIssueQuantity;
	private Double taxPercentage;
	private Integer productCategoryId;
	private Integer manufactureId;
	private String manufacturerName;
	private Integer qtyPrescribed;
    private Boolean otc;
	private String instructionDesc;
	private Integer assetCategoryId;
	private String uomUnit;
    private Boolean isFromAssignPicker;
	private Integer newDispQty;
	private Double packageConsumptionAmt;
	private Double consessionAmount;
	private String frequencyDesc;
	private Integer formulationTypeId;
    private Boolean isOtc;
    private Boolean isNarcotic;
	private Integer organizationId;
    private Boolean isBatchRequired;

    private boolean isThreeUOMApplicable;
    
    private boolean atCop;
	private Boolean isLasa;
    
    public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }

    private String storageUnitName;
    
    public String getStorageUnitName() {
       return storageUnitName;
   }

   public void setStorageUnitName(String storageUnitName) {
       this.storageUnitName = storageUnitName;
   }
   
   private Integer storageUnitId;
   
   public Integer getStorageUnitId() {
		return storageUnitId;
	}

	public void setStorageUnitId(Integer storageUnitId) {
		this.storageUnitId = storageUnitId;
	}

	public String getFrequencyDesc() {
		return frequencyDesc;
	}
	public void setFrequencyDesc(String frequencyDesc) {
		this.frequencyDesc = frequencyDesc;
	}
	public Integer getAssignPickerId() {
		return assignPickerId;
	}
	public void setAssignPickerId(Integer assignPickerId) {
		this.assignPickerId = assignPickerId;
	}
	public Integer getParentAssignPickerId() {
		return parentAssignPickerId;
	}
	public void setParentAssignPickerId(Integer parentAssignPickerId) {
		this.parentAssignPickerId = parentAssignPickerId;
	}
	public Integer getAssignPickerDetailId() {
		return assignPickerDetailId;
	}
	public void setAssignPickerDetailId(Integer assignPickerDetailId) {
		this.assignPickerDetailId = assignPickerDetailId;
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
	public Integer getGenericId() {
		return genericId;
	}
	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}
	public String getGenericName() {
		return genericName;
	}
	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public Double getDose() {
		return dose;
	}
	public void setDose(Double dose) {
		this.dose = dose;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getStopDate() {
		return stopDate;
	}
	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
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
	public Boolean getInterventionFlag() {
		return interventionFlag;
	}
	public void setInterventionFlag(Boolean interventionFlag) {
		this.interventionFlag = interventionFlag;
	}
	public Integer getIssuedQuantity() {
		return issuedQuantity;
	}
	public void setIssuedQuantity(Integer issuedQuantity) {
		this.issuedQuantity = issuedQuantity;
	}
	
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getRackNo() {
		return rackNo;
	}
	public void setRackNo(String rackNo) {
		this.rackNo = rackNo;
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
	
	public Integer getIssueDtlId() {
		return issueDtlId;
	}
	public void setIssueDtlId(Integer issueDtlId) {
		this.issueDtlId = issueDtlId;
	}
	public Integer getQuantityOrdered() {
		return quantityOrdered;
	}
	public void setQuantityOrdered(Integer quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}
	public Integer getQuantityIssued() {
		return quantityIssued;
	}
	public void setQuantityIssued(Integer quantityIssued) {
		this.quantityIssued = quantityIssued;
	}
	public Integer getTaxId() {
		return taxId;
	}
	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public String getTax() {
		return tax;
	}
	public void setTax(String tax) {
		this.tax = tax;
	}
	public Boolean getIsNarcoticSubstitute() {
		return isNarcoticSubstitute;
	}
	public void setIsNarcoticSubstitute(Boolean isNarcoticSubstitute) {
		this.isNarcoticSubstitute = isNarcoticSubstitute;
	}
	public Integer getPrescriptionDetailId() {
		return prescriptionDetailId;
	}
	public void setPrescriptionDetailId(Integer prescriptionDetailId) {
		this.prescriptionDetailId = prescriptionDetailId;
	}
	public BigInteger getPrescriptionDetailid() {
		return prescriptionDetailid;
	}
	public void setPrescriptionDetailid(BigInteger prescriptionDetailid) {
		this.prescriptionDetailid = prescriptionDetailid;
	}
	public Integer getPickedQuantity() {
		return pickedQuantity;
	}
	public void setPickedQuantity(Integer pickedQuantity) {
		this.pickedQuantity = pickedQuantity;
	}
	
	public Long getBatchId() {
		return batchId;
	}
	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}
	public Integer getBatchid() {
		return batchid;
	}
	public void setBatchid(Integer batchid) {
		this.batchid = batchid;
	}
	public Long getPendingIssueQuantity() {
		return pendingIssueQuantity;
	}
	public void setPendingIssueQuantity(Long pendingIssueQuantity) {
		this.pendingIssueQuantity = pendingIssueQuantity;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public Double getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Double getSelfPayable() {
		return selfPayable;
	}
	public void setSelfPayable(Double selfPayable) {
		this.selfPayable = selfPayable;
	}
	public Double getCreditPayable() {
		return creditPayable;
	}
	public void setCreditPayable(Double creditPayable) {
		this.creditPayable = creditPayable;
	}
	public Double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}
	public Double getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}
	public Double getTaxPercentage() {
		return taxPercentage;
	}
	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}
	
	public Integer getParentsubstituteId() {
		return parentsubstituteId;
	}
	public void setParentsubstituteId(Integer parentsubstituteId) {
		this.parentsubstituteId = parentsubstituteId;
	}
	public Integer getProductCategoryId() {
		return productCategoryId;
	}
	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}
	public Integer getManufactureId() {
		return manufactureId;
	}
	public void setManufactureId(Integer manufactureId) {
		this.manufactureId = manufactureId;
	}
	public Integer getParentSubstituteId() {
		return parentSubstituteId;
	}
	public void setParentSubstituteId(Integer parentSubstituteId) {
		this.parentSubstituteId = parentSubstituteId;
	}
	public Integer getParentIssueDetailsId() {
		return parentIssueDetailsId;
	}
	public void setParentIssueDetailsId(Integer parentIssueDetailsId) {
		this.parentIssueDetailsId = parentIssueDetailsId;
	}
	public Boolean getIsFromAssignPicker() {
		return isFromAssignPicker;
	}
	public void setIsFromAssignPicker(Boolean isFromAssignPicker) {
		this.isFromAssignPicker = isFromAssignPicker;
	}
		public Double getConsessionPercentage() {
		return consessionPercentage;
	}
	public void setConsessionPercentage(Double consessionPercentage) {
		this.consessionPercentage = consessionPercentage;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	public Integer getQtyPrescribed() {
		return qtyPrescribed;
	}
	public void setQtyPrescribed(Integer qtyPrescribed) {
		this.qtyPrescribed = qtyPrescribed;
	}
	public Boolean getOtc() {
		return otc;
	}
	public void setOtc(Boolean otc) {
		this.otc = otc;
	}
	public String getInstructionDesc() {
		return instructionDesc;
	}
	public void setInstructionDesc(String instructionDesc) {
		this.instructionDesc = instructionDesc;
	}
	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}
	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}
	public Integer getNewDispQty() {
		return newDispQty;
	}
	public void setNewDispQty(Integer newDispQty) {
		this.newDispQty = newDispQty;
	}
	public BigInteger getBatchID() {
		return batchID;
	}
	public void setBatchID(BigInteger batchID) {
		this.batchID = batchID;
	}
	public Double getPackageConsumptionAmt() {
		return packageConsumptionAmt;
	}
	public void setPackageConsumptionAmt(Double packageConsumptionAmt) {
		this.packageConsumptionAmt = packageConsumptionAmt;
	}
	public Double getConsessionAmount() {
		return consessionAmount;
	}
	public void setConsessionAmount(Double consessionAmount) {
		this.consessionAmount = consessionAmount;
	}
	public Integer getFormulationTypeId() {
		return formulationTypeId;
	}
	public void setFormulationTypeId(Integer formulationTypeId) {
		this.formulationTypeId = formulationTypeId;
	}
	public Boolean getIsOtc() {
		return isOtc;
	}
	public void setIsOtc(Boolean isOtc) {
		this.isOtc = isOtc;
	}
	public Boolean getIsNarcotic() {
		return isNarcotic;
	}
	public void setIsNarcotic(Boolean isNarcotic) {
		this.isNarcotic = isNarcotic;
	}
	public Boolean getIsBatchRequired() {
		return isBatchRequired;
	}
	public void setIsBatchRequired(Boolean isBatchRequired) {
		this.isBatchRequired = isBatchRequired;
	}
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	public Integer getMarkUpDown() {
		return markUpDown;
	}
	public void setMarkUpDown(Integer markUpDown) {
		this.markUpDown = markUpDown;
	}
	public Double getMarkUpDownPer() {
		return markUpDownPer;
	}
	public void setMarkUpDownPer(Double markUpDownPer) {
		this.markUpDownPer = markUpDownPer;
	}

	public boolean getAtCop() {
		return atCop;
	}

	public void setAtCop(boolean atCop) {
		this.atCop = atCop;
	}

	public Boolean getIsLasa() {
		return isLasa;
	}

	public void setIsLasa(Boolean isLasa) {
		this.isLasa = isLasa;
	}
}
