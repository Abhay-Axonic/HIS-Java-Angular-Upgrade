package com.param.global.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;



@NamedQueries({

		@NamedQuery(name = "GET_AMBULANCE_LIST", query = "SELECT ambTypeMaster.ambulanceTypeId as ambulanceTypeId, ambTypeMaster.ambulanceTypeMasterId as ambulanceTypeMasterId,"
				+ "ambTypeMaster.ambulanceType as ambulanceType, " + "ambTypeMaster.status as status "
				+ "FROM AmbulanceType as ambTypeMaster " + "WHERE ambTypeMaster.organizationId=:organizationId "
						+ "ORDER BY ambTypeMaster.ambulanceTypeId DESC"),

		@NamedQuery(name = "GET_AMBULANCE_LIST_BY_NAME", query = "SELECT ambTypeMaster.ambulanceTypeId as ambulanceTypeId, "
				+ "ambTypeMaster.ambulanceType as ambulanceType, ambTypeMaster.ambulanceTypeMasterId as ambulanceTypeMasterId," + "ambTypeMaster.status as status "
				+ "FROM AmbulanceType as ambTypeMaster "
				+ "WHERE (LOWER(ambTypeMaster.ambulanceType)=:ambulanceType OR ambTypeMaster.ambulanceType=:ambulanceType) "
				+ "AND ambTypeMaster.ambulanceTypeId=:ambulanceTypeId AND ambTypeMaster.organizationId=:organizationId "),
		
		@NamedQuery(name = "GET_AMBULANCE_LIST_BY_ID", query = "SELECT ambTypeMaster.ambulanceTypeId as ambulanceTypeId, "
				+ "ambTypeMaster.ambulanceType as ambulanceType, ambTypeMaster.ambulanceTypeMasterId as ambulanceTypeMasterId," + "ambTypeMaster.status as status "
				+ "FROM AmbulanceType as ambTypeMaster WHERE ambTypeMaster.ambulanceTypeMasterId=:ambulanceTypeMasterId "
				+ "and ambTypeMaster.organizationId=:organizationId"),

		@NamedQuery(name = "GET_ACTIVE_AMBULANCE_LIST", query = "SELECT ambTypeMaster.ambulanceTypeId as ambulanceTypeId, "
				+ "ambTypeMaster.ambulanceType as ambulanceType, ambTypeMaster.ambulanceTypeMasterId as ambulanceTypeMasterId, " + "ambTypeMaster.status as status "
				+ "FROM AmbulanceType as ambTypeMaster "
				+ "WHERE ambTypeMaster.status='A' "
				+ "AND ambTypeMaster.organizationId=:organizationId "
				+ "ORDER BY ambTypeMaster.ambulanceType "),
		
		@NamedQuery(name = "GET_AMBULANCE_SINGLE_REC", query = "SELECT ambTypeMaster.ambulanceTypeId as ambulanceTypeId, ambTypeMaster.ambulanceTypeMasterId as ambulanceTypeMasterId,"
				+ "ambTypeMaster.ambulanceType as ambulanceType, " + "ambTypeMaster.status as status "
				+ "FROM AmbulanceType as ambTypeMaster " + "WHERE ambTypeMaster.organizationId=:organizationId "
				+ " AND ambTypeMaster.ambulanceTypeMasterId=:ambulanceTypeMasterId"),
				
})

@Entity
@Table(name = "m_ambulance_type", schema = "adt")
@SequenceGenerator(name = "m_ambulance_type_seq", sequenceName = "adt.m_ambulance_type_seq", allocationSize = 1)
public class AmbulanceType {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_ambulance_type_seq")
	@Column(name = "ambulance_type_master_id")
	private int ambulanceTypeMasterId;

	public int getAmbulanceTypeMasterId() {
		return ambulanceTypeMasterId;
	}

	public void setAmbulanceTypeMasterId(int ambulanceTypeMasterId) {
		this.ambulanceTypeMasterId = ambulanceTypeMasterId;
	}

	@Column(name = "ambulance_type_id")
	private int ambulanceTypeId;

	
	@Column(name = "ambulance_type")
	private String ambulanceType;

	@Column(name = "status")
	private char status;

	@Column(name = "created_by")
	private int createdBy;

	@Column(name = "created_date")
	private String createdDate;

	@Column(name = "updated_by")
	private int updatedBy;

	@Column(name = "updated_date")
	private String updatedDate;

	@Column(name="organization_id")
	private int organizationId;
	

	public int getAmbulanceTypeId() {
		return ambulanceTypeId;
	}

	public void setAmbulanceTypeId(int ambulanceTypeId) {
		this.ambulanceTypeId = ambulanceTypeId;
	}

	public String getAmbulanceType() {
		return ambulanceType;
	}

	public void setAmbulanceType(String ambulanceType) {
		this.ambulanceType = ambulanceType;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public int getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}


	
	

	
	
}
