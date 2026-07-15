package com.param.entity.org.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedNativeQueries({

	@NamedNativeQuery(name = "GET_CARD_DETAILS_BY_MPPER", query = 
	"select "
			+ "		loyalty_card.card_no as \"cardNo\", "
			+ "		config.card_configuration_id as \"cardConfigurationId\", "
			+ "		config.min_start_amount as \"minStartAmount\", "
			+ "		config.threshold_amount as \"thresholdAmount\", "
			+ "		config.min_recharge_amount as \"minRechargeAmount\", "
			+ "		coalesce(mppr.card_amount,0) as \"availableDeposit\" "
			+ "	from "
			+ "		public.m_loyalty_card loyalty_card "
			+ "	inner join public.m_loyalty_card_configuration config on "
			+ "		loyalty_card.unit_id = config.unit_id "
			+ "		and config.is_deleted = 'N' "
			+ "	inner join public.t_patient_loyalty_card_mppr mppr on "
			+ "		mppr.loyalty_card_id = loyalty_card.loyalty_card_id "
			+ "	where "
			+ "		loyalty_card.organisation_id = :orgId "
			+ "		and loyalty_card.unit_id = :unitId "
			+ "		and loyalty_card.status = 'A' "
			+ "		and loyalty_card.is_deleted = 'N' "
			+ "		and mppr.is_deleted = 'N' "
			+ "		and mppr.patient_loyalty_card_mppr_id = :patientLoyaltyCardMpprId"),


	@NamedNativeQuery(name = "GET_CARD_CONFIG_BY_UNIT", query = 
			" select "
			+ "		config.card_configuration_id as \"cardConfigurationId\", "
			+ "		config.min_start_amount as \"minStartAmount\", "
			+ "		config.threshold_amount as \"thresholdAmount\", "
			+ "		config.min_recharge_amount as \"minRechargeAmount\" "
			+ "	from "
			+ "	 public.m_loyalty_card_configuration config  "
			+ "	where "
			+ "	 config.unit_id = :unitId and config.is_deleted = 'N'"),
	
	
	@NamedNativeQuery(name="GET_LOYALTI_CARD_DETAILS_BY_PATIENT_ID",
	query=" SELECT loyalityMstr.loyalty_card_id as \"loyaltyCardId\",loyalityMstr.card_no as \"cardNo\",loyalityMstr.patient_id as \"patientId\","
			+ " loyalityMstr.is_user_defined as \"isUserDefined\", "
			+ " loyalityCardMppr.patient_loyalty_card_mppr_id as \"patientLoyaltyCardMpprId\",coalesce(loyalityCardMppr.card_amount,0) as \"cardAmount\","
			+ " loyalityCardMppr.card_configuration_id as \"cardConfigurationId\",loyalityCardMppr.is_verified as \"isVerified\","
			+ " loyalityCardMppr.otp as \"otp\",loyalityCardMppr.status as \"status\",loyalityCardMppr.otp_expired_time as \"otpExpiredTime\","
			+ " loyaltiConfig.min_recharge_amount as \"minRechargeAmount\",loyaltiConfig.min_start_amount as \"minStartAmount\" ,loyaltiConfig.threshold_amount as \"thresholdAmount\" ,"
			+ " concat(pr.first_name,' ', pr.last_name) as \"firstName\",pr.last_name as \"lastName\", pr.mobile_number as \"mobileNumber\",pr.email as \"email\"  "
			+ " FROM public.m_loyalty_card loyalityMstr "
			+ " INNER JOIN public.t_patient_loyalty_card_mppr loyalityCardMppr"
			+ " ON loyalityMstr.loyalty_card_id = loyalityCardMppr.loyalty_card_id "
			+ " INNER JOIN public.m_loyalty_card_configuration loyaltiConfig  ON loyalityCardMppr.card_configuration_id=loyaltiConfig.card_configuration_id"
			+ " INNER JOIN patient.registration pr ON pr.patient_id=loyalityMstr.patient_id "		
			+ " WHERE (loyalityMstr.is_deleted ='N' and loyalityCardMppr.is_deleted='N')"
			+ "  AND loyalityMstr.organisation_id=:orgId"
			+ "  AND loyalityMstr.unit_id=:unitId "
			+ "  and loyalityMstr.status='A' " 
			+ "  and loyalityMstr.is_deleted = 'N' " 
			+ "  and loyalityMstr.is_active = 'Y' "
			+ "  AND loyalityMstr.patient_id=:patientId"),
	
	@NamedNativeQuery(name="GET_PATIENT_DETAILS_BY_LOYALTI_CARD_NO",
	query=" SELECT loyalityMstr.loyalty_card_id as \"loyaltyCardId\",loyalityMstr.card_no as \"cardNo\",loyalityMstr.patient_id as \"patientId\","
			+ " loyalityMstr.is_user_defined as \"isUserDefined\", "
			+ " loyalityCardMppr.patient_loyalty_card_mppr_id as \"patientLoyaltyCardMpprId\",coalesce(loyalityCardMppr.card_amount,0) as \"cardAmount\","
			+ " loyalityCardMppr.card_configuration_id as \"cardConfigurationId\",loyalityCardMppr.is_verified as \"isVerified\","
			+ " loyalityCardMppr.otp as \"otp\",loyalityCardMppr.status as \"status\",loyalityCardMppr.otp_expired_time as \"otpExpiredTime\","
			+ " loyaltiConfig.min_recharge_amount as \"minRechargeAmount\",loyaltiConfig.min_start_amount as \"minStartAmount\" ,loyaltiConfig.threshold_amount as \"thresholdAmount\" ,"
			+ " concat(pr.first_name,' ', pr.last_name) as \"firstName\",pr.last_name as \"lastName\",pr.mobile_number as \"mobileNumber\", "
			+ " pr.email as \"email\"  "
			+ " FROM public.m_loyalty_card loyalityMstr "
			+ " INNER JOIN public.t_patient_loyalty_card_mppr loyalityCardMppr"
			+ " ON loyalityMstr.loyalty_card_id = loyalityCardMppr.loyalty_card_id "
			+ " INNER JOIN public.m_loyalty_card_configuration loyaltiConfig  ON loyalityCardMppr.card_configuration_id=loyaltiConfig.card_configuration_id"
			+ " INNER JOIN patient.registration pr ON pr.patient_id=loyalityMstr.patient_id "		
			+ " WHERE loyalityMstr.status='A' " 
			+ " AND loyalityMstr.is_deleted = 'N' " 
			+ " AND loyalityMstr.is_active = 'Y' "
			+ " AND loyalityMstr.organisation_id=:orgId "
			+ " AND loyalityMstr.unit_id=:unitId "),

	@NamedNativeQuery(name="GET_LOYALTI_DETAILS_BY_MAPPR_ID",
			query="select	loyalityMstr.card_no as \"cardNo\", "
					+ "	loyalityCardMppr.patient_loyalty_card_mppr_id as \"patientLoyaltyCardMpprId\", "				
					+ "	loyalityCardMppr.card_configuration_id as \"cardConfigurationId\", "
					+ "	loyalityCardMppr.is_verified as \"isVerified\", "
					+ "	loyalityCardMppr.otp as \"otp\", "
					+ " patient.email as \"email\","				
					+ "	loyalityCardMppr.otp_expired_time as \"otpExpiredTime\",coalesce(loyalityCardMppr.otp_count,0) as \"otpCount\",loyalityCardMppr.card_block_date as \"cardBlockDate\" "
					+ "from "
					+ "	public.m_loyalty_card loyalityMstr "
					+ "inner join public.t_patient_loyalty_card_mppr loyalityCardMppr on loyalityMstr.loyalty_card_id=loyalityCardMppr.loyalty_card_id "
					+ " inner join patient.t_patient_registration patient on patient.patient_id = loyalityMstr.patient_id "
					+ "where "
					+ "	 loyalityCardMppr.patient_loyalty_card_mppr_id =:loyaltiMpprId "
					+ "	  and loyalityMstr.card_no=:cardNo "),
	
	@NamedNativeQuery(name = "GET_UNIT_NAME_BY_ORG_ID_OR_UNIT_ID", query = 
	" select "
	+ "	unitmaster.unit_name as \"unitName\" "
	+ "	from "
	+ "	 public.m_unit_master unitmaster  "
	+ "	where "
	+ "	 unitmaster.unit_id = :unitId and unitmaster.organization_id = :orgId"),
	
})

