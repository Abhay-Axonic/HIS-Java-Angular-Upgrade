package com.param.entity.lis.unit;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
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

import com.param.entity.lis.common.LocalTimeConverter;
@NamedQueries({
	@NamedQuery(name = "GET_LANGUAGE_BY_LANGUAGE_ID", query = "SELECT languageMaster.languageId as id, "
			+ " languageMaster.code as code,"
			+ " languageMaster.desc as desc,"
			+ " languageMaster.status as status,"
			+ " languageMaster.orgId as orgId,"
			+ " languageMaster.createdBy as createdBy,"
			+ " languageMaster.createdDate as createdDate,"
			+ " languageMaster.updatedBy as updatedBy,"
			+ " languageMaster.unitId as unitId,"
			+ " languageMaster.updatedDate as updatedDate"
			+ " FROM LanguageMaster languageMaster "
			+ " WHERE languageMaster.languageId =:languageId "
			+ " AND languageMaster.orgId =:orgId "
			+ " AND languageMaster.unitId =:unitId"),
	
	@NamedQuery(name = "GET_LANGUAGE_BY_CODE", query = "SELECT languageMaster.languageId as id,"
			+ " languageMaster.code as code,"
			+ " languageMaster.desc as desc,"
			+ " languageMaster.status as status,"
			+ " languageMaster.createdBy as createdBy,"
			+ " languageMaster.createdDate as createdDate,"
			+ " languageMaster.updatedBy as updatedBy,"
			+ " languageMaster.unitId as unitId,"
			+ " languageMaster.updatedDate as updatedDate"
			+ " FROM LanguageMaster languageMaster"
			+ " WHERE languageMaster.orgId =:orgId "
//			+"  AND languageMaster.unitId =:unitId "
			+ " AND lower(languageMaster.code) = lower(:code)"),
			
			@NamedQuery(name = "UPDATE_GET_LANGUAGE_BY_CODE", query = "SELECT languageMaster.languageId as id,"
					+ " languageMaster.code as code,"
					+ " languageMaster.desc as desc,"
					+ " languageMaster.status as status,"
					+ " languageMaster.createdBy as createdBy,"
					+ " languageMaster.createdDate as createdDate,"
					+ " languageMaster.updatedBy as updatedBy,"
					+ " languageMaster.unitId as unitId,"
					+ " languageMaster.updatedDate as updatedDate"
					+ " FROM LanguageMaster languageMaster "
					+ " WHERE languageMaster.orgId =:orgId "
//					+"  AND languageMaster.unitId =:unitId "
					+ " AND lower(languageMaster.code) = lower(:code) "
					+ " AND languageMaster.languageId <> :languageId "),

	@NamedQuery(name = "GET_PAGINATED_LANGUAGE_MASTER_LIST", query = "SELECT languageMaster.languageId as id,"
			+ " languageMaster.code as code,"
			+ " languageMaster.desc as desc,"
			+ " languageMaster.status as status,"
			+ " languageMaster.createdBy as createdBy,"
			+ " languageMaster.orgId as orgId,"
			+ " languageMaster.unitId as unitId,"
			+ " languageMaster.createdDate as createdDate,"
			+ " languageMaster.updatedBy as updatedBy,"
			+ " languageMaster.updatedDate as updatedDate "
			+ " FROM LanguageMaster languageMaster "
			+ " WHERE languageMaster.orgId =:orgId "
//			+ " and languageMaster.unitId =:unitId "
			+ " ORDER BY id DESC ")

})

@NamedNativeQueries(
{ 
	@NamedNativeQuery(name = "GET_TOTAL_LANGUAGE_RECORD", query = "select count(*) from public.m_language_master  where org_id =:orgId "),
	@NamedNativeQuery(name = "GET_TOTAL_LANGUAGE_RECORD_ACTIVE", 
			query = "select lm.language_id as \"languageId\" , "
					+ " lm.language_desc as \"desc\" , "
					+ "lm.language_code as \"code\"  from public.m_language_master lm  where lm.org_id =:orgId"
//					+ " and lm.unit_id =:unitId  "
					+ " and lm.language_status ='A'"
					+ "ORDER BY lm.language_desc ASC")

	
})


	@Entity
	@Table(name = "m_language_master ", schema = "public")
	@SequenceGenerator(name = "m_seq_language_master", sequenceName = "public.m_seq_language_master", allocationSize = 1)
	public class LanguageMaster
	{
		@Id
		@Column(name = "language_id")
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_seq_language_master")
		private Integer languageId;
		
		@Column(name="language_code")
		private String code;

		@Column(name="language_desc")
		private String desc;

		@Column(name="language_status")
		private Character status; 

		@Column(name="crated_by")
		private Integer createdBy;

		@Column(name="created_date")
		private Date createdDate;
		
		@Column(name="updated_by")
		private Integer updatedBy;
		
		@Column(name="updated_date")
		private Date updatedDate;
		
		@Column(name = "org_id")
		private Integer orgId;
		
		@Column(name = "unit_id")
		private Integer unitId;

		public Integer getLanguageId() {
			return languageId;
		}

		public void setLanguageId(Integer languageId) {
			this.languageId = languageId;
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

		public Integer getUnitId() {
			return unitId;
		}

		public void setUnitId(Integer unitId) {
			this.unitId = unitId;
		}

		
	}
