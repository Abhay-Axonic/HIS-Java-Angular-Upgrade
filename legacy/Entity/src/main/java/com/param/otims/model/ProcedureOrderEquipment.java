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
	
	@NamedQuery(name="GET_PROCEDURE_ORDER_EQUIPMENT_BY_PROCEDURE_ORDER_ID",
	query="SELECT mstr.procedureOrderEquipmentId as procedureOrderEquipmentId , " 
				+ " mstr.procedureRequestId as procedureRequestId , "
				+ " mstr.itemId as itemId, "
				+ " mstr.qty as qty, "
				+ " mstr.procedureOrderId as procedureOrderId, "
				+ " mstr.organizationId as organizationId, "
				+ " mstr.unitId as unitId, "
				+ " mstr.createdBy as createdBy, "
				+ " mstr.updatedBy as updatedBy, "
				+ " to_char(mstr.createdDate, 'dd-MM-yyyy HH:ss:mm')as createdDate, "
				+ " to_char(mstr.updatedDate, 'dd-MM-yyyy HH:ss:mm')as updatedDate, "
				+ " mstr.status as status, "
				+ " item.name as itemName, "
				+ " assetCategory.category as equipmentTypeName, "
				+ " assetCategory.id as equipmentTypeId "
				+ " FROM ProcedureOrderEquipment mstr "
				+ " INNER JOIN  mstr.item item  "
				+ " INNER JOIN  item.productCategory productCategory  "
				+ " INNER JOIN  productCategory.assetCategory assetCategory  "
				+ " WHERE  mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.status = 'A' "
				+ " AND mstr.procedureOrderId= :procedureOrderId "
				)
})
@Entity	
@Table(name = "t_procedure_order_equipment", schema = "otims")
@SequenceGenerator(name = "procedure_order_equipment_seq", sequenceName = "otims.procedure_order_equipment_seq", allocationSize = 1)
public class ProcedureOrderEquipment {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "procedure_order_equipment_seq")
	@Column(name = "procedure_order_equipment_id")
	private Integer procedureOrderEquipmentId;
	
	@Column(name = "procedure_request_id")
	private Integer procedureRequestId;
	
	@Column(name = "item_id")
	private Integer itemId ;
	
	@Column(name = "qty")
	private Integer qty ;
	
	@Column(name = "procedure_order_id")
	private Integer procedureOrderId ;
	
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

	public Integer getProcedureOrderEquipmentId() {
		return procedureOrderEquipmentId;
	}

	public void setProcedureOrderEquipmentId(Integer procedureOrderEquipmentId) {
		this.procedureOrderEquipmentId = procedureOrderEquipmentId;
	}

	public Integer getProcedureRequestId() {
		return procedureRequestId;
	}

	public void setProcedureRequestId(Integer procedureRequestId) {
		this.procedureRequestId = procedureRequestId;
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

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
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
