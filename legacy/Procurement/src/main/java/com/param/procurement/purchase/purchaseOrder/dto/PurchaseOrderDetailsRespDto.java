package com.param.procurement.purchase.purchaseOrder.dto;

import java.util.Date;
import java.util.List;

import com.param.entity.model.master.Status;
import com.param.global.dto.items.UomResponse;
import com.param.procurement.purchase.dto.purchaseOrder.PoOtherChargesMapperDto;
import com.param.procurement.purchase.dto.purchaseOrder.PoPurchaseRequestArray;
import com.param.procurement.purchase.dto.purchaseOrder.PurchaseOrderStagedDto;
import com.param.procurement.purchase.dto.purchaseOrder.PurchaseOrderUomMapperDto;

public class PurchaseOrderDetailsRespDto {
		
	private Integer id;

	private Integer itemId;
	
	private String itemCode;
	
	private String itemName;
	private String warrantyPeriod;
	//private String supportTerms;
	
	public String getWarrantyPeriod() {
		return warrantyPeriod;
	}

	public void setWarrantyPeriod(String warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}

	private Integer prQuantity;

	private Integer poQuantity;
	
	private Integer poUomTypeId;
	
	private String poUomType;

	private Integer poUomUnitId;
	
	private Integer qrdid;
	
	private Long stockAvailable;
	
	private Double roundOffAmount;
	
	private Double totalNetAmount;
	
	public Double getRoundOffAmount() {
		return roundOffAmount;
	}

	public void setRoundOffAmount(Double roundOffAmount) {
		this.roundOffAmount = roundOffAmount;
	}

	public Integer getQrdid() {
		return qrdid;
	}

	public void setQrdid(Integer qrdid) {
		this.qrdid = qrdid;
	}

	private String poUomUnit;
	
	private Integer bonusQuantity;
	private Integer pendingQuantity;
	
	public Integer getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

	private Integer bonusUomTypeId;
	
	private String bonusUomType;

	private Integer bonusUomUnitId;
	
	private String bonusUomUnit;	

	private Double cop;

	private Double mrp;

	private Double discount;
	
	private List<PoPurchaseRequestArray> prId;   
	
	private List<QrDateDto> qrDateDto;
	  
	public List<QrDateDto> getQrDateDto() {
		return qrDateDto;
	}

	public void setQrDateDto(List<QrDateDto> qrDateDto) {
		this.qrDateDto = qrDateDto;
	}

	private Integer taxId;
	
	private String taxCode;

	private Double taxPercentage;

	private Double totalAmount;

	private Double taxAmount;

	private Double netAmount;

	private Double totalDiscount;
	
	private Double totalOtherAmount;
	
    private Integer manufacturerId;
	
	private String manufacturerName;

	private String specification;

	private String remark;	    
	
	private Integer statusId;
	
	private String status;
	
	private Integer approvedQuantity;
	
	private Integer bonusApprovedQuantity;
	
	private Integer rejectQuantity;
	
    private String warrentyPeriod;
	
	private String supportTerms;
	
	private String technicalSpecification;
	
	private String expectedPayment;
	
    private Integer serviceProviderId;
	
	private String serviceProvider;
	
	private Date warrantyDate;
	
	private Integer leaseQuantity;
	
	private Integer maxStoreQuantity;
	
	private Long maxOrderQuantity;
	
	private List<UomResponse> uom;	
	
	private List<PurchaseOrderUomMapperDto> poUom;
	
	private List<PurchaseOrderUomMapperDto> bonusUom;
	
//	private List<PoOtherChargesMapperDto> otherCharges;
	
	private List<PurchaseOrderStagedDto> purchaseOrderStagedDto;
	
	private List<PurchaseOrderStagedDto> initialPurchaseOrderStagedDto;
	
	private List<com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper> poAccessoriesMapper;
	
	
	private Boolean isActive;
	
	private Boolean isDeleted;
	
	private Integer storeId;
	
	private Integer assetTypeId;
	
