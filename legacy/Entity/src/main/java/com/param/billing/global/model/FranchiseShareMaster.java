package com.param.billing.global.model;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "m_franchise_share" , schema = "billing")
@SequenceGenerator(name = "m_franchise_share_seq" , sequenceName = "billing.m_franchise_share_seq" , allocationSize = 1)
public class FranchiseShareMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "m_franchise_share_seq")
    @Column(name = "share_id")
    private int shareId;

    @Column(name = "franchise_unit_id")
    private Integer franchiseUnitId;

    @Column(name = "speciality_id")
    private Integer specialityId;

    @Column(name = "sub_speciality_id")
    private Integer subSpecialityId;

    @Column(name = "percentage")
    private Double percentage;

    @Column(name="code")
    private String code;

    @Column(name="description")
    private String description;

    @Column(name="markup_status")
    private Boolean markupStatus;

    @Column(name = "organization_id")
    private Integer organizationId;

    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name="created_date")
    private Date createdDate;

    @Column(name="updated_date")
    private Date updatedDate;

    @Column(name="created_by")
    private Integer createdBy;

    @Column(name="updated_by")
    private Integer updatedBy;

    @Column(name="status")
    private String status;

    @Column(name = "service_id")
    private Integer serviceId;

    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "asset_category_id")
    private Integer assetCategoryId;

    @Column(name = "product_category_id")
    private Integer productCategoryId;

    public int getShareId() {
        return shareId;
    }

    public void setShareId(int shareId) {
        this.shareId = shareId;
    }

    public Integer getFranchiseUnitId() {
        return franchiseUnitId;
    }

    public void setFranchiseUnitId(Integer franchiseUnitId) {
        this.franchiseUnitId = franchiseUnitId;
    }

    public Integer getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(Integer specialityId) {
        this.specialityId = specialityId;
    }

    public Integer getSubSpecialityId() {
        return subSpecialityId;
    }

    public void setSubSpecialityId(Integer subSpecialityId) {
        this.subSpecialityId = subSpecialityId;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getMarkupStatus() {
        return markupStatus;
    }

    public void setMarkupStatus(Boolean markupStatus) {
        this.markupStatus = markupStatus;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getAssetCategoryId() {
        return assetCategoryId;
    }

    public void setAssetCategoryId(Integer assetCategoryId) {
        this.assetCategoryId = assetCategoryId;
    }

    public Integer getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Integer productCategoryId) {
        this.productCategoryId = productCategoryId;
    }
}
