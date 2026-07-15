package com.param.bloodBank.model;

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

@NamedQueries({
	
					@NamedQuery(name = "CHECK_ACTIVE_BLOOD_BANK_TYPE_COMPONENT_MASTER_AVAILABLE",
					query = "SELECT bloodBankTypeComponentMaster.bloodBankTypeComponentId as bloodBankTypeComponentId,"
						+ " bloodBankTypeComponentMaster.bloodBagTypeId as bloodBagTypeId "
						+ " FROM BloodBankTypeComponentMaster bloodBankTypeComponentMaster"
						+ " WHERE bloodBankTypeComponentMaster.organizationId= :organizationId "
						+ " AND bloodBankTypeComponentMaster.unitId= :unitId "
						+ " AND bloodBankTypeComponentMaster.bloodBagTypeId= :bloodBagTypeId"),
				
					
					@NamedQuery(name = "GET_BLOOD_BANK_TYPE_COMPONENT_MASTER_BY_ID",
					query = "SELECT bloodBankTypeComponentMaster.bloodBankTypeComponentId as bloodBankTypeComponentId,"
					    + " bloodBankTypeComponentMaster.bloodComponentId as bloodComponentId,"
						+ " bloodBankTypeComponentMaster.bloodBagTypeId as bloodBagTypeId,"
						+ " bloodComponentMaster.bloodComponentDesc as bloodComponentDesc,"
						+ " bloodBankTypeComponentMaster.status as status,"
						+ " bloodBankTypeComponentMaster.isDeleted as isDeleted,"
						+ " bloodBankTypeComponentMaster.organizationId as organizationId,"
						+ " bloodBankTypeComponentMaster.unitId as unitId "
						+ " FROM BloodBankTypeComponentMaster bloodBankTypeComponentMaster "
						+ " INNER JOIN bloodBankTypeComponentMaster.bloodComponentMaster bloodComponentMaster "
						+ " WHERE bloodBankTypeComponentMaster.organizationId= :organizationId "
						+ " AND bloodBankTypeComponentMaster.unitId= :unitId "
						+ " AND bloodBankTypeComponentMaster.bloodBagTypeId= :bloodBagTypeId "
						+ " AND bloodBankTypeComponentMaster.isDeleted!= 'Y' "
				
						),
									
					@NamedQuery(name = "GET_ACTIVE_BLOOD_BANK_TYPE_COMPONENT_MASTER_LIST",
					query = "SELECT bloodBankTypeComponentMaster.bloodBankTypeComponentId as bloodBankTypeComponentId,"
					    + " bloodBankTypeComponentMaster.bloodComponentId as bloodComponentId,"
						+ " bloodBankTypeComponentMaster.bloodBagTypeId as bloodBagTypeId,"
						+ " bloodBankTypeComponentMaster.status as status,"
						+ " bloodBankTypeComponentMaster.isDeleted as isDeleted,"
						+ " bloodBankTypeComponentMaster.organizationId as organizationId,"
						+ " bloodBankTypeComponentMaster.unitId as unitId "
						+ " FROM BloodBankTypeComponentMaster bloodBankTypeComponentMaster"
						+ " WHERE bloodBankTypeComponentMaster.status= 'A' "
						+ " AND bloodBankTypeComponentMaster.organizationId= :organizationId "
						+ " AND bloodBankTypeComponentMaster.unitId= :unitId "
						),
					
					@NamedQuery(name = "UPDATE_BLOOD_BANK_TYPE_COMPONENT_MASTER_STATUS",
					query = " UPDATE BloodBankTypeComponentMaster bloodBankTypeComponentMaster "
							+ " SET bloodBankTypeComponentMaster.status = :status"
							+ " WHERE bloodBankTypeComponentMaster.organizationId= :organizationId "
							+ " AND bloodBankTypeComponentMaster.unitId= :unitId "
							+ " AND bloodBankTypeComponentMaster.bloodBagTypeId = :bloodBagTypeId"
						),
					
					@NamedQuery(name = "UPDATE_BLOOD_BANK_TYPE_COMPONENT_MASTER",
					query = " UPDATE BloodBankTypeComponentMaster bloodBankTypeComponentMaster "
							+ " SET bloodBankTypeComponentMaster.isDeleted = :isDeleted"
							+ " WHERE bloodBankTypeComponentMaster.organizationId= :organizationId "
							+ " AND bloodBankTypeComponentMaster.unitId = :unitId"
							+ " AND bloodBankTypeComponentMaster.bloodBagTypeId = :bloodBagTypeId"
						),
					
					@NamedQuery(name = "GET_DETAILED_COMPONENT_LIST",
					query = "SELECT bloodBankTypeComponentMaster.bloodBankTypeComponentId as bloodBankTypeComponentId,"
					    + " bloodBankTypeComponentMaster.bloodComponentId as bloodComponentId,"
						+ " bloodBankTypeComponentMaster.bloodBagTypeId as bloodBagTypeId,"
						+ " bloodComponentMaster.bloodComponentDesc as bloodComponentDesc"
						+ " FROM BloodBankTypeComponentMaster bloodBankTypeComponentMaster"
						+ " INNER JOIN bloodBankTypeComponentMaster.bloodComponentMaster bloodComponentMaster"
						+ " WHERE bloodBankTypeComponentMaster.bloodBagTypeId= :bloodBagTypeId "
						+ " AND bloodBankTypeComponentMaster.organizationId= :organizationId "
						+ " AND bloodBankTypeComponentMaster.unitId= :unitId "
						+ " AND bloodBankTypeComponentMaster.isDeleted!= 'Y' "
						),
			})
				
