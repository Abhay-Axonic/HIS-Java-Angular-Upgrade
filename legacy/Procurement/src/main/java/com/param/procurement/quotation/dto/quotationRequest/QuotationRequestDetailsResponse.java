package com.param.procurement.quotation.dto.quotationRequest;

import java.util.List;

import com.param.global.dto.items.UomResponse;

public class QuotationRequestDetailsResponse {

	private Integer id;

	private Integer itemId;

	private String itemName;

	private String itemCode;

	private Integer quantity;

	private Integer uomTypeId;

	private String uomType;

	private Integer uomUnitId;

	private String uomUnit;

	private String warrantyPeriod;

	private String paymentTerms;

	private String supportTerms;

	private String technicalSpecification;
	
	private Long maxStoreQuantity;
	
	private Long maxOrderQuantity;
	
	private boolean isThreeUOMApplicable;
	
    public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }

	private List<PurchaseRequestDetailsResponse> prDetails;

	private List<QRAccessoriesMapper> expectedAccessories;
	
	private List<UomResponse> uom;
	private Integer qrPendingquantity;

	public Integer getQrPendingquantity() {
		return qrPendingquantity;
	}

	public void setQrPendingquantity(Integer qrPendingquantity) {
		this.qrPendingquantity = qrPendingquantity;
	}

	public QuotationRequestDetailsResponse() {
		super();
	}

	public QuotationRequestDetailsResponse(Integer id, Integer itemId, String itemName, String itemCode,
			Integer quantity, Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit,Long maxStoreQuantity,Long maxOrderQuantity,
			String warrantyPeriod, String paymentTerms, String supportTerms, String technicalSpecification,
			List<UomResponse> uom,List<QRAccessoriesMapper> expectedAccessories,List<PurchaseRequestDetailsResponse> prDetails, boolean isThreeUOMApplicable) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.quantity = quantity;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.maxStoreQuantity = maxStoreQuantity;
		this.maxOrderQuantity = maxOrderQuantity;
		this.warrantyPeriod = warrantyPeriod;
		this.paymentTerms = paymentTerms;
		this.supportTerms = supportTerms;
		this.technicalSpecification = technicalSpecification;
		this.uom = uom;
		this.expectedAccessories = expectedAccessories;
		this.prDetails = prDetails;
        this.isThreeUOMApplicable = isThreeUOMApplicable;
	}
	
	
	public QuotationRequestDetailsResponse(Integer id, Integer itemId, String itemName, String itemCode,
			Integer quantity,Long maxStoreQuantity,Long maxOrderQuantity,
			String warrantyPeriod, String paymentTerms, String supportTerms, String technicalSpecification,
			List<QRAccessoriesMapper> expectedAccessories,List<PurchaseRequestDetailsResponse> prDetails, boolean isThreeUOMApplicable) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.quantity = quantity;
		this.maxStoreQuantity = maxStoreQuantity;
		this.maxOrderQuantity = maxOrderQuantity;
		this.warrantyPeriod = warrantyPeriod;
		this.paymentTerms = paymentTerms;
		this.supportTerms = supportTerms;
		this.technicalSpecification = technicalSpecification;
		this.expectedAccessories = expectedAccessories;
		this.prDetails = prDetails;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public String getUomType() {
		return uomType;
	}

	public void setUomType(String uomType) {
		this.uomType = uomType;
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

	public String getWarrantyPeriod() {
		return warrantyPeriod;
	}

	public void setWarrantyPeriod(String warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
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

	public List<PurchaseRequestDetailsResponse> getPrDetails() {
		return prDetails;
	}

	public void setPrDetails(List<PurchaseRequestDetailsResponse> prDetails) {
		this.prDetails = prDetails;
	}

	public List<QRAccessoriesMapper> getExpectedAccessories() {
		return expectedAccessories;
	}

	public void setExpectedAccessories(List<QRAccessoriesMapper> expectedAccessories) {
		this.expectedAccessories = expectedAccessories;
	}

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
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
	
}
