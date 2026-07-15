package com.param.opd.coversheet.model;

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

import com.param.adt.master.unit.model.MealPreferenceMaster;
@NamedQueries({
	
	@NamedQuery(name="GET_IPD_DIET_ORDER_LIST",
		query=" SELECT  ipdDietOrder.ipdDietOrderId AS ipdDietOrderId, "
				+ " ipdDietOrder.doctorId AS doctorId, "
				+ " ipdDietOrder.admissionId AS admissionId, "
				+ " ipdDietOrder.patientId as patientId, "
				+ " ipdDietOrder.dietStatus as dietStatus, "
				+ " ipdDietOrder.patientId AS patientId, "
                + " to_char(ipdDietOrder.dietStartDate, 'dd/mm/yyyy') AS dietStartDate, "
		        + " to_char(ipdDietOrder.dietStopDate, 'dd/mm/yyyy') AS dietStopDate, "
				+ " ipdDietOrder.dietInstruction AS dietInstruction, "
				+ " ipdDietOrder.dietMealPreferenceId AS dietMealPreferenceId, "
				+ " ipdDietOrder.dietTypeId AS dietTypeId, "
				+ " ipdDietOrder.isNbm AS isNbm, "
				+ " ipdDietOrder.organisationId AS organisationId,"
				+ " ipdDietOrder.unitId AS unitId,"
				+ " ipdDietOrder.status AS status,"
				+ " mealPreferenceMaster.mealPreferenceDesc as mealPreferenceDesc, "
				+ " dietTypeMaster.dietTypeDescription as dietTypeDescription "
				+ " FROM IpdDietOrder ipdDietOrder "
				+ " LEFT JOIN ipdDietOrder.mealPreferenceMaster mealPreferenceMaster"
				+ " LEFT JOIN ipdDietOrder.dietTypeMaster dietTypeMaster"
				+ " WHERE ipdDietOrder.organisationId= :organisationId"
				+ " AND ipdDietOrder.unitId= :unitId"
				+ " AND ipdDietOrder.patientId= :patientId"
				+ " ORDER BY ipdDietOrder.ipdDietOrderId asc "
				)
})
@Entity
@Table(name = "t_ipd_diet_order", schema = "adt")
@SequenceGenerator(name = "t_ipd_diet_order_seq", sequenceName = "adt.t_ipd_diet_order_seq", allocationSize = 1)
public class IpdDietOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_ipd_diet_order_seq")
	
	@Column(name = "ipd_diet_order_id")
	private Integer ipdDietOrderId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "diet_status")
	private String dietStatus;
	
	@Column(name = "diet_start_date")
	private Date dietStartDate;

	@Column(name = "diet_stop_date")
	private Date dietStopDate;
	
	@Column(name = "diet_instruction")
	private String dietInstruction;
	
	@Column(name = "diet_meal_preference_id")
	private Integer dietMealPreferenceId;
	
	@Column(name = "is_nbm")
	private Character isNbm;
	
	@Column(name = "organisation_id")
	private Integer organisationId;
	
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

	@Column(name = "diet_type_id")
	private Integer dietTypeId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="diet_meal_preference_id", insertable=false, updatable=false)
	private MealPreferenceMaster mealPreferenceMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="diet_type_id", insertable=false, updatable=false)
	private DietTypeMaster dietTypeMaster;
	
	public Integer getIpdDietOrderId() {
		return ipdDietOrderId;
	}

	public void setIpdDietOrderId(Integer ipdDietOrderId) {
		this.ipdDietOrderId = ipdDietOrderId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getDietStatus() {
		return dietStatus;
	}

	public void setDietStatus(String dietStatus) {
		this.dietStatus = dietStatus;
	}

	public Date getDietStartDate() {
		return dietStartDate;
	}

	public void setDietStartDate(Date dietStartDate) {
		this.dietStartDate = dietStartDate;
	}

	public Date getDietStopDate() {
		return dietStopDate;
	}

	public void setDietStopDate(Date dietStopDate) {
		this.dietStopDate = dietStopDate;
	}

	public String getDietInstruction() {
		return dietInstruction;
	}

	public void setDietInstruction(String dietInstruction) {
		this.dietInstruction = dietInstruction;
	}

	public Integer getDietMealPreferenceId() {
		return dietMealPreferenceId;
	}

	public void setDietMealPreferenceId(Integer dietMealPreferenceId) {
		this.dietMealPreferenceId = dietMealPreferenceId;
	}

	public Character getIsNbm() {
		return isNbm;
	}

	public void setIsNbm(Character isNbm) {
		this.isNbm = isNbm;
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

	public Integer getDietTypeId() {
		return dietTypeId;
	}

	public void setDietTypeId(Integer dietTypeId) {
		this.dietTypeId = dietTypeId;
	}

	
	
}
