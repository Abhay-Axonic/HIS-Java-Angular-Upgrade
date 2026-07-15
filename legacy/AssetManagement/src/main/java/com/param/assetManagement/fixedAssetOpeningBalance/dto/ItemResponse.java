package com.param.assetManagement.fixedAssetOpeningBalance.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ItemResponse {

	protected Integer id;

	protected Integer itemId;

	protected String itemCode;

	protected String itemName;

	protected String serialNo;

	protected Integer yearOfManufacture;

	protected String vendor;

	protected String serviceProvider;

	protected Date warrantyFrom;

	protected Date warrantyTo;

	protected Integer PMSFrequencyid;

	protected String PMSFrequency;

	protected List<AccessoriesDetails> accesories;
	protected List<SpareDetails> spares;
	protected InsuranceDetails insurance;

	protected String specification;
	//start code for resolved HIS-9716 Fixed asset opening balance - Pending quantity column not visible
	protected Long maxOrderQuantity;
	
	protected Integer vendorId;
	protected Integer serviceProviderId;
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

	public Long getMaxOrderQuantity() {
		return maxOrderQuantity;
	}

	public void setMaxOrderQuantity(Long maxOrderQuantity) {
		this.maxOrderQuantity = maxOrderQuantity;
	}
	//end code for resolved HIS-9716 Fixed asset opening balance - Pending quantity column not visible

	public List<SpareDetails> getSpares() {
		return spares;
	}

	public void setSpares(List<SpareDetails> spares) {
		this.spares = spares;
	}

	protected Integer workingConditionId;

	protected String workingCondition;

	protected Integer quantity;

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

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Integer getYearOfManufacture() {
		return yearOfManufacture;
	}

	public void setYearOfManufacture(Integer yearOfManufacture) {
		this.yearOfManufacture = yearOfManufacture;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public Date getWarrantyFrom() {
		return warrantyFrom;
	}

	public void setWarrantyFrom(Date warrantyFrom) {
		this.warrantyFrom = warrantyFrom;
	}

	public Integer getPMSFrequencyid() {
		return PMSFrequencyid;
	}

	public void setPMSFrequencyid(Integer pMSFrequencyid) {
		PMSFrequencyid = pMSFrequencyid;
	}

	public String getPMSFrequency() {
		return PMSFrequency;
	}

	public void setPMSFrequency(String pMSFrequency) {
		this.PMSFrequency = pMSFrequency;
	}

	public List<AccessoriesDetails> getAccesories() {
		return accesories;
	}

	public void setAccesories(List<AccessoriesDetails> accesories) {
		this.accesories = accesories;
	}

	public Date getWarrantyTo() {
		return warrantyTo;
	}

	public void setWarrantyTo(Date warrantyTo) {
		this.warrantyTo = warrantyTo;
	}

	public InsuranceDetails getInsurance() {
		return insurance;
	}

	public void setInsurance(InsuranceDetails insurance) {
		this.insurance = insurance;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Integer getWorkingConditionId() {
		return workingConditionId;
	}

	public void setWorkingConditionId(Integer workingConditionId) {
		this.workingConditionId = workingConditionId;
	}

	public String getWorkingCondition() {
		return workingCondition;
	}

	public void setWorkingCondition(String workingCondition) {
		this.workingCondition = workingCondition;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public ItemResponse() {
		super();
	}

	public ItemResponse(Integer id, Integer itemId, String itemCode, String itemName, String serialNo,
			Integer yearOfManufacture, String vendor, String serviceProvider, Date warrantyFrom, Date warrantyTo,
			Integer PMSFrequencyid, String pMSFrequency, List<AccessoriesDetails> accesories,
			InsuranceDetails insurance, String specification, Integer workingConditionId, String workingCondition,
			Integer quantity , List<SpareDetails> spares) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.serialNo = serialNo;
		this.yearOfManufacture = yearOfManufacture;
		this.vendor = vendor;
		this.serviceProvider = serviceProvider;
		this.warrantyFrom = warrantyFrom;
		this.warrantyTo = warrantyTo;
		this.PMSFrequencyid = PMSFrequencyid;
		this.PMSFrequency = pMSFrequency;
		this.accesories = accesories;
		this.insurance = insurance;
		this.specification = specification;
		this.workingConditionId = workingConditionId;
		this.workingCondition = workingCondition;
		this.quantity = quantity;
		this.spares = spares;
		
	}
	//start code for resolved HIS-9716 Fixed asset opening balance - Pending quantity column not visible
	public ItemResponse(Integer id, Integer itemId, String itemCode, String itemName, String serialNo,
			Integer yearOfManufacture, String vendor, String serviceProvider, Date warrantyFrom, Date warrantyTo,
			Integer PMSFrequencyid, String pMSFrequency, List<AccessoriesDetails> accesories,
			InsuranceDetails insurance, String specification, Integer workingConditionId, String workingCondition,
			Integer quantity , List<SpareDetails> spares,Long maxOrderQuantity) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.serialNo = serialNo;
		this.yearOfManufacture = yearOfManufacture;
		this.vendor = vendor;
		this.serviceProvider = serviceProvider;
		this.warrantyFrom = warrantyFrom;
		this.warrantyTo = warrantyTo;
		this.PMSFrequencyid = PMSFrequencyid;
		this.PMSFrequency = pMSFrequency;
		this.accesories = accesories;
		this.insurance = insurance;
		this.specification = specification;
		this.workingConditionId = workingConditionId;
		this.workingCondition = workingCondition;
		this.quantity = quantity;
		this.spares = spares;
		this.maxOrderQuantity = maxOrderQuantity;		
	}
	//end code for resolved HIS-9716 Fixed asset opening balance - Pending quantity column not visible

}