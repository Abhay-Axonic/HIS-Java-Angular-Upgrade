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

import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.AssetCategory;
import com.param.entity.model.master.Item;
import com.param.global.common.LocalTimeConverter;
import com.param.global.common.InstantConverter;


@NamedQueries({
	
	@NamedQuery(name="TO_CHECK_EQUIPMENT_USED",
	query="SELECT mstr.procedureEquipmentScheduleId as procedureEquipmentScheduleId "
				+ " FROM ProcedureEquipmentSchedule mstr "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.batchId= :batchId "
				+ " AND to_char(mstr.prodeureDate,'yyyy-MM-dd')= :equDate "
				+ " AND mstr.itemId= :itemId "
				+ " AND mstr.status ='A' "
				+ " AND ((to_char(mstr.fromTime,'hh24:mi:ss') <= :fromTime  AND to_char(mstr.toTime,'hh24:mi:ss')  >= :fromTime ) " 
				+" or (to_char(mstr.fromTime,'hh24:mi:ss') <= :toTime AND to_char(mstr.toTime,'hh24:mi:ss') >= :toTime) " 
				+" or (to_char(mstr.fromTime,'hh24:mi:ss')  >= :fromTime   AND to_char(mstr.toTime,'hh24:mi:ss') <= :toTime))"),
	
	@NamedQuery(name="TO_CHECK_EQUIPMENT_USED_WITH_PRO_REQ_ID",
	query="SELECT mstr.procedureEquipmentScheduleId as procedureEquipmentScheduleId "
				+ " FROM ProcedureEquipmentSchedule mstr "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.batchId = :batchId "
				+ " AND to_char(mstr.prodeureDate,'yyyy-MM-dd')= :equDate "
				+ " AND mstr.itemId = :itemId "
				+ " AND mstr.proReqId <> :proReqId"
				+ " AND mstr.status ='A' "
				+ " AND ((to_char(mstr.fromTime,'hh24:mi:ss') <= :fromTime  AND to_char(mstr.toTime,'hh24:mi:ss')  >= :fromTime ) " 
				+" or (to_char(mstr.fromTime,'hh24:mi:ss') <= :toTime AND to_char(mstr.toTime,'hh24:mi:ss') >= :toTime) " 
				+" or (to_char(mstr.fromTime,'hh24:mi:ss')  >= :fromTime   AND to_char(mstr.toTime,'hh24:mi:ss') <= :toTime))"),
	
	@NamedQuery(name="GET_PROCEDURE_EQUIPMENT_SCHEDULE_BY_PRO_SCHEDULE_ID",
	query="SELECT mstr.procedureEquipmentScheduleId as procedureEquipmentScheduleId , " 
			+ " mstr.proReqId as proReqId , "
			+ " mstr.itemId as itemId, "
			+ " mstr.batchId as batchId, "
			+ " mstr.assetCatId as assetCatId, "
			+ " item.name as itemName, "
			+ " mstr.color as color , "
			+ " to_char(mstr.prodeureDate,'YYYY-MM-DD') as prodeureDate, "
			+" to_char(mstr.fromTime,'hh24:mi') as fromTime, "
			+" to_char(mstr.toTime,'hh24:mi') as toTime, "
			+ " concat( coalesce(item.name), '||', coalesce(batch.batchNo)) as  equipmentName ,"
			+ " assetCategory.category as equipmentTypeName, "
			+ " mstr.organizationId as organizationId, "
			+ " mstr.unitId as unitId, "
			+ " mstr.createdBy as createdBy, "
			+ " mstr.updatedBy as updatedBy, "
			+ " to_char(mstr.createdDate, 'dd-MM-yyyy HH:ss:mm')as createdDate, "
			+ " to_char(mstr.updatedDate, 'dd-MM-yyyy HH:ss:mm')as updatedDate, "
			+ " mstr.status as status "
			+ " FROM ProcedureEquipmentSchedule mstr "
			+ " INNER JOIN  mstr.item item  "
			+ " INNER JOIN mstr.batch batch "
			+ " INNER JOIN  mstr.assetCategory assetCategory  "
			+ " WHERE  mstr.organizationId= :organizationId "
			+ " AND mstr.unitId= :unitId "
			+ " AND mstr.procedureScheduleId= :procedureScheduleId "
			+ " AND mstr.status = 'A' "),
	
	@NamedQuery(name="GET_PROCEDURE_EQUIPMENT_SCHEDULE_BY_PRO_REQ_ID",
	query="SELECT to_char(mstr.prodeureDate, 'YYYY-MM-DD') as prodeureDate ," 
			+ " mstr.proReqId as proReqId , "
			+ " mstr.itemId as itemId, "
			+ " mstr.batchId as batchId, "
			+ " mstr.assetCatId as equipmentTypeId, "//assetCatId
			+" to_char(mstr.fromTime,'hh24:mi') as fromTime, "
			+" to_char(mstr.toTime,'hh24:mi') as toTime, "
			+ " concat( coalesce(item.name), '||', coalesce(batch.batchNo)) as  equipmentName ,"
			+ " assetCategory.category as equipmentTypeName, "
			+ " mstr.organizationId as organizationId, "
			+ " mstr.unitId as unitId, "
			+ " mstr.createdBy as createdBy, "
			+ " mstr.updatedBy as updatedBy, "
			+ " to_char(mstr.createdDate, 'dd-MM-yyyy HH:ss:mm') as createdDate, "
			+ " to_char(mstr.updatedDate, 'dd-MM-yyyy HH:ss:mm') as updatedDate, "
			+ " prm.primaryProcedureId as procedureOrderId, "
			+ " mstr.status as status "
			+ " FROM ProcedureEquipmentSchedule mstr "
			+ " INNER JOIN  mstr.item item  "
			+ " INNER JOIN mstr.batch batch "
			+ " INNER JOIN  mstr.assetCategory assetCategory  "
			+ " INNER JOIN mstr.procedureReqMaster prm "
			+ " WHERE  mstr.proReqId= :proReqId "
			+ " AND mstr.status = 'A' "),
})



