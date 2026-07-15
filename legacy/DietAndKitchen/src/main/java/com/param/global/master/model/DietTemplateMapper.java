package com.param.global.master.model;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.opd.coversheet.model.DietTypeMaster;

@NamedQueries({
		@NamedQuery(name = "DIET_TEMPLATE_MAPPER.GET_LIST_BY_DISH_FOOD_TYPE", query = "SELECT dietTempMap.dietTemplateId as dietTemplateId,"
				+ "dietTempMap.weekDayMasterId AS weekDayMasterId, "
				+ "dietTempMap.dietTimingMasterId as dietTimingMasterId, "
				+ "dietTempMap.dishQuantity as dishQuantity, " + "dietTempMap.organizationId as organizationId, "
				+ "dietTempMap.dietOption as dietOption, " + "dietTempMap.dietTypeMasterId as dietTypeMasterId, "
				+ "dietTempMap.createdBy as createdBy, " + "dietTempMap.updatedBy as updatedBy, "
				+ "dishMaster.dishCode as dishName " + "FROM DietTemplateMapper dietTempMap "
				+ "INNER JOIN dietTempMap.dishMaster AS dishMaster "
				+ "WHERE dietTempMap.dietTypeMasterId=:dietTypeMasterId and dietTempMap.dietOption=:dietOption"),

		@NamedQuery(name = "DIET_TEMPLATE_MAPPER.GET_ACTIVE_LIST", query = "SELECT dietTempMap.dietTemplateId as dietTemplateId,"
				+ "dietTempMap.weekDayMasterId AS weekDayMasterId, "
				+ "dietTempMap.dietTimingMasterId as dietTimingMasterId, "
				+ "dietTempMap.dishQuantity as dishQuantity, " + "dietTempMap.organizationId as organizationId, "
				+ "dietTempMap.dietOption as dietOption, " + "dietTempMap.dietTypeMasterId as dietTypeMasterId, "
				+ "dietTempMap.createdBy as createdBy, " + "dietTempMap.updatedBy as updatedBy, "
				+ "dishMaster.dishCode as dishName " + "FROM DietTemplateMapper dietTempMap "
				+ "INNER JOIN dietTempMap.dishMaster AS dishMaster " + "WHERE dietTempMap.status='A'"),

		@NamedQuery(name = "DIET_TEMPLATE_MAPPER.GET_ALL_LIST", query = "SELECT dietTempMap.dietTemplateId as dietTemplateId,"
				+ "dietTempMap.weekDayMasterId AS weekDayMasterId, "
				+ "dietTempMap.dietTimingMasterId as dietTimingMasterId, "
				+ "dietTempMap.dishQuantity as dishQuantity, " + "dietTempMap.organizationId as organizationId, "
				+ "dietTempMap.dietOption as dietOption, " + "dietTempMap.dietTypeMasterId as dietTypeMasterId, "
				+ "dietTempMap.createdBy as createdBy, " + "dietTempMap.updatedBy as updatedBy, "
				+ "dishMaster.dishCode as dishName " + "FROM DietTemplateMapper dietTempMap "
				+ "INNER JOIN dietTempMap.dishMaster AS dishMaster ") })

@Entity
@Table(name = "m_diet_template_mapper", schema = "kitchen")
@SequenceGenerator(name = "m_diet_template_diet_template_id_seq", sequenceName = "kitchen.m_diet_template_diet_template_id_seq", allocationSize = 1)
public class DietTemplateMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_diet_template_diet_template_id_seq")
	@Column(name = "diet_template_id")
	private Integer dietTemplateId;

	@Column(name = "week_day_id")
	private Integer weekDayMasterId;

	@Column(name = "diet_timing_id")
	private Integer dietTimingMasterId;

	@Column(name = "diet_type_id")
	private Integer dietTypeMasterId;

	@Column(name = "dish_id")
	private Integer dietMasterId;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "dish_quantity")
	private Integer dishQuantity;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "diet_option")
	private String dietOption;

	@Column(name = "status")
	private Character status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diet_timing_id", insertable = false, updatable = false)
	private DietTimingMaster dietTimingMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diet_type_id", insertable = false, updatable = false)
	private DietTypeMaster dietTypeMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dish_id", insertable = false, updatable = false)
	private DishMaster dishMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "week_day_id", insertable = false, updatable = false)
	private WeekDaysMaster weekDaysMaster;

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
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

	public Integer getDietTemplateId() {
		return dietTemplateId;
	}

	public void setDietTemplateId(Integer dietTemplateId) {
		this.dietTemplateId = dietTemplateId;
	}

	public Integer getWeekDayMasterId() {
		return weekDayMasterId;
	}

	public void setWeekDayMasterId(Integer weekDayMasterId) {
		this.weekDayMasterId = weekDayMasterId;
	}

	public Integer getDietTimingMasterId() {
		return dietTimingMasterId;
	}

	public void setDietTimingMasterId(Integer dietTimingMasterId) {
		this.dietTimingMasterId = dietTimingMasterId;
	}

	public Integer getDietTypeMasterId() {
		return dietTypeMasterId;
	}

	public void setDietTypeMasterId(Integer dietTypeMasterId) {
		this.dietTypeMasterId = dietTypeMasterId;
	}

	public Integer getDietMasterId() {
		return dietMasterId;
	}

	public void setDietMasterId(Integer dietMasterId) {
		this.dietMasterId = dietMasterId;
	}

	public Integer getDishQuantity() {
		return dishQuantity;
	}

	public void setDishQuantity(Integer dishQuantity) {
		this.dishQuantity = dishQuantity;
	}

	public String getDietOption() {
		return dietOption;
	}

	public void setDietOption(String dietOption) {
		this.dietOption = dietOption;
	}

	public DietTimingMaster getDietTimingMaster() {
		return dietTimingMaster;
	}

	public void setDietTimingMaster(DietTimingMaster dietTimingMaster) {
		this.dietTimingMaster = dietTimingMaster;
	}

	public DietTypeMaster getDietTypeMaster() {
		return dietTypeMaster;
	}

	public void setDietTypeMaster(DietTypeMaster dietTypeMaster) {
		this.dietTypeMaster = dietTypeMaster;
	}

	public DishMaster getDishMaster() {
		return dishMaster;
	}

	public void setDishMaster(DishMaster dishMaster) {
		this.dishMaster = dishMaster;
	}

	public WeekDaysMaster getWeekDaysMaster() {
		return weekDaysMaster;
	}

	public void setWeekDaysMaster(WeekDaysMaster weekDaysMaster) {
		this.weekDaysMaster = weekDaysMaster;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

}
