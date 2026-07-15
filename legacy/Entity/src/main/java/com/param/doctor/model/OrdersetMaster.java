package com.param.doctor.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@NamedNativeQueries({
	
	@NamedNativeQuery(name="GET_MEDICATION_BY_ORDERSET",query=" select "
			+ "medication_dtls.orderset_medication_details_id as \"ordersetMedicationDetailsId\",flt.type as \"formulationType\", "
			+ "medication_dtls.orderset_master_id as \"ordersetMasterId\", "
			+ "medication_dtls.item_master_id as \"itemMasterId\", "
			+ "  coalesce((select CAST((b.rate) as decimal(10, 2))    " + 
			" from inventory.t_batch b    " + 
			" inner join inventory.t_store_stock S    " + 
			" on B.id = S.batch_id    " + 
			" where b.item_id = item.id   " + 
			" and s.store_id in (   " + 
			"  select store_id    " + 
			" from m_store_item_mapper msim    " + 
			" where item_id = item.id and unit_id = 1 and store_id in    " + 
			" ( select ms.store_id from m_store ms   " + 
			" where ms.unit_id = 1 and ms.is_pharmacy = true))    " + 
			" and b.unit_id = :unitId   " + 
			" and s.unit_id = :unitId   " + 
			" and to_char(b.expiry_date, 'yyyy-mm-dd') >=:expDate   " + 
			" and b.is_quarantine = false   " + 
			" order by b.id desc limit 1),0) as \"rate\","
			+ "item.generic_id as \"genericId\", "
			+ " generic.\"name\" as \"genericName\", "
			+ " item.formulation_type_id as \"formulationTypeId\" ,"
			+ "medication_dtls.route_id as \"routeId\", "
			+ "medication_dtls.dose_unit_id as \"doseUnitId\", "
			+ "medication_dtls.frequency_id as \"frequencyId\", "
			+ "medication_dtls.instruction_id as \"instructionId\", "
			+" instru.instruction_desc as \"instructionDesc\", "
			+ "frequency.description as \"frequencyDesc\", "
			+ "strength.unit as \"strengthDesc\", "
			+ "route.description as \"roaDesc\", "
			+ "item.\"name\" as \"itemName\", "
			+ "medication_dtls.dose as \"dose\" ,"
			+ "frequency.no_of_administration_time as \"noOfAdministrationTime\", "
			+ "diag.diagnosis_desc as \"diagnosisName\", "  
			+ "osm.orderset_name as \"ordersetName\", "  
			+ "splMst.speciality_name as \"specialityName\", "  
			+ "concat('Dr. ', coalesce(dm.first_name, ''), ' ', coalesce(dm.middle_name, ''), ' ', coalesce(dm.last_name, ''))as \"doctorName\"  "
			+ "from "
			+ " doctor.m_orderset_master osm  "
			+ "inner join doctor.t_orderset_medication_details medication_dtls on osm.orderset_master_id = medication_dtls.orderset_master_id "
			+ "inner join public.m_item item on medication_dtls.item_master_id = item.id "
			+ "inner join public.m_generic generic on item.generic_id = generic.id "
			+ "inner join public.m_route_of_administration route on medication_dtls.route_id = route.id "
			+ "inner join public.m_strength_unit as strength on medication_dtls.dose_unit_id  = strength.id "
			+ "inner join public.m_frequency frequency on medication_dtls.frequency_id = frequency.id "
			+ "left join public.m_instruction instru on medication_dtls.instruction_id = instru.instruction_id "
			+ "left join emr.m_diagnosis_master diag on diag.diagnosis_id = osm.diagnosis_id "  
			+ "left join doctor.m_speciality_master splMst on splMst.speciality_id = osm.speciality_id "  
			+ "left join doctor.m_doctor_master dm on osm.doctor_id = dm.doctor_id "
			+ " left join public.m_formulation_type flt on flt.id = item.formulation_type_id "
			+ " where medication_dtls.org_id = :orgId "
			+ " and medication_dtls.unit_id = :unitId "
			+ " and osm.status = 'A' "
			+ " and medication_dtls.orderset_master_id = :ordersetMasterId"),
	
	@NamedNativeQuery(name="GET_INVESTIGATION_BY_ORDERSET",query="select "
			+ "investi.orderset_investigation_details_id as \"ordersetInvestigationDetailsId\", "
			+ "investi.orderset_master_id as \"ordersetMasterId\", "
			+ "investi.speciality_id as \"specialityId\", "
			+ "specility_mst.speciality_name as \"specialityName\", "
			+ "sub_speci_master.sub_speciality_name as \"subSpecialityName\", "
			+ "investi.sub_speciality_id as \"subSpecialityId\", "
			+ "investi.service_id as \"serviceId\" , "
			+ "service_mst.service_standard_name as \"serviceStandardName\", "
			+ "usmd.is_ref_doctor_share as \"isRefDoctorShare\" ,"
			+" usmd.is_appointable  as \"isAppointable\" "
			+ "from "
			+ " doctor.m_orderset_master osm "
			+ " inner join doctor.t_orderset_investigation_details investi on osm.orderset_master_id = investi.orderset_master_id "
			+ " inner join service.m_service_master service_mst on investi.service_id = service_mst.service_master_id "
			+ " inner join doctor.m_speciality_master specility_mst on investi.speciality_id = specility_mst.speciality_id "
			+ " inner join doctor.m_sub_speciality_master sub_speci_master on service_mst.sub_speciality_id = sub_speci_master.sub_speciality_id "
			+ " inner join service.t_unit_service_master_details usmd on service_mst.service_master_id = usmd.service_master_id and usmd.unit_id = osm.unit_id and usmd.status = 'A' "
			+ " where investi.org_id = :orgId "
			+ " and investi.unit_id = :unitId "
			+ " and osm.status = 'A' "
			+ " and investi.orderset_master_id = :ordersetMasterId"),
	
	
	@NamedNativeQuery(name="GET_ORDER_SET",query="select orderset.orderset_master_id as \"ordersetMasterId\", "
			+ "orderset.diagnosis_id as \"diagnosisId\", "
			+ "orderset.orderset_name as \"ordersetName\", "
			+ "orderset.orderset_type_id as \"ordersetTypeId\", "
			+ "orderset.speciality_id as \"specialityId\", "
			+ "orderset.org_id as \"orgId\", "
			+ "orderset.unit_id as \"unitId\", "
			+ "orderset.doctor_id as \"doctorId\" "
			+ "from doctor.m_orderset_master orderset "
			+ "where orderset.org_id = :orgId "
			+ " and orderset.unit_id = :unitId "
			+ " and orderset.status = 'A' "
			+ " and orderset.is_deleted!='Y'"
			+ " and orderset.orderset_master_id in(:ordersetMasterId) "),


		@NamedNativeQuery(name="GET_ORDER_SET_BY_DIAGNOSIS",query="select orderset.orderset_master_id as \"ordersetMasterId\", "
			+ "orderset.diagnosis_id as \"diagnosisId\", "
			+ "orderset.orderset_name as \"ordersetName\", "
			+ "orderset.orderset_type_id as \"ordersetTypeId\", "
			+ "orderset.speciality_id as \"specialityId\", "
			+ "orderset.org_id as \"orgId\", "
			+ "orderset.unit_id as \"unitId\", "
			+ "orderset.doctor_id as \"doctorId\" "
			+ "from doctor.m_orderset_master orderset "
			+ "where orderset.org_id = :orgId "
			+ "and orderset.unit_id = :unitId "
			+ "and orderset.status = 'A' "
			+ "and orderset.diagnosis_id = :diagnosisId "
			+ "and orderset.orderset_name = :ordersetName "
			+ " and orderset.is_deleted!='Y' "),

		@NamedNativeQuery(name="GET_ORDER_SET_BY_ICPC_ID",
			query="select orderset.orderset_master_id as \"ordersetMasterId\", "
			+ "orderset.diagnosis_id as \"diagnosisId\", "
			+ "orderset.orderset_name as \"ordersetName\", "
			+ "orderset.orderset_type_id as \"ordersetTypeId\", "
			+ "orderset.speciality_id as \"specialityId\", "
			+ "orderset.org_id as \"orgId\", "
			+ "orderset.unit_id as \"unitId\", "
			+ "orderset.doctor_id as \"doctorId\" "
			+ "from doctor.m_orderset_master orderset "
			+ "where orderset.org_id = :orgId "
			+ "and orderset.unit_id = :unitId "
			+ "and orderset.status = 'A' "
			+ "and orderset.icpc_id = :icpcId "
			+ "and orderset.orderset_name = :ordersetName "
			+ " and orderset.is_deleted!='Y' "),


		@NamedNativeQuery(name="LIST_ORDER_MASTER",query="select "
			+ "	osm.orderset_name as \"ordersetName\", "
			+ "	osm.orderset_type_id as \"ordersetTypeId\", "
			+ "	osm.orderset_master_id as \"ordersetMasterId\", "
			+ "	digm.diagnosis_desc as \"diagnosisName\", "
			+ "	digm.diagnosis_id as \"diagnosisId\", "
			+ "	sm.speciality_name as \"specialityName\", "
			+ "	sm.speciality_id as \"specialityId\", "
			+ "	osm.status as \"status\", "
			+ "	dm.doctor_id as \"doctorId\", "
			+ "	concat('Dr. ', coalesce(dm.first_name, ''), ' ', coalesce(dm.middle_name, ''), ' ', coalesce(dm.last_name, ''))as \"doctorName\", "
			+ "	(case "
			+ "		when osm.orderset_type_id = 1 then 'Global' "
			+ "		when osm.orderset_type_id = 3 then 'Department Specific' "
			+ "		else 'Doctor Specific' "
			+ "	end "
			+ ") as \"ordersetTypeName\" "
			+ "from "
			+ "doctor.m_orderset_master osm "
			+ "inner join emr.m_diagnosis_master digm on "
			+ "osm.diagnosis_id = digm.diagnosis_id "
			+ "left join doctor.m_speciality_master sm on "
			+ "osm.speciality_id = sm.speciality_id "
			+ "left join doctor.m_doctor_master dm on "
			+ "osm.doctor_id = dm.doctor_id "
			+ "where "
			+ " osm.unit_id = :unitId"
			+ " and osm.is_deleted != 'Y'"
			),

		@NamedNativeQuery(name="LIST_ORDER_MASTER_FOR_ICPC",query="select "
			+ "	osm.orderset_name as \"ordersetName\", "
			+ "	osm.orderset_type_id as \"ordersetTypeId\", "
			+ "	osm.orderset_master_id as \"ordersetMasterId\", "
			+ "	icpc.icpc_desc as \"icpcDesc\", "
			+ "	icpc.icpc_id as \"icpcId\", "
			+ "	sm.speciality_name as \"specialityName\", "
			+ "	sm.speciality_id as \"specialityId\", "
			+ "	osm.status as \"status\", "
			+ "	dm.doctor_id as \"doctorId\", "
			+ "	concat('Dr. ', coalesce(dm.first_name, ''), ' ', coalesce(dm.middle_name, ''), ' ', coalesce(dm.last_name, ''))as \"doctorName\", "
			+ "	(case "
			+ "		when osm.orderset_type_id = 1 then 'Global' "
			+ "		when osm.orderset_type_id = 3 then 'Department Specific' "
			+ "		else 'Doctor Specific' "
			+ "	end "
			+ ") as \"ordersetTypeName\" "
			+ "from "
			+ "doctor.m_orderset_master osm "
			+ "inner join emr.m_icpc icpc on "
			+ "osm.icpc_id = icpc.icpc_id "
			+ "left join doctor.m_speciality_master sm on "
			+ "osm.speciality_id = sm.speciality_id "
			+ "left join doctor.m_doctor_master dm on "
			+ "osm.doctor_id = dm.doctor_id "
			+ "where "
			+ " osm.unit_id = :unitId"
			+ " and osm.is_deleted != 'Y'"
			),
	
	@NamedNativeQuery(name="LIST_ORDER_MASTER_BY_ID",query="select "
			+ "	osm.orderset_name as \"ordersetName\", "
			+ "	osm.orderset_type_id as \"ordersetTypeId\", "
			+ "	osm.orderset_master_id as \"ordersetMasterId\", "
			+ "	digm.diagnosis_desc as \"diagnosisName\", "
			+ "	digm.diagnosis_id as \"diagnosisId\", "
			+ "	sm.speciality_name as \"specialityName\", "
			+ "	sm.speciality_id as \"specialityId\", "
			+ "	osm.status as \"status\", "
			+ "	dm.doctor_id as \"doctorId\", "
			+ "	concat('Dr. ', coalesce(dm.first_name, ''), ' ', coalesce(dm.middle_name, ''), ' ', coalesce(dm.last_name, ''))as \"doctorName\", "
			+ "	(case "
			+ "		when osm.orderset_type_id = 1 then 'Global' "
			+ "		else 'Doctor Specific' "
			+ "	end "
			+ ") as \"ordersetTypeName\" "
			+ "from "
			+ "doctor.m_orderset_master osm "
			+ "inner join emr.m_diagnosis_master digm on "
			+ "osm.diagnosis_id = digm.diagnosis_id "
			+ "left join doctor.m_speciality_master sm on "
			+ "osm.speciality_id = sm.speciality_id "
			+ "left join doctor.m_doctor_master dm on "
			+ "osm.doctor_id = dm.doctor_id "
			+ "where "
			+ " osm.unit_id = :unitId"
			+ " and osm.orderset_master_id = :ordersetMasterId"),

	@NamedNativeQuery(name="LIST_ORDER_MASTER_BY_ID_FOR_ICPC",query="select "
			+ "	osm.orderset_name as \"ordersetName\", "
			+ "	osm.orderset_type_id as \"ordersetTypeId\", "
			+ "	osm.orderset_master_id as \"ordersetMasterId\", "
			+ "	icpc.icpc_desc as \"icpcDesc\", "
			+ "	icpc.icpc_id as \"icpcId\", "
			+ "	sm.speciality_name as \"specialityName\", "
			+ "	sm.speciality_id as \"specialityId\", "
			+ "	osm.status as \"status\", "
			+ "	dm.doctor_id as \"doctorId\", "
			+ "	concat('Dr. ', coalesce(dm.first_name, ''), ' ', coalesce(dm.middle_name, ''), ' ', coalesce(dm.last_name, ''))as \"doctorName\", "
			+ "	(case "
			+ "		when osm.orderset_type_id = 1 then 'Global' "
			+ "		else 'Doctor Specific' "
			+ "	end "
			+ ") as \"ordersetTypeName\" "
			+ "from "
			+ "doctor.m_orderset_master osm "
			+ "inner join emr.m_icpc icpc on "
			+ "osm.icpc_id = icpc.icpc_id "
			+ "left join doctor.m_speciality_master sm on "
			+ "osm.speciality_id = sm.speciality_id "
			+ "left join doctor.m_doctor_master dm on "
			+ "osm.doctor_id = dm.doctor_id "
			+ "where "
			+ " osm.unit_id = :unitId"
			+ " and osm.orderset_master_id = :ordersetMasterId"),

	@NamedNativeQuery(name="LIST_ORDER_MASTER_BY_DIAGNOSIS_ID",query="select "
		+ "	osm.orderset_name as \"ordersetName\", "
		+ "	osm.orderset_type_id as \"ordersetTypeId\", "
		+ "	osm.orderset_master_id as \"ordersetMasterId\", "
		+ "	digm.diagnosis_desc as \"diagnosisName\", "
		+ "	digm.diagnosis_id as \"diagnosisId\", "
		+ "	sm.speciality_name as \"specialityName\", "
		+ "	sm.speciality_id as \"specialityId\", "
		+ "	osm.status as \"status\", "
		+ "	dm.doctor_id as \"doctorId\", "
		+ "	concat('Dr. ', coalesce(dm.first_name, ''), ' ', coalesce(dm.middle_name, ''), ' ', coalesce(dm.last_name, ''))as \"doctorName\", "
		+ "	(case "
		+ "		when osm.orderset_type_id = 1 then 'Global' "
		+ "		else 'Doctor Specific' "
		+ "	end "
		+ ") as \"ordersetTypeName\" "
		+ "from "
		+ "doctor.m_orderset_master osm "
		+ "inner join emr.m_diagnosis_master digm on "
		+ "osm.diagnosis_id = digm.diagnosis_id "
		+ "left join doctor.m_speciality_master sm on "
		+ "osm.speciality_id = sm.speciality_id "
		+ "left join doctor.m_doctor_master dm on "
		+ "osm.doctor_id = dm.doctor_id "
		+ "where "
		+ " osm.unit_id = :unitId"
		+ " and osm.diagnosis_id = :diagnosisId "
		+ " and osm.is_deleted ='N' "),

		@NamedNativeQuery(
				name = "LIST_ORDER_MASTER_BY_ICPC_ID",
				query = "select "
						+ " osm.orderset_name as \"ordersetName\", "
						+ " osm.orderset_type_id as \"ordersetTypeId\", "
						+ " osm.orderset_master_id as \"ordersetMasterId\", "
						+ " icpc.icpc_desc as \"icpcDesc\", "
						+ " icpc.icpc_id as \"icpcId\", "
						+ " sm.speciality_name as \"specialityName\", "
						+ " sm.speciality_id as \"specialityId\", "
						+ " osm.status as \"status\", "
						+ " dm.doctor_id as \"doctorId\", "
						+ " concat('Dr. ', coalesce(dm.first_name, ''), ' ', "
						+ "        coalesce(dm.middle_name, ''), ' ', "
						+ "        coalesce(dm.last_name, '')) as \"doctorName\", "
						+ " case "
						+ "   when osm.orderset_type_id = 1 then 'Global' "
						+ "   when osm.orderset_type_id = 2 then 'Doctor Specific' "
						+ "   when osm.orderset_type_id = 3 then 'Speciality Specific' "
						+ " end as \"ordersetTypeName\" "
						+ "from doctor.m_orderset_master osm "
						+ "inner join emr.m_icpc icpc on osm.icpc_id = icpc.icpc_id "
						+ "left join doctor.m_speciality_master sm on osm.speciality_id = sm.speciality_id "
						+ "left join doctor.m_doctor_master dm on osm.doctor_id = dm.doctor_id "
						+ "where osm.unit_id = :unitId "
						+ "and osm.icpc_id = :icpcId "
						+ "and osm.is_deleted = 'N' "
						+ "and ( "
						+ "     osm.orderset_type_id = 1 "
						+ "  or (osm.orderset_type_id = 2 and osm.doctor_id = :doctorId) "
						+ "  or (osm.orderset_type_id = 3 and osm.speciality_id = :specialityId) "
						+ ")"
		),

		@NamedNativeQuery(
				name = "LIST_ORDER_MASTER_COUNT",
				query = "SELECT COUNT(*) " +
						"FROM doctor.m_orderset_master osm " +
						"WHERE osm.unit_id = :unitId " +
						"AND osm.diagnosis_id IS NOT NULL"
		),

		@NamedNativeQuery(
				name = "LIST_ORDER_MASTER_COUNT_FOR_ICPC",
				query = "SELECT COUNT(*) " +
						"FROM doctor.m_orderset_master osm " +
						"WHERE osm.unit_id = :unitId " +
						"AND osm.icpc_id IS NOT NULL"
		),
	
	@NamedNativeQuery(name="UPDATE_ORDER_SET_STATUS",query="update doctor.m_orderset_master set status = :status where orderset_master_id = :ordersetMasterId"),


	@NamedNativeQuery(name="GET_ORDER_SET_SERVICES",query="select serv.service_master_id as \"serviceMasterId\", sp.speciality_id as \"specialityId\",  sp.speciality_name as \"groupDesc\",   "
			+ " subsp.sub_speciality_id as \"subSpecialityMasterId\", subsp.sub_speciality_name as \"subGroup\", serv.service_standard_code as \"serviceCode\",   "
			+ " serv.service_standard_name as \"serviceDescription\" "
			+ " from service.m_service_master serv  " 
			+ " inner join doctor.m_speciality_master sp on sp.speciality_id = serv.speciality_id  " 
			+ " inner join doctor.m_sub_speciality_master subsp  on subsp.sub_speciality_id = serv.sub_speciality_id "
			+ " inner join service.t_unit_service_master_details usmd on usmd.service_master_id = serv.service_master_id  "
			+ " where serv.status = 'A' and usmd.status = 'A' "
			+ " and usmd.organization_id = :orgId "
			+ " and usmd.unit_id=:unitId ")
	
})
@NamedQuery(name = "UPDATE_ORDER_SETS_DETAILS",
query = " UPDATE OrdersetMaster ordersetMaster "
		+ " SET ordersetMaster.isDeleted = :isDeleted"
		+ " WHERE ordersetMaster.orgId= :orgId "
		+ " AND ordersetMaster.unitId = :unitId"
		+ " AND ordersetMaster.ordersetMasterId = :ordersetMasterId"
	)

