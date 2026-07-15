package com.param.billing.global.smartcard.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Payment_Modifier") 
public class Payment_Modifier {

	 private String Type;
	 
	 private String Amount;
	 
	 private String Receipt;

	 @XmlElement(name="Type") 
	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	@XmlElement(name="Amount") 
	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	@XmlElement(name="Receipt") 
	public String getReceipt() {
		return Receipt;
	}

	public void setReceipt(String receipt) {
		Receipt = receipt;
	}
	 
	 
}
