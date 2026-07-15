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
	
	@NamedQuery(name="GET_AR_CODE_MASTER_LIST",
		query=" SELECT aRCodeMaster.aRCodeId AS aRCodeId, "
				+ "aRCodeMaster.aRCodeDesc AS aRCodeDesc, "
				+ "aRCodeMaster.aRCode AS aRCode, "
				+ "aRCodeMaster.status AS status "
				+ " FROM ARCodeMaster aRCodeMaster "
				+ " WHERE aRCodeMaster.orgId=:orgId "
				+ " AND aRCodeMaster.unitId=:unitId "
				+ " ORDER BY aRCodeMaster.aRCodeId asc "
				),
	
	@NamedQuery(name="GET_AR_CODE_MASTER_BY_ID",
		query="SELECT aRCodeMaster.aRCodeId AS aRCodeId, "
				+ "aRCodeMaster.aRCodeDesc AS aRCodeDesc, "
				+ "aRCodeMaster.aRCode AS aRCode, "
				+ "aRCodeMaster.status AS status "
				+ " FROM ARCodeMaster aRCodeMaster"
				+ " WHERE aRCodeMaster.aRCodeId = :aRCodeId"
				+ " AND aRCodeMaster.orgId=:orgId "
				+ " AND aRCodeMaster.unitId=:unitId "
				),
	
	@NamedQuery(name="GET_ACTIVE_AR_CODE_MASTER",
		query="SELECT aRCodeMaster.aRCodeId AS aRCodeId, "
				+ "aRCodeMaster.aRCodeDesc AS aRCodeDesc, "
				+ "aRCodeMaster.aRCode AS aRCode, "
				+ "aRCodeMaster.status AS status "
				+ " FROM ARCodeMaster aRCodeMaster"
				+ " WHERE aRCodeMaster.status= 'A'"
				+ " AND aRCodeMaster.orgId=:orgId "
				+ " AND aRCodeMaster.unitId=:unitId "
				+ " ORDER BY aRCodeId asc "
				),
	
	@NamedQuery(name="GET_AR_CODE_MASTER_BY_NAME",
		query="SELECT aRCodeMaster.aRCodeId AS aRCodeId, "
				+ "aRCodeMaster.aRCodeDesc AS aRCodeDesc, "
				+ "aRCodeMaster.aRCode AS aRCode, "
				+ "aRCodeMaster.status AS status "
				+ " FROM ARCodeMaster aRCodeMaster"
				+ " WHERE LOWER(aRCodeMaster.aRCodeDesc)= :aRCodeDesc "
				+ " AND aRCodeMaster.orgId=:orgId "
				+ " AND aRCodeMaster.unitId=:unitId "
				),
	
	@NamedQuery(name="GET_AR_CODE_MASTER_BY_NAME_NOT_ID",
	query="SELECT aRCodeMaster.aRCodeId AS aRCodeId, "
				+ "aRCodeMaster.aRCodeDesc AS aRCodeDesc, "
				+ "aRCodeMaster.aRCode AS aRCode, "
				+ "aRCodeMaster.status AS status "
				+ " FROM ARCodeMaster aRCodeMaster"
				+ " WHERE LOWER(aRCodeMaster.aRCodeDesc)= :aRCodeDesc "
				+ " AND aRCodeMaster.aRCodeId != :aRCodeId "
				+ " AND aRCodeMaster.orgId=:orgId "
				+ " AND aRCodeMaster.unitId=:unitId "
				)
})

	@Entity
	@Table(name = "m_ar_code_master", schema = "public")
	@SequenceGenerator(name="m_ar_code_master_seq", sequenceName="public.m_ar_code_master_seq", allocationSize=1)
	public class ARCodeMaster {
		
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_ar_code_master_seq")
		@Column(name = "ar_code_id")
		private Integer aRCodeId;
		
		@Column(name = "ar_code")
		private String aRCode;
		
		@Column(name = "ar_code_desc")
		private String aRCodeDesc;
		
		@Column(name = "status")
		private Character status;
		
		@Column(name = "created_by")
		private Integer createdBy;
		
		@Column(name = "updated_date")
		private Date updatedDate;
		
		@Column(name = "updated_by")
		private Integer updatedBy;
		
		@Column(name = "created_date")
		private Date createdDate;
		
		@Column(name="org_id")
		private Integer orgId;
		
		@Column(name="unitId")
		private Integer unitId;
		
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

		public Integer getaRCodeId() {
			return aRCodeId;
		}

		public void setaRCodeId(Integer aRCodeId) {
			this.aRCodeId = aRCodeId;
		}

		public String getaRCode() {
			return aRCode;
		}

		public void setaRCode(String aRCode) {
			this.aRCode = aRCode;
		}

		public String getaRCodeDesc() {
			return aRCodeDesc;
		}

		public void setaRCodeDesc(String aRCodeDesc) {
			this.aRCodeDesc = aRCodeDesc;
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
		
		
}
