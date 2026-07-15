package com.param.pharmacy.op.dto.salesReturn;

import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ResponseReturnListSearch {
	
	private Integer id;
	
	private Date returnDate;
	
	private String returnNo;
	
	private String uhid;
	
	private String patientDetail;
	private String tempPatientDatails;
	
	private String tempUhid;
	
	public String getTempUhid() {
		return tempUhid;
	}

	public void setTempUhid(String tempUhid) {
		this.tempUhid = tempUhid;
	}

	public String getTempPatientDatails() {
		return tempPatientDatails;
	}

	public void setTempPatientDatails(String tempPatientDatails) {
		this.tempPatientDatails = tempPatientDatails;
	}

	private String billNo;
	
	private Date billDate;
	 private String patientName;
	    private String ageString;
	    private String genderCode;
	    private Integer genderId;
		private String imagePath;
		
		private String paymentRefundVoucherNo;
		
	public ResponseReturnListSearch() {
		super();
		// TODO Auto-generated constructor stub
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

	private Double returnAmount;
	
	private Integer statusId;
	   
	private String status;
	
	private Integer patientId;
	
	private Integer tempPatId;
	
	private Timestamp patientDob;
	
	private String tempPatientDatail;
	
	private Integer storeId;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
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

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public Double getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(Double returnAmount) {
		this.returnAmount = returnAmount;
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

	public ResponseReturnListSearch(Integer id, Date returnDate, String returnNo, String uhid, String patientDetail,
			String billNo, Date billDate, Double returnAmount) {
		super();
		this.id = id;
		this.returnDate = returnDate;
		this.returnNo = returnNo;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.billNo = billNo;
		this.billDate = billDate;
		this.returnAmount = returnAmount;
	}
	
	public ResponseReturnListSearch(Integer id, Date returnDate, String returnNo, String uhid, String patientDetail,
			String billNo, Date billDate, Double returnAmount,Double rate,Double returnPaidAmount,Double returnBalanceAmount,String status,String createdBy ) {
		super();
		this.id = id;
		this.returnDate = returnDate;
		this.returnNo = returnNo;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.billNo = billNo;
		this.billDate = billDate;
		this.returnAmount = returnAmount;
		this.rate = rate;
		this.returnPaidAmount = returnPaidAmount;
		this.returnBalanceAmount = returnBalanceAmount;
		this.status = status;	
		this.createdBy = createdBy;
	}
	
	public ResponseReturnListSearch(Integer id, Date returnDate, String returnNo, String uhid, String patientDetail,
			String billNo, Date billDate, Double returnAmount, Integer statusId, String status) {
		
		this(id, returnDate, returnNo, uhid, patientDetail,
		 billNo, billDate, returnAmount);
		
		this.statusId = statusId;
		this.status = status;
	}
	
	private Double rate;
	
	private Double returnPaidAmount;
	
	private Double returnBalanceAmount;
	

	public Double getReturnBalanceAmount() {
		return returnBalanceAmount;
	}

	public void setReturnBalanceAmount(Double returnBalanceAmount) {
		this.returnBalanceAmount = returnBalanceAmount;
	}

	private String createdBy;	

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
 
	public Double getReturnPaidAmount() {
		return returnPaidAmount;
	}

	public void setReturnPaidAmount(Double returnPaidAmount) {
		this.returnPaidAmount = returnPaidAmount;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getAgeString() {
		return ageString;
	}

	public void setAgeString(String ageString) {
		this.ageString = ageString;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getPaymentRefundVoucherNo() {
		return paymentRefundVoucherNo;
	}

	public void setPaymentRefundVoucherNo(String paymentRefundVoucherNo) {
		this.paymentRefundVoucherNo = paymentRefundVoucherNo;
	}
	
}