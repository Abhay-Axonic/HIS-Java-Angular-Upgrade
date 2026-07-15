package com.param.procurement.quotation.dto.consignmentPOInvoice;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.param.entity.model.procurement.GRNStagedQuantity;
import com.param.global.dto.items.UomResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsignmentStockPODetailResponseDto {
	
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

	private Double taxAmount;
	
	private Double totalOtherAmount;
	private Double totalAmount;
	private Double netAmount;

	private Integer manufacturerId;

	private String manufacturerName;
	
	private String specification;
	private String remark;

	private Integer poId;
	
	private Integer poDetailId;
	
	private Integer grnId;
	
	private String grnNumber;
	
	private Integer grnDetailId;
	
	
	public ConsignmentStockPODetailResponseDto() {
		super();
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

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
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
	
	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
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

	public Integer getGrnDetailId() {
		return grnDetailId;
	}

	public void setGrnDetailId(Integer grnDetailId) {
		this.grnDetailId = grnDetailId;
	}
	
	/*start code added for use as save request*/
	
    private Integer statusId;
    private String status;

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
    
    /*end code added for use as save request*/
	
}
