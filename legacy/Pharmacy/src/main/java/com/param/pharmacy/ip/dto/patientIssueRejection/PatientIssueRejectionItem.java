package com.param.pharmacy.ip.dto.patientIssueRejection;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.UomResponse;

@JsonInclude(Include.NON_NULL)
public class PatientIssueRejectionItem
{
	private Integer id;
	
	private String itemName;
	
	private String genericName;
	
	private boolean rejectionFlag;
	
	private String uom;
	
	private String uomUnit;
	
	private String batchNo;
	
	private Date expiryDate;
	
	private Double price;
	
	private Integer issueRejectQuantity;
	
	private Integer acceptQuantity;
	
	private Integer rejectQuantity;
	
	private String rejectNote;
	
	private String reasonForReject;
	
	private Integer rejectReasonId;
	
	private Integer qty;
	
	private Integer batchMapperId;
	
	private Integer statusId;
	   
	private String status;
	
	private Integer admissionId;
	
	private Integer encounterId;
	
	private String admittedPatientDetails;
	
	private String encounterPatientDetails;
	
	private String admittedPatientUhid;
	
	private String encounterPatientUhid;
	
	private Integer pharmReasonId;
	private String pharmReasonName;
	private Integer genericId;
	private Integer MarkUpDown;
	private Double MarkUpDownPer;
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
    
	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	private boolean isPatientIssueRejection;
	
	public boolean isPatientIssueRejection() {
		return isPatientIssueRejection;
	}

	public void setPatientIssueRejection(boolean isPatientIssueRejection) {
		this.isPatientIssueRejection = isPatientIssueRejection;
	}

	public Integer getPharmReasonId() {
		return pharmReasonId;
	}

	public void setPharmReasonId(Integer pharmReasonId) {
		this.pharmReasonId = pharmReasonId;
	}

	public String getPharmReasonName() {
		return pharmReasonName;
	}

	public void setPharmReasonName(String pharmReasonName) {
		this.pharmReasonName = pharmReasonName;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public String getAdmittedPatientDetails() {
		return admittedPatientDetails;
	}

	public void setAdmittedPatientDetails(String admittedPatientDetails) {
		this.admittedPatientDetails = admittedPatientDetails;
	}

	public String getEncounterPatientDetails() {
		return encounterPatientDetails;
	}

	public void setEncounterPatientDetails(String encounterPatientDetails) {
		this.encounterPatientDetails = encounterPatientDetails;
	}

	public String getAdmittedPatientUhid() {
		return admittedPatientUhid;
	}

	public void setAdmittedPatientUhid(String admittedPatientUhid) {
		this.admittedPatientUhid = admittedPatientUhid;
	}

	public String getEncounterPatientUhid() {
		return encounterPatientUhid;
	}

	public void setEncounterPatientUhid(String encounterPatientUhid) {
		this.encounterPatientUhid = encounterPatientUhid;
	}

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

	public boolean isRejectionFlag() {
		return rejectionFlag;
	}

	public void setRejectionFlag(boolean rejectionFlag) {
		this.rejectionFlag = rejectionFlag;
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

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getIssueRejectQuantity() {
		return issueRejectQuantity;
	}

	public void setIssueRejectQuantity(Integer issueRejectQuantity) {
		this.issueRejectQuantity = issueRejectQuantity;
	}

	public String getReasonForReject() {
		return reasonForReject;
	}

	public void setReasonForReject(String reasonForReject) {
		this.reasonForReject = reasonForReject;
	}

	public Integer getBatchMapperId() {
		return batchMapperId;
	}

	public void setBatchMapperId(Integer batchMapperId) {
		this.batchMapperId = batchMapperId;
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

	public Integer getAcceptQuantity() {
		return acceptQuantity;
	}

	public void setAcceptQuantity(Integer acceptQuantity) {
		this.acceptQuantity = acceptQuantity;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public PatientIssueRejectionItem(Integer id, String itemName, String genericName, boolean rejectionFlag, String uom,
			String uomUnit, String batchNo, Date expiryDate, Double price, Integer issueRejectQuantity,
			String reasonForReject, Integer batchMapperId, boolean isThreeUOMApplicable, List<UomResponse> uomList) {
		super();
		this.id = id;
		this.itemName = itemName;
		this.genericName = genericName;
		this.rejectionFlag = rejectionFlag;
		this.uom = uom;
		this.uomUnit = uomUnit;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.price = price;
		this.issueRejectQuantity = issueRejectQuantity;
		this.reasonForReject = reasonForReject;
		this.batchMapperId = batchMapperId;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
		this.uomList = uomList;
	}

	public Integer getMarkUpDown() {
		return MarkUpDown;
	}

	public void setMarkUpDown(Integer markUpDown) {
		MarkUpDown = markUpDown;
	}

	public Double getMarkUpDownPer() {
		return MarkUpDownPer;
	}

	public void setMarkUpDownPer(Double markUpDownPer) {
		MarkUpDownPer = markUpDownPer;
	}
	
}