package com.param.procurement.workOrder.dto.workOrderCreation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.param.entity.model.master.User;
import com.param.global.dto.global.OtherChargesResponse;
import com.param.global.dto.global.TaxDropDown;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class WOCreationResponse {
	
	private Integer id;
	
	private String reqNo;
	
	private Date reqDate;
	
	private Integer storeId;
	
	private Integer contractTypeId;
	
	private Integer statusId;
	
	
	
	private String store;
	
	private String contractType;
	
	private String woAgainst;
	
	private Integer woAgainstId;
	
	private String approvalStatus;
	
	private String status;
	
	private Integer approvalStatusId;
	
	private String addedBy;
	
	private Integer addedById;
	
	private Integer vendorId;

	
	private String remarks;
	
	private String vendorName;
	
	private String vqrNo;
	
	private String wrNo;
	
	private Date addedDate;
	
	private Date qrDate;
	
	private Integer approvedById;
	
	private Integer requestTypeId;
	
	private Boolean sendVendorSelection;
	
	private Integer workOrderId;
	
	private Integer workOrderQRId;
	
	private String approvedBy;
	
	private String qrNo;
	
	private Double totalAmount;
	
	private Double totalTax ;
	
	private Double totalDiscount;
	
	private Double netAmount;
	
	private Date approvedDate;
	
	private Boolean requestForNegotiation;
	
	private String requestType;
	
	private List<WOCreationDetailResponse> items;
	
	protected List<WOCreationDetailResponse> POCreationDetailList;
    
    protected List<TaxDropDown> taxList;
    
    private String worNo;
	
	
	public Integer getAddedById() {
		return addedById;
	}

	public void setAddedById(Integer addedById) {
		this.addedById = addedById;
	}

	public Integer getWorkOrderQRId() {
		return workOrderQRId;
	}

	public void setWorkOrderQRId(Integer workOrderQRId) {
		this.workOrderQRId = workOrderQRId;
	}
	public Integer getContractTypeId() {
		return contractTypeId;
	}

	public void setContractTypeId(Integer contractTypeId) {
		this.contractTypeId = contractTypeId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getWrNo() {
		return wrNo;
	}

	public void setWrNo(String wrNo) {
		this.wrNo = wrNo;
	}
	public String getVqrNo() {
		return vqrNo;
	}

	public void setVqrNo(String vqrNo) {
		this.vqrNo = vqrNo;
	}

	

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public Integer getApprovedById() {
		return approvedById;
	}

	public void setApprovedById(Integer approvedById) {
		this.approvedById = approvedById;
	}

	public Integer getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(Integer requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public Boolean getSendVendorSelection() {
		return sendVendorSelection;
	}

	public void setSendVendorSelection(Boolean sendVendorSelection) {
		this.sendVendorSelection = sendVendorSelection;
	}

	public Integer getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(Integer workOrderId) {
		this.workOrderId = workOrderId;
	}

	
	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public Boolean getRequestForNegotiation() {
		return requestForNegotiation;
	}

	public void setRequestForNegotiation(Boolean requestForNegotiation) {
		this.requestForNegotiation = requestForNegotiation;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	

	
	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public List<WOCreationDetailResponse> getItems() {
		return items;
	}

	public void setItems(List<WOCreationDetailResponse> items) {
		this.items = items;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getReqNo() {
		return reqNo;
	}

	public void setReqNo(String reqNo) {
		this.reqNo = reqNo;
	}

	public Date getReqDate() {
		return reqDate;
	}

	public void setReqDate(Date reqDate) {
		this.reqDate = reqDate;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getWoAgainst() {
		return woAgainst;
	}

	public void setWoAgainst(String woAgainst) {
		this.woAgainst = woAgainst;
	}

	public Integer getWoAgainstId() {
		return woAgainstId;
	}

	public void setWoAgainstId(Integer woAgainstId) {
		this.woAgainstId = woAgainstId;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	
	
	public Date getQrDate() {
		return qrDate;
	}

	public void setQrDate(Date qrDate) {
		this.qrDate = qrDate;
	}

	public String getQrNo() {
		return qrNo;
	}

	public void setQrNo(String qrNo) {
		this.qrNo = qrNo;
	}
	
	

	public List<WOCreationDetailResponse> getPOCreationDetailList() {
		return POCreationDetailList;
	}

	public void setPOCreationDetailList(List<WOCreationDetailResponse> pOCreationDetailList) {
		POCreationDetailList = pOCreationDetailList;
	}

	public List<TaxDropDown> getTaxList() {
		return taxList;
	}

	public void setTaxList(List<TaxDropDown> taxList) {
		this.taxList = taxList;
	}

	
	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(Double totalTax) {
		this.totalTax = totalTax;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	
	public String getWorNo() {
		return worNo;
	}

	public void setWorNo(String worNo) {
		this.worNo = worNo;
	}

	public WOCreationResponse()
	{
		super();
	}

	public WOCreationResponse(Integer id, String reqNo, Date addedDate, Integer storeId, String storeName,
			String contractType, String reqType, Integer reqTypeId, String status, Integer approvalStatusId, String addedBy, String remark,
			String venderName) {
		this.id=id;
		this.reqNo=reqNo;
		this.reqDate=addedDate;
		this.storeId=storeId;
		this.store=storeName;
		this.contractType=contractType;
		this.woAgainst=reqType;
		this.woAgainstId=reqTypeId;
		this.status=status;
		this.approvalStatusId=approvalStatusId;
		this.addedBy=addedBy;
		this.remarks=remark;
		this.vendorName=venderName;
		
	}

	public WOCreationResponse(Integer id, String workOrderNo, Date addedDate, Integer storeId, String storeName,
			Integer contarcttypeId, String contractTypeCode, Integer approvalStatusId, Integer approvedBy, String remark, Integer reuestTypeId,
			Boolean sendVendorSelection, Integer statusId, Integer workOrderQId, String approvalStatus, String approvedByName, Date approvedOn,
			String requestType, String status, String addedBy, Boolean requestForNegotiation,String vqrNo) {
		this.id=id;
		this.qrNo=workOrderNo;
		this.qrDate=addedDate;
		this.storeId=storeId;
		this.store=storeName;
		this.contractTypeId=contarcttypeId;
		this.contractType=contractTypeCode;
		this.approvalStatusId=approvalStatusId;
		this.approvedById=approvedBy;
		this.remarks=remark;
		this.requestTypeId=reuestTypeId;
		this.sendVendorSelection=sendVendorSelection;
		this.statusId=statusId;
		this.workOrderQRId=workOrderQId;
		this.approvalStatus=approvalStatus;
		this.approvedBy=approvedByName;
		this.approvedDate=approvedOn;
		this.requestType=requestType;
		this.status=status;
		this.addedBy=addedBy;
		this.requestForNegotiation=requestForNegotiation;
		this.vqrNo=vqrNo;
		
		
		
	}

	public WOCreationResponse(Integer id, Integer storeId, String storeName, Integer contractTypeId, String contractTypeCode, Integer approvalStatusId,
			Integer approvedById, String requestNo, Date reuestDate, String approvalStatus, String approvedBy, Date approvedOn,
			Integer requestType, String status, String addedBy, Integer workOrderId) {
		
		this.id=id;
		this.storeId=storeId;
		this.store=storeName;
		this.contractTypeId=contractTypeId;
		this.contractType=contractTypeCode;
		this.approvalStatusId=approvalStatusId;
		this.approvedById=approvedById;
		this.reqDate=reuestDate;
		this.approvalStatus=approvalStatus;
		this.approvedBy=approvedBy;
		this.approvedDate=approvedOn;
		this.requestTypeId=requestType;
		this.status=status;
		this.addedBy=addedBy;
		this.workOrderId=workOrderId;
		this.wrNo=requestNo;
	}

	public WOCreationResponse(Integer id, String workOrderNo, Date addedDate, Integer storeId, String storeName,
			Integer contractTypeId, String contractTypeCode, Integer requestTypeID, String requestType, Integer statusId, String status, String approvalStatus,
			Integer approvalStatusId, Integer addedById, String addedByName, String approvedBy, Date approvedOn, Integer approvedById,
			 Integer workOrderQID, Boolean sendToVenderSelection, Boolean requestForNegotiation
			 ,Integer vendorId,String vendorName,Double totalAmount,Double netAmount,Double totalTax ,Double totalDiscount,String qrNo,String  remark) {
		this.id=id;
		this.wrNo=workOrderNo;
		this.storeId=storeId;
		this.store=storeName;
		this.contractTypeId=contractTypeId;
		this.contractType=contractTypeCode;
		this.requestTypeId=requestTypeID;
		this.requestType=requestType;
		this.statusId=statusId;
		this.status=status;
		this.approvalStatus=approvalStatus;
		this.approvalStatusId=approvalStatusId;
		this.addedBy=addedByName;
		this.addedById=addedById;
		this.approvedBy=approvedBy;
		this.approvedDate=approvedOn;
		this.workOrderQRId=workOrderQID;
		this.sendVendorSelection=sendToVenderSelection;
		this.requestForNegotiation=requestForNegotiation;
		this.vendorId=vendorId;
		this.vendorName=vendorName;
		this.totalAmount=totalAmount;
		this.totalTax=totalTax;
		this.totalDiscount=totalDiscount;
		this.netAmount=netAmount;
		this.addedDate=addedDate;
		this.qrNo=qrNo;
		this.remarks=remark;		
		
	}
	
	public WOCreationResponse(Integer id,  Date addedDate, Integer storeId, String storeName,
			Integer contractTypeId, String contractTypeCode, Integer requestTypeID, String requestType, Integer statusId, String status, String approvalStatus,
			Integer approvalStatusId, Integer addedById, String addedByName, String approvedBy, Date approvedOn, Integer approvedById
			,Integer vendorId,String vendorName,Double totalAmount,Double netAmount,Double totalTax ,Double totalDiscount,String wrNo,String remark) {
		this.id=id;
		this.storeId=storeId;
		this.store=storeName;
		this.contractTypeId=contractTypeId;
		this.contractType=contractTypeCode;
		this.requestTypeId=requestTypeID;
		this.requestType=requestType;
		this.statusId=statusId;
		this.status=status;
		this.approvalStatus=approvalStatus;
		this.approvalStatusId=approvalStatusId;
		this.addedBy=addedByName;
		this.addedById=addedById;
		this.approvedBy=approvedBy;
		this.approvedDate=approvedOn;
		this.vendorId=vendorId;
		this.vendorName=vendorName;
		this.totalAmount=totalAmount;
		this.totalTax=totalTax;
		this.totalDiscount=totalDiscount;
		this.netAmount=netAmount;
		this.addedDate=addedDate;
		this.wrNo=wrNo;
		this.remarks=remark;
		
	}
	public WOCreationResponse(Integer id, String workOrderNo, Date addedDate, Integer storeId, String storeName,
			Integer contractTypeId, String contractTypeCode, Integer requestTypeID, String requestType, Integer statusId, String status, String approvalStatus,
			Integer approvalStatusId, Integer addedById, String addedByName, String approvedBy, Date approvedOn, Integer approvedById,
			Integer workOrdreId,Integer vendorId,String vendorName,Double totalAmount,Double netAmount,Double totalTax ,Double totalDiscount,String worNo,String remark) {
		this.id=id;
		this.wrNo=workOrderNo;
		this.storeId=storeId;
		this.store=storeName;
		this.contractTypeId=contractTypeId;
		this.contractType=contractTypeCode;
		this.requestTypeId=requestTypeID;
		this.requestType=requestType;
		this.statusId=statusId;
		this.status=status;
		this.approvalStatus=approvalStatus;
		this.approvalStatusId=approvalStatusId;
		this.addedBy=addedByName;
		this.addedById=addedById;
		this.approvedBy=approvedBy;
		this.approvedDate=approvedOn;
		this.workOrderQRId=workOrdreId;
		this.vendorId=vendorId;
		this.vendorName=vendorName;
		this.totalAmount=totalAmount;
		this.totalTax=totalTax;
		this.totalDiscount=totalDiscount;
		this.netAmount=netAmount;
		this.addedDate=addedDate;
		this.worNo=worNo;
		this.remarks=remark;
		
	}

	public WOCreationResponse(String name) {
		this.vendorName=name;
	}

	public void addItems(WOCreationDetailResponse item) {
		if (this.items == null)
			this.items = new ArrayList<WOCreationDetailResponse>();

		this.items.add(item);
	}
	

}
