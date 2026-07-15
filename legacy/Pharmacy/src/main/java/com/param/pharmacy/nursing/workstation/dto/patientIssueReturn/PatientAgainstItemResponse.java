package com.param.pharmacy.nursing.workstation.dto.patientIssueReturn;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.UomResponse;
@JsonInclude(Include.NON_NULL)
public class PatientAgainstItemResponse
{
	private Integer id;
	
	private String issueNo;
	
	private Date issueDate;
	
	private String itemName;
	
	private Integer itemId;
	
	private String itemCode;
	
	private String genericName;
	private Integer issuDetailId;

	private String uom;
	
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

	private long batchId;
	public long getBatchId() {
		return batchId;
	}

	public void setBatchId(long batchId) {
		this.batchId = batchId;
	}

	private String uomUnit;
	
	private Integer quantityIssued;
	
	private String batchNo;
	private BigInteger comsumedQty;
	
	public BigInteger getComsumedQty() {
		return comsumedQty;
	}

	public void setComsumedQty(BigInteger comsumedQty) {
		this.comsumedQty = comsumedQty;
	}

	private Date expiry;
	
	private Integer returnQuantity;
	
	private Long patientReturnedQuantity;

	private Integer batchMapperId;
	
	private String indentNo; 
	private Integer returnAcceptedQuantity;
	private Integer patientIssueReturnQuantity;
	private Integer returnRejectedQuantity;
	private Integer returnIssuedQty;
	
	private Long saleId;
	
	public Integer getReturnAcceptedQuantity() {
		return returnAcceptedQuantity;
	}

	public void setReturnAcceptedQuantity(Integer returnAcceptedQuantity) {
		this.returnAcceptedQuantity = returnAcceptedQuantity;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public String getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}

	public Integer getQuantityIssued() {
		return quantityIssued;
	}

	public void setQuantityIssued(Integer quantityIssued) {
		this.quantityIssued = quantityIssued;
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

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}
	
	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}
	
	public Integer getBatchMapperId() {
		return batchMapperId;
	}

	public void setBatchMapperId(Integer batchMapperId) {
		this.batchMapperId = batchMapperId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(String indentNo) {
		this.indentNo = indentNo;
	}
		
	 

	public Long getPatientReturnedQuantity() {
		return patientReturnedQuantity;
	}

	public void setPatientReturnedQuantity(Long patientReturnedQuantity) {
		this.patientReturnedQuantity = patientReturnedQuantity;
	}

	public PatientAgainstItemResponse() {
		super();
	}

	public PatientAgainstItemResponse(Integer id, String issueNo, Date issueDate, Integer itemId, String itemName, String itemCode,
			String genericName, String uom, String uomUnit, Integer quantityIssued, String batchNo, Date expiry,
			Integer returnQuantity, Integer batchMapperId) {
		super();
		this.id = id;
		this.issueNo = issueNo;
		this.issueDate = issueDate;
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.genericName = genericName;
		this.uom = uom;
		this.uomUnit = uomUnit;
		this.quantityIssued = quantityIssued;
		this.batchNo = batchNo;
		this.expiry = expiry;
		this.returnQuantity = returnQuantity;
		this.batchMapperId = batchMapperId;
	}

	public PatientAgainstItemResponse(String itemName, Integer itemId, String itemCode) {
		super();
		this.itemName = itemName;
		this.itemId = itemId;
		this.itemCode = itemCode;
	}

	public Integer getIssuDetailId() {
		return issuDetailId;
	}

	public void setIssuDetailId(Integer issuDetailId) {
		this.issuDetailId = issuDetailId;
	}

	public Integer getPatientIssueReturnQuantity() {
		return patientIssueReturnQuantity;
	}

	public void setPatientIssueReturnQuantity(Integer patientIssueReturnQuantity) {
		this.patientIssueReturnQuantity = patientIssueReturnQuantity;
	}

	public Integer getReturnRejectedQuantity() {
		return returnRejectedQuantity;
	}

	public void setReturnRejectedQuantity(Integer returnRejectedQuantity) {
		this.returnRejectedQuantity = returnRejectedQuantity;
	}

	public Integer getReturnIssuedQty() {
		return returnIssuedQty;
	}

	public void setReturnIssuedQty(Integer returnIssuedQty) {
		this.returnIssuedQty = returnIssuedQty;
	}

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}
	
}