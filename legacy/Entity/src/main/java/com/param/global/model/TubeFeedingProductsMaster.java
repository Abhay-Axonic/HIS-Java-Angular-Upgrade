package com.param.global.model;

import java.util.Date;
import java.util.List;

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

import com.param.opd.coversheet.model.IpdRylesTubeMaster;

@NamedQueries({
	
	@NamedQuery(name="GET_TUBE_FEEDING_PRODUCTS_LIST",
		query="SELECT tubeFeedingProductsMaster.tubeFeedingProductsId AS tubeFeedingProductsId, tubeFeedingProductsMaster.productsCode AS productsCode, tubeFeedingProductsMaster.productsDesc AS productsDesc,"
					+ " tubeFeedingProductsMaster.organisationId AS organisationId, tubeFeedingProductsMaster.unitId AS unitId, tubeFeedingProductsMaster.status AS status"
					+ " FROM TubeFeedingProductsMaster tubeFeedingProductsMaster"
					+ " WHERE tubeFeedingProductsMaster.organisationId= :organisationId"
					//+ " AND tubeFeedingProductsMaster.unitId= :unitId" //coment by minesh bcoz its only on org level.
					+ " ORDER BY tubeFeedingProductsMaster.tubeFeedingProductsId asc"
					),
	
	@NamedQuery(name="GET_TUBE_FEEDING_PRODUCTS_BY_ID",
		query="SELECT tubeFeedingProductsMaster.tubeFeedingProductsId AS tubeFeedingProductsId, tubeFeedingProductsMaster.productsCode AS productsCode, tubeFeedingProductsMaster.productsDesc AS productsDesc,"
					+ " tubeFeedingProductsMaster.organisationId AS organisationId, tubeFeedingProductsMaster.unitId AS unitId, tubeFeedingProductsMaster.status AS status"
					+ " FROM TubeFeedingProductsMaster tubeFeedingProductsMaster"
					+ " WHERE tubeFeedingProductsMaster.tubeFeedingProductsId= :tubeFeedingProductsId"
					+ " AND tubeFeedingProductsMaster.organisationId= :organisationId"
					+ " AND tubeFeedingProductsMaster.unitId= :unitId"
					),
	
	@NamedQuery(name="GET_ACTIVE_TUBE_FEEDING_PRODUCTS",
		query="SELECT tubeFeedingProductsMaster.tubeFeedingProductsId AS tubeFeedingProductsId, tubeFeedingProductsMaster.productsCode AS productsCode, tubeFeedingProductsMaster.productsDesc AS productsDesc,"
					+ " tubeFeedingProductsMaster.organisationId AS organisationId, tubeFeedingProductsMaster.unitId AS unitId, tubeFeedingProductsMaster.status AS status"
					+ " FROM TubeFeedingProductsMaster tubeFeedingProductsMaster"
					+ " WHERE tubeFeedingProductsMaster.status= 'A'"
					+ " AND tubeFeedingProductsMaster.organisationId= :organisationId"
					//+ " AND tubeFeedingProductsMaster.unitId= :unitId" //coment by minesh bcoz its only on org level.
					+ " ORDER BY tubeFeedingProductsMaster.productsDesc asc"
					),
	
	@NamedQuery(name="GET_AVAILABLE_TUBE_FEEDING_PRODUCTS",
		query="SELECT tubeFeedingProductsMaster.tubeFeedingProductsId AS tubeFeedingProductsId "
					+ "	FROM TubeFeedingProductsMaster tubeFeedingProductsMaster"
					+ " WHERE LOWER(tubeFeedingProductsMaster.productsDesc)= :productsDesc"
					+ " AND tubeFeedingProductsMaster.organisationId= :organisationId" //added by minesh for save
					),
	
	@NamedQuery(name="GET_TUBE_FEEDING_PRODUCTS_BY_NAME_NOT_ID",
		query="SELECT tubeFeedingProductsMaster.tubeFeedingProductsId AS tubeFeedingProductsId"
					+ " FROM TubeFeedingProductsMaster tubeFeedingProductsMaster"
					+ " WHERE LOWER(tubeFeedingProductsMaster.productsDesc)= :productsDesc"
					+ " AND tubeFeedingProductsMaster.tubeFeedingProductsId != :tubeFeedingProductsId"
					+ " AND tubeFeedingProductsMaster.organisationId= :organisationId" //added by minesh for update
					),
	})
@Entity
@Table(name = "m_tube_feeding_products", schema = "kitchen")
@SequenceGenerator(name = "m_tube_feeding_products_seq", sequenceName = "kitchen.m_tube_feeding_products_seq", allocationSize = 1)

public class TubeFeedingProductsMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_tube_feeding_products_seq")
	
	@Column(name = "tube_feeding_products_id")
	private Integer  tubeFeedingProductsId;
	
	@Column(name = "  products_code")
	private String   productsCode;
	
	@Column(name = "products_desc")
	private String productsDesc;
	
	@Column(name = "organisation_id")
	private Integer organisationId;
	
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
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tubeFeedingProductsMaster")
	private List<IpdRylesTubeMaster> listTubeFeedingProductsMaster;

	public Integer getTubeFeedingProductsId() {
		return tubeFeedingProductsId;
	}

	public void setTubeFeedingProductsId(Integer tubeFeedingProductsId) {
		this.tubeFeedingProductsId = tubeFeedingProductsId;
	}

	public String getProductsCode() {
		return productsCode;
	}

	public void setProductsCode(String productsCode) {
		this.productsCode = productsCode;
	}

	public String getProductsDesc() {
		return productsDesc;
	}

	public void setProductsDesc(String productsDesc) {
		this.productsDesc = productsDesc;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
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
	
	
	
