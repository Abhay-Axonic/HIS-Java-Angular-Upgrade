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

import com.param.adt.master.global.model.OrganizationMaster;
import com.param.adt.master.global.model.UnitMaster;
@NamedQueries({
	/*@NamedQuery(name="GET_NURSING_STATION_WARD_MAPPER_LIST",
			
			query="SELECT Distinct "
					+ " nurSingMstr.nursingStationId AS nursingStationId ,"
					+ "nrsingStMster.nursingWardId AS nursingWardId,"
					+ "nrsingStMster.wardId AS wardId,"
					+ "wrdMster.wardName AS wardName ,"
					+ "nrsingStMster.status AS status ,"
					
					+ "nurSingMstr.nursingStationDesc AS nursingStationDesc "
					+ "FROM NursingStationWardMapper nrsingStMster "
					+ "INNER JOIN nrsingStMster.wardMaster wrdMster "
					+ "INNER JOIN nrsingStMster.nursingStationMaster nurSingMstr "
					+ "WHERE nrsingStMster.organizationId=:orgId "
					+ "AND nrsingStMster.unitId=:unitId "
//					+ "AND nrsingStMster.status='A'"
					+ ""),*/
	
	@NamedQuery(name="CHECK_IS_NURSING_STATION_ALREADY_ASSIGNED",
	query="SELECT nursingWardId as nursingWardId FROM NursingStationWardMapper"
//			+ " WHERE wardId=:wardId "
			+ " WHERE isDeleted='N' "
			+ " AND nursingStationId=:nursingStationId "
			+ " AND status='A' "
			+ " AND organizationId=:orgId "
			+ " AND unitId=:unitId "
			 ),

	@NamedQuery(name="CHECK_IS_NURSINGSTATION_ALREADY_ASSIGNED_OR_NOT",
	query="SELECT mppr.nursingWardId as nursingWardId FROM NursingStationWardMapper mppr "
			+ "WHERE mppr.wardId=:wardId "
			+ " AND mppr.nursingStationId=:nursingStationId "
			+ " AND mppr.status='A' "
			+ " AND mppr.organizationId=:orgId "
			+ " AND mppr.unitId=:unitId "
			+ " AND mppr.isDeleted='N'"//HIS-23343 Nursing Station Ward Mapping: Pagination not working properly
			),
	@NamedQuery(name="GET_NURSING_STATION_LIST_BY_WARD_ID",
	query=" SELECT nrsingStMster.nursingWardId as nursingWardId,"
			+ " nrsingStMster.nursingStationId AS nursingStationId ,"
			+ " nrsingStMster.wardId AS wardId,"
			+ " wrdMster.wardName as wardName,"
			+ "nurSingMstr.nursingStationDesc AS nursingStationDesc "			
			+ "FROM NursingStationWardMapper nrsingStMster "
			+ "INNER JOIN nrsingStMster.wardMaster wrdMster "
			+ "INNER JOIN nrsingStMster.nursingStationMaster nurSingMstr "
			+ "WHERE nrsingStMster.organizationId=:orgId "
			+ "AND nrsingStMster.unitId=:unitId "
			+ "AND wrdMster.wardId=:wardId "
			+ "AND nrsingStMster.status='A' "
			+ "AND nrsingStMster.isDeleted='N'"),
	
	@NamedQuery(name="GET_WARD_LIST_BY_NURSING_STATION_ID",
	query=" SELECT nrsingStMster.nursingWardId as nursingWardId,"
			+ " nrsingStMster.nursingStationId AS nursingStationId ,"
			+ " nrsingStMster.wardId AS wardId,"
			+ " wrdMster.wardName as wardName "			
			+ "FROM NursingStationWardMapper nrsingStMster "
			+ "INNER JOIN nrsingStMster.wardMaster wrdMster "
			+ "WHERE nrsingStMster.organizationId=:orgId "
			+ "AND nrsingStMster.unitId=:unitId "
			+ "AND nrsingStMster.nursingStationId=:nursingStationId "
			+ "AND nrsingStMster.status='A' "
			+ "AND nrsingStMster.isDeleted='N'"),
	@NamedQuery(name="GET_NURSING_STATION_WARD_MAPPER_BY_ID",
	query=" SELECT "
			+ " nurSingMstr.nursingStationId AS nursingStationId ,"
			+ "nrsingStMster.nursingWardId AS nursingWardId,"
			+ "nrsingStMster.wardId AS wardId,"
			+ "wrdMster.wardName AS wardName ,"
			+ "nrsingStMster.status AS status ,"			
			+ "nurSingMstr.nursingStationDesc AS nursingStationDesc "
			+ "FROM NursingStationWardMapper nrsingStMster "
			+ "INNER JOIN nrsingStMster.wardMaster wrdMster "
			+ "INNER JOIN nrsingStMster.nursingStationMaster nurSingMstr "
			+ "WHERE nrsingStMster.organizationId=:orgId "
			+ "AND nrsingStMster.unitId=:unitId "
			+ "AND nrsingStMster.nursingStationId=:nursingStationId "
			+ "AND nrsingStMster.isDeleted ='N'")
	
})

