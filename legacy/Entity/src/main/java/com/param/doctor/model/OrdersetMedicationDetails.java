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

@NamedQuery(name = "UPDATE_ORDER_SET_MEDICATION_DETAILS",
query = " UPDATE OrdersetMedicationDetails ordersetMedicationDetails "
		+ " SET ordersetMedicationDetails.isDeleted = :isDeleted"
		+ " WHERE ordersetMedicationDetails.orgId= :orgId "
		+ " AND ordersetMedicationDetails.unitId = :unitId"
		+ " AND ordersetMedicationDetails.ordersetMasterId = :ordersetMasterId"
	)


@Entity
@Table(name = "t_orderset_medication_details" , schema = "doctor")
@SequenceGenerator(name = "orderset_medication_details_seq" , sequenceName = "doctor.orderset_medication_details_seq" , allocationSize = 1)
public class OrdersetMedicationDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "orderset_medication_details_seq")
	@Column(name = "orderset_medication_details_id")
	private int ordersetMedicationDetailsId;
	
	@Column(name = "orderset_master_id")
	private Integer ordersetMasterId;
	
	@Column(name = "item_master_id")
	private Integer itemMasterId;
	
	@Column(name = "dose")
	private Integer dose;
	
	@Column(name = "route_id")
	private Integer routeId;
	
	@Column(name = "dose_unit_id")
	private Integer doseUnitId;
	
	@Column(name = "frequency_id")
	private Integer frequencyId;
	
	@Column(name = "instruction_id")
	private Integer instructionId;
	
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
	
	public int getOrdersetMedicationDetailsId() {
		return ordersetMedicationDetailsId;
	}

	public void setOrdersetMedicationDetailsId(int ordersetMedicationDetailsId) {
		this.ordersetMedicationDetailsId = ordersetMedicationDetailsId;
	}

	public Integer getOrdersetMasterId() {
		return ordersetMasterId;
	}

	public void setOrdersetMasterId(Integer ordersetMasterId) {
		this.ordersetMasterId = ordersetMasterId;
	}

	public Integer getItemMasterId() {
		return itemMasterId;
	}

	public void setItemMasterId(Integer itemMasterId) {
		this.itemMasterId = itemMasterId;
	}

	public Integer getDose() {
		return dose;
	}

	public void setDose(Integer dose) {
		this.dose = dose;
	}

	public Integer getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Integer frequencyId) {
		this.frequencyId = frequencyId;
	}

	public Integer getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(Integer instructionId) {
		this.instructionId = instructionId;
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

	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public Integer getDoseUnitId() {
		return doseUnitId;
	}

	public void setDoseUnitId(Integer doseUnitId) {
		this.doseUnitId = doseUnitId;
	}

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}

	
	
}