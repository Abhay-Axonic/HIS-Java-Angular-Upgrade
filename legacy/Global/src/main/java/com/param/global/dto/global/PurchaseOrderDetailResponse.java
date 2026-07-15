package com.param.global.dto.global;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.param.entity.model.procurement.GRNStagedQuantity;
import com.param.global.dto.items.UomResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseOrderDetailResponse {

	private Integer id;

	private Integer itemId;

	private String itemCode;

	private String itemName;

	private Integer poQuantity;

	private Integer poUomTypeId;

	private String poUomType;

	private Integer poUomUnitId;

	private String poUomUnit;

	private Integer bonusQuantity;
	private Integer pendingQuantity;

	private Integer bonusUomTypeId;

	private String bonusUomType;

	private Integer bonusUomUnitId;

	private String bonusUomUnit;

	private Double cop;

	private Double mrp;

	private Double discount;

	private Double discountAmount;

	private Double taxPercentage;

	private Integer taxId;

	private String taxName;

	private Double taxPercent;

	private Double totalOtherAmount;

	private Integer genericId;

	private String genericName;

	private Integer manufacturerId;

	private String manufacturerName;

	private Integer leaseQuantity;

	private String specification;

	private Integer poLeaseQuantity;

	private Integer bonusLeaseQuantity;

	private Long maxStoreQuantity;

	private Long maxOrderQuantity;

	private List<UomResponse> uom;

	private List<PurchaseOrderStaged> poStagedList;

	private List<PurchaseOrderStagedDcDate> grnStagedLists;

	private List<POOtherCharges> poOtherCharges;

	private Integer poGrnQuantity;

	private Integer poPendingQuantity;

	private Integer purchaseOrderId;

	private Integer grnApprovedAndReceivedSum;

	private Integer poId;

	private Integer poDetailId;
	private Integer fromstoreid;
	private String fromstorename;
	private boolean isBatchRequired;
	private boolean isThreeUOMApplicable;
	
	private String dueDate;
	
    public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }
	 
	public boolean isBatchRequired() {
		return isBatchRequired;
	}

	public void setBatchRequired(boolean isBatchRequired) {
		this.isBatchRequired = isBatchRequired;
	}

	public Integer getFromstoreid() {
		return fromstoreid;
	}

	public void setFromstoreid(Integer fromstoreid) {
		this.fromstoreid = fromstoreid;
	}

	public String getFromstorename() {
		return fromstorename;
	}

	public void setFromstorename(String fromstorename) {
		this.fromstorename = fromstorename;
	}

	public Integer getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

	// start code for HIS-9505 GRN form asset against PO issues - Approve quantity
	// is showing 0
	private String remark;
	private String warrantyPeriod;
	private String supportTerms;
	private String technicalSpecification;
	private String expectedPayment;
	private Integer serviceProviderId;
	private String serviceProvider;
	private List<PoAccessoriesMapperGlobal> poAccessoriesMapper;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getWarrantyPeriod() {
		return warrantyPeriod;
	}

	public void setWarrantyPeriod(String warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}

	public String getSupportTerms() {
		return supportTerms;
	}

	public void setSupportTerms(String supportTerms) {
		this.supportTerms = supportTerms;
	}

	public String getTechnicalSpecification() {
		return technicalSpecification;
	}

	public void setTechnicalSpecification(String technicalSpecification) {
		this.technicalSpecification = technicalSpecification;
	}

	public String getExpectedPayment() {
		return expectedPayment;
	}

	public void setExpectedPayment(String expectedPayment) {
		this.expectedPayment = expectedPayment;
	}

	public Integer getServiceProviderId() {
		return serviceProviderId;
	}

	public void setServiceProviderId(Integer serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}

	public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public List<PoAccessoriesMapperGlobal> getPoAccessoriesMapper() {
		return poAccessoriesMapper;
	}

	public void setPoAccessoriesMapper(List<PoAccessoriesMapperGlobal> poAccessoriesMapper) {
		this.poAccessoriesMapper = poAccessoriesMapper;
	}
	// end code for HIS-9505 GRN form asset against PO issues - Approve quantity is
	// showing 0

	public PurchaseOrderDetailResponse() {
		super();
	}

	public PurchaseOrderDetailResponse(Integer id, Integer itemId, String itemCode, String itemName, Integer poQuantity,
			Integer bonusQuantity, Double cop, Double mrp) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.poQuantity = poQuantity;
		this.bonusQuantity = bonusQuantity;
		this.cop = cop;
		this.mrp = mrp;

	}

	public PurchaseOrderDetailResponse(Integer id, Integer itemId, String itemCode, String itemName, Integer poQuantity,
			Integer bonusQuantity, Double cop, Double mrp, Integer taxId, String taxName, Double taxPercent,
			Integer genericId, String genericName, Integer manufacturerId, String manufacturerName,
			List<UomResponse> uom) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.poQuantity = poQuantity;
		this.bonusQuantity = bonusQuantity;
		this.cop = cop;
		this.mrp = mrp;
		this.taxId = taxId;
		this.taxName = taxName;
		this.taxPercent = taxPercent;
		this.genericId = genericId;
		this.genericName = genericName;
		this.manufacturerId = manufacturerId;
		this.manufacturerName = manufacturerName;
		this.uom = uom;
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

	public Integer getPoQuantity() {
		return poQuantity;
	}

	public void setPoQuantity(Integer poQuantity) {
		this.poQuantity = poQuantity;
	}

	public Integer getBonusQuantity() {
		return bonusQuantity;
	}

	public void setBonusQuantity(Integer bonusQuantity) {
		this.bonusQuantity = bonusQuantity;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public Double getTaxPercent() {
		return taxPercent;
	}

	public void setTaxPercent(Double taxPercent) {
		this.taxPercent = taxPercent;
	}

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
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

	public List<PurchaseOrderStaged> getPoStagedList() {
		return poStagedList;
	}

	public void setPoStagedList(List<PurchaseOrderStaged> poStagedList) {
		this.poStagedList = poStagedList;
	}

	public Integer getPoUomTypeId() {
		return poUomTypeId;
	}

	public void setPoUomTypeId(Integer poUomTypeId) {
		this.poUomTypeId = poUomTypeId;
	}

	public String getPoUomType() {
		return poUomType;
	}

	public void setPoUomType(String poUomType) {
		this.poUomType = poUomType;
	}

	public Integer getPoUomUnitId() {
		return poUomUnitId;
	}

	public void setPoUomUnitId(Integer poUomUnitId) {
		this.poUomUnitId = poUomUnitId;
	}

	public String getPoUomUnit() {
		return poUomUnit;
	}

	public void setPoUomUnit(String poUomUnit) {
		this.poUomUnit = poUomUnit;
	}

	public Integer getBonusUomTypeId() {
		return bonusUomTypeId;
	}

	public void setBonusUomTypeId(Integer bonusUomTypeId) {
		this.bonusUomTypeId = bonusUomTypeId;
	}

	public String getBonusUomType() {
		return bonusUomType;
	}

	public void setBonusUomType(String bonusUomType) {
		this.bonusUomType = bonusUomType;
	}

	public Integer getBonusUomUnitId() {
		return bonusUomUnitId;
	}

	public void setBonusUomUnitId(Integer bonusUomUnitId) {
		this.bonusUomUnitId = bonusUomUnitId;
	}

	public String getBonusUomUnit() {
		return bonusUomUnit;
	}

	public void setBonusUomUnit(String bonusUomUnit) {
		this.bonusUomUnit = bonusUomUnit;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Double getTotalOtherAmount() {
		return totalOtherAmount;
	}

	public void setTotalOtherAmount(Double totalOtherAmount) {
		this.totalOtherAmount = totalOtherAmount;
	}

	public List<POOtherCharges> getPoOtherCharges() {
		return poOtherCharges;
	}

	public void setPoOtherCharges(List<POOtherCharges> poOtherCharges) {
		this.poOtherCharges = poOtherCharges;
	}

	public Integer getLeaseQuantity() {
		return leaseQuantity;
	}

	public void setLeaseQuantity(Integer leaseQuantity) {
		this.leaseQuantity = leaseQuantity;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Integer getPoLeaseQuantity() {
		return poLeaseQuantity;
	}

	public void setPoLeaseQuantity(Integer poLeaseQuantity) {
		this.poLeaseQuantity = poLeaseQuantity;
	}

	public Integer getBonusLeaseQuantity() {
		return bonusLeaseQuantity;
	}

	public void setBonusLeaseQuantity(Integer bonusLeaseQuantity) {
		this.bonusLeaseQuantity = bonusLeaseQuantity;
	}

	public Long getMaxStoreQuantity() {
		return maxStoreQuantity;
	}

	public void setMaxStoreQuantity(Long maxStoreQuantity) {
		this.maxStoreQuantity = maxStoreQuantity;
	}

	public Long getMaxOrderQuantity() {
		return maxOrderQuantity;
	}

	public void setMaxOrderQuantity(Long maxOrderQuantity) {
		this.maxOrderQuantity = maxOrderQuantity;
	}

	public Integer getPoGrnQuantity() {
		return poGrnQuantity;
	}

	public void setPoGrnQuantity(Integer poGrnQuantity) {
		this.poGrnQuantity = poGrnQuantity;
	}

	public Integer getPoPendingQuantity() {
		return poPendingQuantity;
	}

	public void setPoPendingQuantity(Integer poPendingQuantity) {
		this.poPendingQuantity = poPendingQuantity;
	}

	public List<PurchaseOrderStagedDcDate> getGrnStagedLists() {
		return grnStagedLists;
	}

	public void setGrnStagedLists(List<PurchaseOrderStagedDcDate> grnStagedLists) {
		this.grnStagedLists = grnStagedLists;
	}

	public Integer getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(Integer purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	public Integer getGrnApprovedAndReceivedSum() {
		return grnApprovedAndReceivedSum;
	}

	public void setGrnApprovedAndReceivedSum(Integer grnApprovedAndReceivedSum) {
		this.grnApprovedAndReceivedSum = grnApprovedAndReceivedSum;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public Integer getPoDetailId() {
		return poDetailId;
	}

	public void setPoDetailId(Integer poDetailId) {
		this.poDetailId = poDetailId;
	}

}
