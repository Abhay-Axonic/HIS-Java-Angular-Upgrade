package com.param.adt.master.unit.model;

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

import com.param.adt.master.global.model.UserMaster;
import com.param.entity.model.master.Store;
import com.param.global.model.EmployeeMaster;
@NamedQueries({
	
	@NamedQuery(name = "CHECK_USER_STORE_MAPPER_AVAILABLE",
	query = "SELECT userStoreMapper.userStoreMapperId as userStoreMapperId,"
		+ " userStoreMapper.userId as userId "
		+ " FROM UserStoreMapperNew userStoreMapper"
		+ " WHERE userStoreMapper.organizationId= :organizationId "
		+ " AND userStoreMapper.unitId= :unitId "
		+ " AND userStoreMapper.userId= :userId"),

	
/*	@NamedQuery(name = "GET_LIST_OF_USER",
	query = "SELECT employeeList.userId as userId,"
			+ "concat(coalesce(employeeList.firstName,'') ,' ', coalesce(employeeList.middleName,'') ,' ', coalesce(employeeList.lastName,'')) as employeeName, employeeList.empCode as empCode "
			+ " FROM EmployeeMaster employeeList "
			+ " INNER JOIN employeeList.userMaster userStoreMappersList "
			+ " WHERE employeeList.organizationId= :organizationId "
			+ " AND employeeList.unitId= :unitId "
		),*/

					
	@NamedQuery(name = "GET_ACTIVE_USER_STORE_MAPPER_LIST",
	query = "SELECT userStoreMapper.userStoreMapperId as userStoreMapperId,"
	    + " userStoreMapper.storeId as storeId,"
		+ " userStoreMapper.userId as userId,"
		+ " userStoreMapper.isDeleted as isDeleted,"
		+ " userStoreMapper.isActive as isActive,"
		+ " userStoreMapper.organizationId as organizationId,"
		+ " userStoreMapper.unitId as unitId "
		+ " FROM UserStoreMapperNew userStoreMapper"
		+ " WHERE  userStoreMapper.organizationId= :organizationId "
		+ " AND userStoreMapper.unitId= :unitId "
		+ " AND userStoreMapper.isDeleted!= true "
		),
	
	@NamedQuery(name = "GET_DETAILED_STORE_LIST",
	query = "SELECT userStoreMapper.userStoreMapperId as userStoreMapperId, userStoreMapper.isDeleted as isDeleted,"
	    + " userStoreMapper.storeId as storeId, storeMaster.storeName as storeName , "
		+ " userStoreMapper.userId as userId "
		+ " FROM UserStoreMapperNew userStoreMapper"
		+ " INNER JOIN userStoreMapper.storeMaster storeMaster"
		+ " WHERE userStoreMapper.userId= :userId "
		+ " AND userStoreMapper.organizationId= :organizationId "
		+ " AND userStoreMapper.unitId= :unitId "
		+ " AND userStoreMapper.isDeleted!= true "
		+ " AND storeMaster.isActive!= false "
		+ " ORDER BY storeMaster.storeName ASC "
		),
	
	
	@NamedQuery(name = "UPDATE_USER_STORE_MAPPER_STATUS",
	query = " UPDATE UserStoreMapperNew userStoreMapper "
			+ " SET  "
			+ " userStoreMapper.isActive= :isActive"
			+ " WHERE userStoreMapper.organizationId= :organizationId "
			+ " AND userStoreMapper.unitId= :unitId "
			+ " AND userStoreMapper.userId = :userId"
		),
	
	@NamedQuery(name = "UPDATE_USER_STORE_MAPPER",
	query = " UPDATE UserStoreMapperNew userStoreMapper "
			+ " SET userStoreMapper.isDeleted = :isDeleted"
			+ " WHERE userStoreMapper.organizationId= :organizationId "
			+ " AND userStoreMapper.unitId = :unitId"
			+ " AND userStoreMapper.userId = :userId"
		),
})

