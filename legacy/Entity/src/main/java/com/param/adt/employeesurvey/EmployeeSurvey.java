package com.param.adt.employeesurvey;

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
	
	@NamedQuery(name="GET_EMPLOYEE_SURVEY_BY_ID",
		    query="SELECT employeeSurvey.pointId AS pointId ,"
		    		+ " employeeSurvey.employeeSurveyId AS employeeSurveyId, "
		        + " employeeSurvey.selectedOption AS selectedOption, "
		        + " employeeSurvey.rating AS rating, "
		        + " employeeSurvey.userId AS userId, "
		        + " employeeSurvey.organizationId AS organizationId, "
		        + " employeeSurvey.unitId AS unitId, "
		        + " employeeSurvey.status AS status "
		        + " FROM EmployeeSurvey employeeSurvey "
		        + " WHERE employeeSurvey.status='A'"
		        + " AND employeeSurvey.userId= :userId"
		        + " AND employeeSurvey.organizationId= :organizationId "
		        + " AND employeeSurvey.unitId= :unitId "
		),

	
	
})


@Entity
@Table(name = "t_employee_survey", schema = "public")
@SequenceGenerator(name = "t_employee_survey_seq", sequenceName = "public.t_employee_survey_seq", allocationSize = 1)
public class EmployeeSurvey {

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_employee_survey_seq")
	@Column(name = "employee_survey_id")
	private Integer employeeSurveyId;
	
	@Column(name = "point_id")
	private Integer pointId;
	
	@Column(name = "selected_option")
	private String selectedOption;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
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
	
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "rating")
	private String rating;

	public Integer getEmployeeSurveyId() {
		return employeeSurveyId;
	}

	public void setEmployeeSurveyId(Integer employeeSurveyId) {
		this.employeeSurveyId = employeeSurveyId;
	}

	public Integer getPointId() {
		return pointId;
	}

	public void setPointId(Integer pointId) {
		this.pointId = pointId;
	}

	public String getSelectedOption() {
		return selectedOption;
	}

	public void setSelectedOption(String selectedOption) {
		this.selectedOption = selectedOption;
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	
}
