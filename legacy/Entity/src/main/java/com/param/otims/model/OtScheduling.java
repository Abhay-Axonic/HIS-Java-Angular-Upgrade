package com.param.otims.model;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
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

import com.param.global.common.LocalTimeConverter;
import com.param.global.common.InstantConverter;

@NamedQueries({
	
	@NamedQuery(name="GET_OT_SCHEDULING",
	query="SELECT mstr.otSchedulingId as otSchedulingId, "
				+ " mstr.tableId as tableId, "
				+ " mstr.theatreId as theatreId, "
				+ " to_char(mstr.fromTime,'hh24:mi:ss') as fromTime, "
				+ " to_char(mstr.toTime,'hh24:mi:ss') as toTime, "
				+ " mstr.dayId as dayId, "
				+ " mstr.status as status, "
				+ " to_char(mstr.otDate,'DD/MM/YYYY') as otDate, "
				+ " mstr.switchOverTime as switchOverTime, "
				+ " opTable.tableName as tableName, "
				+ " opTheatre.theatreName as theatreName "
				+ " FROM OtScheduling mstr "
				+ " INNER JOIN mstr.operationTable opTable "
				+ " INNER JOIN mstr.operationTheatre opTheatre"
				+ " WHERE mstr.orgId= :organizationId "
				+ " AND mstr.unitId= :unitId order by mstr.otSchedulingId desc "),
	
	@NamedQuery(name="TO_CHECK_ALREADY_EXITS_OT_SCHEDULING",
	query="SELECT mstr.otSchedulingId as otSchedulingId "
				+ " FROM OtScheduling mstr "
				+ " WHERE mstr.orgId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.tableId= :opTable "
				+ " AND to_char(mstr.otDate,'yyyy-MM-dd')= :otDate "
				+ " AND mstr.theatreId= :opThreatre "
				+ " AND ((to_char(mstr.fromTime,'hh24:mi:ss') <= :fromTime  AND to_char(mstr.toTime,'hh24:mi:ss')  >= :fromTime ) " 
				+" or (to_char(mstr.fromTime,'hh24:mi:ss') <= :toTime AND to_char(mstr.toTime,'hh24:mi:ss') >= :toTime) " 
				+" or (to_char(mstr.fromTime,'hh24:mi:ss')  >= :fromTime   AND to_char(mstr.toTime,'hh24:mi:ss') <= :toTime))"),
	
	@NamedQuery(name="TO_CHECK_OT_AVAILABILITY",
	query="SELECT mstr.otSchedulingId as otSchedulingId ,"
				+ " mstr.theatreId as theatreId, "
				+ " to_char(mstr.fromTime,'hh24:mi:ss') as fromTime, "
				+ " to_char(mstr.toTime,'hh24:mi:ss') as toTime, "
				+ " mstr.dayId as dayId, "
				+ " mstr.status as status, "
				+ " to_char(mstr.otDate,'DD/MM/YYYY') as otDate, "
				+ " mstr.switchOverTime as switchOverTime "
				+ " FROM OtScheduling mstr "
				+ " WHERE mstr.orgId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.tableId= :opTable "
				+ " AND to_char(mstr.otDate,'yyyy-MM-dd')= :otDate "
				+ " AND mstr.theatreId= :opThreatre "
				+ " AND (to_char(mstr.fromTime,'hh24:mi:ss') <= :fromTime  AND to_char(mstr.toTime,'hh24:mi:ss') >= :toTime ) "),
	
	@NamedQuery(name="GET_OT_SCHEDULING_FOR_OT_CALENDER",
	query="SELECT mstr.otSchedulingId as otSchedulingId, "
				+ " mstr.tableId as tableId, "
				+ " mstr.theatreId as theatreId, "
				+ " to_char(mstr.fromTime,'hh24:mi:ss') as fromTime, "
				+ " to_char(mstr.toTime,'hh24:mi:ss') as toTime, "
				+ " mstr.dayId as dayId, "
				+ " mstr.status as status, "
				+ " to_char(mstr.otDate,'DD/MM/YYYY') as otDate, "
				+ " mstr.switchOverTime as switchOverTime, "
				+ " opTable.tableName as tableName, "
				+ " opTheatre.theatreName as theatreName "
				+ " FROM OtScheduling mstr "
				+ " INNER JOIN mstr.operationTable opTable "
				+ " INNER JOIN mstr.operationTheatre opTheatre"
				+ " WHERE mstr.orgId= :organizationId "
				+ " AND mstr.unitId= :unitId"
				+ " and(to_char(mstr.otDate,'YYYY-MM-DD') between :fromDate and :toDate)"
				+ " order by mstr.otSchedulingId desc "),
})



@Entity
@Table(name = "t_ot_scheduling", schema = "otims")
@SequenceGenerator(name = "ot_scheduling_seq", sequenceName = "otims.ot_scheduling_seq", allocationSize = 1)
public class OtScheduling {
	
	@Id
	@Column(name = "ot_scheduling_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ot_scheduling_seq")
	private Integer otSchedulingId;
	
	@Column(name="table_id")
	private Integer tableId;
	
	@Column(name="theatre_id")
	private Integer theatreId;
	
	@Column(name = "ot_date")
	private Date otDate;
	
	@Column(name="switch_over_time")
	private Integer switchOverTime;
	
	@Column(name="from_time")
	@Convert(converter = InstantConverter.class)
	private Instant fromTime;
	
	@Column(name="to_time")
	@Convert(converter = InstantConverter.class)
	private Instant toTime;

	@Column(name="day_id")
	private Integer dayId;
	
	@Column(name="day_name")
	private String dayName;
	
	@Column(name="status")
	private Character status;
	
	@Column(name = "organization_id")
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
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="table_id",insertable=false,updatable=false)
	OperationTable operationTable;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="theatre_id",insertable=false,updatable=false)
	OperationTheatre operationTheatre;

	public Integer getOtSchedulingId() {
		return otSchedulingId;
	}

	public void setOtSchedulingId(Integer otSchedulingId) {
		this.otSchedulingId = otSchedulingId;
	}

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public Integer getTheatreId() {
		return theatreId;
	}

	public void setTheatreId(Integer theatreId) {
		this.theatreId = theatreId;
	}

	public Date getOtDate() {
		return otDate;
	}

	public void setOtDate(Date otDate) {
		this.otDate = otDate;
	}

	public Integer getSwitchOverTime() {
		return switchOverTime;
	}

	public void setSwitchOverTime(Integer switchOverTime) {
		this.switchOverTime = switchOverTime;
	}

	public Instant getFromTime() {
		return fromTime;
	}

	public void setFromTime(Instant fromTime) {
		this.fromTime = fromTime;
	}

	public Instant getToTime() {
		return toTime;
	}

	public void setToTime(Instant toTime) {
		this.toTime = toTime;
	}

	public Integer getDayId() {
		return dayId;
	}

	public void setDayId(Integer dayId) {
		this.dayId = dayId;
	}

	public String getDayName() {
		return dayName;
	}

	public void setDayName(String dayName) {
		this.dayName = dayName;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	
	
	
}
