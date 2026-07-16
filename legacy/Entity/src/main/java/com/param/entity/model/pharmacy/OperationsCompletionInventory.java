package com.param.entity.model.pharmacy;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.param.entity.model.adt.Admission;
import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.Store;

@Entity(name = "OperationsCompletionInventory")
@Table(name = "t_operations_completion_inventory", schema = "theatre_management")
public class OperationsCompletionInventory extends BaseEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admission_id")
	private Admission admission;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@Column(name = "is_medication", nullable = false)
	private Boolean isMedication = false;

	@Column(name = "is_implant", nullable = false)
	private Boolean isImplant = false;

	@Column(name = "is_consumable", nullable = false)
	private Boolean isConsumable = false;

	// bi-directional many-to-one association to OperationsCompletionInventoryDetail
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "operationsCompletionInventory", cascade = CascadeType.ALL)
	private List<OperationsCompletionInventoryDetail> operationsCompletionInventoryDetails;

	public OperationsCompletionInventory() {
		super();
	}

	public OperationsCompletionInventory(Admission admission, Store store, Boolean isMedication, Boolean isImplant,
			Boolean isConsumable) {
		super();
		this.admission = admission;
		this.store = store;
		this.isMedication = isMedication;
		this.isImplant = isImplant;
		this.isConsumable = isConsumable;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Boolean getIsMedication() {
		return isMedication;
	}

	public void setIsMedication(Boolean isMedication) {
		this.isMedication = isMedication;
	}

	public Boolean getIsImplant() {
		return isImplant;
	}

	public void setIsImplant(Boolean isImplant) {
		this.isImplant = isImplant;
	}

	public Boolean getIsConsumable() {
		return isConsumable;
	}

	public void setIsConsumable(Boolean isConsumable) {
		this.isConsumable = isConsumable;
	}

	public Admission getAdmission() {
		return admission;
	}

	public void setAdmission(Admission admission) {
		this.admission = admission;
	}

	public List<OperationsCompletionInventoryDetail> getOperationsCompletionInventoryDetails() {
		return operationsCompletionInventoryDetails;
	}

	public void setOperationsCompletionInventoryDetails(
			List<OperationsCompletionInventoryDetail> operationsCompletionInventoryDetails) {
		this.operationsCompletionInventoryDetails = operationsCompletionInventoryDetails;
	}

	public OperationsCompletionInventoryDetail addOperationsCompletionInventoryDetail(
			OperationsCompletionInventoryDetail operationsCompletionInventoryDetail) {
		if (getOperationsCompletionInventoryDetails() == null)
			this.operationsCompletionInventoryDetails = new ArrayList<OperationsCompletionInventoryDetail>();
		getOperationsCompletionInventoryDetails().add(operationsCompletionInventoryDetail);
		operationsCompletionInventoryDetail.setOperationsCompletionInventory(this);

		return operationsCompletionInventoryDetail;
	}

	public OperationsCompletionInventoryDetail removeOperationsCompletionInventoryDetail(
			OperationsCompletionInventoryDetail operationsCompletionInventoryDetail) {
		getOperationsCompletionInventoryDetails().remove(operationsCompletionInventoryDetail);
		operationsCompletionInventoryDetail.setOperationsCompletionInventory(null);

		return operationsCompletionInventoryDetail;
	}
}
