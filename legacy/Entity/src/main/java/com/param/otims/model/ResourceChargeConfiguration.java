package com.param.otims.model;

import java.math.BigDecimal;
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

import com.param.global.model.ServiceMaster;
import com.param.global.model.SpecialityMaster;

@NamedQueries({
	
	@NamedQuery(name="IS_RESOURCE_CHARGE_CONFIGURATION_ALREADY_EXIST",query="SELECT rcc.resourceChargeConfigurationId as resourceChargeConfigurationId "
			+ "FROM ResourceChargeConfiguration rcc "
			+ "WHERE rcc.orgId=:orgId "
			+ "AND rcc.unitId=:unitId "
			+ "AND rcc.roleId=:roleId "
			+ "AND rcc.serviceId=:serviceId "),
	
	@NamedQuery(name="IS_RESOURCE_CHARGE_CONFIGURATION_ALREADY_EXIST_NOT_BY_ID",query="SELECT rcc.resourceChargeConfigurationId as resourceChargeConfigurationId "
			+ "FROM ResourceChargeConfiguration rcc "
			+ "WHERE rcc.orgId=:orgId "
			+ "AND rcc.unitId=:unitId "
			+ "AND rcc.roleId=:roleId "
			+ "AND rcc.serviceId=:serviceId "
			+ "AND rcc.amount=:amount "
			+ "AND rcc.resourceChargeConfigurationId!=:resourceChargeConfigurationId "),
	
	@NamedQuery(name="GET_RESOURCE_CHARGE_CONFIGURATION_BY_ID",query="SELECT rcc.resourceChargeConfigurationId as resourceChargeConfigurationId, "
			+ "rcc.specialityId as specialityId, "
			+ "rcc.serviceId as serviceId, "
			+ "rcc.amount as amount "
			+ "FROM ResourceChargeConfiguration rcc "
			+ "WHERE rcc.resourceChargeConfigurationId=:resourceChargeConfigurationId "),
	
	@NamedQuery(name="GET_RESOURCE_CHARGE_CONFIGURATION_LIST",query="SELECT rcc.resourceChargeConfigurationId as resourceChargeConfigurationId, "
			+ "rcc.specialityId as specialityId, "
			+ "speciality.specialityName as specialityName, "
			+ "rcc.serviceId as serviceId, "
			+ "service.serviceStandardName as serviceStandardName, "
			+ "rcc.amount as amount, "
			+ "rcc.status as status, "
			+ "rcc.roleId as roleId, "
			+ "role.procedureRoleName as procedureRoleName "
			+ "FROM ResourceChargeConfiguration rcc "
			+ "INNER JOIN rcc.specialityMaster speciality "
			+ "INNER JOIN rcc.serviceMaster service "
			+ "INNER JOIN rcc.procedureRoleMaster role "
			+ "WHERE rcc.orgId=:orgId "
			+ "AND rcc.unitId=:unitId "),
	
	@NamedQuery(name="GET_RESOURCE_CHAREGES",query="select amount as amount "
			+ "FROM ResourceChargeConfiguration "
			+ "WHERE serviceId=:serviceId "
			+ "AND specialityId=:specialityId "
			+ "AND roleId=:roleId "
			+ "AND orgId=:orgId "
			+ "AND unitId=:unitId ")
	
	
})
@NamedNativeQueries({
	
	@NamedNativeQuery(name="GET_RESOURCE_CHAREGES_BY_DOCTOR",query= " select  "
			 + "	coalesce(config.amount, 0) as \"amount\"  "
			 + "from  "
			 + "	doctor.t_doctor_speciality_mapper mppr inner  "
			 + "join doctor.m_doctor_master doct_mst on  "
			 + "	mppr.doctor_id = doct_mst.doctor_id  "
			 + "inner join otims.t_resource_charge_configuration config on  "
			 + "	config.speciality_id = mppr.speciality_id  "
			 + "where  "
			 + "	mppr.doctor_id = :doctorId  "
			 + "	and config.org_id = :orgId  "
			 + "	and config.unit_id =:unitId  "
			 + " 	and config.role_id =:roleId "
			 + " 	and config.service_id =:serviceId "
			 + "	and mppr.status='A' "
			 )
})
		
		
		

@Entity
@Table(name="t_resource_charge_configuration",schema="otims")
@SequenceGenerator(name="resource_charge_configuration_seq",sequenceName="otims.resource_charge_configuration_seq",allocationSize=1)
public class ResourceChargeConfiguration {

	@Id
	@Column(name="resource_charge_configuration_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="resource_charge_configuration_seq")
	private Integer resourceChargeConfigurationId;
	
	@Column(name="org_id")
	private Integer orgId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="speciality_id")
	private Integer specialityId;
	
	@Column(name="role_id")
	private Integer roleId;
	
	@Column(name="service_id")
	private Integer serviceId;
	
	@Column(name="amount")
	private BigDecimal amount;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "speciality_id", insertable = false, updatable = false)
	private SpecialityMaster specialityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id", insertable = false, updatable = false)
	private ServiceMaster serviceMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", insertable = false, updatable = false)
	private ProcedureRoleMaster procedureRoleMaster;
	
	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getResourceChargeConfigurationId() {
		return resourceChargeConfigurationId;
	}

	public void setResourceChargeConfigurationId(Integer resourceChargeConfigurationId) {
		this.resourceChargeConfigurationId = resourceChargeConfigurationId;
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

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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
	
}

/*
create sequence otims.resource_charge_configuration_seq;

CREATE TABLE otims.t_resource_charge_configuration(
	resource_charge_configuration_id integer default nextval('otims.resource_charge_configuration_seq'),
	org_id int4 NULL,
	unit_id int4 NULL,
	speciality_id integer NULL,
	role_id integer NULL,
	service_id integer NULL,
	amount numeric(15,2) NULL,
	status char(1) NULL DEFAULT 'I'::"char",
	created_by int4 NULL,
	created_date timestamp NULL,
	updated_by int4 NULL,
	updated_date timestamp NULL,
	CONSTRAINT pk_t_resource_charge_configuration PRIMARY KEY (resource_charge_configuration_id)
);

create sequence otims.additional_ot_time_seq;

CREATE TABLE otims.t_additional_ot_time(
	additional_ot_time_id integer default nextval('otims.additional_ot_time_seq'),
	org_id int4 NULL,
	unit_id int4 NULL,
	procedure_type_id integer NULL,
	time_min numeric(15,2) NULL,
	rate numeric(15,2) NULL,
	status char(1) NULL DEFAULT 'I'::"char",
	created_by int4 NULL,
	created_date timestamp NULL,
	updated_by int4 NULL,
	updated_date timestamp NULL,
	CONSTRAINT pk_additional_ot_time PRIMARY KEY (additional_ot_time_id)
);
*/
