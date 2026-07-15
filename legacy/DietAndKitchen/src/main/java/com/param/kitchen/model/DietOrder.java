package com.param.kitchen.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.BedMaster;
import com.param.adt.master.unit.model.FloorMaster;
import com.param.adt.master.unit.model.MealPreferenceMaster;
import com.param.adt.master.unit.model.WardMaster;
import com.param.global.master.model.DishMaster;
import com.param.global.master.model.MealMaster;
import com.param.opd.coversheet.model.DietTypeMaster;

@NamedNativeQueries({
	@NamedNativeQuery(name="GET_ADMITTED_PATIENT_LIST_BY_PATIENT_ID" , query= 
			"SELECT admission.admission_id as \"admissionId\", "
			+ "admission.patient_id as \"patientId\", " 
			+ "admission.t_patient_id as \"tPatientId\", "
			+ "admission.doctor_id as \"doctorId\", "
			+ "concat(patient.first_name,' ',patient.last_name) as \"patientName\", "
			+ "to_char(patient.dob,'dd/MM/yyyy') as \"dob\", " 
			+ "gender.gender_code as \"genderName\", "
			+ "patient.uhid_number as \"UHIDNumber\", "
			+ "patient.image_path as \"imagePath\", "
			+ "patient.gender_id as \"genderId\", "
			+ "bm.bed_number as \"bedNumber\", "
			+ "bm.bed_id as \"bedId\", "
			+ "bm.ward_id as \"wardId\", "
			+ "bm.floor_id as \"floorId\", "
			+ "bm.room_id as \"roomId\", "
			+ "admissionDetails.bed_category_id as \"bedCategoryId\", "
			+ "admissionDetails.billing_bed_category_id as \"billingBedCategoryId\", "
			+ "ward.ward_name as \"wardName\", "
			+ "concat(doctor.first_name,' ',doctor.middle_name,' ',doctor.last_name) as \"dFirstName\","
			+ "to_char(admissionDetails.doa,'dd/MM/yyyy') as \"doa\" " 
			+ "FROM  adt.t_admission admission "
			+ "INNER join patient.t_patient_registration patient on admission.patient_id=patient.patient_id "
			+ "INNER join public.m_gender_master gender on patient.gender_id=gender.gender_id "
			+ "INNER join doctor.m_doctor_master doctor on admission.doctor_id=doctor.doctor_id "
			+ "INNER join adt.t_admission_details admissionDetails on admission.admission_id=admissionDetails.admission_id "
			+ "INNER join adt.m_bed_master bm on admissionDetails.bed_id=bm.bed_id "
			+ "INNER JOIN adt.m_ward_master ward on bm.ward_id=ward.ward_id "
			+ "WHERE admission.patient_id =:patientId "
			+ "AND admission.status='A' "
			+ "AND admissionDetails.status='A' "
			+ "AND admission.organization_id=:organizationId "
			+ "AND admission.unit_id=:unitId ")
})
@NamedQueries({
	
		/*@NamedQuery(name = "DIET_ORDER.GET_BY_MULTIPLE_FIELDS", query = "SELECT dishMaster.dishDescription as dishName,"
				+ " sum(dietOrder.quantity) as quantity from DietOrder dietOrder "
				+ " INNER JOIN dietOrder.dishMaster as dishMaster "
				+ " WHERE dietOrder.status='A' AND dietOrder.mealId=:mealId"
				+ " AND to_char(dietOrder.dietDate, 'dd-MM-yyyy')=:dietDate AND dietOrder.floorId=:floorId"
				+ " AND dietOrder.wardId=:wardId AND dietOrder.isIssued=FALSE AND dietOrder.isRejected=FALSE "
				+ " GROUP BY dishMaster.dishDescription "),*/
	@NamedQuery(name = "DIET_ORDER.GET_BY_MULTIPLE_FIELDS", query = "SELECT dietOrder.orderId as orderId, dietOrder.patientId as patientId,"
			+ " dietOrder.floorId as floorId, dietOrder.wardId as wardId,dietOrder.bedId as bedId, "
			+ " dietOrder.mealId as mealId, dishMaster.dishDescription as dishName, dietOrder.quantity as quantity,"
			+ " dietOrder.status as status "
			+ " from DietOrder dietOrder INNER JOIN dietOrder.dishMaster as dishMaster WHERE dietOrder.status='A' AND dietOrder.mealId=:mealId"
			+ " AND to_char(dietOrder.dietDate, 'dd-MM-yyyy')=:dietDate AND dietOrder.floorId=:floorId"
			+ " AND dietOrder.wardId=:wardId AND dietOrder.isIssued=FALSE AND dietOrder.isRejected=FALSE "),
		@NamedQuery(name = "DIET_ORDER.GET_DIET_ISSUE_SCREEN", query = "SELECT dietOrder.orderId as orderId, dietOrder.patientId as patientId,"
				+ " dietOrder.floorId as floorId, dietOrder.wardId as wardId,dietOrder.bedId as bedId, "
				+ " dietOrder.mealId as mealId, dishMaster.dishDescription as dishName, dietOrder.quantity as quantity,"
				+ " dietOrder.status as status "
				+ " from DietOrder dietOrder INNER JOIN dietOrder.dishMaster as dishMaster WHERE dietOrder.status='A' AND dietOrder.mealId=:mealId"
				+ " AND to_char(dietOrder.dietDate, 'dd-MM-yyyy')=:dietDate AND dietOrder.floorId=:floorId"
				+ " AND dietOrder.wardId=:wardId AND dietOrder.isIssued=FALSE AND dietOrder.isRejected=FALSE "),
		@NamedQuery(name = "DIET_ORDER.GET_DIET_ACCEPTANCE_SCREEN", query = "SELECT dietOrder.orderId as orderId, dietOrder.patientId as patientId,"
				+ " dietOrder.floorId as floorId, dietOrder.wardId as wardId,dietOrder.bedId as bedId, "
				+ " dietOrder.mealId as mealId, dishMaster.dishDescription as dishName, dietOrder.quantity as quantity,"
				+ " dietOrder.status as status "
				+ " from DietOrder dietOrder INNER JOIN dietOrder.dishMaster as dishMaster WHERE dietOrder.status='A' AND dietOrder.mealId=:mealId"
				+ " AND to_char(dietOrder.dietDate, 'dd-MM-yyyy')=:dietDate AND dietOrder.floorId=:floorId"
				+ " AND dietOrder.wardId=:wardId AND dietOrder.isIssued=TRUE AND dietOrder.isRejected=FALSE "),
		@NamedQuery(name = "DIET_ORDER.GET_REJECTED_DIET_SCREEN", query = "SELECT dietOrder.orderId as orderId, dietOrder.patientId as patientId,"
				+ " dietOrder.floorId as floorId, dietOrder.wardId as wardId,dietOrder.bedId as bedId, "
				+ " dietOrder.mealId as mealId, dishMaster.dishDescription as dishName, dietOrder.quantity as quantity,"
				+ " dietOrder.status as status,"
				+ " dietOrder.remarks as remarks from DietOrder dietOrder INNER JOIN dietOrder.dishMaster as dishMaster WHERE dietOrder.status='A' AND dietOrder.mealId=:mealId"
				+ " AND to_char(dietOrder.dietDate, 'dd-MM-yyyy')=:dietDate AND dietOrder.floorId=:floorId"
				+ " AND dietOrder.wardId=:wardId AND dietOrder.isIssued=TRUE AND dietOrder.isRejected=TRUE "),
		@NamedQuery(name = "DIET_ORDER.GET_BY_DIET_PLANNING_ID", query = "SELECT dietOrder.orderId as orderId,"
				+ " dietOrder.dishId as dishId, dishMaster.dishDescription as dishName, dietOrder.quantity as quantity"
				+ " from DietOrder dietOrder INNER JOIN dietOrder.dishMaster as dishMaster WHERE dietOrder.status='A' AND dietOrder.isIssued=FALSE "
				+ " AND dietOrder.dietPlanningId=:dietPlanningId")

})



