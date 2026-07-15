package com.param.procurement.quotation.dto.quotationFill;

import java.util.ArrayList;
import java.util.List;

public class QuotationFillDetailResponse {

	private Integer id;

	private Integer itemId;

	private String itemCode;

	private String itemName;

	private Integer uomTypeId;

	private String uomType;

	private Integer uomUnitId;

	private String uomUnit;

	private Integer qrQuantity;

	private Integer statusId;

	private Double vendorCop;

	private String paymentTerms;

	private String supportTerms;

	private String technicalSpecification;

	private String warrantyPeriod;

	private String vendorExpectedPayment;

	private String vendorExpectedSupport;

	private String vendorTechnicalSpecification;

	private String vendorWarrantyPeriod;

	private String remark;
	
	private List<QuotationFillAccessRequest> expectedAccessories;
	
	public QuotationFillDetailResponse(Integer id, Integer itemId, String itemCode, String itemName, Integer uomTypeId,
			String uomType, Integer uomUnitId, String uomUnit, Integer qrQuantity, Integer statusId, Double vendorCop,
			String paymentTerms, String supportTerms, String technicalSpecification, String warrantyPeriod,
			String vendorExpectedPayment, String vendorExpectedSupport, String vendorTechnicalSpecification,
			String vendorWarrantyPeriod, String remark, List<QuotationFillAccessRequest> expectedAccessories) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.qrQuantity = qrQuantity;
		this.statusId = statusId;
		this.vendorCop = vendorCop;
		this.paymentTerms = paymentTerms;
		this.supportTerms = supportTerms;
		this.technicalSpecification = technicalSpecification;
		this.warrantyPeriod = warrantyPeriod;
		this.vendorExpectedPayment = vendorExpectedPayment;
		this.vendorExpectedSupport = vendorExpectedSupport;
		this.vendorTechnicalSpecification = vendorTechnicalSpecification;
		this.vendorWarrantyPeriod = vendorWarrantyPeriod;
		this.remark = remark;
		this.expectedAccessories = expectedAccessories;
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

	public Integer getQrQuantity() {
		return qrQuantity;
	}

	public void setQrQuantity(Integer qrQuantity) {
		this.qrQuantity = qrQuantity;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Double getVendorCop() {
		return vendorCop;
	}

	public void setVendorCop(Double vendorCop) {
		this.vendorCop = vendorCop;
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

	public String getWarrantyPeriod() {
		return warrantyPeriod;
	}

	public void setWarrantyPeriod(String warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}

	public String getVendorExpectedPayment() {
		return vendorExpectedPayment;
	}

	public void setVendorExpectedPayment(String vendorExpectedPayment) {
		this.vendorExpectedPayment = vendorExpectedPayment;
	}

	public String getVendorExpectedSupport() {
		return vendorExpectedSupport;
	}

	public void setVendorExpectedSupport(String vendorExpectedSupport) {
		this.vendorExpectedSupport = vendorExpectedSupport;
	}

	public String getVendorTechnicalSpecification() {
		return vendorTechnicalSpecification;
	}

	public void setVendorTechnicalSpecification(String vendorTechnicalSpecification) {
		this.vendorTechnicalSpecification = vendorTechnicalSpecification;
	}

	public String getVendorWarrantyPeriod() {
		return vendorWarrantyPeriod;
	}

	public void setVendorWarrantyPeriod(String vendorWarrantyPeriod) {
		this.vendorWarrantyPeriod = vendorWarrantyPeriod;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<QuotationFillAccessRequest> getExpectedAccessories() {
		return expectedAccessories;
	}

	public void setExpectedAccessories(List<QuotationFillAccessRequest> expectedAccessories) {
		this.expectedAccessories = expectedAccessories;
	}
	
	
	public void addItems(QuotationFillAccessRequest item) {
		if (this.expectedAccessories == null)
			this.expectedAccessories = new ArrayList<QuotationFillAccessRequest>();
		
		this.expectedAccessories.add(item);
	}
	
	

}
