package com.param.adt.discharge.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


	@Entity
	@Table(name = "t_discharge_sum_tab_columns_print_mapper", schema = "emr")
	@SequenceGenerator(name = "t_discharge_sum_tab_columns_print_mapper_seq", sequenceName = "emr.t_discharge_sum_tab_columns_print_mapper_seq", allocationSize = 1)
	public class DischargeSumTabColumnsPrintMapper {	
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_discharge_sum_tab_columns_print_mapper_seq")
		@Column(name = "disch_sum_tab_col_id")
		private Integer dischSumTabColId;
		
		@Column(name = "discharge_field_id")
		private Integer dischargeFieldId;
		
		@Column(name = "tab_column_json")
		private String tabColumnJson;
		
		@Column(name = "created_by")
		private Integer createdBy;
		
		@Column(name = "created_date")
		private Date createdDate;
		
		@Column(name = "status")
		private Character status;
		
		@Column(name = "updated_by")
		private Integer updatedBy;
		
		@Column(name = "updated_date")
		private Date updatedDate;
		
		@Column(name = "unit_id")
		private Integer unitId;
		
		@Column(name = "organization_id")
		private Integer organizationId;

		

		public Integer getDischSumTabColId() {
			return dischSumTabColId;
		}

		public void setDischSumTabColId(Integer dischSumTabColId) {
			this.dischSumTabColId = dischSumTabColId;
		}

		public Integer getDischargeFieldId() {
			return dischargeFieldId;
		}

		public void setDischargeFieldId(Integer dischargeFieldId) {
			this.dischargeFieldId = dischargeFieldId;
		}

		public String getTabColumnJson() {
			return tabColumnJson;
		}

		public void setTabColumnJson(String tabColumnJson) {
			this.tabColumnJson = tabColumnJson;
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

		public Character getStatus() {
			return status;
		}

		public void setStatus(Character status) {
			this.status = status;
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

		public Integer getUnitId() {
			return unitId;
		}

		public void setUnitId(Integer unitId) {
			this.unitId = unitId;
		}

		public Integer getOrganizationId() {
			return organizationId;
		}

		public void setOrganizationId(Integer organizationId) {
			this.organizationId = organizationId;
		}
		
		
}
