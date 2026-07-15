package com.param.otims.model;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.LocalTimeConverter;
import com.param.global.common.InstantConverter;

@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_OT_EQUIPMENT_BY_PRO_REQ_ID", query = 
	" SELECT equip.ot_completion_equipment_id  as \"otCompletionEquipmentId\"," + 
	" equip.procedure_order_id  as \"procedureOrderId\", " + 
	" equip.equipment_type_id  as \"equipmentTypeId\"," + 
	" equip.equipment_id  as \"itemId\"," + 
	" to_char(equip.from_time,'HH24:MI:SS') as \"fromTime\", " + 
	" to_char(equip.to_time,'HH24:MI:SS') as \"toTime\", " + 
	" equip.amount  as \"amount\", " + 
	" equip.status  as \"status\"," + 
	" equip.org_id  as \"organizationId\", " + 
	" equip.unit_id  as \"unitId\", " + 
	" equip.created_by  as \"createdBy\", " + 
	" to_char(equip.created_date, 'DD-MM-YYYY HH24:MI:SS') as \"createdDate\", " + 
	" to_char(equip.from_date, 'DD-MM-YYYY HH24:MI:SS') as \"fromDate\", " + 
	" to_char(equip.to_date, 'DD-MM-YYYY HH24:MI:SS') as \"toDate\", " + 
	" equip.updated_by as \"updatedBy\", " + 
	" to_char(equip.updated_date, 'DD-MM-YYYY HH24:MI:SS') as \"updatedDate\", " + 
	" equip.pro_req_id  as \"proReqId\"," + 
	" equip.service_id  as \"serviceId\"," + 
	" item.name as \"equipmentName\"," + 
	" equip.batch_id as \"batchId\"," + 
	" asstcat.category as \"equipmentTypeName\" " + 
	" FROM otims.t_ot_completion_equipment equip " + 
	" inner join public.m_item  item on item.id = equip.equipment_id " + 
	" inner join public.m_asset_category asstcat on asstcat.id = equip.equipment_type_id " + 
	" where equip.pro_req_id =  :id ") 
	})
@Entity
@Table(name = "t_ot_completion_equipment" , schema = "otims")
@SequenceGenerator(name = "t_ot_completion_equipment_seq" , sequenceName = "otims.t_ot_completion_equipment_seq" , allocationSize = 1)
public class OtCompletionEquipment {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_ot_completion_equipment_seq")
	@Column(name = "ot_completion_equipment_id")
	private Integer otCompletionEquipmentId;
	
	@Column(name = "procedure_order_id")
	private Integer procedureOrderId;
	
	@Column(name = "equipment_type_id")
	private Integer equipmentTypeId;
	
	@Column(name = "equipment_id")
	private Integer equipmentId;
	
	@Column(name = "from_time")
	@Convert(converter = InstantConverter.class)
	private Instant fromTime;
	
	//private Date fromTime;
	
	@Column(name = "to_time")
	@Convert(converter = InstantConverter.class)
	private Instant toTime;
	
	//private Date toTime;
	
	@Column(name = "amount")
	private double amount;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "pro_req_id")
	private Integer proReqId;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "batch_id")
	private Integer batchId;
	
	@Column(name = "from_date")
	private Date fromDate;
	
	@Column(name = "to_date")
	private Date toDate;
	

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public int getOtCompletionEquipmentId() {
		return otCompletionEquipmentId;
	}

	public void setOtCompletionEquipmentId(int otCompletionEquipmentId) {
		this.otCompletionEquipmentId = otCompletionEquipmentId;
	}

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}

	public Integer getEquipmentTypeId() {
		return equipmentTypeId;
	}

	public void setEquipmentTypeId(Integer equipmentTypeId) {
		this.equipmentTypeId = equipmentTypeId;
	}

	public Integer getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Integer equipmentId) {
		this.equipmentId = equipmentId;
	}

	

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
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

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public void setOtCompletionEquipmentId(Integer otCompletionEquipmentId) {
		this.otCompletionEquipmentId = otCompletionEquipmentId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}
	
}
