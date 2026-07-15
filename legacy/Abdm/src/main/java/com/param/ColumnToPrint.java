package com.param;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ColumnToPrint {
    
    
	// FOR MEDICATION OBJECT
	private String itemCode;
	private String itemName;
	private String frequencyCode;
	private String frequencyDesc;
	private String instructionText;
	private String roa;
	private String startDate;
	private Integer duration;
	private String durationUnit;
	private String strengthDesc;
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getFrequencyCode() {
		return frequencyCode;
	}
	public void setFrequencyCode(String frequencyCode) {
		this.frequencyCode = frequencyCode;
	}
	public String getFrequencyDesc() {
		return frequencyDesc;
	}
	public void setFrequencyDesc(String frequencyDesc) {
		this.frequencyDesc = frequencyDesc;
	}
	public String getInstructionText() {
		return instructionText;
	}
	public void setInstructionText(String instructionText) {
		this.instructionText = instructionText;
	}
	public String getRoa() {
		return roa;
	}
	public void setRoa(String roa) {
		this.roa = roa;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public String getDurationUnit() {
		return durationUnit;
	}
	public void setDurationUnit(String durationUnit) {
		this.durationUnit = durationUnit;
	}
	public String getStrengthDesc() {
		return strengthDesc;
	}
	public void setStrengthDesc(String strengthDesc) {
		this.strengthDesc = strengthDesc;
	}
 

   
	

    
    
}
