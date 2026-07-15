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

		@NamedQuery(name = "GET_SERVICE_PROVIDER_LIST", query = "SELECT ServiceProvMaster.serviceProviderName as serviceProviderName, ServiceProvMaster.serviceProviderMasterId as serviceProviderMasterId,"
				+ "ServiceProvMaster.serviceProviderAddress as serviceProviderAddress, ServiceProvMaster.serviceProviderCountry as serviceProviderCountry,"
				+ "ServiceProvMaster.serviceProviderState as serviceProviderState, ServiceProvMaster.serviceProviderDistrict as serviceProviderDistrict,"
				+ "ServiceProvMaster.serviceProviderCity as serviceProviderCity, ServiceProvMaster.serviceProviderArea as serviceProviderArea,"
				+ "ServiceProvMaster.serviceProviderZipCode as serviceProviderZipCode, ServiceProvMaster.serviceProviderAmbulanceType as serviceProviderAmbulanceType,"
				+ "ServiceProvMaster.serviceProviderPersonName as serviceProviderPersonName, ServiceProvMaster.serviceProviderPhoneNo as serviceProviderPhoneNo,"
				+ "ServiceProvMaster.serviceProviderCellNo as serviceProviderCellNo, ServiceProvMaster.serviceProviderEmailAddress as serviceProviderEmailAddress,"
				+ "ServiceProvMaster.serviceProviderContractFromDate as serviceProviderContractFromDate, ServiceProvMaster.serviceProviderContractToDate as serviceProviderContractToDate,"
				+ "ServiceProvMaster.serviceProviderFirstDistance as serviceProviderFirstDistance, ServiceProvMaster.serviceProviderFirstDistanceRate as serviceProviderFirstDistanceRate,"
				+ "ServiceProvMaster.serviceProviderAdditionalMileage as serviceProviderAdditionalMileage, ServiceProvMaster.serviceProviderAdditionalMileageRate as serviceProviderAdditionalMileageRate,"
				+ "ServiceProvMaster.status as status "
				+ "FROM ServiceProvider as ServiceProvMaster " + "WHERE ServiceProvMaster.organizationId=:organizationId "
				+ "ORDER BY ServiceProvMaster.serviceProviderName DESC"),

		@NamedQuery(name = "GET_SERVICE_PROVIDER_LIST_BY_NAME", query =  "SELECT ServiceProvMaster.serviceProviderName as serviceProviderName, ServiceProvMaster.serviceProviderMasterId as serviceProviderMasterId,"
				+ "ServiceProvMaster.serviceProviderAddress as serviceProviderAddress, ServiceProvMaster.serviceProviderCountry as serviceProviderCountry,"
				+ "ServiceProvMaster.serviceProviderState as serviceProviderState, ServiceProvMaster.serviceProviderDistrict as serviceProviderDistrict,"
				+ "ServiceProvMaster.serviceProviderCity as serviceProviderCity, ServiceProvMaster.serviceProviderArea as serviceProviderArea,"
				+ "ServiceProvMaster.serviceProviderZipCode as serviceProviderZipCode, ServiceProvMaster.serviceProviderAmbulanceType as serviceProviderAmbulanceType,"
				+ "ServiceProvMaster.serviceProviderPersonName as serviceProviderPersonName, ServiceProvMaster.serviceProviderPhoneNo as serviceProviderPhoneNo,"
				+ "ServiceProvMaster.serviceProviderCellNo as serviceProviderCellNo, ServiceProvMaster.serviceProviderEmailAddress as serviceProviderEmailAddress,"
				+ "ServiceProvMaster.serviceProviderContractFromDate as serviceProviderContractFromDate, ServiceProvMaster.serviceProviderContractToDate as serviceProviderContractToDate,"
				+ "ServiceProvMaster.serviceProviderFirstDistance as serviceProviderFirstDistance, ServiceProvMaster.serviceProviderFirstDistanceRate as serviceProviderFirstDistanceRate,"
				+ "ServiceProvMaster.serviceProviderAdditionalMileage as serviceProviderAdditionalMileage, ServiceProvMaster.serviceProviderAdditionalMileageRate as serviceProviderAdditionalMileageRate,"
				+ "ServiceProvMaster.status as status "
				+ "FROM ServiceProvider as ServiceProvMaster "
				+ "WHERE (LOWER(ServiceProvMaster.serviceProviderName)=:serviceProviderName OR ServiceProvMaster.serviceProviderName=:serviceProviderName) "
				+ "AND ServiceProvMaster.organizationId=:organizationId "),
		
		@NamedQuery(name = "GET_SERVICE_PROVIDER_LIST_BY_ID", query = "SELECT ServiceProvMaster.serviceProviderName as serviceProviderName, ServiceProvMaster.serviceProviderMasterId as serviceProviderMasterId,"
				+ "ServiceProvMaster.serviceProviderAddress as serviceProviderAddress, ServiceProvMaster.serviceProviderCountry as serviceProviderCountry,"
				+ "ServiceProvMaster.serviceProviderState as serviceProviderState, ServiceProvMaster.serviceProviderDistrict as serviceProviderDistrict,"
				+ "ServiceProvMaster.serviceProviderCity as serviceProviderCity, ServiceProvMaster.serviceProviderArea as serviceProviderArea,"
				+ "ServiceProvMaster.serviceProviderZipCode as serviceProviderZipCode, ServiceProvMaster.serviceProviderAmbulanceType as serviceProviderAmbulanceType,"
				+ "ServiceProvMaster.serviceProviderPersonName as serviceProviderPersonName, ServiceProvMaster.serviceProviderPhoneNo as serviceProviderPhoneNo,"
				+ "ServiceProvMaster.serviceProviderCellNo as serviceProviderCellNo, ServiceProvMaster.serviceProviderEmailAddress as serviceProviderEmailAddress,"
				+ "ServiceProvMaster.serviceProviderContractFromDate as serviceProviderContractFromDate, ServiceProvMaster.serviceProviderContractToDate as serviceProviderContractToDate,"
				+ "ServiceProvMaster.serviceProviderFirstDistance as serviceProviderFirstDistance, ServiceProvMaster.serviceProviderFirstDistanceRate as serviceProviderFirstDistanceRate,"
				+ "ServiceProvMaster.serviceProviderAdditionalMileage as serviceProviderAdditionalMileage, ServiceProvMaster.serviceProviderAdditionalMileageRate as serviceProviderAdditionalMileageRate,"
				+ "ServiceProvMaster.status as status "
				+ "FROM ServiceProvider as ServiceProvMaster WHERE ServiceProvMaster.serviceProviderMasterId=:serviceProviderMasterId "
				+ "and ServiceProvMaster.organizationId=:organizationId"),

		@NamedQuery(name = "GET_ACTIVE_SERVICE_PROVIDER_LIST", query = "SELECT ServiceProvMaster.serviceProviderName as serviceProviderName, ServiceProvMaster.serviceProviderMasterId as serviceProviderMasterId,"
				+ "ServiceProvMaster.serviceProviderAddress as serviceProviderAddress, ServiceProvMaster.serviceProviderCountry as serviceProviderCountry,"
				+ "ServiceProvMaster.serviceProviderState as serviceProviderState, ServiceProvMaster.serviceProviderDistrict as serviceProviderDistrict,"
				+ "ServiceProvMaster.serviceProviderCity as serviceProviderCity, ServiceProvMaster.serviceProviderArea as serviceProviderArea,"
				+ "ServiceProvMaster.serviceProviderZipCode as serviceProviderZipCode, ServiceProvMaster.serviceProviderAmbulanceType as serviceProviderAmbulanceType,"
				+ "ServiceProvMaster.serviceProviderPersonName as serviceProviderPersonName, ServiceProvMaster.serviceProviderPhoneNo as serviceProviderPhoneNo,"
				+ "ServiceProvMaster.serviceProviderCellNo as serviceProviderCellNo, ServiceProvMaster.serviceProviderEmailAddress as serviceProviderEmailAddress,"
				+ "ServiceProvMaster.serviceProviderContractFromDate as serviceProviderContractFromDate, ServiceProvMaster.serviceProviderContractToDate as serviceProviderContractToDate,"
				+ "ServiceProvMaster.serviceProviderFirstDistance as serviceProviderFirstDistance, ServiceProvMaster.serviceProviderFirstDistanceRate as serviceProviderFirstDistanceRate,"
				+ "ServiceProvMaster.serviceProviderAdditionalMileage as serviceProviderAdditionalMileage, ServiceProvMaster.serviceProviderAdditionalMileageRate as serviceProviderAdditionalMileageRate,"
				+ "ServiceProvMaster.status as status "
				+ "FROM ServiceProvider as ServiceProvMaster "
				+ "WHERE ServiceProvMaster.status='A' "
				+ "AND ServiceProvMaster.organizationId=:organizationId "
				+ "ORDER BY ServiceProvMaster.serviceProviderName ASC "),
		
		@NamedQuery(name = "GET_SERVICE_PROVIDER_SINGLE_REC", query =  "SELECT ServiceProvMaster.serviceProviderName as serviceProviderName, ServiceProvMaster.serviceProviderMasterId as serviceProviderMasterId,"
				+ "ServiceProvMaster.serviceProviderAddress as serviceProviderAddress, ServiceProvMaster.serviceProviderCountry as serviceProviderCountry,"
				+ "ServiceProvMaster.serviceProviderState as serviceProviderState, ServiceProvMaster.serviceProviderDistrict as serviceProviderDistrict,"
				+ "ServiceProvMaster.serviceProviderCity as serviceProviderCity, ServiceProvMaster.serviceProviderArea as serviceProviderArea,"
				+ "ServiceProvMaster.serviceProviderZipCode as serviceProviderZipCode, ServiceProvMaster.serviceProviderAmbulanceType as serviceProviderAmbulanceType,"
				+ "ServiceProvMaster.serviceProviderPersonName as serviceProviderPersonName, ServiceProvMaster.serviceProviderPhoneNo as serviceProviderPhoneNo,"
				+ "ServiceProvMaster.serviceProviderCellNo as serviceProviderCellNo, ServiceProvMaster.serviceProviderEmailAddress as serviceProviderEmailAddress,"
				+ "ServiceProvMaster.serviceProviderContractFromDate as serviceProviderContractFromDate, ServiceProvMaster.serviceProviderContractToDate as serviceProviderContractToDate,"
				+ "ServiceProvMaster.serviceProviderFirstDistance as serviceProviderFirstDistance, ServiceProvMaster.serviceProviderFirstDistanceRate as serviceProviderFirstDistanceRate,"
				+ "ServiceProvMaster.serviceProviderAdditionalMileage as serviceProviderAdditionalMileage, ServiceProvMaster.serviceProviderAdditionalMileageRate as serviceProviderAdditionalMileageRate,"
				+ "ServiceProvMaster.status as status "
				+ "FROM ServiceProvider as ServiceProvMaster " + "WHERE ServiceProvMaster.organizationId=:organizationId "
				+ " AND ServiceProvMaster.serviceProviderMasterId=:serviceProviderMasterId"),
				
})

