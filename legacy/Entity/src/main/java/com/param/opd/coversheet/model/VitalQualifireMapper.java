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
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.VitalMaster;


@NamedQueries({
@NamedQuery(name="GET_VITAL_QUALIFIRE_MAPPER_LIST",
query="SELECT distinct(vitalQualifireMapper.vitalId) AS vitalId,"
			+ " vitalMaster.vitalName as vitalName, vitalQualifireMapper.status as status "
			+ " FROM VitalQualifireMapper vitalQualifireMapper "
			+ " INNER JOIN vitalQualifireMapper.vitalMaster vitalMaster"
			+ " WHERE  vitalQualifireMapper.organizationId= :organizationId "
			+ " AND vitalQualifireMapper.isDeleted= 'N' "
			+ " ORDER BY vitalQualifireMapper.vitalId asc"
			
			),

@NamedQuery(name="GET_VITAL_QUALIFIRE_MAPPER_LIST_BY_ID",
query="SELECT distinct(vitalQualifireMapper.vitalId) AS vitalId,"
			+ " vitalMaster.vitalName as vitalName "
			+ " FROM VitalQualifireMapper vitalQualifireMapper "
			+ " INNER JOIN vitalQualifireMapper.vitalMaster vitalMaster"
			+ " WHERE  vitalQualifireMapper.organizationId= :organizationId "
			+ " AND  vitalQualifireMapper.vitalId= :vitalId "
			+ " ORDER BY vitalQualifireMapper.vitalId asc"
			),

@NamedQuery(name="GET_VITAL_QUALIFIRE_MAPPER_LIST_FOR_CATEGORY_IN_MAPPER",
query="SELECT distinct(vitalQualifireMaster.vitalCategoryId) AS vitalCategoryId, vitalQualifireMapper.status as status, "
			+ " vitalCategoryMaster.vitalCategoryDesc as vitalCategoryDesc "
			+ " FROM VitalQualifireMapper vitalQualifireMapper "
			+ " INNER JOIN vitalQualifireMapper.vitalQualifireMaster vitalQualifireMaster"
			+ " INNER JOIN vitalQualifireMaster.vitalCategoryMaster vitalCategoryMaster"
			+ " WHERE  vitalQualifireMapper.organizationId= :organizationId "
			+ " AND vitalQualifireMapper.vitalId= :vitalId "
			+ " AND vitalQualifireMapper.isDeleted= 'N' "
//			+ " ORDER BY vitalQualifireMapper.vitalQualifireMapperId asc"
			),

@NamedQuery(name="GET_VITAL_QUALIFIRE_MAPPER_LIST_FOR_CATEGORY_FOR_UPDATE",
query="SELECT distinct(vitalQualifireMaster.vitalCategoryId) AS vitalCategoryId, vitalQualifireMapper.status as status, "
			+ " vitalCategoryMaster.vitalCategoryDesc as vitalCategoryDesc "
			+ " FROM VitalQualifireMapper vitalQualifireMapper "
			+ " INNER JOIN vitalQualifireMapper.vitalQualifireMaster vitalQualifireMaster"
			+ " INNER JOIN vitalQualifireMaster.vitalCategoryMaster vitalCategoryMaster"
			+ " WHERE  vitalQualifireMapper.organizationId= :organizationId "
			+ " AND vitalQualifireMapper.vitalId= :vitalId "
//			+ " AND vitalQualifireMapper.isDeleted= 'N' "
//			+ " ORDER BY vitalQualifireMapper.vitalQualifireMapperId asc"
			),


@NamedQuery(name="GET_VITAL_QUALIFIRE_MAPPER_LIST_FOR_CATEGORY",
query="SELECT distinct(vitalQualifireMaster.vitalCategoryId) AS vitalCategoryId , vitalQualifireMapper.vitalId as vitalId  "
			+ " FROM VitalQualifireMapper vitalQualifireMapper "
			+ " WHERE  vitalQualifireMapper.organizationId= :organizationId "
			+ " AND vitalQualifireMapper.vitalId= :vitalId "
			+ " AND vitalQualifireMapper.isDeleted= 'N' "
//			+ " ORDER BY vitalQualifireMapper.vitalQualifireMapperId asc"
			),

@NamedQuery(name="GET_VITAL_QUALIFIRE_MAPPER_LIST_FOR_QUALIFIRES_IN_MAPPER",
query="SELECT vitalQualifireMaster.vitalCategoryId AS vitalCategoryId, vitalQualifireMapper.status as status, "
			+ " vitalQualifireMaster.vitalQualifireDesc as vitalQualifireDesc, vitalQualifireMapper.vitalQualifireId as vitalQualifireId,"
			+ " vitalQualifireMapper.vitalId as vitalId "
			+ " FROM VitalQualifireMapper vitalQualifireMapper "
			+ " INNER JOIN vitalQualifireMapper.vitalQualifireMaster vitalQualifireMaster"
			+ " WHERE  vitalQualifireMapper.organizationId= :organizationId "
			+ " AND vitalQualifireMaster.vitalCategoryId= :vitalCategoryId "
			+ " AND vitalQualifireMapper.vitalId= :vitalId "
			+ " AND vitalQualifireMapper.isDeleted= 'N' "
//			+ " ORDER BY vitalQualifireMapper.vitalQualifireMapperId asc"
//			+ " AND vitalQualifireMapper.status='A'"
			),

@NamedQuery(name="GET_VITAL_QUALIFIRE_MAPPER_LIST_FOR_QUALIFIRES_FOR_UPDATE",
query="SELECT vitalQualifireMaster.vitalCategoryId AS vitalCategoryId, vitalQualifireMapper.status as status, "
			+ " vitalQualifireMaster.vitalQualifireDesc as vitalQualifireDesc, vitalQualifireMapper.vitalQualifireId as vitalQualifireId,"
			+ " vitalQualifireMapper.vitalId as vitalId "
			+ " FROM VitalQualifireMapper vitalQualifireMapper "
			+ " INNER JOIN vitalQualifireMapper.vitalQualifireMaster vitalQualifireMaster"
			+ " WHERE  vitalQualifireMapper.organizationId= :organizationId "
			+ " AND vitalQualifireMaster.vitalCategoryId= :vitalCategoryId "
			+ " AND vitalQualifireMapper.vitalId= :vitalId "
//			+ " AND vitalQualifireMapper.status= 'A' "
			+ " AND vitalQualifireMapper.isDeleted= 'N' "
			),

@NamedQuery(name="GET_VITAL_QUALIFIRE_MAPPER_LIST_FOR_QUALIFIRES",
query="SELECT vitalQualifireMaster.vitalCategoryId AS vitalCategoryId, vitalQualifireMapper.status as status, "
			+ " vitalQualifireMaster.vitalQualifireDesc as vitalQualifireDesc, vitalQualifireMapper.vitalQualifireId as vitalQualifireId,"
			+ " vitalQualifireMapper.vitalId as vitalId "
			+ " FROM VitalQualifireMapper vitalQualifireMapper "
			+ " INNER JOIN vitalQualifireMapper.vitalQualifireMaster vitalQualifireMaster"
			+ " WHERE  vitalQualifireMapper.organizationId= :organizationId "
			+ " AND vitalQualifireMaster.vitalCategoryId= :vitalCategoryId "
			+ " AND vitalQualifireMapper.vitalId= :vitalId "
//			+ " ORDER BY vitalQualifireMapper.vitalQualifireMapperId asc"
			+ " AND vitalQualifireMapper.isDeleted= 'N' "
			+ " AND vitalQualifireMapper.status='A'"
			),

@NamedQuery(name="GET_VITAL_QUALIFIRE_MAPPER_BY_ID",
query="SELECT vitalQualifireMapper.vitalQualifireMapperId AS vitalQualifireMapperId,"
			+ " vitalQualifireMapper.organizationId AS organizationId, vitalQualifireMapper.unitId AS unitId, vitalQualifireMapper.status AS status"
			+ " FROM VitalQualifireMapper vitalQualifireMapper"
			+ " WHERE vitalQualifireMapper.vitalQualifireMapperId= :vitalQualifireMapperId"
			+ " AND vitalQualifireMapper.organizationId= :organizationId"
			),

@NamedQuery(name="GET_ACTIVE_VITAL_QUALIFIRE_MAPPER",
query="SELECT vitalQualifireMapper.vitalQualifireMapperId AS vitalQualifireMapperId,"
			+ " vitalQualifireMapper.organizationId AS organizationId, vitalQualifireMapper.unitId AS unitId, vitalQualifireMapper.status AS status"
			+ " FROM VitalQualifireMapper vitalQualifireMapper"
			+ " WHERE vitalQualifireMapper.status= 'A'"
			+ " AND vitalQualifireMapper.organizationId= :organizationId"
			+ " AND vitalQualifireMapper.unitId= :unitId"
			+ " ORDER BY vitalQualifireMapper.vitalQualifireMapperId asc"
			),
/*
@NamedQuery(name="GET_VITAL_QUALIFIRE_MAPPER_BY_NAME",
query="SELECT vitalQualifireMapper.vitalQualifireId AS vitalQualifireId "
			+ "	FROM VitalQualifireMapper vitalQualifireMapper"
			+ " WHERE LOWER(vitalQualifireMapper.vitalQualifireDesc)= :vitalQualifireDesc"
			),*/

@NamedQuery(name="GET_VITAL_QUALIFIRE_MAPPER_BY_NAME_NOT_ID",
query="SELECT vitalQualifireMapper.vitalId AS vitalId, vitalQualifireMapper.vitalCategoryId AS vitalCategoryId "
			+ " FROM VitalQualifireMapper vitalQualifireMapper"
			+ " WHERE  vitalQualifireMapper.vitalId = :vitalId"
			+ " AND vitalQualifireMapper.organizationId= :orgId "
//			+ " AND vitalQualifireMapper.vitalCategoryId=:vitalCategoryId"
			),

@NamedQuery(name = "UPDATE_VITAL_QUALIFIRE_MAPPER",
query = "UPDATE VitalQualifireMapper vitalQualifireMapper "
		+ "  SET vitalQualifireMapper.isDeleted = :isDeleted"
		+ " WHERE vitalQualifireMapper.organizationId= :organizationId "
		+ " AND vitalQualifireMapper.vitalId= :vitalId "),
})
@NamedNativeQueries({
	@NamedNativeQuery(name="GET_VITAL_QUALIFIRE_LIST_BY_CATEGORY_ID",
			query="SELECT mst.vital_category_id as \"vitalCategoryId\" ,mst.vital_qualifire_desc as \"vitalQualifireDesc\", mst.vital_qualifire_id as \"vitalQualifireId\" "
					+ " FROM emr.m_vital_qualifire_master mst " + 
					" WHERE mst.vital_category_id=:vitalCategoryId"
					+ " and mst.organization_id=:organizationId")
			
})

