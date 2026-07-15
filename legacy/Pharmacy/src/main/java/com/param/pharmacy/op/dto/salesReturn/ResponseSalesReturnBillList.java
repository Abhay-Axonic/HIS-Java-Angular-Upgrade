package com.param.pharmacy.op.dto.salesReturn;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ResponseSalesReturnBillList {
	
	private Long id;
	
	private BigInteger saleId;
	
    private Date billDate;
    
    private String billNo;
    
    private String uhid;
    
    private String patientDetail;
    
    private Date visitDate;
    
    private String visitNo;
    
    private Double billAmount;
    
    private Integer patientId;
    
    private Integer admissionId;
    
    private Integer encounterId;
    
    private Date admissionDate;
    
    private Date encounterDate;
    
    private String admissionNumber;
    
    private String encounterNumber;
    private Integer saleTypeId;
    
    private Integer age;
    
    private String issueNo;

	public Date getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}

	public Date getEncounterDate() {
		return encounterDate;
	}

	public void setEncounterDate(Date encounterDate) {
		this.encounterDate = encounterDate;
	}

	public String getAdmissionNumber() {
		return admissionNumber;
	}

	public void setAdmissionNumber(String admissionNumber) {
		this.admissionNumber = admissionNumber;
	}

	public String getEncounterNumber() {
		return encounterNumber;
	}

	public void setEncounterNumber(String encounterNumber) {
		this.encounterNumber = encounterNumber;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
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

	private Integer tempPatId;
	
	private Timestamp patientDob;
	
	private String tempPatientDatail;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
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

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public Double getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}

	public ResponseSalesReturnBillList()
	{
		
	}
	
	public ResponseSalesReturnBillList(Long id, Date billDate, String billNo, String patientDetail,
			 Double billAmount) {
		super();
		this.id = id;
		this.billDate = billDate;
		this.billNo = billNo;
		this.patientDetail = patientDetail;
		this.billAmount = billAmount;
	}
	
	public ResponseSalesReturnBillList(Long id, Date billDate, String billNo, String uhid, String patientDetail,
			Date visitDate, String visitNo, Double billAmount) {
		super();
		this.id = id;
		this.billDate = billDate;
		this.billNo = billNo;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.visitDate = visitDate;
		this.visitNo = visitNo;
		this.billAmount = billAmount;
	}

	public Integer getSaleTypeId() {
		return saleTypeId;
	}

	public void setSaleTypeId(Integer saleTypeId) {
		this.saleTypeId = saleTypeId;
	}

	public BigInteger getSaleId() {
		return saleId;
	}

	public void setSaleId(BigInteger saleId) {
		this.saleId = saleId;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}
	
	
    
}