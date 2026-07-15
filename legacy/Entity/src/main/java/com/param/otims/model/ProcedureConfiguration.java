package com.param.otims.model;

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

import com.param.global.model.ServiceMaster;
import com.param.global.model.SpecialityMaster;

@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_PROCEDURE_CONFIGURATION_LIST",
					 query = "select pconf.procedure_config_id as \"procedureConfigId\" , pconf.cpt_code as \"cptCode\", pconf.cpt_description as \"cptDescription\", pconf.duration as \"duration\", "
							 + " pconf.speciality_id as \"specialityId\", pconf.service_id as \"serviceId\", pconf.procedure_type_id as \"procedureTypeId\",  "
							 + " pconf.anesthesia_type_id as \"anesthesiaTypeId\", pconf.status as \"status\", pconf.org_id as \"orgId\", pconf.unit_id as \"unitId\", pconf.created_by as \"createdBy\", "
							 + " to_char(pconf.created_date, 'DD-MM-YYYY HH24:MI:SS') as \"createdDate\", pconf.updated_by as \"updatedBy\", to_char(pconf.updated_date, 'DD-MM-YYYY HH24:MI:SS') as \"updatedDate\", "
							 + " protyp.procedure_type_name as \"procedureType\", anes.anesthesia_type_name as \"anesthesiaType\", spc.speciality_name as \"speciality\", srv.service_standard_name as \"service\" "
							 + " from otims.t_procedure_configuration pconf "
							 + " inner join otims.m_procedure_type_master protyp "
							 + " on protyp.procedure_type_id = pconf.procedure_type_id "
							 + " inner join otims.m_anesthesia_type_master anes "
							 + " on anes.anesthesia_type_id = pconf.anesthesia_type_id "
							 + " inner join doctor.m_speciality_master spc "
							 + " on spc.speciality_id = pconf.speciality_id "
							 + " inner join service.m_service_master srv "
							 + " on srv.service_master_id = pconf.service_id "
							 + " where pconf.org_id = :orgId and pconf.unit_id = :unitId  "
							 + " order by pconf.procedure_config_id desc "),
	
	@NamedNativeQuery(name = "VALID_PROCEDURE_CONFIG_BY_PROCID_ORG_UNIT",
					 query = " select exists(select pconf.procedure_config_id from otims.t_procedure_configuration pconf " 	
					 	   + " where pconf.service_id = :serviceId and pconf.org_id = :orgId and pconf.unit_id = :unitId)"),
	
	@NamedNativeQuery(name = "UPDATE_PROC_CONF_STATUS_BY_ID",
					 query = "update otims.t_procedure_configuration set status =:status "
							+"where procedure_config_id =:id "),
	
	@NamedNativeQuery(name = "VALID_PROCEDURE_CONFIG_BY_PROCID_ORG_UNIT_CONFIG",
	 query = " select exists(select pconf.procedure_config_id from otims.t_procedure_configuration pconf " 	
	 	   + " where pconf.service_id = :serviceId and pconf.org_id = :orgId and pconf.unit_id = :unitId and pconf.procedure_config_id <> :configId)"),
	
	@NamedNativeQuery(name = "VALID_PROCEDURE_CONFIG_BY_PROCID_ORG_UNIT_SERVICEID",
	 query = " select exists(select pconf.procedure_config_id from otims.t_procedure_configuration pconf " 	
	 	   + " where pconf.service_id = :serviceId and pconf.org_id = :orgId and pconf.unit_id = :unitId )"),
	
	@NamedNativeQuery(name = "SEARCH_SERVICES_FOR_PROCEDURE_ORDER",
					 query = "select srvmst.service_standard_name as \"serviceName\", srvmst.service_standard_code as \"serviceCode\", srvmst.speciality_id as \"specialityId\", "
							 + " srvmst.service_master_id as \"serviceId\", spc.speciality_name as \"speciality\", "
//							 + " srvmppr.contract_grade_master_id as \"contractGradeMasterId\", "
//							 + " contMast.grade_desc as \"gradeDesc\" ," 
							 + 
							 " prtype.procedure_type_name as \"procedureType\", "
							 + " proconf.duration as \"procedureDuration\", "
							 + "srvmst.sub_speciality_id as \"subSpecialityId\", proconf.procedure_type_id as \"procedureTypeId\" "
							 + " from service.m_service_master srvmst   "
							 + " inner join service.t_unit_service_master_details srvmppr  on srvmppr.service_master_id = srvmst.service_master_id   "
							 + " inner join doctor.m_speciality_master spc  on spc.speciality_id =  srvmst.speciality_id   "
							 + " inner join doctor.m_sub_speciality_master subspc  on subspc.sub_speciality_id = srvmst.sub_speciality_id   "
							 + " inner join otims.t_procedure_configuration proconf on proconf.service_id = srvmst.service_master_id "
							 + " left join public.m_surgery_grade_master surgery on " + 
							 "	srvmppr.surgery_grade_id = surgery.surgery_grade_id "
							// + " inner join service.m_unit_service_tariff_master ustm on ustm.service_id = srvmst.service_master_id "
							 + " inner join otims.m_procedure_type_master prtype on prtype.procedure_type_id = proconf.procedure_type_id "
							 + " where srvmppr.status = 'A' and proconf.unit_id = :unitId and proconf.org_id = :orgId   "
                             + " and srvmppr.unit_id = :unitId "
							 + " and (lower(trim(srvmst.service_standard_name)) like :serviceName  or  lower(trim(srvmst.service_standard_code)) like :serviceName )  "),
							// + " and ustm.unit_id = proconf.unit_id and ustm.tariff_id = :tariffId and ustm.payment_entitlement_id = :paymentEntitlementId  "
							// + " and ustm.visit_type_id = :visitTypeId and ustm.patient_type_id = :patientTypeId and ustm.billing_bed_category_id = :billingBedCategoryId and ustm.effective_date <= :effectiveDate  "),
	
	@NamedNativeQuery(name = "SEARCH_SERVICES_FOR_PROCEDURE_SERVICE_ORDERING",
					 query = "select srvmst.service_standard_name as \"serviceName\", srvmst.service_standard_code as \"serviceCode\", srvmst.speciality_id as \"specialityId\",  "
							 + " srvmst.service_master_id as \"serviceId\", spc.speciality_name as \"speciality\", ustm.rate as \"rate\", srvmst.sub_speciality_id as \"subSpecialityId\",  "
							 + " procedure_config.duration as \"procedureDuration\""
							 + " from service.m_service_master srvmst    "
							 + " inner join service.t_unit_service_master_details srvmppr  on srvmppr.service_master_id = srvmst.service_master_id    "
							 + " inner join doctor.m_speciality_master spc  on spc.speciality_id =  srvmst.speciality_id    "
							 + " inner join doctor.m_sub_speciality_master subspc  on subspc.sub_speciality_id = srvmst.sub_speciality_id    "
							 + " inner join service.m_unit_service_tariff_master ustm on ustm.service_id = srvmst.service_master_id  "
							 + " inner join otims.t_procedure_configuration procedure_config on ustm.service_id = procedure_config.service_id  "
							 + " where srvmppr.status = 'A' and srvmppr.unit_id = :unitId and srvmppr.organization_id = :orgId   "
							 + " and (lower(trim(srvmst.service_standard_name)) like :serviceName  or  lower(trim(srvmst.service_standard_code)) like :serviceName )   "
							 + " and ustm.unit_id = srvmppr.unit_id and ustm.tariff_id = :tariffId and ustm.payment_entitlement_id = :paymentEntitlementId   "
							 + " and ustm.visit_type_id = :visitTypeId and ustm.patient_type_id = :patientTypeId and ustm.billing_bed_category_id = :billingBedCategoryId and ustm.effective_date <= :effectiveDate "),
	
	@NamedNativeQuery(name = "GET_PROCEDURE_CONFIG_LIST_ON_TEMPLATE",
	 query = "select pconf.procedure_config_id as \"procedureConfigId\" , pconf.cpt_code as \"cptCode\", pconf.cpt_description as \"cptDescription\", pconf.duration as \"duration\", "
			 + " pconf.speciality_id as \"specialityId\", pconf.service_id as \"serviceId\", pconf.procedure_type_id as \"procedureTypeId\",  "
			 + " pconf.anesthesia_type_id as \"anesthesiaTypeId\", pconf.status as \"status\", pconf.org_id as \"orgId\", pconf.unit_id as \"unitId\", pconf.created_by as \"createdBy\", "
			 + " to_char(pconf.created_date, 'DD-MM-YYYY HH24:MI:SS') as \"createdDate\", pconf.updated_by as \"updatedBy\", to_char(pconf.updated_date, 'DD-MM-YYYY HH24:MI:SS') as \"updatedDate\", "
			 + " protyp.procedure_type_name as \"procedureType\",pconf.operation_theatre_type_id as \"operationTheatreTypeId\", anes.anesthesia_type_name as \"anesthesiaType\", spc.speciality_name as \"speciality\", srv.service_standard_name as \"service\" "
			 + " from otims.t_procedure_configuration pconf "
			 + " inner join otims.m_procedure_type_master protyp "
			 + " on protyp.procedure_type_id = pconf.procedure_type_id "
			 + " inner join otims.m_anesthesia_type_master anes "
			 + " on anes.anesthesia_type_id = pconf.anesthesia_type_id "
			 + " inner join doctor.m_speciality_master spc "
			 + " on spc.speciality_id = pconf.speciality_id "
			 + " inner join service.m_service_master srv "
			 + " on srv.service_master_id = pconf.service_id "
			 + " where pconf.org_id = :orgId and pconf.unit_id = :unitId  "
			 + " order by pconf.procedure_config_id desc "),
	
	@NamedNativeQuery(name = "GET_PROCEDURE_CONFIG_OTT",
	 query = " select pconf.operation_theatre_type_id as \"operationTheatreTypeId\","
	 		+ " ott.theatre_type_name as \"theatreTypeName\","
	 		+ " procType.procedure_type_name as \"procedureTypeName\"  "
	 		+ "from otims.t_procedure_configuration pconf "
	 		+ " inner join otims.m_operation_theatre_type ott on pconf.operation_theatre_type_id = ott.operation_theatre_type_id " 	
	 		+ " inner join otims.m_procedure_type_master procType on pconf.procedure_type_id = procType.procedure_type_id " 	
	 	   + " where pconf.procedure_config_id =:procedureConfigId "),
	
	
	@NamedNativeQuery(name = "GET_ORDER_DETAILS_IDS_LIST",
	 query = " select orderDtl.order_details_id as \"orderDetailsId\" ,orderMstr.encounter_id as \"encounterId\" from public.t_order_details orderDtl " + 
	 		"inner join public.t_order_master orderMstr on orderDtl.order_id = orderMstr.order_id " + 
	 		"where  orderDtl.procedure_order_mst_id =:proReqId "),
	
	@NamedNativeQuery(name="GET_NOTELIST_BY_PROCONFIG_ID",
	query="SELECT configNote.procedure_config_note_id AS \"procedureConfigNoteId\" , "
			+ " configNote.procedure_config_id AS \"procedureConfigId\", "
			+ " configNote.note_master_id AS \"noteMasterId\", "
			+ " configNote.note_type_id AS \"noteType\", "
			+ " configNote.template AS \"template\", "
			+ " noteMstr.description AS \"description\" "
			+ " FROM otims.t_procedure_config_note configNote "
			+ " INNER JOIN otims.m_note_master noteMstr on configNote.note_master_id = noteMstr.note_master_id "  
			+ " WHERE  configNote.organization_id = :orgId "
			+ " AND configNote.procedure_config_id = :proConfigId  "
			+ " AND configNote.unit_id= :unitId "
			+ " AND configNote.note_type_id in (2,3)  "
			+ " AND configNote.status = 'A' "
		)

})
@Entity
@Table(name = "t_procedure_configuration" , schema = "otims")
@SequenceGenerator(name = "t_procedure_configuration_seq" , sequenceName = "otims.t_procedure_configuration_seq" , allocationSize = 1)
public class ProcedureConfiguration {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_procedure_configuration_seq")
	@Column(name = "procedure_config_id")
	private int procedureConfigId;
	
