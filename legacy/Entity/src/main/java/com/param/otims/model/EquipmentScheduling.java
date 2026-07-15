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
import com.param.global.common.LocalTimeConverter;
import com.param.global.common.InstantConverter;

@NamedQueries({
	
	@NamedQuery(name="GET_EQUIPMENT_SCHEDULING",
	query="SELECT mstr.equipmentSchedulingId as equipmentSchedulingId, "
				+ " mstr.batchId as batchId, "
				+ " mstr.itemId as itemId, "
				+ " to_char(mstr.fromTime,'hh24:mi:ss') as fromTime, "
				+ " to_char(mstr.toTime,'hh24:mi:ss') as toTime, "
				+ " mstr.dayId as dayId, "
				+ " mstr.status as status, "
				+ " assetCat.category as assetCategory, "
				+ " to_char(mstr.equipmentSchedulingDate,'DD/MM/YYYY') as equipmentSchedulingDate, "
				+ " concat(item.name ,' || ',bth.batchNo) as itemName "
				+ " FROM EquipmentScheduling mstr "
				+ " INNER JOIN mstr.batch bth "
				+ " INNER JOIN bth.item item "
				+ " INNER JOIN mstr.assetCategory assetCat"
				+ " WHERE mstr.orgId= :organizationId "
				+ " AND mstr.unitId= :unitId order by mstr.equipmentSchedulingId desc "),
	
	@NamedQuery(name="TO_CHECK_ALREADY_EXITS_EQUIPMENT_SCHEDULING",
	query="SELECT mstr.equipmentSchedulingId as equipmentSchedulingId "
				+ " FROM EquipmentScheduling mstr "
				+ " WHERE mstr.orgId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.batchId= :batchId "
				+ " AND to_char(mstr.equipmentSchedulingDate,'yyyy-MM-dd')= :equDate "
				+ " AND mstr.itemId= :itemId "
				+ " AND ((to_char(mstr.fromTime,'hh24:mi:ss') <= :fromTime  AND to_char(mstr.toTime,'hh24:mi:ss')  >= :fromTime ) " 
				+" or (to_char(mstr.fromTime,'hh24:mi:ss') <= :toTime AND to_char(mstr.toTime,'hh24:mi:ss') >= :toTime) " 
				+" or (to_char(mstr.fromTime,'hh24:mi:ss')  >= :fromTime   AND to_char(mstr.toTime,'hh24:mi:ss') <= :toTime))"),
	
	@NamedQuery(name="TO_CHECK_AVAILABILITY",
	query="SELECT mstr.equipmentSchedulingId as equipmentSchedulingId "
				+ " FROM EquipmentScheduling mstr "
				+ " WHERE mstr.orgId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.batchId= :batchId "
				+ " AND to_char(mstr.equipmentSchedulingDate,'yyyy-MM-dd')= :equDate "
				+ " AND mstr.itemId= :itemId "
				+ " AND (to_char(mstr.fromTime,'hh24:mi:ss') <= :fromTime  AND to_char(mstr.toTime,'hh24:mi:ss')  >= :toTime ) ")
})


@Entity
@Table(name = "t_equipment_scheduling", schema = "otims")
@SequenceGenerator(name = "equipment_scheduling_seq", sequenceName = "otims.equipment_scheduling_seq", allocationSize = 1)
public class EquipmentScheduling {

	@Id
	@Column(name = "equipment_scheduling_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "equipment_scheduling_seq")
	private Integer equipmentSchedulingId;
	
	@Column(name="asset_category_id")
	private Integer assetCategoryId;
	
	@Column(name="batch_id")
	private Integer batchId;
	
	@Column(name="item_id")
	private Integer itemId;
	
	@Column(name = "equipment_scheduling_date")
	private Date equipmentSchedulingDate;
	
	@Column(name="from_time")
	@Convert(converter = InstantConverter.class)
	private Instant fromTime;
	
	@Column(name="to_time")
	@Convert(converter = InstantConverter.class)
	private Instant toTime;

	@Column(name="day_id")
	private Integer dayId;
	
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
	@JoinColumn(name="batch_id",insertable=false,updatable=false)
	Batch batch ;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="asset_category_id",insertable=false,updatable=false)
	AssetCategory assetCategory ;

	public Integer getEquipmentSchedulingId() {
		return equipmentSchedulingId;
	}

	public void setEquipmentSchedulingId(Integer equipmentSchedulingId) {
		this.equipmentSchedulingId = equipmentSchedulingId;
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Date getEquipmentSchedulingDate() {
		return equipmentSchedulingDate;
	}

	public void setEquipmentSchedulingDate(Date equipmentSchedulingDate) {
		this.equipmentSchedulingDate = equipmentSchedulingDate;
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
