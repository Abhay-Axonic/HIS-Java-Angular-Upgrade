package com.param.entity.model.procurement;

import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;

import java.sql.Timestamp;
import java.util.Date;


/**
 * The persistent class for the t_work_order_detail database table.
 * 
 */
@Entity
@Table(name="t_work_order_detail", schema = "procurement")
public class WorkOrderRequestDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private User approvedBy;
	
	@Column(name = "approved_on")
	private Date approvedDate;


	@Column(name="approved_quantity")
	private Integer approvedQuantity;
	
	@Column(name="reject_quantity")
	private Integer rejectQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "current_vendor_id")
	private Vendor currentVendor;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reject_reason_id")
	private RejectReason rejectReason;
	
	@Column(name = "reject_note", length = 100)
	private String rejectNote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;	

	private Integer quantity;

	private String remark;

	private String specification;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id")
	private Vendor vendor;
	
	//bi-directional many-to-one association to TWorkOrder
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="work_order_id")
	private WorkOrderRequest workOrder;
	
	@Column(name="send_work_order_creation")
	private Boolean sendWorkOrderCreation = false;
	
	@Column(name="send_quotation_request")
	private Boolean sendQuotationRequest = false;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id")
	private Batch batch;

	public WorkOrderRequestDetail() {
		super();
	}
	
	public WorkOrderRequestDetail(Vendor currentVendor, Item item, Integer quantity, String remark,
			String specification, Status status, Vendor vendor, Batch batch) {
		super();
		this.currentVendor = currentVendor;
		this.item = item;
		this.quantity = quantity;
		this.remark = remark;
		this.specification = specification;
		this.status = status;
		this.vendor = vendor;
		this.batch = batch;
	}
	
	public WorkOrderRequestDetail(Integer id) {
		this.id = id;
	}

	public void  updateWorkOrderRequestDetail(Integer quantity, String remark,
			String specification,Vendor currentVendor,Batch batch) {
		this.quantity = quantity;
		this.remark = remark;
		this.specification = specification;
		this.currentVendor = currentVendor;
		this.batch = batch;
	}

	public void approveWorkOrderRequestDetail(Integer approvedQuantity, User approvedBy, Date approvedDate, String rejectNote,
			Integer rejectQuantity,RejectReason rejectReason, Status status, Date updatedDate,
			User updatedBy,Boolean sendWorkOrderCreation,Boolean sendQuotationRequest) {
		this.approvedQuantity = approvedQuantity;
		this.approvedBy = approvedBy;
		this.approvedDate = approvedDate;
		this.rejectNote = rejectNote;
		this.rejectQuantity = rejectQuantity;
		this.rejectReason = rejectReason;
		this.status = status;
		this.updatedDate = updatedDate;
		this.updatedBy = updatedBy;
		this.sendWorkOrderCreation = sendWorkOrderCreation;
		this.sendQuotationRequest = sendQuotationRequest;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}

	public Vendor getCurrentVendor() {
		return currentVendor;
	}

	public void setCurrentVendor(Vendor currentVendor) {
		this.currentVendor = currentVendor;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public WorkOrderRequest getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(WorkOrderRequest workOrder) {
		this.workOrder = workOrder;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public RejectReason getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(RejectReason rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public Boolean getSendWorkOrderCreation() {
		return sendWorkOrderCreation;
	}

	public void setSendWorkOrderCreation(Boolean sendWorkOrderCreation) {
		this.sendWorkOrderCreation = sendWorkOrderCreation;
	}

	public Boolean getSendQuotationRequest() {
		return sendQuotationRequest;
	}

	public void setSendQuotationRequest(Boolean sendQuotationRequest) {
		this.sendQuotationRequest = sendQuotationRequest;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

}