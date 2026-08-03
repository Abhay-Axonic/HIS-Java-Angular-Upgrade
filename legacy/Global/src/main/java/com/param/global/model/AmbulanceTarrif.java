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

		@NamedQuery(name = "GET_AMBULANCE_TARIFF_LIST", query = "SELECT ambTarrifMaster.ambulanceTarrifMasterId as ambulanceTarrifMasterId, ambTarrifMaster.ambulanceType as ambulanceType, "
				+ "ambTarrifMaster.ambulanceProvider as ambulanceProvider, ambTarrifMaster.ambulanceOutSrcVender as ambulanceOutSrcVender,"
				+ "ambTarrifMaster.ambulanceFirstDist as ambulanceFirstDist, ambTarrifMaster.ambulanceFirstDistRate as ambulanceFirstDistRate, "
				+ "ambTarrifMaster.ambulanceAddMileage as ambulanceAddMileage, ambTarrifMaster.ambulanceAddMileageRate as ambulanceAddMileageRate, "
				+ "ambTarrifMaster.status as status "
				+ "FROM AmbulanceTarrif as ambTarrifMaster WHERE ambTarrifMaster.organizationId=:organizationId "
				+ "ORDER BY ambTarrifMaster.ambulanceTarrifMasterId DESC"),

		@NamedQuery(name = "GET_AMBULANCE_TARRIF_LIST_BY_NAME", query = "SELECT ambTarrifMaster.ambulanceTarrifMasterId as ambulanceTarrifMasterId, ambTarrifMaster.ambulanceType as ambulanceType, "
				+ "ambTarrifMaster.ambulanceProvider as ambulanceProvider, ambTarrifMaster.ambulanceOutSrcVender as ambulanceOutSrcVender,"
				+ "ambTarrifMaster.ambulanceFirstDist as ambulanceFirstDist, ambTarrifMaster.ambulanceFirstDistRate as ambulanceFirstDistRate, "
				+ "ambTarrifMaster.ambulanceAddMileage as ambulanceAddMileage, ambTarrifMaster.ambulanceAddMileageRate as ambulanceAddMileageRate, "
				+ "ambTarrifMaster.status as status "
				+ "FROM AmbulanceTarrif as ambTarrifMaster "
				+ "WHERE (LOWER(ambTarrifMaster.ambulanceType)=:ambulanceType OR ambTarrifMaster.ambulanceType=:ambulanceType) "
				+ "AND ambTarrifMaster.organizationId=:organizationId and ambTarrifMaster.ambulanceTarrifMasterId not in(:tariffMasterId)"),
		
		@NamedQuery(name = "GET_AMBULANCE_TARRIF_LIST_BY_ID", query ="SELECT ambTarrifMaster.ambulanceTarrifMasterId as ambulanceTarrifMasterId, ambTarrifMaster.ambulanceType as ambulanceType, "
				+ "ambTarrifMaster.ambulanceProvider as ambulanceProvider, ambTarrifMaster.ambulanceOutSrcVender as ambulanceOutSrcVender,"
				+ "ambTarrifMaster.ambulanceFirstDist as ambulanceFirstDist, ambTarrifMaster.ambulanceFirstDistRate as ambulanceFirstDistRate, "
				+ "ambTarrifMaster.ambulanceAddMileage as ambulanceAddMileage, ambTarrifMaster.ambulanceAddMileageRate as ambulanceAddMileageRate, "
				+ "ambTarrifMaster.status as status "
				+ "FROM AmbulanceTarrif as ambTarrifMaster WHERE ambTarrifMaster.ambulanceTarrifMasterId=:ambulanceTarrifMasterId "
				+ "and ambTarrifMaster.organizationId=:organizationId"),

		@NamedQuery(name = "GET_ACTIVE_AMBULANCE_TARRIF_LIST", query = "SELECT ambTarrifMaster.ambulanceTarrifMasterId as ambulanceTarrifMasterId, ambTarrifMaster.ambulanceType as ambulanceType, "
				+ "ambTarrifMaster.ambulanceProvider as ambulanceProvider, ambTarrifMaster.ambulanceOutSrcVender as ambulanceOutSrcVender,"
				+ "ambTarrifMaster.ambulanceFirstDist as ambulanceFirstDist, ambTarrifMaster.ambulanceFirstDistRate as ambulanceFirstDistRate, "
				+ "ambTarrifMaster.ambulanceAddMileage as ambulanceAddMileage, ambTarrifMaster.ambulanceAddMileageRate as ambulanceAddMileageRate "
				+ "FROM AmbulanceTarrif as ambTarrifMaster "
				+ "WHERE ambTarrifMaster.status='A' "
				+ "AND ambTarrifMaster.organizationId=:organizationId "
				+ "ORDER BY ambTarrifMaster.ambulanceType "),
		
		@NamedQuery(name = "GET_AMBULANCE_TARRIF_SINGLE_REC", query ="SELECT ambTarrifMaster.ambulanceTarrifMasterId as ambulanceTarrifMasterId, ambTarrifMaster.ambulanceType as ambulanceType, "
				+ "ambTarrifMaster.ambulanceProvider as ambulanceProvider, ambTarrifMaster.ambulanceOutSrcVender as ambulanceOutSrcVender,"
				+ "ambTarrifMaster.ambulanceFirstDist as ambulanceFirstDist, ambTarrifMaster.ambulanceFirstDistRate as ambulanceFirstDistRate, "
				+ "ambTarrifMaster.ambulanceAddMileage as ambulanceAddMileage, ambTarrifMaster.ambulanceAddMileageRate as ambulanceAddMileageRate, "
				+ "ambTarrifMaster.status as status "
				+ "FROM AmbulanceTarrif as ambTarrifMaster " + "WHERE ambTarrifMaster.organizationId=:organizationId "
				+ " AND ambTarrifMaster.ambulanceTarrifMasterId=:ambulanceTarrifMasterId"),
				
})

