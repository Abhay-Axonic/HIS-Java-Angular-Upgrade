package com.param.entity.model.pharmacy;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.adt.master.global.model.ReasonMaster;
import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.FrequencyDetail;
import com.param.global.common.LocalTimeConverter;
import com.param.global.common.InstantConverter;

@Entity(name="DrugAdministrationDetail")
@Table(name="t_drug_administration_detail",schema="pharmacy")
public class DrugAdministrationDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="drug_administration_id")
	private DrugAdministration drugAdministration;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="prescription_detail_id")
	private PrescriptionDetail prescriptionDetail;
	
	@Column(name="administration_time")
	private Timestamp administrationTime;
	
	@Column(name="start_administration_time")
	@Convert(converter = InstantConverter.class)
	private Instant startAdministrationTime;
	
	@Column(name="stop_administration_time")
	@Convert(converter = InstantConverter.class)
	private Instant stopAdministrationTime;
	
	@Column(name="start_administration_date")
	private Date startAdministrationDate;
	
	@Column(name="stop_administration_date")
	private Date stopAdministrationDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="reason_id")
	private ReasonMaster reason;
	
	@Column(name="note")
	private String note;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="frequency_detail_id")
	private FrequencyDetail frequencyDetail;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="batch_id")
	private Batch batch;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public DrugAdministration getDrugAdministration() {
		return drugAdministration;
	}

	public void setDrugAdministration(DrugAdministration drugAdministration) {
		this.drugAdministration = drugAdministration;
	}

	public PrescriptionDetail getPrescriptionDetail() {
		return prescriptionDetail;
	}

	public void setPrescriptionDetail(PrescriptionDetail prescriptionDetail) {
		this.prescriptionDetail = prescriptionDetail;
	}

	public Timestamp getAdministrationTime() {
		return administrationTime;
	}

	public void setAdministrationTime(Timestamp administrationTime) {
		this.administrationTime = administrationTime;
	}

	public ReasonMaster getReason() {
		return reason;
	}

	public void setReason(ReasonMaster reason) {
		this.reason = reason;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public FrequencyDetail getFrequencyDetail() {
		return frequencyDetail;
	}

	public void setFrequencyDetail(FrequencyDetail frequencyDetail) {
		this.frequencyDetail = frequencyDetail;
	}

	public DrugAdministrationDetail() {
		super();
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public DrugAdministrationDetail(PrescriptionDetail prescriptionDetail, Timestamp administrationTime,
			ReasonMaster reason, String note, FrequencyDetail frequencyDetail, Batch batch, Instant startAdministrationTime,Instant stopAdministrationTime,
			Date startAdministrationDate, Date stopAdministrationDate) {
		super();
		this.prescriptionDetail = prescriptionDetail;
		this.administrationTime = administrationTime;
		this.reason = reason;
		this.note = note;
		this.frequencyDetail = frequencyDetail;
		this.batch = batch;
		this.startAdministrationTime = startAdministrationTime;
		this.stopAdministrationTime = stopAdministrationTime;
		this.startAdministrationDate = startAdministrationDate;
		this.stopAdministrationDate = stopAdministrationDate;
	}
	
	public void updateDrugAdministrationDetail(PrescriptionDetail prescriptionDetail, Timestamp administrationTime,
			ReasonMaster reason, String note, FrequencyDetail frequencyDetail) {
		this.prescriptionDetail = prescriptionDetail;
		this.administrationTime = administrationTime;
		this.reason = reason;
		this.note = note;
		this.frequencyDetail = frequencyDetail;
	}

	

	public Instant getStartAdministrationTime() {
		return startAdministrationTime;
	}

	public void setStartAdministrationTime(Instant startAdministrationTime) {
		this.startAdministrationTime = startAdministrationTime;
	}

	public Instant getStopAdministrationTime() {
		return stopAdministrationTime;
	}

	public void setStopAdministrationTime(Instant stopAdministrationTime) {
		this.stopAdministrationTime = stopAdministrationTime;
	}

	public Date getStartAdministrationDate() {
		return startAdministrationDate;
	}

	public void setStartAdministrationDate(Date startAdministrationDate) {
		this.startAdministrationDate = startAdministrationDate;
	}

	public Date getStopAdministrationDate() {
		return stopAdministrationDate;
	}

	public void setStopAdministrationDate(Date stopAdministrationDate) {
		this.stopAdministrationDate = stopAdministrationDate;
	}
	
}