@Entity
@Table(name = "m_service_provider", schema = "adt")
@SequenceGenerator(name = "m_service_provider_seq", sequenceName = "adt.m_service_provider_seq", allocationSize = 1)
public class ServiceProvider {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_service_provider_seq")
	@Column(name = "service_provider_master_id")
	private int serviceProviderMasterId;
	@Column(name = "service_provider_name")
	private String serviceProviderName;
	@Column(name = "service_provider_address")
	private String serviceProviderAddress;
	@Column(name = "service_provider_country")
	private String serviceProviderCountry;
	@Column(name = "service_provider_state")
	private String serviceProviderState;
	@Column(name = "service_provider_district")
	private String serviceProviderDistrict;
	@Column(name = "service_provider_city")
	private String serviceProviderCity;
	@Column(name = "service_provider_area")
	private String serviceProviderArea;
	@Column(name = "service_provider_zipcode")
	private String serviceProviderZipCode;
	@Column(name = "service_provider_ambulance_type")
	private String serviceProviderAmbulanceType;
	@Column(name = "service_provider_person_name")
	private String serviceProviderPersonName;
	@Column(name = "service_provider_phone_no")
	private String serviceProviderPhoneNo;
	@Column(name = "service_provider_cell_no")
	private String serviceProviderCellNo;
	@Column(name = "service_provider_email_address")
	private String serviceProviderEmailAddress;
	@Column(name = "service_provider_contract_from_date")
	private String serviceProviderContractFromDate;
	@Column(name = "service_provider_contract_to_date")
	private String 	serviceProviderContractToDate;
	@Column(name = "service_provider_first_distance")
	private String serviceProviderFirstDistance;
	@Column(name = "service_provider_first_distance_rate")
	private String serviceProviderFirstDistanceRate;
	@Column(name = "service_provider_additional_mileage")
	private String serviceProviderAdditionalMileage;
	@Column(name = "service_provider_additional_mileage_rate")
	private String serviceProviderAdditionalMileageRate;
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
	
