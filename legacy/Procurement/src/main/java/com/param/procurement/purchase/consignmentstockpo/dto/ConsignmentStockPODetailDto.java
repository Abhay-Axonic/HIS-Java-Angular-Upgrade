package com.param.procurement.purchase.consignmentstockpo.dto;

import java.util.Date;
import java.util.List;

import com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper;
import com.param.procurement.purchase.dto.purchaseOrder.PoOtherChargesMapperRequest;
import com.param.procurement.purchase.dto.purchaseOrder.PurchaseOrderStagedDto;
import com.param.procurement.purchase.dto.purchaseOrder.PurchaseOrderUomRequest;

public class ConsignmentStockPODetailDto {
	
	private Integer id;

	private Integer organizationId;

	private Integer unitId;

	private Integer poId;

	private Integer itemId;

	private Integer poQuantity;

	private Integer bonusQuantity;

	private Double cop;

	private Double mrp;

	private Double discount;
	
	List<Integer> prId;    
	
	List<Integer> qrId;    
	  
	private Integer taxId;

	private Double taxPercentage;

	private Double totalAmount;

	private Double taxAmount;

	private Double netAmount;

	private Double totalDiscount;
	
	private Double totalTaxAmount;
	
	private Double totalOtherAmount;

	private Integer manufactureId;
	
	private Integer statusId;

	private String specification;

	private String remark;

	private Boolean isActive;

	private Integer poUomTypeId;

	private Integer poUomUnitId;

	private Integer bonusUomTypeId;

	private Integer bonusUomUnitId;

	private Integer grnDetailId;
	
	private Integer poApprovedQuantity;
	
	private Integer bonusApprovedQuantity;
	
	private Integer approvedBy;
	
	private Date approvedDate;
	
	private Integer rejectedQuantity;
	
	private Integer rejectReasonId;
	
	private String rejectNote;
	
	private String warrentyPeriod;
	
	private String supportTerms;
	
	private String technicalSpecification;
	
	private String expectedPayment;
	
	private Date warrantyDate;
	
	private Integer serviceProviderId;
	
	PoAccessoriesMapper poAccessoriesMapper;	
	
	private List<PurchaseOrderUomRequest> poUom;
	
	private List<PurchaseOrderUomRequest> bonusUom;
	
	private List<PoOtherChargesMapperRequest> otherCharges;
	
	private List<PurchaseOrderStagedDto> purchaseOrderStaged;
	
	private boolean isFromConsignmentPoCreation;
	
	private Integer pendingQuantity;
	public Integer getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

	public List<Integer> getQrId() {
		return qrId;
	}

	public void setQrId(List<Integer> qrId) {
		this.qrId = qrId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
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
	
	public Integer getManufactureId() {
		return manufactureId;
	}

	public void setManufactureId(Integer manufactureId) {
		this.manufactureId = manufactureId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	public Integer getPoUomTypeId() {
		return poUomTypeId;
	}

	public void setPoUomTypeId(Integer poUomTypeId) {
		this.poUomTypeId = poUomTypeId;
	}

	public Integer getPoUomUnitId() {
		return poUomUnitId;
	}

	public void setPoUomUnitId(Integer poUomUnitId) {
		this.poUomUnitId = poUomUnitId;
	}

	public Integer getBonusUomTypeId() {
		return bonusUomTypeId;
	}

	public void setBonusUomTypeId(Integer bonusUomTypeId) {
		this.bonusUomTypeId = bonusUomTypeId;
	}

	public Integer getBonusUomUnitId() {
		return bonusUomUnitId;
	}

	public void setBonusUomUnitId(Integer bonusUomUnitId) {
		this.bonusUomUnitId = bonusUomUnitId;
	}

	public Integer getGrnDetailId() {
		return grnDetailId;
	}

	public void setGrnDetailId(Integer grnDetailId) {
		this.grnDetailId = grnDetailId;
	}

	public List<Integer> getPrId() {
		return prId;
	}

	public void setPrId(List<Integer> prId) {
		this.prId = prId;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
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

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(Double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public Integer getPoApprovedQuantity() {
		return poApprovedQuantity;
	}

	public void setPoApprovedQuantity(Integer poApprovedQuantity) {
		this.poApprovedQuantity = poApprovedQuantity;
	}

	public Integer getBonusApprovedQuantity() {
		return bonusApprovedQuantity;
	}

	public void setBonusApprovedQuantity(Integer bonusApprovedQuantity) {
		this.bonusApprovedQuantity = bonusApprovedQuantity;
	}

	public Integer getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Integer approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public Integer getRejectedQuantity() {
		return rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
	}

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public Double getTotalOtherAmount() {
		return totalOtherAmount;
	}

	public void setTotalOtherAmount(Double totalOtherAmount) {
		this.totalOtherAmount = totalOtherAmount;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getWarrentyPeriod() {
		return warrentyPeriod;
	}

	public void setWarrentyPeriod(String warrentyPeriod) {
		this.warrentyPeriod = warrentyPeriod;
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

	public List<PurchaseOrderUomRequest> getPoUom() {
		return poUom;
	}

	public void setPoUom(List<PurchaseOrderUomRequest> poUom) {
		this.poUom = poUom;
	}

	public List<PurchaseOrderUomRequest> getBonusUom() {
		return bonusUom;
	}

	public void setBonusUom(List<PurchaseOrderUomRequest> bonusUom) {
		this.bonusUom = bonusUom;
	}

	public List<PoOtherChargesMapperRequest> getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(List<PoOtherChargesMapperRequest> otherCharges) {
		this.otherCharges = otherCharges;
	}

	public List<PurchaseOrderStagedDto> getPurchaseOrderStaged() {
		return purchaseOrderStaged;
	}

	public void setPurchaseOrderStaged(List<PurchaseOrderStagedDto> purchaseOrderStaged) {
		this.purchaseOrderStaged = purchaseOrderStaged;
	}

	public PoAccessoriesMapper getPoAccessoriesMapper() {
		return poAccessoriesMapper;
	}

	public void setPoAccessoriesMapper(PoAccessoriesMapper poAccessoriesMapper) {
		this.poAccessoriesMapper = poAccessoriesMapper;
	}

	public Date getWarrantyDate() {
		return warrantyDate;
	}

	public void setWarrantyDate(Date warrantyDate) {
		this.warrantyDate = warrantyDate;
	}

	public Integer getServiceProviderId() {
		return serviceProviderId;
	}

	public void setServiceProviderId(Integer serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}

	public boolean getIsFromConsignmentPoCreation() {
		return isFromConsignmentPoCreation;
	}

	public void setIsFromConsignmentPoCreation(boolean isFromConsignmentPoCreation) {
		this.isFromConsignmentPoCreation = isFromConsignmentPoCreation;
	}

	
	
}
