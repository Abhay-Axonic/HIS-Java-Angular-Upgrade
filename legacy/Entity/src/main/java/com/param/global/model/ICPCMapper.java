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
@NamedNativeQueries({
	@NamedNativeQuery(name="GET_ICPC_MAPPER_BY_ID",
			  query=" select ism.icpc_system_mapper_id as \"icpcSystemMapperId\",ism.icpc_system_id as \"icpcSystemId\", im.icpc_system_desc as \"icpcSystemDesc\" from emr.t_icpc_system_mapper ism" + 
			  		" inner join emr.m_icpc_system im on im.icpc_system_id=ism.icpc_system_id" + 
			  		" where ism.icpc_system_mapper_id=:icpcSystemMapperId" + 
			  		" and ism.organization_id=:orgId"),

		@NamedNativeQuery(name="GET_ICPC_MAPPER_LIST",
		query=" select ismppr.icpc_system_mapper_id as \"icpcSystemMapperId\", ismppr.icpc_system_id as \"icpcSystemId\", "
				+ "ism.icpc_system_desc as \"icpcSystemDesc\", " 
				+ " ismppr.status as \"status\" " + 
			" from emr.t_icpc_system_mapper ismppr" + 
			" inner join emr.m_icpc_system ism on ism.icpc_system_id=ismppr.icpc_system_id "
		+" where ismppr.organization_id=:orgId"),
		
		@NamedNativeQuery(name="GET_ACTIVE_ICPC_MAPPER_LIST",
		query=" select ismppr.icpc_system_mapper_id as \"icpcSystemMapperId\", ismppr.icpc_system_id as \"icpcSystemId\", "
				+ " ism.icpc_system_desc as \"icpcSystemDesc\" " + 
			" from emr.t_icpc_system_mapper ismppr" + 
			" inner join emr.m_icpc_system ism on ism.icpc_system_id=ismppr.icpc_system_id "
		+" where ismppr.organization_id=:orgId"
		+ " AND ismppr.status='A'"),
		
		@NamedNativeQuery(name="GET_GENDER_LIST_BY_ICPC_SYSTEM_ID",
		query=" select igm.icpc_system_mapper_id as\"icpcSystemMapperId\",igm.gender_id as\"genderId\",gm.gender_name as\"genderDesc\"" + 
				" from  emr.t_icpc_system_gender_mapper igm" + 
				" inner join public.m_gender_master gm  on  igm.gender_id=gm.gender_id " + 
				" WHERE igm.organization_id =:orgId" + 
				" AND igm.status ='A' " + 
				" AND igm.icpc_system_mapper_id =:icpcSystemMapperId"),
		
		@NamedNativeQuery(name="CHECK_ICPC_MAPPER_AVAILABLE",
		query=" select ismppr.icpc_system_mapper_id as \"icpcSystemMapperId\", ismppr.icpc_system_id as \"icpcSystemId\" " + 
				" from emr.t_icpc_system_mapper ismppr" + 
				" WHERE ismppr.organization_id =:orgId" + 
				" AND ismppr.icpc_system_id =:icpcSystemId"),
		
	@NamedNativeQuery(name="GET_SYSTEM_LIST_FOR_COMPLAINTS_ON_COVERSHEET",
	query="  select distinct(ism.icpc_system_id) as \"icpcSystemId\", isys.icpc_system_desc as \"icpcSystemDesc\" "
			+ " from emr.t_icpc_system_mapper ism " + 
			" inner join emr.m_icpc_system isys on isys.icpc_system_id=ism.icpc_system_id" + 
			" inner join adt.t_icpc_code_configuration icodeMst on icodeMst.icpc_system_id=ism.icpc_system_id" + 
			" inner join adt.t_icpc_code_configuration_mapper icodeMppr on icodeMppr.icpc_configuration_id=icodeMst.icpc_system_id" + 
			" inner join emr.t_icpc_system_gender_mapper ismppr on ismppr.icpc_system_mapper_id=ism.icpc_system_mapper_id" + 
			" where ismppr.gender_id=:genderId "
			+ " and ism.organization_id =:orgId "
		//	+ " ORDER BY ism.icpc_system_desc ASC"
			)
		})

@NamedQueries({
	@NamedQuery(name="UPDATE_ICPC_MAPPER_STATUS",
			query = " UPDATE ICPCMapper iCPCMapper "
					+ " SET iCPCMapper.status = :status"
					+ " WHERE iCPCMapper.organizationId= :orgId "
					+ " AND iCPCMapper.icpcSystemMapperId = :icpcSystemMapperId"),
	
	
})


@Entity 
@Table(name = "t_icpc_system_mapper", schema = "emr")
@SequenceGenerator(name = "t_icpc_system_mapper_seq", sequenceName = "emr.t_icpc_system_mapper_seq", allocationSize = 1)
public class ICPCMapper {
	@Id
	@Column(name = "icpc_system_mapper_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_icpc_system_mapper_seq")
	private Integer icpcSystemMapperId;

	@Column(name = "icpc_system_id")
	private Integer icpcSystemId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;

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
	@JoinColumn(name = "gender_id", insertable = false, updatable = false)
	private GenderMaster genderMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gender_id", insertable = false, updatable = false)
	private ICPCGenderMapper genderMapper;

	public Integer getIcpcSystemMapperId() {
		return icpcSystemMapperId;
	}

	public void setIcpcSystemMapperId(Integer icpcSystemMapperId) {
		this.icpcSystemMapperId = icpcSystemMapperId;
	}

	public Integer getIcpcSystemId() {
		return icpcSystemId;
	}

	public void setIcpcSystemId(Integer icpcSystemId) {
		this.icpcSystemId = icpcSystemId;
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
