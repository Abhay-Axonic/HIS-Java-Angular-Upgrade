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

		@NamedQuery(name = "GET_RESOURCE_ALLOCATION_LIST", query = "SELECT rscAllMaster.ambulanceType as ambulanceType, rscAllMaster.resourceAllocationMasterId as resourceAllocationMasterId,"
				+ "rscAllMaster.ambulanceProvider as ambulanceProvider, rscAllMaster.vehicalNo as vehicalNo, rscAllMaster.resourceName as resourceName,rscAllMaster.mobileNo as mobileNo,"
				+ "rscAllMaster.status as status "
				+ "FROM ResourceAllocation as rscAllMaster " + "WHERE rscAllMaster.organizationId=:organizationId "
				+ "ORDER BY rscAllMaster.ambulanceType DESC"),

		@NamedQuery(name = "GET_RESOURCE_ALLOCATION_LIST_BY_NAME", query = "SELECT rscAllMaster.ambulanceType as ambulanceType, rscAllMaster.resourceAllocationMasterId as resourceAllocationMasterId,"
				+ "rscAllMaster.ambulanceProvider as ambulanceProvider, rscAllMaster.vehicalNo as vehicalNo, rscAllMaster.resourceName as resourceName,rscAllMaster.mobileNo as mobileNo,"
				+ "rscAllMaster.status as status "
				+ "FROM ResourceAllocation as rscAllMaster "
				+ "WHERE (LOWER(rscAllMaster.ambulanceType)=:ambulanceType OR rscAllMaster.ambulanceType=:ambulanceType) "
				+ "AND rscAllMaster.organizationId=:organizationId "),
		
		@NamedQuery(name = "GET_RESOURCE_ALLOCATION_LIST_BY_ID", query ="SELECT rscAllMaster.ambulanceType as ambulanceType, rscAllMaster.resourceAllocationMasterId as resourceAllocationMasterId,"
				+ "rscAllMaster.ambulanceProvider as ambulanceProvider, rscAllMaster.vehicalNo as vehicalNo, rscAllMaster.resourceName as resourceName,rscAllMaster.mobileNo as mobileNo,"
				+ "rscAllMaster.status as status "
				+ "FROM ResourceAllocation as rscAllMaster WHERE rscAllMaster.resourceAllocationMasterId=:resourceAllocationMasterId "
				+ "and rscAllMaster.organizationId=:organizationId"),

		@NamedQuery(name = "GET_ACTIVE_RESOURCE_ALLOCATION_LIST", query = "SELECT rscAllMaster.ambulanceType as ambulanceType, rscAllMaster.resourceAllocationMasterId as resourceAllocationMasterId,"
				+ "rscAllMaster.ambulanceProvider as ambulanceProvider, rscAllMaster.vehicalNo as vehicalNo, rscAllMaster.resourceName as resourceName,rscAllMaster.mobileNo as mobileNo,"
				+ "rscAllMaster.status as status "
				+ "FROM ResourceAllocation as rscAllMaster "
				+ "WHERE rscAllMaster.status='A' "
				+ "AND rscAllMaster.organizationId=:organizationId "
				+ "ORDER BY rscAllMaster.ambulanceType "),
		
		@NamedQuery(name = "GET_RESOURCE_ALLOCATION_SINGLE_REC", query = "SELECT rscAllMaster.ambulanceType as ambulanceType, rscAllMaster.resourceAllocationMasterId as resourceAllocationMasterId,"
				+ "rscAllMaster.ambulanceProvider as ambulanceProvider, rscAllMaster.vehicalNo as vehicalNo, rscAllMaster.resourceName as resourceName,rscAllMaster.mobileNo as mobileNo,"
				+ "rscAllMaster.status as status "
				+ "FROM ResourceAllocation as rscAllMaster " + "WHERE rscAllMaster.organizationId=:organizationId "
				+ " AND rscAllMaster.resourceAllocationMasterId=:resourceAllocationMasterId"),
				
})

@Entity
@Table(name = "m_resource_allocation", schema = "adt")
@SequenceGenerator(name = "m_resource_allocation_seq", sequenceName = "adt.m_resource_allocation_seq", allocationSize = 1)
public class ResourceAllocation {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_resource_allocation_seq")
	@Column(name = "resource_allocation_master_id")
	private int resourceAllocationMasterId;

	@Column(name = "ambulance_type")
	private String ambulanceType;

	@Column(name = "ambulance_provider")
	private String ambulanceProvider;
	
	@Column(name = "vehical_no")
	private String vehicalNo;
	
	@Column(name = "resource_name")
	private String resourceName;
	
	@Column(name = "mobile_no")
	private String mobileNo;
	
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
	
	public int getResourceAllocationMasterId() {
		return resourceAllocationMasterId;
	}

	public void setResourceAllocationMasterId(int resourceAllocationMasterId) {
		this.resourceAllocationMasterId = resourceAllocationMasterId;
	}

	public String getAmbulanceProvider() {
		return ambulanceProvider;
	}

	public void setAmbulanceProvider(String ambulanceProvider) {
		this.ambulanceProvider = ambulanceProvider;
	}

	public String getVehicalNo() {
		return vehicalNo;
	}

	public void setVehicalNo(String vehicalNo) {
		this.vehicalNo = vehicalNo;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
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
