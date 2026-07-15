package com.param.billing.global.transaction.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@Entity
@Table(name="t_doctor_advance", schema="billing")
@SequenceGenerator(name = "t_doctor_advance_seq", sequenceName = "billing.t_doctor_advance_seq", allocationSize = 1)
public class DoctorAdvance {

		@Id
		@Column(name="doctor_advance_id")
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_doctor_advance_seq")
		private Integer doctorAdvanceId;
		
		@Column(name = "doctor_id")
		private Integer doctorId;
		
		@Column(name = "total_advance")
		private Double totalAdvance;
		
		@Column(name = "balance_advance")
		private Double balanceAdvance;
		
		@Column(name = "unit_id")
		private Integer orgUnitId;
		
		@Column(name = "organization_id")
		private Integer orgId;

		@Column(name = "request_status")
		private Integer requestStatus;
		
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
		
		public Integer getDoctorId() {
			return doctorId;
		}

		public void setDoctorId(Integer doctorId) {
			this.doctorId = doctorId;
		}

		public Integer getRequestStatus() {
			return requestStatus;
		}

		public void setRequestStatus(Integer requestStatus) {
			this.requestStatus = requestStatus;
		}

		public Integer getDoctorAdvanceId() {
			return doctorAdvanceId;
		}

		public void setDoctorAdvanceId(Integer doctorAdvanceId) {
			this.doctorAdvanceId = doctorAdvanceId;
		}

		public Double getTotalAdvance() {
			return totalAdvance;
		}

		public void setTotalAdvance(Double totalAdvance) {
			this.totalAdvance = totalAdvance;
		}

		public Double getBalanceAdvance() {
			return balanceAdvance;
		}

		public void setBalanceAdvance(Double balanceAdvance) {
			this.balanceAdvance = balanceAdvance;
		}

		public Integer getOrgUnitId() {
			return orgUnitId;
		}

		public void setOrgUnitId(Integer orgUnitId) {
			this.orgUnitId = orgUnitId;
		}

		public Integer getOrgId() {
			return orgId;
		}

		public void setOrgId(Integer orgId) {
			this.orgId = orgId;
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
