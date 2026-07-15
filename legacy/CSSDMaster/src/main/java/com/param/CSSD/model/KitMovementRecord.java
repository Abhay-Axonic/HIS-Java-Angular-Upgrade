package com.param.CSSD.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.model.master.Store;

@Entity
@Table(name = "t_kit_movement_record", schema = "cssd")
@SequenceGenerator(name = "t_kit_movement_record_id_seq", sequenceName = "cssd.t_kit_movement_record_id_seq", allocationSize = 1)
public class KitMovementRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_kit_movement_record_id_seq")
	@Column(name = "id")
	private Integer kitMovementRecordId;

	@Column(name = "transition_no")
	private Integer transitionNumber;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transition_no", insertable = false, updatable = false)
	private PreSterilizationProcess preSterilizationProcess;
	
	@Column(name = "movement_date")
	private Date movementDate;
	
	@Column(name = "moved_by")
	private Integer movedBy;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "with_indent")
	private Integer withIndent;
	
	@Column(name = "indent_no")
	private Integer indentNo;
	
	@Column(name = "request_no")
	private Integer requestNo;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "request_no", insertable = false, updatable = false)
	private SterilizationRequest  sterilizationRequest;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "indent_no", insertable = false, updatable = false)
	private KitIndent  kitIndent;
	
	@Column(name = "from_store_id")
	private Integer fromStoreId;
	
	@Column(name = "to_store_id")
	private Integer toStoreId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_store_id", insertable = false, updatable = false)
	private Store fromStore;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_store_id", insertable = false, updatable = false)
	private Store toStore;
	
	
	@Column(name = "movement_type")
	private String movementType;

	public Integer getKitMovementRecordId() {
		return kitMovementRecordId;
	}

	public void setKitMovementRecordId(Integer kitMovementRecordId) {
		this.kitMovementRecordId = kitMovementRecordId;
	}

	public Integer getTransitionNumber() {
		return transitionNumber;
	}

	public void setTransitionNumber(Integer transitionNumber) {
		this.transitionNumber = transitionNumber;
	}

	public PreSterilizationProcess getPreSterilizationProcess() {
		return preSterilizationProcess;
	}

	public void setPreSterilizationProcess(PreSterilizationProcess preSterilizationProcess) {
		this.preSterilizationProcess = preSterilizationProcess;
	}

	public Date getMovementDate() {
		return movementDate;
	}

	public void setMovementDate(Date movementDate) {
		this.movementDate = movementDate;
	}

	public Integer getMovedBy() {
		return movedBy;
	}

	public void setMovedBy(Integer movedBy) {
		this.movedBy = movedBy;
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

	public Integer getWithIndent() {
		return withIndent;
	}

	public void setWithIndent(Integer withIndent) {
		this.withIndent = withIndent;
	}

	public Integer getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(Integer indentNo) {
		this.indentNo = indentNo;
	}

	public Integer getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(Integer requestNo) {
		this.requestNo = requestNo;
	}

	public SterilizationRequest getSterilizationRequest() {
		return sterilizationRequest;
	}

	public void setSterilizationRequest(SterilizationRequest sterilizationRequest) {
		this.sterilizationRequest = sterilizationRequest;
	}

	public KitIndent getKitIndent() {
		return kitIndent;
	}

	public void setKitIndent(KitIndent kitIndent) {
		this.kitIndent = kitIndent;
	}

	public Integer getFromStoreId() {
		return fromStoreId;
	}

	public void setFromStoreId(Integer fromStoreId) {
		this.fromStoreId = fromStoreId;
	}

	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
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

	public String getMovementType() {
		return movementType;
	}

	public void setMovementType(String movementType) {
		this.movementType = movementType;
	}
	
	
	
		
}
