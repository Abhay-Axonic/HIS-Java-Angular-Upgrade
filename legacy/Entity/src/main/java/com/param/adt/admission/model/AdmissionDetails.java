package com.param.adt.admission.model;


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
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.AdmissionTypeMaster;
import com.param.adt.master.global.model.BedMaster;
import com.param.adt.master.global.model.OrganizationMaster;
import com.param.adt.master.global.model.ReasonMaster;
import com.param.adt.master.global.model.UnitMaster;
import com.param.adt.master.unit.model.BillingBedCategoryMaster;
import com.param.adt.master.unit.model.FloorMaster;
import com.param.adt.master.unit.model.MealPreferenceMaster;
import com.param.adt.master.unit.model.RoomMaster;
import com.param.adt.master.unit.model.WardMaster;
import com.param.global.model.BedCategoryMaster;
import com.param.opd.coversheet.model.NursingOrderMapper;
@NamedQueries({
	@NamedQuery(name="GET_ADMISSION_DETAILS_BY_ADMISSION_ID",query="SELECT adm.admissionDetailsId as admissionDetailsId, "
			+ "adm.admissionId as admissionId, "
			+ "adm.unitId as unitId, "
			+ "adm.organizationId as organizationId, "
			+ "adm.admissionTypeId as admissionTypeId, "
			+ "adm.floorId as floorId, "
			+ "adm.wardId as wardId, "
			+ "adm.roomId as roomId, "
			+ "adm.bedId as bedId, "
			+ "adm.isHighRisk as isHighRisk, "
			+ "adm.isCritical as isCritical, "
			+ "adm.doa as doa, "
			+ "adm.pdd as pdd, "
			+ "adm.remark as remark, "
			+ "adm.mealPrefId as mealPrefId, "
			+ "adm.attendentMealPrefId as attendentMealPrefId, "
			+ "adm.bedCategoryId as bedCategoryId, "
			+ "adm.billingBedCategoryId as billingBedCategoryId, "
			+ "adm.triageCategoryId as triageCategoryId, "
			+ "adm.erBedTypeAllocation as erBedTypeAllocation, "
			+ "adm.isMedicoLegal as isMedicoLegal, "
			+ "adm.isTransferOfCare as isTransferOfCare, "
			+ "adm.isShortLeave as isShortLeave, "
			+ "adm.isDischarge as isDischarge, "
			+ "adm.nursingAcceptance as nursingAcceptance,"
			+ "adm.isClose as isClose,adm.createdBy as createdBy "
			+ "FROM AdmissionDetails adm "
			+ "WHERE adm.admissionId=:admissionId "
			+ "AND adm.status='A' "
			+ "AND adm.unitId=:unitId "
			+ "AND adm.organizationId=:organizationId "),
})

