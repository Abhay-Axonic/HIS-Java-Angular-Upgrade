package com.param.global.pharmacy.dto;

import java.io.Serializable;
import java.util.List;

import com.param.global.billing.dto.CommonBillingItemsDto;
import com.param.global.dto.items.BatchResponse;

public class PatientConsumableIssueSaveItem extends CommonBillingItemsDto implements Serializable,Cloneable
{
	//private Integer id;
	
	private Integer patientIndentDetailId;
	private Integer indentQuantity;
	
	private List<BatchResponse> batchList;
	private Integer parentId;
	//private Integer statusId;
	
	//Ganesh
	//private Double creditPayable;
	//private Double selfPayable;
	//private Integer itemId;
	//private Integer BatchId;
	//private Integer productCategoryId;
	//private Double totalAmount;
	//private Double netAmount;
	//private Double rate;
	//protected Double taxPercentage;
	//protected Double taxAmount;
	//protected Integer quantity;
	//protected Integer payeeId;
	//protected Integer markUpDown;
	//protected Double markUpDownPer;
	//protected Integer leaseQuantity;
	//protected Integer uomTypeId;
	//protected Integer uomUnitId;
	//protected Integer taxId;
	//protected Integer contractId;
	//protected Double packageConsumptionAmt;
	//protected List<SaleDetailPayeeMapperDto> saleDetailPayeeMapperDtoList;
	//protected List<SaleDetailPackageMapperDto> saleDetailPackageMapperDtoList;
	
	/*public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}*/



	public Integer getPatientIndentDetailId() {
		return patientIndentDetailId;
	}

	public void setPatientIndentDetailId(Integer patientIndentDetailId) {
		this.patientIndentDetailId = patientIndentDetailId;
	}

	public List<BatchResponse> getBatchList() {
		return batchList;
	}

	public void setBatchList(List<BatchResponse> batchList) {
		this.batchList = batchList;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	/*public Integer getStatusId() {
		return statusId;
	}
	
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}*/

	/*public Double getCreditPayable() {
		return creditPayable;
	}
	
	public void setCreditPayable(Double creditPayable) {
		this.creditPayable = creditPayable;
	}*/


	/*public Double getSelfPayable() {
		return selfPayable;
	}
	
	public void setSelfPayable(Double selfPayable) {
		this.selfPayable = selfPayable;
	}*/

	/*public Integer getItemId() {
		return itemId;
	}
	
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}*/

	/*public Integer getProductCategoryId() {
		return productCategoryId;
	}
	
	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}*/

	/*public Double getTotalAmount() {
		return totalAmount;
	}
	
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}*/

	/*public Double getNetAmount() {
		return netAmount;
	}
	
	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}*/

	/*public Double getRate() {
		return rate;
	}
	
	public void setRate(Double rate) {
		this.rate = rate;
	}*/

	/*public Double getTaxPercentage() {
		return taxPercentage;
	}
	
	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}*/

	/*public Double getTaxAmount() {
		return taxAmount;
	}
	
	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}*/

	/*public Integer getQuantity() {
		return quantity;
	}
	
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}*/

/*	public Integer getMarkUpDown() {
		return markUpDown;
	}

	public void setMarkUpDown(Integer markUpDown) {
		this.markUpDown = markUpDown;
	}

	public Double getMarkUpDownPer() {
		return markUpDownPer;
	}

	public void setMarkUpDownPer(Double markUpDownPer) {
		this.markUpDownPer = markUpDownPer;
	}
*/

	/*public List<SaleDetailPayeeMapperDto> getSaleDetailPayeeMapperDtoList() {
		return saleDetailPayeeMapperDtoList;
	}
	
	public void setSaleDetailPayeeMapperDtoList(List<SaleDetailPayeeMapperDto> saleDetailPayeeMapperDtoList) {
		this.saleDetailPayeeMapperDtoList = saleDetailPayeeMapperDtoList;
	}*/

	/*public Integer getPayeeId() {
		return payeeId;
	}
	
	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}*/

	/*public Integer getBatchId() {
		return BatchId;
	}
	
	public void setBatchId(Integer batchId) {
		BatchId = batchId;
	}*/

	/*public Integer getLeaseQuantity() {
		return leaseQuantity;
	}
	
	public void setLeaseQuantity(Integer leaseQuantity) {
		this.leaseQuantity = leaseQuantity;
	}*/

	/*public Integer getUomTypeId() {
		return uomTypeId;
	}
	
	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}*/

	/*public Integer getUomUnitId() {
		return uomUnitId;
	}
	
	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}*/

	/*public Integer getTaxId() {
		return taxId;
	}
	
	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}*/

	/*public Integer getContractId() {
		return contractId;
	}
	
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}*/

	/*public Double getPackageConsumptionAmt() {
		return packageConsumptionAmt;
	}
	
	public void setPackageConsumptionAmt(Double packageConsumptionAmt) {
		this.packageConsumptionAmt = packageConsumptionAmt;
	}*/

	/*public List<SaleDetailPackageMapperDto> getSaleDetailPackageMapperDtoList() {
		return saleDetailPackageMapperDtoList;
	}
	
	public void setSaleDetailPackageMapperDtoList(List<SaleDetailPackageMapperDto> saleDetailPackageMapperDtoList) {
		this.saleDetailPackageMapperDtoList = saleDetailPackageMapperDtoList;
	}*/
	
	public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

	public Integer getIndentQuantity() {
		return indentQuantity;
	}

	public void setIndentQuantity(Integer indentQuantity) {
		this.indentQuantity = indentQuantity;
	}
	
}