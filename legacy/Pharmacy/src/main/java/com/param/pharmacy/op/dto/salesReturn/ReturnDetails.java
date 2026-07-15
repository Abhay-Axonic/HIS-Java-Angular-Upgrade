package com.param.pharmacy.op.dto.salesReturn;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ReturnDetails {
	
	private Integer id;
	
	private String uhid;
	
	private String patientDetail;
	
	private Date dob;
	
	private Date retunDate;
	
	private String returnNo;
	
	private Integer storeId;
	
	private String storeName;
	
	private Double totalRefundAmount;
	
	private Integer statusId;
	   
    private String status;
	
	private List<ReturnItem> item;
	
	private Integer patientId;
	
	private Integer tempPatId;
	
	private Timestamp patientDob;
	
	private String tempPatientDatail;
	
	private Double totalreturnPaidAmount;

	private Integer genderId;
	
	private Integer tempGenderId;
	
	private String imagePath;
	private String tempPatientDatail1;
	private String tempUhid;
	
	public String getTempUhid() {
		return tempUhid;
	}

	public void setTempUhid(String tempUhid) {
		this.tempUhid = tempUhid;
	}

	public String getTempPatientDatail1() {
		return tempPatientDatail1;
	}

	public void setTempPatientDatail1(String tempPatientDatail1) {
		this.tempPatientDatail1 = tempPatientDatail1;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getTempPatId() {
		return tempPatId;
	}

	public void setTempPatId(Integer tempPatId) {
		this.tempPatId = tempPatId;
	}

	public Timestamp getPatientDob() {
		return patientDob;
	}

	public void setPatientDob(Timestamp patientDob) {
		this.patientDob = patientDob;
	}

	public String getTempPatientDatail() {
		return tempPatientDatail;
	}

	public void setTempPatientDatail(String tempPatientDatail) {
		this.tempPatientDatail = tempPatientDatail;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
	}

	public String getPatientDetail() {
		return patientDetail;
	}

	public void setPatientDetail(String patientDetail) {
		this.patientDetail = patientDetail;
	}

	public Date getRetunDate() {
		return retunDate;
	}

	public void setRetunDate(Date retunDate) {
		this.retunDate = retunDate;
	}

	public String getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
	}

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

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Double getTotalRefundAmount() {
		return totalRefundAmount;
	}

	public void setTotalRefundAmount(Double totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}

	public List<ReturnItem> getItem() {
		return item;
	}

	public void setItem(List<ReturnItem> item) {
		this.item = item;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ReturnDetails()
	{
		
	}
	
	public ReturnDetails(Integer id, String patientDetail, Date retunDate, String returnNo,
			Integer storeId, String storeName, Double totalRefundAmount, List<ReturnItem> item) {
		super();
		this.id = id;
		this.patientDetail = patientDetail;
		this.retunDate = retunDate;
		this.returnNo = returnNo;
		this.storeId = storeId;
		this.storeName = storeName;
		this.totalRefundAmount = totalRefundAmount;
		this.item = item;
	} 
	
	public ReturnDetails(Integer id, String patientDetail, Date retunDate, String returnNo,
			Integer storeId, String storeName, Double totalRefundAmount, List<ReturnItem> item, Integer statusId, String status ) {
		
		this(id, patientDetail, retunDate, returnNo,
			storeId, storeName, totalRefundAmount, item);
		this.statusId = statusId;
		this.status = status;
	}
	
	public ReturnDetails(Integer id, String uhid, String patientDetail, Date retunDate, String returnNo,
			Integer storeId, String storeName, Double totalRefundAmount, List<ReturnItem> item) {
		super();
		this.id = id;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.retunDate = retunDate;
		this.returnNo = returnNo;
		this.storeId = storeId;
		this.storeName = storeName;
		this.totalRefundAmount = totalRefundAmount;
		this.item = item;
	}
	
	public ReturnDetails(Integer id, String uhid, String patientDetail, Date dob, Date retunDate, String returnNo,
			Integer storeId, String storeName, Double totalRefundAmount, List<ReturnItem> item) {
		super();
		this.id = id;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.dob = dob;
		this.retunDate = retunDate;
		this.returnNo = returnNo;
		this.storeId = storeId;
		this.storeName = storeName;
		this.totalRefundAmount = totalRefundAmount;
		this.item = item;
	}
	
	public ReturnDetails(Integer id, String uhid, String patientDetail, Date dob, Date retunDate, String returnNo,
			Integer storeId, String storeName, Double totalRefundAmount, List<ReturnItem> item, Integer statusId, String status ) {
		
		this(id, uhid, patientDetail, dob, retunDate, returnNo,
			storeId, storeName, totalRefundAmount, item);
		
		this.statusId = statusId;
		this.status = status;
	}
	

	public Double getTotalreturnPaidAmount() {
		return totalreturnPaidAmount;
	}

	public void setTotalreturnPaidAmount(Double totalreturnPaidAmount) {
		this.totalreturnPaidAmount = totalreturnPaidAmount;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public Integer getTempGenderId() {
		return tempGenderId;
	}

	public void setTempGenderId(Integer tempGenderId) {
		this.tempGenderId = tempGenderId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

}

