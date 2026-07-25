package com.param.adt.master.unit.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.ICUTypeMaster;
import com.param.adt.master.global.model.OrganizationMaster;
import com.param.adt.master.global.model.UnitMaster;
import com.param.adt.transfer.model.Transfer;
import com.param.adt.transfer.model.TransferRequest;
import com.param.billing.global.model.TPeriodicityServiceOrders;
import com.param.global.model.GenderMaster;

@NamedQueries({
	
	@NamedQuery(name="GET_WARD_LIST_BY_FLOOR_ID" , query="SELECT wardGen.wardId as wardId, "
			+ "ward.wardName as wardName,ward.isDayCare as isDayCare  " 
			+ "FROM WardGenderMapper wardGen "
			+ "INNER JOIN wardGen.wardMaster ward "
			+ "WHERE ward.organizationId=:organizationId "
			+ "AND ward.unitId=:unitId "
			+ "AND ward.floorId=:floorId "
			+ "AND ward.status='A' "
			+ "AND wardGen.status='A' "
			+ "AND wardGen.genderId=:genderId "),
	
	
	@NamedQuery(name="GET_WARD_BY_FLOOR_ID",query="SELECT ward.wardId as wardId, "
			+ "ward.wardName as wardName "
			+ "FROM WardMaster ward "
			+ "WHERE ward.organizationId=:organizationId "
			+ "AND ward.unitId=:unitId "
			+ "AND ward.floorId=:floorId "
			+ "AND ward.status='A' "
			+ "ORDER BY ward.wardName ASC "),
	@NamedQuery(name="GET_WARD_MASTER_BY_NAME",query="SELECT ward.wardId as wardId, "
			+ "ward.wardName as wardName "
			+ "FROM WardMaster ward "
			+ "WHERE LOWER(ward.wardName)=:wardName "
			+ "AND ward.organizationId=:orgId "
			+ "AND ward.unitId=:unitId "),
	@NamedQuery(name="GET_WARD_MASTER_BY_NAME_NOT_BY_ID",query="SELECT ward.wardId as wardId, "
			+ "ward.wardName as wardName "
			+ "FROM WardMaster ward "
			+ "WHERE LOWER(ward.wardName)=:wardName "
			+ "AND ward.wardId!=:wardId "
			+ "AND ward.unitId=:unitId "
			+ "AND ward.organizationId=:orgId "),
	
	@NamedQuery(name="GET_GENDER_LIST_BY_WARD_ID",query="SELECT word.genderId as id, "
			+ "gen.desc as lable "
			+ "FROM WardGenderMapper word "
			+ "INNER JOIN word.genderMaster gen "
			+ "WHERE word.wardId=:wardId "
			+ "AND word.status='A' "),
	
	@NamedQuery(name="GET_ACTIVE_WARD_MASTER_LIST",
	query="SELECT ward.wardId as wardId,ward.isDayCare as isDayCare,ward.islaborWard as islaborWard , ward.ishemodialysisward as ishemodialysisward, "
			+ "ward.wardName as wardName,ward.wardCode AS wardCode,ward.floorId AS floorId,ward.isIpWard as isIpWard,ward.isEr as isEr, "
			+ "ward.organizationId AS organizationId, ward.unitId AS unitId "
			+ "FROM WardMaster ward "					
			+ "WHERE "
			+ "ward.organizationId=:orgId "
			+ "AND ward.unitId=:unitId "
			+ " AND ward.status='A' order by ward.wardName ASC"),
	
	@NamedQuery(name = "GET_WARD_LIST_FOR_ER",

	query = " SELECT ward.wardId as wardId, "
			+ " ward.wardName as wardName,ward.wardCode AS wardCode  "			
			+ " FROM WardMaster ward "					
			+ " WHERE ward.organizationId=:orgId "
			+ " AND ward.unitId=:unitId "
			+ " AND ward.status='A' "
			+ " AND ward.isEr='Y' "
		
		),
	
	@NamedQuery(name="GET_ACTIVE_WARD_MASTER_LIST_FOR_ER",
	query=" SELECT distinct(ward.wardId) as wardId,ward.isDayCare as isDayCare, ward.isEr as isEr,ward.islaborWard as islaborWard ,ward.ishemodialysisward as ishemodialysisward ,  "
//			+ "roomMastersList.roomDesc as roomDesc, roomMastersList.roomId as roomId, "
//			+ "roomMastersList.erCategoryId as erCategoryId, "
			+ "ward.wardName as wardName,ward.wardCode AS wardCode,ward.floorId AS floorId  "			
			+ "FROM WardMaster ward "
//			+ "inner join ward.roomMastersList roomMastersList "
			+ "WHERE ward.organizationId=:orgId "
			+ "AND ward.unitId=:unitId "
			+ "AND ward.isEr='Y'"
			+ "AND ward.status='A' order by ward.wardId ASC"),
	
	@NamedQuery(name="GET_ACTIVE_ROOM_MASTER_LIST_FOR_ER_BY_WARD_ID",
	query="SELECT ward.wardId as wardId,ward.isDayCare as isDayCare, ward.isEr as isEr,ward.islaborWard as islaborWard , ward.ishemodialysisward as ishemodialysisward , roomMastersList.roomDesc as roomDesc, roomMastersList.roomId as roomId, "
			+ "roomMastersList.erCategoryId as erCategoryId, "
			+ "ward.wardName as wardName,ward.wardCode AS wardCode,ward.floorId AS floorId  "			
			+ "FROM WardMaster ward "
			+ "inner join ward.roomMastersList roomMastersList "
			+ "WHERE ward.organizationId=:orgId "
			+ "AND ward.unitId=:unitId "
			+ "AND ward.wardId=:wardId "
			+ "AND ward.isEr='Y'"
			+ "AND ward.status='A' order by ward.wardId ASC"),
})
@NamedNativeQueries({
@NamedNativeQuery(name="GET_WARD_LIST_NOT_MAPPED_TO_NURSING_STATION",
            query="SELECT wrdMstr.ward_id AS \"wardId\" ,"
            		+ "wrdMstr.ward_code AS \"wardCode\","
            		+ "wrdMstr.ward_name AS \"wardName\" "
            		+ " FROM adt.m_ward_master wrdMstr "
            		+ " WHERE wrdMstr.ward_id NOT IN(SELECT nursingWrdMapper.ward_id FROM adt.t_nursing_station_ward_mapper nursingWrdMapper "
            		+ " WHERE nursingWrdMapper.nursing_station_id=:nursingId )"
            		+ " AND wrdMstr.organization_id=:orgId"
            		+ " AND wrdMstr.unit_id=:unitId "
            		+ " AND wrdMstr.status='A'"
            ),

@NamedNativeQuery(name="GET_WARD_MASTER_LIST",
query="SELECT wrdMstr.ward_id AS \"wardId\" ,"
		+ "wrdMstr.ward_code AS \"wardCode\","
		+ "wrdMstr.ward_name AS \"wardName\","
		+ "wrdMstr.is_er AS \"isEr\",wrdMstr.is_ip_ward As \"isIpWard\", "
		+ "wrdMstr.is_icu AS \"isIcu\","
		+ "wrdMstr.icu_type_id AS \"icuTypeId\", "
		+ " wrdMstr.is_labor_ward AS \"islaborWard\", "
		+ " wrdMstr.is_hemodialysis_ward as \"ishemodialysisward\", "
		+ "wrdMstr.status AS \"status\", "
		+ "wrdMstr.is_day_care AS \"isDayCare\", "
		+ "genderMaster.gender_id AS \"genderId\", "
		+ "genderMaster.gender_name AS \"desc\", "
		+ "fm.floor_id AS \"floorId\", "
		+ "itm.icu_type AS \"icuTypeName\", "
		+ "fm.floor_name AS \"floorName\"  "
//		+ "sm.store_id AS \"storeId\", "
//		+ "sm.store_name AS \"storeName\" "
		+ " FROM adt.m_ward_master wrdMstr "
		+ " LEFT JOIN  public.m_gender_master genderMaster ON wrdMstr.gender_id=genderMaster.gender_id "
		+ " INNER JOIN  adt.m_floor_master fm ON wrdMstr.floor_id=fm.floor_id "
//		+ " LEFT JOIN  public.m_store sm ON wrdMstr.store_id=sm.store_id "
        + " LEFT JOIN  adt.m_icu_type_master itm ON wrdMstr.icu_type_id=itm.icu_type_id "
		+ " WHERE wrdMstr.organization_id=:orgId "
		+ " AND wrdMstr.unit_id=:unitId"),
@NamedNativeQuery(name="GET_WARD_MASTER_BY_ID",
query="SELECT wrdMstr.ward_id AS \"wardId\" ,"
		+ "wrdMstr.ward_code AS \"wardCode\","
		+ "wrdMstr.ward_name AS \"wardName\","
		+ "wrdMstr.is_er AS \"isEr\", "
		+ "wrdMstr.is_icu AS \"isIcu\", "
		+ "wrdMstr.icu_type_id AS \"icuTypeId\", "
		+ " wrdMstr.is_labor_ward AS \"islaborWard\", "
		+ " wrdMstr.is_hemodialysis_ward as \"ishemodialysisward\", "
		+ "wrdMstr.status AS \"status\", "
		+ "wrdMstr.is_day_care AS \"isDayCare\", "
		+ "genderMaster.gender_id AS \"genderId\", "
		+ "genderMaster.gender_name AS \"desc\", "
		+ "fm.floor_id AS \"floorId\", "
		+ "fm.floor_name AS \"floorName\", "
		+ "sm.store_id AS \"storeId\", "
		+ "sm.store_name AS \"storeName\","
		+ "wrdMstr.is_ip_ward AS \"isIpWard\" "
		+ " FROM adt.m_ward_master wrdMstr "
		+ " LEFT JOIN  public.m_gender_master genderMaster ON wrdMstr.gender_id=genderMaster.gender_id "
		+ " INNER JOIN  adt.m_floor_master fm ON wrdMstr.floor_id=fm.floor_id "
		+ " LEFT JOIN  public.m_store sm ON wrdMstr.store_id=sm.store_id "
		+ " WHERE wrdMstr.organization_id=:orgId "
		+ " AND wrdMstr.unit_id=:unitId "
		+ " AND wrdMstr.ward_id=:wardId")
})

