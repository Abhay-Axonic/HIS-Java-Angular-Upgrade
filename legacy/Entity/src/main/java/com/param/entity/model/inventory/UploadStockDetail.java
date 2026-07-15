package com.param.entity.model.inventory;

import java.io.Serializable;
import java.util.Date;

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
import com.param.entity.model.master.Status;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;

@Entity(name = "UploadStockDetail")
@Table(name = "t_upload_stock_detail", schema = "inventory")
public class UploadStockDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id")
	private Batch batch;

	private Integer qty;

	@Column(name = "approved_upload_quantity")
	private Integer approvedUploadQuantity = 0;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_type_id")
	private UomType uomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_unit_id")
	private UomUnit uomUnit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upload_stock_id")
	private UploadStock uploadStock;
	
	@Column(name = "variance_qty")
	private Integer varianceQty;
	@Column(name = "variance_cop")
	private Double varianceCop;
	@Column(name = "variance_mrp")
	private Double varianceMrp;
	public Integer getVarianceQty() {
		return varianceQty;
	}

	public void setVarianceQty(Integer varianceQty) {
		this.varianceQty = varianceQty;
	}

	public Double getVarianceCop() {
		return varianceCop;
	}

	public void setVarianceCop(Double varianceCop) {
		this.varianceCop = varianceCop;
	}

	public Double getVarianceMrp() {
		return varianceMrp;
	}

	public void setVarianceMrp(Double varianceMrp) {
		this.varianceMrp = varianceMrp;
	}

	public UploadStockDetail() {
		super();
	}

	public UploadStockDetail(Integer id) {
		super();
		this.id = id;
	}

	public UploadStockDetail(Batch batch, Integer qty, UomType uomType, UomUnit uomUnit, Status status) {
		super();
		this.batch = batch;
		this.qty = qty;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.status = status;
	}
	public UploadStockDetail(Batch batch, Integer qty, UomType uomType, UomUnit uomUnit, Status status,Integer approvedUploadQuantity) {
		super();
		this.batch = batch;
		this.qty = qty;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.status = status;
		this.approvedUploadQuantity = approvedUploadQuantity;
	} 


	public UploadStockDetail(Batch batch2, UomType uomType2, UomUnit uomUnit2, Status status2) {
		super();
		this.batch = batch2;
		this.uomType = uomType2;
		this.uomUnit = uomUnit2;
		this.status = status2;
	}

	public Integer getId() {
		return this.id;
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

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
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

	public UploadStock getUploadStock() {
		return uploadStock;
	}

	public void setUploadStock(UploadStock uploadStock) {
		this.uploadStock = uploadStock;
	}

	public void updateUploadStockDetail(Integer qty) {
		this.qty = qty;
	}

	public Integer getApprovedUploadQuantity() {
		return approvedUploadQuantity;
	}

	public void setApprovedUploadQuantity(Integer approvedUploaQuantity) {
		this.approvedUploadQuantity = approvedUploaQuantity;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void approveUploadStockDetail(Integer approvedUploadQuantity, Status status, Date updatedDate,
			User updatedUser, Integer varianceQty, Double varianceCop ,Double varianceMrp) {
		this.approvedUploadQuantity = approvedUploadQuantity;
		this.status = status;
		this.updatedDate = updatedDate;
		this.updatedBy = updatedUser;
		this.varianceQty = varianceQty;
		this.varianceCop = varianceCop;
		this.varianceMrp = varianceMrp;
		
		
	}

}