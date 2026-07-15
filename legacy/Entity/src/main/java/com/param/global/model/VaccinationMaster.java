package com.param.global.model;

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
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.model.master.Generic;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RouteOfAdministration;
import com.param.entity.model.master.StrengthUnit;
import com.param.global.org.common.model.DoseMaster;
import com.param.service.global.model.MPackageMaster;

@NamedQueries({
	
	@NamedQuery(name="GET_Vaccination_Master_LIST",
		query="SELECT vaccinationMaster.vaccinationId AS vaccinationId,vaccinationMaster.vaccinationCode as vaccinationCode,vaccinationMaster.day as day, vaccinationMaster.abbreviation as abbreviation,"
					+ " vaccinationMaster.administrationId AS administrationId,vaccinationMaster.siteId AS siteId,vaccinationMaster.doseId AS doseId,"
					+ " vaccinationMaster.genericId AS genericId,vaccinationMaster.maxAge AS maxAge,vaccinationMaster.maxDay AS maxDay,vaccinationMaster.minAge AS minAge,"
					+ " vaccinationMaster.minDay AS minDay,vaccinationMaster.series AS series,vaccinationMaster.maxDay AS maxDay,vaccinationMaster.minAge AS minAge,vaccinationMaster.interval AS interval,"
					+ " vaccinationMaster.uomId AS uomId,vaccinationMaster.age AS age,vaccinationMaster.remark AS remark, item.strength as strengthDesc,"
					+ " vaccinationMaster.organizationId AS organizationId,vaccinationMaster.unitId AS unitId,vaccinationMaster.status AS status,"
					+ " doseMaster.doseDesc as doseDesc,"
					+ " generic.name as genericName, "
					+ " genderMaster.desc as genderDesc, "
					+ " routeOfAdministration.description as description," 
					+ " siteOfAdministrationMaster.siteName as siteName,"
					+ " strengthUnit.units as units,"
					+ " item.name as name"
					+ " FROM VaccinationMaster vaccinationMaster"
					+ " left JOIN vaccinationMaster.doseMaster doseMaster"
					+ " left JOIN vaccinationMaster.routeOfAdministration routeOfAdministration"
					+ " LEFT JOIN vaccinationMaster.siteOfAdministrationMaster siteOfAdministrationMaster"
					+ " LEFT JOIN vaccinationMaster.strengthUnit strengthUnit"
					+ " LEFT JOIN vaccinationMaster.item item"
					+ " LEFT JOIN vaccinationMaster.generic generic"
					+ " LEFT JOIN vaccinationMaster.genderMaster genderMaster"
					+ " WHERE vaccinationMaster.organizationId= :organizationId"
					+ " AND vaccinationMaster.unitId= :unitId"
					+ " ORDER BY vaccinationMaster.vaccinationId asc"

					),
	
	@NamedQuery(name="GET_Vaccination_Master_BY_ID",
			query="SELECT vaccinationMaster.vaccinationId AS vaccinationId,vaccinationMaster.vaccinationCode as vaccinationCode,vaccinationMaster.day as day, vaccinationMaster.abbreviation as abbreviation,"
					+ " vaccinationMaster.administrationId AS administrationId,vaccinationMaster.siteId AS siteId,vaccinationMaster.doseId AS doseId, vaccinationMaster.genderId AS genderId,"
					+ " vaccinationMaster.genericId AS genericId,vaccinationMaster.maxAge AS maxAge,vaccinationMaster.maxDay AS maxDay,vaccinationMaster.minAge AS minAge,"
					+ " vaccinationMaster.minDay AS minDay,vaccinationMaster.series AS series,vaccinationMaster.maxDay AS maxDay,vaccinationMaster.minAge AS minAge,vaccinationMaster.interval AS interval,vaccinationMaster.chooseInterval AS chooseInterval,"
					+ " vaccinationMaster.uomId AS uomId,vaccinationMaster.age AS age, vaccinationMaster.remark AS remark,"
					+ " vaccinationMaster.organizationId AS organizationId,vaccinationMaster.unitId AS unitId,vaccinationMaster.status AS status,"
					+ " doseMaster.doseDesc as doseDesc, item.strength as strengthDesc,"
					+ " generic.name as genericName, "
					+ " genderMaster.desc as genderDesc, "
					+ " routeOfAdministration.description as description," 
					+ " siteOfAdministrationMaster.siteName as siteName,"
					+ " strengthUnit.units as units,"
					+ " item.name as name"
					+ " FROM VaccinationMaster vaccinationMaster"
					+ " left JOIN vaccinationMaster.doseMaster doseMaster"
					+ " left JOIN vaccinationMaster.routeOfAdministration routeOfAdministration"
					+ " LEFT JOIN vaccinationMaster.siteOfAdministrationMaster siteOfAdministrationMaster"
					+ " LEFT JOIN vaccinationMaster.strengthUnit strengthUnit"
					+ " LEFT JOIN vaccinationMaster.item item"
					+ " LEFT JOIN vaccinationMaster.generic generic"
					+ " LEFT JOIN vaccinationMaster.genderMaster genderMaster"
					+ " WHERE vaccinationMaster.vaccinationId= :vaccinationId"
					+ " AND vaccinationMaster.organizationId= :organizationId"
					+ " AND vaccinationMaster.unitId= :unitId"
					),
	
	@NamedQuery(name="GET_ACTIVE_Vaccination_Master",
		query="SELECT vaccinationMaster.vaccinationId AS vaccinationId,vaccinationMaster.vaccinationCode as vaccinationCode,vaccinationMaster.day as day, vaccinationMaster.abbreviation as abbreviation,"
				+ " vaccinationMaster.administrationId AS administrationId,vaccinationMaster.siteId AS siteId,vaccinationMaster.doseId AS doseId, vaccinationMaster.genderId AS genderId,"
				+ " vaccinationMaster.genericId AS genericId,vaccinationMaster.maxAge AS maxAge,vaccinationMaster.maxDay AS maxDay,vaccinationMaster.minAge AS minAge,"
				+ " vaccinationMaster.minDay AS minDay,vaccinationMaster.series AS series,vaccinationMaster.maxDay AS maxDay,vaccinationMaster.minAge AS minAge,vaccinationMaster.interval AS interval,vaccinationMaster.chooseInterval AS chooseInterval,"
				+ " vaccinationMaster.uomId AS uomId,vaccinationMaster.age AS age, vaccinationMaster.remark AS remark, item.strength as strengthDesc,"
				+ " vaccinationMaster.organizationId AS organizationId,vaccinationMaster.unitId AS unitId,vaccinationMaster.status AS status,"
				+ " doseMaster.doseDesc as doseDesc,"
				+ " generic.name as genericName, "
				+ " genderMaster.desc as genderDesc, "
				+ " routeOfAdministration.description as description," 
				+ " siteOfAdministrationMaster.siteName as siteName,"
				+ " strengthUnit.units as units,"
				+ " item.name as name"
				 + " FROM VaccinationMaster vaccinationMaster"
				 + " left JOIN vaccinationMaster.doseMaster doseMaster"
				 + " left JOIN vaccinationMaster.routeOfAdministration routeOfAdministration"
				 + " LEFT JOIN vaccinationMaster.siteOfAdministrationMaster siteOfAdministrationMaster"
				 + " LEFT JOIN vaccinationMaster.strengthUnit strengthUnit"
				 + " LEFT JOIN vaccinationMaster.item item"
				 + " LEFT JOIN vaccinationMaster.generic generic"
				 + " LEFT JOIN vaccinationMaster.genderMaster genderMaster"
				 + " WHERE vaccinationMaster.status= 'A'"
				 + " AND vaccinationMaster.organizationId= :organizationId"
				 + " AND vaccinationMaster.unitId= :unitId"
				 + " ORDER BY item.name asc"
					),
	
	@NamedQuery(name="GET_ACTIVE_Vaccination_Master_BY_GENDER_ID",
	query="SELECT vaccinationMaster.vaccinationId AS vaccinationId,vaccinationMaster.vaccinationCode as vaccinationCode,vaccinationMaster.day as day, vaccinationMaster.abbreviation as abbreviation,"
			+ " vaccinationMaster.administrationId AS administrationId,vaccinationMaster.siteId AS siteId,vaccinationMaster.doseId AS doseId, vaccinationMaster.genderId AS genderId,"
			+ " vaccinationMaster.genericId AS genericId,vaccinationMaster.maxAge AS maxAge,vaccinationMaster.maxDay AS maxDay,vaccinationMaster.minAge AS minAge,"
			+ " vaccinationMaster.minDay AS minDay,vaccinationMaster.series AS series,vaccinationMaster.maxDay AS maxDay,vaccinationMaster.minAge AS minAge,"
			+ " vaccinationMaster.interval AS interval,vaccinationMaster.chooseInterval AS chooseInterval,"
			+ " vaccinationMaster.uomId AS uomId,vaccinationMaster.age AS age, vaccinationMaster.remark AS remark,"
			+ " vaccinationMaster.organizationId AS organizationId,vaccinationMaster.unitId AS unitId,vaccinationMaster.status AS status,"
			+ " item.strength as strengthDesc, item.id as itemId ,vaccinationMaster.genericId as genericId, "
			+ " generic.name as genericName, "
			+ " genderMaster.desc as genderDesc, "
			+ " routeOfAdministration.description as description," 
			+ " siteOfAdministrationMaster.siteName as siteName,"
			+ " strengthUnit.units as units,"
			+ " item.name as name"
			 + " FROM VaccinationMaster vaccinationMaster"
			 + " left JOIN vaccinationMaster.doseMaster doseMaster"
			 + " left JOIN vaccinationMaster.routeOfAdministration routeOfAdministration"
			 + " LEFT JOIN vaccinationMaster.siteOfAdministrationMaster siteOfAdministrationMaster"
			 + " LEFT JOIN vaccinationMaster.strengthUnit strengthUnit"
			 + " LEFT JOIN vaccinationMaster.item item"
			 + " LEFT JOIN vaccinationMaster.generic generic"
			 + " LEFT JOIN vaccinationMaster.genderMaster genderMaster"
			 + " WHERE vaccinationMaster.status= 'A'"
			 + " AND vaccinationMaster.organizationId= :organizationId"
//			 + " AND vaccinationMaster.unitId= :unitId"
			 + " AND vaccinationMaster.genderId= :genderId"
			 + " ORDER BY item.name asc"
				),
	
	@NamedQuery(name="GET_Vaccination_Master_BY_NAME",
		query="SELECT vaccinationMaster.vaccinationId AS vaccinationId,"
				+ " vaccinationMaster.minAge AS minAge,vaccinationMaster.minDay as minDay,vaccinationMaster.vaccinationCode as vaccinationCode "
				+ " FROM VaccinationMaster vaccinationMaster"
				+ " WHERE vaccinationMaster.vaccinationCode=:vaccinationCode"
				+ " AND vaccinationMaster.minAge=:minAge"
				+ " AND vaccinationMaster.minDay=:minDay"
				+ " AND vaccinationMaster.genderId=:genderId"
				+ " AND vaccinationMaster.series=:series"
				+ " AND vaccinationMaster.organizationId= :organizationId"
				+ " AND vaccinationMaster.unitId= :unitId"
					),
	
	@NamedQuery(name="GET_Vaccination_Master_BY_NAME_NOT_ID",
		query="SELECT vaccinationMaster.vaccinationId AS vaccinationId,vaccinationMaster.minAge AS minAge,vaccinationMaster.minDay as minDay,vaccinationMaster.vaccinationCode as vaccinationCode "
					+ " FROM VaccinationMaster vaccinationMaster"
					+ " WHERE vaccinationMaster.vaccinationCode=:vaccinationCode"
					+ " AND vaccinationMaster.minAge=:minAge"
					+ " AND vaccinationMaster.minDay=:minDay"
					+ " AND vaccinationMaster.genderId=:genderId"
					+ " AND vaccinationMaster.series=:series"
					+ " AND vaccinationMaster.vaccinationId <> :vaccinationId"
					+ " AND vaccinationMaster.organizationId= :organizationId"
					+ " AND vaccinationMaster.unitId= :unitId"
					),
	
//	@NamedQuery(name="GET_ROUTE_OF_ADMINISTRATION_LIST", 
//	query=" select vm.id as administrationId , vm.description as description  "
//			+ " from VaccinationMaster vaccinationMaster"
//			+ " LEFT JOIN vaccinationMaster.routeOfAdministration vm"
//			+ " Where vaccinationMaster.organizationId=:organizationId"
//			+ " and vaccinationMaster.unitId=:unitId")
	})

         @NamedNativeQuery(name="GET_ROUTE_OF_ADMINISTRATION_LIST", 
									query=" select roa.id as \"administrationId\" , roa.description as \"description\"  "
											+ " from m_route_of_administration roa"
											+ " Where roa.organization_id=:organizationId"
											+ " and roa.unit_id=:unitId"
											+ " ORDER BY roa.description ASC ")

