package com.param.opd.coversheet.model;

import java.util.Date;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@NamedQueries({
	
	@NamedQuery(name="GET_LIST_CLINICAL_HEADER_TAB",
			query=" SELECT 		clinHeaTab.headerId as headerId, "
					+ " clinHeaTab.headerName as headerName, "
					+ " clinHeaTab.opdCheck as opdCheck, "
					+ " clinHeaTab.ipdCheck as ipdCheck, "
					+ " clinHeaTab.nurseCheck as nurseCheck, "
					+ " clinHeaTab.status as status "
					+ " FROM ClinicalHeaderTab clinHeaTab"
					+ " where clinHeaTab.status = 'A'"
					+ " order by clinHeaTab.headerId asc"
		),
		
})

@Entity
@Table(name="m_clinical_header_tab",schema="public")
@SequenceGenerator(name="m_clinical_header_tab_seq" , sequenceName="public.m_clinical_header_tab_seq", allocationSize=1)
public class ClinicalHeaderTab {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "m_clinical_header_tab_seq")
	@Column(name="header_id")
	private Integer headerId;
	
	@Column(name="header_name")
	private String headerName;
	
	@Column(name="opd_check")
	private char opdCheck;
	
	@Column(name="ipd_check")
	private char ipdCheck;
	
	@Column(name="nurse_check")
	private char nurseCheck;
	
	@Column(name="status")
	private char status;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;

	public Integer getHeaderId() {
		return headerId;
	}

	public void setHeaderId(Integer headerId) {
		this.headerId = headerId;
	}

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public char getOpdCheck() {
		return opdCheck;
	}

	public void setOpdCheck(char opdCheck) {
		this.opdCheck = opdCheck;
	}

	

	public char getIpdCheck() {
		return ipdCheck;
	}

	public void setIpdCheck(char ipdCheck) {
		this.ipdCheck = ipdCheck;
	}

	public char getNurseCheck() {
		return nurseCheck;
	}

	public void setNurseCheck(char nurseCheck) {
		this.nurseCheck = nurseCheck;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	
}