	public int getServiceProviderMasterId() {
		return serviceProviderMasterId;
	}

	public void setServiceProviderMasterId(int serviceProviderMasterId) {
		this.serviceProviderMasterId = serviceProviderMasterId;
	}

	public String getServiceProviderName() {
		return serviceProviderName;
	}

	public void setServiceProviderName(String serviceProviderName) {
		this.serviceProviderName = serviceProviderName;
	}

	public String getServiceProviderAddress() {
		return serviceProviderAddress;
	}

	public void setServiceProviderAddress(String serviceProviderAddress) {
		this.serviceProviderAddress = serviceProviderAddress;
	}

	public String getServiceProviderCountry() {
		return serviceProviderCountry;
	}

	public void setServiceProviderCountry(String serviceProviderCountry) {
		this.serviceProviderCountry = serviceProviderCountry;
	}

	public String getServiceProviderState() {
		return serviceProviderState;
	}

	public void setServiceProviderState(String serviceProviderState) {
		this.serviceProviderState = serviceProviderState;
	}

	public String getServiceProviderDistrict() {
		return serviceProviderDistrict;
	}

	public void setServiceProviderDistrict(String serviceProviderDistrict) {
		this.serviceProviderDistrict = serviceProviderDistrict;
	}

	public String getServiceProviderCity() {
		return serviceProviderCity;
	}