	@Column(name = "cpt_code")
	private String cptCode;
	
	@Column(name = "cpt_description")
	private String cptDescription;
	
	@Column(name = "duration")
	private int duration;
	
	@Column(name = "speciality_id")
	private Integer specialityId;
	
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "procedure_type_id")
	private Integer procedureTypeId;
	
	@Column(name = "anesthesia_type_id")
	private Integer anesthesiaTypeId;
	
	@Column(name = "status")
	private char status;
	
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
	
	@Column(name = "operation_theatre_type_id")
	private Integer operationTheatreTypeId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "anesthesia_type_id" , insertable = false , updatable = false , nullable = false)
	private AnesthesiaTypeMaster anasthesiaTypeMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "procedure_type_id" , insertable = false , updatable = false , nullable = false)
	private ProcedureTypeMaster procedureTypeMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id" , insertable = false , updatable = false , nullable = false)
	private ServiceMaster serviceMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "speciality_id" , insertable = false , updatable = false , nullable = false)
	private SpecialityMaster specialityMaster;

	public int getProcedureConfigId() {
		return procedureConfigId;
	}

	public void setProcedureConfigId(int procedureConfigId) {
		this.procedureConfigId = procedureConfigId;
	}

	public String getCptCode() {
		return cptCode;
	}

	public void setCptCode(String cptCode) {
		this.cptCode = cptCode;
	}

	public String getCptDescription() {
		return cptDescription;
	}

	public void setCptDescription(String cptDescription) {
		this.cptDescription = cptDescription;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getProcedureTypeId() {
		return procedureTypeId;
	}

	public void setProcedureTypeId(Integer procedureTypeId) {
		this.procedureTypeId = procedureTypeId;
	}

	public Integer getAnesthesiaTypeId() {
		return anesthesiaTypeId;
	}

	public void setAnesthesiaTypeId(Integer anesthesiaTypeId) {
		this.anesthesiaTypeId = anesthesiaTypeId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'I');
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

	public AnesthesiaTypeMaster getAnasthesiaTypeMaster() {
		return anasthesiaTypeMaster;
	}

	public void setAnasthesiaTypeMaster(AnesthesiaTypeMaster anasthesiaTypeMaster) {
		this.anasthesiaTypeMaster = anasthesiaTypeMaster;
	}

	public ProcedureTypeMaster getProcedureTypeMaster() {
		return procedureTypeMaster;
	}

	public void setProcedureTypeMaster(ProcedureTypeMaster procedureTypeMaster) {
		this.procedureTypeMaster = procedureTypeMaster;
	}

	public ServiceMaster getServiceMaster() {
		return serviceMaster;
	}

	public void setServiceMaster(ServiceMaster serviceMaster) {
		this.serviceMaster = serviceMaster;
	}

	public SpecialityMaster getSpecialityMaster() {
		return specialityMaster;
	}

	public void setSpecialityMaster(SpecialityMaster specialityMaster) {
		this.specialityMaster = specialityMaster;
	}

	public Integer getOperationTheatreTypeId() {
		return operationTheatreTypeId;
	}

	public void setOperationTheatreTypeId(Integer operationTheatreTypeId) {
		this.operationTheatreTypeId = operationTheatreTypeId;
	}

	
}