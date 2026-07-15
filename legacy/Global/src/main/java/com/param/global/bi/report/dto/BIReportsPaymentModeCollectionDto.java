package com.param.global.bi.report.dto;

import java.util.List;

import org.hibernate.dialect.PostgresPlusDialect;
import org.json.JSONArray;
import org.json.JSONException;

public class BIReportsPaymentModeCollectionDto  extends PostgresPlusDialect{
	
	private String trandate;
	private JSONArray payment;
	private JSONArray revenue;
	private JSONArray patientcount;

//	private Double cash;
//	
//	private Double cheque;
//	
//	private Double creditcard;
//	
//	private Double debitcard;
//	
//	private Double online;
//	
	private List<BIReportsPaymentModeCollectionDto> payments;
	public String getTrandate() {
		return trandate;
	}
	
	public void setTrandate(String trandate) {
		this.trandate = trandate;
	}

	public JSONArray getPayment() {
		return payment;
	}

	public void setPayment(JSONArray payment) throws JSONException {
		this.payment = new JSONArray(payment);
	}

	public List<BIReportsPaymentModeCollectionDto> getPayments() {
		return payments;
	}

	public void setPayments(List<BIReportsPaymentModeCollectionDto> payments) {
		this.payments = payments;
	}

	public JSONArray getRevenue() {
		return revenue;
	}

	public void setRevenue(JSONArray revenue) {
		this.revenue = revenue;
	}

	public JSONArray getPatientcount() {
		return patientcount;
	}

	public void setPatientcount(JSONArray patientcount) {
		this.patientcount = patientcount;
	}


	
//	public Double getCash() {
//		return cash;
//	}
//	
//	public void setCash(Double cash) {
//		this.cash = cash;
//	}
//	
//	public Double getCheque() {
//		return cheque;
//	}
//	
//	public void setCheque(Double cheque) {
//		this.cheque = cheque;
//	}
//	
//	public Double getCreditcard() {
//		return creditcard;
//	}
//	
//	public void setCreditcard(Double creditcard) {
//		this.creditcard = creditcard;
//	}
//	
//	public Double getDebitcard() {
//		return debitcard;
//	}
//	
//	public void setDebitcard(Double debitcard) {
//		this.debitcard = debitcard;
//	}
//	public Double getOnline() {
//		return online;
//	}
//	
//	public void setOnline(Double online) {
//		this.online = online;
//	}
	
	
}
