package com.param.billing.smartcard.dto;

import java.util.List;

public class InvoicesDto {

	private String  amount;
	private String  gross_amount;
	private String  invoice_date; 
	private String  invoice_number;
	private String  service_type;
	
	private List<LinesDto> lines;
	private List<PaymentModifiersDto> payment_modifiers;
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
	public List<LinesDto> getLines() {
		return lines;
	}
	public void setLines(List<LinesDto> lines) {
		this.lines = lines;
	}
	public List<PaymentModifiersDto> getPayment_modifiers() {
		return payment_modifiers;
	}
	public void setPayment_modifiers(List<PaymentModifiersDto> payment_modifiers) {
		this.payment_modifiers = payment_modifiers;
	}
				
	
	
	
	
}
