package com.param.global.model;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.LocalTimeConverter;
import com.param.global.org.common.model.ShiftMaster;
import com.param.global.common.InstantConverter;

@NamedQueries({
@NamedQuery(name="GET_OUTPUT_HISTORY_LIST",
query="SELECT outputChartDetailsMaster.outputChartDetailsId AS outputChartDetailsId, "
		+ " outputChartDetailsMaster.outputTypeId AS outputTypeId,"
		+ " outputChartDetailsMaster.startOutputQuantity AS startOutputQuantity,"
		+ " to_char(outputChartDetailsMaster.startOutputDate, 'DD-MM-YYYY') AS startOutputDate,"
		+ " to_char(outputChartDetailsMaster.startOutputTime, 'HH24:MI') AS startOutputTime,"
		+ " outputTypeMaster.outputTypeDesc AS outputTypeDesc, "
		+ " shiftMasterOutput.shiftName AS shiftName "
		+ " FROM OutputChartDetailsMaster outputChartDetailsMaster "
		+ " INNER JOIN  outputChartDetailsMaster.outputTypeMaster outputTypeMaster"
		+ " INNER JOIN  outputChartDetailsMaster.shiftMasterOutput shiftMasterOutput"
		+ " LEFT JOIN  outputChartDetailsMaster.outputChartDetails outputChartDetails"
		+ " WHERE outputChartDetailsMaster.organizationId = :organizationId "
		+ " AND outputChartDetailsMaster.unitId = :unitId "
		+ " AND outputChartDetails.patientId = :patientId "
		+ " AND outputChartDetailsMaster.shiftId = :shiftId "
//		+ " AND to_char(outputChartDetailsMaster.startOutputTime, 'HH24:MI')  =:startOutputTime "
		+ " AND to_char(outputChartDetailsMaster.startOutputDate, 'DD-MM-YYYY') =:intakeDate "
)


})

@Entity
@Table(name = "t_output_chart_details", schema = "adt")
@SequenceGenerator(name="t_output_chart_details_seq", sequenceName="adt.t_output_chart_details_seq", allocationSize=1)
public class OutputChartDetailsMaster {
	@Id
	@Column(name = "output_chart_details_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_output_chart_details_seq")
	private Integer outputChartDetailsId;

	@Column(name = "intake_output_chart_id")
	private Integer intakeOutputChartId;
	
	@Column(name = "start_output_date")
	private Date startOutputDate;
	
	@Column(name = "start_output_time")
	@Convert(converter = InstantConverter.class)
	private Instant startOutputTime;
	
	@Column(name = "start_output_quantity")
	private Integer startOutputQuantity;
	
	@Column(name = "total_output")
	private Integer totalOutput;
	
	@Column(name = "output_type_id")
	private Integer outputTypeId;
	
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
	
	@Column(name = "shift_id")
	private Integer shiftId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "intake_output_chart_id", insertable = false, updatable = false)
	private IntakeOutputChartMaster outputChartDetails;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="output_type_id", insertable= false, updatable= false)
	private OutputTypeMaster outputTypeMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="shift_id", insertable= false, updatable= false)
	private ShiftMaster shiftMasterOutput;
	
	public Integer getOutputChartDetailsId() {
		return outputChartDetailsId;
	}

	public void setOutputChartDetailsId(Integer outputChartDetailsId) {
		this.outputChartDetailsId = outputChartDetailsId;
	}

	public Integer getIntakeOutputChartId() {
		return intakeOutputChartId;
	}

	public void setIntakeOutputChartId(Integer intakeOutputChartId) {
		this.intakeOutputChartId = intakeOutputChartId;
	}

	public Date getStartOutputDate() {
		return startOutputDate;
	}

	public void setStartOutputDate(Date startOutputDate) {
		this.startOutputDate = startOutputDate;
	}

	public Instant getStartOutputTime() {
		return startOutputTime;
	}

	public void setStartOutputTime(Instant startOutputTime) {
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

	public Integer getTotalOutput() {
		return totalOutput;
	}

	public void setTotalOutput(Integer totalOutput) {
		this.totalOutput = totalOutput;
	}
	
	
	
}
