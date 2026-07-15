package com.param.adt.master.global.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
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

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.param.adt.master.unit.model.UserNursingStationMapper;
import com.param.adt.master.unit.model.UserStoreMapperNew;
import com.param.billing.global.model.DiscountRightsMaster;
import com.param.global.model.DoctorMaster;
import com.param.global.model.EmployeeMaster;
import com.param.global.model.UserRoleMapper;
import com.param.global.model.UserUnitMapper;

@NamedQueries({
	
		@NamedQuery(name = "USER_LOGIN", query = "SELECT 	distinct(userMst.userId) as userId, "
				+ "userMst.password as password,"
				+ "unitUser.unitId as unitId,"
				+ "userMst.organizationId as organizationId,"
				+ "to_char(userMst.createdDate,'DD-MM-YYYY') as createdDate,"
				+ "userMst.createdBy as createdBy,"
				+ "userMst.userName as userName, "
				+ "userMst.status as status, "
				+ "userMst.pwdExpiry as pwdExpiry, "
				+ "doc.doctorId as doctorId,doc.isRMO as isRMO, "
				+ " unitUser.isRenderedServiceCancelled as isRenderedServiceCancelled, "
				+ " unitUser.isUpdatePatientReg as isUpdatePatientReg, "
				+ " unitUser.opdBillCancellation as opdBillCancellation, "
				+ " unitUser.ipdBillCancellation as ipdBillCancellation, "
				+ " unitUser.isBillManipulationAllowed as isBillManipulationAllowed, "
				+ " unitUser.billSettlement as billSettlement, "
				+ " unitUser.nursingOpScan as nursingOpScan, "
				+ " unitUser.nursingIpScan as nursingIpScan, "
				+ " unitUser.doctorOpScan as doctorOpScan, "
				+ " unitUser.doctorIpScan as doctorIpScan, "
				+ " unitUser.changePayeeRights as changePayeeRights, "
				+ " unitUser.extendPdd as extendPdd, "
				+ " unitUser.ipBillSettlement as ipBillSettlement, "
				+ " unitUser.isUpdateIpdChargesRemark as isUpdateIpdChargesRemark, "
				+ " unitUser.serviceRateEditable as serviceRateEditable, "
				+ " unitUser.isForceUpdateServicePackage as isForceUpdateServicePackage, "
				+ "employee.employeeId as employeeId, "
				+ "concat(coalesce(prefix.prefixCode),'.',' ',coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as employeeName, "
				+ "concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName, "
				+ "employee.firstName as firstName, "
				+ "concat('Dr. ',coalesce(doc.firstName)) as docFirstName, "
				//+ "userNurMppr.nursingStationId as nursingStationId, "
				+ "unitMaster.unitCountryId as unitCountryId, "
				+ "unitMaster.unitStateId as unitStateId, "
				+ "unitMaster.unitDistrictId as unitDistrictId, "
				+ "unitMaster.unitCityId as unitCityId, "
				+ "unitMaster.unitAreaId as unitAreaId, "
				+ "unitMaster.postCode as unitZipCode, "
				+ "userMst.isSuperUser as isSuperUser,unitUser.updateDischargeSummary as updateDischargeSummary "
				+ "FROM	UserMaster userMst "
				+ "INNER JOIN userMst.unitUserMapper unitUser "
				+ "INNER JOIN unitUser.unitMaster unitMaster "
				+ "LEFT JOIN userMst.doctorMastersList doc "
				+ "LEFT JOIN userMst.employeeMastersList employee "

				+ "LEFT JOIN employee.prefixMaster prefix "
				//+ "LEFT JOIN userMst.userNursingStationMappersList userNurMppr " 
				+ "WHERE lower(userMst.userName)=lower(:userName) "
				//+" AND lower(userMst.password) =lower(:password)"
				+" AND userMst.organizationId =:orgId"
				+" AND userMst.status ='A'"
				+" AND unitMaster.unitId =:unitId"
				+" AND unitUser.status ='A' "
				),
		
		@NamedQuery(name = "USER_DATA_BY_ID", query = "SELECT 	distinct(userMst.userId) as userId, "
				+ "userMst.password as password,"
				+ "unitUser.unitId as unitId,"
				+ "userMst.organizationId as organizationId,"
				+ "to_char(userMst.createdDate,'DD-MM-YYYY') as createdDate,"
				+ "userMst.createdBy as createdBy,"
				+ "userMst.userName as userName, "
				+ "userMst.status as status, "
				+ "userMst.pwdExpiry as pwdExpiry, "
				+ "doc.doctorId as doctorId,doc.isRMO as isRMO, "
				+ " unitUser.isRenderedServiceCancelled as isRenderedServiceCancelled, "
				+ " unitUser.isUpdatePatientReg as isUpdatePatientReg, "
				+ " unitUser.opdBillCancellation as opdBillCancellation, "
				+ " unitUser.ipdBillCancellation as ipdBillCancellation, "
				+ " unitUser.billSettlement as billSettlement, "
				+ " unitUser.ipBillSettlement as ipBillSettlement, "
				+ " unitUser.isUpdateIpdChargesRemark as isUpdateIpdChargesRemark, "
				+ " unitUser.serviceRateEditable as serviceRateEditable, "
				+ " unitUser.isForceUpdateServicePackage as isForceUpdateServicePackage, "
				+ "employee.employeeId as employeeId, "
				+ "concat(coalesce(prefix.prefixCode),'.',' ',coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as employeeName, "
				+ "concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName, "
				+ "employee.firstName as firstName, "
				+ "concat('Dr. ',coalesce(doc.firstName)) as docFirstName, "
				//+ "userNurMppr.nursingStationId as nursingStationId, "
				+ "unitMaster.unitCountryId as unitCountryId, "
				+ "unitMaster.unitStateId as unitStateId, "
				+ "unitMaster.unitDistrictId as unitDistrictId, "
				+ "unitMaster.unitCityId as unitCityId, "
				+ "unitMaster.unitAreaId as unitAreaId, "
				+ "unitMaster.postCode as unitZipCode, "
				+ "userMst.isSuperUser as isSuperUser,unitUser.updateDischargeSummary as updateDischargeSummary "
				+ "FROM	UserMaster userMst "
				+ "INNER JOIN userMst.unitUserMapper unitUser "
				+ "INNER JOIN unitUser.unitMaster unitMaster "
				+ "LEFT JOIN userMst.doctorMastersList doc "
				+ "LEFT JOIN userMst.employeeMastersList employee "

				+ "LEFT JOIN employee.prefixMaster prefix "
				//+ "LEFT JOIN userMst.userNursingStationMappersList userNurMppr " 
				+ "WHERE "
				+" userMst.userId =:userId"
				+" and userMst.organizationId =:orgId"
				+" AND userMst.status ='A'"
				+" AND unitMaster.unitId =:unitId"
				+" AND unitUser.status ='A' "
				),
		
		@NamedQuery(name = "USER_DATA_BY_ID_NEW", query = "SELECT 	distinct(userMst.userId) as userId, "
				+ "userMst.password as password,"
				+ "unitUser.unitId as unitId,"
				+ "userMst.organizationId as organizationId,"
				+ "to_char(userMst.createdDate,'DD-MM-YYYY') as createdDate,"
				+ "userMst.createdBy as createdBy,"
				+ "userMst.userName as userName, "
				+ "userMst.status as status, "
				+ "userMst.pwdExpiry as pwdExpiry, "
				+ "doc.doctorId as doctorId,doc.isRMO as isRMO, "
				+ " unitUser.isRenderedServiceCancelled as isRenderedServiceCancelled, "
				+ " unitUser.isUpdatePatientReg as isUpdatePatientReg, "
				+ " unitUser.opdBillCancellation as opdBillCancellation, "
				+ " unitUser.ipdBillCancellation as ipdBillCancellation, "
				+ " unitUser.billSettlement as billSettlement, "
				+ " unitUser.ipBillSettlement as ipBillSettlement, "
				+ " unitUser.isUpdateIpdChargesRemark as isUpdateIpdChargesRemark, "
				+ " unitUser.serviceRateEditable as serviceRateEditable, "
				+ " unitUser.isForceUpdateServicePackage as isForceUpdateServicePackage, "
				+ "employee.employeeId as employeeId, "
				+ "concat(coalesce(prefix.prefixCode),'.',' ',coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as employeeName, "
				+ "concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName, "
				+ "employee.firstName as firstName, "
				+ "concat('Dr. ',coalesce(doc.firstName)) as docFirstName, "
				//+ "userNurMppr.nursingStationId as nursingStationId, "
				+ "unitMaster.unitCountryId as unitCountryId, "
				+ "unitMaster.unitStateId as unitStateId, "
				+ "unitMaster.unitDistrictId as unitDistrictId, "
				+ "unitMaster.unitCityId as unitCityId, "
				+ "unitMaster.unitAreaId as unitAreaId, "
				+ "unitMaster.postCode as unitZipCode, "
				+ "userMst.isSuperUser as isSuperUser,unitUser.updateDischargeSummary as updateDischargeSummary "
				+ "FROM	UserMaster userMst "
				+ "INNER JOIN userMst.unitUserMapper unitUser "
				+ "INNER JOIN unitUser.unitMaster unitMaster "
				+ "LEFT JOIN userMst.doctorMastersList doc "
				+ "LEFT JOIN userMst.employeeMastersList employee "

				+ "LEFT JOIN employee.prefixMaster prefix "
				+ "WHERE "
				+" userMst.userId =:userId"
				+" and userMst.organizationId =:orgId"
				+" AND unitMaster.unitId =:unitId "
				//+" AND unitUser.status ='A' "
				),
		
		
		@NamedQuery(name = "USER_LOGIN_FOR_DOCTOR_APP", query = "SELECT 	userMst.userId as userId, "
				+ "userMst.organizationId as organizationId,"
				+ "userMst.userName as userName, "
				+ "userMst.password as password,"
				+ "userMst.status as status, "
				+ "userMst.pwdExpiry as pwdExpiry, "
				+ "doc.doctorId as doctorId,doc.isRMO as isRMO, "
				+ "concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName "
				+ "FROM	UserMaster userMst "
				+ "LEFT JOIN userMst.doctorMastersList doc "
				+ "WHERE lower(userMst.userName)=lower(:userName) "
				+" AND lower(userMst.password) =lower(:password)"
				+" AND userMst.organizationId =:orgId"
				+" AND userMst.status ='A'"
				),
		
		
		@NamedQuery(name="GET_ACTIVE_USERS_BY_NAME",query="SELECT um.userName as userName, "
				+ "um.userId as userId "
				+ "FROM UserMaster um "
				+ "WHERE um.organizationId=:organizationId "
				//+ "AND um.unitId=:unitId "
				+ "AND lower(um.userName) LIKE :userName "),
		
		@NamedQuery(name="EMAIL_EXIST",query="SELECT um.userName as userName, "
				+ "um.userId as userId "
				+ "FROM UserMaster um "
				+ "WHERE um.organizationId=:organizationId ")	
		,
		@NamedQuery(name="VALIDATE_USER",query="SELECT um.userId as userId "
				+ "FROM UserMaster um "
				+ "INNER JOIN um.unitUserMapper uum "
				+ "WHERE um.organizationId=:organizationId "
				+ "AND uum.unitId=:unitId "
				+ "AND uum.status='A' "
				+ "AND uum.userId=:userId "
				+ "AND um.password=:password "),
		
		@NamedQuery(name="GET_USERNAME_BY_USERID",query="SELECT um.userName as userName "
				+ "FROM UserMaster um "
				+ "INNER JOIN um.unitUserMapper uum "
				+ "WHERE um.organizationId=:organizationId "
				+ "AND uum.unitId=:unitId "
				+ "AND uum.status='A' "
				+ "AND um.userId=:userId "),
		
		@NamedQuery(name="GET_STAFF_LIST_BY_ROLE_ID",
		query="SELECT distinct concat(coalesce(prefix.prefixCode),'.',' ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as userName, "
				+ "um.userId as userId, "
				+ "doc.email as emailId,"
				+ "doc.doctorId as doctorId "
				+ "FROM UserMaster um "
				+ "INNER JOIN um.doctorMastersList doc "
				+ "INNER JOIN doc.prefixMaster prefix "
				+ "INNER JOIN um.userMasterList userMasterList "
				+ "WHERE um.organizationId=:organizationId "
				+ "AND um.unitId=:unitId and um.status = 'A' "
				+ "AND userMasterList.roleId = :roleId "
				+ " ORDER BY doc.firstName ASC "),
})

