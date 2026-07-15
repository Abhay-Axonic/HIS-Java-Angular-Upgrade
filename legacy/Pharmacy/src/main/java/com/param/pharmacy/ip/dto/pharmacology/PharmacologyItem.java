package com.param.pharmacy.ip.dto.pharmacology;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.BatchResponse;
import com.param.global.dto.items.UomResponse;

@JsonInclude(Include.NON_NULL)
public class PharmacologyItem {
	public PharmacologyItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	private Long id;

	private Integer issueId;

	private Long prescriptionDetailId;
	
	private Integer addedItemId;
	//private Integer productCategoryId;

	private Integer assignPickerDetailId;

	private Integer itemId;

	private String itemCode;

	private String itemName;

	private String interventionRemark;

	private Integer genericId;

	private String genericName;

	private String frequency;

	private Double dose;
//	private String doseUnit;

	private Date startDate;

	private Date stopDate;

	private Integer statusId;

	private String status;

	private boolean interventionFlag;

	private Integer quantityOrdered;

	private Integer quantityIssued;

	private Integer quantity;

	private Long issuedQuantity;

	private Long pendingIssueQuantity;

	private Integer manufacturerId;

	private String manufacturerName;

	private Long stockAvailable;

	private List<BatchResponse> batch;
	private List<PharmacologyItem> selectedItems;
	private Long batchId;
	private BigInteger batchIdNew;

	private String batchNo;
	private String rackNo;
	private Date expiry;

	private String uomType;

	private Integer uomTypeId;

	private Integer uomUnitId;
	private Integer formulationTypeId;

	private String uomUnit;

	private Integer taxId;

	private String tax;

	private Double taxPercentage;

	private Integer orderDetailId;

	private String picker;

	private Integer pickerId;

	private Long prescriptionId;

	protected Integer markUpDown;

	protected Integer productCategoryId;

	private String doseUnit;
	
	protected Integer pickedQuantity;
	protected Integer assignPickerId;

	private boolean isAddedItem;
	private Integer storeId;

	Double patientClassPercentage;
	private Boolean isFromAssignPicker;
	boolean markupStatus;
	private Boolean narcoticFlag;
	private Boolean isNarcotic;
	private Boolean isOtc;

	Integer priorityId;
	private Integer parentAssignPickerId;
	
	private Double netAmount;
	private Double rate;
	private Double totalAmount;
	private Double selfPayable;
	private Double creditPayable;
	private Double discountAmount;
	private Double taxAmount;
	private Double itemDiscount;
	private Double patientClassAmount;

	private Integer parentSubstituteId;
	private Boolean isBatchRequired;
	private Integer parentIssueDetailsId;
	private Integer IssueDetailsId;
	private Double packageConsumptionAmt;
	private Integer tempBatchId;
	private String instructionDesc;
	private String frequencyDesc;
	private String details;
	private BigInteger tempPrescriptionId;
	private Integer issueDetailId;
	private Integer qtyPrescribed;
	private Integer issuedQty;
	private Integer quantityDispensedTemp;
	private BigInteger pricId;
	private Integer genProdCatId;
	private Integer productCatId;
	private Integer assetCategoryId;
	private boolean infusionflag;
	private boolean isSolution;
	
    private boolean isThreeUOMApplicable;

	private Integer storageUnitId;
    
    private String durationUnit;
	private Boolean isLasa;
    
    public Integer getStorageUnitId() {
		return storageUnitId;
	}

	public void setStorageUnitId(Integer storageUnitId) {
		this.storageUnitId = storageUnitId;
	}

    
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

	List<UomResponse> uoms;
	 
	public List<UomResponse> getUoms() {
		return uoms;
	}

	public void setUoms(List<UomResponse> uoms) {
		this.uoms = uoms;
	}

	public String getFrequencyDesc() {
		return frequencyDesc;
	}

