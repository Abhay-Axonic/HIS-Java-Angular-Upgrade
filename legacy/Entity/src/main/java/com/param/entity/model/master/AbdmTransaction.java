package com.param.entity.model.master;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "m_abdm_transaction", schema = "abdm")
@SequenceGenerator(name = "m_abdm_transaction_seq", sequenceName = "abdm.m_abdm_transaction_seq", allocationSize = 1)
public class AbdmTransaction {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_abdm_transaction_seq")
	
	@Column(name = "abdm_transaction_mapper_id")
	private Integer abdmTransactionMapperId;

	@Column(name = "transaction_id")
	private String transactionId;

	@Column(name = "aadhar_number")
	private String aadharNumber;

	//@Column(name = "status")
	//private Character status;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "mobile_number")
	private Integer mobileNumber;
	
	@Column(name = "aadhar_preverified_json")
	private String aadharPreverifiedJson;
	
	@Column(name = "abdm_health_card")
	private String abdmHealthCard;
	
	@Column(name = "health_id")
	private String healthId;

	
	@Column(name = "health_id_number")
	private String healthIdNumber;
	
	@Column(name = "consent_checked")
	private Character consentChecked;
	
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "api_responses")
	private String apiResponses;
	
	@Column(name = "api_response_event_3")
	private String apiResponseEvent3;
	
	@Column(name = "api_response_event_4")
	private String apiResponseEvent4;
	
	@Column(name = "api_response_event_5")
	private String apiResponseEvent5;
	
	@Column(name = "api_response_event_6")
	private String apiResponseEvent6;
	
	@Column(name = "api_response_event_7")
	private String apiResponseEvent7;
	
	@Column(name = "api_response_event_8")
	private String apiResponseEvent8;
	
	@Column(name = "api_response_event_9")
	private String apiResponseEvent9;
	
	@Column(name = "api_response_event_10")
	private String apiResponseEvent10;
	
	@Column(name = "api_response_event_11")
	private String apiResponseEvent11;
	
	@Column(name = "api_response_event_12")
	private String apiResponseEvent12;
	
	@Column(name = "api_response_event_13")
	private String apiResponseEvent13;
	
	@Column(name = "api_response_event_28")
	private String apiResponseEvent28;
	
	@Column(name = "api_response_event_29")
	private String apiResponseEvent29;
	
	@Column(name = "api_response_event_39")
	private String apiResponseEvent39;
	
	@Column(name = "api_response_event_40")
	private String apiResponseEvent40;
	
	@Column(name = "transaction_id_care_context")
	private String transactionIdCareContext;
	
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

//	public Character getStatus() {
//		return status;
//	}
//
//	public void setStatus(Character status) {
//		this.status = status;
//	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getAbdmTransactionMapperId() {
		return abdmTransactionMapperId;
	}

	public void setAbdmTransactionMapperId(Integer abdmTransactionMapperId) {
		this.abdmTransactionMapperId = abdmTransactionMapperId;
	}

	public Integer getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(Integer mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getAadharPreverifiedJson() {
		return aadharPreverifiedJson;
	}

	public void setAadharPreverifiedJson(String aadharPreverifiedJson) {
		this.aadharPreverifiedJson = aadharPreverifiedJson;
	}

	public String getAbdmHealthCard() {
		return abdmHealthCard;
	}

	public void setAbdmHealthCard(String abdmHealthCard) {
		this.abdmHealthCard = abdmHealthCard;
	}

	public String getHealthId() {
		return healthId;
	}

	public void setHealthId(String healthId) {
		this.healthId = healthId;
	}

	public String getHealthIdNumber() {
		return healthIdNumber;
	}

	public void setHealthIdNumber(String healthIdNumber) {
		this.healthIdNumber = healthIdNumber;
	}

	public Character getConsentChecked() {
		return consentChecked;
	}

	public void setConsentChecked(Character consentChecked) {
		this.consentChecked = consentChecked;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getApiResponses() {
		return apiResponses;
	}

	public void setApiResponses(String apiResponses) {
		this.apiResponses = apiResponses;
	}

	public String getApiResponseEvent3() {
		return apiResponseEvent3;
	}

	public void setApiResponseEvent3(String apiResponseEvent3) {
		this.apiResponseEvent3 = apiResponseEvent3;
	}

	public String getApiResponseEvent4() {
		return apiResponseEvent4;
	}

	public void setApiResponseEvent4(String apiResponseEvent4) {
		this.apiResponseEvent4 = apiResponseEvent4;
	}

	public String getApiResponseEvent5() {
		return apiResponseEvent5;
	}

	public void setApiResponseEvent5(String apiResponseEvent5) {
		this.apiResponseEvent5 = apiResponseEvent5;
	}

	public String getApiResponseEvent6() {
		return apiResponseEvent6;
	}

	public void setApiResponseEvent6(String apiResponseEvent6) {
		this.apiResponseEvent6 = apiResponseEvent6;
	}

	public String getApiResponseEvent7() {
		return apiResponseEvent7;
	}

	public void setApiResponseEvent7(String apiResponseEvent7) {
		this.apiResponseEvent7 = apiResponseEvent7;
	}

	public String getApiResponseEvent8() {
		return apiResponseEvent8;
	}

	public void setApiResponseEvent8(String apiResponseEvent8) {
		this.apiResponseEvent8 = apiResponseEvent8;
	}

	public String getApiResponseEvent9() {
		return apiResponseEvent9;
	}

	public void setApiResponseEvent9(String apiResponseEvent9) {
		this.apiResponseEvent9 = apiResponseEvent9;
	}

	public String getApiResponseEvent10() {
		return apiResponseEvent10;
	}

	public void setApiResponseEvent10(String apiResponseEvent10) {
		this.apiResponseEvent10 = apiResponseEvent10;
	}

	public String getApiResponseEvent11() {
		return apiResponseEvent11;
	}

	public void setApiResponseEvent11(String apiResponseEvent11) {
		this.apiResponseEvent11 = apiResponseEvent11;
	}

	public String getApiResponseEvent12() {
		return apiResponseEvent12;
	}

	public void setApiResponseEvent12(String apiResponseEvent12) {
		this.apiResponseEvent12 = apiResponseEvent12;
	}

	public String getApiResponseEvent13() {
		return apiResponseEvent13;
	}

	public void setApiResponseEvent13(String apiResponseEvent13) {
		this.apiResponseEvent13 = apiResponseEvent13;
	}

	public String getApiResponseEvent28() {
		return apiResponseEvent28;
	}

	public void setApiResponseEvent28(String apiResponseEvent28) {
		this.apiResponseEvent28 = apiResponseEvent28;
	}

	public String getApiResponseEvent29() {
		return apiResponseEvent29;
	}

	public void setApiResponseEvent29(String apiResponseEvent29) {
		this.apiResponseEvent29 = apiResponseEvent29;
	}

	public String getApiResponseEvent39() {
		return apiResponseEvent39;
	}

	public void setApiResponseEvent39(String apiResponseEvent39) {
		this.apiResponseEvent39 = apiResponseEvent39;
	}

	public String getApiResponseEvent40() {
		return apiResponseEvent40;
	}

	public void setApiResponseEvent40(String apiResponseEvent40) {
		this.apiResponseEvent40 = apiResponseEvent40;
	}

	public String getTransactionIdCareContext() {
		return transactionIdCareContext;
	}

	public void setTransactionIdCareContext(String transactionIdCareContext) {
		this.transactionIdCareContext = transactionIdCareContext;
	}
	
	
	
}
