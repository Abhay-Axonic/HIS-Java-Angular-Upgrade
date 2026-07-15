package com.param.global.model;

import java.math.BigDecimal;
import java.util.Date;

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
	
	@NamedQuery(name="GET_LOYALTY_CARD_CONFIGURATION_LIST",
		query="SELECT loyaltyCardConfiguration.cardConfigurationId AS cardConfigurationId, loyaltyCardConfiguration.minStartAmount AS minStartAmount, loyaltyCardConfiguration.thresholdAmount AS thresholdAmount,"
					+ " loyaltyCardConfiguration.minRechargeAmount AS minRechargeAmount, loyaltyCardConfiguration.isDeleted AS isDeleted, loyaltyCardConfiguration.organisationId AS organisationId, loyaltyCardConfiguration.unitId AS unitId, loyaltyCardConfiguration.status AS status"
					+ " FROM LoyaltyCardConfiguration loyaltyCardConfiguration"
					+ " WHERE loyaltyCardConfiguration.isDeleted ='N'"
					+ " AND loyaltyCardConfiguration.organisationId= :organisationId"
					+ " AND loyaltyCardConfiguration.unitId= :unitId"
					+ " ORDER BY loyaltyCardConfiguration.cardConfigurationId asc"
					),
	
	@NamedQuery(name="GET_LOYALTY_CARD_CONFIGURATION_BY_ID",
		query="SELECT loyaltyCardConfiguration.cardConfigurationId AS cardConfigurationId, loyaltyCardConfiguration.minStartAmount AS minStartAmount, loyaltyCardConfiguration.thresholdAmount AS thresholdAmount,"
					+ " loyaltyCardConfiguration.minRechargeAmount AS minRechargeAmount, loyaltyCardConfiguration.isDeleted AS isDeleted, loyaltyCardConfiguration.organisationId AS organisationId, "
					+ " loyaltyCardConfiguration.unitId AS unitId, loyaltyCardConfiguration.status AS status"
					+ " FROM LoyaltyCardConfiguration loyaltyCardConfiguration"
					+ " WHERE loyaltyCardConfiguration.cardConfigurationId= :cardConfigurationId"
					+ " AND loyaltyCardConfiguration.organisationId= :organisationId"
					+ " AND loyaltyCardConfiguration.unitId= :unitId"
					+ " AND loyaltyCardConfiguration.isDeleted ='N'"

					),
	
	@NamedQuery(name="CHECK_LOYALTY_CARD_CONFIGURATION_AVAILABLE",
	query="SELECT loyaltyCardConfiguration.cardConfigurationId AS cardConfigurationId, loyaltyCardConfiguration.isDeleted AS isDeleted, loyaltyCardConfiguration.organisationId AS organisationId, "
				+ " loyaltyCardConfiguration.unitId AS unitId, loyaltyCardConfiguration.status AS status"
				+ " FROM LoyaltyCardConfiguration loyaltyCardConfiguration"
				+ " WHERE loyaltyCardConfiguration.organisationId= :organisationId"
				+ " AND loyaltyCardConfiguration.unitId =:unitId"
				+ " AND loyaltyCardConfiguration.status ='A'"
				+ " AND loyaltyCardConfiguration.isDeleted !='Y'"

				)
	
	})


@Entity
@Table(name = "m_loyalty_card_configuration", schema = "public")
@SequenceGenerator(name = "seq_loyalty_card_configuration", sequenceName = "public.seq_loyalty_card_configuration", allocationSize = 1)
public class LoyaltyCardConfiguration {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_loyalty_card_configuration")
	
	@Column(name = "card_configuration_id")
	private Integer cardConfigurationId;
	
	@Column(name = "min_start_amount")
	private BigDecimal minStartAmount;
	
	@Column(name = "thresholdAmount")
	private BigDecimal thresholdAmount;
	
	@Column(name = "min_recharge_amount")
	private BigDecimal minRechargeAmount;
		
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "is_deleted")
	private Character isDeleted;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organisation_id")
	private Integer organisationId;

	public Integer getCardConfigurationId() {
		return cardConfigurationId;
	}

	public void setCardConfigurationId(Integer cardConfigurationId) {
		this.cardConfigurationId = cardConfigurationId;
	}

	public BigDecimal getMinStartAmount() {
		return minStartAmount;
	}

	public void setMinStartAmount(BigDecimal minStartAmount) {
		this.minStartAmount = minStartAmount;
	}

	public BigDecimal getThresholdAmount() {
		return thresholdAmount;
	}

	public void setThresholdAmount(BigDecimal thresholdAmount) {
		this.thresholdAmount = thresholdAmount;
	}

	public BigDecimal getMinRechargeAmount() {
		return minRechargeAmount;
	}

	public void setMinRechargeAmount(BigDecimal minRechargeAmount) {
		this.minRechargeAmount = minRechargeAmount;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
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

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}


	
}
