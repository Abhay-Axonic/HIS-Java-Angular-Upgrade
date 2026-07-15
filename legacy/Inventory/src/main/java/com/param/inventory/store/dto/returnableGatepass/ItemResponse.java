package com.param.inventory.store.dto.returnableGatepass;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.entity.model.procurement.GrnBonusBatchExpiry;
import com.param.global.dto.items.UomResponse;

@JsonInclude(Include.NON_NULL)
public class ItemResponse extends ItemRequest {

	protected String itemCode;

	protected String itemName;

	protected String uomType;

	protected String uomUnit;

	protected String batchNo;

	protected Date expiry;

	protected String lotNo;

	protected String vendor;

	protected Integer availableQuantity;
	
	protected Integer approvedReturnedQty;
	
	protected Integer itemId;
	protected Double lcop;
	protected Double totalDiscount;
	protected Integer totalGRNQuantity;
    protected Double actualDiscountPercentage;
    
    private boolean isThreeUOMApplicable;
    protected List<returnableBonusBatchExpiry> returnableBonusBatchExpiry;
    protected Integer bonusApprovedQuantity;
    
    protected Integer bonusUomTypeId;
    protected Integer phyRejectQuantity;
    
    public Integer getBonusUomTypeId() {
		return bonusUomTypeId;
	}

	public void setBonusUomTypeId(Integer bonusUomTypeId) {
		this.bonusUomTypeId = bonusUomTypeId;
	}

	public boolean getIsThreeUOMApplicable() {
		return isThreeUOMApplicable;
	}