@Entity
@Table(name = "diet_order", schema = "kitchen")
@SequenceGenerator(name = "diet_order_id_seq", sequenceName = "kitchen.diet_order_id_seq", allocationSize = 1)
public class DietOrder {

	@Id
	@Column(name = "order_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "diet_order_id_seq")
	private Integer orderId;
	
	@Column(name = "patient_id")
	private Integer patientId;

	@Column(name = "floor_id")
	private Integer floorId;

	@Column(name = "ward_id")
	private Integer wardId;
	
	@Column(name = "bed_id")
	private Integer bedId;
	
	@Column(name = "diet_type_id")
	private Integer dietTypeId;
	
	@Column(name = "diet_preference_id")
	private Integer dietPreferenceId;
	
	@Column(name = "diet_planning_id")
	private Integer dietPlanningId;
	
	@Column(name = "meal_id")
	private Integer mealId;
	
	@Column(name = "day_id")
	private Integer dayId;
	
	@Column(name = "diet_date")
	private Date dietDate;
	
	@Column(name = "dish_id")
	private Integer dishId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "is_issued", insertable = false)
	private Boolean isIssued;
	
	@Column(name = "is_rejected", insertable = false)
	private Boolean isRejected;
	
	@Column(name = "is_accepted" , insertable = false)
	private Boolean isAccepted;
	
