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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.discharge.model.DischargeSummaryTemplateMaster;
import com.param.adt.master.unit.model.DepartmentNursingStationMapper;
import com.param.adt.master.unit.model.UnitSpecialityMapper;
import com.param.entity.org.model.GeneralLedgerMaster;
import com.param.opd.coversheet.model.EncounterMaster;
import com.param.otims.model.ProcedureConfiguration;
import com.param.otims.model.SurgeryResourceConfig;
import com.param.service.global.model.TContractCapDetails;
import com.param.service.global.model.TPackageCapDetails;

@NamedNativeQueries({

		@NamedNativeQuery(name = "GET_ACTIVE_SPECIALITY_LIST", query = "SELECT specialityMaster.speciality_id as \"specialityId\", "
				+ "specialityMaster.status as \"status\", "
				+ "specialityMaster.speciality_name as \"specialityName\", "
				+ "specialityMaster.is_surgical_code as \"isSurgicalCode\", "
				+ "usm.is_surgical_code as \"isSurgicalCodeNew\", "
				+ "usm.dept_wise_appointment as \"deptWiseAppointment\", "
				+ "usm.is_rendering_speciality as \"isRenderingSpeciality\", "
				+ "usm.is_clinical_speciality as \"isClinicalSpeciality\","
				+ "usm.is_kiosk_dept as \"isKioskDept\", "
				+ "usm.is_nursing_assessment_required as \"isNursingAssessmentRequired\", "
				+ "usm.allowed_overbooking_slots as \"allowedOverbookingSlots\", "
				+ "specialityMaster.organization_id as \"organizationId\", "
				+ "usm.unit_id as \"unitId\", "
				+ "usm.pdd_days as \"pddDays\" "
				+ "FROM doctor.m_speciality_master specialityMaster "
				+ "INNER JOIN public.t_unit_speciality_mapper usm on specialityMaster.speciality_id=usm.speciality_id "
				+ "WHERE specialityMaster.status='A' " 
				+ "AND specialityMaster.organization_id=:organizationId "
				+ "AND usm.unit_id=:unitId "
				+ "AND usm.status='A' "
				+ "ORDER BY specialityMaster.speciality_name "),

		@NamedNativeQuery(name = "GET_SPECIALITY_LIST", query = "SELECT	specialityMaster.speciality_id as \"specialityId\", "
				+ "specialityMaster.speciality_name as \"specialityName\", "
				//+ "specialityMaster.dept_wise_appointment as \"deptWiseAppointment\", "
				+ "specialityMaster.speciality_code as \"specialityCode\", "
				+ "ledgerMster.general_ledger_name as \"generalLedgerName\", "
				+ "specialityMaster.status as \"status\" "
				//+ "specialityMaster.is_surgical_code as \"isSurgicalCode\", "
				//+ "specialityMaster.is_rendering_speciality as \"isRenderingSpeciality\", "
				//+ "specialityMaster.is_clinical_speciality as \"isClinicalSpeciality\" ,"
				//+ "specialityMaster.is_kiosk_dept as \"isKioskDept\", "
//				+ "storeMstr.store_id as \"storeId\", "
//				+ "storeMstr.store_name as \"storeName\", "
				//+ "specialityMaster.is_nursing_assessment_required as \"isNursingAssessmentRequired\", "
				//+ "specialityMaster.allowed_overbooking_slots as \"allowedOverbookingSlots\" "
				+ "FROM 	doctor.m_speciality_master specialityMaster  "
				+ "INNER JOIN public.m_general_ledger_master ledgerMster ON specialityMaster.general_ledger_id=ledgerMster.general_ledger_id "
//				+ "left JOIN public.m_store storeMstr ON specialityMaster.store_id=storeMstr.store_id "
				+ "WHERE specialityMaster.organization_id=:orgId " 
				+ "AND specialityMaster.status = 'A' " 
				+ "ORDER BY specialityMaster.speciality_name "),

		@NamedNativeQuery(name = "GET_ORG_ACTIVE_SPECIALITY_LIST_NOT_MAPPED_TO_UNIT", query = "SELECT specialityMaster.speciality_id as \"specialityId\", "
				+ "specialityMaster.speciality_name as \"specialityName\", "
				+ "specialityMaster.speciality_code as \"specialityCode\", " 
				+ "specialityMaster.status as \"status\", "
				//+ "specialityMaster.is_surgical_code as \"isSurgicalCode\", "
				//+ "specialityMaster.is_clinical_speciality as \"isClinicalSpeciality\", "
				+ "specialityMaster.allowed_overbooking_slots as \"allowedOverbookingSlots\" "
				+ "FROM doctor.m_speciality_master specialityMaster  " 
				+ "WHERE specialityMaster.speciality_id NOT IN "
				+ "(SELECT usm.speciality_id as \"specialityId\" FROM public.t_unit_speciality_mapper usm where (usm.status='A' or usm.status='I') "
				+ " AND usm.unit_id=:unitId) " 
				+ " AND specialityMaster.organization_id=:orgId "
				+ " AND specialityMaster.status='A' " 
				+ "ORDER BY specialityMaster.speciality_name "),
		
		@NamedNativeQuery(name = "GET_SPECIALITY_LIST_OF_ACTIVE_INACTIVE", query = "SELECT	specialityMaster.speciality_id as \"specialityId\", "
				+ "specialityMaster.speciality_name as \"specialityName\", "
				//+ "specialityMaster.dept_wise_appointment as \"deptWiseAppointment\", "
				+ "specialityMaster.speciality_code as \"specialityCode\", "
				+ "ledgerMster.general_ledger_name as \"generalLedgerName\", "
				+ "specialityMaster.status as \"status\", "
				+ "specialityMaster.speciality_sac as \"specialitySAC\" "
				//+ "specialityMaster.is_surgical_code as \"isSurgicalCode\", "
				//+ "specialityMaster.is_rendering_speciality as \"isRenderingSpeciality\", "
				//+ "specialityMaster.is_clinical_speciality as \"isClinicalSpeciality\" ,"
				//+ "specialityMaster.is_kiosk_dept as \"isKioskDept\", "
//				+ "storeMstr.store_id as \"storeId\", "
//				+ "storeMstr.store_name as \"storeName\", "
				//+ "specialityMaster.is_nursing_assessment_required as \"isNursingAssessmentRequired\", "
				//+ "specialityMaster.allowed_overbooking_slots as \"allowedOverbookingSlots\" "
				+ "FROM 	doctor.m_speciality_master specialityMaster  "
				+ "INNER JOIN public.m_general_ledger_master ledgerMster ON specialityMaster.general_ledger_id=ledgerMster.general_ledger_id "
//				+ "left JOIN public.m_store storeMstr ON specialityMaster.store_id=storeMstr.store_id "
				+ "WHERE specialityMaster.organization_id=:orgId " 
//				+ "AND specialityMaster.status = 'A' " 
				+ "ORDER BY specialityMaster.speciality_name "),
		
		@NamedNativeQuery(name = "GET_ACTIVE_SPECIALITY_FOR_MODALITY_SCHEDULING",
		  query = " select  " 
				  + " 	distinct (msm.speciality_id) as \"specialityId\",  " 
				  + " 	msm.status as \"status\",  " 
				  + " 	msm.speciality_name as \"specialityName\",  " 
				  + " 	usm.dept_wise_appointment as \"deptWiseAppointment\",  " 
				  + " 	usm.is_rendering_speciality as \"isRenderingSpeciality\",  " 
				  + " 	usm.is_clinical_speciality as \"isClinicalSpeciality\",  " 
				  + " 	usm.is_kiosk_dept as \"isKioskDept\",  " 
				  + " 	usm.is_nursing_assessment_required as \"isNursingAssessmentRequired\"  " 
				  + " from  " 
				  + " 	doctor.m_speciality_master msm " 
				  + " 	inner join t_unit_speciality_mapper usm on  " 
				  + " 		msm.speciality_id = usm.speciality_id " 
				  + " where  " 
				  + " 	usm.status='A' " 
				  + " 	and usm.unit_id =:unitId   "
				  + "  ORDER BY msm.speciality_name ASC " 
				  /*+ " 	and msm.speciality_id in (  " 
				  + " 	select  " 
				  + " 		ser.speciality_id  " 
				  + " 	from  " 
				  + " 		service.t_unit_service_master_details tusmd  " 
				  + " 	inner join service.m_service_master ser on  " 
				  + " 		tusmd.service_master_id = ser.service_master_id  " 
				  + " 	where  " 
				  + " 		tusmd.procedure_type_id =:unitId  " 
				  + " 		and tusmd.is_procedure='Y'  " 
				  + " 		and tusmd.unit_id =:unitId  " 
				  + " 		and ser.speciality_id !=(  " 
				  + " 		select  " 
				  + " 			tuabc.radiology_speciality_id  " 
				  + " 		from  " 
				  + " 			t_unit_application_billing_configuration tuabc  " 
				  + " 		where  " 
				  + " 			unit_id =:unitId  " 
				  + " 			and status = 'A' ) )  "*/ 
				  + " 	 "),
		
		

})
@NamedQueries({

		@NamedQuery(name = "GET_SPECIALITY_LIST_FOR_SUB_SPE", query = "SELECT specialityMaster.specialityId as specialityId, "
				+ "specialityMaster.specialityName as specialityName " + "FROM SpecialityMaster as specialityMaster "
				+ "WHERE specialityMaster.status='A' "
				+ "AND specialityMaster.organizationId=:orgId ORDER BY specialityMaster.specialityName"),

		@NamedQuery(name = "GET_SPECIALITY_LIST_BY_ID", query = "SELECT	SpecialityMaster.specialityId as specialityId, "
				+ "SpecialityMaster.specialityName as specialityName, "
				+ "SpecialityMaster.createdBy as createdBy, "
				+ "SpecialityMaster.specialityCode as specialityCode, "
				+ "SpecialityMaster.specialityName as specialityName,"
				+ "SpecialityMaster.isSurgicalCode as isSurgicalCode,"
				+ "SpecialityMaster.isClinicalSpeciality as isClinicalSpeciality,"
				+ "SpecialityMaster.generalLedgerId as generalLedgerId,"
				+ "ledgerMster.generalLedgerName as generalLedgerName,"
//				+ "SpecialityMaster.storeId as storeId, "
				+ "SpecialityMaster.isRenderingSpeciality as isRenderingSpeciality, "
				+ "SpecialityMaster.deptWiseAppointment as deptWiseAppointment, "
				+ "SpecialityMaster.isKioskDept as isKioskDept, "
				+ "SpecialityMaster.isNursingAssessmentRequired as isNursingAssessmentRequired, "
				+ "SpecialityMaster.specialitySAC as specialitySAC, "
				+ "SpecialityMaster.allowedOverbookingSlots as allowedOverbookingSlots, "
				+ "SpecialityMaster.status as status "
				+ " FROM 		SpecialityMaster SpecialityMaster "
				+ " INNER JOIN SpecialityMaster.generalLedgerMaster ledgerMster  "
				+ "WHERE SpecialityMaster.specialityId = :specialityId "),

		@NamedQuery(name = "GET_SPECIALITY_LIST_BY_NAME_NOT_ID", query = "SELECT	SpecialityMaster.specialityId as specialityId, SpecialityMaster.specialityName as specialityName, "
				+ " 			SpecialityMaster.createdBy as createdBy, SpecialityMaster.specialityCode as specialityCode, SpecialityMaster.specialityName as specialityName,"
				+ "           SpecialityMaster.isSurgicalCode as isSurgicalCode, SpecialityMaster.status as status "
				+ " FROM 		SpecialityMaster SpecialityMaster "
				+ "WHERE ((LOWER(SpecialityMaster.specialityName) =:specialityName OR LOWER(SpecialityMaster.specialityName) =:specialityName)) AND SpecialityMaster.specialityId !=:specialityId and SpecialityMaster.organizationId=:orgId "),

		@NamedQuery(name = "GET_SPECIALITY_LIST_BY_NAME", query = "SELECT	SpecialityMaster.specialityId as specialityId, SpecialityMaster.specialityName as specialityName, "
				+ " 			SpecialityMaster.createdBy as createdBy, SpecialityMaster.specialityCode as specialityCode, SpecialityMaster.specialityName as specialityName,"
				+ "           SpecialityMaster.isSurgicalCode as isSurgicalCode, SpecialityMaster.status as status "
				+ " FROM 		SpecialityMaster SpecialityMaster "
				+ "WHERE (LOWER(SpecialityMaster.specialityName) =:specialityName OR SpecialityMaster.specialityName=:specialityName) and SpecialityMaster.organizationId=:orgId "),
		
		@NamedQuery(name = "GET_SPECIALITY_NAME_BY_ID",
		query = "SELECT	SpecialityMaster.specialityName as specialityName "
				+ "FROM SpecialityMaster SpecialityMaster "
				+ "WHERE SpecialityMaster.specialityId = :specialityId "),
		
		@NamedQuery(name = "GET_CHECK_SPECIALITY_IN_UNIT_MAPPER",
		query = "SELECT	usm.specialityId as specialityId "
				+ "FROM UnitSpecialityMapper as usm "
				+ "WHERE usm.specialityId = :specialityId and usm.organizationId =:organizationId and usm.status = 'A' "),
		
		
		/*@NamedQuery(name = "GET_STORE_BY_SPECIALTY_ID",
		query = "SELECT	specialityMaster.storeId as storeId "
				+ "FROM SpecialityMaster specialityMaster "
				+ "WHERE specialityMaster.specialityId = :specialityId "
				+ "AND specialityMaster.organizationId= :orgId "
				)*/
		
		@NamedQuery(name = "GET_PAGINATED_DEPARTMENT_MASTER_DETAILS_LIST_SEARCH",
		query = "SELECT	specialityMaster.specialityId as specialityId , "
				+ "specialityMaster.specialityName as specialityName , "
				+" specialityMaster.deptWiseAppointment as deptWiseAppointment , "
				+ "specialityMaster.specialityCode as specialityCode , "
				+ "ledgerMster.generalLedgerName as generalLedgerName , "
				+ "specialityMaster.status as status , "
				+ "specialityMaster.isSurgicalCode as isSurgicalCode , "
				+ "specialityMaster.isRenderingSpeciality as isRenderingSpeciality , "
				+ "specialityMaster.isClinicalSpeciality as isClinicalSpeciality  , "
				+ "specialityMaster.isKioskDept as isKioskDept , "
				+ "specialityMaster.isNursingAssessmentRequired as isNursingAssessmentRequired,  "
				+ "specialityMaster.allowedOverbookingSlots as allowedOverbookingSlots "
				+ "FROM 	SpecialityMaster specialityMaster  "
				+ "INNER JOIN specialityMaster.generalLedgerMaster ledgerMster  "
				+ "WHERE specialityMaster.organizationId=:orgId "
				+ " AND ( lower(specialityMaster.specialityName) like :specialityName "
				+ " OR lower(specialityMaster.specialityCode) like :specialityName "
				+ " OR lower(ledgerMster.generalLedgerName) like :specialityName "
				+ " OR lower(specialityMaster.isSurgicalCode) like :specialityName "
				+ " OR lower(specialityMaster.isRenderingSpeciality) like :specialityName "
				+ " OR lower(specialityMaster.isClinicalSpeciality) like :specialityName "
				+ " OR lower(specialityMaster.isKioskDept) like :specialityName "
				+ " OR lower(specialityMaster.isNursingAssessmentRequired) like :specialityName ) " 
				+ "ORDER BY specialityMaster.specialityName "),
})



