package com.param.adt.admission.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

	@Entity
	@Table(name = "m_systolic_blood_pressure", schema = "adt")
	@SequenceGenerator(name = "systolic_blood_pressure_seq", sequenceName = "adt.systolic_blood_pressure_seq", allocationSize = 1)
	public class SystolicBloodPressure {
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "systolic_blood_pressure_seq")
		
		@Column(name = "systolic_blood_pressure_id")
		private Integer systolicBloodPressureId;
		
		@Column(name = "value_desc ")
		private String valueDesc ;
		
		@Column(name = "value")
		private Integer value;
		
		@Column(name = "organization_id")
		private Integer organizationId;
		
		@Column(name = "unit_id")
		private Integer unitId;
		
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

		
		public Integer getSystolicBloodPressureId() {
			return systolicBloodPressureId;
		}

		public void setSystolicBloodPressureId(Integer systolicBloodPressureId) {
			this.systolicBloodPressureId = systolicBloodPressureId;
		}

		public String getValueDesc() {
			return valueDesc;
		}

		public void setValueDesc(String valueDesc) {
			this.valueDesc = valueDesc;
		}

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
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
		
		
}

