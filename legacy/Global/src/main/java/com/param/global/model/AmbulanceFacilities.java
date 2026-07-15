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

		@NamedQuery(name = "GET_AMBULANCE_FACILITIES_LIST", query = "SELECT ambFacilitiesMaster.ambFacilities as ambFacilities, ambFacilitiesMaster.ambulanceFacilitiesMasterId as ambulanceFacilitiesMasterId,"
				+ "ambFacilitiesMaster.status as status "
				+ "FROM AmbulanceFacilities as ambFacilitiesMaster " + "WHERE ambFacilitiesMaster.organizationId=:organizationId "
				+ "ORDER BY ambFacilitiesMaster.ambFacilities DESC"),

		@NamedQuery(name = "GET_AMBULANCE_FACILITIES_LIST_BY_NAME", query = "SELECT ambFacilitiesMaster.ambFacilities as ambFacilities, ambFacilitiesMaster.ambulanceFacilitiesMasterId as ambulanceFacilitiesMasterId,"
				+ "ambFacilitiesMaster.status as status "
				+ "FROM AmbulanceFacilities as ambFacilitiesMaster "
				+ "WHERE (LOWER(ambFacilitiesMaster.ambFacilities)=:ambFacilities OR ambFacilitiesMaster.ambFacilities=:ambFacilities) "
				+ "AND ambFacilitiesMaster.organizationId=:organizationId "),
		
		@NamedQuery(name = "GET_AMBULANCE_FACILITIES_LIST_BY_ID", query ="SELECT ambFacilitiesMaster.ambFacilities as ambFacilities, ambFacilitiesMaster.ambulanceFacilitiesMasterId as ambulanceFacilitiesMasterId,"
				+ "ambFacilitiesMaster.status as status "
				+ "FROM AmbulanceFacilities as ambFacilitiesMaster WHERE ambFacilitiesMaster.ambulanceFacilitiesMasterId=:ambulanceFacilitiesMasterId "
				+ "and ambFacilitiesMaster.organizationId=:organizationId"),

		@NamedQuery(name = "GET_ACTIVE_AMBULANCE_FACILITIES_LIST", query = "SELECT ambFacilitiesMaster.ambFacilities as ambFacilities, ambFacilitiesMaster.ambulanceFacilitiesMasterId as ambulanceFacilitiesMasterId,"
				+ "ambFacilitiesMaster.status as status "
				+ "FROM AmbulanceFacilities as ambFacilitiesMaster "
				+ "WHERE ambFacilitiesMaster.status='A' "
				+ "AND ambFacilitiesMaster.organizationId=:organizationId "
				+ "ORDER BY ambFacilitiesMaster.ambFacilities "),
		
		@NamedQuery(name = "GET_AMBULANCE_FACILITIES_SINGLE_REC", query = "SELECT ambFacilitiesMaster.ambFacilities as ambFacilities, ambFacilitiesMaster.ambulanceFacilitiesMasterId as ambulanceFacilitiesMasterId,"
				+ "ambFacilitiesMaster.status as status "
				+ "FROM AmbulanceFacilities as ambFacilitiesMaster " + "WHERE ambFacilitiesMaster.organizationId=:organizationId "
				+ " AND ambFacilitiesMaster.ambulanceFacilitiesMasterId=:ambulanceFacilitiesMasterId"),
				
})

@Entity
@Table(name = "m_ambulance_facilities", schema = "adt")
@SequenceGenerator(name = "m_ambulance_facilities_seq", sequenceName = "adt.m_ambulance_facilities_seq", allocationSize = 1)
public class AmbulanceFacilities {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_ambulance_facilities_seq")
	@Column(name = "ambulance_facilities_master_id")
	private int ambulanceFacilitiesMasterId;

	@Column(name = "amb_facilities")
	private String ambFacilities;

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
	
	public String getAmbFacilities() {
		return ambFacilities;
	}

	public void setAmbFacilities(String ambFacilities) {
		this.ambFacilities = ambFacilities;
	}
	
	public int getAmbulanceFacilitiesMasterId() {
		return ambulanceFacilitiesMasterId;
	}

	public void setAmbulanceFacilitiesMasterId(int ambulanceFacilitiesMasterId) {
		this.ambulanceFacilitiesMasterId = ambulanceFacilitiesMasterId;
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
