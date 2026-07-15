package com.param.CSSD.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.UserMaster;
import com.param.entity.model.master.Store;
import com.param.global.model.PriorityMaster;


@NamedQueries({

	@NamedQuery(name = "GET_FROM_STORE_LIST_BY_USER_ID", query = "SELECT storeMst.storeId as storeId, "
			+ "storeMst.storeName as storeName, storeMst.storeCode as storeCode "
			+ "FROM UserStoreMapper mapper INNER JOIN mapper.store as storeMst "
			+ "INNER JOIN storeMst.organization as org INNER JOIN storeMst.unit as uni "
			+ "WHERE mapper.userId=:userId and org.organizationId=:organizationId and uni.unitId=:unitId " +
			" AND mapper.isDeleted = false AND mapper.isActive = true "
			+ " ORDER BY storeMst.storeName ASC"),
	
	@NamedQuery(name = "GET_CSSD_STORE_LIST_BY_USER_ID", query = "SELECT storeMst.storeId as storeId, "
			+ "storeMst.storeName as storeName, storeMst.storeCode as storeCode "
			+ "FROM UserStoreMapper mapper INNER JOIN mapper.store as storeMst INNER JOIN storeMst.storeType as storeType "
			+ "INNER JOIN storeMst.organization as org INNER JOIN storeMst.unit as uni "
			+ "WHERE mapper.userId=:userId and org.organizationId=:organizationId and uni.unitId=:unitId and storeType.id=4 "
			+ ""),
	
	@NamedQuery(name = "GET_CSSD_STORE_LIST", query = "SELECT storeMst.storeId as storeId, "
			+ "storeMst.storeName as storeName, storeMst.storeCode as storeCode "
			+ "FROM Store storeMst "
			+ "INNER JOIN storeMst.storeType as storeType "
			+ "INNER JOIN storeMst.organization as org INNER JOIN storeMst.unit as uni "
			+ "WHERE storeMst.isCSSD= true and org.organizationId=:organizationId and uni.unitId=:unitId "
			+ ""),
	
	@NamedQuery(name = "GET_KIT_INDENT_LIST", query = "SELECT kitIndent.indentNo as indentNo, "
			+ "to_char(kitIndent.indentDate,'dd/MM/yyyy') as indentDate, "
			+ "kitIndent.status as status,fromStore.storeName as fromStore,"
			+ "toStore.storeName as toStore,userCreatedBy.userName as userName FROM KitIndent kitIndent "
			+ "INNER JOIN kitIndent.fromStore as fromStore "
			+ "INNER JOIN kitIndent.toStore as toStore "
			+ "INNER JOIN kitIndent.userCreatedBy as userCreatedBy "
			+ "where kitIndent.organizationId=:organizationId and kitIndent.unitId=:unitId "),
	
	@NamedQuery(name = "GET_KIT_INDENT_BY_INDENT_NO", query = "SELECT kitIndent.indentNo as indentNo,kitIndent.remarks as remarks,"
			+ "kitIndent.fromStoreId as fromStoreId,kitIndent.toStoreId as toStoreId,kitIndent.priorityId as priorityId, "
			+ "to_char(kitIndent.indentDate,'dd/MM/yyyy') as indentDate, "
			+ "kitIndent.status as status,fromStore.storeName as fromStore,"
			+ "toStore.storeName as toStore,userCreatedBy.userName as userName FROM KitIndent kitIndent "
			+ "INNER JOIN kitIndent.fromStore as fromStore "
			+ "INNER JOIN kitIndent.toStore as toStore "
			+ "INNER JOIN kitIndent.userCreatedBy as userCreatedBy "
			+ "WHERE kitIndent.indentNo=:indentNo "
			+ "and kitIndent.organizationId=:organizationId and kitIndent.unitId=:unitId "),
	
	@NamedQuery(name = "GET_KIT_INDENT_BY_MULTIPLE_FIELDS", query = "SELECT kitIndent.indentNo as indentNo, "
			+ "to_char(kitIndent.indentDate,'dd/MM/yyyy') as indentDate, "
			+ "kitIndent.status as status,fromStore.storeName as fromStore,"
			+ "toStore.storeName as toStore,userCreatedBy.userName as userName FROM KitIndent kitIndent "
			+ "INNER JOIN kitIndent.fromStore as fromStore "
			+ "INNER JOIN kitIndent.toStore as toStore "
			+ "INNER JOIN kitIndent.userCreatedBy as userCreatedBy "
			+ "WHERE kitIndent.indentDate BETWEEN :fromDate AND :toDate AND kitIndent.status=:status "
			+ "AND fromStore.storeId=:fromStore AND toStore.storeId=:toStore "
			+ "and kitIndent.organizationId=:organizationId and kitIndent.unitId=:unitId "),
	
	@NamedQuery(name = "GET_KIT_INDENT_BY_MULTIPLE_FIELDS_FOR_ALL", query = "SELECT kitIndent.indentNo as indentNo, "
			+ "to_char(kitIndent.indentDate,'dd/MM/yyyy') as indentDate, "
			+ "kitIndent.status as status,fromStore.storeName as fromStore,"
			+ "toStore.storeName as toStore,userCreatedBy.userName as userName FROM KitIndent kitIndent "
			+ "INNER JOIN kitIndent.fromStore as fromStore "
			+ "INNER JOIN kitIndent.toStore as toStore "
			+ "INNER JOIN kitIndent.userCreatedBy as userCreatedBy "
			+ "WHERE kitIndent.indentDate BETWEEN :fromDate AND :toDate "
			+ "AND fromStore.storeId=:fromStore AND toStore.storeId=:toStore "
			+ "and kitIndent.organizationId=:organizationId and kitIndent.unitId=:unitId ")
	})




