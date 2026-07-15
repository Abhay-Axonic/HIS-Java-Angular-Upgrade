package com.param.assetManagement.maintenance.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.param.entity.model.master.Item;

//@NamedQueries({
//	@NamedQuery(name = "GET_SPARE_CONSUMPTION_LIST_BY_PMS_ID", 
//	query = "SELECT spare.spareConsumptionId as spareConsumptionId, spare.assetId as assetId, item.code as assetCode, "
//	+ "spare.serialNo as serialNo, spare.assetQty as assetQty, spare.remark as remark, item.name as assetName, "
//	+ "spare.storeId as storeId FROM SpareConsumption as spare "
//	+ " left join spare.item item  "
//	+ "WHERE spare.pmsDetailsId=:pmsDetailsId AND spare.unitId=:unitId AND spare.organizationId=:organizationId"),
//	
//})

@Entity
@Table(name = "m_spare_consumption", schema = "equipment")
@SequenceGenerator(name = "seq_m_spare_consumption", sequenceName = "equipment.seq_m_spare_consumption", allocationSize = 1)
public class SpareConsumption {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_spare_consumption")
	
	@Column(name = "spare_consumption_id")
	private Integer spareConsumptionId;
	
	@Column(name = "pms_details_id")
	private Integer pmsDetailsId;
	
	@Column(name = "asset_id")
	private Integer assetId;
	
	@Transient
	private String assetCode;
	
	@Transient
	private String assetName;
	
	@Column(name = "serial_no")
	private String serialNo;

	@Column(name = "asset_qty")
	private Integer assetQty;

	@Column(name = "remark")
	private String remark;
	
	@Column(name = "store_id")
	private Integer storeId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by", updatable = false)
	private Integer createdBy;

	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name="unit_id", updatable = false)
	private Integer unitId;
	
	@Column(name="organization_id", updatable = false)
	private Integer organizationId;
	
//	@OneToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "asset_id", insertable = false, updatable = false)
//	private Item item;

	public Integer getSpareConsumptionId() {
		return spareConsumptionId;
	}

	public void setSpareConsumptionId(Integer spareConsumptionId) {
		this.spareConsumptionId = spareConsumptionId;
	}

	public Integer getPmsDetailsId() {
		return pmsDetailsId;
	}

	public void setPmsDetailsId(Integer pmsDetailsId) {
		this.pmsDetailsId = pmsDetailsId;
	}

	public Integer getAssetId() {
		return assetId;
	}

	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Integer getAssetQty() {
		return assetQty;
	}

	public void setAssetQty(Integer assetQty) {
		this.assetQty = assetQty;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
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

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	
	
	
}
