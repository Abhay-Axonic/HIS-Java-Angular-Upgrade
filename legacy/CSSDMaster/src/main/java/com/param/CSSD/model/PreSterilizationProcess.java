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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.CSSDMaster.model.KitMaster;
import com.param.CSSDMaster.model.WrapperExpiryMaster;
import com.param.entity.model.master.Store;



@NamedQueries({
		@NamedQuery(name = "GET_PRE_STERILIZATION_PROCESS_LIST", query = "SELECT preSterilizationProcess.transitionNumber as transitionNumber, "
				+ "preSterilizationProcess.kitId as kitId, " + "kitMaster.code as code, " + "kitMaster.name as name, "
				+ "to_char(kitMaster.created_date,'dd-MM-yyyy') as created_date, preSterilizationProcess.status as status " 
				+ "FROM PreSterilizationProcess preSterilizationProcess INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
				),
		@NamedQuery(name = "SEARCH_PRE_STERILIZATION_PROCESS_LIST", query = "SELECT preSterilizationProcess.transitionNumber as transitionNumber, "
				+ "preSterilizationProcess.kitId as kitId, " + "kitMaster.code as code, " + "kitMaster.name as name, "
				+ "to_char(kitMaster.created_date,'dd-MM-yyyy') as created_date, preSterilizationProcess.status as status " 
				+ "FROM PreSterilizationProcess preSterilizationProcess INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
				+ "WHERE lower(kitMaster.name) LIKE :name OR lower(kitMaster.code) LIKE :name "),
		@NamedQuery(name = "GET_PRE_STERILIZATION_PROCESS_KIT_BY_ID", query = "SELECT preSterilizationProcess.transitionNumber as transitionNumber, "
				+ "preSterilizationProcess.kitId as kitId, " + "kitMaster.code as code, " + "kitMaster.name as name, "
				+ "to_char(kitMaster.created_date,'dd-MM-yyyy') as created_date, preSterilizationProcess.status as status " 
				+ "FROM PreSterilizationProcess preSterilizationProcess INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
				+ "WHERE preSterilizationProcess.kitId=:kitId  "),
		@NamedQuery(name = "GET_KIT_LIST_BY_MULTIPLE_FIELDS", query = "SELECT preSterilizationProcess.transitionNumber as transitionNumber, "
				+ "preSterilizationProcess.kitId as kitId, " + "kitMaster.code as code, " + "kitMaster.name as name, "
				+ "to_char(kitMaster.created_date,'dd-MM-yyyy') as created_date, preSterilizationProcess.status as status " 
				+ "FROM PreSterilizationProcess preSterilizationProcess INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
				+ "WHERE kitMaster.name=:name AND "
				+ "preSterilizationProcess.status=:status AND kitMaster.created_date BETWEEN :from AND :to"),
		@NamedQuery(name = "GET_KIT_LIST_PENDING_FOR_INSPECTION", query = "SELECT preSterilizationProcess.transitionNumber as transitionNumber, "
				+ "preSterilizationProcess.kitId as kitId, " + "kitMaster.code as code, " + "kitMaster.name as name, "
				+ "to_char(kitMaster.created_date,'dd-MM-yyyy') as created_date, preSterilizationProcess.status as status " 
				+ "FROM PreSterilizationProcess preSterilizationProcess INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
				+ "WHERE preSterilizationProcess.status='cleaned'  "),
		@NamedQuery(name = "GET_KIT_LIST_PENDING_FOR_DIS_INFECTION", query = "SELECT preSterilizationProcess.transitionNumber as transitionNumber, "
				+ "preSterilizationProcess.kitId as kitId, " + "kitMaster.code as code, " + "kitMaster.name as name, "
				+ "to_char(kitMaster.created_date,'dd-MM-yyyy') as created_date, preSterilizationProcess.status as status " 
				+ "FROM PreSterilizationProcess preSterilizationProcess INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
				+ "WHERE preSterilizationProcess.status='inspected' "),
		@NamedQuery(name = "GET_KIT_LIST_PENDING_FOR_KIT_FORMATION", query = "SELECT preSterilizationProcess.transitionNumber as transitionNumber, "
				+ "preSterilizationProcess.kitId as kitId, " + "kitMaster.code as code, " + "kitMaster.name as name, "
				+ "to_char(kitMaster.created_date,'dd-MM-yyyy') as created_date, preSterilizationProcess.status as status " 
				+ "FROM PreSterilizationProcess preSterilizationProcess INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
				+ "WHERE preSterilizationProcess.status='disinfected'  "),
		@NamedQuery(name = "GET_FORMED_KIT_LIST", query = "SELECT preSterilizationProcess.transitionNumber as transitionNumber, "
				+ "preSterilizationProcess.kitId as kitId, " + "kitMaster.code as code, " + "kitMaster.name as name, "
				+ "to_char(preSterilizationProcess.formation_date,'dd-MM-yyyy') as formation_date, preSterilizationProcess.status as status " 
				+ "FROM PreSterilizationProcess preSterilizationProcess INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
				+ "WHERE preSterilizationProcess.status='formedkit'")
})







@Entity
@Table(name = "t_pre_sterilize_process", schema = "cssd")
@SequenceGenerator(name = "t_pre_sterilize_process_id_seq", sequenceName = "cssd.t_pre_sterilize_process_id_seq", allocationSize = 1)
public class PreSterilizationProcess {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_pre_sterilize_process_id_seq")
	@Column(name = "id")
	private Integer transitionNumber;

	@Column(name = "kit_id")
	private Integer kitId;
	
