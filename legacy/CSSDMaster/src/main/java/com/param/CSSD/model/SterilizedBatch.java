package com.param.CSSD.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;



@NamedQuery(name = "GET_INDICATORS_BY_BATCH_NO", query = "SELECT sterilizedBatch.batchNumber as batchNumber,"
		+ "sterilizedBatch.sterilizationTemperature as sterilizationTemperature,"
		+ "sterilizedBatch.pressure as pressure, sterilizedBatch.color as color,"
		+ "to_char(sterilizedBatch.expiryDate,'dd-MM-yyyy') as expiryDate "
		+ "FROM SterilizedBatch sterilizedBatch WHERE sterilizedBatch.batchNumber=:batchNo")


@Entity
@Table(name = "t_sterilized_batch", schema = "cssd")
@SequenceGenerator(name = "t_sterilized_batch_id_seq", sequenceName = "cssd.t_sterilized_batch_id_seq", allocationSize = 1)
public class SterilizedBatch {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_sterilized_batch_id_seq")
	@Column(name = "id")
	private Integer id;

	@Column(name = "batch_number")
	private Integer batchNumber;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_number", insertable = false, updatable = false)
	private LoadBatch  loadBatch;
	
	@Column(name = "sterilization_temperature")
	private Integer sterilizationTemperature;
	
	@Column(name = "pressure")
	private Integer pressure;
	
	@Column(name = "expiry_date")
	private Date expiryDate;
	
	@Column(name = "color")
	private String color;
	
	@Column(name = "status")
	private String status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(Integer batchNumber) {
		this.batchNumber = batchNumber;
	}

	public LoadBatch getLoadBatch() {
		return loadBatch;
	}

	public void setLoadBatch(LoadBatch loadBatch) {
		this.loadBatch = loadBatch;
	}

	public Integer getSterilizationTemperature() {
		return sterilizationTemperature;
	}

	public void setSterilizationTemperature(Integer sterilizationTemperature) {
		this.sterilizationTemperature = sterilizationTemperature;
	}

	public Integer getPressure() {
		return pressure;
	}

	public void setPressure(Integer pressure) {
		this.pressure = pressure;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
		
	
}
