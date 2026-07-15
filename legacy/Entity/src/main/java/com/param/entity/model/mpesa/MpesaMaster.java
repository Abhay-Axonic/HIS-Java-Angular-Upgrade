package com.param.entity.model.mpesa;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;


@NamedNativeQueries({
		

		@NamedNativeQuery(name = "CHECK_UNIQUE_MPESA_AMOUNT", query = "SELECT id AS \"id\" ,"
				+ "mpeasa_amount AS \"mpesaAmount\", "
				+ "status AS \"status\", "
				+ "mpesa_txcode AS \"mpesaTxcode\" "
				+ "FROM billing.t_mpesa_master "
				+ "Where mpesa_txcode = :mpesaTxcode "),

		@NamedNativeQuery(name = "UPDATE_MPESA_STATUS", query = "UPDATE billing.t_mpesa_master "
				+ "	SET status= 'D' " 
				+ "	WHERE mpesa_txcode= :mpesaTxcode " 
				+ "	and updated_by = :updatedBy")
		
		
})
		

@Entity
@Table(name = "t_mpesa_master", schema = "billing")
@SequenceGenerator(name = "mpesa_master_seq", sequenceName = "billing.mpesa_master_seq", allocationSize = 1)
public class MpesaMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mpesa_master_seq")
	@Column(name = "id")
	private Integer id;

	@Column(name = "mpesa_account_number")
	private String mpesaAccountnumber;
	
	@Column(name = "mpeasa_amount")
	private Double mpeasaAmount;
	
	@Column(name = "mpesa_date_time")
	private Date mpesaDatetime;
	
	@Column(name = "mpesa_id")
	private String mpesaId;
	
	@Column(name = "mpesa_sender_mobile")
	private String mpesaSendermobile;
	
	@Column(name = "mpesa_sender_name")
	private String mpesaSendername;
	
	@Column(name = "mpesa_terminal")
	private String mpesaTerminal;
	
	@Column(name = "mpesa_txcode")
	private String mpesaTxcode;
	
	@Column(name="mpesa_tx_date")
	private Date mpesaTxdate;
	
	@Column(name = "uhid")
	private String uhid;
	
	@Column(name = "mpesa_balance")
	private String mpesaBalance;
	
	@Column(name = "organisation_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="status")
	private String status;
	
	@Column(name="is_checked")
	private boolean  isChecked;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMpesaAccountnumber() {
		return mpesaAccountnumber;
	}

	public void setMpesaAccountnumber(String mpesaAccountnumber) {
		this.mpesaAccountnumber = mpesaAccountnumber;
	}

	public Double getMpeasaAmount() {
		return mpeasaAmount;
	}

	public void setMpeasaAmount(Double mpeasaAmount) {
		this.mpeasaAmount = mpeasaAmount;
	}

	public Date getMpesaDatetime() {
		return mpesaDatetime;
	}

	public void setMpesaDatetime(Date mpesaDatetime) {
		this.mpesaDatetime = mpesaDatetime;
	}

	public String getMpesaId() {
		return mpesaId;
	}

	public void setMpesaId(String mpesaId) {
		this.mpesaId = mpesaId;
	}


	public String getMpesaSendermobile() {
		return mpesaSendermobile;
	}

	public void setMpesaSendermobile(String mpesaSendermobile) {
		this.mpesaSendermobile = mpesaSendermobile;
	}

	public String getMpesaSendername() {
		return mpesaSendername;
	}

	public void setMpesaSendername(String mpesaSendername) {
		this.mpesaSendername = mpesaSendername;
	}

	public String getMpesaTerminal() {
		return mpesaTerminal;
	}

	public void setMpesaTerminal(String mpesaTerminal) {
		this.mpesaTerminal = mpesaTerminal;
	}

	public String getMpesaTxcode() {
		return mpesaTxcode;
	}

	public void setMpesaTxcode(String mpesaTxcode) {
		this.mpesaTxcode = mpesaTxcode;
	}

	public Date getMpesaTxdate() {
		return mpesaTxdate;
	}

	public void setMpesaTxdate(Date mpesaTxdate) {
		this.mpesaTxdate = mpesaTxdate;
	}

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
	}

	public String getMpesaBalance() {
		return mpesaBalance;
	}

	public void setMpesaBalance(String mpesaBalance) {
		this.mpesaBalance = mpesaBalance;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(Boolean isChecked) {
		this.isChecked = isChecked;
	}

	
	
	
}
