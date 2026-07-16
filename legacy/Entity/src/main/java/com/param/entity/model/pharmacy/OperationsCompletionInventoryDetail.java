package com.param.entity.model.pharmacy;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;

@Entity(name = "OperationsCompletionInventoryDetail")
@Table(name = "t_operations_completion_inventory_detail", schema = "theatre_management")
public class OperationsCompletionInventoryDetail extends BaseEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id")
	private Batch batch;

	private double mrp;

	@Column(name = "total_amount")
	private double totalAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_type_id")
	private UomType uomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_unit_id")
	private UomUnit uomUnit;

	@Column(name = "used_quantity")
	private Integer usedQuantity;

	// bi-directional many-to-one association to OperationsCompletionInventory
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "opterations_completion_inventory_id")
	private OperationsCompletionInventory operationsCompletionInventory;

	public OperationsCompletionInventoryDetail() {
	}

	public OperationsCompletionInventoryDetail(Batch batch, double mrp, double totalAmount, UomType uomType,
			UomUnit uomUnit, Integer usedQuantity) {
		this.batch = batch;
		this.mrp = mrp;
		this.totalAmount = totalAmount;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.usedQuantity = usedQuantity;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public double getMrp() {
		return mrp;
	}

	public void setMrp(double mrp) {
		this.mrp = mrp;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public UomType getUomType() {
		return uomType;
	}

	public void setUomType(UomType uomType) {
		this.uomType = uomType;
	}

	public UomUnit getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(UomUnit uomUnit) {
		this.uomUnit = uomUnit;
	}

	public Integer getUsedQuantity() {
		return usedQuantity;
	}

	public void setUsedQuantity(Integer usedQuantity) {
		this.usedQuantity = usedQuantity;
	}

	public OperationsCompletionInventory getOperationsCompletionInventory() {
		return operationsCompletionInventory;
	}

	public void setOperationsCompletionInventory(OperationsCompletionInventory operationsCompletionInventory) {
		this.operationsCompletionInventory = operationsCompletionInventory;
	}

}
