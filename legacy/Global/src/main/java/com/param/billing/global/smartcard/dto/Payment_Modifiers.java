package com.param.billing.global.smartcard.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Payment_Modifiers")  
public class Payment_Modifiers {
	 
	 PaymentModifier PaymentModifier;

	public PaymentModifier getPaymentModifier() {
		return PaymentModifier;
	}

	public void setPaymentModifier(PaymentModifier paymentModifier) {
		PaymentModifier = paymentModifier;
	}

	
	
}
