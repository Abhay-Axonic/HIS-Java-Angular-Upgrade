package com.param.billing.global.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.DocumentTypeMaster;
import com.param.global.model.PatientRegistration;

@NamedQueries({
	
	@NamedQuery(name = "GET_REQUEST_DOCUMENT_BY_SANCTION_REQUEST_ID", 
	  query ="  SELECT sanctionRequestDoc.fundRequestdocumentDetailsId as fundRequestdocumentDetailsId, "
			+"  sanctionRequestDoc.fundsanctionrequestDetailsId as  fundsanctionrequestDetailsId,  "
			+"  sanctionRequestDoc.documentTypeId as  documentTypeId,  "
			+"  sanctionRequestDoc.documentPath as  documentPath,  "
			+"  documentTypeMaster.documentTypeName as documentTypeName, "
			+"  to_char(sanctionRequestDoc.requestDocumentDate,'dd/mm/yyyy')  as  requestDocumentDate  "
			+"  FROM FundSanctionRequestDocumentsDetails sanctionRequestDoc "
			+"  INNER JOIN sanctionRequestDoc.documentTypeMaster documentTypeMaster "
			+"  WHERE sanctionRequestDoc.fundsanctionrequestDetailsId=:fundsanctionrequestDetailsId "
			+"  "),
	
			
})
@Entity
@Table(name = "t_fund_requestdocuments_details", schema = "billing")
@SequenceGenerator(name = "t_fund_requestdocuments_details_seq", sequenceName = "billing.t_fund_requestdocuments_details_seq", allocationSize = 1)

public class FundSanctionRequestDocumentsDetails {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_fund_requestdocuments_details_seq")
	@Column(name = "fund_requestdocument_details_id")
	private Integer fundRequestdocumentDetailsId;

	@Column(name = "fundsanctionrequest_details_id")
	private Integer fundsanctionrequestDetailsId;
	
	@Column(name = "document_type_id")
	private Integer  documentTypeId;
	
	@Column(name="document_path")
	private String  documentPath;
	
	@Column(name="request_document_date")
	private Date requestDocumentDate;
	 
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="status")
	private String status;


	public Integer getFundsanctionrequestDetailsId() {
		return fundsanctionrequestDetailsId;
	}

	public void setFundsanctionrequestDetailsId(Integer fundsanctionrequestDetailsId) {
		this.fundsanctionrequestDetailsId = fundsanctionrequestDetailsId;
	}

	public Integer getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(Integer documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

	public Date getRequestDocumentDate() {
		return requestDocumentDate;
	}

	public void setRequestDocumentDate(Date requestDocumentDate) {
		this.requestDocumentDate = requestDocumentDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getFundRequestdocumentDetailsId() {
		return fundRequestdocumentDetailsId;
	}

	public void setFundRequestdocumentDetailsId(Integer fundRequestdocumentDetailsId) {
		this.fundRequestdocumentDetailsId = fundRequestdocumentDetailsId;
	}

	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="document_type_id", insertable=false, updatable=false)
	private DocumentTypeMaster documentTypeMaster;


	public DocumentTypeMaster getDocumentTypeMaster() {
		return documentTypeMaster;
	}

	public void setDocumentTypeMaster(DocumentTypeMaster documentTypeMaster) {
		this.documentTypeMaster = documentTypeMaster;
	}
	
	
//	
//	@ManyToOne(fetch= FetchType.LAZY)
//	@JoinColumn(name="patient_id", insertable=false, updatable=false)
//	private PatientRegistration patientRegistration;
	
	
	
	
	

	
}