@Entity
@Table(name="m_ward_master",schema="adt")
@SequenceGenerator(name="ward_master_seq",sequenceName="adt.ward_master_seq",allocationSize=1)
public class WardMaster 
{
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="ward_master_seq")
	@Column(name="ward_id")
	private int wardId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name="ward_code")
	private String wardCode;
	
	@Column(name="floor_id")
	private Integer floorId;
	
	@Column(name="ward_name")
	private String wardName;
	
	@Column(name="is_er")
	private char isEr;
	
	@Column(name="no_of_rooms")
	private Integer noOfRooms;
	
	@Column(name="gender_id")
	private Integer genderId;
	
	@Column(name="is_icu")
	private char isIcu;
	
	@Column(name="is_day_care")
	private char isDayCare;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "created_by")
	private int createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name="updated_by")
	private int updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="icu_type_id")
	private Integer icuTypeId;
	
	@Column(name="store_id")
	private Integer storeId;
	
	@Column(name="is_labor_ward")
	private Character islaborWard;
	
	@Column(name="is_hemodialysis_ward")
	private Character ishemodialysisward;
	
	@Column(name="is_ip_ward")
	private Character isIpWard;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "icu_type_id", insertable = false, updatable = false)
	private ICUTypeMaster icuTypeMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "organization_id", insertable = false, updatable = false)
	private OrganizationMaster organizationMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "unit_id", insertable = false, updatable = false)
	private UnitMaster unitMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "floor_id", insertable = false, updatable = false)
	private FloorMaster floorMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "gender_id", insertable = false, updatable = false)
	private GenderMaster genderMaster;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "wardMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<NursingStationWardMapper> nursingStationWardMappersList;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "wardMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Transfer> transferslist;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "wardMaster2", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Transfer> transferslist2;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "wardMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TransferRequest> transferRequestslist;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "wardMaster2", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TransferRequest> transferRequestslist2;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "WardMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RoomMaster> roomMastersList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "wardMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TPeriodicityServiceOrders> listTPeriodicityServiceOrders;

