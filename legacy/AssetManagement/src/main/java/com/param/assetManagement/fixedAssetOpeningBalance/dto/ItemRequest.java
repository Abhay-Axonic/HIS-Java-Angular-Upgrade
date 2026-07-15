package com.param.assetManagement.fixedAssetOpeningBalance.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class ItemRequest {
	
	public List<SpareDetails> getSpares() {
		return spares;
	}

	public void setSpares(List<SpareDetails> spares) {
		this.spares = spares;
	}

	protected Integer id;
	
	protected Integer itemId;
	
	protected Integer quantity;
	
	protected String specification;
	
	protected Integer statusId;
	
	protected String serialNumber;
	
	protected Integer yearOfManufacture;
	
	protected Integer vendorId;
	
	protected Integer serviceProviderId;
	
	protected Date warrantyFrom;
	
	protected Date warrantyTo;
	
	protected Integer pmsFrequencyId;
	
	protected List<AccessoriesDetails> accessories;
	protected List<SpareDetails> spares;
	
	protected InsuranceDetails insurance;
	
	protected Integer workingConditionId;
	protected Double cop;

	protected Double mrp;
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

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Integer getYearOfManufacture() {
		return yearOfManufacture;
	}

	public void setYearOfManufacture(Integer yearOfManufacture) {
		this.yearOfManufacture = yearOfManufacture;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public Integer getServiceProviderId() {
		return serviceProviderId;
	}

	public void setServiceProviderId(Integer serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}

	public Date getWarrantyFrom() {
		return warrantyFrom;
	}

	public void setWarrantyFrom(Date warrantyFrom) {
		this.warrantyFrom = warrantyFrom;
	}

	public Integer getPmsFrequencyId() {
		return pmsFrequencyId;
	}

	public void setPmsFrequencyId(Integer pmsFrequencyId) {
		this.pmsFrequencyId = pmsFrequencyId;
	}

	public List<AccessoriesDetails> getAccessories() {
		return accessories;
	}

	public void setAccessories(List<AccessoriesDetails> accessories) {
		this.accessories = accessories;
	}

	public InsuranceDetails getInsurance() {
		return insurance;
	}

	public void setInsurance(InsuranceDetails insurance) {
		this.insurance = insurance;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getWorkingConditionId() {
		return workingConditionId;
	}

	public void setWorkingConditionId(Integer workingConditionId) {
		this.workingConditionId = workingConditionId;
	}

	public Date getWarrantyTo() {
		return warrantyTo;
	}

	public void setWarrantyTo(Date warrantyTo) {
		this.warrantyTo = warrantyTo;
	}
	
}