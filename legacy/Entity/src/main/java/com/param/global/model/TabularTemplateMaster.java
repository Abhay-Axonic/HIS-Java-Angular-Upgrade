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

@NamedQueries({
	
	@NamedQuery(name="GET_TABULAR_TEMPLATE_MASTER_LIST",
		query="SELECT tabularTemplateMaster.tabularTemplateId AS tabularTemplateId,"
				   + " tabularTemplateMaster.tabularTemplateCode AS tabularTemplateCode,"
				   + " tabularTemplateMaster.tabularTemplateDesc AS tabularTemplateDesc,"
					+ " tabularTemplateMaster.organizationId AS organizationId, tabularTemplateMaster.unitId AS unitId, tabularTemplateMaster.status AS status"
					+ " FROM TabularTemplateMaster tabularTemplateMaster"
					+ " WHERE  tabularTemplateMaster.organizationId= :organizationId "
					+ " AND tabularTemplateMaster.unitId= :unitId"
					+ " ORDER BY tabularTemplateMaster.tabularTemplateId asc"
					),
	
	@NamedQuery(name="GET_TABULAR_TEMPLATE_MASTER_BY_ID",
		query="SELECT tabularTemplateMaster.tabularTemplateId AS tabularTemplateId, tabularTemplateMaster.tabularTemplateCode AS tabularTemplateCode, tabularTemplateMaster.tabularTemplateDesc AS tabularTemplateDesc,"
					+ " tabularTemplateMaster.organizationId AS organizationId, tabularTemplateMaster.unitId AS unitId, tabularTemplateMaster.status AS status"
					+ " FROM TabularTemplateMaster tabularTemplateMaster"
					+ " WHERE tabularTemplateMaster.tabularTemplateId= :tabularTemplateId"
					+ " AND tabularTemplateMaster.organizationId= :organizationId"
					+ " AND tabularTemplateMaster.unitId= :unitId"
					),
	
	@NamedQuery(name="GET_ACTIVE_TABULAR_TEMPLATE_MASTER",
		query="SELECT tabularTemplateMaster.tabularTemplateId AS tabularTemplateId, tabularTemplateMaster.tabularTemplateCode AS tabularTemplateCode, tabularTemplateMaster.tabularTemplateDesc AS tabularTemplateDesc,"
					+ " tabularTemplateMaster.organizationId AS organizationId, tabularTemplateMaster.unitId AS unitId, tabularTemplateMaster.status AS status"
					+ " FROM TabularTemplateMaster tabularTemplateMaster"
					+ " WHERE tabularTemplateMaster.status= 'A'"
					+ " AND tabularTemplateMaster.organizationId= :organizationId"
					+ " AND tabularTemplateMaster.unitId= :unitId"
					+ " ORDER BY tabularTemplateMaster.tabularTemplateId asc"
					),
	
	@NamedQuery(name="GET_TABULAR_TEMPLATE_MASTER_BY_NAME",
		query="SELECT tabularTemplateMaster.tabularTemplateId AS tabularTemplateId "
					+ "	FROM TabularTemplateMaster tabularTemplateMaster"
					+ " WHERE (LOWER(tabularTemplateMaster.tabularTemplateDesc)=:tabularTemplateDesc OR tabularTemplateMaster.tabularTemplateDesc=:tabularTemplateDesc) "
					+ " AND tabularTemplateMaster.unitId= :unitId"
					),
	
	@NamedQuery(name="GET_TABULAR_TEMPLATE_MASTER_BY_NAME_NOT_ID",
		query="SELECT tabularTemplateMaster.tabularTemplateId AS tabularTemplateId"
					+ " FROM TabularTemplateMaster tabularTemplateMaster"
					+ " WHERE (LOWER(tabularTemplateMaster.tabularTemplateDesc)=:tabularTemplateDesc OR tabularTemplateMaster.tabularTemplateDesc=:tabularTemplateDesc) "
					+ " AND tabularTemplateMaster.tabularTemplateId != :tabularTemplateId"
					+ " AND tabularTemplateMaster.unitId = :unitId"
					),
	})


@Entity
@Table(name = "m_tabular_template_master", schema = "public")
@SequenceGenerator(name = "seq_m_tabular_template_master", sequenceName = "public.seq_m_tabular_template_master", allocationSize = 1)
public class TabularTemplateMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_tabular_template_master")
	
	@Column(name = "tabular_template_id")
	private Integer tabularTemplateId;
	
	@Column(name = "tabular_template_code")
	private String tabularTemplateCode;
	
	@Column(name = "tabular_template_desc")
	private String tabularTemplateDesc;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;

	public Integer getTabularTemplateId() {
		return tabularTemplateId;
	}

	public void setTabularTemplateId(Integer tabularTemplateId) {
		this.tabularTemplateId = tabularTemplateId;
	}
	
	public String getTabularTemplateCode() {
		return tabularTemplateCode;
	}

	public void setTabularTemplateCode(String tabularTemplateCode) {
		this.tabularTemplateCode = tabularTemplateCode;
	}

	public String getTabularTemplateDesc() {
		return tabularTemplateDesc;
	}

	public void setTabularTemplateDesc(String tabularTemplateDesc) {
		this.tabularTemplateDesc = tabularTemplateDesc;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	

}
