package com.param.adt.housekeeping.model;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.BedMaster;
import com.param.adt.master.global.model.OrganizationMaster;
import com.param.adt.master.global.model.UnitMaster;
import com.param.adt.master.global.model.UserMaster;
import com.param.adt.master.unit.model.FloorMaster;
import com.param.adt.master.unit.model.RoomMaster;
import com.param.adt.master.unit.model.WardMaster;
import com.param.global.model.BedCategoryMaster;
import com.param.global.model.PriorityMaster;
@NamedQueries({
	
	@NamedQuery(name="GET_BEDS_FOR_HOUSEKEEPING_LIST",query="SELECT bm.bedId as bedId, "
			+ "bm.floorId as floorId, "
			+ "floor.floorName as floorName, "
			//+ "nur.nursingStationId as nursingStationId, "
			//+ "nur.nursingStationDesc as nursingStationDesc, "
			+ "bm.bedCategoryId as bedCategoryId, "
			+ "bc.bedCategoryDesc as bedCategoryDesc, "
			+ "bm.bedNumber as bedNumber, "
			+ "hskp.priorityId as priorityId, "
			+ "priority.priorityDesc as priorityDesc, "
			+ "hskp.housekeepingId as housekeepingId, "
			+ "hskp.housekeepingStatusId as housekeepingStatusId, "
			+ "hskp.assignedPersonId as assignedPersonId, "
			//+ "um.userName as userName, "
			+ "concat(coalesce(prefix.prefixCode),'.',' ',coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as userName, "
			+ "hsm.housekeepingStatusDesc as housekeepingStatusDesc, "
			+ "hskplg.note as note, "
			+ "hskplg.rejectionReasonId as rejectionReasonId, "
			+ "reason.reasonDesc as reasonDesc "
			+ "FROM BedMaster bm "
			+ "INNER JOIN bm.listBedLogStatus bedLog "
			+ "LEFT JOIN bm.floorMaster floor "
			+ "LEFT JOIN bm.wardMaster ward "
			+ "LEFT JOIN bm.bedCategoryMaster bc "
			+ "LEFT JOIN bm.housekeepingsList hskp "
			//+ "LEFT JOIN ward.nursingStationWardMappersList nurward "
			//+ "LEFT JOIN nurward.nursingStationMaster nur "
			+ "LEFT JOIN hskp.housekeepingLogsList hskplg "
			+ "LEFT JOIN hskplg.reasonMaster reason "
			+ "INNER JOIN hskp.housekeepingStatusMaster hsm "
			+ "INNER JOIN hskp.priorityMaster priority "
			+ "LEFT JOIN hskp.userMaster um "
			+ "LEFT JOIN um.employeeMastersList employee "
			+ "LEFT JOIN employee.prefixMaster prefix "
			+ "WHERE bm.bedStatusId IN (2,6) "
			+ "AND bedLog.bedStatusId IN (2,6) "
			+ "AND hskp.housekeepingStatusId IN (1,2,3,5) "
			+ "AND hskplg.housekeepingStatusId IN (1,2,3,5) "
			+ "AND bm.status='A' "
			+ "AND bedLog.status='A' "
			+ "AND bm.organizationId=:organizationId "
			+ "AND bm.unitId=:unitId "
			+ "AND hskp.status='A' "
			+ "AND hskplg.status='A' ")
	
})



@Entity
@Table(name = "t_housekeeping", schema = "adt")
@SequenceGenerator(name = "housekeeping_seq", sequenceName = "adt.housekeeping_seq", allocationSize = 1)
public class Housekeeping {

	@Id
	@Column(name = "housekeeping_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "housekeeping_seq")
	private Integer housekeepingId;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "nursing_station_id")
	private Integer nursingStationId;

	@Column(name = "floor_id")
	private Integer floorId;

	@Column(name = "ward_id")
	private Integer wardId;

	@Column(name = "room_id")
	private Integer roomId;

	@Column(name = "bed_id")
	private Integer bedId;

	@Column(name = "bed_category_id")
	private Integer bedCategoryId;

	@Column(name = "priority_id")
	private Integer priorityId;

	@Column(name = "assigned_person_id")
	private Integer assignedPersonId;

	@Column(name="housekeeping_status_id")
	private Integer housekeepingStatusId;
	
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
	@JoinColumn(name = "organization_id", insertable = false, updatable = false)
	private OrganizationMaster organizationMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bed_id", insertable = false, updatable = false)
	private BedMaster bedMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unit_id", insertable = false, updatable = false)
	private UnitMaster unitMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "floor_id", insertable = false, updatable = false)
	private FloorMaster floorMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ward_id", insertable = false, updatable = false)
	private WardMaster wardMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id", insertable = false, updatable = false)
	private RoomMaster roomMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bed_category_id", insertable = false, updatable = false)
	private BedCategoryMaster bedCategoryMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "priority_id", insertable = false, updatable = false)
	private PriorityMaster priorityMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assigned_person_id", insertable = false, updatable = false)
	private UserMaster userMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "housekeeping_status_id", insertable = false, updatable = false)
	private HousekeepingStatusMaster housekeepingStatusMaster;

	@OneToMany(fetch = FetchType.LAZY,mappedBy = "housekeeping", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<HousekeepingLog> housekeepingLogsList;
	
	public Integer getHousekeepingStatusId() {
		return housekeepingStatusId;
	}

	public void setHousekeepingStatusId(Integer housekeepingStatusId) {
		this.housekeepingStatusId = housekeepingStatusId;
	}

	public Integer getHousekeepingId() {
		return housekeepingId;
	}

	public void setHousekeepingId(Integer housekeepingId) {
		this.housekeepingId = housekeepingId;
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

	public Integer getNursingStationId() {
		return nursingStationId;
	}

	public void setNursingStationId(Integer nursingStationId) {
		this.nursingStationId = nursingStationId;
	}

	public Integer getFloorId() {
		return floorId;
	}

	public void setFloorId(Integer floorId) {
		this.floorId = floorId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getBedId() {
		return bedId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}

	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Integer getAssignedPersonId() {
		return assignedPersonId;
	}

	public void setAssignedPersonId(Integer assignedPersonId) {
		this.assignedPersonId = assignedPersonId;
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
