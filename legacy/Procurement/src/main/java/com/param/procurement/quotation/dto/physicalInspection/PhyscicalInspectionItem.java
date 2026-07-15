package com.param.procurement.quotation.dto.physicalInspection;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.param.entity.model.procurement.GrnBonusBatchExpiry;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhyscicalInspectionItem
{
	private Integer id; /*grnDetailID*/
	
	private Integer itemId;
	
    private String itemCode;
    
    private String itemName;
    
    private String batchNo;
    
    private Integer grnApprovedQuantity;
    
    private Integer grnUomTypeId;
    private Integer grnRejectQty;
    private List<GrnBonusBatchExpiry> grnBonusBatchExpiry;
    
    public Integer getGrnRejectQty() {
		return grnRejectQty;
	}

	public void setGrnRejectQty(Integer grnRejectQty) {
		this.grnRejectQty = grnRejectQty;
	}

	private String grnUomType;
    private Double lcop;
    
    public Double getLcop() {
		return lcop;
	}

	public void setLcop(Double lcop) {
		this.lcop = lcop;
	}

	private Integer grnUomUnitId;
    
    private String grnUomUnit;
    
    private List<GrnUomMapperResponse> grnUom;
    
    private Integer bonusApprovedQuantity;
    private Integer grnBonusApprovedQuantity;
    
    public Integer getGrnBonusApprovedQuantity() {
		return grnBonusApprovedQuantity;
	}

	public void setGrnBonusApprovedQuantity(Integer grnBonusApprovedQuantity) {
		this.grnBonusApprovedQuantity = grnBonusApprovedQuantity;
	}

	private Integer bonusUomTypeId;
    
    private String bonusUomType;
    
    private Integer bonusUomUnitId;
    
    private String bonusUomUnit;
    
    private Integer approvedQuantity;
    
    private Integer rejectQuantity;
    
    private List<GrnBonusUomMapperResponse> bonusUom;
    
    private Double cop;
    
    private Double mrp;
    
    private Double totalAmount;
    
    private String specifications;
    
    private String remark;
    
    private Integer receivedQuantity;
    
    private Integer receivedBonusQuantity;
    
    private String lotNo;
    
    private Date expiryDate;
    
    private String manufacturerName;
    
    private String receivedItemSpecification;
    
    private Integer statusId;
	  
    private String status;
    
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

	public Integer getGrnApprovedQuantity() {
		return grnApprovedQuantity;
	}

	public void setGrnApprovedQuantity(Integer grnApprovedQuantity) {
		this.grnApprovedQuantity = grnApprovedQuantity;
	}

	public Integer getBonusApprovedQuantity() {
		return bonusApprovedQuantity;
	}

	public void setBonusApprovedQuantity(Integer bonusApprovedQuantity) {
		this.bonusApprovedQuantity = bonusApprovedQuantity;
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

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getGrnUomTypeId() {
		return grnUomTypeId;
	}

	public void setGrnUomTypeId(Integer grnUomTypeId) {
		this.grnUomTypeId = grnUomTypeId;
	}

	public String getGrnUomType() {
		return grnUomType;
	}

	public void setGrnUomType(String grnUomType) {
		this.grnUomType = grnUomType;
	}

	public Integer getGrnUomUnitId() {
		return grnUomUnitId;
	}

	public void setGrnUomUnitId(Integer grnUomUnitId) {
		this.grnUomUnitId = grnUomUnitId;
	}

	public String getGrnUomUnit() {
		return grnUomUnit;
	}

	public void setGrnUomUnit(String grnUomUnit) {
		this.grnUomUnit = grnUomUnit;
	}

	public List<GrnUomMapperResponse> getGrnUom() {
		return grnUom;
	}

	public void setGrnUom(List<GrnUomMapperResponse> grnUom) {
		this.grnUom = grnUom;
	}

	public List<GrnBonusUomMapperResponse> getBonusUom() {
		return bonusUom;
	}

	public void setBonusUom(List<GrnBonusUomMapperResponse> bonusUom) {
		this.bonusUom = bonusUom;
	}
	
	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getReceivedItemSpecification() {
		return receivedItemSpecification;
	}

	public void setReceivedItemSpecification(String receivedItemSpecification) {
		this.receivedItemSpecification = receivedItemSpecification;
	}
	
	public Integer getReceivedQuantity() {
		return receivedQuantity;
	}

	public void setReceivedQuantity(Integer receivedQuantity) {
		this.receivedQuantity = receivedQuantity;
	}

	public Integer getReceivedBonusQuantity() {
		return receivedBonusQuantity;
	}

	public void setReceivedBonusQuantity(Integer receivedBonusQuantity) {
		this.receivedBonusQuantity = receivedBonusQuantity;
	}

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

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}

	public PhyscicalInspectionItem()
	{
		super();
	}
	
	public PhyscicalInspectionItem(Integer id) {
		super();
		this.id = id;
	}
	public PhyscicalInspectionItem(Integer id, Integer itemId, String itemCode, String itemName, String batchNo, Integer grnApprovedQuantity,
			Integer grnUomTypeId, String grnUomType, Integer grnUomUnitId, String grnUomUnit,
			List<GrnUomMapperResponse> grnUom, Integer bonusApprovedQuantity, Integer bonusUomTypeId, String bonusUomType,
			Integer bonusUomUnitId, String bonusUomUnit, List<GrnBonusUomMapperResponse> bonusUom, Double cop, Double mrp,
			String specifications, String remark, Integer receivedQuantity, Integer receivedBonusQuantity, String lotNo, Date expiryDate, 
			String manufacturerName) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.batchNo = batchNo;
		this.grnApprovedQuantity = grnApprovedQuantity;
		this.grnUomTypeId = grnUomTypeId;
		this.grnUomType = grnUomType;
		this.grnUomUnitId = grnUomUnitId;
		this.grnUomUnit = grnUomUnit;
		this.grnUom = grnUom;
		this.bonusApprovedQuantity = bonusApprovedQuantity;
		this.bonusUomTypeId = bonusUomTypeId;
		this.bonusUomType = bonusUomType;
		this.bonusUomUnitId = bonusUomUnitId;
		this.bonusUomUnit = bonusUomUnit;
		this.bonusUom = bonusUom;
		this.cop = cop;
		this.mrp = mrp;
		this.specifications = specifications;
		this.remark = remark;
		this.receivedQuantity = receivedQuantity;
		this.receivedBonusQuantity = receivedBonusQuantity;
		this.lotNo = lotNo;
		this.expiryDate = expiryDate;
		this.manufacturerName = manufacturerName;
	}
	
	public PhyscicalInspectionItem(Integer id, Integer itemId, String itemCode, String itemName, String batchNo, Integer grnApprovedQuantity,
			Integer grnUomTypeId, String grnUomType, Integer grnUomUnitId, String grnUomUnit,
			List<GrnUomMapperResponse> grnUom, Integer bonusApprovedQuantity, Integer bonusUomTypeId, String bonusUomType,
			Integer bonusUomUnitId, String bonusUomUnit, List<GrnBonusUomMapperResponse> bonusUom, Double cop, Double mrp,
			String specifications, String remark, Integer receivedQuantity, Integer receivedBonusQuantity, String lotNo, Date expiryDate, 
			String manufacturerName, Double lcop) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.batchNo = batchNo;
		this.grnApprovedQuantity = grnApprovedQuantity;
		this.grnUomTypeId = grnUomTypeId;
		this.grnUomType = grnUomType;
		this.grnUomUnitId = grnUomUnitId;
		this.grnUomUnit = grnUomUnit;
		this.grnUom = grnUom;
		this.bonusApprovedQuantity = bonusApprovedQuantity;
		this.grnBonusApprovedQuantity=bonusApprovedQuantity;
		this.bonusUomTypeId = bonusUomTypeId;
		this.bonusUomType = bonusUomType;
		this.bonusUomUnitId = bonusUomUnitId;
		this.bonusUomUnit = bonusUomUnit;
		this.bonusUom = bonusUom;
		this.cop = cop;
		this.mrp = mrp;
		this.specifications = specifications;
		this.remark = remark;
		this.receivedQuantity = receivedQuantity;
		this.receivedBonusQuantity = receivedBonusQuantity;
		this.lotNo = lotNo;
		this.expiryDate = expiryDate;
		this.manufacturerName = manufacturerName;
		this.lcop = lcop;
	}
	
	public PhyscicalInspectionItem(Integer id, Integer itemId, String itemCode, String itemName, String batchNo, Integer grnApprovedQuantity,
			Integer bonusApprovedQuantity, Double cop, Double mrp,
			String specifications, String remark, Integer receivedQuantity, Integer receivedBonusQuantity, String lotNo, Date expiryDate, 
			String manufacturerName) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.batchNo = batchNo;
		this.grnApprovedQuantity = grnApprovedQuantity;
		this.bonusApprovedQuantity = bonusApprovedQuantity;
		this.grnBonusApprovedQuantity=bonusApprovedQuantity;
		this.cop = cop;
		this.mrp = mrp;
		this.specifications = specifications;
		this.remark = remark;
		this.receivedQuantity = receivedQuantity;
		this.receivedBonusQuantity = receivedBonusQuantity;
		this.lotNo = lotNo;
		this.expiryDate = expiryDate;
		this.manufacturerName = manufacturerName;
	}
	public PhyscicalInspectionItem(Integer id, Integer itemId, String itemCode, String itemName, String batchNo, Integer grnApprovedQuantity,
			Integer bonusApprovedQuantity, Double cop, Double mrp,
			String specifications, String remark, Integer receivedQuantity, Integer receivedBonusQuantity, String lotNo, Date expiryDate, 
			String manufacturerName , Double lcop) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.batchNo = batchNo;
		this.grnApprovedQuantity = grnApprovedQuantity;
		this.bonusApprovedQuantity = bonusApprovedQuantity;
		this.grnBonusApprovedQuantity=bonusApprovedQuantity;
		this.cop = cop;
		this.mrp = mrp;
		this.specifications = specifications;
		this.remark = remark;
		this.receivedQuantity = receivedQuantity;
		this.receivedBonusQuantity = receivedBonusQuantity;
		this.lotNo = lotNo;
		this.expiryDate = expiryDate;
		this.manufacturerName = manufacturerName;
		this.lcop = lcop;
	}
	public PhyscicalInspectionItem(String itemCode, String itemName, String batchNo) {
		super();
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.batchNo = batchNo;
	}

	public List<GrnBonusBatchExpiry> getGrnBonusBatchExpiry() {
		return grnBonusBatchExpiry;
	}

	public void setGrnBonusBatchExpiry(List<GrnBonusBatchExpiry> grnBonusBatchExpiry) {
		this.grnBonusBatchExpiry = grnBonusBatchExpiry;
	}
	
	
}