package com.param.entity.model.pharmacy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.procurement.GrnOtherChargesMapper;

@Entity(name="MedicationHandover")
@Table(name="t_medication_handover",schema="pharmacy")
public class MedicationHandover extends BaseEntity implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false )
	private Integer id;
	@Column(name="prescription_id")
	private Integer prescriptionId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
		return VisitTypeId;
	}
	public void setVisitTypeId(Integer visitTypeId) {
		VisitTypeId = visitTypeId;
	}
	public List<MedicationHandoverDetail> getMedicationHandoverDetailsList() {
		return medicationHandoverDetailsList;
	}
	public void setMedicationHandoverDetailsList(List<MedicationHandoverDetail> medicationHandoverDetailsList) {
		this.medicationHandoverDetailsList = medicationHandoverDetailsList;
	}
	@Column(name="receiver_name")
	private String receiverName;
	@Column(name="receiver_contact_details")
	private String receiverContactDetails;
	@Column(name="relationship_Id")
	private Integer relationshipId;
	@Column(name="visit_type_id")
	private Integer VisitTypeId;
	@OneToMany(fetch=FetchType.LAZY, mappedBy="medicationHandover",cascade = CascadeType.ALL)
	private List<MedicationHandoverDetail> medicationHandoverDetailsList;
	
	public MedicationHandoverDetail addMedicationHandoverDetail(MedicationHandoverDetail mhDetail) {
		if (getMedicationHandoverDetailsList() == null)
			this.medicationHandoverDetailsList = new ArrayList<MedicationHandoverDetail>();
		getMedicationHandoverDetailsList().add(mhDetail);
		mhDetail.setMedicationHandover(this);
		return mhDetail;
	}
}