@NamedNativeQueries({
	@NamedNativeQuery(name="GET_WARD_LIST_BY_USER_ID",query="select "
					+"ward_id as \"wardId\" "
					+"from	adt.t_nursing_station_ward_mapper nswm "
					+"inner join adt.t_user_nursing_station_mapper unsm on nswm.nursing_station_id = unsm.nursing_station_id "
					+"where unsm.user_id=:userId and nswm.status = 'A'"
					+ " AND nswm.organization_id=:orgId AND  nswm.unit_id=:unitId "),
	
	@NamedNativeQuery(name="GET_WARD_LIST_BY_NURSING_STATION",query="select "
			+"distinct(ward_id) as \"id\" "
			+"from	adt.t_nursing_station_ward_mapper nswm "
			+"inner join adt.t_user_nursing_station_mapper unsm on "
			+ 	"nswm.nursing_station_id = unsm.nursing_station_id "
			+"where unsm.nursing_station_id=:nursingStationId "
			+ "and nswm.status = 'A' and nswm.is_deleted='N' "),
	
	@NamedNativeQuery(name="GET_NURSING_STATION_WARD_MAPPER_LIST",query="select "
			+ " 	distinct nursingsta0_.nursing_station_id as \"nursingStationId\", "
			+ " 	nursingsta0_.status as \"status\", "
			+ " 	nursingsta2_.nursing_station_desc as \"nursingStationDesc\" "
			//, nursingsta0_.nursing_ward_id as \"nursingWardId\"
			//+ " nursingsta0_.ward_id as \"wardId\" "
			+ " from "
			+ " 	adt.t_nursing_station_ward_mapper nursingsta0_ "
			+ " inner join adt.m_nursing_station_master nursingsta2_ on "
			+ " 	nursingsta0_.nursing_station_id = nursingsta2_.nursing_station_id "
			+ " where "
			+ " 	nursingsta0_.organization_id =:orgId "
			+ " 	and nursingsta0_.unit_id =:unitId "
			+ " and nursingsta0_.is_deleted='N' "
			+ " ORDER BY nursingsta2_.nursing_station_desc ASC ")
	
})
@Entity
@Table(name = "t_nursing_station_ward_mapper", schema = "adt")
@SequenceGenerator(name = "nursing_station_ward_mapper_seq", sequenceName = "adt.nursing_station_ward_mapper_seq", allocationSize = 1)
public class NursingStationWardMapper 
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nursing_station_ward_mapper_seq")
	@Column(name="nursing_ward_id")
	private int nursingWardId;
	
	@Column(name="nursing_station_id")
	private Integer nursingStationId;
	
	@Column(name="ward_id")
	private Integer wardId;
	
	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private int createdBy;
	
	@Column(name="updated_by")
	private int updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="status")
	private char status;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;

	@Column(name="is_deleted")
	private Character isDeleted;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", insertable = false, nullable = false, updatable = false)
	private OrganizationMaster organizationMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unit_id", insertable = false, nullable = false, updatable = false)
	private UnitMaster unitMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nursing_station_id", insertable = false, nullable = false, updatable = false)
	private NursingStationMaster nursingStationMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ward_id", insertable = false, nullable = false, updatable = false)
	private WardMaster wardMaster;

	public int getNursingWardId() {
		return nursingWardId;
	}

	public void setNursingWardId(int nursingWardId) {
		this.nursingWardId = nursingWardId;
	}

	public Integer getNursingStationId() {
		return nursingStationId;
	}

	public void setNursingStationId(Integer nursingStationId) {
		this.nursingStationId = nursingStationId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public OrganizationMaster getOrganizationMaster() {
		return organizationMaster;
	}

	public void setOrganizationMaster(OrganizationMaster organizationMaster) {
		this.organizationMaster = organizationMaster;
	}

	public UnitMaster getUnitMaster() {
		return unitMaster;
	}

	public void setUnitMaster(UnitMaster unitMaster) {
		this.unitMaster = unitMaster;
	}

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}
	
}
