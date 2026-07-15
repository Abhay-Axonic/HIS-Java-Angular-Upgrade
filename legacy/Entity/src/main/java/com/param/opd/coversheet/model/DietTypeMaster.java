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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.billing.global.transaction.model.InsuranceMaster;

@NamedQueries({

		@NamedQuery(name = "GET_DIET_TYPE_BY_NAME", query = "SELECT dietTypeMaster.dietTypeId AS dietTypeId "
				+ "FROM DietTypeMaster dietTypeMaster "
				+ "WHERE (LOWER(dietTypeMaster.dietTypeCode)=:dietName OR dietTypeMaster.dietTypeCode=:dietName) " //dilip
				+ "AND dietTypeMaster.organizationId=:orgId "),
		
		@NamedQuery(name = "GET_DIET_TYPE_MASTER_BY_ID", query = "SELECT dietTypeMaster.dietTypeId AS dietTypeId, "
				+ "dietTypeMaster.organizationId AS organizationId, " + "dietTypeMaster.dietTypeCode AS dietTypeCode, "
				+ "dietTypeMaster.dietTypeDescription AS dietTypeDescription, " + "dietTypeMaster.status AS status "
				+ "FROM DietTypeMaster dietTypeMaster "
				+ "WHERE dietTypeMaster.dietTypeId=:dietTypeId AND dietTypeMaster.status='A'"),
		
		@NamedQuery(name = "GET_DIETTYPEMASTER_LIST_BY_ORGANIZATION_ID", query = "SELECT dietTypeMaster.dietTypeId AS dietTypeId, "
				+ "dietTypeMaster.organizationId AS organizationId, " + "dietTypeMaster.dietTypeCode AS dietTypeCode, "
				+ "dietTypeMaster.dietTypeDescription AS dietTypeDescription, " + "dietTypeMaster.status AS status "
				+ "FROM DietTypeMaster dietTypeMaster " + "WHERE dietTypeMaster.organizationId=:orgId "),
		
		@NamedQuery(name = "GET_ALL_DIET_TYPE_MASTER_LIST", query = "SELECT dietTypeMaster.dietTypeId AS dietTypeId, "
				+ "dietTypeMaster.organizationId AS organizationId, " + "dietTypeMaster.dietTypeCode AS dietTypeCode, "
				+ "dietTypeMaster.dietTypeDescription AS dietTypeDescription, " + "dietTypeMaster.status AS status "
				+ "FROM DietTypeMaster dietTypeMaster "
				+ "WHERE dietTypeMaster.organizationId=:organizationId "
				+ " ORDER BY dietTypeMaster.dietTypeDescription ASC " ), // and dietTypeMaster.unitId=:unitId"), //dilip
		
		@NamedQuery(name = "GET_ACTIVE_DIET_TYPE_MASTER_LIST", query = "SELECT dietTypeMaster.dietTypeId AS dietTypeId, "
				+ "dietTypeMaster.organizationId AS organizationId, " + "dietTypeMaster.dietTypeCode AS dietTypeCode, "
				+ "dietTypeMaster.dietTypeDescription AS dietTypeDescription, " + "dietTypeMaster.status AS status "
				+ "FROM DietTypeMaster dietTypeMaster " 
				+ "WHERE dietTypeMaster.status='A' and dietTypeMaster.organizationId=:organizationId "
				+ " ORDER BY dietTypeMaster.dietTypeDescription ASC")  //and dietTypeMaster.unitId=:unitId") //dilip

})

@Entity
@Table(name = "m_diet_type_master", schema = "kitchen")
@SequenceGenerator(name = "diet_type_master_seq", sequenceName = "kitchen.diet_type_master_seq", allocationSize = 1)
public class DietTypeMaster {
	@Id
	@Column(name = "diet_type_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "diet_type_master_seq")
	private Integer dietTypeId;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "diet_type_code")
	private String dietTypeCode;

	@Column(name = "diet_type_description")
	private String dietTypeDescription;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "status")
	private Character status;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "dietTypeMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<IpdDietOrder> ipdDietOrderList;

	public Integer getDietTypeId() {
		return dietTypeId;
	}

	public void setDietTypeId(Integer dietTypeId) {
		this.dietTypeId = dietTypeId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getDietTypeCode() {
		return dietTypeCode;
	}

	public void setDietTypeCode(String dietTypeCode) {
		this.dietTypeCode = dietTypeCode;
	}

	public String getDietTypeDescription() {
		return dietTypeDescription;
	}

	public void setDietTypeDescription(String dietTypeDescription) {
		this.dietTypeDescription = dietTypeDescription;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	
}
