package com.param.adt.master.unit.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.VisitTypeMaster;


@NamedQuery(name="GET_BILLING_BED_CATEGORY_VISIT_LIST",
query="SELECT bbc.visitTypeId as id "
		+ " FROM BillingBedVisitTypeMapper bbc "
		//+ " WHERE bbc.unitId=:unitId "
		+ " WHERE bbc.organizationId=:orgId "
		+ " AND bbc.billingBedCategoryId=:billingBedCategoryId "
		+ " AND bbc.status='A' ")	

@Entity
@Table(name="t_billing_bed_visit_type_mapper",schema="adt")
@SequenceGenerator(name="billing_bed_visit_type_mapper_seq",sequenceName="adt.billing_bed_visit_type_mapper_seq",allocationSize=1)
public class BillingBedVisitTypeMapper {

	@Id
	@Column(name="billing_bed_visit_type_mapper_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="billing_bed_visit_type_mapper_seq")
	private Integer billingBedVisitTypeMapperId;
	
	@Column(name="billing_bed_category_id")
	private Integer billingBedCategoryId;

	@Column(name="visit_type_id")
	private Integer visitTypeId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "billing_bed_category_id", insertable = false, updatable = false)
	private BillingBedCategoryMaster billingBedCategoryMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "visit_type_id", insertable = false, updatable = false)
	private VisitTypeMaster visitTypeMaster;

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getBillingBedVisitTypeMapperId() {
		return billingBedVisitTypeMapperId;
	}

	public void setBillingBedVisitTypeMapperId(Integer billingBedVisitTypeMapperId) {
		this.billingBedVisitTypeMapperId = billingBedVisitTypeMapperId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
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


/*CREATE TABLE adt.t_billing_bed_visit_type_mapper
(
    billing_bed_visit_type_mapper_id integer NOT NULL DEFAULT nextval('adt.billing_bed_visit_type_mapper_seq'::regclass),
    billing_bed_category_id integer,
    visit_type_id integer,
    organization_id integer,
    unit_id integer,
    created_by integer,
    created_date timestamp without time zone DEFAULT now(),
    updated_by integer,
    updated_date timestamp without time zone DEFAULT now(),
    status character(1) COLLATE pg_catalog."default" DEFAULT 'A'::bpchar,
    CONSTRAINT t_billing_bed_visit_type_mapper_pkey PRIMARY KEY (billing_bed_visit_type_mapper_id)
)*/