package com.param.adt.master.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.CountryMaster;

@NamedQueries({

		@NamedQuery(name = "GET_NATIONALITY_LIST", query = "SELECT nationalityMaster.nationalityId as nationalityId, "
				+ "nationalityMaster.nationalityName as nationalityName, "
				+ "nationalityMaster.nationalityCode as nationalityCode, " + "nationalityMaster.status as status,cntMstr.countryName as countryName, cntMstr.countryId as countryId "
				+ "FROM NationalityMaster as nationalityMaster "
				+ " left join nationalityMaster.contryMaster cntMstr "
				+ "WHERE nationalityMaster.organizationId=:orgId "
				+ "ORDER BY nationalityMaster.nationalityId DESC"),

		@NamedQuery(name = "GET_NATIONALITY_LIST_BY_ID", query = "SELECT nationalityMaster.nationalityId as nationalityId, "
				+ "nationalityMaster.nationalityName as nationalityName, "
				+ "nationalityMaster.nationalityCode as nationalityCode, " + "nationalityMaster.status as status ,cntMstr.countryName as countryName, cntMstr.countryId as countryId "
				+ " FROM NationalityMaster as nationalityMaster "
				+ " left join nationalityMaster.contryMaster cntMstr "
				+ " WHERE nationalityMaster.nationalityId=:nationalityId"),

		@NamedQuery(name = "GET_NATIONALITY_LIST_BY_NAME", query = "SELECT nationalityMaster.nationalityId as nationalityId, "
				+ "nationalityMaster.nationalityName as nationalityName, "
				+ "nationalityMaster.nationalityCode as nationalityCode, " + "nationalityMaster.status as status "
				+ "FROM NationalityMaster as nationalityMaster "
				+ "WHERE (LOWER(nationalityMaster.nationalityName)=:nationalityName OR nationalityMaster.nationalityName=:nationalityName) "
				+ "AND nationalityMaster.organizationId=:orgId "),

		@NamedQuery(name = "GET_NATIONALITY_LIST_BY_NAME_NOT_ID", query = "SELECT nationalityMaster.nationalityId as nationalityId, "
				+ "nationalityMaster.nationalityName as nationalityName, "
				+ "nationalityMaster.nationalityCode as nationalityCode, " + "nationalityMaster.status as status "
				+ "FROM NationalityMaster as nationalityMaster "
				+ "WHERE (LOWER(nationalityMaster.nationalityName)=:nationalityName OR nationalityMaster.nationalityName=:nationalityName) "
				+ "AND nationalityMaster.nationalityId !=:nationalityId "
				+ "AND nationalityMaster.organizationId=:orgId"),

		@NamedQuery(name = "GET_ACTIVE_NATIONALITY_LIST", query = "SELECT nationalityMaster.nationalityId as nationalityId, "
				+ "nationalityMaster.nationalityName as nationalityName, "
				+ "nationalityMaster.nationalityCode as nationalityCode, " 
				+ "nationalityMaster.status as status,cntMstr.countryName as countryName, cntMstr.countryId as countryId,  "
				+ "nationalityMaster.organizationId as organizationId "
				+ "FROM NationalityMaster as nationalityMaster "
				+ " left join nationalityMaster.contryMaster cntMstr "
				+ "WHERE nationalityMaster.status='A' "
				+ "AND nationalityMaster.organizationId=:orgId "
				+ "ORDER By nationalityMaster.nationalityName ASC"),
		
		@NamedQuery(name = "GET_NATIONALITY_BY_COUNTRY_ID", 
				query = "SELECT nationalityMaster.nationalityId as nationalityId, "
				+ "nationalityMaster.nationalityName as nationalityName, "
				+ "nationalityMaster.nationalityCode as nationalityCode, " 
				+ "nationalityMaster.status as status ,cntMstr.countryName as countryName, cntMstr.countryId as countryId, "
				+ " nationalityMaster.organizationId as organizationId "
				+ " FROM NationalityMaster as nationalityMaster "
				+ " inner join nationalityMaster.contryMaster cntMstr "
				+ " WHERE nationalityMaster.countryId=:countryId"
				+ "	AND nationalityMaster.organizationId=:orgId "),

})

@Entity
@Table(name = "m_nationality_master", schema = "public")
@SequenceGenerator(name = "nationality_master_seq", sequenceName = "public.nationality_master_seq", allocationSize = 1)
public class NationalityMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nationality_master_seq")
	@Column(name = "nationality_id")
	private int nationalityId;

	@Column(name = "nationality_code")
	private String nationalityCode;

	@Column(name = "nationality_name")
	private String nationalityName;

	@Column(name = "status")
	private char status;

	@Column(name = "created_by")
	private int createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name="updated_by")
	private int updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name = "country_id")
	private Integer countryId;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", insertable = false, updatable = false)
	private OrganizationMaster organizationMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id", insertable = false, updatable = false)
	private CountryMaster contryMaster;
	
	
	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public OrganizationMaster getOrganizationMaster() {
		return organizationMaster;
	}

	public void setOrganizationMaster(OrganizationMaster organizationMaster) {
		this.organizationMaster = organizationMaster;
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

	public int getNationalityId() {
		return nationalityId;
	}

	public void setNationalityId(int nationalityId) {
		this.nationalityId = nationalityId;
	}

	public String getNationalityCode() {
		return nationalityCode;
	}

	public void setNationalityCode(String nationalityCode) {
		this.nationalityCode = nationalityCode;
	}

	public String getNationalityName() {
		return nationalityName;
	}

	public void setNationalityName(String nationalityName) {
		this.nationalityName = nationalityName;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

}
