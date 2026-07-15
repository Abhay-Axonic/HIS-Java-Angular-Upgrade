package com.param.billing.global.smartcard.dto;

import java.util.List;

public class InvoicesJson {

	private Double amount;
	private Double gross_amount;
	private String invoice_date;
	private String invoice_number;
	private String service_type;
	List<ItemsJson> lines;
	List<PaymentModifiersJson> payment_modifiers;

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

	public String getInvoice_date() {
		return invoice_date;
	}

	public void setInvoice_date(String invoice_date) {
		this.invoice_date = invoice_date;
	}

	public String getInvoice_number() {
		return invoice_number;
	}

	public void setInvoice_number(String invoice_number) {
		this.invoice_number = invoice_number;
	}

	public String getService_type() {
		return service_type;
	}

	public void setService_type(String service_type) {
		this.service_type = service_type;
	}

	public List<ItemsJson> getLines() {
		return lines;
	}

	public void setLines(List<ItemsJson> lines) {
		this.lines = lines;
	}

	public List<PaymentModifiersJson> getPayment_modifiers() {
		return payment_modifiers;
	}

	public void setPayment_modifiers(List<PaymentModifiersJson> payment_modifiers) {
		this.payment_modifiers = payment_modifiers;
	}

}
