package com.param.assetManagement.maintenance.model;


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

import com.param.adt.master.global.model.UserMaster;
import com.param.adt.master.unit.model.NursingStationMaster;
import com.param.entity.model.master.Item;
import com.param.global.common.LocalTimeConverter;

@NamedQueries({
	
	/*@NamedQuery(name="GET_PREVENTIVE_MAINTENANCE_LIST",
		query="select distinct ON(item.id)item.id as assetNameId, assc.id as assetCategoryId,assc.category as assetCategory," +
				" pc.id as productCategoryId,pc.category as productCategory, assc.code as assetCode, item.name as assetName," +
				" batch.batch_no as serialNo, vndr.name as vendor from public.m_item item " + 
				" inner join inventory.t_batch batch on item.id = batch.item_id " + 
				" inner join public.m_product_category  pc on item.product_category_id = pc.id " + 
				" inner join public.m_asset_category assc on pc.asset_category_id = assc.id  " + 
				" inner join public.m_asset_type asst on asst.id = assc.asset_type_id " + 
				" inner join public.m_vendor vndr on vndr.id = batch.vendor_id " + 
				" where item.is_active=true and item.unit_id=:unitId and item.organization_id =:organizationId")*/
	
	@NamedQuery(name = "GET_ASSET_CHECKLIST_LIST_BY_ID", 
			query = "SELECT check.assetScheduleChecklistId as assetScheduleChecklistId, check.assetScheduleId as assetScheduleId, "
			+ "check.checklistNameId as checklistNameId, check.checklistName as checklistName, check.checklistCode as checklistCode, "
			+ "check.status as status FROM AssetScheduleChecklist as check "
			+ "WHERE check.status='A' AND check.assetScheduleId=:assetScheduleId "
			+ "AND check.unitId=:unitId AND check.organizationId=:organizationId "
			+ "ORDER BY check.assetScheduleChecklistId ASC "),
})

@Entity
@Table(name = "m_asset_schedule_checklist", schema = "equipment")
@SequenceGenerator(name = "seq_m_asset_schedule_checklist", sequenceName = "equipment.seq_m_asset_schedule_checklist", allocationSize = 1)
public class AssetScheduleChecklist {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_asset_schedule_checklist")
	
	@Column(name = "m_asset_schedule_checklist_Id")
	private Integer assetScheduleChecklistId;
	
	@Column(name = "asset_schedule_id")
	private Integer assetScheduleId;
	
	@Column(name = "checklist_name_id")
	private Integer checklistNameId;
	
	@Column(name = "checklist_name")
	private String checklistName;
	
	@Column(name = "checklist_code")
	private String checklistCode;
	
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

	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="asset_schedule_id", insertable=false, updatable=false)
	private AssetSchedule assetSchedule;

	public Integer getAssetScheduleId() {
		return assetScheduleId;
	}

	public void setAssetScheduleId(Integer assetScheduleId) {
		this.assetScheduleId = assetScheduleId;
	}

	public Integer getChecklistNameId() {
		return checklistNameId;
	}

	public void setChecklistNameId(Integer checklistNameId) {
		this.checklistNameId = checklistNameId;
	}

	public String getChecklistName() {
		return checklistName;
	}

	public void setChecklistName(String checklistName) {
		this.checklistName = checklistName;
	}

	public String getChecklistCode() {
		return checklistCode;
	}

	public void setChecklistCode(String checklistCode) {
		this.checklistCode = checklistCode;
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

	public Integer getAssetScheduleChecklistId() {
		return assetScheduleChecklistId;
	}

	public void setAssetScheduleChecklistId(Integer assetScheduleChecklistId) {
		this.assetScheduleChecklistId = assetScheduleChecklistId;
	}
	
	
	
	

	
	
}
