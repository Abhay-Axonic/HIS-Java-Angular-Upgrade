package com.param.global.billing.dto;

import java.util.Date;
import java.util.List;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.dto.items.BatchResponse;
import com.param.global.dto.items.UomResponse;
import com.param.global.pharmacy.dto.SaleDetailPackageMapperDto;
import com.param.global.pharmacy.dto.SaleDetailPayeeMapperDto;

public class CommonBillingItemsDto {

	protected Integer itemId;
	
	protected Integer issueId;
	
	protected Long batchId;

	protected Double rate;

	protected Integer quantity;

	protected Integer selfQuantity;

	protected Double packageConsumptionAmt;

	protected Double totalAmount;

	protected double discountAmount;

	protected double discount;

	protected Double unitDiscountAmount;

	protected Double netAmount;

	protected Double unitNetAmount;

	protected Integer taxId;

	protected Double taxPercentage;

	protected Double taxAmount;

	protected Integer productCategoryId;

	protected Integer assetCategoryId;

	protected Integer payeeId;

	protected Integer contractId;

	protected Double creditPayable;

	protected Double selfPayable;

	protected Boolean isConsumed;

	protected Integer index;

	protected Integer childIndex;

	protected double apportionedRate;

	protected Double disallowedAmount;

	protected Integer leaseQuantity;

	protected Integer statusId;

	protected Integer uomTypeId;

	protected Integer uomUnitId;

	protected Integer id;// saleDetailsId

	protected Integer prescriptionDetailId;

	protected Integer allocationId;
	
	protected List<Integer> allocationIds;

	protected Integer instructionId;

	protected Integer patientId;

	protected Integer admissionId;

	protected Integer encounterId;

	protected String administrationTime;

	protected String remark;

	protected Integer creditItemQuantity;

	private Integer consumableQuantity;

	protected Boolean isBatchReq;

	protected Date dateAdministrationDate;

	protected Integer tempBatchId;

	protected List<SaleDetailPayeeMapperDto> saleDetailPayeeMapperDtoList;

	protected List<SaleDetailPackageMapperDto> saleDetailPackageMapperDtoList;

	private double consessionPercentage;

	private double consessionAmount;

	private double unitConsessionAmount;

	protected Integer markUpDown;

	protected double markUpDownPer;

	double patientClassPercentage;

	boolean markupStatus;
	
	protected double contractRate;
	
	private Integer consumedItemId;
	
	private Integer consumedPcategroyId;
	private Integer prescriptionId;

	Integer priorityId;

	protected Integer consumedQuantity;
	
	private Integer parentItemId;
	
	private Double discountPercentage;
	
	private boolean preAuthExist;
	
	private boolean addPreAuth;
	
	protected String instructionDesc;
	
	protected Integer batchAllocationId;
	
	private Double oldConcessionPercentage;
	
	private Double oldDiscountPercentage;
	
	private boolean atCop;
	
    protected List<UomResponse> uomList;

	protected Boolean isLasa;

    private Integer grnId;

    private String grnNumber;

    public List<UomResponse> getUomList() {
        return uomList;
    }

    public void setUomList(List<UomResponse> uomList) {
        this.uomList = uomList;
    }

	public Double getOldConcessionPercentage() {
		return oldConcessionPercentage;
	}

	public void setOldConcessionPercentage(Double oldConcessionPercentage) {
		this.oldConcessionPercentage = oldConcessionPercentage;
	}

	public Double getOldDiscountPercentage() {
		return oldDiscountPercentage;
	}

	public void setOldDiscountPercentage(Double oldDiscountPercentage) {
		this.oldDiscountPercentage = oldDiscountPercentage;
	}

	public Integer getBatchAllocationId() {
		return batchAllocationId;
	}

	public void setBatchAllocationId(Integer batchAllocationId) {
		this.batchAllocationId = batchAllocationId;
	}

	public boolean isAddPreAuth() {
		return addPreAuth;
	}

	public void setAddPreAuth(boolean addPreAuth) {
		this.addPreAuth = addPreAuth;
	}

