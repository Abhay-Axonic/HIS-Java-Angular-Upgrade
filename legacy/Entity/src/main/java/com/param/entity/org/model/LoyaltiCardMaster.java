package com.param.entity.org.model;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@NamedQueries({
	
	@NamedQuery(name="GET_LOYALTI_CARD_MASTER_LIST",
		query="SELECT loyaltiCardMaster.loyaltyCardId AS loyaltyCardId, loyaltiCardMaster.cardNo AS cardNo, "
					+ " loyaltiCardMaster.organisationId AS organisationId, loyaltiCardMaster.unitId AS unitId, loyaltiCardMaster.status AS status"
					+ " FROM LoyaltiCardMaster loyaltiCardMaster"
					+ " WHERE loyaltiCardMaster.isDeleted='N'"
					+ " AND loyaltiCardMaster.organisationId= :organisationId"
					+ " AND loyaltiCardMaster.unitId= :unitId"
					+ " ORDER BY loyaltiCardMaster.loyaltyCardId asc"
					)
	
	})
@NamedNativeQueries({
	@NamedNativeQuery(name = "CHECK_LOYALATI_CARD_NO", query = "select "
			+ "	count(loyalty_card.loyalty_card_id) "
			+ "from "
			+ "	public.m_loyalty_card loyalty_card "
			+ "where "
			+ "	loyalty_card.organisation_id = :orgId "
			+ "	and loyalty_card.status = 'A' "
			+ "	and loyalty_card.is_deleted = 'N' "
			+ "	and loyalty_card.card_no = :cardNo"),
	
	@NamedNativeQuery(name = "UPDATE_PATIENT_BY_LOYALTI_CARD",
	 query = "update public.m_loyalty_card set patient_id =:patientId,is_active = 'Y' where loyalty_card_id =:loyaltyCardId "),
	
})
@Entity
@Table(name="m_loyalty_card",schema="public")
@SequenceGenerator(name="seq_loyalty_card",sequenceName="public.seq_loyalty_card",allocationSize=1)
public class LoyaltiCardMaster {
	
	@Id
	@Column(name="loyalty_card_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_loyalty_card")
	private int loyaltyCardId;
	
	@Column(name="card_no")
	private String cardNo;
	
	@Column(name="organisation_id")
	private Integer organisationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="status")
	private char status;

	@Column(name="created_date")
	private Date createdDate;

	@Column(name="created_by")
	private int createdBy;

	@Column(name="updated_by")
	private int updatedBy;

	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="is_deleted")
	private char isDeleted;
	
	@Column(name="is_active")
	private char isActive;
	
	@Column(name="patient_id")
	private Integer patientId;

	@Column(name="is_user_defined")
	private Character isUserDefined;
	
	public int getLoyaltyCardId() {
		return loyaltyCardId;
	}

	public void setLoyaltyCardId(int loyaltyCardId) {
		this.loyaltyCardId = loyaltyCardId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public char getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(char isDeleted) {
		this.isDeleted = isDeleted;
	}

	public char getIsActive() {
		return isActive;
	}

	public void setIsActive(char isActive) {
		 this.isActive = (isActive == '\u0000') ? 'N' : isActive;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Character getIsUserDefined() {
		return isUserDefined;
	}

	public void setIsUserDefined(Character isUserDefined) {
		 this.isUserDefined = (isUserDefined == '\u0000') ? 'N' : isUserDefined;
	}
	
	

}