@NamedNativeQueries({
					@NamedNativeQuery(name="GET_BLOOD_BAG_LIST", 
						query=" select distinct (blood_bag.blood_bag_type_id) as \"bloodBagTypeId\", "
							+ " blood_bag.blood_bag_type_desc as \"bloodBagTypeDesc\" "
							+ " from blood_bank.m_blood_bag_type_master blood_bag "
							+ " where blood_bag.organization_id= :organizationId"
							+ " and blood_bag.unit_id= :unitId "
							+ " and blood_bag.status= 'A' "
						),

								 
					@NamedNativeQuery(name="GET_BLOOD_BANK_TYPE_COMPONENT_MASTER_LIST", 
						query=" select distinct (blood_bag.blood_bag_type_id) as \"bloodBagTypeId\", "
							+ " blood_bag.blood_bag_type_desc as \"bloodBagTypeDesc\", blood_bank_type_comp.status as \"status\"  "
							+ " from blood_bank.m_blood_bag_type_master blood_bag "
							+ " inner join blood_bank.m_blood_bank_type_component_master blood_bank_type_comp " 
							+ " on blood_bag.blood_bag_type_id = blood_bank_type_comp.blood_bag_type_id "
						),
					
					@NamedNativeQuery(name="GET_BLOOD_BANK_TYPE_COMPONENT_MASTER_COUNT", 
						query=" select count(distinct(blood_bag.blood_bag_type_id)) as \"bloodBagTypeId\" "
							+ " from blood_bank.m_blood_bag_type_master blood_bag "
							+ " inner join blood_bank.m_blood_bank_type_component_master blood_bank_type_comp " 
							+ " on blood_bag.blood_bag_type_id = blood_bank_type_comp.blood_bag_type_id "
							+ " where blood_bag.organization_id= :organizationId"
							+ " and blood_bag.unit_id= :unitId"
						)
	})

@Entity
@Table(name = "m_blood_bank_type_component_master", schema = "blood_bank")
@SequenceGenerator(name = "m_blood_bank_type_component_master", sequenceName = "blood_bank.seq_m_blood_bank_type_component_master", allocationSize = 1)
public class BloodBankTypeComponentMaster {
	@Id
	@Column(name = "blood_bank_type_component_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_blood_bank_type_component_master")
	private Integer bloodBankTypeComponentId;
	
	@Column(name = "blood_bag_type_id")
	private Integer bloodBagTypeId;
	
	@Column(name = "blood_component_id")
	private Integer bloodComponentId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
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
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="blood_bag_type_id", insertable=false, updatable=false)
	private BloodBagTypeMaster bloodBagTypeMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="blood_component_id", insertable=false, updatable=false)
	private BloodComponentMaster bloodComponentMaster;


	public Integer getBloodBankTypeComponentId() {
		return bloodBankTypeComponentId;
	}

	public void setBloodBankTypeComponentId(Integer bloodBankTypeComponentId) {
		this.bloodBankTypeComponentId = bloodBankTypeComponentId;
	}

	public Integer getBloodBagTypeId() {
		return bloodBagTypeId;
	}

	public void setBloodBagTypeId(Integer bloodBagTypeId) {
		this.bloodBagTypeId = bloodBagTypeId;
	}

	public Integer getBloodComponentId() {
		return bloodComponentId;
	}

	public void setBloodComponentId(Integer bloodComponentId) {
		this.bloodComponentId = bloodComponentId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getOrganisationId() {
		return organizationId;
	}

	public void setOrganisationId(Integer organizationId) {
		this.organizationId = organizationId;
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

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
}
