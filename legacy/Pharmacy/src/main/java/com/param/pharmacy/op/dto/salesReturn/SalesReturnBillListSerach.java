package com.param.pharmacy.op.dto.salesReturn;

import java.util.Date;

import com.param.service.global.model.CookieObjectDto;

public class SalesReturnBillListSerach extends CookieObjectDto {
	
	private Date fromDate;
	
	private Date  toDate;
	
	private String  patientName;
	
	private Integer patientId;
	private Integer storeId;
	
	
	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	private String  uhid;
	
	private Integer saleTypeId;

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

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
	}

	public Integer getSaleTypeId() {
		return saleTypeId;
	}

	public void setSaleTypeId(Integer saleTypeId) {
		this.saleTypeId = saleTypeId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	/*public SalesReturnBillListSerach(String fromDate, String toDate, String patientName, String uhid, Integer saleTypeId) {
		super();
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.patientName = patientName;
		this.uhid = uhid;
		this.saleTypeId = saleTypeId;
	}*/

}