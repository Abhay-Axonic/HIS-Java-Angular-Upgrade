package com.param.entity.model.procurement;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.patient.PatientRegistration;
import com.param.entity.model.pharmacy.Issue;

@Entity(name = "ConsignmentPurchaseOrderPatient")
@Table(name = "t_consignment_purchase_order_patient", schema = "procurement")
public class ConsignmentPurchaseOrderPatient extends BaseEntity implements Serializable{

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id")
	private PatientRegistration patient;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_id")
	private Issue issue;
	
	@Column(name = "issued_quantity")
	private Integer issuedQuantity;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="purchase_order_detail_id", nullable = false)
	private ConsignmentPurchaseOrderDetail consignmentPurchaseOrderDetail;

	public ConsignmentPurchaseOrderPatient() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ConsignmentPurchaseOrderPatient(Issue issueId, PatientRegistration patientId) {
		this.issue=issueId;
		this.patient=patientId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PatientRegistration getPatient() {
		return patient;
	}

	public void setPatient(PatientRegistration patient) {
		this.patient = patient;
	}

	public Issue getIssue() {
		return issue;
	}

	public void setIssue(Issue issue) {
		this.issue = issue;
	}

	public Integer getIssuedQuantity() {
		return issuedQuantity;
	}

	public void setIssuedQuantity(Integer issuedQuantity) {
		this.issuedQuantity = issuedQuantity;
	}

	public ConsignmentPurchaseOrderDetail getConsignmentPurchaseOrderDetail() {
		return consignmentPurchaseOrderDetail;
	}

	public void setConsignmentPurchaseOrderDetail(ConsignmentPurchaseOrderDetail consignmentPurchaseOrderDetail) {
		this.consignmentPurchaseOrderDetail = consignmentPurchaseOrderDetail;
	}
	
	
	
}
