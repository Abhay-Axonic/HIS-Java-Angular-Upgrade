package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
	
	@NamedQuery(name="GET_AP_CODE_MASTER_LIST",
		query="SELECT aPCodeMaster.aPCodeId AS aPCodeId, aPCodeMaster.aPCodeDesc AS aPCodeDesc, aPCodeMaster.aPCode AS aPCode, aPCodeMaster.status AS status "
					+ " FROM APCodeMaster aPCodeMaster"
					+ " WHERE aPCodeMaster.orgId=:orgId "
					+ " AND aPCodeMaster.unitId=:unitId "
					+ " ORDER BY aPCodeId asc"
				),
	
	@NamedQuery(name="GET_AP_CODE_MASTER_BY_ID",
	query="SELECT aPCodeMaster.aPCodeId AS aPCodeId, aPCodeMaster.aPCodeDesc AS aPCodeDesc, aPCodeMaster.aPCode AS aPCode, aPCodeMaster.status AS status "
				+ " FROM APCodeMaster aPCodeMaster"
				+ " WHERE aPCodeMaster.aPCodeId= :aPCodeId"
				+ " AND aPCodeMaster.orgId=:orgId "
				+ " AND aPCodeMaster.unitId=:unitId "),
	
	@NamedQuery(name="GET_ACTIVE_AP_CODE_MASTER",
			query="SELECT aPCodeMaster.aPCodeId AS aPCodeId, aPCodeMaster.aPCodeDesc AS aPCodeDesc, aPCodeMaster.aPCode AS aPCode, aPCodeMaster.status AS status "
				+ " FROM APCodeMaster aPCodeMaster"
				+ " WHERE aPCodeMaster.status= 'A'"
				+ " AND aPCodeMaster.orgId=:orgId "
				+ " AND aPCodeMaster.unitId=:unitId "
				+ " ORDER BY aPCodeId asc"
				),
	
	@NamedQuery(name="GET_AP_CODE_MASTER_BY_NAME",
	query="SELECT aPCodeMaster.aPCodeId AS aPCodeId, aPCodeMaster.aPCodeDesc AS aPCodeDesc, aPCodeMaster.aPCode AS aPCode, aPCodeMaster.status AS status "
				+ " FROM APCodeMaster aPCodeMaster"
				+ " WHERE LOWER(aPCodeMaster.aPCodeDesc)= :aPCodeDesc"
				+ " AND aPCodeMaster.orgId=:orgId "
				+ " AND aPCodeMaster.unitId=:unitId "),
	
	@NamedQuery(name="GET_AP_CODE_MASTER_BY_NAME_NOT_ID",
	query="SELECT aPCodeMaster.aPCodeId AS aPCodeId, aPCodeMaster.aPCodeDesc AS aPCodeDesc, aPCodeMaster.aPCode AS aPCode, aPCodeMaster.status AS status "
				+ " FROM APCodeMaster aPCodeMaster"
				+ " WHERE aPCodeMaster.aPCodeId != :aPCodeId AND LOWER(aPCodeMaster.aPCodeDesc)= :aPCodeDesc"
				+ " AND aPCodeMaster.orgId=:orgId "
				+ " AND aPCodeMaster.unitId=:unitId ")
})
	@Entity
	@Table(name = "m_ap_code_master", schema = "public")
	@SequenceGenerator(name="m_ap_code_master_seq", sequenceName="public.m_ap_code_master_seq", allocationSize=1)
	public class APCodeMaster {
		
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_ap_code_master_seq")
		@Column(name = "ap_code_id")
		private Integer aPCodeId;
		
		@Column(name = "ap_code")
		private String aPCode;
		
		@Column(name = "ap_code_desc")
		private String aPCodeDesc;
		
		@Column(name = "status")
		private Character status;
		
		@Column(name = "org_id")
		private Integer orgId;
		
		@Column(name = "unit_id")
		private Integer unitId;
		
		@Column(name = "created_by")
		private Integer createdBy;
		
		@Column(name = "created_date")
		private Date createdDate;
		
		@Column(name = "updated_by")
		private Integer updatedBy;
		
		@Column(name = "updated_date")
		private Date updatedDate;

		public Integer getaPCodeId() {
			return aPCodeId;
		}

		public void setaPCodeId(Integer aPCodeId) {
			this.aPCodeId = aPCodeId;
		}

		public String getaPCode() {
			return aPCode;
		}

		public void setaPCode(String aPCode) {
			this.aPCode = aPCode;
		}

		public String getaPCodeDesc() {
			return aPCodeDesc;
		}

		public void setaPCodeDesc(String aPCodeDesc) {
			this.aPCodeDesc = aPCodeDesc;
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

