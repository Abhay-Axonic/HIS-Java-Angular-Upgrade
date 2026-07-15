package com.param.assetManagement.maintenance.model;

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
	@NamedQuery(name="GET_SPARE_LIST_BY_SCHEDULE_ID",
		query="SELECT spare.correctiveMaintenanceSparesComsumptionId as correctiveMaintenanceSparesComsumptionId , " 
				+ "spare.correctiveMaintenanceScheduleId as correctiveMaintenanceScheduleId, "
				+ "spare.itemId as itemId, "
				+ "spare.qty as qty, "
				+ "spare.sparesComsumptionRemark as sparesComsumptionRemark, "
				+ "spare.createdBy as createdBy, "
				+ "spare.status as status, "
				+ "spare.updatedBy as updatedBy, "
				+ " to_char(spare.createdDate,'dd-MM-yyyy HH:ss:mm') as createdDate, "
				+ " to_char(spare.updatedDate,'dd-MM-yyyy HH:ss:mm') as updatedDate, "
				+ "item.name as itemName, "
				+ "item.code as itemCode, "
				+ "spare.unitId as unitId, "
				+ "spare.organizationId as organizationId "
				+" FROM CorrectiveMaintenanceSparesComsumption spare " 
				+" INNER JOIN spare.item item "
				+" where spare.correctiveMaintenanceScheduleId = :correctiveMaintenanceScheduleId  "
				+" AND spare.status = 'A'"
				+" AND spare.unitId = :unitId"
				+" AND spare.organizationId = :organizationId " 
			)
})
@Entity
@Table(name = "t_corrective_maintenance_spares_comsumption", schema = "assets")
@SequenceGenerator(name = "seq_corrective_maintenance_spares_comsumption", sequenceName = "assets.seq_corrective_maintenance_spares_comsumption", allocationSize = 1)
public class CorrectiveMaintenanceSparesComsumption {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_corrective_maintenance_spares_comsumption")
	@Column(name = "corrective_maintenance_spares_comsumption_id")
	private Integer correctiveMaintenanceSparesComsumptionId;
	
	@Column(name = "corrective_maintenance_schedule_id")
	private Integer correctiveMaintenanceScheduleId;
	
	@Column(name = "item_id")
	private Integer itemId;
	
	@Column(name = "qty")
	private Integer qty;
	
	@Column(name = "spares_comsumption_remark")
	private String sparesComsumptionRemark;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "org_unit_id ")
	private Integer unitId ;

	@Column(name = "org_id")
	private Integer organizationId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", insertable = false, updatable = false)
	private Item item;

	public Integer getCorrectiveMaintenanceSparesComsumptionId() {
		return correctiveMaintenanceSparesComsumptionId;
	}

	public void setCorrectiveMaintenanceSparesComsumptionId(Integer correctiveMaintenanceSparesComsumptionId) {
		this.correctiveMaintenanceSparesComsumptionId = correctiveMaintenanceSparesComsumptionId;
	}

	public Integer getCorrectiveMaintenanceScheduleId() {
		return correctiveMaintenanceScheduleId;
	}

	public void setCorrectiveMaintenanceScheduleId(Integer correctiveMaintenanceScheduleId) {
		this.correctiveMaintenanceScheduleId = correctiveMaintenanceScheduleId;
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

	public String getSparesComsumptionRemark() {
		return sparesComsumptionRemark;
	}

	public void setSparesComsumptionRemark(String sparesComsumptionRemark) {
		this.sparesComsumptionRemark = sparesComsumptionRemark;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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
	
	
	
}
