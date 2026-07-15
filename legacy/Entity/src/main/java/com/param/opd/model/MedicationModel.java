package com.param.opd.model;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.model.master.Frequency;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RouteOfAdministration;
import com.param.opd.coversheet.model.InstructionMaster;

/*
 * 
 * Created by Tushar
 * 
 */

/*@NamedQueries({
	@NamedQuery(name="GET_FAVORITE_MEDICATION", query="SELECT meds.medication_id as medication_id,meds.drug_id as drug_id,meds.dose as dose,meds.roa_id as roa_id,"
			+ " r.roa_details as roa_details, m.medication_instruction_details as intruction, "
			+"meds.frequency_id as frequency_id,meds.duration as duration,meds.duration_unit as duration_unit,meds.instruction_id as instruction_id,"
			+"drug.medicationName as medicationName,drug.genericName as genericName,freq.frequency as frequency "
			+"FROM MedicationModel as meds "
			+"INNER JOIN meds.drugMaster drug "
			+"INNER JOIN meds.frequencyMaster freq "
			+"INNER JOIN meds.roaMaster r "
			+ "INNER JOIN meds.medicationInstructionMaster m "
			+"WHERE meds.medication_status = true AND meds.favorite_status = true AND meds.created_by = :DOCID "),
			
	@NamedQuery(name="GET_DRUG_LIST", query="SELECT drug.drugId as drugId,drug.genericName as genericName,drug.medicationName as medicationName,drug.dose as dose "
			+"FROM DrugMaster as drug "
			+"WHERE drug.unitId=:UNITID "
			+" AND 	drug.organizationId=:ORGID "
			+" AND  drug.status = 'A' " 
			+"AND LOWER(drug.medicationName) LIKE :MEDICATION_NAME"),
	
	@NamedQuery(name="GET_DRUG_LIST_ROA", query="SELECT d.medicationName as medicationName,d.dose as dose,d.genericName as genericName,r.roa_id as roa_id,r.roa_details as roa_details "
			+"FROM DrugMaster as d "
			+"INNER JOIN d.roaMaster r "
			+"WHERE d.unitId=:UNITID "
			+" AND 	d.organizationId=:ORGID "
			+" AND  LOWER(d.medicationName) LIKE LOWER(:MEDICATION_NAME) ORDER BY d.medicationName,d.roa_id"),
	
	@NamedQuery(name="GET_DRUG_USING_ROA", query="SELECT drug.drugId as drugId "
			+"FROM DrugMaster as drug "
			+"WHERE drug.unitId=:UNITID "
			+" AND 	drug.organizationId=:ORGID "
			+" AND  drug.status = 'A' "
			+" AND  drug.roa_id = :ROA_ID "
			+"AND drug.medicationName = :MEDICATION_NAME "
			+ "AND drug.dose = :DOSE"),
			
	@NamedQuery(name="GET_LAST_ORDER", query="SELECT meds.medication_id as medication_id,meds.drug_id as drug_id,meds.dose as dose,r.roa_id as roa_id,"
			+ "r.roa_details as roa_details,m.medication_instruction_details as intruction, "
			+ "COALESCE(to_char(meds.start_date, 'MM-DD-YYYY'), '') as start_date, "
			+ "COALESCE(to_char(meds.end_date, 'MM-DD-YYYY'), '') as end_date, "
			+"meds.frequency_id as frequency_id,meds.duration as duration,meds.duration_unit as duration_unit,m.medication_instruction_id as instruction_id,"
			+"drug.medicationName as medicationName,drug.genericName as genericName,freq.frequency as frequency "
			+"FROM MedicationModel as meds "
			+"INNER JOIN meds.drugMaster drug "
			+"INNER JOIN meds.frequencyMaster freq "
			+"INNER JOIN meds.roaMaster r "
			+ "INNER JOIN meds.medicationInstructionMaster m "
			+"WHERE meds.medication_status = true AND meds.patient_id= :PATIENT_ID "
			+"AND meds.encounter_id != :ENCOUNTERID "
			+"ORDER BY meds.created_date desc "),
	
	@NamedQuery(name="GET_ACTIVE_MEDICATIONS", query="SELECT meds.medication_id as medication_id,meds.drug_id as drug_id,meds.dose as dose,r.roa_id as roa_id,"
			+"meds.frequency_id as frequency_id,meds.duration as duration,meds.duration_unit as duration_unit,m.medication_instruction_id as instruction_id,"
			+ "r.roa_details as roa_details,m.medication_instruction_details as intruction, "
			+ "COALESCE(to_char(meds.start_date, 'MM-DD-YYYY'), '') as start_date, "
			+ "COALESCE(to_char(meds.end_date, 'MM-DD-YYYY'), '') as end_date, "
			+"drug.medicationName as medicationName,drug.genericName as genericName,freq.frequency as frequency "
			+"FROM MedicationModel as meds "
			+"INNER JOIN meds.drugMaster drug "
			+"INNER JOIN meds.frequencyMaster freq "
			+"INNER JOIN meds.roaMaster r "
			+"INNER JOIN meds.medicationInstructionMaster m "
			+"WHERE meds.medication_status = true "
			+ "AND meds.patient_id= :PATIENT_ID "
			+ "AND (meds.start_date <= :CURR_DATE AND meds.end_date >= :CURR_DATE) "
			+"ORDER BY meds.end_date desc "),
			
	@NamedQuery(name="GET_MEDIC_INSTRUCTIONS", query="SELECT intr.medication_instruction_id as medication_instruction_id,intr.medication_instruction_details as medication_instruction_details "
			+ "FROM MedicationInstructionMaster as intr"),
	
	@NamedQuery(name="GET_FREQUENCY_DETAILS", query="SELECT fr.frequency_id as frequency_id,fr.frequency as frequency "
			+ "FROM FrequencyMaster as fr"),
	
	@NamedQuery(name="GET_ROA_MASTERS", query="SELECT rm.roa_id as roa_id,rm.roa_details as roa_details "
			+ "FROM RoaMaster as rm"),
	
	
})*/

