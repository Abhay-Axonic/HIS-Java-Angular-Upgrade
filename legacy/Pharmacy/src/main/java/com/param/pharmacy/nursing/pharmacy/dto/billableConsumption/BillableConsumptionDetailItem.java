package com.param.pharmacy.nursing.pharmacy.dto.billableConsumption;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.UomResponse;

@JsonInclude(Include.NON_NULL)
public class BillableConsumptionDetailItem
{
	private Integer id;
	
	private Integer itemId;
	
	private String itemCode;
	
	private String itemName;
	
	private Integer genericId;
	
	private String genericName;
	
	private Long batchId;
	
	private String batchNo;
	
	private Date expiry;
	
	private String uom;

	private Integer uomTypeId;
	
	private String uomUnit;
	
	private Integer consumedQuantity;
	
	private Integer stockAvailable;
	
	private Long returnQuantity;
	
	protected Integer markUpDown;
	
	private String lotNo;
	
	/*start code for resolved HIS-9214 Return billable consumption to have issues, consumed and returned columns*/
	private Long totalConsumedQuantity;
	private Long totalReturnedQuantity;
	private Integer consumptionQuantity;
	private Boolean isLasa;
	public Long getTotalConsumedQuantity() {
		return totalConsumedQuantity;
	}
	public void setTotalConsumedQuantity(Long totalConsumedQuantity) {
		this.totalConsumedQuantity = totalConsumedQuantity;
	}
	public Long getTotalReturnedQuantity() {
		return totalReturnedQuantity;
	}
	public void setTotalReturnedQuantity(Long totalReturnedQuantity) {
		this.totalReturnedQuantity = totalReturnedQuantity;
	}
	public Integer getConsumptionQuantity() {
		return consumptionQuantity;
	}
	public void setConsumptionQuantity(Integer consumptionQuantity) {
		this.consumptionQuantity = consumptionQuantity;
	}
	
	/*end code for resolved HIS-9214 Return billable consumption to have issues, consumed and returned columns*/

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public Boolean getIsBatchReq() {
		return isBatchReq;
	}

	public void setIsBatchReq(Boolean isBatchReq) {
		this.isBatchReq = isBatchReq;
	}

	protected Boolean isBatchReq;
 	
 	public Integer getMarkUpDown() {
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

	protected Double markUpDownPer;

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

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
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

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}

	public Integer getConsumedQuantity() {
		return consumedQuantity;
	}

	public void setConsumedQuantity(Integer consumedQuantity) {
		this.consumedQuantity = consumedQuantity;
	}

	public Integer getStockAvailable() {
		return stockAvailable;
	}

	public void setStockAvailable(Integer stockAvailable) {
		this.stockAvailable = stockAvailable;
	}

	public Long getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Long returnQuantity) {
		this.returnQuantity = returnQuantity;
	}
	
    protected List<UomResponse> uomList;

    public List<UomResponse> getUomList() {
        return uomList;
    }

    public void setUomList(List<UomResponse> uomList) {
        this.uomList = uomList;
    }
    
    private boolean isThreeUOMApplicable;

    public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }

	public Boolean getIsLasa() {
		return isLasa;
	}

	public void setIsLasa(Boolean isLasa) {
		this.isLasa = isLasa;
	}

	public BillableConsumptionDetailItem() {
		super();
	}

	public BillableConsumptionDetailItem(Integer id, Integer itemId, String itemCode, String itemName,
			Integer genericId, String genericName, Long batchId, String batchNo, Date expiry, String uom,
			String uomUnit, Integer consumedQuantity) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericId = genericId;
		this.genericName = genericName;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiry = expiry;
		this.uom = uom;
		this.uomUnit = uomUnit;
		this.consumedQuantity = consumedQuantity;
	}
	
	public BillableConsumptionDetailItem(Integer id, Integer itemId, String itemCode, String itemName,
			Integer genericId, String genericName, Long batchId, String batchNo, Date expiry, String uom,
			String uomUnit, Integer consumedQuantity, Boolean isBatchReq, boolean isThreeUOMApplicable, List<UomResponse> uomList) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericId = genericId;
		this.genericName = genericName;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiry = expiry;
		this.uom = uom;
		this.uomUnit = uomUnit;
		this.consumedQuantity = consumedQuantity;
		this.isBatchReq = isBatchReq;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
		this.uomList  = uomList;
	}

	public BillableConsumptionDetailItem(Integer id, Integer itemId, String itemCode, String itemName,
			Integer genericId, String genericName, Long batchId, String batchNo, Date expiry, String uom,
			Integer uomTypeId,String uomUnit, Integer consumedQuantity, Long returnQuantity, boolean isThreeUOMApplicable, List<UomResponse> uomList) {

		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericId = genericId;
		this.genericName = genericName;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiry = expiry;
		this.uom = uom;
		this.uomTypeId=uomTypeId;
		this.uomUnit = uomUnit;
		this.consumedQuantity = consumedQuantity;
		this.returnQuantity = returnQuantity;
        this.isThreeUOMApplicable = isThreeUOMApplicable;
        this.uomList  = uomList;
	}

	public BillableConsumptionDetailItem(Integer id, Integer itemId, String itemCode, String itemName,
			Integer genericId, String genericName, Long batchId, String batchNo, Date expiry, String uom,
			String uomUnit) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericId = genericId;
		this.genericName = genericName;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiry = expiry;
		this.uom = uom;
		this.uomUnit = uomUnit;
	}
	
}