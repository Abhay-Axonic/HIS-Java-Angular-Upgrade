package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;



@NamedNativeQueries({
	
	@NamedNativeQuery(name="GET_GROWTH_CHART_LIST",
			query="select "
					+ "	growth_chart.growth_chart_id as \"growthChartId\", "
					+ "	growth_chart.age_in_months as \"ageInMonths\", "
					+ "	growth_chart.weight_from as \"weightFrom\", "
					+ "	growth_chart.weight_to as \"weightTo\", "
					+ "	wt_unit.unit_name as \"weightUnit\", "
					+ "	growth_chart.height_from as \"heightFrom\", "
					+ "	growth_chart.height_to as \"heightTo\", "
					+ "	ht_unit.unit_name as \"heightUnitString\", "
					+ "	growth_chart.circumference_from as \"circumferenceFrom\", "
					+ "	growth_chart.circumference_to as \"circumferenceTo\", "
					+ "	cf_unit.unit_name as \"circumferenceUnitString\", "
					+ "	growth_chart.status as \"status\", "
					+ " gender_master.gender_name as \"gender\" "
					+ "from "
					+ "	public.m_growth_chart_master growth_chart "
					+ "inner join public.m_height_unit ht_unit on "
					+ "	growth_chart.height_unit = ht_unit.height_unit_id "
					+ "inner join public.m_weight_unit wt_unit on "
					+ "	growth_chart.weight_unit_id = wt_unit.weight_unit_id "
					+ "inner join public.m_circufereance_unit cf_unit on "
					+ "	growth_chart.circumference_unit = cf_unit.circufereance_unit_id "
					+ "inner join public.m_gender_master gender_master on  "
					+ " growth_chart.gender_id = gender_master.gender_id "
					+ "where "
					+ "	growth_chart.organisation_id = :orgId "
					+ "	and growth_chart.unit_id =:unitId "
					+ "	and growth_chart.status <> 'D'"
					+ "order by growth_chart.growth_chart_id desc"
			),
	
	@NamedNativeQuery(name="GET_GROWTH_CHART_BY_ID",
	query="	select growth_chart.growth_chart_id as \"growthChartId\", "
			+ "	growth_chart.age_in_months as \"ageInMonths\", "
			+ "	growth_chart.weight_from as \"weightFrom\", "
			+ "	growth_chart.weight_to as \"weightTo\", "
			+ "	growth_chart.weight_unit_id as \"weightUnitId\", "
			+ "	growth_chart.height_from as \"heightFrom\", "
			+ "	growth_chart.height_to as \"heightTo\", "
			+ "	growth_chart.height_unit as \"heightUnit\", "
			+ "	growth_chart.circumference_from as \"circumferenceFrom\", "
			+ "	growth_chart.circumference_to as \"circumferenceTo\", "
			+ "	growth_chart.circumference_unit as \"circumferenceUnit\", "
			+ "	growth_chart.status as \"status\", "
			+ "	growth_chart.organisation_id as \"organisationId\", "
			+ "	growth_chart.created_by as \"createdBy\", "	
			+ "	to_char(growth_chart.created_date,'DD-MM-YYYY HH:MI:SS') as \"createdDate\", "
			+ "	growth_chart.gender_id as \"genderId\", "	
			+ "	growth_chart.unit_id as \"unitId\" "
			+ "from "
			+ "	public.m_growth_chart_master growth_chart "
			+ "where "
			+ "	growth_chart.organisation_id = :orgId "
			+ "	and growth_chart.unit_id =:unitId "
			+ "	and growth_chart.growth_chart_id =:growthChartId "
	),
	
	@NamedNativeQuery(name="CHECK_GROWTH_CHART_EXIST_ADD",
	query="select "
			+ "	count(*) "
			+ "from "
			+ "	public.m_growth_chart_master growth_chart "
			+ "where "
			+ "	growth_chart.organisation_id = :orgId "
			+ "	and growth_chart.unit_id =:unitId "
			+ "	and growth_chart.age_in_months =:ageInMonths "
			+ "	and growth_chart.gender_id =:genderId "
			+ "	and growth_chart.status <> 'D'"
	),
	
	
	@NamedNativeQuery(name="CHECK_GROWTH_CHART_EXIST_UPDATE",
	query=" select "
			+ "	count(*) "
			+ "from "
			+ "	public.m_growth_chart_master growth_chart "
			+ "where "
			+ "	growth_chart.organisation_id = :orgId "
			+ "	and growth_chart.unit_id =:unitId "
			+ "	and growth_chart.growth_chart_id <> :growthChartId "
			+ "	and growth_chart.age_in_months =:ageInMonths "
			+ "	and growth_chart.gender_id =:genderId "
			+ "	and growth_chart.status <> 'D'"
	),
	
	@NamedNativeQuery(name="DELETE_GROWTH_CHART_MASTER",
	query=" update public.m_growth_chart_master "
			+ "set status = 'D' "
			+ "where growth_chart_id = :growthChartId"
	),
	
})