@Entity
@Table(name="t_medication",schema="opd")
@SequenceGenerator(name = "t_medication_seq", sequenceName = "opd.t_medication_seq", allocationSize = 1)
public class MedicationModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_medication_seq")
	@Column(name = "medication_id", unique = true)
	private Integer medicationId;
	
	@Column(name = "encounter_id")
	private Integer encounterId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "item_id")
	private Integer itemId;
	
	@Column(name = "dose_id")
	private Integer doseId;
	
	@Column(name = "roa_id")
	private Integer roaId;
	
	@Column(name = "frequency_id")
	private Integer frequencyId;
	
	@Column(name = "duration")
	private Integer duration;
	
	@Column(name = "duration_unit")
	private String durationUnit;
	
	@Column(name = "start_date")
	private Date startDate;
	
	@Column(name = "end_date")
	private Date endDate;
	
	@Column(name = "instruction_id")
	private Integer instructionId;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", insertable = false, nullable = false, updatable = false)
	private Item item;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roa_id", insertable = false, nullable = false, updatable = false)
	private RouteOfAdministration routeOfAdministration;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "frequency_id", insertable = false, nullable = false, updatable = false)
	private Frequency frequency;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "instruction_id", insertable = false, nullable = false, updatable = false)
	private InstructionMaster instructionMaster;

	public Integer getMedicationId() {
		return medicationId;
	}

	public void setMedicationId(Integer medicationId) {
		this.medicationId = medicationId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
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

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getDoseId() {
		return doseId;
	}

	public void setDoseId(Integer doseId) {
		this.doseId = doseId;
	}

	public Integer getRoaId() {
		return roaId;
	}

	public void setRoaId(Integer roaId) {
		this.roaId = roaId;
	}

	public Integer getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Integer frequencyId) {
		this.frequencyId = frequencyId;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getDurationUnit() {
		return durationUnit;
	}

	public void setDurationUnit(String durationUnit) {
		this.durationUnit = durationUnit;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(Integer instructionId) {
		this.instructionId = instructionId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

}