	public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}
	
	public Double getLcop() {
		return lcop;
	}

	public void setLcop(Double lcop) {
		this.lcop = lcop;
	}
	protected Double cop;

	protected String taxName;
	protected Integer quantity;
	
	protected List<accessoriesResponse> accessoriesResponse;

	List<UomResponse> uomList;

	public List<UomResponse> getUomList() {
		return uomList;
	}



	public void setUomList(List<UomResponse> uomList) {
		this.uomList = uomList;
	}



	public ItemResponse() {
		super();
	}

	

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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

	public String getUomType() {
		return uomType;
	}

	public void setUomType(String uomType) {
		this.uomType = uomType;
	}

	public String getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Integer getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(Integer availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxCode) {
		this.taxName = taxCode;
	}
	
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public List<accessoriesResponse> getAccessoriesResponse() {
		return accessoriesResponse;
	}

	public void setAccessoriesResponse(List<accessoriesResponse> accessoriesResponse) {
		this.accessoriesResponse = accessoriesResponse;
	}
	
	public ItemResponse(Integer id, Long batchId, Integer venderId,  Integer taxId,
			Double taxPercentage, Integer returnQty,Integer returnedQty, Double totalAmt, Double totalTaxAmount, Double netAmount,
			Integer statusId, Integer approvedQty, Integer rejectQty, Integer itemId, String code, String name, 
			 String batchNo, Date expiryDate, String vendor, Integer availableQty, Double cop,
			String taxName, List<accessoriesResponse> populateAccessories,Double totalDiscount, boolean isThreeUOMApplicable) {
		
		this.id=id;
		this.vendorId=venderId;
		this.taxPercentage=taxPercentage;
		this.returnQuantity=returnQty;
		this.returnedQuantity=returnedQty;
		this.totalAmount=totalAmt;
		this.totalTaxAmount=totalTaxAmount;
		this.netAmount=netAmount;
		this.statusId=statusId;
		this.approvedQuantity=approvedQty;
		this.rejectedQuantity=rejectQty;
		this.itemId=itemId;
		this.itemCode=code;
		this.itemName=name;
		this.batchNo=batchNo;
		this.expiry=expiryDate;
		this.vendor=vendor;
		this.availableQuantity=availableQty;
		this.cop=cop;
		this.taxName=taxName;
		this.accessoriesResponse=populateAccessories;
		this.totalDiscount=totalDiscount;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
		
	}
	public ItemResponse(Integer id, Long batchId, Integer venderId,  Integer taxId,
			Double taxPercentage, Integer returnQty, Double totalAmt, Double totalTaxAmount, Double netAmount,
			Integer statusId, Integer approvedQty, Integer rejectQty, Integer itemId, String code, String name, 
			 String batchNo, Date expiryDate, String vendor, Integer availableQty, Double cop,
			String taxName, List<accessoriesResponse> populateAccessories, Double lcop,Double totalDiscount, boolean isThreeUOMApplicable) {
		
		this.id=id;
		this.vendorId=venderId;
		this.taxPercentage=taxPercentage;
		this.returnQuantity=returnQty;
		this.totalAmount=totalAmt;
		this.totalTaxAmount=totalTaxAmount;
		this.netAmount=netAmount;
		this.statusId=statusId;
		this.approvedQuantity=approvedQty;
		this.rejectedQuantity=rejectQty;
		this.itemId=itemId;
		this.itemCode=code;
		this.itemName=name;
		this.batchNo=batchNo;
		this.expiry=expiryDate;
		this.vendor=vendor;
		this.availableQuantity=availableQty;
		this.cop=cop;
		this.taxName=taxName;
		this.accessoriesResponse=populateAccessories;
		this.lcop = lcop;
		this.totalDiscount=totalDiscount;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}
	public ItemResponse(Integer id, Long batchId, Integer venderId, Integer uomTypeId, Integer uomUnitId, Integer taxId,
			Double taxPercentage, Integer returnQty, Double totalAmt, Double totalTaxAmount, Double netAmount,
			Integer statusId, Integer approvedQty, Integer rejectQty, Integer itemId, String code, String name, String type,
			String uomUnit,String batchNo, Date expiryDate, String vendor, Integer availableQty, Double cop,
			String taxName, boolean isThreeUOMApplicable) {
		
		this.id=id;
		this.vendorId=venderId;
		this.uomTypeId=uomTypeId;
		this.uomUnitId=uomUnitId;
		this.taxPercentage=taxPercentage;
		this.returnQuantity=returnQty;
		this.totalAmount=totalAmt;
		this.totalTaxAmount=totalTaxAmount;
		this.netAmount=netAmount;
		this.statusId=statusId;
		this.approvedQuantity=approvedQty;
		this.rejectedQuantity=rejectQty;
		this.itemId=itemId;
		this.itemCode=code;
		this.itemName=name;
		this.uomType=type;
		this.uomUnit=uomUnit;
		this.batchNo=batchNo;
		this.expiry=expiryDate;
		this.vendor=vendor;
		this.availableQuantity=availableQty;
		this.cop=cop;
		this.taxName=taxName;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
		
	}
	public ItemResponse(Integer id, Long batchId, Integer venderId, Integer uomTypeId, Integer uomUnitId, Integer taxId,
						Double taxPercentage, Integer returnQty,Integer returnedQty, Double totalAmt, Double totalTaxAmount, Double netAmount,
						Integer statusId, Integer approvedQty, Integer rejectQty, Integer itemId, String code, String name, String type,
						String uomUnit,String batchNo, String lotNo,Date expiryDate, String vendor, Integer availableQty, Double cop,
						String taxName, boolean isThreeUOMApplicable) {

		this.id=id;
		this.vendorId=venderId;
		this.uomTypeId=uomTypeId;
		this.uomUnitId=uomUnitId;
		this.taxPercentage=taxPercentage;
		this.returnQuantity=returnQty;
		this.returnedQuantity=returnedQty;
		this.totalAmount=totalAmt;
		this.totalTaxAmount=totalTaxAmount;
		this.netAmount=netAmount;
		this.statusId=statusId;
		this.approvedQuantity=approvedQty;
		this.rejectedQuantity=rejectQty;
		this.itemId=itemId;
		this.itemCode=code;
		this.itemName=name;
		this.uomType=type;
		this.uomUnit=uomUnit;
		this.batchNo=batchNo;
		this.lotNo=lotNo;
		this.expiry=expiryDate;
		this.vendor=vendor;
		this.availableQuantity=availableQty;
		this.cop=cop;
		this.taxName=taxName;
        this.isThreeUOMApplicable = isThreeUOMApplicable;
	}
	
	public ItemResponse(Integer id, Long batchId, Integer venderId, Integer uomTypeId, Integer uomUnitId, Integer taxId,
			Double taxPercentage, Integer returnQty, Double totalAmt, Double totalTaxAmount, Double netAmount,
			Integer statusId, Integer approvedQty, Integer rejectQty, Integer itemId, String code, String name, String type,
			String uomUnit,String batchNo, String lotNo, Date expiryDate, String vendor, Integer availableQty, Double cop,
			String taxName , Double lcop, boolean isThreeUOMApplicable) {
		
		this.id=id;
		this.vendorId=venderId;
		this.uomTypeId=uomTypeId;
		this.uomUnitId=uomUnitId;
		this.taxPercentage=taxPercentage;
		this.returnQuantity=returnQty;
		this.totalAmount=totalAmt;
		this.totalTaxAmount=totalTaxAmount;
		this.netAmount=netAmount;
		this.statusId=statusId;
		this.approvedQuantity=approvedQty;
		this.rejectedQuantity=rejectQty;
		this.itemId=itemId;
		this.itemCode=code;
		this.itemName=name;
		this.uomType=type;
		this.uomUnit=uomUnit;
		this.batchNo=batchNo;
		this.lotNo=lotNo;
		this.expiry=expiryDate;
		this.vendor=vendor;
		this.availableQuantity=availableQty;
		this.cop=cop;
		this.taxName=taxName;
		this.lcop = lcop;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
		
	}
	
	
	public ItemResponse(Integer id, Long batchId, Integer venderId, Integer uomTypeId, Integer uomUnitId, Integer taxId,
			Double taxPercentage, Integer returnQty, Double totalAmt, Double totalTaxAmount, Double netAmount,
			Integer statusId, Integer approvedQty, Integer rejectQty, Integer itemId, String code, String name, String type,
			String uomUnit,String batchNo, Date expiryDate, String vendor, Integer availableQty, Double cop,
			String taxName , Double lcop) {
		
		this.id=id;
		this.vendorId=venderId;
		this.uomTypeId=uomTypeId;
		this.uomUnitId=uomUnitId;
		this.taxPercentage=taxPercentage;
		this.returnQuantity=returnQty;
		this.totalAmount=totalAmt;
		this.totalTaxAmount=totalTaxAmount;
		this.netAmount=netAmount;
		this.statusId=statusId;
		this.approvedQuantity=approvedQty;
		this.rejectedQuantity=rejectQty;
		this.itemId=itemId;
		this.itemCode=code;
		this.itemName=name;
		this.uomType=type;
		this.uomUnit=uomUnit;
		this.batchNo=batchNo;
		this.expiry=expiryDate;
		this.vendor=vendor;
		this.availableQuantity=availableQty;
		this.cop=cop;
		this.taxName=taxName;
		this.lcop = lcop;
		
	}



	public Double getTotalDiscount() {
		return totalDiscount;
	}



	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}



	public Integer getApprovedReturnedQty() {
		return approvedReturnedQty;
	}



	public void setApprovedReturnedQty(Integer approvedReturnedQty) {
		this.approvedReturnedQty = approvedReturnedQty;
	}



	public Integer getTotalGRNQuantity() {
		return totalGRNQuantity;
	}



	public void setTotalGRNQuantity(Integer totalGRNQuantity) {
		this.totalGRNQuantity = totalGRNQuantity;
	}



	public Double getActualDiscountPercentage() {
		return actualDiscountPercentage;
	}



	public void setActualDiscountPercentage(Double actualDiscountPercentage) {
		this.actualDiscountPercentage = actualDiscountPercentage;
	}

	public Integer getBonusApprovedQuantity() {
		return bonusApprovedQuantity;
	}

	public void setBonusApprovedQuantity(Integer bonusApprovedQuantity) {
		this.bonusApprovedQuantity = bonusApprovedQuantity;
	}

	public List<returnableBonusBatchExpiry> getReturnableBonusBatchExpiry() {
		return returnableBonusBatchExpiry;
	}

	public void setReturnableBonusBatchExpiry(List<returnableBonusBatchExpiry> returnableBonusBatchExpiry) {
		this.returnableBonusBatchExpiry = returnableBonusBatchExpiry;
	}

	public Integer getPhyRejectQuantity() {
		return phyRejectQuantity;
	}

	public void setPhyRejectQuantity(Integer phyRejectQuantity) {
		this.phyRejectQuantity = phyRejectQuantity;
	}
}