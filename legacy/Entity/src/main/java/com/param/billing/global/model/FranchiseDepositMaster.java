package com.param.billing.global.model;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
@NamedNativeQueries({
        @NamedNativeQuery(name = "GET_FRANCHISE_DEPOSIT_LIST", query = "select "
                + "	mfd.fr_deposit_no as \"frDepositNo\", "
                + "	mfd.fr_deposit_id as \"frDepositId\", "
                + "	to_char(mfd.created_date, 'DD-MM-YYYY HH24:MI:SS') as \"frDepositDate\", "
                + "	mfd.created_date as \"frDepositDateNew\", "
                + "	cast(coalesce(mfd.amount, 0) as numeric(10, 2)) as \"amount\", "
                + "	cast(coalesce(mfd.consumed_deposit, 0) as numeric(10, 2)) as \"consumedDeposit\" , "
                + "	cast(coalesce(mfd.available_deposit, 0) as numeric(10, 2)) as \"availableDeposit\", "
                + "	empmst.first_name || ' ' || empmst.last_name as \"createdBy\", "
                + "	mum.unit_code as \"unitCode\", "
                + "	mum.unit_name as \"unitName\" "
                + "from "
                + "	billing.m_franchise_deposit mfd "
                + "left join public.m_unit_master mum on "
                + "	mfd.franchise_unit_id = mum.unit_id "
                + "left join public.m_employee_master empmst on "
                + "	empmst.user_id = mfd.created_by "
                + "where "
                + "	mfd.status = 'A' "
                + "	and mfd.org_id =:orgId "
                + "	and mfd.unit_id =:unitId"),
})

@Entity
@Table(name = "m_franchise_deposit" , schema = "billing")
@SequenceGenerator(name = "m_franchise_deposit_seq" , sequenceName = "billing.m_franchise_deposit_seq" , allocationSize = 1)
public class FranchiseDepositMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "m_franchise_deposit_seq")
    @Column(name = "fr_deposit_id")
    private int frDepositId;

    @Column(name = "franchise_unit_id")
    private Integer franchiseUnitId;

    @Column(name = "fr_deposit_no")
    private String frDepositNo;

    @Column(name = "amount")
    private double amount;

    @Column(name = "available_deposit")
    private double availableDeposit;

    @Column(name = "consumed_deposit")
    private double consumedDeposit;

    @CreationTimestamp
    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "created_by")
    private Integer createdBy;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "org_id")
    private Integer orgId;

    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name = "status")
    private Character status;

    @Column(name = "received_from")
    private String receivedFrom;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "identification_no")
    private String identificationNo;

    @Column(name="counter_id")
    private Integer counterId;

    @Column(name = "signeture_image")
    private String signetureImage;

    @Column(name = "signature")
    private byte[] signature;


    public int getFrDepositId() {
        return frDepositId;
    }

    public void setFrDepositId(int frDepositId) {
        this.frDepositId = frDepositId;
    }

    public Integer getFranchiseUnitId() {
        return franchiseUnitId;
    }

    public void setFranchiseUnitId(Integer franchiseUnitId) {
        this.franchiseUnitId = franchiseUnitId;
    }

    public String getFrDepositNo() {
        return frDepositNo;
    }

    public void setFrDepositNo(String frDepositNo) {
        this.frDepositNo = frDepositNo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public double getAvailableDeposit() {
        return availableDeposit;
    }

    public void setAvailableDeposit(double availableDeposit) {
        this.availableDeposit = availableDeposit;
    }

    public double getConsumedDeposit() {
        return consumedDeposit;
    }

    public void setConsumedDeposit(double consumedDeposit) {
        this.consumedDeposit = consumedDeposit;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }


    public String getReceivedFrom() {
        return receivedFrom;
    }

    public void setReceivedFrom(String receivedFrom) {
        this.receivedFrom = receivedFrom;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getCounterId() {
        return counterId;
    }

    public void setCounterId(Integer counterId) {
        this.counterId = counterId;
    }

    public String getSignetureImage() {
        return signetureImage;
    }

    public void setSignetureImage(String signetureImage) {
        this.signetureImage = signetureImage;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public String getIdentificationNo() {
        return identificationNo;
    }

    public void setIdentificationNo(String identificationNo) {
        this.identificationNo = identificationNo;
    }
}
