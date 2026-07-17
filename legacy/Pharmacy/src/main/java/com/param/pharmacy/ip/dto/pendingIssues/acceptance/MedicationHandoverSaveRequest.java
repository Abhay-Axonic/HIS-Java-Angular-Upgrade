package com.param.pharmacy.ip.dto.pendingIssues.acceptance;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.service.global.model.CookieObjectDto;

@JsonInclude(Include.NON_NULL)
public class MedicationHandoverSaveRequest extends CookieObjectDto{
	public Integer getPrescriptionId() {
		return prescriptionId;
	}
	public void setPrescriptionId(Integer prescriptionId) {
		this.prescriptionId = prescriptionId;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverContactDetails() {
		return receiverContactDetails;
	}
	public void setReceiverContactDetails(String receiverContactDetails) {
		this.receiverContactDetails = receiverContactDetails;
	}
	public Integer getRelationshipId() {
		return relationshipId;
	}
	public void setRelationshipId(Integer relationshipId) {
		this.relationshipId = relationshipId;
	}
	
	public Integer getVisitTypeId() {
		return visitTypeId;
	}
	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	public List<PrescriptionDetailsList> getPrescriptionDetailsList() {
		return prescriptionDetailsList;
	}
	public void setPrescriptionDetailsList(List<PrescriptionDetailsList> prescriptionDetailsList) {
		this.prescriptionDetailsList = prescriptionDetailsList;
	}
	private Integer prescriptionId;
	private String receiverName;
	private String receiverContactDetails;
	private Integer relationshipId;
	private Integer visitTypeId;
	private List<PrescriptionDetailsList> prescriptionDetailsList;
}
