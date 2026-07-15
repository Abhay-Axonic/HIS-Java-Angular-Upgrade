package com.param.pharmacy.op.dto.salesWorkList;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.BatchResponse;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyItem;

@JsonInclude(Include.NON_NULL)
public class PrescriptionDetailItemResponse {
	@JsonIgnore
	private Long id;
	
	private Integer itemId;

	private String itemCode;

	private String itemName;

	private Integer genericId;
	
	private String genericName;

	private Integer manufacturerId;
	
	private String manufacturerName;
	
	private Integer taxId;
     
     private String tax;
     
     private Double taxPercentage;
	
	private Integer qtyPrescribed;
	
	private boolean isOtc;
	private Boolean isNarcotic;

	private Integer statusId;
	private Integer storeId;
    private String remark;

	private String status;
	
	private Long stockAvailable;
	
	private String rackNo;
	@JsonIgnore
	private Long quantityDispensed;
	private Integer quantitydispensed;
	   
	private List<BatchResponse> batch;
	
	private List<FrequencyResponse> frequency;
	
	private String instruction;
	
	protected String batchDetails;
	
	private Integer productCatId;
	
	private Integer genProdCatId;
	
	protected Integer markUpDown;
	
	protected Integer assetCategoryId;
	
	Double patientClassPercentage;
	
	boolean markupStatus;
	
	Integer priorityId;
	
	private Integer issueId;
	
	private Integer issueDetailId;
	
	private Integer batchId;
 	
	private Integer uomTypeId;
	
	private Integer uomUnitId;

	private Integer issuedQty;
	
	private BigInteger pricId;
	private Integer parentSubstituteId;
	private Long prescriptionId;
	private Long prescriptionDetailId;
	private List<PrescriptionDetailItemResponse> selectedItems;
	private Integer productCategoryId;
    private Double consessionPercentage;
    private Double netAmount;
    private Double creditPayable;
    private Double selfPayable;
    private Double discountAmount;
    private Double rate;
    private Double taxAmount;
    private Double totalAmount;
	private Long newDispQty;
	private Integer newdispQty;
	private Integer quantityDispensedTemp;
	private Integer quantity;
	private BigInteger tempPrescriptionId;
	private Double packageConsumptionAmt;
    private Double patientClassAmount;
	private String instructionDesc;
	private String frequencyDesc;
	private Date startDate;
	private Integer formulationTypeId;
	private String units;
	private Integer storageUnitId;
	
    private String storageUnitName;
    private boolean atCop;
    
    private boolean isThreeUOMApplicable;
    
    private Double prescribedDose;

    private Integer prescribedDoseUnitId;
    
    private Integer factorId;
   	
   	private Integer formulationDosageMappId;
   	
   	private Integer duration;
   	
   	private Integer saleDetailId;
   	
   	private String durationUnit;
   	
