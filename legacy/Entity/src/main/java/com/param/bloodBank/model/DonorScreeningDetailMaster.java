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
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import javax.persistence.NamedQueries;

@NamedQueries({
	
	@NamedQuery(name="GET_DONOR_SCREENING_DETAIL_BY_DONOR_ID",
		query="SELECT donorScreeningDetailMaster.donorScreeningId as donorScreeningId,donorScreeningDetailMaster.donorId as donorId,"
				    + " donorScreeningDetailMaster.donorHeight as donorHeight,donorScreeningDetailMaster.donorWeight as donorWeight,"
				    + " donorScreeningDetailMaster.donorTemperature as donorTemperature,donorScreeningDetailMaster.donorPulse as donorPulse,"
				    + " donorScreeningDetailMaster.donorHB as donorHB,donorScreeningDetailMaster.donorBPSystolic as donorBPSystolic,"
				    + " donorScreeningDetailMaster.donorBpDiastolic as donorBpDiastolic,donorScreeningDetailMaster.hbDoneById as hbDoneById,"
				    + " donorScreeningDetailMaster.bleedingDoneById as bleedingDoneById,donorScreeningDetailMaster.screeningResultStatusId as screeningResultStatusId,"
				    + " to_char(donorScreeningDetailMaster.deferralDays,'mm/dd/yyyy') as deferralDays,donorScreeningDetailMaster.screeningRemarks as screeningRemarks,"
				    + " donorScreeningDetailMaster.status as status,donorScreeningDetailMaster.createdBy as createdBy,to_char(donorScreeningDetailMaster.createdDate,'dd/mm/yyyy') as createdDate,"
				    + " donorScreeningDetailMaster.updatedBy as updatedBy,to_char(donorScreeningDetailMaster.updatedDate,'dd/mm/yyyy') as updatedDate,donorScreeningDetailMaster.organizationId as organizationId,"
				    + " donorScreeningDetailMaster.unitId as unitId, "
				    + " donorScreeningDetailMaster.age as age, "
				    + " donorScreeningDetailMaster.temperatureType as temperatureType, "
				    + " donorScreeningDetailMaster.weightType as weightType, "
				    + " donorScreeningDetailMaster.hBType as hBType, "
				    + " bloodBankStatusMaster.name as screeningResultStatus "
					+ " FROM DonorScreeningDetailMaster donorScreeningDetailMaster "
					+ " INNER JOIN donorScreeningDetailMaster.bloodBankStatusMaster  bloodBankStatusMaster "
					+ " WHERE donorScreeningDetailMaster.donorId= :donorId"
					+ " AND donorScreeningDetailMaster.organizationId= :organizationId"
					+ " AND donorScreeningDetailMaster.unitId= :unitId"
					),
	
	@NamedQuery(name="SEARCH_DONOR_BY_MULIPLE_CRITERIA",
	query="SELECT donorScreeningDetailMaster.donorScreeningId as donorScreeningId,"
			    + " donorScreeningDetailMaster.donorId as donorId,"
			    + " donorScreeningDetailMaster.donorHeight as donorHeight,"
			    + " donorScreeningDetailMaster.donorWeight as donorWeight,"
			    + " donorScreeningDetailMaster.donorTemperature as donorTemperature,"
			    + " donorScreeningDetailMaster.donorPulse as donorPulse,"
			    + " donorScreeningDetailMaster.donorHB as donorHB,"
			    + " donorScreeningDetailMaster.donorBPSystolic as donorBPSystolic,"
			    + " donorScreeningDetailMaster.donorBpDiastolic as donorBpDiastolic,"
			    + " donorScreeningDetailMaster.hbDoneById as hbDoneById,"
			    + " donorScreeningDetailMaster.bleedingDoneById as bleedingDoneById,"
			    + " donorScreeningDetailMaster.screeningResultStatusId as screeningResultStatusId,"
			    + " to_char(donorScreeningDetailMaster.deferralDays,'mm/dd/yyyy') as deferralDays,"
			    + " donorScreeningDetailMaster.screeningRemarks as screeningRemarks,"
			    + " donorScreeningDetailMaster.status as status,"
			    + " donorScreeningDetailMaster.createdBy as createdBy,"
			    + " to_char(donorScreeningDetailMaster.createdDate,'dd/mm/yyyy') as createdDate,"
			    + " donorScreeningDetailMaster.updatedBy as updatedBy,"
			    + " to_char(donorScreeningDetailMaster.updatedDate,'dd/mm/yyyy') as updatedDate,"
			    + " donorScreeningDetailMaster.organizationId as organizationId,"
			    + " concat(coalesce(donorRegi.firstName,'') ,' ', coalesce(donorRegi.middleName,'') ,' ', coalesce(donorRegi.lastName,'')) as donorName, "
			    + " donorRegi.bloodGroupId as bloodGroupId, "
			    + " donorRegi.genderId as genderId, "
			    + " genMstr.desc as gender, "
			    + " donorRegi.identificationTypeId as identificationTypeId, "
			    + " coalesce(identification.identificationName,'') as identificationName, "
			    + " donorRegi.identificationNumber as identificationNumber, " 
			    + " donorRegi.isAutologous as isAutologous, "
			    + " bloodGroupMaster.bloodGroupDesc as bloodGroup, "
			    + " donorRegi.patientId as patientId, "
				+ " to_char(donorRegi.dob,'MM/DD/YYYY') as dob, "
			    + " donorRegi.mobileNumber as mobileNumber, "
			    + " bloodBankStatusMaster.name as screeningResultStatus, "
			    + " donorRegi.donorNumber as donorNumber, "
			    + " donorScreeningDetailMaster.unitId as unitId "
				+ " FROM DonorScreeningDetailMaster donorScreeningDetailMaster "
				+ " INNER JOIN donorScreeningDetailMaster.donorRegistrationMaster  donorRegi "
				+ " INNER JOIN donorRegi.genderMaster  genMstr "
				+ " LEFT JOIN donorRegi.identificationMaster identification "
				+ " INNER JOIN donorRegi.bloodGroupMaster  bloodGroupMaster "
				+ " INNER JOIN donorScreeningDetailMaster.bloodBankStatusMaster  bloodBankStatusMaster "
				+ " WHERE donorScreeningDetailMaster.organizationId= :organizationId "
				+ " AND donorScreeningDetailMaster.unitId= :unitId "
				),
	
		@NamedQuery(name="GET_ACCEPTED_DONOR_SCREENING_DETAILS",
		query="SELECT "
			    + " donorScreeningDetailMaster.donorId as donorId,"
			    + " donorScreeningDetailMaster.organizationId as organizationId,"
			    + " concat(coalesce(donorRegi.firstName,'') ,' ', coalesce(donorRegi.middleName,'') ,' ', coalesce(donorRegi.lastName,'')) as donorName, "
			    + " donorRegi.bloodGroupId as bloodGroupId, "
			    + " genMstr.desc as gender, "
			    + " bloodGroupMaster.bloodGroupDesc as bloodGroup, "
			    + " bloodBankStatusMaster.name as statusName, "
			    + " donorRegi.donorNumber as donorNumber, "
			    + " donorScreeningDetailMaster.unitId as unitId "
			    + " FROM DonorScreeningDetailMaster donorScreeningDetailMaster "
			    + " INNER JOIN donorScreeningDetailMaster.donorRegistrationMaster  donorRegi "
			    + " INNER JOIN donorRegi.genderMaster  genMstr "
			    + " INNER JOIN donorRegi.bloodGroupMaster  bloodGroupMaster "
			    + " INNER JOIN donorScreeningDetailMaster.bloodBankStatusMaster  bloodBankStatusMaster "
			    + " WHERE donorScreeningDetailMaster.organizationId= :organizationId "
			    + " AND donorScreeningDetailMaster.unitId= :unitId "
			    + " AND donorScreeningDetailMaster.screeningResultStatusId= 1 "
			    + " AND donorScreeningDetailMaster.exportedToBagCollection= false "
				)
		
	})

