package com.param.otims.model;

import java.util.Date;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.model.master.Item;
@NamedQueries({
	
	@NamedQuery(name="GET_INVENTORY_ITEMS_BY_PROCEDURE_CONFIG_ID",
	query="SELECT mstr.procedureConfigInventoryItemId as procedureConfigInventoryItemId , " 
				+ " mstr.procedureConfigId as procedureConfigId , "
				+ " mstr.itemId as itemId, "
				+ " mstr.qty as qty, "
				+ " item.name as itemName, "
				+ " item.code as itemCode, "
				+ " mstr.organizationId as organizationId, "
				+ " mstr.unitId as unitId, "
				+ " mstr.createdBy as createdBy, "
				+ " mstr.updatedBy as updatedBy, "
				+ " item.isImplant as isImplant, "
				+ " to_char(mstr.createdDate, 'dd-MM-yyyy HH:ss:mm')as createdDate, "
				+ " to_char(mstr.createdDate, 'DD/MM/YYYY')as createdDate1, "
				+ " to_char(mstr.updatedDate, 'dd-MM-yyyy HH:ss:mm')as updatedDate, "
				+ " medicationProductCategory.id as medProductId ,"
				+ " implantProductCategory.id as impProductId ,"
				+ " medAssetCategory.id as medAssetCategoryId ,"
				+ " impAssetCategory.id as impAssetCategoryId , "
				+ " mstr.status as status, "
				+ " tax.id as salesTaxId,"
				+ " generic.id as genericId,"
				+ " generic.name as genericName,"
				+ " manufacturer.id as manufacturerId,"
				+ " manufacturer.name as manufacturerName,"
				+ " tax.name as taxName, "
				+ " procedureConfiguration.serviceId as serviceId,"
				+ " tax.taxPercentage as taxPercent  "
				+ " FROM ProcedureConfigurationInventoryItem mstr "
				+ " INNER JOIN  mstr.item item  "
				+ " LEFT JOIN item.salesTax tax "
				+ " LEFT JOIN item.generic generic "
				+ " LEFT JOIN generic.productCategory medicationProductCategory "
				+ " LEFT JOIN medicationProductCategory.assetCategory medAssetCategory "
				+ " LEFT JOIN item.productCategory implantProductCategory "
				+ " LEFT JOIN implantProductCategory.assetCategory impAssetCategory "
                + " INNER JOIN  mstr.procedureConfiguration procedureConfiguration  "
                + " INNER JOIN item.manufacturer manufacturer "
				+ " WHERE  mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
			//+ " AND mstr.procedureConfigId= :procedureConfigId "
				+ " AND mstr.status = 'A' "
				)
})

@Entity
@Table(name = "t_procedure_config_inventory_item" , schema = "otims")
@SequenceGenerator(name = "procedure_config_inventory_item_seq" , sequenceName = "otims.procedure_config_inventory_item_seq" , allocationSize = 1)
public class ProcedureConfigurationInventoryItem {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "procedure_config_inventory_item_seq")
	@Column(name = "procedure_config_inventory_item_id")
	private Integer procedureConfigInventoryItemId;
	
	@Column(name = "procedure_config_id")
	private Integer procedureConfigId;
	
	@Column(name = "item_id")
	private Integer itemId;
	
	@Column(name = "qty")
	private Integer qty;
	
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", insertable = false, updatable = false)
	private  Item item;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedure_config_id", insertable = false, updatable = false)
    private ProcedureConfiguration procedureConfiguration;


	public Integer getProcedureConfigInventoryItemId() {
		return procedureConfigInventoryItemId;
	}

	public void setProcedureConfigInventoryItemId(Integer procedureConfigInventoryItemId) {
		this.procedureConfigInventoryItemId = procedureConfigInventoryItemId;
	}

	public Integer getProcedureConfigId() {
		return procedureConfigId;
	}

	public void setProcedureConfigId(Integer procedureConfigId) {
		this.procedureConfigId = procedureConfigId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
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
	
}
