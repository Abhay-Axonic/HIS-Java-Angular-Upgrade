package com.param.procurement.quotation.dto.quotationFill;

import java.util.List;

public class QuotationVendorSearchDetailResponse {

	private Integer id;
	
	private Integer qrDetailId;

	private Integer itemId;

	private String itemCode;

	private String itemName;

	private Integer uomTypeId;

	private String uomType;

	private Integer uomUnitId;

	private String uomUnit;

	private Integer vqrQuantity;
	
	private String warrantyPeriod;
    
    private String supportTerms;
    
    private String paymentTerms;
    
    private String technicalSpecification;
	
	List<QuotationFillAccessRequest> accessoriesDetails;
	
	
	public QuotationVendorSearchDetailResponse() {
		super();
		// TODO Auto-generated constructor stub
	}	
	
	
	public QuotationVendorSearchDetailResponse(Integer id, Integer itemId, String itemCode, String itemName,
			Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit, Integer vqrQuantity) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.vqrQuantity = vqrQuantity;
	}



	public QuotationVendorSearchDetailResponse(Integer id, Integer qrDetailId,Integer itemId, String itemCode, String itemName,
			Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit, Integer vqrQuantity,
			String warrantyPeriod,String supportTerms,String paymentTerms,String technicalSpecification,List<QuotationFillAccessRequest> accessoriesDetails) {
		super();
		this.id = id;
		this.qrDetailId = qrDetailId;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.vqrQuantity = vqrQuantity;
		this.warrantyPeriod = warrantyPeriod;
		this.supportTerms = supportTerms;
		this.paymentTerms = paymentTerms;
		this.technicalSpecification = technicalSpecification;
		this.accessoriesDetails = accessoriesDetails;
	}




	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getQrDetailId() {
		return qrDetailId;
	}

	public void setQrDetailId(Integer qrDetailId) {
		this.qrDetailId = qrDetailId;
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

	public Integer getVqrQuantity() {
		return vqrQuantity;
	}

	public void setVqrQuantity(Integer vqrQuantity) {
		this.vqrQuantity = vqrQuantity;
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

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public String getTechnicalSpecification() {
		return technicalSpecification;
	}

	public void setTechnicalSpecification(String technicalSpecification) {
		this.technicalSpecification = technicalSpecification;
	}

	public List<QuotationFillAccessRequest> getAccessoriesDetails() {
		return accessoriesDetails;
	}

	public void setAccessoriesDetails(List<QuotationFillAccessRequest> accessoriesDetails) {
		this.accessoriesDetails = accessoriesDetails;
	}
	
}
