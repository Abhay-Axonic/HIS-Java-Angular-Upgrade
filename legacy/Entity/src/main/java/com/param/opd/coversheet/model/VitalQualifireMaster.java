package com.param.opd.coversheet.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@NamedQueries({
	
	@NamedQuery(name="GET_VITAL_QUALIFIRE_MASTER_LIST",
		query="SELECT vitalQualifireMaster.vitalQualifireId AS vitalQualifireId, vitalQualifireMaster.vitalQualifireCode AS vitalQualifireCode, vitalQualifireMaster.vitalQualifireDesc AS vitalQualifireDesc,"
					+ " vitalQualifireMaster.vitalCategoryId AS vitalCategoryId, vitalQualifireMaster.organizationId AS organizationId, "
					+ " vitalQualifireMaster.unitId AS unitId, vitalQualifireMaster.status AS status, vitalCategoryMaster.vitalCategoryDesc as vitalCategoryDesc "
					+ " FROM VitalQualifireMaster vitalQualifireMaster"
					+ " LEFT JOIN vitalQualifireMaster.vitalCategoryMaster vitalCategoryMaster"
					+ " WHERE  vitalQualifireMaster.organizationId= :organizationId "
					+ " ORDER BY vitalQualifireMaster.vitalQualifireId asc"
					),
	
	@NamedQuery(name="GET_LIST_OF_CATEGORY_WISE_QUALIFIERS",
	query="SELECT distinct(vitalQualifireMaster.vitalCategoryId) AS vitalCategoryId"
//			+ " , vitalQualifireMaster.vitalQualifireCode AS vitalQualifireCode, vitalQualifireMaster.vitalQualifireDesc AS vitalQualifireDesc,"
//				+ " vitalQualifireMaster.vitalCategoryId AS vitalCategoryId, vitalQualifireMaster.organizationId AS organizationId, "
//				+ " vitalQualifireMaster.unitId AS unitId, vitalQualifireMaster.status AS status, vitalCategoryMaster.vitalCategoryDesc as vitalCategoryDesc "
				+ " FROM VitalQualifireMaster vitalQualifireMaster"
				+ " WHERE  vitalQualifireMaster.organizationId= :organizationId "
//				+ " ORDER BY vitalCategoryId.vitalQualifireId asc"
				),

	
	@NamedQuery(name="GET_VITAL_QUALIFIRE_MASTER_BY_ID",
		query="SELECT vitalQualifireMaster.vitalQualifireId AS vitalQualifireId, vitalQualifireMaster.vitalQualifireCode AS vitalQualifireCode, vitalQualifireMaster.vitalQualifireDesc AS vitalQualifireDesc,"
					+ " vitalQualifireMaster.vitalCategoryId AS vitalCategoryId, vitalQualifireMaster.organizationId AS organizationId, vitalQualifireMaster.unitId AS unitId, vitalQualifireMaster.status AS status"
					+ " FROM VitalQualifireMaster vitalQualifireMaster"
					+ " WHERE vitalQualifireMaster.vitalQualifireId= :vitalQualifireId"
					+ " AND vitalQualifireMaster.organizationId= :organizationId"
					),
	
	@NamedQuery(name="GET_ACTIVE_VITAL_QUALIFIRE_MASTER",
		query="SELECT vitalQualifireMaster.vitalQualifireId AS vitalQualifireId, vitalQualifireMaster.vitalQualifireCode AS vitalQualifireCode, vitalQualifireMaster.vitalQualifireDesc AS vitalQualifireDesc,"
					+ " vitalQualifireMaster.vitalCategoryId AS vitalCategoryId, vitalQualifireMaster.organizationId AS organizationId, vitalQualifireMaster.unitId AS unitId, vitalQualifireMaster.status AS status"
					+ " FROM VitalQualifireMaster vitalQualifireMaster"
					+ " WHERE vitalQualifireMaster.status= 'A'"
					+ " AND vitalQualifireMaster.organizationId= :organizationId"
					+ " AND vitalQualifireMaster.unitId= :unitId"
					+ " ORDER BY vitalQualifireMaster.vitalQualifireId asc"
					),
	
	@NamedQuery(name="GET_VITAL_QUALIFIRE_MASTER_BY_NAME",
		query="SELECT vitalQualifireMaster.vitalQualifireId AS vitalQualifireId, vitalQualifireMaster.vitalQualifireDesc AS vitalQualifireDesc "
					+ "	FROM VitalQualifireMaster vitalQualifireMaster"
					+ " WHERE LOWER(vitalQualifireMaster.vitalQualifireDesc)= :vitalQualifireDesc"
					+ " AND vitalQualifireMaster.organizationId= :organizationId"
//					+ " AND vitalQualifireMaster.unitId= :unitId"
					),
	
	@NamedQuery(name="GET_VITAL_QUALIFIRE_MASTER_BY_NAME_NOT_ID",
		query="SELECT vitalQualifireMaster.vitalQualifireId AS vitalQualifireId"
					+ " FROM VitalQualifireMaster vitalQualifireMaster"
					+ " WHERE LOWER(vitalQualifireMaster.vitalQualifireDesc)= :vitalQualifireDesc"
					+ " AND vitalQualifireMaster.vitalQualifireId != :vitalQualifireId"
					+ " AND vitalQualifireMaster.organizationId= :organizationId"
					),
	})
@NamedNativeQuery(name="GET_VITAL_CATEGORY_LIST",
					query="select "
							+" 	vcm.vital_category_id as \"vitalCategoryId\", "
							+" 	vcm.vital_category_desc as \"vitalCategoryDesc\" "
							+" from "
							+" 	emr.m_vital_category_master vcm "
							+" where "
//							+" 	 vcm.organization_id =:organizationId "
							+" 	 vcm.status = 'A' "
							+ " ORDER BY vcm.vital_category_desc ASC ")

@Entity
@Table(name = "m_vital_qualifire_master", schema = "emr")
@SequenceGenerator(name = "m_vital_qualifire_master_seq", sequenceName = "emr.m_vital_qualifire_master_seq", allocationSize = 1)
public class VitalQualifireMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_vital_qualifire_master_seq")
	
	@Column(name = "vital_qualifire_id")
	private Integer vitalQualifireId;
	
	@Column(name = "vital_category_id")
	private Integer vitalCategoryId;
	
	@Column(name = "vital_qualifire_code")
	private String vitalQualifireCode;
	
	@Column(name = "vital_qualifire_desc")
	private String vitalQualifireDesc;
	
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

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="vital_category_id", insertable=false, updatable=false)
	private VitalCategoryMaster vitalCategoryMaster;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vitalQualifireMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VitalQualifireMapper> listVitalQualifireMapper;
	
	public Integer getVitalQualifireId() {
		return vitalQualifireId;
	}

	public void setVitalQualifireId(Integer vitalQualifireId) {
		this.vitalQualifireId = vitalQualifireId;
	}

	public Integer getVitalCategoryId() {
		return vitalCategoryId;
	}

	public void setVitalCategoryId(Integer vitalCategoryId) {
		this.vitalCategoryId = vitalCategoryId;
	}

	public String getVitalQualifireCode() {
		return vitalQualifireCode;
	}

	public void setVitalQualifireCode(String vitalQualifireCode) {
		this.vitalQualifireCode = vitalQualifireCode;
	}

	public String getVitalQualifireDesc() {
		return vitalQualifireDesc;
	}

	public void setVitalQualifireDesc(String vitalQualifireDesc) {
		this.vitalQualifireDesc = vitalQualifireDesc;
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