@Entity
@Table(name = "t_kit_indent", schema = "cssd")
@SequenceGenerator(name = "t_kit_indent_id_seq", sequenceName = "cssd.t_kit_indent_id_seq", allocationSize = 1)
public class KitIndent {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_kit_indent_id_seq")
	@Column(name = "id")
	private Integer indentNo;

	@Column(name = "indent_date")
	private Date indentDate;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "from_store_id")
	private Integer fromStoreId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "priority_id")
	private Integer priorityId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", insertable = false, updatable = false)
	private UserMaster userCreatedBy;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updated_by", insertable = false, updatable = false)
	private UserMaster userUpdatedBy;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_store_id", insertable = false, updatable = false)
	private Store fromStore;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "priority_id", insertable = false, updatable = false)
	private PriorityMaster priority;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_store_id", insertable = false, updatable = false)
	private Store toStore;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "id", insertable = false, updatable = false)
	private List<KitIndentKitMapper> kitList;
	
	
	@Column(name = "to_store_id")
	private Integer toStoreId;

	@Column(name = "cancel_reason_id")
	private Integer cancelReasonId;

	@Column(name = "cancel_reason_note")
	private String cancelReasonNote;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "reason")
	private String reason;

	@Column(name = "remarks")
	private String remarks;
	
	public Integer getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(Integer indentNo) {
		this.indentNo = indentNo;
	}

	public Date getIndentDate() {
		return indentDate;
	}

	public void setIndentDate(Date indentDate) {
		this.indentDate = indentDate;
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

	public void setFromStore(Integer fromStoreId) {
		this.fromStoreId = fromStoreId;
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

	public List<KitIndentKitMapper> getKitList() {
		return kitList;
	}

	public void setKitList(List<KitIndentKitMapper> kitList) {
		this.kitList = kitList;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setFromStoreId(Integer fromStoreId) {
		this.fromStoreId = fromStoreId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public UserMaster getUserCreatedBy() {
		return userCreatedBy;
	}

	public void setUserCreatedBy(UserMaster userCreatedBy) {
		this.userCreatedBy = userCreatedBy;
	}

	public UserMaster getUserUpdatedBy() {
		return userUpdatedBy;
	}

	public void setUserUpdatedBy(UserMaster userUpdatedBy) {
		this.userUpdatedBy = userUpdatedBy;
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

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public PriorityMaster getPriority() {
		return priority;
	}

	public void setPriority(PriorityMaster priority) {
		this.priority = priority;
	}

	
	
	
}
