package com.param.entity.model.procurement;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;


@Entity
@Table(name = "t_new_work_order_document_details", schema = "procurement")
@SequenceGenerator(name = "t_new_work_order_document_details_seq", sequenceName = "procurement.t_new_work_order_document_details_seq", allocationSize = 1)

public class NewWorkOrderCreationDocumentDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_new_work_order_document_details_seq")
	@Column(name = "work_order_document_details_id")
	private Integer workorderDocumentDetailsId;
	
	@Column(name = "workorder_request_id")
	private Integer workorderRequestId;
	
	
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

	public Integer getWorkorderDocumentDetailsId() {
		return workorderDocumentDetailsId;
	}

	public void setWorkorderDocumentDetailsId(Integer workorderDocumentDetailsId) {
		this.workorderDocumentDetailsId = workorderDocumentDetailsId;
	}

	public Integer getWorkorderRequestId() {
		return workorderRequestId;
	}

	public void setWorkorderRequestId(Integer workorderRequestId) {
		this.workorderRequestId = workorderRequestId;
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
	
	
	
	

	
}