	//added by minesh
	private Integer grnId;
	private Integer grnDetailId;
	private String grnNumber;
	private Date grnDate;
	private String batchNo;
	private Long batchId;
	private String lotNo;
	private Integer totalConsignmentStock;
	private Integer consumedQty;
	private Integer convertedConsignmentStock;
	private Integer availableConsignmentStock;
	private Integer returnedConsignmentStock;
	private Integer storeStockTransfered;
	private boolean isThreeUOMApplicable;
	private Integer poApprovedQuantity;

    public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Integer getAvailableConsignmentStock() {
		return availableConsignmentStock;
	}

	public void setAvailableConsignmentStock(Integer availableConsignmentStock) {
		this.availableConsignmentStock = availableConsignmentStock;
	}

	public Integer getReturnedConsignmentStock() {
		return returnedConsignmentStock;
	}

	public void setReturnedConsignmentStock(Integer returnedConsignmentStock) {
		this.returnedConsignmentStock = returnedConsignmentStock;
	}

	public Integer getStoreStockTransfered() {
		return storeStockTransfered;
	}

	public void setStoreStockTransfered(Integer storeStockTransfered) {
		this.storeStockTransfered = storeStockTransfered;
	}

	public Integer getGrnId() {
		return grnId;
	}

	public void setGrnId(Integer grnId) {
		this.grnId = grnId;
	}

	public Integer getGrnDetailId() {
		return grnDetailId;
	}

	public void setGrnDetailId(Integer grnDetailId) {
		this.grnDetailId = grnDetailId;
	}

	public String getGrnNumber() {
		return grnNumber;
	}

	public void setGrnNumber(String grnNumber) {
		this.grnNumber = grnNumber;
	}

	public Date getGrnDate() {
		return grnDate;
	}

	public void setGrnDate(Date grnDate) {
		this.grnDate = grnDate;
	}
	
	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public Integer getTotalConsignmentStock() {
		return totalConsignmentStock;
	}

	public void setTotalConsignmentStock(Integer totalConsignmentStock) {
		this.totalConsignmentStock = totalConsignmentStock;
	}

	public Integer getConsumedQty() {
		return consumedQty;
	}

	public void setConsumedQty(Integer consumedQty) {
		this.consumedQty = consumedQty;
	}

	public Integer getConvertedConsignmentStock() {
		return convertedConsignmentStock;
	}

