package com.param.global.dto.global;

public class TranslatedFrequencyMasterDto {
    private Integer translatedFrequencyId;

    private Integer frequencyId;

    private Integer languageId;

    private String desc;

    private String convertedText;

    private Character status;

    private Integer createdBy;

    private String createdDate;

    private Integer updatedBy;

    private String updatedDate;

    private Integer organizationId;

    private Integer unitId;

    public Integer getTranslatedFrequencyId() {
        return translatedFrequencyId;
    }

    public void setTranslatedFrequencyId(Integer translatedFrequencyId) {
        this.translatedFrequencyId = translatedFrequencyId;
    }

    public Integer getFrequencyId() {
        return frequencyId;
    }

    public void setFrequencyId(Integer frequencyId) {
        this.frequencyId = frequencyId;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getConvertedText() {
        return convertedText;
    }

    public void setConvertedText(String convertedText) {
        this.convertedText = convertedText;
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
}
