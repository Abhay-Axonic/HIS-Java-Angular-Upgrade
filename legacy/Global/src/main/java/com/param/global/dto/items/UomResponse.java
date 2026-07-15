package com.param.global.dto.items;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UomResponse {

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	protected Integer id;
	
	protected Integer poDetailId;

	public Integer getPoDetailId() {
		return poDetailId;
	}

	public void setPoDetailId(Integer poDetailId) {
		this.poDetailId = poDetailId;
	}

	protected String uomType;

	protected Integer uomId;

	protected Integer uomTypeId;

	protected Integer uomUnitId;

	protected String uomUnit;

	protected String uomDetails;

	protected Integer conversion;

	protected Boolean ipUom;

	protected Boolean opUom;

	protected Boolean storeUom;
	
	protected Integer index;
	
	protected Integer itemId;
	
	protected Long batchId;
	
	protected Integer intbatchId;
	
	protected Integer opbDtlId;
	
	protected Integer count;
	protected Integer integerBatchId;
	protected Long stockAvailable;
	protected Long stockInTransitQty;
	protected Long lastMonthSale;
	protected Long currentMonthSale;
	protected Integer maxStoreQty;
	protected Long approvedPOQty;
	protected Long approvedPRAndPOQty;
	protected Integer storeId;
	protected Integer orgId;
	protected Integer unitId;

	protected Double discount;

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	
	public Integer getOpbDtlId() {
		return opbDtlId;
	}

	public void setOpbDtlId(Integer opbDtlId) {
		this.opbDtlId = opbDtlId;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public UomResponse() {

	}

	public UomResponse(Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit, String uomDetails,
			Integer conversion, Boolean ipUom, Boolean opUom, Boolean storeUom) {
		super();
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.uomDetails = uomDetails;
		this.conversion = conversion;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
	}

	public UomResponse(Integer id, String uomType, Integer uomTypeId, Integer uomUnitId, String uomUnit,
			Integer conversion, Boolean ipUom, Boolean opUom, Boolean storeUom) {
		super();
		this.id = id;
		this.uomType = uomType;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.conversion = conversion;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
	}

	public UomResponse(String uomType, Integer uomTypeId, Integer uomUnitId, String uomUnit, Integer conversion,
			Boolean ipUom, Boolean opUom, Boolean storeUom) {
		super();
		this.uomType = uomType;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.conversion = conversion;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
	}

	public UomResponse(Integer uomTypeId, Integer uomUnitId, Integer conversion, Boolean ipUom, Boolean opUom,
			Boolean storeUom) {
		super();
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.conversion = conversion;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
	}

	public UomResponse(Integer id, Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit,
			String uomDetails, Integer conversion, Boolean ipUom, Boolean opUom, Boolean storeUom) {
		super();
		this.id = id;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.uomDetails = uomDetails;
		this.conversion = conversion;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
	}

/*	public UomResponse(Integer opbUomMapperId, String uomType, Integer uomTypeId, Integer uomUnitId, String uomUnit, Integer conversion,
			Boolean ipUom, Boolean opUom, Boolean storeUom) {
		super();
		this.opbUomMapperId= opbUomMapperId;
		this.uomType = uomType;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.conversion = conversion;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
	}*/
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUomType() {
		return uomType;
	}

	public void setUomType(String uomType) {
		this.uomType = uomType;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public Integer getUomId() {
		return uomId;
	}

	public void setUomId(Integer uomId) {
		this.uomId = uomId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
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

	public String getUomDetails() {
		return uomDetails;
	}

	public void setUomDetails(String uomDetails) {
		this.uomDetails = uomDetails;
	}

	public Integer getConversion() {
		return conversion;
	}

	public void setConversion(Integer conversion) {
		this.conversion = conversion;
	}

	public Boolean getIpUom() {
		return ipUom;
	}

	public void setIpUom(Boolean ipUom) {
		this.ipUom = ipUom;
	}

	public Boolean getOpUom() {
		return opUom;
	}

	public void setOpUom(Boolean opUom) {
		this.opUom = opUom;
	}

	public Boolean getStoreUom() {
		return storeUom;
	}

	public void setStoreUom(Boolean storeUom) {
		this.storeUom = storeUom;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getIntbatchId() {
		return intbatchId;
	}

	public void setIntbatchId(Integer intbatchId) {
		this.intbatchId = intbatchId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getIntegerBatchId() {
		return integerBatchId;
	}

	public void setIntegerBatchId(Integer integerBatchId) {
		this.integerBatchId = integerBatchId;
	}

	public Long getStockAvailable() {
		return stockAvailable;
	}

	public void setStockAvailable(Long stockAvailable) {
		this.stockAvailable = stockAvailable;
	}

	public Long getStockInTransitQty() {
		return stockInTransitQty;
	}

	public void setStockInTransitQty(Long stockInTransitQty) {
		this.stockInTransitQty = stockInTransitQty;
	}

	public Long getLastMonthSale() {
		return lastMonthSale;
	}

	public void setLastMonthSale(Long lastMonthSale) {
		this.lastMonthSale = lastMonthSale;
	}

	public Long getCurrentMonthSale() {
		return currentMonthSale;
	}

	public void setCurrentMonthSale(Long currentMonthSale) {
		this.currentMonthSale = currentMonthSale;
	}

	public Long getApprovedPOQty() {
		return approvedPOQty;
	}

	public void setApprovedPOQty(Long approvedPOQty) {
		this.approvedPOQty = approvedPOQty;
	}

	public Long getApprovedPRAndPOQty() {
		return approvedPRAndPOQty;
	}

	public void setApprovedPRAndPOQty(Long approvedPRAndPOQty) {
		this.approvedPRAndPOQty = approvedPRAndPOQty;
	}

	public Integer getMaxStoreQty() {
		return maxStoreQty;
	}

	public void setMaxStoreQty(Integer maxStoreQty) {
		this.maxStoreQty = maxStoreQty;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}
}
