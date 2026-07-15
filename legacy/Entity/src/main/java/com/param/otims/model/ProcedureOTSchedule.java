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
	
	@NamedQuery(name="GET_PROCEDURE_OT_SCHEDULE_BY_PRO_SCHEDULE_ID",
			query="SELECT mstr.procedureOtScheduleId as procedureOtScheduleId , " 
					+ " mstr.proReqId as proReqId , "
					+ " mstr.color as color , "
					+ " to_char(mstr.prodeureDate,'YYYY-MM-DD') as prodeureDate, "
					+" to_char(mstr.fromTime,'hh24:mi') as fromTime, "
					+" to_char(mstr.toTime,'hh24:mi') as toTime, "
					+ " mstr.tableId as tableId, "
					+ " mstr.theatreId as theatreId, "
					+ " operationTheatre.theatreDesc as theatreName, "
					+ " operationTable.tableName as tableName, "
					+ " mstr.organizationId as organizationId, "
					+ " mstr.unitId as unitId, "
					+ " mstr.createdBy as createdBy, "
					+ " mstr.updatedBy as updatedBy, "
					+ " to_char(mstr.createdDate, 'dd-MM-yyyy HH:ss:mm')as createdDate, "
					+ " to_char(mstr.updatedDate, 'dd-MM-yyyy HH:ss:mm')as updatedDate, "
					+ " mstr.status as status "
					+ " FROM ProcedureOTSchedule mstr "
					+ " INNER JOIN  mstr.operationTheatre operationTheatre  "
					+ " INNER JOIN  mstr.operationTable operationTable  "
					+ " WHERE  mstr.organizationId= :organizationId "
					+ " AND mstr.unitId= :unitId "
					+ " AND mstr.procedureScheduleId= :procedureScheduleId "
					+ " AND mstr.status = 'A' "),
	
	@NamedQuery(name="TO_CHECK_OT_USED",
	query="SELECT mstr.procedureOtScheduleId as procedureOtScheduleId "
				+ " FROM ProcedureOTSchedule mstr "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.tableId= :opTable "
				+ " AND to_char(mstr.prodeureDate,'yyyy-MM-dd')= :otDate "
				+ " AND mstr.theatreId= :opThreatre "
				+ " AND mstr.status ='A' "
				+ " AND ((to_char(mstr.fromTime,'hh24:mi:ss') <= :fromTime  AND to_char(mstr.toTime,'hh24:mi:ss')  >= :fromTime ) " 
				+" or (to_char(mstr.fromTime,'hh24:mi:ss') <= :toTime AND to_char(mstr.toTime,'hh24:mi:ss') >= :toTime) " 
				+" or (to_char(mstr.fromTime,'hh24:mi:ss')  >= :fromTime   AND to_char(mstr.toTime,'hh24:mi:ss') <= :toTime))"),
	
	@NamedQuery(name="TO_CHECK_OT_USED_WITH_PRO_REQ_ID",
	query="SELECT mstr.procedureOtScheduleId as procedureOtScheduleId "
				+ " FROM ProcedureOTSchedule mstr "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.tableId = :opTable "
				+ " AND mstr.proReqId <> :proReqId "
				+ " AND to_char(mstr.prodeureDate,'yyyy-MM-dd')= :otDate "
				+ " AND mstr.theatreId = :opThreatre "
				+ " AND mstr.status ='A' "
				+ " AND ((to_char(mstr.fromTime,'hh24:mi:ss') <= :fromTime  AND to_char(mstr.toTime,'hh24:mi:ss')  >= :fromTime ) " 
				+" or (to_char(mstr.fromTime,'hh24:mi:ss') <= :toTime AND to_char(mstr.toTime,'hh24:mi:ss') >= :toTime) " 
				+" or (to_char(mstr.fromTime,'hh24:mi:ss')  >= :fromTime   AND to_char(mstr.toTime,'hh24:mi:ss') <= :toTime))")
})



@Entity	
@Table(name = "t_procedure_ot_schedule", schema = "otims")
@SequenceGenerator(name = "procedure_ot_schedule_seq", sequenceName = "otims.procedure_ot_schedule_seq", allocationSize = 1)
public class ProcedureOTSchedule {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "procedure_ot_schedule_seq")
	@Column(name = "procedure_ot_schedule_id")
	private Integer procedureOtScheduleId;
	
	@Column(name = "pro_req_id")
	private Integer proReqId;
	
	@Column(name = "theatre_id")
	private Integer theatreId;
	
	@Column(name = "table_id")
	private Integer tableId ;
	
	@Column(name = "prodeure_date")
	private Date prodeureDate;
	
	@Column(name = "f_time")
	@Convert(converter = InstantConverter.class)
	private Instant fromTime;
	
	@Column(name = "t_time")
	@Convert(converter = InstantConverter.class)
	private Instant toTime;
	
	@Column(name = "status")
	private char status;
			
	@Column(name = "organization_id")
	private Integer organizationId;
	
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
	
	@Column(name = "procedure_schedule_id")
	private Integer procedureScheduleId;
	
	@Column(name = "color")
	private String color;
	
	@Column(name = "remark")
	private String remark;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "theatre_id", insertable = false, updatable = false)
	OperationTheatre operationTheatre;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "table_id", insertable = false, updatable = false)
	OperationTable operationTable;

	public Integer getProcedureOtScheduleId() {
		return procedureOtScheduleId;
	}

	public void setProcedureOtScheduleId(Integer procedureOtScheduleId) {
		this.procedureOtScheduleId = procedureOtScheduleId;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public Integer getTheatreId() {
		return theatreId;
	}

	public void setTheatreId(Integer theatreId) {
		this.theatreId = theatreId;
	}

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public Date getProdeureDate() {
		return prodeureDate;
	}

	public void setProdeureDate(Date prodeureDate) {
		this.prodeureDate = prodeureDate;
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

	public OperationTheatre getOperationTheatre() {
		return operationTheatre;
	}

	public void setOperationTheatre(OperationTheatre operationTheatre) {
		this.operationTheatre = operationTheatre;
	}

	public OperationTable getOperationTable() {
		return operationTable;
	}

	public void setOperationTable(OperationTable operationTable) {
		this.operationTable = operationTable;
	}

	public Integer getProcedureScheduleId() {
		return procedureScheduleId;
	}

	public void setProcedureScheduleId(Integer procedureScheduleId) {
		this.procedureScheduleId = procedureScheduleId;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

}
