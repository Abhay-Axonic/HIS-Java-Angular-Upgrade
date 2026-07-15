package com.param.global.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.admission.model.AdmissionDetails;
import com.param.adt.admission.model.AdmissionNote;

@NamedNativeQueries({

		@NamedNativeQuery(name = "GET_ROLE_BY_USER_ID", query = "select "
				+ " 	user_role_mapper.role_id as \"roleId\", "
				+ " 	role_mst.is_counter_applicable as \"isCounterApplicable\", "
				+ " 	dashboad.dashboard_link as \"dashboadSate\" " + " from "
				+ " 	t_user_role_mapper user_role_mapper " + " inner join public.m_role_master role_mst on "
				+ " 	user_role_mapper.role_id = role_mst.role_id "
				+ " left join public.m_dashboard_master dashboad on "
				+ " 	dashboad.dashboard_id = role_mst.dashboard_id " + " where "
				+ " 	user_role_mapper.organisation_id =:orgId " + " 	and user_role_mapper.unit_id =:unitId "
				+ " 	and user_role_mapper.user_id =:userId " + " 	and user_role_mapper.status = 'A' "),

		@NamedNativeQuery(name = "GET_ACTIVE_OTIMS_ROLE_MASTER_LIST", query = "SELECT "
				+ " role_id as \"roleId\" , role_name as \"roleName\", org_id as \"organisationId\", unit_id as \"unitId\" , status as \"status\" "
				+ " FROM public.m_otims_role_master otimsRollMaster " + " WHERE otimsRollMaster.status= 'A'"
				+ " AND otimsRollMaster.org_id= :organisationId" + " AND otimsRollMaster.unit_id= :unitId"
				+ " ORDER BY otimsRollMaster.role_id asc"),
		@NamedNativeQuery(name = "GET_ACTIVE_OT_ROLE_LIST", query = "SELECT "
				+ " otimsRollMaster.role_id as \"roleId\" , otimsRollMaster.role_name as \"roleName\" "
				+ " FROM public.m_role_master otimsRollMaster "
				+ " where otimsRollMaster.organisation_id= :organisationId " + " AND otimsRollMaster.unit_id= :unitId "
				+ " AND otimsRollMaster.role_type_id in(8,9,10,11,12)" + " ORDER BY otimsRollMaster.role_name asc") })

@NamedQueries({

		@NamedQuery(name = "GET_USER_ROLE_MASTER_LIST", query = "SELECT " + " userRoleMaster.roleId AS roleId, "
				+ " userRoleMaster.roleName AS roleName, " + " userRoleMaster.roleTypeId AS roleTypeId, "
				+ " userRoleMaster.dashboardId as dashboardId,"
				// + "userRoleMaster.doctorTypeId AS doctorTypeId, "
				+ " userRoleMaster.isCounterApplicable AS isCounterApplicable, "
				+ " userRoleMaster.prophylacticVaccinationApplication AS prophylacticVaccinationApplication, "
				+ " userRoleMaster.status AS status, " + " userRoleMaster.organisationId AS organisationId, "
				+ " userRoleMaster.unitId AS unitId, " + " dashboardMaster.dashboardDesc AS dashboardDesc "
				// + " case when userRoleMaster.roleTypeId=1 then 'doctor' end
				// as \"roleType\" "
				+ " FROM UserRoleMaster userRoleMaster " + " LEFT JOIN userRoleMaster.dashboardMaster dashboardMaster"
				+ " WHERE userRoleMaster.organisationId= :organisationId" + " AND userRoleMaster.unitId= :unitId"
				+ " ORDER BY userRoleMaster.roleId asc"),

		@NamedQuery(name = "GET_USER_ROLE_MASTER_BY_ID", query = "SELECT " + " userRoleMaster.roleId AS roleId, "
				+ " userRoleMaster.roleTypeId AS roleTypeId ," + " userRoleMaster.status AS status, "
				+ " userRoleMaster.isCounterApplicable AS isCounterApplicable, "
				+ " userRoleMaster.prophylacticVaccinationApplication AS prophylacticVaccinationApplication, "
				+ " userRoleMaster.roleName AS roleName," + " userRoleMaster.organisationId AS organisationId,"
				+ " userRoleMaster.unitId AS unitId, " + " userRoleMaster.dashboardId as dashboardId,"
				// + " userRoleMaster.doctorTypeId AS doctorTypeId, "
				+ " dashboardMaster.dashboardDesc AS dashboardDesc " + " FROM UserRoleMaster userRoleMaster "
				+ " LEFT JOIN userRoleMaster.dashboardMaster dashboardMaster"
				+ " WHERE userRoleMaster.organisationId= :organisationId" + " AND userRoleMaster.unitId= :unitId"
				+ " AND userRoleMaster.roleId= :roleId" + " ORDER BY userRoleMaster.roleId asc"),

		@NamedQuery(name = "GET_ACTIVE_USER_ROLE_MASTER_LIST", query = "SELECT "
				+ " userRoleMaster.roleId AS roleId, userRoleMaster.status AS status,"
				+ " userRoleMaster.roleName AS roleName ," + " userRoleMaster.unitId AS unitId ,"
				+ " userRoleMaster.roleTypeId AS roleTypeId, " + " userRoleMaster.organisationId AS organisationId "
				// + " userRoleMaster.doctorTypeId AS doctorTypeId "
				+ " FROM UserRoleMaster userRoleMaster " + " WHERE userRoleMaster.status= 'A'"
				+ " AND userRoleMaster.organisationId= :organisationId" + " AND userRoleMaster.unitId= :unitId"
				+ " ORDER BY userRoleMaster.roleName asc "),

		@NamedQuery(name = "GET_USER_ROLE_MASTER_BY_NAME", query = "SELECT " + " userRoleMaster.roleId AS roleId, "
				+ " userRoleMaster.roleName AS roleName," + " userRoleMaster.roleTypeId AS roleTypeId "
				// + " userRoleMaster.doctorTypeId AS doctorTypeId "
				+ " FROM UserRoleMaster userRoleMaster " + " WHERE LOWER(userRoleMaster.roleName) = :roleName"
				+ " AND userRoleMaster.organisationId= :orgId" + " AND userRoleMaster.unitId= :unitId"),

		@NamedQuery(name = "GET_USER_ROLE_MASTER_BY_NAME_NOT_ID", query = "SELECT "
				+ " userRoleMaster.roleId AS roleId, " + " userRoleMaster.roleName AS roleName ,"
				+ " userRoleMaster.roleTypeId AS roleTypeId "
				// + "userRoleMaster.doctorTypeId AS doctorTypeId "
				+ " FROM UserRoleMaster userRoleMaster " + " WHERE userRoleMaster.roleId != :roleId"
				+ " AND LOWER(userRoleMaster.roleName) = :roleName" + " AND userRoleMaster.organisationId= :orgId"
				+ " AND userRoleMaster.unitId= :unitId" + " ORDER BY userRoleMaster.roleId asc"),
		@NamedQuery(name = "SEARCH_USER_ROLE_BY_NAME", query = "SELECT " + " userRoleMaster.roleId AS roleId, "
				+ " userRoleMaster.roleName AS roleName," + " userRoleMaster.roleTypeId AS roleTypeId "
				// + "userRoleMaster.doctorTypeId AS doctorTypeId "
				+ " FROM UserRoleMaster userRoleMaster " + " WHERE  LOWER(userRoleMaster.roleName)like :roleName"
				+ " AND userRoleMaster.organisationId= :orgId" + " AND userRoleMaster.unitId= :unitId"),
		@NamedQuery(name = "GET_DASHBOARD_MASTER_LIST_BY_ROLE_ID", query = "SELECT "
				+ " userRoleMaster.roleId AS roleId, "
				+ " userRoleMaster.dashboardId AS dashboardId, dashboardMaster.dashboardDesc AS dashboardDesc "
				+ " FROM UserRoleMaster userRoleMaster " + " INNER JOIN userRoleMaster.dashboardMaster dashboardMaster"
				+ " WHERE userRoleMaster.status= 'A' " + " AND userRoleMaster.organisationId= :orgId"
				+ " AND userRoleMaster.unitId= :unitId") })