	public boolean isPreAuthExist() {
		return preAuthExist;
	}

	public void setPreAuthExist(boolean preAuthExist) {
		this.preAuthExist = preAuthExist;
	}

	public Double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public Double getPatientClassPercentage() {
		return patientClassPercentage;
	}

	public Integer getConsumedQuantity() {
		return consumedQuantity;
	}

	public void setConsumedQuantity(Integer consumedQuantity) {
		this.consumedQuantity = consumedQuantity;
	}

	public void setPatientClassPercentage(double patientClassPercentage) {
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

	public double getConsessionPercentage() {
		return consessionPercentage;
	}

	public void setConsessionPercentage(double consessionPercentage) {
		this.consessionPercentage = consessionPercentage;
	}

	public double getConsessionAmount() {
		return consessionAmount;
	}

	public void setConsessionAmount(double consessionAmount) {
		this.consessionAmount = consessionAmount;
	}

	public double getUnitConsessionAmount() {
		return unitConsessionAmount;
	}

	public void setUnitConsessionAmount(double unitConsessionAmount) {
		this.unitConsessionAmount = unitConsessionAmount;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getSelfQuantity() {
		return selfQuantity;
	}

	public void setSelfQuantity(Integer selfQuantity) {
		this.selfQuantity = selfQuantity;
	}

	public Double getPackageConsumptionAmt() {
		return packageConsumptionAmt == null ? 0 : GlobalCommonDateUtils.getAmount(packageConsumptionAmt);
	}

	public void setPackageConsumptionAmt(Double packageConsumptionAmt) {
		this.packageConsumptionAmt = packageConsumptionAmt;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Double getUnitDiscountAmount() {
		return unitDiscountAmount;
	}

	public void setUnitDiscountAmount(Double unitDiscountAmount) {
		this.unitDiscountAmount = unitDiscountAmount;
	}

	public Double getNetAmount() {
		return netAmount == null ? 0 : GlobalCommonDateUtils.getAmount(netAmount);
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Double getCreditPayable() {
		return creditPayable == null ? 0 : GlobalCommonDateUtils.getAmount(creditPayable);
	}

	public void setCreditPayable(Double creditPayable) {
		this.creditPayable = creditPayable;
	}

	public Double getSelfPayable() {
		return selfPayable == null ? 0 : GlobalCommonDateUtils.getAmount(selfPayable);
	}

	public void setSelfPayable(Double selfPayable) {
		this.selfPayable = selfPayable;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getChildIndex() {
		return childIndex;
	}

	public void setChildIndex(Integer childIndex) {
		this.childIndex = childIndex;
	}

	public List<SaleDetailPayeeMapperDto> getSaleDetailPayeeMapperDtoList() {
		return saleDetailPayeeMapperDtoList;
	}

	public void setSaleDetailPayeeMapperDtoList(List<SaleDetailPayeeMapperDto> saleDetailPayeeMapperDtoList) {
		this.saleDetailPayeeMapperDtoList = saleDetailPayeeMapperDtoList;
	}

	public List<SaleDetailPackageMapperDto> getSaleDetailPackageMapperDtoList() {
		return saleDetailPackageMapperDtoList;
	}

	public void setSaleDetailPackageMapperDtoList(List<SaleDetailPackageMapperDto> saleDetailPackageMapperDtoList) {
		this.saleDetailPackageMapperDtoList = saleDetailPackageMapperDtoList;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Long getBatchId() {
		return (tempBatchId != null && tempBatchId > 0) ? Long.valueOf(tempBatchId) : batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = (tempBatchId != null && tempBatchId > 0) ? Long.valueOf(tempBatchId) : batchId;
	}

	public Boolean getIsConsumed() {
		return isConsumed;
	}

	public void setIsConsumed(Boolean isConsumed) {
		this.isConsumed = isConsumed;
	}

	public Double getUnitNetAmount() {
		return unitNetAmount;
	}

	public void setUnitNetAmount(Double unitNetAmount) {
		this.unitNetAmount = unitNetAmount;
	}

	public double getApportionedRate() {
		return apportionedRate;
	}

	public void setApportionedRate(double apportionedRate) {
		this.apportionedRate = apportionedRate;
	}

	public Double getDisallowedAmount() {
		return disallowedAmount;
	}

	public void setDisallowedAmount(Double disallowedAmount) {
		this.disallowedAmount = disallowedAmount;
	}

	public Integer getLeaseQuantity() {
		return leaseQuantity;
	}

	public void setLeaseQuantity(Integer leaseQuantity) {
		this.leaseQuantity = leaseQuantity;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPrescriptionDetailId() {
		return prescriptionDetailId;
	}

	public void setPrescriptionDetailId(Integer prescriptionDetailId) {
		this.prescriptionDetailId = prescriptionDetailId;
	}

	public Integer getAllocationId() {
		return allocationId;
	}

	public void setAllocationId(Integer allocationId) {
		this.allocationId = allocationId;
	}

	public Integer getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(Integer instructionId) {
		this.instructionId = instructionId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public String getAdministrationTime() {
		return administrationTime;
	}

	public void setAdministrationTime(String administrationTime) {
		this.administrationTime = administrationTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCreditItemQuantity() {
		return creditItemQuantity;
	}

	public void setCreditItemQuantity(Integer creditItemQuantity) {
		this.creditItemQuantity = creditItemQuantity;
	}

	public Integer getConsumableQuantity() {
		return consumableQuantity;
	}

	public void setConsumableQuantity(Integer consumableQuantity) {
		this.consumableQuantity = consumableQuantity;
	}

	public Boolean getIsBatchReq() {
		return isBatchReq;
	}

	public void setIsBatchReq(Boolean isBatchReq) {
		this.isBatchReq = isBatchReq;
	}

	public Date getDateAdministrationDate() {
		return dateAdministrationDate;
	}

	public void setDateAdministrationDate(Date dateAdministrationDate) {
		this.dateAdministrationDate = dateAdministrationDate;
	}

	public Integer getTempBatchId() {
		return tempBatchId;
	}

	public void setTempBatchId(Integer tempBatchId) {
		this.tempBatchId = tempBatchId;
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public Integer getMarkUpDown() {
		return markUpDown;
	}

	public void setMarkUpDown(Integer markUpDown) {
		this.markUpDown = markUpDown;
	}

	public double getMarkUpDownPer() {
		return markUpDownPer;
	}

	public void setMarkUpDownPer(double markUpDownPer) {
		this.markUpDownPer = markUpDownPer;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getContractRate() {
		return contractRate;
	}

	public void setContractRate(double contractRate) {
		this.contractRate = contractRate;
	}

	public Integer getConsumedItemId() {
		return consumedItemId;
	}

	public void setConsumedItemId(Integer consumedItemId) {
		this.consumedItemId = consumedItemId;
	}

	public Integer getConsumedPcategroyId() {
		return consumedPcategroyId;
	}

	public void setConsumedPcategroyId(Integer consumedPcategroyId) {
		this.consumedPcategroyId = consumedPcategroyId;
	}

	public Integer getParentItemId() {
		return parentItemId;
	}

	public void setParentItemId(Integer parentItemId) {
		this.parentItemId = parentItemId;
	}

	public List<Integer> getAllocationIds() {
		return allocationIds;
	}

	public void setAllocationIds(List<Integer> allocationIds) {
		this.allocationIds = allocationIds;
	}

	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	private List<BatchResponse> batchList;
	public List<BatchResponse> getBatchList() {
		return batchList;
	}

	public void setBatchList(List<BatchResponse> batchList) {
		this.batchList = batchList;
	}

	public Integer getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(Integer prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public String getInstructionDesc() {
		return instructionDesc;
	}

	public void setInstructionDesc(String instructionDesc) {
		this.instructionDesc = instructionDesc;
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

    public Integer getGrnId() {
        return grnId;
    }

    public void setGrnId(Integer grnId) {
        this.grnId = grnId;
    }

    public String getGrnNumber() {
        return grnNumber;
    }

    public void setGrnNumber(String grnNumber) {
        this.grnNumber = grnNumber;
    }
}
