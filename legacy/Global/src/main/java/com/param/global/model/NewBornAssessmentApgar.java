package com.param.global.model;

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
		@NamedQuery(name = "GET_NEW_BORN_APGAR_DATA", query = "SELECT apgar.apgarId as apgarId, apgar.totalScore as totalScore, "
				+ "apgar.colorScore as colorScore, apgar.heartRateScore as heartRateScore, "
				+ "apgar.reflexScore as reflexScore, apgar.muscleScore as muscleScore, "
				+ "apgar.respirationScore as respirationScore, apgar.apgarRemark as apgarRemark, "
				+ "apgar.time as time, apgar.status as status "
				+ "FROM NewBornAssessmentApgar as apgar "
				+ "WHERE apgar.status=:status AND apgar.organizationId=:organizationId "
				+ "AND apgar.unitId=:unitId AND apgar.assessmentId=:assessmentId "
				+ "ORDER BY apgar.time ASC"),

		/*@NamedQuery(name = "GET_AMBULANCE_TARRIF_LIST_BY_NAME", query = "SELECT ambTarrifMaster.ambulanceTarrifMasterId as ambulanceTarrifMasterId, ambTarrifMaster.ambulanceType as ambulanceType, "
				+ "ambTarrifMaster.ambulanceProvider as ambulanceProvider, ambTarrifMaster.ambulanceOutSrcVender as ambulanceOutSrcVender,"
				+ "ambTarrifMaster.ambulanceFirstDist as ambulanceFirstDist, ambTarrifMaster.ambulanceFirstDistRate as ambulanceFirstDistRate, "
				+ "ambTarrifMaster.ambulanceAddMileage as ambulanceAddMileage, ambTarrifMaster.ambulanceAddMileageRate as ambulanceAddMileageRate, "
				+ "ambTarrifMaster.status as status "
				+ "FROM AmbulanceTarrif as ambTarrifMaster "
				+ "WHERE (LOWER(ambTarrifMaster.ambulanceType)=:ambulanceType OR ambTarrifMaster.ambulanceType=:ambulanceType) "
				+ "AND (LOWER(ambTarrifMaster.ambulanceOutSrcVender)=:ambulanceOutSrcVender OR ambTarrifMaster.ambulanceOutSrcVender=:ambulanceOutSrcVender) "
				+ "AND ambTarrifMaster.organizationId=:organizationId "),
		
		@NamedQuery(name = "GET_AMBULANCE_TARRIF_LIST_BY_ID", query ="SELECT ambTarrifMaster.ambulanceTarrifMasterId as ambulanceTarrifMasterId, ambTarrifMaster.ambulanceType as ambulanceType, "
				+ "ambTarrifMaster.ambulanceProvider as ambulanceProvider, ambTarrifMaster.ambulanceOutSrcVender as ambulanceOutSrcVender,"
				+ "ambTarrifMaster.ambulanceFirstDist as ambulanceFirstDist, ambTarrifMaster.ambulanceFirstDistRate as ambulanceFirstDistRate, "
				+ "ambTarrifMaster.ambulanceAddMileage as ambulanceAddMileage, ambTarrifMaster.ambulanceAddMileageRate as ambulanceAddMileageRate, "
				+ "ambTarrifMaster.status as status "
				+ "FROM AmbulanceTarrif as ambTarrifMaster WHERE ambTarrifMaster.ambulanceTarrifMasterId=:ambulanceTarrifMasterId "
				+ "and ambTarrifMaster.organizationId=:organizationId"),

		@NamedQuery(name = "GET_ACTIVE_AMBULANCE_TARRIF_LIST", query = "SELECT ambTarrifMaster.ambulanceTarrifMasterId as ambulanceTarrifMasterId, ambTarrifMaster.ambulanceType as ambulanceType, "
				+ "ambTarrifMaster.ambulanceProvider as ambulanceProvider, ambTarrifMaster.ambulanceOutSrcVender as ambulanceOutSrcVender,"
				+ "ambTarrifMaster.ambulanceFirstDist as ambulanceFirstDist, ambTarrifMaster.ambulanceFirstDistRate as ambulanceFirstDistRate, "
				+ "ambTarrifMaster.ambulanceAddMileage as ambulanceAddMileage, ambTarrifMaster.ambulanceAddMileageRate as ambulanceAddMileageRate "
				+ "FROM AmbulanceTarrif as ambTarrifMaster "
				+ "WHERE ambTarrifMaster.status='A' "
				+ "AND ambTarrifMaster.organizationId=:organizationId "
				+ "ORDER BY ambTarrifMaster.ambulanceType "),
		
		@NamedQuery(name = "GET_AMBULANCE_TARRIF_SINGLE_REC", query ="SELECT ambTarrifMaster.ambulanceTarrifMasterId as ambulanceTarrifMasterId, ambTarrifMaster.ambulanceType as ambulanceType, "
				+ "ambTarrifMaster.ambulanceProvider as ambulanceProvider, ambTarrifMaster.ambulanceOutSrcVender as ambulanceOutSrcVender,"
				+ "ambTarrifMaster.ambulanceFirstDist as ambulanceFirstDist, ambTarrifMaster.ambulanceFirstDistRate as ambulanceFirstDistRate, "
				+ "ambTarrifMaster.ambulanceAddMileage as ambulanceAddMileage, ambTarrifMaster.ambulanceAddMileageRate as ambulanceAddMileageRate, "
				+ "ambTarrifMaster.status as status "
				+ "FROM AmbulanceTarrif as ambTarrifMaster " + "WHERE ambTarrifMaster.organizationId=:organizationId "
				+ " AND ambTarrifMaster.ambulanceTarrifMasterId=:ambulanceTarrifMasterId"),*/
				
})