	public void setServiceProviderCity(String serviceProviderCity) {
		this.serviceProviderCity = serviceProviderCity;
	}

	public String getServiceProviderArea() {
		return serviceProviderArea;
	}

	public void setServiceProviderArea(String serviceProviderArea) {
		this.serviceProviderArea = serviceProviderArea;
	}

	public String getServiceProviderZipCode() {
		return serviceProviderZipCode;
	}

	public void setServiceProviderZipCode(String serviceProviderZipCode) {
		this.serviceProviderZipCode = serviceProviderZipCode;
	}

	public String getServiceProviderAmbulanceType() {
		return serviceProviderAmbulanceType;
	}

	public void setServiceProviderAmbulanceType(String serviceProviderAmbulanceType) {
		this.serviceProviderAmbulanceType = serviceProviderAmbulanceType;
	}

	public String getServiceProviderPersonName() {
		return serviceProviderPersonName;
	}

	public void setServiceProviderPersonName(String serviceProviderPersonName) {
		this.serviceProviderPersonName = serviceProviderPersonName;
	}

	public String getServiceProviderPhoneNo() {
		return serviceProviderPhoneNo;
	}

	public void setServiceProviderPhoneNo(String serviceProviderPhoneNo) {
		this.serviceProviderPhoneNo = serviceProviderPhoneNo;
	}

	public String getServiceProviderCellNo() {
		return serviceProviderCellNo;
	}

	public void setServiceProviderCellNo(String serviceProviderCellNo) {
		this.serviceProviderCellNo = serviceProviderCellNo;
	}

	public String getServiceProviderEmailAddress() {
		return serviceProviderEmailAddress;
	}

	public void setServiceProviderEmailAddress(String serviceProviderEmailAddress) {
		this.serviceProviderEmailAddress = serviceProviderEmailAddress;
	}

	public String getServiceProviderContractFromDate() {
		return serviceProviderContractFromDate;
	}

	public void setServiceProviderContractFromDate(String serviceProviderContractFromDate) {
		this.serviceProviderContractFromDate = serviceProviderContractFromDate;
	}

	public String getServiceProviderContractToDate() {
		return serviceProviderContractToDate;
	}

	public void setServiceProviderContractToDate(String serviceProviderContractToDate) {
		this.serviceProviderContractToDate = serviceProviderContractToDate;
	}

	public String getServiceProviderFirstDistance() {
		return serviceProviderFirstDistance;
	}

	public void setServiceProviderFirstDistance(String serviceProviderFirstDistance) {
		this.serviceProviderFirstDistance = serviceProviderFirstDistance;
	}

	public String getServiceProviderFirstDistanceRate() {
		return serviceProviderFirstDistanceRate;
	}

	public void setServiceProviderFirstDistanceRate(String serviceProviderFirstDistanceRate) {
		this.serviceProviderFirstDistanceRate = serviceProviderFirstDistanceRate;
	}

	public String getServiceProviderAdditionalMileage() {
		return serviceProviderAdditionalMileage;
	}

	public void setServiceProviderAdditionalMileage(String serviceProviderAdditionalMileage) {
		this.serviceProviderAdditionalMileage = serviceProviderAdditionalMileage;
	}

	public String getServiceProviderAdditionalMileageRate() {
		return serviceProviderAdditionalMileageRate;
	}

	public void setServiceProviderAdditionalMileageRate(String serviceProviderAdditionalMileageRate) {
		this.serviceProviderAdditionalMileageRate = serviceProviderAdditionalMileageRate;
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
