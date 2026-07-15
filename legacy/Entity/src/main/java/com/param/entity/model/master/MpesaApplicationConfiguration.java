package com.param.entity.model.master;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.UnitMaster;


@NamedQueries({
	
	@NamedQuery(name="CHECK_FOR_UNIQUE_MPESA_APP_CONFIGURATION",
			query="SELECT mstr.mpesaApplicationConfigurationId as mpesaApplicationConfigurationId  " 
						+ " FROM MpesaApplicationConfiguration mstr "
						+ " WHERE  mstr.organizationId= :organizationId "
						+ " AND mstr.unitId= :unitId "
						),
	
	@NamedQuery(name="GET_MPESA_APPLICATION_CONFIGURATION",
	query="SELECT mstr.mpesaApplicationConfigurationId as mpesaApplicationConfigurationId , " 
				+ " mstr.businessShortcode as businessShortcode , "
				+ " mstr.stkPushPassword as stkPushPassword , "
				+ " mstr.stkTimestamp as stkTimestamp , "
				+ " mstr.transactionType as transactionType, "
				+ " mstr.partyB as partyB, "
				+ " mstr.callbackUrl as callbackUrl, "
				+ " mstr.accountRefernece as accountRefernece, "
				+ " mstr.transactionDescription as transactionDescription, "
				+ " mstr.appKey as appKey, "
				+ " mstr.appSecretKey as appSecretKey, "
				+ " mstr.terminalMsisdn as terminalMsisdn, "
				+ " mstr.validationPassword as validationPassword, "
				+ " mstr.initiator as initiator, "
				+ " mstr.statusResultURL as statusResultURL, "
				+ " mstr.status as status "
				+ " FROM MpesaApplicationConfiguration mstr "
				+ " WHERE  mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId"
				+ " AND mstr.status= 'A' "
				),
	
	@NamedQuery(name="GET_MPESA_APPLICATION_CONFIGURATION_LIST",
	query="SELECT mstr.mpesaApplicationConfigurationId as mpesaApplicationConfigurationId , " 
				+ " mstr.businessShortcode as businessShortcode , "
				+ " mstr.stkPushPassword as stkPushPassword , "
				+ " mstr.stkTimestamp as stkTimestamp , "
				+ " mstr.transactionType as transactionType, "
				+ " mstr.partyB as partyB, "
				+ " mstr.callbackUrl as callbackUrl, "
				+ " mstr.accountRefernece as accountRefernece, "
				+ " mstr.transactionDescription as transactionDescription, "
				+ " mstr.appKey as appKey, "
				+ " mstr.unitId as unitId, "
				+ " mstr.appSecretKey as appSecretKey, "
				+ " mstr.terminalMsisdn as terminalMsisdn, "
				+ " mstr.validationPassword as validationPassword, "
				+ " unit.unitName as unitName , "
				+ " mstr.initiator as initiator, "
				+ " mstr.statusResultURL as statusResultURL, "
				+ " mstr.status as status "
				+ " FROM MpesaApplicationConfiguration mstr "
				+ " INNER JOIN mstr.unitMaster unit "
				+ " WHERE  mstr.organizationId= :organizationId "
				)
})

@Entity
@Table(name = "m_mpesa_application_configuration", schema = "public")
@SequenceGenerator(name = "mpesa_application_configuration_seq", sequenceName = "public.mpesa_application_configuration_seq", allocationSize = 1)
public class MpesaApplicationConfiguration {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mpesa_application_configuration_seq")
	@Column(name = "mpesa_application_configuration_id")
	private Integer mpesaApplicationConfigurationId;

	@Column(name = "business_shortcode")
	private String businessShortcode;

	@Column(name = "stk_push_password")
	private String stkPushPassword;
	
	@Column(name = "stk_timestamp")
	private String stkTimestamp;
	
	@Column(name = "transaction_type")
	private String transactionType;
	
	@Column(name = "party_b")
	private String partyB;
	
	@Column(name = "callback_url")
	private String callbackUrl;
	
	@Column(name = "account_refernece")
	private String accountRefernece;
	
	@Column(name = "transaction_description")
	private String transactionDescription;
	
	@Column(name = "app_key")
	private String appKey;
	
	@Column(name = "app_secret_key")
	private String appSecretKey;
	
	@Column(name = "terminal_msisdn")
	private String terminalMsisdn;

	@Column(name = "validation_password")
	private String validationPassword;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "status")
	private Character status;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "initiator")
	private String initiator;
	
	@Column(name = "status_result_URL")
	private String statusResultURL;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="unit_id", insertable=false, updatable=false)
	private UnitMaster unitMaster;

	public Integer getMpesaApplicationConfigurationId() {
		return mpesaApplicationConfigurationId;
	}

	public void setMpesaApplicationConfigurationId(Integer mpesaApplicationConfigurationId) {
		this.mpesaApplicationConfigurationId = mpesaApplicationConfigurationId;
	}

	public String getBusinessShortcode() {
		return businessShortcode;
	}

	public void setBusinessShortcode(String businessShortcode) {
		this.businessShortcode = businessShortcode;
	}

	public String getStkPushPassword() {
		return stkPushPassword;
	}

	public void setStkPushPassword(String stkPushPassword) {
		this.stkPushPassword = stkPushPassword;
	}

	public String getStkTimestamp() {
		return stkTimestamp;
	}

	public void setStkTimestamp(String stkTimestamp) {
		this.stkTimestamp = stkTimestamp;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getPartyB() {
		return partyB;
	}

	public void setPartyB(String partyB) {
		this.partyB = partyB;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getAccountRefernece() {
		return accountRefernece;
	}

	public void setAccountRefernece(String accountRefernece) {
		this.accountRefernece = accountRefernece;
	}

	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecretKey() {
		return appSecretKey;
	}

	public void setAppSecretKey(String appSecretKey) {
		this.appSecretKey = appSecretKey;
	}

	public String getTerminalMsisdn() {
		return terminalMsisdn;
	}

	public void setTerminalMsisdn(String terminalMsisdn) {
		this.terminalMsisdn = terminalMsisdn;
	}

	public String getValidationPassword() {
		return validationPassword;
	}

	public void setValidationPassword(String validationPassword) {
		this.validationPassword = validationPassword;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getInitiator() {
		return initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public String getStatusResultURL() {
		return statusResultURL;
	}

	public void setStatusResultURL(String statusResultURL) {
		this.statusResultURL = statusResultURL;
	}


}
