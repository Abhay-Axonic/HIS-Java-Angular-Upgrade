package com.param.entity.model.pharmacy;

import java.io.Serial;
import java.io.Serializable;
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

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;

@Entity(name = "CrashCartRequestDetail")
@Table(name = "t_crash_cart_request_detail", schema = "pharmacy")
public class CrashCartRequestDetail extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "approved_quantity")
	private Integer approvedQty;

	@Column(name = "reject_note", length = 100)
	private String rejectNote;

	@Column(name = "reject_quantity")
	private Integer rejectQty;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reject_reason_id")
	private RejectReason rejectReason;

	@Column(name = "required_quantity")
	private Integer requiredQty;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_type_id")
	private UomType uomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_unit_id")
	private UomUnit uomUnit;
	
	@ManyToOne
	@JoinColumn(name = "crash_cart_id")
	private CrashCartRequest crashCartRequest;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "crashCartRequestDetail", cascade = CascadeType.ALL)
	private List<CrashCartBatchMapper> crashCartBatchMappers;

	public CrashCartRequestDetail() {
	}

	public CrashCartRequestDetail(Integer id) {
		super();
		this.id = id;
	}

	public CrashCartRequestDetail(Item item, UomType uomType, UomUnit uomUnit, Integer requiredQty, Status status) {
		super();
		this.item = item;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.requiredQty = requiredQty;
		this.status = status;

	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getApprovedQty() {
		return this.approvedQty;
	}

	public void setApprovedQty(Integer approvedQty) {
		this.approvedQty = approvedQty;
	}

	public String getRejectNote() {
		return this.rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public Integer getRejectQty() {
		return this.rejectQty;
	}

	public void setRejectQty(Integer rejectQty) {
		this.rejectQty = rejectQty;
	}

	public RejectReason getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(RejectReason rejectReason) {
		this.rejectReason = rejectReason;
	}

	public Integer getRequiredQty() {
		return this.requiredQty;
	}

	public void setRequiredQty(Integer requiredQty) {
		this.requiredQty = requiredQty;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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

	public CrashCartRequest getCrashCartRequest() {
		return this.crashCartRequest;
	}

	public void setCrashCartRequest(CrashCartRequest crashCartRequest) {
		this.crashCartRequest = crashCartRequest;
	}


	  public List<CrashCartBatchMapper> getCrashCartBatchMappers() { 
		  return this.crashCartBatchMappers; 
	 }
	  
	  public void setCrashCartBatchMappers(List<CrashCartBatchMapper> crashCartBatchMappers) {
		  this.crashCartBatchMappers = crashCartBatchMappers; 
	 }
	  
	  public CrashCartBatchMapper addCrashCartBatchMapper(CrashCartBatchMapper crashCartBatchMapper) {
	  getCrashCartBatchMappers().add(crashCartBatchMapper);
	  crashCartBatchMapper.setCrashCartRequestDetail(this);  
	  return crashCartBatchMapper; 
	  }
	  
	  public CrashCartBatchMapper removeTCrashCartBatchMapper(CrashCartBatchMapper crashCartBatchMapper) {
	  getCrashCartBatchMappers().remove(crashCartBatchMapper);
	  crashCartBatchMapper.setCrashCartRequestDetail(null);
	  
	  return crashCartBatchMapper; 
	 }
	 

}
