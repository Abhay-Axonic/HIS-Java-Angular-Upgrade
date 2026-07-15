package com.param.pharmacy.nursing.workstation.dto.patientIssueReturn;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.UomResponse;
@JsonInclude(Include.NON_NULL)
public class PatientIssueReturnDetailItem
{
	private Integer id;
	
	private String itemName;
	
	private String genericName;
	
	private String uom;
	
	private String uomUnit;
	
    private boolean isThreeUOMApplicable;

    public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }
    
    protected List<UomResponse> uomList;

    public List<UomResponse> getUomList() {
        return uomList;
    }

    public void setUomList(List<UomResponse> uomList) {
        this.uomList = uomList;
    }
	
	private String batchNo;
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

	private String issueNo;
	private Date issueDate;
	private Date expiry;
	
	private Integer issuedQuantity;
	
	private Integer returnedQuantity;
	
	private String reasonForReturn;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
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

	public Integer getIssuedQuantity() {
		return issuedQuantity;
	}

	public void setIssuedQuantity(Integer issuedQuantity) {
		this.issuedQuantity = issuedQuantity;
	}

	public Integer getReturnedQuantity() {
		return returnedQuantity;
	}

	public void setReturnedQuantity(Integer returnedQuantity) {
		this.returnedQuantity = returnedQuantity;
	}

	public String getReasonForReturn() {
		return reasonForReturn;
	}

	public void setReasonForReturn(String reasonForReturn) {
		this.reasonForReturn = reasonForReturn;
	}

	public PatientIssueReturnDetailItem(Integer id, String itemName, String genericName, String uom,
			String uomUnit, String batchNo, Date expiry, Integer issuedQuantity, Integer returnedQuantity,
			String reasonForReturn, boolean isThreeUOMApplicable, List<UomResponse> uomList) {
		super();
		this.id = id;
		this.itemName = itemName;
		this.genericName = genericName;
		this.uom = uom;
		this.uomUnit = uomUnit;
		this.batchNo = batchNo;
		this.expiry = expiry;
		this.issuedQuantity = issuedQuantity;
		this.returnedQuantity = returnedQuantity;
		this.reasonForReturn = reasonForReturn;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
		this.uomList = uomList;
	}
	
}