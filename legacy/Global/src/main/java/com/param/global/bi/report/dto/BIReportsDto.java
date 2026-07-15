package com.param.global.bi.report.dto;

import java.util.List;

import org.json.JSONObject;

public class BIReportsDto {
	
	private Integer organizationId;
	
	private Integer unitId;
	private Integer userId;
	
	private String trandate;
	private String misReportIds;

	private Double totalcash;
	
	private Double online;
	
	private Double cheque;
	
	private Double creditcard;
	
	private Double debitcard;

	private Double mpesa;

	private Double loyaltycard;
	
	private Double x_ray;

	private Double sono;

	private Double echo;
	
	private Double doppler;
	
	private Double ct;
	
	private Double ecg;
	
	private Double mri;
	
	private Double path;
	
	private Double consultation;
	
	private Double radiology;
	private JSONObject biReportJson;
	private String biReportString;
	private Integer specialityId;
	private Integer paymentModeId;
	private Integer offset;
	private String fromDate;
	private String toDate;
	private Integer noOfRecordsPerPage;
	private Integer payment_mode_id;
	private Integer speciality_id;
	private String payment_mode_desc;
	private Double amount;
	private String depositdate;
	private Integer organization_id;
	private List<KeyValuePairDto> parameterList;
	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getTrandate() {
		return trandate;
	}

	public void setTrandate(String trandate) {
		this.trandate = trandate;
	}

	public Double getTotalcash() {
		return totalcash;
	}

	public void setTotalcash(Double totalcash) {
		this.totalcash = totalcash;
	}

	public Double getOnline() {
		return online;
	}

	public void setOnline(Double online) {
		this.online = online;
	}

	public Double getCheque() {
		return cheque;
	}

	public void setCheque(Double cheque) {
		this.cheque = cheque;
	}

	public Double getCreditcard() {
		return creditcard;
	}

	public void setCreditcard(Double creditcard) {
		this.creditcard = creditcard;
	}

	public Double getDebitcard() {
		return debitcard;
	}

	public void setDebitcard(Double debitcard) {
		this.debitcard = debitcard;
	}

	public Double getMpesa() {
		return mpesa;
	}

	public void setMpesa(Double mpesa) {
		this.mpesa = mpesa;
	}

	public Double getLoyaltycard() {
		return loyaltycard;
	}

	public void setLoyaltycard(Double loyaltycard) {
		this.loyaltycard = loyaltycard;
	}

	public Double getSono() {
		return sono;
	}

	public void setSono(Double sono) {
		this.sono = sono;
	}

	public Double getEcho() {
		return echo;
	}

	public void setEcho(Double echo) {
		this.echo = echo;
	}

	public Double getDoppler() {
		return doppler;
	}

	public void setDoppler(Double doppler) {
		this.doppler = doppler;
	}

	public Double getCt() {
		return ct;
	}

	public void setCt(Double ct) {
		this.ct = ct;
	}

	public Double getEcg() {
		return ecg;
	}

	public void setEcg(Double ecg) {
		this.ecg = ecg;
	}

	public Double getMri() {
		return mri;
	}

	public void setMri(Double mri) {
		this.mri = mri;
	}

	public Double getPath() {
		return path;
	}

	public void setPath(Double path) {
		this.path = path;
	}

	public Double getConsultation() {
		return consultation;
	}

	public void setConsultation(Double consultation) {
		this.consultation = consultation;
	}

	public Double getRadiology() {
		return radiology;
	}

	public void setRadiology(Double radiology) {
		this.radiology = radiology;
	}


	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Double getX_ray() {
		return x_ray;
	}

	public void setX_ray(Double x_ray) {
		this.x_ray = x_ray;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getMisReportIds() {
		return misReportIds;
	}

	public void setMisReportIds(String misReportIds) {
		this.misReportIds = misReportIds;
	}

	public JSONObject getBiReportJson() {
		return biReportJson;
	}

	public void setBiReportJson(JSONObject biReportJson) {
		this.biReportJson = biReportJson;
	}

	public String getBiReportString() {
		return biReportString;
	}

	public void setBiReportString(String biReportString) {
		this.biReportString = biReportString;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getPaymentModeId() {
		return paymentModeId;
	}

	public void setPaymentModeId(Integer paymentModeId) {
		this.paymentModeId = paymentModeId;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public Integer getPayment_mode_id() {
		return payment_mode_id;
	}

	public void setPayment_mode_id(Integer payment_mode_id) {
		this.payment_mode_id = payment_mode_id;
	}

	public Integer getSpeciality_id() {
		return speciality_id;
	}

	public void setSpeciality_id(Integer speciality_id) {
		this.speciality_id = speciality_id;
	}

	public String getPayment_mode_desc() {
		return payment_mode_desc;
	}

	public void setPayment_mode_desc(String payment_mode_desc) {
		this.payment_mode_desc = payment_mode_desc;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getDepositdate() {
		return depositdate;
	}

	public void setDepositdate(String depositdate) {
		this.depositdate = depositdate;
	}

	public Integer getOrganization_id() {
		return organization_id;
	}

	public void setOrganization_id(Integer organization_id) {
		this.organization_id = organization_id;
	}

	public List<KeyValuePairDto> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<KeyValuePairDto> paramDtoList) {
		this.parameterList = paramDtoList;
	}

	
	
}
