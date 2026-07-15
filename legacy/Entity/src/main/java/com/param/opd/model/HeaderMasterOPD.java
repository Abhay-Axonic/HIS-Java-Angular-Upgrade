package com.param.opd.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

	@Entity
	@Table(name = "m_header_master", schema = "lab")
	@SequenceGenerator(name = "m_seq_header_master", sequenceName = "lab.m_seq_header_master", allocationSize = 1)
	public class HeaderMasterOPD
	{
		@Id
		@Column(name = "header_id")
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_seq_header_master")
		private int headerId;
		@Column(name="header_code")
		private String code;

		@Column(name="header_desc")
		private String desc;

		@Column(name="header_status")
		private char status; 

		@Column(name="crated_by")
		private int createdBy;

		@Column(name="created_date")
		private Long createdDate;
		
		@Column(name="updated_by")
		private int updatedBy;
		
		@Column(name="updated_date")
		private Long updatedDate;
		
		@Column(name = "org_id")
		private Integer orgId;
		
		@Column(name = "org_unit_id")
		private Integer unitId;
		

		public int getHeaderId() {
			return headerId;
		}

		public void setHeaderId(int headerId) {
			this.headerId = headerId;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public char getStatus() {
			return status;
		}

		public void setStatus(char status) {
			this.status = status;
		}

		public int getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(int createdBy) {
			this.createdBy = createdBy;
		}

		public Long getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(Long createdDate) {
			this.createdDate = createdDate;
		}

		public int getUpdatedBy() {
			return updatedBy;
		}

		public void setUpdatedBy(int updatedBy) {
			this.updatedBy = updatedBy;
		}

		public Long getUpdatedDate() {
			return updatedDate;
		}

		public void setUpdatedDate(Long updatedDate) {
			this.updatedDate = updatedDate;
		}

		public Integer getOrgId() {
			return orgId;
		}

		public void setOrgId(Integer orgId) {
			this.orgId = orgId;
		}

		public Integer getUnitId() {
			return unitId;
		}

		public void setUnitId(Integer unitId) {
			this.unitId = unitId;
		}

	}
