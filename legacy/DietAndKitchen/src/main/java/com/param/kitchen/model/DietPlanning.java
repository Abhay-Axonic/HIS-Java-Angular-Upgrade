package com.param.kitchen.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.param.global.master.model.MealMaster;
import com.param.global.master.model.WeekDaysMaster;
import com.param.opd.coversheet.model.DietTypeMaster;

/*@NamedQueries({
		@NamedQuery(name = "DIET_PLANNING.findAll", query = "SELECT dietPlanning.id as id,"
				+ "dietPlanning.patientId as patientId, dietPlanning.status as status,"
				+ "dietPlanning.dietPreference as dietPreference," + "dietPlanning.dietTypeId as dietTypeId,"
				+ "dietPlanning.createdBy as createdBy, to_char(dietPlanning.createdDate,'dd-MM-yyyy' )as createdDate,"
				+ "dietPlanning.updatedBy as updatedBy,to_char(dietPlanning.updatedDate,'dd-MM-yyyy') as updatedDate,"
				+ " to_char(dietPlanning.dietDate,'dd-MM-yyyy') as dietDate,dietPlanning.dietDay as dietDay,"
				+ "dietPlanning.mealId as mealId " + "from DietPlanning dietPlanning WHERE status='A'"),
		@NamedQuery(name = "DIET_PLANNING.GET_BY_PATIENT_ID", query = "SELECT dietPlanning.id as id,"
				+ "dietPlanning.patientId as patientId, dietPlanning.status as status,"
				+ "dietPlanning.dietPreference as dietPreference," + "dietPlanning.dietTypeId as dietTypeId,"
				+ "dietPlanning.createdBy as createdBy, to_char(dietPlanning.createdDate,'dd-MM-yyyy' )as createdDate,"
				+ "dietPlanning.updatedBy as updatedBy,to_char(dietPlanning.updatedDate,'dd-MM-yyyy') as updatedDate,"
				+ " to_char(dietPlanning.dietDate,'dd-MM-yyyy') as dietDate,dietPlanning.dietDay as dietDay,"
				+ "dietPlanning.mealId as mealId "
				+ "from DietPlanning dietPlanning WHERE status='A' AND patientId=:patientId "),
		@NamedQuery(name = "DIET_PLANNING.GET_BY_DIET_DATE", query = "SELECT dietPlanning.id as id,"
				+ "dietPlanning.patientId as patientId, dietPlanning.status as status,"
				+ "dietPlanning.dietPreference as dietPreference," + "dietPlanning.dietTypeId as dietTypeId,"
				+ "dietPlanning.createdBy as createdBy, to_char(dietPlanning.createdDate,'dd-MM-yyyy' )as createdDate,"
				+ "dietPlanning.updatedBy as updatedBy,to_char(dietPlanning.updatedDate,'dd-MM-yyyy') as updatedDate,"
				+ " to_char(dietPlanning.dietDate,'dd-MM-yyyy') as dietDate,dietPlanning.dietDay as dietDay,"
				+ "dietPlanning.mealId as mealId "
				+ "from DietPlanning dietPlanning WHERE status='A' AND dietDate=:dietDate "),
		

		@NamedQuery(name = "DIET_PLANNING.GET_DIET_PREFERENCE_BY_PATIENT_ID", query = "SELECT dietPlanning.id as id,"
				+ "dietPlanning.patientId as patientId, dietPlanning.status as status,"
				+ "dietPlanning.dietPreference as dietPreference," + "dietPlanning.dietTypeId as dietTypeId,"
				+ "dietPlanning.createdBy as createdBy, to_char(dietPlanning.createdDate,'dd-MM-yyyy' )as createdDate,"
				+ "dietPlanning.updatedBy as updatedBy,to_char(dietPlanning.updatedDate,'dd-MM-yyyy') as updatedDate,"
				+ " to_char(dietPlanning.dietDate,'dd-MM-yyyy') as dietDate,dietPlanning.dietDay as dietDay,"
				+ "dietPlanning.mealId as mealId "
				+ "from DietPlanning dietPlanning WHERE status='A' AND patientId=:patientId ")


})*/



@Entity
@Table(name = "diet_planning", schema = "kitchen")
@SequenceGenerator(name = "diet_planning_id_seq", sequenceName = "kitchen.diet_planning_id_seq", allocationSize = 1)
public class DietPlanning {

	@Id
	@Column(name = "diet_planning_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "diet_planning_id_seq")
	private Integer dietPlanningId;

	@Column(name = "patient_id")
	private Integer patientId;

	@Column(name = "diet_type_id")
	private Integer dietTypeId;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diet_type_id", insertable = false, updatable = false)
	private DietTypeMaster dietTypeMaster;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "dietPlanning")
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST })
	private List<DietOrder> dietOrderList;

	public Integer getDietPlanningId() {
		return dietPlanningId;
	}

	public void setDietPlanningId(Integer dietPlanningId) {
		this.dietPlanningId = dietPlanningId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getDietTypeId() {
		return dietTypeId;
	}

	public void setDietTypeId(Integer dietTypeId) {
		this.dietTypeId = dietTypeId;
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

	public DietTypeMaster getDietTypeMaster() {
		return dietTypeMaster;
	}

	public void setDietTypeMaster(DietTypeMaster dietTypeMaster) {
		this.dietTypeMaster = dietTypeMaster;
	}

	public List<DietOrder> getDietOrderList() {
		return dietOrderList;
	}

	public void setDietOrderList(List<DietOrder> dietOrderList) {
		this.dietOrderList = dietOrderList;
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

	

}