@NamedNativeQueries({
	@NamedNativeQuery(name="ADMISSION_TAT",query=" select "
			+ " to_char(nursing_acceptance_date_time,'YYYY-MM-DD HH:MI') as \"createdDate\", "
			+ " cast('Nursing Acceptance' as varchar) as \"transaction\" "
			+ " from "
			+ " adt.t_admission_details tad "
			+ " where "
			+ " tad.admission_id =:admissionId "
			+ " and nursing_acceptance = 'Y' "
			+ " and nursing_acceptance_date_time is not null "
			+ " union "
			+ " select "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
			+ " cast('Dishcarge Summery Generated' as varchar) as \"transaction\" "
			+ " from "
			+ " adt.m_discharge_summary "
			+ " where "
			+ " admission_id =:admissionId "
			+ " and is_report_release = 'Y' and status='A' "
			+ " group by "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
			+ " union "
			+ " select "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
			+ " cast('Compalins Added' as varchar) as \"transaction\" "
			+ " from "
			+ " adt.t_complaint_admission "
			+ " where "
			+ " admission_id =:admissionId "
			+ " group by "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
			+ " union "
			+ " select "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
			+ " cast('Discharge Cancelled' as varchar) as \"transaction\" "
			+ " from "
			+ " adt.t_discharge "
			+ " where "
			+ " admission_id =:admissionId "
			+ " and discharge_status_id = 3 "
			+ " group by "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
			+ " union "
			+ " select "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
			+ " cast('Discharged' as varchar) as \"transaction\" "
			+ " from "
			+ " adt.t_discharge "
			+ " where "
			+ " admission_id =:admissionId "
			+ " and discharge_status_id = 2 "
			+ " group by "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
			+ " union "
			+ " select "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
			+ " cast('MEWS' as varchar) as \"transaction\" "
			+ " from "
			+ " adt.t_mews_mapper "
			+ " where "
			+ " admission_id =:admissionId "
			+ " group by "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
			+ " union "
			+ " select "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
			+ " cast('Short Leave Request Accepted' as varchar) as \"transaction\" "
			+ " from "
			+ " adt.t_short_leave "
			+ " where "
			+ " admission_id =:admissionId "
			+ " and short_leave_status_id = 4 "
			+ " group by "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
			+ " union "
			+ " select "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
			+ " cast('Patient retained from short leave' as varchar) as \"transaction\" "
			+ " from "
			+ " adt.t_short_leave "
			+ " where "
			+ " admission_id =:admissionId "
			+ " and short_leave_status_id = 5 "
			+ " group by "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
			+ " union "
			+ " select "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
			+ " cast('Short leave request rejected' as varchar) as \"transaction\" "
			+ " from "
			+ " adt.t_short_leave "
			+ " where "
			+ " admission_id =:admissionId "
			+ " and short_leave_status_id in (7,8,9) "
			+ " group by "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
			+ " union "
			+ " select "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
			+ " cast('Tranfer of care' as varchar) as \"transaction\" "
			+ " from "
			+ " adt.t_transfer_of_care_request "
			+ " where "
			+ " admission_id =:admissionId "
			+ " and transfer_status_id =11 "
			+ " group by "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
			+ " "
			+ " union "
			+ " select "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
			+ " cast('Tranfer of bed' as varchar) as \"transaction\" "
			+ " from "
			+ " adt.t_transfer_request "
			+ " where "
			+ " admission_id =:admissionId "
			+ " and transfer_status_id =9 "
			+ " and transfer_type_id =1 "
			+ " group by "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
			+ " "
			+ " union "
			+ " select "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
			+ " cast('Visitor pass assigned' as varchar) as \"transaction\" "
			+ " from "
			+ " adt.t_visitor_against_patient "
			+ " where "
			+ " admission_id =:admissionId "
			+ " group by "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
			+ " union "
			+ " select "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
			+ " cast('Deposit Collected' as varchar) as \"transaction\" "
			+ " from "
			+ " billing.m_deposit_master "
			+ " where "
			+ " admission_id =:admissionId "
			+ " group by "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
			+ " union "
			+ " select "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
			+ " cast('Pre Auth Approval' as varchar) as \"transaction\" "
			+ " from "
			+ " billing.m_preauth_request_master "
			+ " where "
			+ " admission_id =:admissionId "
			+ " and auth_status = 1 "
			+ " and is_final_submit ='Y' "
			+ " group by "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
			+ " union "
			+ " select "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
			+ " cast('Bill Generated' as varchar) as \"transaction\" "
			+ " from "
			+ " billing.m_billing_master "
			+ " where "
			+ " admission_id =:admissionId "
			+ " and bill_cancelled ='N'"
			+ " group by "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
			+ " union "
			+ " select "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
			+ " cast('Vital captured' as varchar) as \"transaction\" "
			+ " from "
			+ " patient.t_appointment_vital_mapper "
			+ " where "
			+ " admission_id =:admissionId "
			+ " group by "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
			+ " union "
			+ " select "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') as \"createdDate\", "
			+ " cast('Services ordered' as varchar) as \"transaction\" "
			+ " from "
			+ " t_order_master tom "
			+ " where "
			+ " admission_id =:admissionId "
			+ " group by "
			+ " to_char(created_date,'YYYY-MM-DD HH:MI') "
			+ " union "
			+ " select "
			+ " to_char(added_date, 'YYYY-MM-DD HH:MI') as \"createdDate\", "
			+ " cast('Medications prescribed'  as varchar) as \"transaction\" "
			+ " from "
			+ " pharmacy.t_prescription "
			+ " where "
			+ " admission_id =:admissionId "
			+ " group by "
			+ " to_char(added_date, 'YYYY-MM-DD HH:MI') "
			+ " union "
			+ " select "
			+ " to_char(created_date, 'YYYY-MM-DD HH:MI') as \"createdDate\", "
			+ " cast('Document Uploaded' as varchar)  as \"transaction\" "
			+ " from "
			+ " opd.t_encounter_doc_details "
			+ " where "
			+ " admission_id =:admissionId "
			+ " group by "
			+ " to_char(created_date, 'YYYY-MM-DD HH:MI') "
			+ " order by \"createdDate\" ")
})




