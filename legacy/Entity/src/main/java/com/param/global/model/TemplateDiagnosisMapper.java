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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.opd.coversheet.model.DiagnosisMaster;
@NamedQueries({

			
		@NamedQuery(name = "CHECK_ACTIVE_TEMPLATE_DIGNOSIS_MAPPER_AVAILABLE",
		query = "SELECT templateDiagnosisMapper.templateDiagnosisMapperId as templateDiagnosisMapperId,"
			+ " templateDiagnosisMapper.templateId as templateId "
			+ " FROM TemplateDiagnosisMapper templateDiagnosisMapper"
			+ " WHERE templateDiagnosisMapper.organisationId= :organisationId "
			+ " AND templateDiagnosisMapper.unitId= :unitId "
			+ " AND templateDiagnosisMapper.templateId= :templateId"),

		

		@NamedQuery(name = "GET_TEMPLATE_DIGNOSIS_MAPPER_DETAILED_LIST",
		query = "SELECT templateDiagnosisMapper.templateDiagnosisMapperId as templateDiagnosisMapperId,"
		    + " templateDiagnosisMapper.diagnosisId as diagnosisId,"
			+ " templateDiagnosisMapper.templateId as templateId,"
			+ " templateMaster.title as title, "
			+ " diagnosisMaster.diagnosisDesc as diagnosisDesc,"
			+ " diagnosisMaster.code as diagnosisName"
			+ " FROM TemplateDiagnosisMapper templateDiagnosisMapper"
			+ " INNER JOIN templateDiagnosisMapper.templateMaster templateMaster"
			+ " INNER JOIN templateDiagnosisMapper.diagnosisMaster diagnosisMaster"
			+ " WHERE templateDiagnosisMapper.organisationId= :organisationId "
			+ " AND templateDiagnosisMapper.unitId= :unitId "
			+ " AND templateDiagnosisMapper.templateId= :templateId"
			+ " AND templateDiagnosisMapper.isDeleted='N'  "

				),
		
		
		@NamedQuery(name = "GET_ACTIVE_TEMPLATE_DIGNOSIS_MAPPER_BY_ID",
		query = "SELECT templateDiagnosisMapper.templateDiagnosisMapperId as templateDiagnosisMapperId,"
		    + " templateDiagnosisMapper.diagnosisId as diagnosisId,"
			+ " templateDiagnosisMapper.templateId as templateId,"
			+ " templateDiagnosisMapper.status as status,"
			+ " templateDiagnosisMapper.isDeleted as isDeleted,"
			+ " templateDiagnosisMapper.organisationId as organisationId,"
			+ " templateDiagnosisMapper.unitId as unitId, "
			+ " templateMaster.title as title, "
			+ " diagnosisMaster.diagnosisDesc as diagnosisDesc"
			+ " FROM TemplateDiagnosisMapper templateDiagnosisMapper"
			+ " INNER JOIN templateDiagnosisMapper.templateMaster templateMaster"
			+ " INNER JOIN templateDiagnosisMapper.diagnosisMaster diagnosisMaster"
			+ " WHERE templateDiagnosisMapper.organisationId= :organisationId "
			+ " AND templateDiagnosisMapper.unitId= :unitId "
			+ " AND templateDiagnosisMapper.templateId= :templateId "
			+ " AND templateDiagnosisMapper.isDeleted!= 'Y' "

				),
		
		
		@NamedQuery(name = "GET_ACTIVE_TEMPLATE_DIGNOSIS_MAPPER",
		query = "SELECT templateDiagnosisMapper.templateDiagnosisMapperId as templateDiagnosisMapperId,"
		    + " templateDiagnosisMapper.diagnosisId as diagnosisId,"
			+ " templateDiagnosisMapper.templateId as templateId,"
			+ " templateDiagnosisMapper.status as status,"
			+ " templateDiagnosisMapper.isDeleted as isDeleted,"
			+ " templateDiagnosisMapper.organisationId as organisationId,"
			+ " templateDiagnosisMapper.unitId as unitId "
			+ " FROM TemplateDiagnosisMapper templateDiagnosisMapper"
			+ " WHERE templateDiagnosisMapper.organisationId= :organisationId "
			+ " AND templateDiagnosisMapper.unitId= :unitId "
			+ " AND templateDiagnosisMapper.status= 'A' "
			),
		
		@NamedQuery(name = "UPDATE_TEMPLATE_DIGNOSIS_MAPPER_STATUS",
		query = " UPDATE TemplateDiagnosisMapper templateDiagnosisMapper "
				+ " SET templateDiagnosisMapper.status = :status"
				+ " WHERE templateDiagnosisMapper.organisationId= :organisationId "
				+ " AND templateDiagnosisMapper.unitId= :unitId "
				+ " AND templateDiagnosisMapper.templateId = :templateId"),
		
		@NamedQuery(name = "UPDATE_TEMPLATE_DIGNOSIS_MAPPER",
		query = "UPDATE TemplateDiagnosisMapper templateDiagnosisMapper "
				+ "  SET templateDiagnosisMapper.isDeleted = :isDeleted"
				+ " WHERE templateDiagnosisMapper.organisationId= :organisationId "
				+ " AND templateDiagnosisMapper.unitId = :unitId"
				+ " AND templateDiagnosisMapper.templateId = :templateId"),
		
		@NamedQuery(name = "GET_LIST_OF_DIAGNOSIS",
		query = "SELECT listOfDiagnosis.diagnosisId as diagnosisId, listOfDiagnosis.diagnosisDesc as diagnosisDesc"
			
				+ " FROM DiagnosisMaster listOfDiagnosis"
				+ " WHERE listOfDiagnosis.organizationId= :organisationId "
				+ " AND listOfDiagnosis.unitId= :unitId "
		),

		
		
})

