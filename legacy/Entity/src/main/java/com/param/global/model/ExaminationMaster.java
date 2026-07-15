package com.param.global.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.param.billing.global.transaction.model.BillingMaster;
import com.param.opd.coversheet.model.ExaminationMapper;
@NamedQueries({

		@NamedQuery(name = "GET_EXAMINATION_MASTER_LIST", 
				query = " SELECT  examn.examinationMasterId as examinationMasterId, "
						+ " examn.examinationCode as examinationCode, "
						+ " examn.examinationDesc as examinationDesc, "
						+ " examn.status as status "
				+ " FROM ExaminationMaster examn" + " WHERE examn.organizationId=:organizationId "),

		@NamedQuery(name = "GET_EXAMINATION_MASTER_ACTIVE_LIST", 
		query = " SELECT examn.examinationMasterId as examinationMasterId, "
				+ " examn.examinationCode as examinationCode, "
				+ " examn.examinationDesc as examinationDesc,  "
				+ " examn.status as status "
				+ " FROM ExaminationMaster examn " + " WHERE examn.organizationId=:organizationId "
				+ " and examn.status='A'"),

		@NamedQuery(name = "GET_EXAMINATION_MASTER_BY_ID", 
		query = " SELECT examn.examinationMasterId as examinationMasterId, "
				+ " examn.examinationCode as examinationCode, "
				+ " examn.examinationDesc as examinationDesc, "
				+ " examn.status as status "
				+ " FROM ExaminationMaster examn " + " WHERE examn.organizationId=:organizationId "
				+ " and examn.examinationMasterId=:id"),
		
		@NamedQuery(name = "SEARCH_EXAMINATION_MASTER_ACTIVE_LIST", 
		query = " SELECT examn.examinationMasterId as examinationMasterId, "
				+ " examn.examinationDesc as examinationDesc,  "
				+ " examn.status as status "
				+ " FROM ExaminationMaster examn " + " WHERE examn.organizationId=:organizationId "
				+ " and examn.status='A'"
				+ " and 	LOWER(examn.examinationDesc) LIKE :searchKeyword "

				),

})
@Entity
@Table(name = "m_examination_master", schema = "emr")
@SequenceGenerator(name = "m_examination_master_seq", sequenceName = "emr.m_examination_master_seq", allocationSize = 1)
public class ExaminationMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_examination_master_seq")
	@Column(name = "examination_master_id")
	private Integer examinationMasterId;

	@Column(name = "examination_code")
	private String examinationCode;

	@Column(name = "examination_desc")
	private String examinationDesc;

	@Column(name = "status")
	private char status;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_date")
	private Date updatedDate;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "examinationMaster")
	private List<ExaminationMapper> examinationMasterList;

	public Integer getExaminationMasterId() {
		return examinationMasterId;
	}

	public void setExaminationMasterId(Integer examinationMasterId) {
		this.examinationMasterId = examinationMasterId;
	}

	public String getExaminationCode() {
		return examinationCode;
	}

	public void setExaminationCode(String examinationCode) {
		this.examinationCode = examinationCode;
	}

	public String getExaminationDesc() {
		return examinationDesc;
	}

	public void setExaminationDesc(String examinationDesc) {
		this.examinationDesc = examinationDesc;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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
	
	
}