@NamedNativeQueries({
	@NamedNativeQuery(name="GET_USER_STORE_MAPPER_COUNT", 
			query=" select count(distinct(mppr.user_id)) as \"userId\" "
				+ " from public.m_user_store_mapper mppr "
				+ " inner join public.m_employee_master em " 
				+ " on mppr.user_id=em.user_id "
				+ " inner join public.m_user_master um " 
				+ " on mppr.user_id=um.user_id "
				+ " where mppr.organization_id= :organizationId"
				+ " and mppr.unit_id= :unitId"
			),
	
	@NamedNativeQuery(name="GET_USER_STORE_MAPPER_LIST", 
	query=	" select " 
			 + " 	distinct(emp.user_id) as \"userId\", " 
			 + " 	concat(empdata.first_name, ' ', empdata.middle_name, ' ', empdata.last_name) as \"employeeName\", " 
			 + " 	mppr.is_active as \"isActive\" " 
			 + " from " 
			 + " 	public.m_user_store_mapper mppr " 
			 + " inner join public.m_employee_master empdata on " 
			 + " 	mppr.user_id = empdata.user_id " 
			 + " inner join public.m_user_master emp on " 
			 + " 	mppr.user_id = emp.user_id " 
			 + " where " 
			 + " 	mppr.organization_id = :organizationId " 
			 + " 	and mppr.unit_id = :unitId "
			 + "	and empdata.status='A' " 
			 + " union " 
			 + " select " 
			 + " 	distinct(emp.user_id) as \"userId\", " 
			 + " 	concat(empdata.first_name, ' ', empdata.middle_name, ' ', empdata.last_name) as \"employeeName\", " 
			 + " 	mppr.is_active as \"isActive\" " 
			 + " from " 
			 + " 	public.m_user_store_mapper mppr " 
			 + " inner join doctor.m_doctor_master empdata on " 
			 + " 	mppr.user_id = empdata.user_id " 
			 + " inner join public.m_user_master emp on " 
			 + " 	mppr.user_id = emp.user_id " 
			 + " where " 
			 + " 	mppr.organization_id = :organizationId " 
			 + " 	and mppr.unit_id = :unitId " 
			 + "	and empdata.status='A' "
	),

		@NamedNativeQuery(name="GET_USER_STORE_MAPPER_LIST_USER_NAME_WISE",
				query=" select distinct(emp.user_id) as \"userId\", "
						+ " concat(empdata.first_name,' ',empdata.middle_name,' ',empdata.last_name) as \"employeeName\", mppr.is_active as \"isActive\"  "
						+ " from public.m_user_store_mapper mppr inner join public.m_employee_master empdata on mppr.user_id=empdata.user_id "
						+ " inner join public.m_user_master emp "
						+ " on mppr.user_id=emp.user_id "
						+ " where mppr.organization_id= :organizationId"
						+ " and mppr.unit_id= :unitId"
		),

		@NamedNativeQuery(name="GET_USER_STORE_MAPPER_BY_ID",
	query=" select mppr.id as \"userStoreMapperId\", mppr.store_id as \"storeId\", nsm.store_name as \"storeName\" , "
			+ " mppr.user_id as \"userId\", mppr.organization_id as \"organizationId\", mppr.unit_id as \"unitId\", emp.emp_code as \"empCode\" , mppr.is_active as \"isActive\", "
			+ " (coalesce(emp.first_name, '')||' '||coalesce(emp.middle_name, '')||' '||coalesce(emp.last_name, '')) as \"employeeName\" "
			+ " from public.m_user_store_mapper mppr "
			+ " inner join public.m_employee_master emp " 
			+ " on mppr.user_id=emp.user_id "
			+ " inner join public.m_store nsm " 
			+ " on mppr.store_id=nsm.store_id "
			+ " where mppr.organization_id= :organizationId"
			+ " and mppr.unit_id= :unitId"
			+ " and mppr.user_id= :userId"
			+ " and mppr.is_deleted!= true"
			+ " and nsm.is_active != false"
	),
	
	@NamedNativeQuery(name="GET_STORE_MASTER_LIST", 
	query=" select mppr.store_id as \"storeId\", "
			+ " mppr.store_name as \"storeName\" "
			+ " from public.m_store mppr "
			+ " where mppr.organization_id= :organizationId"
			+ " and mppr.unit_id= :unitId "
			+ " and mppr.is_active = true and mppr.is_deleted = false"
			+ " order by  mppr.store_name  ASC "
	)
})
@Entity
@Table(name="m_user_store_mapper",schema="public")
@SequenceGenerator(name="m_user_store_mapper_id_seq",sequenceName="public.m_user_store_mapper_id_seq",allocationSize=1)
public class UserStoreMapperNew {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="m_user_store_mapper_id_seq")
	private Integer userStoreMapperId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="user_id")
	private Integer userId;
	
	@Column(name="store_id")
	private Integer storeId;
	
	@Column(name="is_active")
	private Boolean isActive;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="added_by")
	private Integer addedBy;
	
	@Column(name="added_date")
	private Date addedDate;
	
	@Column(name="is_deleted")
	private Boolean isDeleted;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="store_id", insertable=false, updatable=false)
	private Store storeMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id", insertable=false, updatable=false)
	private UserMaster userMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id", insertable=false, updatable=false)
	private EmployeeMaster employeeMaster;
	
	public Integer getUserStoreMapperId() {
		return userStoreMapperId;
	}

	public void setUserStoreMapperId(Integer userStoreMapperId) {
		this.userStoreMapperId = userStoreMapperId;
	}

	
	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Store getStoreMaster() {
		return storeMaster;
	}

	public void setStoreMaster(Store storeMaster) {
		this.storeMaster = storeMaster;
	}

	public UserMaster getUserMaster() {
		return userMaster;
	}

	public void setUserMaster(UserMaster userMaster) {
		this.userMaster = userMaster;
	}

	public EmployeeMaster getEmployeeMaster() {
		return employeeMaster;
	}

	public void setEmployeeMaster(EmployeeMaster employeeMaster) {
		this.employeeMaster = employeeMaster;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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

	public Integer getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(Integer addedBy) {
		this.addedBy = addedBy;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
}