	public void setConvertedConsignmentStock(Integer convertedConsignmentStock) {
		this.convertedConsignmentStock = convertedConsignmentStock;
	}

	
	//end code by minesh
	public PurchaseOrderDetailsRespDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public PurchaseOrderDetailsRespDto(Integer id, Integer itemId, String itemCode, String itemName, Integer poQuantity,Integer prQuantity,Integer leaseQuantity,Integer maxStoreQuantity,Long maxOrderQuantity,
			Integer poUomTypeId, String poUomType, Integer poUomUnitId, String poUomUnit, Integer bonusQuantity,
			Integer bonusUomTypeId, String bonusUomType, Integer bonusUomUnitId, String bonusUomUnit, Double cop,
			Double mrp, Double discount, Integer taxId, String taxCode, Double taxPercentage,
			Double totalAmount, Double taxAmount, Double netAmount, Double totalDiscount, 
			Double totalOtherAmount, Integer statusId,String status,Integer manufacturerId, String manufacturerName, String specification, String remark,Integer approvedQuantity,Integer bonusApprovedQuantity,Integer rejectQuantity,
			List<UomResponse> uom, List<PurchaseOrderUomMapperDto> poUom,
			List<PurchaseOrderUomMapperDto> bonusUom,List<PoPurchaseRequestArray> prId,List<PurchaseOrderStagedDto> purchaseOrderStagedDto, List<PurchaseOrderStagedDto> initialPurchaseOrderStagedDto) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.poQuantity = poQuantity;
		this.prQuantity = prQuantity;
		this.leaseQuantity = leaseQuantity;
		this.maxStoreQuantity = maxStoreQuantity;
		this.maxOrderQuantity = maxOrderQuantity;
		this.poUomTypeId = poUomTypeId;
		this.poUomType = poUomType;
		this.poUomUnitId = poUomUnitId;
		this.poUomUnit = poUomUnit;
		this.bonusQuantity = bonusQuantity;
		this.bonusUomTypeId = bonusUomTypeId;
		this.bonusUomType = bonusUomType;
		this.bonusUomUnitId = bonusUomUnitId;
		this.bonusUomUnit = bonusUomUnit;
		this.cop = cop;
		this.mrp = mrp;
		this.discount = discount;
		this.taxId = taxId;
		this.taxCode = taxCode;
		this.taxPercentage = taxPercentage;
		this.totalAmount = totalAmount;
		this.taxAmount = taxAmount;
		this.netAmount = netAmount;
		this.totalDiscount = totalDiscount;
		this.totalOtherAmount = totalOtherAmount;
		this.statusId = statusId;
		this.status = status;
		this.manufacturerId = manufacturerId;
		this.manufacturerName = manufacturerName;
		this.specification = specification;
		this.remark = remark;
		this.approvedQuantity = approvedQuantity;
		this.bonusApprovedQuantity = bonusApprovedQuantity;
		this.rejectQuantity = rejectQuantity;
		this.uom = uom;
		this.poUom = poUom;
		this.bonusUom = bonusUom;
		this.prId = prId;
		//this.otherCharges = otherCharges;
		this.purchaseOrderStagedDto = purchaseOrderStagedDto;
		this.initialPurchaseOrderStagedDto = initialPurchaseOrderStagedDto;
	
		
	}
	
	public PurchaseOrderDetailsRespDto(Integer id, Integer itemId, String itemCode, String itemName, Integer poQuantity,Integer prQuantity,Integer maxStoreQuantity,Long maxOrderQuantity,Integer bonusQuantity,
			Double cop,
			Double mrp, Double discount, Integer taxId, String taxCode, Double taxPercentage,
			Double totalAmount, Double taxAmount, Double netAmount, Double totalDiscount, 
			Double totalOtherAmount, Integer statusId,String status,Integer manufacturerId, String manufacturerName, String specification, String remark,Integer approvedQuantity,Integer bonusApprovedQuantity,Integer rejectQuantity,
			String warrentyPeriod,String supportTerms,String technicalSpecification,String expectedPayment,Integer serviceProviderId,String serviceProvider,Date warrantyDate,
			List<com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper> poAccessoriesMapper,
			List<PoPurchaseRequestArray> prId/*,List<PoOtherChargesMapperDto> otherCharges*/,List<PurchaseOrderStagedDto> purchaseOrderStagedDto, List<PurchaseOrderStagedDto> initialPurchaseOrderStagedDto) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.poQuantity = poQuantity;
		this.prQuantity = prQuantity;
		this.maxStoreQuantity = maxStoreQuantity;
		this.maxOrderQuantity = maxOrderQuantity;
		this.bonusQuantity = bonusQuantity;
		this.cop = cop;
		this.mrp = mrp;
		this.discount = discount;
		this.taxId = taxId;
		this.taxCode = taxCode;
		this.taxPercentage = taxPercentage;
		this.totalAmount = totalAmount;
		this.taxAmount = taxAmount;
		this.netAmount = netAmount;
		this.totalDiscount = totalDiscount;
		this.totalOtherAmount = totalOtherAmount;
		this.statusId = statusId;
		this.status = status;
		this.manufacturerId = manufacturerId;
		this.manufacturerName = manufacturerName;
		this.specification = specification;
		this.remark = remark;
		this.approvedQuantity = approvedQuantity;
		this.bonusApprovedQuantity = bonusApprovedQuantity;
		this.rejectQuantity = rejectQuantity;
		this.warrentyPeriod = warrentyPeriod;
		this.supportTerms = supportTerms;
		this.technicalSpecification = technicalSpecification;
		this.expectedPayment = expectedPayment;
		this.serviceProviderId = serviceProviderId;
		this.serviceProvider = serviceProvider;
		this.warrantyDate = warrantyDate;
		this.poAccessoriesMapper = poAccessoriesMapper;
		this.prId = prId;
		//this.otherCharges = otherCharges;
		this.purchaseOrderStagedDto = purchaseOrderStagedDto;
		this.initialPurchaseOrderStagedDto = initialPurchaseOrderStagedDto;
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

	public Integer getBonusQuantity() {
		return bonusQuantity;
	}

	public void setBonusQuantity(Integer bonusQuantity) {
		this.bonusQuantity = bonusQuantity;
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

	public List<PoPurchaseRequestArray> getPrId() {
		return prId;
	}

	public void setPrId(List<PoPurchaseRequestArray> prId) {
		this.prId = prId;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
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

	public Double getTotalOtherAmount() {
		return totalOtherAmount;
	}

	public void setTotalOtherAmount(Double totalOtherAmount) {
		this.totalOtherAmount = totalOtherAmount;
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

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<PurchaseOrderUomMapperDto> getPoUom() {
		return poUom;
	}

	public void setPoUom(List<PurchaseOrderUomMapperDto> poUom) {
		this.poUom = poUom;
	}

	public List<PurchaseOrderUomMapperDto> getBonusUom() {
		return bonusUom;
	}

	public void setBonusUom(List<PurchaseOrderUomMapperDto> bonusUom) {
		this.bonusUom = bonusUom;
	}

	/*public List<PoOtherChargesMapperDto> getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(List<PoOtherChargesMapperDto> otherCharges) {
		this.otherCharges = otherCharges;
	}*/

	public List<PurchaseOrderStagedDto> getPurchaseOrderStagedDto() {
		return purchaseOrderStagedDto;
	}

	public void setPurchaseOrderStagedDto(List<PurchaseOrderStagedDto> purchaseOrderStagedDto) {
		this.purchaseOrderStagedDto = purchaseOrderStagedDto;
	}
	
	public List<com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper> getPoAccessoriesMapper() {
		return poAccessoriesMapper;
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

	public Date getWarrantyDate() {
		return warrantyDate;
	}

	public void setWarrantyDate(Date warrantyDate) {
		this.warrantyDate = warrantyDate;
	}
	
	public Integer getBonusApprovedQuantity() {
		return bonusApprovedQuantity;
	}

	public void setBonusApprovedQuantity(Integer bonusApprovedQuantity) {
		this.bonusApprovedQuantity = bonusApprovedQuantity;
	}

	public void setPoAccessoriesMapper(
			List<com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper> poAccessoriesMapper) {
		this.poAccessoriesMapper = poAccessoriesMapper;
	}

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}

	public Integer getPrQuantity() {
		return prQuantity;
	}

	public void setPrQuantity(Integer prQuantity) {
		this.prQuantity = prQuantity;
	}

	public Integer getLeaseQuantity() {
		return leaseQuantity;
	}

	public void setLeaseQuantity(Integer leaseQuantity) {
		this.leaseQuantity = leaseQuantity;
	}

	public Integer getMaxStoreQuantity() {
		return maxStoreQuantity;
	}

	public void setMaxStoreQuantity(Integer maxStoreQuantity) {
		this.maxStoreQuantity = maxStoreQuantity;
	}

	public Long getMaxOrderQuantity() {
		return maxOrderQuantity;
	}

	public void setMaxOrderQuantity(Long maxOrderQuantity) {
		this.maxOrderQuantity = maxOrderQuantity;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public Long getStockAvailable() {
		return stockAvailable;
	}

	public void setStockAvailable(Long stockAvailable) {
		this.stockAvailable = stockAvailable;
	}

	public Double getTotalNetAmount() {
		return totalNetAmount;
	}

	public void setTotalNetAmount(Double totalNetAmount) {
		this.totalNetAmount = totalNetAmount;
	}

	public Integer getPoApprovedQuantity() {
		return poApprovedQuantity;
	}

	public void setPoApprovedQuantity(Integer poApprovedQuantity) {
		this.poApprovedQuantity = poApprovedQuantity;
	}

	public List<PurchaseOrderStagedDto> getInitialPurchaseOrderStagedDto() {
		return initialPurchaseOrderStagedDto;
	}

	public void setInitialPurchaseOrderStagedDto(List<PurchaseOrderStagedDto> initialPurchaseOrderStagedDto) {
		this.initialPurchaseOrderStagedDto = initialPurchaseOrderStagedDto;
	}
	
}
