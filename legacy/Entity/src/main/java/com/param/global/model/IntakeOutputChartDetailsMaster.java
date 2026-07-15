package com.param.global.model;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.LocalTimeConverter;
import com.param.global.org.common.model.ShiftMaster;
import com.param.global.common.InstantConverter;

@NamedQueries({
@NamedQuery(name="GET_INTAKE_HISTORY_LIST",
query="SELECT intakeOutputChartDetailsMaster.intakeOutputChartDetailsId AS intakeOutputChartDetailsId, "
		+ " intakeOutputChartDetailsMaster.intakeTypeFluidId AS intakeTypeFluidId,"
		+ " intakeOutputChartDetailsMaster.intakeFluidId AS intakeFluidId,"
		+ " intakeOutputChartDetailsMaster.startIntakeQuantity AS startIntakeQuantity,"
		+ " to_char(intakeOutputChartDetailsMaster.startIntakeDate, 'DD-MM-YYYY') AS startIntakeDate,"
		+ " to_char(intakeOutputChartDetailsMaster.startIntakeTime, 'HH24:MI') AS startIntakeTime,"
		+ " intakeFluidTypeMaster.intakeTypeFluidDesc AS intakeTypeFluidDesc, "
		+ " shiftMasterIntake.shiftName AS shiftName,"
		+ " intakeFluidMaster.intakeFluiddesc AS intakeFluiddesc "
		+ " FROM IntakeOutputChartDetailsMaster intakeOutputChartDetailsMaster "
		+ " INNER JOIN  intakeOutputChartDetailsMaster.intakeFluidTypeMaster intakeFluidTypeMaster"
		+ " INNER JOIN  intakeOutputChartDetailsMaster.shiftMasterIntake shiftMasterIntake"
		+ " LEFT JOIN  intakeOutputChartDetailsMaster.intakeOutputChartDetails intakeOutputChartDetails"
		+ " INNER JOIN  intakeOutputChartDetailsMaster.intakeFluidMaster intakeFluidMaster"
		+ " WHERE intakeOutputChartDetailsMaster.organizationId = :organizationId "
		+ " AND intakeOutputChartDetailsMaster.unitId = :unitId "
		+ " AND intakeOutputChartDetails.patientId = :patientId "
		+ " AND intakeOutputChartDetailsMaster.shiftId = :shiftId "
//		+ " AND to_char(intakeOutputChartDetailsMaster.startIntakeTime, 'HH24:MI')  =:startIntakeTime "
		+ " AND to_char(intakeOutputChartDetailsMaster.startIntakeDate, 'DD-MM-YYYY') =:intakeDate "
)


})
@Entity
@Table(name = "t_intake_output_chart_details", schema = "adt")
@SequenceGenerator(name="t_intake_output_chart_details_seq", sequenceName="adt.t_intake_output_chart_details_seq", allocationSize=1)
public class IntakeOutputChartDetailsMaster {
	@Id
	@Column(name = "intake_output_chart_details_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_intake_output_chart_details_seq")
	private Integer intakeOutputChartDetailsId;

	@Column(name = "intake_output_chart_id")
	private Integer intakeOutputChartId;

	@Column(name = "intake_Fluid_id")
	private Integer intakeFluidId;
	
	@Column(name = "intake_type_Fluid_id")
	private Integer intakeTypeFluidId;
	
	@Column(name = "start_intake_date")
	private Date startIntakeDate;
	
	@Column(name = "shift_id")
	private Integer shiftId;
	
	@Column(name = "start_intake_time")
	@Convert(converter = InstantConverter.class)
	private Instant startIntakeTime;
	
	@Column(name = "start_intake_quantity")
	private Integer startIntakeQuantity;
	
	@Column(name = "total_intake")
	private Integer totalIntake;
	
	
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

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "intake_output_chart_id", insertable = false, updatable = false)
	private IntakeOutputChartMaster intakeOutputChartDetails;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="shift_id", insertable= false, updatable= false)
	private ShiftMaster shiftMasterIntake;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="intake_type_Fluid_id", insertable= false, updatable= false)
	private IntakeFluidTypeMaster intakeFluidTypeMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="intake_Fluid_id", insertable= false, updatable= false)
	private IntakeFluidMaster intakeFluidMaster;
	
	public Integer getIntakeOutputChartDetailsId() {
		return intakeOutputChartDetailsId;
	}

	public void setIntakeOutputChartDetailsId(Integer intakeOutputChartDetailsId) {
		this.intakeOutputChartDetailsId = intakeOutputChartDetailsId;
	}

	public Integer getIntakeOutputChartId() {
		return intakeOutputChartId;
	}

	public void setIntakeOutputChartId(Integer intakeOutputChartId) {
		this.intakeOutputChartId = intakeOutputChartId;
	}

	public Integer getIntakeFluidId() {
		return intakeFluidId;
	}

	public void setIntakeFluidId(Integer intakeFluidId) {
		this.intakeFluidId = intakeFluidId;
	}

	public Integer getIntakeTypeFluidId() {
		return intakeTypeFluidId;
	}

	public void setIntakeTypeFluidId(Integer intakeTypeFluidId) {
		this.intakeTypeFluidId = intakeTypeFluidId;
	}

	public Date getStartIntakeDate() {
		return startIntakeDate;
	}

	public void setStartIntakeDate(Date startIntakeDate) {
		this.startIntakeDate = startIntakeDate;
	}

	public Instant getStartIntakeTime() {
		return startIntakeTime;
	}

	public void setStartIntakeTime(Instant startIntakeTime) {
		this.startIntakeTime = startIntakeTime;
	}

	public Integer getStartIntakeQuantity() {
		return startIntakeQuantity;
	}

	public void setStartIntakeQuantity(Integer startIntakeQuantity) {
		this.startIntakeQuantity = startIntakeQuantity;
	}

	/*public Date getStartOutputDate() {
		return startOutputDate;
	}

	public void setStartOutputDate(Date startOutputDate) {
		this.startOutputDate = startOutputDate;
	}

	public LocalTime getStartOutputTime() {
		return startOutputTime;
	}

	public void setStartOutputTime(LocalTime startOutputTime) {
		this.startOutputTime = startOutputTime;
	}

	public Integer getStartOutputQuantity() {
		return startOutputQuantity;
	}

	public void setStartOutputQuantity(Integer startOutputQuantity) {
		this.startOutputQuantity = startOutputQuantity;
	}

	public Integer getOutputTypeId() {
		return outputTypeId;
	}

	public void setOutputTypeId(Integer outputTypeId) {
		this.outputTypeId = outputTypeId;
	}*/

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

	public Integer getShiftId() {
		return shiftId;
	}

	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}

	public Integer getTotalIntake() {
		return totalIntake;
	}

	public void setTotalIntake(Integer totalIntake) {
		this.totalIntake = totalIntake;
	}
	
	
	
	
}