	public void setFrequencyDesc(String frequencyDesc) {
		this.frequencyDesc = frequencyDesc;
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

	public Integer getPickedQuantity() {
		return pickedQuantity;
	}

	public void setPickedQuantity(Integer pickedQuantity) {
		this.pickedQuantity = pickedQuantity;
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

	protected Double markUpDownPer;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public boolean isInterventionFlag() {
		return interventionFlag;
	}

	public void setInterventionFlag(boolean interventionFlag) {
		this.interventionFlag = interventionFlag;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Integer getQuantityOrdered() {
		return quantityOrdered;
	}

	public void setQuantityOrdered(Integer quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
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

	public String getInterventionRemark() {
		return interventionRemark;
	}

	public void setInterventionRemark(String interventionRemark) {
		this.interventionRemark = interventionRemark;
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

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public Integer getQuantityIssued() {
		return quantityIssued;
	}

	public void setQuantityIssued(Integer quantityIssued) {
		this.quantityIssued = quantityIssued;
	}

	public Long getIssuedQuantity() {
		return issuedQuantity;
	}

	public void setIssuedQuantity(Long issuedQuantity) {
		this.issuedQuantity = issuedQuantity;
	}

	public Integer getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
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

	public List<BatchResponse> getBatch() {
		return batch;
	}

	public void setBatch(List<BatchResponse> batch) {
		this.batch = batch;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
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

	public String getPicker() {
		return picker;
	}

	public void setPicker(String picker) {
		this.picker = picker;
	}

	public Integer getPickerId() {
		return pickerId;
	}

	public void setPickerId(Integer pickerId) {
		this.pickerId = pickerId;
	}

	public Long getPrescriptionDetailId() {
		return prescriptionDetailId;
	}

	public void setPrescriptionDetailId(Long prescriptionDetailId) {
		this.prescriptionDetailId = prescriptionDetailId;
	}

	public Integer getAssignPickerDetailId() {
		return assignPickerDetailId;
	}

	public void setAssignPickerDetailId(Integer assignPickerDetailId) {
		this.assignPickerDetailId = assignPickerDetailId;
	}

	public Long getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(Long prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Long getPendingIssueQuantity() {
		return pendingIssueQuantity;
	}

	public void setPendingIssueQuantity(Long pendingIssueQuantity) {
		this.pendingIssueQuantity = pendingIssueQuantity;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public PharmacologyItem(Long id, Integer itemId, String itemName, String genericName, String frequency, Double dose,
			Date startDate, Date stopDate, boolean interventionFlag) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.genericName = genericName;
		this.frequency = frequency;
		this.dose = dose;
		this.startDate = startDate;
		this.stopDate = stopDate;
		this.interventionFlag = interventionFlag;
	}

	public PharmacologyItem(Long id, Integer itemId, String itemName, String genericName, boolean interventionFlag) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.genericName = genericName;
		this.interventionFlag = interventionFlag;
	}

	public PharmacologyItem(Long id, Integer itemId, String itemCode, String itemName, String genericName,
			String frequency, Double dose, Date startDate, Date stopDate, boolean interventionFlag,
			Integer quantityOrdered, Long stockAvailable, String rackNo, List<BatchResponse> batch) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.frequency = frequency;
		this.dose = dose;
		this.startDate = startDate;
		this.stopDate = stopDate;
		this.interventionFlag = interventionFlag;
		this.quantityOrdered = quantityOrdered;
		this.stockAvailable = stockAvailable;
		this.rackNo = rackNo;
		this.batch = batch;
	}

	public PharmacologyItem(Long id, Integer itemId, String itemCode, String itemName, Integer genericId,
			String genericName, Integer statusId, String status, boolean interventionFlag, Integer quantityOrdered,
			Integer manufacturerId, String manufacturerName, Long stockAvailable, String rackNo,
			List<BatchResponse> batch) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericId = genericId;
		this.genericName = genericName;
		this.statusId = statusId;
		this.status = status;
		this.interventionFlag = interventionFlag;
		this.quantityOrdered = quantityOrdered;
		this.manufacturerId = manufacturerId;
		this.manufacturerName = manufacturerName;
		this.stockAvailable = stockAvailable;
		this.rackNo = rackNo;
		this.batch = batch;
	}

	public PharmacologyItem(Long id, Integer itemId, String itemCode, String itemName, String genericName, Double dose,
			boolean interventionFlag, Integer quantityOrdered, Long stockAvailable, String rackNo,
			List<BatchResponse> batch) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.dose = dose;
		this.interventionFlag = interventionFlag;
		this.quantityOrdered = quantityOrdered;
		this.stockAvailable = stockAvailable;
		this.rackNo = rackNo;
		this.batch = batch;
	}

	public PharmacologyItem(Integer itemId, String itemCode, String itemName, String genericName, Long stockAvailable,
			String storageUnitName, List<BatchResponse> batch, Integer taxId, Integer productCategoryId, Boolean narcoticFlag, String manufacturerName,
			Boolean isOtc , Double taxPercentage) {
		super();
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.stockAvailable = stockAvailable;
		this.storageUnitName = storageUnitName;
		this.batch = batch;
		this.taxId = taxId;
		this.productCategoryId = productCategoryId;
		this.narcoticFlag = narcoticFlag;
		this.manufacturerName = manufacturerName;
		this.isOtc = isOtc;
		this.taxPercentage = taxPercentage;
	}

	public PharmacologyItem(Long id, Integer itemId, String itemCode, String itemName, Integer genericId,
			String genericName, boolean interventionFlag, Integer quantityOrdered, Integer quantityIssued,
			Integer manufacturerId, String manufacturerName, List<BatchResponse> batch, Integer taxId, String tax,
			Double taxPercentage, Boolean isNarcotic) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericId = genericId;
		this.genericName = genericName;
		this.interventionFlag = interventionFlag;
		this.quantityOrdered = quantityOrdered;
		this.quantityIssued = quantityIssued;
		this.manufacturerId = manufacturerId;
		this.manufacturerName = manufacturerName;
		this.batch = batch;
		this.taxId = taxId;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.isNarcotic = isNarcotic;

	}

	public PharmacologyItem(Long id, Integer itemId, String itemCode, String itemName, Integer genericId,
			String genericName, boolean interventionFlag, Integer quantityOrdered, Integer quantityIssued,
			Integer manufacturerId, String manufacturerName, List<BatchResponse> batch, Integer taxId, String tax,
			Double taxPercentage, Long issuedQuantity, Integer pickedQuantity, Boolean isNarcotic) {
		this(id, itemId, itemCode, itemName, genericId, genericName, interventionFlag, quantityOrdered, quantityIssued,
				manufacturerId, manufacturerName, batch, taxId, tax, taxPercentage, isNarcotic);

		this.issuedQuantity = issuedQuantity;
		this.pickedQuantity = pickedQuantity;
		this.isNarcotic = isNarcotic;

	}

	public PharmacologyItem(Long id, Integer itemId, String itemCode, String itemName, Integer genericId,
			String genericName, boolean interventionFlag, Integer quantityOrdered, Integer quantityIssued,
			Integer manufacturerId, String manufacturerName, List<BatchResponse> batch, Integer taxId, String tax,
			Double taxPercentage, Long issuedQuantity, Boolean isNarcotic) {
		this(id, itemId, itemCode, itemName, genericId, genericName, interventionFlag, quantityOrdered, quantityIssued,
				manufacturerId, manufacturerName, batch, taxId, tax, taxPercentage, isNarcotic);

		this.issuedQuantity = issuedQuantity;
	}

	public PharmacologyItem(Long id, Integer itemId, String itemCode, String itemName, Integer genericId,
			String genericName, boolean interventionFlag, Integer quantityOrdered, Integer manufacturerId,
			String manufacturerName, List<BatchResponse> batch, Integer taxId, String tax, Double taxPercentage,
			Integer quantityIssued) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericId = genericId;
		this.genericName = genericName;
		this.interventionFlag = interventionFlag;
		this.quantityOrdered = quantityOrdered;
		this.manufacturerId = manufacturerId;
		this.manufacturerName = manufacturerName;
		this.batch = batch;
		this.taxId = taxId;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.quantityIssued = quantityIssued;
	}

	public PharmacologyItem(Long id, Integer itemId, String itemCode, String itemName, Integer genericId,
			String genericName, boolean interventionFlag, Integer quantityOrdered, Integer manufacturerId,
			String manufacturerName, List<BatchResponse> batch, Integer taxId, String tax, Double taxPercentage,
			Integer quantityIssued, String status, Integer StatusId,  Boolean isNarcotic, boolean isThreeUOMApplicable) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericId = genericId;
		this.genericName = genericName;
		this.interventionFlag = interventionFlag;
		this.quantityOrdered = quantityOrdered;
		this.manufacturerId = manufacturerId;
		this.manufacturerName = manufacturerName;
		this.batch = batch;
		this.taxId = taxId;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.quantityIssued = quantityIssued;
		this.status = status;
		this.statusId = StatusId;
		this.isNarcotic = isNarcotic;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}

