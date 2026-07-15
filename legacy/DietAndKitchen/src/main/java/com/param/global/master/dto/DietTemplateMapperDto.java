package com.param.global.master.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DietTemplateMapperDto {

	private Integer dietTemplateId;

	private String weekDay;

	private String dishName;

	private String dietTiming;

	private Integer weekDayMasterId;

	private Integer dietTimingMasterId;

	private Integer dietTypeMasterId;

	private Integer dietMasterId;

	private Integer organizationId;

	private Integer dishQuantity;

	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;

	private String dietOption;

	private Character status;

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

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getDietTemplateId() {
		return dietTemplateId;
	}

	public void setDietTemplateId(Integer dietTemplateId) {
		this.dietTemplateId = dietTemplateId;
	}

	public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}

	public String getDietTiming() {
		return dietTiming;
	}

	public void setDietTiming(String dietTiming) {
		this.dietTiming = dietTiming;
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

	public String getDishName() {
		return dishName;
	}

	public void setDishName(String dishName) {
		this.dishName = dishName;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

}
