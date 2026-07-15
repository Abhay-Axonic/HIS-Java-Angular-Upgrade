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


		/*@NamedQuery(name = "GET_AMBULANCE_MASTER_DATA_LIST", query = "SELECT ambMaster.ambulanceMasterId as ambulanceMasterId,"
				+ " ambMaster.ambulanceTypeId as ambulanceTypeId, "
				+ " ambMaster.serviceProvider as serviceProvider, ambMaster.ambulanceVehicalNo as ambulanceVehicalNo, "
				+ " ambMaster.availableFacilities as availableFacilities, ambMaster.inhouse as inhouse, ambMaster.status as status "
				+ " FROM AmbulanceMaster as ambMaster"
				+ " where ambMaster.organizationId=:organizationId"
				+ " ORDER BY ambMaster.ambulanceMasterId DESC"),*/
		
		
	@NamedQuery(name = "GET_AMBULANCE_MASTER_DATA_LIST", query = "SELECT ambMaster.ambulanceMasterId as ambulanceMasterId,"
			+ " ambMaster.ambulanceType as ambulanceType, "
			+ " ambMaster.serviceProvider as serviceProvider, ambMaster.ambulanceVehicalNo as ambulanceVehicalNo, "
			+ " ambMaster.availableFacilities as availableFacilities, ambMaster.inhouse as inhouse, ambMaster.status as status "
			+ " FROM AmbulanceMaster as ambMaster "
			+ " where ambMaster.organizationId=:organizationId"
			+ " ORDER BY ambMaster.ambulanceMasterId DESC"),
		
		
		
		
		

		/*@NamedQuery(name = "GET_AMBULANCE_MASTER_LIST_BY_NAME", query = "SELECT ambMaster.ambulanceMasterId as ambulanceMasterId,"
				+ " ambMaster.ambulanceType as ambulanceType, "
				+ " ambMaster.serviceProvider as serviceProvider, ambMaster.ambulanceVehicalNo as ambulanceVehicalNo, "
				+ " ambMaster.availableFacilities as availableFacilities, ambMaster.inhouse as inhouse, ambMaster.status as status "
				+ "FROM AmbulanceMaster as ambMaster "
				+ "WHERE (LOWER(ambMaster.ambulanceType)=:ambulanceType OR ambMaster.ambulanceType=:ambulanceType) "
				+ "AND ambMaster.ambulanceType=:ambulanceType AND ambMaster.organizationId=:organizationId "),*/
		
		/*@NamedQuery(name = "GET_AMBULANCE_MASTER_LIST_BY_ID", query = "SELECT ambMaster.ambulanceMasterId as ambulanceMasterId,"
				+ " ambMaster.ambulanceType as ambulanceType, "
				+ " ambMaster.serviceProvider as serviceProvider, ambMaster.ambulanceVehicalNo as ambulanceVehicalNo, "
				+ " ambMaster.availableFacilities as availableFacilities, ambMaster.inhouse as inhouse, ambMaster.status as status "
				+ "FROM AmbulanceMaster as ambMaster WHERE ambMaster.ambulanceTypeMasterId=:ambulanceTypeMasterId "
				+ "and ambMaster.organizationId=:organizationId"),*/

		@NamedQuery(name = "GET_ACTIVE_AMBULANCE_MASTER_LIST", query = "SELECT ambMaster.ambulanceMasterId as ambulanceMasterId,"
				+ " ambMaster.ambulanceType as ambulanceType, "
				+ " ambMaster.serviceProvider as serviceProvider, ambMaster.ambulanceVehicalNo as ambulanceVehicalNo, "
				+ " ambMaster.availableFacilities as availableFacilities, ambMaster.inhouse as inhouse, ambMaster.status as status "
				+ "FROM AmbulanceMaster as ambMaster "
				+ "WHERE ambMaster.status='A' "
				+ "AND ambMaster.organizationId=:organizationId "
				+ "ORDER BY ambMaster.ambulanceMasterId "),
		
		@NamedQuery(name = "GET_AMBULANCE_MASTER_SINGLE_REC", query = "SELECT ambMaster.ambulanceMasterId as ambulanceMasterId,"
				+ " ambMaster.ambulanceType as ambulanceType, "
				+ " ambMaster.serviceProvider as serviceProvider, ambMaster.ambulanceVehicalNo as ambulanceVehicalNo, "
				+ " ambMaster.availableFacilities as availableFacilities, ambMaster.inhouse as inhouse, ambMaster.status as status "
				+ "FROM AmbulanceMaster as ambMaster " + "WHERE ambMaster.organizationId=:organizationId "
				+ " AND ambMaster.ambulanceMasterId=:ambulanceMasterId"),
		
		@NamedQuery(name = "GET_AMBULANCE_MASTER_LIST_BY_NAME", query = "SELECT ambMaster.ambulanceMasterId as ambulanceMasterId,"
				+ " ambMaster.ambulanceType as ambulanceType, "
				+ " ambMaster.serviceProvider as serviceProvider, ambMaster.ambulanceVehicalNo as ambulanceVehicalNo, "
				+ " ambMaster.availableFacilities as availableFacilities, ambMaster.inhouse as inhouse, ambMaster.status as status "
				+ "FROM AmbulanceMaster as ambMaster " + "WHERE ambMaster.organizationId=:organizationId "
				+ " and (LOWER(ambMaster.ambulanceVehicalNo)=:ambulanceNo OR ambMaster.ambulanceVehicalNo=:ambulanceNo) "),
				
})
	

@Entity
@Table(name = "m_ambulance_master", schema = "adt")
@SequenceGenerator(name = "m_ambulance_master_seq", sequenceName = "adt.m_ambulance_master_seq", allocationSize = 1)
public class AmbulanceMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_ambulance_master_seq")
	@Column(name = "ambulance_master_id")
	private int ambulanceMasterId;

	@Column(name = "ambulance_type")
	private String ambulanceType;

	@Column(name = "service_provider")
	private String serviceProvider;
	
	@Column(name = "ambulance_vehical_no")
	private String ambulanceVehicalNo;
	
	@Column(name = "available_facilities")
	private String availableFacilities;
	
	@Column(name = "inhouse")
	private char inhouse;
	
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
	
	
	public String getAmbulanceType() {
		return ambulanceType;
	}

	public void setAmbulanceType(String ambulanceType) {
		this.ambulanceType = ambulanceType;
	}

	public int getAmbulanceMasterId() {
		return ambulanceMasterId;
	}

	public void setAmbulanceMasterId(int ambulanceMasterId) {
		this.ambulanceMasterId = ambulanceMasterId;
	}

	public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public String getAmbulanceVehicalNo() {
		return ambulanceVehicalNo;
	}

	public void setAmbulanceVehicalNo(String ambulanceVehicalNo) {
		this.ambulanceVehicalNo = ambulanceVehicalNo;
	}

	public String getAvailableFacilities() {
		return availableFacilities;
	}

	public void setAvailableFacilities(String availableFacilities) {
		this.availableFacilities = availableFacilities;
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

	public char getInhouse() {
		return inhouse;
	}

	public void setInhouse(char inhouse) {
		this.inhouse = inhouse;
	}
	
	
}
