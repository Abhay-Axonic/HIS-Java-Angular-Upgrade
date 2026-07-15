package com.param.service.global.model;

import java.io.Serializable;
public class CookieObjectDto implements Serializable {

    private Integer organizationId;

    private Integer unitId;

    private Integer userId;

    private String userName;

    private String loginName;

    private Integer doctorId;

    private Integer unitCountryId;

    private Integer unitStateId;

    private Integer unitDistrictId;

    private Integer unitCityId;

    private Integer unitAreaId;

    private Integer storeId;

    private String orgNme;

    private String unitNme;
    
    private String macAddress;
    
    private Integer consultationSpecialityId;
    
    private Integer counterId;
    
    public CookieObjectDto() {
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getUnitCountryId() {
        return unitCountryId;
    }

    public void setUnitCountryId(Integer unitCountryId) {
        this.unitCountryId = unitCountryId;
    }

    public Integer getUnitStateId() {
        return unitStateId;
    }

    public void setUnitStateId(Integer unitStateId) {
        this.unitStateId = unitStateId;
    }

    public Integer getUnitDistrictId() {
        return unitDistrictId;
    }

    public void setUnitDistrictId(Integer unitDistrictId) {
        this.unitDistrictId = unitDistrictId;
    }

    public Integer getUnitCityId() {
        return unitCityId;
    }

    public void setUnitCityId(Integer unitCityId) {
        this.unitCityId = unitCityId;
    }

    public Integer getUnitAreaId() {
        return unitAreaId;
    }

    public void setUnitAreaId(Integer unitAreaId) {
        this.unitAreaId = unitAreaId;
    }

    public String getOrgNme() {
        return orgNme;
    }

    public void setOrgNme(String orgNme) {
        this.orgNme = orgNme;
    }

    public String getUnitNme() {
        return unitNme;
    }

    public void setUnitNme(String unitNme) {
        this.unitNme = unitNme;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public Integer getConsultationSpecialityId() {
		return consultationSpecialityId;
	}

	public void setConsultationSpecialityId(Integer consultationSpecialityId) {
		this.consultationSpecialityId = consultationSpecialityId;
	}

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}
    
}