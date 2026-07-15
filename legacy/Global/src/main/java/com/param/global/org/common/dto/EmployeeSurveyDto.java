package com.param.global.org.common.dto;

import java.util.List;

public class EmployeeSurveyDto {
	
	 	private Integer employeeSurveyId;
		
		List <EmployeeSurveyListDto> satisfactionList;
		
		private Integer organizationId;
		
		private Integer unitId;
		
		private Character status;
		
		private Integer createdBy;
		
		private String createdDate;
		
		private Integer updatedBy;
		
		private Integer userId;
		
		private String updatedDate;
		
		private String rating;

		public Integer getEmployeeSurveyId() {
			return employeeSurveyId;
		}

		public void setEmployeeSurveyId(Integer employeeSurveyId) {
			this.employeeSurveyId = employeeSurveyId;
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

		public Integer getUserId() {
			return userId;
		}

		public void setUserId(Integer userId) {
			this.userId = userId;
		}

		public String getUpdatedDate() {
			return updatedDate;
		}

		public void setUpdatedDate(String updatedDate) {
			this.updatedDate = updatedDate;
		}

		public List<EmployeeSurveyListDto> getSatisfactionList() {
			return satisfactionList;
		}

		public void setSatisfactionList(List<EmployeeSurveyListDto> satisfactionList) {
			this.satisfactionList = satisfactionList;
		}

		public String getRating() {
			return rating;
		}

		public void setRating(String rating) {
			this.rating = rating;
		}

}