@Entity
@Table(name = "m_role_master", schema = "public")
@SequenceGenerator(name = "m_role_master_seq", sequenceName = "public.m_role_master_seq", allocationSize = 1)
public class UserRoleMaster {

	@Id
	@Column(name = "role_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_role_master_seq")
	private int roleId;

	@Column(name = "role_name")
	private String roleName;

	@Column(name = "status")
	private Character status;

	@Column(name = "organisation_id")
	private Integer organisationId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "role_type_id")
	private Integer roleTypeId;

	@Column(name = "doctor_type_id")
	private Integer doctorTypeId;

	@Column(name = "created_by")
	private int createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private int updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "is_counter_applicable")
	private char isCounterApplicable;

	@Column(name = "dashboard_id")
	private Integer dashboardId;

	@Column(name = "prophylactic_vaccination_application")
	private Character prophylacticVaccinationApplication;

	public Character getProphylacticVaccinationApplication() {
		return prophylacticVaccinationApplication;
	}

	public void setProphylacticVaccinationApplication(Character prophylacticVaccinationApplication) {
		this.prophylacticVaccinationApplication = (prophylacticVaccinationApplication == '\u0000') ? 'N'
				: prophylacticVaccinationApplication;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userRoleMaster", cascade = CascadeType.ALL)
	private List<VaccinationRoleMapper> vaccinationRoleMapper;

	@ManyToOne
	@JoinColumn(name = "dashboard_id", insertable = false, updatable = false)
	private DashboardMaster dashboardMaster;
	/*
	 * @OneToMany(fetch = FetchType.LAZY,mappedBy = "userMaster", cascade =
	 * CascadeType.ALL, orphanRemoval = true) private List<UserMaster>
	 * listUserMaster;
	 */

	// @OneToMany(fetch = FetchType.LAZY,mappedBy = "roleMaster", cascade =
	// CascadeType.ALL, orphanRemoval = true)
	// private List<UserRoleMapper> listUserRoleMppr;

	public List<VaccinationRoleMapper> getVaccinationRoleMapper() {
		return vaccinationRoleMapper;
	}

	public void setVaccinationRoleMapper(List<VaccinationRoleMapper> vaccinationRoleMapper) {
		this.vaccinationRoleMapper = vaccinationRoleMapper;
	}

	public DashboardMaster getDashboardMaster() {
		return dashboardMaster;
	}

	public void setDashboardMaster(DashboardMaster dashboardMaster) {
		this.dashboardMaster = dashboardMaster;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	public char getIsCounterApplicable() {
		return isCounterApplicable;
	}

	public void setIsCounterApplicable(char isCounterApplicable) {
		this.isCounterApplicable = (isCounterApplicable == '\u0000') ? 'N' : isCounterApplicable;
	}

	public Integer getRoleTypeId() {
		return roleTypeId;
	}

	public void setRoleTypeId(Integer roleTypeId) {
		this.roleTypeId = roleTypeId;
	}

	public Integer getDoctorTypeId() {
		return doctorTypeId;
	}

	public void setDoctorTypeId(Integer doctorTypeId) {
		this.doctorTypeId = doctorTypeId;
	}

	public Integer getDashboardId() {
		return dashboardId;
	}

	public void setDashboardId(Integer dashboardId) {
		this.dashboardId = dashboardId;
	}

}
