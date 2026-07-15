package com.param.billing.global.smartcard.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Provider") 
public class Provider {

	 private String Role;
	
	 private String Country_Code;
	
	 private String Group_Practice_Number;
	
	 private String Group_Practice_Name;

	 @XmlElement(name="Role") 
	public String getRole() {
		return Role;
	}

	public void setRole(String role) {
		Role = role;
	}

	 @XmlElement(name="Country_Code") 
	public String getCountry_Code() {
		return Country_Code;
	}

	public void setCountry_Code(String country_Code) {
		Country_Code = country_Code;
	}

	 @XmlElement(name="Group_Practice_Number") 
	public String getGroup_Practice_Number() {
		return Group_Practice_Number;
	}

	public void setGroup_Practice_Number(String group_Practice_Number) {
		Group_Practice_Number = group_Practice_Number;
	}

	 @XmlElement(name="Group_Practice_Name") 
	public String getGroup_Practice_Name() {
		return Group_Practice_Name;
	}

	public void setGroup_Practice_Name(String group_Practice_Name) {
		Group_Practice_Name = group_Practice_Name;
	}
	 
	 
}