@Entity
@Table(name = "m_vaccination_master", schema = "lab")
@SequenceGenerator(name = "seq_m_vaccination_master", sequenceName = "lab.m_vaccination_master_seq", allocationSize = 1)
public class VaccinationMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_vaccination_master")
	@Column(name = "vaccination_id")
	private Integer vaccinationId;
	
	@Column(name = "vaccine_code")
	private Integer vaccinationCode;
	
	
	@Column(name = "administration_id")
	private Integer administrationId;
	
	@Column(name = "site_id")
	private Integer siteId;
	
	@Column(name = "dose_id")
	private Integer doseId;
	
	
	@Column(name = "uom_id")
	private Integer  uomId;
	
	@Column(name = "age")
	private Integer  age;
	
	@Column(name = "total_day_count")
	private Integer  dayid;
	
	@Column(name="day")
	private String day;
	
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
	
	@Column(name = "interval")
	private Integer interval;
	
	@Column(name = "choose_interval")
	private String chooseInterval;
	
	@Column(name = "generic_id")
	private Integer genericId;
	
	@Column(name = "max_age")
	private Integer maxAge;
	
	@Column(name = "max_day")
	private String maxDay;
	
	@Column(name = "min_age")
	private String minAge;
	
	@Column(name = "min_day")
	private String minDay;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "series")
	private Integer series;
	
	@Column(name = "gender_id")
	private Integer genderId;
	
	@Column(name="abbreviation")
	private String abbreviation;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="administration_id", insertable=false, updatable=false)
	private RouteOfAdministration routeOfAdministration;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="dose_id", insertable=false, updatable=false)
	private DoseMaster doseMaster;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="site_id", insertable=false, updatable=false)
	private SiteOfAdministrationMaster siteOfAdministrationMaster;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="uom_id", insertable=false, updatable=false)
	private StrengthUnit strengthUnit;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="vaccine_code", insertable=false, updatable=false)
	private Item item;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="generic_id", insertable=false, updatable=false)
	private Generic generic;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="gender_id", insertable=false, updatable=false)
	private GenderMaster genderMaster;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "vaccinationMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VaccineImmunizationMapper> vaccinationMasterList;

	public Integer getVaccinationId() {
		return vaccinationId;
	}

	public void setVaccinationId(Integer vaccinationId) {
		this.vaccinationId = vaccinationId;
	}

	public Integer getVaccinationCode() {
		return vaccinationCode;
	}

	public void setVaccinationCode(Integer vaccinationCode) {
		this.vaccinationCode = vaccinationCode;
	}

	
	public Integer getAdministrationId() {
		return administrationId;
	}

	public void setAdministrationId(Integer administrationId) {
		this.administrationId = administrationId;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getDoseId() {
		return doseId;
	}

	public void setDoseId(Integer doseId) {
		this.doseId = doseId;
	}

	public Integer getUomId() {
		return uomId;
	}

	public void setUomId(Integer uomId) {
		this.uomId = uomId;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getDayid() {
		return dayid;
	}

	public void setDayid(Integer dayid) {
		this.dayid = dayid;
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

	
	public RouteOfAdministration getRouteOfAdministration() {
		return routeOfAdministration;
	}

	public void setRouteOfAdministration(RouteOfAdministration routeOfAdministration) {
		this.routeOfAdministration = routeOfAdministration;
	}

	public DoseMaster getDoseMaster() {
		return doseMaster;
	}

	public void setDoseMaster(DoseMaster doseMaster) {
		this.doseMaster = doseMaster;
	}

	public SiteOfAdministrationMaster getSiteOfAdministrationMaster() {
		return siteOfAdministrationMaster;
	}

	public void setSiteOfAdministrationMaster(SiteOfAdministrationMaster siteOfAdministrationMaster) {
		this.siteOfAdministrationMaster = siteOfAdministrationMaster;
	}

	public StrengthUnit getStrengthUnit() {
		return strengthUnit;
	}

	public void setStrengthUnit(StrengthUnit strengthUnit) {
		this.strengthUnit = strengthUnit;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public String getChooseInterval() {
		return chooseInterval;
	}

	public void setChooseInterval(String chooseInterval) {
		this.chooseInterval = chooseInterval;
	}

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	public String getMaxDay() {
		return maxDay;
	}

	public void setMaxDay(String maxDay) {
		this.maxDay = maxDay;
	}


	public String getMinAge() {
		return minAge;
	}

	public void setMinAge(String minAge) {
		this.minAge = minAge;
	}

	public String getMinDay() {
		return minDay;
	}

	public void setMinDay(String minDay) {
		this.minDay = minDay;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSeries() {
		return series;
	}

	public void setSeries(Integer series) {
		this.series = series;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public Generic getGeneric() {
		return generic;
	}

	public void setGeneric(Generic generic) {
		this.generic = generic;
	}

	public GenderMaster getGenderMaster() {
		return genderMaster;
	}

	public void setGenderMaster(GenderMaster genderMaster) {
		this.genderMaster = genderMaster;
	}
	
	

}
