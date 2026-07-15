package com.param.opd.model;

/*
 * 
 * Created by TUSHAR
 */
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.DoctorMaster;
import com.param.global.model.PatientRegistration;
import com.param.global.model.ServiceMaster;
import com.param.opd.coversheet.model.EncounterMaster;

@NamedQueries({
	@NamedQuery(name="GET_INVESTIGATIONS", query="SELECT inv.investigation_id as investigation_id,"
			+"gm.group_id as service_type_id,gm.group_name as service_type,ser.serviceMasterId as service_id,"
			+ "ser.serviceStandardName as service,ser.serviceShortDescription as service_description,"
			+"inv.patient_id as patient_id,inv.investigation_status as investigation_status,inv.created_by as created_by,"
			+"inv.created_date as created_date,inv.updated_date as updated_date,inv.updated_by as updated_by,"
			+"inv.encounter_id as encounter_id,inv.unit_id as unit_id,inv.organisation_id as organisation_id "
			+"FROM InvestigationModel as inv "
			+ "INNER JOIN inv.groupMaster gm "
			+ "INNER JOIN inv.serviceMaster ser "
			+"WHERE (inv.created_date BETWEEN :START_DATE AND :END_DATE) "
			+"AND inv.service_id = :SERVICE_ID AND inv.investigation_status = TRUE"),
	
	@NamedQuery(name="GET_SERVICES_FOR_INVESTIGATION_BY_CODE", query="SELECT serv.serviceMasterId as serviceMasterId,serv.serviceStandardCode as serviceStandardCode,serv.serviceStandardName as serviceStandardName,serv.serviceShortDescription as serviceShortDescription, "
			+ "(SELECT unitService.isPanel FROM UnitServiceMasterDetailsOPD as unitService WHERE unitService.serviceMasterId = serv.serviceMasterId) as isPanel "
			+" FROM ServiceMaster as serv WHERE LOWER(serv.serviceStandardCode) LIKE LOWER(:SERVICE_CODE) "
			+ "AND serv.organizationId = :ORGID"),
	
	@NamedQuery(name="GET_SERVICES_FOR_INVESTIGATION_BY_NAME", query="SELECT serv.serviceMasterId as serviceMasterId,serv.serviceStandardCode as serviceStandardCode,serv.serviceStandardName as serviceStandardName,serv.serviceShortDescription as serviceShortDescription, "
			+ "(SELECT unitService.isPanel FROM UnitServiceMasterDetailsOPD as unitService WHERE unitService.serviceMasterId = serv.serviceMasterId) as isPanel "
			+" FROM ServiceMaster as serv WHERE LOWER(serv.serviceStandardName) LIKE LOWER(:SERVICE_NAME) "
			+ "AND serv.organizationId = :ORGID"),
			

	@NamedQuery(name="GET_SERVICE_TYPES_FOR_INVESTIGATION", query="SELECT typ.group_id as group_id,typ.group_name as group_name "
			+"FROM GroupMaster as typ"),

	@NamedQuery(name="GET_SERVICES_FOR_HISTORY", query="SELECT serv.serviceMasterId as serviceMasterId,serv.serviceStandardCode as serviceStandardCode,serv.serviceStandardName as serviceStandardName,serv.serviceShortDescription as serviceShortDescription "
			+"FROM ServiceMaster as serv WHERE serv.organizationId = :ORGID "
			+ "AND serv.serviceMasterId IN (SELECT service_id FROM InvestigationModel where investigation_status =true)"),
	
	@NamedQuery(name="GET_CURRENT_INVESTIGATIONS", query="SELECT inv.investigation_id as investigation_id,"
			+"gm.group_id as service_type_id,gm.group_name as service_type,ser.serviceMasterId as service_id,"
			+ "ser.serviceStandardName as service,ser.serviceShortDescription as service_description,"
			+"inv.patient_id as patient_id,inv.investigation_status as investigation_status,inv.created_by as created_by,"
			+"inv.created_date as created_date,inv.updated_date as updated_date,inv.updated_by as updated_by,"
			+"inv.encounter_id as encounter_id,inv.unit_id as unit_id,inv.organisation_id as organisation_id "
			+"FROM InvestigationModel as inv "
			+ "INNER JOIN inv.groupMaster gm "
			+ "INNER JOIN inv.serviceMaster ser "
			+"WHERE inv.investigation_status = TRUE "
			+ "AND inv.encounter_id = :ENCOUNTER_ID"),
			
	@NamedQuery(name="GET_SERVICES_FROM_TEST", query="SELECT serv.serviceMasterId as serviceMasterId,serv.serviceStandardCode as serviceStandardCode,serv.serviceStandardName as serviceStandardName,serv.serviceShortDescription as serviceShortDescription "
			+ "FROM ServiceMaster as serv "
			+ "WHERE serv.serviceMasterId "
			+ "IN (SELECT testM.serviceId FROM TestMasterOPD as testM WHERE testM.testId "
			+ "IN (SELECT panelD.testId FROM TPanelDetailsMasterOPD as panelD WHERE panelD.isDeleted = 'N' AND panelD.panelId "
			+ "IN (SELECT panelM.panelId FROM TPanelMasterOPD as panelM WHERE panelM.isDeleted = 'N' AND  panelM.serviceId "
			+ "IN (SELECT unitService.serviceMasterId FROM UnitServiceMasterDetailsOPD as unitService "
			+ "WHERE unitService.isPanel = 'Y' AND unitService.serviceMasterId = :SERVICEID))))")
})