    private boolean isActive;
    private String colorCode;
	private Boolean isLasa;
    public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }
    
    public String getStorageUnitName() {
       return storageUnitName;
   }

   public void setStorageUnitName(String storageUnitName) {
       this.storageUnitName = storageUnitName;
   }

	private Date stopDate;
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

	public String getFrequencyDesc() {
		return frequencyDesc;
	}

	public void setFrequencyDesc(String frequencyDesc) {
		this.frequencyDesc = frequencyDesc;
	}

	public Double getPackageConsumptionAmt() {
		return packageConsumptionAmt;
	}

	public void setPackageConsumptionAmt(Double packageConsumptionAmt) {
		this.packageConsumptionAmt = packageConsumptionAmt;
	}

	public Double getPatientClassPercentage() {
		return patientClassPercentage;
	}

	public void setPatientClassPercentage(Double patientClassPercentage) {
		this.patientClassPercentage = patientClassPercentage;
	}

	public boolean isMarkupStatus() {
		return markupStatus;
	}

	public void setMarkupStatus(boolean markupStatus) {
		this.markupStatus = markupStatus;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Integer getMarkUpDown() {
		return markUpDown;
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
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

	protected Double markUpDownPer;

	public Long getId() {
		return (pricId != null && pricId.longValue() > 0) ? pricId.longValue() : id;
	}

	public PrescriptionDetailItemResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setId(Long id) {
		this.id = (pricId != null && pricId.longValue() > 0) ? pricId.longValue() : id;
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

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public Integer getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public List<BatchResponse> getBatch() {
		return batch;
	}

	public void setBatch(List<BatchResponse> list) {
		this.batch = list;
	}

	public boolean isOtc() {
		return isOtc;
	}

	public void setOtc(boolean isOtc) {
		this.isOtc = isOtc;
	}

	public Long getQuantityDispensed() {
		return (quantityDispensedTemp != null && quantityDispensedTemp.longValue() > 0) ? quantityDispensedTemp.longValue() : quantityDispensed;
	}

	public void setQuantityDispensed(Long quantityDispensed) {
		this.quantityDispensed = (quantityDispensedTemp != null && quantityDispensedTemp.longValue() > 0) ? quantityDispensedTemp.longValue() : quantityDispensed;
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

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
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

	public Long getStockAvailable() {
		return stockAvailable;
	}

	public void setStockAvailable(Long stockAvailable) {
		this.stockAvailable = stockAvailable;
	}

	public String getRackNo() {
		return rackNo;
	}

	public void setRackNo(String rackNo) {
		this.rackNo = rackNo;
	}
	
	public List<FrequencyResponse> getFrequency() {
		return frequency;
	}

	public void setFrequency(List<FrequencyResponse> frequency) {
		this.frequency = frequency;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public String getBatchDetails() {
		return batchDetails;
	}

	public void setBatchDetails(String batchDetails) {
		this.batchDetails = batchDetails;
	}

	public PrescriptionDetailItemResponse(Integer itemId, String itemCode, String itemName, Integer genericId,
			String genericName, Integer manufacturerId, String manufacturerName, List<BatchResponse> medicineBatches, boolean isOtc, Integer taxId, String tax, Double taxPercentage) {
		super();
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericId = genericId;
		this.genericName = genericName;
		this.manufacturerId = manufacturerId;
		this.manufacturerName = manufacturerName;
		this.batch = medicineBatches;
		this.isOtc = isOtc;
		this.taxId = taxId;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
	}
	
	public PrescriptionDetailItemResponse(Long id, Integer itemId, String itemCode, String itemName, Integer genericId,
			String genericName, Integer manufacturerId, String manufacturerName, Integer qtyPrescribed, List<BatchResponse> medicineBatches, boolean isOtc, Integer taxId, String tax, Double taxPercentage) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericId = genericId;
		this.genericName = genericName;
		this.manufacturerId = manufacturerId;
		this.manufacturerName = manufacturerName;
		this.qtyPrescribed = qtyPrescribed;
		this.batch = medicineBatches;
		this.isOtc = isOtc;
		this.taxId = taxId;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
	}
	
	public PrescriptionDetailItemResponse(Long id, Integer itemId, String itemCode, String itemName, Integer genericId,
			String genericName, Integer manufacturerId, String manufacturerName, Integer qtyPrescribed, List<BatchResponse> medicineBatches,
			List<FrequencyResponse> frequency, boolean isOtc, Integer taxId, String tax, Double taxPercentage) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericId = genericId;
		this.genericName = genericName;
		this.manufacturerId = manufacturerId;
		this.manufacturerName = manufacturerName;
		this.qtyPrescribed = qtyPrescribed;
		this.batch = medicineBatches;
		this.frequency = frequency;
		this.isOtc = isOtc;
		this.taxId = taxId;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
	}
	
	public PrescriptionDetailItemResponse(Long id, Integer itemId, String itemCode, String itemName, Integer genericId,
			String genericName, Integer manufacturerId, String manufacturerName, Integer qtyPrescribed, 
			List<BatchResponse> medicineBatches, boolean isOtc, Integer taxId, String tax, Double taxPercentage,
			Integer statusId, String status, Long quantityDispensed) {
	
		this(id, itemId, itemCode, itemName, genericId,
		 genericName, manufacturerId, manufacturerName, qtyPrescribed, medicineBatches, isOtc, taxId, tax, taxPercentage);
		
		this.statusId = statusId;
		this.status = status;
		this.quantityDispensed = quantityDispensed;
	}
	
	public PrescriptionDetailItemResponse(Long id, Integer itemId, String itemCode, String itemName, Integer genericId,
			String genericName, Integer manufacturerId, String manufacturerName, Integer qtyPrescribed, 
			List<BatchResponse> medicineBatches, List<FrequencyResponse> frequency, boolean isOtc, Integer taxId, String tax, Double taxPercentage,
			Integer statusId, String status, Long quantityDispensed, String instruction) {
	
		this(id, itemId, itemCode, itemName, genericId,
		 genericName, manufacturerId, manufacturerName, qtyPrescribed, medicineBatches, frequency, isOtc, taxId, tax, taxPercentage);
		
		this.statusId = statusId;
		this.status = status;
		this.quantityDispensed = quantityDispensed;
		this.instruction = instruction;
	}

	public Integer getProductCatId() {
		return productCatId;
	}

	public void setProductCatId(Integer productCatId) {
		this.productCatId = productCatId;
	}

	public Integer getGenProdCatId() {
		return genProdCatId;
	}

	public void setGenProdCatId(Integer genProdCatId) {
		this.genProdCatId = genProdCatId;
	}

	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	public Integer getIssueDetailId() {
		return issueDetailId;
	}

	public void setIssueDetailId(Integer issueDetailId) {
		this.issueDetailId = issueDetailId;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
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

	public Integer getIssuedQty() {
		return issuedQty;
	}

	public void setIssuedQty(Integer issuedQty) {
		this.issuedQty = issuedQty;
	}

	public BigInteger getPricId() {
		return pricId;
	}

	public void setPricId(BigInteger pricId) {
		this.pricId = pricId;
	}

	public Integer getQuantityDispensedTemp() {
		return quantityDispensedTemp;
	}

	public void setQuantityDispensedTemp(Integer quantityDispensedTemp) {
		this.quantityDispensedTemp = quantityDispensedTemp;
	}

	public Integer getParentSubstituteId() {
		return parentSubstituteId;
	}

	public void setParentSubstituteId(Integer parentSubstituteId) {
		this.parentSubstituteId = parentSubstituteId;
	}


	public Long getPrescriptionDetailId() {
		return prescriptionDetailId;
	}

	public void setPrescriptionDetailId(Long prescriptionDetailId) {
		this.prescriptionDetailId = prescriptionDetailId;
	}

	public List<PrescriptionDetailItemResponse> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(List<PrescriptionDetailItemResponse> selectedItems) {
		this.selectedItems = selectedItems;
	}

	public Long getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(Long prescriptionId) {
		this.prescriptionId =  prescriptionId;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Double getConsessionPercentage() {
		return consessionPercentage;
	}

	public void setConsessionPercentage(Double consessionPercentage) {
		this.consessionPercentage = consessionPercentage;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Double getCreditPayable() {
		return creditPayable;
	}

	public void setCreditPayable(Double creditPayable) {
		this.creditPayable = creditPayable;
	}

	public Double getSelfPayable() {
		return selfPayable;
	}

	public void setSelfPayable(Double selfPayable) {
		this.selfPayable = selfPayable;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Long getNewDispQty() {
		return newDispQty;
	}

	public void setNewDispQty(Long newDispQty) {
		this.newDispQty = newDispQty;
	}

	public Integer getQuantitydispensed() {
		return quantitydispensed;
	}

	public void setQuantitydispensed(Integer quantitydispensed) {
		this.quantitydispensed = quantitydispensed;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getNewdispQty() {
		return newdispQty;
	}

	public void setNewdispQty(Integer newdispQty) {
		this.newdispQty = newdispQty;
	}

	public BigInteger getTempPrescriptionId() {
		return tempPrescriptionId;
	}

	public void setTempPrescriptionId(BigInteger tempPrescriptionId) {
		this.tempPrescriptionId = tempPrescriptionId;
	}

	public Boolean getIsNarcotic() {
		return isNarcotic;
	}

	public void setIsNarcotic(Boolean isNarcotic) {
		this.isNarcotic = isNarcotic;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Double getPatientClassAmount() {
		return patientClassAmount;
	}

	public void setPatientClassAmount(Double patientClassAmount) {
		this.patientClassAmount = patientClassAmount;
	}

	public String getInstructionDesc() {
		return instructionDesc;
	}

	public void setInstructionDesc(String instructionDesc) {
		this.instructionDesc = instructionDesc;
	}

	public Integer getFormulationTypeId() {
		return formulationTypeId;
	}

	public void setFormulationTypeId(Integer formulationTypeId) {
		this.formulationTypeId = formulationTypeId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStorageUnitId() {
		return storageUnitId;
	}

	public void setStorageUnitId(Integer storageUnitId) {
		this.storageUnitId = storageUnitId;
	}

	public boolean getAtCop() {
		return atCop;
	}

	public void setAtCop(boolean atCop) {
		this.atCop = atCop;
	}

	public Double getPrescribedDose() {
		return prescribedDose;
	}

	public void setPrescribedDose(Double prescribedDose) {
		this.prescribedDose = prescribedDose;
	}

	public Integer getPrescribedDoseUnitId() {
		return prescribedDoseUnitId;
	}

	public void setPrescribedDoseUnitId(Integer prescribedDoseUnitId) {
		this.prescribedDoseUnitId = prescribedDoseUnitId;
	}

	public Integer getFactorId() {
		return factorId;
	}

	public void setFactorId(Integer factorId) {
		this.factorId = factorId;
	}

	public Integer getFormulationDosageMappId() {
		return formulationDosageMappId;
	}

	public void setFormulationDosageMappId(Integer formulationDosageMappId) {
		this.formulationDosageMappId = formulationDosageMappId;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getDurationUnit() {
		return durationUnit;
	}

	public void setDurationUnit(String durationUnit) {
		this.durationUnit = durationUnit;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public Integer getSaleDetailId() {
		return saleDetailId;
	}

	public void setSaleDetailId(Integer saleDetailId) {
		this.saleDetailId = saleDetailId;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public Boolean getIsLasa() {
		return isLasa;
	}

	public void setIsLasa(Boolean isLasa) {
		this.isLasa = isLasa;
	}
}