@Entity
@Table(name = "m_orderset_master" , schema = "doctor")
@SequenceGenerator(name = "orderset_master_seq" , sequenceName = "doctor.orderset_master_seq" , allocationSize = 1)
public class OrdersetMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "orderset_master_seq")
	@Column(name = "orderset_master_id")
	private int ordersetMasterId;
	
	@Column(name = "diagnosis_id")
	private Integer diagnosisId;

	@Column(name = "icpc_id")
	private Integer icpcId;
	
	@Column(name = "orderset_name")
	private String ordersetName;
	
	@Column(name = "orderset_type_id")
	private Integer ordersetTypeId;
	
	@Column(name = "speciality_id")
	private Integer specialityId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
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
	
	@Column(name = "is_deleted")
	private Character isDeleted;

	public int getOrdersetMasterId() {
		return ordersetMasterId;
	}

	public void setOrdersetMasterId(int ordersetMasterId) {
		this.ordersetMasterId = ordersetMasterId;
	}

	public Integer getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Integer diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public String getOrdersetName() {
		return ordersetName;
	}

	public void setOrdersetName(String ordersetName) {
		this.ordersetName = ordersetName;
	}

	public Integer getOrdersetTypeId() {
		return ordersetTypeId;
	}

	public void setOrdersetTypeId(Integer ordersetTypeId) {
		this.ordersetTypeId = ordersetTypeId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getIcpcId() {
		return icpcId;
	}

	public void setIcpcId(Integer icpcId) {
		this.icpcId = icpcId;
	}
}
