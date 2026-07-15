package com.param.entity.model.master;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "m_abdm_login_verification", schema = "abdm")
@SequenceGenerator(name = "m_abdm_login_verification_seq", sequenceName = "abdm.m_abdm_login_verification_seq", allocationSize = 1)
public class AbdmLoginVerification {


	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_abdm_login_verification_seq")
	
	@Column(name = "verification_mapper_id")
	private Integer verificationMapperId;

	@Column(name = "transaction_id")
	private String transactionId;

	@Column(name = "aadhar_number")
	private String aadharNumber;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "mobile_number")
	private String mobileNumber;
	
	@Column(name = "abha_id")
	private String abhaId;

	
	@Column(name = "abha_address")
	private String abhaAddress;
	
	@Column(name = "fetch_by_id")
	private Integer fetchById;
	
	@Column(name = "api_response_event_15")
	private String apiResponseEvent15;
	
	@Column(name = "api_response_event_8")
	private String apiResponseEvent8;
	
	@Column(name = "api_response_event_9")
	private String apiResponseEvent9;
	
	@Column(name = "api_response_event_14")
	private String apiResponseEvent14;
	
	@Column(name = "api_response_event_17")
	private String apiResponseEvent17;
	
	@Column(name = "api_response_event_13")
	private String apiResponseEvent13;
	
	@Column(name = "api_response_event_21")
	private String apiResponseEvent21;
	
	@Column(name = "api_response_event_24")
	private String apiResponseEvent24;
	
	/*@Column(name = "api_responses")
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
	
	
	
	@Column(name = "api_response_event_10")
	private String apiResponseEvent10;
	
	@Column(name = "api_response_event_11")
	private String apiResponseEvent11;
	
	@Column(name = "api_response_event_12")
	private String apiResponseEvent12;
	
	*/

	public Integer getVerificationMapperId() {
		return verificationMapperId;
	}

	public void setVerificationMapperId(Integer verificationMapperId) {
		this.verificationMapperId = verificationMapperId;
	}

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

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getAbhaId() {
		return abhaId;
	}

	public void setAbhaId(String abhaId) {
		this.abhaId = abhaId;
	}

	public String getAbhaAddress() {
		return abhaAddress;
	}

	public void setAbhaAddress(String abhaAddress) {
		this.abhaAddress = abhaAddress;
	}

	public Integer getFetchById() {
		return fetchById;
	}

	public void setFetchById(Integer fetchById) {
		this.fetchById = fetchById;
	}

	public String getApiResponseEvent15() {
		return apiResponseEvent15;
	}

	public void setApiResponseEvent15(String apiResponseEvent15) {
		this.apiResponseEvent15 = apiResponseEvent15;
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

	public String getApiResponseEvent14() {
		return apiResponseEvent14;
	}

	public void setApiResponseEvent14(String apiResponseEvent14) {
		this.apiResponseEvent14 = apiResponseEvent14;
	}

	public String getApiResponseEvent17() {
		return apiResponseEvent17;
	}

	public void setApiResponseEvent17(String apiResponseEvent17) {
		this.apiResponseEvent17 = apiResponseEvent17;
	}

	public String getApiResponseEvent13() {
		return apiResponseEvent13;
	}

	public void setApiResponseEvent13(String apiResponseEvent13) {
		this.apiResponseEvent13 = apiResponseEvent13;
	}

	public String getApiResponseEvent21() {
		return apiResponseEvent21;
	}

	public void setApiResponseEvent21(String apiResponseEvent21) {
		this.apiResponseEvent21 = apiResponseEvent21;
	}

	public String getApiResponseEvent24() {
		return apiResponseEvent24;
	}

	public void setApiResponseEvent24(String apiResponseEvent24) {
		this.apiResponseEvent24 = apiResponseEvent24;
	}

	/*public String getApiResponses() {
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
	}*/
	

}
