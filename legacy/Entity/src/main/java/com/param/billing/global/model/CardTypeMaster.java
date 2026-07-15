package com.param.billing.global.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.model.pharmacy.SalePaymentMode;

@NamedQueries({
	@NamedQuery(name="GET_ACTIVE_CARD_TYPE_NAME", 
			query="SELECT cardTypeMaster.cardTypeId as cardTypeId, cardTypeMaster.cardTypeName as cardTypeName, "
					+ " cardTypeMaster.unitId as unitId,  cardTypeMaster.pinpadScheme as pinpadScheme, cardTypeMaster.orgnisationId as orgnisationId "
					+ " FROM CardTypeMaster cardTypeMaster"
					+ " WHERE cardTypeMaster.status='A' "
					//+ " AND cardTypeMaster.unitId =:unitId "
					+ " AND cardTypeMaster.orgnisationId =:orgId"
					+" ORDER BY cardTypeMaster.cardTypeName ASC "),
	
	@NamedQuery(name = "GET_CARD_TYPE_LIST_BY_NAME", query = "SELECT cardTypeMaster.cardTypeId as cardTypeId, cardTypeMaster.cardTypeName as cardTypeName, "
			+ "cardTypeMaster.cardTypeCode as cardTypeCode, "
			+ "cardTypeMaster.status as status "
			+ "FROM CardTypeMaster as cardTypeMaster "
			+ "WHERE (LOWER(cardTypeMaster.cardTypeName)=:cardTypeName OR cardTypeMaster.cardTypeName=:cardTypeName) "
			+ "AND cardTypeMaster.orgnisationId=:orgId "),
	
	@NamedQuery(name = "GET_CARD_TYPE_LIST", query = "SELECT cardTypeMaster.cardTypeId as cardTypeId, "
			+ "cardTypeMaster.cardTypeCode as cardTypeCode, " + "cardTypeMaster.cardTypeName as cardTypeName, "
			+ "cardTypeMaster.status as status "
			+ "FROM CardTypeMaster as cardTypeMaster "
					+ "WHERE cardTypeMaster.orgnisationId=:orgId "
					+ "ORDER BY cardTypeMaster.cardTypeId desc"),
	
	@NamedQuery(name = "GET_CARD_TYPE_LIST_BY_NAME_NOT_ID", query = "SELECT cardTypeMaster.cardTypeId as cardTypeId, "
			+ "cardTypeMaster.cardTypeCode as cardTypeCode, " + "cardTypeMaster.cardTypeName as cardTypeName, "
			+ "cardTypeMaster.status as status "
			+ "FROM CardTypeMaster as cardTypeMaster "
			+ "WHERE (LOWER(cardTypeMaster.cardTypeName)=:cardTypeName OR cardTypeMaster.cardTypeName=:cardTypeName) "
			+ "AND cardTypeMaster.cardTypeId!=:cardTypeId "
			+ "AND cardTypeMaster.orgnisationId=:orgId "),
	
})
@Entity
@Table(name="m_card_type_master",schema="billing")
@SequenceGenerator(name = "card_type_master_seq", sequenceName = "billing.card_type_master_seq", allocationSize = 1)
public class CardTypeMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_type_master_seq")
	@Column(name = "card_type_id")
	private int cardTypeId;
	
	@Column(name = "card_type_name")
	private String cardTypeName;
	
	@Column(name = "card_type_code")
	private String cardTypeCode;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "orgnisation_id")
	private Integer orgnisationId;

	@Column(name = "status")
	private Character status;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;
	
	 
	@Column(name = "pin_pad_scheme")
	private String pinpadScheme;
	
	

	
	
	public String getCardTypeCode() {
		return cardTypeCode;
	}

	public void setCardTypeCode(String cardTypeCode) {
		this.cardTypeCode = cardTypeCode;
	}

	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "cardTypeMaster")
	private List<PaymentDepositDetails> listPaymentDepositDetails;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cardTypeMaster", cascade = CascadeType.ALL)
	List<SalePaymentMode> salePaymentModes;
	
	
	
	
	public String getPinpadScheme() {
		return pinpadScheme;
	}

	public void setPinpadScheme(String pinpadScheme) {
		this.pinpadScheme = pinpadScheme;
	}

	public List<SalePaymentMode> getSalePaymentModes() {
		return salePaymentModes;
	}

	public void setSalePaymentModes(List<SalePaymentMode> salePaymentModes) {
		this.salePaymentModes = salePaymentModes;
	}

	public List<PaymentDepositDetails> getListPaymentDepositDetails() {
		return listPaymentDepositDetails;
	}

	public void setListPaymentDepositDetails(List<PaymentDepositDetails> listPaymentDepositDetails) {
		this.listPaymentDepositDetails = listPaymentDepositDetails;
	}

	public int getCardTypeId() {
		return cardTypeId;
	}

	public void setCardTypeId(int cardTypeId) {
		this.cardTypeId = cardTypeId;
	}

	public String getCardTypeName() {
		return cardTypeName;
	}

	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrgnisationId() {
		return orgnisationId;
	}

	public void setOrgnisationId(Integer orgnisationId) {
		this.orgnisationId = orgnisationId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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
	
}
