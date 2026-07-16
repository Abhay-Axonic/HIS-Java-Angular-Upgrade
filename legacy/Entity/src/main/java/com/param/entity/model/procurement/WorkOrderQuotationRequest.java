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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the t_work_order_quotation_request database table.
 * 
 */
@Entity
@Table(name="t_work_order_quotation_request",schema="procurement")
public class WorkOrderQuotationRequest extends BaseEntity implements Serializable {
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

	@Column(name="approved_date")
	private Date approvedDate;
	
	@Column(name="cancel_note")
	private String cancelNote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_type_id")
	private ContractType contractType;

	private String remark;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "request_type_id")
	private RequestType requestType;

	@Column(name="send_vendor_selection")
	private Boolean sendVendorSelection;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="work_order_id")
	private WorkOrderRequest workOrderRequest;

	@Column(name="work_order_quotation_request_no")
	private String workOrderQRNo;
	
	@Column(name="ready_for_negotiation")
	private Boolean requestForNegotiation  = false;

	//bi-directional many-to-one association to TWorkOrderQuotationRequestDetail
	@OneToMany(fetch = FetchType.LAZY, mappedBy="WorkOrderQuotationRequest", cascade = CascadeType.ALL)
	private List<WorkOrderQuotationRequestDetail> workOrderQRDetailsList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="WorkOrderQR", cascade = CascadeType.ALL)
	private List<WoQuotationVendor> woQuotationVendor;

	public WorkOrderQuotationRequest() {
		super();
	}
	
	public WorkOrderQuotationRequest(Integer id) {
		super();
		this.id = id;
	}
	
	public WorkOrderQuotationRequest(Status approvalStatus, ContractType contractType, String remark,
			RequestType requestType, Boolean sendVendorSelection, Status status, Store store,
			WorkOrderRequest workOrderRequest, String workOrderQRNo) {
		super();
		this.approvalStatus = approvalStatus;
		this.contractType = contractType;
		this.remark = remark;
		this.requestType = requestType;
		this.sendVendorSelection = sendVendorSelection;
		this.status = status;
		this.store = store;
		this.workOrderRequest = workOrderRequest;
		this.workOrderQRNo = workOrderQRNo;
	}


	public void updateWorkOrderQuotationRequest(String remark, Boolean sendVendorSelection) {
		this.remark = remark;
		this.sendVendorSelection = sendVendorSelection;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	

	public List<WoQuotationVendor> getWoQuotationVendor() {
		return woQuotationVendor;
	}

	public void setWoQuotationVendor(List<WoQuotationVendor> woQuotationVendor) {
		this.woQuotationVendor = woQuotationVendor;
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

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
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

	public ContractType getContractType() {
		return contractType;
	}

	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
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

	public Boolean getSendVendorSelection() {
		return sendVendorSelection;
	}

	public void setSendVendorSelection(Boolean sendVendorSelection) {
		this.sendVendorSelection = sendVendorSelection;
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

	public WorkOrderRequest getWorkOrderRequest() {
		return workOrderRequest;
	}

	public void setWorkOrderRequest(WorkOrderRequest workOrderRequest) {
		this.workOrderRequest = workOrderRequest;
	}

	public String getWorkOrderQRNo() {
		return workOrderQRNo;
	}

	public void setWorkOrderQRNo(String workOrderQRNo) {
		this.workOrderQRNo = workOrderQRNo;
	}
	
	public Boolean getRequestForNegotiation() {
		return requestForNegotiation;
	}

	public void setRequestForNegotiation(Boolean requestForNegotiation) {
		this.requestForNegotiation = requestForNegotiation;
	}

	public List<WorkOrderQuotationRequestDetail> getWorkOrderQRDetailsList() {
		return workOrderQRDetailsList;
	}

	public void setWorkOrderQRDetailsList(List<WorkOrderQuotationRequestDetail> workOrderQRDetailsList) {
		this.workOrderQRDetailsList = workOrderQRDetailsList;
	}
	
	public WorkOrderQuotationRequestDetail addWorkOrderQRDetailList(WorkOrderQuotationRequestDetail workOrderQRDetail) {
		if (getWorkOrderQRDetailsList() == null)
			this.workOrderQRDetailsList = new ArrayList<WorkOrderQuotationRequestDetail>();

		getWorkOrderQRDetailsList().add(workOrderQRDetail);
		workOrderQRDetail.setWorkOrderQuotationRequest(this);

		return workOrderQRDetail;
	}

	public WorkOrderQuotationRequestDetail removeWorkOrderQuotationRequestDetail(WorkOrderQuotationRequestDetail workOrderQRDetail) {
		getWorkOrderQRDetailsList().remove(workOrderQRDetail);
		workOrderQRDetail.setWorkOrderQuotationRequest(this);

		return workOrderQRDetail;
	}

}