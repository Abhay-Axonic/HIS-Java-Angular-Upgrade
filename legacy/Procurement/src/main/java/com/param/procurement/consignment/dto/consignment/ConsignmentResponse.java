package com.param.procurement.consignment.dto.consignment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ConsignmentResponse {

	private Integer id;

	protected Integer storeId;

	protected String storeName;

	protected Integer vendorId;

	protected String vendorName;

	protected Integer inWardById;

	protected String inWardBy;

	protected Integer outWardById;

	protected String outWardBy;

	protected Integer statusId;

	protected String status;

	protected String inwardNo;

	protected Date inwardDate;
	
	protected String outwardNo;

	protected Date outwardDate;

	protected String remark;

	protected Double totalAmount;
	
	protected Double totalTaxAmount;
	
	protected Double totalNetAmount;

	protected String dcNo;

	protected Date dcDate;

	protected Integer cancelReasonId;

	protected String cancelNote;

	protected List<ConsignmentRequestItems> items;

	protected List<ConsignmentResponseItems> item;

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Integer getInWardById() {
		return inWardById;
	}

	public void setInWardById(Integer inWardById) {
		this.inWardById = inWardById;
	}

	public String getInWardBy() {
		return inWardBy;
	}

	public void setInWardBy(String inWardBy) {
		this.inWardBy = inWardBy;
	}

	public Integer getOutWardById() {
		return outWardById;
	}

	public void setOutWardById(Integer outWardById) {
		this.outWardById = outWardById;
	}

	public String getOutWardBy() {
		return outWardBy;
	}

	public void setOutWardBy(String outWardBy) {
		this.outWardBy = outWardBy;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getInwardNo() {
		return inwardNo;
	}

	public void setInwardNo(String inwardNo) {
		this.inwardNo = inwardNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getDcNo() {
		return dcNo;
	}

	public void setDcNo(String dcNo) {
		this.dcNo = dcNo;
	}

	public Date getDcDate() {
		return dcDate;
	}

	public void setDcDate(Date dcDate) {
		this.dcDate = dcDate;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public List<ConsignmentRequestItems> getItems() {
		return items;
	}

	public void setItems(List<ConsignmentRequestItems> items) {
		this.items = items;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getInwardDate() {
		return inwardDate;
	}

	public void setInwardDate(Date inwardDate) {
		this.inwardDate = inwardDate;
	}

	public List<ConsignmentResponseItems> getItem() {
		return item;
	}

	public void setItem(List<ConsignmentResponseItems> item) {
		this.item = item;
	}

	public ConsignmentResponse() {
		super();
	}

	public ConsignmentResponse(Integer id, Integer storeId, String storeName, Integer vendorId, String vendorName,
			Integer inWardById, String inWardBy, Integer statusId, String status, String inwardNo, Date inwardDate,
			String remark) {
		super();
		this.id = id;
		this.storeId = storeId;
		this.storeName = storeName;
		this.vendorId = vendorId;
		this.vendorName = vendorName;
		this.inWardById = inWardById;
		this.inWardBy = inWardBy;
		this.statusId = statusId;
		this.status = status;
		this.inwardNo = inwardNo;
		this.inwardDate = inwardDate;
		this.remark = remark;
	}

	public ConsignmentResponse(Integer id, Integer storeId, String storeName, Integer vendorId, String vendorName,
			Integer inWardById, String inWardBy, Integer statusId, String status, String inwardNo, Date inwardDate,
			String remark, String dcNo, Date dcDate) {
		this(id, storeId, storeName, vendorId, vendorName, inWardById, inWardBy, statusId, status, inwardNo, inwardDate,
				remark);
		this.dcNo = dcNo;
		this.dcDate = dcDate;
	}

	public ConsignmentResponse(Integer id,String outwardNo,Date outwardDate,Integer storeId,
			String storeName,String vendorName,Integer vendorId,String remark,String outWardBy,
			Integer outWardById,String status,Integer statusId)
	{
		super();
		this.id=id;
		this.outwardNo=outwardNo;
		this.outwardDate=outwardDate;
		this.storeId=storeId;
		this.storeName=storeName;
		this.vendorName=vendorName;
		this.vendorId=vendorId;
		this.remark=remark;
		this.outWardBy=outWardBy;
		this.outWardById=outWardById;
		this.status=status;
		this.statusId=statusId;
		
	
	}

	public ConsignmentResponse(Integer id,String outwardNo, Date outwardDate, Date dcDate, String dcNo, String storeName,
			Integer storeId, String vendorName, Integer vendorId,String remark, String outWardBy, Integer outWardById, String status,
			Integer statusId, double totalAmount, double totalNetAmount, double totalTaxAmount)
	{
		super();
		this.id=id;
		this.outwardNo=outwardNo;
		this.outwardDate=outwardDate;
		this.dcDate=dcDate;
		this.dcNo = dcNo;
		this.storeId=storeId;
		this.storeName=storeName;
		this.vendorName=vendorName;
		this.vendorId=vendorId;
		this.remark=remark;
		this.outWardBy=outWardBy;
		this.outWardById=outWardById;
		this.status=status;
		this.statusId=statusId;
		this.totalAmount=totalAmount;
		this.totalNetAmount=totalNetAmount;
		this.totalTaxAmount=totalTaxAmount;
		
	}

	public String getOutwardNo() {
		return outwardNo;
	}

	public void setOutwardNo(String outwardNo) {
		this.outwardNo = outwardNo;
	}

	public Date getOutwardDate() {
		return outwardDate;
	}

	public void setOutwardDate(Date outwardDate) {
		this.outwardDate = outwardDate;
	}

	public Double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(Double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public Double getTotalNetAmount() {
		return totalNetAmount;
	}

	public void setTotalNetAmount(Double totalNetAmount) {
		this.totalNetAmount = totalNetAmount;
	}

	public void addItems(ConsignmentResponseItems item) {
			if (this.item == null)
				this.item = new ArrayList<ConsignmentResponseItems>();

			this.item.add(item);
		}
		
	}


