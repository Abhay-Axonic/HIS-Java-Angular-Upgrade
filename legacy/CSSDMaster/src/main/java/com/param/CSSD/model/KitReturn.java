package com.param.CSSD.model;

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

import com.param.adt.master.global.model.UserMaster;
import com.param.entity.model.master.Store;


@NamedQueries({

	@NamedQuery(name = "GET_KIT_RETURN_LIST", query = "SELECT kitReturn.returnNo as returnNo,"
			+ "to_char(kitReturn.createdDate,'dd-MM-yyyy') as createdDate,"
			+ "kitReturn.createdBy as createdBy, fromStore.storeName as fromStore,"
			+ "toStore.storeName as toStore,kitReturn.status as status,kitReturn.type as type,"
			+ "kitReturn.reason as reason,kitReturn.remark as remark,kitReturn.carrier as carrier "
			+ "FROM KitReturn kitReturn INNER JOIN kitReturn.fromStore as fromStore "
			+ "INNER JOIN kitReturn.toStore as toStore"),
	@NamedQuery(name = "GET_KIT_RETURN_BY_MULTIPLE_FIELDS", query = "SELECT kitReturn.returnNo as returnNo,"
			+ "to_char(kitReturn.createdDate,'dd-MM-yyyy') as createdDate,"
			+ "kitReturn.createdBy as createdBy, fromStore.storeName as fromStore,"
			+ "toStore.storeName as toStore,kitReturn.status as status,kitReturn.type as type,"
			+ "kitReturn.reason as reason,kitReturn.remark as remark,kitReturn.carrier as carrier "
			+ "FROM KitReturn kitReturn INNER JOIN kitReturn.fromStore as fromStore "
			+ "INNER JOIN kitReturn.toStore as toStore WHERE kitReturn.returnNo=:returnNo AND "
			+ "kitReturn.status=:status AND kitReturn.fromStoreId=:fromStore AND kitReturn.toStoreId=:toStore "),
	@NamedQuery(name = "GET_KIT_RETURN_BY_RETURN_NO", query = "SELECT kitReturn.returnNo as returnNo,"
			+ "to_char(kitReturn.createdDate,'dd-MM-yyyy') as createdDate,"
			+ "kitReturn.createdBy as createdBy, fromStore.storeName as fromStore,"
			+ "toStore.storeName as toStore,kitReturn.status as status,kitReturn.type as type,"
			+ "kitReturn.reason as reason,kitReturn.remark as remark,kitReturn.carrier as carrier "
			+ "FROM KitReturn kitReturn INNER JOIN kitReturn.fromStore as fromStore "
			+ "INNER JOIN kitReturn.toStore as toStore WHERE kitReturn.returnNo=:returnNo"),
	@NamedQuery(name = "GET_KIT_LIST_FOR_KIT_RETURN", query ="SELECT loadBatch.batchNumber as batchNumber, "
			+ "kitMaster.kitId as kitId, kitMaster.code as kitCode,kitMaster.name as kitName, "
			+ "to_char(autoclaveMaster.updated_date + autoclaveMaster.biological_indicator_day,'dd-MM-yyyy') as "
			+ "expiryDate FROM LoadBatchKitMapper loadBatchKitMapper INNER JOIN loadBatchKitMapper.loadBatch as "
			+ "loadBatch INNER JOIN loadBatchKitMapper.preSterilizationProcess as preSterilizationProcess "
			+ "INNER JOIN preSterilizationProcess.kitMaster as kitMaster INNER JOIN loadBatch.autoclaveMaster as "
			+ "autoclaveMaster WHERE preSterilizationProcess.kitId IN (SELECT mapper.kitId from KitIssueKitMapper mapper "
			+ "INNER JOIN mapper.kitIssue as kitIssue WHERE kitIssue.status='accepted' AND kitIssue.fromStoreId=:fromStore "
			+ "AND kitIssue.toStoreId=:toStore )"),
	@NamedQuery(name = "GET_KIT_LIST_BY_RETURN_NO", query = "SELECT mapper.kitId as kitId,"
			+ "kitMaster.code as kitCode,kitMaster.name as kitName,mapper.returnQuantity as "
			+ "returnQuantity,mapper.reasonForReturn as reasonForReturn "
			+ "FROM KitReturnKitMapper mapper INNER JOIN mapper.kitMaster as kitMaster "
			+ "WHERE mapper.returnNo=:returnNo")
	
	
	})




@Entity
@Table(name = "t_kit_return", schema = "cssd")
@SequenceGenerator(name = "t_kit_return_id_seq", sequenceName = "cssd.t_kit_return_id_seq", allocationSize = 1)
public class KitReturn {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_kit_return_id_seq")
	@Column(name = "id")
	private Integer returnNo;

	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "from_store_id")
	private Integer fromStoreId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", insertable = false, updatable = false)
	private UserMaster userCreatedBy;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_store_id", insertable = false, updatable = false)
	private Store fromStore;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_store_id", insertable = false, updatable = false)
	private Store toStore;
	
	@Column(name = "to_store_id")
	private Integer toStoreId;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "reason")
	private String reason;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "carrier")
	private String carrier;
	
	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "cancel_reason_id")
	private Integer cancelReasonId;

	@Column(name = "cancel_reason_note")
	private String cancelReasonNote;

	public Integer getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(Integer returnNo) {
		this.returnNo = returnNo;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getFromStoreId() {
		return fromStoreId;
	}

	public void setFromStoreId(Integer fromStoreId) {
		this.fromStoreId = fromStoreId;
	}

	public Store getFromStore() {
		return fromStore;
	}

	public void setFromStore(Store fromStore) {
		this.fromStore = fromStore;
	}

	public Store getToStore() {
		return toStore;
	}

	public void setToStore(Store toStore) {
		this.toStore = toStore;
	}

	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
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

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public String getCancelReasonNote() {
		return cancelReasonNote;
	}

	public void setCancelReasonNote(String cancelReasonNote) {
		this.cancelReasonNote = cancelReasonNote;
	}

	public UserMaster getUserCreatedBy() {
		return userCreatedBy;
	}

	public void setUserCreatedBy(UserMaster userCreatedBy) {
		this.userCreatedBy = userCreatedBy;
	}

	
	
}