@Entity
@Table(name = "t_vital_qualifire_mapper", schema = "emr")
@SequenceGenerator(name = "t_vital_qualifire_mapper_seq", sequenceName = "emr.t_vital_qualifire_mapper_seq", allocationSize = 1)
public class VitalQualifireMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_vital_qualifire_mapper_seq")
	
	@Column(name = "vital_qualifire_mapper_id")
	private Integer vitalQualifireMapperId;
	
	@Column(name = "vital_id")
	private Integer vitalId;
	
	@Column(name = "vital_qualifire_id")
	private Integer vitalQualifireId;
	
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
	
	@Column(name = "vital_category_id")
	private Integer vitalCategoryId;
	
	@Column(name = "is_deleted")
	private Character isDeleted;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="vital_id", insertable=false, updatable=false)
	private VitalMaster vitalMaster;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="vital_qualifire_id", insertable=false, updatable=false)
	private VitalQualifireMaster vitalQualifireMaster;
	
	public Integer getVitalQualifireMapperId() {
		return vitalQualifireMapperId;
	}

	public void setVitalQualifireMapperId(Integer vitalQualifireMapperId) {
		this.vitalQualifireMapperId = vitalQualifireMapperId;
	}

	public Integer getVitalId() {
		return vitalId;
	}

	public void setVitalId(Integer vitalId) {
		this.vitalId = vitalId;
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

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
}