@Entity	
@Table(name = "t_procedure_equipment_schedule", schema = "otims")
@SequenceGenerator(name = "procedure_equipment_schedule_seq", sequenceName = "otims.procedure_equipment_schedule_seq", allocationSize = 1)
public class ProcedureEquipmentSchedule {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "procedure_equipment_schedule_seq")
	@Column(name = "procedure_equipment_schedule_id")
	private Integer procedureEquipmentScheduleId;
	
	@Column(name = "pro_req_id")
	private Integer proReqId;
	
	@Column(name = "procedure_id")
	private Integer procedureId;
	
	@Column(name = "asset_cat_id")
	private Integer assetCatId ;
	
	@Column(name = "item_id")
	private Integer itemId ;
	
	@Column(name = "batch_id")
	private Integer batchId ;
	
	@Column(name = "prodeure_date")
	private Date prodeureDate;
	
	@Column(name = "from_time")
	@Convert(converter = InstantConverter.class)
	private Instant fromTime;
	
	@Column(name = "to_time")
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
	@JoinColumn(name = "item_id", insertable = false, updatable = false)
	private  Item item;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", insertable = false, updatable = false)
    private Batch batch;
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_cat_id" , insertable = false, updatable = false)
	private AssetCategory assetCategory;
    
    @ManyToOne(fetch = FetchType.LAZY)
   	@JoinColumn(name = "pro_req_id" , insertable = false, updatable = false)
   	private ProcedureReqMaster procedureReqMaster;

	public Integer getProcedureEquipmentScheduleId() {
		return procedureEquipmentScheduleId;
	}

	public void setProcedureEquipmentScheduleId(Integer procedureEquipmentScheduleId) {
		this.procedureEquipmentScheduleId = procedureEquipmentScheduleId;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public Integer getProcedureId() {
		return procedureId;
	}

	public void setProcedureId(Integer procedureId) {
		this.procedureId = procedureId;
	}

	public Integer getAssetCatId() {
		return assetCatId;
	}

	public void setAssetCatId(Integer assetCatId) {
		this.assetCatId = assetCatId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
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
