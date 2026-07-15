package com.param.opd.coversheet.dto;

import java.util.List;

public class StPatientAllergyDto {


	private Integer globalUhid;
	
	private String allergyType;
	
	private String stAllergyPatDesc;
	
	private String stAllergy;
	
	private String createdDate;
	private String localUhid;
	private Integer hospitalId;
	private String hospitalName;
	private Integer pkStPatientAllergyId;
	
	private List<StPatientAllergyDto> listStPatientAllergyDto;
	
	private Integer id;
	private String desc;
	private String type;
	private String uuid;
	private Integer stAllergyId;
	
	private List<StPatientAllergyDto> listStFoodAllergies;
	private List<StPatientAllergyDto> listStDrugAllergies;
	private List<StPatientAllergyDto> listStEnviornmentalAllergies;
	private Integer applicationId;
	private Integer patientId;

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public Integer getPkStPatientAllergyId() {
		return pkStPatientAllergyId;
	}

	public void setPkStPatientAllergyId(Integer pkStPatientAllergyId) {
		this.pkStPatientAllergyId = pkStPatientAllergyId;
	}

	public String getLocalUhid() {
		return localUhid;
	}

	public void setLocalUhid(String localUhid) {
		this.localUhid = localUhid;
	}

	public Integer getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Integer getGlobalUhid() {
		return globalUhid;
	}

	public void setGlobalUhid(Integer globalUhid) {
		this.globalUhid = globalUhid;
	}

	public String getAllergyType() {
		return allergyType;
	}

	public void setAllergyType(String allergyType) {
		this.allergyType = allergyType;
	}

	public String getStAllergyPatDesc() {
		return stAllergyPatDesc;
	}

	public void setStAllergyPatDesc(String stAllergyPatDesc) {
		this.stAllergyPatDesc = stAllergyPatDesc;
	}

	public String getStAllergy() {
		return stAllergy;
	}

	public void setStAllergy(String stAllergy) {
		this.stAllergy = stAllergy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public List<StPatientAllergyDto> getListStPatientAllergyDto() {
		return listStPatientAllergyDto;
	}

	public void setListStPatientAllergyDto(List<StPatientAllergyDto> listStPatientAllergyDto) {
		this.listStPatientAllergyDto = listStPatientAllergyDto;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getStAllergyId() {
		return stAllergyId;
	}

	public void setStAllergyId(Integer stAllergyId) {
		this.stAllergyId = stAllergyId;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public List<StPatientAllergyDto> getListStFoodAllergies() {
		return listStFoodAllergies;
	}

	public void setListStFoodAllergies(List<StPatientAllergyDto> listStFoodAllergies) {
		this.listStFoodAllergies = listStFoodAllergies;
	}

	public List<StPatientAllergyDto> getListStDrugAllergies() {
		return listStDrugAllergies;
	}

	public void setListStDrugAllergies(List<StPatientAllergyDto> listStDrugAllergies) {
		this.listStDrugAllergies = listStDrugAllergies;
	}

	public List<StPatientAllergyDto> getListStEnviornmentalAllergies() {
		return listStEnviornmentalAllergies;
	}

	public void setListStEnviornmentalAllergies(List<StPatientAllergyDto> listStEnviornmentalAllergies) {
		this.listStEnviornmentalAllergies = listStEnviornmentalAllergies;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	
}
