package com.param.procurement.consignment.dto.consignment;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.service.global.model.CookieObjectDto;

@JsonInclude(Include.NON_NULL)
public class ConsignmentRequest extends CookieObjectDto{

	private Integer id;

	private Date fromDate;

	private Date toDate;

	private Integer storeId;

	private Integer vendorId;

	private String vendorName;

	private Integer inWardById;

	private String inWardBy;

	private Integer outWardById;

	private String outWardBy;

	private Integer statusId;

	private String inwardNo;

	private Date inwardDate;

	private String remark;

	private Double totalAmount;

	private Double totalDiscount;

	private Double totalNetAmount;

	private Double totalTaxAmount;

	private String dcNo;

	private Date dcDate;

	private Integer cancelReasonId;
	
	private String cancelNote;
	
	private Date canceledDate;

	private List<Integer> consInwId;

	private List<Integer> consOutwId;
	
	private List<ConsignmentRequestItems> items;

	public ConsignmentRequest() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
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

	public Date getInwardDate() {
		return inwardDate;
	}

	public void setInwardDate(Date inwardDate) {
		this.inwardDate = inwardDate;
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

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Double getTotalNetAmount() {
		return totalNetAmount;
	}

	public void setTotalNetAmount(Double totalNetAmount) {
		this.totalNetAmount = totalNetAmount;
	}

	public Double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(Double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
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

	public List<Integer> getConsInwId() {
		return consInwId;
	}

	public void setConsInwId(List<Integer> consInwId) {
		this.consInwId = consInwId;
	}

	public List<ConsignmentRequestItems> getItems() {
		return items;
	}

	public void setItems(List<ConsignmentRequestItems> items) {
		this.items = items;
	}

	public List<Integer> getConsOutwId() {
		return consOutwId;
	}

	public void setConsOutwId(List<Integer> consOutwId) {
		this.consOutwId = consOutwId;
	}

	public Date getCanceledDate() {
		return canceledDate;
	}

	public void setCanceledDate(Date canceledDate) {
		this.canceledDate = canceledDate;
	}

}
