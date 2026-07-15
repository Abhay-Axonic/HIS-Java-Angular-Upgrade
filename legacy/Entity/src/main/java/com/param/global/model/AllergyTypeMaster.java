package com.param.global.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
	
	@NamedQuery(name="GET_LIST_ALLERGY_TYPE_MAPPER",
			query=" SELECT 		allergyTypeMst.allergyTypeId as allergyTypeId, allergyTypeMst.allergyTypeName as allergyTypeName,"
				+ "				allergyTypeMst.allergyTypeDesc as allergyTypeDesc, allergyTypeMst.status as status, "
				+ "	      allergyTypeMst.isGeneric as isGeneric"
				+ " FROM 		AllergyTypeMaster allergyTypeMst"
				+ " WHERE 		allergyTypeMst.status = 'A'"
//				+ " AND 		allergyTypeMst.unitId=:unitId"
				+ " AND 		allergyTypeMst.organizationId=:organizationId "
				+ " ORDER BY allergyTypeMst.allergyTypeDesc ASC"
				),
	@NamedQuery(name="GET_ALLERGY_TYPE_MASTER_LIST",
	query=" SELECT    allergyTypeMst.allergyTypeId as allergyTypeId, "
			+ "       allergyTypeMst.allergyTypeName as allergyTypeName,"
			+ "	      allergyTypeMst.allergyTypeDesc as allergyTypeDesc,"
			+ "	      allergyTypeMst.isGeneric as isGeneric,"
			+ " 	  allergyTypeMst.status as status"
			+ " FROM  AllergyTypeMaster allergyTypeMst"
			+ " WHERE allergyTypeMst.organizationId=:orgId "
//			+ " AND  allergyTypeMst.unitId=:unitId   "
			+ " ORDER BY allergyTypeMst.allergyTypeDesc ASC"
	),
	@NamedQuery(name="GET_ALLERGY_TYPE_MASTER_LIST_BY_ID",
	query=" SELECT    allergyTypeMst.allergyTypeId as allergyTypeId, "
			+ "       allergyTypeMst.allergyTypeName as allergyTypeName,"
			+ "	      allergyTypeMst.allergyTypeDesc as allergyTypeDesc,"
			+ "	      allergyTypeMst.isGeneric as isGeneric,"
			+ " 	  allergyTypeMst.status as status "
			+ " FROM  AllergyTypeMaster allergyTypeMst "
			+ " WHERE allergyTypeMst.organizationId=:orgId "
//			+ " AND   allergyTypeMst.unitId=:unitId "
			+ " AND   allergyTypeMst.allergyTypeId=:allergyTypeId"
	),
	@NamedQuery(name="GET_ALLERGY_TYPE_MASTER_LIST_BY_NAME",
	query=" SELECT    allergyTypeMst.allergyTypeId as allergyTypeId "			
			+ " FROM  AllergyTypeMaster allergyTypeMst "
			+ " WHERE allergyTypeMst.organizationId=:orgId "
//			+ " AND  allergyTypeMst.unitId=:unitId "
			+ " AND   LOWER(allergyTypeMst.allergyTypeDesc)=:name"
	),
	@NamedQuery(name="GET_ALLERGY_TYPE_MASTER_LIST_BY_NAME_NOT_BY_ID",
	query=" SELECT    allergyTypeMst.allergyTypeId as allergyTypeId "			
			+ " FROM  AllergyTypeMaster allergyTypeMst "
			+ " WHERE allergyTypeMst.organizationId=:orgId  "
			+ " AND   LOWER(allergyTypeMst.allergyTypeDesc)=:name "
			+ " AND   allergyTypeMst.allergyTypeId!=:allergyTypeId "
	),
	@NamedQuery(name="GET_ALLERGY_TYPE_MASTER_LIST_CHECK_ISDRUG_BY_ID",
	query=" SELECT    allergyTypeMst.allergyTypeId as allergyTypeId "			
			+ " FROM  AllergyTypeMaster allergyTypeMst "
			+ " WHERE allergyTypeMst.organizationId=:orgId"
			+ " AND isGeneric = 'Y' "
			+ " AND   allergyTypeMst.allergyTypeId!=:allergyTypeId "
	),
	@NamedQuery(name="GET_ALLERGY_TYPE_MASTER_LIST_CHECK_ISDRUG",
	query=" SELECT    allergyTypeMst.allergyTypeId as allergyTypeId "			
			+ " FROM  AllergyTypeMaster allergyTypeMst "
			+ " WHERE allergyTypeMst.organizationId=:orgId"
			+ " AND isGeneric = 'Y' "
	)
})


@Entity
@Table(name="m_allergy_type_master",schema="emr")
@SequenceGenerator(name = "allergy_type_master_seq", sequenceName = "emr.allergy_type_master_seq", allocationSize = 1)
public class AllergyTypeMaster {

	  	@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "allergy_type_master_seq")
		@Column(name = "allergy_type_id")
		private Integer allergyTypeId;

		@Column(name = "allergy_type_name")
		private String allergyTypeName;

		@Column(name = "allergy_type_desc")
		private String allergyTypeDesc;
		
		@Column(name="status")
		private char status;
		
		
		@Column(name="is_generic")
		private char isGeneric;
		
		
		@Column(name="created_by")
		private Integer createdBy;
		
		@Column(name="updated_by")
		private Integer updatedBy;
		
		@Column(name="created_date")
		private Date createdDate;
		
		@Column(name="updated_date")
		private Date updatedDate;
		
		@Column(name="unit_id")
		private Integer unitId;
		
		@Column(name="organization_id")
		private Integer organizationId;
		
		@Column(name = "marathi_allergy_type_name")
		private String marathiAllergyTypeName;
		
		@OneToMany(fetch = FetchType.LAZY,mappedBy = "allergyTypeMaster", cascade = CascadeType.ALL, orphanRemoval = true)
		private List<AllergyMaster> AllergyMaster;

		
		
		
		
		public char getIsGeneric() {
			return isGeneric;
		}

		public void setIsGeneric(char isGeneric) {
			this.isGeneric = isGeneric;
		}

		public List<AllergyMaster> getAllergyMaster() {
			return AllergyMaster;
		}

		public void setAllergyMaster(List<AllergyMaster> allergyMaster) {
			AllergyMaster = allergyMaster;
		}

		public Integer getAllergyTypeId() {
			return allergyTypeId;
		}

		public void setAllergyTypeId(Integer allergyTypeId) {
			this.allergyTypeId = allergyTypeId;
		}

		public String getAllergyTypeName() {
			return allergyTypeName;
		}

		public void setAllergyTypeName(String allergyTypeName) {
			this.allergyTypeName = allergyTypeName;
		}

		public String getAllergyTypeDesc() {
			return allergyTypeDesc;
		}

		public void setAllergyTypeDesc(String allergyTypeDesc) {
			this.allergyTypeDesc = allergyTypeDesc;
		}

		public char getStatus() {
			return status;
		}

		public void setStatus(char status) {
			this.status = status;
		}

		public Integer getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(Integer createdBy) {
			this.createdBy = createdBy;
		}

		public Integer getUpdatedBy() {
			return updatedBy;
		}

		public void setUpdatedBy(Integer updatedBy) {
			this.updatedBy = updatedBy;
		}

		public Date getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(Date createdDate) {
			this.createdDate = createdDate;
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

		public String getMarathiAllergyTypeName() {
			return marathiAllergyTypeName;
		}

		public void setMarathiAllergyTypeName(String marathiAllergyTypeName) {
			this.marathiAllergyTypeName = marathiAllergyTypeName;
		}
		
		
		
	  
}