@NamedNativeQueries({
	
	@NamedNativeQuery(name = "GET_UNIT_LIST_BY_USER_ORG", query = "select " 
			 + " 	unit.unit_id as \"unitId\", " 
			 + " 	unit.subscription_date as \"subscriptionDate\", " 
			 + " 	userMst.is_super_user as \"isSuperUser\", " 
			 + " 	subUnit.amount_paid as \"amountPaid\", " 
			 + " 	unit.unit_name as \"unitName\" " 
			 + " from " 
			 + " 	t_user_unit_mapper mppr " 
			 + " inner join public.m_unit_subscription_details subUnit on " 
			 + " subUnit.unit_id = mppr.unit_id and subUnit.status='A' "
			 +" inner join m_user_master userMst on mppr.user_id = userMst.user_id "
			 + " inner join m_unit_master unit on " 
			 + " 	unit.unit_id = mppr.unit_id " 
			 + " where " 
			 + " 	mppr.user_id = ( " 
			 + " 	select " 
			 + " 		user_id " 
			 + " 	from " 
			 + " 		m_user_master userMst " 
			 + " 	where " 
			 + " 		lower(userMst.user_name)=lower(:userName) " 
			 + " 		and lower(userMst.password) =lower(:password)" 
			 + " 		and userMst.organization_id =:orgId " 
			 + " 		and userMst.status = 'A' " 
			 + " 	limit 1) "
			 + " 		and mppr.status = 'A' "
			 + "		and unit.status='A' " 
			 + "order by unit.unit_id " ),
	
	
	@NamedNativeQuery(name="EMAIL_EXIST_COUNT",query=
			   " select "
			 + " emp_mst.user_id as \"userId\"  from  public.m_employee_master emp_mst " 
			 + "  where lower(emp_mst.email) =:email " 
			 //+ " and emp_mst.unit_id  =:unitId "  
			 //+ " and emp_mst.organization_id  =:orgId " 
			 + " and emp_mst.status = 'A' "
			 + "union  "
			 + "select"  
			 + " doct_mst.user_id as \"userId\" from  doctor.m_doctor_master doct_mst " 
			 + " where  lower(doct_mst.email) =:email " 
			// + " and doct_mst.unit_id =:unitId " 
			 //+ " and doct_mst.organization_id =:orgId "  
			 + " and doct_mst.status = 'A' "),
	
	@NamedNativeQuery(name="SEARCH_ACTIVE_USERS_BY_NAME_ON_TYPE",query= " select distinct " 
			 + " 	concat(coalesce(prefix.prefix_code), '.', ' ', coalesce(em.first_name), ' ', coalesce(em.middle_name), ' ', coalesce(em.last_name)) as \"userName\", " 
			 + " 	em.user_id as \"userId\" " 
			 + " from " 
			 + " 	public.m_employee_master em " 
			 + " inner join public.m_prefix_master prefix on " 
			 + " 	em.prefix_id = prefix.prefix_id " 
			 + " inner join public.t_user_unit_mapper um on " 
			 + " 	em.user_id = um.user_id " 
			 + " inner join public.t_user_role_mapper urm on " 
			 + " 	urm.user_id = em.user_id " 
			 + " where " 
			 + " 	urm.role_id in (select role_id from m_role_master mrm where mrm.role_type_id = 6 and mrm.unit_id =:unitId  and mrm.status = 'A') " 
			 + " 	and urm.status = 'A' " 
			 + " 	and um.status='A' " +
			 " and em.status = 'A' "
			 + " 	and um.unit_id =:unitId " 
			 + " 	and um.unit_id=urm.unit_id " 
			 + " 	and em.organization_id =:organizationId " ),
	
	@NamedNativeQuery(name="GET_ACTIVE_USERS_LIST",query=
    "SELECT " 
    + " 	CONCAT(COALESCE(prefix.prefix_code), ' ', COALESCE(employee.first_name), ' ', COALESCE(employee.middle_name), ' ', COALESCE(employee.last_name)) AS \"userName\", " 
    + " 	employee.user_id AS \"userId\" " 
    + " FROM " 
    + " 	m_employee_master employee " 
    + " INNER JOIN m_prefix_master prefix ON " 
    + " 	prefix.prefix_id = employee.prefix_id " 
    + " INNER JOIN t_user_unit_mapper um ON " 
    + " 	um.user_id = employee.user_id " 
    + " WHERE " 
    + " 	employee.organization_id = :organizationId " 
    + " 	AND um.unit_id = :unitId " 
    + " 	AND um.status = 'A' " 
    + " UNION " 
    + " SELECT " 
    + " 	CONCAT('Dr. ', COALESCE(employee.first_name), ' ', COALESCE(employee.middle_name), ' ', COALESCE(employee.last_name)) AS \"userName\", " 
    + " 	employee.user_id AS \"userId\" " 
    + " FROM " 
    + " 	doctor.m_doctor_master employee " 
    + " INNER JOIN t_user_unit_mapper um ON " 
    + " 	um.user_id = employee.user_id " 
    + " WHERE " 
    + " 	employee.organization_id = :organizationId " 
    + " 	AND um.unit_id = :unitId " 
    + " 	AND um.status = 'A' " 
    + " ORDER BY \"userName\" ASC "  // Order by the "userName" alias in ascending order
),
	
	@NamedNativeQuery(name="GET_ACTIVE_USERS_LIST_HOUSEKEEPING",query=
    "SELECT " 
    + " 	CONCAT(COALESCE(prefix.prefix_code), ' ', COALESCE(employee.first_name), ' ', COALESCE(employee.middle_name), ' ', COALESCE(employee.last_name)) AS \"userName\", " 
    + " 	employee.user_id AS \"userId\" " 
    + " FROM " 
    + " 	m_employee_master employee " 
    + " INNER JOIN m_prefix_master prefix ON " 
    + " 	prefix.prefix_id = employee.prefix_id " 
    + " INNER JOIN t_user_unit_mapper um ON " 
    + " 	um.user_id = employee.user_id "
    + " INNER JOIN  public.t_user_role_mapper userRoleMapper on "
    + " userRoleMapper.user_id = employee.user_id "
    + " INNER JOIN  public.m_role_master roleMaster on "
    + " userRoleMapper.role_id = roleMaster.role_id "
    + " WHERE " 
    + " 	employee.organization_id = :organizationId " 
    + " 	AND um.unit_id = :unitId " 
    + " 	AND um.status = 'A' "
    + "     AND roleMaster.role_type_id =18 " 
    + " UNION " 
    + " SELECT " 
    + " 	CONCAT('Dr. ', COALESCE(employee.first_name), ' ', COALESCE(employee.middle_name), ' ', COALESCE(employee.last_name)) AS \"userName\", " 
    + " 	employee.user_id AS \"userId\" " 
    + " FROM " 
    + " 	doctor.m_doctor_master employee " 
    + " INNER JOIN t_user_unit_mapper um ON " 
    + " 	um.user_id = employee.user_id " 
    + " INNER JOIN  public.t_user_role_mapper userRoleMapper on "
    + " userRoleMapper.user_id = employee.user_id "
    + " INNER JOIN  public.m_role_master roleMaster on "
    + " userRoleMapper.role_id = roleMaster.role_id "
    + " WHERE " 
    + " 	employee.organization_id = :organizationId " 
    + " 	AND um.unit_id = :unitId " 
    + " 	AND um.status = 'A' " 
    + "     AND roleMaster.role_type_id =18 " 
    + " ORDER BY \"userName\" ASC "  // Order by the "userName" alias in ascending order
),
	


	@NamedNativeQuery(name="GET_IS_PHARMACY_ROLE",query=
	   " select  roleMppr.user_id as \"userId\" from public.t_user_role_mapper roleMppr " + 
	   " inner join public.m_role_master roleMstr on roleMppr.role_id = roleMstr.role_id " + 
	   " where  roleMppr.user_id =:userId "
	 + " and roleMstr.role_type_id =7 " 
	 + " and roleMppr.organisation_id =:organizationId " 
	 + " and roleMppr.unit_id =:unitId "  ),
	
	@NamedNativeQuery(name="GET_ACTIVE_USERS_LIST_WITH_USERNAME",query=
	   " select " 
	 + " 	usr.user_name as \"userName\", " 
	 + " 	usr.user_id as \"userId\" " 
	 + " from public.m_user_master usr  " 
	 + " where usr.unit_id =:unitId " 
	 + " 	and usr.status = 'A' "
	 + "    and usr.organization_id =:organizationId "
	 + "    and (lower(coalesce(usr.user_name, '')) like :userName )" ),
})