@Entity
@Table(name="t_admission_details",schema="adt")
@SequenceGenerator(name="admission_details_seq", sequenceName="adt.admission_details_seq",allocationSize=1)
public class AdmissionDetails 
{

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="admission_details_seq")
	@Column(name="admission_details_id")
	private Integer admissionDetailsId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="admission_id")
	private Integer admissionId;
	
	@Column(name="admission_type_id")
	private Integer admissionTypeId;
	
	@Column(name="floor_id")
	private Integer floorId;
	
	@Column(name="ward_id")
	private Integer wardId;
	
	@Column(name="room_id")
	private Integer roomId;
	
	@Column(name="bed_id")
	private Integer bedId;
	
	@Column(name="cancel_by")
	private Integer cancelBy;
	
	@Column(name="cancel_date")
	private Date cancelDate;
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="doa")
	private Date doa;
	
	@Column(name="pdd")
	private Date pdd;
	
	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="meal_pref_id")
	private Integer mealPrefId;
	
	@Column(name="attendent_meal_pref_id")
	private Integer attendentMealPrefId;
	
	@Column(name="bed_category_id")
	private Integer bedCategoryId;
	
	@Column(name="billing_bed_category_id")
	private Integer billingBedCategoryId;
	
	@Column(name="triage_category_id")
	private Integer triageCategoryId;
	
	@Column(name="er_bed_type_allocation")
	private Integer erBedTypeAllocation;
	
	@Column(name="future_discharge_date")
	private Date futureDischargeDate;
	
	@Column(name="extend_reason_id")
	private Integer extendReasonId;
	
	@Column(name="is_medico_legal")
	private Character isMedicoLegal;
	
	@Column(name="is_transfer_of_care")
	private Character isTransferOfCare;
	
	@Column(name="is_short_leave")
	private Character isShortLeave;
	
	@Column(name="is_discharge")
	private Character isDischarge;
	
	@Column(name="is_cancel")
	private Character isCancel;
	
	@Column(name="is_high_risk")
	private Character isHighRisk;
	
	@Column(name="is_critical")
	private Character isCritical;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="is_close")
	private Character isClose;
	
	@Column(name="nursing_acceptance")
	private Character nursingAcceptance;
	
	@Column(name="nursing_acceptance_date_time")
	private Date nursingAcceptanceDateTime;
	
	@Column(name="nursing_rejection_note")
	private String nursingRejectionNote;
	
	@Column(name="nursing_rejection_reason_id")
	private Integer nursingRejectionReasonId;
	
	@Column(name="diet_type")
	private Integer dietType;
	
	@Column(name="remark_for_diet")
	private String remarkForDiet;

	@Column(name = "admission_date_time",updatable = false)
	private Date admissionDateTime;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "extend_reason_id", insertable = false, nullable = false, updatable = false)
	private ReasonMaster reasonMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", insertable = false, nullable = false, updatable = false)
	private OrganizationMaster organizationMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unit_id", insertable = false, nullable = false, updatable = false)
	private UnitMaster unitMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admission_id", insertable = false, nullable = false, updatable = false)
	private Admission admission;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admission_type_id", insertable = false, nullable = false, updatable = false)
	private AdmissionTypeMaster admissionTypeMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ward_id", insertable = false, nullable = false, updatable = false)
	private WardMaster wardMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id", insertable = false, nullable = false, updatable = false)
	private RoomMaster roomMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bed_id", insertable = false, nullable = false, updatable = false)
	private BedMaster bedMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "floor_id", insertable = false, nullable = false, updatable = false)
	private FloorMaster floorMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "meal_pref_id", insertable = false, nullable = false, updatable = false)
	private MealPreferenceMaster mealPreferenceMaster1;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attendent_meal_pref_id", insertable = false, nullable = false, updatable = false)
	private MealPreferenceMaster attendentMealPreferenceMaster1;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bed_category_id", insertable = false, nullable = false, updatable = false)
	private BedCategoryMaster bedCategoryMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "billing_bed_category_id", insertable = false, nullable = false, updatable = false)
	private BillingBedCategoryMaster billingBedCategoryMaster;

	@OneToMany(fetch = FetchType.LAZY,mappedBy = "admissionDetails", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<NursingOrderMapper> nursingOrderMapperlist;
	
	public Integer getNursingRejectionReasonId() {
		return nursingRejectionReasonId;
	}

	public void setNursingRejectionReasonId(Integer nursingRejectionReasonId) {
		this.nursingRejectionReasonId = nursingRejectionReasonId;
	}

	public Date getNursingAcceptanceDateTime() {
		return nursingAcceptanceDateTime;
	}

	public void setNursingAcceptanceDateTime(Date nursingAcceptanceDateTime) {
		this.nursingAcceptanceDateTime = nursingAcceptanceDateTime;
	}

	public Character getNursingAcceptance() {
		return nursingAcceptance;
	}

	public void setNursingAcceptance(Character nursingAcceptance) {
		this.nursingAcceptance = nursingAcceptance;
	}

	public String getNursingRejectionNote() {
		return nursingRejectionNote;
	}

	public void setNursingRejectionNote(String nursingRejectionNote) {
		this.nursingRejectionNote = nursingRejectionNote;
	}

	public Integer getAdmissionDetailsId() {
		return admissionDetailsId;
	}

	public void setAdmissionDetailsId(Integer admissionDetailsId) {
		this.admissionDetailsId = admissionDetailsId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getAdmissionTypeId() {
		return admissionTypeId;
	}

	public void setAdmissionTypeId(Integer admissionTypeId) {
		this.admissionTypeId = admissionTypeId;
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

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getBedId() {
		return bedId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}

	public Integer getCancelBy() {
		return cancelBy;
	}

	public void setCancelBy(Integer cancelBy) {
		this.cancelBy = cancelBy;
	}

	public Date getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getDoa() {
		return doa;
	}

	public void setDoa(Date doa) {
		this.doa = doa;
	}

	public Date getPdd() {
		return pdd;
	}

	public void setPdd(Date pdd) {
		this.pdd = pdd;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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

	public Integer getMealPrefId() {
		return mealPrefId;
	}

	public void setMealPrefId(Integer mealPrefId) {
		this.mealPrefId = mealPrefId;
	}

	public Integer getAttendentMealPrefId() {
		return attendentMealPrefId;
	}

	public void setAttendentMealPrefId(Integer attendentMealPrefId) {
		this.attendentMealPrefId = attendentMealPrefId;
	}

	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Integer getTriageCategoryId() {
		return triageCategoryId;
	}

	public void setTriageCategoryId(Integer triageCategoryId) {
		this.triageCategoryId = triageCategoryId;
	}

	public Integer getErBedTypeAllocation() {
		return erBedTypeAllocation;
	}

	public void setErBedTypeAllocation(Integer erBedTypeAllocation) {
		this.erBedTypeAllocation = erBedTypeAllocation;
	}

	public Date getFutureDischargeDate() {
		return futureDischargeDate;
	}

	public void setFutureDischargeDate(Date futureDischargeDate) {
		this.futureDischargeDate = futureDischargeDate;
	}

	public Integer getExtendReasonId() {
		return extendReasonId;
	}

	public void setExtendReasonId(Integer extendReasonId) {
		this.extendReasonId = extendReasonId;
	}

	public Character getIsMedicoLegal() {
		return isMedicoLegal;
	}

	public void setIsMedicoLegal(Character isMedicoLegal) {
		this.isMedicoLegal = (isMedicoLegal == null) ? 'N' : isMedicoLegal;
	}



	public Character getIsTransferOfCare() {
		return isTransferOfCare;
	}

	public void setIsTransferOfCare(Character isTransferOfCare) {
		
		this.isTransferOfCare = (isTransferOfCare == null) ? 'N' : isTransferOfCare;
	}

	public Character getIsShortLeave() {
		return isShortLeave;
	}

	public void setIsShortLeave(Character isShortLeave) {
		this.isShortLeave = (isShortLeave == null) ? 'N' : isShortLeave;
	}

	public Character getIsDischarge() {
		return isDischarge;
	}

	public void setIsDischarge(Character isDischarge) {
		this.isDischarge = (isDischarge == null) ? 'N' : isDischarge;
	}

	public Character getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(Character isCancel) {
		this.isCancel = (isCancel == null) ? 'N' : isCancel;
	}

	public Character getIsHighRisk() {
		return isHighRisk;
	}

	public void setIsHighRisk(Character isHighRisk) {
		this.isHighRisk = (isHighRisk == null) ? 'N' : isHighRisk;
	}

	public Character getIsCritical() {
		return isCritical;
	}

	public void setIsCritical(Character isCritical) {
		this.isCritical = (isCritical == null) ? 'N' : isCritical;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = (status == null) ? 'A' : status;
	}

	public Character getIsClose() {
		return isClose;
	}

	public void setIsClose(Character isClose) {
		this.isClose = (isClose == null) ? 'N' : isClose;

	}

	public Integer getDietType() {
		return dietType;
	}

	public void setDietType(Integer dietType) {
		this.dietType = dietType;
	}

	public String getRemarkForDiet() {
		return remarkForDiet;
	}

	public void setRemarkForDiet(String remarkForDiet) {
		this.remarkForDiet = remarkForDiet;
	}

	public Date getAdmissionDateTime() {
		return admissionDateTime;
	}

	public void setAdmissionDateTime(Date admissionDateTime) {
		this.admissionDateTime = admissionDateTime;
	}
}