@Entity
@Table(name = "m_ambulance_tarrif", schema = "adt")
@SequenceGenerator(name = "m_ambulance_tarrif_seq", sequenceName = "adt.m_ambulance_tarrif_seq", allocationSize = 1)
public class AmbulanceTarrif {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_ambulance_tarrif_seq")
	@Column(name = "ambulance_tarrif_master_id")
	private int ambulanceTarrifMasterId;

	public int getAmbulanceTarrifMasterId() {
		return ambulanceTarrifMasterId;
	}

	public void setAmbulanceTarrifMasterId(int ambulanceTarrifMasterId) {
		this.ambulanceTarrifMasterId = ambulanceTarrifMasterId;
	}
	
	@Column(name = "ambulance_type")
	private String ambulanceType;
	
	@Column(name = "ambulance_provider")
	private String ambulanceProvider;
	
	@Column(name = "ambulance_out_src_vender")
	private String ambulanceOutSrcVender;
	
	@Column(name = "ambulance_first_dist")
	private String ambulanceFirstDist;
	
	@Column(name = "ambulance_first_dist_rate")
	private String ambulanceFirstDistRate;
	
	@Column(name = "ambulance_add_mileage")
	private String ambulanceAddMileage;
	
	@Column(name = "ambulance_add_mileage_rate")
	private String ambulanceAddMileageRate;

	@Column(name = "status")
	private char status;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private String createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private String updatedDate;

	@Column(name="organization_id")
	private int organizationId;
	
	public String getAmbulanceProvider() {
		return ambulanceProvider;
	}

	public void setAmbulanceProvider(String ambulanceProvider) {
		this.ambulanceProvider = ambulanceProvider;
	}

	public String getAmbulanceOutSrcVender() {
		return ambulanceOutSrcVender;
	}

	public void setAmbulanceOutSrcVender(String ambulanceOutSrcVender) {
		this.ambulanceOutSrcVender = ambulanceOutSrcVender;
	}

	public String getAmbulanceFirstDist() {
		return ambulanceFirstDist;
	}

	public void setAmbulanceFirstDist(String ambulanceFirstDist) {
		this.ambulanceFirstDist = ambulanceFirstDist;
	}

	public String getAmbulanceFirstDistRate() {
		return ambulanceFirstDistRate;
	}

	public void setAmbulanceFirstDistRate(String ambulanceFirstDistRate) {
		this.ambulanceFirstDistRate = ambulanceFirstDistRate;
	}

	public String getAmbulanceAddMileage() {
		return ambulanceAddMileage;
	}

	public void setAmbulanceAddMileage(String ambulanceAddMileage) {
		this.ambulanceAddMileage = ambulanceAddMileage;
	}

	public String getAmbulanceAddMileageRate() {
		return ambulanceAddMileageRate;
	}

	public void setAmbulanceAddMileageRate(String ambulanceAddMileageRate) {
		this.ambulanceAddMileageRate = ambulanceAddMileageRate;
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

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
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
