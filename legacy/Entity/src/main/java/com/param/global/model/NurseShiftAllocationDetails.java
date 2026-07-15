package com.param.global.model;

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
@NamedNativeQueries({
	@NamedNativeQuery(
			name = "GET_NURSE_SHIFT_DETAILS_BY_ID",
			query ="select  nsa.nurse_shift_allocation_id as \"nurseShiftAllocationId\", nsa.from_date as \"fromDate\", nsa.to_date as \"toDate\", "
					+ " nsa.ward_id as \"wardId\", nsa.nurse_id as \"nurseId\", ad.shift_date as \"shiftDate\", ad.shift_id as \"shiftId\", "
					+ " ad.bed_id as \"bedId\" "
					+ " FROM public.m_nurse_shift_allocation nsa  "
					+ " inner join public.m_nurse_shift_allocation_details ad on nsa.nurse_shift_allocation_id = ad.nurse_shift_allocation_id "
					+ " where nsa.nurse_id=:nurseId AND nsa.ward_id=:wardId AND nsa.org_id=:orgId AND nsa.unit_id=:unitId " )	
})

				
	@Entity
	@Table(name = "m_nurse_shift_allocation_details ", schema = "public")
	@SequenceGenerator(name = "m_nurse_shift_allocation_details_seq", sequenceName = "public.m_nurse_shift_allocation_details_seq", allocationSize = 1)
	public class NurseShiftAllocationDetails {
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_nurse_shift_allocation_details_seq")
		@Column(name = "nurse_shift_allocation_details_id")
		private Integer nurseShiftAllocationDetailsId;

		@Column(name = "nurse_shift_allocation_id")
		private Integer nurseShiftAllocationId;

		@Column(name = "shift_date")
		private Date shiftDate;

		@Column(name = "shift_id")
		private Integer shiftId;

		@Column(name = "bed_id")
		private Integer bedId;

		@Column(name = "nurse_id")
		private Integer nurseId;

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

		@Column(name = "org_id")
		private Integer orgId;
		
		@Column(name = "is_deleted")
		private Character isDeleted;
		
	    @ManyToOne(fetch= FetchType.LAZY)
		@JoinColumn(name="nurse_shift_allocation_id", insertable=false, updatable=false)
		private NurseShiftAllocation nurseShiftAllocation;
		
		public Integer getNurseShiftAllocationDetailsId() {
			return nurseShiftAllocationDetailsId;
		}

		public void setNurseShiftAllocationDetailsId(Integer nurseShiftAllocationDetailsId) {
			this.nurseShiftAllocationDetailsId = nurseShiftAllocationDetailsId;
		}

		public Integer getNurseShiftAllocationId() {
			return nurseShiftAllocationId;
		}

		public void setNurseShiftAllocationId(Integer nurseShiftAllocationId) {
			this.nurseShiftAllocationId = nurseShiftAllocationId;
		}

		public Date getShiftDate() {
			return shiftDate;
		}

		public void setShiftDate(Date shiftDate) {
			this.shiftDate = shiftDate;
		}

		public Integer getShiftId() {
			return shiftId;
		}

		public void setShiftId(Integer shiftId) {
			this.shiftId = shiftId;
		}

		public Integer getBedId() {
			return bedId;
		}

		public void setBedId(Integer bedId) {
			this.bedId = bedId;
		}

		public Integer getNurseId() {
			return nurseId;
		}

		public void setNurseId(Integer nurseId) {
			this.nurseId = nurseId;
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

		public Integer getOrgId() {
			return orgId;
		}

		public void setOrgId(Integer orgId) {
			this.orgId = orgId;
		}

		public NurseShiftAllocation getNurseShiftAllocation() {
			return nurseShiftAllocation;
		}

		public void setNurseShiftAllocation(NurseShiftAllocation nurseShiftAllocation) {
			this.nurseShiftAllocation = nurseShiftAllocation;
		}

		public Character getIsDeleted() {
			return isDeleted;
		}

		public void setIsDeleted(Character isDeleted) {
			this.isDeleted = isDeleted;
		}

		
}