@Embeddable
@Entity
@Table(name = "m_user_master", schema = "public")
@SequenceGenerator(name = "user_master_seq", sequenceName = "public.user_master_seq", allocationSize = 1)
public class UserMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_master_seq")
	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "password")
	private String password;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "password_expiry_date")
	private String pwdExpiry;
	
	@Column(name = "lab_pin")
	private String labPin;
	
	@Column(name = "subscription_date")
	private String subscriptionDate;

	@Column(name = "is_super_user")
	private Character isSuperUser;

	@Column(name = "otp_count")
	private Integer otpCount;

	@Column(name = "otp_time_stamp")
	private Timestamp otpTimeStamp;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserRoleMapper> userMasterList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DoctorMaster> doctorMastersList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DiscountRightsMaster> usersList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserNursingStationMapper> listUsers;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserStoreMapperNew> listUserStoreMapper;

	@OneToMany(fetch = FetchType.LAZY,mappedBy = "userMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EmployeeMaster> employeeMastersList;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "userMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserNursingStationMapper> userNursingStationMappersList;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "userMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserUnitMapper> unitUserMapper;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unit_id", insertable = false, nullable = false, updatable = false)
	private UnitMaster unitMaster;
	
	public Character getIsSuperUser() {
		return isSuperUser;
	}

	public void setIsSuperUser(Character isSuperUser) {
		this.isSuperUser = isSuperUser;
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

/*	@OneToMany(fetch = FetchType.LAZY, mappedBy = "UserRoleMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserRoleMapper> listUserRoleMppr;*/

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userMaster", orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<OrganizationUnitUserMapper> organizationUnitUserMappersList;

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

	public List<OrganizationUnitUserMapper> getOrganizationUnitUserMappersList() {
		return organizationUnitUserMappersList;
	}

	public void setOrganizationUnitUserMappersList(List<OrganizationUnitUserMapper> organizationUnitUserMappersList) {
		this.organizationUnitUserMappersList = organizationUnitUserMappersList;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<UserRoleMapper> getUserMasterList() {
		return userMasterList;
	}

	public void setUserMasterList(List<UserRoleMapper> userMasterList) {
		this.userMasterList = userMasterList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPwdExpiry() {
		return pwdExpiry;
	}

	public void setPwdExpiry(String pwdExpiry) {
		this.pwdExpiry = pwdExpiry;
	}

	public String getLabPin() {
		return labPin;
	}

	public void setLabPin(String labPin) {
		this.labPin = labPin;
	}

	public List<EmployeeMaster> getEmployeeMastersList() {
		return employeeMastersList;
	}

	public void setEmployeeMastersList(List<EmployeeMaster> employeeMastersList) {
		this.employeeMastersList = employeeMastersList;
	}

	public String getSubscriptionDate() {
		return subscriptionDate;
	}

	public void setSubscriptionDate(String subscriptionDate) {
		this.subscriptionDate = subscriptionDate;
	}
	

	/*public List<UserRoleMapper> getListUserRoleMppr() {
		return listUserRoleMppr;
	}

	public void setListUserRoleMppr(List<UserRoleMapper> listUserRoleMppr) {
		this.listUserRoleMppr = listUserRoleMppr;
	}*/

	public Integer getOtpCount() {
		return otpCount;
	}

	public void setOtpCount(Integer otpCount) {
		this.otpCount = otpCount;
	}

	public Timestamp getOtpTimeStamp() {
		return otpTimeStamp;
	}

	public void setOtpTimeStamp(Timestamp otpTimeStamp) {
		this.otpTimeStamp = otpTimeStamp;
	}
}
