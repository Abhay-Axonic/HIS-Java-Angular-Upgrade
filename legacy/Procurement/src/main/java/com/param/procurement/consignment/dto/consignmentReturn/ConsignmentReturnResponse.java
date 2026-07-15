package com.param.procurement.consignment.dto.consignmentReturn;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.VisitType;
import com.param.procurement.consignment.dto.consignment.ConsignmentRequestItems;

@JsonInclude(Include.NON_NULL)	
public class ConsignmentReturnResponse extends ConsignmentReturnRequestItems 

{
	private Integer id;
	private String returnNo;
	private String issueNo;
	private Date issueDate;
	private Integer admissionId;
	private Integer wardId;
	private Integer bedId;
	private String itemName;
	private Integer itemId;
	private String itemCode;
	private String genericName;
	private String uom;
	private String uomUnit;
	private Integer quantityIssued;
	private String batchNo;
	private Integer patientId;
	private String patientName;
	private String patientDetail;
	private String doctorDetail;
	private Date expiry;
	private String returnQuantity;
	private Date returnDate;
	private Integer uhid;
	private String visitType;
	private String visitNo;
	private Date visitDate;
	private String store;
	private String wardName;
	private String bedNo;
	private String status;
	private String indentNo;
	private Date fromDate;
	private Date toDate;
	private Integer storeId;
	private Integer vendorId;
	private String vendorName;
	private Integer statusId;
	private String remark;
		
	private List<ConsignmentReturnRequestItems> congreturnrequestitems;
	
	private List<ConsignmentReturnResponseItems> congreturnresponseitems;
	
	public ConsignmentReturnResponse(Integer id, String returnNo, Date returnDate, Integer patientId,String patientDetail,
										String visitType, String visitNo, String store, String wardName, String bedNo,String status)
	{
			super();
			this.id=id;
			this.returnNo=returnNo;
			this.returnDate=returnDate;
			this.patientId=patientId;
			this.patientDetail=patientDetail;
			this.visitType=visitType;
			this.visitNo=visitNo;
			this.store=store;
			this.wardName=wardName;
			this.bedNo=bedNo;
			this.status=status;
		
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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<ConsignmentReturnRequestItems> getCongreturnrequestitems() {
		return congreturnrequestitems;
	}

	public void setCongreturnrequestitems(List<ConsignmentReturnRequestItems> congreturnrequestitems) {
		this.congreturnrequestitems = congreturnrequestitems;
	}

	public List<ConsignmentReturnResponseItems> getCongreturnresponseitems() {
		return congreturnresponseitems;
	}

	public void setCongreturnresponseitems(List<ConsignmentReturnResponseItems> congreturnresponseitems) {
		this.congreturnresponseitems = congreturnresponseitems;
	}

	public String getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public Integer getUhid() {
		return uhid;
	}

	public void setUhid(Integer uhid) {
		this.uhid = uhid;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(String indentNo) {
		this.indentNo = indentNo;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}

	public Integer getQuantityIssued() {
		return quantityIssued;
	}

	public void setQuantityIssued(Integer quantityIssued) {
		this.quantityIssued = quantityIssued;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public String getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(String returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientDetail() {
		return patientDetail;
	}

	public void setPatientDetail(String patientDetail) {
		this.patientDetail = patientDetail;
	}

	public String getDoctorDetail() {
		return doctorDetail;
	}

	public void setDoctorDetail(String doctorDetail) {
		this.doctorDetail = doctorDetail;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Integer getBedId() {
		return bedId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}

}
