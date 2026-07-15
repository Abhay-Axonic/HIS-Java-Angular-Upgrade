package com.param.entity.lis.unit;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "t_lab_machine_interface", schema = "lab")
@SequenceGenerator(name = "seq_t_lab_machine_interface", sequenceName = "lab.seq_t_lab_machine_interface", allocationSize = 1)
public class LabMachineInterface {

	@Id
	@Column(name = "interface_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_t_lab_machine_interface")
	private int interfaceId;

	@Column(name = "sample_no")
	private String sampleNo;
	
	@Column(name = "parameter_name")
	private String parameterName;
	
	@Column(name = "parameter_value")
	private String parameterValue;
	
	@Column(name = "parameter_unit")
	private String parameterUnit;
	
	@Column(name = "min_value")
	private double minValue;
	
	@Column(name = "max_value")
	private double maxValue;
	
	@Column(name = "high_value")
	private double highValue;
	
	@Column(name = "low_value")
	private double lowValue;

	@Column(name = "param_result")
	private String paramResult;
	
	@Column(name = "note")
	private String note;
	
	@Column(name = "status")
	private char status;

	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "machine_name")
	private String machineName;
	
	
	@Column(name="organisation_id")
	private Integer organisationId;
	
	@Column(name="unit_id")
	private Integer unitId;

	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name = "result_date_time")
	private Date resultDateTime;

	public int getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(int interfaceId) {
		this.interfaceId = interfaceId;
	}

	public String getSampleNo() {
		return sampleNo;
	}

	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	public String getParameterUnit() {
		return parameterUnit;
	}

	public void setParameterUnit(String parameterUnit) {
		this.parameterUnit = parameterUnit;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public double getHighValue() {
		return highValue;
	}

	public void setHighValue(double highValue) {
		this.highValue = highValue;
	}

	public double getLowValue() {
		return lowValue;
	}

	public void setLowValue(double lowValue) {
		this.lowValue = lowValue;
	}

	public String getParamResult() {
		return paramResult;
	}

	public void setParamResult(String paramResult) {
		this.paramResult = paramResult;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public Date getResultDateTime() {
		return resultDateTime;
	}

	public void setResultDateTime(Date resultDateTime) {
		this.resultDateTime = resultDateTime;
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