@Entity
@Table(name = "m_new_born_assessment_apgar", schema = "adt")
@SequenceGenerator(name = "m_new_born_assessment_apgar_seq", sequenceName = "adt.m_new_born_assessment_apgar_seq", allocationSize = 1)
public class NewBornAssessmentApgar {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_new_born_assessment_apgar_seq")
	@Column(name = "m_new_born_assessment_apgar_id")
	private int apgarId;
	
	@Column(name = "m_new_born_assessment_id")
	private int assessmentId;

	@Column(name = "remark")
	private String apgarRemark;
	
	@Column(name = "color_score")
	private String colorScore;

	@Column(name = "heart_rate_score")
	private String heartRateScore;

	@Column(name = "reflex_score")
	private String reflexScore;

	@Column(name = "muscle_score")
	private String muscleScore;

	@Column(name = "respiration_score")
	private String respirationScore;

	@Column(name = "total_score")
	private int totalScore;
	
	@Column(name = "time_after")
	private int time;
	
	@Column(name = "status")
	private char status;

	@Column(name = "created_by")
	private int createdBy;

	@Column(name = "created_date")
	private String createdDate;

	@Column(name = "updated_by")
	private int updatedBy;

	@Column(name = "updated_date")
	private String updatedDate;

	@Column(name="organization_id")
	private int organizationId;
	
	@Column(name="unit_id")
	private int unitId;
	

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public int getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	public int getApgarId() {
		return apgarId;
	}

	public void setApgarId(int apgarId) {
		this.apgarId = apgarId;
	}

	public int getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(int assessmentId) {
		this.assessmentId = assessmentId;
	}

	public String getApgarRemark() {
		return apgarRemark;
	}

	public void setApgarRemark(String apgarRemark) {
		this.apgarRemark = apgarRemark;
	}

	
	public String getColorScore() {
		return colorScore;
	}

	public void setColorScore(String colorScore) {
		this.colorScore = colorScore;
	}

	public String getHeartRateScore() {
		return heartRateScore;
	}

	public void setHeartRateScore(String heartRateScore) {
		this.heartRateScore = heartRateScore;
	}

	public String getReflexScore() {
		return reflexScore;
	}

	public void setReflexScore(String reflexScore) {
		this.reflexScore = reflexScore;
	}

	public String getMuscleScore() {
		return muscleScore;
	}

	public void setMuscleScore(String muscleScore) {
		this.muscleScore = muscleScore;
	}

	public String getRespirationScore() {
		return respirationScore;
	}

	public void setRespirationScore(String respirationScore) {
		this.respirationScore = respirationScore;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	
}
