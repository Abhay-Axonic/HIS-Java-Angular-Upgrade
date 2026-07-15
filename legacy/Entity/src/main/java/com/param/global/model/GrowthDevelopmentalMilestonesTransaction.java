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
	@NamedNativeQuery(name = "GET_GROWTH_DEVELOPMENTAL_MILESTONES_TRANSACTION_BY_PATIENT", 
			query ="SELECT "
						+ "	 tgdmt.growth_developmental_milestones_transaction_id AS \"growthDevelopmentalMilestonesTransactionId\", "
						+ "	 mgdmm.growth_developmental_milestones_id AS \"growthDevelopmentalMilestonesId\", "
						+ "	 mgdmm.age_from AS \"ageFrom\", "
						+ "	 mgdmm.age_to AS \"ageTo\", "
						+ "	 mgdmm.type AS \"type\", "
						+ "	 mgdmm.milestone AS \"milestone\", "
						+ "  to_char( "
						+ "    tgdmt.achieved_date, "
						+ "    'MM-DD-YYYY HH:MI:SS'"
						+ "  ) AS \"achievedDate\", "
						+ "	 tgdmt.actual_age_of_milestone_achieved AS \"actualAgeOfMilestoneAchieved\", "
						+ "	 tgdmt.patient_id AS \"patientId\", "
						+ "	 tgdmt.organization_id AS \"organizationId\", "
						+ "	 tgdmt.unit_id AS \"unitId\", "
						+ "	 tgdmt.status AS \"status\" "
						+ "FROM "
						+ "	 adt.m_growth_developmental_milestones_master mgdmm "
						+ "LEFT JOIN adt.t_growth_developmental_milestones_transaction tgdmt ON mgdmm.growth_developmental_milestones_id = tgdmt.growth_developmental_milestones_id "
						+ "	 AND tgdmt.unit_id = :unitId "
						+ "	 AND tgdmt.patient_id = :patientId "
						+ "	 AND tgdmt.organization_id = :orgId "
						+ "WHERE "
						+ "	 mgdmm.organization_id = :orgId "
						+ "	 AND mgdmm.status <> 'D' "
						+ "  ORDER BY mgdmm.growth_developmental_milestones_id"
					)
})


@Entity
@Table(name = "t_growth_developmental_milestones_transaction" , schema = "adt")
@SequenceGenerator(name = "t_growth_developmental_milestones_transaction_seq" , sequenceName = "adt.t_growth_developmental_milestones_transaction_seq" , allocationSize = 1)
public class GrowthDevelopmentalMilestonesTransaction {
	
	@Id
	@Column(name="growth_developmental_milestones_transaction_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="t_growth_developmental_milestones_transaction_seq")
	private int growthDevelopmentalMilestonesTransactionId;
	
	@Column(name="growth_developmental_milestones_id")
	private Integer growthDevelopmentalMilestonesId;
	
	@Column(name="achieved_date")
	private Date achievedDate;
	
	@Column(name="actual_age_of_milestone_achieved")
	private Integer actualAgeOfMilestoneAchieved;
		
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
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

	public int getGrowthDevelopmentalMilestonesTransactionId() {
		return growthDevelopmentalMilestonesTransactionId;
	}

	public void setGrowthDevelopmentalMilestonesTransactionId(int growthDevelopmentalMilestonesTransactionId) {
		this.growthDevelopmentalMilestonesTransactionId = growthDevelopmentalMilestonesTransactionId;
	}

	public Integer getGrowthDevelopmentalMilestonesId() {
		return growthDevelopmentalMilestonesId;
	}

	public void setGrowthDevelopmentalMilestonesId(Integer growthDevelopmentalMilestonesId) {
		this.growthDevelopmentalMilestonesId = growthDevelopmentalMilestonesId;
	}

	public Date getAchievedDate() {
		return achievedDate;
	}

	public void setAchievedDate(Date achievedDate) {
		this.achievedDate = achievedDate;
	}

	public Integer getActualAgeOfMilestoneAchieved() {
		return actualAgeOfMilestoneAchieved;
	}

	public void setActualAgeOfMilestoneAchieved(Integer actualAgeOfMilestoneAchieved) {
		this.actualAgeOfMilestoneAchieved = actualAgeOfMilestoneAchieved;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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
}
