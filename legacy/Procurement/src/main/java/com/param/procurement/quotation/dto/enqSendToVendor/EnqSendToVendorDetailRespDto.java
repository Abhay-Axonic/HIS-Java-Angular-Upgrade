package com.param.procurement.quotation.dto.enqSendToVendor;

import java.util.List;

import com.param.global.dto.items.UomResponse;
import com.param.procurement.quotation.dto.quotationRequest.PurchaseRequestDetailsResponse;
import com.param.procurement.quotation.dto.quotationRequest.QRAccessoriesMapper;

public class EnqSendToVendorDetailRespDto {
	
	
    private Integer id;

	private Integer itemId;
	
	private String itemCode;
	
	private String itemName;

	private Integer uomTypeId;
	
	private String uomType;
	
	private Integer uomUnitId;
	
	private String uomUnit;
	
	private Integer qrQuantity;
	
	private String warrantyPeriod;
	
	private String supportTerms;
	
	private String paymentTerms;
	
	private String technicalSpecification;
	
    private List<UomResponse> uom;
	
	private List<PurchaseRequestDetailsResponse> prList;

	private List<QRAccessoriesMapper> expectedAccessories;
	
	
	public EnqSendToVendorDetailRespDto(Integer id, Integer itemId, String itemCode, String itemName, Integer uomTypeId,
			String uomType, Integer uomUnitId, String uomUnit, Integer qrQuantity, String warrantyPeriod,
			String supportTerms, String paymentTerms, String technicalSpecification, List<UomResponse> uom,
			List<PurchaseRequestDetailsResponse> prList, List<QRAccessoriesMapper> expectedAccessories) {
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
		this.warrantyPeriod = warrantyPeriod;
		this.supportTerms = supportTerms;
		this.paymentTerms = paymentTerms;
		this.technicalSpecification = technicalSpecification;
		this.uom = uom;
		this.prList = prList;
		this.expectedAccessories = expectedAccessories;
	}
	
	public EnqSendToVendorDetailRespDto(Integer id, Integer itemId, String itemCode, String itemName, Integer uomTypeId,
			String uomType, Integer uomUnitId, String uomUnit, Integer qrQuantity, String warrantyPeriod,
			String supportTerms, String paymentTerms, String technicalSpecification,
			List<PurchaseRequestDetailsResponse> prList, List<QRAccessoriesMapper> expectedAccessories) {
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
		this.warrantyPeriod = warrantyPeriod;
		this.supportTerms = supportTerms;
		this.paymentTerms = paymentTerms;
		this.technicalSpecification = technicalSpecification;
		this.prList = prList;
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

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}

	public List<PurchaseRequestDetailsResponse> getPrList() {
		return prList;
	}

	public void setPrList(List<PurchaseRequestDetailsResponse> prList) {
		this.prList = prList;
	}

	public List<QRAccessoriesMapper> getExpectedAccessories() {
		return expectedAccessories;
	}

	public void setExpectedAccessories(List<QRAccessoriesMapper> expectedAccessories) {
		this.expectedAccessories = expectedAccessories;
	}

}
