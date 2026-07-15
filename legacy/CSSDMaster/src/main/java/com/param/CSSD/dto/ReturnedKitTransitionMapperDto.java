package com.param.CSSD.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(Include.NON_NULL)
public class ReturnedKitTransitionMapperDto {

	private Integer id;
	private Integer issueNo;
	private Integer kitId;
	private Integer transitionNumber;

	private String kitCode;
	
	private String kitName;
	
	private Integer quantity;
	
	private Integer autoclaveNumber;
	
	private String autoclaveDate;
	
	private String expiryDate;
	
	private Integer batchNumber;

	private String code;
	
	private String name;
	
	private String reasonForReturn;
	
	private Boolean isAccepted;
	
	private List<PreSterilizationProcessInstrumentDto> kitInstrumentList;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIssueNo() {
		return issueNo;
	}
	public void setIssueNo(Integer issueNo) {
		this.issueNo = issueNo;
	}
	public Integer getKitId() {
		return kitId;
	}
	public void setKitId(Integer kitId) {
		this.kitId = kitId;
	}
	public Integer getTransitionNumber() {
		return transitionNumber;
	}
	public void setTransitionNumber(Integer transitionNumber) {
		this.transitionNumber = transitionNumber;
	}
	public String getKitCode() {
		return kitCode;
	}
	public void setKitCode(String kitCode) {
		this.kitCode = kitCode;
	}
	public String getKitName() {
		return kitName;
	}
	public void setKitName(String kitName) {
		this.kitName = kitName;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getAutoclaveNumber() {
		return autoclaveNumber;
	}
	public void setAutoclaveNumber(Integer autoclaveNumber) {
		this.autoclaveNumber = autoclaveNumber;
	}
	public String getAutoclaveDate() {
		return autoclaveDate;
	}
	public void setAutoclaveDate(String autoclaveDate) {
		this.autoclaveDate = autoclaveDate;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public Integer getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(Integer batchNumber) {
		this.batchNumber = batchNumber;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getReasonForReturn() {
		return reasonForReturn;
	}
	public void setReasonForReturn(String reasonForReturn) {
		this.reasonForReturn = reasonForReturn;
	}
	public List<PreSterilizationProcessInstrumentDto> getKitInstrumentList() {
		return kitInstrumentList;
	}
	public void setKitInstrumentList(List<PreSterilizationProcessInstrumentDto> kitInstrumentList) {
		this.kitInstrumentList = kitInstrumentList;
	}
	public Boolean getIsAccepted() {
		return isAccepted;
	}
	public void setIsAccepted(Boolean isAccepted) {
		this.isAccepted = isAccepted;
	}
	
	
		
}
