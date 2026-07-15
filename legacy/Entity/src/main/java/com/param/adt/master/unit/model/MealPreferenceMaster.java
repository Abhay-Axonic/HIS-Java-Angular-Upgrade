package com.param.adt.master.unit.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.admission.model.AdmissionDetails;
import com.param.adt.admission.model.Sponsor;
import com.param.adt.master.global.model.OrganizationMaster;
import com.param.adt.master.global.model.UnitMaster;
import com.param.opd.coversheet.model.IpdDietOrder;
@NamedQueries
({
	@NamedQuery(name="GET_MEAL_PREFERENCE",query="SELECT mp.mealPreferenceId as mealPreferenceId, "
			+ "mp.mealPreferenceDesc as mealPreferenceDesc "
			+ "FROM MealPreferenceMaster mp "
			+ "WHERE mp.unitId=:unitId "
			+ "AND mp.organizationId=:organizationId "
			+ "AND mp.status='A'"
			+ "ORDER BY mp.mealPreferenceDesc ASC"),
	@NamedQuery(name = "GET_MEAL_PREFERENCE_MASTER_BY_ID",
	query = "SELECT dm.mealPreferenceId as mealPreferenceId, "
			+ "dm.mealPreferenceDesc as mealPreferenceDesc, "
			+ "dm.mealPreferenceCode as mealPreferenceCode, "
			+ "dm.status as status  "
			+ "FROM MealPreferenceMaster as dm "
			+ "WHERE dm.mealPreferenceId=:mealPrefernceId " 
			+ "AND dm.organizationId=:orgId "
			+ "AND dm.unitId=:unitId"),
	@NamedQuery(name = "GET_MEAL_PREFERENCE_MASTER_LIST", 
	query = "SELECT dm.mealPreferenceId as mealPreferenceId, "
			+ "dm.mealPreferenceCode as mealPreferenceCode, " 
			+ "dm.status as status, " 
			+ "dm.mealPreferenceDesc as mealPreferenceDesc "
			+ "FROM MealPreferenceMaster as dm  "
			+ "WHERE dm.organizationId=:orgId "
			+ "AND dm.unitId=:unitId "
			+ "ORDER BY dm.mealPreferenceDesc ASC"),

		@NamedQuery(name = "GET_ACTIVE_MEAL_PREFERENCE_MASTER_LIST",
	query = "SELECT dm.mealPreferenceId as mealPreferenceId, "
			+ "dm.mealPreferenceCode as mealPreferenceCode, "
			+ "dm.status as status, "
			+ "dm.mealPreferenceDesc as mealPreferenceDesc, "
			+ "dm.unitId as unitId, "
			+ "dm.organizationId as organizationId "
			+ "FROM MealPreferenceMaster as dm  "
			+ "WHERE "
			+ "dm.organizationId=:orgId "
			+ "AND dm.unitId=:unitId "
			+ "AND dm.status='A' "
			+ "ORDER BY dm.mealPreferenceDesc ASC"),

	@NamedQuery(name = "GET_MEAL_PREFERENCE_MASTER_BY_NAME", 
	query = "SELECT dm.mealPreferenceId as mealPreferenceId, "
			+ "dm.mealPreferenceCode as mealPreferenceCode, " 
			+ "dm.mealPreferenceDesc as mealPreferenceDesc, "
			+ "dm.status as status "
			+ "FROM MealPreferenceMaster as dm  "
			+ " WHERE LOWER(dm.mealPreferenceDesc)=:mealDesc  " 
			+ "AND dm.organizationId=:orgId "
			+ "AND dm.unitId=:unitId "),
	@NamedQuery(name = "GET_MEAL_PREFERENCE_MASTER_BYNAME_NOTBYID",
	query = "SELECT dm.mealPreferenceId as mealPreferenceId, "
			+ "dm.mealPreferenceCode as mealPreferenceCode, " 
			+ "dm.mealPreferenceDesc as mealPreferenceDesc "
			+ "FROM MealPreferenceMaster as dm  "
			+ "WHERE LOWER(dm.mealPreferenceDesc)=:mealDesc "
			+ "AND dm.mealPreferenceId !=:mealPrefernceId "
			+ "AND dm.organizationId=:orgId "
			+ "AND dm.unitId=:unitId ")
})
@Entity
@Table(name = "m_meal_preference_master", schema = "adt")
@SequenceGenerator(name = "meal_preference_master_seq", sequenceName = "adt.meal_preference_master_seq", allocationSize = 1)
public class MealPreferenceMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meal_preference_master_seq")
	@Column(name = "meal_preference_id")
	private int mealPreferenceId;

	@Column(name = "meal_preference_code")
	private String mealPreferenceCode;

	@Column(name = "meal_preference_desc")
	private String mealPreferenceDesc;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private int createdBy;

	@Column(name = "updated_by")
	private int updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "status")
	private char status;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", insertable = false, nullable = false, updatable = false)
	private OrganizationMaster organizationMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unit_id", insertable = false, nullable = false, updatable = false)
	private UnitMaster unitMaster;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mealPreferenceMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Sponsor> sponsersList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "attendedMealPreferenceMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Sponsor> attendedsponsersList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mealPreferenceMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<IpdDietOrder> ipdDietOrderList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mealPreferenceMaster1", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AdmissionDetails> admissionDetailsList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "attendentMealPreferenceMaster1", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AdmissionDetails> attendentAdmissionDetailsList;

	public List<Sponsor> getSponsersList() {
		return sponsersList;
	}

	public void setSponsersList(List<Sponsor> sponsersList) {
		this.sponsersList = sponsersList;
	}

	public List<Sponsor> getAttendedsponsersList() {
		return attendedsponsersList;
	}

	public void setAttendedsponsersList(List<Sponsor> attendedsponsersList) {
		this.attendedsponsersList = attendedsponsersList;
	}

	public int getMealPreferenceId() {
		return mealPreferenceId;
	}

	public void setMealPreferenceId(int mealPreferenceId) {
		this.mealPreferenceId = mealPreferenceId;
	}

	public String getMealPreferenceCode() {
		return mealPreferenceCode;
	}

	public void setMealPreferenceCode(String mealPreferenceCode) {
		this.mealPreferenceCode = mealPreferenceCode;
	}

	public String getMealPreferenceDesc() {
		return mealPreferenceDesc;
	}

	public void setMealPreferenceDesc(String mealPreferenceDesc) {
		this.mealPreferenceDesc = mealPreferenceDesc;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	/*
	 * public OrganizationMaster getOrganizationMaster() { return
	 * organizationMaster; }
	 * 
	 * public void setOrganizationMaster(OrganizationMaster organizationMaster)
	 * { this.organizationMaster = organizationMaster; }
	 */

	public UnitMaster getUnitMaster() {
		return unitMaster;
	}

	public void setUnitMaster(UnitMaster unitMaster) {
		this.unitMaster = unitMaster;
	}

}
