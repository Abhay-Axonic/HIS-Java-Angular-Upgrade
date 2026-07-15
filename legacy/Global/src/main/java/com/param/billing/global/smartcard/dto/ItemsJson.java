package com.param.billing.global.smartcard.dto;

import java.util.List;

public class ItemsJson {

	private String item_code;
	private String item_name;
	private String service_group;
	private String charge_date;
	private Double unit_price;
	private Integer quantity;
	private Double amount;
	private Double gross_amount;
	private String pre_authorization_code;
	private List<PaymentReferenceJson> payment_reference;

	public String getItem_code() {
		return item_code;
	}

	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getService_group() {
		return service_group;
	}

	public void setService_group(String service_group) {
		this.service_group = service_group;
	}

	public String getCharge_date() {
		return charge_date;
	}

	public void setCharge_date(String charge_date) {
		this.charge_date = charge_date;
	}

	public Double getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(Double unit_price) {
		this.unit_price = unit_price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getGross_amount() {
		return gross_amount;
	}

	public void setGross_amount(Double gross_amount) {
		this.gross_amount = gross_amount;
	}

	public String getPre_authorization_code() {
		return pre_authorization_code;
	}

	public void setPre_authorization_code(String pre_authorization_code) {
		this.pre_authorization_code = pre_authorization_code;
	}

	public List<PaymentReferenceJson> getPayment_reference() {
		return payment_reference;
	}

	public void setPayment_reference(List<PaymentReferenceJson> payment_reference) {
		this.payment_reference = payment_reference;
	}

}
