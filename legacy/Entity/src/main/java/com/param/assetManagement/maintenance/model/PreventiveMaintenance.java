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
})

@Entity
@Table(name = "t_preventive_maintenance", schema = "assets")
@SequenceGenerator(name = "seq_preventive_maintenance", sequenceName = "assets.seq_preventive_maintenance", allocationSize = 1)
public class PreventiveMaintenance {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_preventive_maintenance")
	
	@Column(name = "preventive_maintenance_id")
	private Integer preventiveMaintenanceId;
	
	@Column(name = "asset_category")
	private String assetCategory;
	
	@Column(name = "asset_code")
	private String itemCode;
	
	@Column(name = "asset_name")
	private String itemName;
	
	@Column(name = "serial_no")
	private String serialNo;
	
	@Column(name = "vendor")
	private String vendor;
	
	@Column(name = "pms_frequency_id")
	private Integer pmsFrequencyId;
	
	@Column(name = "pms_type_id")
	private Integer pmsTypeId;
	
	@Column(name = "start_date")
	private Date startDate;
	
	@Column(name = "end_date")
	private Date endDate;
	
	
	
	/*@Column(name="completion_time")
	@Convert(converter = LocalTimeConverter.class)
	private Instant completionTime;
	
	@Column(name="resolution_expected_time")
	@Convert(converter = LocalTimeConverter.class)
	private Instant resolutionExpectedTime;
	
	@Column(name="down_time")
	@Convert(converter = LocalTimeConverter.class)
	private Instant downTime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", insertable = false, updatable = false)
	private Item item;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", insertable = false, updatable = false)
	private UserMaster userMaster;*/

	
	
	public Integer getPreventiveMaintenanceId() {
		return preventiveMaintenanceId;
	}

	public void setPreventiveMaintenanceId(Integer preventiveMaintenanceId) {
		this.preventiveMaintenanceId = preventiveMaintenanceId;
	}

	public String getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(String assetCategory) {
		this.assetCategory = assetCategory;
	}

	

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Integer getPmsFrequencyId() {
		return pmsFrequencyId;
	}

	public void setPmsFrequencyId(Integer pmsFrequencyId) {
		this.pmsFrequencyId = pmsFrequencyId;
	}

	public Integer getPmsTypeId() {
		return pmsTypeId;
	}

	public void setPmsTypeId(Integer pmsTypeId) {
		this.pmsTypeId = pmsTypeId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	
	
}
