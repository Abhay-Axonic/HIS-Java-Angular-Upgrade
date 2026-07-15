package com.param.opd.coversheet.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@NamedQueries({
	
	@NamedQuery(name="GET_LIST_CLINICAL_SUB_TAB_MAPPER",
			query=" SELECT 		clinicalSubtabMapper.headerId as headerId, "
					+ " clinicalSubtabMapper.subTabId as subTabId, "
					+ " clinicalSubtabMapper.subTabName as subTabName, "
					+ " clinicalSubtabMapper.opdCheck as opdCheck, "
					+ " clinicalSubtabMapper.ipdCheck as ipdCheck, "
					+ " clinicalSubtabMapper.nurseCheck as nurseCheck, "
					+ " clinicalSubtabMapper.status as status "
					+ " FROM ClinicalSubtabMapper clinicalSubtabMapper"
					+ " WHERE clinicalSubtabMapper.status = 'A'"
					+ " AND clinicalSubtabMapper.headerId =:headerId"
		)
})

@Entity
@Table(name="t_clinical_subtab_mapper",schema="public")
@SequenceGenerator(name="t_clinical_subtab_mapper_seq" , sequenceName="public.t_clinical_subtab_mapper_seq", allocationSize=1)
public class ClinicalSubtabMapper {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "t_clinical_subtab_mapper_seq")
	@Column(name="sub_tab_id")
	private Integer subTabId;
	
	@Column(name="sub_tab_name")
	private String subTabName;
	
	@Column(name="header_id")
	private Integer headerId;
	
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

	public Integer getSubTabId() {
		return subTabId;
	}

	public void setSubTabId(Integer subTabId) {
		this.subTabId = subTabId;
	}

	public String getSubTabName() {
		return subTabName;
	}

	public void setSubTabName(String subTabName) {
		this.subTabName = subTabName;
	}

	public Integer getHeaderId() {
		return headerId;
	}

	public void setHeaderId(Integer headerId) {
		this.headerId = headerId;
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
