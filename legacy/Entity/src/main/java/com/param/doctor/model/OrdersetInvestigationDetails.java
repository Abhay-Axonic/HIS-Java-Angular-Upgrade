package com.param.doctor.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@NamedQuery(name = "UPDATE_ORDER_SET_INVESTIGATION_DETAILS",
query = " UPDATE OrdersetInvestigationDetails ordersetInvestigationDetails "
		+ " SET ordersetInvestigationDetails.isDeleted = :isDeleted"
		+ " WHERE ordersetInvestigationDetails.orgId= :orgId "
		+ " AND ordersetInvestigationDetails.unitId = :unitId"
		+ " AND ordersetInvestigationDetails.ordersetMasterId = :ordersetMasterId"
	)
@Entity
@Table(name = "t_orderset_investigation_details" , schema = "doctor")
@SequenceGenerator(name = "orderset_investigation_details_seq" , sequenceName = "doctor.orderset_investigation_details_seq" , allocationSize = 1)
public class OrdersetInvestigationDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "orderset_investigation_details_seq")
	@Column(name = "orderset_investigation_details_id")
	private int ordersetInvestigationDetailsId;
	
	@Column(name = "orderset_master_id")
	private Integer ordersetMasterId;
	
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "speciality_id")
	private Integer specialityId;
	
	@Column(name = "sub_speciality_id")
	private Integer subSpecialityId;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "is_deleted")
	private Character isDeleted;

	public int getOrdersetInvestigationDetailsId() {
		return ordersetInvestigationDetailsId;
	}

	public void setOrdersetInvestigationDetailsId(int ordersetInvestigationDetailsId) {
		this.ordersetInvestigationDetailsId = ordersetInvestigationDetailsId;
	}

	public Integer getOrdersetMasterId() {
		return ordersetMasterId;
	}

	public void setOrdersetMasterId(Integer ordersetMasterId) {
		this.ordersetMasterId = ordersetMasterId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
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

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}

	
	
}
