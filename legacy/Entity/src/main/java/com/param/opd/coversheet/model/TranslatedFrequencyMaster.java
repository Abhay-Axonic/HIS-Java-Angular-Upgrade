package com.param.opd.coversheet.model;

import javax.persistence.*;
import java.util.Date;


@NamedQuery(name = "UPDATE_TRANSLATED_FREQUENCY",
        query = " UPDATE TranslatedFrequencyMaster translatedFrequencyMaster "
                + "  SET translatedFrequencyMaster.status = :status"
                + " WHERE translatedFrequencyMaster.organizationId= :organizationId "
                + " AND translatedFrequencyMaster.unitId = :unitId"
                + " AND translatedFrequencyMaster.frequencyId = :frequencyId")


@Entity
@Table(name = "t_translated_frequency", schema = "public")
@SequenceGenerator(name = "t_translated_frequency_seq", sequenceName = "public.t_translated_frequency_seq", allocationSize = 1)
public class TranslatedFrequencyMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_translated_frequency_seq")
    @Column(name = "translated_frequency_id")
    private Integer translatedFrequencyId;

    @Column(name = "frequency_id")
    private Integer frequencyId;

    @Column(name = "language_id")
    private Integer languageId;

    @Column(name = "frequency_desc")
    private String desc;

    @Column(name = "converted_text")
    private String convertedText;

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
}