@NamedNativeQueries({
	@NamedNativeQuery(name="GET_LIST_OF_TEMPLATES", 
			query=" select distinct (tmp_mst.template_id) as \"templateId\", "
				+ " tmp_mst.title as \"title\" "
				+ " from public.t_template_master tmp_mst "
//				+ " inner join public.t_template_diagnosis_mapper mppr " 
//				+ " on tmp_mst.template_id = mppr.template_id "
				+ " where tmp_mst.template_type_id =17"
				+ " and tmp_mst.org_id= :organisationId"
				+ " and tmp_mst.unit_id= :unitId"
				+ " and tmp_mst.status ='A'"
				+ " ORDER BY tmp_mst.title ASC "
				),
	
			@NamedNativeQuery(name="GET_TEMPLATE_DIGNOSIS_MAPPER_LIST", 
			query=" select distinct (tmp_mst.template_id) as \"templateId\", "
				+ " tmp_mst.title as \"title\", mppr.status as \"status\"  "
				+ " from public.t_template_master tmp_mst "
				+ " inner join public.t_template_diagnosis_mapper mppr " 
				+ " on tmp_mst.template_id = mppr.template_id  where mppr.is_deleted='N' and tmp_mst.template_type_id=17 "
				+ " and tmp_mst.org_id = :organisationId  and tmp_mst.unit_id = :unitId "
			),
			

	@NamedNativeQuery(name="GET_TEMPLATE_DIGNOSIS_MAPPER_COUNT", 
			query=" select count(distinct(tmp_mst.template_id)) as \"templateId\" "
				+ " from public.t_template_master tmp_mst "
				+ " inner join public.t_template_diagnosis_mapper mppr " 
				+ " on tmp_mst.template_id = mppr.template_id "
				+ " where tmp_mst.template_type_id = 17 and mppr.is_deleted='N' "
			
				)
				})


@Entity
@Table(name = "t_template_diagnosis_mapper", schema = "public")
@SequenceGenerator(name = "seq_t_template_diagnosis_mapper", sequenceName = "public.seq_t_template_diagnosis_mapper", allocationSize = 1)
public class TemplateDiagnosisMapper {
	
	@Id
	@Column(name = "t_template_diagnosis_mapper_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_t_template_diagnosis_mapper")
	private Integer templateDiagnosisMapperId;
	
	@Column(name = "template_id")
	private Integer templateId;
	
	@Column(name = "diagnosis_id")
	private Integer diagnosisId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "organisation_id")
	private Integer organisationId;
	
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
	
	@Column(name = "is_deleted")
	private Character isDeleted;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "template_id", insertable = false, updatable = false)
	private TemplateMaster templateMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diagnosis_id", insertable = false, updatable = false)
	private DiagnosisMaster diagnosisMaster;

	public Integer getTemplateDiagnosisMapperId() {
		return templateDiagnosisMapperId;
	}

	public void setTemplateDiagnosisMapperId(Integer templateDiagnosisMapperId) {
		this.templateDiagnosisMapperId = templateDiagnosisMapperId;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public Integer getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Integer diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}

	public TemplateMaster getTemplateMaster() {
		return templateMaster;
	}

	public void setTemplateMaster(TemplateMaster templateMaster) {
		this.templateMaster = templateMaster;
	}

	public DiagnosisMaster getDiagnosisMaster() {
		return diagnosisMaster;
	}

	public void setDiagnosisMaster(DiagnosisMaster diagnosisMaster) {
		this.diagnosisMaster = diagnosisMaster;
	}

	
}