	public PharmacologyItem(Long id, Integer itemId, String itemCode, String itemName, Integer genericId,
			String genericName, boolean interventionFlag, Integer quantityOrdered, Integer quantityIssued,
			Integer manufacturerId, String manufacturerName, List<BatchResponse> batch) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericId = genericId;
		this.genericName = genericName;
		this.interventionFlag = interventionFlag;
		this.quantityOrdered = quantityOrdered;
		this.quantityIssued = quantityIssued;
		this.manufacturerId = manufacturerId;
		this.manufacturerName = manufacturerName;
		this.batch = batch;
	}

	public PharmacologyItem(Long id, Integer itemId, String itemCode, String itemName, Integer genericId,
			String genericName, Integer quantityOrdered,
			Integer manufacturerId, String manufacturerName, Long stockAvailable, String rackNo, Long batchId,
			String batchNo, Date expiry, String uomType, String uomUnit, Integer taxId, String tax,
			Double taxPercentage) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericId = genericId;
		this.genericName = genericName;
//		this.frequency = frequency;
//		this.dose = dose;
//		this.startDate = startDate;
//		this.stopDate = stopDate;
		this.quantityOrdered = quantityOrdered;
		this.manufacturerId = manufacturerId;
		this.manufacturerName = manufacturerName;
		this.stockAvailable = stockAvailable;
		this.rackNo = rackNo;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiry = expiry;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.taxId = taxId;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
	}

	public String getDoseUnit() {
		return doseUnit;
	}

	public void setDoseUnit(String doseUnit) {
		this.doseUnit = doseUnit;
	}

	public boolean isAddedItem() {
		return isAddedItem;
	}

	public void setAddedItem(boolean isAddedItem) {
		this.isAddedItem = isAddedItem;
	}

	public Integer getAddedItemId() {
		return addedItemId;
	}

	public void setAddedItemId(Integer addedItemId) {
		this.addedItemId = addedItemId;
	}

	public Integer getParentAssignPickerId() {
		return parentAssignPickerId;
	}

	public void setParentAssignPickerId(Integer parentAssignPickerId) {
		this.parentAssignPickerId = parentAssignPickerId;
	}

	public Boolean getIsFromAssignPicker() {
		return isFromAssignPicker;
	}

	public void setIsFromAssignPicker(Boolean isFromAssignPicker) {
		this.isFromAssignPicker = isFromAssignPicker;
	}

	public List<PharmacologyItem> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(List<PharmacologyItem> selectedItems) {
		this.selectedItems = selectedItems;
	}

	public Integer getAssignPickerId() {
		return assignPickerId;
	}

	public void setAssignPickerId(Integer assignPickerId) {
		this.assignPickerId = assignPickerId;
	}

	public Boolean getNarcoticFlag() {
		return narcoticFlag;
	}

	public void setNarcoticFlag(Boolean narcoticFlag) {
		this.narcoticFlag = narcoticFlag;
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

	public Boolean getIsNarcotic() {
		return isNarcotic;
	}

	public void setIsNarcotic(Boolean isNarcotic) {
		this.isNarcotic = isNarcotic;
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

	public Integer getIssueDetailsId() {
		return IssueDetailsId;
	}

	public void setIssueDetailsId(Integer issueDetailsId) {
		IssueDetailsId = issueDetailsId;
	}

	public Boolean getIsOtc() {
		return isOtc;
	}

	public void setIsOtc(Boolean isOtc) {
		this.isOtc = isOtc;
	}

	public Double getPackageConsumptionAmt() {
		return packageConsumptionAmt;
	}

	public void setPackageConsumptionAmt(Double packageConsumptionAmt) {
		this.packageConsumptionAmt = packageConsumptionAmt;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Double getItemDiscount() {
		return itemDiscount;
	}

	public void setItemDiscount(Double itemDiscount) {
		this.itemDiscount = itemDiscount;
	}

	public Integer getTempBatchId() {
		return tempBatchId;
	}

	public void setTempBatchId(Integer tempBatchId) {
		this.tempBatchId = tempBatchId;
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
	
	public Boolean getIsBatchRequired() {
		return this.isBatchRequired;
	}

	public void setIsBatchRequired(Boolean isBatchRequired) {
		this.isBatchRequired = isBatchRequired;
	}

	public Integer getFormulationTypeId() {
		return formulationTypeId;
	}

	public void setFormulationTypeId(Integer formulationTypeId) {
		this.formulationTypeId = formulationTypeId;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	public Integer getIssueDetailId() {
		return issueDetailId;
	}

	public void setIssueDetailId(Integer issueDetailId) {
		this.issueDetailId = issueDetailId;
	}

	public Integer getQtyPrescribed() {
		return qtyPrescribed;
	}

	public void setQtyPrescribed(Integer qtyPrescribed) {
		this.qtyPrescribed = qtyPrescribed;
	}

	public Integer getIssuedQty() {
		return issuedQty;
	}

	public void setIssuedQty(Integer issuedQty) {
		this.issuedQty = issuedQty;
	}

	

	public Integer getGenProdCatId() {
		return genProdCatId;
	}

	public void setGenProdCatId(Integer genProdCatId) {
		this.genProdCatId = genProdCatId;
	}

	public Integer getProductCatId() {
		return productCatId;
	}

	public void setProductCatId(Integer productCatId) {
		this.productCatId = productCatId;
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public BigInteger getTempPrescriptionId() {
		return tempPrescriptionId;
	}

	public void setTempPrescriptionId(BigInteger tempPrescriptionId) {
		this.tempPrescriptionId = tempPrescriptionId;
	}

	public Integer getQuantityDispensedTemp() {
		return quantityDispensedTemp;
	}

	public void setQuantityDispensedTemp(Integer quantityDispensedTemp) {
		this.quantityDispensedTemp = quantityDispensedTemp;
	}

	public BigInteger getPricId() {
		return pricId;
	}

	public void setPricId(BigInteger pricId) {
		this.pricId = pricId;
	}

	public BigInteger getBatchIdNew() {
		return batchIdNew;
	}

	public void setBatchIdNew(BigInteger batchIdNew) {
		this.batchIdNew = batchIdNew;
	}

	public boolean getInfusionflag() {
		return infusionflag;
	}

	public void setInfusionflag(boolean infusionflag) {
		this.infusionflag = infusionflag;
	}

	public boolean getIsSolution() {
		return isSolution;
	}

	public void setIsSolution(boolean isSolution) {
		this.isSolution = isSolution;
	}

	public String getDurationUnit() {
		return durationUnit;
	}

	public void setDurationUnit(String durationUnit) {
		this.durationUnit = durationUnit;
	}

	public Boolean getIsLasa() {
		return isLasa;
	}

	public void setIsLasa(Boolean isLasa) {
		this.isLasa = isLasa;
	}
}
