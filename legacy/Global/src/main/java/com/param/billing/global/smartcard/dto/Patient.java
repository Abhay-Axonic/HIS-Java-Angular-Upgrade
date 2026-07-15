package com.param.billing.global.smartcard.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Patient")
public class Patient {

	 private String Dependant;
	
	 private String First_Name;
	
	 private String Middle_Name;
	
	 private String Surname;
	
	 private String Date_Of_Birth;
	
	 private String Gender;

	 @XmlElement(name="Dependant") 
	public String getDependant() {
		return Dependant;
	}

	public void setDependant(String dependant) {
		Dependant = dependant;
	}

	@XmlElement(name="First_Name") 
	public String getFirst_Name() {
		return First_Name;
	}

	public void setFirst_Name(String first_Name) {
		First_Name = first_Name;
	}

	@XmlElement(name="Middle_Name") 
	public String getMiddle_Name() {
		return Middle_Name;
	}

	public void setMiddle_Name(String middle_Name) {
		Middle_Name = middle_Name;
	}

	@XmlElement(name="Surname") 
	public String getSurname() {
		return Surname;
	}

	public void setSurname(String surname) {
		Surname = surname;
	}

	@XmlElement(name="Date_Of_Birth") 
	public String getDate_Of_Birth() {
		return Date_Of_Birth;
	}

	public void setDate_Of_Birth(String date_Of_Birth) {
		Date_Of_Birth = date_Of_Birth;
	}

	@XmlElement(name="Gender") 
	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}
	 
	 
}
