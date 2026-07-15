package com.param.pharmacy.master.unit.dto.mainStore;

import com.param.service.global.model.CookieObjectDto;

public class MainStoreResponseNew  extends CookieObjectDto{
    protected Integer id;

    protected Integer storeTypeId;

    protected String storeType;

    protected String storeName;

    protected String storeCode;

    protected Boolean status;

    protected Integer[] visitTypeId;

    // 4 new field added by sharad
    private Boolean isLinen;
    private Boolean isPharmacy ;
    private Boolean isNarcotic ;
    private Boolean isCSSD;
    // 1 new field added by sharad
    private Boolean isLaundry;
    private Boolean isMaintenance;


    public Boolean getIsMaintenance() {
		return isMaintenance;
	}

	public void setIsMaintenance(Boolean isMaintenance) {
		this.isMaintenance = isMaintenance;
	}

	public Boolean getIsLaundry() {
        return isLaundry;
    }

    public void setIsLaundry(Boolean isLaundry) {
        this.isLaundry = isLaundry;
    }

    public Boolean getIsLinen() {
        return isLinen;
    }

    public void setIsLinen(Boolean isLinen) {
        this.isLinen = isLinen;
    }

    public Boolean getIsPharmacy() {
        return isPharmacy;
    }

    public void setIsPharmacy(Boolean isPharmacy) {
        this.isPharmacy = isPharmacy;
    }

    public Boolean getIsNarcotic() {
        return isNarcotic;
    }

    public void setIsNarcotic(Boolean isNarcotic) {
        this.isNarcotic = isNarcotic;
    }

    public Boolean getIsCSSD() {
        return isCSSD;
    }

    public void setIsCSSD(Boolean isCSSD) {
        this.isCSSD = isCSSD;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStoreTypeId() {
        return storeTypeId;
    }

    public void setStoreTypeId(Integer storeTypeId) {
        this.storeTypeId = storeTypeId;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer[] getVisitTypeId() {
        return visitTypeId;
    }

    public void setVisitTypeId(Integer[] visitTypeId) {
        this.visitTypeId = visitTypeId;
    }


}