@Entity
@Table(name = "m_growth_chart_master" , schema = "public")
@SequenceGenerator(name = "seq_growth_chart_master" , sequenceName = "public.seq_growth_chart_master" , allocationSize = 1)
public class GrowthChartMaster {
	
	@Id
	@Column(name="growth_chart_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_growth_chart_master")
	private int growthChartId;
	
	@Column(name="age_in_months")
	private Integer ageInMonths;
	
	@Column(name="age_in_days")
	private Integer ageInDays;
	
	@Column(name="weight_from")
	private Double weightFrom;
	
	@Column(name="weight_to")
	private Double weightTo;
	
	@Column(name="weight_unit_id")
	private Integer weightUnitId;
	
	@Column(name="height_from")
	private Double heightFrom;
	
	@Column(name="height_to")
	private Double heightTo;
	
	@Column(name="height_unit")
	private Integer heightUnit;
	
	@Column(name="circumference_from")
	private Double circumferenceFrom;
	
	@Column(name="circumference_to")
	private Double circumferenceTo;
	
	@Column(name="circumference_unit")
	private Integer circumferenceUnit;
	
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

	@Column(name="gender_id")
	private Integer genderId;

	public int getGrowthChartId() {
		return growthChartId;
	}

	public void setGrowthChartId(int growthChartId) {
		this.growthChartId = growthChartId;
	}

	public Integer getAgeInMonths() {
		return ageInMonths;
	}

	public void setAgeInMonths(Integer ageInMonths) {
		this.ageInMonths = ageInMonths;
	}

	public Integer getAgeInDays() {
		return ageInDays;
	}

	public void setAgeInDays(Integer ageInDays) {
		this.ageInDays = ageInDays;
	}

	public Double getWeightFrom() {
		return weightFrom;
	}

	public void setWeightFrom(Double weightFrom) {
		this.weightFrom = weightFrom;
	}

	public Double getWeightTo() {
		return weightTo;
	}

	public void setWeightTo(Double weightTo) {
		this.weightTo = weightTo;
	}

	public Integer getWeightUnitId() {
		return weightUnitId;
	}

	public void setWeightUnitId(Integer weightUnitId) {
		this.weightUnitId = weightUnitId;
	}

	public Double getHeightFrom() {
		return heightFrom;
	}

	public void setHeightFrom(Double heightFrom) {
		this.heightFrom = heightFrom;
	}

	public Double getHeightTo() {
		return heightTo;
	}

	public void setHeightTo(Double heightTo) {
		this.heightTo = heightTo;
	}

	public Integer getHeightUnit() {
		return heightUnit;
	}

	public void setHeightUnit(Integer heightUnit) {
		this.heightUnit = heightUnit;
	}

	public Double getCircumferenceFrom() {
		return circumferenceFrom;
	}

	public void setCircumferenceFrom(Double circumferenceFrom) {
		this.circumferenceFrom = circumferenceFrom;
	}

	public Double getCircumferenceTo() {
		return circumferenceTo;
	}

	public void setCircumferenceTo(Double circumferenceTo) {
		this.circumferenceTo = circumferenceTo;
	}

	public Integer getCircumferenceUnit() {
		return circumferenceUnit;
	}

	public void setCircumferenceUnit(Integer circumferenceUnit) {
		this.circumferenceUnit = circumferenceUnit;
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

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

}
