package com.param.service.global.model;

import java.io.Serializable;

public class CookieObjectAppConfig implements Serializable {

	private Integer selfPayeeId;
	private Character isPreOrPostBilling;
	private Integer opdBillingCatId;
	private String groupPracticeNo;
	private String groupPracticeName;
	private String practiceNo;
	private String startDateFormat;
	private Integer defaultSelfTariffId;
	private  Boolean taxInclusive;
	private Character isConsolidatedBill;
	private String host;
	private String port;
	private String id;
	private String password;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Character getIsPreOrPostBilling() {
		return isPreOrPostBilling;
	}

	public void setIsPreOrPostBilling(Character isPreOrPostBilling) {
		this.isPreOrPostBilling = isPreOrPostBilling;
	}

	public Integer getSelfPayeeId() {
		return selfPayeeId;
	}

	public void setSelfPayeeId(Integer selfPayeeId) {
		this.selfPayeeId = selfPayeeId;
	}

	public String getGroupPracticeNo() {
		return groupPracticeNo;
	}

	public void setGroupPracticeNo(String groupPracticeNo) {
		this.groupPracticeNo = groupPracticeNo;
	}

	public String getGroupPracticeName() {
		return groupPracticeName;
	}

	public void setGroupPracticeName(String groupPracticeName) {
		this.groupPracticeName = groupPracticeName;
	}

	public String getPracticeNo() {
		return practiceNo;
	}

	public void setPracticeNo(String practiceNo) {
		this.practiceNo = practiceNo;
	}

	public String getStartDateFormat() {
		return startDateFormat;
	}

	public void setStartDateFormat(String startDateFormat) {
		this.startDateFormat = startDateFormat;
	}

	public Integer getDefaultSelfTariffId() {
		return defaultSelfTariffId;
	}

	public void setDefaultSelfTariffId(Integer defaultSelfTariffId) {
		this.defaultSelfTariffId = defaultSelfTariffId;
	}

	public Boolean getTaxInclusive() {
		return taxInclusive;
	}

	public void setTaxInclusive(Boolean taxInclusive) {
		this.taxInclusive = taxInclusive;
	}

	public Character getIsConsolidatedBill() {
		return isConsolidatedBill;
	}

	public void setIsConsolidatedBill(Character isConsolidatedBill) {
		this.isConsolidatedBill = isConsolidatedBill;
	}
	public Integer getOpdBillingCatId() {
		return opdBillingCatId;
	}

	public void setOpdBillingCatId(Integer opdBillingCatId) {
		this.opdBillingCatId = opdBillingCatId;
	}
	
	
}
