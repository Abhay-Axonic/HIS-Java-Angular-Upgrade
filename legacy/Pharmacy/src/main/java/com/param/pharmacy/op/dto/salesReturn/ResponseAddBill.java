package com.param.pharmacy.op.dto.salesReturn;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ResponseAddBill {
	
	private Long id;
	
    private String billNo;
    
    private String uhid;
    
    private String patientDetail;
    private String  tempPatientDatails;
   
    public String getTempPatientDatails() {
		return tempPatientDatails;
	}

	public void setTempPatientDatails(String tempPatientDatails) {
		this.tempPatientDatails = tempPatientDatails;
	}

	private Integer saleTypeId;
    
    private String saleType;
    
    private Double taxAmount;

    private List<ItemDetails> item;
    
    private Integer patientId;
    
    private Integer tempPatId;
	
	private Timestamp patientDob;
	
	private String tempPatientDatail;
	
	private Integer storeId;
	
	private Double  selfPaidAmount;
    
	
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
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

	public Integer getSaleTypeId() {
		return saleTypeId;
	}

	public void setSaleTypeId(Integer saleTypeId) {
		this.saleTypeId = saleTypeId;
	}

	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	public List<ItemDetails> getItem() {
		return item;
	}

	public void setItem(List<ItemDetails> item) {
		this.item = item;
	}
	
	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public ResponseAddBill()
	{
		
	}
	
	public ResponseAddBill(Long id, String billNo, String patientDetail, Integer saleTypeId,
			String saleType, List<ItemDetails> item) {
		super();
		this.id = id;
		this.billNo = billNo;
		this.patientDetail = patientDetail;
		this.saleTypeId = saleTypeId;
		this.saleType = saleType;
		this.item = item;
	}
	
	public ResponseAddBill(Long id, String billNo, String uhid, String patientDetail, Integer saleTypeId,
			String saleType, List<ItemDetails> item) {
		super();
		this.id = id;
		this.billNo = billNo;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.saleTypeId = saleTypeId;
		this.saleType = saleType;
		this.item = item;
	}
	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Double getSelfPaidAmount() {
		return selfPaidAmount;
	}

	public void setSelfPaidAmount(Double selfPaidAmount) {
		this.selfPaidAmount = selfPaidAmount;
	}

    
}