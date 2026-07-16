package com.param.entity.model.procurement;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.RequestType;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the t_work_order_creation database table.
 * 
 */
@Entity
@Table(name="t_work_order_creation", schema = "procurement")
public class WorkOrderCreation extends BaseEntity implements Serializable {
	@Serial
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

	@Column(name="net_amount")
	private Double netAmount;

	private String remark;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "request_type_id", nullable = false)
	private RequestType requestType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_type_id")
	private ContractType contractType;

	@Column(name="total_amount")
	private Double totalAmount;

	@Column(name="total_discount")
	private Double totalDiscount;

	@Column(name="total_tax")
	private Double totalTax;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id")
	private Vendor vendor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="work_order_id")
	private WorkOrderRequest workOrder;

	@Column(name="work_order_no")
	private String workOrderNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="work_order_quotation_id")
	private WorkOrderQuotationRequest workOrderQuotation;
	
	// bi-directional many-to-one association to TWoQuotationSendToVendorDetail
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workOrderCreation", cascade = CascadeType.ALL)
	private List<WorkOrderCreationDetail> workOrderCreationDetail;

	@Column(name="work_order_completion")
	private boolean workOrderCompletion;
	
	public WorkOrderCreation() {
		super();
	}
	
	
	public WorkOrderCreation(Status approvalStatus, Double netAmount, String remark, RequestType requestType,
			Status status, Store store, ContractType contractType, Double totalAmount, Double totalDiscount,
			Double totalTax, Vendor vendor, WorkOrderRequest workOrder, String workOrderNo,
			WorkOrderQuotationRequest workOrderQuotation) {
		super();
		this.approvalStatus = approvalStatus;
		this.netAmount = netAmount;
		this.remark = remark;
		this.requestType = requestType;
		this.status = status;
		this.store = store;
		this.contractType = contractType;
		this.totalAmount = totalAmount;
		this.totalDiscount = totalDiscount;
		this.totalTax = totalTax;
		this.vendor = vendor;
		this.workOrder = workOrder;
		this.workOrderNo = workOrderNo;
		this.workOrderQuotation = workOrderQuotation;
	}
	
	
	//for work order by QR
	public WorkOrderCreation(Store store, ContractType contractType, Vendor vendor, WorkOrderQuotationRequest woReqOrWoQuotId,
			RequestType woAgainstId, Double total_amount, Double totalDiscount, Double totalTax, Double netAmount,
			Status statusId, Status approvalStatusId, String remark,String workOrderQRNo) {
		
		this.store = store;
		this.contractType = contractType;
		this.vendor = vendor;
		this.workOrderQuotation = woReqOrWoQuotId;
		this.requestType = woAgainstId;
		this.totalAmount = total_amount;
		this.totalDiscount = totalDiscount;
		this.totalTax = totalTax;
		this.netAmount = netAmount;
		this.status = statusId;
		this.approvalStatus = approvalStatusId;
		this.remark = remark;
		this.workOrderNo = workOrderQRNo;
	}
	
	//for work order by WR
		public WorkOrderCreation(Store store, ContractType contractType, Vendor vendor, WorkOrderRequest woReqOrWoQuotId,
				RequestType woAgainstId, Double total_amount, Double totalDiscount, Double totalTax, Double netAmount,
				Status statusId, Status approvalStatusId, String remark,String workOrderQRNo) {
			
			this.store = store;
			this.contractType = contractType;
			this.vendor = vendor;
			this.workOrder = woReqOrWoQuotId;
			this.requestType = woAgainstId;
			this.totalAmount = total_amount;
			this.totalDiscount = totalDiscount;
			this.totalTax = totalTax;
			this.netAmount = netAmount;
			this.status = statusId;
			this.approvalStatus = approvalStatusId;
			this.remark = remark;
			this.workOrderNo = workOrderQRNo;
		}
		
		//for work order by Direct Request
				public WorkOrderCreation(Store store, ContractType contractType, Vendor vendor, 
						RequestType woAgainstId, Double total_amount, Double totalDiscount, Double totalTax, Double netAmount,
						Status statusId, Status approvalStatusId, String remark ,String WOCreationNo) {
					
					this.store = store;
					this.contractType = contractType;
					this.vendor = vendor;
					this.requestType = woAgainstId;
					this.totalAmount = total_amount;
					this.totalDiscount = totalDiscount;
					this.totalTax = totalTax;
					this.netAmount = netAmount;
					this.status = statusId;
					this.approvalStatus = approvalStatusId;
					this.remark = remark;
					this.workOrderNo=WOCreationNo;
				}


	public void updateWorkOrderCreation(Double netAmount, String remark, Double totalAmount, Double totalDiscount,
			Double totalTax) {
		this.netAmount = netAmount;
		this.remark = remark;
		this.totalAmount = totalAmount;
		this.totalDiscount = totalDiscount;
		this.totalTax = totalTax;
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

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
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

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Double getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(Double totalTax) {
		this.totalTax = totalTax;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	
	public ContractType getContractType() {
		return contractType;
	}

	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}

	public WorkOrderRequest getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(WorkOrderRequest workOrder) {
		this.workOrder = workOrder;
	}

	public String getWorkOrderNo() {
		return workOrderNo;
	}

	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}

	public WorkOrderQuotationRequest getWorkOrderQuotation() {
		return workOrderQuotation;
	}

	public void setWorkOrderQuotation(WorkOrderQuotationRequest workOrderQuotation) {
		this.workOrderQuotation = workOrderQuotation;
	}

	public List<WorkOrderCreationDetail> getWorkOrderCreationDetail() {
		return workOrderCreationDetail;
	}

	public void setWorkOrderCreationDetail(List<WorkOrderCreationDetail> workOrderCreationDetail) {
		this.workOrderCreationDetail = workOrderCreationDetail;
	}

	public WorkOrderCreationDetail addWorkOrderCreationDetailList(WorkOrderCreationDetail workOrderCreationDetail) {
		if (getWorkOrderCreationDetail() == null)
			this.workOrderCreationDetail = new ArrayList<WorkOrderCreationDetail>();

		getWorkOrderCreationDetail().add(workOrderCreationDetail);
		workOrderCreationDetail.setWorkOrderCreation(this);

		return workOrderCreationDetail;
	}


	public boolean getWorkOrderCompletion() {
		return workOrderCompletion;
	}


	public void setWorkOrderCompletion(boolean workOrderCompletion) {
		this.workOrderCompletion = workOrderCompletion;
	}
}