@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "m_speciality_master", schema = "doctor")
@SequenceGenerator(name = "speciality_master_seq", sequenceName = "doctor.speciality_master_seq", allocationSize = 1)
public class SpecialityMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "speciality_master_seq")
	@Column(name = "speciality_id")
	private int specialityId;

	@Column(name = "speciality_name")
	private String specialityName;

	@Column(name = "speciality_code")
	private String specialityCode;

	@Column(name = "is_surgical_code")
	private char isSurgicalCode;

	@Column(name = "is_clinical_speciality")
	private char isClinicalSpeciality;

	@Column(name = "status")
	private char status;

	@Column(name = "created_by")
	private int createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "general_ledger_id")
	private Integer generalLedgerId;

	@Column(name = "updated_by")
	private int updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "is_rendering_speciality")
	private char isRenderingSpeciality;

	@Column(name = "dept_wise_appointment")
	private char deptWiseAppointment;
	
	@Column(name = "is_kiosk_dept")
	private Character isKioskDept;
	
	@Column(name = "is_nursing_assessment_required")
	private Character isNursingAssessmentRequired;
	
	@Column(name = "store_id")
	private Integer storeId;
	
	@Column(name = "allowed_overbooking_slots")
	private Integer allowedOverbookingSlots;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "specialityMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ServiceMaster> specialityList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "specialityMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EncounterMaster> encounterMastersList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "specialityMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ModalityMaster> listModalityMaster;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "specialityMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UnitSpecialityMapper> unitSpecialityMappersList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "specialityMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DoctorSpecialityMapper> doctorSpecialityMappersList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "specialityMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TPackageCapDetails> listTPackageCapDetails;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "specialityMaster")
	private List<TContractCapDetails> listTContractCapDetails;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "general_ledger_id", insertable = false, updatable = false)
	GeneralLedgerMaster generalLedgerMaster;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "specialityMaster")
	private List<ProcedureConfiguration> listProcedureConfiguration;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "specialityMaster")
	private List<SurgeryResourceConfig> listSurgeryResourceConfig;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "departmentMaster")
	private List<DepartmentNursingStationMapper> listDepartmentNursingStation;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "specialityMaster")
	private List<DischargeSummaryTemplateMaster> listDischargeSummaryTemplate;
	
	@Column(name = "speciality_SAC")
	private String specialitySAC;
	
	
	public String getSpecialitySAC() {
		return specialitySAC;
	}

	public void setSpecialitySAC(String specialitySAC) {
		this.specialitySAC = specialitySAC;
	}

	public Integer getAllowedOverbookingSlots() {
		return allowedOverbookingSlots;
	}

	public void setAllowedOverbookingSlots(Integer allowedOverbookingSlots) {
		this.allowedOverbookingSlots = allowedOverbookingSlots;
	}

	public Character getIsNursingAssessmentRequired() {
		return isNursingAssessmentRequired;
	}

	public void setIsNursingAssessmentRequired(Character isNursingAssessmentRequired) {
		this.isNursingAssessmentRequired = isNursingAssessmentRequired;
	}

	public SpecialityMaster() {
		super();
	}

	public SpecialityMaster(int specialityId) {
		super();
		this.specialityId = specialityId;
	}

	public List<SurgeryResourceConfig> getListSurgeryResourceConfig() {
		return listSurgeryResourceConfig;
	}

	public void setListSurgeryResourceConfig(List<SurgeryResourceConfig> listSurgeryResourceConfig) {
		this.listSurgeryResourceConfig = listSurgeryResourceConfig;
	}

	public List<ProcedureConfiguration> getListProcedureConfiguration() {
		return listProcedureConfiguration;
	}

	public void setListProcedureConfiguration(List<ProcedureConfiguration> listProcedureConfiguration) {
		this.listProcedureConfiguration = listProcedureConfiguration;
	}

	public List<TContractCapDetails> getListTContractCapDetails() {
		return listTContractCapDetails;
	}

	public void setListTContractCapDetails(List<TContractCapDetails> listTContractCapDetails) {
		this.listTContractCapDetails = listTContractCapDetails;
	}

	public List<TPackageCapDetails> getListTPackageCapDetails() {
		return listTPackageCapDetails;
	}

	public void setListTPackageCapDetails(List<TPackageCapDetails> listTPackageCapDetails) {
		this.listTPackageCapDetails = listTPackageCapDetails;
	}

	public char getIsRenderingSpeciality() {
		return isRenderingSpeciality;
	}

	public void setIsRenderingSpeciality(char isRenderingSpeciality) {
		this.isRenderingSpeciality = (isRenderingSpeciality == '\u0000') ? 'N' : isRenderingSpeciality;
	}

	public char getIsClinicalSpeciality() {
		return isClinicalSpeciality;
	}

	public void setIsClinicalSpeciality(char isClinicalSpeciality) {
		this.isClinicalSpeciality = (isClinicalSpeciality == '\u0000') ? 'N' : isClinicalSpeciality;
	}

	public int getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(int specialityId) {
		this.specialityId = specialityId;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getSpecialityCode() {
		return specialityCode;
	}

	public void setSpecialityCode(String specialityCode) {
		this.specialityCode = specialityCode;
	}

	public char getIsSurgicalCode() {
		return isSurgicalCode;
	}

	public void setIsSurgicalCode(char isSurgicalCode) {
		this.isSurgicalCode = (isSurgicalCode == '\u0000') ? 'A' : isSurgicalCode;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status == '\u0000') ? 'A' : status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getGeneralLedgerId() {
		return generalLedgerId;
	}

	public void setGeneralLedgerId(Integer generalLedgerId) {
		this.generalLedgerId = generalLedgerId;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public List<ServiceMaster> getSpecialityList() {
		return specialityList;
	}

	public void setSpecialityList(List<ServiceMaster> specialityList) {
		this.specialityList = specialityList;
	}

	public List<ModalityMaster> getListModalityMaster() {
		return listModalityMaster;
	}

	public void setListModalityMaster(List<ModalityMaster> listModalityMaster) {
		this.listModalityMaster = listModalityMaster;
	}

	public GeneralLedgerMaster getGeneralLedgerMaster() {
		return generalLedgerMaster;
	}

	public void setGeneralLedgerMaster(GeneralLedgerMaster generalLedgerMaster) {
		this.generalLedgerMaster = generalLedgerMaster;
	}

	public List<UnitSpecialityMapper> getUnitSpecialityMappersList() {
		return unitSpecialityMappersList;
	}

	public void setUnitSpecialityMappersList(List<UnitSpecialityMapper> unitSpecialityMappersList) {
		this.unitSpecialityMappersList = unitSpecialityMappersList;
	}

	public List<DoctorSpecialityMapper> getDoctorSpecialityMappersList() {
		return doctorSpecialityMappersList;
	}

	public void setDoctorSpecialityMappersList(List<DoctorSpecialityMapper> doctorSpecialityMappersList) {
		this.doctorSpecialityMappersList = doctorSpecialityMappersList;
	}

	public char getDeptWiseAppointment() {
		return deptWiseAppointment;
	}

	public void setDeptWiseAppointment(char deptWiseAppointment) {
		this.deptWiseAppointment = (deptWiseAppointment == '\u0000') ? 'N' : deptWiseAppointment;
	}

	public Character getIsKioskDept() {
		return isKioskDept;
	}

	public void setIsKioskDept(Character isKioskDept) {
		this.isKioskDept = (isKioskDept == '\u0000') ? 'N' : isKioskDept;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	
	
	
	

}