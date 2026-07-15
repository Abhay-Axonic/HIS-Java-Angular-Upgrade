package com.param.billing.smartcard.dto;

import java.util.List;


public class CliamDto {

	private String claim_code;
	private String payer_code;
	private String payer_name;
	private String amount;
	private String gross_amount;
	private String batch_number;
	private String dispatch_date;
	private String patient_number;
	private String patient_name;
	private String location_code;
	private String location_name;
	private String scheme_code;
	private String scheme_name;
	private String member_number;
	private String visit_number;
	private String visit_start;
	private String visit_end;
	private String currency;
	private String doctor_name;
	private String file_version;
	
	private List<DiagnosisDto> diagnosis;
	private List<PreAuthorizationDto> pre_authorization;
	private List<AdmissionDto> admission;
	private List<InvoicesDto> invoices;
	
	
	public String getClaim_code() {
		return claim_code;
	}
	public void setClaim_code(String claim_code) {
		this.claim_code = claim_code;
	}
	public String getPayer_code() {
		return payer_code;
	}
	public void setPayer_code(String payer_code) {
		this.payer_code = payer_code;
	}
	public String getPayer_name() {
		return payer_name;
	}
	public void setPayer_name(String payer_name) {
		this.payer_name = payer_name;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getGross_amount() {
		return gross_amount;
	}
	public void setGross_amount(String gross_amount) {
		this.gross_amount = gross_amount;
	}
	public String getBatch_number() {
		return batch_number;
	}
	public void setBatch_number(String batch_number) {
		this.batch_number = batch_number;
	}
	public String getDispatch_date() {
		return dispatch_date;
	}
	public void setDispatch_date(String dispatch_date) {
		this.dispatch_date = dispatch_date;
	}
	public String getPatient_number() {
		return patient_number;
	}
	public void setPatient_number(String patient_number) {
		this.patient_number = patient_number;
	}
	public String getPatient_name() {
		return patient_name;
	}
	public void setPatient_name(String patient_name) {
		this.patient_name = patient_name;
	}
	public String getLocation_code() {
		return location_code;
	}
	public void setLocation_code(String location_code) {
		this.location_code = location_code;
	}
	public String getLocation_name() {
		return location_name;
	}
	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}
	public String getScheme_code() {
		return scheme_code;
	}
	public void setScheme_code(String scheme_code) {
		this.scheme_code = scheme_code;
	}
	public String getScheme_name() {
		return scheme_name;
	}
	public void setScheme_name(String scheme_name) {
		this.scheme_name = scheme_name;
	}
	public String getMember_number() {
		return member_number;
	}
	public void setMember_number(String member_number) {
		this.member_number = member_number;
	}
	public String getVisit_number() {
		return visit_number;
	}
	public void setVisit_number(String visit_number) {
		this.visit_number = visit_number;
	}
	public String getVisit_start() {
		return visit_start;
	}
	public void setVisit_start(String visit_start) {
		this.visit_start = visit_start;
	}
	public String getVisit_end() {
		return visit_end;
	}
	public void setVisit_end(String visit_end) {
		this.visit_end = visit_end;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getDoctor_name() {
		return doctor_name;
	}
	public void setDoctor_name(String doctor_name) {
		this.doctor_name = doctor_name;
	}
	public String getFile_version() {
		return file_version;
	}
	public void setFile_version(String file_version) {
		this.file_version = file_version;
	}
	public List<DiagnosisDto> getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(List<DiagnosisDto> diagnosis) {
		this.diagnosis = diagnosis;
	}
	public List<PreAuthorizationDto> getPre_authorization() {
		return pre_authorization;
	}
	public void setPre_authorization(List<PreAuthorizationDto> pre_authorization) {
		this.pre_authorization = pre_authorization;
	}
	public List<AdmissionDto> getAdmission() {
		return admission;
	}
	public void setAdmission(List<AdmissionDto> admission) {
		this.admission = admission;
	}
	public List<InvoicesDto> getInvoices() {
		return invoices;
	}
	public void setInvoices(List<InvoicesDto> invoices) {
		this.invoices = invoices;
	}
	
	
	
	
	
	
	
	
}