@Entity
@Table(name="t_investigation",schema="opd")
@SequenceGenerator(name = "t_investigation_seq", sequenceName = "opd.t_investigation_seq", allocationSize = 1)
public class InvestigationModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_investigation_seq")
	@Column(name = "investigation_id", unique = true, nullable = false)
	private int investigation_id;
	
	@Column(name = "service_type_id", nullable = false)
	private Integer service_type_id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_type_id", insertable = false, nullable = false, updatable = false)
	private GroupMaster groupMaster;
	
	@Column(name = "service_id", nullable = false)
	private Integer service_id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id", insertable = false, nullable = false, updatable = false)
	private ServiceMaster serviceMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, nullable = false, updatable = false)
	private PatientRegistration patientRegistration;
	
	@Column(name = "investigation_status", nullable = false)
	private boolean investigation_status;
	
	@Column(name = "created_date", nullable = false)
	private Date created_date;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", insertable = false, nullable = false, updatable = false)
	private DoctorMaster createdByDoctor;
	
	@Column(name = "updated_date", nullable = false)
	private Date updated_date;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updated_by", insertable = false, nullable = false, updatable = false)
	private DoctorMaster updatedByDoctor;
	
	@Column(name = "patient_id", nullable = false)
	private Integer patient_id;
	
	@Column(name = "created_by", nullable = false)
	private Integer created_by;
	
	@Column(name = "updated_by", nullable = false)
	private Integer updated_by;
	
	@Column(name = "encounter_id", nullable = false)
	private Integer encounter_id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "encounter_id", insertable = false, nullable = false, updatable = false)
	private EncounterMaster encounterMaster;
	
	@Column(name = "unit_id", nullable = false)
	private Integer unit_id;
	
	@Column(name = "organisation_id", nullable = false)
	private Integer organisation_id;
	
	public void setInvestigation_id(int investigation_id) {
		this.investigation_id = investigation_id;
	}
	
	
	public Integer getService_type_id() {
		return service_type_id;
	}

	public void setService_type_id(Integer service_type_id) {
		this.service_type_id = service_type_id;
	}

	public Integer getService_id() {
		return service_id;
	}
	public void setService_id(Integer service_id) {
		this.service_id = service_id;
	}

	public Integer getPatient_id() {
		return patient_id;
	}

	public void setPatient_id(Integer patient_id) {
		this.patient_id = patient_id;
	}


	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}


	public Integer getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(Integer updated_by) {
		this.updated_by = updated_by;
	}
	

	public PatientRegistration getPatientRegistration() {
		return patientRegistration;
	}
	
	public void setPatientRegistration(PatientRegistration patientRegistration) {
		this.patientRegistration = patientRegistration;
	}
	

	public boolean isInvestigation_status() {
		return investigation_status;
	}
	public void setInvestigation_status(boolean investigation_status) {
		this.investigation_status = investigation_status;
	}
	

	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	

	public DoctorMaster getCreatedByDoctor() {
		return createdByDoctor;
	}
	public void setCreatedByDoctor(DoctorMaster createdByDoctor) {
		this.createdByDoctor = createdByDoctor;
	}
	

	public Date getUpdated_date() {
		return updated_date;
	}
	public void setUpdated_date(Date updated_date) {
		this.updated_date = updated_date;
	}
	

	public DoctorMaster getUpdatedByDoctor() {
		return updatedByDoctor;
	}
	public void setUpdatedByDoctor(DoctorMaster updatedByDoctor) {
		this.updatedByDoctor = updatedByDoctor;
	}

	public Integer getEncounter_id() {
		return encounter_id;
	}

	public void setEncounter_id(Integer encounter_id) {
		this.encounter_id = encounter_id;
	}

	public Integer getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(Integer unit_id) {
		this.unit_id = unit_id;
	}

	public Integer getOrganisation_id() {
		return organisation_id;
	}

	public void setOrganisation_id(Integer organisation_id) {
		this.organisation_id = organisation_id;
	}
	public ServiceMaster getServiceMaster() {
		return serviceMaster;
	}
	
	
	public void setServiceMaster(ServiceMaster serviceMaster) {
		this.serviceMaster = serviceMaster;
	}


	public GroupMaster getGroupMaster() {
		return groupMaster;
	}


	public void setGroupMaster(GroupMaster groupMaster) {
		this.groupMaster = groupMaster;
	}


	public EncounterMaster getEncounterMaster() {
		return encounterMaster;
	}


	public void setEncounterMaster(EncounterMaster encounterMaster) {
		this.encounterMaster = encounterMaster;
	}
}