package com.param.billing.dto;

import java.util.List;

import com.param.billing.global.transaction.config.dto.TCompanyInvoiceServiceDetailsDto;

public class CompanyInvoiceDetailsResDto {
	private Integer companyInvoiceId;
	private Integer companyInvoiceDetailId;
	private String invoiceNumber;
	private String billDateTime;
	private String billNumber;
	private String uhidNumber;
	private Double totalBillAmount;
	private Double netAmount;
	private Double outstanding;
	private Integer billPayeeId;
	private String patientName;
	private Integer billId;
	private Integer saleId;
	private Integer invoiceQueryId;
	private Double billOutStanging;
	private String visitNumber;
	private Integer patientId;
	private String doa;
	private String pdd;
	private String fatherName;
	private Integer visitTypeId;
	private String address;
	private Integer admissionId;
	private String visitDate;
	private Integer encounterId;
	private String doctorName;
	private Integer id;
	private Integer organisationId;
	private Integer unitId;
	private Double crNetAmount;
	private List<TCompanyInvoiceServiceDetailsDto> tCompanyInvoiceServiceDetailsDto;
	private List<TCompanyInvoiceServiceDetailsDto> tCompanyInvoiceServiceDetailsDtoOld;
	private String claimNumber;
	public Double getCrNetAmount() {
		return crNetAmount;
	}

	public void setCrNetAmount(Double crNetAmount) {
		this.crNetAmount = crNetAmount;
	}

	public List<TCompanyInvoiceServiceDetailsDto> gettCompanyInvoiceServiceDetailsDtoOld() {
		return tCompanyInvoiceServiceDetailsDtoOld;
	}

	public void settCompanyInvoiceServiceDetailsDtoOld(
			List<TCompanyInvoiceServiceDetailsDto> tCompanyInvoiceServiceDetailsDtoOld) {
		this.tCompanyInvoiceServiceDetailsDtoOld = tCompanyInvoiceServiceDetailsDtoOld;
	}

	public List<TCompanyInvoiceServiceDetailsDto> gettCompanyInvoiceServiceDetailsDto() {
		return tCompanyInvoiceServiceDetailsDto;
	}

	public void settCompanyInvoiceServiceDetailsDto(
			List<TCompanyInvoiceServiceDetailsDto> tCompanyInvoiceServiceDetailsDto) {
		this.tCompanyInvoiceServiceDetailsDto = tCompanyInvoiceServiceDetailsDto;
	}

	public Double getBillOutStanging() {
		return billOutStanging;
	}

	public void setBillOutStanging(Double billOutStanging) {
		this.billOutStanging = billOutStanging;
	}

	public Integer getInvoiceQueryId() {
		return invoiceQueryId;
	}

	public void setInvoiceQueryId(Integer invoiceQueryId) {
		this.invoiceQueryId = invoiceQueryId;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Integer getCompanyInvoiceId() {
		return companyInvoiceId;
	}

	public void setCompanyInvoiceId(Integer companyInvoiceId) {
		this.companyInvoiceId = companyInvoiceId;
	}

	public Integer getCompanyInvoiceDetailId() {
		return companyInvoiceDetailId;
	}

	public void setCompanyInvoiceDetailId(Integer companyInvoiceDetailId) {
		this.companyInvoiceDetailId = companyInvoiceDetailId;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getBillDateTime() {
		return billDateTime;
	}

	public void setBillDateTime(String billDateTime) {
		this.billDateTime = billDateTime;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public Double getTotalBillAmount() {
		return totalBillAmount;
	}

	public void setTotalBillAmount(Double totalBillAmount) {
		this.totalBillAmount = totalBillAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Double getOutstanding() {
		return outstanding;
	}

	public void setOutstanding(Double outstanding) {
		this.outstanding = outstanding;
	}

	public Integer getBillPayeeId() {
		return billPayeeId;
	}

	public void setBillPayeeId(Integer billPayeeId) {
		this.billPayeeId = billPayeeId;
	}

	public Integer getSaleId() {
		return saleId;
	}

	public void setSaleId(Integer saleId) {
		this.saleId = saleId;
	}

	public String getVisitNumber() {
		return visitNumber;
	}

	public void setVisitNumber(String visitNumber) {
		this.visitNumber = visitNumber;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getDoa() {
		return doa;
	}

	public void setDoa(String doa) {
		this.doa = doa;
	}

	public String getPdd() {
		return pdd;
	}

	public void setPdd(String pdd) {
		this.pdd = pdd;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public String getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	
	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	
	
}
