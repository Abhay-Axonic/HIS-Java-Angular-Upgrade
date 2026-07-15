package com.param.entity.model.pharmacy;

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

@Entity(name = "CrashCartBatchMapper")
@Table(name = "t_crash_cart_batch_mapper", schema = "pharmacy" )
public class CrashCartBatchMapper extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id")
	private Batch batch;

	@Column(name = "consumed_qty")
	private Integer consumedQty;

	// bi-directional many-to-one association to TCrashCartRequestDetail
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "crash_cart_detail_id")
	private CrashCartRequestDetail crashCartRequestDetail;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_type_id", nullable = false)
	private UomType uomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_unit_id", nullable = false)
	private UomUnit uomUnit;

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

	public CrashCartBatchMapper() {
		super();
	}

	public CrashCartBatchMapper(Batch batch, Integer consumedQty, CrashCartRequestDetail crashCartRequestDetail) {
		super();
		this.batch = batch;
		this.consumedQty = consumedQty;
		this.crashCartRequestDetail = crashCartRequestDetail;
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

	public Integer getConsumedQty() {
		return consumedQty;
	}

	public void setConsumedQty(Integer consumedQty) {
		this.consumedQty = consumedQty;
	}

	public CrashCartRequestDetail getCrashCartRequestDetail() {
		return crashCartRequestDetail;
	}

	public void setCrashCartRequestDetail(CrashCartRequestDetail crashCartRequestDetail) {
		this.crashCartRequestDetail = crashCartRequestDetail;
	}

}