
package com.param.opd.coversheet.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;



	@Entity
	@Table(name = "m_vital_category_master", schema = "emr")
	@SequenceGenerator(name = "m_vital_category_master_seq", sequenceName = "emr.m_vital_category_master_seq", allocationSize = 1)
	public class VitalCategoryMaster {
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_vital_category_master_seq")
		
		@Column(name = "vital_category_id")
		private Integer vitalCategoryId;
		
		@Column(name = "vital_category_code")
		private String vitalCategoryCode;
		
		@Column(name = "vital_category_desc")
		private String vitalCategoryDesc;
		
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
		
		@OneToMany(fetch = FetchType.LAZY, mappedBy = "vitalCategoryMaster", cascade = CascadeType.ALL, orphanRemoval = true)
		private List<VitalQualifireMaster> listVitalQualifireMaster;

		public Integer getVitalCategoryId() {
			return vitalCategoryId;
		}

		public void setVitalCategoryId(Integer vitalCategoryId) {
			this.vitalCategoryId = vitalCategoryId;
		}

		public String getVitalCategoryCode() {
			return vitalCategoryCode;
		}

		public void setVitalCategoryCode(String vitalCategoryCode) {
			this.vitalCategoryCode = vitalCategoryCode;
		}

		public String getVitalCategoryDesc() {
			return vitalCategoryDesc;
		}

		public void setVitalCategoryDesc(String vitalCategoryDesc) {
			this.vitalCategoryDesc = vitalCategoryDesc;
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
