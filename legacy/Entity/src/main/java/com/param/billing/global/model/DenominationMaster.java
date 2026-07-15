package com.param.billing.global.model;

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
@NamedQuery(name="GET_DENOMINATION_MASTER_LIST",query="SELECT um.denominationId as denominationId,"
		+ "um.denomination as denomination,"
		+ "um.status as status,"
		+ "um.countryId as countryId "
		+ "FROM DenominationMaster um"
		)

})
@Entity
@Table(name = "m_denomination_master", schema = "public")
@SequenceGenerator(name = "denomination_master_seq", sequenceName = "public.denomination_master_seq", allocationSize = 1)
public class DenominationMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "denomination_master_seq")
	@Column(name = "denomination_id")
	private Integer denominationId;
	
	@Column(name="country_id")
	private Integer countryId;
	
	@Column(name="country_name")
	private String countryName;
	
	@Column(name="denomination")
	private Double denomination;
	
	@Column(name="status")
	private String status;
	
	@Column(name="currency_code")
	private String currencyCode;
	
	@Column(name="currency_symbol")
	private String currencySymbol;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;

	public Integer getDenominationId() {
		return denominationId;
	}

	public void setDenominationId(Integer denominationId) {
		this.denominationId = denominationId;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Double getDenomination() {
		return denomination;
	}

	public void setDenomination(Double denomination) {
		this.denomination = denomination;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
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