@Entity
@Table(name="t_patient_loyalty_card_mppr",schema="public")
@SequenceGenerator(name="seq_patient_loyalty_card_mppr",sequenceName="public.seq_patient_loyalty_card_mppr",allocationSize=1)
public class PatientLoyaltyCardMapper {

	@Id
	@Column(name="patient_loyalty_card_mppr_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_patient_loyalty_card_mppr")
	private int patientLoyaltyCardMpprId;
	
	@Column(name="loyalty_card_id")
	private Integer loyaltyCardId;
	
	@Column(name="card_configuration_id")
	private Integer cardConfigurationId;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="organisation_id")
	private Integer organisationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="status")
	private char status;

	@Column(name="created_date")
	private Date createdDate;

	@Column(name="created_by")
	private int createdBy;

	@Column(name="updated_by")
	private int updatedBy;

	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="is_deleted")
	private char isDeleted;
	
	@Column(name="card_amount")
	private double cardAmount;
	
	@Column(name="otp")
	private Integer otp;
	
	@Column(name="otp_expired_time")
	private Date otpExpiredTime;

	@Column(name="is_verified")
	private char isVerified;
	
	@Column(name="otp_count")
	private Integer otpCount ;
	
	@Column(name="card_block_date")
	private Date cardBlockDate ;

	
	public int getPatientLoyaltyCardMpprId() {
		return patientLoyaltyCardMpprId;
	}

	public void setPatientLoyaltyCardMpprId(int patientLoyaltyCardMpprId) {
		this.patientLoyaltyCardMpprId = patientLoyaltyCardMpprId;
	}

	public Integer getLoyaltyCardId() {
		return loyaltyCardId;
	}

	public void setLoyaltyCardId(Integer loyaltyCardId) {
		this.loyaltyCardId = loyaltyCardId;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		 this.status = (status == '\u0000') ? 'A' : status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public char getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(char isDeleted) {
		 this.isDeleted = (isDeleted == '\u0000') ? 'N' : isDeleted;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getCardConfigurationId() {
		return cardConfigurationId;
	}

	public void setCardConfigurationId(Integer cardConfigurationId) {
		this.cardConfigurationId = cardConfigurationId;
	}

	public double getCardAmount() {
		return cardAmount;
	}

	public void setCardAmount(double cardAmount) {
		this.cardAmount = cardAmount;
	}

	public Integer getOtp() {
		return otp;
	}

	public void setOtp(Integer otp) {
		this.otp = otp;
	}

	public Date getOtpExpiredTime() {
		return otpExpiredTime;
	}

	public void setOtpExpiredTime(Date otpExpiredTime) {
		this.otpExpiredTime = otpExpiredTime;
	}

	public char getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(char isVerified) {
		 this.isVerified = (isVerified == '\u0000') ? 'N' : isVerified;
	}

	public Integer getOtpCount() {
		return otpCount;
	}

	public void setOtpCount(Integer otpCount) {
		this.otpCount = otpCount;
	}

	public Date getCardBlockDate() {
		return cardBlockDate;
	}

	public void setCardBlockDate(Date cardBlockDate) {
		this.cardBlockDate = cardBlockDate;
	}
		
}
