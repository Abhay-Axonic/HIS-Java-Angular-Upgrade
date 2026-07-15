package com.param.entity.model.pharmacy;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.Frequency;
import com.param.entity.model.master.RouteOfAdministration;
import com.param.entity.model.master.StrengthUnit;
import com.param.entity.model.master.User;
import com.param.utility.TimeZoneComponent;


@Entity(name="PrescriptionFrequencyDetail")
@Table(name="t_prescription_frequency_detail",schema="pharmacy")
public class PrescriptionFrequencyDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="dose")
	private Double dose;

	/*@Column(name="dose_unit_id")
	private Integer doseUnitId;*/
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="dose_unit_id")
	private StrengthUnit strengthUnit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="frequency_id")
	private Frequency frequency;

	@Column(name="instructions")
	private String instructions;

	@Column(name="remark")
	private String remark;

	@Column(name="start_date")
	private Date startDate = TimeZoneComponent.getDateByzone(0);

	@Column(name="stop_date")
	private Date stopDate= TimeZoneComponent.getDateByzone(0);

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="prescription_detail_id")
	private PrescriptionDetail prescriptionDetail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="route_id")
	private RouteOfAdministration routeOfAdministration;

	public PrescriptionFrequencyDetail() {
		super();
	}
	
	public PrescriptionFrequencyDetail(Integer id, Double dose, StrengthUnit strengthUnit, Frequency frequency,
			String instructions, String remark, Date startDate, Date stopDate, PrescriptionDetail prescriptionDetail,
			RouteOfAdministration routeOfAdministration) {
		super();
		this.id = id;
		this.dose = dose;
		this.strengthUnit = strengthUnit;
		this.frequency = frequency;
		this.instructions = instructions;
		this.remark = remark;
		this.startDate = startDate;
		this.stopDate = stopDate;
		this.prescriptionDetail = prescriptionDetail;
		this.routeOfAdministration = routeOfAdministration;
	}



	public PrescriptionFrequencyDetail(Double dose, StrengthUnit strengthUnit, Frequency frequency, String instructions,
			Date startDate, Date stopDate, RouteOfAdministration routeOfAdministration,Boolean isActive,
			Boolean isDeleted,User addedBy,User updatedBy,Date addedDate, Date updatedDate) {
		super();
		this.dose = dose;
		this.strengthUnit = strengthUnit;
		this.frequency = frequency;
		this.instructions = instructions;
		this.startDate = startDate;
		this.stopDate = stopDate;
		this.routeOfAdministration = routeOfAdministration;
		this.isActive = isActive;
		this.isDeleted = isDeleted;
		this.addedBy = addedBy;
		this.updatedBy = updatedBy;
		this.addedDate = addedDate;
		this.updatedDate = updatedDate;
	}


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/*public Integer getDoseUnitId() {
		return this.doseUnitId;
	}

	public void setDoseUnitId(Integer doseUnitId) {
		this.doseUnitId = doseUnitId;
	}*/

	public Frequency getFrequency() {
		return this.frequency;
	}

	public void setFrequency(Frequency frequency) {
		this.frequency = frequency;
	}

	public String getInstructions() {
		return this.instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStopDate() {
		return stopDate;
	}

	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}

	public PrescriptionDetail getPrescriptionDetail() {
		return this.prescriptionDetail;
	}

	public void setTPrescriptionDetail(PrescriptionDetail prescriptionDetail) {
		this.prescriptionDetail = prescriptionDetail;
	}

	public RouteOfAdministration getRouteOfAdministration() {
		return routeOfAdministration;
	}

	public void setRouteOfAdministration(RouteOfAdministration routeOfAdministration) {
		this.routeOfAdministration = routeOfAdministration;
	}

	public void setPrescriptionDetail(PrescriptionDetail prescriptionDetail) {
		this.prescriptionDetail = prescriptionDetail;
	}

	public Double getDose() {
		return dose;
	}

	public void setDose(Double dose) {
		this.dose = dose;
	}

	public StrengthUnit getStrengthUnit() {
		return strengthUnit;
	}

	public void setStrengthUnit(StrengthUnit strengthUnit) {
		this.strengthUnit = strengthUnit;
	}
	

}