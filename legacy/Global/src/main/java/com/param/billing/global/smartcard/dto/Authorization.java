package com.param.billing.global.smartcard.dto;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Authorization")
public class Authorization {

	 private String Pre_Authorization_Number;
	 
	 private String Pre_Authorization_Amount;

	 @XmlElement(name="Pre_Authorization_Number") 
	public String getPre_Authorization_Number() {
		return Pre_Authorization_Number;
	}

	public void setPre_Authorization_Number(String pre_Authorization_Number) {
		Pre_Authorization_Number = pre_Authorization_Number;
	}

	 @XmlElement(name="Pre_Authorization_Amount") 
	public String getPre_Authorization_Amount() {
		return Pre_Authorization_Amount;
	}

	public void setPre_Authorization_Amount(String pre_Authorization_Amount) {
		Pre_Authorization_Amount = pre_Authorization_Amount;
	}
	 
	 
}