	@Column(name = "wrapper_id")
	private Integer wrapperId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "store_id")
	private Integer storeId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", insertable = false, updatable = false)
	private Store store;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wrapper_id", insertable = false, updatable = false)
	private WrapperExpiryMaster  wrapperExpiryMaster;
	
	@Column(name = "wrapper_quantity")
	private Integer wrapperQuantity;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "transition")
	private String transition;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kit_id", insertable = false, updatable = false)
	private KitMaster kitMaster;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "kit_id", insertable = false, updatable = false)
	private List<PreSterilizationProcessInstrument> kitEquipmentList;
	
	@Column(name = "created_by")
	private Integer created_by;

	@Column(name = "updated_by")
	private Integer updated_by;

	@Column(name = "created_date")
	private Date created_date;

	@Column(name = "updated_date")
	private Date updated_date;
	
	@Column(name = "formation_date")
	private Date formation_date;
	
	@Column(name = "expiry_date")
	private Date expiryDate;
	
	@Column(name = "is_sterilized")
	private Boolean isSterilized;
	
	@Column(name = "is_issued")
	private Boolean isIssued;
	
	@Column(name = "is_accepted")
	private Boolean isAccepted;
	
	@Column(name = "is_rejected")
	private Boolean isRejected;
	
	@Column(name = "is_returned")
	private Boolean isReturned;
	
	@Column(name = "reject_reason_id")
	private Integer rejectReasonId;
	
	@Column(name = "rejection_note")
	private String rejectionNote;
	
	@Column(name = "reason_for_return")
	private String reasonForReturn;
	
	@Column(name = "sterilization_requested")
	private Boolean sterilizationRequested;
	
	@Column(name = "sterilization_request_no")
	private Integer sterilizationRequestNo;

	@Column(name="is_sterilize_from_opening_stock")
	private Character isSterilizeFromOpeningStock;
	
	public Date getFormation_date() {
		return formation_date;
	}

	public void setFormation_date(Date formation_date) {
		this.formation_date = formation_date;
	}

	public Integer getTransitionNumber() {
		return transitionNumber;
	}

	public void setTransitionNumber(Integer transitionNumber) {
		this.transitionNumber = transitionNumber;
	}

	public Integer getKitId() {
		return kitId;
	}

	public void setKitId(Integer kitId) {
		this.kitId = kitId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public KitMaster getKitMaster() {
		return kitMaster;
	}

	public void setKitMaster(KitMaster kitMaster) {
		this.kitMaster = kitMaster;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public Integer getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(Integer updated_by) {
		this.updated_by = updated_by;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public Date getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(Date updated_date) {
		this.updated_date = updated_date;
	}

	public Integer getWrapperId() {
		return wrapperId;
	}

	public void setWrapperId(Integer wrapperId) {
		this.wrapperId = wrapperId;
	}

	public WrapperExpiryMaster getWrapperExpiryMaster() {
		return wrapperExpiryMaster;
	}

	public void setWrapperExpiryMaster(WrapperExpiryMaster wrapperExpiryMaster) {
		this.wrapperExpiryMaster = wrapperExpiryMaster;
	}

	public Integer getWrapperQuantity() {
		return wrapperQuantity;
	}

	public void setWrapperQuantity(Integer wrapperQuantity) {
		this.wrapperQuantity = wrapperQuantity;
	}

	public List<PreSterilizationProcessInstrument> getKitEquipmentList() {
		return kitEquipmentList;
	}

	public void setKitEquipmentList(List<PreSterilizationProcessInstrument> kitEquipmentList) {
		this.kitEquipmentList = kitEquipmentList;
	}

	public String getTransition() {
		return transition;
	}

	public void setTransition(String transition) {
		this.transition = transition;
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

	public Boolean getIsSterilized() {
		return isSterilized;
	}

	public void setIsSterilized(Boolean isSterilized) {
		this.isSterilized = isSterilized;
	}

	public Boolean getIsIssued() {
		return isIssued;
	}

	public void setIsIssued(Boolean isIssued) {
		this.isIssued = isIssued;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Boolean getIsAccepted() {
		return isAccepted;
	}

	public void setIsAccepted(Boolean isAccepted) {
		this.isAccepted = isAccepted;
	}

	public Boolean getIsRejected() {
		return isRejected;
	}

	public void setIsRejected(Boolean isRejected) {
		this.isRejected = isRejected;
	}

	public String getRejectionNote() {
		return rejectionNote;
	}

	public void setRejectionNote(String rejectionNote) {
		this.rejectionNote = rejectionNote;
	}

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public Boolean getIsReturned() {
		return isReturned;
	}

	public void setIsReturned(Boolean isReturned) {
		this.isReturned = isReturned;
	}

	public String getReasonForReturn() {
		return reasonForReturn;
	}

	public void setReasonForReturn(String reasonForReturn) {
		this.reasonForReturn = reasonForReturn;
	}

	public Boolean getSterilizationRequested() {
		return sterilizationRequested;
	}

	public void setSterilizationRequested(Boolean sterilizationRequested) {
		this.sterilizationRequested = sterilizationRequested;
	}

	public Integer getSterilizationRequestNo() {
		return sterilizationRequestNo;
	}

	public void setSterilizationRequestNo(Integer sterilizationRequestNo) {
		this.sterilizationRequestNo = sterilizationRequestNo;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Character getIsSterilizeFromOpeningStock() {
		return isSterilizeFromOpeningStock;
	}

	public void setIsSterilizeFromOpeningStock(Character isSterilizeFromOpeningStock) {
		this.isSterilizeFromOpeningStock = isSterilizeFromOpeningStock;
	}

	

	
	
	
	
}
