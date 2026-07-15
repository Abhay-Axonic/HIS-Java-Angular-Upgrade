package com.param.global.model;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "m_unit_subscription_config")
public class UnitSubscriptionConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unit_subscription_config_seq")
    @SequenceGenerator(name = "unit_subscription_config_seq", sequenceName = "public.m_unit_subscription_config_seq", allocationSize = 1)
    @Column(name = "subscription_config_id")
    private Integer subscriptionConfigId;

    @Column(name = "unit_id", nullable = false)
    private Integer unitId;

    @Column(name = "organization_id", nullable = false)
    private Integer organizationId;

    @Column(name = "subscription_amount")
    private Double subscriptionAmount;

    @Column(name = "subscription_discount")
    private Double subscriptionDiscount;

    @Column(name = "subscription_net_amount")
    private Double subscriptionNetAmount;

    @Column(name = "subscription_tax")
    private Double subscriptionTax;

    @Column(name = "status")
    private Character status;

    @Column(name = "subscription_type", nullable = false)
    private String subscriptionType;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;

    public Integer getSubscriptionConfigId() {
        return subscriptionConfigId;
    }

    public void setSubscriptionConfigId(Integer subscriptionConfigId) {
        this.subscriptionConfigId = subscriptionConfigId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Double getSubscriptionAmount() {
        return subscriptionAmount;
    }

    public void setSubscriptionAmount(Double subscriptionAmount) {
        this.subscriptionAmount = subscriptionAmount;
    }

    public Double getSubscriptionDiscount() {
        return subscriptionDiscount;
    }

    public void setSubscriptionDiscount(Double subscriptionDiscount) {
        this.subscriptionDiscount = subscriptionDiscount;
    }

    public Double getSubscriptionNetAmount() {
        return subscriptionNetAmount;
    }

    public void setSubscriptionNetAmount(Double subscriptionNetAmount) {
        this.subscriptionNetAmount = subscriptionNetAmount;
    }

    public Double getSubscriptionTax() {
        return subscriptionTax;
    }

    public void setSubscriptionTax(Double subscriptionTax) {
        this.subscriptionTax = subscriptionTax;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
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
}