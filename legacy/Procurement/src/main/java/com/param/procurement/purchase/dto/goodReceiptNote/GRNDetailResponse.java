package com.param.procurement.purchase.dto.goodReceiptNote;

import java.util.Date;
import java.util.List;

import com.param.entity.model.inventory.Batch;
import com.param.entity.model.procurement.CreditNoteAmount;
import com.param.global.dto.items.UomResponse;

public class GRNDetailResponse extends GRNDetailsRequest {

	protected String itemName;

	protected String itemCode;

	protected String grnUomType;

	protected String grnUomUnit;

	protected String bonusUomType;
	protected String supportTerms;
	protected String technicalSpecification;
	protected String expectedPayment;
	protected String warrentyPeriod;
	protected Double roundOffAmount;
	protected Double totalNetAmount;
	private boolean isThreeUOMApplicable;
	private Integer poGrnUomTypeId;
	
    public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }
	
	public Double getTotalNetAmount() {
		return totalNetAmount;
	}



	public void setTotalNetAmount(Double totalNetAmount) {
		this.totalNetAmount = totalNetAmount;
	}



	public Double getRoundOffAmount() {
		return roundOffAmount;
	}



	public void setRoundOffAmount(Double roundOffAmount) {
		this.roundOffAmount = roundOffAmount;
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



	protected Date warrantyDate;
	public Date getWarrantyDate() {
		return warrantyDate;
	}



	public void setWarrantyDate(Date warrantyDate) {
		this.warrantyDate = warrantyDate;
	}



	protected Integer pendingQuantity;
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



	protected Integer serviceProviderId;
	protected String serviceProvider;


	public Integer getPendingQuantity() {
		return pendingQuantity;
	}



	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}



	protected String bonusUomUnit;

	protected String manufacturerName;

	protected String taxCode;

	protected String status;
	
	protected Integer poQuantity;

	protected Integer poBonusQuantity;

	protected Integer leaseQuantity;

	protected Integer maxStoreQuantity;

	public Double getLcop() {
		return lcop;
	}



	public void setLcop(Double lcop) {
		this.lcop = lcop;
	}

	

	protected Double lcop;
	protected Long maxOrderQuantity;

	protected String poSpecification;

	protected List<GRNUomMapper> grnUom;

	protected List<GRNUomMapper> bonusUom;

	/*protected List<GRNOtherChargesMapper> otherCharges;*/

	protected List<PurchasedOrderResponse> poList;

	protected List<UomResponse> uom;

	protected List<GrnAccessoriesMapper> grnAccessoriesMapper;

	private Integer poGrnQuantity;

	private Integer poPendingQuantity;

	private Integer assetTypeId;

	private Integer itemId;

	private Integer storeId;

	private String poNo;
	private Date poDate;
	
	private Date warrantyDateTo;
	private Integer poId;
	private Batch batch;
	private Long batchId;
	protected List<GRNBonusBatchExpiry> grnUomMappersList;
	public Integer getPoId() {
		return poId;
	}



	public void setPoId(Integer poId) {
		this.poId = poId;
	}



	public Date getWarrantyDateTo() {
		return warrantyDateTo;
	}



	public void setWarrantyDateTo(Date warrantyDateTo) {
		this.warrantyDateTo = warrantyDateTo;
	}



	public Date getPoDate() {
		return poDate;
	}



	public void setPoDate(Date poDate) {
		this.poDate = poDate;
	}



	/* sadded by minesh for consignment return */
	private Integer availableBatchQuantity;

	public Integer getAvailableBatchQuantity() {
		return availableBatchQuantity;
	}

	public void setAvailableQuantity(Integer availableBatchQuantity) {
		this.availableBatchQuantity = availableBatchQuantity;
	}
	
	public void setAvailableBatchQuantity(Integer availableBatchQuantity) {
		this.availableBatchQuantity = availableBatchQuantity;
	}
	/* added by minesh for consignment return */

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public GRNDetailResponse() {
		super();
	}

	public GRNDetailResponse(Integer id, Integer itemId, Integer grnApprovedQuantity, Integer bonusApprovedQuantity,
			Integer poQuantity, Integer poBonusQuantity, Integer leaseQuantity, Integer maxStoreQuantity,
			Long maxOrderQuantity, Integer grnReceivedQuantity, Integer bonusReceivedQuantity, String batchNo,
			Integer grnUomTypeId, Integer grnUomUnitId, Integer bonusUomTypeId, Integer bonusUomUnitId, Double cop,
			Double mrp, Double discount, Date expiryDate, String lotNumber, Integer manufacturerId, Integer taxId,
			Double taxPercentage, Double totalAmount, Double taxAmount, Double netAmount, Double totalDiscount,
			Double totalOtherAmount, Integer statusId, String grnSpecification, String poSpecification, String remark,
			Integer rejectedQuantity, List<GRNUomMapper> grnUom, List<GRNUomMapper> bonusUom,
			List<GRNOtherChargesMapper> otherCharges, List<GRNStagedDto> grnStagedQuantity, String itemName,
			String itemCode, String grnUomType, String grnUomUnit, String bonusUomType, String bonusUomUnit,
			String manufacturerName, String taxCode, String status, List<UomResponse> uom,
			List<PurchasedOrderResponse> poList, Integer poGrnQuantity, List<GRNStagedList> grnStagedLists) {
		super(id, itemId, grnApprovedQuantity, bonusApprovedQuantity, grnReceivedQuantity, bonusReceivedQuantity,
				batchNo, grnUomTypeId, grnUomUnitId, bonusUomTypeId, bonusUomUnitId, cop, mrp, discount, expiryDate,
				lotNumber, manufacturerId, taxId, taxPercentage, totalAmount, taxAmount, netAmount, totalDiscount,
				totalOtherAmount, statusId, grnSpecification, remark, rejectedQuantity, grnUom, bonusUom, /*otherCharges,*/
				grnStagedQuantity, grnStagedLists);
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.grnApprovedQuantity = grnApprovedQuantity;
		this.bonusApprovedQuantity = bonusApprovedQuantity;
		this.poQuantity = poQuantity;
		this.poBonusQuantity = poBonusQuantity;
		this.maxStoreQuantity = maxStoreQuantity;
		this.maxOrderQuantity = maxOrderQuantity;
		this.grnUomType = grnUomType;
		this.grnUomUnit = grnUomUnit;
		this.bonusUomType = bonusUomType;
		this.bonusUomUnit = bonusUomUnit;
		this.manufacturerName = manufacturerName;
		this.taxCode = taxCode;
		this.status = status;
		this.rejectedQuantity = rejectedQuantity;
		this.grnUom = grnUom;
		this.bonusUom = bonusUom;
		//this.otherCharges = otherCharges;
		this.uom = uom;
		this.poSpecification = poSpecification;
		this.poList = poList;
		this.poGrnQuantity = poGrnQuantity;
		this.poPendingQuantity = poQuantity != null ? poQuantity - poGrnQuantity : 0;
	}

	public GRNDetailResponse(Integer id, Integer itemId, Integer grnApprovedQuantity, Integer bonusApprovedQuantity,
			Integer poQuantity, Integer poBonusQuantity, Integer maxStoreQuantity, Long maxOrderQuantity,
			Integer grnReceivedQuantity, Integer bonusReceivedQuantity, String batchNo, Double cop, Double mrp,
			Double discount, Date expiryDate, String lotNumber, Integer manufacturerId, Integer taxId,
			Double taxPercentage, Double totalAmount, Double taxAmount, Double netAmount, Double totalDiscount,
			Double totalOtherAmount, Integer statusId, String grnSpecification, String poSpecification, String remark,
			Integer rejectedQuantity, String warrentyPeriod, String supportTerms, String technicalSpecification,
			String expectedPayment, Integer serviceProviderId, String serviceProvider, Date warrantyDate,
			List<GRNOtherChargesMapper> otherCharges, List<GRNStagedDto> grnStagedQuantity, String itemName,
			String itemCode, String manufacturerName, String taxCode, String status,
			List<PurchasedOrderResponse> poList, List<GrnAccessoriesMapper> grnAccessoriesMapper,
			List<GRNStagedList> grnStagedLists) {
		super(id, itemId, grnApprovedQuantity, bonusApprovedQuantity, grnReceivedQuantity, bonusReceivedQuantity,
				batchNo, cop, mrp, discount, expiryDate, lotNumber, manufacturerId, taxId, taxPercentage, totalAmount,
				taxAmount, netAmount, totalDiscount, totalOtherAmount, statusId, grnSpecification, remark,
				rejectedQuantity, warrentyPeriod, supportTerms, technicalSpecification, expectedPayment,
				serviceProviderId, serviceProvider, warrantyDate/*, otherCharges*/, grnStagedQuantity, grnStagedLists);
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.grnApprovedQuantity = grnApprovedQuantity;
		this.bonusApprovedQuantity = bonusApprovedQuantity;
		this.poQuantity = poQuantity;
		this.poBonusQuantity = poBonusQuantity;
		this.maxStoreQuantity = maxStoreQuantity;
		this.maxOrderQuantity = maxOrderQuantity;
		this.manufacturerName = manufacturerName;
		this.taxCode = taxCode;
		this.status = status;
		this.rejectedQuantity = rejectedQuantity;
		//this.otherCharges = otherCharges;
		this.poSpecification = poSpecification;
		this.poList = poList;
		this.grnAccessoriesMapper = grnAccessoriesMapper;
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

	public String getGrnUomType() {
		return grnUomType;
	}

	public void setGrnUomType(String grnUomType) {
		this.grnUomType = grnUomType;
	}

	public String getGrnUomUnit() {
		return grnUomUnit;
	}

	public void setGrnUomUnit(String grnUomUnit) {
		this.grnUomUnit = grnUomUnit;
	}

	public String getBonusUomType() {
		return bonusUomType;
	}

	public void setBonusUomType(String bonusUomType) {
		this.bonusUomType = bonusUomType;
	}

	public String getBonusUomUnit() {
		return bonusUomUnit;
	}

	public void setBonusUomUnit(String bonusUomUnit) {
		this.bonusUomUnit = bonusUomUnit;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<GRNUomMapper> getGrnUom() {
		return grnUom;
	}

	public void setGrnUom(List<GRNUomMapper> grnUom) {
		this.grnUom = grnUom;
	}

	public List<GRNUomMapper> getBonusUom() {
		return bonusUom;
	}

	public void setBonusUom(List<GRNUomMapper> bonusUom) {
		this.bonusUom = bonusUom;
	}

	/*public List<GRNOtherChargesMapper> getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(List<GRNOtherChargesMapper> otherCharges) {
		this.otherCharges = otherCharges;
	}
*/
	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}

	public String getPoSpecification() {
		return poSpecification;
	}

	public void setPoSpecification(String poSpecification) {
		this.poSpecification = poSpecification;
	}

	public List<PurchasedOrderResponse> getPoList() {
		return poList;
	}

	public void setPoList(List<PurchasedOrderResponse> poList) {
		this.poList = poList;
	}

	public List<GrnAccessoriesMapper> getGrnAccessoriesMapper() {
		return grnAccessoriesMapper;
	}

	public void setGrnAccessoriesMapper(List<GrnAccessoriesMapper> grnAccessoriesMapper) {
		this.grnAccessoriesMapper = grnAccessoriesMapper;
	}

	public Integer getPoBonusQuantity() {
		return poBonusQuantity;
	}

	public void setPoBonusQuantity(Integer poBonusQuantity) {
		this.poBonusQuantity = poBonusQuantity;
	}

	public Integer getPoQuantity() {
		return poQuantity;
	}

	public void setPoQuantity(Integer poQuantity) {
		this.poQuantity = poQuantity;
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

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}



	public Batch getBatch() {
		return batch;
	}



	public void setBatch(Batch batch) {
		this.batch = batch;
	}



	public Long getBatchId() {
		return batchId;
	}



	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public List<GRNBonusBatchExpiry> getGrnUomMappersList() {
		return grnUomMappersList;
	}

	public void setGrnUomMappersList(List<GRNBonusBatchExpiry> grnUomMappersList) {
		this.grnUomMappersList = grnUomMappersList;
	}

	public Integer getPoGrnUomTypeId() {
		return poGrnUomTypeId;
	}

	public void setPoGrnUomTypeId(Integer poGrnUomTypeId) {
		this.poGrnUomTypeId = poGrnUomTypeId;
	}
}