	@Column(name = "rejection_status")
	private Boolean rejectionStatus;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "meal_notes")
	private String mealNotes;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dish_id", insertable = false, updatable = false)
	private DishMaster dishMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "diet_planning_id", insertable = false, nullable = false, updatable = false)
	private DietPlanning dietPlanning;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "floor_id", insertable = false, updatable = false)
	private FloorMaster floorMaster;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ward_id", insertable = false, updatable = false)
	private WardMaster wardMaster;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "meal_id", insertable = false, updatable = false)
	private MealMaster mealMaster;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bed_id", insertable = false, updatable = false)
	private BedMaster bedMaster;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diet_type_id", insertable = false, updatable = false)
	private DietTypeMaster dietTypeMaster;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diet_preference_id", insertable = false, updatable = false)
	private MealPreferenceMaster dietPreferenceMaster;
	
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getFloorId() {
		return floorId;
	}

	public void setFloorId(Integer floorId) {
		this.floorId = floorId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Integer getBedId() {
		return bedId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}

	public Integer getDietPlanningId() {
		return dietPlanningId;
	}

	public void setDietPlanningId(Integer dietPlanningId) {
		this.dietPlanningId = dietPlanningId;
	}

	public Integer getMealId() {
		return mealId;
	}

	public void setMealId(Integer mealId) {
		this.mealId = mealId;
	}

	public Date getDietDate() {
		return dietDate;
	}

	public void setDietDate(Date dietDate) {
		this.dietDate = dietDate;
	}

	public Integer getDishId() {
		return dishId;
	}

	public void setDishId(Integer dishId) {
		this.dishId = dishId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Boolean getIsIssued() {
		return isIssued;
	}

	public void setIsIssued(Boolean isIssued) {
		this.isIssued = isIssued;
	}

	public Boolean getIsRejected() {
		return isRejected;
	}

	public void setIsRejected(Boolean isRejected) {
		this.isRejected = isRejected;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public DishMaster getDishMaster() {
		return dishMaster;
	}

	public void setDishMaster(DishMaster dishMaster) {
		this.dishMaster = dishMaster;
	}

	public DietPlanning getDietPlanning() {
		return dietPlanning;
	}

	public void setDietPlanning(DietPlanning dietPlanning) {
		this.dietPlanning = dietPlanning;
	}

	public FloorMaster getFloorMaster() {
		return floorMaster;
	}

	public void setFloorMaster(FloorMaster floorMaster) {
		this.floorMaster = floorMaster;
	}

	public WardMaster getWardMaster() {
		return wardMaster;
	}

	public void setWardMaster(WardMaster wardMaster) {
		this.wardMaster = wardMaster;
	}

	public MealMaster getMealMaster() {
		return mealMaster;
	}

	public void setMealMaster(MealMaster mealMaster) {
		this.mealMaster = mealMaster;
	}

	public Integer getDayId() {
		return dayId;
	}

	public void setDayId(Integer dayId) {
		this.dayId = dayId;
	}

	public Integer getDietTypeId() {
		return dietTypeId;
	}

	public void setDietTypeId(Integer dietTypeId) {
		this.dietTypeId = dietTypeId;
	}

	public Integer getDietPreferenceId() {
		return dietPreferenceId;
	}

	public void setDietPreferenceId(Integer dietPreferenceId) {
		this.dietPreferenceId = dietPreferenceId;
	}

	public DietTypeMaster getDietTypeMaster() {
		return dietTypeMaster;
	}

	public void setDietTypeMaster(DietTypeMaster dietTypeMaster) {
		this.dietTypeMaster = dietTypeMaster;
	}

	public MealPreferenceMaster getDietPreferenceMaster() {
		return dietPreferenceMaster;
	}

	public void setDietPreferenceMaster(MealPreferenceMaster dietPreferenceMaster) {
		this.dietPreferenceMaster = dietPreferenceMaster;
	}

	public BedMaster getBedMaster() {
		return bedMaster;
	}

	public void setBedMaster(BedMaster bedMaster) {
		this.bedMaster = bedMaster;
	}

	public Boolean getIsAccepted() {
		return isAccepted;
	}

	public void setIsAccepted(Boolean isAccepted) {
		this.isAccepted = isAccepted;
	}

	public Boolean getRejectionStatus() {
		return rejectionStatus;
	}

	public void setRejectionStatus(Boolean rejectionStatus) {
		this.rejectionStatus = rejectionStatus;
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
	public String getMealNotes() {
		return mealNotes;
	}

	public void setMealNotes(String mealNotes) {
		this.mealNotes = mealNotes;
	}
	
	
}