/*
	public WardMaster(Integer id) {
		super();
		this.wardId = id;
	}
*/
	public int getWardId() {
		return wardId;
	}

	public void setWardId(int wardId) {
		this.wardId = wardId;
	}
	
	

	public Integer getIcuTypeId() {
		return icuTypeId;
	}

	public void setIcuTypeId(Integer icuTypeId) {
		this.icuTypeId = icuTypeId;
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

	public String getWardCode() {
		return wardCode;
	}

	public void setWardCode(String wardCode) {
		this.wardCode = wardCode;
	}

	public Integer getFloorId() {
		return floorId;
	}

	public void setFloorId(Integer floorId) {
		this.floorId = floorId;
	}

	
	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public char getIsEr() {
		return isEr;
	}

	public void setIsEr(char isEr) {
		this.isEr = isEr;
	}

	public Integer getNoOfRooms() {
		return noOfRooms;
	}

	public void setNoOfRooms(Integer noOfRooms) {
		this.noOfRooms = noOfRooms;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public char getIsIcu() {
		return isIcu;
	}

	public void setIsIcu(char isIcu) {
		this.isIcu = isIcu;
	}

	public char getIsDayCare() {
		return isDayCare;
	}

	public void setIsDayCare(char isDayCare) {
		this.isDayCare = isDayCare;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	public FloorMaster getFloorMaster() {
		return floorMaster;
	}

	public void setFloorMaster(FloorMaster floorMaster) {
		this.floorMaster = floorMaster;
	}

	public GenderMaster getGenderMaster() {
		return genderMaster;
	}

	public void setGenderMaster(GenderMaster genderMaster) {
		this.genderMaster = genderMaster;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Character getIslaborWard() {
		return islaborWard;
	}

	public void setIslaborWard(Character islaborWard) {
		this.islaborWard = islaborWard;
	}

	public Character getIshemodialysisward() {
		return ishemodialysisward;
	}

	public void setIshemodialysisward(Character ishemodialysisward) {
		this.ishemodialysisward = ishemodialysisward;
	}

	public Character getIsIpWard() {
		return isIpWard;
	}

	public void setIsIpWard(Character isIpWard) {
		this.isIpWard = isIpWard;
	}

	
}
