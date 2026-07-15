package com.param.entity.model.procurement;

import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the t_work_order database table.
 * 
 */
@Entity
@Table(name="t_work_order", schema = "procurement")
public class WorkOrderRequest extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approval_status_id")
	private Status approvalStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private User approvedBy;

	@Column(name="approved_on")
	private Date approvedOn;

	@Column(name="cancel_note")
	private String cancelNote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;

	private String remark;

	@Column(name="request_no")
	private String requestNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_type_id")
	private ContractType contractType;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workOrder", cascade = CascadeType.ALL)
	private List<WorkOrderRequestDetail> workOrderDetailList;

	public WorkOrderRequest() {
		super();
	}

	
	public WorkOrderRequest(Status approvalStatus, String remark, String requestNo,
			Status status, Store store, ContractType contractType) {
		super();
		this.approvalStatus = approvalStatus;
		this.remark = remark;
		this.requestNo = requestNo;
		this.status = status;
		this.store = store;
		this.contractType = contractType;
	}
	
	public WorkOrderRequest(Integer id) {
		this.id = id;
	}


	public void updateWorkOrderRequest(String remark) {
		this.remark = remark;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Status getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Status approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public CancelReason getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(CancelReason cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}
	
	public ContractType getContractType() {
		return contractType;
	}

	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}

	public List<WorkOrderRequestDetail> getWorkOrderDetailList() {
		return workOrderDetailList;
	}

	public void setWorkOrderDetailList(List<WorkOrderRequestDetail> workOrderDetailList) {
		this.workOrderDetailList = workOrderDetailList;
	}
	
	public WorkOrderRequestDetail addWorkOrderDetailList(WorkOrderRequestDetail workOrderDetail) {
		if (getWorkOrderDetailList() == null)
			this.workOrderDetailList = new ArrayList<WorkOrderRequestDetail>();

		getWorkOrderDetailList().add(workOrderDetail);
		workOrderDetail.setWorkOrder(this);

		return workOrderDetail;
	}

	public WorkOrderRequestDetail removeGoodReceiptNoteDetail(WorkOrderRequestDetail workOrderDetail) {
		getWorkOrderDetailList().remove(workOrderDetail);
		workOrderDetail.setWorkOrder(this);

		return workOrderDetail;
	}
}