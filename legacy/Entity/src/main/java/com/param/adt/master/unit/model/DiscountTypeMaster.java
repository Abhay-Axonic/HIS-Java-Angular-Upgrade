package com.param.adt.master.unit.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(name="GET_DISCOUNT_CATEGORY_LIST",
	query="SELECT mstr.discountTypeId as discountTypeId,mstr.code as code,mstr.type as type,mstr.isActive as isActive "
					+ " FROM DiscountTypeMaster mstr"
					+ " WHERE mstr.organizationId=:orgId "
					+ " AND mstr.unitId=:unitId"),
	
	@NamedQuery(name="GET_DISCOUNT_CATEGORY_BY_ID",
	query="SELECT mstr.discountTypeId as discountTypeId,mstr.code as code,mstr.type as type,mstr.isActive as isActive "
					+ " FROM DiscountTypeMaster mstr"
					+ " WHERE "
//					+ "mstr.organizationId=:orgId "
//					+ " AND mstr.unitId=:unitId "
					+ "  mstr.discountTypeId=:id"
					),
	
	@NamedQuery(name="GET_DISCOUNT_CATEGORY_EXIST_OR_NOT_BY_NAME",
	query="SELECT mstr.discountTypeId as discountTypeId"
					+ " FROM DiscountTypeMaster mstr"
					+ " WHERE mstr.organizationId=:orgId "
					+ " AND mstr.unitId=:unitId "
					+ " AND lower(mstr.code)=:code "
					+ " AND lower(mstr.type)=:type"),
	
	@NamedQuery(name="GET_DISCOUNT_CATEGORY_EXIST_OR_NOT_BY_NAME_NOT_BY_ID",
	query="SELECT mstr.discountTypeId as discountTypeId"
					+ " FROM DiscountTypeMaster mstr"
					+ " WHERE mstr.organizationId=:orgId "
					+ " AND mstr.unitId=:unitId "
					+ " AND lower(mstr.code)=:code "
					+ " AND lower(mstr.type)=:type "
					+ " AND mstr.discountTypeId!=:id")
})
@Entity
@Table(name="m_discount_type",schema="public")
public class DiscountTypeMaster {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer discountTypeId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="code")
	private String code;
	
	@Column(name="type")
	private String type;
	
	@Column(name="is_active")
	private Boolean isActive;
	
	@Column(name="is_deleted")
	private Boolean isDeleted;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="added_by")
	private Integer addedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="added_date")
	private Date addedDate;

	public Integer getDiscountTypeId() {
		return discountTypeId;
	}

	public void setDiscountTypeId(Integer discountTypeId) {
		this.discountTypeId = discountTypeId;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(Integer addedBy) {
		this.addedBy = addedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}
	
}
