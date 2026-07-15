package com.param.global.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;



public class TallyIntegrationDto {

	private Integer unitId;

	private String voucherNo;
	
	private String ledger;
	
	private BigDecimal debit;
	
	private BigDecimal credit;
	
	private String voucherNarration;
	
	private String reference;
	
	private String voucherType;
	
	private Double billingAmount;
	
	private String paymentId;
	
	private String instrumentNumber;
	
	private String instrumentDate;
	
	private String bankName;
	
	private String newReference;
	
	private String refNo;
	
	private String ledgerNamesFromTally;
	
	private Date searchDate;
	
	private String voucherDate;
	
	private String uniqueNumber;
	
	private String newLedger;
	
	private String underGroupName;

	private List<TallyIntegrationDto> getTallyIntegrationData;
	
	private List<TallyIntegrationDto> ledgerNamesFromHis;

	private String xmlString;
	private String tallyIp;
	private String tallyPortNo;
	private Integer organizationId;
	private String requestXmlString;
	private String responseXmlString;
	private Character isTallyLocalHost;

	public List<TallyIntegrationDto> getGetTallyIntegrationData() {
		return getTallyIntegrationData;
	}

	public void setGetTallyIntegrationData(List<TallyIntegrationDto> getTallyIntegrationData) {
		this.getTallyIntegrationData = getTallyIntegrationData;
	}
	
	public List<TallyIntegrationDto> getLedgerNamesFromHis() {
		return ledgerNamesFromHis;
	}

	public void setLedgerNamesFromHis(List<TallyIntegrationDto> ledgerNamesFromHis) {
		this.ledgerNamesFromHis = ledgerNamesFromHis;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Date getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(Date searchDate) {
		this.searchDate = searchDate;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getLedger() {
		return ledger;
	}

	public void setLedger(String ledger) {
		this.ledger = ledger;
	}


	public BigDecimal getDebit() {
		return debit;
	}

	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	public String getVoucherNarration() {
		return voucherNarration;
	}

	public void setVoucherNarration(String voucherNarration) {
		this.voucherNarration = voucherNarration;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getVoucherType() {
		return voucherType;
	}

	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}

	public Double getBillingAmount() {
		return billingAmount;
	}

	public void setBillingAmount(Double billingAmount) {
		this.billingAmount = billingAmount;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getInstrumentNumber() {
		return instrumentNumber;
	}

	public void setInstrumentNumber(String instrumentNumber) {
		this.instrumentNumber = instrumentNumber;
	}

	public String getInstrumentDate() {
		return instrumentDate;
	}

	public void setInstrumentDate(String instrumentDate) {
		this.instrumentDate = instrumentDate;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getNewReference() {
		return newReference;
	}

	public void setNewReference(String newReference) {
		this.newReference = newReference;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getVoucherDate() {
		return voucherDate;
	}

	public void setVoucherDate(String voucherDate) {
		this.voucherDate = voucherDate;
	}

	public String getUniqueNumber() {
		return uniqueNumber;
	}

	public void setUniqueNumber(String uniqueNumber) {
		this.uniqueNumber = uniqueNumber;
	}
	
	public String getLedgerNamesFromTally() {
		return ledgerNamesFromTally;
	}

	public void setLedgerNamesFromTally(String ledgerNamesFromTally) {
		this.ledgerNamesFromTally = ledgerNamesFromTally;
	}
	public String getNewLedger() {
		return newLedger;
	}

	public void setNewLedger(String newLedger) {
		this.newLedger = newLedger;
	}

	public String getUnderGroupName() {
		return underGroupName;
	}

	public void setUnderGroupName(String underGroupName) {
		this.underGroupName = underGroupName;
	}

	public String getXmlString() {
		return xmlString;
	}

	public void setXmlString(String xmlString) {
		this.xmlString = xmlString;
	}

	public String getTallyIp() {
		return tallyIp;
	}

	public void setTallyIp(String tallyIp) {
		this.tallyIp = tallyIp;
	}

	public String getTallyPortNo() {
		return tallyPortNo;
	}

	public void setTallyPortNo(String tallyPortNo) {
		this.tallyPortNo = tallyPortNo;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getRequestXmlString() {
		return requestXmlString;
	}

	public void setRequestXmlString(String requestXmlString) {
		this.requestXmlString = requestXmlString;
	}

	public String getResponseXmlString() {
		return responseXmlString;
	}

	public void setResponseXmlString(String responseXmlString) {
		this.responseXmlString = responseXmlString;
	}

	public Character getIsTallyLocalHost() {
		return isTallyLocalHost;
	}

	public void setIsTallyLocalHost(Character isTallyLocalHost) {
		this.isTallyLocalHost = isTallyLocalHost;
	}
}