@Entity
@Table(name = "t_donor_screening_details", schema = "blood_bank")
@SequenceGenerator(name = "seq_t_donor_screening_details", sequenceName = "blood_bank.t_donor_screening_details_seq", allocationSize = 1)
public class DonorScreeningDetailMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_t_donor_screening_details")
	@Column(name = "donor_screening_id")
	private Integer donorScreeningId;
	
	@Column(name = "donor_id")
	private Integer donorId;
	
	@Column(name = "donor_height")
	private Float donorHeight;
	
	@Column(name = "donor_weight")
	private Float donorWeight;
	
	@Column(name = "donor_temperature")
	private Float donorTemperature;
	
	@Column(name = "donor_pulse")
	private Float donorPulse;
	
	@Column(name = "donor_hb")
	private Float donorHB;
	
	@Column(name = "donor_bp_systolic")
	private Float donorBPSystolic;
	
	@Column(name = "donor_bp_diastolic")
	private Float donorBpDiastolic;
	
	@Column(name = "screening_hb_done_id")
	private Integer hbDoneById;
	
	@Column(name = "bleeding_done_id")
	private Integer bleedingDoneById;
	
	@Column(name = "screening_result_status_id")
	private Integer screeningResultStatusId;
	
	@Column(name = "deferral_days")
	private Date deferralDays;
	
	@Column(name = "sreening_remarks")
	private String screeningRemarks;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "exported_to_bag_collection")
	private Boolean exportedToBagCollection;
	
	@Column(name = "age")
	private Integer age;
	
	@Column(name = "temperature_type")
	private String temperatureType;
	
	@Column(name = "weight_type")
	private String weightType;
	
	@Column(name = "hb_type")
	private String hBType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "donor_id", insertable = false, updatable = false)
	private DonorRegistrationMaster donorRegistrationMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "screening_result_status_id", insertable = false, updatable = false)
	private BloodBankStatusMaster bloodBankStatusMaster;

	
	public Integer getDonorScreeningId() {
		return donorScreeningId;
	}

	public void setDonorScreeningId(Integer donorScreeningId) {
		this.donorScreeningId = donorScreeningId;
	}

	public Integer getDonorId() {
		return donorId;
	}

	public void setDonorId(Integer donorId) {
		this.donorId = donorId;
	}

	public Float getDonorHeight() {
		return donorHeight;
	}

	public void setDonorHeight(Float donorHeight) {
		this.donorHeight = donorHeight;
	}

	public Float getDonorWeight() {
		return donorWeight;
	}

	public void setDonorWeight(Float donorWeight) {
		this.donorWeight = donorWeight;
	}

	public Float getDonorTemperature() {
		return donorTemperature;
	}

	public void setDonorTemperature(Float donorTemperature) {
		this.donorTemperature = donorTemperature;
	}

	public Float getDonorPulse() {
		return donorPulse;
	}

	public void setDonorPulse(Float donorPulse) {
		this.donorPulse = donorPulse;
	}

	public Float getDonorHB() {
		return donorHB;
	}

	public void setDonorHB(Float donorHB) {
		this.donorHB = donorHB;
	}

	public Float getDonorBPSystolic() {
		return donorBPSystolic;
	}

	public void setDonorBPSystolic(Float donorBPSystolic) {
		this.donorBPSystolic = donorBPSystolic;
	}

	public Float getDonorBpDiastolic() {
		return donorBpDiastolic;
	}

	public void setDonorBpDiastolic(Float donorBpDiastolic) {
		this.donorBpDiastolic = donorBpDiastolic;
	}
	
	public String getScreeningRemarks() {
		return screeningRemarks;
	}

	public void setScreeningRemarks(String screeningRemarks) {
		this.screeningRemarks = screeningRemarks;
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

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Date getDeferralDays() {
		return deferralDays;
	}

	public void setDeferralDays(Date deferralDays) {
		this.deferralDays = deferralDays;
	}

	public DonorRegistrationMaster getDonorRegistrationMaster() {
		return donorRegistrationMaster;
	}

	public void setDonorRegistrationMaster(DonorRegistrationMaster donorRegistrationMaster) {
		this.donorRegistrationMaster = donorRegistrationMaster;
	}

	public Integer getScreeningResultStatusId() {
		return screeningResultStatusId;
	}

	public void setScreeningResultStatusId(Integer screeningResultStatusId) {
		this.screeningResultStatusId = screeningResultStatusId;
	}

	public BloodBankStatusMaster getBloodBankStatusMaster() {
		return bloodBankStatusMaster;
	}

	public void setBloodBankStatusMaster(BloodBankStatusMaster bloodBankStatusMaster) {
		this.bloodBankStatusMaster = bloodBankStatusMaster;
	}

	public Boolean getExportedToBagCollection() {
		return exportedToBagCollection;
	}

	public void setExportedToBagCollection(Boolean exportedToBagCollection) {
		this.exportedToBagCollection = exportedToBagCollection;
	}

	public Integer getHbDoneById() {
		return hbDoneById;
	}

	public void setHbDoneById(Integer hbDoneById) {
		this.hbDoneById = hbDoneById;
	}

	public Integer getBleedingDoneById() {
		return bleedingDoneById;
	}

	public void setBleedingDoneById(Integer bleedingDoneById) {
		this.bleedingDoneById = bleedingDoneById;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getTemperatureType() {
		return temperatureType;
	}

	public void setTemperatureType(String temperatureType) {
		this.temperatureType = temperatureType;
	}

	public String getWeightType() {
		return weightType;
	}

	public void setWeightType(String weightType) {
		this.weightType = weightType;
	}

	public String gethBType() {
		return hBType;
	}

	public void sethBType(String hBType) {
		this.hBType = hBType;
	}
}
